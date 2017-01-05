<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="" content="" />
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心" />
    <meta name="description" content="${setting.siteName}-会员中心" />
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/datautil.js"></script>
	<script type="text/javascript" src="${base}/resources/b2b/js/ePayBank.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon" />
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/cart.css" type="text/css" rel="stylesheet">
    <link href="${base}/resources/b2b/css/credit.css" rel="stylesheet" type="text/css" />
  	<style type="text/css">
  		div.page-wrap .p-skip em {
            padding: 0 4px;
            font-size: 16px;
            line-height: 1.6;
        }
        div.page-wrap {
            margin: 0;
            float: right;
            padding-right:50px;
        }
        div.page-wrap .p-num a, div.page-wrap .p-num b {
            float: left;
            height: 24px;
            line-height: 24px;
            padding: 0 10px;
            font-size: 14px;
        }
        div.page-wrap .p-num a {
            color: #b0b0b0;
            border: 1px solid #d0d0d0;
            background-color: #fff;
        }
        div.page-wrap .p-skip .input-txt {
            float: left;
            width: 30px;
            height: 16px;
            margin: 0 3px;
            line-height: 16px;
            font-size: 14px;
            text-align: center;
            padding: 3px;
            color: #000;
            border: 1px solid #d0d0d0;
        }
        .intro {
            margin: 30px 0px 20px 10px; clear: both;
        }
        .intro ol {
            margin: 5px 0px 0px 5px; color: rgb(102, 102, 102); line-height: 1.5; padding-left: 20px;
        }
        .intro li {
            /* list-style: decimal; */
            /* padding: 2px 0px; */
        }
        table.input th {
            background-color: #F6F6F6;
        }
        .page-nav .links .on{
            background-color: #F6F6F6;
        }
        .imenu {
            width: 98%;
            height: 66px;
            margin-left: 1%;
            margin-right: 1%;
            background-color: black; 
            border-left:black; 
            border-right: black; 
        }
        .bottom{
                display: inline-block;
                border: 1px solid #44b5dd;
                width: 100px;
                height: 30px;
                font-size: 20px;
                line-height: 30px;
                text-align: center;
                border-radius: 5px;
                margin-left: 40%;
                font-color: white;
                background-color: #44b5dd;
        }
        a:hover {
            color: white;
            text-decoration: none;
        }
  	</style>
