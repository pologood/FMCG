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
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $tenantCategoryId = $("#tenantCategoryId");
            var timeout;
            var $browserButton = $("#browserButton");
            var $browserLogoButton = $("#browserLogoButton");
            var $shortName = $("#shortName");
            var $submit = $(":submit");

        [@flash_message /]

            var settings = {
                width: 360,
                height: 360
            };

            $browserButton.browser(settings);

            var settings_logo = {
                width: 120,
                height: 120
            };
            $browserLogoButton.browser(settings_logo);

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

            //店铺名称唯一验证
            $shortName.blur(function () {
                var shortName = $(this).val();
                if (shortName.trim() != "") {
                    $.ajax({
                        url: "${base}/helper/member/profile/checkShortName.jhtml",
                        type: "get",
                        data: {"shortName": shortName},
                        dataType: "json",
                        success: function (data) {
                            if (data) {
                                $("#checkMsg").text("(店铺名称已存在！)");
                                $submit.prop("disabled", true);
                            } else {
                                $("#checkMsg").text("");
                                $submit.prop("disabled", false);
                            }
                        }
                    });
                } else {
                    $("#checkMsg").text("");
                    $submit.prop("disabled", false);
                }
            });


            // 表单验证
            $inputForm.validate({
                rules: {
                    "shortName": {
                        required: true
                    },
                    "tenantType": {
                        required: true
                    },
                    "tenantCategoryId": {
                        required: true
                    },
                    "domain": {
                        remote: {
                            url: "check_domain.jhtml",
                            cache: false
                        }
                    },
                    "areaId": {
                        required: true
                    },
                    "address": {
                        required: true
                    },
                    "linkman": {
                        required: true
                    },
                    "telephone": {
                        required: true,
                        pattern: /^(1[3,5,8,7]{1}[\d]{9})|(((400)-(\d{3})-(\d{4}))|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{3,7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$|^([ ]?)$/
                    },
                    "qq": {
                        pattern: /^\d+$/
                    }
                },
                messages: {
                    shortName: "${message("admin.validate.required")}",
                    tenantType: "${message("admin.validate.required")}",
                    tenantCategoryId: "${message("admin.validate.required")}",
                    domain: {
                        remote: "域名已存在"
                    },
                    areaId: "${message("admin.validate.required")}",
                    address: "${message("admin.validate.required")}",
                    linkman: "${message("admin.validate.required")}",
                    telephone: {
                        required: "必填",
                        pattern: "请输入正确的手机号码"
                    },
                    qq: "请输入正确的qq号"
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


            <!-- <div class="con-con"> -->
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">店铺资料</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">完善我的店铺资料。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/tenant/edit.jhtml?type=0">基本信息</a></li>
                [@helperRole url="helper/member/ad/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus="" href="${base}/${helperRole.retUrl}">店铺装修</a></li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/article/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus="" href="${base}/${helperRole.retUrl}">店铺公告</a></li>
                    [/#if]
                [/@helperRole]
                [#--<li><a href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]

                </ul>

            </div>
            <div class="input">
                <form id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${(tenant.id)!}"/>
                    <input type="hidden" name="tenantType" value="tenant"/>
                    <table class="input">
                        <tr>
                            <th>
                                <span class="requiredField">*</span>店铺名称:
                            </th>
                            <td>
                                <input type="text" id="shortName" name="shortName" class="text"
                                       value="${(tenant.shortName)!}"/><span id="checkMsg" style="color:red">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>主营类目:
                            </th>
                            <td>
                                <span class="fieldSet">
                                    <input type="hidden" id="tenantCategoryId" name="tenantCategoryId"
                                           value="${(tenant.tenantCategory.id)!}"
                                           treePath="${(tenant.tenantCategory.treePath)!}"/>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                店铺头像:
                            </th>
                            <td>
                      <span class="fieldSet">
                          <input type="text" name="logo" class="text" value="${tenant.logo}" maxlength="200"
                                 title="${message("admin.product.imageTitle")}" readonly="true"/>
                          <input type="button" id="browserLogoButton" class="button"
                                 value="${message("admin.browser.select")}"/>
                      [#if tenant.logo??]
                          <a href="${tenant.logo}" target="_blank">${message("admin.common.view")}</a>
                      [/#if]
                      </span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                门头招牌:
                            </th>
                            <td>
                              <span class="fieldSet">
                                  <input type="text" name="thumbnail" class="text" value="${tenant.thumbnail}"
                                         maxlength="200"
                                         title="${message("admin.product.imageTitle")}" readonly="true"/>
                                  <input type="button" id="browserButton" class="button"
                                         value="${message("admin.browser.select")}"/>
                              [#if tenant.thumbnail??]
                                  <a href="${tenant.thumbnail}" target="_blank">${message("admin.common.view")}</a>
                              [/#if]
                              </span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                店铺简介:
                            </th>
                            <td>
                                <textarea name="introduction"
                                          style="width: 80%;heigth:60px;">${(tenant.introduction)!}</textarea>
                            </td>
                        </tr>
                    [#if versionType==1]
                        <input type="hidden" name="rangeInfo" value="${(tenant.rangeInfo)!}"/>
                    [#else]
                        <input type="hidden" name="authorization" value="${(tenant.authorization)!}"/>
                        <tr>
                            <th>
                                销售地域:
                            </th>
                            <td>
                                <input type="text" name="rangeInfo" class="text" value="${(tenant.rangeInfo)!}"/><span>销售范围,例:通海县</span>
                            </td>
                        </tr>
                    [/#if]
                        <tr>
                            <th>
                                绑定域名:
                            </th>
                            <td>
                            [#if tenant.domain??]<a target="_blank"
                                                    href="http://${(tenant.domain)!}">${(tenant.domain)!}</a></span>[#else]
                                <input type="text" name="domain" class="text" value="${(tenant.domain)!}"/>
                                <span>例:<a target="_blank"
                                           href="http://${(tenantUrl)!}">${(tenantUrl)!}</a>（确定后不可编辑）</span>[/#if]
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>所属地区:
                            </th>
                            <td>
                  <span class="fieldSet">
                    <input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}"
                           treePath="${(tenant.area.treePath)!}"/>
                  </span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>地址:
                            </th>
                            <td>
                                <input type="text" name="address" class="text" value="${(tenant.address)!}"
                                       maxlength="30"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>联系人:
                            </th>
                            <td>
                                <input type="text" name="linkman" class="text" value="${(tenant.linkman)!}"
                                       maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>联系电话:
                            </th>
                            <td>
                                <input type="text" name="telephone" class="text" value="${(tenant.telephone)!}"
                                       maxlength="16"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                QQ:
                            </th>
                            <td>
                                <input type="text" name="qq" class="text" value="${(tenant.qq)!}" maxlength="12"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <input type="checkbox" name="noReason" [#if tenant.noReason] checked="checked"[/#if]>7天退货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="hidden" name="noReason" value="false"/>
                                <input type="checkbox" name="toPay" [#if tenant.toPay] checked="checked"[/#if]>货到付款&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="hidden" name="toPay" value="false"/>
                                <input type="checkbox" name="tamPo" [#if tenant.tamPo] checked="checked"[/#if]>担保交易
                                <input type="hidden" name="tamPo" value="false"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                            [@helperRole url="helper/member/tenant/edit.jhtml" type="update"]
                                [#if helperRole.retOper!="0"]
                                    <input type="submit" class="button" value="提交" hidefocus/>
                                [/#if]
                            [/@helperRole]


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
