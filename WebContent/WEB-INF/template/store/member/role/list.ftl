<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>角色管理</title>
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
					<h1>我的店铺<small>角色与权限设置</small>	</h1>
					<ol class="breadcrumb">
						<li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
            <li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
            <li class="active">角色管理</li>
					</ol>
				</section>
				<!-- Main content -->
				<section class="content">
					<div class="row">
						<div class="col-md-12">
							<div class="nav-tabs-custom">
								<ul class="nav nav-tabs pull-right">
									<li class="active">
										<a href="${base}/store/member/role/list.jhtml">角色管理</a>
									</li>
									<li>
										<a href="${base}/store/member/tenant/employee/list.jhtml">员工管理</a>
									</li>
									<li class="pull-left header"><i class="fa fa-user-secret"></i>角色管理</li>
								</ul>
								<form class="form-horizontal" id="listForm" action="list.jhtml" method="get" role="form">
								  <!--导航功能-->
                  <div class="row mtb10">
                    <div class="col-sm-7">
                      <div class="btn-group">
                        <button type="button" class="btn btn-primary btn-sm" onclick="javascript:location.href='add.jhtml';">
                          <i class="fa fa-plus-square mr5" aria-hidden="true"></i> 添加
                        </button>
                        <button type="button" class="btn btn-default btn-sm" id="deleteButton">
                          <i class="fa fa-close mr5" aria-hidden="true"></i>删除
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
                          <input type="text" class="form-control pull-right" id="searchValue" name="searchValue" value="${page.searchValue}" placeholder="Search">
                          <div class="input-group-btn">
                            <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
  								<!--表格-->
  								<div class="tab-content">
  									<table id="listTable" class="table table-bordered table-striped">
  										<thead>
  											<tr>
  												<th width="4%"><input type="checkbox" id="selectAll"></th>
  												<th width="10%">角色名称</th>
  												<th><span>操作</span></th>
  											</tr>
  										</thead>
  										<tbody>
                        [#list page.content as role ]
  											<tr>
  												<td><input type="checkbox" name="ids" value="${role.id}"></td>		
  												<td>
  													<div class="dropdown right fl ml5">
                              <a id="dropdownMenu_${role_index}" data-toggle="dropdown" name="roleName"  roleId="${role.id}" onclick="get_roles_content(this)">
                                  ${(role.name)}
                              </a>
                              <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu_${role_index}" style="padding:5px;margin-left:80px;margin-top:-20px;width:350px;">
                                
                              </ul>
  												</td>
  												<td>
                            <a href="${base}/store/member/role/edit.jhtml?id=${role.id}">编辑</a>
                            [#if role.isSystem]<span style="color: red">[系统内置角色,请勿删除]</span>[/#if]
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
                </form>
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
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript">
      var mpOpType =
      {
        [#list types as type]
        ${type}:
            '${message("helper.role."+type)}',
        [/#list]
      };
      function get_roles_content(obj){
        if($(obj).attr("roleId")==""){
          return;
        }
        $.ajax({
            url: "${base}/store/member/role/getRules.jhtml",
            type: "post",
            data: {id: $(obj).attr("roleId")},
            dataType: "json",
            success: function (message) {
                if (message.data != null && message.message.type == "success") {
                    var strHtml = "";
                    strHtml += " <div>";
                    for (var i = 0; i < message.data.length; i++) {
                        if (message.data[i].rules.children.length == 0) {
                            strHtml += " <li>";
                            strHtml += "<strong>" + message.data[i].rules.name + "：</strong>  ";
                            $.each(message.data[i].mapAuthority, function (key, values) {
                                if (values == true) {
                                    strHtml += mpOpType[key] + "、 ";
                                }
                            });
                            strHtml += " </li>";
                        }

                    }
                    $(obj).parents("td").find("ul").empty();
                    $(obj).parents("td").find("ul").append(strHtml);
                }
            }
        });
      }
    </script>
	</body>
</html>