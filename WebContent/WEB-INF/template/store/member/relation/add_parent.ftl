<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>


    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/store/jcrop/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $tenantCategoryId = $("#tenantCategoryId");
            var $id = $("#id");
            var $licensePhoto = $("#licensePhoto");
            var timeout;
            var $browserButton = $("#browserButton");
        [@flash_message /]
            $browserButton.browser();
            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
            //清除火狐浏览器刷新多出拉下框
            $("select[name='areaId_select']").each(function () {
                if ($(this).val() == "") {
                    $(this).nextAll("select").remove();
                    return false;
                }
            });
            // 店铺分类
            $tenantCategoryId.lSelect({
                url: "${base}/helper/member/tenant/tenantCategory.jhtml"
            });
            // 表单验证
            $inputForm.validate({
                rules: {
                    name: {
                        required: true
                    },
                    areaId: {
                        required: true
                    },
                    tenantCategoryId: {
                        required: true
                    },
                    address: {
                        required: true,
                        maxlength: 100
                    },
                    linkman: {
                        required: true,
                        maxlength: 100
                    },
                    mobile: {
                        required: true,
                        pattern: /^1\d{10}$/
                    },
                    licensePhoto: {
                        required: true
                    }
                },
                messages: {
                    name: {
                        required: "必填"
                    },
                    areaId: {
                        required: "必填"
                    },
                    address: {
                        required: "必填",
                        maxlength: "长度超出"
                    },
                    linkman: {
                        required: "必填",
                        maxlength: "长度超出"
                    },
                    mobile: {
                        required: "必填",
                        pattern: "请输入正确的手机号码"
                    },
                    licensePhoto: {
                        required: "必填"
                    }
                }
            });
        });
    </script>

</head>
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content-header">
            <h1>
                我的供应商
                <small>添加我的供应商</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/helper/member/relation/parent.jhtml">客户管理</a></li>
                <li><a href="${base}/helper/member/relation/parent.jhtml">我的供应商</a></li>
                <li class="active">添加</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-info pb10">
                <div class="box-header with-border">
                    <i class="fa fa-edit"></i>
                    <h3 class="box-title">添加我的供应商</h3>
                </div>
                <!-- /.box-header -->
                <!-- form start -->
                <form class="form-horizontal" role="form" id="inputForm" action="addParent.jhtml" method="post">
                    <input type="hidden" id="type" name="type" value="${type}"/>
                    <input type="hidden" id="status" name="status" value="${status}"/>
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label"><span class="red">*</span>供应商名称</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="name" name="name" placeholder="供应商名称"/>
                        </div>
                    </div>
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label"><span class="red">*</span>商家分类</label>
                        <div class="col-sm-8">
                            <span class="fieldSet">
                        <input type="hidden" id="tenantCategoryId" name="tenantCategoryId"
                               value="" treePath=""/>
                      </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>所属地区</label>
                        <div class="col-sm-8">
                            <span class="fieldSet">
                                <input type="hidden" id="areaId" name="areaId" value=""
                                   treePath=""/>
                             </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>地址</label>
                        <div class="col-sm-8">
                            <input type="text" id="address" name="address" class="form-control"
                                   placeholder="地址"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>联系人</label>
                        <div class="col-sm-8">
                            <input type="text" id="linkman" name="linkman" class="form-control"
                                   placeholder="联系人"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>手机号码</label>
                        <div class="col-sm-8">
                            <input type="text" id="mobile" name="mobile" class="form-control"
                                   placeholder="手机号码"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-2">
                            <button type="submit" class="btn btn-block btn-success">确定</button>

                        </div>
                        <div class="col-sm-offset-0 col-sm-2">
                            <button class="btn btn-block btn-default"
                                    onclick="location.href='parent.jhtml'">返回
                            </button>
                        </div>
                    </div>
                    <div class="kong"></div>
                </form>

            </div>
            <!-- /
          </section>
          <!-- /.content -->
    </div>
</div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
