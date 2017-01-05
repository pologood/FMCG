<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${setting.siteName}-注册</title>
    <meta name="description" content="${setting.siteName}">
    <meta name="keywords" content="${setting.siteName}">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="${base}/resources/b2b/css/supplier.css">
    <link rel="stylesheet" href="${base}/resources/b2b/css/common.css">
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/supplier-index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/input.js"></script>
</head>
<body class="pc-bg">
<div class="top-content">
    <div class="wrap">欢迎来到${setting.siteName}系统平台</div>
</div>
<div class="am-g header-bg">
    <div class="am-container am-center" style="width:1106px;">
        <div class="am-fl">
            <img src="${base}/resources/b2b/images/PC-login_00.png" alt="${setting.siteName}-login" class="header-login0">
        </div>
        <ul class="am-fr nav-entrance">
            <li><a href="#" class="active">官方网站</a></li>
            <li><a href="#">手机客户端</a></li>
            <li><a href="#">加入收藏</a></li>
            <li><a href="#">帮助中心</a></li>
        </ul>
    </div>
</div>
<div class="am-g banner">
    <div class="am-container" style="width:1106px;">
        <div class="px am-fr">
            <div class="am-fl">
                    <span>
                        新用户注册
                    </span>
            </div>
            <div class="am-fr">
                    <span>
                         已有账号？ <a href="login.jhtml">马上登录</a>
                    </span>
            </div>
            <form id="registerForm" action="register.jhtml" method="post">
                <div class="normalInput">
                    <div class="normalInput-bg1 bg"></div>
                    <input id="mobile" name="mobile" type="text" maxlength="11" placeholder="请输入手机号" class="">
                </div>
                <div class="normalInput">
                    <div class="normalInput-bg3 bg"></div>
                    <input id="authcode" name="authcode" type="text" maxlength="10" placeholder="请输入验证码" class="">
                    <a href="javascript:;" id="getSecurityCode" class="button-validation am-fr">获取验证码</a>
                </div>
                <div class="rememberField">
                    <span class="checkboxPic check_chk" tabindex="-1" isshow="true">
                        <i class="i_icon"></i>
                    </span>
                    <label class="pointer">
                        我已阅读并同意《店家助手服务协议》
                    </label>
                </div>
                <a href="javascript:;" class="fullBtnBlue" onclick="$registerForm.submit();">注册</a>
            </form>
        </div>
    </div>

</div>
<div class="am-g" style="background-color:#fff;">
    <div class="am-container" style="width:1106px;">
        <ul class="th-convenient">
            <li style="margin-left:0;">
                <h1>门店体验</h1>
                <img src="${base}/resources/b2b/images/PC-login_10.png" alt="${setting.siteName}">
                <p>地理位置精准匹配</p>
                <p>商家活动一览无余</p>
            </li>
            <li>
                <h1>消费体验</h1>
                <img src="${base}/resources/b2b/images/PC-login_08.png" alt="${setting.siteName}">
                <p>便捷开放消费体验</p>
                <p>多样支付不用排队</p>
            </li>
            <li>
                <h1>社交体验</h1>
                <img src="${base}/resources/b2b/images/PC-login_11.png" alt="${setting.siteName}">
                <p>促销活动及时分享</p>
                <p>完美对接社区商圈</p>
            </li>
            <li style="margin-right:0;">
                <h1>服务体验</h1>
                <img src="${base}/resources/b2b/images/PC-login_12.png" alt="">
                <p>售前售后卓越享受</p>
                <p>精致营销提升粘性</p>
            </li>
        </ul>
    </div>
</div>
<div class="bt-content">
    <div class="wrap">
        <div class="top">
            <ul>
                <li><a href="#" class="active">公司介绍</a></li>
                <li><a href="#">产品中心</a></li>
                <li><a href="#">成功案例</a></li>
                <li><a href="#">招商合作</a></li>
                <li><a href="#">帮助中心</a></li>
                <li><a href="#">联系我们</a></li>
            </ul>
            <span>客服热线</span>
        </div>
        <ul class="left">
            <li>关注我们：</li>
            <li><img src="${base}/resources/b2b/images/xinl.png" alt=""></li>
            <li><img src="${base}/resources/b2b/images/wex.png" alt=""></li>
            <li><img src="${base}/resources/b2b/images/zccc.png" alt=""></li>
        </ul>
        <div class="right">
            <h3>0551-6769-8098</h3>
            <span>工作时间（周一至周五）   9：00-12：00，13：30-21：00</span>
        </div>
    </div>
</div>
[#include "/b2b/include/foote.ftl" /]
</body>
<script type="text/javascript">
    var $registerForm = $("#registerForm");
    $getSecurityCode=$("#getSecurityCode");

    $(function(){
        $registerForm.validate({
            submitHandler: function (form) {
                if($("#mobile").val().trim()==""||!/^1\d{10}$/.test($("#mobile").val().trim())){
                    $.message("error","请输入正确的手机号");
                    return;
                }
                if($("#authcode").val().trim()==""){
                    $.message("error","请输入验证码");
                    return;
                }
                if($(".check_chk").attr("isshow")=="true"){
                    $.message("error","未同意店家助手服务协议");
                    return;
                }
                $.ajax({
                    url: $registerForm.attr('action'),
                    type: "POST",
                    data:  $registerForm.serialize(),
                    dataType: "json",
                    success: function (data) {
                        $.message(data);
                        if(data.type=='success'){
                            window.setTimeout(function(){
                                window.location.href="login.jhtml";
                            },1000);
                        }
                    }
                });
            }
        });

        $getSecurityCode.click(function () {
            getAuthcode();
        });

    });


    function getAuthcode(){
        if($("#mobile").val().trim()==""){
            $.message("error","请先输入手机号");
            return;
        }
        $.ajax({
            type: 'post',
            url: 'send_security_code.jhtml',
            data: {mobile:$("#mobile").val()},
            dataType: 'json',
            success: function (data) {
                $.message(data);
                if (data.type == "success") {
                    var count = 60;
                    $getSecurityCode.unbind("click");
                    var t = window.setInterval(function () {
                        count--;
                        $getSecurityCode.text(count + "秒后重新获取");
                        if (count == 0) {
                            window.clearInterval(t);
                            $getSecurityCode.text("重新获取");
                            $getSecurityCode.click(function () {
                                getAuthcode();
                            });
                        }
                    }, 1000);
                }
            }
        });
    }

</script>
</html>
