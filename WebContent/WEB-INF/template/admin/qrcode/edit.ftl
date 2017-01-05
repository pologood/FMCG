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
            var type = '0';
            $("input[name='mobile']").on("blur", function () {
                var _mobile = $("input[name='mobile']").val();
                if(_mobile.length!=11){
                    type = '2';
                    $("#validator").html("请输入正确的手机号码");
                    return;
                }
                $.ajax({
                    url: "${base}/admin/qrcode/exits.jhtml",
                    type: "POST",
                    data: {mobile:_mobile},
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if(data.type=="error"){
                            $("#validator").html("当前手机号码不是企业号码，请重新输入");
                            type = '1';
                        }
                    }
                });
            });

            $("input[type='submit']").on('click',function(){
                var _mobile = $("input[name='mobile']").val();

                if(_mobile.trim()==""||_mobile==null){
                    $("#validator").html("请输入手机号码");
                    return;
                }

                if(type=='1'){
                    $("#validator").html("当前手机号码不是企业号码，请重新输入");
                }else if(type=='2'){
                    $("#validator").html("请输入正确的手机号码");
                }else {
                    $.ajax({
                        url: "${base}/admin/qrcode/update/${qrcode.id}.jhtml",
                        type: "POST",
                        data: {mobile:_mobile},
                        dataType: "json",
                        cache: false,
                        success: function(data) {
                            $.message(data.type,data.content);
                            if(data.type=="success"){
                                location.href='${base}/admin/qrcode/list.jhtml'
                            }
                        }
                    });
                }
            });

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 绑定二维码
</div>
<input type="hidden" name="id" value="${qrcode.id}"/>
<table class="input">
    <tr>
        <th>
            <span class="requiredField">*</span>企业电话号码：
        </th>
        <td>
            <input type="text" id="mobile" name="mobile"
                   class="text" [#if qrcode.tenant??&&qrcode.tenant?has_content]
                   value="${qrcode.tenant.telephone}"[/#if] maxlength="200" onkeyup=""/>
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
                   onclick="location.href='${base}/admin/qrcode/list.jhtml'"/>
        </td>
    </tr>
</table>
</body>
</html>