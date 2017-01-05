<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}收银台-${setting.siteName}</title>
    <meta name="keywords" content="${setting.siteName}-收银台"/>
    <meta name="description" content="${setting.siteName}-收银台"/>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon">
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2c/css/pay.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2c/css/submitOrder.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/base64.js"></script>
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript">
        var paymentSn;
        var flag = "true"
        var h = 23;
        var m = 59;
        var s = 60;
        var payMethod, paymentMthodName;
        $(function () {
//            $("#payMethod_radio").find($("[name=pay-mode]")).first().attr("checked", "checked");
//            payMethod = $("#payMethod_radio").find($("[name=pay-mode]")).first().val();
            $("#payMethod_radio").find($("[name=pay-mode]")).each(function(){
                if($(this).prop("checked")==true){
                    if($(this).attr("methodName")=="offline"){
                        $("#pay-tab").hide();
                        paymentMthodName="offline";
                    }else{
                        $("#pay-tab").show();
                        paymentMthodName="online";
                    }
                }

            });
            /*控制余额支付时，提交按钮的显示或隐藏*/
            $("#balance_pay").removeClass("pay-tab-close");
            $("#pay-tab-main-balance").css("display", "block");
            $("#balance_pay").on("click", function () {
                if ($("#weixin_main_pay").css("display") == "block") {
                    $("#confirm_pay").show();
                }
            });
            $("#platform_pay").on("click", function () {
                if ($("#pay-tab-main-balance").css("display") == "block") {
                    $("#confirm_pay").hide();
                }
            })
            /*平台支付*/
            $("#confirm_pay").click(function () {
                if (paymentMthodName == "offline") {
                    $.ajax({
                        url: "${base}/b2c/payment/submit_payment.jhtml",
                        type:"POST",
                        data:{
                            paymentPluginId: "offlinePayPlugin",
                            sn:${payment.sn},
                            ordersn:${order.sn}
                        },
                        dataType:"JSON",
                        success:function(){
                            location.href = "${base}/b2c/payment/payment_success.jhtml?sn=" +${order.sn};
                        }
                    });
                } else {
                    if (flag == "true") {
                        if ($("#password").val() == "") {
                            $.message("warn", "密码不能为空");
                        } else {
                            flag = "false";
                            $.message("success", "正在处理中，请稍后...");
                            $(this).text("处理中...");
                            $(this).css("background", "#e4e4e4");
                            $.ajax({
                                url: "${base}/common/public_key.jhtml",
                                type: "POST",
                                data: {local: true},
                                dataType: "JSON",
                                success: function (data) {
                                    var modulus = data.modulus;
                                    var exponent = data.exponent;
                                    var rsaKey = new RSAKey();
                                    rsaKey.setPublic(b64tohex(modulus), b64tohex(exponent));
                                    password = hex2b64(rsaKey.encrypt($("#password").val()));
                                    $.ajax({
                                        url: "${base}/b2c/payment/submit_payment.jhtml",
                                        type: "POST",
                                        data: {
                                            paymentPluginId: "balancePayPlugin",
                                            sn:${payment.sn},
                                            enPassword: password
                                        },
                                        dataType: "JSON",
                                        success: function (data2) {
                                            if (data2.message.type == "success") {
                                                $.message("success", data2.message.content);
                                                location.href = "${base}/b2c/payment/payment_success.jhtml?sn=" +${order.sn};
                                            } else {
                                                flag = "true";
                                                $.message(data2.message.type, data2.message.content);
                                                location.reload();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });

            /*微信扫码支付*/
            $("#wechat").click(function () {
                $.ajax({
                    url: '${base}/b2c/payment/create.jhtml',
                    type: "POST",
                    data: {
                        paymentPluginId: 'weixinQrcodePayPlugin',
                        amount:${order.amount},
                        type: "payment",
                        sn:${order.sn}
                    },
                    dataType: "JSON",
                    success: function (data) {
                        if (data.message.type != "success") {
                            $.message("error", data.message.content);
                        }
                        $.ajax({
                            url: "http://api.wwei.cn/wwei.html?apikey=20160806215020&data=" + data.data.code_url,
                            type: 'post',
                            dataType: "jsonp",
                            jsonp: "callback",
                            success: function (result) {
                                $(document).unbind("ajaxBeforeSend");
                                $("#weixin_qr").attr("src", result.data.qr_filepath);
                                InterValObj = window.setInterval(function () {
                                    queryCash(data.data.sn);
                                }, 3000);
                            }
                        });
                    }
                });
            });
            window.setInterval(function () {
                timer();
            }, 1000);
            function timer() {
                var time_start = new Date().getTime(); //设定当前时间
                var time_end = new Date($("#settime").attr("time")).getTime(); //设定目标时间
                // 计算时间差
                var time_distance = time_end - time_start;
                // 天
                var int_day = Math.floor(time_distance / 86400000)
                time_distance -= int_day * 86400000;
                // 时
                var int_hour = Math.floor(time_distance / 3600000)
                time_distance -= int_hour * 3600000;
                // 分
                var int_minute = Math.floor(time_distance / 60000)
                time_distance -= int_minute * 60000;
                // 秒
                var int_second = Math.floor(time_distance / 1000)
                // 时分秒为单数时、前面加零
                if (int_day < 10) {
                    if (int_day < 0) {
                        int_day = "00";
                    } else {
                        int_day = "0" + int_day;
                    }
                }
                if (int_hour < 10) {
                    int_hour = "0" + int_hour;
                }
                if (int_minute < 10) {
                    int_minute = "0" + int_minute;
                }
                if (int_second < 10) {
                    int_second = "0" + int_second;
                }
                document.getElementById("settime").innerHTML =
                        int_day + "天" + int_hour + "时" + int_minute + "分" + int_second + "秒";
            }
        });

        function getPayMethod(o) {
            payMethod = $(o).val();
            paymentMthodName = $(o).attr("methodName");
        }

        /*微信支付*/
        function queryCash(sn) {
            $.ajax({
                url: '${base}/b2c/payment/weixin_payment.jhtml',
                data: {
                    sn: sn
                },
                type: 'post',
                dataType: 'json',
                success: function (dataBlock) {
                    if (dataBlock.message.type == "success") {
                        window.clearInterval(InterValObj);
                        location.href = "${base}/b2c/payment/payment_success.jhtml?sn=" +${order.sn};
                    }
                    if (dataBlock.message.type == "error") {
                        window.clearInterval(InterValObj);
                        alert(dataBlock.message.content);
                    }
                }
            });
        }

        function showOrDisplay(o) {
            if ($(o).attr("methodname")=="online") {
                $("#pay-tab").show();
            } else {
                $("#pay-tab").hide();
            }
        }
    </script>
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2c/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--logo 搜索开始-->
[#include "/b2c/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--guide 导向开始-->
    <div class="container">
        <div class="guide">
            <div class="guideProcess">
                <div class="guideProcessAll processOne on">
                    <div class="title"><h1>1</h1></div>
                    <span>购物车</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processTwo on">
                    <div class="title"><h1>2</h1></div>
                    <span>确认订单</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processThree on">
                    <div class="title"><h1>3</h1></div>
                    <span>支付</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processFour">
                    <div class="title"><h1>√</h1></div>
                    <span>完成</span>
                    <div class="processBar"></div>
                </div>
            </div>
            <div class=""></div>
        </div>
    </div>
    <!--guide 导向结束-->
</div>
<!--头部end-->
<form style="">
    <div style="display:none;">
        <div style="width:40%;height:30%;margin-left:30%;z-index:1000000;position:absolute;background-color:white;top:20%;">
            <div style="height:30%;width:100%;font-size:20px;text-align:center;border-bottom:1px solid #666666;line-height:50px;">
                余额支付
            </div>
            <div style="height:40%;width:100%;font-size:20px;padding-top:20px;">
                <div style="margin-left:20px;">请输入密码：
                    <input type="password" name="enPassword"
                           style="width:60%;height:30px;margin-bottom:1px solid black;"/>
                </div>
            </div>
            <div style="height:30%;width:100%;font-size:20px;text-align:center;border-top:1px solid #666666;line-height:50px;margin-top:-20px;">
                提交支付
            </div>
        </div>
    </div>
</form>
<!--start-->
<div class="container">
    <div class="pay-order-head clearfix">
        <div class="tit">
            <span>订单提交成功，订单账号：<span>${order.sn}</span>，请您于</span>
            <span class="color" id="settime" time="${order.expire}"></span>
            <span>内完成支付</span>
            <span class="tit-alert">(逾期订单将被取消)</span>
        </div>
        <div class="mon">
            <span>应付金额：</span>
            <span class="mon-num">
                <span>¥</span>
                <span>${order.amount}</span>
            </span>
        </div>
    </div>
    <div class="pay-mode">
        <dl>
            <dt>支付方式</dt>
            <dd id="payMethod_radio">
            [#list paymentMethods as payMethod]
                <label class="on">
                    <input type="radio" name="pay-mode" value="${payMethod.id}" methodName="${payMethod.method}"
                           onchange="getPayMethod(this)" onclick="showOrDisplay(this)" [#if order.paymentMethod.method==payMethod.method]checked="true"[/#if]/>
                ${payMethod.name}
                </label>
            [/#list]
            </dd>
        </dl>
    </div>
    <div class="pay-tab" id="pay-tab">
        <!--余额支付-->
        <div class="slide-tab pay-tab-close" id="balance_pay">
            <div class="pay-tab-hd ">
                <span class="tit">余额支付</span>
                <span class="pay-tab-hd-alert">
                    <span>可用余额：</span>
                    <span>${order.member.balance}</span>
                    <span> 元</span>
                </span>
                <i class="icon-folder"></i>
            </div>
            <div class="pay-tab-main clearfix" id="pay-tab-main-balance">
                <div class="card-txt">
                    <input type="password" placeholder="输入支付密码" id="password">
                    <span class="card-numtip"></span>
                    <!-- <a href="javascript:;" class="btn-confirm">确认</a> -->
                </div>
            </div>
        </div>
        <!--快捷支付pay-tab-close-->
        <!-- <div class="slide-tab " id="fast_pay">
            <div class="pay-tab-hd ">
                <span class="tit" mark="mark" id="fast_mark">快捷支付</span>
                <i class="icon-fastpay"></i>
                <span class="pay-tab-hd-alert">安全可靠，便利快捷</span>
                <i class="icon-folder"></i>
            </div>
            <div class="pay-tab-main clearfix" style="display: block"> -->
        <!-- 快捷支付列表 -->
        <!-- <div class="bank-list-line">
                    <div id="bank-MS" class="bank-list-item">
                        <span class="logo">
                            <img src="//www.mogujie.com/img/bank/0010.jpg" width="130" height="40" alt="">
                        </span>
                        <span class="card-no">
                            ** 6324
                        </span>
                        <span class="card-type"></span>
                        <i class="icon-fastpay"></i>
                        <span class="limit">
                            限额5000元／次
                        </span>
                        <span class="paynum payhover">
                       
                            支付
                            <span class="num">${order.amount}</span>
                            元
                        </span>
                    </div>
                </div> -->
        <!-- 快捷支付列表 end -->
        <!-- 输入银行卡 -->
        <!-- <div class="card-txt">
            <input type="text" maxlength="19" placeholder="直接输入银行卡号进行识别">
            <span class="card-numtip"></span>
            <a href="javascript:;" class="btn-confirm">确认</a>
        </div> -->
        <!-- 输入银行卡 end -->
        <!-- </div>
    </div> -->
        <!--平台支付-->
        <div class="slide-tab pay-tab-close" id="platform_pay">
            <div class="pay-tab-hd">
                <span class="tit" mark="mark" id="other_mark">其他支付</span>
                <span class="pay-tab-hd-alert">支持微信支付、支付宝、银联支付等多种支付方式</span>
                <i class="icon-folder"></i>
            </div>
            <div class="pay-tab-main clearfix" id="weixin_main_pay">
                <!-- 平台支付列表 -->
                <div class="bank-list-list clearfix">
                    <!-- <div id="alipay" class="bank-list-item">
                        <span class="logo">
                            <img src="//s7.mogujie.com/pic/141105/ubzlo_ieydqy3fgq3winjvmqytambqhayde_130x40.jpg" width="130" height="40" alt="">
                        </span>
                    </div> -->
                    <div id="wechat" class="bank-list-item">
                        <span class="logo">
                            <img src="//s7.mogujie.com/pic/141016/fanxiang_ieygentdg5swkzlfmmytambqgyyde_130x40.jpg"
                                 width="130" height="40" alt="">
                        </span>
                    </div>
                    <!-- <div id="unionpay" class="bank-list-item">
                        <span class="logo">
                            <img src="//s7.mogujie.com/pic/131219/o613k_kqzfct2ekrbewvsugfjeg5sckzsew_130x40.png" width="130" height="40" alt="">
                        </span>
                    </div> -->
                </div>
                <!-- 平台支付列表 end -->
            </div>
        </div>

    </div>
    <div class="pay-tab pay-method theme-light">
        <div class="pay-tab-hd pay-method-hd">
            <span class="tit" mark="mark">快捷支付</span>
            <i class="icon-fastpay"></i>
            <span class="pay-tab-hd-alert">安全可靠，便利快捷</span>
            <a href="javascript:;" class="link">
                <span>选择其他支付方式</span>
                <i class="icon-ankr"></i>
            </a>
        </div>
        <div class="pay-tab-main clearfix">
            <!-- 快捷支付列表 -->
            <div class="bank-list-line clearfix fn-paymethodLine">
                <div class="bank-list-item bank-list-item-select">
                    <span class="logo">
                        <img src="//www.mogujie.com/img/bank/0010.jpg" width="130" height="40" alt="">
                    </span>
                    <span class="paynum">
                    <!-- TODO -->
                    <span class="red-bag">
                        （已使用红包抵扣
                        <span class="num">0.00</span>
                        元）
                    </span>
                    支付<span class="num">${order.amount}</span>元
                    </span>
                </div>
            </div>
            <!-- 快捷支付列表 end -->

            <!-- 微信扫码 -->
            <div class="code2-wechat clearfix fn-wechat-detail">
                <div class="code-wrap">
                    <img class="code" width="230" height="230" src="" alt="" id="weixin_qr">
                </div>
                <div class="tip">
                    <h5>请使用微信扫描二维码<br>以完成支付</h5>
                    <p>
                        <a href="javascript:;" target="_blank" class="link">详细使用帮助</a>
                    </p>
                    <p>（如果支付遇到问题，可<a href="javascript:;" target="_blank" class="link">联系客服</a>）</p>
                </div>
            </div>

            <!-- 支付宝扫码 -->
            <div class="code2-alipay clearfix fn-alipay-detail">
                <div class="bx">
                    <h5>使用支付宝钱包扫码支付</h5>
                    <div class="bx-cnt bx-cnt1">
                        <iframe class="code" scrolling="no"
                                src="https://mapi.alipay.com/gateway.do?sign=7f6b0273227028a99d4730980e13064e&body=17&_input_charset=utf-8&it_b_pay=60m&subject=%E8%98%91%E8%8F%87%E8%A1%97%E6%94%AF%E4%BB%98&total_fee=88.20&sign_type=MD5&service=create_direct_pay_by_user&notify_url=http%3A%2F%2Fgateway.pay.mogujie.com%2Fcallback%2Falipay%2FpayNotify&qr_pay_mode=0&partner=2088601997114006&seller_id=2088601997114006&seller_email=corp%40mogujie.com&out_trade_no=1603180231874960&payment_type=1&return_url=http%3A%2F%2Fgateway.pay.mogujie.com%2Fcallback%2Falipay%2FpayReturn"
                                width="320" height="250" frameborder="0"></iframe>
                    </div>
                </div>
                <div class="bx">
                    <h5>使用电脑支付</h5>
                    <div class="bx-cnt bx-cnt2">
                        <div class="payicon"></div>
                        <a href="javascript:;" class="btn-pay">确认并付款</a>
                    </div>
                </div>
                <div class="fence-line">
                    <div class="fence">或</div>
                </div>
            </div>

            <!-- 首次支付 -->
            <div class="pay-form fn-fastpay_1st-detail">
                <form>
                    <dl class="form pay-form-credit">
                        <dt>付款银行：</dt>
                        <dd>
                            <div class="bank-info">
                                <img class="bank-icon" src="//www.mogujie.com/img/bank/0010.jpg" width="130" height="40"
                                     alt="">
                                <span class="type"></span>
                                <i class="icon-fastpay"></i>
                            </div>
                        </dd>

                        <dt>信用卡卡号：</dt>
                        <dd>
                            <span>5289488888888888</span>
                        </dd>

                        <dt>有效期：</dt>
                        <dd>
                            <select name="cardDM">
                                <option value="">请选择</option>
                                <option value="01">01</option>
                                <option value="02">02</option>
                                <option value="03">03</option>
                                <option value="04">04</option>
                                <option value="05">05</option>
                                <option value="06">06</option>
                                <option value="07">07</option>
                                <option value="08">08</option>
                                <option value="09">09</option>
                                <option value="10">10</option>
                                <option value="11">11</option>
                                <option value="12">12</option>
                            </select>
                            <span>月／</span>

                            <select name="cardDY">
                                <option value="">请选择</option>
                                <option value="14">14</option>
                                <option value="15">15</option>
                                <option value="16">16</option>
                                <option value="17">17</option>
                                <option value="18">18</option>
                                <option value="19">19</option>
                                <option value="20">20</option>
                                <option value="21">21</option>
                                <option value="22">22</option>
                                <option value="23">23</option>
                                <option value="24">24</option>
                                <option value="25">25</option>
                            </select>
                            <span>年</span>
                        </dd>

                        <dt>卡验证码：</dt>
                        <dd><input type="text" name="cardCode" class="txt txt-small" maxlength="3"></dd>

                        <dt>真实姓名：</dt>
                        <dd>
                            <span></span>
                            <input type="text" name="name" class="txt">
                        </dd>

                        <dt>身份证号：</dt>
                        <dd>
                            <span></span>
                            <input type="text" name="idCard" class="txt">
                        </dd>

                        <dt>留存手机号：</dt>
                        <dd><input type="text" name="phone" class="txt"></dd>

                        <dt>短信验证码：</dt>
                        <dd>
                            <input type="text" name="smscode" class="txt txt-small">
                            <a href="javascript:;" class="btn-sms btn-sms-enable">发送短信</a>
                        </dd>

                        <dt></dt>
                        <dd>
                            <p class="notice"></p>
                            <p class="agree">
                                <input type="checkbox" name="" id="agree-1stc" checked="checked">
                                <label for="agree-1stc">我已阅读并同意《<a href="javascript:;"
                                                                   target="_blank">快捷支付用户协议</a>》</label>
                            </p>
                            <a href="javascript:;" class="btn-pay">立即开通并支付</a>
                        </dd>
                    </dl>
                </form>
                <form>
                    <dl class="form">
                        <dt>付款银行：</dt>
                        <dd>
                            <div class="bank-info">
                                <img class="bank-icon" src="//www.mogujie.com/img/bank/0010.jpg" width="130" height="40"
                                     alt="">
                                <span class="type "></span>
                                <i class="icon-fastpay"></i>
                            </div>
                        </dd>

                        <dt>储蓄卡卡号：</dt>
                        <dd>
                            <span>6226193401666324</span>
                        </dd>

                        <dt>真实姓名：</dt>
                        <dd>
                            <span></span>
                            <input type="text" name="name" class="txt">
                        </dd>

                        <dt>身份证号：</dt>
                        <dd>
                            <span></span>
                            <input type="text" name="idCard" class="txt">
                        </dd>

                        <dt>留存手机号：</dt>
                        <dd>
                            <input type="text" name="phone" class="txt">
                        </dd>

                        <dt>短信验证码：</dt>
                        <dd>
                            <input type="text" name="smscode" class="txt txt-small">
                            <a href="javascript:;" class="btn-sms btn-sms-enable">发送短信</a>
                        </dd>

                        <dt></dt>
                        <dd>
                            <p class="notice"></p>
                            <p class="agree">
                                <input type="checkbox" name="" id="agree-1std" checked="checked">
                                <label for="agree-1std">我已阅读并同意《<a href="javascript:;"
                                                                   target="_blank">快捷支付用户协议</a>》</label>
                            </p>

                            <a href="javascript:;" class="btn-pay">立即开通并支付</a>
                        </dd>
                    </dl>
                </form>
            </div>

            <!-- 二次支付 -->
            <div class="pay-form fn-fastpay_2nd-detail">
                <form>
                    <dl class="form">
                        <dt>付款银行：</dt>
                        <dd>
                            <div class="bank-info">
                                <img class="bank-icon" src="//www.mogujie.com/img/bank/0010.jpg" width="130" height="40"
                                     alt="">
                                <span class="card-no">** 6324</span>
                                <span class="type"></span>
                                <i class="icon-fastpay"></i>
                            </div>
                        </dd>

                        <dt>手机号码：</dt>
                        <dd>183****5689</dd>

                        <dt>短信验证码：</dt>
                        <dd>
                            <input type="text" name="smscode" class="txt txt-small">
                            <a href="javascript:;" class="btn-sms btn-sms-enable">发送短信</a>
                        </dd>

                        <dt></dt>
                        <dd>
                            <p class="notice"></p>
                            <a href="javascript:;" class="btn-pay">完成支付</a>
                        </dd>
                    </dl>
                </form>
                `
            </div>
        </div>
    </div>
    <div class="pay-order-foot clearfix">
        <a class="btn-comfirm" href="javascript:;" id="confirm_pay">确认并付款</a>
        <div class="mon">
            <span>实付金额：</span>
            <span class="mon-num">
                <span>¥</span>
                <span>${order.amount}</span>
            </span>
        </div>
    </div>
</div>

<div style=" position: fixed; left:0; top:0; bottom:0; background: rgba(0,0,10,0.8); filter:alpha(opacity=80);  height: 100%; width: 100%; z-index:999999;display:none;"></div>
<!--end-->
<script type="text/javascript">
    /*-------点击事件--------*/
    $(document).ready(function () {
        $('.pay-tab .slide-tab').eq(0).click(function () {
            $(".pay-tab .slide-tab").eq(0).removeClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(0).css('display', 'block');
            $(".pay-tab .slide-tab").eq(1).addClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(1).css('display', 'none');
            $(".pay-tab .slide-tab").eq(2).addClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(2).css('display', 'none');
        });
        $('.pay-tab .slide-tab').eq(1).click(function () {
            $(".pay-tab .slide-tab").eq(1).removeClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(1).css('display', 'block');
            $(".pay-tab .slide-tab").eq(0).addClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(0).css('display', 'none');
            $(".pay-tab .slide-tab").eq(2).addClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(2).css('display', 'none');
        });
        $('.pay-tab .slide-tab').eq(2).click(function () {
            $(".pay-tab .slide-tab").eq(2).removeClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(2).css('display', 'block');
            $(".pay-tab .slide-tab").eq(0).addClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(0).css('display', 'none');
            $(".pay-tab .slide-tab").eq(1).addClass("pay-tab-close");
            $(".pay-tab .slide-tab .pay-tab-main").eq(1).css('display', 'none');
        });


        $('.pay-tab-hd .link').click(function () {
            $(".pay-tab .slide-tab").eq(0).css('display', 'block');
            $(".pay-tab .slide-tab").eq(1).css('display', 'block');
            $(".pay-tab .slide-tab").eq(2).css('display', 'block');
            $(".pay-order-foot").css('display', 'block');
            $(".pay-method").css('display', 'none');
            $("#fast_mark").text("快捷支付");
            $("#other_mark").text("其他支付");
        });
        $('.card-txt .btn-confirm').click(function () {
            $(".pay-tab .slide-tab").eq(0).css('display', 'none');
            $(".pay-tab .slide-tab").eq(1).css('display', 'none');
            $(".pay-tab .slide-tab").eq(2).css('display', 'none');
            $(".pay-order-foot").css('display', 'none');
            $(".pay-method").css('display', 'block');
            $(".pay-method .pay-tab-main").css('display', 'block');
            $(".pay-method .fn-paymethodLine").css('display', 'block');
            $(".pay-method .fn-fastpay_1st-detail").css('display', 'block');
        });
        $('#alipay').click(function () {
            $(".pay-tab .slide-tab").eq(0).css('display', 'none');
            $(".pay-tab .slide-tab").eq(1).css('display', 'none');
            $(".pay-tab .slide-tab").eq(2).css('display', 'none');
            $(".pay-order-foot").css('display', 'none');
            $(".pay-method").css('display', 'block');
            $(".pay-method .pay-tab-main").css('display', 'block');
            $(".pay-method .fn-paymethodLine").css('display', 'block');
            $(".pay-method .fn-paymethodLine").find("img").attr("src", "//s7.mogujie.com/pic/141105/ubzlo_ieydqy3fgq3winjvmqytambqhayde_130x40.jpg");
            $("[mark='mark']").text("支付宝扫码付款");
            $(".pay-method .fn-alipay-detail").css('display', 'block');
            $(".pay-method .fn-wechat-detail").css('display', 'none');
        });
        $('#wechat').click(function () {
            $(".pay-tab .slide-tab").eq(0).css('display', 'none');
            $(".pay-tab .slide-tab").eq(1).css('display', 'none');
            $(".pay-tab .slide-tab").eq(2).css('display', 'none');
            $(".pay-order-foot").css('display', 'none');
            $(".pay-method").css('display', 'block');
            $(".pay-method .pay-tab-main").css('display', 'block');
            $(".pay-method .fn-paymethodLine").css('display', 'block');
            $(".pay-method .fn-wechat-detail").css('display', 'block');
            $(".pay-method .fn-alipay-detail").css('display', 'none');
            $(".pay-method .fn-paymethodLine").find("img").attr("src", "//s7.mogujie.com/pic/141016/fanxiang_ieygentdg5swkzlfmmytambqgyyde_130x40.jpg");
            $("[mark='mark']").text("微信扫码付款");
        });
        $('#unionpay').click(function () {
            $(".pay-tab .slide-tab").eq(0).css('display', 'none');
            $(".pay-tab .slide-tab").eq(1).css('display', 'none');
            $(".pay-tab .slide-tab").eq(2).css('display', 'none');
            $(".pay-method").css('display', 'block');
            $(".pay-method .pay-tab-main").css('display', 'block');
            $(".pay-method .fn-paymethodLine").css('display', 'block');
            $(".pay-method .fn-alipay-detail").css('display', 'none');
            $(".pay-method .fn-wechat-detail").css('display', 'none');
            $(".pay-method .fn-paymethodLine").find("img").attr("src", "//s7.mogujie.com/pic/131219/o613k_kqzfct2ekrbewvsugfjeg5sckzsew_130x40.png");
            $("[mark='mark']").text("银联付款");
        });
    });
</script>
<!--标语start-->
[#include "b2c/include/slogen.ftl"]
<!--标语end-->
<!--底部start-->
[#include "/b2c/include/footer.ftl"]
<!--底部end-->
</body>
</html>