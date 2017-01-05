<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>订单详情</title>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/weui.min.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/slider.min.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/weui-extend.css"/>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/fonts/iconfont.css"/>

    <style type="text/css">
        div{
            font-family: "Microsoft YaHei"
        }
    </style>
    <script src="${base}/resources/wap/2.0/js/zepto.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/slider.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/lazyLoad.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>

    <script  type="text/html"  id="tpl_component">
        <!--BEGIN dialog1-->
        <div class="weui_dialog_confirm" id="dialog1" style="display: none;">
            <div class="weui_mask"></div>
            <div class="weui_dialog">
                <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>
                <div class="weui_dialog_bd">自定义弹窗内容，居左对齐显示，告知需要确认的信息等</div>
                <div class="weui_dialog_ft">
                    <a href="javascript:;" class="weui_btn_dialog default" id="idNo">取消</a>
                    <a href="javascript:;" class="weui_btn_dialog primary" id="idOk">确定</a>
                </div>
            </div>
        </div>
        <!--END dialog1-->
        <!--BEGIN dialog2-->
        <div class="weui_dialog_alert" id="dialog2" style="display: none;">
            <div class="weui_mask"></div>
            <div class="weui_dialog">
                <div class="weui_dialog_hd"><strong class="weui_dialog_title">弹窗标题</strong></div>
                <div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div>
                <div class="weui_dialog_ft">
                    <a href="javascript:;" class="weui_btn_dialog primary">确定</a>
                </div>
            </div>
        </div>
        <!--END dialog2-->

        <!--BEGIN toast-->
        <div id="toast" style="display: none;">
            <div class="weui_mask_transparent"></div>
            <div class="weui_toast">
                <i class="weui_icon_toast"></i>
                <p class="weui_toast_content">已完成</p>
            </div>
        </div>
        <!--end toast-->

        <!-- loading toast -->
        <div id="loadingToast" class="weui_loading_toast" style="display:none;">
            <div class="weui_mask_transparent"></div>
            <div class="weui_toast">
                <div class="weui_loading">
                    <div class="weui_loading_leaf weui_loading_leaf_0"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_1"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_2"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_3"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_4"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_5"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_6"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_7"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_8"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_9"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_10"></div>
                    <div class="weui_loading_leaf weui_loading_leaf_11"></div>
                </div>
                <p class="weui_toast_content">数据加载中</p>
            </div>
        </div>
        <!--end loading toast-->

        <!--BEGIN actionSheet-->
        <div id="actionSheet_wrap">
            <div class="weui_mask_transition" id="mask"></div>
            <div class="weui_actionsheet" id="weui_actionsheet">
                <div class="weui_actionsheet_menu">
                    <div class="weui_actionsheet_cell">示例菜单</div>
                    <div class="weui_actionsheet_cell">示例菜单</div>
                    <div class="weui_actionsheet_cell">示例菜单</div>
                    <div class="weui_actionsheet_cell">示例菜单</div>
                </div>
                <div class="weui_actionsheet_action">
                    <div class="weui_actionsheet_cell bg-safe color-revert" id="actionsheet_cancel">取消</div>
                </div>
            </div>
        </div>
        <!--END actionSheet-->
    </script>

