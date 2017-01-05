<!DOCTYPE html>
<html lang="en">
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
    <style>
        .level_color {
            color: orange;
        }
    </style>
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
                <!--我的评价-->
                <form id="listForm" action="list.jhtml" method="get">
                    <div class="myEvaluate">
                        <div class="my-order-header">
                            <div>
                            <span class="li [#if queryStatus=="unreview"]selected[/#if]">
                                <a href="${base}/b2c/member/order/evaluate/list.jhtml?queryStatus=unreview">
                                    <span class="title">待评价订单</span>
                                    <em>${unreviewCount}</em>
                                </a>
                            </span>
                            <span class="li [#if queryStatus=="reviewed"]selected[/#if]">
                                <a href="${base}/b2c/member/order/evaluate/list.jhtml?queryStatus=reviewed">
                                    <span class="title">已评价过订单</span>
                                    <em>${reviewedCount}</em>
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
                                        [#--<input type="checkbox" disabled>--]
                                        [#--<label for=""></label>--]
                                        [#--</div>--]
                                            <div class="td">
                                                <span class="date">${trade.createDate}</span>
                                                <span>订单号：</span>
                                                <span class="">${trade.order.sn}</span>
                                                <span class="title">店铺：</span>
                                                <a class="name" href="${base}/b2c/tenant/index.jhtml?id=${trade.tenant.id}" target="_blank"
                                                   title="${trade.tenant.shortName}">${trade.tenant.shortName}</a>
                                                <span class="level">[#list 0..trade.tenant.score as t]❤[/#list]</span>
                                            </div>
                                        <span class="delete">
                                        [#--<a href="javascript:;" title="删除订单">shan</a>--]
                                        </span>
                                        </div>
                                    </div>
                                    <div class="order-list-content">
                                        <div class="item-body clearfix">
                                            [#list trade.orderItems as orderItem]
                                                [#if orderItem_index==0]
                                                    <ul class="item-content clearfix">
                                                        <li class="td-item" onclick="window.location.href='${base}/b2c/product/detail/${(orderItem.product.id)!}.jhtml'">
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
                                                        <li class="td-price">
                                                            <div class="td-inner">
                                                                <div class="price-content">
                                                                    <div class="price-line">
                                                                        <em>￥${(orderItem.product.marketPrice?string("0.00"))!}</em>
                                                                        <span>￥${(orderItem.price?string("0.00"))!}</span>
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
                                                                <em class="color">${(trade.amount?string("0.00"))!}</em>
                                                                <div>（含运费：<span>${(trade.freight?string("0.00"))!}</span>）
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li class="td-status border-right">
                                                            <div class="td-inner">
                                                                <a title=""
                                                                   href="javascript:;">${trade.finalOrderStatus[0].desc}</a>
                                                                <a title=""
                                                                   href="${base}/b2c/member/order/order_detail.jhtml?id=${trade.id}">订单详情</a>
                                                            </div>
                                                        </li>
                                                        <li class="td-op">
                                                            <div class="td-inner">
                                                                [#if queryStatus=="unreview"]
                                                                    <a title="点击评价" href="javascript:;"
                                                                       class="show-evaluate">点击评价</a>
                                                                [#else]
                                                                    <a class="check show-evaluate" title="查看评价"
                                                                       href="javascript:;">查看评价</a>
                                                                [/#if]
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
                                            <div class="evaluate-con display-n">
                                                [#if queryStatus=="unreview"]
                                                    <dl>
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
                                                [#else]
                                                    <dl>
                                                        <dt><i>*</i>评分：</dt>
                                                        <dd>
                                <span score="" class="level" style="cursor: pointer;font-size: 24px;">
                                    [#if trade.memberReview.score>0]
                                        [#list 1..trade.memberReview.score as t]
                                            <span class="level_color">★</span>
                                        [/#list]
                                        [#if trade.memberReview.score<5]
                                            [#list 1..(5-trade.memberReview.score) as t]
                                                <span>★</span>
                                            [/#list]
                                        [/#if]
                                    [/#if]
                                </span>
                                                            <em>${trade.memberReview.score!0}分</em>
                                                        </dd>
                                                        <dt><i>*</i>心得：</dt>
                                                        <dd>
                                                        <textarea class="textarea" rows="10" cols="15" name="content"
                                                                  readonly>${trade.memberReview.content}</textarea>
                                                            <span class="title">1-500字</span>
                                                        </dd>
                                                    </dl>
                                                [/#if]
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            [/#list]
                            </div>
                        </div>
                    [#include "/b2c/include/pagination.ftl"]
                    </div>
                </form>
            </div>
        </div>
    </div>
    <iframe id="interest" name="interest" src="${base}/b2c/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
[#include "/b2c/include/slogen.ftl"]
</div>

<script type="text/javascript">
    $(function () {
        $(".show-evaluate").click(function () {
            var $evaluate = $(this).parents(".order-list-item").find(".evaluate-con");
            if ($evaluate.css("display") == "none") {
                $(".evaluate-con").hide();
                $evaluate.show();
            } else {
                $evaluate.hide();
            }
        });

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
            var score = $this.parents(".order-list-item").find(".level").find("span.level_color").length;
            var content = $this.parents(".order-list-item").find("[name='content']").val();
            if (score == 0) {
                $.message("error", "请选择评分！");
                return;
            }
            if (content.trim() == "") {
                $.message("error", "请输入评论内容！");
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
                } else if (score == 5) {
                    type = "positive";
                }
                $.ajax({
                    url: '${base}/b2c/member/review/save.jhtml',
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
                        $.message(data);
                        if (data.type == "success") {
                            window.setTimeout(function () {
                                location.href = "${base}/b2c/member/order/evaluate/list.jhtml?queryStatus=reviewed";
                            }, 1000);
                        }
                    }
                });
            });
        });
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
