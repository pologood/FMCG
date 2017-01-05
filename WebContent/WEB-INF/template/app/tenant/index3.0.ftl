<!DOCTYPE html>
<html>
<head>
[#include "/app/include/resource-3.0-part1.ftl"/]
    <title>${name}</title>
    <!-- <link rel="stylesheet" href="${base}/resources/app/3.0/css/static.css">-->
    <link rel="stylesheet" href="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/app/3.0/css/pluginrevise.css">
</head>
<body class="bg-00">
<div style="overflow-x: hidden">
    <input type="hidden" id="pagecache"></input>
</div>
[#-- 顶部搜索框及购物车--]
[#-- 店铺banner,店铺信息--]
<div class="banner TT-IN">
    <div id="silder" class="silder banner-bgs" style="position:relative;color:white;">
        <img src="${base}/resources/app/3.0/image/blank/blank640x250.png" alt="" style="width:100%;">
        <div class="mask_bg"></div>
    </div>
    <div class="banner-shopinfo weui_cell clr-grey08">
        <div class="weui_cell_hd">
            <img src="${imgUrl}" alt="">
        </div>
        <div class="weui_cell_bd weui_cell_primary">
            <h2 class="ft-bs15">${name}</h2>
        [#if tenant.score gte 0]
            <div class="starlevelsR">
                <div class="cf ft-bs0 starlevels" data-starnum="${tenant.score}" data-starlevelized="false">
                    <i></i>
                    <i></i>
                    <i></i>
                    <i></i>
                    <i></i>
                </div>
                <span class="ft-bs0 clr-red05">${tenant.score}分</span>
            </div>
        [/#if]
        </div>
        <div class="weui_cell_ft">
            <span class="save_tenant">
            [#if flag=='yes']
                <i class="iconfont clr-red05 ft-bs4">&#xe645;</i>
            [#elseif flag=='no']
                <i class="iconfont clr-grey08 ft-bs4">&#xe620;</i>
            [/#if]
            </span>
            <div class="ft-bs09 clr-grey08 L">
                <span>粉丝&nbsp;<b id="favorite_num">${favoriteNum}</b></span>
                <span>访问&nbsp;${hits}</span>
            </div>
        </div>
    </div>
</div>
[#-- 店铺导航--]
<div class="nav TT-IN">
    <a href="javascript:;" class="it ft-bs1 index" data-firsttime="true">
        <div class="imgsignR">
            <i class="iconfont">&#xe691;</i>
        </div>
        <p class="ft-bs15">店铺首页</p>
    </a>
    <a href="javascript:;" class="it ft-bs1 all" data-firsttime="true" data-id="" data-switchcategory="false">
        <div class="imgsignR">
            <i class="iconfont">&#xe694;</i>
        </div>
        <p class="ft-bs15">全部好货</p>
    </a>
    <a href="javascript:;" class="it ft-bs1 new" data-firsttime="true">
        <div class="imgsignR">
            <i class="iconfont">&#xe692;</i>
        </div>
        <p class="ft-bs15">新品</p>
    </a>
    <a href="javascript:;" class="it ft-bs1 promotion" data-firsttime="true">
        <div class="imgsignR">
            <i class="iconfont">&#xe693;</i>
        </div>
        <p class="ft-bs15">促销</p>
    </a>
</div>
[#-- 店铺导航对应内容--]
<div class="nav_ctnR TT-IN">
[#-- ContentFor:店铺首页--]
    <div class="it for-index none">
    [#-- 优惠券展示--]
    [#if coupons?size>0]
        <div class="couponboothR bg-05 TT-IN">
            <div class="clr-grey02 title">
                <p class="ft-bs2">先领券&nbsp;&nbsp;再购物</p>
                <p class="ft-bs0">first get coupons and then shopping</p>
            </div>
            <div class="couponbooth TT-IN">
                [#list coupons as coupon]
                        <a href="rzico://coupon/get?id=${coupon.id}"
                           class="coupon TT-IN">
                            <div class="up">
                            [#-- 优惠数额--]
                                <div class="amount">
                                    <span>￥${coupon.amount}</span>
                                </div>
                            [#-- 优惠说明--]
                                <div class="desc">
                                    <h5 class="ft-bs05">优惠券</h5>
                                    <span class="ft-bs08">满${coupon.minimumPrice}元可用</span>
                                </div>
                            </div>
                            <div class="down">
                                <span class="ft-bs0">点击领取</span>
                            </div>
                        </a>
                [/#list]
            </div>
        </div>
    [/#if]
    [#-- 店铺商品轮播--]
        <div class="col-2 bg-05 carousel TT-IN">
        [#list tenant.products as product]
            [#list product.tags as tag]
                [#if tag.id==5 && product.isMarketable==true&&product.isList==true]
                    [#if (tag_index < 5)]
                        <div class="box_product">
                            <a href="rzico://product?id=${product.id}">
                                <div class="imgsymbol">
                                    <img src="${product.thumbnail}" alt="">
                                </div>
                                <P>
                                    <span class="clr-red05 ft-bs15">¥${product.calcEffectivePrice(null)}</span>
                                    [#if product.marketPrice>product.calcEffectivePrice(null)]
                                        <s class="clr-grey03 ft-bs0">¥${product.marketPrice}</s>
                                    [/#if]
                                </P>
                            </a>
                        </div>
                    [/#if]
                [/#if]
            [/#list]
        [/#list]
        </div>
    [#-- 店铺导购--]
    [#if employeePage.content??&&employeePage.content?has_content]
        <div class="shopguide bg-05 TT-IN">
            [#list employeePage.content as employee]
                <div class="shopguide_box">
                    <div class="shopguide_img">
                        <img class="lazy" data-original="${employee.member.headImg}" src="${base}/resources/app/3.0/image/placeholder/user.png" alt="">
                    </div>
                    <div class="shopguide_description">
                        <p class="clr-red05 ft-bs0">${employee.member.displayName}
                            <img src="${base}/resources/app/3.0/image/static/fruit/icon.png" alt=""></p>
                        <p class="clr-grey03 ft-bs09">销量${employee.quertity}件</p>
                    [#-- <a href="tel:${employee.member.mobile};" class="bg-07 clr-grey08 ft-bs09">联系导购</a>--]
                        <a href="rzico://message?id=${employee.member.id}&nickName=${employee.member.displayName}"
                           class="bg-07 clr-grey08 ft-bs09">联系导购</a>
                    </div>
                </div>
            [/#list]
        </div>
    [/#if]
    [#-- 新品概览banner--]
    [@ad_position id=145 tenantId=id count=1/]
    [#-- 商品列表-新品概览(一列)--]
        <div class="newgoods bg-05 TT-IN">
            <div class="ls_product ls_product_new col-1">
            [#list tenant.products as product]
                [#list product.tags as tag]
                    [#if tag.id==2 && product.isMarketable && product.isList]
                        [#if tag_index<5]
                        [#-- new the following--]
                            <div class="box_product">
                                <a href="rzico://product?id=${product.id}">
                                [#-- 商品图片--]
                                    <div class="imgsymbol">
                                        <img class="lazy"
                                             src="${base}/resources/app/3.0/image/AccountBitmap-product.png"
                                             data-original="${product.thumbnail}">
                                    </div>
                                [#-- 商品内容盒--]
                                    <div class="inforap">
                                    [#-- 商品描述--]
                                        <p class="description ft-bs0">${product.name}</p>
                                    [#-- 促销标签--]
                                        <span class="promtags none">
                                            [#list product.promotions as promotion]
                                                <i class="iconfont ft-bs1 promtag pt-${promotion.type}"></i>
                                            [/#list]
                                        </span>
                                        <p>
                                            <b class="ft-bs15 clr-red05 price">￥${product.price}</b>
                                            [#if product.marketPrice>product.price]
                                                <del class="ft-bs0 clr-grey03 marketprice">￥${product.marketPrice}</del>
                                            [/#if]
                                        </p>
                                        <p class="ft-bs0 clr-red05 praisedegree">&nbsp;</p>
                                        <p class="clr-grey13 ft-bs0">人气 ${product.monthHits} /
                                            月销量 ${product.monthSales}</p>
                                    </div>
                                [#-- 立即购买按钮--]
                                    <span class="buynow  ft-bs1">
                                        <i class="iconfont">&#xe651;</i>
                                        <u class="waitingcart hidden">
                                            <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png" data-original="${product.thumbnail}">
                                        </u>
                                    </span>
                                </a>
                            </div>
                        [/#if]
                    [/#if]
                [/#list]
            [/#list]
            </div>
        </div>
    [#-- 商品列表-热销商品(两列)--]
        <div class="sellwell TT-IN">
            <div class="sellwell-hd ft-bs15 clr-grey01 bg-05">
                <span>店铺热销</span>
            </div>
            <div class="sellwell-bd">
            [#-- 店主推荐--]
                <div class="ls_product ls_product_sellwell cf col-2">
                [#list tenant.products as product]
                    [#list product.tags as tag]
                        [#if tag.id==1 && product.isMarketable==true && product.isList==true]
                            [#if tag_index<5]
                                <div class="box_product fl">
                                    <a href="rzico://product?id=${product.id}">
                                    [#-- 商品图片--]
                                        <div class="imgsymbol">
                                            <img class="lazy"
                                                 src="${base}/resources/app/3.0/image/AccountBitmap-product.png"
                                                 data-original="${product.thumbnail}">
                                        </div>
                                    [#-- 商品描述--]
                                        <p class="description ft-bs0">${product.name}</p>
                                    [#-- 促销标签--]
                                            <span class="promtags none">
                                                [#list product.promotions as promotion]
                                                    <i class="iconfont ft-bs1 promtag pt-${promotion.type}"></i>
                                                [/#list]
                                            </span>
                                    [#-- 价格&好评度--]
                                        <div class="weui_cell amounts">
                                            <div class="weui_cell_bd weui_cell_primary">
                                                <b class="ft-bs15 clr-red05 price">￥${product.price}</b>
                                                [#if product.marketPrice>product.price]
                                                    <del class="ft-bs0 clr-grey03 marketprice">
                                                        ￥${product.marketPrice}</del>
                                                [/#if]
                                            </div>
                                            <div class="weui_cell_ft clr-red05 ft-bs0">
                                                <span class="praisedegree">&nbsp;</span>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            [/#if]
                        [/#if]
                    [/#list]
                [/#list]
                </div>
            </div>
        </div>
    </div>
[#-- ContentFor:全部好货--]
    <div class="it for-all none">
    [#-- 点击加载更多--]
        <a href="javascript:;" class="ft-bs1 bg-14 clr-grey03 loadonclick" data-page-size="16" data-page-number="1"
           data-page-ordertype="weight" data-page-hasmore="true" data-needloading="true">点击加载更多</a>
    </div>
[#-- ContentFor:新品--]
    <div class="it for-new none">
    [#-- 点击加载更多--]
        <a href="javascript:;" class="ft-bs1 bg-14 clr-grey03 loadonclick" data-page-size="16" data-page-number="1"
           data-page-ordertype="weight" data-page-hasmore="true" data-needloading="true">点击加载更多</a>
    </div>
[#-- ContentFor:促销--]
    <div class="it for-promotion none">
    [#-- 点击加载更多--]
        <a href="javascript:;" class="ft-bs1 bg-14 clr-grey03 loadonclick" data-page-size="16" data-page-number="1"
           data-page-ordertype="weight" data-page-hasmore="true" data-needloading="true">点击加载更多</a>
    </div>
</div>
[#-- 店铺(门店)信息滑入框--]
<div class="sheet-tntcontactinfo TT-IN">
</div>
[#-- 宝贝分类弹出框--]
<div class="popup-classific TT-IN" data-isshown="false" style="display: none">
    <ul class="menus">
        <li data-id="all">
            <a href="javascript:" class="ft-bs1">全部好货</a>
        </li>
    [#list productCategoryTenants as category]
        <li data-id="${category.id}">
            <a href="javascript:;" class="ft-bs1">${category.name}</a>
        </li>
    [/#list]
    </ul>
</div>
[#-- 高度占位元素--]
<div data-emptyfor="btoperas"></div>
 [#--买单立减浮标链接--]
<a href="rzico://paybill?id=${id}" class="link_toBRRN">
<img src="${base}/resources/app/3.0/image/tenant/link_toBRRN.png" alt="">
</a>
[#-- 商家首页底部操作面板--]
<!-- TXT:联系店铺(门店)-->
<script type="text/html" id="tpl_tntcontactinfo">
    [#-- 店铺首页-联系店铺(门店)-html结构--]
    <div class="tntcontactinfo TT-IN">
    [#-- 头部,店铺信息,tntinfoR--]
        <div class="weui_cells tntinfoR">
            <div class="weui_cell tntinfo">
                <div class="weui_cell_hd tntinfo-hd">
                    <img class="weui_media_appmsg_thumb" src="${imgUrl}" alt="">
                </div>
                <div class="weui_cell_bd tntinfo-bd">
                    <h4 class="ft-bs2">${name}</h4>
                    <span class="ft-bs0 clr-grey02">营业时间：9:00—18:00</span>
                </div>
            </div>
        </div>
    [#-- 内容,门店信息,storesinfoR--]
        <div class="weui_cells weui_cells_access storesinfoR">
        [#list deliveryCenters.content as deliveryCenter]
            <a class="weui_cell" href="${base}/app/tenant/contact/${deliveryCenter.id}.jhtml">
                <div class="weui_cell_bd weui_cell_primary clr-grey02">
                    <p class="ft-bs0">${deliveryCenter.areaName}</p>
                    <h3 class="ft-bs15 clr-grey01">${deliveryCenter.name}</h3>
                    <p class="ft-bs0">${deliveryCenter.address}</p>
                </div>
                [#if deliveryCenter.location??&&deliveryCenter.location?has_content]
                    <div class="weui_cell_ft ft-bs1" data-location-lat="${deliveryCenter.location.lat}"
                         data-location-lng="${deliveryCenter.location.lng}">
                        <span></span>
                    </div>
                [/#if]
            </a>
        [/#list]
        </div>
    [#-- discsyms,资质声明--]
        <div class="weui_cells discsyms">
            <div class="weui_cell" style="background-color: #f4f4f4;">
                <div class="weui_cell_bd weui_cell_primary discsyms-sec1-bd">
                <span class="ft-bs05">
                    <i class="iconfont">&#xe66d;</i> 七天退货
                </span>
                <span class="ft-bs05">
                    <i class="iconfont">&#xe66e;</i> 担保交易
                </span>
                </div>
            </div>
        </div>
    </div>
</script>
<!-- HBS:促销-->
<script type="text/x-handlebars-template" id="tpl_ls_promotion">
    [#include "/app/include/productlist/promotion.ftl"/]
</script>
<!-- HBS:新品-->
<script type="text/x-handlebars-template" id="tpl_ls_new">
    [#include "/app/include/productlist/new.ftl"/]
</script>
<!-- BOTTOM SCRIPT-->
[#include "/app/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.js"></script>
<script type="text/javascript" src="${base}/resources/app/3.0/js/plugin/islider/iSlider.animate.min.js"></script>
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //tiaohuo
    //店铺id
    _TH_.tenantid = "${id}";
    [#-- 店铺是否已收藏标记--]
    _TH_.tenant_saveflag = "${flag}";
    [#-- 用户id--]
    _TH_.memberid = "${memberId}";
    _TH_.member = [];
    _TH_.member.push({headImg: '${(member.headImg)!}', mobile: '${(member.mobile)!}'});
    //其他数据
    _TH_.datalists = []; //${datalists}
    _TH_.ads = [];
    [@ad_position id = 80 tenantId=id count=5]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
            var _url = "";
                [#if ads.linkId??&&ads.linkId?has_content]
                    [#if ads.linkType=="product"]
                    _url = "rzico://product?id=${ads.linkId}";
                    [#elseif ads.linkType=="tenant"]
                    _url = "rzico://tenant?id=${ads.linkId}";
                    [/#if]
                [/#if]
            _TH_.ads.push({content: '<a href="' + _url + '"><img src="${(ads.path)!}"></a>'});
            [/#list]
        [/#if]
    [/@ad_position]
    if (_TH_.ads.length == 0) {
        _TH_.ads.push({content: '${base}/resources/app/3.0/image/no-store.png'});
    }
</script>
<!-- JS:page-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/page/thw/tenant/index-v3.js"></script>
</body>
</html>
