<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>订单详情</title>
    <style type="text/css">
        .spanRadius_3 {
            border-radius: 50%;
            background-color: #ff0000;
            color: #fff;
            position: absolute;
            top: 10px;
            margin-left: 10px;
            z-index: 80;
            transform: translate(50%, -50%);
            width: 18px;
            height: 18px;
            line-height: 18px;
            text-align: center;
            font-size: 10px;
        }

        .am-text-center a {
            color: #333;
        }

        .am-gallery-default.am-avg-5 .visit {
            color: #e60012;
            border-bottom: 3px solid #e60012;
        }
        #pullUpLabel{
            text-align: center;
            line-height: 50px;
            color: #0f0f0f;
            font-weight: bold;
            font-size: 20px;
            background: #e8eaea;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
    <div class="page">
        <div class="empty-for-fixedtop_tab" ></div>
        <div id="orderList">
        {{#each this}}
            <div class="weui_cells weui_cells_access" style="margin-top: 0.5em;">
                <a class="weui_cell weui_cells_title" style="font-size: 16px;margin: 0;" href="${base}/wap/tenant/index/{{tenant.id}}.jhtml">
                    <div class="weui_cell_hd am-margin-right-sm">
                        <i class="iconfont">&#xe663;</i>
                    </div>
                    <div class="weui_cell_bd weui_cell_primary">
                        {{tenant.shortName}}
                    </div>
                    <div class="weui_cell_ft font-small red">{{finalOrderStatus.desc}}</div>
                </a>
                <div class="weui_cells weui_cells_access" style="margin-top:0px;">
                    <a class="weui_cell goodsR" href="${base}/wap/member/order/order_info.jhtml?id={{id}}">
                        <div class="weui_cell_hd">
                            <!-- 商品缩略图 -->
                            <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                                 data-original="{{thumbnail}}"
                                 class="lazy" alt="icon" style="width:80px;margin-right:5px;display:block">
                        </div>
                        <div class="weui_cell_bd weui_cell_primary goodsR-txtR">
                            <p class="font-small" style="color:#aaa">订单号：{{sn}}</p>
                            <p class="font-default dark-grey goodsR-txtR-desc">{{orderItem.fullName}}</p>
                            
                            <p class="goodsR-txtR-price">
                                <span class="pt1">￥{{orderItem.price}}</span>
                                {{#if orderItem.promotions}}
                                    [#list promotions as promotion]
                                        [#if promotion.productId==order.product.id]
                                            [#if promotion.type=='buyfree']
                                                <span class="order-gift MR-OROY">${promotion.name}</span>
                                            [/#if]
                                            [#if promotion.type=='seckill']
                                                <span class="order-discount MR-OROY">${promotion.name}</span>
                                            [/#if]
                                        [/#if]
                                    [/#list]
                                {{/if}}
                            </p>
                        </div>
                        <div class="weui_cell_bd goodsR-howmany" style="text-align:right;">
                            <b>x{{orderItem.quantity}}</b><!-- 商品数量 -->
                        </div>
                    </a>
                    
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary"></div>
                        <div style="text-align:right;">
                            <p style="font-size:16px;">共<span style="color:red;font-size:18px;">{{quantity}}</span>件商品&nbsp;&nbsp;&nbsp;&nbsp;合计：<span
                                    style="color:red;font-size:20px;">￥{{amount}}</span></p>
                        </div>
                    </a>
                </div>

                {{#if finalOrderStatus}}
                    <div class="weui_cells_title"
                         style="line-height:2.0;margin-top:0px;text-align:right;padding-right:20px;">

                        {{#ifCond finalOrderStatus.status 'unconfirmed'}}
                        <a href="javascript:waitReturn({{sn}},{{id}});"
                           class="weui_btn weui_btn_mini weui_btn_plain_default2"
                           style="margin-top:15px;">取消订单</a>
                        {{/ifCond}}

                        {{#ifCond finalOrderStatus.status 'waitPay'}}
                            <a href="javascript:waitPayCancel({{sn}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2">取消订单</a>
                            <a href="javascript:waitShippingPay({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2"
                               style="color:white;background-color: #e71f19;border: 1px solid #e71f19;">立即支付</a>
                        {{/ifCond}}
                        
                        {{#ifCond finalOrderStatus.status 'waitShipping'}}
                            <a href="javascript:waitReturn({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2">退货申请</a>
                            <a href="javascript:waitShippingRemind({{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2"
                               style="color:white;background-color: #e71f19;border: 1px solid #e71f19;margin-top:15px;">提醒商家发货</a>
                        {{/ifCond}}
                        {{#ifCond finalOrderStatus.status 'sign'}}
                            <a href="javascript:waitReturn({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2">退货申请</a>
                            <a href="javascript:waitShippingConfirm({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2"
                               style="color:white;background-color: #e71f19;border: 1px solid #e71f19;margin-top:15px;">立即签收</a>
                        {{/ifCond}}
                        {{#ifCond finalOrderStatus.status 'partialShipment'}}
                            <a href="javascript:waitReturn({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2">退货申请</a>
                            <a href="javascript:waitShippingConfirm({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2"
                               style="color:white;background-color: #e71f19;border: 1px solid #e71f19;margin-top:15px;">立即签收</a>
                        {{/ifCond}}
                        {{#ifCond finalOrderStatus.status 'toReview'}}
                            <a href="javascript:evaluate({{sn}},{{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2"
                               style="color:white;background-color: #e71f19;border: 1px solid #e71f19;margin-top:15px;">立即评价</a>
                        {{/ifCond}}
                        {{#ifCond finalOrderStatus.status 'waitReturn'}}
                            <a href="javascript:waitShippingRemind({{id}});"
                               class="weui_btn weui_btn_mini weui_btn_plain_default2"
                               style="color:white;background-color: #e71f19;border: 1px solid #e71f19;margin-top:15px;">提醒商家退货</a>
                        {{/ifCond}}
                        
                    </div>
                {{/if}}
            </div>
        {{/each}}
        </div>
    </div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <form id="indexForm" action="" method="post" style="display: none;">
        <input name="sn" id="sn"/>
    </form>
    <div id="order_list_content" style="margin-top:40px;"></div>
    <div id="order_list_content"></div>
    <div id="pullUpLabel"></div>
    <div class="weui_cells weui_cells_access fixedtop_tab" style="margin-top:0px;">
        <ul data-am-widget="gallery" class="am-gallery am-gallery-default am-avg-5 am-text-center">
            <li>
                <a href="javascript:;" id="all" onclick="load_order_content('all',1)">
                    <div class="am-gallery-item am-gallery-item-default visit">全部</div>
                </a>
            </li>
            <li>
                <a href="javascript:;" id="unpaid" onclick="load_order_content('unpaid',1)">
                    <div class="am-gallery-item am-gallery-item-default ">
                        <p>待支付</p>
                    [#if unpaid??&&unpaid!=0]
                        <span class="spanRadius_3">${unpaid}</span>
                    [/#if]
                    </div>
                </a>
            </li>
            <li>
                <a href="javascript:;" id="unshipped" onclick="load_order_content('unshipped',1)">
                    <div class="am-gallery-item am-gallery-item-default">
                        <p>待发货</p>
                    [#if unshiped??&&unshiped!=0]
                        <span class="spanRadius_3">${unshiped}</span>
                    [/#if]
                    </div>
                </a>
            </li>
            <li>
                <a href="javascript:;" id="unreciver" onclick="load_order_content('unreciver',1)">
                    <div class="am-gallery-item am-gallery-item-default"><p>待收货</p>
                    [#if shipped??&&shipped!=0]
                        <span class="spanRadius_3">${shipped}</span>
                    [/#if]
                    </div>
                </a>
            </li>
            <li>
                <a href="javascript:;" id="unreview" onclick="load_order_content('unreview',1)">
                    <div class="am-gallery-item am-gallery-item-default"><p>待评价</p>
                    [#if unreview??&&unreview!=0]
                        <span class="spanRadius_3">${unreview}</span>
                    [/#if]
                    </div>
                </a>
            </li>
        </ul>
    </div>
    [#include "/wap/include/footer.ftl" /]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<!-- <script src="${base}/resources/wap/2.0/js/zepto-data.js"></script>-->
<script src="${base}/resources/wap/2.0/js/zepto-callbacks.js"></script>
<script src="${base}/resources/wap/2.0/js/zepto-deferred.js"></script>
<script src="${base}/resources/wap/2.0/js/touch.js"></script>
<script>
    var pageNumber;
    var is_load;
    var is_completed;
    var flag="${type}"==""?"all":"${type}";
    $(function () {
        init();
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        fixedEleCopyHandler(".empty-for-fixedbottom_tab", ".am-topbar-fixed-bottom");
        load_order_content(flag,1);
        scroll(function(){
            if(is_completed=="false"&&is_load=="false"&&pageNumber>1){
                load_order_content(flag,pageNumber);
            }
        });
        
    });
    
    function load_order_content(order_type,num){
        $("#"+order_type).children().addClass("visit");
        $("#"+order_type).parent().siblings().find("div").removeClass("visit");
        is_load="true";
        is_completed="false";
        pageNumber=num;
        flag=order_type;
        ajaxGet({
            url:"${base}/app/member/order/list.jhtml",
            data:{
                type:flag,
                pageNumber:pageNumber,
                pageSize:10
            },
            success:function(data){
                is_load="false";
                var myTemplate = Handlebars.compile($("#wap-list-item").html());
                if(data.pageModel.pageNumber==1){
                    if(data.data.length<10){
                        if(data.data.length==0){
                            $("#pullUpLabel").html("暂无数据！");
                        }
                        is_completed="true";
                    }else if(data.data.length==10){
                        pageNumber++;
                    }
                    $('#order_list_content').html(myTemplate(data.data));
                }else if(data.pageModel.pageNumber>1){
                    if(data.data.length<10){
                        $("#pullUpLabel").html("亲，到底了！");
                        is_completed="true";
                    }else if(data.data.length==10){
                        pageNumber++;
                    }
                    $('#order_list_content').append(myTemplate(data.data));
                }
                $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});
            }
        });
    }
    //取消订单
    function waitPayCancel(sn) {
        checkLock(sn, function () {
            showDialog1("提示", "您确认取消该订单吗？", function () {
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
            });
        });
    }

    //退货申请
    function waitReturn(sn, id) {
        checkLock(sn, function () {
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
        });
    }

    //立即签收
    function waitShippingConfirm(sn, id) {
        checkLock(sn, function () {
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
        });
    }

    //立即支付
    function waitShippingPay(sn,id) {
        checkLock(sn, function () {
            ajaxPost({
                url: '${base}/app/member/order/payment/'+id+'.jhtml',
                //data: {sn: sn},
                success: function (dataBlock) {
                    if (dataBlock.message.type = "success") {
                    [#--window.location.href="${base}/wap/payment/index.jhtml?sn="+dataBlock.data;--]
                        $("#sn").val(dataBlock.data);
                        $("#indexForm").attr("action", "${base}/wap/payment/index.jhtml");
                        $("#indexForm").submit();
                    } else {
                        showDialog2("提示", dataBlock.message.content, function () {
                            location.href = "${base}/wap/member/order/list.jhtml";
                        });
                    }
                }
            });
        });
    }

    //提醒商家
    function waitShippingRemind(id) {
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
    }

    //立即评价
    function evaluate(sn, id) {
        checkLock(sn, function () {
            window.location.href = "${base}/wap/member/order/order_evaluate.jhtml?id=" + id+"&type="+flag;
        })
    }

    function checkLock(sn, fn) {
        ajaxPost({
            url: "${base}/app/member/order/lock.jhtml",
            data: {sn: sn},
            success: function (data) {
                if (data.message.type == "success") {
                    fn();
                } else {
                    showDialog2("提示", data.message.content);
                }
            }
        });
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
