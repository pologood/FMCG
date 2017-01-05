/*
 * vst 常用功能
 * darell.wu 20120106 17:39
 */
var vst = (function() {
	
	var uuid = (function() {
		var a = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");
		return function(b, f) {
			var h = a, e = [], d = Math.random;
			f = f || h.length;
			if (b) {
				for (var c = 0; c < b; c++) {
					e[c] = h[0 | d() * f];
				}
			} else {
				var g;
				e[8] = e[13] = e[18] = e[23] = "-";
				e[14] = "4";
				for (var c = 0; c < 36; c++) {
					if (!e[c]) {
						g = 0 | d() * 16;
						e[c] = h[(c == 19) ? (g & 3) | 8 : g & 15];
					}
				}
			}
		return e.join("").toLowerCase(); }; 
	})();
	
	function openWin(url, width, height) {
		var left = ($(window).width() / 2) - width / 2;
		var top = ($(window).height() / 2) - height / 2;
		var styleStr = 'toolbar=no,location=no,directories=auto,status=no,menubar=no,scrollbars=yes resizable=yes,z-lock=yes,width=' + 
					width+',height=' + height + ',left=' + left + ',top=' + top + ',screenX=' + left + ',screenY=' + top;
		window.open(url, "", styleStr);
	}
	
	function checkAllCbx(allCbxName, targetCbxName) {
		var allCbx = $("input[name='" + allCbxName + "']");
		var targetCbx = $("input[name='" + targetCbxName + "']");
		if (allCbx[0].checked) {
			targetCbx.each(function() {
				$(this).attr("checked", true);
			});
		} else {
			targetCbx.each(function() {
				$(this).attr("checked", false);
			});
		}
	}

	function isCheckCbx(targetCbxName) {
		// 判断是否有选 默认没选
		var isCheck = false;
		$("input[name='" + targetCbxName + "']").each(function() {
			if ($(this).attr("checked")) {
				isCheck = true;
				return false;
			}
		});
	    return isCheck;
	}
	
	function sleep(numberMillis) { 
		var now = new Date();
		var exitTime = now.getTime() + numberMillis;  
		while (true) { 
			now = new Date(); 
			if (now.getTime() > exitTime) return;
		}
	}

	function checkIE6() {
		return ($.browser.msie && $.browser.version == "6.0") ? true : false;
	}
	
	//禁止水平滚动
	function noHScroll() {
		if (vst.checkIE6() && $("html")[0].scrollHeight > $("html").height()) {
			$("html").css("overflowY", "scroll");
		}
	}
	
	//打开父窗口里的iframe，width和height不填时，取最大值
	function openParentDIV(url, title, width, height) {
		//找父窗口
		var obj = $(window.parent.document).find("#m_r_win");	
		
		//宽最大645，高最大500
		if (width == '' || width == undefined || width == null) {
			width=645;
		}
		if (height == '' || height == undefined || height == null) {
			height=500;
		}
		
		var margin_top = 500 - height;
		margin_top = margin_top / 3;
		//设置弹出窗的大小
		$(obj).width(width).css("margin-top", margin_top);
		$(obj).find(".wt_c").width(width - 40).text(title);
		$(obj).find(".wf_c").width(width - 40);
		$(obj).find(".wc").height(height - 65);
		$(obj).find(".wc_l").height(height - 65);
		$(obj).find(".wc_c").height(height - 65).width(width - 40);
		$(obj).find(".wc_r").height(height - 65);
		
		$(obj).find("#iframe_main").attr("src", url);
	}
	
	return {
		alert : function(descr) {
			alert(descr);
		},
		confirm : function(descr) {
			return confirm(descr);
		},
		empty : function(obj) {
			return obj == undefined || obj == "";
		},
		noEmpty : function(obj) {
			return obj != undefined && obj != "";
		},
		uuid : function(b, f) {
			return uuid(b, f);
		},
		openWin : function(url, width, height) {
			openWin(url, width, height)
		},
		checkAllCbx : function(allCbxName, targetCbxName) {
			checkAllCbx(allCbxName, targetCbxName);
		},
		isCheckCbx : function(targetCbxName) {
			return isCheckCbx(targetCbxName);
		},
		sleep : function(millis) {
			sleep(millis);
		},
		checkIE6 : function() {
			return checkIE6();
		},
		noHScroll : function() {
			return noHScroll();
		},
		back : function(preUrl) {
			window.location.href = preUrl;
		},
		openParentDIV : function(url, title, width, height) {
			openParentDIV(url, title, width, height);
		}
	};
	
})();