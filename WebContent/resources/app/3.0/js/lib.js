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
        return _TH_.base+"/resources/app/3.0/js/template/"+ name + ".hbs";
    }
});
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
        return (i < 10 ? '0' : '') + i;
    };
    return fo.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
        switch (a) {
            case 'yyyy':
                return tf(t.getFullYear());
            case 'MM':
                return tf(t.getMonth() + 1);
            case 'dd':
                return tf(t.getDate());
            case "HH":
                return tf(t.getHours());
            case "mm":
                return tf(t.getMinutes());
            case "ss":
                return tf(t.getSeconds());
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

    if (second === 0) {
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
        if (k % 4 == 2 && a[0].charAt(i + 2) !== 0 && a[0].charAt(i + 1) === 0) re = AA[0] + re;
        if (a[0].charAt(i) !== 0) re = AA[a[0].charAt(i)] + BB[k % 4] + re;
        k++;
    }

    if (a.length > 1) //加上小数部分(如果有小数部分)
    {
        re += BB[6];
        for (var kk = 0; kk < a[1].length; kk++) re += AA[a[1].charAt(kk)];
    }
    return re;
}

/**
 * 返回随机色值
 * @return {[type]} [description]
 */
function randomColorValue() {
    var colorstr = '';
    for (var i = 0; i < 3; i++) {
        var onecolor = parseInt(Math.random() * 255, 10).toString(16);
        if (onecolor.length === 1) {
            onecolor = '0' + onecolor;
        }
        colorstr += onecolor;
    }
    return '#' + colorstr;
}

/**
 * 返回一个随机时长
 * @param  {[type]} min 范围开始值
 * @param  {[type]} max 范围结束值
 * @return {[type]}     [description]
 */
function randomTimeDuration(min, max) {
    //var range=;//5
    //8-13
    if ($.type(min) != "number" || $.type(max) != "number") {
        return null;
    }
    return (min + Math.random() * (max - min)).toFixed(3);
}
//randomTimeDuration(8,13)

/**
 * 返回指定范围内能够允许的最大的数值N，指定范围：S
 * 即：S >= N + (N - 1) + (N - 2) + ... + 3 + 2 + 1
 * S >= N * (N - 1);
 * @param  {整型} maxrange 范围值
 * @return {整型sogas}  [description]
 */
function maxNumberLessThanAccum(maxrange) {
    var N = 0;
    var sum = 0;

    while (sum <= maxrange) {
        N++;
        sum = sum + N;
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
    var newTop = $(window).scrollTop();
    var oldTop = newTop;
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
    ScrollChange.prototype.init = function() {
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
    };
    return ScrollChange;
})(Zepto);

/**
 * 固定元素占位副本赋高
 * 调用时需注意调用位置(调用时该fixed元素是否存在)
 * @param  {[type]} empty_slter    [description]
 * @param  {[type]} fixedele_slter [description]
 * @return {[type]}                [description]
 */
function setEmptyEleH(arg) {
    //占位元素赋高
    if ($.type(arg) == "array") {
        $.each(arg, function(index, value) {
            /* iterate through array or object */
            $("[data-emptyfor='" + value + "']").height($("[data-emptyto='" + value + "']").height());
        });
    }
    if ($.type(arg) == "string") {
        $("[data-emptyfor='" + arg + "']").height($("[data-emptyto='" + arg + "']").height());
    }
}
//setEmptyEleH("topbar")

/**
 * 根据经纬度获取城市名
 * @return {[type]} [description]
 */
function getCityNameByPos(lat, lng, rturl) {
    var urlbase = _TH_.base ? _TH_.base : '';
    rturl = rturl ? rturl : urlbase + '/app/lbs/get.jhtml';
    return $.ajax({
        url: rturl,
        data: {
            lat: lat,
            lng: lng,
            force: true
        }
    });
}

/**
 * 微信接口调用封装
 * @return {[type]} [description]
 */
