<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/fixed/tenant/list.css"/>
    <title>搜索</title>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <style type="text/css">
        #demo {
            position: absolute;
            1width: 120px;
            1height: 85px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background: #fff;
            1opacity: 0.75;
            text-align: center;
            margin-top: 10px;
            font-size: 16px;
            z-index: 10001;
            padding: 0.2em 0.5em;
            line-height: 2;
        }
        .triangle-R{
            display: block;
            width: 0;
            height: 0;
            position: absolute;
            left: 50%;
            top: -9px;
            margin-left: -7px;
            border-bottom: 9px solid #ccc;
            border-left: 7px solid transparent;
            border-right: 7px solid transparent;
        }
        .triangle-I{
            display: block;
            width: 0;
            height: 0;
            position: absolute;
            left: 50%;
            top: -7px;
            margin-left: -6px;
            border-bottom: 7px solid #fff;
            border-left: 6px solid transparent;
            border-right: 6px solid transparent;
        }
        #demo:beforesas {
            content: '';
            display: block;
            width: 0;
            height: 0;
            position: relative;
            top: -10px;
            left: 30px;
            border-bottom: 9px solid #fff;
            border-left: 7px solid transparent;
            border-right: 7px solid transparent;
        }

        .shop-items .weui_cell_primary a {
            color: #333;
        }

        .searchBar{
            border-width: 0;
            background-color: white;
            width: 60px;
            height: 30px;
            line-height: 30px;
            border-radius: 5px 0 0 5px;
            text-align: center;
         }
        .searchBar:after{
            content: '';
            width: 0;
            height: 0;
            border-left: 4px solid transparent;
            border-right: 4px solid transparent;
            border-top: 6px solid #666;
            display: inline-block;
            vertical-align: middle;
            margin-left: 2px;
        }

        .visited a:after{
            content: '';
            width: 0;
            height: 0;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            border-top: 10px solid #000000;
            display: inline-block;
            margin-left: 0.1em;
        }
        .search_btn{
            height: 30px;
            line-height: 30px;
            font-size: 14px;
        }
        #pullUpLabel {
            width: 100%;
            line-height: 30px;
            text-align: center;
        }
        #nearbyList:after{
            /*display: none;*/
        }
        #nearbyList .box_tenant.col-1:last-child{
            margin-bottom: 0;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>

