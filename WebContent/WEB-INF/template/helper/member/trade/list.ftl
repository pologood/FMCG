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
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $listForm = $("#listForm");
            var $filterSelect = $("#filterSelect");
            var $filterOption = $("#filterOption a");
            var $mod_menus = $("#mod_menus a");
            var $print = '${print}';
            var $trade = "${trade}";

        [@flash_message /]

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
                $listForm.submit();
                return false;
            });

            // 筛选选项
            $mod_menus.click(function () {
                var $this = $(this);
                var roles = $this.attr("roles");
                roleRoot(function () {
                    $("#orderStatus").val("confirmed");
                    $("#paymentStatus").val("");
                    $("#shippingStatus").val("");
                    $("#hasExpired").val("");
                    var $dest = $("#" + $this.attr("name"));
                    if ($this.attr("val") == "shippedApply") {
                        $("#paymentStatus").val("refundApply");
                    }
                    if ($this.attr("val") == "unpaid") {
                        $("#orderStatus").val("");
                    }
                    $dest.val($this.attr("val"));
                    $listForm.submit();
                    return false;
                }, roles);

            });

            // 打印选择
//            $print.change(function () {
//                var $this = $(this);
//                if ($this.val() != "") {
//                    window.open($this.val());
//                }
//            });
            var $selectAll = $("#selectAll");
            var $ids = $("#listTable input[name='ids']");
            // 全选
            $selectAll.click( function() {
                var $this = $(this);
                var $enabledIds = $("#listTable input[name='ids']:enabled");
                if ($this.prop("checked")) {
                    $enabledIds.prop("checked", true);
                    if ($enabledIds.filter(":checked").size() > 0) {
                        $("#settleButton").removeClass("disabled");
                        // $deleteButtonAll.removeClass("disabled");
                        // $contentRow.addClass("selected");
                    } else {
                        $("#settleButton").addClass("disabled");
                        // $deleteButtonAll.addClass("disabled");
                    }
                } else {
                    $enabledIds.prop("checked", false);
                    $("#settleButton").addClass("disabled");
                    // $deleteButtonAll.addClass("disabled");
                    // $contentRow.removeClass("selected");
                }
            });
            
            // 选择
            $ids.click( function() {
                var $this = $(this);
                if ($this.prop("checked")) {
                    $this.closest("tr").addClass("selected");
                    $("#settleButton").removeClass("disabled");
                    // $deleteButtonAll.removeClass("disabled");
                } else {
                    $this.closest("tr").removeClass("selected");
                    if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
                        $("#settleButton").removeClass("disabled");
                        // $deleteButtonAll.removeClass("disabled");
                    } else {
                        $("#settleButton").addClass("disabled");
                        // $deleteButtonAll.addClass("disabled");
                    }
                }
            });
     
        });
        function batch_shipping(obj){
            $.dialog({
                title: "批量发货",
                [@compress single_line = true]
                content: '<table class= "input" style = "margin-bottom: 30px;">'
                           +'<tr>'
                                +'<th>配送方式:<\/th>'
                                +'<td>'
                                    +'<select name="shippingMethodId" id="shippingMethodId">'
                                        +'<option value="">${message("admin.common.choose")}<\/option>'
                                        [#list shippingMethods as shippingMethod]
                                        +'<option value="${shippingMethod.id}">${shippingMethod.name}<\/option>'
                                        [/#list]
                                    +'<\/select>'
                                +'<\/td>'
                            +'<\/tr>'
                            +'<tr>'
                                +'<th>快递公司:<\/th>'
                                +'<td>'
                                    +'<select name="deliveryCorpId" id="deliveryCorpId">' 
                                        +'<option value="">${message("admin.common.choose")}<\/option>'
                                        [#list deliveryCorps as deliveryCorp]
                                        +'<option value="${deliveryCorp.id}">${deliveryCorp.name}<\/option>'
                                        [/#list]
                                    +'<\/select>'
                               +'<\/td>'
                            +'<\/tr>'
                            +'<tr>'
                                +'<th>运单号（员）:<\/th>'
                                +'<td><input type="text" class="text" value="" id="tracking_no"\/><\/td>'
                            +'<\/tr>'
                       +'<\/table>',
                [/@compress]
                width: 400,
                modal: true,
                ok: "${message("admin.dialog.ok")}",
                cancel: "${message("admin.dialog.cancel")}",
                onOk: function () {
                    if ($(obj).hasClass("disabled")) {
                        return;
                    }
                    var ids=[];
                    $.each($("#listTable input[name='ids']"),function(i,item){
                        if($(this).attr("checked")=="checked"){
                            ids.push($(this).val());
                        }
                    });
                    $.ajax({
                        url:"${base}/helper/member/trade/batch_shipping.jhtml",
                        type:"post",
                        traditional: true,
                        data:{
                            tradeIds:ids,
                            shippingMethodId:$("#shippingMethodId").val(),
                            deliveryCorpId:$("#deliveryCorpId").val(),
                            trackingNo:$("#tracking_no").val()
                            },
                        dataType:"json",
                        success:function(message){
                            if(message.message.type=="success"){
                                location.href="${base}/helper/member/trade/list.jhtml?type=shipped"
                            }else{
                                $.message(message.message);
                            }
                        }
                    });
                }
            });
        }
    </script>
</head>
<body>


[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/my-order.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的订单</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查询及处理我销售的订单。</dd>
                    </dl>
                </div>
                &nbsp;
                <ul class="links" id="mod_menus">
                    <li>
                        <a [#if type == "unshipped"]class="on"[/#if]
                           name="type" val="unshipped" hideFocus="" style="cursor: pointer;"
                           roles="owner,manager,cashier">待发货 <font color="red">${unshippedCount!0}</font></a>
                    </li>
                    <li>
                        <a [#if type == "unpaid"]class="on"[/#if] name="type" val="unpaid" hideFocus=""
                           style="cursor: pointer;"
                           roles="owner,manager">待付款 <font color="red">${unpaidCount!0}</font></a>
                    </li>
                    <li>
                        <a [#if type == "shipped"]class="on"[/#if] name="type" style="cursor: pointer;"
                           val="shipped" hideFocus=""
                           roles="owner,manager,guide,account,cashier">已发货 <font color="red">${shippedCount!0}</font></a>
                    </li>
                    <li>
                        <a [#if type == "unreturned"]class="on"[/#if] name="type" val="unreturned" hideFocus=""
                           style="cursor: pointer;"
                           roles="owner,manager">退货中 <font color="red">${shippedApplyCount!0}</font></a>
                    </li>
                    <li>
                        <a [#if type == "completed"]class="on"[/#if] name="type" val="completed" hideFocus=""
                           style="cursor: pointer;"
                           roles="owner,manager,cashier">已完成 <font color="red">${completedCount!0}</font></a>
                    </li>
                    <li>
                        <a [#if type == "cancelled"]class="on"[/#if] name="type" val="cancelled" hideFocus=""
                           style="cursor: pointer;"
                           roles="owner,manager,cashier">已取消 <font color="red">${cancelledCount!0}</font></a>
                    </li>
                </ul>

            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <div class="bar">
                    <div class="buttonWrap">
                        [#if type=="unshipped" ]
                        <a href="javascript:;" id="settleButton" class="iconButton disabled" onclick="batch_shipping(this)">
                                批量发货
                        </a>
                        [/#if]
                        <a href="search.jhtml" class="iconButton">
                            <span class="addIcon">&nbsp;</span>提货码
                        </a>

                        <a href="javascript:;" class="iconButton" id="refreshButton">
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
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <div class="search" style="width: 220px;">
                        [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
                            <input type="text" id="keyword" name="keyword" value="${keyword}" maxlength="200"
                                   placeholder="搜索订单号、收货人、提货码"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    [#--<div class="popupMenu">--]
                    [#--<ul id="searchPropertyOption">--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if page.searchProperty == "order.sn"] class="current"[/#if]--]
                    [#--val="order.sn">${message("Order.sn")}</a>--]
                    [#--</li>--]
                    [#--</ul>--]
                    [#--</div>--]
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
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
                                <span>${message("Order.consignee")}</span>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="orderStatus">${message("Order.orderStatus")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="paymentStatus">${message("Order.paymentStatus")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="shippingStatus">${message("Order.shippingStatus")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="createDate">${message("admin.common.createDate")}</a>
                            </th>
                            <th><span>打印次数</span></th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as trade]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${trade.id}"/>
                            </td>
                            <td>
                            ${trade.order.sn}
                            </td>
                            <td>
                            ${currency(trade.amount, true)}
                            </td>
                            <td>
                            [#--${trade.order.member.username}--]
                                ${trade.order.consignee}
                            </td>
                            <td>
                            ${message("Order.OrderStatus." + trade.orderStatus)}
                                [#if trade.order.expired]<span
                                        class="gray">(${message("admin.order.hasExpired")})</span>[/#if]
                            </td>
                            <td>
                            ${message("Order.PaymentStatus." + trade.paymentStatus)}
                            </td>
                            <td>
                            ${message("Order.ShippingStatus." + trade.shippingStatus)}
                            </td>
                            <td>
                                <span title="${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}">${trade.createDate}</span>
                            </td>
                            <td>
                                ${trade.print}
                            </td>
                            <td>
                                [@helperRole url="helper/member/trade/list.jhtml" type="read"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="view.jhtml?id=${trade.id}">[${message("admin.common.view")}]</a>
                                    [/#if]
                                [/@helperRole]
                                &nbsp;&nbsp;
                                [@helperRole url="helper/member/trade/list.jhtml" type="print"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="${base}/helper/member/trade/print.jhtml?id=${trade.id}" target="_blank">[打印]</a>
                                    [/#if]
                                [/@helperRole]
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("helper.member.noResult")}</p>
                [/#if]
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/helper/include/pagination.ftl"]
                [/@pagination]
                </div>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
