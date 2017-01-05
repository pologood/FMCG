<!DOCTYPE html>
<html lang="en">
<head>
 <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="">
  <meta name="keywords" content="">
  <title>领取代金券</title>
  <!-- Set render engine for 360 browser -->
  <meta name="renderer" content="webkit">
  <!-- No Baidu Siteapp-->
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <!-- Add to homescreen for Chrome on Android -->
  <meta name="mobile-web-app-capable" content="yes">
  <!-- Add to homescreen for Safari on iOS -->
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="apple-mobile-web-app-title" content="${setting.siteName}"/>
  <meta content="yes" name="apple-mobile-web-app-capable" />
  <meta content="yes" name="apple-touch-fullscreen" />
  <meta content="telephone=no,email=no" name="format-detection" />
  <meta name="App-Config" content="fullscreen=yes,useHistoryState=yes,transition=yes" />
  <link rel="stylesheet" href="${base}/resources/wap/css/amazeui.css">
  <link rel="stylesheet" href="${base}/resources/wap/css/common.css">
  <script src="${base}/resources/wap/js/flexible_css.debug.js"></script>
  <script src="${base}/resources/wap/js/flexible.debug.js"></script>
</head>
<body>
<div class="am-g Shop_coupons">
    <div class="am-intro-bd am-container">
        <div class="am-intro-left am-u-sm-3 ">
            <img src="${base}/resources/wap/img/coupons_09.png" alt="" class="am-circle am-img-responsive" />
        </div>
        <div class="am-intro-right am-u-sm-9">
            <div class="am-g">

                <div class="Shop_name">菲小姐的店菲小姐的店菲小姐的店</div>
            </div>
            <div class="am-g">
                <!-- <div class="icon-likefill iconfont fl" id="level_red"></div>
                <div class="icon-likefill iconfont fl" id="level_red"></div>
                <div class="icon-likefill iconfont fl" id="level_red"></div>

                <div class="icon-likefill iconfont fl" id="level_xin"></div>
                <div class="icon-likefill iconfont fl" id="level_xin"></div> -->
                <i class="wap-icon-start-${(coupon.tenant.score * 2)?string("0")} wap-icon-left"></i>
            </div>
        </div>
    </div>
    <div class="am-g">
        <div class="am-container">
        <i class="icon-caret-up" id="abc"></i>
            <div class="shop_sign">
                <span>
                你点或不点，优惠券，就在这里。你点或不点，优惠券，就在这里。你点或不点，优惠券，就在这里。你点或不点，优惠券，就在这里。
                </span>
            </div>
        </div>
    </div>
    <div class="am-g Shop_coupons_bg">
        <div class="am-g">
            <h1 class="shop_sum">￥<span>100</span></h1>
            <div class="shop_sum_conditions mb_9">
                满1000元可用
            </div>
            <div class="shop_sum_instructions">
                剩余999张
            </div>
            <div class="shop_sum_instructions">
                有效期至：2015-12-18
            </div>
        </div>
        <div class="am-g">
          <div class="am-btn btn-shop_share am-u-sm-8 am-u-sm-centered mt_8">
              告诉小伙伴
          </div>
          <div class="am-btn btn-shop_share am-u-sm-8 am-u-sm-centered mt_3">
              进店逛逛
          </div>
        </div>
    </div>
</div>

</body>
</html>
