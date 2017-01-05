<!DOCTYPE html>
<html lang="en">
<head>
    <title>${setting.siteName}</title>
[#include "/wap/include/resource-2.0.ftl"/]
    <style type="text/css">
        .am-text-truncate {
            word-wrap: normal;
            /* for IE */
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
        }

        .category-ctn {
            background-color: #fff;
        }

        .category-ctn-left {
            width: 30%;
            margin-bottom: 0;
            1 overflow-y: auto;
            background-color: #eee;
        }

        .category-ctn-left .am-list {
            margin-bottom: 0;
        }

        .category-ctn-right {
            width: 70%;
        }

        .menu_fixed {
            display: block !important;
            position: fixed;
            top: 0;
            left: 0;
            z-index: 20;
            padding-bottom: 60px;
            overflow: auto;
        }

        .pagescroll-rap {
            position: fixed;
            top: 0px;
            left: 0;
            right: 0;
            bottom: 0px;
            overflow: hidden;
        }

        .searchbox_state {
            margin-top: 0;
            background-color: transparent;
        }

        .searchbox_state .searchbox_state-ctn {
            padding-left: 0px;
            padding-right: 0px;
        }
        .am-list a span {
            color: #999999;
            display: block;
            font-size: 12px;
            padding:1rem 0;
        }
        .tp-jd{
            opacity: 1;
            margin: 0 10px;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        {{#if childrens}}
        <li style="border: none;">
            <p style="padding: 3px 0px 2px 10px; margin-bottom: 0px;border-bottom: 1px solid #dedede;">
                <a href="${base}/wap/product/listNew/{{id}}.jhtml">
                    <span>{{name}}</span>
                </a>
            </p>
            <ul data-am-widget="gallery" class="am-gallery am-gallery-default am-avg-3 am-text-center"
                data-am-gallery="{}" style="padding-top:0px">
                {{#each childrens}}
                <li>
                    <div class="am-gallery-item am-gallery-item-default">
                        <a href="${base}/wap/product/listNew/{{id}}.jhtml">
                            <img src="{{image}}"  class="lazy" data-original="{{image}}">
                            <p class="am-gallery-title">{{name}}</p>
                        </a>
                    </div>
                </li>
                {{/each}}
            </ul>
        </li>
        {{else}}
        <li style="border: none;">
            <div class="am-gallery-item">
                <a href="${base}/wap/product/listNew/{{id}}.jhtml">
                    <img style="height: 60px;" src="{{image}}"  class="lazy" data-original="{{image}}">
                    <p class="am-text-center am-text-truncate">{{name}}</p>
                </a>
            </div>
        </li>
        {{/if}}
        {{/each}}
    </script>
</head>
<body ontouchstart id="pagebody" class="P-PCG-IN">
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>


[#include "/wap/include/footer.ftl" /]
<script type="text/html" id="tpl_wraper">
    <div class="pagescroll-rap" style="">
        <div class="pagescroll-ctn" style="">
            <div class="page">
                <div class="am-g">
                    <div id="silder" class="silder" style="position: relative;">
                        <img src="${base}/resources/wap/2.0/images/blank.png" alt="load.."/>
                    </div>
                </div>
                <!-- another begin-->
                <div class=" cl weui_cells searchbox_state">
                    <div class="weui_cell searchbox_state-ctn">
                        <a class="weui_cell_primary tp-jd" href="${base}/wap/top_search.jhtml?type=product">
                            <i class="iconfont" style="font-size:12px;">&#xe640;</i>
                            <span class="font-small">搜索</span>
                        </a>
                    </div>
                </div>
                <div class="am-cf category-ctn" id="">
                    <div class="am-fl bg-inverse is-wrapper category-ctn-left" id="silder-left">
                        <ul class="am-list class-items" id="rootProductCategory">
                        [#list productCategoryRootList as productCategory]
                            <li data-id="${productCategory.id}">
                                <a class="am-list-static am-text-center">${productCategory.name}</a>
                            </li>
                        [/#list]
                        </ul>
                    </div>
                    <div class="am-fr bg-default category-ctn-right" id="silder-right">
                        <ul class="am-list" id="subProductCategory">
                        </ul>
                    </div>
                </div>
                <div class="empty-for-fixedbottom_tab P-PCG-IN"></div>
            </div>
        </div>


</script>

<script src="${base}/resources/wap/2.0/js/touch.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/data/category.js"></script>
<script>
    $(function () {
        init();
        //特定赋高
        fixedEleCopyHandler(".empty-for-fixedbottom_tab.P-PCG-IN", ".am-topbar-fixed-bottom");
        var ads = [];
    [@ad_position id=70 areaId=area.id count=5]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
                ads.push({content: '${ads.path}'});
            [/#list]
        [/#if]
    [/@ad_position]

        if (ads.length == 0) {
            ads.push({content: '${base}/resources/wap/2.0/upload/no-store.png'});
        }

        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 800,
            animateType: 'rotate'
        });
        //picLazyLoad
        $('.lazy').picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'});
        //菜单 li 事件handler
        function categoryLiTapHandler() {
            //style
            var rootId = $(this).attr("data-id");
            var compiler = Handlebars.compile($("#wap-list-item").html());
            $("#rootProductCategory").find("li").removeClass("active");
            $(this).siblings('li').removeClass("active");
            $(this).addClass("active");
            initCategory();
            var data = getCategoryChildRens(rootId);
            $("#subProductCategory").html(compiler(eval(getCategoryChildRens(rootId))));
            _g.categoryli_activeindex = $(this).index();
            $('.lazy').picLazyLoad({threshold: 100, placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'});
            setTimeout(function () {
                iscroll_category_real.refresh();
                //刷新之后视口无滚动 则 y 为0,不为零则视口滚动了
                if (iscroll_category_real.y !== 0) {
                    if ((0 - iscroll_category_real.y) <= sticky_menu.getFixedElePositonYToScroll() && (0 - sticky_menu.getPosYStop()) > sticky_menu.getFixedElePositonYToScroll()) {
                        sticky_menu.relax();
                    }
                }
            }, 0);
        }

        //first trigge
        $(".category-ctn-left").find("li").on("tap", categoryLiTapHandler);
        //创建分类菜单副本
        (function () {
            var $copy_category_left = $(".category-ctn-left").clone();
            $copy_category_left.addClass("copy");
            $copy_category_left.css({
                display: "none"
            }).appendTo('body');
            //bind event
            $copy_category_left.find("li").on("tap", categoryLiTapHandler);
        })();
        //define scroll instance
        var iscroll_category_real = new IScroll(".pagescroll-rap", {
            probeType: 3,
            mouseWheel: true,
            click: true
        });
        //sticky ele maker
        var sticky_menu = stickyOnIScroll(iscroll_category_real, {
            scroll_slter: ".pagescroll-rap",
            sticky_slter: ".category-ctn-left",
            copy_sticky_slter: ".category-ctn-left.copy",
            fixed_class: "menu_fixed"
        });
        //trigge event
        $("#rootProductCategory").find("li").eq(_g.categoryli_activeindex).trigger("tap");
    });
</script>
</body>
</html>
