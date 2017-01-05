<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>应用延期 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>

    <script type="text/javascript">

        $().ready(
                function () {
                    //延期时间选择事件
                    $("select[name='time']").on('change', function () {
                        var _time = $("select[name='time']").val();
                    [#if application.validityDate??]
                        var testx=$dp.$DV(
                                '${application.validityDate?string("yyyy-MM-dd")}'
                                ,{y:parseInt(_time)});[/#if]
                        $("#datep").val(testx.y+"-"+testx.M+"-"+testx.d+" "+testx.H+":"+testx.m+":"+testx.s);
                        var x=1;
//
//                        var Year = _date.y + parseInt(_time); // 完整年
//                        var Month = ( _date.M).toString(); // 月
//                        var Day = (_date.d).toString(); // 日
//                        if (Month.length < 2)
//                            Month = "0" + Month;
//                        if (Day.length < 2)
//                            Day = "0" + Day;
//                        $("#datep").value = (Year + "-" + Month + "-" + Day);
                    });
                    //提交事件
                    $("input[type='submit']").on('click', function () {
                        var _time = $("#datep").val();
                        var _status = $("select[name='status']").val();
                        if (_time == null) {
                            $("#validator").html("请选择延期时间");
                            return;
                        }
                        $("#validator").html("");
                        $.ajax({
                            url: "${base}/admin/application/update/${application.id}.jhtml",
                            type: "POST",
                            data: {time: _time, status: _status},
                            dataType: "json",
                            cache: false,
                            success: function (data) {
                                if (data.type == "success") {
                                    location.href = '${base}/admin/application/list.jhtml';
                                } else {
                                    alert("延期失败");
                                }
                            }
                        });
                    });

                }
        );
    </script>
</head>

<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 应用延期
</div>
<input type="hidden" name="id" value="${application.id}"/>
<table class="input">
    <tr>
        <th>
            <span class="requiredField">*</span>延期时间：
        </th>
        <td>
            <select id="time" name="time" maxlength="200" onkeyup="">
                <option value="0">无</option>
                <option value="1">一年</option>
                <option value="2">二年</option>
                <option value="3">三年</option>
            </select>
            <input type="text" id="datep" name="datep" class="text Wdate"
                    value=
            [#if application.validityDate??]
                    "${application.validityDate?string("yyyy-MM-dd HH:mm:ss")}"
            [/#if]
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '${.now?date}'});"/>
            &nbsp;&nbsp;
            <span class="requiredField" id="validator"></span>
        </td>

    </tr>
    <tr>
        <th>
            <span class="requiredField">*</span>应用状态：
        </th>
        <td>

            <select id="status" name="status" maxlength="200" onkeyup="">
            [#list status as statu]

                <option value="${statu}"
                        [#if   statu==application.status]selected="selected"[/#if]>${message("Application.Status."+statu)}</option>
            [/#list]

            </select>
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
</body>
</html>