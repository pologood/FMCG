<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-二级分类专区</title>
    <meta name="keywords" content="${setting.siteName}-二级分类专区"/>
    <meta name="description" content="${setting.siteName}-二级分类专区"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/data/data.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>

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
    $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'none');
    $('.allCategorys').hover(function () {
        $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'block');
    }, function () {
        $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'none');
    });

    $('.menuNav'+[#if versionType==0]'Tiaohuo'+[/#if]' ul>li').hover(function () {
        var eq = $('.menuNav'+[#if versionType==0]'Tiaohuo'+[/#if]' ul>li').index(this),//获取当前滑过是第几个元素
                h = $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).offset().top,//获取当前下拉菜单距离窗口多少像素
                s = $(window).scrollTop(),//获取游览器滚动了多少高度
                i = $(this).offset().top,//当前元素滑过距离窗口多少像素
                item = $(this).children('.menuNavList').height(),//下拉菜单子类内容容器的高度
                sort = $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).height();//父类分类列表容器的高度

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
        initCategoryes();
        getInitCategorye();

        function getInitCategorye() {
            var _productCategoryTagId = '${productCategoryTagId}';
            var _html = "<span ";

            if (_productCategoryTagId == "") {
                _html += " class='on'";
            } else {
                _html += " onclick='clearbrand_price(" + _productCategoryTagId + ");'";
            }
            _html += ">不限</span>";
            for (var i = 0; i < categoryes.length; i++) {
                var first_list = categoryes[i];
                _html += "<span onclick='getPrice(this)' id='" + first_list.id + "'";

                if (_productCategoryTagId == first_list.id + "") {
                    _html += " class='on'";
                }

                _html += " >" + first_list.name + "</span>";
            }
            $("#get_init_categorye").html(_html);
        }

        //$("#current").text(str);
        $(".more").click(function(){
            var $this = $(this);
            if($this.find("a").text()=="展开"){
                $this.prev().removeClass("height");
                $this.find("a").text("隐藏");
                $this.find("i").text("∧");
            }else{
                $this.prev().addClass("height");
                $this.find("a").text("展开");
                $this.find("i").text("∨");
            }
        });
    });
    //根据排序类型分页加载数据
    function product_order(obj) {

        $("#orderType").val(obj);
        $("#listForm").submit();
    }
    //根据价格分页加载数据
    function getPrice(obj) {
        $(obj).addClass("on");
        $(obj).siblings().removeClass("on");
        $("input[name='productCategoryTagId']").val($(obj).attr("id"));
        //$("#endPrice").val($(obj).attr("ep"));
        $("#listForm").submit();
    }
    //根据品牌分页加载数据
    function getBrand(obj) {
        $(obj).addClass("on");
        $(obj).siblings().removeClass("on");
        $("input[name='brandId']").val($(obj).attr("id"));
        $("#_keyword").val("");
        $("#listForm").submit();
    }
    //设置商品数量
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

    //展开/隐藏
    function show_hidden(id) {
        var _text = $("#open" + id).text();

        if (_text == '展开') {
            $("." + id).addClass("extend");
            $("#open" + id).html("隐藏");
            $("#up" + id).hide();
            $("#down" + id).show();
        } else if (_text == '隐藏') {
            $("." + id).removeClass("extend");
            $("#open" + id).html("展开");
            $("#up" + id).show();
            $("#down" + id).hide();
        }
    }

    function clearbrand_price(id) {
        var _brandId = "${(brand.id)!}";
        var _productCategoryTagId = "${(productCategoryTagId)!}";
        var _src = "";
        if (_brandId != "" && _brandId == id) {
            if (_productCategoryTagId != "") {
                _src = "?productCategoryTagId=" + _productCategoryTagId;
            }
        }

        if (_productCategoryTagId != "" && _productCategoryTagId == id) {
            if (_brandId != "") {
                _src = "?brandId=" + _brandId;
            }
        }
        location.href = "${base}/b2b/product/list/${(productCategory.id)!}.jhtml" + _src;
    }

