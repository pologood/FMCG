<!DOCTYPE html>
<html>

<head>
    [#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>联盟商家</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/pluginrevise.css">
</head>

<body class="bg-00">
    <div class="itemlist_rap">
        
    </div>
    <!-- HBS:itemlist-->
    <script type="text/x-handlebars-template" id="tpl_itemlist">
            {{#each data}}
            <div class="box1 TT-UN">
                <a class="weui_cell box1-s1 TT-UN" href="${base}/wap/tenant/index/{{this.tenantid}}.jhtml">
                    <div class="weui_cell_hd">
                        <img class="lazyimg" src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"  data-original="{{this.thumbnail}}" alt="">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary">
                        <div class="box1-s1-sec1">
                            <b class="ft-bs3">{{this.name}}</b>
                            <span data-href="tel:{{this.tel}}" class="iconfont clr-red05 box1-tenanttel TT-UN">&#xe687;</span>
                        </div>
                        <div class="box1-s1-sec2 clr-grey03 ft-bs2">
                            <span>{{this.address}}</span>
                            <span href="javascript:;">
                                <i class="iconfont clr-orange03 none">&#xe623;</i>{{distance}}
                            </span>
                        </div>
                        <div class="box1-s1-sec3">
                            <span>营业时间：{{this.busitime}}</span>    
                        </div>
                    </div>
                </a>
                <p class="box1-s2 TT-UN ft-bs2">
                    <i class="blank"></i>
                    <span class="clr-red05"><i class="iconfont">&#xe66d;</i>&nbsp;七天退货</span>
                    <span class="clr-red05"><i class="iconfont">&#xe66e;</i>&nbsp;担保交易</span>
                </p>
            </div>
            {{/each}}
    </script>
    <!-- BOTTOM SCRIPT-->
    [#include "/wap/include/resource-3.0-part2.ftl"/]
    <!-- JS:plugin-->
    <!-- JS:receiver-->
    <script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //${base}
    //其他数据

    _TH_.datalists = [];

    [#list page.content as tenantRelation]
    _TH_.datalists.push({
        tenantid:"${tenantRelation.parent.id}",
        thumbnail:"${tenantRelation.parent.thumbnail}",
        name:"${tenantRelation.parent.name}",
        address:"${tenantRelation.parent.address}",
        busitime:"9:00-18:00",
        tel:"${tenantRelation.parent.telephone}"
        //distance:2.1
    });
    [/#list]
            [#--[{--]
        [#--tenantid:"405",--]
        [#--tenantavatar: "http://placehold.it/160x160/333/fff.png?text=Avatar",--]
        [#--name: "安徽七匹狼运营中心1",--]
        [#--address:"安徽省合肥市瑶海区中绿广场17楼1708室",--]
        [#--busitime:"9:00-18:00",--]
        [#--tel:"13133664488",--]
        [#--distance:2.1--]
    [#--}, {--]
        [#--tenantid:"406",--]
        [#--tenantavatar: "http://placehold.it/160x160/333/fff.png?text=Avatar",--]
        [#--name: "安徽七匹狼运营中心2",--]
        [#--address:"安徽省合肥市瑶海区中绿广场17楼1708室",--]
        [#--busitime:"9:00-18:00",--]
        [#--tel:"13133664488",--]
        [#--distance:2.1--]
    [#--}, {--]
        [#--tenantid:"407",--]
        [#--tenantavatar: "http://placehold.it/160x160/333/fff.png?text=Avatar",--]
        [#--name: "安徽七匹狼运营中心3",--]
        [#--address:"安徽省合肥市瑶海区中绿广场17楼1708室",--]
        [#--busitime:"9:00-18:00",--]
        [#--tel:"13133664488",--]
        [#--distance:2.1--]
    [#--}]; //${datalists}--]
    // _TH_.other="${tenant}";
    </script>
    <!-- JS:page-->
    <!-- <script type="text/javascript" src="${base}/resources/wap/3.0/js/page/thw/tenant/union.js"></script>-->    
    <!-- JS:other code here-->
    <script type="text/javascript">
        $(function() {
            var compiler = Handlebars.compile($("#tpl_itemlist").html());
            $(".itemlist_rap").html(compiler({ data: _TH_.datalists }));
            $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function() {
                $(".lazyimg").picLazyLoad({
                    threshold: 100,
                    placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                });
            });
            $(".itemlist_rap").on('click', '.box1-tenanttel.TT-UN', function(event) {
                event.preventDefault();
                location.href = $(this).data('href');
            });
        });
    </script>
</body>

</html>
