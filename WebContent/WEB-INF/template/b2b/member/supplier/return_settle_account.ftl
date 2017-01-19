<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-退货结算</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/supplier-index.js"></script>
    <script src="${base}/resources/b2b/js/foundation-datepicker.js"></script>
    <script src="${base}/resources/b2b/js/foundation-datepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>
<body class="bg-base">
[#include "/b2b/include/supplier_left.ftl"]

<div class="f-left rt">
[#include "/b2b/include/supplier_header.ftl"]
    <div class="breadcrumbs">
        <ul class="breadcrumb">
            <p>当前位置：</p>
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>退货结算</li>
        </ul>
    </div>
[#include "/b2b/include/supplier_top.ftl"]
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
            <p data-value="${status}" id="current_account">${status}</p>
            <ul>
                <li data-value="true" onclick="getAccountId(this)">
                    已结算
                </li>
                <li data-value="false" onclick="getAccountId(this)">
                    未结算
                </li>
            </ul>
        </div>
           <!--  <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
        <!-- <input type="text" placeholder="输入条形码/商品名称"  value="" class="sp-input" onchange="get_key_word(this)"> -->
        <a href="#" class="ex-item bg-pink" id="search_account">
            <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
        <a href="#" class="ex-item bg-dark-blue" id="export_ss"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;导出</a>
    </div>
    <form id="listForm" action="${base}/b2b/member/supplier/return_settle_account.jhtml" method="get">
    <input type="hidden" name="start_date" value="${start_time}" id="start_date">
    <input type="hidden" name="end_date" value="${end_time}" id="end_date">
    <input type="hidden" name="status" value="${status}" id="status">
    <input type="hidden" name="date_range" value="${date_range}" id="date_range_mark">
    <input type="hidden" name="menu" value="return_settle_account">
    <div class="tb-container" style="border:0;height:100%;">
        <table class="product-items">
            <thead>
                <tr>
                    <th></th>
                    <th>订单号</th>
                    <th>退货单号</th>
                    <th>创建日期</th>
                    <!-- <th>退货金额</th> -->
                    <th>结算金额</th>
                    <th>结算状态</th>
                    <th>结算日期</th>
                </tr>
            </thead>
            <tbody>
                [#list page.content as returns]
                <tr>
                    <td>${returns_index+1}</td>
                    <td>${returns.orderSn}</td>
                    <td>${returns.sn}</td>
                    <td>${returns.createDate}</td>
                    <!-- <td>${returns.amount}</td> -->
                    <td>${returns.settle}</td>
                    <td>
                        [#if returns.suppliered??&&returns.suppliered=="true"]
                        已结算
                        [#else]
                        未结算
                        [/#if]
                    </td>
                    <td>${returns.supplierDate}</td>
                </tr>
                [/#list]
            </tbody>
        </table>
        <div class="pag-container">
            <div class="page-wrap clearfix " style="float:right;margin:0px 50px;">
                [#include "/b2b/include/pagination.ftl"]
            </div>
        </div>
        </table>
    </div>
    </form>
    <script type="text/javascript">
        $(function(){
            //退货状态下拉列表
            $(".select p").click(function(e){
                $(".select").toggleClass('open');
                e.stopPropagation();
            });
            $(".select ul li").click(function(e){
                var _this=$(this);
                $(".select > p").text(_this.attr('data-value')=="true"?"已结算":"未结算");
                _this.addClass("Selected").siblings().removeClass("Selected");
                $(".select").removeClass("open");
                e.stopPropagation();
            });
            $(document).on('click',function(){
                $(".select").removeClass("open");
            })
            //日期下拉列表
            $(".seone p").click(function(e){
                $(".seone").toggleClass('open');
                e.stopPropagation();
            });
            $(".seone ul li").click(function(e){
                var _this=$(this);
                $(".seone > p").text(_this.attr('data-value')=="true"?"已结算":"未结算");
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
            //================查询提交按钮====================
            $("#search_account").click(function(){
                $("#listForm").submit();
            });
            //导出数据到excel
            $("#export_ss").click(function(){
                $.message("success","正在帮您导出，请稍后")
                $.ajax({
                    url:"${base}/b2b/member/supplier/return_settle_account_export.jhtml",
                    type:"get",
                    data:{
                        start_date:$("#start_date").val(),
                        end_date:$("#end_date").val(),
                        status:$("#status").val()
                    },
                    async:false,
                    dataType:"json",
                    success:function(data){
                        var html='<table style="display:none;" class="table2excel">'+
                        '<thead>'+
                            '<tr>'+
                                '<th>退货单号</th>'+
                                '<th>创建日期</th>'+
                                // '<th>退货金额</th>'+
                                '<th>结算金额</th>'+
                                '<th>结算状态</th>'+
                                '<th>结算日期</th>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>;'
                        $.each(data,function(i,obj){
                            html+='<tr>'+
                                        '<td>'+obj.sn+'</td>'+
                                        '<td>'+obj.date+'</td>'+
                                        // '<td>'+obj.amount+'</td>'+
                                        '<td>'+obj.cost+'</td>'+
                                        '<td>'+obj.status+'</td>'+
                                        '<td>'+obj.time+'</td>'+
                                    '</tr>';
                        });
                        html+='</tbody>'+
                        '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "退货结算",
                    filename: "退货结算",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true 
                });
            });
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
        //===============处理退货管理=====================
        //订单多商品的展示和隐藏
        function show_or_hide(obj){
            if($(obj).attr("flag")=="show"){
                $(obj).parent().find("li").show();
                $(obj).attr("flag","hide");
                $(obj).text("收起");
            }else{
                $(obj).parent().find("li").hide();
                $(obj).parent().find("li").first().show();
                $(obj).attr("flag","show");
                $(obj).text("展开更多");
            }
        }
        //控制拒绝理由的出现和隐藏
        function refuse_return(obj){
            if($(obj).parent().prev().attr("flag")=="true"){
                $(obj).parent().prev().show();
                $(obj).parent().prev().attr("flag","false");
            }else{
                $(obj).parent().prev().hide();
                $(obj).parent().prev().attr("flag","true");
            }
        }
        function check_refuse_return(obj){
            if($(obj).parent().next().attr("mark")=="true"){
                $(obj).parent().next().show();
                $(obj).parent().next().attr("mark","false");
            }else{
                $(obj).parent().next().hide();
                $(obj).parent().next().attr("mark","true");
            }
        }
        //供应商确认退货
        function agree_return(id){
            if(confirm("确认要退货吗")){
                $.ajax({
                    url:"${base}/b2b/member/supplier/confirm_return.jhtml",
                    type:"post",
                    data:{id:id},
                    dataType:"json",
                    success:function(data){
                        if(data.type=="success"){
                            location.reload();
                        }else{
                            $.message(data.type,data.content)
                        }
                    }
                });
            }
        }
        //拒绝退货
        function reject_return(id,obj){
            $.ajax({
                url:"${base}/b2b/member/supplier/refuse_return.jhtml",
                type:"post",
                data:{id:id,memo:$(obj).parent().parent().parent().parent().parent().prev().find("textarea").val()},
                dataType:"json",
                success:function(data){
                    if(data.type=="success"){

                        location.reload();
                    }else{
                        $.message(data.type,data.content)
                    }
                }
            });
        }
    </script>
</div>
<div id="trade_wrap"></div>
</body>
</html>
