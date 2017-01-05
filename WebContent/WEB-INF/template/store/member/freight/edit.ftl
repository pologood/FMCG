<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>运费设置</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
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
	  <h1>
	    我的店铺
	    <small>设置你的运费</small>
	  </h1>
	  <ol class="breadcrumb">
	    <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        <li><a href="${base}/store/member/tenant/edit.jhtml">店铺管理</a></li>
        <li class="active">运费设置</li>
	  </ol>
	</section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
     	<div class="col-md-12">
	 		<div class="nav-tabs-custom">
	 			<ul class="nav nav-tabs pull-right">                                    
	              <li class="pull-left header"><i class="fa fa-truck"></i>运费设置</li>
	            </ul>        
	 			 <div class="tab-content" >
	 			 	<form class="form-horizontal" role="form" id="inputForm" action="update.jhtml" method="post">
					  <div class="form-group">
					    <label for="inputEmail3" class="col-sm-2 control-label">选择配送类型</label>
					    <div class="col-sm-4">
					      <select class="form-control" id="freightType" name="freightType">
							<option value="null">请选择</option>
							<option value="piece">计件</option>
							<option value="weight">计重</option>
						   </select>
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="inputPassword3" class="col-sm-2 control-label" id="firstWeight">*首重(kg)</label>
					    <div class="col-sm-8">
					      <input type="text" class="form-control" name="firstWeight" value="${tenant.freight.firstWeight?number}" >
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="inputPassword3" class="col-sm-2 control-label" id="firstPrice">*首重价格(元)</label>
					    <div class="col-sm-8">
					      <input type="text" class="form-control" name="firstPrice" value="${tenant.freight.firstPrice}">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="inputPassword3" class="col-sm-2 control-label" id="continueWeight">*续重(kg)</label>
					    <div class="col-sm-8">
					      <input type="text" class="form-control" name="continueWeight" value="${tenant.freight.continueWeight}" >
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="inputPassword3" class="col-sm-2 control-label" id="continuePrice">*续重价格(元)</label>
					    <div class="col-sm-8">
					      <input type="text" class="form-control" name="continuePrice" value="${tenant.freight.continuePrice}">
					    </div>
					  </div>
					  <div class="form-group">
	                <div class="col-sm-offset-2 col-sm-2">
	                  <button type="submit" class="btn btn-block btn-primary">保存</button>
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
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript">
	$().ready(function () {
	    $("#inputForm").validate({
	        rules: {
	            firstWeight:{
	                required: true,
	                min: 0,
	                digits:true,
	                decimal: {
	                    integer: 12,
	                    fraction: ${setting.priceScale}
	                }
	            },
	            firstPrice:{
	                required: true,
	                min: 0,
	                digits:true,
	                decimal: {
	                    integer: 12,
	                    fraction: ${setting.priceScale}
	                }
	            },
	            continueWeight:{
	                required: true,
	                min: 0,
	                digits:true,
	                decimal: {
	                    integer: 12,
	                    fraction: ${setting.priceScale}
	                }
	            },
	            continuePrice:{
	                required: true,
	                min: 0,
	                digits:true,
	                decimal: {
	                    integer: 12,
	                    fraction: ${setting.priceScale}
	                }
	            }
	        },
	        messages: {
                firstWeight:{
                	required:"必填",
	                digits:"必须为整数"
                },
                firstPrice:{
                	required:"必填",
	                digits:"必须为整数"
                },
                continueWeight: {
                	required:"必填",
	                digits:"必须为整数"
                },
                continuePrice:{
                	required:"必填",
	                digits:"必须为整数"
                },
            }
	    });
		
		//计重或者计件的选择处理
	    if ("${tenant.freight.freightType}" == "piece") {
	        $('form option[value="piece"]').attr('selected', 'selected');
	        $("#firstWeight").text("*首件(件)");
	        $("#firstPrice").text("*首件价格(元)");
	        $("#continueWeight").text("*续件(件)");
	        $("#continuePrice").text("*续件价格(元)");
	    }
	    if ("${tenant.freight.freightType}" == "weight") {
	        $('form option[value="weight"]').attr('selected', 'selected');
	        $("#firstWeight").text("*首重(kg)");
	        $("#firstPrice").text("*首重价格(元)");
	        $("#continueWeight").text("*续重(kg)");
	        $("#continuePrice").text("*续重价格(元)");
	    }
	    $("#freightType").change(function () {
	        if ($("form option[value='piece']").prop("selected") == true) {
	            $("#firstWeight").text("*首件(件)");
	            $("#firstPrice").text("*首件价格(元)");
	            $("#continueWeight").text("*续件(件)");
	            $("#continuePrice").text("*续件价格(元)");
	        }
	        if ($("form option[value='weight']").prop("selected") == true) {
	            $("#firstWeight").text("*首重(kg)");
	            $("#firstPrice").text("*首重价格(元)");
	            $("#continueWeight").text("*续重(kg)");
	            $("#continuePrice").text("*续重价格(元)");
	        }
	    });

	});
</script>
</body>
</html>
