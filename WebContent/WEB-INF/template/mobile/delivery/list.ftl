f<!DOCTYPE HTML>
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
	var tenantCategoryMap={};
	[@compress single_line = true]
	 [#list tenantCategoryList as rootTenantCategory]
			 var childrenOne="${rootTenantCategory.children}";
			 var tempArray=[];
			 [#if rootTenantCategory.children?has_content]
				 [#list rootTenantCategory.children as childrenOne]
						 var temp= {id: "${childrenOne.id}",name:"${childrenOne.name}"};
			 			  tempArray.push(temp);
				 [/#list]
				tenantCategoryMap["${rootTenantCategory.id}"]=tempArray;
		     [/#if]
	 [/#list]
	[/@compress]
	//根分类li点击展开
	 var $root = $("li[name='root']");
	 var $secondCategory = $("#secondCategory");
	 $root.on('tap',function(){
			var $this = $(this);
			var tenantCategoryId=$this.attr("tenantCategoryId");
			var tenantCategoryName=$this.attr("tenantCategoryName");
			$secondCategory.empty();
			$secondCategory.append('<li><a href="${base}/mobile/delivery/list/'+tenantCategoryId+'.jhtml?lat='+$("#location_y").val()+'&lng='+$("#location_x").val()+'">全部</a></li>');
			
			if (typeof(tenantCategoryMap[tenantCategoryId]) != "undefined") { 
				$.each(tenantCategoryMap[tenantCategoryId],function(i,tenantCategory){
					$secondCategory.append('<li><a href="${base}/mobile/delivery/list/'+tenantCategory.id+'.jhtml?lat='+$("#location_y").val()+'&lng='+$("#location_x").val()+'">'
							+tenantCategory.name+
							"</a></li>");
				});
			}  
			$("#secondCategory").iScroll('refresh');
		
		return false;
	});
	 
	 $("#noCategoryId").on("tap",function(){
	 	location.href="${base}/mobile/delivery/list.jhtml?lat="+$("#location_y").val()+"&lng="+$("#location_x").val();
	 	return false;
	 });
	 var $near = $("#near");
	 var $hot = $("#hot");
	 var $secondMenu = $("#secondMenu");
	 var $allOrderType =$("li[name='allOrderType']");
	 var $deliveryForm = $("#deliveryForm");
	 var $areaId = $("#areaId");
	 var $pageNumber = $("#pageNumber");
	 var $totalPages = $("#totalPages");
	 var $pageSize = $("#pageSize");
	 var $orderType = $("#orderType");
	 var $more = $("#more");
	 var $tenantCategoryId = $("#tenantCategoryId");
	 var $tenantList = $("#tenantList");
	 var $load = $("#load");
	 var $communityId = $("#communityId");
	 var $distatce = $("#distatce");
	 var $nearArea = $("li[name='nearArea']");
	 var $MyCare = $("#MyCare");
	 var $memberId = $("#memberId");
	 
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
					 $secondMenu.append("<li name='all' areaId='"+areaId+"'><a href='javascript:void(0)'>全部</a></li>");
			 		if(list!=''){
			 			for(var i=0;i<list.length;i++){
				 			tmpStr+="<li communityId='"+list[i].id+"' name='nearCommunity'><a href='javascript:;'>"+list[i].name+"</a></li>";
				 		}
				 		$secondMenu.append(tmpStr);
			 		}
				 		var $nearCommunity =$("li[name='nearCommunity']");
				 		$nearCommunity.on('tap',function(){
				 			$this = $(this);
				 			var communityId = $this.attr('communityId');
				 			$areaId.val("");
				 			$communityId.val(communityId);
				 			$distatce.val("");
			 				$memberId.val("");
				 			$pageNumber.val(1);
				 			$deliveryForm.submit();
				 		});
				 		var $all =$("li[name='all']");
				 		$all.on('tap',function(){
				 			$this = $(this);
				 			var areaId = $this.attr('areaId');
				 			$areaId.val(areaId);
				 			$communityId.val("");
				 			$distatce.val("");
			 				$memberId.val("");
				 			$pageNumber.val(1);
				 			$deliveryForm.submit();
				 		});
				}
			});
	 });
 
	 
	 //热门商圈
	 $hot.on('tap',function(){
		 var tmpStr ="";
		 $secondMenu.empty();
		 $.ajax({
				url : "${base}/mobile/product/getCommunity.jhtml",
				type:"get",
				dataType:"json",
				success:function(list){
					tmpStr+="<li communityId='' name='hotCommunity'><a href='javascript:;'>全部</a></li>";
			 		if(list!=''){
			 			for(var i=0;i<list.length;i++){
				 			tmpStr+="<li communityId='"+list[i].id+"' name='hotCommunity'><a href='javascript:void(0)'>"+list[i].name+"</a></li>";
				 		}
				 		$secondMenu.append(tmpStr);
			 		}
				 		var $hotCommunity =$("li[name='hotCommunity']");
				 		$hotCommunity.on('tap',function(){
				 			$this = $(this);
				 			var communityId = $this.attr('communityId');
				 			$communityId.val(communityId);
				 			$pageNumber.val(1);
				 			$distatce.val("");
			 				$memberId.val("");
				 			$deliveryForm.submit();
				 		});
				}
			});
	 });
	 $MyCare.on('tap',function(){
		 var memberId = $memberId.val();
		 if(memberId==""){
			 mtips("请先登录才能搜索你所关注的商家!");
			 if($tenantCategoryId.val()==""){
				 location.href = "vsstoo://login/?redirectURL=mobile/delivery/list.jhtml?lat="+$("#location_y").val()+"&lng="+$("#location_x").val();
			 }else{
				 location.href = "vsstoo://login/?redirectURL=mobile/delivery/list/"+$tenantCategoryId.val()+".jhtml?lat="+$("#location_y").val()+"&lng="+$("#location_x").val();
			 }
		 }else{
			 $deliveryForm.submit();
		 }
	 });
	 //进入地图导航页
	 $(".toShowMap").live("tap",function(){
	 	var $this=$(this);
	 	if($this.attr("defaultDeliveryCenterId")==null||$this.attr("defaultDeliveryCenterId")==""){
	 		return false;
	 	}
	 	location.href="vsstoo://showMap?id="+$this.attr("defaultDeliveryCenterId");return false;
	 });
	 
	 setTimeout(function(){$("#hot").trigger("tap");},500);     
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
			url="${base}/mobile/delivery/loadmore.jhtml";
		}else{
			url="${base}/mobile/delivery/loadmore/"+tenantCategoryId+".jhtml";
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
						 	 	tmpStr+="<div class=a><div class=m_productk><a href=${base}/mobile/delivery/"
						 	 			+result[i].id
						 	 			+"/index.jhtml><img src="
						 	 			+result[i].tenant.thumbnail
						 	 			+" width=80px; height=80px;/></a><h2><a href=${base}/mobile/delivery/"
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
											tmpStr+="--";
										}else{
											if(result[i].distance>1000){
												tmpStr+=currency(result[i].distance/1000,false,false)+"km";
											}else{
												tmpStr+=currency(result[i].distance,false,false)+"m";
											}
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
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_title" alt="${setting.siteName}">${setting.siteName}</div>
		</div>
	</header>
	<div class="m_inquiry">
		<ul>
			<li>附近 <span></span></li>
			<li>分类 <span></span></li>
		</ul>
	</div>
	<div class="m_area">
		<div class="m_areal" id="m_areascrooler_1">
			<ul>
				<li id="MyCare">关注的商家</li>
				<li id="hot" class="m_down">热门商圈</li>
				[#if current.children??]
					[#list current.children as area]
					 	<li areaId="${area.id}" name="nearArea">${area.name}</li>
					[/#list]
				[/#if]
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
				<li [#if !tenantCategory??]class="m_down"[/#if] id="noCategoryId">
					全部
				</li>
				[@tenant_category_root_list count = 10]
				[#list tenantCategories as rootTenantCategory]
				 	[#if tenantCategory??&&rootTenantCategory.id==tenantCategory.id]
					 	<li name="root" tenantCategoryName="${rootTenantCategory.name}" class="m_down" tenantCategoryId="${rootTenantCategory.id}">
							${rootTenantCategory.name}
						</li>
					[#else]
						 <li name="root" tenantCategoryName="${rootTenantCategory.name}" tenantCategoryId="${rootTenantCategory.id}">
							${rootTenantCategory.name}
						 </li>
					[/#if]
				[/#list]
				[/@tenant_category_root_list]
			</ul>
		</div>
		<div class="m_arear" id="m_areascrooler_4">
			<ul id="secondCategory">
				
			</ul>
		</div>
	</div>
	<article class="m_article m_article_list" id="m_scrooler_0">
	<div class="m_bodycont_1">
		<form id="deliveryForm" action="${base}/mobile/delivery/list[#if tenantCategory!=null]/${tenantCategory.id}.jhtml[#else].jhtml[/#if]" method="get">
			<input type="hidden" id="areaId" name="areaId" value="[#if area??]${area.id}[/#if]"/>
			<input type="hidden" id="communityId" name="communityId" value="${communityId}" />
			<input type="hidden" id="distatce" name="distatce" value="${distatce}" />
			<input type="hidden" id="tenantCategoryId" name="tenantCategoryId" value="${tenantCategoryId}" />
			<input type="hidden" id="location_x" name="lng" value="${location.lng}" />
			<input type="hidden" id="location_y" name="lat" value="${location.lat}" />
			<input type="hidden" id="memberId" name="memberId" value="[#if member??]${member.id}[/#if]" />
			<div class="m_bodycont_2">
				<div class="m_listcont m_shoplist">
					<ul id="tenantList">
							[#list page.content as delivery]
							<li>
								<div class="a">
									<div class="m_productk">
										<a href="${base}/mobile/delivery/${delivery.id}/index.jhtml"><img src="${delivery.tenant.thumbnail}" width="80px;"height="80px;"/></a>
										<h2><a href="${base}/mobile/delivery/${delivery.id}/index.jhtml">${delivery.tenant.name}[${delivery.name}]</a></h1>
										<span class="score${(delivery.tenant.score * 2)?string("0")}"></span>
										<p class="padding5">
											<a href="${base}/mobile/delivery/${delivery.id}/index.jhtml">主营：
											[#list delivery.tenant.productCategoryTenants as productCategoryTenant]
												[#if productCategoryTenant_index<=2]
												 	${productCategoryTenant.name}
												 	[#if productCategoryTenant_has_next]/[/#if]
											 	[/#if]
											[/#list]
											</a> 
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
												--
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
[#include "/mobile/include/area_select.ftl" /]	
[/@area_current]
<div class="m_Areas_bg"></div>
</body>
</html>
