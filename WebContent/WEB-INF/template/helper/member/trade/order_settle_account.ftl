<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <style type="text/css">
        #bdshare_weixin_qrcode_dialog {
            box-sizing: content-box;
        }
        #listTable th,#listTable td{
            /*padding-left: 5px;*/
            text-align: center;
        }
        div.popupMenu a{
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
            var $selectAll = $("#selectAll");
            var $ids = $("#listTable input[name='ids']");
            [@flash_message /]
            if($("#settle_status").text()=="已结算"){
                $("#status_val").val("true");
            }else if($("#settle_status").text()=="未结算"){
                $("#status_val").val("false");
            }else{
                $("#status_val").val("");
            }

            // 全选
            $selectAll.click( function() {
                var $this = $(this);
                var $enabledIds = $("#listTable input[name='ids']:enabled");
                if ($this.prop("checked")) {
                    $enabledIds.prop("checked", true);
                    if ($enabledIds.filter(":checked").size() > 0) {
                        $("#settleButton").removeClass("disabled");
                        // $deleteButtonAll.removeClass("disabled");
                        // $contentRow.addClass("selected");
                    } else {
                        $("#settleButton").addClass("disabled");
                        // $deleteButtonAll.addClass("disabled");
                    }
                } else {
                    $enabledIds.prop("checked", false);
                    $("#settleButton").addClass("disabled");
                    // $deleteButtonAll.addClass("disabled");
                    // $contentRow.removeClass("selected");
                }
            });
            
            // 选择
            $ids.click( function() {
                var $this = $(this);
                if ($this.prop("checked")) {
                    $this.closest("tr").addClass("selected");
                    $("#settleButton").removeClass("disabled");
                    // $deleteButtonAll.removeClass("disabled");
                } else {
                    $this.closest("tr").removeClass("selected");
                    if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
                        $("#settleButton").removeClass("disabled");
                        // $deleteButtonAll.removeClass("disabled");
                    } else {
                        $("#settleButton").addClass("disabled");
                        // $deleteButtonAll.addClass("disabled");
                    }
                }
            });
            //=======================导出==============================
            $("#export_ss").click(function () {
                $.message("success", "正在帮您导出，请稍后")
                $.ajax({
                    url: "${base}/helper/member/trade/order_settle_account_export.jhtml",
                    type: "get",
                    data: {
                        startDate: $("#startDate").val(),
                        endDate: $("#endDate").val(),
                        status:$("#status_val").val(),
                        keywords: $("#keyword").val()
                    },
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        var html = '<table style="display:none;" class="table2excel">' +
                                '<thead>' +
                                    '<tr>' +
                                        '<th>创建日期</th>' +
                                        '<th>订单号</th>' +
                                        '<th>支付方式</th>' +
                                        '<th>支付状态</th>' +
                                        '<th>发货状态</th>' +
                                        '<th>订单状态</th>' +
                                        '<th>金额</th>' +
                                        '<th>结算金额</th>' +
                                        '<th>结算状态</th>' +
                                        '<th>结算时间</th>' +
                                    '</tr>' +
                                '</thead>' +
                                '<tbody>';
                        $.each(data, function (i, obj) {
                            html += '<tr>' +
                                        '<td>' + obj.time + '</td>' +
                                        '<td>' + obj.sn + '</td>' +
                                        '<td>' + obj.paymentMethod + '</td>' +
                                        '<td>' + obj.paymentStatus + '</td>' +
                                        '<td>' + obj.shippingStatus + '</td>' +
                                        '<td>' + obj.orderStatus + '</td>' +
                                        '<td>' + obj.amount + '</td>' +
                                        '<td>' + obj.cost + '</td>' +
                                        '<td>' + obj.suppliered + '</td>' +
                                        '<td>' + obj.supplierDate +'</td>' +
                                    '</tr>';
                            
                        });
                        html += '</tbody>' +
                                '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "订单结算",
                    filename: "订单结算",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
            });
            
        });
        function get_date_val(){
            $("#listForm").submit();   
        } 
        function get_status(obj){
            $("#seller_pop").hide();
            if($(obj).attr("id")=="yes"){
                $("#settle_status").text("已结算");
                $("#status_val").val("true");
            }else if($(obj).attr("id")=="no"){
                $("#settle_status").text("未结算");
                $("#status_val").val("false");
            }
            get_date_val();
        }
        function submit_settle_account(obj){
            var amount_total=0;
            var settle_total=0;
            var select_num=0;
            $.each($("#listTable input[name='ids']"),function(i,item){
                if($(this).attr("checked")=="checked"){
                    $(this).parent().parent().find("[amount='amount']").each(function(){
                        amount_total= parseFloat(amount_total)+parseFloat($(this).attr("oamo"));
                        select_num+=1;
                    });
                    $(this).parent().parent().find("[settle='settle']").each(function(){
                        settle_total=parseFloat(settle_total)+parseFloat($(this).attr("samo"));
                    });
                    // $(this).parent().parent().find("[mark='mark']").each(function(){
                    //     if($(this).attr("isSet")=="true"){
                    //         return;
                    //     }
                    // });
                }
            });
            $.dialog({
                title: "结算统计",
                [@compress single_line = true]
                content: '<table class= "input" style = "margin-bottom: 30px;"><tr><th>已选中订单数量:<\/th><td>'+select_num+'<\/td><\/tr><tr><th>选中订单总额:<\/th><td>'+amount_total.toFixed(2)+'<\/td><\/tr><tr><th>选中结算总额:<\/th><td>'+settle_total.toFixed(2)+'<\/td><\/tr><\/table>',
                [/@compress]
                width: 400,
                modal: true,
                ok: "${message("admin.dialog.ok")}",
                cancel: "${message("admin.dialog.cancel")}",
                onOk: function () {
                    if ($(obj).hasClass("disabled")) {
                        return;
                    }
                    var ids=[];
                    $.each($("#listTable input[name='ids']"),function(i,item){
                        if($(this).attr("checked")=="checked"){
                            ids.push($(this).val());
                        }
                    });
                    $.ajax({
                        url:"clear_order_settle.jhtml",
                        type:"post",
                        traditional: true,
                        data:{ids:ids},
                        dataType:"json",
                        success:function(message){
                            if(message.type=="success"){
                                location.reload();
                            }else{
                                $.message(message);
                            }
                        }
                    });
                }
            });
           
            
        }     
    </script>

