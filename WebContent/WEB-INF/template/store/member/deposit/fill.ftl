<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>账户充值</title>
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">
    [#include "/store/member/include/header.ftl"]
    [#include "/store/member/include/menu.ftl"]
    <div class="content-wrapper">
      <section class="content-header">
        <h1>
          账户充值
          <small>使用您已开通网上银行服务的银行卡进行充值</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">账户充值</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li><a href="${base}/store/member/deposit/list.jhtml">我的账单</a></li>
                <li class="active"><a href="${base}/store/member/deposit/fill.jhtml">账户充值</a></li>
                <li class="pull-left header"><i class="fa fa-cny"></i>账户充值</li>
              </ul>
              <div class="tab-content">
                <form class="form-horizontal" role="form">
                  <div class="form-group">
                    <label class="col-sm-2 control-label">当前余额</label>
                    <div class="col-sm-8">
                      <div class="form-control" style="border:0px;"><td>${currency(member.balance, true, true)}</td></div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">充值金额</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" id="amount" >
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-2">
                      <button type="button" class="btn btn-block btn-primary" id="submit">提交</button>
                    </div>
                    <div class="col-sm-offset-0 col-sm-2">
                      <button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                      <h4><strong>相关说明</strong></h4>
                      <ul class="list-number">
                        <li>支持使用信用卡、借记卡等充值，建议单笔金额不要超过5000元，大额分多笔充值。</li>
                        <li>充值金额不允许用于提取现金，充值金额提现将收取相关手续费，手续费不设上下限且没有免手续费优惠。</li>
                        <li>通过“银行汇款”和“签约账户”进行充值均不收取手续费。</li>
                        <li>银行汇款，请您在周一至周五的9：00-17：30选择“普通活期”存储方式完成柜台汇款，汇款 成功后1-2个工作日即可到账。</li>
                      </ul>
                    </div>
                  </div>
                </form>
                <div class="kong"></div>
              </div>
            </div>
          </div>
        </div>
        <!-- Modal -->
        <div class="modal" id="myModel" style="display:none;">
          <div class="modal-dialog">
            <div class="modal-content" style="left:30%;width:310px;">
              <div class="modal-header">
              <h4 class="modal-title" id="myModalLabel" style="text-align:center;font-size:20px;font-weight:bold;">使用微信扫描下方二维码即可完成充值</h4>
              </div>
              <div class="modal-body">
                <img src="" style="width:280px;height:280px;" id="weixin_qr">
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
    [#include "/store/member/include/footer.ftl"]
  </div>
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript">
    $().ready(function () {
      //==============微信充值==============
      $("#submit").click(function(){
        if($("#amount").val()==""){
          $.message("warn","请输入充值金额");
          return;
        }
        $.ajax({
          url:'${base}/store/payment/create.jhtml',
          type:"POST",
          data:{
            paymentPluginId:'weixinQrcodePayPlugin',
            amount:$("#amount").val(),
            type:"recharge"
          },
          dataType:"JSON",
          success:function(data){
            if(data.message.type!="success"){
              $.message("error",data.message.content);
              return;
            }
            $("#myModel").show();
            $.ajax({
              url:"http://api.wwei.cn/wwei.html?apikey=20160929094404&data="+data.data.code_url,
              type:'post',
              dataType:"jsonp",
              jsonp: "callback",
              success:function(result){
                $(document).unbind("ajaxBeforeSend");
                $("#weixin_qr").attr("src",result.data.qr_filepath);
                InterValObj=window.setInterval(function(){
                  queryCash(data.data.sn);
                },3000);
              }
            });
          }
        });
      });
      $("#myModel").click(function(){
        $(this).css("display")=="none"?"":$(this).hide();
      });
    });
    /*微信支付*/
    function queryCash(sn){
      $.ajax({
        url:'${base}/b2b/payment/weixin_payment.jhtml',
        data:{
          sn:sn
        },
        type:'post',
        dataType:'json',
        success:function(dataBlock){

          if(dataBlock.message.type=="success"){
            $.message(dataBlock.message);
            window.clearInterval(InterValObj);
            location.reload();
          }
          if(dataBlock.message.type=="error"){
            window.clearInterval(InterValObj);
            $.message(dataBlock.message);
          }

        }
      });
    }
  </script>
</body>
</html>
