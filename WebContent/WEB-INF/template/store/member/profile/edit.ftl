<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>账户信息</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/common.css"  />
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
  <div class="wrapper">
    [#include "/store/member/include/header.ftl"]
    [#include "/store/member/include/menu.ftl"]
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <h1>
          账户信息
          <small>查看、修改账户基本信息</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li class="active">账户信息</li>
        </ol>
      </section>
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="pull-left header"><i class="fa fa-user"></i>账户信息</li>
              </ul>
              <div class="tab-content" >
                <form class="form-horizontal" id="inputForm" action="update.jhtml" method="post">
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">头像</label>
                    <div class="col-sm-3">
                      <input type="hidden" class="form-control"  name="headImg" id="headImg" value="${member.headImg}">
                      <img src="[#if member.headImg??]${member.headImg}[#else]${base}/resources/store/images/li00.png[/#if]" style="width:160px;height:160px;" id="unload_img">
                    </div>
                    <div class="col-sm-2">
                      <button type="button" class="btn btn-block btn-default" id="browserButton">选择文件</button>
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">账户名称</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" value="${mosaic(member.username,4,'***')}" disabled="true">
                    </div>
                  </div>
                  <div class="form-group">
                    <label  class="col-sm-2 control-label">店铺名称</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control"  d="shortName" name="tenantShortName" value="${(tenant.shortName)!}">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">真实姓名</label>
                    <div class="col-sm-6">
                      [#if member.authStatus == "success"]
                      <input type="text" class="form-control" value="${(member.name)!}" disabled="true">
                      [#elseif member.authStatus == "wait"]
                      <input type="text" class="form-control" value="${(member.name)!}" disabled="true">
                      [#else]
                      <input type="text" class="form-control" name="name" value="${(member.name)!}">
                      [/#if]
                    </div>
                    <div class="col-sm-2">
                      [#if member.authStatus == "success"]
                      <span class="lh35 price msge-cont">【已认证】</span>
                      [#elseif member.authStatus == "wait"]
                      <span class="lh35 price msge-cont">【认证中】</span>
                      [#else]
                      <span class="lh35 price msge-cont"><a href="${base}/store/member/safe/idcard.jhtml?urlid=1">【未认证】</a></span>
                      [/#if]
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">公司名称</label>
                    <div class="col-sm-6">
                      [#if tenant.status=="success"||tenant.status=="confirm"]
                      <input type="text" class="form-control" value="${(tenant.name)!}" disabled="true">
                      [#else]
                      <input type="text" class="form-control" name="tenantName" value="${(tenant.name)!}">
                      [/#if]
                    </div>
                    <div class="col-sm-2">
                      [#if tenant.status=="success"||tenant.status=="confirm"]
                      <span class="lh35 price msge-cont">【已认证】</span>
                      [#else]
                      <span class="lh35 price msge-cont"><a href="${base}/store/member/tenant/add.jhtml?pageActive=1">【未认证】</a></span>
                      [/#if]
                    </div>
                  </div>
                  [#list memberAttributes as memberAttribute]
                  <div class="form-group">
                    [#if memberAttribute.type != "mobile"]
                      [#if memberAttribute.isRequired][/#if]
                      <label class="col-sm-2 control-label">${memberAttribute.name}</label>
                    [/#if]
                    <div class="col-sm-8">
                    [#if memberAttribute.type == "name"]
                      [#if authStatus == "success"]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control"
                      value="${member.name}" maxlength="200" readonly="readonly"/>
                      [#else]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control"
                      value="${member.name}" maxlength="200"/>
                      [/#if]
                    [#elseif memberAttribute.type == "gender"]
                      <span class="fieldSet">
                        [#list genders as gender]
                        <label><input type="radio" name="memberAttribute_${memberAttribute.id}"value="${gender}"[#if gender == member.gender]
                          checked="checked"[/#if]/>${message("Member.Gender." + gender)}
                        </label>
                        [/#list]
                      </span>
                    [#elseif memberAttribute.type == "birth"]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control" value="${member.birth}" onfocus="WdatePicker();"/>
                    [#elseif memberAttribute.type == "area"]
                      <input type="hidden" id="areaId" name="memberAttribute_${memberAttribute.id}" value="${(member.area.id)!}" treePath="${(member.area.treePath)!}"/>
                     [#elseif memberAttribute.type == "address"]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control" value="${member.address}" maxlength="200"/>
                    [#elseif memberAttribute.type == "zipCode"]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control" value="${member.zipCode}" maxlength="200"/>
                    [#elseif memberAttribute.type == "phone"]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control" value="${member.phone}" maxlength="200"/>
                    [#elseif memberAttribute.type == "mobile"]
                    [#elseif memberAttribute.type == "text"]
                      <input type="text" name="memberAttribute_${memberAttribute.id}" class="form-control" value="${member.getAttributeValue(memberAttribute)}" maxlength="200"/>
                    [#elseif memberAttribute.type == "select"]
                      <select name="memberAttribute_${memberAttribute.id}" class="form-control">
                        <option value="">${message("shop.common.choose")}</option>
                        [#list memberAttribute.options as option]
                        <option value="${option}"[#if option == member.getAttributeValue(memberAttribute)] selected="selected"[/#if]>${option}</option>
                        [/#list]
                      </select>
                    [#elseif memberAttribute.type == "checkbox"]
                      <span class="fieldSet">
                        [#list memberAttribute.options as option]
                        <label>
                          <input type="checkbox" name="memberAttribute_${memberAttribute.id}" value="${option}"
                          [#if (member.getAttributeValue(memberAttribute)?seq_contains(option))!] checked="checked"[/#if]/>${option}
                        </label>
                        [/#list]
                      </span>
                    [/#if]
                    </div>
                  </div>
                  [/#list]
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-2">
                      <button type="submit" class="btn btn-block btn-primary">提交</button>
                    </div>
                    <div class="col-sm-offset-0 col-sm-2">
                      <button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
                    </div>
                  </div>
                </form>
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
  <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/jquery.Jcrop.min.js"></script>
  <script type="text/javascript">
    $().ready(function () {

      var $inputForm = $("#inputForm");
      var $areaId = $("#areaId");
      var $browserButton = $("#browserButton");
      var $shortName = $("#shortName");
      var $submit = $(":submit");


      //图片上传
      var settings = {
        width: 360,
        height: 360,
        callback: addImage
      }
      $browserButton.browser(settings);
      function addImage(url, local) {
        $("#unload_img").attr("src", url);
        $("#headImg").val(url);
      }

      // 地区选择
      $areaId.lSelect({
        url: "${base}/common/area.jhtml"
      });

      //清除火狐浏览器刷新多出拉下框
      // $("select[name='memberAttribute_4_select']").each(function () {
      //   if ($(this).val() == "") {
      //     $(this).nextAll("select").remove();
      //     return false;
      //   }
      // });
      

      //店铺名称唯一验证
      $shortName.blur(function () {
        var shortName = $(this).val();
        if (shortName.trim() != "") {
          $.ajax({
            url: "${base}/helper/member/profile/checkShortName.jhtml",
            type: "get",
            data: {"shortName": shortName},
            dataType: "json",
            success: function (data) {
              if (data) {
                $("#checkMsg").text("(店铺名称已存在！)");
                $submit.prop("disabled", true);
              } else {
                $("#checkMsg").text("");
                $submit.prop("disabled", false);
              }
            }
          });
        } else {
          $("#checkMsg").text("");
          $submit.prop("disabled", false);
        }
      });

      // 表单验证
      $inputForm.validate({
        rules: {
          email: {
            required: false
          }
          [#list memberAttributes as memberAttribute]
          [#if memberAttribute.isRequired]
          , memberAttribute_${memberAttribute.id}: {
            required: true
          }
          [/#if]
          [#if memberAttribute.type == "phone"]
          [#if !memberAttribute.isRequired]
          , memberAttribute_${memberAttribute.id}: {
            pattern: /^((\d{3,4}-\d{7,8})|(1[23456798]\d{9}))$/
          }
          [/#if]
          [/#if]
          [#if memberAttribute.type == "zipCode"]
          [#if !memberAttribute.isRequired]
          , memberAttribute_${memberAttribute.id}: {
            pattern: /^\d{3,6}$/
          }
          [/#if]
          [/#if]
          [/#list]
        },
        messages: {
          email: ''
          [#list memberAttributes as memberAttribute]
          [#if memberAttribute.isRequired]
          , memberAttribute_${memberAttribute.id}: {
            required: '必填'
          }
          [/#if]
          [#if memberAttribute.type == "phone"]
          [#if !memberAttribute.isRequired]
          , memberAttribute_${memberAttribute.id}: {
            pattern: "请填写正确的号码"
          }
          [/#if]
          [/#if]
          [#if memberAttribute.type == "zipCode"]
          [#if !memberAttribute.isRequired]
          , memberAttribute_${memberAttribute.id}: {
            pattern: "请填写正确的邮编号码"
          }
          [/#if]
          [/#if]
          [/#list]
        }
      });

});
</script>
</body>
</html>
