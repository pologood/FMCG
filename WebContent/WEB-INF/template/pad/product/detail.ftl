<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />
<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/detail.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $goCart=$("#goCart");
	var $return=$("#return");
	var $memberCenter=$("#memberCenter");
	var $consultation=$("#consultation");
	var $content=$("#content");
	var $addCart = $("#addCart");
	var $quantity = $("#quantity");
	var $productId = $("#productId");
	var $cartItemCount=$("#cartItemCount");
	var productMap = {};
	var $specificationValue=$("span[name='specificationValue']");
	var $specification=$("div[name='specificationNameAndValues']");
	var $addFavorite = $("#addFavorite");
	var $search = $("#search");
	var historyProduct = getCookie("historyProduct");
	//数量
	$quantity.on('keypress',function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
	});
	
	//添加商品咨询
	$consultation.on('tap',function(){
		var content = $content.val().replace(/\s+/g,"");  
		if(content == ""){
			ptips("请输入咨询内容");
		}else{
			$.ajax({
				url :'${base}/pad/consultation/save.jhtml',
				data:{id:$productId.val(),content:content},
				type:'post',
				dataType:'json',
				success:function(message){
					if(message.type=='success'){
						ptips("咨询成功,请等待审核回复");
						$('.p_rechargead').removeClass('rightin').addClass('rightout');
						$('.p_receiptbg').addClass('ddin').removeClass('ddout');
						$.later(function(){
							$('.p_receiptbg').hide();
						}, 800, false);
					}
					return false;
				}
			});
		}
	});
	
	//返回列表
	$return.on("tap",function(){
		var $this = $(this);
		location.href="${base}/pad/product/list.jhtml?productCategoryId="+$this.attr("productCategoryId");return false;
	});
	
	
	//进入购物车
	$goCart.on("tap",function(){
		location.href="${base}/pad/cart/list.jhtml";
		return false;
	});
	//进入会员中心
	$memberCenter.on("touchstart",function(){
		location.href="${base}/pad/member/center.jhtml";
		return false;
	});
	//订购选择减
    $("#sub").on('tap',function(){	
		var $this = $(this);
		var $num=$this.parent('.p_detailcountul').children("input");
		if(parseInt($num.val())>0){
		$num.val( parseInt($num.val())-1+"");
		}
	});
	//订购选择加
	  $("#add").on('tap',function(){
		var $this = $(this);
		var $num=$this.parent('.p_detailcountul').children("input");
		$num.val( parseInt($num.val())+1+"");
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
		$specificationValue.filter(".p_sfadd_hove").each(function(i) {
			selectedIds[i] = $(this).attr("val");
		});
		$specification.each(function() {
			var $this = $(this);
			var selectedId = $this.find("span.p_sfadd_hove").attr("val");
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
		$this.toggleClass("p_sfadd_hove").siblings("span").removeClass("p_sfadd_hove");
		var selectedIds = new Array();
		$specificationValue.filter(".p_sfadd_hove").each(function(i) {
			selectedIds[i] = $(this).attr("val");
		});
		var locked = true;
		$.each(productMap, function(i, product) {
			if (product.specificationValues.length == selectedIds.length && contains(product.specificationValues, selectedIds)) {
				if (product.id != null) {
					location.href = "${base}/pad/product/detail/"+product.id+".jhtml";
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
	
	//加入购物车
	 $addCart.on('tap',function(){
		 
	 	var quantity = $quantity.val();
	 	
	 	if(!(/^\+?[1-9][0-9]*$/.test(quantity))){
	 		ptips("购买数量只能为大于0的正整数");
	 		return false;
	 	}
	 	var productId = $productId.val();
	 	var packagUnitId = $('.packagUnitId').val();
	 	if(quantity==0){
	 		ptips("购买数量不能为0");
	 		return false;
	 	}
	 	$.ajax({
			url :'${base}/ajax/cart/add.jhtml',
			data:{id:productId,quantity:quantity,packagUnitId:packagUnitId},
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.type=='success'){
					getCartCount();
				}
				ptips(data.content);
				return false;
			}
		});
	 });
	 
	 //获取购物车数量方法
	function getCartCount(){
		$.ajax({
			url : "${base}/pad/cart/get_cart_count.jhtml",
			type:"post",
			dataType:"json",
			success:function(data){
				if(data.count>0){
					$cartItemCount.text(data.count);
				}
			}
		});
	};
	
	
	 $addFavorite.on('tap',function(){
		var $productId = $("#productId");
		var $hasFavorite = $("#hasFavorite");
		if($hasFavorite.val()=="0"){
			//收藏过，点击则取消收藏
			$.ajax({
			url: "${base}/pad/favorite/delete.jhtml?id="+$productId.val(),
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				if(message.type=='success'){
					$hasFavorite.val(1);
					$('.p_share').removeClass('p_red');
				}
			}
		});
		}else{
			//未收藏，点击收藏
		$.ajax({
			url: "${base}/pad/favorite/add.jhtml?id="+$productId.val(),
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				if(message.type=='success'){
					$hasFavorite.val(0);
					$('.p_share').addClass('p_red');
				}else if(message.type=='error'){
					ptips("最多只能收藏10个商品");
				}else{
					ptips("已经收藏过该商品");
				}
			}
		});
	 }
		return false;
	});
	 
	 // 浏览记录
	
	var historyProductIds = historyProduct != null ? historyProduct.split(",") : new Array();
	for (var i = 0; i < historyProductIds.length; i ++) {
		if (historyProductIds[i] == "${product.id}") {
			historyProductIds.splice(i, 1);
			break;
		}
	}
	historyProductIds.unshift("${product.id}");
	if (historyProductIds.length > 6) {
		historyProductIds.pop();
	}
	addCookie("historyProduct", historyProductIds.join(","), {path: "${base}/"});
	 
	 	//单位选择
	$('.p_unit').on('tap',function(){
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
		if($(this).hasClass('p_dw')){
			$(this).removeClass('p_dw');
		}else{
			$(this).addClass('p_dw');
		}
	});
	 
	 
	 $search.on('tap',function(){
		 if($("#keyword").val()==''){
				ptips("请输入搜索关键词");
				return false;
			}
	 	location.href = "${base}/pad/product/search.jhtml?keyword="+$("#keyword").val();
	 });
	 getCartCount();
});
</script>
</head>
<body>
<section class="p_section">
	[#include "/pad/include/navigation.ftl" /]
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void();" class="p_return" id="return" productCategoryId="${product.productCategory.id}">
				<div class="p_tag"></div>
			</a>
			<div class="p_detailfun">
				<ul>
					<li class="p_detailfun0">商品详情</li>
					<li class="p_detailfun1">商品描述</li>
					<li class="p_detailfun2">商品参数</li>
					<li class="p_detailfun3">商品评价</li>
					<li class="p_detailfun4">商品咨询</li>
					<li class="p_detailfun5"><a href="#">商铺实景
						<span>hot</span></a>
					</li>
				</ul>
			</div>
			<div class="p_search">
				<div class="p_search_icon"></div>
				<input type="text" placeholder="请输入搜索内容"/>
			</div>
		</div>
	</div>
	<article class="p_article p_articledetail p_darticle0" id="p_contscrooler">
		<div class="bodycont">
			<div class="p_detaill">
				<div class="p_detaillup">
					<h1>${product.fullName}</h1>
					<div class="p_detailp p_wb">商品编号：${product.sn}<div class="p_clear"></div></div>
					<div class="p_detailp"><strong id="x_wholePrice">${currency(product.wholePrice, true)}</strong><del id="x_price">${currency(product.price, true)}</del>[#if discount < 10]<span id="discount"> ${discount?string("0.0")}折</span><div class="p_clear"></div>[/#if]</div>
					<div class="p_detailp p_wb">赠送积分：${product.point}<div class="p_clear"></div></div>
					[#if product.specifications??&&product.specifications?has_content]
						[#assign specificationValues = product.goods.specificationValues /]
						[#list product.specifications as specification]
						<div class="p_detailp p_sfadd" name="specificationNameAndValues"><span>${specification.name}:</span>
							[#if specification.specificationValues??]
								[#list specification.specificationValues as specificationValue]
									[#if specificationValues?seq_contains(specificationValue)]
										<span name="specificationValue" class="[#if product.specificationValues?seq_contains(specificationValue)]p_sfadd_hove[/#if]" val="${specificationValue.id}">
											<a href="javascript:void(0)">${specificationValue.name}</a>
											<div class="p_vtag"></div>
										</span>
									[/#if]
								[/#list]
							[/#if]
							<div class="p_clear"></div>
						</div>
						[/#list]
					[/#if]
					
					<div class="p_detailcount">
						<div class="p_detailcountul">
							<span id="sub">-</span>
							<input type="text" value="1" id="quantity" maxlength="6"/>
							<span id="add">+</span>
						</div>
						<div class="p_unit"><span>${product.unit}</span>
								<input type="hidden" class="allUnits" wholePrice="${product.wholePrice}" price="${product.price}" unitId="" value="${product.unit}"/>
							[#list product.packagUnits as packagUnit]
								<input type="hidden" class="allUnits" wholePrice="${packagUnit.wholePrice}" price="${packagUnit.price}" unitId="${packagUnit.id}" value="${packagUnit.name}"/>
							[/#list]
							<input type="hidden" class="count" value="0"/>
							<input type="hidden" class="packagUnitId" />
						</div>
					</div>
					<input type="hidden" id="productId" value="${product.id}"/>
					<input type="hidden" id="hasFavorite" value="${hasFavorite}"/>
					<div class="p_detailp"><div class="p_detailcart" id="addCart">加入购物车</div></div>
				</div>
				<div class="p_detailldown">
					<div class="p_shopttop">
						商铺信息
					</div>
					<div class="p_shoptcont">
						<img src="${product.tenant.logo}">
						<div class="p_title">
							<h1>${product.tenant.name}</h1>
							<p>地区：${product.tenant.area.parent.fullName}</p>
							<p class="score${(product.tenant.score * 2)?string("0")}"></p>
						</div>
						<p class="p_titlep">
							[#list product.tenant.tags as tag]
                              [#if tag.icon?has_content ] 
									<span class="p_doun1"><img src="${tag.icon}"/></span>
                              [#else] 
                                	<span class="p_doun1">${tag.name}</span>
                              [/#if] 
                       		[/#list]
						</p>
						<p class="p_titlep">最低配送金额：<strong>${product.tenant.price}</strong></p>
						<div class="p_detailcart p_fl"><a href="#">进入商铺</a></div>
						<div class="p_clear"></div>
					</div>
				</div>
			</div>
			<div class="p_detailr">
				<div class="bodycont_detail">
					<div class="[#if hasFavorite==0]p_share p_red[#else]p_share[/#if]"><i class="iconfont" id="addFavorite">&#xe60a</i></div>
					<!-- <div class="p_collect"><i class="iconfont">&#xe6b7</i></div> -->
					<div class="p_indexbanner">
						<ul id="am-slider">
							[#list product.productImages as productImage]
								<li><img src="${productImage.medium}"></li>
							[/#list]
						</ul>
						<div class="p_round">
							[#list product.productImages as productImage]
								[#if productImage_index==0]
								 	<span class="themeStyle"></span>
								[#else]
								 	<span></span>
								[/#if]
							[/#list]
						</div>
						<div class="p_clear"></div>`
					</div>
				</div>
			</div>
		</div>
	</article>
	<article class="p_article p_articledetail_1 p_darticle1" id="p_contscrooler_1" title="商品描述">
		<div class="bodycont_detail">
			${product.introduction}
		</div>
	</article>
	<article class="p_article p_articledetail_1 p_darticle2" id="p_contscrooler_2" title="商品参数">
		<div class="bodycont_detail">
			<div class="p_parameter">
				<h1 class="p_title">${product.productCategory.name}</h1>
				<div class="p_h10"></div>
				<table>
					[#if product.parameterValue?has_content]
						[#list product.parameterValue.keySet() as parameter]
							<tr>
								<th class="p_thr">${abbreviate(parameter.name,20)}</th>
								<td>${abbreviate(product.parameterValue.get(parameter),30,"...")}</td>
							</tr>
						[/#list]
					[/#if]
				</table>
			</div>
		</div>
	</article>
	<article class="p_article p_articledetail_1 p_darticle3" id="p_contscrooler_3" title="商品评价">
		<div class="bodycont_detail">
	
		<div class="p_evaluate">
				<ul>
					[#list product.reviews as review]
						<li>
							<p>${review.content}</p>
							<div class="p_evaluate_text">
								<span>${review.member.username}</span>
								<span class="score${(review.score * 2)?string("0")}"></span>
								<span>
								[#if product.specification_value??&&product.specification_value?has_content]
									[#list product.specification_value as specification_value]
										${specification_value}
									[/#list]
								[/#if]
								</span>
								<span>${review.createDate?string("yyyy-MM-dd HH:mm:SS")}</span>
							</div>
						</li>
					[/#list]
					<div class="p_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<article class="p_article p_articledetail_1 p_darticle4" id="p_contscrooler_4" title="商品咨询">
		<div class="bodycont_detail">
		<div class="p_consulting">
				<ul>
					[#if product.consultations??&&product.consultations?has_content]
						[#list product.consultations as consultation]
						 [#if consultation.isShow]
							[#if consultation.member??]
								<li>
									<div class="p_evaluate_text">
										<span>${(consultation.member.name)!}: </span>
										<span>${consultation.content}</span>
										<span>${consultation.createDate?string("yyyy-MM-dd HH:mm:SS")}</span>
									</div>
									[#if consultation.replyConsultations??&&consultation.replyConsultations?has_content]
										[#list consultation.replyConsultations as replyConsultation]
											<div class="p_evaluate_text_1">
												<span>${(replyConsultation.member.name)!} : </span>
												<span>${replyConsultation.content}</span>
												<span>${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:SS")}</span>
											</div>
										[/#list]
									[/#if]
								</li>
							[/#if]
						  [/#if]
						[/#list]
					[/#if]
					<div class="p_clear"></div>
				</ul>
			</div>
			<div class="p_consultingbtn">我要咨询</div>
		</div>
	</article>
	<article class="p_article p_articledetail_1 p_darticle5" id="p_contscrooler_5" title="商铺时景">
		<div class="bodycont_detail">
商铺时景
		</div>
	</article>
	<footer class="p_footer">
		<div class="p_classification"><i class="iconfont">&#xf000e</i></div>
		<div class="p_navigation">
			<ul>
				<li><a href="#" alt="历史浏览"><i class="iconfont">&#xe60d</i></a></li>
				<li><a href="javascript:;" alt="购物车" id="goCart"><i class="iconfont">&#xe6d1</i></a>
					<span class="p_cartcount" id="cartItemCount">[#if count??&&count>0]${count}[#else]0[/#if]</span>
				</li>
				<li><a href="javascript:;" alt="会员中心" id="memberCenter"><i class="iconfont">&#x3432</i></a></li>
				<li class="p_home"><a href="${base}/pad" alt="首页"><i class="iconfont">&#xf0019</i></a></li>
			</ul>
		</div>
	</footer>
</section>
<div class="p_rechargead" id="addConsultation">
<div class="p_receiptcont">
	<div class="p_receipttop">
		商品咨询
		<span class="p_returndd1">取消</span>
		<span class="p_returncz" id="consultation">确定</span>
	</div>
	<div class="p_zxdder" id="p_zxscroller">
		<ul class="bodycont_list">
			<li>
				<a href="#">
					<img src="${product.thumbnail}">
					<p>${product.fullName}</p>
					<p><span>${currency(product.wholePrice, true)}</span><span>${currency(product.price, true)}</span></p>
				</a>
			</li>
			<li>
				<textarea type="text" id="content" placeholder="输入咨询内容"></textarea>
			</li>
		</ul>
	</div>
</div>
</div>
<div class="p_searchfixed">
	<div class="p_search_icon"></div>
	<input type="text" placeholder="请输入搜索内容" id="keyword"/>
	<div class="p_search_button" id="search">搜索</div>
</div>
<div class="p_windowbg_1"></div>
</body>
</html>
