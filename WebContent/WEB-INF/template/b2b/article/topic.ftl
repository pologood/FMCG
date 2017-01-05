<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-活动专题</title>
    <meta name="keywords" content="${setting.siteName}-活动专题"/>
    <meta name="description" content="${setting.siteName}-活动专题"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/data/data.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/flashSale.css" type="text/css" rel="stylesheet"/>
    <!-- <link href="${base}/resources/b2b/css/coupon_popup.css" type="text/css" rel="stylesheet"> -->
    <link href="${base}/resources/b2b/css/coupon.css" type="text/css" rel="stylesheet">
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
</script>
				<div style="float:none;width:100%;">${article.content}</div>
    <!--标语start-->
[#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--左侧悬浮框 start-->
[#include "/b2b/include/hover_left.ftl"/]
<!--左侧悬浮框end-->

<!--右侧悬浮框 start-->
[#include "/b2b/include/hover_right.ftl"/]
<!--右侧悬浮框end-->
</body>
</html>