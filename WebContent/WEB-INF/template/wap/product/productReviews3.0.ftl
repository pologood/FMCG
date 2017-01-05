<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
<title>商品详情</title>
<link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
<style type="text/css">
    #wrap_photo ul li{
        /*float: left;*/
        position: absolute;
        top:0;
        left: 0;
        /*width:100%;
        height:100%;*/
    }
</style>
<script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
<script type="text/x-handlebars-template" id="wap-list-item">
    {{#each this}}
    <button class="{{#expression @index '==' '0'}}choosed{{/expression}}  " onclick="getSpec();choosedHighLight();"  id="{{color}}">{{color}}</button>
    {{/each}}
</script>
<script type="text/x-handlebars-template" id="review_tpl">
    {{#each this}}
    <div class="am-g">
        <div class="weui_cells">
            <a class="weui_cell" href="javascript:">
                <div class="weui_cell_hd am-margin-right-xs">
                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                         data-original="{{member.headImg}}" alt="icon"
                         style="width: 3rem;border-radius: 50%;">
                </div>
                <div class="weui_cell_bd weui_cell_primary dark-grey font-default">
                    <p>{{member.displayName}}</p>
                </div>
                <div class="weui_cell_ft font-small">
                    {{formatdate createDate "yyyy-MM-dd"}}
                </div>
            </a>
        </div>
        <div class="weui_cells cl margin-0">
            <div class="weui_cell ">
                <div class="weui_cell_primary">
                    {{#ifCond score 3}}
                        <span class="gd-review-btn font-small_1">
                            好评
                        </span>
                    {{/ifCond}}
                    {{#ifCond score 4}}
                        <span class="gd-review-btn font-small_1">
                            好评
                        </span>
                    {{/ifCond}}
                    {{#ifCond score 5}}
                        <span class="gd-review-btn font-small_1">
                            好评
                        </span>
                    {{/ifCond}}
                    {{#ifCond score 1}}
                        <span class="bd-review-btn font-small_1">
                            差评
                        </span>
                    {{/ifCond}}
                    {{#ifCond score 2}}
                        <span class="bd-review-btn font-small_1">
                            差评
                        </span>
                    {{/ifCond}}
                    <span class="font-small_1">{{content}}</span>
                </div>
            </div>
        </div>
      
        <div class="weui_cells margin-0 cl">
            <div class="weui_cell cl">
                <ul class="am-avg-4" style="width:100%;">
                    {{#each productImages}}
                        <li style="padding:5px 5px 0px 5px;">
                            <div style="width:100%;height:0;padding-bottom:100%;overflow:hidden;border-radius:6px;">
                                <img class="lazy" src="${base}/resources/wap/image/img-pl.png"
                                     data-original="{{thumbnail}}" onclick="look_photo(this);" big-photo="{{source}}">
                            </div>
                        </li>
                    {{/each}}
                </ul>
            </div>
        </div>
        <div class="weui_cells cl margin-0">
            <div class="weui_cell font-small_1 light-gray">
                <div class="weui_cell_bd am-margin-right-xs"><!--span>颜色：浅灰/彩蓝</span--></div>
                <div class="weui_cell_bd am-margin-right-xs"><!--span>型号：XS</span--></div>
                <div class="weui_cell_bd am-margin-right-xs">
                    <span>购买日期：{{formatdate createDate "yyyy-MM-dd"}}</span></div>
            </div>
        </div>
      
    </div>
    {{/each}}
</script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]
<div class="container"></div>
<script type="text/html" id="tpl_wraper">
    <div class="am-g bg-silver">
        [#include "/wap/product/pdetail.ftl"/]
        <div class="am-g">
            <div class="weui_cells">
                <div class="weui_cell">
                    <div class="weui_cell_primary font-default">
                        商品评价
                        <small class="font-small color-orangered">好评度${score}%</small>
                    </div>
                    <div class="font-small_1 weui_cell_hd weui_cell_ft">
                    ${product.scoreCount}人评价
                    </div>
                </div>
            </div>
        </div>
        <div id="review_content">
     
        </div>
    </div>
    [#include "/wap/product/pfoot.ftl"/]
    [#include "/wap/product/colorAndStyle_unit.ftl"/]
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/wap/2.0/js/lib.js"></script>
<script type="text/javascript">
    var is_completed;
    var is_load;
    var pageNumber;
    $(function () {
        init();
        get_review(1);
        scroll(function(){
            if(is_load=="false"&&is_completed=="false"){
                get_review(pageNumber);
            }
        });
        //get the fixed ele and then set empty placeholder height
        fixedEleCopyHandler(".empty-for-fixedtop_tab", ".fixedtop_tab");
        //$(".empty-for-fixedtop_tab").height($(".fixedtop_tab").height());
        $('.lazy').picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'
        });
        //
        //var title_index=$("[data-title-index]").data('title-index');
        productSwipeLAndR($("[data-title-index]").data('title-index'));
        $("#wrap_photo").click(function(){
            if($("#wrap_photo").css("display")!="none"){
                $("#wrap_photo").hide();
            }
        });
        
    });
    function look_photo(obj){
        $("#wrap_photo").show();
        // $("#big_photo").attr("src",$(obj).attr("big-photo"));
        var list_img=[];
        $(obj).parent().parent().siblings().each(function(){
            list_img.push($(this).find("img").attr("big-photo"));
        });
        var html="<ul><li><img src="+$(obj).attr("big-photo")+"></li>";
        for(var ii=0;ii<list_img.length;ii++){
            html+="<li style='display:none;'><img  src="+list_img[ii]+"></li>"
        }
        html+="<ul>";
        $("#wrap_photo").html(html);
        //初始化
        var size = $("#wrap_photo ul li").size();
        var i = 0;
        //核心向左运动函数
        function moveL() {
            i--;
            if (i == -1) {
                i = size - 1;
            }
            //alert(i);
            $("#wrap_photo ul li").eq(i).show().siblings().hide();
        }

        //核心向右运动函数
        function move() {
            i++;
            if (i == size) {
                i = 0;
            }
            //alert(i);
            $("#wrap_photo ul li").eq(i).show().siblings().hide();
        }
        var photo=document.getElementById("wrap_photo");
        var start;//记录x，y的初始点
        var deltaX;//保存X移动距离
        photo.addEventListener("touchstart",function(a){
            start = {
                pageX: a.touches[0].pageX,
                pageY: a.touches[0].pageY
            };//记录初始点
            deltaX = 0;//还原移动距离
        }, false);
         
        photo.addEventListener("touchmove",function(a){
            deltaX = a.touches[0].pageX - start.pageX;//计算出划动距离
        }, false);

        photo.addEventListener("touchend",function(){
            if(deltaX>0){
                // alert("向右划动超过了"+deltaX+"，执行某函数");
                moveL();
            }
            if(deltaX<0){
                // alert("向左划动超过了"+deltaX+"，执行某函数");
                move();
            }
        },false);
    }
    function get_review(num){
        is_completed="false";
        is_load="true";
        pageNumber=num;
        var review_wrap = Handlebars.compile($("#review_tpl").html());//首页标签模板
        ajaxGet({
            url:"${base}/wap/product/get_list.jhtml",
            data:{
                id:${id},
                pageSize:10,
                pageNumber:pageNumber
            },
            success:function(data){
                is_load="false";
                if(pageNumber==1){
                    if(data.data.length<10){
                        is_completed="true";
                    }else if(data.data.length==10){
                        pageNumber++;
                    }
                    $("#review_content").html(review_wrap(data.data));
                }else if(pageNumber>1){
                    if(data.data.length<10){
                        is_completed="true";
                    }else if(data.data.length==10){
                        pageNumber++;
                    }
                    $("#review_content").append(review_wrap(data.data));
                }
                $('.lazy').picLazyLoad({
                    threshold: 100,
                    placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'
                });
            }
        });
    }
    /**自定义if标签*/
    Handlebars.registerHelper('ifCond', function(v1, v2, options) {
        if(v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });
   /**自定义日期转换标签*/
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
            }
        }); 
    });
</script>
<div style="position: fixed;z-index: 10000;top: 0px;background-color: rgba(0,0,0,0.9);width: 100%;height: 100%;display:none;" id="wrap_photo">
    <!-- <img src="" style="" id="big_photo"> -->
</div>
</body>
</html>
