<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>订单打印</title>
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
</head>
<body>
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
    <table class="table">
      <tr>
        <th>&nbsp;</th>
        <th>商品条码</th>
        <th>${message("ShippingItem.name")}</th>
        <th>数量</th>
        <th>单价</th>
        <th>金额</th>
      </tr>
      [#assign quantitys=0 prices=0]
      [#list order as orderItem]
        <tr style="color: #666">
          <td style="text-align: right">${orderItem_index+1}&nbsp;&nbsp;</td>
          <td style="width: 16%">${orderItem.barcode}</td>
          <td style="width: 60%"><span
                  title="${orderItem.fullName}">${abbreviate(orderItem.fullName, 100, "...")}</span>
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
        <tr style="color: #666">
          <td colspan="3" style="text-align: center">合计</td>
          <td>${quantitys}</td>
          <td></td>
          <td>${prices}</td>
        </tr>
    </table>
    <table class="table tabs">
      <tr>
        <th>订单合计：</th>
        <td>${trade.price}</td>
        [#assign money=trade.tax+trade.freight+trade.promotionDiscount+trade.couponDiscount+trade.discount+trade.offsetAmount]
        <th colspan="3"><strong style="font-weight: 20px;">应收合计</strong>
          [#if money!=0]
            (
            [#if trade.fee!=0]
                手续费:${trade.fee}元&nbsp;
            [/#if]

            [#if trade.tax!=0]
                税金:${trade.tax}元&nbsp;
            [/#if]
            [#if trade.freight!=0]
                运费:${trade.freight}元&nbsp;
            [/#if]

            [#if trade.promotionDiscount!=0]
                促销折扣:${trade.promotionDiscount}元&nbsp;
            [/#if]

            [#if trade.couponDiscount!=0]
                商家优惠券抵扣:${trade.couponDiscount}元&nbsp;
            [/#if]

            [#if trade.discount!=0]
                平台优惠券抵扣:${trade.discount}元&nbsp;
            [/#if]

            [#if trade.offsetAmount!=0]
                调整金额:${trade.offsetAmount}元&nbsp;
            [/#if]
            )
          [/#if]
          </th>
        <td style="text-align: center;">${trade.amount}</td>
      </tr>
    </table>
    <table class="table tabs">
        <tr style="">
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
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript">
  $().ready(function () {
    var $print = $("#print");
    //打印
    $print.click(function () {
        window.print();
        $.ajax({
            url:"${base}/helper/member/trade/print/times/${trade.id}.jhtml",
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
