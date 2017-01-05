<!DOCTYPE html>
<html>

<head>
[#include "/app/include/resource-3.0-part1.ftl"/]
    <link rel="stylesheet" href="${base}/resources/app/3.0/css/static.css">
    <link rel="stylesheet" href="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/app/3.0/css/pluginrevise.css">
    <title>优惠买单</title>
</head>

<body class="bg-00">
<div class="buyreduceRN TT-BY">
    <div class="weui_cell total_consumption bg-05">
        <div class="weui_cell_hd ft-bs15 clr-grey01">
            消费金额:
        </div>
        <div class="weui_cell_bd weui_cell_primary ft-bs15">
            <input type="number" placeholder="输入到店消费总额">
        </div>
    </div>
    <div class="merchant_coupons bg-05">
        <div class="weui_cell merchant_coupons_tit TT-BY">
            <div class="weui_cell_bd weui_cell_primary">
                <span class="clr-red05 ft-bs05">商家优惠券</span>
            </div>
            <div class="weui_cell_ft">
                <i class="iconfont clr-grey11">&#xe635;</i>
            </div>
        </div>
        <div class="coupon TT-BY bg-09 hidden">
            <div class="weui_cell coupon_cont">
                <div class="weui_cell_bd weui_cell_primary">
                    <div class="money">
                        <span class="section1 ft-bs7 value" id="couponAmount">￥-</span>
                            <span class="">
                                <p class="section1 ft-bs09" id="couponName">-</p>
                                <p class="section2 ft-bs15">优惠券</p>
                            </span>
                    </div>
                    <div class="time ft-bs0 clr-grey03" id="couponTime">
                        使用期限：-
                    </div>
                </div>
                <div class="weui_cell_ft">
                    <i class="iconfont section3 clr-red05">&#xe652;</i>
                </div>
            </div>
        </div>
        <div class="no_coupon TT-BY bg-09 weui_cell">
            <div class="weui_cell_hd">
                <img src="${base}/resources/app/3.0/image/tenant/no_coupon.png" alt="">
            </div>
            <div class="weui_cell_bd weui_cell_primary clr-grey03">
                <p class="ft-bs1">更多优惠</p>
                <p class="ft-bs4">敬请期待</p>
            </div>
        </div>
    </div>
    <div class="delivery_center weui_cell bg-05 hidden">
        <div class="weui_cell_hd ft-bs15">
            <span class="clr-grey01">随机立减：</span>
        </div>
        <div class="weui_cell_bd weui_cell_primary ft-bs15">
            <span class="section3">满200减5元</span>
        </div>
        <div class="weui_cell_ft ft-bs15">
            <span class="section1">￥0</span>
            <i class="iconfont">&#xe635;</i>
        </div>
    </div>
    <div class="pay_money weui_cell  ft-bs15 bg-05">
        <div class="weui_cell_bd weui_cell_primary">
            <span class="clr-grey01">实付金额：</span>
        </div>
        <div class="weui_cell_ft" id="newAmount">
            <span class="section1" id="amount">￥0</span>
        </div>
    </div>
    <div class="buyreduceRN_ft bg-05">
        <div class="confirm_check section4 clr-grey08 ft-bs15">确认买单</div>
        <div class="buyreduceRN_business TT-BY weui_cell">
            <div class="weui_cell_bd weui_cell_primary">
                <p class="clr-grey01 ft-bs15">${tenant.name}</p>
                <p class="clr-grey02 ft-bs0" id="tenantAddress">${tenant.address}</p>
            </div>
            <div class="weui_cell_ft">
                <i class="iconfont clr-grey11 ft-bs15">&#xe635;</i>
            </div>
        </div>
    </div>

    <form id="indexForm" action="" method="post" style="display: none;">
        <input name="sn" id="sn">
    </form>
</div>
[#-- sheet:choose_coupon--]
<div class="sheet-choose_coupon"></div>
[#-- sheet:delivery_center--]
<div class="sheet-delivery_center"></div>
[#-- TXT:choose_coupon,选择优惠券--]
<script type="text/html" id="tpl_choose_coupon">
    [#--
    [#list coupons as coupon]
    ${coupon.name}
    [/#list]--]
    <ul class="more_coupon_cont TT-BY bg-05">
    [#list coupons as coupon]
        <li>
            <p class="ft-bs0 clr-red05"><span class="ft-bs15">${coupon.amount}</span>元</p>
            <p class="ft-bs0 clr-grey01">订单满${coupon.minimumPrice}元使用（不含邮费）</p>
            <p class="ft-bs09 clr-grey03">
                使用日期：${coupon.startDate?string("yyyy.MM.dd")}-${coupon.endDate?string("yyyy.MM.dd")}</p>
        </li>
    [/#list]
    </ul>
</script>
[#-- TXT:delivery_center,线下门店--]
<script type="text/html" id="tpl_delivery_center">
    [#--
    [#list tenant.deliveryCenters as deliveryCenter]
    ${deliveryCenter.name}-${deliveryCenter.address}
    [/#list]
     --]
    <div class="weui_actionsheet addrits TT-BY weui_actionsheet_toggle" id="weui_actionsheet">
        <div class="weui_actionsheet_menu">
            <div class="weui_cells">
                <div class="weui_cell weui_cells_title am-text-center addrits-title TT-BY">
                    <div class="weui_cell_bd weui_cell_primary"><span>线下门店</span></div>
                </div>
                <div class="weui_cells weui_cells_radio addrits-ctn TT-BY">
                [#list tenant.deliveryCenters as deliveryCenter]
                    <div class="weui_cell active" storeid="88">
                        <div class="weui_cell_hd"></div>
                        <div class="weui_cell_bd weui_cell_primary"><h3 class="store-name">${deliveryCenter.name}</h3>
                            <p class="store-address">${deliveryCenter.address}</p></div>
                        <div class="weui_cell_ft"><i class="iconfont icon_check"></i></div>
                    </div>
                [/#list]
                </div>
            </div>
        </div>
        <div class="weui_actionsheet_action" style="display: none;">
            <div class="weui_actionsheet_cell bg-safe color-revert" id="actionsheet_cancel">取消</div>
        </div>
    </div>
</script>
<!-- BOTTOM SCRIPT-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/vendor-32a13b8d79.min.js"></script>
<!-- JS:lib-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/lib.js"></script>
<!-- JS:component-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/whostrap.js"></script>
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.js"></script>
<script type="text/javascript" src="${base}/resources/app/3.0/js/plugin/islider/iSlider.min.js"></script>
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; // ${tscpath}
    // 基准路径base
    _TH_.base = "${base}"; //tiaohuo
    //其他数据
    _TH_.datalists = []; //${datalists}

    _TH_.tenantid =${id};

    $(function () {

        var amount = '${amount}', deliveryCenterId;

//        $.ajax({
//                    url: _TH_.base + "/app/pay/bill/deliver/certer.jhtml",
//                    type:"POST",
//                    data: {
//                        id: _TH_.tenantid,
//                        lat: 31.820587,
//                        lng: 117.227239
//                    }
//                }
//        ).done(function (data) {
//                    if (data.message.type == "success") {
//                        deliveryCenterId= data.data.id;
//                        $("#tenantAddress").html(data.data.address);
//                    } else {
//                        $(".ONCET").tip("addTask", {
//                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
//                            txt: "data.message.content"
//                        });
//                    }
//                }
//        ).fail(function () {
//                    console.log("error");
//                }
//        );

        _wxSDK.initInterface($script, {
            afterOnMenuShare: function () {
                _wxSDK.getLocation()
                        .done(function (lat, lng) {
                            $.ajax({
                                        url: _TH_.base + "/app/pay/bill/deliver/certer.jhtml",
                                        type: "POST",
                                        data: {
                                            id: _TH_.tenantid,
                                            lat: lat,
                                            lng: lng
                                        }
                                    }
                            ).done(function (data) {
                                        if (data.message.type == "success") {
                                            deliveryCenterId = data.data.id;
                                            $("#tenantAddress").html(data.data.address);
                                        } else {
                                            $(".ONCET").tip("addTask", {
                                                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                                txt: "data.message.content"
                                            });
                                        }
                                    }
                            ).fail(function () {
                                        $(".ONCET").tip("addTask", {
                                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                            txt: "获取地址位置失败"
                                        });
                                    }
                            );
                        })
                        .fail(function () {
                                    $(".ONCET").tip("addTask", {
                                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                        txt: "获取地址位置失败"
                                    });
                                }
                        );
            }
        });

        $(".sheet-choose_coupon").actionSheet({
            plugintype: "actionsheet",
            tpldatas: {
                hooks: {
                    classes: {
                        weui_actionsheet_ctn: "thetypes PT-DY"
                    }
                },
                Ctrls: {
                    has_action_cancel: false
                }
            }
        });
        //initialized:WHT:actionsheet
        $(".sheet-choose_coupon").on("initialized:WHT:actionsheet", function (event) {
            //console.log("初始化完毕");
            //填充html内容
            $(".sheet-choose_coupon").actionSheet("fillContent", $("#tpl_choose_coupon").html());
        });

        $(".merchant_coupons_tit").on("click", function () {
            $(".sheet-choose_coupon").actionSheet("show");
        });

        $(".sheet-delivery_center").actionSheet({
            plugintype: "actionsheet",
            tpldatas: {
                hooks: {
                    classes: {
                        weui_actionsheet_ctn: "thetypes PT-DY"
                    }
                },
                Ctrls: {
                    has_action_cancel: false
                }
            }
        });
        //initialized:WHT:actionsheet
        $(".sheet-delivery_center").on("initialized:WHT:actionsheet", function (event) {
            //console.log("初始化完毕");
            //填充html内容
            $(".sheet-delivery_center").actionSheet("fillContent", $("#tpl_delivery_center").html());
        });

        $(".buyreduceRN_business").on("click", function () {
            $(".sheet-delivery_center").actionSheet("show");
        });

        $("input[type='number']").keyup(function () {
            $(".coupon_cont").removeClass("active");
            $("#newAmount").removeClass("active");
            $(".confirm_check").removeClass("active");
            $(".coupon").addClass("hidden");
            $(".no_coupon").removeClass("hidden");
            $(".confirm_check").html("网络计算中...");
            $("#amount").html("网络计算中...");
            amount = $(this).val().trim();
            if (amount != null && amount != "") {
                if (amount > 0) {
                    $.ajax({
                                url: _TH_.base + "/app/pay/bill/get/amount.jhtml",
                                type: "POST",
                                data: {
                                    id: _TH_.tenantid,
                                    amount: amount
                                }
                            }
                    ).done(function (data) {
                                if (data.message.type == "success") {
                                    if (data.data.couponAmount > 0) {
                                        $(".coupon_cont").addClass("active");
                                        $("#couponAmount").html("￥" + data.data.couponAmount);
                                        $("#couponName").html("满" + data.data.couponMinimumPrice + "使用");
                                        $("#couponTime").html("使用期限：" + data.data.couponTime);
                                        $(".coupon").removeClass("hidden");
                                        $(".no_coupon").addClass("hidden");
                                    } else {
                                        $(".coupon").addClass("hidden");
                                        $(".no_coupon").removeClass("hidden");
                                    }
                                    $(".confirm_check").html("确认买单￥" + data.data.amount);
                                    $("#amount").html("￥" + data.data.amount);
                                    $("#newAmount").addClass("active");
                                    $(".confirm_check").addClass("active");
                                } else {
                                    $(".ONCET").tip("addTask", {
                                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                        txt: "data.message.content"
                                    });
                                    $(".coupon_cont").removeClass("active");
                                    $("#newAmount").removeClass("active");
                                    $(".confirm_check").removeClass("active");
                                }
                            }
                    ).fail(function () {
                                console.log("error");
                            }
                    );
                } else {
                    $(".coupon_cont").removeClass("active");
                    $("#newAmount").removeClass("active");
                    $(".confirm_check").removeClass("active");
                    $(".coupon").addClass("hidden");
                    $(".no_coupon").removeClass("hidden");
                    $(".confirm_check").html("确认买单");
                    $("#amount").html("￥0");
                }
            } else {
                $(".coupon_cont").removeClass("active");
                $("#newAmount").removeClass("active");
                $(".confirm_check").removeClass("active");
                $(".coupon").addClass("hidden");
                $(".no_coupon").removeClass("hidden");
                $(".confirm_check").html("确认买单");
                $("#amount").html("￥0");
            }
        });

        $(".confirm_check").on("click", function () {
            if ($(".confirm_check").hasClass("active")) {

                if ("${(member)!}" == "") {
                    location.href = "${base}/app/bound/indexNew.jhtml?redirectUrl=${base}/app/pay/bill/buyreduce/${id}.jhtml?amount=" + amount;
                    return;
                }

                if (amount > 0) {
                    $.ajax({
                                url: _TH_.base + "/app/member/pay/bill/get/amount.jhtml",
                                type: "POST",
                                data: {
                                    id: _TH_.tenantid,
                                    amount: amount,
                                    deliveryCenterId: deliveryCenterId
                                }
                            }
                    ).done(function (data) {
                                if (data.message.type == "success") {
                                    if (data.data.amount == 0) {
                                        location.href = "${base}/app/payment/notify/sync/" + data.data.sn + ".jhtml?result=0";
                                    } else {
                                        $("#sn").val(data.data.sn);
                                        $("#indexForm").attr("action", "${base}/app/payment/index.jhtml");
                                        $("#indexForm").submit();
                                    }

                                } else {
                                    $(".ONCET").tip("addTask", {
                                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                        txt: "data.message.content"
                                    });
                                }
                            }
                    ).fail(function () {
                                $(".ONCET").tip("addTask", {
                                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                    txt: "数据传输失败"
                                });
                            }
                    );
                }
            }
        });

        if (amount != '') {
            $.ajax({
                        url: _TH_.base + "/app/pay/bill/get/amount.jhtml",
                        type: "POST",
                        data: {
                            id: _TH_.tenantid,
                            amount: amount
                        }
                    }
            ).done(function (data) {
                        if (data.message.type == "success") {
                            if (data.message.type == "success") {
                                $("input[type='number']").val(amount);
                                couponId = data.data.couponId;
                                if (data.data.couponAmount > 0) {
                                    $(".coupon_cont").addClass("active");
                                    $("#couponAmount").html("￥" + data.data.couponAmount);
                                    $("#couponName").html("满" + data.data.couponMinimumPrice + "使用");
                                    $("#couponTime").html("使用期限：" + data.data.couponTime);
                                    $(".coupon").removeClass("hidden");
                                    $(".no_coupon").addClass("hidden");
                                } else {
                                    $(".coupon").addClass("hidden");
                                    $(".no_coupon").removeClass("hidden");
                                }
                                $(".confirm_check").html("确认买单￥" + data.data.amount);
                                $("#amount").html("￥" + data.data.amount);
                                $("#newAmount").addClass("active");
                                $(".confirm_check").addClass("active");

                            } else {
                                $(".ONCET").tip("addTask", {
                                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                    txt: "data.message.content"
                                });
                                $(".coupon_cont").removeClass("active");
                                $("#newAmount").removeClass("active");
                                $(".confirm_check").removeClass("active");
                            }

                        } else {
                            $(".ONCET").tip("addTask", {
                                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                txt: "data.message.content"
                            });
                            $(".coupon_cont").removeClass("active");
                            $("#newAmount").removeClass("active");
                            $(".confirm_check").removeClass("active");
                        }
                    }
            ).fail(function () {
                        console.log("error");
                    }
            );
        }
    });

</script>
<!-- JS:page-->
</body>

</html>
