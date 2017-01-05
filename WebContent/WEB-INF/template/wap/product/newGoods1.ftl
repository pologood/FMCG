<!doctype html>
<html>
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>新品上市</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        [#include "/wap/include/product.ftl"/]
        {{/each}}
    </script>
</head>
<body ontouchstart style="background: #eaeaea;">
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="empty-for-fixedtop_tab">

    </div>
    <div class="am-g ">
        <div class="am-g">
            <div id="silder" class="silder" style="position: relative;">
                <img src="${base}/resources/wap/2.0/images/blank.png" alt="load.."/>
            </div>
        </div>
        <ul class="am-g am-avg-3 shop-items" id="Goods">
            <li data-id=""><a href="javascript:;">全部</a></li>
            <li data-id="nowDate"><a href="javascript:;" id="nowDate"></a></li>
            <li data-id="nowWeek"><a href="javascript:;">本周上新</a></li>
        </ul>
    </div>
    <div class="am-g" style="background-color: #ffffff;">
        <ul class="am-gallery am-avg-2 am-gallery-overlay shop-tip-items" id="goodsList">

        </ul>
    </div>
    <div class="am-g" style="width:100%;line-height:30px;text-align:center; background-color: white;"
         id="pullUpLabel">上拉刷新
    </div>
    [#include "/wap/include/footer.ftl"/]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script>
    $(function () {
        init();
        //fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        var ads = [];
        var i=1;
    [@ad_position id=70 areaId=areaId  count=5]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
                [#if ads.path?has_content]
                    ads.push({content: '${ads.path}'});
                [#else ]
                    ads.push({content: '${base}/resources/wap/2.0/upload/no-'+i+'.png'});
                    i++;
                [/#if]
            [/#list]
        [/#if]
    [/@ad_position]
        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 800,
            animateType: 'rotate'
        });

        var $nowDate = $('#nowDate');
        var date = new Date();
        $nowDate.text(date.getMonth() + 1 + '月' + date.getDate() + '号');
        $('.lazy').picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });
        var newGood = Handlebars.compile($("#wap-list-item").html());

        var $type = '', pageN = 1;

        $("#Goods").find("li").on("click", function () {
            $("#Goods").find("li a").removeClass("visited");
            $(this).find("a").addClass("visited");

            pageN = 1;
            $type = $(this).attr("data-id");
            ajaxGet({
                url: '${base}/wap/product/findNewGoods.jhtml',
                data: {
                    type: $type,
                    pageSize: 10,
                    pageNumber: pageN,
                    productCategoryId:'${productCategoryId}'
                },
                success: function (data) {
                    pageN++;
                    if (data.data == null || data.data.length == 0) {
                        $('#pullUpLabel').html('没有相关记录');
                        $('#goodsList').html("");
                    } else {
                        $('#goodsList').html(newGood(data.data));
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/NoPicture.jpg'
                        });
                    }

                    if(data.data.length<=10){
                        $('#pullUpLabel').html('亲，已经到底了！！！');
                    }
                }
            });
        });
        $("#Goods").find("li").eq(0).trigger("click");
        $(window).unbind("scroll");
        scroll(function () {
            ajaxGet({
                url: "${base}/wap/product/findNewGoods.jhtml",
                data: {
                    type: $type,
                    pageSize: 10,
                    pageNumber: pageN,
                    productCategoryId:'${productCategoryId}'
                },
                success: function (data) {
                    pageN++;
                    if (data.data == null || data.data.length == 0) {
                        $('#pullUpLabel').html('亲，已经到底了！！！');
                        return;
                    } else {
                        $('#goodsList').append(newGood(data.data));
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                }
            });
            pageN++;
        });
    });
</script>
</body>
</html>
