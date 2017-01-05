<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.siteClose.title")}[#if systemShowPowered] - Powered By rsico[/#if]</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/b2c/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/b2c/css/b2c-common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/b2c/css/error.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/b2c/css/font.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/b2c-common.js"></script>
</head>
<body>
  [#include "/b2c/include/header.ftl" /][#include "/b2c/include/navigation.ftl" /]
	<div class="desktop">
	<div class="container error">
		<div class="span24">
			<div class="main">
				${setting.siteCloseMessage}
			</div>
		</div>
	</div>
	</div>
	[#include "/b2c/include/footer.ftl" /]
</body>
</html>