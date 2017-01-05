/**
 * Handlebars RegisterHelper
 */
Handlebars.registerHelper("expression", function(left, operator, right, options) {
    if (arguments.length < 3) {
        throw new Error('Handlerbars Helper "compare" needs 2 parameters');
    }
    var operators = {
        '==': function(l, r) {
            return l == r;
        },
        '===': function(l, r) {
            return l === r;
        },
        '!=': function(l, r) {
            return l != r;
        },
        '!==': function(l, r) {
            return l !== r;
        },
        '<': function(l, r) {
            return l < r;
        },
        '>': function(l, r) {
            return l > r;
        },
        '<=': function(l, r) {
            return l <= r;
        },
        '>=': function(l, r) {
            return l >= r;
        },
        'typeof': function(l, r) {
            return typeof l == r;
        }
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

Handlebars.registerHelper("compare", function(v1, v2, options) {
    if (v1 == v2) {
        //满足添加继续执行
        return options.fn(this);
    } else {
        //不满足条件执行{{else}}部分
        return options.inverse(this);
    }
});

Handlebars.registerHelper('addOne', function(index) {
    this._index = index + 1;
    return this._index;
});

Handlebars.registerHelper('formatdistance', function(disvalue) {
    /* 
    if (disvalue == "-1") {
        
    } else {
        
    }*/
    if (disvalue >= 0) {
        return (parseFloat(disvalue) / 1000).toFixed(2) + "km";
    } else {
        return "";
    }
});

Handlebars.registerHelper('ifCond', function(v1, v2, options) {
    if (v1 === v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});

//注册一个比较字符串是否一致的Helper,有options参数，块级Helper
Handlebars.registerHelper("compare1", function(v1, v2, options) {
    //判断v1、v2是否相等
    v1 = v1.replace(/^\s+|\s+$/g, "");
    v2 = v2.replace(/^\s+|\s+$/g, "");
    if (v1 == v2) {
        //继续执行
        return options.fn(this);
    } else {
        //执行else部分
        return options.inverse(this);
    }
});

function getFormatTime(time, fo) {
    var t = new Date(time);
    var tf = function(i) {
        return (i < 10 ? '0' : '') + i
    };
    return fo.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
        switch (a) {
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case "HH":
                return tf(t.getHours());
                break;
            case "mm":
                return tf(t.getMinutes());
                break;
            case "ss":
                return tf(t.getSeconds());
                break;
        }
    });
}

Handlebars.registerHelper('formatDate', function(time, fo) {
    return getFormatTime(time, fo);
});

Handlebars.registerHelper('timeFormatUtil', function(time) {
    return timeFormatUtil(time);
});

function timeFormatUtil(time) {
    var t = new Date(time);
    var millisecond = new Date().getTime() - t.getTime();
    var second = millisecond / 1000;
    if (second <= 0) {
        second = 0;
    }

    if (second == 0) {
        interval = "刚刚";
    } else if (second < 30) {
        interval = parseInt(second) + "秒以前";
    } else if (second >= 30 && second < 60) {
        interval = "半分钟前";
    } else if (second >= 60 && second < 60 * 60) { //大于1分钟 小于1小时
        var minute = second / 60;
        interval = (parseInt(minute)) + "分钟前";
    } else if (second >= 60 * 60 && second < 60 * 60 * 24) { //大于1小时 小于24小时
        var hour = (second / 60) / 60;
        interval = (parseInt(hour)) + "小时前";
    } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) { //大于1D 小于2D
        interval = "昨天" + getFormatTime(time, "HH:mm");
    } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) { //大于2D小时 小于 7天
        var day = ((second / 60) / 60) / 24;
        interval = (parseInt(day)) + "天前";
    } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) { //大于7天小于365天
        interval = getFormatTime(time, "MM-dd HH:mm");
    } else if (second >= 60 * 60 * 24 * 365) { //大于365天
        interval = getFormatTime(time, "yyyy-MM-dd HH:mm");
    } else {
        interval = "0";
    }
    return interval;
}

Handlebars.registerHelper('list', function(items, options) {
    for (var i = 0; i < items.length; i++) {
        return options.fn(items[0]);
    }
});

