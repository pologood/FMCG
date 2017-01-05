<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    [#include "/store/member/include/bootstrap_css.ftl"]
    <link href="${base}/resources/store/css/style.css" type="text/css" rel="stylesheet"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/header.ftl"]
[#include "/store/member/include/menu.ftl"]
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                个人中心
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li class="active">消息列表</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="nav-tabs-custom">
                        <div class="tab-content">
                            <form id="listForm" action="list.jhtml" method="get">
                                <div class="messages">
                                    <ul style="list-style:none;padding-left:0px;">
                                        <li style="height:80px;">
                                            <div style="width:100px;border:1px solid #666666;text-align:center;line-height:39px;float:right;font-size:16px;" onclick="del_all();">一键删除</div>
                                        </li>
                                    [#if page.content?size lte 0]
                                        <li>
                                            <div style="text-align:center;">暂无数据</div>
                                        </li>
                                    [#elseif page.content?size>0]
                                        [#list page.content as message]
                                            <li>
                                                <div class="dt">
                                                    <em>丨</em>
                                                    [#switch message.type]
                                                        [#case "order"]订单提醒
                                                            [#break]
                                                        [#case "account"]账单提醒
                                                            [#break]
                                                        [#case "notice"]系统公告
                                                            [#break]
                                                        [#case "message"]系统消息
                                                            [#break]
                                                        [#case "consultation"]咨询回复
                                                            [#break]
                                                        [#case "contact"]社交圈
                                                    [/#switch]
                                                    <a href="javascript:del(${message.id});" class="delete" title="删除"></a>
                                                </div>
                                                <div class="dd">
                                                    <div class="info">
                                                        <!-- [#assign version=b2c]
                                                        [#if versionType==1][#assign version=b2b][/#if]
                                                        [#if message.trade??&&message.trade?has_content]
                                                            <a href="javascript:;">${message.content}</a>
                                                        [#elseif message.deposit??&&message.deposit?has_content]
                                                            <a href="javascript:;">${message.content}</a>
                                                        [/#if] -->
                                                        <a href="javascript:;">${message.content}</a>
                                                        <span style="position: absolute;right: 72px;">[#if message.receiverRead==true]已读[#else]未读[/#if]</span>
                                                    </div>
                                                    <div class="info">
                                                        ${message.createDate?string("yyyy-MM-dd HH:mm:ss")}
                                                        [#if message.receiverRead==false]
                                                        <input type="button" value="标记为已读" class="btn btn-paimary btn-sm" onclick="read('${message.id}')" style="    position: absolute;right: 60px;">
                                                        [/#if]
                                                        
                                                    </div>
                                                </div>
                                            </li>
                                        [/#list]
                                    [/#if]
                                    </ul>
                                
                                </div>
                                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                                  [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                                  [#include "/store/member/include/pagination.ftl"]
                                  [/@pagination]
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
    </div>
    </section>
[#include "/store/member/include/footer.ftl"]
</div>
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript">
    function del(id) {
        if (window.confirm("确认删除吗？")) {
            $.ajax({
                url: "delete.jhtml",
                type: "post",
                data: {id: id},
                dataType: "json",
                success: function (data) {
                    $.message(data);
                    if (data.type == "success") {
                        window.location.reload(true);
                    }
                }
            });
        }
    }
    function del_all() {
        if (window.confirm("确认要全部删除吗？")) {
            $.ajax({
                url: "delete_all.jhtml",
                type: "post",
                dataType: "json",
                success: function (data) {
                    $.message(data);
                    if (data.type == "success") {
                        window.location.reload(true);
                    }
                }
            });
        }
    }
    function read(id) {
        $.ajax({
            url: "${base}/store/member/message/read.jhtml",
            type: "post",
            data: {id: id},
            dataType: "json",
            success: function (data) {
                if (data.type == "success") {
                    $.message(data);
                    window.location.reload(true);
                }
            }
        });
    }
</script>
</body>
</html>
