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
                $("#memberRank").val($this.attr("val"));
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/user_icon.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的会员</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">尊敬的商家用户，此模块能帮您管理属于您的会员</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">

                [@helperRole url="helper/member/consumer/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a [#if status=='enable']class="on"[/#if] hideFocus="" href="javascript:;"
                               onclick="roleRoot('list.jhtml?status=enable','owner,manager')">我的会员</a></li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/consumer/nearby.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a [#if status==null]class="on"[/#if] hideFocus="" href="javascript:;"
                               onclick="roleRoot('nearby.jhtml','owner,manager,guide,account,cashier')">附近的人</a></li>
                    [/#if]
                [/@helperRole]

                [@helperRole url="helper/member/consumer/collect_list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a [#if status=='none']class="on"[/#if] hideFocus="" href="javascript:;"
                               onclick="roleRoot('collect_list.jhtml','owner,manager,guide,account,cashier')">关注我的人</a>
                        </li>
                    [/#if]
                [/@helperRole]
                </ul>

            </div>


            <form id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="status" name="status" value="${status}"/>
                <input type="hidden" id="memberRank" name="memberRank" value="${memberRank}"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/consumer/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]

                        <a href="javascript:;" id="refreshButton" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                    [#if status=='enable']
                        <div class="menuWrap">
                            <a href="javascript:;" id="statusSelect" class="button">
                                级别<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="statusOption">
                                    <li>
                                        <a href="javascript:;"[#if memberRank == null] class="current"[/#if]
                                           val="">全部</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if memberRank == "1"] class="current"[/#if]
                                           val="1">普通会员</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if memberRank == "2"] class="current"[/#if]
                                           val="2">VIP1</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if memberRank == "3"] class="current"[/#if]
                                           val="3">VIP2</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if memberRank == "4"] class="current"[/#if]
                                           val="4">VIP3</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if memberRank == "5"] class="current"[/#if]
                                           val="5">VIP4</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    [/#if]
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
                            <input placeholder="搜索会员名称" type="text" id="keyword" name="keyword" value="${keyword}"
                                   maxlength="200"/>
                            <button type="submit">&nbsp;</button>
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
                                会员名称
                            </th>
                            <th>
                                会员性别
                            </th>
                            <th>
                                联系电话
                            </th>
                            <th>
                                地址
                            </th>
                        [#if status=="enable"]
                            <th>
                                发展者
                            </th>
                        [/#if]
                            <th>
                                最近登录时间
                            </th>
                        [#if status=='enable']
                            <th>
                                <a href="javascript:;" class="sort" name="memberRank">级别</a>
                            </th>
                        [/#if]
                        [#--<th>--]
                        [#--<a href="javascript:;" class="sort" name="createDate">申请时间</a>--]
                        [#--</th>--]
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as consumer]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${consumer.consumerId}"/>
                            </td>
                            <td>
                            ${consumer.nickName}
                            </td>
                            <td>
                                [#if consumer.gender!=""]
                            ${(message("Member.Gender."+consumer.gender))!}
                                [/#if]
                            </td>
                            <td>
                            ${consumer.mobile}
                            </td>
                            <td>
                                <span title="${consumer.address}">${abbreviate(consumer.address,20,"..")}</span>
                            </td>
                            [#if status=="enable"]
                                <td>
                                [#--[#if consumer.distance>1000000]--]
                                [#--1000km外--]
                                [#--[#elseif (consumer.distance>=1000)]--]
                                [#--${(consumer.distance/1000)?string('#.##')}km--]
                                [#--[#else]--]
                                [#--${consumer.distance?string('#.##')}米--]
                                [#--[/#if]--]
                                ${(consumer.developer.name)!}
                                    [#list consumer.role?split(",") as role]
                                        [#if role=="owner"]店主[#if role_has_next],[/#if][/#if]
                                        [#if role=="manager"]店长[#if role_has_next],[/#if][/#if]
                                        [#if role=="guide"]导购[#if role_has_next],[/#if][/#if]
                                        [#if role=="account"]财务[#if role_has_next],[/#if][/#if]
                                        [#if role=="cashier"]收银[#if role_has_next],[/#if][/#if]
                                    [/#list]
                                </td>
                            [/#if]
                            <td>
                                [#if consumer.modify_date!=""]
                        ${consumer.modify_date?string("yyyy-MM-dd HH:mm")}
                    [/#if]
                            </td>
                            [#if status=='enable']
                                <td>
                                ${consumer.memberRank.name}
                                </td>
                            [/#if]
                        [#--<td>--]
                        [#--<span title="${consumer.createDate?string("yyyy-MM-dd HH:mm:ss")}">${consumer.createDate}</span>--]
                        [#--</td>--]
                            <td>
                                [@helperRole url="helper/member/consumer/list.jhtml" type="supervise"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="edit.jhtml?id=${consumer.id}&status=${consumer.status}">管理</a>
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
