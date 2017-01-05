/**
 * page js
 * @for page indexnew
 */
if (_TH_.tscpath == "/wap/indexnew") {
    $(function() {
        //高度占位元素
        setEmptyEleH(["topbar", "G-nav"]);
        //ajax before检测
        $(document).on('ajaxBeforeSend', function(e, xhr, options) {
            if (options.showloading) {
                $(".ONCE").tip("addAffair", xhr);
            }
        });
        //default ads pic
        if (_TH_.adspic.length === 0) {
            _TH_.adspic.push({ content: _TH_.base + '/resources/wap/2.0/upload/no-store.png' });
        }
        //carousel
        var slider = new iSlider($(".carousel.IDN").get(0), _TH_.adspic, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 1000
        });
        //channel anm
        function channelBounce() {
            var $cnsign = $(".channels").find(".channel_sign").eq(Math.round(Math.random() * 7));
            $cnsign.addClass('bounceup');
            $cnsign.on('animationend webkitAnimationEnd', function(event) {
                event.preventDefault();
                $(this).removeClass('bounceup').off('animationend webkitAnimationEnd');
            });
            setTimeout(channelBounce, 1000 * randomTimeDuration(6, 10)); //6, 10
        }
        //data brush
        var compilerItemlist = Handlebars.compile($("#tpl_itemlist").html());
        //lazy js,lazyload
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function() {
            $(".lazy").picLazyLoad({
                threshold: 100,
                placeholder: _TH_.base + '/resources/wap/2.0/images/AccountBitmap-product.png'
            });
            //$(".channels .channel_sign").removeClass('anm-init');
            $(".channels .channel_sign").each(function(index, ele) {
                $(ele).on('transitionend webkitTransitionEnd', function(event) {
                    event.preventDefault();
                    $(this).removeClass('anm-trsi');
                });
                $(ele).removeClass('anm-init');
            });
            //
            setTimeout(function() {
                //run channel anm
                //channelBounce();
            }, 1000);
        });
        //lazy js,get the weixin jssdk
        //初始化微信接口
        _wxSDK.initInterface($script, {
            afterOnMenuShare: function() {
                //访问微信地理位置接口
                _wxSDK.getLocation()
                    .done(function(lat, lng) {
                        getCityNameByPos(lat, lng)
                            .done(function(info) {
                                //true
                                if (info.data.update) {
                                    $(".switchtocity").on('determined:WHT:dialog:confirm', function(event) {
                                        //城市切换逻辑
                                        location.href = _TH_.base + "/wap/area/update_current.jhtml?lat=" + lat + "&lng=" + lng;
                                    });
                                    $(".switchtocity").dialog({
                                        dialogtype: "confirm",
                                        initialstate: {
                                            isShown: true
                                        },
                                        tpldatas: {
                                            ctns: {
                                                txt_ok: "确定",
                                                txt_cancel: "取消",
                                                txt_title: "提示",
                                                txt_body: "检测到您当前城市是" + info.data.area.name + "，需要切换到当前城市吗？"
                                            },
                                            hooks: {
                                                wrapperid: "switchtocity"
                                            },
                                            Ctrls: {
                                                tplname: "confirm"
                                            }
                                        }
                                    });
                                }
                            })
                            .fail();
                    })
                    .fail(function() {
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "获取地址位置失败"
                        });
                    })
                    .always(function() {
                        setTimeout(function() {
                            //如果存在之前的页码数
                            if (store.session.get("pgindex_pagenum")) {
                                //事务:从本地拿到数据刷出页面
                                var getFromStorageAndShow = function() {
                                    var $def = $.Deferred();
                                    var tenant_datalists = store.session.get("pgindex_rectenantls");
                                    //todo:使用之前保存的数据刷出主页
                                    for (var i = 0; i < tenant_datalists.length; i++) {
                                        //tenant_datalists[i];
                                        //填充指定元素
                                        //compilerItemlist
                                        $(".recomtenants .loadonclick").before(compilerItemlist(tenant_datalists[i]));
                                    }
                                    $(".lazy").picLazyLoad({
                                        threshold: 100,
                                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                                    });
                                    //数字星级化
                                    //starLevelize(".starlevels");
                                    $(".starlevels").starLevelize();
                                    //数据刷完了重置写入当前页码
                                    $(".recomtenants .loadonclick").data("pagenow", store.session.get("pgindex_pagenum"));
                                    if (store.session.get("pgindex_scrolltop")) {
                                        //store.session.get("pgindex_scrolltop")
                                        $(window).scrollTop(store.session.get("pgindex_scrolltop"));
                                    }
                                    //resolve
                                    setTimeout(function() {
                                        $def.resolve();
                                    }, 100);
                                    return $def.promise();
                                };
                                $(".ONCE").tip("addAffair", getFromStorageAndShow());
                            } else {
                                //手动触发加载第一页推荐商家数据
                                $(".recomtenants .loadonclick").trigger('click');
                            }
                        }, 0);
                    });
            }
        });
        //点击扫一扫按钮
        $(".topbar").on('click', '#scan_fun', function(event) {
            event.preventDefault();
            if (!wx) {
                return;
            }
            _wxSDK.scanQRCode();
        });
        //点击加载更多
        $(".recomtenants").on('click', '.loadonclick', function(event) {
            event.preventDefault();
            var this_instance = this;
            var pagenow = 0;
            //var $loadmorefor = $(this_instance).parent();
            if ($(this_instance).data("pagenow")) {
                //$(this_instance).data("pagenow",1);
                pagenow = parseInt($(this_instance).data("pagenow"), 10);
            }
            //要取的数据集页码数
            pagenow++;
            //获取到地理位置
            //ajax获取第一页的数据，填充到指定元素
            _wxSDK.getLocation()
                .done(function(lat, lng) {
                    $.ajax({
                            url: _TH_.base + "/app/b2c/tenant/list.jhtml",
                            data: {
                                tenantCategoryId: '',
                                communityId: '',
                                orderType: 'weight',
                                pageNumber: pagenow,
                                areaId: _TH_.area.id,
                                lat: lat,
                                lng: lng
                            },
                            showloading: true
                        })
                        .done(function(datas) {
                            //console.log("success");
                            //console.log(datas);
                            if (datas.message.type == "success") {
                                //获得了新数据
                                if (datas.data.length) {
                                    var tenantls = [];
                                    //填充指定元素
                                    //compilerItemlist
                                    $(this_instance).before(compilerItemlist(datas));
                                    $(".lazy").picLazyLoad({
                                        threshold: 100,
                                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                                    });
                                    //数字星级化
                                    //starLevelize(".starlevels");
                                    $(".starlevels").starLevelize();
                                    //数据刷完了重置写入当前页码
                                    $(this_instance).data("pagenow", pagenow);
                                    if (pagenow == 1) {
                                        store.session.remove("pgindex_rectenantls");
                                    }
                                    if (store.session.has("pgindex_rectenantls")) {
                                        //将当前数据集合加入到已存在的数据集合中
                                        tenantls = store.session.get("pgindex_rectenantls");
                                    }
                                    tenantls.push(datas);
                                    store.session.set("pgindex_rectenantls", tenantls);
                                } else {
                                    //如果拿不到数据,不填充,页码不累加
                                    $(this_instance).text("已是最后一页");
                                }
                            }
                            //console.log("当前页码是:" + pagenow);
                        })
                        .fail(function() {
                            //console.log("error");
                        });
                })
                .fail(function() {
                    $(".ONCET").tip("addTask", {
                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                        txt: "获取地址位置失败"
                    });
                });
            //调取成功,pagenow+1，保存到元素 data中
        });
        $(".activity.a826,.activitylarge-img").on('click', function(event) {
            event.preventDefault();
            _wxSDK.getLocation()
                .done(function(lat, lng) {
                    location.href = _TH_.base + "/wap/activity/index/" + _TH_.activityid + ".jhtml?lat=" + lat + "&lng=" + lng;
                })
                .fail(function() {
                    location.href = _TH_.base + "/wap/activity/index/" + _TH_.activityid + ".jhtml";
                });
            //location.href = _TH_.base + "/wap/activity/index/" + _TH_.activityid + ".jhtml";
        });
        //
        $(".activitylarge .closebtn").on('click', function(event) {
            event.stopPropagation();
            $(".activitylarge").addClass("none");
            //$(".activitylarge-mask").removeClass("anm-fade");
            $(".activity.a826").removeClass('none anm-out').addClass('anm-in');
        });
        $(window).on('unload', function(event) {
            //event.preventDefault();
            $(".ONCET").tip("addTask", {
                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                txt: "beforeunload事件触发"
            });
            //页面卸载之前清除userlocation
            store.session.remove('userlocation');
            //保存刚刚在首页浏览到的页码(推荐商家)
            if ($(".recomtenants .loadonclick").data('pagenow')) {
                store.session.set('pgindex_pagenum', $(".recomtenants .loadonclick").data('pagenow'), true);
            }
            //保存在首页的滚动位置
            store.session.set("pgindex_scrolltop", $(window).scrollTop());
        });
    });
}
