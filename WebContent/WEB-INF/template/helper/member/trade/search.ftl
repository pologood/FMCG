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
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/order.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $listForm = $("#listForm");
            var $sn = $("#sn");
            [@flash_message /]
            // 表单验证
            $listForm.validate({
                rules: {
                    sn: {
                        required: true,
                        pattern: /^[0-9a-zA-Z_-]+$/
                    }
                },
                messages: {
                    sn: {
                        required: "必填",
                        pattern: "${message("shop.validate.illegal")}"
                    }
                },
                submitHandler: function (form) {
                    form.submit();
                }
            });
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

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/my-order.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">验码提货</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">通过消费者手机收到的提货验码，验证提货。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/trade/list.jhtml">返回列表</a></li>
                </ul>
            </div>
            <form id="listForm" action="search.jhtml" method="post">
                <div class="w_modth">
                    <div class="w_modthinput"><input type="text" name="sn" id="sn" value="${sn}" maxlength="30"
                       placeholder="输入提货码"/></div>
                       <button type="submit">${message("shop.article.searchSubmit")}</button>
                   </div>
               </form>
               [#if content=="1"]
               <div class="w_noorder">查无此订单</div>
               [/#if]
               [#if content != "1"]
               <div class="w_modththsm">
                <h1>备注说明：</h1>
                <p>1.用户购买商品成功后，提货码以短信方式发送给用户，请填写正确地联系方式，注意查收短信；</p>
                <p>2.用户提货成功并确认货物无误时，请在后台确认提货。</p>
                <p>3.最终解释权归${setting.siteHelper}所有，咨询热线：${setting.phone}</p>
            </div>
            [/#if]
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
