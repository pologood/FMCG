<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>绑定二维码 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var type = '0';

            $("input[type='submit']").on('click', function () {
                var _mobile = $("input[name='mobile']").val();
                var _qrcodeId = $("#qrcodeId").val();

                if (_qrcodeId.trim() == "" || _qrcodeId == null) {
                    $("#validator").html("请输选择二维码");
                    return;
                }
                $.ajax({
                    url: "${base}/admin/qrcode/update/" + _qrcodeId + ".jhtml",
                    type: "POST",
                    data: {mobile: _mobile},
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        $.message(data.type, data.content);
                        if (data.type == "success") {
                            location.href = '${base}/admin/tenant/list.jhtml'
                        }
                    }
                });
            });

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 绑定二维码
</div>
<input type="hidden" name="mobile" value="${tenant.telephone}"/>
<table class="input">
    <tr>
        <th>
            商家：
        </th>
        <td>
            <a>${tenant.name}</a>
        </td>
    </tr>
    <tr>
        <th>
            企业电话号码：
        </th>
        <td>
           <a>${tenant.telephone}</a>
        </td>
    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>二维码：
        </th>
        <td>
            <select  class="text"   id="qrcodeId" name="id" >
                <option value="">请选择</option>
                [#list qrcodes as qrItem]
                    <option value="${qrItem.id}">${qrItem.id}</option>
                [/#list]
            </select>
            &nbsp;&nbsp;
            <span class="requiredField" id="validator"></span>
        </td>
    </tr>
    <tr>
        <th>
            &nbsp;
        </th>
        <td>
            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
            <input type="button" class="button" value="${message("admin.common.back")}"
                   onclick="history.go(-1)"/>
        </td>
    </tr>
</table>
</body>
</html>