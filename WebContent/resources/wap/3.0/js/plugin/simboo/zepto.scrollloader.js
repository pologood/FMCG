/*!
 * 页面滚动加载器
 * @module ScrollLoader
 */
;

(function($) {
    /**
     * ScrollLoader Class
     * ScrollLoader基类定义
     */
    ScrollLoader = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, ScrollLoader.DEFAULTS, options);
        //示例属性
        this.isShown = false;
        this.init();
    };
    //插件版本
    ScrollLoader.VERSION = '1.0.0';
    //默认配置(all)
    ScrollLoader.DEFAULTS = {
        pagenow: 0,
        //url
        pagesize: 15,
        type: 'all',
        ordertype: 'weight'
    };
    //隐藏滚动加载器
    ScrollLoader.prototype.hide = function() {
        var this_instance = this;
    };
    //显示滚动加载器
    ScrollLoader.prototype.show = function() {
        //var args = arguments[0];
        var this_instance = this;

    };
    ScrollLoader.prototype.getPageNow = function() {
        var args = arguments[0];
        var this_instance = this;
        $.type(args[0]) == "function" && args[0](this_instance.options.pagenow);
    };
    ScrollLoader.prototype.pageAdd = function() {
        //var args = arguments[0];
        var this_instance = this;
        return ++this_instance.options.pagenow;
    };
    ScrollLoader.prototype.pageMinus = function() {
        //var args = arguments[0];
        var this_instance = this;
        if (this_instance.options.pagenow > 1) {
            return --this_instance.options.pagenow;
        } else {
            return 0;
        }
        //return this_instance.options.pagenow;
    };
    ScrollLoader.prototype.getPageData = function(pagenum) {

        //var args = arguments[0];
        var this_instance = this;
        return $.ajax({
            url: this_instance.options.url,
            data: {
                pageSize: this_instance.options.pagesize,
                pageNumber: pagenum,
                type: this_instance.options.type,
                orderType: this_instance.options.ordertype
            },
            timeout: 100000
        });
    };
    //使用获得数据填充页面(容器)
    ScrollLoader.prototype.fillPageContent = function(pagenum) {
        var this_instance = this;
        if (pagenum == 0) {
            //提示已经是第一页
            $(".ONCE.SOGA").tip({
                tiptype: "toast",
                initialstate: {
                    isShown: true
                },
                tpldatas: {
                    ctns: {
                        txt: "已是第一页"
                    },
                    hooks: {
                        wrapperid: "ONCE"
                    },
                    Ctrls: {
                        tplname: "toast"
                    }
                }
            });
            return null;
        }
        this_instance.getPageData(pagenum)
            .done(function(data) {
                console.log(data);
                var compiler = Handlebars.compile($("#tpl_itemlist").html());
                $(".itemlist_rap").html(compiler({ data: data }));
                //this_instance.options.pagenow = 1;
                //var pagenow=this_instance.getPageNow();
                //console.log("pagenow's value is as following:");
                //console.log(pagenow);
                this_instance.$ele.trigger($.Event('pageContentLoaded:ScrollLoader'), [pagenum]);
            });
    };
    //初始化滚动加载器
    ScrollLoader.prototype.init = function() {
        var this_instance = this;
        //搭建结构
        //this_instance.build();
        console.log("method init run");
        //首次进入先拿传入的列表项
        this_instance.fillPageContent(this_instance.options.pagenow);
        $(window).scroll(function(event) {
            //console.log($(window).height());
            //console.log($(window).scrollTop());
            //console.log($(document).height());
            //滚动到最底部时
            if ($(window).height() + $(window).scrollTop() == $(document).height()) {
                console.log("到达最底部");
                console.log("开始加载下一页");
                //下一页加载请求
                //得到下一页数据
                //刷出hbs模板
                //pagenow=1
                //this.options
                this_instance.fillPageContent(this_instance.pageAdd());
                //显示一个toast
            }
            //滚动到最顶部时
            if ($(window).scrollTop() == 0) {
                console.log("到达最顶部");
                console.log("开始加载上一页");
                //上一页加载请求
                //得到上一页数据
                //刷出hbs模板
                //pagenow=-1(示例使用)
                //this.options
                this_instance.fillPageContent(this_instance.pageMinus());
            }
        });
    };
    //搭建html结构
    ScrollLoader.prototype.build = function() {
        var this_instance = this;
        //build html
    };

    /**
     * ScrollLoader Plugin
     * 滚动加载器插件定义
     */
    function Plugin(option) {
        var initial_arguments = arguments;
        // each function
        return this.each(function() {
            var $this = $(this);
            //试获取对象实例
            var data_plugin = $this.data('scrollloader');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    data_plugin = new ScrollLoader(this, option);
                    $this.data('scrollloader', data_plugin);
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    $.info('Method ' + option + ' does not exist on Zepto.scrollLoader');
                }
            }
        });
    }
    //如果之前存在其他的scrollLoader插件，暂存
    var old = $.fn.scrollLoader;
    //挂载到$原型链
    $.fn.scrollLoader = Plugin;
    //构造器指向类ScrollLoader
    $.fn.scrollLoader.Constructor = ScrollLoader;

    //ScrollLoader noConflict
    $.fn.scrollLoader.noConflict = function() {
        $.fn.scrollLoader = old;
        return this;
    };
    //暂不支持data-api
})(Zepto);
