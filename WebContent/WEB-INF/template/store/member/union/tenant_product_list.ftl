<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>商盟商家商品</title>
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
        <h1>联盟商家商品列表<small>加入的商盟的店铺</small>  </h1>
        <ol class="breadcrumb">
          <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
          <li><a href="${base}/store/member/union/all_union.jhtml">全部商盟</a></li>
          <li>查看商品</li>
        </ol>
      </section>
			<!-- Main content -->
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						<div class="nav-tabs-custom">
							<div class="tab-content" style="padding:15px 0 0 0;">
								<form id="listForm" action="tenant_product_list.jhtml" method="get">
                  <input type="hidden" name="tenantId"value="${tenantId}"/>
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
													<input type="text" class="form-control pull-right" id="keyword" name="keywords" value="${keywords}" placeholder="名称">
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
                            <th><span>商品名称</span></th>
                            <th><span>图片</span></th>
                            <th><span>商品价格</span></th>
                            <th><span>分享次数</span></th>
                            <th><span>成交量</span></th>
                            <th><span>已赚金额</span></th>
                            <th><span>操作</span></th>
                          </tr>
                        </thead>
                        <tbody>
                          [#list page.content as product]
                          <tr>
                            <td>
                              <span title="${product.fullName}">${abbreviate(product.fullName, 30, "..")}</span>
                            </td>
                            <td>
                              [#if product.thumbnail?has_content]
                                <img src="${product.thumbnail}" alt="" width="30px" height="30px"/>
                              [/#if]
                            </td>
                            <td>${currency(product.price)}</td>
                            <td>${(product.shareTimes)!0}</td>
                            <td>${(product.valume)!0}</td>
                            <td>${(product.earnAmount)!0}</td>
                            <td>
                              [#if product.isRecommended=="0"||product.isRecommended==""]
                              <input class="btn btn-default btn-sm" type="button" value="推荐商品" onclick="save_product(${product.id})">
                              [#else]
                              <input class="btn btn-info btn-sm" type="button" value="取消推荐" onclick="cancel_product(${product.id})">
                              [/#if]
                              <input class="btn btn-info btn-sm" type="button" value="分享推广" onclick="share_products(${product.id})">
                            </td>
                          </tr>
                          [/#list]
                        </tbody>
                      </table>
                      <!-- 分享推广弹框【 -->
                      <input type="hidden" data-toggle="modal" onclick="share_product_alert()" id="share_alert" productId="">
                      <div class="modal fade" id="share_product" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display:none;">
                        <div class="modal-dialog">
                          <form class="form-horizontal" role="form" action="" method="post">
                            <div class="modal-content" style=" border-radius: 5px;width:399px;">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <h4 class="modal-title">分享推广</h4>
                              </div>
                              <div class="modal-body" style="color:#428bca;">
                                <div style="text-align: center;">
                                  <img src="" style="width:200px;height:200px;" id="weixin_qr">
                                </div>
                                <h3 style="text-align:center;font-weight:bold;">微信扫描二维码分享推广</h3>
                              </div>
                            </div>
                          </form>
                        </div>
                      </div>
                      <!--申请加入商盟弹框】 -->
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
    /*收藏商品*/
    function save_product(id){
      $.ajax({
        url:'${base}/store/member/union/save_product.jhtml',
        data:{
          productId:id
        },
        type:'post',
        dataType:'json',
        success:function(message){
          $.message(message);
          if(message.type=="success"){
            location.reload();
          }
        }
      });
    }
    /*取消收藏商品*/
    function cancel_product(id){
      $.ajax({
        url:'${base}/store/member/union/cancel_product.jhtml',
        data:{
          productId:id
        },
        type:'post',
        dataType:'json',
        success:function(message){
          $.message(message);
          if(message.type=="success"){
            location.reload();
          }
        }
      });
    }
    /*分享推广*/
    function share_products(id){
      $.ajax({
        url:'${base}/store/member/union/share_product.jhtml',
        data:{
          productId:id
        },
        type:'post',
        dataType:'json',
        success:function(message){
          if(message.type=="success"){
            $("#share_alert").attr("productId",id);
            $("#share_alert").click();
          }else{
            window.document.reload();
          }
        }
      });
    }
    /*控制弹出框,并生称二维码*/
    function share_product_alert(){
      $("#share_alert").attr("data-target","#share_product");
      $("#weixin_qr").attr("src","${base}/store/member/union/make_qrcode.jhtml?productId="+$("#share_alert").attr("productId"));
    }
	</script>
</body>
</html>