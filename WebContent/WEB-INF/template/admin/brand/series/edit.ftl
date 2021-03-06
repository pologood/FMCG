<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.brand.edit")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			brand: "required",
			order: "digits"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.brand.edit")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${brandSeries.id}" />
		<input type="hidden" name="brandId" value="${brand.id}" />
		
		
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Brand.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" value="${brandSeries.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>拼音码:
				</th>
				<td>
					<input type="text" name="phonetic" class="text" value="${brandSeries.phonetic}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					上级:
				</th>
				<td>
					<select name="parentId">
						<option value="">顶级结点</option>
						[#list brandSeriesTree as category]
							[#if category != brandSeries && !children?seq_contains(category)]
								<option value="${category.id}"[#if category == brandSeries.parent] selected="selected"[/#if]>
									[#if category.grade != 0]
										[#list 1..category.grade as i]
											&nbsp;&nbsp;
										[/#list]
									[/#if]
									${category.name}
								</option>
							[/#if]
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					状态:
				</th>
				<td>
					<select id="status" name="status">
						[#list statuses as status]
							<option value="${status}" "[#if status == brandSeries.status] selected="selected"[/#if]>${message("BrandSeries.Status." + status)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="order" class="text" value="${brandSeries.order}" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Brand.introduction")}:
				</th>
				<td>
					<textarea id="editor" name="introduction" class="editor">${brandSeries.introduction?html}</textarea>
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