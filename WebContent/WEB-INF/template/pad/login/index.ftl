<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />

<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/login.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/md5.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $close=$(".p_rclose");
	var $submit = $("#submit");
	var $username = $("#username");
	var $pwd = $("#pwd");
	var $g_code_register=$("#g_code_register");
	var $resetPwd_mobile=$("#resetPwd_mobile");
	var $g_code_pwd=$("#g_code_pwd");
	var $register_mobile = $("#register_mobile");
	var $register_submit = $('#register_submit');
	var $resetPwd_submit = $('#resetPwd_submit');
	var $securityCode_register = $('#securityCode_register');
	var $securityCode_pwd = $('#securityCode_pwd');
	var $password = $('#password');
	var $repassword = $('#repassword');
	var $p_tips = $('.p_tips p');
	var timeout_register,timeout_pwd;
	var time_register=60,time_pwd=60;
	var securityCode_register="";
	//关闭按钮
	$close.on("tap",function(e){
		clearInterval(timeout_pwd);
		clearInterval(timeout_register);
	});
	//获取验证码-注册
	$g_code_register.on('tap',function(){
		if($register_mobile.val()==''){
			$g_code_register.addClass('p_down');
			ptips("请先填写手机号");
			return false;
		}
		if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($register_mobile.val()))){
			$g_code_register.addClass('p_down');
			ptips("手机号码不符合"); 
			return false;
		}
		$.ajax({
			url:"${base}/register/check_username.jhtml",
			type:"get",
			data:{username:$register_mobile.val()},
			dataType:"json",
			success:function(result){
				if(result==true){
					$.ajax({
						url:"${base}/register/send_mobile.jhtml",
						type:"post",
						data:{mobile:$register_mobile.val() },
						dataType:"json",
						success:function(message){
							if(message.type=="success"){
								securityCode_register=message.content;
								timeout_register=setInterval(refreshTime_register,1000);
							}else{
								$g_code_register.addClass('p_down');
							}
							return false;
						}
					});
				}else{
					ptips("手机已经存在!");
					$g_code_register.addClass('p_down');
				}
				return false;
			}
		});
	});
	//获取验证码-找回密码
	$g_code_pwd.on('tap',function(){
		if($resetPwd_mobile.val()==''){
			$g_code_pwd.addClass('p_down');
			ptips("请先填写手机号");
			return false;
		}
		if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($resetPwd_mobile.val()))){
			$g_code_pwd.addClass('p_down');
			ptips("手机号码不符合"); 
			return false;
		}
		$.ajax({
			url:"${base}/pad/password/check_mobile.jhtml",
			type:"post",
			data:{mobile:$resetPwd_mobile.val()},
			dataType:"json",
			success:function(result){
				if(result==true){
					$.ajax({
						url:"${base}/pad/password/send_mobile.jhtml",
						type:"post",
						data:{mobile:$resetPwd_mobile.val() },
						dataType:"json",
						success:function(message){
							if(message.type=="success"){
								securityCode_pwd=message.content;
								timeout_pwd=setInterval(refreshTime_pwd,1000);
							}else{
								$g_code_pwd.addClass('p_down');
							}
							return false;
						}
					});
				}else{
					ptips("手机已经存在!");
					$g_code_pwd.addClass('p_down');
				}
				return false;
			}
		});
	});
	//倒计时-注册
	function refreshTime_register(){
		if(time_register>0){
			time_register=time_register-1;
			$g_code_register.text(time_register+"''");
		}else{
			$g_code_register.addClass('p_down');
			$g_code_register.text("获取验证码");
			clearInterval(timeout_register);
		}
	}
	//倒计时-找回密码
	function refreshTime_pwd(){
		if(time_pwd>0){
			time_pwd=time_pwd-1;
			$g_code_pwd.text(time_pwd+"''");
		}else{
			$g_code_pwd.addClass('p_down');
			$g_code_pwd.text("获取验证码");
			clearInterval(timeout_pwd);
		}
	}
	//注册提交
	$register_submit.on('tap',function(){
		if(securityCode_register!=$securityCode_register.val()){
			ptips("验证码错误");return false;
		}
		if($password.val()!=$repassword.val()){
			ptips("两次密码输入不一致");return false;
		}
		$.ajax({
			url:"${base}/register/simple_submit.jhtml",
			type:"post",
			data:{mobile:$register_mobile.val(),securityCode:securityCode_register,password:$password.val()},
			dataType:"json",
			success:function(message){
				$('.p_tips p').text(message.content);
				setTimeout(function(){location.href="${base}/pad";},3000);
				return false;
			}
		});
	});
	//找回密码提交
	$resetPwd_submit.on('tap',function(){
		if(securityCode_pwd!=$securityCode_pwd.val()){
			ptips("验证码错误");return false;
		}
		$.ajax({
			url:"${base}/pad/password/resetsave.jhtml",
			type:"post",
			data:{mobile:$resetPwd_mobile.val(),securityCode:securityCode_pwd},
			dataType:"json",
			success:function(message){
				ptips(message.content);
				setTimeout(function(){location.href="${base}/pad";},2000);
				return false;
			}
		});
	});
	//登入 提交
	$submit.on('tap',function(){
		if($username.val()==''||$pwd.val()==''){
			ptips('您输入的账号或密码错误，请重新输入！');
		}
		$.ajax({
			url: "${base}/pad/login/get_auth.jhtml",
			type: "GET",
			dataType: "json",
			success:function(data){
				if(data!=""){
					var password=hex_md5(hex_md5($pwd.val())+data+"vst@2014-2020$$");
					$.ajax({
						url :"${base}/pad/login/submit.jhtml",
						type:'post',
						dataType:"json",
						data:{
							username:$username.val(),
							password:password
						},
						success:function(message){
							if(message.type=='success'){
								location.href="${base}/pad/index.jhtml";
							}else{
								ptips(message.content);
							}
							return false;
						}
					});
				}else{
					ptips("请求校验码失败！");
					return false;
				}
			}
		});	
		return false;
	});
});
</script>
</head>
<body class="p_loginbg">
<div class="p_login">
	<div class="p_loginlg"></div>
	<div class="p_loginenter">
		<div class="p_loginttag"></div>
		<div class="p_logincont">
			<h1>会员登录</h1>
			<div class="text">
				<p><i class="iconfont">&#x3432</i><input id="username" type="text" placeholder="请输入账户名称"/></p>
			</div>
			<div class="text">
				<p><i class="iconfont">&#xe620</i><input id="pwd" type="password" placeholder="请输入账户密码"/></p>
			</div>
			<div class="text">
				<a href="javascript:;" class="p_loginbtn" id="submit">登 录</a>
			</div>
			<div class="text">
				<ul>
					<li class="p_nopw">忘记密码？</li>
					<li class="p_userrg">用户注册</li>
				</ul>
			</div>
		</div>
		<div class="p_loginbtag"></div>
	</div>
	<div class="p_copyright">
		Copyright 2013-2020 ${setting.siteName} 版权所有 <br/>${setting.certtext}
	</div>
</div>
<div class="p_nopw_wind">
	<h1>发送密码到手机</h1>
	<div class="text">
		<p>
			<input type="text" placeholder="请输入手机号码" id="resetPwd_mobile" />
			<span class="p_captcha p_down" id="g_code_pwd">获取验证码</span>
		</p>
	</div>
	<div class="text p_incaptcha">
		<p><input type="text" placeholder="请输入手机验证码" id="securityCode_pwd"/></p>
	</div>
	<div class="text">
		<a href="#" class="p_nopwbtn" id="resetPwd_submit">确 定</a>
	</div>
	<div class="p_loginclose p_nclose"></div>
</div>
<div class="p_tips"><p></p><div class="p_tipsclose"></div></div>
<div class="p_windowbg"></div>
</body>
</html>
