<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.product.list")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<style type="text/css">
.moreTable th {
	width: 80px;
	line-height: 25px;
	padding: 5px 10px 5px 0px;
	text-align: right;
	font-weight: normal;
	color: #333333;
	background-color: #f8fbff;
}

.moreTable td {
	line-height: 25px;
	padding: 5px;
	color: #666666;
}

.promotion {
	color: #cccccc;
}
</style>
<script type="text/javascript">

function getArealSelect()
{
	var $areaId = $("#areaId2");
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
}
$().ready(function() {

	var $listForm = $("#listForm");
	var $moreButton = $("#moreButton");
	var $areaSelect = $("#areaSelect");
	var $filterSelect = $("#filterSelect");
	var $filterOption = $("#filterOption a");
	
	[@flash_message /]

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
	
	var $popu = $("#listTable a.popu");
	// 推广链接
	$popu.click(function() {
		var productId=$(this).attr("val");
			$.dialog({
				title: "获取推广链接",
				[@compress single_line = true]
					content: '
						<div>
						<table style="height: 141px;width:480px;">
							<tr>
								<th>选择推广的类型<\/th>
								<td>
									<select id="urlType" name="urlType">
										[#list urlTypes as urlType]
											<option value="${urlType}">${message("Ad.UrlType."+urlType)}<\/option>
										[/#list]	
									<\/select>
								<\/td>
							<\/tr>
							<tr>
								<th>链接地址<\/th>
								<td>
									<input type="text" id="url_result" class="text" style="width:250px"\/>
								<\/td>
							<\/tr>
							<tr >
								<td><\/td>
								<td><a href="javascript:;" id="getUrl" class="button">获取<\/a><\/td>
							<\/tr>
						<\/table>	
						<\/div>
					',
				[/@compress]
				width: 480,
				modal: true,
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					$("#getUrl").click(function(){
						$.ajax({
							url:"${base}/ajax/common/getUrl.jhtml",
							data:{
								urlType:$("#urlType").val(),
								id:productId,
								businessType:'product'
							},
							dataType:"json",
							type:"get",
							success:function(message){
								if(message.type=='success'){
									$("#url_result").val(message.content);
								}else{
									$.message("获取失败！");
								}
							}
						});
					});
				}
			});
	});

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.product.list")} <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
		<input type="hidden" id="promotionId" name="promotionId" value="${promotionId}" />
		<input type="hidden" id="isMarketable" name="isMarketable" value="[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]" />
		<input type="hidden" id="isOutOfStock" name="isOutOfStock" value="[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]" />
		<input type="hidden" id="isStockAlert" name="isStockAlert" value="[#if isStockAlert??]${isStockAlert?string("true", "false")}[/#if]" />
		<div class="bar">
			<a href="add.jhtml" class="iconButton ">
				<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
			</a>
			<div class="buttonWrap">
				<!--
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				-->
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
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<div class="popupMenu">
					<ul id="searchPropertyOption">
						<li>
							<a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if] val="name">${message("Product.name")}</a>
						</li>
						<li>
							<a href="javascript:;"[#if page.searchProperty == "sn"] class="current"[/#if] val="sn">${message("Product.sn")}</a>
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
					促销类型
				</th>
				<th>
					<a href="javascript:;" class="sort" name="sn">${message("Product.sn")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">${message("Product.name")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="productCategory">${message("Product.productCategory")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="price">${message("Product.price")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="cost">${message("Product.cost")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="stock">${message("Product.stock")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="isMarketable">${message("Product.isMarketable")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
				</th>
				<th>
					<span>${message("admin.common.handle")}</span>
				</th>
			</tr>
			[#list page.content as product]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${product.id}" />
					</td>
					<td>
						积分商品
					</td>
					<td>
						${product.sn}
					</td>
					<td>
						<span title="${product.fullName}">
							${abbreviate(product.fullName, 50, "...")}
							[#if product.isGift]
								<span class="gray">[${message("admin.product.gifts")}]</span>
							[/#if]
						</span>
						[#list product.validPromotions as promotion]
							<span class="promotion">${promotion.name}</span>
						[/#list]
					</td>
					<td>
						${product.productCategory.name}
					</td>
					<td>
						${product.point}
					</td>
					<td>
						${currency(product.cost)}
					</td>
					<td>
						[#if product.stock??]
							[#if product.allocatedStock == 0]
								<span[#if product.isOutOfStock] class="red"[/#if]>${product.stock}</span>
							[#else]
								<span[#if product.isOutOfStock] class="red"[/#if] title="${message("Product.allocatedStock")}: ${product.allocatedStock}">${product.stock}</span>
							[/#if]
						[/#if]
					</td>
					<td>
						<span class="${product.isMarketable?string("true", "false")}Icon">&nbsp;</span>
					</td>
					<td>
						<span title="${product.createDate?string("yyyy-MM-dd HH:mm:ss")}">${product.createDate}</span>
					</td>
					<td>
						<a href="edit.jhtml?id=${product.id}">[${message("admin.common.edit")}]</a>
						[#if product.isMarketable]
							<a href="${base}${product.path}" target="_blank">[下架]</a>
						[#else]
							[上架]
						[/#if]
						<a href="javascript:;" class="popu" val="${product.id}">[设置促销]</a>
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