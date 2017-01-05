<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <title>设置支付密码</title>

    <style type="text/css">
        body {
            font-family: "Microsoft YaHei";
            background-color: #E9E9E9;
        }

        .weui_cells:before {
            border-top: 0px;
            left: 0px;
        }

        .weui_cells:after {
            border-bottom: 0px;
        }
    </style>

    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>

    <script type="text/javascript">
        var InterValObj; //timer变量，控制时间
        var count = 60; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        function sendMessage() {
            curCount = count;
            //设置button效果，开始计时
            $("#btnSendCode").attr("disabled", "true");
            $("#captcha").val("消息已发送请" + curCount + "秒再获取");
            InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
        }
        //timer处理函数
        function SetRemainTime() {
            if (curCount == 0) {
                window.clearInterval(InterValObj);//停止计时器
                $("#btnSendCode").removeAttr("disabled");//启用按钮
                $("#captcha").text("请获取验证码");
            } else {
                curCount--;
                $("#captcha").text("消息已发送请" + curCount + "秒后再获取");
            }
        }
    </script>

</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page" style="background-color:#E9E9E9;">
        <div class="weui_cells weui_cells_form">
            <div class="weui_cell">
                <div class="weui_cell_hd"><label class="weui_label" style="width:70px;">验证码</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input class="weui_input" type="tel" placeholder="请输入验证码" style="font-family:'Microsoft YaHei';"
                           id="securityCode" name="securityCode">
                </div>
            </div>
            <a class="weui_cell">
                <div class="weui_cell_hd">
                    <i class="iconfont" id="qr_img_close">&#xe64a;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p id="captcha" style="color:#454545;font-size:15px;">请获取验证码</p>
                </div>
            </a>
        </div>

        <div class="weui_cells weui_cells_form" style="padding-top:20px;padding-bottom:20px;">
            <a href="javascript:;" class="weui_btn weui_btn_default" style="width:80%;" id="btnSendCode">重新获取验证码</a>
        </div>

        <div class="weui_cells weui_cells_form">
            <div class="weui_cell">
                <div class="weui_cell_hd"><label class="weui_label" style="width:70px;">新密码</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input class="weui_input" type="password" maxlength="6" placeholder="请输入新密码"
                           style="font-family:'Microsoft YaHei';" id="newPassword" name="newPassword">
                </div>
            </div>
            <div class="weui_cell ">
                <div class="weui_cell_hd"><label class="weui_label" style="width:70px;">确认密码</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input class="weui_input" type="password" maxlength="6" placeholder="请输入确认密码"
                           style="font-family:'Microsoft YaHei';" id="reNewPassword" name="reNewPassword">
                </div>
            </div>
        </div>

        <div class="weui_cells weui_cells_form" style="padding-top:20px;padding-bottom:20px;">
            <a href="javascript:;" class="weui_btn weui_btn_primary" style="width:80%;" id="submit_code">提交</a>
        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        ajaxGet({
            url: "${base}/app/member/view.jhtml",
            success: function (data) {

                ajaxPost({
                    url: "${base}/app/member/send_mobile.jhtml",
                    data: {username: data.data.username},
                    success: function (data2) {
                        if (data2.message.type == "success") {
                            sendMessage();
                            showToast(data2.message);
                        }
                    }
                });

                $("#btnSendCode").on("click", function () {
                    if ($("#captcha").text() == "请获取验证码") {
                        ajaxPost({
                            url: "${base}/app/member/send_mobile.jhtml",
                            data: {username: data.data.username},
                            success: function (data3) {
                                if (data3.message.type == "success") {
                                    sendMessage();
                                    showToast(data3.message);
                                }
                            }
                        });
                    }
                });

                $("#submit_code").on("click", function () {
                    var _securityCode = $("#securityCode").val().trim();
                    var _newPassword = $("#newPassword").val().trim();
                    var _reNewPassword = $("#reNewPassword").val().trim();

                    if (_securityCode == "" || _securityCode == null) {
                        showToast2({content: "请输入获取到的验证码！"});
                        return;
                    }

                    if (_newPassword.length != 6) {
                        showToast2({content: "支付密码只能是六位数字密码，请重新确认！"});
                        return;
                    }

                    if (_reNewPassword.length != 6) {
                        showToast2({content: "支付密码只能是六位数字密码，请重新确认！"});
                        return;
                    }

                    if (_newPassword != _reNewPassword) {
                        showToast2({content: "两次密码不一致，请重新确认！"});
                        return;
                    }

                    $.ajax({
                        url: "${base}/common/public_key.jhtml",
                        type: "POST",
                        data: {local: true},
                        dataType: "json",
                        cache: false,
                        success: function (data4) {
                            var rsaKey = new RSAKey();
                            rsaKey.setPublic(b64tohex(data4.modulus), b64tohex(data4.exponent));
                            ajaxPost({
                                url: "${base}/wap/member/updatePaymentPassword.jhtml",
                                data: {
                                    newPaymentPassword: hex2b64(rsaKey.encrypt(_newPassword)),
                                    reNewPaymentPassword: hex2b64(rsaKey.encrypt(_reNewPassword)),
                                    securityCode: _securityCode
                                },
                                success: function (message) {
                                    if (message.type == "success") {
                                        showToast(message);
                                        location.href = "${base}/wap/member/set_info.jhtml"
                                    } else {
                                        showToast2(message);
                                    }

                                }
                            });
                        }
                    });
                });
            }
        });

    });
</script>
[#include "/wap/include/footer.ftl" /]
</body>
</html>
