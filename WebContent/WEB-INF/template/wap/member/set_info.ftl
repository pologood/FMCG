<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <title>个人信息</title>
    <style type="text/css">
        body {
            font-family: "Microsoft YaHei";
            background-color: #E9E9E9;
        }

        .weui_cells:before {
            border-top: 0px;
            left: 0px;
        }

        .weui_cells:after {
            border-bottom: 0px;
        }

        .weui_cell:before #selectImage {
            width: 0px;
        }
    </style>

    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <script src="${base}/resources/common/js/wap-upload.js"></script>

    <script type="text/javascript">
        function changeHeadImg(file, id, img) {
            preivew(file, img);
            if ($("#" + id).val().length > 0) {
                $("#headForm").submit();
            }
        }
    </script>

    <script type="text/x-handlebars-template" id="set_info_tpl">
        <form id="headForm" action="setPhoto.jhtml" method="post" enctype="multipart/form-data">
            <input type="file" id="headImg" name="headImg" style="display:none;" accept="image/*"
                   onchange="changeHeadImg(this,'headImg','headImgShow')">
        </form>
        <div class="weui_cells weui_cells_access" style="margin-top:20px;line-height:50px;">

            <a class="weui_cell" href="javascript:;" id="selectImage" style="">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>头像</p>
                </div>
                <div class="weui_cell_ft" id="selectImage">
                    <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                         data-original="{{headImg}}" alt="nopicture"
                         style="width:50px; height:50px; border-radius:50%; overflow:hidden;" id="headImgShow"/>
                </div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top:20px;">
            <a class="weui_cell" href="${base}/wap/member/set_name.jhtml" id="name_a">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>姓名</p>
                </div>
                <div class="weui_cell_ft" id="name">{{name}}</div>
            </a>
            <a class="weui_cell" href="${base}/wap/member/set_gender.jhtml">
                <div class="weui_cell_bd weui_cell_primary" id="gender">
                    <p>性别</p>
                </div>
                {{#ifCond gender "male"}}
                <div class="weui_cell_ft">男</div>
                {{/ifCond}}
                {{#ifCond gender "female"}}
                <div class="weui_cell_ft">女</div>
                {{/ifCond}}
            </a>
            <a class="weui_cell" href="javascript:;" id="qr_code">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>二维码</p>
                </div>
                <div class="weui_cell_ft"><i class="iconfont">&#xe62e;</i></div>
            </a>
        </div>

        <div class="weui_cells weui_cells_access" style="margin-top:20px;">
            <a class="weui_cell" {{#ifCond bindMobile 'none'}} href="${base}/wap/bound/indexNew.jhtml" {{/ifCond}}>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>绑定手机</p>
                </div>
                {{#ifCond bindMobile "none"}}
                <div class="weui_cell_ft" style="color:#CB0101;">去绑定</div>
                {{/ifCond}}
                {{#ifCond bindMobile "binded"}}
                <div style="margin-right:10px;color:#888;">已绑定</div>
                {{/ifCond}}
                {{#ifCond bindMobile "unbind"}}
                <div class="weui_cell_ft" style="color:#CB0101;">已解绑</div>
                {{/ifCond}}
            </a>
            <a class="weui_cell" href="${base}/wap/member/set_auth_condition.jhtml" id="auth">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>实名认证</p>
                </div>

                {{#ifCond authStatus "success"}}
                <div style="margin-right:10px;color:#888;">
                    已认证
                </div>
                {{/ifCond}}

                {{#ifCond authStatus "wait"}}
                <div style="margin-right:10px;color:#888;">
                    等待认证
                </div>
                {{/ifCond}}

                {{#ifCond authStatus "fail"}}
                <div class="weui_cell_ft">
                    认证失败
                </div>
                {{/ifCond}}

                {{#ifCond authStatus "none"}}
                <div class="weui_cell_ft">
                    未认证
                </div>
                {{/ifCond}}

            </a>
            <a class="weui_cell" href="${base}/wap/member/set_login_code.jhtml">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>登录密码</p>
                </div>
                <div class="weui_cell_ft" style="color:#CB0101;">重置密码</div>
            </a>
            <a class="weui_cell" href="${base}/wap/member/set_payment_code.jhtml">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>支付密码</p>
                </div>
                <div class="weui_cell_ft" style="color:#CB0101;">重置密码</div>
            </a>
        </div>

        <div class="weixin-tip2 MBSI">
            <div class="myqrcode MBSI">
                <div class="hd">
                    <div style="width:20%;float:left;display:inline;">
                        <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-head.png"
                             data-original="{{headImg}}" alt="nopicture" style="width:100%;border-radius:3px;">
                    </div>
                    <div style="width:80%;float:left;display:inline;">
                        <div style="width:100%;margin-left:10px;">
                        <span style="font-size:20px;">{{name}}
                            <i class="iconfont">&#xe624;</i>
                        </span>
                            <div style="float:right;">
                                <i class="iconfont" id="qr_img_close">&#xe68a;</i>
                            </div>
                        </div>
                        <div style="margin-left:10px;">{{area.fullName}}</div>
                    </div>
                </div>

                <div class="bd" id="code">
                    <img src="" alt="noPicture" style="width:80%;margin:10%;" id="qr_img">
                </div>

                <div class="ft">
                    <p style="font-size:15px;line-height:17px;text-align:center;">扫描上面的二维码，即可加我微信</p>
                </div>
            </div>
        </div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container" style="height:100%;">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page" style="background-color:#E9E9E9;height:100%;">

        <div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();

        ajaxGet({
            url: "${base}/app/member/view.jhtml",
            success: function (data) {
                compiler = Handlebars.compile($("#set_info_tpl").html());
                $(".page").html(compiler(data.data));
                $(".lazy").picLazyLoad({
                    threshold: 100,
                    placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-head.png'
                });
                if (data.data.authStatus == "wait" || data.data.authStatus == "success") {
                    $("#name").removeAttr("class").css("color", "#888");
                    $("#name_a").attr("href", "javascript:;");
                    $("#auth").attr("href", "javascript:;");
                }

                $("#selectImage").on('click', function () {
                    $("#headImg").click();
                });
                $(".weixin-tip2,#qr_img_close").on('click', function (event) {
                    $(".weixin-tip2").hide();
                });
                $(".weixin-tip2 .myqrcode").on('click', function (event) {
                    event.stopPropagation();
                });
                $("#qr_img_close").on("click", function () {

                });
                $("#qr_code").on("click", function () {
                    $(".weixin-tip2").show();
                    $.ajax({
                        url: "http://api.wwei.cn/wwei.html?apikey=20160806215020&data=${url}",
                        type: 'post',
                        dataType: "jsonp",
                        jsonp: "callback",
                        success: function (result) {
                            closeWaitLoadingToast();
                            $("#qr_img").attr("src", result.data.qr_filepath);
                        },
                        error: function (e) {
                            alert(e);
                        }
                    });
                });

            }
        });

        Handlebars.registerHelper('ifCond', function (v1, v2, options) {
            if (v1 === v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        });
    });
</script>
[#include "/wap/include/footer.ftl" /]
</body>
</html>
