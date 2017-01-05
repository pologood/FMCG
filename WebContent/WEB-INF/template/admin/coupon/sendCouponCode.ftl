<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.coupon.list")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $areaId = $("#areaId");
	var $queryMember = $("#queryMember");
	var $sendCoupon = $("#sendCoupon");

	[@flash_message /]
	
	$(function(){
		$("table[name='memberTable']").hide();
	});
	
	// 地区选择
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
	
	$queryMember.click(function(){
		$.ajax({
			url: "${base}/admin/coupon/queryMember.jhtml",
			type: "GET",
			data:{"area":$areaId.val(),"searchValue":$("#searchValue").val()},
			dataType: "json",
			cache: false,
			success: function(data) {
				$(".memberTr").remove();
				var length = data.length;
				if(length==0){
					$.message("warn", "未查到会员！请修改条件。");
					return;
				}
				for(var i=0;i<length;i++){
					var member = data[i];
					$("table[name='memberTable']").append(
					[@compress single_line = true]
					'<tr class="memberTr">
							<td >
								<input class="reqMember" type="checkbox" name="ids" value='+member.id+' \/>
							<\/td>
							<td>
								'+member.username+'
							<\/td>
							<td>
								'+member.name+'
							<\/td>
							<td>
								'+member.amount+'
							<\/td>
							<td>
								'+member.area.fullName+'
							<\/td>
						<\/tr>');
					[/@compress]
				}
			}
		});
	});
	
	$sendCoupon.click(function(){
		var memberIds=[];
		var $true=true;
		$("input[name='ids']").each(function () {
			var $this=$(this);
			if($this.attr("checked") == "checked") {	
				memberIds.push($this.attr("value"));
				$true=false;
			}
		});
		if ($true) {
			$.message("warn", "请勾选要发送的会员！");
			return false;
		}
		var couponIds=[];
		var reqCount=[];
		var availableCount=[];
		var $allZero = true;
		$("input[name='couponIds']").each(function () {
			var $this=$(this);
			if($this.attr("checked") == "checked") {	
				var couponId = $this.attr("value");
				couponIds.push(couponId);
				var reqC = $("#reqCount"+couponId).val();
				if(reqC!=0){
					$allZero = false;
				}
				reqCount.push(reqC);
				availableCount.push($("#availableCount"+couponId).attr("value"));
			}
		});
		if($allZero){
			$.message("warn", "发送数量不能全部为0！");
			return false;
		}
		$.ajax({
			url:"${base}/admin/coupon/send.jhtml",
			data:{mids:memberIds.join(","), cids:couponIds.join(","), rCounts:reqCount.join(","), aCount:availableCount.join(",")},
			type:"GET",
			dataType:"json",
			cache:false,
			success:function(msg){
				if(msg.error!=null){
					$.message("error",msg.error);
				} else if(msg.success!=null) {
					$.message("success",msg.success);
					window.location.href="${base}/admin/coupon/list.jhtml";
				}
			}
		});
	});
	
	$("#addSendMember").click(function(){
		$("table[name='memberTable']").show();
	});
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.coupon.list")}
	</div>
	<div class="bar">
		<a href="list.jhtml" class="iconButton">
			<span class="moveIcon">&nbsp;</span>返回到列表
		</a>
		<a href="javascript:;" id="addSendMember" class="iconButton">
			<span class="addIcon">&nbsp;</span>选择发放会员
		</a>
		<a href="#" id="sendCoupon" class="iconButton">
			<span class="upIcon">&nbsp;</span>发放
		</a>
	</div>
	<div>
		<table id="listTable" name="coupon" class="list">
			<tr>
				<th><input style="display:none;" type="checkbox" id="selectAllCoupon" checked="checked"/></th>
				<th><a href="javascript:;" class="sort" name="name">红包名称</a></th>
				<th><a href="javascript:;" class="sort" name="productCategory">可使用商品分类</a></th>
				<th><a href="javascript:;" class="sort" name="method">可支付类型</a></th>
				<th><a href="javascript:;" class="sort" name="memberCount">已发放量</a></th>
				<th><a href="javascript:;" class="sort" name="sendMoreCount">可发放数量</a></th>
				<th><a href="javascript:;" class="sort" name="totalCount">总量</a></th>
				<th><a href="javascript:;" class="sort" name="requiredCount">发放数量</a></th>
			</tr>
			[#list coupons as coupon]
			<tr>
				<td><input type="checkbox" style="display:none;" name="couponIds" value="${coupon.id}" checked="checked"/></td>
				<td>${coupon.name}</td>
				<td>
					[#assign productC='']
					[#list coupon.productCategory as pc]
						[#assign productC=productC+pc.name+',']
					[/#list]
					[#if productC?? && productC!='']
						[#assign productC=productC?substring(0,productC?length-1)]
						${productC}
					[#else]
						-
					[/#if]
				</td>
				<td>
					[#if coupon.method=='']
						无限制
					[#elseif coupon.method=='balance']
						平台账户支付
					[#elseif coupon.method=='online']
						网银支付
					[/#if]
				</td>
				<td>
					${coupon.sendCount}
				</td>
				<td>
					<span id="availableCount${coupon.id}" value="${coupon.count-coupon.sendCount}">${coupon.count-coupon.sendCount}</span>
				</td>
				<td>
					${coupon.count}
				</td>
				<td>
					<input type="text" name="${coupon.id}" class="reqCount" id="reqCount${coupon.id}" value="0" />
				</td>
			</tr>
			[/#list]
		</table>
	</div>
	<div class="tabContent">
		<table id="listTable" name="memberTable" class="list" >
			<tr>
				<td colspan="5">
					<span class="fieldSet">
						&nbsp;&nbsp;&nbsp;地区：
						<input type="hidden" id="areaId" name="areaId" maxlength="50"/>
					</span>
					<span class="fieldSet">
						&nbsp;&nbsp;&nbsp;账户名：
						<input type="text" id="searchValue" name="searchValue" value="" maxlength="200" />
					</span>
					<span class="fieldSet">
						<input type="button" class="button" id="queryMember" value="查询"/>
					</span>
				</td>
			</tr>
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="username">会员账号</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">会员名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="amount">消费金额</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="area">地区</a>
				</th>
			</tr>
		</table>
	</div>
</body>
</html>