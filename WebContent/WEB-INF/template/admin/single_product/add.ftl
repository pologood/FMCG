<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.ad.add")} - Powered By rsico</title>
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
            var $type = $("#type");
            var $contentTr = $("#contentTr");
            var $pathTr = $("#pathTr");
            var $path = $("#path");
            var $browserButton = $("#browserButton");
            var $communityId = $("#communityId");
            var $areaId = $("#areaId");

        [@flash_message /]
            $browserButton.browser();
            // 地区选择
            function getCommunity() {
                if ($areaId.val() > 0) {
                    $.ajax({
                        url: "get_community.jhtml",
                        type: "GET",
                        data: {areaId: $areaId.val()},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            var opt = '<option>--请选择--</option>';
                            $.each(data, function (i, v) {
                                opt += '<option value="' + v.id + '" lat="' + v.location.lat + '" lng="' + v.location.lng + '">' + v.name + '</option>';
                            });
                            $communityId.html(opt);
                        }
                    });
                }
            }

            $areaId.lSelect({
                url: "${base}/admin/common/area.jhtml",
                fn: getCommunity()
            });

            // "类型"修改
            $type.change(function () {
                if ($type.val() == "text") {
                    $contentTr.show();
                    $pathTr.hide();
                    $path.prop("disabled", true)
                } else {
                    $contentTr.hide();
                    $pathTr.show();
                    $path.prop("disabled", false);
                    $browserButton.unbind().browser({
                        type: $type.val()
                    });
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
                    if ((_linkType == "tenant" || _linkType == "product") && ($("input[name='linkId']").val() == '' || $("input[name='linkId']").val() == null)) {
                        $.message("error", "请选择商品");
                        return false;
                    }
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

                            ///console.log(flag, message[i].id);
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.ad.add")}
</div>
<form id="inputForm" action="save.jhtml" method="post">
<input type="hidden" name="singleProductPositionId" value="${(singleProductPositionId)!}"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>标题:
            </th>
            <td>
                <input type="text" name="title" class="text" maxlength="200"/>
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
                <textarea name="content" style="width: 300px;height: 100px"></textarea>
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