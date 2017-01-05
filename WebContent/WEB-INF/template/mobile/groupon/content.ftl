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
$().ready(function() {
	var $quantity = $("#quantity");
	var $m_time = $("#m_time");
	var $addFavorite = $("#addFavorite");
	var $reviewList = $("#reviewList");
	var $consultationsList = $("#consultationsList");
	var $addReview = $("#addReview");
	var $addConsultation = $("#addConsultation");
	var $productId = $("#productId");
	var $submit = $("#submit");
	var $grouponId = $("#grouponId");
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
	
	//数量
	$quantity.on('keypress',function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
	});
	
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
//		if($(this).hasClass('p_dw')){
//			$(this).removeClass('p_dw');
//		}else{
//			$(this).addClass('p_dw');
//		}
	});
	
	//促销
	function promotionInfo() {
		$m_time.each(function() {
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
	
	//收藏商品
	  $addFavorite.on('tap',function(){
		  $('.m_collect').removeClass('m_down');
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
				success: function(message){
					if(message.type=='success'){
						$hasFavorite.val(0);
						$('.m_collect').addClass('p_turnin').addClass('m_down');
					}else if(message.type=='error'){
						$('.m_collect').removeClass('m_down');
						mtips("请先登录,再收藏该商品!");
						setTimeout(function(){location.href="vsstoo://login/?redirectURL=mobile/product/content/"+$productId.val()+".jhtml";return false;},1000);
					}else{
						mtips("最多只能收藏10个商品");
					}
				  }
				});
		 }
			return false;
		});
	  
	  	//评论列表
		$reviewList.on('tap',function(){
			var productId = $productId.val();
			location.href="${base}/mobile/groupon/reviewList.jhtml?productId="+productId;
			
		});
		
		//咨询列表
		$consultationsList.on('tap',function(){
			var productId = $productId.val();
			location.href="${base}/mobile/groupon/consultationsList.jhtml?productId="+productId;
			
		});
		
		//添加咨询
		$addConsultation.on('tap',function(){
			var productId = $productId.val();
			location.href="${base}/mobile/consultation/add/"+productId+".jhtml";
		});
		
		//添加评论
		$addReview.on('tap',function(){
			if($.checkLogin()){
				var productId = $productId.val();
				location.href="${base}/mobile/member/review/add/"+productId+".jhtml";
			}else{
				mtips("参与评论,请先登录!");
				setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/groupon/content/"+grouponId+"/"+productId+".jhtml";},1000);
			}
		});
		
		//立即参与
		$submit.on('tap',function(){
			var $this = $(this);
			var grouponId = $grouponId.val();
			var quantity = $quantity.val();
			var productId = $productId.val();
			if(!$.checkLogin()){
				mtips("参与团购请先登录!");
				setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/groupon/content/"+grouponId+"/"+productId+".jhtml";},1000);
				return false;
			}
			$.ajax({
				url :"${base}/mobile/groupon/check.jhtml",
				type:"post",
				dataType:"json",
				data:{id:${promotion.id},quantity:quantity},
				beforeSend:function(){
					$submit.prop("disabled",true);
				},
				success:function(message){
					if(message.type=='success'){
						location.href="${base}/mobile/groupon/detail/${promotion.id}.jhtml?quantity="+quantity; return false;		
					}else{
						mtips(message.content);
						setTimeout(function(){location.reload();},1500);
					}
				},
				complete:function(){
					$submit.prop("disabled",false);
				}
			});
		});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">团购详情</div>
		</div>
	</header>
	<div class="m_area">
		<div class="m_areal" id="m_areascrooler_1">
			<ul>
				<li class="m_down">附近</li>
				<li>热门商圈</li>
				<li>思明区</li>
				<li>湖里区</li>
				<li>集美区</li>
			</ul>
		</div>
		<div class="m_arear" id="m_areascrooler_2">
			<ul>
				<li><a href="#">500m</a></li>
				<li><a href="#">1000m</a></li>
				<li><a href="#">2000m</a></li>
				<li><a href="#">5000m</a></li>
			</ul>
		</div>
	</div>
	<div class="m_area">
		<div class="m_areal" id="m_areascrooler_3">
			<ul>
				<li class="m_down">美容美发</li>
				<li>美容美发</li>
				<li>美容美发</li>
				<li>美容美发</li>
				<li>美容美发</li>
			</ul>
		</div>
		<div class="m_arear" id="m_areascrooler_4">
			<ul>
				<li><a href="#">美容美发</a></li>
				<li><a href="#">美容美发</a></li>
				<li><a href="#">美容美发</a></li>
				<li><a href="#">美容美发</a></li>
			</ul>
		</div>
	</div>
	<div class="m_area arealpx">
		<div class="m_arear" id="m_areascrooler_5">
			<ul>
				<li><a href="#">默认排序</a></li>
				<li><a href="#">置顶降序</a></li>
				<li><a href="#">最惠排序</a></li>
				<li><a href="#">价格升序</a></li>
				<li><a href="#">价格降序</a></li>
				<li><a href="#">销量降序</a></li>
				<li><a href="#">评分降序</a></li>
			</ul>
		</div>
	</div>
	<article class="m_article" id="m_scrooler">
		<div class="m_bodycont_1">
			<div class="m_detail">
				<div class="m_roombanner">
					<ul id="m_roomslider">
					[#list product.productImages as productImage]
					 	<li><img src="${productImage.medium}"></li>
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
					<div class="[#if hasFavorite==0]m_collect m_down p_turnin[#else]m_collect p_turnout[/#if]"><i class="iconfont" id="addFavorite">&#xe605</i></div>
					<div class="m_clear"></div>
				</div>
				<div class="m_detailtitle">
					<a href="${base}/mobile/groupon/introduction.jhtml?id=${product.id}">
						<h1>${product.fullName}</h1>
						<p>当前价<span id="x_wholePrice">${currency(promotion.promotionPrice,true)}</span> <del id="x_price">原价${currency(promotion.promotionOriginalPrice,true)}</del> 赠送积分 <span>${promotion.promotionPoint}</span></p>
						<div class="p_tag"></div>
					</a>
				</div>
				<div class="m_detailtitle m_gery">
					<a href="${base}/mobile/groupon/joinPeople.jhtml?grouponId=${promotion.id}">
						<p class="m_gery">参与人数：<span>${memberNum}</span>人 ( 开团人数<span>${promotion.minimumQuantity}</span>人, 满团人数<span>${promotion.maximumQuantity}</span>人)</p>
						<p class="m_fontgery">起始时间${promotion.beginDate} 截止时间 ${promotion.endDate}</p>
						<div class="p_tag p_tuantag"></div>
					</a>
				</div>
			</div>
			<div class="m_h10"></div>
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
					<input type="hidden" id="productId" value="${product.id}"/>
					<input type="hidden" id="hasFavorite" value="${hasFavorite}"/>
					<input type="hidden" id="grouponId" value="${promotion.id}"/>
				</div>
			</div>
			<div class="m_h10"></div>
			<div class="m_specification" id="reviewList">
				<h1>团购评价</h1>
				<div class="m_specificationstar"><del>(${product.scoreCount})</del><span class="score${(product.score * 2)?string("0")}"></span></div>
				<div class="p_tag"></div>
			</div>
			<div class="m_specification" id="consultationsList">
				<h1>咨询</h1>
				<div class="m_specificationstar"><del>${count}</del></div>
				<div class="p_tag"></div>
			</div>
			<div class="m_evaluate">
				<ul>
					<li><a href="javascript:;"><i class="iconfont">&#xe606</i><span id="addReview">我要评价>></span></a></li>
					<li><a href="javascript:;"><i class="iconfont">&#xe607</i><span id="addConsultation">我要咨询>></span></a></li>
				</ul>
			</div>
			<div class="m_h10"></div>
			<div class="m_specification m_llive">
				<h1>团购清单</h1>
			</div>
			<div class="m_listcont m_listcont_detail">
				<ul>
					[#list products as product]
					<li>
						<a href="${base}/mobile/groupon/content/${promotion.id}/${product.id}.jhtml">
							<div class="m_productk">
								<img src="${product.thumbnail}">
								<p><span>${currency(product.price,true)}</span><del>${currency(product.marketPrice,true)}</del></p>
								<p>${product.fullName}</p>
							</div>
						</a>
					</li>
					[/#list]
					<div class="m_clear"></div>
				</ul>
			</div>
		</div>
	</article>
	<footer class="m_footer">
		<div class="m_detailbtn">
			<ul>
				<li class="m_time" id="m_time" [#if promotion.beginDate??] beginTimeStamp="${promotion.beginDate?long}"[/#if][#if promotion.endDate??] endTimeStamp="${promotion.endDate?long}"[/#if]></li>
				<li><a href="javascript:;"><span class="m_red" id="submit">立即参与</span></a></li>
			</ul>
		</div>
	</footer>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
