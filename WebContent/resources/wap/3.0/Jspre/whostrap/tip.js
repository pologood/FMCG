/*!
 * 提示框
 * @module Tip
 */
;

(function($) {
    //产生随机位数字符串前缀
    function randPrefixBit() {
        return ('00000' + (new Date()).getTime() * Math.random() << 0).toString(16).substr(-5).toUpperCase();
    }
    /**
     * Tip Class
     * Tip基类定义
     */
    WHT.Tip = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, WHT.Tip.DEFAULTS, options);
        //示例属性
        this.isShown = false;
    };
    //插件版本
    WHT.Tip.VERSION = '1.0.0';
    //默认配置(all)
    WHT.Tip.DEFAULTS = {
        tiptype: "toast",
        initialstate: {
            isShown: false
        },
        tpldatas: {
            ctns: {
                txt: "已完成"
            },
            hooks: {
                wrapperid: "", //wrapper id
                wrapperclass: "" //wrapper class
            },
            Ctrls: {
                tplname: "toast",
                innermode: false
            }
        }
    };
    //获取提示框标题
    WHT.Tip.prototype.getTipTitle = function() {
        //return this.options.text_title;
    };
    //隐藏提示框
    WHT.Tip.prototype.hide = function() {
        var this_instance = this;
        if (!this_instance.isShown) {
            return;
        }
        this_instance.$ele.hide();
        this_instance.isShown = false;
        this_instance.$ele.trigger($.Event('closed:WHT:tip'));
        //如果是一次性调用，隐藏之后直接销毁
        if (this_instance.$ele.hasClass('ONCE')) {
            //this_instance.destroy();
        }
    };
    //销毁
    WHT.Tip.prototype.destroy = function() {
        var this_instance = this;
        //do something then
        this_instance.$ele.remove();
        //this = null;
    };
    //显示提示框
    WHT.Tip.prototype.show = function() {
        //var args = arguments[0];
        var this_instance = this;
        if (this_instance.isShown) {
            return;
        }
        this_instance.$ele.show();
        //console.log(args);
        this_instance.isShown = true;
        this_instance.$ele.trigger($.Event('opened:WHT:tip'));
    };
    //toggle提示框
    WHT.Tip.prototype.toggle = function() {
        var this_instance = this;
        if (this_instance.isShown) {
            this_instance.hide();
        } else {
            this_instance.show();
        }
    };
    //初始化提示框
    WHT.Tip.prototype.init = function() {
        var this_instance = this;
        //this_instance.build();
        //结构搭建完毕
        $.when(this_instance.build())
            .done(function() {
                if (this_instance.options.initialstate.isShown) {
                    this_instance.show();
                }
            })
            .fail(function() {
                console.log("网络异常");
            });

    };
    //搭建html结构
    WHT.Tip.prototype.build = function() {
        var def = $.Deferred();
        var this_instance = this;
        hbsTO.render('tip', function(t) {
            var htmlcreated = t(this_instance.options.tpldatas);
            this_instance.$ele.html(htmlcreated);
            //默认采用外部模式
            if (!this_instance.options.tpldatas.Ctrls.innermode) {
                //上wrapper id
                this_instance.$ele.attr('id', this_instance.options.tpldatas.hooks.wrapperid);
                //上wrapper class
                this_instance.$ele.addClass("weui_tip_" + this_instance.options.tpldatas.Ctrls.tplname).addClass(this_instance.options.tpldatas.hooks.wrapperclass);
                this_instance.$ele.attr('data-tiptype', this_instance.options.tiptype);
                this_instance.$ele.css({
                    display: 'none'
                });
            }
            def.resolve();
        });
        return def.promise();
    };
    /**
     * Toast Class
     * Toast类定义
     */
    WHT.Toast = function(ele, options) {
        //call the parent constructor method
        WHT.Tip.call(this, ele, options);
        //define self property
        //配置重置
        this.options = $.extend(true, {}, WHT.Toast.DEFAULTS, options);
        //如果已存在实例
        if ($.type(WHT.Toast.instance) == "object") {
            return WHT.Toast.instance;
        }
        //初始化
        this.init();
        //event声明
        //toast不监听外部事件
        //this.$ele.on('click.WHT.tip.toast', '[jhk="cancel"]', $.proxy(this.hide, this));
        //缓存this到唯一实例
        WHT.Toast.instance = this;
        //如果实例化时带入事务
        if (this.options.affair) {
            //this.addAffair(this.options.affair);
        }
    };
    WHT.Toast.prototype = new WHT.Tip();
    WHT.Toast.prototype.Constructor = WHT.Toast;
    //默认配置
    WHT.Toast.DEFAULTS = {
        tiptype: "toast",
        initialstate: {
            isShown: false
        },
        defalutduration: 2000, //单位ms
        tpldatas: {
            ctns: {
                txt: "已完成"
            },
            hooks: {
                wrapperid: "", //wrapper id
                wrapperclass: "" //wrapper class
            },
            Ctrls: {
                tplname: "toast",
                innermode: false
            }
        }
    };
    //任务队列
    WHT.Toast.prototype.tasklist = [];
    //任务队列活动探测器
    WHT.Toast.prototype.list_detector = null;
    WHT.Toast.prototype.tasklist_manager_timer = null;
    //WHT.Toast.prototype.tasklist_manager_timer
    //舞台任务记录器
    WHT.Toast.prototype.taskstage = {
        begintime: 0,
        showtimer: null
    };
    //显示提示框
    WHT.Toast.prototype.show = function() {
        var this_instance = this;
        WHT.Tip.prototype.show.call(this);
    };
    //改变该元素的主体内容
    WHT.Toast.prototype.changeEleCtn = function(taskctn) {
        var this_instance = this;
        if (taskctn.sign) {
            this_instance.$ele.find('.weui_toast_sign').html(taskctn.sign);
        }
        if (taskctn.txt) {
            this_instance.$ele.find('.weui_toast_content').text(taskctn.txt);
        }
    };
    //激活队列活动探测器
    WHT.Toast.prototype.enableListDetector = function() {
        var this_instance = this;
        var $loading = $(".weui_tip_loadingtoast");
        if ($loading.length && $loading.css("display") == "none") {
            //结束探测
            if (WHT.Toast.prototype.list_detector) {
                clearTimeout(WHT.Toast.prototype.list_detector);
            }
            //toast可以出现
            this_instance.runTasklistManager();
        } else { //loadingtoast正在显示
            //开启定时器进行探测
            WHT.Toast.prototype.list_detector = setTimeout(function() {
                this_instance.enableListDetector();
            }, 500);
        }
    };
    //任务队列管理器-轮询探测
    WHT.Toast.prototype.tasklistManagerDetect = function() {
        var this_instance = this;
        if (WHT.Toast.prototype.taskstage.begintime !== 0) {
            WHT.Toast.prototype.tasklist_manager_timer = setTimeout(function() {
                //查看队列中是否有其他任务
                if (WHT.Toast.prototype.tasklist.length) {
                    console.log("探测轮询 探测到新任务，结束本次轮询");
                    //清空舞台任务
                    WHT.Toast.prototype.taskstage.begintime = 0;
                    clearTimeout(WHT.Toast.prototype.taskstage.showtimer);
                    this_instance.hide();
                    clearTimeout(WHT.Toast.prototype.tasklist_manager_timer);
                    setTimeout(function() {
                        //重新开始整个过程
                        this_instance.runTasklistManager();
                    }, 100);
                } else {
                    console.log("轮询探测 继续探测");
                    this_instance.tasklistManagerDetect();
                }

            }, 100);
        } else {
            if (WHT.Toast.prototype.tasklist_manager_timer) {
                clearTimeout(WHT.Toast.prototype.tasklist_manager_timer);
            }
        }
    };
    //队列运行管理器
    WHT.Toast.prototype.runTasklistManager = function() {
        var this_instance = this;
        console.log("队列管理器运行：新的一轮");
        if (WHT.Toast.prototype.tasklist.length) {
            //取第一个任务放上stage
            var firsttask = WHT.Toast.prototype.tasklist.shift();
            //使用该任务中的配置改变当前元素的显示内容
            this_instance.putTaskOnStage(firsttask);
            //任务队列管理器-轮询探测器
            this_instance.tasklistManagerDetect();
        }
    };
    //将任务放上舞台
    WHT.Toast.prototype.putTaskOnStage = function(task) {
        var this_instance = this;
        //使用该任务中的配置改变当前元素的显示内容
        this_instance.changeEleCtn(task);
        this_instance.show();
        //重置舞台任务
        WHT.Toast.prototype.taskstage.begintime = (new Date()).getTime();
        WHT.Toast.prototype.taskstage.showtimer = setTimeout(function() {
            this_instance.hide();
            WHT.Toast.prototype.taskstage.begintime = 0;
            clearTimeout(WHT.Toast.prototype.taskstage.showtimer);
        }, this_instance.options.defalutduration);
    };
    //增加任务到集合
    WHT.Toast.prototype.addTask = function(task) {
        var this_instance = this;
        var addition = false;
        //如果是外部调用
        if ($.type(task) == "array") {
            task = task[0];
            //如果舞台上有任务  追加标志置true
            if (WHT.Toast.prototype.taskstage.begintime !== 0) {
                addition = true;
                WHT.Toast.prototype.tasklist.push(task);
                console.log(WHT.Toast.prototype.tasklist);
            } else { //如果舞台上没有任务
                WHT.Toast.prototype.tasklist.push(task);
                this_instance.enableListDetector();
                console.log(WHT.Toast.prototype.tasklist);
            }
        }
    };
    /**
     * LoadingToast Class
     * LoadingToast类定义
     * LoadingToast具有特殊性，将其定义成单例
     */
    WHT.LoadingToast = function(ele, options) {

        //call the parent constructor method
        WHT.Tip.call(this, ele, options);
        //define self property
        //配置重置
        this.options = $.extend(true, {}, WHT.LoadingToast.DEFAULTS, options);
        //如果已存在实例
        if ($.type(WHT.LoadingToast.instance) == "object") {
            if (this.options.affair) {
                //实例化添加模式，暂未处理
                //WHT.LoadingToast.prototype.instancecoll.push(this.options.affair);
            }
            return WHT.LoadingToast.instance;
        }
        //初始化
        this.init();
        /* 
        $.when(this.options.affair)
            .done(function(data) {
                console.log(data.name);
            });*/

        //event声明
        //loadingtoast不监听外部事件
        /* 
        this.$ele.on('click.WHT.tip.loadingtoast', '[jhk="ok"]', $.proxy(function() {
            this.$ele.trigger($.Event('determined:WHT:tip:loadingtoast'));
            this.hide();
        }, this));*/
        //this.$ele.on('click.WHT.tip.loadingtoast', '[jhk="cancel"]', $.proxy(this.hide, this));
        //分派事务结束事件到主线处理器
        this.$ele.on('affairEnd:WHT:tip:loadingtoast', $.proxy(this.mainTimerHandlder, this));
        //缓存this到唯一实例
        WHT.LoadingToast.instance = this;
        //如果实例化时带入事务
        //增加一个事务到集合
        if (this.options.affair) {
            this.addAffair(this.options.affair);
        }
    };
    WHT.LoadingToast.prototype = new WHT.Tip();
    WHT.LoadingToast.prototype.Constructor = WHT.LoadingToast;

    //默认配置
    WHT.LoadingToast.DEFAULTS = {
        tiptype: "loadingtoast",
        initialstate: {
            isShown: false
        },
        defalutduration: 10000, //单位ms
        tpldatas: {
            ctns: {
                txt: "数据加载中"
            },
            hooks: {
                wrapperid: "",
                wrapperclass: ""
            },
            Ctrls: {
                tplname: "loadingtoast",
                innermode: false
            }
        }
    };
    //实例(标识)集合
    WHT.LoadingToast.prototype.instancecoll = {};
    //主线定时器
    WHT.LoadingToast.prototype.maintimer = null;
    //开始主线定时器
    WHT.LoadingToast.prototype.startMainTimer = function(isfirstrun) {
        var this_instance = this;
        if (isfirstrun) {
            console.log("主定时器开启");
        } else {
            console.log("主定时器重置");
        }
        this_instance.maintimer = setTimeout(function() {
            this_instance.hide();
            //到达最长持续时间强行关闭
            this_instance.$ele.trigger($.Event('mainTimerEnd:WHT:tip:loadingtoast'));
        }, this_instance.options.defalutduration);
    };
    //主线进程处理器
    WHT.LoadingToast.prototype.mainTimerHandlder = function(e, affairid) {
        var this_instance = this;

        //主线定时器非空
        if (this_instance.maintimer) {
            //
        }
        //如果此时只剩下一个事务,不是最后一个事务无权力关闭loadingtoast
        if (Object.keys(WHT.LoadingToast.prototype.instancecoll).length == 1) {
            //赋空主线定时器并关闭loadingtoast
            clearTimeout(this_instance.maintimer);
            this_instance.maintimer = null;
            console.log("事务" + affairid + "结束");
            //事务处理完毕，直接隐藏
            this_instance.hide();
            console.log("loadingtoast窗口被事务" + affairid + "关闭");
            //事务集合清空，主线定时器清空，正常结束事件
            this_instance.$ele.trigger($.Event('mainTimerEnd:WHT:tip:loadingtoast'));
        } else {
            console.log("事务" + affairid + "结束");
            //集合中删除自身
            //delete WHT.LoadingToast.prototype.instancecoll[affairid];
        }
        delete WHT.LoadingToast.prototype.instancecoll[affairid];
    };
    //增加事务到集合
    WHT.LoadingToast.prototype.addAffair = function(affair) {
        var this_instance = this;
        var addition = false;
        //如果是外部调用
        if ($.type(affair) == "array") {
            //如果当前队列中有事务  追加标志置true
            if (Object.keys(WHT.LoadingToast.prototype.instancecoll).length) {
                addition = true;
            } else { //如果当前队列中没有事务 新建定时器
                this_instance.startMainTimer(true);
                this_instance.show();
            }
            //取得事务对象
            affair = affair[0];
        }
        //var this_instance = this;
        var affairid = randPrefixBit();
        WHT.LoadingToast.prototype.instancecoll[affairid] = {
            begintime: (new Date()).getTime(), //用总毫秒数标识
            affair: affair
        };
        console.log("事务" + affairid + "进入队列");
        //addition
        if (addition && this_instance.maintimer) {
            //重置主线定时器
            clearTimeout(this_instance.maintimer);
            this_instance.maintimer = null;
            this_instance.startMainTimer();
        }

        //事务结束
        affair.always(function() {
            //发送事务结束事件，主线程定时器监听到就删除集合中对应的事务
            this_instance.$ele.trigger($.Event('affairEnd:WHT:tip:loadingtoast'), [affairid]);
        });
    };
    //显示提示框
    WHT.LoadingToast.prototype.show = function() {
        WHT.Tip.prototype.show.call(this);
    };
    //初始化提示框
    WHT.LoadingToast.prototype.init = function() {
        var this_instance = this;
        //this_instance.build();
        //结构搭建完毕
        $.when(this_instance.build())
            .done(function() {
                if (this_instance.options.initialstate.isShown) {
                    this_instance.show();
                    //开启主线定时器
                    this_instance.startMainTimer(true);
                } else {
                    //this_instance.$ele.remove();
                }
            })
            .fail(function() {
                console.log("网络异常");
            });

    };
    //销毁
    WHT.LoadingToast.prototype.destroy = function() {
        var this_instance = this;
        //do something then
        this_instance.$ele.remove();
        //this = null;
        WHT.LoadingToast.instance = null;
        WHT.LoadingToast.prototype.instancecoll = {};
    };
    /**
     * TipFactory Class
     * 提示框工厂类定义
     */
    var TipFactory = function() {};
    //default class
    TipFactory.prototype.tipclass = WHT.Toast;
    //method: create tip
    TipFactory.prototype.createTip = function(ele, options) {
        switch (options.tiptype) {
            case "toast":
                this.tipclass = WHT.Toast;
                break;
            case "loadingtoast":
                this.tipclass = WHT.LoadingToast;
                //return this.tipclass(ele, options);
                break;
            default:
                this.tipclass = WHT.Tip;
        }
        return new this.tipclass(ele, options);
    };

    /**
     * Tip Plugin
     * 提示框插件定义
     */
    function Plugin(option) {
        var initial_arguments = arguments;
        //console.log("before init i get the $ element length is");
        //console.log(this.length);
        //如果是空元素并且选择符.ONCE
        if (this.length === 0) {
            //目前只支持解析类名
            var sltor = this.selector;
            //如果是全部大写 eg: .ONCE.SOGA
            if (sltor == sltor.toUpperCase()) {
                $("<div class='" + sltor.split(".").join(" ") + "'></div>").appendTo('body').tip(option);
            }
        }
        // each function
        return this.each(function() {
            var $this = $(this);
            //试获取对象实例
            var data_plugin = $this.data('TH.tip');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    var instance_TipFactory = new TipFactory();
                    data_plugin = instance_TipFactory.createTip(this, option);
                    $this.data('TH.tip', data_plugin);
                    //data_plugin.init();
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    console.info('Method ' + option + ' does not exist on Zepto.tip');
                }
            }
        });
    }
    //如果之前存在其他的tip插件，暂存
    var old = $.fn.tip;
    //挂载到$原型链
    $.fn.tip = Plugin;
    //构造器指向类Tip
    $.fn.tip.Constructor = TipFactory;

    //Tip noConflict
    $.fn.tip.noConflict = function() {
        $.fn.tip = old;
        return this;
    };
    //暂不支持data-api
    $(function() {
        $(".ONCE").tip({
            tiptype: "loadingtoast",
        });
        $(".ONCET").tip({
            tiptype: "toast",
        });
    });
})(Zepto);
