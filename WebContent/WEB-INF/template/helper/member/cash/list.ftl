<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>汇款查询[#if systemShowPowered][/#if]</title>
<link href="${base}/resources/helper/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/helper/css/font.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${base}/resources/helper/css/product.css">
<!-- <link href="${base}/resources/helper/css/credit.css" rel="stylesheet" type="text/css" /> -->
<style type="text/css">
    
    table.list th{
        /*padding-left: 5px;*/
        text-align: center;
    }
    div.popupMenu a{
        text-align: center;
    }
</style>
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
<script src="${base}/resources/helper/js/amazeui.min.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>

<script type="text/javascript">
$().ready(function() {
	[@flash_message /]
	

});
function get_date_val(){
    $("#listForm").submit();   
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
		             	<img class="js-app_logo app-img" src="${base}/resources/helper/images/app_cash.png"/>
		             	<dl class="app-info">
		               		<dt class="app-title" id="app_name">汇款查询</dt>
		               		<dd class="app-status" id="app_add_status"></dd>
		               		<dd class="app-intro" id="app_desc">查询银行汇款申请流水、到账状态！</dd>
		             	</dl>
		          	</div>
		          	<ul class="links" id="mod_menus">
			          	<li><a class="" hideFocus="" href="index.jhtml">银行汇款</a></li>
			            <li><a class="on" hideFocus="" href="javascript:;">汇款记录</a></li>
		          	</ul>
		    	</div>
		    	<form id="listForm" action="list.jhtml" method="get">
                	<input type="hidden" name="status" value="" id="status_val">
	                <div class="bar">
	                    <div class="buttonWrap">
	                        <a href="${base}/helper/member/trade/order_settle_account.jhtml" class="iconButton">
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
	                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                        <input type="text" id="startDate" name="startDate" value="${begin_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间"/>
	                        <input type="text" id="endDate" name="endDate" value="${end_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});"
	                       placeholder="结束时间"/>
	                       <input type="button" value="查询" onclick="get_date_val()" id="submit_button">
	                        <script type="text/javascript">
	                            $(function(){
	                                $("#settleAccountSelect").mouseover(function(){
	                                    $("#seller_pop").show();
	                                });

	                                $("#settleAccountSelect").mouseout(function(){
	                                    $("#seller_pop").hide();
	                                });
	                            });
	                        </script>
	                    </div>
	                </div>
					<div class="list" style="border-top:0">
						<table class="list">
							<tr>
								<th>
									汇款日期
								</th>
								<th>
									汇款方式
								</th>
								<th>
									编号
								</th>
								<th>
									汇款金额
								</th>
								<th>
									手续费
								</th>
								<th>
									状态
								</th>
							</tr>
							[#list page.content as credit ]
							<tr[#if !credit_has_next] class="last"[/#if]>
								<td>
									<span title="${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}">${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
								</td>
								<td>
								  	[#if credit.method=="immediately" ]
								     	加急汇款
								  	[#else]
									   	普通汇款
									[/#if]
								</td>
								<td>
									${credit.sn}
								</td>
								<td style="text-align:right;">
									${currency(credit.amount, true)}
								</td>
								<td style="text-align:right;">
									${currency(credit.fee, true)}
								</td>
								<td  style="border-right: 0px">
								  ${message("Credit.Status." + credit.status)}
								</td>
							</tr>
							[/#list]
						</table>
						[#if !page.content?has_content]
							<p>${message("shop.member.noResult")}</p>
						[/#if]
					</div>
					[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
						[#include "/helper/include/pagination.ftl"]
					[/@pagination]
				</form>
			</div>
		</div>
	</div>
	[#include "/helper/include/footer.ftl" /]
</body>
</html>