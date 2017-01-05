<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>修改密码</title>
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
	<link href="${base}/resources/store/css/style.css" type="text/css" rel="stylesheet"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">
		[#include "/store/member/include/header.ftl"]
		[#include "/store/member/include/menu.ftl"]
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<h1>
					个人中心
				</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
					<li class="active">个人中心</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">                                    
								<li class="pull-left header"><i class="fa fa-users"></i>修改密码</li>
							</ul>
							<div class="tab-content">
								<div class="account-table1" id="account-table1">
									<form class="form-horizontal" id="inputForm" action="password.jhtml" method="post" role="form">
										<div class="form-group">
											<label  class="col-sm-2 control-label">手机号:</label>
											<div class="col-sm-8">
												<input type="text" value="${mosaic(member.mobile, 3, '~~~')}" class="form-control" disabled="true">
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-2 control-label"><span class="red">*</span>图片验证码:</label>
											<div class="col-sm-7">
												<input type="text" id="img_captcha" class="form-control" maxlength="20">
											</div>
											<div class="col-sm-1">
												<span class="form-control" style="padding:0px;">
													<img src="/common/captcha.jhtml?captchaId=${captchaId}" id="img_captcha_photo" style="height:34px;width:100%;">
												</span>
											</div>
										</div>
										<div class="form-group">
											<label  class="col-sm-2 control-label"><span class="red">*</span>手机验证码:</label>
											<div class="col-sm-6">
												<input type="text" id="phone_captcha" name="password"  class="form-control">
											</div>
											<div class="col-sm-2">
												<input type="button" id="get_code_btn" class="btn btn-block btn-primary" value="获取验证码" onclick="get_code();">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>新密码:</label>
											<div class="col-sm-8">
												<input type="password" id="password" name="password" maxlength="20" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>确认新密码:</label>
											<div class="col-sm-8">
												<input type="password" id="rePassword" name="rePassword" maxlength="20" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<div class="col-sm-offset-2 col-sm-2">
												<button type="button" class="btn btn-block btn-primary" onclick="modify_pass_submit()">提交</button>
											</div>
											<div class="col-sm-offset-0 col-sm-2">
												<button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
											</div>
										</div>
									</form> 
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
			<!-- /.content -->
		</div>
		<!-- /.content-wrapper -->
		[#include "/store/member/include/footer.ftl"]
	</div>
	[#include "/store/member/include/bootstrap_js.ftl"]
	<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
	<script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>

	<script type="text/javascript">
		$().ready(function () {
			var $inputForm = $("#inputForm");
			//点击图片切换验证码
		    $("#img_captcha_photo").click(function () {
		        $("#img_captcha_photo").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");
		    });
            // 表单验证
            $inputForm.validate({
            	rules: {
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
            			required: "必填",
            			pattern: "${message("box.validate.illegal")}",
            			minlength: "密码长度必须大于6"
            		},
            		rePassword: {
            			required: "必填",
            			equalTo: "两次密码不一致"
            		}

            	},
            	submitHandler: function (form) {
            		$.ajax({
            			url: "${base}/common/public_key.jhtml",
            			dataType: "json",
            			type: "post",
            			success: function (data) {
            				var rsaKey = new RSAKey();
            				rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
            				var currentPassword = hex2b64(rsaKey.encrypt($("#currentPassword").val()));
            				var password = hex2b64(rsaKey.encrypt($("#password").val()));
            				var rePassword = hex2b64(rsaKey.encrypt($("#rePassword").val()));
            				$("#currentPassword").val(currentPassword);
            				$("#password").val(password);
            				$("#rePassword").val(rePassword);
            				form.submit();
            			}
            		});
            	}
            });
		});
		//60秒倒计时
		var count = 60, ii;
		function refreshTime() {
		    count = count - 1;
		    if (count == 0) {
		      $("#get_code_btn").val("获取验证码");
		      count = 60;
		      clearInterval(ii);
		      return false;
		    }
		    $("#get_code_btn").val(count + "秒后重新获取");
		}
		//获取验证码
		function get_code() {
		    if (count != 60) {
		      return;
		    }
		    if("${member.mobile}"==""){
		        $.message('warn', "请先绑定手机号");
		        return false;
		    } 
		    if($("#img_captcha").val().trim()==""){
		        $.message('warn', "验证码不能为空");
		        return false;
		    } 
		    $.ajax({
		      url: "${base}/store/member/safe/send_phone.jhtml",
		      data:{
		        username:"${member.mobile}",
		        captchaId:"${captchaId}",
		        captcha:$("#img_captcha").val().trim()
		      },
		      type: "POST",
		      dataType: "json",
		      cache: false,
		      success: function (message) {
		        $.message(message);
		        if(message.type=="success"){
		          ii = setInterval(refreshTime, 1 * 1000);
		          $("#get_code_btn").val(count + "秒后重新获取");
		        }else{
		          $("#img_captcha_photo").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");
		        }
		      }
		    });
		}
		//修改密码提交
		function modify_pass_submit(){
			if($("#phone_captcha").val().trim()==""){
		      	$.message('warn', "验证码不能为空");
		      	return false;
		    }
		    if ($("#password").val().trim() == "" || $("#password").val().trim() == null) {
		      $.message("warn", "请输入新密码");
		      return;
		    }
		    if ($("#rePassword").val().trim() == "" || $("#rePassword").val().trim() == null) {
		      $.message("warn", "请再次输入新密码");
		      return;
		    }

		    if ($("#password").val().trim() != $("#rePassword").val().trim() ) {
		      $.message("warn", "两次密码不一致，请重新确认！");
		      return;
		    } 
		    $.ajax({
		      url:"${base}/store/member/safe/check.jhtml",
		      type:"post",
		      data:{
		        username:"${member.mobile}",
		        securityCode:$("#phone_captcha").val().trim()
		      },
		      dataType:"json",
		      success:function(message){
		        if(message.type=="success"){
		          	$.ajax({
				      url: "/common/public_key.jhtml",
				      type: "POST",
				      data: {local: true},
				      dataType: "json",
				      cache: false,
				      success: function (data) {
				        var rsaKey = new RSAKey();
				        rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
				        var enPassword = hex2b64(rsaKey.encrypt($("#rePassword").val().trim()));
				        $.ajax({
				          url: "/store/member/safe/reset_login_password.jhtml",
				          type: "POST",
				          data: {
				            mobile: "${member.mobile}",
				            newpassword: enPassword,
				            securityCode:$("#phone_captcha").val().trim()
				          },
				          dataType: "json",
				          cache: false,
				          success: function (message) {
				            $.message(message.type, message.content);
				            if (message.type == 'success') {
				              location.href="${base}/store/member/safe/index.jhtml";
				            }
				          }
				        });
				      }
				    });
		        }else{
		        	$.message(message);
		        }
		      }
		    });
		}
	</script>
</body>
</html>
