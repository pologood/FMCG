<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${setting.siteName}-登录</title>
    <meta name="description" content="${setting.siteName}">
    <meta name="keywords" content="${setting.siteName}">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="${base}/resources/b2c/css/supplier.css">
    <link rel="stylesheet" href="${base}/resources/b2c/css/common.css">
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/supplier-index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
</head>
<style>
    .am-container {
        width: 1106px;
        _width: 1106px;
        position: relative;
    }

    .am-container > div {
        z-index: 10;
        position: relative;
    }
</style>
<body class="pc-bg">
<div class="top-content">
    <div class="wrap">欢迎来到${setting.siteName}系统平台</div>
</div>
<div class="am-g header-bg">
    <div class="am-container am-center" style="width:1106px;">
        <div class="am-fl">
        <!--    <img src="${base}/resources/b2c/images/PC-login_00.png" alt="${setting.siteName}-login" class="header-login0"> -->
            <img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}-login" class="header-login0">
        </div>
        <ul class="am-fr nav-entrance">
        [@navigation_list position = "loginMiddle"]
            [#list navigations as navigation]
                <li>
                    <a href="${navigation.url}"
                       [#if navigation.isBlankTarget]target="_blank"[/#if]>${navigation.name}</a>
                </li>
            [/#list]
        [/@navigation_list]
        </ul>
    </div>
</div>
<div class="am-g banner">
    <div class="am-container" style="width:1106px;">

        <div style="display: block;position: absolute;left: 0;top: 0; right: 0;bottom: 0;z-index: 1;">
        [@ad_position id=118  count=1/]
        </div>

        <!-- 登录 -->
        <div class="px am-fr" id="_signin">
            <div class="am-fl">
                    <span>
                        欢迎登录
                    </span>
            </div>
            <!--<div class="am-fr">
                    <span>
                        还没有账号？ <a href="javascript:Login('register');">立即注册</a>
                    </span>
            </div>-->
            <form action="${base}/b2c/supplier/login/submit.jhtml" method="post" id="loginForm">
                <div class="normalInput">
                    <div class="normalInput-bg1 bg"></div>
                    <input id="username" name="username" type="text" maxlength="11" placeholder="请输入手机号" class="">
                </div>
                <div class="normalInput">
                    <div class="normalInput-bg3 bg"></div>
                    <input id="password" name="password" type="password" maxlength="11" placeholder="请输入密码" class="">
                </div>
                <div class="rememberField">
                        <span class="checkboxPic check_chk am-fl" tabindex="-1" isshow="true">
                            <i class="i_icon"></i>
                        </span>
                    <label class="pointer am-fl">
                        记住密码
                    </label>
                    <div class="am-fr">
                        <a href="javascript:Login('modifyPass');">忘记密码？</a>
                    </div>
                </div>
                <a href="javascript:;" class="fullBtnBlue" onclick="$loginForm.submit();">登录</a>
            </form>
        </div>
        <!-- 注册 -->
        <div class="px am-fr hidden" id="_register">
            <div class="am-fl">
                    <span>
                        新用户注册
                    </span>
            </div>
            <div class="am-fr">
                    <span>
                         已有账号？ <a href="javascript:Login('signin');">马上登录</a>
                    </span>
            </div>
            <form id="registercallForm" action="register.jhtml" method="post">
                <div class="normalInput">
                    <div class="normalInput-bg1 bg"></div>
                    <input id="mobilecall" name="mobile" type="text" maxlength="11" placeholder="请输入手机号" class="">
                </div>
                <div class="normalInput" id="register_captcha">
                    <div class="normalInput-bg3 bg"></div>
                    <input type="text" id="captcha" name="captcha" maxlength="18" placeholder="验证码"
                           maxlength="10">
                    <div style="margin-top: 30px;">
                        <img id="captchaImage" class="captchaImage"
                             src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                    </div>
                </div>
                <div class="normalInput" id="cell_register_code">
                    <div class="normalInput-bg3 bg"></div>
                    <input id="authcode" name="authcode" type="text" maxlength="10" placeholder="请输入验证码" class="">
                    <a href="javascript:;" id="getSecurityCl" class="button-validation am-fr">获取验证码</a>
                </div>
                <div class="rememberField">
                [#--<span class="checkboxPic check_chk getcheck" tabindex="-1" isshow="true">--]
                            [#--<i class="i_icon"></i>--]
                        [#--</span>--]
                    [#--<label class="pointer">--]
                        [#--我已阅读并同意<a href="javascript:;" id="protocol">《店家助手服务协议》</a>--]
                    [#--</label>--]
                </div>
                <a href="javascript:;" class="fullBtnBlue" onclick="$registercallForm.submit();">注册</a>
                <!-- 测试时此处发生未知错误，怀疑onclick事件报错 -->
            </form>
        </div>
        <!-- 忘记密码 -->
        <div class="px am-fr hidden" id="_modifyPass">
            <div class="am-fl">
                    <span>
                        找回密码
                    </span>
            </div>
            <div class="am-fr">
                        <span>
                             已有账号？ <a href="javascript:Login('signin');">马上登录</a>
                        </span>
            </div>
            <form id="findForm" action="" method="post">
                <div class="normalInput">
                    <div class="bg normalInput-bg1"></div>
                    <input id="mobileNum" name="mobile" type="text" maxlength="11" placeholder="请输入手机号" class="">
                </div>
                <div class="normalInput" id="register_captcha">
                    <div class="normalInput-bg3 bg"></div>
                    <input type="text" id="captcha" name="captcha" maxlength="18" placeholder="验证码"
                           maxlength="10">
                    <div style="margin-top: 30px;">
                        <img id="captchaImage" class="captchaImage"
                             src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                    </div>
                </div>

                <div class="normalInput">
                    <div class="bg normalInput-bg3"></div>
                    <input id="securityCode" name="securityCode" type="text" maxlength="10" placeholder="请输入验证码"
                           class="">
                    <a href="javascript:;" id="identifying_code" class="button-validation am-fr">获取验证码</a>
                </div>
                <div class="rememberField"></div>
                <a href="javascript:;" class="fullBtnBlue" onclick="$findForm.submit();">下一步</a>
                <!-- 验证码显示不正确，需跟后台沟通 -->
            </form>
        </div>
        <!--重置密码-->
        <div class="px am-fr hidden" id="_resetPassword">
            <div class="am-fl">
                    <span>
                        重置密码
                    </span>
            </div>
            <div class="am-fr">
                    <span>
                        已有账号？ <a href="javascript:Login('signin');">马上登录</a>
                    </span>
            </div>
            <form action="${base}/b2c/supplier/resetPassword.jhtml" method="post" id="resetPwdForm">
                <div class="normalInput">
                    <div class="normalInput-bg1 bg"></div>
                    <input id="mobileRes" name="mobile" type="text" maxlength="11" placeholder="请输入手机号" readonly
                           class="">
                </div>
                <div class="normalInput">
                    <div class="normalInput-bg3 bg"></div>
                    <input id="newpassword" name="password" type="password" maxlength="11" placeholder="请输入新密码"
                           autocomplete="off" class="">
                </div>
                <div class="normalInput">
                    <div class="normalInput-bg3 bg"></div>
                    <input id="repassword" name="repassword" type="password" maxlength="11" placeholder="请输入确认密码"
                           class="">
                </div>
                <a href="javascript:;" class="fullBtnBlue" onclick="$resetPwdForm.submit();">提交</a>
            </form>
        </div>
    </div>
</div>
[#if versionType==0]
<div class="am-g" style="background-color:#fff;">
    <div class="am-container" style="width:1106px;">
        <ul class="th-convenient">
            <li style="margin-left:0;">
                <h1>门店体验</h1>
                <img src="${base}/resources/b2c/images/PC-login_10.png" alt="${setting.siteName}">
                <p>地理位置精准匹配</p>
                <p>商家活动一览无余</p>
            </li>
            <li>
                <h1>消费体验</h1>
                <img src="${base}/resources/b2c/images/PC-login_08.png" alt="${setting.siteName}">
                <p>便捷开放消费体验</p>
                <p>多样支付不用排队</p>
            </li>
            <li>
                <h1>社交体验</h1>
                <img src="${base}/resources/b2c/images/PC-login_11.png" alt="${setting.siteName}">
                <p>促销活动及时分享</p>
                <p>完美对接社区商圈</p>
            </li>
            <li style="margin-right:0;">
                <h1>服务体验</h1>
                <img src="${base}/resources/b2c/images/PC-login_12.png" alt="${setting.siteName}">
                <p>售前售后卓越享受</p>
                <p>精致营销提升粘性</p>
            </li>
        </ul>
    </div>
</div>
[/#if]
<div class="bt-content">
    <div class="wrap">
        <div class="top">
            <ul>
            [@navigation_list position = "bottom"]
                [#list navigations as navigation]
                    <li>
                        <a href="${navigation.url}" target="_blank">${navigation.name}</a>
                    </li>
                [/#list]
            [/@navigation_list]
            </ul>
            <span>客服热线</span>
        </div>
        <ul class="left">
            <li>关注我们：</li>
            <li><img src="${base}/upload/images/jdh_qr.jpg" alt=""></li>
        </ul>
        <div class="right">
            <h3>${setting.phone}</h3>
            <span>工作时间（周一至周五）   8：00-12：00，14：00-18：00</span>
        </div>
    </div>
</div>
<div class="foote">
    <ul>
        <li class="text">
            CopyRight © 2010-2015 <a href="${setting.siteUrl}">${setting.siteName}</a> All Rights Reserved
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;      ${setting.certtext}
            &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp; 技术支持：安徽威思通电子商务有限公司
        </li>
    </ul>
</div>
</body>
<script type="text/javascript">

    function Login(type) {
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
            $("#captchaImage1").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}")
        } else if (type == "modifyPass") {
            $("#_signin").addClass("hidden");
            $("#_register").addClass("hidden");
            $("#_modifyPass").removeClass("hidden");
            $("#_resetPassword").addClass("hidden");
            $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}")
        } else if (type == "resetPass") {
            $("#_signin").addClass("hidden");
            $("#_register").addClass("hidden");
            $("#_modifyPass").addClass("hidden");
            $("#_resetPassword").removeClass("hidden");
        }
    }

    var $loginForm = $("#loginForm");
    $(function () {
        $loginForm.validate({
            submitHandler: function (form) {
                if ($("#username").val().trim() == "" || !/^1\d{10}$/.test($("#username").val().trim())) {
                    $.message("error", "请输入正确的手机号");
                    return;
                }
                if ($("#password").val().trim() == "") {
                    $.message("error", "请输入密码");
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
                        var enPassword = hex2b64(rsaKey.encrypt($("#password").val()));
                        $.ajax({
                            url: $loginForm.attr("action"),
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
                                        location.href = "${base}/b2c/member/supplier/index.jhtml";
                                    [/#if]
                                    }, 1500);
                                } else {
                                    if (message.type == "warn") {
                                        $.message(message);
                                    } else {
                                        $.message(message);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    });

    <!-- 注册 -->
    var $registercallForm = $("#registercallForm");
    var $getSecurityCl = $("#getSecurityCl");
    var $protocol = $("#protocol");
    var $mobilecall = $("#mobilecall");

    $(function () {
        $registercallForm.validate({
            submitHandler: function (form) {
                if ($("#mobilecall").val().trim() == "" || !/^1\d{10}$/.test($("#mobilecall").val().trim())) {
                    $.message("error", "请输入正确的手机号");
                    return;
                }
                if ($("#authcode").val().trim() == "") {
                    $.message("error", "请输入验证码");
                    return;
                }
//                if ($(".getcheck").attr("isshow") == "true") {
//                    $.message("error", "未同意店家助手服务协议");
//                    return;
//                }
                $.ajax({
                    url: $registercallForm.attr('action'),
                    type: "POST",
                    data: $registercallForm.serialize(),
                    dataType: "json",
                    success: function (data) {
                        $.message(data);
                        if (data.type == 'success') {
                            window.setTimeout(function () {
                                window.location.href = "login.jhtml";
                            }, 1000);
                        }
                    }
                });
            }
        });

        $getSecurityCl.click(function () {
            getCode();
        });

        $("#captchaImage,#changeCaptcha").click(function () {
            $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
        });

    });


    function getCode() {
        if ($("#mobilecall").val().trim() == "" || !/^1\d{10}$/.test($("#mobilecall").val().trim())) {
            $.message("error", "请先输入正确的手机号");
            return;
        }
        if ($("#captcha").val().trim() == '') {
            $.message("error", "请输入图片验证码");
            return;
        }
        $.ajax({
            type: 'post',
            url: 'send_security_code.jhtml',
            data: {
                mobile: $("#mobilecall").val(),
                captchaId:"${captchaId}",
                captcha:$("#captcha").val()
            },
            dataType: 'json',
            success: function (data) {
                $.message(data);
                if (data.type == "success") {
                    var count = 60;
                    $getSecurityCl.unbind("click");
                    var t = window.setInterval(function () {
                        count--;
                        $getSecurityCl.text(count + "秒后重新获取");
                        if (count == 0) {
                            window.clearInterval(t);
                            $getSecurityCl.text("重新获取");
                            $getSecurityCl.click(function () {
                                getCode();
                            });
                        }
                    }, 1000);
                }else{
                    $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
                }
            }
        });
    }

    //服务协议
    $protocol.on("click", function () {
        protocolDialog();
    });

    //服务协议
    function protocolDialog() {
        $.dialog({
            type: "warn",
            title: "请仔细阅读《服务协议》",
        [@compress single_line = true]
            content: "<div><p> 尊敬的用户欢迎您注册成为本网站会员。请用户仔细阅读以下全部内容。如用户不同意本服务条款任意内容，请不要注册或使用本网站服务。如用户通过本网站注册程序，即表示用户与本网站已达成协议，自愿接受本服务条款的所有内容。此后，用户不得以未阅读本服务条款内容作任何形式的抗辩。<\/p> <p>一、本站服务条款的确认和接纳<br \/>本网站涉及的各项服务的所有权和运作权归本网站所有。本网站所提供的服务必须按照其发布的服务条款和操作规则严格执行。本服务条款的效力范围及于本网站的一切产品和服务，用户在享受本网站的任何服务时，应当受本服务条款的约束。<\/p> <p>二、服务简介<br \/>本网站运用自己的操作系统通过国际互联网络为用户提供各项服务。用户必须:  1. 提供设备，如个人电脑、手机或其他上网设备。 2. 个人上网和支付与此服务有关的费用。<\/p> <p>三、用户在不得在本网站上发布下列违法信息<br \/>1. 反对宪法所确定的基本原则的； 2. 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的； 3. 损害国家荣誉和利益的； 4. 煽动民族仇恨、民族歧视，破坏民族团结的； 5. 破坏国家宗教政策，宣扬邪教和封建迷信的； 6. 散布谣言，扰乱社会秩序，破坏社会稳定的； 7. 散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的； 8. 侮辱或者诽谤他人，侵害他人合法权益的； 9. 含有法律、行政法规禁止的其他内容的。<\/p> <p>四、有关个人资料<br \/>用户同意:  1. 提供及时、详尽及准确的个人资料。 2. 同意接收来自本网站的信息。 3. 不断更新注册资料，符合及时、详尽准确的要求。所有原始键入的资料将引用为注册资料。 4. 本网站不公开用户的姓名、地址、电子邮箱和笔名。除以下情况外:  a) 用户授权本站透露这些信息。 b) 相应的法律及程序要求本站提供用户的个人资料。<\/p> <p>五、服务条款的修改<br \/>本网站有权在必要时修改服务条款，一旦条款及服务内容产生变动，本网站将会在重要页面上提示修改内容。如果不同意所改动的内容，用户可以主动取消获得的本网站信息服务。如果用户继续享用本网站信息服务，则视为接受服务条款的变动。<\/p> <p>六、用户隐私制度<br \/>尊重用户个人隐私是本网站的一项基本政策。所以，本网站一定不会在未经合法用户授权时公开、编辑或透露其注册资料及保存在本网站中的非公开内容，除非有法律许可要求或本网站在诚信的基础上认为透露这些信息在以下四种情况是必要的:  1. 遵守有关法律规定，遵从本网站合法服务程序。 2. 保持维护本网站的商标所有权。 3. 在紧急情况下竭力维护用户个人和社会大众的隐私安全。 4. 符合其他相关的要求。<\/p> <p>七、用户的帐号、密码和安全性<br \/>用户一旦注册成功，将获得一个密码和用户名。用户需谨慎合理的保存、使用用户名和密码。如果你不保管好自己的帐号和密码安全，将负全部责任。另外，每个用户都要对其帐户中的所有活动和事件负全责。你可随时根据指示改变你的密码。用户若发现任何非法使用用户帐号或存在安全漏洞的情况，请立即通告本网站。   八、 拒绝提供担保 用户明确同意信息服务的使用由用户个人承担风险。本网站不担保服务不会受中断，对服务的及时性，安全性，出错发生都不作担保，但会在能力范围内，避免出错。<\/p> <p>九、有限责任<br \/>如因不可抗力或其它本站无法控制的原因使本站销售系统崩溃或无法正常使用导致网上交易无法完成或丢失有关的信息、记录等本站会尽可能合理地协助处理善后事宜，并努力使客户免受经济损失，同时会尽量避免这种损害的发生。<\/p> <p>十、用户信息的储存和限制<br\/>本站有判定用户的行为是否符合国家法律法规规定及本站服务条款权利，如果用户违背本网站服务条款的规定，本网站有权中断对其提供服务的权利。<\/p> <p>十一、用户管理<br \/>用户单独承担发布内容的责任。用户对服务的使用是根据所有适用于本站的国家法律、地方法律和国际法律标准的。用户必须遵循:  1. 使用网络服务不作非法用途。 2. 不干扰或混乱网络服务。 3. 遵守所有使用网络服务的网络协议、规定、程序和惯例。 用户须承诺不传输任何非法的、骚扰性的、中伤他人的、辱骂性的、恐性的、伤害性的、庸俗的，淫秽等信息资料。另外，用户也不能传输何教唆他人构成犯罪行为的资料；不能传输助长国内不利条件和涉及国家安全的资料；不能传输任何不符合当地法规、国家法律和国际法律的资料。未经许可而非法进入其它电脑系统是禁止的。 若用户的行为不符合以上提到的服务条款，本站将作出独立判断立即取消用户服务帐号。用户需对自己在网上的行为承担法律责任。用户若在本站上散布和传播反动、色情或其它违反国家法律的信息，本站的系统记录有可能作为用户违反法律的证据。<\/p> <p>十二、通告<br \/>所有发给用户的通告都可通过重要页面的公告或电子邮件或常规的信件传送。服务条款的修改、服务变更、或其它重要事件的通告都会以此形式进行。<\/p> <p>十三、信息内容的所有权<br \/>本网站定义的信息内容包括: 文字、软件、声音、相片、录象、图表；在广告中全部内容；本网站为用户提供的其它信息。所有这些内容受版权、商标、标签和其它财产所有权法律的保护。所以，用户只能在本网站和广告商授权下才能使用这些内容，而不能擅自复制、再造这些内容、或创造与内容有关的派生产品。本站所有的文章版权归原文作者和本站共同所有，任何人需要转载本站的文章，必须征得原文作者或本站授权。<\/p> <p>十四、法律<br \/>本协议的订立、执行和解释及争议的解决均应适用中华人民共和国的法律。用户和本网站一致同意服从本网站所在地有管辖权的法院管辖。如发生本网站服务条款与中华人民共和国法律相抵触时，则这些条款将完全按法律规定重新解释，而其它条款则依旧保持对用户的约束力。<\/p><\/div >",
        [/@compress]
            width: 650,
            ok: "同意协议",
            cancel: null,
            onOk: function () {
                $(".getcheck").attr("isshow", "false");
            }
        });
        $(".dialogwarnIcon").css({
            background: "none",
            padding: "0",
            margin: "5px 10px 0px 15px",
            height: "300px",
            overflow: "auto"
        });
    }
    <!-- 找回密码 -->
    var $mobileNum = $("#mobileNum");
    var $findForm = $("#findForm");
    var $identifying_code = $("#identifying_code");
    var $resetPwdForm = $("#resetPwdForm");
    $(function () {
        $findForm.validate({
            submitHandler: function (form) {
                if ($("#mobileNum").val().trim() == "" || !/^1\d{10}$/.test($("#mobileNum").val().trim())) {
                    $.message("error", "输入正确的手机号");
                    return;
                }
                if ($("#securityCode").val().trim() == "") {
                    $.message("error", "请输入验证码");
                    return;
                }
                $.ajax({
                    url: 'check_security_code.jhtml',
                    type: "POST",
                    data: $findForm.serialize(),
                    dataType: "json",
                    success: function (data) {
                        if (data.type == 'success') {
                            $("#mobileRes").val($("#mobileNum").val());
                            Login("resetPass");
                        } else {
                            $.message(data);
                        }
                    }
                });
            }
        });

        $identifying_code.click(function () {
            getAuthcode();
        });

        //重置密码
        $resetPwdForm.validate({
            submitHandler: function (form) {
                if ($("#newpassword").val().trim() == "") {
                    $.message("error", "请输入新密码");
                    return;
                }
                if ($("#repassword").val().trim() == "") {
                    $.message("error", "请输入确认密码");
                    return;
                }
                if ($("#newpassword").val().trim() != $("#repassword").val().trim()) {
                    $.message("error", "两次输入密码不一致");
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
                        var enPassword = hex2b64(rsaKey.encrypt($("#newpassword").val()));
                        $.ajax({
                            url: 'resetPassword.jhtml',
                            type: "POST",
                            data: {
                                mobile: $("#mobileRes").val(),
                                enPassword: enPassword
                            },
                            dataType: "json",
                            success: function (data) {
                                $.message(data);
                                if (data.type == 'success') {
                                    window.setTimeout(function () {
                                        window.location.reload(true);
                                    }, 1000);
                                }
                            }
                        });
                    }
                });
            }
        });
    });

    function getAuthcode() {
        if ($("#mobileNum").val().trim() == "" || !/^1\d{10}$/.test($("#mobileNum").val().trim())) {
            $.message("error", "请先输入手机号");
            return;
        }
        $.ajax({
            type: 'post',
            url: 'send_security_code.jhtml',
            data: {
                mobile: $("#mobileNum").val(),
                type: "resetPwd"
            },
            dataType: 'json',
            success: function (data) {
                $.message(data);
                if (data.type == "success") {
                    var count = 60;
                    $identifying_code.unbind("click");
                    var t = window.setInterval(function () {
                        count--;
                        $identifying_code.text(count + "秒后重新获取");
                        if (count == 0) {
                            window.clearInterval(t);
                            $identifying_code.text("重新获取");
                            $identifying_code.click(function () {
                                getAuthcode();
                            });
                        }
                    }, 1000);
                }
            }
        });
    }
</script>
</html>
