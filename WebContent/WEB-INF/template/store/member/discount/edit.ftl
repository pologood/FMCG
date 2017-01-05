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

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/laddSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]

            var $inputForm = $("#inputForm");
            var $submit = $("#submit");
            $("#zhekoul").attr("readonly", "readonly");
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

            $("#zkj").change(function () {
                $("#zhekouj").removeAttr("readonly");
                $("#zhekoul").val("");
                $("#zhekouj").val("");
                $("#zhekoul").attr("readonly", "readonly");
            });

            $("#zkl").change(function () {
                $("#zhekoul").removeAttr("readonly");
                $("#zhekouj").val("");
                $("#zhekoul").val("");
                $("#zhekouj").attr("readonly", "readonly");
            });

            var productPrice = $("#price").val();
            $("#zhekouj").keyup(function () {
                var zkPrice = $("#zhekouj").val();
                if (parseFloat(zkPrice) > parseFloat(productPrice)) {
                    $("#zhekouj").val();
                    return;
                }
                var newzkl = zkPrice / productPrice;
                $("#zhekoul").val(newzkl.toFixed(2)*100+"%");
            });

            $("#zhekoul").keyup(function () {
                var zk = $("#zhekoul").val();
                if (zk < 0 || zk > 100) {
                    $("#zhekoul").val("");
                    return;
                }

                var newZkj = productPrice - productPrice * (zk / 100);

                $("#zhekouj").val(newZkj.toFixed(2));
            });

            $("#cleaPrevInput_begin").click(function () {
                $('#datetimepicker_begin').val("");
            });

            $("#cleaPrevInput_end").click(function () {
                $('#datetimepicker_end').val("");
            });
        });


    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#include "/store/member/include/header.ftl"]
[#include "/store/member/include/menu.ftl"]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                营销工具
                <small>制定限时折扣价格</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/discount/listproduct.jhtml">营销工具</a></li>
                <li><a href="${base}/store/member/discount/listproduct.jhtml">限时折扣</a></li>
                <li class="active">活动维护</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <form id="inputForm" action="add.jhtml" method="post" class="form-horizontal" role="form">
                        <input type="hidden" name="id" value="${product.id}"/>
                        <input type="hidden" name="productCategoryId" value="${product.productCategory.id}"/>
                        <input type="hidden" name="isMarketable" value="${product.isMarketable}"/>
                        <input type="hidden" name="sn" value="${product.sn}"/>
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs pull-right">
                                <li class="pull-left header"><i class="fa fa-gears"></i>限时折扣</li>
                            </ul>

                            <div class="tab-content">
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><span class="red">*</span>活动名称</label>
                                    <div class="col-sm-8">
                                        <input type="text" class="form-control" name="activityName">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><span class="red">*</span>商品名称</label>
                                    <div class="col-sm-8">
                                        <input type="text" name="name" class="form-control" value="${product.name}"
                                               readonly>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"><span class="red">*</span>销售价</label>
                                    <div class="col-sm-8">
                                        <input type="text" id="price" name="price" class="form-control"
                                               value="${product.price}" readonly>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-2 control-label">库存</label>
                                    <div class="col-sm-8">
                                        <input type="text" value="${product.stock}" class="form-control" readonly>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">按折扣价格/按折扣率</label>
                                    <div class="col-sm-9 pt5 label-m15">
                                        <form>
                                            <div class="col-sm-offset-2 col-sm-2">

                                                <label>
                                                    <input type="radio" name="radio" id="zkj" value="0" checked="true">
                                                    按折扣价格
                                                </label>

                                            </div>
                                            <div class="col-sm-offset-0 col-sm-2">
                                                <label>
                                                    <input type="radio" name="radio" id="zkl"
                                                           value="1">
                                                    按折扣率
                                                </label>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">折扣价格</label>
                                    <div class="col-sm-4">
                                        <input type="text" id="zhekouj" class="form-control" name="zhekouj">
                                    </div>
                                    <div class="col-sm-4 red"> 注：只能输入数字且不能大于销售价</div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">折扣率</label>
                                    <div class="col-sm-4">
                                        <input id="zhekoul" type="text" class="form-control" name="zhekoul">
                                    </div>
                                    <div class="col-sm-4 red"> 注：只能输入1-100之间的数字</div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">开始时间</label>
                                    <div class="col-sm-5">
                                        <input type="text" id="beginDate" name="beginDate" class="form-control"
                                               onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', maxDate: '#F{$dp.$D(\'endDate\')}'});"
                                               placeholder="开始时间"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">结束时间</label>
                                    <div class="col-sm-5">
                                        <input type="text" id="endDate" name="endDate" class="form-control"
                                               onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', minDate: '#F{$dp.$D(\'startDate\')}'});"
                                               placeholder="结束时间"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">每人限购</label>
                                    <div class="col-sm-8">
                                        <input type="text" name="limitation" class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label red">温馨提示</label>
                                    <div class="col-sm-8" style="margin-top: 5px;">
                                        <span class="red">限时折扣的商品，拍下30分钟内未付款将自动关闭订单</span>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-2">
                                        <button type="submit" class="btn btn-block btn-success">确定</button>

                                    </div>
                                    <div class="col-sm-offset-0 col-sm-2">
                                        <button type="submit" class="btn btn-block btn-default"
                                                onclick="javascript:history.back();">返回
                                        </button>
                                    </div>
                                </div>
                    </form>
                </div>

            </div>
    </div>
</div>

</section>
</div>
[#include "/store/member/include/footer.ftl"]
</div>
</body>

</html>