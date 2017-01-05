<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
[@seo type = "index"]
    <title>[#if seo.title??][@seo.title?interpret /][#else]${message("shop.index.title")}[/#if][#if systemShowPowered][/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    [#include "/store/member/include/bootstrap_css.ftl"]
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
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
      <h1>银行汇款<small>支持国内各大银行借记卡往来结算支付申请，快捷方便！</small>  </h1>
      <ol class="breadcrumb">
        <li>
          <a href="#"><i class="fa fa-dashboard"></i>我的店铺</a>
        </li>
        <li class="active">银行汇款</li>
      </ol>
    </section>
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="nav-tabs-custom">
            <div class="tab-content">
              <form class="form-horizontal" role="form" id="inputForm" action="${base}/store/member/cash/saveMemberBank.jhtml" method="post" 
                    enctype="multipart/form-data">
                <input type="hidden" name="type" value="recharge"/>
                <input type="hidden" name="paymentPluginId" value="cmbcPlugin"/>
                <div class="form-group">
                  <label class="col-sm-2 control-label">选择银行</label>
                  <div class="col-sm-4">
                    <select id="parentId" name="bankInfoId" class="form-control">
                        [#list bankInfos as bankInfo]
                            <option value="${bankInfo.id}" >${bankInfo.depositBank}</option>
                        [/#list]
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">卡号</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" id="account" name="account">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">姓名</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" value="${member.name}" disabled="true">
                    <input name="payer" maxlength="255" value="${member.name}" type="hidden"/>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">身份证号</label>
                  <div class="col-sm-8">
                    <input type="text" class="form-control" value="${idcard.no}" disabled="true">
                    <input name="no" maxlength="255" value="${idcard.no}" type="hidden"/>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">手机号</label>
                  <div class="col-sm-6">
                    <input type="hidden" name="mobile" id="mobile" value="${member.mobile}"/>
                    <input type="text" class="form-control" value="${mosaic(member.mobile, 3, '~~~')}" disabled="true">
                  </div>
                  <div class="col-sm-2">
                    <input type="button" id="sendCode" class="btn btn-block btn-default" value="获取验证码"/>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">校验码</label>
                  <div class="col-sm-8">
                    <input type="text" name="securityCode" id="securityCode" class="form-control" maxlength="20"/>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-sm-offset-2 col-sm-2">
                    <button type="submit" class="btn btn-block btn-primary">提交</button>
                  </div>
                  <div class="col-sm-offset-0 col-sm-2">
                    <button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
                  </div>
                </div>
              </form>
              <div class="row">
                  <div class="col-sm-offset-2 col-sm-10">
                    <h4><strong>相关说明</strong></h4>
                    <ul class="list-number">
                        <li>由于各银行入账时间和方式不同，请在预计到账时间前后先查询余额恢复和到账记录情况。</li>
                        <li>请仔细阅读汇款到账时间说明，如果由于预计到账时间超过汇款日期限、汇款信息填写错误或者超过银行的汇款限额等造成延误产生的损失，概不负责。</li>
                        <li>如出现汇款异常情况，失败汇款订单，会在最迟3-5个工作日内核实并将资金退回账户内。</li>
                        <li>银行汇款一旦成功提交，即表示已提交至银行处理，不可撤回和取消，敬请谅解。</li>
                        <li>平台发送的提交成功短信仅作提示使用，最终到账结果请以银行入账为准。</li>
                        <li>每天23点至1点前后银行系统结算时间，可能会影响到账时间，请注意申请汇款时间。</li>
                    </ul>
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
<script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
<script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            var $inputForm = $("#inputForm");
            var $submit = $("#submit");
            var $sendCode = $("#sendCode");
            var $mobile = $("#mobile");

            // 表单验证
            $inputForm.validate({
                rules: {
                    account: {
                        required: true,
                        creditcard: true
                    },
                    securityCode: {required: true}
                },
                messages: {
                    account: {
                        required: '必填',
                        creditcard: '请填写正确的银行卡号码'
                    },
                    securityCode: {
                        required: '必填'
                    }
                },
                beforeSend: function () {
                    $submit.prop("disabled", true);
                },
                success: function (message) {
                    $submit.prop("disabled", false);
                }
            });

            $sendCode.click(function () {
                if ($mobile.val() != "" && $mobile.val().length == 11) {
                    settime(this);
                }

                $.ajax({
                    url: "${base}/store/member/cash/getCheckCode.jhtml",
                    type: "post",
                    data: {mobile: $mobile.val()},
                    dataType: "json",
                    success: function (message) {
                        $.message(message);
                    }
                });
            });

        });

        var countdown = 60;
        function settime(obj) {
            if (countdown == 0) {
                obj.removeAttribute("disabled");
                obj.value = "获取验证码";
                countdown = 60;
                return;
            } else {
                obj.setAttribute("disabled", true);
                obj.value = "" + countdown + "s重新发送";
                countdown--;
            }
            setTimeout(function () {
                settime(obj)
            }, 1000);
        }

        function selectBank(bank) {
            var bankInfo = getBankInfo(bank);
            $("#bank").val(bank);
            $("#bankname").val(bankInfo.bankname);
        }
    </script>
</body>
</html>