<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>付款单 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/resources/helper/css/jquery.datetimepicker.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/ePayBank.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.datetimepicker.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            $("#submitbank").click(function () {
                var $this = $(this);
                $("#showObject").show();
                $.ajax({
                    url: "paybank.jhtml",
                    type: "POST",
                    data: {id:${credit.id}, gettype: "1"},
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        if (data.msgtype == "success") {
                            
                            try {
                                ocxCMBC.cmbcpost(data.xmlbody, data.posturl, data.xmlbyte,${credit.id} , "1");

                                var str = ocxCMBC.msg;
                                var obj = eval('(' + str + ')');
                                if (ocxCMBC.msg != "error") {
                                    $.ajax({
                                        url: "receivebank.jhtml",
                                        type: "GET",
                                        data: {
                                            id:${credit.id},
                                            code: obj.code,
                                            severity: obj.severity,
                                            message: obj.message,
                                            insId:${credit.sn},
                                            statusCode: obj.statusCode,
                                            statusSeverity: obj.statusSeverity,
                                            statusErrMsg: obj.statusErrMsg,
                                            receivetype: "pay"
                                        },
                                        dataType: "json",
                                        cache: false,
                                        success: function (message) {
                                            if (message.type == "success") {
                                                paytobank();
                                            } else {
                                                $("#showObject").hide();
                                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                            }
                                        },
                                        error: function () {
                                            $("#showObject").hide();
                                            window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                        }
                                    });
                                }
                                else {
                                    $("#showObject").hide();
                                    window.location.href = "${base}/admin/credit/receivebank.jhtml?id=${credit.id}&code=error&severity=error&message=error&insId=error&receivetype=pay";
                                }
                            }
                            catch (e) {
                                $("#showObject").hide();
                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                            }
                        }
                        else {
                            $("#showObject").hide();
                            //alert("paybank返回消息类型为失败");
                            window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                        }
                    },
                    error: function (data) {
                        alert(data.xmlbody);
                        $("#showObject").hide();
                        window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                    }
                });
            });


            $("#checkbank").click(function () {
                var $this = $(this);
                $("#showObject").show();
                $.ajax({
                    url: "paybank.jhtml",
                    type: "POST",
                    data: {id:${credit.id}, gettype: "1"},
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        if (data.msgtype == "success") {
                            try {
                                ocxCMBC.cmbcpost(data.xmlbody, data.posturl, data.xmlbyte,${credit.id} , "1");

                                if (ocxCMBC.msg != "error") {
                                    var str = ocxCMBC.msg;
                                    var obj = eval('(' + str + ')');
                                   
                                    $.ajax({
                                        url: "receivebank.jhtml",
                                        type: "GET",
                                        data: {
                                            id:${credit.id},
                                            code: obj.code,
                                            severity: obj.severity,
                                            message: obj.message,
                                            insId:${credit.sn},
                                            statusCode: obj.statusCode,
                                            statusSeverity: obj.statusSeverity,
                                            statusErrMsg: obj.statusErrMsg,
                                            receivetype: "query"
                                        },
                                        dataType: "json",
                                        cache: false,
                                        success: function (message) {
                                            if (message.type == "success") {
                                                $("#showObject").hide();
                                                
                                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                            }
                                            else {
                                                $("#showObject").hide();
                                               
                                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                            }
                                        },
                                        error: function () {
                                            $("#showObject").hide();
                                            
                                            window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                        }
                                    });
                                }
                                else {
                                    $("#showObject").hide();
                                    window.location.href = "${base}/admin/credit/receivebank.jhtml?id=${credit.id}&code=error&severity=error&message=error&insId=error&receivetype=query ";
                                }
                            }
                            catch (e) {
                                $("#showObject").hide();
                               
                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                            }
                        }
                        else {
                            $("#showObject").hide();
                            
                            window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                        }
                    },
                    error: function () {
                        $("#showObject").hide();
                        
                        window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                    }
                });

            });

            function paytobank() {
                var $this = $(this);
                $("#showObject").show();
                $.ajax({
                    url: "paybank.jhtml",
                    type: "POST",
                    data: {id:${credit.id}, gettype: "2"},
                    dataType: "json",
                    cache: false,
                    success: function (data) {
                        if (data.msgtype == "success") {
                            try {
                                ocxCMBC.cmbcpost(data.xmlbody, data.posturl, data.xmlbyte,${credit.id} , "2");
                                var str = ocxCMBC.msg;
                                var obj = eval('(' + str + ')');
                                if (ocxCMBC.msg != "error") {
                                    $.ajax({
                                        url: "receivebank.jhtml",
                                        type: "GET",
                                        data: {
                                            id:${credit.id},
                                            code: obj.code,
                                            severity: obj.severity,
                                            message: obj.message,
                                            insId:${credit.sn},
                                            receivetype: "query"
                                        },
                                        dataType: "json",
                                        cache: false,
                                        success: function (message) {
                                            if (message.type == "success") {
                                                $("#showObject").hide();
                                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                            }
                                            else {
                                                $("#showObject").hide();
                                                window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                            }
                                        },
                                        error: function () {
                                            $("#showObject").hide();
                                            window.location.href = "${base}/admin/credit/view.jhtml?id=${credit.id}";
                                        }
                                    });
                                }
                                else {
                                    $("#showObject").hide();
                                    window.location.href = "${base}/admin/credit/receivebank.jhtml?id=${credit.id}&code=error&severity=error&message=error&insId=error&receivetype=query";
                                }
                            }
                            catch (e) {
                                $("#showObject").hide();
                                window.location.href = "${base}/admin/credit/checkbank.jhtml?id=${credit.id}";
                            }
                        }
                        else {
                            $("#showObject").hide();
                            window.location.href = "${base}/admin/credit/checkbank.jhtml?id=${credit.id}";
                        }
                    },
                    error: function () {
                        $("#showObject").hide();
                        window.location.href = "${base}/admin/credit/checkbank.jhtml?id=${credit.id}";
                    }
                });
            };

            $("#postmessage").click(function () {
                $.ajax({
                    url: "postmessage.jhtml",
                    type: "POST",
                    data: {id:${credit.id}},
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        $.message("success", message.content);
                    }
                });
            });

            var bankInfo = getBankInfo("${(credit.bank)}");
            if (bankInfo != null) {
                $("#bankName").html(bankInfo.bankname);
            }
        [@flash_message /]

        });
    </script>
