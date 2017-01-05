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
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/store/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript">

        $().ready(function () {

            var $listForm = $("#listForm");
            var $status = $("#status");
            var $statusSelect = $("#statusSelect");
            var $statusOption = $("#statusOption a");

        [@flash_message /]

            $statusSelect.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });

            $statusOption.click(function () {
                var $this = $(this);
                $status.val($this.attr("val"));
                $listForm.submit();
                return false;
            });

        });
    </script>

</head>
<body>
<div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
    <div class="js-app-header title-wrap" id="app_0000000844">
        <img class="js-app_logo app-img" src="${base}/resources/store/images/coupon.png"/>
        <dl class="app-info">
            <dt class="app-title" id="app_name">限时折扣</dt>
            <dd class="app-status" id="app_add_status">
            </dd>
            <dd class="app-intro" id="app_desc">管理我的店铺限时折扣。</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <a class="on" hideFocus="" href="${base}/store/member/discount/add.jhtml?type=seckill">所有商品</a>
        <a hideFocus="" href="${base}/store/member/discount/list.jhtml?type=seckill">商品分类</a>
    </ul>

</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="type" name="type" value="${type}"/>
    <input type="hidden" id="status" name="status" value="${status}"/>
    <div class="bar">
        <div class="buttonWrap">
            <a href="add.jhtml?status=${status}&type=${type}" class="iconButton">
                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
            </a>
            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>
            <a href="javascript:;" id="refreshButton" class="iconButton">
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
                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="menuWrap">
            <div class="search">
                <span id="searchPropertySelect" class="arrow">&nbsp;</span>
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
            <div class="popupMenu">
                <ul id="searchPropertyOption">
                    <li>
                        <a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if]
                           val="name">名称</a>
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
                <th>商品名称</th>
                <th><a href="javascript:;" class="sort" name="priceExpression">折后价</a></th>
                <th>标价</th>
                <th><a href="javascript:;" class="sort" name="beginDate">开始时间</a></th>
                <th><a href="javascript:;" class="sort" name="endDate">结束时间</a></th>
                <th>
                    <span>${message("admin.common.handle")}</span>
                </th>
            </tr>
        [#list promotion as promo]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${promo.id}"/>
                </td>
                <td>
                ${promo.fullName}
                </td>
                <td style="color:red;">
                ${promo.price}
                </td>
                <td>
                ${promo.marketPrice}
                </td>
                <td>
                    <span title="${promo.beginDate?string("yyyy-MM-dd HH:mm")}">${promo.beginDate?string("yyyy-MM-dd HH:mm")}</span>
                </td>
                <td>
                    <span title="${promo.endDate?string("yyyy-MM-dd HH:mm")}">${promo.endDate?string("yyyy-MM-dd HH:mm")}</span>
                </td>
                <td>
                    <a href="edit.jhtml?id=${promo.id}">编辑</a>
                </td>
            </tr>
        [/#list]
        </table>
    [#if !page.content?has_content]
        <p>${message("shop.member.noResult")}</p>
    [/#if]
    [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
        [#include "/store/include/pagination.ftl"]
    [/@pagination]
    </div>
</form>
</body>
</html>