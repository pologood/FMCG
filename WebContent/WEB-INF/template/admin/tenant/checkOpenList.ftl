<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>商家列表 - Powered By rsico</title>
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

	var $listForm = $("#listForm");
	var $filterSelect = $("#filterSelect");
	var $filterOption = $("#filterOption a");
	// 订单筛选
	$filterSelect.mouseover(function() {
		var $this = $(this);
		var offset = $this.offset();
		var $menuWrap = $this.closest("div.menuWrap");
		var $popupMenu = $menuWrap.children("div.popupMenu");
		$popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
		$menuWrap.mouseleave(function() {
			$popupMenu.hide();
		});
	});
	
	// 筛选选项
	$filterOption.click(function() {
		var $this = $(this);
		var $dest = $("#" + $this.attr("name"));
		if ($this.hasClass("checked")) {
			$dest.val("");
		} else {
			$dest.val($this.attr("val"));
		}
		$listForm.submit();
		return false;
	});
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 店铺列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="checkOpenList.jhtml" method="get">
	<input type="hidden" id="status" name="status" value="${status}" />
		<div class="bar">
			<div class="buttonWrap">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				<div class="menuWrap">
					<a href="javascript:;" id="filterSelect"	 class="button">
						店铺筛选<span class="arrow">&nbsp;</span>
					</a>
					<div class="popupMenu">
						<ul id="filterOption" class="check">
							<li>
								<a href="javascript:;" name="status" val="none"[#if status == "none"] class="checked"[/#if]>未开通</a>
							</li>
							<li>
								<a href="javascript:;" name="status" val="confirm"[#if status == "confirm"] class="checked"[/#if]>已审核</a>
							</li>
							<li>
								<a href="javascript:;" name="status" val="success"[#if status == "success"] class="checked"[/#if]>已开通</a>
							</li>
							<li>
								<a href="javascript:;" name="status" val="fail"[#if status == "fail"] class="checked"[/#if]>已关闭</a>
							</li>
						</ul>
					</div>
				</div>
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
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if] val="name">名称</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="menuWrap">
				<input type="text" id="beginDate" name="beginDate" class="text Wdate" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
				-<input type="text" id="endDate" name="endDate" class="text Wdate" value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="tenantCategory">分类</a>
				</th>
				<th>
					认证类型
				</th>
				<th>
					<a href="javascript:;" class="sort" name="status">状态</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
				</th>
				<th>
					<span>${message("admin.common.handle")}</span>
				</th>
			</tr>
			[#list page.content as tenant]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${tenant.id}" />
					</td>
					<td>
						<span title="${tenant.name}">${abbreviate(tenant.name, 50, "...")}</span>
					</td>
					<td>
						${tenant.tenantCategory.name}
					</td>
					<td>
						店铺开通
					</td>
					<td>
						[#if tenant.status=="none"]
						  未开通
						[#elseif tenant.status=="confirm"]
						 已审核
						[#elseif tenant.status=="success"]
						  已开通
						[#elseif tenant.status=="fail"]
						  已关闭
						[/#if]
					</td>
					<td>
						<span title="${tenant.createDate?string("yyyy-MM-dd HH:mm:ss")}">${tenant.createDate}</span>
					</td>
					<td>
						<a href="checkOpenEdit.jhtml?id=${tenant.id}">[审核]</a>
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