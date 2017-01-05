[#if notifyMessage??]
${notifyMessage}
[#else]
<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}-温馨提示</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $orderList = $("#orderList");
	$orderList.on('tap',function(){
		location.href = "${base}/wap/member/order/list.jhtml"; return false;
	});
	$("#return_order").on("tap",function(){
		location.href = "${base}/wap/member/order/list.jhtml"; return false;
	});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			 <div class="m_return" id="return_order"><a href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			 <div class="m_title" alt="选择日期">温馨提示</div>
		</div>
	</header>
	<article class="m_article m_article_1" id="m_scrooler">
		<div class="m_bodycont">
			<div class="m_statetitle">
				[#if payment.status == "success"]
					<h1>恭喜您，支付成功！</h1>
					<span><i class="iconfont">&#xe60c</i></span>
				[#elseif payment.status == "wait"]
					<h1>等待支付。。</h1>
					<span><i class="iconfont">&#xe60e</i></span>
				[#elseif payment.status == "failure"]
					<h1>您的款项支付失败，请您及时联系我们进行处理！</h1>
					<span><i class="iconfont">&#xe60d</i></span>
				[/#if]
			</div>
			[#if payment.status != "failure"]
			<div class="m_stateorder">
				<p><span>订单号:</span> ${payment.sn}</p>
				<p><span>支付金额:</span> <strong>${currency(payment.amount,true)}</strong></p>
			</div>
			[/#if]
			<div class="m_statebtn">
				<ul>
					<li id="orderList">返回订单列表</li>
				</ul>
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
[/#if]