<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
[@seo type = "articleContent"]
	<title>[#if article.seoTitle??]${article.seoTitle}[#elseif seo.title??][@seo.title?interpret /][/#if][#if systemShowPowered][/#if]</title>
	 
	[#if article.seoKeywords??]
		<meta name="keywords" content="${article.seoKeywords}" />
	[#elseif seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if article.seoDescription??]
		<meta name="description" content="${article.seoDescription}" />
	[#elseif seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/store/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/store/css/article.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $hits = $("#hits");

	// 查看点击数
	$.ajax({
		url: "${base}/article/hits/${article.id}.jhtml",
		type: "GET",
		success: function(data) {
			$hits.text(data);
		}
	});

});
</script>
</head>
<body>
    [#include "/store/include/header.ftl"]
    [#include "/store/include/navigation.ftl"]
	<div class="container articleContent">
		<div class="left">
			<div class="hotArticleCategory">
				<div class="title">${message("shop.article.articleCategory")}</div>
				[@article_category_root_list count = 10]
					[#list articleCategories as category]
						<dl[#if !category_has_next] class="last"[/#if]>
							<dt>
								<a href="${base}/store/article/list/${category.id}.jhtml">${category.name}</a>
							</dt>
							[#list category.children as articleCategory]
								[#if articleCategory_index == 6]
									[#break /]
								[/#if]
								<dd>
									<a href="${base}/store/article/list/${articleCategory.id}.jhtml">${articleCategory.name}</a>
								</dd>
							[/#list]
						</dl>
					[/#list]
				[/@article_category_root_list]
			</div>
			<div class="hotArticle">
				<div class="title">${message("shop.article.hotArticle")}</div>
				<ul>
					[@article_list articleCategoryId = articleCategory.id count = 10 orderBy="hits desc"]
						[#list articles as article]
							<li>
								<a href="${base}/store/article/content/${article.id}.jhtml" title="${article.title}">${abbreviate(article.title, 30)}</a>
							</li>
						[/#list]
					[/@article_list]
				</ul>
			</div>
		</div>
		<div class="span19 last">
			<div class="path">
				<ul>
					<li>
						<a href="${base}/store/index.jhtml">${message("shop.path.home")}</a>
					</li>
					[@article_category_parent_list articleCategoryId = articleCategory.id]
						[#list articleCategories as articleCategory]
							<li>
								<a href="${base}/store/article/list/${articleCategory.id}.jhtml">${articleCategory.name}</a>
							</li>
						[/#list]
					[/@article_category_parent_list]
					<li>
						<a href="${base}/store/article/list/${articleCategory.id}.jhtml">${articleCategory.name}</a>
					</li>
				</ul>
			</div>
			<div class="main" style="min-height:800px;">
				<h1 class="title">${article.title}[#if article.pageNumber > 1] (${article.pageNumber})[/#if]</h1>
				<div class="info">
					${message("shop.article.createDate")}: ${article.createDate?string("yyyy-MM-dd HH:mm:ss")}
					${message("shop.article.author")}: ${article.author}
					${message("shop.article.hits")}: <span id="hits">&nbsp;</span>
				</div>
				<div class="content" style="float:none;width:910px;">${article.content}</div>
			</div>
		</div>
	</div>
    [#include "/store/include/footer.ftl"]
</body>
</html>