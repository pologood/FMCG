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

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelectProductCategory.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.placeholder.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/laddSelect.js"></script>

[#--<script type="text/javascript" src="${base}/resources/common/js/jquery.tools.js"></script>--]
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
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
    </style>
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

        var productImageIndex, settings;

        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $productCategoryId = $("#productCategoryId");
            var $isMemberPrice = $("#isMemberPrice");
            var $memberPriceTr = $("#memberPriceTr");
            var $memberPrice = $("#memberPriceTr input");
            var $productImageTable = $("#productImageTable");
            var $deleteProductImage = $("a.deleteProductImage");
            var $parameterTable = $("#parameterTable");
            var $attributeTable = $("#attributeTable");
            var $specificationIds = $("[name='specificationIds']");
            var $specificationProductTable = $("#specificationProductTable");
            var $addSpecificationProduct = $("#addSpecificationProduct");
            var $deleteSpecificationProduct = $("a.deleteSpecificationProduct");
            $("#specificationRadioTr").hide();
            $("#specTr").hide();
            $("#specification1Title").hide();
            $("#specification2Title").hide();
            productImageIndex = 1;

            $("#browserButton").browser();
        [@flash_message /]

            settings = {
                isSubmit: false
            };
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
                    }
            );
        [/#if]

            // 商品详情"类型"修改
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

            var previousProductCategoryId = getCookie("previousProductCategoryId");
            if (previousProductCategoryId == null) {
                previousProductCategoryId = $productCategoryId.val();
            }

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
            // 增加商品图片
            for (var i = 0; i < 6; i++) {
                var $browserButton = $("#browserButton" + i);
                settings.img = $("#img" + i);
                settings.input = $("input[name='productImages[" + i + "].local']");
                $browserButton.browser(settings);
            }

            [#--// 删除商品图片--]
            [#--$("#productImageTable tr td a").on("click",function(){--]
[#--//            $deleteProductImage.on("click", function () {--]
[#--//        $deleteProductImage.live("click", function () {--]
                [#--alert(0);--]
                var $this = $(this);
                [#--$.dialog({--]
                    [#--type: "warn",--]
                    [#--content: "${message("shop.dialog.deleteConfirm")}",--]
                    [#--onOk: function () {--]
                        [#--$this.closest("tr").remove();--]
[#--//                    }--]
[#--//                });--]
            [#--});--]


            //删除商品图片


            // 修改商品分类
            $productCategoryId.change(function () {
                var hasValue = false;
                $parameterTable.find(":input").each(function () {
                    if ($.trim($(this).val()) != "") {
                        hasValue = true;
                        return false;
                    }
                });
                if (!hasValue) {
                    $attributeTable.find(":input").each(function () {
                        if ($.trim($(this).val()) != "") {
                            hasValue = true;
                            return false;
                        }
                    });
                }
                if (hasValue) {
                    $.dialog({
                        type: "warn",
                        content: "${message("shop.product.productCategoryChangeConfirm")}",
                        width: 450,
                        onOk: function () {
                            previousProductCategoryId = $productCategoryId.val();
                        },
                        onCancel: function () {
                            $productCategoryId.val(previousProductCategoryId);
                        }
                    });
                } else {
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
                        //商品参数
                        var trHtml = "";
                        $.each(data, function (i, parameterGroup) {
                            trHtml += '<tr><td style="text-align: right;"><strong>' + parameterGroup.name + ':<\/strong><\/td><td>&nbsp;<\/td><\/tr>';
                            $.each(parameterGroup.parameters, function (i, parameter) {
                            [@compress single_line = true]
                                trHtml += '<tr><th> ' + parameter.name + ':<\/th><td><input type="text" name="parameter_' + parameter.id + '" class="text" maxlength="200"\/><\/td><\/tr>';
                            [/@compress]
                            });
                        });
                        $parameterTable.append(trHtml);
                    }
                });
            }

            // 删除规格商品
            $deleteSpecificationProduct.on("click", function () {
                alert(0);
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("shop.dialog.deleteConfirm")}",
                    onOk: function () {
                        if ($this.closest("tr").prev().css("display") == "none" && $specificationProductTable.find("tr:gt(1)").size() > 0) {
                            $this.closest("tr").next().find("td:first").text("当前规格");
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
                            url: "check_sn.jhtml",
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
                    name: "${message("admin.validate.required")}",
                    unit: "${message("admin.validate.required")}",
                    price: "${message("admin.validate.required")}"
                },
                submitHandler: function (form) {

                    var _marketPrice = $("input[name='marketPrice']").val();

                    if ($("[name='specificationRadio']:checked").val() == "many") {
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
                        var parameter = $(this).find("input").serialize();
                        parameters.push(parameter);
                    });
                    if (!isRepeats) {
                        $specificationProductTable.find("tr:eq(1)").find("input").prop("disabled", true);
                        addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                        form.submit();
                    }
                }
            });

            var prevBarcode, prevPrice, prevStock, prevCost, prevMarketPrice;
            //切换规格类型
            $("[name='specificationRadio']").change(function () {
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
                    $('#specificationRadioTr').hide();
                }
            });

            //选择规格
            $specificationIds.click(function () {
                var $this = $(this);
                if ($specificationIds.filter(":checked").size() == 0) {
                    $specificationProductTable.find("tr:gt(1)").remove();
                }
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
                    if ($("#specificationTitle").val().trim() == "") {
                        $.message("error", "请输入规格名称");
                        return;
                    }
                    if ($("#specification1Ids").css("display") == "none") {
                        $("#specification1Ids").show();
                        $("#specification1Name").text($("#specificationTitle").val());
                        $("#specification1Title").val(v);
                    } else if ($("#specification2Ids").css("display") == "none") {
                        $("#specification2Ids").show();
                        $("#specification2Name").text($("#specificationTitle").val());
                        $("#specification2Title").val(v);
                    } else {
                        $.message("warn", "您不能再继续添加规格了");
                        return false;
                    }
                    $("#addSpecTr").hide();
                }
                if (type == "modify") {
                    var v = $("#specificationTitle").val();
                    var id = $("#specificationSelect :checkbox:checked").val();
                    $("#specification" + id + "Name").text(v);
                    $("#specificationProductTable tr:first .specification_" + id).text(v);
                    $("#specification" + id + "Title").val(v);
                    $("#addSpecTr").hide();
                }
            }

            //添加规格商品
            $addSpecificationProduct.click(function () {
            if ($specificationIds.filter(":checked").size() == 0) {
            $.message("warn", "${message("shop.product.specificationRequired")}");
            return false;
            }
                if ($specificationProductTable.find("tr:gt(1)").size() == 0) {
                    var $tr = $specificationProductTable.find("tr:eq(1)").clone().show().appendTo($specificationProductTable);
                    //$tr.find("td:first").text("当前规格");
                } else {
                    $specificationProductTable.find("tr").eq($specificationProductTable.find("tr").length - 1).clone().show().appendTo($specificationProductTable);
                }
            });

            $("#barcode").on("blur", function () {
                var barcodeValue = $("#barcode").val();
                if (barcodeValue != null && barcodeValue != "") {
                    $.ajax({
                        url: '${base}/store/barcode/get.jhtml',
                        type: 'post',
                        data: {barcode: barcodeValue},
                        dataType: 'JSON',
                        cache: false,
                        success: function (data) {
                            if (data.data != null) {
                                //名称
                                $("#name").val(data.data.name);
                                //单位
                                $("#unit").val(data.data.unitName);
                                //市场价
                                if (data.data.outPrice != null && data.data.outPrice > 0) {
                                    $("#price").val(data.data.outPrice);
                                } else {
                                    $("#price").val(null);
                                }
                                //商品详情
                                if (data.data.introduction != null) {
                                    editor.html(data.data.introduction);
                                } else {
                                    editor.html("");
                                }

                                if (data.data.descriptionapp != null) {
                                    editor_app.html(data.data.descriptionapp);
                                } else {
                                    editor_app.html("");
                                }
                                editor.sync();
                                editor_app.sync();
                                //图片
                                $productImageTable.empty();

                            [@compress single_line = true]
                                var varhtnl =
                                        '<tr>' +
                                        '<td colspan="4">' +
                                        '<a href="javascript:;"   onclick="addProductImageFunction ()"  id="addProductImage"' +
                                        'class="button">${message("shop.product.addProductImage")}<\/a>' +
                                        '<span>目前只支持jpg,jpeg,bmp,gif,pngG图片格式，图片大小不能超过2MB！<\/span>' +
                                        '<\/td>' +
                                        '<\/tr>' +
                                        '<tr class="title">' +
                                        '<th style="width:40%;">' +
                                        '${message("ProductImage.file")}' +
                                        '<\/th>' +
                                        '<th style="width:40%;">' +
                                        '${message("ProductImage.title")}' +
                                        '<\/th>' +
                                        '<th style="width:10%;">' +
                                        '${message("shop.common.order")}' +
                                        '<\/th>' +
                                        '<th style="width:10%;">' +
                                        '${message("shop.common.delete")}' +
                                        '<\/th>' +
                                        '<\/tr>';
                            [/@compress]
                                $productImageTable.append(varhtnl);
                                productImageIndex = 0;
                                for (item in data.data.productImages) {
                                    item = data.data.productImages[productImageIndex];
                                    if (item.title == null)
                                        item.title = "";
                                    if (item.source == null)
                                        item.source = "";
                                    if (item.order == null)
                                        item.order = "";
                                    var trHtml =
                                            '<tr>' +
                                            '<td>' +
                                            '<input type="hidden" name="productImages[' + productImageIndex + '].source" value="' + item.source + '"/>' +
                                            '<input type="hidden" name="productImages[' + productImageIndex + '].large" value="' + item.source + '"/>' +
                                            '<input type="hidden" name="productImages[' + productImageIndex + '].medium" value="' + item.source + '"/>' +
                                            '<input type="hidden" name="productImages[' + productImageIndex + '].thumbnail" value="' + item.source + '"/>' +
                                            '<img class="proImg" id="img' + productImageIndex + '" src="' + item.source + '" width="100px" height="100px" \/>' +
                                            '<input type="hidden" name="productImages[' + productImageIndex + '].local" value="' + item.source + '" maxlength="200" \/>' +
                                            '<input type="button" id="browserButton' + productImageIndex + '"  class="button"  value="选择" \/>' +
                                            '<\/td>' +
                                            '<td>' +
                                            '<input type="text" name="productImages[' + productImageIndex + '].title" class="text"  value="' + item.title + '"  maxlength="200" \/>' +
                                            '<\/td>' +
                                            '<td>' +
                                            '<input type="text" name="productImages[' + productImageIndex + '].order" class="text productImageOrder"   value="' + item.order + '"  maxlength="9" style="width: 50px;" \/>' +
                                            '<\/td>' +
                                            '<td>' +
                                            '<a href="javascript:;" class="deleteProductImage">[${message("shop.common.delete")}]<\/a>' +
                                            '<\/td>' +
                                            '<\/tr>';
                                    $productImageTable.append(trHtml);
                                    var $browserButton = $("#browserButton" + productImageIndex);
                                    settings.img = $("#img" + productImageIndex);
                                    $browserButton.browser(settings);
                                    productImageIndex++;
                                }
                            }
                        }
                    });

                }

            });
        });

        function deleteImage(thisTr){
            $(thisTr).closest("tr").remove();
        }

        function deleteSpec(thisTr){
            $(thisTr).closest("tr").remove();
        }
        function addProductImageFunction() {
            var len = $('#productImageTable tr').length;
            if (len > 9) {
                $.message("warn", "您不能在继续添加图片了");
                return;
            }
        [@compress single_line = true]
            var trHtml =
                    '<tr>' +
                    '<td>' +
                    '<img class="proImg" id="img' + productImageIndex + '" src="" width="100px" height="100px" \/>' +
                    '<input type="hidden" name="productImages[' + productImageIndex + '].local" maxlength="200" \/>' +
                    '<input type="button" id="browserButton' + productImageIndex + '"  class="button"  value="选择文件" \/>' +
                    '<\/td>' +
                    '<td>' +
                    '<input type="text" name="productImages[' + productImageIndex + '].title" class="form-control" maxlength="200" style="width: 80px;margin: 0 auto;"\/>' +
                    '<\/td>' +
                    '<td>' +
                    '<input type="text" name="productImages[' + productImageIndex + '].order" class="form-control" maxlength="9" style="width: 80px;margin: 0 auto;" \/>' +
                    '<\/td>' +
                    '<td>' +
                    '<a href="javascript:;" class="deleteProductImage" onclick="deleteImage(this)">[${message("shop.common.delete")}]<\/a>' +
                    '<\/td>' +
                    '<\/tr>';
        [/@compress]

            $("#productImageTable").append(trHtml);
            var $browserButton = $("#browserButton" + productImageIndex);
            settings.img = $("#img" + productImageIndex);
            settings.input = $("input[name='productImages[" + productImageIndex + "].local']");
            $browserButton.browser(settings);
            productImageIndex++;
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
            $("#txtBrand").click(function (evt) {
                stopEventBubble(evt);
            });
            $("#txtBrand").keyup(function () {
                searchBrand(this, "areaDl");
            }).focus(function () {
                $("#areaDl").show();
                $("#areaDl dd").show();
                $("#areaDl dt").hide();
            });
            $("#areaDl dd").click(function () {
                $("#txtBrand").val($(this).text());
                $("#txtBrand").attr("phonetic", $(this).attr("phonetic"));
                $("#txtBrand").attr("phonetid", $(this).attr("phonetid"));
                $("#xbrand").val("");

                if ($("#txtBrand").attr("phonetid") != null && $("#txtBrand").attr("phonetid") != "") {
                    branddataId = $("#txtBrand").attr("phonetid");
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
<div class="wrapper">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                我的商品
                <small>快去回复粉丝们咨询的问题，对您的诚信度有很大的帮助。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/product/isMarketableList.jhtml">我的商品</a></li>
                <li class="active">发布商品</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-solid">
                <div class="nav-tabs-custom" style="box-shadow: none;">
                    <ul class="nav nav-tabs pull-right">
                        <li class="active"><a href="${base}/store/member/product/isMarketableList.jhtml">发布商品</a></li>
                        <li><a href="${base}/store/member/product/addProductCategory.jhtml">我的商品</a></li>
                        <li class="pull-left header"><i class="fa fa-external-link-square"></i>发布商品</li>
                    </ul>
                </div>
                <div class="box-body">
                    <form id="inputForm1" class="hidden" action="" method="post" enctype="multipart/form-data">
                        <div class="list" style="border:0px solid #C6C9CA;">
                            <table class="input tabContent">
                                <tr>
                                    <td colspan="2">
                                        <div class="dl">
                                            <div class="dt">选择常用类目：</div>
                                            <div class="dd">
                                                <div class="nice-select" name="nice-select">
                                                    <input type="text" value="选择常用类目" readonly="">
                                                    <ul style="display: none;">
                                                        <li data-value="" treePath="" class="">
                                                            <span>选择常用类目</span>
                                                        </li>
                                                    [#list productCategoryMembers as productCategoryMember]
                                                        <li data-value="${productCategoryMember.productCategory.id}"
                                                            treePath="${productCategoryMember.productCategory.treePath}"
                                                            class="">
                                                    <span>[#if productCategoryMember.productCategory.parent?has_content][#if productCategoryMember.productCategory.parent.parent?has_content]${productCategoryMember.productCategory.parent.parent.name}
                                                        /[/#if]${productCategoryMember.productCategory.parent.name}
                                                        /[/#if]${productCategoryMember.productCategory.name}</span>
                                                            <a value="${productCategoryMember.id}" name="btn_delete"
                                                               class="btn_delete" href="javascript:;"></a>
                                                        </li>
                                                    [/#list]
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        您当前选择的是：
                                        <span id="show1" style="color:red;"></span>&nbsp;&nbsp;
                                        <input id="addCategoryMember" type="button" class="button" value="加为常用类目"
                                               onclick="addCategory()" hidefocus/>
                                    </td>
                                </tr>
                                <tr>
                                    <th width="80px">
                                        选择类目:
                                    </th>
                                    <td>
                                        <div id="pc_select" style="height: 277px;">
                                            <input type="hidden" id="productCategoryId1" name="productCategoryId1"
                                                   value="${productCategoryId}" treePath="${productCategory.treePath}"/>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                            <table class="input">
                                <tr>
                                    <th>
                                        &nbsp;
                                    </th>
                                    <td>
                                        <input disabled id="submitBtn" onclick="updateProductCategory()" type="button"
                                               class="button" value="发布宝贝" hidefocus/>
                                        请认真选择产品分类，这将影响到你的产品在用户分类搜索时的准确性。
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </form>
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

                        table.input th {
                            width: 80px;
                        }

                        .dl {
                            font-size: 14px;
                            position: relative;
                            padding-top: 10px;
                            margin-left: 10px;
                        }

                        .dl .dt {
                            position: absolute;
                            left: 0;
                            line-height: 38px;
                        }

                        .dl .dd {
                            padding-left: 100px;
                        }

                        .nice-select {
                            width: 450px;
                            padding: 0 10px;
                            height: 38px;
                            border: 1px solid #999;
                            position: relative;
                            box-shadow: 0 0 5px #999;
                            background: #fff url(${base}/resources/helper/images/a2.jpg) no-repeat right center;
                            cursor: pointer;
                            color: #555;
                            font-size: 14px;
                            font-family: "微软雅黑", "Microsoft Yahei";
                        }

                        .nice-select input {
                            display: block;
                            width: 100%;
                            height: 38px;
                            line-height: 38px \9;
                            border: 0;
                            outline: 0;
                            background: none;
                            cursor: pointer;
                            font-size: 14px;
                            font-family: "微软雅黑", "Microsoft Yahei";
                        }

                        .nice-select ul {
                            width: 100%;
                            display: none;
                            position: absolute;
                            left: -1px;
                            top: 39px;
                            overflow: hidden;
                            background-color: #fff;
                            max-height: 180px;
                            overflow-y: auto;
                            border: 1px solid #999;
                            border-top: 0;
                            box-shadow: 0 3px 5px #999;
                            z-index: 9999;
                        }

                        .nice-select ul li {
                            height: 30px;
                            line-height: 30px;
                            overflow: hidden;
                            padding: 0 10px;
                            cursor: pointer;
                            position: relative;
                        }

                        .nice-select ul li.on {
                            background-color: #e0e0e0;
                        }

                        .nice-select ul li span {
                            display: block;
                        }

                        .nice-select ul li a {
                            position: absolute;
                            top: 6px;
                            right: 10px;
                            cursor: pointer;
                            background-position: 0 -1px;
                            width: 18px;
                            height: 18px;
                            text-indent: -9999px;
                            overflow: hidden;
                            _text-indent: 0;
                            _font-size: 0;
                            _line-height: 0;
                            background: url(${base}/resources/helper/images/sicon_v5.png) no-repeat;
                            display: inline-block;
                        }

                        .nice-select ul li a:hover {
                            background-position: 0 -27px;
                        }

                    </style>
                    <script type="text/javascript">

                        $(function () {
                            var $productCategoryId1 = $("#productCategoryId1");
                        [@flash_message /]

                            $productCategoryId1.lSelectProductCategory({
                                url: "${base}/common/productCategory.jhtml",
                                fn: show
                            });

                            show();

                            $("#pc_select").on("click", "#search_btn0", function () {
                                $productCategoryId1.val("");
                                $productCategoryId1.attr("treePath", "");
                                var keyWord = $("#search_text0").val();
                                $productCategoryId1.lSelectProductCategory({
                                    url: "${base}/common/productCategory.jhtml",
                                    fn: show
                                });
                                show();
                                $("#search_text0").val(keyWord);
                                $.each($("#category_select0").find("option"), function (k, v) {
                                    var flag = false;
                                    if ($(v).text().indexOf(keyWord) >= 0) {
                                        flag = true;
                                    }
                                    if (!flag) {
                                        $(v).remove();
                                    }
                                });
                            }).on("click", "#search_btn1", function () {
                                var keyWord = $("#search_text1").val();
                                $("#category_select0").trigger("click");
                                $("#search_text1").val(keyWord);
                                $.each($("#category_select1").find("option"), function (k, v) {
                                    var flag = false;
                                    if ($(v).text().indexOf(keyWord) >= 0) {
                                        flag = true;
                                    }
                                    if (!flag) {
                                        $(v).remove();
                                    }
                                });
                            }).on("click", "#search_btn2", function () {
                                var keyWord = $("#search_text2").val();
                                $("#category_select1").trigger("click");
                                $("#search_text2").val(keyWord);
                                $.each($("#category_select2").find("option"), function (k, v) {
                                    var flag = false;
                                    if ($(v).text().indexOf(keyWord) >= 0) {
                                        flag = true;
                                    }
                                    if (!flag) {
                                        $(v).remove();
                                    }
                                });
                            }).on("keydown", ".search_text", function (event) {
                                var e = event || window.event || arguments.callee.caller.arguments[0];
                                if (e && e.keyCode == 13) {
                                    $(this).next().trigger("click");
                                }
                            });

                            $('[name="nice-select"]').find('input').click(function (e) {
                                if ($(this).next().css('display') == 'none') {
                                    $(this).next().show();
                                } else {
                                    $(this).next().hide();
                                }
                                e.stopPropagation();
                            });
                            $('[name="nice-select"] li').hover(function (e) {
                                $(this).toggleClass('on');
                                e.stopPropagation();
                            });
                            $('[name="nice-select"] li span').click(function (e) {
                                var val = $(this).text();
                                var dataVal = $(this).parent().attr("data-value");
                                var treePath = $(this).parent().attr("treePath");
                                $(this).parents('[name="nice-select"]').find('input').val(val);
                                $('[name="nice-select"] ul').hide();
                                $productCategoryId1.val(dataVal);
                                $productCategoryId1.attr("treePath", treePath);
                                $productCategoryId1.lSelectProductCategory({
                                    url: "${base}/common/productCategory.jhtml",
                                    fn: show
                                });
                                show();
                                $productCategoryId1.attr("treePath", null);
                                e.stopPropagation();
                            });
                            $(document).click(function (e) {
                                $('[name="nice-select"] ul').hide();
                                e.stopPropagation();
                            });

                            $(".btn_delete").click(function () {
                                var $this = $(this);
                                $.ajax({
                                    url: '${base}/helper/member/productCategoryMember/delete.jhtml',
                                    type: 'post',
                                    data: {ids: $this.attr("value")},
                                    dataType: 'json',
                                    success: function (data) {
                                        $.message(data);
                                        if (data.type == "success") {
                                            $this.parent().remove();
                                            show();
                                        }
                                    }
                                });

                            });

                        });

                        function addCategory() {
                            var id = $("#productCategoryId1").val();
                            if (id == "") {
                                $.message("error", "请选择商品类目");
                                return;
                            }
                            $.ajax({
                                url: '${base}/helper/member/productCategoryMember/add.jhtml',
                                type: 'post',
                                data: {id: id},
                                success: function (data) {
                                    $.message(data.message);
                                    if (data.message.type == "success") {
                                        $.each($("[name='nice-select'] ul li"), function (k, v) {
                                            if ($(v).attr("data-value") == data.data.productCategoryId) {
                                                $(v).remove();
                                            }
                                        });
                                        $("[name='nice-select'] ul li:first").after($("[name='nice-select'] ul li:last").clone(true).show());
                                        var $li = $("[name='nice-select'] ul li").eq(1);
                                        $li.attr("data-value", data.data.productCategoryId);
                                        $li.attr("treePath", data.data.productCategoryTreePath);
                                        $li.find("span").text(data.data.fullName);
                                        $li.find("a").attr("value", data.data.id);
                                        $("#addCategoryMember").attr("disabled", true).val("已加为常用类目");
                                    }
                                }
                            });
                        }

                        function show() {
                            var str1 = "";
                            var str2 = "";
                            var str3 = "";
                            var id = "";
                            $(".category_select option:selected").each(function (k, v) {
                                var $this = $(this);
                                if (k == 0) {
                                    str1 = $this.text();
                                    if (str1 != "") {
                                        id = $this.attr("value");
                                    }
                                }
                                if (k == 1) {
                                    str2 = $this.text();
                                    if (str2 != "") {
                                        id = $this.attr("value");
                                    }
                                }
                                if (k == 2) {
                                    str3 = $this.text();
                                    if (str3 != "") {
                                        id = $this.attr("value");
                                    }
                                }
                            });
                            var str = str1 == "" ? "" : (str1 + (str2 == "" ? "" : (">" + str2 + (str3 == "" ? "" : (">" + str3)))));
                            var strr = str1 == "" ? "" : (str1 + (str2 == "" ? "" : ("/" + str2 + (str3 == "" ? "" : ("/" + str3)))));
                            $("#show1").text(str);
                            $("#show1").attr("strr", strr);
                            var flag = true;
                            $.each($('[name="nice-select"] li:not(:first,:last)'), function () {
                                var $this = $(this);
                                var v = $this.attr("data-value");
                                if (v != "" && v == id) {
                                    flag = false;
                                }
                            });
                            if (!flag) {
                                $("#addCategoryMember").attr("disabled", true).val("已加为常用类目");
                            } else {
                                $("#addCategoryMember").attr("disabled", false).val("加为常用类目");
                            }
                            if ($(".category_select option:selected").size() == $(".category_select").size()) {
                                $("#submitBtn").attr("disabled", false);
                            } else {
                                $("#submitBtn").attr("disabled", true);
                            }
                        }

                        function updateProductCategory() {
                            $("#productCategoryId").val($("#productCategoryId1").val());
                            $("#productCategory").text($("#show1").attr("strr"));
                            $("#inputForm1").hide();
                            $("#inputForm").show();
                        }

                        function changForm() {
                            $("#inputForm1").show();
                            $("#inputForm").hide();
                        }

                    </script>
                    <form class="form-horizontal" role="form" id="inputForm" action="save.jhtml" method="post"
                          enctype="multipart/form-data">
                        <input type="hidden" id="productCategoryId" name="productCategoryId"
                               value="${productCategoryId}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品类目</label>
                            <div class="col-sm-8">
                                   <span id="productCategory">[#if productCategory.parent?has_content][#if productCategory.parent.parent?has_content]${productCategory.parent.parent.name}
                                       /[/#if]${productCategory.parent.name}/[/#if]${productCategory.name}</span>
                            [#--&nbsp;&nbsp;<a style="color: red;" href="javascript:changForm();">修改</a>--]
                                <br/><span style="color: darkgrey;">(商品上架后不可修改，请谨慎选择)</span>
                            </div>
                        </div>
                    [#if attributes?size > 0]
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品属性：</label>
                            <div class="col-sm-8">
                                <table id="attributeTable" class="input"
                                       style="border-top: 1px solid #dde9f5;table-layout: fixed;">
                                <tr>
                                    [#list attributes as attribute]
                                        <td style="text-align: right;">${attribute.name}:</td>
                                        <td>
                                            <select name="attribute_${attribute.id}">
                                                <option value="">${message("shop.common.choose")}</option>
                                                [#list attribute.options as option]
                                                    <option value="${option}">${option}</option>
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
                            </div>
                        </div>
                    [/#if]
                        <div class="form-group" id="productBarcode">
                            <label class="col-sm-2 control-label">条形码</label>
                            <div class="col-sm-8">
                                <input type="text" id="barcode" name="barcode" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品分类</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="productCategoryTenantId">
                                    <option value="">请选择</option>
                                [#list productCategoryTreeTenant as productCategoryTenant]
                                    <option value="${productCategoryTenant.id}">
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
                                <input type="text" name="name" id="name" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><span class="red">*</span>单位</label>
                            <div class="col-sm-5">
                                <input type="text" name="unit" class="form-control">
                            </div>
                            <div class="col-sm-3">计量单位,例:盒、件、箱、瓶。</div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品规格</label>
                            <div class="col-sm-9 pt5 label-m15">
                                <div class="radio">
                                    <label>
                                        <input type="radio" value="one" checked name="specificationRadio"
                                               style="margin-bottom: 5px;cursor: pointer;"/><span
                                            onclick="$(this).prev().click();" style="cursor: pointer;">统一规格</span>&nbsp;&nbsp;


                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" value="many" name="specificationRadio"
                                               style="margin-bottom: 5px;cursor: pointer;"/><span
                                            onclick="$(this).prev().click();" style="cursor: pointer;">多规格</span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="specificationRadioTr">
                            <label class="col-sm-2 control-label"></label>
                            <div class="col-sm-8">
                                <table class="table table-bordered table-striped"
                                       style="border-top: solid 1px #e0e0e0;border-bottom: solid 1px #e0e0e0;width: 100%">
                                    <tr>
                                        <td>
                                            <a href="javascript:;" id="addSpecificationProduct"
                                               class="button" style="text-align: left">增加规格商品</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table id="specificationProductTable"
                                                   class="table table-bordered table-striped"
                                                   style="border-top: solid 1px #e0e0e0;">
                                                <tr class="title">
                                                    <td><span>条形码</span></td>
                                                    <td>[#--选择规格--]
                                                        <input type="checkbox" name="specificationIds" value="1"
                                                               id="specification1Ids" class=""/>
                                                        <span class="specification_1">型号</span>
                                                        <input id="specification1Title" type="text"
                                                               style="width: 50px;"
                                                               name="specification1Title" value="型号"
                                                               oninput="changeSpec(this)" onchange="changeSpec(this)"/>
                                                    </td>
                                                    <td>
                                                        <input type="checkbox" name="specificationIds" value="2"
                                                               id="specification2Ids" class=""/>
                                                        <span class="specification_2">颜色</span>
                                                        <input id="specification2Title" type="text"
                                                               style="width: 50px;"
                                                               name="specification2Title" value="颜色"
                                                               oninput="changeSpec(this)" onchange="changeSpec(this)"/>
                                                    </td>
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
                                                    ${message("shop.common.handle")}
                                                    </td>
                                                </tr>
                                                <tr id="specTr">
                                                    <td>
                                                        <input type="text" name="barcodes" style="width: 80px;"/>
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
                                                    <td id="specTd">
                                                        <a href="javascript:;"
                                                           class="deleteSpecificationProduct" onclick="deleteSpec(this)">[删除]</a>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="form-group" id="productPrice">
                            <label class="col-sm-2 control-label"><span class="red">*</span>销售价</label>
                            <div class="col-sm-8">
                                <input type="text" id="price" name="price" class="form-control">
                            </div>
                        </div>
                        <div class="form-group" id="productCost">
                            <label class="col-sm-2 control-label"><span class="red"></span>成本价</label>
                            <div class="col-sm-8">
                                <input type="text" id="cost" name="cost" class="form-control">
                            </div>
                        </div>
                        <div class="form-group" id="productMarketPrice">
                            <label class="col-sm-2 control-label">市场价</label>
                            <div class="col-sm-8">
                                <input type="text" id="marketPrice" name="marketPrice" class="form-control">
                            </div>
                        </div>
                        <div class="form-group" id="productMemberPrice">
                            <label class="col-sm-2 control-label">会员价</label>
                            <div class="col-sm-8">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" id="isMemberPrice" name="isMemberPrice"
                                               value="true"/> ${message("admin.product.isMemberPrice")}
                                    </label>
                                </div>
                                <div class="row select-vip" id="memberPriceTr">
                                [#list memberRanks?sort_by(["id"]) as memberRank]
                                ${message("MemberRank."+memberRank.name)}: <input type="text"
                                                                                  id="memberPrice_${memberRank.id}"
                                                                                  name="memberPrice_${memberRank.id}"
                                                                                  class="text memberPrice"
                                                                                  maxlength="16"
                                                                                  style="width: 60px; margin-right: 6px;"
                                                                                  disabled="disabled"/>&nbsp;&nbsp;
                                [/#list]
                                </div>
                            </div>
                        </div>
                        <div class="form-group" id="productStock">
                            <label class="col-sm-2 control-label"><span class="red">*</span>库存</label>
                            <div class="col-sm-8">
                                <input type="text" id="stock" name="stock" class="form-control" value="">
                            </div>
                        </div>
                    [#if versionType==1]
                        <div class="form-group">
                            <label class="col-sm-2 control-label">品牌</label>
                            <div class="col-sm-4">
                                <select id="brandId" name="brandId" class="form-control">
                                    <option value="">${message("shop.common.choose")}</option>
                                    [#list brands as brand]
                                        <option value="${brand.id}">
                                        ${brand.name}
                                        </option>
                                    [/#list]
                                </select>
                            </div>
                        </div>
                    [/#if]
                        <div class="form-group">
                            <label class="col-sm-2 control-label">主供应商</label>
                            <div class="col-sm-8">
                                <select type="text" class="form-control" name="supplierId">
                                    <option value="">${message("shop.common.choose")}</option>
                                [#list suppliers as supplier]
                                    <option value="${supplier.parent.id}">${supplier.parent.name}</option>
                                [/#list]
                                </select>
                            </div>
                        </div>
                        <div class="hidden">
                            <label class="col-sm-2 control-label"><span class="red">*</span>最低起订单:</label>
                            <div class="col-sm-8">
                                <input type="text" name="stock" class="form-control" name="minReserve">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">商品图片</label>
                            <div class="col-sm-8">
                                <table class="table table-bordered table-striped mt10" id="productImageTable">
                                    <tr>
                                        <td colspan="4">
                                            <a href="javascript:;" id="addProductImage"
                                               onclick="addProductImageFunction ()"
                                               class="button">${message("shop.product.addProductImage")}</a>
                                            <span>目前只支持jpg,jpeg,bmp,gif,pngG图片格式，图片大小不能超过2MB！</span>
                                        </td>
                                    </tr>
                                    <tr class="title">
                                        <th>文件</th>
                                        <th>标题</th>
                                        <th>排序</th>
                                        <th>删除</th>
                                    </tr>
                                    <tr id="speciaTr">
                                        <td>
                                            <img class="proImg" id="img0" src=""
                                                 width="100px" height="100px"/>
                                            <input type="hidden" name="productImages[0].local" maxlength="200"/>
                                            <input type="button" id="browserButton0" class="button" value="选择文件"/>
                                        </td>
                                        <td><input type="text" class="form-control" name="productImages[0].title"
                                                   style="width: 80px;margin: 0 auto;"
                                                   value=""></td>
                                        <td><input type="text" name="productImages[0].order" class="form-control"
                                                   style="width: 80px;margin: 0 auto;"
                                                   value=""></td>
                                        <td><a class="deleteProductImage" onclick="deleteImage(this)">[删除]</a></td>
                                    </tr>
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
                                          style="width: 100%;"></textarea>
                                        </td>
                                    </tr>
                                    <tr id="appTr" style="display: none;">
                                        <td>
                                <textarea id="editor_app" name="descriptionapp" class="editor"
                                          style="width: 100%;"></textarea>
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
                                                       value="${tag.id}"
                                                /> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=2]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}"
                                                /> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=5]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}"
                                                /> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=24]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}"
                                                /> ${tag.name}
                                            </label>
                                        </div>
                                    [#elseif tag.id=31]
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="tagIds"
                                                       value="${tag.id}"
                                                /> ${tag.name}
                                            </label>
                                        </div>
                                    [/#if]

                                </label>
                            [/#list]
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">设置</label>
                            <div class="col-sm-8 ">
                                <label>
                                    <input type="hidden" name="isList" value="true"/>
                                    <input type="hidden" name="_isList" value="true"/>
                                </label>
                                <label>
                                    <input type="checkbox" name="isTop" value="true"/> ${message("Product.isTop")}
                                    <input type="hidden" name="_isTop" value="false"/>
                                </label>
                                <label>
                                    <input type="checkbox" name="isGift" value="true"/> ${message("Product.isGift")}
                                    <input type="hidden" name="_isGift" value="false"/>
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="memo" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">搜索关键词</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="keyword" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">页面标题</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="seoTitle" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">页面关键词</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="seoKeywords" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">页面描述</label>
                            <div class="col-sm-8 ">
                                <input type="text" name="seoDescription" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">上架时间</label>
                            <div class="col-sm-8 pt5 label-m15">
                                <div class="radio">
                                    <label>
                                        <input type="radio" value="true" checked name="isMarketable"
                                               style="margin-bottom: 5px;cursor: pointer;"/><span
                                            onclick="$(this).prev().click();" style="cursor: pointer;">立即上架</span>&nbsp;&nbsp;
                                    </label>
                                </div>
                                <div class="radio">
                                    <label>
                                        <input type="radio" value="false" name="isMarketable"
                                               style="margin-bottom: 5px;cursor: pointer;"/><span
                                            onclick="$(this).prev().click();" style="cursor: pointer;">暂不上架</span>
                                    </label>
                                </div>
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
                    <script type="text/javascript">

                        $(function () {
                            var $productCategoryId1 = $("#productCategoryId1");
                        [@flash_message /]

                            $productCategoryId1.lSelectProductCategory({
                                url: "${base}/common/productCategory.jhtml",
                                fn: show
                            });

                            show();

                            $("#pc_select").on("click", "#search_btn0", function () {
                                $productCategoryId1.val("");
                                $productCategoryId1.attr("treePath", "");
                                var keyWord = $("#search_text0").val();
                                $productCategoryId1.lSelectProductCategory({
                                    url: "${base}/common/productCategory.jhtml",
                                    fn: show
                                });
                                show();
                                $("#search_text0").val(keyWord);
                                $.each($("#category_select0").find("option"), function (k, v) {
                                    var flag = false;
                                    if ($(v).text().indexOf(keyWord) >= 0) {
                                        flag = true;
                                    }
                                    if (!flag) {
                                        $(v).remove();
                                    }
                                });
                            }).on("click", "#search_btn1", function () {
                                var keyWord = $("#search_text1").val();
                                $("#category_select0").trigger("click");
                                $("#search_text1").val(keyWord);
                                $.each($("#category_select1").find("option"), function (k, v) {
                                    var flag = false;
                                    if ($(v).text().indexOf(keyWord) >= 0) {
                                        flag = true;
                                    }
                                    if (!flag) {
                                        $(v).remove();
                                    }
                                });
                            }).on("click", "#search_btn2", function () {
                                var keyWord = $("#search_text2").val();
                                $("#category_select1").trigger("click");
                                $("#search_text2").val(keyWord);
                                $.each($("#category_select2").find("option"), function (k, v) {
                                    var flag = false;
                                    if ($(v).text().indexOf(keyWord) >= 0) {
                                        flag = true;
                                    }
                                    if (!flag) {
                                        $(v).remove();
                                    }
                                });
                            }).on("keydown", ".search_text", function (event) {
                                var e = event || window.event || arguments.callee.caller.arguments[0];
                                if (e && e.keyCode == 13) {
                                    $(this).next().trigger("click");
                                }
                            });

                            $('[name="nice-select"]').find('input').click(function (e) {
                                if ($(this).next().css('display') == 'none') {
                                    $(this).next().show();
                                } else {
                                    $(this).next().hide();
                                }
                                e.stopPropagation();
                            });
                            $('[name="nice-select"] li').hover(function (e) {
                                $(this).toggleClass('on');
                                e.stopPropagation();
                            });
                            $('[name="nice-select"] li span').click(function (e) {
                                var val = $(this).text();
                                var dataVal = $(this).parent().attr("data-value");
                                var treePath = $(this).parent().attr("treePath");
                                $(this).parents('[name="nice-select"]').find('input').val(val);
                                $('[name="nice-select"] ul').hide();
                                $productCategoryId1.val(dataVal);
                                $productCategoryId1.attr("treePath", treePath);
                                $productCategoryId1.lSelectProductCategory({
                                    url: "${base}/common/productCategory.jhtml",
                                    fn: show
                                });
                                show();
                                $productCategoryId1.attr("treePath", null);
                                e.stopPropagation();
                            });
                            $(document).click(function (e) {
                                $('[name="nice-select"] ul').hide();
                                e.stopPropagation();
                            });

                            $(".btn_delete").click(function () {
                                var $this = $(this);
                                $.ajax({
                                    url: '${base}/helper/member/productCategoryMember/delete.jhtml',
                                    type: 'post',
                                    data: {ids: $this.attr("value")},
                                    dataType: 'json',
                                    success: function (data) {
                                        $.message(data);
                                        if (data.type == "success") {
                                            $this.parent().remove();
                                            show();
                                        }
                                    }
                                });

                            });

                        });

                        function addCategory() {
                            var id = $("#productCategoryId1").val();
                            if (id == "") {
                                $.message("error", "请选择商品类目");
                                return;
                            }
                            $.ajax({
                                url: '${base}/helper/member/productCategoryMember/add.jhtml',
                                type: 'post',
                                data: {id: id},
                                success: function (data) {
                                    $.message(data.message);
                                    if (data.message.type == "success") {
                                        $.each($("[name='nice-select'] ul li"), function (k, v) {
                                            if ($(v).attr("data-value") == data.data.productCategoryId) {
                                                $(v).remove();
                                            }
                                        });
                                        $("[name='nice-select'] ul li:first").after($("[name='nice-select'] ul li:last").clone(true).show());
                                        var $li = $("[name='nice-select'] ul li").eq(1);
                                        $li.attr("data-value", data.data.productCategoryId);
                                        $li.attr("treePath", data.data.productCategoryTreePath);
                                        $li.find("span").text(data.data.fullName);
                                        $li.find("a").attr("value", data.data.id);
                                        $("#addCategoryMember").attr("disabled", true).val("已加为常用类目");
                                    }
                                }
                            });
                        }

                        function show() {
                            var str1 = "";
                            var str2 = "";
                            var str3 = "";
                            var id = "";
                            $(".category_select option:selected").each(function (k, v) {
                                var $this = $(this);
                                if (k == 0) {
                                    str1 = $this.text();
                                    if (str1 != "") {
                                        id = $this.attr("value");
                                    }
                                }
                                if (k == 1) {
                                    str2 = $this.text();
                                    if (str2 != "") {
                                        id = $this.attr("value");
                                    }
                                }
                                if (k == 2) {
                                    str3 = $this.text();
                                    if (str3 != "") {
                                        id = $this.attr("value");
                                    }
                                }
                            });
                            var str = str1 == "" ? "" : (str1 + (str2 == "" ? "" : (">" + str2 + (str3 == "" ? "" : (">" + str3)))));
                            var strr = str1 == "" ? "" : (str1 + (str2 == "" ? "" : ("/" + str2 + (str3 == "" ? "" : ("/" + str3)))));
                            $("#show1").text(str);
                            $("#show1").attr("strr", strr);
                            var flag = true;
                            $.each($('[name="nice-select"] li:not(:first,:last)'), function () {
                                var $this = $(this);
                                var v = $this.attr("data-value");
                                if (v != "" && v == id) {
                                    flag = false;
                                }
                            });
                            if (!flag) {
                                $("#addCategoryMember").attr("disabled", true).val("已加为常用类目");
                            } else {
                                $("#addCategoryMember").attr("disabled", false).val("加为常用类目");
                            }
                            if ($(".category_select option:selected").size() == $(".category_select").size()) {
                                $("#submitBtn").attr("disabled", false);
                            } else {
                                $("#submitBtn").attr("disabled", true);
                            }
                        }

                        function updateProductCategory() {
                            $("#productCategoryId").val($("#productCategoryId1").val());
                            $("#productCategory").text($("#show1").attr("strr"));
                            $("#inputForm1").hide();
                            $("#inputForm").show();
                        }

                        function changForm() {
                            $("#inputForm1").show();
                            $("#inputForm").hide();
                        }

                    </script>
                </div>
            </div>
    </div>
[#include "/store/member/include/footer.ftl" /]
</div>
</body>
</html>
