[#-- @ftlroot "../" --]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-首页</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript">
        //        设置商品数量
        function set_number(obj) {
            if ($(obj).attr("id") == "minus") {
                var prevObj = $(obj).next().val();
                if (prevObj > 1) {
                    prevObj--;
                    $(obj).next().val(prevObj)
                }
            } else if ($(obj).attr("id") == "plus") {
                var nextObj = $(obj).prev().val();
                nextObj++;
                $(obj).prev().val(nextObj);
            }

        }
        //添加到购物车
        function addcart(id, obj) {
            $.ajax({
                url: "${base}/b2b/cart/add.jhtml",
                type: "POST",
                data: {id: id, quantity: $(obj).prev().prev().val()},
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.type == "success") {
                        // location.href = "${base}/b2b/cart/list.jhtml"
                        $.message("success", "添加购物车成功");
                        refreshCartCount();
                    } else {
                        $.message(message);
                    }
                }
            });
        }
        //收藏商品/取消收藏
        function addFavorite(id, obj) {
            if ("${member}" == "") {
                location.href = "${base}/b2b/login.jhtml";
            } else {
                if ($(obj).attr("class") == "save_product") {
                    $.ajax({
                        url: "${base}/b2b/member/favorite/delete.jhtml",
                        type: "POST",
                        data: {id: id},
                        dataType: "json",
                        cache: false,
                        success: function (message) {
                            if (message.type == "success") {
                                $(obj).removeClass("save_product");
                                document.location.reload();
                            }
                            $.message(message);
                        }
                    });
                } else {
                    console.log(2);
                    $.ajax({
                        url: "${base}/b2b/member/favorite/add.jhtml",
                        type: "POST",
                        data: {id: id},
                        dataType: "json",
                        cache: false,
                        success: function (message) {
                            if (message.type == "success") {
                                $(obj).addClass("save_product");
                                location.reload();
                            }
                            $.message(message);
                        }
                    });
                }
            }
        }
    </script>
    
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
    /*所有商品分类*/
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
</script>
<!--主页内容区start-->
<div class="paper pc-bg">
    <!--start-->
    <div class="container wallpaper">
        <!--left-->
        <div class="left"></div>
        <!--right-->
        <div class="content">
            <!--轮播-->
            <div class="wrapper">
                <!--焦点轮播-->
                <div class="topWrapper">
                    <ul class="topWrapperSwiper">
                    [@ad_position id=96 areaId=area.id count=0/]
                    </ul>
                    <div>
                        <div class="pagination-all">
                            <div class="pagination"></div>
                        </div>
                    </div>

                    <div class="topWrapperLeft li"><</div>
                    <div class="topWrapperRight li">></div>
                </div>
                <!--小轮播-->
                <div class="bottomWrapper">
                    <div class="bottomWrapperLeft"><</div>
                    <div class="bottomWrapperRight">></div>
                    <div>
                        <ul>
                        [#list dayProducts as products]
                            <li>
                                <a href="${base}/b2b/product/detail/${products.id}.jhtml">
                                    <img src="[#if products.image??]${products.image}[#else]${base}/resources/wap/image/NoPicture.jpg[/#if]"
                                         alt="" style="width:150px;height:150px;"/>
                                    <p>${abbreviate(products.fullName,20,'...')}</p>
                                    <span>
                                        [#if member??]
                                            ￥${products.price}
                                        [#else]
                                            ￥${products.marketPrice}
                                        [/#if]
        						</span>
                                </a>
                            </li>
                        [/#list]
                        </ul>
                    </div>
                </div>
            </div>
            <!--右侧通告栏-->
            <div class="notice">
                <div class="userinfor">
                    <div class="headBox">
                    [#if member??]
                        <a href="${base}/b2b/member/order/list.jhtml">
                            <img src="[#if member.headImg??&&member.headImg?has_content]${member.headImg!}[#else]${base}/resources/b2b/img/AccountBitmap-head.png[/#if]"
                                 style="width: 100%;height: 100%;">
                        </a>
                    [#else ]
                        <img src="${base}/resources/b2b/img/AccountBitmap-head.png" style="width: 100%;">
                    [/#if]
                    </div>
                    <div>
                        <span>
                            [#assign date=.now?string("HH")?number]
                                HI,[#if date gte 6&&date lt 9]早上[#elseif date gte 9 &&date lt 11]
                                上午[#elseif date gte 11&&date lt 13]中午[#elseif date gte 13&&date lt 18]
                                下午[#elseif date gte 18&&date lt 24]晚上[#elseif date gte 0&&date lt 6]凌晨[/#if]好!
                        	<p>欢迎来到${setting.siteName}</p>
                        </span>
                    </div>
                </div>

                <div class="loginRegister">
                [#if member??&&member?has_content]

                [#else ]
                    <a href="${base}/b2b/login.jhtml?type=signin">登录</a>
                    <a href="${base}/b2b/register/register_company.jhtml">注册</a>
                [/#if]
                </div>


                <div class="noticeEvent">
                    <dl>
                        <dd class="noticeEventLeft on">网站公告</dd>
                        <dd class="noticeEventRight">营销活动</dd>
                    </dl>
                    <ul class="display">
                    [#list ad_article as article]
                        <li style="height:17%;">
                            <a href="${base}/helper/article/content/${article.id}.jhtml" target="_blank">
                                <i>[公告]</i>
                                <span>${article.title}</span>
                            </a>
                        </li>
                    [/#list]
                    </ul>
                    <ul>
                    [#list sale_article as article]
                        <li style="height:17%;">
                            <a href="${base}/helper/article/content/${article.id}.jhtml" target="_blank">
                                <i>[活动]</i>
                                <span>${article.title}</span>
                            </a>
                        </li>
                    [/#list]
                    </ul>
                </div>
                <div class="noticeBottom">
                    <ul>
                        <li>
                            <i class="icon01"></i>
                            <span>买家保障</span>
                        </li>
                        <li>
                            <i class="icon02"></i>
                            <span>商家认证</span>
                        </li>
                        <li>
                            <i class="icon03"></i>
                            <span>货到付款</span>
                        </li>
                        <li>
                            <i class="icon04"></i>
                            <span>快速配送</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!--end-->
    <!--热销商品start-->
    <div class="hotProducts container">
        <div class="hotProductsNav">
            <div class="left"><span>热销商品</span></div>
            <dl class="content">
                <dd class="on">新品预购</dd>
                <dd>特惠商品</dd>
            </dl>
        </div>

        <div class="hotProductsShow">
            <ul class="display">
            [#list newProducts as products]
                <li>
                    <a class="img-btn" href="${base}/b2b/product/detail/${products.id}.jhtml">
                        <div class="img-con">
                            <div class="img-mask"></div>
                            <img src="${products.image}"/>
                        </div>
                    </a>
                    <h2>
                        <a href="${base}/b2b/product/detail/${products.id}.jhtml">${products.fullName}</a>
                    </h2>
                    <dl>
                        [#if member??]
                            <dd>建议零售价：<span
                                    style="color:gray;font-size:15px;text-decoration:line-through;font-weight:normal;">￥${products.marketPrice}</span>
                            </dd>
                            <dd>销售价：<span>￥${products.price}</span></dd>
                        [#else]
                            <dd>建议零售价：<span>￥${products.marketPrice}</span></dd>
                        [/#if]
                        <dd>
                            <div onclick="set_number(this)" id="minus">-</div>
                            <input name="number" type="text" id="number" value="1"/>
                            <div onclick="set_number(this)" id="plus">+</div>
                            <i onclick="addcart(${products.id},this)"></i>
                        </dd>
                    </dl>
                    <div>已售：${products.sales}</div>

                    <div>
                        [#if member??&&member.favoriteProducts?has_content]
                            [#list member.favoriteProducts as product]
                                [#if product.id==products.id]已[/#if]
                            [/#list]
                        [/#if]收 藏：
                        <i onclick="addFavorite(${products.id},this)"
                            [#if member??&&member.favoriteProducts?has_content]
                                [#list member.favoriteProducts as product]
                           [#if product.id==products.id]class="save_product"[/#if]
                                [/#list]
                            [/#if]>
                        </i>
                    </div>

                </li>
            [/#list]
            </ul>
            <ul>
            [#list specialProducts as products]
                <li>
                    <a class="img-btn" href="${base}/b2b/product/detail/${products.id}.jhtml">
                        <div class="img-con">
                            <div class="img-mask"></div>
                            <img src="${products.image}"/>
                        </div>
                    </a>
                    <h2>
                        <a href="${base}/b2b/product/detail/${products.id}.jhtml">${products.fullName}</a>
                    </h2>
                    <dl>
                        [#if member??]
                            <dd>建议零售价：<span
                                    style="color:gray;font-size:15px;text-decoration:line-through;font-weight:normal;">￥${products.marketPrice}</span>
                            </dd>
                            <dd>销售价：<span>￥${products.price}</span></dd>
                        [#else]
                            <dd>建议零售价：<span>￥${products.marketPrice}</span></dd>
                        [/#if]
                        <dd>
                            <div onclick="set_number(this)" id="minus">-</div>
                            <input name="number" type="text" id="number" value="1"/>
                            <div onclick="set_number(this)" id="plus">+</div>
                            <i onclick="addcart(${products.id},this)"></i>
                        </dd>
                    </dl>
                    <div>已售：${products.sales}</div>
                    <div>
                        [#if member??&&member.favoriteProducts?has_content]
                            [#list member.favoriteProducts as product]
                                [#if product.id==products.id]已[/#if]
                            [/#list]
                        [/#if]收 藏：
                        <i onclick="addFavorite(${products.id},this)"
                            [#if member??&&member.favoriteProducts?has_content]
                                [#list member.favoriteProducts as product]
                           [#if product.id==products.id]class="save_product"[/#if]
                                [/#list]
                            [/#if]>
                        </i>
                    </div>
                </li>
            [/#list]
            </ul>
        </div>
    </div>
    <!--热销商品end-->
    <!--限时抢购start-->
[#if (promotions_started??&&promotions_started?has_content)||(promotions_unstart??&&promotions_unstart?has_content)]
    <div class="flashSale container">
        <div class="flashSaleNav">
            <div class="left"><span>限时抢购</span></div>
            <dl class="content">
                <dd class="on">正在进行</dd>
                [#if promotions_unstart??&&promotions_unstart?has_content]
                    <dd>即将开始</dd>
                [/#if]
            </dl>
        </div>

        <div class="flashSaleShow">
            <div>
                [@ad_position id=99 areaId=area.id count=1/]
            </div>
            [#if promotions_started??&&promotions_started?has_content]
                <ul class="display">
                    [#list promotions_started as promotions]
                        <li>
                            <a class="img-btn" href="${base}/b2b/product/detail/${promotions.productId}.jhtml">
                                <div class="img-con">
                                    <div class="img-mask"></div>
                                    <img src="${promotions.thumbnail}"/>
                                </div>
                            </a>
                            <h2>
                                <a href="${base}/b2b/product/detail/${promotions.productId}.jhtml">${promotions.fullName}</a>
                            </h2>
                            <dl>
                                <dd>
                                    <span>￥${promotions.price}</span>
                                    <em>原价：￥${promotions.marketPrice}</em>
                                </dd>
                                <dd>
                                    <div onclick="set_number(this)" id="minus">-</div>
                                    <input name="number" type="text" id="number" value="1"/>
                                    <div onclick="set_number(this)" id="plus">+</div>
                                    <a href="${base}/b2b/product/detail/${promotions.productId}.jhtml"
                                       style="text-decoration:none;">抢购</a>
                                </dd>
                            </dl>
                            <!-- <div>已售：</div> -->
                            <div>
                                [#if member??&&member.favoriteProducts?has_content]
                                    [#list member.favoriteProducts as product]
                                        [#if product.id==promotions.productId]已[/#if]
                                    [/#list]
                                [/#if]收 藏：
                                <i onclick="addFavorite(${promotions.productId},this)"
                                    [#if member??&&member.favoriteProducts?has_content]
                                        [#list member.favoriteProducts as product]
                                   [#if product.id==promotions.productId]class="save_product"[/#if]
                                        [/#list]
                                    [/#if]>
                                </i>
                            </div>
                        </li>
                    [/#list]
                </ul>
            [/#if]
            [#if promotions_unstart??&&promotions_unstart?has_content]
                <ul>
                    [#list promotions_unstart as promotions]
                        <li>
                            <a href="${base}/b2b/product/detail/${promotions.productId}.jhtml"><img
                                    src="${promotions.thumbnail}"/></a>
                            <h2>
                                <a href="${base}/b2b/product/detail/${promotions.productId}.jhtml">${promotions.fullName}</a>
                            </h2>
                            <dl>
                                <dd>
                                    <span>￥${promotions.price}</span>
                                    <em>原价：￥${promotions.marketPrice}</em>
                                </dd>
                                <dd>
                                    <div onclick="set_number(this)" id="minus">-</div>
                                    <input name="number" type="text" id="number" value="1"/>
                                    <div onclick="set_number(this)" id="plus">+</div>
                                    <a href="${base}/b2b/product/detail/${promotions.productId}.jhtml"
                                       style="text-decoration:none;">抢购</a>
                                </dd>
                            </dl>
                            <!-- <div>已售：</div> -->
                            <div>
                                [#if member??&&member.favoriteProducts?has_content]
                                    [#list member.favoriteProducts as product]
                                        [#if product.id==promotions.productId]已[/#if]
                                    [/#list]
                                [/#if]收 藏：
                                <i onclick="addFavorite(${promotions.productId},this)"
                                    [#if member??]
                                        [#list member.favoriteProducts as product]
                                   [#if product.id==promotions.productId]class="save_product"[/#if]
                                        [/#list]
                                    [/#if]>
                                </i>
                            </div>
                        </li>
                    [/#list]
                </ul>
            [/#if]
        </div>
    </div>
[/#if]
    <!--限时抢购end-->
    <!--品牌推荐start-->
[#if versionType==1]
    <div class="brandRecommend container">
        <div class="brandRecommendNav">
            <div class="left"><span>品牌推荐</span></div>
            <ul class="content"></ul>
        </div>
        <div class="brandRecommendShow">
            <ul>
                [#list brand as brands]
                    <li>
                        <a href="${base}/b2b/product/list/0.jhtml?brandId=${brands.id}">
                            <div class="shade-bg"></div>
                            <img src="${brands.logo}" alt="${brands.name}"/>
                        </a>
                    </li>
                [/#list]
            </ul>
        </div>
    </div>
[/#if]
    <!--品牌推荐end-->
    <!--楼层start-->
    <div class="floor container" id="floor_container">
    [#assign number=1]
    [#list rootCategory as category]
        [@product_list productCategoryId=category.id count = 4 areaId=area.id tagIds=21]
            [#if products?size gt 0]
                <div id="${category_index+1}F" class="allFloor" [#if c=="false"]style="display:none;"[/#if]>
                    <div class="allFloorNav">
                        <div class="left">
                            <h2>${number}F<span>${category.name}</span></h2>
                            [#assign number=number+1]
                        </div>
                        <div class="content"><a href="${base}/b2b/product/list/${category.id}.jhtml">更多>></a></div>
                    </div>
                    <div class="allFloorShow">
                        <div>
                            [@ad_position id=position[category_index] areaId=area.id  count=1/]
                            <dl style="height:180px;">
                                <dt>分类</dt>
                                [#list category.children as category_children]
                                    [#list category_children.tags as tag]
                                        [#if tag.id==12]
                                            <dd>
                                                <a href="${base}/b2b/product/list/${category_children.id}.jhtml">${category_children.name}</a>
                                            </dd>
                                        [/#if]
                                    [/#list]
                                [/#list]
                            </dl>
                        </div>
                        <ul>
                            [@product_list productCategoryId=category.id count = 4 areaId=area.id tagIds=21]
                                [#list products as products]
                                    <li>
                                        <a class="img-btn" href="${base}/b2b/product/detail/${products.id}.jhtml">
                                            <div class="img-con">
                                                <div class="img-mask"></div>
                                                <img src="${products.image}"/>
                                            </div>
                                        </a>
                                        <h2>
                                            <a href="${base}/b2b/product/detail/${products.id}.jhtml">${products.fullName}</a>
                                        </h2>
                                        <dl>
                                            [#if member??]
                                                <dd style="height:45px;">
                                                    建议零售价：<em
                                                        style="color:gray;font-size:15px;text-decoration:line-through;font-weight:normal;">￥${products.marketPrice}</em></br>
                                                    销售价：<em>￥${products.price}</em>
                                                </dd>
                                            [#else]
                                                <dd>
                                                    建议零售价：<em>￥${products.marketPrice}</em>
                                                </dd>
                                            [/#if]
                                            <dd><b>已售：${products.sales}</b>
                                                <span>
                                                    [#if member??&&member.favoriteProducts?has_content]
                                                        [#list member.favoriteProducts as product]
                                                            [#if product.id==products.id]已[/#if]
                                                        [/#list]
                                                    [/#if]收 藏：
                                                    <i onclick="addFavorite(${products.id},this)"
                                                        [#if member??&&member.favoriteProducts?has_content]
                                                            [#list member.favoriteProducts as product]
                                                       [#if product.id==products.id]class="save_product"[/#if]
                                                            [/#list]
                                                        [/#if]>
                                                    </i>
                                                </span>
                                            </dd>
                                        </dl>
                                    </li>
                                [/#list]
                            [/@product_list]
                        </ul>
                        [#if versionType==0]
                            <dl>
                                <dt>热销商家</dt>
                                [#if category.products?has_content]
                                    [#list category.products as products]
                                        [#list products.tags as tag]
                                            [#if tag.id==5 ]
                                                <dd>
                                                    <p>${products.tenant.name}</p>
                                                    <div>
                                                        <span>特色</span>
                                                        <a href="javascript:;">皮具皮包</a>
                                                    </div>
                                                    <span>满意度：${products.tenant.score}</span>
                                                    <span>月销量：${products.tenant.monthSales}</span>
                                                </dd>
                                            [/#if]
                                        [/#list]
                                    [/#list]
                                [/#if]
                            </dl>
                        [/#if]

                        [#if versionType==1]
                            <div class="dl">
                                <div class="dt">推荐品牌</div>
                                <div class="brandWrapper">
                                    <div class="brandWrapperLeft li"><</div>
                                    <div class="brandWrapperRight li">></div>
                                    <div>
                                        <ul>
                                            [#if category.brands?has_content]
                                                [#list category.brands as brand]
                                                    [#list brand.tags as tag]
                                                        [#if tag.id==24]
                                                            <li>
                                                                <a href="${base}/b2b/product/list/0.jhtml?brandId=${brand.id}">
                                                                    <img src="${brand.logo}" alt="${brand.name}"/>
                                                                </a>
                                                            </li>
                                                        [/#if]
                                                    [/#list]
                                                [/#list]
                                            [/#if]
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        [/#if]
                    </div>
                </div>
            [/#if]
        [/@product_list]
    [/#list]
    </div>
    <!--楼层end-->
    <!--标语start-->
[#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->
<script type="text/javascript">
    //初始化
    var size = $(".topWrapperSwiper li").size();
    for (var i = 1; i <= size; i++) {
        var span = "<span>" + i + "</span>";
        $(".pagination").append(span);
    }
    $(function () {
        //手动控制轮播
        $(".topWrapperSwiper li").eq(0).show();
        $(".pagination span").eq(0).addClass("active");
        $(".pagination span").mouseover(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var index = $(this).index();
            i = index;
            //alert(index)
            $(".topWrapperSwiper li").eq(index).stop().fadeIn(300).siblings().stop().fadeOut(300);
        });

        //自动轮播
        var i = 0;
        var t = setInterval(move, 3000);
        //核心向左运动函数
        function moveL() {
            i--;
            if (i == -1) {
                i = size - 1;
            }
            //alert(i);
            $(".pagination span").eq(i).addClass("active").siblings().removeClass("active");
            $(".topWrapperSwiper li").eq(i).fadeIn(300).siblings().fadeOut(300);
        }

        //核心向右运动函数
        function move() {
            i++;
            if (i == size) {
                i = 0;
            }
            //alert(i);
            $(".pagination span").eq(i).addClass("active").siblings().removeClass("active");
            $(".topWrapperSwiper li").eq(i).fadeIn(300).siblings().fadeOut(300);
        }

        //左边按钮点击事件
        $(".topWrapper .topWrapperLeft").click(function () {
            //alert(1234);
            moveL();
        });
        //右边按钮点击事件
        $(".topWrapper .topWrapperRight").click(function () {
            //alert(5234);
            move();
        });

        //定时器的开始与结束
        $(".topWrapper").hover(function () {
            clearInterval(t)
        }, function () {
            t = setInterval(move, 2000);
        });

    })
</script>
<script type="text/javascript" src="${base}/resources/b2b/js/jquery.carouFredSel-6.0.4-packed.js"></script>
<script type="text/javascript">
    //4图联动 小轮播
    $(function () {

        $('.bottomWrapper ul').carouFredSel({
            prev: '.bottomWrapperLeft',
            next: '.bottomWrapperRight',
            //pagination: "#pager",
            scroll: 1000
        });

    });
</script>

<script type="text/javascript">
    //6图联动 推荐品牌轮播
    $(function () {

        $('.brandWrapper ul').carouFredSel({
            prev: '.brandWrapperLeft',
            next: '.brandWrapperRight',
            //pagination: "#pager",
            scroll: 2000
        });
        console.log("13123123");
        $('.brandWrapper .li').css("display", "none");

        $(".brandWrapper").mouseover(function () {
            console.log("mouseover");
            $('.brandWrapper .li').show();
        });
        $(".brandWrapper").mouseout(function () {
            console.log("mouseout");
            $('.brandWrapper .li').hide();
        });

    });
</script>

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--左侧悬浮框 start-->
[#include "/b2b/include/hover_left.ftl"/]
<!--左侧悬浮框end-->
<script type="text/javascript">
    //左悬浮栏及其定位到楼层
    $('.elevator ul li').click(function () {
        var ind = $(this).attr("number");
        var topVal = $('#' + ind + 'F').offset().top;
        $('body,html').animate({scrollTop: topVal}, 1000)
    });

    //$(window).scroll(scrolls);
    //scrolls();
    function scrolls() {
        var fixRight = $('.elevator ul li');
        var sTop = $(window).scrollTop();
        var i = 0;
    [#list rootCategory as category]
        [#assign e="false"]
        [#list category.products as product]
            [#list product.tags as tag]
                [#if tag.id=5]
                    [#assign e="true"]
                [/#if]
            [/#list]
        [/#list]
        [#if e=="true"]
            var f${category_index+1} = $('#${category_index+1}F').offset().top;
            if (sTop >= f${category_index+1} - 120) {
                fixRight.eq(i).addClass('hover').siblings().removeClass('hover');
                i++;
            }
        [/#if]
    [/#list]
    }
</script>
<!--右侧悬浮框 start-->
[#include "/b2b/include/hover_right.ftl"/]
<!--右侧悬浮框end-->
</body>
</html>
