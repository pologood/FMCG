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
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">通知公告</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">发布及管理本店公告或咨询。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                [@helperRole url="helper/member/tenant/edit.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus="" href="${base}/helper/member/tenant/edit.jhtml?type=0">基本信息</a></li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/ad/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus="" href="${base}/helper/member/ad/list.jhtml">店铺装修</a></li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/article/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="on" hideFocus="" href="${base}/helper/member/article/list.jhtml">店铺公告</a></li>
                    [/#if]
                [/@helperRole]
                [#--<li><a class="" hideFocus=""  href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]

                </ul>

            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <div class="bar">
                [@helperRole url="helper/member/article/list.jhtml" type="add"]
                    [#if helperRole.retOper!="0"]
                        <a href="add.jhtml" class="iconButton">
                            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                        </a>
                    [/#if]
                [/@helperRole]

                    <div class="buttonWrap">
                    [@helperRole url="helper/member/article/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a style="cursor: pointer;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="list.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a style="cursor: pointer;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="pageSizeOption">
                                    <li>
                                        <a style="cursor: pointer;"[#if page.pageSize == 10] class="current"[/#if]
                                           val="10">10</a>
                                    </li>
                                    <li>
                                        <a style="cursor: pointer;"[#if page.pageSize == 20] class="current"[/#if]
                                           val="20">20</a>
                                    </li>
                                    <li>
                                        <a style="cursor: pointer;"[#if page.pageSize == 50] class="current"[/#if]
                                           val="50">50</a>
                                    </li>
                                    <li>
                                        <a style="cursor: pointer;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <div class="search">
                            <span id="searchPropertySelect" class="arrow">&nbsp;</span>
                            <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}"
                                   maxlength="200"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                        <div class="popupMenu">
                            <ul id="searchPropertyOption">
                                <li>
                                    <a style="cursor: pointer;"[#if page.searchProperty == "title"]
                                       class="current"[/#if] val="title">${message("Article.title")}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="title">${message("Article.title")}</a>
                            </th>

                            <th>
                                <a href="javascript:;" class="sort" name="tenant">商家</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="isPublication">${message("Article.isPublication")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="createDate">${message("admin.common.createDate")}</a>
                            </th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as article]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${article.id}"/>
                            </td>
                            <td>
                                <span title="${article.title}">${abbreviate(article.title, 50, "...")}</span>
                            </td>

                            <td>
                            ${article.tenant.name}
                            </td>
                            <td>
                                <span class="${article.isPublication?string("true", "false")}Icon">&nbsp;</span>
                            </td>
                            <td>
                                <span title="${article.createDate?string("yyyy-MM-dd HH:mm:ss")}">${article.createDate}</span>
                            </td>
                            <td>
                                [@helperRole url="helper/member/article/list.jhtml" type="update"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="edit.jhtml?id=${article.id}">[${message("admin.common.edit")}]</a>
                                    [/#if]
                                [/@helperRole]
                                &nbsp;
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("helper.member.noResult")}</p>
                [/#if]
                </div>
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                [#include "/helper/member/include/pagination.ftl"]
            [/@pagination]
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
