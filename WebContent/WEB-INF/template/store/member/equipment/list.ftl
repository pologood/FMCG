<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>购物屏管理</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> 
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" href="${base}/resources/store/css/style.css">
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
				<h1>我的店铺<small>购物屏列表</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          			<li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
          			<li class="active">购物屏列表</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-tv"></i>购物屏管理</li>
							</ul>
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form class="form-horizontal" role="form" id="listForm" action="list.jhtml" method="get">
									<input type="hidden" id="type" name="type" value="${type}"/>
									<input type="hidden" id="status" name="status" value="${status}"/>
									<!--导航功能-->
									<div class="row mtb10">
										<div class="col-sm-7">
											<div class="btn-group">
												<button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();"><i class="fa fa-refresh mr5"></i> 刷新
												</button>
												<div class="dropdown fl ml5">
													<button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown">类型<span class="caret"></span>
													</button>
													<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu2" id="typeOption">
														<li role="presentation" [#if type=='tenant']class="active"[/#if]>
															<a role="menuitem" tabindex="-1" val="tenant">本店的屏</a>
														</li>
														<li role="presentation" [#if type=='store']class="active"[/#if]>
															<a role="menuitem" tabindex="-1" val="store">他店的屏</a>
														</li>
													</ul>
												</div>
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
											<!-- <div class="col-sm-5">
												<div class="box-tools fr">
													<div class="input-group input-group-sm" style="width: 150px;">
														<input type="text" class="form-control pull-right" id="searchValue" name="searchValue" value="${page.searchValue}" placeholder="Search">
														<div class="input-group-btn">
															<button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
														</div>
													</div>
												</div>
											</div> -->
										</div>
										<!--table-->
										<div class="box" style="border-top:0px;">
											<div class="box-body">
												<table id="listTable" class="table table-bordered table-striped">
													<thead>
														<tr>
															<th class="check"><input type="checkbox" id="selectAll"/></th>
															<th>地区</th>
															<th>名称</th>
															<th>标签</th>
															<th>地址</th>
															<th>状态</th>
															<th>操作</th>
														</tr>
													</thead>
													<tbody>
														[#list page.content as equipment]
														<tr>
															<td><input type="checkbox" name="ids" value="${equipment.id}"/></td>
															<td>${equipment.areaName}</td>
															<td>
																${abbreviate(equipment.tenantName,25,"..")}
															</td>
															<td>
																[#list equipment.tags as tag]
																${tag.name}[#if tag_has_next],[/#if]
																[/#list]
															</td>
															<td>${abbreviate(equipment.address,40,"..")}</td>
															<td>[#if equipment.setStatus=="success"]已开启[#else]未开启[/#if]</td>
															<td>
																[@helperRole url="helper/member/equipment/list.jhtml" type="read"]
																[#if helperRole.retOper!="0"]
																<a href="javascript:;" onclick="roleRoot('detail.jhtml?id=${equipment.id}&type=${type}','owner,manager');">详情</a>
																[/#if]
																[/@helperRole]
															</td>
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
		<script type="text/javascript">
			$().ready(function () {
				var $listForm = $("#listForm");
				var $type = $("#type");
				var $status = $("#status");
				var $typeOption = $("#typeOption a");
				$typeOption.click(function () {
					var $this = $(this);
					$type.val($this.attr("val"));
					$listForm.submit();
					return false;
				});
			});
		</script>
	</body>
	</html>