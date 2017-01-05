<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>二维码 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $delete = $("#listTable a.delete");
        [@flash_message /]

            $delete.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $.ajax({
                            url: "${base}/admin/qrcode/delete.jhtml",
                            type: "POST",
                            data: {id: $this.attr("val")},
                            dataType: "json",
                            cache: false,
                            success: function (message) {
                                $.message(message);
                                if (message.type == "success") {
                                    $this.closest("tr").remove();
                                }
                            }
                        });
                    }
                });
                return false;
            });

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 二维码
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
        <a href="${base}/admin/qrcode/add.jhtml" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
        <div class="buttonWrap">
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
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"
                       style="left: 6px;"/>
                <button type="submit">&nbsp;</button>
            </div>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th>
                <span>编号</span>
            </th>
            <th>
                <span>商家</span>
            </th>
            <th>
                <span>令牌</span>
            </th>
            <th>
                <span>操作</span>
            </th>
        </tr>
    [#list page.content as qrcode]
        <tr>
            <td>
            ${qrcode.id}
            </td>
            <td>
                [#if qrcode.tenant??]
                ${qrcode.tenant.name}
                [#else]
                    -
                [/#if]
            </td>
            <td>
            ${qrcode.ticket}
            </td>
            <td>
                <a href="${base}/admin/qrcode/edit/${qrcode.id}.jhtml">绑定</a>
                <a href="javascript:;" class="delete" val="${qrcode.id}">删除</a>
                <a href="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${qrcode.ticket}" target="_blank">查看</a>
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