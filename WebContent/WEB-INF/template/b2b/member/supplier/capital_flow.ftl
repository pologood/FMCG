<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="" content="" />
<title>${setting.siteName}-供应商-资金流水</title>
<meta name="keywords" content="${setting.siteName}-首页" />
<meta name="description" content="${setting.siteName}-首页" />
<script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
<script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
<script src="${base}/resources/b2b/js/foundation-datepicker.js"></script>
<script src="${base}/resources/b2b/js/foundation-datepicker.zh-CN.js"></script>
<link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
<link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>
<style>
div.page-wrap .p-skip em {
    padding: 0 4px;
    font-size: 16px;
    line-height: 1.6;
}
div.page-wrap {
    margin: 0;
    float: right;
    padding-right:50px;
}
div.page-wrap .p-num a, div.page-wrap .p-num b {
    float: left;
    height: 24px;
    line-height: 24px;
    padding: 0 10px;
    font-size: 14px;
}
div.page-wrap .p-num a {
    color: #b0b0b0;
    border: 1px solid #d0d0d0;
    background-color: #fff;
}
div.page-wrap .p-skip .input-txt {
    float: left;
    width: 30px;
    height: 16px;
    margin: 0 3px;
    line-height: 16px;
    font-size: 14px;
    text-align: center;
    padding: 3px;
    color: #000;
    border: 1px solid #d0d0d0;
}

