<!DOCTYPE html>
<html>

<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${activityPlanning.name}</title>
</head>

<body class="bg_act_1118" onload="load()">
[#--广告图区域banner-1--]
<div id="activity_banner" class="banner ACT1021">
    <div class="img">
    [@ad_position id=141 areaId=area.id count=1/]
        [#--测试用--]
        [#--<img src="${base}/resources/wap/3.0/image/1021/activity_1021_banner.png" width="100%" />--]
    </div>
    <a id="guize" class="clr-grey08" href="javascript:void(0);">活动规则</a>
</div>
<div class="guize ACT1021">
    <div class="guize_item" style="overflow-y: visible;">
        ${activityPlanning.introduction}

        <div class="" style="width: 30%;margin: 0 auto;text-align: center;padding: 8vw 0;padding-bottom: 2.5vw;">
            <img src="${base}/resources/wap/3.0/image/1021/activity_1021_guize_01.png" width="100%"/>
        </div>
        <div style="position: relative;width: 100%;z-index: 2;display: inline-block;">
            [#--<img src="${base}/resources/wap/3.0/image/1021/activity_1021_guize_02.png" width="100%"/>--]
            <div style="width: 82.8125vw;padding-bottom: 3.4375vw;position: absolute;top: 0;left: 4.6875vw;background-color: #fff;z-index: 3;border-radius: 1.5625vw;">
                <p style="font-size: 3.125vw;text-align: left;padding: 4.5625vw;line-height: 1.6;">
                    <i style="display: block;font-style: normal;font-weight: 600;">
                        现金券使用规则 :
                    </i>
                    1.&nbsp;每张现金券实行的是一码一券制，扫码领取后纸质现金劵将立即作废。<br/>
                    2.&nbsp;现金券可在所有商盟商家优惠买单时使用。<br/>
                    3.&nbsp;现金券可抵扣购买商品的部分货款，可与店内其他优惠活动同时使用。<br/>
                    4.&nbsp;现金券可跨店，可跨类目使用。<br/>
                    5.&nbsp;适用于挑货商盟所有商家，余额长期有效。
                </p>
                <span style="text-align: center;font-size: 2.8125vw;line-height: 1;position: absolute;bottom: 2vw;width: 100%;">
                    本活动最终解释权归${setting.siteName}所有
                </span>
                <div class="" style="width: 100%;position: absolute;bottom: -20vw;">
                    <a id="guize_delete" style="display:block;width: 30%;margin: 0 auto;" href="javascript:;" onclick="delet();">
                        <img src="${base}/resources/wap/3.0/image/1021/activity_1021_guize_03.png" width="100%"/>
                    </a>
                </div>
            </div>
        </div>

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
<div class="time ACT1118">
[#--倒计时区域--]
    <div id="activity_timer" class="timer ACT1021 clr-grey08">
        [#--<div class="info">--]
            [#--距 离 活 动 结 束 还 有--]
        [#--</div>--]

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

[#assign activity0=0 activity00=0 activityProductChannel0=0 activity1=0 activity01=0 activityProductChannel1=0]
[#assign activity2=0 activity02=0 activityProductChannel2=0 activity3=0 activity03=0 activityProductChannel3=0]
[#assign activity4=0 activity04=0 activityProductChannel4=0 activity5=0 activity05=0 activityProductChannel5=0]
[#assign activity6=0 activity06=0 activityProductChannel6=0 activity7=0 activity07=0 activityProductChannel7=0]
[#assign activity8=0 activity08=0 activityProductChannel8=0 activity9=0 activity09=0 activityProductChannel9=0]
[#list adPositions?sort_by("id") as adPosition]
    [#if adPosition_index==0]
        [#assign activity0=adPosition.id activity00=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
        [#assign  activityProductChannel0=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==1]
        [#assign activity1=adPosition.id activity01=adPosition.description  ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel1=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==2]
        [#assign activity2=adPosition.id activity02=adPosition.description  ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel2=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==3]
        [#assign activity3=adPosition.id activity03=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel3=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==4]
        [#assign activity4=adPosition.id activity04=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel4=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==5]
        [#assign activity5=adPosition.id activity05=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel5=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==6]
        [#assign activity6=adPosition.id activity06=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel6=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==7]
        [#assign activity7=adPosition.id activity07=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel7=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==8]
        [#assign activity8=adPosition.id activity08=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel8=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==9]
        [#assign activity9=adPosition.id activity09=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel9=adPosition.productChannel.id]
        [/#if]
    [/#if]
[/#list ]

[#--活动菜单栏--]
<div id="activity_nav828" class="nav ACT1021">
[#assign activity0=0 activity00=0 activityProductChannel0=0 activity1=0 activity01=0 activityProductChannel1=0]
[#assign activity2=0 activity02=0 activityProductChannel2=0 activity3=0 activity03=0 activityProductChannel3=0]
[#assign activity4=0 activity04=0 activityProductChannel4=0 activity5=0 activity05=0 activityProductChannel5=0]
[#assign activity6=0 activity06=0 activityProductChannel6=0 activity7=0 activity07=0 activityProductChannel7=0]
[#assign activity8=0 activity08=0 activityProductChannel8=0 activity9=0 activity09=0 activityProductChannel9=0]
[#list adPositions?sort_by("id") as adPosition]
    [#if adPosition_index==0]
        [#assign activity0=adPosition.id activity00=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel0=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==1]
        [#assign activity1=adPosition.id activity01=adPosition.description  ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel1=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==2]
        [#assign activity2=adPosition.id activity02=adPosition.description  ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel2=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==3]
        [#assign activity3=adPosition.id activity03=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel3=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==4]
        [#assign activity4=adPosition.id activity04=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel4=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==5]
        [#assign activity5=adPosition.id activity05=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel5=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==6]
        [#assign activity6=adPosition.id activity06=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel6=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==7]
        [#assign activity7=adPosition.id activity07=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel7=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==8]
        [#assign activity8=adPosition.id activity08=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel8=adPosition.productChannel.id]
        [/#if]
    [#elseif adPosition_index==9]
        [#assign activity9=adPosition.id activity09=adPosition.description ]
        [#if adPosition.productChannel??&&adPosition.productChannel?has_content]
            [#assign  activityProductChannel9=adPosition.productChannel.id]
        [/#if]
    [/#if]
    <a onclick="scroller('act-${adPosition_index}',500);" href="javascript:void(0);">
        <span>${(adPosition.description)!}</span>
    </a>
[/#list ]
    <a onclick="scroller('act-tenant',500);" href="javascript:void(0);"><span>附近商家</span></a>
</div>
[#--活动-内容--]
<div class="content">
[#--活动-1F--]

    <div id="act-0" class="list_item ACT1021">
    [#if activity0!=0&&activity00!=0]
        <div class="top">
            <div class="ACT1021">
                <img src="${base}/resources/wap/3.0/image/1118/activity_title_1118_bg.png" width="100%"/>
                <h2 class="clr-grey08">[#--${activity00}--][#if activity00!=0]${activity00}[/#if]</h2>
            </div>
        </div>
        <div class="items bg-05 act1118 level">
                <ul>
                [#if activity0!=0]
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
                                <li>
                                    <a [#if url??&&url?has_content] href="${base}/${url}"[/#if]>
                                        <div class="img" style="border: 1px solid #B7B4B4;">
                                            <img class="lazy" src="${ads.path}" width="100%"/>
                                        </div>
                                    </a>
                                </li>
                            [/#list]
                        [/#if]
                    [/@adActivity]
                [/#if]
                </ul>
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    [/#if]
    </div>

[#--活动-2F--]

    <div id="act-1" class="list_item ACT1021">
    [#if activity1!=0&&activity01!=0]
        <div class="top">
            <div class="ACT1021">
                <img src="${base}/resources/wap/3.0/image/1118/activity_title_1118_bg.png" width="100%"/>
                <h2 class="clr-grey08">[#--${activity00}--][#if activity01!=0]${activity01}[/#if]</h2>
            </div>
        </div>
        <div class="items ">
                <ul>
                [#if activity1!=0]
                    [@adActivity id = activity1 areaId=area.id count=20]
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
                                <li>
                                    <a [#if url??&&url?has_content] href="${base}/${url}"[/#if]>
                                        <div class="img">
                                            <img class="lazy" src="${ads.path}" width="100%"/>
                                        </div>
                                    </a>
                                </li>
                            [/#list]
                        [/#if]
                    [/@adActivity]
                [/#if]
                </ul>
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    [/#if]
    </div>

[#--banner-2--]
    <div id="image_banner_2" class="img hidden" style="padding: 2vw 0;">
    [@ad_position id=142 areaId=area.id count=1/]
    [#--测试用--]
    [#--<img src="${base}/resources/wap/3.0/image/1021/activity_1021_banner_02.png" width="100%" />--]
        [#--<img class="position_abs" src="${base}/resources/wap/3.0/image/1021/background_material_01.png" width="100%"/>--]
        [#--<img class="position_abs" src="${base}/resources/wap/3.0/image/1021/background_material_02.png" width="100%"/>--]
        [#--<img class="position_abs" src="${base}/resources/wap/3.0/image/1021/background_material_03.png" width="100%"/>--]
    </div>
[#--活动-3F--]
    <div id="act-2" class="list_item ACT1021">
    [#if activity2!=0&&activity02!=0]
        <div class="top">
            <div class="ACT1021">
                <img src="${base}/resources/wap/3.0/image/1118/activity_title_1118_bg.png" width="100%"/>
                <h2 class="clr-grey08">[#--${activity00}--][#if activity02!=0]${activity02}[/#if]</h2>
            </div>
        </div>
        <div class="items act1118 level">
            <ul>
                [#if activity2!=0]
                    [@adActivity id = activity2 areaId=area.id count=20]
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
                                <li>
                                    <a [#if url??&&url?has_content] href="${base}/${url}"[/#if]>
                                        <div class="img">
                                            <img class="lazy" src="${ads.path}" width="100%"/>
                                        </div>
                                    </a>
                                </li>
                            [/#list]
                        [/#if]
                    [/@adActivity]
                [/#if]
            </ul>
        </div>
        <div class="bottom clr-grey08 hidden">
            <span>展开更多</span>
            <i>▼</i>
        </div>
    [/#if]
    </div>

[#--活动-附近--]
    <div id="act-tenant" class="list_item ACT1021">
        <div class="top" style="padding: 4vw 0;">
            <img src="${base}/resources/wap/3.0/image/1118/activity_title_1118_bg.png" width="100%"/>
            <h2 class="clr-grey08">附近商家</h2>
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



[#--右侧悬浮返回顶部--]
<div id="actGotop" class="actGotop ACT1021">
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
        var gotop_1 = document.getElementById("actGotop_1");
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
        };
//        gotop_1.onclick = function () {
////            alert("hello");
//            timer = setInterval(function () {
//                var backtop = document.body.scrollTop;
//                var speedtop = backtop / 5;//向上滚动速度
////                document.body.scrollTop -= 100;
//                document.body.scrollTop = backtop - speedtop;
//                if (backtop == 0) {//到达顶部关闭向上滚动事件
//                    clearInterval(timer);
//                }
//            }, 30);
//        }
    }
</script>
</body>

</html>