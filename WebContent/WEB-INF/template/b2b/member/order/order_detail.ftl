<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/cart.css" type="text/css" rel="stylesheet">
    <style type="text/css">
        .level_color {
            color: orange;
        }
    </style>
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2b/include/topnav.ftl"]
</div>
<!--主页内容区 -->
<div class="paper">
    <!-- 会员中心头部 -->
[#include "/b2b/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
        [#include "/b2b/include/member_left.ftl"]
            <div class="content">
                <!--订单详情-->
                <div class="order-info-guide">
                    <div class="guide">
                        <div class="guideProcess" id="guideProcess">
                            <div class="guideProcessAll processOne" id="guide">
                                <div class="title">
                                    <h1>1</h1>
                                </div>
                                <span class="word">提交订单</span>
                                <div class="time">
                                    <em>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}</em>
                                </div>
                                <div class="processBar"></div>
                            </div>
                            <div class="guideProcessAll processTwo " id="guide2">
                                <div class="title"><h1>2</h1></div>
                                <span class="word">买家支付</span>
                                <div class="time">
                                    <em>[#if trade.clearingDate?has_content]${trade.clearingDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</em>
                                </div>
                                <div class="processBar"></div>
                            </div>
                            <div class="guideProcessAll processThree " id="guide3">
                                <div class="title"><h1>3</h1></div>
                                <span class="word">卖家发货</span>
                                <div class="time">
                                    <em>[#if trade.shippingDate?has_content]${trade.shippingDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</em>
                                </div>
                                <div class="processBar"></div>
                            </div>
                            <div class="guideProcessAll processFour " id="guide4">
                                <div class="title"><h1>4</h1></div>
                                <span class="word">确认收货</span>
                                <div class="time">
                                    <em>[#if trade.confirmDate?has_content]${trade.confirmDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</em>
                                </div>
                                <div class="processBar"></div>
                            </div>
                            <div class="guideProcessAll processFive " id="guide5">
                                <div class="title"><h1>5</h1></div>
                                <span class="word">评价</span>
                                <div class="time">
                                    <em>[#if trade.review?has_content]${trade.review.createDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</em>
                                </div>
                                <div class="processBar"></div>
                            </div>
                        </div>
                    </div>
                    <div class="order-info">
                        <div class="info-left">
                            <h1>订单信息</h1>
                            <dl>
                                <dt>收 货 人：</dt>
                                <dd>${trade.order.consignee}&nbsp;</dd>
                                <dt>收货地址：</dt>
                                <dd>${trade.order.areaName}&nbsp;${trade.order.address}&nbsp;</dd>
                                <dt>收货邮编：</dt>
                                <dd>${trade.order.zipCode}&nbsp;</dd>
                                <dt>联系电话：</dt>
                                <dd>${trade.order.phone}&nbsp;</dd>
                                <dt>买家备注：</dt>
                                <dd>${trade.memo}</dd>
                            </dl>
                        </div>
                        <div class="right">
                            <div class="status">
                            [#if trade.finalOrderStatus[0].desc=='completed']
                                <i>✔</i>
                            [/#if]
                                <h1>订单状态
                                    <!-- [#if trade.orderStatus=='unconfirmed']
                                    <span>&nbsp;&nbsp;&nbsp;待确认</span>
                                    [#elseif trade.orderStatus=='confirmed']
                                    <span>&nbsp;&nbsp;&nbsp;已确认</span>
                                    [#elseif trade.orderStatus=='completed']
                                    <span>&nbsp;&nbsp;&nbsp;已完成</span>
                                    [#elseif trade.orderStatus=='cancelled']
                                    <span>&nbsp;&nbsp;&nbsp;已取消</span>
                                    [/#if] -->
                                    <span>${trade.finalOrderStatus[0].desc}</span>
                                </h1>
                            </div>
                            <dl>
                                <dt>订单编号：</dt>
                                <dd class="status-left">${trade.order.sn}</dd>
                                <dt>当前状态：</dt>
                                <dd class="status-right">${trade.finalOrderStatus[0].desc}&nbsp;</dd>
                                <dt>支付方式：</dt>
                                <dd class="status-left">[#if trade.order.paymentMethod]${trade.order.paymentMethod.name}[/#if]</dd>
                                <dt>配送方式：</dt>
                                <dd class="status-right">[#if trade.order.shippingMethod??]${trade.order.shippingMethod.name}[/#if]</dd>
                                <dt>交易时间：</dt>
                                <dd class="status-left">${trade.createDate}</dd>
                            </dl>
                        [#if trade.memberReview==null&&trade.orderStatus=="completed"]
                            <a href="javascript:;" onclick="show_or_hide(this)" flag="false">去评价</a>
                        [/#if]
                        </div>
                    </div>
                    <div class="evaluate-con" style="border:1px solid #c7c7c7;display:none;">
                        <dl id="evaluate_content">
                            <dt><i>*</i>评分：</dt>
                            <dd>
                                <span score="" class="level" style="cursor: pointer;font-size: 24px;">
                                    <span class="level1">★</span>
                                    <span class="level2">★</span>
                                    <span class="level3">★</span>
                                    <span class="level4">★</span>
                                    <span class="level5">★</span>
                                </span>
                                <em>0分</em>
                            </dd>
                            <dt><i>*</i>心得：</dt>
                            <dd>
                            <textarea class="textarea" rows="10" cols="15"
                                      name="content"></textarea>
                                <span class="title">1-500字</span>
                            </dd>
                            <dt></dt>
                            <dd class="open-btn">
                                <a href="javascript:;" sn="${trade.order.sn}"
                                   tradeId="${trade.id}" name="submit">发表评价</a>
                            </dd>
                        </dl>
                    </div>
                    <div class="logistics-info">
                        <h1>物流信息</h1>
                        <dl>
                            <dt>发货时间：</dt>
                            <dd class="time">[#if trade.shippingDate?has_content]${trade.shippingDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</dd>
                            <!--  <dt>物流公司：</dt>
                             <dd class="company">顺丰速递</dd>
                             <dt>快递单号：</dt>
                             <dd class="num">2015928155202</dd> -->
                            <dt>物流状态：</dt>
                            <dd class="zt">
                            [#if trade.shippingStatus=='unshipped']
                                未发货
                            [#elseif trade.shippingStatus=='partialShipment']
                                部分发货
                            [#elseif trade.shippingStatus=='shipped']
                                已发货
                            [#elseif trade.shippingStatus=='partialReturns']
                                部分退货
                            [#elseif trade.shippingStatus=='returned']
                                已退货
                            [#elseif trade.shippingStatus=='accept']
                                已签收
                            [#elseif trade.shippingStatus=='shippedApply']
                                退货中
                            [/#if]
                            </dd>
                            <dt></dt>
                            <dd class="th-code"></dd>
                            <!-- <dt>物流跟踪：</dt>
                            <dd class="trace"><a href="javascript:;" target="_blank">查看物流</a></dd> -->
                        </dl>
                    </div>
                    <div class="product-info-form">
                        <h1>商品信息</h1>
                        <div class="product-info-main">
                            <div class="product-info-th">
                                <div class="th th-item">
                                    <div class="td-inner">商品</div>
                                </div>
                                <div class="th th-price">
                                    <div class="td-inner">单价（元）</div>
                                </div>
                                <div class="th th-amount">
                                    <div class="td-inner">数量</div>
                                </div>
                                <div class="th th-coupon">
                                    <div class="td-inner">优惠</div>
                                </div>
                            </div>
                            <div class="product-info-list">
                                <div class="order-content">
                                    <div class="item-body clearfix">
                                        <ul class="item-content clearfix">
                                        [#list trade.orderItems as trades]
                                            <li class="td td-item">
                                                <div class="td-inner">
                                                    <div class="item-pic">
                                                        <a href="${base}/b2b/product/detail/${(trades.product.id)!}.jhtml" target="_blank">
                                                            <img src="${trades.thumbnail}">
                                                        </a>
                                                    </div>
                                                    <div class="item-info">
                                                        <a href="javascript:;">
                                                        ${trades.fullName}
                                                        </a>
                                                        <div class="type">
                                                            [#if trades.product??&&trades.product?has_content]
                                                                [#list trades.product.specification_value as spec]
                                                                ${spec}&nbsp;&nbsp;&nbsp;
                                                                [/#list]
                                                            [/#if]
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                            <li class="td td-price">
                                                <div class="td-inner">
                                                    <span>￥${trades.price}</span>
                                                </div>
                                            </li>
                                            <li class="td td-amount">
                                                <div class="td-inner">
                                                    <span>${trades.quantity}</span>
                                                </div>
                                            </li>
                                            <li class="td td-coupon">
                                                <div class="td-inner">
                                                    <div>
                                                        <span>已优惠</span>
                                                        <span><em>${trades.trade.couponDiscount}</em>元</span>
                                                    </div>

                                                </div>
                                            </li>
                                        [/#list]
                                        </ul>

                                    </div>
                                    <div class="item-checked">
                                        [#if versionType=="0"]
                                        <span>分润金额：<em>￥${trade.totalProfit}</em></span>
                                        <span>调整金额：<em>￥${trade.offsetAmount}</em></span>
                                        <span>税费：<em>￥${trade.tax}</em></span>
                                        [/#if]
                                    [#if trade.freight==0]
                                        <span>快递运费：<em class="em">包邮</em></span>
                                    [#else]
                                        <span>快递运费：<em class="em">${trade.freight}</em></span>
                                    [/#if]
                                        <!--  <span>优惠券：<em>￥</em></span> -->
                                    </div>
                                    <div class="item-total">
                                        实付款：<span>¥<em>${trade.amount}</em></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--可能感兴趣 -->
    <iframe id="interest" name="interest" src="${base}/b2b/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>

    <!--标语 -->
[#include "/b2b/include/slogen.ftl"]
</div>

<script type="text/javascript">
    $(function () {
        $(".level1").click(function () {
            $(this).parent().find("span:lt(1)").addClass("level_color");
            $(this).parent().find("span:gt(0)").removeClass("level_color");
            $(this).parent().next().text("1分");
        });
        $(".level2").click(function () {
            $(this).parent().find("span:lt(2)").addClass("level_color");
            $(this).parent().find("span:gt(1)").removeClass("level_color");
            $(this).parent().next().text("2分");
        });
        $(".level3").click(function () {
            $(this).parent().find("span:lt(3)").addClass("level_color");
            $(this).parent().find("span:gt(2)").removeClass("level_color");
            $(this).parent().next().text("3分");
        });
        $(".level4").click(function () {
            $(this).parent().find("span:lt(4)").addClass("level_color");
            $(this).parent().find("span:gt(3)").removeClass("level_color");
            $(this).parent().next().text("4分");
        });
        $(".level5").click(function () {
            $(this).parent().find("span").addClass("level_color");
            $(this).parent().next().text("5分");
        });

        $("[name='submit']").click(function () {
            var $this = $(this);
            var score = $this.parent().parent().find("span.level_color").length;
            var content = $this.parent().parent().find("[name='content']").val();
            if (score == 0) {
                alert("请选择评分！");
                return;
            }
            if (content.trim() == "") {
                alert("请输入评论内容！");
                return;
            }
            checkLock($this.attr("sn"), function () {
                var tradeId = $this.attr("tradeId");
                var assistant = score;
                var type = "positive";
                if (score == 1) {
                    type = "negative";
                } else if (score == 2 || score == 3) {
                    type = "moderate";
                } else if (score == 5 || score == 4) {
                    type = "positive";
                }
                $.ajax({
                    url: '${base}/b2b/member/review/save.jhtml',
                    data: {
                        tradeId: tradeId,
                        flag: "trade",
                        score: score,
                        assistant: assistant,
                        type: type,
                        content: content
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.type == "success") {
                            $.message("success", data.content);
                            location.href = "${base}/b2b/member/order/evaluate/list.jhtml?queryStatus=reviewed";
                        } else {
                            $.message("error", data.content);
                        }
                    }
                });
            });
        });

        if ("${trade.paymentStatus}" == "unpaid" && "[#if trade.order.paymentMethod??]${trade.order.paymentMethod.method}[/#if]" != "offline") {
            $("#guide2").prevAll().addClass("on");
        } else {
            if ("${trade.finalOrderStatus[0].status}" == "completed") {
                $("#guide5").addClass("on").prevAll().addClass("on");
            } else if ("${trade.finalOrderStatus[0].status}" == "sign") {
                $("#guide4").prevAll().addClass("on");
            } else if ("${trade.finalOrderStatus[0].status}" == "waitShipping") {
                $("#guide3").prevAll().addClass("on");
            } else if ("${trade.finalOrderStatus[0].status}" == "waitReturn") {
                if ("${trade.shippingStatus}" == "accept") {
                    if ("${isReview}" == "true") {
                        $("#guide5").addClass("on").prevAll().addClass("on");
                    } else {
                        $("#guide5").prevAll().addClass("on");
                    }
                } else {
                    $("#guide4").prevAll().addClass("on");
                }
            } else if ("${trade.finalOrderStatus[0].status}" == "cancelled") {
                $("#guide3").prevAll().addClass("on");
            } else if ("${trade.finalOrderStatus[0].status}" == "waitPay") {
                $("#guide2").prevAll().addClass("on");
            } else if ("${trade.finalOrderStatus[0].status}" == "toReview") {
                $("#guide5").prevAll().addClass("on");
            }
        }
        if ("[#if trade.order.paymentMethod??]${trade.order.paymentMethod.method}[/#if]" == "offline") {
            if ("${trade.finalOrderStatus[0].desc}" == "待签收" || "${trade.finalOrderStatus[0].desc}" == "待发货") {
                $("#guide2").removeClass("on")
            }
        }
    });
    //检查订单是否锁定
    function checkLock(sn, fn) {
        $.ajax({
            url: "${base}/app/member/order/lock.jhtml",
            data: {sn: sn},
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if (data.message.type == "success") {
                    fn();
                } else {
                    alert(data.message.content);
                }
            }
        });
    }
    function show_or_hide(obj) {
        if ($(obj).attr("flag") == "false") {
            $("#evaluate_content").parent().show();
            $(obj).attr("flag", "true")
        } else {
            $("#evaluate_content").parent().hide();
            $(obj).attr("flag", "false")
        }
    }
</script>
<!--底部 -->
[#include "/b2b/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
