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
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

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

            <div class="mainbox member" style="position:static;">
                <div class="manage-list">
                    <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                        <div class="js-app-header title-wrap" id="app_0000000844">
                            <a href="${base}/helper/${tenant.id}/index.jhtml">
                                <img class="js-app_logo app-img" src="${tenant.thumbnail}"/></a>
                            <dl class="app-info">
                                <dt class="app-title" id="app_name">
                                    <a href="${base}/helper/${tenant.id}/index.jhtml">${tenant.shortName}</a>
                                </dt>
                                <dd class="app-status" id="app_add_status"></dd>
                            </dl>
                        </div>
                        <ul class="links" id="mod_menus">
                            <li>
                                <a class="on" hideFocus="" href="${base}/helper/${tenant.id}/index.jhtml">我的店铺</a></li>
                        </ul>
                    </div>
                    <div class="shop_info_left_div">
                        <div class="circle_div">
                            <h2 class="tit02">我的关注</h2>
                            <h1 class="tit01">${FavoriteCount}</h1>
                            <a class="btn01" href="${base}/helper/member/relation/list.jhtml?status=success">关注我的买家</a>
                            <p class="p01">
                            [#list MemberRanks as memberRank]
                                <a href="${base}/helper/member/relation/list.jhtml?memberRankId=${memberRank.id}&status=success">${memberRank.name}
                                    ([#if MemberCounts.get(memberRank)??]${MemberCounts.get(memberRank)}[#else]
                                        0[/#if])</a>
                            [/#list]
                            </p>
                        </div>
                        <div class="circle_div">
                            <h2 class="tit02">我的订单</h2>
                            <h1 class="tit01 tit03">${notCompleted!0}/${allCount!0}</h1>
                            <a class="btn01"
                               href="${base}/helper/member/trade/list.jhtml?orderStatus=confirmed&shippingStatus=unshipped">未完成单量</a>/
                            <a class="btn01" href="${base}/helper/member/trade/list.jhtml?orderStatus=completed">总单量(不包含已取消)</a>
                            <p class="p01 p02">
                                <a href="${base}/helper/member/trade/list.jhtml?orderStatus=confirmed&shippingStatus=unshipped">待发货(${waitShipping})</a>
                                <a href="${base}/helper/member/trade/list.jhtml?paymentStatus=unpaid">待付款(${waitPayment})</a>
                                <a href="${base}/helper/member/trade/list.jhtml?orderStatus=confirmed&shippingStatus=shipped">已发货(${waitSigned})</a>
                                <a href="${base}/helper/member/trade/list.jhtml?orderStatus=confirmed&paymentStatus=refundApply&shippingStatus=shippedApply">退货中(${waitReturn})</a>
                            </p>
                        </div>
                        <div class="circle_div">
                            <h2 class="tit02">交易统计</h2>
                            <h1 class="tit01 tit04">${currency(tenant.monthSales, true)}</h1>
                            <a class="btn01" href="javascript:;">本月交易额</a>
                            <p class="p01 p02">
                                <a href="${base}/helper/member/trade/list.jhtml?orderStatus=completed&paymentStatus=paid">查看所有交易</a>
                            </p>
                        </div>
                    </div>
                    <div class="shop_info_right_div">
                        <div class="shop_help_div">
                            <h3>公告</h3>
                        [@article_list articleCategoryId = 16 count = 8]
                            [#list articles as article]
                                <p>
                                    <a href="${base}/helper/article/content/${article.id}.jhtml"
                                       title="${article.title}" target="_blank">
                                        <span>.</span>${abbreviate(article.title, 30)}</a>
                                </p>
                            [/#list]
                        [/@article_list]
                        </div>
                        <div class="shop_help_div">
                            <h3>新手帮助</h3>
                        [@article_list articleCategoryId = 60 count = 8]
                            [#list articles as article]
                                <p>
                                    <a href="${base}/helper/article/content/${article.id}.jhtml"
                                       title="${article.title}" target="_blank">
                                        <span>.</span>${abbreviate(article.title, 30)}</a>
                                </p>
                            [/#list]
                        [/@article_list]
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
