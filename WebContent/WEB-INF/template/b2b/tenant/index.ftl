<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-商家首页</title>
    <meta name="keywords" content="${setting.siteName}-商家首页" />
    <meta name="description" content="${setting.siteName}-商家首页" />
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/brands.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/merchantHomepage.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/coupon_popup.css" type="text/css" rel="stylesheet">
    <script type="text/javascript">
        $(function(){
            //初始化
            var size=$(".slider-items li").size();
            for(var i=1; i<=size; i++){
                var span="<span>"+i+"</span>";
                $(".trigger-inner").append(span);
            }

            //手动控制轮播
            $(".slider-items li").eq(0).show();
            $(".trigger-inner span").eq(0).addClass("ui-switchable-selected");
            $(".trigger-inner span").mouseover(function(){
                $(this).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
                var index=$(this).index();
                i=index;
                $(".slider-items li").eq(index).stop().fadeIn(300).siblings().stop().fadeOut(300);
            });

            //自动轮播
            var i=0;
            var t=setInterval(move,3000);
            //核心向左运动函数
            function moveL(){
                i--;
                if(i==-1){
                    i=size-1;
                }
                $(".trigger-inner span").eq(i).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
                $(".slider-items li").eq(i).fadeIn(300).siblings().fadeOut(300);
            }
            //核心向右运动函数
            function move(){
                i++;
                if(i==size){
                    i=0;
                }
                $(".trigger-inner span").eq(i).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
                $(".slider-items li").eq(i).fadeIn(300).siblings().fadeOut(300);
            }
            //左边按钮点击事件
            $(".slider-page .page-prev").click(function(){
                moveL();
            });
            //右边按钮点击事件
            $(".slider-page .page-next").click(function(){
                move();
            });

            //定时器的开始与结束
            $(".slider-inner").hover(function(){
                clearInterval(t)
            },function(){
                t=setInterval(move,2000);
            })

        });

        function searchProduct(tenantId){
            var keyWord=$("#keyWord").val();
            location.href="${base}/b2b/tenant/search.jhtml?tenantId="+tenantId+"&keyWord="+keyWord;
        }

    </script>
