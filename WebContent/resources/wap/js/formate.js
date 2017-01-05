$.ajax({
	url : "${base}/wap/mutual/get_config.jhtml",
	data : {
		url : location.href.split('#')[0]
	},
	dataType : "json",
	type : "get",
	success : function(message_wx) {
		if (message_wx.type == "success") {
			var data = JSON.parse(message_wx.content);
			wx.config({
				debug : true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId : data.appId, // 必填，公众号的唯一标识
				timestamp : data.timestamp, // 必填，生成签名的时间戳
				nonceStr : data.nonceStr, // 必填，生成签名的随机串
				signature : data.signature,// 必填，签名，见附录1
				jsApiList : [ "chooseWXPay" ]
			// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			wx.ready(function() {
				$.ajax({
					url : "${base}/wap/member/order/payment.jhtml",
					data : {
						sn : message.content,
						type : "payment"
					},
					dataType : "json",
					type : "post",
					beforeSend : function() {
						$submit.prop("disabled", true);
						invokTips("warn", "支付中");
					},
					success : function(data) {
						if (data.message.type == "success") {
							var jsonObject = JSON.parse(data.dataValue);
							// 调用微信JS api 支付
							wx.chooseWXPay({
								timestamp : jsonObject.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
								nonceStr : jsonObject.nonceStr, // 支付签名随机串，不长于
																// 32 位
								package : jsonObject.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
								signType : jsonObject.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
								paySign : jsonObject.paySign, // 支付签名
								success : function(res) {
									alert(res.err_msg);
									// 支付成功后的回调函数
									if ("get_brand_wcpay_request:ok" == res.err_msg) {
										invokTips("success", "支付成功！");
										setTimeout(function() {
											location.href = "${base}/wap/member/order/list.jhtml";
											return false;
										}, 3000);
									} else if ("chooseWXPay:cancel" == res.err_msg.errMsg) {
										invokTips("warn", "支付取消！");
										setTimeout(function() {
											location.href = "${base}/wap/member/order/list.jhtml";
											return false;
										}, 3000);
									} else {
										invokTips("error", "支付失败！");
										setTimeout(function() {
											location.href = "${base}/wap/member/order/list.jhtml";
											return false;
										}, 3000);
									}
									return false;
								}
							});
						} else {
							invokTips("error", "发起支付失败");
						}
						$submit.prop("disabled", true);
					}
				});
			});

			wx.error(function(res) {
				// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
			});
			wx.checkJsApi({
				jsApiList : [ 'chooseWXPay' ], // 需要检测的JS接口列表，所有JS接口列表见附录2,
				success : function(res) {
					// 以键值对的形式返回，可用的api值true，不可用为false
					// 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
				}
			});

		}
	}
});