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
            $("input[name='tenantMobile']").on("blur", function () {
                var _mobile = $("input[name='tenantMobile']").val();
                if(_mobile.length!=11){
                    type = '21';
                    $("#validator1").html("请输入正确的手机号码");
                    return;
                }
                $.ajax({
                    url: "${base}/admin/equipment/exits.jhtml",
                    type: "POST",
                    data: {mobile:_mobile},
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if(data.type=="error"){
                            $("#validator1").html("当前手机号码不是企业号码，请重新输入");
                            type = '11';
                        }
                    }
                });
            });

            $("input[name='storeMobile']").on("blur", function () {
                var _mobile = $("input[name='storeMobile']").val();
                if(_mobile.length!=11){
                    type = '22';
                    $("#validator2").html("请输入正确的手机号码");
                    return;
                }
                $.ajax({
                    url: "${base}/admin/equipment/exits.jhtml",
                    type: "POST",
                    data: {mobile:_mobile},
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if(data.type=="error"){
                            $("#validator2").html("当前手机号码不是企业号码，请重新输入");
                            type = '12';
                        }
                    }
                });
            });

            $("input[type='submit']").on('click',function(){
                var _tenantMobile = $("input[name='tenantMobile']").val();
                var _storeMobile = $("input[name='storeMobile']").val();
                if(_tenantMobile.trim()==""||_tenantMobile==null){
                    $("#validator1").html("请输入手机号码");
                    return;
                }

                if(_storeMobile.trim()==""||_storeMobile==null){
                    $("#validator2").html("请输入手机号码");
                    return;
                }

                if(type=='11'){
                    $("#validator1").html("当前手机号码不是企业号码，请重新输入");
                }else if(type=='21'){
                    $("#validator1").html("请输入正确的手机号码");
                }if(type=='12'){
                    $("#validator2").html("当前手机号码不是企业号码，请重新输入");
                }else if(type=='22'){
                    $("#validator2").html("请输入正确的手机号码");
                }else {
                    $.ajax({
                        url: "${base}/admin/equipment/update/${equipment.id}.jhtml",
                        type: "POST",
                        data: {
                            tenantMobile:_tenantMobile,
                            storeMobile:_storeMobile
                        },
                        dataType: "json",
                        cache: false,
                        success: function(data) {
                            if(data.type=="success"){
                                location.href='${base}/admin/equipment/list.jhtml'
                            }else{
                                alert("绑定失败");
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 绑定设备
</div>
<input type="hidden" name="id" value="${equipment.id}"/>
<table class="input">
    <tr>
        <th>
            <span class="requiredField">*</span>供货商电话号码：
        </th>
        <td>
            <input type="text" id="mobile" name="tenantMobile"
                   class="text" [#if equipment.tenant??&&equipment.tenant?has_content]
                   value="${equipment.tenant.telephone}"[/#if] maxlength="200" />
            &nbsp;&nbsp;
            <span class="requiredField" id="validator1"></span>
        </td>
    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>零售商电话号码：
        </th>
        <td>
            <input type="text" id="mobile" name="storeMobile"
                   class="text" [#if equipment.store??&&equipment.store?has_content]
                   value="${equipment.store.telephone}"[/#if] maxlength="200" />
            &nbsp;&nbsp;
            <span class="requiredField" id="validator2"></span>
        </td>
    </tr>
    <tr>
        <th>
            &nbsp;
        </th>
        <td>
            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
            <input type="button" class="button" value="${message("admin.common.back")}"
                   onclick="location.href='${base}/admin/equipment/list.jhtml'"/>
        </td>
    </tr>
</table>
</body>
</html>