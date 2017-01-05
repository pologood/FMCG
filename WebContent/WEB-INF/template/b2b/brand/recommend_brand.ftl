<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-品牌推荐</title>
    <meta name="keywords" content="${setting.siteName}-品牌推荐"/>
    <meta name="description" content="${setting.siteName}-品牌推荐"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/data/data.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/brands.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        /*.save_proudct{
            background:#eb3341;
        }*/
    </style>
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2b/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--横幅开始-->
[#include "/b2b/include/topbanner.ftl"]
    <!--横幅结束-->
    <!--logo 搜索开始-->
[#include "/b2b/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--主导航开始-->
[#include "/b2b/include/topmainnav.ftl"]
    <!--主导航结束-->
</div>
<!--头部end-->
<script type="text/javascript">
    /*所有商品分类*/
    $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'none');
    $('.allCategorys').hover(function () {
        $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'block');
    }, function () {
        $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'none');
    });

    $('.menuNav'+[#if versionType==0]'Tiaohuo'+[/#if]' ul>li').hover(function () {
        var eq = $('.menuNav'+[#if versionType==0]'Tiaohuo'+[/#if]' ul>li').index(this),//获取当前滑过是第几个元素
                h = $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).offset().top,//获取当前下拉菜单距离窗口多少像素
                s = $(window).scrollTop(),//获取游览器滚动了多少高度
                i = $(this).offset().top,//当前元素滑过距离窗口多少像素
                item = $(this).children('.menuNavList').height(),//下拉菜单子类内容容器的高度
                sort = $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).height();//父类分类列表容器的高度

        if (item < sort) {//如果子类的高度小于父类的高度
            if (eq == 0) {
                $(this).children('.menuNavList').css('top', (i - h));
            } else {
                $(this).children('.menuNavList').css('top', (i - h) + 1);
            }
        } else {
            if (s > h) {//判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
                if (i - s > 0) {//则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                    $(this).children('.menuNavList').css('top', (s - h) + 0);
                } else {
                    $(this).children('.menuNavList').css('top', (s - h) - (-(i - s)) + 0);
                }
            } else {
                $(this).children('.menuNavList').css('top', 0);
            }
        }

        $(this).addClass('now');
        $(this).children('.menuNavList').css('display', 'block');
    }, function () {
        $(this).removeClass('now');
        $(this).children('.menuNavList').css('display', 'none');
    });

    //    $('.item > .item-list > .close').click(function(){
    //        $(this).parent().parent().removeClass('hover');
    //        $(this).parent().hide();
    //    });
    //收藏商品/取消收藏
    function addFavorite(id, obj) {
        if ("${member}" == "") {
            location.href = "${base}/b2b/login.jhtml";
        } else {
            if ($(obj).attr("class") == "save_product") {
                $.ajax({
                    url: "${base}/b2b/member/favorite/delete.jhtml",
                    type: "POST",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            $(obj).removeClass("save_product");
                            document.location.reload();
                        }
                        alert(message.content);
                    }
                });
            } else {
                console.log(2);
                $.ajax({
                    url: "${base}/b2b/member/favorite/add.jhtml",
                    type: "POST",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            $(obj).addClass("save_product");
                            location.reload();
                        }
                        alert(message.content);
                    }
                });
            }
        }
    }
</script>

<script type="text/javascript">
    //初始化
    var size=$(".slider-items li").size();
    for(var i=1; i<=size; i++){
        var span="<span>"+i+"</span>";
        $(".trigger-inner").append(span);
    }
    $(function(){
        //手动控制轮播
        $(".slider-items li").eq(0).show();
        $(".trigger-inner span").eq(0).addClass("ui-switchable-selected");
        $(".trigger-inner span").mouseover(function(){
            $(this).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
            var index=$(this).index();
            i=index;
            //alert(index)
            $(".slider-items li").eq(index).stop().fadeIn(300).siblings().stop().fadeOut(300);
        })

        //自动轮播
        var i=0;
        var t=setInterval(move,3000);
        //核心向左运动函数
        function moveL(){
            i--;
            if(i==-1){
                i=size-1;
            }
            //alert(i);
            $(".trigger-inner span").eq(i).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
            $(".slider-items li").eq(i).fadeIn(300).siblings().fadeOut(300);
        }
        //核心向右运动函数
        function move(){
            i++;
            if(i==size){
                i=0;
            }
            //alert(i);
            $(".trigger-inner span").eq(i).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
            $(".slider-items li").eq(i).fadeIn(300).siblings().fadeOut(300);
        }
        //左边按钮点击事件
        $(".slider-page .page-prev").click(function(){
            //alert(1234);
            moveL();
        })
        //右边按钮点击事件
        $(".slider-page .page-next").click(function(){
            //alert(5234);
            move();
        })

        //定时器的开始与结束
        $(".slider-inner").hover(function(){
            clearInterval(t)
        },function(){
            t=setInterval(move,2000);
        })
        console.log(${category.products_size});
    })
