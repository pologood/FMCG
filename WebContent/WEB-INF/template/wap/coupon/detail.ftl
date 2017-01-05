<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <title>优惠券-详情</title>
    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <!-- No Baidu Siteapp-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!-- Add to homescreen for Chrome on Android -->
    <meta name="mobile-web-app-capable" content="yes">
    <!-- Add to homescreen for Safari on iOS -->
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="${setting.siteName}"/>
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <meta content="yes" name="apple-touch-fullscreen"/>
    <meta content="telephone=no,email=no" name="format-detection"/>
    <meta name="App-Config" content="fullscreen=yes,useHistoryState=yes,transition=yes"/>
    <link rel="stylesheet" href="${base}/resources/wap/css/amazeui.css">
    <link rel="stylesheet" href="${base}/resources/wap/css/common.css">
    <script src="${base}/resources/wap/js/flexible_css.debug.js"></script>
    <script src="${base}/resources/wap/js/flexible.debug.js"></script>
    <script src="${base}/resources/wap/js/jquery-1.9.1.min.js"></script>
</head>
<body class="coupons_bg">
<div class="am-g">
    <div class="am-container">
        <div class="coupons_details_sub">
            优惠详情
        </div>
    </div>
    <div class="am-g">
        <div class="am-container coupons_details_bd">
            <div class="am-u-sm-3 am-text-right">
                使用限制:
            </div>
            <div class="am-u-sm-9 am-text-left">
            ${(coupon.introduction)!"无"}
            </div>
            <div class="am-u-sm-3 am-text-right">
                使用范围:
            </div>
            <div class="am-u-sm-9 am-text-left">
                全店通用
            </div>
            <div class="am-u-sm-3 am-text-right">
                有效期:
            </div>
            <div class="am-u-sm-9 am-text-left">
            ${coupon.startDate}
            </div>
            <div class="am-u-sm-3 am-text-right">
                至:
            </div>
            <div class="am-u-sm-9 am-text-left">
            ${coupon.endDate}
            </div>
            <div class="am-u-sm-3 am-text-right">
                退货规则:
            </div>
            <div class="am-u-sm-9 am-text-left">
                发生退货时，最多可退买家实际付款金额，优惠券不退。
            </div>
        </div>

    </div>
    <div class="am-g">
        <div class="am-container">
            <div class="coupons_details_sub">
                使用须知
            </div>
        </div>
        <div class="am-g">
            <div class="am-container coupons_details_bd">
                <div class="am-container">
                    请在提交订单时选中本券
                </div>
            </div>
        </div>
    </div>
</div>


</body>
</html>
