<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>销售统计</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> 
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
  <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">
    [#include "/store/member/include/header.ftl"]
    <!-- Left side column. contains the logo and sidebar -->
    [#include "/store/member/include/menu.ftl"]
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <h1>数据统计<small>查询店内销售统计</small>	</h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/statistics/sale_total.jhtml">数据统计</a></li>
          <li class="active">销售统计</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li><a href="${base}/store/member/statistics/product_total.jhtml">产品统计</a></li>
                <li  class="active"><a href="${base}/store/member/statistics/sale_total.jhtml">销售统计</a></li>
                <li class="pull-left header"><i class="fa fa-pie-chart"></i>销售统计</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="sale_total.jhtml" method="get">
                  <div class="row mtb10">
                    <div class="col-sm-3">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i class="fa fa-refresh mr5" aria-hidden="true"></i> ${message("admin.common.refresh")}</button>
                        <button type="button" class="btn btn-default btn-sm" id="export_ss">导出</button>
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                            每页显示
                            <span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="pageSizeOption">
                            <li role="presentation" [#if page.pageSize == 10] class="active"[/#if]>
                              <a role="menuitem" tabindex="-1" href="javascript:;"  val="10">10</a>
                            </li>
                            <li role="presentation" [#if page.pageSize == 20] class="active"[/#if]>
                              <a role="menuitem" tabindex="-1" href="javascript:;"  val="20">20</a>
                            </li>
                            <li role="presentation" [#if page.pageSize == 30] class="active"[/#if]>
                              <a role="menuitem" tabindex="-1" href="javascript:;"  val="30">30</a>
                            </li>
                            <li role="presentation"  [#if page.pageSize == 40] class="active"[/#if]>
                              <a role="menuitem" tabindex="-1" href="javascript:;" val="40">40</a>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="fl">
                        <input type="text" id="startDate" name="begin_date" value="${begin_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间"/>
                      </div>
                      <div class="fl">
                        <i class="fa fa-exchange mid_po_icon"></i>
                      </div>
                      <div class="fl">
                        <input type="text" id="endDate" name="end_date" value="${end_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="结束时间"/>
                      </div>
                      <div class="fl ml5">
                        <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
                      </div>
                    </div>
                    <div class="col-sm-5">
                      <div class="box-tools fr">
                        <div class="input-group input-group-sm" style="width: 150px;">
                          <input type="text"id="keyword" name="keywords" value="${keywords}" maxlength="200" class="form-control pull-right" placeholder="订单号">
                          <div class="input-group-btn">
                            <button type="submit" class="btn btn-default" id="submit_form"><i class="fa fa-search"></i></button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                      <table class="table table-bordered table-striped">
                        <thead>
                          <tr>
                            <th>创建日期</th>
                            <th>发货日期</th>
                            <th>订单号</th>
                            <th>订单状态</th>
                            <th>支付方式</th>
                            <th>配送状态</th>
                            <th>结算状态</th>
                            <th>数量</th>
                            <th>赠品量</th>
                            <th>发货量</th>
                            <th>金额</th>
                            <th>结算额</th>
                            <th>促销折扣</th>
                            <th>优惠券折扣</th>
                            <th>调整金额</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as trade]
                          <tr>
                            <td>${(trade.createDate?string('yyyy-MM-dd'))!}</td>
                            <td>${(trade.shippingDate?string('yyyy-MM-dd'))!}</td>
                            <td><a href="${base}/store/member/trade/view.jhtml?id=${trade.id}">${(trade.order.sn)!}</a></td>
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
                            <td>[#if trade.suppliered==true]已结算[#else]未结[/#if]</td>
                            <td> ${(trade.quantity)!}</td>
                            <td>
                              [#assign quanti=0]
                              [#if trade.giftItems??]
                              [#list trade.giftItems as giftItem]
                              [#assign quanti=quanti+giftItem.quantity]
                              [/#list]
                              [/#if]
                              ${quanti}
                            </td>
                            <td>${(trade.shippedQuantity)!}</td>
                            <td>${(trade.amount)!}</td>
                            <td>${(trade.cost)!}</td>
                            <td>[#if trade.promotionDiscount??]${(trade.promotionDiscount)!}[#else]--[/#if]</td>
                            <td>[#if trade.couponDiscount??]${(trade.couponDiscount)!}[#else]--[/#if]</td>
                            <td>[#if trade.offsetAmount??]${(trade.offsetAmount)!}[#else]--[/#if]</td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                      <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/store/member/include/pagination.ftl"]
                        [/@pagination]
                      </div>
                    </div>
                  </div>
                </from>  
              </div>
            </div>
          </div>
        </div>
      </section>
      <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    [#include "/store/member/include/footer.ftl"]
  </div>
  <!-- ./wrapper -->
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
  <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
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
            '<th>发货日期</th>'+
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
            '<th>优惠券折扣</th>'+
            '<th>调整金额</th>'+
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
                '<td>'+obj.couDis+'</td>'+
                '<td>'+obj.offsetAmount+'</td>'+
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
          fileext: ".xls",
          exclude_img: true,
          exclude_links: false,
          exclude_inputs: true
        });
      });
    });
  </script>
<div id="trade_wrap"></div>
</body>
</html>