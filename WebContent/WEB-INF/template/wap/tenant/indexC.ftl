<!DOCTYPE html>
<html>

<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${setting.siteName}</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/pluginrevise.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css">
</head>

<body class="bg-00">
[#-- 商家信息展示--]
<div class="infoR TT-IC">
    [#-- 头像 背景--]
    <div class="infoR-avatar TT-IC">
        <div class="operas">
            <i class="iconfont ft-bs2 saveornot"></i>
        </div>
        <a href="javascript:;" class="front">
            <img src="${base}/resources/wap/3.0/image/mock/city01.png" alt="">
        </a>
        <div class="back" style="background-image: url(${base}/resources/wap/3.0/image/mock/city01.png)"></div>
    </div>
    [#-- 商家名称 联系方式--]
    <div class="infoR-descs">
        <div class="sec1">
            <div>
                <h3 class="ft-bs3 tenantname">${tenant.name}</h3>
                <div class="starlevelsR">
                    <div class="cf ft-bs0 starlevels" data-starnum="${tenant.score}" data-starlevelized="false">
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                        <i></i>
                    </div>
                    <span class="ft-bs0 clr-red05">${tenant.score}分</span>
                </div>
            </div>
        </div>
        <div class="sec2">
            <p class="ft-bs0">该评分来自行业综合评估</p>
            <a href="tel:${tenant.telephone}">
                <i class="iconfont clr-red05 ft-bs3">&#xe687;</i>
            </a>
        </div>
    </div>
    [#-- 商家地址--]
    <div class="weui_cell bg-15 infoR-address">
        <div class="weui_cell_hd">
            <i class="iconfont">&#xe623;</i>
        </div>
        <div class="weui_cell_bd ft-bs2 clr-grey02 weui_cell_primary">
            ${tenant.address}
        </div>
        <div class="weui_cell_ft ft-bs1">
            [#--0.34km--]
        </div>
    </div>
</div>
[#-- hbs模板容器--]
<div class="recomtenants IDN">
    <div class="weui_cell bg-05 ft-bs2 recomtenants-title IDN">
        <div class="weui_cell_hd">
            <i class="iconfont">&#xe663;</i>
        </div>
        <div class="weui_cell_bd">
            推荐商家
        </div>
    </div>
    <!-- 点击加载更多-->
    <a href="javascript:;" class="ft-bs1 bg-14 clr-grey03 loadonclick">点击加载更多</a>
</div>

<a href="${base}/wap/pay/bill/buyreduce/${tenant.id}.jhtml" class="link_toBRRN">
    <img src="${base}/resources/wap/3.0/image/tenant/link_toBRRN.png" alt="">
</a>

<!-- HBS:itemlist-->
<script type="text/x-handlebars-template" id="tpl_itemlist">
    {{#each data}}
    [#include "/wap/include/tenantlist-indexnew.ftl"/]
    {{/each}}
</script>
<!-- BOTTOM SCRIPT-->
[#include "/wap/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<!-- <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>-->
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
    _TH_.area={};
    //其他数据
    _TH_.datalists = []; //${datalists}
    //_TH_.adspic = [];
</script>
<!-- JS:page-->
<!-- <script type="text/javascript" src="${base}/resources/wap/3.0/js/page/thw/indexnew.js"></script>-->
<!-- <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>-->
<!-- JS:other code here-->
<script type="text/javascript">
    $(function() {
        //ajax before检测
        $(document).on('ajaxBeforeSend', function(e, xhr, options) {
            if (options.showloading) {
                $(".ONCE").tip("addAffair", xhr);
            }
        });
        //data brush
        var compilerItemlist = Handlebars.compile($("#tpl_itemlist").html());
        //lazy js,lazyload
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function() {
            $(".lazy").picLazyLoad({
                threshold: 100,
                placeholder: _TH_.base + '/resources/wap/2.0/images/AccountBitmap-product.png'
            });
        });
        //数字星级化
        //starLevelize(".starlevels");
        $(".starlevels").starLevelize();
        //lazy js,get the weixin jssdk
        //初始化微信接口
        _wxSDK.initInterface($script,{
            afterOnMenuShare : function() {
                setTimeout(function() {
                    $(".recomtenants .loadonclick").trigger('click');
                }, 0);
            }
        });
        //点击加载更多
        $(".recomtenants").on('click', '.loadonclick', function(event) {
            event.preventDefault();
            var this_instance = this;
            var pagenow = 0;
            //var $loadmorefor = $(this_instance).parent();
            if ($(this_instance).data("pagenow")) {
                //$(this_instance).data("pagenow",1);
                pagenow = parseInt($(this_instance).data("pagenow"), 10);
            }
            //console.log("you click me");
            //要取的数据集页码数
            pagenow++;
            //获取到地理位置
            //ajax获取第一页的数据，填充到指定元素
            _wxSDK.getLocation()
                .done(function(lat, lng) {
                    //console.log("当前的经纬度是:" + lat);
                    $.ajax({
                            url: _TH_.base + "/app/b2c/tenant/list.jhtml",
                            data: {
                                tenantCategoryId: '',
                                communityId: '',
                                orderType: 'weight',
                                pageNumber: pagenow,
                                areaId: _TH_.area.id,
                                lat: lat,
                                lng: lng
                            },
                            showloading: true
                        })
                        .done(function(datas) {
                            console.log("success");
                            console.log(datas);
                            if (datas.message.type == "success") {
                                //获得了新数据
                                if (datas.data.length) {
                                    //填充指定元素
                                    //compilerItemlist
                                    $(this_instance).before(compilerItemlist(datas));
                                    $(".lazy").picLazyLoad({
                                        threshold: 100,
                                        placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                                    });
                                    //数据刷完了重置写入当前页码
                                    $(this_instance).data("pagenow", pagenow);
                                } else {
                                    //如果拿不到数据,不填充,页码不累加
                                    $(this_instance).text("已是最后一页");
                                }
                            }
                            console.log("当前页码是:" + pagenow);
                        })
                        .fail(function() {
                            console.log("error");
                        });
                })
                .fail(function() {
                    $(".ONCET").tip("addTask", {
                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                        txt: "获取地址位置失败"
                    });
                });
            //调取成功,pagenow+1，保存到元素 data中
        });
        //收藏店铺按钮
        $(".infoR-avatar").on('click', '.saveornot', function(event) {
            event.preventDefault();
            $(this).toggleClass('active');
        });
    });

</script>
</body>

</html>
