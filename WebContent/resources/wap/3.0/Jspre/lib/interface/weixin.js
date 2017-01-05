/**
 * 微信接口调用封装
 * @return {[type]} [description]
 */
var _wxSDK = {};
//获取并验证公众号配置信息
_wxSDK.checkConfig = function() {
    //获取公众号配置信息
    $.ajax({
            url: _TH_.base + '/wap/mutual/get_config.jhtml',
            data: { url: location.href.split('#')[0] }
        })
        .done(function(message) {
            if (message.type == "success") {
                var data = JSON.parse(message.content);
                //验证公众号信息
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: data.appId, // 必填，公众号的唯一标识
                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr, // 必填，生成签名的随机串
                    signature: data.signature, // 必填，签名，见附录1
                    jsApiList: ["scanQRCode", "onMenuShareTimeline", "onMenuShareAppMessage", "getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
                //保存基本分享信息
                if (!store.session.get('baseshareinfo')) {
                    store.session.set('baseshareinfo', {
                        title: data.sharetitle,
                        desc: data.sharedescr,
                        link: data.sharelink,
                        imgUrl: data.shareimage
                    });
                }
            } else {
                //showDialog2("提示", message.content);
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: message.content
                });
            }
        })
        .fail(function() {
            $(".ONCET").tip("addTask", {
                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                txt: "公众号配置信息调取失败"
            });
        });
};
//调取地理位置
_wxSDK.getLocation = function() {
    var $def = $.Deferred();
    if (store.session.get('userlocation')) { //如果从缓存中得到了用户当前位置
        $def.resolve(store.session.get('userlocation').lat, store.session.get('userlocation').lng);
    } else {
        wx.checkJsApi({
            jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                if (res.checkResult.getLocation) {
                    wx.getLocation({
                        type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                        success: function(res) {
                            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                            var speed = res.speed; // 速度，以米/每秒计
                            var accuracy = res.accuracy; // 位置精度
                            /* 
                            $(".ONCET").tip("addTask", {
                                txt: "纬度:" + latitude + ",经度:" + longitude
                            });*/
                            store.session.set('userlocation', {
                                lat: latitude,
                                lng: longitude
                            });
                            $def.resolve(latitude, longitude);
                        },
                        fail: function() {
                            $def.reject();
                        }
                    });
                }
            }
        });
    }
    return $def.promise();
};
//调取分享接口
_wxSDK.onMenuShare = function() {
    var $def = $.Deferred();
    wx.checkJsApi({
        jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'],
        success: function(res) {
            // 以键值对的形式返回，可用的api值true，不可用为false
            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            var _title = $("meta[property='og:title']").attr("content");
            var _desc = $("meta[property='og:desc']").attr("content");
            var _link = $("meta[property='og:link']").attr("content");
            var _imgUrl = $("meta[property='og:imgUrl']").attr("content");
            var shareopts = {
                title: _title ? _title : store.session.get('baseshareinfo').title,
                desc: _desc ? _desc : store.session.get('baseshareinfo').desc,
                link: _link ? _link : store.session.get('baseshareinfo').link,
                imgUrl: _imgUrl ? _imgUrl : store.session.get('baseshareinfo').imgUrl
            };
            if (res.checkResult.onMenuShareTimeline) {
                wx.onMenuShareTimeline(shareopts);
            }
            if (res.checkResult.onMenuShareAppMessage) {
                wx.onMenuShareAppMessage(shareopts);
            }
            $def.resolve();
        }
    });
    return $def.promise();
};
//调取二维码扫描
_wxSDK.scanQRCode = function() {
    wx.checkJsApi({
        jsApiList: ['scanQRCode'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
        success: function(res) {
            // 以键值对的形式返回，可用的api值true，不可用为false
            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            wx.scanQRCode({
                desc: '扫一扫'
            });
        }
    });
};
//初始化微信接口
_wxSDK.initInterface = function($script, callbacks) {
    $script('http://res.wx.qq.com/open/js/jweixin-1.0.0.js', function() {
        //拿到接口对象并且可以使用wx
        //微信接操纵对象事件监听,配置信息验证通过立即调用的接口行为
        wx.ready(function() {
            //每个页面都需要的分享接口调用
            _wxSDK.onMenuShare()
                .always(function() {
                    //其他接口调用
                    if (callbacks && callbacks.afterOnMenuShare) {
                        callbacks.afterOnMenuShare();
                    }
                });
        });
        wx.error(function(res) {
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            $(".ONCET").tip("addTask", {
                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                txt: "公众号配置信息错误"
            });
        });
        //获取并验证公众号配置信息
        _wxSDK.checkConfig();
    });
};
