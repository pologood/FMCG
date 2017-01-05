<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
[@seo type = "brandContent"]
	<title>[#if seo.title??][@seo.title?interpret /][#else]${brand.name}[/#if][#if systemShowPowered][/#if]</title> 
	[#if seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/store/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${base}/resources/store/css/store-common.css">
<link href="${base}/resources/store/css/font.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/store/css/product.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/store/css/brand.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {
  $("#mainMenu").show();	
});
</script>
</head>
<body>
	<div class="container brandList">
		<div class="mainmenu">
			<div class="hotProduct">
				<div class="title">${message("shop.product.hotProduct")}</div>
				<ul>
					[@product_list count = 6 orderBy="monthSales desc"]
						[#list products as product]
							<li[#if !product_has_next] class="last"[/#if]>
								<a href="${base}/store/product/content/${product.id}.jhtml" title="${product.name}">${abbreviate(product.name, 30)}</a>
								[#if product.scoreCount > 0]
									<div>
										<div>${message("Product.score")}: </div>
										<div class="score${(product.score * 2)?string("0")}"></div>
										<div>${product.score?string("0.0")}</div>
									</div>
								[/#if]
								<div>${message("Product.price")}: <strong>${currency(product.price, true, true)}</strong></div>
								<div>${message("Product.monthSales")}: <em>${product.monthSales}</em></div>
							</li>
						[/#list]
					[/@product_list]
				</ul>
			</div>
		</div>
		<div class="maindesktop last">
			<div class="path">
				<ul>
					<li>
						<a href="${base}/">${message("shop.path.home")}</a>
					</li>
					<li>
						<a href="${base}/brand/list/1.jhtml">${message("shop.brand.title")}</a>
					</li>
				</ul>
			</div>
			<div class="top">
				[#if brand.type == "image"]
					<img src="${brand.logo}" alt="${brand.name}" />
				[/#if]
				<strong>${brand.name}</strong>
				[#if brand.url??]
					${message("shop.brand.site")}: <a href="${brand.url}" target="_blank">${brand.url}</a>
				[/#if]
			</div>
			[#if brand.productCategories?has_content]
				<div class="product">
					<ul>
						<li class="title">
							<a href="${base}/product/list.jhtml?brandId=${brand.id}">${message("shop.brand.product")}</a>
						</li>
						[#list brand.productCategories as productCategory]
							<li>
								<a href="${base}${productCategory.path}?brandId=${brand.id}">${productCategory.name}</a>
							</li>
							[#if productCategory_index == 5]
								[#break /]
							[/#if]
						[/#list]
					</ul>
				</div>
			[/#if]
			<div class="introduction">
				<div class="title">
					<strong>${message("Brand.introduction")}</strong>
					<span>&nbsp;</span>
				</div>
				${brand.introduction}
			</div>
		</div>
	</div>
</body>
</html>