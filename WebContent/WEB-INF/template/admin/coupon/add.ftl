<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.coupon.add")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/b2b/css/v2.0/member.css" type="text/css" rel="stylesheet" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/v2.0/laddSelect.js"></script>
<script type="text/javascript" src="${base}/resources/data/data.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isExchange = $("#isExchange");
	var $point = $("#point");
	var $xbrand = $("#xbrand");
	var $txtBrand = $("#txtBrand");
	var $areaDl = $("#areaDl");
	var $seriesestr = $("#seriesestr");
	[@flash_message /]
	
	//全部商品分类
	initCategoryes();
	for(var i=0;i<categoryes.length;i++){
		$areaDl.append('<dd phonetic="'+categoryes[i].name+'" phonetid="'+categoryes[i].id+'">'+categoryes[i].name+'</dd>');
	}
	
	$(function(){
		$(document).click(function(){
			$areaDl.hide();
		});
		$txtBrand.click(function(evt){
			stopEventBubble(evt);
		});
		$txtBrand.keyup(function(){
			searchBrand(this,"areaDl");
		}).focus(function(){
			$areaDl.show();
			$("#areaDl dd").show();
			$("#areaDl dt").hide();
		});
		$("#areaDl dd").click(function(){
			$txtBrand.val($(this).text());
			$txtBrand.attr("phonetic",$(this).attr("phonetic"));
			$txtBrand.attr("phonetid",$(this).attr("phonetid"));
			$xbrand.val("");
			
			if ($txtBrand.attr("phonetid") != null && $txtBrand.attr("phonetid") != ""){
				branddataId = $txtBrand.attr("phonetid");
				$xbrand.laddSelect({
	    				choose:"全部",
	    				emptyValue:branddataId,
						url: "${base}/admin/coupon/productCategorySeries.jhtml"
				});
			}
		});
	});
	
	// 下拉输入框
	// 阻止事件冒泡
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
			if ($true){
				$areaDl.show().children("dt").show();
			}
		}
	}
	$("#addSeries").click(function() {
		id = $txtBrand.attr('phonetid');
		name = $txtBrand.attr('phonetic');
		if(id!=null&&id!="") {
			$seriesestr.show();
		}
		if($xbrand.val()!=null && $xbrand.val()!=""){
			id = $xbrand.val();
			name=$("option[value="+id+"]").html();
		}
  		$("#"+id).remove();
		$("#serieses").append("<span id="+id+"><input type=\"hidden\" name=\"productCategorys\" value=\""+id+"\">"+name+"<b title='删除' class='delete_b'>×</b>; </span>");
		
	});
	
	$(".delete_b").live("click",function(){
		$(this).parent("span").remove();
		if($("#serieses").children("span").length == 0){
			$xbrand.val("");
  			$seriesestr.hide();
		}
	});
	
	$("#clearSeries").click(function() {
		$("#serieses").html("");
		$txtBrand.val("");
		$txtBrand.removeAttr("phonetic");
		$txtBrand.removeAttr("phonetid");
		$xbrand.val("");
		$seriesestr.hide();
		$("span.fieldSet select").remove();
	});
	
	// 是否允许积分兑换
	$isExchange.click(function() {
		if ($(this).prop("checked")) {
			$point.prop("disabled", false);
		} else {
			$point.val("").prop("disabled", true);
		}
	});
	
	$.validator.addMethod("compare", 
		function(value, element, param) {
			var parameterValue = $(param).val();
			if ($.trim(parameterValue) == "" || $.trim(value) == "") {
				return true;
			}
			try {
				return parseFloat(parameterValue) <= parseFloat(value);
			} catch(e) {
				return false;
			}
		},
		"${message("admin.coupon.compare")}"
	);
	
	jQuery.validator.addMethod("alnum", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
	}, "只能包括英文字母和数字");
	
	// 表单验证
	$inputForm.validate({
		rules: {
//			name: "required",
//			prefix: {
//				required:true,
//				alnum:true
//			},
			amount:{
				required: true,
				number:true,
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			[#--startDate: "required",--]
			[#--endDate: "required",--]
			[#--minimumPrice: {--]
				[#--required: true,--]
				[#--number:true,--]
				[#--min: 0,--]
				[#--decimal: {--]
					[#--integer: 12,--]
					[#--fraction: ${setting.priceScale}--]
				[#--}--]
			[#--},--]
			[#--point: {--]
				[#--required: true,--]
				[#--number:true,--]
				[#--digits:true,--]
				[#--min: 0--]
			[#--},--]
			[#--introduction:"required"--]
		},
		messages: {
			name: "名称不能为空",
			prefix: {
				required: "前缀不能为空",
				alnum: "只能包括英文字母和数字"
			},
			amount:{
				required: "金额不能为空",
				number:"只能是数字",
				min: "金额最小为0",
				decimal: "小数点前最长12位，且小数点后只能有2位小数"
			},
			startDate: "开始时间不能为空",
			endDate: "结束时间不能为空",
			minimumQuantity: {
				required: "最小数量不能为空",
				number:"最小数量必须是数字",
				digits:"最小数量必须是整数",
				min: "最小数量不能小为0"
			},
			minimumPrice: {
				required: "最小订单价格不能为空",
				number: "最小订单价格必须是数字",
				min: "最小订单价格不能小于0",
				decimal: "小数点前最长12位，且小数点后只能有2位小数"
			},
			point: {
				required: "兑换积分不能为空",
				number:"兑换积分必须是数字",
				digits:"兑换积分必须是整数",
				min: "兑换积分不能小为0"
			},
			introduction: "必须填写介绍"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 添加套券
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.coupon.base")}" />
			</li>
			<li>
				<input type="button" value="${message("Coupon.introduction")}" />
			</li>
		</ul>
		<input type="hidden" name="type" class="text" value="coupon"/>
		<input type="hidden" name="isReceiveMore" class="text" value="false"/>
		<input type="hidden" name="receiveTimes" class="text" value="1"/>
		<div class="tabContent">
			<table class="input">
				<tr>
					<th>
						名称:
					</th>
					<td>
						<input type="text" name="name" class="text" maxlength="200" />
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>金额:
					</th>
					<td>
						<input type="text" name="amount" class="text" maxlength="200" />
					</td>
				</tr>
                [#--<tr>
                    <th>
                        允许参与商品分类:
                    </th>
                    <td>
                        <div id="areaDiv">
                            <input type="text" id="txtBrand" placeholder="选择商品分类" class="input_expand"/>
                            <dl id="areaDl">
                                <dt phonetic="0">搜索无结果</dt>
                            </dl>
                        </div>
                        <span class="fieldSet">
					<input type="hidden" id="xbrand" name="xbrand"/>
					</span>
                        <input type="button" id="addSeries" class="button" value="添加"/>
                        <input type="button" id="clearSeries" class="button" value="清除"/>
                    </td>
                </tr>
				<tr id="seriesestr" style="display:none;">
					<th>
						-
					</th>
					<td id="serieses">
					</td>
				</tr>
				<tr class="coupon">
					<th>
						允许参与支付类型
					</th>
					<td colspan="2">
						<select name="method">
							<option value="" selected="selected">无限制</option>
							<option value="balance">平台账户</option>
							<option value="online">网上银行</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>
                        最小商品价格:
					</th>
					<td colspan="2">
						<input type="text" id="minimumPrice" name="minimumPrice" value="0" class="text" maxlength="16" />
					</td>
				</tr>
				<tr>
					<th>
                        设置:
					</th>
					<td>
						<label>
							<input type="checkbox" name="isEnabled" value="true" checked="checked" />是否启用
							<input type="hidden" name="_isEnabled" value="false" />
						</label>
						<label>
							<input type="checkbox" id="isExchange" name="isExchange" value="true" />是否允许积分兑换
							<input type="hidden" name="_isExchange" value="false" />
						</label>
					</td>
				</tr>
				<tr>
					<th>
                        积分兑换数:
					</th>
					<td>
						<input type="text" id="point" name="point" class="text" value="0" maxlength="9" />
					</td>
				</tr>--]
			</table>
		</div>
		<div class="tabContent">
			<table class="input">
				<tr>
					<td>
						<textarea id="editor" name="introduction" class="editor" style="width: 100%;"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<table class="input">
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