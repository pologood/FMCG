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
    <link href="${base}/resources/helper/css/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/laddSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.datetimepicker.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]

            var $inputForm = $("#inputForm");
            var $submit = $("#submit");
            // 表单验证  name,price,zhekouj,zhekoul,beginDate,endDate,limitation
            $inputForm.validate({
                rules: {
                    activityName: {required: true},
                    name: {required: true},
                    price: {required: true},
                    zhekouj: {required: true},
                    zhekoul: {required: true},
                    beginDate: {required: true},
                    endDate: {required: true},
                    limitation: {
                        required: true,
                        min: 0
                    }

                },
                messages: {
                    activityName: {required: '必填'},
                    name: {required: '必填'},
                    price: {required: '必填'},
                    zhekouj: {required: '必填'},
                    zhekoul: {required: '必填'},
                    beginDate: {required: '必填'},
                    endDate: {required: '必填'},
                    limitation: {required: '必填'}
                },
                beforeSend: function () {
                    $submit.prop("disabled", true);
                },
                success: function (message) {
                    $submit.prop("disabled", false);
                }
            });


            $('#datetimepicker_begin').datetimepicker();
            $('#datetimepicker_end').datetimepicker();

            $("#zkj").change(function () {
                $("#zhekouj").removeAttr("readonly");
                $("#zhekoul").attr("value", "");
                $("#zhekouj").attr("value", "");
                $("#zhekoul").attr("readonly", "readonly");
            });

            $("#zkl").change(function () {
                $("#zhekoul").removeAttr("readonly");
                $("#zhekouj").attr("value", "");
                $("#zhekoul").attr("value", "");
                $("#zhekouj").attr("readonly", "readonly");
            });

            var productPrice = $("#price").attr("value");
            $("#zhekouj").keyup(function () {
                var zkPrice = $("#zhekouj").attr("value");
                if (parseFloat(zkPrice) > parseFloat(productPrice)) {
                    $("#zhekouj").attr("value", '');
                    return;
                }
                var newzkl = zkPrice / productPrice;
                $("#zhekoul").attr("value", newzkl.toFixed(2));
            });

            $("#zhekoul").keyup(function () {
                var zk = $("#zhekoul").attr("value");
                if (zk < 0 || zk > 10) {
                    $("#zhekoul").attr("value", '');
                    return;
                }

                var newZkj = productPrice - productPrice * (zk / 10);

                $("#zhekouj").attr("value", newZkj.toFixed(2));
            });

            $("#cleaPrevInput_begin").click(function () {
                $('#datetimepicker_begin').attr("value", "");
            });

            $("#cleaPrevInput_end").click(function () {
                $('#datetimepicker_end').attr("value", "");
            });
        });


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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/upload.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">活动维护</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">制定限时折扣价格</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">活动维护</a></li>
                </ul>
            </div>

            <form id="inputForm" action="add.jhtml" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${product.id}"/>
                <input type="hidden" name="productCategoryId" value="${product.productCategory.id}"/>
                <input type="hidden" name="isMarketable" value="${product.isMarketable}"/>
                <input type="hidden" name="sn" value="${product.sn}"/>
                <div class="list" style="margin-top: 0px;">
                    <table class="input tabContent">
                        <tr>
                            <th>
                                <span class="requiredField">*</span>活动名称:
                            </th>
                            <td>
                                <input type="text" name="activityName" class="text changeInput"
                                       maxlength="20" />
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>商品名称:
                            </th>
                            <td>
                                <input type="text" name="name" class="text changeInput" value="${product.name}"
                                       maxlength="200" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>${message("Product.price")}:
                            </th>
                            <td>
                                <input type="text" id="price" name="price" class="text" value="${product.price}"
                                       maxlength="16" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <th>${message("Product.stock")}:</th>
                            <td>
                                <input type="text" id="stock" name="stock" class="text" value="${product.stock}"
                                       maxlength="9" title="${message("shop.product.stockTitle")}" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <th>按折扣价格/按折扣率</th>
                            <td>
                                <form>
                                    <input type="radio" name="radio" id="zkj" value="0" checked="true"/>按折扣价格
                                    <input type="radio" name="radio" id="zkl" value="1"/>按折扣率
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <th>折扣价格：</th>
                            <td>
                                <input id="zhekouj" type="text" name="zhekouj" class="text" maxlength="200"/>
                                <font id='bz' color="red">注：只能输入数字切不能大于销售价</font>
                            </td>
                        </tr>
                        <tr>
                            <th>折扣率：</th>
                            <td>
                                <input id="zhekoul" type="text" name="zhekoul" class="text" maxlength="200"
                                       readonly="true"/>
                                <font color="red">注：只能输入1-10之间的数字</font>
                            </td>
                        </tr>
                        <tr>
                            <th>开始时间</th>
                            <td>
                                <input class="text" type="text" name="beginDate" id="datetimepicker_begin"
                                       readonly="true" maxlength="200"/>
                                <input type="button" class="button" id="cleaPrevInput_begin" value="清空"/>
                            </td>
                        </tr>
                        <tr>
                            <th>结束时间</th>
                            <td>
                                <input class="text" type="text" name="endDate" id="datetimepicker_end" readonly="true"
                                       maxlength="200"/>
                                <input type="button" class="button" id="cleaPrevInput_end" value="清空"/>
                            </td>
                        </tr>
                        <tr>
                            <th>每人限购</th>
                            <td>
                                <input type="text" name="limitation" class="text" maxlength="200"/>
                            </td>
                        </tr>
                        <tr>
                            <th style="color:red;">温馨提示：</th>
                            <td style="color:red;">
                                限时折扣的商品，拍下30分钟内未付款将自动关闭订单。
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td>
                                <input type="submit" class="button" value="${message("shop.common.submit")}"/>
                                <input type="button" class="button" value="${message("shop.common.back")}"
                                       onclick="javascript:history.back();"/>
                            </td>
                        </tr>
                    </table>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
