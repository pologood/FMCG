<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.ad.edit")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>

    <style type="text/css">
        /*品牌列表样式*/
        .barcode_brand label {
            width: 360px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>

    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $type = $("#type");
            var $contentTr = $("#contentTr");
            var $pathTr = $("#pathTr");
            var $path = $("#path");
            var $browserButton = $("#browserButton");
            var $communityId = $("#communityId");
            var $areaId = $("#areaId");
            var $linkId = '${ad.linkId}';
        [@flash_message /]

            function getCommunity() {
                if ($areaId.val() > 0) {
                    $.ajax({
                        url: "get_community.jhtml",
                        type: "GET",
                        data: {areaId: $areaId.val()},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            var opt = '<option>--请选择--</option>';
                            $.each(data, function (i, v) {

                                var selected = v.id == $linkId ? "selected" : "";

                                opt += '<option value="' + v.id + '" lat="' + v.location.lat + '" lng="' + v.location.lng + '" ' + selected + '>' + v.name + '</option>';
                            });
                            $communityId.html(opt);
                        }
                    });
                }
            }

            $areaId.lSelect({
                url: "${base}/admin/common/area.jhtml",
                fn: getCommunity()
            });
            var _linkType = "${(ad.linkType)!}";
        [#if  ad.linkType=="product"||ad.linkType=="tenant"]
            $.ajax({
                url: "${base}/admin/ad/getProductOrTenant.jhtml",
                type: "POST",
                data: {
                    id: "${(ad.linkId)!}",
                    type: "${(ad.linkType)!}"
                },
                dataType: "json",
                cache: false,
                success: function (message) {
                    if (message.length == 0) {
                        $.message("warn", "没有搜索到您需要的内容");
                        return false;
                    }
                    var $brandstd = $("#productOrTenant2 td");
                    for (var i = 0; i < message.length; i++) {
                        $brandstd.append("<label><input type='radio' name='linkId' value='" + message[i].id + "' checked />" + message[i].name + "</label>");
                    }
                }
            });
        [#elseif  ad.linkType=="community"]
            $("#productOrTenant1").hide();
            $("#productOrTenant2").hide();
            //$("#communityTr").show();
            $("#adUrl").show();
        [#elseif ad.linkType=="unionActivity"]
            $("#unionActivityTr").show();
        [/#if]
        [#if ad.type != "text"]
            $browserButton.browser({
                type: "${ad.type}"
            });
        [/#if]

            // “类型”修改
            $type.change(function () {
                if ($type.val() == "text") {
                    $contentTr.show();
                    $pathTr.hide();
                    $path.prop("disabled", true);
                } else {
                    $contentTr.hide();
                    $pathTr.show();
                    $path.prop("disabled", false);
                    $browserButton.unbind().browser({
                        type: $type.val()
                    });
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    title: "required",
                    adPositionId: "required",
                    path: "required",
                    order: "digits"
                },
                submitHandler: function (form) {
                    if ((_linkType == 'tenant' || _linkType == 'product') && ($("input[name='linkId']").val() == '' || $("input[name='linkId']").val() == null)) {
                        $.message("error", "请选择商品或商家");
                        return false;
                    }
                    form.submit();
                }
            });


            $("select[name='linkType']").change(function () {
                _linkType = $("select[name='linkType']").val();
                if (_linkType == 'tenant' || _linkType == 'product') {
                    $("#productOrTenant1").show();
                    $("#productOrTenant2").show();
                    $("#communityTr").hide();
                    $("#adUrl").hide();
                    $("#unionActivityTr").hide();
                } else if(_linkType == 'community'){
                    $("#productOrTenant1").hide();
                    $("#productOrTenant2").hide();
                    $("#unionActivityTr").hide();
                    //$("#communityTr").show();
                    $("#adUrl").show();
                }else if(_linkType == 'unionActivity'){
                    $("#productOrTenant1").hide();
                    $("#productOrTenant2").hide();
                    $("#communityTr").hide();
                    $("#adUrl").hide();
                    $("#unionActivityTr").show();
                }else{
                    $("#productOrTenant1").hide();
                    $("#productOrTenant2").hide();
                    $("#communityTr").hide();
                    $("#unionActivityTr").hide();
                    $("#adUrl").show();
                }
            });
            $("#searchButton").click(function () {
                var _searchValue = $("#searchValue").val();
                if (_searchValue == null || _searchValue == "") {
                    $.message("warn", "请输入需要搜索的信息！");
                    return false;
                }

                $.ajax({
                    url: "${base}/admin/ad/getProductOrTenant.jhtml",
                    type: "POST",
                    data: {
                        searchValue: _searchValue,
                        type: _linkType
                    },
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                        if (message.length > 1) {
                            checked = "";
                        }
                        //console.log(message.length);
                        var $linkIds = $("input[name='linkId']");
                        var $brandstd = $("#productOrTenant2 td");
                        for (var i = 0; i < message.length; i++) {
                            var flag = true;
                            $linkIds.each(function () {
                                var $this = $(this);
                                if ($this.val() == message[i].id) {
                                    flag = false;
                                }
                            });

                            ///console.log(flag, message[i].id);
                            if (flag) {
                                $brandstd.append("<label><input type='radio' name='linkId' value='" + message[i].id + "'  />" + message[i].name + "</label>");
                            }
                        }
                    }
                });
            });

            $("#selectButton").click(function () {

                var $label = $(".barcode_brand td label");
                $label.each(function () {
                    var $this = $(this);
                    if (!$this.find("input")[0].checked) {
                        $this.remove();
                    }
                });
            });

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.ad.edit")}
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="id" value="${ad.id}"/>
    <table class="input">
        <tr>
            <th>
                <span class="requiredField">*</span>${message("Ad.title")}:
            </th>
            <td>
                <input type="text" name="title" class="text" value="${ad.title}" maxlength="200"/>
            </td>
        </tr>
        <tr>
            <th>
                <span class="requiredField"></span>所属区域:
            </th>
            <td>
					<span class="fieldSet">
						<input type="hidden" id="areaId" name="areaId" value="[#if ad.area??]${(ad.area.id)!}[/#if]"
                               treePath="[#if ad.area??]${(ad.area.treePath)!}[/#if]"/>
					</span>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Ad.linkType")}:
            </th>
            <td>
                <select id="linkType" name="linkType">
                [#list linkTypes as linkType]
                    <option value="${linkType}"[#if linkType == ad.linkType]
                            selected="selected"[/#if]>${message("Ad.LinkType." + linkType)}</option>
                [/#list]
                </select>
            </td>
        </tr>

        <tr>
            <th>
            ${message("Ad.type")}:
            </th>
            <td>
                <select id="type" name="type">
                [#list types as type]
                    <option value="${type}"[#if type == ad.type]
                            selected="selected"[/#if]>${message("Ad.Type." + type)}</option>
                [/#list]
                </select>
            </td>
        </tr>

        <tr>
            <th>
            ${message("Ad.adPosition")}:
            </th>
            <td>
                <select name="adPositionId">
                [#list adPositions as adPosition]
                    <option value="${adPosition.id}"[#if adPosition == ad.adPosition]
                            selected="selected"[/#if]>${adPosition.name} [${adPosition.width} × ${adPosition.height}]
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr id="contentTr"[#if ad.type != "text"] class="hidden"[/#if]>
            <th>
            ${message("Article.content")}:
            </th>
            <td>
                <textarea id="editor" name="content" class="editor" style="width: 100%;">${ad.content?html}</textarea>
            </td>
        </tr>
        <tr id="pathTr"[#if ad.type == "text"] class="hidden"[/#if]>
            <th>
                <span class="requiredField">*</span>${message("Ad.path")}:
            </th>
            <td>
					<span class="fieldSet">
						<input type="text" id="path" name="path" class="text" value="${ad.path}"
                               maxlength="200"[#if ad.type == "text"] disabled="disabled"[/#if]/>
						<input type="button" id="browserButton" class="button"
                               value="${message("admin.browser.select")}"/>
					</span>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Ad.beginDate")}:
            </th>
            <td>
                <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                       value="[#if ad.beginDate??]${ad.beginDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]"
                       onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Ad.endDate")}:
            </th>
            <td>
                <input type="text" id="endDate" name="endDate" class="text Wdate"
                       value="[#if ad.endDate??]${ad.endDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]"
                       onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
            </td>
        </tr>
        <tr id="adUrl" [#if ad.linkType == 'product'||ad.linkType=='tenant'] style="display: none" [/#if]>
            <th>
            ${message("Ad.url")}:
            </th>
            <td>
                <input type="text" name="url" class="text" value="${ad.url}" maxlength="200"
                       style="display: [#if ad.linkType=='product'||ad.linkType=='tenant']none[#else ]block[/#if]"/>
            [#--<select id="tenantId" name="tenantId" style="display: [#if ad.linkType=='product'||ad.linkType=='tenant']block[#else ]none[/#if];float: left; width: 200px; height: 25px; line-height: 25px;" >--]
            [#--<option value="" selected="selected">请选择店铺</option>--]
            [#--[#list tenants as tenant]--]
            [#--<option value="${tenant.id}" [#if (_tenant??&&_tenatn?has_content&&_tenant.id==tenant.id)||(_product??&&_product?has_content&&_product.tenant.id==tenant.id)]selected="selected"[/#if]>--]
            [#--${tenant.name}--]
            [#--</option>--]
            [#--[/#list]--]
            [#--</select>--]


            [#--<select id="productId" name="productId" style="display: [#if ad.linkType=='product']block[#else ]none[/#if];margin-left: 10px;float: left;  width: 200px; height: 25px; line-height: 25px;" >--]
            [#--<option value="0" selected="selected">请选择商品</option>--]
            [#--[#list tenants as tenant]--]
            [#--<option value="${tenant.id}">--]
            [#--${tenant.name}--]
            [#--</option>--]
            [#--[/#list]--]
            [#--</select>--]

            </td>
        </tr>

        <tr id="productOrTenant1"
            style="display: [#if ad.linkType == 'product'||ad.linkType=='tenant'] grid[#else ]none[/#if]">
            <th>
                <span class="requiredField"></span>商品或商铺搜索：
            </th>
            <td>
                <div>
                    <input type="text" id="searchValue" name="searchValue"
                           class="text" maxlength="20" title="请输入商铺或商品信息"/>
                    &nbsp;&nbsp;
                    <input type="button" id="searchButton" class="button" value="查询"/>
                    <input type="button" id="selectButton" class="button" value="选择"/>
                    <span class="requiredField" id="brandValidator"></span>
                </div>
            </td>

        </tr>

        <tr id="productOrTenant2" class="barcode_brand"
            style="display: [#if ad.linkType == 'product'||ad.linkType=='tenant'] grid[#else ]none[/#if]">
            <th>
                商品或商铺筛选：
            </th>
            <td>
            </td>
        </tr>

        <tr id="communityTr" style="display: none">
            <th>
                所属商圈:
            </th>
            <td>
									<span class="fieldSet">
								   	<select id="communityId" ><!-- //name="linkId" -->
                                        <option value="">--请选择--</option>
                                    </select>
									</span>
            </td>
        </tr>

        <tr id="unionActivityTr" style="display: none">
            <th>
                请选择活动:
            </th>
            <td>
									<span class="fieldSet">
								   	<select id="unionActivityId" name="unionActivityId">
                                        <option value="">--请选择--</option>
                                        [#list  activityPlannings as activityPlanning]
                                            <option value="${(activityPlanning.id)!}"  [#if _activity??&&_activity?has_content&&_activity.id==activityPlanning.id]selected[/#if]>${(activityPlanning.name)!}</option>
                                        [/#list]
                                    </select>
									</span>
            </td>
        </tr>

        <tr>
            <th>
            ${message("admin.common.order")}:
            </th>
            <td>
                <input type="text" name="order" class="text" value="${ad.order}" maxlength="9"/>
            </td>
        </tr>
        <tr>
            <th>
                频道:
            </th>
            <td>
                <select name="productChannelId">
                    <option value="">请选择</option>
                [#list productChannels as productChannel]
                    <option value="${productChannel.id}" [#if ad.productChannels?seq_contains(productChannel)]
                            selected="selected"[/#if]>
                    ${productChannel.name}
                    </option>
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
                       onclick="location.href='list.jhtml'"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>