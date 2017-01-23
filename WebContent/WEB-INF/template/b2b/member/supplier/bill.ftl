<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-账单明细</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/foundation-datepicker.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/foundation-datepicker.zh-CN.js"></script>
    <!--<script type="text/javascript" src="${base}/resources/b2b/My97DatePicker/WdatePicker.js"></script>-->

    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>

</head>
<body class="bg-base">
[#include "/b2b/include/supplier_left.ftl"]

<form id="listForm" action="bill.jhtml" method="get">
    <input type="hidden" name="start_date" value="${start_time}" id="start_date">
    <input type="hidden" name="end_date" value="${end_time}" id="end_date">
    <div class="f-left rt">
        [#include "/b2b/include/supplier_header.ftl"]
        <div class="breadcrumbs">
            <ul class="breadcrumb">
                <p>当前位置：</p>
                <li>供货商管理后台</li>
                <li><a href="index.jhtml">首页</a></li>
                <li>查看账单</li>
            </ul>
        </div>
        [#include "/b2b/include/supplier_top.ftl"]
        <div class="tb-container">
            <div class="js_bill">
                <span>账单记录</span>
               
                <div style="float:left;margin-top:13px;font-size:16px;">开始时间：</div>
                <input type="text" value="${start_time}" id="dpd1" class="ls-input" onchange="get_start_date(this)" style="height:26px;margin: 8px 2% 13px 0;">
                <div style="float:left;margin-top:13px;font-size:16px;">结束时间：</div>
                <input type="text" value="${end_time}" id="dpd2" class="ls-input" onchange="get_end_date(this)" style="height:26px;margin: 8px 2% 13px 0;">
                <!-- <a href="javascript:;" id="export_ss">导出</a> -->
                <a href="javascript:;" class="ex-item bg-pink" id="search_account" style="line-height:30px;margin: 8px 0 0 35px;">
                    <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
                <a href="#" class="ex-item bg-dark-blue" id="export_ss" style="line-height:30px;margin: 8px 0 0 35px;"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;导出</a>
            </div>
            <ul class="tab-item">
                <li><a href="javascript:void(0);" class="active detail">明细</a></li>
                <li><a href="javascript:void(0);" class="statistics">统计</a></li>
            </ul>
            <div id="detail">
                <table class="tb-check">
                [#list page.content as deposit]
                    <tr>
                        <td>${deposit.createDate}</td>
                        <td>
                            [#if deposit.type=='recharge']充值
                            [#elseif deposit.type=='payment']购物
                            [#elseif deposit.type=='withdraw']提现
                            [#elseif deposit.type=='receipts']货款
                            [#elseif deposit.type=='profit']分润
                            [#elseif deposit.type=='rebate']佣金
                            [#elseif deposit.type=='cashier']收款
                            [#elseif deposit.type=='income']其他
                            [#elseif deposit.type=='outcome']其他
                            [#elseif deposit.type=='coupon']红包
                            [#elseif deposit.type=='couponuse']红包
                            [/#if]
                        </td>
                        <td>${deposit.memo}</td>
                        <td>
                            <span>
                                [#if deposit.type=='recharge'||deposit.type=='receipts'||deposit.type=='profit'||deposit.type=='cashier'||deposit.type=='income'||deposit.type=='coupon']
                                    [#if deposit.credit<0]${deposit.credit}[#else]+${deposit.credit}[/#if]
                                [#else]
                                    [#if deposit.debit<0]+${-deposit.debit}[#else]-${deposit.debit}[/#if]
                                [/#if]
                            </span>
                        </td>
                        <td style="padding-right: 5%;">[#if deposit.status=='none']处理中[#else]已完成[/#if]</td>
                    [#--<th><a href="#">详情</a></th>--]
                    </tr>
                [/#list]
                </table>
                <div class="pag-container">
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/b2b/include/pagination1.ftl"]
                [/@pagination]
                </div>
            </div>
            <div id="statistics" class="hidden">
                <ul class="info-item">
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-1.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>货款收入&nbsp;&nbsp;[#if receipts==0]
                                    0[#else]${((receipts/total_income)*100)?string('0.00')}[/#if]%</p>
                                <span>+￥${receipts?string("0.00")}</span>
                            </div>
                            <div class="plan bg-org2"
                                 style="width:[#if receipts==0]0[#else]${((receipts/total_income)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-2.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>分润收入&nbsp;&nbsp;[#if profit==0]
                                    0[#else]${((profit/total_income)*100)?string('0.00')}[/#if]%</p>
                                <span>+￥${profit?string('0.00')}</span>
                            </div>
                            <div class="plan bg-org2"
                                 style="width:[#if profit==0]0[#else]${((profit/total_income)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-3.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>交易佣金&nbsp;&nbsp;[#if rebate==0]
                                    0[#else]${((rebate/total_outcome)*100)?string('0.00')}[/#if]%</p>
                                <span>-￥${rebate?string('0.00')}</span>
                            </div>
                            <div class="plan bg-org2"
                                 style="width:[#if rebate==0]0[#else]${((rebate/total_outcome)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                </ul>
                <!--  -->
                <ul class="info-item">
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-4.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>购物支出&nbsp;&nbsp;[#if payment==0]
                                    0[#else]${((payment/total_outcome)*100)?string('0.00')}[/#if]%</p>
                                <span>-￥${payment?string('0.00')}</span>
                            </div>
                            <div class="plan bg-cyan"
                                 style="width:[#if payment==0]0[#else]${((payment/total_outcome)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-5.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>线下收款&nbsp;&nbsp;[#if cashier==0]
                                    0[#else]${((cashier/total_income)*100)?string('0.00')}[/#if]%</p>
                                <span>+￥${cashier?string('0.00')}</span>
                            </div>
                            <div class="plan bg-cyan"
                                 style="width:[#if cashier==0]0[#else]${((cashier/total_income)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-6.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>其他支出&nbsp;&nbsp;[#if outcome==0]
                                    0[#else]${((outcome/total_outcome)*100)?string('0.00')}[/#if]%</p>
                                <span>-￥${outcome?string('0.00')}</span>
                            </div>
                            <div class="plan bg-cyan"
                                 style="width:[#if outcome==0]0[#else]${((outcome/total_outcome)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-7.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>其他收款&nbsp;&nbsp;[#if income==0]
                                    0[#else]${((income/total_income)*100)?string('0.00')}[/#if]%</p>
                                <span>+￥${income?string('0.00')}</span>
                            </div>
                            <div class="plan bg-cyan"
                                 style="width:[#if income==0]0[#else]${((income/total_income)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                </ul>
                <!--  -->
                <ul class="info-item">
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-8.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>充值&nbsp;&nbsp;[#if recharge==0]
                                    0[#else]${((recharge/total_income)*100)?string('0.00')}[/#if]%</p>
                                <span>+￥${recharge?string('0.00')}</span>
                            </div>
                            <div class="plan bg-grayness"
                                 style="width:[#if recharge==0]0[#else]${((recharge/total_income)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="left">
                            <img src="${base}/resources/b2b/images/info-9.png" alt=""/>
                        </div>
                        <div class="right">
                            <div class="title">
                                <p>提现&nbsp;&nbsp;[#if withdraw==0]
                                    0[#else]${((withdraw/total_outcome)*100)?string('0.00')}[/#if]%</p>
                                <span>-￥${withdraw?string("0.00")}</span>
                            </div>
                            <div class="plan bg-grayness"
                                 style="width:[#if withdraw==0]0[#else]${((withdraw/total_outcome)*100)?string('0.00')}[/#if]%;">
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</form>
<div id="trade_wrap"></div>
</body>
<script>
//    js_informDate();
//    function js_informDate() {
//        var checkout = $('#js_inform').fdatepicker({
//            format: "mm/yyyy",
//            linkFormat: "yy/mm",
//            startView: "year"
//        }).on('changeMonth', function (ev) {
//            checkout.update(ev.date);
//            checkout.hide();
////            checkout.unbind('changeMonth');
////            js_informDate();
//            window.location.href = "bill.jhtml?date=" + $("#js_inform").val();
//            checkout.remove();
//        }).data('datepicker');
//    }
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
        //处理日期插件
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
        //==============================
        $("#search_account").click(function(){
            $("#listForm").submit();
        });
        //导出数据到excel
        $("#export_ss").click(function(){
            $.message("success","正在帮您导出，请稍后");
            $.ajax({
                url:"${base}/b2b/member/supplier/bill_detail.jhtml",
                type:"get",
                data:{
                    start_date:$("#start_date").val(),
                    end_date:$("#end_date").val()
                },
                async:false,
                dataType:"json",
                success:function(data){
                    var html='<table style="display:none;" class="table2excel">'+
                    '<thead>'+
                        '<tr>'+
                            '<th>创建日期</th>'+
                            '<th>类型</th>'+
                            '<th>备注</th>'+
                            '<th>资金流水</th>'+
                            '<th>账单状态</th>'+
                        '</tr>'+
                    '</thead>'+
                    '<tbody>;';
                    $.each(data,function(i,obj){
                        html+='<tr>'+
                                    '<td>'+obj.date+'</td>'+
                                    '<td>'+obj.type+'</td>'+
                                    '<td>'+obj.memo+'</td>'+
                                    '<td>'+obj.money+'</td>'+
                                    '<td>'+obj.status+'</td>'+
                                '</tr>';
                    });
                    html+='</tbody>'+
                    '</table>';
                    $("#trade_wrap").html(html);
                }
            });
            $(".table2excel").table2excel({
                exclude: ".noExl",
                name: "账单信息",
                filename: "账单信息",
                fileext: ".xlsx",
                exclude_img: true,
                exclude_links: false,
                exclude_inputs: true 
            });
        });
    });
    function getBill() {
        window.location.href = "bill.jhtml?date=" + $dp.cal.getNewDateStr();
    }

    $(".detail").click(function () {
        $(this).addClass("active");
        $(".statistics").removeClass("active");
        $("#detail").show();
        $("#statistics").hide();
    });

    $(".statistics").click(function () {
        $(this).addClass("active");
        $(".detail").removeClass("active");
        $("#detail").hide();
        $("#statistics").show();
    });
    //================处理查询====================
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
            $("#start_date").val($(obj).val());
        }
        function get_end_date(obj){
            
            $("#end_date").val($(obj).val());
        }
        function getAccountId(obj){
            $("#status").val($(obj).attr("data-value"));
        }
        function get_key_word(obj){
            $("#search_content").val($(obj).val());
        }
</script>
</html>
