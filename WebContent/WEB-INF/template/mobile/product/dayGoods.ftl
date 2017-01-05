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
</head>
<script type="text/javascript">
$().ready(function(){
	$("#productList_Li_a").find("div.ztimg").on("tap",function(){
		location.href=$(this).attr("url"); return false;
	});
	$("#productCategoryId").on("tap",function(){
		location.href="${base}/mobile/product/list/"+$(this).attr("productCategoryId")+".jhtml"; return false;
	});
});
</script>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:void(0);" alt="返回"><div class="p_datag">返回</div></a></div>
			<div class="m_title" alt="选择日期">天天好货</div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont_1">
			<div class="m_ztbanner">
				[@ad_position id=89 count=3/]
				<h1>${now?string('dd/MM')}</h1>
			</div>
			<div class="zt_title">
				<h1>天天好货</h1>
				<p>-GOODS EVERY DAY-</p>
			</div>
			<div class="ztlist">
				[#if dayGoods??&&dayGoods?has_content]
				<ul id="productList_Li_a">
					[#list dayGoods as product]
					<li>
						<a href="javascript:;" >
							<h1>${product.name}</h1>
							[#if product.productImages??]
								[#if product.productImages.size()==1]
									<div class="ztimg image1" url="${base}/mobile/product/content/${product.id}.jhtml">
										[#list product.productImages as productImage]
											<img src="${productImage.thumbnail}">
										[/#list]
									</div>
								[#elseif product.productImages.size()==2]
									<div class="ztimg image2" url="${base}/mobile/product/content/${product.id}.jhtml">
										[#list product.productImages as productImage]
											<img src="${productImage.thumbnail}">
										[/#list]
									</div>
								[#elseif product.productImages.size() >2]
									<div class="ztimg" url="${base}/mobile/product/content/${product.id}.jhtml">
										[#list product.productImages as productImage]
											[#if productImage_index<=2]
												<img src="${productImage.thumbnail}">
											[/#if]
										[/#list]
									</div>
								[/#if]
							[/#if]
							<div class="m_h10"></div>
							<span id="productCategoryId" productCategoryId="${product.productCategory.id}">${product.productCategory.name}</span>
						</a>
						<div class="m_h10"></div>
					</li>
					[/#list]
				</ul>
				[/#if]
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
