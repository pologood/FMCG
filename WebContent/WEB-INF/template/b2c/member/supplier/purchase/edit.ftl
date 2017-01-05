<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-销售明细</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/foundation-datepicker.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/foundation-datepicker.zh-CN.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2c/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2c/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>

<body class="bg-base">
[#include "/b2c/include/supplier_left.ftl"]
<div class="f-left rt">
[#include "/b2c/include/supplier_header.ftl"]
    <div class="breadcrumbs">
        <ul class="breadcrumb">
            <p>当前位置：</p>
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>数据统计</li>
            <li>销售明细</li>
        </ul>
    </div>
[#include "/b2c/include/supplier_top.ftl"]

    <div class="tb-container">

        <div class="input">
            <table class="input" style="border-top: solid 1px #e0e0e0;">
                <tr>
                    <th>
                        单据编号：
                    </th>
                    <td>
                        <input type="text" name="sn" class="text"
                               value="${purchase.sn}" readonly/><span id="checkMsg" style="color:red">

                            <!-- input type="submit" class="button" onclick="window.print();" value="打印"/ -->

                            <a href="${base}/b2c/member/supplier/purchase/print.jhtml?id=${purchase.id}" target="_blank"
                               class="button" style="float: none;">
                                打印
                            </a>
                    </td>
                </tr>
                <tr>
                    <th>
                        <span class="requiredField">*</span>要货时间：
                    </th>
                    <td>
                        <input type="text" name="modifyDate" class="text"
                               value="${(purchase.purchaseDate)!}" readonly/><span id="checkMsg" style="color:red">
                    </td>
                </tr>
                <tr>
                    <th>
                        采购商：
                    </th>
                    <td>

                        <input type="text" name="modifyDate" class="text"
                               value="${(purchase.tenant.name)!}" readonly/><span id="checkMsg" style="color:red">
                    </td>
                </tr>
                <tr>
                    <th colspan="2" style="text-align: center;">
                        采购清单
                    </th>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="list" style="border-top:0;text-align: center">
                            <table class="list">
                                <tr>
                                    <th style=" width: 240px; text-align: center; ">商品名称</th>
                                    <th>商品编码</th>
                                    [#--<th style=" width: 240px; text-align: center; ">供应商</th>--]
                                    <th>采购数量</th>
                                    <th>规格</th>
                                    <th>型号</th>
                                    <th>备注</th>
                                </tr>
                            [#list purchase.purchaseItems as purchaseItem]
                                <tr>
                                    <td>${purchaseItem.name}</td>
                                    <td>${purchaseItem.sn}</td>
                                    [#--<td>${(purchase.supplier.name)!}</td>--]
                                    <td>${purchaseItem.quantity}</td>
                                    <td>${purchaseItem.spec}</td>
                                    <td>${purchaseItem.model}</td>
                                    <td>${purchaseItem.memo}</td>
                                </tr>
                            [/#list]
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th>
                        &nbsp;
                    </th>
                    <td>
                        <input type="button" class="button" value="${message("shop.common.back")}"
                               onclick="javascript:history.back();"/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>
