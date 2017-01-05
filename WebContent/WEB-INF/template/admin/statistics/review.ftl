<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.index.build")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $count = $("#count");
	var $statusTr = $("#statusTr");
	var $status = $("#status");
	var $submit = $(":submit");
	
	var first;
	var buildCount;
	var buildTime;
	var count;
	
	// 表单验证
	$inputForm.validate({
		rules: {
			count: {
				required: true,
				integer: true,
				min: 1
			}
		},
		submitHandler: function(form) {
			first = 0;
			buildCount = 0;
			buildTime = 0;
			count = parseInt($count.val());
			//$buildType.prop("disabled", true);
			$count.prop("disabled", true);
			$submit.prop("disabled", true);
			$statusTr.show();
            $status.text("${message("admin.index.building")} [" + first + " - " + (first + count) + "]");
            build();
		}
	});
	
	function build() {
		$.ajax({
			url: "review.jhtml",
			type: "POST",
			data: {first: first, count: count},
			dataType: "json",
			cache: false,
			success: function(data) {
				buildCount += data.buildCount;
				buildTime += data.buildTime;
				if (!data.isCompleted) {
					first = data.first;
					$status.text("${message("admin.index.building")} [" + first + " - " + (first + count) + "]");
					build();
				} else {
					$count.prop("disabled", false);
					$submit.prop("disabled", false);
					$statusTr.hide();
					$status.empty();
					var time;
					if (buildTime < 60000) {
						time = (buildTime / 1000).toFixed(2) + "${message("admin.index.second")}";
					} else {
						time = (buildTime / 60000).toFixed(2) + "${message("admin.index.minute")}";
					}
					$.message("success", "${message("admin.index.success")} [${message("admin.index.buildCount")}: " + buildCount + " ${message("admin.index.buildTime")}: " + time + "]");
				}
			}
		});
	}

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 自动评价
	</div>
	<form id="inputForm" action="review.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>每次生成数:
				</th>
				<td>
					<input type="text" id="count" name="count" class="text" value="50" maxlength="9" />
				</td>
			</tr>
			<tr id="statusTr" class="hidden">
				<th>
					&nbsp;
				</th>
				<td>
					<span class="loadingBar">&nbsp;</span>
					<div id="status"></div>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='../common/index.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>