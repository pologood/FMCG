/**
 * Created by My-PC on 16/03/16.
 */
function operation(b,o, value, id) {
    if ('zan' == value) {
        ajaxPost({
            url: b+"/app/member/contact/praises.jhtml?id=" + id,
            success: function (data) {
                if ('执行成功' == data.message.content) {
                    o.css('color', 'red');
                } else if ('error' == data.message.content) {
                    o.css('color', 'black');
                }
            }
        });
    } else if ('comment' == value) {
        location.href = b+"/wap/social_circles/comment.jhtml?id=" + id+"&type="+value;
    } else if ('fenxiang' == value) {
        location.href = b+"/wap/social_circles/comment.jhtml?id=" + id+"&type="+value;
        //ajaxGet({
        //    url: b+"/wap/mutual/get_config.jhtml",
        //    data: {
        //        url: location.href.split('#')[0]
        //    },
        //    dataType: "json",
        //    success: function (message) {
        //        if (message.type == "success") {
        //            var data = JSON.parse(message.content);
        //            wx.config({
        //                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        //                appId: data.appId, // 必填，公众号的唯一标识
        //                timestamp: data.timestamp, // 必填，生成签名的时间戳
        //                nonceStr: data.nonceStr, // 必填，生成签名的随机串
        //                signature: data.signature,// 必填，签名，见附录1
        //                jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        //            });
        //            wx.ready(function () {
        //                // 获取“分享到朋友圈”按钮点击状态及自定义分享内容接口
        //                //wx.onMenuShareTimeline({
        //                //    title: '分享标题', // 分享标题
        //                //    link:"分享的url,以http或https开头",
        //                //    imgUrl: "分享图标的url,以http或https开头" // 分享图标
        //                //});
        //                // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
        //
        //
        //                ajaxGet({
        //                    url:b+"/app/member/contact/share.jhtml?id="+id+"&type=app",
        //                    success:function(data){
        //                        if('success' == data.message.type){
        //
        //                            wx.onMenuShareAppMessage({
        //                                title: '分享标题', // 分享标题
        //                                desc: "分享描述", // 分享描述
        //                                link:data.data.url,
        //                                imgUrl: "分享图标的url,以http或https开头", // 分享图标
        //                                type: 'link' // 分享类型,music、video或link，不填默认为link
        //                            });
        //
        //
        //                            //ajaxGet({
        //                            //    url:b+"/app/member/contact/qrcode.jhtml?url="+data.data.url,
        //                            //    success:function(data){
        //                            //        if('success' == data.message.type){
        //                            //            showDetailDialog($("#weixin-tip"));
        //                            //            $("#qr_img").attr("src",data.data);
        //                            //        }else{
        //                            //            showToast(data.message.content);
        //                            //        }
        //                            //    }
        //                            //});
        //                        }else{
        //                            showToast(data.message.content);
        //                        }
        //                    }
        //                });
        //
        //
        //            });
        //            wx.error(function (res) {
        //                // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        //            });
        //            wx.checkJsApi({
        //                jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
        //                success: function (res) {
        //                    // 以键值对的形式返回，可用的api值true，不可用为false
        //                    // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
        //                }
        //            });
        //        } else {
        //            showLoadingToast();
        //            //invokTips("error", message.content);
        //        }
        //    }
        //});
    }
}

function publish(base,receiveId,id,value,type){

    if(receiveId==''){
        showToast2({content:'您还没有绑定'});
        return;
    }
    if(value==''){
        showToast2({content:'内容不能为空'});
        return;
    }
    ajaxPost({
        url:base+"/app/member/contact/reply.jhtml",
        data:{
            receiveId:receiveId,
            id:id,
            content:value
        },
        success:function(data){
            showToast(data.message.content);
            location.reload();
        }
    });
    if('1' == type){
        showDetailDialog($('#message-tip'));
    }
}