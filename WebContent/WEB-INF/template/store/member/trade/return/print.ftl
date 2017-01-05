<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>退货单打印</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    [#include "/store/member/include/bootstrap_css.ftl"]
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <style type="text/css">
       table th {
          font-weight: bold;
          background: #fafafa;
        },
        table th,table td{
          text-align: left;
          font-size: 14pt;
          color: #000000;

        }
    </style>
    <script type="text/javascript">
        $().ready(function () {
            var $print = $("#print");
            $print.click(function () {
                window.print();
                $.ajax({
                    url:"${base}/helper/member/trade/return/print/times/${spReturns.id}.jhtml",
                    type:'post',
                    dataType:"jsonp",
                    jsonp: "callback",
                    success:function(result){
                        console.log(result.type);

                    }
                });
            });

        });
    </script>
</head>
<body onload="opener.location.reload()">
<div class="col-sm-offset-0 col-sm-2 mt5 mb5" style="float:none;">
    <button id="print" class="btn btn-block btn-default">打印</button>
</div>
<div class="content" style="padding:0px;">
    [#list orders as order]
    <div style="overflow-x: hidden; overflow-y: auto;">
        <table class="table tabs" >
            <tr>
                <th style="width:160px;background:white;">
                    <img src="${base}/upload/images/PC-login_00.png" alt="${setting.siteName}"/>
                </th>
                <td colspan="5">
                    <h2 style="text-align:center;">退货单</h2>
                </td>
            </tr>
            <tr>
                <th>
                    会员昵称：
                </th>
                <td>
                ${spReturns.trade.order.member.displayName}
                </td>
                <th>
                    发件人:
                </th>
                <td>
                ${spReturns.trade.order.consignee}
                </td>
                <th>
                    联系电话:
                </th>
                <td>
                ${spReturns.trade.order.phone}
                </td>
            </tr>
            <tr>
                <th>
                    发货地址：
                </th>
                <td colspan="3">
                ${spReturns.trade.order.areaName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${spReturns.trade.order.address}
                </td>
                <th>
                    退货时间:
                </th>
                <td>
                    [#if spReturns.completedDate??]${spReturns.completedDate?string('yyyy/MM/dd HH:mm:ss')}[/#if]
                </td>
            </tr>
            <tr>
                <th>
                    退货单号：
                </th>
                <td colspan="3">
                ${spReturns.sn}
                [#if spReturns.trade.order.paymentMethod.method=="online"]
                (线上付款)
                [#elseif spReturns.trade.order.paymentMethod.method=="offline"]
                (线下付款)
                [#elseif spReturns.trade.order.paymentMethod.method=="balance"]
                (余额支付)
                [/#if]
                </td>
                <th>
                    退单日期:
                </th>
                <td>
                    ${spReturns.trade.order.createDate?string("yyyy/MM/dd HH:mm:ss")}
                </td>
            </tr>
        </table>
        <table class="table">
          <tr style="">
            <th></th>
            <th>商品条码</th>
            <th>商品名称</th>
            <th>数量</th>
            <th>单价</th>
            <th>金额</th>
          </tr>
          [#assign quantitys=0 prices=0]
          [#list order as orderItem]
          <tr style="">
            <td style="text-align: right">${orderItem_index+1}</td>
            <td style="width: 16%">${orderItem.barcode}</td>
            <td style="width:60%;">
              <span title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 100, "...")}</span>
              [#if orderItem.isGift]
              <span class="red">[${message("admin.order.gift")}]</span>
              [/#if]
            </td>
            <td>${orderItem.quantity} ${orderItem.packagUnitName}</td>
            <td>${orderItem.price}</td>
            <td>${orderItem.quantity*orderItem.price}</td>
            [#assign quantitys=quantitys+orderItem.quantity prices=prices+orderItem.quantity*orderItem.price]
          </tr>
          [/#list]
          <tr style="font-weight: bold;">
            <td colspan="3" style="text-align: center">合计</td>
            <td>${quantitys}</td>
            <td></td>
            <td>${prices}</td>
          </tr>
        </table>
        <table class="table tabs">
          <tr>
            <th>订单合计：</th>
            <td>${spReturns.trade.price}</td>
            [#assign money=spReturns.trade.tax+spReturns.trade.freight+spReturns.trade.promotionDiscount+spReturns.trade.couponDiscount+spReturns.trade.discount+spReturns.trade.offsetAmount]
            <th><strong style="font-weight: 20px;">应收合计</strong>
                [#if money!=0]
                    (
                    [#if spReturns.trade.fee!=0]
                        手续费:${spReturns.trade.fee},
                    [/#if]

                    [#if spReturns.trade.tax!=0]
                        税金:${spReturns.trade.tax},
                    [/#if]
                    [#if spReturns.trade.freight!=0]
                        运费:${spReturns.trade.freight},
                    [/#if]

                    [#if spReturns.trade.promotionDiscount!=0]
                        促销折扣:${spReturns.trade.promotionDiscount},
                    [/#if]

                    [#if spReturns.trade.couponDiscount!=0]
                        商家优惠券抵扣:${spReturns.trade.couponDiscount},
                    [/#if]

                    [#if spReturns.trade.discount!=0]
                        平台优惠券抵扣:${spReturns.trade.discount}
                    [/#if]

                    [#if spReturns.offsetAmount!=0]
                        调整金额:${spReturns.offsetAmount}
                    [/#if]
                    )
                [/#if]
                ：</th>
            <td style="text-align: center;">${spReturns.trade.amount}</td>
          </tr>
        </table>
        <table class="table tabs">
          <tr style="">
            <th>
                服务商:
            </th>
            <td>
            ${spReturns.trade.tenant.name}
            </td>
            <th>
                电话:
            </th>
            <td>
            ${spReturns.trade.tenant.telephone}
            </td>
            <th>
                官方网址:
            </th>
            <td>
            ${setting.webSite}
            </td>
          </tr>
        </table>
    </div>
    [/#list]
</div>
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript">
  $().ready(function () {
    var $print = $("#print");
    //打印
    $print.click(function () {
        window.print();
        $.ajax({
            url:"${base}/helper/member/trade/return/print/times/${spReturns.id}.jhtml",
            type:'post',
            dataType:"jsonp",
            jsonp: "callback",
            success:function(result){
            }
        });

    });

  });
</script>
</body>
</html>