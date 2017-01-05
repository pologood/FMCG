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
[/@seo]    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>


    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>

    <script type="text/javascript">

        $().ready(function () {
            $("#deleteButton").addClass("disabled");
        [@flash_message /]
        });
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
                优惠买单
                <small>管理我的店铺优惠买单。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/bill/discount/list.jhtml">营销工具</a></li>
                <li class="active">优惠买单</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom" style="margin-bottom: 0; box-shadow: none;">
                    <div class="row mtb10">
                        <div class="col-sm-7">
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary btn-sm ml5"
                                        onclick="javascript:location.href='add.jhtml'">
                                    <i
                                            class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
                                </button>
                                <button type="button" id="deleteButton" class="btn btn-default btn-sm"><i
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
                        <div class="col-sm-5">
                            <div class="box-tools fr">
                                <div class="input-group input-group-sm" style="width: 150px;">
                                    <input type="text" id="searchValue" name="searchValue"
                                           value="${page.searchValue}" class="form-control pull-right"
                                           placeholder="搜索优惠买单名称">
                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-body">

                    <form id="listForm" action="list.jhtml" method="get">
                        <table id="listTable" class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th class="check">
                                    <input type="checkbox" id="selectAll"/>
                                </th>
                                <th>名称</th>
                                <th>折扣比例</th>
                                <th>返现比例</th>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>${message("admin.common.handle")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            [#list page.content as promotion]
                            <tr>
                                <td>
                                    <input type="checkbox" name="ids" value="${promotion.id}"/>
                                </td>
                                <td>
                                ${abbreviate(promotion.name,18,"..")}
                                </td>
                                <td>
                                ${promotion.agioRate}%
                                </td>
                                <td>
                                ${promotion.backRate}%
                                </td>
                                <td>
                                    <span title="${promotion.beginDate?string("yyyy-MM-dd HH:mm")}">${promotion.beginDate?string("yyyy-MM-dd HH:mm")}</span>
                                </td>
                                <td>
                                    <span title="${promotion.endDate?string("yyyy-MM-dd HH:mm")}">${promotion.endDate?string("yyyy-MM-dd HH:mm")}</span>
                                </td>
                                <td>
                                    [@helperRole url="helper/member/coupon/list.jhtml" type="statistics"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="${base}/store/member/payBill/discount_total.jhtml">统计</a>
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
