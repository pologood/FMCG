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
			$secondCategory.append("<li><a href='${base}/mobile/tenant/list/"+tenantCategoryId+".jhtml'>全部</a></li>");
			
			if (typeof(tenantCategoryMap[tenantCategoryId]) != "undefined") { 
				$.each(tenantCategoryMap[tenantCategoryId],function(i,tenantCategory){
					$secondCategory.append("<li><a href='${base}/mobile/tenant/list/"+tenantCategory.id+".jhtml'>"
							+tenantCategory.name+
							"</a></li>");
				});
			}  
			$("#secondCategory").iScroll('refresh');
		
		return false;
	});
	 
	 var $near = $("#near");
	 var $hot = $("#hot");
	 var $secondMenu = $("#secondMenu");
	 var $allOrderType =$("li[name='allOrderType']");
	 var $tenantForm = $("#tenantForm");
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
			 		if(list!=''){
			 			$secondMenu.append("<li name='all' areaId='"+areaId+"'><a href='javascript:void(0)'>全部</a></li>");
			 			for(var i=0;i<list.length;i++){
				 			tmpStr+="<li communityId='"+list[i].id+"' name='nearCommunity'><a href='javascript:void(0)'>"+list[i].name+"</a></li>";
				 		}
				 		$secondMenu.append(tmpStr);
				 		var $nearCommunity =$("li[name='nearCommunity']");
				 		$nearCommunity.on('tap',function(){
				 			$this = $(this);
				 			var communityId = $this.attr('communityId');
				 			$areaId.val(areaId);
				 			$communityId.val(communityId);
				 			$pageNumber.val(1);
				 			$tenantForm.submit();
				 		});
				 		var $all =$("li[name='all']");
				 		$all.on('tap',function(){
				 			$this = $(this);
				 			var areaId = $this.attr('areaId');
				 			$communityId.val('');
				 			$areaId.val(areaId);
				 			$pageNumber.val(1);
				 			$tenantForm.submit();
				 		});
			 		}
				}
			});
	 });
 
	//排序类型
	$allOrderType.on('tap',function(){
		$this = $(this);
		$("#orderType").val($this.attr('val'));
		$areaId.val('');
		$pageNumber.val(1);
		$tenantForm.submit();
		return false;
	});
	 
	 $near.on('tap',function(){
		 $secondMenu.empty();
		 $secondMenu.append("<li name='distatce' distance='1000'><a href='javascript:void(0);'>1000m</a></li><li name='distatce' distance='2000'><a href='javascript:void(0);'>2000m</a></li><li name='distatce' distance='5000'><a href='javascript:void(0);'>5000m</a></li>");
		 var $allDistatce = $("li[name='distatce']");
		 $allDistatce.on('tap',function(){
			 var $this = $(this);
			 var distance = $this.attr('distance');
			 $distatce.val(distance);
			 $pageNumber.val(1);
			 $tenantForm.submit();
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
			 		if(list!=''){
			 			for(var i=0;i<list.length;i++){
				 			tmpStr+="<li communityId='"+list[i].id+"' name='hotCommunity'><a href='javascript:void(0)'>"+list[i].name+"</a></li>";
				 		}
				 		$secondMenu.append(tmpStr);
				 		var $hotCommunity =$("li[name='hotCommunity']");
				 		$hotCommunity.on('tap',function(){
				 			$this = $(this);
				 			var communityId = $this.attr('communityId');
				 			$communityId.val(communityId);
				 			$pageNumber.val(1);
				 			$tenantForm.submit();
				 		});
			 		}
				}
			});
	 });
	 $MyCare.on('tap',function(){
		 var memberId = $memberId.val();
		 if(memberId==""){
			 mtips("请先登录才能搜索你所关注的商家!");
			 if($tenantCategoryId.val()==""){
				 location.href = "vsstoo://login/?redirectURL=mobile/tenant/list.jhtml";
			 }else{
				 location.href = "vsstoo://login/?redirectURL=mobile/tenant/list/"+$tenantCategoryId.val()+".jhtml";
			 }
		 }else{
			 $tenantForm.submit();
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
	 
	 setTimeout(function(){$(".m_down").trigger("tap");},500);     
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
			url="${base}/mobile/tenant/addMore.jhtml"
		}else{
			url="${base}/mobile/tenant/addMore/"+tenantCategoryId+".jhtml";
		}
		var el, li, i;
		el = document.getElementById('tenantList');
		pageNum=pageNum+1;
		$.ajax({
			url:url,
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum,x:$("#location_x").val(),y:$("#location_y").val()},
			success:function(result){
				 if(result.length>0){
						 for(var i=0;i<result.length;i++){
					 		var tmpStr="";
						 	 li = document.createElement('li');
						 	 	tmpStr+="<div class=a><div class=m_productk><a href=${base}/mobile/tenant/"
						 	 			+result[i].id
						 	 			+"/index.jhtml><img src="
						 	 			+result[i].thumbnail
						 	 			+" width=80px; height=80px;/></a><h2><a href=${base}/mobile/tenant/"
						 	 			+result[i].id
						 	 			+"/index.jhtml>"
						 	 			+result[i].name
						 	 			+"</a></h1><span class=score"
						 	 			+ parseFloat(result[i].score)*2
						 	 			+"></span><p class=padding5>主营：";
						 	 			if(result[i].productCategoryTenants.length>0){
											 for(var j=0;j<result[i].productCategoryTenants.length;j++){
											 	if(j<=2){
												 tmpStr+=result[i].productCategoryTenants[j].name+"/";
											 	}
											 }
										 }
										tmpStr+="</p><p class='toShowMap address'";
											if(result[i].defaultDeliveryCenter.location!=null&&result[i].defaultDeliveryCenter.location!=""){
												tmpStr+=" defaultDeliveryCenterId="+result[i].defaultDeliveryCenter.id ;
											}
										tmpStr+="><i class='iconfont'>&#xe602</i>"+result[i].address+"</p><div class=m_shopdistance>";
										if(result[i].distatce==null){
											tmpStr+="暂无信息";
										}else{
											tmpStr+=currency(result[i].distatce,false,false)+"m";
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
			<!--
			<li>[#if orderType??]${message("Product.OrderType." + orderType)}[#else]默认排序[/#if]<span></span></li>
			-->
		</ul>
	</div>
	<div class="m_area">
		<div class="m_areal" id="m_areascrooler_1">
			<ul>
				<li id="near" class="m_down">附近</li>
				<li id="MyCare">关注的商家</li>
				<li id="hot">热门商圈</li>
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
				[@tenant_category_root_list count = 10]
				[#list tenantCategories as rootTenantCategory]
				 	[#if rootTenantCategory_index==0]
					 	<li name="root" class="m_down" tenantCategoryName="${rootTenantCategory.name}" tenantCategoryId="${rootTenantCategory.id}">
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
	<div class="m_area arealpx">
		<div class="m_arear" id="m_areascrooler_5">
			<ul>
				[#list orderTypes as ot]
					<li name="allOrderType" val="${ot}">
			    		<a href="javascript:void(0);">${message("Product.OrderType." + ot)}</a>
					</li>
				[/#list]
			</ul>
		</div>
	</div>
	<article class="m_article m_article_list" id="m_scrooler_0">
	<div class="m_bodycont_1">
		<form id="tenantForm" action="${base}/mobile/tenant/list[#if tenantCategory!=null]/${tenantCategory.id}.jhtml[#else].jhtml[/#if]" method="get">
			<input type="hidden" id="areaId" name="areaId" value="[#if area??]${area.id}[/#if]"/>
			<input type="hidden" id="communityId" name="communityId" value="${communityId}" />
			<input type="hidden" id="distatce" name="distatce" value="${distatce}" />
			<input type="hidden" id="tenantCategoryId" name="tenantCategoryId" value="${tenantCategoryId}" />
			<input type="hidden" id="location_x" name="x" value="${location.x}" />
			<input type="hidden" id="location_y" name="y" value="${location.y}" />
			<input type="hidden" id="memberId" name="memberId" value="[#if member??]${member.id}[/#if]" />
			<div class="m_bodycont_2">
				<div class="m_listcont m_shoplist">
					<ul id="tenantList">
							[#list page.content as tenant]
							<li>
								<div class="a">
									<div class="m_productk">
										<a href="${base}/mobile/tenant/${tenant.id}/index.jhtml"><img src="${tenant.thumbnail}" width="80px;"height="80px;"/></a>
										<h2><a href="${base}/mobile/tenant/${tenant.id}/index.jhtml">${tenant.name}</a></h1>
										<span class="score${(tenant.score * 2)?string("0")}"></span>
										<p class="padding5">

											<a href="${base}/mobile/tenant/${tenant.id}/index.jhtml">主营：
											[#list tenant.productCategoryTenants as productCategoryTenant]
												[#if productCategoryTenant_index<=2]
												 	${productCategoryTenant.name}
												 	[#if productCategoryTenant_has_next]/[/#if]
											 	[/#if]
											[/#list] 
											</a>
										</p>	
										<p class="toShowMap address" [#if tenant.defaultDeliveryCenter.location??] defaultDeliveryCenterId="${tenant.defaultDeliveryCenter.id}" [/#if] ><i class="iconfont">&#xe602</i>${tenant.address}</p>
										<div class="m_shopdistance">[#if tenant.distatce??]${currency(tenant.distatce,false,false)}m[#else]暂无信息[/#if]</div>
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
