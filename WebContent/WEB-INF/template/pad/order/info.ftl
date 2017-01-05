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
<script type="text/javascript" src="${base}/resources/pad/js/orders.js"></script>
<script type="text/javascript">
$().ready(function(){

	 var $orderForm= $("#orderForm");
	 var $submit= $("#submit");
	 var $receiver= $(".p_receiptadd");
	 var $receiverId= $("#receiverId");
	 var $receiverConsignee= $("#receiverConsignee");
	 var $receiverPhone= $("#receiverPhone");
	 var $receiverAddress= $("#receiverAddress");
	 var $shippingMethodId= $("#shippingMethodId");
	 var $return= $('.p_returnl');
	 var $amountPayable= $("#amountPayable");
	 var $point= $("#point");
	 var $tax= $("#tax");
	 var $freight= $("#freight");
	 var $memberId= $("#memberId");
	 var $balance= $("#balance");
	 
	 var $isInvoice=$("#isInvoice");
	 var $isInvoice_select=$(".isInvoice");
	 var $code = $("#code");
	 var $return_1 = $("#return");
	 var $addShop = $("#addShop");
	 $addShop.on("tap",function(){
	 	location.href="${base}/pad/register/new_account.jhtml";return false;
	 });
	 
	 //返回
	 $return_1.on("tap",function(){
	 	location.href="${base}/pad/cart/list.jhtml";return false;
	 });
	 
	 // 开据发票
	 $isInvoice_select.on("tap",function(){
	 	var $this = $(this);
	 	if($this.hasClass("r_radioup")){
	 		$isInvoice.val("true");
	 	}else{
	 		$isInvoice.val("true");
	 	}
 		calculate();
	 	return false;
	 });
	 
	 //优惠券
	 $code.on("keyup",function(e){
		var $this=$(this);
		if($this.val().length>=10){
			$.ajax({
				url: "coupon_info.jhtml",
				type: "POST",
				data: {code : $this.val()},
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
						$code.val($this.val());
						calculate();
					} 
					alert(data.message.content);
					return false;
				}
			});
		}
	 });
	 
	 //选择收货地址
	 $receiver.on("tap",function(){
	 	var $this = $(this);
	 	$receiverId.val($this.attr("receiverId"));
	 	$receiverConsignee.text($this.attr("receiverConsignee"));
	 	$receiverPhone.text($this.attr("receiverPhone"));
	 	$receiverAddress.text($this.attr("receiverAddress"));
	 	$return.trigger("tap");
	 	$memberId.val($this.attr("memberId"));
	 	$balance.text(currency($this.attr("balance"),true));
	 	return false;
	 });
	 
	 //支付结算
 	 $submit.on("tap",function(e){
 	 	var $p_radior = $(".p_radior");
 	 	$p_radior.each(function(){
 	 		var $this= $(this);
			if($this.hasClass("r_radioup")){
				$shippingMethodId.val($this.attr("shippingMethodId"));
				return false;
			}
 	 	}); 
 	 	if($shippingMethodId.val()==""){
 	 		ptips("请选择配送方式");return false;
 	 	}
 	 	$.ajax({
			url: "create.jhtml",
			type: "POST",
			data: $orderForm.serialize(),
			dataType: "json",
			cache: false,
			beforeSend: function() {
				$submit.prop("disabled", true);
			},
			success: function(message) {
				if (message.type == "success") {
					ptips("下单成功！");
					setTimeout(function(){location.href = "list.jhtml";},1500);
				} else {
					ptips(message.content);
					setTimeout(function(){location.reload(true);},2000);
				}
				return false;
			},
			complete: function() {
				$submit.prop("disabled", false);
			}
		});
 	 });
 	 
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
					$freight.text(currency(data.freight, true));
					$point.text(data.point);
					$tax.text(currency(data.tax, true));
					$amountPayable.text(currency(data.amountPayable, true, true));
				} else {
					setTimeout(function() {
						location.reload(true);
					}, 1000);
				}
			}
		});
	}

});
</script>
</head>
<body>
<section class="p_section">
	[#include "/pad/include/navigation.ftl" /]
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void();" class="p_return" id="return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">订单详情</div>
		</div>
	</div>
	<article class="p_article p_articlecart" id="p_contscrooler">
		<form action="create.jhtml" id="orderForm" method="post">
			<input type="hidden" name="orderId" value="${order.id}">
			<input type="hidden" name="cartToken" value="${cartToken}">
			<input type="hidden" id="isInvoice" name="isInvoice" value="${order.isInvoice}">
			<input type="hidden" id="shippingMethodId" name="shippingMethodId">
			<input type="hidden" name="paymentMethodId" value="${(defaultPaymentmethod.id)!}">
			<input type="hidden" id="receiverId" name="receiverId" value="${(receiver.id)!}">
			<input type="hidden" id="memberId" name="memberId">
			<div class="bodycont_orders">
				<div class="p_address p_addressbc">
					<h1>收货信息</h1>
					<ul class="p_address_input">
						[#if receiver??]
							<li><p><i class="iconfont">&#x3432</i><span id="receiverConsignee">${receiver.consignee}</span></p></li>
							<li><p><i class="iconfont">&#xf005a</i><span id="receiverPhone">${receiver.phone}</span></p></li>
							<li><p><i class="iconfont">&#xf0019</i><span id="receiverAddress">${receiver.address}</span></p></li>
						[/#if]
					</ul>
					<span class="p_watermark"></span>
					<div class="p_tag"></div>
					<div class="p_addressc"></div>
				</div>
				<div class="p_h10"></div>
				<div class="p_address p_addressb">
					<div class="p_addressl p_addressr">
						<h1>配送方式</h1>
						[#if shippingMethods??]
							[#list shippingMethods as shippingMethod]
								<div class="p_action">
									<div class="p_radior" shippingMethodId="${shippingMethod.id}"></div>
									<div class="p_actionspan">${shippingMethod.name}</div>
										${shippingMethod.description}
								</div>
							[/#list]
						[/#if]
					</div>
					<div class="p_clear"></div>
				</div>
				<div class="p_h10"></div>
				<div class="p_address p_addressbg">
					<div class="p_address_cont">
						<div class="p_address_show">
							<div class="p_radiol isInvoice"></div>
							<strong>开具发票</strong>(发票税金: ${order.tax})
						</div>
						<div class="p_invoice p_invoice1">
							<input type="text" placeholder="请输入发票抬头" name="invoiceTitle">
						</div>
					</div>
					<div class="p_address_cont p_tin"><strong>优惠券</strong>
						<div class="p_invoice p_invoicef">
							<input type="text" placeholder="请输入优惠券编号" id="code" name="code">
						</div>
					</div>
				</div>
				<div class="p_h10"></div>
				<div class="p_address p_addressbg">
					<div class="p_address_cont p_tin p_ordertin">
						<div class="p_invoice p_invoicef">
							<input type="text" placeholder="请输入留言内容" name="memo">
						</div>
					</div>
				</div>
				<div class="p_h10"></div>
				<table border="0" class="p_carttable">
					<tr class="p_trhard">
						<td width="46%">商品</td>
						<td width="14%">单价/单位</td>
						<td width="26%">数量</td>
						<td width="14%">小计</td>
					</tr>
					[#if order??&&order.orderItems?has_content]
						[#list order.orderItems as orderItem]
							<tr>
								<td>
									<div class="p_cartcs">
										<a href="${base}/pad/product/detail/${orderItem.product.id}.jhtml">
											<img src="${orderItem.product.thumbnail}">
											<h1>${orderItem.product.name}</h1>
										</a>
										<p>
											[#if orderItem.product.specificationValues?has_content]
												[#list orderItem.product.specificationValues as specificationValue]
													<span><a href="#">${specificationValue.name}</a></span>
												[/#list]
											[/#if]
										</p>
									</div>
								</td>
								<td><div class="p_red">${currency(orderItem.price,true)} /${orderItem.product.unit}</div></td>
								<td>${orderItem.quantity}</td>
								<td><div class="p_red">${currency(orderItem.subtotal,true)}</div></td>
							</tr>
						[/#list]
					[/#if]
				</table>
			</div>
		</form>
	</article>
	<footer class="p_footer p_cartfooter">
		<ul>
			<li class="p_cartfootf">商户余额: <strong id="balance">${currency(member.balance,true)}</strong>
				<span class="p_recharge">充值</span>
			</li>
			<li>应付金额: <strong id="amountPayable">${currency(order.amountPayable,true)}</strong><br/>
				积分: <strong id="point"> ${order.point}</strong> 税金: <strong id="tax">${currency(order.tax)}</strong> 运费: <strong id="freight">${currency(order.freight)}</strong>
			</li>
			<div class="p_clear"></div>
		</ul>
		<a href="javascript:;" class="p_cartset" id="submit">结算</a>
	</footer>
</section>
<div class="p_receipt">
	<div class="p_receiptcont">
		<div class="p_receipttop">
			选择收货地址
			<span class="p_returnl">返回</span>
			<span class="p_returnok" id="addShop">新增</span>
		</div>
		<div class="p_letter_list" id="p_letter_list">
			[#list phonetics as phonetic]
				<a href="#">${phonetic}</a>
			[/#list]
		</div>
		<div class="p_receipttc" id="p_receiptcscroller">
			<ul class="bodycont_list">
				<div class="p_receiptsech">
					<div class="p_search_icon"></div>
					<input type="text" placeholder="请输入搜索内容"/>
					<div class="p_receiptsechbtn">确定</div>
				</div>
				<div class="p_h10"></div>
				[#list phonetics as phonetic]
					[#if members??&&members?has_content]
						[#list members as member]
							[#if member.receivers??&&member.receivers?has_content]
								[#list member.receivers as receiver]
									[#if receiver.phonetic==phonetic]
										<li class="anchor" id="${phonetic}">${phonetic}</li>
										<li>
											<div class="p_address p_receiptadd" receiverId="${receiver.id}" receiverConsignee="${receiver.consignee}" receiverPhone="${receiver.phone}" receiverAddress="${receiver.address}" memberId="${member.id}" balance="${member.balance}" >
												<p><i class="iconfont">&#x3432</i>${receiver.consignee}</p>
												<p><i class="iconfont">&#xf005a</i>${receiver.phone}</p>
												<p><i class="iconfont">&#xf0019</i>${receiver.address}</p>
											</div>
										</li>
									[/#if]
								[/#list]
							[/#if]
						[/#list]
					[/#if]
				[/#list]
			</ul>
		</div>
	</div>
</div>
<div class="p_rechargead">
	<div class="p_receiptcont">
		<div class="p_receipttop">
			充值
			<span class="p_returndd1">返回</span>
			<span class="p_returncz">确定</span>
		</div>
		<div class="p_receiptdder" id="p_resscroller_1">
			<ul class="bodycont_list">
				<li>当前余额：<h1>${currency(member.balance,true)}</h1></li>
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
