<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <title>设置性别</title>


    <style type="text/css">
        body {
            font-family: "Microsoft YaHei";
            background-color: #E9E9E9;
        }
    </style>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page" style="background-color:#E9E9E9;">

        <div class="weui_cells weui_cells_radio">
            <label class="weui_cell weui_check_label" for="x11">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>男</p>
                </div>
                <div class="weui_cell_ft">
                    <input type="radio" class="weui_check" name="radio1"  [#if member.gender=='male']checked[/#if] id="x11" value="0">
                    <span class="weui_icon_checked"></span>
                </div>
            </label>
            <label class="weui_cell weui_check_label" for="x12">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>女</p>
                </div>
                <div class="weui_cell_ft">
                    <input type="radio" name="radio1" class="weui_check" id="x12" [#if member.gender=='female']checked[/#if] value="1">
                    <span class="weui_icon_checked"></span>
                </div>
            </label>
        </div>
        <div style="margin-top:20px;">
            <a href="javascript:;" class="weui_btn weui_btn_primary" style="width:80%;" id="save">保存</a>
        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        $("#save").on("click", function () {
            ajaxPost({
                url: "${base}/app/member/update.jhtml",
                data: {sex: $("input[name='radio1']:checked").val()},
                success: function (data2) {
                    showToast(data2.message);
                    if (data2.data == "success") {
                        location.href = "${base}/wap/member/set_info.jhtml";
                    }
                }
            });
        });
    });
</script>
[#include "/wap/include/footer.ftl" /]
</body>
</html>
