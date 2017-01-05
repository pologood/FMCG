<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.brand.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {

        [@flash_message /]

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 活动列表
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
        <a href="add.jhtml" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
        <div class="buttonWrap">
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
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="name">活动名称</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="introduction">活动介绍</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="beginDate">起始日期</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="endDate">结束日期</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="status">状态</a>
            </th>
            <th>
                活动类型
            </th>
            <th>
                平台优惠券
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as plan]
        <tr>
            <td>
                <input type="checkbox" name="ids" value="${plan.id}"/>
            </td>
            <td>
            ${plan.name}
            </td>
            <td>
            ${plan.introduction}
            </td>
            <td>
                [#if plan.beginDate??]
                    <span title="${plan.beginDate?string("yyyy-MM-dd HH:mm:ss")}">${plan.beginDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                [#else]
                    -
                [/#if]
            </td>
            <td>
                [#if plan.endDate??]
                    <span title="${plan.endDate?string("yyyy-MM-dd HH:mm:ss")}">${plan.endDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                [#else]
                    -
                [/#if]
            </td>

            <td>
                [#if plan.onOff??]
                    <label style="color: [#if plan.onOff=='wait']red[#elseif plan.onOff=='on']green[#else ]grey[/#if]">
                    ${(message("ActivityPlanning.Status."+plan.onOff))!}
                    </label>
                [#else]
                    -
                [/#if]
            </td>
            <td>
                <table class="input">
                    [#if plan.type=="random"]
                    [#list plan.adPositions as adPosition]
                        <tr>
                            <td style="float: left;border-bottom: none">
                                <label style="padding:0 5px;">${(adPosition.description)!}</label>&nbsp;
                                <a href="${base}/admin/ad/list.jhtml?adPositionId=${(adPosition.id)!}">[管理]</a>&nbsp;
                            </td>
                        </tr>
                    [/#list]
                    [#elseif plan.type=="unionActivity"]
                        [#list plan.singleProductPositions as singleProductPosition]
                            <tr>
                                <td style="float: left;border-bottom: none">
                                    <label style="padding:0 5px;">${(singleProductPosition.name)!}</label>&nbsp;
                                    <a href="${base}/admin/single/product/list.jhtml?singleProductPositionId=${(singleProductPosition.id)!}">[管理]</a>&nbsp;
                                </td>
                            </tr>
                        [/#list]
                    [/#if]
                </table>
            </td>
            <td>
                <table class="input">
                    [#list plan.coupons as coupon]
                        <tr>
                            <td style="float: left;border-bottom: none">
                                <label style="padding:0 5px;">满${(coupon.minimumPrice)!}减${(coupon.amount)!}</label>&nbsp;
                                <a href="${base}/admin/activity_planning/sumer.jhtml?id=${(coupon.id)!}&type=used">[统计]</a>&nbsp;
                            </td>
                        </tr>
                    [/#list]
                </table>
            </td>
            <td>
                <a href="edit.jhtml?id=${plan.id}" [#if plan.status??&&plan.status=='wait']style="color: red"[/#if]>
                    [#if plan.status??&&plan.status=='wait']
                        [编辑]
                    [#else]
                        [查看]
                    [/#if]
                </a>
                <!-- a href="${base}/admin/brand/series/list.jhtml?brandId=${plan.id}">管理</a -->
            </td>
        </tr>
    [/#list]
    </table>
[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
    [#include "/admin/include/pagination.ftl"]
[/@pagination]
</form>
</body>
</html>