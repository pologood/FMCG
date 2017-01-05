/**
 * Created by Administrator on 2016/11/30 0030.
 */

/**
 *活动导航浮动
 */
$(window).scroll(function() {
    //当距离页面顶部 大于等于top时 浮动
    var top = document.getElementById("act_banner").scrollHeight + document.getElementById("act-0").scrollHeight - 54;
    if($(window).scrollTop() >= top ){
        $("#act_nav").show().addClass("on");
    }else{
        $("#act_nav").hide().removeClass("on");
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
    //scrolls();
    function scrolls(el){
            var fNum,actNum;
            var $this = $('#act_nav ul li');
            var sTop = $(window).scrollTop();
            for (var i = 0;i < $this.length; i++) {
               fNum = 'f' + (i+1);
               actNum = '#act-' + (i+1);
                //console.log("$this.length"+$this.length);
                //console.log("fNum"+fNum);
                //console.log("actNum"+actNum);
                //console.log("i"+i);
                //console.log("sTop"+sTop);
               fNum = $(actNum).offset().top;
                //console.log("fNum"+fNum);
               if (sTop>=fNum-100){
                   $this.eq(i).addClass('active').siblings().removeClass('active');
               }
            }
    };

});