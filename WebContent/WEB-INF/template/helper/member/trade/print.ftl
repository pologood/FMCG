<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.print.order")}</title>
    <meta name="author" content="zhaoqipei Team"/>
    <meta name="copyright" content="zhaoqipei"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <style type="text/css">
        .bar {
            height: 30px;
            line-height: 30px;
            border-bottom: 1px solid #d7e8f8;
            background-color: #eff7ff;
        }

        table {
            width: 100%;
        }

        table th {
            font-weight: bold;
            text-align: left;
        }

        table td, table th {
            line-height: 30px;
            padding: 0px 4px;
            font-size: 14pt;
            color: #000000;
        }

        .separated th, .separated td {
            border-top: 1px solid #000000;
            border-bottom: 1px solid #000000;
        }
    </style>
    <style type="text/css" media="print">
        .bar {
            display: none;
        }
    </style>
    <script type="text/javascript">
        $().ready(function () {

            var $print = $("#print");

            $print.click(function () {
                //window.location.reload();
                window.print();


                $.ajax({
                    url:"${base}/helper/member/trade/print/times/${trade.id}.jhtml",
                    type:'post',
                    dataType:"jsonp",
                    jsonp: "callback",
                    success:function(result){
                        console.log(result.type);
//                        closeWaitLoadingToast();
//                        $(document).unbind("ajaxBeforeSend");
//                        $("#picture").attr("src",result.data.qr_filepath);
//                        InterValObj=window.setInterval(function(){
//                            queryCash(data.data.sn);
//                        },3000);
                    }
                });

            });

        });
    </script>
</head>
<body onload="opener.location.reload()">
<div class="bar">
    <a href="javascript:;" id="print" class="button">${message("admin.print.print")}</a>
</div>

<div class="content">
[#list orders as order]
    <div style="overflow-x: hidden; overflow-y: auto;">
        <table class="input" style="margin-bottom: 30px;">
            <tr>
                <th>
                    <img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}"/>
                </th>
                <td colspan="5">
                    <h2 style="text-align:center;">发货单</h2>
                </td>
            </tr>
            <tr>
                <th>
                    会员昵称：
                </th>
                <td>
                ${trade.order.member.displayName}
                </td>
                <th>
                    收件人:
                </th>
                <td>
                ${trade.order.consignee}
                </td>
                <th>
                    联系电话:
                </th>
                <td>
                ${trade.order.phone}
                </td>
            </tr>
            <tr>
                <th>
                    收货地址：
                </th>
                <td colspan="3">
                ${trade.order.areaName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${trade.order.address}
                </td>
                <th>
                    发货时间:
                </th>
                <td>
                    [#if trade.shippingDate??]${trade.shippingDate?string('yyyy/MM/dd HH:mm:ss')}[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    订单号：
                </th>
                <td colspan="3">
                ${trade.order.sn}
                    [#if trade.order.paymentMethod.method=="online"]
                        (线上付款)
                    [#elseif trade.order.paymentMethod.method=="offline"]
                        (线下付款)
                    [#elseif trade.order.paymentMethod.method=="balance"]
                        (余额支付)
                    [/#if]
                </td>
                <th>
                    下单日期:
                </th>
                <td>
                ${trade.order.createDate?string("yyyy/MM/dd HH:mm:ss")}
                </td>
            </tr>
        </table>
        <table>
            <tr style="border-bottom: 1px solid #dde9f5;background-color: #f8fbff;">
                <th>&nbsp;</th>
                <th>商品条码</th>
            [#--<th>创建时间</th>--]
                <th>${message("ShippingItem.name")}</th>
                [#--<th>单位</th>--]
                <th>数量</th>
                <th>单价</th>
                <th>金额</th>
            </tr>
            [#assign quantitys=0 prices=0]
            [#list order as orderItem]
                <tr  style="border-bottom: 1px solid #dde9f5;color: #666">
                    <td style="text-align: right">${orderItem_index+1}&nbsp;&nbsp;</td>
                    <td style="width: 16%">${orderItem.barcode}</td>
                [#--<td>${orderItem.time}</td>--]
                    <td style="width: 60%"><span
                            title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 100, "...")}</span>
                        [#if orderItem.isGift]
                            <span class="red">[${message("admin.order.gift")}]</span>
                        [/#if]
                    </td>
                    [#--<td>${orderItem.packagUnitName}</td>--]
                    <td>${orderItem.quantity} ${orderItem.packagUnitName}</td>
                    <td>${orderItem.price}</td>
                    <td>${orderItem.quantity*orderItem.price}</td>

                    [#assign quantitys=quantitys+orderItem.quantity prices=prices+orderItem.quantity*orderItem.price]
                </tr>
            [/#list]
            <tr style="border-bottom: 1px solid #dde9f5;color: #666">

                <td colspan="3" style="text-align: center">合计</td>
                <td>${quantitys}</td>
                <td></td>
                <td>${prices}</td>

            [#--（[#if trade.freight==0||trade.freight==null]包邮[#else]邮费：${trade.freight}[/#if]）--]
            </tr>

        </table>
        <table class="input" style="margin-bottom: 30px;">

            <tr>
                <th>订单合计：</th>
                <td>${trade.price}</td>
                [#assign money=trade.tax+trade.freight+trade.promotionDiscount+trade.couponDiscount+trade.discount+trade.offsetAmount]
                <th colspan="3"><strong style="font-weight: 20px;">应收合计</strong>
                    [#if money!=0]
                        (
                        [#if trade.fee!=0]
                            手续费:${trade.fee},
                        [/#if]

                        [#if trade.tax!=0]
                            税金:${trade.tax},
                        [/#if]
                        [#if trade.freight!=0]
                            运费:${trade.freight},
                        [/#if]

                        [#if trade.promotionDiscount!=0]
                            促销折扣:${trade.promotionDiscount},
                        [/#if]

                        [#if trade.couponDiscount!=0]
                            商家优惠券抵扣:${trade.couponDiscount},
                        [/#if]

                        [#if trade.discount!=0]
                            平台优惠券抵扣:${trade.discount}
                        [/#if]

                        [#if trade.offsetAmount!=0]
                            调整金额:${trade.offsetAmount}
                        [/#if]
                        )
                    [/#if]
                    ：</th>
                <td style="text-align: center;">${trade.amount}</td>
            </tr>
        </table>
        <table class="input" style="margin-bottom: 30px;">
            <tr style="border-top:1px solid #dde9f5;">
                <th>
                    服务商:
                </th>
                <td>
                ${trade.tenant.name}
                </td>

                <th>
                    用户签名:
                </th>
                <td>
                    &nbsp;
                </td>
            </tr>
            <tr style="border-top:1px solid #dde9f5;">
                <th>
                    电话:
                </th>
                <td>
                ${trade.tenant.telephone}
                </td>
                <th>

                    官方网址:
                </th>
                <td>
                ${setting.webSite}
                </th>
            </tr>
        </table>
    </div>
[/#list]
</div>
</body>
</html>