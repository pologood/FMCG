<div class="empty-for-fixedtop_tab">

</div>
<div class="am-g border-bt fixedtop_tab">
    <div class="weui_cell shop-items font-large" style="width: 100%">
        <div class="weui_cell_primary">
        	<a href="${base}/wap/product/content/${product.id}/product.jhtml" [#if type=='details'] class="visit" data-title-index="0"[#else ]style="color: #999999;" [/#if] >商品</a>
        </div>
        <div class="weui_cell_primary">
        	<a href="${base}/wap/product/productParameters/${product.id}.jhtml" [#if type=='parameters'] class="visit" data-title-index="1"[#else ]style="color: #999999;" [/#if]>详情</a>
        </div>
        <div class="weui_cell_primary">
        	<a href="${base}/wap/product/productReviews/${product.id}.jhtml" [#if type=='reviews'] class="visit" data-title-index="2"[#else ]style="color: #999999;" [/#if]>评价<label style="color: red;">(${product.scoreCount})</label></a>
        </div>
    </div>
</div>