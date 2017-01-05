<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.admin.add")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/common/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	[@flash_message /]

	// 表单验证
	$inputForm.validate({
		rules: {
			"code": {
				required: true
			},
			"price": {
                required: true,
                number: true,
                min:${minInvitePrice},
				max:${functionFee}
			},
			"end_date": {
				required: true,
			}
		}
	});
});


</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 新增邀请码
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>邀请码:
				</th>
				<td>
					<input type="text" name="code" class="text" value="${code}" maxlength="20" readonly/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>价格:
				</th>
				<td>
					<input title="价格在${minInvitePrice}至${functionFee}之间" type="text" name="price" class="text" maxlength="20"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>有效期至:
				</th>
				<td>
                    <input type="text" id="endDate" name="endDate" class="text Wdate"
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'});"/>
				</td>
            </tr>
            <tr>
				<th>
					描述:
				</th>
				<td>
                    <textarea name="remark" style="width:50%;height:40px;" maxlength="100"></textarea>
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