</head>
<body>
<!-- 头部start -->
[#include "/b2b/include/tenant_header.ftl"]
<!--头部end-->
<div id="coupon-trigger" style="top:168px;">
    <span class="shop-coupon-trigger">
    <em>店铺优惠券</em>
    <span class=""></span>
</span>
</div>

<div class="ks-overlay cart-tips coupon-popup" id="coupon-popup">
    <a class=" ks-overlay-close" href="javascript:void('close')" role="button">

    </a>
    <div class=" ks-overlay-content">
        <div class="coupon-summary">
            <span class="icon-announcement"></span>
            已领取
            <em>${num}</em>
            张优惠券，[#if isNewCoupon==true]有[#else]暂无[/#if]新优惠券可领取
        </div>
        <div class="coupon-list">
            <div class="cart-container">
                <ul>
                    [#list coupons as coupons]
                    [#if coupons.status=='canUse'||coupons.status=='unUsed']
                    <li class="coupon-youhuijuan">
                        <div class="coupon-amount">
                            <span class="rmb" title="¥ 10">¥ ${coupons.amount}</span>

                        </div>
                        <div class="coupon-detail">
                            <div class="coupon-info">
                                <p class="coupon-title">${coupons.name}&nbsp;消费满${coupons.minPrice}可用</p>
                                <p class="coupon-time">${coupons.startDate}-${coupons.endDate}</p>
                            </div>
                            <div class="coupon-op">
                                [#if coupons.isGet=='yes']
                                <a href="javascript:void(0);" class="coupon-unreceived select">已 领 </a>
                                [#else]
                                <a href="javascript:void(0);" onclick="buildCoupon(${coupons.id})" class="coupon-unreceived">领 取</a>
                                [/#if]
                            </div>
                        </div>
                    </li>
                    [/#if]
                    [/#list]
                </ul>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    /*-------shoppingCart优惠劵点击事件--------*/
    var sctSpan = $(".shop-coupon-trigger span");
    $("#coupon-popup").hide();
    $(document).ready(function() {
        $(".ks-overlay-close").click(function(){
            $("#coupon-popup").hide();
            $(sctSpan).removeClass("on");
        });
    });
    /*-------shoppingCart优惠劵点击事件2--------*/
    function e(obj){
        return document.getElementById(obj)
    }
    e('coupon-trigger').onclick=function(event){
        e('coupon-popup').style.display='block';
        $(sctSpan).addClass("on");
        stopBubble(event);
        document.onclick=function(){
            e('coupon-popup').style.display='none';
            $(sctSpan).removeClass("on");
            document.onclick=null;

        }
    }
    e('coupon-popup').onclick=function(event){
        //只阻止了向上冒泡，而没有阻止向下捕获，所以点击con的内部对象时，仍然可以执行这个函数
        stopBubble(event);
    }
    //阻止冒泡函数
    function stopBubble(e){
        if(e && e.stopPropagation){
            e.stopPropagation();  //w3c
        }else{
            window.event.cancelBubble=true;//IE
        }
    }
    //
    function buildCoupon(id) {
        if("${member}"==""){
            $.message("warn","请先登录")
            setInterval(function(){
                location.href="${base}/b2b/login.jhtml";
            },1000);
        }
        $.ajax({
            url: "${base}/b2b/member/coupon_code/build.jhtml?id=" + id,
            type: "POST",
            dataType: "json",
            success: function (data) {
                $.message(data);
                if(data.type=="success"){
                     window.setTimeout(function () {
                        location.reload();
                    }, 600);
                }
            }
        });
    }

</script>
<!--主页内容区start-->
<div class="paper">
    <!--轮播广告start-->
    <div class="brands-slider">
        <div class="container overflow-v">
            <div class="slider-inner">
                <div class="slider-main">
                    <ul class="slider-items">
                        [@ad_position id=120 tenantId=tenant.id count=3/]
                    </ul>
                </div>
                <div class="slider-extra">
                    <div class="slider-trigger">
                        <div class="trigger-inner">

                        </div>
                    </div>
                    <div class="slider-page">
                        <a class="page-prev" href="javascript:;">&lt;</a>
                        <a class="page-next" href="javascript:;">&gt;</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--轮播广告end-->
    <!--商家头部左右大图展示start-->
    <div class="headerImg ">
        <div class="container pc-bg">
            <div class="left">
            [@ad_position id=121 tenantId=tenant.id useCache=false count=1/]
                <div class="title">
                    <h2>热卖TOP100</h2>
                    <span>火爆销售ING快来买哦</span>
                    <a href="javascript:;" target="_blank">查看更多 ▶</a>
                </div>
            </div>
            <div class="content">
            [@ad_position id=122 tenantId=tenant.id useCache=false count=1/]
                <div class="title">
                    <h2>新品上市</h2>
                    <span>火爆销售ING快来买哦</span>
                    <a href="javascript:;" target="_blank">查看更多 ▶</a>
                </div>
            </div>
        </div>
    </div>
    <!--商家头部左右大图展示end-->
    <!--周推荐start-->
    <div class="week-recommend ">
        <div class="container">
            <div class="recommend-nav">
                <h2>本周推荐/</h2>
                <span>RECOMMENDED&nbsp;THIS&nbsp;WEEK</span>
            </div>
            <div class="recommend-content">
                <ul>
                    [#list recommendProducts as recommendProduct]
                    <li>
                        <a href="${base}/b2b/product/detail/${recommendProduct.id}.jhtml" target="_blank">
                            <div class="item-img">
                                <img width="232px" height="232px" src="${recommendProduct.thumbnail}">
                            </div>
                            <div class="item-info pc-bg">
                                <h2 style="text-overflow: ellipsis; width: 212px;white-space: nowrap;overflow: hidden;">${recommendProduct.name}</h2>
                                <div class="wire"></div>
                                <span class="font">${(recommendProduct.tenant.name)!}</span>
                                <span>${(recommendProduct.seoDescription)!}<em style="visibility: hidden;">页面描述</em></span>
                            </div>
                        </a>
                    </li>
                    [/#list]
                </ul>
            </div>
        </div>
    </div>
    <!--周推荐end-->
    <!--专区类型one start -->
    <div class="hot-brands merchant-area1">
        [#list tenant.productCategoryTenants as productCategoryTenant]
            [#assign hasProduct=false/]
            [#list productCategoryTenant.products as product]
                [#if product.isList==true]
                    [#assign hasProduct=true /]
                    [#if hasProduct==true]
                        [#break /]
                    [/#if]
                [/#if]
            [/#list]
            [#if hasProduct==true]
            <div class="container">
                <div class="hot-nav">
                    <div class="left"></div>
                    <h2>${productCategoryTenant.name}专区<span>${productCategoryTenant.seoTitle}</span><span>${productCategoryTenant.seoDescription}</span></h2>
                    <div class="right"></div>
                </div>
                <div class="items-wrap">
                    <div class="items-nav">
                        <a href="${base}/b2b/tenant/search.jhtml?tenantId=${tenant.id}&productCategoryTenantId=${productCategoryTenant.id}">更多>></a>
                    </div>
                    <div class="items">
                        <ul>
                            [#assign count=0/]
                            [#list productCategoryTenant.products as product]
                                [#if product.isList==true]
                                    <li class="item">
                                        <a class="img" href="${base}/b2b/product/detail/${product.id}.jhtml">
                                            <img src="${product.thumbnail}" width="255" height="255">
                                        </a>
                                        <h2>
                                            <a href="${base}/b2b/product/detail/${product.id}.jhtml" title="【${(product.tenant.name)!}】${product.fullName!}">【${(product.tenant.name)!}】${product.fullName!}</a>
                                        </h2>
                                        <div class="shopping-info">
                                            <span>￥${product.price}</span>
                                            <a href="${base}/b2b/product/detail/${product.id}.jhtml">立即购买</a>
                                        </div>
                                    </li>
                                    [#assign count=count+1/]
                                    [#if count==8]
                                        [#break /]
                                    [/#if]
                                [/#if]
                            [/#list]
                        </ul>
                    </div>
                </div>
            </div>
            [/#if]
        [/#list]
    </div>
    <!--专区类型one end -->
    <!--专区类型two start -->
    [#--<div class="hot-brands merchant-area2">--]
    [#--[#list tenant.productCategoryTenants as productCategoryTenant]--]
        [#--[#assign hasProduct=false/]--]
        [#--[#list productCategoryTenant.products as product]--]
            [#--[#if product.isList==true]--]
                [#--[#assign hasProduct=true /]--]
                [#--[#if hasProduct==true]--]
                    [#--[#break /]--]
                [#--[/#if]--]
            [#--[/#if]--]
        [#--[/#list]--]
        [#--[#if hasProduct==true]--]
        [#--<div class="container">--]
            [#--<div class="hot-nav">--]
                [#--<div class="left"></div>--]
                [#--<h2>${productCategoryTenant.name}专区<span>${productCategoryTenant.seoTitle}</span><span>${productCategoryTenant.seoDescription}</span></h2>--]
                [#--<div class="right"></div>--]
            [#--</div>--]
            [#--<div class="items-wrap">--]
                [#--<div class="items-nav">--]
                    [#--<a href="javascript:;" target="_blank">更多>></a>--]
                [#--</div>--]
                [#--<div class="items">--]
                    [#--<div class="left">--]
                        [#--<a target="_blank" href="javascript:;">--]
                            [#--<img src="${productCategoryTenant.image}" width="315" height="714">--]
                        [#--</a>--]
                    [#--</div>--]
                    [#--<div class="right">--]
                        [#--<ul>--]
                        [#--[#assign count=0/]--]
                        [#--[#list productCategoryTenant.products as product]--]
                            [#--[#if product.isList==true]--]
                                [#--<li class="item">--]
                                    [#--<a class="img" href="javascript:;" target="_blank">--]
                                        [#--<img src="${product.thumbnail}" width="255" height="255">--]
                                    [#--</a>--]
                                    [#--<h2>--]
                                        [#--<a href="javascript:;" target="_blank" title="【天天果园】智利甜心樱桃利甜心樱  甜脆多汁 晶莹剔透">【天天果园】智利甜心樱桃利甜心樱  甜脆多汁 晶莹剔透</a>--]
                                    [#--</h2>--]
                                    [#--<div class="shopping-info">--]
                                        [#--<span>￥300</span>--]
                                        [#--<a href="javascript:;">立即购买</a>--]
                                    [#--</div>--]
                                [#--</li>--]
                                [#--[#assign count=count+1/]--]
                                [#--[#if count==8]--]
                                    [#--[#break /]--]
                                [#--[/#if]--]
                            [#--[/#if]--]
                        [#--[/#list]--]
                        [#--</ul>--]
                    [#--</div>--]

                [#--</div>--]
            [#--</div>--]
        [#--</div>--]
        [#--[/#if]--]
    [#--[/#list]--]
    [#--</div>--]
    <!--专区类型two end -->
    <!--商家底部横幅开始-->
    <div class="bottomBanner">
        <div class="container">
            <a href="javascript:;">
                [@ad_position id=123 tenantId=tenant.id useCache=false count=1/]
            </a>
        </div>
        <div class="container">
            <div class="actGotop">
                <a href="javascript:;"></a>
                <span>返回顶部</span>
            </div>
        </div>
    </div>
    <!--商家底部横幅结束-->
    <!--标语start-->
    [#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
[#include "/b2b/include/hover_right.ftl"/]
<!--右侧悬浮框end-->
<link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
</body>
</html>