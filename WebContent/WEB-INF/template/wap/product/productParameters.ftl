<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>商品详情</title>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <button class="{{#expression @index '==' '0'}}choosed{{/expression}}  " onclick="getSpec();choosedHighLight();"
                id="{{color}}">{{color}}
        </button>
        {{/each}}
    </script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="am-g bg-silver">

    [#include "/wap/product/pdetail.ftl"/]
[#if (product.descriptionapp??&&product.descriptionapp?has_content)||(product.introduction??&&product.introduction?has_content)]
        <div class="weui_cells_title">
            <div class="weui_cell_primary ft-bs3 dark-grey">
                商品描述
            </div>
        </div>
        <div class="weui_cells">
            <div class="weui_cell">
                <div class="weui_cell_primary ft-bs2 dt-text">
                [#if product.descriptionapp??&&product.descriptionapp?has_content]
                ${product.descriptionapp}
                [#else]
                ${product.introduction}
                [/#if]
                </div>
            </div>
        </div>
[#else ]
        <div class="weui_cells_title">
            <div class="weui_cell_primary ft-bs3 dark-grey">
                商品展示
            </div>
        </div>
        <div class="weui_cells">
            <div class="weui_cell">
                <div class="weui_cell_primary">
                [#list product.productImages as productImage]
                    <div class="dtit-dt" style="border-color: #0B4F9C">
                        <img src="${productImage.large}" alt="">
                    </div>
                [/#list]
                </div>
            </div>
        </div>
[/#if]
        <div class="weui_cells cl">
            <div class="am-g spacing">
                <div class=" am-titlebar-default">
                    <h2 class="am-titlebar-title font-small PDT">
                        为您推荐
                    </h2>
                </div>
                <div class="am-g">
                    <ul class="am-gallery am-avg-3 am-gallery-overlay shop-tip-items" style="background: #fff;">
                    [@product_list areaId=areaId tenantId=tenant.id tagIds=5 count=9]
                        [#list products as product]
                            <li>
                                <a class="am-gallery-item am-gallery-item-default"
                                   href="${base}/wap/product/content/${product.id}/product.jhtml">
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
    [#include "/wap/product/pfoot.ftl"/]
    [#include "/wap/product/colorAndStyle_unit.ftl"/]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        //get the fixed ele and then set empty placeholder height
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        //$(".empty-for-fixedtop_tab").height($(".fixedtop_tab").height());
        $('.lazy').picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'
        });
        productSwipeLAndR($("[data-title-index]").data('title-index'));
    });
</script>
</body>
</html>
