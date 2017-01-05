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
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript"  src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
        });
    </script>

</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/user_icon.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的会员</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">尊敬商家用户，此模块能帮你管理我的会员。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/consumer/list.jhtml">我的会员</a></li>
                </ul>

            </div>
            <form id="inputForm" action="edit.jhtml" method="post">
                <input type="hidden" name="id" value="${(consumer)!}"/>
                <table class="input">
                    <tr>
                        <th>
                            会员名称:
                        </th>
                        <td>
                        ${(models.username)!}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            姓名:
                        </th>
                        <td>
                        ${(models.name)!}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            联系电话:
                        </th>
                        <td>
                        ${(models.mobile)!}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            地址:
                        </th>
                        <td>
                        ${(models.address)!}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            等级:
                        </th>
                        <td>
                            <select name="memberRankId">
                            [#list memberRanks as memberRank]
                                <option value="${memberRank.id}"[#if memberRank.id == models.memberRank.id]
                                        selected="selected"[/#if]>
                                ${memberRank.name}
                                </option>
                            [/#list]
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            状态:
                        </th>
                        <td>
                            <select name="status">
                                <option value="none" [#if ("none" == (status)!)] selected[/#if]>待审核</option>
                                <option value="enable" [#if ("enable" == (status)!)] selected[/#if]>已审核</option>
                                <option value="disable" [#if ("disable" == (status)!)] selected[/#if]>已禁用</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <span style="margin-left: 100px;"><h5 style="display: inline-block;">成交订单</h5>：${(orders)!}笔，共${(amount)!}元</span>
                        </td>
                    </tr>
                    [#if trades??&&trades?has_content]
                    <tr>
                        <td colspan="2">
                            <div class="list" style="border-top:0;text-align: center">
                                <table class="list">
                                    <tr>
                                        <th style=" text-align: center; ">商品图片</th>
                                        <th style=" text-align: center; ">单号</th>
                                        <th style="text-align: center; ">价格</th>
                                        <th style=" text-align: center; ">时间</th>
                                        <th style=" text-align: center; ">状态</th>
                                    </tr>
                                [#list trades as trade]
                                    <tr>
                                        <td style=" text-align: center; "><img src="${trade.thumbnail}" style="width: 30px;height: 30px;"></td>
                                        <td style=" text-align: center; ">${trade.sn}</td>
                                        <td style=" text-align: center; ">${(trade.amount)!}</td>
                                        <td style=" text-align: center; ">${trade.create_date}</td>
                                        <td style=" text-align: center; ">${trade.finalOrderStatus.desc}</td>
                                    </tr>
                                [/#list]
                                </table>
                            </div>
                        </td>
                    </tr>
                    [/#if]
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td>
                            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                            <input type="button" class="button" value="${message("admin.common.back")}"
                                   onclick="location.href='list.jhtml?status=enable'"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
