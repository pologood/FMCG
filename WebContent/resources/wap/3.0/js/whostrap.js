/*!
 * whostrap v1.0.0
 * COPYRIGHT 2016, TiaoHuo ALL RIGHTS RESERVED.
 * @namespace WHT
 * @author simboo
 */
//class namespace
var WHT = {};
/*!
 * 对话框
 * @module Dialog
 */
;
(function($) {
    /**
     * Dialog Class
     * Dialog基类定义
     */
    WHT.Dialog = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, WHT.Dialog.DEFAULTS, options);
        //示例属性
        this.isShown = false;
    };
    //插件版本
    WHT.Dialog.VERSION = '1.0.0';
    //默认配置(all)
    WHT.Dialog.DEFAULTS = {
        dialogtype: "alert",
        initialstate: {
            isShown: false
        },
        tpldatas: {
            ctns: {
                txt_ok: "确定",
                txt_cancel: "取消",
                txt_title: "对话框标题",
                txt_body: "对话框提示内容"
            },
            hooks: {
                wrapperid: "",
                wrapperclass: ""
            },
            Ctrls: {
                tplname: "alert",
                innermode: false
            }
        }
    };
    //获取对话框标题
    WHT.Dialog.prototype.getDialogTitle = function() {
        return this.options.text_title;
    };
    //隐藏对话框
    WHT.Dialog.prototype.hide = function() {
        var this_instance = this;
        if (!this_instance.isShown) {
            return;
        }
        this_instance.$ele.hide();
        this_instance.isShown = false;
        this_instance.$ele.trigger($.Event('closed:WHT:dialog'));
    };
    //显示对话框
    WHT.Dialog.prototype.show = function() {
        //var args = arguments[0];
        var this_instance = this;
        if (this_instance.isShown) {
            return;
        }
        this_instance.$ele.show();
        //console.log(args);
        this_instance.isShown = true;
        this_instance.$ele.trigger($.Event('opened:WHT:dialog'));
    };
    //toggle对话框
    WHT.Dialog.prototype.toggle = function() {
        var this_instance = this;
        if (this_instance.isShown) {
            this_instance.hide();
            this_instance.isShown = false;
        } else {
            this_instance.show();
            this_instance.isShown = true;
        }
    };
    //初始化对话框
    WHT.Dialog.prototype.init = function() {
        var this_instance = this;
        //搭建结构
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
    WHT.Dialog.prototype.build = function() {
        var this_instance = this;
        var def = $.Deferred();
        hbsTO.render('dialog', function(t) {
            var htmlcreated = t(this_instance.options.tpldatas);
            this_instance.$ele.html(htmlcreated);
            //默认采用外部模式
            if (!this_instance.options.tpldatas.Ctrls.innermode) {
                this_instance.$ele.attr('id', this_instance.options.tpldatas.hooks.wrapperid);
                this_instance.$ele.addClass("weui_dialog_" + this_instance.options.tpldatas.Ctrls.tplname).addClass(this_instance.options.tpldatas.hooks.wrapperclass);
                this_instance.$ele.attr('data-dialogtype', this_instance.options.dialogtype);
                this_instance.$ele.css({
                    display: 'none'
                });
            }
            def.resolve();
        });
        return def.promise();
    };
    /**
     * Alert Class
     * Alert类定义
     */
    WHT.Alert = function(ele, options) {
        //call the parent constructor method
        WHT.Dialog.call(this, ele, options);
        //define self property
        //配置重置
        this.options = $.extend(true, {}, WHT.Alert.DEFAULTS, options);
        //初始化
        this.init();
        //event声明
        this.$ele.on('click.WHT.dialog.alert', '[jhk="ok"]', $.proxy(this.hide, this));
    };
    WHT.Alert.prototype = new WHT.Dialog();
    WHT.Alert.prototype.Constructor = WHT.Alert;
    //默认配置
    WHT.Alert.DEFAULTS = {
        dialogtype: "alert",
        initialstate: {
            isShown: false
        },
        tpldatas: {
            ctns: {
                txt_ok: "alert确定",
                txt_title: "alert标题",
                txt_body: "alert提示内容"
            },
            hooks: {
                wrapperid: "",
                wrapperclass: ""
            },
            Ctrls: {
                tplname: "alert",
                innermode: false
            }
        }
    };
    /**
     * Confirm Class
     * Confirm类定义
     */
    WHT.Confirm = function(ele, options) {
        //call the parent constructor method
        WHT.Dialog.call(this, ele, options);
        //define self property
        //配置重置
        this.options = $.extend(true, {}, WHT.Confirm.DEFAULTS, options);
        //初始化
        this.init();
        //event声明
        this.$ele.on('click.WHT.dialog.confirm', '[jhk="ok"]', $.proxy(function() {
            this.$ele.trigger($.Event('determined:WHT:dialog:confirm'));
            this.hide();
        }, this));
        this.$ele.on('click.WHT.dialog.confirm', '[jhk="cancel"]', $.proxy(this.hide, this));
    };
    WHT.Confirm.prototype = new WHT.Dialog();
    WHT.Confirm.prototype.Constructor = WHT.Confirm;
    //默认配置
    WHT.Confirm.DEFAULTS = {
        dialogtype: "confirm",
        initialstate: {
            isShown: false
        },
        tpldatas: {
            ctns: {
                txt_ok: "confirm确定",
                txt_cancel: "confirm取消",
                txt_title: "confirm提示",
                txt_body: "confirm提示内容"
            },
            hooks: {
                wrapperid: "",
                wrapperclass: ""
            },
            Ctrls: {
                tplname: "confirm",
                innermode: false
            }
        }
    };
    /**
     * DialogFactory Class
     * 对话框工厂类定义
     */
    var DialogFactory = function() {};
    //default class
    DialogFactory.prototype.dialogclass = WHT.Alert;
    //method: create dialog
    DialogFactory.prototype.createDialog = function(ele, options) {
        switch (options.dialogtype) {
            case "alert":
                this.dialogclass = WHT.Alert;
                break;
            case "confirm":
                this.dialogclass = WHT.Confirm;
                break;
            default:
                this.dialogclass = WHT.Dialog;
        }
        return new this.dialogclass(ele, options);
    };
    /**
     * Dialog Plugin
     * 对话框插件定义
     */
    function Plugin(option) {
        var initial_arguments = arguments;
        console.log("before init i get the $ element length is");
        console.log(this.length);
        //如果是空元素并且选择符.NULL
        if (this.length === 0 && this.selector == ".NULL") {
            var soga = this.selector;
            console.log(soga);
        }
        // each function
        return this.each(function() {
            var $this = $(this);
            //试获取对象实例
            var data_plugin = $this.data('TH.dialog');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    var instance_DialogFactory = new DialogFactory();
                    data_plugin = instance_DialogFactory.createDialog(this, option);
                    $this.data('TH.dialog', data_plugin);
                    //data_plugin.init();
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    $.info('Method ' + option + ' does not exist on Zepto.dialog');
                }
            }
            //console.log("the isShown's value is:");
            //console.log(data_plugin.getDialogTitle());
        });
    }
    //如果之前存在其他的dialog插件，暂存
    var old = $.fn.dialog;
    //挂载到$原型链
    $.fn.dialog = Plugin;
    //构造器指向类Dialog
    $.fn.dialog.Constructor = DialogFactory;
    //Dialog noConflict
    $.fn.dialog.noConflict = function() {
        $.fn.dialog = old;
        return this;
    };
    //暂不支持data-api
})(Zepto);

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

