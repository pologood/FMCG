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
<script type="text/javascript" src="${base}/resources/mobile/js/iscroll.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $tenantList =$("#tenantList li a");
	var $productList =$("#productList li a");
	var $favorite =$("#favorite");
	var $activity =$("#activity");
	var $groupon =$("#groupon");
	var $auction =$("#auction");
	var $member =$("#member");
	var $channel =$("#channel");
	
	$favorite.on("tap",function(){
		if($member.val()==''){
			mtips("请先登录!");
			setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/member/favorite/list.jhtml?pageNumber=1";}, 1 * 1000);
			return false;
		}else{
			location.href="${base}/mobile/member/favorite/list.jhtml?pageNumber=1";
		}
	});
	$activity.on("tap",function(){
		location.href="${base}/mobile/product/list.jhtml?tagIds=15";
		return false;
	});
	
	$groupon.on("tap",function(){
		if(!$.checkLogin()){
			mtips("参与团购请先登录!");
			setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/groupon/list.jhtml";}, 1 * 1000);
			return false;
		}else{
			location.href="${base}/mobile/groupon/list.jhtml";
		}
	});
	
	$auction.on("tap",function(){
		if(!$.checkLogin()){
			mtips("参与拍卖请先登录!");
			setTimeout(function(){location.href = "vsstoo://login/?redirectURL=mobile/auction/list.jhtml";}, 1 * 1000);
			return false;
		}else{
			location.href="${base}/mobile/auction/list.jhtml";
		}
	});
	
	$tenantList.on("tap",function(){
		var $this =$(this);
		mtips("正在为您跳转，请稍等。。");
		location.href="${base}/mobile/delivery/"+$this.attr("deliveryId")+"/index.jhtml?backUrl=${base}/mobile/index.jhtml";return false;
	});
	$productList.live("tap",function(){
		var $this =$(this);
		mtips("正在为您跳转，请稍等。。");
		location.href="${base}/mobile/product/content/"+$this.attr("productId")+".jhtml?backUrl=${base}/mobile/index.jhtml";return false;
	});
	//搜索
	$("#search").on('tap',function(){
		 var keyword = $("#keyword").val();
		 if(keyword==''){
			 mtips("请输入搜索关键字!");
			 return false;
		 }
		 location.href="${base}/mobile/product/search.jhtml?keyword="+keyword;return false;
	});
	
	$channel.find("a").on("tap",function(){
		var $this =$(this);
		if($this.attr("type")=="article"){
		 location.href="${base}/mobile/article/list/"+$this.attr("channelId")+".jhtml"; return false;
		}else if($this.attr("type")=="expert"){
		 	location.href="${base}/mobile/expert_category/list/"+$this.attr("channelId")+".jhtml"; return false;
		}else{
		 	location.href="${base}/mobile/product_channel/"+$this.attr("channelId")+"/index.jhtml"; return false;
		}
	});
	$("#scan_fun").on("tap",function(){
		location.href="vsstoo://scanFunc";return false;
	});
	$("#adList").find("a").on("tap",function(){
		location.href=$(this).attr("url");return false;
	});
	
});

// 2015-1-11 拖动加载

