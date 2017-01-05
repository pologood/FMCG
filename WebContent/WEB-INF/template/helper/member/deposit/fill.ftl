<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>账户充值[#if systemShowPowered][/#if]</title>
    <link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>

    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/amazeui.css">
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css">
    <link href="${base}/resources/helper/css/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/helper/css/account.css"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/ePayBank.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/search.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $amount = $("#amount");
            var $fee = $("#fee");
            var timeout;
            var basePath = "${base}";

        [@flash_message /]

            // 充值金额
            $amount.bind("input propertychange change", function (event) {
                if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                    //	calculateFee();
                }
            });

            // 计算支付手续费
            function calculateFee() {
                clearTimeout(timeout);
                timeout = setTimeout(function () {
                    if ($inputForm.valid()) {
                        $.ajax({
                            url: "calculate_fee.jhtml",
                            type: "POST",
                            data: {paymentPluginId: $("#paymentPluginId").val(), amount: $amount.val()},
                            dataType: "json",
                            cache: false,
                            success: function (data) {
                                if (data.message.type == "success") {
                                    if (data.fee > 0) {
                                        $fee.text(currency(data.fee, true)).closest("tr").show();
                                    } else {
                                        $fee.closest("tr").hide();
                                    }
                                } else {
                                    $.message(data.message);
                                }
                            }
                        });
                    } else {
                        $fee.closest("tr").hide();
                    }
                }, 500);
            }

            // 检查余额
            setInterval(function () {
                $.ajax({
                    url: "check_balance.jhtml",
                    type: "POST",
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        if (data.balance > ${member.balance}) {
                            location.href = "list.jhtml";
                        }
                    }
                });
            }, 10000);

            // 表单验证
            $inputForm.validate({
                rules: {
                    amount: {
                        required: true,
                        positive: true,
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
                        decimal: '数值超出了允许范围'
                    }
                },
                submitHandler: function (form) {
                    form.submit();
                }
            });

            var bankInfo = "<input type=\"hidden\" id=\"paymentPluginId\" name=\"paymentPluginId\" value=\"elinkBankPlugin\" />";
            for (var i = 0; i < bankInfoArray.length; i++) {
                if (bankInfoArray[i].flag.substr(0, 1) == "1") {
                    bankInfo += "<div>";
                    bankInfo += " <input id=\"" + bankInfoArray[i].bankCode + "\" name=\"bank\" type=\"radio\" value=\"" + bankInfoArray[i].bankCode + "\" style=\"vertical-align:top\" />"
                    bankInfo += " <label for=\"" + bankInfoArray[i].bankCode + "\"> <em title=\"" + bankInfoArray[i].bankname + "\" style=\"background-image: url(" + basePath + bankInfoArray[i].logo + ");\"></em>";
                    bankInfo += " </label>";
                    bankInfo += "</div>";
                }
            }

            $("#paymentPlugin").html(bankInfo);

            $("#paymentPlugin :radio").click(function () {
                bankInfo = getBankByCode($(this).val());
                $("#paymentPluginId").val(bankInfo.plugIns);
            });
            //==============微信充值==============
	        $("#submit").click(function(){
	            if($("#amount").val()==""){
	                $.message("warn","请输入充值金额");
	                return;
	            }
	            $.ajax({
	                url:'${base}/helper/payment/create.jhtml',
	                type:"POST",
	                data:{
	                    paymentPluginId:'weixinQrcodePayPlugin',
	                    amount:$("#amount").val(),
	                    type:"recharge"
	                },
	                dataType:"JSON",
	                success:function(data){
	                    if(data.message.type!="success"){
	                        $.message("error",data.message.content);
	                        return;
	                    }
	                    $("#qrcode").show();
	                    $.ajax({
	                        url:"http://api.wwei.cn/wwei.html?apikey=20160806215020&data="+data.data.code_url,
	                        type:'post',
	                        dataType:"jsonp",
	                        jsonp: "callback",
	                        success:function(result){
	                            $(document).unbind("ajaxBeforeSend");
	                            $("#weixin_qr").attr("src",result.data.qr_filepath);
	                            InterValObj=window.setInterval(function(){
	                                queryCash(data.data.sn);
	                            },3000);
	                        }
	                    });
	                }
	            });

	        });
	        $("#qrcode").click(function(){
	            if($("#qrcode").css("display")=="none"){
	                $("#qrcode").show();
	            }else{
	                $("#qrcode").hide();
	            }
	        });

        });
		/*微信支付*/
	    function queryCash(sn){
	        $.ajax({
	            url:'${base}/b2c/payment/weixin_payment.jhtml',
	            data:{
	                sn:sn
	            },
	            type:'post',
	            dataType:'json',
	            success:function(dataBlock){
	                // $("#qrcode").hide();
	                if(dataBlock.message.type=="success"){
                        $.message(dataBlock.message);
	                    window.clearInterval(InterValObj);
	                    location.reload();
	                }
	                if(dataBlock.message.type=="error"){
	                    window.clearInterval(InterValObj);
	                    $.message(dataBlock.message);
	                }
                    //$.message(dataBlock.message.type,dataBlock.message.content);
	            }
	        });
	    }
    </script>
