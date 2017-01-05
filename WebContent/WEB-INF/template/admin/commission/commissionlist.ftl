<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>佣金结算 - Powered By rsico</title>
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
	var $moreButton = $("#moreButton");
	var $areaSelect = $("#areaSelect");
	var $filterSelect = $("#filterSelect");
	var $filterOption = $("#filterOption a");
	// 更多选项
	$moreButton.click(function() {
		$.dialog({
			title: "${message("admin.product.moreOption")}",
			[@compress single_line = true]
				content: '
				<table id="moreTable" class="moreTable">
					<!--<tr>
						<th>
							${message("Product.productCategory")}:
						<\/th>
						<td>
							<select name="productCategoryId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list productCategoryTree as productCategory]
									<option value="${productCategory.id}"[#if productCategory.id == productCategoryId] selected="selected"[/#if]>
										[#if productCategory.grade != 0]
											[#list 1..productCategory.grade as i]
												&nbsp;&nbsp;
											[/#list]
										[/#if]
										${productCategory.name}
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>-->
					<tr>
						<th>
							${message("Product.brand")}:
						<\/th>
						<td>
							<select name="brandId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list brands as brand]
									<option value="${brand.id}"[#if brand.id == brandId] selected="selected"[/#if]>
										${brand.name}
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Product.promotions")}:
						<\/th>
						<td>
							<select name="promotionId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list promotions as promotion]
									<option value="${promotion.id}"[#if promotion.id == promotionId] selected="selected"[/#if]>
										${promotion.name}
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Product.tags")}:
						<\/th>
						<td>
							<select name="tagId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list tags as tag]
									<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>
										${tag.name}
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							地区:
						<\/th>
						<td>
							<span class="fieldSet">
								<input type="hidden" id="areaId2" name="areaId" value="${(area.id)!}" treePath="${(area.treePath)!}" \/>
							<\/span>
							<script>getArealSelect()<\/script>
						<\/td>
					<\/tr>
				<\/table>',
			[/@compress]
			width: 470,
			modal: true,
			ok: "${message("admin.dialog.ok")}",
			cancel: "${message("admin.dialog.cancel")}",
			onOk: function() {
				$("#moreTable :input").each(function() {
					var $this = $(this);
					$("#" + $this.attr("name")).val($this.val());
				});
				$listForm.submit();
			}
		});
	});

	// 商品筛选
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 佣金结算列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<div class="bar">
			<a href="javascript:;" class="iconButton">
				<span class="addIcon">&nbsp;</span>明细
			</a>
			<a href="statisticslist.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>统计
			</a>
			<div class="buttonWrap">
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				<div class="menuWrap">
					<a href="javascript:;" id="filterSelect" class="button">
						${message("admin.product.filter")}<span class="arrow">&nbsp;</span>
					</a>
					<div class="popupMenu">
						<ul id="filterOption" class="check">
							<li>
								<a href="javascript:;" name="isMarketable" val="true"[#if isMarketable?? && isMarketable] class="checked"[/#if]>${message("admin.product.isMarketable")}</a>
							</li>
							<li>
								<a href="javascript:;" name="isMarketable" val="false"[#if isMarketable?? && !isMarketable] class="checked"[/#if]>${message("admin.product.notMarketable")}</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="isList" val="true"[#if isList?? && isList] class="checked"[/#if]>${message("admin.product.isList")}</a>
							</li>
							<li>
								<a href="javascript:;" name="isList" val="false"[#if isList?? && !isList] class="checked"[/#if]>${message("admin.product.notList")}</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="isTop" val="true"[#if isTop?? && isTop] class="checked"[/#if]>${message("admin.product.isTop")}</a>
							</li>
							<li>
								<a href="javascript:;" name="isTop" val="false"[#if isTop?? && !isTop] class="checked"[/#if]>${message("admin.product.notTop")}</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="isGift" val="true"[#if isGift?? && isGift] class="checked"[/#if]>${message("admin.product.isGift")}</a>
							</li>
							<li>
								<a href="javascript:;" name="isGift" val="false"[#if isGift?? && !isGift] class="checked"[/#if]>${message("admin.product.nonGift")}</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="isOutOfStock" val="false"[#if isOutOfStock?? && !isOutOfStock] class="checked"[/#if]>${message("admin.product.isStack")}</a>
							</li>
							<li>
								<a href="javascript:;" name="isOutOfStock" val="true"[#if isOutOfStock?? && isOutOfStock] class="checked"[/#if]>${message("admin.product.isOutOfStack")}</a>
							</li>
							<li class="separator">
								<a href="javascript:;" name="isStockAlert" val="false"[#if isStockAlert?? && !isStockAlert] class="checked"[/#if]>${message("admin.product.normalStore")}</a>
							</li>
							<li>
								<a href="javascript:;" name="isStockAlert" val="true"[#if isStockAlert?? && isStockAlert] class="checked"[/#if]>${message("admin.product.isStockAlert")}</a>
							</li>
						</ul>
					</div>
				</div>
				<a href="javascript:;" id="moreButton" class="button">${message("admin.product.moreOption")}</a>
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
				<input type="text" id="beginDate" name="beginDate" class="text Wdate" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
				-<input type="text" id="endDate" name="endDate" class="text Wdate" value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
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
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="enterprisename">企业名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="tenantname">店铺名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="adminname">用户名称</a>
				</th>
				
				<th>
					<a href="javascript:;" class="sort" name="areaname">所属区域</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="commission">佣金</a>
				</th>				
				<th>
					<a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="modifyDate">${message("admin.common.modifyDate")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="remark">是否已结算</a>
				</th>
			</tr>
			[#list page.content as commission]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${commission.id}" />
					</td>
					<td>
						<span title="${commission.enterprisename}">${commission.enterprisename}</span>
					</td>
					<td>
						<span title="${commission.tenantname}">${commission.tenantname}</span>
					</td>
					<td>
						<span title="${commission.admin.name}">${commission.admin.name}</span>
					</td>
					
					<td>
						<span title="${commission.area.name}">${commission.area.name}</span>
					</td>
					<td>
						<span title="${commission.commission}">${commission.commission}</span>
					</td>
					<td>
						<span title="${commission.createDate?string("yyyy-MM-dd HH:mm:ss")}">${commission.createDate}</span>
					</td>
					<td>
						<span title="${commission.modifyDate?string("yyyy-MM-dd HH:mm:ss")}">${commission.modifyDate}</span>
					</td>
					<td>
						<span title="${commission.remark}">${commission.remark}</span>
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