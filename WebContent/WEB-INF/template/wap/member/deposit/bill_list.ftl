<!DOCTYPE html>
<html lang="en">
<head>
    <title>账单</title>
    [#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <style type="text/css">
        body{
            font-family:"Microsoft YaHei";
            background-color:#E9E9E9;

        }
        .weui_cells:before{
            border-top:0px; 
            left:0px;
        }
        .weui_cells:after{
            border-bottom:0px; 
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <script src="${base}/resources/wap/2.0/js/LCalendar.js"></script>

    <script type="text/x-handlebars-template" id="bill_wraper_1">
        <div id="bill_intail_page">
            {{#each data}}
            <div class="weui_cells" style="margin-top:10px;line-height:30px;">
              <div class="weui_cell" style="font-size:15px;padding:2px 15px;">
                <div style="font-size:20px;margin-right:10px;" id="type">{{iftype type}}</div>
                <div class="weui_cell_bd weui_cell_primary" >
                  {{memo}}
                  <p style="color:#888;">{{formatdate create_date "yyyy-MM-dd"}}</p>
                </div>
                <div class="weui_cell_ft" id="weui_cell_ft">
                    {{#ifCond type "recharge"}}
                        <p style='color:balck;'>￥{{zero credit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "receipts"}}
                        <p style='color:balck;'>￥{{zero credit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "profit"}}
                        <p style='color:balck;'>￥{{zero credit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "cashier"}}
                        <p style='color:balck;'>￥{{zero credit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "income"}}
                        <p style='color:balck;'>￥{{zero credit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "outcome"}}
                        <p style='color:#FF812A;'>-￥{{zero debit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "payment"}}
                        <p style='color:#FF812A;'>-￥{{zero debit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "withdraw"}}
                        <p style='color:#FF812A;'>-￥{{zero debit}}</p>
                    {{/ifCond}}

                    {{#ifCond type "rebate"}}
                        <p style='color:#FF812A;'>-￥{{zero debit}}</p>
                    {{/ifCond}}
                    
                    <p>{{balance}}</p>
                </div>
              </div>
            </div>
            {{/each}}
        </div>
        <div class="am-g" style="width:100%;line-height:30px;text-align:center;" id="pullUpLabel"></div>
    </script>
    <script type="text/x-handlebars-template" id="bill_wraper_2">
        <div id="bill_total_page" style="">
            <div class="weui_cells" style="font-size:15px;margin-top:10px;">
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/fenrunshouru.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>货款收入&nbsp;&nbsp;<span id="daikuanshouru"></span><span style="float:right;">+￥{{zero data.receipts}}</span> </p>
                        <div style="background-color:#FF6D06;border-radius:10px;height:8px;margin-top:5px;" id="dksr"></div>
                    </div>
                </div>
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/fenrunshouru.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>分润收入&nbsp;&nbsp;<span id="fenrunshouru"></span><span style="float:right;">+￥{{zero data.profit}}</span> </p>
                        <div style="background-color:#FF6D06;border-radius:10px;height:8px;margin-top:5px;" id="frsr"></div>
                    </div>
                </div>
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/jiaoyiyongjin.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>交易佣金&nbsp;&nbsp;<span id="jiaoyiyongjin"></span><span style="float:right;">-￥{{zero data.rebate}}</span> </p>
                        <div style="background-color:#FF6D06;border-radius:10px;height:8px;margin-top:5px;" id="jyyj"></div>
                    </div>
                </div>
            </div>

            <div class="weui_cells" style="font-size:15px;margin-top:10px;">
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/gouwuzhichu.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>购物支出&nbsp;&nbsp;<span id="gouwuzhichu"></span><span style="float:right;">-￥{{zero data.payment}}</span> </p>
                        <div style="background-color:#88CB40;border-radius:10px;height:8px;margin-top:5px;" id="gwzc"></div>
                    </div>
                </div>
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/xianxiashoukuan.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>线下收款&nbsp;&nbsp;<span id="xianxiashoukuan"></span><span style="float:right;">+￥{{zero data.cashier}}</span> </p>
                        <div style="background-color:#88CB40;border-radius:10px;height:8px;margin-top:5px;" id="xxsk"></div>
                    </div>
                </div>
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/qitazhichu.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>其他支出&nbsp;&nbsp;<span id="qitazhichu"></span><span style="float:right;">-￥{{zero data.outcome}}</span> </p>
                        <div style="background-color:#88CB40;border-radius:10px;wdith:;height:8px;margin-top:5px;" id="qtzc"></div>
                    </div>
                </div>
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/qitashouru.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>其他收入&nbsp;&nbsp;<span id="qitashouru"></span><span style="float:right;">+￥{{zero data.income}}</span> </p>
                        <div style="background-color:#88CB40;border-radius:10px;height:8px;margin-top:5px;" id="qtsr"></div>
                    </div>
                </div>
            </div>

            <div class="weui_cells" style="font-size:15px;margin-top:10px;">
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/chongzhi.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>充值&nbsp;&nbsp;<span id="chongzhi"></span><span style="float:right;">+￥{{zero data.recharge}}</span> </p>
                        <div style="background-color:gray;border-radius:10px;height:8px;margin-top:5px;" id="cz"></div>
                    </div>
                </div>
                <div class="weui_cell">
                    <div class="weui_cell_hd">
                        <img src="${base}/resources/wap/2.0/images/tixian.png" style="width:35px;">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="margin-left:10px;">
                        <p>提现&nbsp;&nbsp;<span id="tixian"></span><span style="float:right;">-￥{{zero data.withdraw}}</span> </p>
                        <div style="background-color:gray;border-radius:10px;height:8px;margin-top:5px;" id
                        ="tx"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="get_outcome_value" style="display:none;">{{total_outcome}}</div>
        <div id="get_income_value" style="display:none;">{{total_income}}</div>
        
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]
<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div style="position:fixed;width:100%;z-index:100;">
        <div class="weui_cell" style="padding:0px;color:white;font-size:17px;">
            <div class="weui_cell_primary" style="height:60px;background-color:#272A3B;border-right: 1px solid #fff;padding:5px;" id="getDate">
                <div style="width:70%;float:left;text-align:center;">
                    <p><span id="year"></span><span>年</span></p>
                    <p><span id="month"></span><span>月</span></p>  
                </div>
                <div style="float:left;margin-top:8%;">
                    <i class="iconfont" style="font-size:15px;">&#xe612;</i>
                </div>
            </div>

            <div class="weui_cell_primary" style="height:60px;background-color:#272A3B;border-right: 1px solid #fff;text-align:center;padding:5px;">
                <p>支出（元）</p>
                <p id="outcome"></p>
            </div>
            <div class="weui_cell_primary" style="height:60px;background-color:#272A3B;text-align:center;padding:5px;">
                <p>收入（元）</p>
                <p id="income"></p>
            </div>
        </div>
        <div style="">
            <div class="weui_cell" style="padding:0px;font-size:20px;line-height:39px;">
                <div class="weui_cell_primary" style="background-color:white;text-align:center;padding:5px;border-bottom:2px solid #AE0000;color:#AE0000;" id="bill_intail" onclick="get_bill_intail()"> 
                    <p>明细</p>
                </div>
                <div class="weui_cell_primary" style="background-color:white;text-align:center;padding:5px;" id="bill_total" onclick="get_bill_total()">
                    <p>统计</p>
                </div>    
            </div>
        </div>
        <div style="width:100%;height:1px;border-top:1px solid #E8EAE9;"></div>
        
        <input type="text" readonly="" name="input_date" placeholder="请输入日期" data-lcalendar="2000-01-01,2018-01-29" id="demo1" style="display:none;" oninput="getVal(this)"/>
    </div>
   
    <div class="page" style="background-color:#E9E9E9;padding-top:110px;">
        <div id="bill_intail_content"></div>
        <div id="bill_total_content"></div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    var global_var="";
    var load_complete=true;
    var is_load=true;
    $(function () {
        init();
        /**启动日历控件*/
        var calendar = new LCalendar();
        calendar.init({
            'trigger': '#demo1',//标签id
            'type': 'ym'//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择
        });
        $("#getDate").on("click",function(){
            $("#demo1").click();
        });

        /* 
        $(document).on('click',".date_roll_mask",function(event) {
            console.log("this is mask");
            event.stopPropagation();
        });*/

        $(document).on('click',".gearDate", function(event) {
            $(this).remove();
        });

        $(window).unbind("scroll");
        get_bill_intail();
        
    });
   
    /**获取日期的值*/
    function getVal(a){
        global_var=$(a).val();
        get_bill_intail();
    }

    /**获取订单详情*/
    function get_bill_intail(){
        $("#bill_total_content").hide();
        $("#bill_intail_content").show();
        var pageNum=1
        if(global_var==""){
            var date=new Date();
            var year=date.getFullYear();
            var month=date.getMonth()+1;
            $("#year").text(year);
            $("#month").text(month);
            var y=$("#year").text();
            var m=$("#month").text();
        }else{
            var y=global_var.substring(0,4);
            var m=global_var.substring(5,global_var.length);
            $("#year").text(y);
            $("#month").text(m);
        }
        var beginTime = y+"-"+m+"-"+1;
        var endTime;
        if(m==1||m==3||m==5||m==7||m==8||m==10||m==12){
            endTime = y+"-"+m+"-"+31;
        }else if(m==4||m==6||m==9||m==11){
            endTime = y+"-"+m+"-"+30;
        }else{
            if((y%4)==0){
                endTime = y+"-"+m+"-"+29;
            }else{
                endTime = y+"-"+m+"-"+28;
            }
        }
        $("#bill_intail").css("color","#FF6D06").css("border-bottom","2px solid #FF6D06");
        $("#bill_intail").siblings().css("color","").css("border-bottom","");

        compiler = Handlebars.compile($("#bill_wraper_1").html());
        ajaxGet({
            url:"${base}/app/member/deposit/list.jhtml",
            data:{
                begin_date:beginTime,
                end_date:endTime,
                pageSize:15,
                pageNumber:pageNum
            },
            success:function(data){
                $("#bill_intail_content").html(compiler(data));
                load_complete=true;
                $("#pullUpLabel").html("上拉刷新！");
                if(data.data.length==0){
                    $("#pullUpLabel").html("暂无数据！");
                }else if(data.data.length<15){
                    $("#pullUpLabel").html("亲到底了！");
                    is_load=false;
                    return ;
                }
               
            }
        });
        $(window).unbind("scroll"); 
        scroll(function(){
            if(is_load&&load_complete){
                load_complete=false;
                $("#pullUpLabel").html("");
                ajaxGet({
                    url:"${base}/app/member/deposit/list.jhtml",
                    data:{
                        begin_date:beginTime,
                        end_date:endTime,
                        pageSize:15,
                        pageNumber:pageNum+1
                    },
                    success:function(data){
                        pageNum=pageNum+1;
                        $("#bill_intail_content").append(compiler(data));
                        load_complete=true;
                        if(data.data.length==0){
                            $("#pullUpLabel").html("暂无数据！");
                        }else if(data.data.length<15){
                            $("#pullUpLabel").html("亲到底了！");
                            is_load=false;
                            return ;
                        }
                    }
                }); 
            } 
        });
    }
    /**订单统计*/
    function get_bill_total(){
        $(window).unbind("scroll");
        $("#bill_intail_content").hide();
        $("#bill_total_content").show();
        if(global_var==""){
            var date=new Date();
            var year=date.getFullYear();
            var month=date.getMonth()+1;
            $("#year").text(year);
            $("#month").text(month);
            var y=$("#year").text();
            var m=$("#month").text();
        }else{
            var y=global_var.substring(0,4);
            var m=global_var.substring(5,global_var.length);
            $("#year").text(y);
            $("#month").text(m);
        }
        var beginTime = y+"-"+m+"-"+1;
        var endTime;
        if(m==1||m==3||m==5||m==7||m==8||m==10||m==12){
            endTime = y+"-"+m+"-"+31;
        }else if(m==4||m==6||m==9||m==11){
            endTime = y+"-"+m+"-"+30;
        }else{
            if((y%4)==0){
                endTime = y+"-"+m+"-"+29;
            }else{
                endTime = y+"-"+m+"-"+28;
            }
        }
        $("#bill_total").css("color","#FF6D06").css("border-bottom","2px solid #FF6D06");
        $("#bill_total").siblings().css("color","").css("border-bottom","");
        ajaxGet({
            url:"${base}/app/member/deposit/sumer.jhtml",
            data:{
                begin_date:beginTime,
                end_date:endTime
            },
            success:function(data){
                compiler = Handlebars.compile($("#bill_wraper_2").html());
                $("#bill_total_content").html(compiler(data));
                $("#income").text(data.data.total_income);
                $("#outcome").text(data.data.total_outcome);
                var total=data.data.total_outcome+data.data.total_income;
                getDecimal(data.data.receipts,total,"daikuanshouru","dksr");
                getDecimal(data.data.profit,total,"fenrunshouru","frsr");
                getDecimal(data.data.rebate,total,"jiaoyiyongjin","jyyj");
                getDecimal(data.data.payment,total,"gouwuzhichu","gwzc");
                getDecimal(data.data.cashier,total,"xianxiashoukuan","xxsk");
                getDecimal(data.data.outcome,total,"qitazhichu","qtzc");
                getDecimal(data.data.income,total,"qitashouru","qtsr");
                getDecimal(data.data.recharge,total,"chongzhi","cz");
                getDecimal(data.data.withdraw,total,"tixian","tx");
            }
        });
    }
    /**处理小数点*/
    function getDecimal(typ,total,arg1,arg2){
        var a;
        if(total==0){
            a=0;
        }else{
            a=((typ/total).toFixed(4))*100;
        }    
        if(decimal(a,2)==0){
            $("#"+arg1).text("0.00%");
            $("#"+arg2).css("display","none");
        }else{
            $("#"+arg1).text(decimal(a,2)+"%");
            $("#"+arg2).css("width",decimal(a,2)+"%");
        }
        
        
        
    };
    function decimal(num,v){
        var vv = Math.pow(10,v);
        return Math.round(num*vv)/vv;
    };
    /**自定义switch-case标签*/
    Handlebars.registerHelper('iftype', function(v) {
       if(v=="profit"){
            return "分润"
       }else if(v=="recharge"){
            return "充值"
       }else if(v=="payment"){
            return "购物"
       }else if(v=="withdraw"){
            return "提现"
       }else if(v=="receipts"){
            return "货款"
       }else if(v=="rebate"){
            return "佣金"
       }else if(v=="income"){
            return "其他收入"
       }else if(v=="cashier"){
            return "收款"
       }else if(v=="outcome"){
            return "其他支出"
       }
    });
    /**自定义标签*/
    Handlebars.registerHelper('zero', function(v) {
       if(v==0){
            return "0.00";
       }else{
            var vs=v+"";
            var va=vs.substring(vs.length-2,vs.length-1);
            var vz=vs.indexOf(".");
            if(vz==-1){
                v=v+".00"
            }
            if(va=="."){
               v=v+"0";
            }
            return v;
       }
    });
    /**自定义if标签*/
    Handlebars.registerHelper('ifCond', function(v1, v2, options) {
        if(v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });
   /**自定义日期转换标签*/
    Handlebars.registerHelper('formatdate', function(time,fo) {
        var t = new Date(time); 
        var tf = function(i){return (i < 10 ? '0' : '') + i};
        return fo.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){ 
        switch(a){ 
            case 'yyyy': 
            return tf(t.getFullYear()); 
            break; 
            case 'MM': 
            return tf(t.getMonth() + 1); 
            break; 
            case 'dd': 
            return tf(t.getDate()); 
            break; 
            }; 
        }); 
    });
</script>
</body>
</html>
