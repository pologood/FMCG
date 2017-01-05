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
[#include "/store/member/include/bootstrap_css.ftl"]
    [#--<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>--]
    [#--<link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>--]
    [#--<link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>--]
    [#--<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">--]

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelectProductCategory.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>

    <script type="text/javascript">
        $(function () {
            var $productCategoryId = $("#productCategoryId");
        [@flash_message /]

            $productCategoryId.lSelectProductCategory({
                url: "${base}/common/productCategory.jhtml",
                fn: show
            });

            $("#pc_select").on("click", "#search_btn0", function () {
                $productCategoryId.val("");
                $productCategoryId.attr("treePath", "");
                var keyWord = $("#search_text0").val();
                $productCategoryId.lSelectProductCategory({
                    url: "${base}/common/productCategory.jhtml",
                    fn: show
                });
                show();
                $("#search_text0").val(keyWord);
                $.each($("#category_select0").find("option"), function (k, v) {
                    var flag = false;
                    if ($(v).text().indexOf(keyWord) >= 0) {
                        flag = true;
                    }
                    if (!flag) {
                        $(v).remove();
                    }
                });
            }).on("click", "#search_btn1", function () {
                var keyWord = $("#search_text1").val();
                $("#category_select0").trigger("click");
                $("#search_text1").val(keyWord);
                $.each($("#category_select1").find("option"), function (k, v) {
                    var flag = false;
                    if ($(v).text().indexOf(keyWord) >= 0) {
                        flag = true;
                    }
                    if (!flag) {
                        $(v).remove();
                    }
                });
            }).on("click", "#search_btn2", function () {
                var keyWord = $("#search_text2").val();
                $("#category_select1").trigger("click");
                $("#search_text2").val(keyWord);
                $.each($("#category_select2").find("option"), function (k, v) {
                    var flag = false;
                    if ($(v).text().indexOf(keyWord) >= 0) {
                        flag = true;
                    }
                    if (!flag) {
                        $(v).remove();
                    }
                });
            }).on("keydown", ".search_text", function (event) {
                var e = event || window.event || arguments.callee.caller.arguments[0];
                if (e && e.keyCode == 13) {
                    $(this).next().trigger("click");
                }
            });

            $('[name="nice-select"]').find('input').click(function (e) {
                if ($(this).next().css('display') == 'none') {
                    $(this).next().show();
                } else {
                    $(this).next().hide();
                }
                e.stopPropagation();
            });
            $('[name="nice-select"] li').hover(function (e) {
                $(this).toggleClass('on');
                e.stopPropagation();
            });
            $('[name="nice-select"] li span').click(function (e) {
                var val = $(this).text();
                var dataVal = $(this).parent().attr("data-value");
                var treePath = $(this).parent().attr("treePath");
                $(this).parents('[name="nice-select"]').find('input').val(val);
                $('[name="nice-select"] ul').hide();
                $productCategoryId.val(dataVal);
                $productCategoryId.attr("treePath", treePath);
                $productCategoryId.lSelectProductCategory({
                    url: "${base}/common/productCategory.jhtml",
                    fn: show
                });
                show();
                $productCategoryId.attr("treePath", null);
                e.stopPropagation();
            });
            $(document).click(function (e) {
                $('[name="nice-select"] ul').hide();
                e.stopPropagation();
            });

            $(".btn_delete").click(function () {
                var $this = $(this);
                $.ajax({
                    url: '${base}/store/member/productCategoryMember/delete.jhtml',
                    type: 'post',
                    data: {ids: $this.attr("value")},
                    dataType: 'json',
                    success: function (data) {
                        $.message(data);
                        if (data.type == "success") {
                            $this.parent().remove();
                            show();
                        }
                    }
                });

            });

        });

        function addCategory() {
            var id = $("#productCategoryId").val();
            if (id == "") {
                $.message("error", "请选择商品类目");
                return;
            }
            $.ajax({
                url: '${base}/store/member/productCategoryMember/add.jhtml',
                type: 'post',
                data: {id: id},
                success: function (data) {
                    $.message(data.message);
                    if (data.message.type == "success") {
                        $.each($("[name='nice-select'] ul li"), function (k, v) {
                            if ($(v).attr("data-value") == data.data.productCategoryId) {
                                $(v).remove();
                            }
                        });
                        $("[name='nice-select'] ul li:first").after($("[name='nice-select'] ul li:last").clone(true).show());
                        var $li = $("[name='nice-select'] ul li").eq(1);
                        $li.attr("data-value", data.data.productCategoryId);
                        $li.attr("treePath", data.data.productCategoryTreePath);
                        $li.find("span").text(data.data.fullName);
                        $li.find("a").attr("value", data.data.id);
                        $("#addCategoryMember").attr("disabled", true).val("已加为常用类目");
                    }
                }
            });
        }

        function show() {
            var str1 = "";
            var str2 = "";
            var str3 = "";
            var id = "";
            $(".category_select option:selected").each(function (k, v) {
                var $this = $(this);
                if (k == 0) {
                    str1 = $this.text();
                    if (str1 != "") {
                        id = $this.attr("value");
                    }
                }
                if (k == 1) {
                    str2 = $this.text();
                    if (str2 != "") {
                        id = $this.attr("value");
                    }
                }
                if (k == 2) {
                    str3 = $this.text();
                    if (str3 != "") {
                        id = $this.attr("value");
                    }
                }
            });
            var str = str1 == "" ? "" : (str1 + (str2 == "" ? "" : (">" + str2 + (str3 == "" ? "" : (">" + str3)))));
            $("#show1").text(str);
            var flag = true;
            $.each($('[name="nice-select"] li:not(:first,:last)'), function () {
                var $this = $(this);
                var v = $this.attr("data-value");
                if (v != "" && v == id) {
                    flag = false;
                }
            });
            if (!flag) {
                $("#addCategoryMember").attr("disabled", true).val("已加为常用类目");
            } else {
                $("#addCategoryMember").attr("disabled", false).val("加为常用类目");
            }
            if ($(".category_select option:selected").size() == $(".category_select").size()) {
                $("#submitBtn").attr("disabled", false);
            } else {
                $("#submitBtn").attr("disabled", true);
            }
        }
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                我的商品
                <small>用时代新品，引领时尚先锋，触动客户心灵！</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/product/addProductCategory.jhtml">我的商品</a></li>
                <li class="active">发布新品</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="box-header with-border">
                    <i class="fa fa-external-link-square"></i>
                    <h3 class="box-title">商品发布</h3>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" role="form" id="inputForm" action="add.jhtml" method="post"
                          enctype="multipart/form-data">
                        <div class="form-group" name="nice-select">
                            <label class="col-sm-3 control-label">选择常用类目：</label>
                            <div class="col-sm-4">
                                <select class="form-control">
                                    <option>选择常用类目</option>
                                [#list productCategoryMembers as productCategoryMember]
                                    <option data-value="${productCategoryMember.productCategory.id}"
                                            treePath="${productCategoryMember.productCategory.treePath}"> <span>[#if productCategoryMember.productCategory.parent?has_content][#if productCategoryMember.productCategory.parent.parent?has_content]${productCategoryMember.productCategory.parent.parent.name}
                                        /[/#if]${productCategoryMember.productCategory.parent.name}
                                        /[/#if]${productCategoryMember.productCategory.name}</span>
                                        <a value="${productCategoryMember.id}" name="btn_delete"
                                           class="btn_delete" href="javascript:;"></a></option>
                                [/#list]
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">您当前选择的是：<span id="show1" style="color:red;"></span>&nbsp;&nbsp;
                            </label>
                            <div class="col-sm-8">
                                <input id="addCategoryMember" type="button" class="btn btn-default" value="加为常用类目"
                                       onclick="addCategory()" hidefocus/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">选择类目:</label>
                            <div class="col-sm-3">
                                <div id="pc_select" style="height: 277px;">
                                    <input type="hidden" id="productCategoryId" name="productCategoryId"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-10 mt10">
                                <button type="submit" class="btn btn-success" id="submitBtn" disabled>发布宝贝</button>
                                <p class="note">请认真选择产品分类，这将影响到你的产品在用户分类搜索时的准确性。</p>
                            </div>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /
          </section>
          <!-- /.content -->
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>

</body>
</html>
