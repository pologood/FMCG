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
            var teble = $("table[class='list']");
            $("select[name='supplierId']").change(function () {
                var supplierId = $("select[name='supplierId']").children('option:selected').val();
//                var keyword = $("input[name='keyword']").val().trim();
//                if (supplierId != "" || supplierId != null) {
//                    getSupplier(supplierId, keyword);
//                }

                if(supplierId==""){
                    $("#iframe_id").attr("src","");
                    $("#iframe_id").attr("height","10px;");
                }else{
                    $("#iframe_id").attr("src","${base}/helper/member/purchase/returns/supplier.jhtml?id="+supplierId);
                    $("#iframe_id").attr("height","1232px;");
                }
            });

            $("#search").click(function () {
                var supplierId = $("select[name='supplierId']").children('option:selected').val();
                var keyword = $("input[name='keyword']").val().trim();

                if (supplierId == "" || supplierId == null) {
                    $.message("warn", "对不起，请先选择供应商！");
                    return;
                }
                if (keyword == "" || keyword == null) {
                    $.message("warn", "请输入你希望搜索到的商品信息！");
                    return;
                }
                $(document.getElementById("iframe_id").contentWindow.document.body).find("#keywords").val(keyword);
                $(document.getElementById("iframe_id").contentWindow.document.body).find("#listForm").submit();
                //getSupplier(supplierId, keyword);
            });

            [#--function getSupplier(supplierId, keyword) {--]
                [#--$.ajax({--]
                    [#--url: "${base}/helper/member/purchase/returns/supplier.jhtml",--]
                    [#--data: {--]
                        [#--id: supplierId,--]
                        [#--keyword: keyword--]
                    [#--},--]
                    [#--type: "POST",--]
                    [#--dataType: "json",--]
                    [#--cache: false,--]
                    [#--success: function (map) {--]
                        [#--if (map.message.type == "success") {--]
                            [#--var data = map.data;--]
                            [#--var html = "<tr>" +--]
                                    [#--"<th class='check'><input type='checkbox' id='selectAll'></th>" +--]
                                    [#--"<th style='width: 260px; text-align: left; '>商品名称</th><th>规格</th><th>型号</th><th>商品编码</th><th>单位</th><th>退货数量</th><th>备注</th></tr>";--]
                            [#--if (data.length > 0) {--]
                                [#--for (var i = 0; i < data.length; i++) {--]
                                    [#--html += "<tr>" +--]
                                            [#--"<td><input type='checkbox' name='ids' value='"+data[i].id+"'></td>" +--]
                                            [#--"<td style='text-align: left'>" + data[i].fullName + "</td>" +--]
                                            [#--"<td>" + data[i].spec + "</td>" +--]
                                            [#--"<td>" + data[i].color + "</td>" +--]
                                            [#--"<td>" + data[i].sn + "</td>" +--]
                                            [#--"<td>" + data[i].unit + "</td>" +--]
                                            [#--"<td><input type='text' name='stock"+data[i].id+"'   value='" + data[i].stock + "' style='width: 80px;'></td>" +--]
                                            [#--"<td><input type='text' name='content"+data[i].id+"'  style='width: 80px;'></td>" +--]
                                            [#--"</tr>";--]
                                [#--}--]
                                [#--teble.html(html);--]
                            [#--}--]
                        [#--}--]
                    [#--}--]
                [#--});--]
            [#--}--]

            [#--$(".list").on("click","input[id='selectAll']",function(){--]
                [#--var $this = $(this);--]
                [#--var $enabledIds = $(".list input[name='ids']:enabled");--]
                [#--if ($this.prop("checked")) {--]
                    [#--$enabledIds.prop("checked", true);--]
                [#--} else {--]
                    [#--$enabledIds.prop("checked", false);--]
                [#--}--]
            [#--});--]

            $("#confirm").click(function(){
                var $iframe=$(document.getElementById("iframe_id").contentWindow.document.body);
                var sn = $("input[name='sn']").val();
                var modifyDate = $("input[name='modifyDate']").val();
                var supplierId = $("select[name='supplierId']").children('option:selected').val();
                var $checkedIds = $iframe.find(".list input[name='ids']:enabled").filter(":enabled:checked");
                var obj=$iframe.find(".list input[name='ids']");
                var checked_obj="",content_obj="";
                for (var i = 0; i < obj.length; i++) {
                    if (obj[i].checked == true) {
                        checked_obj+="quartitys="+$iframe.find("input[name='stock"+obj[i].value+"']").val()+"&";
                        content_obj+="memos="+$iframe.find("input[name='content"+obj[i].value+"']").val()+"&";
                    }
                }

                if(supplierId==""||supplierId==null){
                    $.message("warn","请选择供应商!");
                    return;
                }

                if(checked_obj==""){
                    $.message("warn","请选择需要采购的商品！");
                    return;
                }

                $.ajax({
                    url: "${base}/helper/member/purchase/returns/save.jhtml?"+checked_obj+content_obj+$checkedIds.serialize(),
                    data: {
                        sn:sn,
                        supplierId: supplierId,
                        purchaseDate:modifyDate
                    },
                    type: "POST",
                    dataType: "json",
                    cache: false,
                    success: function (map) {
                        if(map.message.type=='success'){
                            location.href = "${base}/helper/member/purchase/returns/list.jhtml";
                        }
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
                                   value="${code}" readonly/><span id="checkMsg" style="color:red">

                                &nbsp;&nbsp;
                                [#--<input type="submit" class="button" onclick="window.print();" value="打印"/>--]
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>退货时间：
                        </th>
                        <td>
                            <input type="text" name="modifyDate" class="text"
                                   value="${(time)!}" readonly/><span id="checkMsg" style="color:red">
                        </td>
                    </tr>
                    <tr>
                        <th>
                            供应商：
                        </th>
                        <td>
                            <select name="supplierId">
                                <option value="">${message("shop.common.choose")}</option>
                            [#list suppliers as supplier]
                                <option value="${supplier.parent.id}">${supplier.parent.name}</option>
                            [/#list]
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th colspan="2" style="text-align: center;">
                            采购清单
                        </th>
                    </tr>
                    <tr>
                        <th>
                            查找商品：
                        </th>
                        <td>
                            <input type="text" name="keyword" class="text"/><span id="checkMsg" style="color:red">
                                &nbsp;&nbsp;
                                <input type="submit" class="button" id="search" value="查询"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="list" style="border-top:0;text-align: center">
                                <iframe src=""  frameborder="0" scrolling="no" width="100%" height="" id="iframe_id">
                                    <!-- <form action="">
                                        <table class="list">
                                        </table>
                                    </form> -->
                                </iframe>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td>
                            <input type="submit" class="button" id="confirm" value="确定"/>
                            <input type="button" class="button" value="${message("shop.common.back")}" onclick="javascript:history.back();"/>
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
