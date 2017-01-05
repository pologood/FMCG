
$(function(){
	 var ua = navigator.userAgent.toLowerCase();
     var isSafari = ua.indexOf('iphone')>-1 && ua.indexOf('safari')>-1 && ua.indexOf('version') >-1;
	 function hireUrl(){
		 $('.m_section').css("height",window.innerHeight+100 - $('#christmas').height() - $('#spring_festival').height() +'px');  //强制让内容超过
         window.scrollTo(0, 0);
      }
	  $('body,section').on('touchmove',function(event){event.preventDefault();});
});
$(function(){
	$('header').addClass('topina');
	$('article').addClass('bottomin');
	$('footer').addClass('bottomin');
	
	//article 滚动
	if($('#m_scrooler').length > 0){
		var actscrooler  = $('#m_scrooler'),
			actscrooll = new iScroll('m_scrooler',{onBeforeScrollStart:function(){actscrooler.iScroll('refresh');}});
	}
	//article 分类滚动
	if($('#m_clscrooler').length > 0){
		var clscrooler  = $('#m_clscrooler'),
			clscrooll = new iScroll('m_clscrooler',{onBeforeScrollStart:function(){clscrooler.iScroll('refresh');}}),
			crscrooler  = $('#m_crscrooler'),
			crscrooll = new iScroll('m_crscrooler',{onBeforeScrollStart:function(){crscrooler.iScroll('refresh');}});
	}
	
	//区域选择滚动
	if($('.s_area').length > 0){
		var areascrooler1  = $('#s_areascrooler_1'),
			areascrooll1 = new iScroll('s_areascrooler_1',{onBeforeScrollStart:function(){areascrooler1.iScroll('refresh');}}),
			areascrooler2  = $('#s_areascrooler_2'),
			areascrooll2 = new iScroll('s_areascrooler_2',{onBeforeScrollStart:function(){areascrooler2.iScroll('refresh');}});
	}
	//区列表显示
	if($('.s_area').length > 0){
		
		$('.province_list ul li').on('tap', function(){
			$('.province_list ul li').removeClass('current_province');
			$(this).addClass('current_province');
			
			});
		$('.city_list ul li').on('tap', function(){
			
			$('.city_list ul li').children('.classify').animate({display:'none'},2000);
			$(this).children('.classify').animate({display:'block'},2000);
			
			});
		$('.province_list ul li').on('tap', function(){
			
			$('.city_list ul li').children('.classify').animate({display:'none'},2000);
			});
	}
	
	
})
