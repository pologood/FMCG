<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.member.deposit.list")}[#if systemShowPowered][/#if]</title>
<link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet" />

<link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/amazeui.css">
<link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css">
<link href="${base}/resources/helper/css/iconfont.css" type="text/css" rel="stylesheet" />


<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/helper/js/search.js"></script>
<script src="${base}/resources/helper/js/amazeui.min.js"></script>
<script type="text/javascript">
$().ready(function() {
	
	[@flash_message /]

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
             <img class="js-app_logo app-img" src="${base}/resources/helper/images/payment-details.png"/>
             <dl class="app-info">
               <dt class="app-title" id="app_name">我的账单</dt>
               <dd class="app-status" id="app_add_status">
               </dd>
               <dd class="app-intro" id="app_desc">查询账户收入、支出明细。</dd>
             </dl>
          </div>
          <ul class="links" id="mod_menus">
          	<li><a hideFocus="" href="${base}/helper/member/deposit/fill.jhtml">账户充值</a></li>
            <li><a class="on" hideFocus="" href="javascript:;" >我的账单</a></li>
          </ul>
          
    	 </div>
       	<form id="listForm" action="list.jhtml" method="get">
			<div class="list" style="border-top:0">
				<table class="list">
					<tr>
						<th>
							日期
						</th>
						<th>
							类型
						</th>
						<th>
							${message("Deposit.credit")}
						</th>
						<th>
							${message("Deposit.debit")}
						</th>
						<th>
							${message("Deposit.balance")}
						</th>
						<th style="border-right: 0px">
							摘要
						</th>
					</tr>
					[#list page.content as deposit]
						<tr[#if !deposit_has_next] class="last"[/#if]>
							<td>
								<span title="${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}">${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
							</td>
							<td>
								${message("Deposit.Type." + deposit.type)}
							</td>
							<td style="text-align:right;">
								${currency(deposit.credit,true)}
							</td>
							<td style="text-align:right;">
								${currency(deposit.debit,true)}
							</td>
							<td style="text-align:right;">
								${currency(deposit.balance,true)}
							</td>
							<td style="border-right: 0px;">
								${deposit.memo}
							</td>
						</tr>
					[/#list]
				</table>
				[#if !page.content?has_content]
					<p>${message("shop.member.noResult")}</p>
				[/#if]
			</div>
			[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
				[#include "/helper/member/include/pagination.ftl"]
			[/@pagination]
			</form>
			
</div>
			</div>
		</div>
		[#include "/helper/include/footer.ftl" /]
	</body>
</html>