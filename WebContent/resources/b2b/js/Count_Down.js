/**
 * Created by Administrator on 2016/2/1.
 */
/**
 *首先获取当前时间与目标时间的时间差，然后通过定时器更新这个时间差，就实现了倒计时效果。
 *实现上述过程需要以下两个函数：
 * getTime()// 返回距1970年1月1日之间的毫秒数，这样将时间差（毫秒数）÷3600÷24即为天数，时分秒类似
 *setTimeout(code,millisec);// 在指定的毫秒数后调用函数
 *
 *
 **/
function show_time(){
    var time_start = new Date().getTime(); //设定当前时间
    var time_end =  new Date("2016/04/09 00:00:00").getTime(); //设定目标时间
    // 计算时间差
    var time_distance = time_end - time_start;
    // 天
    var int_day = Math.floor(time_distance/86400000)
    time_distance -= int_day * 86400000;
    // 时
    var int_hour = Math.floor(time_distance/3600000)
    time_distance -= int_hour * 3600000;
    // 分
    var int_minute = Math.floor(time_distance/60000)
    time_distance -= int_minute * 60000;
    // 秒
    var int_second = Math.floor(time_distance/1000)
    // 时分秒为单数时、前面加零
    if(int_day < 10){
        int_day = "0" + int_day;
    }
    if(int_hour < 10){
        int_hour = "0" + int_hour;
    }
    if(int_minute < 10){
        int_minute = "0" + int_minute;
    }
    if(int_second < 10){
        int_second = "0" + int_second;
    }
    // 显示时间
    $(".count_Down_d").val(int_day);
    $(".count_Down_h").val(int_hour);
    $(".count_Down_m").val(int_minute);
    $(".count_Down_s").val(int_second);
    // 设置定时器
    setTimeout("show_time()",1000);
}