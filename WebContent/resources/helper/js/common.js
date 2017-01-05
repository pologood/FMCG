/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.wit.net
 * License: http://www.wit.net/license
 * 
 * JavaScript - Common
 * Version: 3.0
 */
/*
todo  helper
 */
var wit = {
    base: "/helper",
    locale: "zh_CN"
};

var setting = {
    priceScale: "2",
    priceRoundType: "roundUp",
    currencySign: "￥",
    currencyUnit: "元",
    uploadImageExtension: "jpg,jpeg,bmp,gif,png",
    uploadFlashExtension: "swf,flv",
    uploadMediaExtension: "swf,flv,mp3,wav,avi,rm,rmvb",
    uploadFileExtension: "zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx"
};

var messages = {
    "helper.message.success": "操作成功",
    "helper.message.error": "操作错误",
    "helper.dialog.ok": "确&nbsp;&nbsp;定",
    "helper.dialog.cancel": "取&nbsp;&nbsp;消",
    "helper.dialog.deleteConfirm": "您确定要删除吗？",
    "helper.dialog.clearConfirm": "您确定要清空吗？",
    "helper.browser.title": "选择文件",
    "helper.browser.upload": "本地上传",
    "helper.browser.parent": "上级目录",
    "helper.browser.orderType": "排序方式",
    "helper.browser.name": "名称",
    "helper.browser.size": "大小",
    "helper.browser.type": "类型",
    "helper.browser.select": "选择文件",
    "helper.upload.sizeInvalid": "上传文件大小超出限制",
    "helper.upload.typeInvalid": "上传文件格式不正确",
    "helper.upload.invalid": "上传文件格式或大小不正确",
    "helper.validate.required": "必填",
    "helper.validate.email": "E-mail格式错误",
    "helper.validate.url": "网址格式错误",
    "helper.validate.date": "日期格式错误",
    "helper.validate.dateISO": "日期格式错误",
    "helper.validate.pointcard": "信用卡格式错误",
    "helper.validate.number": "只允许输入数字",
    "helper.validate.digits": "只允许输入零或正整数",
    "helper.validate.minlength": "长度不允许小于{0}",
    "helper.validate.maxlength": "长度不允许大于{0}",
    "helper.validate.rangelength": "长度必须在{0}-{1}之间",
    "helper.validate.min": "不允许小于{0}",
    "helper.validate.max": "不允许大于{0}",
    "helper.validate.range": "必须在{0}-{1}之间",
    "helper.validate.accept": "输入后缀错误",
    "helper.validate.equalTo": "两次输入不一致",
    "helper.validate.remote": "输入错误",
    "helper.validate.integer": "只允许输入整数",
    "helper.validate.positive": "只允许输入正数",
    "helper.validate.negative": "只允许输入负数",
    "helper.validate.decimal": "数值超出了允许范围",
    "helper.validate.pattern": "格式错误",
    "helper.validate.extension": "文件格式错误"
};

// 添加Cookie
function addCookie(name, value, options) {
    if (arguments.length > 1 && name != null) {
        if (options == null) {
            options = {};
        }
        if (value == null) {
            options.expires = -1;
        }
        if (typeof options.expires == "number") {
            var time = options.expires;
            var expires = options.expires = new Date();
            expires.setTime(expires.getTime() + time * 1000);
        }
        document.cookie = encodeURIComponent(String(name)) + "=" + encodeURIComponent(String(value)) + (options.expires ? "; expires=" + options.expires.toUTCString() : "") + (options.path ? "; path=" + options.path : "") + (options.domain ? "; domain=" + options.domain : ""), (options.secure ? "; secure" : "");
    }
}

// 获取Cookie
function getCookie(name) {
    if (name != null) {
        var value = new RegExp("(?:^|; )" + encodeURIComponent(String(name)) + "=([^;]*)").exec(document.cookie);
        return value ? decodeURIComponent(value[1]) : null;
    }
}

// 移除Cookie
function removeCookie(name, options) {
    addCookie(name, null, options);
}

