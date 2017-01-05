<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.attribute.add")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var nameTenant = "",nameActivity="",couponLen= 1,activityLen= 1;
            var $inputForm = $("#inputForm");
            var $attributeTable = $("#attributeTable");
            var $addOption = $("#addOption");
            var $deleteOption = $("a.deleteOption");
        [@flash_message /]


            $("input[name='type']").change(function(){
                var _activityType=$("input[name='type']:checked").val();
                if(_activityType=="random"){
                    $("#adActivityTable").html("活动分类");
                    $("#couponTable").show();
                }else if(_activityType=="unionActivity"){
                    $("#adActivityTable").html("商品分类");
                    $("#couponTable").hide();
                }
            });

            // 增加可选项
            $addOption.live("click", function () {
                if(couponLen>=4){
                    $addOption.hide();
                }
            [@compress single_line = true]
                var trHtml =
                        '<tr class="optionTr">' +
                        '<th><span class="requiredField"></span></th>' +
                        '<td><input type="text" name="amounts" maxlength="3" style="width: 150px" placeholder="随机减金额"/>&nbsp;&nbsp;' +
                        '<input type="text" name="counts" maxlength="5" style="width: 150px" placeholder="优惠券库存"/>&nbsp;' +
                        '满<input type="text" name="minimumPrices" maxlength="6" style="width: 150px" placeholder="最低消费"/>元&nbsp;&nbsp;' +
                        '<a href="javascript:;" class="deleteOption">[${message("admin.common.delete")}]</a></td></tr>';
            [/@compress]
                $attributeTable.append(trHtml);
                couponLen++;
            });

            $("#addActivityTenant").live("click", function () {

                if ($("#_tenantName").val() == null || $("#_tenantName").val() == "") {
                    $.message("warn", "请输入您要添加的商家信息！");
                    return false;
                }

                $.ajax({
                    url: "${base}/admin/activity_planning/getTenant.jhtml",
                    type: "POST",
                    data: {keyword: $("#_tenantName").val()},
                    datetype: "json",
                    cache: false,
                    success: function (tenants) {
                        if (tenants.message.type != "success") {
                            $.message("error", "没有找到您所要的信息！");
                            return false;
                        }

                        if (tenants.data.length > 0) {
                            for (var i = 0; i < tenants.data.length; i++) {
                                if (!$.isRepeated(nameTenant, tenants.data[i].id)) {
                                    return false;
                                }

                                nameTenant += "," + tenants.data[i].id;
                                var trHtml =
                                        '<td style="float: left;border-bottom: none">' +
                                        '<input type="hidden" name="tenantIds" value="' + tenants.data[i].id + '">' +
                                        '<a href="' + tenants.data[i].thumbnail + '" target="_blank"><img src="' + tenants.data[i].thumbnail + '" width="25" height="25"></a>' +
                                        '<label style="padding:0 5px;">' + tenants.data[i].shortName + '</label>' +
                                        '<a href="javascript:;" class="deleteOption1">[${message("admin.common.delete")}]</a></td>';
                                $("#activityTenant").append(trHtml);
                            }
                        }else {
                            $.message("error", "没有找到您所要的信息！");
                        }

                    }
                });
            });

            $("#addActivityName").live("click", function () {

                if(activityLen>10){
                    $.message("warn", "活动最多只能添加十个！");
                    return false;
                }

                if ($("#_activityName").val() == null || $("#_activityName").val() == "") {
                    $.message("warn", "请输入您要添加的活动信息！");
                    return false;
                }
                if (!$.isRepeated(nameActivity, $("#_activityName").val())) {
                    return false;
                }

                nameActivity += "," + $("#_activityName").val();

                var trHtml =
                        '<td style="float: left;border-bottom: none">' +
                        '<input type="hidden" name="activityNames" value="' + $("#_activityName").val() + '">' +
                        '<label style="padding:0 5px;">' + $("#_activityName").val() + '</label>' +
                        '<a href="javascript:;" class="deleteOption2">[${message("admin.common.delete")}]</a></td>';
                $("#activityName").append(trHtml);
                activityLen++
            });

            $deleteOption.live("click", function () {
                couponLen--;
                if(couponLen<5){
                    $addOption.show();
                }
                var $this = $(this);
                $this.closest("tr").remove();
            });

            $("a.deleteOption1").live("click", function () {
                var $this = $(this);
                //console.log("开始：", nameTenant);
                var _value = $this.closest("td").find("input").attr("value");
                nameTenant = nameTenant.replace("," + _value, "");
                //console.log("结束：", nameTenant);
                $this.closest("td").remove();
            });

            $("a.deleteOption2").live("click", function () {
                activityLen--;
                var $this = $(this);
                var _value = $this.closest("td").find("input").attr("value");
                nameActivity = nameActivity.replace(","+_value, "");
                $this.closest("td").remove();
            });
            // 表单验证

            var _isActivityName="";
            $inputForm.validate({
                rules: {
                    name: "required",
                    beginDate: "required",
                    endDate: "required"
                    /*,amounts: {
                        required: true,
                        number: true,
                        min: 0
                    },
                    counts: {
                        required: true,
                        integer: true,
                        min: 0
                    },
                    minimumPrices: {
                        required: true,
                        number: true,
                        min: 0
                    }*/
                },
                submitHandler: function (form) {
                    if(_isActivityName!=""&&_isActivityName!="1"){
                        $.message("warn",_isActivityName);
                        return false;
                    }
                    /*var amountsValidate = true;
                    $.each($("[name='amounts']:gt(0)"), function () {
                        if ($(this).val().trim() == "") {
                            amountsValidate = false;
                        }
                    });
                    if (!amountsValidate) {
                        $.message("error", "随机减金额不能为空！");
                        return false;
                    }

                    var countsValidate = true;
                    $.each($("[name='counts']:gt(0)"), function () {
                        if ($(this).val().trim() == "") {
                            countsValidate = false;
                        }
                    });
                    if (!countsValidate) {
                        $.message("error", "优惠券库存不能为空！");
                        return false;
                    }

                    var minimumPricesValidate = true;
                    $.each($("[name='minimumPrices']:gt(0)"), function () {
                        if ($(this).val().trim() == "") {
                            minimumPricesValidate = false;
                        }
                    });
                    if (!minimumPricesValidate) {
                        $.message("error", "最低消费不能为空！");
                        return false;
                    }*/

                    form.submit();
                }
            });

            $.isRepeated = function (text, value) {
                var _isRepeated = "0";
                if(text != "") {
                    var obj = text.split(',');
                    if (obj.length > 0) {
                        for (var i = 0; i < obj.length; i++) {
                            if (obj[i] == value) {
                                _isRepeated = "1";
                            }
                        }
                        if(_isRepeated == "0"){
                            return true;
                        }else {
                            $.message("warn", "您已经添加过了！");
                            return false;
                        }
                    } else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            };

            $.isActivityName = function(name){

                if(name==null||name==""){
                    $.message("warn", "活动名称不可以为空！");
                    return false;
                }
                $.ajax({
                    url:'${base}/admin/activity_planning/isActivityName.jhtml',
                    data:{
                        name:name
                    },
                    type:'post',
                    dataType:'json',
                    success:function(dataBlock){
                        //console.log(dataBlock.type);
                        if(dataBlock.type=="error"){
                            $.message(dataBlock.type,dataBlock.content);
                            _isActivityName = dataBlock.content;
                        }else {
                            _isActivityName = "1";
                        }
                    }
                });
            };



        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo;添加活动
</div>
<form id="inputForm" action="save.jhtml" method="post" onkeypress="if(event.keyCode==13){return false;}">
    <div>
        <table id="attributeTable" class="input">
            <tr>
                <th>
                    <span class="requiredField">*</span>活动名称
                </th>
                <td colspan="2">
                    <input type="text" name="name" class="text" maxlength="20" onblur="$.isActivityName(this.value)"/>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>起始日期
                </th>
                <td colspan="2">
                    <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>结束日期
                </th>
                <td colspan="2">
                    <input type="text" id="endDate" name="endDate" class="text Wdate"
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
                </td>
            </tr>

            <tr>
                <th>
                    <span class="requiredField">*</span>活动状态
                </th>
                <td colspan="2">
                    <label>
                        <input type="radio" name="onOff" value="wait" checked="checked"/>待审核
                    </label>
                    <label>
                        <input type="radio" name="onOff" value="on" />启用
                    </label>
                    <label>
                        <input type="radio" name="onOff" value="off"/>停用
                    </label>
                </td>
            </tr>

            <tr>
                <th>
                    <span class="requiredField">*</span>优惠类型
                </th>
                <td colspan="2">
                    <label>
                        <input type="radio" name="type" value="random" checked="checked"/>随机减
                    </label>
                    <label>
                        <input type="radio" name="type" value="unionActivity" />联盟活动
                    </label>
                </td>
            </tr>
        </table>

        <table class="input" id="couponTable">
            <tr>
                <th>
                    <span class="requiredField">*</span>每人限单
                </th>
                <td colspan="2">
                    <label>
                        <input type="text" name="activityMaximumOrders" value="0"/>&nbsp;&nbsp;
                    </label>
                    <label style="color: red">
                        [每人每天限制订单笔数 默认为0不做限制]
                    </label>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>优惠策略
                </th>
                <td colspan="2">
                    &nbsp;&nbsp;&nbsp;<a href="javascript:;" id="addOption" class="button">添加</a>
                    最多可设置五个层级
                </td>
            </tr>
            <tr class="optionTr">
                <th>
                    <span class="requiredField"></span>
                </th>
                <td>
                    <input type="text" name="amounts" maxlength="3"
                           style="width: 150px" placeholder="随机减金额"/>&nbsp;
                    <input type="text" name="counts" maxlength="5"
                           style="width: 150px" placeholder="优惠券库存"/>
                    满<input type="text" name="minimumPrices" maxlength="6"
                            style="width: 150px" placeholder="最低消费"/>元
                </td>
            </tr>
        </table>

        <table class="input">
            <tr>
                <th>活动商家：</th>
                <td colspan="2">
                    <input type="text" id="_tenantName" name="_tenantName" placeholder="店铺名称或者店主信息" maxlength="200"
                           class="text"/>
                    <input type="button" id="addActivityTenant" class="button" value="添加"/>

                    <label style="color: red">
                        &nbsp;&nbsp;[请输入完整的店铺名称或者店主信息]
                    </label>
                </td>
            </tr>
            <tr>
                <th>&nbsp;</th>
                <td colspan="2">
                    <table id="activityTenant" class="input">

                    </table>
                </td>
            </tr>
        </table>

        <table class="input" >
            <tr>
                <th id="adActivityTable">活动分类</th>
                <td colspan="2">
                    <input type="text" id="_activityName" name="_activityName" placeholder="活动信息"
                           maxlength="200"
                           class="text"/>
                    <input type="button" id="addActivityName" class="button" value="添加"/>
                </td>
            </tr>
            <tr>
                <th>&nbsp;</th>
                <td colspan="2">
                    <table id="activityName" class="input">

                    </table>
                </td>
            </tr>
        </table>

        <table class="input">
            <tr>
                <th>活动规则：</th>
                <td colspan="2">
                    <textarea id="introduction" name="introduction" class="text"></textarea>
                </td>
            </tr>
        </table>
        <table class="input">
            <tr>
                <th>&nbsp;
                </th>
                <td>
                    <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                    <input type="button" class="button" value="${message("admin.common.back")}"
                           onclick="location.href='list.jhtml'"/>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>