<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.order.view")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $confirmForm = $("#confirmForm");
            var $confirmRefundapplyForm = $("#confirmRefundapplyForm");
            var $completeForm = $("#completeForm");
            var $cancelForm = $("#cancelForm");
            var $confirmButton = $("#confirmButton");
            var $paymentButton = $("#paymentButton");
            var $shippingButton = $("#shippingButton");
            var $completeButton = $("#completeButton");
            var $refundsButton = $("#refundsButton");
            var $returnsButton = $("#returnsButton");
            var $cancelButton = $("#cancelButton");
            var isLocked = false;

        [@flash_message /]

            // 检查锁定
            function checkLock() {
                if (!isLocked) {
                    $.ajax({
                        url: "check_lock.jhtml",
                        type: "POST",
                        data: {id: ${trade.order.id}},
                        dataType: "json",
                        cache: false,
                        success: function (message) {
                            if (message.type != "success") {
                                $.message(message);
                                $confirmButton.prop("disabled", true);
                                $paymentButton.prop("disabled", true);
                                $shippingButton.prop("disabled", true);
                                $completeButton.prop("disabled", true);
                                $refundsButton.prop("disabled", true);
                                $returnsButton.prop("disabled", true);
                                $cancelButton.prop("disabled", true);
                                isLocked = true;
                            }
                        }
                    });
                }
            }

            // 确认
            $confirmButton.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.order.confirmDialog")}",
                    onOk: function () {
                        $confirmForm.submit();
                    }
                });
            });

            // 完成
            $completeButton.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.order.completeDialog")}",
                    onOk: function () {
                        $completeForm.submit();
                    }
                });
            });

            // 取消
            $cancelButton.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.order.cancelDialog")}",
                    onOk: function () {
                        $cancelForm.submit();
                    }
                });
            });

        [#if  (trade.order.orderStatus == "confirmed"|| trade.order.orderStatus == "completed") && (trade.order.paymentStatus == "unpaid")]
            // 支付
            $paymentButton.click(function () {
                $.dialog({
                    title: "${message("admin.order.payment")}",
                    [@compress single_line = true]
                        content: '<form id="paymentForm" action="payment.jhtml" method="post">' +
                        '<input type="hidden" name="token" value="${token}" \/>' +
                        '<input type="hidden" name="orderId" value="${trade.order.id}" \/>' +
                        '<table class="input">' +
                        '<tr>' +
                        '<th>${message("Order.sn")}: <\/th>' +
                        '<td width="300">${trade.order.sn}<\/td>' +
                        '<th>${message("admin.common.createDate")}:<\/th>' +
                        '<td>${trade.order.createDate?string("yyyy-MM-dd HH:mm:ss")}<\/td>' +
                        '<\/tr>' +
                        '<tr>' +
                        '<th>${message("Order.amount")}:<\/th>' +
                        '<td>${currency(trade.order.amount, true)}<\/td>' +
                        '<th>${message("Order.amountPaid")}:<\/th>' +
                        '<td>${currency(trade.order.amountPaid, true)}<\/td>' +
                        '<\/tr>' +
                            [#if trade.order.isInvoice]
                            '<tr>'+
                            '<th>${message("Order.invoiceTitle")}:<\/th>'+
                            '<td>${trade.order.invoiceTitle}<\/td>'+
                            '<th>${message("Order.tax")}:<\/th>'+
                            '<td>${currency(trade.order.tax, true)}<\/td>'+
                            '<\/tr>'+
                            [/#if]
                        '<tr>' +
                        '<th>${message("Payment.bank")}:<\/th>' +
                        '<td><input type="text" name="bank" class="text" maxlength="200" \/><\/td>' +
                        '<th>${message("Payment.account")}:<\/th>' +
                        '<td><input type="text" name="account" class="text" maxlength="200" \/><\/td>' +
                        '<\/tr>' +
                        '<tr>' +
                        '<th>${message("Payment.amount")}:<\/th>' +
                        '<td><input type="text" name="amount" class="text" value="${trade.order.amountPayable}" maxlength="16" readonly \/><\/td>' +
                        '<th>${message("Payment.payer")}:<\/th>' +
                        '<td><input type="text" name="payer" class="text" maxlength="200" \/><\/td>' +
                        '<\/tr>' +
                        '<tr>' +
                        '<th>${message("Payment.method")}:<\/th>' +
                        '<td>' +
                        '<select id="method" name="method">' +
                            [#list methods as method]
                            '<option value="${method}">${message("Payment.Method." + method)}<\/option>'+
                            [/#list]
                        '<\/select>' +
                        '<\/td>' +
                        '<th>${message("Payment.paymentMethod")}:<\/th>' +
                        '<td>' +
                        '<select name="paymentMethodId">' +
                        '<option value="">${message("admin.common.choose")}<\/option>' +
                            [#list paymentMethods as paymentMethod]
                            '<option value="${paymentMethod.id}">${paymentMethod.name}<\/option>'+
                            [/#list]
                        '<\/select>' +
                        '<\/td>' +
                        '<\/tr>' +
                        '<tr>' +
                        '<th>${message("Payment.memo")}:<\/th>' +
                        '<td colspan="3"><input type="text" name="memo" class="text" maxlength="200" \/><\/td>' +
                        '<\/tr>' +
                        '<tr>' +
                        '<td colspan="4" style="border-bottom: none;">&nbsp;<\/td>' +
                        '<\/tr>' +
                        '<\/table>' +
                        '<\/form>',
                    [/@compress]
                    width: 900,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onShow: function () {
                        var $method = $("#method");
                        $.validator.addMethod("balance",
                                function (value, element, param) {
                                    return this.optional(element) || $method.val() != "deposit" || parseFloat(value) <= parseFloat(param);
                                },
                                "${message("admin.order.insufficientBalance")}"
                        );
                        $("#paymentForm").validate({
                            rules: {
                                amount: {
                                    required: true,
                                    positive: true,
                                    max: ${trade.order.amountPayable},
                                    decimal: {
                                        integer: 12,
                                        fraction: ${setting.priceScale}
                                    },
                                    balance: ${trade.order.member.balance}
                                },
                                bank: {
                                    required: true
                                },
                                account: {
                                    required: true,
                                    creditcard: true
                                },
                                payer: {
                                    required: true
                                }
                            },
                            messages: {
                                account: {
                                    required: '必填',
                                    creditcard: '卡号不正确'
                                }
                            }
                        });
                    },
                    onOk: function () {
                        $("#paymentForm").submit();
                        return false;
                    }
                });
            });
        [/#if]

        [#if !trade.order.expired && trade.order.orderStatus == "confirmed" && (trade.order.paymentStatus == "paid" || trade.order.paymentStatus == "partialPayment" || trade.order.paymentStatus == "partialRefunds" || order.paymentStatus == "refundapply")]
            // 退款
            $refundsButton.click(function () {
                var paymentStatus = $(this).attr("paymentStatus");
                if ("refundapply" == paymentStatus) {
                    $.dialog({
                        type: "warn",
                        content: "确认该订单的退款申请？",
                        onOk: function () {
                            $confirmRefundapplyForm.submit();
                        }
                    });
                } else {
                    $.dialog({
                        title: "${message("admin.order.refunds")}",
                        [@compress single_line = true]
                            content: '<form id= "refundsForm" action = "refunds.jhtml" method = "post">' +
                            '<input type = "hidden" name = "token" value = "${token}" \/>' +
                            '<input type = "hidden" name = "orderId" value = "${trade.order.id}" \/>' +
                            '<input type = "hidden" name = "status" value = "comfirm" \/>' +
                            '<table class= "input" >' +
                            '<tr><th>${message("Order.sn")}: <\/th>' +
                            '<td width="300">${trade.order.sn} <\/td>' +
                            '<th>${message("admin.common.createDate")}: <\/th>' +
                            '<td>${trade.order.createDate?string("yyyy-MM-dd HH:mm:ss")}<\/td><\/tr>' +
                            '<tr><th>${message("Order.amount")}: <\/th>' +
                            '<td>${currency(trade.order.amount, true)}<\/td>' +
                            '<th>${message("Order.amountPaid")}:<\/th>' +
                            '<td>${currency(trade.order.amountPaid, true)}<\/td><\/tr>' +
                                [#if trade.order.isInvoice]
                                '<tr><th>${message("Order.invoiceTitle")}: <\/th>'+
                                '<td>${trade.order.invoiceTitle}<\/td>'+
                                '<th>${message("Order.tax")}:<\/th><td>${currency(trade.order.tax, true)}<\/td><\/tr>'+
                                [/#if]
                            '<tr><th>${message("Refunds.bank")}:<\/th><td><input type="text" name="bank" class="text" maxlength="200"\/><\/td><th>${message("Refunds.account")}:<\/th><td><input type="text" name="account" class="text" maxlength="200"\/><\/td><\/tr><tr>' +
                            '<th>${message("Refunds.amount")}:<\/th><td><input type="text" name="amount" class="text" value="${trade.order.amountPaid}" maxlength="16"\/><\/td>' +
                            '<th>${message("Refunds.payee")}:<\/th><td><input type="text" name="payee" class="text" maxlength="200"\/><\/td><\/tr>' +
                            '<tr><th>${message("Refunds.method")}:<\/th>' +
                            '<td><select name="method">' +
                                [#list refundsMethods as refundsMethod]
                                '<option value="${refundsMethod}">${message("Refunds.Method." + refundsMethod)}<\/option>'+
                                [/#list]
                            '<\/select><\/td>' +
                            '<th>${message("Refunds.paymentMethod")}:<\/th><td>' +
                            '<select name="paymentMethodId">' +
                            '<option value="">${message("admin.common.choose")}<\/option>' +
                                [#list paymentMethods as paymentMethod]
                                '<option value="${paymentMethod.id}">${paymentMethod.name}<\/option>'+
                                [/#list]
                            '<\/select><\/td><\/tr>' +
                            '<tr><th>${message("Refunds.memo")}:<\/th>' +
                            '<td colspan="3"><input type="text" name="memo" class="text" maxlength="200"\/><\/td><\/tr><tr><td colspan="4" style="border-bottom: none;">&nbsp;<\/td><\/tr><\/table><\/form>',
                        [/@compress]
                        width: 900,
                        modal: true,
                        ok: "${message("admin.dialog.ok")}",
                        cancel: "${message("admin.dialog.cancel")}",
                        onShow: function () {
                            $("#refundsForm").validate({
                                rules: {
                                    amount: {
                                        required: true,
                                        positive: true,
                                        max: ${trade.order.amountPaid},
                                        decimal: {
                                            integer: 12,
                                            fraction: ${setting.priceScale}
                                        }
                                    }
                                }
                            });
                        },
                        onOk: function () {
                            $("#refundsForm").submit();
                            return false;
                        }
                    });
                }
            });
        [/#if]

        [#if !trade.order.expired && trade.order.orderStatus == "confirmed" && (trade.order.shippingStatus == "unshipped" || trade.order.shippingStatus == "partialShipment")]
            // 发货
            $shippingButton.click(function () {
                $.dialog({
                    title: "${message("admin.order.shipping")}",
                    [@compress single_line = true]
                        content: '<form id= "shippingForm" action = "shipping.jhtml" method = "post"><input type = "hidden" name = "token" value = "${token}" \/><input type = "hidden" name = "orderId" value = "${trade.order.id}" \/><div style = "height: 380px; overflow-x: hidden; overflow-y: auto;"><table class= "input" style = "margin-bottom: 30px;"><tr><th>${message("Order.sn")}:<\/th><td width="300">${trade.order.sn}<\/td><th>${message("admin.common.createDate")}:<\/th><td>${trade.order.createDate?string("yyyy-MM-dd HH:mm:ss")}<\/td><\/tr><tr><th>${message("Shipping.shippingMethod")}:<\/th><td><select name="shippingMethodId"><option value="">${message("admin.common.choose")}<\/option>' +
                            [#list shippingMethods as shippingMethod]
                            '<option value="${shippingMethod.id}"[#if shippingMethod == trade.order.shippingMethod] selected="selected"[/#if]>${shippingMethod.name}<\/option>'+
                            [/#list]
                        '<\/select><\/td><th>${message("Shipping.deliveryCorp")}:<\/th><td><select name="deliveryCorpId">' +
                            [#list deliveryCorps as deliveryCorp]
                            '<option value="${deliveryCorp.id}"[#if trade.order.shippingMethod?? && deliveryCorp == trade.order.shippingMethod.defaultDeliveryCorp] selected="selected"[/#if]>${deliveryCorp.name}<\/option>'+
                            [/#list]
                        '<\/select><\/td><\/tr><tr><th>${message("Shipping.trackingNo")}:<\/th><td><input type="text" name="trackingNo" class="text" maxlength="200"\/><\/td><th>${message("Shipping.freight")}:<\/th><td><input type="text" name="freight" class="text" maxlength="16"\/><\/td><\/tr><tr><th>${message("Shipping.consignee")}:<\/th><td><input type="text" name="consignee" class="text" value="${trade.order.consignee}" maxlength="200"\/><\/td><th>${message("Shipping.zipCode")}:<\/th><td><input type="text" name="zipCode" class="text" value="${trade.order.zipCode}" maxlength="200"\/><\/td><\/tr><tr><th>${message("Shipping.area")}:<\/th><td><span class="fieldSet"><input type="hidden" id="areaId" name="areaId" value="${(trade.order.area.id)!}" treePath="${(trade.order.area.treePath)!}"\/><\/span><\/td><th>${message("Shipping.address")}:<\/th><td><input type="text" name="address" class="text" value="${trade.order.address}" maxlength="200"\/><\/td><\/tr><tr><th>${message("Shipping.phone")}:<\/th><td><input type="text" name="phone" class="text" value="${trade.order.phone}" maxlength="200"\/><\/td><th>${message("Shipping.memo")}:<\/th><td><input type="text" name="memo" class="text" maxlength="200"\/><\/td><\/tr><\/table><table class="input"><tr class="title"><th>${message("ShippingItem.sn")}<\/th><th>${message("ShippingItem.name")}<\/th><th>${message("admin.order.productStock")}<\/th><th>${message("admin.order.productQuantity")}<\/th><th>${message("OrderItem.shippedQuantity")}<\/th><th>${message("admin.order.shippingQuantity")}<\/th><\/tr>' +
                            [#list trade.order.orderItems as orderItem]
                            '<tr><td><input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}"\/>${orderItem.sn}<\/td><td width="300"><span title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 50, "...")}<\/span>'+
                                [#if orderItem.isGift]
                                '<span class="red">[${message("admin.order.gift")}]<\/span>'+
                                [/#if]
                            '<\/td><td>${(orderItem.product.stock)!"-"}<\/td><td>${orderItem.quantity}<\/td><td>${orderItem.shippedQuantity}<\/td><td>'+
                                [#if orderItem.product?? && orderItem.product.stock??]
                                    [#if orderItem.product.stock <= 0 || orderItem.quantity - orderItem.shippedQuantity <= 0]
                                    '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text" value="0" style="width: 30px;" disabled="disabled"\/>'+
                                    [#elseif orderItem.product.stock < orderItem.quantity - orderItem.shippedQuantity]
                                    '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${orderItem.product.stock}" maxlength="9" style="width: 30px;" max="${orderItem.product.stock}"\/>'+
                                    [#else]
                                    '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${orderItem.quantity - orderItem.shippedQuantity}" maxlength="9" style="width: 30px;" max="${orderItem.quantity - orderItem.shippedQuantity}"\/>'+
                                    [/#if]
                                [#else]
                                '<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${orderItem.quantity - orderItem.shippedQuantity}" maxlength="9" style="width: 30px;" max="${orderItem.quantity - orderItem.shippedQuantity}"\/>'+
                                [/#if]
                            '<\/td><\/tr>'+
                            [/#list]
                        '<tr><td colspan="6" style="border-bottom: none;">&nbsp;<\/td><\/tr><\/table><\/div><\/form>',
                    [/@compress]
                    width: 900,
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
                                phone: "required"
                            }
                        });
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

        [#if !trade.order.expired && trade.order.orderStatus == "confirmed" && (trade.order.shippingStatus == "shipped" || trade.order.shippingStatus == "partialShipment" || trade.order.shippingStatus == "partialReturns")]
            // 退货
            $returnsButton.click(function () {
                $.dialog({
                    title: "${message("admin.order.returns")}",
                    [@compress single_line = true]
                        content: '< form id= "returnsForm" action="returns.jhtml" method="post"><input type="hidden" name="token" value="${token}" \/><input type="hidden" name="orderId" value="${trade.order.id}" \/><div style="height: 380px; overflow-x: hidden; overflow-y: auto;"><table class="input" style="margin-bottom: 30px;"><tr><th>${message("Order.sn")}:<\/th><td width="300">${trade.order.sn}<\/td><th>${message("admin.common.createDate")}:<\/th><td>${trade.order.createDate?string("yyyy-MM-dd HH:mm:ss")}<\/td><\/tr><tr><th>${message("Returns.shippingMethod")}:<\/th><td><select name="shippingMethodId"><option value="">${message("admin.common.choose")}<\/option>' +
                            [#list shippingMethods as shippingMethod]
                            '<option value="${shippingMethod.id}">${shippingMethod.name}<\/option>'+
                            [/#list]
                        '<\/select><\/td><th>${message("Returns.deliveryCorp")}:<\/th><td><select name="deliveryCorpId"><option value="">${message("admin.common.choose")}<\/option>' +
                            [#list deliveryCorps as deliveryCorp]
                            '<option value="${deliveryCorp.id}">${deliveryCorp.name}<\/option>'+
                            [/#list]
                        '<\/select><\/td><\/tr><tr><th>${message("Returns.trackingNo")}:<\/th><td><input type="text" name="trackingNo" class="text" maxlength="200"\/><\/td><th>${message("Returns.freight")}:<\/th><td><input type="text" name="freight" class="text" maxlength="16"\/><\/td><\/tr><tr><th>${message("Returns.shipper")}:<\/th><td><input type="text" name="shipper" class="text" value="${trade.order.consignee}" maxlength="200"\/><\/td><th>${message("Returns.zipCode")}:<\/th><td><input type="text" name="zipCode" class="text" value="${trade.order.zipCode}" maxlength="200"\/><\/td><\/tr><tr><th>${message("Returns.area")}:<\/th><td><span class="fieldSet"><input type="hidden" id="areaId" name="areaId" value="${(trade.order.area.id)!}" treePath="${(trade.order.area.treePath)!}"\/><\/span><\/td><th>${message("Returns.address")}:<\/th><td><input type="text" name="address" class="text" value="${trade.order.address}" maxlength="200"\/><\/td><\/tr><tr><th>${message("Returns.phone")}:<\/th><td><input type="text" name="phone" class="text" value="${trade.order.phone}" maxlength="200"\/><\/td><th>${message("Returns.memo")}:<\/th><td><input type="text" name="memo" class="text" maxlength="200"\/><\/td><\/tr><\/table><table class="input"><tr class="title"><th>${message("ReturnsItem.sn")}<\/th><th>${message("ReturnsItem.name")}<\/th><th>${message("admin.order.productStock")}<\/th><th>${message("OrderItem.shippedQuantity")}<\/th><th>${message("OrderItem.returnQuantity")}<\/th><th>${message("admin.order.returnsQuantity")}<\/th><\/tr>' +
                            [#list trade.order.orderItems as orderItem]
                            '<tr><td><input type="hidden" name="returnsItems[${orderItem_index}].sn" value="${orderItem.sn}"\/>${orderItem.sn}<\/td><td width="300"><span title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 50, "...")}<\/span>'+
                                [#if orderItem.isGift]
                                '<span class="red">[${message("admin.order.gift")}]<\/span>'+
                                [/#if]
                            '<\/td><td>${(orderItem.product.stock)!"-"}<\/td><td>${orderItem.shippedQuantity}<\/td><td>${orderItem.returnQuantity}<\/td><td><input type="text" name="returnsItems[${orderItem_index}].quantity" class="text returnsItemsQuantity" value="${orderItem.shippedQuantity - orderItem.returnQuantity}" maxlength="9" style="width: 30px;"[#if orderItem.shippedQuantity - orderItem.returnQuantity <= 0] disabled="disabled"[#else] max="${orderItem.shippedQuantity - orderItem.returnQuantity}"[/#if] \/><\/td><\/tr>'+
                            [/#list]
                        '<tr><td colspan="6" style="border-bottom: none;">&nbsp;<\/td><\/tr><\/table><\/div><\/form>',
                    [/@compress]
                    width: 900,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onShow: function () {
                        $("#areaId").lSelect({
                            url: "${base}/common/area.jhtml"
                        });
                        $.validator.addClassRules({
                            returnsItemsQuantity: {
                                required: true,
                                digits: true
                            }
                        });
                        $("#returnsForm").validate({
                            rules: {
                                freight: {
                                    min: 0,
                                    decimal: {
                                        integer: 12,
                                        fraction: ${setting.priceScale}
                                    }
                                },
                                shipper: "required",
                                zipCode: "required",
                                areaId: "required",
                                address: "required",
                                phone: "required"
                            }
                        });
                    },
                    onOk: function () {
                        var total = 0;
                        $("#returnsForm input.returnsItemsQuantity").each(function () {
                            var quantity = $(this).val();
                            if ($.isNumeric(quantity)) {
                                total += parseInt(quantity);
                            }
                        });
                        if (total <= 0) {
                            $.message("warn", "${message("admin.order.returnsQuantityPositive")}");
                        } else {
                            $("#returnsForm").submit();
                        }
                        return false;
                    }
                });
            });
        [/#if]

        });
    </script>
</head>
<body>
<form id="confirmForm" action="confirm.jhtml" method="post">
    <input type="hidden" name="id" value="${trade.id}"/>
</form>
<form id="confirmRefundapplyForm" action="confirmRefundapply.jhtml" method="post">
    <input type="hidden" name="id" value="${trade.id}"/>
</form>
<form id="completeForm" action="complete.jhtml" method="post">
    <input type="hidden" name="id" value="${trade.id}"/>
</form>
<form id="cancelForm" action="cancel.jhtml" method="post">
    <input type="hidden" name="id" value="${trade.id}"/>
</form>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.order.view")}
</div>
<ul id="tab" class="tab">
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
        <input type="button" value="${message("admin.order.rebate")}"/>
    </li>
</ul>
<table class="input tabContent">

    <tr>
        <th>
        ${message("Order.sn")}:
        </th>
        <td width="360">
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
            <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("Order.expire")}: ${trade.order.expire})</span>
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
        ${message("Member.username")}:
        </th>
        <td>
        ${trade.order.member.username}
        </td>
    </tr>
    <tr>
        <th>
        ${message("Order.amount")}:
        </th>
        <td>
        ${currency(trade.amount, true)}
        </td>
        <th>
        ${message("Order.amountPaid")}:
        </th>
        <td>
        [#if trade.paymentStatus=='paid'||trade.paymentStatus=='refunded'||trade.paymentStatus=='refundApply']
        ${currency(trade.amount, true)}
        [/#if]
        </td>
    </tr>
    <tr>
        <th>
        ${message("Order.weight")}:
        </th>
        <td>
        ${trade.order.weight}
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
        ${message("Order.promotion")}:
        </th>
        <td>
        ${(trade.order.promotion)!"-"}
        </td>
        <th>
        ${message("admin.order.coupon")}:
        </th>
        <td>
        ${(trade.order.couponCode.coupon.name)!"-"}
        </td>
    </tr>
    <tr>
        <th>
        ${message("Order.promotionDiscount")}:
        </th>
        <td>
        ${currency(trade.order.promotionDiscount, true)}
        </td>
        <th>
        ${message("Order.couponDiscount")}:
        </th>
        <td>
        ${currency(trade.order.couponDiscount, true)}
        </td>
    </tr>
    <tr>
        <th>
        ${message("Order.offsetAmount")}:
        </th>
        <td>
        ${currency(trade.order.offsetAmount, true)}
        </td>
        <th>
        ${message("Order.point")}:
        </th>
        <td>
        ${trade.point}
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
        ${message("Order.fee")}:
        </th>
        <td>
        ${currency(trade.order.fee, true)}
        </td>
    </tr>
    <tr>
        <th>
        ${message("Order.paymentMethod")}:
        </th>
        <td>
        ${trade.order.paymentMethodName}
        </td>
        <th>
        ${message("Order.shippingMethod")}:
        </th>
        <td>
        ${trade.order.shippingMethodName}-(提货码：${(trade.order.trades[0].sn)!})
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
        ${currency(trade.order.tax, true)}
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
        ${trade.order.memo}
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
            商家
        </th>
        <th>
            联系电话
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
[#list trade.order.orderItems as orderItem]
    [#if orderItem.trade.id==trade.id]
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
            ${trade.tenant.name}
            </td>
            <td>
            ${trade.tenant.telephone}
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
    [/#if]
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
    [#if payment??&&payment.order.orderType=="single"]
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
                    <span title="${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.paymentDate}</span>
                [#else]
                    -
                [/#if]
            </td>
        </tr>
    [#elseif payment??&&payment.order.orderType=="composite"&&(payment.trade==null||payment.trade.id==trade.id)]
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
                ${currency(payment.amount, true)}[#if payment.trade==null](合并付款总金额)[/#if]
            </td>
            <td>
            ${message("Payment.Status." + payment.status)}
            </td>
            <td>
                [#if payment.paymentDate??]
                    <span title="${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.paymentDate}</span>
                [#else]
                    -
                [/#if]
            </td>
        </tr>
    [/#if]
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
[#list trade.order.shippings as shipping]
    [#if shipping.trade!=null&&shipping.trade.id==trade.id]
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
                <span title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate}</span>
            </td>
        </tr>
    [/#if]
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
    </tr>
[#list trade.order.refunds as refunds]
    [#if refunds.trade!=null&&refunds.trade.id==trade.id]
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
                <span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate}</span>
            </td>
        </tr>
    [/#if]
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
[#list trade.order.returns as returns]
    [#if returns.trade!=null&&returns.trade.id==trade.id]
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
                <span title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate}</span>
            </td>
        </tr>
    [/#if]
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
    [#if orderLog.trade!=null&&orderLog.trade.id==trade.id]
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
                <span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate}</span>
            </td>
        </tr>
    [/#if]
[/#list]
</table>
<table class="input tabContent">
    <tr class="title">
        <th>
            分配金额
        </th>
        <th>
            订单佣金
        </th>
        <th>
            描述
        </th>
        <th>
            分配角色
        </th>
        <th>
            类型
        </th>
        <th>
            是否入账
        </th>
    </tr>
[#list trade.rebate as rebates]
    <tr>
        <td>
        ${rebates.amount}
        </td>
        <td>
        ${rebates.brokerage}
        </td>
        <td>
        ${rebates.description}
        </td>
        <td>
            [#if rebates.member?has_content]
                [#if rebates.member.tenant?has_content]
                    [#if rebates.member.id==rebates.member.tenant.member.id]
                        店主(${rebates.member.tenant.name})
                    [#else]用户[/#if]
                [#else]用户
                [/#if]
            [#else]平台[/#if]
        </td>
        <td>
        ${message("Rebate.Type." + rebates.type)}
        </td>
        <td>
        ${message("Rebate.Status." + rebates.status)}
        </td>
    </tr>
[/#list]
</table>
<table class="input">
    <tr>
        <th>
            &nbsp;
        </th>
        <td>
            <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)"/>
        </td>
    </tr>
</table>
</body>
</html>