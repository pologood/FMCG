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
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelectProductCategory.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <style type="text/css">
        .specificationSelect {
            height: 100px;
            padding: 5px;
            overflow-y: scroll;
            border: 1px solid #cccccc;
        }

        .specificationSelect li {
            float: left;
            min-width: 150px;
            _width: 200px;
        }

        table.input th {
            width: 80px;
        }

        .dl {
            font-size: 14px;
            position: relative;
            padding-top: 10px;
            margin-left: 10px;
        }

        .dl .dt {
            position: absolute;
            left: 0;
            line-height: 38px;
        }

        .dl .dd {
            padding-left: 100px;
        }

        .nice-select {
            width: 450px;
            padding: 0 10px;
            height: 38px;
            border: 1px solid #999;
            position: relative;
            box-shadow: 0 0 5px #999;
            background: #fff url(${base}/resources/helper/images/a2.jpg) no-repeat right center;
            cursor: pointer;
            color: #555;
            font-size: 14px;
            font-family: "微软雅黑", "Microsoft Yahei";
        }

        .nice-select input {
            display: block;
            width: 100%;
            height: 38px;
            line-height: 38px \9;
            border: 0;
            outline: 0;
            background: none;
            cursor: pointer;
            font-size: 14px;
            font-family: "微软雅黑", "Microsoft Yahei";
        }

        .nice-select ul {
            width: 100%;
            display: none;
            position: absolute;
            left: -1px;
            top: 39px;
            overflow: hidden;
            background-color: #fff;
            max-height: 180px;
            overflow-y: auto;
            border: 1px solid #999;
            border-top: 0;
            box-shadow: 0 3px 5px #999;
            z-index: 9999;
        }

        .nice-select ul li {
            height: 30px;
            line-height: 30px;
            overflow: hidden;
            padding: 0 10px;
            cursor: pointer;
            position: relative;
        }

        .nice-select ul li.on {
            background-color: #e0e0e0;
        }

        .nice-select ul li span {
            display: block;
        }

        .nice-select ul li a {
            position: absolute;
            top: 6px;
            right: 10px;
            cursor: pointer;
            background-position: 0 -1px;
            width: 18px;
            height: 18px;
            text-indent: -9999px;
            overflow: hidden;
            _text-indent: 0;
            _font-size: 0;
            _line-height: 0;
            background: url(${base}/resources/helper/images/sicon_v5.png) no-repeat;
            display: inline-block;
        }

        .nice-select ul li a:hover {
            background-position: 0 -27px;
        }

    </style>
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
                    url: '${base}/helper/member/productCategoryMember/delete.jhtml',
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
                url: '${base}/helper/member/productCategoryMember/add.jhtml',
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
<body>


[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/upload2.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">新品发布</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">用时代新品，引领时尚先锋，触动客户心灵！</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="" hideFocus="" href="${base}/helper/member/product/isMarketableList.jhtml">我的商品</a>
                    </li>
                    <li><a class="on" hideFocus=""
                           href="${base}/helper/member/product/addProductCategory.jhtml">发布新品</a></li>
                </ul>

            </div>

            <form id="inputForm" action="add.jhtml" method="post" enctype="multipart/form-data">
                <div class="list" style="border:0px solid #C6C9CA;">
                    <table class="input tabContent">
                        <tr>
                            <td colspan="2">
                                <div class="dl">
                                    <div class="dt">选择常用类目：</div>
                                    <div class="dd">
                                        <div class="nice-select" name="nice-select">
                                            <input type="text" value="选择常用类目" readonly="">
                                            <ul style="display: none;">
                                                <li data-value="" treePath="" class="">
                                                    <span>选择常用类目</span>
                                                </li>
                                            [#list productCategoryMembers as productCategoryMember]
                                                <li data-value="${productCategoryMember.productCategory.id}"
                                                    treePath="${productCategoryMember.productCategory.treePath}"
                                                    class="">
                                                    <span>[#if productCategoryMember.productCategory.parent?has_content][#if productCategoryMember.productCategory.parent.parent?has_content]${productCategoryMember.productCategory.parent.parent.name}
                                                        /[/#if]${productCategoryMember.productCategory.parent.name}
                                                        /[/#if]${productCategoryMember.productCategory.name}</span>
                                                    <a value="${productCategoryMember.id}" name="btn_delete"
                                                       class="btn_delete" href="javascript:;"></a>
                                                </li>
                                            [/#list]
                                                <li data-value="" treePath="" class="hidden">
                                                    <span>123</span>
                                                    <a value="" name="btn_delete" class="btn_delete"
                                                       href="javascript:;"></a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                您当前选择的是：
                                <span id="show1" style="color:red;"></span>&nbsp;&nbsp;
                                <input id="addCategoryMember" type="button" class="button" value="加为常用类目"
                                       onclick="addCategory()" hidefocus/>
                            </td>
                        </tr>
                        <tr>
                            <th width="80px">
                                选择类目:
                            </th>
                            <td>
                                <div id="pc_select" style="height: 277px;">
                                    <input type="hidden" id="productCategoryId" name="productCategoryId"/>
                                </div>
                            </td>
                        </tr>
                    </table>
                    <table class="input">
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                            [@helperRole url="helper/member/product/addProductCategory.jhtml" type="add"]
                                [#if helperRole.retOper!="0"]
                                    <input disabled id="submitBtn" type="submit" class="button" value="发布宝贝" hidefocus/>
                                [/#if]
                            [/@helperRole]
                                请认真选择产品分类，这将影响到你的产品在用户分类搜索时的准确性。
                            </td>
                        </tr>
                    </table>
                </div>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
