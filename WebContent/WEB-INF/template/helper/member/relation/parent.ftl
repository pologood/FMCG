<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
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
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">

        $().ready(function () {

            var $listForm = $("#listForm");
            var $status = $("#status");
            var $statusSelect = $("#statusSelect");
            var $statusOption = $("#statusOption a");

        [@flash_message /]

            $statusSelect.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });

            $statusOption.click(function () {
                var $this = $(this);
                $status.val($this.attr("val"));
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage2.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的供应商</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">管理我的供应商</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/relation/parent.jhtml">我的供应商</a></li>
                </ul>

            </div>
            <form id="listForm" action="parent.jhtml" method="get">
                <input type="hidden" id="status" name="status" value="${status}"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/relation/parent.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="${base}/helper/member/relation/addParent.jhtml" class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]
                    [@helperRole url="helper/member/relation/parent.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="javascript:;" class="iconButton" id="refreshButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="statusSelect" class="button">
                                状态<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="statusOption">
                                    <li>
                                        <a href="javascript:;"[#if status == null] class="current"[/#if] val="">全部</a>
                                    </li>
                                [#list relationStatuss as relationStatus]
                                    <li>
                                        <a href="javascript:;"[#if relationStatus == status] class="current"[/#if]
                                           val="${status}">${message("Relation.Status." + relationStatus)}</a>
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
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <div class="search">
                        [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
                            <input placeholder="搜索供应商名称" type="text" id="searchValue" name="searchValue"
                                   value="${page.searchValue}" maxlength="200"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                        <div class="popupMenu">
                            <ul id="searchPropertyOption">
                                <li>
                                    <a href="javascript:;"[#if page.searchProperty == "parent.name"]
                                       class="current"[/#if]
                                       val="parent.name">供应商名称</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th>
                                供应商名称
                            </th>
                            <th>
                                联系人
                            </th>
                            <th>
                                联系电话
                            </th>
                            <th>
                                地址
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="status">状态</a>
                            </th>
                            <th>
                                级别
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="createDate">申请时间</a>
                            </th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as tenantRelation]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${tenantRelation.id}"/>
                            </td>
                            <td>
                            ${abbreviate(tenantRelation.parent.name,18,"..")}
                            </td>
                            <td>
                            ${abbreviate(tenantRelation.parent.linkman,18,"..")}
                            </td>
                            <td>
                            ${tenantRelation.parent.telephone}
                            </td>
                            <td>
                            ${abbreviate(tenantRelation.parent.address,20,"..")}
                            </td>
                            <td>
                            ${message("Relation.Status." + tenantRelation.status)}
                            </td>
                            <td>
                            ${tenantRelation.parent.member.memberRank.name}
                            </td>
                            <td>
                                <span title="${tenantRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${tenantRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                            </td>
                            <td>

                                [@helperRole url="helper/member/relation/parent.jhtml" type="supervise"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="editParent.jhtml?id=${tenantRelation.id}">管理</a>
                                    [/#if]
                                [/@helperRole]
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
