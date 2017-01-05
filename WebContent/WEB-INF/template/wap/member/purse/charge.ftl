<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="balace_item">
        <div class="weui_cells weui_cells_access marginTop0">
            <a class="weui_cell" href="javascript:;" style="background-color: #e53535;color: #ffffff;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>当前余额</p>
                    <p class="balance"><span class="small-font">￥</span><span class="font25">{{balance}}</span></p>
                </div>
            </a>
            <a class="weui_cell tx" href="javascript:;" style="background-color: #fe5b4a;color: #ffffff;">
                <div class="weui_cell_bd weui_cell_primary">
                    <p>可提金额</p>
                    <p class="withdrawbalance"><span class="small-font">￥</span><span class="font25">{{withdrawBalance}}</span></p>
                </div>
            </a>
        </div>
        <div class="weui_cells weui_cells_form marginTop0">
            <div class="weui_cell">
                <div class="weui_cell_hd"><label class="">充值金额（￥）</label></div>
                <div class="weui_cell_bd weui_cell_primary">
                    <input oninput="num(this)" onpropertychange="num(this)" id="amount" class="weui_input" type="tel" placeholder="请输入充值金额" maxlength="18">
                </div>
            </div>
        </div>
        <div class="weui_cells" style="margin-top: 10px;background-color: #ffffff;">
            <div style="padding:10px 20%;">
                <a href="javascript:;" class="weui_btn weui_btn_warn" onclick="submit()">提交</a>
            </div>
            <div style="padding:0 10px;">
                <div class="paddingButtom10">相关说明：</div>
                <div class="paddingButtom10">1.使银联卡无卡支付充值，不收取任何手续费，不需要开通网银即可使用。</div>
                <div class="paddingButtom10">2.暂不支持银行汇款等线下方式进行充值。</div>
                <div class="">3.可使用引用卡充值、储蓄卡进行充值、单笔限额5000元。</div>
            </div>
        </div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page">

    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>

<script>
    $(function () {
        init();

        compiler = Handlebars.compile($("#balace_item").html());
        ajaxGet({
            url: '${base}/app/member/balance.jhtml',
            success: function (data) {
                $(".page").append(compiler(data.data));
            }
        });
    });
    function submit(){
        if($("#amount").val().trim()==""){
            showDialog2("友情提示","请输入金额！");
            return;
        }
        location.href="${base}/wap/member/order/pay.jhtml?amount="+$("#amount").val();
    }
    function num(o){
        var val = $(o).val();
        val = val.replace(/[^\d\.]/g,'');
        if(val==""){
            $(o).val("");
            return false;
        }
        var arr=val.split(".");
        if(arr.length>=2){
            var newVal="";
            $.each(arr,function(k,v){
                if(k==0){
                    if (v == "") {
                        newVal = "0.";
                    } else {
                        newVal += Number(v) + ".";
                    }
                }else{
                    newVal+=v;
                }
            });
            $(o).val(newVal);
            return true;
        }
        $(o).val(Number(val));
        return true;
    }
</script>

</body>
</html>