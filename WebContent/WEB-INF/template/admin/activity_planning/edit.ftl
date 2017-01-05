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

            var nameTenant = "", nameActivity = "", couponLen = 1, activityLen = 1, delCoupon = "", delActivity = "", delSingleProduct = "", delTenant = "";

        [#list plan.tenants as tenant]
            nameTenant += ",${tenant.id}";
        [/#list]

        [#list plan.coupons as counon]
            couponLen++;
        [/#list]

        [#if plan.type=="random"]
            [#list plan.adPositions as adPosition]
                nameActivity += ",${(adPosition.description)!}" +;
            [/#list]
        [#elseif plan.type=="unionActivity"]
            [#list plan.singleProductPositions as singleProductPosition]
                nameActivity += ",${(singleProductPosition.name)!}";
            [/#list]
        [/#if]

            var $inputForm = $("#inputForm");
            var $attributeTable = $("#attributeTable");
            var $addOption = $("#addOption");
            var $deleteOption = $("a.deleteOption");
        [@flash_message /]

            // 增加可选项
            $addOption.live("click", function () {
                if (couponLen >= 5) {
                    $addOption.hide();
                }

            [@compress single_line = true]
                var trHtml =
                        '<tr class="optionTr">' +
                        '<th><span class="requiredField"></span></th>' +
                        '<td>' +
                        '<input type="hidden" name="couponIds" value="0">' +
                        '<input type="text" name="amounts" maxlength="3" style="width: 150px" placeholder="随机减金额"/>&nbsp;&nbsp;' +
                        '<input type="text" name="counts" maxlength="5" style="width: 150px" placeholder="优惠券库存"/>&nbsp;' +
                        '满<input type="text" name="minimumPrices" maxlength="6" style="width: 150px" placeholder="最低消费"/>元&nbsp;&nbsp;' +
                        '<a href="javascript:;" class="deleteOption">[${message("admin.common.delete")}]</a></td></tr>';
            [/@compress]
                $attributeTable.append(trHtml);
                couponLen++;
            });

            $("#addActivityTenant").live("click", function () {

                if ($("#_tenantName").val().trim() == null || $("#_tenantName").val().trim() == "") {
                    $.message("warn", "请输入您要添加的商家信息！");
                    return false;
                }

                $.ajax({
                    url: "${base}/admin/activity_planning/getTenant.jhtml",
                    type: "POST",
                    data: {keyword: $("#_tenantName").val().trim()},
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
                        } else {
                            $.message("error", "没有找到您所要的信息！");
                        }

                    }
                });
            });

            $("#addActivityName").live("click", function () {

                if (activityLen > 10) {
                    $.message("warn", "活动最多只能添加十个！");
                    return false;
                }

                if ($("#_activityName").val().trim() == null || $("#_activityName").val().trim() == "") {
                    $.message("warn", "请输入您要添加的活动信息！");
                    return false;
                }
                if (!$.isRepeated(nameActivity, $("#_activityName").val().trim())) {
                    return false;
                }

                nameActivity += "," + $("#_activityName").val().trim();

                var trHtml =
                        '<td style="float: left;border-bottom: none">' +
                        '<input type="hidden" name="activityIds" value="0">' +
                        '<input type="hidden" name="activityNames" value="' + $("#_activityName").val().trim() + '">' +
                        '<label style="padding:0 5px;">' + $("#_activityName").val().trim() + '</label>' +
                        '<a href="javascript:;" class="deleteOption2">[${message("admin.common.delete")}]</a></td>';
                $("#activityName").append(trHtml);

                activityLen++;
            });

            $deleteOption.live("click", function () {
                couponLen--;
                if (couponLen < 6) {
                    $addOption.show();
                }
                var $this = $(this);


                var _value = $this.closest("td").find("input").attr("value");


                if (_value != "0") {
                    delCoupon += _value + ",";
                }
                $("input[name='delCoupon']").val(delCoupon);
                $this.closest("tr").remove();
            });

            $("a.deleteOption1").live("click", function () {
                var $this = $(this);
                var _value = $this.closest("td").find("input").attr("value");
                nameTenant = nameTenant.replace("," + _value, "");

                if (_value != "0") {
                    delTenant += _value + ",";
                }
                $("input[name='delTen']").val(delTenant);
                $this.closest("td").remove();
            });

            $("a.deleteOption2").live("click", function () {
                activityLen--;
                var $this = $(this);
                var _value = $this.closest("td").find("input").attr("value");
                nameActivity = nameActivity.replace("," + _value, "");

                if (_value != "0") {
                [#if plan.type=="random"]
                    delActivity += _value + ",";
                [#elseif plan.type=="unionActivity"]
                    delSingleProduct += _value + ",";
                [/#if]
                }
                $("input[name='delActivity']").val(delActivity);
                $("input[name='delSingleProduct']").val(delSingleProduct);
                $this.closest("td").remove();
            });
            // 表单验证
            $inputForm.validate({
                rules: {
                    name: "required",
                    beginDate: "required",
                    endDate: "required",
                    amounts: {
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
                    }
                },
                submitHandler: function (form) {
//                    if(_isActivityName!=""&&_isActivityName!="1"){
//                        $.message("warn",_isActivityName);
//                        return false;
//                    }
                    var amountsValidate = true;
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
                    }

                    form.submit();
                }
            });

            $.isRepeated = function (text, value) {
                var _isRepeated = "0";
                if (text != "") {
                    var obj = text.split(',');
                    if (obj.length > 0) {
                        for (var i = 0; i < obj.length; i++) {
                            if (obj[i] == value) {
                                _isRepeated = "1";
                            }
                        }
                        if (_isRepeated == "0") {
                            return true;
                        } else {
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

            $.addCoupoonCount = function (id) {
                var _addCount = $("#addCount" + id).val().trim();
                $("#addCountButton" + id).attr("disabled", "disabled");
                if (_addCount == null || _addCount == "") {
                    //$("#addCount"+id).val().focus();
                    $.message("error", "请输入数值");
                    $("#addCountButton" + id).removeAttr("disabled");
                    return false;
                }

                $.ajax({
                    url: "${base}/admin/activity_planning/add/count.jhtml",
                    type: "POST",
                    data: {id: id, count: _addCount},
                    datetype: "json",
                    cache: false,
                    success: function (data) {
                        $.message(data.type, data.content);
                        $("#addCountButton" + id).removeAttr("disabled");
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
<form id="inputForm" action="update.jhtml" method="post" onkeypress="if(event.keyCode==13){return false;}">
    <input type="hidden" name="activityPlanningId" value="${id}">

    <input type="hidden" name="delCoupon">
    <input type="hidden" name="delTen">
    <input type="hidden" name="delActivity">
    <input type="hidden" name="delSingleProduct">
    <div>
        <table id="attributeTable" class="input">
            <tr>
                <th>
                    <span class="requiredField">*</span>活动名称
                </th>
                <td colspan="2">
                    <input type="text" name="name" class="text" maxlength="20" value="${(plan.name)!}"
                           [#if plan.onOff=='on']readonly[/#if]/>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>起始日期
                </th>
                <td colspan="2">
                    <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                           [#if plan.onOff=='on']readonly[/#if]
                           value="${(plan.beginDate?string("yyyy-MM-dd HH:mm:ss"))!}"
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>结束日期
                </th>
                <td colspan="2">
                    <input type="text" id="endDate" name="endDate" class="text Wdate"
                           [#if plan.onOff=='on']readonly[/#if]
                           value="${(plan.endDate?string("yyyy-MM-dd HH:mm:ss"))!}"
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
                </td>
            </tr>

            <tr>
                <th>
                    <span class="requiredField">*</span>活动状态
                </th>
                <td colspan="2">
                    <label>
                        <input type="radio" name="onOff" value="wait" [#if plan.onOff=='wait']checked="checked"[/#if]/>待审核
                    </label>
                    <label>
                        <input type="radio" name="onOff" value="on" [#if plan.onOff=='on']checked="checked"[/#if]/>启用
                    </label>
                    <label>
                        <input type="radio" name="onOff" value="off" [#if plan.onOff=='off']checked="checked"[/#if]/>停用
                    </label>
                </td>
            </tr>

            <tr>
                <th>
                    <span class="requiredField">*</span>优惠类型
                </th>
                <td colspan="2">
                    <label>
                        <input type="radio" name="type" value="random" [#if plan.type=='random']
                               checked="checked" [/#if]/>随机减
                    </label>
                    <label>
                        <input type="radio" name="type" value="unionActivity" [#if plan.type=='unionActivity']
                               checked="checked" [/#if]/>联盟活动
                    </label>
                </td>
            </tr>
            <tr>
                <th>
                    <span class="requiredField">*</span>每人限单
                </th>
                <td colspan="2">
                    <label>
                        <input type="text" name="activityMaximumOrders" value="${(plan.activityMaximumOrders)!}"
                               [#if plan.onOff=='on']readonly[/#if]/>&nbsp;&nbsp;
                    </label>
                    <label style="color: red">
                        [每人每天限制订单笔数 默认为0不做限制]
                    </label>
                </td>
            </tr>

        [#--<tr>--]
        [#--<th>--]
        [#--<span class="requiredField">*</span>商家限单--]
        [#--</th>--]
        [#--<td colspan="2">--]
        [#--<label>--]
        [#--<input type="text" name="tenantMaximumOrders" value="${(plan.tenantMaximumOrders)!}" [#if plan.onOff=='on']readonly[/#if]/>&nbsp;&nbsp;--]
        [#--</label>--]
        [#--<label style="color: red">--]
        [#--[商家限制订单笔数 默认为0没有限制]--]
        [#--</label>--]
        [#--</td>--]
        [#--</tr>--]

            <tr>
                <th>
                    <span class="requiredField">*</span>优惠策略
                </th>
                <td colspan="2">
                    &nbsp;&nbsp;&nbsp;
                [#if plan.onOff=='wait']
                    <a href="javascript:;" id="addOption" class="button">添加</a>
                [/#if]
                    最多可设置五个层级
                </td>
            </tr>

        [#list plan.coupons as counon]
            <tr class="optionTr">
                <th>
                    <span class="requiredField"></span>
                </th>
                <td>
                    <input type="hidden" name="couponIds" value="${(counon.id)!}">
                    <input type="text" name="amounts" maxlength="3" value="${(counon.amount)!}"
                           [#if plan.onOff=='on']readonly[/#if]
                           style="width: 150px" placeholder="随机减金额"/>&nbsp;
                    <input type="text" name="counts" maxlength="5" value="${(counon.count)!}"
                           [#if plan.onOff=='on']readonly[/#if]
                           style="width: 150px" placeholder="优惠券库存"/>
                    满<input type="text" name="minimumPrices" maxlength="6" value="${(counon.minimumPrice)!}"
                            [#if plan.onOff=='on']readonly[/#if]
                            style="width: 150px" placeholder="最低消费"/>元
                    [#if plan.onOff=='wait']
                        <a href="javascript:;" class="deleteOption">[${message("admin.common.delete")}]</a>
                    [/#if]
                    &nbsp;&nbsp;
                    <label>
                        <input type="number" id="addCount${(counon.id)!}" style="width: 80px" maxlength="4"
                               placeholder="追加优惠券"/>&nbsp;
                        <input type="button" class="button" onclick="$.addCoupoonCount(${(counon.id)!})"
                               id="addCountButton${(counon.id)!}" value="添加"/>
                    </label>
                </td>
            </tr>
        [/#list]
        </table>

        <table class="input">
            <tr>
                <th>活动商家：</th>
                <td colspan="2">
                    <input type="text" id="_tenantName" name="_tenantName" placeholder="请输入完整的店铺名称或者店主信息"
                           maxlength="200"
                           class="text"/>
                [#if plan.onOff=='wait']
                    <input type="button" id="addActivityTenant" class="button" value="添加"/>
                [/#if]

                    <label style="color: red">
                        &nbsp;&nbsp;[请输入完整的店铺名称或者店主信息]
                    </label>

                </td>
            </tr>
            <tr>
                <th>&nbsp;</th>
                <td colspan="2">
                    <table id="activityTenant" class="input">
                    [#list plan.tenants as tenant]
                        <td style="float: left;border-bottom: none">
                            <input type="hidden" name="tenantIds" value="${(tenant.id)!}">
                            <a href="${(tenant.thumbnail)!}" target="_blank">
                                <img src="${(tenant.thumbnail)!}" width="25" height="25">
                            </a>
                            <label style="padding:0 5px;">${(tenant.shortName)!}</label>
                            [#if plan.onOff=='wait']
                                <a class="deleteOption1">[${message("admin.common.delete")}]</a>
                            [/#if]
                        </td>
                    [/#list]
                    </table>
                </td>
            </tr>
        </table>

        <table class="input">
            <tr>
                <th>活动分类</th>
                <td colspan="2">
                    <input type="text" id="_activityName" name="_activityName" placeholder="活动信息"
                           maxlength="200"
                           class="text"/>

                [#if plan.onOff=='wait']
                    <input type="button" id="addActivityName" class="button" value="添加"/>
                [/#if]

                </td>
            </tr>
            <tr>
                <th>&nbsp;</th>
                <td colspan="2">
                    <table id="activityName" class="input">
                    [#if plan.type=="random"]
                        [#list plan.adPositions as adPosition]
                            <td style="float: left;border-bottom: none">
                                <input type="hidden" name="activityIds" value="${(adPosition.id)!}">
                                <input type="hidden" name="activityNames" value="${(adPosition.description)!}">
                                <label style="padding:0 5px;">${(adPosition.description)!}</label>&nbsp;
                                <a href="${base}/admin/ad/list.jhtml?adPositionId=${(adPosition.id)!}">[管理]</a>&nbsp;
                                [#if plan.onOff=='wait']
                                    <a class="deleteOption2">[${message("admin.common.delete")}]</a>
                                [/#if]
                            </td>
                        [/#list]
                    [#elseif plan.type=="unionActivity"]
                        [#list plan.singleProductPositions as singleProductPosition]
                            <td style="float: left;border-bottom: none">
                                <input type="hidden" name="activityIds" value="${(singleProductPosition.id)!}">
                                <input type="hidden" name="activityNames" value="${(singleProductPosition.name)!}">
                                <label style="padding:0 5px;">${(singleProductPosition.name)!}</label>&nbsp;
                                <a href="${base}/admin/single/product/list.jhtml?singleProductPositionId=${(singleProductPosition.id)!}">[管理]</a>&nbsp;
                                [#if plan.onOff=='wait']
                                    <a class="deleteOption2">[${message("admin.common.delete")}]</a>
                                [/#if]
                            </td>
                        [/#list]
                    [/#if]
                    </table>
                </td>
            </tr>
        </table>
        <table class="input">
            <tr>
                <th>活动规则：</th>
                <td colspan="2">
                    <textarea id="introduction" name="introduction" class="text"
                              [#if plan.onOff=='on']readonly[/#if]>${(plan.introduction)!}</textarea>
                </td>
            </tr>
        </table>
        <table class="input">
            <tr>
                <th>&nbsp;
                </th>
                <td>

                [#if plan.onOff!='off']
                    <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                [/#if]
                    <input type="button" class="button" value="${message("admin.common.back")}"
                           onclick="location.href='list.jhtml'"/>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>