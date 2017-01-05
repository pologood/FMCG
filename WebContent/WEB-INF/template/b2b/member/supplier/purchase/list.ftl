<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-销售明细</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/Count_Down.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/foundation-datepicker.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/foundation-datepicker.zh-CN.js"></script>

    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>

<body class="bg-base">
[#include "/b2b/include/supplier_left.ftl"]

<form id="listForm" action="${base}/b2b/member/supplier/purchase/list.jhtml" method="get">
    <input id="dateRange" name="dateRange"  type="hidden"/>
    <div class="f-left rt">
    [#include "/b2b/include/supplier_header.ftl"]
        <div class="breadcrumbs">
            <ul class="breadcrumb">
                <p>当前位置：</p>
                <li>供货商管理后台</li>
                <li><a href="#">首页</a></li>
                <li>数据统计</li>
                <li>销售明细</li>
            </ul>
        </div>
    [#include "/b2b/include/supplier_top.ftl"]
    [#include "/b2b/include/supplier_date.ftl"]
        <div class="tb-container">
            <table class="product-items">
                <thead>
                <tr>
                    <th></th>
                    <th>单据编号</th>
                    <th>采购商</th>
                    <th>单据状态</th>
                    <th>创建日期</th>
                    <th>操作</th>
                </tr>
                </thead>
            [#list page.content as purchase]
                <tr>
                    <th>${(page.pageNumber-1)*page.pageSize+purchase_index+1}</th>
                    <th>${purchase.sn}</th>
                    <th title="${(purchase.tenant.name)!}">${(purchase.tenant.name)!}</th>
                    <th>${message("Purchaase.Type."+purchase.type)}</th>
                    <th>
                        <span title="${purchase.purchaseDate?string("yyyy-MM-dd HH:mm:ss")}">${purchase.purchaseDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                    </th>
                    <th>
                        [#--[#if purchase.type=='applied']--]
                            [#--<a href="${base}/b2b/member/purchase/edit/${purchase.id}.jhtml?type=${purchase.type}"--]
                               [#--style="color: red;">[审核]</a>--]
                        [#--[#else]--]
                            <a href="${base}/b2b/member/supplier/purchase/edit/${purchase.id}.jhtml?type=${purchase.type}">[查看]</a>
                        [#--[/#if]--]
                    </th>
                </tr>
            [/#list]
            </table>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
            [#include "/b2b/include/pagination1.ftl"]
        [/@pagination]
            <!-- ul class="sell-container">
                <li>累计销售额： ￥${total_sale_amount}</li>
                <li>累计销售毛利： ￥${sale_gross_profit}</li>
            </ul -->
        </div>
    </div>
</form>
</body>

</html>
