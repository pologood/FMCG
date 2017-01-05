<!doctype html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <style type="text/css">
        #pullUpLabel {
            width: 100%;
            line-height: 30px;
            text-align: center;
        }
    </style>
    <title>[#if type == 'product']收藏的商品[#elseif type=='tenant']收藏的店铺[/#if]</title>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
[#if type == 'product']
    <script type="text/x-handlebars-template" id="wap-list-product">
        {{#each this}}
            [#include "/wap/include/product.ftl"/]
        {{/each}}
    </script>
[#elseif type=='tenant']
    <script type="text/x-handlebars-template" id="wap-list-tenant">
        {{#each this}}
            [#include "/wap/include/tenant.ftl"/]
        {{/each}}
    </script>
[/#if]

<script type="text/x-handlebars-template" id="wap-list-empty">
    <div style='line-height: 50px;text-align: center;'>{{infotip}}</div>
</script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    [#if type == 'tenant']
    <div class="weui_cells weui_cells_access" id="tenantList" style="margin-top:0px;">
    </div>
    [#elseif type=='product']
    <div class="am-g" style="background-color: #ffffff;" id="productListR">
        <ul class="am-gallery am-avg-2 am-gallery-overlay shop-tip-items" id="productList">
        </ul>
    </div>
    [/#if]
    <div id="pullUpLabel">上拉刷新</div>
    <div class="empty-for-fixedbottom_tab PTLN"></div>
</script>
<script src="${base}/resources/wap/3.0/js/lib.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
//        $('.lazy').picLazyLoad({threshold: 100, placeholder: 'images/blank.gif'});
        var html = "<div style='line-height: 50px;text-align: center;'>没有相关记录</div>";
        var emptyhtmlcompiler=Handlebars.compile($("#wap-list-empty").html());
        var pageNum=2;
        var pageSize=20;
        var isLoading=false;
        var type="${type}";
    [#if type='product']
        var myProduct = Handlebars.compile($("#wap-list-product").html());
        isLoading=true;
        ajaxGet({
            url: '${base}/app/member/favorite/product/list.jhtml',
            data:{pageSize:pageSize},
            success: function (data) {
                if (!data.data.length) {
                    $('.container').html(emptyhtmlcompiler({
                        infotip:"暂无收藏商品"
                    }));
                } else {
                    $("body").css('backgroundColor', '#eaeaea');
                    $('#productList').html(myProduct(data.data));
                    $('.lazy').picLazyLoad({threshold: 100, placeholder: 'images/blank.gif'});
                }
                isLoading=false;
            }
        });
    [#elseif type='tenant']
        var myTenant = Handlebars.compile($("#wap-list-tenant").html());
        isLoading=true;
        ajaxGet({
            url: '${base}/app/member/favorite/tenant/list.jhtml',
            data:{pageSize:pageSize},
            success: function (data) {
                if (!data.data.length) {
                    //$('#tenantList').html(html);
                    $('.container').html(emptyhtmlcompiler({
                        infotip:"暂无收藏店铺"
                    }));
                } else {
                    $('#tenantList').html(myTenant(data.data));
                    $('.lazy').picLazyLoad({threshold: 100, placeholder: 'images/blank.gif'});
                }
                isLoading=false;
            }
        });
    [/#if]

    scroll(function(){
        if(!isLoading) {
            isLoading = true;
            if (type == "product") {
                var myProduct = Handlebars.compile($("#wap-list-product").html());
                ajaxGet({
                    url: '${base}/app/member/favorite/product/list.jhtml',
                    data: {
                        pageNumber: pageNum,
                        pageSize:pageSize
                    },
                    success: function (dataBlock) {
                        pageNum++;
                        if (dataBlock.data == null || dataBlock.data.length == 0) {
                            $("#pullUpLabel").html("亲，到底了！！！");
                            return;
                        }
                        $("#pullUpLabel").html("上拉刷新！");
                        if (dataBlock.data.length < pageSize) {
                            $("#pullUpLabel").html("亲，到底了！！！");
                        }
                        $("#productList").append(myProduct(dataBlock.data));
                        $('.lazy').picLazyLoad({threshold: 100, placeholder: 'images/blank.gif'});
                        isLoading=false;
                    }
                });
            } else if (type == "tenant") {
                var myTenant = Handlebars.compile($("#wap-list-tenant").html());
                ajaxGet({
                    url: '${base}/app/member/favorite/tenant/list.jhtml',
                    data:{
                        pageNumber:pageNum,
                        pageSize:pageSize
                    },
                    success: function (dataBlock) {
                        pageNum++;
                        if (dataBlock.data == null || dataBlock.data.length == 0) {
                            $("#pullUpLabel").html("亲，到底了！！！");
                            return;
                        }
                        $("#pullUpLabel").html("上拉刷新！");
                        if (dataBlock.data.length < pageSize) {
                            $("#pullUpLabel").html("亲，到底了！！！");
                        }
                        $("#tenantList").append(myTenant(dataBlock.data));
                        $('.lazy').picLazyLoad({threshold: 100, placeholder: 'images/blank.gif'});
                        isLoading=false;
                    }
                });
            }
        }
    });

    });
</script>
</body>
</html>