<!DOCTYPE html>
<html>
<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>活动</title>
</head>
<body onload="load()">
[#--广告图区域banner--]
<div id="act_banner">
    <div class="img">
    [@adActivity id = 166 areaId=area.id  linkId=activityPlanning.id count=1]
        [#if adPosition?has_content]
            [#list adPosition as ads]
                [#assign url='javascript:;' ]
                [#--[#if ads.linkId??&&ads.linkId?has_content]--]
                    [#--[#if ads.linkType=="product"]--]
                        [#--[#assign url='${base}/wap/product/content/${ads.linkId}/product.jhtml' ]--]
                    [#--[#elseif ads.linkType=="tenant"]--]
                        [#--[#assign url='${base}/wap/tenant/index/${ads.linkId}.jhtml' ]--]
                    [#--[#elseif ads.linkType=="unionActivity"]--]
                        [#--[#assign url='${base}/wap/activity/union/activity/${ads.linkId}.jhtml' ]--]
                    [#--[/#if]--]
                [#--[/#if]--]
                <div class="item">
                    <a href="${url}">
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

[#--活动菜单栏--]
[#--<div id="act_nav" class="union_act_nav">--]
    [#--<a onclick="scroller('act-hot',500);" class="ft-bs2" href="javascript:void(0);"><span>热门商品</span></a>--]
    [#--<a onclick="scroller('act-tenant',500);" class="ft-bs2" href="javascript:void(0);"><span>推荐商家</span></a>--]
[#--</div>--]
[#if num == 5]
<div id="act_nav_rest" class="act_nav">
    <ul>
    [#list activityPlanning.singleProductPositions as singleProductPosition]
        [#if singleProductPosition_index > 0 ]
            <li>
                <a class="ft-bs2" onclick="scroller('act-${singleProductPosition_index}',500);" href="javascript:void(0);">
                ${(singleProductPosition.name)!}
                </a>
            </li>
        [/#if]
    [/#list ]
    </ul>
</div>
[/#if]
[#if num == 5]
<div id="act_nav" class="act_nav">
    <ul>
    [#list activityPlanning.singleProductPositions as singleProductPosition]
        [#if singleProductPosition_index > 0 ]
        <li>
            <a class="ft-bs2" onclick="scroller('act-${singleProductPosition_index}',500);" href="javascript:void(0);">
            ${(singleProductPosition.name)!}
            </a>
        </li>
        [/#if]
    [/#list ]
    </ul>
</div>
[/#if]
[#--热门商品--]
<div id="act-hot">
[#--NO1（联盟活动）--]
[#--[#list activityPlanning.singleProductPositions as singleProductPosition]--]
    [#--<div class="union_act_bd weui_cells">--]
        [#--<div class="title ft-bs2">· ${(singleProductPosition.name)!} ·</div>--]
        [#--[#list singleProductPosition.singleProducts as singleProduct]--]
            [#--<div class="weui_cell">--]
                [#--<div class="weui_cell_hd">--]
                    [#--<img class="lazy" src="${singleProduct.product.thumbnail}">--]
                [#--</div>--]
                [#--<div class="weui_cell_bd">--]
                    [#--<h2 class="ft-bs3">${singleProduct.product.name}</h2>--]
                    [#--<p class="info ft-bs2">【${singleProduct.title}】</p>--]
                    [#--<div class="price">--]
                        [#--<em class="ft-bs1">￥</em>--]
                        [#--<span class="ft-bs3">${singleProduct.product.price}</span>--]
                        [#--<span class="label ft-bs2">精选</span>--]
                    [#--</div>--]
                    [#--<p class="like ft-bs1">${singleProduct.product.hits}人喜欢</p>--]
                [#--</div>--]
                [#--<a class="weui_cell_ft ft-bs2"--]
                   [#--href="${base}/wap/product/content/${singleProduct.product.id}/product.jhtml">--]
                    [#--<span>立即购买</span>--]
                [#--</a>--]
            [#--</div>--]
        [#--[/#list]--]
    [#--</div>--]
[#--[/#list]--]
[#--NO2（年货价活动）--]
[#list activityPlanning.singleProductPositions as singleProductPosition]
    <div id="act-${(singleProductPosition_index)!}" class="newYear_act_bd weui_cells">
        <div class="title ft-bs2 clr_red"><span>${(singleProductPosition.name)!}</span></div>
        [#if singleProductPosition_index=0]
        <div id="newYear_act_pic">
            <ul>
                [#list singleProductPosition.singleProducts as singleProduct]
                <li>
                    <a href="${base}/wap/product/content/${singleProduct.product.id}/product.jhtml">
                        <div class="img">
                            <img class="lazy" src="${singleProduct.product.thumbnail}">
                        </div>
                        <p class="ft-bs2">${singleProduct.product.name}</p>
                        <p class="ft-bs2 price">
                            <span class="clr_red">￥${singleProduct.product.calcEffectivePrice(null)}</span>
                            [#if singleProduct.product.marketPrice gt singleProduct.product.calcEffectivePrice(null)]
                                <span class="ft-bs1">￥${singleProduct.product.marketPrice}</span>
                            [/#if]
                        </p>
                    </a>
                </li>
                [/#list]
            </ul>
        </div>
        [#else ]
            [#list singleProductPosition.singleProducts as singleProduct]
            <div class="item">
                <a class="weui_cell" href="${base}/wap/product/content/${singleProduct.product.id}/product.jhtml">
                    <div class="weui_cell_hd">
                        <img class="lazy" src="${singleProduct.product.thumbnail}">
                    </div>
                    <div class="weui_cell_bd">
                        <h2 class="ft-bs3">
                            <span class="clr_mauve ft-bs2">[包邮]</span>
                        ${singleProduct.product.name}
                        </h2>
                        <p class="info ft-bs2">${singleProduct.title}</p>
                        <div class="like ft-bs2">
                            <p>
                            <span class="ft-bs1">
                                ${singleProduct.product.hits}人喜欢
                            </span>
                            </p>
                            <p>
                                <i class="iconfont icon-s-heart-01 ft-bs2 clr_red"></i>
                            </p>
                        </div>
                        <div class="priceT clr_gray">
                            <span class="ft-bs1">
                                [#if singleProduct.product.marketPrice gt singleProduct.product.calcEffectivePrice(null)]
                                    ￥${singleProduct.product.marketPrice}
                                [/#if]
                            </span>
                            <span class="ft-bs1"><em class="clr_red">${singleProduct.product.monthSales}</em>件已售</span>
                        </div>
                        <div class="price clr_red">
                            <em class="ft-bs3">￥</em>
                            <span class="ft-bs7">${singleProduct.product.calcEffectivePrice(null)}</span>
                        </div>
                    </div>

                </a>
                <a class="weui_cell_ft ft-bs2" href="${base}/wap/product/content/${singleProduct.product.id}/product.jhtml">
                    <span>马上抢</span>
                </a>
            </div>
            [/#list]
        [/#if]
    </div>
[/#list]
</div>
[#--推荐商家--]
<div id="tenant_title">
    <div class="title ft-bs2 clr_red"><span>推荐商家</span></div>
</div>
<div id="act-tenant">


</div>
[#--底部返回顶部--]
<div id="act_goTop_ft" class="act_goTop_ft ft-bs3">
    <span>返回顶部</span>
</div>
[#--右侧悬浮返回顶部--]
<div id="act_goTop" class="act_goTop">
    <img class="lazy" src="${base}/resources/wap/3.0/image/act_Top_002.png" width="100%"/>
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
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/activity/scroller_tag.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/activity/union_activity.js"></script>
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
    _TH_.base = "${base}"; [#--//${base}--]
    /**
     *懒加载
     */
    $(".lazy").picLazyLoad({
        threshold: 100,
        placeholder: _TH_.base + '/resources/wap/2.0/images/AccountBitmap-product.png'
    });


    $(function () {
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
                                $('#act-tenant').html(compilerItemlist(data));
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

</script>
<script type="text/javascript">
    //返回顶部
    window.onload = function () {
        var gotop = document.getElementById("act_goTop");
        var gotop_ft = document.getElementById("act_goTop_ft");

        var timer = null;
        var pagelookheight = document.getElementById("act_banner").scrollHeight + document.getElementById("act-0").scrollHeight - 54;//获取当前可见高度
        window.onscroll = function () {
            var backtop = document.body.scrollTop;
            if (backtop >= pagelookheight) {//当滚动高度超过当前可见高度则显示否则隐藏
                gotop.style.display = "block";
            } else {
                gotop.style.display = "none";
            }
        };
        function backTop() {
            timer = setInterval(function () {
                var backtop = document.body.scrollTop;
                var speedtop = backtop / 5;//向上滚动速度
                //document.body.scrollTop -= 100;
                document.body.scrollTop = backtop - speedtop;
                if (backtop == 0) {//到达顶部关闭向上滚动事件
                    clearInterval(timer);
                }
            }, 30);
        };
        gotop.onclick = function () {
            backTop();
        };
        gotop_ft.onclick = function () {
            backTop();
        };
    }
</script>
</body>

</html>