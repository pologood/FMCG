<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>已使用列表 - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.coupon.list")} <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="usedList.jhtml" method="get">
		<div class="bar">
			<a href="list.jhtml" class="iconButton">
				<span class="moveIcon">&nbsp;</span>返回到列表
			</a>
		</div>
		<input type="hidden" name="id" value="${usedListCouponId}" />
		<table id="listTable" class="list">
			<tr>
				<th>
					<a>红包名称</a>
				</th>
				<th>
					<a>红包金额</a>
				</th>
				<th>
					<a>红包编码</a>
				</th>
				<th>
					<a>创建时间</a>
				</th>
				<th>
					<a>使用时间</a>
				</th>
				<th>
					<a>使用会员</a>
				</th>
			</tr>
			[#list page.content as couponCode]
				<tr>
					<td>
						${couponCode.coupon.name}
					</td>
					<td>
						${couponCode.coupon.amount}
					</td>
					<td>
						${couponCode.code}
					</td>
					<td>
						${couponCode.createDate}
					</td>
					<td>
						${couponCode.usedDate}
					</td>
					<td>
						${couponCode.member.username}
					</td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
</body>
</html>