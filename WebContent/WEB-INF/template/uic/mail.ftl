<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${setting.siteName}</title>
<meta name="author" content="rzico Team" />
<meta name="copyright" content="rzico" />
</head>
<body>
	<p>${username}:</p>
	<p>您好，欢迎使用${setting.siteName}</p>
	<p>验证码:${safeKey.value}，请在软件验证码对话框中输入此验证码并完成。</p>
	<p>${setting.siteName}</p>
	<p>${.now?string("yyyy-MM-dd")}</p>
</body>
</html>