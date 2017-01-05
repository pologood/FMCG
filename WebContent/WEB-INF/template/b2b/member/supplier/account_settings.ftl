<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-账号设置</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery.validate.js"></script>

    <script type="text/javascript" src="${base}/resources/b2b/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>

    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.js"></script>


    <script type="text/javascript" src="${base}/resources/b2b/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>

    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
</head>
<body class="bg-base">
[#include "/b2b/include/supplier_left.ftl"]

<div class="f-left rt">
[#include "/b2b/include/supplier_header.ftl"]

    <div class="breadcrumbs">
        <ul class="breadcrumb">
            <p>当前位置：</p>
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>账号设置</li>
        </ul>
    </div>
[#include "/b2b/include/supplier_top.ftl"]
    <div class="tb-container" style="border:none;">
        [#--<div class="js_bill" style="border:none;">--]
            [#--<!-- 为表单模块 -->--]
            [#--<span>账单记录</span>--]
        [#--</div>--]
        [#--<form action="#" class="fm">--]
            [#--<table class="fm-container">--]
                [#--<tr>--]
                    [#--<th>真实姓名</th>--]
                    [#--<th width="40%">刘磊 | 340101********4521</th>--]
                    [#--<th width="30%">已绑定</th>--]
                [#--</tr>--]
                [#--<tr>--]
                    [#--<th>手机号码</th>--]
                    [#--<th width="40%">13739240506</th>--]
                    [#--<th width="30%">已绑定</th>--]
                [#--</tr>--]
                [#--<tr>--]
                    [#--<th>银行卡</th>--]
                    [#--<th width="40%"><img src="" width="" height=""/></th>--]
                    [#--<th width="30%">已绑定</th>--]
                [#--</tr>--]
                [#--<tr>--]
                    [#--<th>登录密码</th>--]
                    [#--<th width="40%">登录${setting.siteName}供应商账户时需输入的密码</th>--]
                    [#--<th width="30%"><a href="#">已修改</a></th>--]
                [#--</tr>--]
                [#--<tr>--]
                    [#--<th>注册时间</th>--]
                    [#--<th width="40%">2010年7月23日</th>--]
                    [#--<th width="30%"></th>--]
                [#--</tr>--]
            [#--</table>--]
        [#--</form>--]
        <!--安全设置-->
        <div class="my-settings">
            <h1>账户设置</h1>
            <div class="basic-info">
                <form id="basicInfoForm" action="${base}/b2b/member/safe/save.jhtml" method="post">
                    <h2>您的基本信息<a class="save" href="javascript:;" onclick="basicInfoSave()">保存</a></h2>
                    <dl>
                        <dt>用户名：</dt>
                        <dd>
                            <input class="text" name="username" type="text" value="${(member.username)!}">
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
                        <dt>手机号码：</dt>
                        <dd>
                            <input class="text" name="mobile" type="text" value="${(member.mobile)!}">
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
                        <form id="identityForm" action="${base}/b2b/member/safe/identity.jhtml" method="post">
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
                                <dt></dt>
                                <dd>
                                    <a class="save-btn" href="javascript:;" onclick="idcardSave()">保存</a>
                                </dd>
                            </dl>
                        [#else]
                            <dl>
                                <dt>姓名：</dt>
                                <dd>
                                    <input class="text" name="name" type="text" value="${(member.name)!}" readonly>
                                </dd>
                                <dt>身份证号码：</dt>
                                <dd>
                                    <input class="text" name="no" type="text" value="${(member.idcard.no)!}" readonly>
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
                            <span>[#if member.password?trim!=""&&member.password!=null]已设置[#else]未设置[/#if]</span>
                            <span>登录密码</span>
                            <span class="title">安全性高的密码可以使账号更安全。建议您定期更换密码，且设置一个包含数字和字母，并长度超过6位以上的密码。</span>
                            <a class="f-right view" href="javascript:;">查看</a>
                            <a style="display: none;" class="f-right hide" href="javascript:;">收起</a>
                        </div>
                    </div>
                    <div class="li-main display-n">
                        <form action="" method="post" id="passwordForm">
                            <input type="hidden" id="captchaId" name="captchaId" value="${captchaId}"/>
                            <dl>
                                <dt>现有密码：</dt>
                                <dd>
                                    <input class="text" id="curPassword" name="curPassword" type="password" value="">
                                </dd>
                                <dt>输入新密码：</dt>
                                <dd>
                                    <input class="text" id="newPassword" name="newPassword" type="password" value="">
                                </dd>
                                <dt>重新输入新密码：</dt>
                                <dd>
                                    <input class="text" id="rePassword" name="rePassword" type="password" value="">
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
                                    <a class="save-btn" href="javascript:;" onclick="$passwordForm.submit();">修改</a>
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
                [#--<div class="li" id="bindMobile">--]
                    [#--<div class="li-hd">--]
                        [#--<i>[#if member.bindMobile=='binded']✔[#else]？[/#if]</i>--]
                        [#--<div class="hint">--]
                            [#--<span>[#if member.bindMobile=='none']未绑定[#elseif member.bindMobile=='binded']--]
                                [#--已绑定[#elseif member.bindMobile=='unbind']已解绑[/#if]</span>--]
                            [#--<span>绑定手机</span>--]
                            [#--<span class="title">绑定手机后，您即可享受丰富的手机服务，如手机找回密码等。</span>--]
                            [#--<a class="f-right view" href="javascript:;">查看</a>--]
                            [#--<a style="display: none;" class="f-right hide" href="javascript:;">收起</a>--]
                        [#--</div>--]
                    [#--</div>--]
                    [#--<div class="li-main display-n bindmobile">--]
                        [#--<form method="post" action="" id="mobileForm">--]
                            [#--<dl id="mobileDl">--]
                                [#--<dt>手机号码：</dt>--]
                                [#--<dd>--]
                                    [#--<input class="text" name="mobile" type="text"--]
                                           [#--[#if member.bindMobile=='binded']value="${member.mobile!}" readonly[/#if]>--]
                                [#--</dd>--]
                                [#--<dt></dt>--]
                                [#--<dd>--]
                                    [#--<a class="save-btn" href="javascript:;"--]
                                       [#--onclick="$mobileForm.submit();">[#if member.bindMobile=='binded']解绑[#else]--]
                                        [#--绑定[/#if]</a>--]
                                [#--</dd>--]
                            [#--</dl>--]
                        [#--</form>--]
                        [#--<form action="" method="post" id="bindMobileForm">--]
                            [#--<dl class="display-n" id="securityCodeDl">--]
                                [#--<dt>手机验证码：</dt>--]
                                [#--<dd>--]
                                    [#--<input class="text" name="securityCode" type="text" value="">--]
                                    [#--<a style="display:inline-block;width: 110px;cursor: pointer;line-height: 28px;background-color: rgb(51,143,255);color:white;height: 28px;text-align: center;"--]
                                       [#--id="getSecurityCode">60秒后重新获取</a>--]
                                [#--</dd>--]
                                [#--<dt></dt>--]
                                [#--<dd>--]
                                    [#--<a class="save-btn" href="javascript:;" onclick="$bindMobileForm.submit();">确认</a>--]
                                [#--</dd>--]
                            [#--</dl>--]
                        [#--</form>--]
                    [#--</div>--]
                [#--</div>--]
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
</body>
<script type="text/javascript">
    var $basicInfoForm = $("#basicInfoForm");
    var $identityForm = $("#identityForm");
    var $passwordForm = $("#passwordForm");
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
        $("#captchaImage,#changeCaptcha").click(function () {
            $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
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
            }
        });
    }
    function idcardSave() {
        $.ajax({
            type: 'post',
            url: $identityForm.attr("action"),
            data: $identityForm.serialize(),
            dataType: 'json',
            success: function (data) {
                $.message(data);
                if (data.type == "success") {
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 600);
                }
            }
        });
    }
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
                        url: '${base}/b2b/member/safe/passwordUpdate.jhtml',
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
    $emailForm.validate({
        rules: {
            email: {
                required: true
            }
        },
        submitHandler: function (form) {
            $.ajax({
                type: 'post',
                url: '${base}/b2b/member/safe/send_email.jhtml',
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
                url: '${base}/b2b/member/safe/mobileupdate.jhtml',
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
            url: '${base}/b2b/member/safe/send_security_code.jhtml',
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
</html>
