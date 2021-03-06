﻿<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="" content="" />
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心" />
    <meta name="description" content="${setting.siteName}-会员中心" />
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/datautil.js"></script>
	<script type="text/javascript" src="${base}/resources/b2b/js/ePayBank.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/cart.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2b/css/credit.css" rel="stylesheet" type="text/css" />
  	<style type="text/css">
  		ol li{
  			line-height: 25px;
  		}
  	</style>
    <script type="text/javascript">
        $().ready(function(){
            $("#memberBankId").change(function (obj) {
                var val = $(this).val();
                var bankName=$(this).find("option:selected").attr("bankName");
                $("#bankname").val(bankName);
                $("#account").val(val);

                if(val.indexOf('jhtml')!=-1){

                        location.href=val;

                }
            });
        });
    </script>
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2b/include/topnav.ftl"]
</div>
<!--主页内容区start-->
<div class="paper">
    <!-- 会员中心头部-->
    [#include "/b2b/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
            [#include "/b2b/include/member_left.ftl"]
            <div class="content">
                <div class="mainbox apply portal_rpm_apply">
		            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
		                <div class="js-app-header title-wrap" id="app_0000000844">
		                    <dl class="app-info">
		                        <dt class="app-title" id="app_name">余额提现</dt>
		                        <dd class="app-status" id="app_add_status"></dd>
		                        <dd class="app-intro" id="app_desc">支持国内各大银行借记卡往来结算支付申请，快捷方便！</dd>
		                    </dl>
		                </div>
		            </div>
		            <div>
                        <form id="inputForm" name="inputForm" action="submit.jhtml" method="post">
                            <table class="input">
                                <tr>
                                    <th>选择银行卡：</th>
                                    <td>
                                        <select id="memberBankId" name="memberBankId" style="width:190px">
                                            <option value="#" selected="selected">请选择银行</option>
                                            <option value="${base}/b2b/member/wallet/bank/list.jhtml">--添加银行卡--</option>
                                        [#list options.keySet() as key]
                                            <option value="${key}" >${options.get(key)}</option>
                                        [/#list]
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>提现金额：</th>
                                    <td>
                                        <input type="text" id="amount" name="amount" class="text" maxlength="16"
                                               onpaste="return false;"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>&nbsp;</th>
                                    <td>
                                        <p><span class="mod-tips" id="repayment_amount_tips"
                                                 style="display:block;margin-left:0px"/>单笔金额不能超过50000元</span></p>
                                    </td>
                                </tr>
                                <tr>
                                    <th>手续费:</th>
                                    <td>
                                        <span id="fee" class="text-tips" style="font-color:00">￥0.00</span>
                                    </td>
                                </tr>
                                <tr>
                                    <th>&nbsp;</th>
                                    <td>
                                        <button type="submit" id="submit">立刻提现</button>
                                    </td>
                                </tr>
                            </table>
                        </form>
		            </div>
		            <div style="float:left;margin-left:25px;margin-top:25px;"><span style="font-size:14px;">相关说明：</span>
		                <ol style="font-size:14px;">
		                    <li>由于各银行入账时间和方式不同，请在预计到账时间前后先查询余额恢复和到账记录情况。</li>
		                    <li>请仔细阅读转入到账时间说明，如果由于预计到账时间超过转入日期限、转入信息填写错误或者超过银行的转入限额等造成延误产生的损失，概不负责。</li>
		                    <li>如出现转入异常情况，失败转入订单，会在最迟3-5个工作日内核实并将资金退回账户内。</li>
		                    <li>银行转入一旦成功提交，即表示已提交至银行处理，不可撤回和取消，敬请谅解。</li>
		                    <li>平台发送的提交成功短信仅作提示使用，最终到账结果请以银行入账为准。</li>
		                    <li>每天23点至1点前后银行系统结算时间，可能会影响到账时间，请注意申请转入时间。</li>
		                </ol>
		            </div>
		        </div>
		    </div>
        </div>
    </div>
    <iframe id="interest" name="interest" src="${base}/b2b/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
    [#include "/b2b/include/slogen.ftl"]
</div>

<script type="text/javascript">


    $(function(){
        var $inputForm = $("#inputForm");
        var $account = $("#account");
        var $amount = $("#amount");
        var $mobile = $("#mobile");
        var $payer = $("#payer");
        var $arrive_time_info = $("#arrive_time_info");
        var $submit = $("#submit");

        var $fee = $("#fee");
        var $method1 = $("#method1");
        var $method2 = $("#method2");
        var $method3 = $("#method3");
        var timeout;
        var curMethod = "fast";
        [@flash_message /]

        // 还款金额
        $amount.bind("input propertychange change", function(event) {
            if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
              if (checkValidAmount($amount.val()) ) {
                 $("#repayment_amount_tips").text("单笔金额不能超过50000元");
                 calculateFee();
              } else {
                 $("#repayment_amount_tips").text("为确保汇款成功，请正确填写支付金额");
              }
            }
        });
        // 账户姓名
        $payer.bind("input propertychange change", function(event) {
            if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                if (isValidName($payer.val())) {
                 $("#payer_err").hide();
                } else
                {
                 $("#payer_err").text("为确保汇款成功，请如实填写持卡人的中文姓名");
                 $("#payer_err").show();
                }
            }
        });
        // 手机号
        /*$mobile.bind("input propertychange change", function(event) {
            if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                if (checkIsMobile($mobile.val())) {
                 $("#mobile_err").hide();
                } else
                {
                 $("#mobile_err").text("为确保汇款成功，请填写正确的手机号码");
                 $("#mobile_err").show();
                }
            }
        });
        // 还款账户
        $account.bind("input propertychange change", function(event) {
            if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                if (checkBankId($account.val())) {
                 $("#account_err").hide();
                } else
                {
                 $("#account_err").text("为确保汇款成功，请正确填写收款方银行卡号");
                 $("#account_err").show();
                }
            }
        });*/
        $method1.bind("click", function() {
            curMethod = "immediately";
          $arrive_time_info.text("加急汇款：02:00-15:30提交申请2小时之内到账");
                calculateFee();
        });
        $method2.bind("click", function() {
            curMethod = "general";
          $arrive_time_info.text("普通汇款：申请提交后1个工作日内到账");
                calculateFee();
        });

        var stime = "1";

        // 表单验证
        // $inputForm.validate({
        //     rules: {
        //         account: {
        //                 required: true,
        //                 rangelength:[16,19]
        //             },
        //         payer: {required: true},
        //         mobile: {
        //             required: true,
        //             pattern: /^1[3|4|5|7|8][0-9]\d{8}$/
        //         },
        //         bank: {required: true},
        //         method: {required: true},
        //         amount: {
        //             required: true,
        //             positive: true,
        //             max:50000.00,
        //             min: 0,
        //             decimal: {
        //                 integer: 12,
        //                 fraction: ${setting.priceScale}
        //             }
        //         }
        //     },
        //     messages:{
        //         account: {
        //                 required:'必填',
        //                 rangelength:'请输入正确的银行卡卡号'
        //             },
        //         payer: '必填',
        //         mobile: {
        //                 required:'必填',
        //                 pattern:'请输入正确的手机号'
        //             },
        //         bank: '必填',
        //         method: '必填',
        //         amount: {
        //             required: '必填',
        //             positive: '只允许输入正数',
        //             max: '数额超出',
        //             min: '金额不能小于0',
        //             decimal: '金额格式错误'
        //         }
        //     },
        //     beforeSend: function() {
        //         $submit.prop("disabled", true);
        //     },
        //     success: function(message) {
        //         $submit.prop("disabled", false);
        //     }
        // });
		$inputForm.validate({
            rules: {
                amount: {
                    required: true,
                    positive: true,
                    max: 50000.00,
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                }

            },
            messages: {
                amount: {
                    required: '必填',
                    positive: '只允许输入正数',
                    max: '数额超出',
                    min: '金额不能小于0',
                    decimal: '金额格式错误'
                }
            },
            beforeSend: function () {
                $submit.prop("disabled", true);
            },
            success: function (message) {
                $submit.prop("disabled", false);
            },
            submitHandler: function (form) {
                //if(){
                    var bankName=$("#memberBankId").val();

                if(bankName=='#'){
                    $.message("warn","请选择银行卡号或者添加银行卡");
                    return false;
                }
                form.submit();

                //}

            }
        });

        // 计算支付手续费
        function calculateFee() {
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                if ($amount.val() > 0) {
                    $.ajax({
                        url: "calculate_fee.jhtml",
                        type: "POST",
                        data: {method:curMethod, amount: $amount.val()},
                        dataType: "json",
                        cache: false,
                        success: function(data) {
                            if (data.message.type == "success") {
                                if (data.fee > 0) {
                                    $fee.text(currency(data.fee, true));
                                } else {
                                  $fee.text("￥0.00");
                                }
                            } else {
                                $.message(data.message);
                            }
                        }
                    });
                    } else { $fee.text("￥0.00");}
            }, 500);
        }

        $("#credit_bank").bind("click", function () {
            $("#icon-dropdown").attr("class", "icon-dropdown icon-up");
            $("#credit_bank_wrapper").show();
        });

        $("#credit_bank_tag").bind("click", function () {
            $("#icon-dropdown").attr("class", "icon-dropdown");
            $("#credit_bank_wrapper").hide();
        });

    });
    function selectBank(bank) {
        var bankInfo = getBankInfo(bank);
        $("#credit_bank").html("<em class=\"bank-logo "+bankInfo.bankabbr+"\"/></em>"+bankInfo.bankname+"<ins class=\"icon-dropdown\" id=\"icon-dropdown\"></ins>");
        $("#bank").val(bank);
        $("#bankname").val(bankInfo.bankname);
        $("#credit_bank_wrapper").hide();
        $("#account").val("");
    }

</script>

<!--底部 -->
[#include "/b2b/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
