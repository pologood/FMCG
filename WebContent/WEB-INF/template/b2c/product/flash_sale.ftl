<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-限时抢购</title>
    <meta name="keywords" content="${setting.siteName}-限时抢购"/>
    <meta name="description" content="${setting.siteName}-限时抢购"/>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/message.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/data/data.js"></script>
    <script type="text/javascript" src="${base}/resources/data/category.js"></script>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/flashSale.css" type="text/css" rel="stylesheet"/>
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
[#include "/b2c/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--横幅开始-->
[#include "/b2c/include/topbanner.ftl"]
    <!--横幅结束-->
    <!--logo 搜索开始-->
[#include "/b2c/include/search.ftl"]
    <!--logo 搜索结束-->
    <!--主导航开始-->
[#include "/b2c/include/topmainnav.ftl"]
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

    //    $('.item > .item-list > .close').click(function(){
    //        $(this).parent().parent().removeClass('hover');
    //        $(this).parent().hide();
    //    });
</script>


<!--主页内容区start-->
<div class="paper pc-bg">
    <!--大图展示start-->
    <div class="flashSaleImgShow container">
        <div class="wrapper">
            <!--焦点轮播-->
            <div class="topWrapper">
                <ul class="topWrapperSwiper">
                [@ad_position id=110 areaId=area.id count=5/]
                </ul>
                <div>
                    <div class="pagination-all">
                        <div class="pagination"></div>
                    </div>
                </div>

                <div class="topWrapperLeft li"><</div>
                <div class="topWrapperRight li">></div>
            </div>
        </div>
        <div class="span4 right">
        [@ad_position id=111 areaId=area.id count=1/]
        </div>
    </div>
    <!--大图展示end-->

    <!--楼层start-->
    <div id="F" class="flashSaleFloor container">
        <!-- <div class="flashSaleFloorNav">
            <span></span>

        </div> -->
            [#--<div class="flashSaleNav">--]
                [#--<div class="left">--]
                    [#--<span>限时抢购</span>--]
                [#--</div>--]
                [#--<dl class="content">--]
                    [#--<dd class="on">正在进行</dd>--]
                [#--[#if promotions_unstart??&&promotions_unstart?has_content]--]
                    [#--<dd>即将开始</dd>--]
                [#--[/#if]--]
                [#--</dl>--]
            [#--</div>--]
            [#list page as page]
                [#if page.endDate > 0]
                <div class="flashSaleFloorShow">
                    <dl>
                        <dt>距离结束还有</dt>
                        <dd>
                            <input type="text" name="endDate" endTime="${(page.endDate?string("yyyy-MM-dd HH:mm:ss"))!}"
                                   id="${page.promotionProducts[0].product.id}">
                            <span style="color:red;margin-left:-30px;"></span>
                            <!-- <input type="text" class="count_Down_h">时
                            <input type="text" class="count_Down_m">分
                            <input type="text" class="count_Down_s">秒 -->
                        </dd>
                    </dl>
                    <a href="${base}/b2c/product/detail/${page.promotionProducts[0].product.id}.jhtml">
                        <img src="${page.promotionProducts[0].product.thumbnail}"/>
                    </a>
                    <h2><a href="${base}/b2c/product/detail/${page.promotionProducts[0].product.id}.jhtml">${page.promotionProducts[0].product.fullName}</a></h2>
                    <div>
                        <div>
                            <span>￥${page.priceExpression}</span>
                            <em>原价：${page.promotionProducts[0].product.marketPrice}</em>
                        </div>
                        <h1>
                            <a href="${base}/b2c/product/detail/${page.promotionProducts[0].product.id}.jhtml">抢购</a>
                        </h1>
                    </div>
                </div>
                [/#if]
            [/#list]
       
        </div>
    </div>

    <!--楼层end-->
    <script type="text/javascript">
        //左悬浮栏及其定位到楼层
        $('.elevator ul li').click(function () {
            var ind = $('.elevator ul li').index(this) + 1;
            var topVal = $('#' + ind + 'F').offset().top;
            $('body,html').animate({scrollTop: topVal}, 1000)
        });

        $(window).scroll(scrolls);
        scrolls();
        function scrolls() {
            var f1, f2;
            var fixRight = $('.elevator ul li');
            var sTop = $(window).scrollTop();
            var i = 0;
        }
    </script>
    <!--标语start-->
[#include "/b2c/include/slogen.ftl"]
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
                    var t_end = $(this).attr('endTime');
                    t_end = t_end.replace(/-/g, "/");
                    time_end = new Date(t_end).getTime(); //设定目标时间
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
[#include "/b2c/include/footer.ftl"]
<!--底部end-->

<!--左侧悬浮框 start-->
[#include "/b2c/include/hover_left.ftl"/]
<!--左侧悬浮框end-->

<!--右侧悬浮框 start-->
[#include "/b2c/include/hover_right.ftl"/]
<!--右侧悬浮框end-->
</body>
</html>