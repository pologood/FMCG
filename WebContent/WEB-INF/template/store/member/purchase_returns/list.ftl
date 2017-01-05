<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>采购退货管理</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> [#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
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
				<h1>采购退货管理<small>查询采购退货单明细。</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
			        <li><a href="${base}/store/member/purchase/returns/list.jhtml">采购管理</a></li>
			        <li class="active">采购退货单</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-shopping-cart"></i>采购退货管理</li>
							</ul>
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form id="listForm" action="list.jhtml" method="get">
									<div class="row mtb10">
										<div class="col-sm-5">
											<div class="btn-group">
												<button type="button" class="btn btn-primary btn-sm" onclick="add_purchase_return()">
													<i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
												</button>
												<button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();"><i class="fa fa-refresh mr5"></i> 刷新
												</button>
												<button type="button" class="btn btn-default btn-sm" id="export_ss"><i class="fa fa-print mr5"></i> 导出
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
										<div class="col-sm-4">
											<div class="fl">
												<input type="text" id="beginDate" name="begin_date" value="${begin_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间"/>
											</div>
											<div class="fl">
												<i class="fa fa-exchange mid_po_icon"></i>
											</div>
											<div class="fl">
												<input type="text" id="endDate" name="end_date" value="${end_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="结束时间"/>
											</div>
											<div class="fl ml5">
						                        <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
						                    </div>
										</div>
										<div class="col-sm-3">
											<div class="box-tools fr">
												<div class="input-group input-group-sm" style="width: 150px;">
													<input type="text" class="form-control pull-right" id="keyword" name="keywords" value="${keywords}" placeholder="搜索单号">
													<div class="input-group-btn">
														<button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="box" style="border-top:0px;">
										<div class="box-body">
											<table class="table table-bordered table-striped">
												<thead>
													<tr>
														<th>单据编号</th>
														<th>供应商</th>
														<th>单据状态</th>
														<th>创建日期</th>
														<th>操作</th>
													</tr>
												</thead>
												<tbody>
													[#list page.content as purchase]
													<tr>
														<td>${purchase.sn}</td>
														<td>${purchase.supplier.name}</td>
														<td>${message("Purchaase.Type."+purchase.type)}</td>
														<td>${purchase.purchaseDate?string("yyyy-MM-dd HH:mm:ss")}</td>
														<td><a href="${base}/store/member/purchase/returns/edit/${purchase.id}.jhtml?type=${purchase.type}">
															[#if purchase.type=='applied']
															<span style="color: red;">[审核]</span>
															[#else]
															[查看]
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
		[#include "/store/member/include/footer.ftl"]
	</div>
	<div id="trade_wrap"></div>
	[#include "/store/member/include/bootstrap_js.ftl"]
	<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
	<script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		$().ready(function () {
      		//导出数据到excel
		    $("#export_ss").click(function(){
		      	$.message("success","正在帮您导出，请稍后")
		      	$.ajax({
		      		url:"${base}/store/member/purchase/returns/export_list.jhtml",
		      		type:"get",
		      		data:{
		      			begin_date:$("#beginDate").val(),
		      			end_date:$("#endDate").val(),
		      			keywords:$("[name='keywords']").val()
		      		},
		      		async:false,
		      		dataType:"json",
		      		success:function(data){
		      			var html='<table style="display:none;" class="table2excel">'+
		      			'<thead>'+
		      			'<tr>'+
		      			'<th>单据编号</th>'+
		      			'<th>供应商</th>'+
		      			'<th>单据状态</th>'+
		      			'<th>创建日期</th>'+
		      			'<th></th>'+
		      			'</tr>'+
		      			'</thead>'+
		      			'<tbody>;'
		      			$.each(data,function(i,obj){
		      				html+='<tr>'+
		      				'<td>'+obj.sn+'</td>'+
		      				'<td>'+obj.supplier+'</td>'+
		      				'<td>'+obj.type+'</td>'+
		      				'<td>'+obj.date+'</td>'+
		      				'<td></td>'+
		      				'</tr>'+
		      				'<tr style="background-color:#e4e4e4;">'+
		      				'<td></td>'+
		      				'<td>商品名称</td>'+
		      				'<td>商品编号</td>'+
		      				'<td>商品价格</td>'+
		      				'<td>商品数量</td>'+
		      				'</tr>';
		      				$.each(obj.purchaseItem,function(j,item){
		      					html+='<tr style="background-color:#e4e4e4;">'+
		      					'<td></td>'+
		      					'<td>'+item.name+'</td>'+
		      					'<td>'+item.pSn+'</td>'+
		      					'<td>'+item.price+'</td>'+
		      					'<td>'+item.quantity+'</td>'+
		      					'</tr>';
		      				});
		      			});
		      			html+='</tbody>'+
		      			'</table>';
		      			$("#trade_wrap").html(html);
		      		}
		      	});
				$(".table2excel").table2excel({
					exclude: ".noExl",
					name: "采购退货单",
					filename: "采购退货单",
					fileext: ".xls",
					exclude_img: true,
					exclude_links: false,
					exclude_inputs: true 
				});
			});
		});
		function add_purchase_return(){
			//针对聚德惠月结功能
	        if("${isMonthly}"=="true"){
	            $.message("warn", "今日有月结操作，不能进行此操作");
	            return;
	        }
	        location.href="add.jhtml;"
		}
	</script>
</body>
</html>