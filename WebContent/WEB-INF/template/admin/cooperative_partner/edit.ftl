<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.admin.add")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link rel="shortcut icon" href="${base}/favicon.ico"/>
    <link href="${base}/resources/b2b/css/v2.0/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/v2.0/admin.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <style type="text/css">
        .members label {
            width: 150px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
            var $browserButton = $("#browserButton");
            var $browserButton1 = $("#browserButton1");

        [@flash_message /]
            $browserButton.browser();
            $browserButton1.browser();
            // 表单验证
            $("#inputForm").validate({
                rules: {
                    "name": {
                        required: true
                    },
                    "licensePhoto": {
                        required: true
                    }
                }
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 新增合作伙伴
</div>
<form id="inputForm" action="${base}/admin/cooperative/partner/update.jhtml" method="post" enctype="multipart/form-data">
	<input type="hidden" name="id" value="${cooperativePartner.id}">
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>名称:
            </th>
            <td>
                <input type="text" name="name" class="text" value="${cooperativePartner.name}"/>
            </td>
        </tr>
        <tr>
            <th>
                图片 :
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" name="licensePhoto" class="text" maxlength="200"
                               title="${message("admin.product.imageTitle")}" value="${cooperativePartner.licensePhoto}"/>
						<input type="button" id="browserButton" class="button"
                               value="${message("admin.browser.select")}"/>
					</span>

                <label style="color: red">[图片尺寸 133*50]</label>
            </td>
        </tr>
        <tr>
            <th>
                图片1 :
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" name="licensePhoto1" class="text" maxlength="200"
                               title="${message("admin.product.imageTitle")}" value="${cooperativePartner.licensePhoto1}"/>
						<input type="button" id="browserButton1" class="button"
                               value="${message("admin.browser.select")}"/>
					</span>

                <label style="color: red">[图片尺寸 133*50]</label>
            </td>
        </tr>
        <tr>
            <th>
			${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="order" class="text" value="${cooperativePartner.order}" maxlength="9"/>
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
</form>
</body>
</html>