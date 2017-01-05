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
            <h1>购物屏商家列表
                <small>加入的商盟的店铺</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/union/all_union.jhtml">全部购物屏</a></li>
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
                                <input type="hidden" id="unionId" name="unionId" value="${unionId}">
                                <div class="row mtb10">
                                    <div class="col-sm-5">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-default btn-sm"
                                                    onclick="javascript:location.reload();"><i
                                                    class="fa fa-refresh mr5"></i> 刷新
                                            </button>
                                            <div class="dropdown fl ml5">
                                                <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                        id="dropdownMenu1" data-toggle="dropdown">
                                                    每页显示<span class="caret"></span>
                                                </button>
                                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                                    id="pageSizeOption">
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
                                                <input type="text" class="form-control pull-right" id="keyword"
                                                       name="keywords" value="${keywords}" placeholder="名称/店主">
                                                <div class="input-group-btn">
                                                    <button type="submit" class="btn btn-default"><i
                                                            class="fa fa-search"></i></button>
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
                                                <td><span
                                                        title="${tenant.name}">${abbreviate(tenant.name, 20, "...")}</span>
                                                </td>
                                                <td>${(tenant.products?size)!}</td>
                                                <td>${(tenant.address)!}</td>
                                                <td>${(tenant.member.displayName)!}&nbsp;(${(tenant.member.mobile)!})
                                                </td>
                                                <td>${(tenant.brokerage)!}</td>
                                                <td>${(tenant.generalize)!}</td>
                                                <td>${(tenant.agency)!}</td>
                                                <td>
                                                    <input class="btn btn-primary" type="button" value="申请投放" onclick="create_payment(this,0.01,${tenant.id})" union-bro="300"/>
                                                </td>
                                            </tr>
                                            [/#list]
                                            </tbody>
                                        </table>
                                        <!--申请投放 -->
                                        <input type="hidden" id="apply_union_input" data-toggle="modal" data-target="#apply_union" onclick="apply_union_alert()">
                                        <div class="modal fade" id="apply_union" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" style="display:none;">
                                            <div class="modal-dialog">
                                                <form class="form-horizontal" role="form" action="" method="post">
                                                    <div class="modal-content" style=" border-radius: 5px;">
                                                        <div class="modal-header">
                                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                                            <h4 class="modal-title">申请加入购物屏</h4>
                                                        </div>
                                                        <div class="modal-body" style="color:#428bca;">
                                                            <img src="" style="width:200px;height:200px;" id="weixin_qr">
                                                            <div style="float:right;width:350px;">
                                                                <h3 style="text-align:center;font-weight:bold;">扫描左边二维码申请加入</h3>
                                                                <h3>加盟费用：<span id="union_price"></span>元/每年</h3>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                        <!--申请投放 -->
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
    /* 申请加入弹框*/
    function apply_union_alert(){
        $("#apply_union").attr("data-target","#apply_union");
    }

    /* 创建支付单*/
    function create_payment(obj,price,tenantId){
        $.ajax({
            url:'${base}/store/member/union/check_apply.jhtml',
            data:{
                tenantId:tenantId
            },
            type:'post',
            dataType:'json',
            success:function(data){
                if(data.type=="success"){
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
                                        queryCash(data.data.sn,obj,tenantId);
                                    },3000);
                                }
                            });
                        }
                    });
                }else {
                    $.message("error","您已加入过当前购物屏");
                }
            }
        });
    }
    var flag="false";
    /*查询是否知支付成功*/
    function queryCash(sn,obj,tenantId){
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
                        update_unionTenant_status(obj,tenantId);
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
    function update_unionTenant_status(obj,tenantId){
        flag='true';
        $.ajax({
            url:'${base}/store/member/union/apply_equipment.jhtml',
            data:{
                tenantId:tenantId,
                unionId:${unionId}
            },
            type:'post',
            dataType:'json',
            success:function(message){
                if(message.type=="success"){
                    $.message("success","操作成功，您已是该购物屏的一员。")
                    location.href="${base}/store/member/union/invest_device_apply.jhtml";
                }
            }
        });
    }
</script>
</body>
</html>