<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.Barcode.add")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <style type="text/css">
        /*品牌列表样式*/
        .barcode_brand label {
            width: 150px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>
    <script type="text/javascript">


        $().ready(function () {

            /**
             * 品牌搜索获得焦点
             */
            $("#brandname").focus(function(){
                queryLoadBrand("", "");
            });
            /**
             * 进行选择操作
             * */
            $("#selectButton").click(function(){

                var $label = $(".barcode_brand td label");
                $label.each(function () {
                    var $this = $(this);
                    if (!$this.find("input")[0].checked) {
                        $this.remove();
                    }
                });
            });
            $("#selectedbrand").click(function () {
                $("#selectButton").click();

                if ( $("#brandname").val().replace(/\s/g, "") == "") {
                    return;
                } else {
                    queryLoadBrand( $("#brandname").val(), "checked='checked'");
                }
            });
            $("#selectedAllbrand").click(function () {
                queryLoadBrand("", "");
            });
            /**
             *查询并加载品牌
             */
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
                        var $brandstd = $(".barcode_brand td");
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



            // 表单验证
            $("#inputForm").validate({
                rules: {
                    name: "required",
                    unitName: "required",
//                    brandId: "required",
                    barcode: {
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 13,
                            fraction: ${setting.priceScale}
                        }
                    },

                },
                submitHandler: function (form) {

                    //检查图片
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
                    var isPass="请选择行业标签";
                    //检查行业标签
                    $("input[name='tag.id']").each(function(){
                        if($(this).attr('checked')){
                            isPass="";
                            return;
                        }

                    })
                    if(isPass!=""){
                        $.message("error",isPass);
                        return false;
                    }
                    // addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                    form.submit();
                }
            });



            var productImageIndex = 1;
            var settings = {
                isSubmit: true

            };
            // 增加商品图片
            for (var i = 0; i < 6; i++) {
                var $browserButton = $("#browserButton" + i);
                settings.img = $("#img" + i);
                settings.input=$("input[name='productImages["+i+"].local']");
                $browserButton.browser(settings);
            }
            $("#addProductImage").click(function () {
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
                        '<input type="button" id="browserButton' + productImageIndex + '"  class="button"  value="选择" \/>' +
                        '<\/td>' +
                        '<td>' +
                        '<input type="text" name="productImages[' + productImageIndex + '].title" class="text" maxlength="200" \/>' +
                        '<\/td>' +
                        '<td>' +
                        '<input type="text" name="productImages[' + productImageIndex + '].order" class="text productImageOrder" maxlength="9" style="width: 50px;" \/>' +
                        '<\/td>' +
                        '<td>' +
                        '<a href="javascript:;" class="deleteProductImage">[${message("shop.common.delete")}]<\/a>' +
                        '<\/td>' +
                        '<\/tr>';
            [/@compress]
                $("#productImageTable").append(trHtml);
                var $browserButton = $("#browserButton" + productImageIndex);
                settings.img = $("#img" + productImageIndex);
                settings.input=$("input[name='productImages["+productImageIndex+"].local']");
                $browserButton.browser(settings);
                productImageIndex++;
            });

            // 删除商品图片
            $("a.deleteProductImage").live("click", function () {

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
            var $productCategoryId=$("#productCategoryId")
            $productCategoryId.change(function () {
                var hasValue = false;
//                $parameterTable.add($attributeTable).find(":input").each(function () {
//                    if ($.trim($(this).val()) != "") {
//                        hasValue = true;
//                        return false;
//                    }
//                });
                if (hasValue) {
                    $.dialog({
                        type: "warn",
                        content: "${message("admin.product.productCategoryChangeConfirm")}",
                        width: 450,
                        onOk: function () {
//                            loadParameter();
                            loadAttribute();
                            previousProductCategoryId = $productCategoryId.val();
                        },
                        onCancel: function () {
                            $productCategoryId.val(previousProductCategoryId);
                        }
                    });
                } else {
//                    loadParameter();
                    loadAttribute();
                    previousProductCategoryId = $productCategoryId.val();
                }
            });

// "类型"修改
            $('input[name="type"]').change(function(){
                var $this=$(this);
                if($this.val()=="0"){
                    $("#pcTr").show();
                    $("#appTr").hide();
                }else{
                    $("#pcTr").hide();
                    $("#appTr").show();
                }
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.Barcode.add")}
</div>
<form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data">
    [#--<input type="hidden" name="id" value="${barcode.id}"/>--]
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>条形码：
            </th>
            <td>
                <input type="text" id="barcode" name="barcode"
                       class="text" [#if barcode??&&barcode?has_content]
                       value="${barcode.barcode}"[/#if] maxlength="200" onkeyup=""/>
                &nbsp;&nbsp;
                <span class="requiredField" id="barcodeValidator"></span>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>商品名称：
            </th>
            <td>
                <input type="text" id="name" name="name"
                       class="text" [#if barcode??&&barcode?has_content]
                       value="${barcode.name}"[/#if] maxlength="200" onkeyup=""/>
                &nbsp;&nbsp;
                <span class="requiredField" id="nameValidator"></span>
            </td>
        </tr>

        <tr>
            <th>
                <span class="requiredField">*</span>行业标签：
            </th>
            <td>
            [#list expertTags as expertTag ]
                <input type="radio" name="tag.id" value="${expertTag.id}"/>${expertTag.name}
            [/#list]
            </td>
        </tr>

        <tr>
            <th>
            ${message("Product.productCategory")}:
            </th>
            <td>
                <select id="productCategoryId" name="productCategoryId">
                [#list productCategoryTree as productCategory]
                    <option value="${productCategory.id}"[#if productCategory == barcode.productCategory]
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
                <span class="requiredField">*</span>单位
            </th>
            <td>
                <input type="text" id="unitName" name="unitName"
                       class="text" [#if barcode??&&barcode?has_content]
                       value="${barcode.unitName}"[/#if] maxlength="200" onkeyup=""/>
                &nbsp;&nbsp;
                <span class="requiredField" id="barcodeValidator"></span>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField"></span>品牌搜索：
            </th>
            <td>
                <div>
                    <input type="text" id="brandname" name="brandname"
                           class="text" [#if barcode??&&barcode.brand??&&barcode.brand?has_content]
                           value="${barcode.brand.name}"[/#if] maxlength="200" onkeyup="" title="搜索结果并入品牌筛选中"/>
                    &nbsp;&nbsp;
                    <input type="button" id="selectedbrand" class="button" value="查询"/>
                    <input type="button" id="selectedAllbrand" class="button" value="全部"/>
                    <input type="button" id="selectButton" class="button" value="选择" />


                    <span class="requiredField" id="brandValidator"></span>
                </div>
            </td>

        </tr>
        <tr class="barcode_brand">
            <th>
            ${message("ProductCategory.brands")}:
            </th>
            <td>
            [#if barcode.brand??&&barcode.brand?has_content]
                <label>
                    <input type="radio" name="brandId" value="${barcode.brand.id}"
                           checked="checked"/>${barcode.brand.name}
                </label>
            [/#if]
            </td>
        </tr>
        <tr>
            <th>
                市场价：
            </th>
            <td>
                <input type="text" id="outPrice" name="outPrice"
                       class="text" [#if barcode??&&barcode?has_content]
                       value="${barcode.outPrice}"[/#if] maxlength="200" onkeyup=""/>
                &nbsp;&nbsp;
                <span class="requiredField" id="outPriceValidator"></span>
            </td>

        </tr>

    [#--商品详情--]
        <tr>
            <th>商品详情</th>
            <td>
                <table class="input" style="border-top: solid 1px #e0e0e0;">
                    <tr>
                        <td>
                            <input type="radio" name="type" id="radio1" value="0" style="margin-bottom: 5px;cursor: pointer;" checked="checked" /><span onclick="javascript:$(this).prev().click();" style="cursor: pointer;">电脑端</span>&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="type" id="radio2" value="1" style="margin-bottom: 5px;cursor: pointer;"/><span onclick="javascript:$(this).prev().click();" style="cursor: pointer;">手机端</span>
                        </td>
                    </tr>
                    <tr id="pcTr">
                        <td>
                                <textarea id="editor" name="introduction" class="editor"
                                          style="width: 100%;">${barcode.introduction?html}</textarea>
                        </td>
                    </tr>
                    <tr id="appTr" style="display: none;">
                        <td>
                                <textarea id="editor_app" name="descriptionapp" class="editor"
                                          style="width: 100%;">${barcode.descriptionapp?html}</textarea>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    [#--图片--]
        <tr>
            <th>
                <span class="requiredField">*</span>图片：
            </th>
            <td>
                <table id="productImageTable" class="input tabContent">
                    <tr>
                        <td colspan="4">
                            <a href="javascript:;" id="addProductImage"
                               class="button">${message("shop.product.addProductImage")}</a>
                            <span>最多添加8张图，目前只支持jpg,jpeg,bmp,gif,pngG图片格式，图片大小不能超过2MB！</span>
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
                [#if barcode.productImages[0]??]
                    [#list barcode.productImages as productImage ]
                        <tr>
                            <td>
                                <img class="proImg" id="img"${productImage_index}  src="${productImage.source}" width="100px" height="100px"/>
                                <input type="hidden" name="productImages[${productImage_index}].local"  value="${productImage.source}" maxlength="200"/>
                                <input type="button" id="browserButton${productImage_index}" class="button" value="选择"/>
                            </td>
                            <td>
                                <input type="text" name="productImages[${productImage_index}].title" value="${productImage.title}" class="text"
                                       maxlength="200"/>
                            </td>
                            <td>
                                <input type="text" name="productImages[${productImage_index}].order"  value="${productImage.order}"
                                       class="text productImageOrder"
                                       maxlength="9" style="width: 50px;"/>
                            </td>
                            <td>
                                <a href="javascript:;"  class="deleteProductImage">[删除]</a>
                            </td>
                        </tr>
                    [/#list]
                [#else]
                    <tr>
                        <td>
                            <img class="proImg" id="img0" src="" width="100px" height="100px"/>
                            <input type="hidden" name="productImages[0].local" maxlength="200"/>
                            <input type="button" id="browserButton0" class="button" value="选择"/>
                        </td>
                        <td>
                            <input type="text" name="productImages[0].title" class="text"
                                   maxlength="200"/>
                        </td>
                        <td>
                            <input type="text" name="productImages[0].order"
                                   class="text productImageOrder"
                                   maxlength="9" style="width: 50px;"/>
                        </td>
                        <td>
                            <a href="javascript:;"  class="deleteProductImage">[删除]</a>
                        </td>
                    </tr>

                [/#if]
                </table>
            </td>
        </tr>
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