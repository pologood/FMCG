<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <title>优惠券</title>
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
    <script type="text/javascript">
        $().ready(function () {
            $("#img_logo").click(function () {
                location.href = "${base}/wap/coupon/success.jhtml?id=${coupon.id}";
            });
        });
    </script>
</head>
<body class="coupons_bg">
<div class="am-g fl">
    <div class="coupons_sub_bg">
        <div class="am-intro-bd am-container">
            <div class="am-intro-left am-u-sm-3" id="img_logo">
                <img src="${coupon.tenant.logo}" alt="" class="am-circle am-img-responsive"/>
            </div>
            <div class="am-intro-right am-u-sm-9">
                <div class="am-g">
                    <div class="Shop_name color_fff"
                         onclick="location.href='${base}/wap/delivery/${delivery.id}/index.jhtml'">${coupon.tenant.name}</div>
                </div>
                <div class="am-g">
                    <span class="coupons_subtitle">
                    ${coupon.tenant.address}
                    </span>
                </div>
            </div>
        </div>
        <div class="am-g">
            <div class="am-container">
                <div class="coupons_sign">
                    <span>
                    ${coupon.introduction}
                    </span>
                </div>
                <div class="am-g mt_10">
                    <div class="coupons_sum_instructions">
                    ${coupon.name}
                    </div>
                    <div class="coupons_sum_instructions">
                        <small>有效期至：${coupon.endDate}</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="coupons_bottom_bg"></div>
    <div class="am-container coupons_list_bg">
        <div class="am-row" onclick="location.href='${base}/wap/coupon/detail.jhtml?id=${coupon.id}'">
            <span class="am-fl" id="detail" id="coupon_detail">优惠券详情</span>
              <span class="am-fr am-color-bd">
                  <i class="am-fr icon iconfont" id="coupons_list_arrow"></i>
              </span>
        </div>
        <hr data-am-widget="divider" class="am-divider-default">
        <div class="am-row" onclick="location.href='${base}/wap/index.jhtml'">
            <span class="fl" id="coupon_public">公众号</span>
              <span class="fr am-color-bd">
                  <i class="fr icon iconfont" id="coupons_list_arrow"></i>
              </span>
        </div>
    </div>

</div>

</body>
</html>
