<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>发货地址</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> 
	[#include "/store/member/include/bootstrap_css.ftl"]
	<link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
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
				<h1>我的店铺<small>维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等</small>	</h1>
				<ol class="breadcrumb">
					<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        			<li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
        			<li><a href="${base}/store/member/delivery_center/list.jhtml">门店管理</a></li>
        			<li class="active">编辑</li>
				</ol>
			</section>

			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs pull-right">
								<li>
									<a href="${base}/store/member/delivery_center/list.jhtml">地址列表</a>
								</li>
								<li class="active">
									<a href="${base}/store/member/delivery_center/edit.jhtml">修改地址</a>
								</li>
								<li class="pull-left header"><i class="fa fa-map-marker"></i>发货地址</li>
							</ul>

							<div class="tab-content">
								<!--地图弹框-->
								<div class="modal fade" id="myModal_2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel_2" aria-hidden="true">
									<div class="modal-dialog">
										<div class="modal-content" style="border-radius: 5px;">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
												<h4 class="modal-title" id="myModalLabel_2">获取位置</h4>
											</div>
											<div class="modal-body" style="text-align: center;padding:0px;">
												<div id="map" tyle="position:absolute;">
													<div style="width:600px;height:340px;" id="mapform"></div>
												</div>
											</div>                                  
										</div>
									</div>
								</div>
								<!--地图弹框-->
								<form class="form-horizontal" role="form" id="inputForm" action="update.jhtml" method="post">
									<input type="hidden" name="id" value="${deliveryCenter.id}"/>
									<div class="form-group">
										<label class="col-sm-2 control-label">编码</label>
										<div class="col-sm-5">
											<select class="form-control" name="sn" onchange="get(this.value)">
												<option value="${deliveryCenter.sn}">${deliveryCenter.sn}</option>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>名称</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="name" value="${deliveryCenter.name}">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>联系人</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="contact" value="${deliveryCenter.contact}">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>地区</label>
										<div class="col-sm-8">
											<input type="hidden" id="areaId" name="areaId" treePath="${(deliveryCenter.area.treePath)!}" value="${(deliveryCenter.area.id)!}"/>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>地址</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" id="address" name="address" value="${deliveryCenter.address}">
										</div>
									</div>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label"><span class="red">*</span>营业时间</label>
                                        <div class="col-sm-2">
                                            <input type="text" id="startTime" name="start_time" class="form-control"
                                                   onfocus="WdatePicker({dateFmt: 'HH:mm:00',minDate:'6:00:00',maxDate:'21:00:00'});"
                                                   placeholder="开始时间" [#if deliveryCenter.startTime??]value="${deliveryCenter.startTime?string('HH:mm:ss')}[/#if]"/>
                                        </div>
										<div class="col-sm-2">
                                            <input type="text" id="endTime" name="end_time" class="form-control"
                                                   onfocus="WdatePicker({dateFmt: 'HH:mm:00',minDate:'6:00:00',maxDate:'21:00:00'});"
                                                   placeholder="结束时间" [#if deliveryCenter.endTime??]value="${deliveryCenter.endTime?string('HH:mm:ss')}[/#if]"/>
										</div>
                                    </div>
                                    [#if is_default=="false"]
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label"><span class="red"></span></label>
                                        <div class="col-sm-2">
                                            <input type="checkbox" id="address" name="defaultTime" onchange="judge_is_default(this)">与默认门店相同
                                        </div>
                                    </div>
                                    [/#if]
									<div class="form-group">
										<label class="col-sm-2 control-label">所属商圈</label>
										<div class="col-sm-5">
											<select class="form-control" id="communityId" name="communityId">
												<option value="">--请选择--</option>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><span class="red">*</span>地理位置</label>
										<div class="col-sm-2">
											<input type="text" class="form-control" name="lat" id="lat" placeholder="lat" value="[#if deliveryCenter.location??]${deliveryCenter.location.lat}[/#if]">
										</div>
										<div class="col-sm-2">
											<input type="text" class="form-control" name="lng" id="lng" placeholder="lng" value="[#if deliveryCenter.location??]${deliveryCenter.location.lng}[/#if]">
										</div>	
										<div class="col-sm-2">
											<input type="button" class="btn btn-block btn-default" onclick="openMap();" 
											data-toggle="modal" data-target="#myModal_2" value="获取"/>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">手机</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="mobile" value="${deliveryCenter.mobile}">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">是否默认</label>
										<div class="col-sm-8">
											<input type="checkbox" name="isDefault" style="margin-top:10px;" [#if deliveryCenter.isDefault] checked="checked"[/#if]/>
											<input type="hidden" name="_isDefault" value="false" />
										</div>
									</div>

									<div class="form-group">
										<div class="col-sm-offset-2 col-sm-2">
											<button type="submit" class="btn btn-block btn-success">确定</button>
										</div>
										<div class="col-sm-offset-0 col-sm-2">
											<button class="btn btn-block btn-default" onclick="history.back()">返回</button>
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
	<script type="text/javascript" src="${base}/resources/store/js/jquery.lSelect.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2"></script>
    <script type="text/javascript" src="${base}/resources/store/datePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		$().ready(function() {
			var $inputForm = $("#inputForm");
			var $areaId = $("#areaId");
			var $communityId = $("#communityId");
			var timeout;
			getCommunity();
				//获取商圈
				function getCommunity() {
					if ($areaId.val()>0) {
						$.ajax({
							url: "get_community.jhtml",
							type: "GET",
							data: {areaId:$areaId.val()},
							dataType: "json",
							cache: false,
							success: function(map) {
								var opt="<option value=0>--请选择--</option>";
								for (var key in map) {
									opt = opt +"<option value="+key+">"+map[key].name+"</option>";
								}
								$communityId.html(opt);
							}
						});
					} else {
						$communityId.html("<option value=0>--无--</option>");
					}
				}

				function areaSelect(){
					clearTimeout(timeout);
					timeout = setTimeout(function() {
						getCommunity();
					}, 500);
				}

				// 地区选择
				$areaId.lSelect({
					url: "${base}/common/area.jhtml",
					fn : getCommunity
				});

				// 表单验证
				$inputForm.validate({
					rules: {
						name:"required",
						contact: "required",
						areaId: "required",
						address: "required",
						start_time:"required",
						end_time:"required",
						lat:"required",
						zipCode:{
							pattern:/^\d{3,8}$/
						},
						phone:{
							pattern:/^((\d{3,4}-\d{7,8})|(1[23456798]\d{9}))$/
						},
						mobile:{
							pattern:/^\d{8,11}$/
						}
					},
					messages:{
						sn:"${message("admin.validate.required")}",
                        start_time:"${message("admin.validate.required")}",
                        end_time:"${message("admin.validate.required")}",
						name:"${message("admin.validate.required")}",
						contact:"${message("admin.validate.required")}",
						areaId:"${message("admin.validate.required")}",
						address:"${message("admin.validate.required")}",
						lat:"${message("admin.validate.required")}",
						zipCode:{
							pattern:"请填写正确的邮编号码"
						},
						phone:{
							pattern:"请填写正确的号码"
						},
						mobile:{
							pattern:"请填写正确的号码"
						}
					}
				});
			});

			//从地图获取位置
			var map = new BMap.Map("mapform");// 创建Map实例
			map.centerAndZoom("厦门市", 18);
			map.addControl(new BMap.NavigationControl());
			map.addControl(new BMap.ScaleControl());
			map.addControl(new BMap.OverviewMapControl());
		  map.addEventListener("click",function(e){   //单击地图，形成折线覆盖物
		  	newpoint = new BMap.Point(e.point.lng,e.point.lat);
		  	$("#lat").val(e.point.lat);
		  	$("#lng").val(e.point.lng);
		  	$("#location").html("(lat:"+e.point.lat+",lng:"+e.point.lng+")");
		  	map.clearOverlays();
		  	var mkr = new BMap.Marker(newpoint);
		  	map.addOverlay(mkr);
		  });
		  function closeMap() {
		  	$("#map").hide();
		  }
		  function openMap()  {
		  	$.ajax({
		  		url: "${base}/common/area_key.jhtml",
		  		type: "GET",
		  		data: {id:$("#areaId").val()},
		  		dataType: "json",
		  		cache: false,
		  		success: function(area) {
		  			var myGeo = new BMap.Geocoder();
		  			myGeo.getPoint(area.fullName+$("#address").val(), function(point){
		  				if (point) {
		  					$("#lat").val(point.lat);
		  					$("#lng").val(point.lng);
		  					$("#location").html("(lat:"+point.lat+",lng:"+point.lng+")");
		  					map.centerAndZoom(point, 16);
		  					map.addOverlay(new BMap.Marker(point));
		  				}
		  			}, "北京市");
		  		}
		  	});
		  	$("#map").show();
		  }
		  function judge_is_default(obj){
		  	if($(obj).prop("checked")==true){
		  		$("#startTime").val("${begin_date}");
		  		$("#endTime").val("${end_date}");
		  	}else{
		  		$("#startTime").val("[#if deliveryCenter.startTime??]${deliveryCenter.startTime?string('HH:mm:ss')}[/#if]");
		  		$("#endTime").val("[#if deliveryCenter.endTime??]${deliveryCenter.endTime?string('HH:mm:ss')}[/#if]");
		  	}
		  	
		  }
		</script>
	</body>

	</html>