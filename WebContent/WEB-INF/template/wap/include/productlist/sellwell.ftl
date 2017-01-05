[#-- 店主推荐--]
<div class="ls_product ls_product_sellwell cf col-2">
    {{#with datas}}
        {{#each this}}
        <div class="box_product fl">
            <a href="${base}/wap/product/content/{{id}}/product.jhtml">
                {{!商品图片}}
                <div class="imgsymbol">
                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png" data-original="{{thumbnail}}">
                </div>
                {{!商品描述}}
                <p class="description ft-bs0">{{fullName}}</p>
                {{!促销标签}}
                <span class="promtags none">
                    {{#each promotions}}
                        <i class="iconfont ft-bs1 promtag pt-{{type}}"></i>
                    {{/each}}
                </span>
                {{!价格&好评度}}
                <div class="weui_cell amounts">
                    <div class="weui_cell_bd weui_cell_primary">
                        <b class="ft-bs15 clr-red05 price">￥{{price}}</b>
                        <del class="ft-bs0 clr-grey03 marketprice">￥{{wholePrice}}</del>
                    </div>
                    <div class="weui_cell_ft clr-red05 ft-bs0">
                        <span class="praisedegree">好评度&nbsp;{{praisedegree}}</span>
                    </div>
                </div>
            </a>    
        </div>
        {{/each}}
    {{/with}}
</div>
