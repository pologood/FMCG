/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.wit.net
 * License: http://www.wit.net/license
 * 
 * JavaScript - Common
 * Version: 3.0
 */

var wit = {
	base: "/mobile",
	locale: "zh_CN"
};
var setting = {
	priceScale: "2",
	priceRoundType: "roundUp",
	currencySign: "￥",
	currencyUnit: "元",
	uploadImageExtension: "jpg,jpeg,bmp,gif,png",
	uploadFlashExtension: "swf,flv",
	uploadMediaExtension: "swf,flv,mp3,wav,avi,rm,rmvb",
	uploadFileExtension: "zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx"
};
var messages = {
	"shop.message.success": "操作成功",
	"shop.message.error": "操作错误",
	"shop.dialog.ok": "确&nbsp;&nbsp;定",
	"shop.dialog.cancel": "取&nbsp;&nbsp;消",
	"shop.dialog.deleteConfirm": "您确定要删除吗？",
	"shop.dialog.clearConfirm": "您确定要清空吗？",
	"shop.browser.title": "选择文件",
	"shop.browser.upload": "本地上传",
	"shop.browser.parent": "上级目录",
	"shop.browser.orderType": "排序方式",
	"shop.browser.name": "名称",
	"shop.browser.size": "大小",
	"shop.browser.type": "类型",
	"shop.browser.select": "选择文件",
	"shop.upload.sizeInvalid": "上传文件大小超出限制",
	"shop.upload.typeInvalid": "上传文件格式不正确",
	"shop.upload.invalid": "上传文件格式或大小不正确",
	"shop.validate.required": "必填",
	"shop.validate.email": "E-mail格式错误",
	"shop.validate.url": "网址格式错误",
	"shop.validate.date": "日期格式错误",
	"shop.validate.dateISO": "日期格式错误",
	"shop.validate.pointcard": "信用卡格式错误",
	"shop.validate.number": "只允许输入数字",
	"shop.validate.digits": "只允许输入零或正整数",
	"shop.validate.minlength": "长度不允许小于{0}",
	"shop.validate.maxlength": "长度不允许大于{0}",
	"shop.validate.rangelength": "长度必须在{0}-{1}之间",
	"shop.validate.min": "不允许小于{0}",
	"shop.validate.max": "不允许大于{0}",
	"shop.validate.range": "必须在{0}-{1}之间",
	"shop.validate.accept": "输入后缀错误",
	"shop.validate.equalTo": "两次输入不一致",
	"shop.validate.remote": "输入错误",
	"shop.validate.integer": "只允许输入整数",
	"shop.validate.positive": "只允许输入正数",
	"shop.validate.negative": "只允许输入负数",
	"shop.validate.decimal": "数值超出了允许范围",
	"shop.validate.pattern": "格式错误",
	"shop.validate.extension": "文件格式错误"
};
$(function(){
	 var ua = navigator.userAgent.toLowerCase();
     var isSafari = ua.indexOf('iphone')>-1 && ua.indexOf('safari')>-1 && ua.indexOf('version') >-1;
	 var allmap = $('#allmap');
 	 var allmap_list = $('#allmap_list');
		 function hireUrl(){
		 $('.p_section').css("height",window.innerHeight+100 - $('#christmas').height() - $('#spring_festival').height() +'px');  //强制让内容超过
         window.scrollTo(0, 0);
      }
	  $('body,section').on('touchmove',function(event){event.preventDefault();});
});

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
		document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires ? "; expires=" + options.expires.toUTCString() : "") + (options.path ? "; path=" + options.path : "") + (options.domain ? "; domain=" + options.domain : ""), (options.secure ? "; secure" : "");
	}
}

// 获取Cookie
function getCookie(name) {
	if (name != null) {
		var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
		return value ? decodeURIComponent(value[1]) : null;
	}
}

