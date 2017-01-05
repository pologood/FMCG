/**
 * page js
 * @for page/whostrap/test/tip
 */
if (_TH_.tscpath == "/wap/tenant/index3.0") {
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
        //新品
        var compilerLsNew = Handlebars.compile($("#tpl_ls_new").html());
        //全部
        var compilerLsAllgoods = Handlebars.compile($("#tpl_ls_allgoods").html());
        //asyn声明
        //店铺顶部轮播
        var s = new iSlider($('.silder').get(0), ads, {
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
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', 'lazyload');
        /* 
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function() {
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
            var $save_tenant = $(this);
            event.preventDefault();
            //_TH_.memberid
            if (_TH_.memberid === "") {
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "亲，您还没绑定，暂时不能收藏！"
                });
                location.href = _TH_.base + "/wap/bound/indexNew.jhtml?redirectUrl=" + _TH_.base + "/wap/tenant/index/" + _TH_.tenantid + ".jhtml";
            } else {
                if (_TH_.memberid == _TH_.tenantid) {
                    $save_tenant.addClass('none');
                } else {
                    var ajax_opt = { //默认是取消收藏的数据
                        url: _TH_.base + "/wap/member/favorite/delete.jhtml",
                        type: 'POST',
                        data: {
                            id: _TH_.tenantid,
                            type: "1"
                        },
                        showloading: true
                    };
                    //ajax成功回调
                    var ajaxDoneCallback = function(data) {
                        $save_tenant.children('i').toggleClass('clr-red05 clr-grey08');
                        $("#favorite_num").html(data.data);
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: data.message.content
                        });
                    };
                    if (_TH_.tenant_saveflag == "yes") { //店铺已收藏
                    } else { //用户需要收藏
                        $.extend(true, ajax_opt, {
                            url: _TH_.base + "/wap/member/favorite/add.jhtml"
                        });
                    }
                    if (_TH_.tenant_saveflag == "yes") { //店铺已收藏
                        $.ajax(ajax_opt)
                            .done(function(data) {
                                console.log("success");
                                $save_tenant.children('i').html("&#xe620;");
                                ajaxDoneCallback(data);
                                _TH_.tenant_saveflag = "no";
                            })
                            .fail(function() {
                                console.log("error");
                            })
                            .always(function() {
                                console.log("complete");
                            });
                    } else {
                        $.ajax(ajax_opt)
                            .done(function(data) {
                                console.log("success");
                                $save_tenant.children('i').html("&#xe645;");
                                ajaxDoneCallback(data);
                                _TH_.tenant_saveflag = "yes";
                            })
                            .fail(function() {
                                console.log("error");
                            })
                            .always(function() {
                                console.log("complete");
                            });
                    }
                }
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
                $.ajax({
                        url: _TH_.base + '/app/b2b/cart/add.jhtml',
                        type: 'POST',
                        data: {
                            id: $thiscart.data('productid'),
                            quantity: 1
                        }
                    })
                    .done(function(data) {
                        console.log("success");
                        if (data.message.type == "success") {
                            console.log(data);
                            //当前购物车商品数量
                            if ($topcart_cornernum.hasClass('none')) {
                                $topcart_cornernum.removeClass('none');
                            }
                            $topcart_cornernum.children('b').html(data.data.quantity);
                        } else {
                            $(".ONCET").tip("addTask", {
                                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                txt: data.message.content
                            });
                        }
                    })
                    .fail(function() {
                        console.log("error");
                    })
                    .always(function() {
                        console.log("complete");
                    });
            }
            $waitingcart.on('transitionend webkitTransitionEnd', function(event) {
                $waitingcart.removeClass('anm-fly').addClass('hidden');
                $waitingcart.off("transitionend webkitTransitionEnd");
                $thiscart.data("disabled", false);
            });
            $waitingcart.removeClass("hidden");
            setTimeout(function() {
                $waitingcart.focus().addClass("anm-fly");
            }, 0);
        });
        //底部"宝贝分类"按钮
        $(".btoperas").on('click', '.classification', function(event) {
            var $thepopup = $(".popup-classific.TT-IN");
            if ($thepopup.data("isshown")) {
                $thepopup.hide().data("isshown", false);
            } else {
                $thepopup.show().data("isshown", true);
            }
        });
        //底部"扫码"按钮
        $(".btoperas").on('click', '.scan_code', function(event) {
            event.preventDefault();
            if ($(this).data("asynstate") == "waiting") { //如果仍然是等待状态
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "正在初始化,请稍等..."
                });
                return; //不响应用户点击
            }
            if ($(this).data("asynstate") == "finish") {
                console.log("soga");
                wx.scanQRCode({
                    needResult: 1,
                    scanType: ["qrCode", "barCode"],
                    success: function(res) {
                        //扫描结果
                        var result = res.resultStr;
                        if (result.substr(0, 4) == 'http') {
                            location.href = result;
                        } else {
                            var arr_result = result.split(',');
                            $.ajax({
                                    url: _TH_.base + "/app/product/barcode.jhtml",
                                    data: {
                                        "tenantId": _TH_.tenantid,
                                        "barcode": arr_result[arr_result.length - 1]
                                    },
                                    showloading: true
                                })
                                .done(function(data) {
                                    if (data.message.type == "success") {
                                        if (data.data.length === 0) {
                                            $(".ONCET").tip("addTask", {
                                                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                                txt: "此商品没有上架"
                                            });
                                        } else {
                                            location.href = _TH_.base + '/wap/product/display/' + data.data[0].id + '.jhtml';
                                        }
                                    } else {
                                        $(".ONCET").tip("addTask", {
                                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                            txt: "data.message.content"
                                        });
                                    }
                                })
                                .fail(function() {
                                    console.log("error");
                                });
                        }
                    }
                });
            }
        });
        //底部"联系商家"按钮
        $(".btoperas").on('click', '.contact_tenant', function(event) {
            if ($(this).data("asynstate") == "waiting") { //如果仍然是等待状态
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "正在初始化,请稍等..."
                });
                return; //不响应用户点击
            }
            if ($(this).data("asynstate") == "finish") {
                $(".sheet-tntcontactinfo.TT-IN").actionSheet("show");
            }
        });
        //底部"联盟商家"按钮
        $(".btoperas").on('click', '.union', function(event) {
            if ($(this).hasClass("disabled")) {
                event.preventDefault();
            }
        });
        //宝贝分类弹出框点击句柄
        $(".popup-classific.TT-IN").on('click', 'a', function(event) {
            event.preventDefault();
            var this_menu_a = this;
            var $link_all = $(".nav.TT-IN").find("a.all");
            //该条目和当前显示条目不同
            if ($link_all.data("id") != $(this_menu_a).parent("li").data("id")) {
                $link_all.data("id", $(this_menu_a).parent("li").data("id"));
                $link_all.children('p').text($(this).text());
                //$link_all.addClass('active').siblings(".it").removeClass("active");
                //该条目初次点击状态重置
                $link_all.data("firsttime", true).data("switchcategory", true);
            } else {
                $link_all.data("firsttime", false);
            }
            $(".popup-classific.TT-IN").hide().data("isshown", false);
            $(".nav.TT-IN").find(".it.all").trigger("click");
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
                //console.log("the data hasmore is:");
                //console.log($ctn_btn_more.data("page-hasmore"));
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
                    url: _TH_.base + '/wap/tenant/get_tenant_product/' + _TH_.tenantid + '.jhtml',
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
                    url: _TH_.base + '/wap/tenant/get_category_product/' + _TH_.tenantid + '.jhtml',
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
                        $ctn_btn_more.before(compilerLsAllgoods({
                            column: 2,
                            datas: data
                        }));
                        if (store.session.has("pgtnt_tabdatals")) {
                            store.session.remove("pgtnt_tabdatals");
                        }
                        store.session.set("pgtnt_tabdatals", data);
                    } else {
                        $ctn_btn_more.siblings(".ls_product").append(compilerLsAllgoods({
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
                    url: _TH_.base + '/wap/tenant/get_tenant_product/' + _TH_.tenantid + '.jhtml',
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
                    url: _TH_.base + '/wap/tenant/get_promotion_product/' + _TH_.tenantid + '.jhtml',
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
