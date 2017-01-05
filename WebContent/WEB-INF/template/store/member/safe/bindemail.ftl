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
    <script type="text/javascript" src="${base}/resources/box/js/common.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $email = $("#email");
            var $send = $("#send");
            var ii;
            var count = 60;
            var $getCode = $("#getCode");
            var $span_1 = $("#span_1");

        [@flash_message /]
            $getCode.on('click', function (e) {
                if ($email.val() == '') {
                    alert("请先填写绑定邮箱");
                    return false;
                }
                $.ajax({
                    url: "send_email.jhtml",
                    type: "POST",
                    data: {email: $email.val(), captchaId: "${captchaId}"},
                    dataType: "json",
                    cache: false,
                    success: function (msg) {
                        if (msg.type == "success") {
                            $getCode.attr('style', 'display:none;');
                            $span_1.attr('style', 'display:block;');
                            ii = setInterval(refreshTime, 1 * 1000);
                        } else {
                            $("#send").text("发送失败,请重新发送");
                        }
                    }
                });
            });
            function refreshTime() {
                count = count - 1;
                if (count == 0) {
                    count = 60;
                    $getCode.attr('style', 'display:block');
                    $span_1.attr('style', 'display:none');
                    clearInterval(ii);
                    return false;
                }
                $("#span_1").html(count + "秒后重新获取");

            }

// 表单验证
            $inputForm.validate({
                rules: {
                    email: {
                        required: true
                    }
                }
            });
        });

        [#if member.bindEmail == "binded"]
        $("#email").prop("disabled", true);
        [/#if]
    </script>
</head>

<body>


[#include "/store/include/header.ftl" /]
[#include "/store/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/store/member/include/border.ftl" /]

    [#include "/store/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="mainbox member" style="position:static;">
                <div class="main">
                [#if member.bindEmail == "binded"]
                    <div class="red-title"><P class="margins-lr">账号邮箱解绑</p></div>
                [#else]
                    <div class="red-title"><P class="margins-lr">账号邮箱绑定</p></div>
                [/#if]
                    <form id="inputForm" action="bindemail.jhtml" method="post">
                        <table class="input">
                            <tr>
                                <th>
                                    E-MAIL:
                                </th>
                                <td>
                                    <input type="text" name="email" id="email" class="text" maxlength="255"
                                           value="${member.email}"/>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    校验码:
                                </th>
                                <td>
                                    <input type="text" name="securityCode" id="securityCode" class="text"
                                           maxlength="20"/>
                                    <span id="getCode"><a href="#" id="send">获取验证码</a></span>
                                    <div id="span_1" style="display:none">60秒后重新获取</div>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    &nbsp;
                                </th>
                                <td>
                                [#if member.bindEmail == "binded"]
                                    <input type="submit" class="h-button" value="解绑"/>
                                [#else]
                                    <input type="submit" class="h-button" value="发送"/>
                                [/#if]
                                    <input type="button" class="h-button" value="${message("shop.member.back")}"
                                           onclick="location.href='index.jhtml?menuType=2'"/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
[#include "/store/include/footer.ftl" /]
</body>
</html>