// 移除Cookie
function removeCookie(name, options) {
	addCookie(name, null, options);
}
// 货币格式化
function currency(value, showSign, showUnit) {
	if (value != null) {
		var price;
		if (setting.priceRoundType == "roundHalfUp") {
			price = (Math.round(value * Math.pow(10, setting.priceScale)) / Math.pow(10, setting.priceScale)).toFixed(setting.priceScale);
		} else if (setting.priceRoundType == "roundUp") {
			price = (Math.ceil(value * Math.pow(10, setting.priceScale)) / Math.pow(10, setting.priceScale)).toFixed(setting.priceScale);
		} else {
			price = (Math.floor(value * Math.pow(10, setting.priceScale)) / Math.pow(10, setting.priceScale)).toFixed(setting.priceScale);
		}
		if (showSign) {
			price = setting.currencySign + price;
		}
		if (showUnit) {
			price += setting.currencyUnit;
		}
		return price;
	}
}

// 多语言
function message(code) {
	if (code != null) {
		var content = messages[code] != null ? messages[code] : code;
		if (arguments.length == 1) {
			return content;
		} else {
			if ($.isArray(arguments[1])) {
				$.each(arguments[1], function(i, n) {
					content = content.replace(new RegExp("\\{" + i + "\\}", "g"), n);
				});
				return content;
			} else {
				$.each(Array.prototype.slice.apply(arguments).slice(1), function(i, n) {
					content = content.replace(new RegExp("\\{" + i + "\\}", "g"), n);
				});
				return content;
			}
		}
	}
}

