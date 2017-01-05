<!--<script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>-->
<!--<script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.8.0.min.js"></script>-->
<!--<script type="text/javascript" src="${base}/resources/b2c/js/city.js"></script>-->
<!--<link href="${base}/resources/b2c/css/city.css" rel="stylesheet" type="text/css" />-->

<div class="container">
    <div class="logo span5 none-logo">
        <a href="${base}/b2b/index.jhtml">
            [#if versionType==0]
            <img src="${base}/upload/images/b2c-logo.png" style="width: 100%;height: 70px;"/>
            [/#if]
        </a>
    </div>
    <div class="area span7">
        <div class="city">
            <!--<span id="DY_site_name" class="city-name">合肥</span>-->
            <div id="hide_city_menu_11" class="city-select cut_handdler">
                <!--<a href="javascript:void(0);" class="common-bg selector">切换城市</a>-->
                <div id="header_city_bar_box" class="hide_city_group">
                    <div class="hideMap">
                        <div class="showPanel clearfix">
                            <div class="Left mycity">
                                <!--<div id="current_city_box">
                                    当前城市：<strong id="city_current_city">合肥</strong>
                                    <a class="blue" style="" id="set_default_city_header" href="javascript:void(0);">[设为默认城市]</a>
                                </div>-->
                                <div id="default_city_delete" style="display: none;"></div>
                            </div>
                        </div>
                        <div class="showPanel showPanel2 clearfix">
                            <div class="hot_city" id="header_city_hot">
                            </div>
                            <!-- <div class="mt10">
                                <input id="search_city_input_header" class="search_city_input" placeholder="输入城市名" value="输入城市名">
                                <input type="button" id="search_city_submit_header" class="search_city_submit" value="搜索">
                            </div> -->
                            <div class="city_words mt10" id="header_city_char">
                            </div>
                        </div>
                        <div class="scrollBody">
                            <div class="cityMap clearfix">
                                <table id="header_city_list" class="city_list" style="margin-top: 0px;">
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                            <div class="scrollBar">
                                <span id="header_city_bar" style="margin-top: 0px;"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <span class="common-bg city-logo"></span>
        </div>
    </div>
    <div class="search span12" style="float: right;">
        <div class="span12">
            <input id="keyword" value="" placeholder="请输入您要搜索的货品关键字" autocomplete="off" accesskey="s" type="text"/>
            <button type="button" onclick="search('${base}');">搜索</button>
        </div>
        <div class="span10">
            <span>热门搜索：</span>
        [#if setting.hotSearch?exists]
            [#list setting.hotSearch?split(",") as fileName]
                <a href="${base}/b2b/product/list/0.jhtml?keyword=${fileName}">${fileName}</a>
            [/#list]
        [/#if]
        </div>
    </div>
</div>
<script type="text/javascript">
    var city_id="";
    document.getElementById("keyword").onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 13) { // enter 键
            search('${base}');
        }
    };

    $(function(){
        $.ajax({
            url:"${base}/common/area.jhtml",
            type:"get",
            data:{parentId:""},
            dataType:"json",
            success:function(map){
                var html="";
                $.each(map,function(id,name){
                    html+="<a title="+name+" data-code="+id+" class='' onclick='get_province("+id+",this)'>"+name+"</a>";
                });
                $("#province").html(html);
            }
        });
    
    });
    
    function get_province(id,obj){
        $(obj).addClass("active").siblings().removeClass("active");
        city_id=id;
        if($(".tog_button").attr("switch")=="true"){
            toggle_city();
        }else{
            $("#city_tittle").addClass("active").siblings().removeClass("active");
            $("#city_con").show();
            $("#coun_con").hide();
            $("#pro_con").hide();
            $.ajax({
                url:"${base}/common/area.jhtml",
                type:"get",
                data:{parentId:id},
                dataType:"json",
                success:function(map){
                    var html="";
                    $.each(map,function(id,name){
                        html+="<a title="+name+" data-code="+id+" class='' onclick='get_city("+id+",this)'>"+name+"</a>";
                    });
                    html+="</select>";
                    $("#city").html(html);
                }
            });
        }
        
    }

    function get_city(id,obj){
        $(obj).addClass("active").siblings().removeClass("active");
        city_id=id;
        if($(".tog_button").attr("switch")=="true"){
            toggle_city();
        }else{
             $.ajax({
                url:"${base}/b2b/area/getbyid.jhtml",
                type:"get",
                data:{id:id},
                dataType:"json",
                success:function(flag){
                    if(flag==true){
                       $("#county_tittle").addClass("active").siblings().removeClass("active");
                        $("#coun_con").show();
                        $("#city_con").hide();
                        $("#pro_con").hide();
                        $.ajax({
                            url:"${base}/common/area.jhtml",
                            type:"get",
                            data:{parentId:id},
                            dataType:"json",
                            success:function(map){
                                var html="";
                                $.each(map,function(id,name){
                                    html+="<a title="+name+" data-code="+id+" class='' onclick='get_county("+id+",this)'>"+name+"</a>";
                                });
                                $("#county").html(html);
                            }
                        });
                    }
                }
            });
        }      
    }
    function get_county(id,obj){
        $(obj).addClass("active").siblings().removeClass("active");
        city_id=id;
        if($(".tog_button").attr("switch")=="true"){
            toggle_city();
        }
    }
    function toggle_city(){
        if(city_id==""){
            $.message("warn","您还没选择城市！");
            return;
        }
        $(".tog_button").attr("switch","true");
        $.ajax({
            url:"${base}/b2b/area/update_current.jhtml",
            type:"get",
            data:{id:city_id},
            dataType:"json",
            success:function(message){
                if(message.type="success"){
                    $.message("success",message.content);
                    location.reload();
                }else{
                    $.message("error",message.content);
                }
            }
        });
    }
</script>
