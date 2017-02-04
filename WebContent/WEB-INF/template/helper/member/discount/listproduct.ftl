<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/account.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <style type="text/css">
        #listTable th,#listTable td{
            /*padding-left: 5px;*/
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
        });
    </script>
</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">


            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/upload3.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的商品</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">查询我发布的宝贝，维护当前售价及库存状态等。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li>
                        <a class="on" hideFocus="" href="${base}/helper/member/discount/listproduct.jhtml">所有商品</a>
                    </li>
                </ul>
            </div>
            <form id="listForm" action="listproduct.jhtml" method="get">
                <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}"/>
                <input type="hidden" id="productCategoryTenantId" name="productCategoryTenantId"
                       value="${productCategoryTenantId}"/>
                <input type="hidden" id="brandId" name="brandId" value="${brandId}"/>
                <input type="hidden" id="promotionId" name="promotionId" value="${promotionId}"/>
                <input type="hidden" id="tagId" name="tagId" value="${tagId}"/>
                <input type="hidden" id="isMarketable" name="isMarketable"
                       value="[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isList" name="isList"
                       value="[#if isList??]${isList?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isTop" name="isTop"
                       value="[#if isTop??]${isTop?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isGift" name="isGift"
                       value="[#if isGift??]${isGift?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isOutOfStock" name="isOutOfStock"
                       value="[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isStockAlert" name="isStockAlert"
                       value="[#if isStockAlert??]${isStockAlert?string("true", "false")}[/#if]"/>
                <div class="bar">
                    <div class="buttonWrap">
                        <a href="${base}/helper/member/discount/listproduct.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="filterSelect" class="button">
                            ${message("admin.product.filter")}
                                <span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="filterOption" class="check">
                                    <li class="separator">
                                        <a href="javascript:;" name="isList" val="true"[#if isList?? && isList]
                                           class="checked"[/#if]>${message("admin.product.isList")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isList" val="false"[#if isList?? && !isList]
                                           class="checked"[/#if]>${message("admin.product.notList")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isTop" val="true"[#if isTop?? && isTop]
                                           class="checked"[/#if]>${message("admin.product.isTop")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isTop" val="false"[#if isTop?? && !isTop]
                                           class="checked"[/#if]>${message("admin.product.notTop")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isGift" val="true"[#if isGift?? && isGift]
                                           class="checked"[/#if]>${message("admin.product.isGift")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isGift" val="false"[#if isGift?? && !isGift]
                                           class="checked"[/#if]>${message("admin.product.nonGift")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isOutOfStock"
                                           val="false"[#if isOutOfStock?? && !isOutOfStock]
                                           class="checked"[/#if]>${message("admin.product.isStack")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isOutOfStock"
                                           val="true"[#if isOutOfStock?? && isOutOfStock]
                                           class="checked"[/#if]>${message("admin.product.isOutOfStack")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isStockAlert"
                                           val="false"[#if isStockAlert?? && !isStockAlert]
                                           class="checked"[/#if]>${message("admin.product.normalStore")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isStockAlert"
                                           val="true"[#if isStockAlert?? && isStockAlert]
                                           class="checked"[/#if]>${message("admin.product.isStockAlert")}</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <!--a href="javascript:;" id="moreButton" class="button">商品分类</a-->

                    [#if productCategoryTenants??&&productCategoryTenants?has_content]
                        <div class="menuWrap">
                            <a href="javascript:;" id="productCategoryTenant" class="button">
                                商品分类
                                <span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="product_category_tenant" class="check">
                                    [#list productCategoryTenants as productCategoryTenant]
                                        <li>
                                            <a href="javascript:;" name="productCategoryTenantId"
                                               val="${productCategoryTenant.id}" [#if productCategoryTenantId==productCategoryTenant.id]
                                               class="checked"[/#if]>${productCategoryTenant.name}</a>
                                        </li>
                                    [/#list]
                                </ul>
                            </div>
                        </div>
                    [/#if]
                        <div class="menuWrap">
                            <a href="javascript:;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}
                                <span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="pageSizeOption">
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="menuWrap">
                        <div class="search">
                            <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}"
                                   placeholder="搜索商品名称、货号"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <table id="listTable" class="list">
                    <tr>
                        <th>
                            <a href="javascript:;" class="sort" name="sn">${message("Product.sn")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort" name="name">${message("Product.name")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort"
                               name="productCategory">${message("Product.productCategory")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort" name="price">${message("Product.price")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort" name="stock">${message("Product.stock")}</a>
                        </th>
                        <th>
                            <span>${message("admin.common.handle")}</span>
                        </th>
                    </tr>
                [#list page.content as product]
                    <tr>
                        <td>${product.sn}</td>
                        <td>
						<span title="${product.fullName}">
							${abbreviate(product.name, 30, "..")}
                                [#if product.isGift]
                                    <span class="gray">[${message("admin.product.gifts")}]</span>
                                [/#if]
						</span>
                            [#list product.validPromotions as promotion]
                                <span class="promotion">${abbreviate(promotion.name,10,".")}</span>
                            [/#list]
                        </td>
                        <td>
                            [#if product.productCategoryTenant??]${abbreviate(product.productCategoryTenant.name,16,"..")!}[/#if]
                        </td>
                        <td>
                        ${currency(product.price)}
                        </td>
                        <td>
                            [#if product.stock??]
                                [#if product.allocatedStock == 0]
                                    <span[#if product.isOutOfStock] class="red"[/#if]>${product.stock}</span>
                                [#else]
                                    <span[#if product.isOutOfStock] class="red"[/#if]
                                                                    title="${message("Product.allocatedStock")}: ${product.allocatedStock}">${product.stock}</span>
                                [/#if]
                            [/#if]
                        </td>
                        <td>
                            <a href="${base}/helper/member/discount/edit.jhtml?id=${product.id}">[操作]</a>
                        </td>
                    </tr>
                [/#list]
                </table>
            [#if !page.content?has_content]
                <p class="nothing">${message("helper.member.noResult")}</p>
            [/#if]
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                [#include "/helper/member/include/pagination.ftl"]
            [/@pagination]
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
