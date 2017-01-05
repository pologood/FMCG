/**
 * 根据经纬度获取城市名
 * @return {[type]} [description]
 */
function getCityNameByPos(lat, lng, rturl) {
    var urlbase = _TH_.base ? _TH_.base : '';
    rturl = rturl ? rturl : urlbase + '/app/lbs/get.jhtml';
    return $.ajax({
        url: rturl,
        data: {
            lat: lat,
            lng: lng,
            force: true
        }
    });
}