// 货币格式化
function currency(value, showSign, showUnit) {
    if (value != null) {
        var price;
        if (setting.priceRoundType == "roundHalfUp") {
            price = (Math.round(value * Math.pow(10, setting.priceScale)) / Math.pow(10, setting.priceScale)).toFixed(setting.priceScale);
        } else if (setting.priceRoundType == "roundUp") {
            price = (Math.ceil(value * Math.pow(10, setting.priceScale)) / Math.pow(10, setting.priceScale)).toFixed(setting.priceScale);
        } else {
            price = (Math.floor(value * Math.pow(10, setting.priceScale)) / Math.pow(10, setting.priceScale)).toFixed(setting.priceScale);
        }
        if (showSign) {
            price = setting.currencySign + price;
        }
        if (showUnit) {
            price += setting.currencyUnit;
        }
        return price;
    }
}

// 多语言
function message(code) {
    if (code != null) {
        var content = messages[code] != null ? messages[code] : code;
        if (arguments.length == 1) {
            return content;
        } else {
            if ($.isArray(arguments[1])) {
                $.each(arguments[1], function (i, n) {
                    content = content.replace(new RegExp("\\{" + i + "\\}", "g"), n);
                });
                return content;
            } else {
                $.each(Array.prototype.slice.apply(arguments).slice(1), function (i, n) {
                    content = content.replace(new RegExp("\\{" + i + "\\}", "g"), n);
                });
                return content;
            }
        }
    }
}

