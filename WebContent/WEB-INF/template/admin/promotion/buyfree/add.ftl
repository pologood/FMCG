<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.promotion.add")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<style type="text/css">
.memberRank label, .productCategory label, .brand label, .coupon label {
	min-width: 120px;
	_width: 120px;
	display: block;
	float: left;
	padding-right: 4px;
	_white-space: nowrap;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $productTable = $("#productTable");
	var $productSelect = $("#productSelect");
	var $deleteProduct = $("a.deleteProduct");
	var $productTitle = $("#productTitle");
	var $giftTable = $("#giftTable");
	var $giftSelect = $("#giftSelect");
	var $deleteGift = $("a.deleteGift");
	var $giftTitle = $("#giftTitle");
	var productIds = new Array();
	var giftIds = new Array();
	var giftItemIndex = 0;
	var promoteproductIndex = 0;
	[@flash_message /]
	
	// 商品选择
	$productSelect.autocomplete("product_select.jhtml", {
		dataType: "json",
		max: 20,
		width: 600,
		scrollHeight: 300,
		parse: function(data) {
			return $.map(data, function(item) {
				return {
					data: item,
					value: item.fullName
				}
			});
		},
		formatItem: function(item) {
			if ($.inArray(item.id, productIds) < 0) {
				return '<span title="' + item.fullName + '">' + item.fullName.substring(0, 50) + '<\/span>';
			} else {
				return false;
			}
		}
	}).result(function(event, item) {
		[@compress single_line = true]
			var trHtml = 
			'<tr class="productTr">
				<th>
					<input type="hidden" name="promotionProducts[' + promoteproductIndex + '].id" \/>
					<input type="hidden" name="promotionProducts[' + promoteproductIndex + '].product.id" value="' + item.id + '" \/>
				<\/th>
				<td>
					<span title="' + item.fullName + '">' + item.fullName.substring(0, 50) + '<\/span>
				<\/td>
				<td>
					<a href="${base}' + item.path + '" target="_blank">[${message("admin.common.view")}]<\/a>
					<a href="javascript:;" class="deleteProduct">[${message("admin.common.delete")}]<\/a>
				<\/td>
			<\/tr>';
		[/@compress]
		$productTitle.show();
		$productTable.append(trHtml);
		productIds.push(item.id);
		promoteproductIndex ++;
	});
	
	// 删除商品
	$deleteProduct.live("click", function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.deleteConfirm")}",
			onOk: function() {
				var id = parseInt($this.closest("tr").find("input:hidden")[1].value);
				productIds = $.grep(productIds, function(n, i) {
					return n != id;
				});
				$this.closest("tr").remove();
				if ($productTable.find("tr.productTr").size() <= 0) {
					$productTitle.hide();
				}
			}
		});
	});
	
	// 赠品选择
	$giftSelect.autocomplete("gift_select.jhtml", {
		dataType: "json",
		max: 20,
		width: 600,
		scrollHeight: 300,
		parse: function(data) {
			return $.map(data, function(item) {
				return {
					data: item,
					value: item.fullName
				}
			});
		},
		formatItem: function(item) {
			if ($.inArray(item.id, giftIds) < 0) {
				return '<span title="' + item.fullName + '">' + item.fullName.substring(0, 50) + '<\/span>';
			} else {
				return false;
			}
		}
	}).result(function(event, item) {
		[@compress single_line = true]
			var trHtml = 
			'<tr class="giftTr">
				<th>
					<input type="hidden" name="giftItems[' + giftItemIndex + '].gift.id" value="' + item.id + '" \/>&nbsp;
				<\/th>
				<td>
					<span title="' + item.fullName + '">' + item.fullName.substring(0, 50) + '<\/span>
				<\/td>
				<td>
					<input type="text" name="giftItems[' + giftItemIndex + '].quantity" class="text giftItemQuantity" value="1" maxlength="9" style="width: 30px;" \/>
				<\/td>
				<td>
					<a href="${base}' + item.path + '" target="_blank">[${message("admin.common.view")}]<\/a>
					<a href="javascript:;" class="deleteGift">[${message("admin.common.delete")}]<\/a>
				<\/td>
			<\/tr>';
		[/@compress]
		$giftTitle.show();
		$giftTable.append(trHtml);
		giftIds.push(item.id);
		giftItemIndex ++;
	});
	
	// 删除赠品
	$deleteGift.live("click", function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.deleteConfirm")}",
			onOk: function() {
				var id = parseInt($this.closest("tr").find("input:hidden").val());
				giftIds = $.grep(giftIds, function(n, i) {
					return n != id;
				});
				$this.closest("tr").remove();
				if ($giftTable.find("tr.giftTr").size() <= 0) {
					$giftTitle.hide();
				}
			}
		});
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
		"${message("admin.promotion.compare")}"
	);
	
	$.validator.addClassRules({
		giftItemQuantity: {
			required: true,
			integer: true,
			min: 1
		}
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			title: "required",
			minimumQuantity: "digits",
			maximumQuantity: {
				digits: true,
				compare: "#minimumQuantity"
			},
			minimumPrice: {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			maximumPrice: {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				},
				compare: "#minimumPrice"
			},
			priceExpression: {
				remote: {
					url: "check_price_expression.jhtml",
					cache: false
				}
			},
			pointExpression: {
				remote: {
					url: "check_point_expression.jhtml",
					cache: false
				}
			},
			order: "digits"
		}
	});
	
	$('input[name="minimum"]').click(function(){
		var buyfree=$('input[name="minimum"]:checked').val();
		$('#minimumQuantity').val(buyfree);
		if($('#others').attr("checked")){
			$('#minimumQuantity').show();
		}else{
			$('#minimumQuantity').hide();
		}
	});
	
	
});
</script>

