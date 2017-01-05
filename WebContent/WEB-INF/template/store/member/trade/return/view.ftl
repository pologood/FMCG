<!DOCTYPE html>
<html>

  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>我的订单</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> [#include "/store/member/include/bootstrap_css.ftl"]
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
  </head>

  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">
      [#include "/store/member/include/header.ftl"]
      <!-- Left side column. contains the logo and sidebar -->
      [#include "/store/member/include/menu.ftl"]
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>我的退货单<small>查询及管理我店内单的退货单、并对退货进行处理等操作。</small>  </h1>
          <ol class="breadcrumb">
            <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
            <li><a href="${base}/store/member/trade/list.jhtml">我的订单</a></li>
            <li><a href="${base}/store/member/trade/return/list.jhtml">退货管理</a></li>
            <li class="active">查看</li>
          </ol>
        </section>
        <!-- Main content -->
        <section class="content">
          <div class="row">
            <div class="col-md-12">
              <div class="nav-tabs-custom">
                <ul class="nav nav-tabs pull-right">
                  <li class="pull-left header"><i class="fa fa-file-text"></i>我的退货单</li>
                </ul>

                <div class="tab-content">
                  <div class='tab'>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                      <li role="presentation" class="active">
                        <a href="#s1" role="tab" data-toggle="tab">订单信息</a>
                      </li>
                      <li role="presentation">
                        <a href="#s2" role="tab" data-toggle="tab">退货信息</a>
                      </li>
                      <li role="presentation">
                        <a href="#s3" role="tab" data-toggle="tab">退款信息</a>
                      </li>
                    </ul>
                    <!-- Tab panes -->
                    <div class="tab-content bt-none mt10">
                      <div role="tabpanel" class="tab-pane active" id="s1">
                        <!--按钮组【-->
                        [#if spReturn.returnStatus=='unconfirmed']
                        <div class="form-horizontal">
                          <div class="form-horizontal form-group">
                            <div class="col-sm-offset-0 col-sm-2">
                              [#if versionType==1]
                              <input type="button" class="btn btn-block btn-primary" data-toggle="modal" value="确认受理" id="agree_return_button">
                              [#else]
                              <input type="button" class="btn btn-block btn-primary" data-toggle="modal" value="确认退货" id="agree_return_button">
                              [/#if]
                            </div>
                            <div class="col-sm-offset-0 col-sm-2">
                              <input type="button" class="btn btn-block btn-default" data-toggle="modal" value="拒绝退货" id="refused_return_button">
                            </div>
                            <div class="col-sm-offset-0 col-sm-2">
                              <input type="button" class="btn btn-block btn-default" data-toggle="modal" value="调价" id="update_price_button">
                            </div>
                          </div>
                        </div> 
                        [#elseif spReturn.returnStatus=='audited']
                        <div class="form-horizontal">
                          <div class="form-horizontal form-group">
                            <div class="col-sm-offset-0 col-sm-2">
                              <input type="button" class="btn btn-block btn-primary" data-toggle="modal" value="同意退货" id="agree_return_button">
                            </div>
                          </div>
                        </div>
                        [/#if]
                        <!--按钮组】-->
                        <table class="table table-bordered odder">
                          <tbody>
                            <tr>
                              <td>订单编号:</td>
                              <td>${trade.order.sn}</td>
                              <td>创建日期:</td>
                              <td>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                            </tr>
                            <tr>
                              <td>订单状态:</td>
                              <td>
                                ${message("Order.OrderStatus." + trade.orderStatus)}
                                [#if trade.order.expired]
                                    <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("admin.order.hasExpired")})</span>
                                [#elseif trade.order.expire??]
                                    <span title="${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")}">(${message("Order.expire")}: ${trade.order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
                                [/#if]
                              </td>
                              <td>支付状态:</td>
                              <td>${message("Order.PaymentStatus." + trade.paymentStatus)}</td>
                            </tr>
                            <tr>
                              <td>配送状态:</td>
                              <td>${message("Order.ShippingStatus." + trade.shippingStatus)}</td>
                              <td>支付方式:</td>
                              <td>${trade.order.paymentMethodName}</td>
                            </tr>
                            <tr>
                              <td>订单总价:</td>
                              <td>${currency(trade.amount, true)}</td>
                              <td>调整金额:</td>
                              <td>${currency(trade.offsetAmount, true)}</td>
                            </tr>
                            <tr>
                              <td>商家折扣:</td>
                              <td>${currency(trade.couponDiscount, true)}</td>
                              <td>平台折扣:</td>
                              <td> ${currency(trade.discount, true)}</td>
                            </tr>
                            <tr>
                              <td>商品重量:</td>
                              <td>${trade.weight}</td>
                              <td>商品数量:</td>
                              <td>${trade.quantity}</td>
                            </tr>
                            <tr>
                              <td>运费:</td>
                              <td>${currency(trade.freight, true)}</td>
                              <td>佣金:</td>
                              <td>${currency(trade.totalProfit+trade.agencyAmount, true)}</td>
                            </tr>
                            [#if trade.order.isInvoice]
                            <tr>
                              <td>发票抬头:</td>
                              <td>${trade.order.invoiceTitle}</td>
                              <td>税收:</td>
                              <td>${currency(trade.tax, true)}</td>
                            </tr>
                            [/#if]
                            <tr>
                              <td>用户名:</td>
                              <td>${trade.order.member.username}</td>
                              <td>配送方式:</td>
                              <td>${trade.order.shippingMethodName} (提货码:${trade.sn})</td>
                            </tr>
                            <tr>
                              <td>收货人:</td>
                              <td>${trade.order.consignee}</td>
                              <td>地区:</td>
                              <td>${trade.order.areaName}</td>
                            </tr>
                            <tr>
                              <td>地址:</td>
                              <td>${trade.order.address}</td>
                              <td>邮编</td>
                              <td>${trade.order.zipCode}</td>
                            </tr>
                            <tr>
                              <td>电话:</td>
                              <td>${trade.order.phone}</td>
                              <td>附言:</td>
                              <td>${trade.memo}</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                      <div role="tabpanel" class="tab-pane" id="s2">
                        <table id="" class="table table-bordered table-striped ">
                          <thead>
                            <tr>
                              <th>商品编号</th>
                              <th>商品名称</th>
                              <th>商品价格</th>
                              <th>发货数量</th>
                              <th>退货数量</th>
                              <th>小计</th>
                            </tr>
                          </thead>
                          <tbody>
                            [#list spReturn.returnsItems as returnItem]
                            <tr>
                              <tr>
                                <td>${returnItem.sn}</td>
                                <td width="400">
                                  <span title="${returnItem.orderItem.fullName}">$${abbreviate(returnItem.orderItem.fullName, 50, "...")}
                                  </span>[#if returnItem.orderItem.isGift]<span class="red">[赠品]</span>[/#if]
                                </td>
                                <td>[#if !returnItem.orderItem.isGift]${currency(returnItem.orderItem.price, true)}[#else]-[/#if]</td>
                                <td>${returnItem.shippedQuantity}</td>
                                <td>${returnItem.returnQuantity}</td>
                                <td>[#if !returnItem.orderItem.isGift]${currency(returnItem.returnQuantity*returnItem.orderItem.price, true)}[#else]-[/#if]</td>
                              </tr>
                            </tr>
                            [/#list]
                          </tbody>
                        </table>
                      </div>
                      <div role="tabpanel" class="tab-pane" id="s3">
                        <table id="" class="table table-bordered table-striped ">
                          <thead>
                            <tr>
                              <th>退货金额</th>
                              <th>结算金额</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <tr>
                                <td>${spReturn.amount}</td>
                                <td>${spReturn.cost}</td>
                              </tr>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                      <div class="form-horizontal">
                        <div class="form-horizontal form-group">
                          <div class="col-sm-offset-10 col-sm-2">
                              <input type="button" class="btn btn-block btn-default" value="返回" onclick="javascript:history.back();">
                          </div>
                        </div>
                      </div>
                      <div class="kong"></div>
                      <!-- 同意退货弹框【 -->
                      <div class="modal fade" id="agree_return" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
                        <div class="modal-dialog">
                          <input type="hidden" name="id" value="">
                          <div class="modal-content" style=" border-radius: 5px;">
                            <div class="modal-header">
                              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                              <h4 class="modal-title">同意退货</h4>
                            </div>
                            <div class="modal-body">
                              <lebal>是否确认此操作？</lebal>
                            </div>
                            <div class="modal-footer">
                              <div class="col-sm-offset-8 col-sm-2">
                                <button type="button" class="btn btn-block btn-primary" onclick=[#if versionType==1]"agree_return('true','','jdh')"[#else]"agree_return('true','','other')"[/#if]>确定</button>
                              </div>
                              <div class="col-sm-offset-0 col-sm-2">
                                <input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <!--同意退货弹框】 -->
                      <!-- 拒绝退货弹框【 -->
                      <div class="modal fade" id="refused_return" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
                        <div class="modal-dialog">
                          <form class="form-horizontal" role="form" action="rejected.jhtml" method="post">
                            <input type="hidden" name="id" value="">
                            <div class="modal-content" style=" border-radius: 5px;">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title">拒绝退货</h4>
                              </div>
                              <div class="modal-body">
                                <div class="form-group">
                                <label for="inputExperience" class="col-sm-2 control-label">拒绝理由</label>
                                <div class="col-sm-8">
                                  <textarea class="form-control" id="refused_content"></textarea>
                                </div>
                              </div>
                              </div>
                              <div class="modal-footer">
                                <div class="col-sm-offset-8 col-sm-2">
                                  <button type="button" class="btn btn-block btn-primary" id="confirm_refused_return">确定</button>
                                </div>
                                <div class="col-sm-offset-0 col-sm-2">
                                  <input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
                                </div>
                              </div>
                            </div>
                          </form>
                        </div>
                      </div>
                      <!--拒绝退货弹框】 -->
                      <!-- 调价弹框【 -->
                      <div class="modal fade" id="update_order_amount" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
                        <div class="modal-dialog form-horizontal">
                          <div class="modal-content" style=" border-radius: 5px;">
                            [#assign returnAmount=0]
                            [#assign returnSettle=0]
                            [#list spReturn.returnsItems as spReturnItems]
                                [#assign returnAmount=returnAmount+spReturnItems.returnQuantity*spReturnItems.orderItem.price]
                                [#assign returnSettle=returnSettle+spReturnItems.returnQuantity*spReturnItems.orderItem.cost]
                            [/#list]
                            <div class="modal-header">
                              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                              <h4 class="modal-title">调价</h4>
                            </div>
                            <div class="modal-body">
                              <div class="form-group">
                                <label for="inputExperience" class="col-sm-2 control-label">退货金额</label>
                                <div class="col-sm-6">
                                  <input type="text" class="form-control" id="adjAmount" value="${returnAmount}" onkeyup="adjust_amount('amount',this)">
                                </div>
                               
                                  <span style="color:red;line-height:2.39;">(可调范围：0-${returnAmount})</span>
                               
                              </div>
                              <div class="form-group">
                                <label for="inputName" class="col-sm-2 control-label">结算金额</label>
                                <div class="col-sm-6">
                                  <input type="text" class="form-control" id="adjFreight" value="${returnSettle}" onkeyup="adjust_amount('cost',this)">
                                </div>
                                
                                  <span style="color:red;line-height:2.39;">(可调范围：0-${returnSettle})</span>
                                
                              </div>
                            </div>
                            <div class="modal-footer">
                              <div class="col-sm-offset-8 col-sm-2">
                                <button type="button" class="btn btn-block btn-primary" id="confirm_adj_amount">确定</button>
                              </div>
                              <div class="col-sm-offset-0 col-sm-2">
                                <input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
                              </div>
                            </div>
                          </div>
                          
                        </div>
                      </div>
                      <!--调价弹框】 -->
                    </div>
                  </div>
                </div>

              </div>
            </div>
          </div>
        </section>
        <!-- /.content -->
      </div>
      <!-- /.content-wrapper -->
      [#include "/store/member/include/footer.ftl"]
    </div>
    [#include "/store/member/include/bootstrap_js.ftl"]
    <script type="text/javascript" src="${base}/resources/store/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
    <script>
      $().ready(function () {
        
        // 检查锁定
        var isLocked = false;
        function checkLock() {
          if (!isLocked) {
            $.ajax({
              url: "${base}/store/member/trade/check_lock.jhtml",
              type: "POST",
              data: {id: ${trade.id}},
              dataType: "json",
              cache: false,
              success: function (message) {
                if (message.type != "success") {
                  $.message(message);
                  $("#agree_return_button").prop("disabled", true);
                  $("#refused_return_button").prop("disabled", true);
                  $("#update_price_button").prop("disabled", true);
                  isLocked = true;
                }
              }
            });
          }
        }
        // 检查锁定
        checkLock();
        setInterval(function () {
            checkLock();
        }, 10000);
       
        //调价
        $("#confirm_adj_amount").click(function(){
          //针对聚德惠月结功能
          if("${isMonthly}"=="true"){
              $.message("warn", "今日有月结操作，不能进行此操作");
              return;
          }
          $.ajax({
            url: "${base}/store/member/trade/update_return_price.jhtml",
            type: "POST",
            data: {returnId:${spReturn.id},amount:$("#adjAmount").val(),cost:$("#adjFreight").val()},
            dataType: "json",
            cache: false,
            success: function (message) {
              if (message.type != "success") {
                $.message(message);
                setTimeout(function () {
                  location.href = "${base}/store/member/trade/return/view.jhtml?spReturnsId=${spReturn.id}";
                  return false;
                }, 1000);
              } else {
                setTimeout(function () {
                  window.location.reload();//刷新页面
                  return false;
                }, 1000);
              }
            }
          });
        });
        //拒绝退货
        $("#confirm_refused_return").click(function(){
          agree_return("false",$("#refused_content").val());
        }); 
        //控制按钮是否要弹框
        $("#agree_return_button,#refused_return_button,#update_price_button").click(function(){
          //针对聚德惠月结功能
          if("${isMonthly}"=="true"){
              $.message("warn", "今日有月结操作，不能进行此操作");
              return;
          }
          $("#agree_return_button").attr("data-target","#agree_return");
          $("#refused_return_button").attr("data-target","#refused_return");
          $("#update_price_button").attr("data-target","#update_order_amount");
        });          
      });
      //退货
      function agree_return(obj,content,typ){
        //针对聚德惠月结功能
        if("${isMonthly}"=="true"){
            $.message("warn", "今日有月结操作，不能进行此操作");
            return;
        }
        $.ajax({
          url:"${base}/store/member/trade/return/confirm_return.jhtml",
          type:"post",
          data:{id:${spReturn.id},flag:obj,content:content,app_type:typ},
          dataType:"json",
          success:function(data){
              if(data.type=="success"){
                  $.message("success","操作成功");
                  location.reload();
              }else{
                  $.message(data.type,data.content)
              }
          }
        });
      }
      //判断金额范围
      function adjust_amount(obj,ths){
        if(parseInt($(ths).val())<0){
          $.message("warn","能为负数！");
          if(obj=="amount"){
            $("#adjAmount").val("${returnAmount}");
          }else{
            $("#adjFreight").val("${returnSettle}");
          }
        }
      }
    </script>
  </body>
</html>