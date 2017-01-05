<div class="f-left lf">
    <div class="log-content">
        <div class="log-cell">
            <img src="${base}/resources/b2b/images/log_1.png" alt="${setting.siteName}"/>
        </div>
        <div class="log-img">
            <img src="${(member.headImg)!}" alt=""/>
        </div>
    [#--[#if member.idcard.authStatus=='success']--]
    [#--<div class="approve">--]
    [#--<img src="${base}/resources/b2c/images/approve.png" alt="" />--]
    [#--</div>--]
    [#--[/#if]--]
        <div class="bus-title">
            <p>(${member.tenant.name!})</p>
            <p>最后登录：${member.loginDate?string("yyyy年MM月dd日 HH:mm:ss")}</p>
        </div>
    </div>
    <div class="main_left">
        <div class="sidebar_left">
            <h3 [#if menu=='order']class="active"[/#if]>
                <a href="${base}/b2b/member/supplier/index.jhtml"><i class="iconfont">&#xe601;</i>&nbsp;&nbsp;订单查询</a>
            </h3>
            <ul>
            </ul>
        </div>
        <div class="sidebar_left">
            <h3>
                <a href="javascript:;"><i class="iconfont mr_8">&#xe600;</i>数据统计</a>
            </h3>
            <ul>
                <li [#if menu=='sales_statistics']class="active"[/#if]><a
                        href="${base}/b2b/member/supplier/saleStatistics.jhtml">销售统计</a></li>
                <li [#if menu=='sale_detail']class="active"[/#if]><a
                        href="${base}/b2b/member/supplier/sale_detail.jhtml">销售明细</a></li>
                <!--<li><a href="supplier-click.html">点击量统计</a></li>-->
                <li [#if menu=='management_analyse']class="active"[/#if]><a
                        href="${base}/b2b/member/supplier/management_analyse.jhtml">经营分析</a></li>
                <!--<li class="[#if menu=='capital_flow']active[/#if]"><a
                        href="${base}/b2c/member/supplier/capital_flow.jhtml">资金流水</a></li>-->
            </ul>
        </div>
        <div class="sidebar_left">
            <h3>
                <a href="javascript:;"><i class="iconfont mr_8">&#xe600;</i>往来结算</a>
            </h3>
            <ul>
                <li [#if menu=='order_settle_account']class="active"[/#if]><a
                        href="${base}/b2b/member/supplier/order_settle_account.jhtml">订单结算</a></li>
                <li class="[#if menu=='return_settle_account']active[/#if]"><a
                        href="${base}/b2b/member/supplier/return_settle_account.jhtml">退货结算</a></li>
                <li class="[#if menu=='withdraw_cash_settle_account']active[/#if]"><a
                        href="${base}/b2b/member/supplier/withdraw_cash_settle_account.jhtml">提现结算</a></li>
            </ul>
        </div>
        <div class="sidebar_left">
            <h3 [#if menu=='my_product']class="active"[/#if]>
                <a href="${base}/b2b/member/supplier/my_product.jhtml"><i class="iconfont">&#xe000;</i>&nbsp;&nbsp;我的商品</a>
            </h3>
            <ul>
            </ul>
        </div>
    [#--<div class="sidebar_left">--]
    [#--<h3 [#if menu=='supplier_certification']class="active"[/#if]>--]
    [#--<a href="${base}/b2c/member/supplier/supplierCertification.jhtml"><i class="iconfont">&#xe602;</i>&nbsp;&nbsp;供货商认证</a>--]
    [#--</h3>--]
    [#--<ul>--]
    [#--</ul>--]
    [#--</div>--]
        <div class="sidebar_left">
            <h3 [#if menu=='account_settings']class="active"[/#if]>
                <a href="${base}/b2b/member/supplier/accountSettings.jhtml"><i class="iconfont">&#xe603;</i>&nbsp;&nbsp;账号设置</a>
            </h3>
            <ul>
            </ul>
        </div>
        <div class="sidebar_left">
            <h3 [#if menu=='returns_management']class="active"[/#if]>
                <a href="${base}/b2b/member/supplier/returnsManagement.jhtml"><i class="iconfont">&#xe603;</i>&nbsp;&nbsp;退货管理</a>
            </h3>
            <ul>
            </ul>
        </div>
        <div class="sidebar_left">
            <h3>
                <a href="javascript:;"><i class="iconfont mr_8">&#xe600;</i>采购管理</a>
            </h3>
            <ul>
                <li [#if menu=='purchase']class="active"[/#if]><a
                        href="${base}/b2b/member/supplier/purchase/list.jhtml">采购单</a></li>
                <li class="[#if menu=='purchase_returns']active[/#if] sssssss"><a
                        href="${base}/b2b/member/supplier/purchase/returns/list.jhtml">采购退货单</a></li>
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var mSwitch = new MenuSwitch("sidebar_left");
        mSwitch.setDefault(0);
    [#if menu=='sales_statistics'||menu=='sale_detail'||menu=='management_analyse'||menu=='capital_flow']
        mSwitch.setDefault(1);
    [/#if]
        mSwitch.setPrevious(true);
        mSwitch.init();
    });
</script>