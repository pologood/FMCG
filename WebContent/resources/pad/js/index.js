$(function(){

//首页商铺弹窗
	if($('.p_uwindow').length > 0){
		$('.p_uwindow').addClass('bottomind');
		$('.p_uwindowclose').on('tap',function(){
			$('.p_uwindow').addClass('bottomoutd').removeClass('bottomind');
			$.later(function(){
				$('.p_uwindow').removeClass('bottomoutd').hide();
			}, 500, false);
		});
	}
//推荐滚动
	if($('.p_indexrecommend').length > 0){
		var recommendlist = $('#pr_scroller ul').children('li').length;
		$('#pr_scroller ul').css('width',recommendlist * 240 + 'px');
		
		var pr_scroller = new iScroll('pr_scroller', {
	                snap: true,
	                momentum: true,
	         });
		var recommendlist1 = $('#pr_scroller_1 ul').children('li').length;
		$('#pr_scroller_1 .p_recommend_listcn').css('width',recommendlist1 * 120 + 'px');
		var pr_scroller1 = new iScroll('pr_scroller_1', {
	                snap: true,
	                momentum: true,
	         });
		var bodyw = window.innerWidth ;
		if(bodyw > 800){
			$('#pr_scroller').show();
			$('#pr_scroller_1').hide();
		}else{
			$('#pr_scroller_1').show();
			$('#pr_scroller').hide();
		}
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
		

})