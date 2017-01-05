<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="" content="" />
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心" />
    <meta name="description" content="${setting.siteName}-会员中心" />
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/base64.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2c/include/topnav.ftl"]
</div>

<!--主页内容区 -->
<div class="paper">
    <!-- 会员中心头部 -->
    [#include "/b2c/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
            [#include "/b2c/include/member_left.ftl"]
            <div class="content">
                    <!--我的订单-->
            <form id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="queryStatus" name="queryStatus" value="${queryStatus}">
                <div class="my-order">
                    <div class="my-order-header">
                        <div>
                            <span class="li [#if queryStatus=="none"]selected[/#if]">
                                <a href="list.jhtml?queryStatus=none">
                                    <span class="title">所有订单</span>
                                    <em></em>
                                    <span class="wire">|</span>
                                </a>
                            </span>
                            <span class="li [#if queryStatus=="unpaid"]selected[/#if]">
                                <a href="list.jhtml?queryStatus=unpaid">
                                    <span class="title">待付款</span>
                                    <em>${unpaid}</em>
                                    <span class="wire">|</span>
                                </a>
                            </span>
                            <span class="li [#if queryStatus=="unshipped"]selected[/#if]">
                                <a href="list.jhtml?queryStatus=unshipped">
                                    <span class="title">待发货</span>
                                    <em>${unshipped}</em>
                                    <span class="wire">|</span>
                                </a>
                            </span>
                            <span class="li [#if queryStatus=="shipped"]selected[/#if]">
                                <a href="list.jhtml?queryStatus=shipped">
                                    <span class="title">待收货</span>
                                    <em>${shipped}</em>
                                    <span class="wire">|</span>
                                </a>
                            </span>
                            <span class="li [#if queryStatus=="unreview"]selected[/#if]">
                                <a href="list.jhtml?queryStatus=unreview">
                                    <span class="title">待评价</span>
                                    <em>${unreview}</em>
                                    <span class="wire">|</span>
                                </a>
                            </span>
                        </div>
                    </div>
                    <div class="my-order-header">
                        <div class="my-order-th">
                            <span style="margin-left: 20px;">时间：</span>
                            <input type="text" class="text" id="beginDate" name="beginDate" value="${beginDate}" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',onpicked:function(){endDate.focus();}})" readonly style="width:10%;"> 至
                            <input type="text" class="text" id="endDate" name="endDate" value="${endDate}" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}'})" readonly style="width:10%;">
                            <input type="text" class="text"  name="keywords" value="${keywords}" style="width:15%;margin-left:50px;" placeholder="搜索.....">

                            <input id="searchButton" type="button" class="button" value="查询" style="margin-left: 20px;"/>
                            <input id="export_ss" type="button" class="button" value="导出" style="" />
                        </div>
                    </div>
                    <div class="my-order-main">
                        <div class="my-order-th">
                            <div>
                                <div class="th th-item">
                                    <div class="td-inner">宝贝</div>
                                </div>
                                <div class="th th-price">
                                    <div class="td-inner">单价（元）</div>
                                </div>
                                <div class="th th-amount">
                                    <div class="td-inner">数量</div>
                                </div>
                                <div class="th th-sum">
                                    <div class="td-inner">实付款（元）</div>
                                </div>
                                <div class="th th-status">
                                    <div class="td-inner">交易状态</div>
                                </div>
                                <div class="th th-op">
                                    <div class="td-inner">交易操作</div>
                                </div>
                            </div>
                        </div>
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                            <div class="my-order-page">
                                <div class="f-right">
                                    [#if hasPrevious]
                                        <span class="li up" onclick="$.pageSkip(${previousPageNumber})">
                                            <span>上一页</span>
                                            <i class="on"></i>
                                        </span>
                                    [#else]
                                        <span class="li up">
                                            <i class=""></i>
                                        </span>
                                    [/#if]
                                    [#if hasNext]
                                        <span class="li next" onclick="$.pageSkip(${nextPageNumber})">
                                            <span>下一页</span>
                                            <i class="on"></i>
                                        </span>
                                    [#else]
                                        <span class="li next">
                                            <i class=""></i>
                                        </span>
                                    [/#if]
                                </div>
                            </div>
                        [/@pagination]
                        <div class="my-order-list">
                            [#list page.content as trade]
                            <div class="order-list-item">
                                <div class="order-list-info">
                                    <div class="tr">
                                        [#--<div class="checkbox ">--]
                                            [#--<input type="checkbox"  disabled>--]
                                            [#--<label for=""></label>--]
                                        [#--</div>--]
                                        <div class="td">
                                            <span class="date">${trade.createDate}</span>
                                            <span>订单号：</span>
                                            <span class="">${trade.order.sn}</span>
                                            <span class="title">店铺：</span>
                                            <a class="name" href="${base}/b2c/tenant/index.jhtml?id=${trade.tenant.id}" target="_blank" title="${trade.tenant.shortName}">${trade.tenant.shortName}</a>
                                            <span class="level">
                                                [#if trade.tenant.score>0]
                                                    [#list 1..trade.tenant.score as t]❤[/#list]
                                                [/#if]
                                            </span>
                                        </div>
                                        [#--<span class="delete">--]
                                            [#--<a href="javascript:;" title="删除订单">shan</a>--]
                                        [#--</span>--]
                                    </div>
                                </div>
                                <div class="order-list-content">
                                    <div class="item-body clearfix">
                                        [#list trade.orderItems as orderItem]
                                            [#if orderItem_index==0&&orderItem?has_content]
                                                <ul class="item-content clearfix">
                                                    <li class="td-item" onclick="window.location.href='${base}/b2c/product/detail/${(orderItem.product.id)!}.jhtml'">
                                                        <div class="td-inner">
                                                            <div class="item-pic">
                                                                <a href="javascript:;" target="_blank">
                                                                    <img src="${orderItem.thumbnail}" width="80px" height="80px">
                                                                </a>
                                                            </div>
                                                            <div class="item-info">
                                                                <div class="item-basic-info">
                                                                    <a href="javascript:;" target="_blank" title="${orderItem.fullName}" class="item-title">
                                                                        ${orderItem.fullName}
                                                                    </a>
                                                                </div>
                                                                <div class="item-other-info">
                                                                    <div class="promo-logos"></div>
                                                                    <div class="item-props clearfix">
                                                                        [#if orderItem.product??]
                                                                        [#list orderItem.product.specificationValues as specificationValue]
                                                                            <span>${specificationValue.specification.name}：${specificationValue.name}</span>
                                                                            &nbsp;&nbsp;
                                                                        [/#list]
                                                                        [/#if]
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li class="td-price">
                                                        <div class="td-inner">
                                                            <div class="price-content">
                                                                <div class="price-line">
                                                                    [#if orderItem.product??]
                                                                    <em>￥${orderItem.product.marketPrice?string("0.00")}</em>
                                                                    [/#if]
                                                                    <span>￥${orderItem.price?string("0.00")}</span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li class="td-amount border-right">
                                                        <div class="td-inner">
                                                            <span>${orderItem.quantity}</span>
                                                        </div>
                                                    </li>
                                                    <li class="td-sum border-right">
                                                        <div class="td-inner">
                                                            <em class="color">${trade.amount?string("0.00")}</em>
                                                            <div>（含运费：<span>${trade.freight?string("0.00")}</span>）</div>
                                                        </div>
                                                    </li>
                                                    <li class="td-status border-right">
                                                        <div class="td-inner">
                                                            <a title="" href="javascript:;">
                                                                ${(trade.finalOrderStatus[0].desc)!}
                                                                [#list trade.finalOrderStatus as finalOrderStatu]
                                                                [#if finalOrderStatu_index==0]
                                                                    [#if finalOrderStatu.status!='cancelled']
                                                                        [#if trade.returnsStatus=="returned"]
                                                                        <br/>
                                                                        <span style="color:red;">（已退货）</span>
                                                                        [#elseif trade.returnsStatus=="returning"]
                                                                        <br/>
                                                                        <span style="color:red;">（退货中）</span>
                                                                        [#elseif trade.returnsStatus=="refused"]
                                                                        <br/>
                                                                        <span style="color:red;">（拒绝退货）</span>
                                                                        [/#if]
                                                                    [/#if]
                                                                [/#if]
                                                            [/#list]
                                                            </a>
                                                            <a href="${base}/b2c/member/order/order_detail.jhtml?id=${trade.id}">订单详情</a>
                                                        </div>
                                                    </li>
                                                    <li class="td-op">
                                                        <div class="td-inner">
                                                            [#--<a title="确认收货" href="javascript:;">确认收货</a>--]
                                                            [#list trade.finalOrderStatus as finalOrderStatu]
                                                                [#if finalOrderStatu_index==0]
                                                                    [#if finalOrderStatu.status!='cancelled']
                                                                        [#if finalOrderStatu.status=='waitPay']
                                                                            <a href="javascript:waitPayCancel(${trade.order.sn});" style="background-color: white;color:black;border: 1px solid;">取消订单</a>
                                                                            <a href="javascript:waitShippingPay(${trade.order.sn});" style="margin-top: 5px;">立即支付</a>
                                                                        [#elseif finalOrderStatu.status=='unconfirmed']
                                                                            <a href="javascript:waitPayCancel(${trade.order.sn});" style="background-color: white;color:black;border: 1px solid;">取消订单</a>
                                                                        [#elseif finalOrderStatu.status=='waitShipping']
                                                                            [#if trade.returnsStatus=="refused"||trade.returnsStatus=="null"]
                                                                            <a href="javascript:waitReturn(${trade.order.sn},${trade.id},'${trade.shippingStatus}','${trade.orderStatus}');" style="background-color: white;color:black;border: 1px solid;">退货申请</a>
                                                                            [/#if]
                                                                            <a href="javascript:waitShippingRemind(${trade.id});" style="margin-top: 5px;">提醒商家发货</a>
                                                                        [#elseif finalOrderStatu.status=='sign'||finalOrderStatu.status=='partialShipment']
                                                                            [#if trade.returnsStatus=="refused"||trade.returnsStatus=="null"]
                                                                            <a href="javascript:waitReturn(${trade.order.sn},${trade.id},'${trade.shippingStatus}','${trade.orderStatus}');" style="background-color: white;color:black;border: 1px solid;">退货申请</a>
                                                                            [/#if]
                                                                            <a href="javascript:waitShippingConfirm(${trade.order.sn},${trade.id});" style="margin-top: 5px;">立即签收</a>
                                                                        [#elseif finalOrderStatu.status=='toReview']
                                                                            [#if trade.returnsStatus=="refused"||trade.returnsStatus=="null"]
                                                                                <a href="javascript:waitReturn(${trade.order.sn},${trade.id},'${trade.shippingStatus}','${trade.orderStatus}');" style="background-color: white;color:black;border: 1px solid;">退货申请</a>
                                                                            [/#if]
                                                                            <a href="javascript:evaluate(${trade.order.sn},${trade.id});" style="margin-top: 5px;">立即评价</a>
                                                                        [#elseif finalOrderStatu.status=='completed']
                                                                            [#if trade.returnsStatus=="refused"||trade.returnsStatus=="null"]
                                                                                <a href="javascript:waitReturn(${trade.order.sn},${trade.id},'${trade.shippingStatus}','${trade.orderStatus}');" style="background-color: white;color:black;border: 1px solid;">退货申请</a>
                                                                            [/#if]
                                                                        [#elseif finalOrderStatu.status=='waitReturn']
                                                                            <a href="javascript:waitShippingRemind(${trade.id});">提醒商家退货</a>
                                                                        [/#if]
                                                                    [/#if]
                                                                [/#if]
                                                            [/#list]
                                                            [#--<span>还剩：<em>9天13时5分${trade.confirmDueDate}</em></span>--]
                                                        </div>

                                                    </li>
                                                </ul>
                                            [#else]
                                                <ul class="item-content clearfix">
                                                        <li class="td-item border-top">
                                                            <div class="td-inner">
                                                                <div class="item-pic">
                                                                    <a href="javascript:;" target="_blank">
                                                                        <img src="${orderItem.thumbnail}" width="80px"
                                                                             height="80px">
                                                                    </a>
                                                                </div>
                                                                <div class="item-info">
                                                                    <div class="item-basic-info">
                                                                        <a href="javascript:;" target="_blank"
                                                                           title="${orderItem.fullName}"
                                                                           class="item-title">
                                                                        ${orderItem.fullName}
                                                                        </a>
                                                                    </div>
                                                                    <div class="item-other-info">
                                                                        <div class="promo-logos"></div>
                                                                        <div class="item-props clearfix">
                                                                        [#if orderItem.product??&&orderItem.product?has_content]
                                                                                [#if orderItem.product.specificationValues??&&orderItem.product.specificationValues?has_content]
                                                                            [#list orderItem.product.specificationValues as specificationValue]
                                                                                <span>${specificationValue.specification.name}
                                                                                    ：${specificationValue.name}</span>
                                                                                &nbsp;&nbsp;
                                                                            [/#list]
                                                                            [/#if]
                                                                        [/#if]
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li class="td-price border-top">
                                                            <div class="td-inner">
                                                                <div class="price-content">
                                                                    <div class="price-line">
                                                                        <em>￥${(orderItem.product.marketPrice?string("0.00"))!}</em>
                                                                        <span>￥${(orderItem.price?string("0.00"))!}</span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li class="td-amount border-top border-right">
                                                            <div class="td-inner">
                                                                <span>${orderItem.quantity}</span>
                                                            </div>
                                                        </li>
                                                        <li class="td-sum border-right">

                                                        </li>
                                                        <li class="td-status border-right">

                                                        </li>
                                                        <li class="td-op">

                                                        </li>
                                                    </ul>
                                            [/#if]

                                        [/#list]
                                        <!--
                                        <ul class="item-content item-insurance clearfix">
                                            <li class="td-item border-top">
                                                <div class="td-inner">
                                                    <a href="javascript:;">保险服务</a>
                                                </div>
                                            </li>
                                            <li class="td-price border-top">
                                                <div class="td-inner">
                                                    <div class="price-content">
                                                        <div class="price-line">
                                                            <em>￥698.00</em>
                                                            <span>￥598.00</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                            <li class="td-amount border-top border-right">
                                                <div class="td-inner">
                                                    <span>2</span>
                                                </div>
                                            </li>
                                            <li class="td-sum border-right"></li>
                                            <li class="td-status border-right"></li>
                                            <li class="td-op"></li>
                                        </ul>
                                        -->
                                    </div>

                                </div>
                            </div>
                            [/#list]
                        </div>
                    </div>
                    [#include "/b2c/include/pagination.ftl"]
                </div>
                <div id="trade_wrap"></div>
                <!-- <table style="display:none;" class="table2excel">
                    <thead>
                        <tr>
                            <th>订单号</th>
                            <th>创建时间</th>
                            <th>店铺名称</th>
                            <th>金额</th>
                            <th>商品数量</th>
                            <th>状态</th>
                        </tr>
                    </thead>
                    <tbody>
                        [#list page.content as trade]
                        <tr>
                            <td>${trade.order.sn}</td>
                            <td>${trade.createDate}</td>
                            <td>${trade.tenant.name}</td>
                            <td>${trade.amount}</td>
                            <td>${trade.quantity}</td>
                            <td>
                                ${(trade.finalOrderStatus[0].desc)!}
                            </td>
                        </tr>
                        [/#list]
                    </tbody>
                </table> -->
            </form>
            </div>
        </div>
    </div>
    <!--可能感兴趣 -->
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%" height="auto">
    </iframe>
    <!--标语 -->
    [#include "/b2c/include/slogen.ftl"]
</div>

<script type="text/javascript">
    $(function(){
        function getBill() {
            $("#dateType").val("1");
            $("#listForm").submit();
        }
        function billDetails(type){
            $("#dateType").val("2");
            $("#beginDate").val("");
            $("#endDate").val("");
            $("[name='type']").val(type);
            $("#listForm").submit();
        }
        $("#searchButton").click(function(){
            $("#dateType").val("2");
            $("#listForm").submit();
        });
        //导出数据到excel
        $("#export_ss").click(function(){
            $.message("success","正在帮您导出，请稍后")
            $.ajax({
                url:"${base}/b2c/member/order/all_list.jhtml",
                type:"get",
                data:{
                    beginDate:$("#beginDate").val(),
                    endDate:$("#endDate").val(),
                    keywords:$("[name='keywords']").val(),
                    queryStatus:$("#queryStatus").val()
                },
                async:false,
                dataType:"json",
                success:function(data){
                    var html='<table style="display:none;" class="table2excel">'+
                    '<thead>'+
                        '<tr>'+
                            '<th>订单号</th>'+
                            '<th>创建时间</th>'+
                            '<th>店铺名称</th>'+
                            '<th>金额</th>'+
                            '<th>商品数量</th>'+
                            '<th>状态</th>'+
                        '</tr>'+
                    '</thead>'+
                    '<tbody>;'
                    $.each(data,function(i,obj){
                        html+='<tr>'+
                                    '<td>'+obj.sn+'</td>'+
                                    '<td>'+obj.createData+'</td>'+
                                    '<td>'+obj.name+'</td>'+
                                    '<td>'+obj.amount+'</td>'+
                                    '<td>'+obj.quantity+'</td>'+
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
                name: "订单列表",
                filename: "订单列表",
                fileext: ".xls",
                exclude_img: true,
                exclude_links: false,
                exclude_inputs: true
            });
        });
    });

    //取消订单
    function waitPayCancel(sn) {
        checkLock(sn,function(){
            if(window.confirm("您确认取消该订单吗?")){
                $.ajax({
                    url: '${base}/app/member/order/cancel.jhtml',
                    data: {sn: sn},
                    type:'post',
                    dataType:'json',
                    success: function (data) {
                        if (data.message.type == "success") {
                            $.message("success","取消成功");
                            window.setTimeout(function(){
                                window.location.reload(true);
                            },600);
                        } else {
                            $.message(data.message);
                        }
                    }
                });
            }
        })
    }

    //退货申请
    function waitReturn(sn,id,shipStatus,orderStatus) {
        checkLock(sn,function(){
            if(window.confirm("您确定要申请退货吗?")) {
                if(shipStatus=="accept"&&orderStatus=="completed"){
                    location.href="${base}/b2c/member/order/return/return_management.jhtml?id="+id;
                }else{
                    $.ajax({
                        url: '${base}/b2c/member/order/return.jhtml',
                        data: {id: id},
                        type:'post',
                        dataType:'json',
                        success: function (data) {
                            $.message(data.message);
                            if (data.message.type == "success") {
                                window.setTimeout(function(){
                                    location.reload(true);
                                },600);
                            }
                        }
                    });
                }
            }
        });
    }

    //立即签收
    function waitShippingConfirm(sn,id) {
        checkLock(sn,function(){
            if(window.confirm("您确定要签收这个订单吗")){
                $.ajax({
                    url: '${base}/app/member/order/confirm.jhtml',
                    data: {id: id},
                    type:'post',
                    dataType:'json',
                    success: function (data) {
                        if (data.message.type == "success") {
                            $.message("success","签收成功");
                            window.setTimeout(function(){
                                location.href="${base}/b2c/member/order/evaluate/list.jhtml"
                            },600)
                        } else {
                            $.message(data.message);
                        }

                    }
                });
            }
        });
    }

    //立即支付
    function waitShippingPay(sn) {
        checkLock(sn,function(){
            $.ajax({
                url:"${base}/b2c/payment/create.jhtml",
                type:"post",
                data:{
                    paymentPluginId:"balancePayPlugin",
                    sn:sn
                },
                dataType:"JSON",
                success:function(dataBlock){
                    if(dataBlock.message.type = "success"){
                        location.href="${base}/b2c/payment/payment_index.jhtml?sn="+dataBlock.data;
                    }else{
                        $.message("error",dataBlock.message.content);
                        location.href="${base}/b2c/member/order/list.jhtml"
                    }

                }
            });
        });
    }

    //提醒商家
    function waitShippingRemind(id) {
        $.ajax({
            url: '${base}/app/member/order/remind.jhtml',
            data: {id: id},
            type:'post',
            dataType:'json',
            success: function (data) {
                if (data.message.type = "success") {
                    $.message("success","已提醒");
                } else {
                    $.message(data.message);
                }
            }
        });
    }

    //立即评价
    function evaluate(sn,id){
        checkLock(sn,function(){
            location.href="${base}/b2c/member/order/evaluate/list.jhtml";
        })
    }

    //检查订单是否锁定
    function checkLock(sn,fn){
        $.ajax({
            url:"${base}/app/member/order/lock.jhtml",
            data:{sn:sn},
            type:'post',
            dataType:'json',
            success:function(data){
                if(data.message.type=="success"){
                    fn();
                }else{
                    $.message(data.message);
                }
            }
        });
    }
</script>
<!--底部 -->
[#include "/b2c/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
