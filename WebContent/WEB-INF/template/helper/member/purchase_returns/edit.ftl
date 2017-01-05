<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="${base}/resources/common/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function () {
        [@flash_message /]
            $("#confirm").click(function () {
                $.dialog({
                    type: "success",
                    content: "您确定要提交申请么？",
                    onOk: function () {

                        var _productSn = "",_quartitys="";
                    [#list purchaseReturns.purchaseItems as purchaseReturnsItem]
                        _productSn +="sns=${purchaseReturnsItem.sn}&";
                        _quartitys +="quartitys=${purchaseReturnsItem.quantity}&";
                    [/#list]
                        if(_productSn!=""){
                            _productSn = _productSn.substring(0,_productSn.length-1);
                        }

                        $.ajax({
                            url: "${base}/helper/member/purchase/returns/update/${purchaseReturns.id}.jhtml?" +_quartitys+ _productSn,
                            type: "POST",
                            dataType: "json",
                            cache: false,
                            success: function (map) {
                                if (map.message.type == 'success') {
                                    location.href = "${base}/helper/member/purchase/returns/list.jhtml";
                                }
                            }
                        });
                    }
                });
                //alert(sn+"    "+modifyDate+"    "+$checkedIds.serialize()+"   "+supplierId+"   "+obj+"  "+checked_obj);
            });
        });
    </script>

</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">
            <!-- <div class="con-con"> -->
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">店铺资料</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">完善我的店铺资料。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" href="javascript:;">基本信息</a></li>
                </ul>

            </div>
            <div class="input">
                <table class="input">
                    <tr>
                        <th>
                            单据编号：
                        </th>
                        <td>
                            <input type="text" name="sn" class="text"
                                   value="${purchaseReturns.sn}" readonly/><span id="checkMsg" style="color:red">

                                <a href="${base}/helper/member/purchase/returns/print.jhtml?id=${purchaseReturns.id}" target="_blank" class="button" style="float: none;">
                                    打印
                                </a>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>退货时间：
                        </th>
                        <td>
                            <input type="text" name="modifyDate" class="text"
                                   value="${(purchaseReturns.purchaseDate)!}" readonly/><span id="checkMsg" style="color:red">
                        </td>
                    </tr>
                    <tr>
                        <th>
                            供应商：
                        </th>
                        <td>

                            <input type="text" name="modifyDate" class="text"
                                   value="${(purchaseReturns.supplier.name)!}" readonly/><span id="checkMsg" style="color:red">
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
                                        <th style=" width: 240px; text-align: center; ">供应商</th>
                                        <th>退货数量</th>
                                        <th>规格</th>
                                        <th>型号</th>
                                        <th>备注</th>
                                    </tr>
                                [#list purchaseReturns.purchaseItems as purchaseReturnsItem]
                                    <tr>
                                        <td>${purchaseReturnsItem.name}</td>
                                        <td>${purchaseReturnsItem.sn}</td>
                                        <td>${(purchaseReturns.supplier.name)!}</td>
                                        <td>${purchaseReturnsItem.quantity}</td>
                                        <td>${purchaseReturnsItem.spec}</td>
                                        <td>${purchaseReturnsItem.model}</td>
                                        <td>${purchaseReturnsItem.memo}</td>
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
                        [#if type=="applied"]
                            <input type="submit" class="button" id="confirm" value="审核"/>
                        [/#if]
                            <input type="button" class="button" value="${message("shop.common.back")}"
                                   onclick="javascript:history.back();"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
