<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="" content="" />
<title>${setting.siteName}-供应商-提现</title>
<meta name="keywords" content="${setting.siteName}-首页" />
<meta name="description" content="${setting.siteName}-首页" />
<script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/ePayBank.js"></script>
<link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
<link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/css/credit.css" rel="stylesheet" type="text/css" />
</head>
<style>
div.page-wrap .p-skip em {
    padding: 0 4px;
    font-size: 16px;
    line-height: 1.6;
}
div.page-wrap {
    margin: 0;
    float: right;
    padding-right:50px;
}
div.page-wrap .p-num a, div.page-wrap .p-num b {
    float: left;
    height: 24px;
    line-height: 24px;
    padding: 0 10px;
    font-size: 14px;
}
div.page-wrap .p-num a {
    color: #b0b0b0;
    border: 1px solid #d0d0d0;
    background-color: #fff;
}
div.page-wrap .p-skip .input-txt {
    float: left;
    width: 30px;
    height: 16px;
    margin: 0 3px;
    line-height: 16px;
    font-size: 14px;
    text-align: center;
    padding: 3px;
    color: #000;
    border: 1px solid #d0d0d0;
}
.intro {
    margin: 30px 0px 20px 10px; clear: both;
}
.intro ol {
    margin: 5px 0px 0px 5px; color: rgb(102, 102, 102); line-height: 1.5; padding-left: 20px;
}
.intro li {
    /* list-style: decimal; */
    /* padding: 2px 0px; */
}
table.input th {
    background-color: #F6F6F6;
}
.page-nav .links .on{
    background-color: #F6F6F6;
}
.imenu {
    width: 98%;
    height: 66px;
    margin-left: 1%;
    margin-right: 1%;
    background-color: black; 
    border-left:black; 
    border-right: black; 
}
.credit-other .credit-num-ipt {
    font: bold 22px/23px Arial, Helvetica, sans-serif;
}
.col-main{
    width:1124px;
}
</style>
<body class="bg-base">
<!-- left -->
[#include "/b2b/include/supplier_left.ftl"]
<div class="f-left rt">
    [#include "/b2b/include/supplier_header.ftl"]
    <div class="breadcrumbs">
        <ul class="breadcrumb">
            <p>当前位置：</p>
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>余额提现</li>
            
        </ul>
    </div>
    [#include "/b2b/include/supplier_top.ftl"]
    <div class="imenu">
        <div class="mainbox apply portal_rpm_apply">
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/b2b/images/app_cash.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">提现</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">支持国内各大银行借记卡往来结算支付申请，快捷方便！</dd>
                    </dl>
                </div>
            </div>
            <div>
                <form id="inputForm" action="${base}/b2b/member/cash/submit.jhtml" method="post">
                    <input id="method" name="method" type="hidden" value="fast"/>
                    <div class="activated-wrapper" id="activated_wrapper" style="display: block;">
                        <div class="ps-pn col-2-credit" id="credit_area">
                           <div class="col-main" id="credit_showcase_wrapper">
                                <div class="main-wrap credit-other" id="credit_showcase">
                                    <!-- <span id="credit_showcase_other" style="display: block;">
                                        <div class="hd"><h5 style="font-size:20px;">银行转入</h5></div>
                                        <ul style="margin-left: 55px;">
                                            <li style="margin-top: 10px;"> 
                                                <label style="width:110px">发卡银行：</label>
                                                <span class="select-bank" id="credit_bank" style="background:white;">
                                                    <em class="bank-logo cmb"/></em>招商银行<ins class="icon-dropdown" id="icon-dropdown"></ins> 
                                                </span>
                                                <input id="bank" name="bank" type="hidden" value="308584000013"/>
                                                <input id="bankname" name="bankname" type="hidden" value="招商银行"/>
                                            </li>
                                            <li>
                                                <label style="line-height: 26px;width:110px;" for="credit_num">银行卡号：</label>
                                                <input class="credit-num-ipt" id="account" name="account" style="ime-mode: disabled;height:24px;" type="text"/>
                                            </li>
                                            <li class="err" id="account_err"></li>
                                            <li>
                                                <label for="payer" style="width:110px">开户名：</label>
                                                <input id="payer" name="payer" type="text" style="height:24px;" />
                                            </li>
                                            <li class="err" id="payer_err"></li>
                                            <li>
                                                <label for="mobile" style="width:110px">手机号：</label>
                                                <input id="mobile" name="mobile" type="text" style="height:24px;"/>
                                            </li>
                                            <li class="err" id="mobile_err"></li>
                                        </ul>
                                    </span> -->
                                    <div class="repayment-amount" id="credit_amount" style="display: block;">
                                        <div class="repayment-padding" id="repayment-padding">
                                            <div class="clearfix">
                                                <label class="label" for="repayment_amount">提现金额：</label>
                                                    <div class="element">
                                                        <input class="text-tips" id="amount" name="amount" style="ime-mode: disabled;height:24px;" type="text" value=""/>
                                                        </br>
                                                        [#if tenant??]服务商：(${tenant.name})[/#if]
                                                        <!-- <p><span class="mod-tips" id="repayment_amount_tips" style="display:block;margin-left:0px"/>单笔金额不能超过50000元</span>
                                                        </p> -->
                                                    </div>
                                                </div>
                                                <!-- <div class="clearfix">
                                                    <label class="label" for="repayment_fee">手续费：</label><div class="element"><span id="fee" class="text-tips" style="font-color:00">￥0.00</span></div>
                                                </div> -->
                                                <p class="indent btn-area" id="pn_btn_repay">
                                                    <span class="btn-blue"> <a onclick="submit_withdraw()" style="border: 1px solid gray; padding: 5px 20px;">立即提现</button> </a>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- <div class="ps-wrapper ps-custom" id="credit_bank_wrapper" style="left: 166px; top:86px; width: 440px; height: auto; z-index: 1000;">
                                    <div class="ps-tag" id="credit_bank_tag" style="margin-left: 0px; z-index: 1002;"   ></div>
                                        <div class="ps-inner-c">
                                            <div class="ps-closer" id="credit_bank_closer" style="z-index: 1001;"></div>
                                                <div class="ps-content" id="credit_bank_content" style="">
                                                    <ul id="showbank">
                                                    <li class="bank-li"  onclick="selectBank(308584000013)">
                                                        <span class="bank-logo cmb" hideFocus="">招商银行</span>
                                                        <span>招商银行</span>
                                                    </li>
                                                    <li class="bank-li"  onclick="selectBank(102100099996)">
                                                        <span class="bank-logo icbc" hideFocus="">工商银行</span>
                                                        <span>工商银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(309391000011)">
                                                        <span class="bank-logo cib" hideFocus="">兴业银行</span>
                                                        <span>兴业银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(301290000007)">
                                                        <span class="bank-logo bankcomm" hideFocus="">交通银行</span>
                                                        <span>交通银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(105100000017)">
                                                        <span class="bank-logo ccb" hideFocus="">建设银行</span>
                                                        <span>建设银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(302100011000)">
                                                        <span class="bank-logo zxyh" hideFocus="">中信银行</span>
                                                        <span>中信银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(303100000006)">
                                                        <span class="bank-logo cebb" hideFocus="">光大银行</span>
                                                        <span>光大银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(305100000013)">
                                                        <span class="bank-logo cmbc" hideFocus="">民生银行</span>
                                                        <span>民生银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(103100000026)">
                                                        <span class="bank-logo abc" hideFocus="">农业银行</span>
                                                        <span>农业银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(104100000004)">
                                                        <span class="bank-logo bofc" hideFocus="">中国银行</span>
                                                        <span>中国银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(310290000013)">
                                                        <span class="bank-logo spdb" hideFocus="">浦发银行</span>
                                                        <span>浦发银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(306581000003)">
                                                        <span class="bank-logo gdb" hideFocus="">广发银行</span>
                                                        <span>广发银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(304100040000)">
                                                        <span class="bank-logo hxb" hideFocus="">华夏银行</span>
                                                        <span>华夏银行</span>
                                                     </li>
                                                     <li class="bank-li"  onclick="selectBank(403100000004)">
                                                        <span class="bank-logo psbc" hideFocus="">邮储银行</span>
                                                        <span>邮储银行</span>
                                                     </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div> -->
                            </div>
                        </div>
                    </div>        
                </form>
            </div>
            <div class="intro" id="intro">相关说明： 
                <ol>
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
<script>
    $(function(){
        // var $inputForm = $("#inputForm");
        // var $account = $("#account");
        // var $amount = $("#amount");
        // var $mobile = $("#mobile");
        // var $payer = $("#payer");
        // var $arrive_time_info = $("#arrive_time_info");
        // var $submit = $("#submit");
        
        // var $fee = $("#fee");
        // var $method1 = $("#method1");
        // var $method2 = $("#method2");
        // var $method3 = $("#method3");
        // var timeout;
        // var curMethod = "fast";
        [@flash_message /]
      
        // 还款金额
        // $amount.bind("input propertychange change", function(event) {
        //     if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
        //       if (checkValidAmount($amount.val()) ) {
        //          $("#repayment_amount_tips").text("单笔金额不能超过50000元");
        //          calculateFee();
        //       } else {
        //          $("#repayment_amount_tips").text("为确保汇款成功，请正确填写支付金额");
        //       }
        //     }
        // });
        // 账户姓名
        // $payer.bind("input propertychange change", function(event) {
        //     if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
        //         if (isValidName($payer.val())) {
        //          $("#payer_err").hide();
        //         } else
        //         {
        //          $("#payer_err").text("为确保汇款成功，请如实填写持卡人的中文姓名");
        //          $("#payer_err").show();
        //         }
        //     }
        // });
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
        // $method1.bind("click", function() {
        //     curMethod = "immediately";
        //   $arrive_time_info.text("加急汇款：02:00-15:30提交申请2小时之内到账");
        //         calculateFee();
        // });
        // $method2.bind("click", function() {
        //     curMethod = "general";
        //   $arrive_time_info.text("普通汇款：申请提交后1个工作日内到账");
        //         calculateFee();
        // });
        
        // var stime = "1";
        
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

        // 计算支付手续费
        // function calculateFee() {
        //     clearTimeout(timeout);
        //     timeout = setTimeout(function() {
        //         if ($amount.val() > 0) {
        //             $.ajax({
        //                 url: "calculate_fee.jhtml",
        //                 type: "POST",
        //                 data: {method:curMethod, amount: $amount.val()},
        //                 dataType: "json",
        //                 cache: false,
        //                 success: function(data) {
        //                     if (data.message.type == "success") {
        //                         if (data.fee > 0) {
        //                             $fee.text(currency(data.fee, true));
        //                         } else {
        //                           $fee.text("￥0.00");
        //                         }
        //                     } else {
        //                         $.message(data.message);
        //                     }
        //                 }
        //             }); 
        //             } else { $fee.text("￥0.00");}
        //     }, 500);
        // }
        
        // $("#credit_bank").bind("click", function () {
        //     $("#icon-dropdown").attr("class", "icon-dropdown icon-up");
        //     $("#credit_bank_wrapper").show();
        // });
                
        // $("#credit_bank_tag").bind("click", function () {
        //     $("#icon-dropdown").attr("class", "icon-dropdown");
        //     $("#credit_bank_wrapper").hide();
        // });
        
    });
    // function selectBank(bank) {
    //     var bankInfo = getBankInfo(bank);
    //     $("#credit_bank").html("<em class=\"bank-logo "+bankInfo.bankabbr+"\"/></em>"+bankInfo.bankname+"<ins class=\"icon-dropdown\" id=\"icon-dropdown\"></ins>");
    //     $("#bank").val(bank);
    //     $("#bankname").val(bankInfo.bankname);
    //     $("#credit_bank_wrapper").hide();
    //     $("#account").val("");
    // }
    function submit_withdraw(){
        if($("#amount").val()==""){
            $.message("warn","请输入提现金额");
            return;
        }
        $.ajax({
            url: "${base}/b2b/member/supplier/submit_withdraw.jhtml",
            type: "POST",
            data: {amount: $("#amount").val()},
            dataType: "json",
            success: function(data) {
                $.message(data);
                if (data.type == "success") {
                    setTimeout(function() {
                        location.href="${base}/b2b/member/supplier/withdraw_cash_settle_account.jhtml";
                    }, 1000);
                } 
            }
        });
    }
</script>
</div>
</body>
</html>
