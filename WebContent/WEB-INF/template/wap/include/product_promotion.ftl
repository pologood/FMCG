<li>
    <a href="${base}/wap/product/content/{{id}}/product.jhtml">
        <div class="am-gallery-item am-gallery-item-default displaybox"
             style="border: 1px solid #e4e4e4;background: #fff;">
            <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                 data-original="{{thumbnail}}"
                 alt="拼命加载中"/>
            <p style="text-overflow:ellipsis;overflow:hidden;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;line-height:1.5em;margin: 0.5em;height: 3em;margin-bottom: 0;">
                {{fullName}}
            </p>
            <div class="weui_cell_primary">
                        <span class="font-small_1 {{#if type}}{{#compare type 'buyfree'}} order-discount {{/compare}}{{#compare type 'seckill'}} order-gift {{/compare}}{{/if}} order-prefer">
                        {{name}}
                        </span>
            </div>
            <p class="weui_cell d-price TTI">
                <span class="shop-price red">
                    {{#if price}}
                    <span>¥</span>
                    <span class="main-price">{{price}}</span>
                    {{/if}}
                </span>
                <a class="grab_it" style="color: #fff;"
                   href="javascript:addCart('{{id}}')">
                    抢
                </a>
                <!--{{#if marketPrice}}
                <del class="text-xl light-grey" style="font-size: 10px;">
                    <span class="price-icon">原价¥</span>
                    <span class="font-num">{{marketPrice}}</span>
                </del>
                {{/if}}-->
            </p>
        [#--
        <a class="www" style="color: #fff;"
           href="javascript:addCart('{{id}}')">
            抢
        </a>--]
        </div>
    </a>
</li>

<!-- li>
    <a href="/wap/product/content/{{productId}}/product.jhtml">
        <div class="am-gallery-item am-gallery-item-default" style="border: 1px solid #e4e4e4;">
            <img class="lazy" src="/resources/wap/2.0/images/AccountBitmap-product.png"
                 data-original="{{thumbnail}}"
                 alt="拼命加载中"/>
            <p>
                {{fullName}}
            </p>
            <div class="weui_cell_primary">
                        <span class="font-small_1 {{#if type}}{{#compare type 'buyfree'}} order-discount {{/compare}}{{#compare type 'seckill'}} order-gift {{/compare}}{{/if}} order-prefer">
                        {{name}}
                        </span>
            </div>
            <p class="d-price">
                {{#if price}}
                            <span class="shop-price red">
                                <span>¥</span>
                                <span class="main-price">{{price}}</span>
                            </span>
                {{/if}}
                {{#if marketPrice}}
                <del class="text-xl light-grey" style="font-size: 10px;">
                    <span class="price-icon">原价¥</span>
                    <span class="font-num">{{marketPrice}}</span>
                </del>
                {{/if}}
            </p>
            <a class="www" style="color: #fff;"
               href="javascript:addCart('{{productId}}')">
                抢
            </a>
        </div>
    </a>
</li -->