var _g = {};
var JSHelper = {};
/**
 * Created by jf on 2015/9/11.
 */
var pageManager = {
    $container: $('.container'),
    _pageStack: [],
    _configs: [],
    _defaultPage: null,
    _isGo: false,
    default: function (defaultPage) {
        this._defaultPage = defaultPage;
        return this;
    },
    init: function () {
        var self = this;

        $(window).on('hashchange', function (e) {

            var _isBack = !self._isGo;
            self._isGo = false;
            if (!_isBack) {
                return;
            }

            var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
            var found = null;
            for (var i = 0, len = self._pageStack.length; i < len; i++) {
                var stack = self._pageStack[i];
                if (stack.config.url === url) {
                    found = stack;
                    break;
                }
            }
            if (found) {
                self.back();
            }
            else {
                goDefault();
            }
        });

        function goDefault() {
            var url = location.hash.indexOf('#') === 0 ? location.hash : '#';
            var page = self._find('name', self._defaultPage);
            self.go(page.name);
        }

        goDefault();

        return this;
    },
    push: function (config) {
        this._configs.push(config);
        return this;
    },
    go: function (to) {
        var config = this._find('name', to);
        if (!config) {
            return;
        }

        var html = $(config.template).html();
        var $html = $(html).addClass('slideIn').addClass(config.name);
        this.$container.append($html);
        this._pageStack.push({
            config: config,
            dom: $html
        });

        this._isGo = true;
        location.hash = config.url;

        if (!config.isBind) {
            this._bind(config);
        }

        return this;
    },
    back: function () {
        var stack = this._pageStack.pop();
        if (!stack) {
            return;
        }

        stack.dom.addClass('slideOut').on('animationend', function () {
            stack.dom.remove();
        }).on('webkitAnimationEnd', function () {
            stack.dom.remove();
        });

        return this;
    },
    _find: function (key, value) {
        var page = null;
        for (var i = 0, len = this._configs.length; i < len; i++) {
            if (this._configs[i][key] === value) {
                page = this._configs[i];
                break;
            }
        }
        return page;
    },
    _bind: function (page) {
        var events = page.events || {};
        for (var t in events) {
            for (var type in events[t]) {
                this.$container.on(type, t, events[t][type]);
            }
        }
        page.isBind = true;
    }
};

/* ==========================================================================
 选择对话框
 title:标题
 content:提示说明
 fn:点确认时的回调方法
 ============================================================================ */
function showDialog1(title, content, fn) {
    //pageManager.go("component");
    var $dialog = $('#dialog1');
    var $title = $('#dialog1 .weui_dialog_title');
    var $content = $('#dialog1 .weui_dialog_bd');
    $title.html(title);
    $content.html(content);
    $dialog.show();
    $dialog.find('#dialog1 #idNo').unbind('click');
    $dialog.find('#dialog1 #idOk').unbind('click');
    $dialog.find('#dialog1 #idNo').bind('click', function () {
        $dialog.hide();
    });
    $dialog.find('#dialog1 #idOk').bind('click', function () {
        $dialog.hide();
        if(fn){
            fn();    
        }
    });
}

/* ==========================================================================
 提示对话框
 title:标题
 content:提示说明
 ============================================================================ */
function showDialog2(title, content, fn) {
    //pageManager.go("component");
    var $dialog = $('#dialog2');
    var $title = $('#dialog2 .weui_dialog_title');
    var $content = $('#dialog2 .weui_dialog_bd');
    $title.html(title);
    $content.html(content);
    $dialog.show();
    $dialog.find('.weui_btn_dialog').unbind('click');
    $dialog.find('.weui_btn_dialog').bind('click', function () {
        $dialog.hide();
        if (typeof fn == "function") {
            fn();
        }
    });
}

/* ==========================================================================
 属性弹窗组件
 ============================================================================ */
function showDetailDialog($dialog) {
    //pageManager.go("component");
    //var $dialog = $('#store_detail_dialog');
    var $display = $dialog.css("display");
    if ($display == 'none') {
        $dialog.show();
    } else if ($display == 'block') {
        $dialog.hide();
    }
}
/* ==========================================================================
 下方弹出菜单
 maps:选择项 [{key:1,value:'选项1'}，{key:2,value:'选项2'}，{key:3,value:'选项3'}]
 content:提示说明
 ============================================================================ */

