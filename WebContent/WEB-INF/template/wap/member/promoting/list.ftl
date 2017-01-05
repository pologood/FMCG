<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>我的会员</title>
</head>
<body>
<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="weui_cells">
    [#list promotingMembers as promotingMember]
        <div class="weui_cell box_pomoinfo MBPL">
            <div class="weui_cell_hd">
                <a href="javascript:;">
                    <img src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                     data-original="${promotingMember.headImg}" class="lazy">
                </a>
            </div>
            <div class="weui_cell_bd weui_cell_primary">
                <p class="cf">
                    <span>会员名称：${promotingMember.nickName}</span><i class="fr">${promotingMember.modify_date}</i>
                </p>
                <a href="tel:${promotingMember.mobile}">
                    <span>联系电话：<b>${promotingMember.mobile}</b></span>
                </a>
            </div>
        </div>
    [/#list]
    </div>
    [#--[#include "/wap/include/footer.ftl"/]--]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'
        });
    });
</script>
</body>
</html>