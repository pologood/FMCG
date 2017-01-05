<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	[@seo type = "index"]
	<title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.index.title")}[/#if][#if systemShowPowered][/#if]</title>
	[#if seo.keywords??]
	<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
	<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
	[/@seo]
	<link rel="shortcut icon" href="${base}/favicon.ico" />
	<link rel="stylesheet" href="${base}/resources/b2b/css/v2.0/common.css"/>
	<link rel="stylesheet" href="${base}/resources/b2b/css/v2.0/shop_list.css">
	<link rel="stylesheet" href="${base}/resources/b2b/css/v2.0/list.css"/>
	<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/jquery.placeholder.js"></script>
	<script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/data/data.js"></script>
	<script type="text/javascript" src="${base}/resources/b2b/js/v2.0/search.js"></script>
</head>
<body>
	<script type="text/javascript">
	$(function(){
		var $headerRegister = $("#headerRegister");
		var $headerUsername = $("#headerUsername");
		var $headerLogout = $("#headerLogout");
		var username = getCookie("username");
		var areaId = getCookie("area_id");
		var $selectOrderType = $("a[name='orderType']");
		var $orderSelect = $("#orderSelect");
		var $sort = $(".price-number");
		var $size = $("#sort a.size");
		var $orderType = $("#orderType");
		
		var $change_city = $("#change_city");
		var $back_to_province = $("#back_to_province");
		var $chooseCity = $("#chooseCity");
		var $chooseProvince = $("#chooseProvince");
		var $setCity = $("#setCity");
		var $search_login = $("#search_login");
		
		var $memberPrice=$(".memberPrice");
		var $jt=$(".jt");
		var $jt1=$(".jt1");
		var $addCart = $("a[name='addCart']");
		var $toLogin = $("a[name='toLogin']");
		
		var $pageSize = $("#pageSize");
		var $pageNumber=$("#pageNumber");
		var $listForm=$("#listForm");
		var $series1=$("#series1 li");
		var $series2=$("#series2 li");
		var $series3=$("#series3 li");
		var $brandSeriesId=$("#brandSeriesId");
		var $productCategory = $("#productCategory li");
		var $productCategoryId = $("#productCategoryId");
		var $packagUnit=$(".packagUnit");
		var $normalQuantity = $(".w_pcttcount input[name='quantity']");
		var $table_quantity=$("#p_table tr input[name='productNum']");
		var $normalAddCart=$(".addCart");
		var $addCart = $("p[name='addCart']");
		var $payNow =  $("p[name='payNow']");
		var $add=$(".plus-sign");
		var $sub=$(".sub-sign");
		var $normalSub=$(".w_countl");
		var $normalAdd=$(".w_countr");
		var $packagUnit1=$(".packagUnit1");
		var $method=$("#method");
		var $normal=$(".w_modulecg");
		var $batch=$(".w_modulepl");
		var $addFavorite=$(".sc");
		
		$('input, textarea').placeholder();

		$("#previousPage").click(function(){
			$pageNumber.val(${page.pageNumber-1});
			$listForm.submit();
			return false;
		});
		$("#nextPage").click(function(){
			$pageNumber.val(${page.pageNumber+1});
			$listForm.submit();
			return false;
		});
		
		$series1.click(function(){
			var $this =$(this);
			$brandSeriesId.val($this.attr("seriesId"));
			$pageNumber.val(1);
			$listForm.submit();
			return false;
		});
		
		$("#series1Select").mouseover(function() {
			var $this = $(this);
			var $menuWrap = $this.closest("#series1Select");
			var $popupMenu = $menuWrap.children("#series1");
			$popupMenu.show();
			$menuWrap.mouseleave(function() {
				$popupMenu.hide();
			});
		});
		
		$("#series2Select").mouseover(function() {
			var $this = $(this);
			var $menuWrap = $this.closest("#series2Select");
			var $popupMenu = $menuWrap.children("#series2");
			$popupMenu.show();
			$menuWrap.mouseleave(function() {
				$popupMenu.hide();
			});
		});
		
		$("#series3Select").mouseover(function() {
			var $this = $(this);
			var $menuWrap = $this.closest("#series3Select");
			var $popupMenu = $menuWrap.children("#series3");
			$popupMenu.show();
			$menuWrap.mouseleave(function() {
				$popupMenu.hide();
			});
		});
		
		$series2.click(function(){
			var $this =$(this);
			$brandSeriesId.val($this.attr("seriesId"));
			$pageNumber.val(1);
			$listForm.submit();
			return false;
		});
		$series3.click(function(){
			var $this =$(this);
			$brandSeriesId.val($this.attr("seriesId"));
			$pageNumber.val(1);
			$listForm.submit();
			return false;
		});
		
		$normal.click(function(){
			var $this =$(this);
			$method.val('normal');
			$pageNumber.val(1);
			$listForm.submit();
			return false;
		});
		
		$batch.click(function(){
			var $this =$(this);
			$method.val('batch');
			$pageNumber.val(1);
			$listForm.submit();
			return false;
		});
		
		$productCategory.click(function(){
			var $this = $(this);
			$productCategoryId.val($this.attr("productCategoryId"));
			$pageNumber.val(1);
			$brandId.val("");
			$listForm.submit();
			return false;
		});
		
		$orderSelect.mouseover(function() {
			var $this = $(this);
			var $menuWrap = $this.closest("div.orderSelect");
			var $popupMenu = $menuWrap.children("div.popupMenu");
			$popupMenu.show();
			$menuWrap.mouseleave(function() {
				$popupMenu.hide();
			});
		});
		$sort.click(function() {
			var $this = $(this);
			if ($this.hasClass("current")) {
				$orderType.val("");
			} else {
				$orderType.val($this.attr("orderType"));
			}
			$pageNumber.val(1);
			$listForm.submit();
			return false;
		});
		$size.click(function() {
			var $this = $(this);
			$pageNumber.val(1);
			$pageSize.val($this.attr("pageSize"));
			$listForm.submit();
			return false;
		});	
	//切换单位
	$packagUnit.click(function(){
		var $this =$(this);
		$this.closest("li").find("input[data='packagUnitId']").val($this.attr("packagUnitId"));
		$this.closest("li").find(".price").text($this.attr("price"));
		$this.closest("li").find(".wholePrice").text($this.attr("wholePrice"));
		return false;
	});
	//切换单位
	$packagUnit1.click(function(){
		var $this =$(this);
		$this.closest("tr").find("span[data='choosedUnit']").text($this.text());
		$this.closest("tr").find("input[data='packagUnitId']").val($this.attr("packagUnitId"));
		$this.closest("tr").find(".price").text($this.attr("price"));
		$this.closest("tr").find(".wholePrice").text($this.attr("wholePrice"));
		$this.closest("tr").find("div[name='allUnits']").hide();
		return false;
	});	
	
	// 页码输入
	$pageNumber.keypress(function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key == 13 && $(this).val().length > 0) || (key >= 48 && key <= 57)) {
			return true;
		} else {
			return false;
		}
	});
	
	// 表单提交
	$listForm.submit(function() {
		if (!/^\d*[1-9]\d*$/.test($pageNumber.val())) {
			$pageNumber.val("1");
		}
	});

	$.pageSkip = function(pageNumber) {
		if($normal.hasClass('hoverbg')){
			$method.val("normal");
		}
		if($batch.hasClass('hoverbg')){
			$method.val("batch");
		}
		$pageNumber.val(pageNumber);
		$listForm.submit();
		return false;
	}
	
 //常规，数量
 $normalQuantity.on('keypress',function(event) {
 	var key = event.keyCode ? event.keyCode : event.which;
 	if ((key >= 48 && key <= 57) || key==8) {
 		return true;
 	} else {
 		return false;
 	}
 });
	 //批量，数量
	 $table_quantity.on('keypress',function(event) {
	 	var key = event.keyCode ? event.keyCode : event.which;
	 	if ((key >= 48 && key <= 57) || key==8) {
	 		return true;
	 	} else {
	 		return false;
	 	}
	 });
	 
	 
	//订购,常规
	$normalAddCart.click(function(){
		var $this =$(this);
		var quantity= $this.closest("li").find("input[data='quantity']").val();
		var packagUnitId= $this.closest("li").find("input[data='packagUnitId']").val();
		$.ajax({
			url:"${base}/b2b/cart/add.jhtml",
			type:"post",
			data:{id:$this.attr("productId"),quantity:quantity,packagUnitId:packagUnitId},
			dataType:"json",
			success:function(message){
				$.message(message);
				refreshCartCount();
					//$("#navTopZone_CartSum,#navTopZone_CartSum_header").text($(".messagesuccessIcon span.red").eq(0).text());
					return false;
				}
			});
	});
	   //订购,批量
	   $("a[name='addCart'],p[name='addCart']").click(function(){
	   	var temp=[];
	   	$("input[data='productNum']:text").each(function () {
	   		var $this=$(this);
	   		var $productId= $this.parent().parent().parent().parent().find("input[data='productId']");
	   		var $packagUnitId= $this.parent().parent().parent().parent().find("input[data='packagUnitId']");
	   		if($this.val()!="0"&&$this.val()!=""){
	   			temp.push($productId.val()+"-"+$this.val()+"-"+$packagUnitId.val());
	   		}
	   	});
	   	var idAndQuantity=temp.join(",");
	   	$.ajax({
	   		url:"${base}/b2b/cart/addALL.jhtml",
	   		type:"post",
	   		data:{idAndQuantity:idAndQuantity},
	   		dataType:"json",
	   		success:function(message){
	   			$.message(message);
	   			refreshCartCount();
				   //$("#navTopZone_CartSum,#navTopZone_CartSum_header").text($(".messagesuccessIcon span.red").eq(0).text());
				   return false;
				}
			});
	   });
	   //购物车加减
	   $add.click(function(){
	   	var $this =$(this);
	   	var $quantity=$this.closest("tr").find("input[data='productNum']");
	   	if($quantity.val()==""){
	   		$quantity.val(1);
	   		return false;	
	   	}
	   	if (parseInt($quantity.val()) >= 0)
	   	{
	   		$quantity.val(parseInt($quantity.val())+1);
	   	}
	   	if(isNaN(parseInt($quantity.val())))
	   	{
	   		$quantity.val(1);
	   	};		
	   	return false;
	   });
	   $sub.click(function(){
	   	var $this =$(this);
	   	var $quantity=$this.closest("tr").find("input[data='productNum']");
	   	if(parseInt($quantity.val())>0){
	   		$quantity.val(parseInt($quantity.val())-1);
	   	}
	   	if(isNaN(parseInt($quantity.val())))
	   	{
	   		$quantity.val(0);
	   	};
	   	return false;
	   });
	   $normalSub.click(function(){
	   	var $this =$(this);
	   	var $normalQuantity=$this.siblings('input');
	   	if(parseInt($normalQuantity.val())>1){
	   		$normalQuantity.val(parseInt($normalQuantity.val())-1);
	   	}
	   	return false;
	   });
	   $normalAdd.click(function(){
	   	var $this =$(this);
	   	var $normalQuantity=$this.siblings('input');
	   	$normalQuantity.val(parseInt($normalQuantity.val())+1);
	   	return false;
	   });
	   
	//搜索引擎，商品跟商家输入框切换
	$(".jq_get_ul h4").eq(0).addClass("select_btn02").siblings("h4").removeClass("select_btn02");
	$(".search_input_div").eq(0).show().siblings(".search_input_div").hide();
	
	//立即付款
	$payNow.click(function(){
		var temp=[];
		$("input[data='productNum']:text").each(function () {
			var $this=$(this);
			var $productId= $this.parent().parent().parent().parent().find("input[data='productId']");
			var $packagUnitId= $this.parent().parent().parent().parent().find("input[data='packagUnitId']");
			if($this.val()!="0"&&$this.val()!=""){
					//alert($productId.val());
					temp.push($productId.val()+"-"+$this.val()+"-"+$packagUnitId.val());
				}
			});
		
		
		var idAndQuantity=temp.join(",");
		$.ajax({
			url:"${base}/b2b/cart/addALL.jhtml",
			type:"post",
			data:{idAndQuantity:idAndQuantity},
			dataType:"json",
			success:function(message){
				$.message(message);
				refreshCartCount();
				   //$("#navTopZone_CartSum,#navTopZone_CartSum_header").text($(".messagesuccessIcon span.red").eq(0).text());
				   if (message.type!='error') { 
				   	location.href="${base}/b2b/cart/list.jhtml"; 
				   }
				   return false;
				}
			});
		
	});
	
	   // 添加商品收藏
	   $addFavorite.click(function() {
	   	var $this =$(this);
	   	var $productId=$this.closest("tr").find("input[data='productId']");
	   	var pid=$productId.val();
			//alert(pid);
			$.ajax({
				url: "${base}/b2b/member/favorite/add.jhtml",
				type: "POST",
				data:{"id":pid},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
				}
			});
			return false;
		});
	})	

