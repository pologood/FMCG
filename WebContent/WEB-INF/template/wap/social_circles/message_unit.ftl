<div  id="message-tip" style="display: none;">
    <div class="weui_mask" onclick="showDetailDialog($('#message-tip'));" style="background: rgba(0,0,0,.1);"></div>
    <div class="weui_dialog chose_goods" style="width: 100%;z-index: 1;bottom: 45px;">
        <div class="weui_cell " style="background-color: white;">
            <div class="weui_cell_hd defaults radius-left"><label class="weui_label">回复</label></div>
            <div class="weui_cell_bd weui_cell_primary defaults radius-right">
                <input class="weui_input" id="contactValue" type="text" placeholder="" >
                <input class="weui_input" id="contactId" type="text" hidden>
            </div>
            <div class="weui_cell_ft" style="margin-left: 10px;">
                <a href="javascript:publish('${base}','${member.id}',$('#contactId').val(),$('#contactValue').val(),'1');" class="weui_btn weui_btn_mini weui_btn_default" style="display: block;">发表</a>
            </div>
        </div>
    </div>
</div>