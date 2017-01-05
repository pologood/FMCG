<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>社交圈消息</title>
    <style type="text/css">

    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <div class="weui_cells">
            <div class="weui_cell">
                <a class="weui_cell_hd" href="${base}/wap/social_circles/contact.jhtml?id={{id}}&memberId={{member.id}}">
                    <img src="{{member.headImg}}" class="showImg" >
                </a>
                <a class="weui_cell_primary" href="${base}/wap/social_circles/contact.jhtml?id={{id}}&memberId={{member.id}}">
                    <p class="font-large col">{{member.nickName}}</p>
                    <p class="font-small light-gray">{{timeFormatUtil createDate}}</p>
                </a>
                <a class="weui_cell_bd light-gray"  style="text-align:center;width: 45px;border: 1px solid #999999;border-radius: 4px;" href="javascript:callBackMessage({{id}});">
                    <span class="font-small ">回复</span>
                </a>
            </div>
            <div class="weui_cells" style="margin-top: 0px;">
                <div class="weui_cell">
                    <span>回复 <a style="color: #b2e2fa;">{{forContact.member.nickName}}</a>:{{content}}</span>
                </div>
                {{#if forContact}}
                <div class="weui_panel_bd">
                    <a href="javascript:void(0);" class="weui_media_box weui_media_appmsg bg-f7f7f7">
                        {{#if forContact.productImages}}
                        {{#each forContact.productImages}}
                        {{#expression @index '==' 0}}
                        <div class="weui_media_hd">
                            <img class="lazy weui_media_appmsg_thumb" data-original="{{thumbnail}}" src="${base}/resources/wap/2.0/images/daisy.jpg" alt="">
                        </div>
                        {{/expression}}
                        {{/each}}
                        {{/if}}
                        <div class="weui_media_bd">
                            <h4 class="weui_media_title">{{forContact.member.nickName}}</h4>
                            <p class="weui_media_desc">{{forContact.content}}</p>
                        </div>
                    </a>
                </div>
                {{/if}}
            </div>
        </div>
        {{/each}}
        [#include "/wap/social_circles/message_unit.ftl"/]
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div id="messageList"></div>

    <div id="callBackMessage" class="weui_cell " style="background-color: white;display: none;">
        <div class="weui_cell_hd defaults radius-left"><label class="weui_label">回复</label></div>
        <div class="weui_cell_bd weui_cell_primary defaults radius-right">
            <input class="weui_input" type="number" placeholder="">
        </div>
        <div class="weui_cell_ft" style="margin-left: 10px;">
            <a href="javascript:;" class="weui_btn weui_btn_mini weui_btn_default" style="display: block;">发表</a>
        </div>
    </div>

    [#include "/wap/social_circles/publish_unit.ftl"/]
    [#include "/wap/social_circles/footer.ftl"/]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script src="${base}/resources/wap/2.0/js/operation.js"></script>
<script>
    $(function () {
        init();

        var compiler = Handlebars.compile($("#wap-list-item").html());
        ajaxGet({
            url: "${base}/app/member/contact/message.jhtml",
            data: {
                id: '${member.id}',
                pageSize: 10,
                pageNumber: 1
            },
            success: function (data) {
                $('#messageList').html(compiler(data.data));
                $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});
            }
        });
    });

    function callBackMessage(id){
        $("#contactId").val(id);
        showDetailDialog($('#message-tip'));
    }
</script>
</body>
</html>