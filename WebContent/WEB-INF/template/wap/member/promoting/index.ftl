<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>我的推广</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/lib.js"></script>
    <!-- <script src="${base}/resources/wap/2.0/js/lib/handlebar-loader.js"></script>-->
    <script src="${base}/resources/wap/2.0/js/whostrap.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">

    </script>
</head>
<body>
<script type="text/html" id="tpl_wraper">
<!-- 主区域-->
<div class="mainR MBPI">
    <div class="hd">
        <span>推广赚分润</span>
    </div>
    <div class="bd">
        <span><b class="money">${rebateAmount?string("0.00")}</b>元</span>
    </div>
    <div class="ft">
        <a href="javascript:;" class="howmany">我已成功邀请 <span>${promotingMembers!}</span> 人</a>
        [#if promotingMember!=""]
        <p class="who">我的推荐人${promotingMember!}</p>
        [/#if]
    </div>
</div>
<!-- 邀请区域-->
<div class="inviteR MBPI">
    <div class="symbol">
        <img src="${base}/resources/wap/2.0/images/promoting-index-001.png" alt="">    
    </div>
    <div class="desc">
        <p>邀请TA一起逛遍同城好店</p>
        <a href="javascript:;" class="scan_regular">查看分润规则></a>
    </div>
    <a href="javascript:;" class="invite_friends">邀请好友</a>
</div>
<!-- 二维码按钮-->
<!-- div class="coner_qrsign MBPI">
    <a class="iconfont">&#xe62e;</a>
</div -->

[#--include "/wap/include/footer.ftl"/--]
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
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        if(parseInt($(".mainR .howmany span").text(), 10)){
            $(".mainR .howmany").addClass('active').attr('href', '${base}/wap/member/promoting/list.jhtml');
        }
        //$(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'});
        //var compiler = Handlebars.compile($("#wap-list-item").html());
        //虚拟数据
        //var data_listitem={};
        //$("#am_g").html(compiler(data_listitem));
        //console.log(data_listitem.length);
        $(".inviteR.MBPI").on('click', '.invite_friends', function(event) {
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
        //
        hbsTO.prefetch("dialog");
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
        $(".scan_regular").on('click', function(event) {
            event.preventDefault();
            $(".promoting_regular").dialog("show");
        });
        $(".coner_qrsign").on('click', function(event) {
            event.preventDefault();
            $(".qrcodeboxR.MBPI").removeClass('none');
            setTimeout(function(){
                $(".qrcodeboxR.MBPI .qrcodebox").removeClass('comeout');
            },50);
        });
        $(".qrcodeboxR").on('click', '.weui_mask', function(event) {
            event.stopPropagation();
            event.preventDefault();
            $(this).siblings('.qrcodebox').addClass('comeout');
            $(this).siblings('.qrcodebox').on('transitionend webkitTransitionEnd', function(event) {
                event.preventDefault();
                $(".qrcodeboxR").addClass('none');
                $(this).off("transitionend webkitTransitionEnd");
            });

        });
    });
</script>
</body>
</html>