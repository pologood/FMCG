<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>登录页</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="stylesheet" href="${base}/weixin/style/wap.min.css"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/weixin/js/zepto.min.js"></script>
</head>
<body ontouchstart>
<div class="weui-toptips weui-toptips_warn js_tooltips">错误提示</div>
<div class="container" id="container">
    <div class="page" style="opacity: 1">
        <div class="page__bd" style="height:100%">
            <div class="weui-tab">
                <!--BEGIN login-->
                <div class="jd_login_bgc">
                    <div class="section1">
                        <img src="${base}/weixin/images/static/login_logo.png" alt="">
                    </div>
                    <!--login表单开始-->
                    <div class="form__bd">
                    <#--<form action="${base}/weixin/index/submit.jhtml" id="form-table" method="post">-->
                        <div class="form-cells disFlex">
                            <span class="form-title weui-tabbar__items "><i
                                    class="iconfont weui-tabbar__icon icon-phone f16"></i></span>
                            <input class="input-size f16 disFlex1" name="username" id="username" type="text"
                                   placeholder="请输入已注册的手机号"/>
                        </div>
                        <div class="form-cells disFlex">
                            <span class="form-title weui-tabbar__items"><i
                                    class="iconfont weui-tabbar__icon icon-l-me f16"></i></span>
                            <input class="input-size f16 disFlex1" name="enPassword" id="enPassword" type="password"
                                   placeholder="请输入登录密码"/>
                        </div>
                        <div class="form-cells ">
                        <#--<input id="loginFormButton" type="submit" class="submit-btn f18" value="登录"/>-->
                            <button class="submit-btn f18" id="loginFormButton">登录</button>
                        </div>
                    <#--</form>-->
                    </div>
                    <!--end-->
                    <div class="fromzc f14">
                        <p><a href="javascript:;" id="zc">注册</a></p>
                    </div>
                </div>
                <!--END login-->
            </div>
        </div>
    </div>

</div>
<#--<script type="text/javascript" src="${base}/weixin/js/wap.min.js"></script>-->
<script type="text/javascript">
    $(function () {

        $(document).on('click', '#zc', function () {
            window.location.href = "register_login.html";
        });
        $('.rePass').on('click', function () {
            $(this).css({"background": '#000'})
        });
        //登录验证
        var _i = 0;

        $('#loginFormButton').on('click', function () {
            if (_i != 0) {
                return;
            }
            if ($("#username").val().trim() == "") {
                //$('.msg-error').show();
                //$('#error_msg').text("请先填写手机号");
                return;
            }
            if ($("#enPassword").val().trim() == "") {
                //$('.msg-error').show();
                //$('#error_msg').text("请先填写密码");
                return;
            }
            _i = 1;
            $("#loginFormButton").html("正在为您跳转");
            $.ajax({
                url: "${base}/common/public_key.jhtml",
                type: "POST",
                data: {local: true},
                dataType: "json",
                cache: false,
                success: function (data) {
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                    var enPassword = hex2b64(rsaKey.encrypt($("#enPassword").val()));

                    $.ajax({
                        url: "${base}/weixin/index/submit.jhtml",
                        type: "POST",
                        data: {
                            username: $("#username").val(),
                            enPassword: enPassword
                        },
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            if (data.message.type == "success") {
                                $.message("success", "登录成功！正在为您跳转....");
                            <#--setTimeout(function () {-->
                            <#--[#if redirectUrl??]-->
                            <#--location.href = "${redirectUrl}";-->
                            <#--[#else]-->
                                window.location.href='${base}/weixin/member/member-home.html';
                            <#--[/#if]-->
                            <#--}, 1500);-->

                                if ($("#isRememberUsername").prop("checked")) {
                                    addCookie("memberUsername", $("#username").val(), {expires: 7 * 24 * 60 * 60});
                                } else {
                                    removeCookie("memberUsername");
                                }
                            } else {
                                alert(data.message.content);
                                $("#loginFormButton").html("登&nbsp;&nbsp;&nbsp;&nbsp;录");
                            }
                        }
                    });
                }
            });
        });
    })
</script>
</body>
</html>