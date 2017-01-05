<!doctype html>
<html>
<head>
    [#include "/wap/include/resource.ftl"]
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <title>个人中心</title>
    <style>
        #wrapper {
        }
    </style>
    <script>
        $(function () {
	setTimeout(function(){
	  init();
  },200);
        
        	[#if browse_version=="MicroMessenger"]
	$("#scan_fun").on("click",function(){
		$.ajax({
			url:"${base}/wap/mutual/get_config.jhtml",
			data:{
				url:location.href.split('#')[0]
			},
			dataType:"json",
			type:"get",
			success:function(message){
				if(message.type=="success"){
					var data=JSON.parse(message.content);
					wx.config({
					    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
					    appId: data.appId, // 必填，公众号的唯一标识
					    timestamp: data.timestamp, // 必填，生成签名的时间戳
					    nonceStr: data.nonceStr, // 必填，生成签名的随机串
					    signature: data.signature,// 必填，签名，见附录1
					    jsApiList: ["scanQRCode"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
					});
					wx.ready(function(){
						wx.scanQRCode({
					      desc: '扫一扫'
					    });
					});
					wx.error(function(res){
					    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
					});
					wx.checkJsApi({
					    jsApiList: ['scanQRCode'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
					    success: function(res) {
					        // 以键值对的形式返回，可用的api值true，不可用为false
					        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
					    }   
				    });
				}else{
					invokTips("error",message.content);
				}
			}
		});
});
[#else]
	$("#scan_fun").on("click",function(){
		location.href="vsstoo://scanFunc";return false;
	});
	[/#if]
        
        });

    </script>
</head>

<body>
[#include "/wap/include/static_resource.ftl"]

<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="${base}/wap/index.jhtml" class="">
            <i class="am-icon-home am-icon-md"></i> </a></div>
        <h1 class="am-header-title">个人中心</h1>
      </header>
    </header>
    <div id="wrapper" class="am-padding-horizontal-0 " style="background: #f7f7f7">
        <div>
            <div class="am-g wap-member-header-top" >
                <div>
                    <div class="am-u-sm-4 am-padding-top">
                    <a href="info.jhtml">
                        <img src="${base}/resources/weixin/images/header.png"
                           data-original="${member.headImg}"  class="am-img-thumbnail am-circle lazy">
                           </a>
                    </div>
                    <div class=" am-padding-top">
                       <a style="color:white;" href="info.jhtml"> <p class="am-margin-bottom-0" style="font-size:1.8rem; font-weight:bold;">${member.username}</p></a>

                        <p class="am-margin-vertical-0">${member.memberRank.name}</p>

                        <div>
                            <p class="am-margin-vertical-0">账户余额
                                <span class="wap-member-balance" >${member.balance}</span>
                                元</p>
                        </div>
                    </div>
                </div>
                <div class="am-margin-top-sm wap-member-header" >
                    <ul class="am-avg-sm-2 am-text-center" >
                        <li><a>优惠券&nbsp;&nbsp;${member.couponCodes.size()}</a></li>
                         <li ><a>积分&nbsp;&nbsp;${member.point}</a></li>
                    </ul>
                </div>
            </div>
            <div class="am-g">
                <div class="am-margin-top-xs  wap-member-money"  >
                    <ul class="am-avg-sm-2 am-text-center" >
                        <li><a href="${base}/wap/member/order/recharge.jhtml"><img class="am-img-thumbnail" src="${base}/resources/wap/image/recharge.png">账号充值</a></li>
                        <li ><a href="${base}/wap/member/cash/index.jhtml"><img class="am-img-thumbnail" src="${base}/resources/wap/image/deposit.png">账户提现</a></li>
                    </ul>
                </div>
            </div>
            <div class="am-g am-margin-top-sm wap-member-menulist">
                <div>
                    <ul class="am-list">
                        <li class=" am-padding-left-sm"><a href="${base}/wap/member/order/list.jhtml" class=""><img class="am-img-thumbnail " src="${base}/resources/wap/image/myorder.png"><span class="am-margin-left-sm">我的订单</span> </a></li>
                        <!--<li class=" am-padding-left-sm"><a class=""><img class="am-img-thumbnail " src="${base}/resources/wap/image/mymessage.png"><span class="am-margin-left-sm">我的消息</span> </a></li> -->
                        <li class=" am-padding-left-sm"><a href="${base}/wap/member/favorite/list.jhtml" class=""><img class="am-img-thumbnail " src="${base}/resources/wap/image/mycard.png"><span class="am-margin-left-sm">我的收藏</span> </a></li>
                        <li class=" am-padding-left-sm"><a href="${base}/wap/member/receiver/list.jhtml" class=""><img class="am-img-thumbnail " src="${base}/resources/wap/image/mycard.png"><span class="am-margin-left-sm">管理收货地址</span> </a></li>
                        <li class=" am-padding-left-sm"><a href="${base}/wap/member/deposit/list.jhtml" class=""><img class="am-img-thumbnail " src="${base}/resources/wap/image/accountdetails.png"><span class="am-margin-left-sm">我的账单</span> </a></li>
                        <li class=" am-padding-left-sm"><a href="javascript:;" id="scan_fun"><img class="am-img-thumbnail " src="${base}/resources/wap/image/accountdetails.png"><span class="am-margin-left-sm">扫一扫</span> </a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
[#if browse_version=="MicroMessenger"]
   [#include "/wap/include/footer.ftl" /]	
[/#if]	 
</div>
</body>
</html>
