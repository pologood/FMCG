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
$().ready(function() {
	var $addReview = $("#addReview");
	var $productId = $("#productId");
	$addReview.on('tap',function(){
		location.href="${base}/mobile/member/review/add/"+$productId.val()+".jhtml";
	});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a href="javascript:void(0);" alt="返回" id="return_btn"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">商品评价</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_writh" id="m_scrooler">
		<div class="m_bodycont">
		<input type="hidden" id="productId" value="${product.id}">
			<div class="p_zxdder">
				<ul class="bodycont_list">
					<li>
						<a href="javascript:;" id="productDetail" productId="${product.id}">
							<img src="${product.thumbnail}" width="80px;" height="80px;">
							<p>${product.name}</p>
							<p><span>${currency(product.wholePrice,true)}</span><span>${currency(product.price,true)}</span></p>
						</a>
					</li>
				</ul>
			</div>
			<div class="m_detalims">
				<div class="m_evaluatel">
				<ul>
					[#if reviews?has_content]
					[#list reviews as review]
					[#if review.isShow]
					<li>
						<p>${review.content}</p>
						<div class="m_evaluate_text">
							<span>${review.member.username}</span>
							<span>
								[#if product.specification_value??&&product.specification_value?has_content]
									[#list product.specification_value as specification_value]
										${specification_value}
									[/#list]
								[/#if]
							</span>
						</div>
						<span class="score${(review.score * 2)?string("0")}"></span>
						<span>${review.createDate?string("yyyy-MM-dd HH:mm:SS")}</span>
					</li>
					[/#if]
					[/#list]
					[/#if]
					<div class="p_clear"></div>
				</ul>
			</div>
			</div><!-- 
			<div class="m_btn"><a href="javascript:void(0)" id="addReview">我要评价</a></div> -->
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
