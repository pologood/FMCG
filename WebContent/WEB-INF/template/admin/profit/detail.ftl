<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.index.build")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 分润报表 &raquo; 查看详情
	</div>
	<table class="input tabContent">
		<tr>
			<th>
				创建日期：
			</th>
			<td>
				${(profit.createDate)!}
			</td>
		</tr>
		
		<tr>
			<th>
				分润类型:
			</th>
			<td>
				[#if profit.type=="order"]
					订单分润
				[#elseif profit.type=="fee"]
					功能年费
				[/#if]
			</td>
		</tr>
		
		<tr>
			<th>
				分润状态:
			</th>
			<td>
				[#if profit.status=="none"]
					未入账
				[#elseif profit.status=="share"]
					已入账
				[/#if]
			</td>
		</tr>
		<tr>
			<th>
				入账日期:
			</th>
			<td>
				${(profit.profitDate)!}
			</td>
		</tr>
		<tr>
			<th>
				分润金额:
			</th>
			<td>
				${(profit.amount)!}
			</td>
		</tr>
		<tr>
			<th>
				导购分润:
			</th>
			<td>
				${(profit.guideAmount)!}
			</td>
		</tr>			
		<tr>
			<th>
				店主分润:
			</th>
			<td>
				${(profit.guideOwnerAmount)!}	
			</td>
		</tr>
		
		<tr>
			<th>
				推广分润:
			</th>
			<td>
				${(profit.shareAmount)!}
			</td>
		</tr>
		<tr>
			<th>
				推广店主分润
			</th>
			<td>
				${(profit.shareOwnerAmount)!}	
			</td>
		</tr>
		<tr>
			<th>
				平台分润:
			</th>
			<td>
				${(profit.providerAmount)!}	
			</td>
		</tr>
		
		<tr>
			<th>
				层级:
			</th>
			<td>
				${(profit.level)!}	
			</td>
		</tr>
		<tr>
			<th>
				分润摘要:
			</th>
			<td>
				${(profit.memo)!}	
			</td>
		</tr>
		<tr>
			<th>
				消费者：
			<td>
				[#if profit.member?has_content]${(profit.member.name)!}（${(profit.member.username)!}）[/#if]
			</td>
		</tr>
		<tr>
			<th>
				导购员:
			</th>
			<td>
				[#if profit.guide?has_content]${(profit.guide.name)!}（${(profit.guide.username)!}）[/#if]
			</td>
		</tr>
		<tr>
			<th>
				发展者:
			</th>
			<td>
				[#if profit.share?has_content]${(profit.share.name)!}（${(profit.share.username)!}）[/#if]
			</td>
		</tr>
		<tr>
			<th>
				导购员所在店铺:
			</th>
			<td>
				[#if profit.guideOwner?has_content]${(profit.guideOwner.name)!}[/#if]
			</td>
		</tr>
		<tr>
			<th>
				发展者所在店铺:
			</th>
			<td>
				[#if profit.shareOwner?has_content]${(profit.shareOwner.name)!}[/#if]
			</td>
		</tr>
		<tr>
			<th>
				订单号:
			</th>
			<td>
				[#if profit.order?has_content]${(profit.order.sn)!}[/#if]
			</td>
		</tr>
	</table>
	<table class="input">
		<tr>
			<th>
				&nbsp;
			</th>
			<td>
				<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)" />
			</td>
		</tr>
	</table>
</body>
</html>