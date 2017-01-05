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
	var $productList=$("#productList li a");
	var $productCategoryList=$("#productCategoryList li a");
	
	$productList.on("tap",function(){
		var $this =$(this);
		location.href="${base}/mobile/product/content/"+$this.attr("productId")+".jhtml"; return false;
	});
	$productCategoryList.on("tap",function(){
		var $this =$(this);
		var tagIds="";
		var $tagIds = $("input[name='tagIds']");
		$tagIds.each(function(){
			var $this = $(this);
			tagIds+="&tagIds="+$this.val();
		});
		location.href="${base}/mobile/product/list/"+$this.attr("productCategoryId")+".jhtml?productCategoryId="+$this.attr("productCategoryId")+""+tagIds; return false;
	});
});
</script>
<style>
.m_search{display:block;}
</style>
</head>
<body>
[@area_current]
 [#assign areaId=currentArea.id]
[/@area_current]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_title" alt="选择日期">${productCategory.name}</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont">
			[#list tagIds as tagId]
		 		<input type="hidden"  name="tagIds" value="${tagId}" />
			[/#list]
			<div class="m_classtj" id="m_cltscroller">
				<ul id="productList">
					[@product_list areaId=areaId tagIds=5 productCategoryId=productCategory.id count=6]
						[#list products as product]
							<li>
								<a productId="${product.id}" href="javascript:;">
									<div class="m_productk">
										<img src="${product.thumbnail}" width="80px;" height="80px;">
										<p><span>${currency(product.wholePrice,true)}</span></p>
										<p>${product.fullName}</p>
									</div>
								</a>
							</li>
						[/#list]
					[/@product_list]
					<div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h10"></div>
			<div class="m_classthree">
				<ul id="productCategoryList">
				[#if productCategoryList?has_content]
					[#list productCategoryList as productCategory]
					 	<li><a productCategoryId="${productCategory.id}" href="javascript:;">${productCategory.name}<span class="p_tag"></span></a></li>
					[/#list]
				[/#if]
					<div class="m_clear"></div>
				</ul>
			</div>
		</div>
	</article>
</section>
<div class="m_bodybg"></div>
</body>
</html>
