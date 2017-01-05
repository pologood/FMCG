<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<meta name="baidu-site-verification" content="7EKp4TWRZT"/>
	[@seo type = "index"]
	<title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
	[#if seo.keywords??]
	<meta name="keywords" content="[@seo.keywords?interpret /]"/>
	[/#if]
	[#if seo.description??]
	<meta name="description" content="[@seo.description?interpret /]"/>
	[/#if]
	[/@seo]
	[#include "/store/member/include/bootstrap_css.ftl" /]
	<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
	<link href="${base}/resources/store/css/style.css" type="text/css" rel="stylesheet"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
	[#include "/store/member/include/header.ftl" /]
	[#include "/store/member/include/menu.ftl" /]
	<div class="wrapper">
		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Main content -->
			<section class="content-header">
				<h1>
					我的会员
					<small>尊敬商家用户，此模块能帮你管理我的会员。</small>
				</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
					<li class="active">客户管理</li>
					<li class="active">编辑</li>
				</ol>
			</section>
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-truck"></i>管理我的会员</li>
							</ul>
							<div class="tab-content">
								<form class="form-horizontal" id="inputForm" action="edit.jhtml" method="post">
									<input type="hidden" name="id" value="${(consumer)!}"/>
									<div class="form-group">
										<label for="inputEmail3" class="col-sm-2 control-label">会员名称：</label>
										<div class="col-sm-10">
											<label class="col-sm-2 control-label" style="text-align: left">${(models.username)!}</label>
										</div>
									</div>
									<div class="form-group">
										<label for="inputPassword3" class="col-sm-2 control-label">姓名：</label>
										<div class="col-sm-10">
											<label class="col-sm-2 control-label" style="text-align: left">${(models.name)!}</label>
										</div>
									</div>
									<div class="form-group">
										<label for="inputPassword3" class="col-sm-2 control-label">联系电话：</label>
										<div class="col-sm-10">
											<label class="col-sm-2 control-label" style="text-align: left">${(models.mobile)!}</label>
										</div>
									</div>
									<div class="form-group">
										<label for="inputPassword3" class="col-sm-2 control-label">地址：</label>
										<div class="col-sm-10">
											<label class="col-sm-2 control-label" style="text-align: left">${(models.address)!}</label>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">等级：</label>
										<div class="col-sm-8">
											<select class="form-control" name="memberRankId">
												[#list memberRanks as memberRank]
												<option value="${memberRank.id}" [#if memberRank.id == models.memberRank.id]selected="selected"[/#if]>${memberRank.name}</option>
												[/#list]
											</select>
										</div>
									</div>

									<div class="form-group">
										<label class="col-sm-2 control-label">状态：</label>
										<div class="col-sm-8">
											<select class="form-control" name="status">
												<option value="none" [#if ("none" == (status)!)] selected[/#if]>待审核</option>
												<option value="enable" [#if ("enable" == (status)!)] selected[/#if]>已审核</option>
												<option value="disable" [#if ("disable" == (status)!)] selected[/#if]>已禁用</option>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label for="inputPassword3" class="col-sm-2 control-label">成交订单：</label>
										<div class="col-sm-10">
											<label class="col-sm-2 control-label" style="text-align: left">${(orders)!}笔，共${(amount)!}元</label>
										</div>
									</div>
									
									[#if trades??&&trades?has_content]
									<table id="example" class="table table-bordered table-striped mailbox-messages">
										<thead>
											<tr>
												<th>商品图片</th>
												<th>单号</th>
												<th>价格</th>
												<th>时间</th>
												<th>状态</th>
											</tr>
										</thead>
										<tbody>
											[#list trades as trade]
											<tr>
												<td><img src="${trade.thumbnail}" style="width: 30px;height: 30px;"></td>
												<td>${trade.sn}</td>
												<td>${(trade.amount)!}</td>
												<td>${trade.create_date}</td>
												<td>${trade.finalOrderStatus.desc}</td>
											</tr>
											[/#list]
										</tbody>
									</table>
									[/#if]
									
									
									<div class="form-group">
										[#if consumer??]
										<div class="col-sm-offset-2 col-sm-2">
											<button type="submit" class="btn btn-block btn-success" value="${message("admin.common.submit")}">确定</button>
										</div>
										[/#if]
										<div class="col-sm-offset-[#if consumer??]0[#else]2[/#if] col-sm-2">
											<button type="button" class="btn btn-block btn-default" value="${message("admin.common.back")}" onclick="location.href='list.jhtml?status=enable'">返回</button>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		[#include "/store/member/include/footer.ftl" /]
	</div>
	[#include "/store/member/include/bootstrap_js.ftl" /]
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
	<script type="text/javascript">
		$().ready(function () {
			if("${consumer}"==""){
				$.message("warn","当前会员不能进行编辑");
			}
			
		});
	</script>
</body>
</html>
