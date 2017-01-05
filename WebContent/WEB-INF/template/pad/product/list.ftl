<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<!-- <meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" /> -->
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />
<script type="text/javascript" src="${base}/resources/pad/js/iscroll.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript">

$().ready(function() {
	var $productForm = $("#productForm");
	var $pageNumber = $("#pageNumber");
	var $brandId = $("#brandId");
	var $startPrice = $("#startPrice");
	var $endPrice = $("#endPrice");
	var $allBrand =$("a[name='allBrand']");
	var $allOrderType =$("li[name='allOrderType']");
	var $chooseOrderType = $("#chooseOrderType");
	var $saleUp = $("#saleUp");
	var $saleDown = $("#saleDown");
	var $priceUp = $("#priceUp");
	var $priceDown = $("#priceDown");
	var $comfirm = $("#comfirm");
	var $search=$("#search");
	var $cartItemCount=$("#cartItemCount");
	var $loadData = $("#loadData");
	var $addCart =$("div[name='addCart']");
	var $add =$("span[name='add']");
	var $sub =$("span[name='sub']"); 
	var $goCart =$("#goCart");
	var $memberCenter = $("#memberCenter");
	var $p_phonetic = $("#p_phonetic span");
	var $phonetic = $("#phonetic");
	var $return = $("#return");
	var totalPage = ${page.totalPages};
	var $product = $(".a_productId");
	var $p_search = $("#p_search");
	var $reset = $("#reset");
	var $quantity = $("input[name='quantity']");
	var p_array= new Array(3);
	//初始化拼音
	if($phonetic.val()!=""){
		var select_array=$phonetic.val().split("");
		p_array=select_array;
		$p_phonetic.each(function(){
			var $this = $(this);
			if($.inArray($this.text(),select_array)>=0){
				$this.addClass("p_reda");
			}			
		});
	}
	//拼音收缩
	$p_phonetic.on("tap",function(){
		var $this = $(this);
		if($this.hasClass("p_reda")){
			p_array.push($this.text());
		}else{
			p_array.pop();
		}
		return false;
	});
	//重置
	$reset.on("tap",function(){
		p_array=[];	
		$p_phonetic.removeClass("p_reda");
		return false;
	});
	//拼音搜索
	$p_search.on("tap",function(){
		var t_string="";
		for(var i in p_array){
			t_string+=p_array[i];
		}	
		$phonetic.val(t_string);
		$pageNumber.val(1);
		$productForm.submit();
		return false;
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
	//数量
	$quantity.live('keypress',function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) || key==8) {
			return true;
		} else {
			return false;
		}
	});
	//进入商品详情
	$product.live("tap",function(){
		var $this = $(this);
		location.href="${base}/pad/product/detail/"+$this.attr("productId")+".jhtml"; return false;
	});
	
	//返回
	$return.on("tap",function(){
		location.href="${base}/pad";return false;
	})
	
	//进入购物车
	$goCart.on("tap",function(){
		location.href="${base}/pad/cart/list.jhtml";
		return false;
	});	
	
	//会员中心
	$memberCenter.on("tap",function(){
		location.href="${base}/pad/member/center.jhtml";return false;
	})
	//订购选择减
	$sub.live('tap',function(){
		var $this = $(this);
		var $num=$this.siblings(".p_listCountt").find("input");
		if(parseInt($num.val())>0){
			$num.val( parseInt($num.val())-1);
		}
		return false;
	});
	//订购选择加
	$add.live('tap',function(){
		var $this = $(this);
		var $num=$this.siblings(".p_listCountt").find("input");
		$num.val(parseInt($num.val())+1);
		return false;
	});
	
	//加入购物车
	$addCart.on('tap',function(){
		var $this = $(this);
		var $packagUnitId = $this.parent().parent().siblings("input.packagUnitId");
		var $productId = $this.parent().parent().siblings("input.productId");
		var $quantity = $this.siblings(".p_listCount").children("div").children("input");
		var packagUnitId = $packagUnitId.val();
		var productId = $productId.val();
		var quantity = $quantity.val();
		if(!(/^\+?[1-9][0-9]*$/.test(quantity))){
	 		ptips("购买数量只能为大于0的正整数");
	 		return false;
	 	}
		if(quantity==0){
			alert("购买数量不能为0");
			return false;
		}
		$.ajax({
			url :'${base}/pad/cart/add.jhtml',
			data:{id:productId,quantity:quantity,packagUnitId:packagUnitId},
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.type='success'){
					getCartCount();
				}
			}
		});
		return false;
	});
	//加入购物车
	$addCart.live('tap',function(){
		var $this = $(this);
		var $packagUnitId = $this.parent().parent().siblings("input.packagUnitId");
		var $productId = $this.parent().parent().siblings("input.productId");
		var $quantity = $this.siblings(".p_listCount").children("div").children("input");
		var packagUnitId = $packagUnitId.val();
		var productId = $productId.val();
		var quantity = $quantity.val();
		if(!(/^\+?[1-9][0-9]*$/.test(quantity))){
	 		ptips("购买数量只能为大于0的正整数");
	 		return false;
	 	}
		if(quantity==0){
	 		alert("购买数量不能为0");
	 		return false;
	 	}
	 	$.ajax({
			url :'${base}/pad/cart/add.jhtml',
			data:{id:productId,quantity:quantity,packagUnitId:packagUnitId},
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.type='success'){
					getCartCount();
				}
			}
		});
		return false;
	});
	//单位选择
	$('.p_unit').live('tap',function(){
		var $this = $(this);
		var $allUnits = $this.find(".allUnits");
		var length = $allUnits.length;
		if(length==0){
			return false;
		}
		var $count = $this.parent().parent().siblings("input.count");
		var $packagUnitId = $this.parent().parent().siblings("input.packagUnitId");
		if(parseInt($count.val())<length){
			var $temp =$($allUnits[$count.val()]);
			$this.find("span").text($temp.val());
			$packagUnitId.val($temp.attr("unitId"));
			$this.siblings("a").find("h1").text(currency($temp.attr("wholePrice"),true)).append("<del>"+currency($temp.attr("price"),true)+"</del>");
			$count.val(parseInt($count.val())+1);
		}else{
			$count.val("0");
			$packagUnitId.val("");
			$this.find("span").text($this.find("span").attr("unit"));
			$this.siblings("a").find("h1").text(currency($this.siblings("a").find("h1").attr("wholePrice"),true));
			$this.siblings("a").find("h1").append("<del>"+currency($this.siblings("a").find("h1").attr("price"),true)+"</del>");
		}
		if($(this).hasClass('p_dw')){
			$(this).removeClass('p_dw');
		}else{
			$(this).addClass('p_dw');
		}
	});
	
	
	//排序类型
	$allOrderType.on('tap',function(){
		$this = $(this);
		$("#chooseOrderType").text($this.text());
		$("#orderType").val($this.attr('val'));
		$phonetic.val("");
		$pageNumber.val(1);
		$productForm.submit();
		return false;
	});
	//销量降序
	$saleDown.on('tap',function(){
		var orderType = $("#orderType").val();
		if(orderType=="salesDesc"){
			$("#orderType").val("");
		}else{
			$("#orderType").val("salesDesc");
		}
		$phonetic.val("");
		$pageNumber.val(1);
		$productForm.submit();
	});
	//价格升序
	$priceUp.on('tap',function(){
		var $this = $(this);
		$("#orderType").val($this.attr("orderType"));
		$phonetic.val("");
		$pageNumber.val(1);
		$productForm.submit();
	});
	
	//价格降序
	$priceDown.on('tap',function(){
		var $this = $(this);
		$phonetic.val("");
		$pageNumber.val(1);
		$("#orderType").val($this.attr("orderType"));
		$productForm.submit();
	});
	
	
	//价格区间确定
	$comfirm.on('tap',function(){
		$phonetic.val("");
		$pageNumber.val(1);
		$productForm.submit();
	});
	

	//品牌筛选
	$allBrand.on('tap',function(){
		var $this = $(this);
		$("#brandId").val($this.attr('bId'));
		$phonetic.val("");
		$pageNumber.val(1);
		$productForm.submit();
		return false;
	});
	
	$search.on('tap',function(){
		if($("#keyword").val()==''){
			alert("请输入搜索关键词");
			return false;
		}
		location.href = "${base}/pad/product/search.jhtml?keyword="+$("#keyword").val();
		return false;
	});
	
	var $first = $("#first");
	var $second = $("#second");
	$first.on('tap',function(){
		var $this = $(this);
		var productCategoryId = $this.attr('firstId');
		location.href = "${base}/pad/product/list.jhtml?productCategoryId="+productCategoryId;
	});
	$second.on('tap',function(){
		var $this = $(this);
		var productCategoryId = $this.attr('secondId');
		location.href = "${base}/pad/product/list.jhtml?productCategoryId="+productCategoryId;
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
					$cartItemCount.show();
				}
			}
		});
	};
	getCartCount();
});
	
