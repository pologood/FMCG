<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>退货产品统计</title>
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
        <h1>
          数据统计
          <small>退货统计</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/statistics/return_product_total.jhtml">数据统计</a></li>
          <li class="active">退货产品统计</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="active"><a href="${base}/store/member/statistics/return_product_total.jhtml">退货产品统计</a></li>
                <li><a href="${base}/store/member/statistics/return_total.jhtml">退货统计</a></li>
                <li class="pull-left header"><i class="fa fa-pie-chart"></i>退货产品统计</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="return_product_total.jhtml" method="get">
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
                        <input type="text" id="startDate" name="begin_date" value="${begin_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="退货完成开始时间"/>
                      </div>
                      <div class="fl">
                        <i class="fa fa-exchange mid_po_icon"></i>
                      </div>
                      <div class="fl">
                        <input type="text" id="endDate" name="end_date" value="${end_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="退货完成结束时间"/>
                      </div>
                      <div class="fl ml5">
                        <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
                      </div>
                    </div>
                    <div class="col-sm-5">
                      <div class="box-tools fr">
                        <div class="input-group input-group-sm" style="width: 150px;">
                          <input type="text"id="keyword" name="keywords" value="${keywords}" maxlength="200" class="form-control pull-right" placeholder="名称/条码/编码">
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
                            <th>完成日期</th>
                            <th>名称</th>
                            <th>条码</th>
                            <th>编码</th>
                            <th>供应商</th>
                            <th>赠品</th>
                            <th>单位</th>
                            <th>发货量</th>
                            <th>退货</th>
                            <th>销售价</th>
                            <th>结算价</th>
                            <th>退货金额</th>
                            <th>退货结算额</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list maps as returnItem]
                          <tr>
                            <td>${(returnItem.create_date)!}</td>
                            <td>${(returnItem.completed_date)!}</td>
                            <td title="${(returnItem.name)!}">${abbreviate(returnItem.name,20,"...")}</td>
                            <td>[#if returnItem.barcode??]${(returnItem.barcode)!}[#else]--[/#if]</td>
                            <td>${(returnItem.sn)!}</td>
                            <td>[#if returnItem.supplier??]${abbreviate(returnItem.supplier,10,"...")}[#else]--[/#if]</td>
                            <td>[#if returnItem.isGift=="true"]是[#else]否[/#if]</td>
                            <td>${(returnItem.unit)!}</td>
                            <td>${(returnItem.shippedQuantity)!}</td>
                            <td>${(returnItem.returnQuantity)!}</td>
                            <td>${(returnItem.price)!}</td>
                            <td>${(returnItem.cost)!}</td>
                            <td>${(returnItem.reTotalPrice)!}</td>
                            <td>${(returnItem.reTotalCost)}</td>
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
  <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
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
          url:"${base}/store/member/statistics/return_product_total_export.jhtml",
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
            html+='</tbody></table>';
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
<div id="trade_wrap"></div>
</body>
</html>
