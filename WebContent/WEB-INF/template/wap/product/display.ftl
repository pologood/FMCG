<!DOCTYPE html>
<html>
<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${product.name}</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/pluginrevise.css">
</head>
<body class="bg-00">
[#-- 商品相册轮播--]
<div id="productalbum" class="productalbum bg-05 PT-DY" style="position: relative;">
    <img src="${base}/resources/wap/3.0/image/blank/blank640x640.png" alt="">
    <span class="pagenum"><i>1</i>/<i></i></span>
</div>
[#-- 商品描述--]
<div class="productdesc bg-05 PT-DY">
[#-- 商品名称--]
    <p class="ft-bs15 name">${product.fullName}</p>
[#-- 商品量--]
    <div class="weui_cell productdesc-amount PT-DY">
        <div class="weui_cell_bd weui_cell_primary">
            <b class="ft-bs4 price">￥${product.price}</b>
        [#if product.marketPrice gt product.price]
            <del class="ft-bs0 clr-grey03 marketprice">￥${product.marketPrice}</del>
        [/#if]
        </div>
        <div class="clr-grey01 ft-bs0 weui_cell_ft">
        [#if product.monthSales??&&product.monthSales?has_content]<span
                class="salesvolume">月销${product.monthSales}件</span>[/#if]
            <span class="hitnum">${product.hits}人访问</span>
        </div>
    </div>
[#-- 相关信息--]
    <div class="productdesc-otherinfo">
    [#-- <span class="ft-bs0 clr-grey01">快递：10元</span>--]
    [#if promotions??&&promotions?has_content]
        <span class="promtags">
            [#list promotions as promotion]
                <i class="iconfont ft-bs1 promtag pt-${promotion.type}"></i>
            [/#list]
        [#--<i class="iconfont ft-bs1 promtag pt-coupon"></i>--]
        [#--<i class="iconfont ft-bs1 promtag pt-discount"></i>--]
        [#--<i class="iconfont ft-bs1 promtag pt-buyfree"></i>--]
        </span>
    [/#if]
    </div>
</div>
[#-- 店铺信息--]
<div class="tenantinfo bg-05 PT-DY">
[#-- 店铺名--]
    <a class="weui_cell weui_cell_access tenantinfo-title PT-DY" href="${base}/wap/tenant/index/${tenant.id}.jhtml">
        <div class="weui_cell_bd weui_cell_primary">
            <h3 class="ft-bs15 clr-grey01 tanantname">${tenant.name}</h3>
        </div>
        <div class="weui_cell_ft">
        </div>
    </a>
    <div class="weui_cell tenantinfo-details">
        <div class="weui_cell_hd">
            <img src="${tenant.thumbnail}" alt="">
        </div>
        <div class="weui_cell_bd weui_cell_primary">
            <p class="ft-bs0 clr-grey03 position"><i class="iconfont ft-bs1 clr-orange04">&#xe623;</i>${tenant.address}
            </p>
        [#-- 星级--]
            <div class="datashow ft-bs09 clr-grey01">
            [#-- <span>全部好货12345</span>--]
                <span>收藏人数${tenant.favoriteMembers?size}</span>
                <div class="cf ft-bs08 starlevels" data-starnum="${tenant.score}" data-starlevelized="false">
                    <i></i>
                    <i></i>
                    <i></i>
                    <i></i>
                    <i></i>
                </div>
            </div>
        [#-- 七天 担保--]
            <div class="qualification">
                <span class="ft-bs09 clr-red05"><i class="iconfont ft-bs0 clr-red05">&#xe66d;</i>七天退货</span>
                <span class="ft-bs09 clr-red05"><i class="iconfont ft-bs0 clr-red05">&#xe66e;</i>担保交易</span>
            </div>
        </div>
        <div class="weui_cell_ft">
            <a href="tel:${tenant.telephone}" class="">
                <i class="iconfont ft-bs3 clr-red05">&#xe687;</i>
            </a>
        </div>
    </div>
</div>
[#-- 选择颜色分类--]
[#if hasSpecication!='']
<a class="weui_cell ft-bs0 weui_cell_access choose_colorsize PT-DY">
    <div class="weui_cell_bd weui_cell_primary">
        <span>选择：颜色分类/尺码</span>
    </div>
    <div class="weui_cell_ft">
    </div>
</a>
[/#if]
[#-- 评论传送门--]
<div class="reviewportal bg-05 PT-DY">
[#-- reviewportal-title--]
    <a href="${base}/wap/product/productReviews/${product.id}.jhtml"
       class="weui_cell ft-bs0 weui_cell_access reviewportal-title PT-DY">
        <div class="weui_cell_bd weui_cell_primary clr-grey02">
            评价：
        </div>
        <div class="weui_cell_ft clr-red05">
        [#-- 好评${score}%--]
        </div>
    </a>
[#-- 评论代表--]
[#--[#list reviews as review]--]
[#--<div class="reviewportal-repet PT-DY">--]
[#--<div class="weui_cell ft-bs09 clr-grey01 who">--]
[#--<div class="weui_cell_hd">--]
[#--<img src="http://placehold.it/50x50/333/fff.png?text=A" alt="">--]
[#--</div>--]
[#--<div class="weui_cell_bd weui_cell_primary">--]
[#--menlalishanshijie--]
[#--</div>--]
[#--<div class="weui_cell_ft">--]
[#--2016-8-11--]
[#--</div>--]
[#--</div>--]
[#--<p class="words clr-grey01 ft-bs0">--]
[#--昨晚下单的，今晚就可以穿了，质量很好真的很满意质量很意质量好真的很满意--]
[#--</p>--]
[#--<div class="ft-bs08 clr-grey03 tags">--]
[#--<span>颜色：浅灰/彩蓝</span>--]
[#--<span>型号：XS</span>--]
[#--<span>购买日期：2015-11-11 09:56</span>--]
[#--</div>--]
[#--</div>--]
[#--[/#list]--]
[#-- 查看全部评价--]
    <a href="${base}/wap/product/productReviews/${product.id}.jhtml"
       class="weui_cell weui_cell_access ft-bs09 reviewportal-viewall PT-DY">
        <div class="weui_cell_bd weui_cell_primary">
            查看全部评价
        </div>
    </a>
</div>
[#-- 主体页签:详情，属性，推荐--]
<div class="displaytab PT-DY self">
</div>
[#-- 店主推荐--]
<div class="shopkeeper_recm PT-DY hidden">
    <div class="shopkeeper_recm-hd ft-bs15">
        店主推荐
    </div>
    <div class="shopkeeper_recm-bd bg-00">
    </div>
</div>
[#-- 邻家好货--]
<div class="nearbygoods PT-DY hidden">
    <div class="nearbygoods-hd ft-bs15">
        邻家好货
    </div>
    <div class="nearbygoods-bd bg-00">
    </div>
</div>
[#-- 选择颜色尺寸滑入框--]
<div class="sheet-choose_colorsize PT-DY">
</div>
[#-- 客服(联系商家)滑入框--]
<div class="sheet-contacttenant PT-DY">
</div>
[#-- 回到顶部--]
<div class="backtotop">
</div>
[#-- 高度占位元素--]
<div data-emptyfor="opera_panel"></div>
[#-- 底部操作面板:客服,收藏,购物车,加入购物车,立即购买--]
[#include "/wap/product/pfoot-2.0.ftl"/]
<!-- HBS:pfoot-2.0:联系列表-->
<script type="text/x-handlebars-template" id="tpl_contactlists">
    <div class="weui_cells">
        {{!标题}}
        <div class="weui_cell weui_cells_title contactlists-title">
            <div class="weui_cell_bd weui_cell_primary">
                <span class="ft-bs2">{{listtitle}}</span>
            </div>
        </div>
        {{!内容}}
        <div class="weui_cells contactlists-ctn">
            {{#each listdatas}}
            <div class="weui_cell">
                <div class="weui_cell_bd weui_cell_primary">
                    <span class="ft-bs1 clr-grey02">{{name}}：{{pos}}</span>
                    <span class="ft-bs09 clr-grey03">销售出:{{#if salenum}}{{salenum}}{{else}}0{{/if}}件&nbsp;&nbsp;粉丝数:{{#if salenum}}{{fansnum}}{{else}}0{{/if}}</span>
                </div>
                <div class="weui_cell_ft">
                    <a href="javascript:;" class="iconfont contactme ft-bs35">&#xe64d;</a>
                    <a href="tel:{{telnum}}" class="iconfont telme ft-bs2">&#xe687;</a>
                </div>
            </div>
            {{/each}}
        </div>
    </div>
</script>
<!-- HBS:display:详情-->
<script type="text/x-handlebars-template" id="tpl_display_detail">
    <div class="displayattrs PT-DY">
        <p class="noattr ft-bs1 clr-grey03" style="text-align: left">
        [#if product.descriptionapp??&&product.descriptionapp?has_content]
            ${product.descriptionapp}
        [#elseif product.introduction??&&product.introduction?has_content]
            ${product.introduction}
        [/#if]
        </p>
    </div>
</script>
<!-- HBS:display:属性-->
<script type="text/x-handlebars-template" id="tpl_display_attributes">
    <div class="displayattrs PT-DY">
        {{#if this.length}}
        <table class="ft-bs0 clr-grey01">
            {{#each this}}
            <tr data-attribute="{{id}}">
                <td>{{name}}</td>
                <td>{{value}}</td>
            </tr>
            {{/each}}
        </table>
        {{else}}
        <p class="noattr ft-bs1 clr-grey03">暂无属性</p>
        {{/if}}
    </div>
</script>
<!-- HBS:product:店主推荐-->
<script type="text/x-handlebars-template" id="tpl_product_recommend">
    [#include "/wap/include/productlist/recommend.ftl"/]
</script>
<!-- HBS:product:邻家好货-->
<script type="text/x-handlebars-template" id="tpl_product_nearby">
    [#include "/wap/include/productlist/nearby.ftl"/]
</script>
<!-- HBS:选择颜色和规格-->
<script type="text/x-handlebars-template" id="tpl_choose_colorsize">
    [#include "/wap/product/colorAndStyle_unit-v2.ftl"/]
</script>
<!-- BOTTOM SCRIPT-->
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //${base}
    _TH_.area = {
        name: "合肥",
        id: 999
    };
    //图片轮播示例
    _TH_.slider_productctns = [{
        content: '<a href="javascript:;"><img src="http://placehold.it/640x640/fff/333.png?text=A" alt=""></a>'
    }, {
        content: '<a href="javascript:;"><img src="http://placehold.it/640x640/fff/333.png?text=B" alt=""></a>'
    }, {
        content: '<a href="javascript:;"><img src="http://placehold.it/640x640/fff/333.png?text=C" alt=""></a>'
    }];
    _TH_.slider_productctns.length = 0;
    _TH_.data_display_detail = [];
    [#list product.productImages as productImage]
    _TH_.slider_productctns.push({content: '${productImage.large}'});
    [#--_TH_.data_display_detail.push({--]
    [#--src: '${productImage.large}'--]
    [#--});--]
    [/#list]
    [#-- 店铺id--]
    _TH_.storeid = "${tenant.id}";
    [#-- 线下门店集合--]
    _TH_.storedatas = [];
    [#list deliveryCenters as deliveryCenter]
    _TH_.storedatas.push({
        storeid: "${deliveryCenter.id}",
        name: "${deliveryCenter.name}",
        address: "${deliveryCenter.address}"
    });
    [/#list]
    [#-- 商品id--]
    _TH_.productid = "${product.id}";
    [#-- 是否已被收藏--]
    _TH_.hasFavorite = '${hasFavorite}';
    //商品当前购买状态
    _TH_.buyaction_status = "${status}";//是否支持购买 "success"
    _TH_.buyaction_isMarketable = "${isMarketable}";//商品是否已下架 "true"
    //其他数据
    [#-- datals:店主推荐--]
    _TH_.productls_recommend = [];
    [@product_list areaId=areaId tenantId=tenant.id tagIds=5 count=4]
        [#list products as product]
        _TH_.productls_recommend.push({
            fullName: "${product.fullName}",
            id: ${product.id},
            price: ${product.price},
            promotions: ["new", "coupon", "seckill"],
            thumbnail: "${product.thumbnail}",
            wholePrice: ${product.marketPrice},
            praisedegree: "97%"
        });
        [/#list]
    [/@product_list]
    [#-- datals:邻家好货--]
    _TH_.productls_nearby = [];
</script>
[#include "/wap/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.js"></script>
<!-- JS:page-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/page/thw/product/display.js"></script>
<!-- JS:module-->
[#-- <script type="text/javascript" src="${base}/resources/wap/3.0/js/module/activity/activityBuoy.js"></script>--]
</body>
<script>
    _wxSDK.initInterface($script,{
        afterOnMenuShare:function () {
            _wxSDK.onMenuShare();
        }
    })
</script>
</html>
