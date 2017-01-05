<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>AdminLTE 2 | Dashboard</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
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
        我的购物屏
        <small>显示和管理我的购物屏</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i>店铺管理</a></li>
        <li class="active">我的购物屏</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
            <!-- Left col -->
            <div class="col-md-12">
              <!-- Custom Tabs (Pulled to the right) -->
              <div class="nav-tabs-custom">
                <ul class="nav nav-tabs pull-right">
                  <li class="pull-left header"><i class="fa fa-th"></i>购物屏详情</li>
                </ul>
                <div class="tab-content">
                  <form class="form-horizontal" id="inputForm" action="javascript:;" method="post">
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">累计佣金</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" value="${equipment.total_amount}" disabled="true">
                      </div>
                      <div class="col-sm-2"></div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">累计销售额</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" value="${equipment.total_sale_amount}" disabled="true">
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">状态</label>
                      <div class="col-sm-8">
                        <span style="line-height:2.42857143;">
                          [#if equipment.setStatus=="success"]
                          已开启
                          [#else]
                          未开启
                          [/#if]
                        </span>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">店铺名称</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" value="${equipment.name}" disabled="true">
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">标签</label>
                      <div class="col-sm-8">
                        [#list equipment.tags as tag]
                        ${tag.name}[#if tag_has_next],[/#if]
                        [/#list]
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputExperience" class="col-sm-2 control-label">详细地址</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" value="${equipment.address}" disabled="true">
                      </div>
                    </div>
                    [#list equipment.employees as employee]
                    <div class="form-group">
                      <label for="inputSkills" class="col-sm-2 control-label">${employee.nickName}</label>
                      <div class="col-sm-8">
                        <span style="line-height:2.42857143;">
                          [#list employee.role?split(",") as role]
                        [#if role=="owner"]店主[#if role_has_next],[/#if][/#if]
                        [#if role=="manager"]店长[#if role_has_next],[/#if][/#if]
                        [#if role=="guide"]导购[#if role_has_next],[/#if][/#if]
                        [#if role=="account"]财务[#if role_has_next],[/#if][/#if]
                        [#if role=="cashier"]收银[#if role_has_next],[/#if][/#if]
                        [/#list]
                        </span>
                        
                      </div>
                    </div>
                    [/#list]
                    <div class="form-group">
                      <div class="col-sm-offset-2 col-sm-2">
                        <button type="button" class="btn btn-block btn-primary" onclick="javascript:history.go(-1)">返回</button>
                      </div>
                    </div>
                  </form>
                  <!-- /.tab-pane -->
                </div>
                <!-- /.tab-content -->
              </div>
              <!-- nav-tabs-custom -->
            </div>
        </div>

    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  [#include "/store/member/include/footer.ftl"]
</div>
[#include "/store/member/include/bootstrap_js.ftl"]
</body>
</html>
