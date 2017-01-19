<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>已登记列表 - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<script type="text/javascript">
$().ready(function() {
	[@flash_message /]
	$("#export_ss").click(function(){
        $.message("success","正在帮您导出，请稍后");

    	//导出数据到excel
	    $(".table2excel").table2excel({
	        exclude: ".noExl",
	        name: "已登记详情",
	        filename: "已登记详情",
	        fileext: ".xls",
	        exclude_img: true,
	        exclude_links: false,
	        exclude_inputs: true
	    });
	});
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 已登记列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="markedList.jhtml" method="get">
		<div class="bar">
	        <div class="buttonWrap">
	        	<a href="javascript:history.go(-1);" class="iconButton"><span class="moveIcon">&nbsp;</span>返回</a>
	            <a href="javascript:;" id="export_ss" class="button">导出</a>
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
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 500] class="current"[/#if] val="500">500</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 1000] class="current"[/#if] val="1000">1000</a>
	                        </li>
	                    </ul>
	                </div>
	            </div>
	        </div>
	        <div class="menuWrap">
	            <div class="search" style="width:260px;">
	                <input type="text" id="keyword" name="keyword" placeholder="导购、商家、所属分类、地区" value="${keyword}" maxlength="200" style="width:200px;" />
	                <button type="submit">&nbsp;</button>
	            </div>
	        </div>
	    </div>
		<input type="hidden" name="id" value="${markedListCouponId}" />
		<table id="listTable" class="list table2excel">
			<tr>
				<th>登记导购</th>
				<th>登记商家</th>
				<th>所属分类</th>
				<th>地区</th>
				<th>登记数量</th>
				<th>领用数量</th>
				<th>佣金金额</th>
			</tr>
			[#list maps as c]
				<tr>
					<td>${c.guider_name}</td>
					<td>${c.tenant_name}</td>
					<td>${c.tenant_category_name}</td>
					<td>${c.area_name}</td>
					<td><a href="marked_count_list.jhtml?couponId=${markedListCouponId}&guideMemberId=${c.guider_name_id}">${c.mark_count}</a></td>
					<td><a href="token_count_list.jhtml?couponId=${markedListCouponId}&guideMemberId=${c.guider_name_id}">${c.user_count}</a></td>
					<td>${c.brokerage}</td>
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