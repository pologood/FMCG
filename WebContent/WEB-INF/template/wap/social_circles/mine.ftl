<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>社交圈我的</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-topic-item">
        {{#each this}}
        [#include "/wap/social_circles/topic.ftl"/]
        {{/each}}
    </script>

    <script type="text/x-handlebars-template" id="wap-magic-item">
        {{#each this}}
        <div class="weui_cells am-margin-xs">
            <div class="weui_cells cl am-margin-0 border-bt">
                {{#if productImages}}
                <div class="weui_cell">
                    {{#each productImages}}
                    {{#expression @index '==' '0'}}
                    <div class="sj-items-pic weui_cell_primary">
                        <img class="lazy" src="${base}/resources/wap/2.0/images/c.jpg" data-original="{{large}}" alt="">
                    </div>
                    {{/expression}}
                    {{/each}}
                </div>
                {{/if}}
                <div class="weui_cell">
                    <div class="weui_cell_bd font-small_1">
                        {{content}}
                    </div>
                </div>
                <ul class="weui_cell sj-items-title">
                    <li>
                        <i class="iconfont">&#xe672;</i>
                        <span>编辑</span>
                    </li>
                    <li>
                        <i class="iconfont">&#xe643;</i>
                        <span>分享</span>
                    </li>
                </ul>
            </div>
        </div>
        {{/each}}
    </script>
    <style type="text/css">
    div.userInfor {
        background-image: url(${base}/resources/wap/2.0/images/headerbg.png);
        width: 100%;
        height: auto;
        padding: 10px 0;
    }
    </style>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="empty-for-fixedtop_tab">

    </div>
    <div class="am-g fixedtop_tab">
        <div class="weui_cells" style="margin-top:0;">
            <a href="${base}/wap/member/index.jhtml">
                <div class="userInfor">
                    <div class="headBox">
                        <img src="${base}/resources/wap/2.0/images/AccountBitmap-head.png" data-original="${member.headImg}"
                             class="lazy" style="width: 100%;height: 100%;">
                    </div>
                    <span>${member.nickName}</span>
                    <span>关注${concern} | 粉丝${fans}</span>
                </div>
            </a>
        </div>
        <div class="weui_cell cl font-large" style="background-color:#fff;text-align: center;">
            <a class="weui_cell_primary" href="javascript:" id="myTopic">
                我的话题
            </a>
            <a class="weui_cell_primary" href="javascript:"  id="myMagicShow">
                我的魔拍秀
            </a>
        </div>
    </div>
    <div class="am-g" id="myShow">

    </div>
    [#include "/wap/social_circles/publish_unit.ftl"/]
    [#--include "/wap/social_circles/footer.ftl"/--]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script src="${base}/resources/wap/2.0/js/operation.js"></script>
<script>
    $(function () {
        init();
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'});
        var topic = Handlebars.compile($("#wap-topic-item").html());
        var magic = Handlebars.compile($("#wap-magic-item").html());
        $("#myTopic").on('click', function () {
            $(this).css("color","#ff6d06");
            $("#myMagicShow").css("color","#333");
            ajaxGet({
                url: '${base}/app/member/contact/my/list.jhtml',
                data: {
                    id: '${member.id}',
                    type: 'topic',
                    pageSize: 10,
                    pageNumber: 1
                },
                success: function (data) {
                    if(data.message.type=="success"){
                        $("#myShow").html(topic(data.data.contact));
                        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'});
                    }
                }
            });
        });
        $("#myTopic").trigger("click");
        $("#myMagicShow").on('click', function () {
            $(this).css("color","#ff6d06");
            $("#myTopic").css("color","#333");
            ajaxGet({
                url: '${base}/app/member/contact/my/list.jhtml',
                data: {
                    id: '${member.id}',
                    type: 'camera',
                    pageSize: 10,
                    pageNumber: 1
                },
                success: function (data) {
                    if(data.message.type=="success") {
                        $("#myShow").html(magic(data.data.contact));
                        $(".lazy").picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                }
            });
        });
    });

</script>
</body>
</html>