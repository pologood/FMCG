<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>会员台帐流水 - Powered By rsico</title>
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
        $().ready(function () {
            $("#export_ss").click(function(){
                if(confirm("导出是当前页面数据导出，如想导出多条数据，可选择每页显示数")){
                    $.message("success","正在帮您导出，请稍后");
                    // $.ajax({
                    //   url:"${base}/admin/statistics/member_capital_total_export.jhtml",
                    //   type:"get",
                    //   data:{
                    //     beginDate:$("#beginDate").val(),
                    //     endDate:$("#endDate").val(),
                    //     keyword:$("#keyword").val()
                    //   },
                    //   async:false,
                    //   dataType:"json",
                    //   success:function(data){
                    //     var html='<table style="display:none;" class="table2excel">'+
                    //     '<thead>'+
                    //         '<tr>'+
                    //             '<th>对账日期</th>'+
                    //             '<th>会员名称</th>'+
                    //             '<th>手机号码</th>'+
                    //             '<th>总收入</th>'+
                    //             '<th>总支出</th>'+
                    //             '<th>充值</th>'+
                    //             '<th>购物</th>'+
                    //             '<th>提现</th>'+
                    //             '<th>贷款</th>'+
                    //             '<th>分润</th>'+
                    //             '<th>佣金</th>'+
                    //             '<th>收款</th>'+
                    //             '<th>其他收入</th>'+
                    //             '<th>其他支出</th>'+
                    //             '<th>红包收入</th>'+
                    //             '<th>红包支出</th>'+
                    //         '</tr>'+
                    //     '</thead>'+
                    //     '<tbody>';
                    //     $.each(data,function(i,obj){
                    //       html+=
                    //       '<tr>'+
                    //           '<td>'+obj.create_date+'</td>'+
                    //           '<td>'+obj.name+'</td>'+
                    //           '<td>'+obj.mobile+'</td>'+
                    //           '<td>'+obj.credit+'</td>'+
                    //           '<td>'+obj.debit+'</td>'+
                    //           '<td>'+obj.recharge+'</td>'+
                    //           '<td>'+obj.payment+'</td>'+
                    //           '<td>'+obj.withdraw+'</td>'+
                    //           '<td>'+obj.receipts+'</td>'+
                    //           '<td>'+obj.profit+'</td>'+
                    //           '<td>'+obj.rebate+'</td>'+
                    //           '<td>'+obj.cashier+'</td>'+
                    //           '<td>'+obj.income+'</td>'+
                    //           '<td>'+obj.outcome+'</td>'+
                    //           '<td>'+obj.coupon+'</td>'+
                    //           '<td>'+obj.couponuse+'</td>'+
                    //       '</tr>';
                    //     });
                    //     html+='</tbody>'+
                    //     '</table>';
                    //     $("#trade_wrap").html(html);
                    //   }
                    // });
                    //导出数据到excel
                    $(".table2excel").table2excel({
                      exclude: ".noExl",
                      name: "会员台帐流水",
                      filename: "会员台帐流水",
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
    <a href="${base}/admin/common/index.jhtml">首页</a> &raquo; 会员台帐流水
    <span>(${message("admin.page.total", page.total)})</span>
</div>
<form id="listForm" action="member_capital_total.jhtml" method="get">
    <div class="bar">
        <div class="buttonWrap">
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>刷新
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
                <input type="text" id="keyword" name="keyword" placeholder="会员名称、手机号" value="${keyword}" maxlength="200"/>
                <button type="button" onclick="set_page_number()">&nbsp;</button>
            </div>
        </div>
        <div class="menuWrap">
            <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${(beginDate)!}" placeholder="对账开始时间"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" style="text-align:center;" />&nbsp;-&nbsp;
            <input type="text" id="endDate" name="endDate" class="text Wdate" value="${(endDate)!}" placeholder="对账结束时间"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" style="text-align:center;"/>
        </div>
        <input type="button" class="button" value="查询" onclick="set_page_number()"/>
        <input type="button"id="export_ss" class="bar buttonWrap button" value="导出">
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th>
                <span>对账时间</span>
            </th>
            <th>
                <span>会员名称</span>
            </th>
            <th>
                <span>手机号码</span>
            </th>
            <th>
                <span>总收入</span>
            </th>
            <th>
                <span>总支出</span>
            </th>
            <th>
                <span>充值</span>
            </th>
            <th>
                <span>购物</span>
            </th>
            <th>
                <span>提现</span>
            </th>
            <th>
                <span>贷款</span>
            </th>
            <th>
                <span>分润</span>
            </th>
            <th>
                <span>佣金</span>
            </th>
            <th>
                <span>收款</span>
            </th>
            <th>
                <span>其他收入</span>
            </th>
            <th>
                <span>其他支出</span>
            </th>
            <th>
                <span>红包收入</span>
            </th>
            <th>
                <span>红包支出</span>
            </th>
            <th>
                <span>期初结余</span>
            </th>
            <th>
                <span>结余</span>
            </th>
        
        </tr>
        [#list page.content as memberCapital]
        <tr>
            <td>${(memberCapital.createDate?string("yyyy-MM-dd"))!"--"}</td>
            <td>${(memberCapital.member.name)!"--"}</td>
            <td>${(memberCapital.member.username)!"--"}</td>
            <td>${(memberCapital.credit)!"--"}</td>
            <td>${(memberCapital.debit)!"--"}</td>
            <td>${(memberCapital.recharge)!"--"}</td>
            <td>${(memberCapital.payment)!"--"}</td>
            <td>${(memberCapital.withdraw)!"--"}</td>
            <td>${(memberCapital.receipts)!"--"}</td>
            <td>${(memberCapital.profit)!"--"}</td>
            <td>${(memberCapital.rebate)!"--"}</td>
            <td>${(memberCapital.cashier)!"--"}</td>
            <td>${(memberCapital.income)!"--"}</td>
            <td>${(memberCapital.outcome)!"--"}</td>
            <td>${(memberCapital.coupon)!"--"}</td>
            <td>${(memberCapital.couponuse)!"--"}</td>
            <td>${(memberCapital.lastCapital)!"--"}</td>
            <td>${(memberCapital.capital)!"--"}</td>
        </tr>
        [/#list]
    </table>
    [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
        [#include "/admin/include/pagination.ftl"]
    [/@pagination]
</form>
<div id="trade_wrap"></div>
<script type="text/javascript">
    function set_page_number(){
        $("#pageNumber").val("1");
        $("#listForm").submit();
    }
</script>
</body>
</html>