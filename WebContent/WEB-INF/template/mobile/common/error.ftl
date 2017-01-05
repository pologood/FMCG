<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>错误提示页</title>
<link rel="stylesheet" href="${base}/resources/common/css/error.css" />
</head>
<body>
<div class="errorbody">
	<div class="errorbodyul">
		<h1>错误啦!</h1>
		<p>您所访问的页面可能已经删除、更名或暂时不可用
	请确保您访问的网站地址拼写和格式正确无误</p>
		<div class="errorbodybtn">
			<a href="javascript:void();" class="error_return" onclick="history.go(-1); return false;">返回上一页</a>
			<a href="javascript:void();" class="error_home" onclick="location.href='${base}/mobile'; return false;">返回首页</a>
		</div>
		<span></span>
	</div>
	<div class="errorcr">Copyright 2013-2020 ${setting.siteName} 版权所有 <a target="_blank" href="${setting.siteUrl}">${setting.certtext}</a></div>
</div>

</body>
</html>