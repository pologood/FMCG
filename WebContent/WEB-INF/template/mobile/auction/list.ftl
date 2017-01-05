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
	var $limitBuyInfo = $("div[name='last_time']");
	var $goDetail = $("button[name='goDetail']");
	var $more = $("#more");
	var $productList = $("#productList");
 	var $totalPages = $("#totalPages");
 	var $pageNumber = $("#pageNumber");
 	var $orderType = $("#orderType");
 	var $pageSize = $("#pageSize");
 	var $communityId = $("#communityId");
 	var $load = $("#load");
 	var $backIndex = $("#backIndex");
 	
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
				$secondCategory.empty();
				$.each(productCategoryMap[productCategoryId],function(i,productCategory){
					$secondCategory.append("<li><a href='javascript:;' id='"+productCategory.id+"' name='productCategoryForm'>"
							+productCategory.name+
							"</a></li>");
				});
				$("#secondCategory").iScroll('refresh');
				var $productCategoryForm = $("a[name='productCategoryForm']");
				$productCategoryForm.on('tap',function(){
					var $this=$(this);
					var productCategoryId=$this.attr('id');
					$productCategoryId.val(productCategoryId);
					$pageNumber.val(1);
		 			$productForm.submit();
				});
				return false;
		});
	 	
	 
	 	var $near = $("#near");
	 	var $nearArea =$("li[name='nearArea']");
	 	var $secondMenu = $("#secondMenu");
	 	var $allOrderType =$("li[name='allOrderType']");
	 	var $hot = $("#hot");
	 	var $limitBuyInfo = $("div[name='last_time']");
	 	var $goDetail = $("button[name='goDetail']");
	 	var $load = $("#load");
	 	var $more = $("#more");
	 	var $productList = $("#productList");
	 	var $totalPages = $("#totalPages");
	 	var $pageNumber = $("#pageNumber");
	 	var $orderType = $("#orderType");
	 	var $pageSize = $("#pageSize");
	 	var $communityId = $("#communityId");
	 	var $productForm = $("#productForm");
	 	var $areaId = $("#areaId");
	 	var $distatce = $("#distatce");
	 	var $productCategoryId = $("#productCategoryId");
	 	
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
		
		//排序类型
		$allOrderType.on('tap',function(){
			$this = $(this);
			$("#orderType").val($this.attr('val'));
			$areaId.val('');
			$pageNumber.val(1);
			$productForm.submit();
			return false;
		});
	 	
	//促销
	function promotionInfo() {
		$limitBuyInfo.each(function() {
			var $this = $(this);
			var beginDate = $this.attr("beginTimeStamp") != null ? new Date(parseFloat($this.attr("beginTimeStamp"))) : null;
			var endDate = $this.attr("endTimeStamp") != null ? new Date(parseFloat($this.attr("endTimeStamp"))) : null;
			if (beginDate == null || beginDate <= new Date()) {
				if (endDate != null && endDate >= new Date()) {
					var time = (endDate - new Date()) / 1000;
					$this.html("剩余:<span>" + Math.floor(time / (24 * 3600)) + "<\/span> 天 <span>" + Math.floor((time % (24 * 3600)) / 3600) + "<\/span> 时 <span>" + Math.floor((time % 3600) / 60) + "<\/span> 分");
				} else if (endDate != null && endDate < new Date()) {
					$this.html("${message("shop.index.ended")}");
				} else {
					$this.html("${message("shop.index.going")}");
				}
			}
		});
	}
	promotionInfo();
	setInterval(promotionInfo, 60 * 1000);
	
	//立即参与
	$goDetail.on("tap",function(){
		var $this=$(this);
		var promotionId=$this.attr('promotionId');
		var productId=$this.attr('productId');
		if($.checkLogin()){
			location.href = "${base}/mobile/auction/content/"+promotionId+"/"+productId+".jhtml";
			return false;
		}else{
			mtips("参与拍卖请先登录!");
			setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/auction/content/"+promotionId+"/"+productId+".jhtml";}, 1 * 1000);
			return false;
		}
	});
	
	//加载更多
	 $more.on('tap',function(){
		 var totalPages = $totalPages.val();
		 var pageNumber = $pageNumber.val();
		 var orderType = $orderType.val();
		 var pageSize = $pageSize.val();
		 var communityId = $communityId.val();
		 var tmpStr ="";
		 pageNumber++;
		 $pageNumber.val(pageNumber);
		 if(pageNumber>totalPages){
			 $load.html("亲，已经到底了");
			 return false;
		 }
		 $pageNumber.val(pageNumber);
		 $.ajax({
				url : "${base}/mobile/auction/addMore.jhtml",
				data:{communityId:communityId,orderType:orderType,pageNumber:pageNumber,pageSize:pageSize},
				type:"get",
				dataType:"json",
				success:function(page){
			 		if(page!=''){
			 			for(var i=0;i<page.content.length;i++){
			 				tmpStr+="<li>" +
				 			"<a href='javascript:;' name='detail' promotionId='"+page.content[i].id+"' productId='"+page.content[i].defaultProduct.id+"'>" +
					 			"<div class='m_productk'>";
					 			if(page.content[i].defaultProduct.thumbnail!=null){
						 			tmpStr+="<img src='"+page.content[i].defaultProduct.thumbnail+"' height='120px;'>";
					 			}else{
						 			tmpStr+="<img src='' class='NoPicture' height='120px;'>";
					 			}
						 			tmpStr+="<p><strong>"+page.content[i].defaultProduct.fullName+"</strong></p>" +
						 			"<p><span>"+currency(page.content[i].promotionPrice,true)+"</span></p>" +
						 			"<p><span class='score"+page.content[i].defaultProduct.score*2+"'></span></p>" +
					 			"</div>" +
				 			"</a>" +
				 			"<div class='m_tuanfunc'>" +
				 				"<div class='m_tuanfuncul'>" +
				 				"<div class='m_tuanfuncli' name='last_time' beginTimeStamp='"+page.content[i].beginDate+"' endTimeStamp='"+page.content[i].endDate+"'></div>" +
				 					"<div class='m_tuanfuncli'>" +
				 						"<button name='goDetail' promotionId='"+page.content[i].id+"' productId='"+page.content[i].defaultProduct.id+"'>立即参与</button>" +
				 					"</div>" +
				 				"</div>" +
				 			"</div>" +
				 		"</li>";
			 			}
			 			tmpStr+="<div class='m_clear'></div>";
			 			$productList.append(tmpStr);
			 			$productList.iScroll('refresh');
			 			$limitBuyInfo = $("div[name='last_time']");
			 			promotionInfo();
			 			setInterval(promotionInfo, 60 * 1000);
			 			var $goDetail =$("button[name='goDetail']");
			 			$goDetail.on('tap',function(){
			 				var $this=$(this);
			 				var promotionId=$this.attr('promotionId');
			 				var productId=$this.attr('productId');
			 				if($.checkLogin()){
			 					location.href = "${base}/mobile/auction/content/"+promotionId+"/"+productId+".jhtml";
			 					return false;
			 				}else{
			 					mtips("参与拍卖请先登录!");
			 					setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/auction/content/"+promotionId+"/"+productId+".jhtml";}, 1 * 1000);
			 					return false;
			 				}
			 			});
			 		}
				}
			});
	 });
	 
	 	var $detail =$("a[name='detail']");
		$detail.live('tap',function(){
			var $this=$(this);
			var promotionId=$this.attr('promotionId');
			var productId=$this.attr('productId');
			if($.checkLogin()){
				location.href = "${base}/mobile/auction/content/"+promotionId+"/"+productId+".jhtml";
				return false;
			}else{
				mtips("参与拍卖请先登录!");
				setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/auction/content/"+promotionId+"/"+productId+".jhtml";}, 1 * 1000);
				return false;
			}
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
		
		setTimeout(function(){$(".m_down").trigger("tap");},500);
		
		$backIndex.on("tap",function(){
			location.href="${base}/mobile/index.jhtml?backIndex=true"; return false;
		});
	
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="backIndex" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">拍卖列表</div>
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
	<article class="m_article m_article_list" id="m_scrooler">
		<div class="m_bodycont">
			<form id="productForm" action="${base}/mobile/auction/list.jhtml" method="get">
				<input type="hidden" id="orderType" name="orderType" value="${orderType}"/>
				<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
				<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
				<input type="hidden" id="totalPages" value="${page.totalPages}" />
				<input type="hidden" id="areaId" name="areaId" value="[#if area??]${area.id}[/#if]" />
				<input type="hidden" id="communityId" name="communityId" value="${communityId}" />
				<input type="hidden" id="distatce" name="distance" value="${distatce}" />
				<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
			</form>
			<div class="m_listcont m_shoplist m_tuanlist">
				<ul id="productList">
					[#if page.content?has_content]
						[#list page.content as promotion]
						<li>
							<a href="${base}/mobile/auction/content/${promotion.id}/${promotion.defaultProduct.id}.jhtml">
								<div class="m_productk">
									<img src="${promotion.defaultProduct.thumbnail}" height="120px;">
									<p><strong>${promotion.defaultProduct.fullName}</strong></p>
									<p><span>${currency(promotion.promotionPrice,true)}</span></p>
									<p><span class="score${(promotion.defaultProduct.score * 2)?string("0")}"></span></p>
								</div>
							</a>
							<div class="m_tuanfunc">
								<div class="m_tuanfuncul">
									<div class="m_tuanfuncli" name="last_time" [#if promotion.beginDate??] beginTimeStamp="${promotion.beginDate?long}"[/#if][#if promotion.endDate??] endTimeStamp="${promotion.endDate?long}"[/#if]></div>
									<div class="m_tuanfuncli">
										<button name="goDetail" promotionId="${promotion.id}" productId="${promotion.defaultProduct.id}">立即参与</button>
									</div>
								</div>
							</div>
						</li>
						[/#list]
					[/#if]
					<div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h5"></div>
			[#if page.content?has_content]
				<div class="m_more" id="more"><span id="load">点击加载</span></div>
			[#else]
				<div class="m_more" ><span >暂无拍卖活动</span></div>
			[/#if]
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