/*!
 * 页签导航
 * @module TabNav
 */
;
(function($) {
    /**
     * TabNav Class
     * TabNav基类定义
     */
    WHT.TabNav = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, WHT.TabNav.DEFAULTS, options);
        //变量
        this.activeindex = this.options.initialstate.activeindex; //激活页签索引值
        //内容是否是附加式
        this.ctnadditional = this.options.tpldatas.ctns.bodys ? false : true;
        //初始化
        this.init();
        //event声明
        //tab-head点击事件
        this.$ele.on('click.WHT.tabnav.head', '.weui_tabnav_item', $.proxy(this.headClickhandler, this));
        //this.$ele.find('.weui_tabnav_item').on('click.WHT.tabnav.head', $.proxy(this.headClickhandler, this));
    };
    //插件版本
    WHT.TabNav.VERSION = '1.0.0';
    //默认配置(all)
    WHT.TabNav.DEFAULTS = {
        plugintype: "tabnav",
        initialstate: {
            activeindex: 0
        },
        tpldatas: {
            ctns: {
                heads: ["选项一", "选项二", "选项三"]
                    //bodys: ["bodyA", "bodyB", "bodyC"]
            },
            hooks: {
                wrapperid: "", //wrapper id
                wrapperclass: "", //wrapper class
                classes: {
                    weui_tabnav_item: "",
                    weui_tabbd_item: "",
                }
            },
            Ctrls: {
                tplname: "tabnav",
                innermode: false
            }
        }
    };
    //tab head点击处理句柄
    WHT.TabNav.prototype.headClickhandler = function(event) {
        var $target = $(event.target).hasClass("weui_tabbd_item") ? $(event.target) : $(event.target).closest(".weui_tabnav_item");
        //var $target = $(event.target);
        var this_instance = this;
        this_instance.activeindex = $target.index();
        this_instance.switchToIndex(this_instance.activeindex);
        var alreadyloaded = false;
        if (this_instance.$ele.find('.weui_tabbd_item').eq(this_instance.activeindex).html().length) {
            //tab body里是否已经存在内容
            alreadyloaded = true;
        }
        console.log("alreadyloaded's value is as following:");
        console.log(alreadyloaded);
        //触发indexchose事件,传出当前索引
        this.$ele.trigger($.Event('indexchose:WHT:tabnav'), [this_instance.activeindex, alreadyloaded]);
    };
    //切换到索引
    WHT.TabNav.prototype.switchToIndex = function(index) {
        var this_instance = this;
        //禁止外部调用
        if (arguments[0].externalcall) {
            return;
        }
        //设置tab head的初始激活状态
        this_instance.$ele.find('.weui_tabnav_item').eq(index).addClass('weui_tab_item_on').siblings().removeClass('weui_tab_item_on');
        //设置tab body的初始激活状态
        this_instance.$ele.find('.weui_tabbd_item').eq(index).addClass('weui_tab_item_on').siblings().removeClass('weui_tab_item_on');
    };
    //获取提示框标题
    WHT.TabNav.prototype.getTabNavTitle = function() {
        //return this.options.text_title;
    };
    //填充内容到tab body
    WHT.TabNav.prototype.fillContent = function(contentindex, content) {
        var this_instance = this;
        //如果是外部调用
        if (arguments[0].externalcall) {
            //重置参数列表
            var params = {};
            params.contentindex = arguments[0].params[0];
            params.content = arguments[0].params[1];
            this_instance.$ele.find(".weui_tabbd_item").eq(params.contentindex).html(params.content);
        } else {}
    };
    //搭建html结构 默认是隐藏态->display:none
    WHT.TabNav.prototype.build = function() {
        var def = $.Deferred();
        var this_instance = this;
        hbsTO.render('tabnav', function(t) {
            var htmlcreated = t(this_instance.options.tpldatas);
            this_instance.$ele.html(htmlcreated);
            //默认采用外部模式
            if (!this_instance.options.tpldatas.Ctrls.innermode) {
                //上wrapper id
                this_instance.$ele.attr('id', this_instance.options.tpldatas.hooks.wrapperid);
                //上wrapper class
                this_instance.$ele.addClass("weui_tab").addClass(this_instance.options.tpldatas.hooks.wrapperclass);
                this_instance.$ele.attr('data-plugintype', this_instance.options.plugintype);
            }
            def.resolve();
        });
        return def.promise();
    };
    //初始化提示框
    WHT.TabNav.prototype.init = function() {
        var this_instance = this;
        //搭建结构
        $.when(this_instance.build())
            .done(function() {
                //切换到初始索引
                if (this_instance.activeindex >= 0) {
                    this_instance.switchToIndex(this_instance.activeindex);
                }
                //发送初始化完毕事件
                this_instance.$ele.trigger($.Event('initialized:WHT:tabnav'));
            })
            .fail(function() {
                console.log("网络异常");
            });
    };
    /**
     * TabNavFactory Class
     * 提示框工厂类定义
     */
    var TabNavFactory = function() {};
    //default class
    TabNavFactory.prototype.dutyclass = WHT.TabNav;
    //method: create tabnav
    TabNavFactory.prototype.createTabNav = function(ele, options) {
        switch (options.plugintype) {
            case "SomeSubClassName":
                this.dutyclass = WHT.SomeSubClass;
                break;
            default:
                this.dutyclass = WHT.TabNav;
        }
        return new this.dutyclass(ele, options);
    };
    /**
     * TabNav Plugin
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
            var data_plugin = $this.data('TH.tabnav');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    var instanceof_factory = new TabNavFactory();
                    data_plugin = instanceof_factory.createTabNav(this, option);
                    $this.data('TH.tabnav', data_plugin);
                    //data_plugin.init();
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option]({
                        externalcall: true,
                        params: Array.prototype.slice.call(initial_arguments, 1)
                    });
                    //data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    $.info('Method ' + option + ' does not exist on Zepto.tabnav');
                }
            }
        });
    }
    //如果之前存在其他的tabnav插件，暂存
    var old = $.fn.tabNav;
    //挂载到$原型链
    $.fn.tabNav = Plugin;
    //构造器指向类TabNav
    $.fn.tabNav.Constructor = TabNavFactory;
    //TabNav noConflict
    $.fn.tabNav.noConflict = function() {
        $.fn.tabNav = old;
        return this;
    };
    //暂不支持data-api
})(Zepto);

/*!
 * 滑入式操作表
 * @module ActionSheet
 */
