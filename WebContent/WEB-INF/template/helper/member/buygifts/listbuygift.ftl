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
                        <dd class="app-intro" id="app_desc">活动商品</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">

                [@helperRole url="helper/member/buygifts/listbuygift.jhtml" ]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="on" hideFocus="" href="javascript:">活动商品</a></li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/buygifts/listgift.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a hideFocus="" href="${base}/helper/member/buygifts/listgift.jhtml">赠品管理</a></li>
                    [/#if]
                [/@helperRole]
                </ul>

            </div>
            <form id="listForm" action="listbuygift.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <input type="hidden" id="status" name="status" value="${status}"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/buygifts/listbuygift.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="${base}/helper/member/buygifts/listproduct.jhtml"
                               class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]

                    [@helperRole url="helper/member/buygifts/listbuygift.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButtonAll" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="${base}/helper/member/buygifts/listbuygift.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="pageSizeSelect" class="button">
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
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th>活动名称</th>
                            <th>商品名称</th>
                            <th>价格</th>
                            <th>原价</th>
                            <th>买赠</th>
                            <th>赠品</th>
                            <th>限购</th>
                            <th><a href="javascript:;" class="sort" name="beginDate">开始时间</a></th>
                            <th><a href="javascript:;" class="sort" name="endDate">结束时间</a></th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list promotion as promo]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${promo.id}"/>
                            </td>
                            <td>
                            ${promo.name}
                            </td>
                            <td>
                            ${promo.fullName}
                            </td>
                            <td style="color:red;">
                            ${promo.price}
                            </td>
                            <td>
                                <del>${promo.marketPrice}</del>
                            </td>
                            <td>
                            ${promo.name}
                            </td>
                            <td>
                            ${promo.giftFullName}
                            </td>
                            <td>
                            ${promo.maximumQuantity}
                            </td>
                            <td>
                                [#if promo.beginDate??&&promo.beginDate?has_content]
                                    <span title="${promo.beginDate?string("yyyy-MM-dd HH:mm")}">${promo.beginDate?string("yyyy-MM-dd HH:mm")}</span>
                                [/#if]
                            </td>
                            <td>
                                [#if promo.endDate??&&promo.endDate?has_content]
                                    <span title="${promo.endDate?string("yyyy-MM-dd HH:mm")}">${promo.endDate?string("yyyy-MM-dd HH:mm")}</span>
                                [/#if]
                            </td>
                            <td>

                                [@helperRole url="helper/member/buygifts/listbuygift.jhtml" type="share"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="javascript:;" thumbnail="${promo.thumbnail}" description="${promo.fullName}"
                                           onclick=share(${promo.productId},$(this).attr("thumbnail"),$(this).attr("description"))>分享</a>
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

            <!--share begin -->
            <script>
                var jiathis_config;
                function share(id, thumbnail, description) {
                    jiathis_config = {
                        url: "${url}".toString().replace("ID", id),
                        pic: thumbnail,
                        title: "${title}",
                        summary: description
                    };
                    $(".jiathis_button_weixin").click();
                    $("#jiathis_weixin_tip a").remove();
                }
            </script>
            <div id="ckepop" style="display: none;">
                <span class="jiathis_txt">分享到：</span>
                <a class="jiathis_button_weixin">微信</a>
                <a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jiathis_separator jtico jtico_jiathis"
                   target="_blank">更多</a>
                <a class="jiathis_counter_style"></a></div>
            <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1" charset="utf-8"></script>
        </div>
        <br/>
        <!--share end -->

    </div>
</div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
