<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link rel="shortcut icon" href="${base}/favicon.ico" />
    <link href="${base}/resources/helper/css/tenant.css" type="text/css" rel="stylesheet" />
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function() {
        [#if !tenant??]
            $("#addTenant").click(function() {
                $.message("success", "正在努力跳转....");
                location.href = "${base}/helper/member/tenant/add.jhtml";
            });
        [/#if]
        });
    </script>

</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="box_con_right">
                <div class="box_con_right_title_bg1">
                    <img src="${base}/resources/helper/images/wykd_title_bg1.jpg"/>简单三步 开通网店
                </div>
                <div class="box_con_right_con_bg1">
                    <ul class="f_left">
                        <li class="bg1">注册成为平台用户</li>
                        <li [#if tenant??] class="bg2" [/#if]><span class="f_left">完善公司信息</span>
                            <!--<img src="${base}/resources/helper/images/wykd_pic_bg4.jpg" />--></li>
                        <li [#if tenant??] [#if tenant.status='success'] class="bg3" [/#if] [/#if]><span class="f_left">等待验证通过</span>
                            <!--<img src="${base}/resources/helper/images/wykd_pic_bg4.jpg"/>--></li>
                    </ul>
                    <div class="box_con_right_con_btn">
                        <input type="button" value="已完成" class="wykd_btn_bg1"/>
                        <input type="button" [#if tenant??] value="已填写" class="wykd_btn_bg1" [#else] id="addTenant"
                               value="去填写" class="wykd_btn_bg2"[/#if]/>
                        <input type="button" [#if tenant??] [#if tenant.status='success'] value="已开通"
                               class="wykd_btn_bg1" [#else] [#if tenant.status='fail'] value="去开通"
                               class="wykd_btn_bg2" [#else] value="验证中" class="wykd_btn_bg2" [/#if] [/#if] [#else]
                               value="待申请" class="wykd_btn_bg2"  [/#if]/>
                    </div>
                </div>
                <div class="box_con_right_title_bg2">
                    <p class="f_left"><span class="w3"><img src="${base}/resources/helper/images/wykd_title_bg2.jpg"/></span><span>完成三步</span><span
                            class="w4">免费获得价格</span> <span class="w2"><img
                            src="${base}/resources/helper/images/wykd_pic_bg5.jpg"/></span><span
                            class="w5">元的商家版</span></p>
                    <p class="f_left"><img src="${base}/upload/images/wykd_pic_bg6.png"
                                           style=" margin-bottom:10px;"/></p>
                    <input type="button" value="即将上线，敬请期待" class="wykd_btn_bg3"/>
                </div>
                <div class="box_con_right_con_bg1 f_left">
                    <!-- <input type="button" value="了解店家助手" class="wykd_btn_bg4" style="margin-left:25px;"/>
                    <input type="button" value="去完成平台认证" class="wykd_btn_bg4"/> -->
                    <!--input type="button" id="kf_btn" value="联系客服" class="wykd_btn_bg4" style="margin-right:0;"/-->
                    <!-- <a href="#" target="_blank" class="wykd_btn_bg6">联系客服</a> -->
                </div>
                <div class="box_con_right_con_bg2">
                    <p>帮助文档：</p>
                    <p>1、为什么要完善公司信息？</p>
                    <p>2、需要登记的公司信息都有哪些内容？</p>
                    <p>3、公司信息的安全政策？</p>
                    <p>4、什么是验证？验证需要等多久？</p>
                </div>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
