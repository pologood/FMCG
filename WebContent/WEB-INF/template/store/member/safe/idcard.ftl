<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>实名认证</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
  <link href="${base}/resources/store/css/style.css" type="text/css" rel="stylesheet"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">
    [#include "/store/member/include/header.ftl"]
    [#include "/store/member/include/menu.ftl"]
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <h1>
          实名认证
          <small>用于提升账号的安全性和信任级别。认证后的有卖家记录的账号不能修改认证信息</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">实名认证</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="pull-left header"><i class="fa fa-user"></i>实名认证</li>
              </ul>
              <div class="tab-content" >
                <form class="form-horizontal" id="inputForm" action="idcard.jhtml" method="post">
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">身份证号</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control"  id="no" name="no" value="${idcard.no}">
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">姓名</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" name="name" value="${member.name}" [#if idcard.authStatus=="success"]disabled[/#if]>
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">身份证地址</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control"  name="address" value="${idcard.address}" [#if idcard.authStatus=="success"]disabled[/#if]>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">正面图扫描</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control"  name="pathFront" value="${idcard.pathFront}">
                    </div>
                    <div class="col-sm-2">
                      <button type="button" class="btn btn-block btn-default" id="browserFront">选择文件</button>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">反面图扫描</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" name="pathBack" value="${idcard.pathBack}">
                    </div>
                    <div class="col-sm-2">
                    <button type="button" class="btn btn-block btn-default" id="browserBack">选择文件</button>
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">状态</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control"  
                        value="[#if idcard.authStatus=='success']已完成[#elseif idcard.authStatus=='fail']已驳回[#elseif idcard.authStatus=='wait']待审核[#elseif idcard.authStatus=='none']未认证[/#if]">
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">备注</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" name="memo" value="${idcard.memo}">
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-2">
                      <button type="submit" class="btn btn-block btn-primary">提交</button>
                    </div>
                    <div class="col-sm-offset-0 col-sm-2">
                      <button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
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
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/jquery.lSelect.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
  <script type="text/javascript">
    $().ready(function() {
      var $inputForm = $("#inputForm");
      var $browserFront = $("#browserFront");
      var $browserBack = $("#browserBack");
      var $submit = $("#submit");
      var $url = location.href;
      var settings = {
        width: 360,
        height: 240
      }
      $browserFront.browser(settings);
      $browserBack.browser(settings);

      //返回上一页
      $("#returnUrl").click(function(){
        if ($url.indexOf("urlid=1") > 0)
        {
          location.href = "${base}/store/member/profile/edit.jhtml";
        }else {
          location.href = "index.jhtml";
        }
      });

      // 表单验证
      $inputForm.validate({
        rules: {
          no: {
            required: true,
            remote:{
              type:"POST",
              url:"IdcardCheck.jhtml",
              data:{
                no:function(){
                  return $("#no").val();
                }
              }
            }
          },
          address: {
            required: true
          },
          name: {
            required: true
          },
          pathFront: {
            required: true
          },
          pathBack: {
            required: true
          }
        },
        messages:{
          no:  {
            required:'必填',
            remote:"身份证号码不合法！"
          },
          address: {
            required: '必填'
          },
          name: {
            required: '必填'
          },
          pathFront: {
            required: '必填'
          },
          pathBack: {
            required: '必填'
          }
        },
        errorPlacement: function(error, element) {
          error.insertAfter(element);
        },
        beforeSend: function() {
          $submit.prop("disabled", true);
        },
        success: function(message) {
          $submit.prop("disabled", false);
        }
      });
});
</script>
</body>
</html>
