<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页x[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${base}/resources/helper/font/iconfont.css"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/screen.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">购物屏管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">购物屏详情</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="list.jhtml?type=${type}">购物屏列表</a></li>
                </ul>

            </div>
            <form id="inputForm" action="detail.jhtml" method="post">
                <input type="hidden" name="id" value="${equipment.id}"/>
                <table class="input">
                    <tr>
                        <th>
                            累计佣金:
                        </th>
                        <td>
                        ${equipment.total_amount}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            累计销售额:
                        </th>
                        <td>
                        ${equipment.total_sale_amount}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            状态:
                        </th>
                        <td>
                        [#if equipment.setStatus=="success"]
                            已开启
                        [#else]
                            未开启
                        [/#if]
                        </td>
                    </tr>
                    <tr>
                        <th>
                            店铺名称:
                        </th>
                        <td>
                        ${equipment.name}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            标签:
                        </th>
                        <td>
                        [#list equipment.tags as tag]
                        ${tag.name}[#if tag_has_next],[/#if]
                        [/#list]
                        </td>
                    </tr>
                    <tr>
                        <th>
                            详细地址:
                        </th>
                        <td>
                        ${equipment.address}
                        </td>
                    </tr>
                    <tr>
                        <th style="color: blue;">员工角色</th>
                        <td></td>
                    </tr>
                [#list equipment.employees as employee]
                    <tr>
                        <th>${employee.nickName}</th>
                        <td>
                            [#list employee.role?split(",") as role]
                                [#if role=="owner"]店主[#if role_has_next],[/#if][/#if]
                                [#if role=="manager"]店长[#if role_has_next],[/#if][/#if]
                                [#if role=="guide"]导购[#if role_has_next],[/#if][/#if]
                                [#if role=="account"]财务[#if role_has_next],[/#if][/#if]
                                [#if role=="cashier"]收银[#if role_has_next],[/#if][/#if]
                            [/#list]
                        </td>
                    </tr>
                [/#list]
                </table>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
