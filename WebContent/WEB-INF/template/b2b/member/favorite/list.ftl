<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/cart.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2b/include/topnav.ftl"]
</div>

<!--主页内容区start-->
<div class="paper">
    <!-- 会员中心头部-->
[#include "/b2b/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
        [#include "/b2b/include/member_left.ftl"]
            <div class="content">
                <!-- 我的收藏 -->
                <div class="my-collect">
                    <div class="">
                        <form action="list.jhtml" method="get">
                            <div class="my-collect-header">
                                <div>
                                    <div class="f-left">
                                    [#if versionType==0]
                                        <span class="li [#if type="tenant"]selected[/#if] tentant_collect">
                                    <a href="list.jhtml?type=tenant">
                                        <span class="title">收藏的店铺</span>
                                        <em></em>
                                        <span class="wire">|</span>
                                    </a>
                                </span>
                                    [/#if]
                                        <span class="li [#if type="product"]selected[/#if] product_collect">
                                    <a href="list.jhtml?type=product">
                                        <span class="title">收藏的商品</span>
                                        <em></em>
                                    [#if versionType==0] <span class="wire">|</span>[/#if]
                                    </a>
                                </span>
                                    </div>
                                    <div class="my-collect-h-r f-right">
                                    [#--<div class="select-all">--]
                                    [#--<div class="checkbox ">--]
                                    [#--<input type="checkbox" disabled="">--]
                                    [#--<label for=""></label>--]
                                    [#--</div>--]
                                    [#--<span>全选</span>--]
                                    [#--</div>--]
                                    [#--<div class="delete">--]
                                    [#--<a href="javascript:;" title="删除订单"></a>--]
                                    [#--<span>删除</span>--]
                                    [#--</div>--]
                                        <!--div class="search">
                                            <div class="search-field">
                                                <input name="type" value="${type}" type="hidden">
                                                <input name="searchProperty"
                                                       value="[#if type=="tenant"]name[#else]fullName[/#if]"
                                                       type="hidden">
                                                <input value="${page.searchValue!}"
                                                       placeholder="[#if type=="tenant"]店铺[#else]商品[/#if]搜索" type="text"
                                                       name="searchValue">
                                                <button type="submit">搜索</button>
                                            </div>
                                        </div-->
                                    </div>
                                </div>
                            </div>
                            <!--收藏的店铺-->
                        [#if type=="tenant"]
                            <div class="my-collect-content">
                                [#list page.content as tenant]
                                    <div class="li">
                                        <div class="left">
                                            <div class="pic">
                                                <img width="50" height="50"
                                                     src="${tenant.thumbnail}"/>
                                            </div>
                                            <div class="collect-info">
                                                <h2><a href="${base}/b2b/tenant/index.jhtml?id=${(tenant.id)!}">${tenant.name}</a></h2>
                                                <span class="name">${tenant.member.username}</span>
                                                <span class="main">主营：<em>${tenant.scopeOfBusiness}</em></span>
                                                <span class="level">等级：<em>[#if tenant.score>0][#list 1..tenant.score as t]
                                                    ❤[/#list][/#if]</em></span>
                                                <span class="company">信息：<em>${tenant.introduction}</em></span>
                                    <span class="label">
                                        [#list tenant.promotions as promotion]
                                            [#if promotion.type=="seckill"]
                                                <a class="bg-red" href="javascript:;">${promotion.name}</a>
                                            [#elseif promotion.type=="buyfree"]
                                                <a class="bg-sandybrown" href="javascript:;">${promotion.name}</a>
                                            [#elseif promotion.type=="mail"]
                                                <a class="bg-plum" href="javascript:;">${promotion.name}</a>
                                            [#else]
                                                <a class="bg-red" href="javascript:;">${promotion.name}</a>
                                            [/#if]
                                        [/#list]
                                    </span>
                                            </div>
                                        </div>
                                        <div class="content tenant-products">
                                            [#assign newCount=0]
                                            [#assign recommonedCount=0]
                                            [#assign promotionCount=0]
                                            <div class="collect-show new-product">
                                                <ul>
                                                    [#list tenant.products as product]
                                                        [#list product.tags as tag]
                                                            [#if tag.id==2]
                                                                [#assign newCount=newCount+1]
                                                                <li>
                                                                    <a href="${base}/b2b/product/detail/${product.id}.jhtml">
                                                                        <img src="${product.thumbnail}">
                                                                    </a>
                                                                    <span>￥${product.price}</span>
                                                                </li>
                                                            [/#if]
                                                        [/#list]
                                                    [/#list]
                                                </ul>
                                            </div>
                                            <div class="collect-show recommoned-product" style="display: none;">
                                                <ul>
                                                    [#list tenant.products as product]
                                                        [#list product.tags as tag]
                                                            [#if tag.id==5]
                                                                [#assign recommonedCount=recommonedCount+1]
                                                                <li>
                                                                    <a href="${base}/b2b/product/detail/${product.id}.jhtml">
                                                                        <img src="${product.thumbnail}">
                                                                    </a>
                                                                    <span>￥${product.price}</span>
                                                                </li>
                                                            [/#if]
                                                        [/#list]
                                                    [/#list]
                                                </ul>
                                            </div>
                                            <div class="collect-show promotion-product" style="display: none;">
                                                <ul>
                                                    [#list tenant.products as product]
                                                        [#list product.tags as tag]
                                                            [#if tag.id==15]
                                                                [#assign promotionCount=promotionCount+1]
                                                                <li>
                                                                    <a href="${base}/b2b/product/detail/${product.id}.jhtml">
                                                                        <img src="${product.thumbnail}">
                                                                    </a>
                                                                    <span>￥${product.price}</span>
                                                                </li>
                                                            [/#if]
                                                        [/#list]
                                                    [/#list]
                                                </ul>
                                            </div>
                                            <div class="nav">
                                                <div class="f-left">
                                                    <span class="curr new">上新<em>${newCount}</em></span>
                                                    <span class="recommoned">推荐<em>${recommonedCount}</em></span>
                                                    <span class="promotion">促销<em>${promotionCount}</em></span>
                                                </div>
                                                <div class="f-right open-product">
                                                    <a href="javascript:;">展开></a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                [/#list]
                            </div>
                        [/#if]
                            <!--收藏的商品-->
                        [#if type=="product"]
                            <div class="my-collect-products">
                                <ul>
                                    [#list page.content as product]
                                        <li>
                                            <a class="img" href="${base}/b2b/product/detail/${product.id}.jhtml" target="_blank">
                                                <img src="${product.thumbnail}"/>
                                            </a>
                                            <div class="depict">
                                                <a href="javascript:;" target="_blank">&nbsp;&nbsp;&nbsp;&nbsp;</a>
                                                <a href="${base}/b2b/product/list/0.jhtml?keyword=${product.name}" target="_blank">找相似</a>
                                            </div>
                                            <div class="delete" onclick="" productId="${product.id}"><i></i></div>
                                            <a class="title" href="${base}/b2b/product/detail/${product.id}.jhtml" target="_blank"
                                               title="${product.fullName}">${product.fullName}</a>
                                            <div class="price">
                                                <i></i>
                                                <span class="color">￥${product.price?string("0.00")}</span>
                                                <em>￥${product.marketPrice?string("0.00")}</em>
                                            </div>
                                        </li>
                                    [/#list]
                                </ul>
                            </div>
                        [/#if]
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <iframe id="interest" name="interest" src="${base}/b2b/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
[#include "/b2b/include/slogen.ftl"]
</div>

<script type="text/javascript">
    $(function () {
        $.each($(".tenant-products"), function () {
            $(this).find(".new-product").before($(this).find(".nav"));
        });
        $(".new").click(function () {
            $(this).parent().children().removeClass("curr");
            $(this).addClass("curr");
            $(this).parents('.tenant-products').find('.new-product').show();
            $(this).parents('.tenant-products').find('.recommoned-product').hide();
            $(this).parents('.tenant-products').find('.promotion-product').hide();
        });
        $(".recommoned").click(function () {
            $(this).parent().children().removeClass("curr");
            $(this).addClass("curr");
            $(this).parents('.tenant-products').find('.new-product').hide();
            $(this).parents('.tenant-products').find('.recommoned-product').show();
            $(this).parents('.tenant-products').find('.promotion-product').hide();
        });
        $(".promotion").click(function () {
            $(this).parent().children().removeClass("curr");
            $(this).addClass("curr");
            $(this).parents('.tenant-products').find('.new-product').hide();
            $(this).parents('.tenant-products').find('.recommoned-product').hide();
            $(this).parents('.tenant-products').find('.promotion-product').show();
        });
        $(".open-product").click(function () {
            if ($(this).parents('.tenant-products').find(".collect-show").height() == 190) {
                $(this).parents('.tenant-products').find(".collect-show").height("auto");
                $(this).find("a").text("折叠>");
            } else {
                $(this).parents('.tenant-products').find(".collect-show").height(190);
                $(this).find("a").text("展开>");
            }
        });
        $(".delete").click(function () {
            var id = $(this).attr("productId");
            if (window.confirm("确认取消收藏吗？")) {
                $.ajax({
                    type: 'post',
                    url: 'delete.jhtml',
                    data: {id: id},
                    dataType: 'json',
                    success: function (data) {
                        $.message(data);
                        window.setTimeout(function () {
                            window.location.reload(true);
                        }, 1000);
                    }
                });
            }
        });
    });

</script>

<!--底部 -->
[#include "/b2b/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
