<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="" content="" />
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心" />
    <meta name="description" content="${setting.siteName}-会员中心" />
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
    <style type="text/css">
    .cz_con{
        border-right:1px solid #e4e4e4;
        border-left:1px solid #e4e4e4;
        margin-bottom: 10px; 
        display: none;
    }
    table.input td{
        border-bottom: 1px solid #e4e4e4;
        color: red;
    }
    table.input th{
        background-color:white;
        border-bottom: 1px solid #e4e4e4;
    }
    </style>
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
                <!--我的钱包-->
                <div class="my-wallet">
                    <!--余额-->
                    <div class="assets">
                        <div class="dl wire">
                            <div class="dt">当前余额</div>
                            <div class="dd">
                                <span class="sum">￥${balance?string("0.00")}</span>
                                <a href="javascript:;" onclick="chongzhi_show(this)">充值</a>
                                [#if versionType==1]
                                    <a href="${base}/b2c/member/cash/index.jhtml" style="margin-left: 20px;">提现</a>
                                [#else]
                                    <span class="text">其中可提现金额：<em>${withdrawBalance?string("0.00")}</em>元</span>
                                [/#if]
                            </div>
                        </div>
                        <div class="cz_con">
                            <!-- <form id="inputForm" action="${base}/b2c/payment/submit.jhtml" method="post" target="_blank"> -->
                                <input type="hidden" name="type" value="recharge" />
                                <input type="hidden" name="paymentPluginId" value="balancePayPlugin"/>
                                <table class="input">
                                    <tr>
                                        <th>
                                            ${message("shop.member.deposit.balance")}: 
                                        </th>
                                        <td>
                                            ${currency(member.balance, true, true)}
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>
                                            ${message("shop.member.deposit.amount")}: 
                                        </th>
                                        <td>
                                            <input type="text" id="amount" name="amount" class="text" maxlength="16" onpaste="return false;" />
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
                                            <input type="button" class="button" value="${message("shop.member.submit")}" id="submit" />
                                            <input type="button" class="button" value="${message("shop.member.back")}" onclick="chongzhi_hide(this)" />
                                        </td>
                                    </tr>
                                </table>
                            <!-- </form> -->
                        </div>
                    [#if versionType!=1]
                        <div class="dl">
                            <div class="dt">可提金额</div>
                            <div class="dd">
                                <span class="sum">￥${withdrawBalance?string("0.00")}</span>
                                <a href="${base}/b2c/member/cash/index.jhtml" >提现</a>
                                <div class="ul">
                                    <div class="li wire">
                                        <span>进行中未到账：<em>￥${uncollected?string("0.00")}</em></span>
                                        <span class="title">2个工作日内到账，节假日顺延</span>
                                    </div>
                                    <div class="li">
                                        <span>冻结金额：<em>￥${freezeBalance?string("0.00")}</em></span>
                                        <span class="title">买家已支付，订单未完成</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    [/#if]
                    </div>
                    <!--默认银行卡-->
                    <div class="default-card">
                        <div class="default-card-hd">
                            已默认银行卡（<span>[#if memberBanks?size>0]1[#else]0[/#if]</span>张）
                        </div>
                        <div class="default-card-item">
                            [#if memberBanks?size>0]
                                <span class="logo">
                                    <img src="${(memberBanks[0].bankInfo.logo)!}" width="130" height="40" alt="">
                                </span>
                                <span class="card-no">尾号：<em>**** **** **** ${(memberBanks[0].cardNo?substring(memberBanks[0].cardNo?length-4))!'0000'}</em></span>
                                <a class="control" href="${base}/b2c/member/wallet/bank/list.jhtml">管理银行卡</a>
                            [#else]
                                <a class="control" style="padding-left: 0;" href="[#if idcard.authStatus=='success']${base}/b2c/member/wallet/bank/list.jhtml[#else]javascript:$.message('error','您还未实名认证');[/#if]">添加银行卡</a>
                            [/#if]
                        </div>
                    </div>
                    <div class="bill-record">
                        <iframe id="billIframe" name="billIframe" frameborder="0" src="${base}/b2c/member/wallet/bill/list.jhtml" width="100%" scrolling="no">
                        </iframe>
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

<script type="text/javascript">
    var billIframe = document.getElementById("billIframe");
    billIframe.onload = function () {
        $(".anchor", billIframe.contentWindow.document).click(function () {
            billIframe.onload = function () {
                var top=window.location.href;
                location.href = top.substring(0,top.indexOf("#"))+'#billIframe';
            }
        });
    };
    $(function(){
        var $inputForm = $("#inputForm");
        var $amount = $("#amount");
        var $fee = $("#fee");
        var timeout;
        var basePath = "${base}";
        
        // 充值金额
        $amount.bind("input propertychange change", function(event) {
            if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
            //  calculateFee();
            }
        });
        
        // 计算支付手续费
        function calculateFee() {
            clearTimeout(timeout);
            timeout = setTimeout(function() {
                if ($inputForm.valid()) {
                    $.ajax({
                        url: "calculate_fee.jhtml",
                        type: "POST",
                        data: {paymentPluginId: $("#paymentPluginId").val(), amount: $amount.val()},
                        dataType: "json",
                        cache: false,
                        success: function(data) {
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
        [#--setInterval(function() {--]
            [#--$.ajax({--]
                [#--url: "check_balance.jhtml",--]
                [#--type: "POST",--]
                [#--dataType: "json",--]
                [#--cache: false,--]
                [#--success: function(data) {--]
                    [#--if (data.balance > ${member.balance}) {--]
                        [#--location.href = "list.jhtml";--]
                    [#--}--]
                [#--}--]
            [#--});--]
        [#--}, 10000);--]
        
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
            messages :{
                amount:{
                    required: '必填',
                    positive: '只允许输入正数',
                    decimal: '数值超出了允许范围'
                }
            },
            submitHandler: function(form) {
                form.submit();
            }
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
    function chongzhi_hide(obj){
        $(".cz_con").hide();
    }
    function chongzhi_show(obj){
        if($(".cz_con").css("display")=="none"){
            $(".cz_con").show();
        }else{
            $(".cz_con").hide();
        }
        
    }
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
                    window.clearInterval(InterValObj);
                    location.reload();
                }
                if(dataBlock.message.type=="error"){
                    window.clearInterval(InterValObj);
                    $.message(dataBlock.message);
                }
            }
        });
    }
</script>

<!--底部 -->
[#include "/b2c/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>
<div style="width:100%;height:100%;z-index:1000;background-color:gray;position:fixed;top:0;filter: progid:DXImageTransform.Microsoft.gradient(enabled='true', startColorstr='#B2000000', endColorstr='#B2000000');background:rgba(0,0,0,0.7);display:none;" id="qrcode">
        <div style="margin:100px auto;width:300px;height:350px;background-color:white;padding-top:30px;">
            <div style="margin:auto;width:230px;height:230px;">
                <img src="" style="width:230px;height:230px;" id="weixin_qr">
            </div>
            <div style="height:50px;margin-top:20px;text-align:center;font-size:20px;color:black;padding:0px 20px">
                使用微信扫描上方二维码即可完成充值
            </div>
        </div>
    </div>
</body>
</html>
