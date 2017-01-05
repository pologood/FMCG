<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>我的咨询</title>
    <style type="text/css">
        .showImgs {
            width: 5rem;
            height: 5rem;
            border-radius: 50%;
            margin-right: 10px;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">

        </script>
</head>
<body style="background-color: white">
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <a href="${base}/wap/tenant/consulting/18655953621.jhtml" class="weui_cell" style="border-bottom: solid 1px #e8eaea;">
        <div class="weui_cell_hd">
            <img src="${base}/resources/wap/2.0/images/AccountBitmap-head.png" class="showImgs"/>
        </div>
        <div class="weui_cell_bd weui_cell_primary">
            <p class="font-large color-lired2">哈喽</p>
            <p class="font-small light-gray">123122312312312312312312</p>
        </div>
        <div class="weui_cell_ft">
            <p class="font-large color-lired2">&nbsp;</p>
            <p class="font-small light-gray">2016-7-6 21:00</p>
        </div>
    </a>

    [#include "/wap/include/footer.ftl"/]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'
        });
        var compiler = Handlebars.compile($("#wap-list-item").html());
        $("#am_g").html(compiler(""));
        //console.log(data.length);
    });
</script>
</body>
</html>