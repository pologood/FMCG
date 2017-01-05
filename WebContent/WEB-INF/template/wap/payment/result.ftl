<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>温馨提醒</title>
<link rel="stylesheet" href="${base}/resources/wap/css/library.css" />
<link rel="stylesheet" href="${base}/resources/wap/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/wap/css/common.css" />
<script type="text/javascript" src="${base}/resources/wap/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/wap/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/wap/js/common.js"></script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_title" alt="温馨提醒">温馨提醒</div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont">
			[#if payment.status == "success"]
				<div class="scheduled_success">
					<i class="iconfont">&#xe60a</i>
					<h1>恭喜您，支付成功！</h1>
				</div>
			[#elseif payment.status == "wait"]
				<div class="scheduled_success scheduled_sb">
					<i class="iconfont">&#xe60b</i>
					支付已取消。。
				</div>
			[#elseif payment.status == "failure"]
				<div class="scheduled_success scheduled_sb">
					<i class="iconfont">&#xe60b</i>
					您的款项支付失败，请您及时联系我们进行处理！
				</div>
			[/#if]
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
