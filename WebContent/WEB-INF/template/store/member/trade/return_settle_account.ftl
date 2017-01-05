<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>退货结算</title>
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
				<h1>我的订单<small>管理我的往来结算</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/trade/return_settle_account.jhtml">往来结算</a></li>
          <li class="active">退货结算</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">         
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li>
									<a href="${base}/store/member/trade/withdraw_cash_settle_account.jhtml">提现结算</a>
								</li>
								<li class="active">
									<a href="${base}/store/member/trade/return_settle_account.jhtml">退货结算</a>
								</li>
								<li>
									<a href="${base}/store/member/trade/order_settle_account.jhtml">订单结算</a>
								</li>
								<li class="pull-left header"><i class="fa fa-random"></i>往来结算</li>
							</ul>
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form id="listForm" action="return_settle_account.jhtml" method="get">
									<input type="hidden" id="status_val" name="status" value=""/>
									<div class="row mtb10">
										<div class="col-sm-5">
											<div class="btn-group">
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
												<div class="dropdown fl ml5">
													<button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
														[#if status=="true"]已结算[#elseif status=="false"]未结算[#else]结算状态[/#if]
														<span class="caret"></span>
													</button>
													<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
														<li role="presentation">
															<a role="menuitem" tabindex="-1"id="yes" onclick="get_status(this)">已结算</a>
														</li>
														<li role="presentation">
															<a role="menuitem" tabindex="-1" id="no" onclick="get_status(this)">未结算</a>
														</li>
													</ul>
												</div>
											</div>
										</div>
										<div class="col-sm-4">
											<div class="fl">
												<input type="text" id="startDate" name="startDate" value="${begin_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间"/>
											</div>
											<div class="fl">
												<i class="fa fa-exchange mid_po_icon"></i>
											</div>
											<div class="fl">
												<input type="text" id="endDate" name="endDate" value="${end_date}" class="date_input" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="结束时间"/>
											</div>
                      <div class="fl ml5">
                        <input type="submit" value="搜索" class="btn btn-block btn-default btn-sm">
                      </div>
										</div>
										<div class="col-sm-3">
											<div class="box-tools fr">
												<div class="input-group input-group-sm" style="width: 150px;">
													<input type="text" class="form-control pull-right" id="keyword" name="keywords" value="${keywords}" placeholder="搜索订/退货号">
													<div class="input-group-btn">
														<button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
													</div>
												</div>
											</div>
										</div>
									</div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                     <table id="listTable" class="table table-bordered table-striped">
                      <thead>
                       <tr>
                        <th>创建时间</th>
                        <th>退货单号</th>
                        <th>订单单号</th>
                        <th>支付方式</th>
                        <th>退货状态</th>
                        <th>发货状态</th>
                        <th>结算状态</th>
                        <th>退货金额</th>
                        <th>结算金额</th>
                        <th>结算时间</th>
                      </tr>
                    </thead>
                    <tbody>
                     [#if page??&&page?has_content]
                     [#list page.content as spReturns]
                     <tr>
                      <td>${spReturns.createDate}</td>
                      <td>${spReturns.sn}</td>
                      <td>[#if spReturns.trade.order?has_content]${spReturns.trade.order.sn}[/#if]</td>
                      <td>${spReturns.trade.order.paymentMethodName}</td>
                      <td>    
                        [#if spReturns.returnStatus=='unconfirmed']
                        <span>待受理</span>
                        [#elseif spReturns.returnStatus=='confirmed']
                        <span>已受理</span>
                        [#elseif spReturns.returnStatus=='audited']
                        <span>已认证</span>
                        [#elseif spReturns.returnStatus=='completed']
                        <span>已完成</span>
                        [#elseif spReturns.returnStatus=='cancelled']
                        <span>已取消</span>
                        [/#if]
                      </td>
                      <td>${message("Order.ShippingStatus." + spReturns.trade.shippingStatus)}</td>
                      <td>[#if spReturns.suppliered??&&spReturns.suppliered=="true"]已结算[#else]未结算[/#if]</td>
                      <td>${spReturns.amount}</td>
                      <td>${spReturns.cost}</td>
                      <td>${spReturns.supplierDate}</td>
                    </tr>
                    [/#list]
                    [/#if]
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
</div>
[#include "/store/member/include/footer.ftl"]
</div>
<div id="trade_wrap"></div>
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
  $().ready(function () {
   var $selectAll = $("#selectAll");
   var $ids = $("#listTable input[name='ids']");
   if($("#settle_status").text()=="已结算"){
    $("#status_val").val("true");
  }else if($("#settle_status").text()=="未结算"){
    $("#status_val").val("false");
  }else{
    $("#status_val").val("");
  }
  //=======================导出==============================
  $("#export_ss").click(function () {
  	$.message("success", "正在帮您导出，请稍后")
  	$.ajax({
  		url: "${base}/helper/member/trade/return_settle_account_export.jhtml",
  		type: "post",
  		data: {
  			startDate: $("#startDate").val(),
  			endDate: $("#endDate").val(),
  			status:$("#status_val").val(),
  			keywords: $("#keyword").val()
  		},
  		async: false,
  		dataType: "json",
  		success: function (data) {
  			var html = '<table style="display:none;" class="table2excel">' +
  			'<thead>' +
  			'<tr>' +
  			'<th>创建日期</th>' +
  			'<th>订单单号</th>' +
  			'<th>退货单号</th>' +
  			'<th>支付方式</th>' +
  			'<th>订单状态</th>' +
  			'<th>发货状态</th>' +
  			'<th>退货状态</th>' +
  			'<th>退货金额</th>' +
  			'<th>结算金额</th>' +
  			'<th>结算状态</th>' +
  			'<th>结算时间</th>' +
  			'</tr>' +
  			'</thead>' +
  			'<tbody>;'
  			$.each(data, function (i, obj) {
  				html += '<tr>' +
  				'<td>' + obj.time + '</td>' +
  				'<td>' + obj.oSn + '</td>' +
  				'<td>' + obj.rSn + '</td>' +
  				'<td>' + obj.paymentMethod + '</td>' +
  				'<td>' + obj.orderStatus + '</td>' +
  				'<td>' + obj.shippingStatus + '</td>' +
  				'<td>' + obj.returnStatus + '</td>' +
  				'<td>' + obj.amount + '</td>' +
  				'<td>' + obj.cost + '</td>' +
  				'<td>' + obj.suppliered + '</td>' +
  				'<td>' + obj.supplierDate + '</td>' +
  				'</tr>';
  			});
  			html += '</tbody>' +
  			'</table>';
  			$("#trade_wrap").html(html);
  		}
  	});
    $(".table2excel").table2excel({
    	exclude: ".noExl",
    	name: "退货结算",
    	filename: "退货结算",
    	fileext: ".xls",
    	exclude_img: true,
    	exclude_links: false,
    	exclude_inputs: true
    });
  });
});
function get_date_val(){
	$("#listForm").submit();   
} 
function get_status(obj){
	$("#seller_pop").hide();
	if($(obj).attr("id")=="yes"){
		$("#settle_status").text("已结算");
		$("#status_val").val("true");
	}else if($(obj).attr("id")=="no"){
		$("#settle_status").text("未结算");
		$("#status_val").val("false");
	}
	get_date_val();
}
</script>
</body>
</html>
