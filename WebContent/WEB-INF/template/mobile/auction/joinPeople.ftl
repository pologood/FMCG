<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/detail.js"></script>
<script type="text/javascript">
	
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">参与人员</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_writh" id="m_scrooler">
		<div class="m_bodycont">
			<div class="m_detalims">
			 [#if promotionMembers?? && promotionMembers?has_content]
				<table cellpadding="0" cellspacing="0" class="m_table">
					<tr class="m_tabletop">
						<td width="25%">竞拍人</td>
						<td width="20%">兑换码</td>
						<td width="25%">价格</td>
						<td width="30%">时间</td>
					</tr>
					[#list promotionMembers as promotionMember]
						<tr>
							<td><span class="m_behind"><i class="iconfont">&#xe600</i></span>${promotionMember.member.username}</td>
							<td>${promotionMember.random}</td>
							<td>${currency(promotionMember.offerPrice,true)}</td>
							<td>${promotionMember.createDate?string("yyyy-MM-dd")}</td>
						</tr>
					[/#list]
				</table>
			 [/#if]	
			</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
