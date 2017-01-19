<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.member.view")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	table tr th{
		background-color:#3c8dbc;
	}
</style>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 店铺详情
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="店铺详情" />
		</li>
        <li>
            <input type="button" value="员工信息" />
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
				平台佣金:
			</th>
			<td>
				${tenant.brokerage}
			</td>
		</tr>
		<tr>
			<td colspan="4">
                <table class="input">
                    <tr style="text-align: center">
                        <th style="text-align: center">
                            logo
                        </th>
                        <th style="text-align: center;border-right:1px solid #dde9f5;border-left:1px solid #dde9f5;">
                            缩略图
                        </th>
                        <th style="text-align: center">
                            营业执照
                        </th>
                    </tr>
                    <tr>
                        <td>
                            <img src="${tenant.logo}" width="300" height="200">
                        </td>
                        <td style="text-align: center;border-right:1px solid #dde9f5;border-left:1px solid #dde9f5;">
                            <img src="${tenant.thumbnail}" width="300" height="200">
                        </td>
                        <td>
                            <img src="${tenant.licensePhoto}" width="300" height="200">
                        </td>
                    </tr>
                </table>
			</td>
            [#--<th style="text-align: center">--]
                [#--logo--]
            [#--</th>--]
            [#--<th style="text-align: center;border-right:1px solid #dde9f5;border-left:1px solid #dde9f5;">--]
                [#--缩略图--]
            [#--</th>--]
            [#--<th style="text-align: center">--]
                [#--营业执照--]
            [#--</th>--]
		</tr>
    </table>

        <table class="list tabContent" >
            <tr>
                <th>注册日期</th>
                <th>用户名</th>
                <th>员工姓名</th>
                <th>余额</th>
                <th>积分</th>
                <th>角色</th>
                <th>所属门店</th>
                <th>最近登录时间</th>
                <th>状态</th>
            </tr>
		[#list page.content as employee ]
            <tr>
                <td>
                    <span title="${employee.createDate?string("yyyy-MM-dd HH:mm:ss")}">${employee.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                </td>
                <td>
				${employee.member.username}
                </td>
                <td>
				${employee.member.name}
                </td>
                <td>
				${currency(employee.member.balance, true)}
                </td>
                <td>
				${employee.member.point}
                </td>
                <td>
				${employee.role}-
					[#list roles as rl]
						[#if employee.role?exists]
							[#list employee.role?split(",") as fileName]
								[#if fileName!=""]
									[#if fileName?contains(rl.id.toString())]${rl.name}&nbsp;[/#if]
								[/#if]
							[/#list]
						[/#if]
					[#if employee.role?contains(rl.id.toString())]${rl.name}&nbsp;[/#if]
					[/#list]
                </td>
                <td>
				${(employee.deliveryCenter.name)!}
                </td>
                <td>
					[#if employee.member.loginDate!=null]${employee.member.loginDate?string("yyyy-MM-dd HH:mm:ss")!"-"}[/#if]
                </td>
                <td style="border-right: 0px">
					[#if member.isEnabled ]
                        正常
					[#else]
                        禁用
					[/#if]
                </td>
            </tr>
		[/#list]
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