</script>
<!--主页内容区start-->
<div class="paper">
    <form id="listForm" action="${base}/b2b/product/list/${(productCategory.id)!}.jhtml" method="get">
        <input type="hidden" name="productCategoryId" value="${(productCategory.id)!}"/>
        <input type="hidden" name="brandId" value="${(brand.id)!}"/>
        <input type="hidden" name="productCategoryTagId" value="${(productCategoryTagId)!}"/>
        <input type="hidden" name="pageSize" value="15"/>
        <input type="hidden" id="_keyword" name="keyword" value="${keyword}"/>
        <input type="hidden" id="orderType" name="orderType" value="weight"/>
        <div class="pc-bg twoCategory-h">
            <div class="container">
                <div class="span16">
                    <a href="${base}/b2b/product/list/0.jhtml">所有类目</a>

                [#if productCategory??]
                    <span> ></span>
                    [#if productCategory.parent??]
                        [#if productCategory.parent.parent??]
                            <a href="${base}/b2b/product/list/${(productCategory.parent.parent.id)!}.jhtml">${productCategory.parent.parent.name}</a>
                            <span> ></span>
                        [/#if]
                        <a href="${base}/b2b/product/list/${(productCategory.parent.id)!}.jhtml">${productCategory.parent.name}</a>
                        <span> ></span>
                    [/#if]
                    <a href="${base}/b2b/product/list/${(productCategory.id)!}.jhtml"
                       class="on">${productCategory.name}</a>
                [/#if]
                [#if brand??&&brand?has_content]
                    <a href="javascript:;" class="selected">
                        品牌：<span class="color">${brand.name}</span>
                        <em class="color" onclick="clearbrand_price(${brand.id});">X</em>
                    </a>
                [/#if]

                [#if productCategoryTagId??]
                    <a href="javascript:;" class="selected">
                        分类：<span class="color">${productCategorys.name}</span>
                        <em class="color" onclick="clearbrand_price(${productCategoryTagId});">X</em>
                    </a>
                [/#if]

                [#if brand??||productCategoryTagId??]
                    <a href="${base}/b2b/product/list/${(productCategory.id)!}.jhtml">清空筛选</a>
                [/#if]
                </div>
                <div class="f-right">
                    <div class="twoCategory-h-right">
                        <span>共找到<span class="color">${page.total}</span>件相关商品</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="twoCategory-option">
                [#--<div class="brand">--]
                    [#--<div class="left"><h5>品牌：</h5></div>--]
                    [#--<div class="right height">--]
                        [#--<span [#if !brand??]class="on"[#elseif brand??&&brand?has_content]--]
                              [#--onclick="clearbrand_price(${brand.id});"[/#if]>不限</span>--]
                    [#--[#if brands??&&brands?has_content]--]
                        [#--[#list brands as bra]--]
                            [#--<span onclick="getBrand(this)" id="${bra.id}"--]
                                  [#--[#if brand??&&bra.id==brand.id]class="on"[/#if]>${bra.name}</span>--]
                        [#--[/#list]--]
                    [#--[/#if]--]
                    [#--</div>--]
                    [#--<div class="more">--]
                        [#--<a href="javascript:;">展开</a>--]
                        [#--<i>∨</i>--]
                    [#--</div>--]
                [#--</div>--]
            [#if productCategory??&&productCategory?has_content]
                [#if productCategory.children??&&productCategory.children?has_content]
                    <div class="price">
                        <div class="left"><h5>分类：</h5></div>
                        <div class="right height">
                            <span
                                [#if !productCategoryTagId??]class="on" [#elseif productCategoryTagId??&&productCategoryTagId?has_content]
                                onclick="clearbrand_price(${productCategoryTagId});"[/#if]>不限</span>
                            [#list productCategory.children as category]
                                <span onclick="getPrice(this)" id="${category.id}"
                                      [#if productCategoryTagId==category.id]class="on"[/#if]>${category.name}</span>
                            [/#list]

                        </div>
                        <div class="more">
                            <a href="javascript:;">展开</a>
                            <i>∨</i>
                        </div>
                    </div>
                [/#if]
            [#else ]
                <div class="price">
                    <div class="left"><h5>分类：</h5></div>
                    <div class="right height" id="get_init_categorye">
                    </div>
                    <div class="more">
                        <a href="javascript:;">展开</a>
                        <i>∨</i>
                    </div>
                </div>
            [/#if]
            </div>

            <div class="pc-bg sort clearfix">
                <div class="ul">
                    <a class="li[#if orderType=='weight'] curr [/#if]" href="javascript:product_order('weight');" >综合排序</a>
                    <a class="li [#if orderType=='dateDesc'] curr [/#if]" href="javascript:product_order('dateDesc');" >上架时间<i>∨</i></a>
                    <a class="li [#if orderType=='salesDesc'] curr [/#if]" href="javascript:product_order('salesDesc');" >销量<i>∨</i></a>
                    <a class="li [#if orderType=='priceAsc'||orderType=='priceDesc'] curr [/#if]" href="javascript:product_order('${price}');" >价格<i>[#if price=='priceAsc']∧[#elseif price=='priceDesc']∨[/#if]</i></a>
                </div>

                <div class="f-right sort-num">
                    <span class="text"><span>${page.pageNumber}</span>/<span>${page.totalPages}</span></span>
                    <!--a class="up disabled" href="javascript:;"> <</a>
                    <a class="down" href="javascript:;"> ></a-->
                </div>
            </div>

            <div class="list-show">
                <ul>
                [#if page??&&page.content?has_content]
                    [#list page.content as products]
                        <li>
                            <a href="${base}/b2b/product/detail/${products.id}.jhtml"><img src="${products.thumbnail}"/></a>
                            <h2>
                                <a href="${base}/b2b/product/detail/${products.id}.jhtml">${products.fullName}</a>
                            </h2>
                            <dl>
                                [#if member??]
                                <dd>建议零售价：<span style="color:gray;font-size:15px;text-decoration:line-through;font-weight:normal;">￥${products.marketPrice}</span></dd>
                                <dd>批发价：<span>￥${products.price}</span></dd>
                                [#else]
                                <dd>建议零售价：<span>￥${products.marketPrice}</span></dd>
                                [/#if]
                                <dd>
                                    <div onclick="set_number(this)" id="minus">-</div>
                                    <input type="text" name="number" value="1"/>
                                    <div onclick="set_number(this)" id="plus">+</div>
                                    <i onclick="addcart(${products.id},this)"></i>
                                </dd>
                            </dl>
                            <div>已售：${products.sales}</div>
                            <div>
                                <span>
                                    [#if member??]
                                        [#list member.favoriteProducts as product]
                                            [#if product.id==products.id]已[/#if]
                                        [/#list]
                                    [/#if]
                                    收 藏
                                </span>：
                                <i onclick="addFavorite(${products.id},this)"
                                    [#if member??]
                                        [#list member.favoriteProducts as product]
                                            [#if product.id==products.id]class="save_product"[/#if]
                                        [/#list]
                                    [/#if]>
                                </i>
                            </div>
                        </li>
                    [/#list]
                [#else]
                    <div style="text-align:center;margin-top:200px;">暂无数据</div>
                [/#if]
                </ul>
            </div>
            <div class="page-wrap clearfix" >
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                [#include "/b2b/include/pagination.ftl"]
            [/@pagination]
            </div>
        </div>
    </form>
    <!--标语start-->
[#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
[#include "/b2b/include/hover_right.ftl"/]
<!--右侧悬浮框end-->
</body>
</html>