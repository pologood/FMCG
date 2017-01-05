
$(document).ready(function(){

	
	//输入框激活焦点、移除焦点
	jQuery.focusblur = function(focusid) {
		var focusblurid = $(focusid);
		var defval = focusblurid.val();
		focusblurid.focus(function(){
			var thisval = $(this).val();
			if(thisval==defval){
				$(this).val("");
			}
		});
		focusblurid.blur(function(){
			var thisval = $(this).val();
			if(thisval==""){
				$(this).val(defval);
			}
		});
	 
	};
	/*下面是调用方法*/
	$.focusblur("#form-phone");

	//输入框激活焦点、溢出焦点的渐变特效
	//输入用户名
	if($("#form-account").val()){
		$("#form-account").prev().fadeOut();
	};
	$("#form-account").focus(function(){
		$(this).prev().fadeOut();
		$(".item-regName-wrap .input-tip span").show();
	});
	$("#form-account").blur(function(){
		if(!$("#form-account").val()){
			$(this).prev().fadeIn();
			$(".item-regName-wrap .input-tip span").hide();
		}else {
			$(".item-regName-wrap .input-tip span").hide();
		};
	});
	//输入手机号码
	if($("#form-phone").val()){
		$("#form-phone").prev().fadeOut();
	};
	$("#form-phone").focus(function(){
		$(this).prev().fadeOut();
		$(".item-phone-wrap .input-tip span").show();
	});
	$("#form-phone").blur(function(){
		if(!$("#form-phone").val()){
			$(this).prev().fadeIn();
			$(".item-phone-wrap .input-tip span").hide();
		}else {
			$(".item-phone-wrap .input-tip span").hide();
		};
	});
	//输入密码
	if($("#password").val()){
		$("#password").prev().fadeOut();
	};
	$("#password").focus(function(){
		$(this).prev().fadeOut();
		$(".item-pwd-wrap .input-tip span").show();
	});
	$("#password").blur(function(){
		if(!$("#password").val()){
			$(this).prev().fadeIn();
			$(".item-pwd-wrap .input-tip span").hide();
		}else {
			$(".item-pwd-wrap .input-tip span").hide();
		};
	});
	//再次输入密码
	if($("#rePassword").val()){
		$("#rePassword").prev().fadeOut();
	};
	$("#rePassword").focus(function(){
		$(this).prev().fadeOut();
		$(".item-pwdRepeat-wrap .input-tip span").show();
	});
	$("#rePassword").blur(function(){
		if(!$("#rePassword").val()){
			$(this).prev().fadeIn();
			$(".item-pwdRepeat-wrap .input-tip span").hide();
		}else {
			$(".item-pwdRepeat-wrap .input-tip span").hide();
		};
	});
	//门店名称
	if($("#form-storeName").val()){
		$("#form-storeName").prev().fadeOut();
	};
	$("#form-storeName").focus(function(){
		$(this).prev().fadeOut();
		$(".item-storeName-wrap .input-tip span").show();
	});
	$("#form-storeName").blur(function(){
		if(!$("#form-storeName").val()){
			$(this).prev().fadeIn();
			$(".item-storeName-wrap .input-tip span").hide();
		}else {
			$(".item-storeName-wrap .input-tip span").hide();
		};
	});
	//企业法人
	if($("#form-legalPerson").val()){
		$("#form-legalPerson").prev().fadeOut();
	};
	$("#form-legalPerson").focus(function(){
		$(this).prev().fadeOut();
		$(".item-legalPerson-wrap .input-tip span").show();
	});
	$("#form-legalPerson").blur(function(){
		if(!$("#form-legalPerson").val()){
			$(this).prev().fadeIn();
			$(".item-legalPerson-wrap .input-tip span").hide();
		}else {
			$(".item-legalPerson-wrap .input-tip span").hide();
		};
	});
	//详细地址地址
	if($("#form-fullAddress").val()){
		$("#form-fullAddress").prev().fadeOut();
	};
	$("#form-fullAddress").focus(function(){
		$(this).prev().fadeOut();
		$(".item-shipAddress-wrap .input-tip span").show();
	});
	$("#form-fullAddress").blur(function(){
		if(!$("#form-fullAddress").val()){
			$(this).prev().fadeIn();
			$(".item-shipAddress-wrap .input-tip span").hide();
		}else {
			$(".item-shipAddress-wrap .input-tip span").hide();
		};
	});
	//验证码
	if($("#authCode").val()){
		$("#authCode").prev().fadeOut();
	};
	$("#authCode").focus(function(){
		$(this).prev().fadeOut();
		$(".item-authcode-wrap .input-tip span").show();
	});
	$("#authCode").blur(function(){
		if(!$("#authCode").val()){
			$(this).prev().fadeIn();
			$(".item-authcode-wrap .input-tip span").hide();
		}else {
			$(".item-authcode-wrap .input-tip span").hide();
		};
	});
	//手机验证码
	if($("#phoneCode").val()){
		$("#phoneCode").prev().fadeOut();
	};
	$("#phoneCode").focus(function(){
		$(this).prev().fadeOut();
		$(".item-mobileCode-wrap .input-tip span").show();
	});
	$("#phoneCode").blur(function(){
		if(!$("#phoneCode").val()){
			$(this).prev().fadeIn();
			$(".item-mobileCode-wrap .input-tip span").hide();
		}else {
			$(".item-mobileCode-wrap .input-tip span").hide();
		};
	});
	
	//ajax提交注册信息
	$("#submit").bind("click", function(){
		regist(validate);
	});

	$("body").each(function(){
		$(this).keydown(function(){
			if(event.keyCode == 13){
				regist(validate);
			}
		});
	});
	
});

function regist(validate){	
	//校验Email, password，校验如果失败的话不提交
	if(validate.form()){
		if($("#checkBox").attr("checked")){
			var md5 = new MD5();
			$.ajax({
				url: "./user/regist.do",
				type: "post",
				data: {
					userID: $("#email").val(),
					password: md5.MD5($("#password").val()),
					userName: $("#contact").val(),
					companyName: $("#company").val(),
					tel: $("#tel").val(),
					QQ: $("#qq").val()
					
				},
				dataType: "json",
				beforeSend: function(){
					$('.loading').show();
				},
				success: function(data){
					$('.loading').hide();
					if(data.hasOwnProperty("code")){
						if(data.code == 0){
							//注册成功
							window.location.href = "registOk.jsp?email="+$('#email').val();
						}else if(data.code == 1){
							//数据库链接失败
							$(".login-error").html($.i18n.prop("Error.Exception"));
						}else if(data.code == 2){
							//参数传递失败
							$(".login-error").show();
							$(".login-error").html($.i18n.prop("Error.ParameterError"));
						}else if(data.code == 3){
							//公司已经被注册
							$("#company").addClass("error");
							$("#company").after(registError);						
							$("#company").next("label.repeated").text($.i18n.prop("Error.CompaniesAlreadyExists"));
							registError.show();
						}else if(data.code == 4){
							//邮箱已经被注册
							$("#email").addClass("error");
							$("#email").after(registError);
							$("#email").next("label.repeated").text($.i18n.prop("Error.EmailAlreadyExists"));
							registError.show();
						}else{
							//系统错误
							$(".login-error").html($.i18n.prop("Error.SysError"));
						}
					}
				}
			});
		}else{
			//勾选隐私政策和服务条款
			$(".login-error").show();
			$(".login-error").html($.i18n.prop("Error.ReadAndAgree"));
		}
	}
}

