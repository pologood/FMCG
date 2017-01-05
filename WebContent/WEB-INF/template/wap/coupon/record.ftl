<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>平台券记录</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/main.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/fonts/iconfont.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/coupon.css">
</head>
<body>
<div class="record">
[#list couponNumbers as couponNumber]
    <div class="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__hd">领取</div>
            <div class="weui-cell__bd">${(couponNumber.createDate?string("YYYY-MM-dd"))!}<span
                    class="time">${(couponNumber.createDate?string("HH:mm:ss"))!}</span></div>
            <div class="weui-cell__ft">+￥${(couponNumber.coupon.amount)!}</div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__bd tenant-name"></div>
            <div class="weui-cell__ft"></div>
        </div>
    </div>
[/#list]
</div>
</body>
</html>