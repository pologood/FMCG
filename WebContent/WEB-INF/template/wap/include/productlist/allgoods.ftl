{{!新品上市(新品速递)}}
{{!单列}}
{{#compare column 1}}
    <div class="ls_product ls_product_new col-1">
        {{#with datas}}
            {{#each this}}
            <div class="box_product">
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
                            <b class="ft-bs2 clr-red05 price">￥{{price}}</b>
                            {{#expression wholePrice '>' price}}
                                <del class="ft-bs0 clr-grey03 marketprice">￥{{wholePrice}}</del>
                            {{/expression}}
                        </div>
                        <div class="weui_cell_ft clr-red05 ft-bs0">
                            {{#if praisedegree}}
                                <span class="praisedegree">好评度&nbsp;{{praisedegree}}</span>
                            {{/if}}                            
                        </div>
                    </div>
                </a>    
            </div>
            {{/each}}
        {{/with}}
    </div>
{{/compare}}
{{!两列}}
{{#compare column 2}}
    {{#unless addition}}
    <div class="ls_product ls_product_new cf col-2">
    {{/unless}}
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
                            <b class="ft-bs2 clr-red05 price">￥{{price}}</b>
                            {{#expression wholePrice '>' price}}
                                <del class="ft-bs0 clr-grey03 marketprice">￥{{wholePrice}}</del>
                            {{/expression}}
                        </div>
                        <div class="weui_cell_ft clr-red05 ft-bs0">
                            {{#if praisedegree}}
                                <span class="praisedegree">好评度&nbsp;{{praisedegree}}</span>
                            {{/if}}                            
                        </div>
                    </div>
                </a>    
            </div>
            {{/each}}
        {{/with}}
    {{#unless addition}}
    </div>
    {{/unless}}
{{/compare}}

