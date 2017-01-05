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

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        var countdown = 60;
        function settime(obj) {
            if (countdown == 0) {
                obj.removeAttr("disabled");
                obj.val("获取验证码");
                countdown = 60;
                return;
            } else {
                obj.attr("disabled", true);
                obj.val("" + countdown + "s重新发送");
                countdown--;
            }
            setTimeout(function () {
                settime(obj)
            }, 1000);
        }

    </script>
    <script type="text/javascript">
        $().ready(function () {
            var $registerForm = $("#registerForm");
            var $mobile = $("#mobile");
            var $sendCode = $("#sendCode");
            var $selectedvalue;
            var mpOpType =
            {
        [#list types as type]
            [#if type_has_next]
            ${type}:
                '${message("helper.role."+type)}',
            [#else ]
            ${type}:
                '${message("helper.role."+type)}'
            [/#if]
        [/#list]
        };

            $("input[name='roles']").change(function () {
                $("#roleRulesTr").hide();
                $("input[name='roles']").each(function () {
                    if ($(this).attr("checked") == "checked") {
                        $.ajax({
                            url: "${base}/helper/member/role/getRules.jhtml",
                            type: "post",
                            data: {id: $(this).val()},
                            dataType: "json",
                            success: function (message) {
                                if (message.data != null && message.message.type == "success") {
                                    $("#roleRulesTd").empty();
                                    var strHtml = "";
                                    strHtml += " <div>";
                                    for (var i = 0; i < message.data.length; i++) {
                                        if (message.data[i].rules.children.length == 0) {
                                            strHtml += " <div>";
                                            strHtml += "<strong>" + message.data[i].rules.name + "：</strong>  ";
                                            $.each(message.data[i].mapAuthority, function (key, values) {
                                                if (values == true) {
                                                    strHtml += mpOpType[key] + "、 ";
                                                }
                                            });
                                            strHtml += " </div>";
                                        }

                                    }
                                    $("#roleRulesTd").append(strHtml);
                                    $("#roleRulesTr").show();
                                }
                            }
                        });
                    }
                });
            });
            $sendCode.click(function () {

                if ($mobile.val() == "" || $mobile.val() == null) {
                    $.message("warn", "请输入手机号码");
                    return false;
                }

                if ($mobile.val().length != 11) {
                    $.message("warn", "手机号码不合法");
                    return false;
                }

                $.ajax({
                    url: "${base}/helper/member/tenant/isOwner.jhtml",
                    type: "post",
                    data: {username: $mobile.val()},
                    dataType: "json",
                    success: function (message) {

                        if (message.type == "error") {
                            $.message(message);
                            return false;
                        }
                        settime($sendCode);
                        $.ajax({
                            url: "${base}/helper/member/tenant/employee/send_mobile.jhtml",
                            type: "post",
                            data: {username: $mobile.val()},
                            dataType: "json",
                            success: function (message) {
                                $.message(message);
                            }
                        });
                    }
                });

            });

            $("input[name='employeeType']").change(function () {
                 $selectedvalue = $("input[name='employeeType']:checked").val();
                if ($selectedvalue == 1) {
                    $("#mobileNumber").show();
                    $("#employeeNumber").hide();
                } else {
                    $("#employeeNumber").show();
                    $("#mobileNumber").hide();
                }
            });

            // 表单验证
            $registerForm.validate({
                submitHandler: function (form) {

                    if($selectedvalue==0){
                        //工号
                        $("#mobile").val(null);
                        var _usernumber = $("#usernumber").val();
                        var reg =/^[0-9]+[0-9]$/;
                        if (!reg.test(_usernumber)) {
                           //非数字
                            $.message("warn", "输入工号不合法");
                            return false;
                        }
                        //密码
                        if($("input[name='newPassword']").val()!=$("input[name='enNewPassword']").val()){
                            //密码不正确
                            $.message("warn", "两次输入密码不一致,请重新输入!");
                            return false;
                        }


                    }else {
                        //手机号
                        $("#usernumber").val(null);
                    }


                    var isPass = "请选择角色";
                    //检查行业标签
                    $("input[name='roles']").each(function () {
                        if ($(this).attr('checked')) {
                            isPass = "";
                            return;
                        }
                    });
                    if (isPass != "") {
                        $.message("error", isPass);
                        return false;
                    }
                    $.ajax({
                        url: $registerForm.attr("action"),
                        type: "post",
                        data: $registerForm.serialize(),
                        dataType: "json",
                        success: function (data) {
                            $.message(data.message);
                            if (data.message.type == "success") {
                                setTimeout(function () {
                                    location.href = "${base}/helper/member/tenant/employee/list.jhtml";
                                }, 2000);
                            }
                        }
                    });
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">添加员工</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">添加你的员工。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/tenant/employee/add.jhtml?pageActive=1">添加员工</a>
                    </li>
                </ul>
            </div>

            <div class="wrap">
                <form id="registerForm" action="${base}/helper/member/tenant/employee/add.jhtml" method="post">
                    <table class="input">
                        <thead>
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <label>
                                    <input type="radio" name="employeeType" value="0"/>工号
                                </label>
                                <label>
                                    <input type="radio" name="employeeType" value="1" checked/>手机号
                                </label>
                            </td>
                        </tr>
                        </thead>


                    [#--手机号--]
                        <thead id="mobileNumber">
                        <tr>
                            <th>
                                <span class="requiredField">*</span>手机号:
                            </th>
                            <td>
                                <input type="text" id="mobile" name="mobile" class="text"
                                       maxlength="${setting.usernameMaxLength}"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>验证码:
                            </th>
                            <td>
                                <input type="text" id="captcha" name="captcha" class="text" maxlength="6"/>
                                <input type="button" id="sendCode" value="获取验证码"/>
                            </td>
                        </tr>
                        </thead>
                    [#--END--]

                    [#--工号--]
                        <thead id="employeeNumber" style="display: none;">
                        <tr>
                            <th>
                                <span class="requiredField">*</span>工号:
                            </th>
                            <td>
                                <input type="text" id="usernumber" name="usernumber" class="text"
                                       maxlength="${setting.usernameMaxLength}"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>密码:
                            </th>
                            <td>
                                <input type="password" name="newPassword" class="text" maxlength="18"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>确认密码:
                            </th>
                            <td>
                                <input type="password" name="enNewPassword" class="text" maxlength="18"/>
                            </td>
                        </tr>
                        </thead>
                    [#--END--]


                        <tr>
                            <th>
                                所属门店:
                            </th>
                            <td>
                                <select name="deliveryCenterId">
                                [#list deliveryCenterList as delivery]
                                    <option value="${delivery.id}">${delivery.name}</option>
                                [/#list]
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <a href="${base}/helper/member/role/add.jhtml?redirectUrl=/helper/member/tenant/employee/add.jhtml">
                                    角色:</a>
                            </th>
                            <td>
                            [#list roles as rl]
                                <input type="radio" name="roles"
                                       [#if employee??&&employee.role?contains(rl.id.toString())]checked[/#if]
                                       value="${rl.id}"/>
                            ${rl.name}
                            [/#list]
                            </td>
                        </tr>
                        <tr id="roleRulesTr" style="display: none">
                            <th>
                                权限:
                            </th>
                            <td>
                                <div id="roleRulesTd">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                姓名:
                            </th>
                            <td>
                                <input type="text" name="username">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                性别:
                            </th>
                            <td>
                                <input type="radio" name="gender" value="male">男
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="gender" value="female">女
                            </td>
                        </tr>
                        <tr>
                            <th>
                                详细地址:
                            </th>
                            <td>
                                <input type="text" name="address" value="">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <input type="submit" class="button" value="提交"/>
                                <input type="button" class="button" value="${message("admin.common.back")}"
                                       onclick="location.href='list.jhtml'"/>

                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