function showActionSheet(config) {
    //pageManager.go("component");
    var mask = $('#mask');
    var weuiActionsheet = $('#weui_actionsheet');
    weuiActionsheet.addClass('weui_actionsheet_toggle');
    if(config.removecancel){
        weuiActionsheet.find(".weui_actionsheet_action").hide();
    }
    if(config.showmask){
        mask.addClass('weui_fade_toggle').show();
        mask.on('click', function () {
            hideActionSheet(weuiActionsheet, mask);
        });
    }

    var html = "";
    for (var i = 0; i < config.data.length; i++) {
        chk = "";
        if (config.default == config.data[i].key) {
            chk = 'checked="checked"';
        }
        html += '<label class="weui_cell weui_check_label" for="' + config.data[i].key + '">';
        html += '<div class="weui_cell_hd"><i class="iconfont font-large-4" style="color:' + config.data[i].color + '">' + config.data[i].icon + '</i></div>';
        html += '<div class="weui_cell_bd weui_cell_primary">';
        html += '<p>' + config.data[i].value + '</p>';
        html += '</div>';
        html += '<div class="weui_cell_ft">';
        html += '<input type="radio" class="weui_check" id="' + config.data[i].key + '"  data-id="' + config.data[i].key + '" ' + chk + '">';
        html += '<span class="weui_icon_checked"></span>';
        html += '</div>';
        html += '</label>';

    }
    weuiActionsheet.find('.weui_cells_radio').html(html);
    weuiActionsheet.find('.weui_cells_title').html(config.title);
    //自动高度
    if(config.autoheight){
        var height_sheet=weuiActionsheet.height();
        var height_window=$(window).height();
        if(height_sheet<(height_window / 2)){
            var $blankdiv=$("<div></div>").css('height', (height_window / 2)-height_sheet+"px");
            weuiActionsheet.find(".weui_actionsheet_action").prepend($blankdiv);
        }
    }

    $('#weui_actionsheet .weui_check').unbind('click');
    $('#weui_actionsheet .weui_check').bind("click", function () {
            hideActionSheet(weuiActionsheet, mask);
            if(config.fn){
                config.fn($(this).attr("data-id"));    
            }
        }
    );

    $('#actionsheet_cancel').unbind('click');
    $('#actionsheet_cancel').click(function () {
        hideActionSheet(weuiActionsheet, mask);
    });
    weuiActionsheet.unbind('transitionend').unbind('webkitTransitionEnd');

    function hideActionSheet(weuiActionsheet, mask) {
        weuiActionsheet.removeClass('weui_actionsheet_toggle');
        mask.removeClass('weui_fade_toggle');
        weuiActionsheet.on('transitionend', function () {
            mask.hide();
        }).on('webkitTransitionEnd', function () {
            mask.hide();
        })
    }
}

function tips() {
    showToast2({content:'功能开发中,请耐心等待'});
}

function showActionSheet2(config) {
    //pageManager.go("component");
    var mask = $('#mask');
    var weuiActionsheet = $('#weui_actionsheet');
    weuiActionsheet.addClass('weui_actionsheet_toggle');
    if(config.removecancel){
        weuiActionsheet.find(".weui_actionsheet_action").hide();
    }
    if(config.showmask){
        mask.show().addClass('weui_fade_toggle').one('click', function () {
            hideActionSheet(weuiActionsheet, mask);
        });
    }
    //body html
    var html = "";
    var its_id = "";
    var its_name = "";
    var its_pos = "";
    var its_telnum = "";
    var its_salenum = 0;
    var its_fansnum = 0;

    for (var i = 0; i < config.data.length; i++) {
        its_id = config.data[i].id||'';
        its_name = config.data[i].name;
        its_pos = config.data[i].pos;
        its_telnum = config.data[i].telnum;
        its_salenum = config.data[i].salenum ? config.data[i].salenum : 0;
        its_fansnum = config.data[i].fansnum ? config.data[i].fansnum : 0;

        html += '<div class="weui_cell box-tellist TTC">';
        html += '<div class="weui_cell_bd bd weui_cell_primary">';
        html += '<span>'+its_name+'：'+its_pos+'</span>';
        html += '<span>销售出:'+its_salenum+'件&nbsp;&nbsp;粉丝数:'+its_fansnum+'</span>';
        html += '</div>';
        html += '<div class="weui_cell_ft ft">';
        html += '<a href="'+config.url.replace("TELEPHONE",its_id)+'" class="iconfont">&#xe64d;</a><a href="tel:'+its_telnum+'" class="iconfont">&#xe687;</a>';
        //html += '<a href="/wap/member/chat/index.jhtml?targetid='+its_id+'&tenantid=" class="iconfont">&#xe64d;</a><a href="tel:'+its_telnum+'" class="iconfont">&#xe687;</a>';
        html += '</div></div>';
    }
    //head html
    var head_html='';
    head_html+='<div class="weui_cell_bd weui_cell_primary">';
    head_html+='<span>'+config.title+'</span>';
    head_html+='</div>';
    weuiActionsheet.find('.weui_cells_radio').removeClass().addClass("weui_cells weui_cells_radio "+config.clastr.forbody).html(html);
    weuiActionsheet.find('.weui_cells_title').removeClass().addClass('weui_cell weui_cells_title am-text-center '+config.clastr.fortitle).html(head_html);
    weuiActionsheet.find('.weui_cells_title').parent().css('marginTop', '0');


    $('#actionsheet_cancel').unbind('click');
    $('#actionsheet_cancel').click(function () {
        hideActionSheet(weuiActionsheet, mask);
    });
    weuiActionsheet.unbind('transitionend').unbind('webkitTransitionEnd');

    function hideActionSheet(weuiActionsheet, mask) {
        weuiActionsheet.removeClass('weui_actionsheet_toggle');
        mask.removeClass('weui_fade_toggle');
        weuiActionsheet.on('transitionend', function () {
            mask.hide();
        }).on('webkitTransitionEnd', function () {
            mask.hide();
        })
    }
}

/**
 * 用户需要传入生成后的body html str
 * @param  {[type]} config [description]
 * @return {[type]}        [description]
 */
