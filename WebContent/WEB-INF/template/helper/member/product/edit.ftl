<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]

    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.placeholder.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/laddSelect.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>

    <link href="${base}/resources/common/jcrop/css/jquery.Jcrop.css" rel="stylesheet" type="text/css"/>

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

        #addSpecTable {
            border: 0;
        }

        #addSpecTable td {
            border: 0;
        }

        .deleteIcon {
            width: 18px;
            height: 18px;
            position: absolute;
            top: 4px;
            left: 8px;
            background: url(../images/common_admin.gif) -40px -150px no-repeat;
        }
    </style>
    <script type="text/javascript">
        var branddataId = "";

        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $productCategoryId = $("#productCategoryId");
            var $isMemberPrice = $("#isMemberPrice");
            var $memberPriceTr = $("#memberPriceTr");
            var $memberPrice = $("#memberPriceTr input");
            var $productImageTable = $("#productImageTable");
            var $addProductImage = $("#addProductImage");
            var $deleteProductImage = $("a.deleteProductImage");
            var $parameterTable = $("#parameterTable");
            var $attributeTable = $("#attributeTable");
            var $specificationIds = $("[name='specificationIds']");
            var $specificationProductTable = $("#specificationProductTable");
            var $addSpecificationProduct = $("#addSpecificationProduct");
            var $deleteSpecificationProduct = $("a.deleteSpecificationProduct");
            var productImageIndex = ${(product.productImages?size)!"0"};
            var previousProductCategoryId = "${product.productCategory.id}";

            var $addProductPackagUnit = $("#addProductPackagUnit");
            var $productPackagUnitTable = $("#productPackagUnitTable");
            var $sns = $(".sns");
            var productPackagUnitIndex =${(product.packagUnits?size)!"0"};
            $('input, textarea').placeholder();
            //改变全名描述
            $(".changeInput").change(function () {
                $("#nameAddress").val("");
            });
        [@flash_message /]
            var settings = {
                isSubmit: false
            };

            // "类型"修改
            $('input[name="type"]').change(function () {
                var $this = $(this);
                if ($this.val() == "0") {
                    $("#pcTr").show();
                    $("#appTr").hide();
                } else {
                    $("#pcTr").hide();
                    $("#appTr").show();
                }
            });


            // 会员价
            $isMemberPrice.click(function () {
                if ($(this).prop("checked")) {
                    $memberPriceTr.show();
                    $memberPrice.prop("disabled", false);
                } else {
                    $memberPriceTr.hide();
                    $memberPrice.prop("disabled", true);
                }
            });

        [#if versionType==1]
            var $xbrand = $("#xbrand");
            var $addSeries = $("#addSeries");
            var id;

            $addSeries.click(function () {
                id = $("#txtBrand1").attr('phonetid');
                if ($xbrand.val() != null && $xbrand.val() != "") {
                    id = $xbrand.val();
                }
                $.ajax({
                    url: "${base}/helper/member/product/getSeries.jhtml",
                    type: "GET",
                    data: {id: id},
                    dataType: "json",
                    cache: false,
                    success: function (map) {
                        $("#" + map.id).remove();
                        $("#serieses").append("<span id=" + map.id + "><input type=\"hidden\" name=\"seriesIds\" value=\"" + map.id + "\">" + map.name + ";</span>");
                    }
                });
            });
            var $clearSeries = $("#clearSeries");
            $clearSeries.click(function () {
                $("#serieses").html("");
                $("#txtBrand1").val("");
                $("#txtBrand1").removeAttr("phonetic");
                $("#txtBrand1").removeAttr("phonetid");
                $("#xbrand").val("");
                $("span.fieldSet select").remove();
            });
        [/#if]

        [#if versionType==0]
            var $brandId = $("#brandId");
            // 品牌选择
            $brandId.change(
                    function () {
                        $.ajax({
                            url: "brand_series.jhtml",
                            type: "GET",
                            data: {brandId: $brandId.val()},
                            dataType: "json",
                            cache: false,
                            success: function (map) {
                                var opt = "<option value=\"\">--请选择--</option>";
                                var btrue = 0;
                                for (var key in map) {
                                    opt = opt + "<option value=" + key + ">" + map[key] + "</option>";
                                    btrue = 1;
                                }
                            }
                        });
                    });
        [/#if]

            $addProductImage.click(function () {
                var len = $('#productImageTable tr').length;
                if (len > 9) {
                    $.message("warn", "您不能在继续添加图片了");
                    return;
                }
            [@compress single_line = true]
                var trHtml = '<tr><td><img class="proImg" id = "img' + productImageIndex + '" src = "" width = "100px" height = "100px" \/><input type = "hidden" name = "productImages[' + productImageIndex + '].local" maxlength = "200" \/><input type = "button" id = "browserButton' + productImageIndex + '" class= "button" value = "选择" \/><\/td><td><input type = "text" name = "productImages[' + productImageIndex + '].title" class = "text" maxlength = "200" \/><\/td><td><input type = "text" name = "productImages[' + productImageIndex + '].order" class= "text productImageOrder" maxlength = "9" style = "width: 50px;" \/><\/td><td><a href = "javascript:;" class= "deleteProductImage" > [${message("shop.common.delete")}] <\/a><\/td><\/tr>';
            [/@compress]
                $productImageTable.append(trHtml);
                var $browserButton = $("#browserButton" + productImageIndex);
                settings.img = $("#img" + productImageIndex);
                $browserButton.browser(settings);
                productImageIndex++;
            });

            // 删除商品图片
            $deleteProductImage.live("click", function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("shop.dialog.deleteConfirm")}",
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
                        content: "${message("shop.product.productCategoryChangeConfirm")}",
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
                                trHtml += '<tr><th>' + parameter.name + ':<\/th><td><input type="text" name="parameter_' + parameter.id + '" class="text" maxlength="200"\/><\/td><\/tr>';
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
                            var optionHtml = '<option value="">${message("shop.common.choose")}<\/option>';
                            $.each(attribute.options, function (j, option) {
                                optionHtml += '<option value="' + option + '">' + option + '<\/option>';
                            });
                        [@compress single_line = true]
                            trHtml += '<tr><th>' + attribute.name + ':<\/th><td><select name="attribute_' + attribute.id + '">' + optionHtml + '<\/select><\/td><\/tr>';
                        [/@compress]
                        });
                        $attributeTable.append(trHtml);
                    }
                });
            }

            // 删除规格商品
            $deleteSpecificationProduct.live("click", function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("shop.dialog.deleteConfirm")}",
                    onOk: function () {
                        if ($this.closest("tr").prev().css("display") == "none" && $specificationProductTable.find("tr:gt(1)").size() > 0) {
                            var $tr = $this.closest("tr").next().find("td:first");
                            var $input = $this.closest("tr").next().find("td:first input");
                            $tr.html("当前规格").append($input);
                        }
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
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    productCategoryId: "required",
                    name: "required",
                    unit: "required",
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
                    marketPrice: {
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    weight: "digits",
                    stock: {
                        required: true,
                        digits: true
                    },
                    point: "digits",
                    prices: {
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    stocks: "digits"
                },
                messages: {
                    sn: {
                        pattern: "${message("shop.validate.illegal")}",
                        remote: "${message("shop.validate.exist")}"
                    },
                    name: "${message("shop.validate.required")}",
                    price: {
                        required: "${message("shop.validate.required")}",
                        decimal: "金额格式错误"
                    },
                    unit: "${message("shop.validate.required")}",
                    prices: {
                        required: "${message("shop.validate.required")}",
                        decimal: "金额格式错误"
                    }
                },
                submitHandler: function (form) {

                    var _marketPrice = $("input[name='marketPrice']").val();

                    if ($("[name='specificationType']:checked").val() == "many") {
                        if ($specificationProductTable.find("tr").size() == 0) {
                            $.message("warn", "${message("shop.product.specificationProductRequired")}");
                            return false;
                        } else {
                            var validate = true, content = "";
                            if ($("#specification1Ids").prop("checked")) {
                                $.each($("[name='specification_1']:gt(0)"), function () {
                                    if ($(this).val().trim() == "") {
                                        validate = false;
                                        content = "商品规格【" + $("#specification1Title").val() + "】值不能为空";
                                    }
                                });
                                if (!validate) {
                                    $.message("error", content);
                                    return false;
                                }
                            }
                            if ($("#specification2Ids").prop("checked")) {
                                $.each($("[name='specification_2']:gt(0)"), function () {
                                    if ($(this).val().trim() == "") {
                                        validate = false;
                                        content = "商品规格【" + $("#specification2Title").val() + "】值不能为空"
                                    }
                                });
                                if (!validate) {
                                    $.message("error", content);
                                    return false;
                                }
                            }

                            if (!$("#specification1Ids").prop("checked") && !$("#specification2Ids").prop("checked")) {
                                $.message("error", "请至少选择一种规格或者型号");
                                return false;
                            }

                            $.each($("[name='prices']:gt(0)"), function () {
                                if ($(this).val().trim() == "") {
                                    validate = false;
                                    content = "商品规格【销售价】不能为空";
                                }
//                                console.log($(this).parent.find("td:eq(4)").val().trim());
//                                if (parseFloat($(this).val().trim()) > parseFloat($(this).parent.children("td:eq(4)").val().trim())) {
//                                    validate = false;
//                                    content = "商品规格【销售价】值不能大于市场价";
//                                }
                            });
                            if (!validate) {
                                $.message("error", content);
                                return false;
                            }
                            $.each($("[name='marketPrices']:gt(0)"), function () {
                                if ($(this).val().trim() == "") {
                                    validate = false;
                                    content = "商品规格【市场价】不能为空";
                                }
                            });
                            if (!validate) {
                                $.message("error", content);
                                return false;
                            }

                            $.each($("[name='stocks']:gt(0)"), function () {
                                if ($(this).val().trim() == "") {
                                    validate = false;
                                    content = "商品规格【库存】值不能为空";
                                }
                            });
                            if (!validate) {
                                $.message("error", content);
                                return false;
                            }

                            $.each($("[name='costs']:gt(0)"), function () {
                                if ($(this).val().trim() == "") {
                                    validate = false;
                                    content = "商品规格【成本价】不能为空";
                                }
                            });
                            if (!validate) {
                                $.message("error", content);
                                return false;
                            }
                        }
                    } else {
                        var _price = $("input[name='price']").val();
                        if (parseFloat(_price) > parseFloat(_marketPrice)) {
                            $.message("error", "商品规格【销售价】值不能大于市场价");
                            return false;
                        }
                    }
                    var hasImg = true;
                    $.each($(".proImg"), function () {
                        if ($(this).attr("src") == "") {
                            hasImg = false;
                        }
                    });
                    if ($(".proImg").size() == 0 || !hasImg) {
                        $.message("error", "请上传商品图片");
                        return false;
                    }
                    var isRepeats = false;
                    var parameters = new Array();
                    $specificationProductTable.find("tr:gt(1)").each(function () {
                        var parameter = $(this).children().eq(1).find("input").val();
                        parameters.push(parameter);
                    });
                    if (!isRepeats) {
                        $specificationProductTable.find("tr:eq(1)").find("input").prop("disabled", true);
                        addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                        form.submit();
                    }

                }
            });
            // 增加商品包装单位
            $addProductPackagUnit.click(function () {
            [@compress single_line = true]
                var trHtml = '<tr><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].name" class= "text" maxlength = "200"\/><\/td><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].coefficient"class= "text" maxlength = "200" \/><\/td><td><input type = "text" name = "packagUnits[' + productPackagUnitIndex + '].wholePrice" class = "text" maxlength = "200" \/><\/td><td><a href = "javascript:;" class= "deleteProductBonus" > [${message("admin.common.delete")}] <\/a><\/td><\/tr>';
            [/@compress]
                $productPackagUnitTable.append(trHtml);
                productPackagUnitIndex++;
            });

            for (var i = 0; i < productImageIndex; i++) {
                var $browserButton = $("#browserButton" + i);
                settings.img = $("#img" + i);
                $browserButton.browser(settings);
            }
            var prevBarcode, prevPrice, prevStock, prevCost, prevMarketPrice;
            if ($("[name='specificationType']").val() == "many") {
                prevPrice = "";
                prevStock = "";
            }
            //单规格、多规格按钮change事件
            $("[name='specificationType']").change(function () {
                var $this = $(this);
                if ($this.val() == "many") {
                    prevBarcode = $("#barcode").val();
                    prevPrice = $("#price").val();
                    prevStock = $("#stock").val();
                    prevCost = $("#cost").val();
                    prevMarketPrice = $("#marketPrice").val();
                    if ($("#price").val().trim() == "") $("#price").val(0);
                    if ($("#stock").val().trim() == "") $("#stock").val(0);
                    $("#productBarcode").hide();
                    $("#productPrice").hide();
                    $("#productCost").hide();
                    $("#productMarketPrice").hide();
                    $("#productMemberPrice").hide();
                    $("#productStock").hide();
                    $("#snTr").hide();
                    $("#memberPriceTr").hide();
                    $('#specificationRadioTr').show();
                } else {
                    $("#price").val(prevPrice);
                    $("#stock").val(prevStock);
                    $("#productBarcode").show();
                    $("#productPrice").show();
                    $("#productCost").show();
                    $("#productMarketPrice").show();
                    $("#productMemberPrice").show();
                    $("#productStock").show();
                    $("#snTr").show();
                    $('#specificationRadioTr').hide();
                }
            });
            $.each($specificationIds, function () {
                var $this = $(this);
                if ($this.prop("checked")) {
                    $("#specification" + $this.val() + "Title").show();
                    $(".specification_" + $this.val()).hide();
                    $("[name='specification_" + $this.val() + "']").prop("disabled", false);
                } else {
                    $("#specification" + $this.val() + "Title").hide();
                    $(".specification_" + $this.val()).show();
                    $("[name='specification_" + $this.val() + "']").prop("disabled", true);
                }
            });
            //规格复选框单击触发事件
            $specificationIds.click(function () {
//                $("#addSpecTr").hide();
                if ($specificationIds.filter(":checked").size() == 0) {
                    $specificationProductTable.find("tr:gt(1)").remove();
                }
                var $this = $(this);
                if ($this.prop("checked")) {
                    $("#specification" + $this.val() + "Title").show();
                    $(".specification_" + $this.val()).hide();
                    $("[name='specification_" + $this.val() + "']").prop("disabled", false);
                } else {
                    $("#specification" + $this.val() + "Title").hide();
                    $(".specification_" + $this.val()).show();
                    $("[name='specification_" + $this.val() + "']").prop("disabled", true);
                }
            });
            //添加规格
            $("#addSpec").click(function () {
                $("#specificationTitle").val("");
                $("#addSpecTr").show();
                $("#addSpecificationTitle").unbind("click").click(function () {
                    addSpecificationTitle("add");
                });
            });
            //修改规格
            $("#modifySpec").click(function () {
                if ($specificationIds.filter(":checked").size() == 0) {
                    $.message("error", "请选中一个商品规格");
                    return;
                }
                if ($specificationIds.filter(":checked").size() == 2) {
                    $.message("error", "不能同时修改两个商品规格");
                    return;
                }
                $("#addSpecTr").show();
                $("#specificationTitle").val($specificationIds.filter(":checked").next().text().trim());
                $("#addSpecificationTitle").unbind("click").click(function () {
                    addSpecificationTitle("modify");
                });
            });
            //添加或修改规格
            function addSpecificationTitle(type) {
                if (type == "add") {
                    var v = $("#specificationTitle").val().trim();
                    if (v == "") {
                        $.message("error", "请输入规格名称");
                        return;
                    }
                    if ($("#specification1Checkbox").css("display") == "none") {
                        $("#specification1Checkbox").show();
                        $("#specification1Name").text(v);
                        $("#specification1Title").val(v);
                    } else if ($("#specification2Checkbox").css("display") == "none") {
                        $("#specification2Checkbox").show();
                        $("#specification2Name").text(v);
                        $("#specification2Title").val(v);
                    } else {
                        $.message("warn", "您不能再继续添加规格了");
                    }
                    $("#addSpecTr").hide();
                }
                if (type == "modify") {
                    var vv = $("#specificationTitle").val();
                    var id = $("#specificationSelect :checkbox:checked").val();
                    $("#specification" + id + "Name").text(vv);
                    $("#specificationProductTable tr:first .specification_" + id).text(vv);
                    $("#specification" + id + "Title").val(vv);
                    $("#addSpecTr").hide();
                }
            }

            //添加规格商品
            $addSpecificationProduct.click(function () {
                if ($specificationIds.filter(":checked").size() == 0) {
                    $.message("warn", "${message("shop.product.specificationRequired")}");
                    return false;
                }
                var sn = $("#sn").val();
                if ($specificationProductTable.find("tr:gt(1)").size() == 0) {
                    var $tr = $specificationProductTable.find("tr:eq(1)").clone().show().appendTo($specificationProductTable);
                    //$tr.find("td:first").text("当前规格");
                } else {
                    //$sns.val("");
                    $specificationProductTable.find("tr").eq($specificationProductTable.find("tr").length - 1).clone().show().appendTo($specificationProductTable);
                    $specificationProductTable.find("tr:last").find("input[name='specificationProductIds']").remove();
                    $specificationProductTable.find("tr:last").find("input[class='specSn']").val("");
                }
            });

        });

        function changeSpec(obj) {
            $(obj).prev().text(obj.value);
        }

        //下拉输入框
        //阻止事件冒泡
        function stopEventBubble(event) {
            var e = event || window.event;

            if (e && e.stopPropagation) {
                e.stopPropagation();
            }
            else {
                e.cancelBubble = true;
            }
        }
        function searchBrand(obj, oid) {
            var $this = $(obj).val();
            var $phoneticLen, $strLen;
            var $areaDl = $("#" + oid);
            var $true = true;
            $areaDl.hide();
            $areaDl.children("dd").hide();
            $areaDl.children("dt").hide();
            for (var i = 0; i < $areaDl.children("dd").length; i++) {
                $phoneticLen = $areaDl.children("dd").eq(i).attr("phonetic").substring(0, $this.length);
                $strLen = $areaDl.children("dd").eq(i).text().substring(0, $this.length);
                if (($this == $phoneticLen || $this == $strLen) && $this != "") {
                    $areaDl.show();
                    $areaDl.children("dd").eq(i).show();
                }
            }
            if ($this == "") {
                $areaDl.show();
                $areaDl.children("dd").show();
            } else {
                $areaDl.children("dd").each(function () {
                    if ($(this).css("display") == "block") {
                        $true = false;
                        return false;
                    }
                });
                if ($true) {
                    $areaDl.show().children("dt").show();
                }
            }
        }
        $(function () {
            $(document).click(function () {
                $("#areaDl").hide();
            });
            $("#txtBrand1").click(function (evt) {
                stopEventBubble(evt);
            });
            $("#txtBrand1").keyup(function () {
                searchBrand(this, "areaDl");
            }).focus(function () {
                $("#areaDl").show();
                $("#areaDl dd").show();
                $("#areaDl dt").hide();
            });
            $("#areaDl dd").click(function () {
                $("#txtBrand1").val($(this).text());
                $("#txtBrand1").attr("phonetic", $(this).attr("phonetic"));
                $("#txtBrand1").attr("phonetid", $(this).attr("phonetid"));
                $("#xbrand").val("");

                if ($("#txtBrand1").attr("phonetid") != null && $("#txtBrand1").attr("phonetid") != "") {
                    branddataId = $("#txtBrand1").attr("phonetid");
                    var $xbrand = $("#xbrand");
                    $xbrand.laddSelect({
                        choose: "全部",
                        emptyValue: branddataId,
                        url: "${base}/helper/member/product/brandSeries.jhtml"
                    });
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/upload.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的商品</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查询发布我的宝贝，维护当前销售价及库存状态等。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="" hideFocus="" href="${base}/helper/member/product/isMarketableList.jhtml">我的商品</a>
                    </li>
                    <li><a class="on" hideFocus=""
                           href="${base}/helper/member/product/addProductCategory.jhtml">商品维护</a></li>
                </ul>

            </div>

            <form id="inputForm" action="${base}/helper/member/product/update.jhtml" method="post"
                  enctype="multipart/form-data">
                <input type="hidden" name="id" value="${product.id}"/>
                <input type="hidden" name="productCategoryId" value="${product.productCategory.id}"/>
                <input type="hidden" name="isMarketable" value="${product.isMarketable}"/>
                <input type="hidden" name="sn" value="${product.sn}"/>
                <div class="list" style="margin-top: 0px;">
                    <!-- 基本信息 -->
                    <table class="input tabContent">
                        <tr>
                            <th>商品类目:</th>
                            <td>
                            [#if product.productCategory.parent?has_content][#if product.productCategory.parent.parent?has_content]${product.productCategory.parent.parent.name}
                                /[/#if]${product.productCategory.parent.name}/[/#if]${product.productCategory.name}
                            </td>
                        </tr>
                    [#if product.productCategory.attributes?has_content]
                        <tr>
                            <th>商品属性</th>
                            <td>
                            [#--商品属性--]
                                <table id="attributeTable" class="input"
                                       style="border-top: 1px solid #dde9f5;table-layout: fixed;">
                                <tr>
                                    [#list product.productCategory.attributes as attribute]
                                        <td style="text-align: right;">${attribute.name}:</td>
                                        <td>
                                            <select name="attribute_${attribute.id}">
                                                <option value="">${message("shop.common.choose")}</option>
                                                [#list attribute.options as option]
                                                    <option value="${option}"[#if option == product.getAttributeValue(attribute)]
                                                            selected="selected"[/#if]>${option}</option>
                                                [/#list]
                                            </select>
                                        </td>
                                        [#if (attribute_index+1)%3==0]
                                        </tr>
                                            [#if attribute_has_next]
                                            <tr>
                                            [/#if]

                                        [/#if]
                                    [/#list]
                                    [#assign outCount=attributes?size%3][#--超出个数--]
                                    [#if outCount>0]
                                        [#assign addCount=3-outCount][#--补充个数--]
                                        [#list 1..addCount as t]
                                            <td></td>
                                            <td></td>[/#list]
                                    </tr>
                                    [/#if]
                                </table>
                            </td>
                        </tr>
                    [/#if]
                        <tr id="productBarcode" class="[#if product.specifications?has_content]hidden[/#if]">
                            <th>
                                条形码:
                            </th>
                            <td>
                                <input type="text" name="barcode" class="text" maxlength="100" title="条型码"
                                       value="${product.barcode}"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.productCategory")}:
                            </th>
                            <td>
                                <select id="productCategoryTenantId" name="productCategoryTenantId">
                                    <option value="">请选择</option>
                                [#list productCategoryTreeTenant as productCategoryTenant]
                                    <option value="${productCategoryTenant.id}"  [#if product.productCategoryTenant == productCategoryTenant]
                                            selected="selected"[/#if]>
                                        [#if productCategoryTenant.grade != 0]
                                            [#list 1..productCategoryTenant.grade as i]
                                                &nbsp;&nbsp;
                                            [/#list]
                                        [/#if]
                                    ${productCategoryTenant.name}
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
                                <input type="text" name="name" class="text" maxlength="200" value="${product.name}"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>${message("Product.unit")}:
                            </th>
                            <td>
                                <input type="text" name="unit" class="text" maxlength="200" value="${product.unit}"/>
                                <span>计量单位,例:盒、件、箱、瓶。</span>
                            </td>
                        </tr>
                        <tr>
                            <th>商品规格</th>
                            <td>
                                <input type="radio" value="one"
                                       [#if !product.specifications?has_content]checked[/#if]
                                       name="specificationType"
                                       style="margin-bottom: 5px;cursor: pointer;"/><span
                                    onclick="$(this).prev().click();" style="cursor: pointer;">统一规格</span>&nbsp;&nbsp;
                                <input type="radio" value="many"
                                       [#if product.specifications?has_content]checked[/#if]
                                       name="specificationType"
                                       style="margin-bottom: 5px;cursor: pointer;"/><span
                                    onclick="$(this).prev().click();" style="cursor: pointer;">多规格</span>
                            </td>
                        </tr>
                        <tr id="specificationRadioTr" class="[#if !product.specifications?has_content]hidden[/#if]">
                            <td colspan="2" style="padding: 0;border: 0;">
                                <table class="input">
                                    <tr>
                                        <td>
                                            <a href="javascript:;" id="addSpecificationProduct"
                                               class="button">增加规格商品</a>
                                        </td>
                                    </tr>
                                    <tr>[#--规格复选框--]
                                        <td style="padding: 0;">
                                            <table id="specificationProductTable" class="input" style="border: 0">
                                                <tr class="title">
                                                    <td><span>条形码</span></td>
                                                [#list specifications as specification]
                                                    [#if specification.id==1]
                                                        <td>
                                                            <input type="checkbox" name="specificationIds" value="1"
                                                                   id="specification1Ids"
                                                                [#if product.specifications?seq_contains(specification)]
                                                                   checked="checked"[/#if]/>
                                                            <span class="specification_1">${(product.goods.specification1Title)!specification.name}</span>
                                                            <input id="specification1Title" type="text"
                                                                   name="specification1Title" class="text hidden"
                                                                   style="width: 50px"
                                                                   value="${(product.goods.specification1Title)!specification.name}"
                                                                   oninput="changeSpec(this)"
                                                                   onchange="changeSpec(this)"/>
                                                        </td>
                                                    [/#if]
                                                    [#if specification.id==2]
                                                        <td>
                                                            <input type="checkbox" name="specificationIds" value="2"
                                                                   id="specification2Ids"
                                                                [#if product.specifications?seq_contains(specification)]
                                                                   checked="checked"[/#if]/>
                                                            <span class="specification_2">${(product.goods.specification2Title)!specification.name}</span>
                                                            <input id="specification2Title" type="text"
                                                                   name="specification2Title" class="text hidden"
                                                                   style="width: 50px"
                                                                   value="${(product.goods.specification2Title)!specification.name}"
                                                                   oninput="changeSpec(this)"
                                                                   onchange="changeSpec(this)"/>
                                                        </td>
                                                    [/#if]
                                                [/#list]
                                                    <td>
                                                        销售价
                                                    </td>
                                                    <td>
                                                        市场价
                                                    </td>
                                                    <td>
                                                        库存
                                                    </td>
                                                    <td>
                                                        成本价
                                                    </td>
                                                [#--<td>--]
                                                [#--普通会员--]
                                                [#--</td>--]
                                                    <td>
                                                        VIP1
                                                    </td>
                                                    <td>
                                                        VIP2
                                                    </td>
                                                    <td>
                                                        VIP3
                                                    </td>
                                                    <td>
                                                        VIP4
                                                    </td>
                                                    <td>
                                                        商品编码
                                                    </td>
                                                    <td>
                                                    ${message("shop.common.handle")}
                                                    </td>
                                                </tr>
                                                <tr class="hidden">
                                                    <td>
                                                        <input type="text" name="barcodes" style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="specification_1" style="width: 50px;"
                                                               disabled/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="specification_2" style="width: 50px;"
                                                               disabled/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="prices"
                                                               style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="marketPrices"
                                                               style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="stocks"
                                                               style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="costs"
                                                               style="width: 50px;"/>
                                                    </td>
                                                [#list memberRanks?sort_by(["id"]) as memberRank]
                                                    [#if memberRank.id!=1]
                                                        <td>
                                                            <input type="text" id="memberPrice_${memberRank.id}s"
                                                                   name="memberPrice_${memberRank.id}s"
                                                                   maxlength="8"
                                                                   style="width: 50px; margin-right: 6px;"/>
                                                        </td>
                                                    [/#if]
                                                [/#list]
                                                    <td><input type="text" style="width: 50px;"
                                                               disabled/></td>
                                                    <td>
                                                        <a href="javascript:;"
                                                           class="deleteSpecificationProduct">[删除]</a>
                                                    </td>
                                                </tr>
                                            [#if product.specifications?has_content]
                                                <tr>

                                                    <input type="hidden" name="specificationProductIds"
                                                           value="${product.id}"/>
                                                    <td>
                                                        <input type="text" name="barcodes" style="width: 50px;"
                                                               value="${product.barcode}"/>
                                                    </td>
                                                    [#list specifications as specification]
                                                        [#if (specification.id lte 2)]
                                                            <td>
                                                                <input type="text"
                                                                       name="specification_${specification.id}"
                                                                       id="currentSpecification"
                                                                    [#list specification.specificationValues as specificationValue]
                                                                        [#if product.specificationValues?seq_contains(specificationValue)]
                                                                       value="${specificationValue.name}" [/#if]
                                                                    [/#list] style="width: 50px;"
                                                                />

                                                            </td>
                                                        [/#if]
                                                    [/#list]
                                                    <td>
                                                        <input type="text" id="currentPrice" name="prices"
                                                               value="${product.price}" style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" id="currentMarketPrice" name="marketPrices"
                                                               style="width: 50px;" value="${product.marketPrice}"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" id="currentStock" name="stocks"
                                                               value="${product.stock}" style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" id="currentCost" name="costs"
                                                               style="width: 50px;" value="${product.cost}"/>
                                                    </td>
                                                    [#list memberRanks?sort_by(["id"]) as memberRank]
                                                      [#if memberRank.id!=1]
                                                        <td>
                                                            <input type="text" id="memberPrice_${memberRank.id}s"
                                                                   name="memberPrice_${memberRank.id}s"
                                                                   maxlength="8"
                                                                   value="${product.memberPrice.get(memberRank)}"
                                                                   style="width: 50px; margin-right: 6px;"/>
                                                        </td>
                                                      [/#if]
                                                    [/#list]
                                                    <td><input type="text" value="${product.sn}" style="width: 50px;"
                                                               disabled/></td>
                                                    <td>
                                                        <a href="javascript:;"
                                                           class="deleteSpecificationProduct">[删除]</a>
                                                    </td>
                                                </tr>
                                            [/#if]
                                            [#list product.siblings as specificationProduct]
                                                <tr>
                                                    <input type="hidden" name="specificationProductIds"
                                                           value="${specificationProduct.id}"/>
                                                    <td>
                                                        <input type="text" name="barcodes" style="width: 50px;"
                                                               value="${specificationProduct.barcode}"/>
                                                    </td>
                                                    [#list specifications as specification]
                                                        [#if (specification.id lte 2)]
                                                            <td>
                                                                <input type="text"
                                                                       name="specification_${specification.id}"
                                                                    [#list specification.specificationValues as specificationValue]
                                                                        [#if specificationProduct.specificationValues?seq_contains(specificationValue)]
                                                                       value="${specificationValue.name}" [/#if]
                                                                    [/#list] style="width: 50px;"
                                                                />
                                                            </td>
                                                        [/#if]
                                                    [/#list]

                                                    <td>
                                                        <input type="text" name="prices"
                                                               value="${specificationProduct.price}"
                                                               style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="marketPrices"
                                                               style="width: 50px;"
                                                               value="${specificationProduct.marketPrice}"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="stocks"
                                                               value="${specificationProduct.stock}"
                                                               style="width: 50px;"/>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="costs"
                                                               style="width: 50px;"
                                                               value="${specificationProduct.cost}"/>
                                                    </td>
                                                    [#list memberRanks?sort_by(["id"]) as memberRank]
                                                        [#if memberRank.id!=1]
                                                        <td>
                                                            <input type="text" id="memberPrice_${memberRank.id}s"
                                                                   name="memberPrice_${memberRank.id}s"
                                                                   maxlength="8"
                                                                   value="${specificationProduct.memberPrice.get(memberRank)}"
                                                                   style="width: 50px; margin-right: 6px;"/>
                                                        </td>
                                                        [/#if]
                                                    [/#list]
                                                    <td><input class="specSn" type="text"
                                                               value="${specificationProduct.sn}"
                                                               style="width: 50px;" disabled/></td>
                                                    <td>
                                                        <a href="javascript:;"
                                                           class="deleteSpecificationProduct">[删除]</a>
                                                    </td>
                                                </tr>
                                            [/#list]
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr id="snTr" [#if product.specifications?has_content]class="hidden"[/#if]>
                            <th>商品编码</th>
                            <td>
                                <input type="text" id="sn" class="text" value="${product.sn}" disabled
                                       title="商品编码为自动生成"/>
                            </td>
                        </tr>
                        <tr id="productPrice" class="[#if product.specifications?has_content]hidden[/#if]">
                            <th>
                                <span class="requiredField">*</span>销售价:
                            </th>
                            <td>
                                <input id="price" type="text" name="price" class="text" maxlength="16"
                                       value="${product.price}"/>
                            </td>
                        </tr>
                        <tr id="productCost" class="[#if product.specifications?has_content]hidden[/#if]">
                            <th>
                                <span class="requiredField">*</span>成本价:
                            </th>
                            <td>
                                <input id="cost" type="text" name="cost" class="text" maxlength="16"
                                       value="${product.cost}"/>
                            </td>
                        </tr>
                        <tr id="productMarketPrice" class="[#if product.specifications?has_content]hidden[/#if]">
                            <th>
                                市场价:
                            </th>
                            <td>
                                <input type="text" name="marketPrice" class="text" value="${product.marketPrice}"
                                       maxlength="16" title="${message("shop.product.marketPriceTitle")}"/>
                            </td>
                        </tr>
                        <tr id="productMemberPrice" class="[#if product.specifications?has_content]hidden[/#if]">
                            <th>
                            ${message("Product.memberPrice")}:
                            </th>
                            <td>
                                <label>
                                    <input type="checkbox" id="isMemberPrice" name="isMemberPrice"
                                           value="true"[#if product.memberPrice?has_content]
                                           checked="checked"[/#if]/> ${message("admin.product.isMemberPrice")}
                                </label>
                            </td>
                        </tr>
                        <tr id="memberPriceTr"[#if product.specifications?has_content] class="hidden"[/#if]>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                            [#list memberRanks?sort_by(["id"]) as memberRank]
                                <p style="padding:5px 10px 5px 0; display:block; word-wrap: break-word; word-break: normal; border:0; float:left; text-align:left;">${message("MemberRank."+memberRank.name)}
                                    : <input type="text" name="memberPrice_${memberRank.id}" class="text memberPrice"
                                             value="${product.memberPrice.get(memberRank)}" maxlength="16"
                                             style="width: 60px; margin-right: 6px;"[#if !product.memberPrice?has_content]
                                             disabled="disabled"[/#if]/></p>
                            [/#list]
                            </td>
                        </tr>
                        <tr id="productStock" class="[#if product.specifications?has_content]hidden[/#if]">
                            <th>
                                <span class="requiredField">*</span>库存:
                            </th>
                            <td>
                                <input type="text" id="stock" name="stock" class="text" value="${product.stock}"
                                       maxlength="9"/>
                            </td>
                        </tr>
                    [#if versionType==1]
                        <tr>
                            <th>
                            ${message("Product.brand")}:
                            </th>
                            <td>
                                <select id="brandId" name="brandId">
                                    <option value="">${message("shop.common.choose")}</option>
                                    [#list brands as brand]
                                        <option value="${brand.id}" [#if brand == product.brand]
                                                selected="selected"[/#if]>
                                        ${brand.name}
                                        </option>
                                    [/#list]
                                </select>
                            </td>
                        </tr>
                    [/#if]

                        <tr>
                            <th>
                                主供应商:
                            </th>
                            <td>
                                <select name="supplierId">
                                    <option value="">${message("shop.common.choose")}</option>
                                [#list suppliers as supplier]
                                    <option value="${supplier.parent.id}"[#if product.supplier == supplier.parent]
                                            selected="selected"[/#if]>${supplier.parent.name}</option>
                                [/#list]
                                </select>
                            </td>
                        </tr>
                        <tr class="hidden">
                            <th>
                                最低起订单:
                            </th>
                            <td>
                                <input type="text" name="minReserve" class="text" value="${product.minReserve}"
                                       maxlength="9" title="最低起订单"/>
                            </td>
                        </tr>
                    [#--图片begin--]
                        <tr>
                            <th>商品图片</th>
                            <td>
                                <table id="productImageTable" class="input" style="border-top: solid 1px #e0e0e0;">
                                    <tr>
                                        <td colspan="4">
                                            <a href="javascript:;" id="addProductImage"
                                               class="button">${message("shop.product.addProductImage")}</a>
                                            <span>目前只支持jpg,jpeg,bmp,gif,pngG图片格式，图片大小不能超过2MB！</span>
                                        </td>
                                    </tr>
                                    <tr class="title">
                                        <th style="width:40%;">
                                        ${message("ProductImage.file")}
                                        </th>
                                        <th style="width:40%;">
                                        ${message("ProductImage.title")}
                                        </th>
                                        <th style="width:10%;">
                                        ${message("shop.common.order")}
                                        </th>
                                        <th style="width:10%;">
                                        ${message("shop.common.delete")}
                                        </th>
                                    </tr>
                                [#list product.productImages as productImage]
                                    <tr>
                                        <td>
                                            <input type="hidden" name="productImages[${productImage_index}].source"
                                                   value="${productImage.source}"/>
                                            <input type="hidden" name="productImages[${productImage_index}].large"
                                                   value="${productImage.large}"/>
                                            <input type="hidden" name="productImages[${productImage_index}].medium"
                                                   value="${productImage.medium}"/>
                                            <input type="hidden" name="productImages[${productImage_index}].thumbnail"
                                                   value="${productImage.thumbnail}"/>
                                            <img class="proImg" id="img${productImage_index}"
                                                 src="${productImage.thumbnail}" width="100px"
                                                 height="100px"/>
                                            <input type="hidden" name="productImages[${productImage_index}].local"
                                                   maxlength="200"
                                            />
                                            <input type="button" id="browserButton${productImage_index}" class="button"
                                                   value="选择"
                                            />
                                        </td>
                                        <td>
                                            <input type="text" name="productImages[${productImage_index}].title"
                                                   class="text"
                                                   maxlength="200" value="${productImage.title}"/>
                                        </td>
                                        <td>
                                            <input type="text" name="productImages[${productImage_index}].order"
                                                   class="text productImageOrder" value="${productImage.order}"
                                                   maxlength="9"
                                                   style="width: 50px;"/>
                                        </td>
                                        <td>
                                            <a href="javascript:;"
                                               class="deleteProductImage">[${message("shop.common.delete")}]</a>
                                        </td>
                                    </tr>
                                [/#list]
                                </table>
                            </td>
                        </tr>
                    [#--图片end--]
                    [#--商品参数begin--]
                        <tr [#if !product.productCategory.parameterGroups?has_content]class="hidden" [/#if]>
                            <th>商品参数</th>
                            <td>
                                <table id="parameterTable" class="input" style="border-top: solid 1px #e0e0e0;">
                                [#list product.productCategory.parameterGroups as parameterGroup]
                                    <tr>
                                        <th style="text-align: right; padding-right: 10px;width: 15%;">
                                            <strong>${parameterGroup.name}:</strong>
                                        </th>
                                        <th>
                                            &nbsp;
                                        </th>
                                    </tr>
                                    [#list parameterGroup.parameters as parameter]
                                        <tr>
                                            <td style="text-align: right;">${parameter.name}:</td>
                                            <td>
                                                <input type="text" name="parameter_${parameter.id}" class="text"
                                                       value="${product.parameterValue.get(parameter)}"
                                                       maxlength="200"/>
                                            </td>
                                        </tr>
                                    [/#list]
                                [/#list]
                                </table>
                            </td>
                        </tr>
                    [#--商品参数end--]
                        <tr>
                            <th>商品详情</th>
                            <td>
                                <table class="input" style="border-top: solid 1px #e0e0e0;">
                                    <tr>
                                        <td>
                                            <input type="radio" name="type" id="radio1" value="0"
                                                   style="margin-bottom: 5px;cursor: pointer;" checked="checked"/><span
                                                onclick="javascript:$(this).prev().click();" style="cursor: pointer;">电脑端</span>&nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="type" id="radio2" value="1"
                                                   style="margin-bottom: 5px;cursor: pointer;"/><span
                                                onclick="javascript:$(this).prev().click();" style="cursor: pointer;">手机端</span>
                                        </td>
                                    </tr>
                                    <tr id="pcTr">
                                        <td>
                                <textarea id="editor" name="introduction" class="editor"
                                          style="width: 100%;">${product.introduction?html}</textarea>
                                        </td>
                                    </tr>
                                    <tr id="appTr" style="display: none;">
                                        <td>
                                <textarea id="editor_app" name="descriptionapp" class="editor"
                                          style="width: 100%;">${product.descriptionapp?html}</textarea>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.tags")}:
                            </th>
                            <td>
                            [#list tags as tag]
                                <label>
                                    [#if tag.id=1]
                                        <input type="checkbox" name="tagIds"
                                               value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                               checked="checked"[/#if]/> ${tag.name}
                                    [#elseif tag.id=2]
                                        <input type="checkbox" name="tagIds"
                                               value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                               checked="checked"[/#if]/> ${tag.name}
                                    [#elseif tag.id=5]
                                        <input type="checkbox" name="tagIds"
                                               value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                               checked="checked"[/#if]/> ${tag.name}
                                    [#elseif tag.id=24]
                                        <input type="checkbox" name="tagIds"
                                               value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                               checked="checked"[/#if]/> ${tag.name}
                                    [#elseif tag.id=31]
                                        <input type="checkbox" name="tagIds"
                                               value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                               checked="checked"[/#if]/> ${tag.name}
                                    [#else]
                                        [#if product.tags?seq_contains(tag)]
                                            <input type="hidden" name="tagIds" value="${tag.id}"/>
                                        [/#if]
                                    [/#if]

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
                                    <input type="checkbox" name="isList" value="true"[#if product.isList]
                                           checked="checked"[/#if]/> ${message("Product.isList")}
                                    <input type="hidden" name="_isList" value="false"/>
                                </label>
                                <label>
                                    <input type="checkbox" name="isTop" value="true"[#if product.isTop]
                                           checked="checked"[/#if]/> ${message("Product.isTop")}
                                    <input type="hidden" name="_isTop" value="false"/>
                                </label>
                                <label>
                                    <input type="checkbox" name="isGift" value="true"[#if product.isGift]
                                           checked="checked"[/#if]/> ${message("Product.isGift")}
                                    <input type="hidden" name="_isGift" value="false"/>
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.memo")}:
                            </th>
                            <td>
                                <input type="text" name="memo" class="text" value="${product.memo}" maxlength="200"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.keyword")}:
                            </th>
                            <td>
                                <input type="text" name="keyword" class="text" value="${product.keyword}"
                                       maxlength="200" title="${message("shop.product.keywordTitle")}"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.seoTitle")}:
                            </th>
                            <td>
                                <input type="text" name="seoTitle" class="text" value="${product.seoTitle}"
                                       maxlength="200"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.seoKeywords")}:
                            </th>
                            <td>
                                <input type="text" name="seoKeywords" class="text" value="${product.seoKeywords}"
                                       maxlength="200"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("Product.seoDescription")}:
                            </th>
                            <td>
                                <input type="text" name="seoDescription" class="text" value="${product.seoDescription}"
                                       maxlength="200"/>
                            </td>
                        </tr>
                    </table>
                    <table class="input">
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <input type="submit" class="button" value="${message("shop.common.submit")}"/>
                                <input type="button" class="button" value="${message("shop.common.back")}"
                                       onclick="javascript:history.back();"/>
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
