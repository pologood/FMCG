<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="baidu-site-verification" content="7EKp4TWRZT" />
	[@seo type = "index"]
	<title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if] </title>
	[#if seo.keywords??]
	<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
	<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
	[/@seo]
	<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet" />
	<link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet" />
	<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
	<script src="${base}/resources/helper/js/amazeui.min.js"></script>
	<script type="text/javascript">
		$().ready(function() {

			var $delete = $("#listTable a.delete");

			[@flash_message /]

			// 删除
			$delete.click(function() {
				var $this = $(this);
				$.dialog({
					type: "warn",
					content: "${message("admin.dialog.deleteConfirm")}",
					onOk: function() {
						$.ajax({
							url: "delete.jhtml",
							type: "POST",
							data: {id: $this.attr("val")},
							dataType: "json",
							cache: false,
							success: function(message) {
								$.message(message);
								if (message.type == "success") {
									$this.closest("tr").remove();
								}
							}
						});
					}
				});
				return false;
			});
		});
	</script>
</head>
<body>


	[#include "/helper/include/header.ftl" /]
	[#include "/helper/member/include/navigation.ftl" /]
	<div class="desktop">
		<div class="container bg_fff">

			[#include "/helper/member/include/border.ftl" /]

			[#include "/helper/member/include/menu.ftl" /]

			<div class="wrapper" id="wrapper">

				<div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
					<div class="js-app-header title-wrap" id="app_0000000844">
						<img class="js-app_logo app-img" src="${base}/resources/helper/images/upload5.png"/>
						<dl class="app-info">
							<dt class="app-title" id="app_name">商品分类</dt>
							<dd class="app-status" id="app_add_status">
							</dd>
							<dd class="app-intro" id="app_desc">查询我发布的商品分类。</dd>
						</dl>
					</div>
					<ul class="links" id="mod_menus">
						<li><a class="on" hideFocus="" href="${base}/helper/member/product_category/list.jhtml">商品分类</a></li>
					</ul>
				</div>
				<form id="listForm" action="list.jhtml" method="get">
					<div class="bar">
						<div class="buttonWrap">
						[@helperRole url="helper/member/product_category/list.jhtml" type="add"]
							[#if helperRole.retOper!="0"]
                                <a href="add.jhtml" class="iconButton">
                                    <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                                </a>
							[/#if]
						[/@helperRole]
							<a href="${base}/helper/member/product_category/list.jhtml" class="iconButton">
								<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
							</a>
						</div>
					</div>
					<table id="listTable" class="list">
						<tr>
							<th class="check">
								<input type="checkbox" id="selectAll" />
							</th>
							<th>
								<span>${message("ProductCategory.name")}</span>
							</th>
							<th>
								<span>编号</span>
							</th>
							<th>
								<span>${message("admin.common.order")}</span>
							</th>
							<th>
								<span>${message("admin.common.handle")}</span>
							</th>
						</tr>
						[#list productCategoryTree as productCategory]
						<tr>
							<td>
								<input type="checkbox" name="ids" value="${productCategory.id}" />
							</td>
							<td style="text-align:left;">
								<span style="margin-left: ${productCategory.grade * 20}px;[#if productCategory.grade == 0] color: #000000;[/#if]">
									${abbreviate(productCategory.name,25,"...")}
								</span>
							</td>
							<td>
								${productCategory.id}
							</td>
							<td>
								${productCategory.order}
							</td>
							<td>
								<!--<a href="${base}${productCategory.path}" target="_blank">[${message("admin.common.view")}]</a>-->

								[@helperRole url="helper/member/product_category/list.jhtml" type="update"]
									[#if helperRole.retOper!="0"]
                                        <a href="edit.jhtml?id=${productCategory.id}">[${message("admin.common.edit")}]</a>
									[/#if]
								[/@helperRole]

								[@helperRole url="helper/member/product_category/list.jhtml" type="del"]
									[#if helperRole.retOper!="0"]
                                        <a href="javascript:;" class="delete" val="${productCategory.id}">[${message("admin.common.delete")}]</a>
									[/#if]
								[/@helperRole]
							</td>
						</tr>
						[/#list]
					</table>
					[#if !productCategoryTree?if_exists]
					<p style="text-align:center;border-bottom:solid 1px #c6c9ca;line-height:30px;">${message("helper.member.noResult")}</p>
					[/#if]
				</form>
			</div>
		</div>
	</div>
	[#include "/helper/include/footer.ftl" /]
</body>
</html>
