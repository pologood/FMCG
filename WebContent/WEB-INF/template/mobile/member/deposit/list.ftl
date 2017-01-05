<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>我的账单</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/myorder.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	//返回按钮事件
	 $("#backapp").on("tap",function(){
		 location.href="vsstoo://appback/?backapp=true";
	 })
})
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
// 2015-1-11 拖动加载

var myScroll,pullUpEl, pullUpOffset,generatedCount = 0;
var pageNum = ${page.pageable.pageNumber};
function pullUpAction () {
	setTimeout(function () {	// <-- 模拟网络拥塞，从生产中删除setTimeout的！
		if(pageNum>=(${page.total/page.pageable.pageSize})){
			pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
			return false;
		}
		url = "${base}/mobile/member/deposit/addMore.jhtml";
		var el, li, i;
		el = document.getElementById('depositList');
		pageNum=pageNum+1;
		$.ajax({
			url:url,
			type:"get",
			dataType:"json",
			data:{pageNumber:pageNum},
			success:function(result){
				if(result.length>0){
					for(var i=0;i<result.length;i++){
						var date = new Date(result[i].createDate);
						var Ordertime = date.Format("yyyy-MM-dd hh:mm");
						li = document.createElement('li');
						var html='<div class="item-content">';
						html+='<div class="deposit_left" >';
						if(result[i].type=='Recharge'){
							html+='充值';
						}else if(result[i].type=='Payment'){
							html+='付款';
						}else if(result[i].type=='Credit'){
							html+='收款';
						}else if(result[i].type=='Chargeback'){
							html+='扣费';
						}else if(result[i].type=='Refunds'){
							html+='退款';
						}else if(result[i].type=='Profit'){
							html+='返利';
						}else if(result[i].type=='Rebate'){
							html+='分红';
						}	
						html+='</div>';	
						html+='<a href="javascript:;" class="item-details">';	
						html+='<div class="item-details-l">';	
						html+='<p class="bill-name">';	
						html+=result[i].memo;	
						html+='</p><p class="bill-time">';	
						html+=Ordertime;	
						html+='</p></div><div class="item-details-r">';	
						if(result[i].type=='Payment'||result[i].type=='Chargeback'){
							html+='<p class="bill-money" style="color: green" >';	
							html+='-'+result[i].debit;	
						}else{
							html+='<p class="bill-money" style="color: red" >';	
							html+='-'+result[i].credit;	
						}
						html+='<p class="bill-state">余额:'+result[i].balance+'</p>';	
						html+='</div></a></div>';	
						
						li.innerHTML =html;
						el.appendChild(li, el.childNodes[0]);
					}
					$('#m_scrooler_0').iScroll('refresh');// 请记住，当刷新内容加载（即：在阿贾克斯完成
				}else{
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '已经到底了...';
					return false;
				}
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
      <div class="m_return"><a href="javascript:;" id="backapp" alt="返回">
        <div class="p_datag">返回</div>
        </a></div>
    	[#include "/mobile/include/top_search.ftl" /]
      <div class="m_title" alt="选择日期">我的账单</div>
    </div>
  </header>
  <div class="bill-list" id="m_scrooler_0">
    <div class="bill-content">
     <div class="bill-title">
			        <p class="bill-in"  >总收入：${income}</p>
			        <p class="bill-out">总支出：${outcome}</p>
			    </div>
      [#if page.content??&&page.content?has_content]
     		<ul id="depositList">
     			[#list page.content as item]
     				<li>
			          <div class="item-content">
			            <div class="deposit_left" >
			            	[#if item.type=='Recharge']
			            		充值
			            	[#elseif item.type=='Payment']
			            		付款
			            	[#elseif item.type=='Credit']
			            		收款
			            	[#elseif item.type=='Chargeback']
			            		扣费
			            	[#elseif item.type=='Refunds']
			            		退款
			            	[#elseif item.type=='Profit']
			            		返利
			            	[#elseif item.type=='Rebate']
			            		分红
			            	[/#if]
			            </div>
			            <a href="javascript:;" class="item-details">
			              <div class="item-details-l">
			                <p class="bill-name">${item.memo}</p>
			                <p class="bill-time">${item.createDate?string('yyyy-MM-dd hh:MM')}</p>
			              </div>
			              <div class="item-details-r">
			                	[#if item.type=='Payment'||item.type=='Chargeback']
			                		<p class="bill-money" style="color: green" >-${item.debit}</p>
			                	[#else]
			                		<p class="bill-money" style="color: red">+${item.credit}</p>
			                	[/#if]	
			                <p class="bill-state">余额:${item.balance}</p>
			              </div>
			            </a>
			          </div>
			        </li>
     			[/#list]
     		</ul>
     		<div id="pullUp">
					<span class="pullUpLabel">拉起加载更多...</span>
			</div>
      [#else]
	      <div id="pullUp">
					<span class="pullUpLabel">暂无账单信息...</span>
			</div>
      [/#if]
    </div>
  </div>
</section>
  <div class="m_bodybg ddout" style="display: none;"></div>
</body>
</html>
