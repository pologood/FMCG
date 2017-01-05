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
    [#--<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>--]
    [#--<link href="${base}/resources/helper/css/account.css" type="text/css" rel="stylesheet"/>--]
    [#--<link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>--]
    [#--<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>--]

    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
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
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                我的商品
                <small>查询我发布的宝贝，维护当前售价及库存状态等。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/discount/listproduct.jhtml">营销工具</a></li>
                <li class="active">添加限时折扣</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom" style="box-shadow: none; margin-bottom: 0;">
                    <div class="row mtb10">
                        <form id="listForm" action="listproduct.jhtml" method="get">
                            <input type="hidden" id="productCategoryId" name="productCategoryId"
                                   value="${productCategoryId}"/>
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
                        <div class="col-sm-7">
                            <div class="btn-group">
                                <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
                                        class="fa fa-refresh mr5" aria-hidden="true"></i> 刷新
                                </button>
                            [#if type!='gift']
                                <div class="dropdown fl ml5">
                                    <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                            id="dropdownMenu1" data-toggle="dropdown">
                                        商品筛选
                                        <span class="caret"></span>
                                    </button>

                                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                        id="filterOption">
                                        <li class="[#if isList?? && isList]active[/#if]">
                                            <a href="javascript:;" name="isList" val="true"[#if isList?? && isList]
                                               class="checked"[/#if]>${message("admin.product.isList")}</a>
                                        </li>
                                        <li class="[#if isList?? && !isList]active[/#if]">
                                            <a href="javascript:;" name="isList" val="false"[#if isList?? && !isList]
                                               class="checked"[/#if]>${message("admin.product.notList")}</a>
                                        </li>
                                        <li class="[#if isTop?? && isTop]active[/#if]">
                                            <a href="javascript:;" name="isTop" val="true"[#if isTop?? && isTop]
                                               class="checked"[/#if]>${message("admin.product.isTop")}</a>
                                        </li>
                                        <li class="[#if isTop?? && !isTop]active[/#if]">
                                            <a href="javascript:;" name="isTop" val="false"[#if isTop?? && !isTop]
                                               class="checked"[/#if]>${message("admin.product.notTop")}</a>
                                        </li>
                                        <li class="[#if isGift?? && isGift]active[/#if]">
                                            <a href="javascript:;" name="isGift" val="true"[#if isGift?? && isGift]
                                               class="checked"[/#if]>${message("admin.product.isGift")}</a>
                                        </li>
                                        <li class="[#if isGift?? && !isGift]active[/#if]">
                                            <a href="javascript:;" name="isGift" val="false"[#if isGift?? && !isGift]
                                               class="checked"[/#if]>${message("admin.product.nonGift")}</a>
                                        </li>
                                        <li class="[#if isOutOfStock?? && !isOutOfStock]active[/#if]">
                                            <a href="javascript:;" name="isOutOfStock"
                                               val="false"[#if isOutOfStock?? && !isOutOfStock]
                                               class="checked"[/#if]>${message("admin.product.isStack")}</a>
                                        </li>
                                        <li class="[#if isOutOfStock?? && isOutOfStock]active[/#if]">
                                            <a href="javascript:;" name="isOutOfStock"
                                               val="true"[#if isOutOfStock?? && isOutOfStock]
                                               class="checked"[/#if]>${message("admin.product.isOutOfStack")}</a>
                                        </li>
                                        <li class="[#if isStockAlert?? && !isStockAlert]active[/#if]">
                                            <a href="javascript:;" name="isStockAlert"
                                               val="false"[#if isStockAlert?? && !isStockAlert]
                                               class="checked"[/#if]>${message("admin.product.normalStore")}</a>
                                        </li>
                                        <li class="[#if isStockAlert?? && isStockAlert]active[/#if]">
                                            <a href="javascript:;" name="isStockAlert"
                                               val="true"[#if isStockAlert?? && isStockAlert]
                                               class="checked"[/#if]>${message("admin.product.isStockAlert")}</a>
                                        </li>
                                    </ul>

                                </div>
                            [/#if]
                            [#if productCategoryTenants??&&productCategoryTenants?has_content]
                                <div class="dropdown fl ml5">
                                    <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                            id="dropdownMenu1" data-toggle="dropdown">
                                        商品分类
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                        id="product_category_tenant">
                                        [#list productCategoryTenants as productCategoryTenant]
                                            <li class="[#if productCategoryTenantId==productCategoryTenant.id]active[/#if]">
                                                <a href="javascript:;" name="productCategoryTenantId"
                                                   val="${productCategoryTenant.id}" [#if productCategoryTenantId==productCategoryTenant.id]
                                                   class="checked"[/#if]>${productCategoryTenant.name}</a>
                                            </li>
                                        [/#list]
                                    </ul>
                                </div>
                            [/#if]
                                <div class="dropdown fl ml5">
                                    <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                            id="dropdownMenu1"
                                            data-toggle="dropdown">
                                        每页显示<span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                        id="pageSizeOption">
                                        <li role="presentation" class="[#if page.pageSize==10]active[/#if]">
                                            <a role="menuitem" tabindex="-1" val="10">10</a>
                                        </li>
                                        <li role="presentation" class="[#if page.pageSize==20]active[/#if]">
                                            <a role="menuitem" tabindex="-1" val="20">20</a>
                                        </li>
                                        <li role="presentation" class="[#if page.pageSize==30]active[/#if]">
                                            <a role="menuitem" tabindex="-1" val="30">30</a>
                                        </li>
                                        <li role="presentation" class="[#if page.pageSize==40]active[/#if]">
                                            <a role="menuitem" tabindex="-1" val="40">40</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-5">
                            <div class="box-tools fr">
                                <div class="input-group input-group-sm" style="width: 150px;">
                                    <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" class="form-control pull-right"
                                           placeholder="搜索商品名称、货号">
                                    <ul id="searchPropertyOption" style="display:none;">
                                        <li>
                                            <a style="cursor: pointer;" val="name"></a>
                                        </li>
                                    </ul>
                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-body">
                        <table id="listTable" class="table table-bordered table-hover dataTable">
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
                                            <span[#if product.isOutOfStock]
                                                    class="red"[/#if]>${product.stock}</span>
                                        [#else]
                                            <span[#if product.isOutOfStock] class="red"[/#if]
                                                                            title="${message("Product.allocatedStock")}: ${product.allocatedStock}">${product.stock}</span>
                                        [/#if]
                                    [/#if]
                                </td>
                                <td>
                                    <a href="${base}/store/member/discount/edit.jhtml?id=${product.id}">[操作]</a>
                                </td>
                            </tr>
                        [/#list]
                        </table>
                        <div class="dataTables_paginate paging_simple_numbers" style="margin-top: 10px;">
                        [#if !page.content?has_content]
                            <p class="nothing">${message("box.member.noResult")}</p>
                        [/#if]
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                            [#include "/store/member/include/pagination.ftl"]
                        [/@pagination]
                        </div>
                    </form>

                </div>
            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