</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/discount.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">往来结算</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">管理我的往来结算。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/trade/order_settle_account.jhtml">订单结算</a></li>
                    <li><a hideFocus="" href="${base}/helper/member/trade/return_settle_account.jhtml">退货结算</a></li>
                    <li><a hideFocus="" href="${base}/helper/member/trade/withdraw_cash_settle_account.jhtml">提现结算</a></li>
                </ul>
            </div>
            <form id="listForm" action="order_settle_account.jhtml" method="get">
                <input type="hidden" name="status" value="${status}" id="status_val">
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/trade/order_settle_account.jhtml" type="payment"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="settleButton" class="iconButton disabled" onclick="submit_settle_account(this)">
                                批量缴款
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="${base}/helper/member/trade/order_settle_account.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
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
                                </ul>
                            </div>
                        </div>
                        <div class="menuWrap" id="settleAccountSelect">
                            <a href="javascript:;" class="button">
                                <span id="settle_status">[#if status=="true"]已结算[#elseif status=="false"]未结算[#else]结算状态[/#if]</span><span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu" id="seller_pop" style="display:none;width:80px;margin-left:238px;margin-top:25px;">
                                <ul>
                                    <li><a href="javascript:;" id="yes" onclick="get_status(this)">已结算</a></li>
                                    <li><a href="javascript:;" id="no" onclick="get_status(this)">未结算</a></li>
                                </ul>
                            </div>
                        </div>
                        <a href="javascript:;" id="export_ss" class="button">导出</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="text" id="startDate" name="startDate" value="${begin_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间" style="width:150px;"/>
                        <input type="text" id="endDate" name="endDate" value="${end_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});"
                       placeholder="结束时间" style="width:150px;" />
                        <input type="text" id="keyword" name="keywords" value="${keywords}" maxlength="200"
                                       placeholder="搜索内容....." style="width:150px;height:20px;" />
                        <input type="button" value="查询" onclick="get_date_val()" id="submit_button">
                        <!-- <div class="menuWrap" id="settleAccountSelect">
                            <a href="javascript:;"  class="button" >
                                往来客户<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu" id="seller_pop" style="display:none;margin-left:147px;margin-top:25px;">
                                <ul>
                                    
                                    <li>
                                        <a href="settle_account.jhtml?sellerId=" val=""></a>
                                    </li>
                                    
                                </ul>
                            </div>
                        </div> -->
                        <script type="text/javascript">
                            $(function(){
                                $("#settleAccountSelect").mouseover(function(){
                                    $("#seller_pop").show();
                                });

                                $("#settleAccountSelect").mouseout(function(){
                                    $("#seller_pop").hide();
                                });
                                // $("#select").onmouseout(function(){
                                //     $("#seller_pop").hide()
                                // });
                                
                            });
                        </script>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th><input type="checkbox" id="selectAll" [#if status=="true"]disabled="true"[/#if]></th>
                            <th>创建时间</th>
                            <th>订单号</th>
                            <th>订单金额</th>
                            <th>支付方式</th>
                            <th>支付状态</th>
                            <th>发货状态</th>
                            <th>结算金额</th>
                            <th>结算状态</th>
                            <th>结算时间</th>
                            <th>操作</th>
                        </tr>
                        [#if page??&&page?has_content]
                        [#list page.content as trades]
                        <tr>
                            <td><input type="checkbox" name="ids" value="${trades.id}" [#if (trades.shippingStatus=="shipped"||trades.shippingStatus=="accept")&&trades.paymentStatus=="unpaid"][#else]disabled="true"[/#if]></td>
                        
                            <td>
                                <span>${trades.createDate}</span>
                            </td>
                            <td>
                                <span>[#if trades.order?has_content]${trades.order.sn}[/#if]</span>
                            </td>
                            <td>
                                <span amount="amount" oamo="${(trades.amount)!}">${(trades.amount)!}</span>
                            </td>
                            <td>
                                <span>
                                    [#if trades.order.paymentMethod.method=="online"]
                                    线上付款
                                    [#elseif trades.order.paymentMethod.method=="offline"]
                                    线下付款
                                    [#elseif trades.order.paymentMethod.method=="balance"]
                                    余额支付
                                    [/#if]
                                </span>
                            </td>
                            <td>
                                <span>
                                    [#if trades.paymentStatus=="unpaid"]
                                    未付款
                                    [#elseif trades.paymentStatus=="partialPayment"]
                                    部分付款
                                    [#elseif trades.paymentStatus=="paid"]
                                    已付款
                                    [#elseif trades.paymentStatus=="partialRefunds"]
                                    部分退款
                                    [#elseif trades.paymentStatus=="refunded"]
                                    已退款
                                    [#elseif trades.paymentStatus=="refundApply"]
                                    退款中
                                    [/#if]
                                </span>
                            </td>
                            <td>
                                <span>
                                    [#if trades.shippingStatus=="unshipped"]
                                    未发货
                                    [#elseif trades.shippingStatus=="partialShipment"]
                                    部分发货
                                    [#elseif trades.shippingStatus=="shipped"]
                                    已发货
                                    [#elseif trades.shippingStatus=="partialReturns"]
                                    部分退货
                                    [#elseif trades.shippingStatus=="returned"]
                                    已退货
                                    [#elseif trades.shippingStatus=="accept"]
                                    已签收
                                    [#elseif trades.shippingStatus=="shippedApply"]
                                    退货中
                                    [/#if]
                                </span>
                            </td>
                            <td>
                                <span settle="settle" samo="${(trades.cost)!}">${trades.cost} </span>
                            </td>
                            <td>
                                <span>
                                [#if trades.suppliered??&&trades.suppliered==true]
                                已结算
                                [#else]
                                未结算
                                [/#if]
                                </span>
                            </td>
                            <td>
                                <span>${trades.supplierDate}</span>
                            </td>
                            <td >
                            [@helperRole url="helper/member/trade/order_settle_account.jhtml" type="payment"]
                                [#if helperRole.retOper!="0"]
                                    [#if (trades.shippingStatus=="shipped"||trades.shippingStatus=="accept")&&trades.paymentStatus=="unpaid"]
                                    <span mark="mark" isSet="false">
                                        <a href="javascript:;" onclick="clear_settle_account(${trades.id},${trades.amount},${trades.cost})">缴款</a>
                                    </span>
                                    [/#if]
                                [/#if]
                            [/@helperRole]
                            </td>
                        </tr>
                        [/#list]
                        [/#if]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("box.member.noResult")}</p>
                [/#if]
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/helper/include/pagination.ftl"]
                [/@pagination]
                </div>
            </form>

            <!--share begin -->
            <script>
                var jiathis_config;
                function share(id, thumbnail, description) {
                    jiathis_config = {
                        url: "${url}".toString().replace("ID", id),
                        pic: thumbnail,
                        title: "${title}",
                        summary: description
                    }
                    $(".jiathis_button_weixin").click();
                    $("#jiathis_weixin_tip a").remove();
                }
                function clear_settle_account(id,amount,cost){
                    var ids=[];
                    ids.push(id);
                    var amount_total=0;
                    var settle_total=0;
                    $.dialog({
                        title: "确认缴款吗？",
                        [@compress single_line = true]
                        content: '<table class= "input" style = "margin-bottom: 30px;"><tr><th>订单总额:<\/th><td>'+amount+'<\/td><\/tr><tr><th>结算总额:<\/th><td>'+cost+'<\/td><\/tr><\/table>',
                        [/@compress]
                        width: 400,
                        modal: true,
                        ok: "${message("admin.dialog.ok")}",
                        cancel: "${message("admin.dialog.cancel")}",
                        onOk: function () {
                            $.ajax({
                                url:"${base}/helper/member/trade/clear_order_settle.jhtml",
                                type:"post",
                                data:{ids:ids},
                                dataType:"json",
                                traditional: true,
                                success:function(data){
                                    $.message(data.type,data.content);
                                    if(data.type=="success"){
                                        location.href="${base}/helper/member/trade/order_settle_account.jhtml?status='已结算'";
                                    }
                                }
                            });
                        }
                    });
                    
                }
                // function getSeller(obj){
                //     if($(obj).next().css("display")=="none"){
                //         $(obj).next().css("display","block").css("margin-left","147px").css("margin-top","25px");
                //     }else{
                //         $(obj).next().css("display","none");
                //     }
                // }
            </script>
            <div id="ckepop" style="display: none;">
                <span class="jiathis_txt">分享到：</span>
                <a class="jiathis_button_weixin">微信</a>
                <a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jiathis_separator jtico jtico_jiathis"
                   target="_blank">更多</a>
                <a class="jiathis_counter_style"></a></div>
            <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1" charset="utf-8"></script>
        </div>
        <br/>
        <!--share end -->

    </div>
</div>
</div>
[#include "/helper/include/footer.ftl" /]
<div id="trade_wrap"></div>
</body>
</html>
