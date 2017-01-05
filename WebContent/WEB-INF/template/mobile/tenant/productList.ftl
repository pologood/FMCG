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
	var productCategoryTenantMap={};
	[@compress single_line = true]
	 [#list productCategoryTenants as productCategoryTenant]
			 var childrenOne="${productCategoryTenant.children}";
			 var tempArray=[];
			 [#if productCategoryTenant.children?has_content]
				 [#list productCategoryTenant.children as childrenOne]
						 var temp= {id: "${childrenOne.id}",name:"${childrenOne.name}"};
			 			  tempArray.push(temp);
				 [/#list]
				  productCategoryTenantMap["${productCategoryTenant.id}"]=tempArray;
		     [/#if]
	 [/#list]
	[/@compress]
	 var $more = $("#more");
	 var $load = $("#load");
	 var $tenantId = $("#tenantId");
	 var $productCategoryTenantId = $("#productCategoryTenantId");
	 var $productCategoryId = $("#productCategoryId");
	 var $productList = $("#productList");
	 var $productForm = $("#productForm");
	 var $orderType = $("#orderType");
	 var $totalPages = $("#totalPages");
	 var $pageNumber = $("#pageNumber");
	 var $pageSize = $("#pageSize");
	 var $allOrderType =$("li[name='allOrderType']"); 
	 var $careTenant = $("#careTenant");
	 var $viewVideo = $("#viewVideo");
	 
	//根分类li点击展开
	 var $root = $("li[name='root']");
	 var $secondCategory = $("#secondCategory");
	 var tmpStr = "";
	 $root.on('tap',function(e){
				var $this = $(this);
				var productCategoryTenantId=$this.attr("productCategoryTenantId");
				$secondCategory.empty();
				$secondCategory.append("<li><a href='${base}/mobile/tenant/productList.jhtml?tenantId="+$tenantId.val()+"&productCategoryTenantId="+productCategoryTenantId +"'>全部</a></li>");
				$.each(productCategoryTenantMap[productCategoryTenantId],function(i,productCategoryTenant){
					$secondCategory.append("<li><a href='${base}/mobile/tenant/productList.jhtml?tenantId="+$tenantId.val()+"&productCategoryTenantId="+productCategoryTenant.id +"'>"
							+productCategoryTenant.name+
							"</a></li>");
				});
				$secondCategory.append(tmpStr);
				$("#secondCategory").iScroll('refresh');
				return false;
		});
	 
	//加载更多
	 $more.on('tap',function(){
		 var totalPages = $totalPages.val();
		 var pageNumber = $pageNumber.val();
		 var pageSize = $pageSize.val();
		 var orderType = $orderType.val();
		 var tmpStr ="";
		 pageNumber++;
		 if(pageNumber>totalPages){
			 $load.html("亲，已经到底了");
			 return false;
		 }
		 $pageNumber.val(pageNumber);
		 $.ajax({
				url : "${base}/mobile/tenant/addMOreProduct.jhtml",
				data:{tenantId:$tenantId.val(),productCategoryTenantId:$productCategoryTenantId.val(),productCategoryId:$productCategoryId.val(),orderType:orderType,pageNumber:pageNumber,pageSize:pageSize},
				type:"get",
				dataType:"json",
				success:function(page){
			 			for(var i=0;i<page.content.length;i++){
				 			tmpStr+="<li><a href='javascript:;'><div class='m_productk' name='productDetail' productId='"+page.content[i].id+"'>";
				 			if(page.content[i].thumbnail!=null){
					 			tmpStr+="<img src='"+page.content[i].thumbnail+"' width='140px;' height='140px;'><p><span>";
				 			}else{
					 			tmpStr+="<img src='' class='NoPicture' width='140px;' height='140px;'><p><span>";
				 			}
							tmpStr+=currency(page.content[i].price,true)
									+"</span><del>"
									+currency(page.content[i].marketPrice,true)
									+"</del></p><p>"+page.content[i].fullName+"</p></div></a></li>";
				 		}
			 			tmpStr+="<div class='m_clear'></div>";
			 			$productList.append(tmpStr);
			 			$productList.iScroll('refresh');
			 			var $productDetail =$("div[name='productDetail']");
			 			$productDetail.on('tap',function(){
			 				$this = $(this);
			 				var productId = $this.attr('productId');
			 				location.href="${base}/mobile/product/content/"+productId+".jhtml";
			 			});
			}
	 });
	});
	//排序类型
		$allOrderType.on('tap',function(){
			$this = $(this);
			$("#orderType").val($this.attr('val'));
			$pageNumber.val(1);
			$productForm.submit();
			return false;
		});
		setTimeout(function(){$(".m_down").trigger("tap");},500);
	 
		$viewVideo.on('tap',function(){
			location.href="vsstoo://viewVideo?username=[#if tenant.member.username=='happywine']0592000198[#else]${tenant.member.username}[/#if]"; return false;
		});
		
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a  id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">经营商品</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<div class="m_shopp">
		<div class="m_shoptop">
			<a href="${base}/mobile/tenant/${tenant.id}/index.jhtml"><img src="${tenant.thumbnail}" width="60px;" height="60px;"></a>
			<a href="${base}/mobile/tenant/${tenant.id}/index.jhtml"><h1>${tenant.name}</h1></a>
			<span class="score${(tenant.score * 2)?string("0")}"></span>
			<p>
				[#list tenant.productCategoryTenants as productCategoryTenant]
			 		${productCategoryTenant.name}
			 		[#if productCategoryTenant_has_next]/[/#if]
			 	[/#list]
			</p>
			<div class="m_videobtn" id="viewVideo"></div> 		
			<!--		
			<div class="m_shopsc">
				<i class="iconfont p_turnout">&#xe605</i>
				<p>[#if tenant.distatce??]${tenant.distatce}m[/#if]</p>
			</div>
			--> 
		</div>
	</div>
	<div class="m_inquiry m_shopp_inquiry">
		<ul>
			<li>分类 <span></span></li>
			<li>排序 <span></span></li>
		</ul>
	</div>
	<div class="m_area m_shopp_area">
		<div class="m_areal" id="m_areascrooler_3">
			<ul>
				[#list productCategoryTenants as productCategoryTenant]
				 	[#if productCategoryTenant_index==0]
					 <li name="root" class="m_down" productCategoryTenantId="${productCategoryTenant.id}">
						${productCategoryTenant.name}
					 </li>
					[#else]
					 <li name="root" productCategoryTenantId="${productCategoryTenant.id}">
						${productCategoryTenant.name}
					 </li>
					[/#if]
				[/#list]
			</ul>
		</div>
		<div class="m_arear" id="m_areascrooler_4">
			<ul id="secondCategory">
				
			</ul>
		</div>
	</div>
	<div class="m_area arealpx m_shopp_area">
		<div class="m_arear" id="m_areascrooler_5">
			<ul>
				[#list orderTypes as ot]
				 	<li name="allOrderType" val="${ot}">
		    			<a href="javascript:void(0);">${message("Product.OrderType." + ot)}</a>
		    		</li>
		    	[/#list]
			</ul>
		</div>
	</div>
	<article class="m_article m_article_shopproduct" id="m_scrooler">
	<form id="productForm" action="${base}/mobile/tenant/productList.jhtml" method="get">
		<input type="hidden" id="orderType" name="orderType" value="${orderType}"/>
		<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
		<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
		<input type="hidden" id="totalPages" value="${page.totalPages}" />
		<input type="hidden" id="tenantId" name="tenantId" value="${tenant.id}" />
		<input type="hidden" id="productCategoryTenantId" name="productCategoryTenantId" value="${productCategoryTenantId}" />
		<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
		<input type="hidden" id="hasFavorite" value="${hasFavorite}"/>
		<div class="m_bodycont">
			<div class="m_listcont">
				<ul id="productList">
					[#if page.content?has_content]
					[#list page.content as product]
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
					[/#if]
					 <div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h5"></div>
			[#if page.content??&&page.content?has_content]
			<div class="m_more" id="more"><span id="load">点击加载</span></div>
			[#else]
			<div class="m_more" id="more"><span id="load">找不到相应的商品</span></div>
			[/#if] 
		</div>
	</form>
	</article>
	<div class="m_bodybg m_bodybg_shopp"></div>
</section>
</body>
</html>
