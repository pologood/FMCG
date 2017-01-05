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
        #bdshare_weixin_qrcode_dialog {
            box-sizing: content-box;
        }
    </style>
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
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/red/list.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="add.jhtml?status=${status}&type=${type}" class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]
                    [@helperRole url="helper/member/red/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                    <a href="${base}/helper/member/red/list.jhtml?type=tenantBonus"
                       class="iconButton">
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

                            <div class="menuWrap">
                                <a href="javascript:;" id="typeSelect" class="button">
                                    类型<span class="arrow">&nbsp;</span>
                                </a>
                                <div class="popupMenu">
                                    <ul id="type">
                                    [#list statusValues as statusValue]
                                        <li>
                                            <a href="list.jhtml?type=${type}&status=${statusValue}"
                                               [#if statusValue==status]class="current"[/#if]>${message("Coupon.Status."+statusValue)}</a>
                                        </li>
                                    [/#list]
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="menuWrap">
                        <div class="search">
                        [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
                            <input placeholder="搜索紅包名称" type="text" id="searchValue" name="searchValue"
                                   value="${page.searchValue}"
                                   maxlength="200"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                        <div class="popupMenu">
                            <ul id="searchPropertyOption">
                                <li>
                                    <a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if]
                                       val="name">名称</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="name">名称</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="amount">红包面额</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="minimumPrice">最低消费</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="count">红包数量</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="status">状态</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="startDate">开始时间</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="endDate">结束时间</a>
                            </th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as coupon]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${coupon.id}"/>
                            </td>
                            <td>
                            ${abbreviate(coupon.name,18,"..")}
                            </td>
                            <td>
                            ${coupon.amount}
                            </td>
                            <td>
                            ${coupon.minimumPrice}
                            </td>
                            <td>
                            ${coupon.count}
                            </td>
                            <td>
                            ${message("Coupon.Status."+coupon.status)}
                            </td>
                            <td>
                                <span title="${coupon.startDate?string("yyyy-MM-dd HH:mm")}">${coupon.startDate?string("yyyy-MM-dd HH:mm")}</span>
                            </td>
                            <td>
                                <span title="${coupon.endDate?string("yyyy-MM-dd HH:mm")}">${coupon.endDate?string("yyyy-MM-dd HH:mm")}</span>
                            </td>
                            <td>
                                [@helperRole url="helper/member/red/list.jhtml" type="statistics"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="sumer.jhtml?id=${coupon.id}&type=send&status=${status}">统计</a>
                                    [/#if]
                                [/@helperRole]

                                [@helperRole url="helper/member/red/list.jhtml" type="share"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="javascript:" introduction="${coupon.introduction}"
                                           onclick=share(${coupon.id},$(this).attr("introduction"))>分享</a>
                                    [/#if]
                                [/@helperRole]
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

            <!--share begin -->
            <script>
                var jiathis_config;
                function share(id, introduction) {
                    jiathis_config = {
                        url: "${url}".toString().replace("ID", id),
                        pic: "${thumbnail}",
                        title: "${title}",
                        summary: "" + introduction + ""
                    };
                    $(".jiathis_button_weixin").click();
                    //$("#jiathis_weixin_tip a").remove();
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
[#include "/helper/include/footer.ftl" /]
</body>
</html>
