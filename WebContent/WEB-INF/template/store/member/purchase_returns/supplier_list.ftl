<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  [#include "/store/member/include/bootstrap_css.ftl"]
  <link rel="stylesheet" href="${base}/resources/store/css/style.css">
  <link rel="stylesheet" href="${base}/resources/store/css/common.css" />
</head>
<body>
  <form id="listForm" action="supplier.jhtml" method="get">
    <input type="hidden" name="id" value="${supplierId}">
    <input type="hidden" name="keywords" value="${keywords}" id="keywords">
    <table class="table table-bordered table-hover list">
      <tr>
        <th class='check'><input type='checkbox' id='selectAll' ></th> 
        <th style='width: 260px; text-align: left;'>商品名称</th>
        <th>规格/型号</th>
        <th>单位</th>
        <th>采购数量</th>
        <th>库存</th>
        <th>备注</th>
      </tr>
      [#list page.content as products]
      <tr>
        <td><input type='checkbox' name='ids' value='${products.id}'></td>
        <td style='width: 260px; text-align: left;white-space:nowrap; overflow:hidden;text-overflow:ellipsis;' title="${products.fullName}">${products.fullName}</td>
        <td>
          [#list products.specificationValues as spec]
          ${spec.name}&nbsp;
          [/#list]
        </td>
        <td>${products.unit}</td>
        <td><input type='text' name='stock${products.id}' value='' style='width: 80px;'></td>
        <td>${products.availableStock}</td>
        <td><input type='text' name='content${products.id}' value='' style='width: 80px;'></td>
      </tr>
      [/#list]
    </table>
    <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
      [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
        [#include "/store/member/include/pagination.ftl"]
      [/@pagination]
    </div>
  </form>     
  [#include "/store/member/include/bootstrap_js.ftl"]
  <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
  <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
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
    </script>
</body>
</html>
