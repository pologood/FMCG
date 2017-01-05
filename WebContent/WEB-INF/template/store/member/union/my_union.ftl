<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>我的商盟</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
	<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
	<style type="text/css">
		table th,table td{
			font-size: 20px;
			color: gray;
		}
		table tr{
			line-height: 45px;
		}
		table th{
			text-align: right;
		}
	</style>
</head>
<body class="hold-transition skin-blue sidebar-mini">

	<div class="wrapper">
		[#include "/store/member/include/header.ftl"]
		<!-- Left side column. contains the logo and sidebar -->
		[#include "/store/member/include/menu.ftl"]
		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<h1>
					商盟管理
					<small>处理商盟管理的信息</small>
				</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
					<li><a href="${base}/store/member/union/my_union.jhtml">商盟管理</a></li>
					<li class="active">我的商盟</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<!-- Left col -->
					<div class="col-md-12">
						<!-- Custom Tabs (Pulled to the right) -->
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class=""><a href="${base}/store/member/union/all_union.jhtml">全部商盟</a></li>
								<li class="active"><a href="${base}/store/member/union/my_union.jhtml">我的商盟</a></li>
								<li class="pull-left header"><i class="fa fa-th"></i>我的商盟</li>
							</ul>
							<div class="tab-content">
								<div class="row" style="height:50px;"></div>
								<div class="row">
									<div class="col-sm-offset-2 col-sm-3">
										<img class="img-responsive" src="${(unionTenants[0].union.image)!}" width="390px" alt="Photo">
									</div>
									<div class="col-sm-6">
										<table>
											<tr>
												<th>商盟名称：</th>
												<td>${(unionTenants[0].union.name)!}</td>
											</tr>
											<tr>
												<th>商家数量：</th>
												<td>${(unionTenants[0].union.tenantNumber)!}&nbsp;家</td>
											</tr>
											<tr>
												<th>商盟佣金：</th>
												<td>${(unionTenants[0].union.brokerage*100)!}%</td>
											</tr>
											<tr>
												<th>店铺佣金：</th>
												<td>${(unionTenants[0].tenant.brokerage*100)!}%</td>
											</tr>
											<tr>
												<th>加盟费用：</th>
												<td>${(unionTenants[0].price)!}&nbsp;元/年</td>
											</tr>
											<tr>
												<th>加盟状态：</th>
												<td>${message("UnionTenant.Status." + unionTenants[0].status)}</td>
											</tr>
											<tr>
												<th>有效期：</th>
												<td>${unionTenants[0].expire?string('yyyy-MM-dd HH:mm:ss')}</td>
											</tr>
											<tr>
												<th></th>
												<td>
													<input class="btn btn-primary" type="button" value="进入商盟" onclick="location.href='union_tenant_list.jhtml?unionId=${unionTenants[0].union.id}'">
												</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="row" style="height:100px;"></div>
							</div>
							<!-- /.tab-content -->
						</div>
						<!-- nav-tabs-custom -->
					</div>
				</div>
			</section>
			<!-- /.content -->
		</div>
		<!-- /.content-wrapper -->
		[#include "/store/member/include/footer.ftl"]
	</div>
	[#include "/store/member/include/bootstrap_js.ftl"]
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
	<script type="text/javascript">
		$(function(){

		});
	</script>
</body>
</html>
