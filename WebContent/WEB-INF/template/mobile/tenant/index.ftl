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
	var $introduction = $("#introduction");
	var $tenantId = $("#tenantId");
	var $MyProduct = $("#MyProduct");
	var $return_btn = $("#return_btn");
	var $viewVideo = $("#viewVideo");
	var $careTenant = $("#careTenant");
	var $backIndex = $("#backIndex");
	var $tjProducts = $("#tjProducts")
	var $productCategoryTenantId = $("#productCategoryTenantId")
	
	$careTenant.on('tap',function(){
		var tenantId = $tenantId.val();
		var $hasFavorite = $("#hasFavorite");
		if($hasFavorite.val()=="0"){
			//收藏过，点击则取消收藏
			$.ajax({
			url: "${base}/mobile/favorite/tenant/delete.jhtml?id="+$tenantId.val(),
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				if(message.type=='success'){
					$careTenant.removeClass('m_down');
					$hasFavorite.val(1);
					mtips("取消关注");
				}
				location.href="${base}/mobile/tenant/"+tenantId+"/index.jhtml";
			}
		});
		}else{
			//未收藏，点击收藏
		$.ajax({
			url: "${base}/mobile/favorite/tenant/add.jhtml?id="+$tenantId.val(),
			type: "POST",
			dataType: "json",
			cache: false,
			success: function(message) {
				if(message.type=='success'){
					$careTenant.addClass('m_down');
					$hasFavorite.val(0);
					mtips("关注成功");
					setTimeout(function(){location.href="${base}/mobile/tenant/"+tenantId+"/index.jhtml";}, 1 * 1000);
				}else if(message.type=='error'){
					mtips("请先登录,再关注该商家!");
					setTimeout(function(){location.href="vsstoo://login/?redirectURL=mobile/tenant/"+$tenantId.val()+"/index.jhtml";}, 1 * 1000);
				}else{
					mtips("最多只能关注10个商家");
				}
			}
		});
	 }
		return false;
	});
	$introduction.on('tap',function(){
		var tenantId = $tenantId.val();
		location.href="${base}/mobile/tenant/introduction.jhtml?tenantId="+tenantId;
	});
	
	$MyProduct.on('tap',function(){
		var tenantId = $tenantId.val();
		location.href="${base}/mobile/tenant/productList.jhtml?tenantId="+tenantId;
	});
	$viewVideo.on('tap',function(){
		location.href="vsstoo://viewVideo?username=[#if tenant.member.username=='happywine']0592000198[#else]${tenant.member.username}[/#if]"; return false;
	});
	$backIndex.on("tap",function(){
		location.href="${base}/mobile/index.jhtml?backIndex=true"; return false;
	});
	$tjProducts.find("a").on("tap",function(){
		location.href="${base}/mobile/product/content/"+$(this).attr("productid")+".jhtml"; return false;
	});
	
	$productCategoryTenantId.find("a").on("tap",function(){
		location.href="${base}/mobile/tenant/${tenant.id}/index.jhtml?productCategoryTenantId="+$(this).attr("productCategoryTenantId"); return false;
	});
	//进入地图导航页
	 $(".toShowMap").on("tap",function(){
	 	var $this=$(this);
	 	if($this.attr("defaultDeliveryCenterId")==null||$this.attr("defaultDeliveryCenterId")==""){
	 		return false;
	 	}
	 	location.href="vsstoo://showMap?id="+$this.attr("defaultDeliveryCenterId");return false;
	 });
	 
	[#if backUrl??]
	  $("#backto").on("tap",function(){
		  mtips("正在为您跳转，请稍等。。");
		  location.href="${backUrl}";
	  })	
	[/#if]
	
	// 点击数
	$.ajax({
		url: "${base}/b2b/tenant/hits/${tenant.id}.jhtml",
		type: "GET"
	}); 
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
		var el, li, i;
		el = document.getElementById('productList');
		pageNum=pageNum+1;
		$.ajax({
			url:"${base}/mobile/tenant/${tenant.id}/loadmore.jhtml",
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum,productCategoryTenantId:"${productCategoryTenantId}"},
			success:function(result){
				if(result.content.length>0){
					for(var i=0;i<result.content.length;i++){
						li = document.createElement('li');
						li.innerHTML ="<a href=${base}/mobile/product/content/"+result.content[i].id+".jhtml><div class='m_productk'><img src="+result.content[i].medium+" width='140px;' height='140px;'><p><span>"
						+currency(result.content[i].price,true)
						+"</span><del>"
						+currency(result.content[i].marketPrice,true)
						+"</del></p><p>"
						+result.content[i].fullName
						+"</p></div></a>";
						el.appendChild(li, el.childNodes[0]);
					}
				}else{
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
					return false;
				}
				$('#m_scrooler_0').iScroll('refresh');// 请记住，当刷新内容加载（即：在阿贾克斯完成
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
			<div class="m_title" alt="选择日期">商家首页</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler_0">
		<div class="m_bodycont_1">
		<input type="hidden" id="tenantId" value="${tenant.id}">
		<input type="hidden" id="hasFavorite" value="${hasFavorite}"/>
		<div class="m_roombanner">
			[@ad_position id = 70 tenantId=tenant.id count=3/]
				<div class="m_round">
				[@ad_position id=70 tenantId=tenant.id count=3]
				 	[#if adPosition??&&adPosition.ads?has_content]
						 [#list adPosition.ads as ad]
						  	[#if ad_index==0]
						  	 	<span class="themeStyle"></span>
						  	[#else]
						  	 	<span></span>
						 	[/#if]
					 	 [/#list]
					[/#if]
			[/@ad_position]
			 </div>
		</div>
		<div class="m_h1"></div>
			<div class="m_shoptop">
				<div class="m_logov">
					<img src="${tenant.thumbnail}" width="80px" height="80px" id="viewVideo"/>
					<i class="iconfont">&#xe615</i>
				</div>
				<h1 id="introduction">${tenant.name}</h1>
				<span class="score${(tenant.score * 2)?string("0")}"></span>
				<h2><i class="iconfont">&#xe604</i><a href='tel:${tenant.telephone}'>${tenant.telephone}</a></h2>
				<h2 class="toShowMap"  [#if tenant.defaultDeliveryCenter.location??] defaultDeliveryCenterId="${tenant.defaultDeliveryCenter.id}" [/#if]><i class="iconfont">&#xe602</i>${tenant.address}</h2>
			</div>
			<!-- <div class="m_specification m_nob" id="MyProduct">
				<h1>经营商品<span></span></h1>
				<div class="p_tag"></div>
			</div>
			 -->
			 <!-- <div class="m_evaluate">
				<ul>
					<li id="careTenant" class="[#if hasFavorite==0]m_down[#else][/#if]"><a href="javascript:;"><i class="iconfont">&#xe605</i><span>[#if hasFavorite==0]已关注[#else]关注商家[/#if]</span></a></li>
					<li><a href="javascript:;" id="MyProduct"><i class="iconfont">&#xe612</i><span>经营商品>></span></a></li>
				</ul>
			</div> -->
			<div class="m_h1"></div>
			<h1 class="m_shopt_top"><span></span>推荐商品<!-- <a href="vsstoo://nearTenantList">更多</a> --></h1>
			<div class="m_shopt" id="m_shoptscroll">
				<ul id="tjProducts">
					[@product_list areaId=areaId tenantId="${tenant.id}" tagIds=5 ]
					[#list products as product]
						<li><a href="javascript:;" productid="${product.id}"><img width="75px;" height="75px;" src="${product.medium}"><br/>${product.fullName}</a></li>
					[/#list]
					[/@product_list]
					<div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h1"></div>
			<div class="m_shopt m_shopt_1" id="m_shoptscroll_1">
				<ul id="productCategoryTenantId">
					<li  [#if !(productCategoryTenantId??)]class="down"[/#if]><a href="javascript:;" productCategoryTenantId=''>全部</a></li>
					[#list productCategoryTenants as productCategoryTenant]
						<li  [#if productCategoryTenantId??&&productCategoryTenantId==productCategoryTenant.id]class="down"[/#if]> <a href="javascript:;" productCategoryTenantId="${productCategoryTenant.id}">${productCategoryTenant.name}</a></li>
					[/#list]
					<div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h1"></div>
			<div class="m_listcont m_listcont_detail">
			[#if page??&&page.content?has_content]
				<ul id="productList">
					[#list page.content as product]
						<li>
							<a href="${base}/mobile/product/content/${product.id}.jhtml">
								<div class="m_productk">
									<img src="${product.medium}" width="140px;" height="140px;">
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
			[/#if]
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