</head>

<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 付款单
</div>

<table class="input">
    <tr>
        <th>
            编号:
        </th>
        <td>
        ${credit.sn}
        </td>
        <th>
        ${message("admin.common.createDate")}:
        </th>
        <td>
        ${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}
        </td>
    </tr>
    <tr>
        <th>
            付款类型:
        </th>
        <td>
        ${message("Credit.Type." + credit.type)}
        </td>
        <th>
            付款方式:
        </th>
        <td>
        ${message("Credit.Method." + credit.method)}
        </td>
    </tr>
    <tr>
        <th>
            银行名称:
        </th>
        <td id="bankName">
        ${(credit.bank)!"-"}
        </td>
        <th>
            银行账号:
        </th>
        <td>
        ${credit.account}
        </td>
    </tr>
    <tr>
        <th>
            账户名称:
        </th>
        <td>
        ${(credit.payer)!"-"}
        </td>
        <th>
            金额:
        </th>
        <td>
        ${currency(credit.amount, true)}
        [#if credit.fee > 0]
            (手续费: ${currency(credit.fee, true)})
        [/#if]
        </td>
    </tr>
    <tr>
        <th>
            状态:
        </th>
        <td>
        ${message("Credit.Status." + credit.status)}
        </td>
        <th>
            付款日期:
        </th>
        <td>
        ${(credit.creditDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}
        </td>
    </tr>
    <tr>
        <th>
            申请人:
        </th>
        <td>
        ${(credit.member.name)!"-"}
        </td>
        <th>
            手机号:
        </th>
        <td>
        ${(credit.mobile)!"-"}
        </td>
    </tr>
    <tr>
        <th>
            所属店铺:
        </th>
        <td>
        [#if credit.member.tenant??]
        ${(credit.member.tenant.name)!"-"}
        [#else]
            未开通
        [/#if]
        </td>
        <th>
            店铺状态:
        </th>
        <td>
        [#if credit.member.tenant??]
            [#if credit.member.tenant.status=="success"]
                已开通
            [#elseif credit.member.tenant.status=="none"]
                未开通
            [#elseif credit.member.tenant.status=="wait"]
                待审核
            [#elseif credit.member.tenant.status=="fail"]
                已关闭
            [/#if]
        [#else]
            --
        [/#if]
        </td>
    </tr>
    <tr>
        <th>
            备注:
        </th>
        <td>
        ${credit.memo}
        </td>
        <th>
            操作人:
        </th>
        <td>
        ${(credit.operator)!"-"}
        </td>
    </tr>
    <tr>
        <th>
            &nbsp;
        </th>
        <td colspan="3">
            <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)"/>
            [#if credit.status == "wait"]
                <input type="button" class="button" name="submitbank" id="submitbank" value="提交银行"
                       onclick="javascript:{this.disabled=true;}"/>
                       
                <input type="button" class="button" value="人工汇款" id="artificialRemittance"/>

                <input type="button" class="button" value="撤消"
                       onclick="javascript:{this.disabled=true;};location.href='${base}/admin/credit/cancel.jhtml?id=${credit.id}'"/>
            [/#if]
            [#if credit.status == "wait_success"]
                <input type="button" class="button" name="checkbank" id="checkbank" value="到账检查"
                       onclick="javascript:{this.disabled=true;}"/>
                <input type="button" class="button" value="汇款记录" id="artificialRemittance"/>
            [/#if]
            [#if credit.status == "wait_failure"]
                <input type="button" class="button" value="手工退款"
                       onclick="javascript:{this.disabled=true;};location.href='${base}/admin/credit/failure.jhtml?id=${credit.id}'"/>
            [/#if]
            <br>
            <br>
            <!--<input type="button" class="button" name="postmessage" id="postmessage" value="发送手工短信通知"
                   onclick="javascript:{this.disabled=true;}"/>-->

            <input type="button" class="button" id="showBtn" value="账单列表"/>
        </td>
    </tr>
</table>

<div><a href="#" style="color:red;">${msg}</a></div>
<div id="showObject" style="display:none;">
    <OBJECT
            classid="clsid:9585E556-CFC5-40A2-B759-E010D7679EBC"
            codebase="http://127.0.0.1:8080/mold/myActiveFormProj1.ocx#version=1,0,0,0"
            id="ocxCMBC"
            name="ocxCMBC"
            width="400"
            height="70"
            align="center"
            hspace="0"
            vspace="0"
    >
    </OBJECT>
</div>

<script>
    //显示隐藏账单列表
    $(function () {
        $('#showBtn').click(function () {
            $('#showTable').toggle();
        });


        function checkRate(input,type)
        {
            var re = type==0?/^[0-9]+.?[0-9]*$/:/^[\u2E80-\u9FFF]+$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/

            if (!re.test(input))
            {
                return false;
            }
            return true;
        }


        $("#artificialRemittance").click(function () {
            $.dialog({
                title: "人工汇款",
            [@compress single_line = true]
                content: '<form id="remittanceFrom" action="success.jhtml" method="get">' +
                '<input type="hidden" name="id" value="${credit.id}" />' +
                '<table class="input">' +
                '<tr><th style="text-align: right;"><span class="requiredField">*<\/span>汇款银行:<\/th><td><input type="text" name="bankName" value="${(credit.remittance.bankName)!}"><label id="bankNameTest" class="fieldError"><\/label><\/td><\/tr>' +
                '<tr><th style="text-align: right;"><span class="requiredField">*<\/span>帐号:<\/th><td><input type="text" name="bankCode" value="${(credit.remittance.bankCode)!}"><label id="bankCodeTest" class="fieldError"><\/label><\/td><\/tr>' +
                '<tr><th style="text-align: right;"><span class="requiredField">*<\/span>金额:<\/th><td><input type="text" name="amount" value="${(credit.remittance.amount)!}"><label id="amountTest" class="fieldError"><\/label><\/td><\/tr>' +
                '<tr><th style="text-align: right;"><span class="requiredField">*<\/span>凭证流水:<\/th><td><input type="text" name="documentFlow" value="${(credit.remittance.documentFlow)!}"><label id="documentFlowTest" class="fieldError"><\/label><\/td><\/tr>' +
                '<tr><th style="text-align: right;"><span class="requiredField">*<\/span>经办人:<\/th><td><input type="text" name="acntToName" value="${(credit.remittance.acntToName)!}"><label id="acntToNameTest" class="fieldError"><\/label><\/td><\/tr>' +
                '<tr><th style="text-align: right;"><span class="requiredField">*<\/span>付款时间:<\/th><td><input class="text" value="${(credit.remittance.remittanceDate?string("yyyy-MM-dd HH:mm"))!}" type="text" name="date" id="datetimepicker_begin" readonly="true" maxlength="200"/><label id="remittanceDateTest" class="fieldError"><\/label><\/td><\/tr>' +
                '<tr><th style="text-align: right;">备注:<\/th><td><textarea name="memo" class="text">${(credit.remittance.memo)!}<\/textarea><\/td><\/tr>' +
                '<\/table><\/form>',
            [/@compress]
                width: 470,
                modal: true,
                ok: [#if credit.status!='wait']null[#else ]"${message("admin.dialog.ok")}"[/#if],
                cancel: "${message("admin.dialog.cancel")}",
                onShow: function () {
                    $('#datetimepicker_begin').datetimepicker();
                },
                onOk: function () {

                    if($("input[name='bankName']").val().trim()==null||$("input[name='bankName']").val().trim()==""){
                        $("#bankNameTest").text("必填");
                        $("input[name='bankName']").addClass("fieldError");
                        return false;
                    }else if(!checkRate($("input[name='bankName']").val().trim(),1)){
                        $("#bankNameTest").text("请输入正确的汇款银行");
                        $("input[name='bankName']").addClass("fieldError");
                        return false;
                    }else {
                        $("#bankNameTest").text("");
                        $("input[name='bankName']").removeClass("fieldError");
                    }

                    if($("input[name='bankCode']").val().trim()==null||$("input[name='bankCode']").val().trim()==""){
                        $("#bankCodeTest").text("必填");
                        $("input[name='bankCode']").addClass("fieldError");
                        return false;
                    }else if(!checkRate($("input[name='bankCode']").val().trim(),0)){
                        $("#bankCodeTest").text("请填写正确的银行卡号");
                        $("input[name='bankCode']").addClass("fieldError");
                        return false;
                    }
                    else {
                        $("#bankCodeTest").text("");
                        $("input[name='bankCode']").removeClass("fieldError");
                    }

                    if($("input[name='amount']").val().trim()==null||$("input[name='amount']").val().trim()==""){
                        $("#amountTest").text("必填");
                        $("input[name='amount']").addClass("fieldError");
                        return false;
                    }else if(!checkRate($("input[name='amount']").val().trim(),0)){
                        $("#amountTest").text("请填写正确的金额");
                        $("input[name='amount']").addClass("fieldError");
                        return false;
                    }else {
                        $("#amountTest").text("");
                        $("input[name='amount']").removeClass("fieldError");
                    }

                    if($("input[name='documentFlow']").val().trim()==null||$("input[name='documentFlow']").val().trim()==""){
                        $("#documentFlowTest").text("必填");
                        $("input[name='documentFlow']").addClass("fieldError");
                        return false;
                    }else {
                        $("#documentFlowTest").text("");
                        $("input[name='documentFlow']").removeClass("fieldError");
                    }

                    if($("input[name='acntToName']").val().trim()==null||$("input[name='acntToName']").val().trim()==""){
                        $("#acntToNameTest").text("必填");
                        $("input[name='acntToName']").addClass("fieldError");
                        return false;
                    }else {
                        $("#acntToNameTest").text("");
                        $("input[name='acntToName']").removeClass("fieldError");
                    }

                    if($("input[name='date']").val().trim()==null||$("input[name='date']").val().trim()==""){
                        $("#remittanceDateTest").text("必填");
                        $("input[name='date']").addClass("fieldError");
                        return false;
                    }else {
                        $("#remittanceDateTest").text("");
                        $("input[name='date']").removeClass("fieldError");
                    }

                    $("#remittanceFrom").submit();
                }
            });
        });

    });
</script>

<div id="showTable" style="display:none;">
    <table class="input">
        <tr>
            <td colspan="6"><h3>账单列表(${size}条)</h3></td>
        </tr>
    </table>
    <div style=" max-height:500px; overflow-y:auto;">
        <table class="input">
            <tr>
                <th style="text-align:center;">日期</th>
                <th style="text-align:center;">类型</th>
                <th style="text-align:center;">收入金额</th>
                <th style="text-align:center;">支出金额</th>
                <th style="text-align:center;">当前金额</th>
                <th style="text-align:center;">摘要</th>
            </tr>
        [#list depositList as deposit]
            <tr[#if !deposit_has_next] class="last"[/#if]>
                <td style="text-align:center;">
                    <span title="${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}">${deposit.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                </td>
                <td style="text-align:center;">
                ${message("Deposit.Type." + deposit.type)}
                </td>
                <td style="text-align:center;">
                ${currency(deposit.credit,true)}
                </td>
                <td style="text-align:center;">
                ${currency(deposit.debit,true)}
                </td>
                <td style="text-align:center;">
                ${currency(deposit.balance,true)}
                </td>
                <td style="text-align:center;">
                    [#if deposit.memo??]
                        [#if deposit.memo?index_of("单号")!=-1]
                            [#assign from=deposit.memo?index_of(":")]
                            [#assign snkey=deposit.memo?substring(from+1)]${deposit.memo}
                            <a href="${base}/admin/order/snview.jhtml?sn=${snkey}" target="_blank"></a>
                        [#else]
                        ${deposit.memo}
                        [/#if]
                    [/#if]
                </td>
            </tr>
        [/#list]
        </table>
    [#if !depositList?has_content]
        <p>${message("helper.member.noResult")}</p>
    [/#if]
    </div>
</div>

</body>
</html>