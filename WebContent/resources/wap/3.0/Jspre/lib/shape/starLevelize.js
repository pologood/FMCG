/**
 * 序列化星级
 * 根据固定html结构中元素提供的数字，把星星级别化
 */
;
(function($) {
    $.fn.starLevelize = function() {
        return this.each(function(index, ele) {
            var $ele = $(ele);
            //get flag
            if ($ele.data("starlevelized")) {
                return;
            }
            var starnum = parseFloat($ele.data("starnum")).toFixed(1);
            var starnum_int = parseInt(starnum, 10); //3
            var starnum_dec_per = parseInt((starnum - starnum_int) * 100, 10) + "%"; //0.7
            //3个fullstar 1个half star 1个empty star
            if (starnum_int <= 5) {
                $ele.children().eq(starnum_int).css({
                    background: '-webkit-linear-gradient(left, #ff6d06 ' + starnum_dec_per + ', #bfbebc ' + starnum_dec_per + ')'
                });
            }
            for (var i = starnum_int + 1; i < 5; i++) {
                $ele.children().eq(i).css({
                    background: '-webkit-linear-gradient(left, #bfbebc, #bfbebc)'
                });
            }
            //set flag
            $ele.data("starlevelized", true);
        });
    };
})(Zepto);
//$(".starlevels").starLevelize();
