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
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript"
            src="${base}/resources/store/2.0/plugins/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="${base}/resources/store/2.0/plugins/datatables/dataTables.bootstrap.min.js"></script>
    <style type="text/css">
        #listTable th, #listTable td {
            /*padding-left: 5px;*/
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
            $("#deleteButton").addClass("disabled");
//            $("#listTable").DataTable();
        [@flash_message /]
        });

        function deleteGift(id) {
            $.ajax({
                url: "${base}/store/member/buygifts/delete_gift.jhtml",
                type: "POST",
                dataType: "json",
                data: {
                    id: id
                },
                success: function (message) {
                    if (message.type == "success") {
                        location.reload();
                    }
                }
            });
        }
    </script>

</head>
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                营销工具
                <small>活动商品</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/buygifts/listgift.jhtml">营销工具</a></li>
                <li class="active">买赠搭配</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs pull-right">
                        <li class="active">
                            <a href="${base}/store/member/buygifts/listgift.jhtml">赠品管理</a>
                        </li>
                        <li class="">
                            <a href="${base}/store/member/buygifts/listbuygift.jhtml">活动商品</a>
                        </li>
                        <li class="pull-left header"><i class="fa fa-gift"></i>赠品管理</li>
                    </ul>
                    <div class="row mtb10">
                        <div class="col-sm-7">
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary btn-sm ml5"
                                        onclick="javascript:location.href='${base}/store/member/buygifts/listproduct.jhtml?productCategoryTenantId=${tenantId}&isList=true&isGift=false&type=gift'">
                                    <i
                                            class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
                                </button>
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
                                            id="dropdownMenu1"
                                            data-toggle="dropdown">
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
                </div>
                <div class="box-body">
                    <form id="listForm" action="listgift.jhtml" method="get">
                        <input type="hidden" id="type" name="type" value="${type}"/>
                        <input type="hidden" id="status" name="status" value="${status}"/>
                        <table id="listTable" class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th class="check">
                                    <input type="checkbox" id="selectAll"/>
                                </th>
                                <th>商品名称</th>
                                <th>标价</th>
                                <th>原价</th>
                                <th>总销量</th>
                                <th>库存</th>
                                <th>${message("admin.common.handle")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            [#list page.content as promo]
                            <tr>
                                <td>
                                    <input type="checkbox" name="ids" value="${promo.id}"/>
                                </td>
                                <td>
                                ${promo.fullName}
                                </td>
                                <td>
                                    <del>${currency(promo.marketPrice,true)}</del>
                                </td>
                                <td style="color:red;">
                                ${currency(promo.price,true)}
                                </td>
                                <td>
                                ${promo.sales}
                                </td>
                                <td>
                                    [#if promo.stock??]
                                        [#if promo.allocatedStock == 0]
                                            <span[#if promo.isOutOfStock] class="red"[/#if]>${promo.stock}</span>
                                        [#else]
                                            <span[#if promo.isOutOfStock] class="red"[/#if]
                                                                          title="${message("Product.allocatedStock")}: ${promo.allocatedStock}">${promo.stock}</span>
                                        [/#if]
                                    [/#if]
                                </td>

                                <td>
                                    [@helperRole url="helper/member/buygifts/listgift.jhtml" type="del"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="javascript:deleteGift('${promo.id}') ;">删除</a>
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
                    </form>

                </div>
            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
