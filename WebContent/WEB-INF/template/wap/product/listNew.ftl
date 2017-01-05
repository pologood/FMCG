<!DOCTYPE html>
<html lang="en">
<head>
    <title>${productCategory.name}</title>
[#include "/wap/include/resource-2.0.ftl"/]

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        [#include '/wap/include/product.ftl'/]
        {{/each}}
    </script>
</head>
<style>
    .shop-tip-items a {
        color: #333333;
    }

    .searchbox_state {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
        background-color: #e8eaea;
    }

    .searchbox_state .searchbox_state-ctn {
        padding-left: 0px;
        padding-right: 0px;
    }

    #pullUpLabel {
        width: 100%;
        line-height: 30px;
        text-align: center;
    }
</style>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="am-page">
        <div class="searchbox_state">
            <div class="am-margin-top-0 cl weui_cells ">
                <div class="weui_cell searchbox_state-ctn">
                    <a class="weui_cell_primary tp-jd" href="${base}/wap/top_search.jhtml?type=product">
                        <i class="iconfont" style="font-size:12px;">&#xe640;</i>
                        <span class="font-small">搜索</span>
                    </a>
                </div>
            </div>
            <ul class="am-g am-avg-3 shop-items border-bt" id="md-ul">
                <li data-id="weight"><a href="javascript:;">综合</a></li>
                <li data-id="salesDesc"><a href="javascript:;">销量</a></li>
                <li data-id="priceAsc"><a href="javascript:;">价格</a></li>
            </ul>
        </div>
        <div class="empty-for-fixedtop_tab"></div>
        <div class="am-g" style="background-color: white;">
            <ul class="am-gallery am-avg-2 am-gallery-overlay shop-tip-items" id="ul-page">
            </ul>
        </div>
        <div id="pullUpLabel">上拉刷新</div>
        <div class="empty-for-fixedbottom_tab PTLN"></div>
    </div>
    [#include "/wap/include/footer.ftl" /]
</script>
<script src="${base}/resources/wap/3.0/js/lib.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        //empty placeholder set it height
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".searchbox_state");
        fixedEleCopyHandler(".empty-for-fixedbottom_tab.PTLN", ".am-topbar-fixed-bottom");
        var compiler = Handlebars.compile($("#wap-list-item").html());
        var pageNum = 1;
        var pageSize = 15;
        var orderType = "";
        var isLoading = false;

        $("#md-ul").find("li").on("click", function () {
            pageNum = 1;
            $("#ul-page").html("");
            $("#pullUpLabel").html("数据加载中");
            $("#md-ul").find("li a").removeClass("visited");
            $(this).addClass("visited");
            orderType = $(this).attr("data-id");
            isLoading = true;
            ajaxGet({
                url: "${base}/app/b2c/product/list.jhtml",
                data: {
                    orderType: orderType,
                    productCategoryId: '${productCategoryId}',
                    pageNumber: pageNum,
                    pageSize: pageSize
                },
                success: function (dataBlock) {
                    pageNum++;
                    $("#pullUpLabel").html("上拉刷新！");
                    if (dataBlock.data.length == 0) {
                        //$("#ul-page").html("<div style='line-height: 50px;text-align: center;'>没有相关记录</div>");
                        $("#pullUpLabel").html("没有相关记录");
                    } else {
                        $("#ul-page").html(compiler(dataBlock.data));
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                    }
                    isLoading = false;
                }
            });

            if (orderType == 'priceAsc') {
                $(this).attr("data-id", 'priceDesc');
            } else if (orderType == 'priceDesc') {
                $(this).attr("data-id", 'priceAsc');
            }
        });

        $("#md-ul").find("li").eq(0).trigger("click");

        scroll(function () {
            if (!isLoading) {
                isLoading = true;
                ajaxGet({
                    url: "${base}/app/b2c/product/list.jhtml",
                    data: {
                        orderType: orderType,
                        productCategoryId: '${productCategoryId}',
                        pageNumber: pageNum,
                        pageSize: pageSize
                    },
                    success: function (dataBlock) {
                        pageNum++;

                        if (dataBlock.data == null || dataBlock.data.length == 0) {
                            $("#pullUpLabel").html("亲，到底了！！！");
                            return;
                        }

                        $("#pullUpLabel").html("上拉刷新！");
                        if (dataBlock.data.length < 15) {
                            $("#pullUpLabel").html("亲，到底了！！！");
                        }

                        $("#ul-page").append(compiler(dataBlock.data));
                        $('.lazy').picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                        });
                        isLoading = false;
                    }
                });
            }

        });
    });
</script>
</body>
</html>
