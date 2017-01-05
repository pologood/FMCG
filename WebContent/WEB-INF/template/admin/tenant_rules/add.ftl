<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.main.tenantRules")} - Powered By rsico</title>
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
            $("input[name='lev']").change(function () {
                $("input[name='lev']").each(function () {
                    if ($(this).val() == "1") {
                        if ($(this).attr("checked") == "checked") {
                            $("#parentTr").hide();
                        } else {
                            $("#parentTr").show();
                        }
                    }
                });
            });

            // 表单验证
            $("#inputForm").validate({
                rules: {
                    name: "required",
                    lev: "required",
                    operTypes: "required",
                    url: "required",

                }
                ,
                submitHandler: function (form) {

                    var _pass = true;
                    $("input[name='lev']").each(function () {
                        if ($(this).val() != "1" && $(this).attr("checked") == "checked" && $("select[name='parent.id']").val() == "") {
                            $.message("error", "请选择上级！");
                            _pass = false;
                            return;
                        }
                    });
                    if (!_pass) {
                        return false;
                    }
                    // addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                    form.submit();
                }
            })
            ;

        })
        ;
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.main.tenantRules")}
</div>
<form id="inputForm" action="save.jhtml" method="post" enctype="multipart/form-data">
    <table class="input">
    [#--名称--]
        <tr>
            <th>
                <span class="requiredField">*</span>名称：
            </th>
            <td>
                <input type="text" id="name" name="name"
                       class="text"   maxlength="200" onkeyup=""/>
                &nbsp;&nbsp;
            </td>
        </tr>
    [#--等级--]
        <tr>
            <th>
                <span class="requiredField">*</span>等级：
            </th>
            <td>
                <input type="radio" name="lev" value="1" />一级
                &nbsp;&nbsp;
                <input type="radio" name="lev" value="2" checked="checked" />二级


            </td>
        </tr>
    [#--所属上级--]
        <tr id="parentTr" >
            <th>
                <span class="requiredField">*</span>所属上级：
            </th>
            <td>
                <select name="parent.id">
                    <option value=""
                            selected="selected"
                    >请选择
                    </option>
                [#list tenantRulesParent as perent]
                    <option value="${perent.id}" >${perent.name}</option>
                [/#list]
                </select>
                &nbsp;&nbsp;
            </td>
        </tr>
    [#--访问路径--]
        <tr>
            <th>
                <span class="requiredField">*</span>访问路径：
            </th>
            <td>
                <input type="text" id="url" name="url"
                       class="text"  maxlength="200" onkeyup=""/>
            </td>
        </tr>
    [#--操作--]
        <tr>
            <th>
                <span class="requiredField">*</span>操作：
            </th>
            <td>
            [#list types as type]
                <input type="checkbox" name="operTypes" value="${type}"
                />${message("helper.role."+type)}
            [/#list]
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