<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.refunds.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>

    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">
        $().ready(function () {

        [@flash_message /]
            $("#export_ss").click(function () {
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "${message("admin.refunds.list")}",
                    filename: "${message("admin.refunds.list")}",
                    fileext: ".xls",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.refunds.list")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
        <div class="buttonWrap">
            <!--
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				-->
            <a href="javascript:;" id="export_ss" class="button">导出</a>
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
                        <a href="javascript:;"[#if page.searchProperty == "sn"] class="current"[/#if]
                           val="sn">${message("Refunds.sn")}</a>
                    </li>
                    <li>
                        <a href="javascript:;"[#if page.searchProperty == "payee"] class="current"[/#if]
                           val="payee">${message("Refunds.payee")}</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="menuWrap">
            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                   value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
            -<input type="text" id="endDate" name="endDate" class="text Wdate"
                    value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
        </div>
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>申请日期</span>
            </th>
            <th>
                <span>退款订单单号</span>
            </th>
            <th>
                <span>所属店铺</span>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="sn">${message("Refunds.sn")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="amount">${message("Refunds.amount")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="payee">${message("Refunds.payee")}</a>
            </th>
            <th>
                <span>退款银行</span>
            </th>
            <th>
                <span>退款账号</span>
            </th>
            <th>
                <span>退款状态</span>
            </th>
            <th>
            ${message("Refunds.method")}
            </th>
            <th>
                <a href="javascript:;" class="sort" name="paymentMethod">${message("Refunds.paymentMethod")}</a>
            </th>
            <th>
                <span>退款日期</span>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as refunds]
        <tr>
            <td>
                <input type="checkbox" name="ids" value="${refunds.id}"/>
            </td>
            <td>
                <span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
            </td>
            <td>
            ${refunds.order.sn}&nbsp;
            </td>
            <td>
            ${(refunds.trade.tenant.name)!"-"}&nbsp;
            </td>
            <td>
            ${refunds.sn}&nbsp;
            </td>
            <td>
            ${(refunds.amount?string("0.00"))}
            </td>
            <td>
            ${(refunds.payee)!"-"}
            </td>
            <td>
            ${(refunds.bank)!"-"}
            </td>
            <td>
            ${(refunds.account)!"-"}
            </td>
            <td>
            ${(message("Refunds.Status." +refunds.status))!"-"}
            </td>
            <td>
            ${message("Refunds.Method." + refunds.method)}
            </td>
            <td>
            ${refunds.paymentMethod}
            </td>

            <td>
                [#if refunds.createDate?string("yyyy-MM-dd HH:mm:ss") == refunds.modifyDate?string("yyyy-MM-dd HH:mm:ss")]
                    -
                [#else ]
                    <span title="${refunds.modifyDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.modifyDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                [/#if]
            </td>
            <td>
                <a href="view.jhtml?id=${refunds.id}">[${message("admin.common.view")}]</a>
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