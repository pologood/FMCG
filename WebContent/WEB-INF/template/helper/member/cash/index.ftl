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
    <script type="text/javascript" src="${base}/resources/helper/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/ePayBank.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
            var $amount = $("#amount");
            var $submit = $("#submit");
            var $fee = $("#fee");
            var timeout;
            var curMethod = "fast";
            var _authStatus = "${(member.idcard.authStatus)!}";
        [@flash_message /]

            // 还款金额
            $amount.bind("input propertychange change", function (event) {
                if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                    if (checkValidAmount($amount.val())) {
                        $("#repayment_amount_tips").text("单笔金额不能超过50000元");
                        calculateFee();
                    } else {
                        $("#repayment_amount_tips").text("为确保汇款成功，请正确填写支付金额");
                    }
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    amount: {
                        required: true,
                        positive: true,
                        max: 50000.00,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    }

                },
                messages: {
                    amount: {
                        required: '必填',
                        positive: '只允许输入正数',
                        max: '数额超出',
                        min: '金额不能小于0',
                        decimal: '金额格式错误'
                    }
                },
                beforeSend: function () {
                    $submit.prop("disabled", true);
                },
                success: function (message) {
                    $submit.prop("disabled", false);
                },
                submitHandler: function (form) {
                    //if(){
                        var bankName=$("#memberBankId").val();

                    if(bankName=='#'){
                        $.message("warn","请选择银行卡号或者添加银行卡");
                        return false;
                    }
                    form.submit();

                    //}

                }
            });

            // 计算支付手续费
            function calculateFee() {
                clearTimeout(timeout);
                timeout = setTimeout(function () {
                    if ($amount.val() > 0) {
                        $.ajax({
                            url: "calculate_fee.jhtml",
                            type: "POST",
                            data: {method: curMethod, amount: $amount.val()},
                            dataType: "json",
                            cache: false,
                            success: function (data) {
                                if (data.message.type == "success") {
                                    if (data.fee > 0) {
                                        $fee.text(currency(data.fee, true));
                                    } else {
                                        $fee.text("￥0.00");
                                    }
                                } else {
                                    $.message(data.message);
                                }
                            }
                        });
                    } else {
                        $fee.text("￥0.00");
                    }
                }, 500);
            }

            $("#memberBankId").change(function (obj) {
                var val = $(this).val();
                var bankName=$(this).find("option:selected").attr("bankName");

                $("#bankname").val(bankName);
                $("#account").val(val);

                if(val.indexOf('jhtml')!=-1){
                    if(_authStatus=='success'){
                        location.href=val;
                    }else {
                        location.href='${base}/helper/member/safe/idcard.jhtml';
                    }
                }
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/app_cash.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">银行汇款</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">支持国内各大银行借记卡往来结算支付申请，快捷方便！</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">银行汇款</a></li>
                    <li><a class="" hideFocus="" href="list.jhtml">汇款记录</a></li>
                </ul>
            </div>
            <div class="input deposit">
                <form id="inputForm" name="inputForm" action="submit.jhtml" method="post">
                    <table class="input">
                        <tr>
                            <th>选择银行卡：</th>
                            <td>
                                <select id="memberBankId" name="memberBankId" style="width:190px">
                                    <option value="#" selected="selected">请选择银行</option>
                                    <option value="${base}/helper/member/cash/edit.jhtml">--添加银行卡--</option>
                                [#list options.keySet() as key]
                                    <option value="${key}" >${options.get(key)}</option>
                                [/#list]
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>提现金额：</th>
                            <td>
                                <input type="text" id="amount" name="amount" class="text" maxlength="16"
                                       onpaste="return false;"/>
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td>
                                <p><span class="mod-tips" id="repayment_amount_tips"
                                         style="display:block;margin-left:0px"/>单笔金额不能超过50000元</span></p>
                            </td>
                        </tr>
                        <tr>
                            <th>手续费:</th>
                            <td>
                                <span id="fee" class="text-tips" style="font-color:00">￥0.00</span>
                            </td>
                        </tr>
                        [#--<tr>--]
                            [#--<th>支付密码：</th>--]
                            [#--<td>--]
                                [#--<input type="password" id="enPassword" name="enPassword" class="text" maxlength="6"--]
                                       [#--onpaste="return false;"/>--]
                            [#--</td>--]
                        [#--</tr>--]
                        <tr>
                            <th>&nbsp;</th>
                            <td>
                                <button id="btn_repay" type="submit" id="submit">立刻提现</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="list" style="border-top:0">
                <table class="list">
                    <tr>
                        <th>
                            开户行
                        </th>
                        <th>
                            卡号
                        </th>
                        <th>
                            操作
                        </th>
                    </tr>
                [#list memberBanks as memberBank ]
                    <tr class="last">
                        <td>
                            ${memberBank.depositBank}
                        </td>
                        <td>
                        ${memberBank.cardNo}
                        </td>
                        <td>
                        <a href="${base}/helper/member/cash/delete.jhtml?id=${memberBank.id}">删除</a>
                        </td>
                    </tr>
                [/#list]
                </table>
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