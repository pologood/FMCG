<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>平台资金流水 - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            $("#export_ss").click(function(){
                $.message("success","正在帮您导出，请稍后");
                $.ajax({
                  url:"${base}/admin/statistics/platform_capital_total_export.jhtml",
                  type:"get",
                  data:{
                    beginDate:$("#beginDate").val(),
                    endDate:$("#endDate").val(),
                },
                async:false,
                dataType:"json",
                success:function(data){
                    var html='<table style="display:none;" class="table2excel">'+
                    '<thead>'+
                    '<tr>'+
                    '<th>对账日期</th>'+
                    '<th>收款</th>'+
                    '<th>提现</th>'+
                    '<th>手续费</th>'+
                    '<th>佣金</th>'+
                    '</tr>'+
                    '</thead>'+
                    '<tbody>';
                    $.each(data,function(i,obj){
                      html+=
                      '<tr>'+
                      '<td>'+obj.create_date+'</td>'+
                      '<td>'+obj.gathering+'</td>'+
                      '<td>'+obj.withdrawCash+'</td>'+
                      '<td>'+obj.fee+'</td>'+
                      '<td>'+obj.brokerage+'</td>'+
                      '</tr>';
                  });
                    html+='</tbody>'+
                    '</table>';
                    $("#trade_wrap").html(html);
                }
            });
            //导出数据到excel
            $(".table2excel").table2excel({
                exclude: ".noExl",
                name: "平台资金流水",
                filename: "平台资金流水",
                fileext: ".xls",
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
        <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 平台资金流水
        <span>(${message("admin.page.total", page.total)})</span>
    </div>
    <form id="listForm" action="platform_capital_total.jhtml" method="get">
        <div class="bar">
            <div class="buttonWrap">
                <a href="javascript:;" id="export_ss" class="button">导出</a>
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
                <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${(beginDate)!}" placeholder="对账开始时间" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" style="text-align:center;" />&nbsp;-&nbsp;
                <input type="text" id="endDate" name="endDate" class="text Wdate" value="${(endDate)!}" placeholder="对账结束时间" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" style="text-align:center;"/>
            </div>
            <input type="submit" class="button" value="查询" />
        </div>
        <table id="listTable" class="list table2excel">
            <tr>
                <th>
                    <span>对账日期</span>
                </th>
                <th>
                    <span>收款</span>
                </th>
                <th>
                    <span>提现</span>
                </th>
                <th>
                    <span>手续费</span>
                </th>
                <th>
                    <span>佣金</span>
                </th>
            </tr>
            [#list page.content as platform]
            <tr>
                <td>${(platform.createDate?string('yyyy-MM-dd'))!"--"}</td>
                <td>${(platform.gathering)!"--"}</td>
                <td>${(platform.withdrawCash)!"--"}</td>
                <td>${(platform.fee)!"--"}</td>
                <td>${(platform.brokerage)!"--"}</td>
            </tr>
            [/#list]
        </table>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
        [#include "/admin/include/pagination.ftl"]
        [/@pagination]
    </form>
    <div id="trade_wrap"></div>
</body>
</html>