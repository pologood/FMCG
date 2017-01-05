<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>采购单</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" href="${base}/resources/store/css/common.css" />
	<link rel="stylesheet" href="${base}/resources/store/css/style.css" />
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
				<h1>采购管理<small>查询采购清单明细。</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                    <li><a href="${base}/store/member/purchase/returns/list.jhtml">采购管理</a></li>
                    <li><a href="${base}/store/member/purchase/returns/list.jhtml">采购单</a></li>
                    <li class="active">查看</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-file-text"></i>采购单</li>
							</ul>
							<div class="tab-content">
								<form class="form-horizontal" role="form">
									<div class="form-group">
										<label class="col-sm-2 control-label">单据编号：</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" value="${purchaseReturns.sn}" readonly>
										</div>
										<div class="col-sm-2"><a href="${base}/store/member/purchase/returns/print.jhtml?id=${purchaseReturns.id}" target="_blank"><button type="button" class="btn btn-block btn-default">打印</button></a></div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">*要货时间：</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" value="${(purchaseReturns.purchaseDate)!}" readonly>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">供应商：</label>
										<div class="col-sm-6">
											<input type="text" name="modifyDate" class="form-control" value="${(purchaseReturns.supplier.name)!}" readonly/>
										</div>
									</div>
									<!-- /.box -->
									<div class="row">
										<div class="col-sm-10 col-sm-offset-1">
											<div class="box box-info">
												<!-- /.box-header -->
												<div class="box-header with-border">															
													<h3 class="box-title">采购清单</h3>
												</div>
												<!-- /.box-body -->
												<div class="box-body">
													<table id="listTable" class="table table-bordered table-striped">
														<thead>
															<tr>
																<th style="width:200px;">商品名称</th>
																<th>商品编码</th>
																<th>供应商</th>
																<th>采购数量</th>
																<th>规格</th>
																<th>型号</th>
																<th>备注</th>
															</tr>
														</thead>
														<tbody>
															[#list purchaseReturns.purchaseItems as purchaseReturnsItem]
															<tr>
																<td title="${purchaseReturnsItem.name}">${abbreviate(purchaseReturnsItem.name,20,"...")}
																</td>
																<td>${purchaseReturnsItem.sn}</td>
																<td title="${purchaseReturns.supplier.name}">${abbreviate(purchaseReturns.supplier.name,20,"...")}</td>
																<td>${purchaseReturnsItem.quantity}</td>
																<td>${purchaseReturnsItem.spec}</td>
																<td>${purchaseReturnsItem.model}</td>
																<td>${purchaseReturnsItem.memo}</td>
															</tr>
															[/#list]
														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										[#if type=="applied"]
										<div class="col-sm-offset-1 col-sm-2">
											<button type="button" class="btn btn-block btn-primary" data-toggle="modal" id="agree_purchase_button">审核</button>
										</div>
										[/#if]
										<!-- 审核弹框【 -->
										<div class="modal fade" id="agree_purchase" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
											<div class="modal-dialog">
												<form class="form-horizontal" role="form">
													<div class="modal-content" style=" border-radius: 5px;">
														<div class="modal-header">
															<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
															<h4 class="modal-title">采购单审核</h4>
														</div>
														<div class="modal-body">
															<lebal>是否确认此操作？</lebal>
														</div>
														<div class="modal-footer">
															<div class="col-sm-offset-8 col-sm-2">
																<button type="button" class="btn btn-block btn-primary" id="confirm">确定</button>
															</div>
															<div class="col-sm-offset-0 col-sm-2">
																<input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
															</div>
														</div>
													</div>
												</form>
											</div>
										</div>
										<!--审核弹框】 -->
										<div class="[#if type=='applied']col-sm-offset-0][#else]col-sm-offset-1[/#if] col-sm-2">
											<button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
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
	<script type="text/javascript">
		$().ready(function () {
  			//取人采购单审核
	  		$("#confirm").click(function () {
	  			var _productSn = [], _quartitys = [];
	  			[#list purchaseReturns.purchaseItems as purchaseReturnsItem]
		  			_productSn.push(${purchaseReturnsItem.sn});
		  			_quartitys.push(${purchaseReturnsItem.quantity});
	  			[/#list]

	  			$.ajax({
	  				url: "${base}/store/member/purchase/returns/update/${purchaseReturns.id}.jhtml",
	  				data:{
	  					sns:_productSn,
	  					quartitys:_quartitys
	  				},
	  				type: "POST",
	  				dataType: "json",
	  				traditional: true,
	  				cache: false,
	  				success: function (map) {
	  					if (map.message.type == 'success') {
	  						location.href = "${base}/store/member/purchase/returns/list.jhtml";
	  					}
	  				}
	  			});     
	  		});
	  		//控制按钮是否要弹框
			$("#agree_purchase_button").click(function(){
				//针对聚德惠月结功能
                if("${isMonthly}"=="true"){
                    $.message("warn", "今日有月结操作，不能进行此操作");
                    return;
                }
                $("#agree_purchase_button").attr("data-target","#agree_purchase");
			});
  		});

	</script>
</body>
</html>