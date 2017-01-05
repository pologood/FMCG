<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/iconfont.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/index.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/supplier.css"/>
</head>
<!--[if (gte IE 9)|!(IE)]><!-->
<script src="${base}/resources/helper/js/jquery.js"></script>
<!--<![endif]-->
<!--[if lte IE 8 ]><!-->
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<![endif]-->
<script src="${base}/resources/helper/js/common.js"></script>
<script type="text/javascript">
    $().ready(function () {
    [@flash_message /]
    });
</script>
<body>
[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">
            <div class="members_right">
            [#if versionType==0]
                <div class="members_main_wrapper_title"
                     style="line-height: 45px;height: 45px;background-color: #eb3341;font-size: 14px;color: #fff;margin-bottom: 20px;">
                    <span style="padding-left: 10px;display: inline-block;font-weight: 600;font-size: 16px;">当前积分：${(member.tenant.point)!}</span>
                [@helperRole url='helper/member/activity/list.jhtml']
                    <a onclick="roleRoot('${base}/helper/member/activity/list.jhtml?type=growth','${helperRole.retUrl}');"
                       style="cursor: pointer;float: right;padding-right: 10px;color: #fff;${helperRole.noAuthorityStypeColor}">完成任务，拿积分&gt;</a>
                [/@helperRole]
                </div>
            [/#if]
                <div class="members_main_wrapper_title">
                    <h2>店铺概况</h2>
                </div>
                <ul class="members_pro" id="memberspro">
                    <li style="background-color:#7cbae5;">
                    [@helperRole url='helper/member/trade/list.jhtml']
                        <a style="cursor: pointer; ${helperRole.noAuthorityStypeColor} "
                           onclick="roleRoot('${base}/helper/member/trade/list.jhtml?type=unshipped','${helperRole.retUrl}');"
                        >待发货<span${helperRole.noAuthorityStype}>(${unshippedCount!0})</span></a>
                    [/@helperRole]
                    </li>
                    <li style="background-color:#cec0f4;">
                    [@helperRole url='helper/member/trade/list.jhtml']
                        <a style="cursor: pointer;  ${helperRole.noAuthorityStypeColor} "
                           onclick="roleRoot('${base}/helper/member/trade/list.jhtml?type=unpaid','${helperRole.retUrl}');"
                        >待付款<span${helperRole.noAuthorityStype}>(${unpaidCount!0})</span></a>
                    [/@helperRole]
                    </li>
                    <li style="background-color:#81d2cf;">
                    [@helperRole url='helper/member/trade/list.jhtml']
                        <a style="cursor: pointer; ${helperRole.noAuthorityStypeColor} "
                           onclick="roleRoot('${base}/helper/member/trade/list.jhtml?type=shipped','${helperRole.retUrl}');"
                        >已发货<span${helperRole.noAuthorityStype}>(${shippedCount!0})</span></a>
                    [/@helperRole]
                    </li>
                    <li style="background-color:#92bf77;">
                    [@helperRole url='helper/member/trade/list.jhtml']
                        <a style="cursor: pointer; ${helperRole.noAuthorityStypeColor}"
                           onclick="roleRoot('${base}/helper/member/trade/list.jhtml?type=unreturned','${helperRole.retUrl}');"
                        >退货中<span${helperRole.noAuthorityStype}>(${shippedApplyCount!0})</span></a>
                    [/@helperRole]
                    </li>
                </ul>
                <div class="members_main_wrapper">
                    <div class="members_main_wrapper_title">
                        <h2>我的会员</h2>
                        <div class="title_people">
                            <img src="${base}/resources/helper/img/men_1.png" alt="附近的人">
                        [@helperRole url='helper/member/consumer/nearby.jhtml']
                            <a style="cursor: pointer; ${helperRole.noAuthorityStypeColor}"
                               onclick="roleRoot('${base}/helper/member/consumer/nearby.jhtml','${helperRole.retUrl}');"
                            >附近的人</a>
                        [/@helperRole]
                        </div>
                        <div class="title_people">
                            <img src="${base}/resources/helper/img/men_2.png" alt="关注我的人">
                        [@helperRole url='helper/member/consumer/collect_list.jhtml']
                            <a style="cursor: pointer;${helperRole.noAuthorityStypeColor}"
                               onclick="roleRoot('${base}/helper/member/consumer/collect_list.jhtml','${helperRole.retUrl}');"
                            >关注我的人</a>
                        [/@helperRole]
                        </div>
                    </div>
                    <div class="members_numb">
                        <div class="members_numb_1">
                        [@helperRole url='helper/member/consumer/list.jhtml']
                            <h3><a style="cursor: pointer;"
                                   onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?status=enable','${helperRole.retUrl}');"
                            >${memberCounts!0}</a>
                            </h3>人
                        [/@helperRole]
                        </div>
                        <ul class="members_numb_right">
                            <li>
                                <div class="numb_right-img">
                                    <img src="${base}/resources/helper/img/people_05.png" alt="">
                                </div>
                                <div class="numb_right-item">
                                [@helperRole url='helper/member/consumer/list.jhtml']
                                    <a style="cursor: pointer;${helperRole.noAuthorityStypeColor} "
                                       onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?gender=1&status=enable','${helperRole.retUrl}');"
                                    >
                                        <strong>${womens}%</strong>
                                        女${womencounts!0}人
                                    </a>
                                [/@helperRole]
                                </div>
                            </li>
                            <li>
                                <div class="numb_right-img">
                                    <img src="${base}/resources/helper/img/people_03.png" alt="">
                                </div>
                                <div class="numb_right-item">
                                [@helperRole url='helper/member/consumer/list.jhtml']
                                    <a style="cursor: pointer;${helperRole.noAuthorityStypeColor}"
                                       onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?gender=0&status=enable','${helperRole.retUrl}');"
                                    >
                                        <strong>${mens}%</strong>
                                        男${mencounts!0}人
                                    </a>
                                [/@helperRole]
                                </div>
                            </li>
                        </ul>
                    </div>
                    <ul class="VIP">
                        <li>
                        [@helperRole url='helper/member/consumer/list.jhtml']
                            <a style="cursor: pointer;"
                               onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?memberRank=1&status=enable','${helperRole.retUrl}');"
                               style="cursor:pointer;">普通会员(${VIP0!0})</a>
                        [/@helperRole]
                        </li>
                        <li>
                        [@helperRole url='helper/member/consumer/list.jhtml']
                            <a style="cursor: pointer;"
                               onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?memberRank=2&status=enable','${helperRole.retUrl}');"
                               style="cursor:pointer;">VIP1(${VIP1!0})</a>
                        [/@helperRole]
                        </li>
                        <li>
                        [@helperRole url='helper/member/consumer/list.jhtml']
                            <a style="cursor: pointer;"
                               onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?memberRank=3&status=enable','${helperRole.retUrl}');"
                               style="cursor:pointer;">VIP2(${VIP2!0})</a>
                        [/@helperRole]
                        </li>
                        <li>
                        [@helperRole url='helper/member/consumer/list.jhtml']
                            <a style="cursor: pointer;"
                               onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?memberRank=4&status=enable','${helperRole.retUrl}');"
                               style="cursor:pointer;">VIP3(${VIP3!0})</a>
                        [/@helperRole]
                        </li>
                        <li>
                        [@helperRole url='helper/member/consumer/list.jhtml']
                            <a style="cursor: pointer;"
                               onclick="roleRoot('${base}/helper/member/consumer/list.jhtml?memberRank=5&status=enable','${helperRole.retUrl}');"
                               style="cursor:pointer;">VIP4(${VIP4!0})</a>
                        [/@helperRole]
                        </li>
                    </ul>
                </div>
                <div class="deal_main_wrapper">
                    <div class="members_main_wrapper_title">
                        <h2>交易统计</h2>
                        <div class="title_people">
                            <img src="${base}/resources/helper/img/money_2.png" alt="本月账单统计 ">
                        [@helperRole url='helper/member/deposit/statistics.jhtml']
                            <a style="cursor: pointer;${helperRole.noAuthorityStypeColor} "
                               onclick="roleRoot('${base}/helper/member/deposit/statistics.jhtml','${helperRole.retUrl}');">本月账单统计</a>
                        [/@helperRole]
                        </div>
                        <div class="title_people">
                            <img src="${base}/resources/helper/img/money_1.png" alt="本月账单明细">
                        [@helperRole url='helper/member/deposit/statistics.jhtml']
                            <a style="cursor: pointer;${helperRole.noAuthorityStypeColor}"
                               onclick="roleRoot('${base}/helper/member/deposit/thismonthlist.jhtml','${helperRole.retUrl}');">本月账单明细</a>
                        [/@helperRole]
                        </div>
                    </div>
                    <div class="deal_income">
                        <div class="income_1">
                            <div class="income_1_title">
                                收入
                            </div>
                            <h4>
                                <small>￥</small>
                            [@helperRole url='helper/member/deposit/statistics.jhtml']
                                <a style="cursor: pointer;"
                                   onclick="roleRoot('${base}/helper/member/deposit/statistics.jhtml?type=0','${helperRole.retUrl}');">${income}</a>
                            [/@helperRole]
                            </h4>
                        </div>
                        <div class="income_symbol">-</div>
                        <div class="income_1">
                            <div class="income_1_title">
                                支出
                            </div>
                            <h4>
                                <small>￥</small>
                            [@helperRole url='helper/member/deposit/statistics.jhtml']
                                <a style="cursor: pointer;"
                                   onclick="roleRoot('${base}/helper/member/deposit/statistics.jhtml?type=1','${helperRole.retUrl}');">${outcome}</a>
                            [/@helperRole]
                            </h4>
                        </div>
                        <div class="income_symbol">=</div>
                        <div class="income_1">
                            <div class="income_1_title">
                                盈余
                            </div>
                            <h4>
                                <small>￥</small>
                            [@helperRole url='helper/member/deposit/statistics.jhtml']
                                <a style="cursor: pointer;"
                                   onclick="roleRoot('${base}/helper/member/deposit/statistics.jhtml','${helperRole.retUrl}');">${inorout}</a>
                            [/@helperRole]
                            </h4>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
