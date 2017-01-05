<div class="empty-for-fixedtop_tab">

</div>
<div class="am-g border-bt fixedtop_tab">
    <div class="weui_cell shop-items font-large" style="width: 100%">
        <div class="weui_cell_primary "><a href="${base}/wap/product/content/${product.id}/preview.jhtml" [#if type=='details'] class="visit"[#else ]style="color: #999999;" [/#if] >商品</a></div>
        <div class="weui_cell_primary"><a href="${base}/wap/product/previewParameters/${product.id}.jhtml" [#if type=='parameters'] class="visit"[#else ]style="color: #999999;" [/#if]>详情</a></div>
        <div class="weui_cell_primary"><a href="${base}/wap/product/previewReviews/${product.id}.jhtml" [#if type=='reviews'] class="visit"[#else ]style="color: #999999;" [/#if]>评价</a></div>
    </div>
</div>