function showActionSheetFree(config,activeitem) {
    var $actionsheet = $('#weui_actionsheet');
    var $mask = $actionsheet.siblings('#mask');
    //conditional additions
    $actionsheet.addClass('weui_actionsheet_toggle').addClass(config.clastr.forwrap);
    if(config.removecancel){
        $actionsheet.find(".weui_actionsheet_action").hide();
    }
    if(config.showmask){
        $mask.show().addClass('weui_fade_toggle').one('click', function () {
            hideActionSheet($actionsheet, $mask);
        });
    }
    //build content
    $actionsheet.find('.weui_cells_title').removeClass().addClass('weui_cell weui_cells_title am-text-center '+config.clastr.fortitle).html(config.ctn.fortitle);
    $actionsheet.find('.weui_cells_radio').removeClass().addClass("weui_cells weui_cells_radio "+config.clastr.forbody).html(config.ctn.forbody);    
    $actionsheet.find('.weui_cells_radio').children().removeClass('active').each(function(index, ele) {
        if($(this).attr("storeid")==activeitem){
            $(this).addClass('active');
        }
    });
    //event clear
    $actionsheet.find('#actionsheet_cancel').off('click');
    $actionsheet.off('transitionend webkitTransitionEnd');
    //
    $actionsheet.find('#actionsheet_cancel').click(function () {
        hideActionSheet($actionsheet, $mask);
    });
    $actionsheet.on('click', '[storeid]', function(event) {
        event.preventDefault();
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
        hideActionSheet($actionsheet, $mask);
        if(config.afterSelect){
            config.afterSelect($(this).attr("storeid"));
        }
    });
    //hideActionSheet
    function hideActionSheet($actionsheet, $mask) {
        $actionsheet.removeClass('weui_actionsheet_toggle');
        $mask.removeClass('weui_fade_toggle');
        $actionsheet.on('transitionend webkitTransitionEnd', function () {
            $mask.hide();
        });
    }
}
/* ==========================================================================
 支付密码弹出框
 ============================================================================ */
function showPass(fn) {
    //pageManager.go("component");
    var $password = $('#password');
    if ($password.css('display') != 'none') {
        return;
    }
    $password.show();
    $password.find('#password #idNo').unbind('click');
    $password.find('#password #idOk').unbind('click');
    $password.find('#password #idNo').bind('click', function () {
        $password.hide();
    });
    $password.find('#password #idOk').bind('click', function () {
        $password.hide();
        fn($(".pay-input").val());
    });
}
/* ==========================================================================
 已成功提示 2秒关闭
 ============================================================================ */
function showToast(message) {
    //pageManager.go("component");
    var $toast = $('#toast');
    if ($toast.css('display') != 'none') {
        return;
    }

    if (message != null) {
        $toast.find(".weui_toast_content").html(message.content);
    } else {
        $toast.find(".weui_toast_content").html("已完成");
    }

    $toast.show();
    setTimeout(function () {
        $toast.hide();
    }, 2000);
}

/* ==========================================================================
 用来显示错误信息  2秒关闭
 ============================================================================ */
function showToast2(message) {
    //pageManager.go("component");
    var $toast2 = $('#toast2');
    if ($toast2.css('display') != 'none') {
        return;
    }

    if (message != null) {
        $toast2.find(".weui_toast_content").html(message.content);
    } else {
        $toast2.find(".weui_toast_content").html("已完成");
    }

    $toast2.show();
    setTimeout(function () {
        $toast2.hide();
    }, 2000);
}

/* ==========================================================================
 显示数据加载中 2秒关闭
 ============================================================================ */
function showLoadingToast() {
    //pageManager.go("component");
    var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') {
        return;
    }

    $loadingToast.show();
    setTimeout(function () {
        $loadingToast.hide();
    }, 2000);
}

/* ==========================================================================
 显示数据加载中 不关闭
 ============================================================================ */
function showWaitLoadingToast() {
    //pageManager.go("component");
    var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') {
        return;
    }

    $loadingToast.show();
    setTimeout(function () {
        $loadingToast.hide();
    }, 5000);
}

/* ==========================================================================
 关闭数据加载中
 ============================================================================ */
function closeWaitLoadingToast() {
    //pageManager.go("component");
    var $loadingToast = $('#loadingToast');
    $loadingToast.hide();
}

//var component = {
//    name: 'component',
//    url:  '#component',
//    template: '#tpl_component'
//};
var wraper = {
    name: 'wraper',
    url: '#wraper',
    template: '#tpl_wraper'
};

/* ==========================================================================
 封装  ajax数据请求
 { options:url,  必选
 options.data,  必选
 success:function(data){},  必选
 error；function(xhr,type){}  可选
 }
 ============================================================================ */

function ajaxGet(options) {
    showWaitLoadingToast();
    return $.ajax({
        type: 'GET',
        url: options.url,
        data: options.data,
        dataType: 'json',
        //timeout: 10000,
        context: $('body'),
        success: function (data) {
            closeWaitLoadingToast();
            options.success(data);
        },
        error: function (xhr, type) {
            closeWaitLoadingToast();
            showDialog2('出错了', '请检查网络是否正常连接。');
        }
    })
}
function ajaxPost(options) {
    showWaitLoadingToast();
    $.ajax({
        type: 'POST',
        url: options.url,
        data: options.data,
        dataType: 'json',
        //timeout: 10000,
        context: $('body'),
        success: function (data) {
            closeWaitLoadingToast();
            options.success(data);
        },
        error: function (xhr, type) {
            closeWaitLoadingToast();
            if (options.error != null) {
                options.error(xhr, type);
            } else {
                showDialog2('出错了', '请检查网络是否正常连接。');
            }
        }
    })
}

/* ==========================================================================
 表单验证封装
 ============================================================================ */
