<!doctype html>
<html>
<head>
[#include "/wap/include/resource-2.0.ftl"/]
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>特价商品</title>
    <style type="text/css">
        .www {
            position: absolute;
            right: 0;
            bottom: 0.5em;
            background-color: #fc5843;
            color: #fff;
            padding: 5px 12px;
            border-radius: 5px 0 0 5px;
        }
        .order-com-R{
            margin: 0em 0.5em;
        }
        .displaybox .d-price{
            margin-top: 0.4em;
        }
        .displaybox .d-price .shop-price{
            display: block;
            font-size: 18px;
            line-height: 1;
        }
    </style>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        [#include "/wap/include/product_promotion.ftl"]
        {{/each}}
    </script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="empty-for-fixedtop_tab">

    </div>
    <div class="am-g">
        <div class="am-g">
            <div id="silder" class="silder" style="position: relative;">
                <img src="${base}/resources/wap/2.0/images/blank.png" alt="load.."/>
            </div>
        </div>
        <ul class="am-g am-avg-3 shop-items" id="Goods">
            <li data-id=""><a href="javascript:;">全部</a></li>
            <li data-id="buyfree"><a href="javascript:;">买赠</a></li>
            <li data-id="seckill"><a href="javascript:;">折扣</a></li>
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
            $("#pullUpLabel").html("上拉刷新");
            $type = $(this).attr("data-id");
            ajaxGet({
                url: '${base}/wap/product/findRateGoods.jhtml',
                data: {
                    type: $type,
                    pageSize: 10,
                    pageNumber: pageN,
                    productCategoryId:'${productCategoryId}'

                },
                success: function (data) {
                    pageN++;
                    if (data == null || data.message.type != "success") {
                        $('#goodsList').html("<div style='line-height: 50px;text-align: center;'>没有相关记录</div>");
                    } else {
                        $('#goodsList').html(newGood(data.data));
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
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
                url: "${base}/wap/product/findRateGoods.jhtml",
                data: {
                    type: $type,
                    pageSize: 10,
                    pageNumber: pageN,
                    productCategoryId:'${productCategoryId}'
                },
                success: function (data) {
                    pageN++;
                    if (data == null || data.message.type != "success") {
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

    function addCart(id) {
        ajaxPost({
            url: "${base}/app/b2b/cart/add.jhtml",
            data: {
                id: id,
                type: 'gouwuc',
                quantity: 1
            },
            success: function (data) {
                if (data.message.type == 'success') {
                    showToast({content: '抢购成功，请前往购物车结账'});
                } else {
                    showToast2({content: data.message.content});
                }
            }
        });
    }

</script>
</body>
</html>
