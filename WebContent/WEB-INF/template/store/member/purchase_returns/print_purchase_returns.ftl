<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>${message("admin.print.order")}</title>
	<meta name="author" content="zhaoqipei Team" />
	<meta name="copyright" content="zhaoqipei" />
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
	<style type="text/css">
		table th {
			font-weight: bold;
			background: #fafafa;
		},
		table th,table td{
			text-align: left;
			font-size: 14pt;
			color: #000000;

		}
	</style>
	
</head>
<body>
	<div class="col-sm-offset-0 col-sm-2 mt5 mb5" style="float:none;">
		<button id="print" class="btn btn-block btn-default">打印</button>
	</div>
	<div class="content" style="padding:0px;">
		<table class="table tabs" style="margin-bottom: 30px;">
			<tr>
				<th style="width:160px;background:white;">
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
		<table class="table tabs">
			<tr>
				<th>序号</th>
				<th>${message("ShippingItem.name")}</th>
				<th>商品条码</th>
				<th>商品价格</th>
				<th>数量</th>
				<th>小计</th>
			</tr>
			[#list purchaseReturns.purchaseItems as purchaseItem]
			<tr>
				<td>
					${purchaseItem_index + 1}
				</td>
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
			[/#list]

			<td colspan="4" style="text-align: center">总计</td>
			<td></td>
			<td>[#assign amountTotal=0]
				[#list purchaseReturns.purchaseItems as purchaseItem]
				[#assign amountTotal=amountTotal+purchaseItem.price*purchaseItem.quantity]
				[/#list]
				￥${amountTotal?string("0.00")}元</td>
			</tr>
			<tr>
				<td colspan="7" style="border-bottom: none;">&nbsp;</td>
			</tr>
		</table>
		<table class="table tabs" >
			<tr style="border-top:1px solid #dde9f5;">
				<th>
					供应商:
				</th>
				<td>
					${(purchaseReturns.supplier.name)!}
				</td>

				<th>
					用户签名:
				</th>
				<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			<tr style="border-top:1px solid #dde9f5;">
				<th>
					官方网址:
				</th>
				<td colspan="3">
					${setting.webSite}
				</td>
			</tr>
		</table>
	</div>
	[#include "/store/member/include/bootstrap_js.ftl"]
	<script type="text/javascript">
		$().ready(function() {

			var $print = $("#print");

			$print.click(function() {
				window.print();
				return false;
			});

		});
	</script>
</body>
</html>