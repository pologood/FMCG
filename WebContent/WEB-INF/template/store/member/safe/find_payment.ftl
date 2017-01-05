<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>${message("shop.myAccount.title")}</title>
    <link type="text/css" rel="stylesheet" href="${base}/resources/store/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/store/css/product.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/store/font/iconfont.css"/>


    <script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $passwordForm = $("#passwordForm");
            var $username = $("input[name='username']");
            var $submit = $(":submit");
            var $getCode = $("#getCode");
            var $mobile = $("#mobile");
            var $email = $("#email");
            var $span_1 = $("#span_1");
            var count = 60, ii;

            //获取验证码
            $getCode.on('click', function (e) {
                if ($username.val().trim() == '') {
                    $.message("error", "请先填写用户名");
                    return false;
                }

                if ($("#captcha").val().trim() == '') {
                    $.message("error", "请输入图片验证码");
                    return;
                }

                //获取手机验证码
                $.ajax({
                    url: "${base}/store/member/safe/send_mobile.jhtml",
                    data: {
                        username: $username.val()
                    },
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        $.message(data.message.type,data.message.content);
                        if (data.message.type == 'success') {
                            //$getCode.attr('style', 'display:none;');
                            //$span_1.attr('style', 'display:block;');

                            $getCode.find("a").html(count + "秒后重新获取");
                            ii = setInterval(refreshTime, 1 * 1000);
                        }
                    }
                });
                return false;
                function refreshTime() {
                    count = count - 1;
                    if (count == 0) {
                        count = 60;
                        //$getCode.attr('style', 'display:block');
                        //$span_1.attr('style', 'display:none');
                        $getCode.find("a").html("获取验证码");
                        clearInterval(ii);
                        return false;
                    }
                    //$("#span_1").html(count + "秒后重新获取");
                    $getCode.find("a").html(count + "秒后重新获取");
                }

            });

            // 表单验证
            $passwordForm.validate({
                rules: {
                    username: "required",
                    captcha:"required",
                    checkCode: {
                        required: true
                    }
                },
                messages:{
                    username: "必填",
                    captcha:"必填",
                    checkCode:"必填"
                },
                submitHandler: function (form) {
                    $.ajax({
                        url: "check.jhtml",
                        type: "post",
                        dataType: "json",
                        data: {
                            securityCode: $("input[name='checkCode']").val()
                        },
                        success: function (message) {
                            if (message.type == "success") {
                                form.submit();
                            } else {
                                alert(message.content);
                                return false;
                            }
                        }
                    });
                }
            });
            $("#confirm").click(function () {
                $passwordForm.submit();
            });
        });
    </script>
</head>

<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/header.ftl"]
[#include "/store/member/include/menu.ftl"]
[#include "/store/member/include/bootstrap_css.ftl"]
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                个人中心
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i>首页</a></li>
                <li class="active">个人中心</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <div class="tab-content">
                            <div class="mainbox member" style="position:static;">
                                <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                                    <div class="js-app-header title-wrap" id="app_0000000844">
                                        <img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage.png"/>
                                        <dl class="app-info">
                                            <dt class="app-title" id="app_name">找回支付密码</dt>
                                            <dd class="app-status" id="app_add_status"></dd>
                                            <dd class="app-intro" id="app_desc">忘记支付密码了，可以通过手机找回。</dd>
                                        </dl>
                                    </div>
                                    <ul class="links" id="mod_menus">
                                        <li><a class="on" hideFocus="" href="javascript:;">找回支付密码</a></li>
                                    </ul>
                                </div>

                                <div class="account-table1" id="account-table1">
                                    <div class="wrap" style="margin-top:5px;">
                                        <div class="main">
                                            <form id="passwordForm" action="find_payment.jhtml" method="post">
                                                <table class="table table-bordered table-hover dataTable">
                                                    <tr>
                                                        <th>
                                                            <span class="requiredField">*</span>登陆用户名:
                                                        </th>
                                                        <td>
                                                            <input type="text" id="username" name="username" class="text"
                                                                   maxlength="${setting.usernameMaxLength}"/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                            <span class="requiredField">*</span>图片验证码:
                                                        </th>
                                                        <td>
                                                            <input class="text" type="text" id="captcha" name="captcha" maxlength="18"
                                                                   placeholder="请输入验证码">
                                                            <img id="captchaImage" class="captchaImage"
                                                                 src="${base}/common/captcha.jhtml?captchaId=${captchaId}" alt="验证码">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>
                                                            <span class="requiredField">*</span>验证码:
                                                        </th>
                                                        <td class="w_register_top" style="width: 469px;">
                                                            <input type="text" id="checkCode" name="checkCode" class="text"
                                                                   maxlength="10"/>
                                            <span id="getCode" class="w_registerget">
                                                <a href="javascript:;" style="color: #fff;">获取验证码</a></span>
                                                            <span id="span_1" style="display:none" class="w_timing">60秒后重新获取</span>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>&nbsp;</th>
                                                        <td>
                                                            <input class="h-button button" type="button" class="submit" id="confirm"
                                                                   value="下一步"/>
                                                            <input type="button" class="h-button button"
                                                                   value="${message("box.password.back")}"
                                                                   onclick="location.href='index.jhtml'"/>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                        </div>
                                    </div>
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
<!-- ./wrapper -->
[#include "/store/member/include/bootstrap_js.ftl"]
</body>
</html>
