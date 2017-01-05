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
    <!--商家等级展示开始-->
    <div class="merchant-title pc-bg">
        <div class="container shop-summary">
            <span class="line-left">
                <span class="shop-rank">
                    <a target="_blank">
                    [#if tenant.score>0]
                        [#list 1..tenant.score as t]
                            <img src="//gtms03.alicdn.com/tps/i4/TB17JRyFVXXXXXhXpXXxPfUFXXX-16-16.gif">
                        [/#list]
                    [/#if]
                    </a>
                </span>
                <span class="shop-name">
                    店铺：<a href="${base}/b2c/tenant/index.jhtml?id=${tenant.id}">${tenant.name}</a>
                </span>
                <span class="shop-grade">
                    <span class="shop-grade-bg">
                        <span class="shop-grade-red" style="width: ${((tenant.score/5)*100)!0}%;"></span>
                    </span>
                    <em>${tenant.score}分</em>
                </span>
            </span>
            <span class="line-right">
                <a class="shop-collect" href="javascript:;">
                    <em collected="${collected}" [#if collected==false]style="color: grey;" [/#if]>★</em>
                    <span onclick="javascript:$(this).prev().click();">收藏店铺</span>
                </a>
            </span>
        </div>
    </div>
    <!--商家等级展示结束-->
    <!--商家横幅开始-->
    <div class="merchant-brands-slider">
        <div class="container overflow-v">
            <div class="merchant-slider-inner">
                <div class="merchant-slider-main">
                    <ul class="merchant-slider-items">
                        <li class=" ">
                            <a href="javascript:;">
                            [@ad_position id=119 count=1 tenantId=tenant.id/]
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!--商家横幅结束-->
    <!--主导航开始-->
    <div class="mainNav">
        <div class="container ">
            <div class="merchantNav">
                <ul>
                    <li>
                        <a href="${base}/b2c/tenant/index.jhtml?id=${tenant.id}">首页</a>
                    </li>
                    <li>
                        <a href="${base}/b2c/tenant/search.jhtml?tenantId=${tenant.id}">所有商品</a>
                    </li>
                [#list tenant.productCategoryTenants as productCategoryTenant]
                    [#if productCategoryTenant_index<6]
                        <li>
                            <a href="${base}/b2c/tenant/search.jhtml?tenantId=${tenant.id}&productCategoryTenantId=${productCategoryTenant.id}">${productCategoryTenant.name}</a>
                        </li>
                    [/#if]
                [/#list]
                </ul>
                <div class="merchantNav-search">
                    <input id="keyWord" value="" placeholder="输入商品名称" autocomplete="off" accesskey="s" type="text">
                    <button type="button" value="" onclick="searchProduct('${tenant.id}')">搜索</button>
                </div>
            </div>
        </div>
    </div>
    <!--主导航结束-->
</div>
<script type="text/javascript">
    $(function(){
        /*所有商品分类*/
        $('.menuNav').css('display','none');
        $('.allCategorys').hover(function(){
            $('.menuNav').css('display','block');
        },function(){
            $('.menuNav').css('display','none');
        });

        $('.menuNav ul>li').hover(function(){
            var eq = $('.menuNav ul>li').index(this),//获取当前滑过是第几个元素
                    h = $('.menuNav').offset().top,//获取当前下拉菜单距离窗口多少像素
                    s = $(window).scrollTop(),//获取游览器滚动了多少高度
                    i = $(this).offset().top,//当前元素滑过距离窗口多少像素
                    item = $(this).children('.menuNavList').height(),//下拉菜单子类内容容器的高度
                    sort = $('.menuNav').height();//父类分类列表容器的高度

            if ( item < sort ){//如果子类的高度小于父类的高度
                if ( eq == 0 ){
                    $(this).children('.menuNavList').css('top', (i-h));
                } else {
                    $(this).children('.menuNavList').css('top', (i-h)+1);
                }
            } else {
                if ( s > h ) {//判断子类的显示位置，如果滚动的高度大于所有分类列表容器的高度
                    if ( i-s > 0 ){//则 继续判断当前滑过容器的位置 是否有一半超出窗口一半在窗口内显示的Bug,
                        $(this).children('.menuNavList').css('top', (s-h)+0 );
                    } else {
                        $(this).children('.menuNavList').css('top', (s-h)-(-(i-s))+0 );
                    }
                } else {
                    $(this).children('.menuNavList').css('top', 0 );
                }
            }

            $(this).addClass('now');
            $(this).children('.menuNavList').css('display','block');
        },function(){
            $(this).removeClass('now');
            $(this).children('.menuNavList').css('display','none');
        });

        //    $('.item > .item-list > .close').click(function(){
        //        $(this).parent().parent().removeClass('hover');
        //        $(this).parent().hide();
        //    });

        $(".shop-collect em").click(function(){
            var $this=$(this);
            var memberId="${(member.id)!}";
            if(memberId==""){
                location.href = "${base}/b2c/login.jhtml?redirectUrl=${base}/b2c/tenant/index.jhtml?id=${tenant.id}";
                return;
            }
            var collected=$this.attr("collected");
            if(collected=="true"){
                $.ajax({
                    type:'post',
                    url: "${base}/wap/member/favorite/delete.jhtml",
                    data: {
                        id:${tenant.id},
                        type: "1"
                    },
                    dataType:'json',
                    success: function (data) {
                        $.message(data.message);
                        if(data.message.type=="success"){
                            $this.css("color","grey");
                            $this.attr("collected","false");
                        }
                    }
                });
            }else{
                $.ajax({
                    type:'post',
                    url: "${base}/wap/member/favorite/add.jhtml",
                    data: {
                        id:${tenant.id},
                        type: "1"
                    },
                    dataType:'json',
                    success: function (data) {
                        $.message(data.message);
                        if(data.message.type=="success"){
                            $this.css("color","#ee724d");
                            $this.attr("collected","true");
                        }
                    }
                });
            }
        });
    });
</script>