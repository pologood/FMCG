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
[#--<link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>--]
[#--<link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>--]
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <style type="text/css">
        .brands label {
            width: 150px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $browserButton = $("#browserButton");

            var $selectedbrandvalue = $("#selectedbrandvalue");
            var $selectedbrand = $("#selectedbrand");
            var $selectedAllbrand = $("#selectedAllbrand");
            var $selectedCleanbrand = $("#selectedCleanbrand");
            $browserButton.browser();
        [@flash_message /]

            function queryLoadBrand(brandName, checked) {
                $.ajax({
                    url: "${base}/b2b/brand/search.jhtml",
                    type: "GET",
                    data: {name: brandName},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.length > 1) {
                            checked = "";
                        }
                        var $brandIds = $("input[name='brandIds']");
                        var $brandstd = $(".brands td");
                        for (var i = 0; i < message.length; i++) {
                            var flag = true;
                            $brandIds.each(function () {
                                var $this = $(this);
                                if ($this.val() == message[i].id) {
                                    flag = false;
                                }
                            });

                            if (flag) {
                                $brandstd.append("<label><input type='checkbox' name='brandIds' value='" + message[i].id + "' " + checked + " />" + message[i].name + "</label>");
                            }
                        }
                    }
                });
            }

            $selectedbrand.click(function () {
                if ($selectedbrandvalue.val().replace(/\s/g, "") == "") {
                    return;
                } else {
                    queryLoadBrand($selectedbrandvalue.val(), "checked='checked'");
                }
            });
            $selectedAllbrand.click(function () {
                queryLoadBrand("", "");
            });
            $selectedCleanbrand.click(function () {
                var $label = $(".brands td label");
                $label.each(function () {
                    var $this = $(this);
                    if (!$this.find("input")[0].checked) {
                        $this.remove();
                    }
                });
            });
            $("#level1").click(function () {
                $("#parentTr").hide();
            });
            $("#level2").click(function () {
                $("#parentTr").show();
            });

            //店铺招牌上传
            var settings_logo = {
                width: 120,
                height: 120
            };
            $("#browserLogoButton").browser(settings_logo);
            // 表单验证
            $inputForm.validate({
                rules: {
                    name: "required",
                    order: "digits"
                },
                messages: {
                    name: "${message("admin.validate.required")}"
                }
                ,
                submitHandler: function (form) {
                    var _pass = true;
                    $("input[name='grade']").each(function () {
                        if ($(this).val() > 0 && $(this).attr("checked") == "checked" && $("select[name='parentId']").val() == "") {
                            $.message("error", "请选择上级！");
                            _pass = false;
                            return;
                        }
                    });
                    if (!_pass) {
                        return false;
                    }
                    // addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                    form.submit();
                }
            });

            $("input[name='grade']").change(function () {
                $("input[name='grade']").each(function () {
                    if ($(this).val() == 0) {
                        if ($(this).attr("checked") == "checked") {
                            $("#parentTr").hide();
                        } else {
                            $("#parentTr").show();
                        }
                    }
                });
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
                商品分类
                <small>编辑我发布的商品分类</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/product_category/list.jhtml">我的商品</a></li>
                <li><a href="${base}/store/member/product_category/list.jhtml">商品分类</a></li>
                <li class="active">编辑</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-info pb10">
                <div class="box-header with-border">
                    <i class="fa fa-edit"></i>
                    <h3 class="box-title">编辑我发布的商品分类</h3>
                </div>
                <!-- /.box-header -->
                <!-- form start -->
                <form class="form-horizontal" role="form" id="inputForm" action="update.jhtml" method="post">
                    <input type="hidden" name="id" value="${productCategory.id}"/>
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label">*名称:</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="name" value="${productCategory.name}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">缩例图:</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" value="${productCategory.image}" name="image"
                                   readonly="true"/>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="btn btn-block btn-default" id="browserLogoButton">选择文件</button>
                        </div>
                        <div class="col-sm-1">
                            <button type="button" class="btn btn-block btn-default" data-toggle="modal"
                                    data-target="#myModal">查看
                            </button>
                            <!-- Modal -->
                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
                                 aria-labelledby="myModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content" style="border-radius: 5px;">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span
                                                    aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                                            </button>
                                            <h4 class="modal-title" id="myModalLabel">查看图片</h4>
                                        </div>
                                        <div class="modal-body" style="text-align: center;">
                                            <img src="${productCategory.image}" alt="" width="400" height="400"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">页面标题:</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" value="${productCategory.seoTitle}"
                                   name="seoTitle"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">页面关键词:</label>
                        <div class="col-sm-8">
                            <input type="text" name="seoKeywords" class="form-control"
                                   value="${productCategory.seoKeywords}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">页面描述:</label>
                        <div class="col-sm-8">
                            <input type="text" name="seoDescription" class="form-control"
                                   value="${productCategory.seoDescription}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">排序:</label>
                        <div class="col-sm-8">
                            <input type="text" name="order" class="form-control" value="${productCategory.order}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">层级:</label>
                        <div class="col-sm-9 pt5 label-m15">
                            <div class="radio">
                                <label>
                                    <input type="radio" name="optionsRadios" id="level1" value="0"
                                           [#if productCategory.grade==0]checked="checked"[/#if]/>
                                    一级
                                </label>
                            </div>
                            <div class="radio">
                                <label>
                                    <input type="radio" name="optionsRadios" id="level2" value="1"
                                           [#if productCategory.grade==1]checked="checked"[/#if]/>
                                    二级
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" id="parentTr" [#if productCategory.grade==0]style="display: none"[/#if]>
                        <label class="col-sm-2 control-label">所属上级:</label>
                        <div class="col-sm-2">
                            <select name="parentId" class="form-control valid">
                                <option value=""
                                        selected="selected"
                                >请选择
                                </option>
                            [#list productCategoryTree as parent]
                                [#if parent.grade==0&&parent.id!=productCategory.id]
                                    <option value="${parent.id}"
                                        [#if productCategory.parent??&&productCategory.parent.id==parent.id]
                                            selected="selected"[/#if]
                                    >${parent.name}</option>
                                [/#if]
                            [/#list]
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-2">
                            <button type="submit" class="btn btn-block btn-success">确定</button>
                        </div>
                        <div class="col-sm-offset-0 col-sm-2">
                            <button onclick="location.href='list.jhtml'" class="btn btn-block btn-default">返回</button>
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
