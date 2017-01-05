<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-支付完成</title>
    <meta name="keywords" content="${setting.siteName}-支付完成" />
    <meta name="description" content="${setting.siteName}-支付完成" />
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon">
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部start -->
<div class="header bg">
     <!-- 顶部导航开始 -->
    [#include "/b2c/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--logo 搜索开始-->
    [#include "/b2c/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--guide 导向开始-->
    <div class="container">
        <div class="guide">
            <div class="guideProcess">
                <div class="guideProcessAll processOne on">
                    <div class="title"><h1>1</h1></div>
                    <span>购物车</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processTwo on">
                    <div class="title"><h1>2</h1></div>
                    <span>确认订单</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processThree on">
                    <div class="title"><h1>3</h1></div>
                    <span>支付</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processFour on">
                    <div class="title"><h1>√</h1></div>
                    <span>完成</span>
                    <div class="processBar"></div>
                </div>
            </div>
        </div>
    </div>
    <!--guide 导向结束-->
</div>
<!--头部end-->
<!--我的购物车内容start-->
<div class="container">
    <div class="pay-complete">
        <div class="pay-complete-head clearfix">
            <div class="tit">
                [#if offline=="true"]
                <span class="">您已成功下单，请耐心等待商家发货！<span>
                [#else]
                <span>您已成功付款</span>
                <span class="color">￥</span>
                <span class="color">${order.amount}</span>
                <span>我们将尽快安排发货，请您耐心等待！</span>
                [/#if]
            </div>
        </div>
        <div class="pay-complete-info">
            <h2>订单信息：</h2>
            <div>
                <span class="">订单编号：<span class="color">${order.sn}</span></span>
                <a href="${base}/b2c/member/order/list.jhtml">[查看订单]</a>
                <span class="f-right">应付金额：<span class="color">￥</span><span class="color">${order.amount}</span></span>
            </div>
            <div>
                <span class="">配送方式：<span>${order.shippingMethodName}</span></span>
                <span class="f-right">支付方式：<span>${order.paymentMethodName}</span></span>
            </div>
           <!--  <div>
                <span class="">请您在<span>${order.expire}</span>&nbsp;<span></span>之前支付订单款项，否则该订单将失效！</span>
            </div> -->
        </div>
        <div class="back-home">
            <a href="${base}/b2c/index.jhtml">返回首页>></a>
        </div>
    </div>
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
<!--标语start-->
[#include "b2c/include/slogen.ftl"]
<!--标语end-->
<!--底部start-->
[#include "/b2c/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>
<!--右侧悬浮框end-->
</body>
</html>