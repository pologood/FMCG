<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>我的供应商</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
[#include "/store/member/include/bootstrap_css.ftl"]
    <link rel="stylesheet" href="${base}/resources/store/css/style.css">
    <link rel="stylesheet" href="${base}/resources/store/css/common.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>供应商
                <small>管理我的供应商</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/relation/parent.jhtml">客户管理</a></li>
                <li class="active">我的供应商</li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs pull-right">
                            <li class="pull-left header"><i class="fa fa-archive"></i>我的供应商</li>
                        </ul>
                        <div class="tab-content" style="padding:15px 0 0 0;">
                            <form id="listForm" action="parent.jhtml" method="get">
                                <input type="hidden" id="status" name="status" value="${status}"/>
                                <div class="row mtb10">
                                    <div class="col-sm-7">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-primary btn-sm"
                                                    onclick="javascript:location.href='addParent.jhtml';">
                                                <i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
                                            </button>
                                            <button type="button" class="btn btn-default btn-sm" id="deleteButton"><i
                                                    class="fa fa-close mr5"
                                                    aria-hidden="true"></i>删除
                                            </button>
                                            <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
                                                    class="fa fa-refresh mr5" aria-hidden="true"></i> 刷新
                                            </button>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1"
                                                        data-toggle="dropdown">状态<span class="caret"></span></button>
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
                                                <input placeholder="搜索供应商名称" type="text" id="searchValue"
                                                       name="searchValue" class="form-control pull-right"
                                                       value="${page.searchValue}" maxlength="200"/>
                                                <div class="input-group-btn">
                                                    <button type="submit" class="btn btn-default"><i
                                                            class="fa fa-search"></i></button>
                                                </div>
                                                <div class="hidden">
                                                    <ul id="searchPropertyOption">
                                                        <li>
                                                            <a href="javascript:;"[#if page.searchProperty == "parent.name"]
                                                               class="current"[/#if]
                                                               val="parent.name">客户名称</a>
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
                                                <th>供应商名称</th>
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
                                                    [#list tenantRelation.status as status]
                                                        [#if status=='success']
                                                            已审核
                                                        [#elseif status=='none']
                                                            未审核
                                                        [#elseif status=='fail']
                                                            已驳回
                                                        [/#if]
                                                    [/#list]
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
