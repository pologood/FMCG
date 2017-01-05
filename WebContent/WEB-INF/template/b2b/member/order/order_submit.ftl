<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="" content="">
    <title>${setting.siteName}-确认订单</title>
    <meta name="keywords" content="${setting.siteName}-确认订单" />
    <meta name="description" content="${setting.siteName}-确认订单" />
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon">
    <link href="${base}/resources/b2b/css/message.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2b/css/submitOrder.css" type="text/css" rel="stylesheet">
    <style type="text/css">
        div.choose-mode dd label.on{
            color:#666666;
        }
    </style>
    <script type="text/javascript">
        var current_obj_addr;
        var payMethod,shipMethod,paymentMthodName;
        var code; 
        var codes=[];
        $(function(){
            //默认选中第一个
            var $first_addr=$(".list").find(".addr").first().find(".inner");
            select_addr($first_addr);
            // $("#payMethod_radio").find($("[name=pay-mode]")).first().attr("checked","checked");
            $("#shipMethod_radio").find($("[name=shipping-mode]")).first().attr("checked","checked");
            // payMethod=$("#payMethod_radio").find($("[name=pay-mode]")).first().val();
            // paymentMthodName=$("#payMethod_radio").find($("[name=pay-mode]")).first().attr("methodName");
            shipMethod=$("#shipMethod_radio").find($("[name=shipping-mode]")).first().val();
            getCode();
            calculate();
            //地区选择
            $("#areaId").lSelect({
                url: "${base}/common/area.jhtml"
            });
            $("#areaId2").lSelect({
                url: "${base}/common/area.jhtml"
            });
            $("select[name='areaId_select']").each(function(){//清除火狐浏览器刷新多出拉下框
                if ($(this).val() == ""){
                    $(this).nextAll("select").remove();
                    return false;
                };
            });
            confirm_btn();
            add_addr();
            //当用户没有收货地址时
            if("${member.receivers?size}"=="0"){
            	new_addr();
            }
        });
        //选择地址时，处理的样式
        function select_addr(a){
            current_obj_addr=a;
            $(a).attr("class","inner selected");
            $(a).parent().siblings().children().attr("class","inner");
            $(a).find(".addr-toolbar").show();
            $(a).parent().siblings().find(".addr-toolbar").hide();
            $(a).find(".deftip").show();
            $(a).parent().siblings().find(".deftip").hide();
        }
        //控制新地址弹框的出现和隐藏
        function new_addr(){
            if($("#addr_dailog_update").css("display")=="none"){
                if($("#addr_dailog_new").css("display")=="none"){
                    $("#addr_dailog_new").show();
                }else{
                    $("#addr_dailog_new").hide();
                }
            }else{
                $("#addr_dailog_update").hide();
                $("#addr_dailog_new").show();
            }
        }
        //修改地址赋值
        function addr_edit(){
            a=current_obj_addr;
            if($("#addr_dailog_new").css("display")=="none"){
                if($("#addr_dailog_update").css("display")=="none"){
                    $("#addr_dailog_update").show();
                }else{
                    $("#addr_dailog_update").hide();
                }
            }else{
                $("#addr_dailog_update").show();
                $("#addr_dailog_new").hide();
            }
            $("#areaId").attr("value",""+$(a).find('#addr_id').text());
            $("#areaId").attr("treePath",""+$(a).find('#addr_treePath').text());
            $("#areaId").lSelect({
                url: "${base}/common/area.jhtml"
            });
            $("#consignee").val($(a).find('.name').text());
            $("#postcode").val($(a).find('.postcode').text());
            $("#street").val($(a).find('.street').text());
            $("#phone").val($(a).find('.phone').text());
            
        }
        //地址修改完后的提交
        function confirm_btn(){
            $("#addr_dailog_new").hide();
            a=current_obj_addr;
            $("#confirm-btn").on("click",function(){
                $.ajax({
                    url:"${base}/b2b/member/receiver/update.jhtml",
                    type:"POST",
                    data:{
                        id:$(a).find("#receivers_id").text(),
                        areaId: $("#areaId").val(),
                        consignee:$("#consignee").val(),
                        phone:$("#phone").val(),
                        address:$("#street").val(),
                        zipCode:$("#postcode").val(),
                        isDefault:true
                    },
                    dataType:"JSON",
                    success:function(data){
                        $.message(data);
                        if(data.type=="success"){
                            $("#addr_dailog_update").hide();
                            location.reload("${base}/b2b/member/order/order_submit.jhtml");
                        }
                    },
                    error:function(e){
                        alert(e);
                    }   
                });
            });
            $(".cancel-btn").on("click",function(){
                $("#addr_dailog").hide();
            });
        }
        function cancel(obj){
            if($(obj).prev().attr("id")=="confirm-btn"){
                $("#addr_dailog_update").hide();
            }else{
                $("#addr_dailog_new").hide();
            }
        }
        //新地址添加完提交
        function add_addr(){
            $("#confirm-btn2").on("click",function(){
                $.ajax({
                    url:"${base}/b2b/member/receiver/save.jhtml",
                    type:"POST",
                    dataType:"JSON",
                    data:{
                        areaId: $("#areaId2").val(),
                        consignee:$("#consignee2").val(),
                        phone:$("#phone2").val(),
                        address:$("#street2").val(),
                        zipCode:$("#postcode2").val(),
                        isDefault:true
                    },
                    success:function(data){
                        if(data.type=="success"){
                            $.message("success",data.content);
                            $("#addr_dailog_new").hide();
                            location.reload("${base}/b2b/member/order/order_submit.jhtml");
                        }else{
                            $.message("error",data.content);
                        }  
                    }
                    
                });
            });
        }
        //地址删除
        function addr_delete(id){
            var a=confirm("确认要删除地址吗");
            if(a){
                $.ajax({
                    url:"${base}/b2b/member/receiver/delete.jhtml",
                    type:"POST",
                    data:{id:id},
                    dataType:"JSON",
                    success:function(message){
                        $.message("success","操作成功");
                        window.location.reload();
                    }
                });
            }
        }
        //设置默认地址
        function addr_setDefault(id){
            $.ajax({
                url:"${base}/b2b/member/receiver/setDefault.jhtml",
                type:"POST",
                data:{id:id},
                dataType:"JSON",
                success:function(message){
                    $.message("success","操作成功");
                    window.location.reload();
                }
            });
        }
        //获取支付方式的id
        function getPayMethod(o){
            payMethod=$(o).val();
            paymentMthodName=$(o).attr("methodName");
        }
        //获取配送当时的id
        function getShipMethod(o){
            shipMethod=$(o).val();
            calculate();
        }
        //获取优惠码
        function getCode(o){
            var cs=[]
            $.each($("[name='code']"),function(){
                if($(this).val()!=""){
                    cs.push($(this).val());
                }
            });
            codes=cs;
            
            calculate();
        }
        //计算费用
        function calculate(){
            $.ajax({
                url:"${base}/b2b/member/order/calculate.jhtml",
                type:"POST",
                data:{
                    paymentMethodId:"${defaultPaymentMethodId}",
                    shippingMethodId:shipMethod,
                    usebalance:false,
                    isInvoice:false,
                    code:codes,
                    tenantIds:[],
                    memo:$("#memo").val()
                },
                traditional: true,
                dataType:"JSON",
                success:function(data){
                    $.each(data.data.trades,function(i,obj){
                        $.each($("[name='tenantId']"),function(){
                            if($(this).attr("tenantId")==obj.tenantId){
                                $(this).find($("[name='freight']")).text(obj.freight);
                                $(this).find($("[name='amount']")).text(obj.amount);
                                $(this).find($("[name='couponDiscount']")).text(obj.couponDiscount);
                            }  
                        });
                    });
                    $("#total_price").text(data.data.amountPayable);
                }
            });
        }
        //生成订单
        function confirm_order(){
            $.message("success", "正在后台为您处理，请稍后....");
            var a=current_obj_addr;
            var memo=[];
            var tenantIds=[];
            $.each($("[name='tenantId']"),function(){
                if($("#memo").val()!=""){
                    tenantIds.push($(this).attr("tenantId"));
                    memo.push($("#memo").val());
                }
            });
            $.ajax({
                url:"${base}/b2b/member/order/create.jhtml",
                type:"POST",
                data:{
                    cartToken:"${cartToken}",
                    receiverId:$(a).find("#receivers_id").text(),
                    paymentMethodId:"${defaultPaymentMethodId}",
                    shippingMethodId:shipMethod,
                    usebalance:false,
                    isInvoice:false,
                    point:$("#point").text(),
                    memo:memo,
                    code:codes,
                    tenantIds:tenantIds
                },
                dataType:"JSON",
                traditional: true,
                success:function(data){
                    if(data.type=="success"){
                    	$.ajax({
			                url:"${base}/b2b/payment/create.jhtml",
			                type:"post",
			                data:{
			                    paymentPluginId:"balancePayPlugin",
			                    sn:data.content
			                },
			                dataType:"JSON",
			                success:function(dataBlock){
			                	if(dataBlock.message.type = "success"){
									location.href=
									"${base}/b2b/payment/payment_index.jhtml?sn="+dataBlock.data+"&orderSn="+data.content;
			                	}else{
			                		$.message("error",dataBlock.message.content);
			                		location.href="${base}/b2b/member/order/list.jhtml"
			                	}
			                	
			                }
			            });
                    }else{
                        $.message("error", data.content);
                    }
                }
            });
        }
        
    </script>
