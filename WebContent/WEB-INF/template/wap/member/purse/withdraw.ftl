<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="balace_item">
        <div style="width: 100%;background-color: #e53535;text-align: center;color: #ffffff;height: 50vw;line-height: 50vw;">
            <span class="font25">￥{{withdrawBalance}}</span>
        </div>
        <div class="weui_cell other_blance">
            <div class="weui_cell_primary box">
                <h3 class="large-font">进行中未到账</h3>
                <div id="amount" class="large-font amount">￥</div>
                <span style="color: grey;">2个工作日到账，节假日顺延</span>
            </div>
            <!-- <div class="line"></div>-->
            <div class="weui_cell_primary box">
                <h3 class="large-font">冻结金额</h3>
                <div class="large-font amount">￥{{freezeBalance}}</div>
                <span style="color:grey;">卖家已支付，订单未完成</span>
            </div>
        </div>
    </script>
</head>
<body class="MBPS-WW">
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
                ajaxGet({
                    url: '${base}/app/member/cashier/sumer.jhtml',
                    success: function (data) {
                        $("#amount").text("￥"+data.data);
                    }
                });
            }
        });

    });
</script>

</body>
</html>