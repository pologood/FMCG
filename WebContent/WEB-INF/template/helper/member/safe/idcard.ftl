<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>${message("shop.member.index")}[#if systemShowPowered][/#if]</title>
	<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet" />
	<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" href="${base}/resources/helper/css/product.css"/>


	<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
	<script type="text/javascript">
	$().ready(function() {

		var $inputForm = $("#inputForm");
		var $browserFront = $("#browserFront");
		var $browserBack = $("#browserBack");
		var $submit = $("#submit");
		[@flash_message /]

		var settings = {
			width: 260,
			height: 360
		}

		$browserFront.browser(settings);
		$browserBack.browser(settings);
		var $url = location.href;

	//返回上一页
	$("#returnUrl").click(function(){
		if ($url.indexOf("urlid=1") > 0)
		{
			location.href = "${base}/helper/member/profile/edit.jhtml";
		}else {
			location.href = "index.jhtml";
		}
	});

	// 表单验证
	$inputForm.validate({
		rules: {
			no: {
				required: true,
				remote:{
					type:"POST",
					url:"IdcardCheck.jhtml",
					data:{
						no:function(){
							return $("#no").val();
						}
					}
				}
			},
			address: {
				required: true
			},
			name: {
				required: true
			},
			pathFront: {
				required: true
			},
			pathBack: {
				required: true
			}
		},
		messages:{
			no:  {
				required:'必填',
				remote:"身份证号码不合法！"
			},
			address: {
				required: '必填'
			},
			name: {
				required: '必填'
			},
			pathFront: {
				required: '必填'
			},
			pathBack: {
				required: '必填'
			}
		},
		errorPlacement: function(error, element) {

			error.insertAfter(element);
		},
		beforeSend: function() {
			$submit.prop("disabled", true);
		},
		success: function(message) {
			$submit.prop("disabled", false);
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
						<dt class="app-title" id="app_name">实名认证</dt>
						<dd class="app-status" id="app_add_status">
						</dd>
						<dd class="app-intro" id="app_desc">用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息。</dd>
					</dl>
				</div>
				<ul class="links" id="mod_menus">
					<li  ><a class="on" hideFocus="" href="javascript:;">实名认证</a></li>
				</ul>
			</div>



			<div class="input">
				<form id="inputForm" action="idcard.jhtml" method="post">
					<table class="input">
						<tr>
							<th>
								身份证号:
							</th>
							<td>
								<input type="text"  class="text" style="width:300px;" id="no" name="no" maxlength="255" value="${idcard.no}" />
							</td>
						</tr>
						<tr>
							<th>
								姓名:
							</th>
							<td>
								[#if idcard.authStatus!="success"]
								<input type="text"  class="text" style="width:300px;" name="name" maxlength="255" value="${member.name}" />
								[#else]
								${member.name}
								[/#if]
							</td>
						</tr>
						<tr>
							<th>
								身份证地址:
							</th>
							<td>
								[#if idcard.authStatus!="success"]
								<input type="text"  class="text" style="width:300px;" name="address" maxlength="255" value="${idcard.address}" />
								[#else]
								${idcard.address}
								[/#if]
							</td>
						</tr>
						<tr>
							<th>
								正面图扫描:
							</th>
							<td>
								<span class="fieldSet">
									[#if idcard.authStatus!="success"]
									<input type="text" name="pathFront" class="text" value="${idcard.pathFront}" maxlength="200" readonly="true" />
									<input type="button" id="browserFront" class="button" value="${message("admin.browser.select")}" />
									[/#if]
									[#if idcard.pathFront??]
									<a href="${idcard.pathFront}" target="_blank">${message("admin.common.view")}</a>
									[/#if]
								</span>
							</td>
						</tr>
						<tr>
							<th>
								反面图扫描:
							</th>
							<td>
								<span class="fieldSet">
									[#if idcard.authStatus!="success"]
									<input type="text" name="pathBack" class="text" value="${idcard.pathBack}" maxlength="200" readonly="true" />
									<input type="button" id="browserBack" class="button" value="${message("admin.browser.select")}" />
									[/#if]
									[#if idcard.pathBack??]
									<a href="${idcard.pathBack}" target="_blank">${message("admin.common.view")}</a>
									[/#if]
								</span>
							</td>
						</tr>
						<tr>
							<th>
								状态:
							</th>
							<td>
								[#if idcard.authStatus=="success"]已完成[/#if]
								[#if idcard.authStatus=="fail"]已驳回[/#if]
								[#if idcard.authStatus=="wait"]待审核[/#if]
								[#if idcard.authStatus=="none"]未认证[/#if]
							</td>
						</tr>
						<tr>
							<th>
								备注:
							</th>
							<td>
								${idcard.memo}
							</td>
						</tr>
						<tr>
							<th>
								&nbsp;
							</th>
							<td>
								[#if idcard.authStatus!="success"]
								<input type="submit" class="button" value="提交" />
								[/#if]
								<input type="button" class="button" value="${message("shop.member.back")}" id="returnUrl" />
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
