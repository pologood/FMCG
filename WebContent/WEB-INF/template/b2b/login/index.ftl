<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-登录</title>
    <meta name="keywords" content="${setting.siteName}-登录"/>
    <meta name="description" content="${setting.siteName}-登录"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<div class="pc-g header-bg">
    <div class="container">
        <div class="f-left">
            <a href="${base}/b2b/index.jhtml">
                <img class="header-login" src="${base}/resources/b2b/images/logo_01.png" alt="${setting.siteName}-登录">
            </a>
        </div>
    </div>
</div>

<div class="pc-g">
    <div class="login-wrap">
        <div class="overflow-v container">
            <div class="login-form">

                <!-- 登录 -->
                <div id="_signin" class="login-box">
                    <div class="mt">
                        <h1>欢迎登录</h1>
                        <div class="extra-r">
                            <div class="regist-link">
                                还没有账号？
                                <a href="[#if versionType==1]${base}/b2b/register/register_company.jhtml[#else]javascript:Login('register');[/#if]">立即注册</a>
                            </div>
                        </div>
                    </div>
                    <div class="msg-wrap">
                        <div class="msg-warn"><b></b>公共场所不建议记住密码，以防账号丢失</div>
                        <div class="msg-error hidden"><b></b><span id="error_msg"></span></div>
                    </div>
                    <div class="mc">
                        <div class="form">
                            <div class="item item-fore1">
                                <label for="" class="login-label name-label"></label>
                                <input id="username" class="itxt" autocomplete="off" value="" placeholder="已验证手机"
                                       type="text">
                                <span class="clear-btn"></span>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="password" class="itxt itxt-error" autocomplete="off" placeholder="密码"
                                       type="password">
                                <span class="clear-btn"></span>
                                <span class="capslock"><b></b>大小写锁定已打开</span>
                            </div>
                            <div class="item item-fore3">
                                <div class="safe">
                                        <span>
                                            <input id="isRememberUsername" name="isRememberUsername" class="checkbox"
                                                   tabindex="3" type="checkbox">
                                            <label for="">记住密码</label>
                                        </span>
                                        <span class="forget-pw-safe">
                                            <a href="javascript:Login('modifyPass');">忘记密码?</a>
                                        </span>
                                </div>
                            </div>
                        [#--验证码--]
                        [#--<div id="o-authcode" class="item item-vcode item-fore4">--]
                        [#--<input style="ime-mode: disabled;" id="authcode" class="itxt itxt02" name="authcode" tabindex="5" type="text">--]
                        [#--<img id="JD_Verification1" class="verify-code" src="https://authcode.jd.com/verify/image?a=1&amp;acid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27&amp;uid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27" onclick="this.src= document.location.protocol +'//authcode.jd.com/verify/image?a=1&amp;acid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27&amp;uid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27&amp;yys='+new Date().getTime();$('#authcode').val('');">--]
                        [#--<a href="javascript:void(0)" onclick="$('#JD_Verification1').click();">看不清楚换一张</a>--]
                        [#--</div>--]
                            <div class="item item-fore5">
                                <div class="login-btn">
                                    <a href="javascript:submit();" id="_login"
                                       class="btn-img btn-entry">登&nbsp;&nbsp;&nbsp;&nbsp;录</a>
                                </div>
                            </div>
                        </div>
                        <!-- div class="coagent">
                            <h5>使用合作网站账号登录：</h5>
                            <ul>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">新浪邮箱</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">QQ</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">微信</a>
                                </li>
                            </ul>
                        </div -->
                    </div>
                </div>

                <!-- 注册 -->
                <div id="_register" class="login-box hidden">
                    <div class="mt">
                        <h1>新用户注册</h1>
                        <div class="extra-r">
                            <div class="regist-link">
                                已有账号？
                                <a href="javascript:Login('signin');">马上登录</a>
                            </div>
                        </div>
                    </div>
                    <div class="msg-wrap">
                        <div class="msg-warn"><b></b>公共场所不建议记住密码，以防账号丢失</div>
                        <div class="msg-error hidden"><b></b></div>
                    </div>
                    <div class="mc">
                        <div class="form">

                            <div class="item item-fore1">
                                <label for="" class="login-label name-label"></label>
                                <input id="registerPhone" class="itxt" autocomplete="off" value="" placeholder="请输入手机号码"
                                       type="text">
                                <span class="clear-btn"></span>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="captcha1" class="itxt itxt-error" autocomplete="off"
                                       placeholder="请输入验证码" type="text">
                                <a href="javascript:;" class="get-btn1" style="background-color: white;">
                                    <img id="captchaImage1" class="captchaImage1" src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                                </a>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="registerCode" class="itxt itxt-error" autocomplete="off"
                                       placeholder="请输入手机验证码" type="text">
                                <a href="javascript:getCode('code');" class="get-btn">获取验证码</a>
                            </div>
                            <div class="item item-fore3">
                                <div class="safe">
                                        <span>
                                            <input name="" class="checkbox" checked type="checkbox">
                                            <label for="">我已阅读并同意</label>
                                            <a href="javascript:;" target="_blank">《${setting.siteName}服务协议》</a>
                                        </span>
                                </div>
                            </div>
                            <div class="item item-fore5">
                                <div class="login-btn">
                                    <a href="javascript:register_modify_password('code');" class="btn-img btn-entry">注&nbsp;册&nbsp;并&nbsp;登&nbsp;录</a>
                                </div>
                            </div>
                        </div>
                        <!-- div class="coagent">
                            <h5>使用合作网站账号登录：</h5>
                            <ul>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">新浪邮箱</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">QQ</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">微信</a>
                                </li>
                            </ul>
                        </div -->
                    </div>
                </div>

                <!-- 修改密码 -->
                <div id="_modifyPass" class="login-box hidden">
                    <div class="mt">
                        <h1>找回密码</h1>
                        <div class="extra-r">
                            <div class="regist-link">
                                已有账号？
                                <a href="javascript:Login('signin');">马上登录</a>
                            </div>
                        </div>
                    </div>
                    <div class="msg-wrap">
                        <div class="msg-warn"><b></b>公共场所不建议记住密码，以防账号丢失</div>
                        <div class="msg-error hidden"><b></b></div>
                    </div>
                    <div class="mc">
                        <div class="form">
                            <div class="item item-fore1">
                                <label for="" class="login-label name-label"></label>
                                <input id="passPhone" class="itxt" autocomplete="off" value="" placeholder="请输入手机号码"
                                       type="text">
                                <span class="clear-btn"></span>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="captcha" class="itxt itxt-error" autocomplete="off"
                                       placeholder="请输入验证码" type="text">
                                <a href="javascript:;" class="get-btn1" style="background-color: white;">
                                    <img id="captchaImage" class="captchaImage" src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                                </a>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="passCode" class="itxt itxt-error" autocomplete="off" placeholder="请输入手机验证码"
                                       type="password">
                                <a href="javascript:getCode('pass');" class="get-btn">获取验证码</a>
                            </div>
                            <div class="item item-fore3">

                            </div>
                            <div class="item item-fore5">
                                <div class="login-btn">
                                    <a href="javascript:register_modify_password('pass');" class="btn-img btn-entry">下&nbsp;&nbsp;一&nbsp;&nbsp;步</a>
                                </div>
                            </div>
                        </div>
                        <!-- div class="coagent">
                            <h5>使用合作网站账号登录：</h5>
                            <ul>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">新浪邮箱</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">QQ</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">微信</a>
                                </li>
                            </ul>
                        </div -->
                    </div>
                </div>


                <div id="_resetPassword" class="login-box hidden">
                    <div class="mt">
                        <h1>重置密码</h1>
                        <div class="extra-r">
                            <div class="regist-link">
                                已有账号？
                                <a href="javascript:Login('signin');">马上登录</a>
                            </div>
                        </div>
                    </div>
                    <div class="msg-wrap">
                        <div class="msg-warn"><b></b>公共场所不建议记住密码，以防账号丢失</div>
                        <div class="msg-error hidden"><b></b><span id="error_msg"></span></div>
                    </div>
                    <div class="mc">
                        <div class="form">
                            <div class="item item-fore1">
                                <label for="" class="login-label name-label"></label>
                                <input id="_username" class="itxt" autocomplete="off" value="" placeholder="已验证手机"
                                       type="text">
                                <span class="clear-btn"></span>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="npassword" class="itxt itxt-error" autocomplete="off" placeholder="请输入新密码"
                                       type="password">
                                <span class="clear-btn"></span>
                                <span class="capslock"><b></b>大小写锁定已打开</span>
                            </div>
                            <div class="item item-fore2">
                                <label class="login-label pwd-label" for=""></label>
                                <input id="enpassword" class="itxt itxt-error" autocomplete="off" placeholder="请再次输入新密码"
                                       type="password">
                                <span class="clear-btn"></span>
                                <span class="capslock"><b></b>大小写锁定已打开</span>
                            </div>
                        [#--验证码--]
                        [#--<div id="o-authcode" class="item item-vcode item-fore4">--]
                        [#--<input style="ime-mode: disabled;" id="authcode" class="itxt itxt02" name="authcode" tabindex="5" type="text">--]
                        [#--<img id="JD_Verification1" class="verify-code" src="https://authcode.jd.com/verify/image?a=1&amp;acid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27&amp;uid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27" onclick="this.src= document.location.protocol +'//authcode.jd.com/verify/image?a=1&amp;acid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27&amp;uid=5357b27a-aa98-409a-a4d6-c36f0fbe3f27&amp;yys='+new Date().getTime();$('#authcode').val('');">--]
                        [#--<a href="javascript:void(0)" onclick="$('#JD_Verification1').click();">看不清楚换一张</a>--]
                        [#--</div>--]
                            <div class="item item-fore5">
                                <div class="login-btn">
                                    <a href="javascript:resetPassword();" class="btn-img btn-entry">提&nbsp;&nbsp;&nbsp;&nbsp;交</a>
                                </div>
                            </div>
                        </div>
                        <!-- div class="coagent">
                            <h5>使用合作网站账号登录：</h5>
                            <ul>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">新浪邮箱</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">QQ</a>
                                    <span class="line">|</span>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="window.location=''+window.location.search;return false;">微信</a>
                                </li>
                            </ul>
                        </div -->
                    </div>
                </div>

                <!-- 扫描二维码登录 -->
                <div class="qrcode-login">
                    <div class="mc">
                        <div class="qrcode-desc"><h2>用${setting.siteName}APP <span>扫码安全登录</span></h2></div>
                        <div class="qrcode-error">
                            <b></b>
                            <h6>登录失败</h6>
                            请刷新二维码后重新扫描
                        </div>
                        <div class="qrcode-main">
                            <div class="qrcode-img">
                                <img src="//qr.m.jd.com/show?appid=133&amp;size=147&amp;t=1457084537291" alt="">
                                <div class="qrcode-error02 hidden">
                                    <a href="#none">
                                        <span class="error-icon"></span>
                                        <div class="txt">网络开小差咯 <span> 刷新二维码</span></div>
                                    </a>
                                </div>
                            </div>
                            <div class="qrcode-panel">
                                <ul>
                                    <li class="fore1">
                                        <a href="#none">刷新二维码</a>
                                    </li>
                                    <li><a href="javascript:;">使用帮助</a></li>
                                </ul>
                                <div class="qrcode-tips">
                                    <span>扫描不上，版本过低？</span>
                                    <div class="triangle-border tb-border"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <a href="javascript:changeLogin();" class="qrcode-target btn-2qrcode">扫码登录</a>
            </div>
        </div>
        <div class="login-banner">
            <div class="container">
                <div id="banner-bg" class="i-inner">
                [@ad_position id=113  count=1/]
                </div>
            </div>
        </div>
    </div>

</div>

[#--<!--标语 -->--]
[#--[#include "/b2c/include/slogen.ftl"]--]
<!--底部 -->
[#include "/b2b/include/foot.ftl"]


<script type="text/javascript">
    Login('${type}');
    /**
     * 登录  注册   修改密码
     * @param type
     *       【signin】登录
     *       【register】注册
     *       【modifyPass】修改密码
     * @constructor
     */
    function Login(type) {
        $('.msg-error').hide();
        if (type == "signin") {
            $("#_signin").removeClass("hidden");
            $("#_register").addClass("hidden");
            $("#_modifyPass").addClass("hidden");
            $("#_resetPassword").addClass("hidden");
        } else if (type == "register") {
            $("#_signin").addClass("hidden");
            $("#_register").removeClass("hidden");
            $("#_modifyPass").addClass("hidden");
            $("#_resetPassword").addClass("hidden");
            //$("#captchaImage1").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}")
        } else if (type == "modifyPass") {
            $("#_signin").addClass("hidden");
            $("#_register").addClass("hidden");
            $("#_modifyPass").removeClass("hidden");
            $("#_resetPassword").addClass("hidden");
            //$("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}")
        }
    }

    var count = 60, ii;
    function refreshTime() {
        count = count - 1;
        if (count == 0) {
            $(".get-btn").html("获取验证码");
            count = 60;
            clearInterval(ii);
            return false;
        }
        $(".get-btn").html(count + "秒后重新获取");
    }

    function getCode(type) {
        if (count != 60) {
            return;
        }
        var _mobile = "",_captcha="",_captchaImage="";

        if (type == 'code') {
            _mobile = $("#registerPhone").val().trim();
            _captcha = $("#captcha1").val();
            _captchaImage =$("#captchaImage1");
        } else if (type == 'pass') {
            _mobile = $("#passPhone").val().trim();
            _captcha = $("#captcha").val();
            _captchaImage =$("#captchaImage");
        }

        if ($.trim(_mobile) == '') {
            $.message('warn', "请先填写手机号码");
            return false;
        }
        if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test(_mobile))) {
            $.message('warn', "请确认您的号码是否正确");
            return false;
        }

        $.ajax({
            url: "${base}/b2b/login/getCheckCode.jhtml",
            data: {
                mobile: _mobile,
                captchaId:"${captchaId}",
                captcha:_captcha
            },
            type: "POST",
            dataType: "json",
            cache: false,
            success: function (data) {
                $.message(data.type, data.content);
                if(data.type=="success"){
                    ii = setInterval(refreshTime, 1 * 1000);
                    $(".get-btn").html(count + "秒后重新获取");
                }else{
                    _captchaImage.attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
                }
            }
        });

    }

    var _i = 0;
    function submit() {
        if (_i != 0) {
            return;
        }
        if ($("#username").val().trim() == "") {
            $('.msg-error').show();
            $('#error_msg').text("请先填写手机号");
            return;
        }
        if ($("#password").val().trim() == "") {
            $('.msg-error').show();
            $('#error_msg').text("请先填写密码");
            return;
        }
        _i = 1;
        $("#_login").html("正在为您跳转");
        $.ajax({
            url: "${base}/common/public_key.jhtml",
            type: "POST",
            data: {local: true},
            dataType: "json",
            cache: false,
            success: function (data) {
                var rsaKey = new RSAKey();
                rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                var enPassword = hex2b64(rsaKey.encrypt($("#password").val()));

                $.ajax({
                    url: "${base}/b2b/login/submit.jhtml",
                    type: "POST",
                    data: {
                        username: $("#username").val(),
                        enPassword: enPassword
                    },
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            $.message("success", "登录成功！正在为您跳转....");
                            setTimeout(function () {
                            [#if redirectUrl??]
                                location.href = "${redirectUrl}";
                            [#else]
                                location.href = "${base}/b2b/index.jhtml";
                            [/#if]
                            }, 1500);

                            if ($("#isRememberUsername").prop("checked")) {
                                addCookie("memberUsername", $("#username").val(), {expires: 7 * 24 * 60 * 60});
                            } else {
                                removeCookie("memberUsername");
                            }
                        } else {
                            $.message(message);
                            _i = 0;
                            $("#_login").html("登&nbsp;&nbsp;&nbsp;&nbsp;录");
                        }
                    }
                });
            }
        });
    }

    //注册或者修改密码
    var _securityCode = "";
    function register_modify_password(type) {
        var _mobile = "";
        var _code = "";

        if (type == 'code') {
            _mobile = $("#registerPhone").val().trim();
            _code = $("#registerCode").val().trim();

        } else if (type == 'pass') {
            _mobile = $("#passPhone").val().trim();
            _code = $("#passCode").val().trim();
        }

        if (_mobile == '' || _mobile == null) {
            $.message('warn', "请先填写手机号码");
            return false;
        }
        if (_code == '' || _code == null) {
            $.message('warn', "请先获取验证码");
            return false;
        }

        $(".get-btn").html("获取验证码");
        count = 60;
        clearInterval(ii);

        if (type == 'pass') {
            $.ajax({
                url: '${base}/b2b/login/check_mobile.jhtml',
                data: {
                    mobile: _mobile,
                    securityCode: _code
                },
                type: "POST",
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.type == "success") {
                        _securityCode = _code;
                        $("#_signin").addClass("hidden");
                        $("#_register").addClass("hidden");
                        $("#_modifyPass").addClass("hidden");
                        $("#_resetPassword").removeClass("hidden");
                        $("#_username").val(_mobile);
                    } else {
                        $.message(message.type, message.content);
                    }
                }
            });
        }
        if (type == 'code') {
            $.ajax({
                url: '${base}/b2b/login/register.jhtml',
                data: {
                    mobile: _mobile,
                    securityCode: _code
                },
                type: "POST",
                dataType: "json",
                cache: false,
                success: function (message) {
                    $.message(message.type, message.content);
                    if (message.type == 'success') {
                        location.href = "${base}/b2b/member/safe/index.jhtml";
                    }
                }
            });
        }
    }

    function resetPassword() {
        var _mobile = $("#_username").val().trim();
        var _npassword = $("#npassword").val().trim();
        var _enpassword = $("#enpassword").val().trim();
        if (_npassword == "" || _npassword == null) {
            $.message("warn", "请输入新密码");
            return;
        }
        if (_enpassword == "" || _enpassword == null) {
            $.message("warn", "请再次输入新密码");
            return;
        }

        if (_npassword != _enpassword) {
            $.message("warn", "两次密码不一致，请重新确认！");
            return;
        }


        $.ajax({
            url: "${base}/common/public_key.jhtml",
            type: "POST",
            data: {local: true},
            dataType: "json",
            cache: false,
            success: function (data) {
                var rsaKey = new RSAKey();
                rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                var enPassword = hex2b64(rsaKey.encrypt(_npassword));

                $.ajax({
                    url: "${base}/b2b/login/reset.jhtml",
                    type: "POST",
                    data: {
                        mobile: _mobile,
                        newpassword: enPassword,
                        securityCode: _securityCode
                    },
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        $.message(message.type, message.content);
                        if (message.type == 'success') {
                            Login('signin');
                        }
                    }
                });
            }
        });
    }

    //扫描二维码登录
    function changeLogin() {
//        if($('.qrcode-login').css('display')=='none'){
//            $('.qrcode-login').show();
//            $('.login-box').hide();
//        }else{
//            $('.qrcode-login').hide();
//            $('.login-box').show();
//        }
        $.message("success", "功能开发中，请耐心等待....");
    }

    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 13) { // enter 键
            submit();
        }
    };

    $("#captchaImage,#changeCaptcha").click(function () {
        $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
    });
</script>
</body>
</html>
