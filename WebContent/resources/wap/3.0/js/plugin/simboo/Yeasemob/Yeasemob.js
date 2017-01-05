/*!
 * Yeasemob 聊天
 * 基于zepto 环信
 * @author simboo
 */
;
(function($, WebIM, tpl_message_id) {
    /**
     * Yeasemob Class
     * Yeasemob基类定义
     */
    Yeasemob = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, Yeasemob.DEFAULTS, options);
        //变量
        this.connection = null; //会话连接,提前声明,后赋值
        //初始化
        this._init();
        //event声明
        //tab-head点击事件
        this.$ele.on('click.yeasemob.send', '[jhk="btnsend"]', $.proxy(this._sendMessageHandler, this));
        //this.$ele.find('.weui_yeasemob_item').on('click.yeasemob.head', $.proxy(this.headClickhandler, this));
    };
    //插件版本
    Yeasemob.VERSION = '1.0.0';
    //默认配置(all)
    Yeasemob.DEFAULTS = {
        initialstate: {
            //
        },
        user_self: null, //本地用户信息
        user_target: null //目标用户信息
    };
    //通信连接事件回调
    Yeasemob.prototype._eventCallbacks = {
        onOpened: function(message) {
            console.log("you open the conn");
            console.log("event open");
            console.log(message);
            conn.setPresence();
        },
        onClosed: function(message) {
            console.log("you close the conn");
            console.log("event close");
            console.log(message);
        },
        onPresence: function(message) {
            console.log("i exist");
        },
        onTextMessage: function(message) {
            var this_instance = $(".Yeasemob-RP").data("yeasemob");
            console.log("this is your text message");
            console.log(message);
            //把收到的消息添加到聊天界面
            this_instance._addMessageToScreen(message.data, "left");
        },
        onOnline: function() {
            console.log("i am online");
        },
        onError: function(message) {
            console.log("error");
            console.log(message);
        }
    };
    //将消息添加到屏幕
    Yeasemob.prototype._addMessageToScreen = function(message, direction) {
        var this_instance = this;
        if (message == "" || !message) {
            return;
        }
        var direction = direction || "right";
        var compilerMSG = Handlebars.compile($("#" + tpl_message_id).html());
        //待装载数据
        var datatoload = {
            direction: direction,
            userinfotxt: message
        };
        //扩展缩略图
        if (direction == "right") { //本地用户
            $.extend(true, datatoload, {
                userthumb: this_instance.options.user_self.userthumb
            });
        } else {
            $.extend(true, datatoload, {
                userthumb: this_instance.options.user_target.userthumb
            });
        }
        this_instance.$ele.find('.YM-displayR').append(compilerMSG(datatoload));
    };
    //同服务器建立连接
    Yeasemob.prototype._establishConnection = function() {
        var this_instance = this;
        this_instance.connection = new WebIM.connection({
            https: WebIM.config.https,
            url: WebIM.config.xmppURL,
            isAutoLogin: WebIM.config.isAutoLogin,
            isMultiLoginSessions: WebIM.config.isMultiLoginSessions
        });
    };
    //打开会话连接
    Yeasemob.prototype._openConnection = function() {
        var this_instance = this;
        if (!this_instance.options.user_self) {
            return;
        }
        this_instance.connection.open({
            apiUrl: WebIM.config.apiURL,
            user: this_instance.options.user_self.user,
            pwd: this_instance.options.user_self.pwd,
            appKey: WebIM.config.appkey
        });
    };
    //获取输入框消息(文本)
    Yeasemob.prototype.getInputText = function() {
        var this_instance = this;
        return this_instance.$ele.find('[jhk="inputtxt"]').val();
    };
    //清空输入框消息(文本)
    Yeasemob.prototype.clearInputText = function() {
        var this_instance = this;
        this_instance.$ele.find('[jhk="inputtxt"]').val("");
    };
    //发送消息(用户点击发送按钮)
    Yeasemob.prototype._sendMessageHandler = function() {
        var this_instance = this;
        var inputtxt = this_instance.getInputText();
        var message_id = this_instance.connection.getUniqueId(); //生成本地消息id
        var message = new WebIM.message('txt', message_id); //创建文本消息
        message.set({
            msg: inputtxt,
            to: this_instance.options.user_target.user,
            roomType: false,
            ext: {
                headIMg: this_instance.options.user_self.userthumb,
                nickName: '',
                tenantName: this_instance.options.user_self.tenantName
            },
            success: function(message_id,serverMsgId) {
                console.log("消息发送成功"); //消息发送成功回调   
                console.log("serverMsgId's value is as following:");
                console.log("inputtxt's value is as following:");
                console.log(inputtxt);
                console.log(serverMsgId);
                this_instance._addMessageToScreen(inputtxt);
                this_instance.clearInputText();
            }
        });
        //message.body.roomType = false;
        this_instance.connection.send(message.body);
    };
    //附加回调监听器
    Yeasemob.prototype._patchEventCallbacks = function() {
        var this_instance = this;
        this_instance.connection.listen(this_instance._eventCallbacks);
    };
    //搭建html结构 
    Yeasemob.prototype._build = function() {
        var def = $.Deferred();
        var this_instance = this;
        setTimeout(function() {
            //已手动搭建主体html结构，暂不需要生成html结构
            def.resolve();
        }, 10);
        return def.promise();
    };
    //初始化提示框
    Yeasemob.prototype._init = function() {
        var this_instance = this;
        //搭建结构
        $.when(this_instance._build())
            .done(function() {
                //切换到初始索引
                //发送初始化完毕事件
                //this_instance.$ele.trigger($.Event('initialized:WHT:yeasemob'));
                //建立同服务器的连接
                this_instance._establishConnection();
                //启动事件监听
                this_instance._patchEventCallbacks();
                //打开连接
                this_instance._openConnection();
            })
            .fail(function() {
                console.log("网络异常");
            });
    };
    /**
     * Yeasemob Plugin
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
            var data_plugin = $this.data('yeasemob');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    data_plugin = new Yeasemob(this, option);
                    $this.data('yeasemob', data_plugin);
                    //data_plugin._init();
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
                    $.info('Method ' + option + ' does not exist on Zepto.yeasemob');
                }
            }
        });
    }
    //如果之前存在其他的yeasemob插件，暂存
    var old = $.fn.yeasemob;
    //挂载到$原型链
    $.fn.yeasemob = Plugin;
    //构造器指向类Yeasemob
    $.fn.yeasemob.Constructor = Yeasemob;
    //Yeasemob noConflict
    $.fn.yeasemob.noConflict = function() {
        $.fn.yeasemob = old;
        return this;
    };
    //暂不支持data-api
})(Zepto, WebIM, "tpl_message");
