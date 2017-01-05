var imgUrl = "http://www.10057.com/upload/image/201409/1b6a997e-6eba-46b2-9f53-f73bd8985cbe-thumbnail.jpg";
var lineLink = "http://www.baidu.com";
var descContent = '测试分享';
var shareTitle = '标题';
var appid = '';

function shareFriend(lineLink,imgUrl,shareTitle,descContent) {
	WeixinJSBridge.invoke('sendAppMessage', {
		"img_url" : imgUrl,
		"img_width" : "200",
		"img_height" : "200",
		"link" : lineLink,
		"desc" : descContent,
		"title" : shareTitle
	}, function(res) {
		//alert(res.err_msg);
	})
}
function shareTimeline(lineLink,imgUrl,shareTitle,descContent) {
	WeixinJSBridge.invoke('shareTimeline', {
		"img_url" : imgUrl,
		"img_width" : "200",
		"img_height" : "200",
		"link" : lineLink,
		"desc" : descContent,
		"title" : shareTitle
	}, function(res) {
		// _report('timeline', res.err_msg);
	});
}
function shareWeibo(lineLink,descContent) {
	WeixinJSBridge.invoke('shareWeibo', {
		"content" : descContent,
		"url" : lineLink,
	}, function(res) {
		// _report('weibo', res.err_msg);
	});
}
function addContact(username){
	alert(1);
	alert(username);
	WeixinJSBridge.invoke("addContact", {webtype: "1",username: username}, function(e) {
		if(e.err_msg == 'add_contact:added'){
			return true;
		}else if(e.err_msg == 'add_contact:ok'){
			location.href="${base}/weixin/login/index.jhtml";return false;
		}
		
	})
}

//当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
//document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	// 发送给好友
//	WeixinJSBridge.on('menu:share:appmessage', function(argv) {
//		shareFriend();
//	});
	// 分享到朋友圈
//	WeixinJSBridge.on('menu:share:timeline', function(argv) {
//		shareTimeline();
//	});
	// 分享到微博
//	WeixinJSBridge.on('menu:share:weibo', function(argv) {
//		shareWeibo();
//	});
//}, false);