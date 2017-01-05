<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.article.edit")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/b2b/css/v2.0/member.css" type="text/css" rel="stylesheet" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript">

$().ready(function() {

	var $areaId = $("#areaId");
	var $selectedmembervalue = $("#selectedmembervalue");
	var $selectedmember = $("#selectedmember");
	var $selectedCleanmember = $("#selectedCleanmember");
	var $inputForm = $("#inputForm");
	var $xbrand = $("#xbrand");
	var $txtBrand = $("#txtBrand");
	var $addSeries = $("#addSeries");
	var $clearSeries = $("#clearSeries");
	var id;
	
	[@flash_message /]

	// 地区选择
	$areaId.lSelect({
		url: "${base}/common/area.jhtml"
	});
	
	function queryLoadBrand(username) {
		$.ajax({
			url: "${base}/admin/snDeposit/search.jhtml",
			type: "POST",
			data: {username:username},
			dataType: "json",
			cache: false,
			success: function(message) {
				var $member =$("input[name='member']");
				var $memberstd = $(".members td");
				$memberstd.append("");
				for (var i = 0; i < message.length; i++) {
					var flag = true;
					$member.each(function() {
						var $this = $(this);
						if ($this.val() == message[i].id) {
							flag = false;
						}
					});
					if (flag) {
						$memberstd.append("<label><input type='radio' name='member' value='" + message[i].id + "'/>" + message[i].username  + "</label>");
					}
				}
			}
		});
	}

	$(function(){
		if ($("#enterprisetype").val()=="personproxy")
		{
			$("#Brandtr").show();
			$("#seriesestr").show();
		}
		if ($("#enterprisetype").val() == "provinceproxy")
		{
			$("select[name='areaId_select']").eq(1).hide();
			$("select[name='areaId_select']").eq(2).remove();
		}
		if ($("#enterprisetype").val() == "cityproxy")
		{
			$("select[name='areaId_select']").eq(2).hide();
		}
	});
	$selectedmember.click(function() {
		if ($selectedmembervalue.val().replace(/\s/g,"") == "") {
			return;
		} else {
			queryLoadBrand($selectedmembervalue.val());
		}
	});
	
	$selectedCleanmember.click(function() {
		var $label =$(".members td label");
		$label.each(function() {
			var $this = $(this);
			if(!$this.find("input")[0].checked) {
				$this.remove();
			}
		});
	});
	
	
	$addSeries.click(function() {
		id = $("#txtBrand").attr('phonetid');
		if($xbrand.val()!=null && $xbrand.val()!=""){
			id = $xbrand.val();
		}
		$.ajax({
			url: "${base}/admin/tenant/getTenant.jhtml",
			type: "GET",
			data: {id:id},
			dataType: "json",
			cache: false,
			success: function(map) {
		  		$("#"+map.id).remove();
				$("#serieses").append("<span id="+map.id+"><input type=\"hidden\" name=\"seriesIds\" value=\""+map.id+"\">"+map.name+"<b title='删除' class='delete_b'>×</b>; </span>");
			}
		});
	});

	$clearSeries.click(function() {
  			$("#serieses").html("");
  			$("#txtBrand").val("");
  			$("#txtBrand").removeAttr("phonetic");
  			$("#txtBrand").removeAttr("phonetid");
  			$("#xbrand").val("");
		});
		
	// 表单验证
	$inputForm.validate({
		rules: {
			"snNumber": {
				required: true,
				integer: true,
				min:1
			}
		}
	});


	$(".delete_b").live("click",function(){
		$(this).parent("span").remove();
	});
//下拉输入框
//阻止事件冒泡

		function stopEventBubble(event){
			var e=event || window.event;
		
			if (e && e.stopPropagation){
				e.stopPropagation();    
			}
			else{
				e.cancelBubble=true;
			}
		}
		function searchBrand(obj,oid){
			var $this = $(obj).val();
			var $phoneticLen,$strLen;
			var $areaDl = $("#"+oid);
			var $true = true;
			$areaDl.hide();
			$areaDl.children("dd").hide();
			$areaDl.children("dt").hide();
			for (var i=0;i<$areaDl.children("dd").length;i++){
					$phoneticLen = $areaDl.children("dd").eq(i).attr("phonetic").substring(0,$this.length);
					$strLen = $areaDl.children("dd").eq(i).text().substring(0,$this.length);
					if (($this == $phoneticLen || $this == $strLen) && $this != ""){
							$areaDl.show();
							$areaDl.children("dd").eq(i).show();
						}
				}
			if ($this == ""){
					$areaDl.show();
					$areaDl.children("dd").show();
				}else{
						$areaDl.children("dd").each(function(){
								if ($(this).css("display") == "block"){
										$true = false;
										return false;
									}
							});
						if ($true){$areaDl.show().children("dt").show();}
					}
		}
			$(document).click(function(){
				$("#areaDl").hide();
				});
			$("#txtBrand").click(function(evt){
					stopEventBubble(evt);
				});
			$("#txtBrand").keyup(function(){
				searchBrand(this,"areaDl");
			}).focus(function(){
				$("#areaDl").show();
				$("#areaDl dd").show();
				$("#areaDl dt").hide();
			})
			$("#areaDl dd").click(function(){
				$("#txtBrand").val($(this).text());
				$("#txtBrand").attr("phonetic",$(this).attr("phonetic"));
				$("#txtBrand").attr("phonetid",$(this).attr("phonetid"));
				$("#xbrand").val("");
			});
					
});
$(function(){
		//所属地区
		$("#enterprisetype").change(function(){
			if ($(this).val() == "personproxy")
			{
				$("#Brandtr").show();
				$("#seriesestr").show();
			}else {
				$("#Brandtr").hide();
				$("#seriesestr").hide();
			}
			if ($(this).val() == "cityproxy"){
				$("select[name='areaId_select']").eq(1).show();
				$("select[name='areaId_select']").eq(2).children().eq(0).attr("selected",true);
				$("select[name='areaId_select']").eq(2).hide();
				var valId = $("select[name='areaId_select']").eq(1).val();
				$("#areaId").val(valId);
			}else if ($(this).val() == "provinceproxy"){
				$("select[name='areaId_select']").eq(1).children().eq(0).attr("selected",true);
				$("select[name='areaId_select']").eq(1).hide();
				$("select[name='areaId_select']").eq(2).hide();
				$("select[name='areaId_select']").eq(2).remove();
				var valId = $("select[name='areaId_select']").eq(0).val();
				$("#areaId").val(valId);
			}else{
				$("select[name='areaId_select']").eq(1).show();
				$("select[name='areaId_select']").eq(2).show();
			}
		});
		$("select[name='areaId_select']").live("change",function(){
			if ($(this).index() == 1 && $("#enterprisetype").val() == "provinceproxy")
			{
				var valId = $("select[name='areaId_select']").eq(0).val();
				$("#areaId").attr("treepath",","+valId+",");
				$("#areaId").val(valId);
			}
			if ($(this).val() > 0)
			{
				$("#areaId").val($(this).val());
			}
		});
		//清空areaId默认值
		$("select[name='areaId_select']").each(function(){
			if ($(this).val() > 0)
			{
				$("#areaId").val($(this).val());
			}else {
				$(this).nextAll("select").remove();
				return false;
			}
		});
		$("select[name='areaId_select']").live("change",function(){
			if ($(this).index() == 2 && $("#enterprisetype").val() == "cityproxy")
			{
				$(this).next("select").hide();
			}else if ($(this).index() == 1 && $("#enterprisetype").val() == "provinceproxy"){
				$(this).next("select").hide();
			}
			
		});
	});
	
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 充值许可数
	</div>
	<form id="inputForm" action="charge.jhtml" method="post" enctype="multipart/form-data">
		<input type="hidden" name="id" value="${enterprise.id}" />
		<table class="input">
			<tr>
				<th>
					商家名称: 
				</th>
				<td>
					${(enterprise.name)!}
				</td>
			</tr>
			<tr>
				<th>
					结余许可数 :
				</th>
				<td>
					${(enterprise.snNumber)!}
				</td>
			</tr>
			<tr>
				<th>
					所属区域:
				</th>
				<td>
					${(enterprise.area.name)!}
				</td>
			</tr>
			<tr>
				<th>
					联系电话: 
				</th>
				<td>
					${(enterprise.telephone)!}
				</td>
			</tr>
			<tr>
				<th>
					充值许可数 :
				</th>
				<td>
					<input type="text" name="snNumber" class="text" value=""/>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='list.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>