<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>企业注册</title>
    <meta name="keywords" content="企业注册" />
    <meta name="description" content="企业注册" />
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>

    <link rel="icon" href="${base}/resources/b2c/images/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/register.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<div class="pc-g header-bg header_zc">
    <div class="container">
        <div class="logo-con clearfix">
            <a href="${base}/b2c/index.jhtml" class="logo">
                <img src="${base}/resources/b2c/images/logo_zc.png" alt="" />
            </a>
            <div class="logo-title">企 业 注 册</div>
            <div class="have-account">已有账号 <a href="${base}/b2c/login.jhtml?type=login">请登录</a></div>
        </div>
    </div>
</div>

<div class="container">

    <div class="main clearfix">
        <div class="reg-form f-left">
            <form novalidate="novalidate" action="javascript:;" id="register-form" method="post" enctype="multipart/form-data">

                <div class="item-phone-wrap">
                    <div class="form-item form-item-phone">
                        <label class="select-country" id="select-country">手 机 号 码</label>
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

                <div class="item-pwd-wrap">
                    <div style="z-index: 12;" class="form-item">
                        <label>设 置 密 码</label>
                        <label for="password" class="txt">建议至少使用两种字符组合</label>
                        <input name="pwd" id="password" class="field" maxlength="20" type="password">
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <span>
                        <i class="i-def"></i>
                        建议使用字母、数字和符号两种及以上的组合，6-20个字符
                    </span>
                    </div>
                </div>

                <div class="item-pwdRepeat-wrap">
                    <div style="z-index: 12;" class="form-item">
                        <label>确 认 密 码</label>

                        <label class="txt" for="rePassword" >请再次输入密码</label>
                        <input name="rePassword" id="rePassword" class="field" maxlength="20" type="password">
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <span>
                        <i class="i-def"></i>
                        请再次输入密码
                    </span>
                    </div>
                </div>

                <div class="item-storeName-wrap">
                    <div class="form-item">
                        <label>门 店 名 称</label>
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

                <div class="item-dianpulx-wrap">
                    <div class="form-item form-item-dianpulx">
                        <label>店 铺 类 型</label>
                        <div class="item-ifo">
                            <select name="tenantType" id="tenantType" rel="select" tabindex="8" onchange="get_tenant_type(this)">
                                <option value="" selected="selected">请选择店铺类型</option>
                                <option value="tenant" >服务商</option>
                                <option value="supplier">供货商</option>
                                <option value="retailer" >超市</option>
                            </select>
                        </div>

                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <span>

                    </span>
                    </div>
                </div>

                <div class="item-legalPerson-wrap">
                    <div class="form-item">
                        <label>企 业 法 人</label>
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
                        <label>营 业 执 照</label>
                        <input type="file" class="upload_img" name="uploadFile" id="headImg" onchange="getFullPath()"/>
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <!-- <span> -->
                        <img src="${base}/resources/wap/2.0/images/example-idcard-front.png" id="img1">
                    <!-- </span> -->
                    </div>
                </div>

                <div class="item-shipAddress-wrap" style="margin-top:100px;">
                    <div class="form-item form-item-dizhi">
                        <div style="line-height:25px;">[配 送 地 址]</div>
                        <div class="item-ifo" style="left:0px;margin-top: 15px;">
                        <span class="fieldSet">
                            <input type="hidden" id="areaId" name="areaId" />
                        </span>
                        </div>
                        <div class="item-text" style="left:0px;">
                            <label for="form-fullAddress" class="txt">请输入详细地址</label>
                            <input id="form-fullAddress" name="fullAddress" type="text">
                        </div>
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                    <span>
                        <i class="i-def"></i>
                        例如：合肥市瑶海区中绿广场A座17层1700室
                    </span>
                    </div>
                </div>

                <div class="item-authcode-wrap">
                    <div class="form-item form-item-authcode">
                        <label>验　证　码</label>
                        <label class="txt" for="authCode">请输入验证码</label>
                        <input name="authcode" id="authCode" maxlength="6" class="field form-authcode" type="text">
                        <img src="${base}/common/captcha.jhtml?captchaId=${captchaId}" class="img-code" title="换一换" id="imgAuthCode">
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
                        <label>手机验证码</label>
                        <label class="txt" for="phoneCode" >请输入手机验证码</label>
                        <input name="mobileCode" maxlength="6" id="phoneCode" class="field phonecode" type="text">
                        <button id="getPhoneCode" class="btn-phonecode" type="button">获取验证码</button>
                        <i class="i-status"></i>
                    </div>
                    <div class="input-tip">
                        <span>
                            <i class="i-def"></i>
                            请在60秒内输入验证码，否则将验证失败
                        </span>
                    </div>
                </div>

                <div class="form-agreen">
                    <div><input name="agreen" checked="" id="agree" type="checkbox">我已阅读并同意<a href="javascript:;" id="protocol">《XX用户注册协议》</a> </div>
                    <div class="input-tip">
                        <span></span>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn-register" id="submit_register">立即注册</button>
                </div>
            </form>
        </div>
        <div class="reg-other company_reg_other">
            <div class="phone-fast-reg">
            </div>
            <div class="company-reg">
                <a href="${base}/b2c/register/register_company.jhtml" target="_top">
                    <i class="i-company"></i>
                    <span>企业用户注册</span>
                </a>
            </div>
           <!--  <div class="inter-cust">
                <a href="${base}/b2c/register/register_person.jhtml" target="_top">
                    <i class="i-global"></i>
                    <span>普通用户注册</span>
                </a>
            </div> -->
        </div>
    </div>
    [#include "/b2c/include/prototal.ftl"]
    <div class="ui-mask"></div>
</div>
<script type="text/javascript" src="${base}/resources/b2c/js/register/page_regist.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/register/jquery.properties-1.0.9.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/register/jquery.validate.js"></script>
<script type="text/javascript">
    var url_list;
    $("#protocol").click(function () {
        $(".ui-dialog").show();
        $(".ui-mask").show();
    });
    $(".ui-dialog-close").click(function () {
        $(".ui-dialog").hide();
        $(".ui-mask").hide();
    });
    $(".ui-mask").click(function () {
        $(this).hide();
        $(".ui-dialog").hide();
    });
    $(function(){
        var $inputForm = $("#inputForm");
        var $areaId = $("#areaId");
        var $communityId = $("#communityId");
        var timeout;
        //=====验证码切换=======//
        $("#imgAuthCode").click(function () {
            $("#imgAuthCode").attr("src", "${base}/common/captcha.jhtml?captchaId=${captchaId}");
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
                url: "${base}/b2c/register/send.jhtml",
                data: {
                    username: $("#form-phone").val(),
                    captchaId:"${captchaId}",
                    captcha:$("#authCode").val()
                },
                dataType: "json",
                type: "post",
                success: function (data) {
                    $.message(data.type, data.content);
                    if (data.type == 'success') {
                        
                    }else {
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
            if ($("#form-storeName").val().trim() == '') {
                $.message("error", "店铺名不能为空");
                return;
            }
            if ($("#password").val().trim() == ''||$("#rePassword").val().trim() == '') {
                $.message("error", "密码不能为空");
                return;
            }
            if ($("#password").val().trim()!=$("#rePassword").val().trim()) {
                $.message("error", "两次密码输入不一致");
                return;
            }
            if($("[name='tenantType']").val()==""){
                $.message("error", "请选择店铺类型");
                return;
            }
            if($("#form-legalPerson").val()==""){
                $.message("error", "企业法人不能为空");
                return;
            }
            if(!$("#agree").prop("checked")) {
                $.message("error", "未同意《服务协议》");
                return;
            }
            if($("#headImg").val()=="") {
                $.message("error", "请上传营业执照");
                return;
            }
            if($("#form-fullAddress").val()=="") {
                $.message("error", "请填写详细地址");
                return;
            }
            $.ajax({
                url: "${base}/common/public_key.jhtml",
                type: "POST",
                data: {local: true},
                dataType: "json",
                cache: false,
                success: function (data) {
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                    var enPassword = hex2b64(rsaKey.encrypt($("#password").val().trim()));
                    $.ajax({
                        url: "${base}/b2c/register/register_company.jhtml",
                        type: "POST",
                        data: {
                            mobile:$("#form-phone").val().trim(),
                            newpassword: enPassword,
                            securityCode: $("#phoneCode").val().trim(),
                            name:$("#form-storeName").val().trim(),
                            address:$("#form-fullAddress").val().trim(),
                            licensePhoto:url_list,
                            linkman:$("#form-legalPerson").val().trim(),
                            tenantType:$("#tenantType").val().trim(),
                            areaId:$("#areaId").val().trim()
                        },
                        dataType:"json",
                        cache: false,
                        success: function (message) {
                            $.message(message.type, message.content);
                            if(message.type=="success"){
                                if($("#tenantType").val()=="tenant"){
                                    location.href="${base}/helper/member/index.jhtml";
                                }else if($("#tenantType").val()=="supplier"){
                                    location.href="${base}/b2c/member/supplier/index.jhtml";
                                }else{
                                    location.href="${base}/b2c/index.jhtml";
                                }
                            }else{
                                location.reload();
                            }
                        }
                    });
                }
            });
        }); 
    });
    // function get_tenant_type(obj){
    //     alert($(obj).val());
    // }
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
<!--标语 -->
[#include "/b2c/include/slogen.ftl"]
<!--底部 -->
[#include "/b2c/include/footer.ftl"]
</body>
</html>