<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/weixin/css/library.css" />
<link rel="stylesheet" href="${base}/resources/weixin/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/weixin/css/common.css" />
<script type="text/javascript" src="${base}/resources/weixin/js/iscroll.js"></script>
<script type="text/javascript" src="${base}/resources/weixin/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/weixin/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/weixin/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
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
		 var tenantCategoryId = $("#tenantCategoryId").val();
		if(tenantCategoryId==""){
			url="${base}/weixin/delivery/loadmore.jhtml";
		}else{
			url="${base}/weixin/delivery/loadmore/"+tenantCategoryId+".jhtml";
		}
		var el, li, i;
		el = document.getElementById('tenantList');
		pageNum=pageNum+1;
		$.ajax({
			url:url,
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum,
				  lng:$("#location_x").val(),
				  lat:$("#location_y").val(),
				  communityId:$("#communityId").val(),
				  areaId:$("#areaId").val(),
				  distatce:$("#distatce").val()},
			success:function(result){
				 if(result!=null&&result.length>0){
						 for(var i=0;i<result.length;i++){
					 		var tmpStr="";
						 	 li = document.createElement('li');
						 	 	tmpStr+="<div class=a><div class=m_productk><a href=${base}/weixin/tenant/"
						 	 			+result[i].id
						 	 			+"/index.jhtml><img src="
						 	 			+result[i].tenant.logo
						 	 			+" width=80px; height=80px;/></a><h2><a href=${base}/weixin/tenant/"
						 	 			+result[i].id
						 	 			+"/index.jhtml>"
						 	 			+result[i].tenant.name
						 	 			+"["+result[i].name+"]"
						 	 			+"</a></h1><span class=score"
						 	 			+ parseFloat(result[i].tenant.score)*2
						 	 			+"></span><p class=padding5>主营：";
						 	 			if(result[i].tenant.productCategoryTenants.length>0){
											 for(var j=0;j<result[i].tenant.productCategoryTenants.length;j++){
											 	if(j<=2){
												 tmpStr+=result[i].tenant.productCategoryTenants[j].name+"/";
											 	}
											 }
										 }
										tmpStr+="</p><p class='toShowMap address'";
											if(result[i].location!=null&&result[i].location!=""){
												tmpStr+=" defaultDeliveryCenterId="+result[i].id ;
											}
										tmpStr+="><i class='iconfont'>&#xe602</i>"+result[i].address+"</p><div class=m_shopdistance>";
										if(result[i].distance==null){
											tmpStr+="暂无信息";
										}else{
											tmpStr+=currency(result[i].distance,false,false)+"m";
										}
										tmpStr+="</div></div></div>";
				    		 li.innerHTML=tmpStr;	
				    		 el.appendChild(li, el.childNodes[0]);
				    	}	 
				 }else{
				 	pullUpEl.querySelector('.pullUpLabel').innerHTML = '亲，已经到底了...';
				 	return false;
				 }
	    		$('#m_scrooler_0').iScroll('refresh');													
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
[@area_current]
 [#assign areaId=currentArea.id]
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont">
			<div class="m_city" areaId="${currentArea.id}"><i class="iconfont">&#xe602</i><div>${currentArea.name}</div></div>
			[#include "/weixin/include/top_search.ftl" /]	
			<div class="m_title" alt="${setting.siteName}">${setting.siteName}</div>
		</div>
	</header>
	<article class="m_article m_article_list" id="m_scrooler_0">
	<div class="m_bodycont_1">
		<form id="deliveryForm" action="${base}/weixin/delivery/list[#if tenantCategory!=null]/${tenantCategory.id}.jhtml[#else].jhtml[/#if]" method="get">
			<div class="m_bodycont_2">
				<div class="m_listcont m_shoplist">
					<ul id="tenantList">
							[#list page.content as delivery]
							<li>
								<div class="a">
									<div class="m_productk">
										<a href="${base}/weixin/delivery/${delivery.id}/index.jhtml"><img src="${delivery.tenant.logo}" width="80px;"height="80px;"/></a>
										<h2><a href="${base}/weixin/delivery/${delivery.id}/index.jhtml">${delivery.tenant.name}[${delivery.name}]</a></h1>
										<span class="score${(delivery.tenant.score * 2)?string("0")}"></span>
										<p class="padding5">主营：
											[#list delivery.tenant.productCategoryTenants as productCategoryTenant]
												[#if productCategoryTenant_index<=2]
												 	${productCategoryTenant.name}
												 	[#if productCategoryTenant_has_next]/[/#if]
											 	[/#if]
											[/#list] 
										</p>	
										<p class="toShowMap address" [#if delivery.location??] defaultDeliveryCenterId="${delivery.id}" [/#if] ><i class="iconfont">&#xe602</i>${delivery.address}</p>
										<div class="m_shopdistance">
											[#if delivery.distance??]
												[#if delivery.distance>1000]
													${currency((delivery.distance)/1000,false,false)}km
												[#else]
													${currency(delivery.distance,false,false)}m
												[/#if]	
											[#else]
												暂无信息
											[/#if]
										</div>
									</div>
								</div>
							</li>
							[/#list]
					</ul>
						<div id="pullUp">
							<span class="pullUpLabel">拉起加载更多...</span>
						</div>
				</div>
			</div>
		</form>
	</div>
	</article>
	<div class="m_bodybg"></div>
</section>
[#include "/weixin/include/area_select.ftl" /]	
[/@area_current]
<div class="m_Areas_bg"></div>
</body>
</html>
