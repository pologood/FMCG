/**
 * 级联下拉菜单for mobile
 * by xiaodai
 * date 2016-3
 */
(function($) {
    //一级菜单点击事件处理handler
    var menuL1ItemClickhandler = function(config, afterMenuL2buildover) {
        var $thisli = this;
        ajaxGet({
            url: config.urls.forL2,
            data: {
                id: $thisli.data("id")
            },
            success: function(data) {
                var compiler = Handlebars.compile($(config.tpls.forL2).html());
                var finalhtml = compiler($.parseJSON(data.content));

                var $dropwrap_menuL2_case = $("<ul></ul>")
                $dropwrap_menuL2_case.html(finalhtml);
                $dropwrap_menuL2_case.addClass('menu-R-list');

                $thisli.closest(".menu-L").siblings('.menu-R').html($dropwrap_menuL2_case.html());
                console.info("二级菜单填充完毕");
                afterMenuL2buildover($dropwrap_menuL2_case);
            }
        });
    };
    // 生成menuL1样本
    var buildCaseL1 = function(config, caller_id, afterbuildover) {
        ajaxGet({
            url: config.urls.forL1,
            data: {
                type: config.type
            },
            success: function(data) {
                /* 使用数据生成样本的过程*/
                var compiler = Handlebars.compile($(config.tpls.forL1).html());
                var finalhtml = compiler(data);
                //get dropwrap
                var $dropwrap_case = $("<div></div>");
                //填充
                $dropwrap_case.html(finalhtml);
                //打标记
                //生成的div 具有特殊的id: config.wrapprefixes.forL1 + caller_id
                $dropwrap_case.attr("id", config.wrapprefixes.forL1 + caller_id).addClass(config.classname.forL1).appendTo('body');
                console.info("一级菜单填充完毕");
                //定位
                $dropwrap_case.css('top', $(".fixedtop_tab").height());
                //显示
                //$dropwrap_case.show();
                //生成完成之后
                afterbuildover($dropwrap_case);
                //返回样本
                //return $dropwrap_case;
                //按钮 激活态
                //$this.addClass('active');
                //遮罩层显示
                //some code
                //一级菜单事件绑定
                //some code
            }
        });
    };
    var activeAndShowMask = function(binder_slter) {
        var mask = this;
        mask.active(binder_slter);
        mask.show();
    };
    // 通过字面量创造一个对象，存储我们需要的共有方法
    var methods = {
        // 在字面量对象中定义每个单独的方法
        init: function(opts) {
            // 为了更好的灵活性，对来自主函数，并进入每个方法中的选择器其中的每个单独的元素都执行代码
            return this.each(function() {
                // 为每个独立的元素创建一个Zepto对象
                var $this = $(this);
                //获得jquery对象上的配置数据
                var config = $this.data("dropCascadingMenu");
                //如果该对象上没有写入插件配置
                if ($.type(config) == 'undefined') {
                    //默认配置
                    var defaults = {
                        type: "", //category:分类; order:排序
                        mask_claname: null, //默认不进行遮罩处理
                        classname: {
                            forL1: "dropcasmenu",
                            forL2: "",
                            caller_active: "active", //下拉调用者默认激活态类名
                            menuitem_active: "active" //(一级)菜单项默认激活态类名
                        },
                        urls: {
                            forL1: "url_for_get_menu_L1",
                            forL2: "url_for_get_menu_L2"
                        },
                        tpls: {
                            forL1: "#tpl_dropcasmenu-main",
                            forL2: "#tpl_dropcasmenu-R"
                        },
                        wrapprefixes: {
                            forL1: "dropdownfor_",
                            forL2: ""
                        }
                    };
                    //配置实例化
                    config = $.extend(true, defaults, opts);
                    //保存实例配置
                    $this.data('dropCascadingMenu', config);
                } else { //如果该jq对象上已经存在插件配置
                    //配置实例化
                    config = $.extend(true, defaults, opts);
                }
                //dataok-menuL1 数据是否已经获得，标记样本是否已经形成
                $this.data("dataok-menuL1", "no");
                //生成遮罩
                if (config.mask_claname) {
                    this.mask_halfblack = MaskMaker(config.mask_claname, "#" + config.wrapprefixes.forL1 + $this.attr("id"), {
                        afterhide: function() {
                            $this.trigger('global:iam_onlyone_end');
                        }
                    });
                }
                //全城按钮点击事件
                $this.on('click', function(event) {
                    //如果L1菜单还没有加载过
                    if ($this.data("dataok-menuL1") == "no") {
                        //caller切换到激活态
                        $this.addClass(config.classname.caller_active);
                        $this.trigger('global:iam_onlyone', ["#" + $this.attr('id'), "dropCascadingMenu", "hide", config.classname.caller_active]);
                        //首次获得数据生成样本，首次生成之后其他时刻再次点击只是控制显示和隐藏
                        buildCaseL1(config, $this.attr("id"), function($dropwrap_case) {
                            //menuL1已经生成了 设置状态标识为yes
                            $this.data("dataok-menuL1", "yes");
                            //生成之后控制显示
                            $dropwrap_case.show();
                            //激活并显示遮罩
                            if ($this.get(0).mask_halfblack) {
                                activeAndShowMask.call($this.get(0).mask_halfblack, "#" + config.wrapprefixes.forL1 + $this.attr("id"));
                            }
                            //一级菜单项事件绑定
                            $dropwrap_case.on('click', '.menu-L-list li', function(event) {
                                $(this).siblings().removeClass(config.classname.menuitem_active);
                                $(this).addClass(config.classname.menuitem_active);
                                event.preventDefault();
                                //传入一级菜单$自身
                                menuL1ItemClickhandler.call($(this), config, function($dropwrap_menuL2_case) {});
                            });
                        });
                    } else {
                        //dropwrap_case已经存在了
                        var $dropwrap_case = $("#" + config.wrapprefixes.forL1 + $this.attr("id"));
                        if ($dropwrap_case.css("display") == "none") {
                            /**
                             * @params 1 元素slter
                             * @params 2 插件名 
                             * @params 3 隐藏(独占窗口)方法
                             */
                            //独占请求
                            $this.trigger('global:iam_onlyone', ["#" + $this.attr('id'), "dropCascadingMenu", "hide", config.classname.caller_active]);
                            //caller切换到激活态
                            $this.addClass(config.classname.caller_active);
                            $dropwrap_case.show();
                            //激活并显示遮罩
                            if ($this.get(0).mask_halfblack) {
                                activeAndShowMask.call($this.get(0).mask_halfblack, "#" + config.wrapprefixes.forL1 + $this.attr("id"));
                            }
                        } else {
                            $this.trigger('global:iam_onlyone_end');
                            //caller切换到常态
                            //$this.removeClass(config.classname.caller_active);
                            $dropwrap_case.hide();
                            //遮罩隐藏
                            if ($this.get(0).mask_halfblack) {
                                $this.get(0).mask_halfblack.hide();
                            }
                        }
                    }
                });
            });
        },
        hide: function() {
            console.info("you call dropdownmenu fun hide!!!!");
            var $this = this;
            var config = $this.data("dropCascadingMenu");
            if (!($.type(config) == "undefined")) {
                var $itsdropwrap_case = $("#" + config.wrapprefixes.forL1 + $this.attr("id"));
                //隐藏与其对应的下拉框
                $itsdropwrap_case.hide();
            }
            //遮罩隐藏
            if ($this.get(0).mask_halfblack) {
                $this.get(0).mask_halfblack.hide();
            }
        },
        destroy: function() {
            // 对选择器每个元素都执行方法
            return this.each(function() {
                // 执行代码
            });
        }
    };
    $.fn.dropCascadingMenu = function() {
        //get the first argument
        var method = arguments[0];
        if (methods[method]) {
            // 如果存在该方法就调用该方法
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            // 如果传进来的参数是{}, 就认为是初始化操作.
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on zepto.dropCascadingMenu');
            return this;
        }
    };
})(Zepto);
