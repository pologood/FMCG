<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>商家列表 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .export{
            float: left;
            border: 1px solid #b7c8d9;
            width: 80px;
            line-height: 26px;
            text-align: center;
            font-size: 18px;
            border-radius: 5%;
            background: #b7c8d9;
            color: white;
            margin-left: 5px;
    }
    </style>
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
                    content: '<table id= "moreTable" class= "input" style = "height: 60px"><tr><th style="width:100px;">${message("Product.tags")}:<\/th><td><select name="tagId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list tags as tag]
                        '<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>${tag.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>类型:<\/th><td><select name="tenantType"><option value = "" >${message("admin.common.choose")} <\/option > ' +
                        '<option value="tenant">经销商<\/option>'+
                        '<option value="supplier">供应商<\/option>'+
                        '<option value="retailer">零售商<\/option>'+
                    '<\/select><\/td><\/tr><tr><th style="width:100px;">地区:<\/th><td><span class="fieldSet"><input type="hidden" id="areaId2" name="areaId" value="${(area.id)!}" treePath="${(area.treePath)!}"\/><\/span><script>getArealSelect()<\/script><\/td><\/tr><\/table>',
                [/@compress]
                    width: 270,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function () {
                        $("#moreTable :input").each(function () {
                            var $this = $(this);
                            $("#" + $this.attr("name")).val($this.val());
                        });
                        $listForm.submit();
                    }
                });
            });
            //导出数据到excel
            $("#export_ss").click(function(){
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "店铺报表",
                    filename: "店铺报表",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true 
                });
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 商家列表
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="report_list.jhtml" method="get">
    <input type="hidden" id="status" name="status" value="${status}"/>
    <input type="hidden" id="tagId" name="tagId" value="${tagId}"/>
    <input type="hidden" id="areaId" name="areaId" value="${(area.id)!}"/>
    <input type="hidden" id="tenantType" name="tenantType" value="${tenantType}"/>
    <div class="bar">
        <div class="buttonWrap">
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
                            <a href="javascript:;" name="status" val="confirm"[#if status == "confirm"] class="checked"[/#if]>已审核</a>
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
                    </ul>
                </div>
            </div>
            <div id="export_ss" class="export">导出</div>
        </div>
        <div class="menuWrap">
            <div class="search">
                <span id="searchPropertySelect" class="arrow">&nbsp;</span>
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
            <div class="popupMenu">
                <ul id="searchPropertyOption">
                    <li>
                        <a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if]  val="name">名称</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="menuWrap">
            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                   value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
            -<input type="text" id="endDate" name="endDate" class="text Wdate"
                    value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
        </div>
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th>
                <a href="javascript:;" class="sort">名称</a>
            </th>
            <th>
                <a href="javascript:;" class="sort">店主</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >开通时间</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >评分</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >点击量</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >销售额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >运费</a>
            </th><th>
                <a href="javascript:;" class="sort" >余额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >冻结余额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >库存金额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >上架商品数量</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" >下架商品数量</a>
            </th>
        </tr>
    [#list page.content as tenant]
        <tr>
            <td>
                <span title="${tenant.name}">${abbreviate(tenant.name, 20, "...")}</span>
            </td>
            <td>
                ${(tenant.member.username)!}
            </td>
            <td>
                <span title="${tenant.createDate?string("yyyy-MM-dd HH:mm:ss")}">${tenant.createDate}</span>
            </td>
            <td>
                ${tenant.score}
            </td><td>
                ${tenant.hits}
            </td><td>
                ${tenant.sales}
            </td><td>
                ${tenant.feight}
            </td>
            <td>
                ${(tenant.member.balance)!}
            </td>
            <td>
                ${(tenant.member.freezeBalance)!}
            </td>
            <td>
                [#if tenant.products?size gt 0]
                    [#assign allStockAmount=0]
                    [#list tenant.products as product]
                        [#if product.cost??&&product.stock??]
                            [#assign allStockAmount=allStockAmount+product.cost*product.stock]
                        [/#if]
                    [/#list]
                [/#if]
                ${allStockAmount}
            </td>
            <td>
                [#assign up_num=0]
                [#list tenant.products as product]
                    [#if product.isMarketable==true]
                        [#assign up_num=up_num+1]
                    [/#if]
                [/#list]
                ${up_num}
            </td>
            <td>
                [#assign down_num=0]
                [#list tenant.products as product]
                    [#if product.isMarketable==false]
                        [#assign down_num=down_num+1]
                    [/#if]
                [/#list]
                ${down_num}
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