var validate = {
    require: function (elem, errmsg) {
        elem = $(elem);
        elem.on("change", function () {
            var value = this.value;
            var tipCon = $(this).parents(".weui_cell");
            var tipIcon = $(this).find(".weui_icon_warn");
            if (value == "") {
                tipCon.addClass("weui_cell_warn");
                tipIcon.show();
            } else {
                tipCon.removeClass("weui_cell_warn");
                tipIcon.hide();
            }
        });
    },
    phone: function (elem, errmsg) {
        elem = $(elem);
        var tipCon = $(this).parents(".weui_cell");
        var tipIcon = $(this).find(".weui_icon_warn");
        elem.on("change", function () {
            var value = $.trim(this.value);
            if (!/^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/.test(value)) {
                tipCon.addClass("weui_cell_warn");
                tipIcon.show();
            } else {
                tipCon.removeClass("weui_cell_warn");
                tipIcon.hide();
            }
        });
    },
    email: function (elem, errmsg) {
        elem = $(elem);
        var tipCon = $(this).parents(".weui_cell");
        var tipIcon = $(this).find(".weui_icon_warn");
        elem.on("change", function () {
            var value = $.trim(this.value);
            if (!/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(value)) {
                tipCon.addClass("weui_cell_warn");
                tipIcon.show();
            } else {
                tipCon.removeClass("weui_cell_warn");
                tipIcon.hide();
            }
        });
    },
    compare: function (elem1, elem2, errmsg) {
        elem1 = $(elem1);
        elem2 = $(elem2);
        var tipCon = $(this).parents(".weui_cell");
        var tipIcon = $(this).find(".weui_icon_warn");
        elem2.on("change", function () {
            var value1 = $.trim(elem1[0].value);
            var value2 = $.trim(this.value);
            if (value1 !== value2) {
                tipCon.addClass("weui_cell_warn");
                tipIcon.show();
            } else {
                tipCon.removeClass("weui_cell_warn");
                tipIcon.hide();
            }
        });
    },

    isVaild: function (form) {
        if (form.find(".weui_cell_warn") != null) {
            return false;
        } else {
            return true;
        }
    }
};

/* ==========================================================================
 表单提交 sumitForm($(".formname"));
 ============================================================================ */
function submitForm(form) {
    if (validate.isVaild(form)) {
        showDialog2('友情提醒', '请正确填写带红色标志的数据。');
    }
    showLoadingToast();
    form.submit();
}

/* ==========================================================================
 scroll 分页加载封装
 ============================================================================ */
function scroll(fn) {
    //$(document).ready(function () { //本人习惯这样写了
    $(window).scroll(function () {
        //$(window).scrollTop()这个方法是当前滚动条滚动的距离
        //$(window).height()获取当前窗体的高度
        //$(document).height()获取当前文档的高度
        var bot = 50; //bot是底部距离的高度
        if ((bot + $(window).scrollTop()) >= ($(document).height() - $(window).height())) {
            //当底部基本距离+滚动的高度〉=文档的高度-窗体的高度时；
            //我们需要去异步加载数据了
            //$('#pullUpLabel').html("加载更多...");
            //if (fn()) {
            //$('#pullUpLabel').hide();
            //} else {
            //   $('#pullUpLabel').html("亲,到底了");
            //setTimeout(function () {
            //    $('#pullUpLabel').hide();
            //}, 2000);
            //}
            fn();
        }
    });
    //});
}

/* ==========================================================================
 元素到达指定(相应)位置(门限)切换状态，自定义门限方法
 ============================================================================ */
var switchStateWhenScroll = (function () {
    //var and default
    var count = 0,
        timer = null;
    var direction = "down";
    var threshold = 0;
    var oldTop = newTop = $(window).scrollTop();
    //功能主函数
    function mainfun(config) {
        /* 配置说明*/
        ///                    var config = {
        ///                        threshold : custom number//门限值
        ///                        funGo : custom func//向下超过门限 动作
        ///                        funBack : custom func//向上进入门限 动作
        ///                    }
        //reset config
        threshold = config.threshold || threshold;
        //
        if (timer) clearTimeout(timer);
        newTop = $(window).scrollTop();
        if (newTop === oldTop) {
            clearTimeout(timer);
            //向下超过门限
            if (newTop > threshold && direction == "down") {
                config.funGo();
            }
            //向上进入门限
            if (newTop < threshold && direction == "up") {
                config.funBack();
            }
        } else {
            if (newTop < oldTop) {
                direction = "up";
            } else {
                direction = "down";
            }
            oldTop = newTop;
            timer = setTimeout(mainfun(config), 100);
        }
    }

    return mainfun;
})();
/**
 * 返回特定色值的十六进制,eg:rgb(140,140,140)
 * @param  {[type]} begincolorval_10 起始色值
 * @param  {[type]} bgrate           所提供的比值
 * @param  {[type]} stopcolorval_10  结束色值
 * @return {[type]}                  [description]
 */
