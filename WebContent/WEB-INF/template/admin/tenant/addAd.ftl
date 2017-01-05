<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.article.edit")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            //文件上传
            var settings = {};
            $("#browserButton").browser(settings);
            // 表单验证
            $("#inputForm").validate({
                rules: {
                    title: "required",
                    adPositionId: "required",
                    path: "required",
                    file: "required",
                    order: "digits"
                },
                messages: {
                    title: "${message("admin.validate.required")}",
                    adPositionId: "${message("admin.validate.required")}",
                    file: "${message("admin.validate.required")}",
                    order: "序号如：1、2、3..."
                }
            });
        });
        function get_product_id(obj){
        [#if versionType==0]
            $("#productUrl").val("${base}/b2c/product/detail/"+$(obj).val()+".jhtml");
        [#elseif versionType==1]
            $("#productUrl").val("${base}/b2b/product/detail/"+$(obj).val()+".jhtml");
        [/#if]
        }
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 商家档案
</div>
<form id="inputForm" action="saveAd.jhtml" method="post" enctype="multipart/form-data">
    <input type="hidden" id="type" name="type" value="image"/>
    <input type="hidden" name = "tenantId" value="${tenant.id}"/>
    <table class="input tabContent">
        <tr>
            <th>
                <span class="requiredField">*</span>标题：
            </th>
            <td>
                <input type="text" name="title" class="text" />
            </td>
        </tr>
        <tr>
            <th>
                广告位：
            </th>
            <td>
                <select id="adPositionId" name="adPositionId">
                [#list adPositions as adPosition]
                    [#if adPosition!=null]
                        <option value="${adPosition.id}">
                        ${adPosition.name} [${adPosition.width}× ${adPosition.height}]
                        </option>
                    [/#if]
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                广告类型：
            </th>
            <td>
                <select id="linkType" name="linkType">
                [#list linkTypes as linkType]
                    <option value="${linkType}"[#if linkType_index == 0]selected="selected"[/#if]>
                    ${message("Ad.LinkType." + linkType)}
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
               路径：
            </th>
            <td>
	            <span class="fieldSet">
                    <input type="text" name="path" class="text" readonly="true"/>
			        <input type="button" id="browserButton" class="button" value="${message("admin.browser.select")}"/>
                </span>
            </td>
        </tr>
        <tr>
            <th>链接地址：</th>
            <td>
                <input type="text" class="text" name="url" id="productUrl" readonly="true"/>
                <select class="form-control" name="linkId" onchange="get_product_id(this)">
                    <option value=''>请选择商品</option>
                [#if products??]
                    [#list products as product]
                        <option value='${product.id}' title="${product.name}">${product.name}</option>
                    [/#list]
                [/#if]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                <span></span>排序:
            </th>
            <td>
                <input type="text" name="order" class="text"/>
            </td>
        </tr>
        <tr>
            <th>&nbsp;</th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>