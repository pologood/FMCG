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
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $browserButton = $("#browserButton");

        [@flash_message /]

            var settings = {
                width: 360,
                height: 360
            };

            $browserButton.browser(settings);
            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
            // 表单验证
            $inputForm.validate({
                rules: {
                    title: "required"
                },
                messages: {
                    title: "${message("admin.validate.required")}"
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
                        <dt class="app-title" id="app_name">通知公告</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">发布及管理本店公告或咨询。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="" hideFocus="" href="${base}/helper/member/tenant/edit.jhtml?type=0">基本信息</a></li>
                    <li><a class="" hideFocus="" href="${base}/helper/member/ad/list.jhtml">店铺装修</a></li>
                    <li><a class="on" hideFocus="" href="${base}/helper/member/article/list.jhtml">店铺公告</a></li>
                    [#--<li><a class="" hideFocus="" href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]
                </ul>

            </div>

            <form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data">
                <table class="input">
                    <tr>
                        <th>
                            <span class="requiredField">*</span>${message("Article.title")}:
                        </th>
                        <td>
                            <input type="text" name="title" class="text" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            主题图片:
                        </th>
                        <td>
          <span class="fieldSet">
            <input type="text" name="image" class="text" maxlength="200"
                   title="${message("admin.product.imageTitle")}"/>
            <input type="button" id="browserButton" class="button" value="${message("admin.browser.select")}"/>
          </span>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Article.author")}:
                        </th>
                        <td>
                            <input type="text" name="author" class="text" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("admin.common.setting")}:
                        </th>
                        <td>
                            <label>
                                <input type="checkbox" name="isPublication" value="true"
                                       checked="checked"/>${message("Article.isPublication")}
                                <input type="hidden" name="_isPublication" value="false"/>
                            </label>
                            <label>
                                <input type="checkbox" name="isTop" value="true"/>${message("Article.isTop")}
                                <input type="hidden" name="_isTop" value="false"/>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Article.tags")}:
                        </th>
                        <td>
                        [#list tags as tag]
                            <label>
                                <input type="checkbox" name="tagIds" value="${tag.id}"/>${tag.name}
                            </label>
                        [/#list]
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Article.content")}:
                        </th>
                        <td>
                            <textarea id="editor" name="content" class="editor"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Article.seoTitle")}:
                        </th>
                        <td>
                            <input type="text" name="seoTitle" class="text" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Article.seoKeywords")}:
                        </th>
                        <td>
                            <input type="text" name="seoKeywords" class="text" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Article.seoDescription")}:
                        </th>
                        <td>
                            <input type="text" name="seoDescription" class="text" maxlength="200"/>
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
