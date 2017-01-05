<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
$(function(){
	[#if force]
	mtips("正在定位城市，请稍等。。");
	//分享
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
				    jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				});
				wx.ready(function(){
					wx.getLocation({
					    success: function (res) {
					        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
					        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
					        var speed = res.speed; // 速度，以米/每秒计
					        var accuracy = res.accuracy; // 位置精度
					        $.ajax({
					        	url:"${base}/ajax/lbs/update.jhtml",
					        	type:"get",
					        	dataType:"json",
					        	data:{
					        		lat:latitude,
					        		lng:longitude,
					        		force:true
					        	},
					        	success:function(message){
					        		if(message.type=="success"){
					        			location.reload();
					        		}
					        	}
					        });
					    }
					});
				});
				wx.error(function(res){
				    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
				});
				wx.checkJsApi({
				    jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
				    success: function(res) {
				        // 以键值对的形式返回，可用的api值true，不可用为false
				        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
				    }   
			    });
			}else{
				ptips("error",message.content);
			}
		}
	});
	[/#if]
});
</script>