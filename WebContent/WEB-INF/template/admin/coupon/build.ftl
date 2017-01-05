<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.coupon.build")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $totalCount = $("#totalCount");
	var $count = $("#count");
	var totalCount = ${coupon.count};
	
	[@flash_message /]
	
	// 表单验证
	$("#inputForm").validate({
		rules: {
			count: {
				required: true,
				integer: true,
				min: 1
			}
		},
		submitHandler: function(form) {
			totalCount = totalCount + parseInt($count.val());
			$totalCount.text(totalCount);
			form.submit();
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.coupon.build")}
	</div>
	<form id="inputForm" action="setCouponCodeQuantity.jhtml" method="post">
		<input type="hidden" name="id" value="${coupon.id}" />
		<table class="input">
			<tr>
				<th>
					${message("Coupon.name")}:
				</th>
				<td>
					${coupon.name}
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.coupon.totalCount")}:
				</th>
				<td>
					<span id="totalCount">${coupon.count}</span>
				</td>
			</tr>
			<tr>
				<th>
					已领取数量:
				</th>
				<td>
					${coupon.sendCount}
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.coupon.usedCount")}:
				</th>
				<td>
					${coupon.usedCount}
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("admin.coupon.count")}:
				</th>
				<td>
					<input type="text" id="count" name="count" class="text" value="50" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="生成" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='list.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>