[#--热门搜索的商家--]
    <script type="text/x-handlebars-template" id="wap-tenant-item">
        {{#each this}}
        [#include "/wap/include/tenantlist-indexnew.ftl"/]
        {{/each}}
    </script>

[#--热门搜索的商品--]
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
                    <div class="weui_cell_bd">
                        <div id="thisDiv" class="searchBar">商家</div>
                        <div id='demo' style="display: none;z-index:2000;font-size:16px">
                            <i class="triangle-R"></i><i class="triangle-I"></i>
                            <div id="demoTenant"
                                 style="width: 100%;text-align: center;border-bottom:1px solid whitesmoke;">
                                <i class="iconfont" style="color:#a3b8cc">&#xe615;</i>&nbsp;&nbsp;商家
                            </div>
                            <div id="demoProduct" style="width: 100%;text-align: center;">
                                <i class="iconfont" style="color:#cacc5c">&#xe683;</i>&nbsp;&nbsp;商品
                            </div>
                        </div>
                    </div>
                    <div class="weui_cell_bd weui_cell_primary defaults radius-right" style="background-color: white">
                        <input class="weui_input" id="search_input" type="text" placeholder="根据商家或商品名称查询">
                    </div>
                    <div class="weui_cell_ft" style="margin-left: 10px;">
                        <a href="javascript:;" class="weui_btn weui_btn_default search_btn" id="weui_icon_search">搜索</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="am-g" id="searchTenant" style="display: block;">
        <div id="nearbyList" class="weui_cells weui_cells_access" style="margin-top:0px;background-color: #f2f2f2;">
        </div>
    </div>

    <div class="am-g" id="searchProduct" style="display: none">
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
    <div id="pullUpLabel" style="display: none;"></div>
    <div class="empty-for-fixedbottom_tab PTLN"></div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script>
    $(function () {
        init();
        var wapProductItem = Handlebars.compile($("#wap-product-item").html());
        var wapTenantItem = Handlebars.compile($("#wap-tenant-item").html());

        [#--[#if type == 'product']--]
            $("#goodsList").html(wapProductItem(${product}));
        [#--[#elseif type == 'tenant']--]
            $("#nearbyList").html(wapTenantItem(${tenant}));
        [#--[/#if]--]
        
        $(".ThumbIconLeft .togglebtn").click(ThumbIconLeftHandler);
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });
        //var $demo = $('#demo').css('display');
        var mask_transparent_demo=MaskMaker("mask-transparent","#demo",{
            afterhide:function(){
                //$(".popupmenu").hide();
            }
        });
        $("#thisDiv").on('click', function () {
            if ($('#demo').css('display') == 'block') {
                $("#demo").hide();
                mask_transparent_demo.hide(true);
                //mask_transparent_demo.hide(true);
            } else if ($('#demo').css('display') == 'none') {
                $("#demo").show();
                mask_transparent_demo.active("#demo");
                mask_transparent_demo.show();
            }
        });

        $('#demoProduct').on('click', function () {
            $("#demo").hide();
            mask_transparent_demo.hide(true);
            $("#thisDiv").html('商品');

            $("#searchTenant").hide();
            $("#searchProduct").show();

            $("#type").val("product");
        });

        $('#demoTenant').on('click', function () {
            $("#demo").hide();
            mask_transparent_demo.hide(true);
            $("#thisDiv").html('商家');

            $("#searchTenant").show();
            $("#searchProduct").hide();

            $("#type").val("tenant");
        });
        var pageNum=2;
        var pageSize=10;
        var isLoading=false;
        scroll(function(){
            if(!isLoading){
                var $searchValue = $('#search_input').val();

                if($searchValue==null||$searchValue==''){

                    return;
                }

                var $value = $("#thisDiv").text().replace(/(^\s*)|(\s*$)/g, "");
                isLoading=true;
                if($value=='商家'){
                    ajaxGet({
                        url: '${base}/app/b2c/tenant/list.jhtml',
                        data: {
                            keyword: $searchValue,
                            pageNumber: pageNum,
                            pageSize: pageSize
                        },
                        success: function (dataBlock) {
                            pageNum++;
                            if (dataBlock.data == null || dataBlock.data.length == 0) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                                return;
                            }

                            //$("#pullUpLabel").html("上拉刷新！");
                            if (dataBlock.data.length < pageSize) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                            }

                            $("#nearbyList").append(wapTenantItem(dataBlock.data));
                            $('.lazy').picLazyLoad({
                                threshold: 100,
                                placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                            });
                            isLoading=false;
                        }
                    });
                }else if ($value == '商品') {
                    ajaxGet({
                        url: '${base}/app/product/search.jhtml',
                        data: {
                            keyword: $searchValue,
                            pageNumber: pageNum,
                            pageSize: pageSize
                        },
                        success: function (dataBlock) {
                            pageNum++;
                            if (dataBlock.data == null || dataBlock.data.length == 0) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                                return;
                            }

                            //$("#pullUpLabel").html("上拉刷新！");
                            if (dataBlock.data.length < pageSize) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                            }

                            $("#goodsList").append(wapProductItem(dataBlock.data));
                            $('.lazy').picLazyLoad({
                                threshold: 100,
                                placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                            });
                            isLoading=false;
                        }
                    });
                }
            }
        });
        $('#weui_icon_search').on('click', function () {
            var $searchValue = $('#search_input').val();

            if($searchValue==null||$searchValue==''){
                showToast2({content:"请输入需要查询的内容!"});
                return;
            }
            $("#pullUpLabel").show();
            var $value = $("#thisDiv").text().replace(/(^\s*)|(\s*$)/g, "");

            if ($value == '商家') {
                isLoading=true;
                ajaxGet({
                    url: '${base}/app/b2c/tenant/list.jhtml',
                    data: {
                        keyword: $searchValue,
                        pageNumber: 1,
                        pageSize: 10
                    },
                    success: function (data) {
                        if (data.data.length == 0) {
                            $("#nearbyList").html("<div style='line-height: 50px;text-align: center;'>没有搜索到相关的商家</div>");
                        } else {
                            $("#nearbyList").html(wapTenantItem(data.data));

                            //$("#pullUpLabel").html("上拉刷新！");

                            if (data.data.length < pageSize) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                            }
                            $(".lazy").picLazyLoad({
                                threshold: 100,
                                placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                            });
                        }
                        isLoading=false;
                    }
                });
            } else if ($value == '商品') {
                isLoading=true;
                ajaxGet({
                    url: '${base}/app/product/search.jhtml',
                    data: {
                        keyword: $searchValue,
                        pageNumber: 1,
                        pageSize: 10
                    },
                    success: function (data) {
                        if (data.data.length == 0) {
                            $("#goodsList").html("<div style='line-height: 50px;text-align: center;'>没有搜索到相关的商品</div>");
                        } else {
                            $("#goodsList").html(wapProductItem(data.data));

                           // $("#pullUpLabel").html("上拉刷新！");

                            if (data.data.length < pageSize) {
                                $("#pullUpLabel").html("亲，到底了！！！");
                            }

                            $(".lazy").picLazyLoad({
                                threshold: 100,
                                placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                            });
                        }
                        isLoading=false;
                    },
                    error: function (data) {
                        //alert(data);
                    }
                });
            }
        });

        $("#md-ul").find("li").on("click", function () {
            $("#md-ul").find("li").removeClass("visited");
            $(this).addClass("visited");
            var $orderType = $(this).attr("data-id");
            var $searchValue = $('#search_input').val();
            ajaxGet({
                url: "${base}/wap/product/searchProduct.jhtml",
                data: {
                    keyword: $searchValue,
                    orderType: $orderType
                },
                success: function (dataBlock) {
                    if (dataBlock.data.length == 0) {
                        $("#goodsList").html("<div style='line-height: 50px;text-align: center;'>没有相关记录</div>");
                    } else {
                        $("#goodsList").html(wapProductItem(dataBlock.data));
                    }
                    $('.lazy').picLazyLoad({
                        threshold: 100,
                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                    });
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