var myScroll,pullUpEl, pullUpOffset,generatedCount = 0;
var pageNum = ${recommendProducts.pageable.pageNumber};
function pullUpAction () {
	setTimeout(function () {	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
		if(pageNum>=(${recommendProducts.total/recommendProducts.pageable.pageSize})){
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
			return false;
		}
		var el, li, i;
		el = document.getElementById('productList');
		pageNum=pageNum+1;
		$.ajax({
			url:"${base}/mobile/loadmore.jhtml",
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum},
			success:function(result){
				if(result.content.length>0){
					for(var i=0;i<result.content.length;i++){
						li = document.createElement('li');
						li.innerHTML ="<a href='javascript:;' productId="+result.content[i].id+"><div class='m_productk'><img src="+result.content[i].medium+"><p><span>"
						+currency(result.content[i].price,true)
						+"</span></p><p>"
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
				<!--pullUpAction();	// Execute custom function (ajax call?)-->
			}
		}
	});
	
}

document.addEventListener('touchmove', function (e) {e.preventDefault();}, false);

document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
</script>
<style>
.m_search{display:block;}
.m_productt h1{padding:0 5px;}
.m_productt ul{padding:0;}
.m_productt ul li{width:33.3%;}
.m_productt ul li:nth-child(2) a,.m_productt ul li:nth-child(1) a,.m_productt ul li:nth-child(3) a{padding:0 2.5px 2.5px 2.5px;}
.m_search.m_search_n .m_searchon{background: rgba(0,0,0,.1);}
.m_search.m_search_n .p_search_icon{right:auto;}
.m_search.m_search_n .m_searchon span{display:block;}
</style>
</head>
<body>
[@area_current]
 [#assign areaId=currentArea.id]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont">
			<div class="m_city" areaId="${currentArea.id}"><i class="iconfont">&#xe602</i><div>${currentArea.name}</div></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<!-- <div class="m_title" alt="${setting.siteName}">${setting.siteName}</div> -->
			<div class="m_scan" id="scan_fun"><i class="iconfont">&#xe613</i><!-- <div>${currentArea.name}</div> --></div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler_0">
		<div class="m_bodycont_1">
			<input type="hidden" id="member" value="[#if member??]${member.username}[/#if]"/>
			<div class="m_roombanner">
			[@ad_position id = 70 count=5 /]
				<div class="m_round">
				[@ad_position id=70 count=5]
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
				<div class="m_clear"></div>
			</div>
			<!--
			<div class="m_page">
				<ul >
					<li><a herf="javascript:;"><div class="m_pagecont"><i id="favorite" class="iconfont">&#xe605</i><br/>收藏</div></a></li>
					<li><a herf="javascript:;"><div class="m_pagecont"><i id="auction" class="iconfont">&#xe600</i><br/>拍卖</div></a></li>
					<li><a herf="javascript:;"><div class="m_pagecont"><i id="groupon" class="iconfont">&#xe601</i><br/>团购</div></a></li>
					<li><a herf="javascript:;"><div class="m_pagecont"><i id="activity" class="iconfont">&#xe603</i><br/>最惠</div></a></li>
					<div class="m_clear"></div>
				</ul>
			</div>
			-->
			<div class="m_h1"></div>
			<div class="m_promotions" >
				<ul id="adList">
					[@ad_position id = 74 count=1 /]
					[@ad_position id = 75 count=1 /]
					[@ad_position id = 76 count=1 /]
					[@ad_position id = 77 count=1 /]
				</ul>
			</div>
			<div class="m_h1"></div>
			[@ad_position id = 78 count=1 /]
			<div class="m_h1"></div>
			[@delivery_list areaId=currentArea.id isDefault=true count=20 tagIds="6"]
				[#if deliverys?has_content]
					<h1 class="m_shopt_top"><span></span>明星商家</h1>
						<div class="m_shopt" id="m_shoptscroll">
							<ul id="tenantList">
								 [#list deliverys as delivery]
								  	<li><a deliveryId="${delivery.id}" href="javascript:;"><img src="${delivery.tenant.thumbnail}" width="70" height="70"/><br/>${delivery.tenant.name}</a></li>
								 [/#list]
								<div class="m_clear"></div>
							</ul>
						</div>
					<div class="m_h1"></div>
				[/#if]
			[/@delivery_list]
			<h1 class="m_shopt_top"><span></span>精选频道<!-- <a href="vsstoo://nearTenantList">更多</a> --></h1>
			<div class="m_shopt_pd">
				<ul id="channel">
					[#list productChannels as channel]
						<li>
							<a href="javascript:;" channelId="${channel.id}" type="${channel.type}">
								<img src="${channel.image}">
								<span>${channel.name}</span>
							</a>
						</li>
					[/#list]
					<div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h1"></div>
			[@ad_position id = 79 count=1 /]
			<!--
				[#if member?? && member.member?? && member.member.tenant??]
					<div class="m_specification m_shoptj">
						<h1>${member.member.tenant.name}</h1>
						<div class="m_specificationstar"><del>[#if member.member.tenant.distatce??]${member.member.tenant.distatce}m[/#if]</del><span class="score${(member.member.tenant.score * 2)?string("0")}"></span></div>
						<div class="p_tag"></div>
					</div>
				[/#if]
			-->
			<div class="m_h1"></div>
			<div class="m_productt">
				[#if recommendProducts??&&recommendProducts.content?has_content]
					<h1>为您推荐<span></span></h1>
					<ul id="productList">
							[#list recommendProducts.content as product]
								<li>
									<a productId="${product.id}" href="javascript:;">
										<div class="m_productk">
											<img src="${product.medium}">
											<p><span>${currency(product.price,true)}</span><!-- <del>${currency(product.marketPrice,true)}</del> --></p>
											<p>${product.fullName}</p>
										</div>
									</a>
								</li>
							[/#list]
						<div class="m_clear"></div>
					</ul>
					<div id="pullUp">
						<span class="pullUpLabel">拉起加载更多...</span>
					</div>
				[#else]
					<div id="pullUp">
						<span class="pullUpLabel">已经拉到底了...</span>
					</div>	
				[/#if]
			</div>
		</div>
	</article>
</section>
[#include "/mobile/include/area_select.ftl" /]	
[/@area_current]
<div class="m_Areas_bg"></div>
<div class="m_bodybg"></div>
</body>
</html>