</head>
<body>
<!-- 头部 -->
<div class="header bg">
[#include "/b2b/include/topnav.ftl"]
</div>

<!--主页内容区start-->
<div class="paper">
    <!-- 会员中心头部-->
    [#include "/b2b/include/member_head.ftl"]
    <!-- 会员中心content -->
    <div class="member-content">
        <div class="container">
            <!-- 会员中心左侧 -->
            [#include "/b2b/include/member_left.ftl"]
            <div class="content">
                <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                    <div class="js-app-header title-wrap" id="app_0000000844">
                        <img class="js-app_logo app-img" src="${base}/resources/helper/images/app_cash.png"/>
                        <dl class="app-info">
                            <dt class="app-title" id="app_name">银行汇款</dt>
                            <dd class="app-status" id="app_add_status"></dd>
                            <dd class="app-intro" id="app_desc">支持国内各大银行往来结算支付申请，快捷方便！</dd>
                        </dl>
                    </div>
                    <ul class="links" id="mod_menus">
                        <li  ><a class="on" hideFocus="" href="${base}/helper/member/cash/index.jhtml">银行汇款</a></li>
                    </ul>
                </div>
                <div class="title" style="font-size:20px;margin-left:25%;line-height:35px;margin-top:20px;">
                    [#if status.type != "error"]
                        [#if credit.status == "wait"]
                            您的汇款申请已经提交成功,请注意资金到账情况。
                        [#elseif credit.status == "wait_success"]
                            您的汇款申请已经提交银行处理,请注意资金到账情况。
                        [#elseif credit.status == "success"]
                            您的汇款申请已经支付成功,请注意资金到账情况。
                            [#elseif credit.status == "wait_failure"]
                              您的汇款申请支付失败，3个工作日内将退回账户
                            [#elseif credit.status == "failure"]
                              您的汇款申请支付失败，请重新填写
                            [/#if]
                        [#else]
                          您的汇款申请失败了，原因：${status.content}
                        [/#if]

                </div>
                    <!-- <a href="${base}/helper/member/cash/index.jhtml" style="font-size:18px;margin-left:35%;line-height:50px;"><span id="jumpTo">10</span>秒后系统会自动跳转</a>    -->
                <script type="text/javascript">  
                         // countDown(10,'${base}/b2c/member/cash/withdraw_index.jhtml');  
                </script>
                <table class="info" style="font-size:18px;line-height:50px;margin-left:20%">
                    [#if status.type != "error"]
                    <tr>
                        <th>
                            汇款日期:
                        </th>
                        <td>
                            ${credit.createDate?string("yyyy-MM-dd HH:mm:ss")}
                        </td>
                    </tr>
                    [/#if]
                    <tr>
                        <th>
                            汇款金额:
                        </th>
                        <td>
                            ${currency(credit.amount, true)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            &nbsp;&nbsp;&nbsp;手续费:
                        </th>
                        <td>
                            ${currency(credit.fee, true)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            返利金额:
                        </th>
                        <td>
                            ${currency(credit.profitAmount, true)}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            汇款账户:
                        </th>
                        <td>
                            ${credit.account}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            账户名称:
                        </th>
                        <td>
                            ${credit.payer}
                        </td>
                    </tr>
                    <tr>
                        <th>
                            银行名称:
                        </th>
                        <td id="bankname">
                            ${credit.bankName}
                        </td>
                    </tr>
                </table>
                <div class="bottom">
                    <a href="javascript:;" onclick="history.go(-1)">继续汇款</a>
                </div>
		    </div>
        </div>
    </div>
    <iframe id="interest" name="interest" src="${base}/b2b/product/interest.jhtml" scrolling="no" width="100%"
            height="auto">
    </iframe>
    <!--标语 -->
    [#include "/b2b/include/slogen.ftl"]
</div>

<script>
    $(function(){
        [@flash_message /]
        $(".seone p").click(function(e){
            $(".seone").toggleClass('open');
            e.stopPropagation();
        });

        $(".seone ul li").click(function(e){
            var _this=$(this);
            $(".seone > p").text(_this.attr('data-value'));
            _this.addClass("Selected").siblings().removeClass("Selected");
            $(".seone").removeClass("open");
            e.stopPropagation();
        });

        $(document).on('click',function(){
            $(".seone").removeClass("open");
        })
        
        $(".select p").click(function(e){
            $(".select").toggleClass('open');
            e.stopPropagation();
        });

        $(".select ul li").click(function(e){
            var _this=$(this);
            $(".select > p").text(_this.attr('data-value'));
            _this.addClass("Selected").siblings().removeClass("Selected");
            $(".select").removeClass("open");
            e.stopPropagation();
        });

        $(document).on('click',function(){
            $(".select").removeClass("open");
        })
        //====================================
        var bankInfo = getBankInfo("${credit.bank}");
        $("#bankname").html(bankInfo.bankname);       
    });
    //禁止按键F5
    document.onkeydown = function(e){
        e = window.event || e;
        var keycode = e.keyCode || e.which;
        if( keycode = 116){
            if(window.event){// ie
                try{e.keyCode = 0;}catch(e){}
                e.returnValue = false;
            }else{// ff
                e.preventDefault();
            }
        }
    }

    function countDown(secs,surl){           
        var jumpTo = document.getElementById('jumpTo');  
        jumpTo.innerHTML=secs;    
        if(--secs>0){       
             setTimeout("countDown("+secs+",'"+surl+"')",1000);       
        }else{         
            location.href=surl;       
            -ma  
        }       
    }

</script>

<!--底部 -->
[#include "/b2b/include/footer.ftl"]
<!--右侧悬浮框-->
<div class="actGotop"><a class="icon05" href="javascript:;"></a></div>

</body>
</html>
