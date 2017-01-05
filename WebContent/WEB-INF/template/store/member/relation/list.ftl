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
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content-header">
            <h1>客户管理
                <small>管理关注我的买家，根据业务关系设置他们的vip等级，分级显示价格</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/relation/list.jhtml">客户管理</a></li>
                <li class="active">我的客户</li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs pull-right">
                            <li class="pull-left header"><i class="fa fa-truck"></i>我的客户</li>
                        </ul>
                        <div class="tab-content" style="padding:15px 0 0 0;">
                            <form id="listForm" action="list.jhtml" method="get">
                                <input type="hidden" id="status" name="status" value="${status}"/>
                                <div class="row mtb10">
                                    <div class="col-sm-7">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-default btn-sm" id="deleteButton">
                                                <i class="fa fa-close mr5" aria-hidden="true"></i>删除
                                            </button>
                                            <button type="button" class="btn btn-default btn-sm" id="refreshButton">
                                                <i class="fa fa-refresh mr5" aria-hidden="true"></i> 刷新
                                            </button>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1" data-toggle="dropdown">状态<span
                                                        class="caret"></span>
                                                </button>
                                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                                    id="statusOption">
                                                    <li class="[#if status == null]active[/#if]">
                                                        <a href="javascript:;"[#if status == null] class="current"[/#if]
                                                           val="">全部</a>
                                                    </li>
                                                [#assign currentstatus = status]
                                                [#list statuss as status]
                                                    <li class="[#if currentstatus == status]active[/#if]">
                                                        <a href="javascript:;"[#if currentstatus == status]
                                                           class="current"[/#if]
                                                           val="${status}">${message("Relation.Status." + status)}</a>
                                                    </li>
                                                [/#list]
                                                </ul>
                                            </div>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1" data-toggle="dropdown">每页显示<span
                                                        class="caret"></span>
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
                                                <input placeholder="搜索客户名称" type="text" id="searchValue"
                                                       name="searchValue" class="form-control pull-right"
                                                       value="${page.searchValue}" maxlength="200"/>
                                                <div class="input-group-btn">
                                                    <button type="submit" class="btn btn-default"><i
                                                            class="fa fa-search"></i></button>
                                                </div>
                                                <div class="hidden">
                                                    <ul id="searchPropertyOption">
                                                        <li>
                                                            <a href="javascript:;"[#if page.searchProperty == "tenant.name"]
                                                               class="current"[/#if]
                                                               val="tenant.name">客户名称</a>
                                                        </li>
                                                    </ul>
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
                                                <th>客户名称</th>
                                                <th>联系人</th>
                                                <th>联系电话</th>
                                                <th>地址</th>
                                                <th>状态</th>
                                                <th>级别</th>
                                                <th>申请时间</th>
                                                <th>${message("admin.common.handle")}</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list page.content as tenantRelation]
                                            <tr>
                                                <td>
                                                    <input type="checkbox" name="ids" value="${tenantRelation.id}"/>
                                                </td>
                                                <td>
                                                ${abbreviate(tenantRelation.tenant.name,18,"..")}
                                                </td>
                                                <td>
                                                ${abbreviate(tenantRelation.tenant.linkman,18,"..")}
                                                </td>
                                                <td>
                                                ${tenantRelation.tenant.telephone}
                                                </td>
                                                <td>
                                                ${abbreviate(tenantRelation.tenant.address,20,"..")}
                                                </td>
                                                <td>
                                                ${message("Relation.Status." + tenantRelation.status)}
                                                </td>
                                                <td>
                                                ${tenantRelation.tenant.member.memberRank.name}
                                                </td>
                                                <td>
                                                    <span title="${tenantRelation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${tenantRelation.createDate}</span>
                                                </td>
                                                <td>

                                                    [@helperRole url="helper/member/relation/list.jhtml" type="supervise"]
                                                        [#if helperRole.retOper!="0"]
                                                            <a href="edit.jhtml?id=${tenantRelation.id}">管理</a>
                                                        [/#if]
                                                    [/@helperRole]
                                                </td>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                    [#if !page.content?has_content]
                                        <p class="nothing">${message("box.member.noResult")}</p>
                                    [/#if]
                                        <div class="dataTables_paginate paging_simple_numbers">
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
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>
[#include "/store/member/include/bootstrap_js.ftl" /]
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
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
            $status.val($this.attr("val"));
            $listForm.submit();
            return false;
        });
    });
</script>
</body>
</html>
