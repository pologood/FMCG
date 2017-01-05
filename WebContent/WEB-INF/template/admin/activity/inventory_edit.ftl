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
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.activity.add")}
</div>
<form id="inputForm" action="${base}/admin/activity/inventory/update.jhtml" method="post" enctype="multipart/form-data">
	<input type="hidden" name="id" value="${(activityInventory.id)!}">
    <table class="input">
        <tr>
            <th>
                ${message("ActivityInventory.tenant")}:
            </th>
            <td>
                <label>${(activityInventory.tenant.name)!}</label>
            </td>
        </tr>

        <tr>
            <th>
                ${message("ActivityInventory.tenant.member")}:
            </th>
            <td>
                <label>${(activityInventory.tenant.member.displayName)!}</label>
            </td>
        </tr>

        <tr>
            <th>
                ${message("ActivityInventory.tenant.mobie")}:
            </th>
            <td>
                <label>${(activityInventory.tenant.member.username)!}</label>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ActivityInventory.tenant.mobie")}:
            </th>
            <td>
                <label>${(activityInventory.tenant.member.username)!}</label>
            </td>
        </tr>
        <tr>
            <th>
            ${message("ActivityInventory.description")}:
            </th>
            <td>
                <label>${(activityInventory.description)!}</label>
            </td>
        </tr>
        <tr>
            <th>
                ${message("ActivityInventory.point")}:
            </th>
            <td>
                <label>${(activityInventory.point)!}</label>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField">*</span>${message("ActivityInventory.Status")}
            </th>
            <td>
                <select id="status" name="status">
                [#list ActivityInventoryStatus as ActivityInventoryStatu]
                    <option value="${ActivityInventoryStatu}" [#if ActivityInventoryStatu==activityInventory.status]selected[/#if]>
                    ${message("admin.Inventory.status."+ActivityInventoryStatu)}
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
			${message("ActivityRules.remarks")}:
            </th>
            <td>
                <textarea type="text" name="remarks"  class="text">${(activityInventory.remarks)!}</textarea>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input style="display: [#if activityInventory.status=="success"]none[/#if]" type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="javascript:history.go(-1);"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>