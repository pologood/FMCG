<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />
<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $listForm = $("#listForm");
	var $cDay = $("#cDay");
	var $cWeek = $("#cWeek");
	var $cMonth = $("#cMonth");
	var $timeType = $("#timeType");
	var $allOrder = $("#allOrder");
	var $myOrder = $("#myOrder");
	var $tenantOrder = $("#tenantOrder");
	var $listType = $("#listType");
	var $pageNumber= $("#pageNumber");
	var $pageSize= $("#pageSize");
	var $timeSearch= $("#timeSearch");
	var $return= $("#return");
	//返回
	$return.on("tap",function(){
		location.href="${base}/pad/member/center.jhtml";return false;
	})
	
	//全部订单查询
	$allOrder.on("tap",function(e){
		$listType.val("all");
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
	//店铺订单查询
	$tenantOrder.on("tap",function(e){
		$listType.val("tenant");
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
	//我的订单查询
	$myOrder.on("tap",function(e){
		$listType.val("my");
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
	//当天
	$cDay.on("tap",function(e){
		$timeType.val("day");
		$listType.val($listType.val());
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
	$cWeek.on("tap",function(e){
		$timeType.val("week");
		$listType.val($listType.val());
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
	$cMonth.on("tap",function(e){
		$timeType.val("month");
		$listType.val($listType.val());
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
	//时间搜索
	$timeSearch.on("tap",function(e){
		$timeType.val("");
		$listType.val($listType.val());
		$pageNumber.val(1);
		$listForm.submit();	
		return false;
	});
});
</script>
</head>
<body>
<section class="p_section">
	<form id="listForm" action="list.jhtml" method="get">
		<input type="hidden" id="listType" name="listType" value="${listType}"/>
		<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
		<input type="hidden" id="pageSize" name="pageSize" value="50" />
		<input type="hidden" id="timeType" name="timeType" />
		<div class="p_header">
			<div class="p_hbody">
				<a href="javascript:void();" id="return" class="p_return">
					<div class="p_tag"></div>
				</a>
				<div class="p_ofunction">
					<ul>
						<li id="allOrder">全部订单</li>
						<li id="myOrder">我的订单</li>
						<li id="tenantOrder">店铺订单</li>
						<li class="p_listsort">快捷查询</li>
						<li class="p_interval p_intervalol">
							<div class="p_intervalc">
								<span class="p_intervalcip"><input type="text" name="beginDate" placeholder="2014-01-01" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"></span> -
								<span class="p_intervalcip"><input type="text" name="endDate" placeholder="2014-02-01" value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"></span>
								<span class="p_intervalcbtn" id="timeSearch">查询</span>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="p_listsortc p_orderlist" id="p_sortcscrooll">
			<ul>
			    <li id="cDay">当天查询</li>
			    <li id="cWeek">本周查询</li>
			    <li id="cMonth">本月查询</li>
			</ul>
		</div>
	</form>
	<article class="p_article p_articlecart" id="p_contscrooler">
		<div class="bodycont_orders">
			<table border="0" class="p_carttable">
				<tr class="p_trhard">
					<td width="14%">订单编号</td>
					<td width="13%">订单金额</td>
					<td width="13%">收货人</td>
					<td width="13%">订单状态</td>
					<td width="13%">支付状态</td>
					<td width="13%">配送状态</td>
					<td width="13%">创建日期</td>
					<td width="8%">操作</td>
				</tr>
				[#if page.content??&&page.content?has_content]
						[#list page.content as order]
							<tr>
								<td>${order.sn}</td>
								<td><div class="p_red">${currency(order.price,true)}</div></td>
								<td>${order.consignee}</td>
								<td>${message("Order.OrderStatus." + order.orderStatus)}</td>
								<td>${message("Order.PaymentStatus." + order.paymentStatus)}</td>
								<td>${message("Order.ShippingStatus." + order.shippingStatus)}</td>
								<td>${order.createDate?string("yyyy-MM-dd")}</td>
								<td><a href="view.jhtml?sn=${order.sn}" class="p_searchorder"><div class="p_search_icon"></div></a></td>
							</tr>
						[/#list]
				[#else]
					<tr>暂无订单信息！</tr>
				[/#if]
			</table>
		</div>
	</article>
	<footer class="p_footer p_cartfooter p_orderlfooter">
		<ul>
			<li class="p_cartfootf p_dorderft">
				<div class="p_dorderftli">已支付 <strong>${hasPayCount}</strong></div>
				<div class="p_dorderftli">未支付 <strong>${unPayCount}</strong></div>
				<div class="p_dorderftli">未送达 <strong>${unShipCount}</strong></div>
				<div class="p_dorderftli">已送达 <strong>${hasShipCount}</strong></div>
				<div class="p_dorderftli">已完成 <strong>${complete}</strong></div>
			</li>
			<div class="p_clear"></div>
		</ul>
	</footer>
</section>
</body>
</html>
