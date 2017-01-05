<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>搜索商品</title>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/lib.js"></script>
    <script type="text/x-handlebars-template" id="wap-product-item">
        {{#each this}}
        [#include "/wap/include/product.ftl"/]
        {{/each}}
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">

    <div style="width:100%;height:45px;background-color:#fff;"></div>
    <!-- 支撑因搜索框等脱离文档流缺失的高度-->
    <div class="am-g">
        <div class="bd">
            <div style="position: fixed;top: 0;left: 0;z-index:1000;background-color: #efefef;width:100%;">
                <div class="weui_search_bar" id="search_bar">
                    <div class="weui_cell_bd weui_cell_primary defaults radius-left radius-right" style="background-color: white">
                        <input class="weui_input" id="search_input" type="text" placeholder="根据商品名称搜索">
                    </div>
                    <div class="weui_cell_ft" style="margin-left: 10px;">
                        <a href="javascript:;" class="weui_btn weui_btn_mini weui_btn_default"
                           id="weui_icon_search">搜索</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="am-g" id="searchProduct">
        <ul class="am-g am-avg-3 shop-items" id="md-ul">
            <li data-id="weight"><a href="javascript:;">综合</a></li>
            <li data-id="salesDesc"><a href="javascript:;">销量</a></li>
            <li data-id="priceAsc"><a href="javascript:;">价格</a></li>
        </ul>
        <div class="am-g" style="background-color: #ffffff;">
            <ul class="am-gallery am-avg-2 am-gallery-overlay shop-tip-items" id="goodsList">
            </ul>
        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'});
        var compiler = Handlebars.compile($("#wap-product-item").html());
        $("#goodsList").html("<div style='line-height: 50px;text-align: center;'>请输入您希望搜索到的商品</div>");

        $('#weui_icon_search').on('click', function () {
            var $searchValue = $('#search_input').val();

            if ($searchValue == null || $searchValue == '') {
                showToast2({content:'请先输入您想搜索的内容'});
                return;
            }
            ajaxGet({
                url: '${base}/app/b2b/product/list/${id}.jhtml',
                data: {
                    keyword: $searchValue,
                    pageNumber: 1,
                    pageSize: 10
                },
                success: function (data) {
                    if (data.data.length == 0) {
                        $("#goodsList").html("<div style='line-height: 50px;text-align: center;'>没有搜索到相关的商品</div>");
                    } else {
                        $("#goodsList").html(compiler(data.data));
                        $(".lazy").picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                },
                error: function (data) {
                    //alert(data);
                }
            });
        });

        $("#md-ul").find("li").on("click", function () {
            $("#md-ul").find("li a").removeClass("visited");
            $(this).find("a").addClass("visited");
            var $orderType = $(this).attr("data-id");
            var $searchValue = $('#search_input').val();
            if ($searchValue == null || $searchValue == '') {
                showToast2({content:'您需要先输入您想搜索的内容'});
                return;
            }
            ajaxGet({
                url: "${base}/app/b2b/product/list/${id}.jhtml",
                data: {
                    keyword: $searchValue,
                    orderType: $orderType
                },
                success: function (data) {
                    if (data.data.length == 0) {
                        $("#goodsList").html("<div style='line-height: 50px;text-align: center;'>没有相关的商品</div>");
                    } else {
                        $("#goodsList").html(compiler(data.data));
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                }
            });

            if ($orderType == 'priceAsc') {
                $(this).attr("data-id", 'priceDesc');
            } else if ($orderType == 'priceDesc') {
                $(this).attr("data-id", 'priceAsc');
            }
        });

    });
</script>
</body>
</html>