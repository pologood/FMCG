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
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 已领取列表<span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="sendedList.jhtml" method="get">
		<div class="bar">
	        <div class="buttonWrap">
		        <a href="javascript:history.go(-1);" class="iconButton">
					<span class="moveIcon">&nbsp;</span>返回
				</a>
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
	            <div class="search">
	                <input type="text" id="keyword" name="keyword" placeholder="会员名称、会员帐号" value="${keyword}" maxlength="200"/>
	                <button type="button" onclick="set_page_number()">&nbsp;</button>
	            </div>
	        </div>
	        <div class="menuWrap">
	            <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${(beginDate)!}" placeholder="领取开始时间"
	                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" style="text-align:center;" />&nbsp;-&nbsp;
	            <input type="text" id="endDate" name="endDate" class="text Wdate" value="${(endDate)!}" placeholder="领取结束时间"
	                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" style="text-align:center;"/>
	        </div>
	        <input type="button" class="button" value="查询" onclick="set_page_number()"/>
	    </div>
		<input type="hidden" name="id" value="${sendedListCouponId}" />
		<table id="listTable" class="list">
			<tr>
				<th>
					<a>领取时间</a>
				</th>
				<th>
					<a>商家名称</a>
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
			</tr>
			[#list page.content as couponCode]
			<tr>
				<td>
					${couponCode.createDate?string('yyyy-MM-dd mm:HH:ss')}
				</td>
				<td>
					[#if couponCode.member??][#if couponCode.member.tenant??]${couponCode.member.tenant.name}[#else]--[/#if][#else]--[/#if]
				</td>
				<td>
					[#if couponCode.member??]${couponCode.member.name}[#else]--[/#if]
				</td>
				<td>
					[#if couponCode.member??]${couponCode.member.username}[#else]--[/#if]
				</td>
				<td>
					${couponCode.coupon.name}
				</td>
			</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
<script type="text/javascript">
    function set_page_number(){
        $("#pageNumber").val("1");
        $("#listForm").submit();
    }
</script>
</body>
</html>