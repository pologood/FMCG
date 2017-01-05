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
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]	
			<div class="m_title" alt="选择日期">商家信息</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_writh" id="m_scrooler">
		<div class="m_bodycont_1">
			<div class="m_specification m_nob">
				<img src="${tenant.thumbnail}" width="300px;" height="300px;">
				${tenant.introduction}
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
