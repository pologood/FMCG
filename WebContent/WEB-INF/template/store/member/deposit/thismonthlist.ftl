<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>本月账单明细</title>
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">
    [#include "/store/member/include/header.ftl"]
    [#include "/store/member/include/menu.ftl"]
    <div class="content-wrapper">
      <section class="content-header">
        <h1>
          我的账单
          <small>查询本月账单明细</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">本月账单明细</li>
        </ol>
      </section>
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="pull-left header"><i class="fa fa-fw fa-file-text"></i>本月账单明细</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="thismonthlist.jhtml" method="get">
                  <div class="row mtb10">
                    <div class="col-sm-5">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();"><i class="fa fa-refresh mr5"></i> 刷新
                        </button>
                        <button type="button" class="btn btn-default btn-sm" id="export_ss"><i class="fa fa-print mr5"></i> 导出
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
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">类型<span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                            [#list types as type]
                            <li role="presentation" [#if type==_type]class="active"[/#if]>
                              <a href="thismonthlist.jhtml?type=${type}">
                                ${message("Deposit.Type."+type)}
                              </a>
                            </li>
                            [/#list]
                          </ul>
                        </div>
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">月份<span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                            <li role="presentation">
                              <a href="thismonthlist.jhtml?month=currentMonth">本月</a>
                            </li>
                            <li role="presentation">
                              <a href="thismonthlist.jhtml?month=lastMonth">上月</a>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <div class="col-sm-5">
                      <div class="fl">
                        <input type="text" name="startDate" value="${begin_date}" class="date_input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="开始时间"/>
                      </div>
                      <div class="fl">
                        <i class="fa fa-exchange mid_po_icon"></i>
                      </div>
                      <div class="fl">
                        <input type="text" name="endDate" value="${end_date}" class="date_input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="结束时间"/>
                      </div>
                      <div class="fl ml5">
                        <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
                      </div>
                    </div>
                  </div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                      <table class="table table-bordered table-striped table2excel">
                        <thead>
                          <tr>
                            <th>日期</th>
                            <th>类型</th>
                            <th>收入金额</th>
                            <th>支出金额</th>
                            <th>当前余额</th>
                            <th>摘要</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as deposit]
                          <tr>
                            <td>${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                            <td>${message("Deposit.Type." + deposit.type)}</td>
                            <td>${currency(deposit.credit,true)}</td>
                            <td>${currency(deposit.debit,true)}</td>
                            <td>${currency(deposit.balance,true)}</td>
                            <td>${deposit.memo}</td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                      <div class="dataTables_paginate paging_simple_numbers">
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
    </div>
    [#include "/store/member/include/footer.ftl"]
  </div>
  <!--接收导出数据【-->
  <div id="trade_wrap"></div>
  <!--接收导出数据】-->
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
  <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
  <script type="text/javascript">
    $().ready(function () {
      //导出数据到excel
      $("#export_ss").click(function () {
        $(".table2excel").table2excel({
          exclude: ".noExl",
          name: "本月账单信息",
          filename: "本月账单信息",
          fileext: ".xls",
          exclude_img: true,
          exclude_links: false,
          exclude_inputs: true
        });
      });
    });
  </script>
</body>
</html>
