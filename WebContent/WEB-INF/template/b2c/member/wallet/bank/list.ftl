<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">


    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            var $inputForm = $("#inputForm");
            var $submit = $("#submit");

            $inputForm.validate({
                rules: {
                    account: {
                        required: true,
                        number:true
                    },
                    securityCode: {required: true}
                },
                messages: {
                    account: {
                        required: '必填',
                        number:'请输入正确的银行卡号'
                    },
                    securityCode: {
                        required: '必填'
                    }
                },
                beforeSend: function () {
                    $submit.prop("disabled", true);
                },
                success: function () {
                    $submit.prop("disabled", false);
                }
            });
        });

        function deleteBank(id) {
            if (window.confirm("确认要删除银行卡吗？")) {
                $.ajax({
                    type: 'post',
                    url: '${base}/app/member/bank/delete.jhtml',
                    data: {id: id},
                    dateType: 'json',
                    success: function (data) {
                        $.message(data.message);
                        if (data.message.type == "success") {
                            window.location.reload(true);
                        }
                    }
                });
            }
        }


        var count = 60, ii;
        function refreshTime() {
            count = count - 1;
            if (count == 0) {
                $("#sendCode").attr("value", "获取验证码");
                count = 60;
                clearInterval(ii);
                return false;
            }
            $("#sendCode").attr("value", count + "秒后重新获取");
        }

        function getCode() {
            if (count != 60) {
                return;
            }
            var _mobile = $("#mobile").val().trim();

            if ($.trim(_mobile) == '') {
                $.message('warn', "请先填写手机号码");
                return false;
            }
            if (!(/^1[3|4|5|6|7|8|9]\d{9}$/.test(_mobile))) {
                $.message('warn', "请确认您的号码是否正确");
                return false;
            }

            ii = setInterval(refreshTime, 1 * 1000);
            $("#sendCode").attr("value", count + "秒后重新获取");
            console.log(count, "1");
            $.ajax({
                url: "${base}/b2c/member/send_mobile.jhtml",
                data: {mobile: _mobile},
                type: "POST",
                dataType: "json",
                cache: false,
                success: function (data) {
                    $.message(data.type, data.content);
                }
            });

            console.log(count, "23");
        }

    </script>
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2c/include/topnav.ftl"]
</div>

<!--主页内容区start-->
<div class="paper">
    <!-- 会员中心头部-->
[#include "/b2c/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
        [#include "/b2c/include/member_left.ftl"]
            <div class="content">
                <!--我的银行卡-->
                <div class="my-bank-card">
                    <div class="my-bank-card-hd">
                        <a href="javascript:;">首页</a>
                        &nbsp;>
                        <a href="${base}/b2c/member/wallet/index.jhtml">我的钱包</a>
                        &nbsp;>
                        <a href="javascript:;">我的银行卡</a>
                    </div>
                    <div class="my-bank-card-list">
                    [#list memberBanks as bank]
                        <div class="my-bank-card-item">
                            <div class="card-info">
                                    <span class="logo">
                                        <img src="${(bank.bankInfo.logo)!}" width="130" height="40" alt="">
                                    </span>
                                <span class="f-right title">[#if bank.type=='debit']储蓄卡[#elseif bank.type=='credit']
                                    信用卡[/#if]</span>
                            </div>
                            <div class="card-num">**** ****
                                **** ${bank.cardNo?substring(bank.cardNo?length-4)}</div>
                            <a class="delete" href="javascript:;" onclick="deleteBank(${bank.id})">删除此卡</a>
                        </div>
                    [/#list]
                    </div>
                    <div class="my-bank-card-hd">
                        添加银行卡
                    </div>
                    <div class="account-pay">
                        <div class="input deposit">
                            <form id="inputForm" name="inputForm"
                                  action="${base}/b2c/member/wallet/bank/saveMemberBank.jhtml" method="post">
                                <input type="hidden" name="type" value="recharge">
                                <input type="hidden" name="paymentPluginId" value="cmbcPlugin">
                                <table class="input">
                                    <tr>
                                        <th>
                                            选择银行：
                                        </th>
                                        <td>
                                            <input id="bank" name="bank" type="hidden" value="308584000013">
                                            <input id="bankname" name="bankname" type="hidden" value="招商银行">
                                            <select id="parentId" name="parentId" style="width:190px"
                                                    class="valid">
                                                <option value="308584000013" selected="selected">招商银行</option>
                                                <option value="102100099996">工商银行</option>
                                                <option value="309391000011">兴业银行</option>
                                                <option value="301290000007">交通银行</option>
                                                <option value="105100000017">建设银行</option>
                                                <option value="302100011000">中信银行</option>
                                                <option value="303100000006">光大银行</option>
                                                <option value="305100000013">民生银行</option>
                                                <option value="103100000026">农业银行</option>
                                                <option value="104100000004">中国银行</option>
                                                <option value="310290000013">浦发银行</option>
                                                <option value="306581000003">广发银行</option>
                                                <option value="304100040000">华夏银行</option>
                                                <option value="403100000004">邮储银行</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>
                                            卡号：
                                        </th>
                                        <td>
                                            <input type="text" id="account" name="account" class="text">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>
                                            姓名:
                                        </th>
                                        <td>
                                        ${member.name}
                                            <input class="text" style="width:300px;" name="payer" maxlength="255"
                                                   value="${member.name}" type="hidden">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>
                                            身份证号:
                                        </th>
                                        <td>
                                        [#if idcard??&&idcard?has_content]
                                        ${idcard.no}
                                            <input class="text" style="width:300px;" name="no" maxlength="255"
                                                   value="${idcard.no}" type="hidden">
                                        [/#if]
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>手机号:</th>
                                        <td>
                                            <input type="hidden" name="mobile" id="mobile" class="text" maxlength="13"
                                                   value="${member.mobile}">
                                            <span name="mobile">${mosaic(member.mobile, 3, "~~~")}</span>
                                            <input type="button" id="sendCode" onclick="getCode()" value="获取验证码">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>校验码:</th>
                                        <td><input type="text" name="securityCode" id="securityCode" class="text"
                                                   maxlength="20"></td>
                                    </tr>
                                    <tr>
                                        <th>&nbsp;</th>
                                        <td>
                                            <input type="submit" id="submit" class="button" value="添&nbsp;&nbsp;加">
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div class="intro">相关说明：
                            <ol>
                                <li>添加银行卡的开户名必须为本人实名认证的姓名，否则将不能提现。</li>
                                <li>由于各银行入账时间和方式不同，请在预计到账时间前后先查询余额恢复和到账记录情况。</li>
                                <li>请仔细阅读汇款到账时间说明，如果由于预计到账时间超过汇款日期限、汇款信息填写错误或者超过银行的汇款限额等造成延误产生的损失，概不负责。</li>
                                <li>如出现汇款异常情况，失败汇款订单，会在最迟3-5个工作日内核实并将资金退回账户内。</li>
                                <li>银行汇款一旦成功提交，即表示已提交至银行处理，不可撤回和取消，敬请谅解。</li>
                                <li>平台发送的提交成功短信仅作提示使用，最终到账结果请以银行入账为准。</li>
                                <li>每天23点至1点前后银行系统结算时间，可能会影响到账时间，请注意申请汇款时间。</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
[#include "/b2c/include/slogen.ftl"]
</div>


<!--底部 -->
[#include "/b2c/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
