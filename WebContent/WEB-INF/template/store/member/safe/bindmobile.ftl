<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>手机绑定设置</title>
    [#include "/store/member/include/bootstrap_css.ftl"]
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/style.css" type="text/css" rel="stylesheet"/>
</head>

<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        [#include "/store/member/include/header.ftl"]
        [#include "/store/member/include/menu.ftl"]
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <section class="content-header">
                <h1>
                    个人中心
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                    <li class="active">个人中心</li>
                </ol>
            </section>
            <!-- Main content -->
            <section class="content">
                <div class="row">
                    <div class="col-md-12">
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs pull-right">                                    
                                <li class="pull-left header"><i class="fa fa-users"></i>手机绑定设置</li>
                            </ul>
                            <div class="tab-content">
                                <div class="account-table1" id="account-table1">
                                    <form class="form-horizontal" method="post" role="form">
                                        <div class="form-group mb5">
                                            <label  class="col-sm-2 control-label">绑定状态:</label>
                                            <div class="col-sm-8">
                                                <p class="form-control" style="border:0px;">[#if member.bindMobile=="binded"]已绑定[#else]未绑定[/#if]</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label  class="col-sm-2 control-label"><span class="red">*</span>手机号:</label>
                                            <div class="col-sm-8">
                                                [#if member.bindMobile == "binded"]
                                                <input type="hidden" id="phone"  class="form-control" maxlength="20" value="${member.mobile}">
                                                <input type="text" class="form-control"value="${mosaic(member.mobile, 3, '~~~')}" style="border:0px;" disabled="true">
                                                [#else]
                                                <input type="text" id="phone" class="form-control" maxlength="20" placeholder="请输入要绑定的手机号">
                                                [/#if]
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label  class="col-sm-2 control-label"><span class="red">*</span>手机验证码:</label>
                                            <div class="col-sm-6">
                                                <input type="text" id="phone_captcha" name="password"  class="form-control">
                                            </div>
                                            <div class="col-sm-2">
                                                <input type="button" id="get_code_btn" class="btn btn-block btn-primary" value="获取验证码" onclick="get_code();">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-offset-2 col-sm-2">
                                                <button type="button" class="btn btn-block btn-primary" onclick="bind_mobile()">
                                                    [#if member.bindMobile == "binded"]解绑[#else]绑定[/#if]
                                                </button>
                                            </div>
                                            <div class="col-sm-offset-0 col-sm-2">
                                                <button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
                                            </div>
                                        </div>
                                    </form> 
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <!-- /.content -->
        </div>
        <!-- /.content-wrapper -->
        [#include "/store/member/include/footer.ftl"]
    </div>
    [#include "/store/member/include/bootstrap_js.ftl"]
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>

    <script type="text/javascript">
        //60秒倒计时
        var count = 60, ii;
        function refreshTime() {
            count = count - 1;
            if (count == 0) {
              $("#get_code_btn").val("获取验证码");
              count = 60;
              clearInterval(ii);
              return false;
            }
            $("#get_code_btn").val(count + "秒后重新获取");
        }
        //获取验证码
        function get_code() {
            if (count != 60) {
                  return;
            }
            if($("#phone").val().trim()==""){
                $.message('warn', "手机号为空");
                return false;
            } 
            if (!(/^1[3|4|5|6|7|8|9][0-9]\d{4,8}$/.test($("#phone").val().trim()))) {
                $.message('warn', "请确认您的号码是否正确");
                return false;
            } 
            $.ajax({
                url: "${base}/store/member/safe/send.jhtml",
                data:{username:$("#phone").val().trim()},
                type: "POST",
                dataType: "json",
                cache: false,
                success: function (message) {
                    $.message(message);
                    if(message.type=="success"){
                      ii = setInterval(refreshTime, 1 * 1000);
                      $("#get_code_btn").val(count + "秒后重新获取");
                    }
                }
            });
        }
        //绑定或解绑
        function bind_mobile(){
            if($("#phone_captcha").val()==""){
                $.message("warn","验证码不能为空");
                return;
            }
            $.ajax({
                url: "bindmobile.jhtml",
                type: "POST",
                data: {
                    username:$("#phone").val().trim(),
                    captcha:$("#phone_captcha").val()
                },
                dataType: "json",
                cache: false,
                success: function(message) {
                    $.message(message);
                    if (message.type == "success") {
                        setTimeout(function() {
                            location.href="index.jhtml";
                        }, 1000);
                        
                    } 
                }
            });
        }
    </script>
</body>
</html>
