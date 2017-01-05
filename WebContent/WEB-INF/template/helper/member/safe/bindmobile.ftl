<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${message("shop.myAccount.title")}</title>
<link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css" />
<link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/product.css"/>
<link type="text/css" rel="stylesheet" href="${base}/resources/helper/font/iconfont.css" />


<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
<script src="${base}/resources/helper/js/amazeui.min.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $mobile = $("#mobile");
	var timeout;
	var times=0;
	var ii;
	var count =60;
	var $getCode = $("#getCode");
	var $span_1 = $("#span_1");

	[@flash_message /]

	// 表单验证
	$inputForm.validate({
		rules: {
			mobile: {
				required: true
			},
			securityCode: {
				required: true
			}
		}
	});

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
	$getCode.on('click',function(e){
		if($mobile.val()==''){
			alert("请先填写绑定手机"); return false;
		}
		$.ajax({
			url: "send_mobile.jhtml",
			type: "POST",
			data: {username:$mobile.val() },
			dataType: "json",
			cache: false,
			success: function(msg) {
				if (msg.message.type == "success") {
					 $getCode.attr('style','display:none;');
					$span_1.attr('style','display:block;');
					ii=setInterval(refreshTime,1*1000);
				} else {
				   $("#send").text("发送失败,请重新发送");
				}
			}
		});
	});

	[#if member.bindMobile == "binded"]
	 	$("#mobile").prop("disabled", true);
	[/#if]


});
function bind_mobile(){
	if($("#securityCode").val()==""){
		$.message("warn","验证码不能为空");
		return;
	}
	$.ajax({
		url: "bindmobile.jhtml",
		type: "POST",
		data: {captcha:$("#securityCode").val()},
		dataType: "json",
		cache: false,
		success: function(msg) {
			$.message(msg.message.type,msg.message.content);
			if (msg.message.type == "success") {
				location.href="index.jhtml"
			} 
		}
	});
}
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
			               		<dt class="app-title" id="app_name">手机绑定</dt>
			               		<dd class="app-status" id="app_add_status"></dd>
			               		<dd class="app-intro" id="app_desc">用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息。</dd>
		             		</dl>
	          			</div>
	          			<ul class="links" id="mod_menus">
	            			<li  ><a class="on" hideFocus="" href="javascript:;">[#if member.bindMobile == "binded"]解除绑定[#else]手机绑定[/#if]</a></li>
	          			</ul>
	          		</div>
	          		<div class="account-table1" id="account-table1">
	      				<div class="wrap" style="margin-top:0px;">
							<div class="main">
	 							<!-- <form id="inputForm" action="bindmobile.jhtml" method="post"> -->
									<table class="input">
										<tr>
											<th>手机号: </th>
											<td>
	                							[#if member.bindMobile == "binded"]
	                							<input type="hidden" name="mobile" id="mobile" class="text" maxlength="13" value="${member.mobile}" />
								  				<span>${mosaic(member.mobile, 3, "~~~")}</span>
								   				<span id="getCode"><a href="#" id="send">获取验证码</a></span>
												<div id="span_1" style="display:none">60秒后重新获取</div>
	                							[#else]
								  				<input type="text" name="mobile" id="mobile" class="text" maxlength="13" value="${member.mobile}" />
								   				<span id="getCode"><a href="#" id="send">获取验证码</a></span>
												<div id="span_1" style="display:none">60秒后重新获取</div>
												[/#if]
											</td>
										</tr>
										<tr>
											<th>校验码:</th>
											<td><input type="text" name="securityCode" id="securityCode" class="text" maxlength="20" /></td>
										</tr>
										<tr>
											<th>&nbsp;</th>
											<td>
	                							[#if member.bindMobile == "binded"]
	  											<input type="button" class="button" value="解  绑" onclick="bind_mobile()"/>
	                							[#else]
	  											<input type="button" class="button" value="绑  定" onclick="bind_mobile()"/>
												[/#if]
												<input type="button" class="button" value="${message("box.member.back")}" onclick="location.href='index.jhtml'" />
											</td>
										</tr>
									</table>
								<!-- </form> -->
							</div>
	    				</div>
	  				</div>
			  	</div>
			</div>
		</div>
	</div>
	[#include "/helper/include/footer.ftl" /]
</body>
</html>
