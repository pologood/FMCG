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
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
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

            <!-- <div class="con-con"> -->
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">员工管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">员工信息列表。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">

                [@helperRole url="helper/member/tenant/employee/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="on" hideFocus="" href="${base}/helper/member/tenant/employee/list.jhtml">员工管理</a>
                        </li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/role/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus="" href="${base}/helper/member/role/list.jhtml">角色管理</a></li>
                    [/#if]
                [/@helperRole]
                [#--<li><a class="" hideFocus=""  href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]

                </ul>
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/tenant/employee/list.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="add.jhtml" class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]
                    [@helperRole url="helper/member/tenant/employee/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]

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
                            <input type="text" name="keyWord" value="${keyWord}"
                                   maxlength="200" placeholder="搜索用户名"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table class="list" id="listTable">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll">
                            </th>
                            <th>注册日期</th>
                            <th>用户名</th>
                            <th>员工姓名</th>
                            <th>余额</th>
                            <th>积分</th>
                            <th>角色</th>
                            <th>所属门店</th>
                            <th>最近登录时间</th>
                            <th>状态</th>
                            <th><span>操作</span></th>
                        </tr>
                    [#list page.content as employee ]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${employee.id}"/>
                            </td>
                            <td>
                                <span title="${employee.createDate?string("yyyy-MM-dd HH:mm:ss")}">${employee.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                            </td>
                            <td>
                            ${employee.member.username}
                            </td>
                            <td>
                            ${employee.member.name}
                            </td>
                            <td>
                            ${currency(employee.member.balance, true)}
                            </td>
                            <td>
                            ${employee.member.point}
                            </td>
                            <td>
                            [#--${employee.role}---]
                                [#list roles as rl]
                                    [#if employee.role?exists]
                                        [#list employee.role?split(",") as fileName]
                                            [#if fileName!=""]
                                                [#if fileName?contains(rl.id.toString())]${rl.name}&nbsp;[/#if]
                                            [/#if]
                                        [/#list]
                                    [/#if]
                                [#--[#if employee.role?contains(rl.id.toString())]${rl.name}&nbsp;[/#if]--]
                                [/#list]
                            </td>
                            <td>
                            ${(employee.deliveryCenter.name)!}
                            </td>
                            <td>
                                [#if employee.member.loginDate!=null]${employee.member.loginDate?string("yyyy-MM-dd HH:mm:ss")!"-"}[/#if]
                            </td>
                            <td style="border-right: 0px">
                                [#if member.isEnabled ]
                                    正常
                                [#else]
                                    禁用
                                [/#if]
                            </td>
                            <td>
                                [@helperRole url="helper/member/tenant/employee/list.jhtml" type="update"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="javascript:;"
                                           onclick="roleRoot('edit.jhtml?id=${employee.id}','owner')">编辑</a>
                                    [/#if]
                                [/@helperRole]
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("box.member.noResult")}</p>
                [/#if]
                </div>
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
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
