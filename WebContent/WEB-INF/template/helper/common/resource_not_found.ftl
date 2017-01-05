<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.resourceNotFound.title")}[#if systemShowPowered][/#if]</title>
<link href="${base}/resources/b2b/css/v2.0/common.css" type="text/css" rel="stylesheet" />
<link href="${base}/resources/b2b/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${base}/resources/b2b/css/b2b-common.css">
<link href="${base}/resources/b2b/css/error.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/b2b/css/font.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
</head>
<body>
	<!-- head页头公共部分start -->
  [#include "/b2b/include/navbar.ftl" /]
	<div class="center logo_div"><img src="${base}/resources/b2b/images/v2.0/login_logo.gif" /></div>
	<!-- head页头公共部分end -->
	<div class="desktop">
	<div class="container error">
		<div class="span24">
			<div class="main">
				<dl>
					${message("shop.resourceNotFound.message")}
					<dd>
						<a href="javascript:;" onclick="window.history.back(); return false;">&gt;&gt; ${message("shop.resourceNotFound.back")}</a>
					</dd>
					<dd>
						<a href="${base}/">&gt;&gt; ${message("shop.resourceNotFound.home")}</a>
					</dd>
				</dl>
			</div>
		</div>
	</div>
	</div>
	[#include "/b2b/include/footer.ftl" /]
</body>
</html>