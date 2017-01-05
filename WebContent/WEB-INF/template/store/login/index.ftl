<!DOCTYPE html>
<html itemscope="" itemtype="http://schema.org/WebPage" lang="zh" xmlns:fb="https://www.facebook.com/2008/fbml" xmlns:og="http://ogp.me/ns#" xmlns="http://www.w3.org/1999/xhtml" class="">
<head>
     <meta name="baidu-site-verification" content="ceEugdnrem" />
  [@seo type = "index"]
      <title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.index.title")}[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]" />
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]" />
    [/#if]
  [/@seo]
      <!--<title>${setting.siteName}</title>-->
  <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta content="width=device-width,initial-scale=1.0,user-scalable=yes,minimum-scale=1.0,maximum-scale=3.0" id="viewport" name="viewport">

  <!-- <link rel="stylesheet" href="${base}/resources/store/2.0/bootstrap/css/bootstrap.min.css"> -->
  <link rel="stylesheet" href="${base}/store/css_indexpc/_reset-3883aa74e9ad592d0b5195d748f79e0b.css"/>
  <link rel="stylesheet" href="${base}/store/css_indexpc/main_v4-4a981e858e183200ec06c7591f64eb15.css"/>
  <link rel="stylesheet" href="${base}/store/css_indexpc/pc_index_sr.css"/>
  <link rel="stylesheet" href="${base}/resources/store/css/style.css">
  <link rel="stylesheet" href="${base}/resources/store/css/common.css">
  <!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css"> -->
  <link rel="stylesheet" href="${base}/resources/store/2.0/awesome-font/css/font-awesome.css">
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript">
      var _TH_ = {};
      _TH_.siteName="${setting.siteName}";
      _TH_.siteCompany="${(setting.siteCompany)!}";
      _TH_.address="${setting.address}";
      _TH_.phone="${setting.phone}";
      _TH_.email="${setting.email}";
      _TH_.certtext="${setting.certtext}";
      _TH_.cooperativePartner1=[#if setting.siteName=="直通邦"]"ABC KIDS"[#else ]"聚德汇"[/#if];
      _TH_.cooperativePartner2=[#if setting.siteName=="直通邦"]"特步"[#else ]"安培网"[/#if];
  </script>
  <script type="text/javascript" src="${base}/resources/store/js/index_data.js"></script>
  <script type="text/javascript" src="${base}/store/js_indexpc/i18n-2ad05c23c5a1485c8c77361d3e9e93ab.js"></script>
  <script type="text/javascript" src="${base}/store/js_indexpc/v4/page-site-bundle-bb4805139779693d12a7aceaec30b8b9.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/jsbn.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/prng4.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/rng.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/rsa.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/base64.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/index_data2.js"></script>
  <script type="text/javascript" src="${base}/resources/b2b/js/register/page_regist.js"></script>
  <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>

  <noscript>
    &lt;style &gt;.s-entrance-mask .s-section .container { opacity: 1 !important; }
    &lt;/style &gt;
  </noscript>

  <script>
    (function() {
      window.$B || (window.$B = {});
    }
    ).call(this);
  </script>
</head>
<body class='show locale-zh-CN' id='user_sites'>
  <!--login-->
  <div id="login_dialog">
    <!-- account login -->
    <form  id="account_login" method="post">
      <div class="login-title">
        <div class="title-info">
          <span class="active" onclick="login_method('account',this)">账号登录</span>
          <span onclick="login_method('phone',this)">手机登录</span>
        </div>

      </div>
      <ul class="login-box">
        <li>
          <input type="text" name="username" id="account_username" class="account" maxlength="11" style="color:#999;" placeholder="请输入账号"/>
          <span class="errors error5"></span>
        </li>
        <li>
          <input type="password" name="password" id="account_password" class = "admin_pwd" style="color:#999;" placeholder="请输入密码"/>
          <span class="errors error6"></span>
        </li>
      </ul>
      <div class="forgot-pwd">
        <a href="javascript:;" onclick="login_method('forget')">忘记密码？</a>
      </div>
      <div class="sub">
        <input type="button" value="登录" id="account_login_btn" onclick="account_submit()" />
      </div>
      <div class="login-footer">
        <div class="link"></div>
        <div class="title">
          <span>还没有${setting.siteName}账号？</span>
          <a href="javascript:SongRan_reg();">现在注册！</a>
        </div>
      </div>
    </form>
    <!-- phone login -->
    <form action="javascript:;" style="display: none;"  id="phone_login">
      <div class="login-title">
        <div class="title-info">
          <span onclick="login_method('account',this)">账号登录</span>
          <span class="active" onclick="login_method('phone',this)">手机登录</span>
        </div>

      </div>
      <ul class="login-box">
        <li>
          <input type="text" name="phone" class="phone" id="login_phone" maxlength="11" style="color:#999;" placeholder="请输入手机号"/>
          <span class="errors error1"></span>
        </li>
        <li>
          <input type="text" name="password" class="phonekey" id="login_captcha" style="color:#999;" placeholder="请输入验证码"/>
          <div class="get-code" onclick="get_code('phone_login')"><span class="get-btn">获取验证码</span></div>
          <span class="errors error2"></span>
        </li>
      </ul>
      <div class="forgot-pwd">
        <a href="javascript:;" onclick="login_method('forget')">忘记密码？</a>
      </div>
      <div class="sub">
        <input type="submit" value="登录" onclick="phone_submit()" id="phone_login_btn" />
      </div>
      <div class="login-footer">
        <div class="link"></div>
        <div class="title">
          <span>还没有${setting.siteName}账号？</span>
          <a href="javascript:SongRan_reg();">现在注册！</a>
        </div>
      </div>
    </form>
    <!-- Forgot Password -->
    <form action="" style="display: none;" id="forget_password">
      <div class="login-title">
        <div class="title-info">
          <span class="active">找回密码</span>
          <span>重置密码</span>
        </div>

      </div>
      <ul class="login-box">
        <li>
          <input type="text" name="username" id="forget_username" class="phone" maxlength="11" style="color:#999;" placeholder="请输入手机号"/>
          <span class="errors error1"></span>
        </li>
        <li>
          <input type="text" class="phonekey" id="forget_img_captcha" style="color:#999;" placeholder="请输入图片验证码"/>
          <div class="get-code"><span style="border-left:none;padding-right:0px;"><img src="" id="forget_captcha_img" style="height:35px;"></span></div>
          <span class="errors error2"></span>
        </li>
        <li>
          <input type="text" class="phonekey" id="forget_phone_captcha" style="color:#999;" placeholder="请输入手机验证码"/>
          <div class="get-code" onclick="get_code('forget_password')"><span class="get-btn">获取验证码</span></div>
          <span class="errors error2"></span>
        </li>
      </ul>
      <div class="forgot-pwd">
        <a href="javascript:;" onclick="login_method('account',this)">返回登录</a>
      </div>
      <div class="sub">
        <input type="button" onclick="go_reset()" value="下一步" />
      </div>
      <div class="login-footer">
        <div class="link"></div>
        <div class="title">
          <span>还没有${setting.siteName}账号？</span>
          <a href="javascript:SongRan_reg();">现在注册！</a>
        </div>
      </div>
    </form>
    <!-- Reset Password -->
    <form action="" style="display: none;" id="reset_password">
      <div class="login-title">
        <div class="title-info">
          <span>找回密码</span>
          <span class="active">重置密码</span>
        </div>

      </div>
      <ul class="login-box">
        <li>
          <input type="password" class="password" id="new_password" placeholder="请输入新密码" style="color:#999;"/>
          <span class="errors error3"></span>
        </li>
        <li>
          <input type="password" class="passwordTwo" id="re_new_password" placeholder="请再次输入新密码" style="color:#999;"/>
          <span class="errors error4"></span>
        </li>
      </ul>
      <div class="forgot-pwd">
        <a href="javascript:;" onclick="login_method('account',this)">返回登录</a>
      </div>
      <div class="sub">
        <input type="button" value="提交" onclick="reset_password()" />
      </div>
      <div class="login-footer">
        <div class="link"></div>
        <div class="title">
          <span>还没有${setting.siteName}账号？</span>
          <a href="javascript:SongRan_reg();">现在注册！</a>
        </div>
      </div>
    </form>

  </div>
  <!--注册-->
  <div id="register">
    <div class="link"></div>
    <div class="container">

      <div class="main clearfix">
        <div class="reg-form" style="width:500px;">
          <form novalidate="novalidate" action="javascript:;" id="register-form" method="post" enctype="multipart/form-data">

            <div class="item-phone-wrap">
              <div class="form-item form-item-phone">
                <label class="select-country" id="select-country">手 机 号 码<i class="icon-red"> ✻</i></label>
                <label class="txt" for="form-phone">建议使用常用手机</label>
                <input id="form-phone" name="phone" class="field" maxlength="11" type="text">
                <i class="i-status"></i>
              </div>
              <div class="input-tip">
                <span>
                  <i class="i-def"></i>
                  完成验证后，可以使用该手机登录和找回密码
                </span>
              </div>
            </div>

            <div class="item-authcode-wrap">
              <div class="form-item form-item-authcode">
                <label>图 文 验 证<i class="icon-red"> ✻</i></label>
                <label class="txt" for="authCode">请输入验证码</label>
                <input name="authcode" id="authCode" maxlength="6" class="field form-authcode" type="text">
                <img src="" class="img-code" title="换一换" id="imgAuthCode">
              </div>
              <div class="input-tip">
                <span>
                  <i class="i-def"></i>
                  看不清？点击图片更换验证码
                </span>
              </div>
            </div>

            <div class="item-mobileCode-wrap">
              <div class="form-item form-item-phonecode">
                <label>手 机 验 证<i class="icon-red"> ✻</i></label>
                <label class="txt" for="phoneCode" >请输入手机验证码</label>
                <input name="mobileCode" maxlength="6" id="phoneCode" class="field phonecode" type="text">
                <div class="phone-code">
                  <button id="getPhoneCode" class="btn-phonecode" type="button">获取验证码</button>
                </div>
                <i class="i-status"></i>
              </div>
              <div class="input-tip">
                <span>
                  <i class="i-def"></i>
                  请在60秒内输入验证码，否则将验证失败
                </span>
              </div>
            </div>

            <div class="item-storeName-wrap">
              <div class="form-item">
                <label>门 店 名 称<i class="icon-red"> ✻</i></label>
                <label class="txt" for="form-storeName">您的门店名称</label>
                <input id="form-storeName" name="storeName" class="field" maxlength="20" type="text">
                <i class="i-status"></i>
              </div>
              <div class="input-tip">
                <span>
                  <i class="i-def"></i>
                  支持中文、字母、数字、“-”“_”的组合，4-20个字符
                </span>
              </div>
            </div>

            <div class="item-legalPerson-wrap">
              <div class="form-item">
                <label>企 业 法 人<i class="icon-red"> ✻</i></label>
                <label class="txt" for="form-legalPerson">请输入法人/负责人的姓名</label>
                <input id="form-legalPerson" name="legalPerson" class="field" maxlength="20" type="text">
                <i class="i-status"></i>
              </div>
              <div class="input-tip">
                <span>
                  <i class="i-def"></i>
                  仅支持中文、字母的组合
                </span>
              </div>
            </div>

            <div class="item-charter-wrap">
              <div class="form-item">
                <label>营 业 执 照<i class="icon-red"> ✻</i></label>
                <img src="${base}/official/example-idcard-front.png" id="img1">
                <input type="file" class="upload_img" name="uploadFile" id="headImg" onchange="getFullPath()"/>
                <i class="i-status"></i>
              </div>
            </div>

            <div class="item-shipAddress-wrap" style="margin-top:85px;padding-bottom: 65px;">
              <div class="form-item form-item-dizhi">
                <div style="line-height:25px;padding-left: 20px;">选 择 区 域 及 门 店 地 址<i class="icon-red"> ✻</i></div>
                <div class="item-ifo" style="left:0px;margin-top: 25px;">
                  <span class="fieldSet">
                    <input type="hidden" id="areaId" name="areaId" value="${(area.id)!}" treePath="${(area.treePath)!}" />
                  </span>
                </div>
                <div class="item-text" style="left:0px;">
                  <label for="form-fullAddress" class="txt">请输入详细地址</label>
                  <input id="form-fullAddress" name="fullAddress" type="text" style="width:382px;">
                </div>
                <i class="i-status"></i>
              </div>
              <div class="input-tip" style="padding-left: 20px;">
                <span>
                  <i class="i-def"></i>
                  例如：合肥市瑶海区中绿广场A座17层1700室
                </span>
              </div>
            </div>
            <div class="sub-register" style="right:-20%;">
              <button type="submit" class="btn-register" id="submit_register">完成注册</button>
            </div>
          </form>
        </div>
        <div class="reg-other">
            <div class="phone-fast-reg">
                <img src="${base}/official/reg-ewm-img.png">
            </div>
            <div class="company-reg">
                <a href="javascript:;" target="_top">
                    <i class="i-company"></i>
                    <span>手机快速注册</span>
                </a>
            </div>
        </div>
      </div>
    </div>
  </div>
  <!--注册-->
  <div id='s-page-container'></div>
  <div id='fb-root'></div>
  <div id='app-script-root'></div>
  <div id='app-view-root'></div>
  <script type="text/javascript" src="${base}/resources/store/js/index_data3.js"></script>
  <!--登录js触发事件-->
  <script type="text/javascript">
    var currentPosition, timer;
    function GoTop() {
      var timer = null;
      timer = setInterval(function () {
        var backtop = document.body.scrollTop;
      var speedtop = backtop / 5;//向上滚动速度
      document.body.scrollTop = backtop - speedtop;
      if (backtop == 0) {//到达顶部关闭向上滚动事件
        clearInterval(timer);
      }
    }, 30);

    }
    function SongRan(){
      GoTop();
      $("#login_dialog").show(1000);
      $("#banner_info .banner_login").show(100);
      $("#register").hide(100);
    }
    function SongRan_reg(){
      $("#login_dialog").hide(100);
      $("#banner_info .banner_login").hide(100);
      $("#register").show(1000);
      $("#imgAuthCode").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");

    }
  </script>
  <!--登录页面的提示-->
  <script type="text/javascript">
    //文本框默认提示文字
    function textFocus(el) {
      if (el.defaultValue == el.value) { el.value = ''; el.style.color = '#333'; }
    }
    function textBlur(el) {
      if (el.value == '') { el.value = el.defaultValue; el.style.color = '#999'; }
    }
    /*清除提示信息*/
    function emptyRegister(){
      $(".login-box .phone,.login-box .phonekey,.login-box .password,.login-box .passwordTwo").removeClass("errorC");
      $(".login-box .error1,.login-box .error2,.login-box .error3,.login-box .error4").empty();
    }
    function emptyLogin(){
      $(".login-box .account,.login-box .admin_pwd,.login-box .photokey").removeClass("errorC");
      $(".login-box .error5,.login-box .error6,.login-box .error7").empty();
    }
  </script>
  <!--登录js验证-->
  <script type="text/javascript">
    $().ready(function(){
      $("#forget_captcha_img").click(function () {
        $("#forget_captcha_img").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");
      });
    });
  //控制登录，注册，忘记密码的首先和隐藏
  var login_type="account";
  function login_method(type,obj) {
    if (type == "account") {
      login_type="account";
      $("#account_login").show();
      $("#phone_login").hide();
      $("#forget_password").hide();
      $("#reset_password").hide();
    } else if (type == "phone") {
      login_type="phone";
      $("#account_login").hide();
      $("#phone_login").show();
      $("#forget_password").hide();
      $("#reset_password").hide();
    } else if (type=="forget"){
      $("#account_login").hide();
      $("#phone_login").hide();
      $("#forget_password").show();
      $("#reset_password").hide();
      $("#forget_captcha_img").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");
    }else if (type=="reset"){
      $("#account_login").hide();
      $("#phone_login").hide();
      $("#forget_password").hide();
      $("#reset_password").show();
    }
  }
  //60秒倒计时
  var count = 60, ii;
  function refreshTime() {
    count = count - 1;
    if (count == 0) {
      $(".get-btn").html("获取验证码");
      count = 60;
      clearInterval(ii);
      return false;
    }
    $(".get-btn").html(count + "秒后重新获取");
  }
  //获取验证码
  function get_code(type) {
    var type_url="",data="";
    if (count != 60) {
      return;
    }
    if (type == 'phone_login') {
      if($("#login_phone").val().trim()==""){
        $.message('warn', "请先填写手机号码");
        return false;
      }
      if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($("#login_phone").val().trim()))) {
        $.message('warn', "请确认您的号码是否正确");
        return false;
      }
      type_url="${base}/store/login/send_mobile.jhtml";
      data={
        mobile:$("#login_phone").val().trim()
      };
    }else if(type == 'forget_password'){
      if($("#forget_username").val().trim()==""){
        $.message('warn', "请先填写手机号码");
        return false;
      } 
      if($("#forget_img_captcha").val().trim()==""){
        $.message('warn', "验证码不能为空");
        return false;
      } 
      if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($("#forget_username").val().trim()))) {
        $.message('warn', "请确认您的号码是否正确");
        return false;
      } 
      type_url="${base}/store/login/send.jhtml";
      data={
        username:$("#forget_username").val().trim(),
        captchaId:"${captchaId}",
        captcha:$("#forget_img_captcha").val().trim()
      };
    } 
    $.ajax({
      url: type_url,
      data:data,
      type: "POST",
      dataType: "json",
      cache: false,
      success: function (message) {
        $.message(message);
        if(message.type=="success"){
          ii = setInterval(refreshTime, 1 * 1000);
          $(".get-btn").html(count + "秒后重新获取");
        }else{
          $("#forget_captcha_img").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");
        }
      }
    });
  }
  //点击下一步
  function go_reset(){
    if($("#forget_phone_captcha").val().trim()==""){
      $.message('warn', "验证码不能为空");
      return false;
    } 
    $.ajax({
      url:"${base}/store/login/check_captcha.jhtml",
      type:"post",
      data:{
        username:$("#forget_username").val().trim(),
        securityCode:$("#forget_phone_captcha").val().trim()
      },
      dataType:"json",
      success:function(message){
        $.message(message);
        if(message.type=="success"){
          login_method("reset");
        }else{
          login_method("forget");
        }
      }
    });
  }
  //重置密码提交
  function reset_password() {
    var _mobile = $("#forget_username").val().trim();
    var _npassword = $("#new_password").val().trim();
    var _enpassword = $("#re_new_password").val().trim();
    if (_npassword == "" || _npassword == null) {
      $.message("warn", "请输入新密码");
      return;
    }
    if (_enpassword == "" || _enpassword == null) {
      $.message("warn", "请再次输入新密码");
      return;
    }

    if (_npassword != _enpassword) {
      $.message("warn", "两次密码不一致，请重新确认！");
      return;
    }
    $.ajax({
      url: "/common/public_key.jhtml",
      type: "POST",
      data: {local: true},
      dataType: "json",
      cache: false,
      success: function (data) {
        var rsaKey = new RSAKey();
        rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
        var enPassword = hex2b64(rsaKey.encrypt(_npassword));
        $.ajax({
          url: "/store/login/reset.jhtml",
          type: "POST",
          data: {
            mobile: _mobile,
            newpassword: enPassword,
            securityCode:$("#forget_phone_captcha").val().trim()
          },
          dataType: "json",
          cache: false,
          success: function (message) {
            $.message(message.type, message.content);
            if (message.type == 'success') {
              login_method("account");
            }
          }
        });
      }
    });
  }
  //账户登录提交
  var _i = 0;
  function account_submit() {
    if (_i != 0) {
      return;
    }
    if ($("#account_username").val().trim() == "") {
      $("#account_username").next().text("必填");
      return;
    }
    if ($("#account_password").val().trim() == "") {
      $('#account_password').next().text("必填");
      return;
    }
    _i = 1;
    $("#account_login_btn").val("正在为您跳转");
    $.ajax({
      url: "${base}/common/public_key.jhtml",
      type: "POST",
      data: {local: true},
      dataType: "json",
      cache: false,
      success: function (data) {
        var rsaKey = new RSAKey();
        rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
        var enPassword = hex2b64(rsaKey.encrypt($("#account_password").val()));

        $.ajax({
          url: "${base}/store/login/submit.jhtml",
          type: "POST",
          data: {
            username: $("#account_username").val(),
            enPassword: enPassword
          },
          dataType: "json",
          cache: false,
          success: function (message) {
            if (message.type == "success") {
              $.message("success", "登录成功！正在为您跳转....");
              setTimeout(function () {
                location.href = "${base}/store/member/index.jhtml";
              }, 1500);

              if ($("#isRememberUsername").prop("checked")) {
                addCookie("memberUsername", $("#account_username").val(), {expires: 7 * 24 * 60 * 60});
              } else {
                removeCookie("memberUsername");
              }
            } else {
              $.message(message);
              _i = 0;
              $("#account_login_btn").val("登录");
            }
          }
        });
      }
    });
}
  //手机登录提交
  function phone_submit(){
    if ($("#login_phone").val().trim() == "") {
      $('#login_phone').next().text("必填");
      return;
    }
    if ($("#login_captcha").val().trim() == "") {
      $('#login_captcha').next().next().text("必填");
      return;
    }
    $("#phone_login_btn").val("正在为您跳转");
    $.ajax({
      url: "${base}/store/login/phone_login_submit.jhtml",
      type: "POST",
      data: {
        mobile: $("#login_phone").val().trim(),
        captcha:$("#login_captcha").val().trim()
      },
      dataType: "json",
      cache: false,
      success: function (message) {
        $.message(message.type,message.content);
        if (message.type == "success") {
          setTimeout(function () {
            location.href = "${base}/store/member/index.jhtml";
          }, 1500);

          if ($("#isRememberUsername").prop("checked")) {
            addCookie("memberUsername", $("#login_phone").val(), {expires: 7 * 24 * 60 * 60});
          } else {
            removeCookie("memberUsername");
          }
        } else {

          $("#phone_login_btn").val("登录");
        }
      }
    });
  }
  //按enter键登录
  document.onkeydown = function (event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 13) { // enter 键
      if(login_type=="account"){
        account_submit();
      }else{
        phone_submit();
      }
      
    }
  };
