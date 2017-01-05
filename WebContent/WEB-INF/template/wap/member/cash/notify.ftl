<!DOCTYPE HTML>
<html lang="en">
<head>
 [#include "/wap/include/resource.ftl"]
</head>
<body>
<div class="page">
    <header class="am-topbar am-topbar-fixed-top">
        <header data-am-widget="header" class="am-header am-header-default">
            <div class="am-header-left am-header-nav"><a href="${base}/wap/index.jhtml" class="">
                <i class="am-icon-home am-icon-md"></i> </a></div>
            <h1 class="am-header-title">温馨提示</h1>
        </header>
    </header>
    <div class="am-g">
        <div class="am-text-center am-padding-top-lg">
        	[#if status.type != "error"]
	  		  	[#if credit.status == "wait"]
	  		  		<i class="am-icon-exclamation-circle am-icon-lg" style="color: rgb(145, 145, 145);"></i>
	  		  		 <h4 style="margin-top: 5px;">您的汇款申请已经提交成功,<br>请注意资金到账情况。</h4>
		  	  	[#elseif credit.status == "wait_success"]
	  		  		 <i class="am-icon-check-circle am-icon-lg" style="color: rgb(77, 155, 142);"></i>
	  		  		 <h4 style="margin-top: 5px;">您的汇款申请已经提交银行处理,<br>请注意资金到账情况。</h4>
		  	  	[#elseif credit.status == "success"]
	  		  		 <i class="am-icon-check-circle am-icon-lg" style="color: rgb(77, 155, 142);"></i>
	  		  		 <h4 style="margin-top: 5px;">您的汇款申请已经支付成功,<br>请注意资金到账情况。</h4>
			    	[#elseif credit.status == "wait_failure"]
			    	 <i class="am-icon-exclamation-triangle am-icon-lg" style="color: rgb(226, 71, 71)"></i>
	  		  		 <h4 style="margin-top: 5px;">您的汇款申请支付失败，<br>3个工作日内将退回账户</h4>
			    	[#elseif credit.status == "failure"]
			    	 <i class="am-icon-exclamation-triangle am-icon-lg" style="color: rgb(226, 71, 71)"></i>
	  		  		 <h4 style="margin-top: 5px;">您的汇款申请支付失败，<br>请重新填写</h4>
			    [/#if]
			[#else]
			    	 <i class="am-icon-exclamation-triangle am-icon-lg" style="color: rgb(226, 71, 71)"></i>
				<h4 style="margin-top: 5px;">您的汇款申请支付失败，<br>原因：${status.content}</h4>
			[/#if]
        </div>
    </div>
	 <div class="am-g">
        <div class="am-padding-horizontal-sm">
        	[#if credit??]
             <p class="am-margin-vertical-0">汇款账户:<strong class="am-padding-left-xs">${credit.account}</strong></p>
             <p class="am-margin-vertical-0">账户名称:<strong class="am-padding-left-xs">${credit.payer}</strong></p>
             <p class="am-margin-vertical-0">银行名称:<strong class="am-padding-left-xs"></strong></p>
            <p class="am-margin-vertical-0">汇款金额:<strong class="am-padding-left-xs" style="color: red;"><i
                    class="am-icon-cny"></i>${credit.amount}</strong></p>
            <p class="am-margin-vertical-0">手续费:<strong class="am-padding-left-xs" style="color: red;"><i
                    class="am-icon-cny"></i>${credit.fee}</strong></p>
            <p class="am-margin-vertical-0">返利金额:<strong class="am-padding-left-xs" style="color: red;"><i
                    class="am-icon-cny"></i>${credit.profitAmount}</strong></p>
             [/#if]
            <a href="${base}/wap/member/cash/index.jhtml" class="am-btn am-btn-default am-align-right">继续汇款</a>
        </div>
    </div>
 </div>   
</body>
</html>