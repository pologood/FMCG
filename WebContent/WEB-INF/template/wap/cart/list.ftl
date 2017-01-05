<!DOCTYPE html>
<html lang="en">
<head>
    <title>购物车</title>
[#include "/wap/include/resource-2.0.ftl"]
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap2.css"/>
    <script src="${base}/resources/wap/2.0/js/flexible.debug.js"></script>
    <script type="text/javascript">


    </script>
</head>
<style>
    input[type="checkbox"] {
        width: 8px;
        height: 8px;
    }

    p {
        line-height: 1.6;
    }

    span {
        line-height: 1.2;
    }
</style>
<body class="CT-LT">
<input type="hidden" id="pagecache"></input>
<script type="text/javascript">
  var cacheinput = document.querySelector('#pagecache');
  if (cacheinput.value !== "") {
    location.reload(true);
  }
  cacheinput.value = "some value";
</script>
[#include "/wap/include/static_resource.ftl"]

<div class="container" style="">
</div>
<script type="text/html" id="tpl_wraper">
    [#if cart?? && cart.cartItems?has_content]
        [#list cart.tenants as tenant]
        <div class="weui_cells cl box_cartitR">
            <div class="weui_cells_title weui_cell box_cartitR-hd">
                <div class="weui_cell_hd" style="">
                    <input hidden name="check_tenant" tenant_id="${tenant.id}" ischecked="Y">
                    <i class="ScheckB active" checkfor="check_tenant"></i>
                </div>
                <div class="weui_cell_bd weui_cell_primary storename">
                    <p class="font-default dark-grey"
                       onclick="location.href='${base}/wap/tenant/index/${tenant.id!}.jhtml'">
                        <span>${abbreviate(tenant.name,20,"...")}</span></p>
                </div>
                <div class="weui_cell_ft rightaccess">
                    <i class="font-default iconfont">&#xe635;</i>
                </div>
            </div>
            <!-- box_cartitR-bd-->
            <div class="weui_cells box_cartitR-bd">
                [#list cart.cartItems as cartItem]
                    [#if cartItem.product.tenantId==tenant.id]
                        <div class="weui_cell box_cartitS">
                            <div class="weui_cell_hd">
                                <input hidden name="check_product" tenant_id="${tenant.id}" cartId="${cartItem.id}"
                                       ischecked="[#if cartItem.selected]Y[#else]N[/#if]">
                                <i class="ScheckB [#if cartItem.selected]active[/#if]" checkfor="check_product"></i>
                            </div>
                            <div class="weui_cell_bd weui_cell_primary box_cartitS-bd">
                                <div class="weui_cell box_cartitS-main" style="padding: 0;">
                                    <div class="img weui_cell_hd goodsthumb">
                                        <a href="javascript:;">
                                            <img class="lazy"
                                                 src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                                                 data-original="${cartItem.product.thumbnail}" alt="商品图片"
                                                 onclick="checkPro(this)">
                                        </a>
                                    </div>
                                    <div class="weui_cell_bd weui_cell_primary box_cartitT">
                                        <!-- 信息-->
                                        <div class="sec1">
                                            <div onclick="javascript:window.location.href='${base}/wap/product/content/${cartItem.product.id}/product.jhtml'">
                                            ${abbreviate(cartItem.product.fullName,40,"...")}
                                            </div>
                                            <div>
                                                [#list cartItem.product.specificationValues as specificationValue]
                                                    <span>${specificationValue.specification.name}
                                                        ：${specificationValue.name}</span>
                                                    &nbsp;&nbsp;
                                                [/#list]
                                            </div>
                                        </div>
                                        <!-- 价格-->
                                        <div class="sec2 cl">
                                            <!-- 价格市场价-->
                                            <div class="sec2-prices">
                                                <div>
                                                    <span>¥</span>${cartItem.effectivePrice}
                                                </div>
                                                <div>
                                                    <span class="font-small old-price">
                                                        ¥${cartItem.product.marketPrice}
                                                    </span>
                                                </div>
                                            </div>
                                            <!-- 文字标签-->
                                            <div class="sec2-txttags">
                                                [#if cartItem.product.promotionProducts?has_content]
                                                    [#list cartItem.product.promotionProducts as promotionProduct]
                                                        <span class="font-small_1 order-prefer CT-LT">
                                                        ${promotionProduct.promotion.name}
                                                        </span>
                                                    [/#list]
                                                [/#if]
                                            </div>
                                        </div>
                                        <!-- 操作-->
                                        <div class="weui_cell no_border sec3 cl"
                                             style="padding-top: 0;padding-left: 0;">
                                            <div class="weui_cell_hd">
                                                <form action="#" class="add_minus CT-LT">
                                                    <i name="minus" class="font-default opera-minus"></i>
                                                    <input type="text" value="${cartItem.quantity}"
                                                           class="font-small" name="quantity"
                                                           cartItemId="${cartItem.id}"
                                                        [#if cartItem.packagUnit??]
                                                           packagUnitId="${cartItem.packagUnit.id}"
                                                        [#else]
                                                           packagUnitId=""
                                                        [/#if] onkeydown="old(this)" oninput="modifyQuantity(this)"
                                                           onpropertychange="modifyQuantity(this)" maxlength="4">
                                                    <i name="add" class="font-default opera-add"></i>
                                                </form>
                                            </div>
                                            <div class="weui_cell_bd weui_cell_primary">

                                            </div>
                                            <div class="weui_cell_ft sec3-ft">
                                                <i name="del" id="${cartItem.id}" class="font-small iconfont">
                                                    &#xe610;</i>
                                            </div>

                                        </div>
                                    </div>
                                    <!--<div class="weui_cell_ft">
                                        <span class="font-small price red">
                                            <span class="font-small">¥</span>
                                            <span class="font-default">${cartItem.effectivePrice}</span>
                                        </span><br>
                                        <span class="font-small old-price">
                                            ¥${cartItem.product.marketPrice}
                                        </span><br>
                                        <i name="del" id="${cartItem.id}" class="font-small iconfont">&#xe610;</i>
                                    </div>-->
                                </div>
                            </div>
                        </div>
                    [/#if]
                [/#list]
            </div>

        </div>
        [/#list]
    [#else]
    <div style="width: 100%;height: 450px;text-align: center;">
        <div style="vertical-align: middle;padding-top: 120px;">
            <i class="iconfont" style="font-size: 80px;color: gray;">&#xe651;</i>
            <p style="font-size: 16px;font-weight: bold;margin: 20px 0;">购物车快饿瘪了T.T</p>
            <div style="background-color: orangered;width: 90px;font-weight: bold;padding: 10px 0;margin:0 auto;font-size: 16px;">
                <a style="color: white;" href="${base}/wap/index.jhtml">去逛逛</a>
            </div>
        </div>
    </div>
    [/#if]
    <div style="height: 100px;background-color: #e8eaea;"></div>
    <header class="am-topbar-fixed-bottom">
        <div class="weui_cells cl" id="cart-footer">
            <div class="pd-cl weui_cell">
                <div class="weui_cell_hd color-revert font-default">
                    <input hidden id="checkAll" ischecked="N">
                    <i class="ScheckB" checkfor="check_checkAll" style="margin-left: 0.8em"></i>
                    全选
                </div>
                <div class="weui_cell_primary color-revert" style="text-align:center;">
                    <p style="line-height:1.2;">
                            <span class="font-large">合计：¥<span
                                    id="effectivePrice" class="font-large-1">[#if cart??]${cart.effectivePrice}[#else]
                                0.00[/#if]</span></span>
                    </p>
                </div>
                <a class="weui_cell_hd bg-red color-revert settle" href="javascript:;" onclick="submit()">
                    <span class="font-large-1">结算</span>
                </a>
            </div>

        </div>
    </header>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript">

    $(function () {
        init();
        init_check();
        $(".lazy").picLazyLoad({
            threshold: 100,
            placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
        });
        <!-- 原功能块01-->
        $("input[name='check_tenant']").change(function () {
            $("input[type='checkbox']").attr("disabled", true);
            var $tenant = $(this);
            $.each($("input[name='check_product']"), function () {
                var $product = $(this);
                if ($tenant.prop("checked") && $product.attr("tenant_id") == $tenant.attr("tenant_id")) {
                    $product.prop("checked", true);
                }
                if (!$tenant.prop("checked") && $product.attr("tenant_id") == $tenant.attr("tenant_id")) {
                    $product.prop("checked", false);
                }
            });
            init_check();
            selected();
        });
        <!-- 功能块01 副本-->
        $(".ScheckB[checkfor='check_tenant']").on("touchstart", function () {
            //$("input[type='checkbox']").attr("disabled", true);
            var $itsfor_tenant = $(this).siblings('input[hidden]');
            $.each($("input[name='check_product']"), function () {
                var $product = $(this);
                if ($itsfor_tenant.attr("ischecked") == "Y" && $product.attr("tenant_id") == $itsfor_tenant.attr("tenant_id")) {
                    $product.attr("ischecked", "N");
                    $product.siblings('.ScheckB[checkfor="check_product"]').removeClass('active');
                }
                if ($itsfor_tenant.attr("ischecked") == "N" && $product.attr("tenant_id") == $itsfor_tenant.attr("tenant_id")) {
                    $product.attr("ischecked", "Y");
                    $product.siblings('.ScheckB[checkfor="check_product"]').addClass('active');
                }
            });
            init_check();
            selected();
        });
        <!-- 原功能块02-->
        $("input[name='check_product']").change(function () {
            $("input[type='checkbox']").attr("disabled", true);
            init_check();
            selected();
        });
        <!-- 功能块02 副本-->
        $(".ScheckB[checkfor='check_product']").on("touchstart", function () {
            //$("input[type='checkbox']").attr("disabled", true);
            var $product = $(this).siblings('input[hidden]');
            if ($product.attr("ischecked") == "Y") {
                $product.attr("ischecked", "N");
            } else {
                $product.attr("ischecked", "Y");
            }
            //$(this).siblings('.ScheckB[checkfor="check_product"]').removeClass('active');
            $(this).toggleClass('active');
            init_check();
            selected();
        });
        <!-- 功能区块03-->
        $("#checkAll").change(function () {
            $("input[type='checkbox']").attr("disabled", true);
            if ($(this).prop("checked")) {
                $("input[type='checkbox']").prop("checked", true);
            } else {
                $("input[type='checkbox']").prop("checked", false);
            }
            selected();
        });
        <!-- 功能区块03 副本-->
        $(".ScheckB[checkfor='check_checkAll']").on("touchstart", function () {
            var $hiddeninput = $(this).siblings('input[hidden]');
            //$hiddeninput.attr("ischecked", "Y");
            if ($hiddeninput.attr("ischecked") == "Y") {
                $("input[hidden]").attr("ischecked", "N");
                $("input[hidden]").siblings(".ScheckB").removeClass('active');
                $hiddeninput.attr("ischecked", "N");
            } else {
                $("input[hidden]").attr("ischecked", "Y");
                $("input[hidden]").siblings(".ScheckB").addClass('active');
                $hiddeninput.attr("ischecked", "Y");
            }
            //$(this).toggleClass('active');
            selected();
        });

        $("[name='add']").click(function () {
            var $quantity = $(this).prev();
            if ($quantity.val() == "") {
                $quantity.val(1);
            } else {
                $quantity.val(Number($quantity.val()) + 1);
            }
            edit($quantity);
        });

        $("[name='minus']").click(function () {
            var $quantity = $(this).next();
            if ($quantity.val() == 1) {
                return;
            }
            if ($quantity.val() > 1) {
                $quantity.val(Number($quantity.val()) - 1);
            } else {
                $quantity.val(1);
            }
            edit($quantity);
        });

        $("[name='del']").click(function () {
            var iid = $(this).attr("id");
            showDialog1("友情提示", "确认删除该商品吗？", function () {
                ajaxPost({
                    url: 'del.jhtml',
                    data: {
                        ids: iid
                    },
                    success: function (data) {
                        if (data.message.type == "success") {
                            showToast({content: "删除成功"});
                            window.setTimeout(function () {
                                location.reload();
                            }, 600);
                        } else {
                            showDialog2("友情提示", data.message.content);
                        }
                    }
                });
            });
        });
    });

    function checkPro(o) {
        $(o).parent().parent().parent().parent().prev().children().trigger("click");
    }

    function submit() {
        var check = false;
        $.each($("input[name='check_product']"), function () {
            if ($(this).attr("ischecked") == "Y") {
                check = true;
            }
        });
        if (!check) {
            showDialog2("友情提示", "请选择需要购买的商品！");
            return;
        }
        location.href = "${base}/wap/member/order/orderPay.jhtml";
    }

    function init_check() {
        if ($("input[name='check_product']").size() == 0) {
            return;
        }
        var check_all = "Y";
        $.each($("input[name='check_tenant']"), function () {
            var $tenant = $(this);
            var check_tenant = "Y";
            $.each($("input[name='check_product']"), function () {
                var $product = $(this);
                if ($product.attr("tenant_id") == $tenant.attr("tenant_id") && $product.attr("ischecked") == "N") {
                    check_tenant = "N";
                    check_all = "N";
                }
            });
            $tenant.attr("ischecked", check_tenant);

            if (check_tenant == "Y") {
                $tenant.siblings('.ScheckB[checkfor="check_tenant"]').addClass('active');
            } else {
                $tenant.siblings('.ScheckB[checkfor="check_tenant"]').removeClass('active');
            }
            /* 
            $tenant.siblings('.ScheckB[checkfor="check_tenant"]').toggleClass(function(){
                if(check_tenant=="Y"){
                    return "active";
                }else{
                    return "";
                }
            });*/
            //$tenant.siblings('ScheckB[checkfor="check_tenant"]').addClass('class_name');
        });
        $("#checkAll").attr("ischecked", check_all);
        if (check_all == "Y") {
            $("#checkAll").siblings('.ScheckB[checkfor="check_checkAll"]').addClass('active');
        } else {
            $("#checkAll").siblings('.ScheckB[checkfor="check_checkAll"]').removeClass('active');
        }
    }

    function selected() {
        if ($("input[name='check_product']").size() == 0) {
            $("input[type='checkbox']").removeAttr("disabled");
            return;
        }
        var cartIds = [];
        $.each($("input[name='check_product']"), function () {
            var $this = $(this);
            if ($this.attr("ischecked") == "Y") {
                cartIds.push($this.attr("cartId"));
            }
        });
        ajaxPost({
            url: "selected.jhtml",
            data: {ids: cartIds.join(",")},
            success: function (data) {
                if (data.message.type == "success") {
                    $("#effectivePrice").text(data.effectivePrice);
                    $("#freight").text(data.freight);
                    $("input[type='checkbox']").removeAttr("disabled");
                } else {
                    showDialog2("友情提示", data.message.content, function () {
                        location.reload(true);
                    });
                }
            }
        });
    }

    function edit($quantity) {
        var quantity = $quantity.val();
        var packagUnitId = $quantity.attr("packagUnitId");
        var cartItemId = $quantity.attr("cartItemId");
        if (/^\d*[1-9]\d*$/.test(quantity)) {
            ajaxPost({
                url: "edit.jhtml",
                data: {id: cartItemId, quantity: quantity, packagUnitId: packagUnitId},
                success: function (data) {
                    if (data.message.type == "success") {
                        $("#effectivePrice").text(data.effectivePrice);
                        $("#freight").text(data.freight);
                    } else {
                        showDialog2("友情提示", data.message.content, function () {
                            location.reload(true);
                        });
                    }
                }
            });
        }
    }

    function modifyQuantity(o) {
        var old = $(o).attr("old");
        var val = $(o).val().replace(/[^\d]/g, '');
        if (val == 0 || val.trim() == "") {
            $(o).val("");
            return;
        }
        $(o).val(Number(val));
        if ($(o).val() != old) {
            edit($(o));
        }
    }

    function old(o) {
        $(o).attr("old", $(o).val());
    }

</script>
</body>
</html>
