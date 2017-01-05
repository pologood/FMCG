<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection" content="telephone=no" />
    <title>邀请注册成功</title>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/weui.min.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/main.css">
</head>

<body>
    <div class="wrap RDD">
        <!-- bannerR-->
        <div class="bannerR">
            <img src="${base}/resources/wap/2.0/images/invite-register-banner_bg01.jpg" alt="">
            <!-- bannerR-ctn-->
            <div class="bannerR-ctn">
                <p class="txt">恭喜您已成为本店会员！</p>
            </div>
        </div>
        <!-- operaR-->
        <div class="operaR IRS">
            <div class="welcome">
                <img src="${base}/resources/wap/2.0/images/welcome.png" alt="">
            </div>
            <div class="qrcodeR">
                <div class="imgR">
                    [#assign planning=""]
                    [#if ticket??&&ticket?has_content]
                        <img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}" alt="">
                        [#assign planning=tenant.name]
                    [#else ]
                        <img src="${base}/resources/wap/2.0/upload/tiaohuowang.png" alt="">
                        [#assign planning=setting.siteName]
                    [/#if]
                </div>
                <p>长按识别二维码，立即进${planning}逛逛</p>
            </div>
        </div>
    </div>
    <!-- bottom script-->
    <!-- js plugins here-->
</body>

</html>
