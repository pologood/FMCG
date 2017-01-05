<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"]
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/purse.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="item">
        <div style="background-color: white;">
            <div style="text-align: center;border-bottom: 1px solid #ebebeb;">
                <img style="margin-top: 70px;" width="60px" height="60px" src="${base}/resources/wap/2.0/images/suc.png">
                <p style="padding:10px 0 40px 0;">付款成功</p>
            </div>
            <div style="padding: 0 10px;border-bottom: 1px solid #ebebeb;">
                <table cellspacing="0" border="0" width="100%" style="line-height: 40px;">
                    <tr>
                        <td>收款金额</td>
                        <td style="text-align: right;color: grey;">{{amount}}</td>
                    </tr>
                    <tr>
                        <td>订单编号</td>
                        <td style="text-align: right;color: grey;">{{sn}}</td>
                    </tr>
                    <tr>
                        <td>费用详情</td>
                        <td style="text-align: right;color: grey;">
                            {{#expression type '==' 'payment'}}订单支付{{/expression}}
                            {{#expression type '==' 'recharge'}}账户充值{{/expression}}
                            {{#expression type '==' 'cashier'}}线下代收{{/expression}}
                            {{#expression type '==' 'function'}}功能缴费{{/expression}}
                        </td>
                    </tr>
                </table>
            </div>
            <div style="padding: 15px 20%;">
                <a href="${base}/wap/member/purse/index.jhtml" class="weui_btn weui_btn_warn">完成</a>
            </div>
        </div>
    </script>
</head>
<body style="background-color: white;">
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
        Handlebars.registerHelper("expression", function(left, operator, right, options) {
            if (arguments.length < 3) {
                throw new Error('Handlerbars Helper "compare" needs 2 parameters');
            }
            var operators = {
                '==':     function(l, r) {return l == r; },
                '===':    function(l, r) {return l === r; },
                '!=':     function(l, r) {return l != r; },
                '!==':    function(l, r) {return l !== r; },
                '<':      function(l, r) {return l < r; },
                '>':      function(l, r) {return l > r; },
                '<=':     function(l, r) {return l <= r; },
                '>=':     function(l, r) {return l >= r; },
                'typeof': function(l, r) {return typeof l == r; }
            };

            if (!operators[operator]) {
                throw new Error('Handlerbars Helper "compare" doesn\'t know the operator ' + operator);
            }

            var result = operators[operator](left, right);

            if (result) {
                return options.fn(this);
            } else {
                return options.inverse(this);
            }
        });

        compiler = Handlebars.compile($("#item").html());
        ajaxPost({
            url: '${base}/app/payment/view.jhtml',
            data:{
                sn:"${sn}"
            },
            success: function (data) {
                $(".page").append(compiler(data.data));
            }
        });
    });

</script>

</body>
</html>