<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="" content="" />
<title>${setting.siteName}-供应商-经营分析</title>
<meta name="keywords" content="${setting.siteName}-首页" />
<meta name="description" content="${setting.siteName}-首页" />
<script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/Count_Down.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/menuswitch.js"></script>
<script src="${base}/resources/b2c/js/foundation-datepicker.js"></script>
<script src="${base}/resources/b2c/js/foundation-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
<link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2c/css/supplier.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2c/css/twoCategory.css" type="text/css" rel="stylesheet"/>
<link href="${base}/resources/b2c/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
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
.select{
    width: 100px;
    height: 66px;
    display: block;
    float: left;
    color: #fff;
    font-size:14px;
    line-height: 66px;
    position: relative;
    cursor: pointer;
}
.select::after{
    content: "";
    display: block;
    width: 10px;
    height: 10px;
    border-left: 1px solid #fff;
    border-bottom: 1px solid #fff;
    top: 24px;
    right: -14px;
    position: absolute;
    transform: rotate(-46deg);
    transition: all .3s ease-in;
}
.select p{
    width: 100px;
    height: 66px;
    line-height: 66px;
    font-size: 14px;
    font-family: "microsoft yahei";
    color: #fff;
    overflow: hidden;
}
.select ul{
    width: 300px;
    display: block;
    font-size: 16px;
    background: #FFFFFF;
    position: absolute;
    top: 66px;
    left: -90px;
    max-height: 0px;
    overflow: hidden;
    transition: max-height .3s ease-in;
}
.select ul li{
    width: 100%;
    height: 30px;
    line-height: 30px;
    list-style: none;
    color: #666666;
    text-align: center;
}
.select ul li:hover{
    background: #D0D0D0;
}
@-webkit-keyframes slide-down{
    0%{transform: scale(1,0);}
    25%{transform: scale(1,1.2);}
    50%{transform: scale(1,0.85);}
    75%{transform: scale(1,1.05);}
    100%{transform: scale(1,1);}
}