$(function() {
	var ua = navigator.userAgent.toLowerCase();
	var isSafari = ua.indexOf('iphone') > -1 && ua.indexOf('safari') > -1 && ua.indexOf('version') > -1;
	function hireUrl() {
		$('.m_section').css("height", window.innerHeight + 100 - $('#christmas').height() - $('#spring_festival').height() + 'px'); // 强制让内容超过
		window.scrollTo(0, 0);
	}
	$('body,section').on('touchmove', function(event) {
		event.preventDefault();
	});
});
$(function() {
	// $('header').addClass('topina');
	// $('.m_inquiry').addClass('rightin');
	// $('article').addClass('bottomin');
	// $('footer').addClass('bottomin');
	// $('.m_letter_list').addClass('bottomin');
	// $('.m_shopp').addClass('rightin');

	// 同一返回浏览器上一个记录
	if ($("#return_btn").length > 0) {
		$("#return_btn").on("tap", function() {
			location.href = "vsstoo://back";
			return false;
		})
	}

	// article 滚动
	if ($('#m_scrooler').length > 0) {
		var actscrooler = $('#m_scrooler'), actscrooll = new iScroll('m_scrooler', {
			onBeforeScrollStart : function() {
				actscrooler.iScroll('refresh');
			}
		});
	}
	// article 滚动
	if ($('#m_scrooler_1').length > 0) {
		var actscrooler1 = $('#m_scrooler_1'), actscrooll1 = new iScroll('m_scrooler_1', {
			onBeforeScrollStart : function() {
				actscrooler1.iScroll('refresh');
			}
		});
	}
	// article 分类滚动
	if ($('#m_clscrooler').length > 0) {
		var clscrooler = $('#m_clscrooler'), clscrooll = new iScroll('m_clscrooler', {
			onBeforeScrollStart : function() {
				clscrooler.iScroll('refresh');
			}
		}), crscrooler = $('#m_crscrooler'), crscrooll = new iScroll('m_crscrooler', {
			onBeforeScrollStart : function() {
				crscrooler.iScroll('refresh');
			}
		});
	}
	// 列表筛选滚动
	if ($('#m_areascrooler_1').length > 0) {
		var areascrooler1 = $('#m_areascrooler_1'), areascrooll1 = new iScroll('m_areascrooler_1', {
			onBeforeScrollStart : function() {
				areascrooler1.iScroll('refresh');
			}
		}), areascrooler2 = $('#m_areascrooler_2'), areascrooll2 = new iScroll('m_areascrooler_2', {
			onBeforeScrollStart : function() {
				areascrooler2.iScroll('refresh');
			}
		}), areascrooler3 = $('#m_areascrooler_3'), areascrooll3 = new iScroll('m_areascrooler_3', {
			onBeforeScrollStart : function() {
				areascrooler3.iScroll('refresh');
			}
		});
	}
	if ($('.m_area').length > 0) {
		var areascrooler4 = $('#m_areascrooler_4'), areascrooll4 = new iScroll('m_areascrooler_4', {
			onBeforeScrollStart : function() {
				areascrooler4.iScroll('refresh');
			}
		});
	}

	// 区域滚动
	if($('.m_Areas_cont').length > 0) {
		var Areas_scroole_1 = $('#m_Areas_scrooler_1'), Areasscrooll1 = new iScroll('m_Areas_scrooler_1', {
			onBeforeScrollStart : function() {
				Areas_scroole_1.iScroll('refresh');
			}
		}), Areas_scroole_2 = $('#m_Areas_scrooler_2'), Areasscrooll2 = new iScroll('m_Areas_scrooler_2', {
			onBeforeScrollStart : function() {
				Areas_scroole_2.iScroll('refresh');
			}
		});
	}
	if ($('.m_search').length > 0) {
		// 搜索效果
		$('.m_search').on('tap', function() {
			$('.m_search').addClass('m_search_all');
			$('.m_headercont').addClass('m_headercont_nobg');
			$('.m_city,.m_scan').hide();
			$('.m_searchbtn').show();
			$('.m_bodybg').show().addClass('ddin');
			if ($('.m_return').length > 0) {
				$('.m_return').hide();
			}
			if ($('.m_search_style').length > 0) {
				$('.m_search').removeClass('m_search_n');
			}
			if ($('.m_inquiry').length > 0) {
				$('.m_area').hide().removeClass('ddin');
				$('.m_inquiry ul li').removeClass('m_inquirydown');
			}
			if ($('.m_inquiry').length > 0) {
				$('.m_inquiry').hide();
			}
		});
		$('.m_bodybg').on('tap', function() {
			$('.m_search').removeClass('m_search_all');
			$('.m_headercont').removeClass('m_headercont_nobg');
			$('.m_city,.m_scan').show();
			$('.m_searchbtn').hide();
			$('.m_bodybg').addClass('ddout').removeClass('ddin');
			if ($('.m_return').length > 0) {
				$('.m_return').show();
			}
			if ($('.m_search_style').length > 0) {
				$('.m_search').addClass('m_search_n');
			}
			if ($('.m_inquiry').length > 0) {
				$('.m_area').hide().removeClass('ddin');
				$('.m_inquiry ul li').removeClass('m_inquirydown');
				$('.m_inquiry').show();
			}
			$.later(function() {
				$('.m_bodybg').hide();
			}, 500, false);
		});
	}
	if ($('.m_roombanner').length > 0) {
		// 图片滚动
		var slide = new TouchSlider({
			id : 'm_roomslider',
			auto : false,
			speed : 300,
			timeout : 2000
		});
		var slideFlag = $('.m_round').find('span');
		var slide = new TouchSlider({
			id : 'm_roomslider',
			before : function(index) {
				slideFlag.removeClass('themeStyle');
				slideFlag.eq(index).addClass('themeStyle');
			}
		});

		var m_round = $('.m_round').children('span').length;
		$('.m_round').css('margin-left', -m_round * 5 + 'px');
	}
	if ($('.m_classone').length > 0) {
		$('.m_classone ul li').on('tap', function() {			
			$('.m_articlecl').addClass('down');
			$('.m_articlecr').show().addClass('rightina');
			$('.m_classone').addClass('lidown');
			$('.m_classone ul li').removeClass('m_classdown');
			$(this).addClass('m_classdown');
		});
	}
	// 分类推荐滚动
	if ($('.m_classtj').length > 0) {
		var classtjlist = $('.m_classtj ul').children('li').length;
		$('.m_classtj ul').css('width', classtjlist * 100 + 'px');

		var m_cltscroller = new iScroll('m_cltscroller', {
			snap : true,
			momentum : true,
		});
	}
	// 分类推荐滚动
	if ($('#m_shoptscroll').length > 0) {
		var classtjlist = $('#m_shoptscroll ul').children('li').length;
		$('#m_shoptscroll ul').css('width', classtjlist * 80 + 'px');

		var m_shoptscroller = new iScroll('m_shoptscroll', {
			snap : true,
			momentum : true,
		});
	}
	// 商家分类
	if ($('#m_shoptscroll_1').length > 0) {
		var classtjlist_1 = $('#m_shoptscroll_1 ul').children('li').length;
		$('#m_shoptscroll_1 ul').css('width', classtjlist_1 * 80 + 'px');

		var m_shoptscroller_1 = new iScroll('m_shoptscroll_1', {
			snap : true,
			momentum : true,
		});
	}
	// 列表筛选
	if ($('.m_inquiry').length > 0) {
		var i = "";
		$('.m_inquiry ul li').on('tap', function() {
			i = $(this).index();
			if ($(this).hasClass('m_inquirydown')) {
				$(this).removeClass('m_inquirydown');
				$('.m_area').hide().removeClass('ddin');
				$('.m_bodybg').addClass('ddout').removeClass('ddin');
				$.later(function() {
					$('.m_bodybg').hide();
				}, 500, false);
			} else {
				$('.m_inquiry ul li').removeClass('m_inquirydown');
				$(this).addClass('m_inquirydown');
				$('.m_area').hide().removeClass('ddin');
				$('.m_area').eq(i).show().addClass('ddin');
				$('.m_bodybg').show().addClass('ddin');
			}
		});
		$('.m_areal ul li').on('tap', function() {
			$('.m_areal ul li').removeClass('m_down');
			$(this).addClass('m_down');
			$('.m_arear').iScroll('refresh');
		});
	}
	// 列表搜索
	if ($('.m_receiptsech').length > 0) {
		$('.m_receiptsech').on('tap', function() {
			$('.m_receiptsechbtn').addClass('ddin').removeClass('ddout').show();
		});
		$('.m_receiptsechbtn').on('tap', function() {
			$('.m_receiptsechbtn').addClass('ddout').removeClass('ddin');
			$.later(function() {
				$('.m_receiptsechbtn').hide();
			}, 500, false);
		});
	}
	// 列表搜索
	if ($('.m_dzadd').length > 0) {
		$('.m_dzadd').on('tap', function() {
			$('.m_bodybg_1').show().addClass('ddin').removeClass('ddout');
			$.later(function() {
				$('.m_adddzc').show().addClass('topina').removeClass('topout');
			}, 500, false);
		});
		$('.p_tipsclose').on('tap', function() {
			m_adddzc();
		});
		$('.m_bodybg_1').on('tap', function() {
			m_adddzc();
		});
		function m_adddzc() {
			$('.m_adddzc').addClass('topout').removeClass('topina');
			$.later(function() {
				$('.m_bodybg_1').removeClass('ddin').addClass('ddout');
			}, 500, false);
			$.later(function() {
				$('.m_adddzc').hide();
				$('.m_bodybg_1').hide();
			}, 1000, false);
			$('.m_arear_dz1').hide();
			$('.m_arear_dz2').hide();
			$('.m_arear_dz3').hide();
		}
	}
	if ($('.p_unit').length > 0) {
		// 列表点击单位切换
		$('.p_unit').addClass('p_dw');
		$('.p_unit').on('tap', function() {
			if ($(this).hasClass('p_dw')) {
				$(this).removeClass('p_dw');
			} else {
				$(this).addClass('p_dw');
			}
		});
	}
	// 评价星星
	if ($('.w_satrlist').length > 0) {

		$('.w_satrlist_1 i').on('tap', function() {
			$('.w_satrlist_1 i').addClass('w_down');
			var i = $(this).index();
			$('.w_satrlist_1 i').eq(i).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 1).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 2).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 3).removeClass('w_down');
			$('.w_satrlist_1 i').eq(i + 4).removeClass('w_down');
		});
		$('.w_satrlist_2 i').on('tap', function() {
			$('.w_satrlist_2 i').addClass('w_down');
			var i = $(this).index();
			$('.w_satrlist_2 i').eq(i).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 1).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 2).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 3).removeClass('w_down');
			$('.w_satrlist_2 i').eq(i + 4).removeClass('w_down');
		});
		$('.w_satrlist_3 i').on('tap', function() {
			$('.w_satrlist_3 i').addClass('w_down');
			var i = $(this).index();
			$('.w_satrlist_3 i').eq(i).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 1).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 2).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 3).removeClass('w_down');
			$('.w_satrlist_3 i').eq(i + 4).removeClass('w_down');
		});
	}

	// 收藏
	$('.m_collect_tab li').on('click', function() {
		$('.m_collect_tab li').removeClass('m_down');
		$(this).addClass('m_down');
		tabli = $(this).index();
		if (tabli > 0) {
			$('.m_article_sp').hide();
			$('.m_article_shop').show();
		} else {
			$('.m_article_sp').show();
			$('.m_article_shop').hide();
		}
	})

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

	// 跳转登录
	$.redirectLogin = function(redirectUrl, message) {
		var href = wit.base + "/login.jhtml";
		if (redirectUrl != null) {
			href += "?redirectUrl=" + encodeURIComponent(redirectUrl);
		}
		if (message != null) {
			alert(message);
			setTimeout(function() {
				location.href = href;
			}, 1000);
		} else {
			location.href = href;
		}
	}
	if ($("img").length > 0) {
		$("img").each(function() {
			if (this.attributes.src == null || this.attributes.src.value == "") {
				$(this).addClass("NoPicture");
			}
		});
	}
	//提示窗口
	if($('.m_wtips').length > 0){
		function m_wtipsfun(){
			$('.m_wtipsbg').show().addClass('ddin');
			$('.m_wtips').show().addClass('ddin');
			setTimeout(function(){
				$('.m_wtips').hide();
				$('.m_wtipsbg').hide();
			},2500);
		}
	}
	// 操作窗口
	if ($('.m_wtips_op').length > 0) {
		function m_wtipsopfun() {
			$('.m_wtipsbg').show().addClass('ddin');
			$('.m_wtips_op').show().addClass('ddin');
		}
	}
	//界面提示
	mtips =(function(){
		var mwtipsData ={};
		if ($.isPlainObject(arguments[0])) {
			mwtipsData = arguments[0];
		} else if (typeof arguments[0] === "string") {
			mwtipsData.type = arguments[0];
		} else {
			return false;
		}
		tipsw ='<div class="m_wtips">' + mwtipsData.type + '</div><div class=\"m_wtipsbg\"></div>';
		if($('.m_wtips').length > 0){
			$('.m_wtips').remove();
			$('.m_wtipsbg').remove();
		}$('body').append(tipsw);
		m_wtipsfun();
	});
	// 界面提示
	mtipsop = (function() {
		var mwtipsDatah1 = {};
		var mwtipsDatap = {};
		if ($.isPlainObject(arguments[0])) {
			mwtipsDatah1 = arguments[0];
		} else if (typeof arguments[0] === "string") {
			mwtipsDatah1.type = arguments[0];
		} else {
			return false;
		}
		if ($.isPlainObject(arguments[1])) {
			mwtipsDatap = arguments[1];
		} else if (typeof arguments[1] === "string") {
			mwtipsDatap.type = arguments[1];
		} else {
			return false;
		}
		tipswop = '<div class=\"m_wtips_op\"><h1>' + mwtipsDatah1.type + '</h1><p>' + mwtipsDatap.type
				+ '</p><button class=\"m_confirm\">确定</button><button class=\"m_close\">取消</button></div><div class=\"m_wtipsbg\"></div>';
		if ($('.m_wtips_op').length > 0) {
			$('.m_wtips_op').remove();
			$('.m_wtipsbg').remove();
		}
		$('body').append(tipswop);
		m_wtipsopfun();
		// 窗口取消
		$('.m_close').on('tap', function() {
			$('.m_wtips_op').removeClass('ddin').addClass('ddout');
			$('.m_wtipsbg').removeClass('ddin').addClass('ddout');
			$.later(function() {
				$('.m_wtips_op').hide();
				$('.m_wtipsbg').hide();
			}, 1500, false);
			return false;
		});
	});
