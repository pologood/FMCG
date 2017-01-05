<!DOCTYPE html>
<html lang="en">
<head>
    [#include "/wap/include/resource-2.0.ftl"/]
    <title>同城优品</title>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <script src="${base}/resources/wap/2.0/js/zepto.waypoints.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/lib.js"></script>
    <script src="${base}/resources/wap/2.0/js/sync-async-loading-handlebars.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <div class="am-g displaybox" style="background-color: #ffffff;">
        <!-- 
            <img class="separator" src="${base}/resources/wap/image/youpin1.png" alt="load.."/>-->
            <a href="${base}/wap/product/content/{{id}}/product.jhtml" class="sec-img">
                <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                     data-original="{{image}}" alt="load.."/>
                 <div class="corner_indexmark initial">
                     <span class="num">{{addOne @index}}</span>
                     <div class="bg1">&nbsp;</div>
                     <div class="bg2">&nbsp;</div>
                 </div>
            </a>
            <div class="weui_cells cl sec-txt" style="margin-top: 0">
                <div class="weui_cell part1">
                    <div class="weui_cell_primary">
                        <span class="font-small">{{introduction}}</span>
                    </div>
                </div>
                <div class="weui_cell cl review part2">
                    <div class="weui_cell_bd weui_cell_primary font-large-2 red">
                        <p>￥{{price}}</p>
                    </div>
                    <a href="${base}/wap/product/content/{{id}}/product.jhtml?" class="weui_cell_ft font-small"
                       style="background-color: #ff5555;color: #fff;border-radius: 5px;padding:2px 10px;text-align: center;">
                        购买
                    </a>
                </div>
            </div>
        </div>
        {{/each}}
    </script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">

    <div class="am-g">
        <div id="silder" class="silder" style="position: relative;">
            <img src="${base}/resources/wap/2.0/images/blank.png" alt="load.."/>
        </div>
        <div class="youpin-symbol">
            <div class="sec1">
                <div class="sec1-inner">
                    <div class="table-cell">
                        <span class="pt1">Jan</span>
                        <span class="pt2">1</span>                        
                    </div>
                </div>
            </div>
            <div class="dark-grey sec2">
                同城优品
            </div>
            <!-- soga begin-->
            <div class="soga-authorR none">
                
            </div>
            <div class="soga-student none">
                
            </div>
            <div class="other-student2 none">
                
            </div>
            <!-- soga end-->
        </div>
        <div id="youpin">

        </div>
    </div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        var ads = [];
        var i = 1;

        //placeholder

       if(ads.length==0){
           ads.push({content: '${base}/resources/wap/2.0/upload/no-1.png'});
       }

        var s = new iSlider(document.getElementById('silder'), ads, {
            isLooping: 1,
            isOverspread: 0,
            isAutoplay: 1,
            animateTime: 800,
            animateType: 'rotate'
        });

        var compiler = Handlebars.compile($("#wap-list-item").html());
        $('#youpin').html(compiler(${cityYoupin}));
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });
        //数字角标微信(qq x5)下修复
        if(JSHelper.checkUA.isQQx5()){
            //$(".displaybox .corner_indexmark").addClass('forqqx5');
        }
        //dateNowFormatter
        dateNowFormatter("some_slter",function(month,day){
            var newmonth=month.toUpperCase();
            $(".youpin-symbol .pt1").text(newmonth);
            $(".youpin-symbol .pt2").text(day);
        });
        //waypoints
        var waypoints = $('.corner_indexmark').waypoint({
            handler: function(direction) {
                //notify(this.element.id + ' hit');
                $(this.element).toggleClass('initial');
            },
            offset: 'bottom-in-view'
        });
        //按需获取hbs模板测试
        //hbs temp: author
        hbsTO.render('author', function(t) {
            $('.soga-authorR').html(t({
                authorname:"tom",
                authorinfo:"he is a teacher"
            }));
        });
        //hbs temp: student
        hbsTO.render('student', function(t) {
            $('.soga-student').html(t({
                name:"lina",
                score:"her english score is 100"
            }));
        });
        //hbs temp: student2
        hbsTO.render('student', function(t) {
            $('.other-student2').html(t({
                name:"catelina",
                score:"her english score is 90"
            }));
        });
    });
</script>
</body>
</html>
