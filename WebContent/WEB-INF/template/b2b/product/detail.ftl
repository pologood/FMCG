<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-商品详情</title>
    <meta name="keywords" content="${setting.siteName}-商品详情"/>
    <meta name="description" content="${setting.siteName}-商品详情"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="${base}/resources/b2b/css/v2.0/detail.css" type="text/css">
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <link href="${base}/resources/b2b/css/jqzoom.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/products.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/data/data.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.jqzoom.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <style type="text/css">
        .save_product {
            background: url(${base}/resources/b2b/images/icon02.png) -40px -6px no-repeat;
            position: absolute;
            width: 16px;
            height: 16px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2b/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--横幅开始-->
[#include "/b2b/include/topbanner.ftl"]
    <!--横幅结束-->
    <!--logo 搜索开始-->
[#include "/b2b/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--主导航开始-->
[#include "/b2b/include/topmainnav.ftl"]
    <!--主导航结束-->
</div>
<!--头部end-->
<script type="text/javascript">
    var $zoom = $("#zoom");
    var flag = "no";
    /*所有商品分类*/
    $('.menuNav'[#if versionType==0]+ 'Tiaohuo'[/#if]).css('display', 'none');
    $('.allCategorys').hover(function () {
        $('.menuNav'[#if versionType==0]+ 'Tiaohuo'[/#if]).css('display', 'block');
    }, function () {
        $('.menuNav'[#if versionType==0]+ 'Tiaohuo'[/#if]).css('display', 'none');
    });

    $('.menuNav' +[#if versionType==0]'Tiaohuo'+[/#if] ' ul>li').hover(function () {
        var eq = $('.menuNav' +[#if versionType==0]'Tiaohuo'+[/#if] ' ul>li').index(this),//获取当前滑过是第几个元素
                h = $('.menuNav'[#if versionType==0]+ 'Tiaohuo'[/#if]).offset().top,//获取当前下拉菜单距离窗口多少像素
                s = $(window).scrollTop(),//获取游览器滚动了多少高度
                i = $(this).offset().top,//当前元素滑过距离窗口多少像素
                item = $(this).children('.menuNavList').height(),//下拉菜单子类内容容器的高度
                sort = $('.menuNav'[#if versionType==0]+ 'Tiaohuo'[/#if]).height();//父类分类列表容器的高度

        if (item < sort) {//如果子类的高度小于父类的高度
            if (eq == 0) {
                $(this).children('.menuNavList').css('top', (i - h));
            } else {
                $(this).children('.menuNavList').css('top', (i - h) + 1);
            }
        } else {
            if (s > h) {//判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
                if (i - s > 0) {//则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                    $(this).children('.menuNavList').css('top', (s - h) + 0);
                } else {
                    $(this).children('.menuNavList').css('top', (s - h) - (-(i - s)) + 0);
                }
            } else {
                $(this).children('.menuNavList').css('top', 0);
            }
        }

        $(this).addClass('now');
        $(this).children('.menuNavList').css('display', 'block');
    }, function () {
        $(this).removeClass('now');
        $(this).children('.menuNavList').css('display', 'none');
    });

    //    $('.item > .item-list > .close').click(function(){
    //        $(this).parent().parent().removeClass('hover');
    //        $(this).parent().hide();
    //    });
    $(function () {
        //控制收藏图标的颜色切换
        $("#shoucang").find("i").mouseover(function () {
            $(this).css("background-position", "-64px -6px");
        });
        $("#shoucang").find("i").mouseout(function () {
            $(this).css("background-position", "-40px -6px");
        });

    [#if member??]
        [#list member.favoriteProducts as products]
            [#if product.id==products.id]
                flag = "yes";
                $("#shoucang").find("span").text("已收藏：");
                $("#shoucang").find("i").css("background-position", "-64px -6px");
            [/#if]
        [/#list]
    [/#if]
        var productMap = {};
        var $specification = $("#specification p");
        var $specificationValue = $("#specification a");

        //二维码
        $.ajax({
            url: "http://api.wwei.cn/wwei.html?apikey=20160806215020&data=${base}/b2b/product/detail/${product.id}.jhtml",
            type: 'post',
            dataType: "jsonp",
            jsonp: "callback",
            success: function (result) {
                $("#product_qr").attr("src", result.data.qr_filepath);
            }
        });
        //商品图片放大镜
        $("#zoom").jqzoom({
            zoomWidth: 368,
            zoomHeight: 368,
            title: false,
            showPreload: false,
            preloadImages: false

        });
        // 商品缩略图滚动
        $("#scrollable").scrollable();

        $("#thumbnail").find("img").hover(function () {
            var $this = $(this);
            if ($this.hasClass("img-hover")) {
                return false;
            } else {
                $("#thumbnail").find("img").removeClass("img-hover");
                $this.addClass("img-hover").click();
            }
        });
        // 浏览记录
        var historyProduct = getCookie("historyProduct");
        var historyProductIds = historyProduct != null ? historyProduct.split(",") : new Array();
        for (var i = 0; i < historyProductIds.length; i++) {
            if (historyProductIds[i] == "${product.id}") {
                historyProductIds.splice(i, 1);
                break;
            }
        }
        historyProductIds.unshift("${product.id}");
        if (historyProductIds.length > 6) {
            historyProductIds.pop();
        }
        addCookie("historyProduct", historyProductIds.join(","), {path: "${base}/"});
        $.ajax({
            url: "${base}/b2b/product/history.jhtml",
            type: "GET",
            data: {ids: historyProductIds},
            dataType: "json",
            traditional: true,
            cache: false,
            success: function (data) {
                if (data.length == 0) {
                    $("#history").text("暂无数据");
                }
                for (var i = 0; i < data.length; i++) {
                    $("#history").append(
                            "<li>" +
                            "<div class='img'>" +
                            "<a href='${base}/b2b/product/detail/" + data[i].id + ".jhtml' title=''>" +
                            "<img src='" + data[i].image + "' alt='' width='70px' height='70px'>" +
                            "</a>" +
                            "</div>" +
                            "<div class='price'>￥" + data[i].price + "</div>" +
                            "</li>"
                    );
                }
            }
        });

    [@compress single_line = true]
        productMap[${product.id}] = {
            id: null,
            specificationValues: [
                [#list product.specificationValues as specificationValue]
                    "${specificationValue.id}"[#if specificationValue_has_next],[/#if]
                [/#list]]
        };
        [#list product.siblings as product]
            productMap[${product.id}] = {
                id: "${product.id}",
                specificationValues: [
                    [#list product.specificationValues as specificationValue]
                        "${specificationValue.id}"[#if specificationValue_has_next],[/#if]
                    [/#list]]
            };
        [/#list]
    [/@compress]
        // 锁定规格值
        function lockSpecificationValue() {
            var selectedIds = new Array();
            $specificationValue.filter(".hover").each(function (i) {
                selectedIds[i] = $(this).attr("val");
            });
            $specification.each(function () {
                var $this = $(this);
                var selectedId = $this.find("a.hover").attr("val");
                var otherIds = $.grep(selectedIds, function (n, i) {
                    return n != selectedId;
                });
                $this.find("a").each(function () {
                    var $this = $(this);
                    otherIds.push($this.attr("val"));
                    var locked = true;
                    $.each(productMap, function (i, product) {
                        //console.log(product.specificationValues);
                        // console.log(otherIds);
                        // console.log(contains(product.specificationValues, otherIds));
                        if (contains(product.specificationValues, otherIds)) {

                            locked = false;
                            return false;
                        }
                    });
                    if (locked) {
                        $this.addClass("locked");
                    } else {
                        $this.removeClass("locked");
                    }
                    otherIds.pop();
                });
            });
        }

        // 锁定规格值
        lockSpecificationValue();
        // 规格值选择
        $specificationValue.click(function () {
            var $this = $(this);
            if ($this.hasClass("locked")) {
                return false;
            }
            $this.toggleClass("hover").siblings("a").removeClass("hover");
            var selectedIds = new Array();
            $specificationValue.filter(".hover").each(function (i) {
                selectedIds[i] = $(this).attr("val");
            });
            var locked = true;
            $.each(productMap, function (i, product) {
                if (product.specificationValues.length == selectedIds.length && contains(product.specificationValues, selectedIds)) {
                    if (product.id != null) {
                        location.href = "${base}/b2b/product/detail/" + product.id + ".jhtml";
                        locked = false;
                    }
                    return false;
                }
            });
            if (locked) {
                lockSpecificationValue();
            }
            return false;
        });
        //立即购买
        $("#go_pay").click(function () {
        [#if product.specifications?has_content]
            var specificationValueIds = new Array();
            $specificationValue.filter(".hover").each(function (i) {
                specificationValueIds[i] = $(this).attr("val");
            });
            if (specificationValueIds.length != ${product.specificationValues?size}) {
                $specificationTitle.show();
                return false;
            }
        [/#if]
            if(${product.availableStock}<$("#number").val()||${product.availableStock}<=0){
                $.message("warn","库存不足");
                return;
            }
            $.ajax({
                url: "${base}/b2b/cart/add.jhtml",
                type: "POST",
                data: {id: ${product.id}, quantity: $("#number").val(), type: "buy"},
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.type == "success") {
                        $.message("success", "正在帮您跳转...");
                        location.href = "${base}/b2b/member/order/order_submit.jhtml";
                    } else {
                        if (message.content == "请先登录") {
                            location.href = "${base}/b2b/login.jhtml?redirectUrl=${base}/b2b/product/detail/${product.id}.jhtml";
                        }
                        $.message(message);
                    }
                }
            });
        });
        //添加到购物车
        $("#addcart").click(function () {
        [#if product.specifications?has_content]
            var specificationValueIds = new Array();
            $specificationValue.filter(".hover").each(function (i) {
                specificationValueIds[i] = $(this).attr("val");
            });
            if (specificationValueIds.length != ${product.specificationValues?size}) {
                $specificationTitle.show();
                return false;
            }
        [/#if]
            if(${product.availableStock}<$("#number").val()){
                $.message("warn","库存不足");
                return;
            }
            $.ajax({
                url: "${base}/b2b/cart/add.jhtml",
                type: "POST",
                data: {id:${product.id}, quantity: $("#number").val()},
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.type == "success") {
                        $.message("success", "添加购物车成功");
                        refreshCartCount();
                    } else {
                        $.message(message);
                    }
                }
            });
        });
        $(".shop-collect em").click(function () {
            var $this = $(this);
            var memberId = "${(member.id)!}";
            if (memberId == "") {
                location.href = "${base}/b2b/login.jhtml?redirectUrl=${base}/b2b/tenant/index.jhtml?id=${tenant.id}";
                return;
            }
            var collected = $this.attr("collected");
            if (collected == "true") {
                $.ajax({
                    type: 'post',
                    url: "${base}/wap/member/favorite/delete.jhtml",
                    data: {
                        id:${tenant.id},
                        type: "1"
                    },
                    dataType: 'json',
                    success: function (data) {
                        $.message(data.message);
                        if (data.message.type == "success") {
                            $this.css("color", "grey");
                            $this.attr("collected", "false");
                        }
                    }
                });
            } else {
                $.ajax({
                    type: 'post',
                    url: "${base}/wap/member/favorite/add.jhtml",
                    data: {
                        id:${tenant.id},
                        type: "1"
                    },
                    dataType: 'json',
                    success: function (data) {
                        $.message(data.message);
                        if (data.message.type == "success") {
                            $this.css("color", "#ee724d");
                            $this.attr("collected", "true");
                        }
                    }
                });
            }
        });
    });
    // 判断是否包含
    function contains(array, values) {
        var contains = true;
        for (var ii in values) {
            if ($.inArray(values[ii], array) < 0) {
                contains = false;
                break;
            }
        }
        return contains;
    }
    //商品数量减
    function minus() {
        var product_number = parseInt($('#number').val());
        if (product_number > 1) {
            product_number--;
            $("#number").val(product_number);
        }
    }
    //商品数量加

    var _availableStock = '${product.availableStock}';
    function plus() {
        var product_number = parseInt($('#number').val());
        if (_availableStock != '') {

            if (_availableStock == '0') {
                $.message("warn", "不好意思，库存不足，您可以先收藏");
                return;
            }
            if (product_number < _availableStock) {
                product_number++;
                $("#number").val(product_number);
            }
        } else {
            $.message("warn", "不好意思，库存不足，您可以先收藏");
        }

    }
    //收藏商品/取消收藏
    function addFavorite(id, obj) {
        if ("${member}" == "") {
            location.href = "${base}/b2b/login.jhtml?redirectUrl=${base}/b2b/product/detail/${product.id}.jhtml";
        } else {
            if (flag == "yes") {
                $.ajax({
                    url: "${base}/b2b/member/favorite/delete.jhtml",
                    type: "POST",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            $(obj).css("background-position", "-40px -6px");
                            document.location.reload();
                        }
                        $.message(message);

                    }
                });
            } else if (flag == "no") {
                $.ajax({
                    url: "${base}/b2b/member/favorite/add.jhtml",
                    type: "POST",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.type == "success") {
                            $("#shoucang").find("span").text("已收藏：");
                            $(obj).css("background-position", "-64px -6px");
                            location.reload();
                        }
                        $.message(message);
                    }
                });
            }
        }
    }
    function get_detail_commont(obj, flag) {
        $(obj).addClass("curr");
        $(obj).siblings().removeClass("curr");
        if (flag == "commont") {
            $("#commont_content").show();
            $(".pro-detail-con").hide();
        } else if (flag == "introduct") {
            $("#commont_content").hide();
            $(".pro-detail-con").show();
        }
    }


</script>
<!--主页内容区start-->
<div class="paper pc-bg">
    <div class="container">
        <div class="breadcrumb">
            <h2><a href="${base}/b2b/index.jhtml">首页</a></h2>
            <span>
                &nbsp;&gt;&nbsp;
            [#if product.productCategory??]
                [#if product.productCategory.parent??]
                    [#if product.productCategory.parent.parent??]
                        <a href="${base}/b2b/product/list/${(productCategory.parent.parent.id)!}.jhtml">${productCategory.parent.parent.name}</a>
                            &nbsp;&gt;&nbsp;
                    [/#if]
                    <a href="${base}/b2b/product/list/${(productCategory.parent.id)!}.jhtml">${productCategory.parent.name}</a>
                        &nbsp;&gt;&nbsp;
                [/#if]
                <a href="${base}/b2b/product/list/${(productCategory.id)!}.jhtml" class="on">${productCategory.name}</a>
                &nbsp;&gt;&nbsp;
            [/#if]

            </span>
            <span>
                <a href="javascript:;">${product.fullName}</a>
            </span>
        </div>
    </div>
    <div class="container">
        <div class="product-intro f-left clearfix">
            <div class="preview">
            [#if product.productImages?has_content]
                <div class="spec-n1">
                    <a id="zoom" href="${product.productImages[0].large}" rel="gallery">
                        <img width="350" height="350" src="${product.productImages[0].medium}" alt="">
                    </a>
                </div>
            [#else]
                <div class="spec-n1">
                    <a href="javascript:" rel="gallery">
                        <img width="350" height="350" src="${base}/resources/b2b/img/AccountBitmap-product.png" alt="">
                    </a>
                </div>
            [/#if]
                <div class="spec-list">
                    <a href="javascript:;" class="spec-forward spec-control prev disabled"></a>
                    <a href="javascript:;" class="spec-backward spec-control next"></a>
                    <div class="spec-items" id="scrollable">
                        <ul id="thumbnail">
                        [#if product.productImages?has_content]
                            [#list product.productImages as images]
                                <li>
                                    <a href="javascript:;"
                                       rel="{gallery: 'gallery', smallimage: '${images.medium}', largeimage: '${images.large}'}">
                                        [#if images.thumbnail?has_content]
                                            <img src="${images.thumbnail}" title="${images.title}"
                                                 [#if images_index == 0]class="img-hover" [/#if]/>
                                        [/#if]
                                    </a>
                                </li>
                            [/#list]
                        [#else]
                            <li>
                                <img alt="" src="${base}/resources/b2b/img/AccountBitmap-product.png" width="50"
                                     height="50">
                            </li>
                        [/#if]
                        </ul>
                    </div>

                </div>
                <div class="short-share">
                    <span>商品编号：</span>
                    <span>${product.sn}</span>
                </div>
                <div style="margin-left: 35px;margin-top: 10px;position:relative;" id="shoucang"><span>收藏：</span>
                    <i onclick="addFavorite(${product.id},this)" class="save_product"></i>
                </div>
            </div>
            <div class="item-inner">
                <div class="item-info">
                    <div class="name">
                        <h1>${product.fullName}</h1>
                    </div>
                    <div class="price">
                    [#if member??]
                        <div class="agora-price">
                            <span>建议零售价：</span>
                            <span class="p-price">￥${product.marketPrice}</span>
                        </div>
                        <div class="sale-price">
                            <span>批发价：</span>
                            <span class="p-price" id="sale_price">￥${price}</span>
                        </div>
                    [#else]
                        <div class="sale-price">
                            <span>建议零售价：</span>
                            <span class="p-price">￥${product.marketPrice}</span>
                        </div>
                    [/#if]
                    [#if product.validPromotions??&&product.validPromotions?has_content]
                        <div class="summary-top">
                            <div class="summary-promotion">
                                <div class="dt">促销信息：</div>
                                <div class="dd">
                                    <div class="promotions">
                                        [#list product.validPromotions as promotions]
                                            <div class="include-post">
                                                <em class="red-bg">${promotions.name}</em>
                                                <!-- <span class="color">${promotions.title}</span> -->
                                            </div>
                                        [/#list]
                                    </div>
                                </div>
                            </div>
                        </div>
                    [#else]
                        <div class="dd" style="height:10px;"></div>
                    [/#if]
                        <!-- <div class="summary-stock">
                            <span class="dt">配送区域：</span>
                            <span class="dd">安庆</span>
                            <span class="dd">合肥</span>
                        </div> -->
                    </div>
                    <div class="clearfix choose-wrap s_detailr">
                    [#if product.specifications?has_content]
                        <div id="specification">
                            [#assign specificationValues = product.goods.specificationValues /]
                            [#list product.specifications as specification]
                                <p style="padding:10px 0px;">
                                    <span style="color:#666666;">${abbreviate(specification.name, 8)}：</span>
                                    [#list specification.specificationValues as specificationValue]
                                        [#if specificationValues?seq_contains(specificationValue)]
                                            <a href="javascript:;"
                                               class="[#if product.specificationValues?seq_contains(specificationValue)]hover[/#if]"
                                               val="${specificationValue.id}" style="display:inline-block;">
                                                <span title="${message("shop.product.selected")}" class="ttt">
                                                    [#if specification.type == "text"]
                                                    ${specificationValue.name}
                                                    [#else]
                                                        <img src="${specificationValue.image}"
                                                             alt="${specificationValue.name}"
                                                             title="${specificationValue.name}" height="26px"
                                                             width="26px"/>
                                                    [/#if]
                                                </span>
                                            </a>
                                        [/#if]
                                    [/#list]
                                </p>
                            [/#list]
                        </div>
                    [/#if]
                        <div class="choose-btns li">
                            <div style="float:left;line-height:35px;margin-right:10px;">数量：</div>
                            <div class="choose-amount f-left">
                                <div class="f-left" id="left_minus" onclick="minus()">-</div>
                                <input class="f-left" name="number" type="text" id="number" value="1" size="4">
                                <div class="f-left" id="right_plus" onclick="plus()">+</div>
                            </div>
                            <div style="float:left;line-height:35px;margin-left:12px;">${product.unit}
                                &nbsp;&nbsp;(库存：${product.availableStock})
                            </div>
                        </div>
                        <div class="choose-btns li">
                            <div class="choose-btn-append" id="go_pay">
                                <a href="javascript:;">立即购买</a>
                            </div>
                            <div class="choose-btn-append" id="addcart">
                                <a href="javascript:;">加入购物车</a>
                            </div>
                        </div>
                        <div class="li summary-tips" style="padding:5px 0px;">
                            <div style="float:left;line-height:40px;margin-right:10px;">承诺：</div>
                            <div>
                            [#if tenant.noReason=="true"]
                                <div class="item">
                                    <a href="javascript:;" title="消费者保障服务，卖家承诺7天退换">
                                        <img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" width="16"
                                             height="16" alt="七天退换">
                                        <span>七天退货</span>
                                    </a>
                                </div>
                            [/#if]
                            [#if tenant.toPay=="true"]
                                <div class="item">
                                    <a href="javascript:;" title="消费者保障服务，卖家承诺货到付款">
                                        <img src="${base}/resources/b2b/images/Hdfk-16x16.png" width="16"
                                             height="16" alt="货到付款">
                                        <span>货到付款</span>
                                    </a>
                                </div>
                            [/#if]
                            [#if tenant.tomPo=="true"]
                                <div class="item">
                                    <a href="javascript:;" title="消费者保障服务，卖家承诺物流退货">
                                        <img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" width="16"
                                             height="16" alt="物流退货">
                                        <span>物流退货</span>
                                    </a>
                                </div>
                            [/#if]
                            </div>
                        </div>
                        <div style="float:left;">
                            支付方式：&nbsp;&nbsp;<span>货到付款</span>&nbsp;&nbsp;<span>在线支付</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="f-right merchant-info">
            <h1 style="padding: 5px 5px;"><a href="javascript:;">${tenant.name}</a></h1> 
            <div class="wire"></div>
            <span>综合评分</span>
        [#if tenant.score=="0"]
            <span class="font"><em class="color"></em>★★★★★</span>
        [#elseif tenant.score=="1"]
            <span class="font"><em class="color">★</em>★★★★</span>
        [#elseif tenant.score=="2"]
            <span class="font"><em class="color">★★</em>★★★</span>
        [#elseif tenant.score=="3"]
            <span class="font"><em class="color">★★★</em>★★</span>
        [#elseif tenant.score=="4"]
            <span class="font"><em class="color">★★★★</em>★</span>
        [#elseif tenant.score=="5"]
            <span class="font"><em class="color">★★★★★</em></span>
        [/#if]
            <span>联系电话：<em>${tenant.telephone}</em></span>
            <!-- <span>联系档口：<i>${tenant.qq}</i></span> -->
            <!-- <div class="wire"></div>
            <span>配送方式：<em></em></span>
            <span>起 配 额：<em class="color"></em></span>
            <span>配送区域：<em>${tenant.area}</em></span> -->
            <span>所 在 地：<em>${tenant.address}</em></span>
            <div class="wire"></div>
        [#if setting.customservice?exists]
            [#list setting.customservice?split(",") as custom]
                <span>联系客服${custom_index+1}：
                    <em>
                        <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${custom}&site=qq&menu=yes"><img
                                border="0" src="http://wpa.qq.com/pa?p=2:${custom}:52" alt="聚德汇超市订货网欢迎您的入住"
                                title="聚德汇超市订货网欢迎您的入住"/></a>
                    </em>
                </span>
            [/#list]
        [/#if]
            [#if versionType==0]
            <div class="in_conllect">
                <span class="line-right">
                <!-- <a href="${base}/b2b/tenant/index.jhtml?id=${(tenant.id)!}">进入店铺</a> -->
                <a href="javascript:;">进入店铺</a>
                    </span>
                <span class="line-right">
                    <a class="shop-collect" href="javascript:;">
                        <em collected="${collected}" [#if collected==false]style="color: grey;" [/#if]>★</em>
                        <span onclick="javascript:$(this).prev().click();">收藏店铺</span>
                    </a>
                </span>
            </div>
            [/#if]
        </div>

    </div>

    <div class="container">
        <div class="left">
            <!--div class="goods-qr-code">
                <div class="dt">货品二维码</div>
                <div class="dd">
                    <img src="" width="150" height="150" id="product_qr">
                </div>
            </div>
            <!-- <div class="product-category">
                <div class="dt">产品分类</div>
                <div class="dd">
                    <div class="nav">
                        <span>按新品</span>
                        <span>按价格</span>
                        <span>按销量</span>
                        <span>按人气</span>
                    </div>
                    <div class="search">
                        <input id="" value="" placeholder="搜索档口内货品" type="text">
                        <button type="button" value="">搜索</button>
                    </div>
                    <div class="all-items">
                        <span><i></i>全部货品</span>
                        [#list tenant.productCategoryTenants as category]
                        <span><i></i>${category.name}</span>
                        [/#list]
                    </div>
                </div>
            </div> -->
            <div class="sales-rankings">
                <div class="dt">销量排行</div>
                <div class="dd">
                [@product_list tenantId=product.tenant.id count = 3 orderBy="sales desc"]
                    [#list products as product]
                        <a class="li" href="${base}/b2b/product/detail/${product.id}.jhtml">
                            <i class="color">${product_index+1}</i>
                            <img src="${product.image}" width="48" height="48"/>
                            <div>
                                <div>${product.name}</div>
                                <span class="color">￥${product.price}</span>
                            </div>
                        </a>
                    [/#list]
                [/@product_list]
                </div>
            </div>
            <div class="hot-products">
                <div class="dt">热销产品</div>
                <div class="dd">
                [@product_list tenantId=product.tenant.id count = 6 orderBy="monthSales desc"]
                    [#list products as product]
                        <a class="li" href="${base}/b2b/product/detail/${product.id}.jhtml">
                            <img src="${product.image}" width="120" height="120">
                            <div style="text-align: center">${product.name}</div>
                            <span class="color">￥${product.price}</span>
                        </a>
                    [/#list]
                [/@product_list]
                </div>
            </div>
        </div>
        <div class="content">
            <div class="product-detail">
                <div class="pro-detail-hd">
                    <div class="trigger-wrap clearfix">
                        <ul>

                            <li class="trig-item curr" onclick="get_detail_commont(this,'introduct')">
                                <a href="javascript:;">商品介绍</a>
                            </li>

                            <li class="trig-item" onclick="get_detail_commont(this,'commont')">
                                <a href="javascript:;">商品评论
                                    <span class="color">（${product.reviews?size}）</span>
                                </a>
                            </li>
                            <!-- <li class="trig-item">
                                <a href="javascript:;">商品咨询
                                    <span class="color">（${product.consultations?size}）</span>
                                </a>
                            </li>
                            <li class="trig-item"><a href="javascript:;">售后保障</a></li> -->
                        </ul>
                    </div>
                </div>
                <div class="pro-detail-con">
                    <div class="parameter">
                    [#if product.parameterValue?has_content]
                        <ul class="parameter-list">
                            [#list productCategory.parameterGroups as parameterGroups]
                                [#list parameterGroups.parameters as parameters]
                                    [#if product.parameterValue.get(parameters)??]
                                        <li title="">${parameters.name}
                                            &nbsp;:&nbsp;<span
                                                    title="${product.parameterValue.get(parameters)}">${product.parameterValue.get(parameters)}</span>
                                        </li>
                                    [/#if]
                                [/#list]
                            [/#list]
                        </ul>
                    [/#if]
                    </div>
                [#if product.introduction?has_content]
                    <div class="detail-content clearfix">
                        <div class="detail-content-wrap">
                            <div class="title">
                                <span>商品详情</span>
                            </div>
                            <div class="detail-correction">
                                <b></b>如果您发现商品信息不准确，<a href="" target="_blank">欢迎纠错</a>
                            </div>
                            <div class="detail-content-item">
                            ${product.introduction}
                            </div>
                        </div>
                    </div>
                [/#if]
                </div>
            </div>
            <div id="commont_content" style="display:none;">
                <div class="comment">
                    <div class="dt">
                        <h2>商品评价</h2>
                    </div>
                    <div class="dd">
                        <div class="rate">
                            <strong>[#if product.reviews?size==0]
                                0[#else]${(highScore?size/product.reviews?size*100)?string('0.00')}[/#if]
                                <span>%</span></strong>
                            <br>
                            <span>好评度</span>
                        </div>
                        <div class="percent">
                            <dl>
                                <dt>好评<span>([#if product.reviews?size==0]
                                    0[#else]${(highScore?size/product.reviews?size*100)?string('0.00')}[/#if]%)</span>
                                </dt>
                                <dd>
                                    <div class="good"
                                         style="width:[#if product.reviews?size==0]0[#else]${highScore?size/product.reviews?size*100}[/#if]%;"></div>
                                </dd>
                            </dl>
                            <dl>
                                <dt>中评<span>([#if product.reviews?size==0]
                                    0[#else]${(middleScore?size/product.reviews?size*100)?string('0.00')}[/#if]%)</span>
                                </dt>
                                <dd>
                                    <div class="medium"
                                         style="width:[#if product.reviews?size==0]0[#else]${middleScore?size/product.reviews?size*100}[/#if]%;"></div>
                                </dd>
                            </dl>
                            <dl>
                                <dt>差评<span>([#if product.reviews?size==0]
                                    0[#else]${(lowScore?size/product.reviews?size*100)?string('0.00')}[/#if]%)</span>
                                </dt>
                                <dd>
                                    <div class="bad"
                                         style="width:[#if product.reviews?size==0]0[#else]${lowScore?size/product.reviews?size*100}[/#if]%;"></div>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
                <div class="comment-list">
                    <div class="comment-list-hd">
                        <div class="trigger-wrap clearfix">
                            <ul id="trig-item">
                                <li class="trig-item curr" id="review_all">
                                    <a href="javascript:;">全部评价
                                        <span>（${product.reviews?size}）</span>
                                    </a>
                                </li>
                                <li class="trig-item" id="review_good">
                                    <a href="javascript:;">好评<span>（${highScore?size}）</span></a></li>
                                <li class="trig-item" id="review_middle">
                                    <a href="javascript:;">中评<span>（${middleScore?size}）</span></a></li>
                                <li class="trig-item" id="review_low">
                                    <a href="javascript:;">差评<span>（${lowScore?size}）</span></a></li>
                                <!-- <li class="trig-item" id="review_photo">
                                <a href="javascript:;">晒图评价
                                    <span>（${reviewImageSize?size}）</span>
                                </a>
                            </li> -->
                            </ul>
                        </div>
                    </div>
                    <script>
                        $(function () {
                            $("#trig-item li").click(function () {
                                $(this).addClass("curr").siblings().removeClass("curr");
                                var a = $(this).attr("id");
                                if (a == "review_all") {
                                    $("#all_content").show();
                                    $("#all_content").siblings().hide();
                                } else if (a == "review_good") {
                                    $("#good_content").show();
                                    $("#good_content").siblings().hide();
                                } else if (a == "review_middle") {
                                    $("#middle_content").show();
                                    $("#middle_content").siblings().hide();
                                } else if (a == "review_low") {
                                    $("#low_content").show();
                                    $("#low_content").siblings().hide();
                                } else {
                                    $("#photo_content").show();
                                    $("#photo_content").siblings().hide();
                                }
                            });
                        });
                    </script>
                    <div class="comments-table">
                        <div class="title">
                            <ul>
                                <li class="evaluation-result">评价心得</li>
                                <li class="satisfaction">顾客满意度</li>
                                <li class="buy-info">购买信息</li>
                                <li class="person">评论者</li>
                            </ul>
                        </div>
                        <div class="com-table-main">
                            <div class="comments-item" id="all_content">
                            [#if product.reviews?has_content]
                                [#list product.reviews as reviews]
                                    <div class="com-item-main clearfix">
                                        <div class="li evaluation-result">
                                            <span>${reviews.content}</span>
                                            &nbsp;&nbsp;&nbsp;
                                            <span class="color">${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                            <div class="show-img">
                                                [#if reviews.images?has_content]
                                                    <ul>
                                                        [#list reviews.images as images]
                                                            <li>
                                                                <a href="javascript:;" target="_blank">
                                                                    <img src="${images.thumbnail}" width="80"
                                                                         height="80"/>
                                                                </a>
                                                            </li>
                                                        [/#list]
                                                    </ul>
                                                    <span>共2张图片</span>
                                                <span><a class="blue" href="javascript:;"
                                                         target="_blank">查看晒单></a></span>
                                                [#else]
                                                    <ul>
                                                        <li>
                                                            <a href="javascript:;">

                                                            </a>
                                                        </li>
                                                    </ul>
                                                    <span></span>
                                                [/#if]
                                            </div>
                                        </div>
                                        <div class="li satisfaction">
                                            [#if reviews.score=="0"]
                                                <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★★</span>
                                            [#elseif reviews.score=="1"]
                                                <span class="color">★</span>
                                                <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★</span>
                                            [#elseif reviews.score=="2"]
                                                <span class="color">★★</span>
                                                <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★</span>
                                            [#elseif reviews.score=="3"]
                                                <span class="color">★★★</span>
                                                <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★</span>
                                            [#elseif reviews.score=="4"]
                                                <span class="color">★★★★</span>
                                                <span style="font-size:20px;line-height:12px;margin-left: -3px;">★</span>
                                            [#elseif reviews.score=="5"]
                                                <span class="color">★★★★★</span>
                                            [/#if]
                                        </div>
                                        <div class="li buy-info">
                                            <span>
                                                [#list reviews.product.specification_value as specification]
                                                    ${specification}
                                                [/#list]
                                            </span>
                                        </div>
                                        <div class="li person">
                                            <div class="user-item clearfix">
                                                <img src="${reviews.member.headImg}" width="25px" height="25px" alt=""
                                                     class="user-ico">
                                                [#if reviews.member.name?has_content]
                                                    [#if reviews.isAnonym=="true"]
                                                        匿名
                                                    [#else]
                                                    ${mosaic(reviews.member.name,2,"***")!"匿名"}
                                                    [/#if]
                                                [/#if]
                                            </div>
                                            <div class="type-item">
                                                <span class="u-vip-level red">${reviews.member.memberRank.name}</span>
                                                <span class="u-addr">${reviews.member.area.name}</span>
                                            </div>
                                            <div class="user-item">
                                                <span>${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                &nbsp;&nbsp;
                                                [#if reviews.flag=="product"]
                                                    <span>商品评价</span>
                                                [#elseif reviews.flag=="payment"]
                                                    <span>订单评价</span>
                                                [#else]
                                                    <span>店铺评价</span>
                                                [/#if]
                                                <!-- <span class="user-access">
                                                    <a href="javascript:;" target="_blank">来自XX客户端</a>
                                                </span> -->
                                            </div>
                                        </div>
                                    </div>
                                [/#list]
                            [#else]
                                <div class="com-item-main clearfix" style="text-align:center;">
                                    暂无数据
                                </div>
                            [/#if]
                            </div>
                            <div class="comments-item" id="photo_content" style="display:none;">
                            [#if product.reviews?has_content]
                                [#assign a="false"]
                                [#if a=="false"]
                                    [#list product.reviews as reviews]
                                        [#if reviews.content??]
                                            [#assign a="true"]
                                            <div class="com-item-main clearfix">
                                                <div class="li evaluation-result">
                                                    <span>${reviews.content}</span>
                                                    &nbsp;&nbsp;&nbsp;
                                                    <span class="color">${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                    <div class="show-img">
                                                        [#if reviews.images?has_content]
                                                            <ul>
                                                                [#list reviews.images as images]
                                                                    <li>
                                                                        <a href="javascript:;" target="_blank">
                                                                            <img src="${images.thumbnail}" width="80"
                                                                                 height="80"/>
                                                                        </a>
                                                                    </li>
                                                                [/#list]
                                                            </ul>
                                                            <span>共2张图片</span>
                                                        <span><a class="blue" href="javascript:;"
                                                                 target="_blank">查看晒单></a></span>
                                                        [#else]
                                                            <ul>
                                                                <li>
                                                                    <a href="javascript:;">

                                                                    </a>
                                                                </li>
                                                            </ul>
                                                            <span></span>
                                                        [/#if]
                                                    </div>
                                                </div>
                                                <div class="li satisfaction">
                                                    [#if reviews.score=="0"]
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★★</span>
                                                    [#elseif reviews.score=="1"]
                                                        <span class="color">★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★</span>
                                                    [#elseif reviews.score=="2"]
                                                        <span class="color">★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★</span>
                                                    [#elseif reviews.score=="3"]
                                                        <span class="color">★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★</span>
                                                    [#elseif reviews.score=="4"]
                                                        <span class="color">★★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★</span>
                                                    [#elseif reviews.score=="5"]
                                                        <span class="color">★★★★★</span>
                                                    [/#if]
                                                </div>
                                                <div class="li buy-info">
                                            <span>
                                                [#list reviews.product.specification_value as specification]
                                                    ${specification}
                                                [/#list]
                                            </span>
                                                </div>
                                                <div class="li person">
                                                    <div class="user-item clearfix">
                                                        <img src="${reviews.member.headImg}" width="25px" height="25px"
                                                             alt="" class="user-ico">
                                                        [#if reviews.member.name?has_content]
                                                            [#if reviews.isAnonym=="true"]
                                                                匿名
                                                            [#else]
                                                            ${mosaic(reviews.member.name,2,"***")!"匿名"}
                                                            [/#if]
                                                        [/#if]
                                                    </div>
                                                    <div class="type-item">
                                                        <span class="u-vip-level red">${reviews.member.memberRank.name}</span>
                                                        <span class="u-addr">${reviews.member.area.name}</span>
                                                    </div>
                                                    <div class="user-item">
                                                        <span>${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                        &nbsp;&nbsp;
                                                        [#if reviews.flag=="product"]
                                                            <span>商品评价</span>
                                                        [#elseif reviews.flag=="payment"]
                                                            <span>订单评价</span>
                                                        [#else]
                                                            <span>店铺评价</span>
                                                        [/#if]
                                                        <!-- <span class="user-access">
                                                            <a href="javascript:;" target="_blank">来自XX客户端</a>
                                                        </span> -->
                                                    </div>
                                                </div>
                                            </div>
                                        [/#if]

                                    [/#list]
                                [#else]
                                    <div class="com-item-main clearfix" style="text-align:center;">
                                        暂无数据
                                    </div>
                                [/#if]
                            [#else]
                                <div class="com-item-main clearfix" style="text-align:center;">
                                    暂无数据
                                </div>
                            [/#if]
                            </div>
                            <div class="comments-item" id="good_content" style="display:none;">
                            [#if product.reviews?has_content]
                                [#assign b="false"]
                                [#if b=="false"]
                                    [#list product.reviews as reviews]
                                        [#if reviews.score gt 3]
                                            [#assign b="true"]
                                            <div class="com-item-main clearfix">
                                                <div class="li evaluation-result">
                                                    <span>${reviews.content}</span>
                                                    &nbsp;&nbsp;&nbsp;
                                                    <span class="color">${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                    <div class="show-img">
                                                        [#if reviews.images?has_content]
                                                            <ul>
                                                                [#list reviews.images as images]
                                                                    <li>
                                                                        <a href="javascript:;" target="_blank">
                                                                            <img src="${images.thumbnail}" width="80"
                                                                                 height="80"/>
                                                                        </a>
                                                                    </li>
                                                                [/#list]
                                                            </ul>
                                                            <span>共2张图片</span>
                                                        <span><a class="blue" href="javascript:;"
                                                                 target="_blank">查看晒单></a></span>
                                                        [#else]
                                                            <ul>
                                                                <li>
                                                                    <a href="javascript:;">

                                                                    </a>
                                                                </li>
                                                            </ul>
                                                            <span></span>
                                                        [/#if]
                                                    </div>
                                                </div>
                                                <div class="li satisfaction">
                                                    [#if reviews.score=="0"]
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★★</span>
                                                    [#elseif reviews.score=="1"]
                                                        <span class="color">★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★</span>
                                                    [#elseif reviews.score=="2"]
                                                        <span class="color">★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★</span>
                                                    [#elseif reviews.score=="3"]
                                                        <span class="color">★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★</span>
                                                    [#elseif reviews.score=="4"]
                                                        <span class="color">★★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★</span>
                                                    [#elseif reviews.score=="5"]
                                                        <span class="color">★★★★★</span>
                                                    [/#if]
                                                </div>
                                                <div class="li buy-info">
                                            <span>
                                                [#list reviews.product.specification_value as specification]
                                                    ${specification}
                                                [/#list]
                                            </span>
                                                </div>
                                                <div class="li person">
                                                    <div class="user-item clearfix">
                                                        <img src="${reviews.member.headImg}" width="25px" height="25px"
                                                             alt="" class="user-ico">
                                                        [#if reviews.member.name?has_content]
                                                            [#if reviews.isAnonym=="true"]
                                                                匿名
                                                            [#else]
                                                            ${mosaic(reviews.member.name,2,"***")!"匿名"}
                                                            [/#if]
                                                        [/#if]
                                                    </div>
                                                    <div class="type-item">
                                                        <span class="u-vip-level red">${reviews.member.memberRank.name}</span>
                                                        <span class="u-addr">${reviews.member.area.name}</span>
                                                    </div>
                                                    <div class="user-item">
                                                        <span>${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                        &nbsp;&nbsp;
                                                        [#if reviews.flag=="product"]
                                                            <span>商品评价</span>
                                                        [#elseif reviews.flag=="payment"]
                                                            <span>订单评价</span>
                                                        [#else]
                                                            <span>店铺评价</span>
                                                        [/#if]
                                                        <!-- <span class="user-access">
                                                            <a href="javascript:;" target="_blank">来自XX客户端</a>
                                                        </span> -->
                                                    </div>
                                                </div>
                                            </div>
                                        [/#if]
                                    [/#list]
                                [#else]
                                    <div class="com-item-main clearfix" style="text-align:center;">
                                        暂无数据
                                    </div>
                                [/#if]
                            [#else]
                                <div class="com-item-main clearfix" style="text-align:center;">
                                    暂无数据
                                </div>
                            [/#if]
                            </div>
                            <div class="comments-item" id="middle_content" style="display:none;">
                            [#if product.reviews?has_content]
                                [#assign c="false"]
                                [#if c=="false"]
                                    [#list product.reviews as reviews]
                                        [#if reviews.score == 3]
                                            [#assign c="true"]
                                            <div class="com-item-main clearfix">
                                                <div class="li evaluation-result">
                                                    <span>${reviews.content}</span>
                                                    &nbsp;&nbsp;&nbsp;
                                                    <span class="color">${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                    <div class="show-img">
                                                        [#if reviews.images?has_content&&reviews.images?size gt 0]
                                                            <ul>
                                                                [#list reviews.images as images]
                                                                    <li>
                                                                        <a href="javascript:;" target="_blank">
                                                                            <img src="${images.thumbnail}" width="80"
                                                                                 height="80"/>
                                                                        </a>
                                                                    </li>
                                                                [/#list]
                                                            </ul>
                                                            <span>共2张图片</span>
                                                        <span><a class="blue" href="javascript:;"
                                                                 target="_blank">查看晒单></a></span>
                                                        [#else]
                                                            <ul>
                                                                <li>
                                                                    <a href="javascript:;">

                                                                    </a>
                                                                </li>
                                                            </ul>
                                                            <span></span>
                                                        [/#if]
                                                    </div>
                                                </div>
                                                <div class="li satisfaction">
                                                    [#if reviews.score=="0"]
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★★</span>
                                                    [#elseif reviews.score=="1"]
                                                        <span class="color">★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★</span>
                                                    [#elseif reviews.score=="2"]
                                                        <span class="color">★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★</span>
                                                    [#elseif reviews.score=="3"]
                                                        <span class="color">★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★</span>
                                                    [#elseif reviews.score=="4"]
                                                        <span class="color">★★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★</span>
                                                    [#elseif reviews.score=="5"]
                                                        <span class="color">★★★★★</span>
                                                    [/#if]
                                                </div>
                                                <div class="li buy-info">
                                            <span>
                                                [#list reviews.product.specification_value as specification]
                                                    ${specification}
                                                [/#list]
                                            </span>
                                                </div>
                                                <div class="li person">
                                                    <div class="user-item clearfix">
                                                        <img src="${reviews.member.headImg}" width="25px" height="25px"
                                                             alt="" class="user-ico">
                                                        [#if reviews.member.name?has_content]
                                                            [#if reviews.isAnonym=="true"]
                                                                匿名
                                                            [#else]
                                                            ${mosaic(reviews.member.name,2,"***")!"匿名"}
                                                            [/#if]
                                                        [/#if]
                                                    </div>
                                                    <div class="type-item">
                                                        <span class="u-vip-level red">${reviews.member.memberRank.name}</span>
                                                        <span class="u-addr">${reviews.member.area.name}</span>
                                                    </div>
                                                    <div class="user-item">
                                                        <span>${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                        &nbsp;&nbsp;
                                                        [#if reviews.flag=="product"]
                                                            <span>商品评价</span>
                                                        [#elseif reviews.flag=="payment"]
                                                            <span>订单评价</span>
                                                        [#else]
                                                            <span>店铺评价</span>
                                                        [/#if]
                                                        <!-- <span class="user-access">
                                                            <a href="javascript:;" target="_blank">来自XX客户端</a>
                                                        </span> -->
                                                    </div>
                                                </div>
                                            </div>
                                        [/#if]
                                    [/#list]
                                [#else]
                                    <div class="com-item-main clearfix" style="text-align:center;">
                                        暂无数据
                                    </div>
                                [/#if]
                            [#else]
                                <div class="com-item-main clearfix" style="text-align:center;">
                                    暂无数据
                                </div>
                            [/#if]
                            </div>
                            <div class="comments-item" id="low_content" style="display:none;">
                            [#if product.reviews?has_content]
                                [#assign d="false"]
                                [#if d=="false"]
                                    [#list product.reviews as reviews]
                                        [#if reviews.score lt 3]
                                            [#assign d="true"]
                                            <div class="com-item-main clearfix">
                                                <div class="li evaluation-result">
                                                    <span>${reviews.content}</span>
                                                    &nbsp;&nbsp;&nbsp;
                                                    <span class="color">${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                    <div class="show-img">
                                                        [#if reviews.images?has_content]
                                                            <ul>
                                                                [#list reviews.images as images]
                                                                    <li>
                                                                        <a href="javascript:;" target="_blank">
                                                                            <img src="${images.thumbnail}" width="80"
                                                                                 height="80"/>
                                                                        </a>
                                                                    </li>
                                                                [/#list]
                                                            </ul>
                                                            <span>共2张图片</span>
                                                        <span><a class="blue" href="javascript:;"
                                                                 target="_blank">查看晒单></a></span>
                                                        [#else]
                                                            <ul>
                                                                <li>
                                                                    <a href="javascript:;">

                                                                    </a>
                                                                </li>
                                                            </ul>
                                                            <span></span>
                                                        [/#if]
                                                    </div>
                                                </div>
                                                <div class="li satisfaction">
                                                    [#if reviews.score=="0"]
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★★</span>
                                                    [#elseif reviews.score=="1"]
                                                        <span class="color">★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★★</span>
                                                    [#elseif reviews.score=="2"]
                                                        <span class="color">★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★★</span>
                                                    [#elseif reviews.score=="3"]
                                                        <span class="color">★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★★</span>
                                                    [#elseif reviews.score=="4"]
                                                        <span class="color">★★★★</span>
                                                        <span style="font-size:20px;line-height:12px;margin-left: -3px;">★</span>
                                                    [#elseif reviews.score=="5"]
                                                        <span class="color">★★★★★</span>
                                                    [/#if]
                                                </div>
                                                <div class="li buy-info">
                                            <span>
                                                [#list reviews.product.specification_value as specification]
                                                    ${specification}
                                                [/#list]
                                            </span>
                                                </div>
                                                <div class="li person">
                                                    <div class="user-item clearfix">
                                                        <img src="${reviews.member.headImg}" width="25px" height="25px"
                                                             alt="" class="user-ico">
                                                        [#if reviews.member.name?has_content]
                                                            [#if reviews.isAnonym=="true"]
                                                                匿名
                                                            [#else]
                                                            ${mosaic(reviews.member.name,2,"***")!"匿名"}
                                                            [/#if]
                                                        [/#if]
                                                    </div>
                                                    <div class="type-item">
                                                        <span class="u-vip-level red">${reviews.member.memberRank.name}</span>
                                                        <span class="u-addr">${reviews.member.area.name}</span>
                                                    </div>
                                                    <div class="user-item">
                                                        <span>${reviews.createDate?string("yyyy-MM-dd hh:mm:ss")}</span>
                                                        &nbsp;&nbsp;
                                                        [#if reviews.flag=="product"]
                                                            <span>商品评价</span>
                                                        [#elseif reviews.flag=="payment"]
                                                            <span>订单评价</span>
                                                        [#else]
                                                            <span>店铺评价</span>
                                                        [/#if]
                                                        <!-- <span class="user-access">
                                                            <a href="javascript:;" target="_blank">来自XX客户端</a>
                                                        </span> -->
                                                    </div>
                                                </div>
                                            </div>
                                        [/#if]
                                    [/#list]
                                [#else]
                                    <div class="com-item-main clearfix" style="text-align:center;">
                                        暂无数据
                                    </div>
                                [/#if]
                            [#else]
                                <div class="com-item-main clearfix" style="text-align:center;">
                                    暂无数据
                                </div>
                            [/#if]
                            </div>
                        </div>
                        <!-- <div class="com-table-footer">
                            <div class="page-wrap clearfix">
                                <div class="page">
                                    <a rel="1" class="page-curr" href="javascript:void(0)">1</a>
                                    <a rel="2" href="javascript:;">2</a>
                                    <a rel="3" href="javascript:;">3</a>
                                    <a rel="4" href="javascript:;">4</a>
                                    <a rel="5" href="javascript:;">5</a>
                                    <a rel="6" href="javascript:;">6</a>
                                    <span>...</span>
                                    <a rel="1348" href="javascript:;">1348</a>
                                    <a rel="2" class="pager-next" href="javascript:;">下一页</a>
                                </div>
                            </div>
                        </div> -->
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!--足迹start-->
    <div class="container footmark">
    [#--[#if product.tags?has_content]--]
        <div class="dl may-like">
            <div class="dt clearfix">
                <h2 class="title">猜你喜欢</h2>
                <div class="extra">
                    <!--span class="change"><em>1</em>/4</span-->
                </div>
            </div>
            <div class="dd">
                <a href="javascript:;" class="guess-ctl guess-prev guess-prev-disable"></a>
                <a href="javascript:;" class="guess-ctl guess-next"></a>
                <div class="guess-scroll">
                    <ul class="may-like-list clearfix">

                    [@product_list tenantId=product.tenant.id count = 6 tagid= 4]
                        [#list products as product]
                            <li>
                                <div class="img">
                                    <a href="${base}/b2b/product/detail/${product.id}.jhtml"
                                       title="${product.fullName}">
                                        <img src="${product.image}" alt="${product.fullName}" width="160"
                                             height="160">
                                    </a>
                                    <!-- <div class="name">
                                        <a href="javascript:;" target="_blank" title="">7.6折 ↘</a>
                                    </div> -->
                                </div>
                                <div class="price">￥${product.price}</div>
                            </li>
                        [/#list]
                    [/@product_list]

                    </ul>
                </div>
            </div>
        </div>
    [#--[/#if]--]
        <div class="dl recent-view">
            <div class="dt">
                <h2 class="title">我的足迹</h2>
                <div class="extra">
                    <a href="javascript:;" target="_blank">更多浏览记录</a>
                </div>
            </div>
            <div class="dd">
                <ul class="recent-view-list clearfix" id="history">

                </ul>
            </div>
        </div>

    </div>
    <!--足迹end-->
    <!--标语start-->
[#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
<!-- [#include "/b2b/include/hover_right.ftl"/] -->
<!--右侧悬浮框end-->
</body>
</html>