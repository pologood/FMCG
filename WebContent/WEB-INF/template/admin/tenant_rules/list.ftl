<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.main.tenantRules")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {

        [@flash_message /]

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.main.tenantRules")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
    [#if false]
    [#--目前只开放修改功能和查看功能--]
        <a href="${base}/admin/tenant_rules/add.jhtml" class="iconButton">
            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
        </a>
    [/#if]
        <div class="buttonWrap">

        [#if false]
        [#--目前只开放修改功能和查看功能--]
            <a href="javascript:;" id="deleteButton" class="iconButton">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
            </a>
        [/#if]
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
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
                <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200"
                       style="left: 6px;"/>
                <button type="submit">&nbsp;</button>
            </div>
        </div>

    </div>

    <table id="listTable" class="list">
        <tr>
            <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th>
            <th>
                <span>序号</span>
            </th>
            <th>
                <span>名称</span>
            </th>
            <th>
                <span>等级</span>
            </th>
            <th>
                <span>操作</span>
            </th>
            <th>
                <span>规则访问路径</span>
            </th>
            <th>
                <span>${message("admin.common.handle")}</span>
            </th>
        </tr>
    [#list page.content as tenantRule]
        <tr>
            <td>
                <input type="checkbox" name="ids" value="${tenantRule.id}"/>
            </td>
            <td>
            [#--序号--]
                ${tenantRule_index+1}
            </td>
            <td>
            [#--名称--]
                  ${tenantRule.name}
            </td>
            <td>
            [#--等级--]
                  ${tenantRule.lev}
            </td>
            <td>
            [#--操作--]
                [#list tenantRule.oper?split(',') as opItem]
                    [#if opItem??&&opItem!=""]
                    ${message("helper.role."+opItem)}&nbsp;&nbsp;
                    [/#if]
                [/#list]
            </td>
            <td>

            [#--url--]
                  ${tenantRule.url}
            </td>
            <td>
                <a href="${base}/admin/tenant_rules/edit/${tenantRule.id}.jhtml">${message("admin.common.edit")}</a>

                [#if false]
                [#--目前只开放修改功能和查看功能--]
                    <a href="${base}/admin/tenant_rules/delete/${tenantRule.id}.jhtml">${message("admin.common.delete")}</a>
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