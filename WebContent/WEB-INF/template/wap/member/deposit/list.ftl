<!doctype html>
<html>
<head>
        [#include "/wap/include/resource.ftl"]
           <script src="${base}/resources/wap/js/iscrollEvents.js"></script>
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/handlebars.min.js"></script>
    <title>我的账单</title>
    <style>
        #wrapper {
            padding: 0px;
        }
        /*我的账单*/        
 #events-list li{height: 61px;border: 1px solid #dedede;}
        #events-list li .deposit_left{height: 49px;  line-height: 49px;}

        .bill-list{position:absolute;top:44px;left:0;right:0;bottom:0;}
        .bill-content{padding:0;}
        .bill-content ul{position:relative;width:100%;height:auto;}
        .bill-content ul li{position:relative;width:100%;height:60px;border-bottom:1px solid #f1f1f1;background:#fff;}
        .bill-title{position:relative;width:100%;height:42px;background:url(../images/bill-title.jpg) repeat-x;border-bottom:none;}
        .bill-month{float:left;width:60px;height:20px;margin-top:5px;margin-left:10px;text-align:center;border-right:1px solid #d4975c;line-height:20px;font-size:1em;}
        .bill-in{color: red;float:left;height:33px;margin-top:5px;margin-left:5px;text-align:left;line-height:33px;font-size:1em;}
        .bill-out{float:right;height:33px;margin-top:5px;margin-right:10px;text-align:right;line-height:33px;font-size:1em;color:green;}.item-content{position:absolute;top:5px;left:10px;right:10px;bottom:5px;}
        .item-seller{float:left;width:60px;height:100%;border-right:1px solid #f1f1f1;}
        .item-seller img{float:left;width:35px;height:35px;background:#000;margin-left:10px;}
        .item-seller p{float:left;width:100%;height:15px;line-height:15px;font-size:0.55em;text-overflow:ellipsis; white-space:nowrap; overflow:hidden; text-align:center;color:#000;}
        .item-details{position:absolute;left: 43px;top:0;right:0;bottom:0;}
        .item-details-l{position:absolute;left:0;right:50%;top:0;bottom:0;}

        .item-details-r{position:absolute;left:50%;right:0;top:0;bottom:0;}
        .bill-name{overflow: hidden;margin-top: 3px;font-weight: bold;font-size: smaller; margin: 0px;position:absolute;left:5px;right:0;top:0;height:20px;text-align:left;line-height:20px;color:#000;text-overflow:ellipsis; white-space:nowrap; }
        .bill-time{font-size: x-small;margin: 0px;position:absolute;left:5px;right:0;height:20px;text-align:left;color:#808080;line-height:20px;bottom: 0px;}
        .bill-money{margin: 0px;position:absolute;left:0;right:0;top:0;height:25px;text-align:right;font-size:1.5em;line-height:25px;color:#000;}
        .bill-state{margin: 0px;position:absolute;left:0;right:0;height:20px;text-align:right;font-size:0.9em;color:#808080;line-height:20px;-o-text-overflow: ellipsis;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;bottom: 0px;}
    </style>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li style="height: 61px;">
                                <div class="item-content">
                                    <div class="deposit_left">
                                    {{#expression type '==' 'recharge'}}充值{{/expression}}
                                        {{#expression type '==' 'payment'}}购物{{/expression}}
                                        {{#expression type '==' 'withdraw'}}提现{{/expression}}
                                        {{#expression type '==' 'receipts'}}货款{{/expression}}
                                        {{#expression type '==' 'profit'}}分润{{/expression}}
                                        {{#expression type '==' 'rebate'}}佣金{{/expression}}
                                        {{#expression type '==' 'cashier'}}收款{{/expression}}
                                        {{#expression type '==' 'income'}}其他{{/expression}}
                                        {{#expression type '==' 'outcome'}}其他{{/expression}}
                                    </div>
                                    <a href="javascript:;" class="item-details">
                                        <div class="item-details-l">
                                            <p class="bill-name">{{memo}}</p>
                                            <p class="bill-time">{{#formatDate createDate}}{{/formatDate}}</p>
                                        </div>
                                        <div class="item-details-r">
                                            
                                    {{#expression type '==' 'payment'}}
                                    <p class="bill-money" style="color: green" >-{{debit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'withdraw'}}
                                    <p class="bill-money" style="color: green" >-{{debit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'rebate'}}
                                    <p class="bill-money" style="color: green" >-{{debit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'outcome'}}
                                    <p class="bill-money" style="color: green" >-{{debit}}</p>
                                        {{/expression}}
                                        
                                        {{#expression type '==' 'recharge'}}
                                    <p class="bill-money" style="color: red">+{{credit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'receipts'}}
                                    <p class="bill-money" style="color: red">+{{credit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'profit'}}
                                    <p class="bill-money" style="color: red">+{{credit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'cashier'}}
                                    <p class="bill-money" style="color: red">+{{credit}}</p>
                                        {{/expression}}
                                        {{#expression type '==' 'income'}}
                                    <p class="bill-money" style="color: red">+{{credit}}</p>
                                        {{/expression}}
                                         <p class="bill-state">余额:{{balance}}</p>
                                        </div>
                                    </a>
                                </div>
                            </li>
                            
        
        {{/each}}
    </script>
    <script>
        $(function () {
        var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                    api: "${base}/wap/member/deposit/addMore.jhtml",
                    pageNumber: ${pageable.pageNumber},
                    pageSize: ${pageable.pageSize}
                }
            });
           app.init();
          }); 
    </script>
</head>
<body>
<div class="page">
      <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">我的账单</h1>
      </header>
    </header>
    <div id="wrapper" style="background: #fff">
        <div>
            <div class="am-g" style="height: 42px;background: #dedede">
                <span class="am-align-left am-text-danger am-margin-left-sm" style="line-height: 42px;">总收入:${income}</span>
                <span class="am-align-right am-text-success  am-margin-right-sm" style="line-height: 42px;">总支付:${outcome}</span>
            </div>
            <div class="am-g">
                <div data-am-widget="list_news" class="am-list-news am-list-news-default" style="margin: 0px auto">
                    <div class="am-list-news-bd">
                        <ul class="am-list " id="events-list">
                            <li class="am-list-item-desced">
                                <div class="am-list-item-text"> 正在加载内容...</div>
                            </li>
                        </ul>
                        <div class="pull-action" id="pull-up">
                            <span class="am-icon-arrow-down pull-label" id="pull-up-label">上拉加载更多</span>
                            <span class="am-icon-spinner am-icon-spin"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>