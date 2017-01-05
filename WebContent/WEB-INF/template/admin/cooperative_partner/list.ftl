<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>合作伙伴 - Powered By rsico</title>
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 合作伙伴列表
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
                名称
            </th>
            <th>
                图片
            </th>
            <th>
                图片1
            </th>
            <th>
                排序
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as cooperativePartner]
        <tr>
            <td>
                <input type="checkbox" name="ids" value="${cooperativePartner.id}"/>
            </td>
            <td>
                <span title="${cooperativePartner.name}">${abbreviate(cooperativePartner.name, 50, "...")}</span>
            </td>
            <td>
                <img src="${cooperativePartner.licensePhoto}" style="width: 133px;height: 50px ">
            </td>
            <td>
                <img src="${cooperativePartner.licensePhoto1}" style="width: 133px;height: 50px ">
            </td>
            <td>
            ${cooperativePartner.order}
            </td>
            <td>
                <a href="edit/${cooperativePartner.id}.jhtml">[${message("admin.common.edit")}]</a>
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