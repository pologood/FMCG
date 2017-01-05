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
<script type="text/javascript" src="${base}/resources/mobile/js/detail.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $clearCart = $("#clearCart");
	var $submit = $("#submit");
	var $delete= $("div.p_tipsclose1");
	var $decrease = $("span.decrease");
	var $increase = $("span.increase");
	var $return = $("#return");
	var $getProduct = $(".getProduct");
	var $quantity = $("input[name='quantity']");
	
	//数量
	$quantity.on('keypress',function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
	});
	//商品详情
	$getProduct.on("tap",function(){
		var $this = $(this);
		location.href="${base}/mobile/product/content/"+$this.attr("productId")+".jhtml";return false;
	})
	
	//提交
	$submit.on("tap",function(){
		location.href="${base}/mobile/member/order/info.jhtml";
		return false;
	});
	
	// 改变数量
	$quantity.on('input propertychange change',function(e){
		if (e.type != "propertychange" || e.originalEvent.propertyName == "value") {
			var $this=$(this);
			edit($this);
		}
	});
	
	//清空购物车
	$clearCart.on("tap",function(e){
		if (confirm("确定清空吗?")) {
			$.ajax({
				url :"${base}/mobile/cart/clear.jhtml",
				type:"post",
				dataType:"json",
				success:function(message){
				setTimeout(function(){location.reload(true);},1000);
			}
			});
		}
		return false;
	});
	//删除
	$delete.on("tap",function(e){
		var $this = $(this);
		var cartItemId = $this.attr("cartItemId");
		if (confirm("确定删除？")) {
			$.ajax({
				url: "delete.jhtml",
				type: "POST",
				dataType: "json",
				data:{id:cartItemId},
				success: function(data) {
					setTimeout(function(){location.reload(true);},1000);
				}
			});
		}
		return false;
	});

	 // 编辑数量
	function edit($quantity) {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			var $cartItemId = $quantity.parent().siblings("input[name='cartItemId']");
			var $packagUnitId = $quantity.parent().siblings("input[name='packagUnitId']");
			var $tempPrice = $quantity.siblings(".tempPrice");
			var $effectivePrice= $("#effectivePrice");
			var $effectivePoint= $("#effectivePoint");
			$.ajax({
				url:"edit.jhtml",
				type:"post",
				data:{id:$cartItemId.val(),quantity:quantity,packagUnitId:$packagUnitId.val()},
				dataType:"json",
				cache: false,
				beforeSend: function() {
					$submit.prop("disabled", true);
				},
				success:function(data){
					if (data.message.type == "success") {
						$effectivePrice.text(currency(data.effectivePrice,true));
						$effectivePoint.text(data.effectivePoint);
						$tempPrice.text(currency(data.subtotal,true));
					}else{
						mtips(data.message.content);
					}
				},
				complete: function() {
					$submit.prop("disabled", false);
				}
			});
		}
		return false;
	}
});
</script>
</head>
<body>
[@area_current]
 [#assign areaId=currentArea.id]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_city" areaId="${currentArea.id}"><i class="iconfont">&#xe602</i><div>${currentArea.name}</div></div>
			<div class="m_title" alt="选择日期">购物车</div>
			[#if cart?? && cart.cartItems?has_content]
				<div class="m_member"><a href="javascript:;" alt="清空" id="clearCart">清空</a></div>
			[/#if]
		</div>
	</header>
	<article class="m_article" id="m_scrooler">
		<div class="m_bodycont_1">
			<div class="m_cartlist">
				<ul>
				[#if cart?? && cart.cartItems?has_content]
					[#list cart.cartItems as cartItem]
					<li>
						<input type="hidden" name="cartItemId" value="${cartItem.id}"/>
						<input type="hidden" name="packagUnitId" [#if cartItem.packagUnit??]value="${cartItem.packagUnit.id}"[/#if]/>
						<a href="javascript:void(0);" class="getProduct" productId="${cartItem.product.id}">
							<img src="${cartItem.product.thumbnail}" width="60px;" height="60px;">
							<h1>${cartItem.product.name}</h1>
						</a>	
						<p>
						[#if cartItem.product.specification_value??&&cartItem.product.specification_value?has_content]
							[#list cartItem.product.specification_value as specification_value]
								<span><a href="javascript:void(0);">${specification_value}</a></span>
							[/#list]
						[/#if]
						</p>
							[#if cartItem.packagUnit??]
									<p class="m_cartatout">
										<input type="text" name="quantity" maxlength="6" value="${cartItem.quantity}"/>
										<span>${currency(cartItem.packagUnit.price,true)}/${cartItem.packagUnit.name}</span>
										<span class="tempPrice">${currency(cartItem.tempPrice,true)}</span>
									</p>
							[#else]
									<p class="m_cartatout">
										<input type="text" name="quantity" maxlength="6" value="${cartItem.quantity}"/>
										<span>${currency(cartItem.product.price,true)}/${cartItem.product.unit}</span>
										<span class="tempPrice">${currency(cartItem.tempPrice,true)}</span>
									</p>
							[/#if]
						<div class="m_cartselect"><div class="p_tipsclose1" cartItemId="${cartItem.id}"></div></div>
					</li>
					[/#list]
				[#else]
					<a href="${base}/mobile" style="padding:20px;display:block;color:#666;text-align:center"><p>您的购物车是空的,立即去商城逛逛吧</p></a>
				[/#if]
				</ul>
			</div>
		</div>
	</article>
	[#if cart?? && cart.cartItems?has_content]
	<footer class="m_footer">
		<div class="m_detailbtn m_cartbtn">
			<ul>
				<li>
					<p>余额：<span>[#if member??]${currency(member.balance,true)}[#else]--[/#if]</span> 合计：<span id="effectivePrice">[#if cart??]${currency(cart.effectivePrice,true)}[#else]${currency(0,true)}[/#if]</span></p>
					<p>积分：<span id="effectivePoint">[#if cart??]${cart.point}[#else]0[/#if]</span></p>
				</li>
				<li><a href="javascript:void(0)"><span id="submit">去结算</span></a></li>
			</ul>
		</div>
	</footer>
	[/#if]
	<div class="m_bodybg"></div>
</section>
[#include "/mobile/include/area_select.ftl" /]	
[/@area_current]
<div class="m_Areas_bg"></div>
</body>
</html>
