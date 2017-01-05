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
<script type="text/javascript">
$().ready(function(){
	var $more = $("#more");
	var $tenantMore = $("#tenantMore");
	var $load = $("#load");
	var $tenantLoad = $("#tenantLoad");
	var $totalPages = $("#totalPages");
	var $pageNumber = $("#pageNumber");
	var $pageSize = $("#pageSize");
	var $totalPages1 = $("#totalPages1");
	var $pageNumber1 = $("#pageNumber1");
	var $pageSize1 = $("#pageSize1");
	var $productList = $("#productList");
	var $tenantList = $("#tenantList");
	var $detail =$("div[name='detail']");
	var $backIndex = $("#backIndex");
	
	//商品详情
	$detail.on('tap',function(){
			$this = $(this);
			var productId = $this.attr('productId');
			location.href="${base}/mobile/product/content/"+productId+".jhtml";
	});
	
	 $tenantList.find("a").on("tap",function(){
			 location.href="${base}/mobile/delivery/"+$(this).attr("tenantId")+"/index.jhtml"; return false;
	 });
	 
	 $backIndex.on("tap",function(){
			location.href="vsstoo://appback/?backapp=true"; return false;
		});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a href="javascript:;" alt="返回" id="backIndex"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_collect_tab">
				<ul>
					<li class="m_down">收藏商品</li>
					<li>关注商家</li>
				</ul>
			</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_sp" id="m_scrooler">
		<div class="m_bodycont_2">
			<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
			<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
			<input type="hidden" id="totalPages" value="${page.totalPages}" />
			<div class="m_listcont">
				<ul id="productList">
					[#if page.content?has_content]
					[#list page.content as product]
					<li>
						<a href="javascript:;">
							<div class="m_productk" name="detail" productId="${product.id}">
								<img height="120px;" src="${product.thumbnail}" >
								<p><span>${currency(product.wholePrice,true)}</span><del>${currency(product.price,true)}</del></p>
								<p>${product.fullName}</p>
							</div>
						</a>
					</li>
					[/#list]
					[/#if]
					 <div class="m_clear"></div>
				 </ul>
			</div>
			[#if page.content?has_content]
			[#else]
			 <div class="m_more"><span>您还没有收藏过任何商品喔!</span></div>
			[/#if]
		</div>
	</article>
	<article class="m_article m_article_1 m_article_shop" id="m_scrooler_1" style="display:none;">
		<div class="m_bodycont_2">
			<input type="hidden" id="pageNumber1" name="pageNumber" value="${pageTenant.pageNumber}" />
			<input type="hidden" id="pageSize1" name="pageSize" value="${pageTenant.pageSize}" />
			<input type="hidden" id="totalPages1" value="${pageTenant.totalPages}" />
			<div class="m_listcont m_shoplist">
				<ul id="tenantList">
					[#list pageTenant.content as tenant]
						[#if tenant.defaultDeliveryCenter??]
					<li>
						<div class="a">
							<div class="m_productk">
								<a href="javascript:;" tenantId="${tenant.defaultDeliveryCenter.id}"><img src="${tenant.thumbnail}"></a>
								<h2><a href="javascript:;" tenantId="${tenant.defaultDeliveryCenter.id}">${tenant.name}</a></h2>
								<p class="padding5">主营：
									[#list tenant.productCategoryTenants as productCategoryTenant]
						     			  [#if productCategoryTenant_index<3]
										 	${productCategoryTenant.name}
										 	[#if productCategoryTenant_has_next]/[/#if]
					                  [/#if]
									[/#list]
								</p>
								<p class="toShowMap address" [#if tenant.defaultDeliveryCenter.location??] defaultDeliveryCenterId="${tenant.defaultDeliveryCenter.id}" [/#if] ><i class="iconfont">&#xe602</i>${tenant.address}</p>
								<div class="m_shopdistance">[#if tenant.distatce??]${currency(tenant.distatce,false,false)}m[#else]暂无信息[/#if]</div>
							</div>
						</div>
					</li>
					[/#if]
					[/#list]
					<div class="m_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
