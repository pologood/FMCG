<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>个人中心</title>
    [#include "/wap/include/resource-2.0.ftl"/]
    <style type="text/css">
        div {
            font-family: "Microsoft YaHei"
        }

        @charset "utf-8";
        * {
            margin: 0;
            padding: 0;
            border: none;
        }

        div.userInfor {
            background-image: url(${base}/resources/wap/image/headerbg.jpg);
            width: 100%;
            height: auto;
            padding: 10px 0;
        }

        div.userInfor .headBox {
            display: block;
            width: 74px;
            height: 74px;
            margin: 0px auto;
            border: none;
            border-radius: 50%;
            -webkit-border-radius: 50%;
            -moz-border-radius: 50%;
            overflow: hidden;
            background-color: #e4e4e4;
        }

        div.userInfor span {
            color: #666666;
            font-size: 14px;
            font-color: #666666;
            display: block;
            text-align: center;
            padding: 10px 0px;
        }

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
                        <img src="${base}/resources/weixin/images/header.png" data-original="${member.headImg}"
                             class="lazy" style="width: 100%;height: 100%;">
                    </div>
                    <span>${member.username}</span>
                    <span>${member.memberRank.name}</span>
                </div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top:-1px;">
            <a href="${base}/wap/coupon/list.jhtml" class="weui_grid js_grid" data-id="button">
                <div class="weui_grid_icon">
                    <i class="iconfont font-large-2" style="color: #00aeef;">&#xe60d;</i>[#if member.couponCodes??&&member.couponCodes.size()!=0]<span
                        class="spanRadius">${member.couponCodes.size()}</span>[/#if]
                </div>
                <p class="weui_grid_label">优惠券</p>
            </a>
            <a href="${base}/wap/member/purse/index.jhtml" class="weui_grid js_grid" data-id="cell">
                <div class="weui_grid_icon">
                    <i class="iconfont font-large-2" style="color: #ff8838;">&#xe639;</i>[#if carts??&&carts!=0]<span class="spanRadius">${carts}</span>[/#if]
                </div>
                <p class="weui_grid_label">钱包</p>
            </a>
            <a href="${base}/wap/cart/list.jhtml" class="weui_grid js_grid" data-id="toast">
                <div class="weui_grid_icon">
                    <i class="iconfont font-large-2" style="color: #e76152">&#xe66c;</i>[#if carts??&&carts!=0]<span class="spanRadius">${carts}</span>[/#if]
                </div>
                <p class="weui_grid_label">购物车</p>
            </a>
        </div>

        <div class="weui_cells weui_cells_access">
            <a class="weui_cell" href="${base}/wap/member/order/list.jhtml?type=all">
                <div class="weui_cell_hd ">
                    <i class="icon iconfont font-large-1" style="color: #fcae4e">&#xe64c;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的订单</p>
                </div>
                <div class="weui_cell_ft">查看全部订单</div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top:-1px;">
            <ul data-am-widget="gallery" class="am-gallery am-gallery-default am-avg-4 am-text-center">
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unpaid">
                        <div class="am-gallery-item am-gallery-item-default light-gray">
                            <i class="icon iconfont ">&#xe662;</i>
                        [#if unpaid??&&unpaid!=0]
                            <span class="spanRadius">${unpaid}</span>
                        [/#if]
                            <p class="weui_grid_label">待支付</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unshipped">
                        <div class="am-gallery-item am-gallery-item-default light-gray">
                            <i class="icon iconfont light-gray">&#xe611;</i>
                        [#if unshipped??&&unshipped!=0]
                            <span class="spanRadius">${unshipped}</span>
                        [/#if]
                            <p class="weui_grid_label">待发货</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unreciver">
                        <div class="am-gallery-item am-gallery-item-default light-gray">
                            <i class="icon iconfont ">&#xe611;</i>
                        [#if shipped??&&shipped!=0]
                            <span class="spanRadius">${shipped}</span>
                        [/#if]
                            <p class="weui_grid_label">待收货</p>
                        </div>
                    </a>
                </li>
                <li>
                    <a href="${base}/wap/member/order/list.jhtml?type=unreview">
                        <div class="am-gallery-item am-gallery-item-default light-gray">
                            <i class="icon iconfont ">&#xe679;</i>
                        [#if unreview??&&unreview!=0]
                            <span class="spanRadius">${unreview}</span>
                        [/#if]
                            <p class="weui_grid_label">待评价</p>
                        </div>
                    </a>
                </li>
            </ul>
        </div>

        <div class="weui_cells weui_cells_access">
            <a class="weui_cell" href="${base}/wap/member/deposit/bill_list.jhtml#component">
                <div class="weui_cell_hd">
                    <i class="icon iconfont font-large-1" style="color: #fcae4e">&#xe63a;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的账单</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
            <a class="weui_cell" href="${base}/wap/member/receiver/list.jhtml">
                <div class="weui_cell_hd">
                    <i class="icon iconfont font-large-1" style="color: #4fdcff">&#xe64e;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的地址</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
            <a class="weui_cell" href="${base}/wap/social_circles/mine.jhtml">
                <div class="weui_cell_hd">
                    <i class="icon iconfont font-large-1" style="color: #ff6c4f">&#xe63d;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>我的社交</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access">
            <a class="weui_cell" href="${base}/wap/member/favorite/favoriteList.jhtml?type=tenant">
                <div class="weui_cell_hd">
                    <i class="icon iconfont font-large-1" style="color: #d466ff">&#xe67b;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>收藏的商家</p>
                </div>
                <div class="weui_cell_ft">
                </div>
            </a>
            <a class="weui_cell" href="${base}/wap/member/favorite/favoriteList.jhtml?type=product">
                <div class="weui_cell_hd">
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
    [#include "/wap/include/footer.ftl" /]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});
    });
</script>
</body>
</html>
