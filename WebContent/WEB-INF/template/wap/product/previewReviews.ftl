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

        <div class="am-g">
            <div class="weui_cells">
                <div class="weui_cell">
                    <div class="weui_cell_primary font-default">
                        商品评价
                        <small class="font-small color-orangered">好评度${score}%</small>
                    </div>
                    <div class="font-small_1 weui_cell_hd weui_cell_ft">
                    ${product.scoreCount}人评价
                    </div>
                </div>
            </div>
        </div>
        <!-- 商品好评度 -->
    [#if reviews??]
        [#list reviews as review]
        [#--[#if review.isShow]--]
            [#include "/wap/include/product_review.ftl"]
        [#--[/#if]--]
        [/#list]
    [#else]
        <div style="text-align: center;background-color: white;">暂无有效的评论！</div>
    [/#if]
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<!-- script src="${base}/resources/wap/2.0/js/lib.js"></script -->
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
    });
</script>
</body>
</html>
