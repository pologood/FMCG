<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery.validate.js"></script>
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>

    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.js"></script>

    <script type="text/javascript" src="${base}/resources/b2c/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
</head>
<script type="text/javascript">
    $(function () {
    [@flash_message /]
    });
</script>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2c/include/topnav.ftl"]
</div>

<!--主页内容区 -->
<div class="paper">
    <!-- 会员中心头部 -->
[#include "/b2c/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
        [#include "/b2c/include/member_left.ftl"]
            <div class="content">
                <!--安全设置-->
                <div class="my-settings">
                    <h1>账户设置</h1>
                    <div class="basic-info">
                        <form id="basicInfoForm" action="save.jhtml" method="post">
                            <h2>您的基本信息<a class="save" href="javascript:;" onclick="basicInfoSave()">保存</a></h2>
                            <dl>
                                <dt>用户名：</dt>
                                <dd>
                                    <input class="text" disabled type="text" value="${mosaic(member.username,3,'~~')}">
                                </dd>

                                <dt>
                                    用户头像:
                                </dt>
                                <dd>
                                    <span class="fieldSet">
                                        <input type="text" name="headImg" class="text" value="${member.headImg}"
                                               maxlength="200" title="${message("admin.product.imageTitle")}"
                                               readonly="true"/>
                                        <input type="button" id="browserLogoButton" class="button"
                                               value="${message("admin.browser.select")}"/>
                                    [#if member.headImg??]
                                        <a href="${member.headImg}" target="_blank">${message("admin.common.view")}</a>
                                    [/#if]
                                    </span>
                                </dd>
                                <dt>门店名称：</dt>
                                <dd>
                                    <input class="text" name="nickName" type="text" value="${(member.nickName)!}">
                                </dd>
                                <dt>电子邮箱：</dt>
                                <dd>
                                    <input class="text" name="email" type="text" value="${(member.email)!}">
                                </dd>
                                <dt class="location">所在地区：</dt>
                                <dd>
                                    <input type="hidden" id="areaId" name="areaId" value="${(member.area.id)!}"
                                           treePath="${(member.area.treePath)!}"/>
                                </dd>
                                <dt>详细地址：</dt>
                                <dd class="address">
                                    <input class="text" name="address" type="text" value="${(member.address)!}">
                                </dd>
                            </dl>
                        </form>
                    </div>
                    <div class="security">
                        <h2>您的安全服务</h2>
                        <div class="security-level">
                        [#assign level=0]
                        [#if idcard.authStatus=='success']
                            [#assign level=level+33.33]
                        [/#if]
                        [#if member.password?trim!=""&&member.password!=null]
                            [#assign level=level+33.33]
                        [/#if]
                        [#if member.bindMobile=='binded']
                            [#assign level=level+33.33]
                        [/#if]
                            <div class="dd">
                                安全等级：<em>[#if level==33.33]低[#elseif level=66.66]中[#elseif level==99.99]高[/#if]</em>
                            </div>
                            <div class="dd level-bar-bg">
                                <div class="level-bar" style="width: ${level+0.01}%"></div>
                            </div>
                        [#if idcard.authStatus!='success'&&idcard.authStatus!='wait']
                            <div class="dd">
                                完成<a href="#identity" onclick="$('#identity .view').trigger('click');">身份认证</a>，提升账号安全。
                            </div>
                        [/#if]
                        [#if member.bindMobile!='binded']
                            <div class="dd">
                                完成<a href="#bindMobile" onclick="$('#bindMobile .view').trigger('click');">绑定手机</a>，提升账号安全。
                            </div>
                        [/#if]
                        </div>
                        <div class="li" id="identity">
                            <div class="li-hd">
                                <i>[#if idcard.authStatus=='success']✔[#else]？[/#if]</i>
                                <div class="hint">
                                    <span>
                                    [#if idcard.authStatus=='success']已完成
                                    [#elseif idcard.authStatus=='none'||idcard.authStatus==null]未认证
                                    [#elseif idcard.authStatus=='wait']待审核
                                    [#elseif idcard.authStatus=='fail']认证拒绝
                                    [/#if]</span>
                                    <span>身份认证</span>
                                    <span class="title">用于提升账号安全性和信任级别，认证后的有卖家的账号不能修改认证信息。</span>
                                    <a class="f-right view" href="javascript:;">查看</a>
                                    <a style="display: none;" class="f-right hide" href="javascript:;">收起</a>
                                </div>
                            </div>
                            <div class="li-main display-n">
                                <form id="identityForm" action="identity.jhtml" method="post"
                                      enctype="multipart/form-data">
                                [#if idcard.authStatus=="none"||idcard.authStatus=="fail"||idcard.authStatus==null]
                                    <dl>
                                        <dt>姓名：</dt>
                                        <dd>
                                            <input class="text" name="name" type="text" value="${(member.name)!}">
                                        </dd>
                                        <dt>身份证号码：</dt>
                                        <dd>
                                            <input class="text" name="no" type="text" value="${(member.idcard.no)!}">
                                        </dd>
                                        <dt>身份证正面照片：</dt>
                                        <dd>
                                            <img width="168" height="126"
                                                 src="${base}/resources/wap/2.0/images/example-idcard-front.png"
                                                 onclick="$(this).next().click();">
                                            <input type="file" name="imageFront" style="display: none;" accept="image/*"
                                                   onchange="getFullPath(this)">
                                        </dd>
                                        <dt>身份证反面照片：</dt>
                                        <dd>
                                            <img width="168" height="126"
                                                 src="${base}/resources/wap/2.0/images/example-idcard-back.png"
                                                 onclick="$(this).next().click();">
                                            <input type="file" name="imageBack" style="display: none;" accept="image/*"
                                                   onchange="getFullPath(this)">
                                        </dd>
                                        <dt></dt>
                                        <dd>
                                            <a class="save-btn" href="javascript:;"
                                               onclick="$identityForm.submit();">提交</a>
                                        </dd>
                                    </dl>
                                [#else]
                                    <dl>
                                        <dt>姓名：</dt>
                                        <dd>
                                            <input class="text" name="name" type="text" value="${(member.name)!}"
                                                   readonly>
                                        </dd>
                                        <dt>身份证号码：</dt>
                                        <dd>
                                            <input class="text" name="no" type="text" value="${(member.idcard.no)!}"
                                                   readonly>
                                        </dd>
                                        <dt>门店名称：</dt>
                                        <dd>
                                            <input class="text" name="no" type="text" value="${(member.nickName)!}"
                                                   readonly>
                                        </dd>
                                        <dt>营业执照：</dt>
                                        <dd>
                                            <img width="168" height="126" src="${(member.tenant.licensePhoto)!}">
                                        </dd>
                                    </dl>
                                [/#if]
                                </form>
                            </div>
                        </div>
                        <div class="li">
                            <div class="li-hd">
                                <i>✔</i>
                                <div class="hint">
                                    <span>[#if member.password?trim!=""&&member.password!=null]已设置[#else]
                                        未设置[/#if]</span>
                                    <span>登录密码</span>
                                    <span class="title">安全性高的密码可以使账号更安全。建议您定期更换密码，且设置一个包含数字和字母，并长度超过6位以上的密码。</span>
                                    <a class="f-right view" href="javascript:;">查看</a>
                                    <a style="display: none;" class="f-right hide" href="javascript:;">收起</a>
                                </div>
                            </div>
                            <div class="li-main display-n">
                                <form action="passwordUpdate.jhtml" method="post" id="passwordForm">
                                    <input type="hidden" id="captchaId" name="captchaId" value="${captchaId}"/>
                                    <dl>
                                        <dt>现有密码：</dt>
                                        <dd>
                                            <input class="text" id="curPassword" name="curPassword" type="password"
                                            />
                                        </dd>
                                        <dt>输入新密码：</dt>
                                        <dd>
                                            <input class="text" id="newPassword" name="newPassword" type="password"
                                            />
                                        </dd>
                                        <dt>重新输入新密码：</dt>
                                        <dd>
                                            <input class="text" id="rePassword" name="rePassword" type="password"
                                            />
                                        </dd>
                                        <dt>验证码：</dt>
                                        <dd class="verify-code">
                                            <input id="captcha" name="captcha" class="text" type="text" maxlength="4"
                                                   autocomplete="off">
                                            <img id="captchaImage" class="captchaImage"
                                                 src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                                            <a id="changeCaptcha" href="javascript:;">换一张</a>
                                        </dd>
                                        <dt></dt>
                                        <dd>
                                            <a class="save-btn" href="javascript:;"
                                               onclick="$passwordForm.submit();">修改</a>
                                        </dd>
                                    </dl>
                                </form>
                            </div>
                        </div>
                    [#--<div class="li">--]
                    [#--<div class="li-hd">--]
                    [#--<i>？</i>--]
                    [#--<div class="hint">--]
                    [#--<span>未设置</span>--]
                    [#--<span>密保问题</span>--]
                    [#--<span class="title">是您找回登录密码的方式之一。建议您设置一个容易记住，且最不容易被他人获取的问题及答案，更有效保障您的密码安全。</span>--]
                    [#--<a class="f-right view" href="javascript:;">查看</a>--]
                    [#--<a style="display: none;" class="f-right hide" href="javascript:;">收起</a>--]
                    [#--</div>--]
                    [#--</div>--]
                    [#--<div class="li-main display-n">--]
                    [#--<dl class="">--]
                    [#--<dt>您最喜欢的运动：</dt>--]
                    [#--<dd>--]
                    [#--<input class="text" name="" type="text" value="">--]
                    [#--</dd>--]
                    [#--<dt>您最难忘的事情：</dt>--]
                    [#--<dd>--]
                    [#--<input class="text" name="" type="text" value="">--]
                    [#--</dd>--]
                    [#--<dt>您最擅长的事情：</dt>--]
                    [#--<dd>--]
                    [#--<input class="text" name="" type="text" value="">--]
                    [#--</dd>--]
                    [#--<dt></dt>--]
                    [#--<dd>--]
                    [#--<a class="save-btn" href="javascript:;">保存</a>--]
                    [#--</dd>--]
                    [#--</dl>--]
                    [#--</div>--]
                    [#--</div>--]
                        <div class="li" id="bindMobile">
                            <div class="li-hd">
                                <i>[#if member.password?trim!=""&&member.password!=null]✔[#else]？[/#if]</i>
                                <div class="hint">
                                    <span>[#if member.password?trim!=""&&member.password!=null]已设置[#else]
                                        未设置[/#if]</span>
                                    <span>支付密码</span>
                                    <span class="title">安全性高的密码可以使账号更安全。建议您定期更换密码，且设置一个包含数字和字母，并长度超过6位以上的密码。</span>
                                    <a class="f-right view" href="javascript:;">查看</a>
                                    <a style="display: none;" class="f-right hide" href="javascript:;">收起</a>
                                </div>
                            </div>
                            <div class="li-main display-n bindmobile">
                                <form action="" method="post" id="payPasswordForm">
                                    <input type="hidden" id="captchaId2" name="captchaId" value="${captchaId}"/>
                                    <dl>
                                        <dt>登录密码：</dt>
                                        <dd>
                                            <input class="text" id="curPassword2" name="curPassword" type="password"
                                                   value="" title=""/>
                                        </dd>
                                        <dt>支付密码：</dt>
                                        <dd>
                                            <input class="text" id="newPassword2" name="newPassword" type="password"
                                                   value="" title=""/>
                                        </dd>
                                        <dt>确认支付密码：</dt>
                                        <dd>
                                            <input class="text" id="rePassword2" name="rePassword" type="password"
                                                   value="" title=""/>
                                        </dd>
                                        <dt>验证码：</dt>
                                        <dd class="verify-code">
                                            <input id="captcha2" name="captcha" class="text" type="text" maxlength="4"
                                                   autocomplete="off" title=""/>
                                            <img id="captchaImage2" class="captchaImage"
                                                 src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                                            <a id="changeCaptcha2" href="javascript:;">换一张</a>
                                        </dd>
                                        <dt></dt>
                                        <dd>
                                            <a class="save-btn" href="javascript:;" onclick="update_pay_pas_submit();">修改</a>
                                        </dd>
                                    </dl>
                                </form>
                            </div>
                        </div>
                    [#--<div class="li">--]
                    [#--<div class="li-hd">--]
                    [#--<i>[#if member.bindEmail=='binded']✔[#else]？[/#if]</i>--]
                    [#--<div class="hint">--]
                    [#--<span>[#if member.bindEmail=='none']未绑定[#elseif member.bindEmail=='binded']已绑定[#elseif member.bindEmail=='unbind']已解绑[/#if]</span>--]
                    [#--<span>绑定邮箱</span>--]
                    [#--<span class="title">绑定邮箱后，您即可享受丰富的最新信息，如今日特卖等。</span>--]
                    [#--<a class="f-right view" href="javascript:;">查看</a>--]
                    [#--<a style="display: none;" class="f-right hide" href="javascript:;">收起</a>--]
                    [#--</div>--]
                    [#--</div>--]
                    [#--<div class="li-main display-n">--]
                    [#--<form action="" method="post" id="emailForm">--]
                    [#--<dl>--]
                    [#--<dt>请输入邮箱号码：</dt>--]
                    [#--<dd>--]
                    [#--<input class="text" id="email" name="email" type="text" value="">--]
                    [#--</dd>--]
                    [#--<dt></dt>--]
                    [#--<dd>--]
                    [#--<a class="save-btn" href="javascript:;" onclick="$emailForm.submit();">发送认证邮件</a>--]
                    [#--</dd>--]
                    [#--</dl>--]
                    [#--</form>--]
                    [#--</div>--]
                    [#--</div>--]

                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--可能感兴趣 -->
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
[#include "/b2c/include/slogen.ftl"]
</div>

<script type="text/javascript">
    var $basicInfoForm = $("#basicInfoForm");
    var $identityForm = $("#identityForm");
    var $passwordForm = $("#passwordForm");
    var $payPasswordForm = $("#payPasswordForm");
    var $emailForm = $("#emailForm");
    var $bindMobileForm = $("#bindMobileForm");
    var $mobileForm = $("#mobileForm");
    var $getSecurityCode = $("#getSecurityCode");
    var $browserLogoButton = $("#browserLogoButton");
    var settings_logo = {
        width: 120,
        height: 120
    };
    $browserLogoButton.browser(settings_logo);

    function getFullPath(file) {
        var fileList = file.files;
        var img = $(file).prev().get(0);
        if (file.files[0]) {
            img.style.display = 'block';
            img.style.width = '168px';
            img.style.height = '126px';
            if (window.createObjectURL != undefined) { // basic
                img.src = window.createObjectURL(file.files[0]);
            } else if (window.URL != undefined) { // mozilla(firefox)
                img.src = window.URL.createObjectURL(file.files[0]);
            } else if (window.webkitURL != undefined) { // webkit or chrome
                img.src = window.webkitURL.createObjectURL(file.files[0]);
            }
        }
    }
    function update_pay_pas_submit() {
        $payPasswordForm.submit();
    }

    $(function () {
        $(".view").click(function () {
            $(this).hide();
            $(this).next().show();
            $(this).parent().parent().next().show();
        });
        $(".hide").click(function () {
            $(this).hide();
            $(this).prev().show();
            $(this).parent().parent().next().hide();
        });
        $("#areaId").lSelect({
            url: "${base}/common/area.jhtml"
        });
        //---------修改登录密码——验证码--------//
        $("#captchaImage,#changeCaptcha").click(function () {
            $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
        });
        //---------修改支付密码——验证码--------//
        $("#captchaImage2,#changeCaptcha2").click(function () {
            $("#captchaImage2").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
        });
    });
    function basicInfoSave() {
        $.ajax({
            type: 'post',
            url: $basicInfoForm.attr("action"),
            data: $basicInfoForm.serialize(),
            dataType: 'json',
            success: function (data) {
                $.message(data);
                if (data.type == "success") {
                    window.setTimeout(function () {
                        window.location.reload(true);
                    }, 600);
                }
            }
        });
    }
    $identityForm.validate({
        rules: {
            name: "required",
            no: "required",
            imageFront: "required",
            imageBack: "required"
        },
        submitHandler: function (form) {
            form.submit();
        }
    });
    //---------修改登录密码-------------//
    $passwordForm.validate({
        rules: {
            curPassword: {
                required: true
            },
            newPassword: {
                required: true,
                pattern: /^[^\s&\"<>]+$/,
                minlength: ${setting.passwordMinLength},
                maxlength: ${setting.passwordMaxLength}
            },
            rePassword: {
                required: true,
                equalTo: newPassword
            },
            captcha: {
                required: true
            }
        },
        messages: {
            password: {
                pattern: "${message("box.validate.illegal")}"
            }
        },
        submitHandler: function (form) {
            $.ajax({
                url: "${base}/common/public_key.jhtml",
                dataType: "json",
                type: "post",
                success: function (data) {
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                    var curPassword = hex2b64(rsaKey.encrypt($("#curPassword").val()));
                    var password = hex2b64(rsaKey.encrypt($("#newPassword").val()));
                    $.ajax({
                        type: 'post',
                        url: 'passwordUpdate.jhtml',
                        data: {
                            curPassword: curPassword,
                            password: password,
                            captcha: $("#captcha").val(),
                            captchaId: $("#captchaId").val()
                        },
                        dataType: 'json',
                        success: function (data) {
                            $.message(data);
                            if (data.type == "success") {
                                window.setTimeout(function () {
                                    window.location.reload();
                                }, 600);
                            } else {
                                $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
                            }
                        }
                    });
                }
            });
        }
    });
    //-----------修改支付密码--------------//
    $payPasswordForm.validate({
        rules: {
            curPassword: {
                required: true
            },
            newPassword: {
                required: true,
                pattern: /^[^\s&\"<>]+$/,
                minlength: ${setting.passwordMinLength},
                maxlength: ${setting.passwordMaxLength}
            },
            rePassword: {
                required: true

            },
            captcha: {
                required: true
            }
        },
        messages: {
            password: {
                pattern: "${message("box.validate.illegal")}"
            }
        },
        submitHandler: function (form) {
            if ($("#rePassword2").val() != $("#newPassword2").val()) {
                $.message("warn", "两次密码输入不一致");
                return;
            }
            $.ajax({
                url: "${base}/common/public_key.jhtml",
                dataType: "json",
                type: "post",
                success: function (data) {
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                    var curPassword2 = hex2b64(rsaKey.encrypt($("#curPassword2").val()));
                    var password2 = hex2b64(rsaKey.encrypt($("#newPassword2").val()));
                    $.ajax({
                        type: 'post',
                        url: 'payPasswordUpdate.jhtml',
                        data: {
                            curPassword: curPassword2,
                            password: password2,
                            captcha: $("#captcha2").val(),
                            captchaId: $("#captchaId2").val()
                        },
                        dataType: 'json',
                        success: function (data) {
                            $.message(data);
                            if (data.type == "success") {
                                window.setTimeout(function () {
                                    window.location.reload();
                                }, 600);
                            } else {
                                $("#captchaImage2").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
                            }
                        }
                    });
                }
            });
        }
    });
    $emailForm.validate({
        rules: {
            email: {
                required: true
            }
        },
        submitHandler: function (form) {
            $.ajax({
                type: 'post',
                url: 'send_email.jhtml',
                data: $emailForm.serialize(),
                dataType: 'json',
                success: function (data) {
                    $.message(data);
                }
            });
        }
    });
    //获取验证码
    $mobileForm.validate({
        rules: {
            mobile: {
                required: true,
                pattern: /^1\d{10}$/
            }
        },
        messages: {
            mobile: {
                pattern: "手机号不正确"
            }
        },
        submitHandler: function (form) {
            sendSecurityCode();
        }
    });
    //绑定手机
    $bindMobileForm.validate({
        rules: {
            securityCode: {
                required: true
            }
        },
        submitHandler: function (form) {
            $.ajax({
                type: 'post',
                url: 'mobileupdate.jhtml',
                data: $bindMobileForm.serialize(),
                dataType: 'json',
                success: function (data) {
                    if (data.type == "success") {
                    [#if member.bindMobile=='binded']
                        $.message("success", "解绑成功");
                    [#else]
                        $.message("success", "绑定成功");
                    [/#if]
                        window.setTimeout(function () {
                            window.location.reload();
                        }, 600);
                    } else {
                        $.message(data);
                    }
                }
            });
        }
    });

    function sendSecurityCode() {
        $.ajax({
            type: 'post',
            url: 'send_security_code.jhtml',
            data: $mobileForm.serialize(),
            dataType: 'json',
            success: function (data) {
                $.message(data);
                if (data.type == "success") {
                    $("#mobileDl").hide();
                    $("#securityCodeDl").show();
                    var count = 60;
                    $getSecurityCode.unbind("click");
                    var t = window.setInterval(function () {
                        count--;
                        $getSecurityCode.text(count + "秒后重新获取");
                        if (count == 0) {
                            window.clearInterval(t);
                            $getSecurityCode.text("重新获取");
                            $getSecurityCode.click(function () {
                                sendSecurityCode();
                            });
                        }
                    }, 1000);
                }
            }
        });
    }
</script>
<!--底部 -->
[#include "/b2c/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
