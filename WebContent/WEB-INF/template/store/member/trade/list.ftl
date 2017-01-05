<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>订单管理</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" href="${base}/resources/store/css/style.css">
  <link rel="stylesheet" href="${base}/resources/store/css/common.css" />
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
      <h1>
        订单管理
        <small>显示和管理店铺内的订单</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
        <li><a href="${base}/store/member/trade/list.jhtml">我的订单</a></li>
        <li class="active">订单管理</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <!-- Left col -->
        <div class="col-md-12">
          <!-- Custom Tabs (Pulled to the right) -->
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs pull-right">
              <li class="[#if type=='cancelled']active[/#if]">
                <a href="${base}/store/member/trade/list.jhtml?type=cancelled">已取消<span class="red">&nbsp;${cancelledCount!0}</span></a>
              </li>
              <li class="[#if type=='completed']active[/#if]">
                <a href="${base}/store/member/trade/list.jhtml?type=completed">已完成<span class="red">&nbsp;${completedCount!0}</span></a>
              </li>
              <li class="[#if type=='unreturned']active[/#if]">
                <a href="${base}/store/member/trade/list.jhtml?type=unreturned">退货中<span class="red">&nbsp;${shippedApplyCount!0}</span></a>
              </li>
              <li class="[#if type=='shipped']active[/#if]">
                <a href="${base}/store/member/trade/list.jhtml?type=shipped">已发货<span class="red">&nbsp;${shippedCount!0}</span></a>
              </li>
              <li class="[#if type=='unpaid']active[/#if]">
                <a href="${base}/store/member/trade/list.jhtml?type=unpaid">待付款<span class="red">&nbsp;${unpaidCount!0}</span></a>
              </li>
              <li class="[#if type=='unshipped']active[/#if]">
                <a href="${base}/store/member/trade/list.jhtml?type=unshipped">待发货<span class="red">&nbsp;${unshippedCount!0}</span></a>
              </li>
              <li class="pull-left header"><i class="fa fa-th"></i>订单管理</li>
            </ul>
            <div class="tab-content" style="padding:15px 0 0 0;">
              <form class="form-horizontal" id="listForm" action="list.jhtml" method="get">
                <input type="hidden" id="type" name="type" value="${type}"/>
                <div class="row mtb10">
                  <div class="col-sm-7">
                    <div class="btn-group">
                      <!--批量发货【-->
                      [#if type=='unshipped']
                        <button type="button" class="btn btn-default btn-sm disabled" data-toggle="modal" data-target="" id="batch_ship_button">
                          批量发货
                        </button>
                      [/#if]
                      <div class="modal fade" id="batch_ship" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                          <div class="modal-content" style="border-radius: 5px;">
                            <div class="modal-header">
                              <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                              <h4 class="modal-title" id="myModalLabel">批量发货</h4>
                            </div>
                            <div class="modal-body" style="text-align: center;">
                              <div class="form-group" >
                                <label class="col-sm-2 control-label">配送方式</label>
                                <div class="col-sm-8">
                                  <select class="form-control" name="shippingMethodId" id="shippingMethodId">
                                    <option value="">请选择配送方式</option>
                                    [#list shippingMethods as shippingMethod]
                                    <option value="${shippingMethod.id}">${shippingMethod.name}</option>
                                    [/#list]
                                  </select>
                                </div>
                                <label class="col-sm-2 text-lf mt5 red dis-none" id="bit1">必填</label>
                              </div>
                              <div class="form-group" >
                                <label class="col-sm-2 control-label">快递公司</label>
                                <div class="col-sm-8">
                                  <select class="form-control" name="deliveryCorpId" id="deliveryCorpId">
                                    <option value="">请选择快递公司</option>
                                    [#list deliveryCorps as deliveryCorp]
                                      <option value="${deliveryCorp.id}">${deliveryCorp.name}</option>
                                    [/#list]
                                  </select>
                                </div>
                                <label class="col-sm-2 text-lf mt5 red dis-none" id="bit2">必填</label>
                              </div>
                              <div class="form-group">
                                <label class="col-sm-2 control-label">运单号(员)</label>
                                <div class="col-sm-9">
                                  <input type="text" class="form-control" name="tracking_no" value="">
                                </div>
                              </div>
                            </div>
                            <div class="modal-footer" style="text-align: center;">
                              <div class="col-sm-offset-8 col-sm-2">
                                <button type="button" class="btn btn-block btn-primary" id="batch_shipping">确定</button>
                              </div>
                              <div class="col-sm-offset-0 col-sm-2">
                                <button type="button" class="btn btn-block btn-default" data-dismiss="modal">返回</button>
                              </div>
                            </div>                                   
                          </div>
                        </div>
                      </div>
                      <!--批量发货】-->
                      [#if type=='unshipped']
                        <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="" id="all_export_button" onclick="all_export()">
                          一键导出
                        </button>
                      [/#if]
                      <button type="button" class="btn btn-default btn-sm" onclick="javascript:location.href='search.jhtml'">
                        <i class="fa fa-suitcase mr5"></i>提货码
                      </button>
                      <button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();">
                        <i class="fa fa-refresh mr5"></i> 刷新
                      </button>

                      <div class="dropdown fl ml5">
                        <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                            每页显示<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="pageSizeOption">
                          <li role="presentation" class="[#if page.pageSize==10]active[/#if]">
                            <a role="menuitem" tabindex="-1" val="10">10</a>
                          </li>
                          <li role="presentation" class="[#if page.pageSize==20]active[/#if]">
                            <a role="menuitem" tabindex="-1" val="20">20</a>
                          </li>
                          <li role="presentation" class="[#if page.pageSize==30]active[/#if]">
                            <a role="menuitem" tabindex="-1" val="30">30</a>
                          </li>
                          <li role="presentation" class="[#if page.pageSize==40]active[/#if]">
                            <a role="menuitem" tabindex="-1" val="40">40</a>
                          </li>
                        </ul>
                      </div>
                    </div>    
                  </div>
                  <div class="col-sm-5">
                    <div class="box-tools fr">
                      <div class="input-group input-group-sm" style="width: 150px;">
                        <input type="text" class="form-control pull-right" id="searchValue" id="keyword" name="keyword" value="${keyword}" placeholder="搜索订单号、收货人、提货码">
                        <div class="input-group-btn">
                          <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="box" style="border-top:0px;">
                  <div class="box-body">
                    <table id="listTable" class="table table-bordered table-hover">
                      <thead>
                        <tr>
                          [#if type=='unshipped']<th><input type="checkbox" id="selectAll"/></th>[/#if]
                          <th>创建日期</th>
                          <th>订单编号</th>
                          <th>订单状态</th>
                          <th>支付状态</th>
                          <th>配送状态</th>
                          <th>订单金额</th>
                          <th>收货人</th>
                          <th>打印次数</th>
                          <th>操作</th>
                        </tr>
                      </thead>
                      <tbody>
                        [#list page.content as trade]
                        <tr>
                          [#if type=='unshipped']
                          <td><input type="checkbox" name="ids" value="${trade.id}"/></td>
                          [/#if]
                          <td>${trade.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                          <td>${trade.order.sn}</td>
                          <td>
                          	${message("Order.OrderStatus." + trade.orderStatus)}
                        	[#if trade.order.expired]
                        		<span style="color:red;">(${message("admin.order.hasExpired")})</span>
                        	[/#if]
                          </td>
                          <td>${message("Order.PaymentStatus." + trade.paymentStatus)}</td>
                          <td>${message("Order.ShippingStatus." + trade.shippingStatus)}</td>
                          <td>${currency(trade.amount, true)}</td>
                          <td>${trade.order.consignee}</td>
                          <td>${trade.print}</td>
                          <td>
                          	[@helperRole url="store/member/trade/list.jhtml" type="read"]
                              [#if helperRole.retOper!="0"]
                                  <a href="view.jhtml?id=${trade.id}">[${message("admin.common.view")}]</a>
                              [/#if]
                          [/@helperRole]
                          &nbsp;&nbsp;
                          [@helperRole url="store/member/trade/list.jhtml" type="print"]
                              [#if helperRole.retOper!="0"]
                                  <a href="${base}/store/member/trade/print.jhtml?id=${trade.id}" target="_blank">[打印]</a>
                              [/#if]
                          [/@helperRole]
                          &nbsp;&nbsp;
                          [@helperRole url="store/member/trade/shipping_export.jhtml" type="print"]
                            <a href="javascript:;" target="_blank" onclick="batch_export(${trade.id})">[导出]</a>
                          [/@helperRole]
                          </td>
                        </tr>
                        [/#list]
                      </tbody>
                    </table>
                    <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                    	[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			                    [#include "/store/member/include/pagination.ftl"]
			                [/@pagination]
                    </div>
                  </div>
                  <!-- /.box-body -->
                </div>
              </form>
              <!-- /.tab-pane -->
            </div>
            <!-- /.tab-content -->
          </div>
          <!-- nav-tabs-custom -->
        </div>
      </div>
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  [#include "/store/member/include/footer.ftl"]
</div>
[#include "/store/member/include/bootstrap_js.ftl"]
<script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<script>
    $().ready(function () {
      var $listForm = $("#listForm");
      var $selectAll = $("#selectAll");
      var $ids = $("#listTable input[name='ids']");
      // cehckbox全选
      $selectAll.click( function() {
        var $this = $(this);
        $this.prop("checked")?$ids.prop("checked", true):$ids.prop("checked", false);
        if($("#listTable input[name='ids']:checked").size()<=0){
          $("#batch_ship_button").addClass("disabled");
          $("#batch_ship_button").attr("data-target","");
        }else if($("#listTable input[name='ids']:checked").size()>0){
          $("#batch_ship_button").removeClass("disabled");
          $("#batch_ship_button").attr("data-target","#batch_ship");
        }
      });
      $ids.click(function(){
        if($("#listTable input[name='ids']:checked").size()<=0){
          $("#batch_ship_button").addClass("disabled");
          $("#batch_ship_button").attr("data-target","");
        }else if($("#listTable input[name='ids']:checked").size()>0){
          $("#batch_ship_button").removeClass("disabled");
          $("#batch_ship_button").attr("data-target","#batch_ship");
        }
      });
      //确认批量发货
      $("#batch_shipping").on("click",function(){
        if($("#shippingMethodId").val()==""){
          $("#bit1").show();
          setTimeout(function() {
            $("#bit1").hide();
          }, 2000);
          return;
        };
        if($("#deliveryCorpId").val()==""){
          $("#bit2").show();
          setTimeout(function() {
            $("#bit2").hide();
          }, 2000);
          return;
        };
        var ids=[];
        $.each($("#listTable input[name='ids']:checked"),function(i,item){
            ids.push($(this).val())
        });
        if(ids==""){
            $.message("warn","您还你还没选中订单")
            return;
        }
        $.ajax({
            url:"${base}/store/member/trade/batch_shipping.jhtml",
            type:"post",
            traditional: true,
            data:{
                tradeIds:ids,
                shippingMethodId:$("#shippingMethodId").val(),
                deliveryCorpId:$("#deliveryCorpId").val(),
                trackingNo:$("#tracking_no").val()
                },
            dataType:"json",
            success:function(message){
            	$.message(message.message);
                if(message.message.type=="success"){
                    location.href="${base}/store/member/trade/list.jhtml?type=shipped"
                }
            }
        });
      });
    });
    //批量导出
    function batch_export(id){
      $.message("success","正在帮您导出请稍候。。。")
      $.ajax({
          url:"${base}/store/member/trade/batch_export.jhtml",
          type:"post",
          traditional: true,
          data:{tradeId:id},
          dataType:"json",
          success:function(data){
            $.message(data.message);
            if(data.message.type=="success"){
              export_model(data.data);
              location.href="${base}/store/member/trade/list.jhtml?type=unshipped";
            }
          }
      });
    }
    //一键导出
    function all_export(){
      $.message("success","正在帮您导出请稍候。。。")
      $.ajax({
          url:"${base}/store/member/trade/all_export.jhtml",
          type:"post",
          traditional: true,
          dataType:"json",
          success:function(data){
            $.message(data.message);
            if(data.message.type=="success"){
              export_model(data.data);
              location.href="${base}/store/member/trade/list.jhtml?type=unshipped";
            } 
          }
      });
    }
    //导出模型
    function export_model(data){
      var html = '<table style="display:none;" class="table2excel">' +
              '<thead>' +
                '<tr>' +
                  '<th>下单日期</th>' +
                  '<th>订单单号</th>' +
                  '<th>发货地址</th>' +
                  '<th>发货人</th>' +
                  '<th>发货人联系电话</th>' +
                  '<th>收货地址</th>' +
                  '<th>收货人</th>' +
                  '<th>收货人联系方式</th>' +
                  '<th>商品名称</th>' +
                  '<th>商品编号</th>' +
                  '<th>商品条码</th>' +
                  '<th>商品价格</th>' +
                  '<th>商品数量</th>' +
                  '<th>订单金额</th>' +
                '</tr>' +
              '</thead>' +
              '<tbody>;'
      $.each(data, function (i, obj) {
        html += '<tr>' +
                  '<td>' + obj.create_date + '</td>' +
                  '<td>' + obj.order_sn + '</td>' +
                  '<td>' + obj.delivery_center_address + '</td>' +
                  '<td>' + obj.delivery_center_name + '</td>' +
                  '<td>' + obj.delivery_center_telephone + '</td>' +
                  '<td>' + obj.consignee_address + '</td>' +
                  '<td>' + obj.consignee_name + '</td>' +
                  '<td>' + obj.consignee_telephone + '</td>' +
                  '<td>' + obj.product_name + '</td>' +
                  '<td>' + obj.product_sn + '</td>' +
                  '<td>' + obj.product_barcode + '</td>' +
                  '<td>' + obj.product_price + '</td>' +
                  '<td>' + obj.product_num + '</td>' +
                  '<td>' + obj.trade_amount + '</td>' +
                '</tr>' ;
      });
      html += '</tbody></table>';
      $("#trade_wrap").html(html);
      //导出数据到excel
      $(".table2excel").table2excel({
        exclude: ".noExl",
        name: "发货信息导出",
        filename: "发货信息导出",
        fileext: ".xls",
        exclude_img: true,
        exclude_links: false,
        exclude_inputs: true
      });

    }
  </script>
  <div id="trade_wrap"></div>
</body>
</html>