function NoToChinese(num) {
    if (!/^\d*(\.\d*)?$/.test(num)) {
        alert("Number is wrong!");
        return "Number is wrong!";
    }
    var AA = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");
    var BB = new Array("", "拾", "佰", "仟", "萬", "億", "点", "");
    var a = ("" + num).replace(/(^0*)/g, "").split("."),
        k = 0,
        re = "";
    for (var i = a[0].length - 1; i >= 0; i--) {
        switch (k) {
            case 0:
                re = BB[7] + re;
                break;
            case 4:
                if (!new RegExp("0{4}\\d{" + (a[0].length - i - 1) + "}$").test(a[0]))
                    re = BB[4] + re;
                break;
            case 8:
                re = BB[5] + re;
                BB[7] = BB[5];
                k = 0;
                break;
        }
        if (k % 4 == 2 && a[0].charAt(i + 2) != 0 && a[0].charAt(i + 1) == 0) re = AA[0] + re;
        if (a[0].charAt(i) != 0) re = AA[a[0].charAt(i)] + BB[k % 4] + re;
        k++;
    }

    if (a.length > 1) //加上小数部分(如果有小数部分)
    {
        re += BB[6];
        for (var i = 0; i < a[1].length; i++) re += AA[a[1].charAt(i)];
    }
    return re;
}

/*
 * This decorates Handlebars.js with the ability to load
 * templates from an external source, with light caching.
 * 
 * To render a template, pass a closure that will receive the 
 * template as a function parameter, eg, 
 *   hbsTO.render('templateName', function(t) {
 *       $('#somediv').html( t() );
 *   });
 * Source: https://github.com/wycats/handlebars.js/issues/82
 * little modified by simboo
 */
var HbsTemplateOperation = function() {
    this.cached = {};
};
var hbsTO = new HbsTemplateOperation();
$.extend(HbsTemplateOperation.prototype, {
    render: function(name, callback) {
        if (hbsTO.isCached(name)) {
            callback(hbsTO.cached[name]);
        } else {
            $.get(hbsTO.urlFor(name), function(raw) {
                hbsTO.store(name, raw);
                hbsTO.render(name, callback);
            });
        }
    },
    renderSync: function(name, callback) {
        if (!hbsTO.isCached(name)) {
            hbsTO.fetch(name);
        }
        hbsTO.render(name, callback);
    },
    prefetch: function(name) {
        $.get(hbsTO.urlFor(name), function(raw) { 
            hbsTO.store(name, raw);
        });
    },
    fetch: function(name) {
        // synchronous, for those times when you need it.
        if (! hbsTO.isCached(name)) {
            var raw = $.ajax({'url': hbsTO.urlFor(name), 'async': false}).responseText;
            hbsTO.store(name, raw);         
        }
    },
    isCached: function(name) {
        return !!hbsTO.cached[name];
    },
    store: function(name, raw) {
        hbsTO.cached[name] = Handlebars.compile(raw);
    },
    urlFor: function(name) {
        return "/resources/wap/2.0/js/template/"+ name + ".hbs";
    }
});
/**
 * 返回指定范围内能够允许的最大的数值N，指定范围：S
 * 即：S >= N + (N - 1) + (N - 2) + ... + 3 + 2 + 1
 * S >= N * (N - 1);
 * @param  {整型} maxrange 范围值
 * @return {[type]}  [description]
 */
function maxNumberLessThanAccum(maxrange) {
    var N = 0;
    var sum = 0;

    while (sum <= maxrange) {
        sum = sum + ++N;
    }
    return {
        sum: sum - N,
        n: --N
    };
}

/**
 * 元素到达指定(相应)位置(门限)切换状态，版本3.0
 * @param  {[type]} 
 * @return {[type]}    [description]
 */
