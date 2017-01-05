<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-销售明细</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/foundation-datepicker.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/foundation-datepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>

<body class="bg-base">
[#include "/b2c/include/supplier_left.ftl"]

<form id="listForm" action="sale_detail.jhtml" method="get">
    <input id="dateRange" name="dateRange" value="${dateRange}" type="hidden"/>
    <input id="sellerId" name="sellerId" value="${(seller.id)!}" type="hidden"/>

    <div class="f-left rt">
    [#include "/b2c/include/supplier_header.ftl"]
        <div class="breadcrumbs">
            <ul class="breadcrumb">
                <p>当前位置：</p>
                <li>供货商管理后台</li>
                <li><a href="#">首页</a></li>
                <li>数据统计</li>
                <li>销售明细</li>
            </ul>
        </div>
    [#include "/b2c/include/supplier_top.ftl"]
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
            <!--  <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
            <input name="keyWords" value="${keyWords!}" type="text" placeholder="输入条形码/商品名称" class="sp-input" style="width:17%;">
            <a href="#" class="ex-item bg-pink" onclick="supplierSearch()">
                <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
            <a href="javascript:;" class="ex-item bg-dark-blue" id="export_ss"><i class="iconfont" style="font-size:20px;" >&#xe60a;</i>&nbsp;导出</a>
        </div>
        <div class="tb-container">
            <table class="product-items">
                <thead>
                    <tr>
                        <th></th>
                        <th>日期</th>
                        <th>商品名称</th>
                        <th>商品条码</th>
                        <th>销售商</th>
                        <th>客户</th>
                        <th>单位</th>
                        <th>销量</th>
                        <!-- <th>销售单价</th>
                        <th>销售金额</th> -->
                        <th>结算单价</th>
                        <th>结算金额</th>
                        <th>订单号</th>
                    </tr>
                </thead>
                <tbody>
                    [#list page.content as supplier]
                    <tr>
                        <td>${(page.pageNumber-1)*page.pageSize+supplier_index+1}</td>
                        <td>${supplier.create_date?string("yyyy年MM月dd日")}</td>
                        <td title="${supplier.full_name}">${abbreviate(supplier.full_name,18,"..")}</td>
                        <td>${supplier.barcode}</td>
                        <td>${supplier.seller}</td>
                        <td>${supplier.username}</td>
                        <td>${supplier.packag_unit_name}</td>
                        <td>${supplier.quantity}</td>
                        <!-- <td>${supplier.price}</td>
                        <td>${supplier.sale_amount}</td> -->
                        <td>${supplier.cost}</td>
                        <td>${supplier.settlement_amount}</td>
                        <td>${supplier.sn}</td>
                    </tr>
                    [/#list]
                </tbody>
            </table>
            <div class="pag-container">
                <div class="page-wrap clearfix " style="float:right;margin:0px 50px;">
                    [#include "/b2c/include/pagination.ftl"]
                </div>
            </div>
            <ul class="sell-container">
                <li>销售额： ￥${total_sale_amount}</li>
                <!-- <li>累计销售毛利： ￥${sale_gross_profit}</li> -->
            </ul>
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
        
        //========================================================================
        $("#export_ss").click(function(){
            $.message("success","正在帮您导出，请稍后")
            $.ajax({
                url:"${base}/b2c/member/supplier/sale_detail.jhtml",
                type:"get",
                data:{
                    begin_date:$("#dpd1").val(),
                    end_date:$("#dpd2").val(),
                    keyWords:$("[name='keyWords']").val()
                },
                async:false,
                dataType:"json",
                success:function(data){
                    var html='<table style="display:none;" class="table2excel">'+
                    '<thead>'+
                        '<tr>'+
                            '<th>日期</th>'+
                            '<th>商品名称</th>'+
                            '<th>商品条码</th>'+
                            '<th>销售商</th>'+
                            '<th>客户</th>'+
                            '<th>单位</th>'+
                            '<th>销售量</th>'+
                            // '<th>销售单价</th>'+
                            // '<th>销售金额</th>'+
                            '<th>结算单价</th>'+
                            '<th>结算金额</th>'+
                            '<th>订单号</th>'+
                        '</tr>'+
                    '</thead>'+
                    '<tbody>;'
                    $.each(data,function(i,obj){
                        html+=
                        '<tr>'+
                            '<td>'+obj.create_date+'</td>'+
                            '<td>'+obj.full_name+'</td>'+
                            '<td>'+obj.barcode+'</td>'+
                            '<td>'+obj.seller+'</td>'+
                            '<td>'+obj.username+'</td>'+
                            '<td>'+obj.packag_unit_name+'</td>'+
                            '<td>'+obj.quantity+'</td>'+
                            // '<td>'+obj.price+'</td>'+
                            // '<td>'+obj.sale_amount+'</td>'+
                            '<td>'+obj.cost+'</td>'+
                            '<td>'+obj.settlement_amount+'</td>'+
                            '<td>'+obj.sn+'</td>'+
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
                name: "销售明细",
                filename: "销售明细",
                fileext: ".xlsx",
                exclude_img: true,
                exclude_links: false,
                exclude_inputs: true 
            });
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
