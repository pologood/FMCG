<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>添加员工</title>
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
				<h1>我的店铺<small>添加你的员工</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
			        <li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
			        <li><a href="${base}/store/member/tenant/employee/list.jhtml">员工管理</a></li>
			        <li class="active">添加员工</li>
				</ol>
			</section>

			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li class="pull-left header"><i class="fa fa-user-plus"></i>添加员工</li>
							</ul>
							<div class="tab-content">
								<form class="form-horizontal" role="form" id="registerForm" action="${base}/store/member/tenant/employee/add.jhtml" method="post">
									<div class="form-group">
										<label class="col-sm-2 control-label">请选择</label>
										<div class="col-sm-8">
											<div class="radio">
												<label>
													<input type="radio" name="employeeType" value="0"> 工号
												</label>
												<label>
													<input type="radio" name="employeeType" value="1" checked> 手机号
												</label>
											</div>                
										</div>
									</div>
									<!--手机号注册-->
									<div id="mobileNumber">
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>手机号</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="mobile" name="mobile">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>验证码</label>
											<div class="col-sm-3">
												<input type="text" class="form-control" id="captcha" name="captcha">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-default" id="sendCode">获取验证码</button>
											</div>
										</div>
									</div>
									<!--手机号注册-->
									<!--工号注册-->
									<div id="employeeNumber" style="display: none;">
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>工号</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" id="usernumber" name="usernumber">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>密码</label>
											<div class="col-sm-8">
												<input type="password" name="newPassword" class="form-control">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label"><span class="red">*</span>确认密码</label>
											<div class="col-sm-8">
												<input type="password" name="enNewPassword" class="form-control">
											</div>
										</div>
									</div>
									<!--工号注册-->
									<div class="form-group">
										<label class="col-sm-2 control-label">所属门店</label>
										<div class="col-sm-4">
											<select class="form-control" name="deliveryCenterId">
												[#list deliveryCenterList as delivery]
												<option value="${delivery.id}">${delivery.name}</option>
												[/#list]
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>角色</label>
										<div class="col-sm-8">
											<div class="radio">
												[#list roles as rl]
												<label>
													<input type="radio" name="roles" value="${rl.id}"/>${rl.name}
												</label>
												[#if rl_index=5]</br>[/#if]
												[/#list]
											</div>
										</div>
									</div>
									<div class="form-group" id="roleRulesTr" style="display: none">
										<label class="col-sm-2 control-label">权限</label>
										<div class="col-sm-10" >
											<div id="roleRulesTd" style="padding-top:7px;"></div>
										</div>											
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>姓名</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="username">
										</div>											
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">性别</label>
										<div class="col-sm-8">
											<div class="radio">
												<label>
													<input type="radio" name="gender" value="male">
													男
												</label>
												<label>
													<input type="radio" name="gender" value="female">
													女
												</label>
											</div>
										</div>											
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">详细地址</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="address">
										</div>											
									</div>
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
	<script type="text/javascript" src="${base}/resources/store/js/jsbn.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/prng4.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/rng.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/rsa.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/base64.js"></script>
	<script type="text/javascript">
      //============初始化============
      $().ready(function () {
      	var $registerForm = $("#registerForm");
      	var $mobile = $("#mobile");
      	var $sendCode = $("#sendCode");
      	var $selectedvalue;
        //角色改变
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
        $("input[name='roles']").change(function () {
        	$("#roleRulesTr").hide();
        	var $role_obj=$("input[name='roles']:checked");
        	$.ajax({
        		url: "${base}/store/member/role/getRules.jhtml",
        		type: "post",
        		data: {id: $role_obj.val()},
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
        });
		$sendCode.click(function () {
			if ($mobile.val() == "" || $mobile.val() == null) {
				$.message("warn", "请输入手机号码");
				return false;
			}

			if ($mobile.val().length != 11) {
				$.message("warn", "手机号码不合法");
				return false;
			}

			$.ajax({
				url: "${base}/store/member/tenant/isOwner.jhtml",
				type: "post",
				data: {username: $mobile.val()},
				dataType: "json",
				success: function (message) {
					if (message.type == "error") {
						$.message(message);
						return false;
					}
					settime($sendCode);
					$.ajax({
						url: "${base}/store/member/tenant/employee/send_mobile.jhtml",
						type: "post",
						data: {username: $mobile.val()},
						dataType: "json",
						success: function (message) {
							$.message(message);
						}
					});
				}
			});
		});

		$("input[name='employeeType']").change(function () {
			$selectedvalue = $("input[name='employeeType']:checked").val();
			if ($selectedvalue == 1) {
				$("#mobileNumber").show();
				$("#employeeNumber").hide();
			} else {
				$("#employeeNumber").show();
				$("#mobileNumber").hide();
			}
		});

        // 表单验证
        $registerForm.validate({
        	submitHandler: function (form) {
        		if($selectedvalue==0){
                    //工号
                    $("#mobile").val(null);
                    var _usernumber = $("#usernumber").val();
                    var reg =/^[0-9]+[0-9]$/;
                    if (!reg.test(_usernumber)) {
                      //非数字
                      $.message("warn", "输入工号不合法");
                      return false;
                    }
                    //密码
                    if($("input[name='newPassword']").val()!=$("input[name='enNewPassword']").val()){
                      //密码不正确
                      $.message("warn", "两次输入密码不一致,请重新输入!");
                      return false;
                    }
                }else {
                  //手机号
                  $("#usernumber").val(null);
                }
               
                //检查行业标签
                if($("input[name='roles']:checked").size()<=0){
					$.message("error", "请选择角色");
					return;
                }
                $.ajax({
              	  url: $registerForm.attr("action"),
              	  type: "post",
              	  data: $registerForm.serialize(),
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
      //============定时器============
      var countdown = 60;
      function settime(obj) {
      	if (countdown == 0) {
      		obj.removeAttr("disabled");
      		obj.text("获取验证码");
      		countdown = 60;
      		return;
      	} else {
      		obj.attr("disabled", true);
      		obj.text("" + countdown + "s重新发送");
      		countdown--;
      	}
      	setTimeout(function () {
      		settime(obj)
      	}, 1000);
      }
  </script>
</body>
</html>