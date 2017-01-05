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
$().ready(function(){
	var $orderList = $("#orderList");
	[#if (WEIXIN.OPENID)??]
		$orderList.on('tap',function(){
			history.back(-1);
		});
		//返回按钮事件
		 $("#backapp").on("tap",function(){
			history.back(-1);
		 });
	[#else]
		$orderList.on('tap',function(){
			location.href="vsstoo://appback/?backapp=true";
		});
		//返回按钮事件
		 $("#backapp").on("tap",function(){
			 location.href="vsstoo://appback/?backapp=true";
		 });
	[/#if]
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			 <div class="m_return" id="backapp"><a href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			 [#include "/mobile/include/top_search.ftl" /]
			 <div class="m_title" alt="选择日期">温馨提示</div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont">
			<div class="m_statetitle">
					<h1>${notifyMessage}</h1>
					<span><i class="iconfont">&#xe60c</i></span>
			</div>
			<div class="m_statebtn">
				<ul>
					<li id="orderList">返回</li>
				</ul>
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
