<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>店铺资料</title>
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
				<h1>采购管理<small>完善我的店铺资料</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                    <li><a href="${base}/store/member/purchase/list.jhtml">采购管理</a></li>
                    <li><a href="${base}/store/member/purchase/list.jhtml">采购单</a></li>
                    <li class="active">添加</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-file-text-o"></i>店铺资料</li>
							</ul>

							<div class="tab-content">
								<form class="form-horizontal" role="form">
									<div class="form-group">
										<label class="col-sm-2 control-label">单据编号</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="sn" value="${code}" readonly>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">*要货时间</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="modifyDate" value="${(time)!}" readonly>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">供应商</label>
										<div class="col-sm-4">
											<select class="form-control" name="supplierId">
												<option value="">请选择</option>
												[#list suppliers as supplier]
												<option value="${supplier.parent.id}">${supplier.parent.name}</option>
												[/#list]
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">查找商品</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" name="keyword">
										</div>
										<div class="col-sm-2"><button type="button" id="search" class="btn btn-block btn-default">查询 </button></div>
									</div>
									<div class="row">
										<div class="col-sm-10 col-sm-offset-1">
											<div class="box box-info">
												<div class="box-header with-border">														
													<h3 class="box-title">采购清单</h3>
												</div>
												<div class="box-body">
													<iframe src=""  frameborder="0" scrolling="no" width="100%" height="" id="iframe_id">

													</iframe>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-1 col-sm-2">
											<button type="button" class="btn btn-block btn-primary" id="confirm">提交</button>
										</div>
										<div class="col-sm-offset-0 col-sm-2">
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
		[#include "/store/member/include/footer.ftl"]

	</div>
	[#include "/store/member/include/bootstrap_js.ftl"]
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
	<script type="text/javascript">
		$().ready(function () {
			var teble = $("table[class='list']");
			$("select[name='supplierId']").change(function () {
				var supplierId = $("select[name='supplierId']").children('option:selected').val();
				if(supplierId==""){
					$("#iframe_id").attr("src","");
					$("#iframe_id").attr("height","10px;")
				}else{
					$("#iframe_id").attr("src","${base}/store/member/purchase/supplier_list.jhtml?id="+supplierId);
					$("#iframe_id").attr("height","970px;");
				}

			});

			$("#search").click(function () {
				var supplierId = $("select[name='supplierId']").children('option:selected').val();
				var keyword = $("input[name='keyword']").val().trim();

				if (supplierId == "" || supplierId == null) {
					$.message("warn", "对不起，请先选择供应商！");
					return;
				}
				if (keyword == "" || keyword == null) {
					$.message("warn", "请输入你希望搜索到的商品信息！");
					return;
				}
				$(document.getElementById("iframe_id").contentWindow.document.body).find("#keywords").val(keyword);
				$(document.getElementById("iframe_id").contentWindow.document.body).find("#listForm").submit();

			});



			$("#confirm").click(function(){
				var sn = $("input[name='sn']").val();
				var modifyDate = $("input[name='modifyDate']").val();
				var supplierId = $("select[name='supplierId']").children('option:selected').val();
				var obj=$(document.getElementById("iframe_id").contentWindow.document.body).find(".list input[name='ids']");
				var checked_obj=[],content_obj=[],ids=[];
				for (var i = 0; i < obj.length; i++) {
					if (obj[i].checked == true) {
						checked_obj.push($(document.getElementById("iframe_id").contentWindow.document.body).find("input[name='stock"+obj[i].value+"']").val());
						content_obj.push($(document.getElementById("iframe_id").contentWindow.document.body).find("input[name='content"+obj[i].value+"']").val());
						ids.push(obj[i].value);
					}
				}
				
				if(supplierId==""||supplierId==null){
					$.message("warn","请选择供应商!");
					return;
				}

				if(checked_obj==""){
					$.message("warn","请选择需要采购的商品！");
					return;
				}

				$.ajax({
					url: "${base}/store/member/purchase/save.jhtml",
					data: {
						sn:sn,
						supplierId: supplierId,
						purchaseDate:modifyDate,
						quartitys:checked_obj,
						memos:content_obj,
						ids:ids
					},
					type: "POST",
					dataType: "json",
					cache: false,
					traditional: true,
					success: function (map) {
						if(map.message.type=='success'){
							location.href = "${base}/store/member/purchase/list.jhtml";
						}
					}
				});

			});
		});
	</script>
</body>
</html>