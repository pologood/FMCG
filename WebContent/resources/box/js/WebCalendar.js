/**
 * 日历类 (调用方法查看最下方)
 * @version 3
 * @author skey_chen 2009-12-01 10:20
 * @email skey_chen@163.com
 */
var __WebCalendarConfig__ = {
	contentPath : ""
			+ document.getElementsByTagName("script")[document
					.getElementsByTagName("script").length - 1].src.substring(
					0, document.getElementsByTagName("script")[document
							.getElementsByTagName("script").length - 1].src
							.lastIndexOf("/") + 1),
	skinList : [ {
		name : "default"
	}, {
		name : "gray"
	}, {
		name : "lightGreen"
	} ]
};
if (true) {
	var _headList = document.getElementsByTagName("head");
	for ( var i = 0; i < __WebCalendarConfig__.skinList.length; i++) {
		var fileUrl = __WebCalendarConfig__.contentPath + "skin/"
				+ __WebCalendarConfig__.skinList[i].name + "/WebCalendar.css";
		var fileref = document.createElement("link");
		fileref.setAttribute("rel", "stylesheet");
		fileref.setAttribute("type", "text/css");
		fileref.setAttribute("skinType", "WebCalendar");
		fileref.setAttribute("disabled", "true");
		if (i == 0) {
			fileref.disabled = false;
		}
		fileref.setAttribute("href", fileUrl);
		fileref.setAttribute("skin", __WebCalendarConfig__.skinList[i].name);
		_headList[_headList.length - 1].appendChild(fileref);
	}
}
function CalendarHelper() {
	this.skinLoad = true;
	this.pickMode = {
		"second" : 1,
		"minute" : 2,
		"hour" : 3,
		"day" : 4,
		"month" : 5,
		"year" : 6
	};
	this.skinList = new Array();
	this.language = {
		"year" : [ [ "" ], [ "" ] ],
		"months" : [
				[ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月",
						"十一月", "十二月" ],
				[ "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG",
						"SEP", "OCT", "NOV", "DEC" ] ],
		"weeks" : [ [ "日", "一", "二", "三", "四", "五", "六" ],
				[ "SUN", "MON", "TUR", "WED", "THU", "FRI", "SAT" ] ],
		"quarter" : [
				[ "冬", "冬", "春", "春", "春", "夏", "夏", "夏", "秋", "秋", "秋", "冬" ],
				[ "SPRING", "SPRING", "SPRING", "SUMMER", "SUMMER", "SUMMER",
						"AUTUMN", "AUTUMN", "AUTUMN", "WINTER", "WINTER",
						"WINTER" ] ],
		"hour" : [ [ "时" ], [ "H" ] ],
		"minute" : [ [ "分" ], [ "M" ] ],
		"second" : [ [ "秒" ], [ "S" ] ],
		"clear" : [ [ "清空" ], [ "CLS" ] ],
		"today" : [ [ "今天" ], [ "TODAY" ] ],
		"pickTxt" : [ [ "确定" ], [ "OK" ] ],
		"close" : [ [ "关闭" ], [ "CLOSE" ] ]
	};
	this.date = new Date();
	this.year = this.date.getFullYear();
	this.month = this.date.getMonth();
	this.day = this.date.getDate();
	this.hour = this.date.getHours();
	this.minute = this.date.getMinutes();
	this.second = this.date.getSeconds();
	this.left = 0;
	this.top = 0;
	this.isFocus = false;
	this.beginYearLoad = this.year - 30;
	this.endYearLoad = this.year + 20;
	this.beginYear = this.beginYearLoad;
	this.endYear = this.endYearLoad;
	this.DateMode = this.pickMode["day"];this.lang=0;this.sample="yyyy-MM-dd";this.format="yyyy-MM-dd";this.selectTime=new Date();this.dateControl=null;this.panel=null;this.container=null;}
