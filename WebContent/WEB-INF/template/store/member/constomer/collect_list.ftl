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
  <style type="text/css">
    table.list th {
      text-align: center;
    }

    table.list td {
      text-align: center;
      margin-left: 0px;
    }
  </style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
  [#include "/store/member/include/header.ftl" /]
  [#include "/store/member/include/menu.ftl" /]
  <div class="wrapper">
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
      <!-- Main content -->
      <section class="content-header">
        <h1>
          客户管理 
          <small>尊敬商家用户，此模块能帮你查看多少人关注收藏了该店铺</small>
        </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/consumer/collect_list.jhtml">客户管理</a></li>
          <li class="active">关注我的人</li>
        </ol>
      </section>
      <section class="content">
        <div class="row">
          <div class="col-md-12">
            <div class="nav-tabs-custom">
              <ul class="nav nav-tabs pull-right">
                <li class="active">
                  <a href="${base}/store/member/consumer/collect_list.jhtml">关注我的人</a>
                </li>
                <li class="">
                  <a href="${base}/store/member/consumer/nearby.jhtml">附近的人</a>
                </li>
                <li class="">
                  <a href="${base}/store/member/consumer/list.jhtml?status=enable">我的会员</a>
                </li>
                <li class="pull-left header"><i class="fa fa-heart"></i>关注我的人</li>
              </ul>
              <div class="tab-content" style="padding:15px 0 0 0;">
                <form id="listForm" action="collect_list.jhtml" method="get">
                  <div class="row mtb10">                        
                    <div class="col-sm-7">
                      <div class="btn-group">                               
                        <button type="button" class="btn btn-default btn-sm" id="refreshButton">
                          <i class="fa fa-refresh mr5" aria-hidden="true"></i> 刷新
                        </button>
                        <div class="dropdown fl ml5">
                          <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">每页显示<span class="caret"></span>
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
                      <div class="box-tools fr" >
                        <div class="input-group input-group-sm" style="width: 150px;">
                          <input type="text" name="table_search" class="form-control pull-right" placeholder="Search">
                          <div class="input-group-btn">
                            <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                          </div>
                        </div>
                      </div>
                    </div>    
                  </div>
                  <div class="box" style="border-top:0px;">
                    <div class="box-body">
                      <table id="listTable" class="table table-bordered table-striped">
                        <thead>
                          <tr>
                            <th>用户名称</th>
                            <th>用户性别</th>
                            <th>联系电话</th>
                            <th>联系地址</th>
                            <th>最近登录时间</th>
                            <th>
                              <span>${message("admin.common.handle")}</span>
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as member]
                          <tr>
                            <td>
                              ${member.name}
                            </td>
                            <td>
                              [#if member.gender=="male"]
                              男
                              [#else]
                              女
                              [/#if]
                            </td>
                            <td>
                              ${member.username}
                            </td>
                            <td>
                              <span title="${member.address}">${abbreviate(member.address,20,"..")}</span>
                            </td>
                            <td>
                              [#if member.modifyDate??]
                              ${member.modifyDate?string("yyyy-MM-dd HH:mm")}
                              [/#if]
                            </td>
                            <td>
                              [@helperRole url="helper/member/consumer/collect_list.jhtml" type="supervise"]
                              [#if helperRole.retOper!="0"]
                              <a href="edit.jhtml?id=${member.id}&status=none">管理</a>
                              [/#if]
                              [/@helperRole]
                            </td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                      <div class="dataTables_paginate paging_simple_numbers">
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/store/member/include/pagination.ftl"]
                        [/@pagination]
                      </div>
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
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>

  <script type="text/javascript">
    $().ready(function () {
      var $listForm = $("#listForm");
      var $status = $("#status");
      var $statusSelect = $("#statusSelect");
      var $statusOption = $("#statusOption a");
      $statusSelect.mouseover(function () {
        var $this = $(this);
        var offset = $this.offset();
        var $menuWrap = $this.closest("div.menuWrap");
        var $popupMenu = $menuWrap.children("div.popupMenu");
        $popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
        $menuWrap.mouseleave(function () {
          $popupMenu.hide();
        });
      });

      $statusOption.click(function () {
        var $this = $(this);
        $("#memberRank").val($this.attr("val"));
        $listForm.submit();
        return false;
      });

    });
  </script>
</body>
</html>