</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo;买赠添加
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<input type="hidden" name="type" value="buyfree" />
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.promotion.base")}" />
			</li>
		</ul>
		<div class="tabContent">
			<table class="input">
				<tr>
					<th>
						<span class="requiredField">*</span>${message("Promotion.name")}:
					</th>
					<td colspan="2">
						<input type="text" name="name" class="text" maxlength="200" />
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>活动描述:
					</th>
					<td colspan="2">
						<input type="text" name="title" class="text" maxlength="200" title="描述买赠活动的介绍信息"/>
					</td>
				</tr>
				<tr>
					<th>
						活动开始日期:
					</th>
					<td colspan="2">
						<input type="text" id="beginDate" name="beginDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
					</td>
				</tr>
				<tr>
					<th>
						活动结束日期:
					</th>
					<td colspan="2">
						<input type="text" id="endDate" name="endDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
					</td>
				</tr>
				<tr>
					<th>
						买赠的方式:
					</th>
					<td colspan="2">
						<label>
							<input type="radio" name="minimum" value="1"/>买一赠一
						</label>
						<label>
							<input type="radio" name="minimum" value="2"/>买二赠一
						</label>
						<label>
							<input type="radio" name="minimum" value="" id="others"/>自定义
							<input type="text" id="minimumQuantity" placeholder="请输入输入数字" style="display:none"
							name="minimumQuantity" class="text" maxlength="9" title="例如：填写1就是买一赠一活动"/>
						</label>	
					</td>
				</tr>
				<tr>
					<th>
						单人限购数量:
					</th>
					<td colspan="2">
						<input type="text" name="maximumQuantity" class="text" maxlength="9"  placeholder="请输入数字"/>
					</td>
				</tr>
				<tr>
					<th>
						${message("admin.common.order")}:
					</th>
					<td>
						<input type="text" name="order" class="text" maxlength="9" placeholder="请输入数字"/>
					</td>
				</tr>
				<tr>
					<th>
						${message("admin.common.setting")}:
					</th>
					<td colspan="2">
						<label>
							<input type="checkbox" name="isFreeShipping" value="true" />${message("Promotion.isFreeShipping")}
							<input type="hidden" name="_isFreeShipping" value="false" />
						</label>
						<label>
							<input type="checkbox" name="isCouponAllowed" value="true" checked="checked" />${message("Promotion.isCouponAllowed")}
							<input type="hidden" name="_isCouponAllowed" value="false" />
						</label>
						<label>
							<input type="checkbox" name="isDoublePoint" value="true"/>是否双倍积分
							<input type="hidden" name="_isDoublePoint" value="false" />
						</label>
					</td>
				</tr>
			</table>
			<table id="productTable" class="input">
				<tr>
					<th>
						允许参与商品:
					</th>
					<td colspan="2">
						<input type="text" id="productSelect" name="productSelect" class="text" maxlength="200" title="${message("admin.promotion.productSelectTitle")}" />
					</td>
				</tr>
				<tr id="productTitle" class="title hidden">
					<th>
						&nbsp;
					</th>
					<td width="712">
						${message("Product.name")}
					</td>
					<td>
						${message("admin.common.handle")}
					</td>
				</tr>
			</table>
			<table id="giftTable" class="input">
				<tr>
					<th>
						赠送的礼品:
					</th>
					<td colspan="3">
						<input type="text" id="giftSelect" name="giftSelect" class="text" maxlength="200" title="${message("admin.promotion.giftSelectTitle")}" />
					</td>
				</tr>
				<tr id="giftTitle" class="title hidden">
					<th>
						&nbsp;
					</th>
					<td width="500">
						${message("Product.name")}
					</td>
					<td width="200">
						${message("GiftItem.quantity")}
					</td>
					<td>
						${message("admin.common.handle")}
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