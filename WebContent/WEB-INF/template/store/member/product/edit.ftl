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

    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <link href="${base}/resources/common/jcrop/css/jquery.Jcrop.css" rel="stylesheet" type="text/css"/>
    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.placeholder.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>

    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/laddSelect.js"></script>
    [#--<script src="${base}/resources/store/js/amazeui.min.js"></script>--]
[#--<script type="text/javascript" src="${base}/resources/common/js/jquery.tools.js"></script>--]
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript">
        $(function () {
            // 地区选择
            var $areaId = $("#areaId");
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
            //店铺头像上传
            var settings_logo = {
                width: 120,
                height: 120
            };
            $("#browserLogoButton").browser(settings_logo);
            //店铺招牌上传
            var settings = {
                width: 360,
                height: 360
            };
        });
    </script>

    <style type="text/css">
        div.xxBrowser .browserBar {
            position:relative;
        }
        div.xxBrowser .browserBar a:nth-child(3){
            position: absolute;
            right: 240px;
            top: 16px;
        }
        div.xxBrowser .browserBar a:nth-child(4){
            position: absolute;
            right: 135px;
            top: 16px;
        }
        div.xxBrowser .browserBar form a:nth-child(3){
            position: static;
            right: 0;
            top: 0;
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
//            $("#specificationRadioTr").hide();
            var $addProductPackagUnit = $("#addProductPackagUnit");
            var $productPackagUnitTable = $("#productPackagUnitTable");
            var $sns = $(".sns");
            var productPackagUnitIndex =${(product.packagUnits?size)!"0"};
            $('input, textarea').placeholder();
            $("#specification1Title").hide();
            $("#specification2Title").hide();
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
                    url: "${base}/store/member/product/getSeries.jhtml",
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
                    return false;
                }
            [@compress single_line = true]
                var trHtml = '<tr><td><img class="proImg" id = "img' + productImageIndex + '" src = "" width = "100px" height = "100px" \/><input type = "hidden" name = "productImages[' + productImageIndex + '].local" maxlength = "200" \/><input type = "button" id = "browserButton' + productImageIndex + '" class= "button" value = "选择文件" \/><\/td><td><input type = "text" name = "productImages[' + productImageIndex + '].title" class = "text" maxlength = "200" \/><\/td><td><input type = "text" name = "productImages[' + productImageIndex + '].order" class= "text productImageOrder" maxlength = "9" style = "width: 50px;" \/><\/td><td><a href = "javascript:;" class= "deleteProductImage" onclick="deleteLiveAdd(this)" > [${message("shop.common.delete")}] <\/a><\/td><\/tr>';
            [/@compress]
                $productImageTable.append(trHtml);
                var $browserButton = $("#browserButton" + productImageIndex);
                settings.img = $("#img" + productImageIndex);
                $browserButton.browser(settings);
                productImageIndex++;
            });

            // 删除商品图片
        [#--$deleteProductImage.live("click", function () {--]
        [#--var $this = $(this);--]
        [#--$.dialog({--]
        [#--type: "warn",--]
        [#--content: "${message("shop.dialog.deleteConfirm")}",--]
        [#--onOk: function () {--]
        [#--$this.closest("tr").remove();--]
        [#--}--]
        [#--});--]
        [#--});--]

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
        [#--$deleteSpecificationProduct.live("click", function () {--]
        [#--var $this = $(this);--]
        [#--$.dialog({--]
        [#--type: "warn",--]
        [#--content: "${message("shop.dialog.deleteConfirm")}",--]
        [#--onOk: function () {--]
        [#--if ($this.closest("tr").prev().css("display") == "none" && $specificationProductTable.find("tr:gt(1)").size() > 0) {--]
        [#--var $tr = $this.closest("tr").next().find("td:first");--]
        [#--var $input = $this.closest("tr").next().find("td:first input");--]
        [#--$tr.html("当前规格").append($input);--]
        [#--}--]
        [#--$this.closest("tr").remove();--]
        [#--}--]
        [#--});--]
        [#--});--]

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
                                    content = "商品规格【价格】不能为空";
                                }
//                                console.log($(this).parent.find("td:eq(4)").val().trim());
//                                if (parseFloat($(this).val().trim()) > parseFloat($(this).parent.children("td:eq(4)").val().trim())) {
//                                    validate = false;
//                                    content = "商品规格【价格】值不能大于市场价";
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
                            $.message("error", "商品规格【价格】值不能大于市场价");
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
//                    prevBarcode = $("#barcode").val();
//                    prevPrice = $("#price").val();
//                    prevStock = $("#stock").val();
//                    prevCost = $("#cost").val();
//                    prevMarketPrice = $("#marketPrice").val();
//                    if ($("#price").val().trim() == "") $("#price").val(0);
//                    if ($("#stock").val().trim() == "") $("#stock").val(0);
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
                    $specificationProductTable.find("tr:eq(1)").clone().show().appendTo($specificationProductTable);
                    //$tr.find("td:first").text("当前规格");
                } else {
                    //$sns.val("");
                    $specificationProductTable.find("tr").eq($specificationProductTable.find("tr").length - 1).clone().show().appendTo($specificationProductTable);
                    $specificationProductTable.find("tr:last").find("input[name='specificationProductIds']").remove();
                    $specificationProductTable.find("tr:last").find("input[class='specSn']").val("");
                }
            });

        });

        function deleteLiveAdd(thisTr){
            $(thisTr).closest("tr").remove();
        }

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
                        url: "${base}/store/member/product/brandSeries.jhtml"
                    });
                }
            });
        });

    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
<div class="wrapper">
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                我的商品
                <small>快去回复粉丝们咨询的问题，对您的诚信度有很大的帮助。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/product/isMarketableList.jhtml">我的商品</a></li>
                <li><a href="${base}/store/member/product/isMarketableList.jhtml">商品列表</a></li>
                <li class="active">编辑</li>
            </ol>
        </section>

        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom" style="box-shadow: none;">
                    <ul class="nav nav-tabs pull-right">
                        <li class="active"><a href="${base}/store/member/product/addProductCategory.jhtml">商品维护</a></li>
                        <li><a href="${base}/store/member/product/isMarketableList.jhtml">我的商品</a></li>
                        <li class="pull-left header"><i class="fa fa-pencil-square-o"></i>编辑我的商品</li>
                    </ul>
                </div>

                <div class="box-body">
                    <form class="form-horizontal" role="form" id="inputForm"
                          action="${base}/store/member/product/update.jhtml" method="post"
                          enctype="multipart/form-data">
                        <input type="hidden" name="id" value="${product.id}"/>
                        <input type="hidden" name="productCategoryId" value="${product.productCategory.id}"/>
                        <input type="hidden" name="isMarketable" value="${product.isMarketable}"/>
                        <input type="hidden" name="sn" value="${product.sn}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品类目</label>
                            <div class="col-sm-8">
                            [#if product.productCategory.parent?has_content][#if product.productCategory.parent.parent?has_content]${product.productCategory.parent.parent.name}
                                /[/#if]${product.productCategory.parent.name}/[/#if]${product.productCategory.name}
                            </div>
                        </div>
                    [#if product.productCategory.attributes?has_content]

                    [/#if]
                        <div class="form-group" id="productBarcode">
                            <label class="col-sm-2 control-label">条形码</label>
                            <div class="col-sm-8">
                                <input type="text" name="barcode" class="form-control" value="${product.barcode}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品分类</label>
                            <div class="col-sm-6">
                                <select class="form-control" name="productCategoryTenantId">
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
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><span class="red">*</span>名称</label>
                            <div class="col-sm-8">
                                <input type="text" name="name" class="form-control" value="${product.name}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><span class="red">*</span>单位</label>
                            <div class="col-sm-5">
                                <input type="text" name="unit" class="form-control" value="${product.unit}">
                            </div>
                            <div class="col-sm-3">计量单位,例:盒、件、箱、瓶。</div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品规格</label>
                            <div class="col-sm-9 pt5 label-m15">
                                <div class="radio">
                                    <label>
                                        <input type="radio" value="one"
                                               [#if !product.specifications?has_content]checked[/#if]
                                               name="specificationType"
                                               style="margin-bottom: 5px;cursor: pointer;"/><span
                                            onclick="$(this).prev().click();" style="cursor: pointer;">统一规格</span>&nbsp;&nbsp;
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" value="many"
                                               [#if product.specifications?has_content]checked[/#if]
                                               name="specificationType"
                                               style="margin-bottom: 5px;cursor: pointer;"/><span
                                            onclick="$(this).prev().click();" style="cursor: pointer;">多规格</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="specificationRadioTr" [#if !product.specifications?has_content]style="display: none"[/#if]">
                            <label class="col-sm-2 control-label"></label>
                            <div class="col-sm-8">
                                <table class="table table-bordered table-striped">
                                    <tr>
                                        <td>
                                            <a href="javascript:;" id="addSpecificationProduct"
                                               class="button">增加规格商品</a>
                                        </td>
                                    </tr>
                                    <tr>[#--规格复选框--]
                                        <td style="padding: 0;">
                                            <table id="specificationProductTable" class="table table-bordered table-striped" style="border: 0">
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
                                                                   name="specification1Title"
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
                                                                   name="specification2Title"
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
                                                <tr style="display: none;">
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
                                                           class="deleteSpecificationProduct" onclick="deleteLiveAdd(this)">[删除]</a>
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
                                                           class="deleteSpecificationProduct"onclick="deleteLiveAdd(this)">[删除]</a>
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
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品编码</label>
                            <div class="col-sm-8">
                                <input type="text" id="sn" class="form-control" value="${product.sn}" disabled>
                            </div>
                        </div>
                        <div class="form-group" id="productPrice" [#if product.specifications?has_content]style="display:none;" [/#if]>
                            <label class="col-sm-2 control-label"><span class="red">*</span>销售价</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" name="price" value="${product.price}">
                            </div>
                        </div>
                        <div class="form-group" id="productCost" [#if product.specifications?has_content]style="display: none" [/#if]>
                            <label class="col-sm-2 control-label"><span class="red"></span>成本价</label>
                            <div class="col-sm-8">
                                <input type="text" name="cost" class="form-control" value="${product.cost}">
                            </div>
                        </div>
                        <div class="form-group" id="productMarketPrice" [#if product.specifications?has_content]style="display: none" [/#if]>
                            <label class="col-sm-2 control-label">市场价</label>
                            <div class="col-sm-8">
                                <input type="text" name="marketPrice" class="form-control"
                                       value="${product.marketPrice}">
                            </div>
                        </div>
                        <div class="form-group" id="productMemberPrice" [#if product.specifications?has_content]style="display: none" [/#if]>
                            <label class="col-sm-2 control-label">会员价</label>
                            <div class="col-sm-8">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" id="isMemberPrice" name="isMemberPrice"
                                               value="true"[#if product.memberPrice?has_content]
                                               checked="checked"[/#if]/> ${message("admin.product.isMemberPrice")}
                                    </label>
                                </div>
                                <div id="memberPriceTr" class="row select-vip">
                                [#list memberRanks?sort_by(["id"]) as memberRank]
                                    <p style="padding:5px 10px 5px 0; display:block; word-wrap: break-word; word-break: normal; border:0; float:left; text-align:left;">${message("MemberRank."+memberRank.name)}
                                        : <input type="text" name="memberPrice_${memberRank.id}"
                                                 class="text memberPrice"
                                                 value="${product.memberPrice.get(memberRank)}" maxlength="16"
                                                 style="width: 60px; margin-right: 6px;"[#if !product.memberPrice?has_content]
                                                 disabled="disabled"[/#if]/></p>
                                [/#list]
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="productStock" [#if product.specifications?has_content]style="display: none" [/#if]>
                            <label class="col-sm-2 control-label"><span class="red">*</span>库存</label>
                            <div class="col-sm-8">
                                <input type="text" name="stock" class="form-control" value="${product.stock}">
                            </div>
                        </div>
                    [#if versionType==1]
                        <div class="form-group">
                            <label class="col-sm-2 control-label">品牌</label>
                            <div class="col-sm-4">
                                <select type="text" class="form-control" id="brandId" name="brandId">
                                    <option value="">${message("shop.common.choose")}</option>
                                    [#list brands as brand]
                                        <option value="${brand.id}" [#if brand == product.brand]
                                                selected="selected"[/#if]>
                                        ${brand.name}
                                        </option>
                                    [/#list]
                                </select>
                            </div>
                        </div>
                    [/#if]
                        <div class="form-group">
                            <label class="col-sm-2 control-label">主供应商</label>
                            <div class="col-sm-4">
                                <select name="supplierId" class="form-control">
                                    <option value="">${message("shop.common.choose")}</option>
                                [#list suppliers as supplier]
                                    <option value="${supplier.parent.id}"[#if product.supplier == supplier.parent]
                                            selected="selected"[/#if]>${supplier.parent.name}</option>
                                [/#list]
                                </select>
                            </div>
                        </div>
                        <div class="hidden">
                            <label class="col-sm-2 control-label"><span class="red">*</span>最低起订单</label>
                            <div class="col-sm-8">
                                <input type="text" name="stock" class="form-control" value="${product.stock}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品图片</label>
                            <div class="col-sm-8">
                                <table id="productImageTable" class="table table-bordered table-striped mt10">
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
                                                   value="选择文件"
                                            />
                                        </td>
                                        <td>
                                            <input type="text" name="productImages[${productImage_index}].title"
                                                   class="from-control"
                                                   maxlength="200" value="${productImage.title}"/>
                                        </td>
                                        <td>
                                            <input type="text" name="productImages[${productImage_index}].order"
                                                   class="from-control" value="${productImage.order}"
                                                   maxlength="9"
                                                   style="width: 50px;"/>
                                        </td>
                                        <td>
                                            <a href="javascript:;"
                                               class="deleteProductImage" onclick="deleteLiveAdd(this)">[${message("shop.common.delete")}]</a>
                                        </td>
                                    </tr>
                                [/#list]
                                </table>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品参数</label>
                            <div class="col-sm-8">
                                <table id="parameterTable" class="table table-bordered ">
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
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品详情</label>
                            <div class="col-sm-8">
                                <table class="table table-bordered table-hover dataTable"
                                       style="border-top: solid 1px #e0e0e0;">
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
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">标签</label>
                            <div class="col-sm-8 pt5 label-m15">
                            [#list tags as tag]
                                <label>
                                    [#if tag.id=1]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                                       checked="checked"[/#if]/> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=2]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                                       checked="checked"[/#if]/> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=5]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                                       checked="checked"[/#if]/> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=24]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                                       checked="checked"[/#if]/> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=31]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}" [#if product.tags?seq_contains(tag)]
                                                       checked="checked"[/#if]/> ${tag.name}
                                            </label>
                                        </div>
                                    [#else]
                                        [#if product.tags?seq_contains(tag)]
                                            <div class="checkbox">
                                                <label>
                                                    <input type="hidden" name="tagIds" value="${tag.id}"/>
                                                </label>
                                            </div>
                                        [/#if]
                                    [/#if]

                                </label>
                            [/#list]
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">设置</label>
                            <div class="col-sm-8 pt5 label-m15">
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
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注</label>
                            <div class="col-sm-8 ">
                                <input type="text" class="form-control" name="memo" value="${product.memo}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">搜索关键词</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="keyword" class="form-control" value="${product.keyword}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">页面标题：</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="seoTitle" class="form-control" value="${product.seoTitle}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">页面关键词</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="seoKeywords" class="form-control"
                                       value="${product.seoKeywords}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">页面描述</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="seoDescription" class="form-control"
                                       value="${product.seoDescription}">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-2">
                                <button type="submit" class="btn btn-block btn-success">确定</button>
                            </div>
                            <div class="col-sm-offset-0 col-sm-2">
                                <button onclick="javascript:history.back();" class="btn btn-block btn-default">返回
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /
          </section>
          <!-- /.content -->
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>


</body>
</html>
