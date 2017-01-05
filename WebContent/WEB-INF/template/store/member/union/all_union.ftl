<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>全部商盟</title>
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
        <h1>商家联盟<small>管理我的加入的商盟</small>  </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/union/all_union.jhtml">全部商盟</a></li>
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
                <li class="active"><a href="${base}/store/member/union/all_union.jhtml">全部商盟</a></li>
                <li class=""><a href="${base}/store/member/union/my_union.jhtml">我的商盟</a></li>
                <li class="pull-left header"><i class="fa fa-th"></i>商家联盟</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form class="form-horizontal" id="listForm" action="all_union.jhtml" method="get" role="form">
                  <div class="row mtb10">
                    <div class="col-sm-12">
                      <div class="btn-group">
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
                  </div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                      <table id="listTable" class="table table-bordered table-hover">
                        <thead>
                          <tr>
                            <th>商盟名称</th>
                            <th>商盟背景图</th>
                            <th>商盟佣金</th>
                            <th>年费</th>
                            <th>商家数量</th>
                            <th>操作</th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as union]
                          <tr>
                            <td>${union.name}</td>
                            <td><img src="${union.image}" width="30px" height="30px"></td>
                            <td>${union.brokerage*100}%</td>
                            <td>${union.price}&nbsp;元/年</td>
                            <td>${union.tenantNumber}</td>
                            <td>
                              [#if has_union!="none"]
                                [#if has_union==union&&union_status=="unconfirmed"]
                                已申请
                                <input class="btn btn-primary" type="button" value="去支付" onclick="create_payment(this,${union.price},${unionTenantId})" union-bro="${union.brokerage*100}">
                                [#elseif has_union==union&&union_status=="confirmed"]
                                <input class="btn btn-info" type="button" value="查看店铺" onclick="location.href='union_tenant_list.jhtml?unionId=${union.id}'" >
                                <input class="btn btn-default" type="button" value="退出商盟" data-toggle="modal" onclick="return_union(this)" union-id="${union.id}">
                                [/#if]
                              [#else]
                                <input class="btn btn-primary" type="button" value="申请加入"  onclick="apply_union(this,${union.price})" union-id="${union.id}" union-bro="${union.brokerage*100}">
                              [/#if]
                            </td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                        <!-- 申请加入商盟弹框【 -->
                        <input type="hidden" id="apply_union_input" data-toggle="modal" data-target="#apply_union" onclick="apply_union_alert()">
                        <div class="modal fade" id="apply_union" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display:none;">
                          <div class="modal-dialog">
                            <form class="form-horizontal" role="form" action="" method="post">
                              <div class="modal-content" style=" border-radius: 5px;">
                                <div class="modal-header">
                                  <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                  <h4 class="modal-title">申请加入商盟</h4>
                                </div>
                                <div class="modal-body" style="color:#428bca;">
                                  <img src="" style="width:200px;height:200px;" id="weixin_qr">
                                  <div style="float:right;width:350px;">
                                      <h3 style="text-align:center;font-weight:bold;">扫描左边二维码申请加入</h3>
                                      <h3>加盟费用：<span id="union_price"></span>元/每年</h3>
                                      <h4 id="union_brokerage">如果您的佣金低于<span></span>%，加入商盟后系统会默认调整到<span></span>%</h4>
                                  </div>
                                </div>
                              </div>
                            </form>
                          </div>
                        </div>
                        <!--申请加入商盟弹框】 -->
                        <!--退出商盟弹框【 -->
                        <div class="modal fade" id="return_union" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
                          <div class="modal-dialog">
                            <form class="form-horizontal" role="form" action="" method="post">
                              <input type="hidden" id="union_id" value="">
                              <div class="modal-content" style=" border-radius: 5px;">
                                <div class="modal-header">
                                  <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                  <h4 class="modal-title">拒绝退货</h4>
                                </div>
                                <div class="modal-body">
                                  <lebal>是否确认此操作？确定后不可还原（年费不予退回，望了解）。</lebal>
                                </div>
                                <div class="modal-footer">
                                  <div class="col-sm-offset-8 col-sm-2">
                                    <button type="button" class="btn btn-block btn-primary" onclick="confirmed_return_union()">确定</button>
                                  </div>
                                  <div class="col-sm-offset-0 col-sm-2">
                                    <input type="button" class="btn btn-block btn-default" value="取消" data-dismiss="modal">
                                  </div>
                                </div>
                              </div>
                            </form>
                          </div>
                        </div>
                        <!--退出商盟弹框】 -->
                      <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/store/member/include/pagination.ftl"]
                        [/@pagination]
                      </div>
                    </div>

                    <!-- /.box-body -->
                  </div>
                  <!-- /.tab-pane -->
                </form>
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
  <script type="text/javascript">
    /*申请加入联盟，生成uniontenant*/
    function apply_union(obj,price){
      $.ajax({
        url:'${base}/store/member/union/create_unionTenant.jhtml',
        data:{
          unionId:$(obj).attr("union-id")
        },
        type:'post',
        dataType:'json',
        success:function(message){
          if(message.type=="success"){
            create_payment(obj,price,message.content);
          }else{
            $.message(message);
            window.document.reload();
          }
        }
      });
    }
    /*控制申请弹出框*/
    function apply_union_alert(){
      $("#apply_union").attr("data-target","#apply_union");
    }
    /*控制退出弹出框*/
    function return_union(obj){
      $(obj).attr("data-target","#return_union");
      $("#union_id").val($(obj).attr("union-id"));
    }

    /*创建支付单*/
    function create_payment(obj,price,unionTenantId){
      $("#union_price").text(price);
      $("#union_brokerage").find("span").text($(obj).attr("union-bro"));
      $("#apply_union_input").click();
      $.ajax({
        url:'${base}/store/payment/create.jhtml',
        type:"POST",
        data:{
          paymentPluginId:'weixinQrcodePayPlugin',
          amount:price,
          type:"function"
        },
        dataType:"JSON",
        success:function(data){
          if(data.message.type!="success"){
            $.message("error",data.message.content);
            return;
          }
          $.ajax({
            url:"http://api.wwei.cn/wwei.html?apikey=20160929094404&data="+data.data.code_url,
            type:'post',
            dataType:"jsonp",
            jsonp: "callback",
            success:function(result){
              $(document).unbind("ajaxBeforeSend");
              $("#weixin_qr").attr("src",result.data.qr_filepath);
              InterValObj=window.setInterval(function(){
                queryCash(data.data.sn,obj,unionTenantId);
              },3000);
            }
          });
        }
      });
    }
    var flag="false";
    /*查询是否知支付成功*/
    function queryCash(sn,obj,unionTenantId){
      $.ajax({
        url:'${base}/store/payment/weixin_payment.jhtml',
        data:{
          sn:sn
        },
        type:'post',
        dataType:'json',
        success:function(dataBlock){
          if(dataBlock.message.type=="success"){
            if(flag=="false"){
              update_unionTenant_status(obj,unionTenantId);
            }
          }
          if(dataBlock.message.type=="error"){
            window.clearInterval(InterValObj);
            $.message(dataBlock.message);
          }

        }
      });
    }
    /*支付成功后修改申请的状态*/
    function update_unionTenant_status(obj,unionTenantId){
      flag='true';
      $.ajax({
        url:'${base}/store/member/union/update_unionTenant.jhtml',
        data:{
          unionTenantId:unionTenantId
        },
        type:'post',
        dataType:'json',
        success:function(message){
          if(message.type=="success"){
            $.message("success","操作成功，您已是该商盟的一员。")
            location.href="${base}/store/member/union/my_union.jhtml";
          }
        }
      });
    }
    /*退出商盟*/
    function confirmed_return_union(){
      $.ajax({
        url:'${base}/store/member/union/return_union.jhtml',
        data:{
          unionId:$("#union_id").val()
        },
        type:'post',
        dataType:'json',
        success:function(message){
          $.message(message);
          if(message.type=="success"){
            location.href="${base}/store/member/union/all_union.jhtml";
          }
        }
      });
    }
  </script>
</body>
</html>
