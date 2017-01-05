<!DOCTYPE html>
<html lang="en">
<head>
    [#include "/wap/include/resource-2.0.ftl"]
    <title>实名认证</title>
    
    <style type="text/css">
        body{
            font-family:"Microsoft YaHei";
            background-color:#E9E9E9;
        }
        .weui_cells:before{
            border-top:0px; 
            left:0px;
        }
        .weui_cells:after{
            border-bottom:0px; 
        }
        .weui_cell:before{
            border-top:0px;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll.js"></script>

    <script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>

    <script type="text/javascript">
         
	</script>

  <script  type="text/x-handlebars-template"  id="set_payment_tpl">
   <form id="headForm" action="${base}/wap/member/apply.jhtml" method="post" enctype="multipart/form-data">
    <div class="weui_cells weui_cells_form" style="font-size: 16px;">
        <div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label" style="width:5em;">真实姓名</label></div>
            <div class="weui_cell_bd weui_cell_primary">
              <input class="weui_input" type="input" placeholder="请输入真实姓名" style="font-family:'Microsoft YaHei';" id="true_name" name="name">
            </div>
        </div>
        <div style="width:100%;padding:0 15px;">
            <div style="width:100%;border-top:1px solid #E9E9E9;"></div>
        </div>
        <div class="weui_cell">
            <div class="weui_cell_hd"><label class="weui_label" style="width:5em;">身份证号</label></div>
            <div class="weui_cell_bd weui_cell_primary">
              <input class="weui_input" type="tel" placeholder="请输入15或18位身份证号" style="font-family:'Microsoft YaHei';" id="identification" name="idCard">
            </div>
        </div>
        <div style="width:100%;height:24px;background-color:#E9E9E9;line-height:1.5">
            <span style="margin-left:15px;color:#AAAAAA;font-size:12px">点击图片上传身份证正面照片<span>
        </div>
        <div class="weui_cell">
            <div class="weui_cell_hd">
                 <img src="${base}/resources/wap/2.0/images/example-idcard-front.png" alt="nopicture" style="width:26.25vw;height:auto" id="img1">
            </div>
            <div class="weui_cell_bd weui_cell_primary" style="font-size:12px;color:#aaa;line-height:1.5">
                <p style="margin-left:10px;">1.要求全部信息无遮挡</p>
                <p style="margin-left:10px;">2.证件上文字清晰可识别</p>
            </div>
        </div>
        <div style="width:100%;height:24px;background-color:#E9E9E9;line-height:1.5">
            <span style="margin-left:15px;color:#AAAAAA;font-size:12px">点击图片上传身份证反面照片<span>
        </div>
        <div class="weui_cell">
            <div class="weui_cell_hd">
                <img src="${base}/resources/wap/2.0/images/example-idcard-back.png" alt="nopicture" style="width:26.25vw;height:auto" id="img2">
            </div>
            <div class="weui_cell_bd weui_cell_primary" style="font-size:12px;color:#aaa;line-height:1.5">
                <p style="margin-left:10px;">1.要求全部信息无遮挡</p>
                <p style="margin-left:10px;">2.证件上文字清晰可识别</p>
            </div>
        </div>
    </div>
    <div class="weui_cells weui_cells_form" style="padding-top:20px;padding-bottom:20px;background-color: #E9E9E9;">
        <a href="javascript:;" class="weui_btn weui_btn_primary" style="width:90%;" id="submit_code">提交</a>
    </div>
   
    <input type="file" id="headImg" name="pathFront" style="display:none;" accept="image/*" capture="camera" onchange="getFullPath()">
    <input type="file" id="headImg2" name="pathBack" style="display:none;" accept="image/*" capture="camera" onchange="getFullPath2()">
    </form>
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
    var pathFront,pathBack,flag;
    $(function () {
        init();
        ajaxGet({
            url:"${base}/app/member/view.jhtml",
            success:function(data){
                compiler = Handlebars.compile($("#set_payment_tpl").html());
                $(".page").html(compiler(data));
                $("#img1").on("click",function(){
                    $("#headImg").click();
                });
                $("#img2").on("click",function(){
                    $("#headImg2").click();
                });
                
                $("#submit_code").on("click",function(){
                    var object=$("#identification").val();
                    go(object);
                    if(flag){
                        submitForm($("#headForm"));
                    }
                    
                    // if(go(object)==true){
                    //     ajaxPost({
                    //         url:"${base}/app/member/idcard/save.jhtml",
                    //         data:{
                    //             pathFront:pathFront,
                    //             pathBack:pathBack,
                    //             name:$("#true_name").val(),
                    //             idCard:$("#identification").val()
                    //         },
                    //         success:function(data){
                    //             showToast2(data.message);
                    //             if(data.message.type=="success"){
                    //                 location.href="${base}/wap/member/set_info.jhtml";
                    //             }
                    //         }
                    //     });   
                    // };
                });
            }
        }); 	
    });
    //身份证号验证
    function go(obj){
        var reg=/^[1-9]{1}[0-9]{14}$|^[1-9]{1}[0-9]{16}([0-9]|[xX])$/;
        
        if(obj==""){
            var message={content:"身份证号不能为空"};
            showToast2(message);
            flag=false;
        }
        if(!reg.test(obj)){
            var message={content:"身份证号格式不对"};
            showToast2(message);
            flag=false;
        }else{
           flag=true;
        }
    }
    //图片预览
    function getFullPath() {
        var file=document.getElementById('headImg');
        var fileList = file.files;
        var img=document.getElementById("img1");
        if(file.files[0]){
            img.style.display = 'block';
            img.style.width = '100px';
            img.style.height = '60px';
            if (window.createObjectURL!=undefined) { // basic 
                img.src = window.createObjectURL(file.files[0]); 
            } else if (window.URL!=undefined) { // mozilla(firefox) 
                img.src = window.URL.createObjectURL(file.files[0]); 
            } else if (window.webkitURL!=undefined) { // webkit or chrome 
                img.src = window.webkitURL.createObjectURL(file.files[0]); 
            } 
        }   
        pathFront=img.src;
    }
    function getFullPath2() {
        var file=document.getElementById('headImg2');
        var fileList = file.files;
        var img=document.getElementById("img2");
        if(file.files[0]){
            img.style.display = 'block';
            img.style.width = '100px';
            img.style.height = '60px';
            if (window.createObjectURL!=undefined) { // basic 
                img.src = window.createObjectURL(file.files[0]); 
            } else if (window.URL!=undefined) { // mozilla(firefox) 
                img.src = window.URL.createObjectURL(file.files[0]); 
            } else if (window.webkitURL!=undefined) { // webkit or chrome 
                img.src = window.webkitURL.createObjectURL(file.files[0]); 
            } 
        } 
       pathBack=img.src;
    }
</script>
[#include "/wap/include/footer.ftl" /]
</body>
</html>
