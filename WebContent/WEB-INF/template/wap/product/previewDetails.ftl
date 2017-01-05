<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>商品详情</title>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>

<script type="text/html" id="tpl_wraper">
    <div class="am-g bg-silver">

    [#include "/wap/product/p_preview.ftl"/]

        <div class="am-g border-bt">
            <div id="silder" class="silder" style="position:relative">
                <img src="${base}/resources/wap/2.0/images/transparent_800x800.png" style="width:auto;max-height:800px" />
            </div>
        </div>
        <!-- banner -->
        <div class="bg-default">
            <div class="weui_cell cl-center">
                <div class="weui_cell_primary">
                    <p class="font-default">${product.name}</p>
                    <p class="color-orangered font-default">
                        ￥${price}
                    [#if product.price<product.marketPrice]
                        <small>
                            <del class="light-grey">￥${product.marketPrice}</del>
                        </small>
                    [/#if]
                    </p>
                </div>
                <!-- div class="weui_cell_bd" style="padding-top: 3px;">
                    <p class="font-small_1 border-r" style="padding:0 3px;">降价通知</p>
                </div -->
            </div>
        [#if promotions??&&promotions.size()!=0]
            <div class="weui_cell">
                <div class="weui_cell_bd"></div>
                <div class="weui_cell_bd weui_cell_primary">
                    [#list promotions as promotion]
                        <p class=" font-default am-margin-right-xs">
                            <i class="font-default color-darkorange iconfont">
                                &#xe65c;
                            </i>${promotion.name}.
                        </p>
                    [/#list]
                </div>
            </div>
        [/#if]
        </div>
        <!-- 商品价格、折扣等   href="javascript:showDetailDialog($('#product_details'));"-->
        <div class="weui_cells ">
        [#if hasSpecication!='']
            <div class="weui_cell dark-grey bg-default">
                <div class="weui_cell_hd weui_cell_primary font-default">
                    选择：颜色分类/尺码
                </div>
                <div class="weui_cell_ft"><i class="iconfont">&#xe635;</i></div>
            </div>
        [/#if]
            <a href="${base}/wap/product/previewReviews/${product.id}.jhtml" class="weui_cell dark-grey bg-default">
                <div class="weui_cell_primary font-default">
                    商品评价
                </div>
                <div class="weui_cell_hd font-small">
                    <span>${product.scoreCount}人评价</span>
                </div>
                <!--<div class="weui_cell_hd font-large">&#xe65f;</div>-->
            </a>
            <div class="weui_cell dark-grey font-large">
                <div class="weui_cell_primary font-default">
                    运费描述
                </div>
                <div class="weui_cell_hd font-small">
                    <span>同城配送</span>
                </div>
                <!--[#--<div class="weui_cell_hd font-large">&#xe65f;</div>--]-->
            </div>
        </div>
        <div class="weui_cells">
            <a class="weui_cell dark-grey"
               href="javascript:">
                <div class="weui_cell_hd am-margin-right-sm">
                    <div class="am-g border-r" style="width:6rem;height:6rem;">
                        <img src="${tenant.thumbnail}" alt="">
                    </div>
                </div>
                <div class="weui_cell_hd weui_cell_primary">
                    <p class="font-default" style="line-height:2;">${tenant.name}</p>
                    <p class="shop-level" style="color: #00a0e9;">
                    </p>
                </div>
                <div class="weui_cell_ft"><i class="iconfont">&#xe635;</i></div>
            </a>
        </div>
        <div class="weui_cells">
            <div class="am-g spacing">
                <div class=" am-titlebar-default">
                    <h2 class=" am-titlebar-title font-small PDT">
                        为您推荐
                    </h2>
                </div>
                <div class="am-g">
                    <ul class="am-gallery am-avg-3 am-gallery-overlay shop-tip-items" style="background-color: #fff;">
                    [@product_list areaId=areaId tenantId=tenant.id tagIds=5 count=9]
                        [#list products as product]
                            <li>
                                <a class="am-gallery-item am-gallery-item-default"
                                   href="javascript:">
                                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                                         data-original="${product.thumbnail}" alt="拼命加载中"/>
                                    <h6 class="items-title font-small_1">
                                    ${product.fullName}
                                    </h6>
                                    <div class="red font-small">
                                        ￥${product.price}
                                        [#if product.price<product.marketPrice]
                                            <small>
                                                <del class="light-grey">￥${product.marketPrice}</del>
                                            </small>
                                        [/#if]
                                    </div>
                                </a>
                            </li>
                        [/#list]
                    [/@product_list]
                    </ul>
                </div>
            </div>
        </div>
    </div>

</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        $('.lazy').picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'});
        /* */
        //get the fixed ele and then set empty placeholder height
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        /* set slider data and init slider*/
        var ads = [];
    [#list product.productImages as productImage]
        var path = '${productImage.large}';
        var content = {content: path};
        ads.push(content);
    [/#list]
        //
        var hasSpecication = '${hasSpecication}';
        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 0,
            isOverspread: 0,
            isAutoplay: 0,
            fixPage: false,
            animateTime: 800,
            onslide:function(){
                if(this.slideIndex==this.data.length-1){
                    $(".shop-items a").eq(1).click();
                }
                //当前索引
                //this.slideIndex
                //数组类型
                //this.data.length
            }
        });

        $(".shop-level").html(loadGrade('${tenant.grade}'));
        $("#silder").on('swipeLeft', function(event) {
            //event.preventDefault();
            event.stopPropagation();
        });
        //
        productSwipeLAndR($("[data-title-index]").data('title-index'));
    });

    function loadGrade(number) {
        var img = "";
        for (var i = 0; i < number; i++) {
            img = img + '<i class="iconfont" >&#xe675;</i>&nbsp;';
        }
        if(img==""){
            img = '<i class="iconfont" >&#xe675;</i>&nbsp;';
        }
        return img;
    }
</script>
</body>
</html>
