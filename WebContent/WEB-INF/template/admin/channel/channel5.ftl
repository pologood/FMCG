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
<script type="text/javascript" src="${base}/resources/weixin/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/weixin/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/weixin/js/common.js"></script>[#include "/weixin/include/header.ftl" /]

</head>
<script type="text/javascript">
$().ready(function(){
	$("#productCategoryTenantId").find("a").on("tap",function(){
		location.href="${base}/weixin/product_channel/${productChannel.id}/index.jhtml?productCategoryId="+$(this).attr("productCategoryId"); return false;
	});
	$("#tenantList").find("a").on("tap",function(){
		location.href="${base}/weixin/delivery/"+$(this).attr("tenantId")+"/index.jhtml";return false;
	});
});
// 2015-1-11 拖动加载

var myScroll,pullUpEl, pullUpOffset,generatedCount = 0;
var pageNum = ${page.pageable.pageNumber};
function pullUpAction () {
	setTimeout(function () {	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
		if(pageNum>=(${page.total/page.pageable.pageSize})+1){
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
			return false;
		}
		var el, li, i;
		el = document.getElementById('productList');
		pageNum=pageNum+1;
		$.ajax({
			url:"${base}/weixin/product_channel/${productChannel.id}/loadmore.jhtml?productCategoryId=${productCategoryId}",
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum},
			success:function(result){
				if(result.content.length>0){
					for(var i=0;i<result.content.length;i++){
						li = document.createElement('li');
						li.innerHTML ="<a href=${base}/weixin/product/content/"+result.content[i].id+".jhtml><div class='m_productk'><img src="+result.content[i].thumbnail+"><p><span>"
						+currency(result.content[i].price,true)
						+"</span><del>"
						+currency(result.content[i].marketPrice,true)
						+"</del></p><p>"
						+result.content[i].fullName
						+"</p></div></a>";
						el.appendChild(li, el.childNodes[0]);
					}
					$('#m_scrooler_0').iScroll('refresh');// 请记住，当刷新内容加载（即：在阿贾克斯完成
				}else{
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
					return false;
				}
			}
		});
	}, 1000);	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
}

function loaded() {
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	
	myScroll = new iScroll('m_scrooler_0', {
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
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:void(0);" alt="返回"><div class="p_datag">返回</div></a></div>
			<div class="m_title" alt="选择日期">${productChannel.name}</div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler_0" >
		<div class="m_bodycont_1">
			<div class="m_roombanner" style="overflow: hidden; visibility: visible; list-style: none; position: relative;">
				<ul id="m_roomslider" style="position: relative; overflow: hidden; -webkit-transition: left 600ms ease; transition: left 600ms ease; width: 960px; left: -320px;">
					[#if adPosition??]
						[@ad_position id=adPosition.id /]
					[/#if]
				</ul>
				<div class="m_clear">
				</div>
			</div>
			<div class="m_h1"></div>
			<h1 class="m_shopt_top"><span></span>好店推荐</h1>
			<div class="m_shopt" id="m_shoptscroll" data--iscroll-="iscroll3" style="overflow: hidden;">
				<ul id="tenantList" style="width: 400px; -webkit-transition: -webkit-transform 0ms; transition: -webkit-transform 0ms; -webkit-transform-origin: 0px 0px; -webkit-transform: translate(0px, 0px) translateZ(0px);">
					[#list deliveris as delivery]
						<li><a tenantid="${delivery.id}" href="javascript:;"><img src="${delivery.tenant.logo}" width="75px;" height="75px;"><br>
						${delivery.tenant.name}</a></li>
					[/#list]
					<div class="m_clear">
					</div>
				</ul>
			</div>
			<div class="m_h1"></div>
			<div class="m_shopt m_shopt_1" id="m_shoptscroll_1">
				<ul id="productCategoryTenantId">
					<li  [#if productCategoryId==null||productCategoryId==''] class="down"[/#if]><a href="javascript:;" productCategoryId="">全部</a></li>
					[#list productCategorys as productCategory]
						<li [#if productCategoryId==productCategory.id]class="down"[/#if]> <a href="javascript:;" productCategoryId="${productCategory.id}">${productCategory.name}</a></li>
					[/#list]
					<div class="m_clear"></div>
				</ul>
			</div>
			<div class="m_h10"></div>
			<div class="m_listcont">
			[#if page.content??&&page.content?has_content]
				<ul id="productList">
					[#list page.content as product]
						<li>
							<a href="${base}/weixin/product/content/${product.id}.jhtml">
								<div class="m_productk">
									<img src="${product.thumbnail}">
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
			[#else]	
				<div id="pullUp">
					<span class="pullUpLabel">暂无信息。。</span>
				</div>
			[/#if]
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
