/* ------------------ 首页标签切换 ---------------*/
$(document).ready(function () {
    /*------------右侧通告栏标签切换-------------*/
    $("div.wallpaper .notice .noticeEvent dl dd").each(function (index) {
        var liNode = $(this);
        $(this).mouseover(function () {
            timeoutid = setTimeout(function () {
                $("div.wallpaper .notice .noticeEvent ul").removeClass("display");
                $("div.wallpaper .notice .noticeEvent dl dd.on").removeClass("on");
                $("div.wallpaper .notice .noticeEvent ul").eq(index).addClass("display");
                liNode.addClass("on");

            }, 300);
        }).mouseout(function () {
            clearTimeout(timeoutid);//鼠标移出 则 定时器 停止
        })
    });
    /*------------热销商品标签切换-------------*/
    $("div.hotProducts .hotProductsNav dl dd").each(function (index) {
        var liNode = $(this);
        $(this).mouseover(function () {
            timeoutid = setTimeout(function () {
                $("div.hotProducts .hotProductsShow ul").removeClass("display");
                $("div.hotProducts .hotProductsNav dl dd.on").removeClass("on");
                $("div.hotProducts .hotProductsShow ul").eq(index).addClass("display");
                liNode.addClass("on");

            }, 300);
        }).mouseout(function () {
            clearTimeout(timeoutid);//鼠标移出 则 定时器 停止
        })
    });
    /*------------限时抢购标签切换-------------*/
    $("div.flashSale .flashSaleNav dl dd").each(function (index) {
        var liNode = $(this);
        $(this).mouseover(function () {
            timeoutid = setTimeout(function () {
                $("div.flashSale .flashSaleShow ul").removeClass("display");
                $("div.flashSale .flashSaleNav dl dd.on").removeClass("on");
                $("div.flashSale .flashSaleShow ul").eq(index).addClass("display");
                liNode.addClass("on");

            }, 300);
        }).mouseout(function () {
            clearTimeout(timeoutid);//鼠标移出 则 定时器 停止
        })
    });

});
/*-------------------------回到顶部 以及 左右悬浮栏--------------------------*/
$(document).ready(function () {
    $(window).scroll(function () {
        //当距离页面顶部 大于等于280像素时 楼层导航以及右侧 回到顶部显示
        if ($(window).scrollTop() >= 280) {
            $('.actGotop').fadeIn(300);
            $(".actGotop").css("display", "block");
            $(".elevator").css("display", "block");
            $(".toolbar").css("display", "none");
            $(".toolbarNone").css("display", "block");
        } else {
            $('.actGotop').fadeOut(300);
            $(".actGotop").css("display", "none");
            $(".elevator").css("display", "none");
            $(".toolbar").css("display", "block");
            $(".toolbarNone").css("display", "none");
        }

    });
    //回到顶部
    $('.actGotop').click(function () {
        $('html,body').animate({scrollTop: '0px'}, 800);
    });

});

/*-----------------分类子级渐隐渐现--------------------------*/
//$(document).ready(function(){
//	var time = null;
//	var list = $(".menuNav ul");//.menuNav ul
//	var box = $(".menuNavList");//.menuNavList
//	var lista = list.find("li");//
//
//	for(var i=0,j=lista.length;i<j;i++){
//		if(lista[i].className == "now"){//hover
//			var olda = i;
//		}
//	}
//
//	/*var box_show = function(hei){
//		box.stop().animate({
//			height:hei,
//			opacity:1
//		},400);
//	}
//
//	var box_hide = function(){
//		box.stop().animate({
//			height:0,
//			opacity:0
//		},400);
//	}*/
//
//	lista.hover(function(){
//		lista.removeClass("now");//hover
//		$(this).addClass("now");//hover
//		clearTimeout(time);
//		var index = list.find("li").index($(this));//li
//		box.find(".menuNavListCont").hide().eq(index).show();// .menuNavListCont
//		box.show();
//		/*var _height = box.find(".menuNavListCont").eq(index).height()+0;
//		box_show(_height)*/
//	},function(){
//		time = setTimeout(function(){
//			box.find(".menuNavListCont").hide();
//			box.hide();
//		},50);
//		lista.removeClass("now");//hover
//		lista.eq(olda).addClass("now");
//	});
//
//	box.find(".menuNavListCont").hover(function(){
//		var _index = box.find(".menuNavListCont").index($(this));
//		lista.removeClass("now");
//		lista.eq(_index).addClass("now");
//		clearTimeout(time);
//		$(this).show();
//		/*var _height = $(this).height()+0;
//		box_show(_height);*/
//	},function(){
//		time = setTimeout(function(){
//			$(this).hide();
//			box.hide();
//		},50);
//		lista.removeClass("now");
//		lista.eq(olda).addClass("now");
//	});
//
//});

function search(b) {

    //var $listForm = $("#listForm");
    //var $keyword = $("#_keyword");
    var _keyword = $('#keyword').val();

    if (_keyword == "" || _keyword == null) {
        alert("请输入您要搜索的货品关键字");
        return;
    }
    //$keyword.val(_keyword);
    //$listForm.submit();
    location.href = b + "/b2c/product/list/0.jhtml?keyword=" + _keyword;
}

/*------------------------------------------------*/
