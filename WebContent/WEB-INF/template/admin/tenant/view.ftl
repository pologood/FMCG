<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.member.view")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
	function getval(){
		alert($("#directory").val())
	}
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 店铺详情
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="店铺详情" />
		</li>
	</ul>
	<table class="input tabContent">
		
		<tr>
			<th>
				编号
			</th>
			<td>
				${tenant.id}
			</td>
			<th>
				入住时间
			</th>
			<td>
				${tenant.createDate?string('yyyy-MM-dd mm:HH:ss')}
			</td>
		</tr>
		
		<tr>
			<th>
				店铺名称
			</th>
			<td>
				${tenant.name}
			</td>
			<th>
				联系人/法人:
			</th>
			<td>
				${(tenant.legalRepr)!'--'}
			</td>
		</tr>

		<tr>
			<th>
				店主:
			</th>
			<td>
				${tenant.member.name}
			</td>
			<th>
				店主电话
			</th>
			<td>
				${tenant.member.mobile}
			</td>
		</tr>
		
		<tr>
			<th>
				联系人
			</th>
			<td>
				${(tenant.linkman)!'--'}
			</td>
			<th>
				联系电话
			</th>
			<td>
				${(tenant.telephone)!'--'}
			</td>
		</tr>
		
		<tr>
			<th>
				商家状态:
			</th>
			<td>
				${message("Tenant.Status."+tenant.status)}
			</td>
			<th>
				店铺性质:
			</th>
			<td>
				[#if tenant.tenantType=="tenant"]经销商[#elseif tenant.tenantType=="supplier"]供应商[#else]零售商[/#if]
			</td>
		</tr>
		
		<tr>
			<th>
				所属地区:
			</th>
			<td>
				[#if tenant.area??]${(tenant.arae.name)!'--'}[#else]--[/#if]
			</td>
			<th>
				商铺地址:
			</th>
			<td>
				${tenant.address}
			</td>
		</tr>			
		
		<tr>
			<th>
				店铺分类:
			</th>
			<td>
				[#if tenant.tenantCategory??]${tenant.tenantCategory.name}[#else]--[/#if]
			</td>
			<th>
				门店数量:
			</th>
			<td>
				
				${(deliveryCenterSize)!'0'}
			</td>
		</tr>
		
		<tr>
			<th>
				商品数量:
			</th>
			<td>
				
				${(productSize)!'0'}
			</td>
			<th>
				促销商品数量:
			</th>
			<td>
				
				${(promotionSize)!'0'}
			</td>
		</tr>
		
		<tr>
			<th>
				会员数量:
			</th>
			<td>
				
				${(memberSize)!'0'}
			</td>
			<th>
				员工数量:
			</th>
			<td>
				${(employeSize)!'0'}
			</td>
		</tr>
		
		<tr>
			<th>
				是否开通:
			</th>
			<td>
				[#if tenant.isUnion=="true"]已开通[#else]未开通[/#if]
			</td>
			<th>
				屏联盟数量:
			</th>
			<td>
				${(unionTenants)!'0'}
			</td>
		</tr>
		
		<tr>
			<th>
				商家联盟:
			</th>
			<td>
				[#if tenant.unions??]${tenant.unions.name}[#else]--[/#if]
			</td>
			<th>
				联盟佣金:
			</th>
			<td>
				[#if tenant.unions??]${tenant.unions.brokerage}[#else]--[/#if]
			</td>
		</tr>
		
		<tr>
			<th>
				平台佣金:
			</th>
			<td>
				${tenant.brokerage}
			</td>
			<th>
				是否开通wifi:
			</th>
			<td>
				[#if tenant.isWifi=="true"]已开通[#else]未开通[/#if]
			</td>
		</tr>
		
		<tr>
			<th>
				是否开通云看店:
			</th>
			<td>
				[#if tenant.isCloudTenant=="true"]已开通[#else]未开通[/#if]
			</td>
			<th>
				是否开通购物屏:
			</th>
			<td>
				[#if tenant.isEquipment=="true"]已开通[#else]未开通[/#if]
			</td>
		</tr>
		
		<tr>
			<th>
				logo:
			</th>
			<td>
				<img src="${tenant.logo}" width="300" height="200">
			</td>
			<th>
				缩略图:
			</th>
			<td>
				<img src="${tenant.thumbnail}" width="300" height="200">
			</td>
		</tr>
		
		<tr>
			<th>
				营业执照:
			</th>
			<td>
				<img src="${tenant.licensePhoto}" width="300" height="200">
			</td>
			<th>
				
			</th>
			<td>
				
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