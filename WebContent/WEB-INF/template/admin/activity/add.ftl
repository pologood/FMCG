<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.product.add")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            $("#inputForm").validate({
                errorClass: "fieldError",
                ignoreTitle: true,
                rules: {
                    type: "required",
                    status: "required",
                    description: "required",
                    point:"required"
                }
            });


            $("#point").change(function(){
                $("#_rebacks").attr("name","point");
            });

            $("#amount").change(function(){
                $("#_rebacks").attr("name","amount");
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.activity.add")}
</div>
<form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data">

    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("ActivityRules.type")}:
            </th>
            <td>
                <select id="type" name="type">
                    <option value="">--请选择--</option>
                [#list ActivityRulesTypes as ActivityRulesType]
                    <option value="${ActivityRulesType}">
                    ${message("admin.activity.type."+ActivityRulesType)}
                    </option>
                [/#list]
                </select>
            </td>

        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("ActivityRules.status")}:
            </th>
            <td>
                <select id="status" name="status">

                [#list ActivityRulesStatus as ActivityRulesStatu]
                    <option value="${ActivityRulesStatu}" [#if ActivityRulesStatu=='enabled']selected [/#if]>
                    ${message("admin.activity.status."+ActivityRulesStatu)}
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("ActivityRules.description")}:
            </th>
            <td>
                <textarea name="description" class="text"></textarea>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("ActivityRules.reback")}:
            </th>
            <td>
                <label>
                    <input type="radio" id="point" checked name="reback"/>积分
                    <input type="radio" id="amount" name="reback"/>返现
                </label>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="text" id="_rebacks" name="point" class="text" maxlength="16"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ActivityRules.remarks")}:
            </th>
            <td>
                <input type="text" name="remarks" class="text"/>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="javascript:history.back();"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>