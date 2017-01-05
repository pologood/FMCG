<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="" content="" />
    <title>聚德汇-会员中心</title>
    <meta name="keywords" content="聚德汇-会员中心" />
    <meta name="description" content="聚德汇-会员中心" />
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <link rel="icon" href="${base}/resources/b2b/images/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/cart.css" type="text/css" rel="stylesheet">
    
</head>
<body>
<!-- 头部start -->
<div class="header bg">
<!-- 顶部导航开始 -->
[#include "/b2b/include/topnav.ftl"]
<!-- 顶部导航结束 -->
</div>
<!--头部end-->

<!--主页内容区start-->
<div class="paper">
    <!-- 会员中心头部开始 -->
    [#include "/b2b/include/member_head.ftl"]
    <!-- 会员中心头部结束 -->
    <!--会员中心-我的订单-->
    <div class="member-content">
        <div class="container">
            [#include "/b2b/include/member_left.ftl"]
            <div class="content">
                <!--我的优惠券-->
                <link href="${base}/resources/b2b/css/coupon.css" type="text/css" rel="stylesheet">
                <div class="my-coupon">
                    <div id="main">
                        <div class="mod-main mod-comm mod-coupon hidden">
                            <div class="mt">
                                <h3>优惠券</h3>
                                <div class="extra-r">
                                    <a id="vipLink" href="javascript:;" target="_blank" >
                                        <b class="coupon-icon"></b>
                                        领取更多优惠券
                                        <em id="vipNum" style="display: none">0</em>
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="mod-main mod-comm">
                            <div class="mc">
                                <div class="coupon-toolbar">
                                    <div class="c-sort" style="margin-left: 20px;">
                                        <ul id="select_status">
                                            <li [#if status=="all"||status==""]class="curr"[/#if] >
                                                <a href="javascript:void(0);" sta="all" onclick="get_status(this)">全部</a>
                                            </li>
                                            <li [#if status=="unuse"]class="curr"[/#if] >
                                                <a href="javascript:void(0);" sta="unuse" onclick="get_status(this)">未使用</a>
                                            </li>

                                            <li [#if status=="used"]class="curr"[/#if]>
                                                <a href="javascript:void(0);" sta="used" onclick="get_status(this)">已使用</a>
                                            </li>

                                            <li [#if status=="expired"]class="curr"[/#if]>
                                                <a href="javascript:void(0);" sta="expired" onclick="get_status(this)">已过期</a>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="extra-r">
                                        <div class="coupon-switch-list">
                                            <a href="#none" id="lump-icon" class="curr" title="块列" ></a>
                                            <a href="#none" id="list-icon" title="列表" ></a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="coupon-empty-box hidden">
                                <i class="e-icon"></i>
                                <div class="txt">
                                    哦噢~小主一张券都没有，快去抢几张~
                                </div>
                                <div class="op-btns mt20">
                                    <a href="javascript:void(0);" target="_blank" class="btn-1" >去领券中心</a>
                                </div>
                            </div>

                            <div class="coupon-items">
                                [#list page.content as couponCode]
                                <div class="coupon-item coupon-item-d [#if couponCode.coupon.status=='Expired'||couponCode.coupon.status=='unBegin'||couponCode.isUsed==true]coupon-item-dgray[/#if]">
                                    <div class="c-type">
                                        <div class="c-price">
                                            <strong>￥${couponCode.coupon.amount}</strong>
                                        </div>
                                        <div class="c-limit">
                                            【消费满&nbsp;${couponCode.coupon.minimumPrice}&nbsp;可用】
                                        </div>
                                        <div class="c-time">${couponCode.coupon.startDate}--${couponCode.coupon.endDate}</div>
                                        <div class="c-type-top"></div>
                                        <div class="c-type-icon"></div>
                                        <div class="c-type-bottom"></div>
                                    </div>

                                    <div class="c-msg">
                                        <div class="c-range">
                                            <div class="range-item">
                                                <span class="label">券&nbsp;&nbsp;编&nbsp;&nbsp;号：</span>
                                                <span class="txt">${couponCode.code}</span>
                                            </div>
                                            <div class="range-item">
                                                <span class="label">商家限制：</span>
                                                <span class="txt" title="全平台">
                                                    <a href="${base}/b2b/tenant/index.jhtml?id=${(couponCode.coupon.tenant.id)!}">${(couponCode.coupon.tenant.name)!}</a>
                                                </span>
                                            </div>
                                            <div class="range-item">
                                                <span class="label">&nbsp;&nbsp;</span>
                                                <span class="txt">&nbsp;&nbsp;</span>
                                            </div>
                                        </div>
                                        <div class="op-btns">
                                            [#--<a href="${base}/b2b/tenant/index.jhtml?id=${couponCode.coupon.tenant.id}" class="btn" target="_blank">--]
                                                [#--<span class="txt">立即使用</span>--]
                                                [#--<b></b>--]
                                            [#--</a>--]
                                                <a href="${base}/b2b/index.jhtml" class="btn" target="_blank">
                                                    <span class="txt">立即使用</span>
                                                    <b></b>
                                                </a>
                                        </div>
                                    </div>
                                   
                                    [#if couponCode.isUsed==true]
                                        <div class="used-site"></div>
                                    [#else]
                                        [#if couponCode.coupon.status=="Expired"]
                                            <div class="overdued-site"></div>
                                        [#elseif couponCode.coupon.status=="unBegin"]
                                            <div class="c-del"></div>
                                        [/#if]
                                    [/#if]
                                </div>
                                [/#list]
                            </div> 
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <form id="listForm" action="list.jhtml" method="get">
        <input type="hidden" name="status" id="coupon_status">
        <div class="ui-page-wrap clearfix" style="    padding-right: 100px;">
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
                [#include "/b2b/include/pagination.ftl"]
            [/@pagination]
        </div>
    </form>
<!--标语start-->
[#include "/b2b/include/slogen.ftl"]
<!--标语end-->
</div>
<!--主页内容区end-->
<script type="text/javascript" language="javascript">
    $(function () {
        $('#list-icon').click(function () {
            var couponItems = $('coupon-items');
            $(this).addClass('curr').siblings().removeClass('curr');
            if (!couponItems.hasClass('coupon-items02')) {
                $('.coupon-items').addClass('coupon-items02');
                setCookie("Jd_Coupon_18326635689_p","list",30);//设置为列表风格
            }
        });

        $('#lump-icon').click(function (){
            var couponItems = $('coupon-items');
            $(this).addClass('curr').siblings().removeClass('curr');
            if($('.coupon-items').hasClass('coupon-items02')) {
                $('.coupon-items').removeClass('coupon-items02');
                setCookie("Jd_Coupon_18326635689_p","lump",30);//设置为块状风格
            }
        });

        $('.coupon-item').hover(function () {
            $(this).addClass('coupon-item-hover');
        },function() {
            $(this).removeClass('coupon-item-hover');
        });

        $('.c-type').hover(function () {
            $(this).addClass('c-type-hover');
        }, function () {
            $(this).removeClass('c-type-hover');
        });

        $('.dt').hover(function () {
            $(".dd-type").each(function (index) {
                var type = $(this).attr("hidetype");
                if (type ==-1) {
                    $(this).addClass('curr');
                }
            });
        });

        var selStyle = getCookie("Jd_Coupon_18326635689_p");
        if(null == selStyle || "" == selStyle){
            setCookie("Jd_Coupon_18326635689_p","lump",30);//默认块状
        }else{
            if("lump" == selStyle){//块状
                var couponItems = $('coupon-items');
                $("#lump-icon").addClass('curr').siblings().removeClass('curr')
                if($('.coupon-items').hasClass('coupon-items02')) {
                    $('.coupon-items').removeClass('coupon-items02');
                }
            }

            if("list" == selStyle){//列表状
                var couponItems = $('coupon-items');
                $("#list-icon").addClass('curr').siblings().removeClass('curr');
                if (!couponItems.hasClass('coupon-items02')) {
                    $('.coupon-items').addClass('coupon-items02');
                }
            }
        }


    });

    function randomUrl(url, obj) {
        var append = url.indexOf('?') == -1 ? '?' : '&';
        $(obj).attr('href', url + append + 'r=' + new Date().getTime());
        $(obj).trigger('click');
    }

    $(".tooltip").Jdropdown({"current": "hover"});

    function get_status(obj){
        if($(obj).attr("sta")=="unuse"){
            $("#coupon_status").val("unuse");
        }else if($(obj).attr("sta")=="used"){
            $("#coupon_status").val("used");
        }else if($(obj).attr("sta")=="expired"){
            $("#coupon_status").val("expired");
        }else{
            $("#coupon_status").val("all");
        }
        $("#listForm").submit();
    }

</script>
<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->

<!--右侧悬浮框 start-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>
<!--右侧悬浮框end-->

</body>
</html>
