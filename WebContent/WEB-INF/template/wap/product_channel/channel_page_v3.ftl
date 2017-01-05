<!DOCTYPE html>
<html>
<head>
[#include "/wap/include/resource-3.0-part1.ftl"]
[#-- 频道名称--]
    <title>${productChannel.name}</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/pluginrevise.css">
</head>
<body class="bg-00">
[#--顶部轮播--]
<div id="channel_carousel" style="position: relative;">
    <img src="${base}/resources/wap/3.0/image/blank/blank640x200.png" alt="" style="max-width: 100%;height: auto;">
</div>
[#-- 频道二级分类--]
<div class="channels PT-CNP">
[#list productChannel.tenantCategorys as tenantCategory]
    <a href="${base}/wap/tenant/list.jhtml?tenantCategoryId=${tenantCategory.id}" class="it ft-bs1">
        <img class="lazy channel_sign" src="${tenantCategory.image}">
        <p class="ft-bs0 clr-grey02">${tenantCategory.name}</p>
    </a>
[/#list]
</div>
[#-- 频道中心广告位--]
<div class="sg_ad bg-05 PT-CNP">
[@adProductChannel id=144 areaId=areaId productChannelId=productChannel.id count=0]
    [#if adProductChannels?has_content]
        [#list adProductChannels as ads]
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
        <a href="${_url}" style="display:block;"><img alt="" src="${ads.path}"></a>
        [/#list]
    [/#if]
[/@adProductChannel]
</div>
[#-- 为您推荐占位--]
<div class="recomtenant_foryou">
</div>
[#-- 底部技术支持--]
[#include "/wap/include/techsupport/techsupport.ftl"/]
<!-- HBS:tenantlist-->
<script type="text/x-handlebars-template" id="tpl_tenantlist">
    {{#each data}}
    [#include "/wap/include/tenantlist-indexnew.ftl"/]
    {{/each}}
</script>
<!-- BOTTOM SCRIPT-->
[#include "/wap/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.js"></script>
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //${base}
    //${area}
    //其他数据
    _TH_.datalists = []; //${datalists}
    _TH_.adspic = [];
    //区域id
    _TH_.areaId = "${areaId}";
    [@adProductChannel id=143 areaId=areaId productChannelId=productChannel.id count=0]
        [#if adProductChannels?has_content]
            [#list adProductChannels as adProductChannel]
            var _url = "";
                [#if adProductChannel.linkId??&&adProductChannel.linkId?has_content]
                    [#if adProductChannel.linkType=="product"]
                    _url = "${base}/wap/product/content/${adProductChannel.linkId}/product.jhtml";
                    [#elseif adProductChannel.linkType=="tenant"]
                    _url = "${base}/wap/tenant/index/${adProductChannel.linkId}.jhtml";
                    [/#if]
                [#else ]
                _url = '${adProductChannel.url}';
                [/#if]
            _TH_.adspic.push({content: '<a href="' + _url + '"><img src="${adProductChannel.path}"></a>'});
            [/#list]
        [/#if]
    [/@adProductChannel]
    if (_TH_.adspic.length === 0) {
        for (var k = 1; k <= 3; k++) {
            _TH_.adspic.push({content: '<a href="javascript:;" style="display:block;"><img alt="" src="${base}/resources/wap/3.0/image/channel/${productChannel.id}/carousel/' + k + '.png"></a>'});
        }
    }
</script>
<!-- JS:page-->
<script type="text/javascript">
    $(function () {
        //ajax before检测
        $(document).on('ajaxBeforeSend', function (e, xhr, options) {
            if (options.showloading) {
                $(".ONCE").tip("addAffair", xhr);
            }
        });
        var s = new iSlider(document.getElementById('channel_carousel'), _TH_.adspic, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 800,
            animateType: 'rotate'
        });
        //data brush
        var compilerTenantlist = Handlebars.compile($("#tpl_tenantlist").html());
        //lazy js,lazyload
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function () {
            $(".lazy").picLazyLoad({
                threshold: 100
            });
        });
        //获取"为您推荐数据" 暂不调用微信接口
        $.ajax({
                    url: _TH_.base + '/app/b2c/tenant/list.jhtml',
                    data: {
                        orderType: 'weight',
                        pageNumber: 1,
                        areaId: _TH_.areaId,
                        //lat: lat,
                        //lng: lng,
                        tagIds: 6,
                        channelId: ${productChannel.id}
                    }
                })
                .done(function (data) {
                    $(".recomtenant_foryou").html(compilerTenantlist(data));
                    console.log(data.data);
                    //数字星级化
                    $(".starlevels").starLevelize();
                    if ($.type($.fn.picLazyLoad) == "function") {
                        $(".lazy").picLazyLoad({
                            threshold: 100
                        });
                    } else {
                        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function () {
                            $(".lazy").picLazyLoad({
                                threshold: 100
                            });
                        });
                    }
                })
                .fail(function () {
                    $(".ONCET").tip("addTask", {
                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                        txt: "网络异常"
                    });
                })
                .always(function () {
                    //console.log("complete");
                });
        //unload event
        $(window).on('unload', function (event) {
            //页面卸载之前清除userlocation
            store.session.remove('userlocation');
        });
    });
</script>
</body>
</html>
