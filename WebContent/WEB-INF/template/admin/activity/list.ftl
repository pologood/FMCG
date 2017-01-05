<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.product.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $listForm = $("#listForm");
            var $filterSelect = $("#filterSelect");
            var $filterOption = $("#filterOption a");

        [@flash_message /]

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


        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.activity.list")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="type" name="type" value="${type}"/>

    <div class="bar">
        [#--<a href="add.jhtml" class="iconButton ">--]
            [#--<span class="addIcon">&nbsp;</span>${message("admin.common.add")}--]
        [#--</a>--]
        <div class="buttonWrap">

            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                ${message("admin.activity.filter")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                    [#list ActivityRulesTypes as ActivityRulesType]
                        <li>
                            <a href="javascript:;" name="type" val="${ActivityRulesType}" [#if ActivityRulesType==type]
                               class="checked"[/#if]>${message("admin.activity.type."+ActivityRulesType)}</a>
                        </li>
                    [/#list]
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
                    </ul>
                </div>
            </div>
        </div>
    [#--div class="menuWrap">--]
    [#--<div class="search">--]
    [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
    [#--<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"/>--]
    [#--<button type="submit">&nbsp;</button>--]
    [#--</div>--]
    [#--</div>--]
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>${message("ActivityRules.description")}</span>
            </th>
            <th>
                <span>${message("ActivityRules.point")}</span>
            </th>
            <th>
                <span>${message("ActivityRules.amount")}</span>
            </th>
            <th>
                <span>${message("ActivityRules.status")}</span>
            </th>
            <th>
                <span>${message("ActivityRules.type")}</span>
            </th>
            <th>
                <span>${message("ActivityRules.remarks")}</span>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as activityRules]
        <tr>
            <td>
                <input type="checkbox" name="ids" value="${activityRules.id}"/>
            </td>
            <td>
            ${activityRules.description}
            </td>
            <td>
            ${activityRules.point}
            </td>
            <td>
            ${activityRules.amount}
            </td>
            <td>
            ${message("admin.activity.status."+activityRules.status)}
            </td>
            <td>
            ${message("admin.activity.type."+activityRules.type)}
            </td>
            <td>
            ${activityRules.remarks}
            </td>
            <td>
                <a href="edit/${activityRules.id}.jhtml">[${message("admin.common.edit")}]</a>
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