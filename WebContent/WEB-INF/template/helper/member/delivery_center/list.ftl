<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>我的网店</title>
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
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            var $popu = $("#listTable a.popu");
            // 推广链接
            $popu.click(function () {
                var productId = $(this).attr("val");
                $.dialog({
                    title: "获取推广链接",
                [@compress single_line = true]
                    content: '<div><table style="height: 141px;width:480px;"><tr><th>选择推广的类型<\/th><td><select id="urlType" name="urlType">' +
                        [#list urlTypes as urlType]
                        '<option value="${urlType}">${message("Ad.UrlType."+urlType)}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>链接地址<\/th><td><input type="text" id="url_result" class="text" style="width:250px"\/><\/td><\/tr><tr><td><\/td><td><a href="javascript:;" id="getUrl" class="button">获取<\/a><\/td><\/tr><\/table><\/div>',
                [/@compress]
                    width: 480,
                    modal: true,
                    cancel: "${message("admin.dialog.cancel")}",
                    onShow: function () {
                        $("#getUrl").click(function () {
                            $.ajax({
                                url: "${base}/ajax/common/getUrl.jhtml",
                                data: {
                                    urlType: $("#urlType").val(),
                                    id: productId,
                                    businessType: 'delivery'
                                },
                                dataType: "json",
                                type: "get",
                                success: function (message) {
                                    if (message.type == 'success') {
                                        $("#url_result").val(message.content);
                                    } else {
                                        $.message("获取失败！");
                                    }
                                }
                            });
                        });
                    }
                });
            });
            var _ticket = '${ticket}';
            $("#buttonQrcode").click(function () {

                if (_ticket == "") {
                    $.message("error", "对不起，您还没有绑定二维码，请联系管理员！！！");
                    return;
                }

                $.dialog({
                    title: "店铺二维码",
                [@compress single_line = true]
                    content: '<div style="text-align: center;">' +
                    '<img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket}" alt="">' +
                    '</br>' +
                    '<span style="font-size: 14px;color: orangered">单击右键选择【图片另存为】可保存二维码到本地</span>' +
                    '</br>' +
                    '<span style="font-size: 14px;color: orangered">注：您需要在您保存的文件后面加上【.png】做为文件后缀名</span>' +
                    '<\/div>',
                [/@compress]
                    width: 480,
                    modal: true,
                    cancel: null,
                    ok: null,
                    autoOpen: false,
                    show: {
                        effect: "blind",
                        duration: 1000
                    },
                    hide: {
                        effect: "explode",
                        duration: 1000
                    },
                    onOk: function () {
                        var x = $("#x").val();
                        var y = $("#y").val();
                        var w = $("#w").val();
                        var h = $("#h").val();
                        if (w == 0 || h == 0) {
                            alert("您还没有选择图片的剪切区域,不能进行剪切图片!");
                            return;
                        }
                        //alert("你要剪切图片的X坐标: "+x + ",Y坐标: " + y + ",剪切图片的宽度: " + w + ",高度：" + h );
                        if (confirm("确定按照当前大小剪切图片吗")) {
                            //document.form1.submit();
                            //$("#makeHeadImgDiv").toggle();
                            function loadHeadUrl(data) {
                                //alert(data.headUrl);
                                alert(data.retMsg);
                                $("#img_headUrl").attr("src", data.headUrl);
                                $("#headUrl").val(data.headUrl);
                                //alert($("#headUrl").val());
                            }

                            var url = "${ctx}/admin/pri/guest/guest_cutHeadImg.action";
                            var options = {
                                //beforeSubmit: validate,
                                url: url,
                                success: loadHeadUrl,
                                type: 'post',
                                dataType: 'json'
                            };
                            $('#cutHeadImgFrm').ajaxSubmit(options);
                        }
                    }
                });
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/address-manage4.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">门店管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/delivery_center/list.jhtml">门店管理</a></li>
                </ul>

            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <div class="bar">
                [@helperRole url="helper/member/delivery_center/list.jhtml" type="add"]
                    [#if helperRole.retOper!="0"]
                        <a href="add.jhtml" class="iconButton">
                            <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                        </a>
                    [/#if]
                [/@helperRole]
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/delivery_center/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]

                        <a href="javascript:;" id="refreshButton" class="iconButton">
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
                    </div>

                [#if versionType==0]
                    <a href="javascript:;" class="button" id="buttonQrcode">
                    ${message("admin.common.qrcode")}
                    </a>
                [/#if]
                    <div id="ckepop" style="display: none;">
                        <span class="jiathis_txt">分享到：</span>
                        <a class="jiathis_button_weixin">微信</a>
                        <a href="http://www.jiathis.com/share"
                           class="jiathis jiathis_txt jiathis_separator jtico jtico_jiathis"
                           target="_blank">更多</a>
                        <a class="jiathis_counter_style"></a></div>
                    <div class="menuWrap">
                        <div class="search">
                            <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}"
                                   maxlength="200" placeholder="搜索地区名称、地址"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table id="listTable" class="list">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll"/>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="name">${message("DeliveryCenter.name")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="areaName">${message("DeliveryCenter.areaName")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="address">${message("DeliveryCenter.address")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="contact">${message("DeliveryCenter.contact")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort" name="phone">${message("DeliveryCenter.phone")}</a>
                            </th>
                            <th>
                                <a href="javascript:;" class="sort"
                                   name="isDefault">${message("DeliveryCenter.isDefault")}</a>
                            </th>
                            <th>
                                <span>${message("admin.common.handle")}</span>
                            </th>
                        </tr>
                    [#list page.content as deliveryCenter]
                        <tr>
                            <td class="check">
                                <input type="checkbox" name="ids" value="${deliveryCenter.id}"/>
                            </td>
                            <td>
                            ${deliveryCenter.name}
                            </td>
                            <td>
                            ${deliveryCenter.areaName}
                            </td>
                            <td>
                                <span title="${deliveryCenter.address}">${abbreviate(deliveryCenter.address, 26, "...")}</span>
                            </td>
                            <td>
                            ${abbreviate(deliveryCenter.contact,26,"..")}
                            </td>
                            <td>
                            ${deliveryCenter.mobile}
                            </td>
                            <td>
                                <span class="${deliveryCenter.isDefault?string("true", "false")}Icon">&nbsp;</span>
                            </td>
                            <td>
                                [@helperRole url="helper/member/delivery_center/list.jhtml" type="update"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="edit.jhtml?id=${deliveryCenter.id}">[${message("admin.common.edit")}]</a>
                                    [/#if]
                                [/@helperRole]
                                <!--<a href="javascript:;" class="popu" val="${deliveryCenter.id}">[链接]</a>-->
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p>${message("helper.member.noResult")}</p>
                [/#if]
                [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                    [#include "/helper/include/pagination.ftl"]
                [/@pagination]
                </div>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
