<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-企业认证</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2c/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2c/js/menuswitch.js"></script>
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
        <ul class="breadcrumb">当前位置：
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>数据统计</li>
            <li>销售统计</li>
        </ul>
    </div>
[#include "/b2c/include/supplier_top.ftl"]
    <div class="tb-container" style="border:none;">
        <div class="js_bill" style="border-top:none;border-bottom:1px solid #cccccc;">
            <ul>
                <li><a href="supplierCertification.jhtml">企业认证</a></li>
                <li><a href="supplierCertificationR.jhtml" class="active">厂商授权</a></li>
            </ul>
        </div>
        <div class="order-form">
            <!-- <p class="order-title">
            </p> -->
            <form action="#" class="">
                <table class="fm-basic">
                    <tr>
                        <th class="order-title" style="color:#999;">温馨提示：</th>
                        <th class="order-title">请上传您与厂家签订的授权或代理的合同/协议，或厂家办法给您的合法有效的授权证书，并等待后台的审核通过。</th>
                    </tr>
                    <tr>
                        <th>厂商授权：</th>
                        <th><input type="text" class="wd320 inwarp"/></th>
                    </tr>
                    <tr>
                        <th></th>
                        <th>
                            <div class="fl-wrap">
                                <div class="left">
                                    <p></p>
                                </div>
                                <div class="right">
                                    <div id="file" style="margin-top:75px;">
                                        <input type="file" class="file" multiple="multiple"
                                               accept="image/png,image/gif"/>
                                        <p>上传图片请控制在2MB以内，支持格式为bmp\jpg\png</p>
                                    </div>
                                </div>
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <th>&nbsp;</th>
                        <th>
                            <a href="#" class="bg-orgn btn-accredit">确定授权</a>
                            <a href="#" class="bg-gray btn-accredit">取消授权</a>
                        </th>
                    </tr>
                </table>
            </form>

        </div>
    </div>

</div>

</body>
</html>