CalendarHelper.prototype={
	toDate : function(str, style) {
		if (str == null)
			return new Date();
		try {
			if (str.length == style.length) {
				var y = str.substring(style.indexOf('yyyy'), style
						.indexOf('yyyy') + 4);
				var M = str.substring(style.indexOf('MM'),
						style.indexOf('MM') + 2);
				var d = str.substring(style.indexOf('dd'),
						style.indexOf('dd') + 2);
				var H = str.substring(style.indexOf('HH'),
						style.indexOf('HH') + 2);
				var m = str.substring(style.indexOf('mm'),
						style.indexOf('mm') + 2);
				var s = str.substring(style.indexOf('ss'),
						style.indexOf('ss') + 2);
				if ((s == null || s == "" || isNaN(s))) {
					s = new Date().getSeconds();
				}
				if ((m == null || m == "" || isNaN(m))) {
					m = new Date().getMinutes();
				}
				if ((H == null || H == "" || isNaN(H))) {
					H = new Date().getHours();
				}
				if ((d == null || d == "" || isNaN(d))) {
					d = new Date().getDate();
				}
				if ((M == null || M == "" || isNaN(M))) {
					M = new Date().getMonth() + 1;
				}
				if ((y == null || y == "" || isNaN(y))) {
					y = new Date().getFullYear();
				}
				if (y < 1000) {
					y = new Date().getFullYear();
				}
				var dt;
				eval("dt=new Date('" + y + "','" + (M - 1) + "','" + d + "','"
						+ H + "','" + m + "','" + s + "')");
				return dt;
			}
			return new Date();
		} catch (e) {
			return new Date();
		}
	},
	getWeekOfYear : function(currDate) {
		var cd = new Date(currDate.getFullYear(), 0, 1);
		var cDay = parseInt(Math.abs(currDate - cd) / (1000 * 60 * 60 * 24)) + 1;
		var _w = (cDay + cd.getDay()) / 7;
		var _wInt = parseInt(_w);
		return ((_w > _wInt) ? (_wInt + 1) : _wInt);
	},
	formatDate : function(date, style) {
		var o = {
			"M{2}" : ((this.DateMode <= 5) ? (date.getMonth() + 1) : "MM"),
			"d{2}" : ((this.DateMode <= 4) ? (date.getDate()) : "dd"),
			"H{2}" : ((this.DateMode <= 3) ? (date.getHours()) : "HH"),
			"m{2}" : date.getMinutes(),
			"s{2}" : date.getSeconds(),
			"S{3}" : (new Date().getMilliseconds()),
			"W{1}" : ((this.DateMode <= 4) ? (this.getWeekOfYear(date)) : "W"),
			"w{1}" : ((this.DateMode <= 4) ? (this.language["weeks"][this.lang][date
					.getDay()])
					: "w"),
			"q{1}" : ((this.DateMode <= 5) ? (this.language["quarter"][this.lang][date
					.getMonth()])
					: "q")
		};
		if (/(y{4})/.test(style)) {
			style = style.replace(RegExp.$1, (date.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}
		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(style)) {
				style = style.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: (RegExp.$1.length == 3 ? ("000" + o[k])
								.substr(("" + o[k]).length) : ("00" + o[k])
								.substr(("" + o[k]).length)));
			}
		}
		return style;
	},
	InitContainerPanel : function() {
		var str = '<div id="__WebCalendarPanel__" style="position:absolute;display:none;z-index:9999;" class="CalendarPanel"></div>';
		if (document.all) {
			str += '<iframe style="position:absolute; z-index:2000; width:expression(this.previousSibling.offsetWidth); ';
			str += 'height:expression(this.previousSibling.offsetHeight); ';
			str += 'left:expression(this.previousSibling.offsetLeft); top:expression(this.previousSibling.offsetTop); ';
			str += 'display:expression(this.previousSibling.style.display); " scrolling="no" frameborder="no"></iframe>';
		}
		var div = document.createElement("div");
		div.innerHTML = str;
		div.id = "__WebCalendarContainerPanel__";
		div.style.display = "none";
		document.body.appendChild(div);
	},
	ReturnDate : function(dt, sd) {
		if (this.dateControl != null) {
			this.dateControl.value = dt;
			this.dateControl.showTime = sd;
		}
		this.hide();
		if (this.dateControl.onchange == null) {
			return;
		}
		var ev = this.dateControl.onchange.toString();
		ev = ev.substring(((ev.indexOf("ValidatorOnChange();") > 0) ? ev
				.indexOf("ValidatorOnChange();") + 20 : ev.indexOf("{") + 1),
				ev.lastIndexOf("}"));
		var fun = new Function(ev);
		this.dateControl.changeEvent = fun;
		this.dateControl.changeEvent();
	},
	draw : function() {
		var calendar = this;
		var mvAry = [];
		mvAry[mvAry.length] = '<div style="margin: 0px; ">';
		mvAry[mvAry.length] = '<table width="100%" cellpadding="0" cellspacing="1" class="CalendarTop">';
		mvAry[mvAry.length] = '<tr class="title">';
		mvAry[mvAry.length] = '<th align="left" class="prevMonth"><input style="';
		if (calendar.DateMode > calendar.pickMode["month"]) {
			mvAry[mvAry.length] = 'display:none; ';
		}
		mvAry[mvAry.length] = '" id="__WebCalendarPrevMonth__" type="button" value="&lt;" /></th>';
		mvAry[mvAry.length] = '<th align="center" width="98%" nowrap="nowrap" class="YearMonth">';
		mvAry[mvAry.length] = '<select id="__WebCalendarYear__" class="Year"></select>';
		mvAry[mvAry.length] = '<select id="__WebCalendarMonth__" class="Month" style="';
		if (calendar.DateMode > calendar.pickMode["month"]) {
			mvAry[mvAry.length] = 'display:none;';
		}
		mvAry[mvAry.length] = '"></select></th>';
		mvAry[mvAry.length] = '<th align="right" class="nextMonth"><input style="';
		if (calendar.DateMode > calendar.pickMode["month"]) {
			mvAry[mvAry.length] = 'display:none;';
		}
		mvAry[mvAry.length] = '" id="__WebCalendarNextMonth__" type="button" value="&gt;" /></th>';
		mvAry[mvAry.length] = '</tr>';
		mvAry[mvAry.length] = '</table>';
		mvAry[mvAry.length] = '<table id="__WebCalendarTable__" width="100%" class="CalendarDate" style="';
		if (calendar.DateMode >= calendar.pickMode["month"]) {
			mvAry[mvAry.length] = 'display:none;';
		}
		mvAry[mvAry.length] = '" cellpadding="0" cellspacing="1">';
		mvAry[mvAry.length] = '<tr class="title">';
		for ( var i = 0; i < 7; i++) {
			mvAry[mvAry.length] = '<th>' + calendar.language["weeks"][calendar.lang][i] + '</th>';
		}
		mvAry[mvAry.length] = '</tr>';
		for ( var i = 0; i < 6; i++) {
			mvAry[mvAry.length] = '<tr align="center" class="date" style="display:;">';
			for ( var j = 0; j < 7; j++) {
				if (j == 0) {
					mvAry[mvAry.length] = '<td class="sun" tdname="tdSun" class="sun"></td>';
				} else if (j == 6) {
					mvAry[mvAry.length] = '<td class="sat" tdname="tdSat" class="sat"></td>';
				} else {
					mvAry[mvAry.length] = '<td class="day" tdname="tdDay" class="day"></td>';
				}
			}
			mvAry[mvAry.length] = '</tr>';
		}
		mvAry[mvAry.length] = '</table>';
		mvAry[mvAry.length] = '<table width="100%" class="CalendarTime" style="';
		if (calendar.DateMode >= calendar.pickMode["day"]) {
			mvAry[mvAry.length] = 'display:none;';
		}
		mvAry[mvAry.length] = '" cellpadding="0" cellspacing="1">';
		mvAry[mvAry.length] = '<tr><td align="center" colspan="7">';
		mvAry[mvAry.length] = '<select id="__WebCalendarHour__" class="Hour"></select>' + calendar.language["hour"][calendar.lang];
		mvAry[mvAry.length] = '<span style="';
		if (calendar.DateMode >= calendar.pickMode["hour"]) {
			mvAry[mvAry.length] = 'display:none;';
		}
		mvAry[mvAry.length] = '"><select id="__WebCalendarMinute__" class="Minute"></select>' + calendar.language["minute"][calendar.lang] + '</span>';
		mvAry[mvAry.length] = '<span style="';
		if (calendar.DateMode >= calendar.pickMode["minute"]) {
			mvAry[mvAry.length] = 'display:none;';
		}
		mvAry[mvAry.length] = '"><select id="__WebCalendarSecond__" class="Second"></select>' + calendar.language["second"][calendar.lang] + '</span>';
		mvAry[mvAry.length] = '</td></tr>';
		mvAry[mvAry.length] = '</table>';
		mvAry[mvAry.length] = '<div align="center" class="CalendarButtonDiv">';
		mvAry[mvAry.length] = '<input id="__WebCalendarClear__" type="button" value="' + calendar.language["clear"][calendar.lang] + '"/> ';
		mvAry[mvAry.length] = '<input id="__WebCalendarToday__" type="button" value="';
		mvAry[mvAry.length] = (calendar.DateMode == calendar.pickMode["day"]) ? calendar.language["today"][calendar.lang]
				: calendar.language["pickTxt"][calendar.lang];
		mvAry[mvAry.length] = '" /> ';
		mvAry[mvAry.length] = '<input id="__WebCalendarClose__" type="button" value="' + calendar.language["close"][calendar.lang] + '" />';
		mvAry[mvAry.length] = '</div>';
		mvAry[mvAry.length] = '</div>';
		calendar.panel.innerHTML = mvAry.join("");
		var obj = calendar.getElementById("__WebCalendarPrevMonth__");
		obj.onclick = function() {
			calendar.goPrevMonth(calendar);
		};
		obj.onblur = function() {
			calendar.onblur();
		};
		calendar.prevMonth = obj;
		obj = calendar.getElementById("__WebCalendarNextMonth__");
		obj.onclick = function() {
			calendar.goNextMonth(calendar);
		};
		obj.onblur = function() {
			calendar.onblur();
		};
		calendar.nextMonth = obj;
		obj = calendar.getElementById("__WebCalendarClear__");
		obj.onclick = function() {
			calendar.ReturnDate("", "");
		};
		calendar.calendarClear = obj;
		obj = calendar.getElementById("__WebCalendarClose__");
		obj.onclick = function() {
			calendar.hide();
		};
		calendar.calendarClose = obj;
		obj = calendar.getElementById("__WebCalendarYear__");
		obj.onchange = function() {
			calendar.update(calendar);
		};
		obj.onblur = function() {
			calendar.onblur();
		};
		calendar.calendarYear = obj;
		obj = calendar.getElementById("__WebCalendarMonth__");
		with (obj) {
			onchange = function() {
				calendar.update(calendar);
			};
			onblur = function() {
				calendar.onblur();
			};
		}
		calendar.calendarMonth = obj;
		obj = calendar.getElementById("__WebCalendarHour__");
		obj.onchange = function() {
			calendar.hour = this.options[this.selectedIndex].value;
		};
		obj.onblur = function() {
			calendar.onblur();
		};
		calendar.calendarHour = obj;
		obj = calendar.getElementById("__WebCalendarMinute__");
		obj.onchange = function() {
			calendar.minute = this.options[this.selectedIndex].value;
		};
		obj.onblur = function() {
			calendar.onblur();
		};
		calendar.calendarMinute = obj;
		obj = calendar.getElementById("__WebCalendarSecond__");
		obj.onchange = function() {
			calendar.second = this.options[this.selectedIndex].value;
		};
		obj.onblur = function() {
			calendar.onblur();
		};
		calendar.calendarSecond = obj;
		obj = calendar.getElementById("__WebCalendarToday__");
		obj.onclick = function() {
			var today = (calendar.DateMode != calendar.pickMode["day"]) ? new Date(
					calendar.year, calendar.month, calendar.day, calendar.hour,
					calendar.minute, calendar.second)
					: new Date();
			calendar.ReturnDate(calendar.formatDate(today, calendar.format),
					calendar.formatDate(today, calendar.sample));
		};
		calendar.calendarToday = obj;
	},
	bindYear : function() {
		var cy = this.calendarYear;
		cy.length = 0;
		for ( var i = this.beginYear; i <= this.endYear; i++) {
			cy.options[cy.length] = new Option(i
					+ this.language["year"][this.lang], i);
		}
	},
	bindMonth : function() {
		var cm = this.calendarMonth;
		cm.length = 0;
		for ( var i = 0; i < 12; i++) {
			cm.options[cm.length] = new Option(
					this.language["months"][this.lang][i], i);
		}
	},
	bindHour : function() {
		var ch = this.calendarHour;
		if (ch.length > 0) {
			return;
		}
		var H;
		for ( var i = 0; i < 24; i++) {
			H = ("00" + i + "").substr(("" + i).length);
			ch.options[ch.length] = new Option(H, H);
		}
	},
	bindMinute : function() {
		var cM = this.calendarMinute;
		if (cM.length > 0) {
			return;
		}
		var M;
		for ( var i = 0; i < 60; i++) {
			M = ("00" + i + "").substr(("" + i).length);
			cM.options[cM.length] = new Option(M, M);
		}
	},
	bindSecond : function() {
		var cs = this.calendarSecond;
		if (cs.length > 0) {
			return;
		}
		var s;
		for ( var i = 0; i < 60; i++) {
			s = ("00" + i + "").substr(("" + i).length);
			cs.options[cs.length] = new Option(s, s);
		}
	},
	goPrevMonth : function(e) {
		if (this.year == this.beginYear && this.month == 0) {
			return;
		}
		this.month--;
		if (this.month == -1) {
			this.year--;
			this.month = 11;
		}
		this.date = new Date(this.year, this.month, 1);
		this.changeSelect();
		this.bindData();
	},
	goNextMonth : function(e) {
		if (this.year == this.endYear && this.month == 11) {
			return;
		}
		this.month++;
		if (this.month == 12) {
			this.year++;
			this.month = 0;
		}
		this.date = new Date(this.year, this.month, 1);
		this.changeSelect();
		this.bindData();
	},
	changeSelect : function() {
		var calendar = this;
		var cy = calendar.calendarYear;
		var cm = calendar.calendarMonth;
		var ch = calendar.calendarHour;
		var cM = calendar.calendarMinute;
		var cs = calendar.calendarSecond;
		if (calendar.date.getFullYear() - calendar.beginYear < 0
				|| calendar.date.getFullYear() - calendar.beginYear >= cy.length) {
			cy[0].selected = true;
		} else {
			cy[calendar.date.getFullYear() - calendar.beginYear].selected = true;
		}
		cm[calendar.date.getMonth()].selected = true;
		ch[calendar.hour].selected = true;
		cM[calendar.minute].selected = true;
		cs[calendar.second].selected = true;
	},
	update : function(e) {
		this.year = e.calendarYear.options[e.calendarYear.selectedIndex].value;
		this.month = e.calendarMonth.options[e.calendarMonth.selectedIndex].value;
		this.date = new Date(this.year, this.month, 1);
		this.bindData();
	},
	bindData : function() {
		var calendar = this;
		if (calendar.DateMode >= calendar.pickMode["month"]) {
			return;
		}
		var dateArray = calendar.getMonthViewArray(calendar.date.getFullYear(),
				calendar.date.getMonth());
		var tds = calendar.getElementById("__WebCalendarTable__")
				.getElementsByTagName("td");
		for ( var i = 0; i < tds.length; i++) {
			tds[i].onclick = function() {
				return;
			};
			tds[i].onmouseover = function() {
				return;
			};
			tds[i].onmouseout = function() {
				return;
			};
			if (i > dateArray.length - 1)
				break;
			tds[i].innerHTML = dateArray[i];
			if (tds[i].getAttribute("tdname") == "tdSun") {
				tds[i].className = "sun";
			} else if (tds[i].getAttribute("tdname") == "tdSat") {
				tds[i].className = "sat";
			} else {
				tds[i].className = "day";
			}
			if (dateArray[i] != "  ") {
				var cur = new Date();
				tds[i].isToday = false;
				if (cur.getFullYear() == calendar.date.getFullYear()
						&& cur.getMonth() == calendar.date.getMonth()
						&& cur.getDate() == dateArray[i]) {
					tds[i].className = "today";
					tds[i].isToday = true;
				}
				if (calendar.dateControl != null) {
					if (calendar.selectTime.getDate() == dateArray[i]) {
						calendar.selectedDayTD = tds[i];
						tds[i].className = "selDay";
					}
				}
				tds[i].onclick = function() {
					if (calendar.DateMode == calendar.pickMode["day"]) {
						var tmpDate = new Date(calendar.date.getFullYear(),
								calendar.date.getMonth(), this.innerHTML);
						calendar.ReturnDate(calendar.formatDate(tmpDate,
								calendar.format), calendar.formatDate(tmpDate,
								calendar.sample));
					} else {
						if (calendar.selectedDayTD != null) {
							if (calendar.selectedDayTD.isToday) {
								calendar.selectedDayTD.className = "today";
							} else {
								if (calendar.selectedDayTD
										.getAttribute("tdname") == "tdSun") {
									calendar.selectedDayTD.className = "sun";
								} else if (calendar.selectedDayTD
										.getAttribute("tdname") == "tdSat") {
									calendar.selectedDayTD.className = "sat";
								} else {
									calendar.selectedDayTD.className = "day";
								}
							}
						}
						this.className = "selDay";
						calendar.day = this.innerHTML;
						calendar.selectedDayTD = this;
					}
				};
				tds[i].onmouseover = function() {
					this.className = "dayOver";
				};
				tds[i].onmouseout = function() {
					if (calendar.selectedDayTD != this) {
						if (this.isToday) {
							this.className = "today";
						} else {
							if (this.getAttribute("tdname") == "tdSun") {
								this.className = "sun";
							} else if (this.getAttribute("tdname") == "tdSat") {
								this.className = "sat";
							} else {
								this.className = "day";
							}
						}
					} else {
						this.className = "selDay";
					}
				};
				tds[i].onblur = function() {
					calendar.onblur();
				};
			}
		}
	},
	getMonthViewArray : function(y, m) {
		var mvArray = [];
		var dayOfFirstDay = new Date(y, m, 1).getDay();
		var daysOfMonth = new Date(y, m + 1, 0).getDate();
		for ( var i = 0; i < 42; i++) {
			mvArray[i] = "  ";
		}
		for ( var i = 0; i < daysOfMonth; i++) {
			mvArray[i + dayOfFirstDay] = i + 1;
		}
		return mvArray;
	},
	getElementById : function(id) {
		if (typeof (id) != "string" || id == "")
			return null;
		if (document.getElementById)
			return document.getElementById(id);
		if (document.all)
			return document.all(id);
		try {
			return eval(id);
		} catch (e) {
			return null;
		}
	},
	getElementsByTagName : function(object, tagName) {
		if (document.getElementsByTagName)
			return document.getElementsByTagName(tagName);
		if (document.all)
			return document.all.tags(tagName);
	},
	getAbsPoint : function(e) {
		var x = e.offsetLeft;
		var y = e.offsetTop;
		while (e = e.offsetParent) {
			x += e.offsetLeft;
			y += e.offsetTop;
		}
		return {
			"x" : x,
			"y" : y
		};
	},
	reset : function(args) {
		if (args.beginYear == null) {
			args.beginYear = this.beginYearLoad;
		}
		if (args.endYear == null) {
			args.endYear = this.endYearLoad;
		}
		if (args.lang == null) {
			args.lang = 0;
		}
		if (args.left == null) {
			args.left = 0;
		}
		if (args.top == null) {
			args.top = 0;
		}
		if (args.format == null) {
			args.format = "yyyy-MM-dd";
		}
		if (args.sample == null) {
			args.sample = "yyyy-MM-dd";
		}
		args.format = args.format + "";
		args.sample = args.sample + "";
		return args;
	},
	setSkin : function(args) {
		var hasSkin = false;
		if (args.skin == null)
			args.skin = "default";
		args.skin = args.skin + "";
		if (this.skinList.length == 0) {
			var linkList = document.getElementsByTagName("link");
			for ( var i = 0; i < linkList.length; i++) {
				if (linkList[i].getAttribute("skinType") == "WebCalendar") {
					this.skinList[this.skinList.length] = linkList[i];
				}
			}
		}
		for ( var i = 0; i < this.skinList.length; i++) {
			if (this.skinList[i].getAttribute("skin") == args.skin) {
				this.skinList[i].disabled = false;
				hasSkin = true;
			} else {
				this.skinList[i].disabled = true;
			}
		}
		if (!hasSkin) {
			for ( var i = 0; i < this.skinList.length; i++) {
				if (this.skinList[i].getAttribute("skin") == "default") {
					this.skinList[i].disabled = false;
					break;
				}
			}
		}
	},
	setPickMode : function(sample) {
		if (sample.indexOf('ss') > -1) {
			this.DateMode = this.pickMode["second"];
			return true;
		}
		if (sample.indexOf('mm') > -1) {
			this.DateMode = this.pickMode["minute"];
			return true;
		}
		if (sample.indexOf('HH') > -1) {
			this.DateMode = this.pickMode["hour"];
			return true;
		}
		if (sample.indexOf('dd') > -1) {
			this.DateMode = this.pickMode["day"];
			return true;
		}
		if (sample.indexOf('MM') > -1) {
			this.DateMode = this.pickMode["month"];
			return true;
		}
		if (sample.indexOf('yyyy') > -1) {
			this.DateMode = this.pickMode["year"];
			return true;
		}
		this.DateMode = this.pickMode["day"];
		return true;
	},
	doDraw : function(args) {
		if (this.skinLoad)
			this.setSkin(args);
		var isChange = false;
		args = this.reset(args);
		if (this.beginYear != args.beginYear || this.endYear != args.endYear
				|| this.lang != args.lang || this.sample != args.sample
				|| this.format != args.format) {
			isChange = true;
		}
		this.left = args.left;
		this.top = args.top;
		if (isChange) {
			this.beginYear = args.beginYear;
			this.endYear = args.endYear;
			this.lang = args.lang;
			this.setPickMode(args.sample);
			this.format = args.format;
			this.sample = args.sample;
		}
		return isChange;
	},
	showCalendar : function(dateObj, args, popControl) {
		if (document.getElementById("__WebCalendarContainerPanel__") == null) {
			this.InitContainerPanel();
			this.panel = this.getElementById("__WebCalendarPanel__");
			this.container = this
					.getElementById("__WebCalendarContainerPanel__");
		}
		var isChange = this.doDraw(args);
		if (dateObj == null) {
			throw new Error("arguments[0] is necessary");
		}
		if (dateObj.showTime == null)
			dateObj.showTime = "";
		this.dateControl = dateObj;
		var hasTime = ((dateObj.value.length > 0) ? (this.toDate(dateObj.value,
				this.sample)) : (new Date()));
		this.selectTime = ((dateObj.showTime.length > 0) ? (this.toDate(
				dateObj.showTime, this.sample)) : hasTime);
		this.date = this.selectTime;
		if (this.panel.innerHTML == "" || isChange) {
			this.draw();
			this.bindYear();
			this.bindMonth();
			this.bindHour();
			this.bindMinute();
			this.bindSecond();
		}
		this.year = this.date.getFullYear();
		this.month = this.date.getMonth();
		this.day = this.date.getDate();
		this.hour = this.date.getHours();
		this.minute = this.date.getMinutes();
		this.second = this.date.getSeconds();
		this.changeSelect();
		this.bindData();
		if (popControl == null) {
			popControl = dateObj;
		}
		var xy = this.getAbsPoint(popControl);
		this.panel.style.left = (xy.x + this.left) + "px";
		this.panel.style.top = (xy.y + this.top + dateObj.offsetHeight)+"px";this.panel.style.display="";this.container.style.display="";var calendar=this;if(!calendar.dateControl.isTransEvent){calendar.dateControl.isTransEvent=true;if(calendar.dateControl.onblur!=null){calendar.dateControl.blurEvent=calendar.dateControl.onblur;}calendar.dateControl.onblur=function(){calendar.onblur();if(typeof(this.blurEvent)=='function'){this.blurEvent();}};}calendar.container.onmouseover=function(){calendar.isFocus=true;};calendar.container.onmouseout=function(){calendar.isFocus=false;};},
hide:function(){this.panel.style.display="none";this.container.style.display="none";this.isFocus=false;},
onblur:function(){if(!(this.isFocus)){this.hide();}},
show:function(args0, args1){if(args1.object!=null){this.showCalendar(args1.object,args1);return true;}if(args1.id!=null){args1.id=args1.id+"";var obj=document.getElementById(args1.id);if(obj!=null){this.showCalendar(obj,args1);return true;}}if(typeof(args0)=='object'){this.showCalendar(args0,args1);return true;}else if(typeof(args0)=='string'){var obj=document.getElementById(args0);if(obj==null){return false;}this.showCalendar(obj,args1);return true;}return false;}
}
var __Calendar__=new CalendarHelper();