<div class="topNav">
    <div class="container">
        <!--left-->
        <div class="span8 topNavLeft">
        [#if member??]
            <div class="loggedOn" style="display: block;">
                <span>您好, <a href="${base}/b2b/member/order/list.jhtml">${member.username}</a></span>
                [#assign messageCount=0]
                [#list member.inMessages as message]
                    [#if message.receiverDelete==false]
                        [#assign messageCount=messageCount+1]
                    [/#if]
                [/#list]
                <div class="dropdown">
                    <!--<p>
                            消息 (
                            <a  href="${base}/b2b/member/message/list.jhtml"> ${messageCount}</a>
                            )
                        </p>-->
                    <a href="${base}/b2b/member/message/list.jhtml" class="message_btn">
                        <i class="glyphicon glyphicon-envelope message_incons"></i>
                        消息 (
                        <i class="red">  ${messageCount}</i>
                        )
                        <i class="message_down"></i>
                    </a>
                    <div class="dropdown-menu">
                        <span class="compass"></span>
                        <h5 class="header-h5">未读新消息</h5>
                        [#if messageCount!=0]
                            <div class="new-message-box">
                                <div class="message">
                                    <ul>
                                        [#if  member.inMessages.receiverRead ==false]
                                            [#list member.inMessages as message]
                                                [#if message_index>6]
                                                    [#break /]
                                                [/#if]
                                                <li>
                                                    <div class="dt">
                                                        <em>丨</em>
                                                        [#switch message.type]
                                                            [#case "order"]订单提醒
                                                                [#break]
                                                            [#case "account"]账单提醒
                                                                [#break]
                                                            [#case "notice"]系统公告
                                                                [#break]
                                                            [#case "message"]系统消息
                                                                [#break]
                                                            [#case "consultation"]咨询回复
                                                                [#break]
                                                            [#case "contact"]社交圈
                                                        [/#switch]
                                                        <a href="javascript:del(${message.id});" class="delete"
                                                           title="删除"></a>
                                                    </div>
                                                    <div class="dd">
                                                        <div class="info">
                                                        ${message.content}
                                                        </div>
                                                        <div class="info">${message.createDate?string("yyyy-MM-dd HH:mm:ss")}</div>
                                                    </div>
                                                </li>
                                            [/#list]
                                        [/#if]
                                    </ul>
                                </div>
                            </div>
                            <div class="view-message view-cart" style="margin-bottom:8px;padding-top:5px">
                                <a href="${base}/b2b/member/message/list.jhtml">查看消息</a>
                            </div>
                        [#else]
                            <div class="message-default-box">
                                <img src="http://www.zhaoqipei.com/resources/b2b/images/v3.0/message_default_img.jpg">
                                没有新消息了
                            </div>
                        [/#if]
                    </div>
                </div>
                <span><a class="loggedOnA" href="${base}/b2b/logout.jhtml">退出</a></span>
            </div>
        [#else]
            <div class="loggedOut">
                <span>欢迎来到${setting.siteName}</span>
                <a href="${base}/b2b/login.jhtml?type=signin" style="color: red">请登录</a>
                <i>|</i>
                <a href="[#if versionType==1]${base}/b2b/register/register_company.jhtml[#else]${base}/b2b/login.jhtml?type=register[/#if]" style="color: red">免费注册</a>
            </div>
        [/#if]
        </div>
        <!--right-->
        <div class="span16">
            <ul>
                <li>
                    <a href="${base}/b2b/index.jhtml"><i class="icon06"></i>首页</a>
                </li>
                <li>
                    <a href="${base}/b2b/member/order/list.jhtml"><i class="icon01"></i>我的订单</a>
                </li>

                <li class="personal_center" style="padding-left: 0px;">
                    <div class="dropdown">
                        <a class="message_btn" href="${base}/b2b/cart/list.jhtml" style="padding-left: 25px;">
                            <i class="icon02"></i>
                            购物车（
                            <div id="navTopZone_CartSum" style="display:inline;color:red;">0</div>
                            ）
                            <i class="message_down"></i>
                        </a>
                        <div class="dropdown-menu" style="display:none;">
                            <span class="compass"></span>
                            <h5 class="header-h5">最近加入的商品</h5>
                            <div class="new-message-box" id="carBox" style="display:none;padding: 0 15px 0px 15px;">
                                <table class="cart-header-table" width="100%" cellpadding="0" cellspacing="0">
                                    <tbody id="carItemsList">
                                    <tr valign="top" style="border-bottom: 1px solid #eee;">
                                        <td valign="top" width="40" height="40">
                                            <a class="header-cart-img" href="/product/i5134.html" target="_blank">
                                                <img src="http://cdn.zhaoqipei.com/upload/image/201510/1f5a238d-ce2c-4344-8d10-e023d8b72633-thumbnail.jpg"
                                                     width="40" height="40">
                                            </a>
                                        </td>
                                        <td valign="top">
                                            <div class="cart-folder">
                                                <a href="/product/i5134.html" target="_blank">轮胎195/55R16,产地:安徽轮胎195/55R16,产地:安徽
                                                    轮胎195/55R16,产地:安徽</a>
                                            </div>
                                        </td>
                                        <td valign="top" class="cart-folder-price" align="right">
                                            <div class="red">x10000&nbsp;&nbsp;￥113700</div>
                                            <a href="#" class="cart-folder-delete">删除</a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="view-cart" style="display:none;margin-bottom:8px;margin-top: -1px;">
                                <!-- p>
                                    购物车里面还有
                                    <i class="red" style="position: static;background: none;display: inline;">0</i>
                                    件
                                </p -->
                                <a href="${base}/b2b/cart/list.jhtml" target="_blank">查看购物车</a>
                            </div>
                            <div class="message-default-box" id="defaultBox"><img
                                    src="http://www.zhaoqipei.com/resources/b2b/images/v3.0/cart_default_img.jpg">您的购物车还没有任何商品
                            </div>
                        </div>
                    </div>

                </li>

                <li>
                    <a href="${base}/b2b/member/favorite/list.jhtml"><i class="icon03"></i>我的收藏</a>
                </li>
                <li class="site-nav-menu">
                    <div class="site-nav-menu-hd">
                        <a href="${base}/b2b/index.jhtml" target="_top">
                            超市入口
                        </a>
                    </div>
                    <div class="site-nav-menu-bd site-nav-menu-list">
                        <div class="site-nav-menu-bd-panel menu-bd-panel">
                            <a href="javascript:;" target="_top" onclick="pageGo('retailer')">超市入口</a>
                            <a href="javascript:;" target="_blank" onclick="pageGo('tenant')">服务端入口</a>
                            <a href="javascript:;" target="_blank" onclick="pageGo('suppier')">供应商入口</a>
                        </div>
                    </div>
                </li>

            [#--[@navigation_list position = "top"]
                [#list navigations as navigation]
                    <li>
                        <a href="${navigation.url}"[#if navigation.isBlankTarget]
                           target="_blank"[/#if]>${navigation.name}</a>
                        [#if navigation_has_next]|[/#if]
                    </li>
                [/#list]
            [/@navigation_list]--]

                <!-- li>
                    <a href="javascript:();"><i class="icon04"></i>客服中心</a>
                </li>
                <li>
                    <a href="javascript:();"><i class="icon05"></i>网站导航</a>
                </li-->
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    //删除功能
    var del = function (id) {
        if (window.confirm("确认删除吗？")) {
            $.ajax({
                url: "${base}/b2b/member/message/delete.jhtml",
                type: "post",
                data: {id: id},
                dataType: "json",
                success: function (data) {
                    $.message(data);
                    if (data.type == "success") {
                        window.location.reload(true);
                    }
                }
            });
        }
    }

    $(function () {
        refreshCartCount();
    });
    //刷新购物车数量
    function refreshCartCount() {
        var cartTimeout;

        function getCartCount() {
            clearTimeout(cartTimeout);
            $.ajax({
                url: "${base}/b2b/cart/get_cart_count.jhtml",
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#navTopZone_CartSum").text(data.count);

//                    if(data.count>0){
//                        $("#carBox").show();
//                        $(".view-cart").show();
//                        $("#defaultBox").hide();
//                        var html = "";
//                        for(var i =0;i<data.carItems.lenth;i++){
//                            html += '<tr valign="top" style="border-bottom: 1px solid #eee;">'+
//                                    '<td valign="top" width="40" height="40">'+
//                                    '<a class="header-cart-img" href="/product/i5134.html" target="_blank">'+
//                                    '<img src="http://cdn.zhaoqipei.com/upload/image/201510/1f5a238d-ce2c-4344-8d10-e023d8b72633-thumbnail.jpg" width="40" height="40">'+
//                                    '</a>'+
//                                    '</td>'+
//                                    '<td valign="top">'+
//                                    '<div class="cart-folder">'+
//                                    '<a href="/product/i5134.html" target="_blank">轮胎195/55R16,产地:安徽轮胎195/55R16,产地:安徽 轮胎195/55R16,产地:安徽</a>'+
//                                    '</div>'+
//                                    '</td>'+
//                                    '<td valign="top" class="cart-folder-price" align="right">'+
//                                    '<div class="red">x10000&nbsp;&nbsp;￥113700</div>'+
//                                    '<a href="#" class="cart-folder-delete">删除</a>'+
//                                    '</td>'+
//                                    '</tr>';
//                        }
//
//                    }
                }
            });
        }

        cartTimeout = setTimeout(function () {
            getCartCount();
        }, 500);
    }

</script>

<script type="text/javascript">
    //顶部导航超市入口js
    $(document).ready(function () {
        jQuery.navlevel2 = function (level1, dytime) {
            var delytime;
            //显示下拉
            $(level1).mouseenter(function () {
                var varthis = $(this);
                delytime = setTimeout(function () {
                    varthis.find('div.site-nav-menu-bd').show();
                }, dytime);
//                $('div.topNav .container').css("overflow","visible");
            });
            //隐藏下拉
            $(level1).mouseleave(function () {
                clearTimeout(delytime);
                $(this).find('div.site-nav-menu-bd').hide();
//                $('div.topNav .container').css("overflow","hidden");
            });

        };
        //调用
        $.navlevel2("div.header .topNav li.site-nav-menu", 300);
    });
    function pageGo(obj){
        if(obj=="retailer"){
            window.open("${base}/b2b/index.jhtml");
        }else if(obj=="tenant"){
            window.open("${base}/store/member/index.jhtml");
        }else if(obj=="suppier"){
            window.open("${base}/b2b/supplier/login.jhtml");
        }
    }
</script>