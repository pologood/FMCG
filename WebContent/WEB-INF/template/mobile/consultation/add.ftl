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
<script type="text/javascript" src="${base}/resources/mobile/js/detail.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $content = $("#content");
	var $productId = $("#productId");
	var $submit = $("#submit");
	var $reset = $("#reset");
	var $productDetail = $("#productDetail");
	$submit.on('tap',function(){
		var content = $content.val().trim();
		if(content==''){
			mtips("请输入咨询内容");
		}else{
			 $.ajax({
					url :'${base}/mobile/consultation/save.jhtml',
					data:{id:$productId.val(),content:$content.val(),type:'product'},
					type:'post',
					dataType:'json',
					success:function(data){
						if(data.type='success'){
							mtips(data.content);
							setTimeout(function(){location.href="${base}/mobile/product/content/"+$productId.val()+".jhtml";}, 1 * 1000);
							return false;
						}else{
							mtips(data.content);
							return false;
						}
					}
				});
			 }
		
	});
	
	$reset.on('tap',function(){
		$content.val("");
	});
	
	$productDetail.on('tap',function(){
		var $this = $(this);
		var productId = $this.attr('productId');
		location.href="${base}/mobile/product/content/"+productId+".jhtml";
		return false;
	});
	
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:void(0);" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">我要咨询</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_writh" id="m_scrooler">
		<form id="consultationForm" action="${base}/mobile/consultation/save.jhtml" method="post">
		<input type="hidden" id="productId" value="${product.id}">
		<div class="m_bodycont">
			<div class="p_zxdder">
				<ul class="bodycont_list">
					<li>
						<a href="javascript:;" id="productDetail" productId="${product.id}">
							<img src="${product.thumbnail}" width="80px;" height="80px;">
							<p>${product.name}</p>
							<p><span>${currency(product.wholePrice,true)}</span><span>${currency(product.price,true)}</span></p>
						</a>
					</li>
					<li>
						<textarea type="text" placeholder="输入咨询内容" id="content" name="content"></textarea>
					</li>
				</ul>
			</div>
			<div class="m_detailbtn m_detailbtn_pj">
			<ul>
				<li><a href="javascript:void(0)"><span id="submit">提交</span></a></li>
				<li><a href="javascript:void(0)"><span id="reset">重置</span></a></li>
			</ul>
		</div>
		</div>
		</form>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
