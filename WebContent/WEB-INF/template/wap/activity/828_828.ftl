<!DOCTYPE html>
<html>

<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${setting.siteName}</title>

</head>

<body class="bg_act_828" onload="load()">
[#--广告图区域--]
<div id="activity_banner" class="banner ACT828">
    <div class="img">
        [@ad_position id=141 areaId=area.id count=1/]
        [#--<img src="${base}/resources/wap/3.0/image/activity_828_banner_001.jpg" width="100%"/>--]
    </div>
    <a id="guize" class="clr-grey08" href="javascript:void(0);">随机减规则</a>
</div>
<div class="guize ACT828">
    <div class="guize_item">
        [#--<h2>${setting.siteName}来啦！同城好货，你挑我买<br/>（最高可享100元免单）</h2>--]
        [#--<p>在吗？</p>--]
        [#--<p>想跟你讲件事。</p>--]
        [#--<p>${setting.siteName}推出“随机减”活动，</p>--]
        [#--<p>购买商品随机减5-100元，</p>--]
        [#--<p>最高可享100元免单，</p>--]
        [#--<p>费用将由货网大大来报销。</p>--]
        [#--<p>商品价格要满20元呦，</p>--]
        [#--<p>每人每天限购买2个活动商品。</p>--]
        [#--<p>时间：8月26日-8月28日</p>--]
        [#--<p>客服电话：400-825-827</p>--]
        [#--<p>本活动最终解释权<br/>归安徽威思通电子商务有限公司所有</p>--]
        ${activityPlanning.introduction}
    </div>
</div>
<script>
    var act_banner_a = document.getElementById("guize");
    act_banner_a.onclick = function () {
        $(".guize").css("display","block");
    };

    function load(){
        document.getElementById("guize_delete").click();
    }

    function delet(){
        $(".guize").css("display","none");
    }
</script>
[#--倒计时区域--]
<div id="activity_timer" class="timer ACT828 clr-grey08">
    距离结束：
    <span id="day"></span>
    天
    <span id="hour"></span>
    时
    <span id="mini"></span>
    分
    <span id="sec"></span>
    秒
</div>
[#--活动菜单栏--]
<div id="activity_nav828" class="nav ACT828 bg-05">

[#assign activity0=0 activity00=0 activity1=0 activity11=0]
[#list activityPlanning.adPositions as adPosition]
    [#if adPosition_index==0]
        [#assign activity0=adPosition.id activity00=adPosition.description]
    [#elseif adPosition_index==1]
        [#assign activity1=adPosition.id activity11=adPosition.description]
    [/#if]
    <a onclick="scroller('act-${adPosition_index}',500);" href="javascript:void(0);">${(adPosition.description)!}</a>
[/#list]
    <a onclick="scroller('act-activityTenant',500);" href="javascript:void(0);">推荐商家</a>
    <a onclick="scroller('act-tenant',500);" href="javascript:void(0);">附近商家</a>
</div>
[#--活动-内容--]
<div class="content">
[#--活动-团购--]
    <div id="act-0" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_828_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">${activity00}</h2>
            <span>热销清仓低至39元</span>
        </div>
        <div class="items">
        [@adActivity id = activity0 areaId=area.id count=20]
            [#if adPosition?has_content]
                [#list adPosition as ads]
                    [#assign url='' ]
                    [#if ads.linkId??&&ads.linkId?has_content]
                        [#if ads.linkType=="product"]
                            [#assign url='wap/product/content/'+ads.linkId+'/product.jhtml' ]
                        [#elseif ads.linkType=="tenant"]
                            [#assign url='wap/tenant/index/'+ads.linkId+'.jhtml' ]
                        [/#if]
                    [#else ]
                        [#assign url=ads.url]
                    [/#if]
                    <div class="item">
                        <a [#if url??&&url?has_content] href="${base}/${url}"[/#if]>
                            <div class="img">
                                <img src="${ads.path}" width="100%"/>
                            </div>
                        </a>
                    </div>
                [/#list]
            [/#if]
        [/@adActivity]
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    </div>
[#--活动-清仓--]
    <div id="act-1" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_828_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">${activity11}</h2>
            <span>总有一件适合你的</span>
        </div>
        <div class="items">

        [@adActivity id = activity1 areaId=area.id count=20]
            [#if adPosition?has_content]
                [#list adPosition as ads]
                    [#assign url1='' ]
                    [#if ads.linkId??&&ads.linkId?has_content]
                        [#if ads.linkType=="product"]
                            [#assign url1='wap/product/content/'+ads.linkId+'/product.jhtml' ]
                        [#elseif ads.linkType=="tenant"]
                            [#assign url1='wap/tenant/index/'+ads.linkId+'.jhtml' ]
                        [/#if]
                    [#else ]
                        [#assign url1=ads.url ]
                    [/#if]
                    <div class="item">
                        <a [#if url1??&&url1?has_content] href="${base}/${url1}"[/#if]>
                            <div class="img">
                                <img src="${ads.path}" width="100%"/>
                            </div>
                            <div class="title bg-05 hidden">
                                <span>35元起</span>
                                <span>七匹狼皮具皮包专场</span>
                                <span>剩三天</span>
                            </div>
                        </a>
                    </div>
                [/#list]
            [/#if]
        [/@adActivity]
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    </div>

[#--全城商圈活动广告--]
    <div class="img" style="padding-top: 2vw;">
        [@ad_position id=142 areaId=area.id count=1/]
        [#--<img src="${base}/resources/wap/3.0/image/activity_828_banner_002.jpg" width="100%"/>--]
    </div>
[#--活动-推荐--]
    <div id="act-activityTenant" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_828_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">推荐商家</h2>
            <span>全城优质商家精选</span>
        </div>
        <div class="items">
            <ul>
            [#list activityPlanning.tenants as tenant]
                [#list tenant.tags as tag]
                    [#if tag.id==32]
                        <li>
                            <a href="${base}/wap/tenant/index/${tenant.id}.jhtml">
                                <div class="img">
                                    <img class="lazy" src="${tenant.logo}" width="100%"/>
                                </div>
                                <div class="title_info bg-05">
                                    <div class="title_l">
                                        <span>${tenant.name}</span>
                                        <span>全场随机满减</span>
                                        <span>立即进店</span>
                                    </div>
                                </div>
                            </a>
                        </li>
                    [/#if]
                [/#list]
            [/#list]
            </ul>
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    </div>
[#--活动-附近--]
    <div id="act-tenant" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_828_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">附近商家</h2>
            <span>全城优质商家精选</span>
        </div>
        <div class="items" id="nearByTenant">
        [#-- 红：#ff635b;紫：#c183e2;粉：#ff6670;黄：#fcd04c--]
        [#-- 该页面中使用的优惠图标颜色色值--]
            [#list tenants as tenant]
            <a class="weui_cells box_tenant col-1" href="${base}/wap/tenant/index/${tenant.id}.jhtml">
            [#-- box_tenant-info--]
                <div class="weui_cell box_tenant-info">
                    <div class="weui_cell_hd">
                        <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"
                             data-original="${tenant.thumbnail}">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary ">
                        <p class="storename ft-bs15">
                            <span>${tenant.shortName}</span>
                            <i class="iconfont clr-grey11 ft-bs2">&#xe635;</i>
                        </p>
                    [#-- 评分等级--]
                        [#if tenant.grade > 0]
                        <div class="starlevelsR">
                            <div class="cf ft-bs0 starlevels" data-starnum="${tenant.grade}" data-starlevelized="false">
                                <i></i>
                                <i></i>
                                <i></i>
                                <i></i>
                                <i></i>
                            </div>
                            [#if tenant.grade??&&tenant.grade?has_content]
                            <span class="ft-bs0 clr-red05">${tenant.grade}分</span>
                            [/#if]
                        </div>
                        [/#if]
                        <div class="weui_cell mainAaddress">
                            <div class="weui_cell_hd ft-bs0 mainbusiness">
                                主营：${tenant.tenantCategoryName}
                            </div>
                            <div class="weui_cell_bd weui_cell_primary ft-bs0 address">
                                ${(tenant.distance/1000)?string("0.00")}km
                            </div>
                        </div>
                    </div>
                </div>
            [#-- box_tenant-promotions--]
                [#if tenant.promotions??&&tenant.promotions?has_content]
                <div class="weui_cell box_tenant-promotions">
                    <div class="weui_cell_hd">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary ThumbIconLeft ">
                        [#list tenant.promotions as promotion]
                        <p class="promdesc">
                            [#if promotion.type]
                            <i class="iconfont ft-bs05 promtag pt-${promotion.type}"></i>
                            [/#if]
                            <span class="ft-bs0 clr-grey02">${promotion.name}.</span>
                        </p>
                        [/#list]
                    </div>
                </div>
                [/#if]
            </a>
            [/#list]
        </div>
    </div>
</div>
<div id="actGotop" class="actGotop ACT828">
    <img src="${base}/resources/wap/3.0/image/act_Top_002.png" width="100%"/>
</div>

[#--<script type="text/x-handlebars-template" id="tpl_itemlist">--]
    [#--{{#each data}}--]
    [#--[#include "/wap/include/tenantlist-indexnew.ftl"/]--]
    [#--{{/each}}--]
[#--</script>--]

<!-- BOTTOM SCRIPT-->
[#include "/wap/include/resource-3.0-part2.ftl"/]


<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/activity/count_down.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/activity/scroller_tag.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/activity/activity_828.js"></script>
<!-- JS:receiver-->
<script type="text/javascript">
    /**
     *receive remote data here first
     * 后台刷入数据使用前先存储到_TH_
     */
    var _TH_ = {};
    /**
     * 后台模板路径
     */
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    /**
     * 基准路径base
     */
    _TH_.base = "${base}"; //${base}
    /**
     *懒加载
     */
    $(".lazy").picLazyLoad({
        threshold: 100,
        placeholder: _TH_.base + '/resources/wap/2.0/images/AccountBitmap-product.png'
    });
    /**
     *倒计时调用
     */
    var endTime = "${(activityPlanning.endDate?string("yyyy-MM-dd HH:mm:ss"))!}";
    endTime = endTime.replace(/-/g, ",").replace(/:/g, ",").replace(/ /g, ",");
    var d =  endTime.split(",");
    var obj = {
        sec: document.getElementById("sec"),//秒
        mini: document.getElementById("mini"),//分
        hour: document.getElementById("hour"),//时
        day: document.getElementById("day")//天
    };
    fnTimeCountDown(d, obj);//启用方法
    //_wxSDK.onMenuShare();
    _wxSDK.initInterface($script);
    //var compilerItemlist = Handlebars.compile($("#tpl_itemlist").html());
    //$('#nearByTenant').html(compilerItemlist('${tenants}'));
</script>
<script type="text/javascript">
    //返回顶部
    window.onload = function () {
        var gotop = document.getElementById("actGotop");
        var timer = null;

        var pagelookheight = document.documentElement.clientHeight;//获取当前可见高度
        window.onscroll = function () {
//            alert("hello");
            var backtop = document.body.scrollTop;
            if (backtop >= pagelookheight) {//当滚动高度超过当前可见高度则显示否则隐藏
                gotop.style.display = "block";
            } else {
                gotop.style.display = "none";
            }
        };

        gotop.onclick = function () {
//            alert("hello");
            timer = setInterval(function () {
                var backtop = document.body.scrollTop;
                var speedtop = backtop / 5;//向上滚动速度
//                document.body.scrollTop -= 100;
                document.body.scrollTop = backtop - speedtop;
                if (backtop == 0) {//到达顶部关闭向上滚动事件
                    clearInterval(timer);
                }
            }, 30);
        }
    }
</script>
</body>

</html>












































































