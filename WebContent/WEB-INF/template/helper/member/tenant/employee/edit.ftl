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
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
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

        [@flash_message /]

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

            $inputForm.validate({
                submitHandler: function (form) {
                    var isPass="请选择角色";
                    //检查行业标签
                    $("input[name='roles']").each(function(){
                        if($(this).attr('checked')){
                            isPass="";
                            return;
                        }

                    });
                    if(isPass!=""){
                        $.message("error",isPass);
                        return false;
                    }
                    $.ajax({
                        url: $inputForm.attr("action"),
                        type: "post",
                        data: $inputForm.serialize(),
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
                        <dt class="app-title" id="app_name">员工管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">管理你的员工。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/tenant/employee/list.jhtml">我的员工</a></li>
                </ul>
            </div>
            <form id="inputForm" action="${base}/app/member/employee/update.jhtml" method="post">
                <input type="hidden" name="id" value="${employee.id}"/>
                <table class="input">
                    <tr>
                        <th>
                            用户名:
                        </th>
                        <td>
                        ${employee.member.username}
                        </td>
                    [#if versionType==0]
                        <td style="border-left: 1px solid #dde9f5;text-align: center;width: 230px;">
                            二维码
                        </td>
                    [/#if]
                    </tr>
                    <tr>
                        <th>
                            姓名:
                        </th>
                        <td>
                            <input type="text" name="username" value="${employee.member.name}">
                        </td>
                    [#if versionType==0]
                        <td rowspan="7" style="border-left: 1px solid #dde9f5;">
                            <img src="${base}/helper/member/tenant/qrcode/employee.jhtml?mobile=${employee.member.username}">
                            </br>
                            <span style="font-size: 14px;color: orangered">单击右键选择【图片另存为】可保存二维码到本地</span>
                        </td>
                    [/#if]
                    </tr>
                    <tr>
                        <th>
                            性别:
                        </th>
                        <td> 
                            <input type="radio" name="gender" value="male" [#if employee.member.gender=='male']checked[/#if]>男
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="gender" value="female" [#if employee.member.gender=='female']checked[/#if]>女
                        </td>
                    </tr>
                    <tr>
                        <th>
                            详细地址:
                        </th>
                        <td>
                            <input type="text" name="address" value="${employee.member.address}">
                        </td>
                    </tr>
                    <tr>
                        <th>
                            所属门店:
                        </th>
                        <td>
                            <select name="deliveryCenterId">
                            [#list deliveryCenterList as delivery]
                                <option [#if delivery.id==(employee.deliveryCenter.id)!]selected[/#if]
                                        value="${delivery.id}">${delivery.name}</option>
                            [/#list]
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            角色:
                        </th>
                        <td>
                            [#list roles as rl]
                            <input type="radio" name="roles"


                                    [#if employee.role?exists]
                                        [#list employee.role?split(",") as fileName]
                                            [#if fileName!=""]
                                   [#if fileName?contains(rl.id.toString())]checked[/#if]
                                            [/#if]
                                        [/#list]
                                    [/#if]
                                [#--[#if employee.role?contains(rl.id.toString())]${rl.name}&nbsp;[/#if]--]

                                   [#--[#if employee.role?contains(rl.id.toString())][/#if]--]
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
                    [#if versionType==0]
                    <tr>
                        <th>
                            导购标签:
                        </th>
                        <td>
                            [#list tags as tag]
                                <input value="${tag.id}" type="checkbox" name="tagIds" [#if employee.tags?seq_contains(tag)==true]checked[/#if]/>${tag.name}
                            [/#list]
                            [#--<input type="checkbox" name="isSetGuiderStar" [#if employee.isSetGuiderStar==true]checked[/#if]/>--]
                        </td>
                    </tr>
                    [/#if]
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td>
                            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                            <input type="button" class="button" value="${message("admin.common.back")}"
                                   onclick="location.href='list.jhtml'"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
