<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>${name}</title>


    <style type="text/css">
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
            line-height: 30px;
            text-align: center;
        }

        .am-navbar-nav a {
            color: #666;
        }
    </style>
    <!-- 
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/tenant_details.css">-->

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
                    <!-- div class="fl save_tenant" id="save_tenant">
                    [#if flag=='yes']已收藏[#elseif flag=='no']收藏[/#if]
                    </div -->
                    <div class="fl follower">
                        <b id="div_favorite_num">${favoriteNum}</b>
                        <span>粉丝</span>
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

    <div style="background-color: white">
        <div id="coupon">
            <div class="am-g" style="font-size: 62.5%;">
                <ul class="am-gallery am-avg-3 am-gallery-overlay shop-tip-items">
                [#if coupons??]
                    [#list coupons as coupon]
                        [#if coupon.status =="canUse"]
                            <li>
                                <div class="am-gallery-item am-gallery-item-default" style="">
                                    <a href="${base}/wap/coupon/view.jhtml?id=${coupon.id}&extension=13860431130"
                                       style="color:#E3393C;">
                                        <img src="${base}/resources/wap/2.0/images/promotion.png" alt="load.."
                                             style="height:6.6rem;position:relative;"/>
                                        <div style="position:absolute;top:0.9rem;left:0.2rem;width:100%;">
                                            <div style="display:inline;float:left;line-height:3.2rem;"><span
                                                    style="font-size:1rem;">￥</span></div>
                                            <div style="display:inline;float:left;line-height:3.2rem;font-size:2.4rem;">
                                                <span>${coupon.amount}</span></div>
                                            <div style="display:inline;float:left;height:3.2rem;margin-left:5px;padding: 0.5rem 0;">
                                                <div style="font-size:1rem;line-height:1.2rem;">优惠券</div>
                                                <div style="font-size:1rem;line-height:1.2rem;">满${coupon.minimumPrice}元使用</div>
                                            </div>
                                        </div>
                                    </a>
                                </div>
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
            <div style="padding:5px 10px;height:35px;">
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
                    <li data-id="weight"><a href="javascript:">综合排序<i class="iconfont" style="font-size:10px;">
                        &#xe664;</i></a></li>
                    <li data-id="salesDesc" style="border-left: 1px solid #EAE6E6;border-right: 1px solid #EAE6E6;"><a
                            href="javascript:">销量</a></li>
                    <li data-id="priceAsc"><a href="javascript:" class="pri"><span>价格</span><span
                            class="priceupdown"><i></i><i></i></span></a></li>
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
</script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });

        var ads = [];
        var i = 1;
    [@ad_position id = 80 tenantId=id count=5]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
                [#if ads.path?has_content]
                    ads.push({content: '${ads.path}'});
                [#else ]
                    ads.push({content: '${base}/resources/wap/2.0/upload/no-' + i + '.png'});
                    i++;
                [/#if]
            [/#list]
        [/#if]
    [/@ad_position]

        if (ads.length == 0) {
            ads.push({content: '${base}/resources/wap/2.0/upload/no-1.png'});
            ads.push({content: '${base}/resources/wap/2.0/upload/no-2.png'});
            ads.push({content: '${base}/resources/wap/2.0/upload/no-3.png'});
        }

        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 800,
            animateType: 'rotate'
        });

        var wapIndexItem = Handlebars.compile($("#wap-index-item").html());//首页标签模板
        var wapAllItem = Handlebars.compile($("#wap-all-item").html());//全部/新品标签模板
        var wapPromotionItem = Handlebars.compile($("#wap-promotion-item").html());//促销标签模板

        var $coupon = $("#coupon");
        var $view = $("#view");
        var $items = '', $pageNum = 1;

        var flag = '${flag}',$orderType='weight';
        /**
         * 点击首页/全部/新品/促销时触发的事件
         */
        $("#shopItems").find("li").on("click", function () {
            $pageNum = 1;
            $("#shopItems").find("li a").removeClass("visited");
            $(this).find("a").addClass("visited");

            $items = $(this).attr("data-id");
            if ($items == 'index') {
                $coupon.show();
                $view.hide();
            } else if ($items == 'all') {
                $view.show();
                $coupon.hide();
                $("#md-ul").find("li").eq(0).trigger("click");
                return;
            } else if ($items == 'new') {
                $view.hide();
                $coupon.hide();
            } else if ($items == 'promotion') {
                $view.hide();
                $coupon.hide();
            }

            ajaxGet({
                url: "${base}/wap/tenant/get_tenant_product/${id}.jhtml",
                data: {
                    pageSize: 15,
                    pageNumber: $pageNum,
                    type: $items,
                    orderType: $orderType
                },
                success: function (data) {
                    $pageNum++;

                    if (data == null || data.length == 0) {
                        $("#pullUpLabel").html("暂无数据！");
                        $('#tenant_index').html("");
                        $('#tenant_product').html("");
                        return;
                    }
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
                    $("#pullUpLabel").html("上拉刷新！");
                    if (data.length <= 15 && data.length > 0) {
                        $("#pullUpLabel").html("亲，到底了！！！");
                    }

                    $(".lazy").picLazyLoad({
                        threshold: 100,
                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                    });

                    $("a").attr("href", "javascript:;");
                }
            });
        });
        $("#shopItems").find("li").eq(0).trigger("click");

        //$(window).unbind("scroll");
        scroll(function () {
            ajaxGet({
                url: "${base}/wap/tenant/get_tenant_product/${id}.jhtml",
                data: {
                    pageSize: 15,
                    type: $items,
                    pageNumber: $pageNum,
                    orderType: $orderType
                },
                success: function (data) {
                    $pageNum++;

                    if (data == null || data.length == 0) {
                        $("#pullUpLabel").html("亲，已经到底了！！！");
                        return;
                    }
                    if ($items == 'index') {
                        $('#tenant_index').append(wapIndexItem(data));
                    } else if ($items == 'promotion') {
                        $('#tenant_product').append(wapPromotionItem(data));
                    } else if ($items == 'all' || $items == 'new') {
                        $('#tenant_product').append(wapAllItem(data));
                    }
                    $("#pullUpLabel").html("上拉刷新！");
                    if (data.length <= 15 && data.length > 0) {
                        $("#pullUpLabel").html("亲，到底了！！！");
                    }
                    $(".lazy").picLazyLoad({
                        threshold: 100,
                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                    });

                    $("a").attr("href", "javascript:;");
                }
            });

        });
        /**
         * 点击综合排序/销量/价格
         */
        $("#md-ul").find("li").on("click", function () {
            $pageNum = 1;
            $("#md-ul").find("li a").removeClass("vis");
            $(this).find("a").addClass("vis");
            $orderType = $(this).attr("data-id");

            ajaxGet({
                url: "${base}/wap/tenant/get_tenant_product/${id}.jhtml",
                data: {
                    pageSize: 15,
                    type: $items,
                    pageNumber: $pageNum,
                    orderType: $orderType
                },
                success: function (data) {
                    $pageNum++;
                    $('#tenant_index').hide();
                    $('#tenant_product').show();
                    if (data == null || data.length == 0) {
                        $('#tenant_product').html("暂无数据！");
                    } else {
                        $('#tenant_product').html(wapAllItem(data));
                    }
                    $('.lazy').picLazyLoad({
                        threshold: 100,
                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                    });

                    $("a").attr("href", "javascript:;");
                }
            });

            if ($orderType == 'priceAsc') {
                $(this).attr("data-id", 'priceDesc');
            } else if ($orderType == 'priceDesc') {
                $(this).attr("data-id", 'priceAsc');
            }
        });

    });
</script>
</body>
</html>