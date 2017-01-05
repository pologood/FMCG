<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>社交圈发现</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
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
    <div class="am_g">
        <div class="weui_cell border-bt">
            <div class="weui_cell_bd font-large">
                <i class="font-large color-lired iconfont">&#xe616;</i>
                同城热门
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

        var compiler = Handlebars.compile($("#wap-list-item").html());
        ajaxGet({
            url: '${base}/app/member/contact/list.jhtml',
            data: {
                id: '${member.id}',
                type: 'discover',
                pageSize: 10,
                pageNumber: 1
            },
            success: function (data) {
                $("#myShow").html(compiler(data.data.contact));
                $(".lazy").picLazyLoad({
                    threshold: 100,
                    placeholder: '${base}/resources/web/2.0/images/AccountBitmap.png'
                });
            }
        });
    });
</script>
</body>
</html>