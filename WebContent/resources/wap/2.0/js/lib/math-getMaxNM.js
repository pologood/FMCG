/**
 * 返回指定范围内能够允许的最大的数值N，指定范围：S
 * 即：S >= N + (N - 1) + (N - 2) + ... + 3 + 2 + 1
 * S >= N * (N - 1);
 * @param  {整型} maxrange 范围值
 * @return {[type]}  [description]
 */
function maxNumberLessThanAccum(maxrange) {
    var N = 0;
    var sum = 0;

    while (sum <= maxrange) {
        sum = sum + ++N;
    }
    return {
        sum: sum - N,
        n: --N
    };
}
