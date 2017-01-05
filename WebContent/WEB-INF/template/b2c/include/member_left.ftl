<div class="left">
    <div class="personal">
        <div class="info">
            <div class="pic">
                <div class="avatar">
                [#if member.headImg??&&member.headImg?has_content]
                    <img src="${member.headImg!}">
                [#else]
                    <img src="${base}/resources/b2c/img/AccountBitmap-head.png">
                [/#if]
                </div>
            </div>
            <div class="text">
                <p class="name">
                [#if member.nickName??]${member.nickName}[/#if]
                </br>
                    <em>（${member.username}）</em>
                </p>
                <p class="level">
                    <i>
                    [#if member.score>0]
                        [#list 1..member.score as t]❤[/#list]
                    [/#if]
                    </i>
                    <span>V<em>${member.memberRank.id-1}</em></span>
                </p>
            </div>
            <div class="balance">
                余额：<span class="color" title="${member.balance?string("0.00")}">￥${member.balance?string("0.00")}</span>
            </div>
        </div>
    </div>
    <div class="menu">
        <div class="menu-item">
            <div class="li [#if menu=='order']selected[/#if]">
                <a href="${base}/b2c/member/order/list.jhtml">
                    <span>我的订单</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='wallet']selected[/#if]">
                <a href="${base}/b2c/member/wallet/index.jhtml">
                    <span>我的钱包</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='coupon']selected[/#if]">
                <a href="${base}/b2c/member/coupon_code/list.jhtml">
                    <span>我的优惠券</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='cart']selected[/#if]">
                <a href="${base}/b2c/cart/list.jhtml">
                    <span>购物车</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='favorite']selected[/#if]">
                <a href="${base}/b2c/member/favorite/list.jhtml">
                    <span>我的收藏</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='evaluate']selected[/#if]">
                <a href="${base}/b2c/member/order/evaluate/list.jhtml">
                    <span>我的评价</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='safe']selected[/#if]">
                <a href="${base}/b2c/member/safe/index.jhtml">
                    <span>账号安全</span>
                    <em>></em>
                </a>
            </div>
            <!--div class="li">
                <a href="javascript:;">
                    <span>联系我们</span>
                    <em>></em>
                </a>
            </div>
            <div class="li">
                <a href="javascript:;">
                    <span>通用设置</span>
                    <em>></em>
                </a>
            </div>
            <div class="li">
                <a href="javascript:;">
                    <span>推送设置</span>
                    <em>></em>
                </a>
            </div -->
            <div class="li [#if menu=='return']selected[/#if]">
                <a href="${base}/b2c/member/order/return/return_management.jhtml">
                    <span>退货管理</span>
                    <em>></em>
                </a>
            </div>
            <div class="li [#if menu=='message']selected[/#if]">
                <a href="${base}/b2c/member/message/list.jhtml">
                    <span>消息管理</span>
                    <em>></em>
                </a>
            </div>
        </div>
    </div>
</div>