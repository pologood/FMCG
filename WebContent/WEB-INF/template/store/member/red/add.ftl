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
    <link href="${base}/resources/store/css/style.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>

</head>
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content-header">
            <h1>
                红包
                <small>快去添加红包，对您的销量有很大的帮助。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/red/list.jhtml?type=tenantBonus">营销工具</a></li>
                <li><a href="${base}/store/member/red/list.jhtml?type=tenantBonus">红包</a></li>
                <li class="active">添加</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-info pb10">
                <div class="box-header with-border">
                    <i class="fa fa-edit"></i>
                    <h3 class="box-title">快去添加红包，对您的销量有很大的帮助。</h3>
                </div>
                <!-- /.box-header -->
                <!-- form start -->
                <form class="form-horizontal" role="form" id="inputForm" action="add.jhtml" method="post">
                    <input type="hidden" id="type" name="type" value="${type}"/>
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label"><span class="red">*</span>红包的面额</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="amount" name="amount" placeholder="红包的金额"/>
                        </div>
                    </div>
                    <div class="form-group mt10">
                        <label class="col-sm-2 control-label"><span class="red">*</span>最低消费</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="minimumPrice" name="minimumPrice"
                                   placeholder="可以用红包的最低订单金额"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>红包数量</label>
                        <div class="col-sm-8">
                            <input type="text" id="count" name="count" class="form-control" placeholder="红包数量"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>领红包限制</label>
                        <div class="col-sm-8">
                            <input type="text" id="receiveTimes" name="receiveTimes" class="form-control"
                                   placeholder="每人限领张数"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>开始时间</label>
                        <div class="col-sm-8">
                            <input type="text" id="startDate" name="startDate" class="form-control"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', maxDate: '#F{$dp.$D(\'endDate\')}'});"
                                   placeholder="开始时间"/>
                            </td>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label"><span class="red">*</span>结束时间</label>
                        <div class="col-sm-8">
                            <input type="text" id="endDate" name="endDate" class="form-control"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', minDate: '#F{$dp.$D(\'startDate\')}'});"
                                   placeholder="结束时间"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">分享文案</label>
                        <div class="col-sm-8">
                            <textarea id="introduction" name="introduction" style="width: 60%;height: 80px;"
                                      placeholder="内容会出现在你分享的领券页面（50个字内）" maxlength="50"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-2">
                            <button type="submit" class="btn btn-block btn-success">确定</button>

                        </div>
                        <div class="col-sm-offset-0 col-sm-2">
                            <button class="btn btn-block btn-default"
                                    onclick="javascript:history.go(-1);">返回
                            </button>
                        </div>
                    </div>
                    <div class="kong"></div>
                </form>

            </div>
            <!-- /
          </section>
          <!-- /.content -->
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>
[#include "/store/member/include/bootstrap_js.ftl" /]
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
    $().ready(function () {
        var $inputForm = $("#inputForm");
        var $delete = $("#inputForm a.delete");
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
                "amount": {
                    required: true,
                    number: true,
                    min: 0
                },
                "minimumPrice": {
                    required: true,
                    number: true,
                    min: 0
                },
                "count": {
                    required: true,
                    integer: true,
                    min: 0
                },
                "receiveTimes": {
                    required: true,
                    integer: true,
                    min: 0
                },
                "startDate": {
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
                    $.message("warn", "最低消费必须大于红包的金额！");
                    return;
                }
                form.submit();
            }
        });
    });
</script>
</body>
</html>
