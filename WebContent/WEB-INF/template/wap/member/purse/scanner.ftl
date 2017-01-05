<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page" style="padding:15px 15px;">
        <div style="border-radius: 5%;background-color: #ffffff;display: inline-block;width: 100%;text-align: center;">
            <p style="padding: 8% 0;font-size: 18px;">微信扫一扫，向我付款</p>
            <img style="width: 50vmin;height: 50vmin;margin: 5% 0;" id="picture" src="">
            <p style="padding: 8% 0;font-size: 18px;">￥${amount}</p>
        </div>
    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    var InterValObj;
    $(function () {
        init();
        ajaxPost({
            url:'${base}/app/member/cashier/submit.jhtml',
            data:{
                paymentPluginId:'weixinQrcodePayPlugin',
                amount:"${amount}"
            },
            success:function(data){
                if(data.message.type!="success"){
                    showDialog2("友情提示",data.message.content);
                }
                $.ajax({
                    url:"http://api.wwei.cn/wwei.html?apikey=20160806215020&data="+data.data.code_url,
                    type:'post',
                    dataType:"jsonp",
                    jsonp: "callback",
                    success:function(result){
                        closeWaitLoadingToast();
                        $(document).unbind("ajaxBeforeSend");
                        $("#picture").attr("src",result.data.qr_filepath);
                        InterValObj=window.setInterval(function(){
                            queryCash(data.data.sn);
                        },3000);
                    }
                });
            }
        });
    });

    function queryCash(sn){
        $.ajax({
            url:'${base}/app/member/cashier/query.jhtml',
            data:{
                sn:sn
            },
            type:'post',
            dataType:'json',
            success:function(dataBlock){
                if(dataBlock.message.type=="success"){
                    window.clearInterval(InterValObj);
                    location.href="${base}/wap/member/purse/success.jhtml?sn="+sn;
                }
                if(dataBlock.message.type=="error"){
                    window.clearInterval(InterValObj);
                    showDialog2("友情提示",dataBlock.message.content);
                }
            }
        });
    }
</script>

</body>
</html>