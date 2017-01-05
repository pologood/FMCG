<!doctype html>
<html>
<head>
[#include "/wap/include/resource.ftl"]
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/handlebars.min.js"></script>
    <script src="${base}/resources/common/js/jquery.validate.js"></script>
    <script src="${base}/resources/common/js/jquery-form.js"></script>
    <script src="${base}/resources/common/js/wap-upload.js"></script>
    <script src="${base}/resources/wap/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/js/ajaxfileupload.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <title>个人资料</title>
    <script type="text/x-handlebars-template" id="wap-member-info-name">
        <form id="submitForm" class="am-form am-form-horizontal" action="${base}/wap/member/updateInfo.jhtml"
              method="post">
            <input type="hidden" name="type" value="name">
            <div class="am-form-group" style=" background: #fff; ">
                <label for="name" class="am-form-label"
                       style="float: left;line-height: 35px;padding-top: 0px;padding-right: 0.5em;">姓名:</label>
                <input style="float: left;width: auto;border: none;" type="text" name="name" id="name" value="{{name}}">
            </div>
            <div class="am-form-group">
                <button class="am-btn am-btn-danger am-btn-block">提交</button>
            </div>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-sex">
        <form class="am-form am-form-horizontal" id="submitForm" action="${base}/wap/member/updateInfo.jhtml"
              method="post">
            <input type="hidden" name="type" value="sex">
            <div class="am-form-group">
                <label class="am-u-sm-3 am-form-label" style="padding-top: 0px;" for="name">性别:</label>
                <div>
                    <input type="radio" name="sex" value="0" {{#expression sex '=='0}}checked="checked"{{/expression}}>男
                    <input type="radio" name="sex" value="1" {{#expression sex '=='1}}checked="checked"{{/expression}}>女
                </div>
            </div>
            <div class="am-form-group">
                <button class="am-btn am-btn-danger am-btn-block">提交</button>
            </div>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-area">
        <form class="am-form am-form-horizontal am-padding-horizontal-sm" id="submitForm"
              action="${base}/wap/member/updateInfo.jhtml" method="post">
            <input type="hidden" name="type" value="area">
            <div class="am-form-group">
                <input id="areaId" type="hidden" name="areaId" value="{{areaId}}" treePath="{{treePath}}">
            </div>
            <div class="am-form-group">
                <button class="am-btn am-btn-danger am-btn-block">提交</button>
            </div>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-auth">
        <form class="am-form am-padding-horizontal-sm" id="submitForm" action="${base}/wap/member/apply.jhtml"
              method="post" enctype="multipart/form-data">
            <input type="hidden" id="frontUrl" name="frontUrl"/>
            <input type="hidden" id="backUrl" name="backUrl"/>
            <label class=" am-form-label">身份证正反面:</label>
            <div class="id_pic_list">
                <div class="id_pic pic1">
                    <img id="selectFront">
                    <input type="file" id="pathFront" name="pathFront"
                           onchange="changeFile('pathFront','selectFront','frontUrl')">
                </div>
                <div class="id_pic pic2">
                    <img id="selectBack">
                    <input type="file" id="pathBack" name="pathBack"
                           onchange="changeFile('pathBack','selectBack','backUrl')">
                </div>
            </div>
            <div class="am-form-group" style="margin-top: 99px;">
                真实姓名:
                <input type="text" id="name" name="name" class="am-form-field" placeholder="点此输入您的真实姓名">
            </div>

            <div class="am-form-group">
                身份证号:
                <input type="text" id="idCard" name="idCard" class="am-form-field" placeholder="点此输入身份证号">
            </div>
            <div class="am-form-group">
                <button class="am-btn am-btn-danger am-btn-block">提交</button>
            </div>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-editpassword">
        <form class="am-form-inline am-padding-horizontal-sm" id="submitForm"
              action="${base}/wap/member/updatePass.jhtml" method="post">
            <div class="am-form-group">
                原密码:
                <input type="password" id="oldPass" name="oldPass" class="am-form-field" placeholder="点击输入您的原密码">
            </div>

            <div class="am-form-group">
                新密码:
                <input type="password" id="newPass" name="newPass" class="am-form-field" placeholder="点击输入您的新密码">
            </div>
            <div class="am-form-group">
                确认密码:
                <input type="password" name="reNewPass" class="am-form-field" placeholder="点击重复您的新密码">
            </div>
            <button class="am-btn am-btn-block am-btn-danger">提交</button>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-editpaypwd">
        <form class="am-form-inline am-padding-horizontal-sm" id="submitForm"
              action="${base}/wap/member/updatePaymentPass.jhtml" method="post">
            <div class="am-form-group">
                原密码:
                <input type="password" id="oldPass" name="oldPass" class="am-form-field" placeholder="点击输入您的原密码">
            </div>

            <div class="am-form-group">
                新密码:
                <input type="password" id="newPass" name="newPass" class="am-form-field" placeholder="点击输入您的新密码">
            </div>
            <div class="am-form-group">
                确认密码:
                <input type="password" name="reNewPass" class="am-form-field" placeholder="点击重复您的新密码">
            </div>
            <button class="am-btn am-btn-block am-btn-danger">提交</button>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-findpaypwd">
        <form class="am-form-inline am-padding-horizontal-sm" id="submitForm"
              action="${base}/wap/member/retrievePass.jhtml" method="post">
            <div class="am-form-group" style="display: flex;">
                <input style="width: 50%;" type="text" name="securityCode" id="securityCode" class="am-form-field"
                       placeholder="请获取验证码后输入">
                <a style="width: 50%;" class="am-btn am-btn-danger" id="getCode">点击获取</a>
            </div>
            <div class="am-form-group">
                新密码:
                <input type="password" id="newPass" name="newPass" class="am-form-field" placeholder="点击输入您的新密码">
            </div>
            <div class="am-form-group">
                确认密码:
                <input type="password" name="reNewPass" class="am-form-field" placeholder="点击重复您的新密码">
            </div>
            <button class="am-btn am-btn-block am-btn-danger">提交</button>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-bindmobile">
        <form class="am-form-inline am-padding-horizontal-sm" id="submitForm"
              action="${base}/weixin/member/bind_save.jhtml" method="post">
            <input type="hidden" name="type" value="mobile">
            <div class="am-form-group">
                手机号:
                <input type="text" id="mobile" name="mobile" class="am-form-field" placeholder="点击输入您要绑定的手机号">
            </div>
            <div class="am-form-group" style="display: flex;">
                <input style="width: 50%;" type="text" name="securityCode" id="securityCode" class="am-form-field"
                       placeholder="请获取验证码后输入">
                <a style="width: 50%;" class="am-btn am-btn-danger" bind="mobile" id="getCode">点击获取</a>
            </div>
            <button class="am-btn am-btn-block am-btn-danger">提交</button>
        </form>
    </script>
    <script type="text/x-handlebars-template" id="wap-member-info-bindEmail">
        <form class="am-form-inline am-padding-horizontal-sm" id="submitForm"
              action="${base}/weixin/member/bind_save.jhtml" method="post">
            <input type="hidden" name="type" value="email">
            <div class="am-form-group">
                邮箱:
                <input type="text" id="email" name="email" class="am-form-field" placeholder="点击输入您要绑定的邮箱">
            </div>
            <div class="am-form-group" style="display: flex;">
                <input style="width: 50%;" type="text" name="securityCode" id="securityCode" class="am-form-field"
                       placeholder="请获取验证码后输入">
                <a style="width: 50%;" class="am-btn am-btn-danger" bind="email" id="getCode">点击获取</a>
            </div>
            <button class="am-btn am-btn-block am-btn-danger">提交</button>
        </form>
    </script>
    <style>

    </style>
    <script>
        function changeFile(id, obj, url) {
            if ($("#" + id).val().length > 0) {
                $.ajaxFileUpload({
                    url: 'uploadFile.jhtml',
                    secureuri: false,
                    fileElementId: id, //上传控件ID
                    dataType: 'jsonp',
                    success: function (data, status) {
                        var src = data.replace(/\"/g, "");
                        alert("上传成功");
                        $("#" + obj).attr("src", src.toString());
                        $("#" + url).val(src);
                    },
                    error: function (data, status) {
                        alert("上传失败");
                    }
                });
            }
        }
        $(function () {

            setTimeout(function () {
                init();
            }, 200);
            $('[data-toggle="modalhelper"]').on("click", function (h) {
                var t = $(this).attr("data-type");
                var data = $(this).attr("data-options");
                var g = $(this).attr("data-title");
                var e = $("#modal-helper");
                e.find(".am-popup-title").text(g);
                e.find(".am-popup-bd").text("");
                Handlebars.registerHelper("expression", function () {
                    var exps = [];
                    try {
//最后一个参数作为展示内容，也就是平时的options。不作为逻辑表达式部分
                        var arg_len = arguments.length;
                        var len = arg_len - 1;
                        for (var j = 0; j < len; j++) {
                            exps.push(arguments[j]);
                        }
                        var result = eval(exps.join(' '));
                        if (result) {
                            return arguments[len].fn(this);
                        } else {
                            return arguments[len].inverse(this);
                        }
                    } catch (e) {
                        throw new Error('Handlerbars Helper “expression” can not deal with wrong expression:' + exps.join(' ') + ".");
                    }
                });
                compiler = Handlebars.compile($("#wap-member-" + t).html());
                e.find(".am-popup-bd").html(compiler(eval('(' + data + ')')));
                var rules = {};
                var messages = {};
                if (t == "info-editpassword" || t == "info-editpaypwd") {
                    rules = {
                        oldPass: "required",
                        type: "required",
                        newPass: "required",
                        reNewPass: {
                            required: true,
                            equalTo: "#newPass"
                        }
                    };
                    messages = {
                        oldPass: "原始密码必填",
                        newPass: "新密码必填",
                        reNewPass: {
                            required: "",
                            equalTo: "两次输入密码不一致"
                        }
                    };
                    $("#submitForm").validate({
                        rules: rules,
                        messages: messages,
                        submitHandler: function () {
                            $.ajax({
                                url: "${base}/common/public_key.jhtml",
                                type: "POST",
                                data: {local: true},
                                dataType: "json",
                                cache: false,
                                success: function (data1) {
                                    var rsaKey = new RSAKey();
                                    rsaKey.setPublic(b64tohex(data1.modulus), b64tohex(data1.exponent));
                                    $("#newPass").val(hex2b64(rsaKey.encrypt($("#newPass").val())));
                                    $("#oldPass").val(hex2b64(rsaKey.encrypt($("#oldPass").val())));
                                    $("#submitForm").ajaxSubmit(function (message) {
                                        invokTips(message.type, message.content);
                                        setTimeout(function () {
                                            location.reload(true);
                                        }, 1000);
                                    });
                                }
                            });
                        }
                    });
                } else if (t == "info-findpaypwd") {
                    rules = {
                        securityCode: "required",
                        newPass: "required",
                        reNewPass: {
                            required: true,
                            equalTo: "#newPass"
                        }
                    };
                    messages = {
                        securityCode: "验证码必填",
                        newPass: "新密码必填",
                        reNewPass: {
                            required: "",
                            equalTo: "两次输入密码不一致"
                        }
                    };
                    $("#submitForm").validate({
                        rules: rules,
                        messages: messages,
                        submitHandler: function () {
                            $.ajax({
                                url: "${base}/common/public_key.jhtml",
                                type: "POST",
                                data: {local: true},
                                dataType: "json",
                                cache: false,
                                success: function (data1) {
                                    var rsaKey = new RSAKey();
                                    rsaKey.setPublic(b64tohex(data1.modulus), b64tohex(data1.exponent));
                                    $("#newPass").val(hex2b64(rsaKey.encrypt($("#newPass").val())));
                                    $("#submitForm").ajaxSubmit(function (message) {
                                        invokTips(message.type, message.content);
                                        setTimeout(function () {
                                            location.reload(true);
                                        }, 1000);
                                    });
                                }
                            });
                        }
                    });
                } else {
                    if (t == "info-area") {
                        var $areaId = $("#areaId");
                        $areaId.lSelect({
                            url: "${base}/common/area.jhtml"
                        });
                        rules = {areaId: "required", type: "required"};
                        messages = {areaId: "区域必填"}
                    } else if (t == "info-name") {
                        rules = {name: "required", type: "required"};
                        messages = {name: "姓名必填"}
                    } else if (t == "info-sex") {
                        rules = {sex: "required", type: "required"};
                        messages = {name: "性别必填"}
                    } else if (t == "info-bindmobile") {
                        rules = {mobile: "required", type: "required", securityCode: "required"};
                        messages = {mobile: "手机必填", securityCode: "请输入验证码"}
                    } else if (t == "info-bindEmail") {
                        rules = {email: "required", type: "required", securityCode: "required"};
                        messages = {email: "邮箱必填", securityCode: "请输入验证码"}
                    } else if (t == "info-auth") {
                        rules = {
                            frontUrl: "required",
                            backUrl: "required",
                            name: "required",
                            idCard: "required"
                        };
                        messages = {
                            frontUrl: "请上传正面照片",
                            backUrl: "请上传反面照片",
                            name: "请填写您的真实姓名",
                            idCard: "请填写您的身份证号"
                        };
                    }
                    $("#submitForm").validate({
                        rules: rules,
                        messages: messages,
                        submitHandler: function () {
                            $("#submitForm").ajaxSubmit(function (message) {
                                invokTips(message.type, message.content);
                                setTimeout(function () {
                                    location.reload(true);
                                }, 1000);
                            });
                        }
                    });
                }
                var $getCode = $("#getCode");
                var ii;
                var count = 60;

                function refreshTime() {
                    count = count - 1;
                    if (count == 0) {
                        count = 60;
                        $getCode.prop('disabled', false);
                        $getCode.html("点击获取");
                        clearInterval(ii);
                        return false;
                    }
                    $getCode.html(count + "秒后重新获取");
                }

                $getCode.on("click", function () {
                    var url = "${base}/wap/member/send_captcha.jhtml";
                    if ($(this).attr("bind") == "mobile") {
                        url = "${base}/wap/member/bind_send_captcha.jhtml?bindNo=" + $("#mobile").val() + "&type=mobile";
                    } else if ($(this).attr("bind") == "email") {
                        url = "${base}/wap/member/bind_send_captcha.jhtml?bindNo=" + $("#email").val() + "&type=email";
                    }
                    $.ajax({
                        url: url,
                        dataType: "json",
                        type: "post",
                        success: function (data) {
                            if (data.type == 'success') {
                                invokTips("success", "发送成功");
                                ii = setInterval(refreshTime, 1000);
                                return;
                            } else {
                                invokTips(data.type, data.content);
                            }
                        }
                    });
                });
                e.modal("open");
                return false;
            });

            var $selectImg = $("#selectImg");
            $selectImg.on('click', function () {
                $("#headImg").click();
            });

        });
        function changeHeadImg(file, id, img) {
            preivew(file, img);
            if ($("#" + id).val().length > 0) {
                $("#headForm").submit();
            }
        }
    </script>
    <style>
        #wrapper div {
            background: rgb(241, 241, 241);
        }

        .am-list > li {
            border: 1px solid #dedede;
        }
    </style>
</head>

<body>
<div class="page">
    <header class="am-topbar am-topbar-fixed-top">
        <header data-am-widget="header" class="am-header am-header-default">
            <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                    class="am-header-nav-title wap-rebk">返回</span> </a></div>
            <h1 class="am-header-title">个人资料</h1>
        </header>
    </header>
    <div id="wrapper" class="am-padding-horizontal-0 "
         style="background: #f7f7f7;font-size: 1.6rem;font-weight: bolder;">
        <div style="background:">
            <div class="am-g am-margin-top-xs">
                <div>
                    <ul class="am-list">
                        <li class="am-padding-vertical-xs" style="line-height: 50px;">
                            <form id="headForm" action="uploadPhoto.jhtml" method="post" enctype="multipart/form-data">
                                <input type="file" id="headImg" name="headImg" style="display: none;" accept="image/*"
                                       onchange="changeHeadImg(this,'headImg','headImgShow')">
                            </form>
                            <span class="am-u-sm-9">头像</span>
                            <a style="padding:0px;" id="selectImg"> <img width="50" id="headImgShow"
                                                                         src="${base}/resources/weixin/images/header.png"
                                                                         data-original="${member.headImg}"
                                                                         class="am-circle lazy"></a></li>
                    </ul>
                </div>
            </div>
            <div class="am-g am-margin-top-xs">
                <div>
                    <ul class="am-list wap-member-info-2">
                        <li>
                            <a data-am-modal="{target:'#modal-helper'}" data-type="info-name" data-toggle="modalhelper"
                               style="display: flex;"
                               data-options="{'id':1,'name':'${member.name}'}" data-title="修改姓名">
                                <span class="am-u-sm-9">姓名</span><span class="am-text-secondary">${member.name}</span>
                            </a>
                        </li>
                        <li><a data-am-modal="{target:'#modal-helper'}" data-type="info-sex" data-toggle="modalhelper"
                               style="display: flex;"
                               data-options="{'id':1,'sex':[#if member.gender=='male']0[#else]1[/#if]}"
                               data-title="修改性别"><span class="am-u-sm-9">性别</span><span
                                class="am-text-secondary">[#if member.gender != null && member.gender == 'male']
                            男[/#if][#if member.gender != null && member.gender == 'female']女[/#if]</span></a></li>
                        <li><a data-am-modal="{target:'#modal-helper'}" data-type="info-area" data-toggle="modalhelper"
                               data-options="{'id':1,'areaId':${member.area.id},treePath:'${member.area.treePath}'}"
                               data-title="修改所在区域"><span class="am-u-sm-9" style="    width: 30%;">所在地</span><span
                                class="am-text-secondary">[#if member.area != null]${member.area.fullName}[/#if]</span></a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="am-g am-margin-top-xs">
                <div>
                    <ul class="am-list wap-member-info-3">
                        <li>
                        [#if member.idcard??]
                            [#if member.idcard.authStatus == "success"]
                                <a><span class="am-u-sm-9">实名认证</span><span class="am-text-success">已认证</span></a>
                            [#elseif member.idcard.authStatus == "wait"]
                                <a><span class="am-u-sm-9">实名认证</span><span class="am-text-success">已确认</span></a>
                            [#elseif member.idcard.authStatus == "fail"]
                                <a data-am-modal="{target:'#modal-helper'}" data-type="info-auth"
                                   data-toggle="modalhelper"
                                   data-options="{'id':1}" data-title="实名认证"><span class="am-u-sm-9">实名认证</span><span
                                        class="am-text-warning">已驳回</span></a>
                            [#else]
                                <a data-am-modal="{target:'#modal-helper'}" data-type="info-auth"
                                   data-toggle="modalhelper"
                                   data-options="{'id':1}" data-title="实名认证"><span class="am-u-sm-9">实名认证</span><span
                                        class="am-text-warning">未认证</span></a>
                            [/#if]
                        [#else]
                            <a data-am-modal="{target:'#modal-helper'}" data-type="info-auth"
                               data-toggle="modalhelper"
                               data-options="{'id':1}" data-title="实名认证"><span class="am-u-sm-9">实名认证</span><span
                                    class="am-text-warning">未认证</span></a>
                        [/#if]
                        </li>
                        <li><a data-am-modal="{target:'#modal-helper'}" data-type="info-editpassword"
                               data-toggle="modalhelper"
                               data-options="{'id':1}" data-title="修改密码"><span class="am-u-sm-9">登录密码</span><span
                                class="am-margin-left-sm am-text-warning">修改密码</span></a></li>
                        <li><span class="am-u-sm-9 " style="line-height: 51px;color: #666;">支付密码</span>
                            <a style="display: inline-block;" data-am-modal="{target:'#modal-helper'}"
                               data-type="info-editpaypwd"
                               data-toggle="modalhelper" data-options="{'id':1}" data-title="修改支付密码"><span
                                    class="am-text-warning am-margin-left-sm">修改密码</span></a>
                            <a style="display: inline-block;" data-am-modal="{target:'#modal-helper'}"
                               data-type="info-findpaypwd"
                               data-toggle="modalhelper" data-options="{'id':1}" data-title="找回支付密码"><span
                                    class="am-margin-left-sm am-text-warning">找回密码</span></a></li>
                        <li>
                        [#if member.bindMobile == 'binded']
                            <a><span class="am-u-sm-9">安全手机</span><span class="am-text-secondary">已绑定</span></a>
                        [#elseif  member.bindMobile=='none']
                            <a data-am-modal="{target:'#modal-helper'}" data-type="info-bindmobile"
                               data-toggle="modalhelper"
                               data-options="{'id':1}" data-title="手机绑定"><span class="am-u-sm-9">安全手机</span><span
                                    class="am-text-secondary">未绑定</span></a>
                        [#elseif  member.bindMobile=='unbind']
                            <a data-am-modal="{target:'#modal-helper'}" data-type="info-bindmobile"
                               data-toggle="modalhelper"
                               data-options="{'id':1}" data-title="手机绑定"><span class="am-u-sm-9">安全手机</span><span
                                    class="am-text-secondary">已解绑</span></a>
                        [/#if]
                        </li>
                        <li>
                        [#if member.bindEmail == 'binded']
                            <a><span class="am-u-sm-9">安全邮箱</span><span class="am-text-secondary">已绑定</span></a>
                        [#elseif  member.bindEmail=='none']
                            <a data-am-modal="{target:'#modal-helper'}" data-type="info-bindEmail"
                               data-toggle="modalhelper"
                               data-options="{'id':1}" data-title="邮箱绑定"><span class="am-u-sm-9">安全邮箱</span><span
                                    class="am-text-secondary">未绑定</span></a>
                        [#elseif  member.bindEmail=='unbind']
                            <a data-am-modal="{target:'#modal-helper'}" data-type="info-bindEmail"
                               data-toggle="modalhelper"
                               data-options="{'id':1}" data-title="邮箱绑定"><span class="am-u-sm-9">安全邮箱</span><span
                                    class="am-text-secondary">已解绑</span></a>
                        [/#if]
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="am-popup" id="modal-helper">
        <div class="am-popup-inner">
            <div class="am-popup-hd">
                <h4 class="am-popup-title"></h4>
                <span data-am-modal-close="" class="am-close"><i class="am-icon-chevron-left"></i></span></div>
            <div class="am-popup-bd" style="padding: 15px 0px;"></div>
        </div>
    </div>
</div>

</body>
</html>
