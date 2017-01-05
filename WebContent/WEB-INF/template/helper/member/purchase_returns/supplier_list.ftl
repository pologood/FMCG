<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/common/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            $(".list").on("click","input[id='selectAll']",function(){
                var $this = $(this);
                var $enabledIds = $(".list input[name='ids']:enabled");
                if ($this.prop("checked")) {
                    $enabledIds.prop("checked", true);
                } else {
                    $enabledIds.prop("checked", false);
                }
            });
        });
        function compare_stock(obj) {
            if(parseInt($(obj).val())>parseInt($(obj).attr("stock"))){
                $(obj).val($(obj).attr("stock"));
                $.message("warn","库存不足");
            }
            if(parseInt($(obj).val())<0){
                $(obj).val(0);
                $.message("warn","退货数量不能小于零");
            }
        }
    </script>

</head>
<body>
    <form id="listForm" action="supplier.jhtml" method="get">
        <input type="hidden" name="id" value="${supplierId}">
        <input type="hidden" name="keyword" value="${keywords}" id="keywords">
        <table class="list">
            <tr>
                <th class='check'><input type='checkbox' id='selectAll' ></th> 
                <th style='width: 300px; text-align: left; '>商品名称</th>
                <th>规格/型号</th>
                [#--<th>商品编码</th>--]
                <th>单位</th>
                <th>采购退货数量</th>
                <th>库存</th>
                <th>备注</th>
            </tr>
            [#list page.content as products]
            <tr>
                <td><input type='checkbox' name='ids' value='${products.id}'></td>
                <td>${products.fullName}</td>
                <td>
                    [#list products.specificationValues as spec]
                    ${spec.name}&nbsp;
                    [/#list]
                </td>
                [#--<td>${products.sn}</td>--]
                <td>${products.unit}</td>
                <td><input type='text' name='stock${products.id}' value='' style='width: 80px;' stock="${products.availableStock}" flag="stock" onkeyup="compare_stock(this)"></td>
                <td>${products.availableStock}</td>
                <td><input type='text' name='content${products.id}' value='' style='width: 80px;'></td>
            </tr>
            [/#list]
        </table>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
            [#include "/helper/include/pagination.ftl"]
        [/@pagination]
    </form>                     
</body>
</html>
