<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.password.mailTitle")}[#if systemShowPowered][/#if]</title>
<link rel="shortcut icon" href="${base}/favicon.ico" />
</head>
<body>
	<p>${username}:</p>
	<p> 您好，欢迎您加入店家助手，此邮件为系统发送邮件，请勿回复。</p>
	<p> 您正在使用店家助手账号绑定功能，请点击以下立即缴活绑定您的邮箱[#if safeKey.expire??](${message("shop.password.expire")}: ${safeKey.expire?string("yyyy-MM-dd HH:mm")})[/#if]</p>
	<p>
		<a href="${setting.siteUrl}/bind_email.jhtml?username=${username}&security=${safeKey.value}" target="_blank">${setting.siteUrl}/bind_email.jhtml?username=${username}&security=${safeKey.value}</a>
	</p>
	<p>店家助手</p>
	<p>${.now?string("yyyy-MM-dd")}</p>
</body>
</html>