</head>
<body>
<!-- 头部start -->
<div class="header bg">
    <!-- 顶部导航开始 -->
    [#include "/b2b/include/topnav.ftl"]
    <!-- 顶部导航结束 -->
    <!--logo 搜索开始-->
    [#include "/b2b/include/search.ftl"]
    <!--logo 搜索结束-->
    
    <!--guide 导向开始-->
    
    <div class="container">
        <div class="guide">
            <div class="guideProcess">
                <div class="guideProcessAll processOne on">
                    <div class="title"><h1>1</h1></div>
                    <span>购物车</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processTwo on">
                    <div class="title"><h1>2</h1></div>
                    <span>确认订单</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processThree">
                    <div class="title"><h1>3</h1></div>
                    <span>支付</span>
                    <div class="processBar"></div>
                </div>
                <div class="guideProcessAll processFour">
                    <div class="title"><h1>√</h1></div>
                    <span>完成</span>
                    <div class="processBar"></div>
                </div>
            </div>
            <div class=""></div>
        </div>
    </div>
    <!--guide 导向结束-->
</div>
<!--头部end-->
<!--start-->
<div class="container">
    <div class="submitOrder">
        <div class="selectAddress">
        	[#if member.receivers??&&member.receivers?has_content]
            <h2 class="order-stit">选择收货地址</h2>
            <div class="list">
                [#list member.receivers as receivers]
                <div class="addr" >
                    <div class="inner" onclick="select_addr(this)" >
                        <div id="addr_id" style="display:none;">${receivers.area.id}</div>
                        <div id="receivers_id" style="display:none;">${receivers.id}</div>
                        <div id="addr_treePath" style="display:none;">${receivers.area.treePath}</div>
                        <div class="addr-hd" title="">
                            <span class="name">${receivers.consignee}</span>
                        </div>
                        <div class="addr-bd" title="">
                            <p class="street" >${receivers.address}</p>
                            <p>
                                <span class="area">${receivers.areaName}</span>
                                <span class="postcode">${receivers.zipCode}</span>
                            </p>

                            <p class="phone">${receivers.phone}</p>
                        </div>
                        <div class="addr-toolbar" title="" style="display:none;">
                            <a title="" onclick="addr_edit()">修改</a>
                            &nbsp;
                            <a title="" onclick="addr_delete(${receivers.id})">删除</a>
                             &nbsp;
                            [#if receivers.isDefault==false]
                            <a title="" onclick="addr_setDefault(${receivers.id})">设为默认</a>
                            [/#if]
                        </div>
                        [#if receivers.isDefault==true]
                        <i class="deftip" style="">默认地址</i>
                        [/#if]
                    </div>
                </div>
                [/#list]
            </div>
            [/#if]
            <div class="control clearfix">
                <!-- <span class="manageAddr"><a href="javascript:;" target="_blank">·&nbsp;&nbsp;管理收货地址</a></span> -->
                <span class="createAddr" onclick="new_addr()">·&nbsp;&nbsp;创建新的地址</span>
            </div>
        </div>
        <!--添加地址-->
        <div class="addrWrap" style="display:none;" id="addr_dailog_new">
            <dl class="address-pop">
                <dt>省：</dt>
                <dd class="address-select">
                    <i>*</i>
                     <span class="fieldSet">
                        <input type="hidden" id="areaId2" name="areaId" value="" treePath="" />
                      </span>
                    
                    <span></span>
                </dd>
                <dt>邮政编码：</dt>
                <dd>
                    <i>*</i>
                    <input class="text" name="postcode" type="text" id="postcode2">
                    <span></span>
                </dd>
                <dt>街道地址：</dt>
                <dd>
                    <i>*</i>
                    <textarea class="textarea" name="street" cols="30" rows="3" id="street2"></textarea>
                    <span class="breakline">请填写街道地址，最少5个字，最多不能超过100个字</span>
                </dd>
                <dt>收货人姓名：</dt>
                <dd>
                    <i>*</i>
                    <input class="text" name="name" type="text" id="consignee2">
                    <span>请填写收货人</span>
                </dd>
                <dt>手机：</dt>
                <dd>
                    <i>*</i>
                    <input class="text" name="phone" type="text" id="phone2">
                    <span>请填写正确的联系号码</span>
                </dd>
                <dt></dt>
                <dd class="open-btn">
                    <a class="confirm-btn" id="confirm-btn2" href="javascript:;">确认地址</a>
                    <a class="cancel-btn" href="javascript:;" onclick="cancel(this)">取消</a>
                </dd>
            </dl>
        </div>
        <!--修改地址-->
        <div class="addrWrap" style="display:none;" id="addr_dailog_update">
            <dl class="address-pop">
                <dt>省：</dt>
                <dd class="address-select">
                    <i>*</i>
                     <span class="fieldSet">
                        <input type="hidden" id="areaId" name="areaId" value="" treePath="" />
                      </span>
                    
                    <span></span>
                </dd>
                <dt>邮政编码：</dt>
                <dd>
                    <i>*</i>
                    <input class="text" name="postcode" type="text" id="postcode">
                    <span></span>
                </dd>
                <dt>街道地址：</dt>
                <dd>
                    <i>*</i>
                    <textarea class="textarea" name="street" cols="30" rows="3" id="street"></textarea>
                    <span class="breakline">请填写街道地址，最少5个字，最多不能超过100个字</span>
                </dd>
                <dt>收货人姓名：</dt>
                <dd>
                    <i>*</i>
                    <input class="text" name="name" type="text" id="consignee">
                    <span>请填写收货人</span>
                </dd>
                <dt>手机：</dt>
                <dd>
                    <i>*</i>
                    <input class="text" name="phone" type="text" id="phone">
                    <span>请填写正确的联系号码</span>
                </dd>
                <dt></dt>
                <dd class="open-btn">
                    <a class="confirm-btn" id="confirm-btn" href="javascript:;">确认地址</a>
                    <a class="cancel-btn" href="javascript:;"onclick="cancel(this)">取消</a>
                </dd>
            </dl>
        </div>
        <div class="choose-mode">
            [#--<dl>--]
                [#--<dt>支付方式</dt>--]
                [#--<dd id="payMethod_radio" >--]
                    [#--[#list paymentMethods as payMethod]--]
                    [#--<label class="on">--]
                        [#--<input type="radio" name="pay-mode" value="${payMethod.id}" methodName="${payMethod.method}" onchange="getPayMethod(this)"/>--]
                        [#--${payMethod.name}--]
                    [#--</label>--]
                    [#--[/#list]--]
                [#--</dd>--]
            [#--</dl>--]
            <dl>
                <dt>配送方式</dt>
                <dd id="shipMethod_radio" >
                    [#list shippingMethods as shipMethod]
                        <label class="on">
                            <input type="radio" name="shipping-mode" value="${shipMethod.id}" onchange="getShipMethod(this)"/>
                            ${shipMethod.name}
                        </label>
                    [/#list]
                </dd>
            </dl>
        </div>
        <h2 class="order-stit">确认订单信息</h2>
        <div class="order-form">
            <div class="order-table">
                <div class="order-th">
                    <div class="th th-item">
                        <div class="td-inner">商品</div>
                    </div>
                    <div class="th th-info">
                        <div class="td-inner">商品信息</div>
                    </div>
                    <div class="th th-price">
                        <div class="td-inner">单价（元）</div>
                    </div>
                    <div class="th th-amount">
                        <div class="td-inner">数量</div>
                    </div>
                    <div class="th th-coupon">
                        <div class="td-inner">优惠</div>
                    </div>
                    <div class="th th-sum">
                        <div class="td-inner">小计（元）</div>
                    </div>
                </div>
                [#list order.trades as trades]
                <div class="order-list">
                    <div class="clearfix order-body">
                        <div class="order-body-th clearfix">
                            <img class="shop-icon" src="${trades.tenant.thumbnail}"/>
                            <span>店铺：</span>
                            <a href="${base}/b2b/tenant/index.jhtml?id=${trades.tenant.id}" target="_blank" title="hape旗舰">${trades.tenant.name}</a>
                            <a title="" class="marking-v" href="javascript:;" target="_blank">
                                <span>V${trades.tenant.score}</span>
                            </a>
                            <!-- <div style="display:inline;float:right;">
                                <label for="">优惠劵：</label>
                                <select name="code" tenant="${trades.tenant.id}" onchange="getCode(this)" >
                                    <option value="" selected="selected">不使用优惠</option>
                                    [#list couponCodes as couponcode]
                                    [#if trades.tenant.id==couponcode.coupon.tenant.id]
                                    <option value="${couponcode.code}">${couponcode.coupon.name}</option>
                                    [/#if]
                                    [/#list]
                                </select>
                                <span> -<em></em> 元</span>
                            </div> -->
                        </div>
                        <div class="order-content">
                            [#list trades.orderItems as products]
                            <div class="item-body clearfix">
                                <ul class="item-content clearfix">
                                    <li class="td-item">
                                        <div class="td-inner">
                                            <div class="item-pic">
                                                <a href="javascript:;" target="_blank">
                                                    <img src="${products.thumbnail}">
                                                </a>
                                            </div>
                                            <div class="item-info">
                                                    <a href="javascript:;" target="_blank">
                                                        ${products.fullName}
                                                    </a>
                                            </div>
                                        </div>
                                    </li>
                                    <li class="td-info">
                                        <div class="td-inner">
                                            <div>
                                                [#list products.product.specification_value as specifications]
                                                <p>${specifications}</p>
                                                [/#list]
                                            </div>
                                        </div>
                                    </li>
                                    <li class="td-price">
                                        <div class="td-inner">
                                            <span>${products.originalPrice}</span>
                                        </div>
                                    </li>
                                    <li class="td-amount">
                                        <div class="td-inner">
                                            <span>${products.quantity}</span>
                                        </div>
                                    </li>
                                    <li class="td-coupon">
                                        <div class="td-inner">
                                            <div>
                                                
                                                <!-- <select name="" id="">
                                                    <option value="">不使用优惠</option>
                                                    [#list products.product.coupons as coupons]
                                                    <option value="" selected="selected">${coupons.name}</option>
                                                    [/#list]
                                                </select>
                                                <span>省<em>${products.discount}</em>元</span> -->
                                               
                                            </div>
                                        </div>
                                    </li>
                                    <li class="td-sum">
                                        <div class="td-inner">
                                            [#if products.discount??&&products.discount!=0]
                                                <span>￥${products.effectiveAmount}</span>
                                            [#else]
                                                <span>￥${products.subtotal}</span>
                                            [/#if]
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            [/#list]
                            <div tenantId="${trades.tenant.id}" name="tenantId">
                                <div class="item-checked">
                                    [#if versionType==0]
                                    <span>分润金额：<em name="totalProfit">￥${trades.totalProfit}</em></span>
                                    <span>调整金额：<em name="offsetAmount">￥${trades.offsetAmount}</em></span>
                                    <span>税费：<em name="tax">￥${trades.tax}</em></span>
                                    [/#if]
                                    <span>快递运费：<em class="em" name="freight" >${trades.freight}</em></span>
                                    <span>优惠券：<em name="couponDiscount">￥${trades.couponDiscount}</em></span>
                                </div>
                                <div class="item-total">
                                    合计：<span>¥<em name="amount">${trades.amount}</em>&nbsp;元</span>
                                    &nbsp;
                                    <div style="display:inline;margin-left:10px;">
                                        <label for="">优惠劵:</label>
                                        <select name="code" tenant="${trades.tenant.id}" onchange="getCode(this)" >
                                            <option value="" selected="selected">不使用优惠</option>
                                            [#list couponCodes as couponcode]
                                            [#if trades.tenant.id==couponcode.coupon.tenant.id&&couponcode.coupon.status=="confirmed"]
                                            <option value="${couponcode.code}">${couponcode.coupon.name}</option>
                                            [/#if]
                                            [/#list]
                                        </select>
                                        <!-- <span>元</span> -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                [/#list]
            </div>
            <div>
                <div class="note">
                    <label for="">备注：</label>
                    <input type="text" name="memo" id="memo" placeholder="补充填写其他信息，如有快递不到也请留言备注">
                </div>
                <div class="discount">
                   <!--  <label for="">优惠劵：</label>
                    <select name="code" id="getCode" onchange="getCode(this)">
                        <option value="" selected="selected">不使用优惠</option>
                    </select>
                    <span> -<em></em> 元</span> -->
                </div>
                <script type="text/javascript">
                    $(function(){
                        // $.ajax({
                        //     url:"${base}/b2b/member/order/coupons.jhtml",
                        //     type:"POST",
                        //     data:{},
                        //     dataType:"JSON",
                        //     success:function(message){
                        //         for(var i=0;i<message.data.length;i++){
                        //             $("#getCode").append(
                        //                 "<option value='"+message.data[i].code+"'>"+message.data[i].name+"</option>"
                        //             );
                        //         }
                                
                        //     }
                        // });
                    });
                </script>
            </div>
        </div>
        <!-- footer显示价格 -->
        <div>
            <div class="order-paybar">
                <a class="order_surebtn" href="javascript:confirm_order();">确认并付款 &gt;</a>
                <span class="order_paybar_info_cost" id="total_price">¥ ${order.amount}</span>
                <div id="point" style="display:none;">${order.point}</div>
                <div class="order_paybar_info">
                    共有 <span >${order.quantity}</span> 件商品，总计：
                </div>
                <a href="${base}/b2b/cart/list.jhtml" class="cart_back">返回购物车</a>
            </div>
        </div>
    </div>
</div>
<!--end-->
<!--标语start-->
[#include "/b2b/include/slogen.ftl"]
<!--标语end-->
<!--底部start-->
[#include "/b2b/include/footer.ftl"]
<!--底部end-->
</body>
</html>