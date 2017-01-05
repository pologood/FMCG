/**
 * 返回随机色值
 * @return {[type]} [description]
 */
function randomColorValue() {
    var colorstr = '';
    for (var i = 0; i < 3; i++) {
        var onecolor = parseInt(Math.random() * 255, 10).toString(16);
        if (onecolor.length === 1) {
            onecolor = '0' + onecolor;
        }
        colorstr += onecolor;
    }
    return '#' + colorstr;
}
