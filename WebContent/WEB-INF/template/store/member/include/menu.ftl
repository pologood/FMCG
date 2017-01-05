<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image" onclick="javascript:;'">
                <img src="[#if owner??]${owner.tenant.logo}[#else]${base}/resources/store/images/head_img01.gif[/#if]"
                     class="img-circle" alt="User Image" onclick="location.href='${base}/store/member/safe/index.jhtml;'">
            </div>
            <div class="pull-left info">
                <p title="[#if owner??]${owner.tenant.linkman}[#else][/#if]" onclick="location.href='${base}/store/member/safe/index.jhtml;'">
                [#if owner??] <a href="javascript:;" id="chagePassword">${owner.tenant.linkman}</a>[#else][/#if]
                </p>
                <img src="${base}/resources/store/img/level_xin_0[#if owner??]${owner.tenant.score}[#else]0[/#if].png" alt="">
            </div>
        </div>
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="header"></li>
            <li class="[#if menu=='tenant_management'||menu=='tenant_freight'||menu=='tenant_delivery'||
            menu=='tenant_equiment'||menu=='tenant_employee']active[/#if] treeview">
                [@helperRole url='tenant']
                <a href="javascript:;">
                    <i class="fa fa-dashboard"></i><span>我的店铺</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                [/@helperRole]

                <ul class="treeview-menu">
                [@helperRole urls='store/member/tenant/edit.jhtml;store/member/ad/list.jhtml;store/member/article/list.jhtml']
                    <li [#if menu=="tenant_management"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>店铺管理</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/freight/edit.jhtml']
                    <li [#if menu=="tenant_freight"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>运费设置</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/delivery_center/list.jhtml']
                    <li [#if menu=="tenant_delivery"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>[#if versionType==0]门店管理[#elseif versionType==1]发货地址[/#if]
                        </a>
                    </li>
                [/@helperRole]

                [@helperRole urls='store/member/tenant/employee/list.jhtml;store/member/role/list.jhtml']
                    <li [#if menu=="tenant_employee"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>员工管理</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/equipment/list.jhtml']
                    <li [#if menu=="tenant_equiment"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>购物屏管理</a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
            [#if versionType==0]
            <li class="[#if menu=='union'||menu=='my_union'||menu=='all_union'||menu=='my_device'||menu=='my_extend']active[/#if] treeview">
                [@helperRole url='union']
                <a href="javascript:;">
                    <i class="fa fa-dashboard"></i><span>商盟管理</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                [/@helperRole]

                <ul class="treeview-menu">
                [@helperRole urls='store/member/union/my_union.jhtml']
                    <li [#if menu=="my_union"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>商家联盟</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/union/my_device.jhtml']
                    <li [#if menu=="my_device"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>购物屏联盟</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/union/my_product.jhtml']
                    <li [#if menu=="my_extend"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>我的推广</a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
            [/#if]
            <li class="[#if menu=='order_management'||menu=='return_management'||menu=='settle_account']active[/#if] treeview">
            [@helperRole url='order']
                <a href="#">
                    <i class="fa fa-files-o"></i><span>我的订单</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
            [/@helperRole]

                <ul class="treeview-menu">
                [@helperRole url='store/member/trade/list.jhtml']
                    <li [#if menu=="order_management"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}?type=unshipped','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>订单管理</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/trade/return/list.jhtml']
                    <li [#if menu=="return_management"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>退货管理</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/trade/order_settle_account.jhtml']
                    <li [#if menu=="settle_account"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>往来结算</a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
        [#if versionType==1]
            <li class="[#if menu=='purchase'||menu=='purchase_return']active[/#if] treeview">
                [@helperRole url='purchase']
                    <a href="#"><i class="fa fa-pie-chart"></i><span>采购管理</span>
                        <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                    </a>
                [/@helperRole]

                <ul class="treeview-menu">
                    [@helperRole url='store/member/purchase/list.jhtml']
                        <li [#if menu=="purchase"]class="active"[/#if]>
                            <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                    class="fa fa-circle-o"></i>采购单</a>
                        </li>
                    [/@helperRole]

                    [@helperRole url='store/member/purchase/returns/list.jhtml']
                        <li [#if menu=="purchase_return"]class="active"[/#if]>
                            <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                    class="fa fa-circle-o"></i>采购退货单</a>
                        </li>
                    [/@helperRole]
                </ul>
            </li>
            <li class="[#if menu=='monthly']active[/#if] treeview">
                [@helperRole url='monthly']
                    <a href="#"><i class="fa fa-pie-chart"></i><span>月末结算</span>
                        <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                    </a>
                [/@helperRole]

                <ul class="treeview-menu">
                    [@helperRole url='store/member/monthly/monthly_list.jhtml']
                        <li [#if menu=="monthly"]class="active"[/#if]>
                            <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                            <i class="fa fa-circle-o"></i>月末结算</a>
                        </li>
                    [/@helperRole]
                </ul>
            </li>
        [/#if]
        
            <li class="[#if menu=='sale_detail'||menu=='sale_total'||menu=='product_total'||menu=='return_detail'||menu=='return_total'||menu=='return_product_total'||menu=='discount_total']active[/#if] treeview">
            [@helperRole url='saleDetail']
                <a href="#"><i class="fa fa-pie-chart"></i><span>数据统计</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
            [/@helperRole]
                <ul class="treeview-menu">
                [@helperRole url='store/member/statistics/sale_detail.jhtml']
                    <li [#if menu=="sale_detail"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>销售明细</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/statistics/sale_total.jhtml']
                    <li [#if menu=="sale_total"||menu=="product_total"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>销售统计</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/statistics/return_detail.jhtml']
                    <li [#if menu=="return_detail"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>退货明细</a>
                    </li>
                [/@helperRole]
                [@helperRole url='store/member/statistics/return_total.jhtml']
                    <li [#if menu=="return_total"||menu=="return_product_total"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>退货统计</a>
                    </li>
                [/@helperRole]
                [@helperRole url='store/member/payBill/discount_total.jhtml']
                    <li [#if menu=="discount_total"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>优惠买单统计</a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
   
            <li class="[#if menu=='product_list'||menu=='product_category'||menu=='add_product'||
            menu=='review_management']active[/#if] treeview">
            [@helperRole url='product']
                <a href="#">
                    <i class="fa fa-edit"></i><span>我的商品</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
            [/@helperRole]

                <ul class="treeview-menu">
                [@helperRole url='store/member/product_category/list.jhtml']
                    <li [#if menu=="product_category"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>商品分类</a>
                    </li>
                [/@helperRole]

                [@helperRole urls='store/member/product/addProductCategory.jhtml;store/member/product/isMarketableList.jhtml']
                    <li [#if menu=="add_product"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>发布新品</a>
                    </li>
                [/@helperRole]

                [@helperRole urls='store/member/product/isMarketableList.jhtml;store/member/product/notMarketablelist.jhtml']
                    <li [#if menu=="product_list"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>商品列表</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/review/manager.jhtml']
                    <li [#if menu=="review_management"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>评论管理</a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
            <li class="[#if menu=='my_supplier'||menu=='my_user'||menu=='my_employee']active[/#if] treeview">
                <a href="#">
                    <i class="fa fa-folder"></i><span>客户管理</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
                <ul class="treeview-menu">
                [@helperRole url='store/member/relation/parent.jhtml']
                    <li [#if menu=="my_supplier"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>我的供应商</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/relation/list.jhtml']
                    <li [#if menu=="my_user"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>我的客户</a>
                    </li>
                [/@helperRole]

                [@helperRole urls='store/member/consumer/list.jhtml?status=enable;store/member/consumer/nearby.jhtml;store/member/consumer/collect_list.jhtml']
                    <li [#if menu=="my_employee"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                            <i class="fa fa-circle-o"></i>我的会员
                        </a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
            <li class="[#if menu=='coupon'||menu=='freight_free'||menu=='discount'||
            menu=='extension'||menu=='buy_gift'||menu=='red'||menu=="bill_discount"]active[/#if] treeview">
            [@helperRole url='marketing']
                <a href="#">
                    <i class="fa fa-laptop"></i><span>营销工具</span>
                    <span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>
                </a>
            [/@helperRole]


                <ul class="treeview-menu">

                [#--[@helperRole url='store/member/red/list.jhtml']--]
                    [#--<li [#if menu=="red"]class="active"[/#if]>--]
                        [#--<a href="javascript:roleRoot('${base}/${helperRole.retUrl}?type=tenantBonus','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>--]
                            [#--<i class="fa fa-circle-o"></i>红包</a>--]
                    [#--</li>--]
                [#--[/@helperRole]--]
                [@helperRole url='store/member/coupon/list.jhtml']
                    <li [#if menu=="coupon"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}?type=tenantCoupon','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>代金券</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/promotion/list.jhtml?type=mail']
                    <li [#if menu=="freight_free"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}><i
                                class="fa fa-circle-o"></i>满包邮</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/discount/list.jhtml']
                    <li [#if menu=="discount"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                            <i class="fa fa-circle-o"></i>限时折扣</a>
                    </li>
                [/@helperRole]

                [@helperRole url='store/member/extension/index.jhtml']
                    <li [#if menu=="extension"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                            <i class="fa fa-circle-o"></i>代理佣金</a>
                    </li>
                [/@helperRole]

                [@helperRole urls='store/member/buygifts/listbuygift.jhtml;store/member/buygifts/listgift.jhtml']
                    <li [#if menu=="buy_gift"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                            <i class="fa fa-circle-o"></i>买赠搭配</a>
                    </li>
                [/@helperRole]

                [@helperRole urls='store/member/bill/discount/list.jhtml;']
                    <li [#if menu=="bill_discount"]class="active"[/#if]>
                        <a href="javascript:roleRoot('${base}/${helperRole.retUrl}','${helperRole.retUrl}');" ${helperRole.noAuthorityStype}>
                            <i class="fa fa-circle-o"></i>优惠买单</a>
                    </li>
                [/@helperRole]
                </ul>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>
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