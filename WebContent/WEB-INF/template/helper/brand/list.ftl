<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
[@seo type = "brandList"]
	<title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.brand.title")}[/#if][#if systemShowPowered][/#if]</title>
	[#if seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/helper/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${base}/resources/helper/css/helper-common.css">
<link href="${base}/resources/helper/css/font.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/helper/css/product.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/helper/css/brand.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lazyload.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $logo = $("#list img");
  $("#mainMenu").show();

	$logo.lazyload({
		threshold: 100,
		effect: "fadeIn"
	});
	
});
</script>
</head>
<body>
	<div class="container review">
		<div class="mainmenu">
			<div class="hotProduct">
				<div class="title">${message("shop.product.hotProduct")}</div>
				<ul>
					[@product_list count = 10 orderBy="monthSales desc"]
						[#list products as product]
							<li[#if !product_has_next] class="last"[/#if]>
								<a href="${base}/helper/product/content/${product.id}.jhtml" title="${product.name}">${abbreviate(product.name, 30)}</a>
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
					<li class="last">${message("shop.brand.title")}</li>
				</ul>
			</div>
			<div id="list" class="list clearfix">
				[#if page.content?has_content]
					<ul>
						[#list page.content?sort_by("type")?chunk(4) as row]
							[#list row as brand]
								<li[#if !row_has_next] class="last"[/#if]>
									<a href="${base}${brand.path}">
										<img src="${base}/upload/image/blank.gif"[#if brand.logo??] data-original="${brand.logo}"[/#if] />
										<span title="${brand.name}">${brand.name}</span>
									</a>
								</li>
							[/#list]
						[/#list]
					</ul>
				[#else]
					${message("shop.brand.noResult")}
				[/#if]
			</div>
			[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "{pageNumber}.jhtml"]
				[#include "/helper/include/pagination.ftl"]
			[/@pagination]
		</div>
	</div>
</body>
</html>