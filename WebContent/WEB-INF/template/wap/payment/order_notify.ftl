[#if notifyMessage??]
${notifyMessage}
[#else]
<!DOCTYPE html>
<html lang="en">
<head>
    <title>订单</title>
    [#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>

</head>
<body ontouchstart>
    [#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<div class="sharelayer-tip">
    <p>
        <img src="${base}/resources/wap/2.0/images/payment-orderNotify-sharelayer-tip-02.png"/>
    </p>
    <div>
        <a href="javascript:;" class="">知道了</a>
    </div>
</div>
<script type="text/html" id="tpl_wraper">
    <div class="page">
        <div class="weui_msg">
            <div class="weui_icon_area"><i class="weui_icon_success weui_icon_msg"></i></div>
            <div class="weui_text_area">

            [#--[#if payment.status == "success"]--]
            [#--<h2 class="weui_msg_title">支付成功</h2>--]
            [#--<p class="weui_msg_desc">已支付${payment.amount}元</p>--]
            [#--[#elseif payment.status == "wait"]--]
            [#--<h2 class="weui_msg_title">支付取消</h2>--]
            [#--<p class="weui_msg_desc">用户遇到错误或者主动放弃</p>--]
            [#--[#elseif payment.status == "failure"]--]
            [#--<h2 class="weui_msg_title">支付失败</h2>--]
            [#--<p class="weui_msg_desc">用户遇到错误或者主动放弃</p>--]
            [#--[/#if]--]

                <h2 class="weui_msg_title">订单提交成功</h2>
            </div>
            <div class="weui_opr_area">
                <p class="weui_btn_area">

                [#--[#if payment.type == "payment"]--]
                [#--<a href="${base}/wap/member/order/list.jhtml" class="weui_btn weui_btn_primary">确定</a>--]
                [#--[#else]--]
                [#--<a href="${base}/wap/member/index.jhtml" class="weui_btn weui_btn_primary">返回</a>--]
                [#--[/#if]--]

                    <a href="${base}/wap/member/order/list.jhtml" class="weui_btn weui_btn_primary">确定</a>
                </p>
            </div>
        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $(".sharelayer-tip").show();
        $(".sharelayer-tip p").on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
        });
        $(".sharelayer-tip").on("click", function () {
            $(".sharelayer-tip").hide();
        });
    });
</script>
</body>
</html>
[/#if]