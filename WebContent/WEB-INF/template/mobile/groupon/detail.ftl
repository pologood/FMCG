<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/orders.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $orderForm =$("#orderForm");
	var $submit =$("#submit");
	var $enterReceiver =$("#enterReceiver");
	var $paymentMethodId =$("#paymentMethodId");
	var $paymentMethodName =$("#paymentMethodName");
	var $shippingMethodId =$("#shippingMethodId");
	var $appointmentIdId =$("#appointmentIdId");
	var $shippingMethodName =$("#shippingMethodName");
	var $appointmentName =$("#appointmentName");
	var $paymentMethod=$("#paymentMethod li");
	var $shippingMethod=$("#shippingMethod li");
	var $appointmentId=$("#appointmentId li");
	var $appointment=$("#appointment");
	var $freight=$("#freight");
	var $point=$("#point");
	var $tax=$("#tax");
	var $amountPayable=$("#amountPayable");
	var $isTax=$("#isTax");
	
	$isTax.on("tap",function(){
		calculate();
	});
	$enterReceiver.on("tap",function(){
		location.href="${base}/mobile/groupon/receivers.jhtml?promotionId=${promotion.id}&quantity=${quantity}";return false;
	});
	
	$submit.on("tap",function(){
		if($("#receiverId").val()==""){
			mtips("收货地址未选择！");
			return false;
		}
		var data={id:"${promotion.id}",quantity:"${quantity}",receiverId:$("#receiverId").val()};
		$.ajax({
			url: "${base}/mobile/groupon/submit.jhtml",
			type: "POST",
			data: data,
			dataType: "json",
			cache: false,
			success: function(message) {
				if (message.type == "success") {
					location.href = "${base}/mobile/groupon/notify/"+message.content+".jhtml?quantity=${quantity}";
				} else {
					mtips(message.content);
					setTimeout(function() {
						location.reload(true);
					}, 1000);
				}
			}
		});	
		
	});
	if($("#receiverId").val()==""||$("#receiverId").val()==null){
		mtips("请先填写一个收货地址");
		setTimeout(function() {location.href="${base}/mobile/groupon/receivers.jhtml?promotionId=${promotion.id}&quantity=${quantity}"; return false;}, 1000);
	}
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			<div class="m_title" alt="选择日期">填写订单</div>
		</div>
	</header>
	<article class="m_article" id="m_scrooler">
		<div class="m_bodycont_1">
			<form id="orderForm" action="#" >
			<input type="hidden" id="receiverId" name="receiverId" value="${(receiver.id)!}">
			<div class="m_address m_address_b" id="enterReceiver">
				[#if receiver??]
				<a  href="javascript:;">
						<span><i class="iconfont">&#xe608</i>${receiver.consignee}</span>
						<span><i class="iconfont">&#xe602</i>${receiver.address}</span>
						<p><i class="iconfont">&#xe604</i>${receiver.phone}</p>
						<div class="p_tag"></div>
				</a>
				[#else]
				<a id="enterReceiver" href="javascript:;">
				<span><i class="iconfont">&#xe608</i></span>
				<span><i class="iconfont">&#xe604</i></span>
				<p><i class="iconfont">&#xe602</i></p>
				<div class="p_tag"></div>
				</a>
				[/#if]
			</div>
			<div class="m_h10"></div>
			<h1 class="m_addps_h">购物清单</h1>
			<div class="m_cartlist m_order_setup">
				<ul>
				[#list promotion.promotionProducts as promotionProduct]
						<li>
							<img src="${promotionProduct.product.thumbnail}"/>
							<h1>${promotionProduct.product.name}</h1>
							<p>
							[#list promotionProduct.product.specification_value as specification]
								<span><a herf="javascript:;">${specification}</a></span>
							[/#list]
							</p>
							<p class="m_cartatout"><span>x${promotionProduct.quantity}${promotionProduct.product.packagUnitName}</span><span>${currency(promotionProduct.price,true)}</span></p>
						</li>
				[/#list]
				</ul>
			</div>
			</form>
		</div>
	</article>
	<footer class="m_footer">
		<div class="m_detailbtn m_cartbtn">
			<ul>
				<li>
					<p> 合计：<span id="amountPayable">${currency(promotion.promotionPrice*quantity,true)}</span> 数量：<span>${quantity}</span></p>
					<p>余额：<span>${currency(member.balance,true)}</span>积分：<span id="point"> ${promotion.promotionPoint*quantity}</span></p>
				</li>
				<li><a href="javascript:;" id="submit"><span>确认参团</span></a></li>
			</ul>
		</div>
	</footer>
	<div class="m_bodybg"></div>
</section>
<div class="m_bodybg ddout" style="display: none;"></div>
</body>
</html>
