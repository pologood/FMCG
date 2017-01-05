<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>我收藏的商品</title>
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
        <li><a href="${base}/store/member/union/my_union.jhtml">我的推广</a></li>
        <li class="active">我的收藏</li>
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
                  <li class="pull-left header"><i class="fa fa-th"></i>我的推广</li>
                </ul>
                <div class="tab-content">
                  <!-- /.tab-pane -->
                  <form class="form-horizontal" id="inputForm" method="post">
                    
                    <div class="box" style="border-top:0px;">
                    <div class="box-body">
                      <table id="listTable" class="table table-bordered table-striped">
                        <thead>
                          <tr>
                            <th>商品名称</th>
                            <th>商品价格</th>
                            <th>商品库存</th>
                            <th>商品销量</th>
                            <th>所属店铺</th>
                            <th>推广次数</th>
                            <th>成交次数</th>
                            <th>操作</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as extendCatalog]
                          <tr>
                            <td title="${extendCatalog.product.fullName}">${abbreviate(extendCatalog.product.fullName, 30, "..")}</td>
                            <td>${(extendCatalog.product.price)!}</td>
                            <td>${(extendCatalog.product.availableStock)!}</td>
                            <td>${(extendCatalog.product.sales)!}</td>
                            <td>${(extendCatalog.tenant.name)!}</td>
                            <td>${(extendCatalog.times)!}</td>
                            <td>${(extendCatalog.valume)!0}</td>
                            <td><input type="button" class="btn btn-info btn-sm" value="分享推广"  data-toggle="modal" onclick="share_products(${extendCatalog.product.id})"></td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                      <!-- 分享推广弹框【 -->
                      <input type="hidden" data-toggle="modal" onclick="share_product_alert()" id="share_alert" productId="">
                      <div class="modal fade" id="share_product" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display:none;">
                        <div class="modal-dialog">
                          <form class="form-horizontal" role="form" action="" method="post">
                            <div class="modal-content" style=" border-radius: 5px;width:399px;">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title">分享推广</h4>
                              </div>
                              <div class="modal-body" style="color:#428bca;">
                                <div style="text-align: center;">
                                  <img src="" style="width:200px;height:200px;" id="weixin_qr">
                                </div>
                                <h3 style="text-align:center;font-weight:bold;">微信扫描二维码分享推广</h3>
                              </div>
                            </div>
                          </form>
                        </div>
                      </div>
                      <!--申请加入商盟弹框】 -->
                      <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/store/member/include/pagination.ftl"]
                        [/@pagination]
                      </div>
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
  /*控制弹出框,并生称二维码*/
  // function alert_share_product(obj,id){
  //   $.ajax({
  //     url:'${base}/store/member/union/share_product.jhtml',
  //     data:{
  //       productId:id
  //     },
  //     type:'post',
  //     dataType:'json',
  //     success:function(message){
  //       if(message.type=="success"){
  //         $(obj).attr("data-target","#share_product");
  //         $("#weixin_qr").attr("src","${base}/store/member/union/make_qrcode.jhtml?productId="+id);
  //       }else{
  //         window.document.reload();
  //       }
  //     }
  //   });
  // }
  /*分享推广*/
  function share_products(id){
    $.ajax({
      url:'${base}/store/member/union/share_product.jhtml',
      data:{
        productId:id
      },
      type:'post',
      dataType:'json',
      success:function(message){
        if(message.type=="success"){
          $("#share_alert").attr("productId",id);
          $("#share_alert").click();
        }else{
          window.document.reload();
        }
      }
    });
  }
  /*控制弹出框,并生称二维码*/
  function share_product_alert(){
    $("#share_alert").attr("data-target","#share_product");
    $("#weixin_qr").attr("src","${base}/store/member/union/make_qrcode.jhtml?productId="+$("#share_alert").attr("productId"));
  }
</script>
</body>
</html>
