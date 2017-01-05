<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.review.list")}</title>
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
    [#include "/store/member/include/bootstrap_css.ftl" /]
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script type="text/javascript" src="${base}/resources/store/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/store/2.0/plugins/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/2.0/plugins/datatables/dataTables.bootstrap.min.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $listForm = $("#listForm");
            var $type = $("#type");
            var $typeSelect = $("#typeSelect");
            var $typeOption = $("#typeOption a");

            [@flash_message /]

            $typeSelect.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });

            $typeOption.click(function () {
                var $this = $(this);
                $type.val($this.attr("val"));
                $listForm.submit();
                return false;
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
        [#include "/store/member/include/header.ftl" /]
        [#include "/store/member/include/menu.ftl" /]
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    我的商品
                    <small>快去回复粉丝们咨询的问题，对您的诚信度有很大的帮助。</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                    <li><a href="${base}/store/member/review/manager.jhtml">我的商品</a></li>
                    <li class="active">评论列表</li>
                </ol>
            </section>
            <section class="content">
                <div class="box box-solid">
                    <div class="box-header with-border">
                        <i class="fa fa-comments-o"></i>
                        <h3 class="box-title">评论管理</h3>
                    </div>
                    <form id="listForm" action="manager.jhtml" method="get">
                        <input type="hidden" id="type" name="type" value="${type}"/>
                        <div class="row mtb10">
                            <div class="col-sm-7">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
                                        class="fa fa-refresh mr5"
                                        aria-hidden="true"></i> 刷新
                                    </button>
                                    <div class="dropdown fl ml5">
                                        <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                        id="dropdownMenu1"
                                        data-toggle="dropdown">
                                        类型
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                    id="typeOption">
                                    <li class="[#if type == null]active[/#if]">
                                        <a href="javascript:;"[#if type == null] class="current"[/#if]
                                        val="">${message("admin.review.allType")}</a>
                                    </li>
                                    [#assign currentType = type]
                                    [#list types as type]
                                    <li class="[#if type == currentType]active[/#if]">
                                        <a href="javascript:;"[#if type == currentType] class="current"[/#if]
                                        val="${type}">${message("Review.Type." + type)}</a>
                                    </li>
                                    [/#list]
                                </ul>
                            </div>
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
                        placeholder="商品名称">
                        <div class="input-group-btn">
                            <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                            </button>
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
                        <th>商品</th>
                        <th>评分</th>
                        <th>内容</th>
                        <th>会员</th>
                        <th>是否显示</th>
                        <th>创建日期</th>
                        <th>${message("admin.common.handle")}</th>
                    </tr>
                </thead>
                <tbody>
                    [#list page.content as review]
                    <tr>
                        <td>
                            <input type="checkbox" name="ids" value="${review.id}"/>
                        </td>
                        <td>
                            <a href="${base}/store/product/content/${review.product.id}.jhtml"
                            title="${review.product.name}"
                            target="_blank">${abbreviate(review.product.name, 50, "...")}</a>
                        </td>
                        <td>${review.score}</td>
                        <td>
                            <span title="${review.content}">${abbreviate(review.content, 50, "...")}</span>
                        </td>
                        <td>
                            [#if review.member??]
                            ${review.member.username}
                            [#else]
                            ${message("admin.review.anonymous")}
                            [/#if]
                        </td>
                        <td>
                            [#if review.isShow==true]
                            <i class="fa fa-check" style="color: #9bd36e"></i>
                            [/#if]
                            [#if review.isShow==false]
                            <i class="fa fa-remove" style="color: #ff6600"></i>
                            [/#if]
                        </td>
                        <td>
                            <span title="${review.createDate?string("yyyy-MM-dd HH:mm:ss")}">${review.createDate}</span>
                        </td>
                        <td>
                            [@helperRole url="store/member/review/manager.jhtml" type="update"]
                            [#if helperRole.retOper!="0"]
                            <a href="edit.jhtml?id=${review.id}">[${message("admin.common.edit")}]</a>
                            [/#if]
                            [/@helperRole]
                        </td>
                    </tr>
                    [/#list]
                </tbody>
            </table>
            <div class="dataTables_paginate paging_simple_numbers">
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
</div>
[#include "/store/member/include/bootstrap_js.ftl" /]
</body>
</html>
