<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.review.edit")}</title>
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

    <script type="text/javascript" src="${base}/resources/store/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>

    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
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
                商品评论
                <small>粉丝们对您的商品提出了宝贵的意见，快去看看吧。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/review/manager.jhtml">我的商品</a></li>
                <li class="active">评论编辑</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-info" style="min-height: 400px;">
                <div class="box-header with-border">
                    <h3 class="box-title">评论编辑</h3>
                </div>
                <!-- /.box-header -->
                <!-- form start -->
                <form id="inputForm" action="update.jhtml" method="post" style="padding:20px 0 0 100px">

                    <input type="hidden" name="id" value="${review.id}"/>
                    <table class="table table-bordered table-hover dataTable" style="width: 80%;">
                        <tr>
                            <th>
                            ${message("Review.product")}:
                            </th>
                            <td>
                                <a href="${base}/helper/product/content/${review.product.id}.jhtml"
                                   target="_blank">${review.product.name}</a>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Review.member")}:
                            </th>
                            <td>
                            [#if review.member??]
						${review.member.username}
					[#else]
                            ${message("admin.review.anonymous")}
                            [/#if]
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Review.ip")}:
                            </th>
                            <td>
                            ${review.ip}
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Review.score")}:
                            </th>
                            <td>
                            ${review.score}
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Review.content")}:
                            </th>
                            <td>
                            ${review.content}
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Review.images")}:
                            </th>
                            <td>
                            [#if productImages??&&productImages?has_content]
                                [#list productImages as productImage]
                                    <a href="${productImage.source}" target="_blank">
                                        <img class="lazy" style="width: 160px;height: 160px;cursor: pointer;"
                                             src="${productImage.thumbnail}">
                                    </a>
                                [/#list]
                            [/#if]
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Review.isShow")}:
                            </th>
                            <td>
                                <input type="checkbox" name="isShow" value="true"[#if review.isShow]
                                       checked="checked"[/#if]/>
                            </td>
                        </tr>

                    </table>

                    <div class="col-sm-offset-2 col-sm-2">
                        <button type="submit" class="btn btn-block btn-success"
                                value="${message("admin.common.submit")}">确定
                        </button>

                    </div>
                    <div class="col-sm-offset-0 col-sm-2">
                        <button type="button" class="btn btn-block btn-default" value="${message("admin.common.back")}"
                                onclick="location.href='manager.jhtml'">返回
                        </button>
                    </div>
                    <div class="kong"></div>
                </form>

            </div>
        </section>
    </div>
</div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
