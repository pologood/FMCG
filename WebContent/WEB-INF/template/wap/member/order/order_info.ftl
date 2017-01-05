<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <title>订单详情</title>
    <style type="text/css">
        body {
            background-color: white;
        }

        .weui_cells:before {
            border-top: 0;
            left: 0;
        }

        .weui_cells:after {
            border-bottom: 0;
        }

        .weui_cell:before {
            border-top: 0;
        }
    </style>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>

    <script type="text/x-handlebars-template" id="order_info_tpl">
        <div class="weui_cells_title"
             style="background-color:#E8EAE9;height:45px;margin-top:0;margin-bottom:0;position:static;">
            <span style="font-size:18px;color:#000000;line-height:45px;">交易状态</span>
            <span style="margin-left:20px;color:#e81827;line-height:45px;">{{finalOrderStatus.desc}}</span>
        </div>

        <div style="margin_bottom:0px;">
            [#list orderlogs as orderlog]
            <div style="padding:0 10px;height:70px;background-color:white;" id="yifahuo">
                <div style="width:95%;height:1px;border-top:1px solid #E8EAE9;margin-left:16px;"></div>
                <div style="display:none;" id="index">${orderlog_index+1}</div>
                <img src="${base}/resources/wap/2.0/images/shuxian.png" id="yifahuo_shang_img" alt="icon"
                     style="width:2px;height:50px;margin-left:4px;margin-top:-23px;">
                <div style="">
                    <img src="${base}/resources/wap/2.0/images/yuan.png" id="yifahuo_yuan_img" alt="icon"
                         style="width:10px;">
                    <span style="font-size:16px;">${orderlog.content}</span><span style="float:right;">
                    ${orderlog.createDate?string("yyyy-MM-dd HH:mm:ss")}
            </span>
                </div>
                <div style="float:left;">
                    <p style="font-size:10px;color:#888;display:inline;margin-left:15px;line-height:15px;width:100%;">
                        ${orderlog.content}</p>
                </div>
            </div>
            [/#list]

        </div>

        <div class="weui_cells_title" style="background-color:#E8EAE9;height:45px;">
            <span style="font-size:18px;color:#000000;line-height:45px;">其他信息</span>
        </div>

        <div class="weui_cells" style="font-color:#A0A0A0;line-height:5px;font-size:15px;border-top:0;">
            <div class="weui_cell" style="margin-top:10px;position:initial;">
                <div class="weui_cell_bd">
                    <p style="color:#888">支付方式</p>
                </div>
                <div class="weui_cell_ft weui_cell_primary" style="line-height: 1.2;">{{paymentMethod.name}}</div>
            </div>
            <div class="weui_cell" style="position:initial;">
                <div class="weui_cell_bd" style="color:#888">
                    <p>配送方式</p>
                </div>
                <div class="weui_cell_ft weui_cell_primary" style="line-height: 1.2;">{{shippingMethod.name}}</div>
            </div>
            [#if trade.order.shippingMethod.method=="F2F"]
                <div class="weui_cell" style="position:initial;">
                    <div class="weui_cell_bd" style="color:#888">
                        <p>提货地址</p>
                    </div>
                    <div class="weui_cell_ft weui_cell_primary" style="line-height: 1.2;">${(trade.deliveryCenter.area.fullName)!}${(trade.deliveryCenter.address)!}</div>
                </div>
                <div class="weui_cell" style="position:initial;">
                    <div class="weui_cell_bd" style="color:#888">
                        <p><font color="orange">提货时间</font></p>
                    </div>
                    <div class="weui_cell_ft weui_cell_primary" style="line-height: 1.2;"><font color="orange">${(trade.deliveryDate?string("yyyy年MM月dd日HH时"))!"等待商家确认"}</font></div>
                </div>
            [/#if]
            [#--<div class="weui_cell" style="position:initial;">--]
                [#--<div class="weui_cell_bd weui_cell_primary" style="color:#888">--]
                    [#--<p>{{deliveryCorp}}</p>--]
                [#--</div>--]
                [#--<div class="weui_cell_ft ">运单号:{{trackingNo}}</div>--]
            [#--</div>--]
            <div class="weui_cell" style="position:initial;">
                <div class="weui_cell_bd" style="color:#888">
                    <p>订单号</p>
                </div>
                <div class="weui_cell_ft weui_cell_primary" style="line-height: 1.2;">{{sn}}</div>
            </div>
            <div class="weui_cell" style="margin-bottom:10px;position:initial;">
                <div class="weui_cell_bd" style="color:#888">
                    <p>提货码</p>
                </div>
                <div class="weui_cell_ft weui_cell_primary" style="line-height: 1.2;">{{pickUpCode}}</div>
            </div>
        </div>

        <div class="weui_cells_title" style="background-color:#E8EAE9;height:45px;margin-top:0px;margin-bottom:0px;">
            <span style="font-size:18px;color:#000000;line-height:45px;">商品信息</span>
        </div>

        <div style="margin-top:0;">
            <a class="weui_cell" href="${base}/wap/tenant/index/{{tenant.id}}.jhtml">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="iconfont" style="color:#333333 ;">&#xe663;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p style="font-size:18px;color:#333;">{{tenant.shortName}}</p>
                </div>
                <div class="weui_cell_ft">
                    <i class="iconfont" style="font-size:10px;color:#333333 ;">&#xe65f;</i>
                </div>
            </a>
            <div style="width:100%;height:1px;border-top:1px solid #E8EAE9;"></div>
            {{#each orderItems}}
            <div class="weui_cells" style="margin-top:0px;">
                <a class="weui_cell" href="${base}/wap/product/content/{{productId}}/product.jhtml">
                    <div class="weui_cell_hd" >
                        <img src={{thumbnail}} alt="icon" style="width:80px;margin-right:5px;display:block">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary" style="padding-top:2px;">
                        <p  class="font-default" style="color:#000000;display: -webkit-box;-webkit-line-clamp:2;-webkit-box-orient: vertical;overflow: hidden;">{{fullName}}</p>
                        <p style="font-size:15px;color:#E6C99C;margin-top:2px;">
                            {{#each promotions}}
                            <span style="width:150px;height:50px;border:1px solid #E6C99C;border-radius:3px;color:#E6C99C">{{name}}</span>
                            {{/each}}
                        </p>
                        <!--<p style="font-size:12px;color:#A0A0A0;margin-top:2px;">订单号：{{sn}}</p>-->
                    </div>
                    <div style="text-align:right;padding-top:2px;color:#000000;">
                        <p class="font-large">￥{{price}}</p>
                        <p>&nbsp;</p>
                        <p class="font-small">x{{quantity}}</p>
                    </div>
                </a>
                <div style="padding:0 15px;">
                    <div style="width:100%;height:1px;border-top:1px solid #E8EAE9;"></div>
                </div>
                {{/each}}

                <div class="weui_cell">
                    <div class="weui_cell_bd weui_cell_primary">
                        <p>商品金额</p>
                        {{#expression promotionDiscount '!=' '0'}}<p>优惠</p>{{/expression}}
                        {{#expression freight '!=' '0'}}<p style="">运费</p>{{/expression}}
                    </div>
                    <div style="text-align:right;">
                        <p id="subTotal_qz" class="font-default">￥{{price}}</p>
                        {{#expression promotionDiscount '!=' '0'}}<p class="font-small">￥{{promotionDiscount}}</p>{{/expression}}
                        {{#expression freight '!=' '0'}}<p class="font-default">￥{{freight}}</p>{{/expression}}
                    </div>
                </div>

                <div class="weui_cell" style="border-top:1px solid #E8EAE9;">
                    <div class="weui_cell_bd weui_cell_primary">
                        <p>&nbsp;</p>
                    </div>
                    <div style="text-align:right;">
                        <p>实付款：<span style="color:#ed0f0f;font-size:20px;">￥{{amount}}</span></p>
                    </div>
                </div>

            </div>
        </div>

        <div style="background-color:#E8EAE9;height:50px;padding-right:10px;padding-top:10px;" id="orderStatus">

        </div>
        <form id="indexForm" action="" method="post" style="display: none;">
            <input name="sn" id="sn">
        </form>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page" style="background-color:white;">

    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script>
    $(function () {
        init();
        ajaxGet({
            url: "${base}/app/member/order/view.jhtml",
            data: {
                id:${id}
            },
            success: function (data) {
                var compiler = Handlebars.compile($("#order_info_tpl").html());
                $(".page").html(compiler(data.data));
                $("#index").next().css("height", "30px").css("margin-top", "-1px");
                $("#index").prev().css("display", "none");
                getStatus(data.data.finalOrderStatus.status);
                $("#order_return").on("click",function(){
                    order_return(data.data.sn);
                });
                $("#order_payment").on("click",function(){
                    order_payment(data.data.sn);
                });
                $("#order_warn").on("click",function(){
                    order_warn();
                });
                $("#order_accept").on("click",function(){
                    order_accept(data.data.sn);
                });
                $("#order_evaluate").on("click",function(){
                    order_evaluate(data.data.sn);
                });
                $("#cancel_order").on("click",function(){
                    cancel_order(data.data.sn);
                });


            }
        });
    });
    /**取消订单*/
    function cancel_order(sn) {
        ajaxPost({
            url: "${base}/app/member/order/lock.jhtml",
            data:{
                sn: sn
            },
            success: function (data2) {
                closeWaitLoadingToast();
                if (data2.message.type == "warn") {
                    var message = {content: "该订单已被锁定，请稍后重试"};
                    showToast2(message);
                } else if (data2.message.type == "success") {
                    showDialog1("订单提醒", "确认要取消订单吗？", function(){
                        ajaxPost({
                            url: "${base}/app/member/order/cancel.jhtml",
                            data:{
                                sn:sn
                            },
                            success: function (data3) {
                                closeWaitLoadingToast();
                                if (data3.message.type == "success") {
                                    showToast(data3.message);
                                }
                            }
                        })
                    });
                }
            }
        });
    }
    /**立即评论*/
    function order_evaluate(sn){
         ajaxPost({
            url: "${base}/app/member/order/lock.jhtml",
            data:{
                sn: sn
            },
            success: function (data2) {
                closeWaitLoadingToast();
                if (data2.message.type == "warn") {
                    var message = {content: "该订单已被锁定，请稍后重试"};
                    showToast2(message);
                } else if (data2.message.type == "success") {
                    location.href="${base}/wap/member/order/order_evaluate.jhtml?id=${id}";
                }
            }
        });
    }
    /**立即签收*/
    function order_accept(sn){
         ajaxPost({
            url: "${base}/app/member/order/lock.jhtml",
            data:{
                sn: sn
            },
            success: function (data2) {
                closeWaitLoadingToast();
                if (data2.message.type == "warn") {
                    var message = {content: "该订单已被锁定，请稍后重试"};
                    showToast2(message);
                } else if (data2.message.type == "success") {
                    showDialog1("提示", '您确定要签收这个订单吗？', function () {
                        ajaxPost({
                            url: '${base}/app/member/order/confirm.jhtml',
                            data: {
                                id: ${id}
                            },
                            success: function (data) {
                                if (data.message.type == "success") {
                                    showToast();
                                    setTimeout(function () {
                                        location.href = "${base}/wap/member/order/order_evaluate.jhtml?id=${id}";
                                    }, 600);
                                } else {
                                    showDialog2("提示", data.message.content);
                                }

                            }
                        });
                    });
                }
            }
        });
    }
    /**立即支付*/
    function order_payment(sn){
         ajaxPost({
            url: "${base}/app/member/order/lock.jhtml",
            data:{
                sn:sn
            },
            success: function (data2) {
                closeWaitLoadingToast();
                if (data2.message.type == "warn") {
                    var message = {content: "该订单已被锁定，请稍后重试"};
                    showToast2(message);
                } else if (data2.message.type == "success") {
                    ajaxPost({
                        url:'${base}/app/member/order/payment.jhtml',
                        data:{
                            sn:sn
                        },
                        success:function(dataBlock){
                            if(dataBlock.message.type="success"){
                                $("#sn").val(dataBlock.data);
                                $("#indexForm").attr("action","${base}/wap/payment/index.jhtml");
                                $("#indexForm").submit();
                            }else{
                                showDialog2("提示",dataBlock.message.content,function(){
                                    location.href="${base}/wap/member/order/list.jhtml";
                                });
                            }
                        }
                    });
                }
            }
        });
    }
    /**申请退货*/
    function order_return(sn){
         ajaxPost({
            url: "${base}/app/member/order/lock.jhtml",
            data:{
                sn:sn
            },
            success: function (data2) {
                closeWaitLoadingToast();
                if (data2.message.type == "warn") {
                    var message = {content: "该订单已被锁定，请稍后重试"};
                    showToast2(message);
                } else if (data2.message.type == "success") {
                    showDialog1("提示", '您确定要申请退货吗？', function () {
                        ajaxPost({
                            url: '${base}/app/member/order/return.jhtml',
                            data: {
                                id: ${id}
                            },
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
            }
        });
    }
     /**提醒商家发货*/
    function order_warn(){
         showDialog1("提示", '您确定要执行此操作吗？', function () {
            ajaxPost({
                url: '${base}/app/member/order/remind.jhtml',
                data: {
                    id: ${id}
                },
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
    /**判断当前订单可以执行的动作*/
    function getStatus(v) {
        if(v=="waitPay"){
            $("#orderStatus").html("<a href='javascript:;' " +
                    "style='width:90px;height:30px;border:1px solid #666;border-radius:3px;display:inline;float:right;text-align:center;line-height:30px;'" +
                    "id='order_payment'>"+
            "<span style='color:#666;font-size:16px;'>立即支付</span></a>"+
            "<a style='width:90px;height:30px;border:1px solid #e81827;border-radius:3px;display:inline;float:right;text-"+
            "align:center;margin-right:10px;line-height:30px;' id='cancel_order'>"+
                "<span style='color:#e81827;font-size:16px;'>取消订单</span>"+
            "</a>");
        }else if(v=="waitShipping"){
            $("#orderStatus").html("<a href='javascript:;' " +
                    "style='width:90px;height:30px;border:1px solid #333333 ;border-radius:3px;display:inline;float:right; text-align:center;line-height:30px;'>"+
                "<span style='color:#666;font-size:16px;' id='order_return'>退货申请</span>"+
            "</a>"+
            "<a style='width:115px;height:30px;border:1px solid #e81827;border-radius:3px;display:inline;float:right;text-align:center;margin-right:10px;line-height:30px;' id='order_warn'>"+
                "<span style='color:#e81827;font-size:16px;'>提醒商家发货</span>"+
            "</a>");
        }else if(v=="sign"||v=="partialShipment"){
            $("#orderStatus").html("<a href='javascript:;' " +
                    "style='width:90px;height:30px;border:1px solid #666;border-radius:3px;display:inline;float:right;text-align:center;line-height:30px;' id='order_return'>"+
                "<span style='color:#666;font-size:16px;'>退货申请</span>"+
            "</a>"+
            "<a style='width:90px;height:30px;border:1px solid #e81827;border-radius:3px;display:inline;float:right;text-align:center;margin-right:10px;line-height:30px;' id='order_accept'>"+
                "<span style='color:#e81827;font-size:16px;'>立即签收</span>"+
            "</a>");
        }else if(v=="toReview"){
            $("#orderStatus").html(
                "<a href='javascript:;' " +
                    "style='width:90px;height:30px;border:1px solid #666;border-radius:3px;display:inline;float:right;text-align:center;line-height:30px;' " +
                    "id='order_evaluate'>"+
                "<span style='color:#666;font-size:16px;'>立即评价</span>"+
            "</a>");
            // "<a href='javascript:;' " +
            //         "style='width:90px;height:30px;border:1px solid #666;border-radius:3px;display:inline;float:right;text-align:center;line-height:30px;margin-right:5px;' id='order_return'>"+
            //     "<span style='color:#666;font-size:16px;'>退货申请</span>"+
            // "</a>");
        }else if(v=="waitReturn"){
            $("#orderStatus").html("<a " +
                    "style='width:115px;height:30px;border:1px solid #e81827;border-radius:3px;display:inline;float:right;text-align:center;margin-right:10px;line-height:30px;' " +
                    "id='order_warn'>"+
                    "<span style='color:#e81827;font-size:16px;'>提醒商家发货</span>"+
                "</a>");
        }else{
            return $("#orderStatus").hide();
        }
    }
    /**自定义if标签*/
    Handlebars.registerHelper('ifCond', function(v1, v2, options) {
        if(v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });
</script>
</body>
</html>
