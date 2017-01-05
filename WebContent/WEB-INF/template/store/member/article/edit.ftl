<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>店铺公告-编辑</title>
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
        <li class="active">编辑</li>
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
              <form class="form-horizontal" role="form" id="inputForm" action="update.jhtml" method="post" 
                    enctype="multipart/form-data">
                <input type="hidden" name="id" value="${article.id}" />
                <div class="form-group">
                  <label class="col-sm-2 control-label"><span class="red">*</span>标题</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="title" value="${article.title}">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">页面标题</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="seoTitle" value="${article.seoTitle}">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">页面描述</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="seoDescription" value="${article.seoDescription}">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">页面关键字</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="seoKeywords" value="${article.seoKeywords}">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">作者</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" name="author" value="${article.author}">
                  </div>
                  
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">主题图片</label>
                  <div class="col-sm-4">
                    <input type="text" class="form-control" name="image" value="${article.image}" >
                  </div>
                  <div class="col-sm-2">
                    <button type="button" class="btn btn-block btn-default" id="browserButton">选择文件</button>
                  </div>
                  <div class="col-sm-1">
                    <button type="button" class="btn btn-block btn-default" data-toggle="modal" data-target="#myModal">查看</button> 
                    <!-- Modal -->
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content" style="border-radius: 5px;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">查看图片</h4>
                          </div>
                          <div class="modal-body" style="text-align: center;">
                            <img src="${article.image}" alt="" width="400" height="400"/>
                          </div>                                  
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">内容</label>
                  <div class="col-sm-8">
                    <textarea class="form-control" id="editor" name="content">${article.content?html}</textarea>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputSkills" class="col-sm-2 control-label">设置</label>
                  <div class="col-sm-8">
                    <div class="checkbox-inline" >
                      <label>
                        <input type="checkbox" name="isTop" value="true"[#if article.isTop] checked="checked"[/#if]>
                        是否置顶
                        <input type="hidden" name="_isTop" value="false"/>
                      </label>
                    </div>

                    <div class="checkbox-inline" >
                      <label>
                        <input type="checkbox" name="isPublication" value="true" [#if article.isPublication] checked="checked"[/#if]>
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
                        <input type="checkbox" name="tagIds" value="${tag.id}" [#if article.tags?seq_contains(tag)] checked="checked"[/#if]>${tag.name}
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
<!-- <script type="text/javascript" src="${base}/resources/store/js/editor/kindeditor.js"></script> -->


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
