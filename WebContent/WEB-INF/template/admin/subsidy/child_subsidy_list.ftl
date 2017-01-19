<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>奖励补贴列表 - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	#listForm th,#listForm td{
		text-align: center;
	}
</style>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<script type="text/javascript">
$().ready(function() {
	[@flash_message /]
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 奖励补贴 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="child_subsidy_list.jhtml" method="get">
		<div class="bar">
	        <div class="buttonWrap">
	        	<a href="subsidy_list.jhtml" class="iconButton"><span class="moveIcon">&nbsp;</span>返回到列表</a>
	            <a href="javascript:;" id="refreshButton" class="iconButton">
	                <span class="refreshIcon">&nbsp;</span>刷新
	            </a>
	            <div class="menuWrap">
	                <a href="javascript:;" id="pageSizeSelect" class="button">
	                ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
	                </a>
	                <div class="popupMenu">
	                    <ul id="pageSizeOption">
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
	                        </li>
	                    </ul>
	                </div>
	            </div>
	        </div>
	        <div class="menuWrap">
	            <div class="search">
	                <input type="text" id="keyword" name="keyword" placeholder="导购、商家" value="" maxlength="200"/>
	                <button type="submit">&nbsp;</button>
	            </div>
	        </div>
	    </div>
		<input type="hidden" name="id" value="" />
		<table id="listTable" class="list" >
			<tr>
				<th>生成日期</th>
				<th>商家名称</th>
				<th>补贴用户名称</th>
				<th>补贴金额</th>
				<th>状态</th>
				<!-- <th>操作</th> -->
			</tr>
			[#list page.content as childSubsidies]
				<tr>
					<td>${childSubsidies.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
					<td>${(childSubsidies.tenantName)!'--'}</td>
					<td>${(childSubsidies.username)!'--'}</td>
					<td>${(childSubsidies.amount)!'--'}</td>
					<td>
						[#if childSubsidies.status=="yes"]已入账
						[#elseif childSubsidies.status=="no"]未入账
						[#elseif childSubsidies.status=="fail"]失败(<span style="color:red;">${childSubsidies.failReason}</span>)[/#if]
					</td>
					<!-- <td><a href="${base}/admin/subsidy/child_subsidy_list.jhtml">重新入账</a></td> -->
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
	<div id="trade_wrap"></div>
</body>
</html>