<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.payment.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>

    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>

</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.payment.list")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="paymentMethod" name="paymentMethod" value="${paymentMethod}"/>
    <input type="hidden" id="type" name="type" value="${type}"/>
    <div class="bar">
        <div class="buttonWrap">
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                支付方式<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="paymentMethod" val="钱包支付"[#if paymentMethod == "钱包支付"]
                               class="checked"[/#if]>钱包支付</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="paymentMethod" val="微信支付"[#if paymentMethod == "微信支付"]
                               class="checked"[/#if]>微信支付</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="paymentMethod" val="阿里支付"[#if paymentMethod == "阿里支付"]
                               class="checked"[/#if]>阿里支付</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect2" class="button">
                类型<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption2" class="check">
                        <li class="separator">
                            <a href="javascript:;" name="type" val="payment"[#if type == "payment"]
                               class="checked"[/#if]>${message("Payment.Type.payment")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="type" val="recharge"[#if type == "recharge"]
                               class="checked"[/#if]>${message("Payment.Type.recharge")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="type" val="paybill"[#if type == "paybill"]
                               class="checked"[/#if]>${message("Payment.Type.paybill")}</a>
                        </li>
                    </ul>
                </div>
            </div>
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
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 1000] class="current"[/#if] val="1000" title="(仅显示200条数据，实际导出1000条)">1000</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="menuWrap" style="margin-left:20px;">
                <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                       value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
                       onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"
                       placeholder="创建开始日期"/>
                &nbsp;-&nbsp;
                <input type="text" id="endDate" name="endDate" class="text Wdate"
                        value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
                        onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"
                        placeholder="创建结束日期"/>
            </div>
            <div class="menuWrap">
                <input type="text" id="username" name="username" class="text"
                       placeholder="会员" value="${username}"/>
            </div>
            <div class="menuWrap">
                <input type="text" id="tenantName" name="tenantName" class="text"
                       placeholder="店铺名称" value="${tenantName}"/>
            </div>
            <input type="button" value="查询" class="bar buttonWrap button" id="queryButton">
            <input id="export" class="button" value="导出" type="" style="width: 28px;"/>
        </div>
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="paymentDate">${message("Payment.paymentDate")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="order">${message("Payment.order")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="sn">${message("Payment.sn")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="amount">${message("Payment.amount")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="member">${message("Payment.member")}</a>
            </th>
            <th>
                <span>${message("Payment.tenant")}</span>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="status">${message("Payment.status")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="type">${message("Payment.type")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="method">${message("Payment.method")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="paymentMethod">${message("Payment.paymentMethod")}</a>
            </th>
            <th class="noExl">
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as payment]
        <tr [#if payment_index>199] style="display: none;" [/#if]>
            <td>
                <input type="checkbox" name="ids" value="${payment.id}"/>
            </td>

            <td>
                <span title="${payment.createDate?string("yyyy-MM-dd HH:mm:ss")}">${(payment.createDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}</span>
            </td>
            <td>
                <span title="${(payment.paymentDate?string("yyyy-MM-dd HH:mm:ss"))!}">${(payment.paymentDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}</span>
            </td>
            <td>
            ${(payment.order.sn)!"-"}&nbsp;
            </td>
            <td>
            ${payment.sn}&nbsp;
            </td>
            <td>
            ${payment.amount?string("0.00")}
            </td>
            <td>
            ${(payment.member.displayName)!"-"}
            </td>
            <td>
                [#if payment.type=='payment'&&payment.trade??]
                 ${(payment.trade.tenant.name)!"-"}
                [#elseif payment.type=='paybill']
                ${(payment.payBill.tenant.name)!"-"}
                [#elseif payment.order??]
                    [#list payment.order.trades as trade]
                    [#if payment.order.orderType=='single']
                       ${(trade.tenant.name)!"-"}
                    [#else]
                    ${(trade.tenant.name)!"-"},
                    [/#if]
                    [/#list]
                [#else]
                            -
                [/#if]
            </td>
            <td>
            ${message("Payment.Status." + payment.status)}
            </td>
            <td>
            ${message("Payment.Type." + payment.type)}
            </td>
            <td>
            ${message("Payment.Method." + payment.method)}
            </td>
            <td>
            ${payment.paymentMethod}
            </td>
            <td class="noExl">
                    <a href="view.jhtml?id=${payment.id}">[${message("admin.common.view")}]</a>
            </td>
        </tr>
    [/#list]
    </table>
[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
    [#include "/admin/include/pagination.ftl"]
[/@pagination]
</form>
<script type="text/javascript">
    $().ready(function () {

    [@flash_message /]
        var $listForm = $("#listForm");
        var $filterSelect = $("#filterSelect");
        var $filterOption = $("#filterOption a");
        var $filterSelect2 = $("#filterSelect2");
        var $filterOption2 = $("#filterOption2 a");
        var $query = $("#queryButton");
        // 查询选项
        $query.click(function () {
            $("#pageNumber").val("1");
            $listForm.submit();
        });
        // 订单筛选
        $filterSelect.mouseover(function () {
            var $this = $(this);
            var offset = $this.offset();
            var $menuWrap = $this.closest("div.menuWrap");
            var $popupMenu = $menuWrap.children("div.popupMenu");
            $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
            $menuWrap.mouseleave(function () {
                $popupMenu.hide();
            });
        });
        $filterSelect2.mouseover(function () {
            var $this = $(this);
            var offset = $this.offset();
            var $menuWrap = $this.closest("div.menuWrap");
            var $popupMenu = $menuWrap.children("div.popupMenu");
            $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
            $menuWrap.mouseleave(function () {
                $popupMenu.hide();
            });
        });
        // 支付方式选项
        $filterOption.click(function () {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            if ($this.hasClass("checked")) {
                $dest.val("");
            } else {
                $dest.val($this.attr("val"));
            }
            $("#pageNumber").val("1");
            $listForm.submit();
            return false;
        });
        // 支付状态选项
        $filterOption2.click(function () {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            if ($this.hasClass("checked")) {
                $dest.val("");
            } else {
                $dest.val($this.attr("val"));
            }
            $("#pageNumber").val("1");
            $listForm.submit();
            return false;
        });


        $("#export").click(function () {
            if(confirm("导出是当前页面数据导出，如想导出多条数据，可选择每页显示数")){
                $.message("success","正在帮您导出，请稍后");
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "${message("admin.payment.list")}",
                    filename: "${message("admin.payment.list")}",
                    fileext: ".xls",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
            }

        });
    });
</script>
</body>
</html>