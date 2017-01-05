<!DOCTYPE html>
<html lang="en">
<head>
 <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="description" content="">
  <meta name="keywords" content="">
  <title>代金券-领取</title>
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
  <script src="${base}/resources/wap/js/jquery-1.9.1.min.js"></script>

  <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/handlebars.min.js"></script>
<script src="${base}/resources/wap/js/fastclick.js"></script>
<script src="${base}/resources/wap/js/iscrollEvents.js"></script>
<script src="${base}/resources/wap/js/masonry.pkgd.min.js"></script>
<script src="${base}/resources/wap/js/common.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

<style>
      #wrapper{[#if browse_version=="MicroMessenger"]bottom:45px;[/#if]}
</style>
<style type="text/css">
  .weixin-tip{display: none; position: fixed; left:0; top:0; bottom:0; background: rgba(0,0,10,0.8); filter:alpha(opacity=80);  height: 100%; width: 100%; z-index: 100;}
  .weixin-tip p{text-align: center;padding:0 5%;}
  </style>

   <script type="text/javascript">
  $().ready(function() {
  [#if browse_version=="MicroMessenger"]  
    //分享
  $.ajax({
    url:"${base}/wap/mutual/get_config.jhtml",
    data:{
      url:location.href.split('#')[0]
    },
    dataType:"json",
    type:"get",
    success:function(message){
      if(message.type=="success"){
        var data=JSON.parse(message.content);
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timestamp, // 必填，生成签名的时间戳
            nonceStr: data.nonceStr, // 必填，生成签名的随机串
            signature: data.signature,// 必填，签名，见附录1
            jsApiList: ["onMenuShareTimeline","onMenuShareAppMessage"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function(){
          wx.onMenuShareTimeline({
              title: "${coupon.tenant.name}[${coupon.name}]", // 分享标题
              link: '${sharedUrl}', // 分享链接
              imgUrl: '${coupon.tenant.thumbnail}', // 分享图标
              success: function () { 
                  // 用户确认分享后执行的回调函数
              },
              cancel: function () { 
                  // 用户取消分享后执行的回调函数
              }
          });
          wx.onMenuShareAppMessage({
              title: "${coupon.tenant.name}[${coupon.name}]", // 分享标题
              desc: '${coupon.tenant.introduction}', // 分享描述
              link: '${sharedUrl}', // 分享链接
              imgUrl: '${coupon.tenant.thumbnail}', // 分享图标
              type: '', // 分享类型,music、video或link，不填默认为link
              dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
              success: function () { 
                  // 用户确认分享后执行的回调函数
              },
              cancel: function () { 
                  // 用户取消分享后执行的回调函数
              }
          });
        });
        wx.error(function(res){
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
        wx.checkJsApi({
            jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            }   
          });
      }else{
        invokTips("error",message.content);
      }
    }
  });
    
  // $("#share").on("click",function(){
  //   $(".m_share1").show();
  //   $(".m_sharebg").show();
  // });
  // //分享那妞
  //   $('.m_shareclose').on('click',function(){
  //              $('.m_share1').hide();
  //             $('.m_sharebg').hide();
  //     });
  //     $('.m_sharebg').on('click',function(){
  //              $('.m_share1').hide();
  //             $('.m_sharebg').hide();
  //     });
  //     $("#gotel").on("click",function(){
  //     location.href="tel:${coupon.tenant.telephone}";
  //   }); 
  
  [/#if]     
}); 
</script>
<script type="text/javascript">
  $().ready(function(){
      function is_weixin() {
          var ua = navigator.userAgent.toLowerCase();
          if (ua.match(/MicroMessenger/i) == "micromessenger") {
              return true;
          } else {
              return false;
          }
      }
      var isWeixin = is_weixin();
      if(isWeixin){
         $("#share").on("click",function(){
              $(".weixin-tip").show();
              $(".weixin-tip").on("click",function(){
                $(".weixin-tip").hide();
            });
         });      
      }
  });
  
</script>
</head>
<body>
<div class="am-g Shop_coupons" id="wrapper">
    <div class="am-intro-bd am-container mb_10">
        <div class="am-intro-left am-u-sm-3 " onclick="location.href='${base}/wap/tenant/index/${coupon.tenant.id}.jhtml'">
            <img src="${coupon.tenant.logo}" alt="" class="am-circle am-img-responsive" />
        </div>
        <div class="am-intro-right am-u-sm-9">
            <div class="am-g">
                <div class="Shop_name" onclick="location.href='${base}/wap/delivery/${delivery.id}/index.jhtml'">${coupon.tenant.name}</div>
            </div>
            <div class="am-g">
                <i class="wap-icon-start-${(coupon.tenant.score * 2)?string("0")} wap-icon-left"></i>
            </div>
        </div>
    </div>
    <div class="am-g Shop_coupons_bg">
        <div class="am-g">
            <h1 class="shop_sum">￥<span>${coupon.amount}</span></h1>
            <div class="shop_sum_instructions mb_10">
              - - -  恭喜你!获得店铺优惠券~  - - -
            </div>
            <div class="shop_sum_instructions">
                消费满${coupon.minimumPrice}元抵${coupon.amount}元
            </div>
            <div class="shop_sum_instructions">
                有效期至：${coupon.endDate}
            </div>
        </div>
        <div class="am-g">
          <div class="btn-shop_share am-u-sm-8 am-u-sm-centered mt_8" id="share">
              告诉小伙伴
          </div>
          <div class="btn-shop_share am-u-sm-8 am-u-sm-centered mt_3" onclick="location.href='${base}/wap/tenant/index/${coupon.tenant.id}.jhtml'">
              进店逛逛
        </div>
    </div>
</div>
<div class="weixin-tip">
    <p>
      <img src="${base}/resources/wap/image/am-wxprompt.png" style="max-width: 100%; height: auto;" />
    </p>
  </div>
</body>
</html>
