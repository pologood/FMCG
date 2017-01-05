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
	var $selectCouponCodeLi=$("#selectCouponCode").find("li");
	var shippingMethodIds={};
	
	[@compress single_line = true]
		[#list paymentMethods as paymentMethod]
			shippingMethodIds["${paymentMethod.id}"] = [
				[#list paymentMethod.shippingMethods as shippingMethod]
					"${shippingMethod.id}"[#if shippingMethod_has_next],[/#if]
				[/#list]
			];
		[/#list]
	[/@compress]
	
	$enterReceiver.on("tap",function(){
		location.href="vsstoo://getReceivers?redirect=${base}/mobile/member/order/info.jhtml";return false;
	});
	$paymentMethod.on("tap",function(){
		var $this = $(this);
		$paymentMethodId.val($this.attr("paymentMethodId"));
		var paymentMethodName=$this.find("em").text();
		$paymentMethodName.text(paymentMethodName);
		if($this.attr("paymentMethod")=="balance"){
			$("#useBalance").val(true);
		}else{
			$("#useBalance").val(false);
		}
		$("#paymentMethod_wx").val($this.attr("paymentMethod"));
		$shippingMethodId.val("");
		$shippingMethodName.text("选择配送方式");
		$("#shippingDescription").text("");
		$shippingMethod.find("div.m_radior").removeClass("r_radioup");
		calculate();
	});
	$shippingMethod.on("tap",function(){
		var $this = $(this);
		if($.inArray($this.attr("shippingMethodId"), shippingMethodIds[$paymentMethodId.val()]) < 0){
			$shippingMethod.find("div.m_radior").removeClass("r_radioup");
			alert("请选择正确配送方式");
			return false;	
		}
		$shippingMethodId.val($this.attr("shippingMethodId"));
		var shippingMethodName=$this.find("em").text();
		$shippingMethodName.text(shippingMethodName);
		$("#shippingDescription").text($this.attr("shippingDescription"));
		calculate();
	});
	$appointment.find("li").on("tap",function(){
		var $this = $(this);
		$appointmentId.val($this.attr("appointmentId"));
		var appointmentName=$this.find("em").text();
		$appointmentName.text(appointmentName);
		calculate();
	});
	
	$submit.on("tap",function(){
		if($shippingMethodId.val()==""||$shippingMethodId.val()==""||$appointmentId.val()==""){
			mtips("请检查支付方式，配送方式，配送时间是否已选！");return false;
		}
		[#if setting.isInvoiceEnabled??&&setting.isInvoiceEnabled]
		if($isTax.hasClass("r_radioup")){
			$("#isInvoice").val(true);
		}else{
			$("#isInvoice").val(false);
		}
		[/#if]
		if($("#useBalance").val()=="true"){
			$.ajax({
				url:"${base}/mobile/member/order/check_balance.jhtml",
				type:"POST",
				dataType:"json",
				data:$orderForm.serialize(),
				success:function(message){
					if(message.type!="success"){
						 mtips("余额不足，请保证余额足够支付该订单！");
						 return false;
					}
					$.ajax({
						url:"${base}/mobile/member/order/create.jhtml",
						data:$orderForm.serialize(),
						dataType:"json",
						type:"post",
						beforeSend:function(){
							$submit.prop("disabled",true);
						},
						success:function(message){
							if(message.type=="success"){
								mtips("下单成功！");
								setTimeout(function(){location.href="${base}/mobile/member/order/list.jhtml";},1500);
								return false;
							}else{
								mtips("下单失败！");
								setTimeout(function(){location.href="${base}/mobile/member/order/list.jhtml";},1500);
								return false;
							}	
						}
					});		
				}
			});
		}else{
			$.ajax({
			url:"${base}/mobile/member/order/create.jhtml",
			data:$orderForm.serialize(),
			dataType:"json",
			type:"post",
			beforeSend:function(){
				$submit.prop("disabled",true);
			},
			success:function(message){
				if(message.type=="success"){
					if($("#useBalance").val()=="true"){
						mtips("下单成功！");
						setTimeout(function(){location.href="${base}/mobile/member/order/list.jhtml";},1500);
						return false;
					}
					if($("#paymentMethod_wx").val()=="offline"){
							mtips("下单成功！");
							setTimeout(function(){location.href="${base}/mobile/member/order/list.jhtml";},1500);
							return false;
					};
					$.ajax({
						url:"${base}/mobile/payment/submit.jhtml",
						type:"post",
						data:{
							sn:message.content,
							type:"payment",
							paymentPluginId:"chinapayMobilePlugin"
						},
						dataType:"json",
						success:function(message){
							if(message.type=="success"){
								location.href="vsstoo://payservice/?token=none&paydata="+message.content;
								return false;
							}else{
								mtips(message.content);
								setTimeout(function(){
									location.href="${base}/mobile/member/order/list.jhtml";
									return false;
								},1500);
							}
						},
						beforeSend:function(){
							mtips("支付中。。。");
						}
					});
				}else{	
					mtips(message.content);
				}
			},
			complete:function(){
				$submit.prop("disabled",false);
			}
		});
		}
		
	});
	
	var $codeType=$("#codeType");
	var $code=$("#code");
	var $pointLi=$("#pointLi");
	$("#codediv").on("tap",function(){
		$("#pointLi").find("div.m_radio").removeClass("r_radioup");
		$("#pointLi").removeClass("m_addpstagz");
	});
	$selectCouponCodeLi.on("tap",function(){
		$codeType.val("coupon");
		$code.val($(this).attr("couponCode"));
		$("#pointLi").find("div.m_radio").removeClass("r_radioup");
		$("#pointLi").removeClass("m_addpstagz");
		calculate();
	});
	$pointLi.on("tap",function(){
		$codeType.val("point");
		$("#codediv").removeClass("m_addpstagz");
		$("#codediv").find("div.m_radio").removeClass("r_radioup");
		calculate();
	});
	// 积分
	$("input[name='point']").keypress(function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
		
	});
	// 编辑积分
	$("input[name='point']").on("input propertychange change", function() {
		if(parseInt($(this).val())-${order.member.point}>0){
			$(this).val(${order.member.point});
		}
		if(parseInt($(this).val())-${order.couponMaxPoint}>0){
			$(this).val(${order.couponMaxPoint});
		}
		calculate();
	});
	
	$("#confirmPoint").on("tap",function(){
		$codeType.val("point");
		calculate();
	});
	[#if setting.isInvoiceEnabled??&&setting.isInvoiceEnabled]
	$isTax.on("tap",function(){
		if($isTax.hasClass("r_radioup")){
			$("#isInvoice").val(false);
		}else{
			$("#isInvoice").val(true);
		}
		calculate();
	});
	[/#if]
	//计算费用
	function calculate() {
		$.ajax({
			url: "calculate.jhtml",
			type: "POST",
			data: $orderForm.serialize(),
			dataType: "json",
			cache: false,
			beforeSend: function() {
				$submit.prop("disabled", true);
			},
			complete: function() {
				$submit.prop("disabled", false);
			},
			success: function(data) {
				if (data.message.type == "success") {
					$freight.text(data.freight);
					$point.text(data.point);
					$tax.text(data.tax);
					$amountPayable.text(data.amountPayable);
				} else {
					setTimeout(function() {
						location.reload(true);
					}, 1000);
				}
			}
		});
	}
	if($("#receiverId").val()==""||$("#receiverId").val()==null){
		mtips("请先填写一个收货地址");
		setTimeout(function(){location.href="vsstoo://getReceivers?redirect=${base}/mobile/member/order/info.jhtml";}, 1 * 1000);
	}
	if("${paymentMethod.method}"=="balance"){
		$("#useBalance").val(true);
	}
	setTimeout(function(){calculate();},1000);
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
			<form id="orderForm" action="${base}/mobile/member/order/create.jhtml" >
			<input type="hidden" name="orderId" value="${order.id}">
			<input type="hidden" name="cartToken" value="${cartToken}">
			<input type="hidden" id="isInvoice" name="isInvoice" value="${order.isInvoice}">
			<input type="hidden" id="shippingMethodId" name="shippingMethodId" value="${(shippingMethod.id)!}">
			<input type="hidden" id="paymentMethodId" name="paymentMethodId" value="${(paymentMethod.id)!}">
			<input type="hidden" id="appointmentId" name="appointmentId" value="${(appointment.id)!}">
			<input type="hidden" id="receiverId" name="receiverId" value="${(receiver.id)!}">
			<input type="hidden" id="useBalance" name="useBalance" value="${useBalance}"/>
			<input type="hidden" id="code" name="code" />
			<input type="hidden" id="codeType" name="codeType" value=""/>
			<input type="hidden" id="paymentMethod_wx" name="paymentMethod_wx" value="${(paymentMethod.method)!}"/>
			<div class="m_address m_address_b" >
				[#if receiver??]
				<a  href="javascript:;" id="enterReceiver">
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
			<div class="m_address m_addps">
				<h1>支付方式<span id="paymentMethodName">[#if paymentMethod??]${paymentMethod.name}[/#if]</span></h1>
				<div class="p_tag"></div>
				<div class="m_addps_zfxz">
					<ul id="paymentMethod">
					[#if paymentMethods??]
						[#list paymentMethods as paymentMethod]
						<li paymentMethodId="${paymentMethod.id}" paymentMethod="${paymentMethod.method}"><div class="m_radior"></div><em>${paymentMethod.name}</em></li>
						[/#list]
					[/#if]
					</ul>
				</div>
			</div>
			<div class="m_h10"></div>
			<div class="m_address m_addps">
				<h1>配送方式<span id="shippingMethodName">[#if shippingMethod??]${shippingMethod.name}[/#if]</span></h1>
				<div class="p_tag"></div>
				<div class="m_addps_p" id="shippingDescription">
					${shippingMethod.description}
				</div>
				<div class="m_addps_zfxz">
					<ul id="shippingMethod">
					[#if shippingMethods??]
						[#list shippingMethods as shippingMethod]
							<li shippingMethodId="${shippingMethod.id}" shippingDescription="${shippingMethod.description}"><div class="m_radior"></div><em>${shippingMethod.name}</em></li>
						[/#list]
					[/#if]
					</ul>
				</div>
			</div>
			<div class="m_h10"></div>
			<div class="m_address m_addps">
				<h1>配送时间<span id="appointmentName">[#if appointment??]${appointment.name}[/#if]</span></h1>
				<div class="p_tag"></div>
				<div class="m_addps_zfxz">
					<ul id="appointment">
					[#if appointments??]
						[#list appointments as appointment]
							<li><div class="m_radior"></div><em>${appointment.name}</em></li>
						[/#list]
					[/#if]
					</ul>
				</div>
			</div>
			<div class="m_h10"></div>
			<div class="m_address m_addps">
				<div class="m_h5"></div>
				<p class="m_address_p m_address_p_1"><input type="text" name="memo" placeholder="请输入留言内容"></p>
				<div class="m_h5"></div>
			</div>
			[#if setting.isInvoiceEnabled??&&setting.isInvoiceEnabled]
			<div class="m_h10"></div>
			<div class="m_address m_addps m_addps_yh m_addps_fp">
				<h1><div class="m_radio" id="isTax"></div>开具发票<b>(发票税金: 6%)</b></h1>
				<p class="m_address_p m_address_pfp"><input type="text" name="invoiceTitle" placeholder="请输入发票抬头" value="个人"></p>
				<div class="m_h5"></div>
			</div>
			[/#if]
			<div class="m_h10"></div>
			<div id="codediv" class="m_address m_addps m_addps_yh m_addps_fp">
				<h1><div class="m_radio"></div>使用优惠券
					<span style="right:20px;"></span></h1>
				<div class="p_tag"></div>
				<div class="m_addps_zfxz m_addps_zfxz_1">
					<ul id="selectCouponCode">
						[#if couponCodes??&&couponCodes?has_content]
							[#list couponCodes as couponCode]
								<li couponCode="${couponCode.code}"><div class="m_radior"></div><em>${couponCode.coupon.name}</em></li>
							[/#list]
						[/#if]
					</ul>
				</div>
				<div class="m_h5"></div>
			</div>
			<div class="m_h1"></div>
			<!--
			<div id="pointLi" class="m_address m_addps m_addps_yh m_addps_fp m_point">
				<h1 ><div class="m_radio"></div>使用积分</h1>
				<p class="m_address_p m_address_pfp m_address_jf"><input type="text" name="point" placeholder="请输入使用的积分"/><a href="javascript:;" id="confirmPoint">确定</a></p>
				<div class="m_h5"></div>
			</div>
			<div class="m_h10"></div>
			<div class="m_address m_addps m_addps_yh m_addps_fp" id="isBalance">
				<h1><div class="m_radio"></div>余额支付</h1>
				<div class="m_h5"></div>
			</div>
			<div class="m_h10"></div>
			-->
			<h1 class="m_addps_h">购物清单</h1>
			<div class="m_cartlist m_order_setup">
				<ul>
				[#if order??&&order.orderItems?has_content]
					[#list order.orderItems as orderItem]
						<li>
							<img src="${orderItem.product.thumbnail}"/>
							<h1>${orderItem.product.name}</h1>
							<p>
							[#list orderItem.product.specification_value as specification]
								<span><a herf="javascript:;">${specification}</a></span>
							[/#list]
							</p>
							<p class="m_cartatout"><span>${orderItem.quantity}${orderItem.packagUnitName}</span><span>${currency(orderItem.price,true)}</span></p>
						</li>
					[/#list]
				[/#if]
				</ul>
			</div>
			</form>
		</div>
	</article>
	<footer class="m_footer">
		<div class="m_detailbtn m_cartbtn">
			<ul>
				<li>
					<p>余额：<span>${currency(member.balance,true)}</span> 合计：<span id="amountPayable">${currency(order.amountPayable,true)}</span></p>
					<p>积分：<span id="point"> ${order.point}</span >税金：<span id="tax">${currency(order.tax)}</span>运费<span id="freight">${currency(order.freight)}</span></p>
				</li>
				<li><a href="javascript:;" id="submit"><span>立即支付</span></a></li>
			</ul>
		</div>
	</footer>
	<div class="m_bodybg"></div>
</section>
<div class="m_bodybg ddout" style="display: none;"></div>
</body>
</html>
