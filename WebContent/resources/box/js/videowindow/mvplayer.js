/*
 * MVPLAYER 摄像头播放器
 * darell.wu
 * rebuild 20120106 14:29
 */
var mvPlayer = (function() {
	
	var MV = {
		ID : "mvplayer_$[ID]",
		CLSID : "clsid:5DA5F129-6434-4FEE-82B0-9BCBBE9BBEE3",
		DIV : "<div id='$[ID]' class='normalActiveDiv activeDiv'></div>",
		DIV_NEWLINE : "<div style='clear: both;'></div>",
		OBJ : "<object classid='clsid:5DA5F129-6434-4FEE-82B0-9BCBBE9BBEE3' id='$[ID]'></object>",
		SIZE : {
			WIDTH : 695,
			HEIGHT : 565
		},
		EVENT : {
			CLICK : "VPanelSelect",
			DBLCLICK : "VPanelDBClick",
			DOWN : "VPanelLBtnDown",
			UP : "VPanelLBtnUp",
			NETBROKEN : "NetBroken"
		}
	};
	
	var CHECK_PLUGIN = {
		FUNCTIONNAME : "getVersion",
		DIV : "<div id='pluginDiv' style='display:none'></div>",
		OBJ : MV.OBJ.replace("$[ID]", "objectForDetectPlugin"),
		ID : "objectForDetectPlugin.getVersion",
		VERSION : {
			INIT : 0,
			NOEXIST : -1,
			LOW : -2,
			UPDATE : 1,
			NOW : 2
		}
	};
	
	var DEFAULTPARAM = {
		// 解码类型
		CONTROLTYPE : 0,
		// 控制服务器地址
		SERVERIP : "",
		// 密码
		PASSWORD : "88888\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0",
		// 延迟时间
		DELAYTIME : 1000
	};
	
	var STATUS = {
		NULL : -1,
		STOP : 0,
		RUN : 1,
		CONN : 2
	};
	
	// 当前插件
	var plugin = null;
	
	// 检测客户端是否安装控件,如果存在返回版本号 否则返回false
	function detectClientPlugin(CLSID, functionName) {
		if (!CLSID) {
			CLSID = MV.CLSID;
		}
		if (!functionName) {
			functoinName = CHECK_PLUGIN.FUNCTIONNAME;
		}
		var pluginDiv = document.createElement(CHECK_PLUGIN.DIV);
		document.insertBefore(pluginDiv);
		pluginDiv.innerHTML = CHECK_PLUGIN.OBJ;
		try {
			var returnObj = eval(CHECK_PLUGIN.ID);
			if (!returnObj) {
				pluginDiv.removeNode(true);// 删除pluginDiv及其所有的子元素
				return false;
			} else {
				pluginDiv.removeNode(true);// 删除pluginDiv及其所有的子元素
				return returnObj;
			}
		} catch (e) {
			return false;
		}
	}
	
	return {
		getMv : function() {
			return MV;
		},
		getCheckPlugin : function() {
			return CHECK_PLUGIN;
		},
		getDefaultParam : function() {
			return DEFAULTPARAM;
		},
		detectClientPlugin : function(CLSID, functionName) {
			return detectClientPlugin(CLSID, functionName);
		},
		getStatus : function() {
			return STATUS;
		},
		// 第一步请设置当前插件
		setPlugin : function(_plugin) {
			plugin = _plugin;
		},
		getPlugin : function() {
			if (!plugin) {
				// vst.alert("当前MvPlayer插件不能空");
				return null;
			}
			return plugin;
		},
		setActiveStatus : function(activeStatus) {
			this.getPlugin().activeStatus = activeStatus;
		},
		getActiveStatus : function() {
			var status = STATUS.NULL;
			if (this.getPlugin()) {
				status = this.getPlugin().IsStart();
				if (status == STATUS.STOP && this.getPlugin().activeStatus != undefined) {
					return this.getPlugin().activeStatus;
				}
			}
			return status;
		},
		setCameraInfo : function(cameraInfo) {
			this.getPlugin().cameraInfo = cameraInfo;
		},
		getCameraInfo : function() {
			if (this.getPlugin()) {
				return this.getPlugin().cameraInfo;
			} else {
				return null;
			}
		},
		getVersion : function() {
			return this.getPlugin().getVersion();
		},
		setLocation : function(row, col) {
			this.getPlugin().x = row;
			this.getPlugin().y = col;
		},
		setSize : function(width, height) {
			this.getPlugin().width = width;
			this.getPlugin().height = height;
			
			this.getPlugin().setSize(width, height);
		},
		addEvent : function(event, func) {
			this.getPlugin().attachEvent(event, func);
		},
		stop : function() {
			if (this.getPlugin() && this.getPlugin().IsStart() != STATUS.STOP) {
				this.getPlugin().stop();
			}
		},
		start : function(cameraInfo, ip, port) {
			this.setCameraInfo(cameraInfo);
			// strange STOP
			this.setActiveStatus(STATUS.STOP);
			this.getPlugin().SetPassword(cameraInfo.password);
			this.getPlugin().SetCaptureID(cameraInfo.cameraId);
			this.getPlugin().SetCapName(cameraInfo.cameraName);
			
			var versionStartIdx = 13;
			var versionIdx = 2;
			var versionIdDown = 20;
			var versionIdUp = 23;
			var versionInfo = this.getVersion().substring(versionStartIdx).split(".");
			var version = versionInfo[versionIdx];
			if (version > versionIdDown) {
				if (version >= versionIdUp) {
					this.getPlugin().SetDelayTime(cameraInfo.delayTime);
				}
				// 得到服务IP
				this.getPlugin().SetSvrIp(cameraInfo.serverIp);
				// 采集卡类型
				this.getPlugin().SetCtrlType(cameraInfo.controlType);
			}
			this.getPlugin().SetSvrIp(cameraInfo.serverIp);
			this.getPlugin().SetIP(ip);
			this.getPlugin().SetPort(port);
			try {
				return this.getPlugin().Start();
			} catch (e) {
				return null;
			}
		},
		save : function(savePath) {
			this.getPlugin().setCapDir(savePath);
		},
		cap : function() {
			var ret;
			try {
				//var savePath = $.cookies.get(wordBook.getCookie().SAVEPATH);
				//if (vst.noEmpty(savePath)) {
				//	this.save(savePath);
				//}
				ret = this.getPlugin().OnMenuCap();
			} catch(e) {}
			return ret;
		},
		backPlay : function() {
			this.getPlugin().OnMenuPlay();
		},
		seachDvs : function() {
			try {
				return this.getPlugin().SeachDvs();
			} catch (e) {
				return null;
			}
		}
		
	};
	
})();
