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
        #listTable th{
            text-align: center;
        }
        #listTable td{
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
            $("#submit_form").click(function(){
                if($("#keyword").val()!=""){
                    $("#page_numb").val("1")
                }
                $("#listForm").submit();
            });
            
            $("#export_ss").click(function(){
                $.message("success","正在帮您导出，请稍后");
                $.ajax({
                    url:"${base}/helper/member/statistics/sale_total_export.jhtml",
                    type:"get",
                    data:{
                        begin_date:$("#startDate").val(),
                        end_date:$("#endDate").val(),
                        keywords:$("#keyword").val()
                    },
                    async:false,
                    dataType:"json",
                    success:function(data){
                        var html='<table style="display:none;" class="table2excel">'+
                        '<thead>'+
                            '<tr>'+
                                '<th>创建日期</th>'+
                                '<th>日期</th>'+
                                '<th>订单号</th>'+
                                '<th>订单状态</th>'+
                                '<th>配送状态</th>'+
                                '<th>支付方式</th>'+
                                '<th>结算状态</th>'+
                                '<th>数量</th>'+
                                '<th>赠品数量</th>'+
                                '<th>发货数量</th>'+
                                '<th>金额</th>'+
                                '<th>结算金额</th>'+
                                // '<th>促销折扣</th>'+
                                '<th>优惠券折扣</th>'+
                                '<th>调整金额</th>'+
                                '<th>运费</th>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>';
                        $.each(data,function(i,obj){
                            html+=
                            '<tr>'+
                                '<td>'+obj.date+'</td>'+
                                '<td>'+obj.time+'</td>'+
                                '<td>'+obj.sn+'</td>'+
                                '<td>'+obj.orderStatus+'</td>'+
                                '<td>'+obj.shippingStatus+'</td>'+
                                '<td>'+obj.paymentMethod+'</td>'+
                                '<td>'+obj.clearing+'</td>'+
                                '<td>'+obj.pQ+'</td>'+
                                '<td>'+obj.gQ+'</td>'+
                                '<td>'+obj.sQ+'</td>'+
                                '<td>'+obj.amount+'</td>'+
                                '<td>'+obj.cost+'</td>'+
                                // '<td>'+obj.promoDis+'</td>'+
                                '<td>'+obj.couDis+'</td>'+
                                '<td>'+obj.offsetAmount+'</td>'+
                                '<td>'+obj.freight+'</td>'+
                            '</tr>';
                        });
                        html+='</tbody>'+
                        '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                //导出数据到excel
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "销售统计",
                    filename: "销售统计",
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/my-order.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">销售统计</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查询店内销售统计</dd>
                    </dl>
                </div>
                &nbsp;
                <ul class="links" id="mod_menus">
                    <li>
                        <a [#if type == "order_total"]class="on"[/#if] name="type" val="order_total" hideFocus="" style="cursor: pointer;" roles="owner,manager,cashier">订单统计</a>
                    </li>
                    <li>
                        <a href="${base}/helper/member/statistics/product_total.jhtml" [#if type == "product_total"]class="on"[/#if] name="type" val="product_total" hideFocus=""
                           style="cursor: pointer;" roles="owner,manager">产品统计</a>
                    </li>
                    
                </ul>
            </div>
            <form id="listForm" action="sale_total.jhtml" method="get">
                [#--<input type="hidden" id="page_numb" name="page_numb" value="" />--]
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
                                        <a href="javascript:;" [#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" [#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" [#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" [#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="menuWrap">
                            [@helperRole url="helper/member/statistics/sale_total.jhtml" type="exp"]
                            [#if helperRole.retOper!="0"]
                                <a href="javascript:;" id="export_ss" class="button">
                                    	导出<span class="arrow">&nbsp;</span>
                                </a>
                            [/#if]
                        [/@helperRole]
                        </div>
                    </div>
                    <input type="text" id="startDate" name="begin_date" value="${begin_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间"/>
                    <input type="text" id="endDate" name="end_date" value="${end_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});"
                       placeholder="结束时间"/>
                    <!-- <input type="button" value="查询" onclick="get_date_val()" id="submit_button"> -->
                    <div class="menuWrap">
                        <div class="search" style="width: 220px;">
                            <input type="text" id="keyword" name="keywords" value="${keywords}" maxlength="200"
                                   placeholder="搜索内容....."/>
                            <button type="button" id="submit_form">&nbsp;</button>
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
                                发货日期
                            </th>
                            <th>
                                订单号
                            </th>
                            <th>
                                订单状态
                            </th>
                            <th>
                                支付方式
                            </th>
                            <th>
                                配送状态
                            </th>
                            <th>
                                结算状态
                            </th>
                            <th>
                                商品数量
                            </th>
                            <th>
                                赠品数量
                            </th>
                            <th>
                                发货数量
                            </th>
                            <th>
                                金额
                            </th>
                            <th>
                                结算金额
                            </th>
                            <!-- <th>
                                税费
                            </th>
                            <th>
                                运费
                            </th> -->
                            <th>
                                促销折扣
                            </th>
                            <th>
                                优惠券折扣
                            </th>
                            <th>
                                调整金额
                            </th>
                        </tr>
                        [#list page.content as trade]
                        <tr>
                            <td>
                                ${(trade.createDate?string('yyyy-MM-dd'))!}
                            </td>
                            <td>
                                ${(trade.shippingDate?string('yyyy-MM-dd'))!}
                            </td>
                            <td>
                                <a href="${base}/helper/member/trade/view.jhtml?id=${trade.id}">
                                    ${(trade.order.sn)!}
                                </a>
                            </td>
                            <td>
                                [#if trade.orderStatus=="unconfirmed"]
                                    未确认
                                [#elseif trade.orderStatus=="confirmed"]
                                    已确认
                                [#elseif trade.orderStatus=="completed"]
                                    已完成
                                [#elseif trade.orderStatus=="cancelled"]
                                    已取消
                                [/#if]
                            </td>
                            <td>
                                [#if trade.order.paymentMethod.method=="online"]
                                    线上支付
                                [#elseif trade.order.paymentMethod.method=="offline"]
                                    线下支付
                                [#elseif trade.order.paymentMethod.method=="balance"]
                                    余额支付
                                [/#if]
                            </td>
                            <td>
                                [#if trade.shippingStatus=="unshipped"]
                                未发货
                                [#elseif trade.shippingStatus=="partialShipment"]
                                部分发货
                                [#elseif trade.shippingStatus=="shipped"]
                                已发货
                                [#elseif trade.shippingStatus=="partialReturns"]
                                部分退货
                                [#elseif trade.shippingStatus=="returned"]
                                已退货
                                [#elseif trade.shippingStatus=="accept"]
                                已签收
                                [#elseif trade.shippingStatus=="shippedApply"]
                                退货中
                                [/#if]
                            </td>
                            <td>
                                [#if trade.suppliered==true]
                                    已结算
                                [#else]
                                    未结算
                                [/#if]
                            </td>
                            <td>
                                ${(trade.quantity)!}
                            </td>
                            <td>
                                [#assign quanti=0]
                                [#if trade.giftItems??]
                                    [#list trade.giftItems as giftItem]
                                        [#assign quanti=quanti+giftItem.quantity]
                                    [/#list]
                                [/#if]
                                ${quanti}
                            </td>
                            <td>
                                ${(trade.shippedQuantity)!}
                            </td>
                            <td>
                                ${(trade.price)!}
                            </td>
                            <td>
                                ${(trade.cost)!}
                            </td>
                            <!-- <td>
                                [#if trade.tax??]${(trade.tax)!}[#else]--[/#if]
                            </td>
                            <td>
                                [#if trade.freight??]${(trade.freight)!}[#else]--[/#if]
                            </td> -->
                            <td>
                                [#if trade.promotionDiscount??]${(trade.promotionDiscount)!}[#else]--[/#if]
                            </td>
                            <td>
                                [#if trade.couponDiscount??]${(trade.couponDiscount)!}[#else]--[/#if]
                            </td>
                            <td>
                                [#if trade.offsetAmount??]${(trade.offsetAmount)!}[#else]--[/#if]
                            </td>
                            
                        </tr>
                        [/#list]
                    </table>
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
