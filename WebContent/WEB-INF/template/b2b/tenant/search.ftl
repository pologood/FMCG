<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-搜索页</title>
    <meta name="keywords" content="${setting.siteName}-搜索页" />
    <meta name="description" content="${setting.siteName}-搜索页" />
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/merchantHomepage.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript">
        $(function(){
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
        function searchProduct(tenantId){
            var keyWord=$("#keyWord").val();
            location.href="${base}/b2b/tenant/search.jhtml?tenantId="+tenantId+"&keyWord="+keyWord;
        }
        function getCategory(id){
            $("input[name='productCategoryTenantId']").val(id);
            $("#listForm").submit();
        }

        function product_order(orderType){
            $("input[name='orderType']").val(orderType);
            $("#listForm").submit();
        }
    </script>
</head>
<body>
<!-- 头部start -->
[#include "/b2b/include/tenant_header.ftl"]
<!--头部end-->
<!--主页内容区start-->
<div class="paper">
<form id="listForm" action="search.jhtml" method="get">
    <input type="hidden" name="tenantId" value="${tenant.id}" >
    <input type="hidden" name="keyWord" value="${keyWord}" >
    <input type="hidden" name="productCategoryTenantId" value="${(productCategoryTenant.id)!}" >
    <input type="hidden" name="orderType" value="${orderType}" >
    [#--<input type="hidden" name="brandId" value="${(brand.id)!}" >--]
[#--<div class="pc-bg twoCategory-h">--]
    [#--<div class="container">--]
        [#--<div class="span16">--]
            [#--<a href="javascript:getCategory('');">所有类目</a>--]
            [#--[#if productCategory?has_content]--]
                [#--[#if productCategory.parent?has_content]--]
                    [#--[#if productCategory.parent.parent?has_content]--]
                        [#--<span> ></span>--]
                        [#--<a href="javascript:getCategory('${(productCategory.parent.parent.id)!}');">${productCategory.parent.parent.name}</a>--]
                    [#--[/#if]--]
                    [#--<span> ></span>--]
                    [#--<a href="javascript:getCategory('${(productCategory.parent.id)!}');" [#if !productCategory.children?has_content]class="on"[/#if]>${productCategory.parent.name}</a>--]
                [#--[/#if]--]
                [#--[#if productCategory.children?has_content]--]
                    [#--<span> ></span>--]
                    [#--<a href="javascript:getCategory('${(productCategory.id)!}');" class="on">${productCategory.name}</a>--]
                [#--[/#if]--]
            [#--[/#if]--]
            [#--<span> ></span>--]
            [#--<a href="javascript:;">休闲食品</a>--]
            [#--<span> ></span>--]
            [#--<a href="javascript:;" class="on">巧克力</a>--]
            [#--[#assign hasFilter=false /]--]
            [#--<a href="javascript:;" class="selected">--]
                [#--品牌：<span class="color">玛丽</span> <em class="color">X</em>--]
            [#--</a>--]
            [#--<a href="javascript:;" class="selected">--]
                [#--价格：<span class="color">100-200</span> <em class="color">X</em>--]
            [#--</a>--]
            [#--[#if productCategory?has_content&&!productCategory.children?has_content]--]
                [#--[#assign hasFilter=true /]--]
                [#--<a href="javascript:;" class="selected">--]
                    [#--分类：<span class="color">${productCategory.name}</span> <em onclick="getCategory('${(productCategory.parent.id)!}')" class="color">X</em>--]
                [#--</a>--]
            [#--[/#if]--]
            [#--[#if hasFilter==true]--]
                [#--<a href="javascript:getCategory('${(productCategory.parent.id)!}');">清空筛选</a>--]
            [#--[/#if]--]
        [#--</div>--]
        [#--<div class="f-right">--]
            [#--<div class="twoCategory-h-right">--]
                [#--<span>共找到<span class="color">${page.total}</span>件相关商品</span>--]
                [#--<i>∧</i>--]
            [#--</div>--]
        [#--</div>--]
    [#--</div>--]
[#--</div>--]
<div class="container">
<div class="twoCategory-option">
    <!--点击展开后下面DIV添加class extend -->
    [#--<div class="brand">--]
        [#--<div class="left"><h5>品牌：</h5></div>--]
        [#--<div class="right height">--]
            [#--<span [#if !productCategory?has_content]class="on"[#elseif !productCategory.brands?seq_contains(brand)]class="on"[/#if]>不限</span>--]
            [#--[#list brands as _brand]--]
                [#--<span [#if brand==_brand]class="on"[/#if] onclick="getBrand('${_brand.id}')">${_brand.name}</span>--]
            [#--[/#list]--]
        [#--</div>--]
        [#--<div class="more">--]
            [#--<a href="javascript:;">展开</a>--]
            [#--<i>∨</i>--]
        [#--</div>--]
    [#--</div>--]
    <div class="classes">
        <div class="left"><h5>分类：</h5></div>
        <div class="right height">
            <span [#if !tenant.productCategoryTenants?seq_contains(productCategoryTenant)]class="on"[#else]onclick="getCategory('')"[/#if]>全部</span>
            [#list tenant.productCategoryTenants as category]
                <span [#if productCategoryTenant==category]class="on"[/#if] onclick="getCategory('${category.id}')">${category.name}</span>
            [/#list]
        </div>
        <div class="more">
            <a href="javascript:;">展开</a>
            <i>∨</i>
        </div>
    </div>
</div>
<div class="pc-bg sort clearfix">
    <div class="ul">
        <a class="li [#if orderType=='weight']curr[/#if] sort-synthesis" href="javascript:product_order('weight');">综合排序</a>
        <a class="li [#if orderType=='dateDesc']curr[/#if]" href="javascript:product_order('dateDesc');">上架时间<i>∨</i></a>
        <a class="li [#if orderType=='salesDesc']curr[/#if]" href="javascript:product_order('salesDesc');">销量<i>∨</i></a>
        <a class="li [#if orderType=='priceDesc'||orderType=='priceAsc']curr[/#if] sort-price" href="javascript:product_order('${price_order}');">价格<i>[#if price_order=="priceDesc"]∨[#else]∧[/#if]</i></a>
    </div>

    <div class="f-right sort-num">
        <span class="text"><span>${page.pageNumber}</span>/<span>${page.totalPages}</span></span>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                <a class="up [#if !hasPrevious]disabled[/#if]" href="javascript:[#if hasPrevious]$.pageSkip(${previousPageNumber})[/#if];"> <</a>
            <a class="down [#if !hasNext]disabled[/#if]" href="javascript:[#if hasNext]$.pageSkip(${nextPageNumber})[/#if];"> ></a>
        [/@pagination]
    </div>
</div>
<div class="list-show merchant-search">
    <ul>
        [#list page.content as product]
        <li class="item">
            <a class="img" href="${base}/b2b/product/detail/${product.id}.jhtml">
                <img src="${product.thumbnail}" width="255" height="255">
            </a>
            <h2>
                <a href="${base}/b2b/product/detail/${product.id}.jhtml" title="【${(product.tenant.name)!}】${product.fullName!}">【${(product.tenant.name)!}】${product.fullName!}</a>
            </h2>
            <div class="shopping-info">
                <span>￥${product.price}</span>
                <a href="${base}/b2b/product/detail/${product.id}.jhtml">立即购买</a>
            </div>
        </li>
        [/#list]
    </ul>
</div>
<div class="page-wrap clearfix">
[#--[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]--]
    [#include "/b2b/include/pagination.ftl"]
[#--[/@pagination]--]
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
