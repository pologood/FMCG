<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="balace_item">
        <div class="weui_cells weui_cells_access marginTop0">
            <a class="weui_cell" href="${base}/wap/member/deposit/bill_list.jhtml" style="background-color: #e53535;color: #ffffff;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>当前余额</p>
                    <p class="balance">
                        <span class="font25">￥{{balance}}</span>
                    </p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
            <a class="weui_cell tx" href="${base}/wap/member/purse/withdraw.jhtml;" style="background-color: #fe5b4a;color: #ffffff;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>可提金额</p>
                    <p class="withdrawbalance">
                        <span class="font25">￥{{withdrawBalance}}</span>
                    </p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
        </div>
        <div class="weui_cells marginTop10">
            <div class="weui_cell moneyopera" style="padding: 0;">
                <div class="charge weui_cell_primary" onclick="location.href='${base}/wap/member/purse/charge.jhtml'">充值</div>
                <div class="checkout weui_cell_primary" onclick="location.href='${base}/wap/member/purse/checkout.jhtml'">￥收款</div>
                <div class="withdrawcash weui_cell_primary" onclick="location.href='${base}/wap/member/purse/cash.jhtml'">提现</div>
            </div>
        </div>
        <div class="weui_cells weui_cells_access marginTop10">
            <a class="weui_cell" href="${base}/wap/member/purse/bank.jhtml">
                <div class="weui_cell_bd weui_cell_primary">
                    <p><img width="25px" src="${base}/resources/wap/2.0/images/bank_card.png"> 银行卡</p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
        </div>
        <div class="weui_cells weui_cells_access marginTop10">
            <a class="weui_cell" href="${base}/wap/member/deposit/bill_list.jhtml">
                <div class="weui_cell_bd weui_cell_primary">
                    <p><img width="25px" src="${base}/resources/wap/2.0/images/bill.png"> 账单</p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
        </div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page">

    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        compiler = Handlebars.compile($("#balace_item").html());
        ajaxGet({
            url: '${base}/app/member/balance.jhtml',
            success: function (data) {
                $(".page").append(compiler(data.data));
            }
        });
    });
</script>

</body>
</html>