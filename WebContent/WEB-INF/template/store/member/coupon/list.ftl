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
    <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <style type="text/css">
        #bdshare_weixin_qrcode_dialog {
            box-sizing: content-box;
        }
    </style>
    <script type="text/javascript">

        $().ready(function () {

//            $("#example").DataTable();
            var $listForm = $("#listForm");
            var $status = $("#status");
            var $statusSelect = $("#statusSelect");
            var $statusOption = $("#statusOption a");
            $("#deleteButton").addClass("disabled");

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
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
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
                <li class="active">代金券</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <form id="listForm" action="list.jhtml" method="get">
                    <input type="hidden" id="type" name="type" value="${type}"/>
                    <input type="hidden" id="status" name="status" value="${status}"/>
                    <input type="hidden" id="processStatus" name="processStatus" value="${processStatus}"/>
                    <div class="nav-tabs-custom" style="box-shadow: none;margin-bottom: 0;">
                        <ul class="nav nav-tabs pull-right">
                            <li class="[#if processStatus=='Expired']active[/#if]">
                                <a href="${base}/store/member/coupon/list.jhtml?type=tenantCoupon&processStatus=Expired">已过期</a>
                            </li>
                            <li class="[#if processStatus=='unUsed']active[/#if]">
                                <a href="${base}/store/member/coupon/list.jhtml?type=tenantCoupon&processStatus=unUsed">已领完</a>
                            </li>
                            <li class="[#if processStatus=='unBegin']active[/#if]">
                                <a href="${base}/store/member/coupon/list.jhtml?type=tenantCoupon&processStatus=unBegin">未开始</a>
                            </li>
                            <li class="[#if processStatus=='canUse']active[/#if]">
                                <a href="${base}/store/member/coupon/list.jhtml?type=tenantCoupon&processStatus=canUse">可领用</a>
                            </li>
                            <li class="pull-left header"><i class="fa fa-th"></i>代金券</li>
                        </ul>
                    </div>
                    <div class="row mtb10">
                        <div class="col-sm-3">
                            <div class="btn-group">
                            [@helperRole url="helper/member/coupon/list.jhtml" type="add"]
                                [#if helperRole.retOper!="0"]
                                    <button type="button" class="btn btn-primary btn-sm"
                                            onclick="javascript:location.href='add.jhtml?status=${status}&type=${type}';">
                                        <i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
                                    </button>
                                [/#if]
                            [/@helperRole]
                            [#--<button type="button" class="btn btn-default btn-sm" id="deleteButton"><i--]
                            [#--class="fa fa-refresh mr5"--]
                            [#--aria-hidden="true"></i> 删除--]
                            [#--</button>--]
                                <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i
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
                        <div class="col-sm-4">
                            <div class="fl">
                                <input type="text" id="startDate" name="begin_date" value="${begin_date}"
                                       class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});"
                                       placeholder="开始时间"/>
                            </div>
                            <div class="fl">
                                <i class="fa fa-exchange mid_po_icon"></i>
                            </div>
                            <div class="fl">
                                <input type="text" id="endDate" name="end_date" value="${end_date}" class="date_input"
                                       onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="结束时间"/>
                            </div>
                            <div class="fl ml5">
                                <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
                            </div>
                        </div>
                        <div class="col-sm-5">
                            <div class="box-tools fr">
                                <div class="input-group input-group-sm" style="width: 150px;">
                                    <input type="text" id="searchValue" name="searchValue"
                                           value="${page.searchValue}" class="form-control pull-right"
                                           placeholder="搜索代金券名称">
                                    <ul id="searchPropertyOption" style="display:none;">
                                        <li>
                                            <a style="cursor: pointer;" val="name"></a>
                                        </li>
                                    </ul>
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
                                <th>名称</th>
                                <th>金额</th>
                                <th>最低消费</th>
                                <th>券的库存</th>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>${message("admin.common.handle")}</th>
                            </tr>
                            </thead>
                            <tbody>
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
                                    <span title="${coupon.startDate?string("yyyy-MM-dd HH:mm")}">${coupon.startDate?string("yyyy-MM-dd HH:mm")}</span>
                                </td>
                                <td>
                                    <span title="${coupon.endDate?string("yyyy-MM-dd HH:mm")}">${coupon.endDate?string("yyyy-MM-dd HH:mm")}</span>
                                </td>
                                <td>
                                    [@helperRole url="store/member/coupon/list.jhtml" type="statistics"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="${base}/store/member/coupon/sumer.jhtml?id=${coupon.id}&type=send&status=${status}">统计</a>
                                        [/#if]
                                    [/@helperRole]

                                    [@helperRole url="store/member/coupon/list.jhtml" type="share"]
                                        [#if helperRole.retOper!="0"]
                                            <a href="javascript:" introduction="${coupon.introduction}"
                                               onclick=share(${coupon.id},$(this).attr("introduction"))>分享</a>
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
                    <a href="http://www.jiathis.com/share"
                       class="jiathis jiathis_txt jiathis_separator jtico jtico_jiathis"
                       target="_blank">更多</a>
                    <a class="jiathis_counter_style"></a></div>
                <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1"
                        charset="utf-8"></script>
            </div>
    </div>
    </section>
</div>
</div>
[#include "/store/member/include/footer.ftl" /]
</body>
</html>
