<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.adPosition.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {

        [@flash_message /]
            var $listForm = $("#listForm");
            var $filterSelect = $("#filterSelect");
            var $filterOption = $("#filterOption a");

            // 筛选
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.adPosition.list")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" name="type" id="type" value="${type}"/>
    <div class="bar">
        <a href="add.jhtml?type=${type}" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
        <div class="buttonWrap">
            <!--<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>-->
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                    筛选<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                    [#list  adPositionTypes as adPositionType]
                        [#if versionType==1&&(adPositionType=='weChatStore'||adPositionType=='activity')]
                            [#break /]
                        [/#if]
                        <li>
                            <a href="javascript:;" name="type" val="${adPositionType}"[#if type==adPositionType]
                               class="checked"[/#if]>${message("AdPosition.Type."+adPositionType)}</a>
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
        <div class="menuWrap">
            <div class="search">
            [#--<span id="searchPropertySelect" class="arrow">&nbsp;</span>--]
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
        [#--<div class="popupMenu">--]
        [#--<ul id="searchPropertyOption">--]
        [#--<li>--]
        [#--<a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if]--]
        [#--val="name">${message("AdPosition.name")}</a>--]
        [#--</li>--]
        [#--</ul>--]
        [#--</div>--]
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="name">${message("AdPosition.name")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="type">类型</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="name">编码</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="width">${message("AdPosition.width")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="height">${message("AdPosition.height")}</a>
            </th>
            <th>
                <a href="javascript:;" class="sort">频道</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="description">${message("AdPosition.description")}</a>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as adPosition]
        <tr>
            <td>
                <input type="checkbox" name="ids" value="${adPosition.id}"/>
            </td>
            <td>
            ${adPosition.name}
            </td>
            <td>
                [#if adPosition.type??]${message("AdPosition.Type." + adPosition.type)}[/#if]
            </td>
            <td>
            ${adPosition.id}
            </td>
            <td>
            ${adPosition.width}
            </td>
            <td>
            ${adPosition.height}
            </td>
            <td>
                [#if adPosition.productChannel??]${adPosition.productChannel.name}[/#if]
            </td>
            <td>
                [#if adPosition.description??]
                    <span title="${adPosition.description}">${abbreviate(adPosition.description, 50, "...")}</span>
                [/#if]
            </td>
            <td>
                <a href="edit.jhtml?id=${adPosition.id}">[${message("admin.common.edit")}]</a>
                <a href="${base}/admin/ad/list.jhtml?adPositionId=${adPosition.id}">[管理]</a>
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