<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2c/include/topnav.ftl"]
    <!-- 顶部导航结束 -->

</div>
<!--头部end-->
<!--主页内容区start-->
<div class="paper">
    <!-- 会员中心头部开始 -->
[#include "/b2c/include/member_head.ftl"]
    <!-- 会员中心头部结束 -->
    <!--会员中心-我的订单-->
    <div class="member-content">
        <div class="container">
        [#include "/b2c/include/member_left.ftl"]
            <div class="content">
                <!--退货管理-->
                <form id="listForm" action="" method="Get">
                    <input type="hidden" name="pageSize" value="3">
                    <div class="return-management [#if id??]hidden[/#if]">
                        <div class="my-order-header">
                            <div>
                        <span class="li [#if returnStatus=='']selected[/#if]">
                            <a href="return_management.jhtml">
                                <span class="title">所有退货</span>
                                <em></em>
                                <span class="wire">|</span>
                            </a>
                        </span>
                        <span class="li [#if returnStatus=='unconfirmed']selected[/#if]">
                            <a href="return_management.jhtml?returnStatus=unconfirmed">
                                <span class="title">待受理</span>
                                <em>[#if unconfirmed_size gt 0]${unconfirmed_size}[/#if]</em>
                                <span class="wire">|</span>
                            </a>
                        </span>
                        <span class="li [#if returnStatus=='confirmed']selected[/#if]">
                            <a href="return_management.jhtml?returnStatus=confirmed">
                                <span class="title">已受理</span>
                                <em>[#if confirmed_size gt 0]${confirmed_size}[/#if]</em>
                                <span class="wire">|</span>
                            </a>
                        </span>
                        <span class="li [#if returnStatus=='audited']selected[/#if]">
                            <a href="return_management.jhtml?returnStatus=audited">
                                <span class="title">已审核</span>
                                <em>[#if completed_size gt 0]${audited_size}[/#if]</em>
                                <span class="wire">|</span>
                            </a>
                        </span>
                        <span class="li [#if returnStatus=='completed']selected[/#if]">
                            <a href="return_management.jhtml?returnStatus=completed">
                                <span class="title">已完成</span>
                                <em>[#if completed_size gt 0]${completed_size}[/#if]</em>
                                <span class="wire">|</span>
                            </a>
                        </span>
                        <span class="li [#if returnStatus=='cancelled']selected[/#if]">
                            <a href="return_management.jhtml?returnStatus=cancelled">
                                <span class="title">已取消</span>
                                <em>[#if cancelled_size gt 0]${cancelled_size}[/#if]</em>
                                <span class="wire">|</span>
                            </a>
                        </span>
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
                                    <div class="th th-buynum">
                                        <div class="td-inner">购买数量</div>
                                    </div>
                                    <div class="th th-refundnum">
                                        <div class="td-inner">退货数量</div>
                                    </div>
                                    <div class="th th-status">
                                        <div class="td-inner">交易状态</div>
                                    </div>
                                </div>
                            </div>
                            <div class="my-order-page"></div>
                            <div class="my-order-list">
                            [#list page.content as page]
                                <div class="order-list-item">
                                    <div class="order-list-info">
                                        <div class="tr">
                                            <!-- <div class="checkbox ">
                                                <input type="checkbox"  disabled>
                                                <label for=""></label>
                                            </div> -->
                                            <div class="td">
                                                <span class="date">${page.createDate}</span>
                                                <span>订单号：</span>
                                                <span class="">${page.trade.order.sn}</span>
                                                <span class="title">店铺：</span>
                                                <a class="name" href="${base}/b2c/tenant/index.jhtml?id=${page.trade.tenant.id}" target="_blank"
                                                   title="${page.trade.tenant.name}">${page.trade.tenant.name}</a>
                                                [#if page.trade.tenant.score>0]
                                                    [#list 1..page.trade.tenant.score as s]
                                                        <span class="level">❤</span>
                                                    [/#list]
                                                [/#if]

                                            </div>
                                            <!-- <span class="delete" onclick="delete_return(er)">
                                                <a href="javascript:;" title="删除订单">shan</a>
                                            </span> -->
                                        </div>
                                    </div>
                                    [#list page.returnsItems as returnitems]
                                        [#if returnitems?has_content]
                                        <div class="order-list-content">
                                            <div class="item-body clearfix">
                                                <ul class="item-content clearfix">
                                                    <li class="td-item" onclick="window.location.href='${base}/b2c/product/detail/${(returnitems.orderItem.product.id)!}.jhtml'">
                                                        <div class="td-inner">
                                                            <div class="item-pic">
                                                                <a href="javascript:;" target="_blank">
                                                                    <img src="${returnitems.orderItem.thumbnail}"
                                                                         style="width:82px;height:82px;">
                                                                </a>
                                                            </div>
                                                            <div class="item-info">
                                                                <div class="item-basic-info">
                                                                    <a href="javascript:;" title="" class="item-title">
                                                                    ${returnitems.name}
                                                                    </a>
                                                                </div>
                                                                <div class="item-other-info">
                                                                    <div class="promo-logos"></div>
                                                                    <div class="item-props clearfix">
                                                                        [#if returnitems.orderItem.product??&&returnitems.orderItem.product?has_content]
                                                                            [#if returnitems.orderItem.product.specification_value??&&returnitems.orderItem.product.specification_value?has_content]
                                                                                [#list returnitems.orderItem.product.specification_value as spec]
                                                                                    <span>${spec}</span>
                                                                                [/#list]
                                                                            [/#if]
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
                                                                    <span>￥${returnitems.orderItem.price}</span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                    <li class="td-buynum">
                                                        <div class="td-inner">
                                                            <span>${returnitems.shippedQuantity}</span>
                                                        </div>
                                                    </li>
                                                    <li class="td-refundnum">
                                                        <div class="td-inner">
                                                            <span>${returnitems.returnQuantity}</span>
                                                        </div>
                                                    </li>
                                                    <li class="td-status">
                                                        <div class="td-inner">
                                                            [#if page.returnStatus=='unconfirmed']
                                                                <span>待受理</span>
                                                            [#elseif page.returnStatus=='confirmed']
                                                                <span>已受理</span>
                                                            [#elseif page.returnStatus=='audited']
                                                                <span>已认证</span>
                                                            [#elseif page.returnStatus=='completed']
                                                                <span>已完成</span>
                                                            [#elseif page.returnStatus=='cancelled']
                                                                [#if page.memo?has_content]
                                                                    <span>已拒绝</span>
                                                                    <a href="javascript:;"
                                                                       onclick="look_return_reason(this,${page.id})"
                                                                       flag="true">查看理由</a>
                                                                [#else]
                                                                    <span>已取消</span>
                                                                [/#if]
                                                            [/#if]
                                                            <!-- <a style="" title="" href="javascript:;">拒绝退货</a> -->
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                        [/#if]
                                    [/#list]
                                    <!-- <div class="unfold">
                                        <span>展开</span>
                                        <i>▲</i>
                                        <i class="hidden">▼</i>
                                    </div> -->
                                    [#if page.memo?has_content&&page.returnStatus=="cancelled"]
                                        <div class="order-list-footer"
                                             style="display:none;border-bottom:1px solid #eeeeee;" id="${page.id}">
                                            <dl style="margin-left:0px;">
                                                <dt>拒绝理由：</dt>
                                                <dd>${page.memo}</dd>
                                            </dl>
                                        </div>
                                    [/#if]
                                    <div class="order-list-footer">
                                        <dl>
                                            <dt>退货总数：</dt>
                                            <dd>${page.quantity}件</dd>
                                            <dt>退货价格：</dt>
                                            <dd>￥${page.amount}</dd>
                                            [#if page.returnStatus=='unconfirmed']
                                                <!-- <dt style="float:right;color:#fb4004;"
                                                    onclick="cancel_return(${page.id})">
                                                    <a>取消退货</a>
                                                </dt> -->
                                            [/#if]
                                        </dl>
                                    </div>
                                </div>
                            [/#list]
                            </div>
                        </div>
                        <div class="my-order-footer clearfix">
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/b2c/include/pagination.ftl"]
                    [/@pagination]
                        </div>
                    </div>
                </form>
                <!--退货申请-->
                <div class="return-request [#if id==null]hidden[/#if]">
                [#if return_trade??]
                    <div class="my-order-main">
                        <div class="my-order-th">
                            <div>
                                <div class="th th-item">
                                    <div class="td-inner">宝贝</div>
                                </div>
                                <div class="th th-price">
                                    <div class="td-inner">单价（元）</div>
                                </div>
                                <div class="th th-buynum">
                                    <div class="td-inner">购买数量</div>
                                </div>
                                <div class="th th-refundnum">
                                    <div class="td-inner">退货数量</div>
                                </div>
                            </div>
                        </div>
                        <div class="my-order-page"></div>
                        <div class="my-order-list">

                            <div class="order-list-item">

                                <div class="order-list-info">
                                    <div class="tr">
                                        <div class="checkbox ">
                                            <input type="checkbox" name="tradeId" tradeId="${return_trade.id}"
                                                   onchange="check_return(this)"
                                                   style="position:absolute;left:-3px;top:-2px;">
                                            <!-- <label for=""></label> -->
                                        </div>
                                        <div class="td">
                                            <span class="date">${return_trade.createDate}</span>
                                            <span>订单号：</span>
                                            <span class="">${return_trade.order.sn}</span>
                                            <span class="title">店铺：</span>
                                            <a class="name" href="javascript:;"
                                               title="${return_trade.tenant.name}">${return_trade.tenant.name}</a>
                                            [#if return_trade.tenant.score>0]
                                                [#list 1..return_trade.tenant.score as s]
                                                    <span class="level">❤</span>
                                                [/#list]
                                            [/#if]
                                        </div>
                                        <!-- <span class="delete">
                                            <a href="javascript:;" title="删除订单">shan</a>
                                        </span>     -->
                                    </div>
                                </div>
                                <div class="order-list-content">
                                    <div class="item-body clearfix" id="item_body">
                                        [#list return_trade.orderItems as orderitems]
                                            <ul class="item-content clearfix">
                                                <li class="td-item" >
                                                    <div class="checkbox ">
                                                        <input type="checkbox" name="productId"
                                                               price="${orderitems.price}"
                                                               quantity="${orderitems.quantity}"
                                                               pname="${orderitems.name}"
                                                               shipq="${return_trade.order.shippedQuantity}"
                                                               returnq="${orderitems.returnQuantity}"
                                                               sn="${orderitems.sn}"
                                                               onchange="check_return(this)"
                                                               style="position:absolute;left:-1px;top:-3px;">
                                                        <!-- <label for=""></label> -->
                                                    </div>
                                                    <div class="td-inner">
                                                        <div class="item-pic">
                                                            <a href="javascript:;" target="_blank">
                                                                <img src="${orderitems.thumbnail}"
                                                                     style="width:82px;height:82px;">
                                                            </a>
                                                        </div>
                                                        <div class="item-info">
                                                            <div class="item-basic-info">
                                                                <a href="javascript:;" title="${orderitems.fullName}"
                                                                   class="item-title">
                                                                ${orderitems.fullName}
                                                                </a>
                                                            </div>
                                                            <div class="item-other-info">
                                                                <div class="promo-logos"></div>
                                                                <div class="item-props clearfix">
                                                                    [#if orderitems.product??&&orderitems.product?has_content]
                                                                        [#if orderitems.product.specification_value??&&orderitems.product.specification_value?has_content]
                                                                            [#list orderitems.product.specification_value as spec]
                                                                                <span>${spec}</span>
                                                                            [/#list]
                                                                        [/#if]
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
                                                                <span>￥${orderitems.price}</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li class="td-buynum">
                                                    <div class="td-inner">
                                                        <span>${orderitems.quantity}</span>
                                                    </div>
                                                </li>
                                                <li class="td-refundnum">
                                                    <div class="td-inner">
                                                        <div class="">
                                                            <div class="item-amount ">
                                                                <a href="javascript:;" class="minus"
                                                                   onclick="set_number(${orderitems.quantity},this)"
                                                                   id="minus">-</a>
                                                                <input value="${orderitems.quantity}"
                                                                       class="text text-amount" type="text"
                                                                       onkeyup="get_num(this,${orderitems.quantity})"
                                                                       name="return_num">
                                                                <a href="javascript:;" class="plus"
                                                                   onclick="set_number(${orderitems.quantity},this)"
                                                                   id="plus">+</a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                        [/#list]
                                    </div>
                                </div>
                            </div>
                            <div class="order-list-footer">
                                <dl>
                                    <dt>退货总数：</dt>
                                    <dd id="quantity">0件</dd>
                                    <dt>退货价格：</dt>
                                    <dd id="amount">￥0</dd>
                                </dl>
                            </div>
                            <div class="order-list-btn">
                                <a href="javascript:confirm_return();">确认退货</a>
                            </div>
                        </div>
                    </div>
                [/#if]
                </div>
            </div>
        </div>
    </div>
    <!--我的收藏-->
    <!--start-->
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>

    <!--end-->

    <!--标语start-->
[#include "/b2c/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->
<script type="text/javascript">
    //初始化
    var size = $(".shopkeeper-hot-c ul").size();//.shopkeeper-hot-c ul
    for (var i = 1; i <= size; i++) {
        var li = "<li>" + i + "</li>";
        $(".pagination").append(li);//
    }
    $(function () {
        //手动控制轮播
        $(".shopkeeper-hot-c ul").eq(0).show();
        $(".shopkeeper-hot .pagination li").eq(0).addClass("active");
        $(".shopkeeper-hot .pagination li").mouseover(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var index = $(this).index();
            i = index;
            //alert(index)
            $(".shopkeeper-hot-c ul").eq(index).stop().fadeIn(300).siblings().stop().fadeOut(300);
        })

        //自动轮播
        var i = 0;
        var t = setInterval(move, 3000);
        //核心向左运动函数
        function moveL() {
            i--;
            if (i == -1) {
                i = size - 1;
            }
            //alert(i);
            $(".shopkeeper-hot .pagination li").eq(i).addClass("active").siblings().removeClass("active");
            $(".shopkeeper-hot-c ul").eq(i).fadeIn(300).siblings().fadeOut(300);
        }

        //核心向右运动函数
        function move() {
            i++;
            if (i == size) {
                i = 0;
            }
            //alert(i);
            $(".shopkeeper-hot .pagination li").eq(i).addClass("active").siblings().removeClass("active");
            $(".shopkeeper-hot-c ul").eq(i).fadeIn(300).siblings().fadeOut(300);
        }

        //左边按钮点击事件
        $(".shopkeeper-hot .shopkeeper-hot-l").click(function () {//.shopkeeper-hot .shopkeeper-hot-l
            //alert(1234);
            moveL();
        })
        //右边按钮点击事件
        $(".shopkeeper-hot .shopkeeper-hot-r").click(function () {//.shopkeeper-hot .shopkeeper-hot-r
            //alert(5234);
            move();
        })

        //定时器的开始与结束
        $(".shopkeeper-hot").hover(function () {//.shopkeeper-hot
            clearInterval(t);
        }, function () {
            t = setInterval(move, 2000);
        })

        check_return_content();
    })
    //设置退货数量
    function set_number(num, obj) {
        if (num == 1) {
            return;
        } else if (num > 1) {
            if ($(obj).attr("id") == "minus") {
                var prevObj = $(obj).next().val();
                if (prevObj == 1) {
                    return;
                }
                prevObj--;
                $(obj).next().val(prevObj)
            } else if ($(obj).attr("id") == "plus") {
                var nextObj = $(obj).prev().val();
                if (nextObj == num) {
                    return;
                }
                nextObj++;
                $(obj).prev().val(nextObj);
            }
        }
        check_return_content();
    }
    //监听商品数量
    function get_num(obj, num) {
        if ($(obj).val() > num || $(obj).val() > 0) {
            $(obj).val(num);
            $.message("warn", "退货数量不能超过购买数量");
        }
    }
    //控制checkbox选择
    function check_return(obj) {
        var check_product = true;
        if ($(obj).attr("name") == "tradeId") {
            if ($(obj).prop("checked")) {
                $("input[name='productId']").prop("checked", true);
            } else {
                $("input[name='productId']").prop("checked", false);
            }
        } else if ($(obj).attr("name") == "productId") {
            if ($(obj).prop("checked") == false) {
                $("input[name='tradeId']").prop("checked", false);
            } else {
                $.each($("input[name='productId']"), function () {
                    if ($(this).prop("checked") == false) {
                        check_product = false;
                        return false;
                    }
                });
                if (check_product) {
                    $("input[name='tradeId']").prop("checked", true);
                } else {
                    $("input[name='tradeId']").prop("checked", false);
                }
            }
        }
        check_return_content();
    }
    //实时改变退货数量和金额
    function check_return_content() {
        var subQuantity = 0;
        var subAmount = 0;
        $.each($("input[name='productId']"), function () {
            if ($(this).prop("checked")) {
                var qn = $(this).parent().parent().parent().find(".text-amount").val()
                subQuantity = accAdd(subQuantity, Number(qn));
                subAmount = accAdd(subAmount, accMul(Number($(this).attr("price")), Number(qn)));
            }
        });
        $("#quantity").text(subQuantity);
        $("#amount").text(subAmount);
    }
    //加法精度运算
    function accAdd(arg1, arg2) {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        return (arg1 * m + arg2 * m) / m
    }
    //乘法精度运算
    function accMul(arg1, arg2) {
        var m = 0, s1 = arg1.toString();
        var s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }
    //确认退货
    function confirm_return() {
        var returnitem = new Array();
        var amount = $("#amount").text();
        var flag = true;
        $.each($("input[name='productId']"), function () {
            if ($(this).prop("checked")) {
                flag = false;
                var return_price = $(this).attr("price");
                var return_sn = $(this).attr("sn");
                var shippedQuantity = $(this).attr("shipq");
                var pname = $(this).attr("pname");
                var quanti = $(this).parent().parent().parent().find(".text-amount").val();
                returnitem.push({
                    sn: return_sn, quantity: quanti, amount: amount, shippedQuantity: shippedQuantity,
                    pname: pname, id: $("input[name='tradeId']").attr("tradeId")
                });
            }
        });
        if (flag) {
            alert("请选择商品");
            return;
        }
        $.ajax({
            url: '${base}/b2c/member/order/create_return.jhtml',
            data: JSON.stringify(returnitem),
            type: 'post',
            dataType: 'json',
            contentType: 'application/json;charset=utf-8',
            success: function (data) {
                $.message(data.type, data.content);
                if (data.type == "success") {
                    location.href = "${base}/b2c/member/order/return/return_management.jhtml";
                }
            }
        });
    }
    //取消退货
    function cancel_return(id) {
        if (confirm("确认要取消该退货吗")) {
            $.ajax({
                url: "${base}/b2c/member/order/cancel_return.jhtml",
                type: "post",
                data: {id: id},
                dataType: "json",
                success: function (data) {
                    if (data.type == "success") {
                        location.reload();
                    } else {
                        $.message(data.type, data.content)
                    }
                }
            });
        }
    }
    function look_return_reason(obj, id) {
        if ($(obj).attr("flag") == "true") {
            $("#" + id).show();
            $(obj).attr("flag", "false")
        } else {
            $("#" + id).hide();
            $(obj).attr("flag", "true")
        }
    }
</script>
<!--底部start-->
[#include "/b2c/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>
<!--右侧悬浮框end-->
</body>
</html>
