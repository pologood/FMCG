<!DOCTYPE html>
<html lang="en">
<head>
    [#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>明星商家</title>
</head>
<body class="DSB">
    <!-- StarBusiness,明星商家-->
    <div class="box_starR sec1">
        <!-- -->
        <div class="box_starR-hd">
            <span>&mdash;&nbsp;好店就在${setting.siteName}&nbsp;&mdash;</span>
        </div>
        <div class="box_starR-bd">
            <!-- main-box4-->
            <!-- soga begin-->
            [#if tenants??]
                [#list tenants as tenant]
                    <div class="box_starR-bd-main" data-order-initial="${tenant_index}">
                        <div class="store_symbol2">
                            <div>
                                <a href="${base}/wap/tenant/index/${tenant.id}.jhtml" class="store_symbol2-main" style="background-image:url(${base}/resources/wap/2.0/upload/no-1.png)">
                                    <img src="${base}/resources/wap/2.0/upload/no-1.png" data-original="${tenant.path}" alt="">
                                </a>
                                <div class="store_symbol-mask" style="background-image:url(${base}/resources/wap/2.0/upload/no-1.png)">                                    
                                </div>
                                <div class="weui_cell store_symbol-info DSB">
                                    <div class="weui_cell_hd hd">
                                        [#if tenant.thumbnail??&&tenant.thumbnail?has_content&&tenant.thumbnail!='null']
                                            <img class="hd-img" src="${tenant.thumbnail}">
                                        [#else ]
                                            <img class="hd-img" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png">
                                        [/#if]
                                    </div>
                                    <div class="weui_cell_bd weui_cell_primary bd">
                                        <p>${tenant.shortName}</p>
                                        <span>已有<b id="span_favorite_num">${tenant.favoriteNum}</b>人收藏</span>
                                    </div>
                                    <div class="weui_cell_ft cf ft">
                                        <a href="${base}/wap/tenant/index/${tenant.id}.jhtml">
                                            <span>进店</span>
                                            <i class="font-small iconfont"></i>
                                        </a>
                                    </div>
                                </div>
                                <!-- <a class="store_symbol-mask" href="${base}/wap/tenant/index/${tenant.id}.jhtml"></a>-->
                            </div>
                        </div>
                        <!-- 商品展示-->
                        [#if tenant.products1??&&tenant.products2??&&tenant.products3??]
                            <div class="cf model_display2">
                                <a class="fl A-R" href="${base}/wap/product/content/${tenant.products1.id}/product.jhtml">
                                    <img src="${tenant.products1.thumbnail}" alt="">
                                </a>
                                <div class="fr B-R">
                                    <a class="b1R" href="${base}/wap/product/content/${tenant.products2.id}/product.jhtml">
                                        <img src="${tenant.products2.thumbnail}" alt="">
                                    </a>
                                    <a class="b2R" href="${base}/wap/product/content/${tenant.products3.id}/product.jhtml">
                                        <img src="${tenant.products3.thumbnail}" alt="">
                                    </a>
                                </div>
                            </div>
                        [/#if]
                        <!-- 全部，新品，促销-->
                        <div class="startags2">
                            <a href="${base}/wap/tenant/index/${tenant.id}.jhtml?type=qb" class="startags2-couponR">
                                <div>
                                    <h3>全部</h3>
                                    <p>${tenant.qb}</p>
                                </div>
                            </a>
                            <a href="${base}/wap/tenant/index/${tenant.id}.jhtml?type=xp" class="startags2-couponR">
                                <div>
                                    <h3>新品</h3>
                                    <p>${tenant.xp}</p>
                                </div>
                            </a>
                            <a href="${base}/wap/tenant/index/${tenant.id}.jhtml?type=cx" class="startags2-couponR">
                                <div>
                                    <h3>促销</h3>
                                    <p>${tenant.cx}</p>
                                </div>
                            </a>
                        </div>
                    </div>
                [/#list]
            [/#if]
            <!-- soga end-->
        </div>
        <div class="box_starR-ft">
            <div class="pageindexR">
                <span class="page_now">1</span>
                <span class="page_total">${count}</span>
            </div>
        </div>
    </div>
    <!-- bottom script-->
    <!-- js plugins here-->
    <script type="text/javascript">
    $(function() {
        //初始化设置
        var animate_ctrnum=1;
        var page_now=1;
        var page_all=${count};
        document.ontouchmove = function(e) {e.preventDefault()};
        $(".box_starR.sec1").data('move-locked', 'N'); //赋初值
        var $box_starR_bodymainfirst = $(".box_starR.sec1>.box_starR-bd").find('.box_starR-bd-main').eq(0);
        //.addClass('come-out')
        $box_starR_bodymainfirst.addClass('hidden');
        setTimeout(function() {
            $box_starR_bodymainfirst.addClass('come-out').removeClass('hidden');
        }, 1);
        //第一个可见商家信息 swipeLeft
        function runEventListenerSwipeLeft() {
            $(".box_starR.sec1").on('swipeLeft', '.box_starR-bd-main:last-child', function(event) {
                if ($(".box_starR.sec1").data('move-locked') == "Y") {
                    return; //滑动处于锁定状态，不响应动作
                }
                $(".box_starR.sec1").data('move-locked', 'Y'); //滑动上锁    
                event.preventDefault();
                var this_bodymain = this;
                var counter = 0;
                //监听动画结束
                $(this_bodymain).on('transitionend webkitTransitionEnd', function(event) {
                    if (++counter == animate_ctrnum) { //子元素动画数目(5个元素*2)
                        //移动位置
                        $(this_bodymain).parent().prepend($(this_bodymain));
                        //保留自身动画类，去除2号位动画类
                        $(this_bodymain).next().addClass('hidden');
                        setTimeout(function() {
                            $(this_bodymain).next().removeClass('come-out').removeClass('hidden');
                        }, 1);
                        $(this).off('transitionend webkitTransitionEnd');
                        $(".box_starR.sec1").data('move-locked', 'N'); //动画完成，滑动解锁
                        if(page_now<page_all){
                            $(".pageindexR .page_now").text(++page_now);
                        }else{
                            //page_now=1;
                            $(".pageindexR .page_now").text(page_now=1);
                        }
                        
                    }
                });
                //加入动画类
                $(this_bodymain).addClass('come-out');
            });
        }
        //第一个可见商家信息 swipeRight
        function runEventListenerSwipeRight() {
            $(".box_starR.sec1").on('swipeRight', '.box_starR-bd-main:last-child', function(event) {
                if ($(".box_starR.sec1").data('move-locked') == "Y") {
                    return; //滑动处于锁定状态，不响应动作
                }
                $(".box_starR.sec1").data('move-locked', 'Y'); //滑动上锁    
                event.preventDefault();
                var this_bodymain = this;
                var $bodymain_parent = $(this_bodymain).parent();
                var counter = 0;
                //移动位置
                $bodymain_parent.find('.box_starR-bd-main').eq(0).appendTo($bodymain_parent);
                var $last_now = $bodymain_parent.find('.box_starR-bd-main').last();
                //首个元素添加come-out
                var $ele1 = $bodymain_parent.find('.box_starR-bd-main').eq(0).addClass('hidden');
                setTimeout(function() {
                    $ele1.addClass('come-out').removeClass('hidden');
                }, 1);
                //监听动画结束
                $last_now.on('transitionend webkitTransitionEnd', function(event) {
                    if (++counter == animate_ctrnum) { //子元素动画数目(5个元素*2)
                        $(this).off('transitionend webkitTransitionEnd');
                        $(".box_starR.sec1").data('move-locked', 'N'); //动画完成，滑动解锁
                        if(page_now>1){
                            $(".pageindexR .page_now").text(--page_now);
                        }else{
                            $(".pageindexR .page_now").text(page_now=page_all);
                        }
                        
                    }
                });
                //remove come-out
                setTimeout(function() {
                    $last_now.removeClass('come-out');
                }, 50); //不设置延迟会造成，动画跳跃无渐变
            });
        }
        //run listener after a while
        setTimeout(function() {
            runEventListenerSwipeLeft();
            runEventListenerSwipeRight();
        }, 500);
        //
        //var so_h=$(".box_starR .box_starR-bd .box_starR-bd-main:nth-child(0) .store_symbol-info.DSB").height();
        var $store_symbolinfo_last=$(".store_symbol-info.DSB").last();
        var store_symbolinfo_height= $store_symbolinfo_last.height();
        var store_symbol2_height=$store_symbolinfo_last.parent().height();
        var rate_percent=100-parseInt(store_symbolinfo_height / store_symbol2_height * 100)+"%";
        $(".store_symbol-info.DSB").each(function(index, ele) {
            $(this).siblings('.store_symbol-mask').css({
                "-webkit-clip-path":"polygon(0 "+rate_percent+", 0 100%, 100% 100%, 100% "+rate_percent+")"
            });
        });
    });
    </script>
</body>
</html>
