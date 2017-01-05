<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>我的订单</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/myorder.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/iscroll.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
//json时间格式的格式化显示
Date.prototype.Format = function (fmt) {  
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) 
    	fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
    	fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
$().ready(function(){
	var $orderDetail = $("a[name='seeAll']");
	
	$(".order_screen").find("li").on("tap",function(){
		mtips("查询中。。");
		location.href="${base}/mobile/member/order/list.jhtml?type="+$(this).attr("id"); return false;
	});
	
	//订单详情
	$orderDetail.live('tap',function(){
		var $this = $(this);
		var sn = $this.attr("sn");
		location.href="${base}/mobile/member/order/tradeView.jhtml?sn="+sn; return false;
	});
	
	
	//返回按钮事件
	 $("#backapp").on("tap",function(){
		 location.href="vsstoo://appback/?backapp=true";
	 })
	//待支付
	var $waitPay=$(".waitPay");
	$waitPay.live("tap",function(){
		var $this =$(this);
		$.ajax({
			url:"${base}/mobile/payment/submit.jhtml",
			data:{sn:$this.attr("sn"),type:"payment",paymentPluginId:'unionpayMobilePlugin'},
			dataType:"json",
			type:"post",
			beforeSend:function(){
				$waitPay.prop("disabled",true);
				mtips("支付中。。。");
			},
			success:function(data){
				if(data.type=="success"){
					location.href="vsstoo://payservice/?token=none&paydata="+data.content;
					return false;
				}else{
					mtips(data.content);
				}
			},
			complete:function(){
				$waitPay.prop("disabled",false);
			}
		});
	});
	//取消订单
	var $cancel_order=$(".cancel_order");
	$cancel_order.live("tap",function(){
		var $this =$(this);
		$.ajax({
			url:"${base}/mobile/member/order/cancel.jhtml",
			data:{sn:$this.attr("sn")},
			dataType:"json",
			type:"post",
			beforeSend:function(){
				$cancel_order.prop("disabled",true);
			},
			success:function(data){
				location.reload();
			},
			complete:function(){
				$cancel_order.prop("disabled",false);
			}
		});
	});
	//子订单签收
	$(".sign_order").live("tap",function(){
		var sn =$(this).attr("sn");
		if (confirm("是否签收？")) {
			$.ajax({
				url: "${base}/mobile/member/trade/complete.jhtml?sn="+$(this).attr("sn"),
				type: "POST",
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type == "success") {
						mtips("签收成功！");
						setTimeout(function(){
							location.href="${base}/mobile/member/trade/review.jhtml?sn="+sn; return false;
						},1000);
					} else {
						mtips(message.content);
					}
				}
			});
		}
		return false;
	});
});

// 2015-1-11 拖动加载

var myScroll,pullUpEl, pullUpOffset,generatedCount = 0;
var pageNum = ${page.pageable.pageNumber};
function pullUpAction () {
	setTimeout(function () {	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
		if(pageNum>=(${page.total/page.pageable.pageSize})){
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
			return false;
		}
		var el, li, i;
		el = document.getElementById('orderList');
		pageNum=pageNum+1;
		$.ajax({
			url:"${base}/mobile/member/order/addMore.jhtml",
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum,type:"${type}"},
			success:function(list){
				if(list!=null&&list.length>0){
					for(var i=0;i<list.length;i++){
							var date = new Date(list[i].createDate);
							var Ordertime = date.Format("yyyy-MM-dd hh:mm:ss");
							li = document.createElement('li');
							var tmpStr="";
							tmpStr+="<div class='goods_operate'>";
										for(var j=0;j<list[i].finalOrderStatus.lenght;j++ ){
											if(list[i].finalOrderStatus[j].status=='isExpired'){
												tmpStr+="<a class='o_button_gray' href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='cancelled'){
												tmpStr+="<a class='o_button_gray' href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='toReview'){
												tmpStr+="<a class='o_button_red reviewTrade'  sn="+list[i].sn+" href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='completed'){
												tmpStr+="<a class='o_button_gray' href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='waitReturn'){
												tmpStr+="<a class='o_button_gray' href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='waitShipping'){
												tmpStr+="<a class='o_button_gray' href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='sign'){
												tmpStr+="<a class='o_button_red sign_order'  sn="+list[i].sn+" href='javascript:;'>"+list[i].finalOrderStatus[j].desc+"</a>";
											}else if(list[i].finalOrderStatus[j].status=='waitPay'){
												tmpStr+="<a class='o_button_red cancel_order' sn="+list[i].order.sn+" href='javascript:;'>取消</a>";
												tmpStr+="<a class='o_button_red waitPay' href='javascript:;' sn="+list[i].order.sn+">"+list[i].finalOrderStatus[j].desc+"</a>";
											}
										}
										tmpStr+="</div>";
										tmpStr+="</div>";
								
							li.innerHTML =tmpStr;
	
							el.appendChild(li, el.childNodes[0]);
					}
				}else{
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
					return false;
				}
				$('#m_scrooler_0').iScroll('refresh');// 请记住，当刷新内容加载（即：在阿贾克斯完成
			}
		});
	}, 1000);	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
}

