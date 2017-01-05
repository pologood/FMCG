var allowExt = ['jpg', 'gif', 'bmp', 'png', 'jpeg'];
var Picture = function (file, container) {
    var height = 0,
        widht = 0,
        ext = '',
        size = 0,
        name = '',
        path = '';
    var self = this;
    if (file) {
        name = file.value;
        if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
            file.select();
            path = document.selection.createRange().text;
        } else if (window.navigator.userAgent.indexOf("Firefox") >= 1||window.navigator.userAgent.indexOf("Mozilla") >= 0) {
            if (file.files) {
//                path = window.URL.createObjectURL(file.files[0]);
            } else {
                path = file.value;
            }
        }else if (window.navigator.userAgent.indexOf("Chrome") >= 1 || window.navigator.userAgent.indexOf("Safari") >= 1) { 
        	imgObjPreview.src = window.webkitURL.createObjectURL(docObj.files[0]); 
        } 
    } else {
        throw '无效的文件';
    }
    ext = name.substr(name.lastIndexOf("."), name.length);
    if (container.tagName.toLowerCase() != 'img') {
        throw '不是一个有效的图片容器';
        container.visibility = 'hidden';
    }
    container.src = path;
    container.alt = name;
    container.style.visibility = 'visible';
    height = container.height;
    widht = container.widht;
    size = container.fileSize;
    this.get = function (name) {
        return self[name];
    }
    this.isValid = function () {
        if (allowExt.indexOf(self.ext) !== -1) {
            throw '不允许上传该文件类型';
            return false;
        }
    }
}

function preivew(file, container) {
    try {
        var pic = new Picture(file, document.getElementById(container));
    } catch (e) {
        alert(e);
    }
}