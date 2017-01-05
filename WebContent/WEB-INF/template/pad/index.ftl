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
<script type="text/javascript" src="${base}/resources/pad/js/index.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $addCart=$("div[name='addCart']");
	var $goCart=$("#goCart");
	var $memberCenter=$("#memberCenter");
	var $cartItemCount=$("#cartItemCount");
	var $search = $("#search");
	//搜索 
	$search.on('tap',function(){
		if($("#keyword").val()==''){
			alert("请输入搜索关键词");
			return false;
		}
		 	location.href = "${base}/pad/product/search.jhtml?keyword="+$("#keyword").val();
		 });
	//加入购物车
	$addCart.on("tap",function(){
		var $this=$(this);
		var productId = $this.attr("productId");
		$.ajax({
			url:"${base}/pad/cart/add.jhtml",
			data:{id:productId,quantity:1},
			dataType:"json",
			type:"post",
			success:function(message){
				if(message.type=="success"){
					getCartCount();
				}else{
					alert(message.content);
					return false;
				}
			}
		});
	});
	//进入购物车
	$goCart.on("tap",function(){
		location.href="${base}/pad/cart/list.jhtml";
		return false;
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
	//进入会员中心
	$memberCenter.on("touchstart",function(){
		location.href="${base}/pad/member/center.jhtml";
		return false;
	});
});
</script>
</head>
<body>
<section class="p_section">
	<div class="p_uwindow">
		<div class="p_uwindowul">
			<img src="${(member.tenant.logo)!}" alt="店铺logo">
			<p><strong>[#if member.tenant??]${member.tenant.name}[/#if]</strong></p>
			<p>导购员：${member.name}</p>
			<div class="p_tipsclose p_uwindowclose"></div>
		</div>
	</div>
	[#include "/pad/include/index_navigation.ftl" /]
	<article class="p_article" id="p_contscrooler">
		<div class="bodycont">
			<div class="p_indexbanner">
				[@ad_position id = 57 count=5/]
				<div class="p_round">
				[@ad_position id=57 count=5]
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
				<div class="p_clear"></div>
			</div>
			<div class="p_minibanner">
				<ul>
					<li>[@ad_position id = 58 count=1 /]</li>
					<li>[@ad_position id = 59 count=1 /]</li>
					<li>[@ad_position id = 60 count=1 /]</li>
				</ul>
				<div class="p_clear"></div>
			</div>
			<div class="p_indexrecommend">
				<div class="p_recommend_top"><h1>店铺推荐</h1></div>
				<div class="p_recommend_list" id="pr_scroller">
					<ul>
						[@product_list tenantId=member.tenant.id tagIds=5]
						[#list products as product]
						   <li>
								<div class="p_recommendbody">
									<a href="${base}/pad/product/detail/${product.id}.jhtml">
										<img src="${product.thumbnail}"/>
										<h1>${currency(product.wholePrice,true)} <del>${currency(product.price,true)}</del></h1>
										<p>${product.name}</p>
									</a>
									<div class="p_recommendbtn"  name="addCart" productId="${product.id}">加入购物车</div>
									<div class="p_discount">
										<h1>${(product.wholePrice*10/product.price)?string("0.0")}<span>折</span></h1>
									</div>
								</div>
								<div class="p_clear"></div>
							</li>
						 [/#list]
						 [/@product_list]
					</ul>
					<div class="p_clear"></div>
				</div>
				<div class="p_recommend_list p_recommend_list_1" id="pr_scroller_1">
					<div class="p_recommend_listcn">
					<ul>
						[@product_list tenantId=member.tenant.id tagIds=5]
							[#list products as product]
								[#if product_index%2==0]
									 <li>
										<div class="p_recommendbody">
											<a href="${base}/pad/product/detail/${product.id}.jhtml">
												<img src="${product.thumbnail}"/>
												<h1>${currency(product.wholePrice,true)} <del>${currency(product.price,true)}</del></h1>
												<p>${product.name}</p>
											</a>
											<div class="p_recommendbtn"  name="addCart" productId="${product.id}">加入购物车</div>
											<div class="p_discount">
												<h1>${(product.wholePrice*10/product.price)?string("0.0")}<span>折</span></h1>
											</div>
										</div>
										<div class="p_clear"></div>
									</li>
								[/#if]
							[/#list]
						[/@product_list]
						<div class="p_clear"></div>
					</ul>
					<ul>
						[@product_list tenantId=member.tenant.id tagIds=5]
							[#list products as product]
								[#if product_index%2==1]
									 <li>
										<div class="p_recommendbody">
											<a href="${base}/pad/product/detail/${product.id}.jhtml">
												<img src="${product.thumbnail}"/>
												<h1>${currency(product.wholePrice,true)} <del>${currency(product.price,true)}</del></h1>
												<p>${product.name}</p>
											</a>
											<div class="p_recommendbtn"  name="addCart" productId="${product.id}">加入购物车</div>
											<div class="p_discount">
												<h1>${(product.wholePrice*10/product.price)?string("0.0")}<span>折</span></h1>
											</div>
										</div>
										<div class="p_clear"></div>
									</li>
								[/#if]
							[/#list]
						[/@product_list]
						<div class="p_clear"></div>
					</ul>
					</div>
					<div class="p_clear"></div>
				</div>
			</div>
		</div>
	</article>
	<footer class="p_footer">
		<div class="p_navigation">
			<ul>
				<li><a href="#" alt="历史浏览"><i class="iconfont">&#xe60d</i></a></li>
				<li><a href="javascript:;" alt="购物车" id="goCart"><i class="iconfont">&#xe6d1</i></a>
					<span class="p_cartcount" id="cartItemCount">[#if count??&&count>0]${count}[/#if]</span>
				</li>
				<li><a href="javascript:;" id="memberCenter" alt="会员中心"><i class="iconfont">&#x3432</i></a></li>
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
</body>
</html>
