<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="baidu-site-verification" content="7EKp4TWRZT" />
[@seo type = "index"]
	<title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if] </title>
	[#if seo.keywords??]
		<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
		<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
[/@seo]
<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet" />
<link href="${base}/resources/store/css/detail.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${base}/resources/store/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.jqzoom.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $zoom = $("#zoom");
	var $scrollable = $("#scrollable");
	var $thumbnail = $("#scrollable a");
	var productMap = {};
	var $specification = $("#specification p");
	var $specificationValue = $("#specification a");
	var $quantity = $("#quantity");
	var $increase = $("#increase");
	var $decrease = $("#decrease");
	var $addCart = $("#addCart");
	var $addBuy = $("#addBuy");
	var $addFavorite = $("#addFavorite");
	var $bar = $("#bar ul");
	var $introduction = $("#introduction");
	var $parameter = $("#parameter");
	var $review = $("#review");
	var $addReview = $("#addReview");
	var $consultation = $("#consultation");
	var $addConsultation = $("#addConsultation");
	var $packagUnitPrice = $("#packagUnitPrice");
	var $packagUnitMarketPrice = $("#packagUnitMarketPrice");
	var $packagUnitId = $("#packagUnitId");
	var $packagUnit=$(".packagUnit");
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
	function lockSpecificationValue() {
		var selectedIds = new Array();
		$specificationValue.filter(".hover").each(function(i) {
			selectedIds[i] = $(this).attr("val");
		});
		$specification.each(function() {
			var $this = $(this);
			var selectedId = $this.find("a.hover").attr("val");
			var otherIds = $.grep(selectedIds, function(n, i) {
				return n != selectedId;
			});
			$this.find("a").each(function() {
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
	$specificationValue.click(function() {
		var $this = $(this);
		if ($this.hasClass("locked")) {
			return false;
		}
		$this.toggleClass("hover").siblings("a").removeClass("hover");
		var selectedIds = new Array();
		$specificationValue.filter(".hover").each(function(i) {
			selectedIds[i] = $(this).attr("val");
		});
		var locked = true;
		$.each(productMap, function(i, product) {
			if (product.specificationValues.length == selectedIds.length && contains(product.specificationValues, selectedIds)) {
				if (product.id != null) {
					location.href = "${base}/store/product/content/" + product.id+".jhtml";
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
	// 购买数量
	$quantity.keypress(function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
	});
	
	// 增加购买数量
	$increase.click(function() {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity)) {
			$quantity.val(parseInt(quantity) + 1);
		} else {
			$quantity.val(1);
		}
	});
	
	// 减少购买数量
	$decrease.click(function() {
		var quantity = $quantity.val();
		if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
			$quantity.val(parseInt(quantity) - 1);
		} else {
			$quantity.val(1);
		}
	});
	// 加入购物车
	$addCart.click(function() {
		[#if product.specifications?has_content]
			var specificationValueIds = new Array();
			$specificationValue.filter(".hover").each(function(i) {
				specificationValueIds[i] = $(this).attr("val");
			});
			if (specificationValueIds.length != ${product.specificationValues?size}) {
				$specificationTitle.show();
				return false;
			}
		[/#if]
		var quantity = $quantity.val();
		var packagUnitId = $packagUnitId.val();
		if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 0) {
			$.ajax({
				url: "${base}/store/cart/add.jhtml",
				type: "POST",
				data: {id: ${product.id}, quantity: quantity,packagUnitId:packagUnitId},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					refreshCartCount();
					return false;
				}
			});
		} else {
			$.message("warn", "${message("shop.product.quantityPositive")}");
		}
	});
	// 加入购物车
	$addBuy.click(function() {
		[#if product.specifications?has_content]
			var specificationValueIds = new Array();
			$specificationValue.filter(".hover").each(function(i) {
				specificationValueIds[i] = $(this).attr("val");
			});
			if (specificationValueIds.length != ${product.specificationValues?size}) {
				$specificationTitle.show();
				return false;
			}
		[/#if]
		var quantity = $quantity.val();
		var packagUnitId = $packagUnitId.val();
		if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 0) {
			$.ajax({
				url: "${base}/store/cart/add.jhtml",
				type: "POST",
				data: {id: ${product.id}, quantity: quantity,packagUnitId:packagUnitId},
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type == "success")
					{
						$.message(message);
						location.href = "${base}/store/cart/list.jhtml";
					}else{
						$.message(message);
					}
				}
			});
		} else {
			$.message("warn", "${message("shop.product.quantityPositive")}");
		}
	});
	// 添加商品收藏
	$addFavorite.click(function() {
		$.ajax({
			url: "${base}/store/member/favorite/add.jhtml?id=${product.id}",
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				$.message(message);
			}
		});
		return false;
	});
	$thumbnail.hover(function() {
		var $this = $(this);
		if ($this.hasClass("current")) {
			return false;
		} else {
			$thumbnail.removeClass("current");
			$this.addClass("current").click();
		}
	});
	// 判断是否包含
	function contains(array, values) {
		var contains = true;
		for(i in values) {
			if ($.inArray(values[i], array)<0) {
				contains = false;
				break;
			}
		}
		return contains;
	}
	// 锁定规格值
	lockSpecificationValue();
	
	// 商品图片放大镜
	$zoom.jqzoom({
		zoomWidth: 368,
		zoomHeight: 368,
		title: false,
		showPreload: false,
		preloadImages: false
	});
	
	// 商品缩略图滚动
	$scrollable.scrollable();
	// 浏览记录
	var historyProduct = getCookie("historyProduct");
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
	$.ajax({
		url: "${base}/store/product/history.jhtml",
		type: "GET",
		data: {ids: historyProductIds},
		dataType: "json",
		traditional: true,
		cache: false,
		success: function(data) {
		}
	});
	// 点击数
	$.ajax({
		url: "${base}/store/product/hits/${product.id}.jhtml",
		type: "GET"
	});
	[#if setting.isReviewEnabled && setting.reviewAuthority != "anyone"]
			// 发表商品评论
			$addReview.click(function() {
				if ($.checkLogin()) {
					return true;
				} else {
					$.redirectLogin("${base}/store/review/add/${product.id}.jhtml", "${message("shop.product.addReviewNotAllowed")}");
					return false;
				}
			});
	[/#if]
	
	[#if setting.isConsultationEnabled && setting.consultationAuthority != "anyone"]
		// 发表商品咨询
		$addConsultation.click(function() {
			if ($.checkLogin()) {
				return true;
			} else {
				$.redirectLogin("${base}/store/consultation/add/${product.id}.jhtml", "${message("shop.product.addConsultationNotAllowed")}");
				return false;
			}
		});
	[/#if]
	 
	//切换单位(推荐)
	   $packagUnit.click(function(){
		   var $this =$(this);
		   $this.parent().siblings("input[name='packagUnitId']").val($this.attr("packagUnitId"));
		   $packagUnitPrice.text($this.attr("wholePrice"));
		   $packagUnitMarketPrice.text($this.attr("price"));
		   return false;
	   });
});
</script>
</head>
<body class="loginbody">
[#include "/store/include/header.ftl" /]
<div class="s_content">
	<div class="s_detail">
		<!-- <div class="s_detail_top">
			<span></span>
			[@product_category_parent_list productCategoryId = productCategory.id]
				[#list productCategories as productCategory]
					<a href="${base}/store/product/list/${productCategory.id}.jhtml">${productCategory.name}</a>>
				[/#list]
			[/@product_category_parent_list]
			<a href="${base}/store/product/list/${productCategory.id}.jhtml" class="lastcolor">${productCategory.name}</a>
		</div> -->
		<div class="s_detail_header">
			<div class="s_detaill">
				<div class="productImage">
					[#if product.productImages?has_content]
						<a id="zoom" href="${product.productImages[0].large}" rel="gallery">
							<img class="medium" src="${product.productImages[0].medium}" />
						</a>
					[#else]
						<a id="zoom" href="${setting.defaultLargeProductImage}" rel="gallery">
							<img class="medium" src="${setting.defaultMediumProductImage}" />
						</a>
					[/#if]
					<a href="javascript:;" class="prev disabled"></a>
					<div id="scrollable" class="scrollable">
						<div class="items">
							[#if product.productImages?has_content]
								[#list product.productImages as productImage]
									<a[#if productImage_index == 0] class="current"[/#if] href="javascript:;" rel="{gallery: 'gallery', smallimage: '${productImage.medium}', largeimage: '${productImage.large}'}"><img src="${productImage.thumbnail}" title="${productImage.title}" /></a>
								[/#list]
							[#else]
								<a class="current" href="javascript:;"><img src="${setting.defaultThumbnailProductImage}" /></a>
							[/#if]
						</div>
					</div>
					<a href="javascript:;" class="next disabled"></a>
				</div>
				<div class="clear"></div>
			</div>
			<div class="s_detailr">
				<div class="title">${product.fullName}[#if product.isGift] [${message("shop.product.gifts")}][/#if]</div>
				<div class="price">
						<Span>价格：</Span>
						<h1 id="packagUnitPrice">${currency(product.calculatePrice(member,null),true)}</h1>
						<!--<del id="packagUnitMarketPrice">${currency(product.calculateMarketPrice(null),true)}</del>-->
				</div>
				<p>赠送积分:
					[#if product.point > 0]
						<span>${product.point}</span>
					[/#if]
				</p>
				[#if product.specifications?has_content]
					<div id="specification">
					[#assign specificationValues = product.goods.specificationValues /]
					[#list product.specifications as specification]
						<p>
							<span>${abbreviate(specification.name, 8)}：</span>
							[#list specification.specificationValues as specificationValue]
								[#if specificationValues?seq_contains(specificationValue)]
									<a href="javascript:;" class="[#if product.specificationValues?seq_contains(specificationValue)]hover[/#if]" val="${specificationValue.id}">
										<span title="${message("shop.product.selected")}" class="ttt">
											[#if specification.type == "text"]
											${specificationValue.name}
											[#else]
											<img src="${specificationValue.image}" alt="${specificationValue.name}" title="${specificationValue.name}" height="26px" width="26px"/>
											[/#if]
										</span>
									</a>
								[/#if]
							[/#list]
						</p>
					[/#list]
					</div>
				[/#if]
				<div class="quantity">数量：
					<dd>
						<input type="text" id="quantity" name="quantity" value="1" maxlength="4" onpaste="return false;">
						<div>
							<span id="increase" class="increase">&nbsp;</span>
							<span id="decrease" class="decrease">&nbsp;</span>
						</div>
					</dd>
				</div>
				<div class="quantity">单位：
					<div class="w_pcttdw w_pcttdw_1">
						<input type="text" name="packagUnitId" id="packagUnitId" style="display:none;"/>
						<div class="w_texts">${product.unit}</div>
						<div class="w_pcttdwul">
						[#list product.packagUnits as packagUnit]
							<span class="packagUnit" packagUnitId="${packagUnit.id}" wholePrice="${currency(product.calculatePrice(member,packagUnit),true)}" price="${currency(product.calculateMarketPrice(packagUnit),true)}">${packagUnit.name}</span>
						[/#list]
							<span class="packagUnit" packagUnitId="" wholePrice="${currency(product.calculatePrice(member,null),true)}" price="${currency(product.calculateMarketPrice(null),true)}">${product.unit}</span>
						</div>
					</div>
				</div>
				<div class="detailbtn">
					<ul>
						<li><a href="#" id="addCart">加入购物车</a></li>
						<li><a href="#" id="addBuy">立即购买</a></li>
						<li><a href="#" class="sc" id="addFavorite">加入云仓</a></li>
					</ul>
				</div>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="s_detail_cont">
			<div class="detailcl">
				<div class="hotProduct w_js">
					<div class="title">商铺信息</div>
					<div class="w_content" style="height:auto">
						<p class="w_shopimg"><img src="${(product.tenant.thumbnail)!}" width="100"></p>
						<p><a href="${base}/store/${product.tenant.id}/index.jhtml">${product.tenant.shortName}</a></p>
						[#if product.tenant.qq??]
						<p class="w_lineme"><a href="tencent://message/?uin=${product.tenant.qq}&Site=在线QQ&Menu=yes"> <img src="${base}/resources/store/images/v2.0/qq.gif">和我联系</a></p>
						[/#if]
						<p style="padding:5px 0;overflow:hidden"><!-- 商铺等级：${(product.tenant.gradeLevel)!} --><!-- <div class="score${(product.tenant.score * 2)?string("0")}"></div> -->
							<span class="w_crown w_crown_${product.tenant.creditLevel[0]}"></span>
							<span class="w_diamond w_diamond_${product.tenant.creditLevel[1]}"></span>
							<span class="w_star w_star_${product.tenant.creditLevel[2]}"></span>
						</p>
						<p class="w_ptext">所在地：[#if product.tenant.area.grade<2]${product.tenant.area.fullName}[#else]${product.tenant.area.parent.fullName}[/#if]</p>
						<p class="w_ptext"><span>${product.tenant.price}</span> </p>
						<div class="w_bder"></div>
						<div class="w_goshop w_detailgoshop"><a href="${base}/store/${product.tenant.id}/index.jhtml">进入商铺</a></div>
					</div>
				</div>
				<div class="h10"></div>
				<div class="hotproduct">
					<div class="top">热销商品</div>
					<div class="center">
						<ul>
							[@product_list tenantId=product.tenant.id count = 6 orderBy="monthSales desc"]
								[#list products as product]
									<li[#if !product_has_next] class="last"[/#if]>
										<a href="${base}/store/product/content/${product.id}.jhtml" title="${product.name}">
											<img src="${product.thumbnail}">
											<h1>${abbreviate(product.fullName, 30)}</h1>
											<p><strong>${currency(product.calculatePrice(member,null),true)}</strong><!--<del>${currency(product.calculateMarketPrice(null),true)}</del>--></p>
										</a>
									</li>
								[/#list]
							[/@product_list]	
						</ul>
					</div>
				</div>
				<div class="clear"></div>
			</div>
			<div class="detailcr">
				<div class="detailcr_c" id="bar">
					<ul id="tab_spxq_ul">
						[#if product.introduction?has_content]
							<li id="introduction">
								<span class="hover">商品描述</span>
							</li>
						[/#if]
						[#if product.parameterValue?has_content]
							<li id="parameter">
								<span>商品参数</span>
							</li>
						[/#if]
						[#if setting.isReviewEnabled]
							<li id="review">
								<span>商品评论</span>
							</li>
						[/#if]
						[#if setting.isConsultationEnabled]
							<li id="consultation">
								<span >商品咨询</span>
							</li>
						[/#if]
					</ul>
					<script>
					$(function(){
						$("#tab_spxq_ul li").click(function(){
							$(this).children("span").addClass("hover").parent("li").siblings("li").children("span").removeClass("hover");
							$(".detailcont").eq($(this).index()).show().siblings(".detailcont").hide();
						});
					});
					</script>
				</div>
				<div class="detailcr_ch">
					[#if product.introduction?has_content]
						<div name="introduction" style=" display:block;" class="detailcont s_dt1">
						 	<h1><span>商品详情</span></h1>
							<div>
								${product.introduction}
							</div>
						</div>
					[/#if]
					[#if product.parameterValue?has_content]
						<div name="parameter" class="detailcont s_dt2">
							 <h1><span>商品参数</span></h1>
							<table>
								<tbody>
								[#list productCategory.parameterGroups as parameterGroups]
									<tr>
										<th class="group" colspan="2">${parameterGroups.name}</th>
									</tr>
									[#list parameterGroups.parameters as parameter]
										[#if product.parameterValue.get(parameter)??]
											<tr>
												<th>${parameter.name}</th>
												<td>${product.parameterValue.get(parameter)}</td>
											</tr>
										[/#if]
									[/#list]
								[/#list]
								</tbody>
							</table>
						</div>
					[/#if]
					[#if setting.isReviewEnabled]
						<div name="review" class="detailcont s_dt3">
							 <h1><span>商品评价</span></h1>
							<div class="p_evaluate">
								[#if product.scoreCount > 0]
								<div class="detail_pj">
									<div class="score">
										<strong>${product.score?string("0.0")}</strong>
										<div>
											<div class="score${(product.score * 2)?string("0")}"></div>
											<div>${message("Product.scoreCount")}: ${product.scoreCount}</div>
										</div>
									</div>
									<div class="graph">
										<span style="width: ${(product.score * 20)?string("0.0")}%">
											<em>${product.score?string("0.0")}</em>
										</span>
										<div>&nbsp;</div>
										<ul>
											<li>${message("shop.product.graph1")}</li>
											<li>${message("shop.product.graph2")}</li>
											<li>${message("shop.product.graph3")}</li>
											<li>${message("shop.product.graph4")}</li>
											<li>${message("shop.product.graph5")}</li>
										</ul>
									</div>
									<!-- <div class="handle">
										<a href="${base}/store/review/add/${product.id}.jhtml" id="addReview">${message("shop.product.addReview")}</a>
									</div> -->
									</div>
									[@review_list productId = product.id count = 5]
										[#if reviews?has_content]
											<div class="p_evaluate">
												<ul>
												   [#list reviews as review]
													<li>
														<p>${review.content}</p>
														<div class="p_evaluate_text">
															<span>[#if review.member??]
																	[#if review.member.tenant??]
																		[#if review.isAnonym??]
																			[#if review.isAnonym=="true"]
																				匿名
																			[#else]
																				${mosaic(review.member.tenant.name,4,"***")!"匿名"}
																			[/#if]
																		[#else]
																			${mosaic(review.member.tenant.name,4,"***")!"匿名"}
																		[/#if]
																	[#else]
																		[#if review.isAnonym??]
																			[#if review.isAnonym=="true"]
																				匿名
																			[#else]
																				${mosaic(review.member.username,3,"******")!"匿名"}
																			[/#if]
																		[#else]
																			${mosaic(review.member.username,3,"******")!"匿名"}
																		[/#if]
																	[/#if]
																	[#else]
																		${message("shop.product.anonymous")}
																	[/#if]</span>
															<span><div class="score${(review.score * 2)?string("0")}"></div></span>
															<span>[#list product.specification_value as specification]${specification}[/#list]</span>
															<span title="${review.createDate?string("yyyy-MM-dd HH:mm:ss")}">${review.createDate?string("yyyy-MM-dd")}</span>
														</div>
													</li>
													[/#list]
													<div class="p_clear"></div>
												</ul>
											</div>
											<p>
												<a href="${base}/store/review/content/${product.id}.jhtml">[${message("shop.product.viewReview")}]</a>
											</p>
										[/#if]
									[/@review_list]
								[#else]
									<p>
										${message("shop.product.noReview")} <a href="${base}/store/review/add/${product.id}.jhtml" id="addReview">[${message("shop.product.addReview")}]</a>
									</p>
								[/#if]
							</div>
						</div>
					[/#if]
					[#if setting.isConsultationEnabled]
						<div name="consultation" class="detailcont s_dt4">
							<h1><span>商品咨询</span></h1>
							<div class="p_consulting">
								[@consultation_list productId = product.id count = product.consultations.length]
									[#if consultations?has_content]
										<ul>
											[#list consultations as consultation]
											[#if consultation.forConsultation??]
											[#else]
												<li[#if !consultation_has_next] class="last"[/#if]>
													<div class="p_evaluate_text">
														<span>
															[#if consultation.member??]
																${mosaic(consultation.member.username,3,"******")}:
															[#else]
																${message("shop.consultation.anonymous")}:
															[/#if]
														</span>
														<span>${consultation.content}</span>
														<span title="${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${consultation.createDate?string("yyyy-MM-dd")}</span>
													</div>
													[#if consultation.replyConsultations?has_content]
														[#list consultation.replyConsultations as replyConsultation]
														<div class="p_evaluate_text_1">
															<span>
																<!--[#if replyConsultation.member??]
																	${replyConsultation.member.username}:
																[#else]
																	${message("shop.consultation.anonymous")}:
																	
																[/#if]-->
																客服：
															</span>
															<span>${replyConsultation.content}</span>
															<span title="${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${replyConsultation.createDate?string("yyyy-MM-dd")}</span>
														</div>
														[/#list]
													[/#if]
												</li>
											[/#if]
											[/#list]
										</ul>
										<p>
											<a href="${base}/store/consultation/add/${product.id}.jhtml" id="addConsultation" class="p_consultingbtn">[${message("shop.product.addConsultation")}]</a>
											<a href="${base}/store/consultation/content/${product.id}.jhtml">[${message("shop.product.viewConsultation")}]</a>
										</p>
									[#else]
										<p>
											${message("shop.product.noConsultation")} <a href="${base}/store/consultation/add/${product.id}.jhtml" id="addConsultation">[${message("shop.product.addConsultation")}]</a>
										</p>
									[/#if]
								[/@consultation_list]
							</div>
						</div>
					[/#if]
				  </div>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>
[#include "/store/include/footer.ftl" /]
</body>
</html>