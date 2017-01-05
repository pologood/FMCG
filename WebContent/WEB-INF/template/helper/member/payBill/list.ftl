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
        #listForm tr th {
            text-align: center;
        }

        #listForm tr td {
            text-align: center;
        }
    </style>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/my-order.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">优惠买单统计</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">统计店铺内成功优惠买单的数据</dd>
                    </dl>
                </div>
                &nbsp;
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <div class="bar">
                    <div class="buttonWrap">

                        <a href="javascript:;" class="iconButton" id="refreshButton">
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
                            <div class="menuWrap">
                                <a href="javascript:;" id="typeSelect" class="button">
                                    类型<span class="arrow">&nbsp;</span>
                                </a>
                                <div class="popupMenu">
                                    <ul id="typeOption">
                                    [#list types as type]
                                        <li>
                                            <a href="javascript:;"
                                               [#if type==_type]class="current"[/#if]
                                               val="${type}">${message("PayBill.Type."+type)}</a>
                                        </li>
                                    [/#list]
                                    </ul>
                                </div>
                            </div>

                            <a href="javascript:;" id="export_ss" class="button">导出</a>
                        </div>
                        <div class="menuWrap">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="text" id="startDate" name="startDate"
                                   value="${(startDate?string('yyyy-MM-dd'))!}" class="text Wdate"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间"
                                   style="width:150px;"/>
                            <input type="text" id="endDate" name="endDate" value="${(endDate?string('yyyy-MM-dd'))!}"
                                   class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});"
                                   placeholder="结束时间" style="width:150px;"/>
                            <input type="text" id="keyword" name="keywords" value="${keywords}" maxlength="200"
                                   placeholder="搜索内容....." style="width:150px;height:20px;margin-left:50px;"/>
                            <input type="button" value="查询" onclick="get_date_val()" id="submit_button">
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th>
                                创建日期
                            </th>
                            <th>
                                用户
                            </th>
                            <th>
                                门店
                            </th>
                            <th>
                                流水号
                            </th>
                            <th>
                                活动名称
                            </th>
                            <th>
                                买单状态
                            </th>
                            <th>
                                买单类型
                            </th>
                            <th>
                                结算金额
                            </th>
                            <th>
                                买单金额
                            </th>
                            <th>
                                返现金额
                            </th>
                            <th>
                                商家优惠
                            </th>
                            <th>
                                平台优惠
                            </th>
                        </tr>
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
                    </table>
                </div>
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                [#include "/helper/include/pagination.ftl"]
            [/@pagination]
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
<div id="trade_wrap"></div>
</body>
</html>
