<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>退货管理</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> 
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
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
				<h1>我的退货<small>查询及处理店铺退货信息</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        			<li><a href="${base}/store/member/trade/list.jhtml">我的订单</a></li>
        			<li class="active">退货管理</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li [#if returnStatus=="cancelled" ]class="active" [/#if]>
									<a href="${base}/store/member/trade/return/list.jhtml?returnStatus=cancelled" roles="owner,manager,cashier" onclick="find_return(this)">已取消<span class="red">&nbsp;${cancelled_num!0}</span></a>
								</li>
								<li [#if returnStatus=="completed" ]class="active" [/#if]>
									<a href="${base}/store/member/trade/return/list.jhtml?returnStatus=completed" roles="owner,manager,cashier" onclick="find_return(this)">已完成<span class="red">&nbsp;${completed_num!0}</span></a>
								</li>
								[#if versionType==1]
								<li [#if returnStatus=="audited" ]class="active" [/#if]>
									<a href="${base}/store/member/trade/return/list.jhtml?returnStatus=audited" id="audited" roles="owner,manager,cashier" onclick="find_return(this)">已认证<span class="red">&nbsp;${audited_num!0}</span></a>
								</li>
								<li [#if returnStatus=="confirmed" ]class="active" [/#if]>
									<a href="${base}/store/member/trade/return/list.jhtml?returnStatus=confirmed" roles="owner,manager,cashier" onclick="find_return(this)">已受理<span class="red">&nbsp;${confirmed_num!0}</span></a>
								</li>
								[/#if]
								<li [#if returnStatus=="unconfirmed" ]class="active" [/#if]>
									<a href="${base}/store/member/trade/return/list.jhtml?returnStatus=unconfirmed" roles="owner,manager,cashier">待受理<span class="red">&nbsp;${unconfirmed_num!0}</span></a>
								</li>
								<li class="pull-left header"><i class="fa fa-truck"></i>退货管理</li>
							</ul>
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form id="listForm" action="list.jhtml" method="get">
									<input type="hidden" name="returnStatus" value="${returnStatus}"/>
									<!--导航功能-->
									<div class="row mtb10">
										<div class="col-sm-7">
											<div class="btn-group">
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
													<input type="text" class="form-control pull-right" id="keyword" name="keyword" value="${keyword}" placeholder="搜索编号">
													<div class="input-group-btn">
														<button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="box" style="border-top:0px;">
										<div class="box-body">
											<table id="example2" class="table table-bordered table-striped">
												<thead>
													<tr>
														<th>订单编号</th>
														<th>退货编号</th>
														<th>发货件数</th>
														<th>退货件数</th>
														<th>订单金额</th>
														<th>退货金额</th>
														<th>退货状态</th>
														<th>打印次数</th>
														<th>操作</th>
													</tr>										
												</thead>
												<tbody>
													[#list page.content as returns]
													<tr>
														<td>[#if returns.trade.order?has_content]${returns.trade.order.sn}[/#if]</td>
														<td>${returns.sn}</td>
														<td>${returns.trade.quantity}</td>
														<td>${returns.quantity}</td>
														<td>${returns.trade.amount}</td>
														<td>${returns.amount}</td>
														<td>
															[#if returns.returnStatus=='unconfirmed']
															待受理
															[#elseif returns.returnStatus=='confirmed']
															已受理
															[#elseif returns.returnStatus=='audited']
															已认证
															[#elseif returns.returnStatus=='completed']
															已完成
															[#elseif returns.returnStatus=='cancelled']
															[#if returns.memo?has_content]
															已拒绝
															<a href="javascript:;" onclick="check_refuse_re	turn('${returns.memo}')" data-toggle="modal" data-target="#refused_return_reasion_${returns.id}">查看</a>
															[#else]
															已取消
															[/#if]
															[/#if]
														</td>
														<!-- 查看拒绝退货弹框【 -->
														<div class="modal fade" id="refused_return_reasion_${returns.id}" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
															<div class="modal-dialog">
																<div class="modal-content" style=" border-radius: 5px;">
																	<div class="modal-header">
																		<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
																		<h4 class="modal-title">拒绝退货理由</h4>
																	</div>
																	<div class="modal-body">
																		<lebal>${returns.memo}</lebal>
																	</div>
																	<div class="modal-footer">
																		<div class="col-sm-offset-0 col-sm-2">
																			<input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
																		</div>
																	</div>
																</div>
															</div>
														</div>
														<!--查看拒绝退货弹框】 -->
														<td>${(returns.print)!"-"}</td>
														<td>
															<a href="view.jhtml?spReturnsId=${returns.id}">[查看]</a>
															[#if returns.returnStatus=='completed']
															<a href="${base}/store/member/trade/return/print.jhtml?spReturnsId=${returns.id}" target="_black">[打印]</a>
															[/#if]
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
	<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
</body>
</html>