/*
 * 视频观看点实例videoPoint
 * 视频是 基于视频窗口videowin
 * 视窗点包括视频窗口videowin以及她包含的插件（如：MvPlayer）
 * darell.wu 
 * create 20130116 8:57
 */
var videoPoint = (function() {
	
	var MV = mvPlayer;
	
	var EVENT = {
		ACTIVECLICK : "ActiveClick",	//单击
		ACTIVEDBLCLICK : "ActiveDblClick",//双击
		LAYOUTCHANGE : "LayoutChanged",		
		ACTIVENETBROKEN : "ActiveNetBroken"//断网
	};
	
	// 视频捕捉保存路径
	var savePath = "c:\\";

	// variable
	var init = false;
	
	var targetVideoPoint;
	var targetRow;
	var targetCol;

	var DEFAULT_ROW = 0;

	var SCROLL_WIN = {
		WIDTH : 105,
		HEIGHT : 95
	};

	var CSS = {
		SELECTED : "selectedActive",
		NORMAL : "normalActive",
		CAMERA : "cameraId",
		SAVEPATH : 'maplayer_savepath'
	};
	
	// 返回视点包含Plugin标识id
	function getVideoPointPluginId(row, col) {
		var mvId = MV.getMv().ID;
		return mvId.replace("$[ID]", row + "_" + col);
	}

	// 返回视点Plugin对象
	function getVideoPointPlugin(row, col) {
		return eval("document.all." + getVideoPointPluginId(row, col));
	}
	
	// 设置视频点插件和尺寸
	function setVideoPointPluginSize(row, col, _w, _h) {
		var plugin = getVideoPointPlugin(row, col);
		MV.setPlugin(plugin);

		MV.setLocation(row, col);
		MV.save(savePath);
			
		MV.addEvent(MV.getMv().EVENT.CLICK, function() {
			var spec_eight = $(".video-f").is(":hidden");
			//八屏才进行切换
			if(spec_eight){
				
				$(".e-big").removeClass("current-video");
				$(".inner").removeClass("current-video");
				$("#spec8_"+plugin.x+"_"+plugin.y).addClass("current-video");
				swapVideoPoint(plugin.x, plugin.y);
			}else{
				$(".video-f ul li").removeClass("current-video");
				$("#spec4_"+plugin.x+"_"+plugin.y).addClass("current-video");
				videoPointTarget(plugin.x, plugin.y);
				
			}
		});
		MV.addEvent(MV.getMv().EVENT.NETBROKEN, function() {
			apiUtil.notifyEvent(EVENT.ACTIVENETBROKEN, plugin);
		});

		MV.setSize(_w, _h);
	}
			
	// 互换视窗
	function swapVideoPoint(_row, _col) {
		videoPointTarget(_row, _col);
		if (_row == DEFAULT_ROW && _col == 0) { return ; }

		var videoPoint = getVideoPointPlugin(0, 0);
		var _videoPoint = getVideoPointPlugin(_row, _col);
		$("#spec8_0_0").children().remove();
		$("#spec8_0_0").append(_videoPoint);

		$("#spec8_"+_row+"_"+_col).children().remove();
		$("#spec8_"+_row+"_"+_col).append(videoPoint);
		
		var bodyHeight =$(window).height();
		var bodyWidth =$(window).width();  
		var videoWidth = bodyWidth-$(".node-list").width()-$(".contral-list").width();
		var videoWidth_e_small = (videoWidth-8)/4;//8屏中小个单屏的宽度
		var videoHeight_e_small = (bodyHeight-8)/4;//8屏中小个单屏的高度
		var videoWidth_e_big = (videoWidth-4)*3/4;//8屏中大个单屏的宽度
		var videoHeight_e_big = (bodyHeight-4)*3/4;//8屏中大个单屏的高度
		
		// 置换视频尺寸
		MV.setPlugin(videoPoint);
		MV.setSize(videoWidth_e_small,videoHeight_e_small);

		MV.setPlugin(_videoPoint);
		MV.setSize(videoWidth_e_big, videoHeight_e_big);

		var TARGET_ID = "id";
		var TARGET_ATTR_1 = "x";
		var TARGET_ATTR_2 = "y";
		
		$(_videoPoint).attr(TARGET_ID, getVideoPointPluginId(DEFAULT_ROW, 0));
		$(_videoPoint).attr(TARGET_ATTR_1, DEFAULT_ROW);
		$(_videoPoint).attr(TARGET_ATTR_2, 0);
				
		$(videoPoint).attr(TARGET_ID, getVideoPointPluginId(_row, _col));
		$(videoPoint).attr(TARGET_ATTR_1, _row);
		$(videoPoint).attr(TARGET_ATTR_2, _col);
	}
	
	// 插件被选中时调用此方法
	function videoPointTarget(_row, _col) {
		
		if (targetRow == _row && targetCol == _col) { return ; }
		if (targetVideoPoint) {
			//targetVideoPoint.removeClass(CSS.SELECTED);
			//targetVideoPoint.addClass(CSS.NORMAL);
		}
		
		targetVideoPoint = eval("document.all." + "mvplayer_"+_row+"_"+_col);
		targetRow = _row;
		targetCol = _col;
		
		//targetVideoPoint.removeClass(CSS.NORMAL);
		//targetVideoPoint.addClass(CSS.SELECTED);
		
		apiUtil.notifyEvent(EVENT.ACTIVECLICK, {
			row : _row, col : _col
		});
	}
	
	function getRegisterCamera(camera) {
		var cameraId = "";
		if (camera && camera.cameraId != undefined && camera.cameraId != null) {
			cameraId = camera.cameraId;
		}
		var plugin_obj = "object[" + CSS.CAMERA + "='" + cameraId + "']";
		return $(plugin_obj);
	}

	// 插件plugin注册CAMERA
	function registerCamera(plugin, cameraId) {
		if (plugin && $(plugin)) {
			$(plugin).attr(CSS.CAMERA, cameraId);
			$(plugin).attr(CSS.CAMERA, cameraId);
		}
	}
	
	// 插件plugin重置CAMERA
	function resetCamera(plugin) {
		if (plugin && $(plugin)) {
			$(plugin).attr(CSS.CAMERA, "");
		}
	}
	
	function _init() {
		// 初始化视频观看点 仅能执行一次
		if (init) { return ; }
		
		//初始化8个控件
		for(var i=0;i<3;i++){
			for(var j=0;j<3;j++){
				var plugin_obj = MV.getMv().OBJ.replace("$[ID]", getVideoPointPluginId(i, j));
				$("#spec8_"+i+"_"+j).append(plugin_obj);
			}
		}
		
		setVideoPointPluginSize(0, 0, 500, 300);
		setVideoPointPluginSize(0, 1, 200, 150);
		setVideoPointPluginSize(1, 0, 200, 150);
		setVideoPointPluginSize(1, 1, 200, 150);
		setVideoPointPluginSize(0, 2, 200, 150);
		setVideoPointPluginSize(1, 2, 200, 150);
		setVideoPointPluginSize(2, 0, 200, 150);
		setVideoPointPluginSize(2, 1, 200, 150);
//		
//		// 初始化主屏幕
//		var plugin_obj = MV.getMv().OBJ.replace("$[ID]", getVideoPointPluginId(DEFAULT_ROW, 0));
//		videowin.addMainWin(plugin_obj);
//		setVideoPointPluginSize(DEFAULT_ROW, 0, MV.getMv().SIZE.WIDTH, MV.getMv().SIZE.HEIGHT);
//		// 初始化 滚动小屏幕
//		var maxNum = videowin.getMaxNumVideoWin();
//		for (var i = 1; i <= maxNum; i++) {
//			plugin_obj = MV.getMv().OBJ.replace("$[ID]", getVideoPointPluginId(DEFAULT_ROW, i));
//			videowin.addScrollSmallWin(plugin_obj, i);
//			setVideoPointPluginSize(DEFAULT_ROW, i, SCROLL_WIN.WIDTH, SCROLL_WIN.HEIGHT);
//		}
//		
//		// 初始化 主屏幕事件
//		videowin.addMainWinEvent(swapVideoPoint);
//
//		// 初始化 滚动小屏幕事件
//		videowin.addScrollWinEvent();
//
//		// 初始化 工具栏事件
//		videoPointTarget(DEFAULT_ROW, 0);
		
		init = true;
	}

	return {
		getEvent : function() {
			return EVENT;
		},
		initVideowin : function() {
			_init();
		},
		setVideoPointPluginSize : function(row, col, width, height) {
			setVideoPointPluginSize(row, col, width, height);
		},
		// 目标即选中pointDiv
		getTargetViewPoint : function() {
			return vst.empty(targetVideoPoint) ? getVideoPointPlugin(0, 0) : targetVideoPoint;
		},
		// 目标即选中MV
		getTargetPlugin : function() {
			var _col = vst.empty(targetCol) ? 0 : targetCol;
			return getVideoPointPlugin(DEFAULT_ROW, _col);
		},
		getVideoPointPlugin : function(row, col) {
			return getVideoPointPlugin(row, col);
		},
		// 摄像头信息
		getTargetCameraInfo : function() {
			return videoApi.getActiveCameraInfo(this.getTargetPlugin());
		},
		isRegisterCamera : function(camera) {
			return getRegisterCamera(camera).length > 0 ? true : false;
		},
		getRegisterCamera : function(camera) {
			return getRegisterCamera(camera);
		},
		// 返回指定下标插件PLUGIN播放状态 -1表未加载 其它返回值同videoApi.getActiveCameraStatus
		// 控件的状态:0表已关闭,1表正在播放,2表正在连接服务器获取连接地址,-1表kj参数为空
		getVideoPointPluginStatus : function(row, col) {
			var plugin = getVideoPointPlugin(row, col);
			if (plugin) {
				return videoApi.getActiveCameraStatus(plugin);
			} else {
				var PARAM_NULL = -1;
				return PARAM_NULL;
			}
		},
		// 目标即选中MV的播放状态
		getTargetPluginStatus : function() {
			var _col = vst.empty(targetCol) ? 0 : targetCol;
			return this.getVideoPointPluginStatus(DEFAULT_ROW, _col);
		},
		// 设置视窗为选中状态
		doTarget : function(row, col) {
			videoPointTarget(row, col);
		},
		// 启动MVPlayer
		// camera 启动摄像头信息，
		// 形如：{cameraId,cameraName,delayTime,controlType},其中属性delayTime,controlType可选
		startPlugin : function(row, col, camera) {
			var _plugin = getVideoPointPlugin(row, col);
			// 启动jQuery同步
			$.ajaxSetup({async:false});
			videoApi.startCamera({
				plugin : _plugin,
				password : "",
				cameraId : camera.cameraId,
				cameraName : camera.cameraName,
				controlType : camera.controlType,
				serverIP : camera.serverIp,
				delayTime : camera.delayTime,
				status : camera.status,
				nodeId : camera.nodeId
			});
			// 恢复jQuery异步
			$.ajaxSetup({async:true});
		},
		startPluginAndRegisterCamera : function(row, col, camera) {
			this.startPlugin(row, col, camera);
			var plugin = getVideoPointPlugin(row, col);
			registerCamera(plugin, camera.cameraId);
		},
		// 启动目标targetView中MVPlayer
		// camera 启动MVPlayer的摄像头信息 参考startVideo方法camera
		startPluginOnTargetView : function(camera) {
			var plugin = this.getTargetPlugin();
			var RUN = 1;
			if (plugin && videoApi.getActiveCameraStatus(plugin) == RUN) {
				vst.alert("兹视窗正在播放，请先关闭，谢谢。");
			} else {
				this.startPluginAndRegisterCamera(targetRow, targetCol, camera);
			}
		},
		// 启动多个MVPlayer
		// cameraList:数组camera，参考startMvPlayer方法camera
		startPluginList : function(cameraList) {
			if (!cameraList || !$.isArray(cameraList) || cameraList.length < 1) {
				return ;
			}
			// 计算已开启的视频数
			var openNum = 0;
			
//			
//			
//			var maxNum = videowin.getMaxNumVideoWin();
//			for (var i = 0; i <= maxNum; i++) {
//				if (videoPoint.getVideoPointPluginStatus(DEFAULT_ROW, i) > MV.getStatus().STOP) {
//					openNum++;
//				}
//			}
			
			
			for(var i=0;i<3;i++){
				for(var j=0;j<2;j++){
					
					if (videoPoint.getVideoPointPluginStatus(i, j) > MV.getStatus().STOP) {
						openNum++;
					}
				}
			}
				
			if (cameraList.length + openNum > 8) {
				vst.alert("敬告：您打开的视窗超过数量限制！");
				return ;
			}

			var num = 0;
			var col = 0;
			while (num < cameraList.length && col <= 8) {
				if (this.isRegisterCamera(cameraList[num])) {
					num++;
					continue;
				}

				var status = this.getVideoPointPluginStatus(DEFAULT_ROW, col);
				
				if (status > MV.getStatus().STOP) {
					col++;
					continue;
				} else {
					this.startPluginAndRegisterCamera(DEFAULT_ROW, col++, cameraList[num++]);
				}
			}
		},
		// 关闭所有视窗
		stopAllPlugin : function() {
//			var maxNum = videowin.getMaxNumVideoWin();
//			for (var i = 0; i <= maxNum; i++) {
//				var plugin = getVideoPointPlugin(DEFAULT_ROW, i);
//				MV.setPlugin(plugin);
//				MV.stop();
//				
//				// 重置摄像头设置
//				resetCamera(MV.getPlugin());
//			}
			
			//初始化8个控件
			for(var i=0;i<3;i++){
				for(var j=0;j<2;j++){
					var plugin = getVideoPointPlugin(i, j);
					MV.setPlugin(plugin);
					MV.stop();
					// 重置摄像头设置
					resetCamera(MV.getPlugin());
				}
			}
		},
		// 关闭当前视窗
		stopPlugin : function() {
			//var plugin = this.getTargetPlugin();
			var plugin = getVideoPointPlugin(DEFAULT_ROW, 0);
			if (plugin) {
				MV.setPlugin(plugin);
				MV.stop();
			}
		},
		// 关闭当前视窗并重置摄像头注册
		stopMvPlayerAndResetCamera : function() {
			this.stopMvPlayer();
			
			var plugin = this.getTargetPlugin();
			resetCamera(plugin);
		},
		// 抓图当前视窗
		capPlugin : function() {
			var target;
			var plugin = getVideoPointPlugin(DEFAULT_ROW, 0);
			if (plugin) {
				MV.setPlugin(plugin);
				target = MV.cap();
			}
			return target;
		},
		// 回放当前视窗
		backplayPlugin : function() {
			
			//获取当前选择的控件
			var kj= this.getTargetViewPoint();
			if(kj!=null){
			 	if(kj.IsStart()==1){
			 		kj.OnMenuPlay();
			 	}
			}
		},
		// 返回 空闲Mv所在下标 返回对象{ row:_row, col:_col }
		findIdleVideoPoint : function() {
			var result, count = 0;
			var maxNum = videowin.getMaxNumVideoWin();
			for (var i = 0; i <= maxNum; i++) {
				count++;
				if (videoPoint.getVideoPointPluginStatus(DEFAULT_ROW, i) <= MV.getStatus().STOP) {
					result = {
						row : DEFAULT_ROW,
						col : i,
						count : count
					};
					break;
				}
			}
			return result;
		},
		// 设置抓图保存的文件夹路径
		setSavePath : function(path) {
			if (!path || path == savePath) {
				return ;
			}
			if (!/[A-Za-z]:[^\?\/\*\|<>]+$/.test(path)) {
				return ;
			}
			savePath = path;
			// 保存5000个小时
			$.cookies.set(CSS.SAVEPATH, savePath, { hoursToLive: 5000 });
			
			var maxNum = videowin.getMaxNumVideoWin();
			for (var i = 0; i <= maxNum; i++) {
				var plugin = getVideoPointPlugin(DEFAULT_ROW, i);
				MV.setPlugin(plugin);
				MV.save(savePath);
			}
		}
	};
})();