function getSpecialColorValue(begincolorval_10,bgrate,stopcolorval_10){
    var colorstr_arr=[];
    for (var i = 2; i >= 0; i--) {
        colorstr_arr.push(parseInt(begincolorval_10 - (begincolorval_10 - stopcolorval_10)*bgrate).toString(16));
    }
    return "#"+colorstr_arr.join("");
}
/* ==========================================================================
 元素到达指定(相应)位置(门限)切换状态，版本3.0
 应用于：主页搜索框
 ============================================================================ */
 var ScrollChange2 = (function($) {
    //设置初始值
    var oldTop = newTop = $(window).scrollTop();
    //默认配置
    var defaults = {
        //custom_overThreshold: function() {},
        //custom_justOverThreshold: function() {},
        //custom_belowThreshold: function() {},
        //custom_justBelowThreshold: function() {},
        count: 0,
        threshold: 50,
        timerframe: null
    };
    /**
     * 声明
     * @param {[type]} opts [description]
     */
    function ScrollChange2(opts) {
        if (!opts.wrap_slter) {
            return;
        }
        this.config = $.extend(true, defaults, opts);
    }
    /**
     * 高于水位线执行的动作
     * @return {[type]} [description]
     */
    ScrollChange2.prototype.overThreshold = function(heightrate) {
        //默认动作
        //console.info("在水位线之上up");
        //自定义动作
        if (this.config.custom_overThreshold) {
            this.config.custom_overThreshold(heightrate);
        }
    };
    /**
     * 刚刚高于水位线执行的动作
     * @return {[type]} [description]
     */
    ScrollChange2.prototype.justOverThreshold = function() {
        //默认动作
        //console.info("刚刚高于水位线");
        //自定义动作
        if (this.config.custom_justOverThreshold) {
            this.config.custom_justOverThreshold();
        }

    };
    /**
     * 低于水位线执行的动作
     * @return {[type]} [description]
     */
    ScrollChange2.prototype.belowThreshold = function() {
        //默认动作
        //console.info("在水位线之下down");
        //自定义动作
        if (this.config.custom_belowThreshold) {
            this.config.custom_belowThreshold();
        }
    };
    /**
     * 刚刚低于水位线的执行动作
     * @return {[type]} [description]
     */
    ScrollChange2.prototype.justBelowThreshold = function() {
        //默认动作
        //console.info("刚刚低于水位线");
        //自定义动作
        if (this.config.custom_justBelowThreshold) {
            this.config.custom_justBelowThreshold();
        }
    };
    /**
     * 触摸递归
     * @return {[type]} [description]
     */
    ScrollChange2.prototype.movehandler = function() {
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
            //如果在水位线以上
            if (newTop <= this.config.threshold) {
                var heightrate = newTop / this.config.threshold;
                //getSpecialColorValue(255,bgrate,140)
                this.overThreshold(heightrate);
                //如果刚刚超过水位线
                if ($(this.config.wrap_slter).data("statelock") == "Y") {
                    //重置statelock
                    this.justOverThreshold();
                }
                $(this.config.wrap_slter).removeData('statelock');
            }
            //如果在水位线以下
            if (newTop > this.config.threshold) {
                this.belowThreshold();
                //如果刚刚低于水位线
                if (!$(this.config.wrap_slter).data("statelock")) {
                    this.justBelowThreshold();
                }
                $(this.config.wrap_slter).data("statelock", "Y");
            }
        } else {
            oldTop = newTop;
            this.config.timerframe = requestAnimationFrame(function() {
                this_sc.movehandler();
            });
        }
    };
    return ScrollChange2;
})(Zepto);

/* ==========================================================================
 商户展示列表项目缩略图标显示与隐藏handler,delivery/nearby.jhtml
 ============================================================================ */
function ThumbIconLeftHandler() {
    //reserve this
    var self = this;
    if ($(self).data('is_shown') == undefined) {
        $(self).data('is_shown', "true");
    }
    //整个toggle页面显示隐藏的状态
    var $father_p = $(this).closest('p');

    /**
     * 元素隐藏方法
     * @param  {[type]} $this    自身
     * @param  {[type]} isitself 是否是触摸事件源本身所在的P元素
     * @return {[type]}          [description]
     */
    function hideOne($this, isitself) {
        //如果是点击事件自身
        var slter = isitself ? "span, a" : "span";
        $this.css({
            display: "inline-block"
        });
        $this.find(slter).css({
            display: "none"
        });
    }

    function showOne($this, isitself) {
        //如果是点击事件自身
        var slter = isitself ? "span, a" : "span";
        $this.css({
            display: "block"
        });
        $this.find(slter).css({
            display: "inline"
        });
    }

    function showAll() {
        //处理自身
        //showOne($father_p);
        $next_p = $father_p.next('p');
        while ($next_p.length > 0) {
            showOne($next_p);
            //reset $next_p
            $next_p = $next_p.next('p');
        }
        //逐条操作完毕，resetis_shown
        $(self).data('is_shown', "true");
    }

    function hideAll() {
        //处理自身
        //hideOne($father_p);
        //处理后续节点
        $next_p = $father_p.next('p');
        while ($next_p.length > 0) {
            hideOne($next_p);
            //reset $next_p
            $next_p = $next_p.next('p');
        }
        //逐条操作完毕，resetis_shown
        $(self).data('is_shown', "false");
    }

    //run
    if ($(self).data('is_shown') == "true") {
        //如果当前是显示态
        hideAll();
        //处理自身的状态变化
        $(self).find('.iconfont').html('&#xe612;');
    } else {
        //如果当前是隐藏态
        showAll();
        //处理自身的状态变化
        $(self).find('.iconfont').html('&#xe659;');
    }
}
/**
 * 固定元素占位副本赋高
 * 调用时需注意调用位置(调用时该fixed元素是否存在)
 * @param  {[type]} empty_slter    [description]
 * @param  {[type]} fixedele_slter [description]
 * @return {[type]}                [description]
 */
function fixedEleCopyHandler(empty_slter, fixedele_slter) {
    //占位元素赋高
    $(empty_slter).height($(fixedele_slter).height());
}
/* ==========================================================================
 sticky元素生成器，以iscroll为基础
 ============================================================================ */
