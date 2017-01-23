<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.productCategory.edit")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
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
        var judge = "true";
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $browserButton = $("#browserButton");
            var $browserButton1 = $("#browserButton1");
            var $selectedbrandvalue = $("#selectedbrandvalue");
            var $selectedbrand = $("#selectedbrand");
            var $selectedAllbrand = $("#selectedAllbrand");
            var $selectedCleanbrand = $("#selectedCleanbrand");

        [@flash_message /]
            $browserButton.browser();
            $browserButton1.browser();
            function queryLoadBrand(brandName, checked) {
                $.ajax({
                    url: "${base}/admin/brand/search.jhtml",
                    type: "POST",
                    data: {name: brandName},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.length > 1) {
                            checked = "";
                        }
                        var $brandIds = $("input[name='brandIds']");
                        var $brandstd = $(".brands td");
                        for (var i = 0; i < message.length; i++) {
                            var flag = true;
                            $brandIds.each(function () {
                                var $this = $(this);
                                if ($this.val() == message[i].id) {
                                    flag = false;
                                }
                            });
                            if (flag) {
                                $brandstd.append("<label><input type='checkbox' name='brandIds' value='" + message[i].id + "' " + checked + " />" + message[i].name + "</label>");
                            }
                        }
                    }
                });
            }

            $selectedbrand.click(function () {
                if ($selectedbrandvalue.val().replace(/\s/g, "") == "") {
                    return;
                } else {
                    queryLoadBrand($selectedbrandvalue.val(), "checked='checked'");
                }
            });
            $selectedAllbrand.click(function () {
                queryLoadBrand("", "");
            });
            $selectedCleanbrand.click(function () {
                var $label = $(".brands td label");
                $label.each(function () {
                    var $this = $(this);
                    if (!$this.find("input")[0].checked) {
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
        function edit_cate(obj) {
            if ($(obj).parent().css("display") != "hidden") {
                judge = "false";
                $(obj).parent().hide();
                $("#edit_cate_se").show();
                get_root_child($("#root_cate"))
            }
        }
        function return_edit(obj) {
            judge = "true";
            if ($("#edit_cate_se").css("display") != "hidden") {
                $("#edit_cate_se").hide();
                var _parentid = "";

            [#if productCategory.parent??&&productCategory.parent?has_content]
                _parentid = "${productCategory.parent.id}";
            [/#if]

                $("#root_cate").attr("name", "");
                $("#second_cate").attr("name", "");
                $("#default_value").attr("name", "parentId");
                $("[name='parentId']").val(_parentid);
                $("#edit_warp").show();
                $("#second_cate").hide();
            }
        }

        function get_root_child(obj) {
            $.ajax({
                url: "${base}/common/child_category.jhtml",
                type: "get",
                data: {parentId: $(obj).val()},
                dataType: "json",
                success: function (map) {
                    var html = "<option value=''>--请选择--</option>";
                    $.each(map, function (id, name) {
                        html += "<option value=" + id + ">" + name + "</option>";
                    });

                    if ($(obj).attr("cate") == "one") {
                        if ($("#root_cate").val() == "") {
                            if (judge == "true") {
                                $("#root_cate").attr("name", "");
                                $("#default_value").attr("name", "parentId");
                            } else {
                                $("#root_cate").attr("name", "parentId");
                                $("#default_value").attr("name", "");
                            }
                            $("#second_cate").attr("name", "");
                        } else {
                            $("#root_cate").attr("name", "parentId");
                            $("#second_cate").attr("name", "");
                            $("#default_value").attr("name", "");
                        }
                        $("#select_value").text($(obj).find("option:selected").text());
                        $("#second_cate").show();
                        $("#second_cate").html(html);
                    } else if ($(obj).attr("cate") == "two") {
                        if ($("#second_cate").val() == "") {
                            $("#select_value").text($("#root_cate").find("option:selected").text());
                            $("#second_cate").val($("#root_cate").val());
                            $("#root_cate").attr("name", "parentId");
                            $("#second_cate").attr("name", "");
                            $("#default_value").attr("name", "");
                        } else {
                            $("#select_value").text($(obj).find("option:selected").text());
                            $("#root_cate").attr("name", "");
                            $("#second_cate").attr("name", "parentId");
                            $("#root_cate").attr("name", "");
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.productCategory.edit")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="id" value="${productCategory.id}"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("ProductCategory.name")}:
            </th>
            <td>
                <input type="text" id="name" name="name" class="text" value="${productCategory.name}" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ProductCategory.parent")}:
            </th>
            <td>
            [#if children??&&children?has_content]
                <input type="hidden" name="parentId"
                       value="[#if productCategory.parent??]${productCategory.parent.id}[/#if]"/>
            ${(productCategory.parent.name)!}
            [#else]
            [#-- <select name="parentId">--]
            [#--<option value="">${message("admin.productCategory.root")}</option>--]
            [#--[#list productCategoryTree as category]--]
            [#--[#if category != productCategory && !children?seq_contains(category)]--]
            [#--<option value="${category.id}"[#if category == productCategory.parent]--]
            [#--selected="selected"[/#if]>--]
            [#--[#if category.grade != 0]--]
            [#--[#list 1..category.grade as i]--]
            [#--&nbsp;&nbsp;--]
            [#--[/#list]--]
            [#--[/#if]--]
            [#--${category.name}--]
            [#--</option>--]
            [#--[/#if]--]
            [#--[/#list]--]
            [#--</select> --]
                <div style="display:inline-block;" id="edit_warp">${(productCategory.parent.name)!}
                    <input type="hidden" id="default_value" name="parentId" value="${(productCategory.parent.id)!}">
                    [<a href="javascript:;" onclick="edit_cate(this)">修改</a>]
                </div>
                <input type="hidden" id="default_value" ad="">
                <div style="display:none;" id="edit_cate_se">
                    <select name="" onchange="get_root_child(this)" id="root_cate" cate="one" flag="true">
                        <option value="">顶级分类</option>
                        [#list rootCategory as category]
                            <option value="${category.id}">${category.name}</option>
                        [/#list]

                    </select>
                    <select name="" onchange="get_root_child(this)" id="second_cate" cate="two"
                            style="display:none;">
                    </select>
                    <div style="display:inline;">(已选中：<span id="select_value"></span>)</div>
                    [<a href="javascript:;" onclick="return_edit(this)">返回</a>]
                </div>
            [/#if]
            </td>
        </tr>
        <tr>
            <th>
                品牌搜索:
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" id="selectedbrandvalue" class="text" maxlength="200" title="搜索结果并入品牌筛选中"/>
						<input type="button" id="selectedbrand" class="button" value="查询"/>
						<input type="button" id="selectedAllbrand" class="button" value="全部"/>
						<input type="button" id="selectedCleanbrand" class="button" value="清除"/>
					</span>
            </td>
        </tr>
        <tr class="brands">
            <th>
            ${message("ProductCategory.brands")}:
            </th>
            <td>
            [#list productCategory.brands as brand]
                <label>
                    <input type="checkbox" name="brandIds" value="${brand.id}" checked="checked"/>${brand.name}
                </label>
            [/#list]
            </td>
        </tr>
        <tr>
            <th>
                图标:
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" name="image" class="text" value="${productCategory.image}" maxlength="200"
                               title="${message("admin.product.imageTitle")}"/>
						<input type="button" id="browserButton" class="button"
                               value="${message("admin.browser.select")}"/>
                    [#if productCategory.image??]
                        <a href="${productCategory.image}" target="_blank">${message("admin.common.view")}</a>
                    [/#if]
					</span>
            </td>
        </tr>
        <tr>
            <th>
                缩例图:
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" name="thumbnail" class="text" value="${productCategory.thumbnail}"
                               maxlength="200"
                               title="${message("admin.product.imageTitle")}"/>
						<input type="button" id="browserButton1" class="button"
                               value="${message("admin.browser.select")}"/>
                    [#if productCategory.thumbnail??]
                        <a href="${productCategory.thumbnail}" target="_blank">${message("admin.common.view")}</a>
                    [/#if]
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
                    <input type="checkbox" name="tagIds" value="${tag.id}"[#if productCategory.tags?seq_contains(tag)]
                           checked="checked"[/#if]/>${tag.name}
                </label>
            [/#list]
            </td>
        </tr>
        <tr>
            <th>
            ${message("ProductCategory.seoTitle")}:
            </th>
            <td>
                <input type="text" name="seoTitle" class="text" value="${productCategory.seoTitle}" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ProductCategory.seoKeywords")}:
            </th>
            <td>
                <input type="text" name="seoKeywords" class="text" value="${productCategory.seoKeywords}"
                       maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ProductCategory.seoDescription")}:
            </th>
            <td>
                <input type="text" name="seoDescription" class="text" value="${productCategory.seoDescription}"
                       maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="order" class="text" value="${productCategory.order}" maxlength="9"/>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="location.href='list.jhtml'"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>