function loaded() {
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	
	myScroll = new iScroll('m_scrooler_0', {
		onRefresh: function () {
			if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '拉起加载更多...';
			}
		},
		onScrollMove: function () {
			if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
				pullUpEl.className = 'flip';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '正在刷新...';
				this.maxScrollY = this.maxScrollY;
			} else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '正在刷新...';
				this.maxScrollY = pullUpOffset;
			}
		},
		onScrollEnd: function () {
			if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
				pullUpAction();	// Execute custom function (ajax call?)
			}
		}
	});
	
}

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
</script>
</head>

<body>
<section class="m_section">
  <header class="m_header">
	    <div class="m_headercont_1">
		      <div class="m_return" ><a href="javascript:;" alt="返回" id="backapp"><div class="p_datag">返回</div></a></div>
		      [#include "/mobile/include/top_search.ftl" /]
		      <div class="m_title" alt="选择日期">我的订单</div>
	    </div>
  </header>
  
  <div class="order_screen">
	    <ul>
		      <li id="all" class="[#if type=='all']current_screen[/#if]" ><img height="20px" src="${base}/resources/mobile/images/all-order.png" /><p>全部</p></li>
		      <li id="unpaid" class="[#if type=='unpaid']current_screen[/#if]"><img height="20px" src="${base}/resources/mobile/images/pay.png" /><p>待付款</p>[#if waitPayCount>0]<span>${waitPayCount}</span>[/#if]</li>
		      <li id="unshipped" class="[#if type=='unshipped']current_screen[/#if]"><img height="20px" src="${base}/resources/mobile/images/send.png" /><p>待发货</p>[#if waitShippingCount>0]<span>${waitShippingCount}</span>[/#if]</li>
		      <li id="shipped" class="[#if type=='shipped']current_screen[/#if]"><img height="20px" src="${base}/resources/mobile/images/get.png" /><p>待收货</p></li>
		      <li id="unreview" class="[#if type=='unreview']current_screen[/#if]"><img height="20px" src="${base}/resources/mobile/images/appraise.png" /><p>待评价</p>[#if waitReviewCount>0]<span>${waitReviewCount}</span>[/#if]</li>
	    </ul>
  </div>
  <article class="order_list" id="m_scrooler_0" style="display:block">
    <div class="order_content m_bodycont_1">
      	[#if page.content?? && page.content?has_content]
	      <ul name="orders" id="orderList">
	      	[#list page.content as trade]
		      	[#list trade.orderItems as orderItem]
			      	[#if orderItem_index == 0]
			        <li>
				          <div class="order_num"><a href="javascript:;">订单号:${trade.order.sn}</a><p class="order_time">${trade.createDate?string("yyyy-MM-dd HH:mm:SS")}</p></div>
				          <div class="order_goods">
					          	<a class="order_goods_img" href="#"><img src="${orderItem.thumbnail}"/></a>
					            <a href="javascript:;" class="order_goods_a">
						            <div class="order_goods_c">
							              <p class="goods_name">${orderItem.fullName}</p>
							              <p class="goods_style">[#if orderItem.product??&&orderItem.product.specification_value?has_content]${orderItem.product.specification_value}[/#if]</p>
						            </div>
						            <div class="order_goods_r">
							              <p class="goods_price">${currency(orderItem.price,true)}</p>
							              <p class="goods_amount">x${orderItem.quantity}</p>
							              <p class="shop_name">${orderItem.trade.tenant.name}</p>
						            </div>
				           		 </a>
				          </div>
			       		  <div class="goods_details"><a href="javascript:;" name="seeAll" sn="${trade.sn}">查看详情</a></div>
				          <div class="goods_others">
					            <div class="goods_total">
						              <p class="amount_title">数量</p>
						              <p class="total_amount">${trade.quantity}</p>
						              <p class="total_title">总计</p>
						              <p class="total_price">${currency(trade.amount,true)}</p>
					            </div>
					            <div class="goods_operate">
						           [#list trade.finalOrderStatus as fos]
					            		[#if fos.status=='isExpired']
						             		<a class="o_button_gray" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='cancelled']	
								            <a class="o_button_gray" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='toReview']	
							             	<a class="o_button_red reviewTrade" href="javascript:;" sn="${trade.sn}" >${fos.desc}</a>
						             	[#elseif fos.status=='completed']	
						             		<a class="o_button_gray" href="javascript:;">${fos.desc}<${message("Order.ShippingStatus."+trade.shippingStatus)}></a>
						             	[#elseif fos.status=='waitReturn']	
						             		<a class="o_button_gray" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='waitShipping']	
						             		<a class="o_button_gray" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='sign']	
						             		<a class="o_button_red sign_order" sn="${trade.sn}" href="javascript:;">${fos.desc}</a>
						             	[#elseif fos.status=='waitPay']	
						             		<a class="o_button_red cancel_order" sn="${trade.order.sn}" href="javascript:;">取消</a>
						             		<a class="o_button_red waitPay" sn="${trade.order.sn}" href="javascript:;">${fos.desc}</a> 
					            		[/#if]
					            	[/#list]
					            </div>
				          </div>
			        </li>
			        [/#if]
		        [/#list]
	        [/#list]
	      </ul>
	      <div id="pullUp">
				<span class="pullUpLabel">拉起加载更多...</span>
		  </div>
      [#else]
	      <div id="pullUp">
				<span class="pullUpLabel">暂无待收货订单!</span>
		  </div>
      [/#if]
    </div>
  </article>
</section>
</body>
</html>