(function ($) {

    var zIndex = 100;

    // 消息框
    var $message;
    var messageTimer;
    $.message = function () {
        var message = {};
        if ($.isPlainObject(arguments[0])) {
            message = arguments[0];
        } else if (typeof arguments[0] === "string" && typeof arguments[1] === "string") {
            message.type = arguments[0];
            message.content = arguments[1];
        } else {
            return false;
        }

        if (message.type == null || message.content == null) {
            return false;
        }

        if ($message == null) {
            $message = $('<div class="xxMessage"><div class="messageContent message' + message.type + 'Icon"><\/div><\/div>');
            if (!window.XMLHttpRequest) {
                $message.append('<iframe class="messageIframe"><\/iframe>');
            }
            $message.appendTo("body");
        }

        $message.children("div").removeClass("messagewarnIcon messageerrorIcon messagesuccessIcon").addClass("message" + message.type + "Icon").html(message.content);
        $message.css({"margin-left": -parseInt($message.outerWidth() / 2), "z-index": zIndex++}).show();

        clearTimeout(messageTimer);
        messageTimer = setTimeout(function () {
            $message.hide();
        }, 3000);
        return $message;
    };

    // 对话框
    $.dialog = function (options) {
        var settings = {
            width: 320,
            height: "auto",
            modal: true,
            ok: message("helper.dialog.ok"),
            cancel: message("helper.dialog.cancel"),
            onShow: null,
            onClose: null,
            onOk: null,
            onCancel: null
        };
        $.extend(settings, options);

        if (settings.content == null) {
            return false;
        }

        var $dialog = $('<div class="xxDialog"><\/div>');
        var $dialogTitle;
        var $dialogClose = $('<div class="dialogClose"><\/div>').appendTo($dialog);
        var $dialogContent;
        var $dialogBottom;
        var $dialogOk;
        var $dialogCancel;
        var $dialogOverlay;
        if (settings.title != null) {
            $dialogTitle = $('<div class="dialogTitle"><\/div>').appendTo($dialog);
        }
        if (settings.type != null) {
            $dialogContent = $('<div class="dialogContent dialog' + settings.type + 'Icon"><\/div>').appendTo($dialog);
        } else {
            $dialogContent = $('<div class="dialogContent"><\/div>').appendTo($dialog);
        }
        if (settings.ok != null || settings.cancel != null) {
            $dialogBottom = $('<div class="dialogBottom"><\/div>').appendTo($dialog);
        }
        if (settings.ok != null) {
            $dialogOk = $('<input type="button" class="button" value="' + settings.ok + '" \/>').appendTo($dialogBottom);
        }
        if (settings.cancel != null) {
            $dialogCancel = $('<input type="button" class="button" value="' + settings.cancel + '" \/>').appendTo($dialogBottom);
        }
        if (!window.XMLHttpRequest) {
            $dialog.append('<iframe class="dialogIframe"><\/iframe>');
        }
        $dialog.appendTo("body");
        if (settings.modal) {
            $dialogOverlay = $('<div class="dialogOverlay"><\/div>').insertAfter($dialog);
        }

        var dialogX;
        var dialogY;
        if (settings.title != null) {
            $dialogTitle.text(settings.title);
        }
        $dialogContent.html(settings.content);
        $dialog.css({
            "width": settings.width,
            "height": settings.height,
            "margin-left": -parseInt(settings.width / 2),
            "z-index": zIndex++
        });
        dialogShow();

        if ($dialogTitle != null) {
            $dialogTitle.mousedown(function (event) {
                $dialog.css({"z-index": zIndex++});
                var offset = $(this).offset();
                if (!window.XMLHttpRequest) {
                    dialogX = event.clientX - offset.left;
                    dialogY = event.clientY - offset.top;
                } else {
                    dialogX = event.pageX - offset.left;
                    dialogY = event.pageY - offset.top;
                }
                $("body").bind("mousemove", function (event) {
                    $dialog.css({"top": event.clientY - dialogY, "left": event.clientX - dialogX, "margin": 0});
                });
                return false;
            }).mouseup(function () {
                $("body").unbind("mousemove");
                return false;
            });
        }

        if ($dialogClose != null) {
            $dialogClose.click(function () {
                dialogClose();
                return false;
            });
        }

        if ($dialogOk != null) {
            $dialogOk.click(function () {
                if (settings.onOk && typeof settings.onOk == "function") {
                    if (settings.onOk($dialog) != false) {
                        dialogClose();
                    }
                } else {
                    dialogClose();
                }
                return false;
            });
        }

        if ($dialogCancel != null) {
            $dialogCancel.click(function () {
                if (settings.onCancel && typeof settings.onCancel == "function") {
                    if (settings.onCancel($dialog) != false) {
                        dialogClose();
                    }
                } else {
                    dialogClose();
                }
                return false;
            });
        }

        function dialogShow() {
            if (settings.onShow && typeof settings.onShow == "function") {
                if (settings.onShow($dialog) != false) {
                    $dialog.show();
                    $dialogOverlay.show();
                }
            } else {
                $dialog.show();
                $dialogOverlay.show();
            }
        }

        function dialogClose() {
            if (settings.onClose && typeof settings.onClose == "function") {
                if (settings.onClose($dialog) != false) {
                    $dialogOverlay.remove();
                    $dialog.remove();
                }
            } else {
                $dialogOverlay.remove();
                $dialog.remove();
            }
        }

        return $dialog;
    };

    // 文件浏览
    $.fn.extend({
        browser: function (options) {
            var settings = {
                type: "image",
                title: message("helper.browser.title"),
                isUpload: true,
                uploadUrl: wit.base + "/file/upload-crop.jhtml",
                jcropUrl: wit.base + "/file/jcrop.jhtml",
                submitUrl: wit.base + "/file/submit.jhtml",
                callback: null,
                isSubmit: true,
                width: 0,
                height: 0
            };
            $.extend(settings, options);

            var token = getCookie("token");
            var cache = {};
            return this.each(function () {
                var browserFrameId = "browserFrame" + (new Date()).valueOf() + Math.floor(Math.random() * 1000000);
                var $browserButton = $(this);
                $browserButton.click(function () {
                    var $browser = $('<div class="xxBrowser"><\/div>');
                    var $browserBar = $('<div class="browserBar"><\/div>').appendTo($browser);
                    var $browserFrame;
                    var $browserForm;
                    var $browserUploadButton;
                    var $browserUploadInput;
                    var $browserJcropButton;
                    var $browserSubmitButton;
                    var $browserLoadingIcon;
                    var $browserList;
                    if (settings.isUpload) {
                        $browserFrame = $('<iframe id="' + browserFrameId + '" name="' + browserFrameId + '" style="display: none;"><\/iframe>').appendTo($browserBar);
                        $browserForm = $('<form action="' + settings.uploadUrl + '" method="post" encType="multipart/form-data" target="' + browserFrameId + '"><input type="hidden" name="token" value="' + token + '" \/><input type="hidden" name="fileType" value="' + settings.type + '" \/><\/form>').appendTo($browserBar);
                        $browserUploadButton = $('<a href="javascript:;" class="browserUploadButton button">' + message("helper.browser.upload") + '<\/a>').appendTo($browserForm);
                        $browserUploadInput = $('<input type="file" name="file" accept="image/*"  \/>').appendTo($browserUploadButton);
                    }
                    $browserJcropButton = $('<a href="javascript:;" class="button">裁切<\/a>').appendTo($browserBar);
                    $browserSubmitButton = $('<a href="javascript:;" class="button">提交<\/a>').appendTo($browserBar);
                    $browserLoadingIcon = $('<span class="loadingIcon" style="display: none;">&nbsp;<\/span>').appendTo($browserBar);
                    $browserList = $('<div class="browserList"><\/div>').appendTo($browser);

                    var $dialog = $.dialog({
                        title: settings.title,
                        content: $browser,
                        width: 630,
                        modal: true,
                        ok: null,
                        cancel: null
                    });

                    createBrowserList(null);
                    function createBrowserList(data) {
                        var browserListHtml = "";
                        if (data == null) {
                            browserListHtml = '<span style="left:50%;top:50%">请上传本地文件...</span>';
                            $browserList.html(browserListHtml);
                            return;
                        }
                        else {
                            browserListHtml = '<input type=\"hidden\" id=\"url\" name=\"url\" value=\"' + data.url + '\"><input type=\"hidden\" id=\"local\" name=\"local\"><input type=\"hidden\" id=\"x\" name=\"x\" value=\"0\"><input type=\"hidden\" id=\"y\" name=\"y\" value=\"0\"><input type=\"hidden\" id=\"w\" name=\"w\" value=\"0\"><input type=\"hidden\" id=\"h\" name=\"h\" value=\"0\"><img src=\"' + data.url + '\" id=\"target\" alt=\"请上传本地文件\"/>';
                            $browserList.html(browserListHtml);
                        }
                        $browserList.html(browserListHtml);
                        $browserJcropButton.unbind("click").bind("click", function () {
                            $.ajax({
                                url: settings.jcropUrl,
                                type: "POST",
                                data: {
                                    url: $('#url').val(),
                                    local: $('#local').val(),
                                    x: $('#x').val(),
                                    y: $('#y').val(),
                                    w: $('#w').val(),
                                    h: $('#h').val()
                                },
                                dataType: "json",
                                cache: false,
                                beforeSend: function () {
                                    $browserLoadingIcon.show();
                                },
                                success: function (data) {
                                    if (data.message.type == "success") {
                                        createBrowserList(data);
                                    } else {
                                        $.message(data.message);
                                    }
                                },
                                complete: function () {
                                    $browserLoadingIcon.hide();
                                }
                            });
                        });
                        $browserSubmitButton.unbind("click").bind("click", function () {
                            if (settings.isSubmit) {
                                $.ajax({
                                    url: settings.submitUrl,
                                    type: "POST",
                                    data: {
                                        local: $('#local').val(),
                                        url: $('#url').val(),
                                        w: settings.width,
                                        h: settings.height,
                                        isUpload: settings.isUpload
                                    },
                                    dataType: "json",
                                    cache: false,
                                    beforeSend: function () {
                                        $browserLoadingIcon.show();
                                    },
                                    success: function (data) {
                                        if (data.message.type == "success") {
                                            if (settings.input != null) {
                                                settings.input.val(data.url);
                                            } else {
                                                $browserButton.prev(":text").val(data.url);
                                                $browserButton.next().attr("href", data.url);
                                            }
                                            if (settings.callback != null && typeof settings.callback == "function") {
                                                settings.callback(data.url);
                                            }
                                            cache = {};
                                            $dialog.next(".dialogOverlay").andSelf().remove();
                                        } else {
                                            $.message(data.message);
                                        }
                                    },
                                    complete: function () {
                                        $browserLoadingIcon.hide();
                                    }
                                });
                            } else {
                                $browserButton.prev(":hidden").val(data.local);
                                if (settings.img != null) {
                                    settings.img.attr("src", data.url);
                                }
                                if (settings.callback != null && typeof settings.callback == "function") {
                                    settings.callback(data.url, data.local);
                                }
                                cache = {};
                                $dialog.next(".dialogOverlay").andSelf().remove();
                            }
                        });
                        $("#local").val(data.local);
                        adjustImgSize($("#target"), 600, 400);

                        var jcrop_api;

                        function initJcrop() {
                            var rate = 1;
                            if (settings.width != 0) {
                                rate = settings.width / settings.height;
                            }
                            $('#target').Jcrop({
                                aspectRatio: rate,
                                onSelect: updateCoords,
                                onRelease: releaseCheck
                            }, function () {
                                jcrop_api = this;
                            });

                        }

                        function releaseCheck() {
                            jcrop_api.setOptions({
                                allowSelect: true
                            });
                            $('#x').val("0");
                            $('#y').val("0");
                            $('#w').val("0");
                            $('#h').val("0");
                        }

                        function updateCoords(c) {
                            $('#x').val(c.x);
                            $('#y').val(c.y);
                            $('#w').val(c.w);
                            $('#h').val(c.h);
                        }

                        function adjustImgSize(img, boxWidth, boxHeight) {

                            var tempImg = new Image();
                            tempImg.onload = function () {
                                var imgWidth = tempImg.width;
                                var imgHeight = tempImg.height;

                                //比较imgBox的长宽比与img的长宽比大小
                                if ((boxWidth / boxHeight) >= (imgWidth / imgHeight)) {
                                    //重新设置img的width和height
                                    img.width((boxHeight * imgWidth) / imgHeight);
                                    img.height(boxHeight);

                                }
                                else {
                                    //重新设置img的width和height
                                    img.width(boxWidth);
                                    img.height((boxWidth * imgHeight) / imgWidth);
                                }
                                initJcrop();
                            };
                            tempImg.src = img.attr('src');
                        }
                    }


                    $browserUploadInput.change(function () {
                        var allowedUploadExtensions;
                        if (settings.type == "flash") {
                            allowedUploadExtensions = setting.uploadFlashExtension;
                        } else if (settings.type == "media") {
                            allowedUploadExtensions = setting.uploadMediaExtension;
                        } else if (settings.type == "file") {
                            allowedUploadExtensions = setting.uploadFileExtension;
                        } else {
                            allowedUploadExtensions = setting.uploadImageExtension;
                        }
                        if ($.trim(allowedUploadExtensions) == "" || !new RegExp("^\\S.*\\.(" + allowedUploadExtensions.replace(/,/g, "|") + ")$", "i").test($browserUploadInput.val())) {
                            $.message("warn", message("helper.upload.typeInvalid"));
                            return false;
                        }
                        $browserLoadingIcon.show();
                        $browserForm.submit();
                    });

                    $browserFrame.load(function () {
                        var text;
                        var io = document.getElementById(browserFrameId);
                        if (io.contentWindow) {
                            text = io.contentWindow.document.body ? io.contentWindow.document.body.innerHTML : null;
                        } else if (io.contentDocument) {
                            text = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
                        }
                        if ($.trim(text) != "") {
                            $browserLoadingIcon.hide();
                            var data = $.parseJSON(text);
                            if (data.message.type == "success") {
                                if (data.fileType == "image") {
                                    createBrowserList(data);
                                } else {
                                    if (settings.input != null) {
                                        settings.input.val(data.url);
                                    } else {
                                        $browserButton.prev(":text").val(data.url);
                                    }
                                    if (settings.callback != null && typeof settings.callback == "function") {
                                        settings.callback(data.url);
                                    }
                                    cache = {};
                                    $dialog.next(".dialogOverlay").andSelf().remove();
                                }
                            } else {
                                $.message(data.message);
                            }
                        }
                    });

                });
            });
        }
    });

    // 令牌
    $(document).ajaxSend(function (event, request, settings) {
        if (!settings.crossDomain && settings.type != null && settings.type.toLowerCase() == "post") {
            var token = getCookie("token");
            if (token != null) {
                request.setRequestHeader("token", token);
            }
        }
    });

    $(document).ajaxComplete(function (event, request, settings) {
        var loginStatus = request.getResponseHeader("loginStatus");
        var tokenStatus = request.getResponseHeader("tokenStatus");

        if (loginStatus == "accessDenied") {
            $.message("warn", "登录超时，请重新登录");
            setTimeout(function () {
                location.reload(true);
            }, 2000);
        } else if (loginStatus == "unauthorized") {
            $.message("warn", "对不起，您无此操作权限！");
        } else if (tokenStatus == "accessDenied") {
            var token = getCookie("token");
            if (token != null) {
                $.extend(settings, {
                    global: false,
                    headers: {token: token}
                });
                $.ajax(settings);
            }
        }
    });

})(jQuery);

// 令牌
$().ready(function () {

    $("form").submit(function () {
        var $this = $(this);
        if ($this.attr("method") != null && $this.attr("method").toLowerCase() == "post" && $this.find("input[name='token']").size() == 0) {
            var token = getCookie("token");
            if (token != null) {
                $this.append('<input type="hidden" name="token" value="' + token + '" \/>');
            }
        }
    });

});

function iFrameHeight() {
    var ifm = document.getElementById("iframepage");
    var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument;
    if (ifm != null && subWeb != null) {
        ifm.height = subWeb.body.scrollHeight;
        ifm.width = subWeb.body.scrollWidth;
    }
}
