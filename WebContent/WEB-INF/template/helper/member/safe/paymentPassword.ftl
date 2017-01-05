<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${message("shop.myAccount.title")}</title>
	<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" href="${base}/resources/helper/css/product.css">
	<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="${base}/resources/common/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
	<script type="text/javascript">
	$().ready(function() {

		var $inputForm = $("#inputForm");

		[@flash_message /]
	// 表单验证
	$inputForm.validate({
		rules: {
			currentPassword: {
				required: true
			},
			password: {
				required: true,
				pattern: /^[^\s&\"<>]+$/,
				minlength: ${setting.passwordMinLength},
				maxlength: ${setting.passwordMaxLength}
			},
			rePassword: {
				required: true,
				equalTo: "#password"
			}
		},
		messages: {
			password: {
				pattern: "${message("box.validate.illegal")}"
			}
		},
		submitHandler:function(form){
			$.ajax({
				url:"${base}/common/public_key.jhtml",
				dataType:"json",
				type:"post",
				success:function(data){
					var rsaKey = new RSAKey();
					rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
					var currentPassword = hex2b64(rsaKey.encrypt($("#currentPassword").val()));
					var password = hex2b64(rsaKey.encrypt($("#password").val()));

					$("#currentPassword").val(currentPassword);
					$("#password").val(password);
					$("#rePassword").val(password);
					form.submit();
				}
			});
		}
	});
});
</script>
</head>
<body>

[#include "/helper/include/header.ftl" /]
		[#include "/helper/member/include/navigation.ftl" /]
		<div class="desktop">
			<div class="container bg_fff">

				[#include "/helper/member/include/border.ftl" /]

				[#include "/helper/member/include/menu.ftl" /]

				<div class="wrapper" id="wrapper">

		<div class="mainbox member" style="position:static;">
				<div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
					<div class="js-app-header title-wrap" id="app_0000000844">
						<img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage.png"/>
						<dl class="app-info">
							<dt class="app-title" id="app_name">修改支付密码</dt>
							<dd class="app-status" id="app_add_status">
							</dd>
							<dd class="app-intro" id="app_desc">请定时修改支付密码，保障账户资金安全。</dd>
						</dl>
					</div>
					<ul class="links" id="mod_menus">
						<li  ><a class="on" hideFocus="" >支付密码</a></li>
					</ul>

				</div>
				<div class="account-table1" id="account-table1">
					<form id="inputForm" action="paymentPassword.jhtml" method="post">
						<table class="input">
							<tr>
								<th>
									${message("box.password.currentPassword")}:
								</th>
								<td>
									<input type="password" id="currentPassword" name="currentPassword" class="text" maxlength="20" /> * 默认为登录密码
								</td>
							</tr>
							<tr>
								<th>
									${message("box.password.newPassword")}:
								</th>
								<td>
									<input type="password" id="password" name="password" class="text" maxlength="6" />
								</td>
							</tr>
							<tr>
								<th>
									${message("box.password.rePassword")}:
								</th>
								<td>
									<input type="password" id="rePassword" name="rePassword" class="text" maxlength="6" />
								</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="h-button button" value="${message("box.password.submit")}" />
									<input type="button" class="h-button button" value="${message("box.password.back")}" onclick="location.href='index.jhtml'" />
								</td>
							</tr>
						</table>
					</form>
				</div>
	</div>
</div>
			</div>
		</div>
		[#include "/helper/include/footer.ftl" /]
	</body>
</html>
