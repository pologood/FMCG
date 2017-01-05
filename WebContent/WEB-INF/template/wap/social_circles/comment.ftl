<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>详情</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        [#list contact.replyContacts as replyContacts]
        <div class="weui_cell" onclick="getContactId('${replyContacts.id}','${replyContacts.member.nickName}');">
            <div class="weui_cell_hd">
                <img src="${replyContacts.member.headImg}" class="showImg">
            </div>
            <div class="weui_cell_primary">
                <p class="font-large color-lired2">${replyContacts.member.nickName}</p>
                <p class="font-small light-gray">{{timeFormatUtil '${replyContacts.createDate?string("yyyy-MM-dd HH:mm:ss")}'}}</p>
            </div>
        </div>
        <div class="weui_cell cl" onclick="getContactId('${replyContacts.id}','${replyContacts.member.nickName}');">
            <div class="weui_cell_hd showImg"></div>
            <div class="weui_cell_primary">
                <span>${replyContacts.content}</span>
            </div>
        </div>
        [/#list]
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="weui_cells am-margin-xs">
        <div class="weui_cells cl am-margin-0 border-bt">
            <div class="weui_cell">
                <div class="weui_cell_hd am-margin-right-xs">
                    <img src="${contact.member.headImg}" class="showImg">
                </div>
                <div class="weui_cell_primary">
                    <p class="font-large color-lired2">${contact.member.nickName}</p>
                    <p class="font-small light-gray" id="timeFormat"></p>
                </div>
                <div class="weui_cell_bd"
                     style="border-left:1px solid #cecece;text-align:center; padding:0 0.2rem;line-height:1.2;">
                    <p class="font-large">${contact.hits}</p>
                    <p class="font-small light-gray">阅读</p>
                </div>
            </div>
            <div class="weui_cell">
            [#--<div id="silder" class="silder" style="position:relative">--]
            [#--<img src="${base}/resources/wap/2.0/images/transparent_800x800.png"--]
            [#--style="width:auto;max-height:800px"--]
            [#--alt="load.."/>--]
            [#--</div>--]
            [#list contact.productImages as image]
                <div class="sj-items-pic weui_cell_primary">
                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                         data-original="${image.large}" alt="">
                </div>
            [/#list]
            </div>
            <div class="weui_cell">
                <div class="weui_cell_bd font-small_1">
                            <span class="am-text-center"
                                  style="background-color:#fc4949;color:#fff;padding:0.1rem 0.2rem;-webkit-border-radius: 0.2rem;-moz-border-radius: 0.2rem;border-radius: 0.2rem;;">TA已入手
                            </span>
                    <span>${contact.content}</span>
                </div>
            </div>
        [#list contact.products as product]
            <div style="margin-left: 10px;margin-right: 10px;margin-bottom: 10px;">
                <a class="weui_cell bg-silver light-grey cl"
                   href="${base}/wap/social_circles/content/preview.jhtml?sn=${product.sn}">
                    <div class="weui_cell_hd">
                        <!-- 商品缩略图 -->
                        <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                             data-original="${product.thumbnail}"
                             class="lazy" alt="icon" style="width:80px;margin-right:5px;display:block">
                    </div>
                    <div class="weui_cell_bd weui_cell_primary">
                        <p style="font-size:15px;">${product.fullName}</p><!-- 商品名称 -->
                        <p style="font-size:15px;color:#e6222f">￥${product.price}</p><!-- 优惠信息展示 -->
                    </div>
                    <div class="weui_cell_ft"><i class="iconfont">&#xe635;</i></div>
                </a>
            </div>
        [/#list]
            <div style="margin-left: 10px;margin-right: 10px;">
                <a class="weui_cell bg-silver light-grey cl" href="${base}/wap/tenant/index/${tenant.id}.jhtml">
                    <div class="weui_cell_hd">
                        <img class="lazy" data-original="${tenant.thumbnail}"
                             src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"
                             style="height: 7.5rem;margin-right:5px;display:block" alt="">
                    </div>
                    <div class="weui_cell_primary " style="height: 7.5rem;">
                        <p class="font-small cl-blue">${tenant.shortName}</p>
                        <p class="font-small">主营：${tenant.tenantCategoryName}</p>
                        <p class="font-small">${tenant.address}</p>
                    </div>
                    <div class="weui_cell_ft"><i class="iconfont">&#xe635;</i></div>
                </a>
            </div>
            <ul class="weui_cell sj-items-title cl">
                <li onclick="[#if member??&&member?has_content]operation('${base}',$(this),'zan','${contact.id}')[#else]showToast2({content:'您还没有绑定'});[/#if]"
                [#if member??&&member?has_content]
                    [#if contact.praises??]
                        [#list contact.praises as praises]
                            [#if '${member.id}' == '${praises.id}' ] style="color:red;" [/#if]
                        [/#list]
                    [/#if]
                [/#if]
                >
                    <i class="iconfont">&#xe614;</i>
                    <span>赞</span>
                </li>
                <li class="soco_share">
                    <i class="iconfont">&#xe643;</i>
                    <span>分享</span>
                </li>
            </ul>
        </div>
    </div>

    <div class="weui_cells am-margin-xs" id="wapListItem">

    </div>
    [#include "/wap/social_circles/footer1.ftl"/]
    [#include "/wap/social_circles/sharing_unit.ftl"/]
</script>
<!-- share_maskbox-->
<div class="ft-bs1 share_maskbox">
    <div class="txttip">
        <img src="${base}/resources/wap/2.0/images/share_maskbox-txttip.png" alt="">
    </div>
    <div class="qrcode-R">
        <div class="hd">
            或者：邀请好友扫二维码
        </div>
        <div class="bd">
            <div class="img-rap">
                <img src="${base}/wap/social_circles/qrcode.jhtml?id=${id}&type=${type}">
            </div>
        </div>
        <div class="ft">
            <a href="javascript:;" class="weui_btn weui_btn_primary iknow">知道了</a>
        </div>
    </div>
</div>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/operation.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
        });
        $('#contactId').val('${id}');
        $('#timeFormat').html(timeFormatUtil('${contact.createDate?string("yyyy-MM-dd HH:mm:ss")}'));
        var compiler = Handlebars.compile($("#wap-list-item").html());
        $("#wapListItem").html(compiler(''));

        var _type = "${type}";
        if (_type == 'fenxiang') {
            $(".share_maskbox").show();
        }
    });
    function getContactId(id, name) {
        $('#contactId').val(id);
        $('#contactValue').attr('placeholder', name);
    }

    $(document).on('click', '.soco_share', function (event) {
        event.preventDefault();
        $(".share_maskbox").show();
    });
    $(".share_maskbox .iknow").click(function (event) {
        $(".share_maskbox").hide();
    });
    $(".share_maskbox div").click(function (event) {
        event.preventDefault();
        event.stopPropagation();
    });
    $(".share_maskbox").click(function () {
        $(this).hide();
    });
</script>
</body>
</html>