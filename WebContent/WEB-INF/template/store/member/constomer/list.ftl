<!DOCTYPE html>
<html>
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
[#include "/store/member/include/bootstrap_css.ftl" /]
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                客户管理
                <small>尊敬的商家用户，此模块能帮您管理属于您的会员</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/consumer/list.jhtml?status=enable">客户管理</a></li>
                <li class="active">我的会员</li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs pull-right">
                            <li class="">
                                <a href="${base}/store/member/consumer/collect_list.jhtml">关注我的人</a>
                            </li>
                            <li class="[#if status==null]active[/#if]">
                                <a href="${base}/store/member/consumer/nearby.jhtml">附近的人</a>
                            </li>
                            <li class="[#if status=='enable']active[/#if]">
                                <a href="${base}/store/member/consumer/list.jhtml?status=enable">我的会员</a>
                            </li>
                            <li class="pull-left header"><i class="fa fa-user"></i>我的会员</li>
                        </ul>
                        <div class="tab-content" style="padding:15px 0 0 0;">
                            <form id="listForm" action="list.jhtml" method="get">
                                <input type="hidden" id="status" name="status" value="${status}"/>
                                <input type="hidden" id="memberRank" name="memberRank" value="${memberRank}"/>
                                <div class="row mtb10">
                                    <div class="col-sm-7">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-default btn-sm" id="deleteButton"><i
                                                    class="fa fa-close mr5"
                                                    aria-hidden="true"></i>删除
                                            </button>
                                            <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
                                                    class="fa fa-refresh mr5"
                                                    aria-hidden="true"></i> 刷新
                                            </button>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1" data-toggle="dropdown">
                                                    级别<span class="caret"></span>
                                                </button>
                                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                                    id="statusOption">
                                                    <li class="[#if memberRank == null]active[/#if]">
                                                        <a href="javascript:;"[#if memberRank == null]
                                                           class="current"[/#if]
                                                           val="">全部</a>
                                                    </li>
                                                    <li class="[#if memberRank == "1"]active[/#if]">
                                                        <a href="javascript:;"[#if memberRank == "1"]
                                                           class="current"[/#if]
                                                           val="1">普通会员</a>
                                                    </li>
                                                    <li class="[#if memberRank == "2"]active[/#if]">
                                                        <a href="javascript:;"[#if memberRank == "2"]
                                                           class="current"[/#if]
                                                           val="2">VIP1</a>
                                                    </li>
                                                    <li class="[#if memberRank == "3"]active[/#if]">
                                                        <a href="javascript:;"[#if memberRank == "3"]
                                                           class="current"[/#if]
                                                           val="3">VIP2</a>
                                                    </li>
                                                    <li class="[#if memberRank == "4"]active[/#if]">
                                                        <a href="javascript:;"[#if memberRank == "4"]
                                                           class="current"[/#if]
                                                           val="4">VIP3</a>
                                                    </li>
                                                    <li class="[#if memberRank == "5"]active[/#if]">
                                                        <a href="javascript:;"[#if memberRank == "5"]
                                                           class="current"[/#if]
                                                           val="5">VIP4</a>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1" data-toggle="dropdown">
                                                    每页显示<span class="caret"></span>
                                                </button>
                                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                                    id="pageSizeOption">
                                                    <li role="presentation" class="[#if page.pageSize==10]active[/#if]">
                                                        <a role="menuitem" tabindex="-1" val="10">10</a>
                                                    </li>
                                                    <li role="presentation" class="[#if page.pageSize==20]active[/#if]">
                                                        <a role="menuitem" tabindex="-1" val="20">20</a>
                                                    </li>
                                                    <li role="presentation" class="[#if page.pageSize==30]active[/#if]">
                                                        <a role="menuitem" tabindex="-1" val="30">30</a>
                                                    </li>
                                                    <li role="presentation" class="[#if page.pageSize==40]active[/#if]">
                                                        <a role="menuitem" tabindex="-1" val="40">40</a>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-5">
                                        <div class="box-tools fr">
                                            <div class="input-group input-group-sm" style="width: 150px;">
                                                <input type="text" class="form-control pull-right" id="keyword"
                                                       name="keyword" value="${keyword}"
                                                       placeholder="搜索会员名称">
                                                <ul id="searchPropertyOption" style="display:none;">
                                                    <li>
                                                        <a style="cursor: pointer;" val="name"></a>
                                                    </li>
                                                </ul>
                                                <div class="input-group-btn">
                                                    <button type="submit" class="btn btn-default"><i
                                                            class="fa fa-search"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="box" style="border-top:0px;">
                                    <div class="box-body">
                                        <table id="listTable" class="table table-bordered table-striped">
                                            <thead>
                                            <tr>
                                                <th class="check">
                                                    <input type="checkbox" id="selectAll"/>
                                                </th>
                                                <th>会员名称</th>
                                                <th>会员性别</th>
                                                <th>联系电话</th>
                                                <th>地址</th>
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
                                                <th>
                                                    <span>${message("admin.common.handle")}</span>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
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
                                                    <span title="${consumer.address}">${abbreviate(consumer.address,30,"..")}</span>
                                                </td>
                                                [#if status=="enable"]
                                                    <td>
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
                                                <td>
                                                    [@helperRole url="helper/member/consumer/list.jhtml" type="supervise"]
                                                        [#if helperRole.retOper!="0"]
                                                            <a href="edit.jhtml?id=${consumer.id}&status=${consumer.status}">管理</a>
                                                        [/#if]
                                                    [/@helperRole]
                                                </td>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                        <div class="dataTables_paginate paging_simple_numbers">
                                        [#if !page.content?has_content]
                                            <p class="nothing">${message("box.member.noResult")}</p>
                                        [/#if]
                                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                                            [#include "/store/member/include/pagination.ftl"]
                                        [/@pagination]
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>
[#include "/store/member/include/bootstrap_js.ftl" /]
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript">
    $().ready(function () {
        var $listForm = $("#listForm");
        var $status = $("#status");
        var $statusSelect = $("#statusSelect");
        var $statusOption = $("#statusOption a");
        $("#deleteButton").addClass("disabled");

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
</body>
</html>
