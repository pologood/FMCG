<div class="am-g">
    <div class="weui_cells">
        <a class="weui_cell" href="javascript:">
            <div class="weui_cell_hd am-margin-right-xs">
                <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                     data-original="${(review.member.headImg)!}" alt="icon"
                     style="width: 3rem;border-radius: 50%;">
            </div>
            <div class="weui_cell_bd weui_cell_primary dark-grey font-default">
                <p>[#if review.member??]${mosaic(review.member.username,3)}[#else]游客[/#if]</p>
            </div>
            <div class="weui_cell_ft font-small">
            ${review.createDate?string("yyyy-MM-dd HH:mm:SS")}
            </div>
        </a>
    </div>
    <div class="weui_cells cl margin-0">
        <div class="weui_cell ">
            <div class="weui_cell_primary">
                [#if review.score == '3'||review.score == '4'||review.score == '5']
                    <span class="gd-review-btn font-small_1">
                        好评
                    </span>
                [#else ]
                    <span class="bd-review-btn font-small_1">
                        差评
                    </span>
                [/#if]
                <span class="font-small_1">${review.content}</span>
            </div>
        </div>
    </div>
    [#if review.product??]

        <div class="weui_cells margin-0 cl">
            <div class="weui_cell cl">
                <ul class="am-avg-4" style="width:100%;">
                    [#list review.product.productImages as productImage]
                        <li style="padding:5px 5px 0px 5px;">
                            <div style="width:100%;height:0;padding-bottom:100%;overflow:hidden;border-radius:6px;">
                                <img class="lazy" src="${base}/resources/wap/image/img-pl.png"
                                     data-original="${productImage.thumbnail}" alt="${productImage.title}">
                            </div>
                        </li>
                    [/#list]
                </ul>
            </div>
        </div>
        <div class="weui_cells cl margin-0">
            <div class="weui_cell font-small_1 light-gray">
                <div class="weui_cell_bd am-margin-right-xs"><!--span>颜色：浅灰/彩蓝</span--></div>
                <div class="weui_cell_bd am-margin-right-xs"><!--span>型号：XS</span--></div>
                <div class="weui_cell_bd am-margin-right-xs">
                    <span>购买日期：${review.createDate?string("yyyy-MM-dd HH:mm:SS")}</span></div>
            </div>
        </div>
    [/#if]
</div>