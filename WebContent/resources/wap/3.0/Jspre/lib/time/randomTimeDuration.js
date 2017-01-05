/**
 * 返回一个随机时长
 * @param  {[type]} min 范围开始值
 * @param  {[type]} max 范围结束值
 * @return {[type]}     [description]
 */
function randomTimeDuration(min, max) {
    //var range=;//5
    //8-13
    if ($.type(min) != "number" || $.type(max) != "number") {
        return null;
    }
    return (min + Math.random() * (max - min)).toFixed(3);
}
//randomTimeDuration(8,13)
