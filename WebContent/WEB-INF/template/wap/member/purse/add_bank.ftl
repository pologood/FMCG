<!DOCTYPE html>
<html lang="en">
<head>
    <title>添加银行卡</title>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <script src="${base}/resources/wap/2.0/js/bank.js"></script>
[#include "/wap/component/bank.ftl"]
<style type="text/css">
    .papering{
        position: fixed;
    }
</style>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page papering" id="weui_wraper">
            <div id="add_bank">
                <div style="background-color: #fff391;line-height: 32px;color:red;padding: 0 10px;">
                    该交易仅支持储蓄卡
                </div>
                <div style="display: inline-block;width: 100%;padding: 10px 10px;">
                    <div style="display: inline-block;width: 100%;border-bottom: 1px solid;padding-bottom: 5px;">
                        <div style="display: inline-block;float: left;width: 33%;">输入银行卡号</div>
                        <div style="display: inline-block;float: left;width: 34%;text-align: center;">信息校验</div>
                        <div style="display: inline-block;float: left;width: 33%;text-align: right;">付款</div>
                    </div>
                    <div style="display:inline-block;width: 8px;height: 8px;border-radius: 50%;background-color: black;float: left;margin-top: -8px;"></div>
                    <div style="display:inline-block;width: 8px;height: 8px;border-radius: 50%;background-color: black;float: left;margin-left: 50%;margin-top: -8px;"></div>
                    <div style="display:inline-block;width: 8px;height: 8px;border-radius: 50%;background-color: black;float: right;margin-top: -8px;"></div>
                </div>
                <div class="weui_cells weui_cells_form marginTop0">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label class="weui_label">卡号</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input id="cardNo" class="weui_input" type="tel" placeholder="输入银行卡号">
                        </div>
                    </div>
                </div>
                <p style="padding: 10px 10px;color: grey;">${setting.siteName}智能加密，保证您的用卡安全。</p>
                <div style="padding:10px 16%;">
                    <a href="javascript:;" onclick="bankInfo()" class="weui_btn weui_btn_warn">下一步</a>
                </div>
            </div>

            <div id="bank_info" style="display: none;">
                <div class="weui_cells weui_cells_access marginTop10">
                    <a class="weui_cell" href="javascript:;" id="bank">
                        <div class="weui_cell_bd">
                            <span>卡类型</span>
                            <span style="color: #a9a9a9;" id="bankName" >请选择发卡银行</span>
                        </div>
                    </a>
                </div>
                <div class="weui_cells weui_cells_form marginTop10">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label class="weui_label">姓名</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input id="depositUser" class="weui_input" placeholder="">
                        </div>
                    </div>
                </div>
                <div class="weui_cells weui_cells_form  marginTop10">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label class="weui_label">证件号</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input id="noId" class="weui_input" type="tel" placeholder="">
                        </div>
                    </div>
                </div>
                <p style="padding: 10px 10px 0 10px;color:grey;">信息加密处理，仅用于银行验证</p>
                <div class="weui_cells weui_cells_form marginTop10">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label class="weui_label">手机号</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input id="mobile" oninput="value=value.replace(/[^\d]/g,'')" class="weui_input" type="tel" placeholder="请输入银行预留手机号" maxlength="11">
                        </div>
                    </div>
                </div>
                <p style="padding: 10px 10px;">同意<span style="color: #0088CC;">《服务协议》</span></p>
                <div style="padding:10px 16%;">
                    <a href="javascript:;" class="weui_btn weui_btn_warn" onclick="captcha()">确认添加</a>
                </div>
            </div>
            <div id="send_captcha" style="display: none;">
                <div>
                    <div style="padding: 10px;10px;font-size: 16px;">请输入手机号<span id="cell"></span>收到的验证码</div>
                    <div style="background-color: #ffffff;">
                        <div class="weui_cells weui_cells_form marginTop0">
                            <div class="weui_cell" style="padding: 0 10px;">
                                <div class="weui_cell_hd"><label class="weui_label">验证码</label></div>
                                <div class="weui_cell_bd weui_cell_primary">
                                    <input class="weui_input" type="tel" placeholder=""
                                           style="width: calc(100% - 80px);height: 42px;" id="captcha">
                                    <div style="width: 75px;float: right;">
                                        <a href="javascript:;" class="weui_btn weui_btn_warn"
                                           style="padding-left: 0;padding-right: 0;" id="reSend">重新获取</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="padding:10px 10px;">
                        <a href="javascript:;" class="weui_btn weui_btn_warn" onclick="submit()">确定</a>
                    </div>
                </div>
            </div>
    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        $("#bank").click(function(){
            findBank(function(id,title){
                $("#bankName").attr("bankInfoId",id);
                $("#bankName").text(title);
            });
        });
    });

    function submit(){
        ajaxPost({
            url:'${base}/app/member/bank/save.jhtml',
            data:{
                depositUser:$("#depositUser").val(),
                cardNo:$("#cardNo").val(),
                bankInfoId:$("#bankName").attr("bankInfoId"),
                captcha:$("#captcha").val(),
                depositBank:$("#bankName").text()
                //idCardNo:$("#noId").val()
            },
            success:function(data){
                if(data.message.type=="success"){
                    showToast({content:"绑卡成功"});
                    setTimeout(function(){
                        location.href="${base}/wap/member/purse/bank.jhtml?choose_bank=${choose_bank!''}";
                    },1000);
                }else{
                    showDialog2("提示",data.message.content);
                }
            }
        });
    }

    function bankInfo(){
        if($("#cardNo").val().trim()==""){
            showDialog2("友情提示","请输入银行卡号！");
            return;
        }else if(!luhmCheck($("#cardNo").val().trim())){
            showDialog2("友情提示","请输入正确的银行卡号！");
            return;
        }
        $("#add_bank").hide();
        $("#bank_info").show();
    }

    function captcha(){
        if($("#bankName").attr("bankInfoId")==null){
            showDialog2("提示","请选择发卡银行！");
            return;
        }
        if($("#depositUser").val().trim()==""){
            showDialog2("提示","请输入姓名！");
            return;
        }
        if($("#noId").val()==""){
            showDialog2("提示","请输入证件号！");
            return;
        }
        if($("#mobile").val().trim()==""||!/^1\d{10}$/.test($("#mobile").val().trim())){
            showDialog2("友情提示","请输入正确的手机号码！");
            return;
        }
        $("#bank_info").hide();
        $("#send_captcha").show();
        $("#cell").text($("#mobile").val().trim());
        sendCaptcha();
    }

    var InterValObj;
    var count=60;
    var curCount=count;

    function sendCaptcha(){
        $("#reSend").unbind("click");
        ajaxPost({
            url:'${base}/app/member/bank/send_mobile.jhtml',
            data:{
                mobile:$("#mobile").val().trim()
            },
            success:function(data){
                if(data.message.type=="success"){
                    showToast({content:"发送成功"});
                    InterValObj = window.setInterval(SetRemainTime, 1000);
                }
            }
        });
    }

    function SetRemainTime(){
        if(curCount==0){
            window.clearInterval(InterValObj);
            curCount=count;
            $("#reSend").text("重新获取");
            $("#reSend").bind("click",function(){
                sendCaptcha();
            });
        }else{
            curCount--;
            $("#reSend").text(curCount+"秒");
        }
    }

    //银行卡号校验

    //Description:  银行卡号Luhm校验

    //Luhm校验规则：16位银行卡号（19位通用）:

    // 1.将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
    // 2.将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
    // 3.将加法和加上校验位能被 10 整除。
    function luhmCheck(bankno){
        if (bankno.length < 16 || bankno.length > 19) {
            //$("#banknoInfo").html("银行卡号长度必须在16到19之间");
            return false;
        }
        var num = /^\d*$/;  //全数字
        if (!num.exec(bankno)) {
            //$("#banknoInfo").html("银行卡号必须全为数字");
            return false;
        }
        //开头6位
        var strBin="10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";
        if (strBin.indexOf(bankno.substring(0, 2))== -1) {
            //$("#banknoInfo").html("银行卡号开头6位不符合规范");
            return false;
        }
        var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）

        var first15Num=bankno.substr(0,bankno.length-1);//前15或18位
        var newArr=new Array();
        for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组
            newArr.push(first15Num.substr(i,1));
        }
        var arrJiShu=new Array();  //奇数位*2的积 <9
        var arrJiShu2=new Array(); //奇数位*2的积 >9

        var arrOuShu=new Array();  //偶数位数组
        for(var j=0;j<newArr.length;j++){
            if((j+1)%2==1){//奇数位
                if(parseInt(newArr[j])*2<9)
                    arrJiShu.push(parseInt(newArr[j])*2);
                else
                    arrJiShu2.push(parseInt(newArr[j])*2);
            }
            else //偶数位
                arrOuShu.push(newArr[j]);
        }

        var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数
        var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数
        for(var h=0;h<arrJiShu2.length;h++){
            jishu_child1.push(parseInt(arrJiShu2[h])%10);
            jishu_child2.push(parseInt(arrJiShu2[h])/10);
        }

        var sumJiShu=0; //奇数位*2 < 9 的数组之和
        var sumOuShu=0; //偶数位数组之和
        var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
        var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和
        var sumTotal=0;
        for(var m=0;m<arrJiShu.length;m++){
            sumJiShu=sumJiShu+parseInt(arrJiShu[m]);
        }

        for(var n=0;n<arrOuShu.length;n++){
            sumOuShu=sumOuShu+parseInt(arrOuShu[n]);
        }

        for(var p=0;p<jishu_child1.length;p++){
            sumJiShuChild1=sumJiShuChild1+parseInt(jishu_child1[p]);
            sumJiShuChild2=sumJiShuChild2+parseInt(jishu_child2[p]);
        }
        //计算总和
        sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);

        //计算Luhm值
        var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;
        var luhm= 10-k;

        if(lastNum==luhm){
//            $("#banknoInfo").html("Luhm验证通过");
            return true;
        }
        else{
//            $("#banknoInfo").html("银行卡号必须符合Luhm校验");
            return false;
        }
    }
</script>

</body>
</html>