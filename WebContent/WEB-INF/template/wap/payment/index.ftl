<!DOCTYPE html>
<html lang="en">
<head>
    <title>支付</title>
[#include "/wap/include/resource-2.0.ftl"]
    <style type="text/css" media="screen">
        #actionSheet_wrap label.weui_check_label .weui_cell_bd > p {
            display: inline-block;
            vertical-align: middle;
            text-indent: 0.6em;
            font-size: 16px;
        }

        #actionSheet_wrap .weui_actionsheet_menu > .weui_cells {
            margin-top: 0;
        }

        #actionSheet_wrap .weui_actionsheet_menu > .weui_cells .weui_cells_title {
            background-color: #fff;
            /* border-bottom: 1px solid #0bb20c;*/
            margin: 0;
            color: #333;
            font-size: 17px;
            height: 2.6em;
            line-height: 2.6em;
        }

        #actionSheet_wrap .weui_actionsheet_menu > .weui_cells .weui_cells_radio:before {
            border-top: 1px solid #bfbfbf;
            color: #bfbfbf;
        }

        #select_payment .weui_cell_bd > p {
            display: inline-block;
            vertical-align: middle;
            padding-top: 0.3em;
            padding-left: 0.6em;
        }

        /* 密码输入框*/
        .pass_trueR {
            position: absolute;
            top: 0px;
            right: 0;
            z-index: 10;
            left: 50%;
            bottom: 0;

            sogaopacity: 0;
            /*
            overflow: hidden;*/
        }

        .pass_true {
            border: 1px solid #fafafc;
            color: #fafafc;
            font-size: 1px;
            outline: none;
            background: transparent;
            display: block;
            height: 4px;
            width: 4px;
            text-indent: -999em;
            /* margin-left: -100%; */
            /* width: 200%; */
            top: -5px;
            position: absolute;
            left: 0;
        }

        /* 密码占位框*/
        .passbox_sym {
            margin: 0.4em 0;
            display: -webkit-box;
            -webkit-box-orient: horizontal;
            -webkit-box-pack: center;
            -webkit-box-align: center;
            1 border: 1px solid #bdbdbd;
            border-radius: 2px;
        }

        .passbox_sym span {
            display: block;
            position: relative;
            height: 12.5vw;
            width: 12.5vw;
            border-right: 1px solid #d6d6d6;
            border-top: 1px solid #bdbdbd;
            border-bottom: 1px solid #bdbdbd;
        }

        .passbox_sym span.active:before {
            content: "";
            position: absolute;
            width: 2vw;
            height: 2vw;
            border-radius: 50%;
            background-color: #333;
            left: 50%;
            top: 50%;
            margin-top: -0.5vw;
            margin-left: -0.5vw;
        }

        .passbox_sym span:first-child {
            border-left: 1px solid #bdbdbd;
        }

        .passbox_sym span:first-child {
            border-right: 1px solid #bdbdbd;
        }

        .weui_dialog_ft.am-g {
            z-index: 20;
            background-color: #fafafc;
        }

        .weui_dialog {
            overflow: hidden;
        }

        #idNo, #idOk {
            z-index: 30;
            background-color: #fafafc;
            border-top: 1px solid #d5d5d6;
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/zepto-detect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/base64.js"></script>
</head>
<body ontouchstart>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="page">
        <div class="weui_cells">
            <div class="weui_cell">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>付款单号</p>
                </div>
                <div class="weui_cell_ft">${(payment.sn)!}</div>
            </div>
            <div class="weui_cell">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>费用详情</p>
                </div>
                <div class="weui_cell_ft">${(payment.memo)!}</div>
            </div>
            <div class="weui_cell">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>付款金额</p>
                </div>
                <div class="weui_cell_ft color-warn">${(payment.amount)!}</div>
            </div>
        </div>
        <div class="weui_cells weui_cells_access">
            <a class="weui_cell" href="javascript:;" id="select_payment">
                <div class="weui_cell_hd"><i class="iconfont font-large-4" style="color:#0bbd14;">&#xe65b;</i></div>
                <input type="hidden" name="paymentPlugId" id="paymentPlugId" value="weixinPayPlugin"/>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>微信支付</p>
                </div>
                <div class="weui_cell_ft"></div>
            </a>
        </div>
        <div class="spacing am-margin-top-sm">
            <a href="javascript:;" class="weui_btn weui_btn_warn" id="submit">安全付款</a>
        </div>
    </div>
    <!--BEGIN password-->
    <div class="weui_dialog_confirm" id="password1" style="display: none;">
        <div class="weui_mask"></div>
        <div class="weui_dialog">
            <div class="weui_dialog_hd" style="padding: 0.8em 0 0.5em;border-bottom: 1px solid #0bb20c;"><strong
                    class="weui_dialog_title">请输入支付密码</strong></div>
            <div class="weui_dialog_bd am-text-center color-black"
                 style="overflow: hidden;position: relative;padding:0 5vw;">
                <div class="am-text-danger text-xxl">
                    <i class="iconfont icon-rmb text-xxxl"></i>
                    <p style="margin-top: 0.8em;margin-bottom: 0.2em;font-size: 12px;">钱包付款</p>
                    <p style="font-size:30px">￥${(payment.amount)!}</p>
                </div>
                <div class="light-grey am-divider-default" style="border-width: 0;">
                </div>
                <div class="am-g am-divider-default am-fl am-padding-vertical-sm "
                     style="border-top-width: 0;padding-top:0;">
                    <div class="passbox_sym">
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>

                </div>
            </div>
            <div class="weui_dialog_ft am-g " style="z-index:20">
                <a href="javascript:;" class="weui_btn_dialog default" id="idNo">取消</a>
                <a href="javascript:;" class="weui_btn_dialog primary" id="idOk">确定</a>
                <div class="pass_trueR">
                    <input type="password" name="pass_true" maxlength="6" autocomplete="off" class="pass_true" value="">
                </div>

            </div>
        </div>
    </div>
    <!--END password-->
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        if ($.os.iphone) {
            //console.log($.os);
            //console.log($(".pass_trueR").length);
            if ($(".pass_trueR").length) {
                $(".pass_trueR").css({
                    opacity: 0
                }).find(".pass_true").css({
                    height: 0,
                    width: 0
                });
            }
        }
        config = {
            title: '支付方式',
            default: 'weixinPayPlugin',
            fn: fnselect,
            showmask: true,
            autoheight: true,
            removecancel: false,
            data: [
                {key: 'weixinPayPlugin', value: '微信钱包', icon: '&#xe65b;', color: '#0bbd14'},
                {key: 'balancePayPlugin', value: '账户钱包', icon: '&#xe674;', color: '#fa656b'}
            ]
        };

        function fnselect(key) {
            $("#paymentPlugId").val(key);
            for (var i = 0; i < config.data.length; i++) {
                if (config.data[i].key == key) {
                    //选中项回填
                    $("#select_payment").find(".weui_cell_bd").html('<p>' + config.data[i].value + '</p>');
                    $("#select_payment").find(".weui_cell_hd").html('<i class="iconfont font-large-4" style="color:' + config.data[i].color + '">' + config.data[i].icon + '</i>');
                }
            }
        }

        $("#select_payment").on("click", function () {
                    config.default = $("#paymentPlugId").val();
                    showActionSheet(config);
                }
        );

        function showPassword(fn, sn) {
            var $dialog = $('#password1');
            $dialog.show();
            $dialog.find(".pass_true").click();
            //$dialog.find(".pass_true").trigger("focus");
            $dialog.find('#password1 #idNo').unbind('click');
            $dialog.find('#password1 #idOk').unbind('click');
            $dialog.find('#password1 #idNo').bind('click', function () {
                $("#password1 .pass_true").val("");
                $("#password1 .passbox_sym span").removeClass('active');
                $dialog.hide();
            });
            $dialog.find('#password1 #idOk').bind('click', function () {
                var pwd = $('.pass_true').val();
                $dialog.hide();
                if ((pwd == null || pwd == undefined || pwd == '')) {
                    showDialog2('出错了', "请正确输入6位数密码");
                    return;
                }
                fn(pwd, sn);
            });
        }

        /* password input begin*/
        $("#password1 .pass_true").click(function () {
            $(this).focus();
        });
        $(".passbox_sym").on("tap click touchstart", function () {
            $("#password1 .pass_true").focus();

        });
        var pass_true_ctnlen = $("#password1 .pass_true").val().length;
        //var isQQx5=JSHelper.checkUA.isQQx5();
        //var pass_true_ctnlen_now=pass_true_ctnlen;
        $("#password1").on('input', '.pass_true', function (event) {
            event.preventDefault();
            //pass_true_ctnlen_now = $(this).val().length;
            //当密码框中字符增多
            if ($(this).val().length > pass_true_ctnlen) {
                $(".passbox_sym span").eq($(this).val().length - 1).addClass('active');
            } else {
                $(".passbox_sym span").eq($(this).val().length).removeClass('active');
            }
            //输入到最后一位字符
            if ($(this).val().length == 6) {
                $(this).trigger('blur');
                //触发确定按钮
                $("#password1 #idOk").click();
            }
            //事务处理结束之后对 pass_true_ctnlen重新赋值
            pass_true_ctnlen = $(this).val().length;
        });

        /* password input end*/
        function weixin(plugId, sn) {
            ajaxPost({
                url: '${base}/wap/payment/submit.jhtml?paymentPluginId=' + plugId + '&sn=' + sn,
                success: function (dataBlock) {
                    if (dataBlock.message.type != 'success') {
                        showDialog2('出错了', dataBlock.message.content);
                        return;
                    } else {
//                        alert(dataBlock.message.content);
//                        alert(dataBlock.data.package);
                        function onBridgeReady() {
                            var jsonObject = dataBlock.data;
                            WeixinJSBridge.invoke(
                                    'getBrandWCPayRequest', {
                                        "appId": jsonObject.appId,
                                        "timeStamp": jsonObject.timeStamp,
                                        "nonceStr": jsonObject.nonceStr,
                                        "package": jsonObject.package,
                                        "signType": "MD5",
                                        "paySign": jsonObject.paySign
                                    },
                                    function (res) {
                                        if (res.err_msg == "get_brand_wcpay_request:ok") {
                                            location.href = "${base}/wap/payment/notify/sync/" + sn + ".jhtml?result=0";
                                        } else {
                                            location.href = "${base}/wap/payment/notify/sync/" + sn + ".jhtml?result=1";
                                        }
                                    }
                            );
                        }

                        if (typeof WeixinJSBridge == "undefined") {
                            if (document.addEventListener) {
                                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                            } else if (document.attachEvent) {
                                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                            }
                        } else {
                            onBridgeReady();
                        }
                    }

                }
            });
        }

        function balance(password, sn) {
            ajaxPost({
                url: "${base}/common/public_key.jhtml",
                data: {local: true},
                success: function (data) {
                    var modulus = data.modulus;
                    var exponent = data.exponent;
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(modulus), b64tohex(exponent));
                    password = hex2b64(rsaKey.encrypt(password));
                    ajaxPost({
                        url: '${base}/wap/payment/submit.jhtml?paymentPluginId=balancePayPlugin&sn=' + sn,
                        data: {enPassword: password},
                        success: function (dataBlock) {
                            if(dataBlock.message.type=="warn"){
                                showDialog2("警告",dataBlock.message.content);
                                return false;
                            }
                            if (dataBlock.message.type != 'success') {
                                location.href = "${base}/wap/payment/notify/sync/" + sn + ".jhtml?result=1";
                            } else {
                                location.href = "${base}/wap/payment/notify/sync/" + sn + ".jhtml?result=0";
                            }
                        }
                    });
                }
            });
        }

        $("#submit").on("click", function () {
                    var plugId = $("#paymentPlugId").val();
                    if (plugId == 'weixinPayPlugin') {
                        weixin(plugId,${payment.sn});
                    } else {
                        showPassword(balance,${payment.sn});
                    }
                }
        );

    });
</script>
</body>
</html>
