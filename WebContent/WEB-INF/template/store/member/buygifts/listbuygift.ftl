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
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <style type="text/css">
        #listTable th, #listTable td {
            /*padding-left: 5px;*/
            text-align: center;
        }
    </style>
    <script type="text/javascript">

        $().ready(function () {
            $("#deleteButton").addClass("disabled");
//            $("#listTable").DataTable();
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
                营销工具
                <small>活动商品</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/buygifts/listbuygift.jhtml">营销工具</a></li>
                <li class="active">买赠搭配</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom" style="margin-bottom: 0; box-shadow: none;">
                    <ul class="nav nav-tabs pull-right">
                        <li class="">
                            <a href="${base}/store/member/buygifts/listgift.jhtml">赠品管理</a>
                        </li>
                        <li class="active">
                            <a href="${base}/store/member/buygifts/listbuygift.jhtml">活动商品</a>
                        </li>
                        <li class="pull-left header"><i class="fa fa-gift"></i> 买赠搭配</li>
                    </ul>
                    <div class="row mtb10">
                        <form id="listForm" action="listbuygift.jhtml" method="get">
                            <input type="hidden" id="type" name="type" value="${type}"/>
                            <input type="hidden" id="status" name="status" value="${status}"/>
                        <div class="col-sm-7">
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary btn-sm ml5"
                                        onclick="javascript:location.href='${base}/store/member/buygifts/listproduct.jhtml'">
                                    <i
                                            class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
                                </button>
                                <button type="button" class="btn btn-default btn-sm" id="deleteButtonAll"><i
                                        class="fa fa-close mr5"
                                        aria-hidden="true"></i>删除
                                </button>
                                <button type="button" class="btn btn-default btn-sm" id="deleteButtonAll"><i
                                        class="fa fa-refresh mr5"
                                        aria-hidden="true"></i> 刷新
                                </button>
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
                                    <input type="text" name="table_search" class="form-control pull-right"
                                           placeholder="Search">
                                    <div class="input-group-btn">
                                        <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-body">
                        <table id="listTable" class="table table-bordered table-striped">
                            <thead>
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
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>${message("admin.common.handle")}</th>
                            </tr>
                            </thead>
                            <tbody>
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
                                            <a href="javascript:;" thumbnail="${promo.thumbnail}"
                                               description="${promo.fullName}"
                                               onclick=share(${promo.productId},$(this).attr("thumbnail"),$(this).attr("description"))>分享</a>
                                        [/#if]
                                    [/@helperRole]
                                </td>
                            </tr>
                            [/#list]
                            </tbody>
                        </table>
                    [#if !page.content?has_content]
                        <p class="nothing">${message("box.member.noResult")}</p>
                    [/#if]
                        <div class="dataTables_paginate paging_simple_numbers">
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                                 [#include "/store/member/include/pagination.ftl"]
                            [/@pagination]
                        </div>
                    </form>
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
            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