</style>
<body class="bg-base">
<!-- left -->
[#include "/b2b/include/supplier_left.ftl"]
<div class="f-left rt">
    [#include "/b2b/include/supplier_header.ftl"]
    <div class="breadcrumbs">
        <ul class="breadcrumb">
            <p>当前位置：</p>
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>数据统计</li>
            <li>资金流水</li>
        </ul>
    </div>
    [#include "/b2b/include/supplier_top.ftl"]
    <div class="imenu">
        <div class="seone">
            <p data-value="${date_range}" id="date_range">${date_range}</p>
            <ul>
                <li data-value="今天" onclick="selectToday()">今天</li>
                <li data-value="昨天" onclick="selectYesterday()">昨天</li>
                <li data-value="本周" onclick="selectWeek()">本周</li>
                <li data-value="本月" onclick="selectMonth()">本月</li>
                <li data-value="本年" onclick="selectYear()">本年</li>
            </ul>
        </div>
        <input type="text" value="${start_time}" id="dpd1" class="ls-input" onchange="get_start_date(this)">
        <input type="text" value="${end_time}" id="dpd2" class="ls-input" onchange="get_end_date(this)">
        <div class="select">
            <p data-value="[#if seller??]${seller.name}[/#if]" id="current_account">[#if seller??]${seller.name}[/#if]</p>
            <ul>
                [#list tenants as tenants]
                <li data-value="${tenants.tenant.name}" onclick="getAccountId(${tenants.tenant.id})">
                    ${tenants.tenant.name}
                </li>
                [/#list]
            </ul>
        </div>
           <!--  <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
       <!--  <input type="text" placeholder="输入条形码/商品名称"  class="sp-input"> -->
        <a href="javascript:;" class="ex-item bg-pink" id="search_account">
            <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
        <!--<a href="#" class="ex-item bg-dark-blue"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;打印</a>-->
    </div>
    <form id="listForm" action="${base}/b2b/member/supplier/capital_flow.jhtml" method="get">
        <input type="hidden" name="start_date" value="${start_time}" id="start_date">
        <input type="hidden" name="end_date" value="${end_time}" id="end_date">
        <input type="hidden" name="sellerId" value="[#if seller??]${seller.id}[/#if]" id="sellerId">
        <input type="hidden" name="date_range" value="${date_range}" id="date_range_mark">
        <input type="hidden" name="menu" value="capital_flow">
        <div class="tb-container">
            <table class="product-items">
                <thead>
                    <tr>
                        <th></th>
                        <th>日期</th>
                        <th>单据号</th>
                        <th>业务类型</th>
                        <th>往来客户</th>
                        <th>支付方式</th>
                        <th>收入</th>
                        <th>查看明细</th>
                    </tr>
                </thead>
                [#list page.content as page]
                <tr>
                    <th>${page_index+1}</th>
                    <th>${page.createDate}</th>
                    <th>${page.sn}</th>
                    <th>货款</th>
                    <th>${page.tenant.name}</th>
                    <th>平台支付</th>
                    <th><span>${page.amount}</span></th>
                    <th><a href="#">查看明细</a></th>
                </tr>
                [/#list]
            </table>
            <div class="pag-container">
                <div class="page-wrap clearfix ">
                   [#include "/b2b/include/pagination.ftl"]

                </div>
            </div>
            <ul class="sell-container">
                [#if total_sale_amount??&&total_sale_amount?has_content]
                <li>累计销售额： ￥${total_sale_amount}</li>
                    [#if total_settlement_amount==null||total_settlement_amount==0]
                    <li>累计销售毛利： ￥${total_sale_amount}</li>
                    [#else]
                    <li>累计销售毛利： ￥${total_sale_amount}-${total_settlement_amount}</li>
                    [/#if]
                [#else]
                <li>累计销售额： ￥0</li>
                <li>累计销售毛利：￥0</li>
                [/#if]
            </ul>
        </div>
    </form>
    <script>
        $(function(){
            //日期下拉列表
            $(".seone p").click(function(e){
                $(".seone").toggleClass('open');
                e.stopPropagation();
            });

            $(".seone ul li").click(function(e){
                var _this=$(this);
                $(".seone > p").text(_this.attr('data-value'));
                _this.addClass("Selected").siblings().removeClass("Selected");
                $(".seone").removeClass("open");
                e.stopPropagation();
            });
            $(document).on('click',function(){
                $(".seone").removeClass("open");
            })
            //店家下拉列表
            $(".select p").click(function(e){
                $(".select").toggleClass('open');
                e.stopPropagation();
            });
            $(".select ul li").click(function(e){
                var _this=$(this);
                $(".select > p").text(_this.attr('data-value'));
                _this.addClass("Selected").siblings().removeClass("Selected");
                $(".select").removeClass("open");
                e.stopPropagation();
            });
            $(document).on('click',function(){
                $(".select").removeClass("open");
            })
            //日期插件
            $('#js_inform').fdatepicker();
            var nowTemp = new Date();
            var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
            var checkin = $('#dpd1').fdatepicker({
                onRender: function (date) {
                    // return date.valueOf() < now.valueOf() ? 'disabled' : '';
                }
            }).on('changeDate', function (ev) {
                if (ev.date.valueOf() > checkout.date.valueOf()) {
                    var newDate = new Date(ev.date)
                    newDate.setDate(newDate.getDate() + 1);
                    checkout.update(newDate);
                }
                checkin.hide();
                $('#dpd2')[0].focus();
            }).data('datepicker');
            var checkout = $('#dpd2').fdatepicker({
                onRender: function (date) {
                    return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
                }
            }).on('changeDate', function (ev) {
                checkout.hide();
            }).data('datepicker');

            //====================================
            $("#search_account").click(function(){
                $("#listForm").submit();
            });
            
        });
        function selectToday() {
            var d1 = new Date();
            var d2 = new Date(d1);
            d2.setDate(d2.getDate() + 1);
            $("#dpd1").val(dateFormat(d1));
            $("#dpd2").val(dateFormat(d2));
            $("#date_range_mark").val("今天");
            get_start_date($("#dpd1"));
            get_end_date($("#dpd2"));
        }
        function selectYesterday() {
            var d1 = new Date();
            var d2 = new Date(d1);
            d1.setDate(d1.getDate() - 1);
            $("#dpd1").val(dateFormat(d1));
            $("#dpd2").val(dateFormat(d2));
            $("#date_range_mark").val("昨天");
            get_start_date($("#dpd1"));
            get_end_date($("#dpd2"));
        }
        function selectWeek() {
            var d1 = new Date();
            var d2 = new Date(d1);
            if (d1.getDay() == 7) {
                d1.setDate(d1.getDate() - 6);
            } else {
                d1.setDate(d1.getDate() - d1.getDay() + 1);
            }
            d2.setDate(d2.getDate() + 1);
            $("#dpd1").val(dateFormat(d1));
            $("#dpd2").val(dateFormat(d2));
            $("#date_range_mark").val("本周");
            get_start_date($("#dpd1"));
            get_end_date($("#dpd2"));
        }
        function selectMonth() {
            var d1 = new Date();
            var d2 = new Date(d1);
            d1.setDate(1);
            d2.setDate(d2.getDate() + 1);
            $("#dpd1").val(dateFormat(d1));
            $("#dpd2").val(dateFormat(d2));
            $("#date_range_mark").val("本月");
            get_start_date($("#dpd1"));
            get_end_date($("#dpd2"));
        }
        function selectYear() {
            var d1 = new Date();
            var d2 = new Date(d1);
            d1.setMonth(0);
            d1.setDate(1);
            d2.setDate(d2.getDate() + 1);
            $("#dpd1").val(dateFormat(d1));
            $("#dpd2").val(dateFormat(d2));
            $("#date_range_mark").val("本年");
            get_start_date($("#dpd1"));
            get_end_date($("#dpd2"));
        }
        function dateFormat(d) {
            var date = new Date(d);
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var day = date.getDate();
            return (month < 10 ? ("0" + month) : month) + "/" + (day < 10 ? ("0" + day) : day) + "/" + year;
        }
        function get_start_date(obj){
            var start_flag=false;
            $("#start_date").val($(obj).val());
        }
        function get_end_date(obj){
            var end_flag=false;
            $("#end_date").val($(obj).val());
        }
        function getAccountId(obj){
            $("#sellerId").val(obj);
        }
    </script>
</div>
</body>
</html>
