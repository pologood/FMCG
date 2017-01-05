<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="" content=""/>
    <title>${setting.siteName}-供应商-企业认证</title>
    <meta name="keywords" content="${setting.siteName}-首页"/>
    <meta name="description" content="${setting.siteName}-首页"/>
    <script type="text/javascript" src="${base}/resources/b2b/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${base}/resources/b2b/js/menuswitch.js"></script>
    <link rel="icon" href="${base}/favicon.ico" type="image/x-icon"/>
    <link href="${base}/resources/b2b/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/supplier.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/css/twoCategory.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/b2b/fonts/iconfont.css" type="text/css" rel="stylesheet"/>
</head>
<body class="bg-base">
[#include "/b2b/include/supplier_left.ftl"]

<div class="f-left rt">
[#include "/b2b/include/supplier_header.ftl"]
    <div class="breadcrumbs">
        <ul class="breadcrumb">当前位置：
            <li>供货商管理后台</li>
            <li><a href="#">首页</a></li>
            <li>数据统计</li>
            <li>销售统计</li>
        </ul>
    </div>
[#include "/b2b/include/supplier_top.ftl"]
    <div class="tb-container" style="border:none;">
        <div class="js_bill" style="border-top:none;border-bottom:1px solid #cccccc;">
            <ul>
                <li><a href="supplierCertification.jhtml" class="active">企业认证</a></li>
                <li><a href="supplierCertificationR.jhtml">厂商授权</a></li>
            </ul>
        </div>
        <div class="order-form">
            <!-- <p class="order-title">
            </p> -->
            <form action="#" class="">
                <table class="fm-basic">
                    <tr>
                        <th class="order-title" style="color:#999;">温馨提示：</th>
                        <th class="order-title">请按照提示填写企业的真实材料！</th>
                    </tr>
                    <tr>
                        <th>用户账号：</th>
                        <th>15856969651</th>
                    </tr>
                    <tr>
                        <th><span>*</span>公司名称：</th>
                        <th><input type="text" class="wd320 inwarp"/></th>
                    </tr>
                    <tr>
                        <th><span>*</span>营业执照：</th>
                        <th>
                            <div class="fl-wrap">
                                <div class="left">
                                    <p>工商注册号：</p>
                                </div>
                                <div class="right">
                                    <input type="text" class="inwarp"/>
                                    <div id="file">
                                        <input type="file" class="file" multiple="multiple"
                                               accept="image/png,image/gif"/>
                                        <p>上传图片请控制在2MB以内，支持格式为bmp\jpg\png</p>
                                    </div>
                                </div>
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <th><span>*</span>法人代表：</th>
                        <th>
                            <div class="fl-wrap">
                                <div class="left">
                                    <p>其他证件：</p>
                                </div>
                                <div class="right">
                                    <input type="text" class="inwarp"/>
                                    <div id="file">
                                        <input type="file" class="file" multiple="multiple"
                                               accept="image/png,image/gif"/>
                                        <p>上传图片请控制在2MB以内，支持格式为bmp\jpg\png</p>
                                    </div>
                                </div>
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <th><span>*</span>经营地址：</th>
                        <th>
                            <select name="" id="" class="inwarp">
                                <option value="">请选择</option>
                            </select>
                            <select name="" id="" class="inwarp">
                                <option value="">请选择</option>
                            </select>
                            <select name="" id="" class="inwarp">
                                <option value="">请选择</option>
                            </select>
                        </th>
                    </tr>
                    <tr>
                        <th><span>*</span>经营范围：</th>
                        <th>
                            <select name="" id="" class="inwarp">
                                <option value="">请选择</option>
                            </select>
                        </th>
                    </tr>
                    <tr>
                        <th>街道：</th>
                        <th><input type="text" class="wd500 inwarp"/></th>
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
