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
