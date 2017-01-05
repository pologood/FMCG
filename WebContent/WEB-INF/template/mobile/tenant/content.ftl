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
	var $introduction = $("#introduction");
	var $tenantId = $("#tenantId");
	var $MyProduct = $("#MyProduct");
	var $return_btn = $("#return_btn");
	var $viewVideo = $("#viewVideo");
	var $careTenant = $("#careTenant");
	
	$careTenant.on('tap',function(){
		var tenantId = $tenantId.val();
		var $hasFavorite = $("#hasFavorite");
		if($hasFavorite.val()=="0"){
			//收藏过，点击则取消收藏
			$.ajax({
			url: "${base}/mobile/favorite/tenant/delete.jhtml?id="+$tenantId.val(),
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				if(message.type=='success'){
					$careTenant.removeClass('m_down');
					$hasFavorite.val(1);
					mtips("取消关注");
				}
				location.href="${base}/mobile/tenant/"+tenantId+"/index.jhtml";
			}
		});
		}else{
			//未收藏，点击收藏
		$.ajax({
			url: "${base}/mobile/favorite/tenant/add.jhtml?id="+$tenantId.val(),
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				if(message.type=='success'){
					$careTenant.addClass('m_down');
					$hasFavorite.val(0);
					mtips("关注成功");
					setTimeout(function(){location.href="${base}/mobile/tenant/"+tenantId+"/index.jhtml";}, 1 * 1000);
				}else if(message.type=='error'){
					mtips("请先登录,再关注该商家!");
					setTimeout(function(){location.href="vsstoo://login/?redirectURL=mobile/tenant/"+$tenantId.val()+"/index.jhtml";}, 1 * 1000);
				}else{
					mtips("最多只能关注10个商家");
				}
			}
		});
	 }
		return false;
	});
	
	$introduction.on('tap',function(){
		var tenantId = $tenantId.val();
		location.href="${base}/mobile/tenant/introduction.jhtml?tenantId="+tenantId;
	});
	
	$MyProduct.on('tap',function(){
		var tenantId = $tenantId.val();
		location.href="${base}/mobile/tenant/productList.jhtml?tenantId="+tenantId;
	});
	<!--
	$viewVideo.on('tap',function(){
		location.href="vsstoo://viewVideo?username=[#if tenant.member.username=='happywine']0592000198[#else]${tenant.member.username}[/#if]"; return false;
	});
	-->
});

</script>
</head>
<body>
[@area_current]
 [#assign areaId=currentArea.id]
[/@area_current]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:void(0);" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_title" alt="选择日期">商家详情</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont_1">
		<input type="hidden" id="tenantId" value="${tenant.id}">
		<input type="hidden" id="hasFavorite" value="${hasFavorite}"/>
			<div class="m_shoptop">
				<img src="${tenant.thumbnail}" width="80px;" height="80px;">
				<h1 id="introduction">${tenant.name}</h1>
				<span class="score${(tenant.score * 2)?string("0")}"></span>
				<p>
				[#list tenant.productCategoryTenants as productCategoryTenant]
     			  [#if productCategoryTenant_index<3]
					 	${productCategoryTenant.name}
					 	[#if productCategoryTenant_has_next]/[/#if]
                  [/#if]
				[/#list] 
				</p>
				<div class="m_videobtn" id="viewVideo"></div>
				<!-- <div class="m_shopsc">
					<i class="iconfont">&#xe605</i>
					<p>[#if tenant.distatce??]${tenant.distatce}m[/#if]</p>
				</div> -->
			</div>
			<div class="m_specification m_shopxx m_xxfirst">
				<h1><i class="iconfont">&#xe602</i>${tenant.address}</h1>
				<!-- <div class="p_tag"></div> -->
			</div>
			<div class="m_specification m_shopxx">
				<h1><i class="iconfont">&#xe604</i><a href='tel:${tenant.telephone}'>${tenant.telephone}</a></h1>
				<!-- <div class="p_tag"></div> -->
			</div>
			<div class="m_h10"></div>
			<!-- <div class="m_specification m_nob" id="MyProduct">
				<h1>经营商品<span></span></h1>
				<div class="p_tag"></div>
			</div>
			 -->
			 <div class="m_evaluate">
				<ul>
					<li id="careTenant" class="[#if hasFavorite==0]m_down[#else][/#if]"><a href="javascript:;"><i class="iconfont">&#xe605</i><span>[#if hasFavorite==0]已关注[#else]关注商家[/#if]</span></a></li>
					<li><a href="javascript:;" id="MyProduct"><i class="iconfont">&#xe612</i><span>经营商品>></span></a></li>
				</ul>
			</div>
			<div class="m_h10"></div>
			<div class="m_specification m_llive">
				<h1>猜你喜欢</h1>
				<!--<div class="p_tag"></div>-->
			</div>
			<div class="m_listcont m_listcont_detail">
				<ul>
					[@product_list areaId=areaId tenantId="${tenant.id}" tagIds=5 count=6]
					[#list products as product]
						<li>
							<a href="${base}/mobile/product/content/${product.id}.jhtml">
								<div class="m_productk">
									<img src="${product.thumbnail}" width="140px;" height="140px;">
									<p><span>${currency(product.price,true)}</span><del>${currency(product.marketPrice,true)}</del></p>
									<p>${product.fullName}</p>
								</div>
							</a>
						</li>
					[/#list]
					[/@product_list]
					<div class="m_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
