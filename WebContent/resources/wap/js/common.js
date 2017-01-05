var wit = {
	base : "/wap",
	locale : "zh_CN"
};
(function($) {
	WapJS = function() {
		// css 动画
		this.doScale = function(element, scaleAction) {
			$(element).addClass(scaleAction).one($.AMUI.support.animation.end, function() {
				$(element).removeClass(scaleAction);
			});
		}
		// 遮罩层
		this.modalFade = function(content, fade) {
			if ($("#wap-modal-pannel").lenght == 0) {
				var modalHtml = '<div class="am-modal am-modal-no-btn" tabindex="-1" id="wap-modal-pannel"><div class="wap-noicon-tips"><div class="am-modal-bd"></div></div></div>';
				$("body").append(modalHtml);
			}
			$(".am-modal-bd").text(content);
			$("#wap-modal-pannel").modal("open");
			if (fade) {
				setTimeout(function() {
					$("#wap-modal-pannel").modal("close");
				}, fade);
			}
		}
	}
	// 判断是否微信浏览器
	$.isWeiXin = function() {
		var ua = window.navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == 'micromessenger') {
			return true;
		} else {
			return false;
		}
	}
	// 检测登录
	$.checkLogin = function() {
		var result = false;
		$.ajax({
			url : "/common/check.jhtml",
			type : "GET",
			dataType : "json",
			cache : false,
			async : false,
			success : function(data) {
				result = data;
			}
		});
		return result;
	}

})(window.jQuery)

function init() {
	lazyloadimg();
	new FastClick(document.body);
}
function hScroll(element,fastclick) {
		 var iScroll= new IScroll("#" + element,{
		 	scrollY:false,
		 	scrollX:true,
		 	eventPassthrough:'vertical',
		 	lockDirection: true
		 });
		 if (fastclick) {
      	new FastClick(document.body);
		 }
		 return iScroll;
}
function vScroll(element,fastclick) {
		 var iScroll= new IScroll("#" + element,{
		 	scrollY:true,
		 	scrollX:false,
		 	eventPassthrough:'horizontal',
		 	lockDirection: true
		 });
		 if (fastclick) {
      	new FastClick(document.body);
		 }
		 return iScroll;
}

function refreshScroll(iScroll) {
  iScroll.refresh();
  new FastClick(document.body);
}

function lazyloadimg() {
	if (typeof ($(".lazy")) != "undefined" && $(".lazy").length > 0) {
		$(".lazy").lazyload({
			effect : "fadeIn",
			threshold : 100
		});
	}
}
function invokTips(type, content) {
	if ($("#inok").length == 0) {
		var html = '<div class="inok" id="inok" style=""><i class="am-icon-check"></i>&nbsp;操作成功</div>';
		$("body").append(html);
	}
	if (type === "success") {
		$('#inok').html("<i class=\"am-icon-check\"></i>&nbsp;" + content);
	} else if (type === "error") {
		$('#inok').html("<i class=\"am-icon-close\"></i>&nbsp;" + content);
	} else if (type === "warn") {
		$('#inok').html("<i class=\"am-icon-exclamation-triangle\"></i>&nbsp;" + content);
	}
	$("#inok").show();
	setTimeout(function() {
		$('#inok').fadeOut(1000)
	}, 1000);
}
$("#chooseLocation").on("close.modal.amui", function() {
	window.location.href = SELF
});

// 对话框
function showconfirm(info, b, url, forword) {
	var e = $("#modal-confirm");
	e.find(".am-modal-bd").text(info);
	e.modal({
		relatedTarget : b,
		onConfirm : function(options) {
			$.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				success : function(data) {
					invokTips(data.type, data.content);
					setTimeout(function() {
						if (forword) {
							location.href = forword;
						} else {
							location.reload(true);
						}
					}, 600);
				}
			});
		}
	})
}
// 添加Cookie
function addCookie(name, value, options) {
	if (arguments.length > 1 && name != null) {
		if (options == null) {
			options = {};
		}
		if (value == null) {
			options.expires = -1;
		}
		if (typeof options.expires == "number") {
			var time = options.expires;
			var expires = options.expires = new Date();
			expires.setTime(expires.getTime() + time * 1000);
		}
		document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value))
				+ (options.expires ? "; expires=" + options.expires.toUTCString() : "") + (options.path ? "; path=" + options.path : "")
				+ (options.domain ? "; domain=" + options.domain : ""), (options.secure ? "; secure" : "");
	}
}
// 获取Cookie
function getCookie(name) {
	if (name != null) {
		var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
		return value ? decodeURIComponent(value[1]) : null;
	}
}
// json时间格式的格式化显示
Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};
$(function() {

	$("#scan_fun").on("click",function(){
		$.ajax({
			url:"/wap/mutual/get_config.jhtml",
			data:{
				url:location.href.split('#')[0]
			},
			dataType:"json",
			type:"get",
			success:function(message){
				if(message.type=="success"){
					var data=JSON.parse(message.content);
					wx.config({
					    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
					    appId: data.appId, // 必填，公众号的唯一标识
					    timestamp: data.timestamp, // 必填，生成签名的时间戳
					    nonceStr: data.nonceStr, // 必填，生成签名的随机串
					    signature: data.signature,// 必填，签名，见附录1
					    jsApiList: ["scanQRCode"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
					});
					wx.ready(function(){
						wx.scanQRCode({
					      desc: '扫一扫'
					    });
					});
					wx.error(function(res){
					    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
					});
					wx.checkJsApi({
					    jsApiList: ['scanQRCode'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
					    success: function(res) {
					        // 以键值对的形式返回，可用的api值true，不可用为false
					        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
					    }
				    });
				}else{
					invokTips("error",message.content);
				}
			}
		});
    });

	// 返回按钮
	if ($(".wap-rebk").length > 0) {
		$(".wap-rebk").on("click", function() {
		    if($.isWeiXin()){
			   history.back(-1);
			} else {
			   location.href="vsstoo://back";
			}
		});
	}
	if ($("#cnum").length > 0) {
		var count = getCookie("cartCount");
		if (count != null && parseInt(count) > 0) {
			$("#cnum").text(count);
			$("#cnum").removeClass("am-hide");
		} else {
			$("#cnum").addClass("am-hide");
		}
	}
	// 评价星星
	if ($('.wap-trade-review').length > 0) {

		$('.w_satrlist_1 i').on('click', function() {
			$('.w_satrlist_1 i').addClass('w_down');
			var i = $(this).index();
			$('.w_satrlist_1 i').eq(i).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 1).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 2).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 3).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 4).removeClass('w_down');
		});
		$('.w_satrlist_2 i').on('click', function() {
			$('.w_satrlist_2 i').addClass('w_down');
			var i = $(this).index();
			$('.w_satrlist_2 i').eq(i).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 1).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 2).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 3).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 4).removeClass('w_down');
		});
		$('.w_satrlist_3 i').on('click', function() {
			$('.w_satrlist_3 i').addClass('w_down');
			var i = $(this).index();
			$('.w_satrlist_3 i').eq(i).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 1).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 2).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 3).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 4).removeClass('w_down');
		});
	}
});