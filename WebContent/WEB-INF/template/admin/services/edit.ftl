<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.deliveryCorp.edit")} - Powered By rsico</title>
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
            $("input[type='submit']").click(function () {
                var _content = $("textarea[name='content']").val().trim();
                var _type = $("select[name='type'] option:selected").val();
                if (_content != "" && _content != null) {
                    // alert(_content+" "+_type);
                    $.ajax({
                        url: "${base}/admin/services/update.jhtml",
                        type: "POST",
                        data: {id: "${services.id}", content: _content, type: _type},
                        dataType: "json",
                        cache: false,
                        success: function (map) {
                            $.message(map.type, map.content);
                            if (map.type == "success") {
                                location.href = '${base}/admin/services/list.jhtml';
                            }
                        }
                    });
                } else {
                    $.message("warn", "您还没有填写备注信息");
                }
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 服务开通
</div>
<input type="hidden" name="id" value="${services.id}"/>
<table class="input">
    <tr>
        <th>
            <span class="requiredField">*</span>店铺名称：
        </th>
        <td>
            <input type="text" id="mobile" name="mobile"
                   class="text" [#if services.tenant??&&services.tenant?has_content]
                   value="${services.tenant.name}"[/#if] readonly/>
            &nbsp;&nbsp;
            <span class="requiredField" id="validator"></span>
        </td>
    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>联系方式：
        </th>
        <td>
            <input type="text" id="mobile" name="mobile"
                   class="text" [#if services.tenant??&&services.tenant?has_content]
                   value="${services.tenant.telephone}"[/#if] readonly/>
            &nbsp;&nbsp;
            <span class="requiredField" id="validator"></span>
        </td>
    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>申请服务：
        </th>
        <td>
            <input type="text" id="mobile" name="mobile" class="text"
                   value="${message("admin.services.Status." +services.status)}" readonly/>
            &nbsp;&nbsp;
            <span class="requiredField" id="validator"></span>
        </td>
    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>申请时间：
        </th>
        <td>
            <input type="text" id="mobile" name="mobile" class="text" value="${services.modifyDate}" readonly/>
            &nbsp;&nbsp;
            <span class="requiredField" id="validator"></span>
        </td>
    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>跟踪结果：
        </th>
        <td>
            <select name="type">
            [#list types as type]
                [#--[#if type=='success']--]
                [#--[#break /]--]
                [#--[/#if]--]
                <option [#if type==services.type]selected="selected" [/#if]
                        value="${type}">${message("admin.services.Type." + type)}</option>
            [/#list]
            </select>
        </td>
    </tr>

    <tr>
        <th>
            <span class="requiredField">*</span>添加备注：
        </th>
        <td>
            <textarea name="content" class="text" style="width: 300px; height: 60px;">${services.content}</textarea>
        </td>
    </tr>
    <tr>
        <th>
            &nbsp;
        </th>
        <td>
            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
            <input type="button" class="button" value="${message("admin.common.back")}"
                   onclick="location.href='${base}/admin/services/list.jhtml'"/>
        </td>
    </tr>
</table>
</body>
</html>