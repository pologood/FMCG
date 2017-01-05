<li>
    <a href="${base}/wap/product/content/{{id}}/product.jhtml">
        <div class="am-gallery-item am-gallery-item-default" style="background: #fff;padding: 4px;">
            <div style="text-align:center;vertical-align:middle;overflow:hidden;">
                <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                     data-original="{{thumbnail}}" alt="拼命加载中"/>
            </div>
            <p class="font-small fillme_blanks" style="text-overflow:ellipsis;overflow:hidden;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;line-height:1.5em;height:3em;margin: 0 0.3em;">
                {{fullName}}
            </p>
            <div class="am-cf price_and_tags" style="margin: 0 0.3em;">
                <p class="am-fl d-price">
                    <span class="shop-price red" style="margin-right:2px;">
                        <span class="ft-bs3">¥{{price}} </span>
                    </span>
                    <!--{{#if wholePrice}}
                    < del class="light-grey">
                        <span class="ft-bs2">¥{{wholePrice}}</span>
                    </del>
                    {{/if}}-->
                </p>
                {{#if tags}}
                {{#each tags}}
                {{#if id}}
                    {{#compare id '1'}}
                    <span class="am-fr product_tags TTI" style="background-color:#ff6670 ;">{{name}}</span>
                    {{/compare}}
                    {{#compare id '2'}}
                    <span class="am-fr product_tags TTI" style="background-color:#ffab66 ;">{{name}}</span>
                    {{/compare}}
                    {{#compare id '5'}}
                    <span class="am-fr product_tags TTI" style="background-color:#ffcf40 ;">{{name}}</span>
                    {{/compare}}
                {{/if}}
                {{/each}}
                {{/if}}
            </div>
        </div>
    </a>
</li>
