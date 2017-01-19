[#assign shiro=JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>优惠买单 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>

</head>
<body>
<div id="trade_wrap"></div>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.order.list")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get" accept-charset="UTF-8">
    <input type="hidden" id="paymentMethod" name="paymentMethod" value="${paymentMethod}"/>
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
        </div>
        <div class="menuWrap">
            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                   value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]" placeholder="付款开始时间"
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
            -<input type="text" id="endDate" name="endDate" class="text Wdate"
                    value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]" placeholder="付款结束时间"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
        </div>
        <div class="menuWrap">
            <input type="text" id="username" name="username" class="text"
                   placeholder="会员" value="${username}"/>
        </div>
        <div class="menuWrap">
            <input type="text" id="tenantName" name="tenantName" class="text"
                   placeholder="店铺名称" value="${tenantName}"/>
        </div>
        <input class="button" value="查询" type="button" id="queryButton"/>
        <input id="export" class="button" value="导出" type="" style="width: 28px;"/>
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="createDate">创建日期</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="createDate">付款日期</a>
            </th>
            <th>
                <span>订单</span>
            </th>
            <th>
                <span>编号</span>
            </th>
            <th>
                <span>付款金额</span>
            </th>
            <th>
                <span>会员</span>
            </th>
            <th>
                <span>店铺名称</span>
            </th>
            <th>
                <span>状态</span>
            </th>
            <th>
                <span>类型</span>
            </th>
            <th>
                <span>方式</span>
            </th>
            <th>
                <span>支付方式</span>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as paybill]
        <tr [#if paybill_index>199] style="display: none;" [/#if]>
            <td>
                <input type="checkbox" name="ids" value="${paybill.id}"/>
            </td>
            <td>
            ${paybill.createDate}
            </td>
            <td>
                [#if (paybill.payment)??]
                    ${(paybill.payment.paymentDate)!"-"}
                [#else]
                    -
                [/#if]
            </td>
            <td>
                [#if (paybill.payment)??]
                    ${(paybill.payment.order.id)!"-"}
                [#else]
                    -
                [/#if]
            </td>
            <td>
            ${paybill.sn}
            </td>
            <td>
            ${currency(paybill.amount, true)}
            </td>
            <td>
            [#if paybill.member??]
                ${paybill.member.displayName}
            [#else]
            -
            [/#if]
            </td>
            <td>
            ${paybill.tenant.name}
            </td>
            <td>
            ${message("PayBill.Status." + paybill.status)}
            </td>
            <td>
            [#if paybill.type??]
                ${message("PayBill.Type." + paybill.type)}
            [#else]
                -
            [/#if]
            </td>
            <td>
                [#if paybill.payment??]
                    ${(message("Payment.Method." + paybill.payment.method)!"-")}
                [#else]
                     -
                [/#if]
            </td>
            <td>
                [#if paybill.payment??]
                ${(paybill.payment.paymentMethod)!"-"}
                [#else]
                    -
                [/#if]
            </td>
            <td>
                <a href="view.jhtml?id=${paybill.id}">[${message("admin.common.view")}]</a>
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
        $("#export").click(function(){
            //导出数据到excel
            $(".table2excel").table2excel({
                exclude: ".noExl",
                name: "优惠买单",
                filename: "优惠买单",
                fileext: ".xls",
                exclude_img: true,
                exclude_links: false,
                exclude_inputs: true
            });
        });

        var $listForm = $("#listForm");
        var $filterSelect = $("#filterSelect");
        var $filterOption = $("#filterOption a");
        var $print = $(".print");
        var $moreButton = $("#moreButton");
        var $query = $("#queryButton");

    [@flash_message /]

        // 支付方式筛选
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

        // 筛选选项
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

        // 查询选项
        $query.click(function () {
            $("#pageNumber").val("1");
            $listForm.submit();
        });

    });
</script>
</body>
</html>