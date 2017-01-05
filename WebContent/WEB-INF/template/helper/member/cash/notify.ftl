<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>银行汇款[#if systemShowPowered][/#if]</title>
    <link href="${base}/resources/helper/css/common.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${base}/resources/helper/css/product.css">
    <link href="${base}/resources/helper/css/credit.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/ePayBank.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">

        //禁止按键F5
        document.onkeydown = function (e) {
            e = window.event || e;
            var keycode = e.keyCode || e.which;
            if (keycode = 116) {
                if (window.event) {// ie
                    try {
                        e.keyCode = 0;
                    } catch (e) {
                    }
                    e.returnValue = false;
                } else {// ff
                    e.preventDefault();
                }
            }
        };

        function countDown(secs, surl) {
            var jumpTo = document.getElementById('jumpTo');
            jumpTo.innerHTML = secs;
            if (--secs > 0) {
                setTimeout("countDown(" + secs + ",'" + surl + "')", 1000);
            }
            else {
                location.href = surl;
                -ma
            }
        }


        $().ready(function () {
            var bankInfo = getBankInfo(${credit.bank});
            $("#bankname").html(bankInfo.bankname);
        });
    </script>
</head>
<body>
<div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
    <div class="js-app-header title-wrap" id="app_0000000844">
        <img class="js-app_logo app-img" src="${base}/resources/helper/images/app_cash.png"/>
        <dl class="app-info">
            <dt class="app-title" id="app_name">银行汇款</dt>
            <dd class="app-status" id="app_add_status">
            </dd>
            <dd class="app-intro" id="app_desc">支持国内各大银行往来结算支付申请，快捷方便！</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <li><a class="on" hideFocus="" href="${base}/helper/member/cash/index.jhtml">银行汇款</a></li>
    </ul>
</div>
<div class="title">
[#if status.type != "error"]
    [#if credit.status == "wait"]
        您的汇款申请已经提交成功,请注意资金到账情况。
    [#elseif credit.status == "wait_success"]
        您的汇款申请已经提交银行处理,请注意资金到账情况。
    [#elseif credit.status == "success"]
        您的汇款申请已经支付成功,请注意资金到账情况。
    [#elseif credit.status == "wait_failure"]
        您的汇款申请支付失败，3个工作日内将退回账户
    [#elseif credit.status == "failure"]
        您的汇款申请支付失败，请重新填写
    [/#if]
[#else]
    您的汇款申请失败了，原因：${status.content}
[/#if]
</div>
<a href="${base}/helper/member/cash/index.jhtml"><span id="jumpTo">10</span>秒后系统会自动跳转</a>
<script type="text/javascript">
    countDown(10, '${base}/helper/member/cash/index.jhtml');
</script>
<table class="info">
[#if status.type != "error"]
    <tr>
        <th>
            汇款日期:
        </th>
        <td>
        ${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}
        </td>
    </tr>
[/#if]
    <tr>
        <th>
            汇款金额:
        </th>
        <td>
        ${currency(credit.amount, true)}
        </td>
    </tr>
    <tr>
        <th>
            手续费:
        </th>
        <td>
        ${currency(credit.fee, true)}
        </td>
    </tr>
    <tr>
        <th>
            返利金额:
        </th>
        <td>
        ${currency(credit.profitAmount, true)}
        </td>
    </tr>
    <tr>
        <th>
            汇款账户:
        </th>
        <td>
        ${credit.account}
        </td>
    </tr>
    <tr>
        <th>
            账户名称:
        </th>
        <td>
        ${credit.payer}
        </td>
    </tr>
    <tr>
        <th>
            银行名称:
        </th>
        <td id="bankname">
        ${credit.bankName}
        </td>
    </tr>
</table>
<div class="bottom">
    <a href="${base}/helper/member/cash/index.jhtml">继续汇款</a>
</div>
</body>
</html>
