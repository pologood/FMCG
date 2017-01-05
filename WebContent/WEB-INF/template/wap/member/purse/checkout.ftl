<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page">
        <div class="weui_cells weui_cells_form marginTop0">
            <div class="weui_cell">
                <div class="weui_cell_hd"><label>收款金额（￥）</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input id="amount" oninput="number(this)" onpropertychange="number(this)" class="weui_input" type="tel" placeholder="请输入收款金额">
                </div>
            </div>
        </div>
        <div style="font-size: 16px;background-color: #ffffff;" class="marginTop10">
            <div style="width: 50%;background-color: black;color: #ffffff;text-align: center;line-height: 34px;padding: 3px 3px;">
                <img width="20px" src="${base}/resources/wap/2.0/images/weixin_black.png">微信钱包
                <img style="vertical-align: top;float: right;" width="10px" src="${base}/resources/wap/2.0/images/sure.png">
            </div>
        </div>
        <p style="padding: 10px 10px;font-size: 16px;">每日限额：200000.00元，每笔限额：200000.00元</p>
        <div style="padding: 10px 10px;background-color: #ffffff;font-size: 16px;">
            <div style="padding-bottom: 5px;"><span style="font-size: 18px;">交易安全描述：</span></div>
            微店将配合银行卡共同打击无真实交易背景的虚假交易，银行卡转账套现或洗钱等被禁止的交易行为，请如实交易，否则款项将不能提现！
        </div>
        <div style="padding: 10px 10px;">
            <a href="javascript:;" class="weui_btn weui_btn_warn" onclick="submit()">下一步</a>
        </div>
    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

    });
    function submit(){
        location.href="${base}/wap/member/purse/scanner.jhtml?amount="+$("#amount").val();
    }
    function number(n) {
        var val = $(n).val();
        if (val.trim() == "") {
            $(n).val("");
            return false;
        } else if (isNaN(val)) {
            $(n).val("");
            return false;
        }
        $(n).val(val.trim());
        return true;
    }
</script>

</body>
</html>