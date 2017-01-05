<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.review.edit")}</title>
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

    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage4.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">商品评论</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">粉丝们对您的商品提出了宝贵的意见，快去看看吧。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">评论编辑</a></li>
                </ul>

            </div>
            <form id="inputForm" action="update.jhtml" method="post">
                <input type="hidden" name="id" value="${review.id}"/>
                <table class="input">
                    <tr>
                        <th>
                        ${message("Review.product")}:
                        </th>
                        <td>
                            <a href="${base}/helper/product/content/${review.product.id}.jhtml"
                               target="_blank">${review.product.name}</a>
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Review.member")}:
                        </th>
                        <td>
                        [#if review.member??]
						${review.member.username}
					[#else]
                        ${message("admin.review.anonymous")}
                        [/#if]
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Review.ip")}:
                        </th>
                        <td>
                        ${review.ip}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Review.score")}:
                        </th>
                        <td>
                        ${review.score}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Review.content")}:
                        </th>
                        <td>
                        ${review.content}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Review.images")}:
                        </th>
                        <td>
                        [#if productImages??&&productImages?has_content]
                            [#list productImages as productImage]
                            <a href="${productImage.source}" target="_blank">
                                <img class="lazy" style="width: 160px;height: 160px;cursor: pointer;" src="${productImage.thumbnail}" >
                            </a>
                            [/#list]
                        [/#if]
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Review.isShow")}:
                        </th>
                        <td>
                            <input type="checkbox" name="isShow" value="true"[#if review.isShow]
                                   checked="checked"[/#if]/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td>
                            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                            <input type="button" class="button" value="${message("admin.common.back")}"
                                   onclick="location.href='manager.jhtml'"/>
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
