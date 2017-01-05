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
                    url:"${base}/helper/member/statistics/return_detail_export.jhtml",
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
                                '<th>商品名称</th>'+
                                '<th>商品编码</th>'+
                                '<th>订单号</th>'+
                                '<th>退货单号</th>'+
                                '<th>用户名</th>'+
                                '<th>供应商</th>'+
                                '<th>单位</th>'+
                                '<th>商品单价</th>'+
                                '<th>发货数量</th>'+
                                '<th>发货金额</th>'+
                                '<th>结算单价</th>'+
                                '<th>结算金额</th>'+
                                '<th>退货数量</th>'+
                                '<th>退货金额</th>'+
                                '<th>退货结算</th>'+
                                '<th>订单状态</th>'+
                                '<th>支付方式</th>'+
                                '<th>支付状态</th>'+
                                '<th>配送状态</th>'+
                                '<th>退货状态</th>'+
                                '<th>结算状态</th>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>';
                        $.each(data,function(i,obj){
                            html+=
                            '<tr>'+
                                '<td>'+obj.date+'</td>'+            //创建日期
                                '<td>'+obj.time+'</td>'+            //完成日期
                                '<td>'+obj.name+'</td>'+            //商品名称
                                '<td>'+obj.psn+'</td>'+             //商品编码
                                '<td>'+obj.osn+'</td>'+             //订单号
                                '<td>'+obj.rsn+'</td>'+             //退货单号
                                '<td>'+obj.username+'</td>'+        //用户名
                                '<td>'+obj.supplierName+'</td>'+    //供应商
                                '<td>'+obj.unit+'</td>'+            //单位
                                '<td>'+obj.price+'</td>'+           //商品单价
                                '<td>'+obj.shippedQuantity+'</td>'+  //发货数量
                                '<td>'+obj.totalPrice+'</td>'+       //发货金额
                                '<td>'+obj.cost+'</td>'+            //结算单价
                                '<td>'+obj.totalCost+'</td>'+        //结算金额
                                '<td>'+obj.returnQuantity+'</td>'+   //退货数量
                                '<td>'+obj.price*obj.returnQuantity+'</td>'+          //退货金额
                                '<td>'+obj.cost*obj.returnQuantity+'</td>'+          //退货结算
                                '<td>'+obj.orderStatus+'</td>'+      //订单状态
                                '<td>'+obj.settleStatus+'</td>'+     //支付方式
                                '<td>'+obj.paymentStatus+'</td>'+     //支付方式
                                '<td>'+obj.shippingStatus+'</td>'+   //配送状态
                                '<td>'+obj.returnStatus+'</td>'+     //退货状态
                                '<td>'+obj.clearing+'</td>'+         //结算状态
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
                    name: "退货明细",
                    filename: "退货明细",
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
                        <dt class="app-title" id="app_name">退货明细</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查询店内退货明细</dd>
                    </dl>
                </div>
                &nbsp;
            </div>
            <form id="listForm" action="return_detail.jhtml" method="get">
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
                                        <a href="javascript:;" val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="menuWrap">
                            [@helperRole url="helper/member/statistics/return_detail.jhtml" type="exp"]
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
                                   placeholder="搜索内容....." />
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
                            <th>
                                订单单号
                            </th>
                            <th>
                                退货单号
                            </th>
                            <th>
                                商品名称
                            </th>
                            <th>
                                商品编码
                            </th>
                            <th>
                                供应商
                            </th>
                            <th>
                                发货数量
                            </th>
                            <th>
                                退货数量
                            </th>
                            <th>
                                退货金额
                            </th>
                            <th>
                                结算金额
                            </th>
                            <th>
                                支付方式
                            </th>
                            <th>
                                结算状态
                            </th>
                            
                        </tr>
                        [#if page.content?size>0]
                        [#list page.content as returns]
                            [#if returns.returnsItems?size>0]
                            [#list returns.returnsItems as returnItem]
                            [#if returnItem.returnQuantity>0]
                            <tr>
                                <td>
                                    ${(returnItem.returns.createDate)!}
                                </td>
                                <td>
                                    ${(returnItem.returns.completedDate)!}
                                </td>
                                <td>
                                    <a href="${base}/helper/member/trade/view.jhtml?id=${returns.trade.order.id}">
                                        ${(returns.trade.order.sn)!}
                                    </a>
                                </td>
                                <td>
                                    <a href="${base}/helper/member/trade/return/view.jhtml?spReturnsId=${returns.id}">
                                        ${(returns.sn)!}
                                    </a>
                                </td>
                                <td >
                                    <span style="width:150px;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;cursor:pointer;" title="${(returnItem.name)!}">
                                    ${abbreviate(returnItem.name,20,"...")}
                                    </span>
                                </td>
                                <td>
                                    ${(returnItem.sn)!}
                                </td>
                                <td>
                                    [#if returnItem.orderItem.supplier??&&returnItem.orderItem.supplier?has_content]
                                    ${abbreviate(returnItem.orderItem.supplier.name,10,"..")}
                                    [/#if]
                                </td>
                                <td>
                                    ${(returnItem.shippedQuantity)!}
                                </td>
                                <td>
                                    ${(returnItem.returnQuantity)!}
                                </td>
                                <td>
                                    ${(returnItem.returnQuantity*returnItem.orderItem.price)!}
                                </td>
                                <td>
                                   ${(returnItem.returnQuantity*returnItem.orderItem.cost)!}
                                </td>
                                <td>
                                    [#if returnItem.orderItem.trade.order.paymentMethod.method=="online"]
                                        线上支付
                                    [#elseif returnItem.orderItem.trade.order.paymentMethod.method=="offline"]
                                        线下支付
                                    [#elseif returnItem.orderItem.trade.order.paymentMethod.method=="balance"]
                                        余额支付
                                    [/#if]
                                </td>
                                <td>
                                    [#if returnItem.returns.suppliered==true]
                                        已结算
                                    [#else]
                                        未结算
                                    [/#if]
                                </td>
                            </tr>
                            [/#if]
                            [/#list]
                            [/#if]
                        [/#list]
                        [/#if]
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
