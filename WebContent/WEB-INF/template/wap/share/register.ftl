<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>邀请注册</title>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/main.css">
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="wrap RDD">
        <!-- bannerR-->
        <div class="bannerR">
            <img src="${base}/resources/wap/2.0/images/invite-register-banner_bg01.jpg" alt="">
            <!-- bannerR-ctn-->
            <div class="bannerR-ctn">
                <p class="txt">马上成为[#if tenant??&&tenant?has_content]本店[#else ]${setting.siteName}[/#if]会员！</p>
            </div>
            <!-- storeinfo-->
            <div class="storeinfo">
                <div class="pt1">
                    <img class="lazy" src="${base}/resources/wap/2.0/images/logo.png"
                         [#if tenant??&&tenant?has_content]
                         data-original="${tenant.thumbnail}"
                         [/#if]>
                </div>
                <span class="pt2">
                [#if tenant??&&tenant?has_content]${tenant.name}[#else ]${setting.siteName}[/#if]
                </span>
            </div>
        </div>
        <!-- operaR-->
        <div class="operaR" style="background: white;">
            <div class="inputS">
                <i class="icon"><img src="${base}/resources/wap/2.0/images/icon-user.png" alt=""></i>
                <input type="text" id="registerPhone" placeholder="请输入手机号码">
            </div>
            <div class="inputS">
                <i class="icon"><img src="${base}/resources/wap/2.0/images/icon-lock.png" alt=""></i>
                <input type="text" id="registerCode" placeholder="请输入验证码">
                <a href="javascript:getCode();" class="getVcode">获取验证码</a>
            </div>
            <div class="btnT">
                <a href="javascript:register();" ontouchstart="" class="btn-ok">完成</a>
            </div>
        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/logo.png'});
    });

    var count = 60, ii;
    function refreshTime() {
        count = count - 1;
        if (count == 0) {
            $(".getVcode").html("获取验证码");
            count = 60;
            clearInterval(ii);
            return false;
        }
        $(".getVcode").html(count + "秒后重新获取");
    }

    function getCode() {
        if (count != 60) {
            return;
        }
        var _mobile = $("#registerPhone").val().trim();

        if ($.trim(_mobile) == '') {
            showToast2({content: "请先填写手机号码"});
            return false;
        }
        if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test(_mobile))) {
            showToast2({content: "请确认您的号码是否正确"});
            return false;
        }

        ii = setInterval(refreshTime, 1 * 1000);
        $(".getVcode").html(count + "秒后重新获取");
        ajaxPost({
            url: "${base}/wap/share/send_mobile.jhtml",
            data: {mobile: _mobile},
            success: function (data) {
                if (data.type == 'success') {
                    showToast({content: data.content});
                } else {
                    showToast2({content: data.content});
                }
            }
        });
    }

    function register() {
        var _mobile = $("#registerPhone").val().trim();
        var _code = $("#registerCode").val().trim();

        if (_mobile == '' || _mobile == null) {
            showToast2({content: "请先填写手机号码"});
            return false;
        }
        if (_code == '' || _code == null) {
            showToast2({content: "请先获取验证码"});
            return false;
        }

        $(".getVcode").html("获取验证码");
        count = 60;
        clearInterval(ii);

        ajaxPost({
            url: "${base}/wap/share/save.jhtml",
            data: {
                mobile: _mobile,
                captcha: _code
            },
            success: function (data) {
                if (data.type == 'success') {
                    showToast({content: data.content});
                    location.href = "${base}/wap/share/index.jhtml?extension=${extension}";
                } else {
                    showToast2({content: data.content});
                }
            }
        });
    }
</script>
</body>
</html>