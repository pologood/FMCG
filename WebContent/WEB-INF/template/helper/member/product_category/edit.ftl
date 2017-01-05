<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
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
	<link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet" />
	<link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet" />


	<script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
	<script src="${base}/resources/helper/js/amazeui.min.js"></script>
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
			$browserButton.browser();
			[@flash_message /]

			function queryLoadBrand(brandName, checked) {
				$.ajax({
					url: "${base}/b2b/brand/search.jhtml",
					type: "GET",
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
				},
				messages:{
					name:"${message("admin.validate.required")}"
				}
                ,
                submitHandler: function (form) {
                    var _pass = true;
                    $("input[name='grade']").each(function () {
                        if ($(this).val()>0 && $(this).attr("checked") == "checked" && $("select[name='parentId']").val() == "") {
                            $.message("error", "请选择上级！");
                            _pass = false;
                            return;
                        }
                    });
                    if (!_pass) {
                        return false;
                    }
                    // addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                    form.submit();
                }
			});

            $("input[name='grade']").change(function () {
                $("input[name='grade']").each(function () {
                    if ($(this).val() ==0) {
                        if ($(this).attr("checked") == "checked") {
                            $("#parentTr").hide();
                        } else {
                            $("#parentTr").show();
                        }
                    }
                });
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
						<img class="js-app_logo app-img" src="${base}/resources/helper/images/upload.png"/>
						<dl class="app-info">
							<dt class="app-title" id="app_name">商品分类</dt>
							<dd class="app-status" id="app_add_status">
							</dd>
							<dd class="app-intro" id="app_desc">编辑我发布的商品分类。</dd>
						</dl>
					</div>
					<ul class="links" id="mod_menus">
						<li><a class="on" hideFocus="" href="${base}/helper/member/product_category/list.jhtml">商品分类</a></li>
					</ul>

				</div>

				<form id="inputForm" action="update.jhtml" method="post">
					<input type="hidden" name="id" value="${productCategory.id}" />
					<table class="input">
						<tr>
							<th>
								<span class="requiredField">*</span>${message("ProductCategory.name")}:
							</th>
							<td>
								<input type="text" id="name" name="name" class="text" value="${productCategory.name}" maxlength="20" />
							</td>
						</tr>
						<tr>
							<th>
								缩例图:
							</th>
							<td>
								<span class="fieldSet">
									<input type="text" name="image" class="text" value="${productCategory.image}" maxlength="200" title="${message("admin.product.imageTitle")}" readonly="true"/>
									<input type="button" id="browserButton" class="button" value="${message("admin.browser.select")}" />
									[#if productCategory.image??]
									<a href="${productCategory.image}" target="_blank">${message("admin.common.view")}</a>
									[/#if]
								</span>
							</td>
						</tr>
						<tr>
							<th>
								${message("ProductCategory.seoTitle")}:
							</th>
							<td>
								<input type="text" name="seoTitle" class="text" value="${productCategory.seoTitle}" maxlength="200" />
							</td>
						</tr>
						<tr>
							<th>
								${message("ProductCategory.seoKeywords")}:
							</th>
							<td>
								<input type="text" name="seoKeywords" class="text" value="${productCategory.seoKeywords}" maxlength="200" />
							</td>
						</tr>
						<tr>
							<th>
								${message("ProductCategory.seoDescription")}:
							</th>
							<td>
								<input type="text" name="seoDescription" class="text" value="${productCategory.seoDescription}" maxlength="200" />
							</td>
						</tr>
						<tr>
							<th>
								${message("admin.common.order")}:
							</th>
							<td>
								<input type="text" name="order" class="text" value="${productCategory.order}" maxlength="9" />
							</td>
						</tr>
                        <tr>
                            <th>
                                层级:
                            </th>
                            <td>
                                <input type="radio" name="grade"  value="0" [#if productCategory.grade==0]checked="checked"[/#if] /> 一级
                                <input type="radio" name="grade"  value="1" [#if productCategory.grade==1]checked="checked"[/#if] /> 二级
                            </td>
                        </tr>
					[#--所属上级--]
                        <tr id="parentTr" [#if productCategory.grade==0]style="display: none"[/#if] >
                            <th>
                                <span class="requiredField">*</span>所属上级：
                            </th>
                            <td>
                                <select name="parentId">
                                    <option value=""
                                            selected="selected"
                                    >请选择
                                    </option>
								[#list productCategoryTree as parent]
									[#if parent.grade==0&&parent.id!=productCategory.id]
                                        <option value="${parent.id}"
												[#if productCategory.parent??&&productCategory.parent.id==parent.id] selected="selected"[/#if]
										>${parent.name}</option>
									[/#if]
								[/#list]
                                </select>
                                &nbsp;&nbsp;
                            </td>
                        </tr>
						<tr>
							<th>
								&nbsp;
							</th>
							<td>
							[@helperRole url="helper/member/product_category/list.jhtml" type="update"]
								[#if helperRole.retOper!="0"]
                                    <input type="submit" class="button" value="${message("admin.common.submit")}" />
								[/#if]
							[/@helperRole]
								<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='list.jhtml'" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	[#include "/helper/include/footer.ftl" /]
</body>
</html>
