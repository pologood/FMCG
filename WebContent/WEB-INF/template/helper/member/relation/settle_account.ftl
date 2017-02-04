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
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <style type="text/css">
        #bdshare_weixin_qrcode_dialog {
            box-sizing: content-box;
        }
        #listTable th,#listTable td{
            /*padding-left: 5px;*/
            text-align: center;
        }

    </style>
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/discount.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">往来结算</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">管理我的往来结算。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/relation/settle_account.jhtml">往来结算</a></li>
                </ul>

            </div>
            <form id="listForm" action="settle_account.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <input type="hidden" id="status" name="status" value="${status}"/>
                <div class="bar">
                    <div class="buttonWrap">
                        <!-- <a href="${base}/helper/member/discount/listproduct.jhtml" class="iconButton">
                            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                        </a>
                        <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                            <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                        </a> -->
                        <a href="${base}/helper/member/relation/settle_account.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="pageSizeOption">
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="menuWrap" id="settleAccountSelect">
                            <a href="javascript:;"  class="button" >
                                往来客户<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu" id="seller_pop" style="display:none;margin-left:147px;margin-top:25px;">
                                <ul>
                                    [#list tenants as tenants]
                                    <li>
                                        <a href="settle_account.jhtml?sellerId=${tenants.tenant.id}" val="${tenants.tenant.name}">${tenants.tenant.name}</a>
                                    </li>
                                    [/#list]
                                </ul>
                            </div>
                        </div>
                        <script type="text/javascript">
                            $(function(){
                                $("#settleAccountSelect").mouseover(function(){
                                    $("#seller_pop").show();
                                });

                                $("#settleAccountSelect").mouseout(function(){
                                    $("#seller_pop").hide();
                                });
                                // $("#select").onmouseout(function(){
                                //     $("#seller_pop").hide()
                                // });
                            });
                        </script>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th>时间</th>
                            <th>单据号</th>
                            <th>业务类型</th>
                            <th>往来客户</th>
                            <th>支付方式</th>
                            <th>收入</th>
                            <th>
                                <span>查看明细</span>
                            </th>
                        </tr>
                        [#list page.content as page]
                        <tr>
                            <td>
                                <span>${page.create_date}</span>
                            </td>
                            <td>
                                <span>${page.sn}</span>
                            </td>
                            <td>
                                <span>货款</span>
                            </td>
                            <td>
                                <span>${page.tenant.name}</span>
                            </td>
                            <td>
                                <span>平台支付</span>
                            </td>
                            <td>
                                <span>${page.amount}</span>
                            </td>
                            <td>
                                <a href="javascript:;" >查看明细</a>
                            </td>
                        </tr>
                        [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("helper.member.noResult")}</p>
                [/#if]
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/helper/include/pagination.ftl"]
                [/@pagination]
                </div>
            </form>

            <!--share begin -->
            <script>
                var jiathis_config;
                function share(id, thumbnail, description) {
                    jiathis_config = {
                        url: "${url}".toString().replace("ID", id),
                        pic: thumbnail,
                        title: "${title}",
                        summary: description
                    }
                    $(".jiathis_button_weixin").click();
                    $("#jiathis_weixin_tip a").remove();
                }
                // function getSeller(obj){
                //     if($(obj).next().css("display")=="none"){
                //         $(obj).next().css("display","block").css("margin-left","147px").css("margin-top","25px");
                //     }else{
                //         $(obj).next().css("display","none");
                //     }
                // }
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
