/*
 * API工具实例apiUtil
 * darell.wu 
 * create 20111128 11:30
 */
var apiUtil = (function() {

	// 事件监听器
	var _eventListeners = {};

	return {
		/*
		 * 用jQuery.ajax发送请求<br>
		 * 如果VideoApisParams.sessionHolder不为空，在url后加上sessionHolder参数以保证会话不丢失
		 * 如果VideoApisParams.crossDomain设为true，在url后加上jsoncallback=?以支持跨域请求<br>
		 * 
		 * @param options
		 *            options的dataType值一定是'json'
		 */
		jsonAjax : function(options) {
			if (options) {
				options.url = this._urlWarp(options.url);
			}
			options.dataType = "json";
			$.ajax(options);
		},
		/*
		 * 调用jQuery.getJSON,并支持跨域请求
		 * 
		 * @param url
		 */
		getJSON : function(url, data, callback) {
			$.getJSON(this._urlWarp(url), data, callback);
		},
		_urlWarp : function(url) {
			if (wordBook.getApiInfo().PARAMS.sessionHolder && url) {
				if (url.indexOf('?') >= 0) {
					url = url + "&sessionHolder=" + wordBook.getApiInfo().PARAMS.sessionHolder;
				} else {
					url = url + "?sessionHolder=" + wordBook.getApiInfo().PARAMS.sessionHolder;
				}
			}
			if (wordBook.getApiInfo().PARAMS.crossDomain && url) {
				if (url.indexOf('?') >= 0) {
					url = url + "&jsoncallback=?";
				} else {
					url = url + "?jsoncallback=?";
				}
			}
			return url;
		},
		lTrim : function(str) {
			var _str = new String(str);
			var result = '';
			for ( var a = 0; a < str.length; a++) {
				if (_str.charAt(a) != '\n' && _str.charAt(a) != '\r') {
					result += _str.charAt(a);
				}
			}
			return result;
		},
		/*
		 * 向videoApi注册一个事件监听器
		 * 
		 * @param event
		 *            事件名
		 * @param
		 *            listenerFunc监听器，形如functoin(paramObj){}的方法，paramObj参数依事件的不同而不同，详见各使用者的事件常量定义
		 */
		addEventListener : function(event, listenerFunc) {
			if (!_eventListeners[event]) {
				_eventListeners[event] = [];
			}
			$.merge(_eventListeners[event], [ listenerFunc ]);
		},
		// 触发特定事件
		notifyEvent : function(event, paramObj) {
			if (_eventListeners[event]) {
				$.each(_eventListeners[event], function(i, listenerFunc) {
					if ($.isFunction(listenerFunc)) {
						listenerFunc(paramObj);
					}
				});
			}
		}
	};
})();
