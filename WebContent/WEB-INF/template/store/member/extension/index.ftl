<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>代理佣金</title>
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
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            $("#brokerag").hide();
            $("#agenc").hide();
            var $inputForm = $("#inputForm");
        [@flash_message /]
            // 表单验证
            $inputForm.validate({
                rules: {
                    "brokerage": {
                        required: true,
                        digits: true,
                        max:100
                    },
                    "agency": {
                        required: true,
                        digits: true,
                        max:100
                    }
                },
                submitHandler: function (form) {
                    $.ajax({
                        url: 'update.jhtml',
                        type: 'post',
                        data: {
                            brokerage: $("#brokerage").val() / 100,
                            agency: $("#agency").val() / 100,
                            isUnion: $("#isUnion").val()
                        },
                        dataType: 'json',
                        success: function (data) {
                            $.message(data);
                            if (data.type == "success") {
                                window.setTimeout(function () {
                                    window.location.reload();
                                }, 600);
                            }
                        }
                    });
                }
            });

            //修改推广分润
            $("#btn1").click(function () {
                var $this = $(this);
                $this.val("确认");
                $("#brokerag").show();
                $("#brokerag").css("display", "inline-block");
                $this.prev().prev().hide();
                $this.unbind("click").click(function () {
                    $inputForm.submit();
                });
            });

            //修改联盟拥金
            $("#btn2").click(function () {
                var $this = $(this);
                $this.next().hide();
                $this.val("确认");
                $("#agenc").show();
                $("#agenc").css("display", "inline-block");
                $this.parent().children(":first").hide();
                $this.unbind("click").click(function () {
                    $inputForm.submit();
                });
            });

            //关闭联盟拥金
            $("#btn3").click(function () {
                $.dialog({
                    type: "warn",
                    content: "确认是否要关闭联盟佣金？",
                    onOk: function () {
                        $("#isUnion").val(false);
                        $inputForm.submit();
                    }
                });
            });

            //开启联盟拥金
            $("#btn4").click(function () {
                var $this = $(this);
                $this.val("确认");
                $this.parent().children().eq(0).hide();
                $this.parent().children().eq(1).css("display", "inline-block");
                $("#isUnion").val(true);
                $this.unbind("click").click(function () {
                    $inputForm.submit();
                });
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
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content-header">
            <h1>
               	 营销工具
                <small>代理佣金</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/extension/index.jhtml">营销工具</a></li>
                <li class="active">代理佣金</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="box-header with-border">
					  <i class="fa fa-cny"></i>
					  <h3 class="box-title">代理佣金</h3>
				</div>
				<div class="box-body">
                <form id="inputForm" action="update.jhtml" method="post">
                    <table class="table table-bordered dataTable">
                        <tr>
                            <th>
                                推广分润：
                            </th>
                            <td colspan="3">
                                <div style="min-width: 150px;display: inline-block;">${brokerage*100} %</div>
                                <div style="min-width: 150px;" id="brokerag">
                                    <input type="text" class="text" id="brokerage" name="brokerage"
                                           value="${brokerage*100}" style="width: 100px;" maxlength="4"/> %
                                </div>
                            [@helperRole url="helper/member/extension/index.jhtml" type="update"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" class="btn btn-info" value="修改" id="btn1"/>
                                [/#if]
                            [/@helperRole]
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" style="padding:5px 0 30px 20px;">
                                <div>设置推广分润，让推广者帮你获得更多订单！</div>
                                <br/>
                                <font size="4">推广分润规则</font>
                                <br/>
                                <div style="width: 80%;">
                                    设置推广分润，让顾客将购买的商品分享给他人，促成交易后，顾客作为分享者会得到推广分润。
                                    消费者变销售者，人人都给你卖货，扩大店铺经营半径，降低营销成本，实现业绩增长。
                                    通过推广分润，所有门店的导购和所有的线上线下消费者，均转化为商品销售的业务员，
                                    深度挖掘潜在商机。
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                联盟佣金：
                            </th>
                            <td colspan="3">
                                <div style="min-width: 150px;display: inline-block;">
                                [#if isUnion==true]${agency*100} %[#else]未开启[/#if]
                                </div>
                                <div id="agenc" style="min-width: 150px;">
                                    <input type="text" class="text" id="agency" name="agency"
                                           value="${agency*100}" style="width: 100px;" maxlength="4"/> %
                                </div>
                                <input type="hidden" id="isUnion" name="isUnion" value="${isUnion}"/>
                            [#if isUnion==true]

                                [@helperRole url="helper/member/extension/index.jhtml" type="update"]
                                    [#if helperRole.retOper!="0"]
                                        <input type="button" class="btn btn-info" value="修改" id="btn2"/>
                                    [/#if]
                                [/@helperRole]
                                [@helperRole url="helper/member/extension/index.jhtml" type="close"]
                                    [#if helperRole.retOper!="0"]
                                        <input type="button" class="btn btn-default" value="关闭" id="btn3"/>
                                    [/#if]
                                [/@helperRole]
                            [#else]
                                [@helperRole url="helper/member/extension/index.jhtml" type="open"]
                                    [#if helperRole.retOper!="0"]
                                        <input type="button" class="button" value="开启" id="btn4"/>
                                    [/#if]
                                [/@helperRole]
                            [/#if]
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" style="padding:5px 0 30px 20px;">
                                <div>加入${setting.siteName}联盟，吸引其他商家销售我的商品！</div>
                                <br/>
                                <font size="4">联盟佣金规则</font>
                                <br/>
                                <div style="width: 80%;">
                                    开通联盟佣金，让其他商家帮你推广商品，获得更多订单。您只需为因其他商家而
                                    成功售出的商品付出佣金，交易不成功无需支付任何费用。设置的佣金比例越高，
                                    其他商家推广的动力就越大哦！
                                </div>
                            </td>
                        </tr>
                    </table>
                </form>
              </div>
        </section>
    </div>
[#include "/store/member/include/footer.ftl" /]

</div>

</body>
</html>
