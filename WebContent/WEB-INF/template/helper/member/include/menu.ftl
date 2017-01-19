<script type="text/javascript" src="${base}/resources/helper/js/menuswitch.js"></script>
<script type="text/javascript">
    $().ready(function () {
        var mSwitch = new MenuSwitch("sidebar-li");
        mSwitch.setDefault();
        mSwitch.setPrevious(true);
        mSwitch.init();
    });
</script>

<input id="baseUrl" value="${base}" style="display:none;">
<div class="menuNav">

    <!-- 我的店铺 -->
    <div class="sidebar-li">
    [@helperRole url='tenant']
        <h3 class="am-accordion-title" ${helperRole.noAuthorityStype}>
            <i class="icon iconfont group">&#xe706;</i>我的店铺
        </h3>
    [/@helperRole]
        <ul>

        [@helperRole urls='helper/member/tenant/edit.jhtml;helper/member/ad/list.jhtml;helper/member/article/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    店铺管理</a>
            </li>
        [/@helperRole]

        [@helperRole url='helper/member/freight/edit.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    运费设置</a></li>
        [/@helperRole]


        [@helperRole url='helper/member/delivery_center/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    [#if versionType==0]
                        门店管理
                    [#elseif versionType==1]
                        发货地址
                    [/#if]
                </a>
            </li>
        [/@helperRole]

        [@helperRole urls='helper/member/tenant/employee/list.jhtml;helper/member/role/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    员工管理</a>
            </li>
        [/@helperRole]

        [@helperRole url='helper/member/equipment/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    购物屏管理</a>
            </li>
        [/@helperRole]
        </ul>
    </div>

    <!-- 我的订单-->
    <div class="sidebar-li">
    [@helperRole url='order']
        <h3 class="am-accordion-title"  ${helperRole.noAuthorityStype}>
            <i class="icon iconfont group">&#xe697;</i>我的订单
        </h3>
    [/@helperRole]
        <ul>

        [@helperRole url='helper/member/trade/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}?type=unshipped','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    订单管理</a>
            </li>
        [/@helperRole]

        [@helperRole url='helper/member/trade/return/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    退货管理</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/trade/order_settle_account.jhtml']
            [#if versionType==1]
                <li>
                    <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                        往来结算</a>
                </li>
            [/#if]
        [/@helperRole]
        </ul>
    </div>

    <!-- 采购管理 -->
[#if versionType==1]
    <div class="sidebar-li">
        [@helperRole url='purchase']
            <h3 class="am-accordion-title"  ${helperRole.noAuthorityStype}>
                <i class="icon iconfont group">&#xe697;</i>采购管理
            </h3>
        [/@helperRole]
        <ul>
            [@helperRole url='helper/member/purchase/list.jhtml']
                <li>
                    <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                        采购单</a>
                </li>
            [/@helperRole]

            [@helperRole url='helper/member/purchase/returns/list.jhtml']
                <li>
                    <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                        采购退货单</a>
                </li>
            [/@helperRole]
        </ul>
    </div>
[/#if]

    <!-- 数据统计 -->
    <div class="sidebar-li">
    [@helperRole url='saleDetail']
        <h3 class="am-accordion-title" ${helperRole.noAuthorityStype}>
            <i class="icon iconfont group">&#xe706;</i>数据统计
        </h3>
    [/@helperRole]
        <ul>
        [@helperRole url='helper/member/statistics/sale_detail.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    销售明细</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/statistics/sale_total.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    销售统计</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/statistics/return_detail.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    退货明细</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/statistics/return_total.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    退货统计</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/payBill/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    优惠买单统计</a>
            </li>
        [/@helperRole]
        </ul>
    </div>
    <!-- 月末结算 -->
[#if versionType==1]
    <div class="sidebar-li">
        [@helperRole url='saleDetail']
            <h3 class="am-accordion-title" ${helperRole.noAuthorityStype}>
                <i class="icon iconfont group">&#xe706;</i>月末结算
            </h3>
        [/@helperRole]
        <ul>
            [@helperRole url='helper/member/monthly/list.jhtml']
                <li>
                    <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                        月末结算</a>
                </li>
            [/@helperRole]

        </ul>
    </div>
[/#if]
    <!-- 我的商品 -->
    <div class="sidebar-li">
    [@helperRole url='product']
        <h3 class="am-accordion-title" ${helperRole.noAuthorityStype}>
            <i class="icon iconfont group">&#xe752;</i>我的商品
        </h3>
    [/@helperRole]
        <ul>

        [@helperRole url='helper/member/product_category/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    商品分类</a>
            </li>
        [/@helperRole]


        [@helperRole urls='helper/member/product/addProductCategory.jhtml;helper/member/product/isMarketableList.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    发布新品</a>
            </li>
        [/@helperRole]


        [@helperRole urls='helper/member/product/isMarketableList.jhtml;helper/member/product/notMarketablelist.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    商品列表</a>
            </li>
        [/@helperRole]


        [@helperRole url='helper/member/review/manager.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    评论管理</a>
            </li>
        [/@helperRole]
        </ul>
    </div>


    <!-- 客户管理 -->
    <div class="sidebar-li">
    [@helperRole url='consumer']
        <h3 class="am-accordion-title" ${helperRole.noAuthorityStype}>
            <i class="icon iconfont group">&#xe726;</i>客户管理
        </h3>
    [/@helperRole]
        <ul>

        [@helperRole url='helper/member/relation/parent.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    我的供应商</a></li>
        [/@helperRole]
        [@helperRole url='helper/member/relation/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    我的客户</a></li>
        [/@helperRole]
        [@helperRole urls='helper/member/consumer/list.jhtml;helper/member/consumer/nearby.jhtml;helper/member/consumer/collect_list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}?status=enable','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    我的会员</a>
            </li>
        [/@helperRole]
        [#--<li><a href="javascript:roleRoot('${base}/helper/member/consultation/manager.jhtml','owner,manager,guide,account,cashier');">咨询回复</a></li>--]
        </ul>
    </div>


    <!-- 营销工具 -->
    <div class="sidebar-li">

    [@helperRole url='marketing']
        <h3 class="am-accordion-title" ${helperRole.noAuthorityStype}>
            <i class="icon iconfont group">&#xe73a;</i>营销工具
        </h3>
    [/@helperRole]
        <ul>
        [@helperRole url='helper/member/red/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}?type=tenantBonus','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    红包</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/coupon/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}?type=tenantCoupon','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    代金券</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/promotion/list.jhtml?type=mail']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    满包邮</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/discount/list.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    限时折扣</a>
            </li>
        [/@helperRole]
        [@helperRole url='helper/member/extension/index.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    代理佣金</a>
            </li>
        [/@helperRole]

        [@helperRole urls='helper/member/buygifts/listbuygift.jhtml;helper/member/buygifts/listgift.jhtml']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    买赠搭配</a>
            </li>
        [/@helperRole]

        [@helperRole urls='helper/member/bill/discount/list.jhtml;']
            <li>
                <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                    买单折扣</a>
            </li>
        [/@helperRole]

        </ul>
    </div>

</div>

<script type="text/javascript">

    /**
     * 权限处理
     * @param urlOrFn 认证成功后跳转Url
     * @param data 模板处理后结果
     */
    function roleRoot(urlOrFn, data) {
        var hasTenant = "${Session['hasTenant']}";
        if (hasTenant != "true") {
            $.message("error", "请先申请开通店铺，才能使用此功能！");
            return;
        }


        if (data == '0') {
            $.message("error", "您没有权限进行此项操作");
        } else {
            if (typeof urlOrFn == "function") {
                urlOrFn();
            } else {
                location.href = urlOrFn;
            }
        }

    }

</script>
