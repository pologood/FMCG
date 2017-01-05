<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-代金券</title>
    <meta name="keywords" content="${setting.siteName}-代金券"/>
    <meta name="description" content="${setting.siteName}-代金券"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/data/data.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/flashSale.css" type="text/css" rel="stylesheet"/>
    <!-- <link href="${base}/resources/b2b/css/coupon_popup.css" type="text/css" rel="stylesheet"> -->
    <link href="${base}/resources/b2b/css/coupon.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2b/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--横幅开始-->
[#include "/b2b/include/topbanner.ftl"]
    <!--横幅结束-->
    <!--logo 搜索开始-->
[#include "/b2b/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--主导航开始-->
[#include "/b2b/include/topmainnav.ftl"]
    <!--主导航结束-->
</div>
<!--头部end-->
<script type="text/javascript">
    /*所有商品分类*/
    $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'none');
    $('.allCategorys').hover(function () {
        $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'block');
    }, function () {
        $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).css('display', 'none');
    });

    $('.menuNav'+[#if versionType==0]'Tiaohuo'+[/#if]' ul>li').hover(function () {
        var eq = $('.menuNav'+[#if versionType==0]'Tiaohuo'+[/#if]' ul>li').index(this),//获取当前滑过是第几个元素
                h = $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).offset().top,//获取当前下拉菜单距离窗口多少像素
                s = $(window).scrollTop(),//获取游览器滚动了多少高度
                i = $(this).offset().top,//当前元素滑过距离窗口多少像素
                item = $(this).children('.menuNavList').height(),//下拉菜单子类内容容器的高度
                sort = $('.menuNav'[#if versionType==0]+'Tiaohuo'[/#if]).height();//父类分类列表容器的高度

        if (item < sort) {//如果子类的高度小于父类的高度
            if (eq == 0) {
                $(this).children('.menuNavList').css('top', (i - h));
            } else {
                $(this).children('.menuNavList').css('top', (i - h) + 1);
            }
        } else {
            if (s > h) {//判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
                if (i - s > 0) {//则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                    $(this).children('.menuNavList').css('top', (s - h) + 0);
                } else {
                    $(this).children('.menuNavList').css('top', (s - h) - (-(i - s)) + 0);
                }
            } else {
                $(this).children('.menuNavList').css('top', 0);
            }
        }

        $(this).addClass('now');
        $(this).children('.menuNavList').css('display', 'block');
    }, function () {
        $(this).removeClass('now');
        $(this).children('.menuNavList').css('display', 'none');
    });
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
    <div class="my-coupon">
        <div id="main">
            <div class="mod-main mod-comm mod-coupon hidden">
                <div class="mt">
                    <h3>优惠券</h3>
                    <div class="extra-r">
                        <a id="vipLink" href="javascript:;" target="_blank" >
                            <b class="coupon-icon"></b>
                            领取更多优惠券
                            <em id="vipNum" style="display: none">0</em>
                        </a>
                    </div>
                </div>
            </div>
            <div class="mod-main mod-comm">
                <div class="mc">
                    <div class="coupon-toolbar">
                        <div class="c-sort" style="margin-left: 20px;">
                            <ul id="select_status">
                                <!-- <li [#if status=="all"||status==""]class="curr"[/#if] >
                                    <a href="javascript:void(0);" sta="all" onclick="get_status(this)">全部</a>
                                </li>
                                <li [#if status=="unuse"]class="curr"[/#if] >
                                    <a href="javascript:void(0);" sta="unuse" onclick="get_status(this)">未使用</a>
                                </li>

                                <li [#if status=="used"]class="curr"[/#if]>
                                    <a href="javascript:void(0);" sta="used" onclick="get_status(this)">已使用</a>
                                </li>

                                <li [#if status=="expired"]class="curr"[/#if]>
                                    <a href="javascript:void(0);" sta="expired" onclick="get_status(this)">已过期</a>
                                </li> -->
                            </ul>
                        </div>
                        <div class="extra-r">
                            <div class="coupon-switch-list">
                                <!-- <a href="#none" id="lump-icon" class="curr" title="块列" ></a>
                                <a href="#none" id="list-icon" title="列表" ></a> -->
                            </div>
                        </div>
                    </div>
                </div>

                <div class="coupon-empty-box hidden">
                    <i class="e-icon"></i>
                    <div class="txt">
                        哦噢~小主一张券都没有，快去抢几张~
                    </div>
                    <div class="op-btns mt20">
                        <a href="javascript:void(0);" target="_blank" class="btn-1" >去领券中心</a>
                    </div>
                </div>
                
                <div class="coupon-items">
                    [#list coupons as coupons]
                    [#if coupons.status=='confirmed'&&coupons.startDate lte .now?string("yyyy-MM-dd")]
                    <div class="coupon-item coupon-item-d">
                        <div class="c-type">
                            <div class="c-price">
                                <strong>￥${coupons.amount}</strong>
                            </div>
                            <div class="c-limit">
                                【${coupons.name}&nbsp;消费满${coupons.minPrice}可用】
                            </div>
                            <div class="c-time"></div>
                            <div class="c-type-top"></div>
                            <div class="c-type-icon"></div>
                            <div class="c-type-bottom"></div>
                        </div>

                        <div class="c-msg">
                            <div class="c-range">
                                <div class="range-item">
                                    <span class="label" style="font-size:18px;width: 172px;color:#74d2d4;">每人限领：${coupons.receiverTimes}张</span>
                                    <span class="txt"></span>
                                </div>
                                <div class="range-item">
                                    <span class="label" style="font-size:18px;width: 172px;color:#74d2d4;">总数：${coupons.count}张</span>
                                    <span class="txt"></span>
                                </div>
                                <div class="range-item">
                                    <span class="label" style="font-size:18px;width: 172px;color:#74d2d4;">剩余数量：${coupons.count-coupons.sendCount}张</span>
                                    <span class="txt" title="">
                                        <a href=""></a>
                                    </span>
                                </div>
                                <!-- <div class="range-item">
                                    <span class="label"></span>
                                    <span class="txt">&nbsp;&nbsp;</span>
                                </div> -->
                            </div>
                            <div class="op-btns">
                                <!-- <span class="txt" onclick="buildCoupon(${coupons.id})">立即领取</span>
                                    <b></b> -->
                                [#if coupons.isGet=='yes']
                                <a href="javascript:;" class="btn" style="color:#666666;border-color:#e4e4e4;background:#e4e4e4;">
                                    <span class="" >已领完</span>
                                </a>
                                [#else]
                                <a href="javascript:;" onclick="buildCoupon(${coupons.id})" class="btn">
                                    <span class="txt" >立即领取</span>
                                    <b></b>
                                </a>
                                [/#if]
                            </div>
                        </div>
                    </div>
                    [/#if]
                    [/#list]
                </div> 
            </div>
        </div>
    </div>
    <!--标语start-->
[#include "/b2b/include/slogen.ftl"]
    <!--标语end-->
</div>
<!--主页内容区end-->
<script type="text/javascript">
    //初始化
    var size = $(".topWrapperSwiper li").size();
    for (var i = 1; i <= size; i++) {
        var span = "<span>" + i + "</span>";
        $(".pagination").append(span);
    }
    $(function () {
        //手动控制轮播
        $(".topWrapperSwiper li").eq(0).show();
        $(".pagination span").eq(0).addClass("active");
        $(".pagination span").mouseover(function () {
            $(this).addClass("active").siblings().removeClass("active");
            var index = $(this).index();
            i = index;
            //alert(index)
            $(".topWrapperSwiper li").eq(index).stop().fadeIn(300).siblings().stop().fadeOut(300);
        });

        //自动轮播
        var i = 0;
        var t = setInterval(move, 3000);
        //核心向左运动函数
        function moveL() {
            i--;
            if (i == -1) {
                i = size - 1;
            }
            //alert(i);
            $(".pagination span").eq(i).addClass("active").siblings().removeClass("active");
            $(".topWrapperSwiper li").eq(i).fadeIn(300).siblings().fadeOut(300);

            // console.log("moveL", i);
        }

        //核心向右运动函数
        function move() {
            i++;
            if (i == size) {
                i = 0;
            }
            //alert(i);
            $(".pagination span").eq(i).addClass("active").siblings().removeClass("active");
            $(".topWrapperSwiper li").eq(i).fadeIn(300).siblings().fadeOut(300);

            // console.log("move", i);
        }

        //左边按钮点击事件
        $(".topWrapper .topWrapperLeft").click(function () {
            //alert(1234);
            moveL();
        });
        //右边按钮点击事件
        $(".topWrapper .topWrapperRight").click(function () {
            //alert(5234);
            move();
        });

        //定时器的开始与结束
        $(".topWrapper").hover(function () {
            clearInterval(t)
        }, function () {
            t = setInterval(move, 2000);
        });
        setInterval(timer, 1000);
        function timer() {
            var time_start = new Date().getTime(); //设定当前时间
            $.each($("[name='endDate']"), function () {
                var time_end = new Date().getTime();
                if($(this).attr('endTime')!=null&&$(this).attr('endTime')!=""){
                    time_end = new Date($(this).attr('endTime')).getTime(); //设定目标时间
                }
                // 计算时间差
                var time_distance = time_end - time_start;
                // 天
                var int_day = Math.floor(time_distance / 86400000);
                time_distance -= int_day * 86400000;
                // 时
                var int_hour = Math.floor(time_distance / 3600000);
                time_distance -= int_hour * 3600000;
                // 分
                var int_minute = Math.floor(time_distance / 60000);
                time_distance -= int_minute * 60000;
                // 秒
                var int_second = Math.floor(time_distance / 1000);
                // 时分秒为单数时、前面加零
                if (int_day < 10) {
                    if (int_day < 0) {
                        int_day = "00";
                    } else {
                        int_day = "0" + int_day;
                    }
                }
                if (int_hour < 10) {
                    int_hour = "0" + int_hour;
                }
                if (int_minute < 10) {
                    int_minute = "0" + int_minute;
                }
                if (int_second < 10) {
                    int_second = "0" + int_second;
                }
                $(this).next().text(int_day + "天" + int_hour + "时" + int_minute + "分" + int_second + "秒");
            });
        }

    })
</script>

<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--左侧悬浮框 start-->
[#include "/b2b/include/hover_left.ftl"/]
<!--左侧悬浮框end-->

<!--右侧悬浮框 start-->
[#include "/b2b/include/hover_right.ftl"/]
<!--右侧悬浮框end-->
</body>
</html>