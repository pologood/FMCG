<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>AdminLTE 2 | Dashboard</title>
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
          月末结算
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">月末结算</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="pull-left header"><i class="fa fa-pie-chart"></i>月末结算</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="monthly_list.jhtml" method="get">
                  <div class="row mtb10">
                    <div class="col-sm-3">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal">
                          <i class="fa fa-money mr5" aria-hidden="true"></i>结帐
                        </button>
                        <!-- 结帐弹框 【-->
                        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                          <div class="modal-dialog form-horizontal">
                            <div class="modal-content" style=" border-radius: 5px;">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title" id="myModalLabel">月末结账</h4>
                              </div>
                              <div class="modal-body">
                                <div class="form-group">
                                  <label for="inputExperience" class="col-sm-3 control-label">余额</label>
                                  <div class="col-sm-8">
                                    <input type="text" class="form-control" id="adjAmount" value="[#if tenant??]${tenant.member.balance}[#else]0.00[/#if]" disabled="true">
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label for="inputName" class="col-sm-3 control-label">冻结余额</label>
                                  <div class="col-sm-8">
                                    <input type="text" class="form-control" id="adjFreight" value="[#if tenant??]${tenant.member.freezeBalance}[#else]0.00[/#if]" disabled="true">
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label for="inputExperience" class="col-sm-3 control-label">待发货余额</label>
                                  <div class="col-sm-8">
                                    <input type="text" class="form-control" id="adjAmount" value="[#if amount??]${amount}[#else]0.00[/#if]" disabled="true">
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label for="inputName" class="col-sm-3 control-label">库存</label>
                                  <div class="col-sm-8">
                                    <input type="text" class="form-control" id="adjFreight" value="${stock}" disabled="true">
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label for="inputExperience" class="col-sm-3 control-label">锁定库存</label>
                                  <div class="col-sm-8">
                                    <input type="text" class="form-control" id="adjAmount" value="${lockStock}" disabled="true">
                                  </div>
                                </div>
                                <div class="form-group">
                                  <label for="inputName" class="col-sm-3 control-label">库存金额</label>
                                  <div class="col-sm-8">
                                    <input type="text" class="form-control" id="adjFreight" value="[#if StockAmount??]${StockAmount?string('0.00')}[#else]0.00[/#if]" disabled="true">
                                  </div>
                                </div>
                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-primary" id="confirmed_monthly">确认结帐</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
                              </div>
                            </div>
                          </div>
                        </div>
                        <!-- 结帐弹框 】-->  
                        <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i class="fa fa-refresh mr5" aria-hidden="true"></i> ${message("admin.common.refresh")}</button>
                        <button type="button" class="btn btn-default btn-sm" id="deleteButton"><i class="fa fa-close mr5" aria-hidden="true"></i>删除</button>
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
                  </div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                      <table class="table table-bordered table-striped" id="listTable">
                        <thead>
                          <tr>
                            <th class="check"><input type="checkbox" id="selectAll"/></th>
                            <th>创建时间</th>
                            <th>商家</th>
                            <th>商家余额</th>
                            <th>商家冻结金额</th>
                            <th>待发货冻结金额</th>
                            <th>库存</th>
                            <th>冻结库存</th>
                            <th>库存金额</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as monthlys]
                          <tr>
                            <td><input type="checkbox" name="ids" value="${monthlys.id}"/></td>
                            <td>${monthlys.createDate}</td>
                            <td>${monthlys.tenant.name}</td>
                            <td>${monthlys.balance}</td>
                            <td>${monthlys.freezeBalance}</td>
                            <td>${monthlys.unShippingBalance}</td>
                            <td>${monthlys.stock}</td>
                            <td>${monthlys.lockStock}</td>
                            <td>${monthlys.stockAmount}</td>
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
    $().ready(function(){
      $("#confirmed_monthly").click(function(){
        $.ajax({
          url:"confirm_monthly.jhtml",
          type:"get",
          dataType:"json",
          success:function(message){
            $.message(message);
            if(message.type=="success"){
              location.reload();
            }
          }
        });
      });
    });
  </script>
</body>
</html>
