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
    <style type="text/css">
        table.list th{
            text-align: center;
        }
        table.list td{
            text-align: center;
            margin-left: 0px;
        }
    </style>
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
                        <dt class="app-title" id="app_name">关注我的人</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">尊敬商家用户，此模块能帮你查看多少人关注收藏了该店铺</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                [@helperRole url="helper/member/consumer/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a hideFocus="" href="javascript:;"
                               onclick="roleRoot('list.jhtml?status=enable','owner,manager')">我的会员</a></li>
                    [/#if]
                [/@helperRole]

                [@helperRole url="helper/member/consumer/nearby.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a hideFocus="" href="javascript:;"
                               onclick="roleRoot('nearby.jhtml','owner,manager,guide,account,cashier')">附近的人</a></li>
                    [/#if]
                [/@helperRole]

                [@helperRole url="helper/member/consumer/collect_list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="on" hideFocus="" href="javascript:;"
                               onclick="roleRoot('list.jhtml?status=none','owner,manager,guide,account,cashier')">关注我的人</a>
                        </li>
                    [/#if]
                [/@helperRole]

                </ul>
            </div>
            <form id="listForm" action="collect_list.jhtml" method="get">
                <div class="bar">
                    <div class="buttonWrap">
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
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <div class="search">
                            <input placeholder="搜索会员手机号" type="text" id="keyword" name="keyword" value=""
                                   maxlength="200"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th>
                                用户名称
                            </th>
                            <th>
                                用户性别
                            </th>
                            <th>
                                联系电话
                            </th>
                            <th>
                                联系地址
                            </th>
                            <th>
                                最近登录时间
                            </th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                        [#list page.content as member]
                        <tr>
                            <td>
                            ${member.name}
                            </td>
                            <td>
                                [#if member.gender=="male"]
                                    男
                                [#else]
                                    女
                                [/#if]
                            </td>
                            <td>
                            ${member.username}
                            </td>
                            <td>
                                <span title="${member.address}">${abbreviate(member.address,20,"..")}</span>
                            </td>
                            <td>
                                [#if member.modifyDate??]
                                    ${member.modifyDate?string("yyyy-MM-dd HH:mm")}
                                [/#if]
                            </td>
                            <td>
                                [@helperRole url="helper/member/consumer/collect_list.jhtml" type="supervise"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="edit.jhtml?id=${member.id}&status=none">管理</a>
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
