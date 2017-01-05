<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>订单详情</title>
    <style type="text/css">
        .spanRadius {
            border-radius: 50%;
            background-color: #ff0000;
            color: #fff;
            padding: 1.2px 3px;
            position: absolute;
            top: 15px;
            z-index: 80;
            font-size: 8px;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page">
        <div class="weui_cells weui_cells_access" style="margin-top:0px;">
            <ul data-am-widget="gallery" class="am-gallery am-gallery-default am-avg-5 am-text-center">
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=all">
                        <div class="am-gallery-item am-gallery-item-default [#if type=="all"]visit[/#if]">全部</div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unpaid">
                        <div class="am-gallery-item am-gallery-item-default [#if type=="unpaid"]visit[/#if]">
                            <p>待支付</p>
                        [#if unpaid??&&unpaid!=0]
                            <span class="spanRadius">${unpaid}</span>
                        [/#if]
                        </div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unshipped">
                        <div class="am-gallery-item am-gallery-item-default [#if type=="unshipped"]visit[/#if]">待发货
                        </div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unreciver">
                        <div class="am-gallery-item am-gallery-item-default [#if type=="unreciver"]visit[/#if]">待收货
                        [#if shipped??&&shipped!=0]
                            <span class="spanRadius">${shipped}</span>
                        [/#if]
                        </div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unreview">
                        <div class="am-gallery-item am-gallery-item-default [#if type=="unreview"]visit[/#if]">待评价
                        [#if unreview??&&unreview!=0]
                            <span class="spanRadius">${unreview}</span>
                        [/#if]

                        </div>
                    </a>
                </li>
            </ul>
        </div>
        <div id="orderList">
        [#list page as trade]
            <div class="weui_cells weui_cells_access">
                <div class="weui_cells weui_cells_access" style="margin-top:0px;">
                    <a class="weui_cell" href="${base}/wap/tenant/index/${trade.tenant.id}.jhtml">
                        <div class="weui_cell_hd">
                            <i class="iconfont font-large-2">&#xe663;</i>
                        </div>
                        <div class="weui_cell_bd weui_cell_primary" style="font-size:23px;">
                        ${trade.tenant.shortName}
                        </div>
                        <div class="weui_cell_ft" style="color:red;">${trade.finalOrderStatus.desc}</div>
                    </a>
                    [#list trade.orderItem as order]
                        <a class="weui_cell" href="${base}/wap/member/order/order_info.jhtml?id=${trade.id}">
                            <div class="weui_cell_hd">
                                <!-- 商品缩略图 -->
                                <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png" data-original="${order.thumbnail}"
                                     class="lazy" alt="icon" style="width:80px;margin-right:5px;display:block">
                            </div>
                            <div class="weui_cell_bd weui_cell_primary">
                                <p style="font-size:15px;">${order.fullName}</p><!-- 商品名称 -->
                                <p style="font-size:10px;color:#E6C99C">订单号：${trade.sn}</p><!-- 优惠信息展示 -->
                                <p style="font-size:10px;color:#A0A0A0;"></p><!-- 商品规格展示 -->
                            </div>
                            <div style="text-align:right;">
                                <p style="font-size:25px;color:red;">${order.price}</p><!-- 商品现价 -->
                                <p style="font-size:15px;">
                                    <del>
                                        <del>
                                </p><!-- 商品原价 -->
                                <p>x${order.quantity}</p><!-- 商品数量 -->
                            </div>
                        </a>
                    [/#list]
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary"></div>
                        <div style="text-align:right;">
                            <p>共<span style="color:red;">${trade.quantity}</span>件商品&nbsp;&nbsp;&nbsp;&nbsp;合计：<span
                                    style="color:red;">￥${trade.amount}</span></p>
                        </div>
                    </a>
                </div>

                [#if trade.finalOrderStatus.status!='cancelled']
                    <div class="weui_cells_title"
                         style="line-height:2.0;margin-top:0px;text-align:right;padding-right:20px;">
                        [#if trade.finalOrderStatus.status=='waitPay']
                            <a href="javascript:waitPayCancel(${trade.sn});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default">取消订单</a>
                            <a href="javascript:waitShippingPay(${trade.sn});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default"
                               style="color:white;background-color:red;">立即支付</a>
                        [#elseif trade.finalOrderStatus.status=='waitShipping']
                            <a href="javascript:waitReturn(${trade.id});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default">退货申请</a>
                            <a href="javascript:waitShippingRemind(${trade.id});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default"
                               style="color:white;background-color:red;margin-top:15px;">提醒商家发货</a>
                        [#elseif trade.finalOrderStatus.status=='sign'||trade.finalOrderStatus.status=='partialShipment']
                            <a href="javascript:waitReturn(${trade.id});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default">退货申请</a>
                            <a href="javascript:waitShippingConfirm(${trade.id});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default"
                               style="color:white;background-color:red;margin-top:15px;">立即签收</a>
                        [#elseif trade.finalOrderStatus.status=='toReview' || trade.finalOrderStatus.status=='completed']
                            <a href="${base}/wap/member/order/order_evaluate.jhtml?id=${trade.id}"
                               class="weui_btn weui_btn_mini weui_btn_plain_default"
                               style="color:white;background-color:red;margin-top:15px;">立即评价</a>
                        [#elseif trade.finalOrderStatus.status=='waitReturn']
                            <a href="javascript:waitShippingRemind(${trade.id});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default"
                               style="color:white;background-color:red;margin-top:15px;">提醒商家退货</a>
                        [/#if]
                    </div>
                [/#if]
            </div>
        [/#list]
        </div>
    </div>
    [#include "/wap/include/footer.ftl" /]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});
    });
    //取消订单
    function waitPayCancel(sn) {
        ajaxPost({
            url: '${base}/app/member/order/cancel.jhtml',
            data: {sn: sn},
            success: function (message) {
                if (message.message.type == "success") {
                    showToast();
                    window.setTimeout(function () {
                        window.location.reload(true);
                    }, 600);
                } else {
                    showDialog2("提示", message.message.content);
                }
            }
        });
    }

    //退货申请
    function waitReturn(id) {
        showDialog1("提示", '您确定要申请退货吗？', function () {
            ajaxPost({
                url: '${base}/app/member/order/return.jhtml',
                data: {id: id},
                success: function (message) {
                    if (message.message.type == "success") {
                        showToast();
                        window.setTimeout(function () {
                            window.location.reload(true);
                        }, 600);
                    } else {
                        showDialog2("提示", message.message.content);
                    }
                }
            });
        });
    }

    //立即签收
    function waitShippingConfirm(id) {
        showDialog1("提示", '您确定要签收这个订单吗？', function () {
            ajaxPost({
                url: '${base}/app/member/order/confirm.jhtml',
                data: {id: id},
                success: function (data) {
                    if (data.message.type == "success") {
                        showToast();
                        setTimeout(function () {
                            location.href = "${base}/wap/member/order/order_evaluate.jhtml?id=" + id;
                        }, 600);
                    } else {
                        showDialog2("提示", data.message.content);
                    }

                }
            });
        });
    }

    //立即支付
    function waitShippingPay(sn) {
        [#--ajaxPost({--]
            [#--url: '${base}/wap/payment/create.jhtml?type=payment&sn=' + sn,--]
            [#--success: function (data) {--]
                [#--if (data.message.type === 'success') {--]
                    [#--ajaxPost({--]
                        [#--url: '${base}/wap/payment/index.jhtml?sn=' + data.data,--]
                        [#--success: function (data) {--]
                            [#--alert(data);--]
                        [#--}--]
                    [#--});--]
                [#--}--]
            [#--}--]
        [#--});--]
        ajaxPost({
            url:'${base}/app/member/order/payment.jhtml',
            data:{sn:sn},
            success:function(dataBlock){
                if(dataBlock.message.type="success"){
                    window.location.href="${base}/wap/payment/index.jhtml?sn="+dataBlock.data;
                }else{
                    showDialog2("提示",dataBlock.message.content,function(){
                        location.href="${base}/wap/member/order/list.jhtml";
                    });
                }
            }
        });

    }

    //提醒商家
    function waitShippingRemind(id) {
        showDialog1("提示", '您确定要执行此操作吗？', function () {
            ajaxPost({
                url: '${base}/app/member/order/remind.jhtml',
                data: {id: id},
                success: function (data) {
                    if (data.message.type = "success") {
                        showToast({content: "已提醒"});
                    } else {
                        showDialog2("提示", data.message.content);
                    }
                }
            });
        });
    }


</script>
</body>
</html>
