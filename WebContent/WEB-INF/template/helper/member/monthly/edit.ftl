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
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
            var $type = $("#type");
            var $contentTr = $("#contentTr");
            var $pathTr = $("#pathTr");
            var $path = $("#path");
            var $file = $("#file");
            var $browserButton = $("#browserButton");

        [@flash_message /]

            var settings = {};

            $browserButton.browser(settings);

            // 表单验证
            $inputForm.validate({
                rules: {
                    title: "required",
                    adPositionId: "required",
                    path: "required",
                    file: "required",
                    order: "digits"
                },
                messages: {
                    title: "${message("admin.validate.required")}",
                    adPositionId: "${message("admin.validate.required")}",
                    path: "${message("admin.validate.required")}",
                    file: "${message("admin.validate.required")}",
                    order: "序号如：1、2、3..."
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
                    <img class="js-app_logo app-img" src="${base}/resources/b2b/images/v2.0/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">店铺装修</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">装修我的店铺，选择模版。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="" hideFocus="" href="${base}/helper/member/tenant/edit.jhtml?type=0">基本信息</a></li>
                    <li><a class="on" hideFocus="" href="${base}/helper/member/ad/list.jhtml">店铺装修</a></li>
                    <li><a class="" hideFocus="" href="${base}/helper/member/article/list.jhtml">店铺公告</a></li>
                    [#--<li><a class="" hideFocus="" href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]
                </ul>

            </div>
            <form id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${ad.id}"/>
                <input type="hidden" id="type" name="type" value="${ad.type}"/>
                <table class="input">
                    <tr>
                        <th>
                            <span class="requiredField">*</span>${message("Ad.title")}:
                        </th>
                        <td>
                            <input type="text" name="title" class="text" value="${ad.title}" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Ad.adPosition")}:
                        </th>
                        <td>
                            <select name="adPositionId">
                            [#list adPositions as adPosition]
                                <option value="${adPosition.id}" [#if adPosition == ad.adPosition]
                                        selected="selected"[/#if]>${adPosition.name} [${adPosition.width}
                                    × ${adPosition.height}]
                                </option>
                            [/#list]
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Ad.linkType")}:
                        </th>
                        <td>
                            <select id="type" name="linkType">
                            [#list linkTypes as linkType]
                                <option value="${linkType}"[#if linkType == ad.linkType]
                                        selected="selected"[/#if]>${message("Ad.LinkType." + linkType)}</option>
                            [/#list]
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>${message("Ad.path")}:
                        </th>
                        <td>
                            <span class="fieldSet">
                                <input type="text" name="path" class="text" value="${ad.path}" maxlength="200" title="${message("admin.product.imageTitle")}" readonly="true"/>
                                <input type="button" id="browserButton" class="button" value="${message("admin.browser.select")}"/>
                                [#if ad.path??]
                                <a href="${ad.path}" target="_blank">${message("admin.common.view")}</a>
                                [/#if]
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Ad.url")}:
                        </th>
                        <td>
                            <input type="text" name="url" id="productUrl" class="text" hidden value="${ad.url}" maxlength="200"
                                   readonly=true/>
                            <select id="selectProduct" name="linkId" onchange="generateUrl()">
                                <option value=''>请选择商品</option>
                            [#if products??]
                                [#list products as product]
                                    <option value='${product.id}' [#if ad.linkId==product.id] selected[/#if]>${product.name}</option>
                                [/#list]
                            [/#if]
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("admin.common.order")}:
                        </th>
                        <td>
                            <input type="text" name="order" class="text" value="${ad.order}" maxlength="9"/>
                        </td>
                    </tr>
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
