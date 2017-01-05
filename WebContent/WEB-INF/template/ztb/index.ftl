<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-首页</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/ztb/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/ztb/js/jquery.SuperSlide.2.1.1.js"></script>

    <link rel="icon" href="${base}/resources/ztb/images/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/ztb/css/common.css" type="text/css" rel="stylesheet"/>
</head>
<body>
<!-- 头部start -->
<div id="1ZTB" class="header bg">
    <!-- 顶部导航开始 -->
    <div class="top-nav-block">
        <div class="container">
            <div class="top-nav">
                <div class="logo-top f-left">
                    <img src="${base}/resources/ztb/images/logo.gif"/>
                </div>
                <div class="nav-ul f-right">
                    <ul>
                        <li class="active"><a href="javascript:;" target="_top">首页</a></li>
                        <li><a href="javascript:;" target="_top">公司简介</a></li>
                        <li><a href="javascript:;" target="_top">合作伙伴</a></li>
                        <li><a href="javascript:;" target="_top">公司动态</a></li>
                        <li><a href="javascript:;" target="_top">联系我们</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!-- 顶部导航结束 -->
    <!-- 首页轮播start -->
    <div id="brands-slider" class="brands-slider">
        <div class="container overflow-v">
            <div class="slider-inner">
                <div class="slider-main">
                    <ul class="slider-items">
                        <li>
                            <a href="javascript:;">
                                <img src="${base}/resources/ztb/images/banner-1_01.gif" alt="" title="" width="" height="">
                            </a>
                            <div class="container">
                                <div class="tit">
                                    <h2>帮助实体店互联网化的服务平台</h2>
                                    <h3>实现渠道裂变、销售裂变</h3>
                                    <h4></h4>
                                    <div class="tit_a">
                                        [#--<a class="active" href="javascript:;" target="_top"></a>--]
                                        <a class="bofang" href="javascript:;" target="_top">▶&nbsp;&nbsp;介绍视频</a>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li>
                            <a href="javascript:;">
                                <img src="${base}/resources/ztb/images/banner-2_01.gif" alt="" title="" width="" height="">
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="slider-extra">
                <div class="slider-trigger">
                    <div class="trigger-inner">
                        <!-- span class="ui-switchable-num"></span>
                        <span class="ui-switchable-num ui-switchable-selected"></span>
                        <span class="ui-switchable-num "></span -->
                    </div>
                </div>
                <!--<div class="slider-page">-->
                <!--<a class="page-prev" href="javascript:;">&lt;</a>-->
                <!--<a class="page-next" href="javascript:;">&gt;</a>-->
                <!--</div>-->
            </div>
        </div>
    </div>
    <script type="text/javascript">
        //初始化
        var size=$(".slider-items li").size();
        for(var i=1; i<=size; i++){
            var span="<span>"+i+"</span>";
            $(".trigger-inner").append(span);
        }
        $(function(){
            //手动控制轮播
            $(".slider-items li").eq(0).show();
            $(".trigger-inner span").eq(0).addClass("ui-switchable-selected");
            $(".trigger-inner span").mouseover(function(){
                $(this).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
                var index=$(this).index();
                i=index;
                //alert(index)
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
                //alert(i);
                $(".trigger-inner span").eq(i).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
                $(".slider-items li").eq(i).fadeIn(300).siblings().fadeOut(300);
            }
            //核心向右运动函数
            function move(){
                i++;
                if(i==size){
                    i=0;
                }
                //alert(i);
                $(".trigger-inner span").eq(i).addClass("ui-switchable-selected").siblings().removeClass("ui-switchable-selected");
                $(".slider-items li").eq(i).fadeIn(300).siblings().fadeOut(300);
            }
            //左边按钮点击事件
            $(".slider-page .page-prev").click(function(){
                //alert(1234);
                moveL();
            });
            //右边按钮点击事件
            $(".slider-page .page-next").click(function(){
                //alert(5234);
                move();
            });

            //定时器的开始与结束
            $(".slider-inner").hover(function(){
                clearInterval(t)
            },function(){
                t=setInterval(move,2000);
            });
            console.log();
        });
        $(".tit").css({"bottom": -240}).animate({"bottom": 90},1000);
    </script>
    <!-- 首页轮播end -->
</div>
<!--头部end-->

<!-- 宣传视频 -->
<div class="shiping" style=" display:none;">
    <div class="shiping_1">
        <div class="shiping_">

        </div>
    </div>
</div>
<!-- 宣传视频 -->
<script type="text/javascript">
    /**
     * 视频JS
     */
    $('.shiping_1').on('click', function () {
        document.getElementById('player').controls.stop();
    });
    $(".shiping").click(function (e) {
        if (e && e.toElement && e.toElement.id == 'player') return;
        $(".shiping").hide();

        $(".shiping_").html(" ");
    });


    $(".bofang").click(function () {
        if (ding == 1) {
            if (confirm("您的浏览器还没有安装Flash插件，现在安装？")) {
                window.location.href = "http://get.adobe.com/cn/flashplayer/";
            }
        }

        var embedsrc_str = "<i>×</i><embed width='1000' id='player'  height='562' align='middle' type='application/x-shockwave-flash' allowscriptaccess='always' quality='high' allowfullscreen='true' src='http://static.video.qq.com/TPout.swf?vid=p0322nnemvl&auto=0' />";
        $(".shiping_").html(embedsrc_str);
        $(".shiping").eq(0).show();
    });

    $(".shiping").click(function () {
        $(this).hide();
    });

    $(".bofang").click(function () {
        $(".shiping").eq(0).show();
    });

</script>
<script>
    /**
     * 视频JS
     */
    function flashChecker() {
        var hasFlash = 0;　　　　//是否安装了flash
        var flashVersion = 0;　　//flash版本

        if (document.all) {
            var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
            if (swf) {
                hasFlash = 1;
                VSwf = swf.GetVariable("$version");
                flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
            }
        } else {
            if (navigator.plugins && navigator.plugins.length > 0) {
                var swf = navigator.plugins["Shockwave Flash"];
                if (swf) {
                    hasFlash = 1;
                    var words = swf.description.split(" ");
                    for (var i = 0; i < words.length; ++i) {
                        if (isNaN(parseInt(words[i]))) continue;
                        flashVersion = parseInt(words[i]);
                    }
                }
            }
        }
        return {f: hasFlash, v: flashVersion};
    }
    var fls = flashChecker();
    var s = "";
    var ding = 0;
    if (!fls.f) {
        ding = 1;

    }

</script>
<!--主页内容start-->
<div class="paper">
    <!--公司简介start-->
    <div id="2ZTB" class="company_info">
        <div class="container">
            <h1>公司简介</h1>
            <p><i></i>泉州普惠云商网络技术有限公司成立于2015年，是一家致力于帮助实体行业互联网化的高新技术企业。
                公司拥有全国领先的互联网技术、移动互联网技术及相关信息化应用技术，目前公司在厦门、合肥、
                南京等地设立多个技术研发中心，拥有全国技术服务能力。普惠云商旗下拥有厦门${setting.siteName}营运中心，
                致力于打造“实体店+零售新模式”的终端网络应用，以技术创新改革实体店下滑为己任，
                以多元化需求提升消费者购物体验的终端竞争力为目标。其人性化的全新设计理念、现代化的高科技研发团队、
                多元化的终端需求，将为实体行业带来巨大的商业机遇。</p>
            <div class="company_content" id="company_content">
                <img src="${base}/resources/ztb/images/jianjie01.gif" alt=""/>
                <img src="${base}/resources/ztb/images/jianjie02.gif" alt=""/>
                <img src="${base}/resources/ztb/images/jianjie03.png" alt=""/>
                <img src="${base}/resources/ztb/images/jianjie04.gif" alt=""/>
                <img src="${base}/resources/ztb/images/jianjie05.gif" alt=""/>
                <img src="${base}/resources/ztb/images/jianjie06.gif" alt=""/>
            </div>
        </div>
    </div>
    <!--公司简介end-->
    <!--合作伙伴start-->
    <div id="3ZTB" class="partner_block">
        <div class="container">
            <h1>合作伙伴</h1>
            <h3>与国内知名品牌公司达成战略合作伙伴，共创共赢</h3>
            <div class="partner_content">
                <ul>
                    [#if cooperativePartners??&&cooperativePartners?has_content]
                [#list cooperativePartners as cooperativePartner]
                    <li>
                        <a href="javascript:;" target="_top">
                            <img class="img1" src="${cooperativePartner.licensePhoto}" alt=""/>
                            <img class="img2" src="${cooperativePartner.licensePhoto1}" alt=""/>
                        </a>
                    </li>
                [/#list]
                    [/#if]
                </ul>
            </div>
        </div>
    </div>
    <!--合作伙伴end-->
    <!--新闻动态start-->
    <div id="4ZTB" class="news_bulletin slideTxtBox">
        <div class="container">
            <div class="hd">
                <ul>
                    <li>
                        <div class="img">
                            <img class="img1" src="${base}/resources/ztb/images/IT-news.gif" alt=""/>
                            <img class="img2" src="${base}/resources/ztb/images/news.gif" alt=""/>
                        </div>
                        公司动态
                    </li>
                    <li>
                        <div class="img">
                            <img class="img1" src="${base}/resources/ztb/images/IT-news.gif" alt=""/>
                            <img class="img2" src="${base}/resources/ztb/images/news.gif" alt=""/>
                        </div>
                        行业新闻
                    </li>
                </ul>
            </div>
            <div class="bd">
                <div class="item">
                    <ul class="js-blog-list68">
                    [#if companyDynamics??&&companyDynamics?has_content]
                        [#list companyDynamics.content as companyDynamic]
                            <li>
                                <div class="item_l">
                                    <h2>${companyDynamic.createDate?string("dd")}</h2>
                                    <span>${companyDynamic.createDate?string("yyyy-MM")}</span>
                                </div>
                                <div class="item_r">
                                    <span title="${companyDynamic.title}">${abbreviate(companyDynamic.title, 220, "...")}</span>
                                    <a class="details" date-id="${companyDynamic.id}" href="javascript:;" target="_top">详情</a>
                                </div>
                            </li>
                        [/#list]
                    [/#if]
                    </ul>
                    <!--加载更多按钮-->
                    <div class="js-load-more load-more" date-id="68">
                        <a href="javascript:;">更多动态</a>
                    </div>
                </div>
                <div class="item">
                    <ul class="js-blog-list67">
                    [#if industryNews??&&industryNews?has_content]
                        [#list industryNews.content as industryNew]
                            <li>
                                <div class="item_l">
                                    <h2>${industryNew.createDate?string("dd")}</h2>
                                    <span>${industryNew.createDate?string("yyyy-MM")}</span>
                                </div>
                                <div class="item_r">
                                    <span title="${industryNew.title}">${abbreviate(industryNew.title, 220, "...")}</span>
                                    <a class="details" date-id="${industryNew.id}" href="javascript:;" target="_top">详情</a>
                                </div>
                            </li>
                        [/#list]
                    [/#if]
                    </ul>

                    <!--加载更多按钮-->
                    <div class="js-load-more load-more" date-id="67">
                        <a href="javascript:;">更多动态</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        /**
         * 文章标签切换
         */
        jQuery(".slideTxtBox").slide({
            mainCell: ".bd",
            effect: "left",
            easing: "swing",
            delayTime: 500,
            autoPlay: false
        });
    </script>
    <script type="text/javascript" src="${base}/resources/ztb/js/zepto.min.js"></script>
    <script type="text/javascript">
        $(function(){

            /*初始化*/
            var counter = 0; /*计数器*/
            var pageNumber = 2; /*offset*/
            var pageSize = 20; /*size*/

            /*首次加载*/
            //getData(pageStart, pageSize);

            /*监听加载更多*/
            $(document).on('click', '.js-load-more', function(){
                var _id =$(this).attr("date-id");
                counter ++;
                pageNumber = counter * pageSize;

                getData(pageNumber, pageSize ,_id);
            });


            function getData(offset,size,id){
                $.ajax({
                    type: 'GET',
                    url: '${base}/ztb/index/get/more/'+id+'.jhtml',
                    /**url: 'json/blog.json'+ '?' + offset + '/' + size,**/ /**这里offset,size无作用，仅方便调试**/
                    dataType: 'json',
                    date:{
                        pageNumber:offset,
                        pageSize:size
                    },
                    success: function(reponse){

                        var data = reponse;
                        var sum = reponse.length;

                        var result = '';

                        /************业务逻辑块：实现拼接html内容并append到页面*****************/

                        console.log(offset , size, sum);

                        /*如果剩下的记录数不够分页，就让分页数取剩下的记录数
                         * 例如分页数是5，只剩2条，则只取2条
                         *
                         * 实际MySQL查询时不写这个不会有问题
                         */
                        if(sum - offset < size ){
                            size = sum - offset;

                    }
                        /*使用for循环模拟SQL里的limit(offset,size)*/
                        for(var i=offset; i< (offset+size); i++){
                            result +='<li>'+
                                    '<div class="item_l"><h2>'+data[i].date+'</h2><span>'+data[i].year+'</span></div>'+
                                    '<div class="item_r"><span title="'+data[i].title+'">'+data[i].title+'</span><a class="details" href="javascript:;" target="_top">'+data[i].desc+'</a></div>'+
                                    '</li>';
                        }

                        $('.js-blog-list'+id).append(result);

                        /*******************************************/

                        /*隐藏more*/
                        if ( (offset + size) >= sum){
                            $(".js-load-more").hide();
                        }else{
                            $(".js-load-more").show();
                        }
                    }
////                    error: function(xhr, type){
////                        alert('Ajax error!');
////                    }
                });
            }
        });
    </script>
    <script src="${base}/resources/ztb/js/layer.js"></script>
    <script>
        //文章详情点击弹出
        $('.details').on('click', function () {
            var _id = $(this).attr("date-id");
            layer.open({
                type: 2,
                /**
                 * skin:颜色
                 */
                // skin: 'layui-layer-lan',
                title: '详情',
                fix: false,
                shadeClose: true,
                maxmin: true,
                area: ['870px', '500px'],
                content: '${base}/ztb/index/title/'+_id+'.jhtml'

            });
        });
    </script>
    <!--新闻动态end-->
</div>
<!--主页内容end-->

<div id="5ZTB" class="footer">
    <div class="container">
        <div class="left">
            <h2>联系我们</h2>
            <h4>总部地址：福建省泉州市鲤城区源和堂1916创意园区30栋</h4>
            <h4>办公地址：厦门市思明区观音山国际商务营运中心9号14层</h4>
            <h4>联系电话：0592-5028846</h4>
            <h4>咨询反馈：phys007@puhuiyunshang.com</h4>
        </div>
        <div class="content">
            <div class="bottom">
                <div class="img">
                    <img src="${base}/resources/ztb/images/erweima.gif" alt="" width="200" height="200"/>
                </div>
                <span>普惠云商微信公众号</span>
            </div>
            <div class="top">
                <h2>全国客服热线</h2>
                <span>400-9988-968</span>
            </div>
        </div>
    </div>
    <div class="wire"></div>
    <div class="container">
        <div class="ban_quan">
            <span>泉州普惠云网络技术有限公司&nbsp;&nbsp;&nbsp;版权所有&nbsp;&nbsp;闽ICP备07049452</span>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $(window).scroll(function () {
            //当距离页面顶部 大于等于top时 浮动
            var top = document.getElementById("brands-slider").scrollHeight;
            if ($(window).scrollTop() >= top) {
                $(".top-nav-block").addClass("change");
            } else {
                $(".top-nav-block").removeClass("change");
            }
        });
        //回到顶部
        $('.actGotop').click(function () {
            $('html,body').animate({scrollTop: '0px'}, 800);
        });

    });
    /**
     * 顶部悬浮栏及其定位到楼层
     */
    $('.top-nav-block ul li').click(function () {
        var ind = $('.top-nav-block ul li').index(this) + 1;
        var topVal = $('#' + ind + 'ZTB').offset().top;
        $('body,html').animate({scrollTop: topVal}, 1000)
    });

    $(window).scroll(scrolls);
    scrolls();
    function scrolls() {
        var f1, f2, f3, f4, f5;
        var fixRight = $('.top-nav-block ul li');
        var sTop = $(window).scrollTop();
        f1 = $('#1ZTB').offset().top;
        f2 = $('#2ZTB').offset().top;
        f3 = $('#3ZTB').offset().top;
        f4 = $('#4ZTB').offset().top;
        f5 = $('#5ZTB').offset().top;

        if (sTop >= f1) {
            fixRight.eq(0).addClass('active').siblings().removeClass('active');
        } else {
            fixRight.eq(0).removeClass('active');
        }
        if (sTop >= f2 - 100) {
            fixRight.eq(1).addClass('active').siblings().removeClass('active');
        }
        if (sTop >= f3 - 100) {
            fixRight.eq(2).addClass('active').siblings().removeClass('active');
        }
        if (sTop >= f4 - 100) {
            fixRight.eq(3).addClass('active').siblings().removeClass('active');
        }
        if (sTop >= f5 - 100) {
            fixRight.eq(4).addClass('active').siblings().removeClass('active');
        }
    }
</script>
</body>
</html>