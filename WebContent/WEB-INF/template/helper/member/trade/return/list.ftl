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
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        
        function find_return(obj){
            location.href="${base}/helper/member/trade/return/list.jhtml?returnStatus="+$(obj).attr("id");
        }
        //确认退货
        function agree_return(id){
            if(confirm("确认要退货吗")){
                $.ajax({
                    url:"${base}/helper/member/trade/return/confirm_return.jhtml",
                    type:"post",
                    data:{id:id},
                    dataType:"json",
                    success:function(data){
                        if(data.type=="success"){
                            location.reload();
                        }else{
                            $.message(data.type,data.content)
                        }
                    }
                });
            }
        }
        function check_refuse_return(obj){
            $.dialog({
                title: "拒绝理由",
                [@compress single_line = true]
                content: '<table class="input"><tr><th style="width:80px;"> 拒绝理由:<\/th><td>'+obj+'<\/td><\/tr><\/table>',
                [/@compress]
                width: 435,
                modal: true
            });
        }
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
                        <img class="js-app_logo app-img" src="${base}/resources/helper/images/my-order.png"/>
                        <dl class="app-info">
                            <dt class="app-title" id="app_name">退货管理</dt>
                            <dd class="app-status" id="app_add_status">
                            </dd>
                            <dd class="app-intro" id="app_desc">查询及处理店铺退货信息。</dd>
                        </dl>
                    </div>
                    &nbsp;
                    <ul class="links" id="mod_menus">
                        <li>
                            <a [#if returnStatus == "unconfirmed"]class="on"[/#if] hideFocus=""
                            href="javascript:;" id="unconfirmed" roles="owner,manager,cashier" onclick="find_return(this)">待受理</a>
                        </li>
                        [#if versionType==1]
                        <li>
                            <a [#if returnStatus == "confirmed"]class="on"[/#if] hideFocus=""
                            href="javascript:;" id="confirmed" roles="owner,manager,cashier" onclick="find_return(this)">已受理</a>
                        </li>
                        <li>
                            <a [#if returnStatus == "audited"]class="on"[/#if] hideFocus=""
                            href="javascript:;" id="audited" roles="owner,manager,cashier" onclick="find_return(this)">已认证</a>
                        </li>
                        [/#if]
                        <li>
                            <a [#if returnStatus == "completed"]class="on"[/#if] hideFocus=""
                            href="javascript:;" id="completed" roles="owner,manager,cashier" onclick="find_return(this)">已完成</a>
                        </li>
                        <li>
                            <a [#if returnStatus == "cancelled"]class="on"[/#if] hideFocus=""
                            href="javascript:;" id="cancelled" roles="owner,manager,cashier" onclick="find_return(this)">已取消</a>
                        </li>
                    </ul>

                </div>
                <form id="listForm" action="list.jhtml" method="get">
                    <input type="hidden" name="returnStatus" value="${returnStatus}"/>
                    <div class="bar">
                        <div class="buttonWrap">
                            <a href="javascript:;"  class="iconButton" id="refreshButton">
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
                                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="menuWrap">
                            <div class="search" style="width: 220px;">
                                <input type="text" id="keyword" name="keyword" value="${keyword}" maxlength="200"
                                       placeholder="搜索退货单号、收货人、提货码"/>
                                <button type="submit">&nbsp;</button>
                            </div>
                        </div>
                    </div>
                    <div class="list">
                        <table id="listTable" class="list">
                            <tr>
                                <th>
                                    <span>订单编号</span>
                                </th>
                                <th>
                                    <span>退货编号</span>
                                </th>
                                <th>
                                    <span>发货件数</span>
                                </th>
                                <th>
                                    <span>退货件数</span>
                                </th>
                                <th>
                                    <span>订单金额</span>
                                </th>
                                <th>
                                    <span>退货金额</span>
                                </th>
                                <th>
                                    <span>退货状态</span>
                                </th>
                                <th>
                                    <span>打印次数</span>
                                </th>
                                [#if returnStatus=='unconfirmed' || returnStatus=='audited']
                                <th>
                                    <span>操作</span>
                                </th>
                                [/#if]
                            </tr>
                            [#list page.content as returns]
                            <tr>
                                <td>
                                    <span>[#if returns.trade.order?has_content]${returns.trade.order.sn}[/#if]</span>
                                </td>
                                <td>
                                    <span>${returns.sn}</span>
                                </td>
                                <td>
                                    <span>${returns.trade.quantity}</span>
                                </td>
                                <td>
                                    <span>${returns.quantity}</span>
                                </td>
                                <td>
                                    <span>${returns.trade.amount}</span>
                                </td>
                                <td>
                                    <span>${returns.amount}</span>
                                </td>
                                <td>
                                    [#if returns.returnStatus=='unconfirmed']
                                    <span>待受理</span>
                                    [#elseif returns.returnStatus=='confirmed']
                                    <span>已受理</span>
                                    [#elseif returns.returnStatus=='audited']
                                    <span>已认证</span>
                                    [#elseif returns.returnStatus=='completed']
                                    <span>已完成</span>
                                    [#elseif returns.returnStatus=='cancelled']
                                    [#if returns.memo?has_content]
                                    <span>已拒绝</span>
                                    <a href="javascript:;" onclick="check_refuse_return('${returns.memo}')">查看</a>
                                    [#else]
                                    <span>已取消</span>
                                    [/#if]
                                    [/#if]
                                </td>
                                <td>
                                    ${returns.print}
                                </td>
                                <td>
                                    <span>
                                        [#if returns.returnStatus=='unconfirmed' || returns.returnStatus=='audited']
                                            [@helperRole url="helper/member/trade/return/list.jhtml" type="read"]
                                                [#if helperRole.retOper!="0"]
                                                    <a href="view.jhtml?spReturnsId=${returns.id}">[查看]</a>
                                                [/#if]
                                            [/@helperRole]
                                        [/#if]
                                    </span>
                                    [#if returns.returnStatus=='audited']
                                    <a href="${base}/helper/member/trade/return/print.jhtml?spReturnsId=${returns.id}" target="_black">[打印]</a>
                                    [/#if]
                                </td>
                            </tr>
                            [/#list]
                        </table>
                        <p class="nothing"></p>
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
