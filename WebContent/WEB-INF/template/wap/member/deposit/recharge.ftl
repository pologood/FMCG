<!doctype html>
<html>
<head>
   	[#include "/wap/include/resource.ftl"]
   	<script src="${base}/resources/common/js/jquery.validate.js"></script>
<script src="${base}/resources/common/js/jquery-form.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <title>我的账单</title>
    <style>
		.recharge .balance{position: relative;
margin-top: 1rem;
text-align: right;
line-height: 45px;
background: #f7f7f7;}
		.recharge .balance .label_left{width: 20%;
margin-left: 1rem;
float: left;}
		.recharge .balance .label_right{width: 10%;
float: right;
margin-right: 2rem;}
		.recharge .balance span{color: #d4975c;
font-size: 1.5em;}
			
		.recharge .recharge_input{
line-height: 45px;
}
		.recharge .recharge_input input{width: 80%;
height: 45px;
border: none;
line-height: 45px;
font-weight: bolder;
text-align: right;
font-size: larger;
color: rgb(250, 40, 0);}
		.recharge .recharge_input label{width: 10%;
float: right;
}
		.recharge button.submit_btn {background: #FF0A0A;
border-radius: 5px;
width: 98%;}
			
    
    	.recharge_intro label{color: #383838;
font-size: x-small;
padding-left: 1rem;
margin-bottom: 0px;}
    	.recharge_intro ol{margin-top: 0px;
color: #383838;
font-size: x-small;}
    </style>
    <script>
        $(function () {
        	var $submit=$(":submit");
        	var $form=$("#rechargeForm");
        	$form.validate({
                rules: {
                    amount:{
                    	required:true,
                    	number:true
                    }, 
                    type: "required",
                },
                messages:{
                	amount:{
                    	required:"必填",
                    	number:"请输入数字"
                    }
                },
              	submitHandler:function(){
              		$submit.prop("disabled",true);
              		$form.ajaxSubmit(function(message_data){
              			if(message_data.message.type=="success"){
              				 $.ajax({
                                url: "${base}/wap/mutual/get_config.jhtml",
                                data: {
                                    url: location.href.split('#')[0]
                                },
                                dataType: "json",
                                type: "get",
                                success: function (message_wx) {
                                	var data = JSON.parse(message_wx.content);
                                    wx.config({
                                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                                        appId: data.appId, // 必填，公众号的唯一标识
                                        timestamp: data.timestamp, // 必填，生成签名的时间戳
                                        nonceStr: data.nonceStr, // 必填，生成签名的随机串
                                        signature: data.signature,// 必填，签名，见附录1
                                        jsApiList: ["chooseWXPay"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                                    });
                                    var jsonObject = JSON.parse(message_data.dataValue);
                                    //调用微信JS api 支付
                                    wx.chooseWXPay({
                                        timestamp: jsonObject.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                                        nonceStr: jsonObject.nonceStr, // 支付签名随机串，不长于 32 位
                                        package: jsonObject.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                                        signType: jsonObject.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                                        paySign: jsonObject.paySign, // 支付签名
                                        success: function (res) {
                                            // 支付成功后的回调函数
                                            if ("chooseWXPay:ok" == res.errMsg) {
                                                invokTips("success", "支付成功！");
                                                setTimeout(function () {
                                                    location.href = "${base}/wap/member/index.jhtml";
                                                    return false;
                                                }, 3000);
                                            } else if ("chooseWXPay:cancel" == res.errMsg) {
                                                invokTips("warn", "支付取消！");
                                                setTimeout(function () {
                                                   	window.location.reload();
                                                    return false;
                                                }, 3000);
                                            } else {
                                                invokTips("error", "支付失败！");
                                                setTimeout(function () {
                                                    window.location.reload();
                                                    return false;
                                                }, 3000);
                                            }
                                            return false;
                                        }
                                    });
                                    wx.error(function (res) {
                                        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                                    });
                                    wx.checkJsApi({
                                        jsApiList: ['chooseWXPay'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                                        success: function (res) {
                                            // 以键值对的形式返回，可用的api值true，不可用为false
                                            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                                        }
                                    });
                                }
                              });  
              			}
              		});
              	}
            });    
              
        });
    </script>
     <title>账户充值</title>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">账户充值</h1>
    </header>
    <div class="am-g recharge">
        <form id="rechargeForm" action="${base}/wap/member/order/payment.jhtml" method="post"> 
        	<input type="hidden" name="type" value="recharge"/>
            <div class="balance" >
                <label class="label_left">账户余额</label>
                <span >${member.balance?string("0.00")}</span>
                <label class="label_right" >元</label>
            </div>
            <div class="recharge_input" >
                <input type="text"  placeholder="请输入充值金额" name="amount">
                <label >元</label>
            </div>
             <button type="submit" class="am-btn am-btn-danger am-btn-block am-center am-margin-top-sm submit_btn" >立即充值</button>
        </form>
    </div>
    <div class="am-g recharge_intro">
        <label>相关说明:</label>
        <ol >
            <li>使微信支付充值，不收取任何手续费，不需要开通网银即可使用。</li>
            <li>暂不支持“银行汇款”等线下方式进行充值。</li>
            <li>可使用信用卡充值、储蓄卡进行充值，单笔限额5000元。</li>
        </ol>
    </div>
</div>
</body>
</html>