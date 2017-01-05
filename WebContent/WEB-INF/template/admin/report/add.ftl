<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>商家档案 - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $selectAll = $("#inputForm .selectAll");
	var $browserButton = $("#browserButton");
	//var $reportButton = $("#reportButton");
	
	[@flash_message /]
	
	$browserButton.browser();
	//$reportButton.reportUpload();
	
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
			description: "required",
			multipartFile:{
				required:true
				
			}
		},
		messages:{
			name:{
				required:"必填"
			},
			description:{
				required:"必填"
			},
			multipartFile:{
				required:"必填",
				pattern:"文件格式不正确"
			}
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo;报表管理&raquo;报表上传
	</div>
	<form id="inputForm" action="save.jhtml" method="post" encType="multipart/form-data">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>名称：
				</th>
				<td>
					<input type="text" name="name" class="text"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>描述：
				</th>
				<td>
					<input type="text" name="description" class="text"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>上传报表：
				</th>
				<td>
					<span class="fieldSet">
			        	<input type="file" name="multipartFile" />
				    </span>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='list.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>

</html>