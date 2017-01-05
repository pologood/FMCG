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
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/marketing.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/store/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
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
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#include "/store/member/include/bootstrap_js.ftl" /]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content-header">
            <h1>
               	营销工具 	
                <small>管理我的店铺代金券</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/coupon/list.jhtml?type=tenantCoupon">营销工具</a></li>
                <li><a href="${base}/store/member/coupon/list.jhtml?type=tenantCoupon">代金券</a></li>
                <li class="active">统计</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs pull-right">
                        <li class="[#if type ='used']active[/#if]">
                            <a href="${base}/store/member/coupon/sumer.jhtml?id=${id}&type=used&status=${status}">使用详情</a>
                        </li>
                        <li class="[#if type=="send"]active[/#if]">
                            <a href="${base}/store/member/coupon/sumer.jhtml?id=${id}&type=send&status=${status}">领用详情</a>
                        </li>
                        <li class="pull-left header"><i class="fa fa-money" ></i>代金券</li>
                    </ul>
                    <span class="sumer">总[#if type=="send"]领取[#else]使用[/#if]量：${total.sumerCount} ， 总[#if type=="send"]
                        领取[#else]使用[/#if]人数：${total.sumerNumber}</span>
                    <div class="box-body">
                    <form id="listForm" action="sumer.jhtml" method="get">
                        <input type="hidden" id="type" name="type" value="${type}"/>
                        <input type="hidden" id="id" name="id" value="${id}"/>
                        <input type="hidden" id="status" name="status" value="${status}"/>
                        <div class="list">
                            <table id="listTable" class="table table-bordered table-hover dataTable">
                                <tr>
                                    <th>
                                        日期
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
                                        <a href="${base}/store/member/statistics/sale_total.jhtml?couponId=${id}">[查看]</a>
                                    </td>
                                </tr>
                            [/#list]
                            </table>
                        [#if !page.content?has_content]
                            <p class="nothing">${message("box.member.noResult")}</p>
                        [/#if]
                            <div class="dataTables_paginate paging_simple_numbers">
                            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                                 [#include "/store/member/include/pagination.ftl"]
                            [/@pagination]
                            </div>
                        </div>
                    </form>
                    </div>
                </div>
        </section>
    </div>
[#include "/store/member/include/footer.ftl" /]

</div>

</body>
</html>
