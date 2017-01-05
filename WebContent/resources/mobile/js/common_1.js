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
$(function(){
	//首页搜索 效果

	if($('.p_search').length > 0){
		$('.p_search').on('tap',function(){
			$('.p_search').addClass('ddout').removeClass('ddin').hide();
			$('.p_searchfixed').addClass('ddin').removeClass('ddout').show();
			$('.p_windowbg_1').addClass('ddin').removeClass('ddout').show();
			$('section').addClass('scalein').removeClass('scaleout');
		});
		$('.p_windowbg_1').on('tap',function(){
			$('.p_searchfixed').removeClass('ddin').addClass('ddout');
			$.later(function(){
				$('.p_windowbg_1').removeClass('ddin').addClass('ddout');
				$('.p_search').removeClass('ddout').addClass('ddin').show();
				$('section').removeClass('scalein').addClass('scaleout');
			}, 500, false);
			$.later(function(){
				$('.p_searchfixed').hide();
				$('.p_windowbg_1').hide();
			}, 800, false);
		});
	}
	if($('.p_aside').length > 0){
		//分类滚动
		var navscrooler  = $('#p_navscrooler'),
			navscrooll = new iScroll('p_navscrooler',{onBeforeScrollStart:function(){navscrooler.iScroll('refresh');}}),
			classf2scrooler  = $('#p_classf2scrooler'),
			classf2crooll = new iScroll('p_classf2scrooler',{onBeforeScrollStart:function(){classf2scrooler.iScroll('refresh');}}),
			classf3scrooler  = $('#p_classf3scrooler'),
			classf3crooll = new iScroll('p_classf3scrooler',{onBeforeScrollStart:function(){classf3scrooler.iScroll('refresh');}});

		$('.p_tap1li ul li .p_listin').on('tap',function(){
			$('.p_tap2li ul li').removeClass('p_navhove');
			$('.p_tap2li ul li').find('.p_tag').removeClass('p_deg180');
			$('.p_tap1li ul li').find('.p_tag').removeClass('p_deg180');
			if($(this).parent().parent().hasClass('p_navhove')){
				$(this).parent().parent().removeClass('p_navhove');
				if($('.p_classf3').hasClass('leftind')){
					$('.p_classf3').animate({left:0 + "px"},1000).removeClass('leftind').addClass('leftoutd');
					//$('.p_classf3').removeClass('p_block').addClass('p_none');
				}
				$('.p_classf2').animate({left:0 + "px"},1000).removeClass('leftind').addClass('leftoutd');
				//$('.p_classf2').removeClass('p_block').addClass('p_none')
				$(this).parent().find('.p_tag').removeClass('p_deg180');
			}else{
				$('.p_tap1li li').removeClass('p_navhove');
				$(this).parent().parent().addClass('p_navhove');
				$('.p_classf2').addClass('leftind').addClass('p_block').animate({left:250 + "px"},1000).removeClass('leftoutd');
				if($('.p_classf3').hasClass('leftind')){
					$('.p_classf3').animate({left:0 + "px"},1000).addClass('leftoutd').removeClass('leftind');
				}
				$(this).parent().find('.p_tag').addClass('p_deg180');
			}
	    });
	  //   $('.p_tap2li ul li .p_listin').on('tap',function(){
	  //   	alert(1);
	  //   	$('.p_tap2li ul li').find('.p_tag').removeClass('p_deg180');
			// if($(this).parent().parent().hasClass('p_navhove')){
			// 	$('.p_tap2li ul li').removeClass('p_navhove');
			// 	$('.p_classf3').animate({left:0 + "px"},1000).addClass('leftoutd').removeClass('leftind');
			// 	//$('.p_classf3').removeClass('p_block').addClass('p_none');
			// 	$(this).parent().find('.p_tag').removeClass('p_deg180');
			// }else{
			// 	$('.p_tap2li ul li').removeClass('p_navhove');
			// 	$(this).parent().parent().addClass('p_navhove');
			// 	$('.p_classf3').animate({left:500 + "px"},1000).addClass('leftind').addClass('p_block').removeClass('leftoutd');
			// 	$(this).parent().find('.p_tag').addClass('p_deg180');
			// }
	  //   });
	}
	$('article').on('touchmove',function(){
		dongt();
		classificationout();
		$('.p_listcf').removeClass('p_90deg');
		if($('.p_ssclist_1').length > 0){
			$('.p_ssclist_1').show();
		}
	});
	$('article').on('tap',function(){
		dongt();
		classificationout();
		$('.p_listcf').removeClass('p_90deg');
		if($('.p_ssclist_1').length > 0){
			$('.p_ssclist_1').show();
		}
	});
		function dongt(){
			$('.p_tap1li ul li').removeClass('p_navhove');
			if($('.p_classf3').hasClass('leftind')){
				$('.p_classf3').addClass('leftoutd').removeClass('leftind');
			}
			$.later(function(){
				$('.p_classf2').addClass('leftoutd').removeClass('leftind');
			}, 300, false);
		}
    
	//article 滚动
	if($('#p_contscrooler').length > 0){
	var contscrooler = $('#p_contscrooler'),
		contscrooll = new iScroll('p_contscrooler',{onBeforeScrollStart:function(){contscrooler.iScroll('refresh');}});
	}
	if($('.p_unit').length > 0){
	//列表点击单位切换
		$('.p_unit').addClass('p_dw');
		$('.p_unit').on('tap',function(){
			if($(this).hasClass('p_dw')){
				$(this).removeClass('p_dw');
			}else{
				$(this).addClass('p_dw');
			}
		});
	}
	//列表
	if($('.p_articlelist').length > 0){
		//列表排序 滚动
		// var p_sortcscrooller = $('#p_sortcscrooll'),
		// 	p_sortcscrooll = new iScroll('p_sortcscrooll',{onBeforeScrollStart:function(){p_sortcscrooller.iScroll('refresh');}});
		
		//列表排序
		$('.p_listsort').on('tap',function(){
			if($('.p_ss').hasClass('p_ssup')){
				$('.p_ssc').hide();
				$('.p_ss').removeClass('p_ssup');
			}
			$('.p_listsort').toggleClass('p_ssup');

//			$('.p_sales .p_uptag').removeClass('p_uptag_1');
//			$('.p_sales .p_downtag').removeClass('p_downtag_1');
//			
//			$('.p_price .p_uptag').removeClass('p_uptag_1');
//			$('.p_price .p_downtag').removeClass('p_downtag_1');
			classificationout()
			$('.p_classification').removeClass('p_90deg');
//			$('.p_interval').removeClass('p_intervalcipb');
			if($('.p_ssclist').length > 0){
				$('.p_ssclist').show();
			}
		});

		//列表筛选
		$('.p_ss').on('tap',function(){
			if($('.p_listsort').hasClass('p_aftern')){
				$('.p_listsortc').hide();
				$('.p_listsort').removeClass('p_aftern');
			}
			$('.p_ss').toggleClass('p_ssup');
			$('.p_ssc').toggle();

//			$('.p_sales .p_uptag').removeClass('p_uptag_1');
//			$('.p_sales .p_downtag').removeClass('p_downtag_1');
//
//			$('.p_price .p_uptag').removeClass('p_uptag_1');
//			$('.p_price .p_downtag').removeClass('p_downtag_1');
			classificationout()
			$('.p_classification').removeClass('p_90deg');
//			$('.p_interval').removeClass('p_intervalcipb');
			if($('.p_ssclist').length > 0){
				$('.p_ssclist').show();
			}
		});
		//列表销量
		$('.p_sales').on('tap',function(){
			if($('.p_sales .p_uptag').hasClass('p_uptag_1')){
				$('.p_sales .p_uptag').removeClass('p_uptag_1');
				$('.p_sales .p_downtag').removeClass('p_downtag_1');
			}else{ 
				$('.p_sales .p_uptag').addClass('p_uptag_1');
				$('.p_sales .p_downtag').addClass('p_downtag_1');
			}
			$('.p_price .p_uptag').removeClass('p_uptag_1');
			$('.p_price .p_downtag').removeClass('p_downtag_1');
			$('.p_ssc').hide();
			$('.p_ss').removeClass('p_ssup');
			$('.p_listsortc').hide();
			$('.p_listsort').removeClass('p_ssup');
			classificationout()
			$('.p_classification').removeClass('p_90deg');
			$('.p_interval').removeClass('p_intervalcipb');
			if($('.p_ssclist').length > 0){
				$('.p_ssclist').show();
			}
		});
		//列表价格
		$('.p_price').on('tap',function(){
			if($('.p_price .p_uptag').hasClass('p_uptag_1')){
				$('.p_price .p_uptag').removeClass('p_uptag_1');
				$('.p_price .p_downtag').addClass('p_downtag_1');
			}else if($('.p_price .p_downtag').hasClass('p_downtag_1')){
				$('.p_price .p_uptag').addClass('p_uptag_1');
				$('.p_price .p_downtag').removeClass('p_downtag_1');
			}else{
				$('.p_price .p_uptag').addClass('p_uptag_1');
			}
			$('.p_sales .p_uptag').removeClass('p_uptag_1');
			$('.p_sales .p_downtag').removeClass('p_downtag_1');
			$('.p_ssc').hide();
			$('.p_ss').removeClass('p_ssup');
			$('.p_listsortc').hide();
			$('.p_listsort').removeClass('p_ssup');
			classificationout()
			$('.p_classification').removeClass('p_90deg');
			$('.p_interval').removeClass('p_intervalcipb');
			if($('.p_ssclist').length > 0){
				$('.p_ssclist').show();
			}
		});

		


		// var recommendlist = $('.p_listbody ul').children('li').length;
		// $('.p_listbody').css('width',recommendlist * 66.8 + 'px');
		
		// var plist_scroller = new iScroll('plist_scroller', {
	 //                snap: true,
	 //                momentum: true,
	 //    });
		if($('.p_sscscrooll').length > 0){
		    var p_sscscrooller = new iScroll('p_sscscrooll', {
		                snap: true,
		                momentum: true,
		    });
		}
	    //筛选滚动
	    if($('.p_sscscrooll').length > 0){
		    var p_sscscrooller = $('#p_sscscrooll'),
				p_sscscrooll = new iScroll('p_sscscrooll',{onBeforeScrollStart:function(){p_sscscrooller.iScroll('refresh');}});
		}
	}
	if($('.p_listsort').length > 0){
	//列表排序 滚动
		var p_sortcscrooller = $('#p_sortcscrooll'),
			p_sortcscrooll = new iScroll('p_sortcscrooll',{onBeforeScrollStart:function(){p_sortcscrooller.iScroll('refresh');}});
		
		//列表排序
		$('.p_listsort').on('tap',function(){
			if($('.p_ss').hasClass('p_ssup')){
				$('.p_ssc').hide();
				$('.p_ss').removeClass('p_ssup').removeClass('p_aftern');
			}
			$('.p_listsort').toggleClass('p_ssup').toggleClass('p_aftern');
			$('.p_listsortc').toggle();

//			$('.p_sales .p_uptag').removeClass('p_uptag_1');
//			$('.p_sales .p_downtag').removeClass('p_downtag_1');
//			
//			$('.p_price .p_uptag').removeClass('p_uptag_1');
//			$('.p_price .p_downtag').removeClass('p_downtag_1');
			classificationout()
			$('.p_classification').removeClass('p_90deg');
//			$('.p_interval').removeClass('p_intervalcipb');
			if($('.p_ssclist').length > 0){
				$('.p_ssclist').show();
			}
		});
	}	
	//列表分类
	var classif_h =window.innerHeight - 60 ;
	var classifl_h =window.innerHeight - 109 ;
	$('.p_listaside').css("height",classif_h +"px");
	$('.p_listclassf2').css("height",classifl_h +"px");
	$('.p_listclassf3').css("height",classifl_h +"px");
	$('.p_detaill').css("height",classifl_h +"px");
	$('.p_detailr').css("height",classifl_h +"px");

	$('.p_classification').on('tap',function(e){
		if($('.p_listaside').hasClass('leftind')){
			classificationout()
			$('.p_classification').removeClass('p_90deg');
			$('.p_ssclist').show();
		}else{
			$('.p_classification').addClass('p_90deg');
			$('.p_listaside').show().addClass('leftind').animate({left:0 + "px"},1000);
			$('.p_ssc').hide();
			$('.p_ss').removeClass('p_ssup');
			$('.p_listsortc').hide();
			$('.p_listsort').removeClass('p_ssup');
			$('.p_interval').removeClass('p_intervalcipb');
			$('.p_ssclist').hide();
		}
		
	});
	function classificationout(){
		if($('.p_classf3').hasClass('leftind')){
			$('.p_classf3').animate({left:0 + "px"},1000).removeClass('leftind').addClass('leftoutd');
		}
		if($('.p_classf2').hasClass('leftind')){
			$('.p_classf2').animate({left:0 + "px"},500).removeClass('leftind').addClass('leftoutd');
		}
		$.later(function(){
			$('.p_listaside').removeClass('leftind').animate({left:-260 + "px"});
			$('.p_tap1li li').removeClass('p_navhove');
			$('.p_tap2li li').removeClass('p_navhove');
			$('.p_tap2li ul li').find('.p_tag').removeClass('p_deg180');
			$('.p_tap1li ul li').find('.p_tag').removeClass('p_deg180');
		}, 500, false);
	}


	//图片滚动
	var slide = new TouchSlider({
        id:'am-slider',
        auto:false,
        speed:300, 
        timeout:2000
        });
        var slideFlag = $('.p_round').find('span');
        var slide = new TouchSlider({id:'am-slider',before:function(index){
                slideFlag.removeClass('themeStyle');
                slideFlag.eq(index).addClass('themeStyle');
    	}});
        var p_round = $('.p_round').children('span').length;
		$('.p_round').css('margin-left',- p_round * 11 + 'px');

	//分享收藏效果
	if($('.p_share').length > 0){
		$('.p_share').on('tap',function(e){
			$('.p_share').addClass('p_turn');
			$.later(function(){
				$('.p_share').toggleClass('p_red');
				$('.p_share').removeClass('p_turn');
			}, 800, false);
		});
		// $('.p_collect').on('tap',function(e){
		// 	$('.p_collect').addClass('p_turn');
		// 	$.later(function(){
		// 		$('.p_collect').removeClass('p_turn');
		// 		$('.p_collect').toggleClass('p_red');
		// 	}, 800, false);
		// });
	}
	if($('.p_articlecart').length > 0){
			$('.p_carttabletop').addClass('bottomin');
			$('.p_articlecart').addClass('bottomin');
	}
	if($('.p_articlelist').length > 0){
		$('.p_articlelist').addClass('bottomin');
	}
	//充值
//	if($('.p_recharge').length > 0){
//		$('.p_recharge').on('tap',function(){
//			$('.p_rechargead').show().addClass('rightin').removeClass('leftout').removeClass('rightout');
//			$('.p_receiptbg').show().addClass('ddin').removeClass('ddout');	
//		});
//		$('.p_returndd1').on('tap',function(){
//			$('.p_rechargead').removeClass('rightin').addClass('rightout');
//			$('.p_receiptbg').addClass('ddin').removeClass('ddout');
//			$.later(function(){
//				$('.p_receiptbg').hide();
//			}, 800, false);		
//		});
//	}

	 //加入购物车
	 if($('.p_recommendbtn').length > 0){
	 	
	 	$('.p_recommendbtn').on('tap',function(){
			$('.p_cartcount').show();
	 		//$(this).parent().find('.p_cart_img').show().addClass('p_addcart1');
			$('.p_cartcount').addClass('p_scale_big').removeClass('p_scale_small');
			$.later(function(){
				$('.p_cartcount').removeClass('p_scale_big').addClass('p_scale_small');
			}, 500, false);	
			return false;
	      });
	 	$('.p_recommendbtn').live('tap',function(){
	 		
	 		//$(this).parent().find('.p_cart_img').show().addClass('p_addcart1');
	 		$('.p_cartcount').addClass('p_scale_big').removeClass('p_scale_small');
	 		$.later(function(){
	 			$('.p_cartcount').removeClass('p_scale_big').addClass('p_scale_small');
	 		}, 500, false);	
	 		return false;
	 	});
	 }
	 //列表搜索p_receiptsech
	 if($('.p_receiptsech').length > 0){
	 	$('.p_receiptsech').on('tap',function(){
			$('.p_receiptsechbtn').addClass('ddin').removeClass('ddout').show();
	     });
	 	$('.p_receiptsechbtn').on('tap',function(){
			$('.p_receiptsechbtn').addClass('ddout').removeClass('ddin');
			$.later(function(){
				$('.p_receiptsechbtn').hide();
			}, 500, false);	
	     });
	 }
	 if($('.p_interval').length > 0){
	 //列表价格区间
		$('.title').on('tap',function(){
			if($('.p_interval').hasClass('p_intervalcipb')){

			}else{
//				$('.p_sales .p_uptag').removeClass('p_uptag_1');
//				$('.p_sales .p_downtag').removeClass('p_downtag_1');
//				$('.p_price .p_uptag').removeClass('p_uptag_1');
//				$('.p_price .p_downtag').removeClass('p_downtag_1');
				$('.p_ssc').hide();
				$('.p_ss').removeClass('p_ssup');
				$('.p_listsortc').hide();
				$('.p_listsort').removeClass('p_ssup');
				classificationout()
				$('.p_classification').removeClass('p_90deg');
				$('.p_interval').addClass('p_intervalcipb');
				if($('.p_ssclist').length > 0){
					$('.p_ssclist').show();
				}
			}
		});
		$('.p_intervalcbtn').on('tap',function(e){
			$('.p_interval').removeClass('p_intervalcipb');
		});
	 }
	 if($('#p_receiptcscroller').length > 0){
	//根据字母定位
		  var receiptrooller = $('#p_receiptcscroller'),
			  receiptrooll = new iScroll('p_receiptcscroller',{onBeforeScrollStart:function(){receiptrooller.iScroll('refresh');}});
			  
	      $('#p_letter_list a').on('tap',function(){
	      	if($('.bodycont_list').children('li').length >10){
		      	receiptrooller.iScroll('refresh');
				receiptrooll.scrollToElement('#'+$(this).text(),600);
	      	}
	      });
	  }

  //获取验证码
   if($('.p_captcha').length > 0){
		$('.p_captcha').on('tap',function(){
			$(this).removeClass('p_down');
		});
	}
	//会员中心 近期浏览
	// if($('.p_recentlist').length > 0){
	// 	var recentlistw = $('.p_recentlist ul').children('li').length;
	// 	$('.p_recentlist ul').css('width',recentlistw * 190 + 'px');
	// 	var resslistcroll = new iScroll('p_resslistcroller', {
	//                 snap: true,
	//                 momentum: true,
	//     });
	// }
	//提示窗口
	if($('.p_wtips').length > 0){
		function p_wtipsfun(){
			$('.p_wtipsbg').show().addClass('ddin');
			$('.p_wtips').show().addClass('ddin');
			$.later(function(){
				$('.p_wtips').hide();
				$('.p_wtipsbg').hide();
			}, 2500, false);	
		}
	}
	//列表置顶
	if($('.p_totop').length > 0){
		$('.bodycont_list').unbind();
		$('.bodycont_list').bind('swipeDown', function(){
			$('.p_totop').addClass('ddout').removeClass('ddin');
			$.later(function(){
				$('.p_totop').hide().removeClass('ddout');
			}, 1200, false);
		});
		$('.bodycont_list').bind('swipeUp', function(){
			var p_listbodyli = $('.p_listbody ul').children('li').length;
			if(p_listbodyli > 16){
				$('.p_totop').show().addClass('ddin');	
			}
		});
		$('.p_totop').on('tap',function(){
			$('#wrapper').iScroll('refresh');
			myScroll.scrollToElement('.p_indexrecommendtag',600);
			$('.p_totop').addClass('ddout').removeClass('ddin');
			$.later(function(){
				$('.p_totop').hide().removeClass('ddout');
			}, 1200, false);	
		});
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

	//列表字母
		if($('.p_ssclist').length > 0){
			$('.p_ssclist span').on('tap',function(){
				if($('.p_ssclist span.p_reda').length<3){
					if($(this).hasClass('p_reda')){
						$(this).removeClass('p_reda');
					}else{
						$(this).addClass('p_reda');
					}
				}else if($(this).hasClass('p_reda')){
					$(this).removeClass('p_reda');
				}
				
			});
		}
	//列表字母滚动
	if($('#p_sslcroller').length > 0){
		var sslcroller = new iScroll('p_sslcroller', {
		                snap: true,
		                momentum: true,
	    });
	    $('.p_sscllastz').on('tap',function(){
			$('.p_ssclist_2').show();
			$('.p_ssclist_1').hide();
			$('#p_sslcroller1').iScroll('refresh');
		});
	}
	//列表品牌滚动
	if($('#p_sslcroller1').length > 0){
		var sslcw = $('#p_sslcroller1 div').children('span').length;
		$('#p_sslcroller1 div').css('width',sslcw * 90 + 'px');
		var sslcroller1 = new iScroll('p_sslcroller1', {
		                snap: true,
		                momentum: true,
	    });
	    $('.p_sscllastp').on('tap',function(){
			$('.p_ssclist_1').show();
			$('.p_ssclist_2').hide();
			$('#p_sslcroller').iScroll('refresh');
		});
	}

})
