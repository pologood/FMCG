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
	font-size: 10pt;
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
		<table>
			<tr>
				<td colspan="2" rowspan="2">
					<img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}" />
				</td>
				<td>
					${setting.siteName}
				</td>
				<td>
					&nbsp;
				</td>
				<td colspan="2">
					${message("Order.consignee")}: ${purchase.operator}
				</td>
			</tr>
			<tr>
				<td>
					${setting.siteUrl}
				</td>
				<td>
					&nbsp;
				</td>
				<td colspan="2">
					${message("Order.member")}: ${member.username}
				</td>
			</tr>
			<tr class="separated">
				<th colspan="2">
					${message("Order.sn")}: ${purchase.sn}
				</th>
				<th colspan="2">
					${message("admin.common.createDate")}: ${purchase.purchaseDate?string("yyyy-MM-dd")}
				</th>
				<th colspan="2">
					${message("admin.print.printDate")}: ${.now?string("yyyy-MM-dd")}
				</th>
			</tr>
			<tr>
				<td colspan="6">
					&nbsp;
				</td>
			</tr>
			<tr class="separated">
				<th>
					${message("admin.print.number")}
				</th>
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
					${message("OrderItem.subtotal")}
				</th>
			</tr>
			<#list purchase.purchaseItems as purchaseItem>
				<tr>
					<td>
						${purchaseItem_index + 1}
					</td>
					<td>
						${purchaseItem.sn}
					</td>
					<td>
						${abbreviate(purchaseItem.name, 50, "...")}
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
			<tr  style="border-top: 1px solid #000000">
				<td colspan="6">
					&nbsp;
				</td>
			</tr>
			<#--<!-- tr class="separated">-->
				<#--<td colspan="3">-->
					<#--${message("Order.memo")}: ${order.memo}-->
				<#--</td>-->
				<#--<td colspan="3">-->
					<#--${message("Order.price")}: ${currency(order.price, true)}<br />-->
					<#--${message("Order.fee")}: ${currency(order.fee, true)}<br />-->
					<#--${message("Order.freight")}: ${currency(order.freight, true)}<br />-->
					<#--${message("Order.amount")}: ${currency(order.amount, true)}-->
				<#--</td>-->
			<#--</tr>-->
			<#--<tr>-->
				<#--<td colspan="6">-->
					<#--&nbsp;-->
				<#--</td>-->
			<#--</tr>-->
			<#--<tr class="separated">-->
				<#--<td colspan="3">-->
					<#--&nbsp;-->
				<#--</td>-->
				<#--<td colspan="3">-->
					<#--${message("Order.consignee")}: ${order.consignee}<br />-->
					<#--${message("Order.address")}: ${order.areaName}${order.address}<br />-->
					<#--${message("Order.zipCode")}: ${order.zipCode}<br />-->
					<#--${message("Order.phone")}: ${order.phone}<br />-->
					<#--${message("Order.shippingMethod")}: ${order.shippingMethodName}-->
				<#--</td>-->
			<#--</tr>-->
			<#--<tr>-->
				<#--<td>-->
					<#--&nbsp;-->
				<#--</td>-->
				<#--<td>-->
					<#--&nbsp;-->
				<#--</td>-->
				<#--<td>-->
					<#--&nbsp;-->
				<#--</td>-->
				<#--<td>-->
					<#--&nbsp;-->
				<#--</td>-->
				<#--<td colspan="2">-->
					<#--Powered By 找汽配-->
				<#--</td>-->
			<#--</tr &ndash;&gt;-->
		</table>
	</div>
</body>
</html>