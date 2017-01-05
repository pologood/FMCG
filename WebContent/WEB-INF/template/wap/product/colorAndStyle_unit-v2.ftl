{{!选择颜色和规格,v2.0}}
<div class="clrsize">
    {{!仪表盘}}
    <div class="weui_cell clrsize-board">
        <div class="weui_cell_hd">
            <div class="imgrap">
                <img jhk="imgthumb" src="${base}/resources/wap/3.0/image/placeholder/product.png" alt="">    
            </div>
        </div>        
        <div class="weui_cell_bd weui_cell_primary">
            <p class="ft-bs3 clr-red01">￥<span jhk="price">0</span></p>
            <p class="ft-bs0 clr-grey02">库存<span jhk="stock">0</span>件</p>
            <p class="ft-bs0 clr-grey02"><span jhk="chosenconds-desc">已选：</span>&nbsp;&nbsp;<span class="clr-red05" jhk="chosenconds"></span></p>
        </div>
    </div>
    {{!操作区}}
    <div class="clrsize-opera">
        {{!筛选器}}
        {{#each filters_produced}}
            <div class="condn {{this.property}}s" data-filter-property="{{this.property}}">
                <p class="ft-bs15 condn-hd">{{this.description}}：</p>
                <div class="condn-bd">
                    {{#each this.content}}
                        <a href="javascript:;" class="condn-btn ft-bs1" jhk="condn-btn">{{this}}</a>    
                    {{/each}}
                </div>
            </div>            
        {{/each}}
        {{!购买数量}}
        <div class="weui_cell quantity">
            <div class="weui_cell_bd weui_cell_primary">
                <span class="ft-bs15">购买数量：</span>
            </div>
            <div class="weui_cell_ft ft-bs0 addminus">
                <span class="iconfont clr-grey02" jhk="opera_minus">&#xe690;</span>
                <b class="clr-grey01">1</b>
                <span class="iconfont clr-grey02" jhk="opera_add">&#xe601;</span>
            </div>
        </div>
        {{!确定按钮}}
        <div class="sureR">
            <a href="javascript:;" class="weui_btn weui_btn_warn sure ft-bs1 bg-11">确定</a>    
        </div>
    </div>
</div>
