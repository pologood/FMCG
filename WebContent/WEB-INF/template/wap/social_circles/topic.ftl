<div class="weui_cells am-margin-xs">
    <div class="weui_cells cl am-margin-0 border-bt">
        <a class="weui_cell color-black" href="${base}/wap/social_circles/contact.jhtml?id={{id}}&memberId={{member.id}}">
            <div class="weui_cell_hd am-margin-right-xs" >
                <img src="{{member.headImg}}" class="showImg">
            </div>
            <div class="weui_cell_primary">
                <p class="font-large color-lired2">{{member.nickName}}</p>
                <p class="font-small light-gray">{{timeFormatUtil createDate}}</p>
            </div>
            <div class="weui_cell_bd"
                 style="border-left:1px solid #cecece;text-align:center; padding:0 0.2rem;line-height:1.2;padding-left: 0.8rem;">
                <p class="font-large">{{hits}}</p>
                <p class="font-small light-gray">阅读</p>
            </div>
        </a>
        {{#if productImages}}
        <a class="weui_cell" href="${base}/wap/social_circles/comment.jhtml?id={{id}}" >
            {{#each productImages}}
            {{#expression @index '==' '0'}}
            <div class="sj-items-pic weui_cell_primary" >
                <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png" data-original="{{large}}" alt="">
            </div>
            {{/expression}}
            {{/each}}
        </a>
        {{/if}}
        <div class="weui_cell">
            <div class="weui_cell_bd font-small_1">
                <span class="tyrs vertimid">TA已入手</span>
                <span class="font-small vertimid">{{content}}</span>
            </div>
        </div>
        <ul class="weui_cell sj-items-title">
            <li onclick="operation('${base}',$(this),'zan',{{id}})"
                {{#if praises}}
                {{#each praises}}
                {{#expression '${member.id}' '==' id }} style="color:red;" {{/expression}}
                {{/each}}
                {{/if}}
                >
                <i class="iconfont vertimid">&#xe620;</i>
                <span class="font-small vertimid">赞</span>
            </li>
            <li  onclick="operation('${base}',$(this),'comment',{{id}})">
                <i class="iconfont vertimid">&#xe679;</i>
                <span class="font-small vertimid">评论</span>
            </li>
            <li id="showActionSheet" onclick="operation('${base}',$(this),'fenxiang',{{id}})">
                <i class="iconfont vertimid">&#xe643;</i>
                <span class="font-small vertimid">分享</span>
            </li>
        </ul>
    </div>
</div>

