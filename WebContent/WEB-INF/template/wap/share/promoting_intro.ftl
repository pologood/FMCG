<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>我的推广</title>
    <script src="${base}/resources/wap/2.0/js/zepto.waypoints.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/lib.js"></script>
    <script src="${base}/resources/wap/2.0/js/whostrap.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">

    </script>
</head>
<body>
<script type="text/html" id="tpl_wraper">
    <img src="${base}/upload/images/promoting_intro-001[#if setting.siteName=='直通邦']-zhitongbang[/#if].jpg">
    <img src="${base}/resources/wap/2.0/images/promoting_intro-002.jpg" alt="">
    <img src="${base}/resources/wap/2.0/images/promoting_intro-003.jpg" alt="">
    <img src="${base}/resources/wap/2.0/images/promoting_intro-004.jpg" alt="">
    <!-- 
    <a href="${base}/wap/bound/indexNew.jhtml?extension=${extension}" style="display:block" class="btm-reg">
        <img src="${base}/resources/wap/2.0/images/promoting_intro-005.jpg" alt="">
    </a>-->
    <div class="btm-reg">
        <a href="${base}/wap/bound/indexNew.jhtml?extension=${extension}">
            <span>
                <b>立即注册</b>
            </span>
            <div class="finger-pressme anm-init">
                
            </div>
        </a>
    </div>

</script>

<div class="container">
</div>
<div class="qrcodeboxR MBPI none">
    <div class="weui_mask">
    </div>
    <div class="qrcodebox comeout">
        <img src="${base}/resources/wap/2.0/images/qr_code.png"/>
    </div>
</div>
<!-- 分享提示-->
<div class="sharelayer-tip">
    <p>
        <img src="${base}/resources/wap/2.0/images/promoting-index-002.png"/>
    </p>
    <div>
        <a href="javascript:;" class="">知道了</a>
    </div>
</div>

<!-- 分润规则-->
<div class="promoting_regular">

</div>
<!-- 滚动到页面底部元素-->
<div class="downtoregister SEPGI">
</div>
<!-- 回到顶部-->
<div class="backtotop">
</div>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        //prefetch
        hbsTO.prefetch("dialog");
        //picLazyLoad
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });
        var compiler = Handlebars.compile($("#wap-list-item").html());
        //虚拟数据
        var data_listitem = {};
        $("#am_g").html(compiler(data_listitem));
        //回到顶部
        $(".backtotop").positionScroller({
            plugintype: "positionscroller", //默认值
            autohide_threshold : $(window).height() / 2,
            autohide: true,
            tpldatas: {
                ctns: {
                    //txt: "置顶"
                },
                hooks: {
                    wrapperid: "backtotop"
                },
                Ctrls: {
                    tplname: "positionscroller"
                }
            }
        });
        //跳转到底部立即注册
        $(".downtoregister.SEPGI").positionScroller({
            plugintype: "positionscroller", //默认值
            targetposY: $(document).height(),
            tpldatas: {
                ctns: {
                    head: '<img src="${base}/resources/wap/2.0/images/promoting_intro-registernow.png" onload="this.width=this.width/2">',
                    body: "<a href='javascript:;'>立即注册</a>"
                },
                hooks: {
                    wrapperid: "downtoregister"
                },
                Ctrls: {
                    tplname: "positionscroller"
                }
            }
        });
        $(".downtoregister.SEPGI").on('initialized:WHT:positionscroller', function(event) {
            $(this).positionScroller("show");
        });
        var waypoints = $('.btm-reg>a').waypoint({
            handler: function(direction) {
                //notify(this.element.id + ' hit');
                $(this.element).find('.finger-pressme').toggleClass('anm-in');
                //console.log("direction's value is as following:");
                //console.log(direction);
            },
            offset: function() {
              return Waypoint.viewportHeight() - this.element.clientHeight * 1.5;
            }
        });
        $('.btm-reg').on('click', 'a', function(event) {
            //event.preventDefault();
            var $this_span=$(this).find("span");
            $this_span.addClass('presseddown');
            setTimeout(function () {
                  $this_span.removeClass('presseddown');
            }, 150);
        });
        //console.log(data_listitem.length);
        $(".inviteR.MBPI").on('click', '.invite_friends', function (event) {
            event.preventDefault();
            $(".sharelayer-tip").show();
        });
        $(".sharelayer-tip p").on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
        });
        $(".sharelayer-tip").on("click", function () {
            $(".sharelayer-tip").hide();
        });
        //分润规则弹窗
        $(".promoting_regular").dialog({
            dialogtype: "alert",
            tpldatas: {
                ctns: {
                    txt_ok: "确定",
                    txt_title: "分润规则",
                    txt_body: "<div style='font-size:12px'><p>分润在您的钱包中，可以提现、购物。</p><br/><p style='text-align:left'>一、邀请好友注册成为${setting.siteName}会员，好友的订单您都能获得分润。</p><p style='text-align:left'>二、将您心仪的商品微信分享给好友购买后，您也会得到分润哦。</p><br/><p>确认订单后，分润才会产生。取消订单、退货，亲是得不到分润哒。</p></div>"
                },
                hooks: {
                    wrapperid: "promoting_regular"
                },
                Ctrls: {
                    tplname: "alert"
                }
            }
        });
        $(".scan_regular").on('click', function (event) {
            event.preventDefault();
            $(".promoting_regular").dialog("show");
        });
        $(".coner_qrsign").on('click', function (event) {
            event.preventDefault();
            $(".qrcodeboxR.MBPI").removeClass('none');
            setTimeout(function () {
                $(".qrcodeboxR.MBPI .qrcodebox").removeClass('comeout');
            }, 50);
        });
        $(".qrcodeboxR").on('click', '.weui_mask', function (event) {
            event.stopPropagation();
            event.preventDefault();
            $(this).siblings('.qrcodebox').addClass('comeout');
            $(this).siblings('.qrcodebox').on('transitionend webkitTransitionEnd', function (event) {
                event.preventDefault();
                $(".qrcodeboxR").addClass('none');
                $(this).off("transitionend webkitTransitionEnd");
            });

        });
    });
</script>
</body>
</html>