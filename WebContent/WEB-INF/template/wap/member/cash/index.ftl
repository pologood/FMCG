<!doctype html>
<html>
<head>
   	[#include "/wap/include/resource.ftl"]
   	<script src="${base}/resources/common/js/jquery.validate.js"></script>
<script src="${base}/resources/common/js/jquery-form.js"></script>
    <title>银行汇款</title>
    <style>
        .cash-form{}
        .cash-form div.am-form-group select{border-top: none;border-left:none;border-right: none;}
        .cash-form div.am-form-group input{ border-top: none;border-left:none;border-right: none}
        .recharge_intro label{color: #383838;
font-size: x-small;
padding-left: 1rem;
margin-bottom: 0px;}
    	.recharge_intro ol{margin-top: 0px;
color: #383838;
font-size: x-small;}
    </style>
    <script>
    	$(function(){
    		var $amount=$("input[name='amount']");
    		var $fee=$("#fee");
    		var $cashForm =$("#cashForm");
    		
    		$cashForm.validate({
                rules: {
                    bank:{
                    	required:true
                    }, 
                    account:{
                    	required:true,
                    	number:true
                    }, 
                    payer:{
                    	required:true
                    }, 
                    mobile:{
                    	required:true,
                    	number:true
                    }, 
                    amount:{
                    	required:true,
                    	number:true
                    }
                },
                messages:{
                	bank:{
                    	required:"必选"
                    },
                	account:{
                    	required:"必填",
                    	number:"请输入正确银行卡号"
                    },
                	mobile:{
                    	required:"必填",
                    	number:"请输入正确手机号"
                    },
                	payer:{
                    	required:"必填"
                    },
                	amount:{
                    	required:"必填",
                    	number:"请输入金额"
                    }
                },
              	submitHandler:function(form){
              		form.submit();
              	}
            }); 	
              	
             // 手续费金额
			$amount.bind("input propertychange change", function(event) {
				if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
				  if ($amount.val()!="") {
				     calculateFee();
				  } else {
				     $("#fee").text("为确保汇款成功，请正确填写支付金额");
				  }
				}
			});
    		
    		// 计算支付手续费
			function calculateFee() {
			    if ($amount.val() > 0) {
					$.ajax({
						url: "calculate_fee.jhtml",
						type: "POST",
						data: {method:"general", amount: $amount.val()},
						dataType: "json",
						cache: false,
						success: function(data) {
							if (data.message.type == "success") {
								if (data.fee > 0) {
									$fee.text("￥"+data.fee);
								} else {
								  $fee.text("￥0.00");
								}
							} 
						}
					}); 
				} else { 
					$fee.text("￥0.00");
				}
			}
    	});
    </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav wap-rebk"><a href="javascript:;" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
        <h1 class="am-header-title">银行汇款</h1>
    </header>
    <div class="am-g cash-form">
        <form id="cashForm" class="am-form"  action="${base}/wap/member/cash/submit.jhtml" method="post" data-am-validator>
            <input type="hidden" name="method" value="${method}"/>
            <div class="am-form-group am-margin-bottom-0">
                <select name="bank" required>
                    <option value="">选择银行卡</option>
                    [#list banks.keySet() as key]
	                    <option value="${key}">${banks.get(key)}</option>
                    [/#list]
                </select>
            </div>
            <div class="am-form-group am-margin-bottom-0">
                <input type="text" class="" name="account" placeholder="请输入银行卡号" required>
            </div>
            <div class="am-form-group am-margin-bottom-0">
                <input type="text" class="" name="payer" placeholder="请输入开户名" required>
            </div>
            <div class="am-form-group am-margin-bottom-0">
                <input type="text" class="" name="mobile" placeholder="请输入手机号" required>
            </div>
            <div class="am-form-group am-margin-bottom-0">
                <input type="text" class="" name="amount" placeholder="请输入汇款金额" required>
            </div>
            <p class="am-text-right am-margin-top-0 am-padding-right-sm" style="font-size: smaller;color: #666;"><span>手续费:</span><span id="fee" class="am-text-danger">￥0.00</span></p>
            <button class="am-btn am-btn-danger am-btn-block am-center am-margin-top-sm"
               style="background:#FF0A0A;border-radius: 5px;width: 98%">立即汇款</button>
        </form>
    </div>
    <div class="am-g recharge_intro">
        <label >相关说明:</label>
        <ol >
            <li>由于各银行入账时间和方式不同，请在预计到账时间前后先查询余额恢复和到账记录情况。</li>
            <li>请仔细阅读汇款到账时间说明，如果由于预计到账时间超过汇款日期限、汇款信息填写错误或者超过银行的汇款限额等造成延误产生的损失，概不负责。</li>
            <li>如出现汇款异常情况，失败汇款订单，会在最迟3-5个工作日内核实并将资金退回睿商圈账户内。</li>
            <li>银行汇款一旦成功提交，即表示已提交至银行处理，不可撤回和取消，敬请谅解。</li>
            <li>平台发送的提交成功短信仅作提示使用，最终到账结果请以银行入账为准。</li>
            <li>每天23点至1点前后银行系统结算时间，可能会影响到账时间，请注意申请汇款时间。</li>
        </ol>
    </div>
</div>
</body>
</html>