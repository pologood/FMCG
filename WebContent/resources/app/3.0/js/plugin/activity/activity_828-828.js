/**
 * Created by Administrator on 2016/8/10 0010.
 */

/**
 *活动导航浮动
 */
$(window).scroll(function() {
    //当距离页面顶部 大于等于top时 浮动
    var top = document.getElementById("activity_banner").scrollHeight + document.getElementById("activity_timer").scrollHeight + document.getElementById("activity_nav828").scrollHeight*2;
    if($(window).scrollTop() >= top ){
        $("#activity_nav828").css("position","fixed");
        //$("#activity_nav828 a").addClass("ACT924");
    }else{
        $("#activity_nav828").css("position","relative");
        //$("#activity_nav828 a").removeClass("ACT924");
    }
});
/**
 *活动标签点击添加移除样式
 */
$(function() {
    /**
     *  活动楼层
     */
    $(window).scroll(scrolls);
    scrolls();
    function scrolls(){
        var f1,f2,f3,f4;
        var fixRight = $('#activity_nav828 a');
        var sTop = $(window).scrollTop();
        f1 = $('#act-0').offset().top;
        f2 = $('#act-1').offset().top;
        f3 = $('#act-activityTenant').offset().top;
        f4 = $('#act-tenant').offset().top;
        if(sTop>=f1){
            fixRight.eq(0).addClass('ACT828').siblings().removeClass('ACT828');
        }else{
            fixRight.eq(0).removeClass('ACT828');
        }
        if(sTop>=f2-100){
            fixRight.eq(1).addClass('ACT828').siblings().removeClass('ACT828');
        }
        if(sTop>=f3-100){
            fixRight.eq(2).addClass('ACT828').siblings().removeClass('ACT828');
        }
        if(sTop>=f4-100){
            fixRight.eq(3).addClass('ACT828').siblings().removeClass('ACT828');
        }
    };

});