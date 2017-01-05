<!DOCTYPE html>
<html>
<head>
[#include "/app/include/resource-3.0-part1.ftl"/]
    <title>${setting.siteName}</title>
    <link rel="stylesheet" href="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/app/3.0/css/pluginrevise.css">
</head>
<body class="bg-00">
<div id="carousel" class="carousel IDN" style="position: relative;">
    <img src="${base}/resources/app/3.0/image/blank/blank640x330.png" alt="">
</div>
[#--频道--]
<div class="channels IDN">
[#list productChannels as channel]
    <a href="rzico://channel?id=${channel.id}" class="it ft-bs1">
        <div class="channel_sign_rap">
            <img class="lazy channel_sign anm-trsi anm-init"
                 src="${base}/resources/app/3.0/image/AccountBitmap-product.png"
                 data-original="${channel.image}">
        </div>
        <p class="ft-bs0 clr-grey02">${channel.name}</p>
    </a>
[/#list]
</div>
[#--广告位--]
<div class="adsboard IDN">
[@ad_position id=127 areaId=area.id count=0]
    [#if adPosition?has_content]
        [#list adPosition.ads as ads]
            [#assign _url = ""]
            [#if ads.linkId??&&ads.linkId?has_content]
                [#if ads.linkType=="product"]
                    [#assign _url = "rzico://product?id=${ads.linkId}"]
                [#elseif ads.linkType=="tenant"]
                    [#assign _url =  "rzico://tenant?id=${ads.linkId}"]
                [/#if]
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
                        [#assign _url = "rzico://product?id=${ads.linkId}"]
                    [#elseif ads.linkType=="tenant"]
                        [#assign _url = "rzico://tenant?id=${ads.linkId}"]
                    [/#if]
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
                            [#assign _url = "rzico://product?id=${ads.linkId}"]
                        [#elseif ads.linkType=="tenant"]
                            [#assign _url = "rzico://tenant?id=${ads.linkId}"]
                        [/#if]
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
                            [#assign _url = "rzico://product?id=${ads.linkId}"]
                        [#elseif ads.linkType=="tenant"]
                            [#assign _url = "rzico://tenant?id=${ads.linkId}"]
                        [/#if]
                    [/#if]
                    <a href="${_url}" class="R-2" style="background-image:url(${ads.path})"><img src="${ads.path}"></a>
                [/#list]
            [/#if]
        [/@ad_position]
        </div>
    </div>
</div>
[#-- 全局导航--]
[#--[#include "/wap/include/nav.ftl"/]--]
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

[#-- 高度占位元素--]
<div data-emptyfor="G-nav"></div>
[#-- 是否切换城市对话框--]
<div class="switchtocity">
</div>
[#-- 浮标,8.26狂欢节--]
[#if activitystate == "begin"]
<a id="activity826" activityid="${activityid}" class="activity a826 anm-out none"
   href="rzico://activity?id=${activityid}">
    <img src="${base}/resources/app/3.0/image/activity_826.png" width="110" height="110" border="0">
</a>
[#-- 浮标大图,8.26狂欢节--]
<div class="activitylarge">
    <div class="activitylarge-mask trsi anm-fade">
    </div>
    <div class="activitylarge-img">
        <a href="javascript:;" class="closebtn">
            <i class="iconfont ft-bs0 clr-grey00">&#xe68a;</i>
        </a>
        <img src="${base}/resources/app/3.0/image/activity_826-large.png" alt="">
    </div>
</div>
[/#if]
<!-- HBS:itemlist-->
<script type="text/x-handlebars-template" id="tpl_itemlist">
    {{#each data}}
    [#include "/app/include/tenantlist-indexnew.ftl"/]
    {{/each}}
</script>
<!-- BOTTOM SCRIPT-->
[#include "/app/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.js"></script>
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
    //${area}
    //其他数据

    _TH_.lat =${lat};
    _TH_.lng =${lng};
    _TH_.datalists = []; //${datalists}
    _TH_.adspic = [];
    _TH_.activityid = "${activityid}";
    [@ad_position id=159 areaId=area.id count=5]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
            var _url = "";
                [#if ads.linkId??&&ads.linkId?has_content]
                    [#if ads.linkType=="product"]
                    _url = "rzico://product?id=${ads.linkId}";
                    [#elseif ads.linkType=="tenant"]
                    _url = "rzico://tenant?id=${ads.linkId}";
                    [/#if]
                [/#if]
                    _TH_.adspic.push({content: '<a href="' + _url + '"><img src="${ads.path}"></a>'});
            [/#list]
        [/#if]
    [/@ad_position]
</script>
<!-- JS:page-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/page/thw/indexnew.js"></script>
</body>
</html>