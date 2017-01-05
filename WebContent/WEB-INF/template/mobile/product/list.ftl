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
			 var childrenOne="${rootProductCategory.children}";
			 var tempArray=[];
			 [#if rootProductCategory.children?has_content]
				 [#list rootProductCategory.children as childrenOne]
						 var temp= {id: "${childrenOne.id}",name:"${childrenOne.name}"};
			 			  tempArray.push(temp);
				 [/#list]
				productCategoryMap["${rootProductCategory.id}"]=tempArray;
		     [/#if]
	 [/#list]
	[/@compress]
	//根分类li点击展开
	 var $root = $("li[name='root']");
	 var $secondCategory = $("#secondCategory");
	 $root.on('tap',function(e){
				var $this = $(this);
				var productCategoryId=$this.attr("productCategoryId");
				var productCategoryName=$this.attr("productCategoryName");
				var tagIds = "";
				var $tagIds = $("input[name='tagIds']");
				$tagIds.each(function(){
					tagIds+="&tagIds="+$(this).val();
				});
				$secondCategory.empty();
				$.each(productCategoryMap[productCategoryId],function(i,productCategory){
					$secondCategory.append("<li><a href='${base}/mobile/product/list/"
							+productCategory.id
							+".jhtml'>"
							+productCategory.name+
							"</a></li>");
				});
				$("#secondCategory").iScroll('refresh');
			
			return false;
		});
	 
	 var $near = $("#near");
	 var $hot = $("#hot");
	 var $secondMenu = $("#secondMenu");
	 var $more = $("#more");
	 var $pageNumber = $("#pageNumber");
	 var $totalPages = $("#totalPages");
	 var $productForm = $("#productForm");
	 var $productCategoryId = $("#productCategoryId");
	 var $orderType = $("#orderType");
	 var $pageSize = $("#pageSize");
	 var $productList = $("#productList");
	 var $load = $("#load");
	 var $areaId = $("#areaId");
	 var $allOrderType =$("li[name='allOrderType']");
	 var $nearArea =$("li[name='nearArea']");
	 var $communityId = $("#communityId");
	 var $search = $("#search");
	 var $keyword = $("#keyword");
	 var $distatce = $("#distatce");
	 var $MyCare = $("#MyCare");
	 var $memberId = $("#memberId");
	 
	 $search.on('tap',function(){
		 var keyword = $keyword.val();
		 if(keyword==''){
			 mtips("请输入搜索关键字!");
			 return false;
		 }
		 location.href="${base}/mobile/product/search.jhtml?areaId=1029&keyword="+keyword;
		 
	 });
	 
		//排序类型
	$allOrderType.on('tap',function(){
		$this = $(this);
		$("#orderType").val($this.attr('val'));
		$areaId.val('');
		$pageNumber.val(1);
		$productForm.submit();
		return false;
	});
	 //地区选择
	$nearArea.on('tap',function(){
		 $this = $(this);
		 var areaId = $this.attr('areaId');
		 var tmpStr ="";
		 $secondMenu.empty();
		 $.ajax({
				url : "${base}/mobile/product/getCommunity.jhtml",
				data:{areaId:areaId},
				type:"get",
				dataType:"json",
				success:function(list){
			 		if(list!=''){
			 			$secondMenu.append("<li name='all' areaId='"+areaId+"'><a href='javascript:void(0)'>全部</a></li>");
			 			for(var i=0;i<list.length;i++){
				 			tmpStr+="<li communityId='"+list[i].id+"' name='nearCommunity'><a href='javascript:void(0)'>"+list[i].name+"</a></li>";
				 		}
				 		$secondMenu.append(tmpStr);
				 		var $nearCommunity =$("li[name='nearCommunity']");
				 		$nearCommunity.on('tap',function(){
				 			$this = $(this);
				 			var communityId = $this.attr('communityId');
				 			$communityId.val(communityId);
				 			$pageNumber.val(1);
				 			$productForm.submit();
				 		});
				 		var $all =$("li[name='all']");
				 		$all.on('tap',function(){
				 			$this = $(this);
				 			var areaId = $this.attr('areaId');
				 			$communityId.val('');
				 			$areaId.val(areaId);
				 			$pageNumber.val(1);
				 			$productForm.submit();
				 		});
			 		}
				}
			});
	 });
	 $near.on('tap',function(){
		 $secondMenu.empty();
		 $secondMenu.append("<li name='distatce' distance='500'><a href='javascript:void(0);'>500m</a></li><li name='distatce'distance='1000'><a href='javascript:void(0);'>1000m</a></li><li name='distatce' distance='2000'><a href='javascript:void(0);'>2000m</a></li><li name='distatce' distance='5000'><a href='javascript:void(0);'>5000m</a></li>");
		 var $allDistatce = $("li[name='distatce']");
		 $allDistatce.on('tap',function(){
			 var $this = $(this);
			 var distance = $this.attr('distance');
			 $distatce.val(distance);
			 $pageNumber.val(1);
			 $productForm.submit();
		 });
	 });
	 $hot.on('tap',function(){
		 var tmpStr ="";
		 $secondMenu.empty();
		 $.ajax({
				url : "${base}/mobile/product/getCommunity.jhtml",
				type:"get",
				dataType:"json",
				success:function(list){
			 		$secondMenu.append("<li name='allCommunity' areaId=''><a href='javascript:void(0)'>全部</a></li>");
			 		if(list!=null){
			 			for(var i=0;i<list.length;i++){
				 			tmpStr+="<li communityId='"+list[i].id+"' name='hotCommunity'><a href='javascript:void(0)'>"+list[i].name+"</a></li>";
				 		}
				 		$secondMenu.append(tmpStr);
				 		var $hotCommunity =$("li[name='hotCommunity']");
				 		$hotCommunity.on('tap',function(){
				 			$this = $(this);
				 			var communityId = $this.attr('communityId');
				 			$communityId.val(communityId);
				 			$areaId.val('');
				 			$pageNumber.val(1);
				 			$productForm.submit();
				 		});
				 		var $allCommunity =$("li[name='allCommunity']");
				 		$allCommunity.on('tap',function(){
				 			$communityId.val('');
				 			$areaId.val('');
				 			$pageNumber.val(1);
				 			$productForm.submit();
				 		});
				 		
			 		}
				}
			});
	 });
	 $MyCare.on('tap',function(){
		 var memberId = $memberId.val();
		 if(memberId==""){
			 mtips("请先登录才能搜索你所收藏的商品!");
			 if($productCategoryId.val()==""){
				 location.href = "vsstoo://login/?redirectURL=mobile/product/list.jhtml";
			 }else{
				 location.href = "vsstoo://login/?redirectURL=mobile/product/list/"+$productCategoryId.val()+".jhtml";
			 }
		 }else{
			 $productForm.submit();
		 }
	 });
	 
	 
	  setTimeout(function(){$(".m_down").trigger("tap");},500);
});

// 2015-1-11 拖动加载

var myScroll,pullUpEl, pullUpOffset,generatedCount = 0;
var pageNum = ${page.pageable.pageNumber};
function pullUpAction () {
	setTimeout(function () {	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
		if(pageNum>=(${page.total/page.pageable.pageSize})){
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
			return false;
		}
		var productCategoryId=$("#productCategoryId").val();
		if(productCategoryId==""){
			 url = "${base}/mobile/product/addMore.jhtml";
		 }else{
			 url = "${base}/mobile/product/addMore/"+productCategoryId+".jhtml";
		 }
		var el, li, i;
		el = document.getElementById('productList');
		pageNum=pageNum+1;
		$.ajax({
			url:url,
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum},
			success:function(result){
				if(result!=null&&result.length>0){
					for(var i=0;i<result.length;i++){
						li = document.createElement('li');
						li.innerHTML ="<a href=${base}/mobile/product/content/"+result[i].id+".jhtml><div class='m_productk'><img src="+result[i].medium+"><p><span>"
						+currency(result[i].price,true)
						+"</span><del>"
						+currency(result[i].marketPrice,true)
						+"</del></p><p>"
						+result[i].fullName
						+"</p></div></a>";
						el.appendChild(li, el.childNodes[0]);
					}
					$('#m_scrooler_0').iScroll('refresh');// 请记住，当刷新内容加载（即：在阿贾克斯完成
				}else{
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
					return false;
				}
			}
		});
	}, 1000);	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
}

function loaded() {
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	
	myScroll = new iScroll('m_scrooler_0', {
		onRefresh: function () {
			if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '拉起加载更多...';
			}
		},
		onScrollMove: function () {
			if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
				pullUpEl.className = 'flip';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '正在刷新...';
				this.maxScrollY = this.maxScrollY;
			} else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '正在刷新...';
				this.maxScrollY = pullUpOffset;
			}
		},
		onScrollEnd: function () {
			if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
				pullUpAction();	// Execute custom function (ajax call?)
			}
		}
	});
	
}

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
</script>
<style>
.m_search{display:block;}
</style>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:void(0);" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_title" alt="选择日期">商品列表</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<div class="m_inquiry">
		<ul>
			<li>附近 <span></span></li>
			<li>分类 <span></span></li>
			<li>[#if orderType??]${message("Product.OrderType." + orderType)}[#else]默认排序[/#if]<span></span></li>
		</ul>
	</div>
	<div class="m_area">
		<div class="m_areal" id="m_areascrooler_1">
			<ul>
				<li id="near" class="m_down">附近</li>
				<li id="MyCare">收藏的商品</li>
				<li id="hot">热门商圈</li>
				[#list areas as area]
				 	<li areaId="${area.id}" name="nearArea">${area.name}</li>
				[/#list]
			</ul>
		</div>
		<div class="m_arear" id="m_areascrooler_2">
			<ul id="secondMenu">
			</ul>
		</div>
	</div>
	<div class="m_area">
		<div class="m_areal" id="m_areascrooler_3">
			<ul>
				[@product_category_root_list count = 10]
				[#list productCategories as rootProductCategory]
				[#if rootProductCategory_index==0]
				 	<li name="root" class="m_down" productCategoryName="${rootProductCategory.name}" productCategoryId="${rootProductCategory.id}">
						${rootProductCategory.name}
				 	</li>
				[#else]
				 	<li name="root" productCategoryName="${rootProductCategory.name}" productCategoryId="${rootProductCategory.id}">
						${rootProductCategory.name}
				 	</li>
				[/#if]
				[/#list]
				[/@product_category_root_list]
			</ul>
		</div>
		<div class="m_arear" id="m_areascrooler_4">
			<ul id="secondCategory">
				
			</ul>
		</div>
	</div>
	<div class="m_area arealpx">
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
	<article class="m_article m_article_list" id="m_scrooler_0">
		<div class="m_bodycont_3">
		<form id="productForm" action="${base}/mobile/product/list[#if productCategory??]/${productCategory.id}[/#if].jhtml" method="get">
			<input type="hidden" id="productCategoryId"  value="[#if productCategory??]${productCategory.id}[/#if]">
			<input type="hidden" id="brandId" name="brandId" value="${(brand.id)!}">
			<input type="hidden" id="orderType" name="orderType" value="${orderType}"/>
			<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
			<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
			<input type="hidden" id="totalPages" value="${page.totalPages}" />
			<input type="hidden" id="areaId" name="areaId" value="[#if area??]${area.id}[/#if]" />
			<input type="hidden" id="communityId" name="communityId" value="${communityId}" />
			<input type="hidden" id="distatce" name="distatce" value="${distatce}" />
			<input type="hidden" id="memberId" name="memberId" value="[#if member??]${member.id}[/#if]" />
			[#list tagIds as tagId]
		 		<input type="hidden"  name="tagIds" value="${tagId}" />
			[/#list]
		 	
			<div class="m_listcont">
				<ul id="productList">
					[#list page.content as product]
					<li>
						<a href="${base}/mobile/product/content/${product.id}.jhtml">
							<div class="m_productk">
								<img src="${product.medium}" >
								<p><span>${currency(product.price,true)}</span><del>${currency(product.marketPrice,true)}</del></p>
								<p>${product.fullName}</p>
							</div>
						</a>
					</li>
					[/#list]
				</ul>
				<div id="pullUp">
					<span class="pullUpLabel">拉起加载更多...</span>
				</div>
			</div>
			</form>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
