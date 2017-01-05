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
