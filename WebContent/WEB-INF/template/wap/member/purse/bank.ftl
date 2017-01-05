<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="bank_list">
        {{#each this}}
        <div class="weui_cells weui_cells_access marginTop10">
            <a class="weui_cell" bankId="{{id}}">
                <div class="weui_cell_hd" [#if choose_bank??]onclick="javascript:location.href='${base}/wap/member/purse/cash.jhtml?bankId={{id}}';"[/#if]>
                    <img width="50px" height="50px;" src="{{#if logo}}{{logo}}{{else}}${base}/resources/wap/image/NoPicture.jpg{{/if}}" alt="icon" style="margin-right:5px;display:block">
                </div>
                <div class="weui_cell_bd weui_cell_primary" [#if choose_bank??]onclick="javascript:location.href='${base}/wap/member/purse/cash.jhtml?bankId={{id}}';"[/#if]>
                    <p>{{bankName}}</p>
                    <div style="color:grey;font-size: 16px;line-height: 30px;">
                    <p>{{#ifCond  type "debit"}}储蓄卡{{else}}信用卡{{/ifCond}}</p>
                    <p>*** **** **** {{cardNo}} {{bankName}}</p>
                    </div>
                </div>
                <div class="weui_cell_bd">
                    <div style="height: 84px;">
                        <i onclick="del({{id}},this)" class="iconfont" style="font-size: 21px;float: right;padding:30px 20px;">&#xe610;</i>
                    </div>
                </div>
            </a>
        </div>
        {{/each}}
        <div class="weui_cells weui_cells_access marginTop10">
            <a class="weui_cell" href="[#if ((idcard.authStatus)!'')=='success']${base}/wap/member/purse/addBank.jhtml?choose_bank=${choose_bank!''}[#else]javascript:showDialog2('友情提示','未通过实名认证，暂不能添加银行卡');[/#if]">
                <div class="weui_cell_bd weui_cell_primary">
                    <p style="color:#0088CC;line-height: 80px;text-align: center;">+添加银行卡</p>
                </div>
            </a>
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
        Handlebars.registerHelper('ifCond', function(v1, v2, options) {
            if(v1 === v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        });
        compiler = Handlebars.compile($("#bank_list").html());
        ajaxGet({
            url: '${base}/app/member/bank/list.jhtml',
            success: function (data) {
                $(".page").append(compiler(data.data));
            }
        });


    });

    var del=function(id,obj){
        showDialog1("友情提示","您确认删除此银行卡吗？",function(){
            ajaxPost({
                url:'${base}/app/member/bank/delete.jhtml',
                data:{id:id},
                success:function(data){
                    if(data.message.type=="success"){
                        $(obj).parents(".weui_cells_access").remove();
                        showToast();
                    }else{
                        showDialog2("提示",data.message.content);
                    }
                }
            });
        });
    }
</script>

</body>
</html>