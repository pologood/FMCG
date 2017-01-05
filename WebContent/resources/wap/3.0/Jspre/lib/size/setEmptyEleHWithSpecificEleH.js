/**
 * 固定元素占位副本赋高
 * 调用时需注意调用位置(调用时该fixed元素是否存在)
 * @param  {[type]} empty_slter    [description]
 * @param  {[type]} fixedele_slter [description]
 * @return {[type]}                [description]
 */
function setEmptyEleH(arg) {
    //占位元素赋高
    if ($.type(arg) == "array") {
        $.each(arg, function(index, value) {
            /* iterate through array or object */
            $("[data-emptyfor='" + value + "']").height($("[data-emptyto='" + value + "']").height());
        });
    }
    if ($.type(arg) == "string") {
        $("[data-emptyfor='" + arg + "']").height($("[data-emptyto='" + arg + "']").height());
    }
}
//setEmptyEleH("topbar")
