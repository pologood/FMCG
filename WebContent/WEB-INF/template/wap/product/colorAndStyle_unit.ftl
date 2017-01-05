<!-- 选择商品颜色和尺寸 -->
<div class="weui_dialog_alert" id="product_details" style="display: none;">
    <div class="weui_mask" onclick="showDetailDialog($('#product_details'));"></div>
    <div class="weui_dialog chose_goods" style="width: 100%;">
        <div class="weui_cells cl" style="overflow: initial;margin-top:0;">
            <div class="weui_cell border-bt chose-title">
                <div class="weui_cell_hd lass-ab am-margin-horizontal-s">
                    <img id="thumbnail" alt="loading....">
                </div>
                <div class="weui_cell_hd lass-txt">
                    <p class="cl-rede font-large-1" id="price">￥190.00</p>
                    <p class="font-small_1">库存<span id="stock"></span>件</p>
                    <p class="font-small_1"><span>已选<label id="color"></label>&nbsp;&nbsp;<label id="spec"></label></span></p>
                </div>
                <div class="weui_cell_hd weui_cell_primary">
                </div>
                <a class="weui_cell_ft iconfont chose_goods-closebtn" href="javascript:showDetailDialog($('#product_details'));">&#xe601;
                </a>
            </div>
            <div class="weui_cell cl box_hd">
                <div class="weui_cell_hd">颜色分类：</div>
            </div>
            <div class="weui_cell cl border-bt box_bd">
                <div class="font-large weui_cell_hd kind-items" id="kind-items-color">

                </div>
            </div>
            <div class="weui_cell cl box_hd" id="kind-items-spec-label">
                <div class="weui_cell_hd">型号选择：</div>
            </div>
            <div class="weui_cell cl border-bt box_bd ">
                <div class="font-large weui_cell_hd kind-items" id="kind-items-spec">

                </div>
            </div>
            <div class="weui_cell cl border-bt">
                <div class="weui_cell_hd box_hd">购买数量：</div>
                <div class="weui_cell_hd weui_cell_primary"></div>
                <div class="weui_cell_ft ">
                    <form class="add_minus">
                        <i name="minus" class="opera-minus" onclick="selectNumber('minus')"></i>
                        <input type="text" value="1" class="opera-quantity" id="quantity" name="quantity" readonly>
                        <i name="add" class="opera-add" onclick="selectNumber('add')"></i>
                    </form>
                </div>
            </div>
            <div class="weui_cell cl">
                <input id="productId" hidden>
                <a href="javascript:addCart('','buy');" id="" class="font-default weui_btn weui_btn_warn3" style="width: 80%;">确认</a>
            </div>
        </div>
    </div>
</div>

<!-- END选择商品颜色和尺寸 -->