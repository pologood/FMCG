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
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.order.view")}
</div>
<ul id="tab" class="tab">
    <li>
        <input type="button" value="${message("admin.paybill.info")}"/>
    </li>
    <li>
        <input type="button" value="${message("admin.order.rebate")}"/>
    </li>
</ul>
<table class="input tabContent">

    <tr>
        <td>
        </td>
        <td>
        </td>
    </tr>

    <tr>
        <th>
        创建日期
        </th>
        <td>
        ${paybill.createDate}
        </td>
        <th>
        付款日期
        </th>
        <td>
            [#if paybill.payment??]
                ${paybill.payment.paymentDate}
            [/#if]
        </td>
    </tr>
    <tr>
        <th>
        订单
        </th>
        <td>
        [#if (paybill.payment)??]
        ${(paybill.payment.order.id)!"-"}
        [#else]
            -
        [/#if]
        </td>
        <th>
        编号
        </th>
        <td>
        ${paybill.sn}
        </td>
    </tr>
    <tr>
        <th>
        付款金额
        </th>
        <td>
        ${currency(paybill.amount, true)}
        </td>
        <th>
        会员
        </th>
        <td>
        ${paybill.member.username}
        </td>
    </tr>
    <tr>
        <th>
        店铺名称
        </th>
        <td>
        ${paybill.tenant.name}
        </td>
        <th>
        状态
        </th>
        <td>
        ${message("PayBill.Status." + paybill.status)}
        </td>
    </tr>
    <tr>
        <th>
        类型
        </th>
        <td>
        [#if paybill.type??]
        ${message("PayBill.Type." + paybill.type)}
        [/#if]
        </td>
        <th>
        方式
        </th>
        <td>
        [#if paybill.payment??]
        ${(message("Payment.Method." + paybill.payment.method)!"-")}
        [#else]
            -
        [/#if]
        </td>
    </tr>
    <tr>
        <th>
        支付方式
        </th>
        <td>
        [#if paybill.payment??]
        ${(paybill.payment.paymentMethod)!"-"}
        [#else]
            -
        [/#if]
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
[#list paybill.rebate as rebates]
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
            [#if rebates.member??]
                [#if rebates.member.tenant??&&rebates.member.id==rebates.member.tenant.member.id]店主(${rebates.member.tenant.name})[/#if]
                [#if rebates.member.tenant??&&rebates.member.id!=rebates.member.tenant.member.id]用户[/#if]
            [#else]
                平台
            [/#if]
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