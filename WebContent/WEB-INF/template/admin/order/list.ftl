[#assign shiro=JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.order.list")} - Powered By rsico</title>
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
    <input type="hidden" id="orderStatus" name="orderStatus" value="${orderStatus}"/>
    <input type="hidden" id="paymentStatus" name="paymentStatus" value="${paymentStatus}"/>
    <input type="hidden" id="shippingStatus" name="shippingStatus" value="${shippingStatus}"/>
    <input type="hidden" id="hasExpired" name="hasExpired"
           value="[#if hasExpired??]${hasExpired?string("true", "false")}[/#if]"/>
    <input type="hidden" id="areaId" name="areaId" value="${(area.id)!}"/>
    <div class="bar">
        <div class="buttonWrap">
            <!--
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
			-->
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                ${message("admin.order.statu")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="orderStatus"
                               val="unconfirmed"[#if orderStatus == "unconfirmed"]
                               class="checked"[/#if]>${message("Order.OrderStatus.unconfirmed")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="orderStatus" val="confirmed"[#if orderStatus == "confirmed"]
                               class="checked"[/#if]>${message("Order.OrderStatus.confirmed")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="orderStatus" val="completed"[#if orderStatus == "completed"]
                               class="checked"[/#if]>${message("Order.OrderStatus.completed")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="orderStatus" val="cancelled"[#if orderStatus == "cancelled"]
                               class="checked"[/#if]>${message("Order.OrderStatus.cancelled")}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <a href="javascript:;" id="moreButton" class="button">${message("admin.product.moreOption")}</a>
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
                   value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]" placeholder="创建开始时间"
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
            -<input type="text" id="endDate" name="endDate" class="text Wdate"
                    value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]" placeholder="创建结束时间"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
        </div>
        <div class="menuWrap">
            <input type="text" id="consignee" name="consignee" class="text"
                   placeholder="收货人" value="${consignee}"/>
        </div>
        <div class="menuWrap">
            <input type="text" id="searchValue" name="searchValue" class="text"
                   placeholder="订单号" value="${page.searchValue}"/>
        </div>
        <div class="menuWrap">
            <input type="text" id="tenantName" name="tenantName" class="text"
                   placeholder="店铺名称" value="${tenantName}"/>
        </div>
        <div class="menuWrap">
            <input type="text" id="userName" name="userName" class="text"
                   placeholder="会员" value="${userName}"/>
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
                <a href="javascript:;" class="sort" name="sn">${message("Order.sn")}</a>
            </th>
            <th>
                <span>${message("Order.amount")}</span>
            </th>
            <th>
                <span>${message("Order.member")}</span>
            </th>
            <th>
                <span>${message("Order.consignee")}</span>
            </th>
            <th>
                <span>店铺名称</span>
            </th>
            <th>
                <span>${message("Order.paymentMethodName")}</span>
            </th>
            <th>
                <span>${message("Order.shippingMethodName")}</span>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="orderStatus">${message("Order.orderStatus")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="paymentStatus">${message("Order.paymentStatus")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="shippingStatus">${message("Order.shippingStatus")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
            </th>
        [@shiro.hasPermission name = "admin:print"]
            <th>
                <span>${message("admin.trade.print")}</span>
            </th>
        [/@shiro.hasPermission]
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as trade]
        <tr [#if trade_index>199] style="display: none;" [/#if]>
            <td>
                <input type="checkbox" name="ids" value="${trade.order.id}"/>
            </td>
            <td>
            ${trade.order.sn}
            </td>
            <td>
            ${currency(trade.amount, true)}
            </td>
            <td>
                [#if trade.order.member??]
                ${trade.order.member.displayName}
                [#else]
                    (该用户账户不存在)
                [/#if]
            </td>
            <td>
            ${trade.order.consignee}
            </td>
            <td>
            ${trade.tenant.name}
            </td>
            <td>
            ${trade.order.paymentMethodName}
            </td>
            <td>
            ${trade.order.shippingMethodName}
            </td>
            <td>
            ${message("Order.OrderStatus." + trade.orderStatus)}
                [#if trade.order.expired]<span class="gray">(${message("admin.order.hasExpired")})</span>[/#if]
            </td>
            <td>
            ${message("Order.PaymentStatus." + trade.paymentStatus)}
            </td>
            <td>
            ${message("Order.ShippingStatus." + trade.shippingStatus)}
            </td>
            <td>
                <span title="${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}">${trade.order.createDate}</span>
            </td>
            [@shiro.hasPermission name = "admin:print"]
                <td>
                    <div class="admin_seach">
                        <strong>${message("admin.common.choose")}</strong>
                        <div class="admin_slist">
                            <ul>
                                <li><a href="javascript:;" class="print"
                                       url="../print/order.jhtml?id=${trade.order.id}">${message("admin.trade.orderPrint")}</a>
                                </li>
                                <li><a href="javascript:;" class="print"
                                       url="">${message("admin.trade.shippingPrint")}</a>
                                    <div class="admin_slist_cont">
                                        [#list order.trades as trade]
                                            <a href="javascript:;" class="print"
                                               url="../print/shipping.jhtml?id=${trade.id}">${message("admin.trade.shippingPrint")}
                                                -${trade.tenant.name}</a>
                                        [/#list]
                                    </div>
                                </li>
                                <li><a href="javascript:;" class="print"
                                       url="../print/product.jhtml?id=${trade.order.id}">${message("admin.order.productPrint")}</a>
                                </li>
                                <li><a href="javascript:;" class="print"
                                       url="../print/delivery.jhtml?orderId=${trade.order.id}">${message("admin.order.deliveryPrint")}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </td>
            [/@shiro.hasPermission]
            <td>
                <a href="view.jhtml?id=${trade.id}">[${message("admin.common.view")}]</a>

            [#--[#if !order.expired && order.orderStatus == "unconfirmed"]--]
            [#--<a href="edit.jhtml?id=${order.id}">[${message("admin.common.edit")}]</a>--]
            [#--[#else]--]
            [#--<span title="${message("admin.order.editNotAllowed")}">[${message("admin.common.edit")}]</span>--]
            [#--[/#if] --]
            </td>
        </tr>
    [/#list]
    </table>
[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
    [#include "/admin/include/pagination.ftl"]
[/@pagination]
</form>
<style type="text/css">
    .moreTable th {
        width: 80px;
        line-height: 25px;
        padding: 5px 10px 5px 0px;
        text-align: right;
        font-weight: normal;
        color: #333333;
        background-color: #f8fbff;
    }

    .moreTable td {
        line-height: 25px;
        padding: 5px;
        color: #666666;
    }

    .promotion {
        color: #cccccc;
    }
</style>
<script type="text/javascript">

    function getArealSelect() {
        var $areaId = $("#areaId2");
        $areaId.lSelect({
            url: "${base}/common/area.jhtml"
        });
    }

    $().ready(function () {
        $("#export").click(function(){
            //导出数据到excel
            $(".table2excel").table2excel({
                exclude: ".noExl",
                name: "订单",
                filename: "订单",
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

        // 查询选项
        $query.click(function () {
            $("#pageNumber").val("1");
            $listForm.submit();
        });

    [@flash_message /]

        // 更多选项
        $moreButton.click(function () {
            $.dialog({
                title: "${message("admin.product.moreOption")}",
            [@compress single_line = true]
                content: '<table id="moreTable" class="moreTable"><tr><th>地区:<\/th><td><span class="fieldSet"><input type="hidden" id="areaId2" name="areaId" value="${(area.id)!}" treePath="${(area.treePath)!}" \/><\/span><script>getArealSelect()<\/script><\/td><\/tr><\/table>',
            [/@compress]
                width: 470,
                modal: true,
                ok: "${message("admin.dialog.ok")}",
                cancel: "${message("admin.dialog.cancel")}",
                onOk: function () {
                    $("#moreTable :input").each(function () {
                        var $this = $(this);
                        $("#" + $this.attr("name")).val($this.val());
                    });
                    $("#pageNumber").val("1");
                    $listForm.submit();
                }
            });
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

        // 打印选择
        $print.on("click", function () {
            var $this = $(this);
            if ($this.attr("url") != "") {
                window.open($this.attr("url"));
            }
        });

        //配送方式
        $('.admin_seach').on('mouseover', function () {
            $('.admin_seach .admin_slist').hide();
            $(this).find('.admin_slist').show();
        });
        $('.admin_slist li').on('mouseover', function () {
            $('.admin_slist .admin_slist_cont').hide();
            $(this).find('.admin_slist_cont').show();
            $(this).find('a').addClass('down');
        });
        $('.admin_slist li').on('mouseout', function () {
            $(this).find('a').removeClass('down');
        });
        $('.admin_slist li a').on('click', function () {
            var p = $(this).text();
            $(this).parents('.admin_seach').find('strong').text(p);
        });
        $('.admin_slist_cont span').on('click', function () {
            var p = $(this).text();
            $(this).parents('.admin_seach').find('strong').text(p);
        });
        $('.admin_seach').on('mouseleave', function () {
            $('.admin_seach .admin_slist').hide();
        });
    });
</script>
</body>
</html>