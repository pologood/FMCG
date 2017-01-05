[#if notifyMessage??]
${notifyMessage}
[#else]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.payment.notify")}[#if systemShowPowered][/#if]</title>

<link href="${base}/resources/store/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${base}/resources/store/css/store-common.css">
<link href="${base}/resources/store/css/font.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/store/css/payment.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/store/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
</head>
<body>
  [#include "/store/include/header.ftl" /]
	<div id="desktop">
	<div class="payment">
			<div class="title">
				[#if payment.status == "wait"]
					${message("shop.payment.waitTitle")}
				[#elseif payment.status == "success"]
					[#if payment.type == "payment"]
						${message("shop.payment.orderTitle")}
					[#elseif payment.type == "recharge"]
						${message("shop.payment.depositTitle")}
					[/#if]
				[#elseif payment.status == "failure"]
					${message("shop.payment.failureTitle")}
				[/#if]
			</div>
				<table class="info" style="margin-top:10px;">
	  		  [#if payment.status == "success"]
					<tr>
						<th>
						  划账时间:
						</th>
						<td>
							${payment.paymentDate?string("yyyy-MM-dd HH:mm:ss")}
						</td>
					</tr>
			   	[/#if]
					<tr>
						<th>
							支付金额:
						</th>
						<td>
							${currency(payment.amount, true)}
						</td>
					</tr>
					<tr>
						<th>
							手续费:
						</th>
						<td>
							${currency(payment.fee, true)}
						</td>
					</tr>
				</table>
			<div class="bottom" style="border-top: solid 1px #e5e5ce;">
				[#if payment.type == "payment"]
					<a href="${base}/store/member/order/view.jhtml?sn=${payment.order.sn}">${message("shop.payment.viewOrder")}</a>
				[#elseif payment.type == "recharge"]
					<a href="${base}/store/member/deposit/list.jhtml">${message("shop.payment.deposit")}</a>
				[/#if]
				| <a href="${base}/store">${message("shop.payment.index")}</a>
			</div>
	</div>
	</div>
	[#include "/store/include/footer.ftl" /]
</body>
</html>
[/#if]