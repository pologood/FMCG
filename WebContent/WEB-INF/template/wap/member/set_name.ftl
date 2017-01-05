<!DOCTYPE html>
<html lang="en">
<head>
    [#include "/wap/include/resource-2.0.ftl"]
    <title>设置姓名</title>
    <style type="text/css">
        body{
            font-family:Microsoft YaHei;
            background-color:#E9E9E9;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <script  type="text/html"  id="tpl_component">
        <!--BEGIN toast-->
        <div id="toast" style="display: none;">
            <div class="weui_mask_transparent"></div>
            <div style="width:80%;margin:10px;background: rgba(40, 40, 40, 0.75);color: #FFFFFF;text-align:center;top:39%;position:fixed;left:7%;height:50px;;line-height:50px;font-size:15px;border-radius:5px;z-index:10000px;">
                <p class="weui_toast_content" id="message"></p>
            </div>
        </div>
        <!--end toast-->
    </script>
    <script  type="text/x-handlebars-template"  id="set_name_tpl">
    <div class="weui_cells weui_cells_form" style="margin-top:20px;">
        <div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label">姓名</label></div>
            <div class="weui_cell_bd weui_cell_primary">
              <input class="weui_input" type="text" style="font-family:Microsoft YaHei;" name="newName" id="newName" value="{{name}}"/>
            </div>
        </div>
        <div style="margin-top:20px;">
            <a href="javascript:;" class="weui_btn weui_btn_primary" style="width:80%;" id="save">保存</a>
        </div>
    </div>
    </script>

</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
<div class="page" style="background-color:#E9E9E9;">
    
</div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        ajaxGet({
            url:"${base}/app/member/view.jhtml",
            success:function(data){
                var compiler = Handlebars.compile($("#set_name_tpl").html());
                $(".page").html(compiler(data.data));
                
                $("#save").on("click",function(){
                    ajaxPost({
                        url:"${base}/app/member/update.jhtml",
                        data:{name:$("#newName").val()},
                        success:function(data2){
                            if(data2.data=="success"){
                                if($("#newName").val()!=data.data.name){
                                    showToast(data2.message);
                                }
                                location.href="${base}/wap/member/set_info.jhtml";
                            }
                        }
                    });
                });
            }
        });

    });
</script>
[#include "/wap/include/footer.ftl" /]
</body>
</html>
