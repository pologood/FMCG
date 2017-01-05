<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>验码提货</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> 
		[#include "/store/member/include/bootstrap_css.ftl"]
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
					<h1>我的订单<small>通过消费者手机收到的提货验码，验证提货</small>	</h1>
					<ol class="breadcrumb">
						<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                    	<li><a href="${base}/store/member/trade/list.jhtml">我的订单</a></li>
                    	<li><a href="${base}/store/member/trade/list.jhtml">订单管理</a></li>
						<li class="active">验码提货</li>
					</ol>
				</section>
				<!-- Main content -->
				<section class="content">
					<div class="row">
						<div class="col-md-12">
							<div class="nav-tabs-custom">
								<ul class="nav nav-tabs pull-right">
									<li class="pull-left header"><i class="fa fa-gift"></i>验码提货</li>
								</ul>
								<div class="tab-content">
									<div class="row">
										<div class="col-sm-12">
											<form id="listForm" action="search.jhtml" method="post">
												<div class="box">
													<div class="box-header with-border">
														<h3 class="box-title">输入提货码</h3>
													</div>
													<div class="box-body">
														<div class="row">
															<div class="col-sm-6"><input class="form-control" type="text" name="sn" id="sn" value="${sn}" placeholder="请输入提货码"></div>
															<div class="col-sm-2"><button type="submit" class="btn btn-block btn-primary">搜索</button></div>
														</div>
														<div class="row mt5">
															<div class="col-sm-6"><img src="${base}/resources/store/images/w_thq.jpg" alt=""></div>
														</div>
													</div>
													<div class="box-footer">
														<dl>
															[#if content=="1"] 
															<dt>查无此订单</dt>
															[#else]
															<dt>备注说明：</dt>
															<dd class="mt10">1.用户购买商品成功后，提货码以短信方式发送给用户，请填写正确地联系方式，注意查收短信；</dd>	
															<dd>2.用户提货成功并确认货物无误时，请在后台确认提货。</dd>
															<dd>3.最终解释权归所有，咨询热线：${setting.phone}</dd>
															[/#if]									
														</dl>
													</div>
												</div>
											</form>
										</div>
									</div>
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
		 <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
		 <script type="text/javascript">
			 $().ready(function () {
			 	$("#listForm").validate({
	  				rules: {
	  					sn: {
		  					required:true,
		  					digits:true
	  					}
	  				},
	  				messages:{
	  					sn: {
		  					required: "必填",
		  					digits:"请输入数字"
	  					}
	  				}
	  			});
		 	})
		</script>
	</body>
</html>