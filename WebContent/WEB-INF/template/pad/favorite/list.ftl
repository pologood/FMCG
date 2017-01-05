<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
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
	 var $return=$("#return");
	 var $product= $("#product_list li a");
	 
	 //
	 $product.on("tap",function(){
	 	var $this = $(this);
	 	location.href=" ${base}/pad/product/detail/"+$this.attr("productId")+".jhtml";
	 	return false;
	 });
	//
	 $return.on("touchstart",function(){
	 	location.href="${base}/pad/member/center.jhtml";return false;
	 });
});
</script>
</head>
<body>
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void();" class="p_return" id="return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">我的收藏</div>
		</div>
	</div>
	<article class="p_article p_articlecart" id="p_contscrooler">
		<div class="bodycont_detail">
			<h1 class="p_recenttitle">商品收藏</h1>
			<div class="p_recentlist" id="p_resslistcroller">
				<ul id="product_list">
					[#if page.content??&&page.content?has_content]
						[#list page.content as product]
							<li>
								<a href="javascript:void();" productId="${product.id}" >
									<img src="${product.thumbnail}">
									<h1>${currency(product.price,true)}<del>${currency(product.marketPrice,ture)}</del></h1>
									<p>${product.fullname}</p>
								</a>
							</li>
						[/#list]
					[/#if]
					<div class="p_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<footer class="p_footer p_cartfooter">
		<ul>
			<li>
				<div class="p_membername">
					<img src="${(member.tenant.logo)!}" alt="${(member.tenant.name)!}">
					<p><strong>${member.name}</strong></p>
					<p>${(member.tenant.name)!}</p>
				</div>
			</li>
			<li class="p_cartfootf p_memberf">当前余额: <strong>${currency(member.balance,true)}</strong>
				<span class="p_recharge">充值</span>
				<br>
					积分: <strong>${member.point}</strong>
			</li>
			<div class="p_clear"></div>
		</ul>
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
