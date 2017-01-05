<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
  <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
  [@seo type = "index"]
  <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
  [#if seo.keywords??]
  <meta name="keywords" content="[@seo.keywords?interpret /]"/>
  [/#if]
  [#if seo.description??]
  <meta name="description" content="[@seo.description?interpret /]"/>
  [/#if]
  [/@seo]
  [#include "/store/member/include/bootstrap_css.ftl" /]
  <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
  [#include "/store/member/include/header.ftl" /]
  [#include "/store/member/include/menu.ftl" /]
  <div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
      <!-- Main content -->
      <section class="content-header">
        <h1>供应商<small>管理我的供应商</small></h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/relation/parent.jhtml">客户管理</a></li>
          <li class="active">管理我的供应商</li>
        </ol>
      </section>
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right"> 
                <li class="pull-left header"><i class="fa fa-truck"></i>管理我的供应商</li>
              </ul>
              <div class="tab-content" >
                <form class="form-horizontal p20" role="form" id="inputForm" action="editParent.jhtml" method="post">
                  <input type="hidden" name="id" value="${tenantRelation.id}"/>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">供应商名称</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" value="${tenantRelation.parent.name}" readonly="true"> 
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">联系人</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" name="linkman" value="${tenantRelation.parent.linkman}">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">手机号码</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" value="${tenantRelation.parent.telephone}" readonly="true">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">地址</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" name="address" value="${tenantRelation.parent.address}">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">等级</label>
                    <div class="col-sm-6">
                      <select class="form-control" name="memberRankId">
                        [#list memberRanks as memberRank]
                        <option value="${memberRank.id}"[#if memberRank.id == tenantRelation.parent.member.memberRank.id] selected="selected"[/#if]>${memberRank.name}</option>
                        [/#list]
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">状态</label>
                    <div class="col-sm-6">
                      <select class="form-control" name="status">
                        <option value="none" [#if ("none" == (tenantRelation.status)!)] selected[/#if]>未审核
                        </option>
                        <option value="success" [#if ("success" == (tenantRelation.status)!)] selected[/#if]>
                          已审核
                        </option>
                        <option value="fail" [#if ("fail" == (tenantRelation.status)!)] selected[/#if]>已驳回
                        </option>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">账户余额</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" value="${(tenantRelation.parent.balance)!}元" readonly="true">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">冻结金额</label>
                    <div class="col-sm-8">
                      <input type="text" class="form-control" value="${(tenantRelation.parent.freezeBalance)!}元" readonly="true">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-2 control-label">库存金额</label>
                    <div class="col-sm-8">
                      [#assign stock=0]
                      [#list products as product]
                      [#if product.supplier??&&product.supplier.id==tenantRelation.parent.id]
                      [#assign stock=stock+product.stock*product.cost]
                      [/#if]
                      [/#list]
                      <input type="text" class="form-control" value="${stock}元" readonly="true">
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
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
    [#include "/store/member/include/footer.ftl" /]
  </div>
  [#include "/store/member/include/bootstrap_js.ftl" /]
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
  <script type="text/javascript">
    $().ready(function () {
      var $inputForm = $("#inputForm");
    });
  </script>
</body>
</html>