var ScrollChange = (function($) {
    //设置初始值
    var oldTop = newTop = $(window).scrollTop();
    //默认配置
    var defaults = {
        //custom_overThreshold: function() {},
        //custom_justOverThreshold: function() {},
        //custom_belowThreshold: function() {},
        //custom_justBelowThreshold: function() {},
        count: 0, //调试使用，打印顺序
        threshold: 50, //默认门限值，单位px
        timerframe: null //定时器赋null
    };
    /**
     * 声明
     * @param {[type]} opts [description]
     */
    function ScrollChange(opts) {
        //如果不传入自身slter，直接返回
        if (!opts.wrap_slter) {
            return;
        }
        this.config = $.extend(true, {}, defaults, opts);
        //状态锁
        this.statelocked = {
            over: false,
            below: false
        };
        //实例化之后立即检测当前位置
        //this.checkPositionAndDo($(window).scrollTop());
        this.init();
        //事件绑定
        //$(window).on('touchmove scroll', $.proxy(this.movehandler, this));
    }
    //初始化
    ScrollChange.prototype.init = function(arguments) {
        var scrollTop_now = $(window).scrollTop();
        if (scrollTop_now <= this.config.threshold) {
            //赋予over终止状态
            this.overThreshold(scrollTop_now / this.config.threshold);
        } else {
            //赋予below终止状态
            this.belowThreshold();
        }
    };
    /**
     * 当前位置检测
     * @return {[type]} [description]
     */
    ScrollChange.prototype.checkPositionAndDo = function(scrollTop_now) {
        //如果在门限以下
        if (scrollTop_now <= this.config.threshold) { ////如果在水位线以上
            var heightrate = scrollTop_now / this.config.threshold;
            //getSpecialColorValue(255,bgrate,140)

            //如果刚刚超过水位线
            if ($(this.config.wrap_slter).data("statelock") == "Y") {
                //重置statelock
                this.justOverThreshold();
                $(this.config.wrap_slter).removeData('statelock');
            } else {
                this.overThreshold(heightrate);
            }

        } else { //如果在水位线以下

            //如果刚刚低于水位线
            if (!$(this.config.wrap_slter).data("statelock")) {
                this.justBelowThreshold();
                $(this.config.wrap_slter).data("statelock", "Y");
            } else {
                this.belowThreshold();
            }

        }
    };
    /**
     * 高于水位线执行的动作
     * @return {[type]} [description]
     */
    ScrollChange.prototype.overThreshold = function(heightrate) {
        var this_instance = this;
        //over 非锁定状态才会执行over的行为
        if (!this_instance.statelocked.over) {
            //自定义动作
            if (this.config.custom_overThreshold) {
                this.config.custom_overThreshold(heightrate);
            }
        }
        //默认动作
        //console.info("在水位线之上up");
    };
    /**
     * 刚刚高于水位线执行的动作
     * @return {[type]} [description]
     */
    ScrollChange.prototype.justOverThreshold = function() {
        var this_instance = this;
        this_instance.statelocked.over = true;
        //默认动作
        //console.info("刚刚高于水位线");
        //自定义动作
        if (this.config.custom_justOverThreshold) {
            //this.config.custom_justOverThreshold();
            this.config.custom_justOverThreshold.call(this_instance);
        }
        //1.5s之后强制自动解锁over状态
        setTimeout(function() {
            this_instance.statelocked.over = false;
        }, 1500);
    };
    /**
     * 低于水位线执行的动作
     * @return {[type]} [description]
     */
    ScrollChange.prototype.belowThreshold = function() {
        var this_instance = this;
        //默认动作
        //console.info("在水位线之下down");
        if (!this_instance.statelocked.below) {
            //自定义动作
            if (this.config.custom_belowThreshold) {
                this.config.custom_belowThreshold();
            }
        }

    };
    /**
     * 刚刚低于水位线的执行动作
     * @return {[type]} [description]
     */
    ScrollChange.prototype.justBelowThreshold = function() {
        var this_instance = this;
        this_instance.statelocked.below = true;
        //默认动作
        //console.info("刚刚低于水位线");
        //自定义动作
        if (this.config.custom_justBelowThreshold) {
            //this.config.custom_justBelowThreshold();
            this.config.custom_justBelowThreshold.call(this_instance);
        }
        setTimeout(function() {
            this_instance.statelocked.below = false;
        }, 1500);
    };
    /**
     * 触摸递归
     * @return {[type]} [description]
     */
    ScrollChange.prototype.movehandler = function() {
        var this_sc = this;
        //如果frametimer已经存在，取消frametimer
        if (this.config.timerframe) {
            cancelAnimationFrame(this.config.timerframe);
        }
        newTop = $(window).scrollTop();
        //console.info(++(this.config.count), oldTop, newTop);
        if (newTop === oldTop) {
            //console.info("滚动停止");
            if (this.config.timerframe) {
                cancelAnimationFrame(this.config.timerframe);
            }
            //write code here
            this.checkPositionAndDo(newTop);
        } else {
            oldTop = newTop;
            this.config.timerframe = requestAnimationFrame(function() {
                this_sc.movehandler();
            });
        }
    }
    return ScrollChange;
})(Zepto);
