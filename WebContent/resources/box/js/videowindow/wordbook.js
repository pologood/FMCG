/*
 * 视窗 词典
 * darell.wu
 * rebuild 20120104 13:39
 */
var wordBook = (function() {
	
	var SCRMODEL = {
		DEFAULT : 0,
		SINGLE : 1,
		SPECIAL : 2
	};
	
	var COMMAND = {
		MOVE : {
			UP : 1,
			DOWN : 2,
			LEFT : 3,
			RIGHT : 4
		},
		ZOOM : {
			BIG : 1,
			SHORT : 2
		},
		BRIGHTNESS : {
			MORE : 1,
			LESS : 2
		},
		CONTRAST : {
			MORE : 1,
			LESS : 2
		},
		SATURATION : {
			MORE : 1,
			LESS : 2
		},
		HUE : {
			MORE : 1,
			LESS : 2
		},
		STOP : 0
	};

	var KEY = {
		ESC : 27,
		F11 : 122
	};
	
	var ACCESSURL = "http://video.vsstoo.com:8688/";
	var LOCALURL = "http://box.tohola.com/";	//系统地址
	var PROJECTNAME = "vbox";	//系统项目名
	
	var APIINFO = {
		OK : "ok",
		EXCEPTION : "cs_exception",
		UNCONNECT : "connect_failure",
		NOFOUND : "000.000.000.000:9999",
		DOWNLINE : "downline",
		CAMERAEXPIRED : "camera_expired",
		CAMERAOFF : "camera_off",
		VERSIONLOWER : "version_lower",
		BROWERDISABLED : "brower_disabled",
		ERROR : "error",
		SUCCESS : 1,
		FAIL : -1,
		PARAMS : {
			handlerURL : LOCALURL + "$[METHOD]?rand=$[RAND]&cameraId=$[CAMERAID]&serverIp=$[IP]&nodeId=$[NODEID]",
			ptzCtrlURL : LOCALURL + "$[METHOD]?rand=$[RAND]&cameraId=$[CAMERAID]&type=$[TYPE]&param=$[PARAM]&speed=$[SPEED]&nodeId=$[NODEID]",
			playerVersionURL : LOCALURL + "$[METHOD]?rand=$[RAND]",
			playerDownloadURL : "http://www.vsstoo.com/vsstooplayer.rar",
			crossDomain : true,
			sessionHolder : "$[SESSIONHOLDER]",
			quickConnect : true	
		}
	};
	
	var BROWSER = {
		IE : 1,
		OTHER : -1
	};
	
	var COOKIE = {
		SAVEPATH : 'maplayer_savepath'
	};
		
	var HTMLMAP = {
		TOP_C : "view_TopDiv",
		SAVE : "savePath",
		DIV : {
			PTZ_1 : "div.main_b_l",
			PTZ_2 : "div.main_b_c",
			LAYOUT : "div.main_b_r",
			TREE : ".left_meau_main",
			COMPATIBILITY : "#compatibilityDiv",
			DOWNLOAD : "#downloadActiveDiv",
			GRID : "#gridContainer",
			SEARCH_BTN : "#search_href",
			INPUT_TXT : ".txtinput",
			CAMERA : "currentCamera"
		},
		SCR : {
			DEFAULT_C : "gridContainer",
			DEFAULT_ID : "gridDiv_$[ID]",
			SINGLE_C : "gridContainer",
			SINGLE_ID : "gridDiv_$[ID]",
			SPECIAL_C : "special$[TYPE]Container",
			SPECIAL_ID : "specDiv$[TYPE]_$[ID]"
		},
		SCR_ATTR : {
			LOAD : "activeLoaded",
			CAMERA : "cameraId"
		},
		SCR_CLASS : {
			ACTIVE : "normalActiveDiv activeDiv",
			NORMAL : "normalActiveDiv",
			SELECTED : "selectedActiveDiv"
		},
		SPECIAL : {
			_6 : {
				TYPE : 0,
				TAG : "s6",
				LOCATION : {
					_1 : { ROW : 0, COL : 0 },
					_2 : { ROW : 0, COL : 1 },
					_3 : { ROW : 1, COL : 0 },
					_4 : { ROW : 1, COL : 1 },
					_5 : { ROW : 0, COL : 2 },
					_6 : { ROW : 1, COL : 2 }
				}
			},
			_8 : {
				TYPE : 1,
				TAG : "s8",
				LOCATION : {
					_1 : { ROW : 0, COL : 0 },
					_2 : { ROW : 0, COL : 1 },
					_3 : { ROW : 1, COL : 0 },
					_4 : { ROW : 1, COL : 1 },
					_5 : { ROW : 0, COL : 2 },
					_6 : { ROW : 1, COL : 2 },
					_7 : { ROW : 2, COL : 0 },
					_8 : { ROW : 2, COL : 1 }
				}
			},
			_13 : {
				TYPE : 2,
				TAG : "s13",
				LOCATION : {
					_1 : { ROW : 0, COL : 1 },
					_2 : { ROW : 1, COL : 0 },
					_3 : { ROW : 1, COL : 1 },
					_4 : { ROW : 0, COL : 2 },
					_5 : { ROW : 1, COL : 2 },
					_6 : { ROW : 2, COL : 0 },
					_7 : { ROW : 0, COL : 0 },
					_8 : { ROW : 2, COL : 1 },
					_9 : { ROW : 2, COL : 2 },
					_10 : { ROW : 0, COL : 3 },
					_11 : { ROW : 1, COL : 3 },
					_12 : { ROW : 2, COL : 3 },
					_13 : { ROW : 3, COL : 0 }
				}
			}
		}
	};
	
	return {
		getScrModel : function() {
			return SCRMODEL;
		},
		getApiInfo : function() {
			return APIINFO;
		},
		getBrowser : function() {
			return BROWSER;
		},
		getCookie : function() {
			return COOKIE;
		},
		getHtmlMap : function() {
			return HTMLMAP;
		},
		getCommand : function() {
			return COMMAND;
		},
		getKey : function() {
			return KEY;
		},
		getProjectName : function() {
			return PROJECTNAME;
		}
	};
	
})();
