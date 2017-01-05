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
                    maximumQuantity:{required: true},
                    beginD: {required: true},
                    endD: {required: true}
                },
                messages: {
                    activityName: {required: '必填'},
                    name: {required: '必填'},
                    price: {required: '必填'},
                    beginD: {required: '必填'},
                    endD: {required: '必填'}
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
                        <dd class="app-intro" id="app_desc">制定买赠搭配方式</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">活动维护</a></li>
                </ul>
            </div>

            <form id="inputForm" action="add_buyfree.jhtml" method="post" enctype="multipart/form-data">
                <input type="hidden" name="productId" value="${product.id}"/>
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
                                       maxlength="200" readonly="readonly"/>
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
                            <th>&nbsp;</th>
                            <td>
                                买
                                <select id="parentId" name="minimumQuantity" style="width:50px">
                                    <option value="1" selected="selected">1</option>
                                [#list 2..10 as i]
                                    <option value="${i}">${i}</option>
                                [/#list]
                                </select>
                                赠
                                <select id="parentId" name="giftQuantity" style="width:50px">
                                    <option value="1" selected="selected">1</option>
                                [#list 2..10 as i]
                                    <option value="${i}">${i}</option>
                                [/#list]
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>赠品名称</th>
                            <td>
                                <select id="parentId" name="giftProductId" style="width:200px">
                                    <option value="${product.id}" selected="selected">${product.name}</option>
                                [#list products as product]
                                    <option value="${product.id}" >${product.name}</option>
                                [/#list]
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>开始时间</th>
                            <td>
                                <input class="text" type="text" name="beginD" id="datetimepicker_begin"
                                        maxlength="200"/>
                                <input type="button" class="button" id="cleaPrevInput_begin" value="清空"/>
                            </td>
                        </tr>
                        <tr>
                            <th>结束时间</th>
                            <td>
                                <input class="text" type="text" name="endD" id="datetimepicker_end"
                                       maxlength="200"/>
                                <input type="button" class="button" id="cleaPrevInput_end" value="清空"/>
                            </td>
                        </tr>
                        <tr>
                            <th>每人限购</th>
                            <td>
                                <input type="text" id="maximumQuantity" name="maximumQuantity" class="text"  maxlength="200"/>
                            </td>
                        </tr>
                        <tr>
                            <th style="color:red;">温馨提示：</th>
                            <td style="color:red;">
                                买赠搭配的商品，拍下30分钟内未付款将自动关闭订单。
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
