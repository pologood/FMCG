<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>个人中心</title>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <style type="text/css">

        .font-large-2 {
            position: relative;
            font-size: 30px;
        }

        .am-margin-right-sm {
            margin-right: 0.5rem;
        }

        .am-gallery .weui_grid_label {
            color: #666;
        }

        .weui_cells_access .weui_cell_ft:after {
            height: 8px;
            width: 8px;
        }
    </style>
</head>

<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="page">
        <div class="weui_cells" style="margin-top:0px;">
            <a href="set_info.jhtml">
                <div class="userInfor">
                    <div class="headBox">
                        <img src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                             data-original="${(member.headImg)!}"
                             class="lazy" style="width: 100%;height: 100%;">
                    </div>
                    <span>${(member.displayName)!}</span>
                    <span>
                    [#if member??&&member.idcard??&&member.idcard.authStatus??&& member.idcard.authStatus=="success"]
                        已认证
                    [#else]
                        未认证
                    [/#if]
                    </span>
                    <span>${(member.memberRank.name)!}</span>
                </div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top:-1px;">
            <a href="${base}/wap/coupon/list.jhtml" class="weui_grid js_grid" data-id="button">
                <div class="weui_grid_icon tipicon-wrap">
                    <div class="tipicon-main myxiu_quan">
                    [#if couponCodes??&&couponCodes!=0]<p class="spanRadius"><b>${couponCodes}</b></p>[/#if]</div>
                </div>
                <p class="weui_grid_label">优惠券</p>
            </a>
            <a href="${base}/wap/member/purse/index.jhtml" class="weui_grid js_grid" data-id="cell">
                <div class="weui_grid_icon tipicon-wrap">
                    <div class="tipicon-main myxiu_wallet"></div>

                </div>
                <p class="weui_grid_label">钱包</p>
            </a>
            <a href="${base}/wap/cart/list.jhtml" class="weui_grid js_grid" data-id="toast">
                <div class="weui_grid_icon tipicon-wrap">
                    <div class="tipicon-main myxiu_shoppingcar">[#if carts??&&carts!=0]<p class="spanRadius">
                        <b>${carts}</b></p>[/#if]</div>
                </div>
                <p class="weui_grid_label">购物车</p>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top: 6px;">
            <a class="weui_cell" href="${base}/wap/member/order/list.jhtml?type=all">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #fcae4e">&#xe64c;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary ">
                    <p>我的订单</p>
                </div>
                <div class="weui_cell_ft font-small">查看全部订单</div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top:-1px;">
            <ul data-am-widget="gallery"
                class="am-gallery am-gallery-default am-avg-4 am-text-center myorder_shortcut MBIN">
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unpaid" class="orderR">
                        <div class="am-gallery-item am-gallery-item-default light-gray orderR-sym">
                            <i class="icon iconfont ">&#xe662;</i>
                        [#if unpaid??&&unpaid!=0]
                            <p class="spanRadius"><b>${unpaid}</b></p>
                        [/#if]
                        </div>
                        <p class="weui_grid_label orderR-txt">待支付</p>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unshipped" class="orderR">
                        <div class="am-gallery-item am-gallery-item-default light-gray orderR-sym">
                            <i class="icon iconfont light-gray">&#xe611;</i>
                        [#if unshiped??&&unshiped!=0]
                            <p class="spanRadius"><b>${unshiped}</b></p>
                        [/#if]
                        </div>
                        <p class="weui_grid_label orderR-txt">待发货</p>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unreciver" class="orderR">
                        <div class="am-gallery-item am-gallery-item-default light-gray orderR-sym">
                            <i class="icon iconfont " style="font-size:22px;">&#xe689;</i>
                        [#if shipped??&&shipped!=0]
                            <p class="spanRadius"><b>${shipped}</b></p>
                        [/#if]
                        </div>
                        <p class="weui_grid_label orderR-txt">待收货</p>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unreview" class="orderR">
                        <div class="am-gallery-item am-gallery-item-default light-gray orderR-sym">
                            <i class="icon iconfont ">&#xe679;</i>
                        [#if unreview??&&unreview!=0]
                            <p class="spanRadius"><b>${unreview}</b></p>
                        [/#if]
                        </div>
                        <p class="weui_grid_label orderR-txt">待评价</p>
                    </a>
                </li>
            </ul>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top: 6px;">
            <a class="weui_cell" href="${base}/wap/member/deposit/bill_list.jhtml#component">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #fcae4e">&#xe63a;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的账单</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
        [#-- 我的地址--]
            <a class="weui_cell" href="${base}/wap/member/receiver/list.jhtml">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #4fdcff">&#xe64e;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的地址</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
        [#-- 我的推广--]
            <a class="weui_cell" href="${base}/wap/member/promoting/index.jhtml">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #4fdcff">&#xe68b;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的推广</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>

            <a class="weui_cell" href="${base}/wap/member/consulting/list.jhtml">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #ff6c4f">&#xe64d;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的咨询</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
        [#if versionType==1]
            <a class="weui_cell" href="${base}/wap/social_circles/mine.jhtml">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #ff6c4f">&#xe63d;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的社交</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
        [/#if]
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top: 6px;">
            <a class="weui_cell" href="${base}/wap/member/favorite/favoriteList.jhtml?type=tenant">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #d466ff">&#xe67b;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>收藏的商家</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
            <a class="weui_cell" href="${base}/wap/member/favorite/favoriteList.jhtml?type=product">
                <div class="weui_cell_hd am-margin-right-sm">
                    <i class="icon iconfont font-large-1" style="color: #ff614f">&#xe67a;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>收藏的商品</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
        </div>
    </div>
    <div style="height: 50px;background-color: #E8EAEA;"></div>
    [#include "/wap/include/footer.ftl" /]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        //特定赋高
        fixedEleCopyHandler(".empty-for-fixedbottom_tab", ".am-topbar-fixed-bottom");
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});
    });
</script>
</body>
</html>
