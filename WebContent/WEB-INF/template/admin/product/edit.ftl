<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.product.edit")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <style type="text/css">
        .specificationSelect {
            height: 100px;
            padding: 5px;
            overflow-y: scroll;
            border: 1px solid #cccccc;
        }

        .specificationSelect li {
            float: left;
            min-width: 150px;
            _width: 200px;
        }

        .product_brand label {
            width: 150px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $productCategoryId = $("#productCategoryId");
            var $isMemberPrice = $("#isMemberPrice");
            var $memberPriceTr = $("#memberPriceTr");
            var $memberPrice = $("#memberPriceTr input");
            var $browserButton = $("#browserButton");
            var $productImageTable = $("#productImageTable");
            var $addProductImage = $("#addProductImage");
            var $deleteProductImage = $("a.deleteProductImage");
            var $parameterTable = $("#parameterTable");
            var $attributeTable = $("#attributeTable");
            var $specificationIds = $("#specificationSelect :checkbox");
            var $specificationProductTable = $("#specificationProductTable");
            var $addSpecificationProduct = $("#addSpecificationProduct");
            var $deleteSpecificationProduct = $("a.deleteSpecificationProduct");
            var productImageIndex = ${(product.productImages?size)!"0"};
            var previousProductCategoryId = "${product.productCategory.id}";

            var $addProductBonus = $("#addProductBonus");
            var $productBonusTable = $("#productBonusTable");
            var $deleteProductBonus = $("a.deleteProductBonus");
            var $addProductPackagUnit = $("#addProductPackagUnit");
            var $productPackagUnitTable = $("#productPackagUnitTable");


            var $selectedbrandvalue = $("#selectedbrandvalue");
            var $selectedbrand = $("#selectedbrand");
            var $selectedAllbrand = $("#selectedAllbrand");
            var $selectedCleanbrand = $("#selectedCleanbrand");

            var productBonusIndex = ${(product.bonuses?size)!"0"};
            var productPackagUnitIndex =${(product.packagUnits?size)!"0"};
        [@flash_message /]

            $browserButton.browser();

            // 会员价
            $isMemberPrice.change(function () {
                alert($(this).prop("checked"))
                if ($(this).prop("checked")) {
                    $memberPriceTr.show();
                    $memberPrice.prop("disabled", false);
                } else {
                    $memberPriceTr.hide();
                    $memberPrice.prop("disabled", true);
                }
            });

            // 增加商品图片
            $addProductImage.click(function () {
            [@compress single_line = true]
                var trHtml = '<tr><td><input type = "file" name = "productImages[' + productImageIndex + '].file" class = "productImageFile" \/><\/td><td><input type = "text" name = "productImages[' + productImageIndex + '].title" class = "text" maxlength = "200" \/><\/td><td><input type = "text" name = "productImages[' + productImageIndex + '].order" class = "text productImageOrder" maxlength = "9" style = "width: 50px;" \/><\/td><td><a href = "javascript:;" class = "deleteProductImage" > [${message("admin.common.delete")}] <\/a><\/td><\/tr>';
            [/@compress]
                $productImageTable.append(trHtml);
                productImageIndex++;
            });

            // 删除商品图片
            $deleteProductImage.live("click", function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $this.closest("tr").remove();
                    }
                });
            });

            // 修改商品分类
            $productCategoryId.change(function () {
                var hasValue = false;
                $parameterTable.add($attributeTable).find(":input").each(function () {
                    if ($.trim($(this).val()) != "") {
                        hasValue = true;
                        return false;
                    }
                });
                if (hasValue) {
                    $.dialog({
                        type: "warn",
                        content: "${message("admin.product.productCategoryChangeConfirm")}",
                        width: 450,
                        onOk: function () {
                            loadParameter();
                            loadAttribute();
                            previousProductCategoryId = $productCategoryId.val();
                        },
                        onCancel: function () {
                            $productCategoryId.val(previousProductCategoryId);
                        }
                    });
                } else {
                    loadParameter();
                    loadAttribute();
                    previousProductCategoryId = $productCategoryId.val();
                }
            });

            // 加载参数
            function loadParameter() {
                $.ajax({
                    url: "parameter_groups.jhtml",
                    type: "GET",
                    data: {id: $productCategoryId.val()},
                    dataType: "json",
                    beforeSend: function () {
                        $parameterTable.empty();
                    },
                    success: function (data) {
                        var trHtml = "";
                        $.each(data, function (i, parameterGroup) {
                            trHtml += '<tr><td style="text-align: right;"><strong>' + parameterGroup.name + ':<\/strong><\/td><td>&nbsp;<\/td><\/tr>';
                            $.each(parameterGroup.parameters, function (i, parameter) {
                            [@compress single_line = true]
                                trHtml += '<tr><th>' + parameter.name + ':<\/th><td><input type="text" name="parameter_' + parameter.id + '" class="text" maxlength="200" \/><\/td><\/tr>';
                            [/@compress]
                            });
                        });
                        $parameterTable.append(trHtml);
                    }
                });
            }

            // 加载属性
            function loadAttribute() {
                $.ajax({
                    url: "attributes.jhtml",
                    type: "GET",
                    data: {id: $productCategoryId.val()},
                    dataType: "json",
                    beforeSend: function () {
                        $attributeTable.empty();
                    },
                    success: function (data) {
                        var trHtml = "";
                        $.each(data, function (i, attribute) {
                            var optionHtml = '<option value="">${message("admin.common.choose")}<\/option>';
                            $.each(attribute.options, function (j, option) {
                                optionHtml += '<option value="' + option + '">' + option + '<\/option>';
                            });
                        [@compress single_line = true]
                            trHtml += '<tr><th> ' + attribute.name + ':<\/th><td><select name="attribute_' + attribute.id + '">' + optionHtml + '<\/select><\/td><\/tr>';
                        [/@compress]
                        });
                        $attributeTable.append(trHtml);
                    }
                });
            }

            // 修改商品规格
            $specificationIds.click(function () {
                if ($specificationIds.filter(":checked").size() == 0) {
                    $specificationProductTable.find("tr:gt(1)").remove();
                }
                var $this = $(this);
                if ($this.prop("checked")) {
                    $specificationProductTable.find("td.specification_" + $this.val()).show().find("select").prop("disabled", false);
                } else {
                    $specificationProductTable.find("td.specification_" + $this.val()).hide().find("select").prop("disabled", true);
                }
            });

            // 增加规格商品
            $addSpecificationProduct.click(function () {
                if ($specificationIds.filter(":checked").size() == 0) {
                    $.message("warn", "${message("admin.product.specificationRequired")}");
                    return false;
                }
                if ($specificationProductTable.find("tr:gt(1)").size() == 0) {
                    $tr = $specificationProductTable.find("tr:eq(1)").clone().show().appendTo($specificationProductTable);
                    $tr.find("td:first").text("${message("admin.product.currentSpecification")}");
                    $tr.find("td:last").text("-");
                } else {
                    $specificationProductTable.find("tr:eq(1)").clone().show().appendTo($specificationProductTable);
                }
            });

            // 删除规格商品
            $deleteSpecificationProduct.live("click", function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $this.closest("tr").remove();
                    }
                });
            });

            $.validator.addClassRules({
                memberPrice: {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                productImageFile: {
                    required: true,
                    extension: "${setting.uploadImageExtension}"
                },
                productImageOrder: {
                    digits: true
                },
                perexpression: {
                    pattern: /^\d+(.{1}\d+)?$|^\d+(.{1}\d+)?%$/
                },
                valueRequired: {
                    required: true
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    productCategoryId: "required",
                    name: "required",
                    sn: {
                        pattern: /^[0-9a-zA-Z_-]+$/,
                        remote: {
                            url: "check_sn.jhtml?previousSn=${product.sn}",
                            cache: false
                        }
                    },
                    price: {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    wholePrice: {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    cost: {
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    marketPrice: {
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    weight: "digits",
                    stock: "digits",
                    point: "digits"
                },
                messages: {
                    sn: {
                        pattern: "${message("admin.validate.illegal")}",
                        remote: "${message("admin.validate.exist")}"
                    }
                },
                submitHandler: function (form) {
                    addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                    form.submit();
                }
            });
            // 增加商品提成
            $addProductBonus.click(function () {
            [@compress single_line = true]
                var trHtml = '<tr><td><select id = "roleTypeId" name = "bonuses[' + productBonusIndex + '].roleType" ><option value = "salesman">业务员<\/option><\/select><\/td><td><input type = "text" name = "bonuses[' + productBonusIndex + '].expression" class = "text perexpression valueRequired" maxlength = "200" title = "提成计算：数字(例如:0.4 固定金额)\/百分数(例如: 4% 销售价百分比)"\/><\/td><td><a href = "javascript:;" class = "deleteProductBonus" > [${message("admin.common.delete")}] <\/a><\/td><\/tr> ';
            [/@compress]
                $productBonusTable.append(trHtml);
                productBonusIndex++;
            });
            // 增加商品包装单位
            $addProductPackagUnit.click(function () {
            [@compress single_line = true]
                var trHtml = '<tr><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].name" class= "text valueRequired" maxlength = "200"\/><\/td><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].coefficient" class= "text memberPrice valueRequired" maxlength = "200" \/><\/td><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].wholePrice" class= "text memberPrice" maxlength = "200" \/><\/td><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].price" class= "text memberPrice" maxlength = "200" \/><\/td><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].barcode" class="text" maxlength="200" \/><\/td><td><a href = "javascript:;" class = "deleteProductBonus" > [${message("admin.common.delete")}] <\/a> <\/td><\/tr> ';
            [/@compress]
                $productPackagUnitTable.append(trHtml);
                productPackagUnitIndex++;
            });
            // 删除商品提成
            $deleteProductBonus.live("click", function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $this.closest("tr").remove();
                    }
                });
            });


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
                        console.log(message.length);
                        var $brandIds = $("input[name='brandId']");
                        var $brandstd = $(".product_brand td");
                        for (var i = 0; i < message.length; i++) {
                            var flag = true;
                            $brandIds.each(function () {
                                var $this = $(this);
                                if ($this.val() == message[i].id) {
                                    flag = false;
                                }
                            });

                            console.log(flag, message[i].id);
                            if (flag) {
                                $brandstd.append("<label><input type='radio' name='brandId' value='" + message[i].id + "' " + checked + " />" + message[i].name + "</label>");
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
                var $label = $(".product_brand td label");
                $label.each(function () {
                    var $this = $(this);
                    if (!$this.find("input")[0].checked) {
                        $this.remove();
                    }
                });
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.product.edit")}
</div>
<form id="inputForm" action="update.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${product.id}"/>
    <ul id="tab" class="tab">
        <li>
            <input type="button" value="${message("admin.product.base")}"/>
        </li>
        <li>
            <input type="button" value="${message("admin.product.introduction")}"/>
        </li>
        <li>
            <input type="button" value="${message("admin.product.productImage")}"/>
        </li>
        <li>
            <input type="button" value="${message("admin.product.parameter")}"/>
        </li>
        <li>
            <input type="button" value="${message("admin.product.attribute")}"/>
        </li>
        <li>
            <input type="button" value="${message("admin.product.specification")}"/>
        </li>
        <li>
            <input type="button" value="商品提成"/>
        </li>
        <li>
            <input type="button" value="包装单位"/>
        </li>
    </ul>
    <table class="input tabContent">
    [#if product.specifications?has_content]
        <tr>
            <th>
            ${message("Product.specifications")}:
            </th>
            <td>
                [#list product.specificationValues as specificationValue]
							${specificationValue.name}
						[/#list]
            </td>
        </tr>
    [/#if]
    [#if product.validPromotions?has_content]
        <tr>
            <th>
            ${message("Product.promotions")}:
            </th>
            <td>
                [#list product.validPromotions as promotion]
                    <p>
                    ${promotion.name}
                        [#if promotion.beginDate?? || promotion.endDate??]
                            [${promotion.beginDate} ~ ${promotion.endDate}]
                        [/#if]
                    </p>
                [/#list]
            </td>
        </tr>
    [/#if]
        <tr>
            <th>
            ${message("Product.productCategory")}:
            </th>
            <td>
                <select id="productCategoryId" name="productCategoryId">
                [#list productCategoryTree as productCategory]
                    <option value="${productCategory.id}"[#if productCategory == product.productCategory]
                            selected="selected"[/#if]>
                        [#if productCategory.grade != 0]
                            [#list 1..productCategory.grade as i]
                                &nbsp;&nbsp;
                            [/#list]
                        [/#if]
                    ${productCategory.name}
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Product.name")}:
            </th>
            <td>
                <input type="text" name="name" class="text" readOnly="readOnly" value="${product.name}"
                       maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.sn")}:
            </th>
            <td>
                <input type="text" name="sn" class="text" disabled="disabled" value="${product.sn}" maxlength="100"
                       title="${message("admin.product.snTitle")}"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Product.price")}:
            </th>
            <td>
                <input type="text" name="price" class="text" disabled="disabled" value="${product.price}"
                       maxlength="16"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>批发价:
            </th>
            <td>
                <input type="text" name="wholePrice" disabled="disabled" class="text" value="${product.wholePrice}"
                       maxlength="16"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.memberPrice")}:
            </th>
            <td>
                <label>
                    <input type="checkbox" id="isMemberPrice" onclick="return false;" name="isMemberPrice"
                           value="true"[#if product.memberPrice?has_content]
                           checked="checked"[/#if]/>${message("admin.product.isMemberPrice")}
                </label>
            </td>
        </tr>
        <tr id="memberPriceTr"[#if !product.memberPrice?has_content] class="hidden"[/#if]>
            <th>
                &nbsp;
            </th>
            <td>
            [#list memberRanks as memberRank]
            ${memberRank.name}: <input type="text" disabled="disabled" name="memberPrice_${memberRank.id}"
                                       class="text memberPrice" value="${product.memberPrice.get(memberRank)}"
                                       maxlength="16"
                                       style="width: 60px; margin-right: 6px;"[#if !product.memberPrice?has_content]
                                       disabled="disabled"[/#if]/>
            [/#list]
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.cost")}:
            </th>
            <td>
                <input type="text" name="cost" class="text" disabled="disabled" value="${product.cost}" maxlength="16"
                       title="${message("admin.product.costTitle")}"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.marketPrice")}:
            </th>
            <td>
                <input type="text" name="marketPrice" class="text" disabled="disabled" value="${product.marketPrice}"
                       maxlength="16" title="${message("admin.product.marketPriceTitle")}"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.image")}:
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" name="image" class="text" value="${product.image}" maxlength="200"
                               title="${message("admin.product.imageTitle")}"/>
						<input type="button" id="browserButton" class="button" disabled="disabled"
                               value="${message("admin.browser.select")}"/>
                    [#if product.image??]
                        <a href="${product.image}" target="_blank">${message("admin.common.view")}</a>
                    [/#if]
					</span>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Product.unit")}:
            </th>
            <td>
                <input type="text" name="unit" class="text valueRequired" disabled="disabled" maxlength="200"
                       value="${product.unit}"/>
            </td>
        </tr>
        <tr>
            <th>
                最低起订单:
            </th>
            <td>
                <input type="text" name="minReserve" class="text" disabled="disabled" value="${product.minReserve}"
                       maxlength="9" title="最低起订单"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.weight")}:
            </th>
            <td>
                <input type="text" name="weight" class="text" disabled="disabled" value="${product.weight}"
                       maxlength="9" title="${message("admin.product.weightTitle")}"/>
            </td>
        </tr>
        <tr>
            <th>
                条形码:
            </th>
            <td>
                <input type="text" name="barcode" class="text" disabled="disabled" value="${product.barcode}"
                       maxlength="128" title="条形码"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.stock")}:
            </th>
            <td>
                <input type="text" name="stock" disabled="disabled" class="text" value="${product.stock}" maxlength="9"
                       title="${message("admin.product.stockTitle")}"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.stockMemo")}:
            </th>
            <td>
                <input type="text" disabled="disabled" name="stockMemo" class="text" value="${product.stockMemo}"
                       maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.point")}:
            </th>
            <td>
                <input type="text" disabled="disabled" name="point" class="text" value="${product.point}" maxlength="9"
                       title="${message("admin.product.pointTitle")}"/>
            </td>
        </tr>
    [#--<tr>--]
    [#--<th>--]
    [#--${message("Product.brand")}:--]
    [#--</th>--]
    [#--<td>--]
    [#--<select name="brandId">--]
    [#--<option value="">${message("admin.common.choose")}</option>--]
    [#--[#list brands as brand]--]
    [#--<option value="${brand.id}"[#if brand == product.brand] selected="selected"[/#if]>--]
    [#--${brand.name}--]
    [#--</option>--]
    [#--[/#list]--]
    [#--</select>--]
    [#--</td>--]
    [#--</tr>--]
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
        <tr class="product_brand">
            <th>
            ${message("ProductCategory.brands")}:
            </th>
            <td>
            [#if product.brand??&&product.brand?has_content]
                <label>
                    <input type="radio" name="brandId" value="${product.brand.id}"
                           checked="checked"/>${product.brand.name}
                </label>
            [/#if]
            </td>
        </tr>

        <tr>
            <th>
            ${message("Product.tags")}:
            </th>
            <td>
            [#list tags as tag]
                <label>
                    <input type="checkbox" name="tagIds" value="${tag.id}"[#if product.tags?seq_contains(tag)]
                           checked="checked"[/#if]/>${tag.name}
                </label>
            [/#list]
            </td>
        </tr>
        <tr>
            <th>
            ${message("admin.common.setting")}:
            </th>
            <td>
                <label>
                    <input type="checkbox" name="isMarketable" onclick="return false;"
                           value="true"[#if product.isMarketable]
                           checked="checked"[/#if]/>${message("Product.isMarketable")}
                    <input type="hidden" name="_isMarketable" value="false"/>
                </label>
                <label>
                    <input type="checkbox" name="isList" onclick="return false;" value="true"[#if product.isList]
                           checked="checked"[/#if]/>${message("Product.isList")}
                    <input type="hidden" name="_isList" value="false"/>
                </label>
                <label>
                    <input type="checkbox" name="isTop" onclick="return false;" value="true"[#if product.isTop]
                           checked="checked"[/#if]/>${message("Product.isTop")}
                    <input type="hidden" name="_isTop" value="false"/>
                </label>
                <label>
                    <input type="checkbox" name="isGift" onclick="return false;" value="true"[#if product.isGift]
                           checked="checked"[/#if]/>${message("Product.isGift")}
                    <input type="hidden" name="_isGift" value="false"/>
                </label>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.memo")}:
            </th>
            <td>
                <input type="text" name="memo"  class="text" value="${product.memo}" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.keyword")}:
            </th>
            <td>
                <input type="text" name="keyword" class="text" value="${product.keyword}" maxlength="200"
                       title="${message("admin.product.keywordTitle")}"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.seoTitle")}:
            </th>
            <td>
                <input type="text" name="seoTitle" class="text" value="${product.seoTitle}" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.seoKeywords")}:
            </th>
            <td>
                <input type="text" name="seoKeywords" class="text" value="${product.seoKeywords}" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Product.seoDescription")}:
            </th>
            <td>
                <input type="text" disabled="disabled" name="seoDescription" class="text"
                       value="${product.seoDescription}" maxlength="200"/>
            </td>
        </tr>
    </table>
    <table class="input tabContent">
        <tr>
            <td>
                <!--
					<textarea id="editor" name="introduction" class="editor"  style="width: 100%;">${product.introduction?html}</textarea>
				-->
                <textarea disabled="disabled" style="width: 100%;"
                          class="editor">${product.introduction?html}</textarea>
            </td>
        </tr>
    </table>
    <table id="productImageTable" class="input tabContent">
        <tr>
            <td colspan="4">
                <!--
					<a href="javascript:;" id="addProductImage"  class="button">${message("admin.product.addProductImage")}</a>
					-->
            </td>
        </tr>
        <tr class="title">
            <th>
            ${message("ProductImage.file")}
            </th>
            <th>
            ${message("ProductImage.title")}
            </th>
            <th>
            ${message("admin.common.order")}
            </th>
            <th>
            ${message("admin.common.delete")}
            </th>
        </tr>
    [#list product.productImages as productImage]
        <tr>
            <td>
                <input type="hidden" name="productImages[${productImage_index}].source" value="${productImage.source}"/>
                <input type="hidden" name="productImages[${productImage_index}].large" value="${productImage.large}"/>
                <input type="hidden" name="productImages[${productImage_index}].medium" value="${productImage.medium}"/>
                <input type="hidden" name="productImages[${productImage_index}].thumbnail"
                       value="${productImage.thumbnail}"/>
                <input type="file" disabled="disabled" name="productImages[${productImage_index}].file"
                       class="productImageFile ignore"/>
                <a href="${productImage.large}" target="_blank">${message("admin.common.view")}</a>
            </td>
            <td>
                <input type="text" name="productImages[${productImage_index}].title" disabled="disabled" class="text"
                       maxlength="200" value="${productImage.title}"/>
            </td>
            <td>
                <input type="text" name="productImages[${productImage_index}].order" disabled="disabled"
                       class="text productImageOrder" value="${productImage.order}" maxlength="9" style="width: 50px;"/>
            </td>
            <td>
                <!--
						<a href="javascript:;" disabled="disabled" class="deleteProductImage">[${message("admin.common.delete")}]</a>
						-->
            </td>
        </tr>
    [/#list]
    </table>
    <table id="parameterTable" class="input tabContent">
    [#list product.productCategory.parameterGroups as parameterGroup]
        <tr>
            <td style="text-align: right; padding-right: 10px;">
                <strong>${parameterGroup.name}:</strong>
            </td>
            <td>
                &nbsp;
            </td>
        </tr>
        [#list parameterGroup.parameters as parameter]
            <tr>
                <th>${parameter.name}:</th>
                <td>
                    <input type="text" name="parameter_${parameter.id}" disabled="disabled" class="text"
                           value="${product.parameterValue.get(parameter)}" maxlength="200"/>
                </td>
            </tr>
        [/#list]
    [/#list]
    </table>
    <table id="attributeTable" class="input tabContent">
    [#list product.productCategory.attributes as attribute]
        <tr>
            <th>${attribute.name}:</th>
            <td>
                <select name="attribute_${attribute.id}" disabled="disabled">
                    <option value="">${message("admin.common.choose")}</option>
                    [#list attribute.options as option]
                        <option value="${option}"[#if option == product.getAttributeValue(attribute)]
                                selected="selected"[/#if]>${option}</option>
                    [/#list]
                </select>
            </td>
        </tr>
    [/#list]
    </table>
    <table class="input tabContent">
        <tr class="title">
            <th>
            ${message("admin.product.selectSpecification")}:
            </th>
        </tr>
        <tr>
            <td>
                <div id="specificationSelect" class="specificationSelect">
                    <ul>
                    [#list specifications as specification]
                        <li>
                            <label>
                                <input type="checkbox" name="specificationIds" disabled="disabled"
                                       value="${specification.id}"[#if product.specifications?seq_contains(specification)]
                                       checked="checked"[/#if]/>${specification.name}
                                [#if specification.memo??]
                                    <span class="gray">[${specification.memo}]</span>
                                [/#if]
                            </label>
                        </li>
                    [/#list]
                    </ul>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <!--
					<a href="javascript:;" id="addSpecificationProduct" class="button">${message("admin.product.addSpecificationProduct")}</a>
				-->
            </td>
        </tr>
        <tr>
            <td>
                <table id="specificationProductTable" class="input">
                    <tr class="title">
                        <td width="60">
                            &nbsp;
                        </td>
                    [#list specifications as specification]
                        <td class="specification_${specification.id}[#if !product.specifications?seq_contains(specification)] hidden[/#if]">
                        ${specification.name}
                            [#if specification.memo??]
                                <span class="gray">[${specification.memo}]</span>
                            [/#if]
                        </td>
                    [/#list]
                        <td>
                        ${message("admin.common.handle")}
                        </td>
                    </tr>
                    <tr class="hidden">
                        <td>
                            &nbsp;
                        </td>
                    [#list specifications as specification]
                        <td class="specification_${specification.id}[#if !product.specifications?seq_contains(specification)] hidden[/#if]">
                            <select name="specification_${specification.id}"[#if !product.specifications?seq_contains(specification)]
                                    disabled="disabled"[/#if] disabled="disabled">
                                [#list specification.specificationValues as specificationValue]
                                    <option value="${specificationValue.id}">${specificationValue.name}</option>
                                [/#list]
                            </select>
                        </td>
                    [/#list]
                        <td>
                            <!--
								<a href="javascript:;" class="deleteSpecificationProduct">[${message("admin.common.delete")}]</a>
							-->
                        </td>
                    </tr>
                [#if product.specifications?has_content]
                    <tr>
                        <td>
                        ${message("admin.product.currentSpecification")}
                            <input type="hidden" name="specificationProductIds" value="${product.id}"/>
                        </td>
                        [#list specifications as specification]
                            <td class="specification_${specification.id}[#if !product.specifications?seq_contains(specification)] hidden[/#if]">
                                <select name="specification_${specification.id}"[#if !product.specifications?seq_contains(specification)]
                                        disabled="disabled"[/#if] disabled="disabled">
                                    [#list specification.specificationValues as specificationValue]
                                        <option value="${specificationValue.id}"[#if product.specificationValues?seq_contains(specificationValue)]
                                                selected="selected"[/#if]>${specificationValue.name}</option>
                                    [/#list]
                                </select>
                            </td>
                        [/#list]
                        <td>
                            -
                        </td>
                    </tr>
                [/#if]
                [#list product.siblings as specificationProduct]
                    <tr>
                        <td>
                            &nbsp;
                            <input type="hidden" name="specificationProductIds" value="${specificationProduct.id}"/>
                        </td>
                        [#list specifications as specification]
                            <td class="specification_${specification.id}[#if !specificationProduct.specifications?seq_contains(specification)] hidden[/#if]">
                                <select name="specification_${specification.id}"[#if !specificationProduct.specifications?seq_contains(specification)]
                                        disabled="disabled"[/#if] disabled="disabled">
                                    [#list specification.specificationValues as specificationValue]
                                        <option value="${specificationValue.id}"[#if specificationProduct.specificationValues?seq_contains(specificationValue)]
                                                selected="selected"[/#if]>${specificationValue.name}</option>
                                    [/#list]
                                </select>
                            </td>
                        [/#list]
                        <td>
                            <!--
									<a href="javascript:;" class="deleteSpecificationProduct">[${message("admin.common.delete")}]</a>
									<a href="edit.jhtml?id=${specificationProduct.id}">[${message("admin.common.edit")}]</a>
								-->
                        </td>
                    </tr>
                [/#list]
                </table>
            </td>
        </tr>
    </table>
    <table id="productBonusTable" class="input tabContent">
        <tr>
            <td colspan="4">
                <!--
                    <a href="javascript:;" id="addProductBonus" class="button">增加提成</a>
                -->
            </td>
        </tr>
        <tr class="title">
            <td>
                提成角色
            </td>
            <td>
                提成方式
            </td>
            <td>
                操作
            </td>
        </tr>
    [#list product.bonuses as bonuses]
        <tr>
            <td>
                <input type="hidden" name="bonuses[${bonuses_index}].id" class="text" maxlength="200"
                       value="${bonuses.id}"/>
                <select id="roleTypeId" name="bonuses[${bonuses_index}].roleType">
                    <option value="salesman" [#if bonuses.roleType==salesman]selected[/#if]>业务员</option>
                </select>
            </td>
            <td>
                <input type="text" name="bonuses[${bonuses_index}].expression" class="text perexpression valueRequired"
                       value="${bonuses.expression}" title="提成计算：数字(例如:0.4 固定金额)/百分数(例如: 4% 销售价百分比)"/>
            </td>
            <td>
                <!--
					<a href="javascript:;" class="deleteProductBonus">[${message("admin.common.delete")}]</a>
				-->
            </td>
        </tr>
    [/#list]
    </table>
    <table id="productPackagUnitTable" class="input tabContent">
        <tr>
            <td colspan="4">
                <!--
                    <a href="javascript:;" id="addProductPackagUnit" class="button">增加包装单位</a>
                -->
            </td>
        </tr>
        <tr class="title">
            <td>
                名称
            </td>
            <td>
                换算系数
            </td>
            <td>
                批发价
            </td>
            <td>
                销售价
            </td>
            <td>
                包装条码
            </td>
            <td>
                操作
            </td>
        </tr>
    [#list product.packagUnits as packagUnits]
        <tr>
            <td>
                <input type="hidden" name="packagUnits[${packagUnits_index}].id" class="text" maxlength="200"
                       value="${packagUnits.id}"/>
                <input type="text" disabled="disabled" name="packagUnits[${packagUnits_index}].name"
                       class="text valueRequired" maxlength="200" value="${packagUnits.name}"/>
            </td>
            <td>
                <input type="text" disabled="disabled" name="packagUnits[${packagUnits_index}].coefficient"
                       class="text memberPrice valueRequired" value="${packagUnits.coefficient}"/>
            </td>
            <td>
                <input type="text" disabled="disabled" name="packagUnits[${packagUnits_index}].wholePrice"
                       class="text memberPrice" value="${packagUnits.wholePrice}"/>
            </td>
            <td>
                <input type="text" disabled="disabled" name="packagUnits[${packagUnits_index}].price"
                       class="text memberPrice" value="${packagUnits.price}"/>
            </td>
            <td>
                <input type="text" disabled="disabled" name="packagUnits[${packagUnits_index}].barcode" class="text"
                       value="${packagUnits.barcode}"/>
            </td>
            <td>
                <!--
						<a href="javascript:;" class="deleteProductBonus">[${message("admin.common.delete")}]</a>
					-->
            </td>
        </tr>
    [/#list]
    </table>
    <table class="input">
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>