</script>

<!--主页内容区start-->
<div class="paper">
    <!--轮播广告start-->
    <div class="brands-slider">
        <div class="container overflow-v">
            <div class="slider-inner">
                <div class="slider-main">
                    <ul class="slider-items">
                    [@ad_position id=112 areaId=area.id count=5/]
                    </ul>
                </div>
                <div class="slider-extra">
                    <div class="slider-trigger">
                        <div class="trigger-inner">
                            <!-- span class="ui-switchable-num"></span>
                            <span class="ui-switchable-num ui-switchable-selected"></span>
                            <span class="ui-switchable-num "></span -->
                        </div>
                    </div>
                    <div class="slider-page">
                        <a class="page-prev" href="javascript:;">&lt;</a>
                        <a class="page-next" href="javascript:;">&gt;</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--轮播广告end-->
    <!--周推荐start-->
    [#if versionType==0]
    <div class="week-recommend pc-bg">
        <div class="container">
            <div class="recommend-nav">
                <h2>本周推荐/</h2>
                <span>RECOMMENDED&nbsp;THIS&nbsp;WEEK</span>
            </div>
            <div class="recommend-content">
                <ul>
                [#list deliverys as delivery]
                    <li>
                        <a href="javascript:;" target="_blank">
                            <div class="item-img">
                                <img src="${delivery.tenant.thumbnail}" style="width:232px;height:232px;">
                            </div>
                            <div class="item-info">
                                <h2>${delivery.name}</h2>
                                <div class="wire"></div>
                                <span class="font">${delivery.address}店</span>
                                <span>${delivery.memo}</span>
                            </div>
                        </a>
                    </li>
                [/#list]
                </ul>
            </div>
        </div>
    </div>
    [/#if]
    <!--周推荐end-->
    <!--人气品牌start-->
    <div class="hot-brands pc-bg">
        [#--<div class="container">--]
            [#--<div class="hot-nav">--]
                [#--<div class="left"></div>--]
                [#--<h2>人气品牌<span>HOT&nbsp;BRAND</span></h2>--]
                [#--<div class="right"></div>--]
            [#--</div>--]
            [#--<div class="brand-items-wrap">--]
                [#--<div class="hot-left-nav">--]
                    [#--<ul>--]
                        [#--<li class="on" onclick="getBrand(this,10)">热门</li>--]
                    [#--[#list category as category]--]
                        [#--<li onclick="getBrand(this,${category_index+11})">${category.name}</li>--]
                    [#--[/#list]--]
                    [#--</ul>--]
                [#--</div>--]
                [#--<script type="text/javascript">--]
                    [#--function getBrand(obj, obj_content) {--]
                        [#--$(obj).addClass("on").siblings().removeClass("on");--]
                        [#--$("#" + obj_content).show().siblings().hide();--]
                    [#--}--]
                [#--</script>--]
                [#--<div class="brand-items">--]
                [#--<ul id="10" >--]
                    [#--[#list brand as brands]--]
                        [#--[#if brands.logo??]--]
                        [#--<li class="brand-item">--]
                            [#--<a href="${base}/b2c/product/list/0.jhtml?brandId=${brands.id}">--]
                                [#--<img src="${brands.logo}">--]
                            [#--</a>--]
                        [#--</li>--]
                        [#--[/#if]--]
                    [#--[/#list]--]
                [#--</ul>--]
                [#--[#list category as category]--]
                    [#--<ul id="${category_index+11}" style="display:none;">--]
                        [#--[#list category.brands as brands]--]
                            [#--[#if brands.logo??]--]
                            [#--<li class="brand-item">--]
                                [#--<a href="${base}/b2c/product/list/0.jhtml?brandId=${brands.id}">--]
                                    [#--<img src="${brands.logo}">--]
                                [#--</a>--]
                            [#--</li>--]
                            [#--[/#if]--]
                        [#--[/#list]--]
                    [#--</ul>--]
                [#--[/#list]--]
                [#--</div>--]
            [#--</div>--]
        [#--</div>--]
    </div>
    <!--人气品牌end-->
    <!--热门start-->
    <div class="hot-brands">
        <div class="container">
            <div class="hot-nav">
                <div class="left"></div>
                <h2>热门<span>HOT</span></h2>
                <div class="right"></div>
            </div>
            <div class="brand-items-wrap">
                <div class="nav-bar">
                    <ul>
                        <li class="on" onclick="getContent(this,0)">热门</li>
                    [#list category as category]
                    	[#list category.products as products]
                    		[#assign p_products_index=0]
	                        [#if products_index lt 10 && products.isMarketable == true]
	                        	[#assign p_products_index=1]
	                        [/#if]
                        [/#list]
                        [#if p_products_index gt 0]
                        <li onclick="getContent(this,${category_index+1})">${category.name}</li>
                        [/#if]
                    [/#list]
                    </ul>
                    <script type="text/javascript">
                        function getContent(obj, obj_content) {
                            $(obj).addClass("on").siblings().removeClass("on");
                            $("#" + obj_content).show().siblings().hide();
                        }
                        // function concern(obj,obj_id){
                        //     $(obj).css("background","#d62b2b");
                        // }

                    </script>
                </div>
                <div class="item-content">
                    <ul id="0" style="border-top:1px solid #ddd;border-bottom:0px;">
                        [#list dayProducts as products]
                        <li style="border-bottom:1px solid #ddd;border-top:0px;">
                            <a href="${base}/b2b/product/detail/${products.id}.jhtml">
                                <div class="black-border"></div>
                                <div class="white-border"></div>
                                <div class="item-img">
                                    <img src="${products.image}" width="237" height="237">
                                </div>
                                <div class="item-info">
                                    <img src="${(products.brand.logo)!}" width="120" height="45" alt="">
                                    [#if products.promotions??]
                                        [#list products.promotions as promotions]
                                            <p>${(promotions.name)!}</p>
                                        [/#list]
                                    [/#if]
                                </div>
                            </a>
                            <div class="go-follow">
                                <i onclick="addFavorite(${products.id},this)"
                                    [#if member??]
                                        [#list member.favoriteProducts as product]
                                    [#if product.id==products.id]class="save_product"[/#if]
                                        [/#list]
                                    [/#if]>+
                                </i>
                                <span class="follow">
                                    [#assign a=true]
                                    [#if member??]
                                        [#list member.favoriteProducts as product]
                                            [#if product.id==products.id]
                                                [#assign a=false]
                                                已关注
                                            [/#if]
                                        [/#list]
                                        [#if a==true]
                                            关注
                                        [/#if]
                                    [#else]
                                        关注
                                    [/#if]
                                </span>
                            </div>
                        </li>
                        [/#list]
                    </ul>
                    [#list category as category]
                    <ul style="display:none;border-top:1px solid #ddd;border-bottom:0px;" id="${category_index+1}">
                        [#list category.products as products]
                        [#if products_index lt 10 && products.isMarketable == true]
                            <li style="border-bottom:1px solid #ddd;border-top:0px;">
                                <a href="${base}/b2b/product/detail/${products.id}.jhtml">
                                    <div class="black-border"></div>
                                    <div class="white-border"></div>
                                    <div class="item-img">
                                        <img src="${products.image}" width="237" height="237">
                                    </div>
                                    <div class="item-info">
                                        <img src="${(products.brand.logo)!}" width="120" height="45" alt="">
                                        [#if products.promotions??]
                                            [#list products.promotions as promotions]
                                                <p>${(promotions.name)!}</p>
                                                ${category.products_size}
                                            [/#list]
                                        [/#if]
                                    </div>
                                </a>

                                <div class="go-follow">
                                    <i onclick="addFavorite(${products.id},this)"
                                        [#if member??]
                                            [#list member.favoriteProducts as product]
                                        [#if product.id==products.id]class="save_product"[/#if]
                                            [/#list]
                                        [/#if]>+
                                    </i>
                                <span class="follow">
                                    [#assign a=true]
                                    [#if member??]
                                        [#list member.favoriteProducts as product]
                                            [#if product.id==products.id]
                                                [#assign a=false]
                                                已关注
                                            [/#if]
                                        [/#list]
                                        [#if a==true]
                                            关注
                                        [/#if]
                                    [#else]
                                        关注
                                    [/#if]
                                </span>
                                </div>
                            </li>
                        [/#if]
                        [/#list]
                    </ul>
                    [/#list]
                </div>
            </div>
        </div>
    </div>
    <!--热门品牌end-->
    <!--标语start-->
[#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>


<!--主页内容区end-->

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
[#include "/b2b/include/hover_right.ftl"]
<!--右侧悬浮框end-->
</body>
</html>