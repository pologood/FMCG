<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${message("shop.myAccount.title")}</title>
	<link rel="stylesheet" href="${base}/resources/store/css/product.css">
	<link href="${base}/resources/store/css/common.css" rel="stylesheet" type="text/css" />
	<link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
	<script type="text/javascript">
	$().ready(function() {

		var $passwordForm = $("#passwordForm");
		var $username = $("#username");
		var $submit = $(":submit");
		var $getCode=$("#getCode");
		var $mobile=$("#mobile");
		var $email=$("#email");
		var $span_1=$("#span_1");
		var count =60;

		//获取验证码
		$getCode.on('click',function(e){
			if($username.val()==''){
				alert("请先填写用户名"); return false;
			}

		//获取手机验证码
		if($mobile.attr("checked")=="checked"){
			$.ajax({
				url :"${base}/box/password/send.jhtml",
				data:{username:$username.val(),type:'mobile'},
				dataType:"json",
				type:"post",
				success:function(data){
					if(data.type=='success'){
						$getCode.attr('style','display:none;');
						$span_1.attr('style','display:block;');
						ii=setInterval(refreshTime,1*1000);
						return ;
					}else{
						alert(data.content);
					}
				}
			});
			return false;

		}
		//获取邮箱验证码
		if($email.attr("checked")=="checked"){
			$.ajax({
				url :"${base}/box/password/send.jhtml",
				data:{username:$username.val(),type:"email"},
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.type=='success'){
						$getCode.attr('style','display:none;');
						$span_1.attr('style','display:block;');
						ii=setInterval(refreshTime,1*1000);
						return ;
					}else{
						alert("验证码发送失败");
					}
				}
			});
			return false;
		}
		function refreshTime(){
			count=count-1;
			if(count==0){
				count=60;
				$getCode.attr('style','display:block');
				$span_1.attr('style','display:none');
				clearInterval(ii);
				return false;
			}
			$("#span_1").html(count+"秒后重新获取");
		}
	});

	// 表单验证
	$passwordForm.validate({
		rules: {
			username: "required",
			newpassword:{
				required: true
			},
			confirmPwd: {
				equalTo: "#newpassword"
			}
		},
		submitHandler: function(form) {
			form.submit();
		}
	});
	$("#confirm").click(function(){
		$passwordForm.submit();
	});
});
</script>
</head>

<body>

[#include "/store/include/header.ftl" /]
		[#include "/store/member/include/navigation.ftl" /]
		<div class="desktop">
			<div class="container bg_fff">

				[#include "/store/member/include/border.ftl" /]

				[#include "/store/member/include/menu.ftl" /]

				<div class="wrapper" id="wrapper">

		<div class="mainbox member" style="position:static;">
				<div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
					<div class="js-app-header title-wrap" id="app_0000000844">
						<img class="js-app_logo app-img" src="${base}/resources/store/images/message-manage.png"/>
						<dl class="app-info">
							<dt class="app-title" id="app_name">重置支付密码</dt>
							<dd class="app-status" id="app_add_status">
							</dd>
							<dd class="app-intro" id="app_desc">忘记支付密码了，可以通过手机找回。</dd>
						</dl>
					</div>
					<ul class="links" id="mod_menus">
						<li  ><a class="on" hideFocus="" href="javascript:;">重置支付密码</a></li>
					</ul>

				</div>
				<div class="account-table1" id="account-table1">
					<div class="wrap" style="margin-top:5px;">
						<div class="main">
							<form id="passwordForm" action="resetSave.jhtml" method="post">
								<input type="hidden" name="username" id="username" value="${member.username}" />
								<table class="input">
									<tr>
										<th>
											<span class="requiredField">*</span>用户名:
										</th>
										<td>
											${member.username}
										</td>
									</tr>

									<tr>
										<th>
											<span class="requiredField">*</span>新密码:
										</th>
										<td>
											<input type="password" id="newpassword" name="newpassword" class="text" maxlength="200" />
										</td>
									</tr>
									<tr>
										<th>
											<span class="requiredField">*</span>确认新密码:
										</th>
										<td>
											<input type="password" id="confirmPwd" name="confirmPwd" class="text" maxlength="200" />
										</td>
									</tr>
									<tr>
										<th>
											&nbsp;
										</th>
										<td>
											<input class="button" type="submit" id="confirm" value="${message("shop.password.submit")}" />
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
	</div>
</div>
			</div>
		</div>
		[#include "/store/include/footer.ftl" /]
	</body>
</html>


