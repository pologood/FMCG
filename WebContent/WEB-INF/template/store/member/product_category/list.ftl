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
    <link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript"
            src="${base}/resources/store/2.0/plugins/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="${base}/resources/store/2.0/plugins/datatables/dataTables.bootstrap.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

//            $("#listTable").DataTable();
            var $delete = $("#listTable a.delete");

        [@flash_message /]

            // 删除
            $delete.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $.ajax({
                            url: "delete.jhtml",
                            type: "POST",
                            data: {id: $this.attr("val")},
                            dataType: "json",
                            cache: false,
                            success: function (message) {
                                $.message(message);
                                if (message.type == "success") {
                                    $this.closest("tr").remove();
                                }
                            }
                        });
                    }
                });
                return false;
            });
        });
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>我的商品
                <small>查询我发布的商品分类</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/product_category/list.jhtml">我的商品</a></li>
                <li class="active">商品分类</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="box-header with-border">
                    <i class="fa fa-shopping-bag"></i>
                    <h3 class="box-title">商品分类</h3>
                </div>
                <div class="box-body">
                    <form id="listForm" action="list.jhtml" method="get">

                        <!--导航功能--->
                        <div class="row mtb10">
                            <div class="col-sm-7">
                                <div class="btn-group">
                                    <a href="${base}/store/member/product_category/add.jhtml">
                                        <button type="button" class="btn btn-primary btn-sm ml5"><i
                                                class="fa fa-plus-square mr5"></i> 添加
                                        </button>
                                    </a>
                                    <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
                                            class="fa fa-refresh mr5"></i> 刷新
                                    </button>
                                </div>
                            </div>
                        </div>
                        <table id="listTable" class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th class="check">
                                    <input type="checkbox" id="selectAll"/>
                                </th>
                                <th>名称</th>
                                <th>编号</th>
                                <th>排序</th>
                                <th>${message("admin.common.handle")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            [#list productCategoryTree as productCategory]
                            <tr>
                                <td>
                                    <input type="checkbox" name="ids" value="${productCategory.id}"/>
                                </td>
                                <td style="text-align:left;">
								<span style="margin-left: ${productCategory.grade * 20}px;[#if productCategory.grade == 0] color: #000000;[/#if]">
                                ${abbreviate(productCategory.name,25,"...")}
								</span>
                                </td>
                                <td>
                                ${productCategory.id}
                                </td>
                                <td>
                                ${productCategory.order}
                                </td>
                                <td>
                                    <!--<a href="${base}${productCategory.path}" target="_blank">[${message("admin.common.view")}]</a>-->

                                    [@helperRole url="helper/member/product_category/list.jhtml" type="update"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="edit.jhtml?id=${productCategory.id}">[${message("admin.common.edit")}]</a>
                                        [/#if]
                                    [/@helperRole]

                                    [@helperRole url="helper/member/product_category/list.jhtml" type="del"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="javascript:;" class="delete" val="${productCategory.id}">[${message("admin.common.delete")}]</a>
                                        [/#if]
                                    [/@helperRole]
                                </td>
                            </tr>
                            <div class="modal fade" id="agree_return" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
                                <div class="modal-dialog">
                                    <form class="form-horizontal" role="form" action="delete.jhtml" method="post">
                                        <input type="hidden" name="id" value="${productCategory.id}">
                                        <div class="modal-content" style=" border-radius: 5px;">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                                <h4 class="modal-title">删除</h4>
                                            </div>
                                            <div class="modal-body">
                                                <lebal>是否确认此操作？</lebal>
                                            </div>
                                            <div class="modal-footer">
                                                <div class="col-sm-offset-8 col-sm-2">
                                                    <button type="submit" class="btn btn-block btn-primary">确定</button>
                                                </div>
                                                <div class="col-sm-offset-0 col-sm-2">
                                                    <input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            [/#list]
                            </tbody>
                        </table>
                    [#if !productCategoryTree?if_exists]
                        <p style="text-align:center;border-bottom:solid 1px #c6c9ca;line-height:30px;">${message("box.member.noResult")}</p>
                    [/#if]
                    </form>
                </div>
            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>
</body>
</html>
