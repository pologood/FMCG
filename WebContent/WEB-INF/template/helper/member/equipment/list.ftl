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
            var $type = $("#type");
            var $status = $("#status");
            var $typeSelect = $("#typeSelect");
            var $statusSelect = $("#statusSelect");
            var $typeOption = $("#typeOption a");
            var $statusOption = $("#statusOption a");

        [@flash_message /]

            $typeSelect.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });

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

            $typeOption.click(function () {
                var $this = $(this);
                $type.val($this.attr("val"));
                $listForm.submit();
                return false;
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/screen.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">购物屏管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">购物屏列表</dd>
                    </dl>
                </div>
            [#--<ul class="links" id="mod_menus">--]
            [#--<li><a class="on" hideFocus="" href="${base}/helper/member/relation/parent.jhtml">我的供应商</a></li>--]
            [#--</ul>--]
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <input type="hidden" id="status" name="status" value="${status}"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [#--<a href="${base}/helper/member/relation/addParent.jhtml" class="iconButton">--]
                    [#--<span class="addIcon">&nbsp;</span>${message("admin.common.add")}--]
                    [#--</a>--]
                    [#--<a href="javascript:;" id="deleteButton" class="iconButton disabled">--]
                    [#--<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}--]
                    [#--</a>--]
                        <a href="javascript:;" class="iconButton" id="refreshButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="typeSelect" class="button">
                                类型<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="typeOption">
                                    <li>
                                        <a href="javascript:;"[#if type=='tenant'] class="current"[/#if]
                                           val="tenant">本店的屏</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if type == "store"] class="current"[/#if]
                                           val="store">他店的屏</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    [#--<div class="menuWrap">--]
                    [#--<a href="javascript:;" id="statusSelect" class="button">--]
                    [#--状态<span class="arrow">&nbsp;</span>--]
                    [#--</a>--]
                    [#--<div class="popupMenu">--]
                    [#--<ul id="statusOption">--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if status == null] class="current"[/#if] val="">全部</a>--]
                    [#--</li>--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if status=='success'] class="current"[/#if]--]
                    [#--val="success">已开启</a>--]
                    [#--</li>--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if status == "none"] class="current"[/#if]--]
                    [#--val="none">未开启</a>--]
                    [#--</li>--]
                    [#--</ul>--]
                    [#--</div>--]
                    [#--</div>--]
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
                            <input placeholder="搜索名称" type="text" id="keyWord" name="keyWord"
                                   value="${keyWord}" maxlength="200"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list" style="table-layout: fixed;">
                        <tr>
                            <th class="check" style="width: 30px;">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th width="100px">
                                <span>地区</span>
                            </th>
                            <th width="200px">
                                <span>名称</span>
                            </th>
                            <th width="100px">
                                <span>标签</span>
                            </th>
                            <th>
                                <span>地址</span>
                            </th>
                            <th width="100px">
                                <span>状态</span>
                            </th>
                            <th width="100px">
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as equipment]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${equipment.id}"/>
                            </td>
                            <td>
                            ${equipment.areaName}
                            </td>
                            <td>
                                <span title="${equipment.tenantName}">${abbreviate(equipment.tenantName,25,"..")}</span>
                            </td>
                            <td>
                                [#list equipment.tags as tag]
                                ${tag.name}[#if tag_has_next],[/#if]
                                [/#list]
                            </td>
                            <td>
                                <span title="${equipment.address}">${abbreviate(equipment.address,40,"..")}</span>
                            </td>
                            <td>
                                [#if equipment.setStatus=="success"]已开启[#else]未开启[/#if]
                            </td>
                            <td>
                                [@helperRole url="helper/member/equipment/list.jhtml" type="read"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="javascript:;"
                                           onclick="roleRoot('detail.jhtml?id=${equipment.id}&type=${type}','owner,manager');">详情</a>
                                    [/#if]
                                [/@helperRole]
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("box.member.noResult")}</p>
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
