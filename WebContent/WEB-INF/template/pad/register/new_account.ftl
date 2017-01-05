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
<script type="text/javascript" src="${base}/resources/pad/js/regist.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $return_back = $("#return_back");
	var $goCart=$("#goCart");
	var $areaId=$("#areaId");
	var $memberCenter=$("#memberCenter");
	var $phone_code = $("#phone_code");
	var $phone_checkcode = $("#phone_checkcode");
	var $register_mobile = $("#register_mobile");
	var $phoneRegisterForm = $("#phoneRegisterForm");
	var $rePassword = $("#rePassword");
	var $phone_password = $("#phone_password");
	var securityCode_register ="";
	var timeout_register;
	var time_register=60;
	var $province = $("#province");
	var $p_province=$("#p_rescroller ul");
	var $city = $("#city");
	var $p_city=$("#p_rescroller_1 ul");
	var $district = $("#district");
	var $p_district=$("#p_rescroller_2 ul");
	var $phone_submit = $("#phone_submit");
	//邮箱
	var $register_email = $("#register_email");
	var $email_code = $("#email_code");
	var $email_checkcode = $("#email_checkcode");
	var $emailRegisterForm = $("#emailRegisterForm");
	var $email_rePassword = $("#email_rePassword");
	var $email_password = $("#email_password");
	var email_securityCode_register ="";
	var email_timeout_register;
	var email_time_register=60;
	var $province_email	 = $("#province_email");
	var $city_email = $("#city_email");
	var $email_areaId = $("#email_areaId");
	var $district_email = $("#district_email");
	var $email_submit = $("#email_submit");
	//用户名
	var $register_username = $("#register_username");
	var $captchaImage = $("#captchaImage");
	var $usernameRegisterForm = $("#usernameRegisterForm");
	var $username_password = $("#username_password");
	var $username_rePassword = $("#username_rePassword");
	var username_timeout_register;
	var username_time_register=60;
	var $province_username	 = $("#province_username");
	var $city_username = $("#city_username");
	var $username_areaId = $("#username_areaId");
	var $district_username = $("#district_username");
	var $username_submit = $("#username_submit");
	
	//验证码-用户名
	$captchaImage.on("tap",function(){
		$captchaImage.attr("src", "${uc_captcha}?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
	});
	
	//省份-用户名
	$province_username.on("tap",function(){
		$.ajax({
			url :"${base}/common/area.jhtml?parentId=0",
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_province.empty();
        		for(var key in data){
        			$p_province.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_province.iScroll('refresh');
				$p_province.find("li").on('tap', function () {
					var $this = $(this);
					$province_username.text($this.text());
				    $province_username.attr("key",$this.attr("key"));
				    $city_username.attr("key","").text("选择城市");$district_username.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//城市-用户名
	$city_username.on("tap",function(){
		if($province_username.attr("key")==""){
			alert("请先选择省份");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$province_username.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_city.empty();
        		for(var key in data){
        			$p_city.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_city.iScroll('refresh');
				$p_city.find("li").on('tap', function () {
					var $this = $(this);
					$city_username.text($this.text());
				    $city_username.attr("key",$this.attr("key"));
				    $district_username.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//地区-邮箱
	$district_username.on("tap",function(){
		if($city_username.attr("key")==""){
			alert("请先选择城市");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$city_username.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_district.empty();
        		for(var key in data){
        			$p_district.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_district.iScroll('refresh');
				$p_district.find("li").on('tap', function () {
					var $this = $(this);
					$district_username.text($this.text());
				    $district_username.attr("key",$this.attr("key"));
				});
        	}
		});
	});
	
	//用户名注册提交
	$username_submit.on("tap",function(){
		
		if(!(/^[A-Za-z0-9]+$/.test($register_username.val()))){
			alert("只能输入由数字和26个英文字母组成的用户名!");return false;
		}
		if($province_username.attr("key")==""||$city_username.attr("key")==""){
			alert("省份和城市不能为空");return false;
		}
		if($username_password.val()!=$("#username_rePassword").val()){
			alert("两次输入密码不同");return false;
		}
		if($district_username.attr("key")==""){
			$username_areaId.val($city_username.attr("key"));
		}else{
			$username_areaId.val($district_username.attr("key"));
		}
		$.ajax({
			url:"${base}/pad/register/submit.jhtml",
			data:$usernameRegisterForm.serialize(),
			dataType:"json",
			type:"post",
			success:function(message){
				if(message.type=="success"){
					location.href="addShop.jhtml?username="+message.content;
					return false;
				}
				alert(message.content);
				return false;
			}
		});
	});
	
	//省份-邮箱
	$province_email.on("tap",function(){
		$.ajax({
			url :"${base}/common/area.jhtml?parentId=0",
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_province.empty();
        		for(var key in data){
        			$p_province.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_province.iScroll('refresh');
				$p_province.find("li").on('tap', function () {
					var $this = $(this);
					$province_email.text($this.text());
				    $province_email.attr("key",$this.attr("key"));
				    $city_email.attr("key","").text("选择城市");$district_email.attr("key","").text("选择地区");
				});
        	}
		});
	});
	
	//城市-邮箱
	$city_email.on("tap",function(){
		if($province_email.attr("key")==""){
			alert("请先选择省份");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$province_email.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_city.empty();
        		for(var key in data){
        			$p_city.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_city.iScroll('refresh');
				$p_city.find("li").on('tap', function () {
					var $this = $(this);
					$city_email.text($this.text());
				    $city_email.attr("key",$this.attr("key"));
				    $district_email.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//地区-邮箱
	$district_email.on("tap",function(){
		if($city_email.attr("key")==""){
			alert("请先选择城市");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$city_email.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_district.empty();
        		for(var key in data){
        			$p_district.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_district.iScroll('refresh');
				$p_district.find("li").on('tap', function () {
					var $this = $(this);
					$district_email.text($this.text());
				    $district_email.attr("key",$this.attr("key"));
				});
        	}
		});
	});
	
	//邮箱注册提交
	$email_submit.on("tap",function(){
		if($register_email.val()==''||!(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test($register_email.val()))){
			alert("请输入合法的邮箱");return false;
		}
		if(email_securityCode_register!=$email_checkcode.val()){
			alert("验证码错误");return false;
		}
		if($province_email.attr("key")==""||$city_email.attr("key")==""){
			alert("省份和城市不能为空");return false;
		}
		if($email_password.val()!=$("#email_rePassword").val()){
			alert("两次输入密码不同");return false;
		}
		if($district_email.attr("key")==""){
			$email_areaId.val($city_email.attr("key"));
		}else{
			$email_areaId.val($district_email.attr("key"));
		}
		$.ajax({
			url:"${base}/pad/register/submit.jhtml",
			data:$emailRegisterForm.serialize(),
			dataType:"json",
			type:"post",
			success:function(message){
				alert(message.content);
				if(message.type=="success"){
					location.href="addShop.jhtml?username="+message.content;
					return false;
				}
				return false;
			}
		});
	});
	
	//手机注册提交
	$phone_submit.on("tap",function(){
		if($register_mobile.val()==''||!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($register_mobile.val()))){
			alert("请输入合法的电话号码");return false;
		}
		if(securityCode_register!=$phone_checkcode.val()){
			alert("验证码错误");return false;
		}
		if($province.attr("key")==""||$city.attr("key")==""){
			alert("省份和城市不能为空");return false;
		}
		if($phone_password.val()!=$("#rePassword").val()){
			alert("两次输入密码不同");return false;
		}
		var data;
		if($district.attr("key")==""){
			$areaId.val($city.attr("key"));
		}else{
			$areaId.val($district.attr("key"));
		}
		$.ajax({
			url:"${base}/pad/register/submit.jhtml",
			data:$phoneRegisterForm.serialize(),
			dataType:"json",
			type:"post",
			success:function(message){
				alert(message.content);
				if(message.type=="success"){
					location.href="addShop.jhtml?username="+message.content;
					return false;
				}
				return false;
			}
		});
	});
	
	//省份
	$province.on("tap",function(){
		$.ajax({
			url :"${base}/common/area.jhtml?parentId=0",
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_province.empty();
        		for(var key in data){
        			$p_province.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_province.iScroll('refresh');
				$p_province.find("li").on('tap', function () {
					var $this = $(this);
					$province.text($this.text());
				    $province.attr("key",$this.attr("key"));
				    $city.attr("key","").text("选择城市");$district.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//城市
	$city.on("tap",function(){
		if($province.attr("key")==""){
			alert("请先选择省份");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$province.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_city.empty();
        		for(var key in data){
        			$p_city.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_city.iScroll('refresh');
				$p_city.find("li").on('tap', function () {
					var $this = $(this);
					$city.text($this.text());
				    $city.attr("key",$this.attr("key"));
				    $district.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//地区
	$district.on("tap",function(){
		if($city.attr("key")==""){
			alert("请先选择城市");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$city.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_district.empty();
        		for(var key in data){
        			$p_district.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_district.iScroll('refresh');
				$p_district.find("li").on('tap', function () {
					var $this = $(this);
					$district.text($this.text());
				    $district.attr("key",$this.attr("key"));
				});
        	}
		});
	});
	//获取验证码-注册-邮箱
	$email_code.on('tap',function(){
		if($register_email.val()==''){
			$email_code.addClass('p_down');
			alert("请先填邮箱");
			return false;
		}
		if(!(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test($register_email.val()))){
			$email_code.addClass('p_down');
			alert("邮箱不合法");
			return false;
		}
		$.ajax({
			url:"${base}/pad/register/check_username.jhtml",
			type:"get",
			data:{username:$register_email.val()},
			dataType:"json",
			success:function(result){
				if(result==true){
					$.ajax({
						url:"${base}/pad/register/send_email.jhtml",
						type:"post",
						data:{email:$register_email.val() },
						dataType:"json",
						success:function(message){
							if(message.type=="success"){
								email_securityCode_register=message.content;
								email_timeout_register=setInterval(email_refreshTime_register,1000);
							}else{
								$email_code.addClass('p_down');
							}
							return false;
						}
					});
				}else{
					alert("邮箱已经存在!");
					$email_code.addClass('p_down');
				}
				return false;
			}
		});
	});
	
	//倒计时-注册-邮箱
	function email_refreshTime_register(){
		if(email_time_register>0){
			email_time_register=email_time_register-1;
			$email_code.text(email_time_register+"''");
		}else{
			$email_code.addClass('p_down');
			$email_code.text("获取验证码");
			clearInterval(email_timeout_register);
		}
	}
	//返回
	$return_back.on("tap",function(){
		location.href="${base}/pad/member/center.jhtml";
		return false;
	});
	//进入购物车
	$goCart.on("tap",function(){
		location.href="${base}/pad/cart/list.jhtml";
		return false;
	});
	//进入会员中心
	$memberCenter.on("tap",function(){
		location.href="${base}/pad/member/center.jhtml";
		return false;
	});
	
	//获取验证码-注册
	$phone_code.on('tap',function(){
		if($register_mobile.val()==''){
			$phone_code.addClass('p_down');
			alert("请先填写手机号");
			return false;
		}
		if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($register_mobile.val()))){
			$phone_code.addClass('p_down');
			alert("手机号码不符合"); 
			return false;
		}
		$.ajax({
			url:"${base}/pad/register/check_username.jhtml",
			type:"get",
			data:{username:$register_mobile.val()},
			dataType:"json",
			success:function(result){
				if(result==true){
					$.ajax({
						url:"${base}/pad/register/getCheckCode.jhtml",
						type:"post",
						data:{mobile:$register_mobile.val() },
						dataType:"json",
						success:function(message){
							if(message.type=="success"){
								securityCode_register=message.content;
								timeout_register=setInterval(refreshTime_register,1000);
							}else{
								$phone_code.addClass('p_down');
							}
							return false;
						}
					});
				}else{
					alert("手机已经存在!");
					$phone_code.addClass('p_down');
				}
				return false;
			}
		});
	});
	//倒计时-注册
	function refreshTime_register(){
		if(time_register>0){
			time_register=time_register-1;
			$phone_code.text(time_register+"''");
		}else{
			$phone_code.addClass('p_down');
			$phone_code.text("获取验证码");
			clearInterval(timeout_register);
		}
	}
});
</script>
</head>
<body>
<section class="p_section">
	[#include "/pad/include/navigation.ftl" /]
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:;" class="p_return" id="return_back">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">会员注册</div>
			<div class="p_detailfun p_regist_top">
				<ul>
					<li class="p_registtab_1 p_detailfunup">手机注册</li>
					<li class="p_registtab_2">邮箱注册</li>
					<li class="p_registtab_3">用户名注册</li>
				</ul>
			</div>
		</div>
	</div>
	<article class="p_article p_registc p_darticle0 p_zc">
		<div class="bodycont">
			<div class="p_userrg_wind p_userrg_wind_1" id="p_nactscrooler_1">
				<form id="phoneRegisterForm">
					<input type="hidden" name="areaId" id="areaId" value="${area.id}"/>
					<div class="text">
						<p>
							<input type="text" placeholder="请输入手机号码" id="register_mobile" name="mobile"/>
							<span class="p_captcha p_down" id="phone_code">获取验证码</span>
						</p>
					</div>
					<div class="text p_incaptcha">
						<p><input type="text" placeholder="请输入手机验证码" id="phone_checkcode" name="checkCode"/></p>
					</div>
					<div class="text">
						<p><input type="password" placeholder="请设置用户密码" id="phone_password" name="password"/></p>
					</div>
					<div class="text">
						<p><input type="password" placeholder="请重复输入用户密码" id="rePassword"/></p>
					</div>
					<div class="text">
						<div class="p_area">
							<div class="p_areap p_areap_1">
								<span id="province" key="${(province.id)!}">
								[#if province??]
									${(province.name)!}
								[#else]
									选择省份
								[/#if]
								</span>
							</div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_2"><span id="city" key="${(city.id)!}">
								[#if city??]
									${(city.name)!}
								[#else]
									选择城市
								[/#if]
							</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_3"><span id="district" key="${(district.id)!}">
								[#if district??]
									${(district.name)!}
								[#else]
									选择地区
								[/#if]
							</span></div>
						</div>
					</div>
					<div class="text">
						<a href="javascript:;" class="p_nopwbtn" id="phone_submit">同意以下协议并注册</a>
					</div>
					<div class="p_agreement">注册协议</div>
				</form>
			</div>
			<div class="p_userrg_wind p_userrg_wind_2" id="p_nactscrooler_2">
				<form id="emailRegisterForm">
					<input type="hidden" name="areaId" id="email_areaId" value="${area.id}"/>
					<div class="text">
						<p>
							<input type="text" placeholder="请输入邮箱地址" id="register_email" name="email"/>
							<span class="p_captcha p_down" id="email_code">获取验证码</span>
						</p>
					</div>
					<div class="text p_incaptcha">
						<p><input type="text" placeholder="请输入验证码" id="email_checkcode" name="checkCode"/></p>
					</div>
					<div class="text">
						<p><input type="password" placeholder="请设置用户密码" id="email_password" name="password"/></p>
					</div>
					<div class="text">
						<p><input type="password" placeholder="请重复输入用户密码"  id="email_rePassword"/></p>
					</div>
					<div class="text">
						<div class="p_area">
							<div class="p_areap p_areap_1">
							<span id="province_email" key="${(province.id)!}">
								[#if province??]
									${(province.name)!}
								[#else]
									选择省份
								[/#if]
							</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_2">
							<span id="city_email" key="${(city.id)!}">
								[#if city??]
									${(city.name)!}
								[#else]
									选择城市
								[/#if]
							</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_3">
							<span id="district_email" key="${(district.id)!}">
								[#if district??]
									${(district.name)!}
								[#else]
									选择地区
								[/#if]
							</span></div>
						</div>
					</div>
					<div class="text">
						<a href="javascript:;" class="p_nopwbtn" id="email_submit">同意以下协议并注册</a>
					</div>
					<div class="p_agreement">注册协议</div>
				</form>	
			</div>
			<div class="p_userrg_wind p_userrg_wind_3" id="p_nactscrooler_3">
				<form id="usernameRegisterForm">
					<input type="hidden" name="areaId" id="username_areaId" value="${area.id}"/>
					<input type="hidden" name="captchaId"  value="${captchaId}"/>
					<div class="text">
						<p>
							<input type="text" placeholder="请输入用户名" id="register_username" name="username"/>
						</p>
					</div>
					
					<div class="text">
						<p><input type="password" placeholder="请设置用户密码" id="username_password" name="password"/></p>
					</div>
					<div class="text">
						<p><input type="password" placeholder="请重复输入用户密码" id="username_rePassword"/></p>
					</div>
					<div class="text p_incaptcha">
						<p><input type="text" placeholder="请输入验证码" id="captcha" name="captcha"  maxlength="4" autocomplete="off"/></p>
						<img id="captchaImage" src="${uc_captcha}?captchaId=${captchaId}" title="${message("shop.captcha.imageTitle")}"/>
					</div>
					<div class="text">
						<div class="p_area">
							<div class="p_areap p_areap_1"><span id="province_username" key="${(province.id)!}">
								[#if province??]
									${(province.name)!}
								[#else]
									选择省份
								[/#if]
							</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_2"><span id="city_username" key="${(city.id)!}">
								[#if city??]
									${(city.name)!}
								[#else]
									选择城市
								[/#if]
							</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_3"><span id="district_username" key="${(district.id)!}">
								[#if district??]
									${(district.name)!}
								[#else]
									选择地区
								[/#if]
							</span></div>
						</div>
					</div>
					<div class="text">
						<a href="javascript:;" class="p_nopwbtn" id="username_submit">同意以下协议并注册</a>
					</div>
					<div class="p_agreement">注册协议</div>
				</form>	
			</div>
		</div>
	</article>
	<div class="p_areawz">
		<div class="p_areawzul">
			<div class="p_areaselect p_areaselect_1" id="p_rescroller">
				<ul></ul>
			</div>
			<div class="p_areaselect p_areaselect_2" id="p_rescroller_1">
				<ul></ul>
			</div>
			<div class="p_areaselect p_areaselect_3" id="p_rescroller_2">
				<ul></ul>
			</div>
		</div>
	</div>
	<footer class="p_footer">
		<div class="p_navigation">
			<ul>
				<li><a href="#" alt="历史浏览"><i class="iconfont">&#xe60d</i></a></li>
				<li><a href="javascript:;" alt="购物车" id="goCart"><i class="iconfont">&#xe6d1</i></a>
					<span class="p_cartcount" id="cartItemCount">[#if count??&&count>0]${count}[#else]0[/#if]</span>
				</li>
				<li><a href="javascript:;" alt="会员中心" id="memberCenter"><i class="iconfont">&#x3432</i></a></li>
				<li class="p_home"><a href="${base}/pad" alt="首页"><i class="iconfont">&#xf0019</i></a></li>
			</ul>
		</div>
	</footer>
</section>
<div class="p_agreementtext" id="p_atextscrooll">
	<div>
		<h1>用户协议</h1>
		<p>用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容用户协议内容</p>
	</div>
</div>
<div class="p_loginclose p_registclose"></div>
</body>
</html>
