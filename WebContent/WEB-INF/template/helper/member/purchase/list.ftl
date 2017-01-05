<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("shop.member.deposit.list")}[#if systemShowPowered][/#if]</title>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/product.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/amazeui.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/iconfont.css"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/search.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            $("#add_purchase").click(function(){
                if("${isMonthly}"=="true"){
                    $.message("warn", "今日有月结操作，不能进行此操作");
                    return;
                }
                location.href="add.jhtml";
            });
            //导出数据到excel
            $("#exportBtn").click(function () {
                $.message("success", "正在帮您导出，请稍后")
                $.ajax({
                    url: "${base}/helper/member/purchase/export_list.jhtml",
                    type: "get",
                    data: {
                        beginDate: $("#beginDate").val(),
                        endDate: $("#endDate").val(),
                        keywords: $("[name='keywords']").val()
                    },
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        var html = '<table style="display:none;" class="table2excel">' +
                                '<thead>' +
                                '<tr>' +
                                '<th>单据编号</th>' +
                                '<th>供应商</th>' +
                                '<th>单据状态</th>' +
                                '<th>创建日期</th>' +
                                '<th></th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>;'
                        $.each(data, function (i, obj) {
                            html += '<tr>' +
                                    '<td>' + obj.sn + '</td>' +
                                    '<td>' + obj.supplier + '</td>' +
                                    '<td>' + obj.type + '</td>' +
                                    '<td>' + obj.date + '</td>' +
                                    '<td></td>' +
                                    '</tr>' +
                                    '<tr style="background-color:#e4e4e4;">' +
                                    '<td></td>' +
                                    '<td>商品名称</td>' +
                                    '<td>商品编号</td>' +
                                    '<td>商品价格</td>' +
                                    '<td>商品数量</td>' +
                                    '</tr>';
                            $.each(obj.purchaseItem, function (j, item) {
                                html += '<tr style="background-color:#e4e4e4;">' +
                                        '<td></td>' +
                                        '<td>' + item.name + '</td>' +
                                        '<td>' + item.pSn + '</td>' +
                                        '<td>' + item.price + '</td>' +
                                        '<td>' + item.quantity + '</td>' +
                                        '</tr>';
                            });
                        });
                        html += '</tbody>' +
                                '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "采购单",
                    filename: "采购单",
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
                        <dt class="app-title" id="app_name">我的采购单</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查询采购清单明细。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">我的采购单</a></li>
                </ul>
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/purchase/list.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" class="iconButton" id="add_purchase">
                                <span class="addIcon">&nbsp;</span>添加
                            </a>
                        [/#if]
                    [/@helperRole]

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
                        </div>
                    </div>
                    <div class="menuWrap" style="">
                        <span style="">时间：</span><input type="text" class="text W120" id="beginDate" name="beginDate"
                                                        value="${(beginDate?string('yyyy-MM-dd'))!}"
                                                        onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\',{d:-1})}',onpicked:function(){endDate.focus();}})"
                                                        readonly placeholder="开始时间"> 至 <input type="text"
                                                                                              class="text W120"
                                                                                              id="endDate"
                                                                                              name="endDate"
                                                                                              value="${(endDate?string('yyyy-MM-dd'))!}"
                                                                                              onfocus="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\',{d:1})}'})"
                                                                                              readonly
                                                                                              placeholder="结束时间">
                        <!-- <input type="text" class="text W120" name="supplierName" value="${supplierName}" placeholder="搜索供应商"/> -->
                        <input type="text" class="text W120" name="keywords" value="${keywords}" placeholder="搜索...."
                               style="margin-left:20px;"/>
                        <input type="submit" value="搜索" class="button"/>

                    [@helperRole url="helper/member/purchase/list.jhtml" type="exp"]
                        [#if helperRole.retOper!="0"]
                            <input id="exportBtn" type="button" value="导出" class="button"/>
                        [/#if]
                    [/@helperRole]

                    [#--<div class="search" style="width: 220px;">--]
                    [#--<input type="text" id="keyword" name="keyword" value="${keyword}" maxlength="200"--]
                    [#--placeholder="搜索供货商、商品条码"/>--]
                    [#--<button type="submit">&nbsp;</button>--]
                    [#--</div>--]
                    </div>
                </div>

                <div class="list">
                    <table class="list">
                        <thead>
                        <tr>
                            <th>
                                单据编号
                            </th>
                            <th>
                                供应商
                            </th>
                            <th>
                                单据状态
                            </th>
                            <th>
                                创建日期
                            </th>
                            <th class="noExl">
                                操作
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        [#list page.content as purchase]
                        <tr[#if !purchase_has_next] class="last"[/#if]>
                            <td>
                            ${purchase.sn}
                            </td>
                            <td>
                            ${purchase.supplier.name}
                            </td>
                            <td>
                            ${message("Purchaase.Type."+purchase.type)}
                            </td>
                            <td>
                                <span title="${purchase.purchaseDate?string("yyyy-MM-dd HH:mm:ss")}">${purchase.purchaseDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                            </td>
                            <td class="noExl">
                                [#if purchase.type=='applied']

                                    [@helperRole url="helper/member/purchase/list.jhtml" type="applied"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="${base}/helper/member/purchase/edit/${purchase.id}.jhtml?type=${purchase.type}"
                                               style="color: red;">[审核]</a>
                                        [/#if]
                                    [/@helperRole]
                                [#else]
                                    [@helperRole url="helper/member/purchase/list.jhtml" type="read"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="${base}/helper/member/purchase/edit/${purchase.id}.jhtml?type=${purchase.type}">[查看]</a>
                                        [/#if]
                                    [/@helperRole]
                                [/#if]
                            </td>
                        </tr>
                        [/#list]
                        </tbody>
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
<div id="trade_wrap"></div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>