<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="" content="" />
<title>${setting.siteName}-供应商-我的商品</title>
<meta name="keywords" content="${setting.siteName}-首页" />
<meta name="description" content="${setting.siteName}-首页" />
<script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/Count_Down.js"></script>
<script type="text/javascript" src="${base}/resources/b2c/js/menuswitch.js"></script>
<script src="${base}/resources/b2c/js/foundation-datepicker.js"></script>
<script src="${base}/resources/b2c/js/foundation-datepicker.zh-CN.js"></script>
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
            <li><a href="#">首页</a></li>
            <li>我的商品</li>
            
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
                <li data-value="本年" onclick="selectYear()">本年</li>
            </ul>
        </div> -->
        <div style="display: inline;float: left;margin-top: 20px;margin-left: 10px;font-size: 20px;color:white;">时间：</div>
        <input type="text" value="${start_time}" id="dpd1" class="ls-input" onchange="get_start_date(this)">
        <div style="display: inline;float: left;margin-top: 20px;margin-right:2%;font-size: 20px;color:white;">至</div>
        <input type="text" value="${end_time}" id="dpd2" class="ls-input" onchange="get_end_date(this)">
        <div class="select">
            <p data-value="${seller.name}">${seller.name}</p>
            <ul>
                [#list list as tenant]
                <li data-value="${tenant.tenant.name}" onclick="getAccountId(${tenant.tenant.id})">
                    ${tenant.tenant.name}
                </li>
                [/#list]
            </ul>
        </div>
           <!--  <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
        <input type="text" placeholder="输入条形码/商品名称"  value="${search_content}" class="sp-input" onchange="get_key_word(this)" style="width:17%;">
        <a href="javascript:;" class="ex-item bg-pink" id="search_account">
            <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
        <!--<a href="#" class="ex-item bg-dark-blue"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;打印</a>-->
    </div>
    <form id="listForm" action="${base}/b2c/member/supplier/my_product.jhtml" method="get">
        <input type="hidden" name="start_date" value="${start_time}" id="start_date">
        <input type="hidden" name="end_date" value="${end_time}" id="end_date">
        <input type="hidden" name="sellerId" value="${sellerId}" id="sellerId">
        <input type="hidden" name="date_range" value="${date_range}" id="date_range_mark">
        <input type="hidden" name="search_content" value="${search_content}" id="search_content">
        <input type="hidden" name="menu" value="my_product">
        <div class="tb-container">
            <table class="product-items">
                <thead>
                    <tr>
                        <th></th>
                        <!--<th>日期</th>-->
                        <th>商品名称</th>
                        <th>商品条码</th>
                        <th>规格</th>
                        <th>月销量</th>
                        <th>点击量</th>
                        <th>锁定库存</th>
                        <th>库存</th>
                    </tr>
                </thead>
                [#list page.content as products]
                <tr>
                    <th>${products_index+1}</th>
                    <!--<th>2016年3月11日</th>-->
                    <th>${products.name}</th>
                    <th>${products.barcode}</th>
                    <th>${products.unit}</th>
                    <th>${products.monthSales}</th>
                    <th>${products.hits}</th>
                    <th>${products.allocatedStock}</th>
                    <th>
                        [#--<input type="text" value="${products.stock}" class="inv-num" onclick="show_save_button(this)"/>--]
                        [#--<a href="javascript:;" class="inv" style="display:none;width:45px;height:30px;line-height:30px;float:right;margin-right:30px;" onclick="save_stock(${products.id},this,${products.allocatedStock})">保存</a>--]
                        ${products.stock}
                    </th>
                </tr>
                [/#list]
            </table>
            <div class="pag-container">
                <div class="page-wrap clearfix ">
                    [#include "/b2c/include/pagination.ftl"]
                </div>
            </div>
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
    <script type="text/javascript">
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
        $("#start_date").val($(obj).val());
    }
    function get_end_date(obj){
        $("#end_date").val($(obj).val());
    }
    function getAccountId(obj){
        $("#sellerId").val(obj);
    }
    function get_key_word(obj){
        $("#search_content").val($(obj).val());
    }
    //修改库存
    function save_stock(id,obj,lockStock){
        if($(obj).prev().val()<lockStock){
            alert("库存数不能小于锁定库存数");
        }else{
            $.ajax({
                url:"${base}/b2c/member/supplier/save_stock.jhtml",
                type:"post",
                data:{
                    id:id,
                    stock:$(obj).prev().val()
                },
                dataType:"json",
                success:function(message){
                    alert(message.content);
                    $(obj).hide();
                }
            });
        }
    }
    //动态显示修改库存保存按钮
    function show_save_button(obj){
        $(obj).next().show().parent().parent().siblings().find("a").hide();
    }
    </script>
    
</div>
</body>
</html>
