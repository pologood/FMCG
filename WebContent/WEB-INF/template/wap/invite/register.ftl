<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>我要开店</title>
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
    <!-- Tile icon for Win8 (144x144 + tile color) -->
    <link rel="stylesheet" href="${base}/resources/wap/css/amazeui.css">
    <link rel="stylesheet" href="${base}/resources/wap/css/common.css">
    <script src="${base}/resources/wap/js/jquery-1.9.1.min.js"></script>
    <script src="${base}/resources/wap/js/amazeui.min.js"></script>
    <script src="${base}/resources/wap/js/flexible_css.debug.js"></script>
    <script src="${base}/resources/wap/js/flexible.debug.js"></script>

    <script type="text/javascript">
        var InterValObj; //timer变量，控制时间
        var count = 60; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        var code = ""; //验证码
        var codeLength = 6;//验证码长度

        $().ready(function () {
            var $btnSendCode = $("#btnSendCode");
            var $mobile = $("#mobile");
            var $complete = $("#complete");
            var $captcha = $("#captcha");

            var _clickCode = '0';

            $btnSendCode.click(function () {
                if ($mobile.val().trim() == '') {
                    invokTips("error", "请先填写手机号码");
                    return false;
                }
                if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($mobile.val()))) {
                    invokTips("error", "请确认您的号码是否正确");
                    return false;
                }

                $.ajax({
                    url: "${base}/wap/invite/send_mobile.jhtml",
                    type: "post",
                    data: {mobile: $mobile.val()},
                    dataType: "json",
                    success: function (message) {
                        invokTips(message.type, message.content);
                        _clickCode = '1' ;
                        if (message.type == "success") {
                            sendMessage();
                        }
                    }
                });
            });
            $complete.click(function () {
                if ($mobile.val().trim() == '') {
                    invokTips("error", "请先填写手机号码");
                    return false;
                }
                if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($mobile.val()))) {
                    invokTips("error", "请确认您的号码是否正确");
                    return false;
                }

                if ($captcha.val().trim() == '') {
                    invokTips("error", "请先填写验证码");
                    return false;
                }

                $.ajax({
                    url: "${base}/wap/invite/save.jhtml",
                    type: "post",
                    data: {captcha: $captcha.val(), mobile: $mobile.val()},
                    dataType: "json",
                    success: function (message) {
                        invokTips(message.type, message.content);

                        if(_clickCode == '1'){
                            window.clearInterval(InterValObj);//停止计时器
                            $("#btnSendCode").removeAttr("disabled");//启用按钮
                            $("#btnSendCode").val("重新发送验证码");
                        }

                        if (message.type == "success") {
                            setTimeout(function () {
                                location.href = "${base}/wap/invite/download.jhtml";
                            }, 1000);
                        }
                    }
                });
            });
        });
        function invokTips(type, content) {
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

        function sendMessage() {
            curCount = count;
            //产生验证码
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

</head>
<body class="open_shop_f0eff4">
<div class="am-g">
    <div id="inok" class="inok"></div>
    <div>
        <div class="am-g">
            <img src="${base}/resources/wap/img/kaidian.png" alt="邀请开店" class="open_shop_bg">
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
                <input type="button" class="am_input_complete color_fff" value="完成" id="complete">
                <div class="am-container">
                    <ul class="am-list am_kaidian_li_style">
                        <li>零成本开店，原来做买手这么简单</li>
                        <li>一键发展会员，分润实时到帐</li>
                        <li>终身享受收益，轻松赚取零花钱</li>
                    </ul>
                </div>

            </div>
        </div>
    </div>
</body>
</html>
