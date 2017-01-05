<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>商品详情</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <button class="{{#expression @index '==' '0'}}choosed{{/expression}}  " onclick="getSpec();choosedHighLight();"
                id="{{color}}">{{color}}
        </button>
        {{/each}}
    </script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]
<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="am-g bg-silver">
    [#include "/wap/product/pdetail.ftl"/]
        <div class="am-g border-bt">
            <div id="silder" class="silder" style="position:relative">
                <img src="${base}/resources/wap/2.0/images/transparent_800x800.png" style="width:auto;max-height:800px"
                     alt="load.."/>
            </div>
        </div>
        <!-- banner -->
        <div class="bg-default">
            <div class="weui_cell cl-center">
                <div class="weui_cell_primary">
                    <p class="font-default"
                       style="overflow:hidden;text-overflow:ellipsis;-webkit-line-clamp:2;display:-webkit-box;-webkit-box-orient:vertical;white-space:initial">${product.name}</p>
                    <p class="cf color-orangered font-default" style="font-size:1.4em;padding-top:0.2em">
                        <span>￥${price}</span>
                        <span class="fr" style="font-size: 0.5em;line-height: 2;color: #aaa;">已有${hits}人访问</span>
                    [#if product.price<product.marketPrice]
                        <small>
                            <del class="light-grey">￥${product.marketPrice}</del>
                        </small>
                    [/#if]
                    </p>
                </div>
                <!-- div class="weui_cell_bd" style="padding-top: 3px;">
                    <p class="font-small_1 border-r" style="padding:0 3px;display: none;">降价通知</p>
                </div -->
            </div>
        [#if promotions??&&promotions.size()!=0]
            <div class="weui_cell">
                <div class="weui_cell_bd"></div>
                <div class="weui_cell_bd weui_cell_primary">
                    [#list promotions as promotion]
                        <p class="font-small_1 am-margin-right-xs">
                            <i class="iconfont ft-bs05 promtag pt-${promotion.type}" style="margin-right: 0.3em;"></i>${promotion.name}.
                        </p>
                    [/#list]
                </div>
            </div>
        [/#if]
        </div>
        <!-- 商品价格、折扣等   href="javascript:showDetailDialog($('#product_details'));"-->
        <div class="weui_cells ">
        [#if hasSpecication!='']
            <div class="weui_cell dark-grey bg-default" id="chooseColorAndSize">
                <div class="weui_cell_hd weui_cell_primary font-default">
                    选择：颜色分类/尺码
                </div>
                <div class="weui_cell_ft"><i class="iconfont">&#xe635;</i></div>
            </div>
        [/#if]
            <a href="${base}/wap/product/productReviews/${product.id}.jhtml" class="weui_cell dark-grey bg-default">
                <div class="weui_cell_primary font-default">
                    商品评价
                </div>
                <div class="weui_cell_hd font-small">
                    <span>${product.scoreCount}人评价</span>
                </div>
                <!--<div class="weui_cell_hd font-large">&#xe65f;</div>-->
            </a>
            <div class="weui_cell dark-grey font-large">
                <div class="weui_cell_primary font-default">
                    运费描述
                </div>
                <div class="weui_cell_hd font-small">
                    <span>同城配送</span>
                </div>
                <!--[#--<div class="weui_cell_hd font-large">&#xe65f;</div>--]-->
            </div>
        </div>
        <div class="weui_cells">
            <a class="weui_cell dark-grey"
               href="${base}/wap/tenant/index/${tenant.id}.jhtml">
                <div class="weui_cell_hd am-margin-right-sm">
                    <div class="am-g border-r" style="width:6rem;height:6rem;">
                        <img src="${tenant.thumbnail}" alt="">
                    </div>
                </div>
                <div class="weui_cell_hd weui_cell_primary">
                    <p class="font-default" style="line-height:2;">${tenant.name}</p>
                    <p class="shop-level" style="color: #00a0e9;">
                    </p>
                </div>
                <div class="weui_cell_ft"><i class="iconfont">&#xe635;</i></div>
            </a>
            <!-- 线下门店-->
            <a href="javascript:;" class="weui_cell box_st PPPD">
                <div class="weui_cell_hd">
                    <i class="iconfont">&#xe623;</i>
                    <span>线下门店</span>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <span storeid="0101" class="box_st_name">门店名</span>
                </div>
                <div class="weui_cell_ft">
                    <i class="iconfont">&#xe612;</i>
                </div>
            </a>
            <div class="box_st_addr">
                门店地址
            </div>
        </div>
        <div class="weui_cells">
            <div class="am-g spacing">
                <div class=" am-titlebar-default">
                    <h2 class=" am-titlebar-title font-small PDT">
                        为您推荐
                    </h2>
                </div>
                <div class="am-g">
                    <ul class="am-gallery am-avg-3 am-gallery-overlay shop-tip-items" style="background-color: #fff;">
                    [@product_list areaId=areaId tenantId=tenant.id tagIds=5 count=9]
                        [#list products as product]
                            <li>
                                <a class="am-gallery-item am-gallery-item-default"
                                   href="${base}/wap/product/content/${product.id}/product.jhtml">
                                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                                         data-original="${product.thumbnail}" alt="拼命加载中"/>
                                    <h6 class="items-title font-small_1">
                                    ${product.fullName}
                                    </h6>
                                    <div class="red font-small">
                                        ￥${product.price}
                                        [#if product.price<product.marketPrice]
                                            <small>
                                                <del class="light-grey">￥${product.marketPrice}</del>
                                            </small>
                                        [/#if]
                                    </div>
                                </a>
                            </li>
                        [/#list]
                    [/@product_list]
                    </ul>
                </div>
            </div>
        </div>
    </div>
    [#include "/wap/product/pfoot.ftl"/]
    [#include "/wap/product/colorAndStyle_unit.ftl"/]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        $('.lazy').picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });
        /* */
        //get the fixed ele and then set empty placeholder height
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        /* set slider data and init slider*/
        var ads = [];
    [#list product.productImages as productImage]
        var path = '${productImage.large}';
        var content = {content: path};
        ads.push(content);
    [/#list]
        //
        var hasSpecication = '${hasSpecication}';
        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 0,
            isOverspread: 0,
            isAutoplay: 0,
            fixPage: false,
            animateTime: 800,
            onslide: function () {
                if (this.slideIndex == this.data.length - 1) {
                    $(".shop-items a").eq(1).click();
                }
                //当前索引
                //this.slideIndex
                //数组类型
                //this.data.length
            }
        });
        //虚拟数据
        var storedatas = [];
        [#list deliveryCenters as deliveryCenter]
            var de = {storeid: "${deliveryCenter.id}", name: "${deliveryCenter.name}", address: "${deliveryCenter.address}"};
            storedatas.push(de);
        [/#list]
        var $ofl_store=$(".box_st.PPPD");
        //默认展示的线下门店(取第一个)
        if(storedatas.length){
            //var $ofl_store=$(".box_st.PPPD");
            if (!$ofl_store.data("storedatas")) {
                $ofl_store.data("storedatas", storedatas);
            }
            $ofl_store.find('.box_st_name').attr("storeid",storedatas[0].storeid).text(storedatas[0].name);
            $ofl_store.siblings('.box_st_addr').text(storedatas[0].address);
        }else{
            $ofl_store.hide().siblings('.box_st_addr').hide();
        }
        //线下门店配置信息
        var config_box_st = {
            showmask: true,
            removecancel: true,
            afterSelect: function (data) {
                var $fa = $(".box_st.PPPD");
                if ($fa.data("storedatas")) {
                    var colls = $fa.data("storedatas");
                    $.each(colls, function (index, ele) {
                        if (ele.storeid == data) {
                            $fa.find('.box_st_name').attr("storeid", ele.storeid).text(ele.name);
                            $('.box_st_addr').text(ele.address);
                        }
                    });
                }
            },
            clastr: {
                forwrap: "addrits PPPD",
                fortitle: "addrits-title PPPD",
                forbody: "addrits-ctn PPPD"
            },
            ctn: {
                fortitle: (function (title) {
                    //build content
                    var html = '';
                    html += '<div class="weui_cell_bd weui_cell_primary">';
                    html += '<span>' + title + '</span>';
                    html += '</div>';
                    return html;
                })("线下门店"),
                forbody: (function (stdatas) {
                    //body html
                    var html = "";
                    var its_stid = "";
                    var its_name = "";
                    var its_address = "";
                    //build content
                    for (var i = 0; i < stdatas.length; i++) {
                        its_stid = stdatas[i].storeid;
                        its_name = stdatas[i].name;
                        its_address = stdatas[i].address;
                        //build
                        html += '<div class="weui_cell" storeid="' + its_stid + '">';
                        html += '<div class="weui_cell_hd"></div>';
                        html += '<div class="weui_cell_bd weui_cell_primary">';
                        html += '<h3 class="store-name">' + its_name + '</h3>';
                        html += '<p class="store-address">' + its_address + '</p>';
                        html += '</div><div class="weui_cell_ft">';
                        html += '<i class="iconfont icon_check">&#xe652;</i>';
                        html += '</div></div>';
                    }
                    return html;
                })(storedatas)
            }
        };
        $(".shop-level").html(loadGrade('${tenant.grade}'));
        $(".container").on('click', '.box_st.PPPD', function (event) {
            event.preventDefault();
            showActionSheetFree(config_box_st, $(".box_st.PPPD .box_st_name").attr("storeid"));
        });
        $("#silder").on('swipeLeft', function (event) {
            //event.preventDefault();
            event.stopPropagation();
        });
        //
        $("#chooseColorAndSize").on("click", function () {
            productShop("gouwuc");
        });
        productSwipeLAndR($("[data-title-index]").data('title-index'));
    });
    function loadGrade(number) {
        var img = "";
        for (var i = 0; i < number; i++) {
            img = img + '<i class="iconfont" >&#xe675;</i>&nbsp;';
        }
        if (img == "") {
            img = '<i class="iconfont" >&#xe675;</i>&nbsp;';
        }
        return img;
    }
</script>
</body>
</html>
