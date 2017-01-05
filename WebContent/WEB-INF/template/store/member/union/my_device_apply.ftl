<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>全部商盟</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
[#include "/store/member/include/bootstrap_css.ftl"]
    <link rel="stylesheet" href="${base}/resources/store/css/style.css">
    <link rel="stylesheet" href="${base}/resources/store/css/common.css"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/header.ftl"]
    <!-- Left side column. contains the logo and sidebar -->
[#include "/store/member/include/menu.ftl"]
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>购物屏联盟
                <small>管理我的购物屏联盟</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/union/all_union.jhtml">购物屏联盟</a></li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- Left col -->
                <div class="col-md-12">
                    <!-- Custom Tabs (Pulled to the right) -->
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs pull-right">
                            <li class="active"><a href="${base}/store/member/union/my_device_apply.jhtml">投放我的</a></li>
                            <li class=""><a href="${base}/store/member/union/invest_device_apply.jhtml">我投放的</a></li>
                            <li class="pull-left header"><i class="fa fa-th"></i>投放我的申请</li>
                        </ul>
                        <div class="tab-content" style="padding:15px 0 0 0;">
                            <form class="form-horizontal" id="listForm" action="my_device_apply.jhtml" method="get"
                                  role="form">
                                <input type="hidden" id="status" name="status" value="${status}"/>
                                <div class="row mtb10">
                                    <div class="col-sm-12">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-default btn-sm"
                                                    onclick="javascript:location.reload();">
                                                <i class="fa fa-refresh mr5"></i> 刷新
                                            </button>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1"
                                                        data-toggle="dropdown">
                                                    状态
                                                    <span class="caret"></span>
                                                </button>
                                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                                    id="typeOption">
                                                [#assign currentType = status]
                                                [#list statuses as status]
                                                    <li class="[#if status == currentType]active[/#if]">
                                                        <a href="javascript:;"[#if status == currentType] class="current"[/#if]
                                                           val="${status}">${message("UnionTenant.Status."+status)}</a>
                                                    </li>
                                                [/#list]
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
                                </div>
                                <div class="box" style="border-top:0px;">
                                    <div class="box-body">
                                        <table id="listTable" class="table table-bordered table-hover">
                                            <thead>
                                            <tr>
                                                <th>商家名称</th>
                                                <th>地址</th>
                                                <th>佣金</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            [#list page.content as tenant]
                                            <tr>
                                                <td>${tenant.name}</td>
                                                <td>${tenant.address}</td>
                                                <td>${tenant.brokerage}</td>
                                                <td>
                                                    [#if status=="unconfirmed"]
                                                        <input class="btn btn-info" type="button" value="同意" onclick="confirmApply(${tenant.id})" >
                                                        <input class="btn btn-info" type="button" value="拒绝" onclick="refuseApply(${tenant.id})" >
                                                    [#elseif status=="freezed"]
                                                        <input class="btn btn-info" type="button" value="已拒绝"  >
                                                    [#elseif status=="confirmed"]
                                                        <input class="btn btn-info" type="button" value="已同意" " >
                                                    [#elseif status=="canceled"]
                                                        <input class="btn btn-info" type="button" value="已取消" " >
                                                    [/#if]
                                                </td>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                        <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/store/member/include/pagination.ftl"]
                        [/@pagination]
                                        </div>
                                    </div>

                                    <!-- /.box-body -->
                                </div>
                                <!-- /.tab-pane -->
                            </form>
                        </div>
                        <!-- /.tab-content -->
                    </div>
                    <!-- nav-tabs-custom -->
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
[#include "/store/member/include/footer.ftl"]
</div>
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript">
    $().ready(function () {
        var $listForm = $("#listForm");
        var $type = $("#status");
        var $typeSelect = $("#typeSelect");
        var $typeOption = $("#typeOption a");

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

        $typeOption.click(function () {
            var $this = $(this);
            $type.val($this.attr("val"));
            $listForm.submit();
            return false;
        });
    });
    function confirmApply(uTenantId){
        $.ajax({
            url: '${base}/store/member/union/confirm_apply.jhtml',
            data:{
                uTenantId:uTenantId
            },
            type:'post',
            dataType:'json',
            success:function(data){
                if(data.type=="success"){
                    $.message("success","确认成功");
                    location.href = "${base}/store/member/union/my_device_apply.jhtml";
                }else{
                    $.message("error","确认失败");
                }
            }
        });
    }
    function refuseApply(uTenantId){
        $.ajax({
            url: '${base}/store/member/union/refuse_apply.jhtml',
            data:{
                uTenantId:uTenantId
            },
            type:'post',
            dataType:'json',
            success:function(data){
                if(data.type=="success"){
                    $.message("success","拒绝成功");
                    location.href = "${base}/store/member/union/my_device_apply.jhtml";
                }else{
                    $.message("error","拒绝失败");
                }
            }
        });
    }
</script>
</body>
</html>
