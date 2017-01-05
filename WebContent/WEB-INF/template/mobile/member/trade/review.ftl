<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
<title>订单详情</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $reset=$("#reset");
	var $content=$("textarea[name='content']");
	var $submit=$("#submit");
	var $allscore=$("i[name='score']");
	
	$submit.on('tap',function(){
		var content = $content.val().trim();
		if(content==''){
			mtips("亲！评价内容不能为空哦");
			return false;
		}
		var xs=$(".w_satrlist_1").find("i.w_down").length;
		var type;
		if(xs<=1){
			type="negative";
		}else if(xs<=3){
			type="moderate";
		}else if(xs<=5){
			type="positive";
		}
		var score=$(".w_satrlist_2").find("i.w_down").length;
		var assistant=$(".w_satrlist_3").find("i.w_down").length;
		 $.ajax({
				url :'${base}/mobile/member/review/save.jhtml',
				data:{id:${trade.id},score:score,type:type,assistant:assistant,content:content},
				type:'post',
				dataType:'json',
				success:function(data){
					if(data.type='success'){
						mtips(data.content);
						setTimeout(function(){location.href="${base}/mobile/member/order/tradeView.jhtml?sn=${trade.sn}";}, 1500);
					}else{
						mtips(data.content);
						return false;
					}
				}
		  });
		
	});
	
	$reset.on('tap',function(){
		$content.val("");
	});
	$("#return_btn").on("tap",function(){
		location.href="${base}/mobile/member/order/list.jhtml"; return false;
	});
});	
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			<div class="m_search m_search_n m_search_style">
				<div class="p_search_icon"></div>
				<input type="text" placeholder="请输入搜索内容">
				<div class="m_searchbtn">搜索</div>
			</div>
			<div class="m_title" alt="选择日期">我要评价</div>
			<!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> -->
		</div>
	</header>
	<article class="m_article m_article_1 m_article_writh" id="m_scrooler">
		<div class="m_bodycont_1 m_bodycont_bg">
			<div class="p_zxdder">
				<ul class="bodycont_list">
					[#assign count=5]
					<li>
						<a href="javascript:;" productId="">
							<img src="${trade.tenant.thumbnail}">
							<p>${trade.tenant.name}</p>
							<p>订单号：${trade.order.sn}</p>
							<p>下单时间：${trade.createDate?string('yyyy-MM-dd hh:mm:SS')}</p>
						</a>
					</li>
					<li>
						<div class="w_satrlist w_satrlist_1"><span>相识度</span>
							[#list 1..count as i]
								<i class="iconfont" name="type">&#xe609</i>
							[/#list]
						</div>
					</li>
					<li>
						<div class="w_satrlist w_satrlist_2"><span>服务</span>
							[#list 1..count as i]
								<i class="iconfont" name="score">&#xe609</i>
							[/#list]
						</div>
					</li>
					<li>
						<div class="w_satrlist w_satrlist_3"><span>物流</span>
							[#list 1..count as i]
								<i class="iconfont" name="assistant">&#xe609</i>
							[/#list]
						</div>
					</li>
					<li>
						<textarea type="text" placeholder="输入评价内容" name="content"></textarea>
					</li>
				</ul>
			</div>
			<div class="m_detailbtn m_detailbtn_pj">
			<ul>
				<li><a href="javascript:;" ><span id="submit">提交</span></a></li>
				<li><a href="javascript:;" ><span id="reset">重置</span></a></li>
			</ul>
		</div>
		</div>
	</article>
	<div class="m_bodybg"></div>
</section>
</body>
</html>
