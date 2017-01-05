<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>满包邮</title>
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
    [#include "/store/member/include/bootstrap_css.ftl" /]
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">


</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                营销工具
                <small>快去设置满包邮，对您的销量有很大的帮助。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/promotion/add.jhtml?type=mail">营销工具</a></li>
                <li><a href="${base}/store/member/promotion/add.jhtml?type=mail">满包邮</a></li>
                <li class="active">添加</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="box-header with-border">
                    <i class="fa fa-truck"></i>
                    <h3 class="box-title">满包邮</h3>
                </div>
                <div class="box-body">
                    <form id="inputForm" action="add.jhtml" method="post">
                        <input type="hidden" id="type" name="type" value="mail"/>
                        <div class="form-group" style="height: 40px;">
                            <label class="col-sm-2 control-label" style="text-align: right;">消费满</label>
                            <div class="col-sm-5">
                                <input type="text" id="minimumPrice" name="minimumPrice" class="form-control" placeholder="请输入金额"
                                       value="[#if promotionMailModel??]${promotionMailModel.minimumPrice}[/#if]">
                            </div>
                        </div>
                        <div class="col-sm-offset-2 col-sm-10">
                            <h4>满包邮活动规则</h4>
                            <ul style="list-style-type: disc;padding-left: 15px;">
                                <li>买家消费满足你设置的金额，则订单包邮</li>
                                <li>如果你同时设置了一下活动，买家购买时可以同时享受优惠，包括：店铺优惠券，限时折扣。</li>
                            </ul>
                            <p class="mt20">包邮只对自营商品生效，分销商品只能由供应商设置。</p>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="submit" class="btn btn-success">确定</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>
[#include "/store/member/include/bootstrap_js.ftl" /]

<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
    $().ready(function () {
        var $inputForm = $("#inputForm");
        // 表单验证
        $inputForm.validate({
            rules: {
                "minimumPrice": {
                    required: true,
                    number: true,
                    min: 0
                }
            },
            messages:{
                "minimumPrice": {
                    required: "必填",
                    number: "必须为数字",
                    min: "最小为零"
                }
            }
        });


    });
</script>
</body>
</html>
