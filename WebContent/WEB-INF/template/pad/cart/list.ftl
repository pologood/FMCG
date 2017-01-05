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
	var $clearCart = $("#clearCart");
	var $submit = $("#submit");
	var $delete= $("div.p_tipsclose");
	var $decrease = $("span.decrease");
	var $increase = $("span.increase");
	var $quantity = $("input[name='quantity']");
	var $recharge = $("#recharge");
	var $return = $("#return");
	var $getProduct = $(".getProduct");
	//关闭充值
	$recharge.prop("disabled",true);
	//返回
	$return.on("tap",function(){
		location.href="${base}/pad/index.jhtml";return false;
	});
	
	$getProduct.on("tap",function(){
		var $this = $(this);
		location.href="${base}/pad/product/detail/"+$this.attr("productId")+".jhtml";return false;
	})
	
	//提交
	$submit.on("tap",function(){
		location.href="${base}/pad/order/info.jhtml";
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
		$.ajax({
			url :"${base}/pad/cart/clear.jhtml",
			type:"post",
			dataType:"json",
			success:function(message){
				ptips(message.content);
				setTimeout(function(){location.reload(true);},1500);
			}
		});
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
					location.reload(true);
				}
			});
		}
		return false;
	});
	//减少
	$decrease.on("tap",function(e){
		var $this = $(this);
		var $quantity= $this.siblings("input");
		var quantity= $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
			$quantity.val(parseInt(quantity) - 1);
		} else {
			$quantity.val(1);
		}
		edit($quantity);
	});
	//增加
	$increase.on("tap",function(e){
		var $this = $(this);
		var $quantity= $this.siblings("input");
		var quantity= $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			$quantity.val(parseInt(quantity) + 1);
		} else {
			$quantity.val(1);
		}
		edit($quantity);
	});
	 // 编辑数量
	function edit($quantity) {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			var $cartItemId = $quantity.closest("tr").find("input[name='cartItemId']");
			var $packagUnitId = $quantity.closest("tr").find("input[name='packagUnitId']");
			var $tempPrice = $quantity.closest("td").siblings("td.tempPrice").find("div.p_red");
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
						ptips(data.message.content);
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
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void();" id="return" class="p_return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">购物车</div>
			<div class="p_editor" id="clearCart">清空购物车</div> <!--
			<div class="p_all">全选</div> -->
		</div>
	</div>
	<table border="0" class="p_carttable p_carttabletop">
		<tr class="p_trhard">
			<td width="40%">商品</td>
			<td width="14%">单价/单位</td>
			<td width="26%">数量</td>
			<td width="14%">小计</td>
			<td width="6%"></td>
		</tr>
	</table>
	<article class="p_article p_articlecart" id="p_contscrooler">
		<div class="bodycont_cart">
			<table border="0" class="p_carttable">
			[#if cart?? && cart.cartItems?has_content]
					[#list cart.cartItems as cartItem]
					<tr>
						<input type="hidden" name="cartItemId" value="${cartItem.id}"/>
						<input type="hidden" name="packagUnitId" [#if cartItem.packagUnit??]value="${cartItem.packagUnit.id}"[/#if]/>
						<td width="40%">
							<div class="p_cartcs">
								<a href="javascript:void();" class="getProduct" productId="${cartItem.product.id}">
									<img src="${cartItem.product.thumbnail}">
									<h1>${cartItem.product.name}</h1>
								</a>
								<p>
									[#if cartItem.product.specification_value??&&cartItem.product.specification_value?has_content]
										[#list cartItem.product.specification_value as specification_value]
											<span><a href="#">${specification_value}</a></span>
										[/#list]
									[/#if]
								</p>
							</div>
						</td>
						<td width="14%"><div class="p_red">
							[#if cartItem.packagUnit??]
								${currency(cartItem.packagUnit.wholePrice,true)}/${cartItem.packagUnit.name}
							[#else]
								${currency(cartItem.product.wholePrice,true)}/${cartItem.product.unit}
							[/#if]
						 </div></td>
						<td width="26%">
							<div class="p_detailcountul">
								<span class="decrease">-</span>
								<input type="text" placeholder="1" name="quantity" maxlength="6" value="${cartItem.quantity}">
								<span class="increase">+</span>
							</div>
						</td>
						<td width="14%" class="tempPrice"><div class="p_red">${currency(cartItem.tempPrice,true)}</div></td>
						<td width="6%"><div class="p_cartselect"><div class="p_tipsclose" cartItemId="${cartItem.id}"></div></div></td>
					</tr>
					[/#list]
			[#else]
				<p>购物车为空</p>
			[/#if]
			</table>
		</div>
	</article>
	<footer class="p_footer p_cartfooter">
		<ul>
			<li class="p_cartfootf">当前余额: <strong>${currency(member.balance,true)}</strong>
				<span class="p_recharge" id="recharge">充值</span>
			</li>
			<li>商品总价: <strong id="effectivePrice">
			[#if cart??]${currency(cart.price,true)}[#else]${currency(0,true)}[/#if]</strong><br/>
				赠送积分: <strong id="effectivePoint">[#if cart??]${cart.point}[#else]0[/#if]</strong>
			</li>
			<div class="p_clear"></div>
		</ul>
		<a href="javascript:void();" id="submit" class="p_cartset">提交订单</a>
	</footer>
</section>
<div class="p_rechargead">
	<div class="p_receiptcont">
		<div class="p_receipttop">
			充值
			<span class="p_returndd1">返回</span>
			<span class="p_returncz">确定</span>
		</div>
		<div class="p_receiptdder" id="p_resscroller_1">
			<ul class="bodycont_list">
				<li>当前余额：<h1>￥10.0</h1></li>
				<li>
					<div class="p_addtext"><input type="text" placeholder="输入充值金额"></div>
				</li>
				<li class="p_explanation">
					<h2>充值说明：</h2>
					<p><span>1.</span>使用信用卡充值，建议单笔金额不要超过5000元，大额分多笔支付，低于5000元参考银行网付限额《银行支付限额表》。</p>
					<p><span>2.</span>使用网银充值，收取充值金额的0.3%作为手续费，手续费不设上下限且没有免手续费优惠。</p>
					<p><span>3.</span>通过“银行汇款”和“签约账户”进行充值均不收取手续费，账户签约目前只支持中国银行。</p>
					<p><span>4.</span>银行汇款，请您在周一至周五的9：00-17：30选择“普通活期”存储方式完成柜台汇款，汇款 成功后1-2个工作日即可到账。</p>
				</li>
			</ul>
		</div>
	</div>
</div>
<div class="p_searchfixed">
	<div class="p_search_icon"></div>
	<input type="text" placeholder="请输入搜索内容"/>
	<div class="p_search_button">搜索</div>
</div>
<div class="p_windowbg_1"></div>
<div class="p_receiptbg"></div>
</body>
</html>
