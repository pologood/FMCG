/*
 * 视频窗口 videowin
 * waf
 * build  20140709 14:22
 */
var videowin = (function() {

	// 共10屏 另有1主视频窗口
	var MAXNUM_SCROLLWIN = 10;

	var CSS = {
		OUTER : "outer",
		MAIN: "main",
		NODE_LIST: "node-list",
		NODE_LIST_TITLE: "red-title",
		NODE_LIST_CONTENT: "node-list-content",
		LIST_TITLE: "margins-lr",
		VIDEO_LIST: "video-list",
		VIDEO_FOUR: "video-f",
		LI_1: "li-1",
		LI_2: "li-2",
		LI_3: "li-3",
		LI_4: "li-4",
		CURRENT_VIDEO: "current-video",
		VIDEO_EIGHT: "video-e",
		BIG_VIDEO: "e-big",
		VIDEO_RIGHT: "e-r",
		VIDEO_BOTTOM: "e-b",
		INNER: "inner",
		CONTRAL_LIST: "contral-list",
		TOP : "alert_video",
		SCROLLCONTAINER : "scroll_box",
		SCROLL_PRE : "scroll_prev",
		SCROLLVIDEO : "small_video",
		SCROLL_NEXT : "scroll_next",
		CONTAINER : "video_box",
		VIDEO : "video",
		TOOLBAR : "video_action",
		DIRECTION : "direction",
		UP : "m-up",
		LEFT : "m-left",
		CENTER : "m-center",
		RIGHT : "m-right",
		DOWN : "m-down",
		ENLARGE : "v_enlarge",
		NARROW : "v_narrow",
		PLAY : "v_play",
		CAPTURE : "v_capture",
		FOUR : "v_four",
		FOUR_BG : "v_four_bg",
		EIGHT : "v_eight",
		
		
		
		DRAG : "drag_video",
		VIDEO_PRE : "video_prev",
		VIDEO_NEXT : "video_next",
		SLOW : "v_slow",
		FAST : "v_fast",
		VOICE: "v_voice",
		VOICE_UP: "v_voice_up",
		VOICE_DOWN: "v_voice_down",
		CLOSEALL : "v_close_all",
		CLOSEONE : "v_close_one",
		RECORD : "v_video",
		PREV :"v_video_prev",
		NEXT :"v_video_next",
		MIN : "small",
		TAB : "v_change",
		DRAG : "v_drag",
		SCROLLWIN_PREFIX : "scrollwin_prefix_li",
		ON : "sv_on",
		SELECTED : "selectedActive",
		NORMAL : "normalActive"
	};
	
	var WORD = {
		UP : "向上",
		LEFT : "向左",
		RIGHT : "向右",
		DOWN : "向下",
		ENLARGE : "放大",
		NARROW : "缩小",
		PLAY : "远程回放",
		CAPTURE : "视频抓图",
		VOICE : "声音",
		FOUR : "显示四屏",
		EIGHT : "显示八屏",
		
		
		
		SLOW : "慢",
		FAST : "快",
		CLOSEALL : "关闭所有摄像头",
		CLOSEONE : "关闭当前摄像头",
		RECORD : "本地录像",
		PREV :"上一屏",
		NEXT :"下一屏",
		MIN : "隐藏(点击左上角摄像头小图标显示)",
		TAB : "切换视频模式",
		DRAG : "按住拖动",
		SCROLLCONTAINER : "scroll_box",
		SCROLLVIDEO : "small_video",
		SCROLL_PRE : "scroll_prev",
		SCROLL_NEXT : "scroll_next",
		ON : "sv_on",
		SELECTED : "selectedActive",
		SCROLLWIN_PREFIX : "scrollwin_prefix_li",
		NORMAL : "normalActive"
	};
	
	//<div class="m_tree">
	//<div class="show_video_all" title="显隐摄像头"></div>
    //<div class="left_meau_top">
    //	<span class="f_n f_r m_r10"><a href="javascript:void(0)" onclick="javascript:loadzTree();"><spring:message code="g.update"/></a></span>
    //</div>
    // <div class="left_meau_main"><ul id="tree" class="ztree"></ul></div>
	//</div>
	
	
	
	
var htmlPanel = "" + 
"<div class='" + CSS.OUTER + "'>" + 
	"<div class='" + CSS.MAIN + "'>" + 
		"<div class='" + CSS.NODE_LIST + "'>" + 
			"<div class='" + CSS.NODE_LIST_TITLE + "'>" + 
				"<p class='" + CSS.LIST_TITLE + "'>通道列表</p>" + 
				"<ul id='tree' class='ztree'></ul>" + 
			"</div>" + 
			"<div class='" + CSS.NODE_LIST_CONTENT + "'></div>" + 
		"</div>" + 
		"<div class='" + CSS.VIDEO_LIST + "'>" + 
			"<div class='" + CSS.VIDEO_FOUR + "'  style='display:block;'>" + 
				"<ul>" + 
					"<li id='spec4_0_0' class='" + CSS.LI_1+" "+CSS.CURRENT_VIDEO + "'></li>" + 
					"<li id='spec4_0_1' class='" + CSS.LI_2 + "'></li>" + 
					"<li id='spec4_1_0' class='" + CSS.LI_3 + "'></li>" + 
					"<li id='spec4_1_1' class='" + CSS.LI_4 + "'></li>" + 
				"</ul>" + 
			"</div>" + 
			"<div class='" + CSS.VIDEO_EIGHT + "'  style='display:none;'>" + 
				"<div  id='spec8_0_0' class='" + CSS.BIG_VIDEO+" "+CSS.CURRENT_VIDEO + "'></div>" + 
				"<ul class='" + CSS.VIDEO_RIGHT + "'>" + 
					"<li style='height:33%;'>" + 
						"<div  id='spec8_0_1' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
					"<li style='height:33%;'>" + 
						"<div id='spec8_1_0' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
					"<li style='height:34%;'>" + 
						"<div id='spec8_1_1' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
				"</ul>" + 
				"<ul class='" + CSS.VIDEO_BOTTOM + "'>" + 
					"<li style='left:0;'>" + 
						"<div id='spec8_0_2' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
					"<li style='left:25%;'>" + 
						"<div id='spec8_1_2' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
					"<li style='left:50%;'>" + 
						"<div id='spec8_2_0' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
					"<li style='left:75%;'>" + 
						"<div id='spec8_2_1' class='" + CSS.INNER + "'></div>" +
					"</li>" + 
				"</ul>" + 
			"</div>" + 
		"</div>" + 
		"<div class='" + CSS.CONTRAL_LIST + "'>" + 
			"<div class='" + CSS.TOOLBAR + "'>" + 
				"<div class='" + CSS.DIRECTION + "'>" + 
					"<div class='" + CSS.UP + "' title='" + WORD.UP + "'><a href='javascript:void(0);' onmousedown='sendPTZCtrl(videoApi.PTZ.move, COMMAND.MOVE.UP)' onclick='sendPTZCtrl(videoApi.PTZ.move, COMMAND.STOP)'></a></div>" +
					"<div class='" + CSS.LEFT + "' title='" + WORD.LEFT + "'><a href='javascript:void(0);' onmousedown='sendPTZCtrl(videoApi.PTZ.move, COMMAND.MOVE.LEFT)' onclick='sendPTZCtrl(videoApi.PTZ.move, COMMAND.STOP)'></a></div>" +
					"<div class='" + CSS.CENTER + "'></div>" + 
					"<div class='" + CSS.RIGHT + "' title='" + WORD.RIGHT + "'><a href='javascript:void(0);' onmousedown='sendPTZCtrl(videoApi.PTZ.move, COMMAND.MOVE.RIGHT)' onclick='sendPTZCtrl(videoApi.PTZ.move, COMMAND.STOP)'></a></div>" +
					"<div class='" + CSS.DOWN + "' title='" + WORD.DOWN + "'><a href='javascript:void(0);' onmousedown='sendPTZCtrl(videoApi.PTZ.move, COMMAND.MOVE.DOWN)' onclick='sendPTZCtrl(videoApi.PTZ.move, COMMAND.STOP)'></a></div>" +
				"</div>" +
				"<ul>" +
					"<li class='" + CSS.FAST + "' title='" + WORD.FAST + "'><a href='javascript:videoApi.PTZ.speedUp();'></a></li>" +
					"<li class='" + CSS.SLOW + "' title='" + WORD.SLOW + "'><a href='javascript:videoApi.PTZ.speedDown();'></a></li>" +
					"<li class='" + CSS.ENLARGE + "' title='" + WORD.ENLARGE + "'><a href='javascript:void(0);' onmousedown='sendPTZCtrl(videoApi.PTZ.zoom, COMMAND.ZOOM.BIG)' onclick='sendPTZCtrl(videoApi.PTZ.zoom, COMMAND.STOP)'></a></li>" +
					"<li class='" + CSS.NARROW + "' title='" + WORD.NARROW + "'><a href='javascript:void(0);' onmousedown='sendPTZCtrl(videoApi.PTZ.zoom, COMMAND.ZOOM.SHORT)' onclick='sendPTZCtrl(videoApi.PTZ.zoom, COMMAND.STOP)'></a></li>" +
					"<li class='" + CSS.VOICE + "' title='" + WORD.VOICE + "'><a class='" + CSS.VOICE_UP + "' href='javascript:videoApi.PTZ.voiceUp();' ></a><a class='" + CSS.VOICE_DOWN + "' href='javascript:videoApi.PTZ.voiceDown();' ></a></li>" +
					"<li class='" + CSS.CAPTURE + "' title='" + WORD.CAPTURE + "'><a href='javascript:capCamera()'></a></li>" +
					"<li class='" + CSS.PLAY + "' title='" + WORD.PLAY + "'><a href='javascript:videoPoint.backplayPlugin();'></a></li>" +
					"<li class='" + CSS.FOUR + "' title='" + WORD.FOUR + "'  style='margin-top:10px;'><a class='" + CSS.FOUR_BG + "' href='javascript:changeTo4();' ></a></li>" +
					"<li class='" + CSS.EIGHT + "' title='" + WORD.EIGHT + "' style='margin-top:10px;'><a href='javascript:changeTo8();'></a></li>" +
				"</ul>" +
			"</div>" +
		"</div>" +
	"</div>" +
"</div>";
	      
	var nextIdx = 1;
	function addMainWinEvent(func) {
		/*$("." + CSS.VIDEO_NEXT).click(function() {
			if (nextIdx < MAXNUM_SCROLLWIN) {
				if (func) {
					func(0, nextIdx);
				}
				nextIdx++;
			}
		});
		$("." + CSS.VIDEO_PRE).click(function() {
			if (nextIdx > 1) {
				nextIdx--;
				if (func) {
					func(0, nextIdx);
				}
			}
		});*/
	}
	
	
	

	function addScrollWinEvent() {
		// 初始化滚动小视频块的向左向右
		var pageIdx = 1;
		var pageSize = 5; //每版放4个视频块
		var scrollWin = $("." + CSS.SCROLLVIDEO);
		var totalHeight = scrollWin.height();
		var allScrollWin = $("." + CSS.SCROLLVIDEO + " ul");
		var scrollWinNum = allScrollWin.find("li").length;
		var pageCnt = Math.ceil(scrollWinNum / pageSize);
		//向右 按钮
		$("." + CSS.SCROLL_NEXT).click(function() {
			if (!allScrollWin.is(":animated")) {
				if (pageIdx == pageCnt) {  //已经到最后一个版面了,如果再向后，必须跳转到第一个版面。
					allScrollWin.animate({ top : '0px' }, 800); //通过改变left值，跳转到第一个版面
					pageIdx = 1;
				} else {
					allScrollWin.animate({ top : '-=' + totalHeight + 'px' }, 800);  //通过改变left值，达到每次换一个版面
					pageIdx++;
				}
			}
		});
		//往左 按钮
		$("." + CSS.SCROLL_PRE).click(function() {
			if (!allScrollWin.is(":animated")) {
				if (pageIdx == 1) {  //已经到第一个版面了,如果再向前，必须跳转到最后一个版面。
					allScrollWin.animate({ top : '-=' + totalHeight * (pageCnt - 1) + 'px' }, 800); //通过改变left值，跳转到最后一个版面
					pageIdx = pageCnt;
				} else {
					allScrollWin.animate({ top : '+=' + totalHeight + 'px'}, 800);  //通过改变left值，达到每次换一个版面
					pageIdx--;
				}
			}
		});
	}
	
	
	
	
	

	return {
		/*
		 * 初始化视频窗口，只可调用一次
		 */
		addVideoWin : function() {
			$("body").append(htmlPanel);
		},
		initVideoCount : function(count) {
			MAXNUM_SCROLLWIN = count;
		},
		getVideoWin : function() {
			return $("." + CSS.OUTER);
		},
		getMainWin : function() {
			return $("." + CSS.VIDEO + " ul li");
		},
		addMainWin : function(obj_html) {
			this.getMainWin().append(obj_html);
		},
		getScrollWin : function() {
			return $("." + CSS.SCROLLVIDEO + " ul");
		},
		addScrollSmallWin : function(obj_html, idx) {
			this.getScrollWin().append("<li id='" + CSS.SCROLLWIN_PREFIX + idx + "'><a href='javascript:void(0)'>" + obj_html + "</a></li>");
		},
		getScrollSmallWin : function(idx) {
			return this.getScrollWin().find("li#" + CSS.SCROLLWIN_PREFIX + idx + " a");
		},
		getMaxNumVideoWin : function() {
			return MAXNUM_SCROLLWIN;
		},
		addMainWinEvent : function(func) {
			addMainWinEvent(func);
		},
		addScrollWinEvent : function() {
			addScrollWinEvent();
		},
		showVideoWin : function() {
			this.getVideoWin().show();
		},
		hideVideoWin : function() {
			this.getVideoWin().hide();
		}
	};
})();