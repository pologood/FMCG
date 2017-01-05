<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}-温馨提示</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $orderList = $("#orderList");
	$orderList.on('tap',function(){
		location.href = "${base}/mobile/member/order/list.jhtml";
		return false;
	});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			 <div class="m_return" id="return_btn"><a href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			 [#include "/mobile/include/top_search.ftl" /]
			 <div class="m_title" alt="选择日期">温馨提示</div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont">
			<div class="m_statetitle m_stateok">
				<h1>您的团购已提交，等待商家确认发货！</h1>
				<span><i class="iconfont">&#xe60c</i></span>
			</div>
			<div class="m_h10"></div>
			<div class="m_statetable">
				<h1 colspan="4">团购信息:</h1>
				<div class="m_statetable_div m_statetable_divt">
					<ul>
						<li width="50">${message("shop.order.image")}</li>
						<li>${message("shop.order.product")}</li>
						<li>团购价</li>
						<li>${message("shop.order.quantity")}</li>
						<div class="m_clear"></div>
					</ul>
				</div>
				[#list promotion.promotionProducts as promotionProduct]
				<div class="m_statetable_div">
					<ul>
						<li>
							<img width="50px" src="[#if promotionProduct.product.thumbnail??]${promotionProduct.product.thumbnail}[#else]${setting.defaultThumbnailProductImage}[/#if]" alt="${promotionProduct.product.name}" />
						</li>
						<li>
							<a href="${base}/mobile/product/detail/${promotionProduct.product.id}.jhtml" title="${promotionProduct.product.fullName}" target="_blank">${abbreviate(promotionProduct.product.fullName, 50, "...")}</a>
						</li>
						<li>
							${currency(promotionProduct.price, true)}
						</li>
						<li>
							${promotionProduct.quantity}
						</li>
						<div class="m_clear"></div>
					</ul>
				</div>
				[/#list]
				<div class="m_clear"></div>
			</div>
			<div class="m_h10"></div>
			<a id="orderList" href="javascript:;" class="m_tuanmore">查看团购订单>></a>
			数量:<span>${quantity}</span>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
