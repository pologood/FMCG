<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.article.edit")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $areaId = $("#areaId");
	var timeout;
	
	[@flash_message /]
		// 地区选择
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			articleCategoryId: "required",
			"name": {
				required: true
			},
			"tenantType": {
				required: true
			},
			"areaId": {
				required: true
			}
		}
	});
	//商家认证
	var $certified = $("#certified");//门店
	var $enterprise = $("#enterprise");//企业
	var $manufacturers = $("#manufacturers");//厂家
	//var $certified = $("#certified");//个人
	function eachSelectInput(othis,nTrue)
	{
		var checkedInput = function(nTrue,iVal){
			$("input[name='tagIds']").each(function(){
			  	if ($(this).next().text() == iVal)
			  	{
			  		$(this).attr("checked",nTrue);
			  		return false;
			  	}
			  });
		}
		switch($(othis).attr("id"))
		{
		case "certified"://门店
		  checkedInput(nTrue,"店铺认证");
		  break;
		case "certified"://门店
		  checkedInput(nTrue,"门店认证");
		  break;
		case "enterprise"://企业
		  checkedInput(nTrue,"企业认证");
		  break;
		case "manufacturers"://厂家
		  checkedInput(nTrue,"厂家授权");
		  break;
		case "manufacturers"://个人
		 // checkedInput(nTrue,"线上付款");
		 // break;
		default:
		  return false;
		}
		
	}
	$("select[name='authenStatus']").change(function(){
		if ($(this).val() == "success")
		{
			eachSelectInput(this,true);
		}else {
			eachSelectInput(this,false);
		}
	});
	//取消认证
	function cancelCheckBox(othis,sVal)
	{
		var toogleCheck = function(obj,sVal){
			$("#"+obj+" option[value='"+sVal+"']").attr("selected",true);
		}
		switch($(othis).next().text())
		{
		case "门店认证"://门店
		  toogleCheck("certified",sVal);//门店
		  break;
		case "店铺认证"://门店
		  toogleCheck("certified",sVal);//门店
		  break;
		case "企业认证"://企业
		  toogleCheck("enterprise",sVal);//企业
		  break;
		case "厂家授权"://厂家
		  toogleCheck("manufacturers",sVal);//厂家
		  break;
		case "线上付款"://个人
		 // toogleCheck("certified",sVal);//个人
		 // break;
		default:
		  return false;
		}
	}
	$("input[name='tagIds']").click(function(){
		if ($(this).attr("checked") != "checked" && $(this).val() > 6)
		{
			cancelCheckBox(this,"wait");
		}else {
			cancelCheckBox(this,"success");
		}
	});
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 商家认证审核
	</div>
	<form id="inputForm" action="authenSave.jhtml" method="post"  enctype="multipart/form-data">
		<input type="hidden" name="id" value="${tenant.id}" />
		<table class="input">
				<tr>
					<th>
						<span class="requiredField">*</span>商家名称: 
					</th>
					<td>
						<input type="text" name="name" class="text" value="${(tenant.name)!}" readonly="readonly" /><span><a target="_blank" href="${base}/helper/${tenant.id}/index.jhtml" >查看</a></span>
					</td>
				</tr>
  				<tr>
  					<th>
  						店铺分类:
  					</th>
  					<td>
  						<select id="tenantCategoryId" name="tenantCategoryId" readonly="readonly">
  							[#list tenantCategoryTree as tenantCategory]
  								<option value="${tenantCategory.id}"[#if tenantCategory == tenant.tenantCategory] selected="selected"[/#if]>
  									[#if tenantCategory.grade != 0]
  										[#list 1..tenantCategory.grade as i]
  											&nbsp;&nbsp;
  										[/#list]
  									[/#if]
  									${tenantCategory.name}
  								</option>
  							[/#list]
  						</select>
  					</td>
  				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>性质: 
					</th>
					<td>
						<select name="tenantType">
							<option value="">--请选择--</option>
							<option value="enterprise" [#if ("enterprise" == (tenant.tenantType)!)] selected[/#if] >供应商</option>
							<option value="individual" [#if ("individual" == (tenant.tenantType)!)] selected[/#if] >商家</option>
							<!-- option value="organization" [#if ("organization" == (tenant.tenantType)!)] selected[/#if] >事业单位或社会团体</option>
							<option value="personal" [#if ("personal" == (tenant.tenantType)!)] selected[/#if] >个人</option -->
						</select>
					</td>
				</tr>
				<!--<tr>
					<th>
						<span class="requiredField">*</span>地址: 
					</th> 
					<td>
				   	<input type="text" name="address" class="text"  value="${(tenant.address)!}" readonly="readonly"/>
					</td>
				</tr>-->
				<tr>
					<th>
						<span class="requiredField">*</span>联系人: 
					</th>
					<td>
						<input type="text" name="linkman" class="text" value="${(tenant.linkman)!}" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>联系电话: 
					</th>
					<td>
						<input type="text" name="telephone" class="text" value="${(tenant.telephone)!}" readonly="readonly"/>
					</td>
				</tr>
				[#list tenant.authen as authen]
				[#if authen.authenType=="enterprise"]
  				<tr>
					<th align="right" valign="top"><span class="requiredField">*</span>营业执照：</th>
					<td>
						<div class="authen_upload">
							<p class="up_p01">工商注册号：<input type="text" name="licenseCode" class="text" value="${tenant.licenseCode}" /></p>
							<span class="fieldSet">
  							<a href="${authen.pathFront}" target="_blank"><img src="${authen.pathFront}" width="120px;" height="120px;" ></a>
  							</span>
						</div>
					</td>
				</tr>
				<tr>
					<th align="right"><span class="requiredField">*</span>法人代表：</th>
					<td><input type="text" name="legalRepr" class="text" value="${tenant.legalRepr}" /></td>
				</tr>
				<tr>
					<th align="right"><span class="requiredField">*</span>经营地址：</th>
					<td><span class="fieldSet">
							<input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}" treePath="${(tenant.area.treePath)!}" />
						</span>
					</td>
				</tr>
				<tr>
					<th align="right"></th>
					<td><input type="text" name="address" class="text"  value="${(tenant.address)!}" />
					</td>
				</tr>
				
				<tr>
					<th valign="top">
						<span class="requiredField">*</span>状态:
					</th>
					<td style="padding-bottom:50px;">
						<select id="enterprise" name="authenStatus">
							<option value="none">--请选择--</option>
							<!--<option value="none" [#if ("none" == (tenant.status)!)] selected[/#if] >审核中</option>-->
							<option value="wait" [#if ("wait" == (authen.authenStatus)!)] selected[/#if] >审核中</option>
							<option value="success" [#if ("success" == (authen.authenStatus)!)] selected[/#if] >已认证</option>
							<option value="fail" [#if ("fail" == (authen.authenStatus)!)] selected[/#if] >已驳回</option>
						</select>
					</td>
				</tr>
				[/#if]
				[#if authen.authenType=="certified"]
  				<tr>
					<th align="right" valign="top" width="140"><span class="requiredField">*</span>门店招牌：</th>
					<td>
						<span class="fieldSet">
  							<a href="${authen.pathFront}" target="_blank"><img src="${authen.pathFront}" width="120px;" height="120px;" ></a>
  						</span>
					</td>
				</tr>
				
				<tr>
					<th valign="top">
						<span class="requiredField">*</span>状态:
					</th>
					<td style="padding-bottom:50px;">
						<select id="certified" name="authenStatus">
							<option value="none">--请选择--</option>
							<!--<option value="none" [#if ("none" == (tenant.status)!)] selected[/#if] >审核中</option>-->
							<option value="wait" [#if ("wait" == (authen.authenStatus)!)] selected[/#if] >审核中</option>
							<option value="success" [#if ("success" == (authen.authenStatus)!)] selected[/#if] >已认证</option>
							<option value="fail" [#if ("fail" == (authen.authenStatus)!)] selected[/#if] >已驳回</option>
						</select>
					</td>
				</tr>
				[/#if]
				[#if authen.authenType=="manufacturers"]
  				<tr>
					<th align="right" width="140">厂家授权：</th>
					<td>
						<input name="authorization" type="text" class="text" value="${tenant.authorization}" />
					</td>
				</tr>
				<tr>
					<th align="right" valign="top">&nbsp;</th>
					<td>
						<span class="fieldSet">
  							<a href="${authen.pathFront}" target="_blank"><img src="${authen.pathFront}" width="120px;" height="120px;" ></a>
  						</span>
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>状态:
					</th>
					<td>
						<select id="manufacturers" name="authenStatus">
							<option value="none">--请选择--</option>
							<!--<option value="none" [#if ("none" == (tenant.status)!)] selected[/#if] >审核中</option>-->
							<option value="wait" [#if ("wait" == (authen.authenStatus)!)] selected[/#if] >审核中</option>
							<option value="success" [#if ("success" == (authen.authenStatus)!)] selected[/#if] >已认证</option>
							<option value="fail" [#if ("fail" == (authen.authenStatus)!)] selected[/#if] >已驳回</option>
						</select>
					</td>
				</tr>
				[/#if]
				[/#list]
				
			 	<tr>
					<th>
						${message("Article.tags")}:
					</th>
					<td>
						[#list tags as tag]
							<label>
								<input type="checkbox" name="tagIds" value="${tag.id}"[#if tenant.tags?seq_contains(tag)||tagSet?seq_contains(tag)] checked="checked"[/#if]  /><span>${tag.name}</span>
							</label>
						[/#list]
					</td>
				</tr>
				<tr>
					<th>
						商家联盟:
					</th>
					<td>
						[#list uniontags as tag]
							<label>
								<input type="checkbox" name="unionTagIds" value="${tag.id}"[#if tenant.unionTags?seq_contains(tag)] checked="checked"[/#if] />${tag.name}
							</label>
						[/#list]
					</td>
				</tr>
				<tr>
					<th>
						佣金比例: 
					</th>
					<td>
						<input type="text" name="brokerage" class="text" value="${(tenant.brokerage)!}"/><span>结算货款时,按交易额的比例抽取佣金,例:0.01</span>
					</td>
				</tr>
				<tr>
					<th>
						&nbsp;
					</th>
					<td>
						<input type="submit" class="button" value="${message("admin.common.submit")}" />
						<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)" />
					</td>
				</tr>
		</table>
	</form>
</body>
</html>