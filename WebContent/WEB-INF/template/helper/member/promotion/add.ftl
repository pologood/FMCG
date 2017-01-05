<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>满包邮</title>
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
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
        [@flash_message /]
            // 表单验证
            $inputForm.validate({
                rules: {
                    "minimumPrice": {
                        required: true,
                        number: true,
                        min: 0
                    }
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
        <img class="js-app_logo app-img" src="${base}/resources/helper/images/promotion.png"/>
        <dl class="app-info">
            <dt class="app-title" id="app_name">满包邮</dt>
            <dd class="app-status" id="app_add_status"></dd>
            <dd class="app-intro" id="app_desc">快去设置满包邮，对您的销量有很大的帮助。</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <li><a class="on" hideFocus="" href="javascript:;">满包邮</a></li>
    </ul>
</div>
<form id="inputForm" action="add.jhtml" method="post">
    <input type="hidden" id="type" name="type" value="mail"/>
    <table class="input">
        <tr>
            <th>
                消费满：
            </th>
            <td colspan="3">
                <input type="text" id="minimumPrice" name="minimumPrice" class="text" placeholder="请输入金额"
                       value="[#if promotionMailModel??]${promotionMailModel.minimumPrice}[/#if]"/> 元
            </td>
        </tr>
        <tr>
            <td colspan="4" style="padding:15px 0 30px 20px;">
                <font size="4">满包邮活动规则</font>
                <ul style="list-style-type: disc;padding-left: 15px;">
                    <li>买家消费满足你设置的金额，则订单包邮</li>
                    <li>如果你同时设置了一下活动，买家购买时可以同时享受优惠，包括：店铺优惠券，限时折扣。</li>
                </ul>
                <br>
                包邮只对自营商品生效，分销商品只能由供应商设置。
            </td>
        </tr>
        <tr>
            [#--<th>--]
                [#--&nbsp;--]
            [#--</th>--]
            <td colspan="4" style="padding-left: 165px;">
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
            [#if promotionMailModel??]<input type="button" class="button" value="${message("admin.common.back")}"
                                             onclick="location.href='list.jhtml?type=mail';"/>[/#if]
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
