<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>欢迎注册</title>
    <meta name="keywords" content="欢迎注册" />
    <meta name="description" content="欢迎注册" />
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/base64.js"></script>

    <link rel="icon" href="${base}/resources/b2c/images/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/register.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<div class="pc-g header-bg header_zc">
    <div class="container">
        <div class="logo-con clearfix">
            <a href="javascript:;" class="logo">
                <img src="${base}/resources/b2c/images/logo_zc.png" alt="" />
            </a>
            <div class="logo-title">欢 迎 注 册</div>
            <div class="have-account">已有账号 <a href="${base}/b2c/login.jhtml?type=login">请登录</a></div>
        </div>
    </div>
</div>

<div class="container">
    <div class="main clearfix">
        <div class="reg-form f-left">
            <form novalidate="novalidate" action="javascript:;" id="register-form" method="post">
            <input type="hidden" id="captchaId" name="captchaId" value="${captchaId}"/>
                <div class="item-regName-wrap">
                    <div class="form-item form-item-account">
                        <label>用　户　名</label>
                        <label for="form-account" class="txt">您的账户名和登录名</label>
                        <input id="form-account" name="nickName" class="field" maxlength="20" type="text" />
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                        <span>
                            <i class="i-def"></i>
                            支持中文、字母、数字、“-”“_”的组合，4-20个字符
                        </span>
                    </div>
                </div>

                <div class="item-pwd-wrap">
                    <div style="z-index: 12;" class="form-item">
                        <label>设 置 密 码</label>
                        <label for="password" class="txt">建议至少使用两种字符组合</label>
                        <input name="password" id="password" class="field" maxlength="20" type="password">
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <span>
                        <i class="i-def"></i>
                        建议使用字母、数字和符号两种及以上的组合，6-20个字符
                    </span>
                    </div>
                </div>

                <div class="item-pwdRepeat-wrap">
                    <div style="z-index: 12;" class="form-item">
                        <label>确 认 密 码</label>

                        <label class="txt" for="rePassword" >请再次输入密码</label>
                        <input name="rePassword" id="rePassword" class="field" maxlength="20" type="password">
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <span>
                        <i class="i-def"></i>
                        请再次输入密码
                    </span>
                    </div>
                </div>

                <div class="item-phone-wrap">
                    <div class="form-item form-item-phone">
                        <label class="select-country" id="select-country">手 机 验 证</label>
                        <label for="form-phone" class="txt">建议使用常用手机</label>
                        <input id="form-phone" name="mobile" class="field" maxlength="11" type="text">
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                        <span>
                            <i class="i-def"></i>
                            完成验证后，可以使用该手机登录和找回密码
                        </span>
                    </div>
                </div>

                <div class="item-authcode-wrap">
                    <div class="form-item form-item-authcode">
                        <label>验　证　码</label>
                        <label class="txt" for="authCode">请输入验证码</label>
                        <input name="authcode" id="authCode" maxlength="6" class="field form-authcode" type="text">
                        <img src="${base}/common/captcha.jhtml?captchaId=${captchaId}" class="img-code" title="换一换" id="imgAuthCode" >
                    </div>
                    <div class="input-tip">
                    <span id="change_captcha">
                        <i class="i-def"></i>
                        看不清？点击图片更换验证码
                    </span>
                    </div>
                </div>

                <div class="item-mobileCode-wrap">
                    <div class="form-item form-item-phonecode">
                        <label>手机验证码</label>
                        <label class="txt" for="phoneCode" >请输入手机验证码</label>
                        <input name="mobileCode" maxlength="6" id="phoneCode" class="field phonecode" type="text">
                        <button id="getPhoneCode" class="btn-phonecode" type="button">获取验证码</button>
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                        <span>
                            <i class="i-def"></i>
                            请在60秒内输入验证码，否则将验证失败
                        </span>
                    </div>
                </div>

                <div class="form-agreen">
                    <div><input type="checkbox" name="agree" checked id="agree" >我已阅读并同意<a href="javascript:;" id="protocol">《XX用户注册协议》</a> </div>
                    <div class="input-tip">
                        <span></span>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn-register" id="submit_register">立即注册</button>
                </div>
            </form>
        </div>
        <div class="reg-other">
            <div class="phone-fast-reg">
            </div>
            <div class="company-reg">
                <a href="${base}/b2c/register/register_company.jhtml" target="_top">
                    <i class="i-company"></i>
                    <span>企业用户注册</span>
                </a>
            </div>
            <!-- <div class="inter-cust">
                <a href="${base}/b2c/register/register_person.jhtml" target="_top">
                    <i class="i-global"></i>
                    <span>普通用户注册</span>
                </a>
            </div> -->
        </div>
    </div>
    [#include "/b2c/include/prototal.ftl"]
    <div class="ui-mask"></div>

</div>
<script type="text/javascript" src="${base}/resources/b2c/js/register/page_regist.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/register/jquery.properties-1.0.9.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/register/jquery.validate.js"></script>
<script type="text/javascript">
    $("#protocol").click(function () {
        $(".ui-dialog").show();
        $(".ui-mask").show();
    });
    $(".ui-dialog-close").click(function () {
        $(".ui-dialog").hide();
        $(".ui-mask").hide();
    });
    $(".ui-mask").click(function () {
        $(this).hide();
        $(".ui-dialog").hide();
    });
    $(function(){
        //=====验证码切换=======//
        $("#imgAuthCode").click(function () {
            $("#imgAuthCode").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
        });
        //======获取手机验证码========//
        $("#getPhoneCode").click(function(){
            if ($("#form-phone").val().trim() == '') {
                $.message("error", " 请先填写手机号");
                return false;
            }
            if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($("#form-phone").val()))) {
                $.message("error", "手机号码不符合");
                return;
            }
            if ($("#authCode").val().trim() == '') {
                $.message("error", " 请先填写验证码");
                return false;
            }
            $.ajax({
                url: "${base}/b2c/register/send.jhtml",
                data: {
                    username: $("#form-phone").val(),
                    captchaId:"${captchaId}",
                    captcha:$("#authCode").val()
                },
                dataType: "json",
                type: "post",
                success: function (data) {
                    $.message(data.type, data.content);
                    if (data.type == 'success') {
                      
                    }else {
                        $("#imgAuthCode").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
                    }
                }
            });
        });
        //======注册提交======//
        $("#submit_register").click(function(){
            if ($("#form-account").val().trim() == '') {
                $.message("error", "用户名不能为空");
                return;
            }
            if ($("#password").val().trim() == ''||$("#rePassword").val().trim() == '') {
                $.message("error", "密码不能为空");
                return;
            }
            if ($("#password").val().trim()!=$("#rePassword").val().trim()) {
                $.message("error", "两次密码输入不一致");
                return;
            }
            if(!$("#agree").prop("checked")) {
                $.message("error", "未同意《服务协议》");
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
                    var enPassword = hex2b64(rsaKey.encrypt($("#password").val().trim()));
                    $.ajax({
                        url: "${base}/b2c/register/register.jhtml",
                        type: "POST",
                        data: {
                            nickName:$("#form-account").val().trim(),
                            mobile:$("#form-phone").val().trim(),
                            newpassword: enPassword,
                            securityCode: $("#phoneCode").val().trim()
                        },
                        dataType: "json",
                        cache: false,
                        success: function (message) {
                            $.message(message.type, message.content);
                            location.href="${base}/b2c/index.jhtml";
                        }
                    });
                }
            });
        }); 
    });
    function refreshTime() {
        count = count - 1;
        if (count == 0) {
            count = 60;
            $getNum.attr('style', 'display:block');
            $span_2.attr('style', 'display:none');
            clearInterval(ii);
            return false;
        }
        $("#span_2").html(count + "秒后重新获取");

    }
</script>
<!--标语 -->
[#include "/b2c/include/slogen.ftl"]
<!--底部 -->
[#include "/b2c/include/footer.ftl"]
</body>
</html>