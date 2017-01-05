<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>已发放列表 - Powered By rsico</title>
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
	<form id="listForm" action="sendedList.jhtml" method="get">
		<div class="bar">
			<a href="list.jhtml" class="iconButton">
				<span class="moveIcon">&nbsp;</span>返回到列表
			</a>
		</div>
		<input type="hidden" name="id" value="${sendedListCouponId}" />
		<table id="listTable" class="list">
			<tr>
				<th>
					<a>企业名称</a>
				</th>
				<th>
					<a>会员名称</a>
				</th>
				<th>
					<a>会员账号</a>
				</th>
				<th>
					<a>红包名称</a>
				</th>
				<th>
					<a>发放数量</a>
				</th>
			</tr>
			[#list page.content as tuple]
			<tr>
				<td>
					[#if tuple.get(1).tenant??]
						${tuple.get(1).tenant.name}
					[#else]
						-
					[/#if]
				</td>
				<td>
					[#if tuple.get(1).name??]
						${tuple.get(1).name}
					[#else]
						-
					[/#if]
				</td>
				<td>
					[#if tuple.get(1).username??]
						${tuple.get(1).username}
					[#else]
						-
					[/#if]
				</td>
				<td>
					${tuple.get(2).name}
				</td>
				<td>
					${tuple.get(0)}
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