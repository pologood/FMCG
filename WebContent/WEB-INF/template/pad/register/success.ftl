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
	var $back_index = $("#back_index");
	var $back_shop = $("#back_shop");
	$back_index.on("tap",function(){
		location.href="${base}/pad/index.jhtml"; return false;
	});
	$return.on("tap",function(){
		location.href="${base}/pad/member/center.jhtml"; return false;
	});
	$back_shop.on("tap",function(){
		location.href="${base}/pad/tenant/agency.jhtml"; return false; 
	})
});
$(function(){
	//申请店铺成功滚动
	var  nactscrooler5 = $('#p_nactscrooler_5'),
         nactscrooll5 = new iScroll('p_nactscrooler_5',{onBeforeScrollStart:function(){nactscrooler5.iScroll('refresh');}});
         $('.p_userrg_wind').css("max-height",window.innerHeight -139 +'px');			
});
</script>
</head>
<body>
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:void();" class="p_return" id="return">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">审核状态</div>
			<div class="p_editor"><a href="javascript:void();" id="back_index">回首页</a></div>
		</div>
	</div>
	<article class="p_article p_orderlistc p_zc">
		<div class="bodycont_orders">
			<div class="p_success p_successsh">
				<div class="p_userrg_wind p_userrg_wind_1" id="p_nactscrooler_5">
					<div class="text">
						店铺名:${tenant.name}
					</div>
					<div class="text">
						店铺分类: ${(tenant.tenantCategory.name)!}
					</div>
					<div class="text">
						店铺性质: 
						[#if tenant.tenantType=="enterprise"]
							 企业单位
							[#elseif tenant.tenantType=="individual"]
							 个体经营
							[#elseif tenant.tenantType=="organization"]
							 事业单位或团体
							[#elseif tenant.tenantType=="personal"]
							 个人
							[/#if]
						${(tenant.community.name)!}
					</div>
					<div class="text">
						所在地区:${(tenant.area)!}
					</div>
					<div class="text">
						地址:${tenant.address}
					</div>
					<div class="text">
						联系人/法人: ${tenant.legalRepr}
					</div>
					<div class="text">
						联系电话: ${tenant.telephone}
					</div>
					<div class="text">
						经营许可证: ${tenant.licenseCode}
					</div>
					<div class="text">
						审核状态: 
							[#if tenant.status=="wait"]
							  申请中
							[#elseif tenant.status=="success"]
							  已认证
							[#elseif tenant.status=="fail"]
							  已驳回
							[#else]
							  未认证
							[/#if]
					</div>
					<div class="text">
						<a href="javascript:;" class="p_nopwbtn" id="back_shop">前往商铺列表</a>
					</div>
				</div>
				
			</div>
		</div>
	</article>
</section>
<div class="p_searchfixed">
	<div class="p_search_icon"></div>
	<input type="text" placeholder="请输入搜索内容"/>
	<div class="p_search_button">搜索</div>
</div>
<div class="p_windowbg_1"></div>
<div class="p_receiptbg"></div>
</body>
</html>