var url ="${base}/pad/product/list/${productCategory.id}.jhtml";
var myScroll,pullUpEl, pullUpOffset,generatedCount = 0;
//ajax加载
function ajaxAddContent(propertyStr){
	$.ajax({
		url:url+propertyStr,
		type:"get",
		dataType:"json",
		success:function(data){
			if(data!=null){
				for(var i in data.content){
						var tempStr="<li><input type='hidden' class='productId' value='"
									+data.content[i].id
									+"'/><input type='hidden' class='count' value='0'/><input type='hidden' class='packagUnitId' /><div class='p_recommendbodyn'><div class='p_recommendbody'><a href='${base}/pad/product/detail/"
									+ data.content[i].id
									+".jhtml'><img src="
									+data.content[i].thumbnail
									+" height='110px' width='110px'/><h1 wholePrice="
									+ data.content[i].wholePrice
									+" price="
									+ data.content[i].price
									+">"
									+currency(data.content[i].wholePrice,true)
									+"<del>"
									+currency(data.content[i].price,true)
									+"</del></h1><p>"
									+data.content[i].name
									+"</p></a><div class='p_listCount p_listCount_1'><span class='p_minus' name='sub'>-</span><div class='p_listCountt'><input type='text' value='1' name='quantity'/></div><span class='p_plus' name='add'>+</span></div><div class='p_unit'><span unit="
									+data.content[i].unit
									+">"
									+data.content[i].unit
									+"</span>";
						tempStr+="<input type='hidden' class='allUnits' wholePrice='"+data.content[i].wholePrice+"' price='"+data.content[i].price+"' unitId='' value='"+data.content[i].unit+"'/>";
						for(var j in data.content[i].packagUnits){
							tempStr+="<input type='hidden' class='allUnits' wholePrice='"+data.content[i].wholePrice+"' price='"+data.content[i].packagUnits[j].wholePrice+"' unitId='"+data.content[i].packagUnits[j].id+"' value='"+data.content[i].packagUnits[j].name+"'/>";
						}
						tempStr+="</div><div class='p_recommendbtn' name='addCart'>加入购物车</div><div class='p_discount'><h1>"
									+(data.content[i].wholePrice*10.0/data.content[i].price)
									+"<span>折</span></h1></div></div><div class='p_clear'></div></div></li>";
						$("#firstUl").append(tempStr);
				}
			}else{
				pullUpEl.querySelector('.pullUpLabel').innerHTML="亲，已经拉到底了。。"
			}
			return false;
		}
	});
}
function pullUpAction () {
	setTimeout(function () {	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
		$("#pageNumber").val(parseInt($("#pageNumber").val())+1);
		ajaxAddContent("?pageNumber="+$("#pageNumber").val()+"&orderType="+$("#orderType").val()+"&brandId="+$("#brandId").val()+"&startPrice="+$("#startPrice").val()+"&endPrice="+$("#endPrice").val()+"&phonetic="+$("#phonetic").val());
		myScroll.refresh();		// 请记住，当刷新内容加载（即：在阿贾克斯完成
	}, 1000);	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
}
function loaded() {
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	
	myScroll = new iScroll('wrapper', {
		useTransition: true,
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
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '拉起加载更多...';
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
	document.getElementById('wrapper').style.left = '0'; ;
}
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
</script>
<style>
.p_listbodyleft{left:1000px}
</style>
<script type="text/javascript">

</script>

</head>
<body>
<section class="p_section">
	[#include "/pad/include/navigation.ftl" /]
	<form id="productForm" action="${base}/pad/product/list.jhtml" method="get">
		<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategory.id}">
		<input type="hidden" id="brandId" name="brandId" value="${(brand.id)!}">
		<input type="hidden" id="orderType" name="orderType" value="${orderType}"/>
		<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
		<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
		<input type="hidden" id="phonetic" name="phonetic" value="${phonetic}" />
		<div class="p_header">
			<div class="p_hbody">
				<a href="javascript:void();" class="p_return" id="return">
					<div class="p_tag"></div>
				</a>
				<div class="p_classification p_listcf"><i class="iconfont">&#xf000e</i></div>
				<div class="p_function p_listfunction">
					<ul>
						<!-- <li class="p_ss" >[#if brand??]${brand.name}[#else]所有品牌[/#if]<span class="p_ssicon"></span><span class="p_ssiconl"></span></li> -->
						<li class="p_listsort" id="chooseOrderType">
							[#if orderType??]
								${message("Product.OrderType." + orderType)}
							[#else]
								${message("Product.OrderType." + orderTypes[0])}
							[/#if]
						</li>
						<li class="p_sales" id="saleDown">销量 
							<span class="[#if orderType=='salesDesc']p_uptag p_uptag_1[#else]p_uptag[/#if]" orderType="salesDesc"></span>
							<span class="[#if orderType=='salesDesc']p_downtag p_downtag_1[#else]p_downtag[/#if]" orderType="salesDesc"></span>
						</li>
						<li class="p_price">价格
							<span class="[#if orderType=='priceAsc']p_uptag p_uptag_1[#else]p_uptag[/#if]" id="priceUp" orderType="priceAsc"></span>
							<span class="[#if orderType=='priceDesc']p_downtag p_downtag_1[#else]p_downtag[/#if]" id="priceDown" orderType="priceDesc"></span>
						</li>
						<li class="p_interval [#if startPrice??||endPrice??]p_intervalcipb[/#if]">
							<span class="title">价格区间</span>
							<div class="p_intervalc">
								<span class="p_intervalcip"><input type="text" name="startPrice" id="startPrice" value="${startPrice}"/></span> -
								<span class="p_intervalcip"><input type="text" name="endPrice" id="endPrice" value="${endPrice}"/></span>
								<span class="p_intervalcbtn" id="comfirm">确定</span>
							</div>
						</li>
					</ul>
				</div>
				<div class="p_search">
					<div class="p_search_icon"></div>
					<input type="text" placeholder="请输入搜索内容"/>
				</div>
			</div>
		</div>
		<div class="p_listsortc" id="p_sortcscrooll">
			<ul>
			    [#list orderTypes as ot]
					<li name="allOrderType" val="${ot}">
			    		${message("Product.OrderType." + ot)}
					</li>
				[/#list]
			</ul>
		</div>
		<!-- <div class="p_ssc" id="p_sscscrooll">
			<div>
				<span><a href="javascript:void(0)" bId="" name="allBrand">所有品牌</a></span>
				[#list productCategory.brands as brand]
				 	<span><a href="javascript:void(0)" bId="${brand.id}" name="allBrand">${brand.name}</a></span>
			    [/#list]
			</div>
		</div> -->
		<div class="p_ssclistall">
			<div class="p_ssclist p_ssclist_1" id="p_phonetic">
				<div class="p_ssclistulp p_ssclistul" id="p_sslcroller">
					<div>
					[#list phonetics as phonetic1]
						<span [#if phonetic==phonetic1]class="p_reda"[/#if]>${phonetic1}</span>
					[/#list]
					</div>
				</div>
				<div class="p_sscllastz">品牌</div>
				<div class="p_sscllastc" id="reset">重置</div> 
				<div class="p_sscllast" id="p_search">搜索</div> 
			</div>
			<div class="p_ssclist p_ssclist_2" id="p_phonetic">
				<div class="p_ssclistulp" id="p_sslcroller1">
					<div>
						[#list productCategory.brands as brand]
						 	<span><a href="javascript:void(0)" bId="${brand.id}" name="allBrand">${brand.name}</a></span>
					    [/#list]
					</div>
				</div>
				<div class="p_sscllastp">字母</div>
			</div>
		</div>
	</form>
	<article class="p_article p_articlelist" id="wrapper">
		<div class="bodycont_list scroller" id="loadData">
			<div class="p_indexrecommend p_indexrecommendtag">
				<div class="p_recommend_list p_list">
					<div class="p_listbody" >
							<ul id="firstUl">
							[#if page.content??&&page.content?has_content]
								[#list page.content as product]
								 	<li id="firstli">
										<input type="hidden" class="productId" value="${product.id}"/>
										<input type="hidden" class="count" value="0"/>
										<input type="hidden" class="packagUnitId" />
										<div class="p_recommendbodyn">
											<div class="p_recommendbody">
												<a href="javascript:void();" productId="${product.id}" class="a_productId">
													<img src="${product.thumbnail}" height="110px" width="110px"/>
													<h1 wholePrice="${product.wholePrice}" price="${product.price}">${currency(product.wholePrice,true)} <del>${currency(product.price,true)}</del></h1>
													<p>${product.name}</p>
												</a>
												<div class="p_listCount p_listCount_1">
													<span class="p_minus" name="sub">-</span>
													<div class="p_listCountt">
														<input type="text" value="1" name="quantity"/>
													</div>
													<span class="p_plus" name="add">+</span>
												</div>
												<div class="p_unit"><span unit="${product.unit}">${product.unit}</span>
													<input type="hidden" class="allUnits" wholePrice="${product.wholePrice}" price="${product.price}" unitId="" value="${product.unit}"/>
													[#list product.packagUnits as packagUnit]
													 	<input type="hidden" class="allUnits" wholePrice="${packagUnit.wholePrice}" price="${packagUnit.price}" unitId="${packagUnit.id}" value="${packagUnit.name}"/>
													[/#list]
												</div>
												<div class="p_recommendbtn" name="addCart">加入购物车</div>
												<div class="p_discount">
													<h1>${(product.wholePrice*10.0/product.price)?string("0.0")}<span>折</span></h1>
												</div>
											</div>
											<div class="p_clear"></div>
										</div>
								  </li>
							  [/#list]
						  [/#if] 
						  <div class="p_clear"></div>
						</ul>
						<div class="p_clear"></div>
						<div id="pullUp">
							<span class="pullUpIcon"></span><span class="pullUpLabel">拉起刷新...</span>
						</div>
						<div class="p_clear"></div>
					</div>
					<div class="p_clear"></div>
				</div>
				<div class="p_h50"></div>
			</div>
			<div class="p_pulldown"></div>
		</div>
	</article>
	
	<footer class="p_footer">
		<div class="p_listzt">
		<span>当前类别</span>:
		<span id="first" [#if grandpa??]firstId="${grandpa.id}"[#elseif parent??]firstId="${parent.id}"[#elseif productCategory??]firstId="${productCategory.id}"[/#if]>[#if grandpa??]${grandpa.name}[#elseif parent??]${parent.name}[#elseif productCategory??]${productCategory.name}[/#if]</span>
		<span id="second" [#if grandpa??]secondId="${parent.id}" [#elseif parent??]secondId="${productCategory.id}"[/#if] > [#if grandpa??]>${parent.name}[#elseif parent??]>${productCategory.name}[/#if]</span>
		<span>[#if grandpa??&& parent??&& productCategory??]>${productCategory.name}[/#if]</span>
		</div>
		<div class="p_navigation">
			<ul>
				<li><a href="#" alt="历史浏览"><i class="iconfont">&#xe60d</i></a></li>
				<li><a href="javascript:void();" alt="购物车" id="goCart"><i class="iconfont">&#xe6d1</i></a>
					<span class="p_cartcount" id="cartItemCount">[#if count??&&count>0]${count}[/#if]</span>
				</li>
				<li><a href="javascript:void();" alt="会员中心" id="memberCenter"><i class="iconfont">&#x3432</i></a></li>
				<li class="p_home"><a href="${base}/pad" alt="首页"><i class="iconfont">&#xf0019</i></a></li>
			</ul>
		</div>
	</footer>
</section>
<div class="p_searchfixed">
	<div class="p_search_icon"></div>
	<input type="text" placeholder="请输入搜索内容" id="keyword"/>
	<div class="p_search_button" id="search">搜索</div>
</div>
<div class="p_windowbg_1"></div>
<div class="p_totop"><div class="p_tag"></div></div>
</body>
</html>
