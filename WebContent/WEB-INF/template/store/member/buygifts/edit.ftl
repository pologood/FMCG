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
    <link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/laddSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.datetimepicker.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
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
                    maximumQuantity: {required: true},
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
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                营销工具
                <small>活动维护</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/buygifts/listproduct.jhtml.jhtml">营销工具</a></li>
                <li><a href="${base}/store/member/buygifts/listproduct.jhtml.jhtml">买赠搭配</a></li>
                <li class="active">活动维护</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-info">
                <div class="box-header with-border">
                    <i class="fa fa-gears"></i>
                    <h3 class="box-title">制定买赠搭配方式</h3>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" role="form" id="inputForm" action="add_buyfree.jhtml" method="post"
                          enctype="multipart/form-data">
                        <input type="hidden" name="productId" value="${product.id}"/>
                        <input type="hidden" name="productCategoryId" value="${product.productCategory.id}"/>
                        <input type="hidden" name="isMarketable" value="${product.isMarketable}"/>
                        <input type="hidden" name="sn" value="${product.sn}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><span class="red">*</span>活动名称</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="activityName" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><span class="red">*</span>商品名称</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="name" value="${product.name}" readonly="readonly">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><span class="red">*</span>销售价</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="price" value="${product.price}" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">库存</label>
                            <div class="col-sm-3">
                                <input type="text" name="stock" id="stock" class="form-control" value="${product.stock}" readonly>
                            </div>
                            <div class="col-sm-2">
                                <span class="text-fl">买</span><select class="form-control" name="minimumQuantity" style="width:200px">
                                <option value="1" selected="selected">1</option>
                            [#list 2..10 as i]
                                <option value="${i}">${i}</option>
                            [/#list]
                            </select>
                            </div>
                            <div class="col-sm-2">
                                <span class="text-fl">赠</span><select class="form-control" name="giftQuantity" style="width:200px">
                                <option value="1" selected="selected">1</option>
                            [#list 2..10 as i]
                                <option value="${i}">${i}</option>
                            [/#list]
                            </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">赠品名称</label>
                            <div class="col-sm-7">
                                <select id="parentId" name="giftProductId" class="form-control">
                                    <option value="${product.id}" selected="selected">${product.name}</option>
                                [#list products as product]
                                    <option value="${product.id}" >${product.name}</option>
                                [/#list]
                                </select>
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">开始时间</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="beginD" id="datetimepicker_begin">
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-default" id="cleaPrevInput_begin">清空</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">结束时间</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="endD" id="datetimepicker_end">
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-default" id="cleaPrevInput_end">清空</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">每人限购</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="maximumQuantity" name="maximumQuantity">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label red">温馨提示</label>
                            <div class="col-sm-8 red" style="padding-top:5px">
                                买赠搭配的商品，拍下30分钟内未付款将自动关闭订单。
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-2">
                                <button type="submit" class="btn btn-block btn-success">确定</button>
                            </div>
                            <div class="col-sm-offset-0 col-sm-2">
                                <button onclick="javascript:history.back();" class="btn btn-block btn-default">返回</button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