_g.categoryli_activeindex = 0;
var stickyOnIScroll = (function () {
    //固定元素的初始相对(scroll元素)位置Y
    var fixedele_positonY_to_scroll = 0;
    //fixedele_positonY_to_scroll 赋值锁
    var lockfor_fixedele_positonY_to_scroll = false;
    var isbefore = true;
    //停止时视口顶端位置(Y)
    var posY_stop = 0;
    //defaults
    var defaults = {};
    //config
    var config = {};
    //获得元素位置(Y)
    function setFixedElePositonYToScroll() {
        //set fixed ele posY
        fixedele_positonY_to_scroll = $(config.sticky_slter).offset().top - $(config.scroll_slter).offset().top;
    }

    //getter
    function getFixedElePositonYToScroll() {
        return fixedele_positonY_to_scroll;
    }

    //getter
    function getPosYStop() {
        return posY_stop;
    }

    //fixed 到头部
    function tie() {
        var $indexLi = null;
        //本尊隐藏
        $(config.sticky_slter).css({
            visibility: "hidden"
        });
        //副本显示
        $(config.copy_sticky_slter).addClass(config.fixed_class).css({
            visibility: "visible"
        });
        //设置副本菜单激活态
        $indexLi = $(config.copy_sticky_slter).find("li").eq(_g.categoryli_activeindex);
        $indexLi.siblings('li').removeClass("active");
        $indexLi.addClass("active");
        //
        isbefore = false;
    }

    //从顶部解绑
    function relax() {
        //本尊显示
        $(config.sticky_slter).css({
            visibility: "visible"
        });
        //副本隐藏
        $(config.copy_sticky_slter).removeClass(config.fixed_class).css({
            visibility: "hidden"
        });
        //设置本尊激活态
        $indexLi = $(config.sticky_slter).find("li").eq(_g.categoryli_activeindex);
        $indexLi.siblings('li').removeClass("active");
        $indexLi.addClass("active");
        //
        isbefore = true;
    }

    //绑到顶部and从顶部松绑
    function tieAndRelax(pospointer) {
        var $indexLi = null;
        //固定
        if (pospointer < (0 - fixedele_positonY_to_scroll) && isbefore) {
            tie();
        }
        //解除固定
        if (pospointer >= (0 - fixedele_positonY_to_scroll) && !isbefore) {
            relax();
        }
    }

    /**
     * main function
     * @param  {[type]} instance_iscroll scroll实例
     * @param  {[type]} opts             配置
     * @return {[type]}                  object
     */
    function main(instance_iscroll, opts) {
        //set config
        config = $.extend(true, defaults, opts);
        //bind scroll event
        instance_iscroll.on('scroll', function () {
            //not lock yet
            if (!lockfor_fixedele_positonY_to_scroll) {
                //set posY
                setFixedElePositonYToScroll();
                //lock now
                lockfor_fixedele_positonY_to_scroll = true;
            }
            //main handler
            tieAndRelax(this.y);
        });
        //bind scrollEnd event
        instance_iscroll.on('scrollEnd', function () {
            //main handler
            tieAndRelax(this.y);
            //set stop position
            posY_stop = this.y;
        });
        return {
            tie: tie,
            relax: relax,
            getPosYStop: getPosYStop,
            getFixedElePositonYToScroll: getFixedElePositonYToScroll
        }
    }

    return main;
})();

/**
 * 解决fixed元素touch 穿透的问题
 * @param  {[type]} ele_slter 元素选择符
 * @return {[type]}           [description]
 */
function preventTouchThroughOnFixedEle(ele_slter) {
    for (var i = arguments.length - 1; i >= 0; i--) {
        $(document).on('touchmove', arguments[i], function (e) {
            //stop default action
            e.preventDefault();
            //e.stoppropagation();
        });
    }
}

/**
 * 遮罩层生成器
 * @param  {Object} ) {                   
 * @return {[type]}   [description]
 */
var MaskMaker = (function () {
    //all masks
    var mask_collection = {};
    var _g = {
        active_binder_slter: ""//当前活动binder，不同实例间互斥
    };

    /**
     * constructor
     */
    function MaskMaker(callbacks) {
        this.mask_cla_name = "";
        //_g.active_binder_slter = "";
        this.coll_binder_slter = [];
        this.callbacks = callbacks;
    }

    //show
    MaskMaker.prototype.show = function () {
        //_g.active_binder_slter
        var this_mask = this;
        $("." + this.mask_cla_name).show();
        //bind click event
        $("." + this.mask_cla_name).on('click', function (event) {
            this_mask.hide(true);
            event.preventDefault();
        });
    };
    //单独隐藏遮罩层方法
    MaskMaker.prototype.hideMaskSelf = function() {
        $("." + this.mask_cla_name).hide();
    };
    //隐藏遮罩层组合方法
    MaskMaker.prototype.hide = function (ismaskclicked) {
        $("." + this.mask_cla_name).hide();
        $(_g.active_binder_slter).hide();
        //主要是用户点击遮罩，遮罩会关闭，但是可能还需要处理一些动作，所以留下这个接口
        if (ismaskclicked && this.callbacks.afterhide) {
            this.callbacks.afterhide();
        }
        _g.active_binder_slter = "";
        //unbind click event
        $("." + this.mask_cla_name).off("click");

    };
    //实例化之后，使用之前必须要激活
    MaskMaker.prototype.active = function (binder_slter) {
        //如果是有效slter
        if ($.inArray(binder_slter, this.coll_binder_slter) !== -1) {
            //首次激活
            if (_g.active_binder_slter == "") {
                //将binder_slter设定为激活态
                _g.active_binder_slter = binder_slter;
            } else {
                //非首次激活
                this.hide();
                //将binder_slter设定为激活态
                _g.active_binder_slter = binder_slter;
                this.show();
            }
        }

    };
    //静态方法
    main.hideAll=function(){
        $.each(mask_collection, function(index, ele) {
             ele.hideMaskSelf();
        });
    };
    //main
    function main(cla_name, binder_slter, callbacks) {
        if (!arguments.length) {
            return null;
        }
        var mask_obj = null;
        //结果集合
        var the_objs = $.map(mask_collection, function (ele, index) {
            if (index == cla_name) {
                return mask_collection[index];
            }
        });
        //如果已经存在
        if (the_objs.length) {
            mask_obj = the_objs[0];
        } else {
            mask_obj = new MaskMaker(callbacks);
            //push to array
            mask_collection[cla_name] = mask_obj;
        }
        //push binder to array
        if ($.inArray(binder_slter, mask_obj.coll_binder_slter) == -1) {
            mask_obj.coll_binder_slter.push(binder_slter);
        }
        mask_obj.mask_cla_name = cla_name;
        if ($("." + cla_name).length == 0) {
            $("<div>").addClass(cla_name).appendTo('body');
        }
        return mask_obj;
    }

    return main;
})();

