<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>领用数量列表 - Powered By rsico</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 领用数量列表<span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="marked_count_list.jhtml" method="get">
		<div class="bar">
			<a href="javascript:history.go(-1);" class="iconButton"><span class="moveIcon">&nbsp;</span>返回</a>
			<a href="javascript:;" id="refreshButton" class="iconButton">
	                <span class="refreshIcon">&nbsp;</span>刷新
	            </a>
		</div>
		<input type="hidden" name="guideMemberId" value="${guideMemberId}" />
		<input type="hidden" name="couponId" value="${couponId}" />
		<table id="listTable" class="list">
			<tr>
				<th>
					<a>登记时间</a>
				</th>
				<th>
					<a>领取时间</a>
				</th>
				<th>
					<a>领取编码</a>
				</th>
				<th>
					<a>登记状态</a>
				</th>
				<th>
					<a>使用次数</a>
				</th>
				
			</tr>
			[#list page.content as cn]
				<tr>
					<td>
						${cn.markedDate}
					</td>
					<td>
						${cn.receiveDate}
					</td>
					<td>
						${cn.code}
					</td>
					<td>
						[#if cn.status=="bound"]已登记[#elseif cn.status=="receive"]已领用[#else]已用完[/#if]
					</td>
					<td>
						${cn.useTimes}
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