// article 订单滚动
	if ($('#odr_scrooler_0').length > 0) {
		var odrscrooler0 = $('#odr_scrooler_0'), odrscrooll0 = new iScroll('odr_scrooler_0', {
			onBeforeScrollStart : function() {
				odrscrooler0.iScroll('refresh');
			}
		});
		var odrscrooler1 = $('#odr_scrooler_1'), odrscrooll1 = new iScroll('odr_scrooler_1', {
			onBeforeScrollStart : function() {
				odrscrooler1.iScroll('refresh');
			}
		});
		var odrscrooler2 = $('#odr_scrooler_2'), odrscrooll2 = new iScroll('odr_scrooler_2', {
			onBeforeScrollStart : function() {
				odrscrooler2.iScroll('refresh');
			}
		});
		var odrscrooler3 = $('#odr_scrooler_3'), odrscrooll3 = new iScroll('odr_scrooler_3', {
			onBeforeScrollStart : function() {
				odrscrooler3.iScroll('refresh');
			}
		});
		var odrscrooler4 = $('#odr_scrooler_4'), odrscrooll4 = new iScroll('odr_scrooler_4', {
			onBeforeScrollStart : function() {
				odrscrooler4.iScroll('refresh');
			}
		});
	}
	// article 订单滚动
	if ($('#m_scrooler_0').length > 0) {
		var odrscrooler0 = $('#m_scrooler_0'), odrscrooll0 = new iScroll('m_scrooler_0', {
			onBeforeScrollStart : function() {
				odrscrooler0.iScroll('refresh');
			}
		});
	}
	// 最新资讯
	if ($('.reference_tab').length > 0) {
		var i = "";
		$('.reference_tab ul li').on('tap', function() {
			i = $(this).index();
			if (!$(this).hasClass('current_screen')) {
				$(".reference_tab ul li").removeClass("current_screen");
				$(this).addClass("current_screen");
				$(".reference_list").hide();
				$(".reference_list").eq(i).show();
			} 
		});
	}
	
	//咨询列表滚动
	if($('.reference_list').length > 0){
            var rscrooler0  = $('#r_scrooler0'),
			rscrool0 = new iScroll('r_scrooler0',{
				onBeforeScrollStart:function(){rscrooler0.iScroll('refresh');}}),
			rscrooler1  = $('#r_scrooler1'),
			rscrool1 = new iScroll('r_scrooler1',{onBeforeScrollStart:function(){rscrooler1.iScroll('refresh');}});
			
	}
	
	//界面提示
	ptips =(function(){
		var pwtipsData ={};
		if ($.isPlainObject(arguments[0])) {
			pwtipsData = arguments[0];
		} else if (typeof arguments[0] === "string") {
			pwtipsData.type = arguments[0];
		} else {
			return false;
		}
		tipsw ='<div class="p_wtips">' + pwtipsData.type + '</div><div class=\"p_wtipsbg\"></div>';
		if($('.p_wtips').length > 0){
			$('.p_wtips').remove();
			$('.p_wtipsbg').remove();
		}$('body').append(tipsw);
		p_wtipsfun();
	});
	
	//统一a标签
	  $("a").live("tap",function(){
	  	if($(this).attr("url")!=null&&$(this).attr("url")!=""){
	  		location.href=$(this).attr("url");
	  		return false;
	  	}
	  });
})
