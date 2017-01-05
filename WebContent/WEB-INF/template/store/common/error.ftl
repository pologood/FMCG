<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.error.title")}[#if systemShowPowered][/#if]</title> 
<link href="${base}/resources/store/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
</head>
<body>
	<!-- head页头公共部分start -->
  [#include "/store/include/navigation.ftl" /]
	<div class="center logo_div"><img src="${base}/resources/store/images/v2.0/login_logo.gif" /></div>
	<!-- head页头公共部分end -->
	<div class="desktop">
	<div class="container error">
		<div class="span24">
			<div class="main">
				<dl>
					<dt>${message("shop.error.message")}</dt>
					[#if message??]
						<dd>${content}</dd>
					[/#if]
					[#if constraintViolations?has_content]
						[#list constraintViolations as constraintViolation]
							<dd>[${constraintViolation.propertyPath}] ${constraintViolation.message}</dd>
						[/#list]
					[/#if]
					<dd>
						<a href="javascript:;" onclick="window.history.back(); return false;">${message("shop.error.back")}</a>
					</dd>
					<dd>
						<a href="${base}/">&gt;&gt; ${message("shop.error.home")}</a>
					</dd>
				</dl>
			</div>
		</div>
	</div>
	</div>
	[#include "/store/include/footer.ftl" /]
</body>
</html>