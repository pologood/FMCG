<!DOCTYPE html>
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

            // 同意退货
            $returnsSubmit.click(function () {
                if("${isMonthly}"=="true"){
                    $.message("warn", "今日有月结操作，不能进行此操作");
                    return;
                }
                $.dialog({
                    type: "warn",
                    content: "同意退货后将无法编辑，是否继续?",
                    ok: "${message("b2b.dialog.ok")}",
                    cancel: "${message("b2b.dialog.cancel")}",
                    onOk: function () {
                        agree_return();
                    }
                });
            });
            //拒绝退货
            $("#refuse_return").click(function(){
                $.dialog({
                    title: "拒绝理由",
                    [@compress single_line = true]
                    content: '
                    <table class= "input">
                    <tr>
                    <th>
                    拒绝理由:
                    <\/th>
                    <td>
                    <textarea type="text" row="1" cols="10" id="refuse_content" class="text" value="">
                    <\/textarea>
                    <\/td>
                    <\/tr>
                    <\/table>'
                    ,
                    [/@compress]
                    width: 435,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function() {
                        agree_return("false",$("#refuse_content").val());
                    }
                });
            });
            //调价
            $("#updatePriceButton").click(function(){
                [#assign returnAmount=0]
                [#assign returnSettle=0]
                [#list spReturn.returnsItems as spReturnItems]
                    [#assign returnAmount=returnAmount+spReturnItems.returnQuantity*spReturnItems.orderItem.price]
                    [#assign returnSettle=returnSettle+spReturnItems.returnQuantity*spReturnItems.orderItem.cost]
                [/#list]
                $.dialog({
                    title: "调价",
                    [@compress single_line = true]
                    content: '
                    <table class= "input" style="margin-bottom: 30px;">
                    <tr>
                    <th>
                    退货金额:
                    <\/th>
                    <td>
                    <input type="text" id="adjAmount" class="text" value="${returnAmount}" \/>
                    <span style="color:red;">(可调范围：0-${returnAmount})<\/span>
                    <\/td>
                    <\/tr>
                    <tr>
                    <th>
                    结算金额:
                    <\/th>
                    <td>
                    <input type="text" id="adjFreight" class="text" value="${returnSettle}" \/>
                    <span style="color:red;">(可调范围：0-${returnSettle})<\/span>
                    <\/td>
                    <\/tr>
                    <\/table>'
                    ,
                    [/@compress]
                    width: 500,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function() {
                        if("${spReturn.trade.order.paymentMethod.method}"=="offline"){
                            if($("#adjAmount").val()==""||$("#adjFreight").val()==""){
                                $.message("warn","退货金额和结算金额不能为空");
                                return;
                            }
                        }
                        $.ajax({
                            url: "${base}/helper/member/trade/update_return_price.jhtml",
                            type: "POST",
                            data: {returnId:${spReturn.id},amount:$("#adjAmount").val(),cost:$("#adjFreight").val()},
                            dataType: "json",
                            cache: false,
                            success: function(message) {
                                if (message.type != "success") {
                                    $.message(message);
                                    setTimeout(function() {
                                        location.href = "${base}/helper/member/trade/return/view.jhtml?spReturnsId=${spReturn.id}";
                                        return false;
                                    }, 1000);
                                }else {
                                    setTimeout(function() {
                                        location.reload();//刷新页面
                                        return false;
                                    }, 1000);
                                }
                            },
                            error:function(e){
                                alert(e)
                            }
                        });
                    }
                });
            });
        });
        //退货
        function agree_return(obj,content,typ){
            $.ajax({
                url:"${base}/helper/member/trade/return/confirm_return.jhtml",
                type:"post",
                data:{id:${spReturn.id},flag:obj,content:content,app_type:typ},
                dataType:"json",
                success:function(data){
                    if(data.type=="success"){
                        $.message("success","操作成功");
                        location.reload();
                    }else{
                        $.message(data.type,data.content)
                    }
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
            <dt class="app-title" id="app_name">我的退货单</dt>
            <dd class="app-status" id="app_add_status">
            </dd>
            <dd class="app-intro" id="app_desc">查询及管理我销售的退货处理。</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <li><a class="on" hideFocus="" href="${base}/helper/member/trade/list.jhtml">我的退货单</a></li>
    </ul>

</div>
<ul id="tab" class="tab">
    <li>
        <input type="button" value="${message("admin.order.orderInfo")}"/>
    </li>
    <li>
        <input type="button" value="${message("admin.order.returnsInfo")}"/>
    </li>
    <li>
        <input type="button" value="退款信息"/>
    </li>
</ul>
<div class="list" style="margin-top:0px;border-right: 1px solid #D8DEEA">
    <table class="input tabContent">
        <tr>
            [#if spReturn.returnStatus=='unconfirmed']
            <td>
                &nbsp;
            </td>
            <td>
                [@helperRole url="helper/member/trade/return/list.jhtml" type="confirm"]
                    [#if helperRole.retOper!="0"]
                        [#if versionType==1]
                            <input type="button" class="button" value="确认受理" onclick="agree_return('true','','jdh')"/>
                        [#else]
                            <input type="button" class="button" value="确认受理" onclick="agree_return('true','','other')"/>
                        [/#if]
                    [/#if]
                [/@helperRole]

            </td>
            <td>
                [@helperRole url="helper/member/trade/return/list.jhtml" type="dismissal"]
                    [#if helperRole.retOper!="0"]
                        <input type="button" id="refuse_return" class="button" value="拒绝受理" />
                    [/#if]
                [/@helperRole]
            </td>
            <td>
                [@helperRole url="helper/member/trade/return/list.jhtml" type="modifyPrice"]
                    [#if helperRole.retOper!="0"]
                        <input type="button" id="updatePriceButton" class="button" value="调价">
                    [/#if]
                [/@helperRole]
            </td>
            [#elseif spReturn.returnStatus=='audited']
            <td>
                &nbsp;
            </td>
            <td>
                <input type="button" id="returnsSubmit" class="button" value="同意退货"/>
            </td>
            <!-- <td>
                <input type="button" id="rejectedButton" class="button" value="拒绝退货"/>
            </td> -->
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
            <!-- ${message("Order.offsetAmount")}: -->
            订单数量:
            </th>
            <td>
            ${(trade.quantity)!}
            </td>
        </tr>
        <tr>
            <th>
            退货金额：
            </th>
            <td>
            ${(spReturn.amount)!}
            </td>
            <th>
            退货数量:
            </th>
            <td>
            ${(spReturn.quantity)!}
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
            ${currency(trade.totalProfit, true)}
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
            ${message("OrderItem.price")}
            </th>
            <!-- <th>
            ${message("OrderItem.quantity")}
            </th> -->
            <th>
            <!-- ${message("OrderItem.shippedQuantity")} -->
            发货数量
            </th>
            <th>
            <!-- ${message("OrderItem.returnQuantity")} -->
            退货数量
            </th>
            <th>
            ${message("OrderItem.subtotal")}
            </th>
        </tr>
    [#list spReturn.returnsItems as returnItem]
        <tr>
            <td>
            ${returnItem.sn}
            </td>
            <td width="400">
                <span title="${returnItem.orderItem.fullName}">${abbreviate(returnItem.orderItem.fullName, 50, "...")}</span>
                [#if returnItem.orderItem.isGift]
                    <span class="red">[${message("admin.order.gift")}]</span>
                [/#if]
            </td>
            <td>
                [#if !returnItem.orderItem.isGift]
                ${currency(returnItem.orderItem.price, true)}
                [#else]
                    -
                [/#if]
            </td>
            <!-- <td>
            ${returnItem.orderItem.returnQuantity}
            </td> -->
            <td>
            ${returnItem.shippedQuantity}
            </td>
            <td>
            ${returnItem.returnQuantity}
            </td>
            <td>
                [#if !returnItem.orderItem.isGift]
                ${currency(returnItem.returnQuantity*returnItem.orderItem.price, true)}
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
            退货金额
            </th>
            <th>
            结算金额
            </th>
        </tr>
        <tr>
            <td>
            ${spReturn.amount}
            </td>
            <td>
            ${spReturn.cost}
            </td>
        </tr>
    </table>
    <!-- <table class="input tabContent">
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
                    <span title="${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.paymentDate}</span>
                [#else]
                    -
                [/#if]
            </td>
        </tr>
    [/#list]
    </table> -->
    <!-- <table class="input tabContent">
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
                <span title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate}</span>
            </td>
        </tr>
    [/#list]
    </table> -->
    <!-- <table class="input tabContent">
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
                <span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate}</span>
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
    </table> -->
    <!-- <table class="input tabContent">
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
                <span title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate}</span>
            </td>
        </tr>
    [/#list]
    </table> -->
    <!-- <table class="input tabContent">
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
                <span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate}</span>
            </td>
        </tr>
    [/#list]
    </table> -->
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
