<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>银行汇款</title>
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
          银行汇款
          <small>支持国内各大银行借记卡往来结算支付申请，快捷方便！</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active"> 银行汇款</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li><a href="${base}/store/member/cash/list.jhtml">汇款记录</a></li>
                <li  class="active"><a href="${base}/store/member/cash/index.jhtml">银行汇款</a></li>
                <li class="pull-left header"><i class="fa fa-money" ></i>银行汇款</li>
              </ul>
              <div class="tab-content" >
                <form class="form-horizontal" id="inputForm" name="inputForm" action="submit.jhtml" method="post">
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">选择银行卡</label>
                    <div class="col-sm-4">
                      <select class="form-control" id="memberBankId" name="memberBankId">
                        <option value="#" selected="selected">选择银行</option>
                        <option value="${base}/store/member/cash/edit.jhtml">添加银行卡</option>
                        [#list options.keySet() as key]
                        <option value="${key}" >${options.get(key)}</option>
                        [/#list]
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">提现金额</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control"  id="amount" name="amount">
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label"></label>
                    <div class="col-sm-4">
                      <p class="bg-warning bg-box">单笔金额不能超过50000元</p>
                    </div>
                  </div>

                  <div class="form-group">
                    <label class="col-sm-2 control-label">手续费</label>
                    <div class="col-sm-6">
                      <div class="form-control" id="fee"></div>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-2">
                      <button type="submit" class="btn btn-block btn-primary" id="btn_repay" id="submit">立刻提现</button>
                    </div>
                  </div>
                </form>
                <table class="table table-bordered table-striped">
                  <thead>
                    <tr>
                      <th>开户行</th>
                      <th>卡号</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    [#list memberBanks as memberBank ]
                    <tr>
                      <td>${memberBank.depositBank}</td>
                      <td>${memberBank.cardNo}</td>
                      <td><a href="${base}/store/member/cash/delete.jhtml?id=${memberBank.id}">删除</a></td>
                    </tr>
                    [/#list]
                  </tbody>
                </table>
                <div class="row">
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

              </div>

            </div>
          </div>
        </div>
      </section>
    </div>
    [#include "/store/member/include/footer.ftl"]
  </div>
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/ePayBank.js"></script>
  <script type="text/javascript">
    $().ready(function () {
      var $inputForm = $("#inputForm");
      var $amount = $("#amount");
      var $submit = $("#submit");
      var $fee = $("#fee");
      var timeout;
      var curMethod = "fast";
      var _authStatus = "${(member.idcard.authStatus)!}";
      // 还款金额
      $amount.bind("input propertychange change", function (event) {
        if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
          if (checkValidAmount($amount.val())) {
            $("#repayment_amount_tips").text("单笔金额不能超过50000元");
            calculateFee();
          } else {
            $("#repayment_amount_tips").text("为确保汇款成功，请正确填写支付金额");
          }
        }
      });

      // 表单验证
      $inputForm.validate({
        rules: {
          amount: {
            required: true,
            positive: true,
            max: 50000.00,
            min: 0,
            decimal: {
              integer: 12,
              fraction: ${setting.priceScale}
            }
          }
        },
        messages: {
          amount: {
            required: '必填',
            positive: '只允许输入正数',
            max: '数额超出',
            min: '金额不能小于0',
            decimal: '金额格式错误'
          }
        },
        beforeSend: function () {
          $submit.prop("disabled", true);
        },
        success: function (message) {
          $submit.prop("disabled", false);
        },
        submitHandler: function (form) {
          var bankName=$("#memberBankId").val();
          if(bankName=='#'){
            $.message("warn","请选择银行卡号或者添加银行卡");
            return false;
          }
          form.submit();
        }
      });

      // 计算支付手续费
      function calculateFee() {
        clearTimeout(timeout);
        timeout = setTimeout(function () {
          if ($amount.val() > 0) {
            $.ajax({
              url: "calculate_fee.jhtml",
              type: "POST",
              data: {method: curMethod, amount: $amount.val()},
              dataType: "json",
              cache: false,
              success: function (data) {
                if (data.message.type == "success") {
                  if (data.fee > 0) {
                    $fee.text(currency(data.fee, true));
                  } else {
                    $fee.text("￥0.00");
                  }
                } else {
                  $.message(data.message);
                }
              }
            });
          } else {
            $fee.text("￥0.00");
          }
        }, 500);
      }

      $("#memberBankId").change(function (obj) {
        var val = $(this).val();
        var bankName=$(this).find("option:selected").attr("bankName");
        $("#bankname").val(bankName);
        $("#account").val(val);
        if(val.indexOf('jhtml')!=-1){
          if(_authStatus=='success'){
            location.href=val;
          }else {
            location.href='${base}/store/member/safe/idcard.jhtml';
          }
        }
      });
    });
</script>
</body>
</html>
