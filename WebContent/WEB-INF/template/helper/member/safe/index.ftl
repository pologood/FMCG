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
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css">
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/product.css">

    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/font/iconfont.css"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            $().ready(function () {
                $("#clickHere").click(function () {
                    var link = $(this).attr('href');
                    $('.wrapper').attr('src', link);
                    var href = window.location.href;
                    window.parent.location.href = href.substr(0, href.indexOf('/tiaohuo')) + link;
                    return false;
                });
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

            <div class="box_aqsz_con">
                <p>
                    <span>手机号码：</span>
                    <span class="color">${mosaic(member.mobile, 3, "~~~")}</span>
                </p>
                <p>
                    <a href="${base}/helper/member/safe/bindmobile.jhtml">修改手机</a>
                    <a href="${base}/helper/member/safe/password.jhtml">修改密码</a>
                </p>
                <p class="w">
                    <span>上次登录：${member.loginDate?string("yyyy-MM-dd HH:mm:ss")}（不是您登录的？请</span>
                    <a href="${base}/helper/login.jhtml">点击这里</a>）
                </p>
            </div>

            <div class="box_aqsz_title2">你的安全服务</div>

            <div class="box_aqsz_con2">
                <p class="f_left">
                    <span>安全等级:</span>
                    <span class="color2">中</span>
                    <img src="${base}/resources/helper/images/aqsz_pic_bg3.jpg"/>
                <p class="f_right w">完成<a>密保设置</a>，提升账户安全</p>
                </p>
                <div class="box_aqsz_con2_bg">
                    <h1 class="f_left [#if member.authStatus!="success"]mb[/#if]">
                        ${message("Member.AuthStatus."+member.authStatus)}
                    </h1>
                    <p class="f_left" style="width:560px;">
                        <span class="m">身份认证</span>
                        <span class="m2">用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息。</span>
                    </p>
                    <a class="mk" href="${base}/helper/member/safe/idcard.jhtml">[#if member.authStatus=="none"]
                        去认证[#else]查看[/#if]</a>
                </div>

                <div class="box_aqsz_con2_bg">
                    <h1 class="f_left">已设置</h1>
                    <p class="f_left" style="width:560px;">
                        <span class="m">支付密码</span>
                        <span class="m2">安全性高的密码可以使账号更安全。建议您定期更换密码，且长度只能是6位的数字密码。</span>
                    </p>
                    <a class="mk" href="${base}/helper/member/safe/paymentPassword.jhtml">修改</a>
                    <a class="mk" href="${base}/helper/member/safe/find_payment.jhtml">找回</a>
                </div>

                <!-- <div class="box_aqsz_con2_bg">
                  <h1 class="f_left mb">未设置</h1>
                  <p class="f_left" style="width:560px;">
                    <span class="m">密保问题</span>
                    <span class="m2">
                      是您找回登录密码的方式之一。建议您设置一个容易记住，且最不容易被他人获取的问题及答案，更有效保障您的密码安全。
                    </span>
                  </p>
                  <a class="mk"  href="#" name="" style="disabled:false;">设置</a>
                </div> -->

                <div class="box_aqsz_con2_bg">
                    <h1 class="f_left[#if member.bindMobile!="binded"] mb[/#if]">[#if member.bindMobile=="binded"]
                        已绑定[#else]未绑定[/#if]</h1>
                    <p class="f_left" style="width:560px;">
                        <span class="m">手机绑定</span>
                        <span class="m2">用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息。</span>
                    </p>
                    <a class="mk" href="${base}/helper/member/safe/bindmobile.jhtml">
                    [#if member.bindMobile=="binded"]解绑[#else]去绑定[/#if]
                    </a>
                </div>

                <!-- <div class="box_aqsz_con2_bg">
                  <h1 class="f_left" mb>未设置</h1>
                  <p class="f_left" style="width:560px;">
                    <span class="m">操作保护设置</span>
                    <span class="m2">
                      在账号进行敏感操作（如：修改密码等）的时候对账号进行保护，进一步提高账号的安全性，防止他人恶意盗用。
                    </span>
                  </p>
                  <a class="mk"  href="javascript:;" style="disabled:false;">维护</a>
                </div> -->

            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
