<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>店铺装修-添加</title>
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
      <h1>我的店铺<small>维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等</small>  </h1>
      <ol class="breadcrumb">
        <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        <li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
        <li><a href="${base}/store/member/ad/list.jhtml">门店装修</a></li>
        <li class="active">添加</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs pull-right">
              <li>
                <a href="/store/member/article/list.jhtml">店铺公告</a>
              </li>
              <li class="active">
                <a href="/store/member/ad/list.jhtml">店铺装修</a>
              </li>
               <li>
                <a href="/store/member/edit.jhtml">基本信息</a>
              </li>
              <li class="pull-left header"><i class="fa fa-truck"></i>店铺装修</li>
            </ul>
            <div class="tab-content">
              <form class="form-horizontal" id="inputForm" role="form" action="save.jhtml" method="post" >
                <input type="hidden" id="type" name="type" value="image"/>
                <div class="form-group">
                  <label class="col-sm-2 control-label"><span class="red">*</span>标题</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="title" >
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">广告位</label>
                  <div class="col-sm-8">
                    <select class="form-control" name="adPositionId">
                      [#list adPositions as adPosition]
                        [#if adPosition!=null]
                            <option value="${adPosition.id}">
                            ${adPosition.name} [${adPosition.width}× ${adPosition.height}]
                            </option>
                        [/#if]
                      [/#list]
                    </select> 
                  </div>
                </div>
                <div class="form-group" >
                  <label class="col-sm-2 control-label">广告类型</label>
                  <div class="col-sm-8">
                    <select class="form-control" id="type" name="linkType">
                      [#list linkTypes as linkType]
                        <option value="${linkType}"[#if linkType_index == 0]selected="selected"[/#if]>
                          ${message("Ad.LinkType." + linkType)}
                        </option>
                      [/#list]
                    </select> 
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">路径</label>
                  <div class="col-sm-4">
                    <input type="text" class="form-control" name="path" readonly="true">
                  </div>
                  <div class="col-sm-2">
                    <button type="button" class="btn btn-block btn-default" id="browserButton">选择文件</button>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">链接地址</label>
                  <div class="col-sm-2">
                    <input type="text" class="form-control"  name="url" id="productUrl" readonly="true">
                  </div>
                  <div class="col-sm-6">
                    <select class="form-control" name="linkId" onchange="get_product_id(this)">
                      <option value=''>请选择商品</option>
                      [#if products??]
                          [#list products as product]
                              <option value='${product.id}' title="${product.name}">${product.name}</option>
                          [/#list]
                      [/#if]
                    </select> 
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">排序</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control"  name="order">
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
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
<script type="text/javascript">
    $().ready(function () {
        //文件上传
        var settings = {};
        $("#browserButton").browser(settings);
        // 表单验证
        $("#inputForm").validate({
            rules: {
                title: "required",
                adPositionId: "required",
                path: "required",
                file: "required",
                order: "digits"
            },
            messages: {
                title: "${message("admin.validate.required")}",
                adPositionId: "${message("admin.validate.required")}",
                file: "${message("admin.validate.required")}",
                order: "序号如：1、2、3..."
            }
        });
    });
    function get_product_id(obj){
      [#if versionType==0]
        $("#productUrl").val("${base}/b2c/product/detail/"+$(obj).val()+".jhtml");
      [#elseif versionType==1]
        $("#productUrl").val("${base}/b2b/product/detail/"+$(obj).val()+".jhtml");
      [/#if]
    }
</script>
</body>
</html>
