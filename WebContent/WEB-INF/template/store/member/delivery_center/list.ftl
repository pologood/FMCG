<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>门店管理</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" href="${base}/resources/store/css/style.css">
	<link rel="stylesheet" href="${base}/resources/store/css/common.css" />
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
				<h1>我的店铺<small>维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          			<li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
          			<li class="active">门店管理</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-institution"></i>门店管理</li>
							</ul>
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form class="form-horizontal" id="listForm" action="list.jhtml" method="get" role="form">
									<!--导航功能-->
									<div class="row mtb10">
										<div class="col-sm-7">
											<div class="btn-group">
												<button type="button" class="btn btn-primary btn-sm" onclick="javascript:location.href='add.jhtml';">
													<i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
												</button>
												<button type="button" class="btn btn-default btn-sm" id="deleteButton">
													<i class="fa fa-close mr5" aria-hidden="true"></i>删除
												</button>
												<button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();">
													<i class="fa fa-refresh mr5"></i> 刷新
												</button>

												<div class="dropdown fl ml5">
													<button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
														每页显示<span class="caret"></span>
													</button>
													<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="pageSizeOption">
														<li role="presentation" class="[#if page.pageSize==10]active[/#if]">
															<a role="menuitem" tabindex="-1" val="10">10</a>
														</li>
														<li role="presentation" class="[#if page.pageSize==20]active[/#if]">
															<a role="menuitem" tabindex="-1" val="20">20</a>
														</li>
														<li role="presentation" class="[#if page.pageSize==30]active[/#if]">
															<a role="menuitem" tabindex="-1" val="30">30</a>
														</li>
														<li role="presentation" class="[#if page.pageSize==40]active[/#if]">
															<a role="menuitem" tabindex="-1" val="40">40</a>
														</li>
													</ul>
												</div>
											</div>		
										</div>
										<div class="col-sm-5">
											<div class="box-tools fr">
												<div class="input-group input-group-sm" style="width: 150px;">
													<input type="text" class="form-control pull-right" id="searchValue" name="searchValue" value="${page.searchValue}" placeholder="搜索名称">
													<ul id="searchPropertyOption" style="display:none;">
														<li>
															<a style="cursor: pointer;" val="name"></a>
														</li>
														<li>
															<a style="cursor: pointer;" val="contact"></a>
														</li>
													</ul>
													<div class="input-group-btn">
														<button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<!--table-->
									<div class="box" style="border-top:0px;">
										<div class="box-body">
											<table id="listTable" class="table table-bordered table-striped">
												<thead>
													<tr>
														<th class="check"><input type="checkbox" id="selectAll"/></th>
														<th>名称</th>
														<th>地区名称</th>
														<th>地址</th>
														<th>联系人</th>
														<th>电话</th>
														<th>是否默认</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													[#list page.content as deliveryCenter]
													<tr>
														<td><input type="checkbox" name="ids" value="${deliveryCenter.id}"/></td>
														<td>${deliveryCenter.name}</td>
														<td>${deliveryCenter.areaName}</td>
														<td>${deliveryCenter.address}</td>
														<td>${deliveryCenter.contact}</td>
														<td>${deliveryCenter.mobile}</td>
														<td>
															<a href="" class="red">
																<i class="fa [#if deliveryCenter.isDefault=='true']fa-check[#else]fa-remove[/#if]"></i>
															</a>
														</td>
														<td><a href="${base}/store/member/delivery_center/edit.jhtml?id=${deliveryCenter.id}">[编辑]</a></td>
													</tr>
													[/#list]
												</tbody>
											</table>
											<div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
												[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
												[#include "/store/member/include/pagination.ftl"]
												[/@pagination]
											</div>
										</div>
									</div>
									
								</form>
							</div>
						</div>
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
	<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
</body>
</html>