;
(function($) {
    /**
     * ActionSheet Class
     * ActionSheet基类定义
     */
    WHT.ActionSheet = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, WHT.ActionSheet.DEFAULTS, options);
        //属性
        this.isShown = false;
        //变量
        //this.activeindex = this.options.initialstate.activeindex; //激活页签索引值
        //内容是否是附加式
        //this.ctnadditional = this.options.tpldatas.ctns.bodys ? false : true;
        //初始化
        this.init();
        //event声明
        //actionsheet点击事件
        this.$ele.on('click.WHT.actionsheet.mask', '[jhk="mask"]', $.proxy(this.hide, this));
        this.$ele.on('click.WHT.actionsheet.cancel', '[jhk="cancel"]', $.proxy(this.hide, this));
        //this.$ele.find('.weui_actionsheet_item').on('click.WHT.actionsheet.head', $.proxy(this.headClickhandler, this));
    };
    //插件版本
    WHT.ActionSheet.VERSION = '1.0.0';
    //默认配置(all)
    WHT.ActionSheet.DEFAULTS = {
        plugintype: "actionsheet",
        tpldatas: {
            ctns: {
                //title: ""
                //body: ""
            },
            hooks: {
                wrapperid: "", //wrapper id
                wrapperclass: "", //wrapper class
                classes: {
                    weui_actionsheet_ctn: ""
                }
            },
            Ctrls: {
                tplname: "actionsheet",
                innermode: false,
                has_action_cancel: true
            }
        }
    };
    //获得实例显示状态
    WHT.ActionSheet.prototype.getShownState = function() {
        var this_instance = this;
        if (arguments[0].externalcall) {
            //重置参数列表
            var params = {};
            params.callback = arguments[0].params[0];
            if(params.callback){
                params.callback(this_instance.isShown);
            }
        }
    };
    //显示
    WHT.ActionSheet.prototype.show = function() {
        var this_instance = this;
        if (this_instance.isShown) {
            return;
        }
        var $actionsheet = this_instance.$ele.find('[jhk="actionsheet"]');
        var $mask = this_instance.$ele.find('[jhk="mask"]');
        $actionsheet.addClass('weui_actionsheet_toggle');
        $mask.show().focus().addClass('weui_fade_toggle');
        this_instance.isShown = true;
        this_instance.$ele.trigger($.Event('opened:WHT:actionsheet'));
    };
    //隐藏
    WHT.ActionSheet.prototype.hide = function() {
        var this_instance = this;
        if (!this_instance.isShown) {
            return;
        }
        var $actionsheet = this_instance.$ele.find('[jhk="actionsheet"]');
        var $mask = this_instance.$ele.find('[jhk="mask"]');
        $actionsheet.removeClass('weui_actionsheet_toggle');
        $mask.removeClass('weui_fade_toggle');
        $mask.on('transitionend webkitTransitionEnd', function() {
            $mask.hide();
            $mask.off("transitionend webkitTransitionEnd");
        });
        this_instance.isShown = false;
        this_instance.$ele.trigger($.Event('closed:WHT:actionsheet'));
    };
    WHT.ActionSheet.prototype.fillContent = function(content) {
        var this_instance = this;
        //如果是外部调用
        if (arguments[0].externalcall) {
            //重置参数列表
            var params = {};
            params.content = arguments[0].params[0];
            this_instance.$ele.find('.weui_actionsheet_ctn').html(params.content);
        } else {}
    };
    //搭建html结构 默认是隐藏态->display:none
    WHT.ActionSheet.prototype.build = function() {
        var def = $.Deferred();
        var this_instance = this;
        hbsTO.render('actionsheet', function(t) {
            var htmlcreated = t(this_instance.options.tpldatas);
            this_instance.$ele.html(htmlcreated);
            //默认采用外部模式
            if (!this_instance.options.tpldatas.Ctrls.innermode) {
                //上wrapper id
                this_instance.$ele.attr('id', this_instance.options.tpldatas.hooks.wrapperid);
                //上wrapper class
                this_instance.$ele.addClass("weui_actionsheet_wrap").addClass(this_instance.options.tpldatas.hooks.wrapperclass);
                this_instance.$ele.attr('data-plugintype', this_instance.options.plugintype);
            }
            def.resolve();
        });
        return def.promise();
    };
    //初始化提示框
    WHT.ActionSheet.prototype.init = function() {
        var this_instance = this;
        //搭建结构
        $.when(this_instance.build())
            .done(function() {
                //切换到初始索引
                //this_instance.switchToIndex(this_instance.activeindex);
                //发送初始化完毕事件
                this_instance.$ele.trigger($.Event('initialized:WHT:actionsheet'));
                console.log("init over");
            })
            .fail(function() {
                console.log("网络异常");
            });
    };
    /**
     * ActionSheetFactory Class
     * 提示框工厂类定义
     */
    var ActionSheetFactory = function() {};
    //default class
    ActionSheetFactory.prototype.dutyclass = WHT.ActionSheet;
    //method: create actionsheet
    ActionSheetFactory.prototype.createActionSheet = function(ele, options) {
        switch (options.plugintype) {
            case "SomeSubClassName":
                this.dutyclass = WHT.SomeSubClass;
                break;
            default:
                this.dutyclass = WHT.ActionSheet;
        }
        return new this.dutyclass(ele, options);
    };
    /**
     * ActionSheet Plugin
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
            var data_plugin = $this.data('TH.actionsheet');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    var instanceof_factory = new ActionSheetFactory();
                    data_plugin = instanceof_factory.createActionSheet(this, option);
                    $this.data('TH.actionsheet', data_plugin);
                    //data_plugin.init();
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option]({
                        externalcall: true,
                        params: Array.prototype.slice.call(initial_arguments, 1)
                    });
                    //data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    $.info('Method ' + option + ' does not exist on Zepto.actionsheet');
                }
            }
        });
    }
    //如果之前存在其他的actionsheet插件，暂存
    var old = $.fn.actionSheet;
    //挂载到$原型链
    $.fn.actionSheet = Plugin;
    //构造器指向类ActionSheet
    $.fn.actionSheet.Constructor = ActionSheetFactory;
    //ActionSheet noConflict
    $.fn.actionSheet.noConflict = function() {
        $.fn.actionSheet = old;
        return this;
    };
    //暂不支持data-api
})(Zepto);