</head>
<body>
<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
<div class="page">
    <div class="weui_cells_title" style="background-color:#E8EAE9;line-height:2.0;margin-top:0px;margin-bottom:0px;position:static;">
        <span style="font-size:18px;color:#000000;">交易状态</span>
        <span style="margin-left:20px;color:red;">${trade.orderStatus}未付款</span>
    </div>

    <div style="padding-left:10px;padding-right:10px;margin-bottom:15px;height:60px;" id="yifukuan">
        <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" alt="icon" style="width:2px;height:25px;margin-left:4px;margin-bottom:-2px;" id="yifukuan_shang_img">
        <div style="">
            <img src="/tiaohuo/resources/wap/2.0/images/yuan.png" id="yifukuan_yuan_img" alt="icon" style="width:10px;">
            ${trade.paymentStatus}已付款<span style="float:right;">2016-1-30 8:00:00<img src="/tiaohuo/resources/wap/2.0/images/xia.png" id="yifukuan_zhedie_img" alt="icon" style="width:10px;margin-left:5px;"></span>
        </div>
        <div style="float:left;overflow:hidden;">
            <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" id="yifukuan_xia_img" alt="icon" style="width:2px;height:26px;margin-left:4px;display:none;">
        </div>
        <div style="float:left;margin-left:3px;">
            <p style="font-size:10px;color:#888;display:inline;margin-left:12px;line-height:15px;" id="order_code">单号:12346789，订单已付款，请耐心等待商家发货</p> 
        </div>
    </div>

    <div style="margin-left:24px;margin-top:7px;padding:0; width:88%;height:1px;background-color:#EFEFEF;overflow:hidden;display:none;" id="yifukuan_line"></div>

    <div style="padding-left:10px;padding-right:10px;height:60px;display:none;" id="yifahuo">
        <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" id="yifahuo_shang_img" alt="icon" style="width:2px;height:25px;margin-left:4px;margin-bottom:-2px;">
        <div style="">
          <img src="/tiaohuo/resources/wap/2.0/images/yuan.png" id="yifahuo_yuan_img" alt="icon" style="width:10px;">
          ${trade.shippingStatus}已发货<span style="float:right;margin-right:15px;">2016-1-30 8:00:00</span>
        </div>
        <div style="float:left;">
            <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" id="yifahuo_xia_img"alt="icon" style="width:2px;height:26px;margin-left:4px;">
        </div>
        <div style="float:left;">
            <p style="font-size:10px;color:#888;display:inline;margin-left:9px;line-height:15px;width:100%;">单号:12346789，订单已发货，请耐心等待配送</p>
        </div>
    </div>

    <div style="margin-left:24px;margin-top:7px;padding:0; width:88%;height:1px;background-color:#EFEFEF;overflow:hidden;display:none;" id="yifahuo_line"></div>

    <div style="padding-left:10px;padding-right:10px;height:60px;display:none;" id="yiqianshou">
        <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" id="yiqianshou_shang_img" alt="icon" style="width:2px;height:25px;margin-left:4px;margin-bottom:-2px;">
        <div style="">
          <img src="/tiaohuo/resources/wap/2.0/images/yuan.png" id="yiqianshou_yuan_img" alt="icon" style="width:10px;">
          ${trade.shippingStatus}已签收/未评价<span style="float:right;margin-right:15px;">2016-1-30 8:00:00</span>
        </div>
        <div style="float:left;">
            <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" id="yiqianshou_xia_img" alt="icon" style="width:2px;height:26px;margin-left:4px;">
        </div>
        <div style="float:left;">
            <p style="font-size:10px;color:#888;display:inline;margin-left:9px;line-height:15px;width:100%;">订单已签收，您还没评价</p>
        </div>
    </div>

    <div style="margin-left:24px;margin-top:7px;padding:0; width:88%;height:1px;background-color:#EFEFEF;overflow:hidden;display:none;" id="yiqianshou_line"></div>

    <div style="padding-left:10px;padding-right:10px;height:60px;margin-bottom:10px;display:none;" id="yiwancheng">
        <img src="/tiaohuo/resources/wap/2.0/images/shuxian.png" id="yiwancheng_shang_img" alt="icon" style="width:2px;height:25px;margin-left:4px;margin-bottom:-2px;">
        <div style="">
          <img src="/tiaohuo/resources/wap/2.0/images/dui.png" id="yiwancheng_dui_img" alt="icon" style="width:13px;margin-left:-1px;">
          ${trade.orderStatus}<span style="float:right;margin-right:17px;">2016-1-30 8:00:00</span>
        </div>
        
        <div style="float:left;">
            <p style="font-size:10px;color:#888;display:inline;margin-left:17px;line-height:15px;width:100%;">订单已完成，欢迎您的再次光临</p>
        </div>
    </div>
    <div class="weui_cells_title" style="background-color:#E8EAE9;line-height:2.0;margin-top:0px;margin-bottom:0px;">
        <span style="font-size:18px;color:#000000;">其他信息</span>
    </div>
    <div class="weui_cells" style="font-color:#A0A0A0;line-height:5px;font-size:15px;border-top:0;">
        <div class="weui_cell" style="margin-top:10px;position:initial;">
            <div class="weui_cell_bd weui_cell_primary">
                <p style="color:#888">支付方式</p>
            </div>
            <div class="weui_cell_ft"></div>
        </div>
        <div class="weui_cell" style="position:initial;">
            <div class="weui_cell_bd weui_cell_primary" style="color:#888">
                <p>配送方式</p>
            </div>
            <div class="weui_cell_ft">货到付款</div>
        </div>
        <div class="weui_cell" style="position:initial;">
            <div class="weui_cell_bd weui_cell_primary" style="color:#888">
                <p>订单号</p>
            </div>
            <div class="weui_cell_ft ">123456789</div>
        </div>
        <div class="weui_cell" style="margin-bottom:10px;position:initial;">
            <div class="weui_cell_bd weui_cell_primary" style="color:#888">
                <p>提货吗</p>
            </div>
            <div class="weui_cell_ft">888888888</div>
        </div>
    </div>
    
    
    <div class="weui_cells_title" style="background-color:#E8EAE9;line-height:2.0;margin-top:0px;margin-bottom:0px;">
        <span style="font-size:18px;color:#000000;">商品信息</span>
    </div>
    <div class="weui_cells weui_cells_access" style="margin-top:0px;">
        <a class="weui_cell" href="javascript:;" >
            <div class="weui_cell_hd">
                <img src="${base}/resources/wap/2.0/images/order_info.png" alt="icon" style="width:30px;margin-right:5px;display:block">
            <#--</div>-->
            <div class="weui_cell_bd weui_cell_primary">
                <p>时代曼都品牌鞋店</p>
            </div>
            <div class="weui_cell_ft"></div>    
        </a>
        <div class="weui_cells weui_cells_access" style="margin-top:0px;">
          <a class="weui_cell" href="javascript:;">
            <div class="weui_cell_hd">
              <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png" alt="icon" style="width:80px;margin-right:5px;display:block">
            </div>
            <div class="weui_cell_bd weui_cell_primary">
              <p style="font-size:15px;">大码女鞋鱼嘴鞋大码女鞋鱼嘴鞋</p>
              <p style="font-size:15px;color:#E6C99C">全场满30减10</p>
              <p style="font-size:10px;color:#A0A0A0;">颜色:红色  尺码:38码</p>
            </div>
            <div style="text-align:right;">
              <p>￥23.00</p>
              <p>&nbsp;</p>
              <p>x1</p>
            </div>
          </a>
          <a class="weui_cell" href="javascript:;">
            <div class="weui_cell_hd">
              <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png" alt="icon" style="width:80px;margin-right:5px;display:block">
            </div>
            <div class="weui_cell_bd weui_cell_primary">
              <p style="font-size:15px;">大码女鞋鱼嘴鞋大码女鞋鱼嘴鞋</p>
              <p style="font-size:15px;color:#E6C99C">全场满30减10</p>
              <p style="font-size:10px;color:#A0A0A0;">颜色:红色  尺码:38码</p>
            </div>
            <div style="text-align:right;">
              <p>￥23.00</p>
              <p>&nbsp;</p>
              <p>x1</p>
            </div>
          </a>
          <a class="weui_cell" href="javascript:;">
            <div class="weui_cell_bd weui_cell_primary">
              <p >商品金额</p>
              <p>优惠</p>
              <p style="">运费</p>
              <p>&nbsp;</p>
            </div>
            <div style="text-align:right;">
              <p>￥23.00</p>
              <p>-￥23.00</p>
              <p>0</p>
              <p>实付款：<span style="color:red;">￥23.00</span></p>
            </div>
          </a>
        </div>
    </div>

    <div class="weui_cells_title" style="background-color:#E8EAE9;line-height:2.0;margin-top:0px;text-align:right;padding-right:20px;">
        <a href="javascript:" class="weui_btn weui_btn_mini weui_btn_plain_default">删除订单</a>
        <a href="javascript:" class="weui_btn weui_btn_mini weui_btn_plain_default" style="color:red;border-color:red;">立即评价</a>
    </div>
