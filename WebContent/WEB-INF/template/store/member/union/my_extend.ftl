<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>我的推广</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

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
        商盟管理
        <small>处理商盟管理的信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        <li><a href="${base}/store/member/union/my_union.jhtml">商盟管理</a></li>
        <li class="active">我的推广</li>
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
                  <li class=""><a href="${base}/store/member/union/my_product.jhtml">我收藏的商品</a></li>
                  <li class="active"><a href="${base}/store/member/union/my_extend.jhtml">我的推广</a></li>
                  <li class="pull-left header"><i class="fa fa-th"></i>我的推广</li>
                </ul>
                <div class="tab-content">
                  <!-- /.tab-pane -->
                  <form class="form-horizontal" id="inputForm" method="post">
                    
                   
                  </form>
                  <!-- /.tab-pane -->
                </div>
                <!-- /.tab-content -->
              </div>
              <!-- nav-tabs-custom -->
            </div>
        </div>
         <!-- <div class="con-con"> -->
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
  
</script>
</body>
</html>
