<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-购物车</title>
    <meta name="keywords" content="${setting.siteName}-购物车" />
    <meta name="description" content="${setting.siteName}-购物车" />
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon">
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="interested">
        <div class="interestedNav">
            <ul class="container">
                <li class="on"><a href="javascript:;" >掌柜热卖</a></li>
                <li><a href="javascript:;" >最近浏览的</a></li>
                <li><a href="javascript:;" >最近收藏的</a></li>
                <li><a href="javascript:;" >猜你喜欢的</a></li>
            </ul>
        </div>
        <div class="interestedShow">
            [#--掌柜热卖--]
            <div class="shopkeeper-hot show display">
                <div class="share">
                    <div class="shopkeeper-hot-c hover">
                        <ul>
                            [#list hot as product]
                                [#if product_index gte 0&&product_index lte 4]
                                    <li>
                                        <div class="show-img">
                                        <a href="${base}/b2c/product/detail/${product.id}.jhtml" target="_blank">
                                            <img alt="" src="${product.thumbnail}"/>
                                        </a>
                                        </div>
                                        <div class="price">
                                        <span>
                                            ￥<em>${product.price?string("0.00")}</em>
                                        </span>
                                        <span class="txt">
                                            ￥<em>${product.marketPrice?string("0.00")}</em>
                                        </span>
                                            [#list product.tenant.promotions as promotion]
                                                [#if promotion.type=='mail']
                                                    <a target="_blank">包邮</a>
                                                    [#break /]
                                                [/#if]
                                            [/#list]
                                        </div>
                                        <div class="title">
                                            <a title="${product.fullName}" href="${base}/b2c/product/detail/${product.id}.jhtml" target="_parent">${product.fullName}</a>
                                        </div>
                                        <div class="volume">
                                        <span>
                                            <a href="javascript:;" target="_blank">销量：<em>${product.sales}</em></a>
                                        </span>
                                        <div>
                                            [#if product.tenant.noReason==true]
                                                <a href="javascript:;" target="_blank"><img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" alt=""></a>
                                            [/#if]
                                            [#list product.promotions as promotion]
                                                <a class="img" href="javascript:;" target="_blank">${promotion.name}</a>
                                            [/#list]
                                        </div>
                                        </div>
                                        <div class="discount">
                                            <a href="javascript:;" target="">[#if product.marketPrice>0]${((product.price/product.marketPrice)*10)?string('0.0')}折[/#if]</a>
                                        </div>
                                    </li>
                                [/#if]
                            [/#list]
                        </ul>
                        [#if hot??&&hot?has_content&&hot?size>5]
                            <ul class="hidden">
                                [#list hot as product]
                                    [#if product_index gte 5&&product_index lte 9]
                                        <li>
                                            <div class="show-img">
                                                <a href="${base}/b2c/product/detail/${product.id}.jhtml" target="_blank">
                                                    <img alt="" src="${product.thumbnail}"/>
                                                </a>
                                            </div>
                                            <div class="price">
                                            <span>
                                                ￥<em>${product.price?string("0.00")}</em>
                                            </span>
                                            <span class="txt">
                                                ￥<em>${product.marketPrice?string("0.00")}</em>
                                            </span>
                                                [#list product.tenant.promotions as promotion]
                                                    [#if promotion.type=='mail']
                                                        <a href="javascript:;" target="_blank">包邮</a>
                                                        [#break /]
                                                    [/#if]
                                                [/#list]
                                            </div>
                                            <div class="title">
                                                <a title="${product.fullName}" href="${base}/b2c/product/detail/${product.id}.jhtml" target="_parent">${product.fullName}</a>
                                            </div>
                                            <div class="volume">
                                            <span>
                                                <a href="javascript:;" target="_blank">销量：<em>${product.sales}</em></a>
                                            </span>
                                                <div>
                                                    [#if product.tenant.noReason==true]
                                                        <a href="javascript:;" target="_blank"><img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" alt=""></a>
                                                    [/#if]
                                                    [#list product.promotions as promotion]
                                                        <a class="img" href="javascript:;" target="_blank">${promotion.name}</a>
                                                    [/#list]
                                                </div>
                                            </div>
                                            <div class="discount">
                                                <a href="javascript:;" target="">[#if product.marketPrice>0]${((product.price/product.marketPrice)*10)?string('0.0')}折[/#if]</a>
                                            </div>
                                        </li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </div>
                    <ul class="pagination"></ul>
                    <div class="shopkeeper-hot-l common goL"><</div>
                    <div class="shopkeeper-hot-r common goR">></div>
                </div>
            </div>
            [#--最近浏览的--]
            <div class="recent-viewed show">
                <div class="share">
                    <div class="recent-viewed-c hover">
                    </div>
                    <ul class="pagination">
                    </ul>
                    <div class="recent-viewed-l common goL"><</div>
                    <div class="recent-viewed-r common goR">></div>
                </div>
            </div>
            [#--最近收藏的--]
            <div class="recent-favorites show">
                <div class="share">
                    <div class="recent-favorites-c hover">
                        <ul>
                            [#list collect as product]
                                [#if product_index gte 0&&product_index lte 4]
                                    <li>
                                        <div class="show-img">
                                            <a href="${base}/b2c/product/detail/${product.id}.jhtml" target="_blank">
                                                <img alt="" src="${product.thumbnail}"/>
                                            </a>
                                        </div>
                                        <div class="price">
                                        <span>
                                            ￥<em>${product.price?string('0.00')}</em>
                                        </span>
                                        </div>
                                        <div class="title">
                                            <a title="${product.fullName}" href="${base}/b2c/product/detail/${product.id}.jhtml" target="_parent">${product.fullName}</a>
                                        </div>
                                    </li>
                                [/#if]
                            [/#list]
                        </ul>
                        [#if collect??&&collect?has_content&&collect?size>5]
                            <ul class="hidden">
                                [#list collect as product]
                                    [#if product_index gte 5&&product_index lte 9]
                                        <li>
                                            <div class="show-img">
                                                <a href="${base}/b2c/product/detail/${product.id}.jhtml" target="_blank">
                                                    <img alt="" src="${product.thumbnail}"/>
                                                </a>
                                            </div>
                                            <div class="price">
                                        <span>
                                            ￥<em>${product.price?string('0.00')}</em>
                                        </span>
                                            </div>
                                            <div class="title">
                                                <a title="${product.fullName}" href="${base}/b2c/product/detail/${product.id}.jhtml" target="_parent">${product.fullName}</a>
                                            </div>
                                        </li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </div>
                    <ul class="pagination">
                        <li class="active">1</li>
                        <li>2</li>
                    </ul>
                    <div class="recent-favorites-l common goL"><</div>
                    <div class="recent-favorites-r common goR">></div>
                </div>
            </div>
            [#--猜你喜欢的--]
            <div class="guess-like show">
                <div class="share">
                    <div class="guess-like-c hover">
                        <ul>
                            [#list like as product]
                                [#if product_index gte 0&&product_index lte 4]
                                    <li>
                                        <div class="show-img">
                                            <a href="${base}/b2c/product/detail/${product.id}.jhtml" target="_blank">
                                                <img alt="" src="${product.thumbnail}"/>
                                            </a>
                                        </div>
                                        <div class="price">
                                            <a href="javascript:;" target="_blank">
                                                <span>
                                                    ￥<em>${product.price?string('0.00')}</em>
                                                </span>
                                                <span class="txt">
                                                    ￥<em>${product.marketPrice?string("0.00")}</em>
                                                </span>
                                            </a>
                                            <div class="line"></div>
                                        </div>
                                        <div class="title">
                                            <a title="${product.fullName}" href="${base}/b2c/product/detail/${product.id}.jhtml" target="_parent">${product.fullName}</a>
                                        </div>
                                        <div class="volume">
                                            <span>
                                                <a href="javascript:;" target="_blank">销量：<em>${product.sales}</em></a>
                                            </span>
                                            <div>
                                            [#if product.tenant.noReason==true]
                                                <a href="javascript:;" target="_blank"><img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" alt=""></a>
                                            [/#if]
                                            </div>
                                        </div>
                                        [#--<div class="recommend_by">--]
                                            [#--<a href="javascript:;" target="_blank" title="根据您浏览的&quot;玻璃杯&quot;推荐">--]
                                                [#--根据您浏览的<span>"玻璃杯"</span>推荐--]
                                            [#--</a>--]
                                        [#--</div>--]
                                    </li>
                                [/#if]
                            [/#list]
                        </ul>
                        [#if like??&&like?has_content&&like?size>5]
                            <ul class="hidden">
                                [#list like as product]
                                    [#if product_index gte 5&&product_index lte 9]
                                        <li>
                                            <div class="show-img">
                                                <a href="${base}/b2c/product/detail/${product.id}.jhtml" target="_blank">
                                                    <img alt="" src="${product.thumbnail}"/>
                                                </a>
                                            </div>
                                            <div class="price">
                                                <a href="javascript:;" target="_blank">
                                                        <span>
                                                            ￥<em>${product.price?string('0.00')}</em>
                                                        </span>
                                                        <span class="txt">
                                                            ￥<em>${product.marketPrice?string("0.00")}</em>
                                                        </span>
                                                </a>
                                                <div class="line"></div>
                                            </div>
                                            <div class="title">
                                                <a title="${product.fullName}" href="${base}/b2c/product/detail/${product.id}.jhtml" target="_parent">${product.fullName}</a>
                                            </div>
                                            <div class="volume">
                                                    <span>
                                                        <a href="javascript:;" target="_blank">销量：<em>${product.sales}</em></a>
                                                    </span>
                                                <div>
                                                    [#if product.tenant.noReason==true]
                                                        <a href="javascript:;" target="_blank"><img src="//img.alicdn.com/tps/i3/T1Vyl6FCBlXXaSQP_X-16-16.png" alt=""></a>
                                                    [/#if]
                                                </div>
                                            </div>
                                            [#--<div class="recommend_by">--]
                                                [#--<a href="javascript:;" target="_blank" title="根据您浏览的&quot;玻璃杯&quot;推荐">--]
                                                    [#--根据您浏览的<span>"玻璃杯"</span>推荐--]
                                                [#--</a>--]
                                            [#--</div>--]
                                        </li>
                                    [/#if]
                                [/#list]
                            </ul>
                        [/#if]
                    </div>
                    <ul class="pagination">
                        <li class="active">1</li>
                        <li>2</li>
                    </ul>
                    <div class="guess-like-l common goL"><</div>
                    <div class="guess-like-r common goR">></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        var $showNav=$(".show").eq(0);
        var $hoverNav=$(".hover").eq(0);
        var size=$hoverNav.eq(0).find("ul").size();//轮播页数
        var timeout;//定时器
        var interval;//计时器
        var ul_index=0;//轮播的ul

        /*--标签切换--*/
        $("div.interested .interestedNav ul li").each(function(index){
            var $this = $(this);

            $this.mouseover(function(){
                $showNav.unbind('mouseenter mouseleave');
                clearInterval(interval);
                $showNav.find(".goL").unbind('click');
                $showNav.find(".goR").unbind('click');
                $showNav=$(".show").eq(index);
                $hoverNav=$(".hover").eq(index);
                size=$hoverNav.find("ul").size();

                timeout = setTimeout(function(){
                    $(".show").removeClass("display");
                    $showNav.addClass("display");
                    $("div.interested .interestedNav ul li.on").removeClass("on");
                    $this.addClass("on");

                },300);

                init();

            }).mouseout(function(){
                clearTimeout(timeout);

            })
        });

        function init(){
            //初始化
            ul_index=0;
            $showNav.find('.pagination').html("");
            if(size>1){
                for(var i=1; i<=size; i++){
                    var li="<li>"+i+"</li>";
                    $showNav.find('.pagination').append(li);
                }
            }
            //手动控制轮播
            $showNav.find(".pagination li").eq(0).addClass("active");
            $showNav.find(".pagination li").mouseover(function(){
                $(this).addClass("active").siblings().removeClass("active");
                ul_index=$(this).index();
                $hoverNav.find("ul").eq(ul_index).stop().fadeIn(300).siblings().stop().fadeOut(300);
            });
            //左边按钮点击事件
            $showNav.find(".goL").click(function(){
                moveL();
            });
            //右边按钮点击事件
            $showNav.find(".goR").click(function(){
                move();
            });
            //自动轮播
            interval=setInterval(move,2000);
            //定时器的开始与结束
            $showNav.hover(function(){
                clearInterval(interval);
            },function(){
                interval=setInterval(move,2000);
            });
        }

        init();

        //核心向左运动函数
        function moveL(){
            ul_index--;
            if(ul_index==-1){
                ul_index=size-1;
            }
            $showNav.find(".pagination li").eq(ul_index).addClass("active").siblings().removeClass("active");
            $hoverNav.find("ul").eq(ul_index).fadeIn(300).siblings().fadeOut(300);
        }

        //核心向右运动函数
        function move(){
            ul_index++;
            if(ul_index==size){
                ul_index=0;
            }
            $showNav.find(".pagination li").eq(ul_index).addClass("active").siblings().removeClass("active");
            $hoverNav.find("ul").eq(ul_index).fadeIn(300).siblings().fadeOut(300);
        }

        // 浏览记录
        var historyProduct = getCookie("historyProduct");
        var historyProductIds = historyProduct != null ? historyProduct.split(",") : new Array();
        $.ajax({
            url: "${base}/b2c/product/history.jhtml",
            type: "GET",
            data: {ids: historyProductIds},
            dataType: "json",
            traditional: true,
            cache: false,
            success: function(data) {
                var html1='<ul>';
                var html2='<ul class="hidden">';
                $.each(data,function(index,product){
                    if(index<5){
                        html1+='<li>'+
                                '<div class="show-img">'+
                                '<a href="${base}/b2c/product/detail/'+product.id+'.jhtml" target="_blank">'+
                                '<img alt="" src="'+product.thumbnail+'"/>'+
                                '</a>'+
                                '</div>'+
                                '<div class="price">'+
                                '<span>'+
                                '￥<em>'+product.price+'</em>'+
                                '</span>'+
                                '</div>'+
                                '<div class="title">'+
                                '<a title="'+product.fullName+'" href="${base}/b2c/product/detail/'+product.id+'.jhtml" target="_parent">'+product.fullName+'</a>'+
                                '</div>'+
                                '</li>';
                    }else if(index<10){
                        html2+='<li>'+
                                '<div class="show-img">'+
                                '<a href="javascript:;" target="_blank">'+
                                '<img alt="" src="'+product.thumbnail+'"/>'+
                                '</a>'+
                                '</div>'+
                                '<div class="price">'+
                                '<span>'+
                                '￥<em>'+product.price+'</em>'+
                                '</span>'+
                                '</div>'+
                                '<div class="title">'+
                                '<a title="'+product.fullName+'" href="${base}/b2c/product/detail/'+product.id+'.jhtml" target="_parent">'+product.fullName+'</a>'+
                                '</div>'+
                                '</li>';
                    }

                });
                html1+="</ul>";
                html2+="</ul>";
                $(".recent-viewed-c").append(html1);
                if(data.length>5){
                    $(".recent-viewed-c").append(html2);
                }
            }
        });
        $("#interest",parent.document).height(document.body.clientHeight);
    });
</script>
</body>
</html>