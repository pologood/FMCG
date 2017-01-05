<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>汇款查询</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">
    [#include "/store/member/include/header.ftl"]
    <!-- Left side column. contains the logo and sidebar -->
    [#include "/store/member/include/menu.ftl"]
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <h1>
          汇款查询
          <small>查询银行汇款申请流水、到账状态！</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">汇款查询</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li  class="active"><a href="${base}/store/member/cash/list.jhtml">汇款记录</a></li>
                <li><a href="${base}/store/member/cash/index.jhtml">银行汇款</a></li>
                <li class="pull-left header"><i class="fa fa-search" ></i>汇款查询</li>
              </ul>

              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="list.jhtml" method="get">
                  <input type="hidden" name="status" value="" id="status_val">
                  <div class="row mtb10">
                    <div class="col-sm-4">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();">
                          <i class="fa fa-refresh mr5"></i> 刷新
                        </button>
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                            每页显示<span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="pageSizeOption">
                            <li role="presentation" class="[#if page.pageSize==10]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="10">10</a>
                            </li>
                            <li role="presentation" class="[#if page.pageSize==20]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="20">20</a>
                            </li>
                            <li role="presentation" class="[#if page.pageSize==30]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="30">30</a>
                            </li>
                            <li role="presentation" class="[#if page.pageSize==40]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="40">40</a>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="fl">
                        <input type="text" id="startDate" name="startDate" value="${begin_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="汇款开始时间"/>
                      </div>
                      <div class="fl">
                        <i class="fa fa-exchange mid_po_icon"></i>
                      </div>
                      <div class="fl">
                        <input type="text" id="endDate" name="endDate" value="${end_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="汇款结束时间"/>
                      </div>
                      <div class="fl ml5">
                        <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
                      </div>
                    </div>
                    <div class="col-sm-4">
                      <div class="box-tools fr">
                        <div class="input-group input-group-sm" style="width: 150px;">
                          <input type="text" class="form-control pull-right" id="searchValue" name="searchValue" value="${page.searchValue}"placeholder="编号">
                          <div class="input-group-btn">
                            <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
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
                            <th>汇款日期</th>
                            <th>汇款方式</th>
                            <th>编号</th>
                            <th>汇款金额</th>
                            <th>手续费</th>
                            <th>状态</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as credit ]
                          <tr>
                            <td>${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                            <td>[#if credit.method=="immediately"]加急汇款[#else]普通汇款[/#if]</td>
                            <td>${credit.sn}</td>
                            <td>${currency(credit.amount, true)}</td>
                            <td>${currency(credit.fee, true)}</td>
                            <td>${message("Credit.Status." + credit.status)}</td>
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
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
</body>
</html>
