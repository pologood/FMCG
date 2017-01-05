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
$().ready(function(){
	var $addConsultation = $("#addConsultation");
	var $productId = $("#productId");
	$addConsultation.on('tap',function(){
		location.href="${base}/mobile/consultation/add/"+$productId.val()+".jhtml";
	});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a href="javascript:;" alt="返回" id="return_btn"><div class="p_datag">返回</div></a></div>
			[#include "/mobile/include/top_search.ftl" /]
			<div class="m_title" alt="选择日期">团购咨询</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_writh" id="m_scrooler">
		<div class="m_bodycont">
			<input type="hidden" id="productId" value="${product.id}">
			<div class="m_detalims">
				<div class="m_consulting">
					<ul>
							[#if page.content?has_content]
							[#list page.content as consultation]
							[#if consultation.isShow ]
							<li>
								<div class="m_evaluate_text">
									<span>${(consultation.member.username)!} : </span>
									<span>${consultation.content}</span>
									<span>${consultation.createDate?string("yyyy-MM-dd HH:mm:SS")}</span>
								</div>
								[#if consultation.replyConsultations??&&consultation.replyConsultations?has_content]
								[#list consultation.replyConsultations as replyConsultation]
								<div class="m_evaluate_text_1">
										<span>${(replyConsultation.member.name)!} : </span>
										<span>${replyConsultation.content}</span>
										<span>${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:SS")}</span>
								</div>
								[/#list]
								[/#if]	
							</li>
							[/#if]
							[/#list]
							[/#if]
						<div class="p_clear"></div>
					</ul>
				</div>
			</div>
			<div class="m_btn"><a href="javascript:void(0)" id="addConsultation">我要咨询</a></div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
