<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.role.edit")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.authorities label {
	min-width: 120px;
	_width: 120px;
	display: block;
	float: left;
	padding-right: 4px;
	_white-space: nowrap;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $selectAll = $("#inputForm .selectAll");
	
	[@flash_message /]
	
	$selectAll.click(function() {
		var $this = $(this);
		var $thisCheckbox = $this.closest("tr").find(":checkbox");
		if ($thisCheckbox.filter(":checked").size() > 0) {
			$thisCheckbox.prop("checked", false);
		} else {
			$thisCheckbox.prop("checked", true);
		}
		return false;
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			authorities: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.role.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${role.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Role.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" value="${role.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Role.roleType")}:
				</th>
				<td>
					<select id="roleType" name="roleType">
						[#if role.roleType=="admin"]
						<option value="admin">${message("Role.roleType.admin")}</option>
						<option value="proxy">${message("Role.roleType.proxy")}</option>
						<option value="provinceproxy">${message("Role.roleType.provinceproxy")}</option>
						<option value="cityproxy">${message("Role.roleType.cityproxy")}</option>
						<option value="countyproxy">${message("Role.roleType.countyproxy")}</option>
						<option value="personproxy">${message("Role.roleType.personproxy")}</option>
						[#else]
						<option value="${role.roleType}">${message("Role.roleType."+role.roleType)}</option>
						[/#if]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Role.description")}:
				</th>
				<td>
					<input type="text" name="description" class="text" value="${role.description}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					&nbsp;
				</td>
			</tr>
			<tr class="authorities">
				<th>
					<a href="javascript:;" class="selectAll" title="${message("admin.role.selectAll")}">操作权限</a>
				</th>
				<td>
					<span class="fieldSet">
						<label>
							<input type="checkbox" name="authorities" value="admin:invitationCode"[#if role.authorities?seq_contains("admin:invitationCode")] checked="checked"[/#if] />邀请码管理
						</label>
						<label>
							<input type="checkbox" name="authorities" value="admin:enterPrise"[#if role.authorities?seq_contains("admin:enterPrise")] checked="checked"[/#if] />合作伙伴
						</label>
						<label>
							<input type="checkbox" name="authorities" value="admin:modulePermissions"[#if role.authorities?seq_contains("admin:modulePermissions")] checked="checked"[/#if] />模板权限
						</label>
						<label>
							<input type="checkbox" name="authorities" value="admin:commission"[#if role.authorities?seq_contains("admin:commission")] checked="checked"[/#if] />佣金结算
						</label>
						
					</span>
				</td>
			</tr>
			[#if role.isSystem]
				<tr>
					<th>
						&nbsp;
					</th>
					<td>
						<span class="tips">${message("admin.role.editSystemNotAllowed")}</span>
					</td>
				</tr>
			[/#if]
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}"[#if role.isSystem] disabled="disabled"[/#if] />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='list.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>