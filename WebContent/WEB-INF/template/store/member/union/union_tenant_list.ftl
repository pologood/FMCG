<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>商盟商家</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
	<link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
  <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/common.css">
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
        <h1>联盟商家列表<small>加入的商盟的店铺</small>  </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/union/all_union.jhtml">全部商盟</a></li>
          <li>查看商家</li>
        </ol>
      </section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form id="listForm" action="union_tenant_list.jhtml" method="get">
                  <input type="hidden" name="unionId" value="${unionId}">
									<div class="row mtb10">
										<div class="col-sm-5">
											<div class="btn-group">
												<button type="button" class="btn btn-default btn-sm" onclick="javascript:location.reload();"><i class="fa fa-refresh mr5"></i> 刷新
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
										<div class="col-sm-7">
											<div class="box-tools fr">
												<div class="input-group input-group-sm" style="width: 150px;">
													<input type="text" class="form-control pull-right" id="keyword" name="keywords" value="${keywords}" placeholder="名称/店主">
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
                            <th><span>所属区域</span></th>
                            <th><span>店铺分类</span></th>
                            <th><span>店铺名称</span></th>
                            <th><span>商品款数</span></th>
                            <th><span>地址</span></th>
                            <th><span>店主(电话)</span></th>
                            <th><span>平台佣金</span></th>
                            <th><span>推广分润</span></th>
                            <th><span>联盟佣金</span></th>
                            <th><span>操作</span></th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as tenant]
                          <tr>
                            <td>${(tenant.area.fullName)!}</td>
                            <td>${(tenant.tenantCategory.name)!}</td>
                            <td><span title="${tenant.name}">${abbreviate(tenant.name, 20, "...")}</span></td>
                            <td>${(tenant.products?size)!}</td>
                            <td>${(tenant.address)!}</td>
                            <td>${(tenant.member.displayName)!}&nbsp;(${(tenant.member.mobile)!})</td>
                            <td>${(tenant.brokerage)!}</td>
                            <td>${(tenant.generalize)!}</td>
                            <td>${(tenant.agency)!}</td>
                            <td>
                              [#if tenant.products?size gt 0]
                                <input class="btn btn-info btn-sm" type="button" value="查看商品" onclick="location.href='tenant_product_list.jhtml?tenantId=${tenant.id}'">
                              [/#if]
                                <input class="btn btn-info btn-sm" type="button" value="查看推广" data-toggle="modal" onclick="look_extend(this,${tenant.id})">
                                <input class="btn btn-info btn-sm" type="hidden" value="查看推广" data-toggle="modal" id="look_extend_button" onclick="look_extend_alert()">
                            </td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                      <!-- 推广弹框【 -->
                      <div class="modal fade" id="my_extend" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display:none;">
                        <div class="modal-dialog">
                          <form class="form-horizontal" role="form" action="" method="post">
                            <div class="modal-content" style=" border-radius: 5px;">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title">推广信息</h4>
                              </div>
                              <div class="modal-body" style="font-size:20px;">
                                <div style="text-align:center;">
                                  <div>我推广他</span></div>
                                  <div style="line-height:50px;">订单：<span id="i_to_he_valume"></span>&nbsp;笔&nbsp;&nbsp;&nbsp;销售金额：<span id="i_to_he_total"></span>&nbsp;元&nbsp;&nbsp;&nbsp;已赚取：<span id="i_to_he_amount"></span>&nbsp;元</div>
                                  <div>他推广我</div>
                                  <div style="line-height:50px;">订单：<span id="he_to_i_valume"></span>&nbsp;笔&nbsp;&nbsp;&nbsp;销售金额：<span id="he_to_i_total"></span>&nbsp;元&nbsp;&nbsp;&nbsp;已赚取：<span id="he_to_i_amount"></span>&nbsp;元</div>
                                </div>
                              </div>
                            </div>
                          </form>
                        </div>
                      </div>
                      <!--推广弹框】 -->
                      <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
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
      <!-- /.content -->
    </div>
    [#include "/store/member/include/footer.ftl"]
  </div>
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
  <script type="text/javascript">
  function look_extend(obj,id){
    /*查看推广信息*/
    $.ajax({
      url:'${base}/store/member/union/look_extend.jhtml',
      data:{
        tenantId:id
      },
      type:'post',
      dataType:'json',
      success:function(data){
        if(data.type=="false"){
          $.message("warn",data.content);
        }else{
          $("#look_extend_button").click();
          $("#i_to_he_valume").text(data.i_to_he_valume);
          $("#i_to_he_amount").text(data.i_to_he_amount);
          $("#i_to_he_total").text(data.i_to_he_total);
          $("#he_to_i_valume").text(data.he_to_i_valume);
          $("#he_to_i_amount").text(data.he_to_i_amount);
          $("#he_to_i_total").text(data.he_to_i_total);
        }
        
      }
    });
  }
  function look_extend_alert(){
    $("#look_extend_button").attr("data-target","#my_extend");
  }
	</script>
</body>
</html>