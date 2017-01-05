/*
 * 产生视窗影响
 * darell.wu 
 * create 20130114 16:16
 */

 var COMMAND = wordBook.getCommand();

$(function() {
	videoApi.doCheck();
	// 添加视频窗口 20130115 14:35
	videowin.addVideoWin();
	if (videoApi.getPluginFlag() == mvPlayer.getCheckPlugin().VERSION.UPDATE) {
		// 不提示更新控件
		videoApi.setPluginFlag(mvPlayer.getCheckPlugin().VERSION.NOW);
	}
	if (checkValid()) {
		init();
		$(document).bind("contextmenu", function(e) { return false; });		
	} else if (videoApi.getBrowserFlag() != wordBook.getBrowser().IE) {
		var COMPATIBILITY = "<div id='compatibilityDiv' style='text-align:center; position:relative; top:50%; color:#1e1e1e;'>" + 
							"敬告：该视窗播放器仅限微软IE浏览器访问，不便之处敬请谅解" + 
							"</div>";
		videowin.getVideoWin().html(COMPATIBILITY);
	} else if (videoApi.getPluginFlag() != mvPlayer.getCheckPlugin().VERSION.NOW) {alert(videoApi.getPluginFlag());
		var DOWNLOAD = "<div id='downloadActiveDiv' style='text-align:center; position:relative; top:50%; color:#1e1e1e;'>" + 
		   				"最新版视窗播放器，请点击" + 
		   				"<a href='javascript:void(0);' style='color:#207fc8; font-weight:bold;' " + 
		   				"onclick='alert(\"敬告：安装完毕请重启浏览器\");downloadNowMv(wordBook.getApiInfo().PARAMS.playerDownloadURL);'>这里</a>" + "下载" + "</div>";
		videowin.getVideoWin().html(DOWNLOAD);
	} else {
		// do nothing
	}
	
//	//显示隐藏视频窗口
//	$(".small").click(function() {
//		$(".alert_video").hide();
//	});
//	
//    $(".show_video_all").click(function() {
//		if ($(".alert_video").is(":hidden")) {
//			$(".alert_video").show();
//		} else {
//			$(".alert_video").hide();
//		}
//	});
//	
//	$(".show_video_all").mouseover(function() {
//		$(this).addClass("on");
//	});
//	$(".show_video_all").mouseout(function() {
//		$(this).removeClass("on");
//	});
//	$(".small").mouseover(function() {
//		$(this).addClass("on");
//	});
//	$(".small").mouseout(function() {
//		$(this).removeClass("on");
//	});
});

function checkValid() {
	return videoApi.getBrowserFlag() == wordBook.getBrowser().IE && videoApi.getPluginFlag() == mvPlayer.getCheckPlugin().VERSION.NOW;
}

function init() {
	//var detectPlugin = videoApi.detectPlugin();
	//if(detectPlugin){
		videoPoint.initVideowin();
		addListener();
		// 退出处理
		_close();
	//}
}

// 视频窗口 start//////////////////

function addListener() {
	apiUtil.addEventListener(videoPoint.getEvent().ACTIVENETBROKEN, reconnect);
}

function _close() {
	$(window).unload(function() {
		videoPoint.stopAllPlugin();
	});
}

// 发送云台控制或视频参数指令
function sendPTZCtrl(ptzFunc, power) {
	
	var row = 0;
	var col = 0;
	var selectedKj = videoPoint.getTargetViewPoint();//当前选中的控件 
	if (selectedKj.IsStart() == mvPlayer.getStatus().RUN) {
		var cameraInfo = selectedKj.cameraInfo;
		ptzFunc(power, cameraInfo.cameraId);
	}
}

// 视频抓图
function capCamera() {
	
	//获取当前选择的控件
	var kj= videoPoint.getTargetViewPoint();
	if(kj!=null){
	 	if(kj.IsStart()==1){
	 		var path = kj.OnMenuCap();
			alert("抓图成功！文件位置："+path);
	 	}
	}
}

function downloadNowMv(_url) {
	window.location.href = _url;
}

