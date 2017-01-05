<!doctype html>
<html>
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <title>绑定登录</title>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">
    <div class="am_g">
        <div id="wrapper">
            <div>
            [#--<div class="am-g" style="height: 135px;text-align: center;padding-top: 2rem;">--]
            [#--<img src="${base}/resources/wap/2.0/images/logo.png"></img>--]
            [#--</div>--]
                <div class="weui_cells_title font-large" style="font-weight: bold;">请选择您的国家和地区并输入手机号</div>

                <div class="weui_cells weui_cells_access">
                    <div class="weui_cell ">
                        <div class="weui_cell_hd">
                            国家/地区
                        </div>
                        <div class="weui_cell_bd weui_cell_primary">
                        </div>
                        <div class="weui_cell_ft">中国</div>
                    </div>
                    <div class="weui_cell weui_cell_select weui_select_before">
                        <div class="weui_cell_hd">
                            <select class="weui_select" name="select2">
                                <option value="1">+86</option>
                                <option value="2">+80</option>
                                <option value="3">+84</option>
                                <option value="4">+87</option>
                            </select>
                        </div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input class="weui_input" id="phoneNumber" type="number" pattern="[0-9]*"
                                   placeholder="请输入号码">
                        </div>
                    </div>
                </div>

                <div class="weui_cells weui_cells_access">
                    <div class="weui_cell">
                        <div class="weui_cell_bd weui_cell_primary">
                            <input class="weui_input" id="codeNumber" type="number" placeholder="请输入验证码">
                        </div>
                        <div style="text-align: right;">
                            <a class="weui_btn weui_btn_primary" href="javascript:" id="getCode">获取验证码</a>
                            <a class="weui_btn weui_btn_primary" href="javascript:" id="getCodes"
                               style="display: none"></a>
                        </div>
                    </div>
                </div>

                <div class="weui_cells weui_cells_access">
                    <div class="weui_cell">
                        <div class="weui_cell_bd weui_cell_primary">
                            <input class="weui_input" id="inviteCode" type="text" maxlength="6" placeholder="若有邀请码，请输入邀请码">
                        </div>
                    [#--<div style="text-align: right;">--]
                    [#--<img id="captchaImage" class="captchaImage"--]
                    [#--src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">--]
                    [#--</div>--]
                    </div>
                </div>

                <div class="weui_cells weui_cells_checkbox" style="background-color:none;">
                    <label class="weui_cell weui_check_label font-small_1" for="s11">
                        <div class="weui_cell_hd">
                            <input type="checkbox" class="weui_check" name="checkbox1" id="s11" checked="checked">
                            <i class="weui_icon_checked"></i>
                        </div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>已阅读并同意<label style="color: red">《${setting.siteName}服务平台协议》</label></p>
                        </div>
                    </label>
                </div>
                <div class="weui_btn_area">
                    <a class="weui_btn weui_btn_primary" href="javascript:" id="showTooltips">登 录</a>
                </div>
            </div>
        </div>
    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        var $redirectUrl = '${redirectUrl}';
        $("#showTooltips").on("click", function () {
            var $phoneNumber = $("#phoneNumber").val();
            var $codeNumber = $("#codeNumber").val();
            var $inviteCode = $("#inviteCode").val();
            if ($phoneNumber == '' || $phoneNumber == null) {
                showToast2({content: "请先填写手机号码"});
                return false;
            }
            if ($codeNumber == '' || $codeNumber == null) {
                showToast2({content: "请先获取验证码"});
                return false;
            }

            $("#getCode").html("获取验证码");
            count = 60;
            clearInterval(ii);
            ajaxPost({
                url: '${base}/wap/bound/submit.jhtml',
                data: {
                    mobile: $phoneNumber,
                    securityCode: $codeNumber,
                    inviteCode:$inviteCode
                },
                success: function (message) {
                    if (message.type == 'success') {
                        if ($redirectUrl == '') {
                            location.href = "${base}/wap/member/index.jhtml";
                        } else if($redirectUrl == 'sjq') {
                            location.href = "${base}/wap/social_circles/index.jhtml";
                        }else{
                            location.href = "${redirectUrl}";
                        }
                    } else {
                        showToast2({content: message.content});
                    }
                }
            });
        });

        //获取手机验证码
        $("#getCode").on('click', function (e) {
            if(count!=60){
                return;
            }
            if ($("#phoneNumber").val().trim() == '') {
                showToast2({content: "请先填写手机号码"});
                return false;
            }
            if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($("#phoneNumber").val()))) {
                showToast2({content: "请确认您的号码是否正确"});
                return false;
            }

            ajaxPost({
                url: "${base}/wap/bound/getCheckCode.jhtml",
                data: {
                    mobile: $("#phoneNumber").val(),
                    captchaId:"${captchaId}",
                    captcha:$("#captcha").val()
                },
                success: function (data) {
                    if (data.type == 'success') {
                        showToast({content: data.content});
                        ii = setInterval(refreshTime, 1 * 1000);
                        $("#getCode").html(count + "秒后重新获取");
                    } else {
                        showToast2({content: data.content});
                        $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
                    }
                }
            });

        });

        $("#captchaImage,#changeCaptcha").click(function () {
            $("#captchaImage").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
        });
    });

    var count = 60,ii;
    function refreshTime() {
        count = count - 1;
        if (count == 0) {
            count = 60;
            $("#getCode").html("获取验证码");
            clearInterval(ii);
            return false;
        }
        $("#getCode").html(count + "秒后重新获取");
    }
</script>
</body>
</html>