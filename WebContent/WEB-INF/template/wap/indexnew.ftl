<!DOCTYPE html>
<html>
<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${setting.siteName}</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/pluginrevise.css">
</head>
<body class="bg-00">
[#-- topbar--]
<div class="topbar bg-05 IDN" data-emptyto="topbar">
[#-- 扫一扫--]
    <a href="javascript:;" id="scan_fun">
        <i class="iconfont ft-bs4">&#xe656;</i>
    </a>
[#-- 地区选择--]
    <a href="${base}/wap/area/city.jhtml" class="ft-bs3">
        <i class="iconfont">&#xe623;</i>
        <span>${area.name}</span>
    </a>
[#-- 搜索--]
    <a href="${base}/wap/top_search.jhtml?type=tenant">
        <i class="iconfont ft-bs3">&#xe640;</i>
    </a>
</div>
[#-- 高度占位元素--]
<div data-emptyfor="topbar"></div>
[#-- 轮播--]
<div id="carousel" class="carousel IDN" style="position: relative;">
    <img src="${base}/resources/wap/3.0/image/blank/blank640x250.png" alt="">
</div>
[#-- 频道--]
<div class="channels IDN">
[#list productChannels as channel]
[#--<a href="${base}/wap/tenant/list.jhtml?channelId=${channel.id}" class="it ft-bs1">--]
    <a href="${base}/wap/product_channel/channel/${channel.id}.jhtml" class="it ft-bs1">
        <div class="channel_sign_rap">
            <img class="lazy channel_sign anm-trsi anm-init"
                 src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                 data-original="${channel.image}">
        </div>
        <p class="ft-bs0 clr-grey02">${channel.name}</p>
    </a>
[/#list]
</div>
<div id="act_banner">
    <div class="img">
    [@ad_position id = 131 areaId=area.id  count=0]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
            [#if ads.linkType=="unionActivity"]
                <div class="item">
                    <a href="${base}/wap/activity/union/activity/${ads.linkId}.jhtml">
                        <div class="img">
                            <img src="${ads.path}" width="100%"/>
                        </div>
                    </a>
                </div>
            [/#if]
            [/#list]
        [/#if]
    [/@ad_position]
    </div>
</div>

[#-- 广告位--]
<div class="adsboard IDN">
[@ad_position id=127 areaId=area.id count=0]
    [#if adPosition?has_content]
        [#list adPosition.ads as ads]
            [#assign _url = ""]
            [#if ads.linkId??&&ads.linkId?has_content]
                [#if ads.linkType=="product"]
                    [#assign _url = "/wap/product/content/"+ads.linkId+"/product.jhtml"]
                [#elseif ads.linkType=="tenant"]
                    [#assign _url = "/wap/tenant/index/"+ads.linkId+".jhtml"]
                [/#if]
            [#else ]
                [#assign _url = ads.url]
            [/#if]
            <a href="${_url}" class="L" style="background-image:url(${ads.path})">
                <img src="${ads.path}" class="none"></a>
        [/#list]
    [/#if]
[/@ad_position]
    <div class="R">
    [@ad_position id=128 areaId=area.id count=0]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
                [#assign _url = ""]
                [#if ads.linkId??&&ads.linkId?has_content]
                    [#if ads.linkType=="product"]
                        [#assign _url = "/wap/product/content/"+ads.linkId+"/product.jhtml"]
                    [#elseif ads.linkType=="tenant"]
                        [#assign _url = "/wap/tenant/index/"+ads.linkId+".jhtml"]
                    [/#if]
                [#else ]
                    [#assign _url = ads.url]
                [/#if]
                <a href="${_url}" class="R-1" style="background-image:url(${ads.path})"><img src="${ads.path}"></a>
            [/#list]
        [/#if]
    [/@ad_position]
        <div class="R-2">
        [@ad_position id=129 areaId=area.id count=0]
            [#if adPosition?has_content]
                [#list adPosition.ads as ads]
                    [#assign _url = ""]
                    [#if ads.linkId??&&ads.linkId?has_content]
                        [#if ads.linkType=="product"]
                            [#assign _url = "/wap/product/content/"+ads.linkId+"/product.jhtml"]
                        [#elseif ads.linkType=="tenant"]
                            [#assign _url = "/wap/tenant/index/"+ads.linkId+".jhtml"]
                        [/#if]
                    [#else ]
                        [#assign _url = ads.url]
                    [/#if]
                    <a href="${_url}" class="R-2" style="background-image:url(${ads.path})"><img src="${ads.path}"></a>
                [/#list]
            [/#if]
        [/@ad_position]
        [@ad_position id=130 areaId=area.id count=0]
            [#if adPosition?has_content]
                [#list adPosition.ads as ads]
                    [#assign _url = ""]
                    [#if ads.linkId??&&ads.linkId?has_content]
                        [#if ads.linkType=="product"]
                            [#assign _url = "/wap/product/content/"+ads.linkId+"/product.jhtml"]
                        [#elseif ads.linkType=="tenant"]
                            [#assign _url = "/wap/tenant/index/"+ads.linkId+".jhtml"]
                        [/#if]
                    [#else ]
                        [#assign _url = ads.url]
                    [/#if]
                    <a href="${_url}" class="R-2" style="background-image:url(${ads.path})"><img src="${ads.path}"></a>
                [/#list]
            [/#if]
        [/@ad_position]
        </div>
    </div>
</div>
[#-- 全局导航--]
[#include "/wap/include/nav.ftl"/]
[#-- hbs模板容器--]
<div class="recomtenants IDN">
    <div class="weui_cell bg-05 ft-bs15 clr-grey01 recomtenants-title IDN">
        <div class="weui_cell_hd">
            <i class="iconfont ft-bs1">&#xe663;</i>
        </div>
        <div class="weui_cell_bd">
            推荐商家
        </div>
    </div>
    <!-- 点击加载更多-->
    <a href="javascript:;" class="ft-bs1 bg-14 clr-grey03 loadonclick">点击加载更多</a>
</div>
[#-- 底部技术支持--]
[#include "/wap/include/techsupport/techsupport.ftl"/]
[#-- 高度占位元素--]
<div data-emptyfor="G-nav"></div>
[#-- 是否切换城市对话框--]
<div class="switchtocity">
</div>
[#-- 浮标,8.26狂欢节--][#-- href="${base}/wap/activity/index/${activityid}.jhtml?lat=31.820587&lng=117.227239"--]
[#if activitystate == "begin"]
<a id="activity826" activityid="${activityid}" class="activity a826 anm-out none">
    <img src="${base}/resources/wap/3.0/image/activity_1021.png" width="110" height="110" border="0">
</a>
[#-- 浮标大图,8.26狂欢节--]
<div class="activitylarge">
    <div class="activitylarge-mask trsi anm-fade">
    </div>
    <div class="activitylarge-img">
        <a href="javascript:;" class="closebtn">
            <i class="iconfont ft-bs0 clr-grey00">&#xe68a;</i>
        </a>
        <img src="${base}/resources/wap/3.0/image/activity_1021-large.png" alt="">
    </div>
</div>
[/#if]
<!-- HBS:itemlist-->
<script type="text/x-handlebars-template" id="tpl_itemlist">
    {{#each data}}
    [#include "/wap/include/tenantlist-indexnew.ftl"/]
    {{/each}}
</script>
<!-- BOTTOM SCRIPT-->
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //${base}
    _TH_.area = {};
    _TH_.area.id = "${area.id}";
    _TH_.area.name = "${area.name}";
    //其他数据
    _TH_.datalists = []; //${datalists}
    _TH_.adspic = [];
    _TH_.lat="${area.location.lat}";
    _TH_.lng="${area.location.lng}";
    _TH_.activityid = "${activityid}";
    _TH_.updateCurrent= "${updateCurrent}";
    [@ad_position id=70 areaId=area.id count=0]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
            var _url = "";
                [#if ads.linkId??&&ads.linkId?has_content]
                    [#if ads.linkType=="product"]
                    _url = "${base}/wap/product/content/${ads.linkId}/product.jhtml";
                    [#elseif ads.linkType=="tenant"]
                    _url = "${base}/wap/tenant/index/${ads.linkId}.jhtml";
                    [#elseif ads.linkType=="unionActivity"]
                    _url = "${base}/wap/activity/union/activity/${ads.linkId}.jhtml";
                    [/#if]
                [#else ]
                _url = '${ads.url}';
                [/#if]
            _TH_.adspic.push({content: '<a href="' + _url + '"><img src="${ads.path}"></a>'});
            [/#list]
        [/#if]
    [/@ad_position]

</script>
[#include "/wap/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.js"></script>
<!-- JS:page-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/page/thw/indexnew.js"></script>
<!-- JS:module-->
<!-- <script type="text/javascript" src="${base}/resources/wap/3.0/js/module/activity/activityBuoy.js"></script>-->
</body>
</html>
