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
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $rejectedForm = $("#rejectedForm");
            var $orderCloseForm = $("#orderCloseForm");
            var $shippingButton = $("#shippingButton");
            var $returnsButton = $("#returnsButton");
            var $rejectedButton = $("#rejectedButton");
            var $orderCloseButton = $("#orderCloseButton");
            var $reviewButton = $("#reviewButton");
            var $returnsSubmit = $("#returnsSubmit");
            var $returnsSubmitForm = $("#returnsSubmitForm");
            var $updatePriceButton = $("#updatePriceButton");
            var isLocked = false;
        [@flash_message /]

            // 检查锁定
            function checkLock() {
                if (!isLocked) {
                    $.ajax({
                        url: "check_lock.jhtml",
                        type: "POST",
                        data: {id: ${trade.id}},
                        dataType: "json",
                        cache: false,
                        success: function (message) {
                            if (message.type != "success") {
                                $.message(message);
                                $shippingButton.prop("disabled", true);
                                $returnsButton.prop("disabled", true);
                                $rejectedButton.prop("disabled", true);
                                $orderCloseButton.prop("disabled", true);
                                $reviewButton.prop("disabled", true);
                                $updatePriceButton.prop("disabled", true);
                                isLocked = true;
                            }
                        }
                    });
                }
            }

            // 检查锁定
            checkLock();
            setInterval(function () {
                checkLock();
            }, 10000);

            // 拒绝
            $rejectedButton.click(function () {
                $.dialog({
                    type: "warn",
                    content: "拒绝申请后将无法编辑，是否继续?",
                    onOk: function () {
                        $rejectedForm.submit();
                    },
                    ok: "${message("b2b.dialog.ok")}",
                    cancel: "${message("b2b.dialog.cancel")}"
                });
            });

            // 关闭
            $orderCloseButton.click(function () {
                //针对聚德惠月结功能
                if("${isMonthly}"=="true"){
                    $.message("warn", "今日有月结操作，不能进行此操作");
                    return;
                }
                $.dialog({
                    type: "warn",
                    content: "同意取消订单后将无法恢复，是否继续?",
                    onOk: function () {
                        $orderCloseForm.submit();
                    },
                    ok: "${message("b2b.dialog.ok")}",
                    cancel: "${message("b2b.dialog.cancel")}"
                });
            });

            // 退货
            $returnsSubmit.click(function () {
                if("${isMonthly}"=="true"){
                    $.message("warn", "今日有月结操作，不能进行此操作");
                    return;
                }
                $.dialog({
                    type: "warn",
                    content: "同意退货后将无法编辑，是否继续?",
                    onOk: function () {
                        $returnsSubmitForm.submit();
                    },
                    ok: "${message("b2b.dialog.ok")}",
                    cancel: "${message("b2b.dialog.cancel")}"
                });
            });

        [#if trade.orderItems??]
            [#list trade.orderItems as products]
                [#if products.sn==null]
                    $shippingButton.hide();
                [/#if]
            [/#list]
        [/#if]
        [#if !trade.order.expired && trade.order.orderStatus == "confirmed" && (trade.shippingStatus == "unshipped" || trade.shippingStatus == "partialShipment")]
            // 发货   ${message("Shipping.trackingNo")}
            $shippingButton.click(function () {
                if("${isMonthly}"=="true"){
                    $.message("warn", "今日有月结操作，不能进行此操作");
                    return;
                }
                $.dialog({
                    title: "${message("admin.order.shipping")}",
                    [@compress single_line = true]
                    content: '<form id= "shippingForm" action = "shipping.jhtml" method = "post">' +
                    '<input type = "hidden" name = "token" value = "${token}" \/>' +
                    '<input type = "hidden" name = "tradeId" value = "${trade.id}" \/>' +
                    '<div style = "height: 450px; overflow-x: hidden; overflow-y: auto;">' +
                    '<table class= "input" style = "margin-bottom: 30px;">' +
                    '<tr>' +
                    '<th>${message("Order.sn")}:<\/th><td width="150">${trade.order.sn}<\/td>' +
                    '<th>${message("admin.common.createDate")}:<\/th><td>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}<\/td>' +
                    '<\/tr>' +
                    '<tr>' +
                    '<th>${message("Shipping.shippingMethod")}:<\/th>' +
                    '<td>' +
                    '<select name="shippingMethodId" id="shippingMethodId" onchange="getShippingMethod()"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list shippingMethods as shippingMethod]
                        '<option value="${shippingMethod.id}" [#if trade.order.shippingMethodName==shippingMethod.name]selected="selected"[/#if]>${shippingMethod.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td>' +
                    '<th>${message("Shipping.deliveryCorp")}:<\/th>' +
                    '<td><select name="deliveryCorpId">' +
                        [#list deliveryCorps as deliveryCorp]
                        '<option value="${deliveryCorp.id}"[#if trade.order.shippingMethod?? && deliveryCorp == trade.order.shippingMethod.defaultDeliveryCorp] selected="selected"[/#if]>${deliveryCorp.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr>' +
                    '<tr [#if trade.order.shippingMethod!="F2F"]style="display:none;"[/#if] id="pick_up_id"><th>提货时间:<\/th><td><input type="text" name="pick_up_time" class="text" maxlength="200" onfocus="WdatePicker({dateFmt: &quot;yyyy-MM-dd HH:mm:ss&quot;});"\/><\/td><th><\/th><td><\/td><\/tr><tr><th>' +[#if versionType==0]"运单号"[#else ]"运单员"[/#if] +':<\/th><td><input type="text" name="trackingNo" class="text" maxlength="200" \/><\/td><th>${message("Shipping.freight")}:<\/th><td><input type="text" name="freight" class="text" maxlength="16" \/><\/td><\/tr><tr><th>${message("Shipping.consignee")}:<\/th><td><input type="text" name="consignee" class="text" value="${trade.order.consignee}" maxlength="200" \/><\/td><th>${message("Shipping.zipCode")}:<\/th><td><input type="text" name="zipCode" class="text" value="${trade.order.zipCode}" maxlength="200" \/><\/td><\/tr><tr><th>${message("Shipping.area")}:<\/th><td><span class="fieldSet"><input type="hidden" id="areaId" name="areaId" value="${(trade.order.area.id)!}" treePath="${(trade.order.area.treePath)!}" \/><\/span><\/td><th>${message("Shipping.address")}:<\/th><td><input type="text" name="address" class="text" value="${trade.order.address}" maxlength="200" \/><\/td><\/tr><tr><th>${message("Shipping.phone")}:<\/th><td><input type="text" name="phone" class="text" value="${trade.order.phone}" maxlength="200" \/><\/td><th>${message("Shipping.memo")}:<\/th><td><input type="text" name="memo" class="text" maxlength="200" \/><\/td><\/tr><\/table><table class="input"><tr class="title"><th>${message("ShippingItem.sn")}<\/th><th>${message("ShippingItem.name")}<\/th><th>${message("admin.order.productStock")}<\/th><th>${message("admin.order.productQuantity")}<\/th><th>${message("OrderItem.shippedQuantity")}<\/th><th>${message("admin.order.shippingQuantity")}<\/th><\/tr>' +
                        [#list trade.orderItems as orderItem]
                        '<tr><td><input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>${orderItem.sn}<\/td><td width="150"><span title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 50, "...")}<\/span>'+
                            [#if orderItem.isGift]
                            '<span class="red">[${message("admin.order.gift")}]<\/span>'+
                            [/#if]
                        '<\/td><td>${(orderItem.product.stock)!"-"}<\/td><td>${orderItem.quantity}<\/td><td>${orderItem.shippedQuantity}<\/td><td>'+
                            [#if orderItem.product?? && orderItem.product.stock??]
                                [#if orderItem.product.stock <= 0 || orderItem.quantity - orderItem.shippedQuantity <= 0]
                                '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text" value="0" style="width: 30px;" disabled="disabled" \/>'+
                                [#elseif orderItem.product.stock < orderItem.quantity - orderItem.shippedQuantity]
                                '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${orderItem.product.stock}" maxlength="9" style="width: 30px;" max="${orderItem.product.stock}" \/>'+
                                [#else]
                                '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${orderItem.quantity - orderItem.shippedQuantity}" maxlength="9" style="width: 30px;" max="${orderItem.quantity - orderItem.shippedQuantity}" \/>'+
                                [/#if]
                            [#else]
                            '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${orderItem.quantity - orderItem.shippedQuantity}" maxlength="9"  style="width: 30px;" max="${orderItem.quantity - orderItem.shippedQuantity}" \/>'+
                            [/#if]
                        '<\/td><\/tr>'+
                        [/#list]
                    '<tr><td colspan="6" style="border-bottom: none;">&nbsp;<\/td><\/tr><\/table><\/div><\/form>',
                    [/@compress]
                    width: 700,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onShow: function () {
                        $("#areaId").lSelect({
                            url: "${base}/common/area.jhtml"
                        });
                        $.validator.addClassRules({
                            shippingItemsQuantity: {
                                required: true,
                                digits: true
                            }
                        });
                        $("#shippingForm").validate({
                            rules: {
                                shippingMethodId: "required",
                                deliveryCorpId: "required",
                                freight: {
                                    min: 0,
                                    decimal: {
                                        integer: 12,
                                        fraction: ${setting.priceScale}
                                    }
                                },
                                consignee: "required",
                                zipCode: "required",
                                areaId: "required",
                                address: "required",
                                //pick_up_time:"required",
                                phone: "required"
                            }
                        });
                        //首次加载判断是否是到店提货
                        getShippingMethod();
                    },
                    onOk: function () {
                        var total = 0;
                        $("#shippingForm input.shippingItemsQuantity").each(function () {
                            var quantity = $(this).val();
                            if ($.isNumeric(quantity)) {
                                total += parseInt(quantity);
                            }
                        });
                        if (total <= 0) {
                            $.message("warn", "${message("admin.order.shippingQuantityPositive")}");
                        } else {
                            $("#shippingForm").submit();
                        }
                        return false;
                    }
                });
            });
        [/#if]
            //调价
            $("#updatePriceButton").click(function () {
                $.dialog({
                    title: "调价",
                [@compress single_line = true]
                    content: '<table class= "input" style = "margin-bottom: 30px;"><tr><th>订单金额:<\/th><td><input type="text" id="adjAmount" class="text" value="${trade.amount}"\/><\/td><\/tr><tr><th>运费:<\/th><td><input type="text" id="adjFreight" class="text" value="${trade.freight}"\/><\/td><\/tr><\/table>',
                [/@compress]
                    width: 400,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function () {
                        $.ajax({
                            url: "update_price.jhtml",
                            type: "POST",
                            data: {tradeId:${trade.id}, amount: $("#adjAmount").val(), freight: $("#adjFreight").val()},
                            dataType: "json",
                            cache: false,
                            success: function (message) {
                                if (message.type != "success") {
                                    $.message(message);
                                    setTimeout(function () {
                                        location.href = "${base}/helper/member/trade/view.jhtml?id=${trade.id}";
                                        return false;
                                    }, 1000);
                                } else {
                                    setTimeout(function () {
                                        window.location.reload();//刷新页面
                                        return false;
                                    }, 1000);
                                }
                            }
                        });
                    }
                });
            });
        });
        function getShippingMethod() {
            if ($("#shippingMethodId").val() == 3) {
                $("#pick_up_id").show();
            } else {
                $("#pick_up_id").hide();
            }
        }
        function cancel_trade(obj) {
            if("${isMonthly}"=="true"){
                $.message("warn", "今日有月结操作，不能进行此操作");
                return;
            }
            if (confirm("确定要取消订单吗？")) {
            	$("#orderCloseForm").submit();
                // $.ajax({
                //     url: "${base}/helper/member/trade/cancel_trade.jhtml",
                //     type: "post",
                //     data: {tradeId: obj},
                //     dataType: "json",
                //     success: function (data) {
                //         $.message(data);
                //         if (data.type == "success") {
                //             location.reload();
                //         }
                //     }
                // });
            }
        }
    </script>
</head>
<body>
[#if (trade.order.paymentMethod.method=='online'&&trade.paymentStatus=='unpaid')||(trade.order.paymentMethod.method=='offline'&&trade.paymentStatus=='unpaid'&&trade.shippingStatus=='unshipped')]
<form id="orderCloseForm" action="close.jhtml" method="post">
    <input type="hidden" name="id" value="${trade.id}"/>
</form>
[#elseif (trade.order.paymentMethod.method=='offline'&&trade.shippingStatus=='shipped')||(trade.order.paymentMethod.method=='online'&&trade.paymentStatus=='paid')||(trade.order.paymentMethod.method=='online'&&trade.shippingStatus=='shipped')]
<form id="orderCloseForm" action="returns.jhtml" method="post">
    <input type="hidden" name="tradeId" value="${trade.id}"/>
</form>
[/#if]

<form id="rejectedForm" action="rejected.jhtml" method="post">
    <input type="hidden" name="id" value="${trade.id}"/>
</form>
<form id="returnsSubmitForm" action="returns.jhtml" method="post">
    <input type="hidden" name="tradeId" value="${trade.id}"/>
</form>
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
                        <dd class="app-intro" id="app_desc">查询及管理我销售的订单并对订单进行发货、退货处理。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/trade/list.jhtml">我的订单</a></li>
                </ul>

            </div>
            <ul id="tab" class="tab" style="padding-left:85px;">
                <li>
                    <input type="button" value="${message("admin.order.orderInfo")}"/>
                </li>
                <li>
                    <input type="button" value="${message("admin.order.productInfo")}"/>
                </li>
                <li>
                    <input type="button" value="${message("admin.order.paymentInfo")}"/>
                </li>
                <li>
                    <input type="button" value="${message("admin.order.shippingInfo")}"/>
                </li>
                <li>
                    <input type="button" value="${message("admin.order.refundsInfo")}"/>
                </li>
                <li>
                    <input type="button" value="${message("admin.order.returnsInfo")}"/>
                </li>
                <li>
                    <input type="button" value="${message("admin.order.orderLog")}"/>
                </li>
                <li>
                    <input type="button" value="优惠券信息"/>
                </li>
            </ul>
            <div class="list" style="margin-top:0px;border-right: 1px solid #D8DEEA">
                <table class="input tabContent">
                    <tr>
                        <td>
                            &nbsp;
                        </td>
                    [#if trade.order.orderStatus == "unconfirmed"]
                        <td>
                            [@helperRole url="helper/member/trade/list.jhtml" type="modifyPrice"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" id="updatePriceButton" class="button" value="调价"
                                        [#if trade.order.expired] disabled="disabled" [/#if]/>
                                [/#if]
                            [/@helperRole]
                        </td>
                        <td>
                            &nbsp;
                        </td>
                        <td>
                            [@helperRole url="helper/member/trade/list.jhtml" type="cancelOrder"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" id="orderCloseButton" class="button" value="取消订单"/>
                                [/#if]
                            [/@helperRole]
                        </td>
                    [#elseif isReturnAllowed!=null]
                        <td>
                            [@helperRole url="helper/member/trade/list.jhtml" type="agreeReturn"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" id="returnsSubmit" class="button" value="同意退货"/>
                                [/#if]
                            [/@helperRole]
                        </td>
                        <td>
                            &nbsp;
                        </td>
                        <td>

                            [@helperRole url="helper/member/trade/list.jhtml" type="refuseReturn"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" id="rejectedButton" class="button" value="拒绝退货"/>
                                [/#if]
                            [/@helperRole]
                        </td>
                    [#else]
                        <td>

                            [@helperRole url="helper/member/trade/list.jhtml" type="sendGoods"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" id="shippingButton" class="button"
                                           value="发货"[#if trade.order.paymentStatus=="refundapply"||trade.order.expired || trade.order.orderStatus != "confirmed" || (trade.shippingStatus != "unshipped" && trade.shippingStatus != "partialShipment")||trade.orderStatus=="completed"]
                                           disabled="disabled"[/#if]/>
                                [/#if]
                            [/@helperRole]
                        </td>
                        <td>

                            [@helperRole url="helper/member/trade/list.jhtml" type="cancelOrder"]
                                [#if helperRole.retOper!="0"]
                                    [#if trade.orderStatus=="uncomfirmed"||trade.orderStatus=="confirmed"]
                                        <input type="button" id="cancelButton" class="button"
                                               value="取消订单" onclick="cancel_trade(${trade.id})"/>
                                    [/#if]
                                [/#if]
                            [/@helperRole]

                        </td>
                        <td>
                            &nbsp;
                        </td>
                        <td>
                            &nbsp;
                        </td>
                    [/#if]
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.sn")}:
                        </th>
                        <td width="260">
                        ${trade.order.sn}
                        </td>
                        <th>
                        ${message("admin.common.createDate")}:
                        </th>
                        <td>
                        ${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.orderStatus")}:
                        </th>
                        <td>
                        ${message("Order.OrderStatus." + trade.orderStatus)}
                        [#if trade.order.expired]
                            <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("admin.order.hasExpired")})</span>
                        [#elseif trade.order.expire??]
                            <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
                        [/#if]
                        </td>
                        <th>
                        ${message("Order.paymentStatus")}:
                        </th>
                        <td>
                        ${message("Order.PaymentStatus." + trade.paymentStatus)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.shippingStatus")}:
                        </th>
                        <td>
                        ${message("Order.ShippingStatus." + trade.shippingStatus)}
                        </td>
                        <th>
                        ${message("Order.paymentMethod")}:
                        </th>
                        <td>
                        ${trade.order.paymentMethodName}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            订单总价:
                        </th>
                        <td>
                        ${currency(trade.amount, true)}
                        </td>
                        <th>
                        ${message("Order.offsetAmount")}:
                        </th>
                        <td>
                        ${currency(trade.offsetAmount, true)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            商家折扣:
                        </th>
                        <td>
                            ${currency(trade.couponDiscount, true)}
                        </td>
                        <th>
                            平台折扣:
                        </th>
                        <td>
                            ${currency(trade.discount, true)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.weight")}:
                        </th>
                        <td>
                        ${trade.weight}
                        </td>
                        <th>
                        ${message("Order.quantity")}:
                        </th>
                        <td>
                        ${trade.quantity}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.freight")}:
                        </th>
                        <td>
                        ${currency(trade.freight, true)}
                        </td>
                        <th>
                            佣金:
                        </th>
                        <td>
                        ${currency(trade.totalProfit+trade.agencyAmount, true)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Member.username")}:
                        </th>
                        <td>
                        ${trade.order.member.username}
                        </td>
                        <th>
                        ${message("Order.shippingMethod")}:
                        </th>
                        <td>
                        ${trade.order.shippingMethodName} (提货码:${trade.sn})
                        </td>
                    </tr>
                [#if trade.order.isInvoice]
                    <tr>
                        <th>
                        ${message("Order.invoiceTitle")}:
                        </th>
                        <td>
                        ${trade.order.invoiceTitle}
                        </td>
                        <th>
                        ${message("Order.tax")}:
                        </th>
                        <td>
                        ${currency(trade.tax, true)}
                        </td>
                    </tr>
                [/#if]
                    <tr>
                        <th>
                        ${message("Order.consignee")}:
                        </th>
                        <td>
                        ${trade.order.consignee}
                        </td>
                        <th>
                        ${message("Order.area")}:
                        </th>
                        <td>
                        ${trade.order.areaName}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.address")}:
                        </th>
                        <td>
                        ${trade.order.address}
                        </td>
                        <th>
                        ${message("Order.zipCode")}:
                        </th>
                        <td>
                        ${trade.order.zipCode}
                        </td>
                    </tr>
                    <tr>
                        <th>
                        ${message("Order.phone")}:
                        </th>
                        <td>
                        ${trade.order.phone}
                        </td>
                        <th>
                        ${message("Order.memo")}:
                        </th>
                        <td>
                        ${trade.memo}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            提货时间:
                        </th>
                        <td>

                        </td>
                        <th>

                        </th>
                        <td>

                        </td>
                    </tr>
                </table>
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        ${message("OrderItem.sn")}
                        </th>
                        <th>
                        ${message("OrderItem.name")}
                        </th>
                        <th>
                        ${message("OrderItem.price")}
                        </th>
                        <th>
                        ${message("OrderItem.quantity")}
                        </th>
                        <th>
                        ${message("OrderItem.shippedQuantity")}
                        </th>
                        <th>
                        ${message("OrderItem.returnQuantity")}
                        </th>
                        <th>
                        ${message("OrderItem.subtotal")}
                        </th>
                    </tr>
                [#list trade.orderItems as orderItem]
                    <tr>
                        <td>
                        ${orderItem.sn}
                        </td>
                        <td width="400">
                            <span title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 50, "...")}</span>
                            [#if orderItem.isGift]
                                <span class="red">[${message("admin.order.gift")}]</span>
                            [/#if]
                        </td>
                        <td>
                            [#if !orderItem.isGift]
                            ${currency(orderItem.price, true)}
                            [#else]
                                -
                            [/#if]
                        </td>
                        <td>
                        ${orderItem.quantity}
                        </td>
                        <td>
                        ${orderItem.shippedQuantity}
                        </td>
                        <td>
                        ${orderItem.returnQuantity}
                        </td>
                        <td>
                            [#if !orderItem.isGift]
                            ${currency(orderItem.subtotal, true)}
                            [#else]
                                -
                            [/#if]
                        </td>
                    </tr>
                [/#list]
                </table>
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        ${message("Payment.sn")}
                        </th>
                        <th>
                        ${message("Payment.method")}
                        </th>
                        <th>
                        ${message("Payment.paymentMethod")}
                        </th>
                        <th>
                        ${message("Payment.amount")}
                        </th>
                        <th>
                        ${message("Payment.status")}
                        </th>
                        <th>
                        ${message("Payment.paymentDate")}
                        </th>
                    </tr>
                [#list trade.order.payments as payment]
                    <tr>
                        <td>
                        ${payment.sn}
                        </td>
                        <td>
                        ${message("Payment.Method." + payment.method)}
                        </td>
                        <td>
                        ${(payment.paymentMethod)!"-"}
                        </td>
                        <td>
                        ${currency(payment.amount, true)}
                        </td>
                        <td>
                        ${message("Payment.Status." + payment.status)}
                        </td>
                        <td>
                            [#if payment.paymentDate??]
                                <span title="${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                            [#else]
                                -
                            [/#if]
                        </td>
                    </tr>
                [/#list]
                </table>
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        ${message("Shipping.sn")}
                        </th>
                        <th>
                        ${message("Shipping.shippingMethod")}
                        </th>
                        <th>
                        ${message("Shipping.deliveryCorp")}
                        </th>
                        <th>
                        ${message("Shipping.trackingNo")}
                        </th>
                        <th>
                        ${message("Shipping.consignee")}
                        </th>
                        <th>
                        ${message("admin.common.createDate")}
                        </th>
                    </tr>
                [#list trade.shippings as shipping]
                    <tr>
                        <td>
                        ${shipping.sn}
                        </td>
                        <td>
                        ${shipping.shippingMethod}
                        </td>
                        <td>
                        ${shipping.deliveryCorp}
                        </td>
                        <td>
                        ${(shipping.trackingNo)!"-"}
                        </td>
                        <td>
                        ${shipping.consignee}
                        </td>
                        <td>
                            <span title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                        </td>
                    </tr>
                [/#list]
                </table>
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        ${message("Refunds.sn")}
                        </th>
                        <th>
                        ${message("Refunds.method")}
                        </th>
                        <th>
                        ${message("Refunds.paymentMethod")}
                        </th>
                        <th>
                        ${message("Refunds.amount")}
                        </th>
                        <th>
                        ${message("admin.common.createDate")}
                        </th>
                        <th>
                            操作
                        </th>
                    </tr>
                [#list trade.order.refunds as refunds]
                    <tr>
                        <td>
                        ${refunds.sn}
                        </td>
                        <td>
                        ${message("Refunds.Method." + refunds.method)}
                        </td>
                        <td>
                        ${refunds.paymentMethod!"-"}
                        </td>
                        <td>
                        ${currency(refunds.amount, true)}
                        </td>
                        <td>
                            <span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                        </td>
                        <td>
                            [#if refunds.status=="uncomfirm"]
                                <input type="hidden" id="refundsId" value="${refunds.id}"/>
                                <input type="hidden" id="refundsAmount" value="${refunds.amount}"/>
                                <input type="hidden" id="refundsMemo" value="${refunds.memo}"/>
                                <input type="hidden" id="refundsPayee" value="${refunds.payee}"/>
                                <input type="button" class="button" value="修改" id="refundsButton"/>
                            [/#if]
                        </td>
                    </tr>
                [/#list]
                </table>
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        ${message("Returns.sn")}
                        </th>
                        <th>
                        ${message("Returns.shippingMethod")}
                        </th>
                        <th>
                        ${message("Returns.deliveryCorp")}
                        </th>
                        <th>
                        ${message("Returns.trackingNo")}
                        </th>
                        <th>
                        ${message("Returns.shipper")}
                        </th>
                        <th>
                        ${message("admin.common.createDate")}
                        </th>
                    </tr>
                [#list trade.returns as returns]
                    <tr>
                        <td>
                        ${returns.sn}
                        </td>
                        <td>
                        ${(returns.shippingMethod)!"-"}
                        </td>
                        <td>
                        ${(returns.deliveryCorp)!"-"}
                        </td>
                        <td>
                        ${(returns.trackingNo)!"-"}
                        </td>
                        <td>
                        ${returns.shipper}
                        </td>
                        <td>
                            <span title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                        </td>
                    </tr>
                [/#list]
                </table>
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        ${message("OrderLog.type")}
                        </th>
                        <th>
                        ${message("OrderLog.operator")}
                        </th>
                        <th>
                        ${message("OrderLog.content")}
                        </th>
                        <th>
                        ${message("admin.common.createDate")}
                        </th>
                    </tr>
                [#list trade.order.orderLogs as orderLog]
                    <tr>
                        <td>
                        ${message("OrderLog.Type." + orderLog.type)}
                        </td>
                        <td>
                            [#if orderLog.operator??]
                            ${orderLog.operator}
                            [#else]
                                <span class="green">${message("admin.order.member")}</span>
                            [/#if]
                        </td>
                        <td>
                            [#if orderLog.content??]
                                <span title="${orderLog.content}">${abbreviate(orderLog.content, 50, "...")}</span>
                            [/#if]
                        </td>
                        <td>
                            <span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                        </td>
                    </tr>
                [/#list]
                </table>
                [#if trade.tenantCouponCode??]
                <table class="input tabContent">
                    <tr class="title">
                        <th>
                        优惠券
                        </th>
                        <th>
                        金额
                        </th>
                        <th>
                        是否已使用
                        </th>
                        <th>
                        状态
                        </th>
                        <th>
                        使用者
                        </th>
                    </tr>
                    
                    <tr>
                        <td>
                        ${trade.tenantCouponCode.coupon.name}
                        </td>
                        <td>
                        ${trade.tenantCouponCode.coupon.amount}
                        </td>
                        <td>
                        [#if trade.tenantCouponCode.isUsed=="true"]
                        已使用
                        [#else]
                        未使用
                        [/#if]
                        </td>
                        <td>
                            [#if trade.tenantCouponCode.coupon.status=="canUse"]
                            可领用
                            [#elseif trade.tenantCouponCode.coupon.status=="unBegin"]
                            为开始
                            [#elseif trade.tenantCouponCode.coupon.status=="unUsed"]
                            已领完
                            [#elseif trade.tenantCouponCode.coupon.status=="Expired"]
                            已过期
                            [/#if]
                        </td>
                        <td>
                        [#if trade.tenantCouponCode.member??]
                        ${trade.tenantCouponCode.member.username}
                        [#else]
                            --
                        [/#if]
                        </td>
                    </tr>
                </table>
                [/#if]
                <table class="input">
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td>
                            <input type="button" class="button" value="${message("admin.common.back")}"
                                   onclick="javascript:history.back();"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
