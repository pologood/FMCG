/**
 * page js
 * @for page product/display
 */
if (_TH_.tscpath == "/wap/product/display") {
    $(function() {
        //用户购买行为类型 "" / "buy"
        var buyaction_type = "";
        //高度占位元素
        setEmptyEleH(["opera_panel"]);
        //ajax before检测
        $(document).on('ajaxBeforeSend', function(e, xhr, options) {
            if (options.showloading) {
                $(".ONCE").tip("addAffair", xhr);
            }
        });
        //商品相册轮播
        if (_TH_.slider_productctns.length) {
            var slider = new iSlider($(".productalbum.PT-DY").get(0), _TH_.slider_productctns, {
                isLooping: true,
                isOverspread: false,
                isAutoplay: true,
                animateTime: 1800,
                duration: 6000,
                fixPage: false,
                onRenderComplete: function(index, ele) {
                    //$(".productalbum.PT-DY .pagenum").children('i').eq(0).text(index + 1);
                },
                onSlideChange: function(index, ele) {
                    $(".productalbum.PT-DY .pagenum").children('i').eq(0).text(index + 1);
                }
            });
        } else {
            $(".productalbum.PT-DY .pagenum").children('i').eq(0).text(0);
        }
        //轮播页码修正
        $(".productalbum.PT-DY .pagenum").children('i').eq(1).text(_TH_.slider_productctns.length);
        //星级可视化
        $(".starlevels").starLevelize();
        //data brush : compilerDisplayDetail
        var compilerDisplayDetail = Handlebars.compile($("#tpl_display_detail").html());
        //data brush : 店主推荐
        var compilerProductRecommend = Handlebars.compile($("#tpl_product_recommend").html());
        //data brush : 邻家好货
        var compilerProductNearby = Handlebars.compile($("#tpl_product_nearby").html());
        //data brush : pfoot-2.0:联系列表
        var compilerContactlists = Handlebars.compile($("#tpl_contactlists").html());
        //data brush : 选择颜色和尺寸
        var compilerChooseColorsize = Handlebars.compile($("#tpl_choose_colorsize").html());
        //
        $(".shopkeeper_recm-bd").html(compilerProductRecommend(_TH_.productls_recommend));
        $(".nearbygoods-bd").html(compilerProductNearby(_TH_.productls_recommend));
        //实例化页签
        $(".displaytab.PT-DY").tabNav({
            plugintype: "tabnav",
            initialstate: {
                activeindex: -1
            },
            tpldatas: {
                ctns: {
                    heads: ['<a href="javascript:;">详情</a>',
                        '<a href="javascript:;">评价</a>',
                        '<a href="javascript:;">属性</a>'
                    ]
                },
                hooks: {
                    classes: {
                        weui_tabnav_item: "ft-bs15" //,
                            //weui_tabbd_item: "",
                    }
                }
            }
        });
        //加载lazyload
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function() {
            $(".displaytab.self").find(".weui_tabnav_item").eq(0).trigger("click");
        });
        //页签点击，显示内容
        $(".displaytab.PT-DY").on('indexchose:WHT:tabnav', function(event, activeindex, alreadyloaded) {
            event.preventDefault();
            event.preventDefault();
            console.log("activeindex's value is as following:");
            console.log(activeindex);
            console.log("alreadyloaded's value is as following:");
            console.log(alreadyloaded);
            if (alreadyloaded) {
                console.log("已经存在了内容");
            } else {
                //详情
                if (activeindex === 0) {
                    console.log(_TH_.data_display_detail);
                    //生成内容并注入
                    $(".displaytab.PT-DY").tabNav("fillContent", activeindex, compilerDisplayDetail(_TH_.data_display_detail));
                    //lazy处理
                    $(".lazy").picLazyLoad({
                        threshold: 100,
                        placeholder: _TH_.base + '/resources/wap/3.0/image/blank/blank640x640.png'
                    });
                }
                //评价
                if (activeindex === 1) {}
                //属性
                if (activeindex === 2) {}
                //改变副本活动索引值
                $(".displaytab.copy").find(".weui_tabnav_item").eq(activeindex).addClass("weui_tab_item_on").siblings('.weui_tabnav_item').removeClass('weui_tab_item_on');
            }
        });
        //颜色尺寸滑入框实例化
        $(".sheet-choose_colorsize.PT-DY").actionSheet({
            plugintype: "actionsheet",
            tpldatas: {
                hooks: {
                    classes: {
                        weui_actionsheet_ctn: "thetypes PT-DY"
                    }
                },
                Ctrls: {
                    has_action_cancel: false
                }
            }
        });
        //客服(联系商家)滑入框实例化
        $(".sheet-contacttenant.PT-DY").actionSheet({
            plugintype: "actionsheet",
            tpldatas: {
                hooks: {
                    classes: {
                        weui_actionsheet_ctn: "contactlists PT-DY"
                    }
                }
            }
        });
        //选择颜色和分类标签
        $(".choose_colorsize").on('click', function(event) {
            event.preventDefault();
            console.log("sheet-choose_colorsize");
            $(".sheet-choose_colorsize.PT-DY").actionSheet("show");
        });
        //联系导购/客服(商家)
        $(".opera_panel.PT-DY").on('click', '.contact', function(event) {
            event.preventDefault();
            if ($(".sheet-contacttenant.PT-DY").data("contentfilled")) {
                $(".sheet-contacttenant.PT-DY").actionSheet("show");
            } else {
                $.ajax({
                        url: _TH_.base + "/wap/product/contact/" + _TH_.storeid + ".jhtml",
                        type: 'POST'
                    })
                    .done(function(message) {
                        console.log("i get the message");
                        if (message.message.type == "success") {
                            var htmlctn = compilerContactlists({
                                listtitle: "联系列表",
                                listdatas: message.data
                            });
                            $(".sheet-contacttenant.PT-DY").actionSheet("fillContent", htmlctn);
                            $(".sheet-contacttenant.PT-DY").actionSheet("show");
                            $(".sheet-contacttenant.PT-DY").data("contentfilled", true);
                        }
                    })
                    .fail(function() {
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "网络异常"
                        });
                    })
                    .always(function() {
                        //console.log("complete");
                    });
            }
        });
        //点击收藏&取消收藏
        $(".opera_panel.PT-DY").on('click', '.favor', function(event) {
            if (_TH_.hasFavorite == "") { //暂未绑定
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "亲，您还没绑定，暂时不能收藏！"
                });
                //直接跳转绑定页面
                location.href = _TH_.base + "/wap/bound/indexNew.jhtml?redirectUrl=" + _TH_.base + "/wap/product/display/" + _TH_.productid + ".jhtml";
            }
            if (_TH_.hasFavorite == "0") { //已收藏
                $.ajax({
                        url: _TH_.base + "/app/member/favorite/product/delete.jhtml?id=" + _TH_.productid,
                        type: 'POST'
                    })
                    .done(function(message) {
                        //console.log("success");
                        $(this).find("i").toggleClass("clr-red05 clr-grey01").html('&#xe62c;');
                        $(this).find("span").text("收藏");
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "已取消收藏"
                        });
                        _TH_.hasFavorite = "1";
                    })
                    .fail(function() {
                        console.log("error");
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "取消收藏失败"
                        });
                    });
            }
            if (_TH_.hasFavorite == "1") { //未收藏
                $.ajax({
                        url: _TH_.base + "/app/member/favorite/product/add.jhtml?id=" + _TH_.productid,
                        type: 'POST'
                    })
                    .done(function(message) {
                        //console.log("success");
                        $(this).find("i").toggleClass("clr-red05 clr-grey01").html('&#xe655;');
                        $(this).find("span").text("已收藏");
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "收藏成功"
                        });
                        _TH_.hasFavorite = "0";
                    })
                    .fail(function() {
                        console.log("error");
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "收藏失败"
                        });
                    });
            }
        });
        //(颜色尺寸)框内确定按钮
        $(".sheet-choose_colorsize.PT-DY").on('initialized:WHT:actionsheet', function(event) {
            $(".thetypes.PT-DY").on('click', '.sure', function(event) {
                $(".thetypes.PT-DY").chooseCS("getProductIdAndQuantity", function(id, quantity) {
                    if (id, quantity) { //获取id和数量成功
                        if (id) {
                            console.log("your choose product id is");
                            console.log(id);
                        } else {
                            console.log("your choose product id is");
                            console.log(_TH_.productid);
                        }
                        console.log("quantity's value is as following:");
                        console.log(quantity);
                        var data_tobesent = {
                            id: id === 0 ? _TH_.productid : id,
                            quantity: quantity
                        };
                        if (buyaction_type == "buy") {
                            data_tobesent.type = "buy";
                        }
                        $.ajax({
                                url: _TH_.base + '/app/b2b/cart/add.jhtml',
                                type: 'POST',
                                data: data_tobesent
                            })
                            .done(function(data) {
                                if (data.message.type == "success") {
                                    if (buyaction_type == "buy") {
                                        location.href = _TH_.base + "/wap/member/order/orderPay.jhtml";
                                    }
                                    $(".sheet-choose_colorsize.PT-DY").actionSheet("hide");
                                } else {
                                    $(".ONCET").tip("addTask", {
                                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                        txt: data.message.content
                                    });
                                }
                                console.log("success");
                            })
                            .fail(function() {
                                console.log("error");
                            })
                            .always(function() {
                                console.log("complete");
                            });
                    } else { //获取id失败
                        $(".sheet-choose_colorsize.PT-DY").actionSheet("getShownState", function(isShown) {
                            if (isShown) { //已存在
                                $(".ONCET").tip("addTask", {
                                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                    txt: "请选择颜色规格"
                                });
                            } else { //不存在
                                $(".sheet-choose_colorsize.PT-DY").actionSheet("show");
                            }
                        });
                    }
                });
            });
        });
        //底部页面动作前检测
        function checkBeforeBuyAction() {
            if (_TH_.buyaction_status != "success") {
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "不好意思,该商品暂时不支持购买"
                });
                return false;
            }
            if (_TH_.buyaction_isMarketable != "true") {
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "当前商品以下架,如有需要请尝试联系店主或导购!"
                });
                return false;
            }
            return true;
        }
        //加入购物车点击事件
        $(".opera_panel.PT-DY").on('click', '.opera_panel-R1', function(event) {
            event.preventDefault();
            if (!checkBeforeBuyAction()) {
                return;
            }
            buyaction_type = "";
            $(".sheet-choose_colorsize.PT-DY").actionSheet("show");
        });
        //立即购买点击事件
        $(".opera_panel.PT-DY").on('click', '.opera_panel-R2', function(event) {
            event.preventDefault();
            if (!checkBeforeBuyAction()) {
                return;
            }
            buyaction_type = "buy";
            $(".thetypes.PT-DY .sure").trigger("click");
        });
        //页签实例化结束
        $(".displaytab.PT-DY").on('initialized:WHT:tabnav', function(event) {
            event.preventDefault();
            //生成页签副本
            var $displaytab_copy = $(".displaytab.PT-DY").clone().addClass("copy").removeClass("self");
            $displaytab_copy.find(".weui_tab_bd").remove();
            $displaytab_copy.appendTo('body').addClass("leaveout");
            $displaytab_copy.on('click', '.weui_tabnav_item', function(event) {
                event.preventDefault();
                var $this_tabitem = $(this).hasClass("weui_tabbd_item") ? $(this) : $(this).closest(".weui_tabnav_item");
                if ($this_tabitem.hasClass('weui_tab_item_on')) {
                    //如果点击的是已经激活的页签，不响应动作，直接返回
                    return;
                } else {
                    $this_tabitem.addClass("weui_tab_item_on").siblings('.weui_tabnav_item').removeClass('weui_tab_item_on');
                    $(".displaytab.PT-DY").find(".weui_tabnav_item").eq($this_tabitem.index()).trigger('click');
                }
            });
        });
        //针对页签做scrollchange监听处理
        var sc_displaytab = new ScrollChange({
            wrap_slter: ".displaytab.PT-DY.self",
            threshold: $(".displaytab.PT-DY.self").offset().top + $(".displaytab.PT-DY.self").height() + 50,
            custom_justBelowThreshold: function() {
                console.log("just below");
                //副本出现 comeout
                $(".displaytab.PT-DY.copy").removeClass("leaveout");
                $(".displaytab.PT-DY.self .weui_tabnav").addClass("leaveout-trsi").focus().addClass("leaveout");
                //$(".displaytab.PT-DY.copy").show();
            },
            custom_belowThreshold: function() {
                //console.log("below");
                if ($(".displaytab.PT-DY.copy").hasClass('leaveout')) {
                    $(".displaytab.PT-DY.copy").removeClass("leaveout");
                }
            },
            custom_justOverThreshold: function() {
                console.log("just over");
                //副本离开 leaveout
                $(".displaytab.PT-DY.copy").addClass("leaveout");
                $(".displaytab.PT-DY.copy").on('transitionend webkitTransitionEnd', function(event) {
                    event.preventDefault();
                });
                $(".displaytab.PT-DY.self .weui_tabnav").addClass("leaveout-trsi").focus().removeClass("leaveout");
                //$(".displaytab.PT-DY.copy").hide();
            },
            custom_overThreshold: function() {
                //console.log("over");
                if (!$(".displaytab.PT-DY.copy").hasClass('leaveout')) {
                    $(".displaytab.PT-DY.copy").addClass("leaveout");
                }
            }
        });
        //加载颜色尺寸选择插件
        $script(_TH_.base + '/resources/wap/3.0/js/module/choosecolorsize/choosecolorsize.js', function() {
            $.ajax({
                    url: _TH_.base + '/wap/product/view1.jhtml',
                    data: { id: _TH_.productid }
                })
                .done(function(datas) {
                    //datas 相关商品条目集合 初始数据集
                    console.log("初始数据集");
                    console.log(datas);
                    //初始化 chooseCS 插件
                    $(".thetypes.PT-DY").chooseCS({
                        tplid: "tpl_choose_colorsize",
                        initialdatas: datas
                    });
                })
                .fail(function() {
                    //console.log("error");
                    $(".ONCET").tip("addTask", {
                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                        txt: "网络异常"
                    });
                });
            var mock_CSdata = {
                display: {
                    price: 489,
                    stock: 10,
                    choseninfo: {
                        color: "绿色",
                        spec: " S"
                    }
                },
                filters: {
                    colors: ["红色", "绿色", "黑色"],
                    specs: ["S", "M", "L"]
                }
            };
            console.log("choose me");
            //compilerChooseColorsize
            //var htmlctn = compilerChooseColorsize(mock_CSdata);
            //$(".sheet-choose_colorsize.PT-DY").actionSheet("fillContent", htmlctn);
        });
        //
        $(window).on('scroll', $.proxy(sc_displaytab.movehandler, sc_displaytab));
    });
}
