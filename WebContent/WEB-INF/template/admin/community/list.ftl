<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>社区列表 - Powered By rsico</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 社区列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<div class="bar">
			<a href="add.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
			</a>
			<div class="buttonWrap">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
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
					<!-- <span id="searchPropertySelect" class="arrow">&nbsp;</span> -->
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="搜索社区名称"/>
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if] val="name">社区名称</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="createDate">创建时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="status">状态</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">社区名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="area">行政区域</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="area">地理位置</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="area">门店数量</a>
				</th>
				<th>
					<span>${message("admin.common.handle")}</span>
				</th>
			</tr>
			[#list page.content as community]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${community.id}" />
					</td>
					<td>
						<span title="${community.createDate?string("yyyy-MM-dd HH:mm:ss")}">${community.createDate}</span>
					</td>
					<td>
					  [#if community.status="wait"]
					   	申请
					  [#elseif community.status="open"]
					    开通
					  [#else]
					    关闭
					  [/#if]
					</td>
					<td>
						<span title="${community.name}">${abbreviate(community.name, 50, "...")}</span>
					</td>
					<td>
						${community.area.fullName}
					</td>
					<td>
						[#if community.location?has_content]经度：${community.location.lng} ，纬度：${community.location.lat}[#else]--[/#if]
					</td>
					<td>
						[#if community.deliveryCenters?size>0]
							${community.deliveryCenters?size}&nbsp;
							<a href="${base}/admin/community/deliveryCenter_list.jhtml?id=${community.id}">[查看]</a>
						[#else]
							0&nbsp;&nbsp;<a href="${base}/admin/community/deliveryCenter_add.jhtml?communityId=${community.id}">[添加]</a>
						[/#if]
					</td>
					<td>
						<a href="edit.jhtml?id=${community.id}">[${message("admin.common.edit")}]</a>
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