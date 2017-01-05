<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>销售明细</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
  <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
      [#include "/store/member/include/header.ftl"]
      <!-- Left side column. contains the logo and sidebar -->
      [#include "/store/member/include/menu.ftl"]
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>数据统计<small>查询店内销售明细</small>	</h1>
          <ol class="breadcrumb">
            <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
            <li><a href="${base}/store/member/statistics/sale_detail.jhtml">数据统计</a></li>
            <li class="active">销售明细</li>
          </ol>
        </section>
        <!-- Main content -->
        <section class="content">
          <div class="row">
            <div class="col-md-12">
              <div class="nav-tabs-custom">
                <ul class="nav nav-tabs pull-right">
                  <li class="pull-left header"><i class="fa fa-pie-chart"></i>销售明细</li>
                </ul>
                <div class="tab-content" style="padding:15px 0 0 0;">
                  <form id="listForm" action="sale_detail.jhtml" method="get">
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
                            <input type="text"id="keyword" name="keywords" value="${keywords}" maxlength="200" class="form-control pull-right" placeholder="单号/条码/名称">
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
                              <th>商品名称</th>
                              <th>商品编码</th>
                              <th>供应商</th>
                              <th>单位</th>
                              <th>数量</th>
                              <th>单价</th>
                              <th>金额</th>
                              <th>结算单价</th>
                              <th>结算金额</th>
                              <th>订单状态</th>
                              <th>支付方式</th>
                              <th>结算状态</th>
                            </tr>
                          </thead>
                          <tbody>
                            [#list page.content as orderItem]
                            <tr>
                              <td> ${(orderItem.createDate)!}</td>
                              <td> ${(orderItem.trade.shippingDate)!}</td>
                              <td>
                                <a href="${base}/store/member/trade/view.jhtml?id=${orderItem.trade.id}">
                                  ${(orderItem.trade.order.sn)!}
                                </a>
                              </td>
                              <td>
                                <span title="${(orderItem.fullName)!}">
                                  ${abbreviate(orderItem.fullName, 10, "...")}
                                </span>
                              </td>
                              <td>${(orderItem.sn)!}</td>
                              <td title="${orderItem.supplier.name}">
                                [#if orderItem.supplier??&&orderItem.supplier?has_content]
                                ${abbreviate(orderItem.supplier.name, 10, "...")}
                                [/#if]
                              </td>
                              <td>${(orderItem.packagUnitName)!}</td>
                              <td>${(orderItem.quantity)!}</td>
                              <td>${(orderItem.price)!}</td>
                              <td>${(orderItem.quantity*orderItem.price)!}</td>
                              <td>${(orderItem.cost)!}</td>
                              <td>${(orderItem.cost*orderItem.quantity)!}</td>
                              <td>${message("Order.OrderStatus." + orderItem.trade.orderStatus)}</td>
                              <td>${orderItem.trade.order.paymentMethodName}</td>
                              <td>
                                [#if orderItem.trade.suppliered==true]
                                已结算
                                [#else]
                                未结算
                                [/#if]
                              </td>
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
                  </form>
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
            url:"${base}/store/member/statistics/sale_detail_export.jhtml",
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
                    '<th>商品名称</th>'+
                    '<th>商品条码</th>'+
                    '<th>商品编码</th>'+
                    '<th>用户名</th>'+
                    '<th>昵称</th>'+
                    '<th>供应商</th>'+
                    '<th>单位</th>'+
                    '<th>数量</th>'+
                    '<th>销售单价</th>'+
                    '<th>销售金额</th>'+
                    '<th>结算单价</th>'+
                    '<th>结算金额</th>'+
                    '<th>支付方式</th>'+
                    '<th>支付状态</th>'+
                    '<th>配送状态</th>'+
                    '<th>订单状态</th>'+
                    '<th>结算状态</th>'+
                  '</tr>'+
                '</thead>'+
              '<tbody>';
              $.each(data,function(i,obj){
                html+=
                  '<tr>'+
                    '<td>'+obj.create_date+'</td>'+
                    '<td>'+obj.shipping_date+'</td>'+
                    '<td>'+obj.osn+'</td>'+
                    '<td>'+obj.name+'</td>'+
                    '<td>'+obj.barcode+'</td>'+
                    '<td>'+obj.sn+'</td>'+
                    '<td>'+obj.username+'</td>'+
                    '<td>'+obj.nickname+'</td>'+
                    '<td>'+obj.supplierName+'</td>'+
                    '<td>'+obj.unit+'</td>'+
                    '<td>'+obj.quantity+'</td>'+
                    '<td>'+obj.price+'</td>'+
                    '<td>'+obj.totalPrice+'</td>'+
                    '<td>'+obj.cost+'</td>'+
                    '<td>'+obj.totalCost+'</td>'+
                    '<td>'+obj.paymentMethod+'</td>'+
                    '<td>'+obj.paymentStatus+'</td>'+
                    '<td>'+obj.shippingStatus+'</td>'+
                    '<td>'+obj.orderStatus+'</td>'+
                    '<td>'+obj.clearing+'</td>'+
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
            name: "销售明细",
            filename: "销售明细",
            fileext: ".xls",
            exclude_img: true,
            exclude_links: true,
            exclude_inputs: true
          });
        });
      });
    </script>
<div id="trade_wrap"></div>
</body>
</html>