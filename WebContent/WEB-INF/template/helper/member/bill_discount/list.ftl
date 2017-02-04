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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/coupon.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">买单折扣</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">管理我的店铺买单折扣券。</dd>
                    </dl>
                </div>
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/bill/discount/list.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="add.jhtml" class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]
                    [@helperRole url="helper/member/bill/discount/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="${base}/helper/member/bill/discount/list.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="pageSizeOption">
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <div class="search">
                            <input placeholder="搜索买单折扣券名称" type="text" id="searchValue" name="searchValue"
                                   value="${page.searchValue}"
                                   maxlength="50"/>
                            <button type="submit">&nbsp;</button>
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
                                <a href="javascript:;" class="sort" name="name">名称</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="agioRate">折扣比例</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="backRate">返现比例</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="beginDate">开始时间</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="endDate">结束时间</a>
                            </th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as promotion]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${promotion.id}"/>
                            </td>
                            <td>
                            ${abbreviate(promotion.name,18,"..")}
                            </td>
                            <td>
                            ${promotion.agioRate}%
                            </td>
                            <td>
                            ${promotion.backRate}%
                            </td>
                            <td>
                                <span title="${promotion.beginDate?string("yyyy-MM-dd HH:mm")}">${promotion.beginDate?string("yyyy-MM-dd HH:mm")}</span>
                            </td>
                            <td>
                                <span title="${promotion.endDate?string("yyyy-MM-dd HH:mm")}">${promotion.endDate?string("yyyy-MM-dd HH:mm")}</span>
                            </td>
                            <td>
                                [@helperRole url="helper/member/coupon/list.jhtml" type="statistics"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="${base}/helper/member/payBill/list.jhtml">统计</a>
                                    [/#if]
                                [/@helperRole]
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("helper.member.noResult")}</p>
                [/#if]
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/helper/include/pagination.ftl"]
                [/@pagination]
                </div>
            </form>
        [#----]
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
