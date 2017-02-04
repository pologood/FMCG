<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.consultation.list")}</title>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>



    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

        [@flash_message /]

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
        <img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage3.png"/>
        <dl class="app-info">
            <dt class="app-title" id="app_name">咨询回复</dt>
            <dd class="app-status" id="app_add_status">
            </dd>
            <dd class="app-intro" id="app_desc">快去回复粉丝们咨询的问题，对您的诚信度有很大的帮助。</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <li><a class="on" hideFocus="" href="javascript:;">咨询列表</a></li>
    </ul>

</div>
<form id="listForm" action="manager.jhtml" method="get">
    <input type="hidden" id="type" name="type" value="${type}"/>
    <div class="bar">
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
                [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
                <input placeholder="搜索商品内容" type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
            <div class="popupMenu">
                <ul id="searchPropertyOption">
                    <li>
                        <a href="javascript:;"[#if page.searchProperty == "content"] class="current"[/#if]
                           val="content">${message("Consultation.content")}</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="list" style="border-top: 0;">
        <table id="listTable" class="list">
            <tr>
                <th class="check">
                    <input type="checkbox" id="selectAll"/>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="product">${message("Consultation.product")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="content">${message("Consultation.content")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="member">${message("Consultation.member")}</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="isShow">${message("Consultation.isShow")}</a>
                </th>
                <th>
                    <span>${message("admin.consultation.isReply")}</span>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
                </th>
                <th>
                    <span>${message("admin.common.handle")}</span>
                </th>
            </tr>
        [#list page.content as consultation]
            <tr>
                <td>
                    <input type="checkbox" name="ids" value="${consultation.id}"/>
                </td>
                <td>
                    <a href="${base}/helper/product/content/${consultation.product.id}.jhtml" title="${consultation.product.name}" target="_blank">${abbreviate(consultation.product.name, 50, "...")}</a>
                </td>
                <td>
                    <span title="${consultation.content}">${abbreviate(consultation.content, 50, "...")}</span>
                </td>
                <td>
                    [#if consultation.member??]
							${consultation.member.username}
						[#else]
                    ${message("admin.consultation.anonymous")}
                    [/#if]
                </td>
                <td>
                    <span class="${consultation.isShow?string("true", "false")}Icon">&nbsp;</span>
                </td>
                <td>
                    <span class="${consultation.replyConsultations?has_content?string("true", "false")}Icon">&nbsp;</span>
                </td>
                <td>
                    <span title="${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${consultation.createDate}</span>
                </td>
                <td style="width:100px;">
                    <a href="reply.jhtml?id=${consultation.id}">[${message("admin.consultation.reply")}]</a>
                    <a href="edit.jhtml?id=${consultation.id}">[${message("admin.common.edit")}]</a>
                </td>
            </tr>
        [/#list]
        </table>
    [#if !page.content?has_content]
       <p class="nothing">${message("helper.member.noResult")}</p>
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
