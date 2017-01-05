<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.productCategory.add")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.brands label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	var $browserButton = $("#browserButton");
	
	var $selectedbrandvalue = $("#selectedbrandvalue");
	var $selectedbrand = $("#selectedbrand");
	var $selectedAllbrand = $("#selectedAllbrand");
	var $selectedCleanbrand = $("#selectedCleanbrand");
	
	[@flash_message /]
	$browserButton.browser();
	
	function queryLoadBrand(brandName, checked) {
		$.ajax({
			url: "${base}/admin/brand/search.jhtml",
			type: "POST",
			data: {name:brandName},
			dataType: "json",
			cache: false,
			success: function(message) {
				if (message.length > 1) {
					checked = "";
				}
				var $brandIds =$("input[name='brandIds']");
				var $brandstd = $(".brands td");
				for (var i = 0; i < message.length; i++) {
					var flag = true;
					$brandIds.each(function() {
						var $this = $(this);
						if ($this.val() == message[i].id) {
							flag = false;
						}
					});
					if (flag) {
						$brandstd.append("<label><input type='checkbox' name='brandIds' value='" + message[i].id + "' " + checked + " />" + message[i].name  + "</label>");
					}
				}
			}
		});
	}

	$selectedbrand.click(function() {
		if ($selectedbrandvalue.val().replace(/\s/g,"") == "") {
			return;
		} else {
			queryLoadBrand($selectedbrandvalue.val(), "checked='checked'");
		}
	});
	$selectedAllbrand.click(function() {
		queryLoadBrand("", "");
	});
	$selectedCleanbrand.click(function() {
		var $label =$(".brands td label");
		$label.each(function() {
			var $this = $(this);
			if(!$this.find("input")[0].checked) {
				$this.remove();
			}
		});
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			order: "digits"
		}
	});
	$("#select_value").text("顶级分类");
});
function get_root_child(obj){
	$.ajax({
        url:"${base}/common/child_category.jhtml",
        type:"get",
        data:{parentId:$(obj).val()},
        dataType:"json",
        success:function(map){
            var html="<option value=''>--请选择--</option>";
            $.each(map,function(id,name){
                html+="<option value="+id+">"+name+"</option>";
            });

            if($(obj).attr("cate")=="one"){
				$("#select_value").text($(obj).find("option:selected").text());
            	$("#second_cate").show();
            	$("#second_cate").attr("name","");
				$("#second_cate").html(html);
            }else if($(obj).attr("cate")=="two"){
            	if($("#second_cate").val()==""){
					$("#select_value").text($("#root_cate").find("option:selected").text());
					$("#second_cate").val($("#root_cate").val());
					$("#root_cate").attr("name","parentId");
            		$("#second_cate").attr("name","");
				}else{
					$("#select_value").text($(obj).find("option:selected").text());
					$("#root_cate").attr("name","");
            		$("#second_cate").attr("name","parentId");
				}
            	$("#second_cate").show();
            }
        }
    });
}
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.productCategory.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("ProductCategory.name")}:
				</th>
				<td>
					<input type="text" id="name" name="name" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.parent")}:
				</th>
				<td>
					<!-- <select name="parentId">
						<option value="">${message("admin.productCategory.root")}</option>
						[#list productCategoryTree as category]
							<option value="${category.id}">
								[#if category.grade != 0]
									[#list 1..category.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${category.name}
							</option>
						[/#list]
					</select> -->
					<select name="parentId" onchange="get_root_child(this)" id="root_cate" cate="one">
                    	<option value="">顶级分类</option>
                    	[#list rootCategory as category]
                    	<option value="${category.id}">${category.name}</option>
                    	[/#list]
                    </select>
                    <select onchange="get_root_child(this)" id="second_cate" cate="two" style="display:none;">
                    	
                    </select>
                   	<div style="display:inline;">(已选中：<span id="select_value"></span>)</div>
				</td>
			</tr>
			<tr>
				<th>
					品牌搜索:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" id="selectedbrandvalue" class="text" maxlength="200" title="搜索结果并入品牌筛选中" />
						<input type="button" id="selectedbrand" class="button" value="查询" />
						<input type="button" id="selectedAllbrand" class="button" value="全部" />
						<input type="button" id="selectedCleanbrand" class="button" value="清除" />
					</span>
				</td>
			</tr>
			<tr class="brands">
				<th>
					${message("ProductCategory.brands")}:
				</th>
				<td></td>
			</tr>
			<tr>
				<th>
					缩例图<120*120>:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="image" class="text" maxlength="200" title="${message("admin.product.imageTitle")}" />
						<input type="button" id="browserButton" class="button" value="${message("admin.browser.select")}" />
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("Product.tags")}:
				</th>
				<td>
					[#list tags as tag]
						<label>
							<input type="checkbox" name="tagIds" value="${tag.id}" />${tag.name}
						</label>
					[/#list]
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.seoTitle")}:
				</th>
				<td>
					<input type="text" name="seoTitle" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.seoKeywords")}:
				</th>
				<td>
					<input type="text" name="seoKeywords" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.seoDescription")}:
				</th>
				<td>
					<input type="text" name="seoDescription" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="order" class="text" maxlength="9" />
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