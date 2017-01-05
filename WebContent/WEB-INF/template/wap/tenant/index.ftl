<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <script src="${base}/resources/wap/2.0/js/iSlider-animate.js"></script>
    <title>${name}</title>
    <style type="text/css">
        body {
            /* background: #eaeaea;*/
        }

        .www {
            position: absolute;
            right: 0;
            bottom: 5px;
            background-color: #eb3341;
            color: #fff;
            padding: 5px 12px;
            border-radius: 5px 0px 0 5px;
        }

        ul.shopItems {
            background-color: #fff;
            border: 1px solid #EAE6E6;
        }

        ul.shopItems li a {
            font-size: 14px;
            display: block;
            padding: 7px;
            text-align: center;
            color: #666;
        }

        a {
            color: #333;
        }

        #md-ul li a.vis {
            color: #ff6d06;
        }

        [data-id="priceDesc"] .vis .priceupdown i:last-child {
            border-top-color: #ff6d06 !important;
        }

        [data-id="priceAsc"] .vis .priceupdown i:first-child {
            border-bottom-color: #ff6d06 !important;
        }

        .shopItems .pri span {
            vertical-align: middle;
        }

        .shopItems span.priceupdown {
            display: inline-block;
            position: relative;
            height: 14px;
            width: 12px;
            padding: 0 5px;
        }

        .shopItems span.priceupdown i {
            position: absolute;
            display: block;
            height: 0;
        }

        .shopItems span.priceupdown i:first-child {
            top: 1px;
            border-bottom: 5px solid #666;
            border-left: 3px solid transparent;
            border-right: 3px solid transparent;
        }

        .shopItems span.priceupdown i:last-child {
            bottom: 1px;
            border-top: 5px solid #666;
            border-left: 3px solid transparent;
            border-right: 3px solid transparent;
        }

        #pullUpLabel {
            width: 100%;
            line-height: 2.5;
            text-align: center;
            background: #eaeaea;
        }

        .am-navbar-nav a {
            color: #666;
        }
    </style>
    <!-- <link rel="stylesheet" href="${base}/resources/wap/2.0/css/tenant_details.css">-->

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-index-item">
        {{#each this}}
        [#include "/wap/include/product_recommend.ftl"/]
        {{/each}}
    </script>

    <script type="text/x-handlebars-template" id="wap-all-item">
        {{#each this}}
        [#include "/wap/include/product.ftl"/]
        {{/each}}
    </script>

    <script type="text/x-handlebars-template" id="wap-promotion-item">
        {{#each this}}
        [#include "/wap/include/product_promotion.ftl"/]
        {{/each}}
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="am-g ">
        <div id="silder" class="silder" class="am-g" style="position:relative;color:white;">
            <img src="${base}/resources/wap/2.0/images/blank.png" alt="load.." style="width:100%;"/>
            <!-- 店铺信息banner展示-->
            <!-- 
            <div style="width:100%;height:150px;padding-top:90px;padding-left:10px;position:absolute;bottom:0;z-index: 300;">
                <div style="display:inline;float:left;">
                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                         data-original="${thumbnail}" alt="load.." style="width:50px;height:50px;border-radius:50%;"/>
                </div>
                <div style="display:inline;float:left;font-size:15px;margin-left:5px;position:relative;margin-top: 5px;">
                    <p>${name}</p>
                    <p>已有<span id="span_favorite_num"> ${favoriteNum}</span>人收藏</p>
                </div>

                <div style="float:right;margin-right:0px;height:30px;margin-top:8px;">
                    <div style="display:inline;float:left;width:60px;background-color:[#if flag=='yes']#dc2727[#elseif flag=='no']#dc2727[/#if];text-align:center;line-height:30px;font-size:16px;"
                         id="save_tenant">[#if flag=='yes']已收藏[#elseif flag=='no']收藏[/#if]
                    </div>
                    <div style="display:inline;float:left;width:60px;background-color:#cf011b;text-align:center;line-height:30px;">
                        <div style="line-height:15px;font-size:12px;" id="div_favorite_num">${favoriteNum}</div>
                        <div style="line-height:15px;font-size:12px;">粉丝</div>
                    </div>
                </div>
            </div>-->
            <!-- store_symbol-info-->
            <div class="weui_cell store_symbol-info TTI">
                <div class="weui_cell_hd hd">
                    <img class="lazy hd-img" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                         data-original="${thumbnail}" alt="load.."/>
                    <img class="lazy hd-img" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                         data-original="${thumbnail}" alt="load.."/>
                </div>
                <div class="weui_cell_bd weui_cell_primary bd">
                    <p>${name}</p>
                    <span style="display:none">已有<b id="span_favorite_num">${favoriteNum}</b>人收藏</span>
                </div>
                <div class="weui_cell_ft cf ft">
                    <div class="fl save_tenant" id="save_tenant">
                    [#if flag=='yes']已收藏[#elseif flag=='no']收藏[/#if]
                    </div>
                    <div class="fl follower">
                    </div>
                </div>
            </div>
            <!-- 店铺半透明遮罩-->
            <div class="store_symbol-mask TTI"></div>
        </div>

        <ul class="am-g am-avg-4 shop-items" id="shopItems" style="font-size:15px;">
            <li data-id="index">
                <a href="javascript:" class="visited">首页</a>
            </li>
            <li id="all" data-id="all">
                <a href="javascript:">全部</a>
            </li>
            <li data-id="new">
                <a href="javascript:">新品</a>
            </li>
            <li data-id="promotion">
                <a href="javascript:">促销</a>
            </li>
        </ul>
    </div>
    <div class="divider"></div>

    <div style="">
        <div id="coupon">
            <div class="am-g" style="font-size: 62.5%;">
                <ul class="am-gallery am-avg-3 am-gallery-overlay shop-tip-items store_couponR TTI">
                [#if coupons??]
                    [#list coupons as coupon]
                        [#if coupon.status =="canUse"]
                            <li class="store_coupon TTI">
                                <a class="am-gallery-item am-gallery-item-default store_coupon-it TTI"
                                   href="${base}/wap/coupon/view.jhtml?id=${coupon.id}&extension=13860431130"
                                   style="color:#E3393C;">
                                    <div class="hd" style="text-align:center;line-height: 2;font-size: 1.15em;">
                                        <span>优惠券</span>
                                    </div>
                                    <div class="bd"
                                         style="text-align:center;height:50%;line-height:1;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;margin-top: -0.2em;">
                                        满<b style="font-size:2.2em;font-weight:normal;vertical-align:text-bottom">${coupon.minimumPrice}</b>减<b
                                            style="font-size:2em;font-weight:normal;vertical-align:text-bottom">${coupon.amount}</b><span
                                            style="vertical-align:text-bottom">&nbsp;</span>
                                    </div>
                                    <div class="ft">
                                        点击领取
                                    </div>
                                </a>
                            </li>
                        [/#if]
                    [/#list]
                [#else]
                    <div class="am-gallery-item am-gallery-item-default"
                         style="height:6.6rem;text-align:center;line-height:6.6rem;font-size:2.0rem;">
                        该店铺还没有添加优惠券！
                    </div>
                [/#if]
                </ul>
            </div>

        [#if isActivityTenant=="yes"]
            <div class="divider"></div>
            <div class="am-g" style="font-size: 62.5%; ">
                <img src="${base}/resources/wap/2.0/images/tenant-activity-banner_03.png">
            </div>
        [/#if]
            <div style="padding:5px 10px;line-height: 2;">
                <div>
                    <span style="padding-left:10px;font-size:18px;display:inline-block;border-left:2px solid #ff6d06;line-height:1;">店长推荐</span>
                </div>
            </div>
            <div style="padding:0 10px;">
                <div style="width:100%;height:1px;border-top:1px solid #E8EAE9;"></div>
            </div>
        </div>

        <div id="view" style="display:none;">
            <div class="am-g" style="padding:10px;" id="order_product">
                <ul class="am-g am-avg-3 shopItems" id="md-ul">
                    <li data-id="weight">
                        <a href="javascript:">综合排序
                            <i class="iconfont" style="font-size:10px;">&#xe664;</i>
                        </a>
                    </li>
                    <li data-id="salesDesc" style="border-left: 1px solid #EAE6E6;border-right: 1px solid #EAE6E6;">
                        <a href="javascript:">销量</a>
                    </li>
                    <li data-id="priceAsc">
                        <a href="javascript:" class="pri">
                            <span>价格</span>
                            <span class="priceupdown"><i></i><i></i></span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <div class="am-g">
            <ul class="am-gallery am-avg-3 am-gallery-overlay shop-tip-items" id="tenant_index">
            </ul>
        </div>
        <div class="am-g">
            <ul class="am-gallery am-avg-2 am-gallery-overlay shop-tip-items" id="tenant_product">
            </ul>
        </div>

        <div id="pullUpLabel"></div>

    </div>
    <!-- 底部版权、技术支持-->
    <footer class="btcopyright">
        <div class="links">
            <a href="${base}/wap/index.jhtml">商城首页</a>
            <a href="javascript:;">分类</a>
            <a href="javascript:;">全部</a>
        </div>
      <!--  <a href="${base}/wap/html/techsupport.html"> -->
        <a>
            <div class="techsupport">
            </div>
        </a>
    </footer>
    <div class="empty-for-fixedbottom_tab" style="height: 5rem;background-color:none;"></div>
    <header class="am-topbar am-topbar-fixed-bottom bg-silver">
        <div data-am-widget="navbar" class="am-navbar am-cf am-navbar-default am-no-layout"
             style="border-top: 1px solid #ddd;" id="footer_b">
            <ul class="am-navbar-nav am-cf am-avg-sm-5 ">
                <li>
                    <a href="${base}/wap/tenant/search/${id}.jhtml">
                        <span class="icon iconfont we-icon-bottom">&#xe640;</span>
                        <span class="am-navbar-label">搜索</span>
                    </a>
                </li>
                <li>
                    <a href="javascript:;" class="popup-selecteditem">
                        <span class="icon iconfont we-icon-bottom">&#xe608;</span>
                        <span class="am-navbar-label">分类</span>
                    </a>
                </li>
                <li>
                    <a href="javascript:;" id="scan_fun">
                        <span class="icon iconfont we-icon-bottom">&#xe684;</span>
                        <span class="am-navbar-label">扫码</span>
                    </a>
                </li>

                <!-- 暂时隐藏-->
                <!-- 
                <li>
                    <a href="javascript:callCustomer();">
                        <span class="icon iconfont we-icon-bottom">&#xe66a;</span>
                        <span class="am-navbar-label">客服</span>
                    </a>
                </li>-->
                <li>
                    <a href="javascript:;" class="store_detail"
                       id="showActionSheet">
                        <span class="icon iconfont we-icon-bottom">&#xe66b;</span>
                        <span class="am-navbar-label">联系商家</span>
                    </a>
                </li>
            [#if union=="true"]
                <li>
                    <a href="${base}/wap/tenant/union/${id}.jhtml">
                        <span class="icon iconfont we-icon-bottom">&#xe633;</span>
                        <span class="am-navbar-label">联盟商家</span>
                    </a>
                </li>
            [/#if]
            </ul>
        </div>
    </header>

    <!-- 店铺详情页的弹框-->
    <div class="store_detail_dialog" id="store_detail_dialog" style="display: none;">
        <!-- <div class="weui_mask store_detail_dialog-mask"></div>-->
        <div class="popupsheet actionsheet_detail" style="right:0">
            <!-- panel头部-->
            <div class="weui_cells sim_panel_box01R">
                <div class="weui_cell sim_panel_box01">
                    <div class="weui_cell_hd sim_panel_box01-hd">
                        <img class="weui_media_appmsg_thumb" src="${thumbnail}" alt="">
                    </div>
                    <div class="weui_cell_bd sim_panel_box01-bd">
                        <h4>${name}</h4>
                        <span>营业时间：9:00—18:00</span>
                        <!--<div>
                            <a href="javascript:;" title="" class="check_licence">查询执照</a>
                        </div>-->
                    </div>
                </div>
            </div>

            <!-- panel内容-->
            <div class="sim_panel_bd">
                <div class="weui_cells weui_cells_access sd_dialog-ctn am-margin-0">
                [#list deliveryCenters.content as deliveryCenter]
                    <a class="weui_cell" href="${base}/wap/tenant/contact/${deliveryCenter.id}.jhtml">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>${deliveryCenter.areaName}</p>
                            <h3>${deliveryCenter.name}</h3>
                            <p>${deliveryCenter.address}</p>
                        </div>
                        [#if deliveryCenter.location??&&deliveryCenter.location?has_content]
                            <div class="weui_cell_ft" data-location-lat="${deliveryCenter.location.lat}"
                                 data-location-lng="${deliveryCenter.location.lng}">
                                <span></span>
                            </div>
                        [/#if]
                    </a>
                [/#list]
                </div>
            </div>
            <!-- discsyms-->
            <div class="weui_cells discsyms">
                <div class="weui_cell" style="background-color: #f4f4f4;">
                    <div class="weui_cell_bd weui_cell_primary">
                        <span><i class="iconfont">
                            &#xe66d;</i> 七天退货</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><i
                            class="iconfont">&#xe66e;</i>担保交易</span>
                    </div>
                    <div class="weui_cell_ft"></div>
                </div>
            </div>

        </div>
    </div>
    <!-- 店铺分类产品的弹框-->
    <div class="popupmenu" style="display:none">
        <ul class="menus">
            <li data-id="all"><a href="javascript:" value="全部">全部</a></li>
        [#list productCategoryTenants as category]
            <li data-id="${category.id}"><a href="javascript:" value="${category.name}">${category.name}</a>
            </li>
        [/#list]
        </ul>
        <span class="arrow"></span>
    </div>

</script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        //特定赋高
        fixedEleCopyHandler(".empty-for-fixedbottom_tab", ".am-topbar-fixed-bottom");
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });

        var ads = [];
    [@ad_position id = 80 tenantId=id count=5]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
                var _url = "";
                [#if ads.linkId??&&ads.linkId?has_content]
                    [#if ads.linkType=="product"]
                        _url = "${base}/wap/product/content/${ads.linkId}/product.jhtml";
                    [#elseif ads.linkType=="tenant"]
                        _url = "${base}/wap/tenant/index/${ads.linkId}.jhtml";
                    [/#if]
                [/#if]
                [#if ads.path?has_content]
                    if (_url == "") {
                        ads.push({content: '${ads.path}'});
                    } else {
                        ads.push({content: '<a href="' + _url + '"><img src="${ads.path}"></a>'});
                    }
                [/#if]
            [/#list]
        [/#if]
    [/@ad_position]
        //
        if (ads.length == 0) {
            ads.push({content: '${base}/resources/wap/2.0/upload/no-store.png'});
        }

        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            duration: 2000,
            animateTime: 2000,
            animateType: 'zoomout'//zoomout,fade
        });
        //
        var follower_ctn = [{
            'content': '<div style="width:60px"><b id="div_favorite_num">${favoriteNum}</b><span>粉丝</span></div>'
        }, {
            'content': '<div style="width:60px"><b id="div_visitor_num">${hits}</b><span>访问</span></div>'
        }];
        //粉丝数和访问量
        var s = new iSlider($(".store_symbol-info .follower").get(0), follower_ctn, {
            isLooping: 1,
            isVertical: 1,
            isOverspread: 0,
            isAutoplay: 1,
            duration: 2500,
            animateTime: 1500,
            animateEasing: "cubic-bezier(0.895, 0.030, 0.685, 0.220)"
        });

        var wapIndexItem = Handlebars.compile($("#wap-index-item").html());//首页标签模板
        var wapAllItem = Handlebars.compile($("#wap-all-item").html());//全部/新品标签模板
        var wapPromotionItem = Handlebars.compile($("#wap-promotion-item").html());//促销标签模板

        var $coupon = $("#coupon");
        var $view = $("#view");
        var $items = '', $pageNum;
        var $is_load, $is_completed;
        var $orderType;
        var $flag_f;
        var $is_load_f, $is_completed_f;
        var $dataId;

        var flag = '${flag}', _type = '${type}';
        /**
         * 点击首页/全部/新品/促销时触发的事件
         */
        $("#shopItems").find("li").on("click", function () {
            $("#shopItems").find("li a").removeClass("visited");
            $(this).find("a").addClass("visited");
            $items = $(this).attr("data-id");
            $orderType = "weight";
            $flag_f = "tenant";
            $pageNum = 1;
            $is_load = "true";
            $is_completed = "false";

            if ($items == 'index') {
                $coupon.show();
                $view.hide();
            } else if ($items == 'all') {
                $view.show();
                $coupon.hide();
                $("#md-ul").find("li").eq(0).find("a").addClass("vis");
                // $("#md-ul").find("li").eq(0).trigger("click");
                // return;
            } else if ($items == 'new') {
                $view.hide();
                $coupon.hide();
            } else if ($items == 'promotion') {
                $view.hide();
                $coupon.hide();

                ajaxGet({
                    url: "${base}/wap/tenant/get_promotion_product/${id}.jhtml",
                    data: {
                        pageSize: 15,
                        pageNumber: $pageNum,
                        type: $items,
                        orderType: 'weight'
                    },
                    success: function (data) {
                        $is_load = "false";
                        if (data == null || data == "" || data.length == 0) {
                            $("#pullUpLabel").html("暂无数据！");
                            $('#tenant_index').html("");
                            $('#tenant_product').html("");
                            $is_completed = "true";
                            return;
                        } else if (data.length < 15) {
                            $is_completed = "true";
                        }

                        $pageNum++;
                        $('#tenant_index').hide();
                        $('#tenant_product').show();
                        $('#tenant_product').html(wapPromotionItem(data));

                        $(".lazy").picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                });

                return;
            }

            ajaxGet({
                url: "${base}/wap/tenant/get_tenant_product/${id}.jhtml",
                data: {
                    pageSize: 15,
                    pageNumber: $pageNum,
                    type: $items,
                    orderType: 'weight'
                },
                success: function (data) {
                    $is_load = "false";
                    if (data == null || data == "" || data.length == 0) {
                        $("#pullUpLabel").html("暂无数据！");
                        $('#tenant_index').html("");
                        $('#tenant_product').html("");
                        $is_completed = "true";
                        return;
                    } else if (data.length < 15) {
                        $is_completed = "true";
                    }

                    $pageNum++;
                    if ($items == 'index') {
                        $('#tenant_product').hide();
                        $('#tenant_index').show();
                        $('#tenant_index').html(wapIndexItem(data));
                    } else if ($items == 'promotion') {
                        $('#tenant_index').hide();
                        $('#tenant_product').show();
                        $('#tenant_product').html(wapPromotionItem(data));
                    } else {
                        $('#tenant_index').hide();
                        $('#tenant_product').show();
                        $('#tenant_product').html(wapAllItem(data));
                    }

                    $(".lazy").picLazyLoad({
                        threshold: 100,
                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                    });

                }
            });
        });

        if (_type == 'qb') {
            $("#shopItems").find("li").eq(1).trigger("click");
        } else if (_type == 'cx') {
            $("#shopItems").find("li").eq(3).trigger("click");
        } else if (_type == 'xp') {
            $("#shopItems").find("li").eq(2).trigger("click");
        } else {
            $("#shopItems").find("li").eq(0).trigger("click");
        }

        scroll(function () {
            if ($flag_f == "tenant") {//这是获取店铺产品的分页加载
                if ($is_load == "false" && $is_completed == "false") {
                    $is_load = "true";
                    $is_completed = "false";
                    ajaxGet({
                        url: "${base}/wap/tenant/get_tenant_product/${id}.jhtml",
                        data: {
                            pageSize: 15,
                            type: $items,
                            pageNumber: $pageNum,
                            orderType: $orderType
                        },
                        success: function (data) {
                            $is_load = "false";
                            if (data.length < 15 && data.length >= 0) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                                $is_completed = "true";
                                setTimeout(function () {
                                    $("#pullUpLabel").html("");
                                }, 2000);
                            }
                            $pageNum++;
                            if ($items == 'index') {
                                $('#tenant_index').append(wapIndexItem(data));
                            } else if ($items == 'promotion') {
                                $('#tenant_product').append(wapPromotionItem(data));
                            } else if ($items == 'all' || $items == 'new') {
                                $('#tenant_product').append(wapAllItem(data));
                            }

                            $(".lazy").picLazyLoad({
                                threshold: 100,
                                placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                            });
                        }
                    });
                }
            } else {//这是获取店铺分类产品的分页加载
                if ($is_load_f == "false" && $is_completed_f == "false") {
                    $is_load_f = "true";
                    $is_completed_f = "false";
                    ajaxGet({
                        url: "${base}/wap/tenant/get_category_product/${id}.jhtml",
                        data: {
                            productCategoryId: $dataId,
                            pageNumber: $pageN,
                            pageSize: 15,
                            orderType: $orderType
                        },
                        success: function (data) {
                            $is_load_f = "false";
                            if (data.length < 15) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                                $is_completed_f = "true";
                                setTimeout(function () {
                                    $("#pullUpLabel").html("");
                                }, 2000);
                            }
                            $('#tenant_product').append(wapAllItem(data));
                            $pageN++;
                            $('.lazy').picLazyLoad({
                                threshold: 100,
                                placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                            });
                        }
                    });
                }
            }

        });
        /**
         * 点击综合排序/销量/价格
         */
        $("#md-ul").find("li").on("click", function () {
            if ($orderType == 'priceAsc') {
                $(this).attr("data-id", 'priceDesc');
            } else if ($orderType == 'priceDesc') {
                $(this).attr("data-id", 'priceAsc');
            }
            $("#md-ul").find("li a").removeClass("vis");
            $(this).find("a").addClass("vis");
            $orderType = $(this).attr("data-id");
            $('#tenant_index').hide();
            $('#tenant_product').show();
            if ($flag_f == "tenant") {
                $pageNum = 1;
                $is_completed = "false";
                $is_load = "true";
                $flag_f = "tenant";
                ajaxGet({
                    url: "${base}/wap/tenant/get_tenant_product/${id}.jhtml",
                    data: {
                        pageSize: 15,
                        type: $items,
                        pageNumber: $pageNum,
                        orderType: $orderType
                    },
                    success: function (data) {
                        $is_load = "false";
                        if (data == null || data.length == 0) {
                            $('#tenant_product').html("暂无数据！");
                            $is_completed = "true";
                            return;
                        } else if (data.length < 15) {
                            $is_completed = "true";
                        }
                        $('#tenant_product').html(wapAllItem(data));
                        $pageNum++;
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });

                    }
                });
            } else {
                var $pageN = 1;
                $is_completed_f = "false";
                $is_load_f = "true";
                $flag_f = "category";
                ajaxGet({
                    url: "${base}/wap/tenant/get_category_product/${id}.jhtml",
                    data: {
                        pageSize: 15,
                        productCategoryId: $dataId,
                        pageNumber: $pageN,
                        orderType: $orderType
                    },
                    success: function (data) {
                        $is_load_f = "false";
                        if (data == null || data.length == 0) {
                            $('#tenant_product').html("暂无数据！");
                            $is_completed_f = "true";
                            return;
                        } else if (data.length < 15) {
                            $is_completed_f = "true";
                        }
                        $('#tenant_product').html(wapAllItem(data));
                        $pageN++;
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });

                    }
                });
            }
        });
        //附加操作
        $("footer.btcopyright .links").on('click', 'a', function (event) {
            event.preventDefault();
            switch ($(this).index()) {
                case 0:
                    location.href = "${base}/wap/index.jhtml";
                    break;
                case 1:
                    $(".popup-selecteditem").trigger('click');
                    break;
                case 2:
                    $(window).scrollTop(0);
                    $("#shopItems").find("li").eq(1).trigger("click");
                    break;
                default:
                    // statements_def
                    break;
            }
        });
        /**
         * 分类事件
         */
        $(".popup-selecteditem").on("click", function (e) {
            MaskMaker.hideAll();
            if ($(".popupmenu").data("posited") == undefined) {
                //定位弹出框
                var offset_popup = $(this).offset().left + $(this).width() / 2;
                $(".popupmenu").css({
                    left: offset_popup + "px"
                });
                $(".popupmenu").data("posited", "Y");
            }
            if ($(".popupmenu").css("display") == "none") {
                $(".popupmenu").show();
                mask_transparent_popupmenu.active(".popupmenu");
                mask_transparent_popupmenu.show();
            } else {
                $(".popupmenu").hide();
                mask_transparent_popupmenu.hide(true);
            }
            $("#store_detail_dialog").hide();
            $(".store_detail_dialog").data("sim_display", "N");
            //mask_halfblack_store_detail_dialog.active(".store_detail_dialog");
            //mask_halfblack_store_detail_dialog.hide(true);
        });
        var mask_transparent_popupmenu = MaskMaker("mask-transparent", ".popupmenu", {
            afterhide: function () {
                //$(".popupmenu").hide();
            }
        });

        /**
         * 选择某个分类事件
         */
        $(".menus").find("li").on("click", function () {
            $("#md-ul").find("li").eq(0).find("a").addClass("vis");
            $flag_f = "category";
            $pageN = 1;
            $dataId = $(this).attr("data-id");
            var $liValue = $(this).find("a").attr("value");
            $(".popupmenu").hide();
            mask_transparent_popupmenu.hide(true);
            $("#all").find("a").html($liValue);
            if ($dataId == 'all') {
                $("#shopItems").find("li").eq(1).trigger("click");
            } else {
                $view.show();
                $coupon.hide();
                $('#tenant_index').hide();
                $('#tenant_product').show();
                $("#shopItems").find("li a").removeClass("visited");
                $("#all").find("a").addClass("visited");
                $is_completed_f = "false";
                $is_load_f = "true";
                $orderType = "weight";
                ajaxGet({
                    url: "${base}/wap/tenant/get_category_product/${id}.jhtml",
                    data: {
                        productCategoryId: $dataId,
                        pageNumber: $pageN,
                        orderType: $orderType,
                        pageSize: 15
                    },
                    success: function (data) {
                        $is_load_f = "false";
                        if (data == null || data.length == 0) {
                            $('#tenant_product').html("暂无数据！");
                            $is_completed_f = "true";
                            return;
                        }
                        if (data.length < 15) {
                            $is_completed_f = "true";
                        }
                        $('#tenant_product').html(wapAllItem(data));
                        $pageN++;
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                });
            }
        });

        /**
         * 收藏
         */
        $("#save_tenant").on("click", function () {
            var memberid = "${memberId}";
            if (memberid == "") {
                showToast2({content: "亲，您还没绑定，暂时不能收藏！"});
                location.href = "${base}/wap/bound/indexNew.jhtml?redirectUrl=${base}/wap/tenant/index/${id}.jhtml";
            } else {
                if (memberid == ${id}) {
                    $("#save_tenant").css("display", "none");
                } else {
                    if (flag == "yes") {
                        ajaxPost({
                            url: "${base}/wap/member/favorite/delete.jhtml",
                            data: {
                                id:${id},
                                type: "1"
                            },
                            success: function (data) {
                                flag = "no";
                                $("#save_tenant").css("background-color", "#dc2727");
                                $("#save_tenant").html('收藏');
                                $("#span_favorite_num").html(data.data);
                                $("#div_favorite_num").html(data.data);
                                showToast({content: data.message.content});
                            }
                        });
                    } else {
                        $("#save_tenant").css("background-color", "#dc2727");
                        ajaxPost({
                            url: "${base}/wap/member/favorite/add.jhtml",
                            data: {
                                id:${id},
                                type: "1"
                            },
                            success: function (data) {
                                flag = "yes";
                                $("#save_tenant").css("background-color", "#dc2727");
                                $("#save_tenant").html('已收藏');
                                $("#span_favorite_num").html(data.data);
                                $("#div_favorite_num").html(data.data);
                                showToast({content: data.message.content});
                            }
                        });
                    }
                }
            }
        });
        /* 扫码*/
        $("#scan_fun").on("click", function () {
            ajaxGet({
                url: "${base}/wap/mutual/get_config.jhtml",
                data: {
                    url: location.href.split('#')[0]
                },
                success: function (message) {
                    if (message.type == "success") {
                        var data = JSON.parse(message.content);
                        wx.config({
                            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                            appId: data.appId, // 必填，公众号的唯一标识
                            timestamp: data.timestamp, // 必填，生成签名的时间戳
                            nonceStr: data.nonceStr, // 必填，生成签名的随机串
                            signature: data.signature,// 必填，签名，见附录1
                            jsApiList: ["scanQRCode"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                        });
                        wx.ready(function () {
                            wx.scanQRCode({
                                needResult: 1,
                                scanType: ["qrCode", "barCode"],
                                success: function (res) {
                                    var result = res.resultStr;
                                    if (result.substr(0, 4) == 'http') {
                                        location.href = result;
                                    } else {
                                        var b = result.split(',');
                                        ajaxGet({
                                            url: "${base}/app/product/barcode.jhtml",
                                            data: {
                                                "tenantId":${id}, "barcode": b[b.length - 1]
                                            },
                                            success: function (data) {
                                                if (data.message.type == "success") {
                                                    if (data.data.length == 0) {
                                                        showDialog2("提示", "此商品没有上架。");
                                                    } else {
                                                        location.href = "${base}/wap/product/content/" + data.data[0].id + "/product.jhtml";
                                                    }
                                                } else {
                                                    showDialog2("提示", data.message.content);
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        });
                        wx.error(function (res) {
                            //config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                        });
                        wx.checkJsApi({
                            jsApiList: ['scanQRCode'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                            success: function (res) {
                                // 以键值对的形式返回，可用的api值true，不可用为false
                                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                            }
                        });
                    } else {
                        showDialog2("提示", message.content);
                    }
                }
            });
        });
        /* 联系商家*/
        $(".store_detail").on('click', function (event) {
            MaskMaker.hideAll();
            event.preventDefault();
            //$(this).data("soga", $(".store_detail_dialog").data("sim_display") == undefined)
            //$(".store_detail_dialog")
            if ($(".store_detail_dialog").data("sim_display") == undefined || $(".store_detail_dialog").data("sim_display") == "N") {
                $(".store_detail_dialog").show();
                mask_halfblack_store_detail_dialog.active(".store_detail_dialog");
                mask_halfblack_store_detail_dialog.show();
                //$(".sd_dialog-ctn").data("height-setted", "Y");
                //高度未设置 一次性操作
                if ($(".sim_panel_bd").data("height-setted") == undefined || $(".sim_panel_bd").data("height-setted") == "N") {
                    //设置高度
                    var sim_panel_bd_height = 0;
                    var limited_hight = 2;
                    var cell_length = $(".sd_dialog-ctn>.weui_cell").length;
                    if (cell_length > limited_hight) {
                        //获取前两个weui-cell的高度
                        for (var index = 0; index < limited_hight; index++) {
                            sim_panel_bd_height = sim_panel_bd_height + $(".sd_dialog-ctn>.weui_cell").eq(index).height();
                        }
                        $(".sim_panel_bd").height(sim_panel_bd_height).css({
                            overflowY: "scroll",
                            overflowX: "hidden"
                        });
                        //console.log("the sim_panel_bd_height is");
                        //console.log(sim_panel_bd_height);
                    }
                    $(".sim_panel_bd").data("height-setted", "Y");

                }
                /* 
                $(".store_detail_dialog-mask").one('click', function(event) {
                    $(".store_detail_dialog").hide();
                    $(".store_detail_dialog").data("sim_display", "N");
                });*/
                //console.log("cell_length");
                //console.log(cell_length);
                $(".store_detail_dialog").data("sim_display", "Y");
            } else {
                $(".store_detail_dialog").hide();
                mask_halfblack_store_detail_dialog.hide(true);
                $(".store_detail_dialog").data("sim_display", "N");
            }
            $(".popupmenu").hide();
            //mask_transparent_popupmenu.active(".popupmenu");
            //mask_transparent_popupmenu.hide(true);
        });
        var mask_halfblack_store_detail_dialog = MaskMaker("mask-halfblack", ".store_detail_dialog", {
            afterhide: function () {
                //$(".store_detail_dialog").hide();
                $(".store_detail_dialog").data("sim_display", "N");
            }
        });
        //mask_halfblack_store_detail_dialog.active(".store_detail_dialog");
    });
    function callCustomer() {
        location.href = "tel:${telephone}";
    }

    function addCart(id) {
        ajaxPost({
            url: "${base}/app/b2b/cart/add.jhtml",
            data: {
                id: id,
                type: 'gouwuc',
                quantity: 1
            },
            success: function (data) {
                if (data.message.type == 'success') {
                    showToast({content: '抢购成功，请前往购物车结账'});
                } else {
                    showToast2({content: data.message.content});
                }
            }
        });
    }

</script>
<!-- 百度JavaScript api-->
<script src="http://api.map.baidu.com/api?v=2.0&ak=11gjCy4d5FwxG72gv7V1XRalDak4ablq" type="text/javascript"></script>
<script type="text/javascript">
    //百度ak
    var baidu_ak = "11gjCy4d5FwxG72gv7V1XRalDak4ablq";
    //当前用户所处位置(名称) 手动默认设置
    var locationname_now = {
        addressname: "蜀山森林公园",
        cityname: "合肥"
    };
    //当前用户所处位置(坐标点)
    var locationpoint_now = {};
    var locationpoints = [];
    var locationnames = [];
    /**
     * 地图回调
     * 返回该点到基准点的距离
     * @param  {[type]} pointdata [description]
     * @return {[type]}           [description]
     */
    function jsonpGetDistanceToO(pointdata) {
        //console.log(pointdata);
        var distance_to_O = calculateDistance(locationpoint_now, pointdata.result);
        //console.log("distance_to_O is");
        var distance_to_O_fixed = (distance_to_O / 1000).toFixed(1);
        //console.log(distance_to_O_fixed + "km");
    }
    /**
     * 地图回调
     * 获得当前所处位置的坐标点
     * @return {[type]} [description]
     */
    function jsonpGetLocationpointNow(pointdata) {
        locationpoint_now = pointdata.result;
        //console.log(locationpoint_now);
        //return pointdata.result;
    }
    /**
     * 生成编码之后的baidu url请求字串
     * @param  {[type]} baidu_ak     [description]
     * @param  {[type]} locationname [description]
     * @return {[type]}              [description]
     */
    function createBaiduGEOURI(baidu_ak, locationname, callbackname) {
        var baidugeo_url = "http://api.map.baidu.com/geocoder/v2/?ak=" + baidu_ak + "&callback=" + callbackname + "&output=json&address=" + locationname.addressname + "&city=" + locationname.cityname;
        return encodeURI(baidugeo_url);
    }
    /**
     * 位置计算器
     * @param  {[type]} ) {                                   var $maplittlebox [description]
     * @return {[type]}   [description]
     */
    var calculateDistance = (function () {
        //构造mapbox
        var $maplittlebox = $("<div class='maplittlebox'></div>").css({
            width: '0px',
            height: '0px'
        });
        $maplittlebox.appendTo('body');
        //创建地图实例  
        var map = new BMap.Map($maplittlebox.get(0));
        //function main
        function main(pointO, pointP) {
            //创建基准点
            var mappointO = new BMap.Point(pointO.location.lng, pointO.location.lat);
            //创建比较点
            var mappointP = new BMap.Point(pointP.location.lng, pointP.location.lat);
            //返回距离
            //console.log("the distance is");
            //console.log(map.getDistance(mappointO, mappointP));
            return map.getDistance(mappointO, mappointP);
        }

        return main;
    })();

    $(function () {
        //如果已经通过jssdk被初始化
        if ($.isPlainObject(locationpoint_now)) {

        }
        //联系商家
        $(".store_detail").on("click", function () {
            //一次性操作
            if ($(".sd_dialog-ctn").data("location-setted") == undefined || $(".sd_dialog-ctn").data("location-setted") == "N") {
                //获取距离
                ajaxGet({
                    url: "${base}/wap/mutual/get_config.jhtml",
                    data: {
                        url: location.href.split('#')[0]
                    },
                    success: function (message) {
                        if (message.type == "success") {
                            var data = JSON.parse(message.content);
                            //console.log("the data is");
                            //console.log(data);
                            wx.config({
                                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                                appId: data.appId, // 必填，公众号的唯一标识
                                timestamp: data.timestamp, // 必填，生成签名的时间戳
                                nonceStr: data.nonceStr, // 必填，生成签名的随机串
                                signature: data.signature,// 必填，签名，见附录1
                                jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                            });
                            wx.ready(function () {
                                /* 
                                $("<div id='printout'>你好</div>").css({
                                    position:"fixed",
                                    left:"50%",
                                    top:"50%",
                                    zIndex:"1000"
                                }).appendTo('body');*/
                                wx.getLocation({
                                    type: 'gcj02',
                                    success: function (res) {
                                        var latitude = res.latitude;
                                        var longitude = res.longitude;
                                        var speed = res.speed;
                                        var accuracy = res.accuracy;
                                        //微信获得的火星坐标
                                        locationpoint_now = {
                                            location: {
                                                lat: res.latitude,
                                                lng: res.longitude
                                            }
                                        };
                                        //暂时不用，获得对应的百度坐标
                                        ajaxGetBDCoordinate(res.latitude, res.longitude, {
                                            success: function (data) {
                                                //do something
                                            },
                                            error: function () {
                                                //do something
                                            }
                                        });
                                        directCalc();
                                        /* 
                                        $("<div id='printout2'>位置:"+locationpoint_now.location.lat+","+locationpoint_now.location.lng+"</div>").css({
                                            position:"fixed",
                                            left:"50%",
                                            top:"50%",
                                            zIndex:"1000"
                                        }).appendTo('body');
                                        */
                                    },
                                    fail: function () {
                                        //如果调取位置失败,使用默认基准点位置
                                        //获得当前位置坐标
                                        /* 
                                        loadScript(createBaiduGEOURI(baidu_ak, locationname_now, "jsonpGetLocationpointNow"), function() {
                                        });*/
                                        //调取位置失败就不显示位置了
                                        return;
                                        //directCalc();
                                    }
                                });

                            });
                            wx.error(function (res) {
                                //config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                            });
                            wx.checkJsApi({
                                jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                                success: function (res) {
                                    // 以键值对的形式返回，可用的api值true，不可用为false
                                    // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                                    /* 
                                    if(res.checkResult){
                                        $("#printout").text(res.checkResult.getLocation);
                                    }*/
                                }
                            });
                        } else {
                            showDialog2("提示", message.content);
                        }
                    }
                });
                //如果存在当前坐标信息，则直接进行计算距离，计算之后直接写入html
                function directCalc() {
                    $(".sd_dialog-ctn a.weui_cell").find(".weui_cell_ft").each(function (index) {
                        //当店铺存在经纬度数据，计算距离赋值到元素内容
                        if ($(this).data("location-lat") && $(this).data("location-lng")) {
                            var inner_distance = calculateDistance(locationpoint_now, {
                                location: {
                                    lat: $(this).data("location-lat"),
                                    lng: $(this).data("location-lng")
                                }
                            });
                            $(this).find("span").text((inner_distance / 1000).toFixed(1) + "km");
                        }
                    });
                    $(".sd_dialog-ctn").data("location-setted", "Y");
                    return;
                }

                //获得对应的baidu坐标
                //当前无法跨域到baidu，目前转百度坐标系暂未实现
                function ajaxGetBDCoordinate(lat, lng, callbacks) {
                    $.ajax({
                        type: 'GET',
                        url: "http://api.map.baidu.com/geoconv/v1/?coords=" + lng + "," + lat + "&ak=" + baidu_ak + "&from=3",
                        // type of data we are expecting in return:
                        dataType: 'json',
                        success: function (data) {
                            callbacks.success(data);
                        },
                        error: function (xhr, type) {
                            console.log("get baidu Coordinate error");
                            callbacks.error(xhr, type);
                        }
                    });
                }

                //以下功能暂时不需要
                //根据位置名称查询具体坐标，再计算距离
                /* 
                $(".sd_dialog-ctn a.weui_cell").find(".weui_cell_primary").each(function(index) {
                    locationnames.push({
                        addressname:$(this).data("addressname"),
                        cityname:$(this).data("cityname")
                    });
                });*/
                //遍历
                /* 
                for (var i = locationnames.length - 1; i >= 0; i--) {
                    loadScript(createBaiduGEOURI(baidu_ak, locationnames[i], "jsonpGetDistanceToO"));
                }*/
                //获得对应的百度坐标系
                /* 
                ajaxGetBDCoordinate(31.9853860086, 117.3317273443, {
                    success: function (data) {
                        console.log("ok");
                        console.log("the data.result.x's value is:");
                        console.log(data.result.x);
                    },
                    error: function () {
                    }
                });*/
                //set zepto data to lock state
                $(".sd_dialog-ctn").data("location-setted", "Y");
            }
            //店铺分店具体地址名称(必须符合百度地图搜索精准名称)
            /* 
            var locationnames = [{
                addressname: "中绿广场",
                cityname: "合肥"
            }, {
                addressname: "合肥包河万达广场",
                cityname: "合肥"
            }];*/
        });

    });
</script>
</body>
</html>