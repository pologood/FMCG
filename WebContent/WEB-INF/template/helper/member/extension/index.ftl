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
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
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
                $this.prev().css("display", "inline-block");
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
                $this.parent().children(":first").hide();
                $this.parent().children().eq(1).css("display", "inline-block");
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/extension.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">代理佣金</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">对该店所有商品设定统一的佣金比例。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">代理佣金</a></li>
                </ul>
            </div>
            <form id="inputForm" action="update.jhtml" method="post">
                <table class="input">
                    <tr>
                        <th>
                            推广分润：
                        </th>
                        <td colspan="3">
                            <div style="min-width: 150px;display: inline-block;">${brokerage*100} %</div>
                            <div class="hidden" style="min-width: 150px;">
                                <input type="text" class="text" id="brokerage" name="brokerage"
                                       value="${brokerage*100}" style="width: 100px;" maxlength="4"/> %
                            </div>
                        [@helperRole url="helper/member/extension/index.jhtml" type="update"]
                            [#if helperRole.retOper!="0"]
                                <input type="button" class="button" value="修改" id="btn1"/>
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
                            <div class="hidden" style="min-width: 150px;">
                                <input type="text" class="text" id="agency" name="agency"
                                       value="${agency*100}" style="width: 100px;" maxlength="4"/> %
                            </div>
                            <input type="hidden" id="isUnion" name="isUnion" value="${isUnion}"/>
                        [#if isUnion==true]

                            [@helperRole url="helper/member/extension/index.jhtml" type="update"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" class="button" value="修改" id="btn2"/>
                                [/#if]
                            [/@helperRole]
                            [@helperRole url="helper/member/extension/index.jhtml" type="close"]
                                [#if helperRole.retOper!="0"]
                                    <input type="button" class="button" value="关闭" id="btn3"/>
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
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
