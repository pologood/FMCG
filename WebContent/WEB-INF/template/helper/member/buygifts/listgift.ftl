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
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <style type="text/css">
        #listTable th, #listTable td {
            /*padding-left: 5px;*/
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
        });

        function deleteGift(id) {
            $.ajax({
                url: "${base}/helper/member/buygifts/delete_gift.jhtml",
                type: "POST",
                dataType: "json",
                data: {
                    id: id
                },
                success: function (message) {
                    if (message.type == "success") {
                        location.reload();
                    }
                }
            });
        }
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/buygifts.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">买赠搭配</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">赠品管理</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">

                [@helperRole url="helper/member/buygifts/listbuygift.jhtml" ]
                    [#if helperRole.retUrl!="0"]
                        <li><a hideFocus="" href="${base}/helper/member/buygifts/listbuygift.jhtml">活动商品</a></li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/buygifts/listgift.jhtml" ]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="on" hideFocus="" href="javascript">赠品管理</a></li>
                    [/#if]
                [/@helperRole]
                </ul>

            </div>
            <form id="listForm" action="listgift.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <input type="hidden" id="status" name="status" value="${status}"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/buygifts/listgift.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="${base}/helper/member/buygifts/listproduct.jhtml?productCategoryTenantId=${tenantId}&isList=true&isGift=false&type=gift"
                               class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]
                    [@helperRole url="helper/member/buygifts/listgift.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="${base}/helper/member/buygifts/listgift.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <!--div class="menuWrap">
                            <a style="cursor: pointer;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
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
                        </div-->
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th>商品名称</th>
                            <th>标价</th>
                            <th>原价</th>
                            <th><a href="javascript:;" class="sort" name="priceExpression">总销量</a></th>
                            <th><a href="javascript:;" class="sort" name="beginDate">库存</a></th>

                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as promo]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${promo.id}"/>
                            </td>
                            <td>
                            ${promo.fullName}
                            </td>
                            <td>
                                <del>${currency(promo.marketPrice,true)}</del>
                            </td>
                            <td style="color:red;">
                                ${currency(promo.price,true)}
                            </td>
                            <td >
                                ${promo.sales}
                            </td>
                            <td>
                                [#if promo.stock??]
                                    [#if promo.allocatedStock == 0]
                                        <span[#if promo.isOutOfStock] class="red"[/#if]>${promo.stock}</span>
                                    [#else]
                                        <span[#if promo.isOutOfStock] class="red"[/#if]
                                                                        title="${message("Product.allocatedStock")}: ${promo.allocatedStock}">${promo.stock}</span>
                                    [/#if]
                                [/#if]
                            </td>

                            <td>

                                [@helperRole url="helper/member/buygifts/listgift.jhtml" type="del"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="javascript:deleteGift('${promo.id}') ;">删除</a>
                                    [/#if]
                                [/@helperRole]
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("helper.member.noResult")}</p>
                [/#if]
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/helper/include/pagination.ftl"]
                [/@pagination]
                </div>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
