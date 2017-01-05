<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
<title>订单详情</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/myorder.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $paySubmit = $("#paySubmit");
	$paySubmit.on("tap",function(){
		var $this = $(this);
		$.ajax({
			url:"${base}/mobile/payment/submit.jhtml",
			type:"post",
			data:{sn:$this.attr("sn"),type:'payment',paymentPluginId:'unionpayMobilePlugin'},
			dataType:"json",
			beforeSend:function(){
				$paySubmit.prop("disabled",true);
			},
			success:function(data){
				if(data.type=="success"){
					location.href="vsstoo://payservice/?token=none&paydata="+data.content;
					return false;
				}else{
					mtips(data.content);
				}
			},
			complete:function(){
				$paySubmit.prop("disabled",false);
			}
		});
	});
	//跳转商品详情
	$("#productList").find("a").on("tap",function(){
		location.href="${base}/mobile/product/content/"+$(this).attr("productid")+".jhtml"; return false;
	});
	//跳转评价页面
	$(".reviewTrade").on("tap",function(){
		location.href="${base}/mobile/member/trade/review.jhtml?sn="+$(this).attr("sn"); return false;
	});
	
	//子订单签收
	$(".sign_order").on("tap",function(){
		if (confirm("是否签收？")) {
			$.ajax({
				url: "${base}/mobile/member/trade/complete.jhtml?sn="+$(this).attr("sn"),
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type == "success") {
						mtips("签收成功！");
	        	location.href="${base}/mobile/member/trade/review.jhtml?sn="+$(this).attr("sn"); return false;
					} else {
						mtips(message.content);
					}
				}
			});
		}
		return false;
	});
	//子订单退货
	$(".returnTrade").on("tap",function(){
		if (confirm("是否退货？")) {
			$.ajax({
				url: "${base}/mobile/member/trade/returnTrade.jhtml?sn="+$(this).attr("sn"),
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type == "success") {
						mtips("操作成功！");
						setTimeout(function(){
							location.reload(true);
						},1000);
					} else {
						mtips(message.content);
					}
				}
			});
		}
		return false;
	});
	//取消订单
	var $cancel_order=$(".cancel_order");
	$cancel_order.live("tap",function(){
		var $this =$(this);
		$.ajax({
			url:"${base}/mobile/member/order/cancel.jhtml",
			data:{sn:$this.attr("sn")},
			dataType:"json",
			type:"post",
			beforeSend:function(){
				$cancel_order.prop("disabled",true);
			},
			success:function(data){
				location.reload();
			},
			complete:function(){
				$cancel_order.prop("disabled",false);
			}
		});
	});
});
</script>
<style type="text/css">
	.goods_amount{margin-top:20px;}
</style>
</head>

<body>
<section class="m_section">
  <header class="m_header">
    <div class="m_headercont_1">
      <div class="m_return"><a href="javascript:;" id="return_btn" alt="返回">
        <div class="p_datag">返回</div>
        </a></div>
        [#include "/mobile/include/top_search.ftl" /]
      <div class="m_title" alt="选择日期">订单详情</div>
    </div>
  </header>
  <article class="m_article" id="m_scrooler">
	<div class="m_bodycont_1">
		<div class="m_address m_address_b" >
			<a  href="javascript:;" id="enterReceiver">
				<span><i class="iconfont">&#xe608</i>${trade.order.consignee}</span>
				<span><i class="iconfont">&#xe602</i>${trade.order.address}</span>
				<p><i class="iconfont">&#xe604</i>${trade.order.phone}</p>
			</a>
		</div>
		<div class="m_h10"></div>
	    <div class="m_orderzt">
           <p>订单状态：<span>${message("Order.OrderStatus." + trade.orderStatus)}<${message("Order.ShippingStatus." + trade.shippingStatus)}></span></p>
           <p>订单号：<span>${trade.order.sn}</span></p>
           <p>成交时间：<span>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></p>
	    </div>	   
		<div class="m_order">
			  <div class="order_detail_header">
			  	  <img src="${trade.tenant.thumbnail}"/>
				  <span class="o_list_title">${trade.tenant.name}</span> 
				 					[#list trade.finalOrderStatus as fos]
					            		[#if fos.status=='isExpired']
						             		<a class="trade_detail_unable" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='cancelled']	
								            <a class="trade_detail_unable" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='toReview']	
							             	<a href="javascript:;" class="trade_detail_abled reviewTrade" sn="${trade.sn}" >${fos.desc}</a>
						             	[#elseif fos.status=='completed']	
						             		<a class="trade_detail_unable" href="javascript:;">${fos.desc}<${message("Order.ShippingStatus."+trade.shippingStatus)}></a>
						             	[#elseif fos.status=='waitReturn']	
						             		<a class="trade_detail_unable" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='waitShipping']
						             		<a href="javascript:;" sn="${trade.sn}" class="trade_detail_abled returnTrade">退货</a>	
						             		<a class="trade_detail_unable" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='sign']	
						             		<a href="javascript:;"  sn="${trade.sn}" class="trade_detail_abled sign_order">${fos.desc}</a>
						             		<a href="javascript:;" sn="${trade.sn}" class="trade_detail_abled returnTrade">退货</a>
						             	[#elseif fos.status=='waitPay']	
						             		<a class="trade_detail_abled cancel_order" sn="${trade.order.sn}" href="javascript:;">取消</a>
					            		[/#if]
					            	[/#list]
			  </div>	  
			  <div class="o_goods_list" >
			     <div class="o_list_content" >
				     <ul id="productList">
				      		[#list trade.orderItems as orderItem]
					         <li>
					            <a href="javascript:;" productid="${orderItem.product.id}" class="o_goods"> 
						            <div class="order_goods_img"> <img src="${orderItem.product.thumbnail}"/></div>
						            <div class="o_goods_c">
						              <p class="goods_name">${orderItem.name}</p>
						              <p class="goods_style">[#if orderItem.product.specification_value?has_content]${orderItem.product.specification_value}[/#if]</p>
						            </div>
						            <div class="order_goods_r">
						              <p class="goods_price">${currency(orderItem.price,true)}</p>
						              <p class="goods_amount">x${trade.quantity}</p>
						            </div>
						        </a>
					         </li>
					        [/#list]
				      </ul>
				      <div class="m_clear"></div>
			     </div>      
		  	 </div>
	    </div>
	</div>
  </article>
  <footer class="m_footer">
  		  <div class="o_total">
		  <div class="o_total_c">
		     <p class="o_total_title">总计:</p>
		     <p class="o_total_money">${currency(trade.amount,true)}</p>
		   </div>
		  </div>
		  <div class="o_opreate">
		      <div class="o_opreate_btn">
		      	[#if trade.paymentStatus == 'unpaid' && !trade.expired && trade.order.orderStatus!='cancelled'&&trade.order.paymentMethod.method!='offline']
		      	 	<a class="o_button" href="javascript:;" id="paySubmit" sn="${trade.order.sn}">去付款</a>
		      	[/#if]
		      	[#if trade.order.orderStatus =='cancelled']
		      	  	<a class="o_button" href="javascript:;">已取消</a>
		      	[/#if]
		      </div>
		  </div>
   </footer>
</section>
<div class="m_bodybg ddout" style="display: none;"></div>
</body>
</html>
