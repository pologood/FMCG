<!DOCTYPE html>
<html lang="en">
<head>
    [#include "/wap/include/resource-2.0.ftl"]
    <meta charset="UTF-8">
    <title>城市切换</title>
    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <!-- No Baidu Siteapp-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!-- Add to homescreen for Chrome on Android -->
    <meta name="mobile-web-app-capable" content="yes">
    <!-- Add to homescreen for Safari on iOS -->
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="${setting.siteName}"/>
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="yes" name="apple-touch-fullscreen" />
    <meta content="telephone=no,email=no" name="format-detection" />
    <meta name="App-Config" content="fullscreen=yes,useHistoryState=yes,transition=yes" />
    
    <link rel="stylesheet" href="${base}/resources/wap/css/amazeui.css">
    <link rel="stylesheet" href="${base}/resources/wap/css/common.css">
    <link rel="stylesheet" href="${base}/resources/wap/css/wap.css">
    <style type="text/css">
        table.city-table td {
            border-left: 1px solid #E8E9EA;
            border-bottom: 1px solid #E8E9EA;
            text-align: center;
            line-height: inherit;
            height: 40px;
            width: 80px;
        }
        .am-navbar-default .am-navbar-nav {
            background-color: #E8E9EA;
        }
        .am-topbar {
            border-top:1px solid #E8E9EA;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
    <script src="${base}/resources/wap/js/flexible.debug.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="${base}/resources/data/area.js"></script>
    <script type="text/javascript">
        $().ready(function(){
        //读取js中的城市 
        initCity();
        var letter=['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
        for(var i=0;i<letter.length;i++){
            var city=getAreas(letter[i].toLowerCase());
            var html="<tr id='"+letter[i]+"'><td rowspan='2' class='am-text-middle first-letter'>"+letter[i]+"</td>";
            var h=9;
            var a=Math.ceil((city.length+2)/4)*4-city.length-2;//空格

            for(var j=0;j<city.length;j++){
                html+="<td><a href='${base}/wap/area/update_current.jhtml?id="+city[j].id+"'>"+city[j].name+"</a></td>";
                if(j==2){
                    html+="</tr><tr>";
                }
                if(j==5){
                    html+="</tr><tr>";
                };
                if(j==h){    
                    html+="</tr><tr>";
                    h+=4;
                }
            }
            if(city.length==0){
                    continue;
            }else if(city.length==1){
                html+="<td></td><td></td></tr><tr><td><a></a></td><td><a>&nbsp;</a></td><td><a></a></td></tr>";
            }else if(city.length==2){
                html+="<td></td></tr> <tr><td><a>&nbsp;</a></td><td></td><td></td></tr>";
            }else if(city.length==3){
                html+="<td><a>&nbsp;</a></td><td></td><td></td></tr>";
            }else if(city.length>3){
                if(a==0){
                    html=html.substring(0,html.length-5);
                }else if(a==1){
                    html+="<td></td></tr>";
                }else if(a==2){
                    html+="<td></td><td></td></tr>";
                }else if(a==3){
                    html+="<td></td><td></td><td></td></tr>";
                }
            }
            $("#table").append(html); 
        }
        
        // 定位当前地理位置
        $("#resetPosition").on("click",function(){
            [#if browse_version=="MicroMessenger"]
                $.ajax({
                    url:"${base}/wap/mutual/get_config.jhtml",
                    data:{
                        url:location.href.split('#')[0]
                    },
                    dataType:"json",
                    type:"get",
                    success:function(message){
                        if(message.type=="success"){
                            var data=JSON.parse(message.content);
                            wx.config({
                                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                                appId: data.appId, // 必填，公众号的唯一标识
                                timestamp: data.timestamp, // 必填，生成签名的时间戳
                                nonceStr: data.nonceStr, // 必填，生成签名的随机串
                                signature: data.signature,// 必填，签名，见附录1
                                jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                            });
                            wx.ready(function(){
                                wx.getLocation({
                                    success: function (res) {
                                        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                                        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                                        var speed = res.speed; // 速度，以米/每秒计
                                        var accuracy = res.accuracy; // 位置精度
                                        $.ajax({
                                            url:"${base}/ajax/lbs/update.jhtml",
                                            type:"get",
                                            dataType:"json",
                                            data:{
                                                lat:latitude,
                                                lng:longitude,
                                                force:true
                                            },
                                            beforeSend:function(){
                                                // invokTips("warn","正在重新定位。。");
                                                showToast2({content:"正在重新定位....."});
                                            },
                                            success:function(message){
                                                if(message.type=="success"){
                                                    location.reload();
                                                }
                                            }
                                        });
                                    }
                                });
                            });
                            wx.error(function(res){
                                // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                            });
                            wx.checkJsApi({
                                jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                                success: function(res) {
                                    // 以键值对的形式返回，可用的api值true，不可用为false
                                    // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                                }   
                            });
                        }else{
                            // invokTips("error",message.content);
                            showToast2(message);
                        }
                    },
                });
            [#else] 
                    showToast2({content:"正在定位....."});
                    // invokTips("warn","正在定位。。");
                    location.href="vsstoo://location?url=${base}/wap/delivery/list.jhtml"; 
                    return false;    
            [/#if]
        });
    });
    </script>

</head>
<body class="cart-page">
    <div class="am-g">
        <div class="am-center-55">
            <div class="mt_4 mb_4 Locating-city rounded-8 pd_2 align-center" id="resetPosition" >
                <i class="icon iconfont align-center" id="icon-map">&#xe649;</i>
                <span class="color_999 fs-18" >
                    定位城市：
                </span>
                <span class="dark-grey fs-18" >
                    [#if area??]${area.name}[#else]未定位到城市[/#if]
                </span>
            </div>
            <div class="am-g">
                <span class=" dark-grey font-normal">
                热门城市
                </span>
            </div>
            <div class="am-g mt_3 mb_4">
                <ul class="rounded-8 Locating-city am-g city-items">
                    <li><a href="${base}/wap/area/update_current.jhtml?id=1">北京</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=792">上海</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=1947">广州</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=1971">深圳</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=18">天津</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=811">南京</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=927">杭州</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=1165">厦门</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=853">苏州</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=1029">合肥</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=1693">武汉</a></li>
                    <li><a href="${base}/wap/area/update_current.jhtml?id=2808">西安</a></li>
                    <!-- 此处li必须为4的倍数，否则会样式变形 -->
                </ul>
            </div>
            <div class="am-g">
                <span class="mb_3 mt_4 dark-grey font-normal">
                全部城市
                </span>
            </div>
            <div class="am-g mt_3 mb_4">
                <ul class="am-g letter-items rounded-8 Locating-city">
                    <li><a href="#A">A</a></li>
                    <li><a href="#B">B</a></li>
                    <li><a href="#C">C</a></li>
                    <li><a href="#D">D</a></li>
                    <li><a href="#E">E</a></li>
                    <li><a href="#F">F</a></li>
                    <li><a href="#G">G</a></li>
                    <li><a href="#H">H</a></li>
                    <li><a href="#I">I</a></li>
                    <li><a href="#J">J</a></li>
                    <li><a href="#K">K</a></li>
                    <li><a href="#L">L</a></li>
                    <li><a href="#M">M</a></li>
                    <li><a href="#N">N</a></li>
                    <li><a href="#O">O</a></li>
                    <li><a href="#P">P</a></li>
                    <li><a href="#Q">Q</a></li>
                    <li><a href="#R">R</a></li>
                    <li><a href="#S">S</a></li>
                    <li><a href="#T">T</a></li>
                    <li><a href="#U">U</a></li>
                    <li><a href="#V">V</a></li>
                    <li><a href="#W">W</a></li>
                    <li><a href="#X">X</a></li>
                    <li><a href="#Y">Y</a></li>
                    <li><a href="#Z">Z</a></li>
                    <!-- 此处li必须为4的倍数，否则会样式变形 -->
                </ul>

                <table class="am-table-bordered am-table-centered bg_white mt_2 city-table" id="table">
                     
                </table>
                <!-- table已做好锚点，新建tr"大写字母" 请在tr里添加id="字母"-->
            </div>
        </div>

    </div>
</body>
</html>
