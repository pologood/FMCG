/**
 * page js
 * @for page/whostrap/test/tip
 */
if (_TH_.tscpath == "/app/tenant/index3.0") {
    $(function() {

        //check page has been cache 
        var flag_ispageback = false;
        var cacheinput = $("#pagecache").get(0);
        if (cacheinput.value !== "") {
            flag_ispageback = true;
        }
        cacheinput.value = "somevalue";
        //高度占位元素
        setEmptyEleH(["btoperas", "top"]);
        //ajax before检测
        $(document).on('ajaxBeforeSend', function(e, xhr, options) {
            if (options.showloading) {
                $(".ONCE").tip("addAffair", xhr);
            }
        });
        //data brush
        //促销
        var compilerLsPromotion = Handlebars.compile($("#tpl_ls_promotion").html());
        var compilerLsNew = Handlebars.compile($("#tpl_ls_new").html());
        //asyn声明
        //店铺顶部轮播
        var s = new iSlider($('.silder').get(0), _TH_.ads, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 1800,
            duration: 6000,
            animateType: 'zoomout' //zoomout,fade
        });
        //lazyload img, id is lazyload
        $script.ready('lazyload', function() {
            $(".lazy").picLazyLoad({
                threshold: 100
            });
        });
        //实例化回到顶部
        $(".backtotop").positionScroller({
            plugintype: "positionscroller", //默认值
            autohide_threshold: $(window).height() / 2,
            autohide: true,
            tpldatas: {
                ctns: {
                    //txt: "置顶"
                },
                hooks: {
                    wrapperid: "backtotop"
                },
                Ctrls: {
                    tplname: "positionscroller"
                }
            }
        });
        $script(_TH_.base + '/resources/app/3.0/js/plugin/lazyload/lazyload.min.js', 'lazyload');
        /* 
         $script(_TH_.base + '/resources/app/3.0/js/plugin/lazyload/lazyload.min.js', function() {
         $(".lazy").picLazyLoad({
         threshold: 100
         });
         });*/
        //数字星级化
        $(".starlevels").starLevelize();
        //初始化微信接口
        _wxSDK.initInterface($script, {
            afterOnMenuShare: function() {
                //更改异步状态锁
                $(".btoperas .scan_code").data("asynstate", "finish");
            }
        });
        //店铺(门店)信息滑入框实例化
        $(".sheet-tntcontactinfo.TT-IN").actionSheet({
            plugintype: "actionsheet",
            tpldatas: {
                hooks: {
                    classes: {
                        weui_actionsheet_ctn: "tntcontactinfo TT-IN"
                    }
                },
                Ctrls: {
                    has_action_cancel: false
                }
            }
        });
        //宝贝分类弹出框初始化(定位)
        $(".popup-classific.TT-IN").css('bottom', $(".btoperas.TT-IN").height());
        //监听initialized:WHT:actionsheet
        $(".sheet-tntcontactinfo.TT-IN").on('initialized:WHT:actionsheet', function(event) {
            //填充
            $(this).actionSheet("fillContent", $("#tpl_tntcontactinfo").html());
            $(".btoperas .contact_tenant").data("asynstate", "finish");
        });
        //店铺信息-(取消)收藏店铺
        $(".banner-shopinfo").on('click', '.save_tenant', function(event) {
            var ajax_opt = { //默认是取消收藏的数据
                url: _TH_.base + "/wap/member/favorite/delete.jhtml",
                type: 'POST',
                data: {
                    id: _TH_.tenantid,
                    type: "1"
                },
                showloading: true
            };

            var ajaxDoneCallback = function(data) {
                $save_tenant.children('i').toggleClass('clr-red05 clr-grey08');
                $("#favorite_num").html(data.data);
            };

            var $save_tenant = $(this);
            event.preventDefault();
            if (_TH_.tenant_saveflag == "yes") { //店铺已收藏
                location.href = "rzico://favorite/tenant/delete?id=" + _TH_.tenantid;
                $.ajax(ajax_opt)
                    .done(function(data) {
                        $save_tenant.children('i').html("&#xe620;");
                        _TH_.tenant_saveflag = "no";
                        ajaxDoneCallback(data);
                    })
                    .fail(function() {
                        console.log("error");
                    })
                    .always(function() {
                        console.log("complete");
                    });

            } else {
                location.href = "rzico://favorite/tenant?id=" + _TH_.tenantid;

                $.extend(true, ajax_opt, {
                    url: _TH_.base + "/wap/member/favorite/add.jhtml"
                });

                $.ajax(ajax_opt)
                    .done(function(data) {
                        $save_tenant.children('i').html("&#xe645;");
                        _TH_.tenant_saveflag = "yes";
                        ajaxDoneCallback(data);
                    })
                    .fail(function() {
                        console.log("error");
                    })
                    .always(function() {
                        console.log("complete");
                    });
            }
        });
        //商品列表-新品概览点击按钮加入购物车
        $(".newgoods .box_product").on('click', '.buynow', function(event) {
            var $thiscart = $(this);
            var $waitingcart = $thiscart.children('.waitingcart');
            var $topcart_cornernum = $(".top.TT-IN .cornernum");
            event.preventDefault();
            if ($thiscart.data('disabled')) {
                return;
            }
            $thiscart.data("disabled", true);
            //call the remote interface
            if ($thiscart.data('productid')) {
                location.href = "rzico://cart/add?id=" +  $thiscart.data('productid')+"&quantity=1";
            }
        });


        //店铺导航点击句柄
        $(".nav.TT-IN").on('click', '.it', function(event) {
            event.preventDefault();
            var $this_navtab = $(this);
            var $ctn_btn_more = null;
            var page_size = 0;
            var page_number = 0;
            var order_type = "";
            var page_hasmore = true;
            //当前点击项索引值
            var this_navtab_index = $this_navtab.index();
            //切换tab激活态
            $this_navtab.addClass('active').siblings().removeClass("active");
            if ($this_navtab.data("firsttime")) { //是初次点击，全部 新品 促销 需要加载数据
                //如果店铺首页
                if ($this_navtab.hasClass("index")) {}
                //如果是全部好货
                if ($this_navtab.hasClass("all")) {
                    //$ctn_btn_more = $(".for-all .loadonclick");
                    $(".for-all .loadonclick").trigger("click");
                }
                //如果是新品
                if ($this_navtab.hasClass("new")) {
                    $(".for-new .loadonclick").trigger("click");
                }
                //如果是促销
                if ($this_navtab.hasClass("promotion")) {
                    $(".for-promotion .loadonclick").trigger("click");
                }
                $this_navtab.data("firsttime", false);
            }
            $(".nav_ctnR").children('.it').eq(this_navtab_index).removeClass("none").siblings().addClass('none');
        });
        //加载更多(按钮)-外部方法声明
        var externalmethods_loadonclick = {
            refreshState: function() {
                $ctn_btn_more = this;
                var page_hasmore = $ctn_btn_more.data("page-hasmore");
                if (!page_hasmore) {
                    $ctn_btn_more.text("没有更多了");
                }
            }
        };
        //加载更多(按钮)事件绑定-全部
        $(".for-all").on('click', '.loadonclick', function(event) {
            $ctn_btn_more = $(this);
            var $nav_all = $(".nav.TT-IN").find("a.all");
            var page_size = parseInt($ctn_btn_more.data("page-size"));
            var page_number = parseInt($ctn_btn_more.data("page-number"));
            var order_type = $ctn_btn_more.data("page-ordertype");
            var page_hasmore = $ctn_btn_more.data("page-hasmore");
            var needloading = $ctn_btn_more.data("needloading");
            var all_id = !$nav_all.data("id") ? "all" : $nav_all.data("id");
            var all_ajax_opts = {};
            if ($nav_all.data("switchcategory")) {
                page_number = 1;
                $nav_all.data("switchcategory", false);
                //属性重置
                page_hasmore = true;
            }
            if (!needloading) { //不需要(remote)加载，直接返回
                $ctn_btn_more.data("needloading", true); //趋势锁复原
                return;
            }
            if (!page_hasmore) { //没有更多数据,直接返回
                return;
            }
            if (all_id == "all") {
                all_ajax_opts = {
                    url: _TH_.base + '/app/tenant/get_tenant_product/' + _TH_.tenantid + '.jhtml',
                    data: {
                        pageSize: page_size,
                        pageNumber: page_number,
                        type: "all",
                        orderType: order_type
                    },
                    showloading: true
                };
            } else {
                all_ajax_opts = {
                    url: _TH_.base + '/app/tenant/get_category_product/' + _TH_.tenantid + '.jhtml',
                    data: {
                        productCategoryId: all_id,
                        pageSize: page_size,
                        pageNumber: page_number,
                        orderType: order_type
                    },
                    showloading: true
                };
            }
            $.ajax(all_ajax_opts)
                .done(function(data) {
                    console.log("success");
                    console.log(data);
                    if (!data || !data.length) { //获取不到数据
                        $ctn_btn_more.data("page-hasmore", false);
                        //清空前一子目录选择后的数据痕迹
                        if (page_number === 1) {
                            if ($ctn_btn_more.siblings(".ls_product").length) {
                                $ctn_btn_more.siblings(".ls_product").remove();
                            }
                        }
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "暂无数据"
                        });
                        externalmethods_loadonclick.refreshState.call($ctn_btn_more);
                        return;
                    }
                    if (data.length < page_size && data.length > 0) { //最后不完全数据条目
                        $ctn_btn_more.data("page-hasmore", false);
                    }
                    //装载数据
                    if (page_number === 1) {
                        if ($ctn_btn_more.siblings(".ls_product").length) {
                            $ctn_btn_more.siblings(".ls_product").remove();
                        }
                        $ctn_btn_more.before(compilerLsNew({
                            column: 2,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            store.session.remove("pgtnt_tabdatals");
                        }
                        store.session.set("pgtnt_tabdatals", data);
                    } else {
                        $ctn_btn_more.siblings(".ls_product").append(compilerLsNew({
                            column: 2,
                            addition: true,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            var tabdatalist = store.session.get("pgtnt_tabdatals");
                            store.session.set("pgtnt_tabdatals", [].concat(tabdatalist, data));
                        }
                    }
                    $ctn_btn_more.data("page-number", page_number + 1);
                    externalmethods_loadonclick.refreshState.call($ctn_btn_more);
                    if ($.fn.picLazyLoad) {
                        $(".lazy").picLazyLoad({
                            threshold: 100
                        });
                    }
                })
                .fail(function() {
                    console.log("error");
                })
                .always(function() {
                    console.log("complete");
                });
        });
        //加载更多(按钮)事件绑定-新品
        $(".for-new").on('click', '.loadonclick', function(event) {
            $ctn_btn_more = $(this);
            var page_size = parseInt($ctn_btn_more.data("page-size"));
            var page_number = parseInt($ctn_btn_more.data("page-number"));
            var order_type = $ctn_btn_more.data("page-ordertype");
            var page_hasmore = $ctn_btn_more.data("page-hasmore");
            var needloading = $ctn_btn_more.data("needloading");
            if (!needloading) { //不需要(remote)加载，直接返回
                $ctn_btn_more.data("needloading", true); //趋势锁复原
                return;
            }
            if (!page_hasmore) { //没有更多数据,直接返回
                return;
            }
            $.ajax({
                    url: _TH_.base + '/app/tenant/get_tenant_product/' + _TH_.tenantid + '.jhtml',
                    data: {
                        pageSize: page_size,
                        pageNumber: page_number,
                        type: "new",
                        orderType: order_type
                    },
                    showloading: true
                })
                .done(function(data) {
                    console.log("success");
                    console.log(data);
                    if (!data || !data.length) { //获取不到数据
                        $ctn_btn_more.data("page-hasmore", false);
                        /* 
                         if ($ctn_btn_more.siblings(".ls_product").length) {
                         $ctn_btn_more.siblings(".ls_product").remove();
                         }*/
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "暂无数据"
                        });
                        externalmethods_loadonclick.refreshState.call($ctn_btn_more);
                        return;
                    }
                    if (data.length < page_size && data.length > 0) { //最后不完全数据条目
                        $ctn_btn_more.data("page-hasmore", false);
                    }
                    if (page_number === 1) {
                        if ($ctn_btn_more.siblings(".ls_product").length) {
                            $ctn_btn_more.siblings(".ls_product").remove();
                        }
                        //装载数据
                        $ctn_btn_more.before(compilerLsNew({
                            column: 2,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            store.session.remove("pgtnt_tabdatals");
                        }
                        store.session.set("pgtnt_tabdatals", data);
                    } else {
                        //装载数据
                        $ctn_btn_more.siblings(".ls_product").append(compilerLsNew({
                            column: 2,
                            addition: true,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            var tabdatalist = store.session.get("pgtnt_tabdatals");
                            store.session.set("pgtnt_tabdatals", [].concat(tabdatalist, data));
                        }
                    }
                    $ctn_btn_more.data("page-number", page_number + 1);
                    externalmethods_loadonclick.refreshState.call($ctn_btn_more);
                    if ($.fn.picLazyLoad) {
                        $(".lazy").picLazyLoad({
                            threshold: 100
                        });
                    }
                })
                .fail(function() {
                    console.log("error");
                })
                .always(function() {
                    console.log("complete");
                });
        });
        //加载更多(按钮)事件绑定-促销
        $(".for-promotion").on('click', '.loadonclick', function(event) {
            $ctn_btn_more = $(this);
            var page_size = parseInt($ctn_btn_more.data("page-size"));
            var page_number = parseInt($ctn_btn_more.data("page-number"));
            var order_type = $ctn_btn_more.data("page-ordertype");
            var page_hasmore = $ctn_btn_more.data("page-hasmore");
            var needloading = $ctn_btn_more.data("needloading");
            if (!needloading) { //不需要(remote)加载，直接返回
                $ctn_btn_more.data("needloading", true); //趋势锁复原
                return;
            }
            if (!page_hasmore) { //没有更多数据,直接返回
                return;
            }
            $.ajax({
                    url: _TH_.base + '/app/tenant/get_promotion_product/' + _TH_.tenantid + '.jhtml',
                    data: {
                        pageSize: page_size,
                        pageNumber: page_number,
                        type: "promotion",
                        orderType: order_type
                    },
                    showloading: true
                })
                .done(function(data) {
                    console.log("success");
                    console.log(data);
                    if (!data || !data.length) { //获取不到数据
                        $ctn_btn_more.data("page-hasmore", false);
                        /* 
                         if ($ctn_btn_more.siblings(".ls_product").length) {
                         $ctn_btn_more.siblings(".ls_product").remove();
                         }*/
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "暂无数据"
                        });
                        externalmethods_loadonclick.refreshState.call($ctn_btn_more);
                        return;
                    }
                    if (data.length < page_size && data.length > 0) { //最后不完全数据条目
                        $ctn_btn_more.data("page-hasmore", false);
                    }
                    if (page_number === 1) {
                        if ($ctn_btn_more.siblings(".ls_product").length) {
                            $ctn_btn_more.siblings(".ls_product").remove();
                        }
                        //装载数据
                        $ctn_btn_more.before(compilerLsPromotion({
                            column: 2,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            store.session.remove("pgtnt_tabdatals");
                        }
                        store.session.set("pgtnt_tabdatals", data);
                    } else {
                        //装载数据
                        $ctn_btn_more.siblings(".ls_product").append(compilerLsPromotion({
                            column: 2,
                            addition: true,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            var tabdatalist = store.session.get("pgtnt_tabdatals");
                            store.session.set("pgtnt_tabdatals", [].concat(tabdatalist, data));
                        }
                    }
                    $ctn_btn_more.data("page-number", page_number + 1);
                    externalmethods_loadonclick.refreshState.call($ctn_btn_more);
                    if ($.fn.picLazyLoad) {
                        $(".lazy").picLazyLoad({
                            threshold: 100
                        });
                    }
                })
                .fail(function() {
                    console.log("error");
                })
                .always(function() {
                    console.log("complete");
                });
        });
        //店铺首页触发(有条件触发)
        if (!flag_ispageback) {
            $(".nav.TT-IN").find(".it.index").trigger("click");
        } else {
            if (store.session.get("pgtnt_tabinfo")) { //如果能够取到之前保存的导航附加的信息
                //define vars
                var $popupmenu = $(".popup-classific.TT-IN");
                var $ctn_btn_more = null;
                var pgtnt_tabinfo = store.session.get("pgtnt_tabinfo");
                //从本地缓存中取得并显示数据
                var showDataLsFromLS = function($btn_more, hbsCompiler) {
                    var tabdatals = store.session.get("pgtnt_tabdatals");
                    if ($btn_more.siblings(".ls_product").length) {
                        $btn_more.siblings(".ls_product").remove();
                    }
                    if (tabdatals.length) {
                        setTimeout(function() {
                            if ($.type(hbsCompiler) == 'function') {
                                $btn_more.before(hbsCompiler({
                                    column: 2,
                                    datas: tabdatals
                                }));
                            }
                            $btn_more.data("page-number", pgtnt_tabinfo.pagenum + 1);
                            $script.ready('lazyload', function() {
                                $(".lazy").picLazyLoad({
                                    threshold: 100
                                });
                            });
                            //滚动位置
                            if (store.session.get("pgtnt_scrolltop")) {
                                $(window).scrollTop(store.session.get("pgtnt_scrolltop"));
                            }
                        }, 100);
                    }
                };
                switch (pgtnt_tabinfo.category) {
                    case "all":
                        $ctn_btn_more = $(".for-all .loadonclick");
                        //趋势锁needloading临时赋不稳定态false，不触发数据加载
                        $ctn_btn_more.data("page-ordertype", pgtnt_tabinfo.ordertype).data("needloading", false);
                        $popupmenu.find('li').each(function(index, ele) {
                            if (pgtnt_tabinfo.id == $(ele).data("id")) {
                                $(ele).children('a').trigger("click");
                                return false;
                            }
                        });
                        if (store.session.has("pgtnt_tabdatals")) {
                            showDataLsFromLS($ctn_btn_more, compilerLsNew);
                        }
                        break;
                    case "new":
                        $ctn_btn_more = $(".for-new .loadonclick");
                        //趋势锁needloading临时赋不稳定态false，不触发数据加载
                        $ctn_btn_more.data("page-ordertype", pgtnt_tabinfo.ordertype).data("needloading", false);
                        $(".nav.TT-IN").find(".it.new").trigger("click");
                        if (store.session.has("pgtnt_tabdatals")) {
                            showDataLsFromLS($ctn_btn_more, compilerLsNew);
                        }
                        //$(".nav.TT-IN").find(".it.new").trigger("click");
                        break;
                    case "promotion":
                        $ctn_btn_more = $(".for-promotion .loadonclick");
                        //趋势锁needloading临时赋不稳定态false，不触发数据加载
                        $ctn_btn_more.data("page-ordertype", pgtnt_tabinfo.ordertype).data("needloading", false);
                        $(".nav.TT-IN").find(".it.promotion").trigger("click");
                        if (store.session.has("pgtnt_tabdatals")) {
                            showDataLsFromLS($ctn_btn_more, compilerLsPromotion);
                        }
                        break;
                    default:
                        $(".nav.TT-IN").find(".it.index").trigger("click");
                        break;
                }
            } else {
                $(".nav.TT-IN").find(".it.index").trigger("click");
            }
        }
        //window unload
        $(window).on('unload', function(event) {
            var $activetab = $(".nav.TT-IN").find(".it.active");
            //保存在店铺首页的滚动位置
            store.session.set("pgtnt_scrolltop", $(window).scrollTop());
            //index
            if ($activetab.hasClass('index')) {
                //店铺主导航相关信息
                store.session.set("pgtnt_tabinfo", {
                    category: "index"
                });
            }
            //all
            if ($activetab.hasClass('all')) {
                //店铺主导航相关信息
                store.session.set("pgtnt_tabinfo", {
                    category: "all",
                    id: !$activetab.data("id") ? "all" : $activetab.data("id"),
                    pagenum: $(".for-all .loadonclick").data("page-number") - 1,
                    ordertype: $(".for-all .loadonclick").data("page-ordertype")
                });
            }
            //new
            if ($activetab.hasClass('new')) {
                //店铺主导航相关信息
                store.session.set("pgtnt_tabinfo", {
                    category: "new",
                    pagenum: $(".for-new .loadonclick").data("page-number") - 1,
                    ordertype: $(".for-new .loadonclick").data("page-ordertype")
                });
            }
            //promotion
            if ($activetab.hasClass('promotion')) {
                //店铺主导航相关信息
                store.session.set("pgtnt_tabinfo", {
                    category: "promotion",
                    pagenum: $(".for-promotion .loadonclick").data("page-number") - 1,
                    ordertype: $(".for-promotion .loadonclick").data("page-ordertype")
                });
            }
        });
    });
}
