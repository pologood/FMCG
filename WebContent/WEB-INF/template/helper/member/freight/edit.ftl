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
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <style type="text/css">
        .brands label {
            width: 150px;
            display: block;
            float: left;
            padding-right: 6px;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
            /*重定向提示信息*/
        [@flash_message /]

            $("#inputForm").validate({
                rules: {
                    firstWeight:{
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    firstPrice:{
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    continueWeight:{
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    },
                    continuePrice:{
                        required: true,
                        min: 0,
                        decimal: {
                            integer: 12,
                            fraction: ${setting.priceScale}
                        }
                    }
                }
            });


            if ("${tenant.freight.freightType}" == "piece") {
                $('form option[value="piece"]').attr('selected', 'selected');
                $("#firstWeight").text("首件(件)");
                $("#firstPrice").text("首件价格(元)");
                $("#continueWeight").text("续件(件)");
                $("#continuePrice").text("续件价格(元)");
            }
            if ("${tenant.freight.freightType}" == "weight") {
                $('form option[value="weight"]').attr('selected', 'selected');
                $("#firstWeight").text("首重(kg)");
                $("#firstPrice").text("首重价格(元)");
                $("#continueWeight").text("续重(kg)");
                $("#continuePrice").text("续重价格(元)");
            }
            $("#freightType").change(function () {
                if ($("form option[value='piece']").attr("selected") == "selected") {
                    $("#firstWeight").text("首件(件)");
                    $("#firstPrice").text("首件价格(元)");
                    $("#continueWeight").text("续件(件)");
                    $("#continuePrice").text("续件价格(元)");
                }
                if ($("form option[value='weight']").attr("selected") == "selected") {
                    $("#firstWeight").text("首重(kg)");
                    $("#firstPrice").text("首重价格(元)");
                    $("#continueWeight").text("续重(kg)");
                    $("#continuePrice").text("续重价格(元)");
                }
            });

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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/upload.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">运费设置</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">设置你的运费。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/freight/edit.jhtml">运费设置</a></li>
                </ul>

            </div>

            <form id="inputForm" action="update.jhtml" method="post">
                <table class="input">
                    <tr>
                        <th>
                            选择配送类型:
                        </th>
                        <td>
					<span class="fieldSet">
					   	<select id="freightType" name="freightType">
							<option value="null">请选择</option>
							<option value="piece">计件</option>
							<option value="weight">计重</option>
		  				</select>
					</span>
                        </td>
                    </tr>
                    <tr>
                        <th id="firstWeight">
                            首重(kg):
                        </th>
                        <td>
                            <input type="text" name="firstWeight" class="text" value="${tenant.freight.firstWeight}"
                                   maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th id="firstPrice">
                            首重价格(元):
                        </th>
                        <td>
                            <input type="text" name="firstPrice" class="text" value="${tenant.freight.firstPrice}"
                                   maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th id="continueWeight">
                            续重(kg):
                        </th>
                        <td>
                            <input type="text" name="continueWeight" class="text"
                                   value="${tenant.freight.continueWeight}" maxlength="200"/>
                        </td>
                    </tr>
                    <tr>
                        <th id="continuePrice">
                            续重价格(元):
                        </th>
                        <td>
                            <input type="text" name="continuePrice" class="text" value="${tenant.freight.continuePrice}"
                                   maxlength="200"/>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td>
                        [@helperRole url="helper/member/freight/edit.jhtml" type="update"]
                            [#if helperRole.retOper!="0"]
                                <input type="submit" class="button" value="保存"/>
                            [/#if]
                        [/@helperRole]
                            &nbsp;
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
