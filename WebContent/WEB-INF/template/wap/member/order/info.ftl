<!DOCTYPE HTML>
<html lang="en">
<head>
[#include "/wap/include/resource.ftl"]
<script src="${base}/resources/common/js/jquery.validate.js"></script>
<script src="${base}/resources/common/js/jquery-form.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/rng.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<style>
	#wrapper {
            bottom: 49px;
            padding: 0px;
        }
</style>
<script type="text/javascript">
$().ready(function(){
	var shippingMethodIds={};
	[@compress single_line = true]
		[#list paymentMethods as paymentMethod]
			shippingMethodIds["${paymentMethod.id}"] = [
				[#list paymentMethod.shippingMethods as shippingMethod]
					"${shippingMethod.id}"[#if shippingMethod_has_next],[/#if]
				[/#list]
			];
		[/#list]
	[/@compress]
	
	var $paymentMethodId=$("select[name='paymentMethodId']");
	var $shippingMethodId=$("select[name='shippingMethodId']");
	var $isInvoice=$("input[name='isInvoice']");
	var $freight=$("#freight");
	var $couponDiscount=$("#couponDiscount");
	var $tax=$("#tax");
	var $amountPayable=$("#amountPayable");
	var $orderForm=$("#orderForm");
	var $couponCodeId=$("select[name='couponCodeId']");
	var $submit=$(":submit");
	$paymentMethodId.on("change",function(){
		var $this=$(this);
		if($("select[name='paymentMethodId'] option:selected").attr("paymentMethod")=="balance"){
			$("#useBalance").val(true);
		}else{
			$("#useBalance").val(false);
		}
		$shippingMethodId.find("option").each(function(){
			if($.inArray($(this).val(), shippingMethodIds[$this.val()]) < 0){
				$(this).prop("disabled",true);
			}else{
				$(this).prop("disabled",false);
			}
		});
	});
	$shippingMethodId.on("change",function(){
		$("#shippingMethodDesc").text($("select[name='shippingMethodId'] option:selected").attr("shippingDescription"));
		calculate();
	});
	$couponCodeId.on("change",function(){
		$("#shippingMethodDesc").text($("select[name='shippingMethodId'] option:selected").attr("shippingDescription"));
		calculate();
	});
	$isInvoice.on("change",function(){
		if($(this).prop("checked")){
			$(this).val(true);
			$("input[name='invoiceTitle']").prop("disabled",false)
		}else{
			$("input[name='invoiceTitle']").prop("disabled",true)
			$(this).val(false);
		}
		calculate();
	});
	
	
	$submit.on("click",function(){
		$orderForm.submit();
	});
	$orderForm.validate({
                rules: {
                    paymentMethodId: "required",
                    receiverId: "required",
                    shippingMethodId: "required"
                },
                submitHandler: function () {
                    $submit.prop("disabled", true);
                    if ($("#useBalance").val() == "true") {
                        var e = $("#modal-confirm");
                        e.modal({
                            closeViaDimmer: false,
                            relatedTarget: this,
                            onCancel: function () {
                                $submit.prop("disabled", false);
                            },
                            onClose: function () {
                                $submit.prop("disabled", false);
                            },
                            onConfirm: function (options) {
                                invokTips("warn", "支付中。。。");
                                $.ajax({
                                    url: "${base}/wap/member/order/check_balance.jhtml",
                                    type: "POST",
                                    dataType: "json",
                                    data: $orderForm.serialize(),
                                    success: function (message) {
                                        if (message.type != "success") {
                                            invokTips(message.type, message.content);
                                            $submit.prop("disabled", false);
                                            return false;
                                        }
                                        
                                        var $password = $("#password");
                                        $.ajax({
                                            url: "${base}/common/public_key.jhtml",
                                            dataType: "json",
                                            cache: false,
                                            data: {local: true},
                                            type: "post",
                                            success: function (data) {
                                                
                                                var modulus = data.modulus;
                                                var exponent = data.exponent;
                                                var rsaKey = new RSAKey();
                                                rsaKey.setPublic(b64tohex(modulus), b64tohex(exponent));
                                                var password = hex2b64(rsaKey.encrypt($password.val()));
                                                $("#enPassword").val(password);
                                                $orderForm.ajaxSubmit(function (message) {
                                                    
                                                    if (message.type == "success") {
                                                        invokTips(message.type, "下单成功！");
                                                        setTimeout(function () {
                                                            location.href = "${base}/wap/member/order/list.jhtml";
                                                        }, 500);
                                                    } else {
                                                        invokTips(message.type, message.content);
                                                        setTimeout(function () {
                                                            location.reload(true);
                                                        }, 500);
                                                    }
                                                    $submit.prop("disabled", false);
                                                    return false;
                                                })
                                            }
                                        });
                                    }
                                })
                            }
                        });
                    } else {
                        invokTips("warn", "支付中。。");
                        $orderForm.ajaxSubmit(function (message) {
                            if (message.type == "success") {
                                invokTips(message.type, "下单成功！");
                                if ($("select[name='paymentMethodId'] option:selected").attr("paymentMethod") == "offline") {
                                    setTimeout(function () {
                                        location.href = "${base}/wap/member/order/list.jhtml";
                                    }, 500);
                                    return false;
                                }else {
                                	if($.isWeiXin()){
                                    $.ajax({
                                        url: "${base}/wap/mutual/get_config.jhtml",
                                        data: {
                                            url: location.href.split('#')[0]
                                        },
                                        dataType: "json",
                                        type: "get",
                                        success: function (message_wx) {
                                            if (message_wx.type == "success") {
                                                var data = JSON.parse(message_wx.content);
                                                wx.config({
                                                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                                                    appId: data.appId, // 必填，公众号的唯一标识
                                                    timestamp: data.timestamp, // 必填，生成签名的时间戳
                                                    nonceStr: data.nonceStr, // 必填，生成签名的随机串
                                                    signature: data.signature,// 必填，签名，见附录1
                                                    jsApiList: ["chooseWXPay"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                                                });
                                                wx.ready(function () {
                                                    $.ajax({
                                                        url: "${base}/wap/member/order/payment.jhtml",
                                                        data: {sn: message.content, type: "payment"},
                                                        dataType: "json",
                                                        type: "post",
                                                        beforeSend: function () {
                                                            $submit.prop("disabled", true);
                                                            invokTips("warn", "支付中");
                                                        },
                                                        success: function (data) {
                                                            if (data.message.type == "success") {
                                                                var jsonObject = JSON.parse(data.dataValue);
                                                                //调用微信JS api 支付
                                                                wx.chooseWXPay({
                                                                    timestamp: jsonObject.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                                                                    nonceStr: jsonObject.nonceStr, // 支付签名随机串，不长于 32 位
                                                                    package: jsonObject.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                                                                    signType: jsonObject.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                                                                    paySign: jsonObject.paySign, // 支付签名
                                                                    cancel:function(){
                                                                    	invokTips("warn", "支付取消！");
                                                                            setTimeout(function () {
                                                                                location.href = "${base}/wap/member/order/list.jhtml";
                                                                                return false;
                                                                            }, 500);
                                                                    },
                                                                    success: function (res) {
                                                                        // 支付成功后的回调函数
                                                                        if ("chooseWXPay:ok" == res.errMsg) {
                                                                            invokTips("success", "支付成功！");
                                                                            setTimeout(function () {
                                                                                location.href = "${base}/wap/member/order/list.jhtml";
                                                                                return false;
                                                                            }, 500);
                                                                        } else if ("chooseWXPay:cancel" == res.errMsg) {
                                                                            invokTips("warn", "支付取消！");
                                                                            setTimeout(function () {
                                                                                location.href = "${base}/wap/member/order/list.jhtml";
                                                                                return false;
                                                                            }, 500);
                                                                        } else {
                                                                            invokTips("error", "支付失败！");
                                                                            setTimeout(function () {
                                                                                location.href = "${base}/wap/member/order/list.jhtml";
                                                                                return false;
                                                                            }, 500);
                                                                        }
                                                                        return false;
                                                                    }
                                                                });
                                                            } else {
                                                                invokTips("error", "发起支付失败");
                                                            }
                                                            $submit.prop("disabled", false);
                                                        }
                                                    });
                                                });

                                                wx.error(function (res) {
                                                	alert(res.errMsg);
                                                    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                                                });
                                                wx.checkJsApi({
                                                    jsApiList: ['chooseWXPay'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                                                    success: function (res) {
                                                        // 以键值对的形式返回，可用的api值true，不可用为false
                                                        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                                                    }
                                                });

                                            }
                                        }
                                    });
                                	}else{
                                    //不是微信浏览器
                                	$.ajax({
											url:"${base}/wap/payment/submit.jhtml",
											type:"post",
											data:{
												sn:message.content,
												type:"payment",
												paymentPluginId:"chinapayMobilePlugin"
											},
											dataType:"json",
											success:function(message){
												if(message.type=="success"){
													location.href="vsstoo://payservice/?token=none&paydata="+message.content;
													return false;
												}else{
													invokTips(message.type,message.content);
													setTimeout(function(){
														location.href="${base}/wap/member/order/list.jhtml";
														return false;
													},1500);
												}
											},
											beforeSend:function(){
												invokTips("warn","支付中。。。");
											}
										});
						
                                	}
                                }
                            }
                            $submit.prop("disabled", false);
                        })
                    }

                }
            });
	//计算费用
	function calculate() {
		$.ajax({
			url: "calculate.jhtml",
			type: "POST",
			data: $orderForm.serialize(),
			dataType: "json",
			cache: false,
			beforeSend: function() {
				$submit.prop("disabled", true);
			},
			complete: function() {
				$submit.prop("disabled", false);
			},
			success: function(data) {
				if (data.message.type == "success") {
					$freight.text(data.freight);
					$couponDiscount.text(data.couponDiscount);
					$tax.text(data.tax);
					$amountPayable.text(data.amountPayable);
					if($("#useBalance").val()){
						$("#payableAmount").text(data.amountPayable);
					}
				} 
			}
		});
	}
	$("#select_address").on("click",function(){
		location.href="${base}/wap/member/receiver/list.jhtml?fromCart=true";
	});
	if($("#receiverId").val()==""||$("#receiverId").val()==null){
		invokTips("warn","您还没有收货地址，正在为您跳转。。");
		setTimeout(function(){location.href="${base}/wap/member/receiver/list.jhtml?fromCart=true";}, 1500);
	}
	
	setTimeout(function(){
	  init();
  },200);
});
</script>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">订单预览</h1>
      </header>
    </header>
    <div id="wrapper" class="am-g">
        <div>
            <form id="orderForm" action="create.jhtml" method="post">
                <input type="hidden" id="receiverId" name="receiverId" value="${(receiver.id)!}">
                <input type="hidden" id="useBalance" name="useBalance" value="${useBalance}"/>
                <input type="hidden" id="cartToken" name="cartToken" value="${cartToken}"/>
                <input type="hidden" id="enPassword" name="enPassword" />
                <input type="hidden" id="paymentMethod_wx" name="paymentMethod_wx" value="${(paymentMethod.method)!}"/>

                <div class="wap-address" id="select_address">
                    [#if receiver??]
                    <address >
                        <p><i class="am-icon-user am-icon-xs "></i><span>${receiver.consignee}</span>
                            <i class="am-icon-phone" style="margin-left: 35px;"></i><span>${receiver.phone}</span>
                        </p>

                        <p><i class="am-icon-map-marker"></i><span>${receiver.address}</span></p>
                        <a href="javascript:;" class=" am-fr am-margin-right-sm wap-address-to"><i
                                class=" am-icon-angle-right am-icon-sm"></i></a>
                    </address>
                    [/#if]
                </div>
                
                <div class="wap-method-div">
                    <label>支付方式</label>
                    <span class=" am-fr am-margin-right-sm">
		<select name="paymentMethodId"
                data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
            [#list paymentMethods as paymentMethod]
            [#if  browse_version =="MicroMessenger"&&paymentMethod.method=="online"]
            <option value="${paymentMethod.id}" paymentMethod="${paymentMethod.method}">微信支付</option>
            [#else]
            <option value="${paymentMethod.id}" paymentMethod="${paymentMethod.method}">${paymentMethod.name}</option>
            [/#if]
            [/#list]
        </select>
        </span>
		</div>
		
                <div class="wap-method-div">
                    <label>配送方式</label>
					<span class="am-fr am-margin-right-sm">
						<select name="shippingMethodId"
			                    data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
			                [#list shippingMethods as shippingMethod]
			                <option value="${shippingMethod.id}" shippingDescription="${shippingMethod.description}">
			                    ${shippingMethod.name}
			                </option>
			                [/#list]
			            </select>
					</span>
                </div>
                <!-- <div style=" padding:0 6px;">
                <em style="font-size:small;" id="shippingMethodDesc"></em></div> -->
              <!--   <div class="wap-method-div am-padding-horizontal-sm am-margin-top-sm">
                    <label>配送时间</label>
		<span class=" am-fr am-margin-right-sm">
		<select data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
            [#if appointments??]
            [#list appointments as appointment]
            <option value="${appointment.id}">${appointment.name}</option>
            [/#list]
            [/#if]
        </select>
		</span></div> -->
			 <div class="am-input-group" style="margin-top:2px;width: 100%;">
                <input type="text" style="border-bottom: none;" class="am-form-field" placeholder="如需留言请输入。。" name="memo">
            </div>
             <div class="am-u-sm-12" style="padding:0px; margin-top:5px;">
                <div class="am-input-group am-input-group-default"> <span class="am-input-group-label">
			<input type="checkbox" name="isInvoice">
			发票</span>
                    <input type="text" class="am-form-field" placeholder="如需开具发票,请点选后输入" name="invoiceTitle" value="个人">
                </div>
            </div>
                
                <div class="wap-method-div am-padding-horizontal-sm ">
                    <label class="am-checkbox am-default am-u-sm-6">
                        优惠券:</label>
		<span class=" am-fr am-margin-right-sm">
		<select name="couponCodeId"
                data-am-selected="{btnWidth: 100, btnSize: 'sm', btnStyle: 'default',maxHeight:'25'}">
            <option value="">请选择</option>
            [#if couponCodes??&&couponCodes?has_content]
            [#list couponCodes as couponCode]
            <option value="${couponCode.id}">${couponCode.coupon.name}</option>
            [/#list]
            [/#if]
        </select>
		</span></div>
                <hr data-am-widget="divider" style=" margin-top:1px;margin-bottom: 0px;"
                    class="am-divider am-divider-default"/>
                    <div style="height:5px;background: #ddd;"></div>
                <div>
                    <div data-am-widget="titlebar"
                         class="am-titlebar am-titlebar-default am-margin-zero am-titlebar-multi am-no-layout"
                         style="margin-bottom: 0px;border-bottom: none;"><h2
                            class="am-titlebar-title">购物清单</h2></div>

                    <div class="am-list-news-bd">
                        <ul class="am-list wap-info-list">
                            [#if order??&&order.orderItems?has_content]
                            [#list order.orderItems as orderItem]
                            [#if !orderItem.isGift ]
                            <li class="am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left "
                               style="padding: 0.5rem;margin-left: 0px;
margin-right: 0px;">
                                <div class="am-u-sm-4 am-list-thumb">
                                    <img class="am-img-responsive lazy" src="${base}/resources/wap/image/NoPicture.jpg"
                                         data-original="${orderItem.product.thumbnail}"
                                           style="height: 80px;" /></div>
                                <div class=" am-u-sm-8 am-list-main" style="height: 80px;">
                                    <p class="am-list-item-hd">${abbreviate(orderItem.product.fullName,45,"...")}
                                    </p>
                                    <div class="am-list-item-text" style="position: absolute;bottom: 0px;width: 84%">
                                        <strong class="am-text-danger">
                                        	<i class="am-icon-cny"></i>${orderItem.effectivePrice}
                                    	</strong>
                                    	<span style="float: right;">x${orderItem.quantity}${orderItem.packagUnitName}</span></div>
                                </div>
                            </li>
                            [/#if]
                            [/#list]
                            [/#if]
                        </ul>
                    </div>
                </div>
            </form>
        </div>
        <div style="height:49px;"></div>
    </div>
    <div data-am-widget="navbar"
         class="am-navbar am-cf am-navbar-default am-no-layout wap-orderinfo-bottom" style="background:#fff;border-top: 1px solid #dedede;">
        <div class="am-fl am-u-sm-9 wap-orderinfo-bottom-left">
            <div>
                <strong class="am-margin-left-xs">
                    <span>合计:<i class="am-icon-cny"></i><span id="amountPayable">${order.amountPayable}</span></span></strong>
            </div>
            <div>
                <small class="am-margin-left-xs">运费:<span id="freight">${order.freight}</span></small>
                <small class="am-margin-left-xs">代金券:<span id="couponDiscount">${order.couponDiscount}</span></small>
                <small class="am-margin-left-xs">税金:<span id="tax">${order.tax}</span></small>
            </div>
        </div>
        <button type="submit" class=" am-btn  am-btn-block am-btn-danger" style="width: 25%;height: 100%;">去结算</button>
    </div>
    <div class="am-modal am-modal-confirm" tabindex="-1" id="modal-confirm">
        <div class="am-modal-dialog">
            <div class="am-modal-hd">请输入支付密码</div>
            <div class="am-modal-bd">
                <form action="">
                    <p class="am-text-danger">支付金额:<i class="am-icon-cny"></i><span id="payableAmount">${order.amountPayable}</span></p>
                    <p>账户余额:<i class="am-icon-cny"></i>${member.balance}</p>
                    <input type="password" placeholder="输入支付密码" id="password">
                </form>
            </div>
            <div class="am-modal-footer"><span class="am-modal-btn" data-am-modal-cancel="">取消</span><span
                    class="am-modal-btn" data-am-modal-confirm="">支付</span></div>
        </div>
    </div>
</div>
</body>
</html>
