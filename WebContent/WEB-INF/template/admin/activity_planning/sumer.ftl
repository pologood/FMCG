<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.brand.list")} - Powered By rsico</title>
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
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo;平台活动优惠券统计
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="sumer.jhtml" method="get">
    <input type="hidden" id="type" name="type" value="${type}"/>
    <input type="hidden" id="id" name="id" value="${id}"/>
    <input type="hidden" id="status" name="status" value="${status}"/>
    <div class="bar">
        <div class="buttonWrap">
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
                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                               val="100">100</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
                    <span class="sumer">总[#if type=="send"]领取[#else]使用[/#if]量：${total.sumerCount} ， 总[#if type=="send"]
                        领取[#else]使用[/#if]人数：${total.sumerNumber}</span>
    </div>
    <div class="list">
        <table id="listTable" class="list">
            <tr>
                <th>
                    <a href="javascript:;" class="sort" name="sumerDate">日期</a>
                </th>
                <th>
                [#if type=="send"]领取数量[#elseif type=="used"]使用数量[/#if]
                </th>
                <th>
                [#if type=="send"]领取人数[#elseif type=="used"]使用人数[/#if]
                </th>
                [#--<th>--]
                    [#--操作--]
                [#--</th>--]
            </tr>
        [#list page.content as couponSumer]
            <tr>
                <td>
                    <span title="${couponSumer.sumerDate?string("yyyy-MM-dd HH:mm")}">${couponSumer.sumerDate?string("yyyy-MM-dd HH:mm")}</span>
                </td>
                <td>
                ${couponSumer.sumerCount}
                </td>
                <td>
                ${couponSumer.sumerNumber}
                </td>
                [#--<td>--]
                    [#--<a href="${base}/helper/member/statistics/sale_total.jhtml?couponId=${id}">[查看]</a>--]
                [#--</td>--]
            </tr>
        [/#list]
        </table>
    [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
        [#include "/helper/include/pagination.ftl"]
    [/@pagination]
    </div>
</form>
</body>
</html>
