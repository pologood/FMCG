[#-- 红：#ff635b;紫：#c183e2;粉：#ff6670;黄：#fcd04c--]
[#-- 该页面中使用的优惠图标颜色色值--]
<a class="weui_cells box_tenant col-1" href="${base}/wap/tenant/index/{{id}}.jhtml">
    [#-- box_tenant-info--]
    <div class="weui_cell box_tenant-info">
        <div class="weui_cell_hd">
            <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"
                 data-original="{{thumbnail}}"
                 alt="{{fullName}}">
        </div>
        <div class="weui_cell_bd weui_cell_primary ">
            <p class="storename ft-bs15">
                <span>{{shortName}}</span>
                <i class="iconfont clr-grey11 ft-bs2">&#xe635;</i>
            </p>
            [#-- 评分等级--]
            {{#expression grade '>=' 0}}
                <div class="starlevelsR">
                    <div class="cf ft-bs0 starlevels" data-starnum="{{grade}}" data-starlevelized="false">
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                    </div>
                    {{#if grade}}
                        <span class="ft-bs0 clr-red05">{{grade}}分</span>
                    {{/if}}
                </div>            
            {{/expression}}
            <div class="weui_cell mainAaddress">
                <div class="weui_cell_hd ft-bs0 mainbusiness">
                    主营：{{tenantCategoryName}}                    
                </div>
                <div class="weui_cell_bd weui_cell_primary ft-bs0 address">
                    {{#if businesscircle}}
                        {{businesscircle}}&nbsp;{{#formatdistance distance}}{{/formatdistance}}
                    {{else}}
                        &nbsp;{{#formatdistance distance}}{{/formatdistance}}
                    {{/if}}
                </div>
            </div>
        </div>
    </div>
    [#-- box_tenant-promotions--]
    {{#if promotions}}
    <div class="weui_cell box_tenant-promotions">
        <div class="weui_cell_hd">
        </div>
        <div class="weui_cell_bd weui_cell_primary ThumbIconLeft ">
            {{#each promotions}}
            <p class="promdesc">
                {{#if type}}
                    <i class="iconfont ft-bs05 promtag pt-{{type}}"></i>
                {{/if}}
                <span class="ft-bs0 clr-grey03">{{name}}.</span>
                {{#expression ../promotions.length ">=" 3}}
                    {{#expression @index "==" 0}}
                    <!-- <a href="javascript:;" class="togglebtn"> <i class="iconfont">&#xe659;</i></a>-->
                    {{/expression}}
                {{/expression}}
            </p>
            {{/each}}
        </div>
    </div>
    {{/if}}
</a>