var _wxSDK = {};
//获取并验证公众号配置信息
_wxSDK.checkConfig = function() {
    //获取公众号配置信息
    $.ajax({
            url: _TH_.base + '/app/mutual/get_config.jhtml',
            data: { url: location.href.split('#')[0] }
        })
        .done(function(message) {
            if (message.type == "success") {
                var data = JSON.parse(message.content);
                //验证公众号信息
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: data.appId, // 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr, // 必填，生成签名的随机串
                    signature: data.signature, // 必填，签名，见附录1
                    jsApiList: ["scanQRCode", "onMenuShareTimeline", "onMenuShareAppMessage", "getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
                //保存基本分享信息
                if (!store.session.get('baseshareinfo')) {
                    store.session.set('baseshareinfo', {
                        title: data.sharetitle,
                        desc: data.sharedescr,
                        link: data.sharelink,
                        imgUrl: data.shareimage
                    });
                }
            } else {
                //showDialog2("提示", message.content);
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: message.content
                });
            }
        })
        .fail(function() {
            $(".ONCET").tip("addTask", {
                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                txt: "公众号配置信息调取失败"
            });
        });
};
//调取地理位置
_wxSDK.getLocation = function() {
    var $def = $.Deferred();
    if (store.session.get('userlocation')) { //如果从缓存中得到了用户当前位置
        $def.resolve(store.session.get('userlocation').lat, store.session.get('userlocation').lng);
    } else {
        wx.checkJsApi({
            jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                if (res.checkResult.getLocation) {
                    wx.getLocation({
                        type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                        success: function(res) {
                            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                            var speed = res.speed; // 速度，以米/每秒计
                            var accuracy = res.accuracy; // 位置精度
                            /* 
                            $(".ONCET").tip("addTask", {
                                txt: "纬度:" + latitude + ",经度:" + longitude
                            });*/
                            store.session.set('userlocation', {
                                lat: latitude,
                                lng: longitude
                            });
                            $def.resolve(latitude, longitude);
                        },
                        fail: function() {
                            $def.reject();
                        }
                    });
                }
            }
        });
    }
    return $def.promise();
};
//调取分享接口
_wxSDK.onMenuShare = function() {
    var $def = $.Deferred();
    wx.checkJsApi({
        jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'],
        success: function(res) {
            // 以键值对的形式返回，可用的api值true，不可用为false
            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            var _title = $("meta[property='og:title']").attr("content");
            var _desc = $("meta[property='og:desc']").attr("content");
            var _link = $("meta[property='og:link']").attr("content");
            var _imgUrl = $("meta[property='og:imgUrl']").attr("content");
            var shareopts = {
                title: _title ? _title : store.session.get('baseshareinfo').title,
                desc: _desc ? _desc : store.session.get('baseshareinfo').desc,
                link: _link ? _link : store.session.get('baseshareinfo').link,
                imgUrl: _imgUrl ? _imgUrl : store.session.get('baseshareinfo').imgUrl
            };
            if (res.checkResult.onMenuShareTimeline) {
                wx.onMenuShareTimeline(shareopts);
            }
            if (res.checkResult.onMenuShareAppMessage) {
                wx.onMenuShareAppMessage(shareopts);
            }
            $def.resolve();
        }
    });
    return $def.promise();
};
//调取二维码扫描
_wxSDK.scanQRCode = function() {
    wx.checkJsApi({
        jsApiList: ['scanQRCode'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
        success: function(res) {
            // 以键值对的形式返回，可用的api值true，不可用为false
            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            wx.scanQRCode({
                desc: '扫一扫'
            });
        }
    });
};
//初始化微信接口
_wxSDK.initInterface = function($script, callbacks) {
    $script('http://res.wx.qq.com/open/js/jweixin-1.0.0.js', function() {
        //拿到接口对象并且可以使用wx
        //微信接操纵对象事件监听,配置信息验证通过立即调用的接口行为
        wx.ready(function() {
            //每个页面都需要的分享接口调用
            _wxSDK.onMenuShare()
                .always(function() {
                    //其他接口调用
                    if (callbacks && callbacks.afterOnMenuShare) {
                        callbacks.afterOnMenuShare();
                    }
                });
        });
        wx.error(function(res) {
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            $(".ONCET").tip("addTask", {
                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                txt: "公众号配置信息错误"
            });
        });
        //获取并验证公众号配置信息
        _wxSDK.checkConfig();
    });
};

/**
 * 序列化星级
 * 根据固定html结构中元素提供的数字，把星星级别化
 */
;
(function($) {
    $.fn.starLevelize = function() {
        return this.each(function(index, ele) {
            var $ele = $(ele);
            //get flag
            if ($ele.data("starlevelized")) {
                return;
            }
            var starnum = parseFloat($ele.data("starnum")).toFixed(1);
            var starnum_int = parseInt(starnum, 10); //3
            var starnum_dec_per = parseInt((starnum - starnum_int) * 100, 10) + "%"; //0.7
            //3个fullstar 1个half star 1个empty star
            if (starnum_int <= 5) {
                $ele.children().eq(starnum_int).css({
                    background: '-webkit-linear-gradient(left, #ff6d06 ' + starnum_dec_per + ', #bfbebc ' + starnum_dec_per + ')'
                });
            }
            for (var i = starnum_int + 1; i < 5; i++) {
                $ele.children().eq(i).css({
                    background: '-webkit-linear-gradient(left, #bfbebc, #bfbebc)'
                });
            }
            //set flag
            $ele.data("starlevelized", true);
        });
    };
})(Zepto);
//$(".starlevels").starLevelize();