/**
 * 下拉点击项选入，类似于select
 * by xiaodai
 * date: 2016-3-30
 */
(function ($, MaskMaker) {
    //生成所需的html结构
    var buildHtml = function (datas, config) {
        var $dropselect = $("<div class='" + config.claname.dropwrap + "'></div>");
        var htmlstr = "";
        for (var i = datas.length - 1; i >= 0; i--) {
            htmlstr = htmlstr + '<li><a href="javascript:;" id="' + datas[i].id + '"  data-id="'+datas[i].id+'"onclick="get_community_category_order(1,this)">' + datas[i].name + '</a></li>';
        }
        htmlstr = '<ul>' + htmlstr + '</ul>';
        $dropselect.append($(htmlstr));
        $dropselect.css('top', $(".fixedtop_tab").height()).appendTo('body');
        return $dropselect;
    };
    //激活并显示mask
    var activeAndShowMask = function (binder_slter) {
        var mask = this;
        mask.active(binder_slter);
        mask.show();
    };
    // 通过字面量创造一个对象，存储我们需要的公有方法
    var methods = {
        // 在字面量对象中定义每个单独的方法
        init: function (opts) {
            // 为了更好的灵活性，对来自主函数，并进入每个方法中的选择器其中的每个单独的元素都执行代码
            return this.each(function () {
                // 为每个独立的元素创建一个Zepto对象
                var $this = $(this);
                var this_instance = this;
                //获得zepto对象上的插件配置数据
                var config = $this.data("dropDownSelect");
                //如果该对象上没有写入插件配置
                if ($.type(config) == 'undefined') {
                    //默认配置
                    var defaults = {
                        paramdatas: {
                            type: "order"
                        },
                        url: "url_you_should_give",
                        mask_claname: "mask-halfblack",
                        claname: {
                            dropwrap: "soga-dropDownSelect",
                            caller_active: "active"
                        },
                        wrapprefixes: {
                            forwrap: "dropDownSelectfor_"
                        }
                    };
                    //配置合并
                    config = $.extend(true, defaults, opts);
                    //保存实例配置
                    $this.data('dropDownSelect', config);
                } else { //如果该jq对象上已经存在插件配置
                    //配置合并
                    config = $.extend(true, defaults, opts);
                }
                //then you can get config like this: $this.data("dropDownSelect");
                //ajax 获得数据
                if (!config.virtualdata) {
                    //如果没有虚拟数据
                    ajaxGet({
                        url: config.url,
                        data: config.paramdatas,
                        success: function (data) {
                            //生成
                            this_instance.$dropselect = buildHtml(data, config);
                            $this.get(0).$dropselect.attr('id', config.wrapprefixes.forwrap + $this.attr("id"));
                        }
                    });
                } else {
                    //如果传入了虚拟数据
                    //生成
                    this_instance.$dropselect = buildHtml(config.virtualdata, config);
                    $this.get(0).$dropselect.attr('id', config.wrapprefixes.forwrap + $this.attr("id"));
                }
                //生成遮罩
                if (config.mask_claname) {
                    this.mask_halfblack = MaskMaker(config.mask_claname, "#" + config.wrapprefixes.forwrap + $this.attr("id"), {
                        afterhide: function () {
                            $this.trigger('global:iam_onlyone_end');
                        }
                    });
                }
                //初始化的过程中就获得数据内容
                $this.on('click', function (event) {
                    event.preventDefault();
                    if ($this.get(0).$dropselect.css('display') == "none") {
                        $this.trigger('global:iam_onlyone', ['#' + $this.attr('id'), 'dropDownSelect', 'hide', config.claname.caller_active]);
                        $this.addClass(config.claname.caller_active);
                        $this.get(0).$dropselect.show();
                        //激活并显示遮罩
                        if ($this.get(0).mask_halfblack) {
                            activeAndShowMask.call($this.get(0).mask_halfblack, "#" + config.wrapprefixes.forwrap + $this.attr("id"));
                        }
                    } else {
                        $this.trigger('global:iam_onlyone_end');
                        $this.removeClass(config.claname.caller_active);
                        $this.get(0).$dropselect.hide();
                        //遮罩隐藏
                        if ($this.get(0).mask_halfblack) {
                            $this.get(0).mask_halfblack.hide();
                        }
                    }
                });
            });
        },
        hide: function () {
            var $this = this;
            var config = $this.data("dropDownSelect");
            if (!($.type(config) == "undefined")) {
                var $dropDownSelect_case = $("#" + config.wrapprefixes.forwrap + $this.attr("id"));
                //隐藏与其对应的下拉框
                $dropDownSelect_case.hide();
                console.info("you call dropDownSelect fun hide!!!");
            }
        },
        destroy: function () {
            // 对选择器每个元素都执行方法
            return this.each(function () {
                // 执行代码
            });
        }
    };
    $.fn.dropDownSelect = function () {
        //get the first argument
        var method = arguments[0];
        if (methods[method]) {
            // 如果存在该方法就调用该方法
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            // 如果传进来的参数是{}, 就认为是初始化操作.
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on zepto.dropDownSelect');
            return this;
        }
    };
})(Zepto, MaskMaker);

