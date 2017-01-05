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
