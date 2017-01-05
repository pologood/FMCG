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

    <div class="page" >
        <div style="background-color: white;padding: 10px 10px;line-height: 40px;">
            <table cellspacing="0" border="0" width="100%" style="border-bottom: 2px solid;">
                <tr>
                    <td>订单编号</td>
                    <td style="text-align: right;color: grey;" id="sn"></td>
                </tr>
                <tr>
                    <td>费用详情</td>
                    <td style="text-align: right;color: grey;" id="type">账户充值</td>
                </tr>
                <tr>
                    <td>实付费用</td>
                    <td style="text-align: right;color: grey;">${amount}</td>
                </tr>
            </table>
            <div style="display: inline-block;font-size: 18px;font-weight: bold;">
                <img width="20px" src="${base}/resources/wap/2.0/images/weixin.png"> 微信支付
            </div>
            <i class="iconfont" style="float:right;font-size: 14px;color: #ebebeb;">&#xe65f;</i>
        </div>
        <div style="padding:25px 16%;">
            <a href="javascript:;" class="weui_btn weui_btn_warn" onclick="submit()">确认支付</a>
        </div>
    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

<script>
    var message_data;
    $(function () {
        init();

        [#--ajaxPost({--]
            [#--url: '${base}/app/member/deposit/fill.jhtml',--]
            [#--data:{--]
                [#--amount:'${amount}'--]
            [#--},--]
            [#--success: function (data) {--]
                [#--$("#sn").text(data.data);--]
                [#--ajaxPost({--]
                    [#--url: '${base}/app/payment/view.jhtml',--]
                    [#--data:{--]
                        [#--sn:data.data--]
                    [#--},--]
                    [#--success: function (dataBlock) {--]
                        [#--if(dataBlock.data.type=="recharge"){--]
                            [#--$("#type").text("账户充值");--]
                        [#--}else if(dataBlock.data.type=="payment"){--]
                            [#--$("#type").text("订单支付");--]
                        [#--}else if(dataBlock.data.type=="cashier"){--]
                            [#--$("#type").text("线下代收");--]
                        [#--}else if(dataBlock.data.type=="function"){--]
                            [#--$("#type").text("功能缴费");--]
                        [#--}--]
                    [#--}--]
                [#--});--]
            [#--}--]
        [#--});--]

        ajaxPost({
            url:'${base}/wap/member/order/payment.jhtml',
            data:{
                amount:'${amount}',
                type:"recharge"
            },
            success:function(data){
                message_data=data;
                if(message_data.message.type=="success"){
                    $("#sn").text(message_data.sn);
                }
            }
        });

    });

    function submit(){
        [#--ajaxPost({--]
            [#--url:'${base}/wap/member/order/payment.jhtml',--]
            [#--data:{--]
                [#--amount:'${amount}',--]
                [#--type:"recharge"--]
            [#--},--]
            [#--success:function(message_data){--]
                if(message_data.message.type=="success"){
                    ajaxGet({
                        url: "${base}/wap/mutual/get_config.jhtml",
                        data: {
                            url: location.href.split('#')[0]
                        },
                        success: function (message_wx) {
                            var data = JSON.parse(message_wx.content);
                            wx.config({
                                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                                appId: data.appId, // 必填，公众号的唯一标识
                                timestamp: data.timestamp, // 必填，生成签名的时间戳
                                nonceStr: data.nonceStr, // 必填，生成签名的随机串
                                signature: data.signature,// 必填，签名，见附录1
                                jsApiList: ["chooseWXPay"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                            });
                            var jsonObject = JSON.parse(message_data.dataValue);
                            //调用微信JS api 支付
                            wx.ready(function () {
                                wx.chooseWXPay({
                                    timestamp: jsonObject.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                                    nonceStr: jsonObject.nonceStr, // 支付签名随机串，不长于 32 位
                                    package: jsonObject.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                                    signType: jsonObject.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                                    paySign: jsonObject.paySign, // 支付签名
                                    success: function (res) {
                                        // 支付成功后的回调函数
                                        if ("chooseWXPay:ok" == res.errMsg) {
                                            showToast({content:"支付成功"});
                                            setTimeout(function () {
                                                location.href = "${base}/wap/member/index.jhtml";
                                                return false;
                                            }, 600);
                                        } else if ("chooseWXPay:cancel" == res.errMsg) {
                                            showDialog1("提示","支付取消",function(){
                                                window.location.reload();
                                            });
                                        } else {
                                            showDialog1("提示","支付失败",function(){
                                                window.location.reload();
                                            });
                                        }
                                        return false;
                                    }
                                });
                            });
                            wx.error(function (res) {
                                // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                            });
                            wx.checkJsApi({
                                jsApiList: ['chooseWXPay'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                                success: function (res) {
                                    // 以键值对的形式返回，可用的api值true，不可用为false
                                    // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                                }
                            });
                        }
                    });
                }
//            }
//        });
    }
</script>

</body>
</html>