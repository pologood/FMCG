/*
 * 云台控制和视频参数调节
 * darell.wu
 * rebuild 20120109 16:00
 */
videoApi.PTZ = (function() {
	
	var SPEEDTOP = 63;
	var SPEEDBOTTOM = 0;
	
	var _COMMOND = {
		BRIGHTNESS : 0,
		CONTRAST : 1,
		SATURATION : 2,	
		HUE : 3,
		ZOOM : 4,
		APERTURE : 5,
		FOCALDIS : 6,
		MOVE : 7
	};
	
	var SPEED = {
		DOWN : 1,
		UP : 2
	};
	
	var EVENT = {
		/*
		 * 云台转速变更事件，由videoApi.PTZ触发
		 * paramObj:{type:类型, 2提高转速, 1降低转速, speed:当前转速}
		 */
		SPEEDCHANGE : "speedChanged"	
	};
	
	//清录像缓存1.0.40版本之后的功能，之前是不支持该功能
//按下云台按钮触发
function startPTZCtrl(){
	var row = 0;
	var col = 0;
	if (videoPoint.getVideoPointPluginStatus(row, col) == mvPlayer.getStatus().RUN) {
		var videoPointPlugin = videoPoint.getVideoPointPlugin(row, col);
		//ptzFunc(power, cameraInfo.cameraId);
		try{
			if(!videoPointPlugin.lastTime){
				videoPointPlugin.startPTZCtrl();
			}
			videoPointPlugin.lastTime = new Date().getTime();
			videoPointPlugin.clearFlag = 0;
		}catch(e){}
	}
}
	
//释放云台按钮5秒钟之后触发
function stopPTZCtrl(){
	var row = 0;
	var col = 0;
	if (videoPoint.getVideoPointPluginStatus(row, col) == mvPlayer.getStatus().RUN) {
		var videoPointPlugin = videoPoint.getVideoPointPlugin(row, col);
		videoPointPlugin.clearFlag = 1;
		videoPointPlugin.lastTime = new Date().getTime();
		//间隔时间毫秒数
		var intervalTime = 10000;
		var clear = setInterval(function() {
			//获取当前摄像头控件
			try{
				if(videoPointPlugin.lastTime){
					var currentTime = new Date().getTime();
					if(currentTime-videoPointPlugin.lastTime>intervalTime&&videoPointPlugin.clearFlag){
						videoPointPlugin.stopPTZCtrl();
						clearInterval(clear);
						videoPointPlugin.lastTime = null;
						//alert('a');
					}
				}else{
					videoPointPlugin.lastTime = new Date().getTime();
				}
			}catch(e){}
		},1000);
	}
}
	
	
	return {
		// 云台控制用到的转速，值在0到63之间
		speedCount : 32,
		getEvent : function() {
			return EVENT;
		},
		// 发送云台控制命令
		sendPTZCtrlCmd : function(param, type, cameraId) {
			
			var _url = wordBook.getApiInfo().PARAMS.ptzCtrlURL.replace("$[METHOD]", "box/video/ptzctrl.jhtml");
			_url = _url.replace("$[RAND]", Math.random());
			_url = _url.replace("$[CAMERAID]", cameraId);
			_url = _url.replace("$[TYPE]", type);
			_url = _url.replace("$[PARAM]", param);
			_url = _url.replace("$[SPEED]", this.speedCount);
			apiUtil.getJSON(_url);
		},
		/*
		 * 移动（转动）云台
		 * 
		 * @param direct
		 *            方向：0:停止, 1:向上, 2：向下, 3:向左, 4:向右
		 */
		move : function(direct, cameraId) {
			if(direct == 0){
				stopPTZCtrl();
			}else{
				startPTZCtrl();
			}
			
			videoApi.PTZ.sendPTZCtrlCmd(direct, _COMMOND.MOVE, cameraId);
		},
		/*
		 * 缩放（变倍）
		 * 
		 * @param power
		 *            0:停止, 1:放大, 2:缩小
		 */
		zoom : function(power, cameraId) {
			
			if(power == 0){
				stopPTZCtrl();
			}else{
				startPTZCtrl();
			}
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.ZOOM, cameraId);
		},
		/*
		 * 光圈
		 * 
		 * @param power
		 *            0:停止, 1:放大, 2:缩小
		 */
		aperture : function(power, cameraId) {
			startPTZCtrl();
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.APERTURE, cameraId);
		},
		/*
		 * 焦距
		 * 
		 * @param power
		 *            0:停止, 1:放大, 2:缩小
		 */
		focalDis : function(power, cameraId) {
			if(direct == 0){
				stopPTZCtrl();
			}else{
				startPTZCtrl();
			}
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.FOCALDIS, cameraId);
		},
		/*
		 * 亮度
		 * 
		 * @param power
		 *            1:增加, 2：减少
		 */
		brightness : function(power, cameraId) {
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.BRIGHTNESS, cameraId);
		},
		/*
		 * 对比度
		 * 
		 * @param power
		 *            1:增加, 2：减少
		 */
		contrast : function(power, cameraId) {
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.CONTRAST, cameraId);
		},
		/*
		 * 饱和度
		 * 
		 * @param power
		 *            1:增加, 2：减少
		 */
		saturation : function(power, cameraId) {
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.SATURATION, cameraId);
		},
		/*
		 * 色调
		 * 
		 * @param power
		 *            1:增加, 2：减少
		 */
		hue : function(power, cameraId) {
			videoApi.PTZ.sendPTZCtrlCmd(power, _COMMOND.HUE, cameraId);
		},
		/*
		 * 转速
		 * 
		 * @param power
		 *            1:降低, 2:提高
		 */
		speed : function(power) {
			startPTZCtrl();
			switch (power) {
			case SPEED.UP :
				videoApi.PTZ.speedUp();
				break;
			case SPEED.DOWN :
				videoApi.PTZ.speedDown();
				break;
			default :
				vst.alert("转速参数出错power" + power);
				break;
			}
		},
		// 提高转速
		speedUp : function() {
			var step = 4;
			videoApi.PTZ.speedCount = videoApi.PTZ.speedCount + step;
			if (videoApi.PTZ.speedCount > SPEEDTOP) {
				videoApi.PTZ.speedCount = SPEEDTOP;
			}
			apiUtil.notifyEvent(EVENT.SPEEDCHANGE, {
				type : SPEED.UP,
				speed : videoApi.PTZ.speedCount
			});
		},
		// 降低转速
		speedDown : function() {
			if (videoApi.PTZ.speedCount == SPEEDTOP) {
				videoApi.PTZ.speedCount = SPEEDTOP + 1;
			}

			var step = 4;
			videoApi.PTZ.speedCount = videoApi.PTZ.speedCount - step;

			if (videoApi.PTZ.speedCount < SPEEDBOTTOM) {
				videoApi.PTZ.speedCount = SPEEDBOTTOM;
			}
			apiUtil.notifyEvent(EVENT.SPEEDCHANGE, {
				type : SPEED.DOWN,
				speed : videoApi.PTZ.speedCount
			});
		},
		// 速度到显示的百分比文本值
		speedToPercent : function(speed) {
			var percent;
			if (speed == SPEEDTOP) {
				percent = '+100%';
			}
			else if (speed == 32) {
				percent = '0%';
			}
			else if (speed > 32) {
				percent = '+' + ((videoApi.PTZ.speedCount - 32) / 4) * 12.5 + '%';
			}
			else {
				percent = '-' + ((32 - videoApi.PTZ.speedCount) / 4) * 12.5 + '%';
			}
			return percent;
		},
		//开启声音
		voiceUp : function(){
			var kj=null;
			//先查看当前选中的控件是否有视频
			kj = videoPoint.getTargetViewPoint();
			if(kj!=null){
			 	if(kj.IsStart()!=0){
			 		var result = kj.OpenAudio();
			 	}
			}
		},
		//关闭声音
		voiceDown : function(){
			var kj=null;
			//先查看当前选中的控件是否有视频
			kj = videoPoint.getTargetViewPoint();
			if(kj!=null){
			 	if(kj.IsStart()!=0){
			 		var result = kj.StopAudio();
			 	}
			}
		}
	};
})();