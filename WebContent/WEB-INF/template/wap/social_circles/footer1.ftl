
<header id="callBackMessage" class="am-topbar am-topbar-fixed-bottom bg-default">
    <div class="weui_cell " style="background-color: white;">
        <div class="weui_cell_hd defaults radius-left"><label class="weui_label">回复</label></div>
        <div class="weui_cell_bd weui_cell_primary defaults radius-right">
            <input class="weui_input" id="contactValue" type="text" placeholder="">
            <input class="weui_input" id="contactId" type="text"  hidden>
        </div>
        <div class="weui_cell_ft" style="margin-left: 10px;">
            <a href="javascript:publish('${base}',[#if member??&&member?has_content]'${member.id}'[#else]''[/#if],$('#contactId').val(),$('#contactValue').val(),'0');" class="weui_btn weui_btn_mini weui_btn_default" style="display: block;">发表</a>
        </div>
    </div>
</header>
<!-- BEGIN empty div for fixed ele -->
<div class="empty-for-fixedbottom_tab"></div>
<!-- END empty div for fixed ele -->