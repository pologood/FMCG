<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.password.find")}[#if systemShowPowered][/#if]</title>
<link rel="shortcut icon" href="${base}/favicon.ico" />
<link href="${base}/resources/store/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $newpassword = $("#newpassword");
	var $confirmPwd = $("#confirmPwd");
	var $confirm = $("#confirm");

	$confirm.on('click',function(){
		if($newpassword.val()==''){
            $.message("warn", " 请填写新密码");
			return false;
		}
		if($newpassword.val()!=$confirmPwd.val()){
            $.message("warn", " 密码不一致");
			return false;
		}
		$.ajax({
			url:"${base}/store/password/reset.jhtml",
			data:{username:$("#username").val(),newpassword:$("#newpassword").val(),securityCode:${securityCode}},
			dataType:'json',
			type:'POST',
			success:function(data){
				if(data.type=='success'){
					$.message(data);
					setTimeout(function(){location.href = "${base}/store/member/index.jhtml"},1500);
					return false;
				}else{
					$.message(data);
				}
				return ;
			}

		});

	});

});
</script>
</head>

<body class="pc-bg">
    <div class="top-content">
        <div class="wrap">欢迎来到${setting.siteName}系统平台
        </div>
    </div>
    <div class="am-g header-bg">
        <div class="am-container am-center">
            <div class="am-fl">
                <img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}-login" width="160" height="88">
            </div>
            <ul class="am-fr nav-entrance">
                <li><a href="${base}/www/index.html" target="_blank" class="active">官方网站</a></li>
<!--                 <li><a href="http://www.tiaohuo.com/www/Product.html#2" target="_blank">手机客户端</a></li>
                <li><a href="javascript:shoucang('店家助手','${setting.siteUrl}')" id="setfavorite">加入收藏</a></li> -->
                <li><a href="${base}/store/article/list/65.jhtml" target="_blank">帮助中心</a></li>
            </ul>
        </div>
    </div>
    <div class="am-g banner">
        <div class="am-container" style="width:1106px;min-height:430px;">
            <div class="px am-fr" style="margin-top:35px;">
                <div class="am-fl">
                    <span>
                        重置密码
                    </span>
                </div>
                <div class="am-fr">
                    <span>
                         已有账号？ <a href="${base}/store/login.jhtml">马上登录</a>
                    </span>
                </div>
                <form id="passwordForm" action="reset.jhtml" method="post">
					<div class="normalInput">
                        <div class="bg normalInput-bg1"></div>
                        <input type="text" name="username" id="username" value="${member.username}" readonly='true'/>
                    </div>
                    <div class="normalInput">
                        <div class="bg normalInput-bg2"></div>
                        <input type="password" name="newpassword" id="newpassword" maxlength="11" placeholder="请输入新密码" class="">
                    </div>
					<div class="normalInput">
                        <div class="bg normalInput-bg2"></div>
                        <input type="password" name="confirmPwd" id="confirmPwd" maxlength="11" placeholder="请确认新密码" class="">
                    </div>
					<input type="button" class="fullBtnBlue" id="confirm" value="${message("shop.password.submit")}" style="margin-top:30px;" />
				</form>
            </div>
        </div>
    </div>
    <div class="am-g" style="background-color:#fff;">
        <div class="am-container" style="width:1106px;">
            <ul class="th-convenient">
                <li style="margin-left:0;">
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
                    <img src="${base}/update/PC-login_12.png" alt="">
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
                    <li><a href="${base}/www/index.html" target="_blank" class="active">公司介绍</a></li>
                    <li><a href="#">产品中心</a></li>
                    <li><a href="#">成功案例</a></li>
                    <li><a href="${base}/store/article/list/65.jhtml" target="_blank">招商合作</a></li>
                    <li><a href="${base}/store/article/list/65.jhtml" target="_blank">帮助中心</a></li>
                    <li><a href="${base}/store/article/list/65.jhtml" target="_blank">联系我们</a></li>
                </ul>
                <span>客服热线</span>
            </div>
            <ul class="left">
                <li>关注我们：</li>
                <li><img src="${base}/update/xinl.png" alt=""></li>
                <li><img src="${base}/update/wex.png" alt=""></li>
                <li><img src="${base}/upload/images/jdh_qr.jpg" alt=""></li>
            </ul>
            <div class="right">
                <h3>0551-6769-8098</h3>
                <span>工作时间（周一至周五）   9：00-12：00，13：30-21：00</span>
            </div>
        </div>
    </div>
    [#include "/store/include/footer.ftl"]
</body>

</html>
