<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/fixed/tenant/list.css"/>
    <title>[#if tenantCategory!=null] ${tenantCategory.name} [#else]附近[/#if]</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <!-- <script src="${base}/resources/wap/2.0/js/zepto-data.js"></script>-->
    <script src="${base}/resources/wap/2.0/js/zepto-callbacks.js"></script>
    <script src="${base}/resources/wap/2.0/js/zepto-deferred.js"></script>
    <script src="${base}/resources/wap/2.0/js/touch.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each data}}
        [#include "/wap/include/tenantlist-indexnew.ftl"/]
        {{/each}}
    </script>

    <!-- 下拉菜单通用模板dropcasmenu-itself-->
    <script type="text/x-handlebars-template" id="tpl_dropcasmenu-main">
        <!-- 下拉级联菜单body-->
        <div class="menu-rap menu-L">
            <ul class="menu-L-list">
                {{#each this}}
                <!-- test-->
                <li data-id='{{id}}' onclick="get_id(this)">
                    <div class="itname"><span>{{name}}</span></div>
                    <div class="itdesc">
                        <span class="count">{{num}}</span>
                        <i class="iconfont">&#xe635;</i>
                    </div>
                </li>
                {{/each}}
            </ul>
        </div>
        <div class="menu-rap menu-R">

        </div>
    </script>

    <!-- 排序下拉菜单dropcasmenu-itself-->
    <script type="text/x-handlebars-template" id="tpl_orderType">
        <!-- 下拉级联菜单body-->
        <div class="dropcasmenu">
            <div class="menu-rap">
                <ul class="menu-L-list">
                [#list orderTypes as orderType]
                    <!-- test-->
                    <li data-id={{dataid}}>
                        <div class="itname"><span>${message("Tenant.OrderType." + orderType)}</span></div>
                        <div class="itdesc">
                            <span class="count"></span>
                            <i class="iconfont">&#xe635;</i>
                        </div>
                    </li>
                [/#list]
                </ul>
            </div>
        </div>
    </script>
    <!-- dropcasmenu-right-->
    <script type="text/x-handlebars-template" id="tpl_dropcasmenu-R">
        <ul class="menu-R-list">
            <!--全部 -->
            <li data-id="" onclick="get_community_category_order(1,this)">
                <div class="itname"><span>全部</span></div>
                <!--<div class="itdesc">
                    <span class="count2">10</span>
                </div>-->
            </li>
            <!-- 以A001子菜单作为默认显示-->
            {{#each this}}
            <!-- test-->
            <li data-id="{{id}}" onclick="get_community_category_order(1,this)">
                <div class="itname"><span>{{name}}</span></div>
                <!--<div class="itdesc">
                    <span class="count2">10</span>
                </div>-->
            </li>
            {{/each}}
        </ul>
    </script>
</head>
<style>
    a i {
        color: #999999;
    }

    .fixedtop_tab {
        background-color: #eee;
    }

    .dropcasmenu {
        display: none;
    }

    .menu-rap {

    }

    .menu-rap ul {
        display: table;
        width: 100%;
        font-size: 12px;
    }

    .menu-rap li {
        display: table-row;
        font-size: 1.2em;
        width: 100%;
    }

    .menu-rap li.active {
        background-color: #fff;
    }

    .menu-rap li > div {
        display: table-cell;
        width: 50%;
        height: 2.5em;
        text-align: center;
        vertical-align: middle;
        padding: 0 0.5em;
    }

    .menu-rap .itname {
        color: #666;
    }

    .menu-rap .itname span {
        vertical-align: middle;
    }

    .menu-rap .itdesc {
        text-align: right;
    }

    .menu-rap .itdesc span, .menu-rap .itdesc i {
        vertical-align: middle;
    }

    .menu-rap .itdesc .iconfont {
        color: #c9c9c9;
        font-size: 1.2em;
    }

    .menu-rap .count {
        display: inline-block;
        background-color: #aaa;
        font-size: 0.8em;
        border-radius: 0.4em;
        color: #fff;
        padding: 0 0.3em;
        text-align: center;
        line-height: 1.5em;
    }

    .menu-rap.menu-R {
        padding: 0 1em;
    }

    .menu-rap.menu-R li > div {
        border-bottom: 1px solid #d6d6d6;
    }

    /* .dropcasmenu-title*/
    .dropcasmenu-title {
        font-size: 12px;
    }

    .dropcasmenu-title a {
        font-size: 1.4em;
    }

    .dropcasmenu-title .active a, .dropcasmenu-title .active .iconfont {
        color: #ff6d06;
    }

    .dropcasmenu-title .active a:after {
        content: "\e659";
    }

    .dropcasmenu-title a .iconfont {
        font-size: 0.7em;
    }

    .dropcasmenu .menu-L {
        background-color: #eeeeee;
    }

    .dropcasmenu-title > div a:after {
        display: inline;
        content: "\e612";
        font-family: "iconfont" !important;
        font-style: normal;
        -webkit-font-smoothing: antialiased;
        -webkit-text-stroke-width: 0.2px;
        -moz-osx-font-smoothing: grayscale;
        font-size: 0.7em;
    }
</style>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">
</div>
<script type="text/html" id="tpl_wraper">
    <div class="empty-for-fixedtop_tab"></div>
    <div class="am-g fixedtop_tab">
        <!-- 搜索框-->
        <!-- 
        <div class="am-g">
            <div class="bd">
                <div style="top: 0;left: 0;z-index:1000;background-color: #efefef;width:100%;">
                    <div class="weui_search_bar" id="search_bar" style="background-color: white;">
                        <div class="weui_cell_bd weui_cell_primary defaults radius-left radius-right" >
                            <input class="weui_input" id="search_input" type="text" placeholder="根据商家名称搜索">
                        </div>
                        <div class="weui_cell_ft" style="margin-left: 10px;">
                            <a href="javascript:;" class="weui_btn weui_btn_mini weui_btn_default"
                               id="weui_icon_search">搜索</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>-->
        <!-- 定位信息-->
        <div class="weui_cells_tips weui_cell cl border-bt locateposR TTL" id="resetPosition"
             style="background-color:#eee;">
            <div class="weui_cell_primary font-small">
                当前：<span class="areaname">[#if area??]${area.name}[#else]未定位到城市[/#if]</span>
            </div>
            <div class="weui_cell_bd">
                <a href="#" style="color:#999999;">
                    <i class="iconfont">&#xe623;</i>
                </a>
            </div>
        </div>
        <!-- 下拉菜单title-->
        <div class="weui_cell cl shop-items dropcasmenu-title border-bt"
             style="background-color:#fff;">
            <div class="weui_cell_primary " id="pos">
                <a href="javascript:" class="visited">全城</a>
            </div>
            <div class="weui_cell_primary " id="kind">
                <a href="javascript:" class="visited">分类</a>
            </div>
            <div class="weui_cell_primary " id="order">
                <a href="javascript:" class="visited">排序</a>
            </div>
        </div>
    </div>
    <div id="nearbyList" class="weui_cells weui_cells_access box_tenantinfoR"
         style="margin-top:0px;background-color: #f2f2f2;">
    </div>
    [#include "/wap/include/footer.ftl"/]
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script src="${base}/resources/wap/2.0/js/simboo-DropCascadingMenu.js"></script>
<script>
    var lat, lng;
    function getNowLocation(afterGetLocation) {
        ajaxGet({
            url: "${base}/wap/mutual/get_config.jhtml",
            data: {
                url: location.href.split('#')[0]
            },
            dataType: "json",
            success: function (message) {
                if (message.type == "success") {
                    var data = JSON.parse(message.content);
                    wx.config({
                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                        appId: data.appId, // 必填，公众号的唯一标识
                        timestamp: data.timestamp, // 必填，生成签名的时间戳
                        nonceStr: data.nonceStr, // 必填，生成签名的随机串
                        signature: data.signature,// 必填，签名，见附录1
                        jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                    });
                    wx.ready(function () {
                        wx.getLocation({
                            success: function (res) {
                                var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                                var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                                lng = res.longitude;
                                lat = res.latitude;

                                //run callback
                                //if (afterGetLocation) {
                                    afterGetLocation(latitude, longitude);
                                //}
                            }
                        });
                    });
                    wx.error(function (res) {
                        afterGetLocation(null, -1);
                        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                    });
                    wx.checkJsApi({
                        jsApiList: ['getLocation'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                        success: function (res) {
                            // 以键值对的形式返回，可用的api值true，不可用为false
                            // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                            console.log("checkJsApi",new Date());
                        }
                    });
                } else {
                    showLoadingToast();
                }
            }
        });
    }
    /**
     * 序列化星级
     * 根据固定html结构中元素提供的数字，把星星级别化
     */
    (function ($) {
        $.fn.starLevelize = function () {
            return this.each(function (index, ele) {
                var $ele = $(ele);
                //get flag
                if ($ele.data("starlevelized")) {
                    return;
                }
                var starnum = parseFloat($ele.data("starnum")).toFixed(1);
                var starnum_int = parseInt(starnum, 10); //3
                var starnum_dec_per = parseInt((starnum - starnum_int) * 100, 10) + "%"; //0.7
                //3个fullstar 1个half star 1个empty star
                if (starnum_int <= 5) {
                    $ele.children().eq(starnum_int).css({
                        background: '-webkit-linear-gradient(left, #ff6d06 ' + starnum_dec_per + ', #bfbebc ' + starnum_dec_per + ')'
                    });
                }
                for (var i = starnum_int + 1; i < 5; i++) {
                    $ele.children().eq(i).css({
                        background: '-webkit-linear-gradient(left, #bfbebc, #bfbebc)'
                    });
                }
                //set flag
                $ele.data("starlevelized", true);
            });
        };
    })(Zepto);

    $(function () {
        init();
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        fixedEleCopyHandler(".empty-for-fixedbottom_tab", ".am-topbar-fixed-bottom");
        //定位当前城市
        //当前位置坐标
        //通过微信sdk获取当前经纬度
        //包含ajax请求

        //根据经纬度查询地域名称
        function getCityName(latitude, longitude) {
            ajaxGet({
                url: "${base}/app/lbs/update.jhtml",
                type: "get",
                dataType: "json",
                data: {
                    lat: latitude,
                    lng: longitude,
                    force: true
                },
                success: function (data) {
                    if (data.message.type == "success") {
                        location.reload();
                        $(".locateposR .areaname").text(data.data.name);
                    }
                }
            });
        }

        //soga begin
        function test_getCityName(latitude, longitude, callback) {
            ajaxGet({
                url: "${base}/app/lbs/update.jhtml",
                type: "get",
                dataType: "json",
                data: {
                    lat: latitude,
                    lng: longitude,
                    force: true
                },
                success: function (data) {
                    if (data.message.type == "success") {
                        //location.reload();
                        if (callback) {
                            callback(data);
                        }
                    }
                }
            });
        }

        //当前：定位城市
        $("#resetPosition").on("click", function () {
            getNowLocation(function (latitude, longitude) {
                getCityName(latitude, longitude);
            });
        });

        //首次加载，功能暂时屏蔽
        get_community_category_order(1, "");

    });
    var flag = "";
    var is_completed;
    var is_load;
    var pageNumber;
    var objc;
    /* 层级下拉菜单*/
    $(function () {
        //实例化全城
        $(".dropcasmenu-title #pos").dropCascadingMenu({
            type: "area",
            mask_claname: "mask-halfblack",
            urls: {
                forL1: "${base}/wap/tenant/getCategory.jhtml[#if productChannel!=null]?channelId=${productChannel.id}[/#if]",
                forL2: "${base}/ajax/community/list.jhtml"
            }
        });
        $(".dropcasmenu-title #pos").on("click", function () {
            flag = "area";
        });
        //实例化分类
        $(".dropcasmenu-title #kind").dropCascadingMenu({
            type: "category",
            mask_claname: "mask-halfblack",
            urls: {
                forL1: "${base}/wap/tenant/getCategory.jhtml[#if productChannel!=null]?channelId=${productChannel.id}[/#if]",
                forL2: "${base}/ajax/tenant_category/childrens.jhtml"
            }
        });
        $(".dropcasmenu-title #kind").on("click", function () {
            flag = "category";
        });
        //监听弹出窗口独占请求
        $(document).on('global:iam_onlyone', function (event, caller_slter, pluginname, methodname_hide, caller_active_claname) {
            event.preventDefault();
            //如果请求来自同一对象
            var setdata = function () {
                _g.the_onlyone = {
                    caller_slter: caller_slter,
                    pluginname: pluginname,
                    methodname_hide: methodname_hide,
                    caller_active_claname: caller_active_claname
                };
            };
            //没有对象索要唯一展示，首次呼出遮罩
            if ($.type(_g.the_onlyone) == "undefined") {
                //保存当前独占者
                setdata();
            } else { //非首次呼出
                //与上个独占者相同 return
                if (caller_slter == _g.the_onlyone.caller_slter) {
                    return;
                }
                //上个独占者隐藏相应窗口
                $(_g.the_onlyone.caller_slter)[_g.the_onlyone.pluginname](_g.the_onlyone.methodname_hide);

                //$(_g.the_onlyone.caller_slter).removeClass(_g.the_onlyone.caller_active_claname);
                $(_g.the_onlyone.caller_slter).trigger('global:iam_onlyone_end');
                //保存当前独占者
                setdata();
            }
        });
        //监听弹出窗口独占请求结束
        $(document).on('global:iam_onlyone_end', function (event) {
            event.preventDefault();
            //上个独占者激活态失效
            $(_g.the_onlyone.caller_slter).removeClass(_g.the_onlyone.caller_active_claname);
            delete _g.the_onlyone;
        });
        //虚拟数据for dropDownSelect
        var orderdatas_soga = [{
            id: "001",
            name: "默认排序"
        }, {
            id: "002",
            name: "点击降序"
        }, {
            id: "003",
            name: "评分降序"
        }, {
            id: "004",
            name: "日期降序"
        }];
        $(".dropcasmenu-title #order").dropDownSelect({
            paramdatas: {
                type: "order"
            },
            url: "url_you_should_give",
            virtualdata: orderdatas_soga
        });
        $("#order").on("click", function () {
            flag = "order";
        });
        //======全城/分类/排序(分页加载)======//
        scroll(function () {
            if (is_load == "false" && is_completed == "false") {
                get_community_category_order(pageNumber, objc);
            }
        });
    });

    [#--ajaxGet({--]
    [#--url:"${base}/app/b2c/tenant/list.jhtml",--]
    [#--data:{--]
    [#--areaId:1029,--]
    [#--lat:31.820587,--]
    [#--lng:117.227239--]
    [#--},--]
    [#--success:function(data){--]
    [#--if(data.message.type=="success"){--]
    [#--var myTemplate = Handlebars.compile($("#wap-list-item").html());--]
    [#--is_load="false";--]
    [#--if(pageNumber==1){--]
    [#--if(data.data.length<20){--]
    [#--is_completed="true";--]
    [#--}else if(data.data.length==20){--]
    [#--pageNumber++;--]
    [#--}--]
    [#--$('#nearbyList').html(myTemplate(data.data));--]
    [#--}else if(pageNumber>1){--]
    [#--if(data.data.length<20){--]
    [#--is_completed="true";--]
    [#--}else if(data.data.length==20){--]
    [#--pageNumber++;--]
    [#--}--]
    [#--$('#nearbyList').append(myTemplate(data.data));--]
    [#--}--]
    [#--$(".lazy").picLazyLoad({--]
    [#--threshold: 100,--]
    [#--placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'--]
    [#--});--]
    [#--$(".starlevels").starLevelize();--]
    [#--$(".youhuiquanShow .togglebtn").click();--]
    [#--}--]
    [#--}--]
    [#--});--]

    //======全城/分类/排序======//
    function get_community_category_order(num, obj) {
        var tenantCategoryId = "${(tenantCategory.id)!}";
        var communityId = "";
        var orderType = "";
        var areaId = "${area.id}";
        is_completed = "false";
        objc = obj;
        pageNumber = num;
        if (flag == "area") {
            communityId = $(obj).attr("data-id");
            areaId = area_id;
            if (communityId == "") {
                $(".dropcasmenu-title #pos a").html($("li[data-id='" + area_id + "']").find("div[class='itname']").find("span").html());
            } else {
                $(".dropcasmenu-title #pos a").html($(obj).text());
            }
            $(".dropcasmenu-title #pos").removeClass('active').dropCascadingMenu("hide");
        } else if (flag == "category") {
            if ($(obj).attr("data-id") == "") {
                tenantCategoryId = tenantCategory_id;
                $(".dropcasmenu-title #kind a").html($("li[data-id='" + tenantCategory_id + "']").find("div[class='itname']").find("span").html());
            } else {
                tenantCategoryId = $(obj).attr("data-id");
                $(".dropcasmenu-title #kind a").html($(obj).text());
            }
            $(".dropcasmenu-title #kind").removeClass('active').dropCascadingMenu("hide");
        } else if (flag == "order") {
            if ($(obj).attr("id") == "001") {
                orderType = "weight";
            } else if ($(obj).attr("id") == "002") {
                orderType = "hitsDesc";
            } else if ($(obj).attr("id") == "003") {
                orderType = "scoreDesc";
            } else if ($(obj).attr("id") == "004") {
                orderType = "dateDesc";
            }
            $(".dropcasmenu-title #order a").html($(obj).text());
            $(".dropcasmenu-title #order").removeClass("active").dropDownSelect("hide");
            $(".dropcasmenu-title #order").get(0).mask_halfblack.hide();
        } else {
            $(".dropcasmenu-title #kind").removeClass('active').dropCascadingMenu("hide");
        }
        is_load = "true";
        getNowLocation(function (now_lat, now_lng) {
            ajaxGet({
                url: "${base}/app/b2c/tenant/list.jhtml",
                data: {
                    tenantCategoryId: tenantCategoryId,
                    communityId: communityId,
                    orderType: orderType,
                    pageNumber: pageNumber,
                    areaId: areaId,
                    [#if productChannel!=null]channelId:${productChannel.id},[/#if]
                    lat: now_lat,
                    lng: now_lng
                },
                success: function (data) {
                    if (data.message.type == "success") {
                        var myTemplate = Handlebars.compile($("#wap-list-item").html());
                        is_load = "false";
                        if (pageNumber == 1) {
                            if (data.data.length < 20) {
                                is_completed = "true";
                            } else if (data.data.length == 20) {
                                pageNumber++;
                            }
                            $('#nearbyList').html(myTemplate(data));
                        } else if (pageNumber > 1) {
                            if (data.data.length < 20) {
                                is_completed = "true";
                            } else if (data.data.length == 20) {
                                pageNumber++;
                            }
                            $('#nearbyList').append(myTemplate(data));
                        }
                        $(".lazy").picLazyLoad({
                            threshold: 100,
                            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                        });
                        //$(".starlevels").starLevelize();
                        //$(".youhuiquanShow .togglebtn").click();
                    }
                },error:function(){
                    console.log("asdfasdasdfasdf","error");
                }
            });
        });
    }
    var area_id, tenantCategory_id;
    function get_id(obj) {
        if (flag == "area") {
            area_id = $(obj).attr("data-id");
        } else {
            tenantCategory_id = $(obj).attr("data-id");
        }
    }
</script>
</body>
</html>
