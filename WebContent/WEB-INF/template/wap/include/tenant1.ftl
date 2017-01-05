[#-- 红：#ff635b;紫：#c183e2;粉：#ff6670;黄：#fcd04c--]
[#-- 该页面中使用的优惠图标颜色色值--]
<div class="weui_cells weui_cells_access box_tenantinfo col-1">
    <a class="weui_cell" href="${base}/wap/tenant/index/{{id}}.jhtml">
        <div class="weui_cell_hd box_tenantinfo-hd">
            <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"
                 data-original="{{thumbnail}}"
                 alt="{{fullName}}">
        </div>
        <div class="weui_cell_bd weui_cell_primary box_tenantinfo-bd">
            <p class="storename">{{shortName}}</p>
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

            <p class="mainbusiness">
                主营：{{tenantCategoryName}}
            </p>
            <p class="address">
                <span><i class="iconfont">&#xe623;</i>{{address}}</span>
                <b>{{#formatdistance distance}}{{/formatdistance}}</b>
            </p>
        </div>
    </a>
    {{#if promotions}}
    <div class="weui_cell youhuiquanShow" id="youhuiquanShow">
        <div class="weui_cell_bd" style="width:80px;margin-right:5px;"></div>
        <div class="weui_cell_bd weui_cell_primary ThumbIconLeft">
            {{#each promotions}}
            <p class="bg-default font-default am-margin-right-xs" style="color:#666;">
                {{#if type}}
                    <i class="iconfont ft-bs0 promtag pt-{{type}}"></i>
                {{/if}}
                <span class="ft-bs0">{{name}}.</span>
                {{#expression ../promotions.length ">=" 3}}
                    {{#expression @index "==" 0}}
                    <a href="javascript:;" class="togglebtn"> <i class="iconfont">&#xe659;</i></a>
                    {{/expression}}
                {{/expression}}
            </p>
            {{/each}}
        </div>
    </div>
    {{/if}}
</div>
