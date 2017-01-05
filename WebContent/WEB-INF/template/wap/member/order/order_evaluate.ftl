<!DOCTYPE html>
<html lang="en">
<head>
    [#include "/wap/include/resource-2.0.ftl"]
    <title>订单评价</title>
    <style type="text/css">
        body{
            font-family:"Microsoft YaHei";
            background-color:white;
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
        /*星星打分*/
        
        #star{overflow:hidden;}
        #star li{float:left; width:20px; height:20px; margin:2px; display:inline; color:#999; font:bold 18px arial; cursor:pointer}
        #star .act{color:#c00}
        #star_word{width:80px; height:30px; line-height:30px; border:1px solid #ccc; margin:10px; text-align:center; display:none}
        /*星星打分*/
    </style>
    
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <script type="text/javascript">
        
    </script> 
    <script  type="text/x-handlebars-template"  id="order_evaluate_tpl">
        {{#list orderItems}}
        <div class="weui_cells cl" style="margin-top:0px;">
        <div class="weui_cell" style="">
            <a  href="${base}/wap/product/content/{{productId}}/product.jhtml">
                <img src={{thumbnail}} alt="icon" style="width:80px;margin-right:5px;display:block">
            </a>
            <div class="weui_cell_bd weui_cell_primary" style="height:80px;">
                <p style="font-size:15px;display: -webkit-box;-webkit-line-clamp:2;-webkit-box-orient: vertical;overflow: hidden;
                    ">{{fullName}}
                </p>
                <p style="font-size:15px;color:#E6C99C;margin-top:2px;">
                    {{#each promotions}}
                    <a href="javascript:;" style="width:150px;height:50px;
                    border:1px solid #E6C99C;border-radius:3px;color:#E6C99C">{{name}}</a>
                    {{/each}}
                </p>
                <p style="font-size:10px;color:#A0A0A0;">订单号：{{../sn}}</p>
            </div>
            <div style="text-align:right;height:80px;padding-top:2px;">
                <p>￥{{price}}</p>
                <p>&nbsp;</p>
                <p>x{{quantity}}</p>
            </div>
        </div>
        </div>

        {{/list}}
        <div style="background-color:#E8EAE9;height:10px;margin-top:0px;margin-bottom:0px;"></div>

        
        <form id="headForm" action="javascript:;" method="post" enctype="multipart/form-data">
            <input type="file" id="headImg" name="file" style="display:none;" accept="image/*" onchange="getFullPath()" capture="camera" multiple>
           
            <div class="weui_cells weui_cells_form" style="margin-top:0px;">
                <div class="weui_cell" >
                    <div class="weui_cell_bd weui_cell_primary" style="border:1px solid #E8EAE9;">
                        <textarea name="content" class="weui_textarea" placeholder="请输入评论" rows="3" id="textarea" maxlength="200"></textarea>
                        <div class="weui_textarea_counter"><span id="text_length">0</span>/200</div>
                    </div>
                </div>
            </div>
        </form>

        <div class="weui_cell cl"style="">
            <div style="display:inline-block;" id="photo_list">
                
            </div>
            <div style="display:inline-block;" >
                <img src="${base}/resources/wap/2.0/images/plus23.png" alt="nopicture" style="width:65px; height:65px;margin-top:5px;" id="addPhotoButton"/>
            </div>
            
        </div>

        <div style="background-color:#E8EAE9;height:10px;margin-top:0px;margin-bottom:0px;"></div>

        <div class="weui_cells" style="margin:0px;">
            <div class="weui_cell" style="height:50px;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>满意度</p>
                </div>
                <div class="weui_cell_ft">
                    <div class="wrapper">
                        <ul id="star">
                            <li id="star_li">★</li>
                            <li id="star_li">★</li>
                            <li id="star_li">★</li>
                            <li id="star_li">★</li>
                            <li id="star_li">★</li>
                        </ul>
                    </div>
                </div>
            </div>
            [#--<div style="padding:0 15px;">--]
                [#--<div style="width:100%;height:1px;border-top:1px solid #E8EAE9;"></div>--]
            [#--</div>--]
            [#--<div class="weui_cell">--]
                [#--<div class="weui_cell_bd weui_cell_primary">--]
                    [#--<p>是否同步到订单秀</p>--]
                [#--</div>--]
                [#--<div class="weui_cell_ft">--]
                    [#--<div id="div1" style="width:50px;height;30px;border-radius:15px;background-color:#E8EAE9;display:none;">--]
                        [#--<div id="open1" style="width:30px;height:30px;border-radius:15px;background-color:white;box-shadow: 0px 2px 4px rgba(0,0,0,0.4);"></div>--]
                    [#--</div>--]
                    [#--<div id="div2" style="width:50px;height;30px;border-radius:15px;background-color:green;">--]
                        [#--<div id="open2" style="width:30px;height:30px;border-radius:15px;background-color:white;margin-left:20px;box-shadow: 0px 2px 4px rgba(0,0,0,0.4);"></div>--]
                        [#--<div style=""></div>--]
                    [#--</div>--]
                [#--</div>--]
            [#--</div>--]
        </div>

        <div style="background-color:#E8EAE9;height:10px;margin-top:0px;margin-bottom:0px;"></div>

        <div style="margin:20px auto;">
            <a href="javascript:;" class="weui_btn weui_btn_warn" style="width:80%;" id="save">确认</a>
        </div>
        
        <div style="background-color:#E8EAE9;height:10px;margin-top:0px;margin-bottom:0px;"></div>
        <div class="empty-for-fixedbottom_tab MR-OREL"></div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container" style="height:100%;">

</div>
<script type="text/html" id="tpl_wraper">
<div class="page" style="background-color:white;height:100%;">
    
</div>
[#include "/wap/include/footer.ftl" /]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    var url_list=[];
    $(function () {
        init();

        compiler = Handlebars.compile($("#order_evaluate_tpl").html());
        
        ajaxGet({
            url:"${base}/app/member/order/view.jhtml?id=${id}",
            success:function(data){
                $(".page").html(compiler(data.data));
                //特定赋高
                fixedEleCopyHandler(".empty-for-fixedbottom_tab.MR-OREL", ".am-topbar-fixed-bottom");
                //检测文本域的字数
                $("#textarea").keyup(function() {
                    $("#text_length").text($("#textarea").val().length);
                });
                /*开关*/
//                $("#div1").on("click",function(){
//                    $("#div2").show();
//                    $("#div1").hide();
//                });
//                $("#div2").on("click",function(){
//                    $("#div1").show();
//                    $("#div2").hide();
//                });
                
                $("#addPhotoButton").on("click",function(){
                    $("#headImg").click();
                    
                });
                
                
                /*星星打分*/
                var star = document.getElementById("star");
                var star_li=$("#star").find("li");
                // var star_li = document.getElementsByTagName("li");//这是所有的li元素，包括头部和尾部的li元素
                var iStar=0;
                var len = star_li.length;
                var iScore=fnPoint(len-1);//减去所有的头部和尾部元素，//默认是4分
                var view_type;
                for(var i=1; i<=len; i++){
                    star_li[i-1].index = i;
                    star_li[i-1].onmouseover = function(){
                        fnPoint(this.index);
                        // star_li[i].className = "act";
                    };
                    star_li[i-1].onclick = function(){
                          iStar=this.index;
                    };
                }
                function fnPoint(iArg){
                    iScore = iArg || iStar;
                    for (var ii = 0; ii < len; ii++) star_li[ii].className = ii < iScore ? "act" : "";
                        return iScore;
                }
               
                //点击添加评论按钮
                $("#save").on("click",function(){
                    if(iScore==1){
                        view_type="negative";
                    }else if(iScore==2||iScore==3){
                        view_type="moderate";
                    }else{
                        view_type="positive";
                    }
                    // $("#trade").val(data.data.id);
                    // $("#flag").val("trade");
                    // $("#type").val(view_type);
                    // $("#assistant").val(iScore);
                    // $("#score").val(iScore);
                   
                    if($("#textarea").val()==""){
                        showDialog2("内容提示","评论内容不能为空") 
                    }else{
                        $.ajax({
                            url:"${base}/wap/member/review/add.jhtml",
                            type:"post",
                            dataType:"json",
                            data:{  
                                tradeId:data.data.id,
                                flag:"trade",
                                type:view_type,
                                assistant:iScore,
                                score:iScore,
                                content:$("#textarea").val(),
                                url_list:url_list
                            },
                            traditional: true,
                            success:function(data2){
                                closeWaitLoadingToast(); 
                                if(data2.message.type=="success"){
                                    showToast(data2.message);
                                    location.href="${base}/wap/member/order/list.jhtml?type=${type}";
                                }else if(data2.message.type=="error"){
                                    showToast2(data2.message);
                                }
                            }
                        });
                    }
                });
            }
        });  
    });
    function getFullPath() {
        var file=document.getElementById('headImg');
        var photo_tpl=document.getElementById('photo_list');
        var fileList = file.files;
        for(var i=0;i<fileList.length;i++){
            photo_tpl.innerHTML+="<div style='float:left;margin-top:5px;margin-left:5px;'><img id='img"+i+"'></div>";
            var img=document.getElementById("img"+i);
            if(file.files && file.files[i]){
                img.style.display = 'block';
                img.style.width = '60px';
                img.style.height = '60px';
                if (window.createObjectURL!=undefined) { // basic 
                    img.src = window.createObjectURL(file.files[i]); 
                } else if (window.URL!=undefined) { // mozilla(firefox) 
                    img.src = window.URL.createObjectURL(file.files[i]); 
                } else if (window.webkitURL!=undefined) { // webkit or chrome 
                    img.src = window.webkitURL.createObjectURL(file.files[i]); 
                } 
            }
            var f_i= new FormData();
            f_i.append("file",fileList[i]);
            $.ajax({
                url:"${base}/app/file/upload_to_temp_image.jhtml",
                type:"post",
                data: f_i,
                async: false ,
                processData: false,
                contentType: false,
                success:function (data){
                    url_list.push(data.data)
                }
            });
        }
    }
               
    Handlebars.registerHelper('formatdate', function(time,fo) {
        var t = new Date(time); 
        var tf = function(i){return (i < 10 ? '0' : '') + i};
        return fo.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){ 
        switch(a){ 
            case 'yyyy': 
            return tf(t.getFullYear()); 
            break; 
            case 'MM': 
            return tf(t.getMonth() + 1); 
            break; 
            case 'dd': 
            return tf(t.getDate()); 
            break; 
            case "HH":
            return tf(t.getHours());
            break;
            case "mm":
            return tf(t.getMinutes());
            break;
            case "ss":
            return tf(t.getSeconds());
            break;
            }
        }); 
    });
    
    Handlebars.registerHelper('list', function(items, options) {
        for(var i=0;i<items.length;i++){
            return options.fn(items[0]) ;
        }
    });

</script>

</body>
</html>