function browseFolder() {
	try {
		var Message = "请选择抓图保存路径"; //选择框提示信息
		var Shell = new ActiveXObject("Shell.Application");
		var Folder = Shell.BrowseForFolder(0, Message, 64, 17); //起始目录为：我的电脑
		//var Folder = Shell.BrowseForFolder(0,Message,0); //起始目录为：桌面
		if (Folder != null) {
			Folder = Folder.items(); // 返回 FolderItems 对象
			Folder = Folder.item();  // 返回 Folderitem 对象
			Folder = Folder.Path; 	 // 返回路径
			if (Folder.charAt(Folder.length - 1) != "") {
				Folder = Folder + "";
			}
			$(wordBook.getHtmlMap().SAVE).val(Folder);
			videoPoint.setSavePath(Folder);
		}
	}
	catch (e) {
		vst.alert("敬告：浏览器安全设置问题，弹出文件选择框被阻止。\n" + 
				  "请确保IE浏览器 - 工具 - Internet选项 - 安全 - 自定义级别... " +
				  "- ActiveX控件和插件 下的 第三个选项（对未标记为可安全执行脚本" + 
				  "的ActiveX控件初始化并执行脚本）设置为启用；同时设置本站点为可信任站点。");
	}
}

/*
 * 启动摄像头
 * @param camera 摄像头信息对象
 */
function startMvPlayer(camera) {
	if (videoPoint.isRegisterCamera(camera)) {
		var RUN = 1;
		var _mv = videoPoint.getRegisterCamera(camera);
		var _row = _mv.attr("x");
		var _col = _mv.attr("y");
		videoPoint.doTarget(_row, _col);
		var _mv_status = videoPoint.getVideoPointPluginStatus(_row, _col);
		if (_mv_status != RUN) {
			videoPoint.stopPlugin();
			videoPoint.startPlugin(_row, _col, camera);
		}
		return ;
	}
	var _location = videoPoint.findIdleVideoPoint();
	if (_location) {
		videoPoint.doTarget(_location.row, _location.col);
		videoPoint.startPluginOnTargetView(camera);
	} else {
		vst.alert("敬告：您打开的视窗超过数量限制！请关闭其他视频！");
	}
}

/*
 * 启动某节点下摄像头列表
 * @param cameraList 摄像头信息对象列表
 *        nodeName 节点名称
 *        num 该节点下的摄像头列表数
 */
function startMvPlayerList(cameraList, nodeName, num) {
	
	if (vst.confirm("打开[" + nodeName + "]下的所有摄像头前先关闭当前摄像头？")) {
		videoPoint.stopAllPlugin();
		videoPoint.startPluginList(cameraList);
	}
}
function startMvPlayerList2(cameraList) {
	videoPoint.stopAllPlugin();
	videoPoint.startPluginList(cameraList);
}

/*
 * 取得摄像头信息
 * @param _attrs 节点的属性data
 * @return 摄像头信息对象
 */
function obj_camera(cameraId, cameraName,nodeId) {
  var camera = {};
  camera.cameraId = cameraId;
  camera.delayTime = '';
  camera.controlType = '';
  camera.serverIp = '';
  camera.status = 1;
  camera.cameraName = cameraName;
  camera.nodeId = nodeId;
  return camera;
}

/*
 * 取得摄像头信息 @zcj
 * @param _attrs 节点的属性data
 * @return 摄像头信息对象  添加门店ID即nodeID
 */
function obj_storeCamera(cameraId, cameraName, nodeId) {
  var camera = {};
  camera.cameraId = cameraId;
  camera.delayTime = '';
  camera.controlType = '';
  camera.serverIp = '';
  camera.status = 1;
  camera.cameraName = cameraName;
  camera.nodeId = nodeId;
  return camera;
}

//显隐视频窗口
function showViewpointPanel() {
	if ($(".alert_video").is(":hidden")) {
		$(".alert_video").show();
	}
}

//通过局域网找设备
function seachDvs() {
	var dvs = new Array();
	var dvsStr = mvPlayer.seachDvs();
	if (dvsStr != "") {
		var tmp = new Array();
		tmp = dvsStr.split(";");
		var len = tmp.length;
		for (var i=0; i<len; i++) {
			var sno = tmp[i].split(",")[1];
			if (sno != "" && sno != undefined) {
				dvs.push(sno.split(":")[1]);
			}
		}
	}
	return dvs;
}
