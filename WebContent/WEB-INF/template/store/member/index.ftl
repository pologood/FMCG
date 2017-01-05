<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${setting.siteName}后台首页</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, minimum-scale=1.0,maximum-scale=1, user-scalable=no" name="viewport">
    [#include "/store/member/include/bootstrap_css.ftl"]
    <script type="text/javascript" src='http://webapi.amap.com/maps?v=1.3&key=您申请的key值&plugin=AMap.ToolBar'></script>
    <script type="text/javascript" src="http://webapi.amap.com/demos/js/liteToolbar.js"></script>
    <script type="text/javascript">
        var map = new AMap.Map('mapDiv', {
            resizeEnable: true,
            center:[117.282699, 31.866942],
            zoom:18
        });
        map.on('indoor_create',function(){
            map.indoorMap.showIndoorMap('B000A856LJ',5);
        })
    </script>
    <script type="text/javascript" src="http://webapi.amap.com/demos/js/liteToolbar.js"></script>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">
    <style type="text/css">
    #info{
        width:603px;
        padding-top:3px;
        overflow:hidden;
    }
    /*.btn{
        width:112px;
    }*/
    #container{
        height: 520px;
    }
    </style>
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
            <h1>
                <small>当前积分：</small>
                ${(member.tenant.point)!}
                [@helperRole url='store/member/activity/list.jhtml']
                    <small><a href="javascript:void(0)" onclick="roleRoot('${base}/store/member/activity/list.jhtml?type=growth','${helperRole.retUrl}');">
                        完成任务拿积分
                          </a>
                    </small>
                [/@helperRole]
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> ${setting.siteName}体验店 </a></li>
                <li class="active">首页</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <!-- box-ico -->
            <div class="box box-solid">
                <div class="box-body">
                    <div class="row">
                    <div class="col-sm-3">
                        <div class="i-orange pd40">
                        [@helperRole url='store/member/trade/list.jhtml']
                            <a href="javascript:void(0);"onclick="roleRoot('${base}/store/member/trade/list.jhtml?type=unshipped','${helperRole.retUrl}');">

                            <div class="col-sm-4"><img src="${base}/resources/store/images/i-ico1.png" alt=""></div>
                            <div class="col-sm-8 font-white">
                                          <p>${unshippedCount!0}</p>
                                     <p>待发货</p>

                            </div>
                            </a>
                        [/@helperRole]
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <a href="javascript:void(0);" onclick="javascript:location.href='${base}/store/member/trade/return/list.jhtml?returnStatus=unconfirmed';">
                        <div class="i-blue pd40">
                            <div class="col-sm-4"><img src="${base}/resources/store/images/i-ico2.png" alt=""></div>
                            <div class="col-sm-8 font-white">
                                <p>${spreturnApplyCount!0}</p>
                                <p>维权订单</p>
                            </div>
                        </div>
                        </a>
                    </div>
                    <div class="col-sm-3">
                        <div class="i-red pd40" onclick="yestoday_trade()">
                            <div class="col-sm-4"><img src="${base}/resources/store/images/i-ico3.png" alt=""></div>
                            <div class="col-sm-8 font-white">
                                <p >${yestoday_order_num!0}</p>
                                <p>昨日订单</p>
                            </div>
                        </div>
                        </a>
                    </div>
                    <div class="col-sm-3" onclick="yestoday_trade()">
                        <div class="i-green pd40">
                            <div class="col-sm-4"><img src="${base}/resources/store/images/i-ico4.png" alt=""></div>
                            <div class="col-sm-8 font-white">
                                <p >${yestodayAmount!0}</p>
                                <p>昨日交易金额</p>
                            </div>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
            <!-- box-ico end-->

            <div class="row">
                <div class="col-sm-8 col-md-9">
                        <div class="box box-solid ">
                                <div class="datex">
                                    <ul>
                                        <li class=" font-small">今日实时更新（更新时间：2015-09-23 16：00）</li>
                                        <li class=""><a href="" data-toggle="tab" aria-expanded="false"> 昨日</a></li>
                                        <li class="dropdown mega-dropdown ">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">最近七天 <span class="caret"></span></a>
                                            <div class="dropdown-menu mega-dropdown-menu">
                                                <div class="container-fluid" >
                                                    <div class="row">
                                                        <div class="col-sm-3">
                                                            <div class="f26">UV</div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">1280</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <div class="f26">PV</div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">64</span>
                                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <div class="f20">平均停留时间</div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">23S</span>
                                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <div class="f20">新增会员</div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">今日</span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">9-22</span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">9-21</span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">9-20</span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">9-19</span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">9-18</span>
                                                            </div>
                                                            <div class="dates">
                                                                <span class="f20 fl">26</span>
                                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                                                <span class="datep">9-17</span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                 </div>
                                <div class="p20s">
                                    <div class="row border-right">
                                        <div class="col-sm-3">
                                            <div class="f26">UV</div>
                                            <div class="dates">
                                                <span class="f20 fl">1280</span>
                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                            </div>
                                        </div>
                                        <div class="col-sm-3">
                                            <div class="f26">PV</div>
                                            <div class="dates">
                                                <span class="f20 fl">64</span>
                                                <span class="f16 fl">+1.6% <i class="fa fa-long-arrow-up"></i></span>
                                            </div>
                                        </div>
                                        <div class="col-sm-3">
                                            <div class="f20">平均停留时间</div>
                                            <div class="dates">
                                                <span class="f20 fl">23S</span>
                                                <span class="f16 fl">+1.2% <i class="fa fa-long-arrow-up"></i></span>
                                            </div>
                                        </div>
                                        <div class="col-sm-3">
                                            <div class="f20">新增会员</div>
                                            <div class="dates">
                                                <span class="f20 fl">26</span>
                                                <span class="f16 fl">+1.8% <i class="fa fa-long-arrow-up"></i></span>
                                            </div>
                                        </div>
                                    </div>
                             </div>
                        </div>

                    <div class="row">
                        <div class="[#if ourStoreList?size lte 1][#else]col-sm-4[/#if]" [#if ourStoreList?size lte 1]style="display:none;"[/#if]>
                            <!--地图-->
                            <div class="box box-solid" id="mapDiv">
                                <div class="box-header with-border">
                                    <h3 class="box-title">商家圈</h3>
                                </div>
                                <div id="container"></div>
                            </div>
                        </div>
                        <div class="[#if ourStoreList?size lte 1]col-sm-12[#else]col-sm-8[/#if]">
                            <!--七天交易数据-->
                            <div class="box  box-solid">
                                <div class="nav-tabs-custom">
                                    <ul class="nav nav-tabs " id="tabs">
                                        <li class="active"><a href="#tab_1" data-toggle="tab">七天交易数据</a></li>
                                        <li><a href="#tab_2" data-toggle="tab">单品销量排行版</a> </li>
                                    </ul>
                                    <div class="tab-content" [#if ourStoreList?size lte 1]style="min-height:500px;"[#else][/#if]>
                                        <div class="tab-pane active" id="tab_1">
                                            <div id="chat" style="min-width:280px;height:300px" >

                                            </div>
                                        [#if new_weeks??&&new_weeks?has_content]
                                            [#list new_weeks as week]
                                                <input type="hidden" class="new_week" value ="${week}" >
                                            [/#list]
                                        [/#if]
                                        [#if sevenTradeList??&&sevenTradeList?has_content]
                                            [#list sevenTradeList as map]
                                                <input type="hidden" class="incomeTotal" value ="${map.incomeTotal}" >
                                                <input type="hidden" class="tradeSum" value ="${(map.tradeSum)!}" >
                                                <input type="hidden" class="priceTotal" value ="${(map.priceTotal)!}" >
                                            [/#list]
                                        [/#if]
                                        </div>
                                        <!-- /.tab-pane -->
                                        <div class="tab-pane" id="tab_2">
                                            <div class="blist">
                                                <span class="fl s-gray">更新时间：${currentTime}</span>
                                                <span class="fr"><a href="">查看完整榜单</a></span>
                                            </div>
                                            <table class="table th-bg">
                                                <thead>
                                                <tr>
                                                    <th>排名</th>
                                                    <th>商品</th>
                                                    <th>交易金额</th>
                                                    <th>UV</th>
                                                    <th>购买转化率</th>
                                                    <th>升降位次</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                [#if mapsList??&&mapsList?has_content]
                                                    [#list mapsList as maps]
                                                    <tr>
                                                        <td><div  [#if maps_index+1 ==1]class="number-block b-red"[#elseif maps_index+1 ==2]class="number-block b-yellow"[#elseif maps_index+1 ==3]class="number-block b-blue"[#else]class="number-block b-gray"[/#if]>${(maps_index+1)!}</div></td>
                                                        <td><a href="">${(maps.name)!}</a></td>
                                                        <td>¥${(maps.price)!}</td>
                                                        <td>256</td>
                                                        <td>4.5%</td>
                                                        <td>4 <i class="fa fa-long-arrow-up red-ico" ></i></td>
                                                    </tr>
                                                    [/#list]
                                                [/#if]
                                                </tbody>
                                            </table>
                                        </div>
                                        <!-- /.tab-pane -->

                                    </div>
                                    <!-- /.tab-content -->
                                </div>
                            </div>
                            <!--我们的门店-->
                            <div class="box box-solid" [#if ourStoreList?size lte 1]style="display:none;"[/#if]>
                                <div class="nav-tabs-custom">
                                    <ul class="nav nav-tabs" id="blue-tab">
                                        <li class="active"><a href="#tab_11" data-toggle="tab">我们的门店</a></li>
                                        <li><a href="#tab_21" data-toggle="tab">联盟商店</a></li>
                                        <li><a href="#tab_31" data-toggle="tab">附近门店</a></li>

                                        <li class="pull-right"><a href="#" class="text-muted">推荐好友来开店，拿奖励！ 邀请码：<span class="red">${member.id+100000}</span> </a></li>
                                    </ul>
                                    <div class="tab-content">
                                        <div class="tab-pane active" id="tab_11">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div id="Carousel" class="carousel slide">
                                                        <!-- Carousel items -->
                                                        <div class="carousel-inner">
                                                            <div class="item active">
                                                                <div class="row">
                                                                [#if ourStoreList??&&ourStoreList?has_content]
                                                                    [#list ourStoreList as store]
                                                                    [#if store_index lt 6]
                                                                     <div class="col-md-2"><a href="#" class="thumbnail"><img src="${store.tenant.thumbnail}" alt="Image" style="max-width:100%;"></a></div>
                                                                    [/#if]
                                                                    [/#list]
                                                                [/#if]
                                                                </div><!--.row-->
                                                            </div><!--.item-->
                                                        </div><!--.carousel-inner-->
                                                    </div><!--.Carousel-->
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.tab-pane -->
                                        <div class="tab-pane" id="tab_21">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div id="mycarousel" class="carousel slide"
                                                        <!-- Carousel items -->
                                                        <div class="carousel-inner">
                                                            <div class="item active">
                                                                <div class="row">
                                                                    [#list tenants as tenant]
                                                                    <div class="col-md-2"><a href="#" class="thumbnail"><img src="${tenant.thumbnail}" alt="Image" style="max-width:100%;"></a></div>
                                                                    [/#list]
                                                                </div><!--.row-->
                                                            </div><!--.item-->
                                                        </div><!--.carousel-inner-->
                                                    </div><!--.Carousel-->
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.tab-pane -->
                                        <div class="tab-pane" id="tab_31">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div id="carousellast" class="carousel slide">
                                                        <!-- <ol class="carousel-indicators">
                                                            <li data-target="#carousellast" data-slide-to="0" class="active"></li>
                                                            <li data-target="#carousellast" data-slide-to="1"></li>
                                                            <li data-target="#carousellast" data-slide-to="2"></li>
                                                        </ol> -->
                                                        <!-- Carousel items -->
                                                        <div class="carousel-inner">
                                                            <div class="item active">
                                                                <div class="row">
                                                                    [#list nearStoreList as tenant]
                                                                    <div class="col-md-2"><a href="#" class="thumbnail"><img src="${tenant.thumbnail}" alt="Image" style="max-width:100%;"></a></div>
                                                                    [/#list]
                                                                </div><!--.row-->
                                                            </div><!--.item-->
                                                        </div><!--.carousel-inner-->
                                                        <!-- <a data-slide="prev" href="#carousellast" class="left carousel-control"><i class="fa fa-caret-left"></i>‹</a>
                                                        <a data-slide="next" href="#carousellast" class="right carousel-control"><i class="fa fa-caret-right"></i>›</a> -->
                                                    </div><!--.Carousel-->

                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.tab-pane -->
                                    </div>
                                    <!-- /.tab-content -->
                                </div>
                            </div>

                        </div>
                    </div>

                </div>
                <div class="col-sm-4 col-md-3 ">
                    <div class="box box-solid">
                        <div class="box-body chart-responsive">
                            <div id="sales-chart" style="height: 260px; position: relative;"></div>
                             <input id="balance" type="hidden"  value="[#if owner??]${owner.balance?string("0.00")}[#else]0.00[/#if]">
                             <input id="freezeBalance" type="hidden" value="[#if owner??]${owner.freezeBalance?string("0.00")}[#else]0.00[/#if]">
                        </div>
                    </div>
                    <div class="box box-solid">
                        <div id="contain" style="min-width:290px;height:290px"></div>

                            <ul class="inbox">
                            [@helperRole url='store/member/deposit/statistics.jhtml']
                                <a href="javascript:void (0)" onclick="roleRoot('${base}/store/member/deposit/statistics.jhtml?type=0','${helperRole.retUrl}');">
                                  <li>收入：¥${income}</li>
                                </a>
                            [/@helperRole]
                            [@helperRole url='store/member/deposit/statistics.jhtml']
                                <a href="javascript:void (0)"
                                   onclick="roleRoot('${base}/store/member/deposit/statistics.jhtml?type=1','${helperRole.retUrl}');">
                                <li>支出：¥ ${outcome}<input id="outcome" type="hidden" value="[#if income!=0]${(outcome/income*100)?string("00.0")}[#else]00.0[/#if]"></li>
                                </a>
                            [/@helperRole]
                            [@helperRole url='store/member/deposit/statistics.jhtml']
                                <a  href="javascript:void (0)"
                                         onclick="roleRoot('${base}/store/member/deposit/statistics.jhtml','${helperRole.retUrl}');">
                                <li>盈余：¥ ${inorout}<input id="inorout" type="hidden" value="[#if income!=0]${(inorout/income*100)?string("00.0")}[#else]00.0[/#if]"></li>
                                </a>
                            [/@helperRole]
                            </ul>

                        <div class="link-box">
                            <span class="fl">
                            [@helperRole url='store/member/deposit/statistics.jhtml']
                                <a  href="javascript:void (0)"
                                   onclick="roleRoot('${base}/store/member/deposit/statistics.jhtml','${helperRole.retUrl}');">本月账单统计</a>
                            [/@helperRole]
                            </span>
                            <span class="fr">
                                  [@helperRole url='store/member/deposit/statistics.jhtml']
                                      <a  href="javascript:void (0)"
                                         onclick="roleRoot('${base}/store/member/deposit/thismonthlist.jhtml','${helperRole.retUrl}');">本月账单明细</a>
                                  [/@helperRole]
                            </span>

                        </div>
                        <div class="kong"></div>
                    </div>

                </div>
            </div>


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
[#include "/store/member/include/footer.ftl"]
    <!-- Control Sidebar -->

</div>
<!-- ./wrapper -->
[#include "/store/member/include/bootstrap_js.ftl"]
<!-- Morris.js charts -->
<script type="text/javascript" src="${base}/resources/store/2.0/plugins/morris/raphael-min.js"></script>
<script type="text/javascript" src="${base}/resources/store/2.0/plugins/morris/morris.js"></script>
<script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/store/2.0/plugins/chartjs/highcharts.js"></script>
<script type="text/javascript" src="${base}/resources/store/2.0/plugins/chartjs/exporting.js"></script>
<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&key=4ZWBZ-NHJCX-EEC4I-7VBM2-XYQPE-LCB4T"></script>

<script>
$(function(){

    var center = new qq.maps.LatLng([#if area.location??]${area.location.lat},${area.location.lng}[#else]31.86000000000,117.27000000000[/#if]);
    var map = new qq.maps.Map(document.getElementById('container'),{
        center: center,
        zoom: 13
    });
    //提示框
    var info= new qq.maps.InfoWindow({
        map: map
    });
    
    //创建我们的门店marker
    [#if ourStoreList??]
        [#list ourStoreList as deliveryCenter]
        [#if deliveryCenter.location??]
        [#if deliveryCenter.location.lat!=""&&deliveryCenter.location.lng!=""]
        var marker1_${deliveryCenter_index} = new qq.maps.Marker({
            position: new qq.maps.LatLng(${deliveryCenter.location.lat},${deliveryCenter.location.lng}),
            map: map
        });
        qq.maps.event.addListener(marker1_${deliveryCenter_index}, 'click', function() {
            info.open(); 
            info.setContent('<div style=""><img style="width:80px; height:80px;" src="${deliveryCenter.tenant.logo}"/><div style="display:niline;">${deliveryCenter.tenant.name}</div></div>');
            info.setPosition(new qq.maps.LatLng(${deliveryCenter.location.lat},${deliveryCenter.location.lng})); 
        });
        
        [/#if]
        [/#if]
        [/#list]
    [/#if]
    //创建联盟门店marker
    [#if uninStoreList??]
        [#list uninStoreList as tenantRelation]
        [#if tenantRelation.tenant.defaultDeliveryCenter.location??]
        [#if tenantRelation.tenant.defaultDeliveryCenter.location.lat!=""&&tenantRelation.tenant.defaultDeliveryCenter.location.lng!=""]
        var marker2_${tenantRelation_index} = new qq.maps.Marker({
            position: new qq.maps.LatLng(${tenantRelation.tenant.defaultDeliveryCenter.location.lat},${tenantRelation.tenant.defaultDeliveryCenter.location.lng}),
            map: map
        });
        qq.maps.event.addListener(marker2_${tenantRelation_index}, 'click', function() {
            info.open(); 
            info.setContent('<div style=""><img style="width:80px; height:80px;" src="${tenantRelation.tenant.logo}"/><div style="display:niline;">${tenantRelation.tenant.name}</div></div>');
            info.setPosition(new qq.maps.LatLng(${tenantRelation.tenant.location.defaultDeliveryCenter.lat},${tenantRelation.tenant.defaultDeliveryCenter.location.lng})); 
        });
        [/#if]
        [/#if]
        [/#list]
    [/#if]
    //创建附近的门店marker
    [#if nearStoreList??]
        [#list nearStoreList as tenant]
        [#if tenant.defaultDeliveryCenter.location??]
        [#if tenant.defaultDeliveryCenter.location.lat!=""&&tenant.defaultDeliveryCenter.location.lng!=""]
        var marker3_${tenant_index} = new qq.maps.Marker({
            position: new qq.maps.LatLng(${tenant.defaultDeliveryCenter.location.lat},${tenant.defaultDeliveryCenter.location.lng}),
            map: map
        });
        qq.maps.event.addListener(marker3_${tenant_index}, 'click', function() {
            info.open(); 
            info.setContent('<div style=""><img style="width:80px; height:80px;" src="${tenant.logo}"/><div style="display:niline;">${tenant.name}</div></div>');
            info.setPosition(new qq.maps.LatLng(${tenant.defaultDeliveryCenter.location.lat},${tenant.defaultDeliveryCenter.location.lng})); 
        });
        [/#if]
        [/#if]
        [/#list]
    [/#if]
});
</script>
<script>
    $(function () {
        "use strict";
        //DONUT CHART
        var donut = new Morris.Donut({
            element: 'sales-chart',
            resize: true,
            colors: ["#6699ff", "#ffcc66"],
            data: [
                {label: " 可用金额 ", value: $("#balance").val()},
                {label: "冻结金额 ", value: $("#freezeBalance").val()},
            ],
            hideHover: 'auto'
        });
    });
</script>
<script>
    $(function () {
        var arrIncome=[],arrTrade=[],arrPrice=[],arrWeek=[],$this=$(this);
        $(".new_week").each(function(){
            arrWeek.push($(this).val());
        });
        $(".incomeTotal").each(function(){
            arrIncome.push($(this).val());
        });
        $(".tradeSum").each(function(){
            arrTrade.push($(this).val());
        });
        $(".priceTotal").each(function(){
            arrPrice.push($(this).val());
        });
        $("#yesterdayAmountTotal").text(arrPrice[6]);//昨日交易金额
        $("#tradeyesterdayCount").text(arrTrade[6]);//昨日订单
        $('#chat').highcharts({
            chart: {
                type: 'line'
            },
            title: {
                text: false
            },
            xAxis: {
                categories: [arrWeek[6],arrWeek[5],arrWeek[4],arrWeek[3],arrWeek[2],arrWeek[1],arrWeek[0]]
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            },
            credits: { enabled: false },
            series: [{
                name: '收入',
                data: [parseInt(arrIncome[0]),parseInt(arrIncome[1]),parseInt(arrIncome[2]),parseInt(arrIncome[3]) ,parseInt(arrIncome[4]),parseInt(arrIncome[5]),parseInt(arrIncome[6])]
            }, {
                name: '订单量',
                data: [parseInt(arrTrade[0]),parseInt(arrTrade[1]),parseInt(arrTrade[2]),parseInt(arrTrade[3]) ,parseInt(arrTrade[4]),parseInt(arrTrade[5]) ,parseInt(arrTrade[6])]
            }, {
                name: '交易额',
                data: [parseInt(arrPrice[0]),parseInt(arrPrice[1]),parseInt(arrPrice[2]),parseInt(arrPrice[3]) ,parseInt(arrPrice[4]),parseInt(arrPrice[5]) ,parseInt(arrPrice[6])]
            }]
        });
    });
</script>


<script>
$(function () {
    var income=${income};
    var outcome=${outcome};
    var profit=${inorout}
    $('#contain').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: false
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: '数据统计',
            data: [
                ['盈余', profit],
                ['收入', income],
                ['支出', outcome]
               
            ]
        }]
    });
});
function GetDateStr(AddDayCount) { 
    var dd = new Date(); 
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
    var y = dd.getFullYear(); 
    var m = dd.getMonth()+1;//获取当前月份的日期 
    var d = dd.getDate(); 
    return y+"-"+m+"-"+d; 
} 
function yestoday_trade(){
    var begin_date=GetDateStr(-1);
    var end_date=GetDateStr(0);
    location.href="";
    location.href='${base}/store/member/statistics/sale_total.jhtml?begin_date='+begin_date+'&end_date='+end_date;
}
</script>
  <script>
       Highcharts.setOptions({
           colors: ['#ffcc66', '#e5e8ec', '#6699ff', '#ffcc66', '#24CBE5', '#64E572', '#FF9655',
       '#FFF263', '#6AF9C4']
       });

  </script>
</body>
</html>
