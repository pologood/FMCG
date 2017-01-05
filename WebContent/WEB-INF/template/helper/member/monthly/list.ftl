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
  <style type="text/css">
    #listTable th{
      text-align: center;
    }
    #listTable td{
      text-align: center;
    }
  </style>
  <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
  <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
  <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
  <script src="${base}/resources/helper/js/amazeui.min.js"></script>
  <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
  <script type="text/javascript">
    $().ready(function () {
      $("#monthly").click(function(){
        $.dialog({
          title: "月末结账",
          [@compress single_line = true]
          content: '<table class= "input" style = "margin-bottom: 30px;">'+
          '<tr><th>余额:</th><td>'+[#if tenant??]${tenant.member.balance}[#else]0.00[/#if]+'</td></tr>'+
          '<tr><th>冻结余额:</th><td>'+[#if tenant??]${tenant.member.freezeBalance}[#else]0.00[/#if]+'</td></tr>'+
          '<tr><th>待发货余额:</th><td>'+[#if amount??]${amount}[#else]0.00[/#if]+'</td></tr>'+
          '<tr><th>库存:</th><td>'+${stock}+'</td></tr>'+
          '<tr><th>锁定库存:</th><td>'+${lockStock}+'</td></tr>'+
          '<tr><th>库存金额:</th><td>'+[#if StockAmount??]${StockAmount?string("0.00")}[#else]0.00[/#if]+'</td></tr>'+
          '</table>',
          [/@compress]
          width: 600,
          modal: true,
          ok: "${message("admin.dialog.ok")}",
          cancel: "${message("admin.dialog.cancel")}",
          onOk: function () {
            $.ajax({
              url:"confirm_monthly.jhtml",
              type:"get",
              dataType:"json",
              success:function(message){
                if(message.type=="success"){
                  location.reload();
                }else{
                  $.message(message);
                }
              }
            });
          }
        });
});
})
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
        <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
          <div class="js-app-header title-wrap" id="app_0000000844">
            <img class="js-app_logo app-img" src="${base}/resources/b2b/images/v2.0/shop-data.png"/>
            <dl class="app-info">
              <dt class="app-title" id="app_name">月末结账</dt>
              <dd class="app-status" id="app_add_status">
              </dd>
              <dd class="app-intro" id="app_desc">装修我的店铺，选择模版。</dd>
            </dl>
          </div>
        </div>

        <form id="listForm" action="list.jhtml" method="get">
          <div class="bar">
            <div class="buttonWrap">
              <a href="javascript:;" class="button" id="monthly"> 结帐</a>
              <a style="cursor: pointer;" id="deleteButton" class="iconButton disabled">
                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
              </a>
              <a href="${base}/helper/member/monthly/list.jhtml" class="iconButton">
                <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
              </a>
              <div class="menuWrap">
                <a style="cursor: pointer;" id="pageSizeSelect" class="button">
                  ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                </a>
                <div class="popupMenu">
                  <ul id="pageSizeOption">
                    <li>
                      <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                    </li>
                    <li>
                      <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                    </li>
                    <li>
                      <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                    </li>
                    <li>
                      <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
                    </li>
                  </ul>
                </div>
              </div>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <input type="text" id="startDate" name="startDate" value="${begin_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="开始时间" style="width:150px;"/>
              <input type="text" id="endDate" name="endDate" value="${end_date}" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd'});" placeholder="结束时间" style="width:150px;" />
              <input type="submit" style="height:26px;" />
            </div>
          </div>
          <div class="list">
            <table id="listTable" class="list">
              <tr>
                <th class="check"><input type="checkbox" id="selectAll"/></th>
                <th>创建时间</th>
                <th>商家</th>
                <th>商家余额</th>
                <th>商家冻结金额</th>
                <th>待发货冻结金额</th>
                <th>库存</th>
                <th>冻结库存</th>
                <th>库存金额</th>
                
              </tr>
              [#list page.content as monthlys]
              <tr>
                <td><input type="checkbox" name="ids" value="${monthlys.id}"/></td>
                <td>${monthlys.createDate}</td>
                <td>${monthlys.tenant.name}</td>
                <td>${monthlys.balance}</td>
                <td>${monthlys.freezeBalance}</td>
                <td>${monthlys.unShippingBalance}</td>
                <td>${monthlys.stock}</td>
                <td>${monthlys.lockStock}</td>
                <td>${monthlys.stockAmount}</td>
              </tr>
              [/#list]
            </table>
          </div>
          [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
          [#include "/helper/member/include/pagination.ftl"]
          [/@pagination]
        </form>

      </div>
    </div>
  </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>