<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>联系商家</title>
    <style type="text/css" media="screen">
        body {
            background-color: #eaeaea;
        }

        .weui_actionsheet_menu > .weui_cells {
            margin-top: 0;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<!-- mapwrap-->
<!-- 
<div id="mapwrap" class="mapwrap TTC">
    
</div>-->
<script type="text/html" id="tpl_wraper">
    <!-- box_store-->
    <div class="weui_cells box_store TTC">
        <div class="weui_cells_title weui_cell storeinfo TTC">
            <div class="weui_cell_hd storeinfo-hd TTC">
                <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"
                     data-original="${thumbnail}" alt="">
            </div>
            <div class="weui_cell_bd weui_cell_primary storeinfo-bd TTC">
                <span>${areaName}</span>
                <p>${tenantName}</p>
                <span>${address}</span>
            </div>
        </div>
        <div class="weui_cells">
        [#if lat!=""||lng!=""]
            <a href="javascript:;" class="weui_cell box_contactinfo TTC openmap">
                <div class="weui_cell_hd box-hd">
                    <i class="icon iconfont">&#xe688;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary box-bd">
                    <span>到这里去</span>
                </div>
            </a>
        [/#if]
            <!-- 微信联系 暂时搁置-->
            <!--
            <a href="javascript:;" class="weui_cell box_contactinfo callweixin TTC">
                <div class="weui_cell_hd box-hd">
                    <i class="icon iconfont">&#xe65b;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary box-bd">
                    <span>微信联系</span>
                </div>
            </a>-->
            <a href="javascript:;" class="weui_cell box_contactinfo callphone TTC">
                <div class="weui_cell_hd box-hd">
                    <i class="icon iconfont">&#xe625;</i>
                </div>
                <div class="weui_cell_bd weui_cell_primary box-bd">
                    <span>我要咨询</span>
                </div>
            </a>
        </div>
    </div>
    [#--include "/wap/include/footer.ftl"/--]
</script>
<script src="http://api.map.baidu.com/api?v=2.0&ak=11gjCy4d5FwxG72gv7V1XRalDak4ablq" type="text/javascript"></script>
<script type="text/javascript"
        src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
        });
        //actionsheet配置

        var _url = "${member}"==""?"${base}/wap/bound/indexNew.jhtml?redirectUrl=${base}/wap/member/chat/index.jhtml?targetid=TELEPHONE":"${base}/wap/member/chat/index.jhtml?targetid=TELEPHONE";

        var actionsheet_config = {
            title: '联系列表',
            //default: 'weixinPayPlugin',
            //fn: fnselect,
            clastr: {
                fortitle: "weui_cell tellists-title TTC",
                forbody: "tellists-ctn TTC"
            },
            showmask: true,
            removecancel: true,
            data: ${employees},
            url:_url
        };
        //电话联系
        $("a.callphone").on('click', function (event) {
            event.preventDefault();
            showActionSheet2(actionsheet_config);
        });
        //点击调取微信自带在线地图
        $("a.openmap").on('click', function (event) {
            event.preventDefault();
            //获取距离
            ajaxGet({
                url: "${base}/wap/mutual/get_config.jhtml",
                data: {
                    url: location.href.split('#')[0]
                },
                success: function (message) {
                    if (message.type == "success") {
                        var data = JSON.parse(message.content);
                        //console.log("the data is");
                        //console.log(data);
                    [#if lat!=""||lng!=""]
                        wx.config({
                            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                            appId: data.appId, // 必填，公众号的唯一标识
                            timestamp: data.timestamp, // 必填，生成签名的时间戳
                            nonceStr: data.nonceStr, // 必填，生成签名的随机串
                            signature: data.signature,// 必填，签名，见附录1
                            jsApiList: ["openLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                        });
                        wx.ready(function () {
                            /* 
                            $("<div id='printout'>你好</div>").css({
                                position:"fixed",
                                left:"50%",
                                top:"50%",
                                zIndex:"1000"
                            }).appendTo('body');*/
                            wx.openLocation({
                                latitude: ${lat}, // 纬度，浮点数，范围为90 ~ -90
                                longitude: ${lng}, // 经度，浮点数，范围为180 ~ -180。
                                name: '${tenantName}', // 位置名
                                address: '${address}', // 地址详情说明
                                scale: 20, // 地图缩放级别,整形值,范围从1~28。默认为最大
                                infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
                            });
                        });
                        wx.error(function (res) {
                            //config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                        });
                        wx.checkJsApi({
                            jsApiList: ['openLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                            success: function (res) {
                                // 以键值对的形式返回，可用的api值true，不可用为false
                                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                                /* 
                                if(res.checkResult){
                                    $("#printout").text(res.checkResult.getLocation);
                                }*/
                            }
                        });
                    [/#if]
                    } else {
                        showDialog2("提示", message.content);
                    }
                }
            });
        });

        //var compiler = Handlebars.compile($("#wap-list-item").html());
        //$("#am_g").html(compiler(data));
        //console.log(data.length);
        //百度在线地图暂时不需要
        /* 
        //虚拟坐标数据(店铺坐标)
        var locationpoint_store={
            lat:31.8319,
            lng:117.23292
        };
        //创建地图实例  
        var map = new BMap.Map("mapwrap");
        //创建点坐标  
        var mappointO = new BMap.Point(locationpoint_store.lng,locationpoint_store.lat);
        //初始化地图，设置中心点坐标和地图级别
        map.centerAndZoom(mappointO, 15);
        map.enableScrollWheelZoom();
        map.addControl(new BMap.NavigationControl({
            type: BMAP_NAVIGATION_CONTROL_LARGE
        }));
        var infocontent = '<p style="width:280px;margin:0;lineheight:20px;">地址： 望江西路与潜山路交叉路大唐国际购物广场一楼<br>电话：0551-12345678</p>' ;
        var searchInfoWindow = new BMapLib.SearchInfoWindow(map,"信息框内容",{
            title: "沃特体育蜀山店", //标题
            panel : "panel", //检索结果面板
            enableAutoPan : true, //自动平移
            enableSendToPhone: false, //是否显示发送到手机按钮
            searchTypes:[
                BMAPLIB_TAB_TO_HERE,
                BMAPLIB_TAB_FROM_HERE
            ]
        });
        //map.addControl(new BMap.ScaleControl());
        //map.addControl(new BMap.OverviewMapControl());
        //map.addControl(new BMap.MapTypeControl());
        //仅当设置城市信息时，MapTypeControl的切换功能才能可用
        map.setCurrentCity("合肥");
        var marker = new BMap.Marker(mappointO); // 创建标注    
        marker.addEventListener("click",function(e){
            searchInfoWindow.open(marker);
        });
        map.addOverlay(marker); // 将标注添加到地图中*/

        //soga
        //var point2 = new BMap.Point(121.452224, 31.229868);
        //var point3 = new BMap.Point(121.448594, 31.229173);

        //var soga_distance = map.getDistance(point2, point3);
        //console.log(soga_distance);
    });
</script>
</body>
</html>