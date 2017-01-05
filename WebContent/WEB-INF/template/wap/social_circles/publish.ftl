<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>描述</title>
    <style type="text/css">
        .spanRadius {
            border-radius: 50%;
            /* background-color: rgba(0,0,0,0.6); */
            color: red;
            position: absolute;
            top: 0;
            margin-left: 10px;
            right: 0;
            z-index: 80;
            transform: translate(15%, -15%);
            width: 1.2em;
            height: 1.2em;
            line-height: 1.2em;
            text-align: center;
            font-weight: bold;

        }

        .spanRadius b {
            font-weight: normal;
            font-size: 0.3em;
            display: inherit;
        }

        .weui_btn_1 {
            position: relative;
            display: block;
            margin-left: auto;
            margin-right: auto;
            padding-left: 14px;
            padding-right: 14px;
            box-sizing: border-box;
            text-align: center;
            text-decoration: none;
            line-height: 2;
            border-radius: 5px;
            overflow: hidden;
            border: 1px solid rgba(0, 0, 0, .2);
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        [#list products as product]
        <div class="weui_cell">
            <div class="weui_cell_hd">
                <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                     data-original="${product.thumbnail}" class="lazy" alt="icon"
                     style="width:80px;margin-right:5px;display:block">
            </div>
            <div class="weui_cell_bd weui_cell_primary">
                <p style="font-size:15px;">${product.fullName}</p>
                <p style="font-size:15px;color:#E6C99C">￥${product.price}
                    [#if product.promotions]
                        [#list product.promotions as promotion]
                            <span class="order-discount order-prefer"
                                  style="margin-left: 2rem;font-size: 12px;">${promotion.name}</span>
                        [/#list]
                    [/#if]
                </p>
            </div>
            <a style="text-align:right;" name="rmlink" href="javascript:bindListener(${product.id})">
            <span class="order-discount order-prefer" style="margin-left: 5rem;">
                        删除
                </span>
            </a>
        </div>
        [/#list]
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <form id="headForm" action="${base}/app/file/upload_image.jhtml" method="post" enctype="multipart/form-data">
        <input type="file" id="headImg" name="file" style="display:none;" accept="image/*" multiple/>
        <!-- onchange="getFullPath(this)"-->
    </form>
    <div class="weui_cells weui_cells_form" style="margin-top: 0.8rem;">
        <div class="weui_cell">
            <div class="weui_cell_bd weui_cell_primary">
                <textarea id="weuiTextarea" class="weui_textarea" placeholder="写下详细的购物心得，与朋友们分享您的购物体验！！！"
                          rows="3"></textarea>
                <div class="weui_textarea_counter"><span id="mathLength">0</span>/200</div>
            </div>
        </div>
        <div class="weui_cell cl">
            <div class="weui_cell_bd weui_cell_primary">
                <div class="weui_uploader">
                    <div class="weui_uploader_bd">
                        <ul class="weui_uploader_files" id="photo_list">
                        </ul>
                        <div class="weui_uploader_input_wrp" id="upload_file"
                             style="margin-left:5px;margin-top:5px;position:relative">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    [#if type!='wayward']
    <div style="width: 100%;background-color: white;">
        <div class="weui_cells weui_cells_access" style="margin-top: 0.8rem; margin-right: 1rem;" id="checkedOrder">

        </div>
        [#if type == 'camera']
            <div class="weui_cell">
                <a href="${base}/wap/social_circles/productShow.jhtml?type=camera" class="weui_btn_1"><i
                        class="iconfont">&#xe633;</i>&nbsp;点击关联商品</a>
            </div>
        [/#if]
    </div>
    [/#if]
    <header class="am-topbar am-topbar-fixed-bottom bg-lightslategray">
        <div class="weui_cell">
            <a href="javascript:" class="weui_btn weui_btn_warn" style="width:80%;">发布</a>
        </div>
    </header>

    <!-- BEGIN empty div for fixed ele -->
    <div class="empty-for-fixedbottom_tab"></div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script src="${base}/resources/common/js/wap-upload.js"></script>
<script>
    var ids = "";
    var images = '';
    $(function () {
        init();
        var compiler = Handlebars.compile($("#wap-list-item").html());

        $("#checkedOrder").html(compiler(""));
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });

    [#list ids as id]
        ids += "&ids=" +${id};
    [/#list]

        $("#headImg").change(function () {
            $("#headForm").submit();
        });

        $("#headIframe").load(function (text) {
            console.log(text);
            //var data = $.parseJSON(text);
        });


        $(".weui_btn.weui_btn_warn").on("click", function () {

            upload();

            return;


            var $content = $("#weuiTextarea").val();
            if ($content.replace(/(^\s*)|(\s*$)/g, "") == "" || $content.replace(/(^\s*)|(\s*$)/g, "") == null) {
                showToast2({content: '亲，您需要分享下您的购物体验！！！'});
                return;
            }

            ajaxGet({
                url: "${base}/app/member/contact/publish.jhtml?type=${type}" + ids + images,
                data: {
                    content: $content
                },
                success: function (data) {
                    $("#checkedOrder").html(compiler(data));
                    $(".lazy").picLazyLoad({threshold: 100, placeholder: '${base}/resources/weixin/images/header.png'});
                }
            });
        });

        /**检测评论字数*/
        $("#weuiTextarea").keyup(function () {
            var max = 200;
            var reg = /[^x00-xff]/gm;
            var str = $("#weuiTextarea").val().replace(reg, 'aa');//全换成单字节字符计算
            var v = $("#mathLength");
            if (str.length > max) {
                var n = 0, strr = [];
                for (var i = 0; i < $("#weuiTextarea").val().length; i++) {//得到50字节以内的字符
                    /[^x00-xff]/.test($("#weuiTextarea").val().charAt(i)) ? n += 2 : n += 1;
                    if (n > max)break;
                    strr.push($("#weuiTextarea").val().charAt(i));
                }
                $("#weuiTextarea").val(strr.join(''));
                v.html(0);
            } else {
                v.html(str.length);
            }
        });
        /**触发上传图片*/
        $("#upload_file").on("click", function () {
            $("#headImg").click();
        });
        bindListener('');
    });

    /**图片预览*/

    function getFullPath(file) {


        return;

        var photo_tpl = document.getElementById('photo_list');
        //var file=document.getElementById('headImg');
        var fileList = file.files;
        //a = fileList.length;
        for (var i = 0; i < fileList.length; i++) {
            images = '';
            photo_tpl.innerHTML += "<div style='float:left;margin-top:5px;margin-left:5px;position:relative'><img id='img" + i + "'><a name='rmlink' class='iconfont spanRadius'>&#xe626;</a></div>";
            var img = document.getElementById("img" + i);
            if (file.files && file.files[i]) {
                img.style.display = 'block';
                img.style.width = '77px';
                img.style.height = '77px';

                if (window.createObjectURL != undefined) { // basic
                    img.src = window.createObjectURL(file.files[i]);
                } else if (window.URL != undefined) { // mozilla(firefox)
                    img.src = window.URL.createObjectURL(file.files[i]);
                } else if (window.webkitURL != undefined) { // webkit or chrome
                    img.src = window.webkitURL.createObjectURL(file.files[i]);
                }
            }

        [#--console.log(i);--]
        [#--ajaxPost({--]
        [#--url:"${base}/app/file/upload_to_temp_image.jhtml?file="+fileList,--]
        [#--success:function(data){--]
        [#--console.log("1212    "+i);--]
        [#--images+="&images["+i+"].local="+data.data+"&images["+i+"].title=&images["+i+"].order=";--]

        [#--console.log("1212    "+images+"");--]
        [#--}--]
        [#--});--]
        [#--console.log(images);--]
        }
        bindListener('');
    }


    function bindListener(id) {
        $("a[name=rmlink]").unbind().click(function () {
            $(this).parent().remove();
        });

        if (id != '') {
            id = "&ids=" + id;
            ids = ids.replace(id, '');
        }
    }


    function upload() {
        var file1 = document.getElementById('photo_list');
        postFile(file1.files[0]);
    }

    function postFile(data) {
        //1.创建异步对象（小浏览器）
        var req = new XMLHttpRequest();

        //2.设置参数
        req.open("post", "${base}/app/file/upload_image.jhtml", true);

        //3.设置 请求 报文体 的 编码格式（设置为 表单默认编码格式）
        req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        // req.setRequestHeader("")
        //4.设置回调函数
        req.onreadystatechange = function () {
            //请求状态readyState=4准备就绪,服务器返回的状态码status=200接收成功
            if (req.readyState == 4 && req.status == 200) {

                if (req.responseText != "上传出错！") {
                    changeName(req.responseText);
                }
            }
        };

        //4.发送异步请求
        req.send(data);//post传参在此处
    }

    function changeName(name) {
        var file1 = document.getElementById("file1");
        var realname = file1.value;

        var req = new XMLHttpRequest();

        //如果名称遇到中文，请在此处转码,然后放入url中
        req.open("get", "C02FileUploadsByJs.ashx?name=" + name + "&realname=" + realname, true);

        req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        req.onreadystatechange = function () {
            //请求状态readyState=4准备就绪,服务器返回的状态码status=200接收成功
            if (req.readyState == 4 && req.status == 200) {
                document.getElementById("sp").innerHTML = req.responseText + "<br/>" + realname;
            }
        };

        //4.发送异步请求
        req.send();
    }

</script>
</body>
</html>