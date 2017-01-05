<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/cart.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet">
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2b/include/topnav.ftl"]
</div>

<!--主页内容区 -->
<div class="paper">
    <!-- 会员中心头部 -->
[#include "/b2b/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
        [#include "/b2b/include/member_left.ftl"]
            <div class="content">
                <!--消息管理-->
                <form id="listForm" action="list.jhtml" method="get">
                    <div class="message">
                        <ul>
                            <li style="height:45px;">
                                <div style="width:100px;border:1px solid #666666;text-align:center;line-height:39px;float:right;font-size:16px;" onclick="del_all();">一键删除</div>
                            </li>
                            [#if page.content?size lte 0]
                            <li>
                                <div style="text-align:center;">暂无数据</div>
                            </li>
                            [#elseif page.content?size>0]
                                [#list page.content as message]
                                <li>
                                    <div class="dt">
                                        <em>丨</em>
                                        [#switch message.type]
                                            [#case "order"]订单提醒
                                                [#break]
                                            [#case "account"]账单提醒
                                                [#break]
                                            [#case "notice"]系统公告
                                                [#break]
                                            [#case "message"]系统消息
                                                [#break]
                                            [#case "consultation"]咨询回复
                                                [#break]
                                            [#case "contact"]社交圈
                                        [/#switch]
                                        <a href="javascript:del(${message.id});" class="delete" title="删除"></a>
                                    </div>
                                    <div class="dd">
                                        <div class="info">
                                            [#if message.trade??&&message.trade?has_content]
                                            <a href="${base}/b2b/member/order/order_detail.jhtml?id=${message.trade.id}">${message.content}</a>
                                            [#elseif message.deposit??&&message.deposit?has_content]
                                            <a href="${base}/b2b/member/wallet/index.jhtml">${message.content}</a>
                                            [/#if]   
                                        </div>
                                        <div class="info">${message.createDate?string("yyyy-MM-dd HH:mm:ss")}</div>
                                    </div>
                                </li>
                                [/#list]
                            [/#if]
                        </ul>
                    [#include "/b2b/include/pagination.ftl"]
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--可能感兴趣 -->
    <iframe id="interest" name="interest" src="${base}/b2b/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
[#include "/b2b/include/slogen.ftl"]
</div>

<script type="text/javascript">
    var del = function (id) {
        if (window.confirm("确认删除吗？")) {
            $.ajax({
                url: "delete.jhtml",
                type: "post",
                data: {id: id},
                dataType: "json",
                success: function (data) {
                    $.message(data);
                    if (data.type == "success") {
                        window.location.reload(true);
                    }
                }
            });
        }
    }
    function del_all(){
        if (window.confirm("确认要全部删除吗？")) {
            $.ajax({
                url: "delete_all.jhtml",
                type: "post",
                dataType: "json",
                success: function (data) {
                    $.message(data);
                    if (data.type == "success") {
                        window.location.reload(true);
                    }
                }
            });
        }
    }
</script>
<!--底部 -->
[#include "/b2b/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
