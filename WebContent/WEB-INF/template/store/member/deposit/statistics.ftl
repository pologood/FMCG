<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>账单统计</title>
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
          <small>统计当月的收入、支出情况</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">账单统计</li>
        </ol>
      </section>
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="pull-left header"><i class="fa fa-fw fa-file-text"></i>账单统计</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="statistics.jhtml" method="get">
                  <div class="row mtb10">
                    <div class="col-sm-5">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();"><i class="fa fa-refresh mr5"></i> 刷新
                        </button>
                        <button type="button" class="btn btn-default btn-sm" id="export_ss"><i class="fa fa-print mr5"></i> 导出
                        </button>
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">月份<span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                            <li role="presentation">
                              <a href="statistics.jhtml?month=currentMonth">本月</a>
                            </li>
                            <li role="presentation">
                              <a href="statistics.jhtml?month=lastMonth">上月</a>
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
                            <th>类型</th>
                            <th>收入金额</th>
                            <th>支出金额</th>
                            <th>当前余额</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page as deposit]
                          <tr>
                            <td>${deposit.type}</td>
                            <td>${currency(deposit.credit,true)}</td>
                            <td>${currency(deposit.debit,true)}</td>
                            <td>${currency(deposit.balance,true)}</td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
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
          name: "账单统计",
          filename: "账单统计",
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
