<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.consultation.edit")}</title>
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
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
            var $delete = $("#inputForm a.delete");
        [@flash_message /]
            // 删除
            $delete.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $.ajax({
                            url: "delete_reply.jhtml",
                            type: "POST",
                            data: {id: $this.attr("val")},
                            dataType: "json",
                            cache: false,
                            success: function (message) {
                                $.message(message);
                                if (message.type == "success") {
                                    $this.closest("tr").remove();
                                }
                            }
                        });
                    }
                });
                return false;
            });


            // 表单验证
            $inputForm.validate({
                rules: {
                    "name": {
                        required: true
                    },
                    "agioRate": {
                        required: true,
                        number: true,
                        min: 0,
                        max:100
                    },
                    "backRate": {
                        required: true,
                        integer: true,
                        min: 0,
                        max:100
                    },
                    "beginDate": {
                        required: true
                    },
                    "endDate": {
                        required: true
                    }
                },
                submitHandler: function (form) {
                    var $amount = $("input[name='amount']").val();
                    var $minimumPrice = $("input[name='minimumPrice']").val();

                    if (parseFloat($minimumPrice) < parseFloat($amount)) {
                        $.message("warn", "最低消费必须大于券的金额！");
                        return;
                    }
                    form.submit();
                }
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">

[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content-header">
            <h1>
                优惠买单
                <small>快去添加买单折扣对你的销量有很大帮助</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/bill/discount/add.jhtml">营销工具</a></li>
                <li><a href="${base}/store/member/bill/discount/add.jhtml">优惠买单</a></li>
                <li class="active">添加</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-info pb10">
                <div class="box-header with-border">
                    <i class="fa fa-edit"></i>
                    <h3 class="box-title">快去添加优惠买单对你的销量有很大帮助</h3>
                </div>
                <!-- /.box-header -->
                <!-- form start -->
                <form class="form-horizontal" role="form" id="inputForm" action="save.jhtml" method="post">
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label"><span class="red">*</span>名称</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control"  id="name" name="name" value="${(promotion.name)!}"/>
                        </div>
                    </div>
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label"><span class="red">*</span>折扣比例</label>
                        <div class="col-sm-3">
                            <input type="number" class="form-control" id="agioRate" name="agioRate" value="${(promotion.agioRate)!}"/>
                            <label style="color: red">0-100,设置买单折扣的比例  如：80%</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>返现比例</label>
                        <div class="col-sm-3">
                            <input type="number"  id="backRate" name="backRate" class="form-control" value="${(promotion.backRate)!}"/>
                            <label style="color: red">根据用户实际支付的金额按此比例返现</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>开始时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="beginDate" name="beginDate" class="form-control"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', maxDate: '#F{$dp.$D(\'beginDate\')}'});"
                                   />
                            </td>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>结束时间</label>
                        <div class="col-sm-3">
                            <input type="text" id="endDate" name="endDate" class="form-control"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', minDate: '#F{$dp.$D(\'endDate\')}'});"
                                  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">活动规则</label>
                        <div class="col-sm-5">
                            <textarea id="introduction" name="introduction" style="width: 60%;height: 80px;"
                                     maxlength="50">${(promotion.introduction?default(""))!}</textarea>
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
            <!-- /
          </section>
          <!-- /.content -->
    </div>
</div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
