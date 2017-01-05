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
                    url:"${base}/helper/member/statistics/return_product_total_export.jhtml",
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
                                '<th>完成日期</th>'+
                                '<th>名称</th>'+
                                '<th>条码</th>'+
                                '<th>编码</th>'+
                                '<th>供应商</th>'+
                                '<th>单位</th>'+
                                '<th>发货量</th>'+
                                '<th>退货量</th>'+
                                '<th>销售价</th>'+
                                '<th>结算价</th>'+
                                '<th>退货额</th>'+
                                '<th>结算额</th>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>';
                        $.each(data,function(i,obj){
                            html+=
                            '<tr>'+
                                '<td>'+obj.create_date+'</td>'+
                                '<td>'+obj.completed_date+'</td>'+
                                '<td>'+obj.name+'</td>'+
                                '<td>'+obj.barcode+'</td>'+
                                '<td>'+obj.sn+'</td>'+
                                '<td>'+obj.supplier+'</td>'+
                                '<td>'+obj.unit+'</td>'+
                                '<td>'+obj.shippedQuantity+'</td>'+
                                '<td>'+obj.returnQuantity+'</td>'+
                                '<td>'+obj.price+'</td>'+
                                '<td>'+obj.cost+'</td>'+
                                '<td>'+obj.reTotalPrice+'</td>'+
                                '<td>'+obj.reTotalCost+'</td>'+
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
                    name: "退货产品统计",
                    filename: "退货产品统计",
                    fileext: ".xls",
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
                        <dt class="app-title" id="app_name">退货商品统计</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">店铺退货商品的统计</dd>
                    </dl>
                </div>
                &nbsp;
                <ul class="links" id="mod_menus">
                    <li>
                        <a href="${base}/helper/member/statistics/return_total.jhtml" [#if menu == "return_total"]class="on"[/#if]
                           name="type" val="order_total" style="cursor: pointer;" roles="owner,manager,cashier">退货统计</a>
                    </li>
                    <li>
                        <a href="${base}/helper/member/statistics/return_product_total.jhtml" [#if menu == "return_product_total"]class="on"[/#if]
                           name="type" val="product_total" style="cursor: pointer;" roles="owner,manager">退货产品统计</a>
                    </li>
                </ul>
            </div>
            <form id="listForm" action="return_product_total.jhtml" method="get">
                <input type="hidden" id="page_numb" name="page_numb" value="" />
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
                            [@helperRole url="helper/member/statistics/product_total.jhtml" type="exp"]
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
                                完成日期
                            </th>
                            <th style="width:150px;">
                                名称
                            </th>
                            <th>
                                条码
                            </th>
                            <th>
                                编码
                            </th>
                            <th>
                                供应商
                            </th>
                            <th>
                                赠品
                            </th>
                            <th>
                                单位
                            </th>
                            <th>
                                发货量
                            </th>
                            <th>
                                退货量
                            </th>
                            <th>
                                销售价
                            </th>
                            <th>
                                结算价
                            </th>
                            <th>
                                退货金额
                            </th>
                            <th>
                                退货结算额
                            </th>
                        </tr>
                       
                        [#list maps as returnItem]
                        <tr>
                            <td>
                                ${(returnItem.create_date)!}
                            </td>
                            <td>
                                ${(returnItem.completed_date)!}
                            </td>
                            <td title="${(returnItem.name)!}">
                                ${abbreviate(returnItem.name,20,"...")}
                            </td>
                            <td>
                                [#if returnItem.barcode??]${(returnItem.barcode)!}[#else]--[/#if]
                            </td>
                            <td>
                                ${(returnItem.sn)!}
                            </td>
                            <td>
                                [#if returnItem.supplier??]${abbreviate(returnItem.supplier,10,"...")}[#else]--[/#if]
                            </td>
                            <td>
                                [#if returnItem.isGift=="true"]是[#else]否[/#if]
                            </td>
                            <td>
                                ${(returnItem.unit)!}
                            </td>
                            <td>
                               ${(returnItem.shippedQuantity)!}
                            </td>
                            <td>
                                ${(returnItem.returnQuantity)!}
                            </td>
                            <td>
                                ${(returnItem.price)!}
                            </td>
                            <td>
                                ${(returnItem.cost)!}
                            </td>
                            <td>
                                ${(returnItem.reTotalPrice)!}
                            </td>
                            <td>
                                ${(returnItem.reTotalCost)}
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