</script>
<script type="text/javascript">
  var url_list;
  var timeout;
  var $areaId;
  var $communityId;
  $(function(){
    var $inputForm = $("#inputForm");
    $areaId = $("#areaId");
    $communityId = $("#communityId");

    //=====验证码切换=======//
    $("#imgAuthCode").click(function () {
      $("#imgAuthCode").attr("src", "/common/captcha.jhtml?captchaId=${captchaId}");
    });
    //======获取手机验证码========//
    $("#getPhoneCode").click(function(){
      if ($("#form-phone").val().trim() == '') {
        $.message("error", " 请先填写手机号");
        return false;
      }
      if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($("#form-phone").val()))) {
        $.message("error", "手机号码不符合");
        return;
      }
      if ($("#authCode").val().trim() == '') {
        $.message("error", " 请先填写验证码");
        return false;
      }
      $.ajax({
        url: "${base}/store/register/send.jhtml",
        data: {
          username: $("#form-phone").val(),
          captchaId:"${captchaId}",
          captcha:$("#authCode").val()
        },
        dataType: "json",
        type: "post",
        success: function (data) {
          $.message(data.type, data.content);
          if (data.type == 'error') {
            $("#imgAuthCode").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
          }
        }
      });
    });
    // =======地区选择===============
    $areaId.lSelect({
      url: "${base}/common/area.jhtml",
      fn:areaSelect
    });
    //======注册提交======//
    $("#submit_register").click(function(){
      if ($("#form-phone").val().trim() == '') {
        $.message("error", " 请先填写手机号");
        return false;
      }
      if ($("#form-storeName").val().trim() == '') {
        $.message("error", "店铺名不能为空");
        return;
      }
      if($("#form-legalPerson").val()==""){
        $.message("error", "企业法人不能为空");
        return;
      }
      if($("#form-fullAddress").val()=="") {
        $.message("error", "请填写详细地址");
        return;
      }
      
      $.ajax({
        url: "${base}/store/register/register_company.jhtml",
        type: "POST",
        data: {
          mobile:$("#form-phone").val().trim(),
          securityCode: $("#phoneCode").val().trim(),
          name:$("#form-storeName").val().trim(),
          address:$("#form-fullAddress").val().trim(),
          licensePhoto:url_list,
          linkman:$("#form-legalPerson").val().trim(),
          tenantType:$("[name='tenantType']").val(),
          areaId:$("#areaId").val().trim()
        },
        dataType:"json",
        cache: false,
        success: function (message) {
          if(message.type=="success"){
            $.message("success", "注册成功，等待审核中...");
            SongRan();
            login_method("account");
          }else{
            $.message(message);
          }
        }
      });
       
    }); 
  });
  //图片预览
  function getFullPath() {
    var file=document.getElementById('headImg');
    var fileList = file.files;
    var img=document.getElementById("img1");
    for(var i=0;i<fileList.length;i++){
      if(file.files[0]){
        img.style.display = 'block';
        img.style.width = '168px';
        img.style.height = '120px';
        if (window.createObjectURL!=undefined) { // basic 
          img.src = window.createObjectURL(file.files[0]); 
        } else if (window.URL!=undefined) { // mozilla(firefox) 
          img.src = window.URL.createObjectURL(file.files[0]); 
        } else if (window.webkitURL!=undefined) { // webkit or chrome 
          img.src = window.webkitURL.createObjectURL(file.files[0]); 
        } 
      }   
      var f_i= new FormData();
      f_i.append("file",fileList[i])
      $.ajax({
        url:"${base}/app/file/upload_image.jhtml",
        type:"post",
        data: f_i,
        async: false ,
        processData: false,
        contentType: false,
        success:function (data){
          url_list=data.data;
        }
      });
    }
  }
  function getCommunity() {
    if ($areaId.val()>0) {
      $.ajax({
        url: "get_community.jhtml",
        type: "GET",
        data: {areaId:$areaId.val()},
        dataType: "json",
        cache: false,
        success: function(map) {
          var opt="<option value=0>--请选择--</option>";
          for (var key in map) {
            opt = opt +"<option value="+key+">"+map[key]+"</option>";
          }
          $communityId.html(opt);
        }
      });
    } else {
      $communityId.html("<option value=0>--无--</option>");
    }
  }
  function areaSelect(){
    clearTimeout(timeout);
    timeout = setTimeout(function() {
      getCommunity();
    }, 500);
  }
</script>
</body>
</html>
