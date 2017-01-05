<!DOCTYPE HTML>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>${setting.siteName}</title>
<link rel="stylesheet" href="${base}/resources/pad/css/library.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/pad/css/common.css" />
<script type="text/javascript" src="${base}/resources/pad/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/pad/js/regist.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $return_back = $("#return_back");
	var $communityId = $("#communityId");
	var $areaId = $("#areaId");
	var $community=$("#p_rescroller_6 ul");
	var $tenantType_ul=$("#p_rescroller_5 ul li");
	var $tenantCategory_ul=$("#p_rescroller_4 ul li");
	var $tenantType=$("#tenantType");
	var $tenantCategoryId=$("#tenantCategoryId");
	var $province = $("#province");
	var $p_province=$("#p_rescroller ul");
	var $city = $("#city");
	var $p_city=$("#p_rescroller_1 ul");
	var $district = $("#district");
	var $p_district=$("#p_rescroller_2 ul");
	var $submit = $("#submit");
	var $telephone = $("#telephone");
	var $submitForm = $("#submitForm");
	var $tenantCategory_s = $("#tenantCategory_s");
	var $tenantType_s = $("#tenantType_s");
	
	//提交
	$submit.on("touchstart",function(){
		if($telephone.val()==''||!(/^1[3|4|5|8][0-9]\d{4,8}$/.test($telephone.val()))){
			alert("请输入合法的电话号码");return false;
		}
		if($province.attr("key")==""||$city.attr("key")==""){
			alert("省份和城市不能为空");return false;
		}
		if($district.attr("key")==""){
			$areaId.val($city.attr("key"));
		}else{
			$areaId.val($district.attr("key"));
		}
		if($areaId.val()==""){
			alert("区域必选");return false;
		}
		$submitForm.submit();
		return false;
	});
	
	//分类选择
	$tenantCategory_ul.on("touchstart",function(){
		var $this = $(this);
		$tenantCategoryId.val($this.attr("key"));
		$tenantCategory_s.text($this.text());
		return false;
	});
	//企业性质选择
	$tenantType_ul.on("touchstart",function(){
		var $this= $(this);
		$tenantType.val($this.attr("key"));
		$tenantType_s.text($this.text());
		return false;
	});
	//返回
	$return_back.on("touchstart",function(){
		location.href="${base}/pad/member/center.jhtml";
		return false;
	});
	//省份
	$province.on("touchstart",function(){
		$.ajax({
			url :"${base}/common/area.jhtml?parentId=0",
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_province.empty();
        		for(var key in data){
        			$p_province.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_province.iScroll('refresh');
				$p_province.find("li").on('touchstart', function () {
					var $this = $(this);
					$province.text($this.text());
				    $province.attr("key",$this.attr("key"));
				    $city.attr("key","").text("选择城市");$district.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//城市
	$city.on("touchstart",function(){
		if($province.attr("key")==""){
			alert("请先选择省份");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$province.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_city.empty();
        		for(var key in data){
        			$p_city.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_city.iScroll('refresh');
				$p_city.find("li").on('touchstart', function () {
					var $this = $(this);
					$city.text($this.text());
				    $city.attr("key",$this.attr("key"));
				    $district.attr("key","").text("选择地区");
				});
        	}
		});
	});
	//地区
	$district.on("touchstart",function(){
		if($city.attr("key")==""){
			alert("请先选择城市");
			return false;
		}
		$.ajax({
			url :"${base}/common/area.jhtml?parentId="+$city.attr("key"),
        	type:'get',
        	dataType:'json',
        	success:function(data){
				if(data==null){
					return false;
				} 
				$p_district.empty();
        		for(var key in data){
        			$p_district.append("<li key="+key+">"+data[key]+"</li>");
        		}
        		$p_district.iScroll('refresh');
				$p_district.find("li").on('touchstart', function () {
					var $this = $(this);
					$district.text($this.text());
				    $district.attr("key",$this.attr("key"));
				});
        	}
		});
	});
	
});
</script>
</head>
<body>
<section class="p_section">
	<div class="p_header">
		<div class="p_hbody">
			<a href="javascript:;" class="p_return" id="return_back">
				<div class="p_tag"></div>
			</a>
			<div class="p_title">申请开店</div>
		</div>
	</div>
	<article class="p_article p_shopregistc p_darticle0 p_zc">
		<div class="bodycont">
			<div class="p_userrg_wind p_userrg_wind_1" id="p_nactscrooler_4">
				<form id="submitForm" action="apply.jhtml" method="post">
					<input type="hidden" name="username" value="${username}" />
					<input type="hidden" name="communityId" id="communityId" />
					<input type="hidden" name="no" id="no" value="0" />
					<input type="hidden" name="areaId" id="areaId" value="${(area.id)!}" />
					<input type="hidden" name="tenantType" id="tenantType" value="${tenant.tenantType}" />
					<input type="hidden" name="tenantCategoryId" id="tenantCategoryId" value="${(tenant.tenantCategory.id)!}" />
					<div class="text">
						<p><input type="text" placeholder="${tenant.name}的店铺" name="name" value="${tenant.name}的店铺" /></p>
					</div>
					<div class="text">
						<div class="p_area p_areashop">
							<div class="p_areap p_areap_4"><span id="tenantCategory_s">店铺分类</span></div>
						</div>
					</div>
					<div class="text">
						<div class="p_area p_areashop">
							<div class="p_areap p_areap_5"><span id="tenantType_s">店铺性质</span></div>
						</div>
					</div>
					<div class="text">
						<div class="p_area">
							<div class="p_areap p_areap_1"><span id="province" key="${(province.id)!}">
									[#if province??]
										${province.name}
									[#else]
										选择省份
									[/#if]</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_2"><span id="city" key="${(city.id)!}">
								[#if city??]
										${(city.name)!}
									[#else]
										选择城市
									[/#if]
								</span></div>
						</div>
						<div class="p_area">
							<div class="p_areap p_areap_3"><span id="district" key="${(district.id)!}">
									[#if district??]
										${(district.name)!}
									[#else]
										选择地区
									[/#if]
							</span></div>
						</div>
					</div>
					<div class="text">
						<p><input type="text" placeholder="请输入地址" name="address"/></p>
					</div>
					<div class="text">
						<p><input type="text" placeholder="请输入联系人/法人" name="legalRepr"/></p>
					</div>
					<div class="text">
						<p><input type="text" placeholder="请输入联系电话" id="telephone" name="telephone" value="${tenant.telephone}"/></p>
					</div>
					<div class="text">
						<p><input type="text" placeholder="请输入经营许可证" name="licenseCode"/></p>
					</div>
					<div class="text">
						<a href="javascript:;" class="p_nopwbtn" id="submit">提交申请</a>
					</div>
				</form>
			</div>
		</div>
	</article>
	<div class="p_areawz p_areawzshop">
		<div class="p_areawzul">
			<div class="p_areaselect p_areaselect_1" id="p_rescroller">
				<ul>
				</ul>
			</div>
			<div class="p_areaselect p_areaselect_2" id="p_rescroller_1">
				<ul>
				</ul>
			</div>
			<div class="p_areaselect p_areaselect_3" id="p_rescroller_2">
				<ul>
				</ul>
			</div>
			<div class="p_areaselect p_areaselect_4" id="p_rescroller_4">
				<ul>
					[#if tenantCategoryTree??&&tenantCategoryTree?has_content]
						[#list tenantCategoryTree as tenantCategory]
							<li key="${tenantCategory.id}">${tenantCategory.name}</li>
						[/#list]
					[/#if]
				</ul>
			</div>
			<div class="p_areaselect p_areaselect_5" id="p_rescroller_5">
				<ul>
					<li key="enterprise">供应商</li>
					<li key="individual">团队</li>
					<!-- li key="organization">事业单位或社会团体</li>
					<li key="personal">个人</li -->
				</ul>
			</div>
			<div class="p_areaselect p_areaselect_6" id="p_rescroller_6">
				<ul></ul>
			</div>
		</div>
	</div>
</section>
</body>
</html>
