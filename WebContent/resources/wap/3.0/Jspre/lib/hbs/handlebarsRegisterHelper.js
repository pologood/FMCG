/**
 * Handlebars RegisterHelper
 */
Handlebars.registerHelper("expression", function(left, operator, right, options) {
    if (arguments.length < 3) {
        throw new Error('Handlerbars Helper "compare" needs 2 parameters');
    }
    var operators = {
        '==': function(l, r) {
            return l == r;
        },
        '===': function(l, r) {
            return l === r;
        },
        '!=': function(l, r) {
            return l != r;
        },
        '!==': function(l, r) {
            return l !== r;
        },
        '<': function(l, r) {
            return l < r;
        },
        '>': function(l, r) {
            return l > r;
        },
        '<=': function(l, r) {
            return l <= r;
        },
        '>=': function(l, r) {
            return l >= r;
        },
        'typeof': function(l, r) {
            return typeof l == r;
        }
    };

    if (!operators[operator]) {
        throw new Error('Handlerbars Helper "compare" doesn\'t know the operator ' + operator);
    }

    var result = operators[operator](left, right);

    if (result) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

Handlebars.registerHelper("compare", function(v1, v2, options) {
    if (v1 == v2) {
        //满足添加继续执行
        return options.fn(this);
    } else {
        //不满足条件执行{{else}}部分
        return options.inverse(this);
    }
});

Handlebars.registerHelper('addOne', function(index) {
    this._index = index + 1;
    return this._index;
});

Handlebars.registerHelper('ifCond', function(v1, v2, options) {
    if (v1 === v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});

//注册一个比较字符串是否一致的Helper,有options参数，块级Helper
Handlebars.registerHelper("compare1", function(v1, v2, options) {
    //判断v1、v2是否相等
    v1 = v1.replace(/^\s+|\s+$/g, "");
    v2 = v2.replace(/^\s+|\s+$/g, "");
    if (v1 == v2) {
        //继续执行
        return options.fn(this);
    } else {
        //执行else部分
        return options.inverse(this);
    }
});

function getFormatTime(time, fo) {
    var t = new Date(time);
    var tf = function(i) {
        return (i < 10 ? '0' : '') + i;
    };
    return fo.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
        switch (a) {
            case 'yyyy':
                return tf(t.getFullYear());
            case 'MM':
                return tf(t.getMonth() + 1);
            case 'dd':
                return tf(t.getDate());
            case "HH":
                return tf(t.getHours());
            case "mm":
                return tf(t.getMinutes());
            case "ss":
                return tf(t.getSeconds());
        }
    });
}

Handlebars.registerHelper('formatDate', function(time, fo) {
    return getFormatTime(time, fo);
});

Handlebars.registerHelper('timeFormatUtil', function(time) {
    return timeFormatUtil(time);
});

function timeFormatUtil(time) {
    var t = new Date(time);
    var millisecond = new Date().getTime() - t.getTime();
    var second = millisecond / 1000;
    if (second <= 0) {
        second = 0;
    }

    if (second === 0) {
        interval = "刚刚";
    } else if (second < 30) {
        interval = parseInt(second) + "秒以前";
    } else if (second >= 30 && second < 60) {
        interval = "半分钟前";
    } else if (second >= 60 && second < 60 * 60) { //大于1分钟 小于1小时
        var minute = second / 60;
        interval = (parseInt(minute)) + "分钟前";
    } else if (second >= 60 * 60 && second < 60 * 60 * 24) { //大于1小时 小于24小时
        var hour = (second / 60) / 60;
        interval = (parseInt(hour)) + "小时前";
    } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) { //大于1D 小于2D
        interval = "昨天" + getFormatTime(time, "HH:mm");
    } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) { //大于2D小时 小于 7天
        var day = ((second / 60) / 60) / 24;
        interval = (parseInt(day)) + "天前";
    } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) { //大于7天小于365天
        interval = getFormatTime(time, "MM-dd HH:mm");
    } else if (second >= 60 * 60 * 24 * 365) { //大于365天
        interval = getFormatTime(time, "yyyy-MM-dd HH:mm");
    } else {
        interval = "0";
    }
    return interval;
}

Handlebars.registerHelper('list', function(items, options) {
    for (var i = 0; i < items.length; i++) {
        return options.fn(items[0]);
    }
});

Handlebars.registerHelper('formatdistance', function(disvalue) {
    /* 
    if (disvalue == "-1") {
        
    } else {
        
    }*/
    if (disvalue >= 0) {
        return (parseFloat(disvalue) / 1000).toFixed(2) + "km";
    } else {
        return "";
    }
});
/**
 * 纯小数->百分比
 */
Handlebars.registerHelper('formatPraisedegreePercent', function(value) {
    if (value <= 1) { //小数
        return (parseFloat(value) * 100).toFixed(0) + "%";
    } else {
        return "";
    }
});

function NoToChinese(num) {
    if (!/^\d*(\.\d*)?$/.test(num)) {
        alert("Number is wrong!");
        return "Number is wrong!";
    }
    var AA = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖");
    var BB = new Array("", "拾", "佰", "仟", "萬", "億", "点", "");
    var a = ("" + num).replace(/(^0*)/g, "").split("."),
        k = 0,
        re = "";
    for (var i = a[0].length - 1; i >= 0; i--) {
        switch (k) {
            case 0:
                re = BB[7] + re;
                break;
            case 4:
                if (!new RegExp("0{4}\\d{" + (a[0].length - i - 1) + "}$").test(a[0]))
                    re = BB[4] + re;
                break;
            case 8:
                re = BB[5] + re;
                BB[7] = BB[5];
                k = 0;
                break;
        }
        if (k % 4 == 2 && a[0].charAt(i + 2) !== 0 && a[0].charAt(i + 1) === 0) re = AA[0] + re;
        if (a[0].charAt(i) !== 0) re = AA[a[0].charAt(i)] + BB[k % 4] + re;
        k++;
    }

    if (a.length > 1) //加上小数部分(如果有小数部分)
    {
        re += BB[6];
        for (var kk = 0; kk < a[1].length; kk++) re += AA[a[1].charAt(kk)];
    }
    return re;
}
