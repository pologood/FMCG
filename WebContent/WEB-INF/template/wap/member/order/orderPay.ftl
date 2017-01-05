<!DOCTYPE html>
<html lang="en">
<head>
    <title>确认订单</title>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <script src="${base}/resources/wap/2.0/js/flexible.debug.js"></script>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/base64.js"></script>
    <style type="text/css">
        div, select {
            font-family: "Microsoft YaHei";
        }
    </style>
</head>
<body class="MR-OROY">
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <form id="indexForm" action="" method="post" style="display: none;">
        <input name="sn" id="sn">
    </form>
    <form id="orderForm" action="create.jhtml" method="post">
        <input type="hidden" id="receiverId" name="receiverId" value="${(receiver.id)!}">
        <input type="hidden" id="isInvoice" name="isInvoice" value="false">
        <input type="hidden" id="useBalance" name="useBalance" value="false"/>
        <input type="hidden" id="cartToken" name="cartToken" value="${cartToken}"/>
        <input type="hidden" id="enPassword" name="enPassword"/>
        <input type="hidden" id="paymentMethod_wx" name="paymentMethod_wx" value="${(paymentMethod.method)!}"/>

        <!-- 订单条件(方式)-->
        <div class="weui_cells weui_cells_form order_methodsR MR-OROY">
            <div class="weui_cell weui_cell_select order_methodsR-select">
                <div class="weui_cell_hd hd">支付方式：</div>
                <div class="weui_cell_bd bd weui_cell_primary selectR">
                    <select class="weui_select" name="paymentMethodId" style="color:#888;"
                            data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
                    [#list paymentMethods as paymentMethod]
                        [#if paymentMethod.method=='offline']
                            [#break /]
                        [/#if]
                        <option value="${paymentMethod.id}"
                                paymentMethod="${paymentMethod.method}">${paymentMethod.name}</option>
                    [/#list]
                    </select>
                </div>
            </div>
            <div class="weui_cell weui_cell_select order_methodsR-select">
                <div class="weui_cell_hd hd">配送方式：</div>
                <div class="weui_cell_bd bd weui_cell_primary selectR">
                    <select class="weui_select" name="shippingMethodId" style="color:#888;"
                            data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
                    [#list shippingMethods as shippingMethod]
                        <option value="${shippingMethod.id}" method="${shippingMethod.method}">
                        ${shippingMethod.name}
                        </option>
                    [/#list]
                    </select>
                </div>
            </div>

            <div class="weui_cell addtiondesc MR-OROY">
                <div class="weui_cell_bd weui_cell_primary">
                    <textarea class="weui_textarea" placeholder="订单补充说明" rows="1" name="memo"></textarea>
                </div>
            </div>
        [#if couponCodes??&&couponCodes?has_content]
            <div class="weui_cell weui_cell_select order_methodsR-select">
                <div class="weui_cell_hd hd">优惠券：</div>
                <div class="weui_cell_bd bd weui_cell_primary selectR">
                    <select class="weui_select" name="code" style="color:#888;"
                            data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
                        <option selected="" value="">请选择优惠券</option>
                        [#list couponCodes as couponCode]
                            <option value="${couponCode.code}">
                            ${couponCode.coupon.name}
                            </option>
                        [/#list]
                    </select>
                </div>
            </div>
        [/#if]
        </div>

        <div class="weui_cells weui_cells_access receiverR MR-OROY">
            <a class="weui_cell"
               href="${base}/wap/member/receiver/list.jhtml?fromCart=true&backUrl=${bse}/wap/member/order/orderPay.jhtml">
                <div class="weui_cell_bd weui_cell_primary receiverR-main">
                    <p>
                        <span>收货人：${(receiver.consignee)!}</span>
                        <span style="float: right;">${(receiver.phone)!}</span>
                    </p>
                    <p>
                        <i class="iconfont">&#xe649;</i>${(receiver.areaName)!}${(receiver.address)!}
                    </p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
        </div>

    [#if order??&&order.trades?has_content]
        [#list order.trades as trade]
            <div class="weui_cells weui_cells_access order_tradeR MR-OROY">
                [#--店铺名称--]
                <a class="weui_cells_title weui_cell storenameR"
                   href="${base}/wap/tenant/index/${trade.tenant.id}.jhtml">
                    <div class="weui_cell_hd">
                        <i class="iconfont">&#xe663;</i>
                    </div>
                    <div class="weui_cell_bd weui_cell_primary">
                        &nbsp;&nbsp;${trade.tenant.name}
                    </div>
                </a>
                <div class="weui_cells parent">
                    [#list trade.effectiveOrderItems as orderItem]
                        <a class="weui_cell goodsR"
                           href="${base}/wap/product/content/${orderItem.product.id}/product.jhtml">
                            [#--商品图片--]
                            <div class="weui_cell_hd">
                                <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                                     data-original="${orderItem.product.thumbnail}"
                                     class="lazy" alt="icon" style="width:80px;margin-right:5px;display:block">
                            </div>
                            <div class="weui_cell_bd weui_cell_primary goodsR-txtR">
                                [#--商品名称--]
                                <p class="goodsR-txtR-desc">${abbreviate(orderItem.product.fullName,45,"...")}</p>
                                [#--商品规格--]
                                <p class="goodsR-txtR-clr">
                                    [#if jsonArray??&&jsonArray?has_content]
                                        [#list jsonArray as product]
                                            [#if product.productID==orderItem.product.id]
                                                [#if product.color??&&product.color?has_content]
                                                    颜色：${product.color}[/#if]
                                                &nbsp;&nbsp;
                                                [#if product.spec??&&product.spec?has_content]尺码：${product.spec}[/#if]
                                            [/#if]
                                        [/#list]
                                    [/#if]
                                </p>
                                <p class="goodsR-txtR-price">
                                    [#--商品价格--]
                                    <span class="pt1">￥${orderItem.price}</span>
                                    [#-- <del class="pt2">￥${orderItem.product.marketPrice}</del>--]
                                    [#--商品活动--]
                                    [#if promotionModel??&&promotionModel?has_content]
                                        [#list promotionModel as promotion]
                                            [#if promotion.productId==orderItem.product.id]
                                                [#if promotion.type=='buyfree']
                                                    <span class="order-gift MR-OROY">${promotion.name}</span>
                                                [/#if]
                                                [#if promotion.type=='seckill']
                                                    <span class="order-discount MR-OROY">${promotion.name}</span>
                                                [/#if]
                                            [/#if]
                                        [/#list]
                                    [/#if]
                                </p>
                            </div>
                            [#--商品数量--]
                            <div class="weui_cell_bd goodsR-howmany">
                                <b>x${orderItem.quantity}</b>
                            </div>
                        </a>
                    [/#list]
                    [#--<div>--]
                    [#--提货地址--]
                    <div class="weui_cell weui_cell_select order_methodsR-select hidden deliveryCenter">
                        <div class="weui_cell_hd hd">提货地址：</div>
                        <div class="weui_cell_bd bd weui_cell_primary selectR">
                            <select class="weui_select" name="deliveryCenterIds" style="color:#888;"
                                    data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
                                [#list trade.tenant.deliveryCenters as deliveryCenter]
                                    <option value="${deliveryCenter.id}">
                                    ${deliveryCenter.area.fullName}${deliveryCenter.address}
                                    </option>
                                [/#list]
                            </select>
                        </div>
                    </div>
                    [#--提货时间--]
                    <div class="weui_cell hidden delliveryDate">
                        <div class="weui_cell_hd weui_cell_primary">
                            <p style="color:orange;">提货时间：</p>
                        </div>
                        <div class="weui_cell_bd">
                            <p style="color:orange;">等待商家确认</p>
                        </div>
                    </div>
                    [#--</div>--]
                    [#--快递运费--]
                    <a class="weui_cell freight" href="javascript:;" [#if trade.freight==0]style="display: none;"[/#if]>
                        <div class="weui_cell_bd weui_cell_primary">快递运费：</div>
                        <div style="text-align:right;" name="freight" tenantId="${trade.tenant.id}">
                            ￥${trade.freight}</div>
                    </a>
                    [#--折扣金额--]
                    [#if trade.promotionDiscount??&&trade.promotionDiscount!=0]
                        <div class="weui_cell">
                            <div class="weui_cell_hd weui_cell_primary">
                                <p><i class="iconfont" style="font-size:25px;color:#EE2C2C">&#xe65d;</i> 优惠金额</p>
                            </div>
                            <div class="weui_cell_bd">
                                <p>-￥${trade.promotionDiscount!0}</p>
                            </div>
                        </div>
                    [/#if]
                    [#--赠品--]
                    [#if trade.giftItems?has_content]
                        <div class="weui_cell">
                            <div class="weui_cell_hd">
                                <p><i class="iconfont" style="font-size:25px;color:#FF8C00;">&#xe65c;</i></p>
                            </div>
                            <div class="weui_cell_bd weui_cell_primary" style="padding-left: 5px;">
                                [#list trade.giftItems as giftItem]
                                    <p>${giftItem.fullName}</p>
                                [/#list]
                            </div>
                            <div class="weui_cell_bd">
                                <p>赠品</p>
                            </div>
                        </div>
                    [/#if]
                    <!-- 店铺小计-->
                    <a class="weui_cell" href="javascript:;">
                        <div class="weui_cell_bd weui_cell_primary"></div>
                        <div style="text-align:right;">
                            <p>小计：
                                <span style="color:red;" tenantId="${trade.tenant.id}" name="trade_amount">￥${trade.amount}</span>
                            </p>
                        </div>
                    </a>
                </div>

            </div>
        [/#list]
    [/#if]
        [#--挑货立减--]
        <div class="weui_cells weui_cells_access order_tradeR MR-OROY">
            <a class="weui_cell" href="javascript:;" [#if order.discount==0]style="display: none;"[/#if]>
                <i class="iconfont ft-bs05 promtag pt-activity"></i>
                <div class="weui_cell_bd weui_cell_primary">${setting.siteName}活动立减：</div>
                <div style="text-align:right;" id="orderDiscount">￥${order.discount}</div>
            </a>
        </div>

        <div class="weui_cells" style="margin-top: 0;">
            <div class="weui_cell">
                <div class="weui_cell_bd">邀请码：</div>
                <div class="weui_cell_bd weui_cell_primary"><input id="inviteCode" class="weui_input" maxlength="6" type="text" oninput='this.value=this.value.replace(/\D/gi,"")'/></div>
            </div>
        </div>
    </form>
    <div style="height: 100px;background-color: #e8eaea;"></div>
    [#--总计--]
    <header class="am-topbar-fixed-bottom">
        <div class="weui_cells cl" id="cart-footer">
            <div class="pd-cl weui_cell">
                <div class="weui_cell_hd color-revert ">
                </div>
                <div class=" weui_cell_hd weui_cell_primary am-margin-left-xs color-revert" style="text-align:left;">
                    总计：￥<span id="amountPayable">${order.amount}</span>
                </div>
                <a id="submit" class="weui_cell_hd bg-red color-revert settle" href="javascript:;" onclick="submit()">
                    <sapn>确认下单</sapn>
                </a>
            </div>
        </div>
    </header>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});

        calculate();

        $("select[name='shippingMethodId']").on("change", function () {
            console.log($(this).find("option:selected").attr("method"));
            if($(this).find("option:selected").attr("method")=="F2F"){
                $(".delliveryDate").removeClass("hidden");
                $(".deliveryCenter").removeClass("hidden");
            }else{
                $(".delliveryDate").addClass("hidden");
                $(".deliveryCenter").addClass("hidden");
            }
            calculate();
        });

        $("select[name='code']").on("change", function () {
            calculate();
        });

        [#--if ($("#receiverId").val() == "" || $("#receiverId").val() == null) {--]
            [#--showToast2({content: "您还没有收货地址，正在为您跳转。。"});--]
            [#--setTimeout(function () {--]
                [#--location.href = "${base}/wap/member/receiver/list.jhtml?fromCart=true&backUrl=/wap/member/order/orderPay.jhtml&addReceiver=1";--]
            [#--}, 1500);--]
        [#--}--]

    });

    //提交订单
    function submit() {
        if ($("select[name='paymentMethodId']").val() == 0) {
            showDialog2("友情提示", "请选择支付方式！");
            return;
        }
        if ($("select[name='shippingMethodId']").val() == 0) {
            showDialog2("友情提示", "请选择配送方式！");
            return;
        }
        if($("#inviteCode").val().trim()!=""){
            ajaxGet({
                url:'${base}/app/member/invite_code.jhtml',
                data:{code:$("#inviteCode").val()},
                success:function(data){
                    if(data.message.type=="success"){
                        var jsons=$("#orderForm").serialize()+"&extensionId="+data.data+"&token_key=${token_key!''}";
                        console.log(jsons);
                        submitForm(jsons);
                    }else{
                        showDialog2("友情提示",data.message.content);
                    }
                }
            });
        }else{
            var jsons=$("#orderForm").serialize()+"&token_key=${token_key!''}";
            console.log(jsons);
            submitForm(jsons);
        }
    }

    function submitForm(postData){
        console.log(postData);
        ajaxPost({
            url: '${base}/app/member/order/create.jhtml',
            data: postData,
            success: function (data) {
                if (data.message.type == "success") {
                    if ($("select[name='paymentMethodId']").find("option:selected").attr("paymentMethod").trim() == "offline") {
                        location.href = "${base}/wap/payment/orderNotify.jhtml";
                        return;
                    } else {
                        ajaxPost({
                            url: '${base}/app/member/order/payment.jhtml',
                            data: {sn: data.data},
                            success: function (dataBlock) {
                                if (dataBlock.message.type = "success") {
                                    $("#sn").val(dataBlock.data);
                                    $("#indexForm").attr("action", "${base}/wap/payment/index.jhtml");
                                    $("#indexForm").submit();
                                } else {
                                    showDialog2("提示", dataBlock.message.content, function () {
                                        //location.href = "${base}/wap/member/order/list.jhtml";
                                    });
                                }
                            }
                        });
                    }
                } else {
                    showDialog2("提示", data.message.content, function () {
                        //location.href = "${base}/wap/member/order/list.jhtml";
                    });
                }
            }
        });
    }

    //计算费用
    function calculate() {
        $.ajax({
            url: "${base}/app/member/order/calculateW.jhtml",
            type: "POST",
            data: $("#orderForm").serialize(),
            dataType: "json",
            cache: false,
            beforeSend: function () {
                $("#submit").prop("disabled", true);
            },
            complete: function () {
                closeWaitLoadingToast();
                $("#submit").prop("disabled", false);
            },
            success: function (data) {
                if (data.message.type == "success") {
                    $("#amountPayable").text(data.data.amountPayable);
                    $("#orderDiscount").text(data.data.discount);
                    $.each(data.data.trades, function (k, v) {
                        $.each($("[name='trade_amount']"), function () {
                            if (v.tenantId == $(this).attr("tenantId")) {
                                $(this).text("￥" + v.amount);
                                if (v.freight != 0) {
                                    $(this).parents(".parent").find(".freight").show();
                                }
                                $(this).parents(".parent").find("[name='freight']").text(v.freight);
                            }
                        });
                    });
                }
            }
        });
    }

</script>
</body>
</html>
