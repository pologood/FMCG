<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-会员中心</title>
    <meta name="keywords" content="${setting.siteName}-会员中心"/>
    <meta name="description" content="${setting.siteName}-会员中心"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/index.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/payfor.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/member.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/cart.css" type="text/css" rel="stylesheet">
    <script type="text/javascript">
        $(function () {
            $("#searchButton").click(function () {
                $("#dateType").val("2");
                $("#listForm").submit();
            });
            //导出数据到excel
            $("#exportBtn").click(function () {
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "账单记录",
                    filename: "账单记录",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
            });

            $("select[name='type']").on("change",function(){
                $("#dateType").val("2");
                $("[name='type']").val($(this).children('option:selected').val());
                $("#listForm").submit();
            });

        });
        function getBill() {
            $("#dateType").val("1");
            $("#listForm").submit();
        [#--location.href = "${base}/b2c/member/wallet/bill/list.jhtml?date=" + $dp.cal.getNewDateStr();--]
        }

        function billDetails(type) {
            $("#dateType").val("2");
            $("#beginDate").val("");
            $("#endDate").val("");
            $("[name='type']").val(type);
            $("#listForm").submit();
        }
    </script>
</head>
<body>
<form id="listForm" action="${base}/b2c/member/wallet/bill/list.jhtml" method="get">
    <input type="hidden" id="dateType" name="dateType" value="1">
    <div class="bill-record-hd">
        <h2>账单记录</h2>
        <div class="date" style="display: none;">
            <input type="text" id="date" name="date" class="date_input"
                   onclick="WdatePicker({dateFmt: 'yyyy-MM',onpicked:getBill,readOnly:true,isShowClear:false,isShowToday:false,autoUpdateOnChanged:false});"
                   value="${date?string("yyyy-MM")}">
            <i>▼</i>
        </div>
        <div>
            <span style="margin-left: 90px;">时间：</span>
            <input type="text" class="text" id="beginDate" name="beginDate" value="${(beginDate?string('yyyy-MM-dd'))!}"
                   onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\',{d:-1})}',onpicked:function(){endDate.focus();}})"
                   readonly> 至 <input type="text" class="text" id="endDate" name="endDate"
                                      value="${(endDate?string('yyyy-MM-dd'))!}"
                                      onfocus="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\',{d:1})}'})" readonly>
            <span style="margin-left: 20px;">类型：</span>
            <select name="type" style="height: 25px;">
                <option value="">全部</option>
            [#list types as typee]
                <option value="${typee}" [#if typee==type]selected[/#if]>
                    [#if typee=='recharge']充值 - 收入[/#if]
                    [#if typee=='payment']购物 - 支出[/#if]
                    [#if typee=='withdraw']提现 - 支出[/#if]
                    [#if typee=='receipts']货款 - 收入[/#if]
                    [#if typee=='profit']分润 - 收入[/#if]
                    [#if typee=='rebate']佣金 - 支出[/#if]
                    [#if typee=='cashier']收款 - 收入[/#if]
                    [#if typee=='income']其他 - 收入[/#if]
                    [#if typee=='outcome']其他 - 支出[/#if]
                    [#if typee=='coupon']红包 - 收入[/#if]
                    [#if typee=='couponuse']红包 - 支出[/#if]
                </option>
            [/#list]
            </select>
            <input id="searchButton" type="button" class="button" value="查询" style="margin-left: 20px;"/>
            <input id="exportBtn" type="button" class="button" value="导出" style="float: right;"/>
        </div>
    </div>
    <div class="bill-record-list">
        <div class="bill-record-list-nav">
            <div class="li detail selected">明细</div>
            <div class="li statistics">统计</div>
        </div>
        <!--明细-->
        <table class="hidden table2excel">
            <tbody>
            [#list page.content as deposit]
            <tr>
                <td>${deposit.createDate?string("yyyy-MM-dd")}</td>
                <td>
                    [#if deposit.type=='recharge']充值
                    [#elseif deposit.type=='payment']购物
                    [#elseif deposit.type=='withdraw']提现
                    [#elseif deposit.type=='receipts']货款
                    [#elseif deposit.type=='profit']分润
                    [#elseif deposit.type=='rebate']佣金
                    [#elseif deposit.type=='cashier']收款
                    [#elseif deposit.type=='income']其他
                    [#elseif deposit.type=='outcome']其他
                    [#elseif deposit.type=='coupon']红包
                    [#elseif deposit.type=='couponuse']红包
                    [/#if]
                </td>
                <td>${deposit.memo}</td>
                <td>
                    [#if deposit.type=='recharge'||deposit.type=='receipts'||deposit.type=='profit'||deposit.type=='cashier'||deposit.type=='income'||deposit.type=='coupon']
                        [#if deposit.credit<0]${deposit.credit}[#else]+${deposit.credit}[/#if]
                    [#else]
                        [#if deposit.debit<0]+${-deposit.debit}[#else]-${deposit.debit}[/#if]
                    [/#if]
                </td>
                <td>[#if deposit.status=='none']处理中[#else]已完成[/#if]</td>
            </tr>
            [/#list]
            </tbody>
        </table>
        <div class="bill-detail">
            <div class="bill-record-list-item">
            [#list page.content as deposit]
                <div class="li [#if deposit_index%2=0]bg[/#if]">
                    <span class="date">${deposit.createDate}</span>
                            <span class="info">
                                [#if deposit.type=='recharge']充值
                                [#elseif deposit.type=='payment']购物
                                [#elseif deposit.type=='withdraw']提现
                                [#elseif deposit.type=='receipts']货款
                                [#elseif deposit.type=='profit']分润
                                [#elseif deposit.type=='rebate']佣金
                                [#elseif deposit.type=='cashier']收款
                                [#elseif deposit.type=='income']其他
                                [#elseif deposit.type=='outcome']其他
                                [#elseif deposit.type=='coupon']红包
                                [#elseif deposit.type=='couponuse']红包
                                [/#if]
                            </span>
                [#--<span class="pay-num">订单支付&nbsp;单号：<em>201603079790456</em></span>--]
                    <span class="pay-num">${deposit.memo}</span>
                            <span class="price">
                                [#if deposit.type=='recharge'||deposit.type=='receipts'||deposit.type=='profit'||deposit.type=='cashier'||deposit.type=='income'||deposit.type=='coupon']
                                    [#if deposit.credit<0]${deposit.credit}[#else]+${deposit.credit}[/#if]
                                [#else]
                                    [#if deposit.debit<0]+${-deposit.debit}[#else]-${deposit.debit}[/#if]
                                [/#if]
                            </span>
                    <span class="status">[#if deposit.status=='none']处理中[#else]已完成[/#if]</span>
                    <!-- a class="details" href="${base}/b2c/member/order/order_detail.jhtml?id=${deposit.id}">详情</a -->
                </div>
            [/#list]
            </div>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
            [#include "/b2c/include/pagination1.ftl"]
        [/@pagination]
        </div>
        <!--统计-->
        <div class="bill-record-list-items bill-statistics display-n">
            <!---->
            <div class="ul">
                <div class="li bg">
                    <div class="left">
                        <div class="img bg-red">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">贷款收入<em>[#if receipts==0]
                                0[#else]${((receipts/total_income)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('receipts')"
                                  style="cursor: pointer;">+￥${receipts?string("0.00")}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-red"
                                 style="width: [#if receipts==0]0[#else]${(receipts/total_income)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
            [#if versionType!=1]
                <div class="li">
                    <div class="left">
                        <div class="img bg-red">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">分润收入<em>[#if profit==0]
                                0[#else]${((profit/total_income)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('profit')"
                                  style="cursor: pointer;">+￥${profit?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-red"
                                 style="width: [#if profit==0]0[#else]${(profit/total_income)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
                <div class="li bg">
                    <div class="left">
                        <div class="img bg-red">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">交易佣金<em>[#if rebate==0]
                                0[#else]${((rebate/total_outcome)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('rebate')"
                                  style="cursor: pointer;">-￥${rebate?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-red"
                                 style="width: [#if rebate==0]0[#else]${(rebate/total_outcome)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
            [/#if]
            </div>
            <!---->
            <div class="ul">
                <div class="li">
                    <div class="left">
                        <div class="img bg-green">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">购物支出<em>[#if payment==0]
                                0[#else]${((payment/total_outcome)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('payment')"
                                  style="cursor: pointer;">-￥${payment?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-green"
                                 style="width: [#if payment==0]0[#else]${(payment/total_outcome)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
            [#if versionType!=1]
                <div class="li bg">
                    <div class="left">
                        <div class="img bg-green">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">线下收款<em>[#if cashier==0]
                                0[#else]${((cashier/total_income)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('cashier')"
                                  style="cursor: pointer;">+￥${cashier?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-green"
                                 style="width: [#if cashier==0]0[#else]${(cashier/total_income)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
                <div class="li">
                    <div class="left">
                        <div class="img bg-green">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">其他支出<em>[#if outcome==0]
                                0[#else]${((outcome/total_outcome)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('outcome')"
                                  style="cursor: pointer;">-￥${outcome?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-green"
                                 style="width: [#if outcome==0]0[#else]${(outcome/total_outcome)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
                <div class="li bg">
                    <div class="left">
                        <div class="img bg-green">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">其他收款<em>[#if income==0]
                                0[#else]${((income/total_income)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('income')"
                                  style="cursor: pointer;">+￥${income?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-green"
                                 style="width: [#if income==0]0[#else]${(income/total_income)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
            [/#if]
            </div>
            <!---->
            <div class="ul">
                <div class="li">
                    <div class="left">
                        <div class="img bg-grey">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">充值<em>[#if recharge==0]
                                0[#else]${((recharge/total_income)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('recharge')"
                                  style="cursor: pointer;">+￥${recharge?string('0.00')}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-grey"
                                 style="width: [#if recharge==0]0[#else]${(recharge/total_income)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
                <div class="li bg">
                    <div class="left">
                        <div class="img bg-grey">
                            <i></i>
                        </div>
                    </div>
                    <div class="right">
                        <div class="info">
                            <span class="f-left">提现<em>[#if withdraw==0]
                                0[#else]${((withdraw/total_outcome)*100)?string('0.00')}[/#if]%</em></span>
                            <span class="" onclick="billDetails('withdraw')"
                                  style="cursor: pointer;">-￥${withdraw?string("0.00")}</span>
                        </div>
                        <div class="progress-bar">
                            <div class="wire-bar bg-grey"
                                 style="width: [#if withdraw==0]0[#else]${(withdraw/total_outcome)*100}[/#if]%;"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    $(function () {
        $(".detail").click(function () {
            $(this).addClass("selected");
            $(this).next().removeClass("selected");
            $(".bill-detail").show();
            $(".bill-statistics").hide();
            $("#billIframe", parent.document).height($("html").height());
        });
        $(".statistics").click(function () {
            $(this).addClass("selected");
            $(this).prev().removeClass("selected");
            $(".bill-detail").hide();
            $(".bill-statistics").show();
            $("#billIframe", parent.document).height($("html").height());
        });
        $("#billIframe", parent.document).height($("html").height());
    });

</script>
</body>