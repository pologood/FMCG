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
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>

    <style type="text/css">
        .dialogwarnIconsss {
            line-height: 24px;
            padding-left: 30px;
        }

        .ulll{
            display: inline-block;
        }
        .ulll li{
            float: left;width: 94%;padding-left: 6%;position: relative;line-height: 24px;margin: 10px 0;
        }
        .ulll li input{
            position: absolute;top: 3px;left: 0;
        }
        .ulll li .li{
            width: 25%;float: left;
        }
        .ulll li .yichu{
            overflow: hidden;white-space: nowrap;text-overflow: ellipsis;
        }
        .beizhu {
            padding-left: 15px;padding-top: 25px;display: block;color: #999;
        }
        .ta_c {
            text-align: center;
        }
    </style>

    <script type="text/javascript">
        $().ready(function () {
            $("#exchange").click(function(){
                $.dialog({
                    title:"积分兑换",
                    content: '<div class="dialogwarnIconsss">' +
                    '<span style="display: block;width: 100%;text-align: left;color: #333;">您当前积分为${(point)!}分，可以兑换以下服务： </span> ' +
                    '<ul class="ulll"> ' +
                    '<li> ' +
                    '<input value="智能路由器" data-point="2000" type="radio" name="cc" [#if point<2000]disabled[/#if]> ' +
                    '<h6 class="li black yichu" title="智能路由器">智能路由器路</h6> ' +
                    '<span class="li black ta_c" >2000分/个</span> ' +
                    '<span class="li yichu" style="color: #999;">每个账号仅限一次</span> ' +
                    '<span class="li ta_c [#if point>2000]green[#else ]red[/#if]" >[#if point>2000]满足[#else ]不足[/#if]</span> ' +
                    '</li> ' +
                    '<li> ' +
                    '<input value="微信首页广告位" data-point="1000" type="radio" name="cc" [#if point<1000]disabled[/#if]> ' +
                    '<h6 class="li black yichu" title="微信首页广告位">微信首页广告位</h6> ' +
                    '<span class="li black ta_c">1000分/个/天</span> ' +
                    '<span class="li yichu" style="color: #999;">每月仅限一次</span> ' +
                    '<span class="li ta_c  [#if point>1000]green[#else ]red[/#if]">[#if point>1000]满足[#else ]不足[/#if]</span>' +
                    '</li>' +
                    '<li> ' +
                    '<input value="活动商品首页推荐" data-point="500" type="radio" name="cc" [#if point<500]disabled[/#if]> ' +
                    '<h6 class="li black yichu" title="活动商品首页推荐">活动商品首页推荐</h6> ' +
                    '<span class="li black ta_c">500分/款/天</span> ' +
                    '<span class="li yichu" style="color: #999;">每月仅限四次</span> ' +
                    '<span class="li ta_c [#if point>500]green[#else ]red[/#if]">[#if point>500]满足[#else ]不足[/#if]</span>' +
                    '</li>' +
                    '</ul>' +
                    '<span class="beizhu">备注：${setting.siteName}保留最终解释权</span></div>',
                    width:540,
                    ok: [#if point>500]"${message("b2b.dialog.ok")}"[#else ]null[/#if],
                    cancel: "${message("b2b.dialog.cancel")}",
                    onOk: function() {
                        var _checked = $("input[name='cc']:checked").val();
                        if(_checked==undefined){
                            $.message("warn","请选择你需要兑换的物品！");
                            return false;
                        }
                        var _dataPoint=  $("input[name='cc']:checked").attr("data-point");
                        var _tenantPoint = ${(point)!}-_dataPoint;
                        $.ajax({
                            url: "${base}/helper/member/activity/exchange.jhtml",
                            type: "POST",
                            data: {
                                id:${member.tenant.id} ,
                                description:_checked,
                                point:_dataPoint,
                                tenantPoint:_tenantPoint
                            },
                            dataType: "json",
                            cache: false,
                            success: function(data) {
                                $.message(data.type,data.content);
                            }
                        });
                    }
                });
            });
        });

        var openDialog=function(content){
            $.dialog({
                width:300,
                title:'提示',
                content:'<div style="height: 65px;font-size: 14px;padding: 0 10px;">'+content+'</div>',
            });
        }
    </script>

</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper" style="padding: 0 10px;">

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav" style="padding: 0;">

                <div class="members_main_wrapper_title"
                     style="background-color: #eee;font-size: 14px;color: #333;margin-bottom: 20px;margin-top: 10px;padding: 0 10px;">
                    <span style="display: inline-block;font-weight: 600;font-size: 16px;line-height: 60px;">积分：${(point)!}</span>
                    <a id="exchange" style="cursor: pointer;float: right;color: #fff;display: block;background-color: #eb3341;padding: 5px 12px;margin-top: 15px;border-radius: 5px;">兑换</a>
                </div>

                <div class="js-app-header title-wrap" id="app_0000000844" style="">
                    <dl class="app-info">
                        <dd class="app-intro" id="app_desc" style="color: #00A7EE">活动积分奖励解释权最终归${setting.siteName}所有</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus" style="padding-top: 0;">
                    <li><a class='[#if type=="growth"]on[/#if]' hideFocus=""
                           href="${base}/helper/member/activity/list.jhtml?type=growth">成长任务</a>
                    </li>
                    <li><a class='[#if type=="daily"]on[/#if]' hideFocus=""
                           href="${base}/helper/member/activity/list.jhtml?type=daily">每日任务</a>
                    </li>
                    <li><a class='[#if type=="activity"]on[/#if]' hideFocus=""
                           href="${base}/helper/member/activity/list.jhtml?type=activity">活动任务</a>
                    </li>
                </ul>
            </div>
            <form id="listForm" action="list.jhtml" method="get" style="
">
                <input type="hidden" id="type" name="type" value="growth">
                <input type="hidden" id="status" name="status" value="" >
                <div class="list">
                    <ul>
                        <li style="margin-bottom: 10px;float: left;width: 979px;">

                            [#assign groups=[{'id':1}+{"name":'完善店铺资料'}]
                            +[{'id':11}+{'name':'完善个人信息'}]
                            +[{'id':15}+{'name':'发布商品'}]
                            +[{'id':22}+{'name':'营销工具'}]
                            +[{'id':29}+{'name':'订单处理'}]
                            +[{'id':34}+{'name':'异业联盟'}]
                            +[{'id':38}+{'name':'钱包帐号'}]
                            +[{'id':41}+{'name':'店铺推广'}]]

                            [#list page as activityRules]
                                [#list groups as group]
                                [#if activityRules.id==group.id]
                                    <div style="background-color: #eee;color: #333;padding: 10px;position: relative;">
                                        <h2 style="font-size: 14px;color: #333;line-height: 26px;">${group.name}</h2>
                                    [#--<i style="position: absolute;right: 10px;top: 0px;font-style: normal;font-size: 20px;line-height: 45px;">∧∨</i>  display:[#if type=='activity']none[#else] block [/#if]--]
                                    </div>
                                [/#if]
                                [/#list]
                                <div style="padding: 10px;line-height: 24px;background-color: #f9f9f9;width: 959px;display: inline-block;font-size: 13px;">
                                    <dl style="">
                                        <dt style="width: 20%;float: left;font-weight: 600;color: #333;">${activityRules_index+1}、${(activityRules.title)!}
                                        </dt>
                                        <dd style="width: 50%;float: left;">${(activityRules.description)!}
                                        </dd>
                                        <dd style="width: 20%;float: left;color: blue;">奖励：[#if type=='activity']${(activityRules.amount)!}元[#else] ${(activityRules.point)!}分 [/#if]
                                        </dd>
                                        [@compress]
                                        <dd style="width: 10%;float: left;color: [#if activityRules.isActivity==true]green[#else ]red[/#if];">
                                            进度：
                                            [#if activityRules.isActivity==true]
                                                已完成
                                            [#else]
                                                [#if activityRules.url?has_content]
                                                    <a href="${base}/${activityRules.url}" style="text-decoration: underline;color: red">未完成</a>
                                                [#else]
                                                    [#assign personal=11..13][#--个人信息--]
                                                    [#assign leagues=34..36][#--异界联盟--]
                                                    [#assign extend=[41,42,43,44,45,46,51,52,54]][#--店铺推广--]
                                                    [#if personal?seq_contains(activityRules.id)]
                                                        <a href="javascript:openDialog('前往${setting.siteName}商家版APP或关注${setting.siteName}公众号完成。');" style="text-decoration: underline;color: red">未完成</a>
                                                    [#elseif leagues?seq_contains(activityRules.id)]
                                                        <a href="javascript:openDialog('前往${setting.siteName}商家版APP完成。');" style="text-decoration: underline;color: red">未完成</a>
                                                    [#elseif extend?seq_contains(activityRules.id)]
                                                        <a href="javascript:openDialog('前往${setting.siteName}商家版APP完成。');" style="text-decoration: underline;color: red">未完成</a>
                                                    [#else]
                                                        未完成
                                                    [/#if]
                                                [/#if]
                                            [/#if]
                                        </dd>
                                        [/@compress]
                                    </dl>
                                </div>
                            [/#list]
                            [#--<dl>--]
                            [#--<dt style="width: 10%;float: left;font-weight: 600;color: #333;">2.店铺地址--]
                            [#--</dt>--]
                            [#--<dd style="width: 70%;float: left;">成功提交店铺地址，即完成任务--]
                            [#--</dd>--]
                            [#--<dd style="width: 10%;float: left;color: blue;">奖励：+10--]
                            [#--</dd>--]
                            [#--<dd style="width: 10%;float: left;color: green;">进度：已完成--]
                            [#--</dd>--]
                            [#--</dl>--]
                        </li>
                    [#--<li style="margin-bottom: 10px;float: left;width: 979px; display:[#if type=='activity']block[#else]none[/#if]">--]
                    [#--<div style="--]
                    [#--background-color: #eee;--]
                    [#--line-height: 24px;--]
                    [#--color: #333;--]
                    [#--padding: 10px;position: relative;">--]
                    [#--<span style="--]
                    [#--display: inline-block;--]
                    [#--color: #333;--]
                    [#--font-size: 14px;--]
                    [#--font-weight: 600;--]
                    [#--">wsufhsjdhfsjjf</span>--]
                    [#--<span style="--]
                    [#--display: inline-block;--]
                    [#--color: #666;--]
                    [#--font-size: 14px;--]
                    [#--">sdfsdfsdfsdfsdfsd</span>--]
                    [#--<h4 style="--]
                    [#--font-size: 14px;--]
                    [#--font-weight: 400;--]
                    [#--color: #eb3341;--]
                    [#--">ssfsdffffsdfsfsfsdf.</h4>--]
                    [#--</div>--]
                    [#--<div style="--]
                    [#--padding: 10px;--]
                    [#--line-height: 24px;--]
                    [#--background-color: #f9f9f9;--]
                    [#--width: 959px;--]
                    [#--display: inline-block;--]
                    [#--font-size: 13px;--]
                    [#--">--]
                    [#--<dl>--]
                    [#--<dt style="width: 10%;float: left;font-weight: 600;color: #333;">1.店铺名称--]
                    [#--</dt>--]
                    [#--<dd style="width: 70%;float: left;">注册账号，并成功设置店铺名称，即完成任务--]
                    [#--</dd>--]
                    [#--<dd style="width: 10%;float: left;color: blue;">奖励：+10--]
                    [#--</dd>--]
                    [#--<dd style="width: 10%;float: left;color: green;">进度：已完成--]
                    [#--</dd>--]
                    [#--</dl>--]
                    [#--<dl>--]
                    [#--<dt style="width: 10%;float: left;font-weight: 600;color: #333;">2.店铺地址--]
                    [#--</dt>--]
                    [#--<dd style="width: 70%;float: left;">开通时间 2016 07 1--]
                    [#--</dd>--]
                    [#--<dd style="width: 10%;float: left;color: blue;">奖励：+40元--]
                    [#--</dd>--]
                    [#--<dd style="width: 10%;float: left;color: green;">进度：已到账--]
                    [#--</dd>--]
                    [#--</dl>--]
                    [#--</div>--]
                    [#--</li>--]
                    </ul>
                </div>
            </form>
        </div>
    </div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
