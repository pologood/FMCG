<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />
<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $return = $("#return");
	var $pay = $("#pay");
	$pay.on("tap",function(){
		$.ajax({
			url:"depositPayment.jhtml",
			data:{sn:$("#sn").text()},
			dataType:"json",
			type:"get",
			success:function(message){
				ptips(message.content);
				if(message.type=="success"){
					setTimeout(function(){location.href="list.jhtml"; return false;	},2000);
				}
			}
		});
		return false;
	});
	//返回
	$return.on("tap",function(){
		location.href="${base}/pad/order/list.jhtml"; return false;
	});
});
</script>
</head>
<body>
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void();" id="return" class="p_return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">订单详情</div>
		</div>
	</div>
	<article class="p_article p_articlecart" id="p_contscrooler">
		<div class="bodycont_orders">
			<div class="p_orderdetail_text">
				<ul>
					<li><span>创建日期</span>${order.createDate?string("yyyy-MM-dd hh:MM:ss")}</li>
					<li><span>支付方式</span>${order.paymentMethodName}</li>
					<li><span>配送方式</span>${order.shippingMethodName}</li>
					<li><span>留言</span>${order.memo}</li>
				</ul>
				<ul>
					<li><span>赠送积分</span>${order.point}</li>
					<li><span>商品价格</span>${currency(order.price,true)}</li>
					<li><span>订单金额</span>${currency(order.amount,true)}</li>
				</ul>
			</div>
			<div class="p_h10"></div>
			<div class="p_orderdetail_text">
				<ul>
					<li><span>收货人</span>${order.consignee}</li>
					<li><span>邮编</span>${order.zipCode}</li>
				</ul>
				<ul>
					<li><span>地址</span>${order.address}</li>
					<li><span>电话</span>${order.phone}</li>
				</ul>
			</div>
			<div class="p_h10"></div>
			<table border="0" class="p_carttable">
				<tr class="p_trhard">
					<td width="15%">商品编号</td>
					<td width="40%">商品</td>
					<td width="15%">金额</td>
					<td width="15%">数量</td>
					<td width="15%">小计</td>
				</tr>
				[#if order.orderItems??&&order.orderItems?has_content]
					[#list order.orderItems as item]
						<tr>
							<td>${item.sn}</td>
							<td>
								<div class="p_cartcs">
									<a href="${base}/pad/product/detail/${item.product.id}.jhtml">
										<img src="${item.thumbnail}">
										<h1>${item.name}</h1>
									</a>
									<p>
										[#if item.product.specification_value??&&item.product.specification_value?has_content]
											[#list item.product.specification_value as specification_value]
												<span><a href="#">${specification_value.name}</a></span>
											[/#list]
										[/#if]
									</p>
								</div>
							</td>
							<td><div class="p_red">${currency(item.price,true)}</div></td>
							<td>${item.quantity}</td>
							<td><div class="p_red">${currency(item.subtotal,true)}</div></td>
						</tr>
					[/#list]
				[#else]
					<tr>无订单信息</tr>
				[/#if]
			</table>
		</div>
	</article>
	<footer class="p_footer p_cartfooter">
		<ul>
			<li class="p_cartfootf">订单编号 <strong id="sn">${order.sn}</strong>
			</li>
			<li class="p_cartfootf">
				状态 <strong>${message("Order.PaymentStatus." + order.paymentStatus)}</strong>
			</li>
			<div class="p_clear"></div>
		</ul>
		[#if order.paymentStatus=="unpaid"]
			<a href="javascript:void();" class="p_cartset" id="pay">余额支付</a>
		[/#if]
	</footer>
</section>
<div class="p_searchfixed">
	<div class="p_search_icon"></div>
	<input type="text" placeholder="请输入搜索内容"/>
	<div class="p_search_button">搜索</div>
</div>
<div class="p_windowbg_1"></div>
</body>
</html>
