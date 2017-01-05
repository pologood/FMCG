<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title>${message("admin.review.list")}</title>
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
		<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet" />
		<link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet" />

		<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
		<script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
		<script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
        <script src="${base}/resources/helper/js/amazeui.min.js"></script>
		<script type="text/javascript">
			$().ready(function() {

				var $listForm = $("#listForm");
				var $type = $("#type");
				var $typeSelect = $("#typeSelect");
				var $typeOption = $("#typeOption a");

				[@flash_message /]

				$typeSelect.mouseover(function() {
					var $this = $(this);
					var offset = $this.offset();
					var $menuWrap = $this.closest("div.menuWrap");
					var $popupMenu = $menuWrap.children("div.popupMenu");
					$popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
					$menuWrap.mouseleave(function() {
						$popupMenu.hide();
					});
				});

				$typeOption.click(function() {
					var $this = $(this);
					$type.val($this.attr("val"));
					$listForm.submit();
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
				<img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage4.png"/>
				<dl class="app-info">
					<dt class="app-title" id="app_name">商品评论</dt>
					<dd class="app-status" id="app_add_status"></dd>
					<dd class="app-intro" id="app_desc">快去回复粉丝们咨询的问题，对您的诚信度有很大的帮助。</dd>
				</dl>
			</div>
			<ul class="links" id="mod_menus">
				<li><a class="on" hideFocus="" href="${base}/helper/member/review/manager.jhtml">评论列表</a></li>
			</ul>
		</div>
		<form id="listForm" action="manager.jhtml" method="get">
			<input type="hidden" id="type" name="type" value="${type}" />
			<div class="bar">
				<div class="buttonWrap">
				<!--<a href="javascript:;" id="deleteButton" class="iconButton disabled">
				<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>-->
					<a href="javascript:;" id="refreshButton" class="iconButton">
						<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
					</a>
					<div class="menuWrap">
						<a href="javascript:;" id="typeSelect" class="button">
							${message("admin.review.type")}<span class="arrow">&nbsp;</span>
						</a>
						<div class="popupMenu">
							<ul id="typeOption">
								<li>
									<a href="javascript:;"[#if type == null] class="current"[/#if] val="">${message("admin.review.allType")}</a>
								</li>
								[#assign currentType = type]
								[#list types as type]
								<li>
									<a href="javascript:;"[#if type == currentType] class="current"[/#if] val="${type}">${message("Review.Type." + type)}</a>
								</li>
								[/#list]
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
						<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}"  placeholder="搜索商品名称、会员" />
						<button type="submit">&nbsp;</button>
					</div>
				</div>
			</div>
			<div class="list" >
				<table id="listTable" class="list">
					<tr>
						<th class="check">
							<input type="checkbox" id="selectAll" />
						</th>
						<th>
							<a href="javascript:;" class="sort" name="product">${message("Review.product")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="score">${message("Review.score")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="content">${message("Review.content")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="member">${message("Review.member")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="isShow">${message("Review.isShow")}</a>
						</th>
						<th>
							<a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
						</th>
						<th width="60px">
							<span>${message("admin.common.handle")}</span>
						</th>
					</tr>
					[#list page.content as review]
					<tr>
						<td>
							<input type="checkbox" name="ids" value="${review.id}" />
						</td>
						<td>
							<a href="${base}/helper/product/content/${review.product.id}.jhtml" title="${review.product.name}" target="_blank">${abbreviate(review.product.name, 50, "...")}</a>
						</td>
						<td>${review.score}</td>
						<td>
							<span title="${review.content}">${abbreviate(review.content, 50, "...")}</span>
						</td>
						<td>
							[#if review.member??]
							${review.member.username}
							[#else]
							${message("admin.review.anonymous")}
							[/#if]
						</td>
						<td>
							<span class="${review.isShow?string("true", "false")}Icon">&nbsp;</span>
						</td>
						<td>
							<span title="${review.createDate?string("yyyy-MM-dd HH:mm:ss")}">${review.createDate}</span>
						</td>
						<td>
							[@helperRole url="helper/member/review/manager.jhtml" type="update"]
								[#if helperRole.retOper!="0"]
                                    <a href="edit.jhtml?id=${review.id}">[${message("admin.common.edit")}]</a>
								[/#if]
							[/@helperRole]
						</td>
					</tr>
					[/#list]
				</table>
				[#if !page.content?has_content]
				<p>${message("shop.member.noResult")}</p>
				[/#if]
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
				[#include "/helper/include/pagination.ftl"]
				[/@pagination]
			</div>
		</form>
</div>
			</div>
		</div>
		[#include "/helper/include/footer.ftl" /]
	</body>
</html>
