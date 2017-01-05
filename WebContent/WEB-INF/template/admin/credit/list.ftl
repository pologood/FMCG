<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>付款单列表 - Powered By rsico</title>
    <meta name="author" content="rsico Team" />
    <meta name="copyright" content="rsico" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <!-- <script type="text/javascript" src="${base}/resources/admin/js/tableExport.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.base64.js"></script> -->
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>

</head>
<body>
<div id="trade_wrap"></div>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 付款单列表 <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="method" name="method" value="${method}" />
    <input type="hidden" id="status" name="status" value="${status}" />
    <div class="bar">
        <div class="buttonWrap">
            <!--
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>
				-->
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
				${message("admin.credit.method")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="method" val="immediately"[#if method == "immediately"] class="checked"[/#if]>${message("Credit.Method.immediately")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="method" val="fast"[#if method == "fast"] class="checked"[/#if]>${message("Credit.Method.fast")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="method" val="general"[#if method == "general"] class="checked"[/#if]>${message("Credit.Method.general")}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelects" class="button">
				${message("admin.credit.status")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOptions" class="check">
                        <li>
                            <a href="javascript:;" name="status" val="wait"[#if status == "wait"] class="checked"[/#if]>${message("Credit.Status.wait")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="wait_success"[#if status == "wait_success"] class="checked"[/#if]>${message("Credit.Status.wait_success")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="success"[#if status == "success"] class="checked"[/#if]>${message("Credit.Status.success")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="wait_failure"[#if status == "wait_failure"] class="checked"[/#if]>${message("Credit.Status.wait_failure")}</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="failure"[#if status == "failure"] class="checked"[/#if]>${message("Credit.Status.failure")}</a>
                        </li>
                    </ul>
                </div>
            </div>
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
                            <a href="javascript:;"[#if page.pageSize == 1000] class="current"[/#if] val="1000" title="(仅显示200条数据，实际导出1000条)">1000</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="menuWrap">
                <input type="text" id="beginDate" name="beginDate" placeholder="申请开始时间" class="text Wdate" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
                -<input type="text" id="endDate" name="endDate" placeholder="申请结束时间" class="text Wdate" value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
            </div>
            <div class="menuWrap">
                <input type="text" id="beginDates" name="beginDates" placeholder="付款开始时间" class="text Wdate" value="[#if beginDates??]${beginDates?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDates\')}'});" />
                -<input type="text" id="endDates" name="endDates" placeholder="付款结束时间" class="text Wdate" value="[#if endDates??]${endDates?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDates\')}'});" />
            </div>
            <div class="menuWrap">
                <input type="text" id="searchValue" name="searchValue" class="text"
                       placeholder="账户名称" value="${page.searchValue}"/>
            </div>
            <input class="button" value="查询" type="button" id="queryButton"/>
            <input id="export" class="button" value="导出" type="" style="width: 28px;"/>
        </div>

    </div>
    <table id="listTable" class="list table2excel">
        <thead>
        <tr>
            <th class="check noExl">
                <input type="checkbox" id="selectAll" />
            </th>
            <th>
                <a href="javascript:;" class="sort" name="createDate">申请日期</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="sn">编号</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="type">类型</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="method">付款方式</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="amount">付款金额</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="fee">手续费</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="member">账户名称</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="member">联系方式</a>
            </th>

            <th>
                <span>所属店铺</span>
            </th>

            <th>
                <span>银行名称</span>
            </th>

            <th>
                <span>银行账号</span>
            </th>

            <th>
                <a href="javascript:;" class="sort" name="status">状态</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="creditDate">付款日期</a>
            </th>
            <th class="noExl">
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
        </thead>
        <tbody>
		[#list page.content as credit]
        <tr [#if credit_index>199] style="display: none;" [/#if]>
            <td class="noExl">
                <input type="checkbox" name="ids" value="${credit.id}" />
            </td>
            <td>
                <span title="${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}">${credit.createDate}</span>
            </td>
            <td>
			${credit.sn}&nbsp;
            </td>
            <td>
			${message("Credit.Type." + credit.type)}
            </td>
            <td>
			${message("Credit.Method." + credit.method)}
            </td>
            <td style="text-align:right;">
			${(credit.amount?string("0.00"))}
            </td>
            <td style="text-align:right;">
			${(credit.fee?string("0.00"))}
            </td>
            <td>
			${(credit.payer)!"-"}
            </td>
            <td>
			${(credit.mobile)!"-"}
            </td>


            <td>
				[#if credit.member.tenant??]
				${(credit.member.tenant.name)!"-"}
				[#else]
                    未开通
				[/#if]
            </td>
            <td>
			${(credit.bank)!"-"}
            </td>
            <td>
			${credit.account}&nbsp;
            </td>


            <td>
			${message("Credit.Status." + credit.status)}
            </td>
            <td>
				[#if credit.creditDate??]
                    <span title="${credit.creditDate?string("yyyy-MM-dd HH:mm:ss")}">${credit.creditDate}</span>
				[#else]
                    -
				[/#if]
            </td>
            <td class="noExl">
                <a href="view.jhtml?id=${credit.id}&memberId=${credit.member.id}">[${message("admin.common.view")}]</a>
            </td>
        </tr>
		[/#list]
        </tbody>
    </table>
[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
	[#include "/admin/include/pagination.ftl"]
[/@pagination]
</form>
<script type="text/javascript">
    $().ready(function() {
    [@flash_message /]
        var $listForm = $("#listForm");
        var $filterSelect = $("#filterSelect");
        var $filterOption = $("#filterOption a");
        var $filterSelects = $("#filterSelects");
        var $filterOptions = $("#filterOptions a");
        var $query = $("#queryButton");

        // 查询选项
        $query.click(function () {
            $("#pageNumber").val("1");
            $listForm.submit();
        });

        // 订单筛选
        $filterSelect.mouseover(function() {
            var $this = $(this);
            var offset = $this.offset();
            var $menuWrap = $this.closest("div.menuWrap");
            var $popupMenu = $menuWrap.children("div.popupMenu");
            $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
            $menuWrap.mouseleave(function() {
                $popupMenu.hide();
            });
        });
        // 订单筛选
        $filterSelects.mouseover(function() {
            var $this = $(this);
            var offset = $this.offset();
            var $menuWrap = $this.closest("div.menuWrap");
            var $popupMenu = $menuWrap.children("div.popupMenu");
            $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
            $menuWrap.mouseleave(function() {
                $popupMenu.hide();
            });
        });
        // 筛选选项
        $filterOption.click(function() {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            if ($this.hasClass("checked")) {
                $dest.val("");
            } else {
                $dest.val($this.attr("val"));
            }
            $("#pageNumber").val("1");
            $listForm.submit();
            return false;
        });
        // 筛选选项
        $filterOptions.click(function() {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            if ($this.hasClass("checked")) {
                $dest.val("");
            } else {
                $dest.val($this.attr("val"));
            }
            $("#pageNumber").val("1");
            $listForm.submit();
            return false;
        });
        //导出数据到excel
        $("#export").click(function(){
            //导出数据到excel
            $(".table2excel").table2excel({
                exclude: ".noExl",
                name: "付款管理",
                filename: "付款管理",
                fileext: ".xls",
                exclude_img: true,
                exclude_links: false,
                exclude_inputs: true
            });
        });

    });
</script>
</body>
</html>