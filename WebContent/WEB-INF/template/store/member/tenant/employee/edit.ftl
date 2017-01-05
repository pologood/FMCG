<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>员工管理</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> 
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" href="${base}/resources/store/css/style.css">
	<link rel="stylesheet" href="${base}/resources/store/css/common.css"/>
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
				<h1>我的店铺<small>管理你的员工</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
					<li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
					<li><a href="${base}/store/member/tenant/employee/list.jhtml">员工管理</a></li>
					<li class="active">编辑</li>
				</ol>
			</section>

			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">									
								<li class="pull-left header"><i class="fa fa-users"></i>员工管理</li>
							</ul>
							<div class="tab-content">
								<div class="row">
									<div class="col-sm-9 border-r">
										<form class="form-horizontal" role="form" id="inputForm" action="${base}/store/member/tenant/employee/update.jhtml" method="post">
											<input type="hidden" name="id" value="${employee.id}"/>
											<div class="form-group">
												<label class="col-sm-2 control-label">用户名</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" value="${employee.member.username}" readonly="true">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label"><span class="red">*</span>姓名</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="username" value="${employee.member.name}">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">性别</label>
												<div class="col-sm-9 pt5 label-m15">
													<div class="radio">
														<label>
															<input type="radio" name="gender" value="male" [#if employee.member.gender=='male']checked[/#if]>
															男
														</label>
													</div>
													<div class="radio">
														<label>
															<input type="radio" name="gender" value="female" [#if employee.member.gender=='female']checked[/#if]>
															女
														</label>
													</div>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">详细地址</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="address" value="${employee.member.address}">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">所属门店</label>
												<div class="col-sm-5">
													<select class="form-control" name="deliveryCenterId">
														[#list deliveryCenterList as delivery]
														<option [#if delivery.id==(employee.deliveryCenter.id)!]selected[/#if]
														value="${delivery.id}">${delivery.name}</option>
														[/#list]
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">角色</label>
												<div class="col-sm-9">
													<div class="radio">
														[#list roles as rl]
														<label>
															<input type="radio" name="roles" value="${rl.id}"
															[#if employee.role?exists]
															[#list employee.role?split(",") as fileName]
															[#if fileName!=""]
															[#if fileName?contains(rl.id.toString())]checked[/#if]
															[/#if]
															[/#list]
															[/#if]
															/> ${rl.name}
														</label>
														[#if rl_index=5]</br>[/#if]
														[/#list]
													</div>
												</div>
											</div>
											<div class="form-group" id="roleRulesTr" style="display: none">
												<label class="col-sm-2 control-label">权限</label>
												<div class="col-sm-10">
													<div id="roleRulesTd" style="padding-top:7px;"></div>
												</div>											
											</div>
											[#if versionType==0]
											<div class="form-group">
												<label class="col-sm-2 control-label">导购标签</label>
												<div class="col-sm-10" style="line-height:37px;">
													[#list tags as tag]
													<input value="${tag.id}" type="checkbox" name="tagIds" [#if employee.tags?seq_contains(tag)==true]checked[/#if]/>${tag.name}
													[/#list]
												</div>											
											</div>
											[/#if]
											<div class="form-group">
												<div class="col-sm-offset-2 col-sm-2">
													<button type="submit" class="btn btn-block btn-primary">提交</button>
												</div>
												<div class="col-sm-offset-0 col-sm-2">
													<button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
												</div>
											</div>
										</form>
									</div>
									[#if versionType==0]
									<div class="col-sm-3">
										<div class="box-header with-border">
											<i class="fa fa-qrcode" ></i>
											<h3 class="box-title">二维码</h3>
										</div>
										<div class="box-body with-border">
											<img src="${base}/store/member/tenant/qrcode/employee.jhtml?mobile=${employee.member.username}"/>
										</div>
										<div class="box-footer with-border">
											<div class="red">单击右键选择【图片另存为】可保存二维码到本地</div>
										</div>
									</div>
									[/#if]
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
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
	<script type="text/javascript">
		$().ready(function () {
			var $inputForm = $("#inputForm");
			var mpOpType =
			{
				[#list types as type]
					[#if type_has_next]
						${type}:'${message("helper.role."+type)}',
					[#else ]
						${type}:'${message("helper.role."+type)}'
					[/#if]
				[/#list]
			};
        	//角色改变
        	$("input[name='roles']").change(function () {
        		$("#roleRulesTr").hide();
        		var $role_obj=$("input[name='roles']:checked");
        		get_role_content($role_obj.val());
        	});
        	[#list roles as rl]
	        	[#if employee.role?exists]
		        	[#list employee.role?split(",") as fileName]
			        	[#if fileName!=""]
				        	[#if fileName?contains(rl.id.toString())]
				        		get_role_content(${rl.id});
				        	[/#if]
			        	[/#if]
		        	[/#list]
	        	[/#if]
        	[/#list]
        	function get_role_content(obj){
        		$.ajax({
        			url: "${base}/store/member/role/getRules.jhtml",
        			type: "post",
        			data: {id: obj},
        			dataType: "json",
        			success: function (message) {
        				if (message.data != null && message.message.type == "success") {
        					$("#roleRulesTd").empty();
        					var strHtml = "";
        					strHtml += " <div>";
        					for (var i = 0; i < message.data.length; i++) {
        						if (message.data[i].rules.children.length == 0) {
        							strHtml += " <div>";
        							strHtml += "<strong>" + message.data[i].rules.name + "：</strong>  ";
        							$.each(message.data[i].mapAuthority, function (key, values) {
        								if (values == true) {
        									strHtml += mpOpType[key] + "、 ";
        								}
        							});
        							strHtml += " </div>";
        						}
        					}
        					$("#roleRulesTd").append(strHtml);
        					$("#roleRulesTr").show();
        				}
        			}
        		});
			}
			//表单验证
			$inputForm.validate({
				submitHandler: function (form) {
					var isPass="请选择角色";
		            //检查行业标签
	                $("input[name='roles']").each(function(){
	                	if($(this).attr('checked')){
	                		isPass="";
	                		return;
	                	}

	                });
	                if(isPass!=""){
	                	$.message("error",isPass);
	                	return false;
	                }
	                $.ajax({
	                	url: $inputForm.attr("action"),
	                	type: "post",
	                	data: $inputForm.serialize(),
	                	dataType: "json",
	                	success: function (data) {
	                		$.message(data.message);
	                		if (data.message.type == "success") {
	                			setTimeout(function () {
	                				location.href = "${base}/store/member/tenant/employee/list.jhtml";
	                			}, 2000);
	                		}
	                	}
	                });
	            }
	        });
		});
	</script>
</body>
</html>