</div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $("#yifukuan_zhedie_img").on("click",function(){
            var yifukuan_zhedie_img=$("#yifukuan_zhedie_img").attr("src");
            var xiaPng = "${base}"+"/resources/wap/2.0/images/xia.png";
            var shangPng = "${base}"+"resources/wap/2.0/images/shang.png";
            if(yifukuan_zhedie_img==xiaPng){
                $("#yifahuo").show();
                $("#yifahuo_line").show();
                $("#yiqianshou").show();
                $("#yiqianshou_line").show();
                $("#yiwancheng").show();
                $("#yifukuan_line").show();
                $("#yifukuan_xia_img").show();
                $("#yifukuan").css("margin-bottom","0px;");
                $("#order_code").css("margin-left","6px;");
                $("#yifukuan_zhedie_img").attr("src",shangPng);
            }else if(yifukuan_zhedie_img==shangPng){
                $("#yifukuan_zhedie_img_2").attr("id","yifukuan_zhedie_img");
                $("#yifahuo").hide();
                $("#yifahuo_line").hide();
                $("#yiqianshou").hide();
                $("#yiqianshou_line").hide();
                $("#yiwancheng").hide();
                $("#yifukuan_line").hide();
                $("#yifukuan_xia_img").hide();
                $("#yifukuan").css("margin-bottom","15px;");
                $("#order_code").css("margin-left","12px;");
                $("#yifukuan_zhedie_img").attr("src",xiaPng);
            }    
        });

    });
</script>
</body>
</html>
