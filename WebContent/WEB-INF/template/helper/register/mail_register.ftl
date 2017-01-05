<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.register.mailTitle")}[#if systemShowPowered][/#if]</title>
<link rel="shortcut icon" href="${base}/favicon.ico" />
</head>
<body>
	<p>${username}:</p>
	<p> 您好，欢迎您加入店家助手，此邮件为系统发送邮件，请勿回复。</p>
	<p> 您正在使用店家助手账号邮箱注册功能，请在注册页面输入以下验证码完成注册[#if safeKey.expire??](${message("shop.password.expire")}: ${safeKey.expire?string("yyyy-MM-dd HH:mm")})[/#if]</p>
	<p>
		验证码:${safeKey.value}
	</p>
	<p>店家助手</p>
	<p>${.now?string("yyyy-MM-dd")}</p>
</body>
</html>