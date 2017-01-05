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
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $tenantCategoryId = $("#tenantCategoryId");
            var $id = $("#id");
            var $licensePhoto = $("#licensePhoto");
            var timeout;
            var $browserButton = $("#browserButton");

        [@flash_message /]
            $browserButton.browser();

            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
            //清除火狐浏览器刷新多出拉下框
            $("select[name='areaId_select']").each(function () {
                if ($(this).val() == "") {
                    $(this).nextAll("select").remove();
                    return false;
                }
            });
            // 店铺分类
            $tenantCategoryId.lSelect({
                url: "${base}/helper/member/tenant/tenantCategory.jhtml"
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    name: {
                        required: true
                    },
                    areaId: {
                        required: true
                    },
                    tenantCategoryId: {
                        required: true
                    },
                    address: {
                        required: true,
                        maxlength: 100
                    },
                    linkman: {
                        required: true,
                        maxlength: 100
                    },
                    telephone: {
                        required: true,
                        pattern: /^(1[3,5,8,7]{1}[\d]{9})|(((400)-(\d{3})-(\d{4}))|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{3,7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$|^([ ]?)$/
                    }
//                    },
//                    licensePhoto: {
//                        required: true
//                    }
                },
                messages: {
                    name: {
                        required: "必填"
                    },
                    areaId: {
                        required: "必填"
                    },
                    address: {
                        required: "必填",
                        maxlength: "长度超出"
                    },
                    linkman: {
                        required: "必填",
                        maxlength: "长度超出"
                    },
                    telephone: {
                        required: "必填",
                        pattern: "请输入正确的手机号码"
                    }
//                    },
//                    licensePhoto: {
//                        required: "必填"
//                    }
                },
                submitHandler: function (form) {
                    $.ajaxSetup({async: false});
                    $.ajax({
                        url: $inputForm.attr("action"),
                        type: "POST",
                        data: {
                            name: $("#name").val(),
                            address: $("#address").val(),
                            linkman: $("#linkman").val(),
                            telephone: $("#telephone").val(),
                            id: $id.val(),
                            licensePhoto: $licensePhoto.val(),
                            tenantCategoryId: $tenantCategoryId.val(),
                            areaId: $areaId.val()
                        },
                        dataType: "json",
                        cache: false,
                        success: function (message) {
                            if (message.type == "success") {
                            [#if redirectUrl??]
                                location.href = "${redirectUrl}";
                            [#else]
                                location.href = "${base}/helper/member/profile/edit.jhtml";
                            [/#if]
                            } else {
                                location.href = "${base}/helper/member/tenant/add.jhtml";
                            }
                        }
                    });
                    $.ajaxSetup({async: true});
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
                        <dt class="app-title" id="app_name">完成资料</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">开店申请，完善店铺信息。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/tenant/add.jhtml?pageActive=1">完善资料</a>
                    </li>
                </ul>
            </div>

            <div class="wrap">
                <div class="main clearfix">
                    <form id="inputForm" action="save.jhtml" method="post">
                        <input type="hidden" name="id" id="id" value="${(tenant.id)!}"/>
                        <table class="input">
                            <tr>
                                <th>
                                    <span class="requiredField">*</span>店铺名称:
                                </th>
                                <td>
                                    <input type="text" name="name" id="name" class="text" value="${(tenant.name)!}"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <span class="requiredField">*</span>商家分类:
                                </th>
                                <td>
                                    <span class="fieldSet">
                                        <input type="hidden" id="tenantCategoryId" name="tenantCategoryId"
                                                value="${(tenant.tenantCategory.id)!}" treePath="${(tenant.tenantCategory.treePath)!}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <span class="requiredField">*</span>所属地区:
                                </th>
                                <td>
                                    <span class="fieldSet">
                                        <input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}" treePath="${(tenant.area.treePath)!}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <span class="requiredField">*</span>地址:
                                </th>
                                <td>
                                    <input type="text" name="address" id="address" class="text"
                                           value="${(tenant.address)!}"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <span class="requiredField">*</span>联系人:
                                </th>
                                <td>
                                    <input type="text" name="linkman" id="linkman" class="text"
                                           value="${(tenant.linkman)!}"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    <span class="requiredField">*</span>联系电话:
                                </th>
                                <td>
                                    <input type="text" id="telephone" name="telephone" class="text"
                                           value="${(tenant.telephone)!}"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    营业执照:
                                </th>
                                <td>
                                    <span class="fieldSet">
                                        <input type="text" id="licensePhoto" name="licensePhoto" class="text"
                                               value="${tenant.licensePhoto}" maxlength="200" title="营业执照"
                                               readonly="true"/>
                                        <input type="button" id="browserButton" class="button"
                                               value="${message("admin.browser.select")}"/>
                                    [#if tenant.licensePhoto??]
                                        <a href="${tenant.licensePhoto}"
                                           target="_blank">${message("admin.common.view")}</a>
                                    [/#if]
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    &nbsp;
                                </th>
                                <td>
                                    <input type="submit" class="button" value="提交申请" hidefocus/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
