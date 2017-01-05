<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <title>领取[#if coupon.type=="multipleCoupon"]现金券[#else ]代金券[/#if]</title>
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
    <link rel="stylesheet" href="${base}/resources/wap/2.0/fonts/iconfont.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/css/common.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/coupon.css">
    <style type="text/css" media="screen">
        .Shop_name {
            font-size: 16px;
        }
    </style>
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
    <script src="${base}/resources/wap/js/flexible_css.debug.js"></script>
    <script src="${base}/resources/wap/js/flexible.debug.js"></script>
    <script src="${base}/resources/wap/js/amazeui.min.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

    <script type="text/javascript">
        function invokTips(type, content) {//提示框
            if (type == "success") {
                $('#inok').html("<i class=\"am-icon-check\"></i>&nbsp;" + content);
            } else if (type == "error") {
                $('#inok').html("<i class=\"am-icon-close\"></i>&nbsp;" + content);
            } else if (type == "warn") {
                $('#inok').html("<i class=\"am-icon-exclamation-triangle\"></i>&nbsp;" + content);
            }
            $("#inok").show();
            setTimeout(function () {
                $('#inok').fadeOut(1000)
            }, 1000);
        }
        function is_weixin() {//是否是微信浏览器
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                return true;
            } else {
                return false;
            }
        }
        $().ready(function () {
            [#--if ("${coupon.status}" == "Expired") {//已过期--]
                [#--$(".Shop_coupons_bg").first().prepend('<div class="shop_img_get"></div>');--]
                [#--$("#clickCoupon").css("background", "#a6a6a5");--]
                [#--$("#clickCoupon").text("告诉小伙伴").css("color", "#6F6867");--]
                [#--$("#clickCoupon").attr("id", "");--]
            [#--} else if ("${coupon.status}" == "unUsed" || "${coupon.status}" == "unBegin") {//未开始或者已领完--]
                [#--$("#clickCoupon").css("background", "#a6a6a5");--]
                [#--$("#clickCoupon").text("领取优惠券").css("color", "#6F6867");--]
                [#--$("#clickCoupon").attr("id", "");--]
            [#--} else if ("${coupon.status}" == "canUse") {//可以领用--]
                [#--if ("${couponCode}" != "") {//已经领过--]
                    [#--$(".Shop_coupons_bg").first().prepend('<div class="shop_img_get"></div>');--]
                    [#--$("#clickCoupon").text("告诉小伙伴");--]
                [#--}--]
            [#--}--]

            if ("${coupon.type}" != "multipleCoupon"&&"${couponCode}" != "") {//已经领过
                $(".Shop_coupons_bg").first().prepend('<div class="shop_img_get"></div>');
                $("#clickCoupon").text("告诉小伙伴");
            }

            /**点击领取优惠券按钮*/
            $("#clickCoupon").click(function () {
                if ($("#clickCoupon").text().trim() == "告诉小伙伴") {
                    var isWeixin = is_weixin();
                    if (isWeixin) {
                        $(".weixin-tip").css("height", "100%");
                        $(".weixin-tip").show();
                        $(".weixin-tip").on('click', 'p', function (event) {
                            event.preventDefault();
                            event.stopPropagation();
                        });
                        $(".weixin-tip").on("click", function () {
                            $(".weixin-tip").hide();
                        });
                    }
                } else if ($("#clickCoupon").text().trim() == "领取优惠券"||$("#clickCoupon").text().trim() == "领取现金券") {
                    $.ajax({
                        url: "${base}/wap/coupon/judge.jhtml",
                        type: "post",
                        data: {id:${coupon.id}, no: "${no}"},
                        dataType: "json",
                        success: function (message) {
                            if (message.type == "error") {
                                invokTips(message.type, message.content);
                                setTimeout(function () {
                                    $("#art").click();//间接触发注册弹框
                                }, 1500);
                            } else if (message.type == "success") {
                                if (message.content == "领取成功") {
                                    if ("${coupon.type}" == "multipleCoupon") {
                                        location.href = "${base}/wap/coupon/receive/${coupon.id}.jhtml?no=${no}&type=success";
                                    } else {
                                        location.href = "${base}/wap/coupon/inform.jhtml?id=${coupon.id}";
                                    }
                                } else if (message.content == "已领取") {
                                    $(".Shop_coupons_bg").first().prepend('<div class="shop_img_get"></div>');
                                    $("#clickCoupon").text("告诉小伙伴");
                                }
                            } else if (message.type == "warn") {
                                invokTips(message.type, message.content);
                                if (message.content == "已领完") {
                                    $(".Shop_coupons_bg").first().prepend('<div class="shop_img_get"></div>');
                                    $("#clickCoupon").css("background", "#a6a6a5");
                                    $("#clickCoupon").text("告诉小伙伴").css("color", "#6F6867");
                                    $("#clickCoupon").attr("id", "");
                                } else if (message.content == "活动未开始" || message.content == "活动已结束") {
                                    $("#clickCoupon").css("background", "#a6a6a5");
                                    $("#clickCoupon").text("点击领取优惠券").css("color", "#6F6867");
                                    $("#clickCoupon").attr("id", "");
                                }

                            }
                        }
                    });
                }
            });

            /**发送验证码*/
            $("#btnShopShare").click(function () {
                var isWeixin = is_weixin();
                if (isWeixin) {
                    $(".weixin-tip").css("height", "100%");
                    $(".weixin-tip").show();
                    $(".weixin-tip").on('click', 'p', function (event) {
                        event.preventDefault();
                        event.stopPropagation();
                    });
                    $(".weixin-tip").on("click", function () {
                        $(".weixin-tip").hide();
                    });
                }
            });

            /**发送验证码*/
            $("#btnSendCode").click(function () {
                $.ajax({
                    url: "${base}/wap/coupon/send_mobile.jhtml",
                    type: "post",
                    data: {mobile: $("#mobile").val()},
                    dataType: "json",
                    success: function (message) {
                        if (message.type == "error") {
                            invokTips(message.type, message.content);
                        } else if (message.type == "success") {
                            sendMessage();
                            invokTips(message.type, message.content);
                        }
                    }
                });
            });
            /**提交注册信息/登录*/
            $("#am-modal-btn-shop").click(function () {
                $.ajax({
                    url: "${base}/wap/coupon/register.jhtml?id=${coupon.id}",
                    type: "post",
                    data: {captcha: $("#captcha").val(), mobile: $("#mobile").val()},
                    dataType: "json",
                    success: function (message) {
                        if (message.type == "success") {
                            if (message.content == "已领取") {
                                $(".Shop_coupons_bg").first().prepend('<div class="shop_img_get"></div>');
                                $(".shop_img_get").css("background", "url(${base}/resources/wap/img/coupons_00.png) no-repeat center");
                                $("#clickCoupon").text("告诉小伙伴");
                            } else if (message.content == "领取成功") {
                                invokTips(message.type, message.content);
                                location.href = "${base}/wap/coupon/inform.jhtml?id=${coupon.id}";
                            }
                        } else {
                            invokTips(message.type, message.content);
                        }
                    },
                    error: function () {
                        alert("error")
                    }
                });
            });
        });
    </script>

    <script type="text/javascript">
        /**60秒时间计数*/
        var InterValObj; //timer变量，控制时间
        var count = 60; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        var code = ""; //验证码
        var codeLength = 6;//验证码长度
        function sendMessage() {
            curCount = count;
            var dealType; //验证方式
            var uid = $("#uid").val();//用户uid
            if ($("#phone").attr("checked") == true) {
                dealType = "phone";
            }
            else {
                dealType = "email";
            }
            // 产生验证码
            for (var i = 0; i < codeLength; i++) {
                code += parseInt(Math.random() * 9).toString();
            }

            //设置button效果，开始计时
            $("#btnSendCode").attr("disabled", "true");
            $("#btnSendCode").val(+curCount + "秒再获取");
            InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
        }
        //timer处理函数
        function SetRemainTime() {
            if (curCount == 0) {
                window.clearInterval(InterValObj);//停止计时器
                $("#btnSendCode").removeAttr("disabled");//启用按钮
                $("#btnSendCode").val("重新发送验证码");
                code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
            }
            else {
                curCount--;
                $("#btnSendCode").val(+curCount + "秒再获取");
            }
        }
    </script>
    <script type="text/javascript">
        /**微信分享*/
        $().ready(function () {
        [#if browse_version=="MicroMessenger"]
            //分享
            $.ajax({
                url: "${base}/wap/mutual/get_config.jhtml",
                data: {
                    url: location.href.split('#')[0]
                },
                dataType: "json",
                type: "get",
                success: function (message) {
                    if (message.type == "success") {
                        var data = JSON.parse(message.content);
                        wx.config({
                            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                            appId: data.appId, // 必填，公众号的唯一标识
                            timestamp: data.timestamp, // 必填，生成签名的时间戳
                            nonceStr: data.nonceStr, // 必填，生成签名的随机串
                            signature: data.signature,// 必填，签名，见附录1
                            jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                        });
                        wx.ready(function () {
                            wx.onMenuShareTimeline({
                                title: "${(coupon.tenant.name)!}[${coupon.name}]", // 分享标题
                                link: "${sharedUrl}", // 分享链接
                                imgUrl: '${(coupon.tenant.thumbnail)!}', // 分享图标
                                success: function () {
                                    // 用户确认分享后执行的回调函数
                                },
                                cancel: function () {
                                    // 用户取消分享后执行的回调函数
                                }
                            });
                            wx.onMenuShareAppMessage({
                                title: "${(coupon.tenant.name)!}[${coupon.name}]", // 分享标题
                                desc: '${coupon.introduction}', // 分享描述
                                link: '${sharedUrl}', // 分享链接
                                imgUrl: '${(coupon.tenant.thumbnail)!}', // 分享图标
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
                        wx.error(function (res) {
                            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                        });
                        wx.checkJsApi({
                            jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                            success: function (res) {
                                // 以键值对的形式返回，可用的api值true，不可用为false
                                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                            }
                        });
                    } else {
                        invokTips("error", message.content);
                    }
                }
            });
        [/#if]
        });
    </script>

[#--使用规则弹窗--]
    <script type="text/javascript" class="dialog js_show">
        $(function () {
            var $iosDialog2 = $('#iosDialog2');
            $('#dialogs').on('click', '.weui-dialog__btn', function () {
                $(this).parents('.js_dialog').fadeOut(200);
            });
            $('#showIOSDialog2').on('click', function () {
                $iosDialog2.fadeIn(200);
            });
        });</script>

</head>
<body>
<div class="am-g Shop_coupons">
    <div id="inok" class="inok"></div>
    <div class="am-intro-bd am-container guize">
    [#if coupon.tenant??]
        <div class="am-intro-left am-u-sm-3"
             onclick="location.href='${base}/wap/tenant/index/${coupon.tenant.id}.jhtml'">
            <img src="${coupon.tenant.thumbnail}" alt="" class="am-circle am-img-responsive"/>
        </div>
        <div class="am-intro-right am-u-sm-9" onclick="location.href='${base}/wap/tenant/index/${coupon.tenant.id}.jhtml'">
            <div class="am-g" onclick="location.href='${base}/wap/tenant/index/${coupon.tenant.id}.jhtml'">
                <div class="Shop_name">${coupon.tenant.name}</div>
            </div>
            <div class="am-g">
                <i class="iconfont" style="font-size: 12px;letter-spacing: -0.12em;color: #00a0e9;">
                    [#if coupon.tenant.score?size != 0]
                        [#list 1..coupon.tenant.score as i]
                            &#xe675;
                        [/#list]
                    [/#if]
                </i>
            </div>
        </div>
    [#else ]
        <span class="ft-bs15 clr-grey01" id="showIOSDialog2">使用规则</span>
    [/#if]
    </div>

    <div id="dialogs">
        <div class="js_dialog" id="iosDialog2" style=" display: none;">
            <div class="weui-mask"></div>
            <div class="weui-dialog">
                <div class="weui-dialog__bd">
                    <div class="rule-window">
                        <p>1.每张现金券实行的是一码一券制，扫码领取后纸质现金劵将立即作废。</p>
                        <p>2.现金券可在所有商盟商家优惠买单时使用。</p>
                        <p>3.现金券可抵扣购买商品的部分货款，可与店内其他优惠活动同时使用。</p>
                        <p>4.现金券可跨店，可跨类目使用。</p>
                        <p>本活动最终解释权归${setting.siteName}所有。</p>
                    </div>
                </div>
                <div class="weui-dialog__ft">
                    <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary">好的，知道了</a>
                </div>
            </div>
        </div>
    </div>

    <div class="am-g none">
        <div class="am-container">
            <i class="icon-caret-up" id="abc"></i>
            <div class="shop_sign">
            [#if coupon.introduction??]
                <span>
                ${coupon.introduction}
                  </span>
            [/#if]
            </div>
        </div>
    </div>

    <div class="am-g Shop_coupons_bg">
        <div class="am-g mar [#if coupon.tenant??]mar0[/#if]">
            <h1 class="shop_sum"><span>￥${coupon.amount}</span></h1>
        [#if coupon.tenant??]
            <div class="shop_sum_conditions mb_9 mar1">
                消费满${coupon.minimumPrice}元抵${coupon.amount}元
            </div>
            <div class="shop_sum_instructions ">
                剩余${coupon.count-coupon.sendCount}张
            </div>
            <div class="shop_sum_instructions ">
                有效期至：${coupon.endDate}
            </div>
        [#else ]
            <div class="shuoming">--- ~ 平台现金券 ~ ---</div>
        [/#if]
        </div>
        <div class="am-g">
            <div class="btn-shop_share am-u-sm-8 am-u-sm-centered mt_8" id="clickCoupon">
                领取[#if coupon.type=="multipleCoupon"]现金券[#else ]优惠券[/#if]
            </div>
        [#if coupon.tenant??]
            <div class="btn-shop_share am-u-sm-8 am-u-sm-centered mt_3"
                 onclick="location.href='${base}/wap/tenant/index/${coupon.tenant.id}.jhtml'">
                进店逛逛
            </div>
        [#else ]
            <div class="btn-shop_share am-u-sm-8 am-u-sm-centered mt_3" id="btnShopShare">
                分享
            </div>
        [/#if]
        </div>
    </div>
</div>
<!--注册弹框-->
<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
    <div class="am-modal-dialog open_shop_e8eaea">
        <div class="am-g">
            <div class="am-g nation_p color_333">
                <p>请输入你的手机号</p>
            </div>
            <div class="am-container">
                <div class="am-input-group am-input-grou_bg">
                    <div class="am-btn am-btn_333">
                        <span class="am-fl">国家和地区</span>
                         <span class="am-fr">中国
                         </span>
                    </div>
                    <div class="am-btn_phone">
                        <span class="">+86</span>
                        <span class="am-btn_separator">|</span>
                        <input type="text" id="mobile" name="mobile">
                    </div>
                </div>
                <input type="text" placeholder="验证码" class="am_input_code" name="captcha" id="captcha">
                <input type="button" value="获取验证码" class="bg_red_1 am_input_code_bg" id="btnSendCode">
            </div>
        </div>
        <div class="am-modal-footer">
            <span class="am-modal-btn" id="am-modal-btn-shop">确定</span>
        </div>
    </div>
</div>
<!--遮盖层-->
<button id="art" data-am-modal="{target: '#my-alert',closeViaDimmer: 0}" style="display:none;"></button>
<div class="weixin-tip">
    <p>
        <img src="${base}/resources/wap/2.0/images/share_youhuiquan-mask-01.png"/>
    </p>
    <div>
        <a href="javascript:;" class="">知道了</a>
    </div>
</div>
</body>
</html>
