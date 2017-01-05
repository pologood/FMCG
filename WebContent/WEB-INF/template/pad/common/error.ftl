<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}404</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />
<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $memberCenter=$("#memberCenter");
	//会员中心
	$memberCenter.on("tap",function(){
		location.href="${base}/pad/member/center.jhtml";return false;
	})
});	
</script>
</head>
<body>
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="${base}/pad" class="p_return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">404</div>
			<!-- <div class="p_editor">取消</div>  -->
			<!-- <div class="p_all">全选</div>  -->
		</div>
	</div>
	<article class="p_article p_orderlistc p_article404" id="p_contscrooler">
		<div class="bodycont_orders">
			<div class="p_success p_success404">
				<div class="p_404">
					<div class="p_404title">404</div>
					<div class="p_404text">亲，你访问的页面不存在！</div>
				</div>
				<p>
					<a href="${base}/pad"><span>返回首页</span></a>
					<a href="javascript:void();" id="memberCenter"><span>返回会员中心</span></a>
				</p>
			</div>
		</div>
	</article>
</section>
<div class="p_searchfixed">
	<div class="p_search_icon"></div>
	<input type="text" placeholder="请输入搜索内容"/>
	<div class="p_search_button">搜索</div>
</div>
<div class="p_windowbg_1"></div>
<div class="p_receiptbg"></div>
</body>
</html>
