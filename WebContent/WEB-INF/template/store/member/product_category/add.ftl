<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
    <title> 首页</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    [#include "/store/member/include/bootstrap_css.ftl" /]
     <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    [#--<!--- <link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>--->--]
    [#--<link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>--]
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <style type="text/css">
        div.xxBrowser .browserBar {
            position:relative;
        }
        div.xxBrowser .browserBar a:nth-child(3){
            position: absolute;
            right: 240px;
            top: 16px;
        }
        div.xxBrowser .browserBar a:nth-child(4){
            position: absolute;
            right: 135px;
            top: 16px;
        }
        div.xxBrowser .browserBar form a:nth-child(3){
            position: static;
            right: 0;
            top: 0;
        }
    </style>
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
                商品分类
                <small>添加我发布的商品分类</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i>我的商品</a></li>
                <li class="active">商品分类</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- Left col -->
                <div class="col-md-12">
                    <!-- Custom Tabs (Pulled to the right) -->
                    <div class="nav-tabs-custom">
                        <div class="tab-content">
                            <!-- /.tab-pane -->
                            <form class="form-horizontal" id="inputForm" action="save.jhtml" method="post">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">*名称:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="name" id="name"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">缩例图:</label>
                                    <div class="col-sm-4">
                                        <input type="text" name="image" class="form-control" maxlength="200"
                                               title="${message("admin.product.imageTitle")}" readonly="true"/>
                                    </div>
                                    <div class="col-sm-2">
                                        <input type="button" id="browserButton" class="btn btn-default"
                                               value="${message("admin.browser.select")}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">页面标题:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="seoTitle">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">页面关键词:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="seoKeywords">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">页面描述:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="seoDescription">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">排序:</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="order">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">层级:</label>
                                    <div class="col-sm-9 pt5 label-m15">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="grade" id="level1" value="0"
                                                       checked="checked" />
                                                一级
                                            </label>
                                        </div>
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="grade" id="level2" value="1" />
                                                二级
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group" id="parentTr" style="display: none">
                                    <label class="col-sm-2 control-label">所属上级:</label>
                                    <div class="col-sm-2">
                                        <select name="parentId" class="form-control valid">
                                            <option value=""
                                                    selected="selected"
                                            >请选择
                                            </option>
                                        [#list productCategoryTree as perent]
                                            [#if perent.grade==0]
                                                <option value="${perent.id}">${perent.name}</option>
                                            [/#if]
                                        [/#list]
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-2">
                                    [@helperRole url="helper/member/product_category/list.jhtml" type="add"]
                                        [#if helperRole.retOper!="0"]
                                            <input type="submit" class="btn btn-block btn-success" value="${message("admin.common.submit")}"/>
                                        [#else ]
                                            &nbsp;
                                        [/#if]
                                    [/@helperRole]

                                    </div>
                                    <div class="col-sm-offset-0 col-sm-2">
                                        <input type="button" class="btn btn-block btn-default" value="${message("admin.common.back")}"
                                               onclick="location.href='list.jhtml'"/>
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
[#include "/store/member/include/bootstrap_js.ftl" /]
[#--<script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>--]
[#--<script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>--]
[#--<script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>--]
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>

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

//            $("input[name='grade']").change(function () {
//                $("input[name='grade']").each(function () {
//                    if ($(this).val() == 0) {
//                        if ($(this).attr("checked") == "checked") {
//                            $("#parentTr").hide();
//                        } else {
//                            $("#parentTr").show();
//                        }
//                    }
//                });
//            });

        $("#level1").click(function(){
            $("#parentTr").hide();
        });
        $("#level2").click(function(){
            $("#parentTr").show();
        });

    });
    function hideOrShow(){
        if ($(this).val() == 0) {
            if ($(this).attr("checked") == "checked") {
                $("#parentTr").hide();
            } else {
                $("#parentTr").show();
            }
        }
    };
</script>
</body>
</html>
