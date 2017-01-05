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
    <link href="${base}/resources/helper/css/marketing.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">

        $().ready(function () {

            var $listForm = $("#listForm");
            var $status = $("#status");
            var $statusSelect = $("#statusSelect");
            var $statusOption = $("#statusOption a");

        [@flash_message /]

            $statusSelect.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });

            $statusOption.click(function () {
                var $this = $(this);
                $status.val($this.attr("val"));
                $listForm.submit();
                return false;
            });

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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/coupon.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">红包</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">管理我的店铺红包。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                [#--<li><a class="" hideFocus="" href="${base}/helper/member/coupon/list.jhtml?type=tenantCoupon&status=${status}">代金券</a></li>--]
                    <li><a [#if type=="send"]class="on"[/#if] hideFocus=""
                           href="${base}/helper/member/red/sumer.jhtml?id=${id}&type=send&status=${status}">领用详情</a>
                    </li>
                    <li><a [#if type=="used"]class="on"[/#if] hideFocus=""
                           href="${base}/helper/member/red/sumer.jhtml?id=${id}&type=used&status=${status}">使用详情</a>
                    </li>
                </ul>

            </div>
            <form id="listForm" action="sumer.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <input type="hidden" id="id" name="id" value="${id}"/>
                <input type="hidden" id="status" name="status" value="${status}"/>
                <div class="bar">
                    <div class="buttonWrap">
                        <a href="${base}/helper/member/red/list.jhtml?type=tenantBonus&status=${status}"
                           id="returnButton" class="iconButton backButton">
                            返回
                        </a>
                        <a href="javascript:;" id="refreshButton" class="iconButton">
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
                    <span class="sumer">总[#if type=="send"]领取[#else]使用[/#if]量：${total.sumerCount} ， 总[#if type=="send"]
                        领取[#else]使用[/#if]人数：${total.sumerNumber}</span>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                    [#--<tr>--]
                    [#--<td colspan="2">总领取量：${total.sumerCount} ， 总领取人数：${total.sumerNumber}</td>--]
                    [#--</tr>--]
                        <tr>
                            <th>
                                <a href="javascript:;" class="sort" name="sumerDate">日期</a>
                            </th>
                            <th>
                            [#if type=="send"]领取数量[#elseif type=="used"]使用数量[/#if]
                            </th>
                            <th>
                            [#if type=="send"]领取人数[#elseif type=="used"]使用人数[/#if]
                            </th>
                            <th>
                                操作
                            </th>
                        </tr>
                    [#list page.content as couponSumer]
                        <tr>
                            <td>
                                <span title="${couponSumer.sumerDate?string("yyyy-MM-dd HH:mm")}">${couponSumer.sumerDate?string("yyyy-MM-dd HH:mm")}</span>
                            </td>
                            <td>
                            ${couponSumer.sumerCount}
                            </td>
                            <td>
                            ${couponSumer.sumerNumber}
                            </td>
                            <td>
                                <a href="${base}/helper/member/statistics/sale_total.jhtml?couponId=${id}">[查看]</a>
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("box.member.noResult")}</p>
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
