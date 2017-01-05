<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>社交圈首页</title>
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
    <div class="am-g" id="myShow"></div>
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
                type: 'index',
                pageSize: 10,
                pageNumber: 1
            },
            success: function (data) {
                $("#myShow").html(compiler(data.data.contact));
                $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wep/2.0/images/AccountBitmap-product.png'});
            }
        });
    });
</script>
</body>
</html>