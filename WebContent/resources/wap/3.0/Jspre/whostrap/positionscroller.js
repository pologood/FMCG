/*!
 * 位置滚动器
 * @module PositionScroller
 */
;

(function($) {
    /**
     * PositionScroller Class
     * PositionScroller基类定义
     */
    WHT.PositionScroller = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, WHT.PositionScroller.DEFAULTS, options);
        //示例属性
        this.isShown = false;

        //初始化
        this.init();
        //event声明
        //整个元素点击事件
        this.$ele.on('click.WHT.positionscroller', $.proxy(this.scrollActionDistributor, this));
    };
    //插件版本
    WHT.PositionScroller.VERSION = '1.0.0';
    //默认配置(all)
    WHT.PositionScroller.DEFAULTS = {
        plugintype: "positionscroller",
        autohide: false,
        autohide_threshold: $(window).height(),
        targetposY: "top", //可选值："top","bottom",300(数值，单位px)
        tpldatas: {
            ctns: {
                head: '<i class="iconfont">&#xe664;</i>', //回到顶部图标
                body: "" //默认文本为空
            },
            hooks: {
                wrapperid: "", //wrapper id
                wrapperclass: "" //wrapper class
            },
            Ctrls: {
                tplname: "positionscroller",
                innermode: false
            }
        }
    };
    //应用位置探测器    
    WHT.PositionScroller.prototype.applyPositionDetect = function() {
        var this_instance = this;
        //实例化位置探测器并作为属性附加
        this_instance.scrolldetector = new ScrollChange({
            wrap_slter: "." + this_instance.$ele.attr('class').split(" ").join("."),
            threshold: this_instance.options.autohide_threshold,
            custom_justBelowThreshold: function() {
                var this_sd = this;
                $(this_sd.config.wrap_slter).on('animationend.WHT.positionscroller.anmIn webkitAnimationEnd.WHT.positionscroller.anmIn', function(event) {
                    $(this).show().removeClass('anm-in');
                    $(this).off('animationend.WHT.positionscroller.anmIn webkitAnimationEnd.WHT.positionscroller.anmIn');
                    this_sd.statelocked.below = false;
                });
                setTimeout(function() {
                    $(this_sd.config.wrap_slter).show().addClass('anm-in');
                }, 0);
                //$(this.wrap_slter).show();
            },
            custom_belowThreshold: function() {
                if ($(this.wrap_slter).css('display') == "none") {
                    $(this.wrap_slter).show();
                }
            },
            custom_justOverThreshold: function() {
                var this_sd = this;
                $(this_sd.config.wrap_slter).on('animationend.WHT.positionscroller.anmOut webkitAnimationEnd.WHT.positionscroller.anmOut', function(event) {
                    $(this).hide().removeClass('anm-out');
                    $(this).off('animationend.WHT.positionscroller.anmOut webkitAnimationEnd.WHT.positionscroller.anmOut');
                    this_sd.statelocked.over = false;
                });
                setTimeout(function() {
                    $(this_sd.config.wrap_slter).addClass('anm-out');
                }, 0);
                //$(this.wrap_slter).hdie();
            },
            custom_overThreshold: function() {
                if ($(this.wrap_slter).css('display') == "block") {
                    $(this.wrap_slter).hide();
                }
            }
        });
        $(window).on('touchmove scroll', $.proxy(this_instance.scrolldetector.movehandler, this_instance.scrolldetector));
    };
    //获取提示框标题
    WHT.PositionScroller.prototype.getPositionScrollerTitle = function() {
        //return this.options.text_title;
    };
    //滚动行为分发器
    WHT.PositionScroller.prototype.scrollActionDistributor = function() {
        var this_instance = this;
        var targetposY = this_instance.options.targetposY;
        //顶部底部快捷方式
        if ($.type(targetposY) == "string") {
            switch (targetposY) {
                case "top":
                    this_instance.scrollToTop();
                    break;
                case "bottom":
                    //this_instance.scrollToBottom();//暂时未想到解决方案
                    break;
                default:
                    // statements_def
                    break;
            }
        }
        //具体数值
        if ($.type(targetposY) == "number") {
            this_instance.scrollToPosition(targetposY);
        }
    };
    //按步长缓动
    WHT.PositionScroller.prototype.easingMoveByStep = function(scopeobj, direction, final_posY) {
        //滚动基准值
        final_posY = final_posY || 0;
        var this_instance = this;
        var timerframe = null;
        //将被设置的scrollTop值
        var scopesum = direction == "up" ? -scopeobj.sum : scopeobj.sum;
        //var scopesum = scopeobj.sum;
        //初始步长
        var initialsteplength = scopeobj.n;

        function moveByStep(steplength) {
            var next_scopesum = 0;
            if (timerframe) {
                cancelAnimationFrame(timerframe);
            }
            this_instance.jumpToPosition(scopesum + final_posY);
            //下一距离值
            //scopesum = scopesum - steplength; //下一距离值

            if (scopesum >= 0) {
                next_scopesum = scopesum - steplength; //下一距离值
            } else {
                next_scopesum = scopesum + steplength; //下一距离值
            }
            //var next_scopesum = scopesum - steplength; //下一距离值
            //下一步长
            //步长递减到1时会保持1不变
            steplength = steplength > 1 ? steplength - 1 : 1;

            if (Math.abs(scopesum) > steplength) {
                //next 赋现
                scopesum = next_scopesum;
                timerframe = requestAnimationFrame(function(argument) {
                    moveByStep(steplength);
                });
            } else {
                this_instance.jumpToPosition(final_posY);
            }
        }
        moveByStep(initialsteplength);
    };
    //回到顶部
    WHT.PositionScroller.prototype.scrollToTop = function() {
        var this_instance = this;
        var view_height = $(window).height();
        //获取根据预先设定的范围的返回值
        //传入范围值进行缓动
        this_instance.easingMoveByStep(maxNumberLessThanAccum(view_height), "down");
    };
    //跳转到底部,功能暂不可用,未想到好的解决方案
    WHT.PositionScroller.prototype.scrollToBottom = function() {
        var this_instance = this;
        var view_height = $(window).height();
        var S = $(document).height() - ($(window).scrollTop() + $(window).height());
        //获取根据预先设定的范围的返回值
        //传入范围值进行缓动
        //this_instance.easingMoveByStep(maxNumberLessThanAccum(S), "up", );
    };
    //滚动到指定位置
    WHT.PositionScroller.prototype.scrollToPosition = function(targetposY) {
        var this_instance = this;
        //元素纵向偏移量(纵向目的地)
        var ele_offsetY = targetposY;
        //文档当前scrollTop值
        var doc_scrolltop = $(window).scrollTop();
        var S = 0;

        if (ele_offsetY > doc_scrolltop) { //应向上滚动
            if (ele_offsetY + $(window).height() <= $(document).height()) { //元素可以提到窗口最顶端
                S = ele_offsetY - doc_scrolltop;
            } else {
                S = $(document).height() - (doc_scrolltop + $(window).height());

            }
            // then check S
            if (S < $(window).height()) { //距离未超过一屏高度
                //直接缓动
                //传入范围值进行缓动
                this_instance.easingMoveByStep(maxNumberLessThanAccum(S), "up", ele_offsetY);
            } else {
                //跳至一屏高度
                this_instance.jumpToPosition(ele_offsetY - $(window).height());
                //开始缓动
                this_instance.easingMoveByStep(maxNumberLessThanAccum($(window).height()), "up", ele_offsetY);
            }
        } else { //应向下滚动
            S = doc_scrolltop - ele_offsetY;
            if (S < $(window).height()) { //距离未超过一屏高度
                //直接缓动
                this_instance.easingMoveByStep(maxNumberLessThanAccum(S), "down", ele_offsetY);
            } else {
                //跳至一屏高度
                this_instance.jumpToPosition(ele_offsetY + $(window).height());
                //开始缓动
                this_instance.easingMoveByStep(maxNumberLessThanAccum($(window).height()), "down", ele_offsetY);
            }
        }
    };
    //隐藏
    WHT.PositionScroller.prototype.hide = function() {
        var this_instance = this;
        if (!this_instance.isShown) {
            return;
        }
        this_instance.$ele.hide();
        this_instance.isShown = false;
        this_instance.$ele.trigger($.Event('closed:WHT:positionscroller'));
    };
    //显示
    WHT.PositionScroller.prototype.show = function() {
        //var args = arguments[0];
        var this_instance = this;
        if (this_instance.isShown) {
            return;
        }
        this_instance.$ele.show();
        this_instance.isShown = true;
        this_instance.$ele.trigger($.Event('opened:WHT:positionscroller'));
    };
    //搭建html结构 默认是隐藏态->display:none
    WHT.PositionScroller.prototype.build = function() {
        var def = $.Deferred();
        var this_instance = this;
        hbsTO.render('positionscroller', function(t) {
            var htmlcreated = t(this_instance.options.tpldatas);
            this_instance.$ele.html(htmlcreated);
            //默认采用外部模式
            if (!this_instance.options.tpldatas.Ctrls.innermode) {
                //上wrapper id
                this_instance.$ele.attr('id', this_instance.options.tpldatas.hooks.wrapperid);
                //上wrapper class
                this_instance.$ele.addClass("weui_positionscroller").addClass(this_instance.options.tpldatas.hooks.wrapperclass);
                this_instance.$ele.attr('data-plugintype', this_instance.options.plugintype);
                this_instance.$ele.css({
                    display: 'none'
                });
            }
            def.resolve();
        });
        return def.promise();
    };
    //直接跳到指定位置
    WHT.PositionScroller.prototype.jumpToPosition = function(posY) {
        $(window).scrollTop(posY);
    };
    //初始化提示框
    WHT.PositionScroller.prototype.init = function() {
        var this_instance = this;
        //搭建结构
        $.when(this_instance.build())
            .done(function() {
                if (this_instance.options.autohide) {
                    //附加位置探测
                    this_instance.applyPositionDetect();
                }
                //发送初始化完毕事件
                this_instance.$ele.trigger($.Event('initialized:WHT:positionscroller'));
            })
            .fail(function() {
                console.log("网络异常");
            });
        //this_instance.show();
    };

    /**
     * PositionScrollerFactory Class
     * 提示框工厂类定义
     */
    var PositionScrollerFactory = function() {};
    //default class
    PositionScrollerFactory.prototype.dutyclass = WHT.PositionScroller;
    //method: create positionscroller
    PositionScrollerFactory.prototype.createPositionScroller = function(ele, options) {
        switch (options.plugintype) {
            case "SomeSubClassName":
                this.dutyclass = WHT.SomeSubClass;
                break;
            default:
                this.dutyclass = WHT.PositionScroller;
        }
        return new this.dutyclass(ele, options);
    };

    /**
     * PositionScroller Plugin
     * 提示框插件定义
     */
    function Plugin(option) {
        var initial_arguments = arguments;
        //如果是空元素并且选择符.NULL
        if (this.length === 0 && this.selector == ".NULL") {
            return null;
        }
        // each function
        return this.each(function() {
            var $this = $(this);
            //试获取对象实例
            var data_plugin = $this.data('TH.positionscroller');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    var instanceof_factory = new PositionScrollerFactory();
                    data_plugin = instanceof_factory.createPositionScroller(this, option);
                    $this.data('TH.positionscroller', data_plugin);
                    //data_plugin.init();
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    $.info('Method ' + option + ' does not exist on Zepto.positionscroller');
                }
            }


        });
    }
    //如果之前存在其他的positionscroller插件，暂存
    var old = $.fn.positionScroller;
    //挂载到$原型链
    $.fn.positionScroller = Plugin;
    //构造器指向类PositionScroller
    $.fn.positionScroller.Constructor = PositionScrollerFactory;

    //PositionScroller noConflict
    $.fn.positionScroller.noConflict = function() {
        $.fn.positionScroller = old;
        return this;
    };
    //暂不支持data-api
})(Zepto);
