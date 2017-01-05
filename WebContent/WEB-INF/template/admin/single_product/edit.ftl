<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.ad.edit")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>

    <style type="text/css">
        /*品牌列表样式*/
        .barcode_brand label {
            width: 360px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>

    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
        [@flash_message /]

            $.ajax({
                url: "${base}/admin/single/product/getProductOrTenant.jhtml",
                type: "POST",
                data: {
                    id: "${(singleProduct.product.id)!}"
                },
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.length == 0) {
                        return false;
                    }
                    var $brandstd = $("#productOrTenant2 td");
                    for (var i = 0; i < message.length; i++) {
                        $brandstd.append("<label><input type='radio' name='linkId' value='" + message[i].id + "' checked />" + message[i].name + "</label>");
                    }
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    title: "required",
                    adPositionId: "required",
                    path: "required",
                    order: "digits"
                },
                submitHandler: function (form) {
                    form.submit();
                }
            });

            $("#searchButton").click(function () {
                var _searchValue = $("#searchValue").val();
                if (_searchValue == null || _searchValue == "") {
                    $.message("warn", "请输入需要搜索的信息！");
                    return false;
                }

                $.ajax({
                    url: "${base}/admin/single/product/getProductOrTenant.jhtml",
                    type: "POST",
                    data: {
                        searchValue: _searchValue
                    },
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.length > 1) {
                            checked = "";
                        }
                        //console.log(message.length);
                        var $linkIds = $("input[name='linkId']");
                        var $brandstd = $("#productOrTenant2 td");
                        for (var i = 0; i < message.length; i++) {
                            var flag = true;
                            $linkIds.each(function () {
                                var $this = $(this);
                                if ($this.val() == message[i].id) {
                                    flag = false;
                                }
                            });
                            if (flag) {
                                $brandstd.append("<label><input type='radio' name='linkId' value='" + message[i].id + "'  />" + message[i].name + "</label>");
                            }
                        }
                    }
                });
            });

            $("#selectButton").click(function () {

                var $label = $(".barcode_brand td label");
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.ad.edit")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="id" value="${singleProduct.id}"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>标题:
            </th>
            <td>
                <input type="text" name="title" class="text" value="${singleProduct.title}" maxlength="200"/>
            </td>
        </tr>

        <tr id="productOrTenant1">
            <th>
                <span class="requiredField"></span>商品搜索：
            </th>
            <td>
                <div>
                    <input type="text" id="searchValue" name="searchValue"
                           class="text" maxlength="20" title="请输入商品信息"/>
                    &nbsp;&nbsp;
                    <input type="button" id="searchButton" class="button" value="查询"/>
                    <input type="button" id="selectButton" class="button" value="选择"/>
                    <span class="requiredField" id="brandValidator"></span>
                </div>
            </td>
        </tr>

        <tr id="productOrTenant2" class="barcode_brand" >
            <th>
                商品筛选：
            </th>
            <td>
            </td>
        </tr>

        <tr>
            <th>
                内容
            </th>
            <td>
                <textarea name="content" style="width: 300px;height: 100px">${singleProduct.content}</textarea>
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