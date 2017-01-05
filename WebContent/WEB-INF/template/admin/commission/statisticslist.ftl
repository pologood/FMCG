<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.article.edit")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/admin/css/style.css" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript">

$().ready(function() {

	
	
	[@flash_message /]
	


	
	});
	
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 编辑合作伙伴
	</div>
	<form id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
		<div class="bar">
			<a href="commissionlist.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>明细
			</a>
			<a href="javascript:;" class="iconButton">
				<span class="addIcon">&nbsp;</span>统计
			</a>
		</div>
		<input type="hidden" name="id" value="" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>贷款收入
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_yellow" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>分润收入
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_yellow" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>交易佣金
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_yellow" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>购物支出
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_blue" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>线下收款
				</th> 
				<td>
			   		<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_blue" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>其他支出
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_blue" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>其他收入
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_blue" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>充值
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_black" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>提现
				</th>
				<td>
					<div class="progressbar_1">
						<div class="text_1">${page}%</div>
						<div class="bar_black" style="width: ${page}%;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='commissionlist.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>