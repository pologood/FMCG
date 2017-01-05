<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>TA发布的秀秀</title>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-topic-item">
        {{#each this}}
        [#include "/wap/social_circles/topic.ftl"/]
        {{/each}}
    </script>
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
                <div class="userInfor">
                    <div class="headBox">
                        <img src="${base}/resources/wap/2.0/images/AccountBitmap-head.png" data-original="${owner.headImg}"
                             class="lazy" style="width: 100%;height: 100%;">
                    </div>
                    <span>${member.nickName}</span>
                    <span>关注${concern} | 粉丝${fans} | <label id="">+关注</label>  </span>
                </div>
        </div>
        <div class="weui_cell font-large" style="background-color:#fff;text-align: center;">
            <div class="weui_cell_hd" style="color:#333;" >
                TA发布的秀秀
            </div>
        </div>
    </div>
    <div class="am-g" id="myShow">

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
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wep/2.0/images/AccountBitmap-product.png'});
        var topic = Handlebars.compile($("#wap-topic-item").html());
        ajaxGet({
            url: '${base}/app/member/contact/my/list.jhtml',
            data: {
                id: '${owner.id}',
                type: 'topic',
                pageSize: 10,
                pageNumber: 1
            },
            success: function (data) {
                $("#myShow").html(topic(data.data.contact));
                $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wep/2.0/images/AccountBitmap-product.png'});
            }
        });
    });
</script>
</body>
</html>