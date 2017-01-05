<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>${pos}:${member.displayName}</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        [#list messages as message]
        <div class="weui_cell" >
            <div class="weui_cell_hd">
                <img src="${message.sender.headImg}" class="showImg">
            </div>
            <div class="weui_cell_primary">
                <p class="font-large color-lired2">${message.sender.nickName}</p>
                <p class="font-small light-gray">{{timeFormatUtil '${message.createDate?string("yyyy-MM-dd HH:mm:ss")}'}}</p>
            </div>
        </div>
        <div class="weui_cell cl">
            <div class="weui_cell_hd showImg"></div>
            <div class="weui_cell_primary">
                <span>${message.content}</span>
            </div>
        </div>
        [/#list]
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">

    <div class="weui_cells am-margin-xs" id="wapListItem">

    </div>
    <header id="callBackMessage" class="am-topbar am-topbar-fixed-bottom bg-default">
        <div class="weui_cell " style="background-color: white;">
            <div class="weui_cell_hd defaults radius-left"><label class="weui_label">咨询:</label></div>
            <div class="weui_cell_bd weui_cell_primary defaults radius-right">
                <input class="weui_input" id="contactValue" type="text" placeholder="">
                <input class="weui_input" id="contactId" type="text"  hidden>
            </div>
            <div class="weui_cell_ft" style="margin-left: 10px;">
                <a href="javascript:;" id="consultiing" class="weui_btn weui_btn_mini weui_btn_default" style="display: block;">发送</a>
            </div>
        </div>
    </header>
    <!-- BEGIN empty div for fixed ele -->
    <div class="empty-for-fixedbottom_tab"></div>
    <!-- END empty div for fixed ele -->
</script>
<!-- share_maskbox-->

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/operation.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
        });
        var compiler = Handlebars.compile($("#wap-list-item").html());
        $("#wapListItem").html(compiler(''));


        $("#consultiing").click(function () {
            var _contactValue = $("#contactValue").val().trim();

            if(_contactValue==null||_contactValue==""){
                showToast2({content:"请先输入内容。。。"});
                return false;
            }


            $("#wapListItem").append("<div>"+_contactValue+"<\/div>");


            ajaxPost({
                url: "${base}/wap/tenant/consult.jhtml",
                data:{
                    content:_contactValue,
                    receiver:"${receiver}"
                },
                success: function (data) {
                    if(data.message.type='success'){
                        showToast(data.message.content);
                    }else {
                        showToast2(data.message.content);
                    }
                }
            });
        });
    });

</script>
</body>
</html>