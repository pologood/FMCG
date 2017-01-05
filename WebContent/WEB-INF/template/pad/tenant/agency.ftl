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
	var $return = $("#return");
	//返回中心
	$return.on("touchstart",function(){
		location.href="${base}/pad/member/center.jhtml"; return false;
	});

});
</script>
</head>
<body>
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void()" class="p_return" id="return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">旗下店铺</div>
		</div>
	</div>
	<article class="p_article p_articlecart" id="p_contscrooler">
		<div class="bodycont_orders">
			<table border="0" class="p_carttable">
				<tr class="p_trhard">
					<td width="30%">店铺名称</td>
					<td width="30%">店铺地址</td>
					<td width="16%">联系人</td>
					<td width="16%">联系电话</td>
					<td width="8%">操作</td>
				</tr>
				[#if page.content??&&page.content?has_content]
					[#list page.content as tenant]
						<tr>
							<td><img src="${tenant.logo}"/>${tenant.name}</td>
							<td>${tenant.address}</td>
							<td>${tenant.linkman}</td>
							<td>${tenant.telephone}</td>
							<td>
							[#if tenant.status=="wait"]
							  申请中
							[#elseif tenant.status=="success"]
							  已认证
							[#elseif tenant.status=="fail"]
							  已驳回
							[#else]
							  未认证
							[/#if]
							</td>
						</tr>
					[/#list]
				[/#if]
			</table>
		</div>
	</article>
	<footer class="p_footer p_cartfooter p_orderlfooter">
		<ul>
			<li class="p_cartfootf p_dorderft">
				<div class="p_dorderftli">总店铺数 <strong>${page.total}</strong></div>
				<div class="p_dorderftli">近期新增 <strong>${c_count}</strong></div>
				<div class="p_dorderftli">已审核 <strong>${has_count}</strong></div>
				<div class="p_dorderftli">未审核 <strong>${page.total-has_count}</strong></div>
			</li>
			<div class="p_clear"></div>
		</ul>
	</footer>
</section>
</body>
</html>
