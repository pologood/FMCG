<!doctype html>
<html>
<head>
    [#include "/wap/include/resource.ftl"]
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <title>订单详情</title>
    <style>
        #wrapper {
            top: 49px;
            overflow: hidden;
            background-color: rgb(238, 238, 238);
            padding-right: 0px;
            padding-left: 0px;
            bottom: 49px;
        }

    </style>
    <script>
        $(function () {
	setTimeout(function(){
	  init();
  },200);
            
            $("#paySubmit").on("click",function(){
            	var paySn=$(this).attr("sn");
            	[#if  browse_version =="MicroMessenger"]
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
				debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
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
						sn : paySn,
						type : "payment"
					},
					dataType : "json",
					type : "post",
					beforeSend : function() {
						$(this).prop("disabled", true);
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
									} else if ("chooseWXPay:cancel" == res.errMsg) {
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
						$(this).prop("disabled", true);
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
                             [#else]
                             	//不是微信浏览器
                                	$.ajax({
						url:"${base}/mobile/payment/submit.jhtml",
						type:"post",
						data:{
							sn:paySn,
							type:"payment",
							paymentPluginId:"chinapayMobilePlugin"
						},
						dataType:"json",
						success:function(message){
							if(message.type=="success"){
								location.href="vsstoo://payservice/?token=none&paydata="+message.content;
								return false;
							}else{
								setTimeout(function(){
									location.href="${base}/wap/member/order/list.jhtml";
									return false;
								},1500);
							}
						},
						beforeSend:function(){
							invokTips("warn","支付中。。。");
						}
					});
                             [/#if]   
            	
            });
            
        });
//对话框
        function showconfirm(info, b,url,forword) {
            var e = $("#modal-confirm");
            e.find(".am-modal-bd").text(info);
            e.modal({
                relatedTarget: b, 
                onConfirm: function (options) {
                	$.ajax({
				url: url,
				type: "POST",
				dataType: "json",
				success: function(data) {
					invokTips(data.type,data.content);
					setTimeout(function(){
						if(forword){
							location.href=forword;
						}else{
						location.reload(true);
						}
					},600);
				}
			});
                }
            })
        }
    </script>
</head>

<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">订单详情</h1>
      </header>
    </header>
    <div id="wrapper">
        <div>
            <div class="am-g">
                <div class="wap-address">
                    <address>
                        <p><i class="am-icon-user am-icon-xs "></i><span>${trade.order.consignee}</span>
                            <i class="am-icon-phone" style="margin-left: 35px;"></i><span>${trade.order.phone}</span>
                        </p>
                        <p><i class="am-icon-map-marker"></i><span>${trade.order.address}</span></p>
                    </address>
                </div>
            </div>
            <div class="am-g am-margin-top-sm am-padding-sm wap-order-detail">
                <div>
                    <p class="am-margin-vertical-0">订单号：<span>${trade.order.sn}</span></p>
                    <p class="am-margin-vertical-0">订单状态：<span>${message("Order.OrderStatus." + trade.orderStatus)}</span></p>
                    <p class="am-margin-vertical-0">提货码：<span>${trade.sn}</span></p>
                    <p class="am-margin-vertical-0">成交时间：<span>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></p>
                    <p class="am-margin-vertical-0">调整金额：<span>${trade.offsetAmount?string("0.00")}</span></p>
                    <p class="am-margin-vertical-0">运费：<span>${trade.freight?string("0.00")}</span></p>
                    <p class="am-margin-vertical-0">税率：<span>${trade.tax?string("0.00")}</span></p>
                </div>
            </div>
            <div class="am-g am-margin-top-sm">
                <div class="am-panel am-panel-default">
                    <div class="am-panel-hd wap-trade-header"  >
                    	<img class="am-circle" src="${base}/resources/wap/image/NoPicture.jpg" data-original="${trade.tenant.thumbnail}" style="width: 24px;"/>
                        ${trade.tenant.name}
                        [#list trade.finalOrderStatus as fos]
					            		[#if fos.status=='isExpired']
						             		<a class="trade_detail_unable am-fr" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='cancelled']	
								            <a class="trade_detail_unable am-fr" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='toReview']	
							             	<a href="${base}/wap/member/trade/review.jhtml?sn=${trade.sn}" class="trade_detail_abled reviewTrade am-fr" >${fos.desc}</a>
						             	[#elseif fos.status=='completed']	
						             		<a class="trade_detail_unable am-fr" href="javascript:;">${fos.desc}<${message("Order.ShippingStatus."+trade.shippingStatus)}></a>
						             	[#elseif fos.status=='waitReturn']	
						             		<a class="trade_detail_unable am-fr" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='waitShipping']
						             		<a href="javascript:showconfirm('您是否要退货?',this,'${base}/wap/member/trade/returnTrade.jhtml?sn=${trade.sn}');"  class="trade_detail_abled returnTrade am-fr">退货</a>
						             		<a class="trade_detail_unable am-fr" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='sign']	
						             		<a href="javascript:showconfirm('您是否要签收?',this,'${base}/wap/member/trade/complete.jhtml?sn=${trade.sn}','${base}/wap/member/trade/review.jhtml?sn=${trade.sn}');"  class="trade_detail_abled sign_order am-fr">${fos.desc}</a>
						             		<a href="javascript:showconfirm('您是否要退货?',this,'${base}/wap/member/trade/returnTrade.jhtml?sn=${trade.sn}');" class="trade_detail_abled returnTrade am-fr">退货</a>
						             	[#elseif fos.status=='waitPay']	
						             		<a class="trade_detail_abled cancel_order am-fr" href="javascript:showconfirm('您是否要取消?',this,'${base}/wap/member/order/cancel.jhtml?sn=${trade.order.sn}');">取消</a>
					            		[/#if]
					            	[/#list]
                    </div>
                    <div class="am-panel-bd am-padding-0">
                        <div class="am-list-news-bd">
                            <ul class="am-list wap-info-list">
                            	[#list trade.orderItems as orderItem]
                            	<li class="am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left "
                               		style="padding: 0.5rem;">
                                <div class="am-u-sm-4 am-list-thumb">
                                <a href="${base}/wap/product/content/${orderItem.product.id}.jhtml">
                                    <img class="am-img-responsive lazy" src="${base}/resources/wap/image/NoPicture.jpg"
                                         data-original="${orderItem.product.thumbnail}"
                                           style="height: 80px;" />
                                  </a>         </div>
                                <div class=" am-u-sm-8 am-list-main" style="height: 80px;">
                                    <p class="am-list-item-hd">${abbreviate(orderItem.product.fullName,42,"..")}
                                    </p>
                                    <div class="am-list-item-text" style="position: absolute;bottom: 0px;width: 84%">
                                        <strong class="am-text-danger">
                                        	<i class="am-icon-cny"></i>${orderItem.price}
                                    	</strong>
                                    	<span style="float: right;">x ${orderItem.quantity}${orderItem.packagUnitName}</span></div>
                                </div>
                            </li>
                            	[/#list]
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div data-am-widget="navbar"
         class="am-navbar am-cf am-navbar-secondary am-no-layout wap-static-float-bottom wap-order-detail-bottom">
        <p class="am-margin-0 am-text-center" >总计：<span>${currency(trade.amount,true)}</span></p>
        	[#if trade.paymentStatus == 'unpaid' && !trade.expired && trade.order.orderStatus!='cancelled'&&trade.order.paymentMethod.method!='offline']
		      	 	<a class="o_button_red am-center am-text-center" href="javascript:;" id="paySubmit" sn="${trade.order.sn}">去付款</a>
		      	[/#if]
		      	[#if trade.order.orderStatus =='cancelled']
		      		<a class="am-center am-text-center" >已取消</a>
		      	[/#if]
            
    </div>
    <div class="am-modal am-modal-confirm" tabindex="-1" id="modal-confirm">
            <div class="am-modal-dialog">
                <div class="am-modal-hd">提示</div>
                <div class="am-modal-bd"></div>
                <div class="am-modal-footer"><span class="am-modal-btn" data-am-modal-cancel="">取消</span><span
                        class="am-modal-btn" data-am-modal-confirm="">确定</span></div>
            </div>
        </div>
</div>
</body>
</html>
