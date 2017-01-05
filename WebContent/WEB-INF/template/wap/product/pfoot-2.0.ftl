[#-- 商品详情页底部操作面板--]
<div class="opera_panel PT-DY" data-emptyto="opera_panel">
    <div class="bg-05 opera_panel-L">
        [#-- 联系客服--]
        <a href="javascript:;" class="contact">
            <i class="iconfont ft-bs1 clr-grey01">&#xe66f;</i>
            <span class="clr-grey02 ft-bs0">客服</span>
        </a>
        [#-- 收藏--]
        <a href="javascript:;" class="favor">
            <i class="iconfont ft-bs1 clr-grey01">&#xe62c;</i>
            <span class="clr-grey02 ft-bs0">收藏</span>
        </a>
        [#-- 购物车--]
        <a href="${base}/wap/cart/list.jhtml" class="cart">
            [#-- <i class="iconfont ft-bs1 clr-grey01">&#xe66c;</i>--]
            <div>
                <i class="iconfont ft-bs1 clr-grey01">&#xe66c;</i>
                <u class="cornernum ft-bs0 [#if cartcount == 0]none[/#if]"><b>${cartcount}</b></u>
            </div>
            <span class="clr-grey02 ft-bs0">购物车</span>
            [#-- 暂时不展现购物车中商品数量--]
        </a>
    </div>
    <div class="opera_panel-R1">
        <a href="javascript:;" class="ft-bs15 clr-grey08 addtocart">加入购物车</a>    
    </div>
    <div class="opera_panel-R2 bg-07">
        <a href="javascript:;" class="ft-bs15 clr-grey08  buynow">立即购买</a>    
    </div>
</div>