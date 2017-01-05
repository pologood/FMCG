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
	
	
	var productCategoryMap={};
	[@compress single_line = true]
	 [#list productCategoryRootList as rootProductCategory]
			 var tempArray=[];
			 [#if rootProductCategory.children?has_content]
				 [#list rootProductCategory.children as childrenOne]
						 var temp= {id: "${childrenOne.id}",name:"${childrenOne.name}",image:"${childrenOne.image}"};
			 			  tempArray.push(temp);
			 			  var tempThree=[];
						  [#list childrenOne.children as childrenThree]	
						  	[@tag_list id=12]
								[#list tags as tag]
									[#if childrenThree.tags??&&childrenThree.tags?seq_contains(tag)] 
									  	var tempT={id:"${childrenThree.id}",name:"${childrenThree.name}"};
									  	tempThree.push(tempT);
									[/#if]
								[/#list]
							[/@tag_list]
						  	[#if count<3]
						  	[/#if]
						  [/#list]
						  productCategoryMap["${childrenOne.id}"]=tempThree;
				 [/#list]
				productCategoryMap["${rootProductCategory.id}"]=tempArray;
		     [/#if]
	 [/#list]
	[/@compress]
	$("#scan_fun").on("tap",function(){
		location.href="vsstoo://scanFunc";return false;
	});
	
	//根分类li点击展开
	 var $root = $("li[name='root']");
	 var $secondCategory = $("#secondCategory");
	 $root.on('tap',function(e){
				var $this = $(this);
				var productCategoryId=$this.attr("productCategoryId");
				var productCategoryName=$this.attr("productCategoryName");
				$secondCategory.empty();
				$.each(productCategoryMap[productCategoryId],function(i,productCategory){
					var tempStr="<li productCategoryId="+productCategory.id+"><div class='class_cont'><img src="+productCategory.image+"><p>"+productCategory.name+"</p></div></li>";
					$secondCategory.append(tempStr);
				});
				$secondCategory.append("<div class='m_clear'></div>");
				$("#secondCategory").iScroll('refresh');
				$("#secondCategory").find("li").on("tap",function(){
					var $this =$(this);
					location.href="${base}/mobile/product/list/"+$this.attr("productCategoryId")+".jhtml"; return false;
				});
			return false;
		});
		// setTimeout(function(){$(".m_classdown").trigger("tap");},500);
});
</script>
<style>
.m_search{display:block;left:10px;}
.m_productt h1{padding:0 5px;}
.m_productt ul{padding:0;}
.m_productt ul li{width:33.3%;}
.m_productt ul li:nth-child(2) a,.m_productt ul li:nth-child(1) a,.m_productt ul li:nth-child(3) a{padding:0 2.5px 2.5px 2.5px;}
.m_search.m_search_n .m_searchon{background: rgba(0,0,0,.1);}
.m_search.m_search_n .p_search_icon{right:auto;}
.m_search.m_search_n .m_searchon span{display:block;}
</style>
</head>
<body>
[@area_current]
 [#assign areaId=currentArea.id]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont">
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_scan" id="scan_fun"><i class="iconfont">&#xe613;</i></div>
		</div>
	</header>
	<article class="m_articlecl" id="m_clscrooler">
		<div class="m_bodycont_1">
			<div class="m_classone">
				<ul>
					[@product_category_root_list]
					[#list productCategories as rootProductCategory]
						[@product_category_children_list productCategoryId=rootProductCategory.id ]
							[#list productCategories as secondProductCategory]
							 	<li name="root" productCategoryName="${secondProductCategory.name}" productCategoryId="${secondProductCategory.id}" [#if secondProductCategory_index==0]class="m_classdown"[/#if]>
							 		<img src="${secondProductCategory.image}">
							 		<p>${secondProductCategory.name}</p>
							 		<!--
							 		<img src="${secondProductCategory.image}">
									<h1>${secondProductCategory.name}</h1>
									<p>
										[@product_category_children_list productCategoryId=secondProductCategory.id tagIds=12 count=3]
											[#list productCategories as productCategory]
												[#if productCategory_has_next]
													${productCategory.name} |
												[#else]
													${productCategory.name}
												[/#if]
											[/#list]
										[/@product_category_children_list]	
									</p>
							 		-->
							 	</li>
							[/#list]
						[/@product_category_children_list]
					[/#list]
					[/@product_category_root_list]
					<div class="m_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<article class="m_articlecr" id="m_crscrooler">
		<div class="m_bodycont_1">
			<div class="m_classtwo">
				<ul id="secondCategory">
				</ul>
			</div>
		</div>
	</article>
</section>
[#include "/mobile/include/area_select.ftl" /]	
[/@area_current]
<div class="m_Areas_bg"></div>
<div class="m_bodybg"></div>
</body>
</html>
