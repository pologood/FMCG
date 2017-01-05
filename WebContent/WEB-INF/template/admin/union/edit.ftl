<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.promotion.add")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules:{
			name:{
				required:true
			},
			price:{
				required:true,
				digits:true,
				min:360
			},
			brokerage:{
				required: true,
                digits: true,
                range:[0,100]
			}
			
		},
		messages:{
			name:{
				required:"必填"
			},
			price:{
				required:"必填",
				digits:"必须为整数",
				min:"不得小于360"
			},
			brokerage:{
				required:"必填",
				digits:"必须为整数",
				range:"佣金范围在0-100之间"
			}
		},
		submitHandler: function (form) {
            $("#brokerage_send").val($("#brokerage").val()/100);
            form.submit();
        }
	});
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">首页</a> &raquo; 编辑联盟
	</div>
	<form id="inputForm" action="${base}/admin/union/update.jhtml" method="post" enctype="multipart/form-data">
		<input type="hidden" name="unionId" value="${union.id}">
		<input type="hidden" name="brokerage_send" id="brokerage_send">
		<div class="tabContent">
			<table class="input">
				<tr>
					<th>
						<span class="requiredField">*</span>名称:
					</th>
					<td>
						<input type="text" name="name" class="text" maxlength="200" value="${union.name}" />
					</td>
				</tr>
				<tr>
					<th>
						联盟背景图:
					</th>
					<td>
						<input type="file" id="file" name="file" />
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>联盟年费:
					</th>
					<td>
						<input type="text" name="price" class="text" value="${union.price}"/>&nbsp;元/年
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>联盟佣金:
					</th>
					<td>
						<input type="text" name="brokerage" class="text" value="${union.brokerage*100}" id="brokerage" />&nbsp;%
					</td>
				</tr>
			</table>
		</div>
		<table class="input">
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