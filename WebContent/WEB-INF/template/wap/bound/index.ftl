<!doctype html>
<html>
<head>
    [#include "/wap/include/resource.ftl"]
 <script src="${base}/resources/common/js/jquery.validate.js"></script>
<script src="${base}/resources/common/js/jquery-form.js"></script>   
<script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <title>绑定登录</title>
    <style>
        #wrapper {
        }
    </style>
    <script>
        $(function () {
           	setTimeout(function(){
             	  init();
            },2000);
            var $loginForm=$("#login-form");
            var $submit=$(":submit");
            var $password=$("#password");
            $loginForm.validate({
            	rules:{
            		username:"required",
            		password:"required"
            	},
            	messages:{
            		username:"请输入您的手机号",
            		password:"请输入您的密码"
            	},
            	submitHandler:function(){
            		$submit.prop("disabled",true);
					$.ajax({
						url: "${base}/common/public_key.jhtml",
						dataType: "json",
						cache: false,
						data : {local:true},
						type:"post",
						success: function(data) {
								var	modulus=data.modulus;
								var	exponent=data.exponent;
								var rsaKey = new RSAKey();
								rsaKey.setPublic(b64tohex(modulus), b64tohex(exponent));
								var password = hex2b64(rsaKey.encrypt($password.val()));
								$("#enPassword").val(password);
								$loginForm.ajaxSubmit(function(message){
									invokTips(message.type,message.content);	
									if(message.type == "success"){
										if($("#redirectUrl").val()!=""){
											location.href = $("#redirectUrl").val();
										}else{
											location.href = "${base}/wap/member/index.jhtml";
										}
									}
									$submit.prop("disabled",false);
									return false;
								});
						}
					});            		
            	}
            });

        });
    </script>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="${base}/wap/index.jhtml" class="">
            <i class="am-icon-home am-icon-md"></i> </a></div>
        <h1 class="am-header-title">绑定登录</h1>
        <div class="am-header-right am-header-nav"><a href="${base}/wap/register/index.jhtml" class="">
            <span>注册</span> </a></div>
      </header>
    </header>
    <div class="am-g">
        <div id="wrapper">
            <div>
                <div class="am-g">
                    <div class="am-panel">
                        <div class="am-panel-bd am-vertical-align" style="height: 135px;text-align: center;">
                            <img src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                                 class="am-img-responsive am-circle  am-vertical-align-middle"></img>
                        </div>
                    </div>
                </div>
                <div class="am-g">
                    <div class="am-padding-horizontal-sm">
                        <form method="post" class="am-form am-form-horizontal" id="login-form" action="${base}/wap/bound/save.jhtml">
                           	<input type="hidden" id="enPassword" name="enPassword">
                           	<input type="hidden" id="redirectUrl" value="${redirectUrl}">
                            <div class="am-form-group">
                                <div class="am-u-sm-12">
                                    <div class="am-form-icon">
                                     <i class="am-icon-mobile-phone"></i>
                                     <input type="text" name="username" id="username" class="am-form-field" placeholder="点击输入您的账号">
                                    </div>
                                </div>
                            </div>
                            <div class="am-form-group">
                                <div class="am-u-sm-12">
                                    <div class="am-form-icon"><i class="am-icon-lock"></i>
                                        <input type="password" name="password" id="password" class="am-form-field"
                                               placeholder="点击输入您的密码">
                                    </div>
                                </div>
                            </div>
                            <div class="am-form-group am-margin-bottom-sm">
                                <div class="am-u-sm-12">
                                    <input type="submit"  value="登 录"  class="am-btn-block am-btn am-btn-danger am-btn-sm">
                                </div>
                            </div>
                            <hr>
                            <!--
                            <div class="am-form-group">
                                <div class="am-u-sm-12">
                                    合作网站账号登录：
                                </div>
                                <div class="am-u-sm-12"><a href=""  class="am-btn am-btn-default am-btn-sm">
                                    <span class="am-icon-qq"></span>&nbsp;QQ号登录</a>
                                </div>
                            </div>
                            -->
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>