(function () {
    //check the UserAgent
    if (!JSHelper.checkUA) {
        JSHelper.checkUA = {};
        //判断是否是QQx5
        JSHelper.checkUA.isQQx5 = function () {
            if (navigator.userAgent.match(/mqqbrowser/gi)) {
                return true;
            }
            return false;
        };
    }
})();

var dateNowFormatter = (function ($) {
    var monthmap = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

    function main(P_slter, callback) {
        var nowdate = new Date();
        var month = nowdate.getMonth();
        var day = nowdate.getDate();
        callback(monthmap[month], day);
    }

    return main;
})(Zepto);
/**
 * 取得两点之间的距离
 * 坐标系：百度坐标系
 * @param  {[type]} pointO 
 * 格式:{lat:"纬度",lng:"经度"}
 * @param  {[type]} pointP [description]
 * @return {[type]}        [description]
 */
function getDistanceFromOToP(pointO,pointP){
    var pk = 180 / 3.14169;
    sk_O_lat = pointO.lat / pk;
    sk_O_lng = pointO.lng / pk;
    sk_P_lat = pointP.lat / pk;
    sk_P_lng = pointP.lng / pk;
    t1 = Math.cos(sk_O_lat) * Math.cos(sk_O_lng) * Math.cos(sk_P_lat) * Math.cos(sk_P_lng);
    t2 = Math.cos(sk_O_lat) * Math.sin(sk_O_lng) * Math.cos(sk_P_lat) * Math.sin(sk_P_lng);
    t3 = Math.sin(sk_O_lat) * Math.sin(sk_P_lat);
    tt = Math.acos(t1 + t2 + t3);
    return 6366000 * tt;
}
/**
 * 异步加载js
 * @param  {[type]}   url      [description]
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
function loadScript(url, callback) {
    var script = document.createElement("script");
    script.type = "text/javascript";
    if (script.readyState) { //IE浏览器状态判断
        script.onreadystatechange = function() {
            if (script.readyState == "loaded" || script.readyState == "complete") {
                script.onreadystatechange = null;
                if (callback) {
                    callback();
                }
            }
        };
    } else { //其它浏览器状态判断
        script.onload = function() {
            if (callback) {
                callback();
            }
        };
    }
    script.src = url;
    //document.getElementsByTagName("head")[0].appendChild(script);
    document.body.appendChild(script);
}
/**
 * 商品详情页-商品，详情，评价左右滑动
 * @param  {[type]} title_index 当前访问页面索引值
 * @return {[type]}             [description]
 */
function productSwipeLAndR(title_index){
    $(".container").on({
        'swipeLeft':function(event){
            if(title_index<2){
                $(".shop-items a").eq(parseInt(title_index)+1).click();
            }
        },
        'swipeRight':function(event){
            if(title_index>0){
                $(".shop-items a").eq(parseInt(title_index)-1).click();
            }
        }
    });
}

function wx_on_menu() {
    ajaxGet({
        url: "/wap/mutual/get_config.jhtml",
        data: {
            url: location.href.split('#')[0]
        },
        success: function (message) {
            if (message.type == "success") {
                var data = JSON.parse(message.content);
                var _title = $("meta[property='og:title']").attr("content");
                var _desc = $("meta[property='og:desc']").attr("content");
                var _link = $("meta[property='og:link']").attr("content");
                var _imgUrl = $("meta[property='og:imgUrl']").attr("content");
                if (_title == null || _title == undefined || _title == '') {
                    _title = data.sharetitle;
                    _desc = data.sharedescr;
                    _link = data.sharelink;
                    _imgUrl = data.shareimage;
                }
                wx.config({
                    debug: false,
                    appId: data.appId,
                    timestamp: data.timestamp,
                    nonceStr: data.nonceStr,
                    signature: data.signature,
                    jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage"]
                });
                wx.ready(function () {
                    wx.onMenuShareTimeline({
                        title: _title,
                        desc: _desc,
                        link: _link,
                        imgUrl: _imgUrl
                    });
                    wx.onMenuShareAppMessage({
                        title: _title,
                        desc: _desc,
                        link: _link,
                        imgUrl: _imgUrl
                    });
                });
                wx.checkJsApi({
                    jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                    success: function (res) {
                        // 以键值对的形式返回，可用的api值true，不可用为false
                        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                    }
                });

            }
        }

    });

}

function init() {
    pageManager
    //.push(component)
        .push(wraper)
        .default('wraper')
        .init();
    if (!($(".P-PCG-IN").length >= 0 || $(".INDEX").length >= 0)) {
        //排除特定页面
        fixedEleCopyHandler(".empty-for-fixedbottom_tab", ".am-topbar-fixed-bottom");
    }
    wx_on_menu();
}

