<!--BEGIN dialog1-->
<div class="weui_dialog_confirm" id="dialog1" style="display: none;">
    <div class="weui_mask"></div>
    <div class="weui_dialog">
        <div class="weui_dialog_hd"><strong class="weui_dialog_title"></strong></div>
        <div class="weui_dialog_bd"></div>
        <div class="weui_dialog_ft">
            <a href="javascript:;" class="weui_btn_dialog default" id="idNo">取消</a>
            <a href="javascript:;" class="weui_btn_dialog primary" id="idOk">确定</a>
        </div>
    </div>
</div>
<!--END dialog1-->
<!--BEGIN dialog2-->
<div class="weui_dialog_alert" id="dialog2" style="display: none;">
    <div class="weui_mask"></div>
    <div class="weui_dialog">
        <div class="weui_dialog_hd"><strong class="weui_dialog_title"></strong></div>
        <div class="weui_dialog_bd"></div>
        <div class="weui_dialog_ft">
            <a href="javascript:;" class="weui_btn_dialog primary">确定</a>
        </div>
    </div>
</div>
<!--END dialog2-->

<!--BEGIN toast-->
<div id="toast" style="display: none;">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <i class="weui_icon_toast"></i>
        <p class="weui_toast_content">已完成</p>
    </div>
</div>
<!--end toast-->

<!-- loading toast -->
<div id="loadingToast" class="weui_loading_toast" style="display:none;">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div>
<!--end loading toast-->
<!--BEGIN password-->
<div class="weui_dialog_confirm" id="password" style="display: none;">
    <div class="weui_mask"></div>
    <div class="weui_dialog">
        <div class="weui_dialog_hd"><strong class="weui_dialog_title">支付密码</strong></div>
        <div class="weui_dialog_bd am-text-center color-black">
            <div class="am-text-danger font-large-3"><i class="iconfont icon-rmb"></i><span
                    id="payableAmount">9.39</span></div>
            <div class="light-grey am-divider-default">
                <div class="am-g am-padding-vertical-xs am-fl">
                                <span class="am-fl">余额:
                                </span>
                    <i class="iconfont icon-right am-fr"></i>
                </div>
            </div>
            <form action="#" class="am-g am-divider-default am-fl am-padding-vertical-sm ">
                <input type="password" placeholder="" class="pay-input" maxlength="6">
            </form>
        </div>
        <div class="weui_dialog_ft am-g ">
            <a href="javascript:;" class="weui_btn_dialog default" id="idNo">取消</a>
            <a href="javascript:;" class="weui_btn_dialog primary" id="idOk">确定</a>
        </div>
    </div>
</div>
<!--END password-->
<!--BEGIN actionSheet-->
<div id="actionSheet_wrap">
    <div class="weui_mask_transition" id="mask"></div>
    <div class="weui_actionsheet" id="weui_actionsheet">
        <div class="weui_actionsheet_menu">
            <div class="weui_cells">
                <div class="weui_cells_title am-text-center">选择</div>
                <div class="weui_cells weui_cells_radio">
                </div>
            </div>
        </div>
        <div class="weui_actionsheet_action">
            <div class="weui_actionsheet_cell bg-safe color-revert" id="actionsheet_cancel">取消</div>
        </div>
    </div>
</div>
<!--END actionSheet-->

<!--BEGIN toast-->
<div id="toast2" style="display: none;">
    <div class="weui_mask_transparent"></div>
    <div style="width:80%;margin:10px;background: rgba(40, 40, 40, 0.75);color: #FFFFFF;text-align:center;top:39%;position:fixed;left:7%;height:50px;;line-height:50px;font-size:15px;border-radius:5px;z-index:1000;">
        <p class="weui_toast_content" id="message">错误提示信息</p>
    </div>
</div>
<!--end toast-->