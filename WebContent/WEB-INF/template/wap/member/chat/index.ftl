<!DOCTYPE html>
<html>
<head>
    [#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${guide.displayName}</title>
    <link rel="stylesheet" href="${base}/resources/wap/3.0/js/plugin/simboo/Yeasemob/Yeasemob.css">
</head>
<body class="bg-00">
<!-- Yeasemob-wrap-->
<div class="Yeasemob-RP EMB">
    <!-- 显示区域-->
    <div class="YM-displayR EMB">
    </div>
    <!-- 操作区域-->
    <div class="weui_cell YM-inputsR">
        <div class="weui_cell_hd">
            <i class="iconfont face">&#xe653;</i>
        </div>
        <div class="weui_cell_bd weui_cell_primary">
            <textarea name="" rows="1" class="inputme" jhk="inputtxt"></textarea>
        </div>
        <div class="weui_cell_ft">
            <a href="javascript:;" class="send" jhk="btnsend">发送</a>
        </div>
    </div>
</div>
<!-- HBS:消息模板-->
<script type="text/x-handlebars-template" id="tpl_message">
    {{!direction:消息方向}}
    <div class="message message-{{direction}}">
        <div class="userthumbR">
            <div class="userthumb">
                <img src="{{userthumb}}" alt="">
            </div>
        </div>
        <div class="userinfotxtR">
            <div class="userinfotxt">
                {{userinfotxt}}
            </div>
        </div>
    </div>
</script>
<!-- BOTTOM SCRIPT-->
[#include "/wap/include/resource-3.0-part2.ftl"/]
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/easemob/strophe.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/easemob/websdk-1.1.2.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/easemob/webim.config.js"></script>
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/simboo/Yeasemob/Yeasemob.js"></script>
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //tiaohuo
    //店铺id
    _TH_.tenantid = "${id}";

    _TH_.member = [];
    _TH_.member.push({headImg:'${(member.headImg)!}',mobile:'${(member.mobile)!}'});
    //当前用户信息
    _TH_.user_self={
        user:'${(member.id)!}',
        pwd: "rzico@2015",
        userthumb:'${(member.headImg)!}',
        tenantName:'${(tenant.name)!}'
    };
    //目标用户信息
    _TH_.user_target={
        user:'${(guide.id)!}',
        userthumb:'${(guide.headImg)!}'
    };
</script>
<!-- JS:page-->
<script type="text/javascript">
    $(function() {
        WebIM.Emoji = {
            path: '../../image/faces',/*表情包路径*/
            map: {
                '[):]': 'ee_1.png',
                '[:D]': 'ee_2.png',
                '[;)]': 'ee_3.png',
                '[:-o]': 'ee_4.png',
                '[:p]': 'ee_5.png'
            }
        };
        //实例化
        $(".Yeasemob-RP").yeasemob({
            user_self: _TH_.user_self,
            user_target: _TH_.user_target
        });
    });

</script>
</body>
</html>
