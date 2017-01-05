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
    [#--<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>--]
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <style type="text/css">
        #listForm tr th {
            text-align: center;
        }

        #listForm tr td {
            text-align: center;
        }
    </style>
    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            $("#export_ss").click(function () {
                $.message("success", "正在帮您导出，请稍后");
                $.ajax({
                    url: "${base}/helper/member/payBill/list_export.jhtml",
                    type: "get",
                    data: {
                        startDate: $("#startDate").val(),
                        endDate: $("#endDate").val(),
                        keywords: $("#keyword").val(),
                        type: $("#type").val()
                    },
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        var html = '<table style="display:none;" class="table2excel">' +
                                '<thead>' +
                                '<tr>' +
                                '<th>创建日期</th>' +
                                '<th>用户</th>' +
                                '<th>门店名称</th>' +
                                '<th>流水号</th>' +
                                '<th>活动名称</th>' +
                                '<th>买单状态</th>' +
                                '<th>买单类型</th>' +
                                '<th>结算金额</th>' +
                                '<th>买单金额</th>' +
                                '<th>返现金额</th>' +
                                '<th>商家优惠</th>' +
                                '<th>平台优惠</th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>';
                        $.each(data, function (i, obj) {
                            html += '<tr>' +
                                    '<td>' + obj.date + '</td>' +
                                    '<td>' + obj.username + '</td>' +
                                    '<td>' + obj.deliveryCenter + '</td>' +
                                    '<td>' + obj.sn + '</td>' +
                                    '<td>' + obj.activityName + '</td>' +
                                    '<td>' + obj.status + '</td>' +
                                    '<td>' + obj.type + '</td>' +
                                    '<td>' + obj.total + '</td>' +
                                    '<td>' + obj.amount + '</td>' +
                                    '<td>' + obj.backDiscount + '</td>' +
                                    '<td>' + obj.tenantDiscount + '</td>' +
                                    '<td>' + obj.discount + '</td>' +
                                    '</tr>';

                        });
                        html += '</tbody>' +
                                '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "优惠买单统计",
                    filename: "优惠买单统计",
                    fileext: ".xls",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
            });

            $("#typeOption a").click(function () {
                var $this = $(this);
                $("#type").val($this.attr("val"));
                $("#listForm").submit();
                return false;
            });

        });
        function get_date_val() {
            $("#listForm").submit();
        }
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                营销工具
                <small>统计店铺内成功优惠买单的数据</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">营销工具</a></li>
                <li class="active">买单折扣</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="box-header with-border">
                    <i class="fa fa-comments-o"></i>
                    <h3 class="box-title">优惠买单统计</h3>
                </div>
                <form id="listForm" action="list.jhtml" method="get">
                    <input type="hidden" id="type" name="type" value="${type}"/>
                    <div class="row mtb10">
                        <div class="col-sm-7">
                            <div class="btn-group">
                                <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
                                        class="fa fa-refresh mr5"
                                        aria-hidden="true"></i> 刷新
                                </button>
                                <div class="dropdown fl ml5">
                                    <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                            id="dropdownMenu1"
                                            data-toggle="dropdown">
                                        类型
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                        id="typeOption">
                                    [#list types as type]
                                        <li>
                                            <a href="javascript:;"
                                               [#if type==_type]class="current"[/#if]
                                               val="${type}">${message("PayBill.Type."+type)}</a>
                                        </li>
                                    [/#list]
                                    </ul>
                                </div>
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
                                <button type="button" id="export_ss" class="btn btn-default btn-sm">导出</button>
                            </div>
                        </div>
                        <div class="col-sm-5">
                            <div class="box-tools fr">
                                <div class="input-group input-group-sm" style="width: 150px;">
                                    <input type="text" name="table_search" class="form-control pull-right"
                                           placeholder="Search">
                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="box-body">
                        <table id="listTable" class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>创建日期</th>
                                <th>用户</th>
                                <th>门店</th>
                                <th>流水号</th>
                                <th>活动名称</th>
                                <th>买单状态</th>
                                <th>买单类型</th>
                                <th>结算金额</th>
                                <th>买单金额</th>
                                <th>返现金额</th>
                                <th>商家优惠</th>
                                <th>平台优惠</th>
                            </tr>
                            </thead>
                            <tbody>
                            [#list page.content as payBill]
                            <tr>
                                <td>
                                ${payBill.createDate?string('yyyy-MM-dd')}
                                </td>
                                <td>
                                ${(payBill.member.displayName)!}
                                </td>
                                <td>
                                ${(payBill.deliveryCenter.name)!}
                                </td>
                                <td>
                                ${(payBill.sn)!}
                                </td>
                                <td>
                                ${(payBill.activityName)!}
                                </td>
                                <td>
                                ${message("PayBill.Status."+payBill.status)}
                                </td>
                                <td>
                                ${message("PayBill.Type."+payBill.type)}
                                </td>
                                <td>
                                ${(payBill.clearingAmount)!}
                                </td>
                                <td>
                                ${(payBill.amount)!}
                                </td>
                                <td>
                                ${(payBill.backDiscount)!}
                                </td>
                                <td>
                                ${(payBill.tenantDiscount)!}
                                </td>
                                <td>
                                ${(payBill.discount)!}
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
                </form>
            </div>
    </div>
</div>
[#include "/store/member/include/footer.ftl" /]
</div>
<div id="trade_wrap"></div>
</body>
</html>
