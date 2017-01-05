<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>完善资料</title>
<link rel="shortcut icon" href="${base}/favicon.ico" />
<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/common/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready( function() {
 
	var $inputForm = $("#inputForm");
	var $areaId = $("#areaId");
	var $tenantCategoryId = $("#tenantCategoryId");
	var $id = $("#id");
	var $licensePhoto = $("#licensePhoto");
	var timeout;
	var $browserButton = $("#browserButton");
	
	[@flash_message /]
	$browserButton.browser();
		
		// 地区选择
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
	
		// 店铺分类
	$tenantCategoryId.lSelect({
		url: "${base}/helper/register/tenantCategory.jhtml"
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name:{
				required: true
			},
			areaId:{
				required: true
			},
			tenantCategoryId:{
				required: true
			},
			address:{
				required: true,
				maxlength: 100
			},
			linkman:{
				required: true,
				maxlength: 100
			},
			telephone:{
				required: true,
				pattern: /^1[3|4|5|8][0-9]\d{4,8}$/
			}
		},
		messages:{
			name:{
				required: "必填"
			},
			areaId:{
				required: "必填"
			},
			address:{
				required: "必填",
				maxlength: "长度超出"
			},
			linkman:{
				required: "必填",
				maxlength: "长度超出"
			},
			telephone:{
				required: "必填",
				pattern: "请输入正确的手机号码"
			}
		},
		submitHandler: function(form) {
			$.ajaxSetup({async:false});
			$.ajax({
				url: $inputForm.attr("action"),
				type: "POST",
				data:{
					name : $("#name").val(),
					address : $("#address").val(),
					linkman:$("#linkman").val(),
					telephone:$("#telephone").val(),
					id:$id.val(),
					licensePhoto:$licensePhoto.val(),
					tenantCategoryId:$tenantCategoryId.val(),
					areaId : $areaId.val()
				},
				dataType: "json",
				cache: false,
				success: function(message) {
					if(message.type == "success"){
					   [#if redirectUrl??]
					   		location.href ="${redirectUrl}";
					   [#else]
						  	location.href = "${base}/helper/member/index.jhtml";
					   [/#if]
					}else{
					   location.href = "${base}/helper/register/create.jhtml";
					}
				}
			});
			$.ajaxSetup({async:true});
		}
	});
});
</script>
</head>
</div>
	<!-- head页头公共部分start -->
	<div class="center logo_div"><img src="${base}/resources/helper/images/login_logo.gif" /></div>
	<!-- head页头公共部分end -->
	<div class="container1 register">
		<div class="span24">
			<div class="wrap">
				<div class="main clearfix">
				<div class="step step2">
					<ul>
						<li>用户注册</li>
						<li class="current">完善信息</li>
						<li>完成注册</li>
					</ul>
				</div>
				<div class="w_switching">
							<ul>
								<li class="w_phone w_hover">完善信息</li>
								
								<div class="clear"></div>
							</ul>
						</div>
					<form id="inputForm" action="create.jhtml" method="post">
					<input type="hidden" name="id" id="id" value="${(tenant.id)!}" />
						<table class="input">
							<tr>
								<th>
									<span class="requiredField">*</span>店铺名称: 
								</th>
								<td>
									<input type="text" name="name" id="name" class="text" value="${(tenant.name)!}"  />
								</td>
							</tr>
		        			<tr>
		        				<th>
		        					<span class="requiredField">*</span>商家分类:
		        				</th>
		        				<td>
									    <span class="fieldSet">		        				
		        						<input type="hidden" id="tenantCategoryId" name="tenantCategoryId" value="${(tenant.tenantCategory.id)!}" treePath="${(tenant.tenantCategory.treePath)!}" />
		        					</span> 	
		        				</td>
		        			</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>所属地区:
								</th>
								<td>
									<span class="fieldSet">
										<input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}" treePath="${(tenant.area.treePath)!}" />
									</span>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>地址: 
								</th> 
								<td>
							   	<input type="text" name="address" id="address" class="text"  value="${(tenant.address)!}" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>联系人: 
								</th>
								<td>
									<input type="text" name="linkman" id="linkman" class="text" value="${(tenant.linkman)!}"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>联系电话: 
								</th>
								<td>
									<input type="text" id="telephone" name="telephone" class="text" value="${(tenant.telephone)!}"/>
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>营业执照: 
								</th>
        						<td>
	            				<span class="fieldSet">
			               			<input type="text" id="licensePhoto" name="licensePhoto" class="text" value="${tenant.licensePhoto}" maxlength="200" title="营业执照" />
			              			<input type="button" id="browserButton" class="button" value="${message("admin.browser.select")}" />
			              			[#if tenant.licensePhoto??]
			           			  	<a href="${tenant.licensePhoto}" target="_blank">${message("admin.common.view")}</a>
				               		[/#if]
				            	</span>
        						</td>
							</tr>
							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="提交" hidefocus />
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
	[#include "/helper/include/footer.ftl" /]
</body>
</html>