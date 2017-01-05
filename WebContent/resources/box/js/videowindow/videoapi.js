/*
 * videoApi core
 * 视频API
 * darell.wu
 * rebuild 20120109 16:00
 */
var videoApi = (function() {

	var EVENT = {
		CHECKVERSION : "checkVersionOver",
		VIDEOSTART : "VideoStarted"
	};
	
	var LEVEL = {
		INFO : "info",
		WARN : "warn",
		ERROR : "error"
	};
	
	/*
	 * 浏览器检查结果
	 * 默认0表示未检查
	 */
	var browserFlag = 0;
	
	// 默认不存在
	var pluginFlag = -1;
	
	// video插件版本号
	var pluginVersion = "";
	
	var BROWSER = wordBook.getBrowser();
	
	/*
	 * 检查浏览器
	 * 
	 * @return 1:浏览器可支持播放
	 *         2：浏览器可播放，但推荐使用其它浏览器（暂时不存在此值的参数）
	 *         -1：不支持的浏览器
	 */
	function checkBrowser() {
		var result = BROWSER.OTHER;
		if ($.browser.msie) {
			result = BROWSER.IE;
		}
		return result;
	}
	
	/*
	 * 检查版本逻辑
	 * 
	 * @param VPVersion
	 *            浏览器控件版本
	 * @param currentVersion
	 *            服务器当前版本
	 * @return -1：控件不存在
	 *         -2：版本过低,强制要求更新
	 *         1：版本不是最新的,需要更新
	 *         2：正常观看
	 */
	function checkVersion(VPVersion, currentVersion) {
		var result = mvPlayer.getCheckPlugin().VERSION.NOW;
		if (!VPVersion) {
			result = mvPlayer.getCheckPlugin().VERSION.NOEXIST; // 控件不存在
		} else if (currentVersion != VPVersion) {
			var versionInfo = VPVersion.split(".");
			
			// mvversion model VSSTOOPlayer V1.1.22
			var lastVersionIdx = 2;
			
			var version = versionInfo.length > lastVersionIdx ? versionInfo[lastVersionIdx] : mvPlayer.getCheckPlugin().VERSION.LOW;
			var MIN_VERSION = 20;
			if (version < MIN_VERSION) {
				// 版本过低
				result = mvPlayer.getCheckPlugin().VERSION.LOW;
			} else {
				result = mvPlayer.getCheckPlugin().VERSION.UPDATE;
			}
		} else {
			// 正常观看
			result = mvPlayer.getCheckPlugin().VERSION.NOW;
		}
		// 去掉检查
		//ApiUtils.notifyEvent(VideoApis.Event.checkVersionOver, {
		//	result : VideoApis.getVersionFlag()
		//});
		
		return result;
	}
	
	/**
	 * 显示消息应该调用此方法
	 * 
	 * @param msg
	 *            要显示的消息
	 * @param level
	 *            消息等级，可选参数，默认为'info'，可选值为'info','warn','error'
	 */
	function _showMsg(msg, level, args) {
		if ($.isArray(level)) {
			args = level;
			level = null;
		}
		if (level != LEVEL.WARN && level != LEVEL.ERROR) {
			level = LEVEL.INFO;
		}
		if (args && $.isArray(args)) {
			$.each(args, function(n, value) {
						msg = msg.replace(/%/, value);
						
					});

		}
		return msg;
	}
	
	/**
	 * videoApi所有弹出的消息都应该调用此方法
	 * 
	 * @param msg
	 *            要弹出的消息
	 * @param level
	 *            消息等级，可选参数，默认为'info'，可选值为'info','warn','error'
	 */
	function _alertMsg(msg, level, args) {
		if ($.isArray(level)) {
			args = level;
			level = null;
		}
		if (level != LEVEL.WARN && level != LEVEL.ERROR) {
			level = LEVEL.INFO;
		}
		if (args && $.isArray(args)) {
			$.each(args, function(n, value) {
				msg = msg.replace(/%/, value);
				
			});
		}
		vst.alert(msg);
	}
	
	function valid(report) {
		return (report == wordBook.getApiInfo().SUCCESS) || (report != wordBook.getApiInfo().EXCEPTION && 
				report != wordBook.getApiInfo().DOWNLINE && report != wordBook.getApiInfo().NOFOUND &&
				report != wordBook.getApiInfo().FAIL);
	}
	
	function startCamera(plugin, cameraInfo, callBack) {
		mvPlayer.setPlugin(plugin);
		mvPlayer.setCameraInfo(cameraInfo);
		mvPlayer.setActiveStatus(mvPlayer.getStatus().CONN);
		
		var _url = wordBook.getApiInfo().PARAMS.handlerURL.replace("$[METHOD]", "box/video/findvideosocket.jhtml");
		_url = _url.replace("$[RAND]", Math.random());
		_url = _url.replace("$[CAMERAID]", cameraInfo.cameraId);
		_url = _url.replace("$[IP]", cameraInfo.serverIP);
		_url = _url.replace("$[NODEID]", cameraInfo.nodeId);
		// 启动jQuery同步
		$.ajaxSetup({async:false});
		$.ajax({
			type : 'get',
			url : _url,
			dataType : 'json',
			success : function(socket) {
				var report = apiUtil.lTrim(socket.report);
				mvPlayer.setActiveStatus(mvPlayer.getStatus().STOP);
				if (valid(report)) {
					try {
						var controlCenter = socket.controlCenterIp;
						controlCenters=controlCenter.split(":");
						cameraInfo.serverIp=controlCenters[0];
						mvPlayer.stop();
						report = mvPlayer.start(cameraInfo, socket.ip, socket.port);
					} catch (e) {}
				} else {
					// do nothing
				}
				callBack(plugin, cameraInfo, report);
			},
			error : function() {
				callBack(plugin, cameraInfo, wordBook.getApiInfo().ERROR);
			}
		});
		// 恢复jQuery异步
		$.ajaxSetup({async:true});
	}

	// 重连
	function reconnect(plugin) {
		if (!plugin || !plugin.cameraInfo) { return; }
		
		var reconnCnt = 0;
		try {
			reconnCnt = vst.empty(plugin.reconnectCount) ? 1 : plugin.reconnectCount++;
		} catch (e) {}
		// n指start视频摄像头后的返回值
		startCamera(plugin, plugin.cameraInfo, function(plugin, cameraInfo, report, ip, port) {
			var CONNECT_SUCCESS = 1;
			var MAXSECOND_WAIT = 120;
			var STEP = 5;
			var _1s = 200;
			if (report != CONNECT_SUCCESS) {
				// 如果连接失败，等待一定的时间后再试一次，最长等待时间120秒
				var t = STEP + reconnCnt * STEP;
				if (t > MAXSECOND_WAIT) {
					t = MAXSECOND_WAIT;
				}
				setTimeout(function() { reconnect(plugin); }, _1s * t);
			} else {
				plugin.reconnectCount = 0;
			}
		});
	}

	msgHandler = {};

	return {
		getEvent : function() {
			return EVENT;
		},
		getBrowserFlag : function() {
			return browserFlag;
		},
		setBrowserFlag : function(flag) {
			browserFlag = flag;
		},
		getPluginFlag : function() {
			return pluginFlag;
		},
		setPluginFlag : function(flag) {
			pluginFlag = flag;
		},
		getPluginVersion : function() {
			return pluginVersion;
		},
		setPluginVersion : function(version) {
			pluginVersion = version;
		},
		/*
		 * 检查浏览器和控件版本
		 * 如果currentPlayerVersion为空，将异步的向服务器请求当前控件版本号进行比对，否则用指定的版本号进行比对
		 */
		doCheck : function(currentPlayerVersion) {
			var checkBrowserResult = checkBrowser();
			this.setBrowserFlag(checkBrowserResult);
			
			if (checkBrowserResult == BROWSER.IE) {
				this.doCheckVersion(currentPlayerVersion);
			} else {
				// this.alertMsg(wordBook.getApiInfo().BROWERDISABLED);
			}
		},
		/*
		 * 判断是否安装控件
		 */
		detectPlugin : function() {
			
			//判断是否安装了控件
			var CLSID = '5DA5F129-6434-4FEE-82B0-9BCBBE9BBEE3';
			var pluginDiv = document.createElement("<div id=\"pluginDiv\" style=\"display:none\"></div>")
			document.insertBefore(pluginDiv);
			pluginDiv.innerHTML = '<object id="objectForDetectPlugin" classid="CLSID:'+ CLSID + '"></object>';
			var plugin_div = $(".video-list");
			var content = "<div style='font-size:15px;padding-top:100px;text-align:center'>未安装控件,请点击<span style='color:red;font-size:22px'><a href='http://www.vsstoo.com/vsstooplayer.rar' >下载</a></span>获取最新控件</div>";
			try {
				var returnObj = eval("objectForDetectPlugin.getVersion");
				if (returnObj == undefined) {
					plugin_div.children().remove();
					plugin_div.append(content);
					return false;
				}
			} catch (e) {
				plugin_div.children().remove();
				plugin_div.append(content+'.');
				return false;
			}
			
			return true;

		},
		/*
		 * 控件版本检查,并更新playerVersion属性和videoVersionStatusFlag属性
		 * 如果currentPlayerVersion为空,将异步的向服务器请求当前控件版本号进行比对,否则用指定的版本号进行比对
		 */
		doCheckVersion : function(currentPlayerVersion) {
			var VPVersion = mvPlayer.detectClientPlugin(mvPlayer.getMv().CLSID, mvPlayer.getCheckPlugin().FUNCTIONNAME);
			// 设置检测的MV版本
			this.setPluginVersion(VPVersion);
			if (currentPlayerVersion) {
				var _pluginFlag = checkVersion(VPVersion, currentPlayerVersion);
				this.setPluginFlag(_pluginFlag);
			} else {
				var _pluginFlag = checkVersion(VPVersion);
				this.setPluginFlag(_pluginFlag);
				if (this.getPluginFlag() == mvPlayer.getCheckPlugin().VERSION.LOW) {
					// this.alertMsg(wordBook.getApiInfo().VERSIONLOWER);
				}
				var url = wordBook.getApiInfo().PARAMS.playerVersionURL;
				url = url.replace("$[METHOD]", "box/video/playerversion.jhtml");
				url = url.replace("$[RAND]", Math.random());
				// 启动jQuery同步
				$.ajaxSetup({async:false});
				$.getJSON(url, function(json) {
					var values = json.returnData;
					if (values.length > 0) {
						var _pluginFlag = checkVersion(VPVersion, values);
						videoApi.setPluginFlag(_pluginFlag);
					} else {
						// 如果获取最新版本号失败，跳过更新检查
						var _pluginFlag = checkVersion(VPVersion, VPVersion);
						videoApi.setPluginFlag(_pluginFlag);
					}
				});
				// 恢复jQuery异步
				$.ajaxSetup({async:true});
			}
		},
		/*
		 * 启动单个摄像头<br>
		 * 支持两种参数形式，单参数或7参数<br>
		 * 单参数：一个Object对像，形如:{plugin,password,cameraId,cameraName,controlType,serverIP,delayTime}
		 * 其中password,controlType,serverIP,delayTime为可选属性，如果不设置则使用DefaultParam中的相应属性
		 * 7参数：完整的7个参数：plugin,password,cameraId,cameraName,controlType,serverIP,delayTime
		 * 8参数：在7参数基础上加上status，如果为-1提示摄像头已过期
		 */
		startCamera : function() {
			// 获取参数
			var plugin, pluginPassword, cameraId, cameraName, controlType, servIP, delay_time, status, nodeId;
			if (arguments.length == 1) {
				var arg = arguments[0];
				plugin = arg.plugin;
				pluginPassword = vst.empty(arg.password) ? mvPlayer.getDefaultParam().PASSWORD : arg.password;
				cameraId = arg.cameraId;
				cameraName = arg.cameraName;
				controlType = vst.empty(arg.controlType) ? mvPlayer.getDefaultParam().CONTROLTYPE : arg.controlType;
				servIP = vst.empty(arg.serverIP) ? mvPlayer.getDefaultParam().SERVERIP : arg.serverIP;
				delay_time = vst.empty(arg.delayTime) ? mvPlayer.getDefaultParam().DELAYTIME : arg.delayTime;
				status = arg.status;
				nodeId = arg.nodeId;
			} else {
				plugin = arguments[0];
				pluginPassword = arguments[1];
				cameraId = arguments[2];
				cameraName = arguments[3];
				controlType = arguments[4];
				servIP = arguments[5];
				delay_time = arguments[6];
				if (arguments.length == 8) {
					status = arguments[7];
				}
			}
			
			if (!plugin) { return ; }
			
			if (status == mvPlayer.getStatus().NULL) {
				this.alertMsg(wordBook.getApiInfo().CAMERAEXPIRED, [cameraName]);
				return ;
			}
			var cameraInfo = {
				cameraId : cameraId,
				cameraName : cameraName,
				controlType : vst.empty(controlType) ? 0 : controlType,
				serverIP : servIP,
				delayTime : delay_time,
				password : pluginPassword,
				nodeId : nodeId
			};
			startCamera(plugin, cameraInfo, function(plugin, cameraInfo, report) {
				
				if (valid(report)) {
					var _url = wordBook.getApiInfo().PARAMS.handlerURL.replace("$[METHOD]", "box/video/quickconn.jhtml");
					_url = _url.replace("$[RAND]", Math.random());
					_url = _url.replace("$[CAMERAID]", cameraInfo.cameraId);
					_url = _url.replace("$[IP]", cameraInfo.serverIP);
					_url = _url.replace("$[NODEID]", cameraInfo.nodeId);
					var NORMAL_STATUS = 0;
					if (report > NORMAL_STATUS) {
						if (wordBook.getApiInfo().PARAMS.quickConnect) {
							//$.get(_url, { "result" : "1" });
						}
						apiUtil.notifyEvent(EVENT.VIDEOSTART, cameraInfo);
					} else {
						// 无法连接则重连
						reconnect(plugin);
					}
				} else if (wordBook.getApiInfo().EXCEPTION == report) {
					//videoApi.alertMsg(wordBook.getApiInfo().EXCEPTION);
					plugin.ErrorLog(videoApi.showMsg(wordBook.getApiInfo().EXCEPTION));
					
				} else if (wordBook.getApiInfo().DOWNLINE == report) {
					//videoApi.alertMsg(wordBook.getApiInfo().CAMERAOFF, [cameraInfo.cameraName]);
					plugin.ErrorLog(videoApi.showMsg(wordBook.getApiInfo().CAMERAOFF, [cameraInfo.cameraName]));
				} else if (wordBook.getApiInfo().NOFOUND == report) {
					//videoApi.alertMsg(wordBook.getApiInfo().UNCONNECT, [cameraInfo.cameraName, "2001012"]);
					plugin.ErrorLog(videoApi.showMsg(wordBook.getApiInfo().UNCONNECT, [cameraInfo.cameraName, "2001012"]));
				} else {
					//videoApi.alertMsg(wordBook.getApiInfo().ERROR);
					plugin.ErrorLog(videoApi.showMsg(wordBook.getApiInfo().ERROR));
				}
			});
		},
		reconnect : function(plugin) {
			reconnect(plugin);
		},
		// 返回指定控件的状态:0表已关闭,1表正在播放,2表正在连接服务器获取连接地址,-1表plugin参数为空
		getActiveCameraStatus : function(plugin) {
			mvPlayer.setPlugin(plugin);
			return mvPlayer.getActiveStatus();
		},
		/*
		 * 返回控件播放的摄像头的信息，可能为空
		 * cameraInfo具有如下属性:cameraId,cameraName,controlType,serverIP,delayTime
		 */
		getActiveCameraInfo : function(plugin) {
			mvPlayer.setPlugin(plugin);
			return mvPlayer.getCameraInfo();
		},
		/**
		 * 弹出指定KEY的消息
		 * 
		 * @param key
		 *            key中的消息都定义在videoApiI18N对象相应的属性中
		 * @param level
		 *            [可选]消息等级，可选参数，默认为'info'，可选值为'info','warn','error'
		 */
		alertMsg : function(key, level, args) {
			if (msgHandler[key]) {
				msgHandler[key](key, args);
			} else {
				_alertMsg(videoApiI18N[key], level, args);
			}
		},
		/**
		 * 显示指定KEY的消息
		 * 
		 * @param key
		 *            key中的消息都定义在VideoApisI18N对象相应的属性中
		 * @param level
		 *            [可选]消息等级，可选参数，默认为'info'，可选值为'info','warn','error'
		 */
		showMsg : function(key, level, args) {
			if (msgHandler[key]){
				msgHandler[key](key, args);
			}else{
				return _showMsg(videoApiI18N[key], level, args);
			}
		},
		setMsgHandler : function(key, handler) {
			if (key) {
				msgHandler[key] = handler;
			}
		}
	};
})();
