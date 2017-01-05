[#-- @ftlroot "../../../" --]
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
    <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/account.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/store/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/store/css/style.css">

    <script src="${base}/resources/store/2.0/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <script src="${base}/resources/store/2.0/bootstrap/js/bootstrap.min.js"></script>
    <script src="${base}/resources/store/2.0/dist/js/app.min.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/store/js/list.js"></script>
    <script src="${base}/resources/store/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript"
            src="${base}/resources/store/2.0/plugins/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="${base}/resources/store/2.0/plugins/datatables/dataTables.bootstrap.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
//            $("#listTable").DataTable();
            var $listForm = $("#listForm");
            var $moreButton = $("#moreButton");
            var $selectAll = $("#selectAll");
            var $ids = $("#listTable input[name='ids']");
            var $upMarketButton = $("#upMarketButton");
            var $deleteButton = $("#deleteButton");
            var $changePriceButton = $("#changePriceButton");
            $deleteButton.addClass("disabled");
            $upMarketButton.addClass("disabled");
            $changePriceButton.addClass("disabled");

        [@flash_message /]

            // 更多选项
            $moreButton.click(function () {
                $.dialog({
                    title: "${message("admin.product.moreOption")}",
                [@compress single_line = true]
                    content: '<table id="moreTable" class="input"><tr><th>${message("Product.productCategory")}:<\/th><td><select name="productCategoryId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list productCategoryTree as productCategory]
                        '<option value="${productCategory.id}"[#if productCategory.id == productCategoryId] selected="selected"[/#if]>'+
                            [#if productCategory.grade != 0]
                                [#list 1..productCategory.grade as i]
                                '&nbsp;&nbsp;'+
                                [/#list]
                            [/#if]
                        '${productCategory.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.brand")}:<\/th><td><select name="brandId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list brands as brand]
                        '<option value="${brand.id}"[#if brand.id == brandId] selected="selected"[/#if]>${brand.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.promotions")}:<\/th><td><select name="promotionId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list promotions as promotion]
                        '<option value="${promotion.id}"[#if promotion.id == promotionId] selected="selected"[/#if]>${promotion.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.tags")}:<\/th><td><select name="tagId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list tags as tag]
                        '<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>${tag.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr>' +
                        [#if versionType==1]
                        '<tr><th>供应商:<\/th><td><select name="supplierId"><option value="">${message("admin.common.choose")}<\/option>' +
                            [#list suppliers as supplier]
                            '<option value="${supplier.parent.id}"[#if supplier.parent.id == supplierId] selected="selected"[/#if]>${supplier.parent.name}<\/option>'+
                            [/#list]
                        '<\/select><\/td><\/tr>' +
                        [/#if]
                    '<\/table>',
                [/@compress]
                    width: 470,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function () {
                        $("#moreTable :input").each(function () {
                            var $this = $(this);
                            $("#" + $this.attr("name")).val($this.val());
                        });
                        $listForm.submit();
                    }
                });
            });

            //上架
            $(".upMarketable").click(function () {
                if (confirm("确认上架？")) {
                    $.ajax({
                        url: "upMarketable.jhtml",
                        data: {id: $(this).attr("productId")},
                        dataType: "json",
                        type: "get",
                        success: function (message) {
                            alert(message.content);
                            location.reload();
                        }
                    });
                }
            });

            /**
             * 商品批量上架
             */
            $upMarketButton.click(function () {
                var $this = $(this);
                if ($this.hasClass("disabled")) {
                    return false;
                }
                var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
                $.dialog({
                    type: "warn",
                    content: '<div style="padding-right: 40px;"><h2 style="font-size: 16px;font-weight: 400;color: #000;">您确定要上架选中的商品么？</h2></div>',
                    width: 360,
                    onOk: function () {
                        $.ajax({
                            url: "upMarketables.jhtml",
                            data: $checkedIds.serialize(),
                            dataType: "json",
                            type: "get",
                            success: function (message) {
                                if (message.type == "success") {
                                    $.message("success", message.content);
                                    window.setTimeout(function () {
                                        window.location.reload();
                                    }, 600);
                                }
                                $upMarketButton.addClass("disabled");
                                $selectAll.prop("checked", false);
                                $checkedIds.prop("checked", false);
                            }
                        });
                    }
                });
            });

            // 全选
            $selectAll.click(function () {
                var $this = $(this);
                var $enabledIds = $("#listTable input[name='ids']:enabled");
                if ($this.prop("checked")) {
                    $enabledIds.prop("checked", true);
                    if ($enabledIds.filter(":checked").size() > 0) {
                        $deleteButton.removeClass("disabled");
                        $upMarketButton.removeClass("disabled");
                        $changePriceButton.removeClass("disabled");
                    } else {
                        $deleteButton.addClass("disabled");
                        $upMarketButton.addClass("disabled");
                        $changePriceButton.addClass("disabled");
                    }
                } else {
                    $enabledIds.prop("checked", false);
                    $deleteButton.addClass("disabled");
                    $upMarketButton.addClass("disabled");
                    $changePriceButton.addClass("disabled");
                }
            });

            // 选择
            $ids.click(function () {
                var $this = $(this);
                if ($this.prop("checked")) {
                    $this.closest("tr").addClass("selected");
                    $deleteButton.removeClass("disabled");
                    $upMarketButton.removeClass("disabled");
                    $changePriceButton.removeClass("disabled");
                } else {
                    $this.closest("tr").removeClass("selected");
                    if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
                        $deleteButton.removeClass("disabled");
                        $upMarketButton.removeClass("disabled");
                        $changePriceButton.removeClass("disabled");
                    } else {
                        $deleteButton.addClass("disabled");
                        $upMarketButton.addClass("disabled");
                        $changePriceButton.addClass("disabled");
                    }
                }
            });

            $("body").on("change", "#priceForm #checkAll", function () {
                $("#priceForm input[name='ids']").prop("checked", $("#priceForm #checkAll").prop("checked"));
            }).on("change", "#priceForm input[name='ids']", function () {
                var allChecked = true;
                $.each($("#priceForm input[name='ids']"), function () {
                    if (allChecked) {
                        if (!$(this).prop("checked")) {
                            allChecked = false;
                        }
                    }
                });
                $("#priceForm #checkAll").prop("checked", allChecked);
            });

            $changePriceButton.click(function () {
                var $this = $(this);
                if ($this.hasClass("disabled")) {
                    return false;
                }
                var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
                $.ajax({
                    url: 'view.jhtml',
                    type: 'get',
                    data: $checkedIds.serialize(),
                    dataType: 'json',
                    success: function (data) {
                        if (data.message.type == "success") {
                            var content = '<div style="height: 350px;overflow-y: auto;">' +
                                    '<form id="priceForm"><table class="list" style="table-layout: fixed;">' +
                                    '<thead>' +
                                    '<tr>' +
                                    '<th width="180" style="padding-left:5px"><input type="checkbox" id="checkAll"/><a href="javascript:;">名称</a></th>' +
                                    '<th><a href="javascript:;">销售价</a></th>' +
                                    [#list memberRanks as memberRank]
                                    '<th><a href="javascript:;">${memberRank.name}</a></th>' +
                                    [/#list]
                                    '</tr>' +
                                    '</thead>' +
                                    '<tbody>';
                            $.each(data.data, function (i, products) {
                                $.each(products, function (index, product) {
                                    content += '<tr class="productTr">' +
                                            '<td style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;';
                                    if (index != 0) {
                                        content += 'padding-left: 25px;';
                                    }
                                    content += '"><input type="checkbox" name="ids" value="' + product.id + '" />' + product.fullName + '</td>' +
                                            '<td><input marketPrice="' + product.marketPrice + '" name="price_' + product.id + '" class="text" value="' + product.price + '" type="text" style="width: 100px;"/></td>' +
                                            [#list memberRanks as memberRank]
                                            '<td><input type="text" class="text memberPrice" memberRankName="${memberRank.name}" memberRankId="${memberRank.id}" name="memberPrice_' + product.id + '_${memberRank.id}" value="'+ (product.memberPrice.memberPrice_${memberRank.id} == null ? '' : product.memberPrice.memberPrice_${memberRank.id}) + '" style="width: 100px;"/></td>' +
                                            [/#list]
                                            '<tr/>';
                                });
                            });
                            content += '<tr>' +
                                    '<td style="text-align: center;">统一价格：</td>' +
                                    '<td><input name="unitPrice" class="text" type="text" style="width: 100px;"/></td>' +
                                    [#list memberRanks as memberRank]
                                    '<td><input name="unitMemberPrice_${memberRank.id}" class="text" type="text" style="width: 100px;"/></td>' +
                                    [/#list]
                                    '</tr>' +
                                    '</tbody>' +
                                    '</table>' +
                                    '</div>';
                            $.dialog({
                                title: '批量改价',
                                content: content,
                                width: 1000,
                                onOk: function () {
                                    var pass = true;
                                    var unitePrice = $("input[name='unitPrice']").val().trim();
                                    var $checked = $("#priceForm input[name='ids']:checked");
                                    if ($checked.size() == 0) {
                                        $.message("error", "请选择需要改价的商品");
                                        return false;
                                    }
                                    $.each($checked, function () {
                                        if (pass) {
                                            var $this = $(this);
                                            var price = $this.parent().next().children().val();
                                            var marketPrice = $this.parent().next().children().attr("marketPrice");
                                            var productName = $this.parent().text();
                                            if (unitePrice == "") {
                                                if (isNaN(price)) {
                                                    pass = false;
                                                    $.message("error", "商品（" + productName + "）的销售价输入有误");
                                                } else if (Number(price) > Number(marketPrice)) {
                                                    pass = false;
                                                    $.message("error", "商品（" + productName + "）的销售价不能大于其市场价（" + marketPrice + "元）");
                                                }
                                            } else {
                                                if (isNaN(unitePrice)) {
                                                    pass = false;
                                                    $.message("error", "统一价格（销售价）输入有误");
                                                } else if (Number(unitePrice) > Number(marketPrice)) {
                                                    pass = false;
                                                    $.message("error", "统一价格（销售价）不能大于商品（" + productName + "）的市场价（" + marketPrice + "元）");
                                                }
                                            }
                                            var memberPricePass = true;
                                            $.each($this.parents(".productTr").find(".memberPrice"), function () {
                                                if (memberPricePass) {
                                                    var memberRankId = $(this).attr("memberRankId");
                                                    var memberRankName = $(this).attr("memberRankName");
                                                    var memberPrice = $(this).val().trim();
                                                    var unitMemberPrice = $("[name='unitMemberPrice_" + memberRankId + "']").val().trim();
                                                    if (unitMemberPrice == "") {
                                                        if (memberPrice != "" && isNaN(memberPrice)) {
                                                            memberPricePass = false;
                                                            pass = false;
                                                            $.message("error", "商品（" + productName + "）的" + memberRankName + "价格输入有误");
                                                        }
                                                    } else {
                                                        if (isNaN(unitMemberPrice)) {
                                                            memberPricePass = false;
                                                            pass = false;
                                                            $.message("error", "统一价格（" + memberRankName + "）输入有误");
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    if (!pass) {
                                        return false;
                                    }
                                    $.ajax({
                                        url: "price.jhtml",
                                        data: $("#priceForm").serialize(),
                                        dataType: "json",
                                        type: "get",
                                        success: function (message) {
                                            $.message(message);
                                            if (message.type == "success") {
                                                window.setTimeout(function () {
                                                    window.location.reload();
                                                }, 600);
                                            } else {
                                                return false;
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            $.message(data.message);
                        }
                    }
                });

            });

            //批量上架
            $("#multiUpMarketButton").click(function(){
                var content='<div style="height: 350px;overflow-y: auto;">' +
                        '<form id="spForm">' +
                        '<div style="padding:15px 0;">' +
                        '<span style="padding-left:10px;">选择供应商：</span>' +
                        '<select id="supplierSelect">' +
                        '<option value="">--请选择--</option>' +
                        [#list suppliers as suppler]
                        '<option value="${suppler.parent.id}">${(suppler.parent.name)!}</option>' +
                        [/#list]
                        '</select>' +
                        '</div>' +
                        '<table class="list" style="table-layout: fixed;" id="spTable">' +
                        '<tr>' +
                        '<thead>' +
                        '<th style="padding-left:5px"><input type="checkbox" id="spCheckAll"/>名称</th>' +
                        '<th>上架数量</th>' +
                        '</thead>' +
                        '<tbody id="supplierProduct">' +
                        '<tbody>' +
                        '</tr>' +
                        '</table>' +
                        '</form>' +
                        '</div>';
                $.dialog({
                    title: '批量上架',
                    content: content,
                    width: 600,
                    onOk: function () {
                        var $checked=$("#spTable input[name='ids']:checked");
                        if($checked.size()==0){
                            $.message("error","请选择需要上架的商品");
                            return false;
                        }
                        var flag=true;
                        $.each($checked, function(){
                            var $this=$(this);
                            var stock=$("input[name='upMarketStock_"+$this.val()+"']").val().trim();
                            if(stock===""||isNaN(stock)){
                                flag=false;
                            }
                        });
                        if(!flag){
                            $.message("error","请输入正确的上架数量");
                            return false;
                        }
                        $.ajax({
                            url:'checkFullName.jhtml',
                            data:$("#spTable input[name='fullNames']:enabled:checked").serialize(),
                            dataType:'json',
                            type:'get',
                            success:function(data){
                                console.log(data);
                                if(data.message.type=="success"){
                                    if(data.data.length>0){
                                        var msg='<div style="padding-right: 40px;">' +
                                                '<h2 style="font-size: 16px;font-weight: 400;color: #000;">您的店铺存在下列相同名称的商品，是否上架？</h2>' +
                                                '<span style="display: block;position: relative;margin-top: 5px;line-height: 16px;">' +
                                                '<span style="display: block;">';
                                        $.each(data.data,function(k,v){
                                            msg+="【"+ v +"】";
                                        });
                                        msg+='</span>' +
                                                '</span>' +
                                                '</div>';
                                        $.dialog({
                                            type: "warn",
                                            content: msg,
                                            ok: message("helper.dialog.ok"),
                                            cancel: message("helper.dialog.cancel"),
                                            width: 360,
                                            onOk: function () {
                                                spUpMarket();
                                            }
                                        });
                                    }else if(data.data.length==0){
                                        spUpMarket();
                                    }
                                }else{
                                    $.message(data.message);
                                }

                            }
                        });
                        return false;
                    }
                });
            });
            //========================导出==============================
            $("#export_ss").click(function () {
                $.message("success", "正在帮您导出，请稍后")
                $.ajax({
                    url: "${base}/store/member/product/isMarketableList_export.jhtml",
                    type: "post",
                    data: {
                        productCategoryId: "${productCategoryId}",
                        brandId: "${brandId}",
                        searchValue: "${searchValue}",
                        promotionId: "${promotionId}",
                        tagId: "${tagId}",
                        supplierId: "${supplierId}",
                        isMarketable: "[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]",
                        isList: "[#if isList??]${isList?string("true", "false")}[/#if]",
                        isTop: "[#if isTop??]${isTop?string("true", "false")}[/#if]",
                        isGift: "[#if isGift??]${isGift?string("true", "false")}[/#if]",
                        isOutOfStock: "[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]",
                        isStockAlert: "[#if isStockAlert??]${isStockAlert?string("true", "false")}[/#if]"
                    },
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        var html = '<table style="display:none;" class="table2excel">' +
                                '<thead>' +
                                '<tr>' +
                                '<th>商品名称</th>' +
                                '<th>所属商家</th>' +
                                '<th>所属供应商</th>' +
                                '<th>商品条型码</th>' +
                                '<th>商品编码</th>' +
                                '<th>商品分类</th>' +
                                '<th>所有库存</th>' +
                                '<th>可用库存</th>' +
                                '<th>销售价</th>' +
                                '<th>成本价</th>' +
                                '<th>单位</th>' +
                                '<th>是否上下架</th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>;'
                        $.each(data, function (i, obj) {
                            html +=
                                    '<tr>' +
                                    '<td>' + obj.name + '</td>' +
                                    '<td>' + obj.tenantName + '</td>' +
                                    '<td>' + obj.supplierName + '</td>' +
                                    '<td>' + obj.barcode + '</td>' +
                                    '<td>' + obj.sn + '</td>' +
                                    '<td>' + obj.productCategory + '</td>' +
                                    '<td>' + obj.stock + '</td>' +
                                    '<td>' + obj.availableStock + '</td>' +
                                    '<td>' + obj.price + '</td>' +
                                    '<td>' + obj.cost + '</td>' +
                                    '<td>' + obj.unit + '</td>' +
                                    '<td>' + obj.isMarket + '</td>' +
                                    '</tr>';
                        });
                        html += '</tbody>' +
                                '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                //导出数据到excel
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "商品列表",
                    filename: "商品列表",
                    fileext: ".xls",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true
                });
            });

        });

        function showStocks(id) {
            $.ajax({
                url: 'view.jhtml',
                type: 'get',
                data: {ids: id},
                dataType: 'json',
                success: function (data) {
                    if (data.message.type == "success") {
                        var content = '<div style="height: 260px;overflow-y: auto;">' +
                                '<form id="priceForm"><table class="list" style="table-layout: fixed;">' +
                                '<thead>' +
                                '<tr>' +
                                '<th width="200"><a href="javascript:;">名称</a></th>' +
                                '<th><a href="javascript:;">库存</a></th>' +
                                '<th><a href="javascript:;">可用库存</a></th>' +
                                '<th><a href="javascript:;">锁定库存</a></th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>';
                        $.each(data.data, function (i, products) {
                            $.each(products, function (index, product) {
                                content += '<tr>' +
                                        '<td style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">' + product.fullName + '</td>' +
                                        '<td>' + (Number(product.stock) < 0 ? 0 : product.stock) + '</td>' +
                                        '<td>' + (Number(product.availableStock) < 0 ? 0 : product.availableStock) + '</td>' +
                                        '<td>' + product.allocatedStock + '</td>' +
                                        '<tr/>';
                            });
                        });
                        content += '</tbody>' +
                                '</table>' +
                                '</div>';
                        $.dialog({
                            title: '库存明细',
                            content: content,
                            width: 550,
                            cancel: null
                        });
                    } else {
                        $.message(data.message);
                    }
                }
            });
        }

    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
[#include "/store/member/include/bootstrap_css.ftl" /]
[#--[#include "/store/member/include/bootstrap_js.ftl" /]--]
[#include "/store/member/include/header.ftl" /]
[#include "/store/member/include/menu.ftl" /]
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                我的商品
                <small>查询我发布的宝贝，维护当前售价及库存状态等。</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                <li><a href="${base}/store/member/product/notMarketableList.jhtml">我的商品</a></li>
                <li class="active">商品列表</li>
            </ol>
        </section>
        <section class="content">
            <div class="box">
                <div class="box-body">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs pull-right">
                            <li class="active">
                                <a href="${base}/store/member/product/notMarketablelist.jhtml">已下架商品</a>
                            </li>
                            <li class="">
                                <a href="${base}/store/member/product/isMarketableList.jhtml">已上架商品</a>
                            </li>
                            <li class="pull-left header"><i class="fa fa-th"></i>我的商品</li>
                        </ul>
                        <form id="listForm" action="notMarketablelist.jhtml" method="get">
                            <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}"/>
                            <input type="hidden" id="brandId" name="brandId" value="${brandId}"/>
                            <input type="hidden" id="promotionId" name="promotionId" value="${promotionId}"/>
                            <input type="hidden" id="tagId" name="tagId" value="${tagId}"/>
                            <input type="hidden" id="supplierId" name="supplierId" value="${supplierId}"/>
                            <input type="hidden" id="isMarketable" name="isMarketable"
                                   value="[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]"/>
                            <input type="hidden" id="isList" name="isList"
                                   value="[#if isList??]${isList?string("true", "false")}[/#if]"/>
                            <input type="hidden" id="isTop" name="isTop"
                                   value="[#if isTop??]${isTop?string("true", "false")}[/#if]"/>
                            <input type="hidden" id="isGift" name="isGift"
                                   value="[#if isGift??]${isGift?string("true", "false")}[/#if]"/>
                            <div class="row mtb10">
                                <div class="col-sm-7">
                                    <div class="btn-group">
                                        <button type="button" id="upMarketButton" class="btn btn-default btn-sm"><i
                                                class="fa fa-close mr5"
                                                aria-hidden="true"></i>上架
                                        </button>
                                        <button type="button" id="deleteButton" class="btn btn-default btn-sm"><i
                                                class="fa fa-close mr5"
                                                aria-hidden="true"></i>删除
                                        </button>
                                        <button type="button" class="btn btn-default btn-sm" id="refreshButton"><i class="fa fa-refresh mr5"
                                                                                                                   aria-hidden="true"></i> 刷新
                                        </button>
                                        <button type="button" id="moreButton" class="btn btn-default btn-sm" data-toggle="modal"
                                                data-target="#myModal">更多选项
                                        </button>
                                        <button type="button" id="changePriceButton" class="btn btn-default btn-sm">批量改价</button>
                                        <button type="button" id="multiUpMarketButton" class="btn btn-default btn-sm">批量上架</button>
                                        <button type="button" id="export_ss" class="btn btn-default btn-sm">导出</button>
                                        <div class="dropdown fl ml5">
                                            <button class="btn btn-default btn-sm dropdown-toggle" type="button"
                                                    id="dropdownMenu1" data-toggle="dropdown">
                                                商品筛选
                                                <span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1"
                                                id="filterOption">
                                                <li class="[#if isList?? && isList]active[/#if]">
                                                    <a href="javascript:;" name="isList" val="true"[#if isList?? && isList]
                                                       class="checked"[/#if]>${message("admin.product.isList")}</a>
                                                </li>
                                                <li class="[#if isList?? && !isList]active[/#if]">
                                                    <a href="javascript:;" name="isList"
                                                       val="false"[#if isList?? && !isList]
                                                       class="checked"[/#if]>${message("admin.product.notList")}</a>
                                                </li>
                                                <li class="[#if isTop?? && isTop]active[/#if]">
                                                    <a href="javascript:;" name="isTop" val="true"[#if isTop?? && isTop]
                                                       class="checked"[/#if]>${message("admin.product.isTop")}</a>
                                                </li>
                                                <li class="[#if isTop?? && !isTop]active[/#if]">
                                                    <a href="javascript:;" name="isTop" val="false"[#if isTop?? && !isTop]
                                                       class="checked"[/#if]>${message("admin.product.notTop")}</a>
                                                </li>
                                                <li class="[#if isGift?? && isGift]active[/#if]">
                                                    <a href="javascript:;" name="isGift" val="true"[#if isGift?? && isGift]
                                                       class="checked"[/#if]>${message("admin.product.isGift")}</a>
                                                </li>
                                                <li class="[#if isGift?? && !isGift]active[/#if]">
                                                    <a href="javascript:;" name="isGift"
                                                       val="false"[#if isGift?? && !isGift]
                                                       class="checked"[/#if]>${message("admin.product.nonGift")}</a>
                                                </li>
                                            </ul>
                                        </div>

                                        <div class="dropdown fl ml5">
                                            <button class="btn btn-default btn-sm dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                                                每页显示<span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" id="pageSizeOption">
                                                <li role="presentation" class="[#if page.pageSize==10]active[/#if]">
                                                    <a role="menuitem" tabindex="-1" val="10">10</a>
                                                </li>
                                                <li role="presentation" class="[#if page.pageSize==20]active[/#if]">
                                                    <a role="menuitem" tabindex="-1" val="20">20</a>
                                                </li>
                                                <li role="presentation" class="[#if page.pageSize==30]active[/#if]">
                                                    <a role="menuitem" tabindex="-1" val="30">30</a>
                                                </li>
                                                <li role="presentation" class="[#if page.pageSize==40]active[/#if]">
                                                    <a role="menuitem" tabindex="-1" val="40">40</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-5">
                                    <div class="box-tools fr">
                                        <div class="input-group input-group-sm" style="width: 150px;">
                                            <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" class="form-control pull-right"
                                                   placeholder="搜索商品名称、货号">
                                            <div class="input-group-btn">
                                                <button type="submit" class="btn btn-default"><i class="fa fa-search"></i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </div>
                            <table id="listTable" class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th class="check">
                                        <input type="checkbox" id="selectAll"/>
                                    </th>
                                    <th>编号</th>
                                [#if versionType=="1"]
                                    <th>
                                        <a href="javascript:;" class="sort">条形码</a>
                                    </th>
                                [/#if]
                                    <th>名称</th>
                                    <th>图片</th>
                                    <th>商品分类</th>
                                    <th>销售价</th>
                                    <th>库存</th>
                                    <th>上架</th>
                                    <th>${message("admin.common.handle")}</th>
                                </tr>
                                </thead>
                                <tbody>
                                [#list page.content as product]
                                <tr>
                                    <td>
                                        <input type="checkbox" name="ids" value="${product.id}"/>
                                    </td>
                                    <td>
                                    ${product.sn}
                                    </td>
                                    [#if versionType=="1"]
                                        <td>
                                        ${(product.barcode)!}
                                        </td>
                                    [/#if]
                                    <td>
								<span title="${product.fullName}">
									${abbreviate(product.fullName, 30, "..")}
                                        [#if product.isGift]
                                            <span class="gray">[${message("admin.product.gifts")}]</span>
                                        [/#if]
								</span>
                                        [#list product.validPromotions as promotion]
                                            <span class="promotion">${abbreviate(promotion.name,10,".")}</span>
                                        [/#list]
                                    </td>
                                    <td>
                                        [#if product.thumbnail?has_content]
                                            <img src="${product.thumbnail}" alt="" width="30px" height="30px"/>
                                        [/#if]
                                    </td>
                                    <td>
                                        [#if product.productCategoryTenant??]${abbreviate(product.productCategoryTenant.name,16,"..")!}[/#if]
                                    </td>
                                    <td>
                                    ${currency(product.price)}
                                    </td>
                                    <td style="cursor: pointer;" title="点击查看库存明细" onclick="showStocks('${product.id}')">
                                    ${(product.goods.stock)!}
                                    </td>
                                    <td>
                                        <i class="fa fa-remove" style="color: #ff6600"></i>
                                    </td>
                                    <td>
                                        [@helperRole url="store/member/product/notMarketablelist.jhtml" type="upMarket"]
                                            [#if helperRole.retOper!="0"]
                                                <a href="javascript:;" class="upMarketable" productId="${product.id}">[上架]</a>
                                            [/#if]
                                        [/@helperRole]

                                        [@helperRole url="store/member/product/notMarketablelist.jhtml" type="update"]
                                            [#if helperRole.retOper!="0"]
                                                <a href="${base}/store/member/product/edit.jhtml?id=${product.id}">[${message("shop.common.edit")}]</a>
                                            [/#if]
                                        [/@helperRole]
                                        <a target="_blank"
                                           href="${base}/[#if versionType=="1"]b2b[#else]b2c[/#if]/product/detail/${product.id}.jhtml">[查看]</a>
                                    </td>
                                </tr>
                                [/#list]
                                </tbody>
                            </table>
                            <div class="dataTables_paginate paging_simple_numbers">
                            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/store/member/include/pagination.ftl"]
                    [/@pagination]
                            </div>
                        </form>
                    </div>
                </div>
            </div>
[#include "/store/member/include/footer.ftl" /]
    </div>

<div id="trade_wrap"></div>
</body>
</html>
