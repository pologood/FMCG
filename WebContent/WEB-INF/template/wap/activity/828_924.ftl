<!DOCTYPE html>
<html>

<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${setting.siteName}</title>

</head>

<body class="bg_act_924" onload="load()">
[#--广告图区域--]
<div id="activity_banner" class="banner ACT828">
    <div class="img">
    [@ad_position id=141 areaId=area.id count=1/]
    </div>
    <a id="guize" class="clr-grey08" href="javascript:void(0);">活动规则</a>
</div>
<div class="guize ACT828">
    <div class="guize_item">
        ${activityPlanning.introduction}
    </div>
    <div class="hidden"
         style="color: #7d14c0;background-color: #fff;width: 90%;border-radius: 1vw;margin: 0 auto;margin-top: -9vw;text-align: center;padding: 8vw 0;">
        <a id="guize_delete"
           style="background-color: #7d14c0;color: #fff;padding: 3vw 15vw;border-radius: 1vw;display: inline-block;"
           href="javascript:;" onclick="delet();">知道了</a>
    </div>
</div>
<script>
    var act_banner_a = document.getElementById("guize");
    act_banner_a.onclick = function () {
        $(".guize").css("display", "block");
    };

    function load() {
        document.getElementById("guize_delete").click();
    }

    function delet() {
        $(".guize").css("display", "none");
    }
</script>
<div class="gradient ACT828">
[#--倒计时区域--]
    <div id="activity_timer" class="timer ACT828 clr-grey08">
        <div class="ACT924"></div>
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
</div>
[#--活动菜单栏--]
<div id="activity_nav828" class="nav ACT828 bg-05">

[#assign activity0=0 activity00=0 activity1=0 activity11=0]
[#list adPositions?sort_by(["id"]) as adPosition]
    [#if adPosition_index==0]
        [#assign activity0=adPosition.id activity00=adPosition.description]
    [#elseif adPosition_index==1]
        [#assign activity1=adPosition.id activity11=adPosition.description]
    [/#if]
    <a onclick="scroller('act-${adPosition_index}',500);" href="javascript:void(0);">
        <span>${(adPosition.description)!}</span>
        <i>HOT</i>
    </a>
[/#list]
    <a onclick="scroller('act-activityTenant',500);" href="javascript:void(0);"><span>优质商家</span></a>
    <a onclick="scroller('act-tenant',500);" href="javascript:void(0);"><span>附近商家</span></a>
</div>
[#--活动-内容--]
<div class="content">
[#--活动-团购--]
    <div id="act-0" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_924_titler_001.png" width="100%"/>

            <h2 class="clr-grey08">[#--${activity00}--]全/城/热/门/商/圈</h2>
            <span>美食与美景更配</span>
        </div>
        <div class="items">
            <div class="ACT924">
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
                        [#if ads.linkType=="community"]
                            [#assign url='wap/activity/community/'+ads.url+'.jhtml' ]
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
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    </div>
[#--活动-清仓--]
    <div id="act-1" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_924_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">[#--${activity11}--]全/城/特/惠/清/仓</h2>
            <span>想给你省更多的钱</span>
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
                    [#if ads.linkType=="community"]
                        [#assign url1='wap/activity/community/'+ads.url+'.jhtml' ]
                    [/#if]
                    <div class="item bg-05">
                        <a [#if url1??&&url1?has_content] href="${base}/${url1}"[/#if]>
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

[#--全城商圈活动广告--]
    <div class="img" style="padding-top: 2vw;">
    [@ad_position id=142 areaId=area.id count=1/]
    </div>
[#--活动-推荐--]
    <div id="act-activityTenant" class="list_item ACT828">
        <div class="top">
            <img src="${base}/resources/wap/3.0/image/activity_924_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">全/城/最/优/商/家</h2>
            <span>要讲究不将就</span>
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
                                        <span>主营：[#if tenant.tenantCategory??]${tenant.tenantCategory.name}[#else ]${abbreviate(tenant.getIntroduction(),20,"..")}[/#if]</span>
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
            <img src="${base}/resources/wap/3.0/image/activity_924_titler_001.png" width="100%"/>
            <h2 class="clr-grey08">离/我/最/近/商/家</h2>
            <span>远亲不若近邻</span>
        </div>
        <div class="items" id="nearByTenant">
        </div>
    </div>
</div>
<div id="actGotop" class="actGotop ACT828">
    <img src="${base}/resources/wap/3.0/image/act_Top_002.png" width="100%"/>
</div>
<script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>

<script type="text/x-handlebars-template" id="tpl_itemlist">
    {{#each data}}
    [#include "/wap/include/tenantlist-indexnew.ftl"/]
    {{/each}}
</script>

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
    var d = endTime.split(",");
    var obj = {
        sec: document.getElementById("sec"),//秒
        mini: document.getElementById("mini"),//分
        hour: document.getElementById("hour"),//时
        day: document.getElementById("day")//天
    };
    fnTimeCountDown(d, obj);//启用方法
    //_wxSDK.onMenuShare();


    $(function () {

        [#--$.ajax({--]
                    [#--url: _TH_.base + "/wap/activity/get/location.jhtml",--]
                    [#--type: "POST",--]
                    [#--data: {--]
                        [#--id: ${activityPlanning.id},--]
                        [#--lat: 31.820587,--]
                        [#--lng: 117.227239--]
                    [#--}--]
                [#--}--]
        [#--).done(function (data) {--]
                    [#--if (data.message.type == "success") {--]
                        [#--var compilerItemlist = Handlebars.compile($("#tpl_itemlist").html());--]
                        [#--$('#nearByTenant').html(compilerItemlist(data));--]
                        [#--$(".lazy").picLazyLoad({--]
                            [#--threshold: 100,--]
                            [#--placeholder: _TH_.base + '/resources/wap/2.0/images/AccountBitmap-product.png'--]
                        [#--});--]
                    [#--} else {--]
                        [#--$(".ONCET").tip("addTask", {--]
                            [#--sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',--]
                            [#--txt: "data.message.content"--]
                        [#--});--]
                    [#--}--]
                [#--}--]
        [#--).fail(function () {--]
                    [#--console.log("error");--]
                [#--}--]
        [#--);--]


        _wxSDK.initInterface($script, {
            afterOnMenuShare: function () {
                _wxSDK.getLocation()
                        .done(function (lat, lng) {
                            $.ajax({
                                url: _TH_.base + "/wap/activity/get/location.jhtml",
                                type: "POST",
                                data: {
                                    id: ${activityPlanning.id},
                                    lat: lat,
                                    lng: lng
                                }
                            }).done(function (data) {
                                if (data.message.type == "success") {
                                    var compilerItemlist = Handlebars.compile($("#tpl_itemlist").html());
                                    $('#nearByTenant').html(compilerItemlist(data));
                                    $(".lazy").picLazyLoad({
                                        threshold: 100,
                                        placeholder: _TH_.base + '/resources/wap/2.0/images/AccountBitmap-product.png'
                                    });
                                } else {
                                    $(".ONCET").tip("addTask", {
                                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                        txt: "data.message.content"
                                    });
                                }
                            }).fail(function () {
                                console.log("erroe");
                            });
                        })
                        .fail(function () {
                            console.log("erroe");
                        });
            }
        });
    });

    //_wxSDK.initInterface($script);

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