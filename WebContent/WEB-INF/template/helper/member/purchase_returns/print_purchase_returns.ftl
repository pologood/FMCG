<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.print.order")}</title>
<meta name="author" content="zhaoqipei Team" />
<meta name="copyright" content="zhaoqipei" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<style type="text/css">
.bar {
	height: 30px;
	line-height: 30px;
	border-bottom: 1px solid #d7e8f8;
	background-color: #eff7ff;
}

table {
	width: 100%;
}

table th {
	font-weight: bold;
	text-align: left;
}

table td, table th {
	line-height: 30px;
	padding: 0px 4px;
	font-size: 14pt;
	color: #000000;
}

.separated th, .separated td {
	border-top: 1px solid #000000;
	border-bottom: 1px solid #000000;
}
</style>
<style type="text/css" media="print">
.bar {
	display: none;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $print = $("#print");

	$print.click(function() {
		window.print();
		return false;
	});

});
</script>
</head>
<body>
	<div class="bar">
		<a href="javascript:;" id="print" class="button">${message("admin.print.print")}</a>
	</div>
	<div class="content">
        <table class="input" style="margin-bottom: 30px;">
            <tr>
                <th>
                    <img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}"/>
                </th>
                <td colspan="5">
                    <h2 style="text-align:center;">采购退货单</h2>
                </td>
            </tr>
            <tr>
                <th>
                    收货人:
                </th>
                <td colspan="3">
				${purchaseReturns.operator}
                </td>
                <th>
                    手机号码:
                </th>
                <td>
				${member.username}
                </td>
            </tr>
            <tr>
                <th>
                    订单编号:
                </th>
                <td colspan="3">
				${purchaseReturns.sn}
                </td>
                <th>
                    创建日期:
                </th>
                <td>
				${purchaseReturns.purchaseDate?string("yyyy-MM-dd")}
                </td>
            </tr>
            <tr>
                <th>
                    打印日期:
                </th>
                <td colspan="3">
				${.now?string("yyyy-MM-dd")}
                </td>
                <th>

                </th>
                <td>

                </td>
            </tr>
        </table>
        <table class="input">
            <tr class="title">
                <th>序号</th>
                <th>${message("ShippingItem.name")}</th>
                <th>商品条码</th>
                <th>商品价格</th>
                <th>数量</th>
                <th>小计</th>
            </tr>
		<#list purchaseReturns.purchaseItems as purchaseItem>
            <tr>
                <td>
				${purchaseItem_index + 1}
                </td>
			<#--<td>-->
			<#--${purchaseItem.sn}-->
			<#--</td>-->
                <td>
				${abbreviate(purchaseItem.name, 50, "...")}
                </td>
                <td>
				${(purchaseItem.barcode)!}
                </td>
                <td>
				${currency(purchaseItem.price, true)}
                </td>
                <td>
				${purchaseItem.quantity}
                </td>
                <td>
                    ￥${(purchaseItem.price*purchaseItem.quantity)?string("0.00")}
                </td>
            </tr>
		</#list>

            <td colspan="4" style="text-align: center">总计</td>
            <td></td>
            <td><#assign amountTotal=0>
			<#list purchaseReturns.purchaseItems as purchaseItem>
				<#assign amountTotal=amountTotal+purchaseItem.price*purchaseItem.quantity>
			</#list>
                ￥${amountTotal?string("0.00")}元</td>
		<#--<td>${prices}</td>-->

		<#--[#--（[#if trade.freight==0||trade.freight==null]包邮[#else]邮费：${trade.freight}[/#if]）--->
            </tr>
            <tr>
                <td colspan="7" style="border-bottom: none;">&nbsp;</td>
            </tr>
        </table>
        <table class="input" style="margin-bottom: 30px;">
            <tr style="border-top:1px solid #dde9f5;">
                <th>
                    供应商:
                </th>
                <td style="width: 50%">
				${(purchaseReturns.supplier.name)!}
                </td>

                <th>
                    用户签名:
                </th>
                <td>
                    &nbsp;
                </td>
            </tr>
            <tr style="border-top:1px solid #dde9f5;">
                <td colspan="7" style="text-align: center">
                    官方网址:${setting.webSite}
                </td>
            </tr>
        </table>
	</div>
</body>
</html>