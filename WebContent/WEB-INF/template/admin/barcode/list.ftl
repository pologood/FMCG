<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>条码库 - Powered By rsico</title>
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 条码库
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
        <a href="${base}/admin/barcode/add.jhtml" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
        <div class="buttonWrap">
            <a href="javascript:;" id="deleteButton" class="iconButton">
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
            <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"   style="left: 6px;"/>
                <button type="submit">&nbsp;</button>
            </div>
            [#--<div class="search">--]
                [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
                [#--<button type="submit">&nbsp;</button>--]
            [#--</div>--]
            [#--<div class="popupMenu">--]
                [#--<ul id="searchPropertyOption">--]
                    [#--<li>--]
                        [#--<a href="javascript:;"[#if page.searchProperty == "brand"] class="current"[/#if] val="brand">${message("Barcode.brand")}</a>--]
                    [#--</li>--]
                    [#--<li>--]
                        [#--<a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if] val="name">${message("Barcode.name")}</a>--]
                    [#--</li>--]
                [#--</ul>--]
            [#--</div>--]
        </div>

    </div>

    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
               <span>序号</span>
            </th>
            <th>
                <span>条形码</span>
            </th>
            <th>
                <span>商品名称</span>
            </th>
            <th>
               <span>品牌</span>
            </th>
            <th>
                <span>市场价</span>
            </th>
            <th>
            [#--图片--]
                <span></span>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as barcode]
        <tr>
            <td>

                <input type="checkbox" name="ids" value="${barcode.id}"/>
            </td>
            <td>
            [#--序号--]
                ${barcode_index+1}
            </td>
            <td>
                 ${barcode.barcode}
            </td>
            <td>
            [#--商品名称--]
                  ${barcode.name}
            </td>
            <td>
            [#--品牌--]
                [#if barcode??&&barcode.brand??&&barcode.brand?has_content]
                   ${barcode.brand.name}"[/#if]
            </td>
            <td>
            [#--市场价--]
                  ${barcode.outPrice}
            </td>
            <td>

            [#--图片--]
                <img src="[#if barcode.productImages[0]??&&barcode.productImages[0].source??]${barcode.productImages[0].source}[/#if]" alt="" width="30px" height="30px"/>
[#--<a>[#if barcode.productImages[0]??&&barcode.productImages[0].source??]url=${barcode.productImages[0].source}[/#if]</a>--]

            </td>
            <td>
                <a href="${base}/admin/barcode/edit/${barcode.id}.jhtml">${message("admin.common.edit")}</a>
                <a href="${base}/admin/barcode/delete/${barcode.id}.jhtml">${message("admin.common.delete")}</a>
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