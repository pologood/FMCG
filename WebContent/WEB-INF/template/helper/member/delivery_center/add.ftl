<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>我的网店</title>
	<meta name="baidu-site-verification" content="7EKp4TWRZT" />
	[@seo type = "index"]
	<title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if] </title>
	[#if seo.keywords??]
	<meta name="keywords" content="[@seo.keywords?interpret /]" />
	[/#if]
	[#if seo.description??]
	<meta name="description" content="[@seo.description?interpret /]" />
	[/#if]
	[/@seo]
	<link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet" />
	<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
	<script type="text/javascript">
		$().ready(function() {
			var $inputForm = $("#inputForm");
			var $areaId = $("#areaId");
			var $communityId = $("#communityId");
			var timeout;
			[@flash_message /]

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
								opt = opt +"<option value="+key+">"+map[key]+"</option>";
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
			fn:areaSelect
		});

	// 表单验证
	$inputForm.validate({
		rules: {
			sn: "required",
			name: {
				required:true,
				remote:{
					type:"post",
					url:"checkName.jhtml",
					data:{
						name:function(){return $("[name='name']").val();}
					}
				}
			},
			contact: "required",
			areaId: "required",
			address: "required",
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
			name:{
				required:"${message("admin.validate.required")}",
				remote:"门店名称已存在"
			},
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
</script>

</head>
<body>

	[#include "/helper/include/header.ftl" /]
	[#include "/helper/member/include/navigation.ftl" /]
	<div class="desktop">
		<div class="container bg_fff">

			[#include "/helper/member/include/border.ftl" /]

			[#include "/helper/member/include/menu.ftl" /]

			<div class="wrapper" id="wrapper">

				<div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
					<div class="js-app-header title-wrap" id="app_0000000844">
						<img class="js-app_logo app-img" src="${base}/resources/helper/images/address-manage.png"/>
						<dl class="app-info">
							<dt class="app-title" id="app_name">发货地址</dt>
							<dd class="app-status" id="app_add_status">
							</dd>
							<dd class="app-intro" id="app_desc">维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等。</dd>
						</dl>
					</div>
					<ul class="links" id="mod_menus">
						<li  ><a class="on" hideFocus="" href="javascript:;">添加地址</a></li>
						<li  ><a class="" hideFocus="" href="${base}/helper/member/delivery_center/list.jhtml">地址列表</a></li>
					</ul>

				</div>
				<div class="list" style="margin-top: 0px;border-right: 1px solid #d8deea;">
					<div id="map" style="position:absolute;margin-top:110px;margin-left:380px;display: none;">
						<div style="width:520px;height:340px;border:1px solid gray" id="mapform"></div>
						<p><input type="button" onclick="closeMap()" value="关闭" /></p>
					</div>
					<form id="inputForm" action="save.jhtml" method="post">
						<table class="input">
							<tr>
								<th>
									<span class="requiredField">*</span>编码:
								</th>
								<td>
									[#assign deliverCenterSn=deliveryCenters?size]
									<select name="sn" class="text" onchange="get(this.value)">
										[#if deliverCenterSn <= 9]
											<option value="${member.tenant.id+100000000}000${deliverCenterSn+1}">
												${member.tenant.id+100000000}000${deliverCenterSn+1}
											</option>
										[#elseif deliverCenterSn <=99&&deliverCenterSn >9]
											<option value="${member.tenant.id+100000000}00${deliverCenterSn+1}">
												${member.tenant.id+100000000}00${deliverCenterSn+1}
											</option>
										[#elseif deliverCenterSn <=999&&deliverCenterSn >99]
											<option value="${member.tenant.id+100000000}0${deliverCenterSn+1}">
												${member.tenant.id+100000000}0${deliverCenterSn+1}
											</option>
										[#else]
											<option value="${member.tenant.id+100000000}${deliverCenterSn+1+1}">
												${member.tenant.id+100000000}${deliverCenterSn+1}
											</option>
										[/#if]

									</select>
									<!-- <input type="text" name="sn" class="text" maxlength="20" value="" /> -->
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("DeliveryCenter.name")}:
								</th>
								<td>
									<input type="text" name="name" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("DeliveryCenter.contact")}:
								</th>
								<td>
									<input type="text" name="contact" class="text" maxlength="20" />
								</td>
							</tr>
							<tr>
								<th>
									<span class="requiredField">*</span>${message("DeliveryCenter.area")}:
								</th>
								<td>
									<span class="fieldSet">
										<input type="hidden" id="areaId" name="areaId" />
									</span>
								</td>
							</tr>

							<tr>
								<th>
									<span class="requiredField">*</span>${message("DeliveryCenter.address")}:
								</th>
								<td >
									<input type="text" id="address" name="address" class="text" maxlength="30" />
								</td>
							</tr>

							<tr>
								<th>
									<span class="requiredField">*</span>地理位置:
								</th>
								<td >
									<input type="hidden" name="lat" id="lat" class="text"/>
									<input type="hidden" name="lng" id="lng" class="text"/>
									<span id="location"></span><input type="button"  class="button" onclick="openMap();" value="获取" />
								</td>
							</tr>

							<tr>
								<th>
									${message("DeliveryCenter.mobile")}:
								</th>
								<td>
									<input type="text" name="mobile" class="text" maxlength="12" />
								</td>
							</tr>
							<tr>
								<th>
									${message("DeliveryCenter.isDefault")}:
								</th>
								<td>
									<input type="checkbox" name="isDefault" />
									<input type="hidden" name="_isDefault" value="false" />
								</td>
							</tr>

							<tr>
								<th>
									&nbsp;
								</th>
								<td>
									<input type="submit" class="button" value="${message("admin.common.submit")}" hidefocus/>
									<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='list.jhtml'" hidefocus/>
								</td>
							</tr>
						</table>
					</form>
				</div>

			</div>
		</div>
	</div>
	[#include "/helper/include/footer.ftl" /]
</body>
</html>

<script type="text/javascript">
  var map = new BMap.Map("mapform");                        // 创建Map实例
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
</script>
