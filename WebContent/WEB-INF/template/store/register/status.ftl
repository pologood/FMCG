<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.register.title")}</title>
<link rel="shortcut icon" href="${base}/favicon.ico" />
<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${base}/resources/common/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/box/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/common/datePicker/WdatePicker.js"></script>
</head>
<body>
	<!-- head页头公共部分start -->
	<div class="center logo_div"><img src="${base}/resources/store/images/login_logo.gif" /></div>
	<!-- head页头公共部分end -->
	<div class="container1 register">
		<div class="span24">
			<div class="wrap">
				<div class="main clearfix">
					<div class="step step3">
						<ul>
							<li>用户注册</li>
							<li>完善信息</li>
							<li class="current">完成注册</li>
						</ul>
					</div>						
						<table class="input">
							<tr>
								<th>
									<span class="requiredField">*</span>店铺名称: 
								</th>
								<td>
									${(tenant.name)!}
								</td>
							</tr>
							
							<tr>
								<th>
									<span class="requiredField">*</span>所属地区:
								</th>
								<td>
										${(tenant.area)!}							
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>地址: 
								</th> 
								<td>
							   	${(tenant.address)!} 
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>联系人/法人: 
								</th>
								<td>
									${(tenant.linkman)!}
								</td>
							</tr>	
							<tr>
								<th>
									<span class="requiredField">*</span>联系电话: 
								</th>
								<td>
									${(tenant.telephone)!}
								</td>
							</tr>
		        			<tr>
								<th>
									审核状态: 
								</th>
								<td>
										[#if tenant.status=="wait"]
											 已确认&nbsp;&nbsp;&nbsp;
										[#elseif tenant.status=="success"]
						 					 已认证
										[#elseif tenant.status=="fail"]
											 已驳回
										[#else]
						 					 审核中
										[/#if]
								</td>
							</tr>
							<tr>
								<th>
								</th>
								<td>
									<a href="${base}/store/member/index.jhtml">[返回首页]</a>
								</td>
							</tr>
						</table>
				</div>
			</div>
		</div>
	</div>
	[#include "/store/include/footer.ftl" /]
</body>
</html>