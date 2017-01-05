<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>应用下载</title>
    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <!-- No Baidu Siteapp-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!-- Add to homescreen for Chrome on Android -->
    <meta name="mobile-web-app-capable" content="yes">
    <!-- Add to homescreen for Safari on iOS -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="${setting.siteName}"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <!-- Tile icon for Win8 (144x144 + tile color) -->
    <link rel="stylesheet" href="${base}/resources/wap/css/amazeui.css">
    <link rel="stylesheet" href="${base}/resources/wap/css/common.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <script type="text/javascript" src="${base}/resources/wap/js/jquery.min.js"></script>

    <script type="text/javascript">
        $().ready(function () {
            function is_weixin() {
                var ua = navigator.userAgent.toLowerCase();
                if (ua.match(/MicroMessenger/i) == "micromessenger") {
                    return true;
                } else {
                    return false;
                }
            }

            var isWeixin = is_weixin();
            if (isWeixin) {
                $(".sec3 a").attr("href", "javascript:;");
                $(".sec3 a").on("click", function () {
                    $(".weixin-tip").css("height", "100%");
                    $(".weixin-tip").show();
                    $(".weixin-tip").on('click','p', function(event) {
                        event.preventDefault();
                        event.stopPropagation();
                    });
                    $(".weixin-tip").on("click", function () {
                        $(".weixin-tip").hide();
                    });
                });
            }
        });

    </script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message/]

        });
    </script>
</head>
<body>
<div class="pageR">
    <div class="sec1">

    </div>
    <div class="sec2">

    </div>
    <div class="sec3">
        <a href="http://itunes.apple.com/cn/app/id1067224099?mt=8" class="downlink iphone">iPhone</a>
        <a href="http://oss.rzico.com/update/helper/helper-release-1.0.3.apk"
           class="downlink android">Android</a>
    </div>
</div>
<div class="weixin-tip">
    <p>
        <img src="${base}/resources/wap/2.0/images/invite-download-01.png"/>
    </p>
    <div>
        <a href="javascript:;" class="">知道了</a>    
    </div>
</div>
</body>
</html>
