<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.password.find")}[#if systemShowPowered][/#if]</title>
<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $passwordForm = $("#passwordForm");
	var $username = $("#username");
	
	var $captcha = $("#captcha");
	var $captchaImage = $("#captchaImage");
	var $mobile = $("#mobile");
	var $email = $("#email");
	
	var ii;
	var count =60;
	var $submit = $(":submit");
	
	var $confirm = $("#confirm");
	var $checkCode = $("#checkCode");
	var $getCode = $("#getCode");
	var $span_1 = $("#span_1");
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	
	//获取验证码
	$getCode.on('click',function(e){
		if($username.val()==''){
            $.message("error", " 请先填写手机号"); return false;
		}
		
		//获取手机验证码
		$.ajax({
			url :"${base}/helper/password/send.jhtml",
			data:{username:$username.val(),type:'mobile'},
			dataType:"json",
			type:"post",
			success:function(data){
				if(data.type=='success'){
					$getCode.attr('style','display:none;');
					$span_1.attr('style','display:block;');
					ii=setInterval(refreshTime,1*1000);
					return ;
				}else{
                    $.message("error"," "+data.content);
				}
			}
		});
			
		function refreshTime(){
			count=count-1;
			if(count==0){
				count=60;
				$getCode.attr('style','display:block');
				$span_1.attr('style','display:none');
				clearInterval(ii);
				return false;
			}
			$("#span_1").html(count+"秒后重新获取");
			
		}
		
	});
	$confirm.on('click',function(){
		if($username.val()==''){
            $.message("error", " 请先填写手机号"); return false;
		}
		if($checkCode.val()==''){
            $.message("error", " 请先填写验证码"); return false;
		}
		$.ajax({
			url:"${base}/helper/password/check.jhtml",
			data:{username:$username.val(),securityCode:$checkCode.val()},
			dataType:'json',
			type:'POST',
			success:function(data){
				if(data.type=='success'){
					location.href="${base}/helper/password/reset.jhtml?username="+$username.val()+"&securityCode="+$checkCode.val();
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
    <div class="am-g header-bg">
        <div class="am-container am-center">
            <div class="am-fl">
                <img src="${base}/resources/helper/img/PC-login_00.png" alt="${setting.siteName}-login" class="header-login">
            </div>
            <ul class="am-fr nav-entrance">
                <li><a href="#">官方网站</a></li>
                <li><a href="#">卖家入口</a></li>
                <li><a href="#">手机客户端</a></li>
                <li><a href="#">加入收藏</a></li>
                <li><a href="#">帮助中心</a></li>
            </ul>
        </div>
    </div>
    <div class="am-g banner">
        <div class="am-container">
            <div class="px am-fr">
                <div class="am-fl">
                    <span>
                        ${message("shop.password.find")}
                    </span>
                </div>
                <div class="am-fr">
                    <span>
                         已有账号？ <a href="${base}/helper/login.jhtml">马上登录</a>
                    </span>
                </div>
                <form id="passwordForm" action="find.jhtml" method="post">
                
                	<div class="normalInput">
                        <input type="text" name="username" id="username" maxlength="11" placeholder="请输入手机号" class="normalInput-bg1">
                    </div>
                	<div class="normalInput">
                        <input type="text" id="checkCode" name="checkCode" maxlength="18" placeholder="请输入验证码" class="normalInput-bg3" maxlength="10">                         
						<span id="getCode" class="w_registerget"><a href="javascript:;" class="button-validation am-fr">获取验证码</a></span>
						<div id="span_1" style="display:none" class="w_timing">60秒后重新获取</div>                        
                    </div>
                    
                    <div class="rememberField">
                         &nbsp;
                    </div>
                	<input type="button" class="fullBtnBlue" id="confirm" value="下一步" />
				</form>
            </div>
        </div>

    </div>
    <div class="am-g">
        <div class="am-container">
            <ul class="th-convenient">
                <li>
                    <img src="${base}/resources/helper/img/PC-login_08.png" alt="${setting.siteName}">
                    <h2>门店体验</h2>
                    <p>地理位置精准匹配</p>
                    <p>商家活动一览无余</p>
                </li>
                <div class="th-convenient-bg"></div>
                <li>
                    <img src="${base}/resources/helper/img/PC-login_10.png" alt="${setting.siteName}">
                    <h2>消费体验</h2>
                    <p>便捷开放消费体验</p>
                    <p>多样支付不用排队</p>
                </li>
                <div class="th-convenient-bg"></div>
                <li>
                    <img src="${base}/resources/helper/img/PC-login_11.png" alt="${setting.siteName}">
                    <h2>社交体验</h2>
                    <p>促销活动及时分享</p>
                    <p>完美对接社区商圈</p>
                </li>
                <div class="th-convenient-bg"></div>
                <li>
                    <img src="${base}/resources/helper/img/PC-login_12.png" alt="">
                    <h2>服务体验</h2>
                    <p>售前售后卓越享受</p>
                    <p>精致营销提升粘性</p>
                </li>
            </ul>
            <div class="am-container th-convenient-bt-bg">
            </div>
            <div class="am-container">
                <div class="am-fl Contact-us">
                    <h2>联系我们</h2>
                    <p>在线时间：周一至周五，早9点到晚9点</p>
                    <h3><img src="${base}/resources/helper/img/PC-login_31.png" alt="">0551-6769-8098</h3>
                </div>
                <ul class="am-fr Contact-us-fr">
                    <li>
                        <a href="#">
                            <img src="${base}/resources/helper/img/PC-login_25.png" alt=""> </a>
                    </li>
                    <li>
                        <a href="#">
                            <img src="${base}/resources/helper/img/PC-login_24.png" alt=""> </a>
                    </li>
                    <li>
                        <a href="#">
                            <img src="${base}/resources/helper/img/PC-login_23.png" alt=""> </a>
                    </li>
                    <li>
                        <a href="#">
                            <img src="${base}/resources/helper/img/PC-login_26.png" alt=""> </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div id="footer">
        <ul>
            <li>
                安徽省合肥市瑶海区站前路中绿广场写字楼17层
                &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="http://www.tiaohuo.com">网址：www.tiaohuo.com</a>
                &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="#">关于我们</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="#">联系我们</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="#">用户帮助</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="#">${setting.siteName}联盟</a>
            </li>
            <li class="text">
                CopyRight © 2010-2016 <a href="${base}/www/index.html">${setting.siteName}</a> All Rights Reserved
                &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;      ${setting.certtext}
                &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;       技术支持：安徽威思通电子商务有限公司
            </li>
        </ul>
    </div>
</body>


</html>