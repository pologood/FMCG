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
