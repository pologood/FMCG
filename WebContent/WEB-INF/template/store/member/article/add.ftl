<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>店铺公告-添加</title>
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
        <li><a href="${base}/store/member/article/list.jhtml">店铺公告</a></li>
        <li class="active">添加</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs pull-right">
              <li class="active"><a href="${base}/store/member/article/list.jhtml">店铺公告</a></li>
              <li class=""><a href="${base}/store/member/ad/list.jhtml">店铺装修</a></li>
              <li class=""><a href="${base}/store/member/tenant/edit.jhtml">基本信息</a></li>
              <li class="pull-left header"><i class="fa fa-th"></i>店铺资料</li>
            </ul>

            <div class="tab-content">
              <form class="form-horizontal" role="form" id="inputForm" action="save.jhtml" method="post" 
                    enctype="multipart/form-data">
                <div class="form-group">
                  <label class="col-sm-2 control-label"><span class="red">*</span>标题</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="title">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">页面标题</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="seoTitle">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">页面描述</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="seoDescription">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">页面关键字</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="seoKeywords">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">作者</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="author">
                  </div>
                  
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">主题图片</label>
                  <div class="col-sm-4">
                    <input type="text" class="form-control" name="image">
                  </div>
                  <div class="col-sm-2">
                    <button type="button" class="btn btn-block btn-default" id="browserButton">选择文件</button>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label"><span class="red">*</span>内容</label>
                  <div class="col-sm-8">
                    <textarea id="editor" name="content" class="editor"
                              style="width: 100%;"></textarea>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputSkills" class="col-sm-2 control-label">设置</label>
                  <div class="col-sm-8">
                    <div class="checkbox-inline" >
                      <label>
                        <input type="checkbox" name="isTop" value="true">
                        是否置顶
                        <input type="hidden" name="_isTop" value="false"/>
                      </label>
                    </div>

                    <div class="checkbox-inline" >
                      <label>
                        <input type="checkbox" name="isPublication" value="true" checked="checked">
                        是否发布
                        <input type="hidden" name="_isPublication" value="false"/>
                      </label>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputSkills" class="col-sm-2 control-label">标签</label>
                  <div class="col-sm-8">
                    [#list tags as tag]
                    <div class="checkbox-inline" >
                      <label>
                        <input type="checkbox" name="tagIds" value="${tag.id}">${tag.name}
                      </label>
                    </div>
                    [/#list]
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
<script type="text/javascript" src="${base}/resources/store/js/editor/kindeditor.js"></script>

<script type="text/javascript">
  $().ready(function () {
      //图片上传
      var settings = {
          width: 360,
          height: 360
      };
      $("#browserButton").browser(settings);
      // 表单验证
      $("#inputForm").validate({
          rules: {
              title: "required",
              content: "required",
          },
          messages: {
              title: "${message("admin.validate.required")}",
              content: "${message("admin.validate.required")}"
          }
      });
  });
</script>
</body>
</html>
