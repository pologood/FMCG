<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${setting.siteName}-找回密码</title>
    <meta name="description" content="${setting.siteName}">
    <meta name="keywords" content="${setting.siteName}">
    <link rel="stylesheet" href="${base}/resources/b2c/css/supplier.css">
    <link rel="stylesheet" href="${base}/resources/b2c/css/amazeui.css">
    <link rel="stylesheet" href="${base}/resources/b2c/css/common.css">

    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/supplier-index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
</head>
<body class="pc-bg">
<div class="top-content">
    <div class="wrap">欢迎来到${setting.siteName}系统平台</div>
</div>
<div class="am-g header-bg">
    <div class="am-container am-center" style="width:1106px;">
        <div class="am-fl">
            <img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}-login" class="header-login0">
        </div>
        <ul class="am-fr nav-entrance">
            <li><a href="${base}/www/index.html" target="_blank" class="active">官方网站</a></li>
<!--                 <li><a href="http://www.tiaohuo.com/www/Product.html#2" target="_blank">手机客户端</a></li>
            <li><a href="javascript:shoucang('店家助手','${setting.siteUrl}')" id="setfavorite">加入收藏</a></li> -->
            <li><a href="${base}/helper/article/list/65.jhtml" target="_blank">帮助中心</a></li>
        </ul>
    </div>
</div>
<div class="am-g banner">
    <div class="am-container">
        <div class="px am-fr" style="margin-top:35px;">
            <div class="am-fl">
                    <span>
                        重置密码
                    </span>
            </div>
            <div class="am-fr">
                    <span>
                         已有账号？ <a href="login.jhtml">马上登录</a>
                    </span>
            </div>
            <form id="registerForm" action="" method="post">
                <div class="normalInput">
                    <input id="mobile" name="mobile" value="${mobile}" readonly type="text" maxlength="11" placeholder="请输入手机号" class="normalInput-bg1">
                </div>
                <div class="normalInput">
                    <input id="password" name="password" type="password" maxlength="11" placeholder="请输入密码" class="normalInput-bg3">
                </div>
                <div class="normalInput">
                    <input id="repassword" name="repassword" type="password" maxlength="11" placeholder="请输入确认密码" class="normalInput-bg3">
                </div>
                <a href="javascript:;" class="fullBtnBlue" onclick="$registerForm.submit();">提交</a>
            </form>
        </div>
    </div>

</div>
<div class="am-g" style="background-color:#fff;">
    <div class="am-container" style="width:1106px;">
        <ul class="th-convenient">
            <li>
                <h1>门店体验</h1>
                <img src="${base}/update/PC-login_10.png" alt="${setting.siteName}">
                <p>地理位置精准匹配</p>
                <p>商家活动一览无余</p>
            </li>
            <li>
                <h1>消费体验</h1>
                <img src="${base}/update/PC-login_08.png" alt="${setting.siteName}">
                <p>便捷开放消费体验</p>
                <p>多样支付不用排队</p>
            </li>
            <li>
                <h1>社交体验</h1>
                <img src="${base}/update/PC-login_11.png" alt="${setting.siteName}">
                <p>促销活动及时分享</p>
                <p>完美对接社区商圈</p>
            </li>
            <li style="margin-right:0;">
                <h1>服务体验</h1>
                <img src="${base}/update/PC-login_12.png" alt="${setting.siteName}">
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
                <li><a href="${base}/www/about_us.html" target="_blank" >关于我们</a></li>
                <li><a href="${base}/www/Product.html" target="_blank">产品中心</a></li>
                <li><a href="${base}/www/case.html" target="_blank">成功案例</a></li>
                <li><a href="${base}/www/news.html" target="_blank">资讯动态</a></li>
                <li><a href="${base}/www/Cooperation.html" target="_blank">招商合作</a></li>
            </ul>
            <span>客服热线</span>
        </div>
        <ul class="left">
            <li>关注我们：</li>
            <li><img src="${base}/upload/images/jdh_qr.jpg" alt=""></li>
        </ul>
        <div class="right">
            <h3>0551-6769-8098</h3>
            <span>工作时间（周一至周五）   9：00-12：00，13：30-21：00</span>
        </div>
    </div>
</div>
[#include "/b2c/include/foote.ftl" /]
</body>
<script type="text/javascript">
    var $registerForm = $("#registerForm");
    $getSecurityCode=$("#getSecurityCode");

    $(function(){
        $registerForm.validate({
            submitHandler: function (form) {
                if($("#password").val().trim()==""){
                    $.message("error","请输入密码");
                    return;
                }
                if($("#repassword").val().trim()==""){
                    $.message("error","请输入确认密码");
                    return;
                }
                if($("#password").val().trim()!=$("#repassword").val().trim()){
                    $.message("error","两次输入密码不一致");
                    return;
                }
                $.ajax({
                    url: "${base}/common/public_key.jhtml",
                    type: "POST",
                    data : {local:true},
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        var rsaKey = new RSAKey();
                        rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                        var enPassword = hex2b64(rsaKey.encrypt($("#password").val()));
                        $.ajax({
                            url: 'resetPassword.jhtml',
                            type: "POST",
                            data:  {
                                mobile:$("#mobile").val(),
                                enPassword:enPassword
                            },
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
            data: {
                mobile:$("#mobile").val(),
                type:"resetPwd"
            },
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

    // 加入收藏 兼容360和IE6
    function shoucang(sTitle,sURL)
    {
        try
        {
            window.external.addFavorite(sURL,sTitle);
        }
        catch (e)
        {
            try
            {
                window.sidebar.addPanel(sTitle, sURL, "");
            }
            catch (e)
            {
                alert("加入收藏失败，请使用Ctrl+D进行添加");
            }
        }
    }

</script>
</html>
