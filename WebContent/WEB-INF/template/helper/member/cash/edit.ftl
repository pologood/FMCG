<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
[@seo type = "index"]
    <title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.index.title")}[/#if][#if systemShowPowered][/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link rel="stylesheet" type="text/css" href="${base}/resources/helper/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/helper/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/helper/css/product.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/helper/css/account.css"/>

    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/amazeui.css">

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/ePayBank.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            var $inputForm = $("#inputForm");
            var $submit = $("#submit");
            var $sendCode = $("#sendCode");
            var $mobile = $("#mobile");

            // 表单验证
            $inputForm.validate({
                rules: {
                    account: {
                        required: true,
                        creditcard: true
                    },
                    securityCode: {required: true}
                },
                messages: {
                    account: {
                        required: '必填',
                        creditcard: '请填写正确的银行卡号码'
                    },
                    securityCode: {
                        required: '必填'
                    }
                },
                beforeSend: function () {
                    $submit.prop("disabled", true);
                },
                success: function (message) {
                    $submit.prop("disabled", false);
                }
            });

            $sendCode.click(function () {
                if ($mobile.val() != "" && $mobile.val().length == 11) {
                    settime(this);
                }

                $.ajax({
                    url: "${base}/helper/member/cash/getCheckCode.jhtml",
                    type: "post",
                    data: {mobile: $mobile.val()},
                    dataType: "json",
                    success: function (message) {
                        $.message(message);
                    }
                });
            });

        });

        var countdown = 60;
        function settime(obj) {
            if (countdown == 0) {
                obj.removeAttribute("disabled");
                obj.value = "获取验证码";
                countdown = 60;
                return;
            } else {
                obj.setAttribute("disabled", true);
                obj.value = "" + countdown + "s重新发送";
                countdown--;
            }
            setTimeout(function () {
                settime(obj)
            }, 1000);
        }

        function selectBank(bank) {
            var bankInfo = getBankInfo(bank);
            $("#bank").val(bank);
            $("#bankname").val(bankInfo.bankname);
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/app_cash.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">银行汇款</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">支持国内各大银行借记卡往来结算支付申请，快捷方便！</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">银行汇款</a></li>
                </ul>
            </div>
            <div class="input deposit">
                <form id="inputForm" name="inputForm" action="${base}/helper/member/cash/saveMemberBank.jhtml"
                      method="post">
                    <input type="hidden" name="type" value="recharge"/>
                    <input type="hidden" name="paymentPluginId" value="cmbcPlugin"/>
                    <table class="input">
                        <tr>
                            <th>
                                选择银行：
                            </th>
                            <td>
                                <select id="parentId" name="bankInfoId" style="width:190px">
                                [#list bankInfos as bankInfo]
                                    <option value="${bankInfo.id}" >${bankInfo.depositBank}</option>
                                [/#list]
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                卡号：
                            </th>
                            <td>
                                <input type="text" id="account" name="account" class="text">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                姓名:
                            </th>
                            <td>
                            ${member.name}
                                <input class="text" style="width:300px;" name="payer" maxlength="255"
                                       value="${member.name}" type="hidden"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                身份证号:
                            </th>
                            <td>
                            ${idcard.no}
                                <input class="text" style="width:300px;" name="no" maxlength="255" value="${idcard.no}"
                                       type="hidden"/>
                            </td>
                        </tr>
                        <tr>
                            <th>手机号:</th>
                            <td>
                                <input type="hidden" name="mobile" id="mobile" value="${member.mobile}"/>
                                <span name="mobile">${mosaic(member.mobile, 3, "~~~")}</span>
                                <input type="button" id="sendCode" value="获取验证码"/>
                            </td>
                        </tr>
                        <tr>
                            <th>校验码:</th>
                            <td><input type="text" name="securityCode" id="securityCode" class="text" maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td>
                                <input type="submit" class="button" value="${message("shop.member.submit")}"/>
                                <input type="button" class="button" value="${message("shop.member.back")}"
                                       onclick="location.href='index.jhtml'"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="intro" id="intro">相关说明：
                <ol>
                    <li>由于各银行入账时间和方式不同，请在预计到账时间前后先查询余额恢复和到账记录情况。</li>
                    <li>请仔细阅读汇款到账时间说明，如果由于预计到账时间超过汇款日期限、汇款信息填写错误或者超过银行的汇款限额等造成延误产生的损失，概不负责。</li>
                    <li>如出现汇款异常情况，失败汇款订单，会在最迟3-5个工作日内核实并将资金退回账户内。</li>
                    <li>银行汇款一旦成功提交，即表示已提交至银行处理，不可撤回和取消，敬请谅解。</li>
                    <li>平台发送的提交成功短信仅作提示使用，最终到账结果请以银行入账为准。</li>
                    <li>每天23点至1点前后银行系统结算时间，可能会影响到账时间，请注意申请汇款时间。</li>
                </ol>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>