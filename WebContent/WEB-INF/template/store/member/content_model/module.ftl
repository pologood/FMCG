<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css"><!---自定义样式链接--->

${base}/store/member/article/list.jhtml

${base}/store/member/tenant/qrcode/employee.jhtml?mobile=100202


<!--总框架--->
<div class="row">
	<div class="col-md-12">
		<div class="nav-tabs-custom">
			<ul class="nav nav-tabs pull-right"> 
				<li><a href="${base}/store/member/article/list.jhtml">员工管理</a></li>
				<li  class="active"><a href="${base}/store/member/article/list.jhtml">角色管理</a></li>
				<li class="pull-left header"><i class="fa fa-truck"></i>运费设置</li>
			</ul>
			
			<div class="tab-content" >
				
			</div>
			
		</div>
	</div>
</div>


<!--页面标题--->
	<section class="content-header">
		<h1>我的店铺<small>维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等</small>	</h1>	    		  
		<ol class="breadcrumb">
			<li><a href="#"><i class="fa fa-dashboard"></i>我的店铺</a></li>						
			<li class="active">门店管理</li>
		</ol>
	</section>


<!--导航功能--->
	<div class="btn-group">
	<button type="button" class="btn btn-primary btn-sm"><i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加</button>
	</div>
	
	<div class="row mtb10">                        
    	<div class="col-sm-7">
			<div class="btn-group">
			  <button type="button" class="btn btn-default btn-sm"><i class="fa fa-minus-square mr5" aria-hidden="true"></i>下架</button>
			  <button type="button" class="btn btn-default btn-sm"><i class="fa fa-close mr5" aria-hidden="true"></i>删除</button>
			  <button type="button" class="btn btn-default btn-sm"><i class="fa fa-refresh mr5" aria-hidden="true"></i> 刷新</button>
			  <button type="button" class="btn btn-default btn-sm">商品筛选</button>
			  <button type="button" class="btn btn-default btn-sm">更多选项</button>								  								  
			  <button type="button" class="btn btn-default btn-sm">批量改价</button>
			  <button type="button" class="btn btn-default btn-sm">批量上架</button>
			  <button type="button" class="btn btn-default btn-sm">导出</button>
			  <div class="dropdown fl ml5">
				  <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
				    	每页显示
				    <span class="caret"></span>
				  </button>
				  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
				    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">10</a></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">20</a></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">30</a></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">40</a></li>
				  </ul>
				</div>
			</div>
		</div>		
		<div class="col-sm-5">
			<div class="box-tools fr" >
          <div class="input-group input-group-sm" style="width: 150px;">
            <input type="text" name="table_search" class="form-control pull-right" placeholder="Search">
            <div class="input-group-btn">
              <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
            </div>
          </div>
        </div>
	 </div>		
   </div>	
   
   
   <div class="col-sm-3">
		<div class="form-group" style="margin-bottom: 0px;">
			<div class="input-group">
				<div class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</div>
				<input type="text" class="form-control pull-right" id="reservation">
			</div>
			<!-- /.input group -->
		</div>
	</div>
	
	<script>
			///Date range picker
			$('#reservation').daterangepicker();
			//Date range picker with time picker
			$('#reservationtime').daterangepicker({
				timePicker: true,
				timePickerIncrement: 30,
				format: 'MM/DD/YYYY h:mm A'
			});
			//Date range as a button
			$('#daterange-btn').daterangepicker({
					ranges: {
						'Today': [moment(), moment()],
						'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
						'Last 7 Days': [moment().subtract(6, 'days'), moment()],
						'Last 30 Days': [moment().subtract(29, 'days'), moment()],
						'This Month': [moment().startOf('month'), moment().endOf('month')],
						'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
					},
					startDate: moment().subtract(29, 'days'),
					endDate: moment()
				},
				function(start, end) {
					$('#daterange-btn span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
				}
			);

			//Date picker
			$('#datepicker').datepicker({
				autoclose: true
			});
		</script>
	
	
<!--表格--->
odder
	<table class="table table-bordered table-striped"> 
	  <thead>
		<tr>
		 <th></th>
		</tr>
	  </thead>
	  <tbody>
		<tr>
			<td></td>
		</tr>
	  </tbody>
	</table>
	
	
	
	
<!--弹框--->	
<!-- Button trigger modal -->
<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
  Launch demo modal
</button>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">Modal title</h4>
      </div>
      <div class="modal-body">
        ...
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>	

	
	
<!--表单--->	

<form class="form-horizontal" role="form">
<div class="form-group">
<label  class="col-sm-2 control-label">*编码:</label>
<div class="col-sm-8">
	<select class="form-control">
		  <option>1</option>
		  <option>2</option> 
		</select>
</div>
</div>
<div class="form-group">
<label  class="col-sm-2 control-label">*名称:</label>
<div class="col-sm-8">
	<input type="text" class="form-control"  value="">
</div>
</div>
<div class="form-group">
<label  class="col-sm-2 control-label">*联系人:</label>
<div class="col-sm-8">
	<input type="text" class="form-control"  value="">
</div>
</div>

<div class="form-group">
	<label class="col-sm-2 control-label">性别:</label>
	<div class="col-sm-9 pt5 label-m15">
		<div class="radio">
			<label>
			<input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="">
					男
		  </label>
		</div>
		<div class="radio">
			<label>
			<input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="">
					女
		  </label>
		</div>
	</div>
</div>

<div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-default">Sign in</button>
        </div>
      </div>
</form>	
	

<!--标签切换--->	
<div class='tab'>	
<!-- Nav tabs -->
	<ul class="nav nav-tabs" role="tablist">
	  <li role="presentation" class="active"><a href="#home" role="tab" data-toggle="tab">Home</a></li>
	  <li role="presentation"><a href="#profile" role="tab" data-toggle="tab">Profile</a></li>
	  <li role="presentation"><a href="#messages" role="tab" data-toggle="tab">Messages</a></li>
	  <li role="presentation"><a href="#settings" role="tab" data-toggle="tab">Settings</a></li>
	</ul>

<!-- Tab panes -->
<div class="tab-content">
  <div role="tabpanel" class="tab-pane active" id="home">...</div>
  <div role="tabpanel" class="tab-pane" id="profile">...</div>
  <div role="tabpanel" class="tab-pane" id="messages">...</div>
  <div role="tabpanel" class="tab-pane" id="settings">...</div>
</div>	
	
</div>	
	
	
<!-- box 盒子 -->
<div class="box box-default">
	<!-- /.box-header -->
	<div class="box-header with-border">
		<i class="fa fa-bullhorn"></i>
		<h3 class="box-title">Callouts</h3>
	</div>
	<!-- /.box-body -->
	<div class="box-body">
		dss
	</div>											
</div>
	
<!-- /.分页导航 -->
<div class="row">
	<div class="col-sm-5">
		<div class="dataTables_info" id="example2_info" role="status" aria-live="polite">Showing 1 to 10 of 57 entries</div>
	</div>
	<div class="col-sm-7">
		<div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
			<ul class="pagination">
				<li class="paginate_button previous disabled" id="example2_previous">
					<a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a>
				</li>
				<li class="paginate_button active">
					<a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">1</a>
				</li>
				<li class="paginate_button ">
					<a href="#" aria-controls="example2" data-dt-idx="2" tabindex="0">2</a>
				</li>
				<li class="paginate_button ">
					<a href="#" aria-controls="example2" data-dt-idx="3" tabindex="0">3</a>
				</li>
				<li class="paginate_button ">
					<a href="#" aria-controls="example2" data-dt-idx="4" tabindex="0">4</a>
				</li>
				<li class="paginate_button ">
					<a href="#" aria-controls="example2" data-dt-idx="5" tabindex="0">5</a>
				</li>
				<li class="paginate_button ">
					<a href="#" aria-controls="example2" data-dt-idx="6" tabindex="0">6</a>
				</li>
				<li class="paginate_button next" id="example2_next">
					<a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a>
				</li>
			</ul>
		</div>
	</div>
</div>

<!-- /.box标题 -->
	<div class="box-header with-border">
		  <i class="fa fa-shopping-bag"></i>	
		  <h3 class="box-title">商品分类</h3>
	</div>