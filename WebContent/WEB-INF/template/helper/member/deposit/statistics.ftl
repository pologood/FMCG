<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("shop.member.deposit.list")}[#if systemShowPowered][/#if]</title>
    <link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/amazeui.css">
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css">
    <link href="${base}/resources/helper/css/iconfont.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .export {
            float: left;
            border: 1px solid gray;
            width: 80px;
            line-height: 35px;
            text-align: center;
            font-size: 18px;
            border-radius: 5%;
            background: gray;
            color: white;
            margin-left: 120px;
            margin-top: 5px;
        }
    </style>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/search.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            //导出数据到excel
            $("#export_ss").click(function () {
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "本月账单统计",
                    filename: "本月账单统计",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/payment-details.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的账单</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">统计当月的收入、支出情况</dd>
                    </dl>
                [#--<div id="export_ss" class="export">导出</div>--]
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">账单统计</a></li>
                </ul>
            </div>
            <form id="listForm" action="statistics.jhtml" method="get">

                <div class="bar">
                    <div class="buttonWrap">

                    [@helperRole url="helper/member/deposit/statistics.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="export_ss" class="button">导出</a>
                        [/#if]
                    [/@helperRole]

                    [#--<a href="javascript:;" id="deleteButtonProduct" class="iconButton disabled">--]
                    [#--<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}--]
                    [#--</a>--]
                        <a href="statistics.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>

                        &nbsp&nbsp&nbsp&nbsp

                        <a href="statistics.jhtml?month=currentMonth" id="currentMonth" name="currentMonth"
                           class="button">本月</a>
                        <a href="statistics.jhtml?month=lastMonth" id="lastMonth" name="lastMonth"
                           class="button">上月</a>
                        <input type="text" id="beginDate" name="beginDate" class="text Wdate" style="width: 150px"
                               value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
                               onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
                        ---<input type="text" id="endDate" name="endDate" class="text Wdate" style="width: 150px"
                                  value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
                                  onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>

                    [#--<div class="menuWrap">--]
                    [#--<a href="javascript:;" id="filterSelect" class="button">--]
                    [#--${message("admin.product.filter")}<span class="arrow">&nbsp;</span>--]
                    [#--</a>--]
                    [#--<div class="popupMenu">--]

                    [#--</div>--]
                    [#--</div>--]

                    [#--<div class="menuWrap">--]
                    [#--<a href="javascript:;" id="pageSizeSelect" class="button">--]
                    [#--${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>--]
                    [#--</a>--]
                    [#--<div class="popupMenu">--]
                    [#--<ul id="pageSizeOption">--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>--]
                    [#--</li>--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>--]
                    [#--</li>--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>--]
                    [#--</li>--]
                    [#--<li>--]
                    [#--<a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]--]
                    [#--val="100">100</a>--]
                    [#--</li>--]
                    [#--</ul>--]
                    [#--</div>--]
                    [#--</div>--]
                    [#--<div class="menuWrap">--]
                    [#--<a href="javascript:;" id="typeSelect" class="button">--]
                    [#--类型<span class="arrow">&nbsp;</span>--]
                    [#--</a>--]
                    [#--<div class="popupMenu">--]
                    [#--<ul id="type">--]
                    [#--[#list types as type]--]
                    [#--<li>--]
                    [#--<a href="statistics.jhtml?type=${type}"--]
                    [#--[#if type==_type]class="current"[/#if]>${message("Deposit.Type."+type)}</a>--]
                    [#--</li>--]
                    [#--[/#list]--]
                    [#--</ul>--]
                    [#--</div>--]
                    [#--</div>--]
                    </div>
                    <div class="menuWrap">
                        <input type="submit" class="button" value="查询"/>
                    </div>
                </div>

                <div class="list" style="border-top:0">
                    <table class="list table2excel">
                        <tr>
                            <th style="text-align:center;">
                                类型
                            </th>
                            <th style="text-align:center;">
                            ${message("Deposit.credit")}
                            </th>
                            <th style="text-align:center;">
                            ${message("Deposit.debit")}
                            </th>
                            <th style="text-align:center;">
                            ${message("Deposit.balance")}
                            </th>
                        </tr>
                    [#list page as deposit]
                        <tr[#if !deposit_has_next] class="last"[/#if]>
                            <td style="text-align:center;">
                            ${deposit.type}
                            </td>
                            <td style="text-align:center;">
                            ${currency(deposit.credit,true)}
                            </td>
                            <td style="text-align:center;">
                            ${currency(deposit.debit,true)}
                            </td>
                            <td style="text-align:center;">
                            ${currency(deposit.balance,true)}
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page?has_content]
                    <p>${message("shop.member.noResult")}</p>
                [/#if]
                </div>
            </form>

        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>