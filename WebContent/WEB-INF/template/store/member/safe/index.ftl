<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>个人中心</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link type="text/css" rel="stylesheet" href="${base}/resources/store/css/common.css">
  <link type="text/css" rel="stylesheet" href="${base}/resources/store/css/style.css">
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
          个人中心
        </h1>
        <ol class="breadcrumb">
          <li><a href="#"><i class="fa fa-dashboard"></i>首页</a></li>
          <li class="active">个人中心</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="pull-left header"><i class="fa fa-truck"></i>你的安全服务</li>
              </ul>
              <div class="tab-content" style="margin-top:66px;">
                <form class="form-horizontal" id="inputForm" action="idcard.jhtml" method="post">
                  <div class="form-group">
                    <label  class="col-sm-2 control-label" style="font-size:18px;">手机号码：</label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" value="${mosaic(member.mobile, 3, '~~~')}" disabled="true">
                    </div>
                    <div class="col-sm-4">
                      <a class="lh35" href="${base}/store/member/safe/password.jhtml">修改密码</a>
                    </div>
                  </div>
                  <div class="form-group mb5">
                    <label  class="col-sm-2 control-label" style="font-size:18px;">上次登录：</label>
                    <div class="col-sm-8">
                      <p class="lh35">${member.loginDate?string("yyyy-MM-dd HH:mm:ss")}（不是您登录的？请<a href="${base}/store/login.jhtml">点击这里重新登录</a>）</p>
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label" style="font-size:18px;">安全等级：</label>
                    <div class="col-sm-1">
                      <p class="lh35">中</p>
                    </div>
                    <div class="col-sm-3">
                      <p class="lh35"><img src="${base}/resources/store/images/aqsz_pic_bg3.jpg"/></p>
                    </div>
                    <div class="col-sm-4">
                      <p class="lh35">完成密保设置，提升账户安全</p>
                    </div>
                  </div>
                </form>
                <div class="box-body table-responsive no-padding no-border" style="margin:66px 130px;">
                  <table class="table table-hover no-border" style="font-size:20px;">
                    <tr>
                      <td>[#if member.authStatus!="success"]<i class="fa fa-warning yellow"></i>[#else]<i class="fa fa-check green">[/#if] ${message("Member.AuthStatus."+member.authStatus)}</td>
                      <td>身份认证</td>
                      <td style="text-align:left;color:gray;">用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息。</td>
                      <td><a href="${base}/store/member/safe/idcard.jhtml">[#if member.authStatus=="none"]去认证[#else]查看[/#if]</a></td>
                    </tr>
                    <tr>
                      <td><i class="fa fa-check green"></i>已设置</td>
                      <td>支付密码</td>
                      <td style="text-align:left;color:gray;">安全性高的密码可以使账号更安全。建议您定期更换密码，且长度只能是6位的数字密码。</td>
                      <td><a href="${base}/store/member/safe/paymentPassword.jhtml">修改</a></td>
                    </tr>
                    <tr>
                      <td> [#if member.bindMobile!="binded"]<i class="fa fa-warning yellow">[#else]<i class="fa fa-check green"></i>[/#if][#if member.bindMobile=="binded"]已绑定[#else]未绑定[/#if]</td>
                      <td>手机绑定</td>
                      <td style="text-align:left;color:gray;">用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息。</td>
                      <td><a href="${base}/store/member/safe/bindmobile.jhtml">[#if member.bindMobile=="binded"]解绑[#else]去绑定[/#if] </a></td>
                    </tr>
                  </table>
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
    </body>
    </html>
