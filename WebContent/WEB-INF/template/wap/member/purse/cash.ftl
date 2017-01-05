<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/base64.js"></script>
    <style type="text/css">
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

        /* 密码占位框,begin*/
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

        /* 密码占位框,end*/
    </style>
    <script type="text/x-handlebars-template" id="balace_item">
        <div class="weui_cells weui_cells_access marginTop0">
            <a class="weui_cell" href="javascript:;" style="background-color: #e53535;color: #ffffff;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>当前余额</p>
                    <p class="balance"><span class="small-font">￥</span><span class="font25">{{balance}}</span></p>
                </div>
            </a>
            <a class="weui_cell tx" href="javascript:;" style="background-color: #fe5b4a;color: #ffffff;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>可提金额</p>
                    <p class="withdrawbalance"><span class="small-font">￥</span><span
                            class="font25">{{withdrawBalance}}</span></p>
                </div>
            </a>
        </div>
        <div class="weui_cells weui_cells_access marginTop0">
            <a class="weui_cell" href="${base}/wap/member/purse/bank.jhtml?choose_bank='true'" style="padding: 0 15px;"
               id="bank" memberBankId="${(bank.id)!}">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>银行卡</p>
                </div>
                <div class="weui_cell_ft bank_info" style="font-size: 15px;color:#0088CC;">
                    <span id="bankName">${(bank.bankName)!}</span>&nbsp;&nbsp;&nbsp;<br>
                    <span id="bankInfo">[#if bank??]*** **** **** ${(bank.cardNo)!} ${(bank.account)!}[/#if]</span>
                </div>
            </a>
        </div>
        <div class="weui_cells weui_cells_form marginTop0">
            <div class="weui_cell">
                <div class="weui_cell_hd"><label class="">提现金额（￥）</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input oninput="amount(this)" onpropertychange="amount(this)" id="amount" class="weui_input"
                           type="number" step="any" placeholder="请输入提现金额" maxlength="18">
                </div>
            </div>
        </div>
        <div style="background-color: #ffffff;padding:0 15px;">
            <div style="height: 45px;">
                <div style="color:#0088CC;font-size: 16px;float:left;display: inline-block;width: 50%;">
                    单笔限额5万元
                </div>
                <div style="color:#ff0000;font-size: 16px;float:right;display: inline-block;text-align: right;width:50%;">
                    手续费：￥<span id="calculate">0.00</span>
                </div>
            </div>
            <div style="padding:10px 10%;">
                <a href="javascript:;" class="weui_btn weui_btn_warn"  onclick="submit()">提交</a>
            </div>
        </div>
    </script>
</head>
<body style="background-color: #ffffff;">
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page">
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
                    <p style="margin-top: 0.8em;margin-bottom: 0.2em;font-size: 12px;">提现金额</p>
                    <p style="font-size:30px" class="withdrawnumber">￥<b>10000</b></p>
                </div>
                <div class="light-grey am-divider-default" style="border-width: 0;">
                    <!-- 
                    <div class="am-g am-padding-vertical-xs am-fl">
                        <span class="am-fl">可用余额:10000
                        </span>
                        <i class="iconfont icon-right am-fr"></i>
                    </div>-->
                </div>
                <div class="am-g am-divider-default am-fl am-padding-vertical-sm "
                     style="border-top-width: 0;padding-top:0;">
                    <!-- <input type="password" maxlength="6" autocomplete="off" placeholder="" id="pay-input" class="pay-input">-->
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
    var _withdrawBalance = "";

    $(function () {
        init();
        //do something on iphone
        if ($.os.iphone) {
            if ($(".pass_trueR").length) {
                $(".pass_trueR").css({
                    opacity: 0
                }).find(".pass_true").css({
                    height: 0,
                    width: 0
                });
            }
        }
        var compiler = Handlebars.compile($("#balace_item").html());

        ajaxGet({
            url: '${base}/app/member/balance.jhtml',
            success: function (data) {
                _withdrawBalance = data.data.withdrawBalance;
                $(".page").append(compiler(data.data));
            [#if !bank??]
                ajaxGet({
                    url: '${base}/app/member/bank/list.jhtml',
                    success: function (data) {
                        if(data.data.length>0){
                            $("#bank").attr("memberBankId", data.data[0].id);
                            $("#bankName").text(data.data[0].bankName);
                            $("#bankInfo").text("*** **** **** " + data.data[0].cardNo + " " + data.data[0].account);
                        }
                    }
                });
            [/#if]

            }
        });
    });

    function submit() {
        if ($("#bank").attr("memberBankId") == "") {
            showDialog2("友情提示", "请选择银行卡！");
            return;
        }
        if ($("#amount").val().trim() == "") {
            showDialog2("友情提示", "请输入金额！");
            return;
        }

        if(parseFloat(_withdrawBalance)<parseFloat($("#amount").val().trim())){
            showDialog2("友情提示", "大于可提现金额，请重新输入！");
            return;
        }

        showPassword(function (password) {
            cash(password);
        });
    }
    //显示输入密码弹窗
    function showPassword(fn) {
        var $dialog = $('#password1');
        $dialog.find(".withdrawnumber b").text($("#amount").val().trim());
        //amount
        $dialog.show();
        $dialog.find(".pass_true").click();
        //$dialog.find(".pass_true").trigger("focus");
        $dialog.find('#password1 #idNo').unbind('click');
        $dialog.find('#password1 #idOk').unbind('click');
        $dialog.find('#password1 #idNo').bind('click', function () {
            $("#password1 .pass_true").val("");
            pass_true_ctnlen = 0;
            $("#password1 .passbox_sym span").removeClass('active');
            $dialog.hide();
        });
        $dialog.find('#password1 #idOk').bind('click', function () {
            var pwd = $('.pass_true').val();
            $dialog.hide();
            //
            if ((pwd == null || pwd == undefined || pwd == '')) {
                showDialog2('出错了', "请正确输入6位数密码");
                return;
            }
            $("#password1 .pass_true").val("");
            pass_true_ctnlen = 0;
            $("#password1 .passbox_sym span").removeClass('active');
            fn(pwd);
        });
    }
    /* password input begin*/

    setTimeout(function () {
        //pass_true_ctnlen=$("#password1 .pass_true").val().length;    
        //pass_true_ctnlen=0;
        $("#password1 .pass_true").val("");
        pass_true_ctnlen = 0;
        $("#password1 .passbox_sym span").removeClass('active');
        $("#password1 .pass_true").click(function () {
            $(this).focus();
        });
        $(".passbox_sym").on("tap click touchstart", function () {
            $("#password1 .pass_true").focus();

        });
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
    }, 500);

    /* password input end*/
    function cash(password) {
        if (password.trim() != "") {
            ajaxPost({
                url: "${base}/common/public_key.jhtml",
                data: {local: true},
                success: function (data) {
                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
                    var enPassword = hex2b64(rsaKey.encrypt(password.trim()));
                    ajaxPost({
                        url: '${base}/wap/member/cash/save.jhtml',
                        data: {
                            memberBankId: $("#bank").attr("memberBankId"),
                            amount: $("#amount").val().trim(),
                            enPassword: enPassword
                        },
                        success: function (message) {
                            if (message.message.type == "success") {
                                showToast(message.message);
                                window.setTimeout(function () {
                                    location.href = "${base}/wap/member/purse/index.jhtml";
                                }, 600);
                            } else {
                                showDialog2("提示", message.message.content);
                            }
                        }
                    });
                }
            });
        }
    }

    function amount(o) {
        if (num(o)) {
            $(document).unbind('ajaxBeforeSend');
            ajaxPost({
                url: "${base}/app/member/cash/calculate.jhtml",
                data: {
                    method: "fast",
                    amount: $(o).val()
                },
                success: function (data) {
                    if (data.data == 0) {
                        $("#calculate").text("0.00");
                    } else {
                        $("#calculate").text(data.data);
                    }
                }
            });
        } else {
            $("#calculate").text("0.00");
        }
    }

    function num(o) {
        var val = $(o).val();
        val = val.replace(/[^\d\.]/g, '');
        if (val == "") {
            $(o).val("");
            return false;
        }
        var arr = val.split(".");
        if (arr.length >= 2) {
            var newVal = "";
            $.each(arr, function (k, v) {
                if (k == 0) {
                    if (v == "") {
                        newVal = "0.";
                    } else {
                        newVal += Number(v) + ".";
                    }
                } else {
                    newVal += v;
                }
            });
            $(o).val(newVal);
            return true;
        }
        $(o).val(Number(val));
        return true;
    }

</script>

</body>
</html>