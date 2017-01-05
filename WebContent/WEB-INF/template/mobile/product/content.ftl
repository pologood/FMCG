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
	var $addFavorite = $("#addFavorite");
	var $productId = $("#productId");
	var $addCart = $("#addCart");
	var $goCart = $("#goCart");
	var $quantity = $("#quantity");
	var $reviewList = $("#reviewList");
	var $consultationsList = $("#consultationsList");
	var $introduction = $("#introduction");
	var $introduction = $(".introduction");
	var $addConsultation = $("#addConsultation");
	var $addopenshop = $("#addopenshop");
	var $addReview = $("#addReview");
	var $specificationValue=$("span[name='specificationValue']");
	var $specification=$("div[name='specificationNameAndValues']");
	var productMap = {};
	var $shard = $("#shard");
	
	//分享
	$shard.on("tap",function(){
		location.href="vsstoo://shareProduct?url=${productUrl}&productName=${product.fullName}&descriptionapp=亲，不错哦，快去看看&thumbnail=${product.thumbnail}"; return false;
	});
	//数量
	$quantity.on('keypress',function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
	});
	
	
	//订购选择减
    $("#sub").on('tap',function(){	
		var $this = $(this);
		var $num=$this.parent('.m_detailcountul').children("input");
		if(parseInt($num.val())>1){
			$num.val( parseInt($num.val())-1+"");
		}
	});
	//订购选择加
	$("#add").on('tap',function(){
		var $this = $(this);
		var $num=$this.parent('.m_detailcountul').children("input");
		if($num!=""){
			$num.val( parseInt($num.val())+1+"");
		}else{
			$num.val("1");
		}
	});
	
	//商品介绍和参数
	$introduction.on('tap',function(){
		var productId = $productId.val();
		location.href="${base}/mobile/product/introduction.jhtml?productId="+productId;
	});
	
	
	//评论列表
	$reviewList.on('tap',function(){
		var productId = $productId.val();
		location.href="${base}/mobile/product/reviewList.jhtml?productId="+productId;
		
	});
	
	//咨询列表
	$consultationsList.on('tap',function(){
		var productId = $productId.val();
		location.href="${base}/mobile/product/consultationsList.jhtml?productId="+productId;
		
	});
	
	//添加咨询
	$addConsultation.on('tap',function(){
		var productId = $productId.val();
		location.href="${base}/mobile/consultation/add/"+productId+".jhtml";
		return false;
	});
	
	//我要开店
	$addopenshop.on('tap',function(){
		location.href="${base}/mobile/openshop.html";
		return false;
	});
	
	//添加评论
	$addReview.on('tap',function(){
		var productId = $productId.val();
		location.href="${base}/mobile/member/review/add/"+productId+".jhtml";
		return false;
	});
	  
	//立即购买
	$goCart.on("tap",function(){
		var quantity = $quantity.val();
	 	
		 if(!(/^\+?[1-9][0-9]*$/.test(quantity))){
		 	mtips("购买数量只能为大于0的正整数");
		 	return false;
		 }
		 var productId = $productId.val();
		 var packagUnitId = $('.packagUnitId').val();
		 if(quantity==0){
		 		mtips("购买数量不能为0");
		 		return false;
		 }
		 $.ajax({
				url :'${base}/mobile/cart/add.jhtml',
				data:{id:productId,quantity:quantity,packagUnitId:packagUnitId},
				type:'post',
				dataType:'json',
				beforeSend:function(){
					$goCart.prop("disabled",true);
				},
				success:function(data){
					if(data.type=='success'){
						location.href="vsstoo://listCart";
						return false;
					}
					mtips(data.content);
					return false;
				},
				complete:function(){
					$goCart.prop("disabled",false);
				}
			});
	});
	  
		
	//加入购物车
	$addCart.on('tap',function(){
			 
		var quantity = $quantity.val();
		 	
		 if(!(/^\+?[1-9][0-9]*$/.test(quantity))){
		 	mtips("购买数量只能为大于0的正整数");
		 	return false;
		 }
		 var productId = $productId.val();
		 var packagUnitId = $('.packagUnitId').val();
		 if(quantity==0){
		 		mtips("购买数量不能为0");
		 		return false;
		 }
		 $.ajax({
				url :'${base}/mobile/cart/add.jhtml',
				data:{id:productId,quantity:quantity,packagUnitId:packagUnitId},
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.type=='success'){
						mtips("成功加入购物车！");
						location.href="vsstoo://addCart?quantity="+data.content;
					}else{
						mtips(data.content);
					}
					return false;
				}
			});
		 });
	  
	  //收藏商品
	  $addFavorite.on('tap',function(){
			var $productId = $("#productId");
			var $hasFavorite = $("#hasFavorite");
			if($hasFavorite.val()=="0"){
				//收藏过，点击则取消收藏
				$.ajax({
				url: "${base}/mobile/favorite/delete.jhtml?id="+$productId.val(),
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					$('.m_collect').addClass('m_down');
					if(message.type=='success'){
						$hasFavorite.val(1);
						$('.m_collect').removeClass('m_down');
					}
				}
			});
			}else{
				//未收藏，点击收藏
			$.ajax({
				url: "${base}/mobile/favorite/add.jhtml?id="+$productId.val(),
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					if(message.type=='success'){
						$hasFavorite.val(0);
						$('.m_collect').addClass('m_down');
					}else if(message.type=='error'){
						$('.m_collect').removeClass('m_down');
						mtips("请先登录,再收藏该商品!");
						setTimeout(function(){location.href="vsstoo://login/?redirectURL=mobile/product/content/"+$productId.val()+".jhtml";}, 1 * 1000);
					}else{
						mtips("最多只能收藏10个商品");
					}
				}
			});
		 }
			return false;
		});
	  
	  
	  [@compress single_line = true]
		productMap[${product.id}] = {
			id: null,
			specificationValues: [
				[#list product.specificationValues as specificationValue]
					"${specificationValue.id}"[#if specificationValue_has_next],[/#if]
				[/#list]
			]
		};
		[#list product.siblings as product]
			productMap[${product.id}] = {
				id: "${product.id}",
				specificationValues: [
					[#list product.specificationValues as specificationValue]
						"${specificationValue.id}"[#if specificationValue_has_next],[/#if]
					[/#list]
				]
			};
		[/#list]
	  [/@compress]
	   
	// 锁定规格值
	lockSpecificationValue();
		
	// 锁定规格值
	function lockSpecificationValue() {
		var selectedIds = new Array();
		$specificationValue.filter(".m_sfadd_hove").each(function(i) {
			selectedIds[i] = $(this).attr("val");
		});
		$specification.each(function() {
			var $this = $(this);
			var selectedId = $this.find("span.m_sfadd_hove").attr("val");
			var otherIds = $.grep(selectedIds, function(n, i) {
				return n != selectedId;
			});
			$this.find("span").each(function() {
				var $this = $(this);
				otherIds.push($this.attr("val"));
				var locked = true;
				$.each(productMap, function(i, product) {
					if (contains(product.specificationValues, otherIds)) {
						locked = false;
						return false;
					}
				});
				if (locked) {
					$this.addClass("locked");
				} else {
					$this.removeClass("locked");
				}
				otherIds.pop();
			});
		});
	}
	
	// 规格值选择
	$specificationValue.on('tap',function(e){
		var $this = $(this);
		if ($this.hasClass("locked")) {
			return false;
		}
		$this.toggleClass("m_sfadd_hove").parent().siblings('li').children().removeClass("m_sfadd_hove");
		var selectedIds = new Array();
		$specificationValue.filter(".m_sfadd_hove").each(function(i) {
			selectedIds[i] = $(this).attr("val");
		});
		var locked = true;
		$.each(productMap, function(i, product) {
			if (product.specificationValues.length == selectedIds.length && contains(product.specificationValues, selectedIds)) {
				if (product.id != null) {
					location.href = "${base}/mobile/product/content/"+product.id+".jhtml";
					locked = false;
				}
				return false;
			}
		});
		if (locked) {
			lockSpecificationValue();
		}
		return false;
	});
	
	// 判断是否包含
	function contains(array, values) {
		var contains = true;
		for(i in values) {
			if ($.inArray(values[i], array) < 0) {
				contains = false;
				break;
			}
		}
		return contains;
	}
	 
	
 	//单位选择
	$('.m_unit').on('tap',function(){
		var $this = $(this);
		var $count = $this.children("input.count");
		var $packagUnitId = $this.children("input.packagUnitId");
		var $allUnits = $this.find(".allUnits");
		var length = $allUnits.length;
		if(parseInt($count.val()) < length){
			var $temp =$($allUnits[$count.val()]);
			$("#x_wholePrice").text(currency($temp.attr("wholePrice"),true));
			$("#x_price").text(currency($temp.attr("price"),true));
			$this.find("span").text($temp.val());
			$packagUnitId.val($temp.attr("unitId"));
			$count.val(parseInt($count.val())+1);
		}else{
			$count.val("0");
			$packagUnitId.val("");
		}
	
	});
	
	[#if backUrl??]
	  $("#backto").on("tap",function(){
		  mtips("正在为您跳转，请稍等。。");
		  location.href="${backUrl}";
	  })	
	[/#if]
	
	 //联系卖家
	 $("#contectTenant").on("tap",function(){
	 	location.href="chat://open?sender="+encodeURI("${(member.username)!}")
	 	+"&sender-nickName="+encodeURI("${(member.name)!}")
	 	+"&sender-image="+encodeURI("${(member.headImg)!}")
	 	+"&recver="+encodeURI("${product.tenant.member.username}")
	 	+"&recver-nickName="+encodeURI("${(product.tenant.member.name)!}")
	 	+"&recver-image="+encodeURI("${(product.tenant.member.headImg)!}")
	 	+"&descr="+encodeURI("${(product.fullName)!}")
	 	+"&descr-image="+encodeURI("${(product.medium)!}");
	 	return false;
	 });
	
});
</script>
</head>
<body>
[@area_current]
 [#assign areaId=currentArea.id]
[/@area_current]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a [#if backUrl??]id="backto"[#else]id="return_btn"[/#if] href="javascript:void(0);" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_title" alt="选择日期">商品详情</div>
			<div id="contectTenant" class="m_detailcart" style="width: 70px;text-align: center;margin-top: 3px;">
			<i class="iconfont">&#xe604;</i>
			<a  href="javascript:;" style="font-size: 1px;line-height: 18px;">联系卖家</a></div>
		</div>
	</header>
	<article class="m_article" id="m_scrooler">
		<div class="m_bodycont_1">
			<div class="m_detail">
				<div class="m_roombanner">
					<ul id="m_roomslider">
						[#list product.productImages as productImage]
						 	<li><a href="javascript:void(0);" class="introduction"><img src="${productImage.large}"></a></li>
						[/#list]
					</ul>
					<div class="m_round">
						[#list product.productImages as productImage]
						 	[#if productImage_index==0]
						 	 	<span class="themeStyle"></span>
						 	[#else]
						 	 	<span></span>
						 	[/#if]
						 [/#list]
					</div>
					<div class="[#if hasFavorite==0]m_collect m_down[#else]m_collect[/#if]"  id="addFavorite">
						<i class="iconfont">&#xe605</i>
						<p>收藏</p>
					</div>
					<div class="m_share" id="shard">
						<i class="iconfont">&#xe60f</i>
						<p>分享</p>
					</div>
					<div class="m_clear"></div>
				</div>
				<div class="m_detailtitle">
					<a href="javascript:void(0);" id="introduction">
						<h1>${product.fullName}</h1>
						<p><span id="x_wholePrice">${currency(product.price,true)}</span> <del id="x_price">${currency(product.marketPrice,true)}</del> 赠送积分 <span>${product.point}  </span></p>
						<p>
						  ${product.calculateFreightDesc()}
						</p>
						<div class="p_tag"></div>
					</a>
				</div>
			</div>
			<div class="m_h10"></div>
			  [#if product.specifications??&&product.specifications?has_content]
					[#assign specificationValues = product.goods.specificationValues /]
					[#list product.specifications as specification]
					<div class="m_specification" name="specificationNameAndValues">
						<h1>${specification.name}:</h1>
						[#if specification.specificationValues??]
						<div class="m_scli">
							<ul>
							[#list specification.specificationValues as specificationValue]
							 [#if specificationValues?seq_contains(specificationValue)]
								<li><span name="specificationValue" class="[#if product.specificationValues?seq_contains(specificationValue)]m_sfadd_hove[/#if]" val="${specificationValue.id}">${specificationValue.name}<div class="p_vtag"></div></span></li>
							 [/#if]
							[/#list]
							</ul>
						</div>
						[/#if]
					</div>
					[/#list]
			   [/#if]	 
			<div class="m_detailcount">
				<div class="m_detailcountul">
					<span id="sub">-</span>
					<input type="text" value="1" id="quantity" maxlength="6">
					<span id="add">+</span>
				</div>
				<div class="m_unit">
					<span>${product.unit}</span>
					<input type="hidden" class="allUnits" wholePrice="${product.price}" price="${product.marketPrice}" unitId="" value="${product.unit}"/>
					[#list product.packagUnits as packagUnit]
						<input type="hidden" class="allUnits" wholePrice="${packagUnit.wholePrice}" price="${packagUnit.price}" unitId="${packagUnit.id}" value="${packagUnit.name}"/>
					[/#list]
					<input type="hidden" class="count" value="0"/>
					<input type="hidden" class="packagUnitId" />
				</div>
				<input type="hidden" id="productId" value="${product.id}"/>
				<input type="hidden" id="hasFavorite" value="${hasFavorite}"/>
			</div>
			<div class="m_h10"></div>
			<div class="m_specification" id="reviewList">
				<h1>评价</h1>
				<div class="m_specificationstar"><del>(${product.scoreCount})</del><span class="score${(product.score * 2)?string("0")}"></span></div>
				<div class="p_tag"></div>
			</div>
			<div class="m_specification" id="consultationsList">
				<h1>咨询</h1>
				<div class="m_specificationstar"><del>(${count})</del></div>
				<div class="p_tag"></div>
			</div>
			<!--
			<div class="m_evaluate">
				<ul>
					<li><a href="javascript:;"><i class="iconfont">&#xe606</i><span id="addConsultation">我要咨询>></span></a></li>
					<li><a href="javascript:;"><i class="iconfont">&#xe610</i><span id="addopenshop">我要开店>></span></a></li>
				</ul>
			</div>
			-->
			<div class="m_h10"></div>
			<div class="m_specification m_specification_p">
				<a href="[#if product.tenant.defaultDeliveryCenter??]${base}/mobile/delivery/${product.tenant.defaultDeliveryCenter.id}/index.jhtml[#else]javascript:;[/#if]">
					<h1>所属商家</h1>
					<div class=""><strong>${product.tenant.name}</strong><span class="score${(product.tenant.score * 2)?string("0")}"></span></div>
					<div class="p_tag"></div>
				</a>
			</div>
			<div class="m_h10"></div>
			<div class="m_specification m_llive">
				<h1>猜你喜欢</h1>
				<!--<div class="p_tag"></div>-->
			</div>
			<div class="m_listcont m_listcont_detail">
				<ul>
					[@product_list areaId=areaId productCategoryId=product.productCategory.id tagIds=5 count=6]
						[#list products as product]
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
					[/@product_list]
					<div class="m_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<footer class="m_footer">
		<div class="m_detailbtn">
			<ul>
				<li><a href="javascript:void(0)"><span id="goCart">立即购买</span></a></li>
				<li><a href="javascript:void(0)"><span id="addCart">加入购物车</span></a></li>
			</ul>
		</div>
	</footer>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
