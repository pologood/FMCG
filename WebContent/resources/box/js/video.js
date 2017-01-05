
$(function(){
     var len  = $(".small_video ul li").length;
	 var index = 0;
	 var adTimer;
	 $(".small_video ul li").click(function(){
		index  =   $(".small_video ul li").index(this);
		showImg(index);
	 }).eq(0).mouseover();	
	 
	
	$(".small_video a").click(function(){
	$(".small_video a").removeClass("sv_on");
	$(this).addClass("sv_on");
	});
})

// 通过控制top ，来显示不同的幻灯片
function showImg(index){
        var adWidth = $(".video").width();
		$(".video ul").stop(true,false).animate({left : -adWidth*index},400);
		$(".small_video a").removeClass("sv_on")
		.eq(index).addClass("sv_on");
}

$(function(){
	var page = 1;
	var i = 5; //每版放5个图片
	var $v_show = $(".small_video ul");
	var $mask=$(".small_video");
	var v_height = $mask.height() ;
	var len = $v_show.find("li").length;
	var page_count = Math.ceil(len / i) ; 
	 //向右 按钮
    $(".scroll_next").click(function(){ 
								
		if( !$v_show.is(":animated") ){
			if( page == page_count ){  //已经到最后一个版面了,如果再向后，必须跳转到第一个版面。
				$v_show.animate({ top : 0}, 800); //通过改变left值，跳转到第一个版面
				page = 1;
			}else{
				$v_show.animate({ top : '-='+v_height}, 800);  //通过改变left值，达到每次换一个版面
				page++;
			}
		}
   });
    //往左 按钮
    $(".scroll_prev").click(function(){
	    if( !$v_show.is(":animated") ){
			if( page == 1 ){  //已经到第一个版面了,如果再向前，必须跳转到最后一个版面。
				$v_show.animate({ top : '-='+v_height*(page_count-1)}, 800); //通过改变left值，跳转到最后一个版面
				page = page_count;
			}else{
				$v_show.animate({ top : '+='+v_height }, 800);  //通过改变left值，达到每次换一个版面
				page--;
			}
		}
    });
});


		   

