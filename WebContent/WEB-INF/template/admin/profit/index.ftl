<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.index.build")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $filterSelect = $("#filterSelect");
            var $filterSelect2 = $("#filterSelect2");
            var $filterSelect3 = $("#filterSelect3");
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
            $filterSelect2.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });
            $filterSelect3.mouseover(function () {
                var $this = $(this);
                var offset = $this.offset();
                var $menuWrap = $this.closest("div.menuWrap");
                var $popupMenu = $menuWrap.children("div.popupMenu");
                $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
                $menuWrap.mouseleave(function () {
                    $popupMenu.hide();
                });
            });
          
			$("#export_ss").click(function(){
                if(confirm("导出是当前页面数据导出，如想导出多条数据，可选择每页显示数")){
    	           $.message("success","正在帮您导出，请稍后");
    	          // $.ajax({
    	          //   url:"${base}/admin/profit/index_export.jhtml",
    	          //   type:"get",
    	          //   data:{
    	          //     beginDate:$("#beginDate").val(),
    	          //     endDate:$("#endDate").val(),
    	          //     rebate_status:$("#profit_status").val(),
    	          //     order_type:$("#orderType").val(),
    	          //     rebate_type:$("#rebate_type").val()
    	          //   },
    	          //   async:false,
    	          //   dataType:"json",
    	          //   success:function(data){
    	          //     var html='<table style="display:none;" class="table2excel">'+
    	          //       '<thead>'+
    	          //         '<tr>'+
    	          //           '<th>创建日期</th>'+
    	          //           '<th>描述信息</th>'+
    	          //           '<th>订单类型</th>'+
    	          //           '<th>分润类型</th>'+
    	          //           '<th>分润状态</th>'+
    	          //           '<th>商家</th>'+
    	          //           '<th>订单单号</th>'+
    	          //           '<th>支付单号</th>'+
    	          //           '<th>优惠码</th>'+
    	          //           '<th>分配人</th>'+
    	          //           '<th>佣金金额</th>'+
    	          //           '<th>分配比例</th>'+
    	          //           '<th>分配金额</th>'+
    	          //         '</tr>'+
    	          //       '</thead>'+
    	          //     '<tbody>';
    	          //     $.each(data,function(i,obj){
    	          //       html+=
    	          //         '<tr>'+
    	          //           '<td>'+obj.create_date+'</td>'+
    	          //           '<td>'+obj.description+'</td>'+
    	          //           '<td>'+obj.order_type+'</td>'+
    	          //           '<td>'+obj.rebate_type+'</td>'+
    	          //           '<td>'+obj.rebate_status+'</td>'+
    	          //           '<td>'+obj.tenant_name+'</td>'+
    	          //           '<td>'+obj.order_sn+'</td>'+
    	          //           '<td>'+obj.paybill_sn+'</td>'+
    	          //           '<td>'+obj.coupon_code+'</td>'+
    	          //           '<td>'+obj.name+'</td>'+
    	          //           '<td>'+obj.amount+'</td>'+
    	          //           '<td>'+obj.brokerage+'</td>'+
    	          //           '<td>'+obj.percent+'</td>'+
    	          //         '</tr>';
    	          //     });
    	          //     html+='</tbody>'+
    	          //     '</table>';
    	          //     $("#trade_wrap").html(html);
    	          //   }
    	          // });
    	          //导出数据到excel
    	          $(".table2excel").table2excel({
    	            exclude: ".noExl",
    	            name: "分润报表",
    	            filename: "分润报表",
    	            fileext: ".xls",
    	            exclude_img: true,
    	            exclude_links: true,
    	            exclude_inputs: true
    	          });
                }
	        });
        });
        function getStatus(types,obj) {
            $(obj).addClass("checked").parent().siblings().find("a").removeClass("checked");
            if(types=="rebate"){
            	$("#profit_status").val($(obj).attr("val"));
            }else if(types=="order_type"){
            	$("#orderType").val($(obj).attr("val"));
            }else if(types=="rebate_type"){
            	$("#rebate_type").val($(obj).attr("val"));
            }
            $("#listForm").submit();
            return false;
        }
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 分润报表
</div>
<form id="listForm" action="${base}/admin/profit/index.jhtml" method="get">
    <input type="hidden" id="profit_status" name="rebate_status" value="${status}">
    <input type="hidden" id="rebate_type" name="rebate_type" value="${rebate_type}">
    <input type="hidden" id="orderType" name="order_type" value="${order_type}">
    <div class="bar">
        <div class="buttonWrap">
            <a href="javascript:;" id="refreshButton" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
            </a>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect" class="button">
                    分润状态<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="status" val="none"[#if status == "none"] class="checked"[/#if]
                               onclick="getStatus('rebate',this)">未分润</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="status" val="share"[#if status == "share"]
                               class="checked"[/#if] onclick="getStatus('rebate',this)">已分润</a>
                        </li>

                    </ul>
                </div>
            </div>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect2" class="button">
                    订单类型<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="orderType" val="order"[#if order_type == "order"] class="checked"[/#if]
                               onclick="getStatus('order_type',this)">订单分润</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="orderType" val="payBill"[#if order_type == "payBill"]
                               class="checked"[/#if] onclick="getStatus('order_type',this)">优惠买单</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="orderType" val="coupon"[#if order_type == "coupon"] class="checked"[/#if]
                               onclick="getStatus('order_type',this)">代金券</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="orderType" val="none"[#if order_type == "none"] class="checked"[/#if]
                               onclick="getStatus('order_type',this)">其他</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="menuWrap">
                <a href="javascript:;" id="filterSelect3" class="button">
                    分润类型<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                    <ul id="filterOption" class="check">
                        <li>
                            <a href="javascript:;" name="type" val="platform"[#if rebate_type == "platform"] class="checked"[/#if]
                               onclick="getStatus('rebate_type',this)">平台佣金</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="type" val="extension"[#if rebate_type == "extension"]
                               class="checked"[/#if] onclick="getStatus('rebate_type',this)">推广佣金</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="type" val="sale"[#if rebate_type == "sale"] class="checked"[/#if]
                               onclick="getStatus('rebate_type',this)">销售佣金</a>
                        </li>
                        <li>
                            <a href="javascript:;" name="type" val="rebate"[#if rebate_type == "rebate"]
                               class="checked"[/#if] onclick="getStatus('rebate_type',this)">消费返利</a>
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
                            <a href="javascript:;"[#if page.pageSize == 500] class="current"[/#if] val="500">500</a>
                        </li>
                        <li>
                            <a href="javascript:;"[#if page.pageSize == 1000] class="current"[/#if] val="1000">1000</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
       <!--  <div class="menuWrap">
            <div class="search">
                <input type="text" id="searchValue" name="searchValue" value="${keyword}" maxlength="200"/>
                <button type="submit">&nbsp;</button>
            </div>
        </div> -->
        <div class="menuWrap">
            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                   value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" placeholder="开始时间"/>
            -&nbsp;<input type="text" id="endDate" name="endDate" class="text Wdate"
                    value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" placeholder="结束时间"/>
            <input type="button" class="button" value="查询" onclick="set_page_number()">
        </div>
        <input type="button"id="export_ss" class="bar buttonWrap button" value="导出">
    </div>
    <table id="listTable" class="list table2excel">
        <tr>
            <th>
                <span>创建日期</span>
            </th>
            <th>
                <span>描述信息</span>
            </th>
            <th>
                <span>订单类型</span>
            </th>
            <th>
                <span>分润类型</span>
            </th>
            <th>
                <span>分润状态</span>
            </th>
            <th>
                <span>商家</span>
            </th>
            <th>
                <span>分润订单号</span>
            </th>
            <th>
                <span>支付单号</span>
            </th>
            <th>
                <span>优惠码</span>
            </th>
            <th>
                <span>分配者</span>
            </th>
            <th>
                <span>佣金金额</span>
            </th>
            <th>
                <span>分配比例</span>
            </th>
            <th>
                <span>分配金额</span>
            </th>
        </tr>
    [#list page.content as rebate]
        <tr>
            <td>
            ${(rebate.createDate)!}
            </td>
            <td>
            ${(rebate.description)!}
            </td>
            <td>
                [#if rebate.orderType=="order"]
                    订单分润
                [#elseif rebate.orderType=="payBill"]
                	优惠买单
                [#elseif rebate.orderType=="coupon"]
                	代金券
                [#elseif rebate.orderType=="none"]
                	其他
                [/#if]
            </td>
            <td>
                [#if rebate.type=="platform"]
                   	平台佣金
                [#elseif rebate.type=="extension"]
                	推广佣金
                [#elseif rebate.type=="rebate"]
                	消费返利
                [#elseif rebate.type=="sale"]
                	销售佣金
                [/#if]
            </td>
            <td>
                [#if rebate.status=="none"]
                    未入账
                [#elseif rebate.status=="success"]
                    已入账
                [/#if]
            </td>
            <td>
            [#if rebate.orderType=="order"]
                [#if rebate.trade??]${(rebate.trade.tenant.name)!'--'}[#else]--[/#if]
            [#elseif rebate.orderType=="payBill"]
            	[#if rebate.payBill??]${(rebate.payBill.tenant.name)!'--'}[#else]--[/#if]
            [#elseif rebate.orderType=="coupon"]
            	[#if rebate.couponCode??]${(rebate.couponCode.coupon.tenant.name)!'--'}[#else]--[/#if]
            [#else]
            	--
            [/#if]
            </td>
     		<td>
            [#if rebate.trade??]${(rebate.trade.order.sn)!'--'}[#else]--[/#if]
            </td>
            <td>
            [#if rebate.payBill??]${(rebate.payBill.sn)!'--'}[#else]--[/#if]
            </td>
            <td>
            [#if rebate.couponCode??]${(rebate.couponCode.code)!'--'}[#else]--[/#if]
            </td>
            <td>
            [#if rebate.member??]${(rebate.member.name)!'--'}[#else]--[/#if]
            </td>
            <td>
            ${(rebate.amount)!0}
            </td>
            <td>
            ${(rebate.percent*100)!0}%
            </td>
            <td>
            ${(rebate.brokerage)!0}
            </td>
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