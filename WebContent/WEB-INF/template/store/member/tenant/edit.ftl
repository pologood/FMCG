<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>基本信息</title>
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
        我的店铺
        <small>显示和管理店铺内的信息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        <li><a href="${base}/store/member/tenant/edit.jhtml">店铺管理</a></li>
        <li class="active">基本信息</li>
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
                  <li class=""><a href="${base}/store/member/ad/list.jhtml">店铺装修</a></li>
                  <li class="active"><a href="${base}/store/member/tenant/edit.jhtml">基本信息</a></li>
                  <li class="pull-left header"><i class="fa fa-th"></i>店铺资料</li>
                </ul>
                <div class="tab-content">
                  <!-- /.tab-pane -->
                  <form class="form-horizontal" id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${(tenant.id)!}"/>
                    <input type="hidden" name="tenantType" value="tenant"/>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label"><span class="red">*</span>店铺名称</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" id="" name="shortName"
                                     value="${(tenant.shortName)!}">
                      </div>
                      <div class="col-sm-2"></div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label"><span class="red">*</span>联系人</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" id="" name="linkman" value="${(tenant.linkman)!}">
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label"><span class="red">*</span>联系电话</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" id="" name="telephone" value="${(tenant.telephone)!}">
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">QQ</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" id="" name="qq" value="${(tenant.qq)!}">
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label">绑定域名</label>
                      <div class="col-sm-5">
                        <input type="text" class="form-control" id="inputName" name="domain" value="[#if tenant.domain??]${(tenant.domain)!}[/#if]">
                      </div>
                      <div class="col-sm-3">
                        <div>例：localhost（确定后不可修改）</div>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputExperience" class="col-sm-2 control-label">店铺简介</label>
                      <div class="col-sm-8">
                        <textarea class="form-control" id="inputExperience" name="introduction" >${(tenant.introduction)!}</textarea>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputName" class="col-sm-2 control-label"><span class="red">*</span>店铺地址</label>
                      <div class="col-sm-8">
                        <input type="text" class="form-control" id="inputName" name="address" value="${(tenant.address)!}">
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputSkills" class="col-sm-2 control-label"><span class="red">*</span>所属地区</label>
                      <div class="col-sm-10">
                      <input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}"
                                 treePath="${(tenant.area.treePath)!}"/>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputSkills" class="col-sm-2 control-label"><span class="red">*</span>主营类目</label>
                      <div class="col-sm-4">
                        <select class="form-control" name="tenantCategoryId">
                          <option value="#">请选择...</option>
                          [#list tenantCategorys as category]
                          <option value="${(category.id)!}" [#if category.id==tenant.tenantCategory.id]selected="true"[/#if]>${(category.name)!}</option>
                          [/#list]
                        </select>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputSkills" class="col-sm-2 control-label">店铺头像</label>
                      <div class="col-sm-4">
                        <input type="text" class="form-control" id="" name="logo" value="${tenant.logo}" title="${tenant.logo}">
                      </div>
                      <div class="col-sm-2">
                        <button type="button" class="btn btn-block btn-default" id="browserLogoButton">选择文件</button>
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
												        <img src="${tenant.logo}" alt="" width="400" height="400"/>
												      </div>														      
												    </div>
												  </div>
												</div>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputSkills" class="col-sm-2 control-label">门店招牌</label>
                      <div class="col-sm-4">
                        <input type="text" class="form-control" id="" name="thumbnail" value="${tenant.thumbnail}" title="${tenant.logo}">
                      </div>
                      <div class="col-sm-2">
                        <button type="button" class="btn btn-block btn-default" id="browserButton">选择文件</button>
                      </div>
                      <div class="col-sm-1">
                        <button type="button" class="btn btn-block btn-default" data-toggle="modal" data-target="#myModal_2">查看</button> 
                        <!-- Modal -->
                        <div class="modal fade" id="myModal_2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel_2" aria-hidden="true">
                          <div class="modal-dialog">
                            <div class="modal-content" style="border-radius: 5px;">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title" id="myModalLabel_2">查看图片</h4>
                              </div>
                              <div class="modal-body" style="text-align: center;">
                                <img src="${tenant.thumbnail}" alt="" width="400" height="400"/>
                              </div>                                  
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="form-group">
                      <label for="inputSkills" class="col-sm-2 control-label"></label>
                      <div class="col-sm-8">
                        <div class="checkbox-inline" >
                          <label>
                            <input type="checkbox" name="noReason" [#if tenant.noReason] checked="checked"[/#if]>
                            7天退货
                            <input type="hidden" name="noReason" value="false"/>
                          </label>
                        </div>

                        <div class="checkbox-inline" >
                          <label>
                            <input type="checkbox" name="tamPo" [#if tenant.tamPo] checked="checked"[/#if]>
                            担保交易
                            <input type="hidden" name="tamPo" value="false"/>
                          </label>
                        </div>

                        <div class="checkbox-inline">
                          <label>
                            <input type="checkbox" name="toPay" [#if tenant.toPay] checked="checked"[/#if]>
                            货到付款
                            <input type="hidden" name="toPay" value="false"/>
                          </label>
                        </div>
                      </div>
                    </div> 
                    <div class="form-group">
                      <div class="col-sm-offset-2 col-sm-2">
                        <button type="submit" class="btn btn-block btn-primary">提交</button>
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
  $(function(){
    // 地区选择
    var $areaId = $("#areaId");
    $areaId.lSelect({
        url: "${base}/common/area.jhtml"
    });
    //店铺头像上传
    var settings_logo = {
      width: 120,
      height: 120
    };
    $("#browserLogoButton").browser(settings_logo);
    //店铺招牌上传
    var settings = {
        width: 360,
        height: 360
    };
    $("#browserButton").browser(settings);
    // 表单验证
    var $inputForm = $("#inputForm");
    $inputForm.validate({
        rules: {
            "shortName": {
                required: true
            },
            "tenantType": {
                required: true
            },
            "tenantCategoryId": {
                required: true
            },
            "domain": {
                remote: {
                    url: "check_domain.jhtml",
                    cache: false
                }
            },
            "areaId": {
                required: true
            },
            "address": {
                required: true
            },
            "linkman": {
                required: true
            },
            "telephone": {
                required: true,
                pattern: /^(1[3,5,8,7]{1}[\d]{9})|(((400)-(\d{3})-(\d{4}))|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{3,7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$|^([ ]?)$/
            },
            "qq": {
                pattern: /^\d+$/
            }
        },
        messages: {
            shortName: "必填",
            tenantType: "必填",
            tenantCategoryId: "必填",
            domain: {
                remote: "域名已存在"
            },
            areaId: "必填",
            address: "必填",
            linkman: "必填",
            telephone: {
                required: "必填",
                pattern: "请输入正确的手机号码"
            },
            qq: "请输入正确的qq号"
        }
    });

  });
</script>
</body>
</html>