</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">
    [#include "/helper/member/include/border.ftl" /]
    [#include "/helper/member/include/menu.ftl" /]
        <div class="wrapper" id="wrapper">
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/app_cash.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">账户充值</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">使用您已开通网上银行服务的银行卡进行充值。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">账户充值</a></li>
                    <li><a hideFocus="" href="${base}/helper/member/deposit/list.jhtml">我的账单</a></li>
                </ul>
            </div>
            <div class="input deposit">
                <form id="inputForm" action="${base}/helper/payment/submit.jhtml" method="post" target="_blank">
                    <input type="hidden" name="type" value="recharge"/>
                    <input type="hidden" name="paymentPluginId" value="cmbcPlugin"/>
                    <table class="input">
                        <tr>
                            <th>
                            ${message("shop.member.deposit.balance")}:
                            </th>
                            <td>
                            ${currency(member.balance, true, true)}
                            </td>
                        </tr>
                        <!--
                        <tr>
                            <th>
                                网银充值:
                            </th>
                            <td>
                                <div id="paymentPlugin" class="paymentPlugin clearfix">
                                    </div>
                                </div>
                            </td>
                        </tr>
                        -->
                        <tr>
                            <th>
                            ${message("shop.member.deposit.amount")}:
                            </th>
                            <td>
                                <input type="text" id="amount" name="amount" class="text" maxlength="16"
                                       onpaste="return false;"/>
                            </td>
                        </tr>
                        <tr class="hidden">
                            <th>
                                手续费:
                            </th>
                            <td>
                                <span id="fee"></span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <input type="button" class="button" value="${message("shop.member.submit")}" id="submit"/>
                                <input type="button" class="button" value="${message("shop.member.back")}"
                                       onclick="location.href='list.jhtml'"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="intro" id="intro">相关说明：
                <ol>
                    <li>支持使用信用卡、借记卡等充值，建议单笔金额不要超过5000元，大额分多笔充值。</li>
                    <li>充值金额不允许用于提取现金，充值金额提现将收取相关手续费，手续费不设上下限且没有免手续费优惠。</li>
                    <li>通过“银行汇款”和“签约账户”进行充值均不收取手续费。</li>
                    <li>银行汇款，请您在周一至周五的9：00-17：30选择“普通活期”存储方式完成柜台汇款，汇款 成功后1-2个工作日即可到账。</li>
                </ol>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
<div style="width:100%;height:100%;z-index:100;background-color:gray;position:fixed;top:0;filter: progid:DXImageTransform.Microsoft.gradient(enabled='true', startColorstr='#B2000000', endColorstr='#B2000000');background:rgba(0,0,0,0.7);display:none;" id="qrcode">
    <div style="margin:100px auto;width:300px;height:350px;background-color:white;padding-top:30px;">
        <div style="margin:auto;width:230px;height:230px;">
            <img src="${base}/resources/helper/img/code-1.png;" style="width:230px;height:230px;" id="weixin_qr">
        </div>
        <div style="height:50px;margin-top:20px;text-align:center;font-size:20px;color:black;padding:0px 20px">
            使用微信扫描上方二维码即可完成充值
        </div>
    </div>
</div>
</body>
</html>