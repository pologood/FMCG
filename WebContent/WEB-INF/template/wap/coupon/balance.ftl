<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>平台券余额</title>

    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/coupon.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/main.css">
    <link rel="stylesheet" href="${base}/resources/wap/2.0/fonts/iconfont.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
</head>
<body>

<div class="arrivalRedBag">
    <div class="success">
        <p class="money">￥${amount}</p>
        <p class="balance"><a href="${base}/wap/coupon/record.jhtml">明细查询</a></p>
    </div>
    <div class="tit">推荐商家</div>
</div>

<div class="weui-cells recommendTenant" id="wrapper">
    <div class="recommend_list" id="scroller">
    </div>
</div>

<script src="${base}/resources/wap/2.0/js/zepto1.1.6-release-olive.mangle.min.js"></script>
<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/iscroll/iscroll.js"></script>

<!-- BOTTOM SCRIPT-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/vendor.js"></script>
<!-- JS:lib-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/lib.js"></script>
<!-- JS:component-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/whostrap.js"></script>

<script type="text/x-handlebars-template" id="wap-list-item">
    {{#each data}}
    <div class="weui-cell weui-cell_access">
        <div class="weui-cell__hd"><img src="{{thumbnail}}"/></div>
        <div class="weui-cell__bd">
            <p class="shop_name">{{shortName}}</p>
            <p>主营：{{tenantCategoryName}}</p>
            <p><span class="tenant_address">{{address}}</span><span class="distance">{{#formatdistance distance}}{{/formatdistance}}</span><i class="iconfont icon-go orange"></i></p>
            {{#if promotions}}
            <p>
                {{#each promotions}}
                {{#if type}}
                <i class="iconfont promtag pt-{{type}}"></i>
                {{/if}}
                {{/each}}
            </p>
            {{/if}}
            <a href="${base}/wap/pay/bill/buyreduce/{{id}}.jhtml" class="orange use">立即使用</a>
        </div>
    </div>
    {{/each}}
</script>

<script type="text/javascript">

    var _TH_ = {};
    // 基准路径base
    _TH_.base = "${base}"; //tiaohuo

    //底部滚动高度固定
    $(function(){
        var viewHeight = window.innerHeight
                || document.documentElement.clientHeight;
        var topHeight = $(".arrivalRedBag").height();
        var bottomHeight = viewHeight-topHeight;
        document.getElementById("wrapper").style.height = bottomHeight+"px";


        $.tenantList = function(lat,lng,areaId){
            ajaxGet({
                url: "${base}/app/b2c/tenant/list.jhtml",
                data: {
                    pageNumber: 1,
                    orderType:"distance",
                    areaId: areaId?areaId:"${areaId}",
                    lat: lat,
                    lng: lng,
                    isUnion:true
                },
                success: function (data) {
                    if (data.message.type == "success") {
                        var myTemplate = Handlebars.compile($("#wap-list-item").html());
                        $('.recommend_list').html(myTemplate(data));
                    }
                    myScroll.refresh();
                },error:function(){
                    console.log("asdfasdasdfasdf","error");
                }
            });
        };
        $.tenantList(null, null);
        _wxSDK.initInterface($script, {
            afterOnMenuShare: function () {
                _wxSDK.getLocation()
                        .done(function (lat, lng) {
                            ajaxGet({
                                url:'${base}/weixin/lbs/get.jhtml',
                                data:{
                                    lat:lat,
                                    lng:lng
                                },
                                success:function (data) {
                                    $.tenantList(lat, lng, data.area.id);
                                }
                            });
                        })
                        .fail(function () {
                            $.tenantList(null, null);
                        });
            }
        });
    });



    //iphone 不能滚动解决方案 使用iscroll.js插件 此处为页面调用
    var myScroll;
    function loaded() {
        myScroll = new iScroll('wrapper', { scrollbarClass: 'myScrollbar' });
    }
    document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
    document.addEventListener('DOMContentLoaded', loaded, false);
</script>

</body>
</html>