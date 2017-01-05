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
<script type="text/javascript" src="${base}/resources/mobile/js/orders.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $adress =$(".addressselect");
	var $submit =$("#submit");
	var $phone =$("#phone");
	var $receiverForm =$("#receiverForm");
	var $address=$("#address");
	var $consignee=$("#consignee");
	var $reset=$("#reset");
	$reset.on("tap",function(){
		$consignee.val('');
		$address.val('');
		$phone.val('');
	});
	$adress.on("tap",function(){
		var $this = $(this);
		location.href="${base}/mobile/groupon/detail/${promotionId}.jhtml?receiverId="+$this.attr("receiverId")+"&quantity=${quantity}"; return false;
	});
	var $qy1=$("#qy1");
	var $qy2=$("#qy2");
	var $qy3=$("#qy3");
	var qy2,qy3;
	var $m_areascrooler_1=$("#m_areascrooler_1 ul");
	var $m_areascrooler_2=$("#m_areascrooler_2 ul");
	var $m_areascrooler_3=$("#m_areascrooler_3 ul");
	var $m_areascrooler_li1=$("#m_areascrooler_1 ul li");
	var $m_areascrooler_li2=$("#m_areascrooler_2 ul li");
	var $m_areascrooler_li3=$("#m_areascrooler_3 ul li");
	
	$m_areascrooler_li1.on("tap",function(){
		var $this=$(this);
		$qy1.find("span").text($this.find("a").text());
		$.ajax({
			url:"${base}/common/area.jhtml",
			type:"get",
			data:{parentId:$this.attr("areaId")},
			dataType:"json",
			beforeSend:function(){
				$qy1.prop("disabled",true);
				$qy2.prop("disabled",true);
				$qy3.prop("disabled",true);
				$m_areascrooler_2.empty();
				$m_areascrooler_3.empty();
			},
			success:function(data){
				if(data!=null){
					$.each(data,function(i,area){
						$m_areascrooler_2.append("<li areaId='"+ i +"'><a  href='javascript:;'>"+area+"</a></li>");
					})
				}
			},
			complete:function(){
				$qy1.prop("disabled",false);
				$qy2.prop("disabled",false);
				$qy3.prop("disabled",false);
			}
		});
	});
	$m_areascrooler_li2.live("tap",function(){
		var $this=$(this);
		qy2=$this.attr("areaId");
		qy3="";
		$("#areaId").val(qy2);
		$qy2.find("span").text($this.find("a").text());
		$.ajax({
			url:"${base}/common/area.jhtml",
			type:"get",
			data:{parentId:$this.attr("areaId")},
			dataType:"json",
			beforeSend:function(){
				$qy1.prop("disabled",true);
				$qy2.prop("disabled",true);
				$qy3.prop("disabled",true);
				$m_areascrooler_3.empty();
			},
			success:function(data){
				if(data!=null){
					$.each(data,function(i,area){
						$m_areascrooler_3.append("<li areaId='"+ i +"'><a  href='javascript:;'>"+area+"</a></li>");
					})
				}
			},
			complete:function(){
				$qy1.prop("disabled",false);
				$qy2.prop("disabled",false);
				$qy3.prop("disabled",false);
			}
		});
	});
	$m_areascrooler_li3.live("tap",function(){
		var $this=$(this);
		qy3=$this.attr("areaId");
		$("#areaId").val(qy3);
		$qy3.find("span").text($this.find("a").text());
	});
	
	$submit.on("tap",function(){
		if(qy3==""&&qy2==""){
			mtips("请先选择区域"); return false;
		}
		if($address==""||$consignee==""||$phone==""){
			mtips("信息不完整");return false;
		}
		if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($phone.val()))){
			mtips("电话号码不正确");return false;
		}
		$.ajax({
			url:"${base}/mobile/member/order/save_receiver.jhtml",
			type:"post",
			data:$receiverForm.serialize(),
			dataType:"json",
			success:function(message){
				mtips(message.message.content);
				location.reload();
			}
		});
	});
	
	//电话
	$phone.on('keypress',function(event) {
		var key = event.keyCode ? event.keyCode : event.which;
		if ((key >= 48 && key <= 57) ) {
			return true;
		} else {
			return false;
		}
	});
	//选完区域后自动收缩
	$('.m_arear li').live('tap',function(){
		$(this).parents('.m_arear').hide();
	});
});
</script>
</head>
<body>
<section class="m_section">
	<header class="m_header">
		<div class="m_headercont_1">
			<div class="m_return"><a id="return_btn" href="javascript:;" alt="返回"><div class="p_datag">返回</div></a></div>
			<div class="m_title" alt="选择日期">选择地址</div>
			<div class="m_member m_dzadd"><a href="#">+</a></div>
		</div>
	</header>
	<div class="m_letter_list" id="m_letter_list">
		[#list phonetics as phonetic]
				<a href="#">${phonetic}</a>
			[/#list]
	</div>
	<article class="m_article m_article_1 m_article_writh" id="m_receiptcscroller">
		<div class="m_bodycont_1">
			<div class="m_receipttc">
			<ul class="bodycont_list">
				<div class="m_receiptsech">
					<div class="p_search_icon"></div>
					<input type="text" placeholder="请输入搜索内容">
					<div class="m_receiptsechbtn ddin">确定</div>
				</div>
				<div class="m_h10"></div>
				[#list phonetics as phonetic]
					[#list receivers as receiver]
						[#if receiver.phonetic==phonetic]
							<li class="anchor" id="${phonetic}">${phonetic}</li>
							<li class="addressselect" receiverId="${receiver.id}">
								<div class="m_address m_receiptadd">
									<p><i class="iconfont">&#xe608</i>${receiver.consignee}</p>
									<p><i class="iconfont">&#xe604</i>${receiver.phone}</p>
									<p><i class="iconfont">&#xe602</i>${receiver.address}</p>
								</div>
							</li>
						[/#if]
					[/#list]
				[/#list]
			</ul>
		</div>
		</div>
	</article>
	<div class="m_adddzc">
		<form id="receiverForm" action="${base}/mobile/member/receiver/save.jhtml" method="post">
			<input type="hidden" id="areaId" name="areaId" />
			<div class="m_adddzcul">
				<div class="p_tipsclose"></div>
				<p><i class="iconfont">&#xe608</i><input type="text" id="consignee" name="consignee" placeholder="请输入收件人姓名"></p>
				<div class="m_h10"></div>
				<p><i class="iconfont">&#xe604</i><input type="text" id="phone" name="phone" placeholder="请输入联系电话"></p>
				<div class="m_h10"></div>
				<div class="m_adddzc_area">
					<p class="m_arear_qy1" id="qy1"><span>区域</span></p>
					<p class="m_arear_qy2" id="qy2"><span>区域</span></p>
					<p class="m_arear_qy3" id="qy3"><span>区域</span></p>
				</div>
				<div class="m_h10"></div>
				<p><i class="iconfont">&#xe602</i><input type="text" id="address" name="address" placeholder="请输入收件地址"></p>
				<div class="m_h10"></div>
				<div class="m_arear m_arear_dz1" id="m_areascrooler_1">
					<ul>	
						[#list areas as area]
							<li areaId="${area.id}"><a href="javascript:;">${area.name}</a></li>
						[/#list]
					</ul>
				</div>
				<div class="m_arear m_arear_dz2" id="m_areascrooler_2">
					<ul>
					</ul>
				</div>
				<div class="m_arear m_arear_dz3" id="m_areascrooler_3">
					<ul>
					</ul>
				</div>
				<p><span><a id="submit" href="javascript:;">确定</a></span>  <span class="m_last" id="reset"><a href="javascript:;">重置</a></span></p>
			</div>
		</form>
	</div>
	<div class="m_bodybg_1"></div>
</section>
</body>
</html>
