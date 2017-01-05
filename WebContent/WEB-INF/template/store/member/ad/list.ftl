<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>店铺装修</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" href="${base}/resources/store/css/style.css">
  <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>

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
          <li class="active">门店装修</li>
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
                <li class=""><a href="${base}/store/member/article/list.jhtml">店铺公告</a></li>
                <li class="active"><a href="${base}/store/member/ad/list.jhtml">店铺装修</a></li>
                <li class=""><a href="${base}/store/member/tenant/edit.jhtml">基本信息</a></li>
                <li class="pull-left header"><i class="fa fa-th"></i>店铺资料</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form class="form-horizontal" id="listForm" action="list.jhtml" method="get" enctype="multipart/form-data">
                  <div class="row mtb10">
                    <div class="col-sm-7">
                      <div class="btn-group">
                        <button type="button" class="btn btn-primary btn-sm" onclick="javascript:location.href='add.jhtml?tenantId=${tenant.id}';"><i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加</button>
                        <button type="button" class="btn btn-default btn-sm" id="deleteButton"><i class="fa fa-close mr5" aria-hidden="true"></i>删除</button>
                        <button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();"><i class="fa fa-refresh mr5"></i> 刷新</button>
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                            每页显示<span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="pageSizeOption">
                            <li role="presentation" class="[#if page.pageSize==10]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="10">10</a>
                            </li>
                            <li role="presentation" class="[#if page.pageSize==20]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="20">20</a>
                            </li>
                            <li role="presentation" class="[#if page.pageSize==30]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="30">30</a>
                            </li>
                            <li role="presentation" class="[#if page.pageSize==40]active[/#if]">
                              <a role="menuitem" tabindex="-1" val="40">40</a>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                     <!--  <div class="col-sm-5">
                        <div class="box-tools fr">
                          <div class="input-group input-group-sm" style="width: 150px;">
                            <input type="text" name="table_search" class="form-control pull-right" placeholder="Search">
                            <div class="input-group-btn">
                              <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                            </div>
                          </div>
                        </div>
                      </div> -->
                    </div>
                    <div class="box" style="border-top:0px;">
                      <div class="box-body">
                        <table id="listTable" class="table table-bordered table-hover">
                          <thead>
                            <tr>
                              <th><input type="checkbox" id="selectAll"/></th>
                              <th>标题</th>
                              <th>商家</th>
                              <th>类型</th>
                              <th>开始时间</th>
                              <th>结束时间</th>
                              <th>排序</th>
                              <th>操作</th>
                            </tr>
                          </thead>
                          <tbody>
                            [#list page.content as ad]
                            <tr>
                              <td><input type="checkbox" name="ids" value="${ad.id}"/></td>
                              <td>${(ad.title)!}</td>
                              <td>${(ad.tenant.name)!}</td>
                              <td>${message("Ad.Type." + ad.type)}</td>
                              <td>
                                [#if ad.beginDate??]
                                <span title="${ad.beginDate?string("yyyy-MM-dd HH:mm:ss")}">${ad.beginDate}</span>
                                [#else]
                                -
                                [/#if]
                              </td>
                              <td>
                                [#if ad.endDate??]
                                <span title="${ad.endDate?string("yyyy-MM-dd HH:mm:ss")}">${ad.endDate}</span>
                                [#else]
                                -
                                [/#if]
                              </td>
                              <td>${(ad.order)!}</td>
                              <td><a href="edit.jhtml?id=${ad.id}&tenantId=${tenant.id}">[编辑]</a></td>
                            </tr>
                            [/#list]
                          </tbody>
                        </table>
                        <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                          [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                          [#include "/store/member/include/pagination.ftl"]
                          [/@pagination]
                        </div>
                      </div>
                      <!-- /.box-body -->
                    </div>
                  </form>
                  <div class="box-footer">
                    <form role="form" id="tenantForm" action="${base}/store/member/tenant/addImage.jhtml" method="post"
                    enctype="multipart/form-data" >
                    <h3 class="box-title">
                      我的相册
                    </h3>
                    <ul class="mailbox-attachments clearfix">
                      [#if tenant.tenantImages?size>0]
                      [#list tenant.tenantImages as tenantImage]
                      <li id="img${tenantImage_index}">
                        <input type="hidden" name="tenantImages[${tenantImage_index}].source"
                        value="${tenantImage.source}"/>
                        <input type="hidden" name="tenantImages[${tenantImage_index}].large"
                        value="${tenantImage.large}"/>
                        <input type="hidden" name="tenantImages[${tenantImage_index}].medium"
                        value="${tenantImage.medium}"/>
                        <input type="hidden" name="tenantImages[${tenantImage_index}].thumbnail"
                        value="${tenantImage.thumbnail}"/>
                        <input type="hidden" name="tenantImages[${tenantImage_index}].local" />
                        <span class="mailbox-attachment-icon has-img">
                          <img src="${tenantImage.thumbnail}" alt="${tenantImage.thumbnail}">
                        </span>
                        <div class="mailbox-attachment-info" style="text-align:center;" self="delete">
                          <a href="javascript:;" class="mailbox-attachment-name" onclick="delete_photo(this)">删除</a>
                        </div>
                      </li>
                      [/#list]
                      [/#if]
                      <li>
                        <span class="mailbox-attachment-icon has-img" style="font-size:65px;color:#f4f4f4;">+</span>
                        <div class="mailbox-attachment-info" style="text-align:center;" id="addImage">
                          <a href="javaScript:;" class="mailbox-attachment-name" id="browserAddButton">添加</a>
                        </div>
                      </li>
                    </ul>
                  </form> 
                </div>
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
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
  <script>
    $(function () {
      /*添加图片*/
      var productImageIndex = ${(tenant.tenantImages?size)!"0"};
      function addImage(url, local) {
        var html = "<input type='hidden' name='tenantImages[" + productImageIndex + "].local' value="+local+"   maxlength='200' />";
        $("#addImage").append(html);
        productImageIndex++;
      // 删除商品图片
      $("#tenantForm").submit();
    }

    var settings = {
      width: 320,
      height: 320,
      isSubmit: false,
      callback: addImage
    }
    $("#browserAddButton").browser(settings);

  });
    /*图片删除*/
    function delete_photo(obj){
      if(confirm("确定要删除该图片吗？")){
        $(obj).closest("li").remove();
        $("#tenantForm").submit();
      }
    }
  </script>
</body>
</html>
