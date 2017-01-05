<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>商家列表 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">

        function getArealSelect() {
            var $areaId = $("#areaId2");
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
        }

        $().ready(function () {

        [@flash_message /]
            var $listForm = $("#listForm");
            var $filterSelect = $("#filterSelect");
            var $filterOption = $("#filterOption a");
            var $moreButton = $("#moreButton");
            // 订单筛选
            $filterSelect.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });

            // 筛选选项
            $filterOption.click(function () {
                var $this = $(this);
                var $dest = $("#" + $this.attr("name"));
                if ($this.hasClass("checked")) {
                    $dest.val("");
                } else {
                    $dest.val($this.attr("val"));
                }
                $listForm.submit();
                return false;
            });

            // 更多选项
            $moreButton.click(function () {
                $.dialog({
                    title: "${message("admin.product.moreOption")}",
                [@compress single_line = true]
                    content: '<table id= "moreTable" class= "input" style = "height: 60px">' +
                    '<tr>' +
                    '<th style="width:90px;">${message("Product.tags")}:<\/th>' +
                    '<td>' +
                    '<select name="tagId">' +
                    '<option value="">${message("admin.common.choose")}<\/option>' +
                        [#list tags as tag]
                        '<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>${tag.name}<\/option>'+
                        [/#list]
                    '<\/select>' +
                    '<\/td>' +
                    '<\/tr>' +
                    '<tr>' +
                    '<th style="width:90px;">类型:<\/th>' +
                    '<td>' +
                    '<select name="tenantType"><option value = "" >${message("admin.common.choose")} <\/option > ' +
                    '<option value="tenant">经销商<\/option>' +
                    '<option value="supplier">供应商<\/option>' +
                    '<option value="retailer">零售商<\/option>' +
                    '<\/select>' +
                    '<\/td>' +
                    '<\/tr>' +
                    '<tr>' +
                    '<th style="width:90px;">地区:<\/th>' +
                    '<td>' +
                    '<span class="fieldSet">' +
                    '<input type="hidden" id="areaId2" name="areaId" value="${(area.id)!}" treePath="${(area.treePath)!}"\/><\/span>' +
                    '<script>getArealSelect()<\/script>' +
                    '<\/td><\/tr>' +
                    '<tr>' +
                    '<th style="width:90px;">绑定二位码:<\/th>' +
                    '<td>' +
                    '<select name="qrCodeStatus"><option value = "" >${message("admin.common.choose")} <\/option > ' +
                    '<option value="1" [#if qrCodeStatus=='1']  selected="selected" [/#if]>已绑定<\/option>' +
                    '<option value="0" [#if qrCodeStatus=='0']  selected="selected" [/#if]>未绑定<\/option>' +
                    '<\/select>' +
                    '<\/td> <\/tr>' +
                    '<tr>' +
                    '<th style="width:90px;">商品上架数量:<\/th>' +
                    '<td>' +
                    '<select name="marketableSize"><option value = "" >${message("admin.common.choose")} <\/option > ' +
                    '<option value="0,20" [#if marketableSize=='0,20']  selected="selected" [/#if]>0--20<\/option>' +
                    '<option value="21,50"[#if marketableSize=='21,50']  selected="selected" [/#if]>21--50<\/option>' +
                    '<option value="51,100"[#if marketableSize=='51,100']  selected="selected" [/#if]>51--100<\/option>' +
                    '<option value="100,"[#if marketableSize=='100,']  selected="selected" [/#if]>100+<\/option>' +
                    '<\/select>' +
                     '<\/td> <\/tr>' +
                    '<tr><th style="width:90px;">商品分类:<\/th>' +
                    '<td><select name="tenantCategoryId" id="tenantCategory_id">'+
                        '<option value = "" >${message("admin.common.choose")} <\/option > ' +
                        [#list tenantCategorys as tenantCategory]
                        '<option value = "${tenantCategory.id}" [#if tenantCategory.id==tenantCategoryId]selected="selected"[/#if]>'+
                        '${tenantCategory.name}'+
                        '<\/option > ' +
                        [/#list]
                    '<\/select>' +
                    '<\/td> <\/tr>' +
                    '<\/table>',
                [/@compress]
                    width: 380,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function () {
                        $("#moreTable :input").each(function () {
                            var $this = $(this);
                            $("#" + $this.attr("name")).val($this.val());
                        });
                        // alert($("#tenantCategory_id").val());
                        $("#tenantCategoryId").val($("#tenantCategory_id").val());
                        $listForm.submit();
                    }
                });
            });
            $.showQrcode = function (ticket) {
                if (ticket == "") {
                    return;
                }

                $.dialog({
                    title: "店铺二维码",
                [@compress single_line = true]
                    content: '<div style="text-align: center;">' +
                    '<img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + ticket + '" alt="">' +
                    '</br>' +
                    '<span style="font-size: 14px;color: orangered">单击右键选择【图片另存为】可保存二维码到本地</span>' +
                    '</br>' +
                    '<span style="font-size: 14px;color: orangered">注：您需要在您保存的文件后面加上【.png】做为文件后缀名</span>' +
                    '<\/div>',
                [/@compress]
                    width: 480,
                    modal: true,
                    cancel: null,
                    ok: null,
                    autoOpen: false,
                    show: {
                        effect: "blind",
                        duration: 1000
                    },
                    hide: {
                        effect: "explode",
                        duration: 1000
                    }
                });
            };

            var _date = new Date().toLocaleDateString();

            $("#export_ss").click(function(){
                if(confirm("导出是当前页面数据导出，如想导出多条数据，可选择每页显示数")){
                    $.message("success","正在帮您导出，请稍后");
                    $(".table2excel").table2excel({
                        exclude: ".noExl",
                        name:"商家档案",
                        filename:"商家档案",
                        fileext: ".xls",
                        exclude_img: true,
                        exclude_links: false,
                        exclude_inputs: true
                    });
                }
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 商家列表
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="status" name="status" value="${status}"/>
    <input type="hidden" id="tagId" name="tagId" value="${tagId}"/>
    <input type="hidden" id="areaId" name="areaId" value="${(area.id)!}"/>
    <input type="hidden" id="tenantType" name="tenantType" value="${tenantType}"/>
    <input type="hidden" id="tenantCategoryId" name="tenantCategoryId" value="${tenantCategoryId}"/>

    <input type="hidden" id="qrCodeStatus" name="qrCodeStatus" value="${qrCodeStatus}"/>
    <input type="hidden" id="marketableSize" name="marketableSize" value="${marketableSize}"/>
    <div class="bar">
        <div class="buttonWrap">
            <a href="javascript:;" id="export_ss" class="button">导出</a>
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                ${message("admin.tenant.filter")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="status" val="none"[#if status == "none"] class="checked"[/#if]>待审核</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="confirm"[#if status == "confirm"]
                               class="checked"[/#if]>已审核</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="success"[#if status == "success"]
                               class="checked"[/#if]>已开通</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="fail"[#if status == "fail"]
                               class="checked"[/#if]>已关闭</a>
                        </li>
                    </ul>
                </div>
            </div>
            <a href="javascript:;" id="moreButton" class="button">${message("admin.product.moreOption")}</a>
            <div class="menuWrap">
                <a href="javascript:;" id="pageSizeSelect" class="button">
                ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="pageSizeOption">
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                        </li>
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                        </li>
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                        </li>
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
                        </li>
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 500] class="current"[/#if] val="500">500</a>
                        </li>
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 1000] class="current"[/#if] val="1000">1000</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="menuWrap">
            <div class="search">
                <input type="text" id="searchValue" name="searchValue" value="${keyword}" maxlength="200" placeholder="手机号、会员、店铺" style="width:190px;"/>
                <button type="submit">&nbsp;</button>
            </div>
        </div>
        <div class="menuWrap">
            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                   value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"
                   placeholder="入住开始日期"/>
            &nbsp;-&nbsp;
            <input type="text" id="endDate" name="endDate" class="text Wdate"
                    value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"
                    placeholder="入住结束日期"/>
        </div>
        <input type="submit" value="查询" class="bar buttonWrap button">
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th class="check noExl">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>编号</span>
            </th>
            <th>
                <span>入驻时间</span>
            </th>
            <th>
                <span>所属区域</span>
            </th>
            <th>
                <span>店铺分类</span>
            </th>
            <th>
                <span>店铺名称</span>
            </th>
            <th>
                <span>商品款数</span>
            </th>
            <th>
                <span>地址</span>
            </th>
            <th>
                <span>店主(电话)</span>
            </th>
            <th>
                <span>缩略图</span>
            </th>
            <th>
                <span>审核状态</span>
            </th>

            <th>
                <span>是否推荐</span>
            </th>
            <th>
                <span>是否加盟</span>
            </th>
            <th>
                <span>平台佣金</span>
            </th>
            <th>
                <span>推广分润</span>
            </th>
            <th>
                <span>联盟佣金</span>
            </th>
            <th>
                <span>实名认证</span>
            </th>
            <th>
                <span>绑定银行卡</span>
            </th>
        [#if versionType==0]
            <th>
                <span>二维码</span>
            </th>
        [/#if]
            <th class="noExl">
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as tenant]
        <tr>
            <td class="noExl">
                <input type="checkbox" name="ids" value="${tenant.id}"/>
            </td>
            <td>
            ${(tenant.id)!}
            </td>
            <td>
            ${tenant.createDate?string("yyyy-MM-dd")}
            </td>
            <td>
            ${(tenant.area.fullName)!}
            </td>
            <td>
            ${(tenant.tenantCategory.name)!}
            </td>
            <td>
                <span title="${tenant.name}">${abbreviate(tenant.name, 20, "...")}</span>
            </td>
            <td>
            ${(tenant.products?size)!}
            </td>
            <td>
            ${(tenant.address)!}
            </td>
            <td>
            ${(tenant.member.displayName)!}&nbsp;(${(tenant.member.mobile)!})
            </td>
            <td>
                [#if tenant.member.thumbnail??]是[#else ]否[/#if]
            </td>
            <td>
            ${message("Tenant.Status."+tenant.status)}
            </td>
            <td>
                [#assign isTag="否"]
                [#list tags as tag]
                [#if tenant.tags?seq_contains(tag)]
                    [#assign isTag="是"]
                    [#break /]
                [/#if]
            [/#list]
                ${isTag}
            </td>
            <td>[#if tenant.isUnion]是[#else ]否[/#if]</td>
            <td>${(tenant.brokerage)!}</td>
            <td>${(tenant.generalize)!}</td>
            <td>${(tenant.agency)!}</td>
            <td>[#if tenant.member.idcard??]是[#else ]否[/#if]</td>
            <td>[#if tenant.member.memberBanks?size>0]是[#else ]否[/#if]</td>
            [#if versionType==0]
                <td><!-- 二维码 -->
                    [#if tenant.qrcodes??&&tenant.qrcodes.size()>0]
                        <a href="javascript:;">
                            <img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${tenant.qrcodes[0].ticket}"
                                 width="30px" height="30px" onclick=" $.showQrcode('${tenant.qrcodes[0].ticket}')"/>
                        </a>
                    [#else]
                        [#if tenant.status=='success']
                            <a href="edit_qrcode.jhtml?id=${tenant.id}">去绑定</a>
                        [/#if]
                    [/#if]
                </td>
            [/#if]
            <td class="noExl">
                <a href="${base}/admin/tenant/view.jhtml?id=${tenant.id}">[详情]</a>
                <a href="edit.jhtml?id=${tenant.id}">[${message("admin.common.edit")}]</a>
                <a href="${base}/admin/product/list.jhtml?tenantId=${tenant.id}">[商品]</a>
                <a href="${base}/admin/deposit/list.jhtml?memberId=${tenant.member.id}">账单</a>
                [#if versionType==0]
                    <a href="${base}/admin/product/oneKeyAdd.jhtml?tenantId=${tenant.id}">一键上架</a>
                [/#if]
            </td>
        </tr>
    [/#list]
    </table>
[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
    [#include "/admin/include/pagination.ftl"]
[/@pagination]
</form>
</body>
</html>