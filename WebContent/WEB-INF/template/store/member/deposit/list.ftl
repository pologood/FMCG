<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>我的账单</title>
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
          账户充值
          <small>使用您已开通网上银行服务的银行卡进行充值</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">我的账单</li>
        </ol>
      </section>
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="active"><a href="${base}/store/member/deposit/list.jhtml">我的账单</a></li>
                <li><a href="${base}/store/member/deposit/fill.jhtml">账户充值</a></li>
                <li class="pull-left header"><i class="fa fa-file-text"></i>我的账单</li>
              </ul>
              <div class="tab-content">
                <form id="listForm" action="list.jhtml" method="get">
                  <table class="table table-bordered table-striped">
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
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
</body>
</html>
