<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.index.title")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .input .powered {
            font-size: 11px;
            text-align: right;
            color: #cccccc;
        }
    </style>
</head>
<body>
<div class="path">
${message("admin.index.title")}
</div>
<table class="input">
    <tr>
        <th>
        ${message("admin.index.systemName")}:
        </th>
        <td>
        ${systemName}
        </td>
        <th>
        ${message("admin.index.systemVersion")}:
        </th>
        <td>
        ${systemVersion}
        </td>
    </tr>
    <tr>
        <td colspan="4">
            &nbsp;
        </td>
    </tr>
    <tr>
        <th>
        ${message("admin.index.javaVersion")}:
        </th>
        <td>
        ${javaVersion}
        </td>
        <th>
        ${message("admin.index.javaHome")}:
        </th>
        <td>
        ${javaHome}
        </td>
    </tr>
    <tr>
        <th>
        ${message("admin.index.osName")}:
        </th>
        <td>
        ${osName}
        </td>
        <th>
        ${message("admin.index.osArch")}:
        </th>
        <td>
        ${osArch}
        </td>
    </tr>
    <tr>
        <th>
        ${message("admin.index.serverInfo")}:
        </th>
        <td>
        ${serverInfo}
        </td>
        <th>
        ${message("admin.index.servletVersion")}:
        </th>
        <td>
        ${servletVersion}
        </td>
    </tr>
    <tr>
        <td colspan="4">
            &nbsp;
        </td>
    </tr>
    <tr>
        <th>
        ${message("admin.index.marketableProductCount")}:
        </th>
        <td>
            <a href="${base}/admin/tenant/report_list.jhtml">
                ${marketableProductCount}
            </a>
        </td>
        <th>
        ${message("admin.index.notMarketableProductCount")}:
        </th>
        <td>
            <a href="${base}/admin/tenant/report_list.jhtml">
                ${notMarketableProductCount}
            </a>
        </td>
    </tr>
    [#if versionType==0]
    <tr>
        <th>
        ${message("admin.index.stockAlertProductCount")}:
        </th>
        <td>
        ${stockAlertProductCount}
        </td>
        <th>
        ${message("admin.index.outOfStockProductCount")}:
        </th>
        <td>
        ${outOfStockProductCount}
        </td>
    </tr>
    <tr>
        <th>
        ${message("admin.index.waitingPaymentOrderCount")}:
        </th>
        <td>
        ${waitingPaymentOrderCount}
        </td>
        <th>
        ${message("admin.index.waitingShippingOrderCount")}:
        </th>
        <td>
        ${waitingShippingOrderCount}
        </td>
    </tr>
    [/#if]
    <tr>
        <th>
        ${message("admin.index.memberCount")}:
        </th>
        <td>
        ${memberCount}
        </td>
        <th>
        ${message("admin.index.unreadMessageCount")}:
        </th>
        <td>
        ${unreadMessageCount}
        </td>
    </tr>

    <tr>
        <td colspan="4">
            &nbsp;
        </td>
    </tr>

    <tr>
        <th>
            用户余额:
        </th>
        <td>
            <a href="${base}/admin/member/list.jhtml">${amounts.balance}</a>
        </td>
        <th>
            用户冻结金额:
        </th>
        <td>
            <a href="${base}/admin/member/list.jhtml">${amounts.freezeBalance}</a>
        </td>
    </tr>

    <tr>
        <th>
            库存金额:
        </th>
        <td>
        <a href="${base}/admin/member/list.jhtml">${amounts.stockAmount}</a>
        </td>
        <th>
            分润金额:
        </th>
        <td>
        <a href="${base}/admin/member/list.jhtml"> ${amounts.amount}</a>
        </td>
    </tr>
    [#if versionType==0]
    <tr>
        <th>
            导购分润:
        </th>
        <td>
        ${amounts.guideAmount}
        </td>
        <th>
            店主分润:
        </th>
        <td>
        ${amounts.guideOwnerAmount}
        </td>
    </tr>

    <tr>
        <th>
            推广分润:
        </th>
        <td>
        ${amounts.shareAmount}
        </td>
        <th>
            推广店主分润:
        </th>
        <td>
        ${amounts.shareOwnerAmount}
        </td>
    </tr>

    <tr>
        <th>
            平台:
        </th>
        <td>
        ${amounts.providerAmount}
        </td>
        <th>
            &nbsp;
        </th>
        <td>
            &nbsp;
        </td>
    </tr>
    [/#if]
    <tr>
        <td class="powered" colspan="4">
            COPYRIGHT © 2013-2015 ${setting.siteName} ALL RIGHTS RESERVED.
        </td>
    </tr>
</table>
</body>
</html>