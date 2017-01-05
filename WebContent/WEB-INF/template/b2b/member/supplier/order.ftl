<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-订单查询</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/foundation-datepicker.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/foundation-datepicker.zh-CN.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>

<body class="bg-base">
[#include "/b2b/include/supplier_left.ftl"]

<form id="listForm" action="index.jhtml" method="get">
    <input id="dateRange" name="dateRange" value="${dateRange}" type="hidden"/>
    <input id="sellerId" name="sellerId" value="${(seller.id)!}" type="hidden"/>

    <div class="f-left rt" style="position: relative;">
    [#include "/b2b/include/supplier_header.ftl"]
        <div class="breadcrumbs">
            <ul class="breadcrumb">
                <p>当前位置：</p>
                <li>供货商管理后台</li>
                <li><a href="#">首页</a></li>
                <li>订单查询</li>
            [#--<li>销售统计</li>--]
            </ul>
        </div>
    [#include "/b2b/include/supplier_top.ftl"]
        <div class="imenu">
            <!-- <div class="seone">
                <p data-value="${dateRange}">${dateRange}</p>
                <ul>
                    <li data-value="今天" onclick="selectToday()">今天</li>
                    <li data-value="昨天" onclick="selectYesterday()">昨天</li>
                    <li data-value="本周" onclick="selectWeek()">本周</li>
                    <li data-value="本月" onclick="selectMonth()">本月</li>
                    <li data-value="本年" onclick="selectYear()">本年</li>
                </ul>
            </div> -->
            <div style="display: inline;float: left;margin-top: 20px;margin-left: 10px;font-size: 20px;color:white;">时间：</div>
            <input name="begin_date" type="text" value="[#if begin_date??]${begin_date?string('MM/dd/yyyy')}[/#if]"
                   id="dpd1" class="ls-input">
            <div style="display: inline;float: left;margin-top: 20px;margin-right:2%;font-size: 20px;color:white;">至</div>
            <input name="end_date" type="text" value="[#if end_date??]${end_date?string('MM/dd/yyyy')}[/#if]" id="dpd2"
                   class="ls-input">
            <div class="select">
                <p data-value="${(seller.id)!}">${(seller.name)!}</p>
                <ul>
                [#list list as tenantRelation]
                    <li data-value="${tenantRelation.tenant.id}">${tenantRelation.tenant.name}</li>
                [/#list]
                </ul>
            </div>
             <!-- <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
            <input name="keyWords" value="${keyWords!}" type="text" placeholder="输入条形码/商品名称" class="sp-input">
            <a href="#" class="ex-item bg-pink" onclick="supplierSearch()">
                <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
            <!--<a href="#" class="ex-item bg-dark-blue"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;打印</a>-->
        </div>
        <div class="tb-container">
            <table class="product-items">
                <thead>
                <tr>
                    <th></th>
                    <th>日期</th>
                    <th>订单号</th>
                    <th>销售商</th>
                    <th>收货人</th>
                    <th>交易状态</th>
                    <th>结算金额</th>
                    <th>操作</th>
                </tr>
                </thead>
            [#--[#list page.content as supplier]--]
                [#--<tr>--]
                    [#--<th>${(page.pageNumber-1)*page.pageSize+supplier_index+1}</th>--]
                    [#--<th>${supplier.create_date?string("yyyy年MM月dd日")}</th>--]
                    [#--<th>${supplier.sn}</th>--]
                    [#--<th>${supplier.seller}</th>--]
                    [#--<th>${supplier.consignee}</th>--]
                    [#--<th>--]
                        [#--[#if supplier.order_status==0]--]
                            [#--未确认[#elseif supplier.order_status==1]--]
                            [#--已确认[#elseif supplier.order_status==2]--]
                            [#--已完成[#elseif supplier.order_status==3]--]
                            [#--已取消[/#if]--]
                    [#--</th>--]
                    [#--<th>${supplier.transaction_amount}</th>--]
                    [#--<th><a href="sale_detail.jhtml">查看明细</a></th>--]
                [#--</tr>--]
            [#--[/#list]--]
            [#list page.content as trade]
                <tr>
                    <th>${(page.pageNumber-1)*page.pageSize+trade_index+1}</th>
                    <th>${trade.createDate?string("yyyy年MM月dd日")}</th>
                    <th>${trade.order.sn}</th>
                    <th>${trade.tenant.name}</th>
                    <th>${trade.order.consignee}</th>
                    <th>
                        ${trade.finalOrderStatus[0].desc}
                    </th>
                    <th>
                        [#assign transaction_amount=0]
                        [#list trade.orderItems as orderItem]
                            [#if orderItem.supplier==member.tenant]
                                [#assign transaction_amount=transaction_amount+orderItem.cost*orderItem.quantity]
                            [/#if]
                        [/#list]
                        [#if transaction_amount!=0]
                            ${transaction_amount}
                        [/#if]
                    </th>
                    <th><a href="sale_detail.jhtml">查看明细</a></th>
                </tr>
            [/#list]
            </table>
            <div class="pag-container">
                <div class="page-wrap clearfix " style="float:right;margin:0px 50px;">
                    [#include "/b2b/include/pagination.ftl"]
                </div>
            </div>
            <!-- <ul class="sell-container">
                <li>累计销售额： ￥${total_sale_amount}</li>
                <li>累计销售毛利： ￥${sale_gross_profit}</li>
            </ul> -->
        </div>
    </div>
</form>
<div id="trade_wrap"></div>
</body>
<script type="text/javascript">
    $(function () {
        $('#js_inform').fdatepicker();
        var nowTemp = new Date();
        var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
        //第一个日期
        var checkin = $('#dpd1').fdatepicker({
            onRender: function (date) {
//                return date.valueOf() < now.valueOf() ? 'disabled' : '';
            }
        }).on('changeDate', function (ev) {
            if (ev.date.valueOf() > checkout.date.valueOf()) {
                var newDate = new Date(ev.date);
                newDate.setDate(newDate.getDate() + 1);
                checkout.update(newDate);
            }
            checkin.hide();
            $('#dpd2')[0].focus();
        }).data('datepicker');
        //第二个日期
        var checkout = $('#dpd2').fdatepicker({
            onRender: function (date) {
                return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
            }
        }).on('changeDate', function (ev) {
            checkout.hide();
        }).data('datepicker');

        //代理商家选择
        $(".select p").click(function (e) {
            $(".select").toggleClass('open');
            e.stopPropagation();
        });
        $(".select ul li").click(function (e) {
            var _this = $(this);
            $(".select > p").text(_this.text());
            $(".select > p").attr("data-value", _this.attr('data-value'));
            _this.addClass("Selected").siblings().removeClass("Selected");
            $(".select").removeClass("open");
            e.stopPropagation();
        });
        $(document).on('click', function () {
            $(".select").removeClass("open");
        });

        //报表日期范围选择
        $(".seone p").click(function (e) {
            $(".seone").toggleClass('open');
            e.stopPropagation();
        });
        $(".seone ul li").click(function (e) {
            var _this = $(this);
            $(".seone > p").text(_this.text());
            $(".seone > p").attr("data-value", _this.attr('data-value'));
            _this.addClass("Selected").siblings().removeClass("Selected");
            $(".seone").removeClass("open");
            e.stopPropagation();
        });
        $(document).on('click', function () {
            $(".seone").removeClass("open");
        });

    });

    function supplierSearch() {
        $("#dateRange").val($(".seone > p").text());
        $("#sellerId").val($(".select > p").attr("data-value"));
        $("#pageNumber").val(1);
        $('#listForm').submit();
    }

    function selectToday() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }
    function selectYesterday() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d1.setDate(d1.getDate() - 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
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
    }

    function selectMonth() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d1.setDate(1);
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }

    function selectYear() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d1.setMonth(0);
        d1.setDate(1);
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }

    function dateFormat(d) {
        var date = new Date(d);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        return (month < 10 ? ("0" + month) : month) + "/" + (day < 10 ? ("0" + day) : day) + "/" + year;
    }
</script>
</html>