</script>
<!--IE浏览器提示更新start-->
<div class="ie_browser_box">
	<div class="ie_browser"><b onclick="$('.ie_browser_box').remove()" title="关闭">×</b>您的浏览器版本过低！为了您更好的体验《店家助手》，请您<a title="点击升级IE浏览器" href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" target="_brank">升级IE浏览器</a>或者点击下载<a title="点击升级UC浏览器" href="http://www.uc.cn/" target="_brank">UC浏览器</a>或<a title="点击升级谷歌浏览器" href="http://www.google.cn/intl/zh-CN/chrome/browser/desktop/index.html" target="_brank">谷歌浏览器</a>！</div>
</div>
<!--IE浏览器提示更新end-->
[#include "/b2b/include/city.ftl" /]

[#include "/b2b/include/header.ftl" /]
<div class="s_content">
	[#if productCategory??]
	<div class="path">
		<ul>
			[@product_category_parent_list productCategoryId = productCategory.id]
			[#list productCategories as productCategory]
			<li>
				<a href="${base}/b2b${productCategory.path}">${productCategory.name}</a>
			</li>
			[/#list]
			[/@product_category_parent_list]
			<li>${productCategory.name}</li><li  class="last">共找到${page.total}个商品。</li>
		</ul>
	</div>
	[#else]
	[#if keyword??]
	<div class="path">
		<ul>
			<li  class="last">您正在查找"${(brand.name)!}${keyword}">共找到${page.total}个商品。</li>
		</ul>
	</div>
	[/#if]
	[/#if]
	<div class="w_productlist">
		<div class="w_module">
			<form id="listForm" action="" method="get">
				<div class="w_moduletop">
					<input type="hidden" id="productCategoryId" name="productCategoryId" value="${(productCategory.id)!}" />
					<input type="hidden" id="phonetic" name="phonetic" value="${phonetic}" />
					<input type="hidden" id="brandId" name="brandId" value="${(brand.id)!}" />
					<input type="hidden" id="areaId" name="areaId" value="${(areaId)!}" />
					<input type="hidden" id="brandSeriesId" name="brandSeriesId" value="${(brandSeries.id)!}" />
					<input type="hidden" id="method" name="method" value="${method}" />
					<input type="hidden" id="keyword" name="keyword" value="${keyword}" />
					<input type="hidden" id="orderType" name="orderType" value="${orderType}" />
					<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
					<div class="w_moduleli">
						<font>选择车型：</font>
					</div>
					<div class="w_moduleli w_moduleli_brand">
						<a href="javascript:tabLayer('contentid','block',this);" class="w_brandall_btn" id="brandName">[#if brand??]${(brand.name)!}[#else]全部品牌[/#if]</a>
						
						[#if brandSeries1??]
						<div class="position"  id="series1Select">
							<span class="w_brandall" id="series1Display">${series1}</span>
							<div class="w_brandsc" id="series1">
								[#if brandSeries1??]
								<ul>
									<li seriesId="">全系列</li>
									[#list brandSeries1 as series1]
									<li seriesId="${series1.id}">${series1.name}</li>
									[/#list]
								</ul>
								[/#if]
							</div>
						</div>
						[/#if]
						[#if brandSeries2??]
						<div class="position" id="series2Select">
							<span class="w_brandall" id="series2Name">${series2}</span>
							<div class="w_brandsc" id="series2">
								[#if brandSeries2??]
								<ul >
									<li seriesId="${brandSeries.parent.id}">全部</li>
									[#list brandSeries2 as series2]
									<li seriesId="${series2.id}">${series2.name}</li>
									[/#list]
								</ul>
								[/#if]
							</div>
						</div>
						[/#if]
						[#if brandSeries3??]
						<div class="position" id="series3Select">
							<span class="w_brandall"  id="series3Name">${series3}</span>
							<div class="w_brandsc" id="series3">
								[#if brandSeries3??]
								<ul>
									<li seriesId="${brandSeries.parent.id}">全部</li>
									[#list brandSeries3 as series3]
									<li seriesId="${series3.id}">${series3.name}</li>
									[/#list]
								</ul>
								[/#if]
							</div>
						</div>
						[/#if]
					</div>
				</div>
				<!--     bar -->					
				<div class="bar">
					<div id="sort" class="sort">
						
						
						<div class="tag01">
							<span class="page">
								<label>${message("shop.product.totalCount", page.total)} ${page.pageNumber}/[#if page.totalPages > 0]${page.totalPages}[#else]1[/#if]</label>
								[#if page.totalPages > 0]
								[#if page.pageNumber != 1]
								<a href="javascript:;">
									<span id="previousPage">${message("shop.product.previousPage")}</span>
								</a>
								[/#if]
								[#if page.pageNumber != page.totalPages]
								<a href="javascript:;">
									<span id="nextPage">${message("shop.product.nextPage")}</span>
								</a>
								[/#if]
								[/#if]
							</span>
							<div id="tab_city_div">
								<span cityid="0" class="w_brandall tab_default" id="areaName" onclick="deleteAddress('block')">[#if area??] ${area.name} [#else] 全国 [/#if]</span>
								
								<div class="tab_prov">
									<div class='tab_tit'>
										<font onclick="deleteAddress('none')" class="tab_delete"></font>
										<strong class="all_name" cityid="0"></strong>
									</div>
									<dl id="prov">
										
									</dl>
								</div>
							</div>
							<span class="show_batch">视图：</span>
							<a href="javascript:;" class="batch01 [#if method=='batch']w_modulepl hoverbg[#else]w_modulepl[/#if]">批量</a>
							<a href="javascript:;" class="batch01 [#if method=='normal']w_modulecg hoverbg[#else]w_modulecg[/#if]">常规</a>
							
							<span class="show_batch show_batch2">分页：</span>
							<a href="javascript:;" class="size[#if page.pageSize == 20] current[/#if]" pagesize="20">
								<span>20</span>
							</a>
							<a href="javascript:;" class="size[#if page.pageSize == 40] current[/#if]" pagesize="40">
								<span>40</span>
							</a>
							<a href="javascript:;" class="size[#if page.pageSize == 80] current[/#if]" pagesize="80">
								<span>80</span>
							</a>	
						</div>
						<div id="orderSelect" class="orderSelect">
							[#if orderType??]
							<span>${message("Product.OrderType." + orderType)}</span>
							[#else]
							<span>${message("Product.OrderType." + orderTypes[0])}</span>
							[/#if]
							<div class="popupMenu">
								<ul>
									[#list orderTypes as ot]
									<li>
										<a href="javascript:;"[#if ot == orderType] class="price-number ${ot} current" title="${message("shop.product.cancel")}"[#else] class="price-number ${ot}" [/#if] orderType="${ot}">${message("Product.OrderType." + ot)}</a>
									</li>
									[/#list]
								</ul>
							</div>
						</div>
						<a href="javascript:;" class='price-number [#if orderType == "priceAsc"]currentAsc current[#else]asc[/#if]' orderType="priceAsc">${message("shop.product.priceAsc")}</a>
						<a href="javascript:;" class='price-number [#if orderType == "salesDesc"]currentDesc current[#else]desc[/#if]' orderType="salesDesc">${message("shop.product.salesDesc")}</a>
						<a href="javascript:;" class='price-number [#if orderType == "scoreDesc"]currentDesc current[#else]desc[/#if]' orderType="scoreDesc">${message("shop.product.scoreDesc")}</a>
					</div>
				</div>
				<!--     end bar -->					
				
				<div class="h10"></div>
				<div class="w_modulelist">
					<ul style="[#if method=='normal']display: block;[#else]display: none;[/#if]">
						[#if page??&&page.content?has_content]
						[#list page.content as product]
						[#if product.tenant??]
						[#if product.tenant.status=="success"]
						<li>
							<input type="hidden" data="packagUnitId"/>
							<div class="w_modulepct">
								<div class="w_pctt">
									<a target="_blank" href="${base}/b2b/product/content/${product.id}.jhtml">
										<img title="${product.fullName}" src="${product.thumbnail}" height="216px">
										<p title="${product.fullName}">${product.fullName}</p>
									</a>
									<div class="w_pcttpr w_border">
										销售价：<strong class="wholePrice">${currency(product.calculatePrice(member,null),true)}</strong>
									</div>
								<!--<div class="w_pcttpr">
									市场价：<del class="price">${currency(product.calculateMarketPrice(null),true)}</del>
									<span>最小起订量为${product.minReserveMemo}</span>
								</div>-->
								<div class="w_pcttpr w_pcttprh">
									<div class="w_pcttdw">
										<div class="w_texts">${product.unit}</div>
										<div class="w_pcttdwul">
											[#list product.packagUnits as packagUnit]
											<span class="packagUnit" packagUnitId="${packagUnit.id}" wholePrice="${currency(product.calculatePrice(member,packagUnit),true)}" price="${currency(product.calculateMarketPrice(packagUnit),true)}">${packagUnit.name}</span>
											[/#list]
											<span class="packagUnit" packagUnitId="" wholePrice="${currency(product.calculatePrice(member,null),true)}" price="${currency(product.calculateMarketPrice(null),true)}">${product.unit}</span>
										</div>
									</div>
									<div class="w_pcttcount">
										<a href="javascript:;" class="w_countl"></a>
										<input class="w_texts" value="1" type="text" data="quantity">
										<div class="w_pcttdwul">
											<span>1</span>
											<span>10</span>
											<span>20</span>
										</div>
										<a href="javascript:;" class="w_countr"></a>
									</div>
									<div class="w_pcttbtn addCart" productId="${product.id}">加入购物车</div>
								</div>
							</div>
						</div>
					</li>
					[/#if]
					[/#if]
					[/#list]
					[/#if]
					<div class="clear"></div>
				</ul>
				<table class="product_table" id="p_table" style="[#if method=='batch']display: block;[#else]display: none;[/#if]">
					<tbody>
						[#if page??&&page.content?has_content]
						<tr>
							<th width="60" align="center">图片</th>
							<th width="420" align="center">商品名称</th>
							<!--<th width="100" align="center">市场价</th>-->
							<th width="100" align="center">销售价</th>
							<th width="100" align="center">数量</th>
							<th width="200" align="center">经销商</th>
							<th width="100" align="center">操作</th>
						</tr>
						[#list page.content as product]
						[#if product.tenant??]
						[#if product.tenant.status=="success"]
						<tr>
							<td width="60" align="center" class="small_pic">
								<input type="hidden" data="productId" value="${product.id}">
								<input type="hidden" data="packagUnitId"><img title="${product.fullName}" src="${product.thumbnail}" width="30px" height="30px;"><div class="big_pic"><img title="${product.fullName}" src="${product.thumbnail}" width="170px" height="170px;"></div> </td>
								<td width="428" class="w_widthy" align="left"><a  target="_blank" href="${base}/b2b/product/content/${product.id}.jhtml" title="${product.fullName}">${product.fullName}</a></td>
								<!--<td width="60" align="center" class="price">${currency(product.calculateMarketPrice(null),true)}</td>-->
								<td width="60" align="center" class="wholePrice">${currency(product.calculatePrice(member,null),true)}</td>
								<td width="100" align="center">
									<div class="jj-oq">
										<a class="sub-sign" href="javascript:;">-</a>
										<div class="sr-oq">
											<input type="text" value="0" data="productNum">
										</div>
										<a class="plus-sign" href="javascript:;">+</a>
									</div>
								</td>
								
								<td width="200" align="center"><a target="_blank" href="${base}/b2b/${product.tenant.id}/index.jhtml">${product.tenant.shortName}</a></td>
								<td width="60" align="center" ><a href="javascript:;" class="sc" >加入云仓</a></td>
							</tr>
							[/#if]
							[/#if]
							[/#list]
							<tr>
								<td colspan="11">
									<div class="add_to_car"><p name="addCart" id="addCart"><a href="javascript:;">加入购物车</a></p></div>
									<div class="add_to_car"><p name="payNow" id="payNow"><a href="javascript:;">立即付款</a></p></div>
								</td>
							</tr>
						</tbody>
						[/#if]
					</table>
					[#if !page.content?has_content]
					${message("shop.product.noListResult")}
					[/#if]
					[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "javascript: $.pageSkip({pageNumber});"]
					<div class="w_pagination">[#include "/b2b/include/pagination.ftl"]</div>
					[/@pagination]
					
				</div>
			</div>
		</form>	
	</div>
	[#include "/b2b/include/footer.ftl" /]
</div>

</body>
</html>