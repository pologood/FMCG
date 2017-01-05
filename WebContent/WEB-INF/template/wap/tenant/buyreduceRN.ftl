<!DOCTYPE html>
<html>
<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/static.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.css">
    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/pluginrevise.css">
    <title>优惠买单</title>
</head>
<body class="bg-00">
<div class="buyreduceRN TT-BY">
    <div class="weui_cell total_consumption bg-05">
        <div class="weui_cell_hd clr-grey01">
            消费金额:
        </div>
        <div class="weui_cell_bd weui_cell_primary">
            <input type="number" id="amount" placeholder="输入到店消费总额">
        </div>
    </div>
    <div class="not_participate TT-BY bg-05">
        <div class="weui_cell ck_box">
            <div class="weui_cell_hd">
                <input hidden type="checkbox" id="ckBoxMoney" disabled>
                <label id="SR_ico" class="iconfont" for="ckBoxMoney">&#xe654;</label>
            </div>
            <div class="weui_cell_bd weui_cell_primary">
                输入不参与优惠金额
            </div>
        </div>
        <div class="SR_bg hidden"></div>
        <div class="weui_cell ck_box_money hidden">
            <div class="weui_cell_hd clr-grey01">
                不参与优惠金额:
            </div>
            <div class="weui_cell_bd weui_cell_primary">
                <input type="number" id="noAmount" placeholder="询问商家收银员后输入">
            </div>
        </div>
    </div>
    <div class="merchant_coupons bg-05 ${isCoupon}">
        <div class="weui_cell merchant_coupons_tit TT-BY">
            <div class="weui_cell_bd weui_cell_primary">
                <span class="clr-red05">商家优惠券</span>
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
        <div class="no_coupon TT-BY bg-09 weui_cell ${isCoupon}">
            <div class="weui_cell_hd">
                <img src="${base}/resources/wap/3.0/image/tenant/no_coupon.png" alt="">
            </div>
            <div class="weui_cell_bd weui_cell_primary clr-grey03">
                <p class="ft-bs1">更多优惠</p>
                <p class="ft-bs4">敬请期待</p>
            </div>
        </div>
    </div>
    <div class="business_discount bg-05 ${isBillDiscount}">
        <div class="bd_tit">
            <span class="line"></span>
            <span class="clr-red05">商家折扣</span>
            <span class="line"></span>
        </div>
        <div class="bd_cont weui_cell">
            <div class="weui_cell_bd weui_cell_primary">
                <p class="ft-bs15 clr-grey02">${(promotionName)!}<span class="section4 hidden">返10%</span></p>
                <p class="ft-bs0 clr-grey03">${(promotionTime)!}</p>
            </div>
            <div class="weui_cell_ft">
                <span class="ft-bs15 clr-red05" id="promotionAmount">-￥${(promotionAmount)!"0"}</span>
            </div>
        </div>
    </div>
    <div class="delivery_center weui_cell bg-05 ${isActivityTenant}">
        <div class="weui_cell_hd ft-bs15">
            <span class="clr-grey01">${(activityName)!}：</span>
        </div>
        <div class="weui_cell_bd weui_cell_primary ft-bs15">
            <span class="section3" id="activityName"></span>
        </div>
        <div class="weui_cell_ft ft-bs15">
            <span class="section1" id="activityCouponAmount">￥0</span>
        [#--<i class="iconfont">&#xe635;</i>--]
        </div>
    </div>
    <div class="pay_money weui_cell bg-05">
        <div class="weui_cell_bd weui_cell_primary">
            <span class="clr-grey01">实付金额：</span>
        </div>
        <div class="weui_cell_ft" id="newAmount">
            <span class="section1" id="_amount">￥0</span>
        </div>
    </div>
    <div class="buyreduceRN_ft">
        <div class="confirm_check section4 clr-grey08">确认买单</div>
        <div class="buyreduceRN_business TT-BY">
            <div class="weui_cell_bd weui_cell_primary">
                <p class="clr-grey01">${tenant.name}</p>
                <p class="clr-grey02" id="tenantAddress">${tenant.address}</p>
            </div>
            <div class="weui_cell_ft">
                <i class="iconfont clr-grey11">&#xe65f;</i>
            </div>
        </div>
    </div>
    <form id="indexForm" action="" method="post" style="display: none;">
        <input name="sn" id="sn">
    </form>
</div>
[#-- sheet:choose_coupon--]
<div class="sheet-choose_coupon"></div>
[#-- sheet:choose_coupon--]
<div class="sheet-choose_activity_coupon"></div>
[#-- sheet:delivery_center--]
<div class="sheet-delivery_center"></div>
[#-- TXT:choose_coupon,选择优惠券--]
<script type="text/html" id="tpl_choose_coupon">
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
[#-- TXT:choose_coupon,选择优惠券--]
<script type="text/html" id="tpl_choose_activity_coupon">
    <ul class="more_coupon_cont TT-BY bg-05">
    [#list activityCoupons as activityCoupon]
        <li>
            <p class="ft-bs0 clr-red05"><span class="ft-bs15">${activityCoupon.amount}</span>元</p>
            <p class="ft-bs0 clr-grey01">订单满${activityCoupon.minimumPrice}元使用（不含邮费）</p>
            <p class="ft-bs09 clr-grey03">
                使用日期：${activityCoupon.startDate?string("yyyy.MM.dd")}-${activityCoupon.endDate?string("yyyy.MM.dd")}</p>
        </li>
    [/#list]
    </ul>
</script>
[#-- TXT:delivery_center,线下门店--]
<script type="text/html" id="tpl_delivery_center">
    <div class="weui_actionsheet addrits TT-BY weui_actionsheet_toggle" id="weui_actionsheet">
        <div class="weui_actionsheet_menu">
            <div class="weui_cells">
                <div class="weui_cell weui_cells_title am-text-center addrits-title TT-BY">
                    <div class="weui_cell_bd weui_cell_primary"><span>线下门店</span></div>
                </div>
                <div class="weui_cells weui_cells_radio addrits-ctn TT-BY">
                [#list tenant.deliveryCenters as deliveryCenter]
                    <div class="weui_cell" storeid="${deliveryCenter.id}">
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
<script type="text/javascript" src="${base}/resources/wap/3.0/js/vendor.js"></script>
<!-- JS:lib-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/lib.js"></script>
<!-- JS:component-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/whostrap.js"></script>
<!-- JS:plugin-->
<script type="text/javascript" src="${base}/resources/wap/3.0/js/plugin/islider/iSlider.min.js"></script>
<!-- JS:receiver-->
<script type="text/javascript">
    // receive remote data here first
    // 后台刷入数据使用前先存储到_TH_
    var _TH_ = {};
    // 后台模板路径
    _TH_.tscpath = "${tscpath}"; //
    // 基准路径base
    _TH_.base = "${base}"; //tiaohuo
    //其他数据
    _TH_.tenantid =${id};
</script>
<!-- JS:page-->
<script type="text/javascript">
    $(function () {
        var deliveryCenterId, timeout;

        var payBillGetAmount = function (amount, noAmount) {

            $(".coupon_cont").removeClass("active");
            $("#newAmount").removeClass("active");
            $(".coupon").addClass("hidden");
            $(".no_coupon").removeClass("hidden");
            $(".confirm_check").html("网络计算中...");

            if ((amount != null || amount != "") && parseFloat(amount) > 0) {
                $.ajax({
                    url: _TH_.base + "/wap/pay/bill/get/amount.jhtml",
                    type: "POST",
                    data: {
                        id: _TH_.tenantid,
                        amount: amount,
                        noAmount: noAmount
                    }
                }).done(function (data) {
                    if (data.message.type == "success") {
                        if (data.data.type=="coupon") {
                            $(".coupon_cont").addClass("active");
                            $("#couponAmount").html("￥" + data.data.couponAmount);
                            $("#couponName").html("满" + data.data.couponMinimumPrice + "使用");
                            $("#couponTime").html("使用期限：" + data.data.couponTime);
                            $(".coupon").removeClass("hidden");
                            $(".no_coupon").addClass("hidden");
                        } else {
                            $(".coupon").addClass("hidden");
                            $(".no_coupon").removeClass("hidden");
                            $("#promotionAmount").html("-￥" + data.data.promotionAmount);
                        }
                        $(".confirm_check").html("确认买单￥" + data.data.amount);
                        $("#_amount").html("￥" + data.data.amount);
                        $("#newAmount").addClass("active");
                        $(".confirm_check").addClass("active");

                        if ("${isActivityTenant}" == "") {
                            $("#activityCouponAmount").html("-￥" + data.data.activityCouponAmount);
                        }
                    } else {
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "data.message.content"
                        });
                        $(".coupon_cont").removeClass("active");
                        $("#newAmount").removeClass("active");
                        $(".confirm_check").removeClass("active");

                        if ("${isActivityTenant}" == "") {
                            $("#activityCouponAmount").html("-￥0");
                        }
                    }
                }).fail(function () {
                    clearTimeout(timeout);
                    $(".ONCET").tip("addTask", {
                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                        txt: "网络异常，请检查您的网络环境"
                    });

                    $("#promotionAmount").html("-￥0");
                    $(".coupon_cont").removeClass("active");
                    $("#newAmount").removeClass("active");
                    $(".confirm_check").removeClass("active");
                    $(".coupon").addClass("hidden");
                    $(".no_coupon").removeClass("hidden");
                    $(".confirm_check").html("确认买单");
                    $("#_amount").html("￥0");
                    $("#ckBoxMoney").attr("disabled", "disabled");

                    if ("${isActivityTenant}" == "") {
                        $("#activityCouponAmount").html("-￥0");
                    }

                    $(".ck_box_money").addClass("hidden");
                    $("#SR_ico").removeClass("SR101");
                    $(".SR_bg").addClass("hidden");
                    $("#noAmount").val("");
                    $("#amount").val("");
                });
            } else {
                $("#promotionAmount").html("-￥0");
                $(".coupon_cont").removeClass("active");
                $("#newAmount").removeClass("active");
                $(".confirm_check").removeClass("active");
                $(".coupon").addClass("hidden");
                $(".no_coupon").removeClass("hidden");
                $(".confirm_check").html("确认买单");
                $("#_amount").html("￥0");
                $("#ckBoxMoney").attr("disabled", "disabled");

                if ("${isActivityTenant}" == "") {
                    $("#activityCouponAmount").html("-￥0");
                }

                $(".ck_box_money").addClass("hidden");
                $("#SR_ico").removeClass("SR101");
                $(".SR_bg").addClass("hidden");
                $("#noAmount").val("");
            }
        };

        _wxSDK.initInterface($script, {
            afterOnMenuShare: function () {
                _wxSDK.getLocation()
                        .done(function (lat, lng) {
                            $.ajax({
                                url: _TH_.base + "/wap/pay/bill/deliver/certer.jhtml",
                                type: "POST",
                                data: {
                                    id: _TH_.tenantid,
                                    lat: lat,
                                    lng: lng
                                }
                            }).done(function (data) {
                                if (data.message.type == "success") {
                                    deliveryCenterId = data.data.id;
                                    $("#tenantAddress").html(data.data.address).data("deliveryCenterId", deliveryCenterId);
                                } else {
                                    $(".ONCET").tip("addTask", {
                                        sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                        txt: "data.message.content"
                                    });
                                }
                            }).fail(function () {
                                console.log("获取地址位置失败");
                            });
                        })
                        .fail(function () {
                            console.log("获取地址位置失败");
                        });
            }
        });
        /*店铺优惠券窗口信息*/
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
        $(".sheet-choose_activity_coupon").actionSheet({
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
        $(".sheet-choose_activity_coupon").on("initialized:WHT:actionsheet", function (event) {
            //console.log("初始化完毕");
            //填充html内容
            $(".sheet-choose_activity_coupon").actionSheet("fillContent", $("#tpl_choose_activity_coupon").html());
        });
        $(".delivery_center").on("click", function () {
            //$(".sheet-choose_activity_coupon").actionSheet("show");
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
            $(".addrits-ctn.TT-BY").on('click', 'div.weui_cell', function (event) {
                var $addrit_cell = $(this);
                event.preventDefault();
                $addrit_cell.addClass("active").siblings().removeClass("active");
                $("#tenantAddress").text($addrit_cell.find(".store-address").text());
                $("#tenantAddress").data("deliveryCenterId", deliveryCenterId = $addrit_cell.attr("storeid"));
                $(".sheet-delivery_center").actionSheet("hide");
            });
        });
        $(".buyreduceRN_business").on("click", function () {
            var this_business = this;
            var $choseaddr = $(this_business).find("#tenantAddress");
            var $addrits_centers = $(".addrits-ctn.TT-BY");
            //$(".addrits-ctn.TT-BY>div.active").length
            //$addrits_centers.children('div.active');
            if ($addrits_centers.children('div.active').length) {
            } else {
                //var chose_deliverycenter_id=$choseaddr.data("deliveryCenterId");
                if ($choseaddr.data("deliveryCenterId")) {
                    $addrits_centers.children('div').each(function (index, ele) {
                        if ($(ele).attr("storeid") == $choseaddr.data("deliveryCenterId")) {
                            console.log("存在相同的店铺id");
                            console.log($choseaddr.data("deliveryCenterId"));
                        }
                    });
                } else {
                    $addrits_centers.children('div').each(function (index, ele) {
                        if ($(ele).find(".store-address").text() == $choseaddr.text()) {
                            $(ele).addClass("active");
                            console.log("the text is");
                            console.log($choseaddr.text());
                        }
                    });
                }
            }
            $(".sheet-delivery_center").actionSheet("show");
        });

        $("#amount").bind('input propertychange', function () {

            $("#ckBoxMoney").removeAttr("disabled");
            var amount = $("#amount").val().trim();
            clearTimeout(timeout);

            timeout = setTimeout(function () {
                payBillGetAmount(amount, "");
            }, 500);

        });

        $("#noAmount").bind('input propertychange', function () {
            var amount = $("#amount").val().trim();
            var noAmount = $(this).val().trim();

            if (parseFloat(noAmount) > parseFloat(amount)) {
                $(".ONCET").tip("addTask", {
                    sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                    txt: "不参与优惠金额不得大于消费金额"
                });
                $("#noAmount").val(amount).focus();
                noAmount = amount;
            }
            clearTimeout(timeout);
            timeout = setTimeout(function () {
                payBillGetAmount(amount, noAmount);
            }, 500);
        });

        $(".confirm_check").on("click", function () {
            if ($(".confirm_check").hasClass("active")) {
                if ("${(member)!}" == "") {
                    location.href = "${base}/wap/bound/indexNew.jhtml?redirectUrl=${base}/wap/pay/bill/buyreduce/${id}.jhtml?amount=" + amount;
                    return;
                }

                if (parseFloat($("#amount").val().trim()) > 0) {
                    $.ajax({
                        url: _TH_.base + "/wap/member/pay/bill/get/amount.jhtml",
                        type: "POST",
                        data: {
                            id: _TH_.tenantid,
                            amount: $("#amount").val().trim(),
                            noAmount: $("#noAmount").val().trim(),
                            deliveryCenterId: deliveryCenterId
                        }
                    }).done(function (data) {
                        if (data.message.type == "success") {
                            if (data.data.amount == 0) {
                                location.href = "${base}/wap/payment/notify/sync/" + data.data.sn + ".jhtml?result=0";
                            } else {
                                $("#sn").val(data.data.sn);
                                $("#indexForm").attr("action", "${base}/wap/payment/index.jhtml");
                                $("#indexForm").submit();
                            }
                        } else {
                            $(".ONCET").tip("addTask", {
                                sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                                txt: "data.message.content"
                            });
                        }
                    }).fail(function () {
                        $(".ONCET").tip("addTask", {
                            sign: '<i class="weui_icon_toast weui_icon_info_circle"></i>',
                            txt: "数据传输失败"
                        });
                    });
                }
            }
        });
//        if (store.session.get("amount")) {
//            amount = store.session.get("amount");
//
//            if(parseFloat(amount)>0){
//                $("#amount").val(amount);
//                $("#ckBoxMoney").removeAttr("disabled");
//            }
//
//            if (store.session.get("noAmount")) {
//                noAmount = store.session.get("noAmount");
//                if(parseFloat(noAmount)>0){
//                    $("#noAmount").val(noAmount);
//                    $(".ck_box_money").removeClass("hidden");
//                    $("#SR_ico").addClass("SR101");
//                    $(".SR_bg").removeClass("hidden");
//                }
//            }else {
//                $(".ck_box_money").addClass("hidden");
//                $("#SR_ico").removeClass("SR101");
//                $(".SR_bg").addClass("hidden");
//            }
//            store.session.remove("amount");
//            store.session.remove("noAmount");
//            payBillGetAmount();
//        }

        $("#ckBoxMoney").on("change", function () {
            var hasChk = $('#ckBoxMoney').is(':checked');
            if (hasChk) {
                /*已选中*/
                $(".ck_box_money").removeClass("hidden");
                $("#SR_ico").addClass("SR101");
                $(".SR_bg").removeClass("hidden");
            } else {
                /*未选中*/
                $("#noAmount").val("");
                $(".ck_box_money").addClass("hidden");
                $("#SR_ico").removeClass("SR101");
                $(".SR_bg").addClass("hidden");
                payBillGetAmount($("#amount").val().trim(),$("#noAmount").val().trim());
            }
        });

    });
</script>
</body>
</html>
