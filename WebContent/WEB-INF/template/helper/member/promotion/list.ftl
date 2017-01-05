<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.consultation.edit")}</title>
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
    <style type="text/css">
        #bdshare_weixin_qrcode_dialog {
            box-sizing: content-box;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
            var $delete = $("#deleteButton");
        [@flash_message /]
            // 删除
            $delete.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "确认是否要删除活动？",
                    onOk: function () {
                        $.ajax({
                            url: "delete.jhtml",
                            type: "POST",
                            data: {id: ${promotionMailModel.id}},
                            dataType: "json",
                            cache: false,
                            success: function (data) {
                                if (data.message.type == "success") {
                                    $("#active").remove();
                                    $("#promotionName").html("暂无活动");
                                    $("#deleteButton").attr("disabled", "disabled");
                                    $("#shareButton").attr("disabled", "disabled");
                                }
                            }
                        });
                    }
                });
                return false;
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
            <table class="input" style="text-align: center;">
                <tr>
                    <td style="padding-top: 30px;">
                        <img src="${base}/resources/helper/images/promotionmail.png"><br>
                        <div id="active" style="padding-top: 10px;"><span>活动正在进行中</span></div>
                        <div style="padding-top: 10px;"><h2 id="promotionName">${promotionMailModel.name}</h2></div>
                        <br>
                    </td>
                </tr>
                <tr>
                    <td>
                        包邮只对自营商品生效，分销商品只能由供应商设置。
                    </td>
                </tr>
                <tr>
                    <td style="padding: 20px 0 20px 0;">
                    [@helperRole url="helper/member/promotion/list.jhtml?type=mail" type="update"]
                        [#if helperRole.retOper!="0"]
                            <input type="button" class="button" value="修改" onclick="location.href='add.jhtml?type=mail';"/>
                        [/#if]
                    [/@helperRole]

                    [@helperRole url="helper/member/promotion/list.jhtml?type=mail" type="del"]
                        [#if helperRole.retOper!="0"]
                            <input id="deleteButton" type="button" class="button" value="删除"/>
                        [/#if]
                    [/@helperRole]

                    [@helperRole url="helper/member/promotion/list.jhtml?type=mail" type="share"]
                        [#if helperRole.retOper!="0"]
                            <input id="shareButton" type="button" class="button" value="分享" onclick="share()"/>
                        [/#if]
                    [/@helperRole]
                    </td>
                </tr>
            </table>
        [#--</form>--]
            <!--share begin -->
            <script>
                var jiathis_config;
                function share() {
                    jiathis_config = {
                        url: "${url}",
                        pic: "${thumbnail}",
                        title: "${title}",
                        summary: "${description}"
                    }
                    $(".jiathis_button_weixin").click();
                    $("#jiathis_weixin_tip a").remove();
                }
            </script>
            <div id="ckepop" style="display: none;">
                <span class="jiathis_txt">分享到：</span>
                <a class="jiathis_button_weixin">微信</a>
                <a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jiathis_separator jtico jtico_jiathis"
                   target="_blank">更多</a>
                <a class="jiathis_counter_style"></a></div>
            <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1" charset="utf-8"></script>
        </div>
        <br/>
        <!--share end -->
    </div>
</div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
