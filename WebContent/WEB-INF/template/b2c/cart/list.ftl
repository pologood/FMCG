<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-购物车</title>
    <meta name="keywords" content="${setting.siteName}-购物车"/>
    <meta name="description" content="${setting.siteName}-购物车"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon">
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2c/include/topnav.ftl"]

    <div class="topBanner">
        <div class="container">
            <!--a href="javascript:;">
        		<img src="${base}/resources/b2c/images/J_banner_01.jpg" alt="" />
        	</a -->
        [#--[@ad_position id=97 areaId=area.id count=1/]--]
        </div>
    </div>
    <!--logo 搜索开始-->
[#include "/b2c/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--guide 导向开始-->
    <div class="container">
        <div class="guide">
            <div class="guideProcess">
                <div class="guideProcessAll processOne on">
                    <div class="title"><h1>1</h1></div>
                    <span>购物车</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processTwo">
                    <div class="title"><h1>2</h1></div>
                    <span>确认订单</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processThree">
                    <div class="title"><h1>3</h1></div>
                    <span>支付</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processFour">
                    <div class="title"><h1>√</h1></div>
                    <span>完成</span>
                    <div class="processBar"></div>
                </div>
            </div>
            <div class="line"></div>
        </div>
    </div>
    <!--guide 导向结束-->
</div>
<!--头部end-->
<!--我的购物车内容start-->
<div class="container">
    <!--购物车商品确认start-->
    <div class="cart-layout">
        <div class="cart">
            <div class="cart-main">
                <div class="cart-table-th">

                [#if cart?? && cart.cartItems?has_content]
                    <div>
                        <div class="th th-chk">
                            <div class="select-all">
                                <input type="checkbox" name="checkAll">
                                &nbsp;&nbsp;全选
                            </div>
                        </div>
                        <div class="th th-item">
                            <div class="td-inner">商品信息</div>
                        </div>
                        <div class="th th-info">
                            <div class="td-inner">&nbsp;</div>
                        </div>
                        <div class="th th-price">
                            <div class="td-inner">单价（元）</div>
                        </div>
                        <div class="th th-amount">
                            <div class="td-inner">数量</div>
                        </div>
                        <div class="th th-sum">
                            <div class="td-inner">小计（元）</div>
                        </div>
                        <div class="th th-op">
                            <div class="td-inner">操作</div>
                        </div>
                    </div>
                [/#if]
                </div>
                <div class="cart-order-list">
                [#if cart?? && cart.cartItems?has_content]
                    [#list cart.tenants as tenant]
                        <div style="height: auto;position: relative;" class="cart_tenant">
                            [#if member??]
                                [#assign isShow=false]
                                [#assign hasNew=false]
                                [#assign getCount=0]
                            [#--优惠券start--]
                                <div class="ks-overlay cart-tips  coupon-popup display-n"
                                     style="top: 40px; left: 240px;">
                                    <a class="ks-overlay-close" href="javascript:void('close')" role="button">

                                    </a>
                                    <div class="ks-overlay-content">

                                        <div class="coupon-list coupon-list-hidden">
                                            <div style="position: relative; overflow-x: hidden; width: 340px; height: 195px; outline: medium none;"
                                                 class="cart-container">
                                                <ul style="position: absolute; top: 0px; left: 0px; width: 100%; height: auto; overflow: visible;">
                                                    [#list tenant.coupons as coupon]
                                                        [#if coupon.status=='canUse'||coupon.status=='unUsed']
                                                            [#assign isShow=true]
                                                            <li class="coupon-youhuijuan">
                                                                <div class="coupon-amount">
                                                                    <span class="rmb">¥</span>
                                                                ${coupon.amount}
                                                                </div>
                                                                <div class="coupon-detail">
                                                                    <div class="coupon-info">
                                                                        <p class="coupon-title">${coupon.introduction}
                                                                            满${coupon.minimumPrice}减${coupon.amount}</p>
                                                                        <p class="coupon-time">${coupon.startDate?string("yyyy.MM.dd")}
                                                                            -${coupon.endDate?string("yyyy.MM.dd")}</p>
                                                                    </div>
                                                                    <div class="coupon-op">
                                                                        [#assign couponGetCount=0]
                                                                        [#list coupon.couponCodes as couponCode]
                                                                            [#if couponCode.member==member]
                                                                                [#assign couponGetCount=couponGetCount+1 /][#--该优惠券领取数量--]
                                                                                [#assign getCount=getCount+1 /][#--总领取数量--]
                                                                            [/#if]
                                                                        [/#list]
                                                                        [#if couponGetCount gt 0&&couponGetCount gte coupon.receiveTimes]
                                                                            <span class="coupon-unreceived">已领取</span>
                                                                        [#else]
                                                                            [#assign hasNew=true]
                                                                            <span class="coupon-unreceived"
                                                                                  onclick="buildCoupon(${coupon.id})">领 取</span>
                                                                        [/#if]
                                                                    </div>
                                                                </div>
                                                            </li>
                                                        [/#if]
                                                    [/#list]
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="coupon-summary">
                                            <span class="icon-announcement"></span>
                                            已领取<em>${getCount}</em>张优惠券，
                                            [#if hasNew==true]
                                                有新优惠券可领取
                                            [#else]
                                                暂无新优惠券可领取
                                            [/#if]
                                        </div>
                                        <span class="arrow"></span>
                                    </div>
                                </div>
                            [#--优惠券end--]
                            [/#if]
                            <div class="clearfix cart-order-body">
                                <div class="cart-shop clearfix">
                                    <div class="cart-shop-info">
                                        <input type="checkbox" name="check_tenant" tenant_id="${tenant.id}">
                                        [#if tenant.noReason==true]
                                            <img class="shop-icon"
                                                 src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" alt="">
                                        [/#if]
                                        店铺：<a href="${base}/b2c/tenant/index.jhtml?id=${tenant.id}" target="_blank"
                                              title="${tenant.name}">${(tenant.name)}</a>
                                        <!-- <a title="" class="marking-v" href="javascript:;" target="_blank">
                                            <span>V${tenant.score}${abbreviate(tenant.name,20,"...")}</span>
                                        </a> -->
                                        [#if member??]
                                            [#if isShow==true]
                                                <span class="shop-coupon-trigger">
                                                    <em>优惠券</em>
                                                    <span class="arrow"></span>
                                                </span>
                                            [/#if]
                                        [/#if]
                                    </div>
                                </div>
                                [#list cart.cartItems as cartItem]
                                    [#if cartItem.product.tenantId==tenant.id]
                                        <div class="cart-order-content">
                                            <div class="item-body clearfix">
                                                <ul class="item-content clearfix">
                                                    <li class="td-chk">
                                                        <div class="td-inner">
                                                            <div class=" " style="margin:0 10px 0 0;float: right;">
                                                                <input name="check_product" tenant_id="${tenant.id}"
                                                                       cartId="${cartItem.id}" [#if cartItem.selected]
                                                                       checked="true" [/#if] type="checkbox">
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li class="td-item">
                                                        <div class="td-inner">
                                                            <div class="item-pic">
                                                                <a href="${base}/b2c/product/detail/${cartItem.product.id}.jhtml" target="_blank">
                                                                    <img src="${cartItem.product.thumbnail}">
                                                                </a>
                                                            </div>
                                                            <div class="item-info">
                                                                <div class="item-basic-info">
                                                                    <a href="${base}/b2c/product/detail/${cartItem.product.id}.jhtml"
                                                                       title="${cartItem.product.fullName}"
                                                                       class="item-title">
                                                                    ${abbreviate(cartItem.product.fullName,40,"...")}
                                                                    </a>
                                                                </div>
                                                                <div class="item-other-info">
                                                                    <div class="promo-logos"></div>
                                                                    <div class="item-icon-list clearfix">
                                                                        <div class="item-icons">
                                                                    <span class="item-icon" title="支持信用卡支付">
                                                                        <img src="//assets.alicdn.com/sys/common/icon/trade/xcard.png"
                                                                             alt="">
                                                                    </span>
                                                                            [#if tenant.noReason==true]
                                                                                <a href="javascript:;" target="_blank"
                                                                                   class="item-icon"
                                                                                   title="消费者保障服务，卖家承诺7天退换">
                                                                                    <img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png"
                                                                                         alt="七天退换">
                                                                                </a>
                                                                            [/#if]
                                                                            [#if tenant.tamPo==true]
                                                                                <a href="javascript:;" target="_blank"
                                                                                   class="item-icon"
                                                                                   title="消费者保障服务，卖家承诺如实描述">
                                                                                    <img src="//img.alicdn.com/tps/i4/T1BCidFrNlXXaSQP_X-16-16.png"
                                                                                         alt="如实描述">
                                                                                </a>
                                                                            [/#if]
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li style="padding: 20px 20px;width: 172px;">
                                                        <div>
                                                            [#list cartItem.product.specificationValues as specificationValue]
                                                                <p class="sku-line">${specificationValue.specification.name}
                                                                    ：${specificationValue.name}</p>
                                                            [/#list]
                                                        </div>
                                                    </li>
                                                    <li class="td-price">
                                                        <div class="td-inner">
                                                            <div class="price-content">
                                                                <div class="price-line">
                                                                    <em class="price-original">￥${cartItem.product.marketPrice?string("0.00")}</em>
                                                                </div>
                                                                <div class="price-line">
                                                                    <em class="price-now">￥${cartItem.effectivePrice?string("0.00")}</em>
                                                                </div>
                                                            </div>
                                                            [#if cartItem.product.promotionProducts?has_content]
                                                                <div class="promo-main">
                                                                    [#list cartItem.product.promotionProducts as promotionProduct]
                                                                        <div class="promo-content">
                                                                            <span>${(promotionProduct.promotion.name)!}</span>
                                                                            <span class="arrow"></span>
                                                                        </div>
                                                                        [#if promotionProduct_has_next]
                                                                            <div style="height: 5px;"></div>
                                                                        [/#if]
                                                                    [/#list]
                                                                </div>

                                                            [/#if]
                                                        </div>
                                                    </li>
                                                    <li class="td-amount">
                                                        <div class="td-inner">
                                                            <div class="[#if (cartItem.isLowStock==true&&cartItem.quantity==1)||cartItem.product.isLimit==true]amount-has-error[/#if]">
                                                                <div class="item-amount ">
                                                                    <a href="javascript:;"
                                                                       class="[#if cartItem.isLowStock==true]no-minus[#else]minus[/#if]">-</a>
                                                                    <input onkeydown="old(this)"
                                                                           oninput="modifyQuantity(this)"
                                                                           value="${cartItem.quantity}"
                                                                           class="text text-amount" autocomplete="off"
                                                                           type="text" maxlength="5"
                                                                           cartItemId="${cartItem.id}">
                                                                    <a href="javascript:;"
                                                                       class="[#if cartItem.isLowStock==true]no-plus[#else]plus[/#if]">+</a>
                                                                </div>
                                                                <div class="amount-msg">
                                                                    [#if cartItem.product.isLimit==true]
                                                                        <em title="该商品限购${cartItem.product.limitCounts}件哦，要买的赶紧啊">限购${cartItem.product.limitCounts}件</em>
                                                                    [/#if]
                                                                    [#if cartItem.isLowStock==true]
                                                                        <em title="该商品只有1件了哦，要买的赶紧啊">库存紧张</em>
                                                                    [/#if]
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li class="td-sum">
                                                        <div class="td-inner">
                                                            <em class="subtotal">${cartItem.effectiveAmount?string("0.00")}</em>
                                                            <div></div>
                                                        </div>
                                                    </li>
                                                    <li class="td-op">
                                                        <div class="td-inner">
                                                            <a title="移入收藏夹" href="javascript:;"
                                                               onclick="addFavorite('${cartItem.product.id}');">
                                                                [#assign level='移入']
                                                                [#if member??&&member?has_content]
                                                                    [#if member.favoriteProducts??&&member.favoriteProducts?has_content]
                                                                        [#list member.favoriteProducts as product]
                                                                            [#if product.id==cartItem.product.id][#assign level='移出'][/#if]
                                                                        [/#list]
                                                                    [/#if]
                                                                [/#if]
                                                                ${level}收藏夹
                                                            </a>
                                                            <a href="javascript:;" name="del" id="${cartItem.id}">删除</a>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    [/#if]
                                [/#list]
                            </div>
                        </div>
                    [/#list]
                [#else]
                    <div style="text-align: center;padding: 50px 0;font-size: large;">
                        <div>购物车快饿瘪了, <a href="${base}/b2c/index.jhtml" style="color: rgb(255,68,0);">去逛逛</a></div>
                    </div>
                [/#if]
                </div>
            </div>
            <div class="float-bar-holder">
                <div class="float-bar clearfix">
                    <div class="float-bar-wrapper">
                    [#if cart?? && cart.cartItems?has_content]
                        <div class="select-all">
                            <input type="checkbox" name="checkAll">&nbsp;全选
                        </div>
                        <div class="operations">
                            <a href="javascript:deleteSelect();" hidefocus="true">删除</a>
                        </div>
                    [/#if]
                        <div class="float-bar-right content">
                            <div class="btn-area">
                                <a href="javascript:submit();" class="submit-btn">
                                    <span>结&nbsp;算</span>
                                    <b></b>
                                </a>
                            </div>
                            <div class="price-sum">
                                <span class="txt">总价（不含运费）:&nbsp;</span>
                                <span class="price">¥<em
                                        id="effectivePrice">[#if cart??]${cart.effectivePrice?string("0.00")}[#else]
                                    0.00[/#if]</em></span>
                                <br/>
                                <span class="txt">已节省：¥ <em
                                        id="discount">[#if cart??]${cart.discount?string("0.00")}[#else]0.00[/#if]</em></span>
                                <span class="txt">&nbsp;;&nbsp;运费：¥ <em
                                        id="freight">[#if cart??]${cart.freight?string("0.00")}[#else]
                                    0.00[/#if]</em></span>
                            </div>
                            <div class="pipe"></div>
                            <div class="check-cod">
                                <span class="icon-cod"></span>
                                <span class="s-checkbox"></span>
                                货到付款
                            </div>
                            <div class="amount-sum">
                                <span>已选商品</span>
                                <em id="effectiveQuantity">[#if cart??]${cart.effectiveQuantity}[#else]0[/#if]</em>
                                <span>件</span>
                            [#--<div class="arrow-box">--]
                            [#--<span class="arrow"></span>--]
                            [#--</div>--]
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!--可能感兴趣 -->
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
</div>

<script type="text/javascript">
    $(function () {
        /*-------shoppingCart优惠劵点击事件--------*/
        $('.shop-coupon-trigger').click(function () {
            var $this = $(this);
            var $coupon = $this.parents(".cart_tenant").find(".ks-overlay");
            if ($this.find("span").attr("class") == "arrow") {
                $this.find("span").addClass("on");
                $coupon.show();
            } else {
                $this.find("span").attr("class", "arrow");
                $coupon.hide();
            }
        });
        $('.ks-overlay-close').click(function () {
            $(this).parent().hide();
            $(this).parents(".cart_tenant").find(".shop-coupon-trigger span").removeClass("on");
        });

    [#if member??]
        $.each($(".ks-overlay"), function () {
            var $this = $(this);
            $this.find(".coupon-list").before($this.find(".coupon-summary"));
        });
    [/#if]

        init_check();

        $("input[name='check_tenant']").change(function () {
            $("input[type='checkbox']").attr("disabled", true);
            var $tenant = $(this);
            $.each($("input[name='check_product']"), function () {
                var $product = $(this);
                if ($tenant.prop("checked") && $product.attr("tenant_id") == $tenant.attr("tenant_id")) {
                    $product.prop("checked", true);
                }
                if (!$tenant.prop("checked") && $product.attr("tenant_id") == $tenant.attr("tenant_id")) {
                    $product.prop("checked", false);
                }
            });
            init_check();
            selected();
        });

        $("input[name='check_product']").change(function () {
            $("input[type='checkbox']").attr("disabled", true);
            init_check();
            selected();
        });

        $("input[name='checkAll']").change(function () {
            $("input[type='checkbox']").attr("disabled", true);
            if ($(this).prop("checked")) {
                $("input[type='checkbox']").prop("checked", true);
            } else {
                $("input[type='checkbox']").prop("checked", false);
            }
            selected();
        });

        $(".plus").click(function () {
            var $quantity = $(this).prev();
            if ($quantity.val() == "") {
                $quantity.val(1);
            } else {
                $quantity.val(Number($quantity.val()) + 1);
            }
            edit($quantity);
        });

        $(".minus,.no-minus").click(function () {
            var $quantity = $(this).next();
            if ($quantity.val() == 1) {
                return;
            }
            if ($quantity.val() > 1) {
                $quantity.val(Number($quantity.val()) - 1);
            } else {
                $quantity.val(1);
            }
            edit($quantity);
        });

        $("[name='del']").click(function () {
            var iid = $(this).attr("id");
            if (confirm("确认删除该商品吗")) {
                $.ajax({
                    url: '${base}/wap/cart/del.jhtml',
                    data: {
                        ids: iid
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        $.message(data.message);
                        if (data.message.type == "success") {
                            window.setTimeout(function () {
                                location.reload();
                            }, 600);
                        }
                    }
                });
            }
        });

    });

    function init_check() {
        if ($("input[name='check_product']").size() == 0) {
            return;
        }
        var check_all = true;
        $.each($("input[name='check_tenant']"), function () {
            var $tenant = $(this);
            var check_tenant = true;
            $.each($("input[name='check_product']"), function () {
                var $product = $(this);
                if ($product.attr("tenant_id") == $tenant.attr("tenant_id") && $product.prop("checked") == false) {
                    check_tenant = false;
                    check_all = false;
                }
            });
            $tenant.prop("checked", check_tenant);
        });
        $("input[name='checkAll']").prop("checked", check_all);
    }

    function selected() {
        if ($("input[name='check_product']").size() == 0) {
            $("input[type='checkbox']").removeAttr("disabled");
            return;
        }
        var cartIds = [];
        $.each($("input[name='check_product']"), function () {
            var $this = $(this);
            if ($this.prop("checked")) {
                cartIds.push($this.attr("cartId"));
            }
        });
        $.ajax({
            url: "${base}/wap/cart/selected.jhtml",
            data: {ids: cartIds.join(",")},
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.message.type == "success") {
                    $("#effectivePrice").text(data.effectivePrice);
                    $("#discount").text(data.discount);
                    $("#freight").text(data.freight);
                    $("#effectiveQuantity").text(data.effectiveQuantity);
                    $("input[type='checkbox']").removeAttr("disabled");
                } else {
                    $.message(data.message);
                    window.setTimeout(function () {
                        location.reload(true);
                    }, 600)
                }
            }
        });
    }

    function edit($quantity) {
        var quantity = $quantity.val();
        var packagUnitId = $quantity.attr("packagUnitId");
        var cartItemId = $quantity.attr("cartItemId");
        if (/^\d*[1-9]\d*$/.test(quantity)) {
            $.ajax({
                url: "${base}/wap/cart/edit.jhtml",
                data: {id: cartItemId, quantity: quantity, packagUnitId: packagUnitId},
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    if (data.message.type == "success") {
                        $("#effectivePrice").text(data.effectivePrice);
                        $("#discount").text(data.discount);
                        $("#freight").text(data.freight);
                        $("#effectiveQuantity").text(data.effectiveQuantity);
                        $quantity.parents(".cart-order-content").find(".subtotal").text(data.subtotal);
                    } else {
                        $.message(data.message);
                        window.setTimeout(function () {
                            location.reload(true);
                        }, 600)
                    }
                }
            });
        }
    }

    //删除选中商品
    function deleteSelect() {
        var ids = [];
        $.each($("input[name='check_product']:checked"), function () {
            ids.push($(this).attr("cartId"));
        });
        if (confirm("确认删除选中的商品吗？")) {
            $.ajax({
                traditional: true,
                url: '${base}/wap/cart/del.jhtml',
                data: {
                    ids: ids
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    $.message(data.message);
                    if (data.message.type == "success") {
                        window.setTimeout(function () {
                            location.reload();
                        }, 600);
                    }
                }
            });
        }
    }

    //添加收藏
    function addFavorite(id) {
        if ("${member}" == "") {
            location.href = "${base}/b2c/login.jhtml";
        } else {
            var favoriteProducts = [];
        [#if member??]
        [#list  member.favoriteProducts as product]
            favoriteProducts.push('${product.id}');
        [/#list]
        [/#if]
            var _favorite = 0;

            if (favoriteProducts.length > 0) {
                for (var i = 0; i < favoriteProducts.length; i++) {
                    if (id == favoriteProducts[i]) {
                        _favorite = 1;
                    }
                }
            }

            if (_favorite == 0) {
                $.ajax({
                    url: "${base}/b2c/member/favorite/add.jhtml",
                    type: "POST",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            //$(obj).html(message.content);
                            $.message(message);
                            location.reload();
                        }

                    }
                });
            } else if (_favorite == 1) {
                $.ajax({
                    url: "${base}/b2c/member/favorite/delete.jhtml",
                    type: "POST",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            //$(obj).removeClass(message.content);
                            $.message(message);
                            location.reload();
                        }

                    }
                });
            }
        }
    }

    function buildCoupon(id) {
        $.ajax({
            url: "${base}/b2c/member/coupon_code/build.jhtml?id=" + id,
            type: "POST",
            dataType: "json",
            success: function (data) {
                $.message(data);
                window.setTimeout(function () {
                    location.reload();
                }, 600);
            }
        });
    }

    function modifyQuantity(o) {
        var old = $(o).attr("old");
        var val = $(o).val().replace(/[^\d]/g, '');
        if (val == 0 || val.trim() == "") {
            $(o).val("");
            return;
        }
        $(o).val(Number(val));
        if ($(o).val() != old) {
            edit($(o));
        }
    }

    function old(o) {
        $(o).attr("old", $(o).val());
    }

    function submit() {
        var check = false;
        $.each($("input[name='check_product']"), function () {
            if ($(this).prop("checked")) {
                check = true;
            }
        });
        if (!check) {
            $.message('warn', '请选择需要购买的商品');
            return;
        }
        window.location.href = '${base}/b2c/member/order/order_submit.jhtml';
    }
</script>
<!--标语 -->
[#include "/b2c/include/slogen.ftl"]
<!--底部 -->
[#include "/b2c/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>