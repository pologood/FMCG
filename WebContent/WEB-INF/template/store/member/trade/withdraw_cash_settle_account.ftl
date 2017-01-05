<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>提现结算</title>
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
          <li><a href="${base}/store/member/trade/withdraw_cash_settle_account.jhtml">往来结算</a></li>
          <li class="active">体现结算</li>
				</ol>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="active"v>
									<a href="${base}/store/member/trade/withdraw_cash_settle_account.jhtml">提现结算</a>
								</li>
								<li>
									<a href="${base}/store/member/trade/return_settle_account.jhtml">退货结算</a>
								</li>
								<li>
									<a href="${base}/store/member/trade/order_settle_account.jhtml">订单结算</a>
								</li>
								<li class="pull-left header"><i class="fa fa-random"></i>往来结算</li>
							</ul>
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form id="listForm" action="withdraw_cash_settle_account.jhtml" method="get">
									<input type="hidden" id="status_val" name="status" value="${status}"/>
									<div class="row mtb10">
										<div class="col-sm-5">
											<div class="btn-group">
												<button type="button" class="btn btn-default btn-sm disabled" data-toggle="modal" data-target="#batch_settle" onclick="clear_return_account(this)" id="batch_button"><i class="fa fa-external-link mr5"></i>批量缴款</button>
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
										<div class="col-sm-7">
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
									</div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                     <table id="listTable" class="table table-bordered table-striped">
                      <thead>
                       <tr>
                        <th><input type="checkbox" id="selectAll" ></th>
                        <th>创建时间</th>
                        <th>结算单号</th>
                        <th>结算状态</th>
                        <th>结算金额</th>
                        <th>供应商</th>
                        <th>操作</th>
                      </tr>
                    </thead>
                    <tbody>
                     [#if page??&&page?has_content]
                     [#list page.content as account]
                     <tr>
                      <td><input type="checkbox" name="ids" value="${account.id}" ></td>
                      <td>${account.createDate}</td>
                      <td>${(account.sn)!"--"}</td>
                      <td>
                       [#if account.status=='success']
                       <span>已结算</span>
                       [#elseif account.status=='none']
                       <span>未结算</span>
                       [/#if]
                     </td>
                     <td amount="amount" oamo="${(account.amount)!}">${account.amount}</td>
                     <td>
                       [#if account.supplier??&&account.supplier?has_content]
                       ${account.supplier.name}(${account.supplier.username})
                       [/#if]
                     </td>
                     <td>
                       <span ele="a">

                         <a href="javascript:;" onclick="clear_return_account(this,${account.id},${account.amount})">付款</a>

                       </span>
                     </td>
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
<!-- 缴款弹框【 -->
<div class="modal fade" id="batch_settle" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
  <div class="modal-dialog">
   <form class="form-horizontal" method="post" role="form">
    <input type="hidden" name="id" value="">
    <div class="modal-content" style=" border-radius: 5px;">
     <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <h4 class="modal-title">批量付款</h4>
    </div>
    <div class="modal-body">
      <div class="form-group">
       <label for="inputName" class="col-sm-3 control-label">已选中数量</label>
       <div class="col-sm-8">
        <input type="text" class="form-control" id="selected_num" disabled="true">
      </div>
    </div>
    <div class="form-group">
     <label for="inputName" class="col-sm-3 control-label">选中结算总额</label>
     <div class="col-sm-8">
      <input type="text" class="form-control" id="selected_cost" disabled="true">
    </div>
  </div>
</div>
<div class="modal-footer"> 
  <div class="col-sm-offset-8 col-sm-2">
   <button type="button" class="btn btn-block btn-primary" id="confirm_settle">确定</button>
 </div>
 <div class="col-sm-offset-0 col-sm-2">
   <input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
 </div>
</div>
</div>
</form>
</div>
</div>
<!--缴款弹框】 -->
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
  var flobal_ids='';
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
			//控制批量发货按钮
      $("#selectAll").click(function(){
      	if($(this).prop("checked")==true){
      		$("#batch_button").attr("data-target","#batch_settle");
      		$("#batch_button").removeClass("disabled");
      	}else{
      		$("#batch_button").attr("data-target","");
      		$("#batch_button").addClass("disabled");
      	}
      });
			//确认缴款
      $("#confirm_settle").click(function(){
      	if(global_ids==''){
      		return;
      	}
      	$.ajax({
          url:"${base}/helper/member/trade/clear_withdraw_cash.jhtml",
          type:"post",
          traditional: true,
          data:{ids:global_ids},
          dataType:"json",
          success:function(message){
            if(message.type=="success"){
              location.reload();
            }else{
              $.message(message);
            }
          }
        });
      });
      //====================导出==========================
      $("#export_ss").click(function(){
      	$.message("success","正在帮您导出，请稍后")
      	$.ajax({
      		url:"${base}/helper/member/trade/withdraw_cash_settle_account_export.jhtml",
      		type:"get",
      		data:{
      			startDate:$("#startDate").val(),
      			endDate:$("#endDate").val(),
      			status:$("#status_val").val()
      		},
      		async:false,
      		dataType:"json",
      		success:function(data){
      			var html='<table style="display:none;" class="table2excel">'+
      			'<thead>'+
            '<tr>'+
            '<th>创建时间</th>'+
            '<th>结算单号</th>'+
            '<th>结算金额</th>'+
            '<th>结算状态</th>'+
            '<th>供应商</th>'+
            '</tr>'+
            '</thead>'+
            '<tbody>;'
            $.each(data,function(i,obj){
              html+=
              '<tr>'+
              '<td>'+obj.time+'</td>'+
              '<td>'+obj.sn+'</td>'+
              '<td>'+obj.amount+'</td>'+
              '<td>'+obj.status+'</td>'+
              '<td>'+obj.supplier+'</td>'+
              '</tr>';
            });
            html+='</tbody>'+
            '</table>';
            $("#trade_wrap").html(html);
          }
        });
        //导出数据到excel
        $(".table2excel").table2excel({
        	exclude: ".noExl",
        	name: "提现结算",
        	filename: "提现结算",
        	fileext: ".xls",
        	exclude_img: true,
        	exclude_links: false,
        	exclude_inputs: true 
        });
      });
});
		//结算状态
		function get_status(obj){
			$("#seller_pop").hide();
			if($(obj).attr("id")=="yes"){
				$("#settle_status").text("已结算");
				$("#status_val").val("true");
			}else if($(obj).attr("id")=="no"){
				$("#settle_status").text("未结算");
				$("#status_val").val("false");
			}
			$("#listForm").submit();
		}
		//统计缴款的信息处理
		function clear_return_account(obj,id,amount){
			var amount_total=0;
			var select_num=0;
			$.each($("#listTable input[name='ids']"),function(i,item){
				if($(this).prop("checked")==true){
					$(this).parents("tr").find("[amount='amount']").each(function(){
						amount_total= parseFloat(amount_total)+parseFloat($(this).attr("oamo"));
						select_num+=1;
					});
				}
			});
			var ids=[];
			if($(obj).parent().attr("ele")=="a"){
				$("#batch_button").removeClass("disabled");
				$(obj).parent().attr("data-target","#batch_settle");
				$(obj).parent().attr("data-toggle","modal");
				$("#selected_num").val(1);
				$("#selected_cost").val(amount.toFixed(2));
				ids.push(id);
			}else{
				if(select_num>0){
					$("#batch_button").removeClass("disabled");
					$("#batch_button").attr("data-target","#batch_settle");
					$("#selected_num").val(select_num);
					$("#selected_cost").val(amount_total.toFixed(2));
					$.each($("#listTable input[name='ids']"),function(i,item){
           if($(this).prop("checked")==true){
             ids.push($(this).val());
           }
         });
				}else{
					$("#batch_button").attr("data-target","");
					$("#batch_button").addClass("disabled");
				}
			}
			global_ids=ids;
		}     
	</script>
</body>
</html>