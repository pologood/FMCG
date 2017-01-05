[#if notifyMessage??]
${notifyMessage}
[#else]
<!DOCTYPE html>
<html lang="en">
<head>
    <title>支付</title>
    [#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/main.css">
    <script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/iscroll/iscroll.js"></script>
</head>
<body class="bg-05" ontouchstart>
    [#include "/wap/include/static_resource.ftl"]

<div class="container NF">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page NF">
        <div class="weui_msg">
            <div class="payment_top">
                <div class="weui_icon_area">
                    <i class="[#if payment.status == "success"]weui_icon_success[#else]weui_icon_warn[/#if] weui_icon_msg"></i>
                </div>
                <div class="weui_text_area">
                    [#if payment.status == "success"]
                        <p class="weui_msg_desc">支付成功</p>
                        <h2 class="clr-grey01 ft-bs4 weui_msg_title" style="font-size: 36px;color: #09bb07">
                            ￥${payment.amount?string("0.00")}</h2>
                    [#elseif payment.status == "wait"]
                        <h2 class="clr-grey01 ft-bs4 weui_msg_title">支付取消</h2>
                        <p class="weui_msg_desc">用户遇到错误或者主动放弃</p>
                    [#elseif payment.status == "failure"]
                        <h2 class="clr-grey01 ft-bs4 weui_msg_title">支付失败</h2>
                        <p class="weui_msg_desc">用户遇到错误或者主动放弃</p>
                    [/#if]
                </div>
                [#assign  _tenant='']
                [#if payment.status == "success"]
                    <div class="payment_details">
                        <div class="payment_details-container ft-bs05">
                            <div class="clr-grey14 pd_text_left">
                                成功支付:
                            </div>
                            <div class="clr-grey15 pd_text_right">
                                &nbsp;&nbsp;￥${payment.amount}
                            </div>
                            <div class="clr-grey14 pd_text_left">
                                流水编号:
                            </div>
                            <div class="clr-grey15 pd_text_right">
                                &nbsp;&nbsp;${payment.sn}
                            </div>
                            <div class="clr-grey14 pd_text_left">
                                收款商家:
                            </div>
                            <div class="clr-grey15 pd_text_right">
                                &nbsp;&nbsp;${abbreviate(payment.tenantName,16,"...")}
                            </div>
                            <div class="clr-grey14 pd_text_left">
                                支付时间:
                            </div>
                            <div class="clr-grey15 pd_text_right">
                                &nbsp;&nbsp;${.now?string("yyyy-MM-dd HH:mm:ss")}
                            </div>
                            [#if payment.payBill??&&payment.payBill.backDiscount!=0]
                                <div class="clr-grey14 pd_text_left">
                                    &nbsp;&nbsp;返现金额:
                                </div>
                                <div class="clr-grey15 pd_text_right">
                                    &nbsp;&nbsp;￥${payment.payBill.backDiscount}
                                    <a href="${base}/wap/member/purse/index.jhtml" class="wallet clr-red05">查看钱包</a>
                                </div>
                            [/#if]
                        </div>

                    </div>
                [/#if]

                <div class="weui_opr_area btn_total">
                    <p class="weui_btn_area">
                        [#if payment.type == "payment"]
                            [#if payment.status=="success"]
                                <a href="${base}/wap/member/order/list.jhtml" class="weui_btn weui_btn_primary">确定</a>
                            [#else]
                                <a href="${base}/wap/member/order/list.jhtml" class="weui_btn weui_btn_warn">确认</a>
                            [/#if]
                        [#else]
                            <a href="${base}/wap/member/index.jhtml"
                               class="weui_btn weui_btn_plain_primary ft-bs15 return">返回</a>
                        [/#if]
                    </p>
                </div>

                [#if payment.status == "success"]
                    [#if payment.payBill??&&payment.payBill.backDiscount!=0]
                        <div class="cashback">
                            <img src="${base}/resources/wap/3.0/image/cashback_bg.png" alt="">
                            <p class="clr-red08 ft-bs05">已返现<span class="ft-bs35">${payment.payBill.backDiscount}</span>元，赶紧去花掉吧！
                            </p>
                        </div>
                    [#else ]
                        [#if tenants??&&tenants?has_content]
                            <div class="cashback">
                                <img src="${base}/resources/wap/3.0/image/cashback_bg.png" alt="">
                                <p class="clr-red08 ft-bs05">更多优惠商家，赶紧去逛逛！</p>
                            </div>
                        [/#if]
                    [/#if]
                [/#if]
                [#if tenants??&&tenants?has_content]
                    <div class="items_tit ft-bs15 clr-grey01">
                        付款的用户都在看
                    </div>
                [/#if]
            </div>
            <div class="payment_bottom" id="wrapper">
                <div class="items NF" id="nearByTenant ">

                    <div class="items_box" id="scroller">

                        [#list tenants as tenant]
                            <a class="weui_cells box_tenant col-1"
                               href="${base}/wap/tenant/index/${tenant.id}.jhtml?extension=${_tenant}">
                            [#-- box_tenant-info--]
                                <div class="weui_cell box_tenant-info">
                                    <div class="weui_cell_hd">
                                        <img class="lazy"
                                             src="${tenant.thumbnail}"
                                             data-original="${tenant.thumbnail}">
                                    </div>
                                    <div class="weui_cell_bd weui_cell_primary btm_border">
                                        <p class="storename ft-bs15">
                                            <span>${tenant.shortName}</span>
                                            <i class="iconfont clr-grey11 ft-bs2">&#xe635;</i>
                                        </p>
                                    [#-- 评分等级--]
                                        [#if tenant.grade > 0]
                                            <div class="starlevelsR" style="text-align: left">
                                                <div class="cf ft-bs0 starlevels" data-starnum="{{grade}}"
                                                     data-starlevelized="false">
                                                    <i></i>
                                                    <i></i>
                                                    <i></i>
                                                    <i></i>
                                                    <i></i>
                                                </div>
                                                [#if tenant.grade??&&tenant.grade?has_content]
                                                    <span class="ft-bs0 clr-red05">${tenant.grade}分</span>
                                                [/#if]
                                            </div>
                                        [/#if]
                                        <div class="weui_cell mainAaddress">
                                            <div class="weui_cell_hd ft-bs0 mainbusiness">
                                                主营：${tenant.tenantCategoryName}
                                            </div>
                                            <div class="weui_cell_bd weui_cell_primary ft-bs0 address">
                                            [#--${(tenant.distance/1000)?string("0.00")}km--]
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            [#-- box_tenant-promotions--]
                                [#if tenant.promotions??&&tenant.promotions?has_content]
                                    <div class="weui_cell box_tenant-promotions">
                                        <div class="weui_cell_hd">
                                        </div>
                                        <div class="weui_cell_bd weui_cell_primary ThumbIconLeft activity">
                                            [#list tenant.promotions as promotion]
                                                <p class="promdesc">
                                                    [#if promotion.type]
                                                        <i class="iconfont ft-bs05 promtag pt-${promotion.type}"></i>
                                                    [/#if]
                                                    <span class="ft-bs0 clr-grey03">${promotion.name}.</span>
                                                </p>
                                            [/#list]
                                        </div>
                                    </div>
                                [/#if]
                            </a>
                        [/#list]
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
        });
    });
</script>
<script>
    //底部滚动高度固定
    $(function () {
        var viewHeight = window.innerHeight
                || document.documentElement.clientHeight;
        var topHeight = $(".payment_top").height();
        var bottomHeight = viewHeight - topHeight;
        document.getElementById("wrapper").style.height = bottomHeight + "px";
    })
</script>
<script>
    //iphone 不能滚动解决方案 使用iscroll.js插件 此处为页面调用
    var myScroll;
    function loaded() {
        myScroll = new iScroll('wrapper', {scrollbarClass: 'myScrollbar'});
    }
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    document.addEventListener('DOMContentLoaded', loaded, false);
</script>
</body>
</html>
[/#if]