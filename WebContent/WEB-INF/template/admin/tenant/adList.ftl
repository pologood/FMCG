<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>商家列表 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<form id="listForm" action="adList.jhtml" method="get">
    <div class="bar">
        <div class="buttonWrap">
            <a href="add.jhtml?tenantId=${tenant.id}" id="export_ss" class="button">添加</a>
            <a href="javascript:;" id="deleteButton" class="button">删除</a>
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
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>标题</span>
            </th>
            <th>
                <span>商家</span>
            </th>
            <th>
                <span>类型</span>
            </th>
            <th>
                <span>开始时间</span>
            </th>
            <th>
                <span>结束时间</span>
            </th>
            <th>
                <span>排序</span>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as ad]
        <tr>
            <td><input type="checkbox" name="ids" value="${ad.id}"/></td>
            <td>${(ad.title)!}</td>
            <td>${(ad.tenant.name)!}</td>
            <td>${message("Ad.Type." + ad.type)}</td>
            <td>
                [#if ad.beginDate??]
                    <span title="${ad.beginDate?string("yyyy-MM-dd HH:mm:ss")}">${ad.beginDate}</span>
                [#else]
                    -
                [/#if]
            </td>
            <td>
                [#if ad.endDate??]
                    <span title="${ad.endDate?string("yyyy-MM-dd HH:mm:ss")}">${ad.endDate}</span>
                [#else]
                    -
                [/#if]
            </td>
            <td>${(ad.order)!}</td>
            <td><a href="editAd.jhtml?id=${ad.id}&tenantId=${tenant.id}">[编辑]</a></td>
        </tr>
    [/#list]
    </table>
[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
    [#include "/admin/include/pagination.ftl"]
[/@pagination]
</form>
</body>
</html>