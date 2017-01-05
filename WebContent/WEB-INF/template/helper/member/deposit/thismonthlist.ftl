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
                    name: "本月账单信息",
                    filename: "本月账单信息",
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
                        <dt class="app-title" id="app_name">本月账单明细</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">查询本月账单明细</dd>
                    </dl>
                [#--<div id="export_ss" class="export">导出</div>--]
                </div>
                <ul class="links" id="mod_menus">
                    <!-- <li><a hideFocus="" href="${base}/helper/member/deposit/thismonthlist.jhtml">本月交易明细</a></li> -->
                    <li><a class="on" hideFocus="" href="javascript:;">本月账单明细</a></li>
                </ul>
            </div>
            <form id="listForm" action="thismonthlist.jhtml" method="get">

                <div class="bar">
                    <div class="buttonWrap">

                    [@helperRole url="helper/member/deposit/statistics.jhtml" type="exp"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="export_ss" class="button">导出</a>
                        [/#if]
                    [/@helperRole]

                    [#--<a href="javascript:;" id="deleteButtonProduct" class="iconButton disabled">--]
                    [#--<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}--]
                    [#--</a>--]
                        <a href="thismonthlist.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>

                        &nbsp&nbsp&nbsp&nbsp

                        <a href="thismonthlist.jhtml?month=currentMonth" id="currentMonth" name="currentMonth"
                           class="button">本月</a>
                        <a href="thismonthlist.jhtml?month=lastMonth" id="lastMonth" name="lastMonth"
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
                        <div class="menuWrap">
                            <a href="javascript:;" id="typeSelect" class="button">
                                类型<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="type">
                                [#list types as type]
                                    <li>
                                        <a href="thismonthlist.jhtml?type=${type}"
                                           [#if type==_type]class="current"[/#if]>${message("Deposit.Type."+type)}</a>
                                    </li>
                                [/#list]
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <input type="submit" class="button" value="查询"/>
                    </div>
                </div>
                <div class="list" style="border-top:0">
                    <table class="list table2excel">
                        <tr>
                            <th>
                                <a href="javascript:;" class="sort">
                                    日期
                                </a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort">
                                    类型
                                </a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort">
                                    收入金额
                                </a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort">
                                    支出金额
                                </a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort">
                                    当前余额
                                </a>
                            </th>
                            <th style="border-right: 0px">
                                <a href="javascript:;" class="sort">
                                    摘要
                                </a>
                            </th>
                        </tr>
                    [#list page.content as deposit]
                        <tr[#if !deposit_has_next] class="last"[/#if]>
                            <td>
                                <span title="${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}">${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                            </td>
                            <td>
                            ${message("Deposit.Type." + deposit.type)}
                            </td>
                            <td style="text-align:right;">
                            ${currency(deposit.credit,true)}
                            </td>
                            <td style="text-align:right;">
                            ${currency(deposit.debit,true)}
                            </td>
                            <td style="text-align:right;">
                            ${currency(deposit.balance,true)}
                            </td>
                            <td style="border-right: 0px;">
                            ${deposit.memo}
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p>${message("shop.member.noResult")}</p>
                [/#if]
                </div>
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
                [#include "/helper/member/include/pagination.ftl"]
            [/@pagination]
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>