.select.open ul{
    max-height: 250px;
    transform-origin: 50% 0;
    -webkit-animation: slide-down .5s ease-in;
    transition: max-height .2s ease-in;
}
.select.open::after{
    transform: rotate(134deg);
    transition: all .3s ease-in;
    top: 30px;
}
</style>
<body class="bg-base">
<!-- left -->
[#include "/b2c/include/supplier_left.ftl"]
<div class="f-left rt">
    [#include "/b2c/include/supplier_header.ftl"]
    <div class="breadcrumbs">
        <ul class="breadcrumb">
            <p>当前位置：</p>
            <li>供货商管理后台</li>
            <li><a href="${base}/b2c/member/supplier/index.jhtml">首页</a></li>
            <li>数据统计</li>
            <li>经营分析</li>
        </ul>
    </div>
   [#include "/b2c/include/supplier_top.ftl"]
    <div class="imenu">
        <!-- <div class="seone">
            <p data-value="${date_range}" id="date_range">${date_range}</p>
            <ul>
                <li data-value="今天" onclick="selectToday()">今天</li>
                <li data-value="昨天" onclick="selectYesterday()">昨天</li>
                <li data-value="本周" onclick="selectWeek()">本周</li>
                <li data-value="本月" onclick="selectMonth()">本月</li>
                <li data-value="本年" onclick="selectYear()">本年</li>i>
            </ul>
        </div> -->
        <div style="display: inline;float: left;margin-top: 20px;margin-left: 10px;font-size: 20px;color:white;">时间：</div>
        <input type="text" value="${start_time}" id="dpd1" class="ls-input" onchange="get_start_date(this)">
        <div style="display: inline;float: left;margin-top: 20px;margin-right:2%;font-size: 20px;color:white;">至</div>
        <input type="text" value="${end_time}" id="dpd2" class="ls-input" onchange="get_end_date(this)">
        <div class="select">
            <p data-value="${seller.name}" id="current_account">${seller.name}</p>
            <ul>
                [#list list as tenants]
                <li data-value="${tenants.tenant.name}" onclick="getAccountId(${tenants.tenant.id})">
                    ${tenants.tenant.name}
                </li>
                [/#list]
            </ul>
        </div>
           <!--  <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
        <input type="text" placeholder="输入条形码/商品名称"  value="${search_content}" class="sp-input" onchange="get_key_word(this)" style="width:17%;">
        <a href="#" class="ex-item bg-pink" id="search_account">
            <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
        <a href="javascript:;" class="ex-item bg-dark-blue" id="export_ss"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;导出</a>
    </div>
    <form id="listForm" action="${base}/b2c/member/supplier/management_analyse.jhtml" method="get">
        <input type="hidden" name="start_date" value="${start_time}" id="start_date">
        <input type="hidden" name="end_date" value="${end_time}" id="end_date">
        <input type="hidden" name="sellerId" value="${seller.id}" id="sellerId">
        <input type="hidden" name="date_range" value="${date_range}" id="date_range_mark">
        <input type="hidden" name="search_content" value="${search_content}" id="search_content">
        <input type="hidden" name="menu" value="management_analyse">
        <div class="tb-container">
            <table class="product-items">
                <thead>
                    <tr>
                        <th></th>
                        <th>商品名称</th>
                        <th>商品条码</th>
                        <th>销售商</th>
                        <th>规格</th>
                        <th>销量</th>
                        <th>结算价</th>
                        <th>结算额</th>
                    </tr>
                </thead>
                <tbody>
                    [#list page.content as orderItems]
                    <tr>
                        <td>${orderItems_index+1}</td>
                        <td>${orderItems.full_name}</td>
                        <td>${orderItems.barcode}</td>
                        <td>${orderItems.tenant}</td>
                        <td>${orderItems.unit}</td>
                        <td>${orderItems.quantity}</td>
                        <td>${orderItems.cost}</td>
                        <td>${orderItems.totalCost}</td>
                    </tr>
                    [/#list]
                </tbody>
            </table>
            <div class="pag-container">
                <div class="page-wrap clearfix ">
                    [#include "/b2c/include/pagination.ftl"]
                </div>
            </div>
            <!-- <div class="pag-container">
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                [#include "/b2c/include/pagination1.ftl"]
            [/@pagination]
            </div> -->
            <!-- <ul class="sell-container">
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
            </ul> -->
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
           
            $("#export_ss").click(function(){
                $.message("success","正在帮您导出，请稍后")
                $.ajax({
                    url:"${base}/b2c/member/supplier/management_analyse_export.jhtml",
                    type:"get",
                    data:{
                        start_date:$("#dpd1").val(),
                        end_date:$("#dpd2").val(),
                        keywords:$("#search_account").val()
                    },
                    async:false,
                    dataType:"json",
                    success:function(data){
                        var html='<table style="display:none;" class="table2excel">'+
                        '<thead>'+
                            '<tr>'+
                                '<th>商品名称</th>'+
                                '<th>商品条码</th>'+
                                '<th>销售商</th>'+
                                '<th>规格</th>'+
                                '<th>销量</th>'+
                                '<th>结算价价</th>'+
                                '<th>结算额</th>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>;'
                        $.each(data,function(i,obj){
                            html+='<tr>'+
                                        '<td>'+obj.full_name+'</td>'+
                                        '<td>'+obj.barcode+'</td>'+
                                        '<td>'+obj.tenant+'</td>'+
                                        '<td>'+obj.unit+'</td>'+
                                        '<td>'+obj.quantity+'</td>'+
                                        '<td>'+obj.cost+'</td>'+
                                        '<td>'+obj.totalCost+'</td>'+
                                    '</tr>';
                        });
                        html+='</tbody>'+
                        '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "经营分析",
                    filename: "经营分析",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true 
                });
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
        function get_key_word(obj){
            $("#search_content").val($(obj).val());
        }
    </script>
</div>
<div id="trade_wrap"></div>
</body>
</html>
