var EventsList = function(e1, e2, options) {
	var $main = e1;
	var $list = $main.find('#events-list');
	var $pullDown = $main.find('#pull-down');
	var $pullDownLabel = $main.find('#pull-down-label');
	var $pullUp = $main.find('#pull-up');
	var $pullUpLabel = $main.find('#pull-up-label');
	var topOffset = -$pullDown.outerHeight();

	this.compiler = Handlebars.compile(e2.html());
	Handlebars.registerHelper("expression", function(left, operator, right, options) {
		if (arguments.length < 3) {
	           throw new Error('Handlerbars Helper "compare" needs 2 parameters');
	         }
	         var operators = {
	           '==':     function(l, r) {return l == r; },
	           '===':    function(l, r) {return l === r; },
	           '!=':     function(l, r) {return l != r; },
	           '!==':    function(l, r) {return l !== r; },
	           '<':      function(l, r) {return l < r; },
	           '>':      function(l, r) {return l > r; },
	           '<=':     function(l, r) {return l <= r; },
	           '>=':     function(l, r) {return l >= r; },
	           'typeof': function(l, r) {return typeof l == r; }
	         };

	         if (!operators[operator]) {
	           throw new Error('Handlerbars Helper "compare" doesn\'t know the operator ' + operator);
	         }

	         var result = operators[operator](left, right);

	         if (result) {
	           return options.fn(this);
	         } else {
	           return options.inverse(this);
	         }
	});

	Handlebars.registerHelper("isNotNull", function(object) {
		if (object == null || object == "") {
			return ""
		} else {
			return object;
		}
	});

	Handlebars.registerHelper("formatDate", function(value) {
		if (value == null || value == "") {
			return "";
		} else {
			var date = new Date(value);
			var formartDate = date.Format("yyyy-MM-dd hh:mm");
			return formartDate;
		}
	});
	Handlebars.registerHelper("formatDistance", function(value) {
		if (value == null || value == "") {
			return "--";
		} else {
			if (value % 1000 > 0) {
				return (value / 1000).toFixed(2) + "km";
			} else {
				return value + "m";
			}
		}
	});
	Handlebars.registerHelper("abbreviate", function(value, count, str) {
		if (value == null || value == "") {
			return "";
		} else {
			var length = value.length;
			if (length <= count) {
				return value;
			}
			var result = value.substring(0, count);
			if (str) {
				result += str;
			}
			return result;
		}
	});
	
	Handlebars.registerHelper("multi", function(value, count) {
		if (value == null || value == "" || count == null || count == "") {
			return "0";
		} else {
			return parseInt(value) * parseInt(count);
		}
	});
	
	this.prev = this.next = this.start = options.params.pageNumber;
	this.total = null;
	this.addEvents = function() {
	};

	this.initEevents = function() {
		new FastClick(document.body);
		this.addEvents();
	}

	this.getUrl = function(params) {
		var queries = [];
		for ( var key in params) {
			if (key !== 'api' && key != "pageNumber") {
				queries.push(key + '=' + params[key]);
			}
		}
		queries.push("pageNumber=");
		return params.api + "?" + queries.join('&');
	};

	this.renderList = function(start, type, target) {
		var _this = this;
		var $el = $pullDown;

		if (type === 'load') {
			$el = $pullUp;
		}
		_this.setLoading($el);
		$.ajax({
					url : this.getUrl(options.params) + start,
					type : "get",
					dataType : "json",
					success : function(data) {
						_this.total = data.totalPages;
						var html = _this.compiler(data.content);
						if (type === 'refresh') {
							$list.html(html);
						} else if (type === 'load') {
							if ($list.find(".scroll_stuff").length > 0) {
								$list.find(".scroll_stuff").remove();
							}
							$list.append(html);
						} else {
							$list.html(html);
						}
						if (data.total < data.pageSize) {
							$list.append("<div class=\"scroll_stuff\" style=\"height:500px;\"><div>");
						}
						if (data.pageNumber >= data.totalPages) {
							if (data.pageNumber > data.totalPages) {
								$list
										.find(".scroll_stuff")
										.html("<div class=\"am-text-center am-margin-top-lg \"><i style=\"color: #ccc\" class=\"am-icon-exclamation-circle am-icon-lg am-icon-btn-sm\"></i><p style=\"font-size: x-small;\">没有相关记录 !</p></div>");
							}
							$pullUpLabel.html("亲，别再刷了，到底了");
						} else {
							$pullUpLabel.html("点击加载更多...");
							$pullDownLabel.html("下拉刷新");
						}
						_this.initEevents();
						// refresh iScroll
						setTimeout(function() {
							lazyloadimg();
							_this.resetLoading($el);
							 //_this.iscroll.refresh();
							// $list.masonry({
							// itemSelector : '.msry-item'
							// });
							// $list.masonry("appended",
							// html).masonry('layout');
							var element = e1.attr("id");
							$("#" + element).trigger("scroll");
						}, 50);
						if (type !== 'load') {
							if (target) {
							} else {
								//_this.iscroll.scrollTo(0, topOffset, 800,IScroll.utils.ease.elastic);
							}
						}
					}
				});

	};

	this.setLoading = function($el) {
		if ($el) {
			$el.addClass('loading');
		}
	};

	this.resetLoading = function($el) {
		if ($el) {
			$el.removeClass('loading');
		}
	};

	this.resetParams = function(params) {
		options.params = params;
	}

	this.init = function(target) {
		//var element = e1.attr("id");
		//var myScroll = this.iscroll = new IScroll("#" +element, {
		//	click:false,
		//	hScrollbar : false,
		//	vScrollbar : false});

		var _this = this;
		var pullFormTop = false;
		var pullStart;
		//myScroll.refresh();
		this.renderList(options.params.pageNumber, "refresh", target);
    
	//	myScroll.on('scrollStart', function() {
	//	  if (this.y >= topOffset) {
	//	     pullFormTop = true;
	//	  }

	//	  pullStart = this.y;
		  //$("#" + element).trigger("scroll");
		  // console.log(this);
	//	});

	//	myScroll.on('scrollEnd', function() {
		  
		  // console.log(this.directionY);
	//	  if (pullFormTop && this.directionY === -1) {
	//		     setTimeout(function() { 
	//	   _this.handlePullDown();
	//		}, 1000); //1秒  
	//	  }
	//	  pullFormTop = false;

		// pull up to load more
	//	  if (pullStart === this.y && (this.directionY === 1)) {
	//	     setTimeout(function() { 
	//	      _this.handlePullUp();
	//			}, 1000); //1秒  
	//	  }
	//	});
		
		$pullUpLabel.on("click", function() {
			_this.handlePullUp();
		});
        
		//$("#" + element).trigger("scroll");
		
	//	document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);  
		new FastClick(document.body);
	};

	this.handlePullDown = function() {
		if (this.prev > 0) {
			$pullDownLabel.html("努力加载中...");
			  this.prev = options.params.pageNumber;
			  this.renderList(this.prev, 'refresh');
		} else {
			$pullDownLabel.html("亲，别再刷了，到底了");
		}
	};

	this.handlePullUp = function() {
		if (this.next < this.total) {
			$pullUpLabel.html("努力加载中...");
			  this.next += 1;
			  this.renderList(this.next, 'load');
		} else {
			console.log(this.next);
		}
	}

};
