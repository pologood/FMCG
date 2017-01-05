/**
 * page js
 * @for page/whostrap/test/tip
 */
if (_TH_tscpath == "page/whostrap/test/tip") {
    //路由器定义
    var router = new Router({
        container: '#RP-container',
        enter: 'RP-enter',
        leave: 'RP-leave',
        enterTimeout: 250,
        leaveTimeout: 250
    });
    //路由:home
    var route_home = {
        url: '/',
        className: 'RP-tip',
        render: function() {
            var compiler = Handlebars.compile($("#tpl_tip_home").html());
            return compiler({
                timenow: Date().toLocaleString()
            });
        },
        bind: function() {
            //模拟网络延迟,1s
            setTimeout(function() {
                console.log("tip主页面加载完毕");
                $script('../../../js/plugin/moment/moment.min.js', function() {
                    console.log("plugin moment loaded");
                    $(".nowdate.WHT-TIP .bd").text(moment().format('YYYYMMDD'));
                });
            }, 1500);
        }
    };
    //路由:toast
    var route_toast = {
        url: '/toast',
        className: 'RP-toast',
        render: function() {
            //var id = this.params.id;

            var compiler = Handlebars.compile($("#tpl_tip_toast").html());
            return compiler({
                title: "我是toast"
            });
        },
        bind: function() {
            console.log("toast页面加载完毕");
            //初始化 success 提示框
            $(".success.WHT").tip({
                tiptype: "toast",
                tpldatas: {
                    ctns: {
                        txt: "已完成"
                    },
                    hooks: {
                        wrapperid: "success"
                    },
                    Ctrls: {
                        tplname: "toast"
                    }
                }
            });
            //按钮组
            $(".btns").on('click', '.opentoast', function(event) {
                event.preventDefault();
                $(".success.WHT").tip("toggle");
            });
            $(".btns").on('click', '.dosomething', function(event) {
                event.preventDefault();
                setTimeout(function() {
                    $(".success.WHT").tip("show");
                }, 2000);
            });
            //监听插件格式化事件
            $(".success.WHT").on('opened:WHT:tip', function(event) {
                console.log("提示框" + $(this).attr("class") + "已开启");
            });
            $(".success.WHT").on('closed:WHT:tip', function(event) {
                console.log("提示框" + $(this).attr("class") + "已关闭");
            });
        }
    };
    //路由:loadingtoast
    var route_loadingtoast = {
        url: '/loadingtoast',
        className: 'RP-loadingtoast',
        render: function() {
            //var id = this.params.id;
            var compiler = Handlebars.compile($("#tpl_tip_loadingtoast").html());

            return compiler({
                title: "我是loadingtoast"
            });
        },
        bind: function() {
            //相当于模板的onHTMLReady
            console.log("loadingtoast页面加载完毕");
            //asynSomethingA
            function asynSomethingA() {
                var $def = $.Deferred();
                setTimeout(function() {
                    $def.resolve({
                        name: "tom"
                    });
                }, 2000);
                return $def.promise();
            }
            //asynSomethingB
            function asynSomethingB() {
                var $def = $.Deferred();
                setTimeout(function() {
                    $def.resolve({
                        name: "tony"
                    });
                }, 1000);
                return $def.promise();
            }
            //asynSomethingB
            function asynSomethingC() {
                var $def = $.Deferred();
                setTimeout(function() {
                    $def.resolve({
                        name: "zakas"
                    });
                }, 2000);
                return $def.promise();
            }
            $(".btns").on('click', '.openloadingtoastA', function(event) {
                event.preventDefault();
                $(".ONCE").tip({
                    tiptype: "loadingtoast",
                    initialstate: {
                        isShown: true
                    },
                    affair: asynSomethingA(),
                    tpldatas: {
                        hooks: {
                            wrapperid: "loadingB"
                        }
                    }
                });
            });
            $(".btns").on('click', '.openloadingtoastB', function(event) {
                event.preventDefault();
                $(".ONCE").tip({
                    tiptype: "loadingtoast",
                    initialstate: {
                        isShown: true
                    },
                    affair: asynSomethingA(),
                    tpldatas: {
                        hooks: {
                            wrapperid: "loadingA"
                        }
                    }
                });
                setTimeout(function() {
                    $(".ONCE").tip("addAffair", asynSomethingB());
                }, 500);
                setTimeout(function() {
                    $(".ONCE").tip("addAffair", asynSomethingC());
                }, 1000);
            });
            //监听loadingtoast多异步事件结束
            $(document).on('mainTimerEnd:WHT:tip:loadingtoast', '.ONCE', function(event) {
                event.preventDefault();
                console.log("主线定时器结束");
            });
        }
    };
    //初始化路由
    router.push(route_home).push(route_toast).push(route_loadingtoast).setDefault('/').init();
    //$script.path('/js/modules/');

    //prefetch
    hbsTO.prefetch("tip");
}
