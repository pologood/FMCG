<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.coupon.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {

        [@flash_message /]

            $.showQrcode = function (id) {
                if (id == "") {
                    return;
                }

                $.ajax({
                    url: "${base}/admin/coupon/get/code.jhtml",
                    type: "POST",
                    data:{
                        id : id
                    },
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if(data.message.type == "success"){

                            var _code = parseFloat(data.data)+1;

                            $.dialog({
                                title: "二维码",
                            [@compress single_line = true]
                                content: '<div style="text-align: center;">' +
                                '<img src="${base}/admin/coupon/qrcode/coupon.jhtml?id=' + id + '&code='+_code+'" >' +
                                '<\/div>',
                            [/@compress]
                                width: 200,
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
                        }
                    }
                });


            };

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.coupon.list")}
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="list.jhtml" method="get">
    <div class="bar">
    [#if currentMember??]
        [#if currentMember.username == 'admin' || currentMember.username == 'zqp_cw']
            <a href="add.jhtml" class="iconButton">
                <span class="addIcon">&nbsp;</span>添加
            </a>
        [/#if]
    [/#if]
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
                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
                        </li>
                    </ul>
                </div>
            </div>
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
                        <a href="javascript:;"[#if page.searchProperty == "name"] class="current"[/#if]
                           val="name">${message("Coupon.name")}</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <table id="listTable" class="list">
        <tr>
            <!-- <th class="check">
                <input type="checkbox" id="selectAll"/>
            </th> -->
            <th>
                <a href="javascript:;" class="sort" name="name">名称</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="amount">金额</a>
            </th>
            <th>已登记</th>
            <th>
                <a href="javascript:;" class="sort" name="usedCount">已使用</a>
            </th>
            <th>
                <a href="javascript:;" class="sort" name="sendCount">已领取</a> 
            </th>
            <th>
                <span>操作</span>
            </th>
        </tr>
    [#list maps as coupon]
        <tr>
            <!-- <td>
                <input type="checkbox" name="ids" value=""/>
            </td> -->
            <td>
            ${coupon.name}
            </td>
            <td>
            ${coupon.amount}
            </td>
            <td>
                <a href="markedList.jhtml?id=${coupon.id}" >${coupon.mark_count}</a>
            </td>
            <td>
                <a href="usedList.jhtml?id=${coupon.id}" name="usedCount">${coupon.used_count}</a>
            </td>
            <td>
                <a href="sendedList.jhtml?id=${coupon.id}" name="sendCount">${coupon.send_count}</a>
            </td>
            <td>
                <a href="edit.jhtml?id=${coupon.id}">[编辑]</a>
                &nbsp;&nbsp;
                [#--<a href="javascript:$.showQrcode(${coupon.id})">[生成二维码]</a>--]
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