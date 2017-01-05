<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>我的订单</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
          name="viewport"> [#include "/store/member/include/bootstrap_css.ftl"]
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/header.ftl"]
    <!-- Left side column. contains the logo and sidebar -->
[#include "/store/member/include/menu.ftl"]
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>我的订单
                <small>查询及管理我销售的订单并对订单进行发货、退货处理。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/trade/list.jhtml">我的订单</a></li>
                <li><a href="${base}/store/member/trade/list.jhtml">订单管理</a></li>
                <li class="active">查看</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs pull-right">
                            <li class="pull-left header"><i class="fa fa-file-text"></i>我的订单</li>
                        </ul>

                        <div class="tab-content">
                            <div class='tab'>
                                <!-- Nav tabs -->
                                <ul class="nav nav-tabs" role="tablist">
                                    <li role="presentation" class="active">
                                        <a href="#s1" role="tab" data-toggle="tab">订单信息</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s2" role="tab" data-toggle="tab">商品信息</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s3" role="tab" data-toggle="tab">收款信息</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s4" role="tab" data-toggle="tab">发货信息</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s5" role="tab" data-toggle="tab">退款信息</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s6" role="tab" data-toggle="tab">退货信息</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s7" role="tab" data-toggle="tab">订单日志</a>
                                    </li>
                                    <li role="presentation">
                                        <a href="#s8" role="tab" data-toggle="tab">优惠券信息</a>
                                    </li>
                                </ul>
                                <!-- Tab panes -->
                                <div class="tab-content bt-none mt10">

                                    <div role="tabpanel" class="tab-pane active" id="s1">
                                        <!--按钮组【-->

                                    [#if isReturnAllowed!=null]
                                        <div class="form-horizontal">
                                            <div class="form-horizontal form-group">
                                                <div class="col-sm-offset-0 col-sm-2">
                                                    <input type="button" class="btn btn-block btn-primary"
                                                           data-toggle="modal" value="同意退货" id="agree_return_button">
                                                </div>
                                                <div class="col-sm-offset-0 col-sm-2">
                                                    <input type="button" class="btn btn-block btn-default"
                                                           data-toggle="modal" value="拒绝退货" id="refused_return_button">
                                                </div>
                                            </div>
                                        </div>
                                    [#elseif trade.orderStatus == "unconfirmed"]
                                        <div class="form-horizontal">
                                            <div class="form-horizontal form-group">
                                                <div class="col-sm-offset-0 col-sm-2">
                                                    <input type="button" class="btn btn-block btn-primary"
                                                           data-toggle="modal" value="调价" id="adj_amount_button"
                                                           [#if trade.paymentStatus="paid"]style="display: none" [/#if]>
                                                </div>
                                                [#if trade.paymentStatus="paid" ||trade.order.shippingMethodName=="到店提货"]
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-primary"
                                                               data-toggle="modal" value="确认订单" id="confirm_order">
                                                    </div>
                                                [/#if]
                                                <div class="col-sm-offset-0 col-sm-2">
                                                    <input type="button" class="btn btn-block btn-default"
                                                           data-toggle="modal" value="取消订单" id="close_button">
                                                </div>
                                            </div>
                                        </div>
                                    [#else]
                                        <div class="form-horizontal">
                                            <div class="form-group">
                                                [#if versionType==0]
                                                    [#if trade.order.shippingMethod.method=="F2F"]
                                                        <div class="col-sm-offset-0 col-sm-2">
                                                            <input type="button" class="btn btn-block btn-primary"
                                                                   data-toggle="modal" value="核销提货码"
                                                                   id="cancel_verification"
                                                                   [#if trade.orderStatus == "completed"]disabled[/#if]>
                                                        </div>
                                                    [/#if]
                                                [/#if]
                                                [#if (trade.shippingStatus=='unshipped'||trade.shippingStatus=='partialShipment')&&trade.orderStatus=="confirmed"&&trade.order.shippingMethod.method=="TPL"]
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-primary"
                                                               data-toggle="modal" value="发货"
                                                               [#if trade.order.paymentStatus=="refundapply"||trade.order.expired || trade.orderStatus != "confirmed" || (trade.shippingStatus != "unshipped" && trade.shippingStatus != "partialShipment")||trade.orderStatus=="completed"]disabled="disabled"[/#if]
                                                               id="shipping_button">
                                                    </div>
                                                [/#if]
                                                [#if trade.orderStatus=="unconfirmed"||trade.orderStatus=="confirmed"]
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-default"
                                                               data-toggle="modal" value="取消订单" id="close_button">
                                                    </div>
                                                [/#if]
                                            </div>
                                        </div>
                                    [/#if]
                                        <!--按钮组】-->
                                        <table class="table table-bordered odder">
                                            <tbody>
                                            <tr>
                                                <td>订单编号:</td>
                                                <td>${trade.order.sn}</td>
                                                <td>创建日期:</td>
                                                <td>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                                            </tr>
                                            <tr>
                                                <td>订单状态:</td>
                                                <td>
                                                ${message("Order.OrderStatus." + trade.orderStatus)}
                                                [#if trade.order.expired]
                                                    <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("admin.order.hasExpired")})</span>
                                                [#elseif trade.order.expire??]
                                                    <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
                                                [/#if]
                                                </td>
                                                <td>支付状态:</td>
                                                <td>${message("Order.PaymentStatus." + trade.paymentStatus)}</td>
                                            </tr>
                                            <tr>
                                                <td>配送状态:</td>
                                                <td>${message("Order.ShippingStatus." + trade.shippingStatus)}</td>
                                                <td>支付方式:</td>
                                                <td>${trade.order.paymentMethodName}</td>
                                            </tr>
                                            <tr>
                                                <td>订单总价:</td>
                                                <td>${currency(trade.amount, true)}</td>
                                                <td>调整金额:</td>
                                                <td>${currency(trade.offsetAmount, true)}</td>
                                            </tr>
                                            <tr>
                                                <td>商家折扣:</td>
                                                <td>${currency(trade.couponDiscount, true)}</td>
                                                <td>平台折扣:</td>
                                                <td> ${currency(trade.discount, true)}</td>
                                            </tr>
                                            <tr>
                                                <td>商品重量:</td>
                                                <td>${trade.weight}</td>
                                                <td>商品数量:</td>
                                                <td>${trade.quantity}</td>
                                            </tr>
                                            <tr>
                                                <td>运费:</td>
                                                <td>${currency(trade.freight, true)}</td>
                                                <td>佣金:</td>
                                                <td>${currency(trade.totalProfit+trade.agencyAmount, true)}</td>
                                            </tr>
                                            [#if trade.order.isInvoice]
                                            <tr>
                                                <td>发票抬头:</td>
                                                <td>${trade.order.invoiceTitle}</td>
                                                <td>税收:</td>
                                                <td>${currency(trade.tax, true)}</td>
                                            </tr>
                                            [/#if]
                                            <tr>
                                                <td>用户名:</td>
                                                <td>${trade.order.member.username}</td>
                                                <td>配送方式:</td>
                                                <td>${trade.order.shippingMethodName}</td>
                                            </tr>
                                            <tr>
                                                <td>收货人:</td>
                                                <td>${trade.order.consignee}</td>
                                                <td>地区:</td>
                                                <td>${trade.order.areaName}</td>
                                            </tr>
                                            <tr>
                                                <td>地址:</td>
                                                <td>${trade.order.address}</td>
                                                <td>邮编</td>
                                                <td>${trade.order.zipCode}</td>
                                            </tr>
                                            <tr>
                                                <td>电话:</td>
                                                <td>${trade.order.phone}</td>
                                                <td>附言:</td>
                                                <td>${trade.memo}</td>
                                            </tr>
                                            <tr [#if trade.order.shippingMethod=="F2F"]style="display:none;"[/#if]>
                                                <td>提货时间:</td>
                                                <td>
                                                [#if trade.shippings?size gt 0]
                                                    [#assign ship=""]
                                                    [#list trade.shippings as shipping]
                                                        [#if shipping??]
                                                            [#assign ship=shipping.pickUpTime]
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
															${ship}
                                                </td>
                                                <td>提货门店</td>
                                                <td>${trade.tenant.name}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s2">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>商品编号</th>
                                                <th>商品名称</th>
                                                <th>商品价格</th>
                                                <th>数量</th>
                                                <th>已发货数量</th>
                                                <th>已退货数量</th>
                                                <th>小计</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list trade.orderItems as orderItem]
                                            <tr>
                                            <tr>
                                                <td>${orderItem.sn}</td>
                                                <td width="400">
																<span title="${orderItem.fullName}">${abbreviate(orderItem.fullName,30,"...")}
																</span>[#if orderItem.isGift]<span
                                                        class="red">[赠品]</span>[/#if]
                                                </td>
                                                <td>[#if !orderItem.isGift]${currency(orderItem.price, true)}[#else]
                                                    -[/#if]</td>
                                                <td>${orderItem.quantity}</td>
                                                <td>${orderItem.shippedQuantity}</td>
                                                <td>${orderItem.returnQuantity}</td>
                                                <td>[#if !orderItem.isGift]${currency(orderItem.subtotal,true)}[#else]
                                                    -[/#if]</td>
                                            </tr>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s3">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>编号</th>
                                                <th>方式</th>
                                                <th>支付方式</th>
                                                <th>付款金额</th>
                                                <th>付款日期</th>
                                                <th>状态</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list trade.order.payments as payment]
                                            <tr>
                                            <tr>
                                                <td>${payment.sn}</td>
                                                <td>${message("Payment.Method." + payment.method)}</td>
                                                <td>${(payment.paymentMethod)!"-"}</td>
                                                <td>${currency(payment.amount, true)}</td>
                                                <td>
                                                    [#if payment.paymentDate??]
                                                        <span title="${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                                                    [#else]
                                                        -
                                                    [/#if]
                                                </td>
                                                <td>${message("Payment.Status." + payment.status)}</td>
                                            </tr>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s4">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>编号</th>
                                                <th>配送方式</th>
                                                <th>物流公司</th>
                                                <th>运单号</th>
                                                <th>收货人</th>
                                                <th>创建日期</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list trade.shippings as shipping]
                                            <tr>
                                            <tr>
                                                <td>${shipping.sn}</td>
                                                <td>${shipping.shippingMethod}</td>
                                                <td>${shipping.deliveryCorp}</td>
                                                <td>${(shipping.trackingNo)!"-"}</td>
                                                <td>${shipping.consignee}</td>
                                                <td><span
                                                        title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                                                </td>
                                            </tr>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s5">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>编号</th>
                                                <th>方式</th>
                                                <th>支付方式</th>
                                                <th>退货金额</th>
                                                <th>创建日期</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list trade.order.refunds as refunds]
                                            <tr>
                                            <tr>
                                                <td>${refunds.sn}</td>
                                                <td>${message("Refunds.Method." + refunds.method)}</td>
                                                <td>${refunds.paymentMethod!"-"}</td>
                                                <td>${currency(refunds.amount, true)}</td>
                                                <td><span
                                                        title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                                                </td>
                                            </tr>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s6">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>编号</th>
                                                <th>配送方式</th>
                                                <th>物流公司</th>
                                                <th>运单号</th>
                                                <th>收货人</th>
                                                <th>创建日期</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list trade.returns as returns]
                                            <tr>
                                            <tr>
                                                <td>${returns.sn}</td>
                                                <td>${(returns.shippingMethod)!"-"}</td>
                                                <td>${(returns.deliveryCorp)!"-"}</td>
                                                <td>${(returns.trackingNo)!"-"}</td>
                                                <td>${returns.shipper}</td>
                                                <td><span
                                                        title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                                            </tr>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s7">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>类型</th>
                                                <th>操作员</th>
                                                <th>内容</th>
                                                <th>创建日期</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list trade.order.orderLogs as orderLog]
                                            <tr>
                                            <tr>
                                                <td>${message("OrderLog.Type." + orderLog.type)}</td>
                                                <td>[#if orderLog.operator??]
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
                                                <td><span
                                                        title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                                            </tr>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="s8">
                                        <table id="" class="table table-bordered table-striped ">
                                            <thead>
                                            <tr>
                                                <th>优惠券</th>
                                                <th>金额</th>
                                                <th>是否已使用</th>
                                                <th>状态</th>
                                                <th>使用者</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#if trade.tenantCouponCode??]
                                            <tr>
                                                <td>${trade.tenantCouponCode.coupon.name}</td>
                                                <td>${trade.tenantCouponCode.coupon.amount}</td>
                                                <td>[#if trade.tenantCouponCode.isUsed=="true"]已使用[#else]未使用[/#if]</td>
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
																	[/#if]
                                                </td>
                                            </tr>
                                            [/#if]
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="form-horizontal">
                                        <div class="form-horizontal form-group">
                                            <div class="col-sm-offset-10 col-sm-2">
                                                <input type="button" class="btn btn-block btn-default" value="返回"
                                                       onclick="javascript:history.back();">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="kong"></div>
                                    <!-- 发货弹框【 -->
                                    <div class="modal fade" id="shipping" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog wid_750">
                                            <form class="form-horizontal" id="shippingForm" action="shipping.jhtml"
                                                  method="post" role="form">
                                                <input type="hidden" name="token" value="${token}">
                                                <input type="hidden" name="tradeId" value="${trade.id}">
                                                <div class="modal-content" style="width: 800px; border-radius: 5px;">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal"><span
                                                                aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                                                        </button>
                                                        <h4 class="modal-title">发货</h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">订单编号</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control"
                                                                       value="${trade.order.sn}" disabled="true">
                                                            </div>
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">创建日期</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control"
                                                                       value="${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}"
                                                                       disabled="true">
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">配送方式</label>
                                                            <div class="col-sm-4">
                                                                <select class="form-control" name="shippingMethodId"
                                                                        id="shippingMethodId">
                                                                    <option value="">请选择配送方式</option>
                                                                [#list shippingMethods as shippingMethod]
                                                                    <option value="${shippingMethod.id}"
                                                                            [#if trade.order.shippingMethodName==shippingMethod.name]selected="selected"[/#if]>
                                                                    ${shippingMethod.name}
                                                                    </option>
                                                                [/#list]
                                                                </select>
                                                            </div>
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">物流公司</label>
                                                            <div class="col-sm-4">
                                                                <select class="form-control" name="deliveryCorpId"
                                                                        id="deliveryCorpId">
                                                                    <option value="">请选择快递公司</option>
                                                                [#list deliveryCorps as deliveryCorp]
                                                                    <option value="${deliveryCorp.id}"[#if trade.order.shippingMethod?? && deliveryCorp == trade.order.shippingMethod.defaultDeliveryCorp]
                                                                            selected="selected"[/#if]>
                                                                    ${deliveryCorp.name}
                                                                    </option>
                                                                [/#list]
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName" class="col-sm-2 control-label">
                                                            [#if versionType==0]运单号[#else]运单员[/#if]
                                                            </label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control"
                                                                       name="trackingNo">
                                                            </div>
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">物流费用</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" name="freight">
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">收货人</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" name="consignee"
                                                                       value="${trade.order.consignee}">
                                                            </div>
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">邮编</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" name="zipCode"
                                                                       value="${trade.order.zipCode}">
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">地址</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" name="address"
                                                                       value="${trade.order.address}">
                                                            </div>
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">电话</label>
                                                            <div class="col-sm-4">
                                                                <input type="text" class="form-control" name="phone"
                                                                       value="${trade.order.phone}">
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">地区</label>
                                                            <div class="col-sm-10">
                                                                <input type="hidden" id="areaId" name="areaId"
                                                                       value="${(trade.order.area.id)!}"
                                                                       treePath="${(trade.order.area.treePath)!}"/>
                                                            </div>
                                                        </div>
                                                        <div class="form-group"
                                                             [#if trade.order.shippingMethod=="F2F"]style="display:none;"[/#if]>
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">提货时间</label>
                                                            <div class="col-sm-10">
                                                                <input type="text" class="form-control"
                                                                       name="pick_up_time" maxlength="200"
                                                                       onfocus="WdatePicker({dateFmt: &quot;yyyy-MM-dd HH:mm:ss&quot;});">
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">备注</label>
                                                            <div class="col-sm-10">
                                                                <input type="text" class="form-control" name="memo">
                                                            </div>
                                                        </div>
                                                        <table id="example2"
                                                               class="table table-bordered table-striped mt10">
                                                            <thead>
                                                            <tr class="title">
                                                                <th>商品编号</th>
                                                                <th style="width:200px;">商品名称</th>
                                                                <th>商品库存</th>
                                                                <th>购买数量</th>
                                                                <th>已发货数量</th>
                                                                <th>发货数量</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            [#list trade.orderItems as orderItem]
                                                            <tr>
                                                                <td>
                                                                    <input type="hidden"
                                                                           name="shippingItems[${orderItem_index}].sn"
                                                                           value="${orderItem.sn}">
                                                                ${orderItem.sn}
                                                                </td>
                                                                <td width="150">
																					<span title="${orderItem.fullName}">
																						${abbreviate(orderItem.fullName, 20, "...")}
                                                                                            [#if orderItem.isGift]<span
                                                                                                    class="red">[赠品]</span>[/#if]
																					</span>
                                                                </td>
                                                                <td>${(orderItem.product.stock)!"-"}</td>
                                                                <td>${orderItem.quantity}</td>
                                                                <td>${orderItem.shippedQuantity}</td>
                                                                <td>
                                                                    [#if orderItem.product?? && orderItem.product.stock??]
                                                                        [#if orderItem.product.stock lte 0 || (orderItem.quantity - orderItem.shippedQuantity) lte 0]
                                                                            <input type="text"
                                                                                   name="shippingItems[${orderItem_index}].quantity"
                                                                                   class="form-control mark" value="0"
                                                                                   style="width: 60px;"
                                                                                   disabled="disabled"/>
                                                                        [#elseif orderItem.product.stock lt (orderItem.quantity - orderItem.shippedQuantity)]
                                                                            <input type="text"
                                                                                   name="shippingItems[${orderItem_index}].quantity"
                                                                                   class="form-control mark"
                                                                                   value="${orderItem.product.stock}"
                                                                                   style="width: 60px;"
                                                                                   max="${orderItem.product.stock}"/>
                                                                        [#else]
                                                                            <input type="text"
                                                                                   name="shippingItems[${orderItem_index}].quantity"
                                                                                   class="form-control mark"
                                                                                   value="${orderItem.quantity - orderItem.shippedQuantity}"
                                                                                   style="width: 60px;"
                                                                                   max="${orderItem.quantity - orderItem.shippedQuantity}"/>
                                                                        [/#if]
                                                                    [#else]
                                                                        <input type="text"
                                                                               name="shippingItems[${orderItem_index}].quantity"
                                                                               class="form-control mark"
                                                                               value="${orderItem.quantity - orderItem.shippedQuantity}"
                                                                               style="width: 60px;"
                                                                               max="${orderItem.quantity - orderItem.shippedQuantity}"/>
                                                                    [/#if]
                                                                </td>
                                                            </tr>
                                                            [/#list]
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="form-horizontal form-group">
                                                            <div class="col-sm-offset-8 col-sm-2">
                                                                <button type="button" class="btn btn-block btn-primary"
                                                                        id="confirm_shipping">确定
                                                                </button>
                                                            </div>
                                                            <div class="col-sm-offset-0 col-sm-2">
                                                                <input type="button" class="btn btn-block btn-default"
                                                                       value="取消" data-dismiss="modal">
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <!-- 发货弹框】 -->
                                    <!-- 同意退货弹框【 -->
                                    <div class="modal fade" id="agree_return" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog">
                                            <form class="form-horizontal" role="form" action="returns.jhtml"
                                                  method="post">
                                                <input type="hidden" name="tradeId" value="${trade.id}">
                                                <div class="modal-content" style=" border-radius: 5px;">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal"><span
                                                                aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                                                        </button>
                                                        <h4 class="modal-title">同意退货</h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <lebal>是否确认此操作？</lebal>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="col-sm-offset-8 col-sm-2">
                                                            <button type="submit" class="btn btn-block btn-primary">确定
                                                            </button>
                                                        </div>
                                                        <div class="col-sm-offset-0 col-sm-2">
                                                            <input type="button" class="btn btn-block btn-default"
                                                                   value="取消" data-dismiss="modal">
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <!--同意退货弹框】 -->
                                    <!-- 拒绝退货弹框【 -->
                                    <div class="modal fade" id="refused_return" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog">
                                            <form class="form-horizontal" role="form" action="rejected.jhtml"
                                                  method="post">
                                                <input type="hidden" name="id" value="${trade.id}">
                                                <div class="modal-content" style=" border-radius: 5px;">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal"><span
                                                                aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                                                        </button>
                                                        <h4 class="modal-title">拒绝退货</h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <lebal>是否确认此操作？</lebal>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="col-sm-offset-8 col-sm-2">
                                                            <button type="submit" class="btn btn-block btn-primary">确定
                                                            </button>
                                                        </div>
                                                        <div class="col-sm-offset-0 col-sm-2">
                                                            <input type="button" class="btn btn-block btn-default"
                                                                   value="取消" data-dismiss="modal">
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <!--拒绝退货弹框】 -->
                                    <!-- 取消订单弹框【 -->
                                    <div class="modal fade" id="close_order" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog">
                                        [#if (trade.order.paymentMethod.method=='online'&&trade.paymentStatus=='unpaid')||(trade.order.paymentMethod.method=='offline'&&trade.paymentStatus=='unpaid'&&trade.shippingStatus=='unshipped')]
                                        <form class="form-horizontal" method="post" role="form" action="close.jhtml">
                                            <input type="hidden" name="id" value="${trade.id}">
                                        [#elseif (trade.order.paymentMethod.method=='offline'&&trade.shippingStatus=='shipped')||(trade.order.paymentMethod.method=='online'&&trade.paymentStatus=='paid')||(trade.order.paymentMethod.method=='online'&&trade.shippingStatus=='shipped')]
                                        <form class="form-horizontal" method="post" role="form" action="returns.jhtml">
                                            <input type="hidden" name="tradeId" value="${trade.id}">
                                        [/#if]
                                            <div class="modal-content" style=" border-radius: 5px;">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"><span
                                                            aria-hidden="true">&times;</span><span
                                                            class="sr-only">Close</span></button>
                                                    <h4 class="modal-title">取消订单</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <lebal>取消订单后数据将无法还原，是否确认此操作？</lebal>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="col-sm-offset-8 col-sm-2">
                                                        <button type="submit" class="btn btn-block btn-primary">确定
                                                        </button>
                                                    </div>
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-default"
                                                               value="取消" data-dismiss="modal">
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        </div>
                                    </div>
                                    <!--取消订单弹框】 -->
                                    <!-- 调价弹框【 -->
                                    <div class="modal fade" id="update_order_amount" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog form-horizontal">
                                            <div class="modal-content" style=" border-radius: 5px;">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"><span
                                                            aria-hidden="true">&times;</span><span
                                                            class="sr-only">Close</span></button>
                                                    <h4 class="modal-title">调价</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="form-group">
                                                        <label for="inputExperience"
                                                               class="col-sm-2 control-label">调价金额</label>
                                                        <div class="col-sm-9">
                                                            <input type="text" class="form-control" id="adjAmount"
                                                                   value="${trade.amount}"
                                                                   onkeyup="adjust_amount('amount',this)">
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">订单运费</label>
                                                        <div class="col-sm-9">
                                                            <input type="text" class="form-control" id="adjFreight"
                                                                   value="${trade.freight}"
                                                                   onkeyup="adjust_amount('freight',this)">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="col-sm-offset-8 col-sm-2">
                                                        <button type="button" class="btn btn-block btn-primary"
                                                                id="confirm_adj_amount">确定
                                                        </button>
                                                    </div>
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-default"
                                                               value="取消" data-dismiss="modal">
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                    <!--调价弹框】 -->
                                    <!-- 确认订单 -->
                                    <div class="modal fade" id="confirm_order_access" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog form-horizontal">
                                            <div class="modal-content" style=" border-radius: 5px;">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"><span
                                                            aria-hidden="true">&times;</span><span
                                                            class="sr-only">Close</span></button>
                                                    <h4 class="modal-title">确认订单</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="form-group">
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">配送方式</label>
                                                        <div class="col-sm-5">
                                                            <input type="text" class="form-control"
                                                                   name="name" maxlength="200"
                                                                   value=" ${(trade.order.shippingMethodName)!}"
                                                                   readonly>
                                                        </div>
                                                    </div>
                                                [#if trade.order.shippingMethodName!="同城快递"]
                                                    [#if versionType==0]
                                                        <div class="form-group">
                                                            <label for="inputName"
                                                                   class="col-sm-2 control-label">提货门店</label>
                                                            <div class="col-sm-5">
                                                                <input type="text" class="form-control"
                                                                       name="name" maxlength="200"
                                                                       value="[#if trade.deveryCenter]${trade.deliveryCenter.name}[#else]--[/#if]"
                                                                       readonly>
                                                            </div>
                                                        </div>
                                                    [/#if]
                                                    <div class="form-group">
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">提货时段</label>
                                                        <div class="col-sm-5">
                                                            <select class="form-control" id="chooseTime">
                                                                <option value="0">营业时间内不限</option>
                                                                <option value="1">选择时间</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group" id="pickUpTime">
                                                    [#--<label class="col-sm-2 control-label">开始时间</label>--]
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">提货时间</label>
                                                        <div class="col-sm-5">
                                                            <input type="text" class="form-control" id="pickGoodsTime"
                                                                   name="pickGoodsTime" maxlength="200"
                                                                   onfocus="WdatePicker({dateFmt: &quot;yyyy-MM-dd HH:mm:ss&quot;});">
                                                        </div>
                                                    </div>
                                                [/#if]
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="col-sm-offset-8 col-sm-2">
                                                        <button type="button" class="btn btn-block btn-primary"
                                                                id="confirm_order_submit">确定
                                                        </button>
                                                    </div>
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-default"
                                                               value="取消" data-dismiss="modal">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--确认订单 -->
                                    <!--核销提货码-->
                                    <div class="modal fade" id="cancel_verification_access" tabindex="-1" role="dialog"
                                         aria-labelledby="myModalLabel" aria-hidden="false">
                                        <div class="modal-dialog form-horizontal">
                                            <div class="modal-content" style=" border-radius: 5px;">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal"><span
                                                            aria-hidden="true">&times;</span><span
                                                            class="sr-only">Close</span></button>
                                                    <h4 class="modal-title">核销提货码</h4>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="form-group">
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">提货码</label>
                                                        <div class="col-sm-4">
                                                            <input type="text" class="form-control"
                                                                   name="sn" id="sn" maxlength="200"
                                                                   [#if hideshow=="show"]value="${trade.sn}"[/#if][#if hideshow=="hide"]onkeyup="searchSn()"[/#if]>
                                                        </div>
                                                    </div>
                                                    <div class="form-group"
                                                         [#if hideshow=="show"]id="orderSn"[/#if][#if hideshow=="hide"]id="orderSn1"[/#if]>
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">订单号</label>
                                                        <div class="col-sm-4">
                                                            <input type="text" class="form-control"
                                                                   name="sn" maxlength="200"
                                                                   value="${trade.order.sn}" readonly>
                                                        </div>
                                                    </div>
                                                    <div class="form-group"
                                                         [#if hideshow=="show"]id="username"[/#if][#if hideshow=="hide"]id="username1"[/#if]>
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">用户名</label>
                                                        <div class="col-sm-4">
                                                            <input type="text" class="form-control"
                                                                   name="username" maxlength="200"
                                                                   value="${trade.order.member.username}" readonly>
                                                        </div>
                                                    </div>
                                                    <div class="form-group"
                                                         [#if hideshow=="show"]id="deliverCenter"[/#if][#if hideshow=="hide"]id="deliverCenter1"[/#if]>
                                                    [#--<label class="col-sm-2 control-label">开始时间</label>--]
                                                        <label for="inputName"
                                                               class="col-sm-2 control-label">提货门店</label>
                                                        <div class="col-sm-4">
                                                            <input type="text" class="form-control"
                                                                   name="name" maxlength="200"
                                                                   value="${trade.tenant.name}" readonly>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="col-sm-offset-8 col-sm-2">
                                                        <button type="button" class="btn btn-block btn-primary"
                                                                id="cancel_verification_success">确定
                                                        </button>
                                                    </div>
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <input type="button" class="btn btn-block btn-default"
                                                               value="取消" data-dismiss="modal">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--核销提货码-->
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
[#include "/store/member/include/footer.ftl"]
</div>
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript" src="${base}/resources/store/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
<script>
    $().ready(function () {
        //地区选择
        $("#pickUpTime").hide();
        $("#username1").hide();
        $("#orderSn1").hide();
        $("#deliverCenter1").hide();
        $("#areaId").lSelect({
            url: "${base}/common/area.jhtml"
        });
        // 检查锁定
        var isLocked = false;

        $("#chooseTime").change(function () {
            if ($(this).children('option:selected').val() == 0) {
                $("#pickUpTime").hide();
            } else {
                $("#pickUpTime").show();
            }
        });

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
                            $("#shipping_button").prop("disabled", true);
                            $("#adj_amount_button").prop("disabled", true);
                            $("#agree_return_button").prop("disabled", true);
                            $("#refused_return_button").prop("disabled", true);
                            $("#close_button").prop("disabled", true);
                            $("#adj_amount_button").prop("disabled", true);
                            $("#confirm_order").prop("disabled", true);
                            $("#cancel_verification").prop("disabled", true);
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
        //确认发货
        $("#confirm_shipping").click(function () {
            //针对聚德惠月结功能
            if ("${isMonthly}" == "true") {
                $.message("warn", "今日有月结操作，不能进行此操作");
                return;
            }
            //确认发货验证
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
                    phone: "required",
                    pickGoodsTime: "required"
                },
                messages: {
                    shippingMethodId: "必填",
                    deliveryCorpId: "必填",
                    freight: "必填",
                    consignee: "必填",
                    zipCode: "必填",
                    areaId: "必填",
                    address: "必填",
                    phone: "必填",
                    pickGoodsTime: "必填"
                }
            });
            var total = 0;
            $("#shippingForm input.mark").each(function () {
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
        });
        //调价
        $("#confirm_adj_amount").click(function () {
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
                            location.href = "${base}/store/member/trade/view.jhtml?id=${trade.id}";
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
        });

        //确认订单
        $("#confirm_order_submit").click(function () {
            $.ajax({
                url: "confirm_order.jhtml",
                type: "POST",
                data: {tradeId:${trade.id}, deliveryDate: $("#pickGoodsTime").val()},
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.type != "success") {
                        $.message(message);
                        setTimeout(function () {
                            location.href = "${base}/store/member/trade/view.jhtml?id=${trade.id}";
                            return false;
                        }, 1000);
                    } else {
                        setTimeout(function () {
                            location.href = "${base}/store/member/trade/view.jhtml?id=${trade.id}";
                            return false;
                        }, 1000);
                    }
                }
            });
        });

        //核销提货码
        $("#cancel_verification_success").click(function () {
            $.ajax({
                url: "cancel_verification.jhtml",
                type: "POST",
                data: {sn: $("#sn").val()},
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.type != "success") {
                        $.message(message);
                        setTimeout(function () {
                            location.href = "${base}/store/member/trade/view.jhtml?id=${trade.id}";
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
        });

        //控制按钮是否要弹框
        $("#adj_amount_button,#close_button,#shipping_button,#agree_return_button,#refused_return_button,#confirm_order,#cancel_verification").click(function () {
            //针对聚德惠月结功能
            if ("${isMonthly}" == "true") {
                $.message("warn", "今日有月结操作，不能进行此操作");
                return;
            }
            $("#close_button").attr("data-target", "#close_order");
            $("#adj_amount_button").attr("data-target", "#update_order_amount");
            $("#shipping_button").attr("data-target", "#shipping");
            $("#agree_return_button").attr("data-target", "#agree_return");
            $("#refused_return_button").attr("data-target", "#refused_return");
            $("#confirm_order").attr("data-target", "#confirm_order_access");
            $("#cancel_verification").attr("data-target", "#cancel_verification_access");
        });

    });
    //判断金额范围
    function adjust_amount(obj, ths) {
        if (parseInt($(ths).val()) < 0) {
            $.message("warn", "不能为负数！");
            if (obj == "amount") {
                $("#adjAmount").val("${trade.amount}");
            } else {
                $("#adjFreight").val("${trade.freight}");
            }
        }
    }
    //    提货码信息显示
    function searchSn() {
        $.ajax({
            url: "searchBySn.jhtml",
            type: "POST",
            data: {sn: $("#sn").val()},
            dataType: "json",
            cache: false,
            success: function (message) {
                if (message.type != "success") {
                    $.message(message);
                    $("#username1").hide();
                    $("#orderSn1").hide();
                    $("#deliverCenter1").hide();
                } else {
                    $("#username1").show();
                    $("#orderSn1").show();
                    $("#deliverCenter1").show();
                }
            }
        });
    }

</script>
</body>
</html>