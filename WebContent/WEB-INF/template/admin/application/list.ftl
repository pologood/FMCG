<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>应用 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            //订单筛选
            $("#filterSelect").mouseover(function(){
                var $this=$(this);
                var offset=$this.offset();
                var $menuWrap=$this.closest("div.menuWrap");
                var $popupMenu=$menuWrap.children("div.popupMenu");
                $popupMenu.css({left:offset.left,top:offset.top+$this.height()+2}).show();
                $menuWrap.mouseleave(function(){
                    $popupMenu.hide();
                });
            });
            //筛选选项
            $("#filterOption a").click(function(){
                var $this=$(this);
                var $dest=$("#"+$this.attr("name"));
                if ($this.hasClass("checked")){
                    $dest.val("");//????????????????????????????????????????
                }else {
                    $dest.val($this.attr("val"));
                }
                $("#listForm").submit();
                return false;
            });
        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 应用
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="status" name="status" value="${status}"/>
    <div class="bar">
        [#--<a href="${base}/admin/application/add.jhtml" class="iconButton">--]
            [#--<span class="addIcon">&nbsp;</span>${message("admin.common.add")}--]
        [#--</a>--]
        <div class="buttonWrap">
            <a href="javascript::" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                    ${message("Application.Status.filter")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        [#list statusList as _statu]
                        <li>
                            <a href="javascript:;" name="status" val="${_statu}"  [#if status==_statu]
                                class="checked"[/#if]>${message("Application.Status."+_statu)}</a>
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
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" style="left: 6px;"/>
                <button type="submit">&nbsp;</button>
            </div>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <th>
					<span>编号<span>
            </th>
            <th>
					<span>名称<span>
            </th>
            <th>
					<span>商家<span>
            </th>
            <th>
                <span>有效日期</span>
            </th>
            <th>
                <span>支付状态</span>
            </th>
            <th>
                <span>操作</span>
            </th>
        </tr>
    [#list page.content as application]
        <tr>
            <td>
                 ${application.id}
            </td>
            <td>
                ${application.name}
            </td>
            <td>
                [#if application.tenant??]
                ${application.tenant.name}
                [#else]
                    -
                [/#if]
            </td>
            <td>
                <a [#if application.validityDate<nowDate] style="color: red"[/#if]>
                ${application.validityDate}
                </a>
            </td>
            <td>
                ${message("Application.Status."+application.status)}
            </td>
            <td>
                <a href="${base}/admin/application/edit/${application.id}.jhtml">延期</a>
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