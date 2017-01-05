<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>${message("admin.product.add")} - Powered By rsico</title>
    <meta name="author" content="rsico Team" />
    <meta name="copyright" content="rsico" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
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
    </style>
    <script type="text/javascript">
        $().ready(function() {
            // 表单验证
            $("#inputForm").validate({
                rules: {
                    name: "required",
                },
                submitHandler: function (form) {
                    var isPass="请选择行业标签";
                    //检查行业标签
                    $("input[name='expertTagId']").each(function(){
                        if($(this).attr('checked')){
                            isPass="";
                            return;
                        }

                    })
                    if(isPass!=""){
                        $.message("error",isPass);
                        return false;
                    }
                    form.submit();
                }
            });

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 商家列表&raquo; 一键${message("admin.product.add")}
</div>
<form id="inputForm" action="oneKeySave.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${tenant.id}"/>
    <table class="input tabContent">

        <tr>
            <th>
                <span class="requiredField">*</span>商家名称：
            </th>
            <td>
                <input type="text" name="name" value="${tenant.name}" readonly="readonly"/>
            </td>
        </tr>

        <tr>
            <th>
                <span class="requiredField">*</span>行业标签：
            </th>
            <td>
            [#list expertTags as expertTag ]
                <input type="radio" name="expertTagId" value="${expertTag.id}"
                       [#if expertTag_index==0] checked="checked" [/#if]/>${expertTag.name}
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